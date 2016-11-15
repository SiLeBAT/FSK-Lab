package de.bund.bfr.knime.fsklab.nodes.creator;

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

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.util.FileUtil;


public class FskCreatorNodeDialog extends NodeDialogPane {

	// models
	private final SettingsModelString m_modelScript;
	private final SettingsModelString m_paramScript;
	private final SettingsModelString m_visualizationScript;
	private final SettingsModelString m_metaDataDoc;
	private final SettingsModelString m_libDirectory;
	private final SettingsModelStringArray m_selectedLibs;

	/** Chooser to pick the libraries. */
	private final DialogComponentStringListSelection libChooser;

	// type of the dialogs
	private static final int dialogType = JFileChooser.OPEN_DIALOG;

	public FskCreatorNodeDialog() {

		m_modelScript = new SettingsModelString(FskCreatorNodeModel.CFGKEY_MODEL_SCRIPT, "");
		m_paramScript = new SettingsModelString(FskCreatorNodeModel.CFGKEY_PARAM_SCRIPT, "");
		m_visualizationScript = new SettingsModelString(FskCreatorNodeModel.CFGKEY_VISUALIZATION_SCRIPT, "");
		m_metaDataDoc = new SettingsModelString(FskCreatorNodeModel.CFGKEY_SPREADSHEET, "");
		m_libDirectory = new SettingsModelString(FskCreatorNodeModel.CFGKEY_DIR_LIBS, "");
		m_selectedLibs = new SettingsModelStringArray(FskCreatorNodeModel.CFGKEY_LIBS, new String[0]);

		// Creates the GUI
		Box box = Box.createHorizontalBox();
		box.add(createFilesSelection());

		DialogComponentFileChooser dirChooser = new DialogComponentFileChooser(m_libDirectory, "lib-dir", dialogType,
				true);
		dirChooser.addChangeListener(new ChangeListener() {
			private FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return new FileNameExtensionFilter("Zip files", "zip").accept(pathname);
				}
			};

			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					File dirFile = FileUtil.getFileFromURL(FileUtil.toURL(m_libDirectory.getStringValue()));
					File[] filesInDir = dirFile.listFiles(filter);
					if (filesInDir.length > 0) {
						List<String> fnames = Arrays.stream(filesInDir).map(File::getName).collect(Collectors.toList());
						libChooser.replaceListItems(fnames, (String[]) null);
					}
				} catch (InvalidPathException | MalformedURLException error) {
					error.printStackTrace();
				}
			}
		});

		libChooser = new DialogComponentStringListSelection(m_selectedLibs, "Select libs", Arrays.asList(""), true, 10);

		Box librariesBox = Box.createVerticalBox();
		librariesBox.add(dirChooser.getComponentPanel());
		librariesBox.add(libChooser.getComponentPanel());
		box.add(librariesBox);

		addTab("Selection", box);
		removeTab("Options");
	}

	/** Creates Box to select R scripts and spreadsheet with meta data. */
	private Box createFilesSelection() {
		String rFilters = ".r|.R"; // Extension filters for the R scripts

		DialogComponentFileChooser modelScriptChooser = new DialogComponentFileChooser(m_modelScript,
				"modelScript-history", dialogType, rFilters);
		modelScriptChooser.setBorderTitle("Select model script");

		DialogComponentFileChooser paramScriptChooser = new DialogComponentFileChooser(m_paramScript,
				"paramScript-history", dialogType, rFilters);
		paramScriptChooser.setBorderTitle("Select param script");

		DialogComponentFileChooser vizScriptChooser = new DialogComponentFileChooser(m_visualizationScript,
				"vizScript-history", dialogType, rFilters);
		vizScriptChooser.setBorderTitle("Select visualization script");

		DialogComponentFileChooser metaDataChooser = new DialogComponentFileChooser(m_metaDataDoc, "metaData-history",
				dialogType);
		metaDataChooser.setBorderTitle("Select spreadsheet");

		Box box = Box.createVerticalBox();
		box.add(modelScriptChooser.getComponentPanel());
		box.add(paramScriptChooser.getComponentPanel());
		box.add(vizScriptChooser.getComponentPanel());
		box.add(metaDataChooser.getComponentPanel());

		return box;
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		try {
			m_modelScript.loadSettingsFrom(settings);
			m_paramScript.loadSettingsFrom(settings);
			m_visualizationScript.loadSettingsFrom(settings);
			m_metaDataDoc.loadSettingsFrom(settings);
			m_libDirectory.loadSettingsFrom(settings);
			m_selectedLibs.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		m_modelScript.saveSettingsTo(settings);
		m_paramScript.saveSettingsTo(settings);
		m_visualizationScript.saveSettingsTo(settings);
		m_metaDataDoc.saveSettingsTo(settings);
		m_libDirectory.saveSettingsTo(settings);
		m_selectedLibs.saveSettingsTo(settings);
	}
}
