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
package de.bund.bfr.knime.pmm.xlstimeseriesreader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.DBUtilities;
import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.FilePanel;
import de.bund.bfr.knime.pmm.common.ui.FilePanel.FileListener;
import de.bund.bfr.knime.pmm.common.ui.UI;
import de.bund.bfr.knime.pmm.common.units.Categories;

/**
 * <code>NodeDialog</code> for the "XLSTimeSeriesReader" Node.
 * 
 * @author Christian Thoens
 */
public class XLSTimeSeriesReaderNodeDialog extends NodeDialogPane
		implements ActionListener, ItemListener, FileListener {

	private static final String DO_NOT_USE = "Do Not Use";
	private static final String OTHER_PARAMETER = "Select Other";

	private XLSReader xlsReader;

	private SettingsHelper set;

	private JPanel mainPanel;

	private FilePanel filePanel;
	private JComboBox<String> sheetBox;
	private List<String> fileSheetList;
	private List<String> fileColumnList;

	private JCheckBox preserveIdsBox;

	private JButton addLiteratureButton;
	private JButton removeLiteratureButton;
	private JList<LiteratureItem> literatureList;

	private JPanel agentPanel;
	private JComboBox<String> agentBox;
	private JButton agentButton;
	private Map<String, JButton> agentButtons;

	private JPanel matrixPanel;
	private JComboBox<String> matrixBox;
	private JButton matrixButton;
	private Map<String, JButton> matrixButtons;

	private JPanel columnsPanel;
	private Map<String, JComboBox<String>> columnBoxes;
	private Map<String, JButton> columnButtons;
	private Map<String, JComboBox<String>> columnUnitBoxes;

	private JLabel noLabel;

	/**
	 * New pane for configuring the XLSTimeSeriesReader node.
	 */
	protected XLSTimeSeriesReaderNodeDialog() {
		xlsReader = new XLSReader();

		filePanel = new FilePanel("XLS File", FilePanel.OPEN_DIALOG);
		filePanel.setAcceptAllFiles(false);
		filePanel.addFileFilter(".xls", "Excel Spreadsheat (*.xls)");
		filePanel.addFileListener(this);
		sheetBox = new JComboBox<>();
		sheetBox.addItemListener(this);
		fileSheetList = new ArrayList<>();
		fileColumnList = new ArrayList<>();

		preserveIdsBox = new JCheckBox("Preserve Data IDs");

		addLiteratureButton = new JButton("Add");
		addLiteratureButton.addActionListener(this);
		removeLiteratureButton = new JButton("Remove");
		removeLiteratureButton.addActionListener(this);
		literatureList = new JList<>();

		noLabel = new JLabel();
		noLabel.setPreferredSize(new Dimension(100, 50));
		agentPanel = new JPanel();
		agentPanel.setBorder(BorderFactory.createTitledBorder(AttributeUtilities.getName(TimeSeriesSchema.ATT_AGENT)));
		agentPanel.setLayout(new BorderLayout());
		agentPanel.add(noLabel, BorderLayout.CENTER);
		agentButtons = new LinkedHashMap<>();
		matrixPanel = new JPanel();
		matrixPanel
				.setBorder(BorderFactory.createTitledBorder(AttributeUtilities.getName(TimeSeriesSchema.ATT_MATRIX)));
		matrixPanel.setLayout(new BorderLayout());
		matrixPanel.add(noLabel, BorderLayout.CENTER);
		matrixButtons = new LinkedHashMap<>();
		columnsPanel = new JPanel();
		columnsPanel.setBorder(BorderFactory.createTitledBorder("XLS Column -> PMM-Lab assignments"));
		columnsPanel.setLayout(new BorderLayout());
		columnsPanel.add(noLabel, BorderLayout.CENTER);
		columnBoxes = new LinkedHashMap<>();
		columnButtons = new LinkedHashMap<>();
		columnUnitBoxes = new LinkedHashMap<>();

		JPanel otherOptionsPanel = new JPanel();

		otherOptionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
		otherOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		otherOptionsPanel.add(preserveIdsBox);

		JPanel northLiteraturePanel = new JPanel();

		northLiteraturePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		northLiteraturePanel.add(addLiteratureButton);
		northLiteraturePanel.add(removeLiteratureButton);

		JPanel literaturePanel = new JPanel();

		literaturePanel.setBorder(BorderFactory.createTitledBorder("Literature"));
		literaturePanel.setLayout(new BorderLayout());
		literaturePanel.add(northLiteraturePanel, BorderLayout.NORTH);
		literaturePanel.add(new JScrollPane(literatureList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JPanel otherAndLiteraturePanel = new JPanel();

		otherAndLiteraturePanel.setLayout(new BorderLayout());
		otherAndLiteraturePanel.add(otherOptionsPanel, BorderLayout.NORTH);
		otherAndLiteraturePanel.add(literaturePanel, BorderLayout.CENTER);

		JPanel optionsPanel = new JPanel();

		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
		optionsPanel.add(otherAndLiteraturePanel);
		optionsPanel.add(agentPanel);
		optionsPanel.add(matrixPanel);
		optionsPanel.add(columnsPanel);

		JPanel sheetPanel = new JPanel();

		sheetPanel.setBorder(BorderFactory.createTitledBorder("Sheet"));
		sheetPanel.setLayout(new BorderLayout());
		sheetPanel.add(sheetBox, BorderLayout.NORTH);

		JPanel fileSheetPanel = new JPanel();

		fileSheetPanel.setLayout(new BorderLayout());
		fileSheetPanel.add(filePanel, BorderLayout.CENTER);
		fileSheetPanel.add(sheetPanel, BorderLayout.EAST);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(fileSheetPanel, BorderLayout.NORTH);
		mainPanel.add(optionsPanel, BorderLayout.CENTER);

		addTab("Options", mainPanel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		set = new SettingsHelper();
		set.loadSettings(settings);

		preserveIdsBox.setSelected(set.isPreserveIds());

		filePanel.removeFileListener(this);
		filePanel.setFileName(set.getFileName());
		filePanel.addFileListener(this);

		try {
			fileSheetList = xlsReader.getSheets(KnimeUtils.getFile(set.getFileName()));
		} catch (Exception e) {
			fileSheetList = new ArrayList<>();
		}

		sheetBox.removeItemListener(this);
		sheetBox.removeAllItems();

		for (String sheet : fileSheetList) {
			sheetBox.addItem(sheet);
		}

		UI.select(sheetBox, set.getSheetName());
		sheetBox.addItemListener(this);

		try {
			fileColumnList = xlsReader.getColumns(KnimeUtils.getFile(filePanel.getFileName()),
					(String) sheetBox.getSelectedItem());
		} catch (Exception e) {
			fileColumnList = new ArrayList<>();
		}

		if (set.getAgentColumn() == null) {
			if (set.getAgent() != null) {
				set.setAgentColumn(OTHER_PARAMETER);
			} else {
				set.setAgentColumn(DO_NOT_USE);
			}
		}

		if (set.getMatrixColumn() == null) {
			if (set.getMatrix() != null) {
				set.setMatrixColumn(OTHER_PARAMETER);
			} else {
				set.setMatrixColumn(DO_NOT_USE);
			}
		}

		literatureList.setListData(set.getLiterature().toArray(new LiteratureItem[0]));

		updateAgentPanel();
		updateMatrixPanel();
		updateColumnsPanel();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		set.setPreserveIds(preserveIdsBox.isSelected());
		cleanMaps();

		if (set.getFileName() == null) {
			throw new InvalidSettingsException("No file is specfied");
		}

		if (set.getSheetName() == null) {
			throw new InvalidSettingsException("No sheet is selected");
		}

		if (fileColumnList.isEmpty()) {
			throw new InvalidSettingsException("Specified file is invalid");
		}

		if (set.getAgentColumn() != null && set.getAgentColumn().equals(OTHER_PARAMETER) && set.getAgent() == null) {
			throw new InvalidSettingsException("No assignment for " + TimeSeriesSchema.ATT_AGENT);
		}

		if (set.getMatrixColumn() != null && set.getMatrixColumn().equals(OTHER_PARAMETER) && set.getMatrix() == null) {
			throw new InvalidSettingsException("No assignment for " + TimeSeriesSchema.ATT_MATRIX);
		}

		Set<Object> assignments = new LinkedHashSet<>();
		boolean idAssigned = false;

		for (String column : set.getColumnMappings().keySet()) {
			Object assignment = set.getColumnMappings().get(column);

			if (assignment == null) {
				throw new InvalidSettingsException("Column \"" + column + "\" has no assignment");
			}

			if (!assignments.add(assignment)) {
				String name = null;

				if (assignment instanceof MiscXml) {
					name = ((MiscXml) assignment).getName();
				} else if (assignment instanceof String) {
					name = (String) assignment;
				}

				throw new InvalidSettingsException("\"" + name + "\" can only be assigned once");
			}

			if (assignment.equals(XLSReader.ID_COLUMN)) {
				idAssigned = true;
			}
		}

		if (!idAssigned) {
			throw new InvalidSettingsException("\"" + XLSReader.ID_COLUMN + "\" must be assigned to a column");
		}

		if (set.getAgentColumn() != null && !set.getAgentColumn().equals(OTHER_PARAMETER)) {
			set.setAgent(null);
		}

		if (set.getMatrixColumn() != null && !set.getMatrixColumn().equals(OTHER_PARAMETER)) {
			set.setMatrix(null);
		}

		if (set.getAgentColumn() != null
				&& (set.getAgentColumn().equals(OTHER_PARAMETER) || set.getAgentColumn().equals(DO_NOT_USE))) {
			set.setAgentColumn(null);
		}

		if (set.getMatrixColumn() != null
				&& (set.getMatrixColumn().equals(OTHER_PARAMETER) || set.getMatrixColumn().equals(DO_NOT_USE))) {
			set.setMatrixColumn(null);
		}

		try {
			xlsReader.getTimeSeriesTuples(KnimeUtils.getFile(set.getFileName()), set.getSheetName(),
					set.getColumnMappings(), set.getTimeUnit(), set.getConcentrationUnit(), set.getAgentColumn(),
					set.getAgentMappings(), set.getMatrixColumn(), set.getMatrixMappings(), set.isPreserveIds(),
					set.getUsedIds());
		} catch (Exception e) {
		}

		set.saveSettings(settings);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == agentButton) {
			Integer id;

			if (set.getAgent() != null) {
				id = DBKernel.openAgentDBWindow(agentButton, set.getAgent().id);
			} else {
				id = DBKernel.openAgentDBWindow(agentButton, null);
			}

			if (id != null) {
				String name = DBKernel.getValue("Agenzien", "ID", id + "", "Agensname") + "";

				set.setAgent(new AgentXml(id, name, null, DBKernel.getLocalDBUUID()));
				agentButton.setText(name);
			}
		} else if (e.getSource() == matrixButton) {
			Integer id;

			if (set.getMatrix() != null) {
				id = DBKernel.openMatrixDBWindow(matrixButton, set.getMatrix().getId());
			} else {
				id = DBKernel.openMatrixDBWindow(matrixButton, null);
			}

			if (id != null) {
				String name = DBKernel.getValue("Matrices", "ID", id + "", "Matrixname") + "";

				set.setMatrix(new MatrixXml(id, name, null, DBKernel.getLocalDBUUID()));
				matrixButton.setText(name);
			}
		} else if (e.getSource() == addLiteratureButton) {
			Integer id = DBKernel.openLiteratureDBWindow(addLiteratureButton, null);
			Set<Integer> ids = new LinkedHashSet<>();

			for (LiteratureItem item : set.getLiterature()) {
				ids.add(item.id);
			}

			if (id != null && !ids.contains(id)) {
				LiteratureItem l = DBUtilities.getLiteratureItem(id);

				set.getLiterature().add(l);
				literatureList.setListData(set.getLiterature().toArray(new LiteratureItem[0]));
			}
		} else if (e.getSource() == removeLiteratureButton) {
			if (literatureList.getSelectedIndices().length > 0) {
				int[] indices = literatureList.getSelectedIndices();

				Arrays.sort(indices);

				for (int i = indices.length - 1; i >= 0; i--) {
					set.getLiterature().remove(indices[i]);
				}

				literatureList.setListData(set.getLiterature().toArray(new LiteratureItem[0]));
			}
		} else {
			for (String value : agentButtons.keySet()) {
				if (e.getSource() == agentButtons.get(value)) {
					Integer id;

					if (set.getAgentMappings().get(value) != null) {
						id = DBKernel.openAgentDBWindow(agentButtons.get(value),
								set.getAgentMappings().get(value).id);
					} else {
						id = DBKernel.openAgentDBWindow(agentButtons.get(value), null);
					}

					if (id != null) {
						String name = DBKernel.getValue("Agenzien", "ID", id + "", "Agensname") + "";

						agentButtons.get(value).setText(name);
						set.getAgentMappings().put(value, new AgentXml(id, name, null, DBKernel.getLocalDBUUID()));
					}

					break;
				}
			}

			for (String value : matrixButtons.keySet()) {
				if (e.getSource() == matrixButtons.get(value)) {
					Integer id;

					if (set.getMatrixMappings().get(value) != null) {
						id = DBKernel.openMatrixDBWindow(matrixButtons.get(value),
								set.getMatrixMappings().get(value).getId());
					} else {
						id = DBKernel.openMatrixDBWindow(matrixButtons.get(value), null);
					}

					if (id != null) {
						String name = DBKernel.getValue("Matrices", "ID", id + "", "Matrixname") + "";

						matrixButtons.get(value).setText(name);
						set.getMatrixMappings().put(value, new MatrixXml(id, name, null, DBKernel.getLocalDBUUID()));
					}

					break;
				}
			}

			for (String column : columnButtons.keySet()) {
				if (e.getSource() == columnButtons.get(column)) {
					Integer id;

					if (set.getColumnMappings().get(column) instanceof MiscXml) {
						id = DBKernel.openMiscDBWindow(columnButtons.get(column),
								((MiscXml) set.getColumnMappings().get(column)).getId());
					} else {
						id = DBKernel.openMiscDBWindow(columnButtons.get(column), null);
					}

					if (id != null) {
						String name = DBKernel.getValue("SonstigeParameter", "ID", id + "", "Parameter") + "";
						String description = DBKernel.getValue("SonstigeParameter", "ID", id + "", "Beschreibung") + "";
						List<String> categoryIDs = Arrays.asList(DBKernel
								.getValue("SonstigeParameter", "ID", id + "", "Kategorie").toString().split(","));
						String unit = null;

						if (!categoryIDs.isEmpty()) {
							unit = Categories.getCategory(categoryIDs.get(0)).getStandardUnit();
						}

						columnButtons.get(column).setText(name);
						set.getColumnMappings().put(column,
								new MiscXml(id, name, description, null, categoryIDs, unit, DBKernel.getLocalDBUUID()));
						updateColumnsPanel();
					}

					break;
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() != ItemEvent.SELECTED) {
			return;
		}

		if (e.getSource() == sheetBox) {
			try {
				set.setSheetName((String) sheetBox.getSelectedItem());
				fileColumnList = xlsReader.getColumns(KnimeUtils.getFile(filePanel.getFileName()), set.getSheetName());
			} catch (Exception ex) {
				fileColumnList = new ArrayList<>();
			}

			updateColumnsPanel();
			updateAgentPanel();
			updateMatrixPanel();
			mainPanel.revalidate();
		} else if (e.getSource() == agentBox) {
			set.setAgentColumn((String) agentBox.getSelectedItem());
			updateAgentPanel();
		} else if (e.getSource() == matrixBox) {
			set.setMatrixColumn((String) matrixBox.getSelectedItem());
			updateMatrixPanel();
		} else {
			for (String column : columnBoxes.keySet()) {
				if (e.getSource() == columnBoxes.get(column)) {
					String selected = (String) columnBoxes.get(column).getSelectedItem();

					if (selected.equals(XLSReader.ID_COLUMN) || selected.equals(MdInfoXml.ATT_COMMENT)
							|| selected.equals(AttributeUtilities.TIME)
							|| selected.equals(AttributeUtilities.CONCENTRATION)
							|| selected.equals(XLSReader.CONCENTRATION_STDDEV_COLUMN)
							|| selected.equals(XLSReader.CONCENTRATION_MEASURE_NUMBER)
							|| selected.equals(AttributeUtilities.AGENT_DETAILS)
							|| selected.equals(AttributeUtilities.MATRIX_DETAILS)) {
						set.getColumnMappings().put(column, selected);
					} else if (selected.equals(AttributeUtilities.ATT_TEMPERATURE)) {
						set.getColumnMappings().put(column,
								new MiscXml(AttributeUtilities.ATT_TEMPERATURE_ID, AttributeUtilities.ATT_TEMPERATURE,
										null, null, Arrays.asList(Categories.getTempCategory().getName()),
										Categories.getTempCategory().getStandardUnit()));
					} else if (selected.equals(AttributeUtilities.ATT_PH)) {
						set.getColumnMappings().put(column,
								new MiscXml(AttributeUtilities.ATT_PH_ID, AttributeUtilities.ATT_PH, null, null,
										Arrays.asList(Categories.getPhCategory().getName()), Categories.getPhUnit()));
					} else if (selected.equals(AttributeUtilities.ATT_AW)) {
						set.getColumnMappings().put(column,
								new MiscXml(AttributeUtilities.ATT_AW_ID, AttributeUtilities.ATT_AW, null, null,
										Arrays.asList(Categories.getAwCategory().getName()), Categories.getAwUnit()));
					} else if (selected.equals(OTHER_PARAMETER)) {
						set.getColumnMappings().put(column, null);
					} else if (selected.equals(DO_NOT_USE)) {
						set.getColumnMappings().remove(column);
					}

					updateColumnsPanel();
					break;
				}
			}

			for (String column : columnUnitBoxes.keySet()) {
				if (e.getSource() == columnUnitBoxes.get(column)) {
					String unit = (String) columnUnitBoxes.get(column).getSelectedItem();

					if (set.getColumnMappings().get(column) instanceof MiscXml) {
						MiscXml condition = (MiscXml) set.getColumnMappings().get(column);

						condition.setUnit(unit);
					} else if (set.getColumnMappings().get(column) instanceof String) {
						String mapping = (String) set.getColumnMappings().get(column);

						if (mapping.equals(AttributeUtilities.TIME)) {
							set.setTimeUnit(unit);
						} else if (mapping.equals(AttributeUtilities.CONCENTRATION)) {
							set.setConcentrationUnit(unit);
						}
					}

					break;
				}
			}
		}
	}

	@Override
	public void fileChanged(FilePanel source) {
		set.setFileName(filePanel.getFileName());

		try {
			fileSheetList = xlsReader.getSheets(KnimeUtils.getFile(set.getFileName()));
		} catch (Exception e) {
			fileSheetList = new ArrayList<>();
		}

		sheetBox.removeItemListener(this);
		sheetBox.removeAllItems();

		for (String sheet : fileSheetList) {
			sheetBox.addItem(sheet);
		}

		if (!fileSheetList.isEmpty()) {
			sheetBox.setSelectedIndex(0);
		}

		set.setSheetName((String) sheetBox.getSelectedItem());
		sheetBox.addItemListener(this);

		try {
			fileColumnList = xlsReader.getColumns(KnimeUtils.getFile(set.getFileName()),
					(String) sheetBox.getSelectedItem());
		} catch (Exception e) {
			fileColumnList = new ArrayList<>();
		}

		updateColumnsPanel();
		updateAgentPanel();
		updateMatrixPanel();
		mainPanel.revalidate();
	}

	private void updateAgentPanel() {
		agentButtons.clear();
		agentBox = new JComboBox<>(new String[] { DO_NOT_USE, OTHER_PARAMETER });
		agentButton = new JButton(OTHER_PARAMETER);

		for (String column : fileColumnList) {
			agentBox.addItem(column);
		}

		UI.select(agentBox, set.getAgentColumn(), DO_NOT_USE);

		if (set.getAgent() != null) {
			agentButton.setText(set.getAgent().name);
		} else {
			agentButton.setText(OTHER_PARAMETER);
		}

		agentBox.addItemListener(this);
		agentButton.addActionListener(this);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new GridBagLayout());
		northPanel.add(new JLabel("XLS Column:"), createConstraints(0, 0));
		northPanel.add(agentBox, createConstraints(1, 0));

		if (agentBox.getSelectedItem().equals(DO_NOT_USE)) {
			// Do nothing
		} else if (agentBox.getSelectedItem().equals(OTHER_PARAMETER)) {
			northPanel.add(agentButton, createConstraints(1, 1));
		} else {
			int row = 1;
			String column = (String) agentBox.getSelectedItem();
			Set<String> values;

			try {
				values = xlsReader.getValuesInColumn(KnimeUtils.getFile(filePanel.getFileName()),
						(String) sheetBox.getSelectedItem(), column);
			} catch (Exception e) {
				values = new LinkedHashSet<>();
			}

			for (String value : values) {
				JButton button = new JButton();

				if (set.getAgentMappings().get(value) != null) {
					button.setText(set.getAgentMappings().get(value).name);
				} else {
					button.setText(OTHER_PARAMETER);
				}

				button.addActionListener(this);
				agentButtons.put(value, button);

				northPanel.add(new JLabel(value + ":"), createConstraints(0, row));
				northPanel.add(button, createConstraints(1, row));
				row++;
			}

		}

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);

		agentPanel.removeAll();
		agentPanel.add(new JScrollPane(panel), BorderLayout.CENTER);
		agentPanel.revalidate();
	}

	private void updateMatrixPanel() {
		matrixButtons.clear();
		matrixBox = new JComboBox<>(new String[] { DO_NOT_USE, OTHER_PARAMETER });
		matrixButton = new JButton(OTHER_PARAMETER);

		for (String column : fileColumnList) {
			matrixBox.addItem(column);
		}

		UI.select(matrixBox, set.getMatrixColumn(), DO_NOT_USE);

		if (set.getMatrix() != null) {
			matrixButton.setText(set.getMatrix().getName());
		} else {
			matrixButton.setText(OTHER_PARAMETER);
		}

		matrixBox.addItemListener(this);
		matrixButton.addActionListener(this);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new GridBagLayout());
		northPanel.add(new JLabel("XLS Column:"), createConstraints(0, 0));
		northPanel.add(matrixBox, createConstraints(1, 0));

		if (matrixBox.getSelectedItem().equals(DO_NOT_USE)) {
			// Do nothing
		} else if (matrixBox.getSelectedItem().equals(OTHER_PARAMETER)) {
			northPanel.add(matrixButton, createConstraints(1, 1));
		} else {
			int row = 1;
			String column = (String) matrixBox.getSelectedItem();
			Set<String> values;

			try {
				values = xlsReader.getValuesInColumn(KnimeUtils.getFile(filePanel.getFileName()),
						(String) sheetBox.getSelectedItem(), column);
			} catch (Exception e) {
				values = new LinkedHashSet<>();
			}

			for (String value : values) {
				JButton button = new JButton();

				if (set.getMatrixMappings().get(value) != null) {
					button.setText(set.getMatrixMappings().get(value).getName());
				} else {
					button.setText(OTHER_PARAMETER);
				}

				button.addActionListener(this);
				matrixButtons.put(value, button);

				northPanel.add(new JLabel(value + ":"), createConstraints(0, row));
				northPanel.add(button, createConstraints(1, row));
				row++;
			}
		}

		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);

		matrixPanel.removeAll();
		matrixPanel.add(new JScrollPane(panel), BorderLayout.CENTER);
		matrixPanel.revalidate();
	}

	private void updateColumnsPanel() {
		if (!fileColumnList.isEmpty()) {
			columnBoxes.clear();
			columnButtons.clear();
			columnUnitBoxes.clear();

			JPanel northPanel = new JPanel();
			int row = 0;

			northPanel.setLayout(new GridBagLayout());

			for (String column : fileColumnList) {
				JComboBox<String> box = new JComboBox<>(new String[] { DO_NOT_USE, OTHER_PARAMETER, XLSReader.ID_COLUMN,
						MdInfoXml.ATT_COMMENT, AttributeUtilities.TIME, AttributeUtilities.CONCENTRATION,
						// XLSReader.CONCENTRATION_STDDEV_COLUMN,
						// XLSReader.CONCENTRATION_MEASURE_NUMBER,
						AttributeUtilities.AGENT_DETAILS, AttributeUtilities.MATRIX_DETAILS,
						AttributeUtilities.ATT_TEMPERATURE, AttributeUtilities.ATT_PH, AttributeUtilities.ATT_AW });
				JButton button = new JButton();

				if (set.getColumnMappings().containsKey(column)) {
					Object mapping = set.getColumnMappings().get(column);

					if (mapping == null) {
						UI.select(box, OTHER_PARAMETER);
						button.setEnabled(true);
						button.setText(OTHER_PARAMETER);
					} else if (mapping instanceof String) {
						UI.select(box, mapping);
						button.setEnabled(false);
						button.setText(OTHER_PARAMETER);
					} else if (mapping instanceof MiscXml) {
						int id = ((MiscXml) mapping).getId();

						if (id == AttributeUtilities.ATT_TEMPERATURE_ID) {
							UI.select(box, AttributeUtilities.ATT_TEMPERATURE);
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
						} else if (id == AttributeUtilities.ATT_PH_ID) {
							UI.select(box, AttributeUtilities.ATT_PH);
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
						} else if (id == AttributeUtilities.ATT_AW_ID) {
							UI.select(box, AttributeUtilities.ATT_AW);
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
						} else {
							UI.select(box, OTHER_PARAMETER);
							button.setEnabled(true);
							button.setText(((MiscXml) mapping).getName());
						}
					}
				} else {
					UI.select(box, DO_NOT_USE);
					button.setEnabled(false);
					button.setText(OTHER_PARAMETER);
				}

				box.addItemListener(this);
				button.addActionListener(this);
				columnBoxes.put(column, box);
				columnButtons.put(column, button);

				northPanel.add(new JLabel(column + ":"), createConstraints(0, row));
				northPanel.add(box, createConstraints(1, row));
				northPanel.add(button, createConstraints(2, row));

				if (set.getColumnMappings().get(column) instanceof MiscXml) {
					MiscXml condition = (MiscXml) set.getColumnMappings().get(column);
					List<String> allUnits = new ArrayList<>();

					for (String cat : condition.getCategories()) {
						allUnits.addAll(Categories.getCategory(cat).getAllUnits());
					}

					JComboBox<String> unitBox = new JComboBox<>(allUnits.toArray(new String[0]));

					UI.select(unitBox, condition.getUnit());
					unitBox.addItemListener(this);
					columnUnitBoxes.put(column, unitBox);
					northPanel.add(unitBox, createConstraints(3, row));
				} else if (set.getColumnMappings().get(column) instanceof String) {
					String mapping = (String) set.getColumnMappings().get(column);

					if (mapping.equals(AttributeUtilities.TIME)) {
						JComboBox<String> unitBox = new JComboBox<>(
								Categories.getTimeCategory().getAllUnits().toArray(new String[0]));

						UI.select(unitBox, set.getTimeUnit());
						unitBox.addItemListener(this);
						columnUnitBoxes.put(column, unitBox);
						northPanel.add(unitBox, createConstraints(3, row));
					} else if (mapping.equals(AttributeUtilities.CONCENTRATION)) {
						JComboBox<String> unitBox = new JComboBox<>(
								Categories.getUnitsFromCategories(Categories.getConcentrationCategories())
										.toArray(new String[0]));

						UI.select(unitBox, set.getConcentrationUnit());
						unitBox.addItemListener(this);
						columnUnitBoxes.put(column, unitBox);
						northPanel.add(unitBox, createConstraints(3, row));
					}
				}

				row++;
			}

			JPanel panel = new JPanel();

			panel.setLayout(new BorderLayout());
			panel.add(northPanel, BorderLayout.NORTH);

			columnsPanel.removeAll();
			columnsPanel.add(new JScrollPane(panel), BorderLayout.CENTER);
			columnsPanel.revalidate();
		} else {
			columnsPanel.removeAll();
			columnsPanel.add(noLabel, BorderLayout.CENTER);
			columnButtons.clear();
			columnsPanel.revalidate();
		}
	}

	private GridBagConstraints createConstraints(int x, int y) {
		return new GridBagConstraints(x, y, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
	}

	private void cleanMaps() {
		Map<String, AgentXml> newAgentMappings = new LinkedHashMap<>();
		Map<String, MatrixXml> newMatrixMappings = new LinkedHashMap<>();
		Map<String, Object> newColumnMappings = new LinkedHashMap<>();

		for (String agent : agentButtons.keySet()) {
			newAgentMappings.put(agent, set.getAgentMappings().get(agent));
		}

		for (String matrix : matrixButtons.keySet()) {
			newMatrixMappings.put(matrix, set.getMatrixMappings().get(matrix));
		}

		for (String column : fileColumnList) {
			if (set.getColumnMappings().containsKey(column)) {
				newColumnMappings.put(column, set.getColumnMappings().get(column));
			}
		}

		set.setAgentMappings(newAgentMappings);
		set.setMatrixMappings(newMatrixMappings);
		set.setColumnMappings(newColumnMappings);
	}

}
