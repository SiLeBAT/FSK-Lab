/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.preferences;
/*
 * ------------------------------------------------------------------------
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
 * ------------------------------------------------------------------------
 *
 * History
 *   Sep 25, 2014 (Patrick Winter): created
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.knime.conda.prefs.CondaPreferences;
import org.knime.core.node.NodeLogger;

import de.bund.bfr.knime.fsklab.envconfigs.PythonKernelTester.PythonKernelTestResult;
import de.bund.bfr.knime.fsklab.python2.config.CondaEnvironmentCreationObserver;
import de.bund.bfr.knime.fsklab.python2.config.CondaEnvironmentsConfig;
import de.bund.bfr.knime.fsklab.python2.config.FSKEnvironmentConfig;
import de.bund.bfr.knime.fsklab.python2.config.FSKEnvironmentsConfig;
import de.bund.bfr.knime.fsklab.python2.config.ManualEnvironmentsConfig;
import de.bund.bfr.knime.fsklab.python2.config.PythonConfig;
import de.bund.bfr.knime.fsklab.python2.config.PythonConfigsObserver;
import de.bund.bfr.knime.fsklab.python2.config.PythonEnvironmentType;
import de.bund.bfr.knime.fsklab.python2.config.PythonEnvironmentTypeConfig;
import de.bund.bfr.knime.fsklab.python2.config.PythonVersionConfig;
import de.bund.bfr.knime.fsklab.python2.config.SerializerConfig;
import de.bund.bfr.knime.fsklab.python2.config.AbstractPythonConfigsObserver.PythonConfigsInstallationTestStatusChangeListener;

/**
 * Preference page for configurations related to the de.bund.bfr.knime.fsklab.python2 plug-in.
 *
 * @author Marcel Wiedenmann, KNIME GmbH, Konstanz, Germany
 * @author Christian Dietz, KNIME GmbH, Konstanz, Germany
 * @author Patrick Winter, KNIME AG, Zurich, Switzerland
 */
public final class PythonPreferencePage  extends AbstractPythonPreferencePage {

    private PythonVersionConfig m_pythonVersionConfig;

    private PythonEnvironmentTypeConfig m_environmentTypeConfig;

    private StackLayout m_environmentConfigurationLayout;
    private StackLayout m_FSKenvironmentConfigurationLayout;
    private CondaEnvironmentsConfig m_condaEnvironmentsConfig;

    private CondaEnvironmentCreationObserver m_python2EnvironmentCreator;

    private CondaEnvironmentCreationObserver m_python3EnvironmentCreator;
    private CondaEnvironmentCreationObserver m_rEnvironmentCreator;

    private CondaEnvironmentsPreferencePanel m_condaEnvironmentPanel;

    private ManualEnvironmentsConfig m_manualEnvironmentsConfig;
    private FSKEnvironmentsConfig m_FSKEnvironmentsConfig;

    private ManualEnvironmentsPreferencePanel m_manualEnvironmentPanel;
    private ManualEnvironmentsPreferencePanel m_FSKmanualEnvironmentPanel;

    private SerializerConfig m_serializerConfig;

    private PythonConfigsObserver m_configObserver;

    private final IPropertyChangeListener m_condaDirPropertyChangeListener = event -> {
        if ("condaDirectoryPath".equals(event.getProperty()) && m_configObserver != null) {
            m_configObserver.testCurrentPreferences();
        }
    };

    /**
     * Constructs the main preference page of the KNIME Python integration.
     */
    public PythonPreferencePage() {
        super(PythonPreferences.CURRENT, PythonPreferences.DEFAULT);
    }

    @Override
    @SuppressWarnings("unused") // References to some panels are not needed; everything is done in their constructor.
    protected void populatePageBody(final Composite container, final List<PythonConfig> configs) {
        createInfoHeader(container);
        m_FSKEnvironmentsConfig = new FSKEnvironmentsConfig();
        configs.add(m_FSKEnvironmentsConfig);
        final Group environmentFSKConfigurationGroup = new Group(container, SWT.NONE);
        environmentFSKConfigurationGroup.setText("FSK-Lab working directory");
        environmentFSKConfigurationGroup.setLayout(new GridLayout());
        environmentFSKConfigurationGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Composite compositeWorkingDirectory = new Composite(environmentFSKConfigurationGroup, SWT.MULTI);

		GridData gridDataWorkingDirectory = new GridData();
		gridDataWorkingDirectory.horizontalSpan = 3;
		gridDataWorkingDirectory.horizontalAlignment = SWT.FILL;
		
		compositeWorkingDirectory.setLayoutData(gridDataWorkingDirectory);
		
		FSKWorkingDirectoryFieldEditor  fskWorkingDirectory= 
				new FSKWorkingDirectoryFieldEditor(m_FSKEnvironmentsConfig.getFSKConfig(),
						"FSK Working directory:",
						compositeWorkingDirectory);
		fskWorkingDirectory.setStringValue(m_FSKEnvironmentsConfig.getFSKConfig().getFSKPath().getStringValue());

        /*
        final Composite environmentFSKConfigurationPanel = new Composite(environmentFSKConfigurationGroup, SWT.NONE);
        m_FSKenvironmentConfigurationLayout = new StackLayout();
        environmentFSKConfigurationPanel.setLayout(m_FSKenvironmentConfigurationLayout);
        environmentFSKConfigurationPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        m_FSKEnvironmentsConfig = new FSKEnvironmentsConfig();
        configs.add(m_FSKEnvironmentsConfig);
        m_FSKmanualEnvironmentPanel =
            new FSKEnvironmentsPreferencePanel(m_FSKEnvironmentsConfig, environmentFSKConfigurationGroup);
        m_FSKmanualEnvironmentPanel.getPanel();
        */
        /* FSK Working directory
        Composite parent = getFieldEditorParent();
		compositeWorkingDirectory = new Composite(parent, SWT.MULTI);

		GridData gridDataWorkingDirectory = new GridData();
		gridDataWorkingDirectory.horizontalSpan = 3;
		gridDataWorkingDirectory.horizontalAlignment = SWT.FILL;
		
		compositeWorkingDirectory.setLayoutData(gridDataWorkingDirectory);
		
		FSKWorkingDirectoryFieldEditor  fskWorkingDirectory= new FSKWorkingDirectoryFieldEditor(PreferenceInitializer.FSK_PATH_CFG, "FSK Working directory:", compositeWorkingDirectory);
		fskWorkingDirectory.setStringValue(Plugin.getDefault().getPreferenceStore().getString(PreferenceInitializer.FSK_PATH_CFG));

        */
        
        // Python version selection:
        m_pythonVersionConfig = new PythonVersionConfig();
        configs.add(m_pythonVersionConfig);
        new PythonVersionPreferencePanel(m_pythonVersionConfig, container);

        // Environment configuration:
        final Group environmentConfigurationGroup = new Group(container, SWT.NONE);
        environmentConfigurationGroup.setText("FSK Python environment configuration");
        environmentConfigurationGroup.setLayout(new GridLayout());
        environmentConfigurationGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Environment type selection:
        m_environmentTypeConfig = new PythonEnvironmentTypeConfig();
        configs.add(m_environmentTypeConfig);
        new PythonEnvironmentTypePreferencePanel(m_environmentTypeConfig, environmentConfigurationGroup);
        final Label separator = new Label(environmentConfigurationGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Composite environmentConfigurationPanel = new Composite(environmentConfigurationGroup, SWT.NONE);
        m_environmentConfigurationLayout = new StackLayout();
        environmentConfigurationPanel.setLayout(m_environmentConfigurationLayout);
        environmentConfigurationPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Conda environment:
        m_condaEnvironmentsConfig = new CondaEnvironmentsConfig();
        configs.add(m_condaEnvironmentsConfig);

        m_python2EnvironmentCreator = new CondaEnvironmentCreationObserver(RPythonVersion.PYTHON2);
        m_python3EnvironmentCreator = new CondaEnvironmentCreationObserver(RPythonVersion.PYTHON3);
        m_rEnvironmentCreator = new CondaEnvironmentCreationObserver(RPythonVersion.R);
        m_condaEnvironmentPanel = new CondaEnvironmentsPreferencePanel(m_condaEnvironmentsConfig,
            m_python2EnvironmentCreator, m_python3EnvironmentCreator,m_rEnvironmentCreator, environmentConfigurationPanel);

        // Manual environment:
        m_manualEnvironmentsConfig = new ManualEnvironmentsConfig();
        configs.add(m_manualEnvironmentsConfig);
        m_manualEnvironmentPanel =
            new ManualEnvironmentsPreferencePanel(m_manualEnvironmentsConfig, environmentConfigurationPanel);

        // Serializer selection:
        m_serializerConfig = new SerializerConfig();
        configs.add(m_serializerConfig);
        new SerializerPreferencePanel(m_serializerConfig, container);
    }

    private static void createInfoHeader(final Composite parent) {
        final Link installationGuideInfo = new Link(parent, SWT.NONE);
        installationGuideInfo.setLayoutData(new GridData());
        final String message = "See <a href=\"https://docs.knime.com/latest/python_installation_guide/index.html\">"
            + "this guide</a> for details on how to install Python for use with KNIME.";
        installationGuideInfo.setText(message);
        final Color gray = new Color(parent.getDisplay(), 100, 100, 100);
        installationGuideInfo.setForeground(gray);
        installationGuideInfo.addDisposeListener(e -> gray.dispose());
        installationGuideInfo.setFont(JFaceResources.getFontRegistry().getItalic(""));
        installationGuideInfo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                try {
                    PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(e.text));
                } catch (PartInitException | MalformedURLException ex) {
                    NodeLogger.getLogger(PythonPreferencePage.class).error(ex);
                }
            }
        });
    }

    @Override
    protected void reflectLoadedConfigurations() {
        displayPanelForEnvironmentType(m_environmentTypeConfig.getEnvironmentType().getStringValue());
    }

    private void displayPanelForEnvironmentType(final String environmentTypeId) {
        final PythonEnvironmentType environmentType = PythonEnvironmentType.fromId(environmentTypeId);
        if (PythonEnvironmentType.CONDA.equals(environmentType)) {
            m_environmentConfigurationLayout.topControl = m_condaEnvironmentPanel.getPanel();
        } else if (PythonEnvironmentType.MANUAL.equals(environmentType)) {
            m_environmentConfigurationLayout.topControl = m_manualEnvironmentPanel.getPanel();
        } else {
            throw new IllegalStateException(
                "Selected Python environment type is neither Conda nor manual. This is an implementation error.");
        }
        updateDisplayMinSize();
    }

    @Override
    protected void setupHooks() {
        m_environmentTypeConfig.getEnvironmentType().addChangeListener(
            e -> displayPanelForEnvironmentType(m_environmentTypeConfig.getEnvironmentType().getStringValue()));

        m_configObserver = new PythonConfigsObserver(m_pythonVersionConfig, m_environmentTypeConfig,
            m_condaEnvironmentsConfig, m_python2EnvironmentCreator, m_python3EnvironmentCreator,m_rEnvironmentCreator,
            m_manualEnvironmentsConfig, m_serializerConfig);

        // Displaying installation test results may require resizing the scroll view.
        m_configObserver.addConfigsTestStatusListener(new PythonConfigsInstallationTestStatusChangeListener() {

            @Override
            public void condaInstallationTestStarting() {
                updateDisplayMinSize();
            }

            @Override
            public void condaInstallationTestFinished(final String errorMessage) {
                updateDisplayMinSize();
            }

            @Override
            public void environmentInstallationTestStarting(final PythonEnvironmentType environmentType,
                final RPythonVersion pythonVersion) {
                updateDisplayMinSize();
            }

            @Override
            public void environmentInstallationTestFinished(final PythonEnvironmentType environmentType,
                final RPythonVersion pythonVersion, final PythonKernelTestResult testResult) {
                updateDisplayMinSize();
            }
        });

        // Trigger installation test if the Conda directory path changes
        CondaPreferences.addPropertyChangeListener(m_condaDirPropertyChangeListener);

        // Trigger initial installation test.
        m_configObserver.testCurrentPreferences();
    }

    @Override
    protected void performApply() {
        super.performApply();
        m_configObserver.testCurrentPreferences();
    }

    @Override
    public void dispose() {
        super.dispose();
        CondaPreferences.removePropertyChangeListener(m_condaDirPropertyChangeListener);
    }
    private class FSKWorkingDirectoryFieldEditor extends DirectoryFieldEditor {

    	public FSKWorkingDirectoryFieldEditor(final FSKEnvironmentConfig fskConfig, final String labelText, final Composite parent) {
    		String name = fskConfig.getFSKPath().getStringValue();
    		init(name, labelText);
    		setChangeButtonText(JFaceResources.getString("openBrowse"));
    		setValidateStrategy(VALIDATE_ON_KEY_STROKE);
    		createControl(parent);
    	}

    	@Override
    	protected boolean doCheckState() {
    		//final String newFSKWorkingDirectory = getStringValue();
    		//Plugin.getDefault().getPreferenceStore().putValue(FSKPreferenceInitializer.FSK_PATH_CFG, newFSKWorkingDirectory);
    		return true;
    	}
    }
}