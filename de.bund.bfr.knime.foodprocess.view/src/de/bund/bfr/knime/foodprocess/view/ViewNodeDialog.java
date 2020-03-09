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
package de.bund.bfr.knime.foodprocess.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObject;

import de.bund.bfr.knime.pcml.node.pcmltotable.PCMLDataTable;
import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.util.NameAndDbId;
import de.bund.bfr.pcml10.PCMLDocument;

/**
 * <code>NodeDialog</code> for the "View" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class ViewNodeDialog extends DataAwareNodeDialogPane {

	private ViewUi viewUi;

	/**
	 * New pane for configuring the View node.
	 */
	protected ViewNodeDialog() {
		viewUi = new ViewUi();
		addTab("Options", viewUi);
	}

	private Map<NameAndDbId, Integer> sortCols(Map<NameAndDbId, Integer> columns) {
		Map<NameAndDbId, Integer> sortedColumns = new LinkedHashMap<NameAndDbId, Integer>();
		for (NameAndDbId col : columns.keySet()) {
			if (JCheckboxWithObject.isAgent(col.getName())) sortedColumns.put(col, columns.get(col));
		}
		for (NameAndDbId col : columns.keySet()) {
			if (JCheckboxWithObject.isTemperature(col.getName())) sortedColumns.put(col, columns.get(col));
		}
		for (NameAndDbId col : columns.keySet()) {
			if (JCheckboxWithObject.isPH(col.getName())) sortedColumns.put(col, columns.get(col));
		}
		for (NameAndDbId col : columns.keySet()) {
			if (JCheckboxWithObject.isAW(col.getName())) sortedColumns.put(col, columns.get(col));
		}
		for (NameAndDbId col : columns.keySet()) {
			if (JCheckboxWithObject.isPressure(col.getName())) sortedColumns.put(col, columns.get(col));
		}
		for (NameAndDbId col : columns.keySet()) {
			if (JCheckboxWithObject.isMatrix(col.getName())) sortedColumns.put(col, columns.get(col));
		}
		for (NameAndDbId col : columns.keySet()) {
			if (!sortedColumns.containsKey(col) && JCheckboxWithObject.isWithUnit(col.getName())) sortedColumns.put(col, columns.get(col));
		}
		return sortedColumns;
	}
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			PortObject[] inObjects) throws NotConfigurableException { 
    	PCMLPortObject pcmlPortObject = (PCMLPortObject) inObjects[0];
    	PCMLDocument pcmlDoc = pcmlPortObject.getPcmlDoc();

    	viewUi.setDoc(pcmlDoc);

    	
    	viewUi.getJCheckBoxListPanel().getDefaultListModel().removeAllElements();
		Map<NameAndDbId, Integer> columns = PCMLDataTable.createColumnMap(pcmlDoc);
		Map<NameAndDbId, Integer> sortedColumns = sortCols(columns);
		for (NameAndDbId col : sortedColumns.keySet()) {
			JCheckboxWithObject cbwo = new JCheckboxWithObject(col.getName());
			viewUi.getJCheckBoxListPanel().getDefaultListModel().addElement(cbwo);
		}

		try {
			if (settings.containsKey(ViewNodeModel.CFG_XUNITS) && settings.getString(ViewNodeModel.CFG_XUNITS) != null) viewUi.getXUnits().setSelectedItem(settings.getString(ViewNodeModel.CFG_XUNITS));
			else viewUi.getXUnits().setSelectedIndex(2);
			
			if (settings.containsKey(ViewNodeModel.CFG_EQUIDISTANT)) viewUi.setEquidistantProcesses(settings.getBoolean(ViewNodeModel.CFG_EQUIDISTANT));
			
			if (settings.containsKey(ViewNodeModel.CFG_PLOTLINES)) {
				viewUi.setPlotLines(settings.getBoolean(ViewNodeModel.CFG_PLOTLINES));
			}
			
			if (settings.containsKey(ViewNodeModel.CFG_PLOTPOINTS)) {
				viewUi.setPlotPoints(settings.getBoolean(ViewNodeModel.CFG_PLOTPOINTS));
			}

			List<String> list = XmlConverter.xmlToObject(settings.getString(ViewNodeModel.CFG_USEDPARAMETERS), new ArrayList<String>());

			viewUi.getJCheckBoxListPanel().getJCheckBoxList().deselectAll();
			for (String cond : list) {
				for (int i=0;i<viewUi.getJCheckBoxListPanel().getJCheckBoxList().getModel().getSize();i++) {
					JCheckboxWithObject checkbox = viewUi.getJCheckBoxListPanel().getJCheckBoxList().getModel().getElementAt(i); 
					if (checkbox.getObject().equals(cond)) {
						checkbox.setSelected(true);
					}
				}
			}

		}
		catch (InvalidSettingsException e) {
		}
		viewUi.createChart();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (viewUi.getJCheckBoxListPanel().getJCheckBoxList().getSelections().size() == 0) {
			throw new InvalidSettingsException("List of conditions cannot be empty");
		}
		settings.addString(ViewNodeModel.CFG_USEDPARAMETERS, XmlConverter.objectToXml(viewUi.getJCheckBoxListPanel().getJCheckBoxList().getSelections()));

		
		settings.addString(ViewNodeModel.CFG_XUNITS, viewUi.getXUnits().getSelectedItem().toString());
		settings.addBoolean(ViewNodeModel.CFG_EQUIDISTANT, viewUi.isEquidistantProcesses());
		settings.addBoolean(ViewNodeModel.CFG_PLOTLINES, viewUi.isPlotLines());
		settings.addBoolean(ViewNodeModel.CFG_PLOTPOINTS, viewUi.isPlotPoints());
	}
}