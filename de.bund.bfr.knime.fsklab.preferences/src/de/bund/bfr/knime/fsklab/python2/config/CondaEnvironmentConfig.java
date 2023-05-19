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
 *   Feb 3, 2019 (marcel): created
 */
package de.bund.bfr.knime.fsklab.python2.config;

import java.io.IOException;
import java.util.List;

import org.knime.conda.Conda;
import org.knime.conda.CondaEnvironmentIdentifier;
import org.knime.conda.prefs.CondaPreferences;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import org.knime.python2.PythonCommand;

import de.bund.bfr.knime.fsklab.envconfigs.PythonCondaUtils;
import de.bund.bfr.knime.fsklab.preferences.RPythonVersion;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 */
public final class CondaEnvironmentConfig extends AbstractPythonEnvironmentConfig {

    private final RPythonVersion m_rPythonVersion;

    private final SettingsModelString m_environmentDirectory;

    private final String m_legacyEnvironmentNameConfigKey;

    private final String m_defaultEnvironmentDirectory;

    /** Only used for legacy support. See {@link #loadConfigFrom(PythonConfigStorage)} below. */
    private static final String LEGACY_PLACEHOLDER_CONDA_ENV_NAME = "<no environment>";

    /** Not meant for saving/loading. We just want observable values here to communicate with the view. */
    private final ObservableValue<CondaEnvironmentIdentifier[]> m_availableEnvironments;

    /**
     * @param rPythonVersion The Python version of Conda environments described by this instance.
     * @param environmentDirectoryConfigKey The identifier of the Conda environment directory config. Used for
     *            saving/loading the path to the environment's directory.
     * @param legacyEnvironmentNameConfigKey Legacy support: we used to only save the environment's name, not the path
     *            to its directory. This parameter is used to support configs of the old format. Leave it {@code null}
     *            if no legacy support is necessary.
     * @param defaultEnvironment The initial Conda environment.
     */
    public CondaEnvironmentConfig(final RPythonVersion rPythonVersion, //
        final String environmentDirectoryConfigKey, //
        final String legacyEnvironmentNameConfigKey, //
        final CondaEnvironmentIdentifier defaultEnvironment) {
        m_rPythonVersion = rPythonVersion;
        m_environmentDirectory =
            new SettingsModelString(environmentDirectoryConfigKey, defaultEnvironment.getDirectoryPath());
        m_legacyEnvironmentNameConfigKey = legacyEnvironmentNameConfigKey;
        m_defaultEnvironmentDirectory = m_environmentDirectory.getStringValue();
        m_availableEnvironments = new ObservableValue<>(new CondaEnvironmentIdentifier[]{defaultEnvironment});
    }

    /**
     * @return The path to the directory of the Python Conda environment.
     */
    public SettingsModelString getEnvironmentDirectory() {
        return m_environmentDirectory;
    }

    /**
     * @return The list of the currently available Python Conda environments. Not meant for saving/loading.
     */
    public ObservableValue<CondaEnvironmentIdentifier[]> getAvailableEnvironments() {
        return m_availableEnvironments;
    }

    @Override
    public PythonCommand getPythonCommand() {
    	
        return PythonCondaUtils.createPythonCommand(m_rPythonVersion, CondaPreferences.getCondaInstallationDirectory(),
            m_environmentDirectory.getStringValue());
    }

    @Override
    public void saveDefaultsTo(final PythonConfigStorage storage) {
        // Do nothing.
    }

    @Override
    public void saveConfigTo(final PythonConfigStorage storage) {
        storage.saveStringModel(m_environmentDirectory);
    }

    @Override
    public void loadConfigFrom(final PythonConfigStorage storage) {
        if (environmentDirectoryEntryExists(storage)) {
            storage.loadStringModel(m_environmentDirectory);
        } else if (!tryUseEnvironmentNameEntry(storage)) {
            /**
             * If none of the entries are present, fall back to defaults. This follows the behavior required from this
             * method as documented in {@link PythonConfig#saveDefaultsTo(PythonConfigStorage)}.
             */
            m_environmentDirectory.setStringValue(m_defaultEnvironmentDirectory);
        }
    }

    private boolean environmentDirectoryEntryExists(final PythonConfigStorage storage) {
        final SettingsModelString environmentDirectory =
            new SettingsModelString(m_environmentDirectory.getKey(), LEGACY_PLACEHOLDER_CONDA_ENV_NAME);
        storage.loadStringModel(environmentDirectory);
        return !LEGACY_PLACEHOLDER_CONDA_ENV_NAME.equals(environmentDirectory.getStringValue());
    }

    /**
     * Legacy support: we used to only save the environment's name, not the path to its directory. If the name is
     * available, we need to convert it into the correct path.
     */
    private boolean tryUseEnvironmentNameEntry(final PythonConfigStorage storage) {
        if (m_legacyEnvironmentNameConfigKey == null) {
            return false;
        }
        final SettingsModelString environmentName =
            new SettingsModelString(m_legacyEnvironmentNameConfigKey, LEGACY_PLACEHOLDER_CONDA_ENV_NAME);
        storage.loadStringModel(environmentName);
        final String environmentNameValue = environmentName.getStringValue();
        final boolean environmentNameEntryExists = !LEGACY_PLACEHOLDER_CONDA_ENV_NAME.equals(environmentNameValue);
        return environmentNameEntryExists && tryConvertEnvironmentNameIntoDirectory(environmentNameValue, storage);
    }

    private boolean tryConvertEnvironmentNameIntoDirectory(final String environmentName,
        final PythonConfigStorage storage) {
        try {
            final Conda conda = new Conda();
            conda.testInstallation();
            final List<CondaEnvironmentIdentifier> environments = conda.getEnvironments();
            for (final CondaEnvironmentIdentifier environment : environments) {
                if (environmentName.equals(environment.getName())) {
                    m_environmentDirectory.setStringValue(environment.getDirectoryPath());
                    storage.saveStringModel(m_environmentDirectory);
                    return true;
                }
            }
        } catch (final IOException ex) {
            NodeLogger.getLogger(CondaEnvironmentConfig.class).debug(ex);
            // Name conversion failed. Fall through and use directory path's default value instead.
        }
        return false;
    }
}
