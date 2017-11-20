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
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

public class UIUtils {

	private UIUtils() {
	}

	/** Creates a panel with the names of a number of libraries. */
	public static JPanel createLibrariesPanel(final Set<File> libs) {

		final JPanel panel = new JPanel(new BorderLayout());
		panel.setName("Libraries list");

		final String[] libNames = libs.stream().map(File::getName).toArray(String[]::new);
		final JList<String> list = new JList<>(libNames);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(new JScrollPane(list));

		return panel;
	}

	/** Creates a panel with a list of resource files. */
	public static final JPanel createResourcesPanel(final Collection<Path> resources) {

		final JPanel panel = new JPanel(new BorderLayout());
		panel.setName("Resources list");

		final JList<Path> list = new JList<>(resources.stream().toArray(Path[]::new));
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
}
