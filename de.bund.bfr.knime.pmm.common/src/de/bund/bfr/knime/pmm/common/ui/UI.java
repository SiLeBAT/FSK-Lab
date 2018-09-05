/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.common.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class UI {

	private UI() {
	}

	public static void packColumns(JTable table) {
		for (int c = 0; c < table.getColumnCount(); c++) {
			TableColumn col = table.getColumnModel().getColumn(c);

			if (col.getPreferredWidth() == 0) {
				continue;
			}

			TableCellRenderer renderer = col.getHeaderRenderer();
			Component comp = table
					.getTableHeader()
					.getDefaultRenderer()
					.getTableCellRendererComponent(table, col.getHeaderValue(),
							false, false, 0, 0);
			int width = comp.getPreferredSize().width + 20;

			for (int r = 0; r < table.getRowCount(); r++) {
				renderer = table.getCellRenderer(r, c);
				comp = renderer.getTableCellRendererComponent(table,
						table.getValueAt(r, c), false, false, r, c);
				width = Math.max(width, comp.getPreferredSize().width);
			}

			width += 5;
			col.setPreferredWidth(width);
		}
	}

	public static String select(JComboBox<String> box, Object item) {
		box.setSelectedItem(item);

		if (box.getSelectedItem() != null
				&& !box.getSelectedItem().equals(item)) {
			box.setSelectedItem(null);
		}

		return (String) box.getSelectedItem();
	}

	public static String select(JComboBox<String> box, Object item,
			Object defaultItem) {
		box.setSelectedItem(item);

		if (box.getSelectedItem() == null
				|| !box.getSelectedItem().equals(item)) {
			box.setSelectedItem(defaultItem);
		}

		return (String) box.getSelectedItem();
	}

	public static void adjustDialog(JDialog dialog) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(
				dialog.getGraphicsConfiguration());
		int maxWidth = screenSize.width - insets.left - insets.right;
		int maxHeight = screenSize.height - insets.top - insets.bottom;

		dialog.setSize(Math.min(dialog.getWidth(), maxWidth),
				Math.min(dialog.getHeight(), maxHeight));

		int minX = insets.left;
		int minY = insets.top;
		int maxX = screenSize.width - insets.right - dialog.getWidth();
		int maxY = screenSize.height - insets.bottom - dialog.getHeight();

		dialog.setLocation(Math.max(dialog.getX(), minX),
				Math.max(dialog.getY(), minY));
		dialog.setLocation(Math.min(dialog.getX(), maxX),
				Math.min(dialog.getY(), maxY));
	}
}
