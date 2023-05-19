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
package de.bund.bfr.knime.fsklab.python2.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.knime.conda.Conda;
import org.knime.conda.CondaEnvironmentIdentifier;
import org.knime.conda.prefs.CondaPreferences;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import org.knime.python2.PythonCommand;
import de.bund.bfr.knime.fsklab.envconfigs.PythonKernelTester;
import org.knime.python2.PythonModuleSpec;
import de.bund.bfr.knime.fsklab.envconfigs.PythonKernelTester.PythonKernelTestResult;

import de.bund.bfr.knime.fsklab.preferences.RPythonVersion;
import de.bund.bfr.knime.fsklab.python2.config.AbstractCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatus;
import de.bund.bfr.knime.fsklab.python2.config.AbstractCondaEnvironmentCreationObserver.CondaEnvironmentCreationStatusListener;
import org.knime.python2.extensions.serializationlibrary.SerializationLibraryExtensions;

/**
 * Observes Python environment configurations and initiates installation tests as soon as relevant configuration entries
 * change. Clients can subscribe to changes in the status of such installation tests or can manually trigger such tests.
 * The observer updates all relevant installation status messages in {@link CondaEnvironmentConfig} and/or
 * {@link ManualEnvironmentConfig} as soon as a respective installation test finishes.
 *
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 * @author Benjamin Wilhelm, KNIME GmbH, Konstanz, Germany
 */
public class PythonConfigsObserver extends AbstractPythonConfigsObserver {

    private static final String ARROW_SERIALIZER_ID = "org.knime.python2.serde.arrow";

    private final PythonVersionConfig m_versionConfig;

    private final PythonEnvironmentTypeConfig m_environmentTypeConfig;

    private final CondaEnvironmentsConfig m_condaEnvironmentsConfig;

    private final CondaEnvironmentCreationObserver m_python2EnvironmentCreator;

    private final CondaEnvironmentCreationObserver m_python3EnvironmentCreator;
    private final CondaEnvironmentCreationObserver m_rEnvironmentCreator;

    private final ManualEnvironmentsConfig m_manualEnvironmentsConfig;

    private final SerializerConfig m_serializerConfig;

    /**
     * @param versionConfig Python version. Changes to the selected version are observed.
     * @param environmentTypeConfig Environment type. Changes to the selected environment type are observed.
     * @param condaEnvironmentsConfig Conda environment configuration. Changes to the conda directory path as well as to
     *            the Python 2 and Python 3 environments are observed.
     * @param python2EnvironmentCreator Manages the creation of new Python 2 Conda environments. Enabled/disabled by
     *            this instance based on the validity of the local Conda installation. Finished creation processes are
     *            observed.
     * @param python3EnvironmentCreator Manages the creation of new Python 3 Conda environments. Enabled/disabled by
     *            this instance based on the validity of the local Conda installation. Finished creation processes are
     *            observed.
     * @param manualEnvironmentsConfig Manual environment configuration. Changes to the Python 2 and Python 3 paths are
     *            observed.
     * @param serializerConfig Serializer configuration. Changes to the serializer are observed.
     */
    public PythonConfigsObserver(final PythonVersionConfig versionConfig,
        final PythonEnvironmentTypeConfig environmentTypeConfig, final CondaEnvironmentsConfig condaEnvironmentsConfig,
        final CondaEnvironmentCreationObserver python2EnvironmentCreator,
        final CondaEnvironmentCreationObserver python3EnvironmentCreator,
        final CondaEnvironmentCreationObserver rEnvironmentCreator,
        final ManualEnvironmentsConfig manualEnvironmentsConfig, final SerializerConfig serializerConfig) {
        m_versionConfig = versionConfig;
        m_environmentTypeConfig = environmentTypeConfig;
        m_condaEnvironmentsConfig = condaEnvironmentsConfig;
        m_python2EnvironmentCreator = python2EnvironmentCreator;
        m_python3EnvironmentCreator = python3EnvironmentCreator;
        m_rEnvironmentCreator = rEnvironmentCreator;
        m_manualEnvironmentsConfig = manualEnvironmentsConfig;
        m_serializerConfig = serializerConfig;

        // Initialize view-model of default Python environment (since this was/is not persisted):

        updateDefaultPythonEnvironment();

        // Update default environment on version change.
        versionConfig.getPythonVersion().addChangeListener(e -> updateDefaultPythonEnvironment());

        // Test all environments of the respective type on environment type change:
        environmentTypeConfig.getEnvironmentType().addChangeListener(e -> {
            updateDefaultPythonEnvironment();
            testCurrentPreferences();
        });

        // Test Conda environments on change:
        condaEnvironmentsConfig.getPython2Config().getEnvironmentDirectory()
            .addChangeListener(e -> testPythonEnvironment(true, false, false));
        condaEnvironmentsConfig.getPython3Config().getEnvironmentDirectory()
            .addChangeListener(e -> testPythonEnvironment(true, true, false));
        condaEnvironmentsConfig.getRConfig().getEnvironmentDirectory()
        .addChangeListener(e -> testPythonEnvironment(true, false, true));

        // Disable Conda environment creation by default, updated when Conda installation is tested.
        python2EnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(false);
        python3EnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(false);
        rEnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(false);

        // Handle finished Conda environment creation processes:
        observeEnvironmentCreation(python2EnvironmentCreator, false, false);
        observeEnvironmentCreation(python3EnvironmentCreator, true, false);
        observeEnvironmentCreation(rEnvironmentCreator, false, true);

        // Test manual environments on change:
        manualEnvironmentsConfig.getPython2Config().getExecutablePath()
            .addChangeListener(e -> testPythonEnvironment(false, false, false));
        manualEnvironmentsConfig.getPython3Config().getExecutablePath()
            .addChangeListener(e -> testPythonEnvironment(false, true, false));
        manualEnvironmentsConfig.getRConfig().getExecutablePath()
        .addChangeListener(e -> testPythonEnvironment(false, false, true));
        // Test required external modules of serializer on change:
        serializerConfig.getSerializer().addChangeListener(e -> testCurrentPreferences());
    }

    /**
     * @return The currently selected PythonEnvironmentType
     */
    protected PythonEnvironmentType getEnvironmentType() {
        return PythonEnvironmentType.fromId(m_environmentTypeConfig.getEnvironmentType().getStringValue());
    }

    /**
     * @return The environments of the current type. Can be overloaded to support bundled conda environments.
     */
    protected PythonEnvironmentsConfig getEnvironmentsOfCurrentType() {
        final var environmentType = getEnvironmentType();
        if (PythonEnvironmentType.CONDA.equals(environmentType)) {
            return m_condaEnvironmentsConfig;
        } else if (PythonEnvironmentType.MANUAL.equals(environmentType)) {
            return m_manualEnvironmentsConfig;
        } else {
            throw new IllegalStateException("Selected environment type '" + environmentType.getName()
                + "' is neither " + "conda nor manual. This is an implementation error.");
        }
    }

    private void updateDefaultPythonEnvironment() {
        if (PythonEnvironmentType.BUNDLED.equals(getEnvironmentType())) {
            // We do not configure a default environment if bundling is selected,
            // that will happen once the user selects "Conda" or "Manual" for the first time.
            return;
        }

        final List<PythonEnvironmentConfig> notDefaultEnvironments = new ArrayList<>(6);
        Collections.addAll(notDefaultEnvironments,
        		m_condaEnvironmentsConfig.getPython2Config(),
        		m_condaEnvironmentsConfig.getPython3Config(),
        		m_condaEnvironmentsConfig.getRConfig(),
        		m_manualEnvironmentsConfig.getPython2Config(),
            m_manualEnvironmentsConfig.getPython3Config(),
            m_manualEnvironmentsConfig.getRConfig());

        final PythonEnvironmentsConfig environmentsOfCurrentType = getEnvironmentsOfCurrentType();
        final PythonEnvironmentConfig defaultEnvironment;
        final RPythonVersion pythonVersion = RPythonVersion.fromId(m_versionConfig.getPythonVersion().getStringValue());
        if (RPythonVersion.PYTHON2.equals(pythonVersion)) {
            defaultEnvironment = environmentsOfCurrentType.getPython2Config();
        } else if (RPythonVersion.PYTHON3.equals(pythonVersion)) {
            defaultEnvironment = environmentsOfCurrentType.getPython3Config();
        } else if (RPythonVersion.R.equals(pythonVersion)) {
            defaultEnvironment = environmentsOfCurrentType.getRConfig();
        } else {
            throw new IllegalStateException("Selected default Python version is neither Python 2 nor Python 3. "
                + "This is an implementation error.");
        }
        notDefaultEnvironments.remove(defaultEnvironment);

        for (final PythonEnvironmentConfig notDefaultEnvironment : notDefaultEnvironments) {
            notDefaultEnvironment.getIsDefaultPythonEnvironment().setBooleanValue(false);
        }
        defaultEnvironment.getIsDefaultPythonEnvironment().setBooleanValue(true);
    }

    /**
     * Initiates installation tests for all environments of the currently selected {@link PythonEnvironmentType} as well
     * as for the currently selected serializer. Depending on the selected environment type, the status of each of these
     * tests is published to all installation status models in either the observed {@link CondaEnvironmentConfig} or the
     * observed {@link ManualEnvironmentConfig}. Also to the installation error model of the observed
     * {@link SerializerConfig}.
     */
    public void testCurrentPreferences() {
        final PythonEnvironmentType environmentType = getEnvironmentType();
        if (PythonEnvironmentType.CONDA.equals(environmentType)) {
            refreshAndTestCondaConfig();
        } else if (PythonEnvironmentType.MANUAL.equals(environmentType)) {
            testPythonEnvironment(false, false, false);
            testPythonEnvironment(false, true, false);
            testPythonEnvironment(false, false, true);
        } else {
            throw new IllegalStateException("Selected environment type '" + environmentType.getName() + "' is neither "
                + "conda nor manual. This is an implementation error.");
        }
        testSerializer();
    }

    private void refreshAndTestCondaConfig() {
        new Thread(() -> {
            final Conda conda;
            try {
                conda = testCondaInstallation();
            } catch (final Exception ex) {
                return;
            }
            final List<CondaEnvironmentIdentifier> availableEnvironments;
            try {
                availableEnvironments = getAvailableCondaEnvironments(conda, true, true, true);
            } catch (final Exception ex) {
                return;
            }

            try {
                setAvailableCondaEnvironments(false, false, availableEnvironments);
                testPythonEnvironment(true, false, false);
            } catch (Exception ex) {
                // Ignore, we still want to configure and test the second environment.
            }
            try {
                setAvailableCondaEnvironments(true, false, availableEnvironments);
                testPythonEnvironment(true, true, false);
            } catch (Exception ex) {
                // Ignore
            }
            try {
                setAvailableCondaEnvironments(false, true, availableEnvironments);
                testPythonEnvironment(true, false, true);
            } catch (Exception ex) {
                // Ignore
            }
        }).start();
    }

    private Conda testCondaInstallation() throws Exception {
        final SettingsModelString condaInfoMessage = m_condaEnvironmentsConfig.getCondaInstallationInfo();
        final SettingsModelString condaErrorMessage = m_condaEnvironmentsConfig.getCondaInstallationError();
        try {
            condaInfoMessage.setStringValue("Testing Conda installation...");
            condaErrorMessage.setStringValue("");
            onCondaInstallationTestStarting();
            final String condaDir = CondaPreferences.getCondaInstallationDirectory();
            final Conda conda = new Conda(condaDir);
            conda.testInstallation();
            String condaVersionString = conda.getVersionString();
            try {
                condaVersionString =
                    "Conda version: " + Conda.condaVersionStringToVersion(condaVersionString).toString();
            } catch (final IllegalArgumentException ex) {
                // Ignore and use raw version string.
            }
            condaInfoMessage.setStringValue("Using Conda at '" + condaDir + "'. " + condaVersionString);
            condaErrorMessage.setStringValue("");
            m_python2EnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(true);
            m_python3EnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(true);
            m_rEnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(true);
            m_rEnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(true);
            onCondaInstallationTestFinished("");
            return conda;
        } catch (final Exception ex) {
            condaInfoMessage.setStringValue("");
            condaErrorMessage.setStringValue(ex.getMessage());
            clearAvailableCondaEnvironments(false, false);
            setCondaEnvironmentStatusMessages(false, false, "", "");
            clearAvailableCondaEnvironments(true, false);
            setCondaEnvironmentStatusMessages(true, false, "", "");
            clearAvailableCondaEnvironments(false, true);
            setCondaEnvironmentStatusMessages(false, true,"", "");
            m_python2EnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(false);
            m_python3EnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(false);
            m_rEnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(false);
            m_rEnvironmentCreator.getIsEnvironmentCreationEnabled().setBooleanValue(false);
            onCondaInstallationTestFinished(ex.getMessage());
            throw ex;
        }
    }

    private List<CondaEnvironmentIdentifier> getAvailableCondaEnvironments(final Conda conda,
        final boolean updatePython2StatusMessage, final boolean updatePython3StatusMessage,  final boolean updateRStatusMessage) throws Exception {
        try {
            final String determiningEnvironmentsMessage = "Collecting available environments...";
            if (updatePython2StatusMessage) {
                setCondaEnvironmentStatusMessages(false, false, determiningEnvironmentsMessage, "");
            }
            if (updatePython3StatusMessage) {
                setCondaEnvironmentStatusMessages(true, false, determiningEnvironmentsMessage, "");
            }
            if (updateRStatusMessage) {
                setCondaEnvironmentStatusMessages(false, true, determiningEnvironmentsMessage, "");
            }
            return conda.getEnvironments();
        } catch (final Exception ex) {
            m_condaEnvironmentsConfig.getCondaInstallationError().setStringValue(ex.getMessage());
            final String environmentsNotDetectedMessage = "Available environments could not be detected.";
            clearAvailableCondaEnvironments(false,false);
            setCondaEnvironmentStatusMessages(false, false, "", environmentsNotDetectedMessage);
            clearAvailableCondaEnvironments(true,false);
            setCondaEnvironmentStatusMessages(true,false, "", environmentsNotDetectedMessage);
            clearAvailableCondaEnvironments(false,true);
            setCondaEnvironmentStatusMessages(false, true, "", environmentsNotDetectedMessage);
            throw ex;
        }
    }

    private void clearAvailableCondaEnvironments(final boolean isPython3,final boolean isR) {
        setAvailableCondaEnvironments(isPython3, isR, Collections.emptyList());
    }

    private void setCondaEnvironmentStatusMessages(final boolean isPython3, final boolean isR, final String infoMessage,
        final String errorMessage) {
        final CondaEnvironmentConfig condaEnvironmentConfig = isPython3 //
            ? m_condaEnvironmentsConfig.getPython3Config() //
            : (isR ? m_condaEnvironmentsConfig.getRConfig() : m_condaEnvironmentsConfig.getPython2Config());
        condaEnvironmentConfig.getPythonInstallationInfo().setStringValue(infoMessage);
        condaEnvironmentConfig.getPythonInstallationError().setStringValue(errorMessage);
    }

    private void setAvailableCondaEnvironments(final boolean isPython3, final boolean isR,
        List<CondaEnvironmentIdentifier> availableEnvironments) {
        final CondaEnvironmentConfig condaConfig = isPython3 //
            ? m_condaEnvironmentsConfig.getPython3Config() //
            : (isR ? m_condaEnvironmentsConfig.getRConfig() : m_condaEnvironmentsConfig.getPython2Config());
        if (availableEnvironments.isEmpty()) {
            availableEnvironments = Arrays.asList(CondaEnvironmentIdentifier.PLACEHOLDER_CONDA_ENV);
        }
        condaConfig.getAvailableEnvironments()
            .setValue(availableEnvironments.toArray(new CondaEnvironmentIdentifier[0]));
        final String currentlySelectedEnvironment = condaConfig.getEnvironmentDirectory().getStringValue();
        if (availableEnvironments.stream()
            .noneMatch(env -> Objects.equals(env.getDirectoryPath(), currentlySelectedEnvironment))) {
            condaConfig.getEnvironmentDirectory().setStringValue(availableEnvironments.get(0).getDirectoryPath());
        }
    }

    private void testPythonEnvironment(final boolean isConda, final boolean isPython3, final boolean isR) {
        final PythonEnvironmentsConfig environmentsConfig;
        final PythonEnvironmentType environmentType;
        boolean isCondaPlaceholder = false;
        if (isConda) {
            if (isPlaceholderEnvironmentSelected(isPython3, isR)) {
                // We don't want to test the placeholder but just clear the installation status messages and return.
                isCondaPlaceholder = true;
            }
            environmentsConfig = m_condaEnvironmentsConfig;
            environmentType = PythonEnvironmentType.CONDA;
        } else {
            environmentsConfig = m_manualEnvironmentsConfig;
            environmentType = PythonEnvironmentType.MANUAL;
        }
        final PythonEnvironmentConfig environmentConfig;
        final RPythonVersion rPythonVersion;
        if (isPython3) {
            environmentConfig = environmentsConfig.getPython3Config();
            rPythonVersion = RPythonVersion.PYTHON3;
        } else if (isR) {
            environmentConfig = environmentsConfig.getRConfig();
            rPythonVersion = RPythonVersion.R;
        } else {
            environmentConfig = environmentsConfig.getPython2Config();
            rPythonVersion = RPythonVersion.PYTHON2;
        }
        final SettingsModelString infoMessage = environmentConfig.getPythonInstallationInfo();
        final SettingsModelString errorMessage = environmentConfig.getPythonInstallationError();
        final String environmentCreationInfo;
        if (isConda) {
            environmentCreationInfo =
                "\nNote: You can create a new " + rPythonVersion.getName() + " Conda environment that "
                    + "contains all packages\nrequired by the KNIME Python integration by clicking the '"
                    + AbstractCondaEnvironmentsPanel.CREATE_NEW_ENVIRONMENT_BUTTON_TEXT + "' button\nabove.";
        } else {
            environmentCreationInfo =
                "\nNote: An easy way to create a new " + rPythonVersion.getName() + " Conda environment that "
                    + "contains all packages\nrequired by the KNIME Python integration can be found on the '"
                    + PythonEnvironmentType.CONDA.getName() + "' tab of this preference page.";
        }
        if (isCondaPlaceholder) {
            infoMessage.setStringValue("");
            errorMessage.setStringValue("No environment available. Please create a new one to be able to use "
                + rPythonVersion.getName() + "." + environmentCreationInfo);
            return;
        }
        final Collection<PythonModuleSpec> requiredSerializerModules = getAdditionalRequiredModules();
        infoMessage.setStringValue("Testing " + rPythonVersion.getName() + " environment...");
        errorMessage.setStringValue("");
        new Thread(() -> {
            onEnvironmentInstallationTestStarting(environmentType, rPythonVersion);
            final PythonCommand pythonCommand = environmentConfig.getPythonCommand();
            final PythonKernelTestResult testResult = isPython3 //
                ? PythonKernelTester.testPython3Installation(pythonCommand, requiredSerializerModules, true) //
                : (isR ? PythonKernelTester.testPython3Installation(pythonCommand, requiredSerializerModules, true) :
                	PythonKernelTester.testPython2Installation(pythonCommand, requiredSerializerModules, true));
            infoMessage.setStringValue(testResult.getVersion());
            String errorLog = testResult.getErrorLog();
            if (errorLog != null && !errorLog.isEmpty()) {
                errorLog += environmentCreationInfo;
            }
            errorMessage.setStringValue(errorLog);
            onEnvironmentInstallationTestFinished(environmentType, rPythonVersion, testResult);
        }).start();
    }

    /**
     * Provides any additional modules that are required for the environment to be valid.
     * By default these are the requirements of the selected serialization library.
     *
     * @return any additional modules that have to be present
     */
    protected Collection<PythonModuleSpec> getAdditionalRequiredModules() {
        return SerializationLibraryExtensions
        .getSerializationLibraryFactory(m_serializerConfig.getSerializer().getStringValue())
        .getRequiredExternalModules();
    }

    private boolean isPlaceholderEnvironmentSelected(final boolean isPython3, final boolean isR) {
        final SettingsModelString condaEnvironmentDirectory = isPython3 //
            ? m_condaEnvironmentsConfig.getPython3Config().getEnvironmentDirectory() //
            : (isR ? m_condaEnvironmentsConfig.getRConfig().getEnvironmentDirectory() :
            	m_condaEnvironmentsConfig.getPython2Config().getEnvironmentDirectory());
        return CondaEnvironmentIdentifier.PLACEHOLDER_CONDA_ENV.getDirectoryPath()
            .equals(condaEnvironmentDirectory.getStringValue());
    }

    private void testSerializer() {
        String serializerErrorMessage = "";
        if (Objects.equals(m_serializerConfig.getSerializer().getStringValue(), ARROW_SERIALIZER_ID)) {
            serializerErrorMessage = "Apache Arrow cannot be used as the serialization library for Python 2."
                + "\nIf you intend to use Python 2, please use a different serialization library.";
        }
        m_serializerConfig.getSerializerError().setStringValue(serializerErrorMessage);
    }

    private void observeEnvironmentCreation(final CondaEnvironmentCreationObserver creationStatus,
        final boolean isPython3, final boolean isR) {
        creationStatus.addEnvironmentCreationStatusListener(new CondaEnvironmentCreationStatusListener() {

            @Override
            public void condaEnvironmentCreationStarting(final CondaEnvironmentCreationStatus status) {
                // no-op
            }

            @Override
            public void condaEnvironmentCreationFinished(final CondaEnvironmentCreationStatus status,
                final CondaEnvironmentIdentifier createdEnvironment) {
                final Conda conda;
                try {
                    conda = testCondaInstallation();
                } catch (final Exception ex) {
                    return;
                }
                final List<CondaEnvironmentIdentifier> availableEnvironments;
                try {
                    availableEnvironments = getAvailableCondaEnvironments(conda, !isPython3, isPython3, isR);
                } catch (final Exception ex) {
                    return;
                }
                try {
                    setAvailableCondaEnvironments(isPython3, isR, availableEnvironments);
                    final CondaEnvironmentConfig environmentConfig = isPython3 //
                        ? m_condaEnvironmentsConfig.getPython3Config() //
                        : (isR ? m_condaEnvironmentsConfig.getRConfig() : m_condaEnvironmentsConfig.getPython2Config());
                    environmentConfig.getEnvironmentDirectory().setStringValue(createdEnvironment.getDirectoryPath());
                    testPythonEnvironment(true, isPython3, isR);
                } catch (Exception ex) {
                    // Ignore, we still want to configure and test the second environment.
                }
            }

            @Override
            public void condaEnvironmentCreationCanceled(final CondaEnvironmentCreationStatus status) {
                // no-op
            }

            @Override
            public void condaEnvironmentCreationFailed(final CondaEnvironmentCreationStatus status,
                final String errorMessage) {
                // no-op
            }
        }, false);
    }
}
