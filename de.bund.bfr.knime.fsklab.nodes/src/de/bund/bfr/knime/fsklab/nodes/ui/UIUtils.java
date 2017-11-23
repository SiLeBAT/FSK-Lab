/*
 ***************************************************************************************************
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import org.knime.core.util.SimpleFileFilter;

import de.bund.bfr.swing.UI;

public class UIUtils {

	private UIUtils() {
	}

	/** Creates a panel with the names of a number of libraries. */
	public static JPanel createLibrariesPanel(final Collection<File> libs) {

		final JPanel panel = new JPanel(new BorderLayout());
		panel.setName("Libraries list");

		final String[] libNames = libs.stream().map(File::getName).toArray(String[]::new);
		final JList<String> list = new JList<>(libNames);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(new JScrollPane(list));

		return panel;
	}

	/**
	 * Do not use original JTable! On Mac grid lines are not visible.
	 * 
	 * @return JTable with light gray grid lines.
	 */
	public static JTable createTable(final TableModel model) {
		final JTable table = new JTable(model);
		table.setGridColor(Color.lightGray);
		return table;
	}

	public static JButton createAddButton() {
		JButton addButton = new JButton();
		addButton.setIcon(new ImageIcon(UIUtils.class.getResource("/img/ic_add_box_black_24dp_1x.png")));
		addButton.setContentAreaFilled(false);
		addButton.setToolTipText("Add");

		return addButton;
	}

	public static JButton createFileUploadButton() {
		JButton addButton = new JButton();
		addButton.setIcon(new ImageIcon(UIUtils.class.getResource("/img/ic_file_upload_black_24dp_1x.png")));
		addButton.setContentAreaFilled(false);
		addButton.setToolTipText("File upload");

		return addButton;
	}

	public static JButton createEditButton() {
		JButton addButton = new JButton();
		addButton.setIcon(new ImageIcon(UIUtils.class.getResource("/img/ic_mode_edit_black_24dp_1x.png")));
		addButton.setContentAreaFilled(false);
		addButton.setToolTipText("Edit");

		return addButton;
	}

	public static JButton createRemoveButton() {
		JButton addButton = new JButton();
		addButton.setIcon(new ImageIcon(UIUtils.class.getResource("/img/ic_remove_circle_black_24dp_1x.png")));
		addButton.setContentAreaFilled(false);
		addButton.setToolTipText("Remove");

		return addButton;
	}
	
	
	/**
	 * Creates panel to add/remove resource files.
	 * @param parent Parent component
	 * @param listModel Model of the {@link JList} displaying the resources.
	 */
	public static JPanel createResourcesPanel(final Component parent, final DefaultListModel<Path> listModel) {
		JList<Path> list = new JList<>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);

		final JButton addButton = UIUtils.createAddButton();
		final JButton removeButton = UIUtils.createRemoveButton();
		final JPanel buttonsPanel = UI.createHorizontalPanel(addButton, removeButton);

		// Initialize chooser before the event so that it remembers the current
		// directory between uses.
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.addChoosableFileFilter(new SimpleFileFilter("txt", "Plain text file"));
		fc.addChoosableFileFilter(new SimpleFileFilter("rdata", "R workspace file"));
		fc.setAcceptAllFileFilterUsed(false); // do not use the AcceptAll FileFilter

		addButton.addActionListener(event -> {
			final int returnVal = fc.showOpenDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				final Path selectedFile = fc.getSelectedFile().toPath();
				if (!listModel.contains(selectedFile)) {
					listModel.addElement(selectedFile);
				}
			}
		});

		removeButton.addActionListener(event -> {
			final int selectedIndex = list.getSelectedIndex();
			if (selectedIndex != -1) {
				listModel.remove(selectedIndex);
			}
		});

		final JPanel northPanel = UI.createNorthPanel(new JScrollPane(list));
		northPanel.add(UI.createCenterPanel(buttonsPanel), BorderLayout.SOUTH);

		return northPanel;
	}
}
