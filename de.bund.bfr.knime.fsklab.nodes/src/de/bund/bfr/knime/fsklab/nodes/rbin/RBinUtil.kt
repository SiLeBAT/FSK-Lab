/*
 * ------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   17.09.2007 (thiel): created
 */
package de.bund.bfr.knime.fsklab.nodes.rbin

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.util.Properties

import com.sun.jna.Platform
import org.knime.core.node.KNIMEConstants
import org.knime.core.node.NodeLogger
import org.knime.core.util.FileUtil

import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceInitializer
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceProvider

/**
 * Utility class with methods to call R binary.
 *
 * @author Heiko Hofer.
 */
object RBinUtil {

    /**
     * The temp directory used as a working directory for R.
     */
    private val TEMP_PATH = KNIMEConstants.getKNIMETempDir().replace('\\', '/')

    private val LOGGER = NodeLogger.getLogger(RBinUtil::class.java)

    /**
     * Exception thrown when the specified R_HOME directory is invalid.
     *
     * @author Jonathan Hale
     */
    class InvalidRHomeException
    /**
     * Constructor
     *
     * @param msg
     * error message
     */
    internal constructor(msg: String) : Exception(msg) {
        companion object {

            /**
             * Generated serialVersionUID
             */
            private val serialVersionUID = -69909100381242901L
        }
    }

    /**
     * Get properties about the used R installation.
     *
     * @param rpref
     * provider for path to R executable
     * @return properties about used R
     */
    @JvmOverloads
    fun retrieveRProperties(rpref: RPreferenceProvider = RPreferenceInitializer.r3Provider): Properties {
        val tmpPath = File(TEMP_PATH)
        val propsFile: File
        val rOutFile: File

        try {
            propsFile = FileUtil.createTempFile("R-propsTempFile-", ".r", true)
            rOutFile = FileUtil.createTempFile("R-propsTempFile-", ".Rout", tmpPath, true)
        } catch (e2: IOException) {
            LOGGER.error("Could not create temporary files for R execution.")
            return Properties()
        }

        val propertiesPath = propsFile.absolutePath.replace('\\', '/')
        val script = """
            | setwd('${tmpPath.absolutePath.replace('\\', '/')}')
            | foo <- paste(names(R.Version()), R.Version(), sep='=')
            | foo <- append(foo, paste('memory.limit', memory.limit(), sep='='))
            | foo <- append(foo, paste('Rserve.path', find.package('Rserve', quiet=TRUE), sep='='))
            | foo <- append(foo, paste('miniCRAN.path', find.package('miniCRAN', quiet=TRUE), sep='='))
            | foo <- append(foo, paste('Cairo.path', find.package('Cairo', quiet=TRUE), sep='='))
            | foo <- append(foo, paste('rhome', R.home(), sep='='))
            | write(foo, file='$propertiesPath', ncolumns=1, append=FALSE, sep='\\n')
            | q()
            """.trimMargin()


        val rCommandFile: File = try {
            writeRCommandFile(script)
        } catch (e1: IOException) {
            LOGGER.error("Could not write R command file.")
            return Properties()
        }

        val builder = ProcessBuilder()
        builder.command(rpref.getRBinPath("Rscript"), "--vanilla", rCommandFile.name, rOutFile.name)
        builder.directory(rCommandFile.parentFile)

        /** Run R on the script to get properties  */
        try {
            val process = builder.start()
            val outputReader = BufferedReader(InputStreamReader(process.inputStream))
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))

            // Consume the output produced by the R process, otherwise may block
            // process on some operating system
            Thread({
                try {
                    val b = outputReader.readText()
                    LOGGER.debug("External RScript process output: $b")
                } catch (e: Exception) {
                    LOGGER.error("Error reading output of external R process. $e")
                }
            }, "R Output Reader").start()

            Thread({
                try {
                    val b = errorReader.readText()
                    LOGGER.debug("External Rscript process error output: $b")
                } catch (e: Exception) {
                    LOGGER.error("Error reading error output of external R process. $e")
                }
            }, "R Error Reader").start()

            process.waitFor()
        } catch (e: Exception) {
            LOGGER.debug(e.message, e)
            return Properties()
        }

        // load properties from propsFile
        val props = Properties()
        try {
            FileInputStream(propsFile).use { fis -> props.load(fis) }
        } catch (e: IOException) {
            LOGGER.warn("Could not retrieve properties from R.", e)
        }

        return props
    }

    /**
     * Writes the given string into a file and returns it.
     *
     * @param cmd
     * The string to write into a file.
     * @return The file containing the given string.
     * @throws IOException
     * If string could not be written to a file.
     */
    @Throws(IOException::class)
    private fun writeRCommandFile(cmd: String): File {
        val tempCommandFile = FileUtil.createTempFile("R-readPropsTempFile-", ".r", File(TEMP_PATH), true)
        FileWriter(tempCommandFile).use { fw -> fw.write(cmd) }

        return tempCommandFile
    }

    /**
     * Checks whether the given path is a valid R_HOME directory. It checks the
     * presence of the bin and library folder.
     *
     * @param rHomePath
     * path to R_HOME.
     * @param fromPreferences
     * Set to true if this function is called from the R preference
     * page.
     * @throws InvalidRHomeException
     * If the specified R_HOME path is invalid.
     */
    @Throws(InvalidRHomeException::class)
    @JvmOverloads
    fun checkRHome(rHomePath: String, fromPreferences: Boolean = false) {
        val rHome = File(rHomePath)
        val msgSuffix = if (fromPreferences)
            ""
        else
            """
                | R_HOME ('$rHomePath') is meant to be the path to the folder which is the root of R's installation tree. \n
                | It contains a 'bin' folder which itself contains the R executable and a 'library' folder. \n
                | Please change the R settings in the preferences.
                """.trimMargin()
        val R_HOME_NAME = if (fromPreferences) "Path to R Home" else "R_HOME"

        /* check if the directory exists. */
        if (!rHome.exists()) {
            throw InvalidRHomeException(R_HOME_NAME + " does not exist." + msgSuffix)
        }

        /* Make sure R home is not a file. */
        if (!rHome.isDirectory) {
            throw InvalidRHomeException(R_HOME_NAME + " is not a directory." + msgSuffix)
        }

        /* Check if there is a bin directory. */
        val binDir = File(rHome, "bin")
        if (!binDir.isDirectory) {
            throw InvalidRHomeException(R_HOME_NAME + " does not contain a folder with name 'bin'." + msgSuffix)
        }

        /* Check if there is an R Executable. */
        val rExecutable = File(RPreferenceProvider(rHomePath).getRBinPath("R"))
        if (!rExecutable.exists()) {
            throw InvalidRHomeException(R_HOME_NAME + " does not contain an R executable." + msgSuffix)
        }

        /* Make sure there is a library directory. */
        val libraryDir = File(rHome, "library")
        if (!libraryDir.isDirectory) {
            throw InvalidRHomeException(
                    R_HOME_NAME + " does not contain a folder with name 'library'." + msgSuffix)
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
                    throw InvalidRHomeException(R_HOME_NAME
                            + " does not contain a folder with name 'bin\\x64'. Please install R 64-bit files."
                            + msgSuffix)
                }
            } else {
                val expectedFolder = File(binDir, "i386")
                if (!expectedFolder.isDirectory) {
                    throw InvalidRHomeException(R_HOME_NAME
                            + " does not contain a folder with name '\\bin\\i386'. Please install R 32-bit files."
                            + msgSuffix)
                }
            }
        }
    }
}