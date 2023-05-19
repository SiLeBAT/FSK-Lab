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
 *   Jan 24, 2019 (marcel): created
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


import de.bund.bfr.knime.fsklab.python2.config.AbstractCondaEnvironmentsPanel;
import de.bund.bfr.knime.fsklab.python2.config.CondaEnvironmentConfig;
import de.bund.bfr.knime.fsklab.python2.config.CondaEnvironmentCreationObserver;
import de.bund.bfr.knime.fsklab.python2.config.CondaEnvironmentsConfig;

/**
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 */
public final class CondaEnvironmentsPreferencePanel
    extends AbstractCondaEnvironmentsPanel<CondaEnvironmentCreationPreferenceDialog, Composite> {

    public CondaEnvironmentsPreferencePanel(final CondaEnvironmentsConfig config,
        final CondaEnvironmentCreationObserver python2EnvironmentCreator,
        final CondaEnvironmentCreationObserver python3EnvironmentCreator,
        final CondaEnvironmentCreationObserver rEnvironmentCreator,final Composite parent) {
        super(config, python2EnvironmentCreator, python3EnvironmentCreator,rEnvironmentCreator, parent);
    }

    @Override
    protected Composite createPanel(final Composite parent) {
        final Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout());
        return panel;
    }

    @Override
    protected void createCondaDirectoryPathStatusWidget(final SettingsModelString installationInfoMessage,
        final SettingsModelString installationErrorMessage, final Composite panel) {
        final CondaDirectoryPathStatusPanel statusPanel =
            new CondaDirectoryPathStatusPanel(installationInfoMessage, installationErrorMessage, panel);
        final GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = SWT.FILL;
        statusPanel.setLayoutData(gridData);
    }

    @Override
    protected void createPython2EnvironmentWidget(final CondaEnvironmentConfig python2Config,
        final CondaEnvironmentCreationObserver python2EnvironmentCreator, final Composite panel) {
        if (python2EnvironmentCreator != null) {
            createEnvironmentWidget(RPythonVersion.PYTHON2, python2Config, python2EnvironmentCreator, panel);
        }
    }

    @Override
    protected void createPython3EnvironmentWidget(final CondaEnvironmentConfig python3Config,
        final CondaEnvironmentCreationObserver python3EnvironmentCreator, final Composite panel) {
        createEnvironmentWidget(RPythonVersion.PYTHON3, python3Config, python3EnvironmentCreator, panel);
    }
    @Override
    protected void createREnvironmentWidget(final CondaEnvironmentConfig rConfig,
            final CondaEnvironmentCreationObserver rEnvironmentCreator, final Composite panel) {
            //createREnvironmentWidget(RPythonVersion.R, RConfig, REnvironmentCreator, panel);
    		createEnvironmentWidget(RPythonVersion.R, rConfig, rEnvironmentCreator, panel);
    		
        }
    private static void createEnvironmentWidget(final RPythonVersion rPythonVersion,
        final CondaEnvironmentConfig config, final CondaEnvironmentCreationObserver environmentCreator,
        final Composite panel) {
        final String pythonName = rPythonVersion.getName();
        final CondaEnvironmentSelectionBox environmentSelection = new CondaEnvironmentSelectionBox(rPythonVersion,
            config.getEnvironmentDirectory(), config.getAvailableEnvironments(), pythonName,
            "Name of the " + pythonName + " Conda environment", config.getPythonInstallationInfo(),
            config.getPythonInstallationError(), environmentCreator, panel);
        final GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        // TODO(benjamin) remove
//        gridData.horizontalIndent = 20;
        gridData.grabExcessHorizontalSpace = true;
        environmentSelection.setLayoutData(gridData);
        final SettingsModelBoolean isDefaultEnvironment = config.getIsDefaultPythonEnvironment();
        environmentSelection.setDisplayAsDefault(isDefaultEnvironment.getBooleanValue());
        isDefaultEnvironment
            .addChangeListener(e -> environmentSelection.setDisplayAsDefault(isDefaultEnvironment.getBooleanValue()));
    }
    /*
    private static void createREnvironmentWidget(final RPythonVersion rPythonVersion,
            final CondaEnvironmentConfig rConfig, final CondaEnvironmentCreationObserver environmentCreator,
            final Composite panel) {
            final String pythonName = rPythonVersion.getName();
            final CondaEnvironmentSelectionBox environmentSelection = new CondaEnvironmentSelectionBox(rPythonVersion,
                rConfig.getEnvironmentDirectory(), rConfig.getAvailableEnvironments(), "Python 3.8",
                "Name of the new " + pythonName + " Conda environment", rConfig.getPythonInstallationInfo(),
                rConfig.getPythonInstallationError(), environmentCreator, panel);
            final GridData gridData = new GridData();
            gridData.horizontalAlignment = SWT.FILL;
            // TODO(benjamin) remove
//            gridData.horizontalIndent = 20;
            gridData.grabExcessHorizontalSpace = true;
            environmentSelection.setLayoutData(gridData);
            final SettingsModelBoolean isDefaultEnvironment = rConfig.getIsDefaultPythonEnvironment();
            environmentSelection.setDisplayAsDefault(isDefaultEnvironment.getBooleanValue());
            isDefaultEnvironment
                .addChangeListener(e -> environmentSelection.setDisplayAsDefault(isDefaultEnvironment.getBooleanValue()));
        }
        */
}