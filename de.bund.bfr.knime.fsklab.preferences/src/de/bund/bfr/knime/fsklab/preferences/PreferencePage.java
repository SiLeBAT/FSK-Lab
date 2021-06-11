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
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import de.bund.bfr.knime.fsklab.preferences.RBinUtil.InvalidRHomeException;

/**
 * Preferences page.
 * 
 * @author Miguel Alba
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/** Creates a new preference page. */
	public PreferencePage() {
		super(GRID);

		setPreferenceStore(Plugin.getDefault().getPreferenceStore());
		setDescription("FSK-Lab");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		addField(new RHomeDirectoryFieldEditor(PreferenceInitializer.R3_PATH_CFG, "Path to R 3", parent));
		addField(new BooleanFieldEditor(PreferenceInitializer.RESTORE_RPROFILE, "restore .RProfile after every run", parent));
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
			if(PreferenceInitializer.refresh) {
			  setMessage("Please restart you Knime to have all setting applied." );
			}
			if (!Files.isDirectory(rHomePath)) {
				setMessage("The selected path is not a directory.", ERROR);
				return false;
			}

			try {
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

				setMessage(null, NONE);
				return true;
			} catch (InvalidRHomeException e) {
				setMessage(e.getMessage(), ERROR);
				return false;
			}
		}
	}
}
