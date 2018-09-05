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
package de.bund.bfr.knime.pmm.xml2table;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdom2.Attribute;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;

/**
 * <code>NodeDialog</code> for the "XML2Table" Node.
 * 
 * @author BfR
 */
public class XML2TableNodeDialog extends DataAwareNodeDialogPane implements
		ItemListener {

	private SettingsHelper set;
	private BufferedDataTable table;

	private JComboBox<String> columnBox;
	private JList<String> elementList;

	protected XML2TableNodeDialog() {
		set = new SettingsHelper();

		columnBox = new JComboBox<>();
		columnBox.addItemListener(this);
		elementList = new JList<>();

		JPanel columnPanel = new JPanel();

		columnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.X_AXIS));
		columnPanel.add(new JLabel("Column:"));
		columnPanel.add(Box.createHorizontalStrut(5));
		columnPanel.add(columnBox);

		JPanel elementPanel = new JPanel();

		elementPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		elementPanel.setLayout(new BoxLayout(elementPanel, BoxLayout.X_AXIS));
		elementPanel.add(new JLabel("Elements:"));
		elementPanel.add(Box.createHorizontalStrut(5));
		elementPanel.add(new JScrollPane(elementList));

		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(columnPanel);
		panel.add(elementPanel);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new BorderLayout());
		northPanel.add(panel, BorderLayout.NORTH);

		addTab("Options", northPanel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		set.loadSettings(settings);
		table = input[0];
		columnBox.removeItemListener(this);
		columnBox.removeAllItems();

		List<String> columns = getXmlColumns(table.getSpec());

		for (String column : columns) {
			columnBox.addItem(column);
		}

		columnBox.addItemListener(this);
		columnBox.setSelectedItem(null);

		if (columns.contains(set.getColumn())) {
			columnBox.setSelectedItem(set.getColumn());
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		set.setColumn((String) columnBox.getSelectedItem());
		set.setXmlElements(elementList.getSelectedValuesList().toArray(
				new String[0]));
		set.saveSettings(settings);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == columnBox
				&& e.getStateChange() == ItemEvent.SELECTED) {
			String column = (String) columnBox.getSelectedItem();

			if (column != null) {
				List<String> elements = getElements(table, column);
				int[] indices = new int[set.getXmlElements().length];				

				for (int i = 0; i < set.getXmlElements().length; i++) {
					indices[i] = elements.indexOf(set.getXmlElements()[i]);					
				}
				
				elementList.setListData(elements.toArray(new String[0]));
				elementList.setSelectedIndices(indices);
			}
		}
	}

	private static List<String> getXmlColumns(DataTableSpec spec) {
		List<String> columns = new ArrayList<>();

		for (DataColumnSpec column : spec) {
			if (column.getType().equals(XMLCell.TYPE)) {
				columns.add(column.getName());
			}
		}

		return columns;
	}

	private static List<String> getElements(BufferedDataTable table,
			String column) {
		int index = table.getSpec().findColumnIndex(column);
		Set<String> elements = new LinkedHashSet<>();

		for (DataRow row : table) {
			PmmXmlDoc xml = XML2TableNodeModel.createXml(row.getCell(index));

			if (xml != null) {
				for (PmmXmlElementConvertable e : xml.getElementSet()) {
					for (Attribute attr : e.toXmlElement().getAttributes()) {
						elements.add(attr.getName());
					}
				}
			}
		}

		return new ArrayList<>(elements);
	}

}
