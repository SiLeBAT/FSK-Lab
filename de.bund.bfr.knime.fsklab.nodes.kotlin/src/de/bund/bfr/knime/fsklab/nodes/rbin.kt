package de.bund.bfr.knime.fsklab.nodes

import com.sun.jna.Platform
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceInitializer
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceProvider
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer
import org.eclipse.jface.preference.DirectoryFieldEditor
import org.eclipse.jface.preference.FieldEditorPreferencePage
import org.eclipse.jface.resource.JFaceResources
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.IWorkbench
import org.eclipse.ui.IWorkbenchPreferencePage
import org.knime.core.node.KNIMEConstants
import org.knime.core.node.NodeLogger
import org.knime.core.util.FileUtil
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Properties
import kotlin.concurrent.thread

private val TEMP_PATH = KNIMEConstants.getKNIMETempDir().replace("\\", "/")
private val LOGGER = NodeLogger.getLogger("RBinUtil")

/**
 * Exception thrown when the specified R_HOME directory is invalid.
 * @param message error message
 * @author Jonathan Hale
 * @author Miguel de Alba
 */
class InvalidRHomeException(message: String) : Exception(message)

/**
 * @return properties about the used R.
 * @throws IOException in case that running R fails.
 */
fun retrieveRProperties(): Properties = retrieveRProperties(RPreferenceInitializer.getR3Provider())

/**
 * @return properties about the use R installation.
 * @param rpref provider for path to R executable
 */
fun retrieveRProperties(rpref: RPreferenceProvider): Properties {

	val tmpPath = File(TEMP_PATH)
	val propsFile: File
	val rOutFile: File

	try {
		propsFile = FileUtil.createTempFile("R-propsTempFile-", ".r", true)
		rOutFile = FileUtil.createTempFile("R-propsTempFile-", ".Rout", tmpPath, true)
	} catch (_: IOException) {
		LOGGER.error("Could not create temporary files for R execution.")
		return Properties()
	}

	val propertiesPath = propsFile.absolutePath.replace("\\", "/")
	val script = """
		|setwd('${tmpPath.absolutePath.replace("\\", "/")}')
		|foo <- paste(names(R.Version()), R.Version(), sep='=')
		|foo <- append(foo, paste('memory.limit', memory.limit(), sep='='))
		|foo <- append(foo, paste('Rserve.path', find.package('Rserve', quiet=TRUE), sep='='))
		|foo <- append(foo, paste('miniCRAN.path', find.package('miniCRAN', quiet=TRUE), sep='='))
		|foo <- append(foo, paste('Cairo.path', find.package('Cairo', quiet=TRUE), sep='='))
		|foo <- append(foo, paste('rhome', R.home(), sep='='))
		|write(foo, file='$propertiesPath', ncolumns=1, append=FALSE, sep='\\n')
		|q()
		""".trimMargin()

	val rCommandFile: File
	try {
		rCommandFile = writeRCommandFile(script)
	} catch (_: IOException) {
		LOGGER.error("Could not write R command file")
		return Properties()
	}

	val builder = ProcessBuilder()
	// TODO: rCommandFile
	builder.command(rpref.getRBinPath("Rscript"), "--vanilla", rCommandFile.name, rOutFile.name)
	builder.directory(rCommandFile.parentFile)

	/** Run R on the script to get properties. */
	try {
		val process = builder.start()
		val outputReader = BufferedReader(InputStreamReader(process.inputStream))
		val errorReader = BufferedReader(InputStreamReader(process.errorStream))

		// Consume the output produced by the R process, otherwise may block
		// process on some operating system
		thread(start = true, name = "R Output Reader") {
			try {
				val output = outputReader.readText()
				LOGGER.debug("External RScript process output: $output")
			} catch (e: Exception) {
				LOGGER.error("Error reading output of external R process.", e)
			}
		}

		thread(start = true, name = "R Error Reader") {
			try {
				val output = errorReader.readText()
				LOGGER.debug("External RScript process error output: $output")
			} catch (e: Exception) {
				LOGGER.error("Error reading error output of external R process.", e)
			}
		}

		process.waitFor()
	} catch (e: Exception) {
		LOGGER.debug(e.message, e)
		return Properties()
	}

	// load properties from propsFile
	val props = Properties()
	try {
		val fis = FileInputStream(propsFile)
		props.load(fis)
		fis.close()
	} catch (e: IOException) {
		LOGGER.warn("Could not retrieve properties from R.", e)
	}

	return props
}

/**
 * Writes the given string into a file and returns it.
 * @param cmd The string to write into a file
 * @return The file containing the given string.
 * @throws IOException If string could not be written to a file.
 */
fun writeRCommandFile(cmd: String): File {
	val tempCommandFile = FileUtil.createTempFile("R-readPropsTempFile-", ".r", File(TEMP_PATH), true)
	FileWriter(tempCommandFile).use { writer -> writer.write(cmd) }

	return tempCommandFile
}

/**
 * @param rHomePath
 * @throws InvalidRHomeException
 */
fun checkRHome(rHomePath: String) = checkRHome(rHomePath, false)

/**
 * Checks whether the given path is a valid R_HOME directory. It checks the
 * presence of the bin and library folder.
 *
 * @param rHomePath path to R_HOME
 * @param fromPreferences Set to true if this function is called from the R preference
 * @throws InvalidRHomeException If the specified R_HOME path is invalid
 */
fun checkRHome(rHomePath: String, fromPreferences: Boolean) {

	val rHome = File(rHomePath)
	val msgSuffix = if (fromPreferences) "" else """
		| R_HOME ('$rHomePath') is meant to be the path to the folder which is the root of R's installation tree.
		| It contains a 'bin' folder which itself contains the R executable and a 'library' folder. Please change the R settings in the preferences.
		""".trimMargin()
	val R_HOME_NAME = if (fromPreferences) "Path to R Home" else "R_HOME"

	/* check if the directory exists. */
	if (!rHome.exists()) {
		throw InvalidRHomeException("$R_HOME_NAME does not exist.$msgSuffix")
	}

	/* Make sure R home is not a file. */
	if (!rHome.isDirectory()) {
		throw InvalidRHomeException("$R_HOME_NAME is not a directory.$msgSuffix")
	}

	/* Check if there is a bin directory. */
	val binDir = File(rHome, "bin")
	if (!binDir.isDirectory) {
		throw InvalidRHomeException("$R_HOME_NAME does not containa folder with name 'library'.$msgSuffix")
	}

	/*
     * On Windows, we expect the appropiate platform-specific folders
     * corresponding to out Platform.
     */
	if (Platform.isWindows()) {
		if (Platform.is64Bit()) {
			val expectedFolder = File(binDir, "x64")
			val expectedFolder2 = File(binDir, "i386")
			if (!expectedFolder.isDirectory && !expectedFolder2.isDirectory) {
				throw InvalidRHomeException("$R_HOME_NAME does not contain a folder with name 'bin\\x64'. " +
						"Please install R 64-bit files.$msgSuffix")
			}
		} else {
			val expectedFolder = File(binDir, "i386")
			if (!expectedFolder.isDirectory) {
				throw InvalidRHomeException("$R_HOME_NAME does not contain a folder with name '\\bin\\i386'. " +
						"Please install R 32-bit files.$msgSuffix")
			}
		}
	}
}

/**
 * Class that provides a path to an R home directory
 * @author Heiko Hofer
 * @author Jonathan Hale
 * @author Miguel de Alba
 */
interface RPreferenceProvider {

	/** Path to RHome. */
	fun getRHome(): String

	/**
	 * @param command R command ("R" or "Rscript" for example)
	 * @return path to the command executable
	 */
	fun getRBinPath(command: String): String

	/** @return path to an Rserve executable */
	fun getRServeBinPath(): String
}

/**
 * Default provider for R preferences. It determines the R binary path based on
 * the R home given in the constructor.
 * @property rHome R's home directory
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @author Jonathan Hale
 * @author Miguel de Alba
 */
class DefaultRPreferenceProvider(home: String) : RPreferenceProvider {

	private val rHome = home

	/** Properties for this provider. */
	val properties by lazy { retrieveRProperties(this) }

	override fun getRHome() = rHome

	override fun getRBinPath(command: String): String {

		val binPath = Paths.get(getRHome(), "bin")

		return if (Platform.isWindows()) {
			val arch = if (Platform.is64Bit()) "x64" else "i386"
			"${binPath.resolve(arch)}${File.separator}$command.exe"
		} else "$binPath{File.separator}$command"
	}

	override fun getRServeBinPath(): String {

		val rservePath = Paths.get(properties.getProperty("Rserve.path"))
		val rserveLibs = rservePath.resolve("libs")

		return if (Platform.isWindows()) {
			val arch = if (Platform.is64Bit()) "x64" else "i386"
			rserveLibs.resolve("$arch/RServe.exe").toString()
		} else rserveLibs.resolve("Rserve.dbg").toString()
	}
}

val R3_PATH = "r3.path"
private var cachedR3PreferenceProvider: RPreferenceProvider? = null

/* @return provider to the path to the R3 executable. */
fun getR3Provider(): RPreferenceProvider {

	val r3Home = FskPlugin.getDefault().preferenceStore.getString(R3_PATH)

	val localProvider = cachedR3PreferenceProvider
	if (localProvider == null || localProvider.rHome != r3Home) {
		cachedR3PreferenceProvider = DefaultRPreferenceProvider(r3Home)
	}

	return cachedR3PreferenceProvider!!
}

/**
 * Invalidate the cached R3 preference provider to refetch R properties
 * (which launches an external R command).
 */
fun invalidateR3PreferenceProviderCache() {
	cachedR3PreferenceProvider = null
}

/**
 * Initializes preference page with defualt paths to R2 and R3 environments.
 * @author Miguel de Alba
 */
class RPreferenceInitializer : AbstractPreferenceInitializer() {

	override fun initializeDefaultPreferences(): Unit {
		FskPlugin.getDefault().preferenceStore.setDefault("R3_PATH", "")
	}
}

/**
 * Preferences page.
 * @author Miguel de Alba
 */
class RPreferencePage : FieldEditorPreferencePage(GRID), IWorkbenchPreferencePage {

	init {
		preferenceStore = FskPlugin.getDefault().preferenceStore
		description = "BfR R nodes preferences"
	}

	override fun createFieldEditors() {
		addField(RHomeDirectoryFieldEditor(name = R3_PATH, labelText = "R v3 environment location",
				parent = fieldEditorParent))
	}

	override fun init(workbench: IWorkbench) = Unit

	private inner class RHomeDirectoryFieldEditor(name: String, labelText: String, parent: Composite) :
			DirectoryFieldEditor() {

		init {
			init(name, labelText)
			setChangeButtonText(JFaceResources.getString("openBrowse"))
			setValidateStrategy(VALIDATE_ON_KEY_STROKE)
			createControl(parent)
		}
		
		override fun doCheckState(): Boolean {
			val rHome = stringValue
			
			val rHomePath = Paths.get(rHome)
			if (!Files.isDirectory(rHomePath)) {
				setMessage("The selected path is not a directory.", ERROR)
				return false
			}
			
			try {
				checkRHome(rHomePath = rHome, fromPreferences = true)
				
				val props = DefaultRPreferenceProvider(rHome).properties
				// the version numbers may contain spaces
				val version = (props.getProperty("major") + "." + props.getProperty("minor")).replace(" ", "")
				
				if ("3.1.0" == version) {
					setMessage("You have selected an R 3.1.0 installation. " +
							"Please see http://tech.knime.org/faq#q26 for details.", WARNING)
					return true
				}
				
				if (props.getProperty("Rserve.path").isNullOrEmpty()) {
					setMessage("The package 'Rserve' needs to be installed in your R installation. " +
							"Please install it in R using \"install.packages('Rserve')\".", WARNING)
					return true
				}
				
				if (Platform.isMac() && props.getProperty("Cairo.path").isNullOrEmpty()) {
					// Under Mac we need Cairo package to use png()/bmp() devices
					setMessage("The package 'Cairo' needs to be installed in your R installation for " +
							"bitmap graphics devices to work properly. Please install it in R using " +
							"\"install.packages('Cairo')\".", WARNING)
					return true
				}
				
				setMessage(null, NONE)
				return true
			} catch (e: InvalidRHomeException) {
				setMessage(e.message, ERROR)
				return false
			}
		}
	}
}

