package de.bund.bfr.knime.fsklab.preferences;

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
 *   Jan 25, 2019 (marcel): created
 */

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.knime.conda.prefs.CondaPreferences;

import org.knime.python2.Activator;
import org.knime.python2.PythonCommand;
import org.knime.python2.PythonModuleSpec;
import de.bund.bfr.knime.fsklab.python2.config.CondaEnvironmentsConfig;
import de.bund.bfr.knime.fsklab.python2.config.FSKEnvironmentsConfig;
import de.bund.bfr.knime.fsklab.python2.config.ManualEnvironmentsConfig;
import de.bund.bfr.knime.fsklab.python2.config.PythonConfigStorage;
import de.bund.bfr.knime.fsklab.python2.config.PythonEnvironmentConfig;
import de.bund.bfr.knime.fsklab.python2.config.PythonEnvironmentType;
import de.bund.bfr.knime.fsklab.python2.config.PythonEnvironmentTypeConfig;
import de.bund.bfr.knime.fsklab.python2.config.PythonEnvironmentsConfig;
import de.bund.bfr.knime.fsklab.python2.config.PythonVersionConfig;
import de.bund.bfr.knime.fsklab.python2.config.SerializerConfig;
import org.knime.python2.extensions.serializationlibrary.SerializationLibraryExtensions;

/**
 * Convenience front-end of the preference-based configuration of the Python integration.
 *
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 */
public final class PythonPreferences {

    private static final PreferenceStorage DEFAULT_SCOPE_PREFERENCES =
        new PreferenceStorage("de.bund.bfr.knime.fsklab.preferences", DefaultScope.INSTANCE);

    private static final PreferenceStorage CURRENT_SCOPE_PREFERENCES =
        new PreferenceStorage("de.bund.bfr.knime.fsklab.preferences", InstanceScope.INSTANCE, DefaultScope.INSTANCE);

    /**
     * Accessed by preference page.
     */
    static final PythonConfigStorage CURRENT = new PreferenceWrappingConfigStorage(CURRENT_SCOPE_PREFERENCES);

    /**
     * Accessed by preference page and preferences initializer.
     */
    static final PythonConfigStorage DEFAULT = new PreferenceWrappingConfigStorage(DEFAULT_SCOPE_PREFERENCES);

    private PythonPreferences() {
    }

    /**
     * Initializes sensible default Python preferences on the instance scope-level if necessary.
     *
     * @see PythonPreferencesInitializer
     */
    public static void init() {
        // Backward compatibility: Old configured preferences should still have the "Manual" environment configuration
        // selected by default while we want "Conda" environment configuration as the default for new installations.
        try {
            final List<String> currentPreferencesKeys =
                Arrays.asList(CURRENT_SCOPE_PREFERENCES.getWritePreferences().keys());
            if (!currentPreferencesKeys.contains(PythonEnvironmentTypeConfig.CFG_KEY_ENVIRONMENT_TYPE)
                && (currentPreferencesKeys.contains(ManualEnvironmentsConfig.CFG_KEY_PYTHON2_PATH)
                    || currentPreferencesKeys.contains(ManualEnvironmentsConfig.CFG_KEY_PYTHON3_PATH)
                    || currentPreferencesKeys.contains(ManualEnvironmentsConfig.CFG_KEY_R_PATH)
                    || currentPreferencesKeys.contains(FSKEnvironmentsConfig.DEFAULT_FSK_PATH))) {
                final PythonEnvironmentTypeConfig environmentTypeConfig = new PythonEnvironmentTypeConfig();
                environmentTypeConfig.getEnvironmentType().setStringValue(PythonEnvironmentType.MANUAL.getId());
                environmentTypeConfig.saveConfigTo(CURRENT);
            }
        } catch (final Exception ex) {
            // Stick with default value.
        }
    }

    /**
     * @return The currently selected default Python version.
     */
    public static RPythonVersion getPythonVersionPreference() {
        final PythonVersionConfig pythonVersionConfig = new PythonVersionConfig();
        pythonVersionConfig.loadConfigFrom(CURRENT);
        return RPythonVersion.fromId(pythonVersionConfig.getPythonVersion().getStringValue());
    }

    /**
     * @return The currently selected Python environment type (Conda v. manual).
     */
    public static PythonEnvironmentType getEnvironmentTypePreference() {
        final PythonEnvironmentTypeConfig environmentTypeConfig = new PythonEnvironmentTypeConfig();
        environmentTypeConfig.loadConfigFrom(CURRENT);
        return PythonEnvironmentType.fromId(environmentTypeConfig.getEnvironmentType().getStringValue());
    }

    /**
     * @return The currently selected default Python 2 command.
     */
    public static PythonCommand getPython2CommandPreference() {
        return getPythonCommandPreference(RPythonVersion.PYTHON2);
    }

    /**
     * @return The currently selected default Python 3 command.
     */
    public static PythonCommand getPython3CommandPreference() {
        return getPythonCommandPreference(RPythonVersion.PYTHON3);
    }
    
    /**
     * @return The currently selected default R command.
     */
    public static PythonCommand getRCommandPreference() {
        return getPythonCommandPreference(RPythonVersion.R);
    }


    /**
     * @return The current FSK working directory.
     */
    public static FSKEnvironmentsConfig getFSKEnvironmentsConfig() {
    	FSKEnvironmentsConfig environmentsConfig = new FSKEnvironmentsConfig();
    	environmentsConfig.loadConfigFrom(CURRENT);
        return environmentsConfig;
    }

    /**
     * Retrieve the Python environments configuration for the given environment type
     *
     * Used e.g. to initialize the preference page for Python (Labs).
     * @param environmentType
     *
     * @since 4.6
     * @return The currently selected default Python 3 environments configuration.
     */
    public static PythonEnvironmentsConfig getPythonEnvironmentsConfig(final PythonEnvironmentType environmentType) {
        PythonEnvironmentsConfig environmentsConfig;
        if (PythonEnvironmentType.CONDA.equals(environmentType)) {
            environmentsConfig = new CondaEnvironmentsConfig();
        } else if (PythonEnvironmentType.MANUAL.equals(environmentType)) {
            environmentsConfig = new ManualEnvironmentsConfig();
        } else {
            throw new IllegalStateException(
                "Selected Python environment type is neither Conda nor manual. This is an implementation error.");
        }
        environmentsConfig.loadConfigFrom(CURRENT);
        return environmentsConfig;
    }

    private static PythonEnvironmentConfig getPythonEnvironmentConfig(final RPythonVersion pythonVersion) {
        PythonEnvironmentsConfig environmentsConfig = getPythonEnvironmentsConfig(getEnvironmentTypePreference());
        PythonEnvironmentConfig environmentConfig;
        if (RPythonVersion.PYTHON2.equals(pythonVersion)) {
            environmentConfig = environmentsConfig.getPython2Config();
        } else if (RPythonVersion.PYTHON3.equals(pythonVersion)) {
            environmentConfig = environmentsConfig.getPython3Config();
        } else if (RPythonVersion.R.equals(pythonVersion)) {
            environmentConfig = environmentsConfig.getRConfig();
        } else {
            throw new IllegalStateException("Selected default Python version is neither Python 2 nor Python 3. "
                + "This is an implementation error.");
        }
        return environmentConfig;
    }

    private static PythonCommand getPythonCommandPreference(final RPythonVersion pythonVersion) {
        return getPythonEnvironmentConfig(pythonVersion).getPythonCommand();
    }

    /**
     * @return The currently selected serialization library.
     */
    public static String getSerializerPreference() {
        final SerializerConfig serializerConfig = new SerializerConfig();
        serializerConfig.loadConfigFrom(CURRENT);
        return serializerConfig.getSerializer().getStringValue();
    }

    /**
     * @return The required modules of the currently selected serialization library.
     */
    public static Collection<PythonModuleSpec> getCurrentlyRequiredSerializerModules() {
        return SerializationLibraryExtensions.getSerializationLibraryFactory(getSerializerPreference())
            .getRequiredExternalModules();
    }

    /**
     * @return the configured path to the conda installation
     */
    public static String getCondaInstallationPath() {
        return CondaPreferences.getCondaInstallationDirectory();
    }

    public static String getPython2CondaEnvironmentDirectoryPath() {
        final CondaEnvironmentsConfig condaEnvironmentsConfig = loadCurrentCondaEnvironmentsConfig();
        return condaEnvironmentsConfig.getPython2Config().getEnvironmentDirectory().getStringValue();
    }

    public static String getPython3CondaEnvironmentDirectoryPath() {
        final CondaEnvironmentsConfig condaEnvironmentsConfig = loadCurrentCondaEnvironmentsConfig();
        return condaEnvironmentsConfig.getPython3Config().getEnvironmentDirectory().getStringValue();
    }
    public static String getRCondaEnvironmentDirectoryPath() {
        final CondaEnvironmentsConfig condaEnvironmentsConfig = loadCurrentCondaEnvironmentsConfig();
        return condaEnvironmentsConfig.getRConfig().getEnvironmentDirectory().getStringValue();
    }
    private static CondaEnvironmentsConfig loadCurrentCondaEnvironmentsConfig() {
        final CondaEnvironmentsConfig condaEnvironmentsConfig = new CondaEnvironmentsConfig();
        condaEnvironmentsConfig.loadConfigFrom(CURRENT);
        return condaEnvironmentsConfig;
    }
}