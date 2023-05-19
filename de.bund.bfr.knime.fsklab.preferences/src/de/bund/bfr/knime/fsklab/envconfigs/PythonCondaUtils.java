package de.bund.bfr.knime.fsklab.envconfigs;

/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
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
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
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
 *   Mar 2, 2022 (benjamin): created
 */

import java.io.IOException;
import java.util.List;

import org.knime.conda.Conda;
import org.knime.conda.CondaCanceledExecutionException;
import org.knime.conda.CondaEnvironmentCreationMonitor;
import org.knime.conda.CondaEnvironmentIdentifier;
import org.knime.core.util.Version;
import org.knime.python2.CondaPythonCommand;
import org.knime.python2.PythonCommand;
import org.knime.python2.PythonVersion;

import de.bund.bfr.knime.fsklab.envconfigs.CondaEnvironments;
import de.bund.bfr.knime.fsklab.preferences.RPythonVersion;

/**
 * Utilities for accessing Conda functionallity of the {@link Conda} class for Python.
 *
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Benjamin Wilhelm, KNIME GmbH, Konstanz, Germany
 */
public final class PythonCondaUtils {

    private static final String DEFAULT_PYTHON2_ENV_PREFIX = "py2_knime";

    private static final String DEFAULT_PYTHON3_ENV_PREFIX = "py3_knime";
    private static final String DEFAULT_R_ENV_PREFIX = "r_knime";
    private PythonCondaUtils() {
    }

    /**
     * Creates and returns a {@link PythonCommand} that describes a Python process of the given Python version that is
     * run in the Conda environment identified by the given Conda installation directory and the given Conda environment
     * name.<br>
     * The validity of the given arguments is not tested.
     *
     * @param pythonVersion The Python version of the Python environment.
     * @param condaInstallationDirectoryPath The path to the directory of the Conda installation.
     * @param environmentDirectoryPath The path to the directory of the Conda environment. The directory does not
     *            necessarily need to be located inside the Conda installation directory, which is why a path is
     *            required.
     * @return A command to start a Python process in the given environment using the given Conda installation.
     */
    public static PythonCommand createPythonCommand(final RPythonVersion rPythonVersion,
        final String condaInstallationDirectoryPath, final String environmentDirectoryPath) {
    	PythonVersion pythonVersion = rPythonVersion.getId().equals("python2") ? PythonVersion.PYTHON2 : PythonVersion.PYTHON3;
        return new CondaPythonCommand(pythonVersion, condaInstallationDirectoryPath, environmentDirectoryPath);
    }

    /**
     * @param conda the {@link Conda} instance to use
     * @param suffix a suffix for the environment name
     * @return A name for a Python 2 environment. It is ensured that the name does not already exist in this Conda
     *         installation.
     * @throws IOException If an error occurs during execution of the underlying Conda commands.
     */
    public static String getPython2EnvironmentName(final Conda conda, final String suffix) throws IOException {
        return getDefaultPythonEnvironmentName(conda, RPythonVersion.PYTHON2, suffix);
    }

    /**
     * @param conda the {@link Conda} instance to use
     * @param suffix a sufix for the environment name
     * @return A name for a Python 3 environment. It is ensured that the name does not already exist in this Conda
     *         installation.
     * @throws IOException If an error occurs during execution of the underlying Conda commands.
     */
    public static String getPython3EnvironmentName(final Conda conda, final String suffix) throws IOException {
        return getDefaultPythonEnvironmentName(conda, RPythonVersion.PYTHON3, suffix);
    }
    /**
     * @param conda the {@link Conda} instance to use
     * @param suffix a sufix for the environment name
     * @return A name for a R environment. It is ensured that the name does not already exist in this Conda
     *         installation.
     * @throws IOException If an error occurs during execution of the underlying Conda commands.
     */
    public static String getREnvironmentName(final Conda conda, final String suffix) throws IOException {
        return getDefaultPythonEnvironmentName(conda, RPythonVersion.R, suffix);
    }
    private static String getDefaultPythonEnvironmentName(final Conda conda, final RPythonVersion pythonVersion,
        final String suffix) throws IOException {
        final String environmentPrefix =
            (pythonVersion == RPythonVersion.PYTHON2 ? DEFAULT_PYTHON2_ENV_PREFIX :
            	(pythonVersion == RPythonVersion.PYTHON3 ? DEFAULT_PYTHON3_ENV_PREFIX :
            		DEFAULT_R_ENV_PREFIX ))
                + (suffix.isEmpty() ? "" : ("_" + suffix));
        String environmentName = environmentPrefix;
        long possibleEnvironmentSuffix = 1;
        final List<String> environmentNames = conda.getEnvironmentNames();
        while (environmentNames.contains(environmentName)) {
            environmentName = environmentPrefix + "_" + possibleEnvironmentSuffix;
            possibleEnvironmentSuffix++;
        }
        return environmentName;
    }

    /**
     * Creates a new predefined Conda environment of the given Python version that contains all packages required by the
     * KNIME Python integration.
     *
     * @param conda the {@link Conda} instance to use
     * @param environmentName The name of the environment. Must not already exist in this Conda installation. May be
     *            {@code null} or empty in which case a default name is used.
     * @param pythonVersion The Python version of the environment. Must match a version for which a predefined
     *            environment file is available.
     * @param monitor Receives progress of the creation process. Allows to cancel the environment creation from within
     *            another thread.
     * @return A description of the created Conda environment.
     * @throws IOException If an error occurs during execution of the underlying Conda commands. This also includes
     *             cases where an environment of name {@code environmentName} is already present in this Conda
     *             installation.
     * @throws CondaCanceledExecutionException If environment creation was canceled via the given monitor.
     * @throws UnsupportedOperationException If creating a default environment is not supported for the local operating
     *             system.
     */
    public static CondaEnvironmentIdentifier createDefaultPythonEnvironment(final Conda conda,
        final String environmentName, final Version pythonVersion, final CondaEnvironmentCreationMonitor monitor)
        throws IOException, CondaCanceledExecutionException {
        final RPythonVersion pythonMajorVersion = pythonVersion.getMajor() == 3 //
            ? RPythonVersion.PYTHON3 :
            	pythonVersion.getMajor() == 2 ? RPythonVersion.PYTHON2 :
            		RPythonVersion.R;
        return createEnvironmentFromFile(conda, pythonMajorVersion,
            CondaEnvironments.getPathToCondaConfigFile(pythonVersion), environmentName, monitor);
    }

    /**
     * {@code conda env create --file <pathToFile> --name <environmentName or generated name>}.<br>
     * The environment name specified in the file is ignored and replaced by either {@code environmentName} if it's
     * non-{@code null} and non-empty or a unique name that considers the already existing environments of this Conda
     * installation. The generated name is based on the given Python version.
     *
     * @param conda the {@link Conda} instance to use
     * @param pythonVersion The major version of the Python environment to create. Determines the generated name of the
     *            environment if {@code environmentName} is {@code null}.
     * @param pathToFile The path to the environment description file.
     * @param environmentName The name of the environment. Must not already exist in this Conda installation. May be
     *            {@code null} or empty in which case a default name is used.
     * @param monitor Receives progress of the creation process. Allows to cancel the environment creation from within
     *            another thread.
     * @return A description of the created environment.
     * @throws IOException If an error occurs during execution of the underlying command. This also includes cases where
     *             an environment of name {@code environmentName} is already present in this Conda installation.
     * @throws CondaCanceledExecutionException If environment creation was canceled via the given monitor.
     */
    public static CondaEnvironmentIdentifier createEnvironmentFromFile(final Conda conda,
        final RPythonVersion pythonVersion, final String pathToFile, String environmentName,
        final CondaEnvironmentCreationMonitor monitor) throws IOException, CondaCanceledExecutionException {
        if (environmentName == null || environmentName.isEmpty()) {
            environmentName = getDefaultPythonEnvironmentName(conda, pythonVersion, "");
        } else {
            final List<String> existingEnvironmentNames = conda.getEnvironmentNames();
            if (existingEnvironmentNames.contains(environmentName)) {
                throw new IOException(
                    "Conda environment '" + environmentName + "' already exists. Please use a different, unique name.");
            }
        }
        return conda.createEnvironmentFromFile(pathToFile, environmentName, monitor);
        //return conda.createEnvironmentFromFile("C:\\Users\\thsch\\OneDrive\\Dokumente\\CodeAndScripts\\pytest_knime.yml", "pytest_knime", monitor);
    }
}
