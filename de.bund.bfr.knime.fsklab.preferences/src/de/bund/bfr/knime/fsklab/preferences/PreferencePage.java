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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.knime.python2.Activator;
import org.knime.python2.CondaPythonCommand;
import org.knime.python2.ManualPythonCommand;
import org.knime.python2.PythonKernelTester;
import org.knime.python2.PythonKernelTester.PythonKernelTestResult;
import org.knime.python2.PythonVersion;
import org.knime.python2.conda.Conda;
import org.knime.python2.conda.CondaEnvironmentIdentifier;
import org.knime.python2.config.CondaEnvironmentsConfig;
import org.knime.python2.config.PythonConfigStorage;
import org.knime.python2.prefs.PreferenceStorage;
import org.knime.python2.prefs.PreferenceWrappingConfigStorage;
import org.knime.python2.prefs.PythonPreferences;

import de.bund.bfr.knime.fsklab.preferences.RBinUtil.InvalidRHomeException;

/**
 * Preferences page.
 * 
 * @author Miguel Alba
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/** Creates a new preference page. */
	private Composite compositePythonConda;
	private Composite compositeRConda;
	private Composite compositeNormal;
	private Composite compositeRNormal;
	private PythonFileFieldEditor python2FieldEditor;
	private PythonFileFieldEditor python3FieldEditor;
	private ComboViewer rEnvs;
	private ComboViewer python3Envs;
	private ComboViewer python2Envs;
	private Map<String, String> envsMaps;
	private Label messagePython2;
	private Label messagePython3;
	private Label messageRConda;
	private Label messageRManual;
	private Label messagepython2path;
	private Label messagepython3path;
	String condaPath;
	/**
	 * properties to access the KNIME Python settings.
	 */
	private static final String PLUGIN_ID = "org.knime.python2";
	private static final PreferenceStorage CURRENT_SCOPE_PREFERENCES = new PreferenceStorage(Activator.PLUGIN_ID,
			InstanceScope.INSTANCE, DefaultScope.INSTANCE);
	private static final PythonConfigStorage CURRENT = new PreferenceWrappingConfigStorage(CURRENT_SCOPE_PREFERENCES);

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Plugin.getDefault().getPreferenceStore());
		condaPath = PythonPreferences.getCondaInstallationPath();
	}

	@Override
	protected void createFieldEditors() {
		
		Composite parent = getFieldEditorParent();
		Label rTitle = new Label(parent, SWT.HORIZONTAL);
		rTitle.setText("R Setting:");
		Label rseparator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		GridData rgridDataSeperator = new GridData();
		rgridDataSeperator.horizontalSpan = 2;
		rgridDataSeperator.horizontalAlignment = SWT.FILL;
		rseparator.setLayoutData(rgridDataSeperator);
		Group rRadioGroup = new Group(parent, SWT.HORIZONTAL);
		rRadioGroup.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button rConda = new Button(rRadioGroup, SWT.RADIO);
		rConda.setText("Conda");
		rConda.setBounds(10, 30, 75, 30);
		rConda.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				super.widgetSelected(e);
				if (rConda.getSelection()) {
					compositeRConda.setVisible(true);
					((GridData) compositeRConda.getLayoutData()).exclude = false;
					compositeRNormal.setVisible(false);
					((GridData) compositeRNormal.getLayoutData()).exclude = true;
					compositeRConda.layout(true, true);
					compositeRConda.getParent().pack();
					Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.IS_R_CONDA, "TRUE");

				}
			}
		});

		Button rManual = new Button(rRadioGroup, SWT.RADIO);
		rManual.setText("Manual");
		rManual.setBounds(10, 5, 75, 30);
		rManual.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				super.widgetSelected(e);
				if (rManual.getSelection()) {
					compositeRConda.setVisible(false);
					((GridData) compositeRConda.getLayoutData()).exclude = true;
					compositeRNormal.setVisible(true);
					((GridData) compositeRNormal.getLayoutData()).exclude = false;
					compositeRNormal.layout(true, true);
					compositeRNormal.getParent().pack();
					Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.IS_R_CONDA, "FALSE");

				}
			}
		});

		
		Label rspacer = new Label(parent, SWT.VERTICAL);
		rspacer.setText("");
		Label rspacer2 = new Label(parent, SWT.VERTICAL);
		rspacer2.setText("");
		compositeRConda = new Composite(parent, SWT.VERTICAL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		compositeRConda.setLayout(gridLayout);
		if (!PreferenceInitializer.isRConda()) {
			rManual.setSelection(true);
			compositeRConda.setVisible(false);
		} else
			rConda.setSelection(true);
		messageRConda = new Label(compositeRConda, SWT.WRAP);
		GridData gridDataR2Normal = new GridData();
		gridDataR2Normal.horizontalSpan = 3;
		gridDataR2Normal.horizontalAlignment = SWT.FILL;
		
		messageRConda.setLayoutData(gridDataR2Normal);
		messageRConda.setText("Path to Conda: \n"+ condaPath); 
		
		Label envRLabel = new Label(compositeRConda, SWT.LEFT);
		envRLabel.setText("Select a R Environment:");

		rEnvs = new ComboViewer(compositeRConda, SWT.READ_ONLY);

		
		Button checkREnv = new Button(compositeRConda, SWT.BUTTON1);
		checkREnv.setText("Check R Environment");
		checkREnv.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							StructuredSelection sel = (StructuredSelection) rEnvs.getSelection();
							String selectedRElement = (String) sel.getFirstElement();
							String rEnvHome = envsMaps.get(selectedRElement);
							Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.R_ENV_CFG,
									envsMaps.get(selectedRElement));
							rEnvs.getCombo().setText(selectedRElement);
							testRHome(PreferenceInitializer.createExecutableString(rEnvHome), messageRConda);
						}
					});
					break;
				}
			}
		});
		fillCondaEnvsforR(condaPath, messageRConda, compositeRConda);
		
		compositeRNormal = new Composite(parent, SWT.MULTI);

		GridData gridDataRNormal = new GridData();
		gridDataRNormal.horizontalSpan = 3;
		gridDataRNormal.horizontalAlignment = SWT.FILL;
		if (PreferenceInitializer.isRConda()) {
			gridDataRNormal.exclude = true;
			compositeRNormal.setVisible(false);
		}
		compositeRNormal.setLayoutData(gridDataRNormal);
		messageRManual = new Label(compositeRNormal, SWT.WRAP);

		messageRManual.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));
		new BooleanFieldEditor(PreferenceInitializer.RESTORE_RPROFILE, "restore .RProfile after every run",
				compositeRNormal);
		Label separator1 = new Label(compositeRNormal, SWT.HORIZONTAL);
		GridData gridDataSeperator1 = new GridData();
		gridDataSeperator1.horizontalSpan = 2;
		gridDataSeperator1.horizontalAlignment = SWT.FILL;
		separator1.setLayoutData(gridDataSeperator1);

		RHomeDirectoryFieldEditor rhome = new RHomeDirectoryFieldEditor(PreferenceInitializer.R3_PATH_CFG, "Path to R 3", compositeRNormal);
		rhome.setStringValue(Plugin.getDefault().getPreferenceStore().getString(PreferenceInitializer.R3_PATH_CFG));
		Label newLineLable = new Label(parent, SWT.HORIZONTAL);
		GridData newLine = new GridData();
		newLine.horizontalSpan = 3;
		newLine.horizontalAlignment = SWT.FILL;
		newLineLable.setLayoutData(newLine);

		Label title = new Label(parent, SWT.HORIZONTAL);
		title.setText("Python Setting:");
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		GridData gridDataSeperator = new GridData();
		gridDataSeperator.horizontalSpan = 2;
		gridDataSeperator.horizontalAlignment = SWT.FILL;
		separator.setLayoutData(gridDataSeperator);

		Group pythonRadioGroup = new Group(parent, SWT.NONE);
		pythonRadioGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
		Button pythonConda = new Button(pythonRadioGroup, SWT.RADIO);
		pythonConda.setText("Conda");
		pythonConda.setBounds(10, 30, 75, 30);
		pythonConda.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				super.widgetSelected(e);
				if (pythonConda.getSelection()) {
					compositePythonConda.setVisible(true);
					((GridData) compositePythonConda.getLayoutData()).exclude = false;
					compositeNormal.setVisible(false);
					((GridData) compositeNormal.getLayoutData()).exclude = true;
					compositePythonConda.layout(true, true);
					compositePythonConda.getParent().pack();
					Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.IS_PYTHON_CONDA, "TRUE");
				}
			}
		});

		Button pythonManual = new Button(pythonRadioGroup, SWT.RADIO);
		pythonManual.setText("Manual");
		pythonManual.setBounds(10, 5, 75, 30);
		pythonManual.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				super.widgetSelected(e);
				if (pythonManual.getSelection()) {
					compositePythonConda.setVisible(false);
					((GridData) compositePythonConda.getLayoutData()).exclude = true;
					compositeNormal.setVisible(true);
					((GridData) compositeNormal.getLayoutData()).exclude = false;
					compositeNormal.layout(true, true);
					compositeNormal.getParent().pack();
					Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.IS_PYTHON_CONDA, "FALSE");
				}
			}
		});
		
		Label spacer = new Label(parent, SWT.HORIZONTAL);
		spacer.setText("");
		Label spacer2 = new Label(parent, SWT.HORIZONTAL);
		spacer2.setText("");

		compositePythonConda = new Composite(parent, SWT.MULTI);
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 3;
		compositePythonConda.setLayout(gridLayout2);
		if (!PreferenceInitializer.isPythonConda()) {
			compositePythonConda.setVisible(false);
			pythonManual.setSelection(true);
		} else 
			pythonConda.setSelection(true);
		
		Label messagePythonConda = new Label(compositePythonConda, SWT.WRAP);
		messagePythonConda.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));
		messagePythonConda.setText("Path to Conda: "+condaPath);
		fillCondaEnvsforPython(condaPath, messageRConda, compositePythonConda);
		//pythonCondaFieldEditor = new CondaFieldEditor(PreferenceInitializer.CONDA_PATH_CFG,
		//		"Path to Conda installation directory", compositePythonConda, this, true, messagePythonConda);
		//addField(pythonCondaFieldEditor);

		messagePython2 = new Label(compositePythonConda, SWT.WRAP);

		messagePython2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));

		Label env2Label = new Label(compositePythonConda, SWT.NONE);
		env2Label.setText("Select a Python 2 Environment:");
		python2Envs = new ComboViewer(compositePythonConda, SWT.READ_ONLY);

		
		Button checkPython2Env = new Button(compositePythonConda, SWT.BUTTON1);
		checkPython2Env.setText("Check Python 2 Environment");
		checkPython2Env.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							Color black = new Color(python3Envs.getControl().getParent().getDisplay(), 0, 0, 0);
							messagePython2.setText("Checking...");
							messagePython2.setForeground(black);
							StructuredSelection sel = (StructuredSelection) python2Envs.getSelection();
							String selectedPython2Element = (String) sel.getFirstElement();
							if (StringUtils.isEmpty(selectedPython2Element))
								return;
							String pythonEnvHome = envsMaps.get(selectedPython2Element);
							PythonKernelTestResult result;
							result = PythonKernelTester.testPython2Installation(
									new CondaPythonCommand(PythonVersion.fromId("python2"), condaPath, pythonEnvHome),
									Collections.emptyList(), true);

							Color red = new Color(python3Envs.getControl().getParent().getDisplay(), 255, 0, 0);
							if (result.hasError()) {

								messagePython2.setText(result.getErrorLog());
								messagePython2.setForeground(red);

							} else {
								messagePython2.setText(result.getVersion() + " is installed");
								messagePython2.setForeground(black);

							}

							Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.PYTHON2_ENV_CFG,
									envsMaps.get(selectedPython2Element));
							python2Envs.getCombo().setText(selectedPython2Element);
						}
					});
					break;
				}
			}
		});

		compositeNormal = new Composite(parent, SWT.MULTI);

		GridData gridDataNormal = new GridData();
		gridDataNormal.horizontalSpan = 3;
		if (PreferenceInitializer.isPythonConda()) {
			gridDataNormal.exclude = true;
			compositeNormal.setVisible(false);
		}

		compositeNormal.setLayoutData(gridDataNormal);

		messagepython2path = new Label(compositeNormal, SWT.WRAP);

		messagepython2path.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));
		this.python2FieldEditor = new PythonFileFieldEditor(PreferenceInitializer.PYTHON2_PATH_CFG,
				"Path to Python2 execution", compositeNormal, "python2");
		python2FieldEditor.setStringValue("python");
		addField(this.python2FieldEditor);

		messagePython3 = new Label(compositePythonConda, SWT.WRAP);

		messagePython3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));
		Label env3Label = new Label(compositePythonConda, SWT.NONE);
		env3Label.setText("Select a Python 3 Environment:");
		python3Envs = new ComboViewer(compositePythonConda, SWT.READ_ONLY);

		
		Button checkPython3Env = new Button(compositePythonConda, SWT.BUTTON1);
		checkPython3Env.setText("Check Python 3 Environment");
		checkPython3Env.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							Color black = new Color(python3Envs.getControl().getParent().getDisplay(), 0, 0, 0);
							messagePython3.setText("Checking...");
							messagePython3.setForeground(black);
							StructuredSelection sel = (StructuredSelection) python3Envs.getSelection();
							String selectedPython3Element = (String) sel.getFirstElement();
							if (StringUtils.isEmpty(selectedPython3Element))
								return;
							String pythonEnvHome = envsMaps.get(selectedPython3Element);
							PythonKernelTestResult result;
							result = PythonKernelTester.testPython3Installation(
									new CondaPythonCommand(PythonVersion.fromId("python3"),
											condaPath, pythonEnvHome),
									Collections.emptyList(), true);

							Color red = new Color(python3Envs.getControl().getParent().getDisplay(), 255, 0, 0);
							
							if (result.hasError()) {

								messagePython3.setText(result.getErrorLog());
								messagePython3.setForeground(red);

							} else {
								messagePython3.setText(result.getVersion() + " is installed");
								messagePython3.setForeground(black);

							}
							Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.PYTHON3_ENV_CFG,
									envsMaps.get(selectedPython3Element));
							python3Envs.getCombo().setText(selectedPython3Element);
						}
					});
					break;
				}
			}
		});
		messagepython3path = new Label(compositeNormal, SWT.WRAP);

		messagepython3path.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));

		this.python3FieldEditor = new PythonFileFieldEditor(PreferenceInitializer.PYTHON3_PATH_CFG,
				"Path to Python3 execution", compositeNormal, "python3");
		python2FieldEditor.setStringValue("python3");
		addField(this.python3FieldEditor);
		String storedCondaHome = PreferenceInitializer.getCondaPath();
		String condaHome = getCondaInstallationPath();
		if (StringUtils.isEmpty(storedCondaHome)) {
			Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.CONDA_PATH_CFG, condaHome);
		}

		compositeNormal.layout(true, true);
		compositeNormal.getParent().pack();

	}

	private void fillCondaEnvsforR(String condaHome, Label messageConda, Composite parent) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				List<CondaEnvironmentIdentifier> list = new ArrayList<>();
				try {
					Conda conda = new Conda(condaHome);
					list = conda.getEnvironments();

				} catch (final Exception ex) {
					// messageConda.setText(ex.getMessage());
					messageConda.setForeground(new Color(messageConda.getParent().getDisplay(), 0, 0, 0));
				}
				if (envsMaps == null || envsMaps.isEmpty()) {
					envsMaps = (Map<String, String>) list.stream().collect(
							java.util.stream.Collectors.toMap(item -> item.getName(), item -> item.getDirectoryPath()));
				}
				rEnvs.setContentProvider(new IStructuredContentProvider() {

					@Override
					public void inputChanged(Viewer paramViewer, Object paramObject1, Object paramObject2) {
					}

					@Override
					public void dispose() {
					}

					@Override
					public Object[] getElements(Object paramObject) {

						return ((HashMap) paramObject).keySet().toArray();
					}
				});
				rEnvs.setLabelProvider(new LabelProvider() {

					@Override
					public String getText(Object element) {
						return element.toString();
					}

				});
				rEnvs.setInput(envsMaps);
				String value = PreferenceInitializer.getREnv();
				if (!StringUtils.isEmpty(value))
					rEnvs.getCombo().setText(getKey(envsMaps, value));
				PreferencePage.this.adjustGridLayout();
				parent.requestLayout();

			}
		});
	}

	private void fillCondaEnvsforPython(String condaHome, Label messageConda, Composite parent) {
		if (StringUtils.isEmpty(condaHome))
			return;

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				List<CondaEnvironmentIdentifier> list = new ArrayList<>();
				try {
					Conda conda = new Conda(condaHome);
					list = conda.getEnvironments();

				} catch (final Exception ex) {
					// messageConda.setText(ex.getMessage());
					messageConda.setForeground(new Color(messageConda.getParent().getDisplay(), 0, 0, 0));
				}

				if (envsMaps == null || envsMaps.isEmpty()) {
					envsMaps = (Map<String, String>) list.stream().collect(
							java.util.stream.Collectors.toMap(item -> item.getName(), item -> item.getDirectoryPath()));
				}
				python3Envs.setContentProvider(new IStructuredContentProvider() {

					@Override
					public void inputChanged(Viewer paramViewer, Object paramObject1, Object paramObject2) {
					}

					@Override
					public void dispose() {
					}

					@Override
					public Object[] getElements(Object paramObject) {

						return ((HashMap) paramObject).keySet().toArray();
					}
				});
				python3Envs.setLabelProvider(new LabelProvider() {

					@Override
					public String getText(Object element) {
						return element.toString();
					}

				});
				python3Envs.setInput(envsMaps);

				python2Envs.setContentProvider(new IStructuredContentProvider() {

					@Override
					public void inputChanged(Viewer paramViewer, Object paramObject1, Object paramObject2) {
					}

					@Override
					public void dispose() {
					}

					@Override
					public Object[] getElements(Object paramObject) {

						return ((HashMap) paramObject).keySet().toArray();
					}
				});
				python2Envs.setLabelProvider(new LabelProvider() {

					@Override
					public String getText(Object element) {
						return element.toString();
					}

				});
				python2Envs.setInput(envsMaps);
				String value = PreferenceInitializer.getPython2Env();
				if (!StringUtils.isEmpty(value))
					python2Envs.getCombo().setText(getKey(envsMaps, value));
				else {
					String python2CondaEnvironmentDirectory = getPython2CondaEnvironmentDirectoryPath();
					if (!python2CondaEnvironmentDirectory.equals("no_conda_environment_selected")) {
						python2Envs.getCombo().setText(getKey(envsMaps, python2CondaEnvironmentDirectory));
					}
				}

				String value2 = PreferenceInitializer.getPython3Env();
				if (!StringUtils.isEmpty(value2))
					python3Envs.getCombo().setText(getKey(envsMaps, value2));
				else {
					String python3CondaEnvironmentDirectory = getPython3CondaEnvironmentDirectoryPath();
					if (!python3CondaEnvironmentDirectory.equals("no_conda_environment_selected"))
						python3Envs.getCombo().setText(getKey(envsMaps, python3CondaEnvironmentDirectory));
				}
				PreferencePage.this.adjustGridLayout();
				parent.requestLayout();
			}
		});
	}

	class PythonSelectionListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (((Button) e.widget).getText().equals("Conda")) {
				compositePythonConda.setVisible(true);
				((GridData) compositePythonConda.getLayoutData()).exclude = false;
				compositeNormal.setVisible(false);
				((GridData) compositeNormal.getLayoutData()).exclude = true;
				compositePythonConda.layout(true, true);
				compositePythonConda.getParent().pack();
				Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.IS_PYTHON_CONDA, "TRUE");

			} else {
				compositePythonConda.setVisible(false);
				((GridData) compositePythonConda.getLayoutData()).exclude = true;
				compositeNormal.setVisible(true);
				((GridData) compositeNormal.getLayoutData()).exclude = false;
				compositeNormal.layout(true, true);
				compositeNormal.getParent().pack();
				Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.IS_PYTHON_CONDA, "FALSE");

			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

	}
 
	private class RHomeDirectoryFieldEditor extends DirectoryFieldEditor {

		public RHomeDirectoryFieldEditor(final String name, final String labelText, final Composite parent) {
			init(name, labelText);
			setChangeButtonText(JFaceResources.getString("openBrowse"));
			setValidateStrategy(VALIDATE_ON_KEY_STROKE);
			createControl(parent);
		}

		@Override
		protected boolean doCheckState() {
			final String rHome = getStringValue();
			Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.R3_PATH_CFG, rHome);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					testRHome(rHome, messageRManual);
				}
			});
			return true;
		}
	}

	@Override
	public void init(final IWorkbench workbench) {
		// nothing to do
	}

	private boolean testRHome(String rHome, Label label) {
		Color red = new Color(label.getParent().getDisplay(), 255, 0, 0);
		Color yellow = new Color(label.getParent().getDisplay(), 255, 255, 0);
		if(!Files.exists(Paths.get(rHome))) {
			label.setText("The selected environment is not a valid!");
			label.setForeground(red);
			return false;
		}
		final Path rHomePath = Paths.get(rHome);
		if (PreferenceInitializer.refresh) {
			label.setText("Please restart you Knime to have all setting applied.");
		}
		if (!Files.isDirectory(rHomePath)) {
			label.setText("The selected path is not a directory.");
			label.setForeground(red);
			return false;
		}

		try {
			PreferenceInitializer.refresh = false;
			RBinUtil.checkRHome(rHome, true);

			DefaultRPreferenceProvider prefProvider = new DefaultRPreferenceProvider(rHome);
			final Properties props = prefProvider.getProperties();
			// the version numbers may contain spaces
			final String version = (props.getProperty("major") + "." + props.getProperty("minor")).replace(" ", "");

			if ("3.1.0".equals(version)) {
				label.setText("You have selected an R 3.1.0 installation. "
						+ "Please see http://tech.knime.org/faq#q26 for details.");
				label.setForeground(yellow);
				return true;
			}

			// Check if null or empty
			if (StringUtils.isEmpty(props.getProperty("Rserve.path"))) {
				label.setText("The package 'Rserve' needs to be installed in your R installation. "
						+ "Please install it in R using \"install.packages('Rserve')\".");
				label.setForeground(red);
				return false;
			}

			// Check if null or empty
			if (StringUtils.isEmpty(props.getProperty("miniCRAN.path"))) {
				label.setText("The package 'miniCRAN' needs to be installed in your R installation. "
						+ "Please install it in R using \"install.packages('miniCRAN')\".");
				label.setForeground(red);
				return false;
			}

			// Check if null or empty
			if (StringUtils.isEmpty(props.getProperty("svglite.path"))) {
				label.setText("The package 'svglite' needs to be installed in your R installation. "
						+ "Please install it in R using \"install.packages('svglite')\".");
				label.setForeground(red);
				return false;
			}

			// under Mac we need the Cairo package to use png()/bmp() devices
			if (Platform.getOS().equals(Platform.OS_MACOSX)) {
				// Check if null or empty
				final String cairoPath = props.getProperty("Cairo.path");
				if (StringUtils.isEmpty(cairoPath)) {
					label.setText("The package 'Cairo' needs to be installed in your R installation for "
							+ "bitmap graphics devices to work properly. Please install it in R using "
							+ "\"install.packages('Cairo')\".");
					label.setForeground(red);
					return false;
				}
			}
			PreferenceInitializer.refresh = true;
			label.setText("");
			return true;
		} catch (InvalidRHomeException e) {
			label.setText(e.getMessage());
			label.setForeground(red);
			return false;
		}
	}

	private class PythonFileFieldEditor extends FileFieldEditor {
		String versionId;
		Composite parent;

		public PythonFileFieldEditor(final String name, final String labelText, final Composite parent,
				String versionId) {
			init(name, labelText);
			setChangeButtonText(JFaceResources.getString("openBrowse"));
			createControl(parent);
			this.parent = parent;
			this.versionId = versionId;
		}

		@Override
		protected boolean doCheckState() {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					final String pythonHome = getStringValue();
					PythonKernelTestResult result;
					if (versionId.equals("python3")) {
						result = PythonKernelTester.testPython3Installation(
								new ManualPythonCommand(PythonVersion.fromId(versionId), pythonHome),
								Collections.emptyList(), true);
					} else {
						result = PythonKernelTester.testPython2Installation(
								new ManualPythonCommand(PythonVersion.fromId(versionId), pythonHome),
								Collections.emptyList(), true);
					}
					Color red = new Color(parent.getDisplay(), 255, 0, 0);
					Color black = new Color(parent.getDisplay(), 0, 0, 0);
					if (result.hasError()) {

						if (versionId.equals("python3")) {
							messagepython3path.setText(result.getErrorLog());
							messagepython3path.setForeground(red);
						} else {
							messagepython2path.setText(result.getErrorLog());
							messagepython2path.setForeground(red);
						}

					} else {
						if (versionId.equals("python3")) {
							messagepython3path.setText(result.getVersion() + " is installed");
							messagepython3path.setForeground(black);
						} else {
							messagepython2path.setText(result.getVersion() + " is installed");
							messagepython2path.setForeground(black);
						}
					}
				}
			});
			return true;
		}
	}


	public <K, V> K getKey(Map<K, V> map, V value) {
		for (Entry<K, V> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String getCondaInstallationPath() {
		final CondaEnvironmentsConfig condaEnvironmentsConfig = loadCurrentCondaEnvironmentsConfig();
		return condaEnvironmentsConfig.getCondaDirectoryPath().getStringValue();
	}

	public static String getPython2CondaEnvironmentDirectoryPath() {
		final CondaEnvironmentsConfig condaEnvironmentsConfig = loadCurrentCondaEnvironmentsConfig();
		return condaEnvironmentsConfig.getPython2Config().getEnvironmentDirectory().getStringValue();
	}

	public static String getPython3CondaEnvironmentDirectoryPath() {
		final CondaEnvironmentsConfig condaEnvironmentsConfig = loadCurrentCondaEnvironmentsConfig();
		return condaEnvironmentsConfig.getPython3Config().getEnvironmentDirectory().getStringValue();
	}

	private static CondaEnvironmentsConfig loadCurrentCondaEnvironmentsConfig() {
		final CondaEnvironmentsConfig condaEnvironmentsConfig = new CondaEnvironmentsConfig();
		condaEnvironmentsConfig.loadConfigFrom(CURRENT);
		return condaEnvironmentsConfig;
	}
}
