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
package de.bund.bfr.knime.pmm.timeseriescreator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.DBUtilities;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmConstants;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.IntTextField;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;
import de.bund.bfr.knime.pmm.common.ui.TextListener;
import de.bund.bfr.knime.pmm.common.ui.TimeSeriesTable;
import de.bund.bfr.knime.pmm.common.ui.UI;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;

/**
 * <code>NodeDialog</code> for the "TimeSeriesCreator" Node.
 * 
 * @author Christian Thoens
 */
public class TimeSeriesCreatorNodeDialog extends NodeDialogPane implements ActionListener {

	private static final int ROW_COUNT = 1000;
	private static final int DEFAULT_TIMESTEPNUMBER = 10;
	private static final double DEFAULT_TIMESTEPSIZE = 1.0;

	private static final String OTHER_PARAMETER = "Other Parameter";
	private static final String NO_PARAMETER = "Do Not Use";
	private static final String SELECT = "Select";

	private XLSReader xlsReader;
	private SettingsHelper set;

	private JPanel panel;
	private JButton clearButton;
	private JButton stepsButton;
	private JButton xlsButton;
	private TimeSeriesTable table;
	private JButton addLiteratureButton;
	private JButton removeLiteratureButton;
	private JList<LiteratureItem> literatureList;
	private JButton agentButton;
	private JButton matrixButton;
	private StringTextField idField;
	private StringTextField commentField;
	private DoubleTextField temperatureField;
	private DoubleTextField phField;
	private DoubleTextField waterActivityField;
	private JComboBox<String> timeBox;
	private JComboBox<String> logcBox;
	private JComboBox<String> tempBox;
	private JComboBox<String> phBox;
	private JComboBox<String> awBox;

	private List<MiscXml> conditions;
	private List<JButton> condButtons;
	private List<DoubleTextField> condValueFields;
	private List<JComboBox<String>> condUnitFields;
	private List<JButton> addButtons;
	private List<JButton> removeButtons;

	private JPanel settingsPanel;
	private int settingsPanelRows;

	/**
	 * New pane for configuring the TimeSeriesCreator node.
	 */
	protected TimeSeriesCreatorNodeDialog() {
		xlsReader = new XLSReader();

		condButtons = new ArrayList<>();
		conditions = new ArrayList<>();
		condValueFields = new ArrayList<>();
		condUnitFields = new ArrayList<>();
		addButtons = new ArrayList<>();
		removeButtons = new ArrayList<>();

		panel = new JPanel();
		settingsPanel = new JPanel();
		settingsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		settingsPanel.setLayout(new GridBagLayout());
		xlsButton = new JButton("Read from XLS file");
		xlsButton.addActionListener(this);
		stepsButton = new JButton("Set equidistant time steps");
		stepsButton.addActionListener(this);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		addLiteratureButton = new JButton("+");
		addLiteratureButton.addActionListener(this);
		removeLiteratureButton = new JButton("-");
		removeLiteratureButton.addActionListener(this);
		literatureList = new JList<>();
		agentButton = new JButton(SELECT);
		agentButton.addActionListener(this);
		matrixButton = new JButton(SELECT);
		matrixButton.addActionListener(this);
		idField = new StringTextField();
		commentField = new StringTextField(true);
		temperatureField = new DoubleTextField(true);
		temperatureField.setPreferredSize(new Dimension(100, temperatureField.getPreferredSize().height));
		phField = new DoubleTextField(PmmConstants.MIN_PH, PmmConstants.MAX_PH, true);
		phField.setPreferredSize(new Dimension(100, phField.getPreferredSize().height));
		waterActivityField = new DoubleTextField(PmmConstants.MIN_WATERACTIVITY, PmmConstants.MAX_WATERACTIVITY, true);
		waterActivityField.setPreferredSize(new Dimension(100, waterActivityField.getPreferredSize().height));
		timeBox = new JComboBox<>(Categories.getTimeCategory().getAllUnits().toArray(new String[0]));
		logcBox = new JComboBox<>(
				Categories.getUnitsFromCategories(Categories.getConcentrationCategories()).toArray(new String[0]));
		tempBox = new JComboBox<>(Categories.getTempCategory().getAllUnits().toArray(new String[0]));
		phBox = new JComboBox<>(Categories.getPhCategory().getAllUnits().toArray(new String[0]));
		awBox = new JComboBox<>(Categories.getAwCategory().getAllUnits().toArray(new String[0]));

		settingsPanel.add(new JLabel(AttributeUtilities.getName(TimeSeriesSchema.ATT_LITMD) + ":"),
				createConstraints(0, 0));
		settingsPanel.add(new JLabel(AttributeUtilities.getName(TimeSeriesSchema.ATT_AGENT) + ":"),
				createConstraints(0, 2));
		settingsPanel.add(new JLabel(AttributeUtilities.getName(TimeSeriesSchema.ATT_MATRIX) + ":"),
				createConstraints(0, 3));
		settingsPanel.add(new JLabel("ID:"), createConstraints(0, 4));
		settingsPanel.add(new JLabel(MdInfoXml.ATT_COMMENT + ":"), createConstraints(0, 5));
		settingsPanel.add(new JLabel(AttributeUtilities.getName(AttributeUtilities.TIME) + ":"),
				createConstraints(0, 6));
		settingsPanel.add(new JLabel(AttributeUtilities.getName(AttributeUtilities.CONCENTRATION) + ":"),
				createConstraints(0, 7));
		settingsPanel.add(new JLabel(AttributeUtilities.getName(AttributeUtilities.ATT_TEMPERATURE) + ":"),
				createConstraints(0, 8));
		settingsPanel.add(new JLabel(AttributeUtilities.getName(AttributeUtilities.ATT_PH) + ":"),
				createConstraints(0, 9));
		settingsPanel.add(new JLabel(AttributeUtilities.getName(AttributeUtilities.ATT_AW) + ":"),
				createConstraints(0, 10));

		settingsPanel.add(new JScrollPane(literatureList), new GridBagConstraints(1, 0, 2, 2, 0, 2,
				GridBagConstraints.LINE_START, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		settingsPanel.add(agentButton, createConstraints(1, 2));
		settingsPanel.add(matrixButton, createConstraints(1, 3));
		settingsPanel.add(idField, createConstraints(1, 4));
		settingsPanel.add(commentField, createConstraints(1, 5));
		settingsPanel.add(temperatureField, createConstraints(1, 8));
		settingsPanel.add(phField, createConstraints(1, 9));
		settingsPanel.add(waterActivityField, createConstraints(1, 10));

		settingsPanel.add(timeBox, createConstraints(2, 6));
		settingsPanel.add(logcBox, createConstraints(2, 7));
		settingsPanel.add(tempBox, createConstraints(2, 8));
		settingsPanel.add(phBox, createConstraints(2, 9));
		settingsPanel.add(awBox, createConstraints(2, 10));

		settingsPanel.add(addLiteratureButton, createConstraints(3, 0));
		settingsPanel.add(removeLiteratureButton, createConstraints(4, 0));

		settingsPanelRows = 11;

		addButtons(0);

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(xlsButton);
		buttonPanel.add(stepsButton);
		buttonPanel.add(clearButton);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new BorderLayout());
		northPanel.add(settingsPanel, BorderLayout.WEST);

		table = new TimeSeriesTable(ROW_COUNT, 1, true, true);
		panel.setLayout(new BorderLayout());
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
			throws NotConfigurableException {
		set = new SettingsHelper();
		set.loadSettings(settings);

		literatureList.setListData(set.getLiterature().toArray(new LiteratureItem[0]));

		timeBox.setSelectedItem(set.getTimeUnit());
		logcBox.setSelectedItem(set.getLogcUnit());

		if (set.getAgent() != null) {
			agentButton.setText(set.getAgent().name);
		}

		if (set.getMatrix() != null) {
			matrixButton.setText(set.getMatrix().getName());
		}

		if (set.getComment() != null) {
			commentField.setText(set.getComment());
		} else {
			commentField.setText("");
		}

		if (set.getId() != null) {
			idField.setText(set.getId());
		} else {
			idField.setText("");
		}

		for (int i = 0; i < set.getTimeSeries().size(); i++) {
			table.setTime(i, set.getTimeSeries().get(i).getTime());
			table.setLogc(i, set.getTimeSeries().get(i).getConcentration());
		}

		int n = removeButtons.size();

		for (int i = 0; i < n; i++) {
			removeButtons(0);
		}

		for (MiscXml misc : set.getMisc()) {
			if (misc.getId() == AttributeUtilities.ATT_TEMPERATURE_ID) {
				temperatureField.setValue(misc.getValue());
				tempBox.setSelectedItem(misc.getUnit());
			} else if (misc.getId() == AttributeUtilities.ATT_PH_ID) {
				phField.setValue(misc.getValue());
				phBox.setSelectedItem(misc.getUnit());
			} else if (misc.getId() == AttributeUtilities.ATT_AW_ID) {
				waterActivityField.setValue(misc.getValue());
				awBox.setSelectedItem(misc.getUnit());
			} else {
				addButtons(0);
				condButtons.get(0).setText(misc.getName());
				conditions.set(0, misc);
				condValueFields.get(0).setValue(misc.getValue());

				for (String category : misc.getCategories()) {
					for (String u : Categories.getCategory(category).getAllUnits()) {
						condUnitFields.get(0).addItem(u);
					}
				}

				condUnitFields.get(0).setSelectedItem(misc.getUnit());
			}
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		if (!temperatureField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (!phField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (!waterActivityField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		for (MiscXml m : conditions) {
			if (m == null) {
				throw new InvalidSettingsException("Invalid Value");
			}
		}

		for (int i = 0; i < condValueFields.size(); i++) {
			if (!condValueFields.get(i).isValueValid()) {
				throw new InvalidSettingsException("Invalid Value");
			}
		}

		List<TimeSeriesXml> timeSeries = new ArrayList<>();
		String timeUnit = (String) timeBox.getSelectedItem();
		String concentrationUnit = (String) logcBox.getSelectedItem();
		List<MiscXml> miscValues = new ArrayList<>();

		if (temperatureField.getValue() != null) {
			miscValues.add(new MiscXml(AttributeUtilities.ATT_TEMPERATURE_ID, AttributeUtilities.ATT_TEMPERATURE, null,
					temperatureField.getValue(), Arrays.asList(Categories.getTempCategory().getName()),
					(String) tempBox.getSelectedItem()));
		}

		if (phField.getValue() != null) {
			miscValues
					.add(new MiscXml(AttributeUtilities.ATT_PH_ID, AttributeUtilities.ATT_PH, null, phField.getValue(),
							Arrays.asList(Categories.getPhCategory().getName()), (String) phBox.getSelectedItem()));
		}

		if (waterActivityField.getValue() != null) {
			miscValues.add(new MiscXml(AttributeUtilities.ATT_AW_ID, AttributeUtilities.ATT_AW, null,
					waterActivityField.getValue(), Arrays.asList(Categories.getAwCategory().getName()),
					(String) awBox.getSelectedItem()));
		}

		for (int i = 0; i < conditions.size(); i++) {
			MiscXml cond = conditions.get(i);

			cond.setValue(condValueFields.get(i).getValue());

			if (condUnitFields.get(i).getSelectedItem() != null) {
				cond.setUnit((String) condUnitFields.get(i).getSelectedItem());
			}

			miscValues.add(cond);
		}

		for (int i = 0; i < ROW_COUNT; i++) {
			Double time = table.getTime(i);
			Double logc = table.getLogc(i);

			if (time != null || logc != null) {
				timeSeries.add(new TimeSeriesXml(null, time, timeUnit, logc, concentrationUnit, null, null));
			}
		}

		set.setComment(commentField.getValue());
		set.setId(idField.getValue());
		set.setTimeSeries(timeSeries);
		set.setTimeUnit((String) timeBox.getSelectedItem());
		set.setLogcUnit((String) logcBox.getSelectedItem());
		set.setMisc(miscValues);
		set.saveSettings(settings);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == xlsButton) {
			loadFromXLS();
		} else if (event.getSource() == clearButton) {
			int n = removeButtons.size();

			agentButton.setText(SELECT);
			set.setAgent(null);
			matrixButton.setText(SELECT);
			set.setMatrix(null);
			commentField.setValue(null);
			temperatureField.setValue(null);
			phField.setValue(null);
			waterActivityField.setValue(null);

			for (int i = 0; i < n; i++) {
				removeButtons(0);
			}

			for (int i = 0; i < ROW_COUNT; i++) {
				table.setTime(i, null);
				table.setLogc(i, null);
			}

			panel.revalidate();
			table.repaint();
		} else if (event.getSource() == stepsButton) {
			TimeStepDialog dialog = new TimeStepDialog(panel);

			dialog.setVisible(true);

			if (dialog.isApproved()) {
				int stepNumber = dialog.getNumberOfSteps();
				double stepSize = dialog.getStepSize();

				for (int i = 0; i < ROW_COUNT; i++) {
					Double time = null;

					if (i < stepNumber) {
						time = i * stepSize;
					}

					table.setTime(i, time);
					table.setLogc(i, null);
				}

				table.repaint();
			}
		} else if (event.getSource() == addLiteratureButton) {
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
		} else if (event.getSource() == removeLiteratureButton) {
			if (literatureList.getSelectedIndices().length > 0) {
				int[] indices = literatureList.getSelectedIndices();

				Arrays.sort(indices);

				for (int i = indices.length - 1; i >= 0; i--) {
					set.getLiterature().remove(indices[i]);
				}

				literatureList.setListData(set.getLiterature().toArray(new LiteratureItem[0]));
			}
		} else if (event.getSource() == agentButton) {
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
		} else if (event.getSource() == matrixButton) {
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
		} else if (addButtons.contains(event.getSource())) {
			addButtons(addButtons.indexOf(event.getSource()));
			panel.revalidate();
		} else if (removeButtons.contains(event.getSource())) {
			removeButtons(removeButtons.indexOf(event.getSource()));
			panel.revalidate();
		} else if (condButtons.contains(event.getSource())) {
			int i = condButtons.indexOf(event.getSource());
			Integer id;

			if (conditions.get(i) != null) {
				id = DBKernel.openMiscDBWindow(condButtons.get(i), conditions.get(i).getId());
			} else {
				id = DBKernel.openMiscDBWindow(condButtons.get(i), null);
			}

			if (id != null) {
				String name = DBKernel.getValue("SonstigeParameter", "ID", id + "", "Parameter") + "";
				String description = DBKernel.getValue("SonstigeParameter", "ID", id + "", "Beschreibung") + "";
				List<String> categoryIDs = Arrays.asList(
						DBKernel.getValue("SonstigeParameter", "ID", id + "", "Kategorie").toString().split(","));

				condButtons.get(i).setText(name);
				conditions.set(i,
						new MiscXml(id, name, description, null, categoryIDs, null, DBKernel.getLocalDBUUID()));
				condUnitFields.get(i).removeAllItems();

				for (String categoryID : categoryIDs) {
					Category category = Categories.getCategory(categoryID);

					for (String u : category.getAllUnits()) {
						condUnitFields.get(i).addItem(u);
					}

					condUnitFields.get(i).setSelectedItem(category.getStandardUnit());
				}
			}
		}
	}

	private void addButtons(int i) {
		if (addButtons.isEmpty()) {
			JButton addButton = new JButton("+");

			addButton.addActionListener(this);

			addButtons.add(0, addButton);
			settingsPanel.add(addButton, createConstraints(3, settingsPanelRows));
		} else {
			JButton addButton = new JButton("+");
			JButton removeButton = new JButton("-");
			JButton button = new JButton(OTHER_PARAMETER);
			DoubleTextField valueField = new DoubleTextField(true);
			JComboBox<String> unitBox = new JComboBox<>();

			addButton.addActionListener(this);
			removeButton.addActionListener(this);
			button.addActionListener(this);
			valueField.setPreferredSize(new Dimension(100, valueField.getPreferredSize().height));

			for (JButton c : addButtons) {
				settingsPanel.remove(c);
			}

			for (JButton c : removeButtons) {
				settingsPanel.remove(c);
			}

			for (JButton c : condButtons) {
				settingsPanel.remove(c);
			}

			for (DoubleTextField c : condValueFields) {
				settingsPanel.remove(c);
			}

			for (JComboBox<String> c : condUnitFields) {
				settingsPanel.remove(c);
			}

			addButtons.add(i, addButton);
			removeButtons.add(i, removeButton);
			conditions.add(i, null);
			condButtons.add(i, button);
			condValueFields.add(i, valueField);
			condUnitFields.add(i, unitBox);

			for (int j = 0; j < addButtons.size(); j++) {
				settingsPanel.add(addButtons.get(j), createConstraints(3, settingsPanelRows + j));
			}

			for (int j = 0; j < removeButtons.size(); j++) {
				settingsPanel.add(removeButtons.get(j), createConstraints(4, settingsPanelRows + j));
			}

			for (int j = 0; j < condButtons.size(); j++) {
				settingsPanel.add(condButtons.get(j), createConstraints(0, settingsPanelRows + j));
			}

			for (int j = 0; j < condValueFields.size(); j++) {
				settingsPanel.add(condValueFields.get(j), createConstraints(1, settingsPanelRows + j));
			}

			for (int j = 0; j < condUnitFields.size(); j++) {
				settingsPanel.add(condUnitFields.get(j), createConstraints(2, settingsPanelRows + j));
			}

			settingsPanel.revalidate();
		}
	}

	private void removeButtons(int i) {
		settingsPanel.remove(addButtons.remove(i));
		settingsPanel.remove(removeButtons.remove(i));
		conditions.remove(i);
		settingsPanel.remove(condButtons.remove(i));
		settingsPanel.remove(condValueFields.remove(i));
		settingsPanel.remove(condUnitFields.remove(i));
	}

	private void loadFromXLS() {
		JFileChooser fileChooser = new JFileChooser();
		FileFilter xlsFilter = new FileFilter() {

			@Override
			public String getDescription() {
				return "Excel Spreadsheat (*.xls)";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".xls");
			}
		};

		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(xlsFilter);

		if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
			try {
				Object[] sheets = xlsReader.getSheets(fileChooser.getSelectedFile()).toArray();
				Object sheet = JOptionPane.showInputDialog(panel, "Select Sheet", "Input", JOptionPane.QUESTION_MESSAGE,
						null, sheets, sheets[0]);
				XLSDialog dialog = new XLSDialog(panel, fileChooser.getSelectedFile(), (String) sheet);

				dialog.setVisible(true);

				if (!dialog.isApproved()) {
					return;
				}

				Map<String, KnimeTuple> tuples = null;

				try {
					tuples = xlsReader.getTimeSeriesTuples(fileChooser.getSelectedFile(), (String) sheet,
							dialog.getMappings(), dialog.getTimeUnit(), dialog.getConcentrationUnit(), null, null, null,
							null, false, new ArrayList<Integer>());

					if (!xlsReader.getWarnings().isEmpty()) {
						JOptionPane.showMessageDialog(panel, xlsReader.getWarnings().get(0), "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

				Object[] values = tuples.keySet().toArray();
				Object selection = JOptionPane.showInputDialog(panel, "Select Time Series", "Input",
						JOptionPane.QUESTION_MESSAGE, null, values, values[0]);
				KnimeTuple tuple = tuples.get(selection);

				agentButton.setText(SELECT);
				set.setAgent(null);
				matrixButton.setText(SELECT);
				set.setMatrix(null);
				idField.setValue(tuple.getString(TimeSeriesSchema.ATT_COMBASEID));
				commentField.setValue(((MdInfoXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO).get(0)).getComment());

				PmmXmlDoc miscXML = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				int n = removeButtons.size();

				for (int i = 0; i < n; i++) {
					removeButtons(0);
				}

				temperatureField.setValue(null);
				phField.setValue(null);
				waterActivityField.setValue(null);

				for (int i = 0; i < miscXML.getElementSet().size(); i++) {
					MiscXml misc = (MiscXml) miscXML.getElementSet().get(i);
					int id = misc.getId();
					String name = misc.getName();
					Double value = misc.getValue();
					String unit = misc.getUnit();

					if (value != null && value.isNaN()) {
						value = null;
					}

					if (id == AttributeUtilities.ATT_TEMPERATURE_ID) {
						temperatureField.setValue(value);
						tempBox.setSelectedItem(unit);
					} else if (id == AttributeUtilities.ATT_PH_ID) {
						phField.setValue(value);
						phBox.setSelectedItem(unit);
					} else if (id == AttributeUtilities.ATT_AW_ID) {
						waterActivityField.setValue(value);
						awBox.setSelectedItem(unit);
					} else {
						addButtons(0);
						condButtons.get(0).setText(name);
						conditions.set(0, misc);
						condValueFields.get(0).setValue(value);
						condUnitFields.get(0).removeAllItems();

						for (String u : Categories.getUnitsFromCategories(misc.getCategories())) {
							condUnitFields.get(0).addItem(u);
						}

						condUnitFields.get(0).setSelectedItem(unit);
					}
				}

				PmmXmlDoc timeSeriesXml = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				int count = timeSeriesXml.getElementSet().size();

				if (count > ROW_COUNT) {
					JOptionPane.showMessageDialog(panel,
							"Number of measured points XLS-file exceeds maximum number of rows (" + ROW_COUNT + ")",
							"Warning", JOptionPane.WARNING_MESSAGE);
				}

				for (int i = 0; i < ROW_COUNT; i++) {
					Double time = null;
					Double logc = null;

					if (i < count) {
						time = ((TimeSeriesXml) timeSeriesXml.get(i)).getTime();
						logc = ((TimeSeriesXml) timeSeriesXml.get(i)).getConcentration();
						timeBox.setSelectedItem(((TimeSeriesXml) timeSeriesXml.get(i)).getTimeUnit());
						logcBox.setSelectedItem(((TimeSeriesXml) timeSeriesXml.get(i)).getConcentrationUnit());
					}

					table.setTime(i, time);
					table.setLogc(i, logc);
				}

				panel.revalidate();
				table.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static GridBagConstraints createConstraints(int x, int y) {
		return new GridBagConstraints(x, y, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
				new Insets(3, 3, 3, 3), 0, 0);
	}

	private class XLSDialog extends JDialog implements ActionListener, ItemListener {

		private static final long serialVersionUID = 1L;

		private boolean approved;

		private Map<String, JComboBox<String>> mappingBoxes;
		private Map<String, JButton> mappingButtons;
		private Map<String, JComboBox<String>> unitBoxes;
		private Map<String, Object> mappings;
		private String timeUnit;
		private String concentrationUnit;

		private JButton okButton;
		private JButton cancelButton;

		public XLSDialog(Component owner, File file, String sheet) throws Exception {
			super(SwingUtilities.getWindowAncestor(owner), "XLS File", DEFAULT_MODALITY_TYPE);

			approved = false;

			mappings = new LinkedHashMap<>();
			timeUnit = null;
			concentrationUnit = null;

			mappingBoxes = new LinkedHashMap<>();
			mappingButtons = new LinkedHashMap<>();
			unitBoxes = new LinkedHashMap<>();

			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			List<String> columnList = xlsReader.getColumns(file, sheet);
			JPanel northPanel = new JPanel();
			int row = 0;

			northPanel.setLayout(new GridBagLayout());

			for (String column : columnList) {
				JComboBox<String> box = new JComboBox<>(new String[] { XLSReader.ID_COLUMN, MdInfoXml.ATT_COMMENT,
						AttributeUtilities.TIME, AttributeUtilities.CONCENTRATION, AttributeUtilities.ATT_TEMPERATURE,
						AttributeUtilities.ATT_PH, AttributeUtilities.ATT_AW, OTHER_PARAMETER, NO_PARAMETER });
				JButton button = new JButton();
				JComboBox<String> unitBox = new JComboBox<>();

				box.setSelectedItem(NO_PARAMETER);
				button.setEnabled(false);
				button.setText(OTHER_PARAMETER);

				box.addItemListener(this);
				button.addActionListener(this);
				unitBox.addItemListener(this);

				mappingBoxes.put(column, box);
				mappingButtons.put(column, button);
				unitBoxes.put(column, unitBox);

				northPanel.add(new JLabel(column + ":"), createConstraints(0, row));
				northPanel.add(box, createConstraints(1, row));
				northPanel.add(button, createConstraints(2, row));
				northPanel.add(unitBox, createConstraints(3, row));
				row++;
			}

			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setResizable(false);
			setLocationRelativeTo(owner);
			UI.adjustDialog(this);
		}

		public boolean isApproved() {
			return approved;
		}

		public Map<String, Object> getMappings() {
			return mappings;
		}

		public String getTimeUnit() {
			return timeUnit;
		}

		public String getConcentrationUnit() {
			return concentrationUnit;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				for (String column : mappingBoxes.keySet()) {
					if (e.getSource() == mappingBoxes.get(column)) {
						JComboBox<String> box = mappingBoxes.get(column);
						JButton button = mappingButtons.get(column);
						String selected = (String) box.getSelectedItem();

						if (selected.equals(XLSReader.ID_COLUMN) || selected.equals(MdInfoXml.ATT_COMMENT)) {
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
							mappings.put(column, selected);
						} else if (selected.equals(AttributeUtilities.TIME)) {
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
							mappings.put(column, selected);
							unitBoxes.get(column).removeAllItems();

							for (String unit : Categories.getTimeCategory().getAllUnits()) {
								unitBoxes.get(column).addItem(unit);
							}

							pack();
						} else if (selected.equals(AttributeUtilities.CONCENTRATION)) {
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
							mappings.put(column, selected);
							unitBoxes.get(column).removeAllItems();

							for (String unit : Categories
									.getUnitsFromCategories(Categories.getConcentrationCategories())) {
								unitBoxes.get(column).addItem(unit);
							}

							pack();
						} else if (selected.equals(AttributeUtilities.ATT_TEMPERATURE)) {
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
							mappings.put(column,
									new MiscXml(AttributeUtilities.ATT_TEMPERATURE_ID,
											AttributeUtilities.ATT_TEMPERATURE, null, null,
											Arrays.asList(Categories.getTempCategory().getName()),
											Categories.getTempCategory().getStandardUnit()));
							unitBoxes.get(column).removeAllItems();

							for (String unit : Categories.getTempCategory().getAllUnits()) {
								unitBoxes.get(column).addItem(unit);
							}

							pack();
						} else if (selected.equals(AttributeUtilities.ATT_PH)) {
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
							mappings.put(column,
									new MiscXml(AttributeUtilities.ATT_PH_ID, AttributeUtilities.ATT_PH, null, null,
											Arrays.asList(Categories.getPhCategory().getName()),
											Categories.getPhUnit()));
							unitBoxes.get(column).removeAllItems();

							for (String unit : Categories.getPhCategory().getAllUnits()) {
								unitBoxes.get(column).addItem(unit);
							}

							pack();
						} else if (selected.equals(AttributeUtilities.ATT_AW)) {
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
							mappings.put(column,
									new MiscXml(AttributeUtilities.ATT_AW_ID, AttributeUtilities.ATT_AW, null, null,
											Arrays.asList(Categories.getAwCategory().getName()),
											Categories.getAwUnit()));
							unitBoxes.get(column).removeAllItems();

							for (String unit : Categories.getAwCategory().getAllUnits()) {
								unitBoxes.get(column).addItem(unit);
							}

							pack();
						} else if (selected.equals(OTHER_PARAMETER)) {
							button.setEnabled(true);
							button.setText(OTHER_PARAMETER);
							mappings.put(column, null);
						} else if (selected.equals(NO_PARAMETER)) {
							button.setEnabled(false);
							button.setText(OTHER_PARAMETER);
							mappings.remove(column);
						}

						break;
					}
				}

				for (String column : unitBoxes.keySet()) {
					if (e.getSource() == unitBoxes.get(column)) {
						String unit = (String) unitBoxes.get(column).getSelectedItem();

						if (mappings.get(column) instanceof MiscXml) {
							MiscXml condition = (MiscXml) mappings.get(column);

							condition.setUnit(unit);
						} else if (mappings.get(column) instanceof String) {
							String mapping = (String) mappings.get(column);

							if (mapping.equals(AttributeUtilities.TIME)) {
								timeUnit = unit;
							} else if (mapping.equals(AttributeUtilities.CONCENTRATION)) {
								concentrationUnit = unit;
							}
						}

						break;
					}
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				for (Object value : mappings.values()) {
					if (value == null) {
						JOptionPane.showMessageDialog(this, "All Columns must be assigned", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				approved = true;
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			} else {
				for (String column : mappingButtons.keySet()) {
					if (e.getSource() == mappingButtons.get(column)) {
						Integer oldID = null;

						if (mappings.get(column) instanceof MiscXml) {
							oldID = ((MiscXml) mappings.get(column)).getId();
						}

						Integer id = DBKernel.openMiscDBWindow(mappingButtons.get(column), oldID);

						if (id != null) {
							String name = DBKernel.getValue("SonstigeParameter", "ID", id + "", "Parameter") + "";
							String description = DBKernel.getValue("SonstigeParameter", "ID", id + "", "Beschreibung")
									+ "";
							List<String> categoryIDs = Arrays.asList(DBKernel
									.getValue("SonstigeParameter", "ID", id + "", "Kategorie").toString().split(","));

							unitBoxes.get(column).removeAllItems();

							String unit = null;

							for (String categoryID : categoryIDs) {
								Category category = Categories.getCategory(categoryID);

								for (String u : category.getAllUnits()) {
									unitBoxes.get(column).addItem(u);
								}

								unit = category.getStandardUnit();
								unitBoxes.get(column).setSelectedItem(unit);

								mappingButtons.get(column).setText(name);
							}

							mappings.put(column, new MiscXml(id, name, description, null, categoryIDs, unit));
							pack();
						}

						break;
					}
				}
			}
		}
	}

	private class TimeStepDialog extends JDialog implements ActionListener, TextListener {

		private static final long serialVersionUID = 1L;

		private boolean approved;
		private int numberOfSteps;
		private double stepSize;

		private IntTextField numberField;
		private DoubleTextField sizeField;

		private JButton okButton;
		private JButton cancelButton;

		public TimeStepDialog(Component owner) {
			super(SwingUtilities.getWindowAncestor(owner), "Time Steps", DEFAULT_MODALITY_TYPE);

			approved = false;
			numberOfSteps = 0;
			stepSize = 0.0;

			numberField = new IntTextField(1, ROW_COUNT);
			numberField.setValue(DEFAULT_TIMESTEPNUMBER);
			numberField.setPreferredSize(new Dimension(150, numberField.getPreferredSize().height));
			numberField.addTextListener(this);
			sizeField = new DoubleTextField(0.0, Double.POSITIVE_INFINITY);
			sizeField.setPreferredSize(new Dimension(150, sizeField.getPreferredSize().height));
			sizeField.setValue(DEFAULT_TIMESTEPSIZE);
			sizeField.addTextListener(this);
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel centerPanel = new JPanel();
			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();
			JPanel bottomPanel = new JPanel();

			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			leftPanel.setLayout(new GridLayout(2, 1, 5, 5));
			leftPanel.add(new JLabel("Number of Time Steps:"));
			leftPanel.add(new JLabel("Step Size:"));

			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			rightPanel.setLayout(new GridLayout(2, 1, 5, 5));
			rightPanel.add(numberField);
			rightPanel.add(sizeField);

			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(leftPanel, BorderLayout.WEST);
			centerPanel.add(rightPanel, BorderLayout.CENTER);

			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(centerPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setResizable(false);
			setLocationRelativeTo(owner);
			UI.adjustDialog(this);
		}

		public boolean isApproved() {
			return approved;
		}

		public int getNumberOfSteps() {
			return numberOfSteps;
		}

		public double getStepSize() {
			return stepSize;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				approved = true;
				numberOfSteps = numberField.getValue();
				stepSize = sizeField.getValue();
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}

		@Override
		public void textChanged(Object source) {
			if (numberField.isValueValid() && sizeField.isValueValid()) {
				okButton.setEnabled(true);
			} else {
				okButton.setEnabled(false);
			}
		}
	}

}
