package de.bund.bfr.knime.fsklab.nodes

import com.sun.jna.Platform
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceInitializer
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceProvider
import org.knime.core.node.KNIMEConstants
import org.knime.core.node.NodeLogger
import org.knime.core.util.FileUtil
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
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


