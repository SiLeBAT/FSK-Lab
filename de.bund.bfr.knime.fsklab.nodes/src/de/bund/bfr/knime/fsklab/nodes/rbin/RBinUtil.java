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
package de.bund.bfr.knime.fsklab.nodes.rbin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.knime.core.node.KNIMEConstants;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;

import com.sun.jna.Platform;

import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.DefaultRPreferenceProvider;
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceInitializer;
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceProvider;

/**
 * Utility class with methods to call R binary.
 *
 * @author Heiko Hofer.
 */
public class RBinUtil {

	/**
	 * The temp directory used as a working directory for R.
	 */
	private static final String TEMP_PATH = KNIMEConstants.getKNIMETempDir().replace('\\', '/');

	private final static NodeLogger LOGGER = NodeLogger.getLogger(RBinUtil.class);

	/**
	 * Exception thrown when the specified R_HOME directory is invalid.
	 *
	 * @author Jonathan Hale
	 */
	public static class InvalidRHomeException extends Exception {

		/**
		 * Generated serialVersionUID
		 */
		private static final long serialVersionUID = -69909100381242901L;

		/**
		 * Constructor
		 *
		 * @param msg
		 *            error message
		 */
		private InvalidRHomeException(final String msg) {
			super(msg);
		}
	}

	/**
	 * Get properties about the used R.
	 *
	 * @return properties about used R.
	 * @throws IOException
	 *             in case that running R fails.
	 */
	public static Properties retrieveRProperties() throws IOException {
		return retrieveRProperties(RPreferenceInitializer.getR3Provider());
	}

	/**
	 * Get properties about the used R installation.
	 *
	 * @param rpref
	 *            provider for path to R executable
	 * @return properties about used R
	 */
	public static Properties retrieveRProperties(final RPreferenceProvider rpref) {
		final File tmpPath = new File(TEMP_PATH);
		File propsFile;
		File rOutFile;

		try {
			propsFile = FileUtil.createTempFile("R-propsTempFile-", ".r", true);
			rOutFile = FileUtil.createTempFile("R-propsTempFile-", ".Rout", tmpPath, true);
		} catch (IOException e2) {
			LOGGER.error("Could not create temporary files for R execution.");
			return new Properties();
		}

		final String propertiesPath = propsFile.getAbsolutePath().replace('\\', '/');
		final String script = "setwd('" + tmpPath.getAbsolutePath().replace('\\', '/') + "')\n"
				+ "foo <- paste(names(R.Version()), R.Version(), sep='=')\n"
				+ "foo <- append(foo, paste('memory.limit', memory.limit(), sep='='))\n"
				+ "foo <- append(foo, paste('Rserve.path', find.package('Rserve', quiet=TRUE), sep='='))\n"
				+ "foo <- append(foo, paste('miniCRAN.path', find.package('miniCRAN', quiet=TRUE), sep='='))\n"
				+ "foo <- append(foo, paste('Cairo.path', find.package('Cairo', quiet=TRUE), sep='='))\n"
				+ "foo <- append(foo, paste('rhome', R.home(), sep='='))\n" //
				+ "write(foo, file='" + propertiesPath + "', ncolumns=1, append=FALSE, sep='\\n')\nq()";

		File rCommandFile;
		try {
			rCommandFile = writeRCommandFile(script);
		} catch (IOException e1) {
			LOGGER.error("Could not write R command file.");
			return new Properties();
		}
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(rpref.getRBinPath("Rscript").toString(), "--vanilla", rCommandFile.getName(), rOutFile.getName());
		builder.directory(rCommandFile.getParentFile());

		/** Run R on the script to get properties */
		try {
			final Process process = builder.start();
			final BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			final BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			// Consume the output produced by the R process, otherwise may block
			// process on some operating
			// system
			new Thread(() -> {
				try {
					final StringBuilder b = new StringBuilder();
					String line;
					while ((line = outputReader.readLine()) != null) {
						b.append(line);
					}
					LOGGER.debug("External RScript process output: " + b.toString());
				} catch (Exception e) {
					LOGGER.error("Error reading output of external R process.", e);
				}
			}, "R Output Reader").start();

			new Thread(() -> {
				try {
					final StringBuilder b = new StringBuilder();
					String line;
					while ((line = errorReader.readLine()) != null) {
						b.append(line);
					}
					LOGGER.debug("External Rscript process error output: " + b.toString());
				} catch (Exception e) {
					LOGGER.error("Error reading error output of external R process.", e);
				}
			}, "R Error Reader").start();

			process.waitFor();
		} catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			return new Properties();
		}

		// load properties from propsFile
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(propsFile)) {
			props.load(fis);
		} catch (IOException e) {
			LOGGER.warn("Could not retrieve properties from R.", e);
		}

		return props;
	}

	/**
	 * Writes the given string into a file and returns it.
	 *
	 * @param cmd
	 *            The string to write into a file.
	 * @return The file containing the given string.
	 * @throws IOException
	 *             If string could not be written to a file.
	 */
	private static File writeRCommandFile(final String cmd) throws IOException {
		File tempCommandFile = FileUtil.createTempFile("R-readPropsTempFile-", ".r", new File(TEMP_PATH), true);
		try (FileWriter fw = new FileWriter(tempCommandFile)) {
			fw.write(cmd);
		}

		return tempCommandFile;
	}

	/**
	 * @param rHomePath
	 * @throws InvalidRHomeException
	 */
	public static void checkRHome(final String rHomePath) throws InvalidRHomeException {
		checkRHome(rHomePath, false);
	}

	/**
	 * Checks whether the given path is a valid R_HOME directory. It checks the
	 * presence of the bin and library folder.
	 *
	 * @param rHomePath
	 *            path to R_HOME.
	 * @param fromPreferences
	 *            Set to true if this function is called from the R preference
	 *            page.
	 * @throws InvalidRHomeException
	 *             If the specified R_HOME path is invalid.
	 */
	public static void checkRHome(final String rHomePath, final boolean fromPreferences) throws InvalidRHomeException {

		final Path rHome = Paths.get(rHomePath);
		final String msgSuffix = ((fromPreferences) ? ""
				: " R_HOME ('" + rHomePath + "')" + " is meant to be the path to the folder which is the root of R's "
						+ "installation tree. \nIt contains a 'bin' folder which itself contains the R executable and a "
						+ "'library' folder. Please change the R settings in the preferences.");
		final String R_HOME_NAME = (fromPreferences) ? "Path to R Home" : "R_HOME";

		/* check if the directory exists. */
		if (Files.notExists(rHome)) {
			throw new InvalidRHomeException(R_HOME_NAME + " does not exist." + msgSuffix);
		}

		/* Make sure R home is not a file. */
		if (!Files.isDirectory(rHome)) {
			throw new InvalidRHomeException(R_HOME_NAME + " is not a directory." + msgSuffix);
		}

		/* Check if there is a bin directory. */
		final Path binDir = rHome.resolve("bin");
		if (!Files.isDirectory(binDir)) {
			throw new InvalidRHomeException(R_HOME_NAME + " does not contain a folder with name 'bin'." + msgSuffix);
		}

		/* Check if there is an R Executable. */
		final Path rExecutable = new DefaultRPreferenceProvider(rHomePath).getRBinPath("R");
		if (Files.notExists(rExecutable)) {
			throw new InvalidRHomeException(R_HOME_NAME + " does not contain an R executable." + msgSuffix);
		}

		/* Make sure there is a library directory. */
		final Path libraryDir = rHome.resolve("library");
		if (!Files.isDirectory(libraryDir)) {
			throw new InvalidRHomeException(
					R_HOME_NAME + " does not contain a folder with name 'library'." + msgSuffix);
		}

		/*
		 * On Windows, we expect the appropiate platform-specific folders
		 * corresponding to out Platform.
		 */
		if (Platform.isWindows()) {
			if (Platform.is64Bit()) {
				final Path x64Path = binDir.resolve("x64");
				final Path i386Path = binDir.resolve("i386");
				
				if (!Files.isDirectory(x64Path) && !Files.isDirectory(i386Path)) {
					throw new InvalidRHomeException(R_HOME_NAME
							+ " does not contain a folder with name 'bin\\x64'. Please install R 64-bit files."
							+ msgSuffix);
				}
			} else {
				final Path i386Path = binDir.resolve("i386");
				if (!Files.isDirectory(i386Path)) {
					throw new InvalidRHomeException(R_HOME_NAME
							+ " does not contain a folder with name '\\bin\\i386'. Please install R 32-bit files."
							+ msgSuffix);
				}
			}
		}
	}
}
