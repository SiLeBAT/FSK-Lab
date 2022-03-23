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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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

import de.bund.bfr.knime.fsklab.preferences.RBinUtil.InvalidRHomeException;

/**
 * Preferences page.
 * 
 * @author Miguel Alba
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/** Creates a new preference page. */
	private Composite compositeConda;
	private Composite compositeNormal;
	private PythonFileFieldEditor python2FieldEditor;
	private PythonFileFieldEditor python3FieldEditor;
	private ComboViewer python3Envs;
	private ComboViewer python2Envs;
	private Map<String, String> envsMaps;
	private Label messagePython2;
	private Label messagePython3;
	private Label messagepython2path;
	private Label messagepython3path;
	PythonCondaFieldEditor PythonCondaFieldEditor;
	/**
	 * properties to access the KNIME Python settings.
	 * */
	private static final String PLUGIN_ID = "org.knime.python2";
	private static final PreferenceStorage CURRENT_SCOPE_PREFERENCES =
	        new PreferenceStorage(Activator.PLUGIN_ID, InstanceScope.INSTANCE, DefaultScope.INSTANCE);
	private static final PythonConfigStorage CURRENT = new PreferenceWrappingConfigStorage(CURRENT_SCOPE_PREFERENCES);

	public PreferencePage() {
		super(GRID);

		setPreferenceStore(Plugin.getDefault().getPreferenceStore());
		setDescription("FSK-Lab");
		
	}

	@Override
	protected void createFieldEditors() {

		Composite parent = getFieldEditorParent();
		addField(new RHomeDirectoryFieldEditor(PreferenceInitializer.R3_PATH_CFG, "Path to R 3", parent));
		addField(new BooleanFieldEditor(PreferenceInitializer.RESTORE_RPROFILE, "restore .RProfile after every run",
				parent));

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

		Button[] radios = new Button[3];

		radios[0] = new Button(parent, SWT.RADIO);
		radios[0].setText("Conda");
		radios[0].setSelection(true);
		radios[0].setBounds(10, 30, 75, 30);
		radios[0].addSelectionListener(new CondaSelectionListener());

		radios[1] = new Button(parent, SWT.RADIO);
		radios[1].setText("Manual");
		radios[1].setBounds(10, 5, 75, 30);
		radios[1].addSelectionListener(new CondaSelectionListener());

		Label spacer = new Label(parent, SWT.HORIZONTAL);
		spacer.setText("");

		compositeConda = new Composite(parent, SWT.MULTI);
		GridData gridDataConda = new GridData();
		gridDataConda.horizontalSpan = 3;
		compositeConda.setLayoutData(gridDataConda);
		
		PythonCondaFieldEditor = new PythonCondaFieldEditor(PreferenceInitializer.CONDA_PATH_CFG,
				"Path to Conda installation directory", compositeConda, this);
		addField(PythonCondaFieldEditor);

		messagePython2 = new Label(compositeConda, SWT.WRAP);

		messagePython2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));

		Label env2Label = new Label(compositeConda, SWT.NONE);
		env2Label.setText("Select an Python 2 Environment:");
		python2Envs = new ComboViewer(compositeConda, SWT.READ_ONLY);

		python2Envs.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent paramSelectionChangedEvent) {
				((StructuredSelection) paramSelectionChangedEvent.getSelection()).getFirstElement();
				var selectedElement = ((StructuredSelection) paramSelectionChangedEvent.getSelection())
						.getFirstElement().toString();

				String pythonEnvHome = envsMaps.get(selectedElement);
				PythonKernelTestResult result;
				result = PythonKernelTester.testPython2Installation(
						new CondaPythonCommand(PythonVersion.fromId("python2"), PythonCondaFieldEditor.pythonHome, pythonEnvHome),
						Collections.emptyList(), true);

				Color red = new Color(python3Envs.getControl().getParent().getDisplay(), 255, 0, 0);
				Color black = new Color(python3Envs.getControl().getParent().getDisplay(), 0, 0, 0);
				if (result.hasError()) {

					messagePython2.setText(result.getErrorLog());
					messagePython2.setForeground(red);

				} else {
					messagePython2.setText(result.getVersion() + " is installed");
					messagePython2.setForeground(black);

				}

				Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.PYTHON2_ENV_CFG,
						envsMaps.get(selectedElement));
				python2Envs.getCombo().setText(selectedElement);
			}
		});

		compositeNormal = new Composite(parent, SWT.MULTI);
		compositeNormal.setVisible(false);
		GridData gridDataNormal = new GridData();
		gridDataNormal.horizontalSpan = 3;
		gridDataNormal.exclude = true;
		compositeNormal.setLayoutData(gridDataNormal);

		messagepython2path = new Label(compositeNormal, SWT.WRAP);

		messagepython2path.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));
		this.python2FieldEditor = new PythonFileFieldEditor(PreferenceInitializer.PYTHON2_PATH_CFG,
				"Path to Python2 execution", compositeNormal, "python2");
		python2FieldEditor.setStringValue("python");
		addField(this.python2FieldEditor);

		messagePython3 = new Label(compositeConda, SWT.WRAP);

		messagePython3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 3, 1));
		Label env3Label = new Label(compositeConda, SWT.NONE);
		env3Label.setText("Select an Python 3 Environment:");
		python3Envs = new ComboViewer(compositeConda, SWT.READ_ONLY);

		python3Envs.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent paramSelectionChangedEvent) {
				((StructuredSelection) paramSelectionChangedEvent.getSelection()).getFirstElement();
				var selectedElement = ((StructuredSelection) paramSelectionChangedEvent.getSelection())
						.getFirstElement().toString();
				String pythonEnvHome = envsMaps.get(selectedElement);
				PythonKernelTestResult result;
				result = PythonKernelTester.testPython3Installation(
						new CondaPythonCommand(PythonVersion.fromId("python3"), PythonCondaFieldEditor.pythonHome, pythonEnvHome),
						Collections.emptyList(), true);

				Color red = new Color(python3Envs.getControl().getParent().getDisplay(), 255, 0, 0);
				Color black = new Color(python3Envs.getControl().getParent().getDisplay(), 0, 0, 0);
				if (result.hasError()) {

					messagePython3.setText(result.getErrorLog());
					messagePython3.setForeground(red);

				} else {
					messagePython3.setText(result.getVersion() + " is installed");
					messagePython3.setForeground(black);

				}
				Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.PYTHON3_ENV_CFG,
						envsMaps.get(selectedElement));
				python3Envs.getCombo().setText(selectedElement);
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
		if(StringUtils.isEmpty(storedCondaHome)) {
			Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.CONDA_PATH_CFG, condaHome);
			PythonCondaFieldEditor.setStringValue(condaHome);
		}
		
		compositeNormal.layout(true, true);
		compositeNormal.getParent().pack();

	}
	
	class CondaSelectionListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (((Button) e.widget).getText().equals("Conda")) {
				compositeConda.setVisible(true);
				((GridData) compositeConda.getLayoutData()).exclude = false;
				compositeNormal.setVisible(false);
				((GridData) compositeNormal.getLayoutData()).exclude = true;
				compositeConda.layout(true, true);
				compositeConda.getParent().pack();
				Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.IS_CONDA, "TRUE");

			} else {
				compositeConda.setVisible(false);
				((GridData) compositeConda.getLayoutData()).exclude = true;
				compositeNormal.setVisible(true);
				((GridData) compositeNormal.getLayoutData()).exclude = false;
				compositeNormal.layout(true, true);
				compositeNormal.getParent().pack();
				Plugin.getDefault().getPreferenceStore().putValue(PreferenceInitializer.IS_CONDA, "TRUE");

			}

		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void init(final IWorkbench workbench) {
		// nothing to do
	}

	/**
	 * Modified DirectoryFieldEditor with an appropriate doCheckState() overridden
	 * and verification on key stroke.
	 * 
	 * @author Jonathan Hale
	 */
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

			final Path rHomePath = Paths.get(rHome);
			if (PreferenceInitializer.refresh) {
				setMessage("Please restart you Knime to have all setting applied.");
			}
			if (!Files.isDirectory(rHomePath)) {
				setMessage("The selected path is not a directory.", ERROR);
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
					setMessage("You have selected an R 3.1.0 installation. "
							+ "Please see http://tech.knime.org/faq#q26 for details.", WARNING);
					return true;
				}

				// Check if null or empty
				if (StringUtils.isEmpty(props.getProperty("Rserve.path"))) {
					setMessage("The package 'Rserve' needs to be installed in your R installation. "
							+ "Please install it in R using \"install.packages('Rserve')\".", ERROR);
					return false;
				}

				// Check if null or empty
				if (StringUtils.isEmpty(props.getProperty("miniCRAN.path"))) {
					setMessage("The package 'miniCRAN' needs to be installed in your R installation. "
							+ "Please install it in R using \"install.packages('miniCRAN')\".", ERROR);
					return false;
				}

				// Check if null or empty
				if (StringUtils.isEmpty(props.getProperty("svglite.path"))) {
					setMessage("The package 'svglite' needs to be installed in your R installation. "
							+ "Please install it in R using \"install.packages('svglite')\".", ERROR);
					return false;
				}

				// under Mac we need the Cairo package to use png()/bmp() devices
				if (Platform.getOS().equals(Platform.OS_MACOSX)) {
					// Check if null or empty
					final String cairoPath = props.getProperty("Cairo.path");
					if (StringUtils.isEmpty(cairoPath)) {
						setMessage("The package 'Cairo' needs to be installed in your R installation for "
								+ "bitmap graphics devices to work properly. Please install it in R using "
								+ "\"install.packages('Cairo')\".", WARNING);
						return false;
					}
				}
				PreferenceInitializer.refresh = true;
				setMessage(null, NONE);
				return true;
			} catch (InvalidRHomeException e) {
				setMessage(e.getMessage(), ERROR);
				return false;
			}
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
			final String pythonHome = getStringValue();
			PythonKernelTestResult result;

			if (versionId.equals("python3")) {
				result = PythonKernelTester.testPython3Installation(
						new ManualPythonCommand(PythonVersion.fromId(versionId), pythonHome), Collections.emptyList(),
						true);
			} else {
				result = PythonKernelTester.testPython2Installation(
						new ManualPythonCommand(PythonVersion.fromId(versionId), pythonHome), Collections.emptyList(),
						true);
			}
			Color red = new Color(this.parent.getDisplay(), 255, 0, 0);
			Color black = new Color(this.parent.getDisplay(), 0, 0, 0);
			if (result.hasError()) {

				if (versionId.equals("python3")) {
					messagepython3path.setText(result.getErrorLog());
					messagepython3path.setForeground(red);
				} else {
					messagepython2path.setText(result.getErrorLog());
					messagepython2path.setForeground(red);
				}
				return true;
			} else {
				if (versionId.equals("python3")) {
					messagepython3path.setText(result.getVersion() + " is installed");
					messagepython3path.setForeground(black);
				} else {
					messagepython2path.setText(result.getVersion() + " is installed");
					messagepython2path.setForeground(black);
				}
				return true;
			}
		}
	}

	private class PythonCondaFieldEditor extends DirectoryFieldEditor {
		PreferencePage page;
		Composite parent;
		String pythonHome;
		public PythonCondaFieldEditor(final String name, final String labelText, final Composite parent,
				PreferencePage page) {
			init(name, labelText);
			setChangeButtonText(JFaceResources.getString("openBrowse"));
			createControl(parent);
			this.page = page;
			this.parent = parent;

		}

		@Override
		protected boolean doCheckState() {
			pythonHome = getStringValue();
			if(StringUtils.isEmpty(pythonHome))
				return true;
			try {

				final Conda conda = new Conda(pythonHome);

				List<CondaEnvironmentIdentifier> list = conda.getEnvironments();
				envsMaps = (Map<String, String>) list.stream().collect(
						java.util.stream.Collectors.toMap(item -> item.getName(), item -> item.getDirectoryPath()));

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
				if(!StringUtils.isEmpty(value))
					python2Envs.getCombo().setText(getKey(envsMaps, value));
				else {
					String python2CondaEnvironmentDirectory = getPython2CondaEnvironmentDirectoryPath();
					python2Envs.getCombo().setText(getKey(envsMaps, python2CondaEnvironmentDirectory));
				}
					
				String value2 = PreferenceInitializer.getPython3Env();
				if(!StringUtils.isEmpty(value2))
					python3Envs.getCombo().setText(getKey(envsMaps, value2));
				else {
					String python3CondaEnvironmentDirectory = getPython3CondaEnvironmentDirectoryPath();
					python3Envs.getCombo().setText(getKey(envsMaps, python3CondaEnvironmentDirectory));
				}

				page.adjustGridLayout();
				parent.requestLayout();

			} catch (final Exception ex) {
				setMessage(ex.getMessage(), ERROR);
			}
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
