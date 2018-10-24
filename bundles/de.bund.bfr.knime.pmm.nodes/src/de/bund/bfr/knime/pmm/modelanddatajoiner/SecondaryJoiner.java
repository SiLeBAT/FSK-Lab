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
package de.bund.bfr.knime.pmm.modelanddatajoiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

public class SecondaryJoiner implements Joiner, ActionListener {

	private BufferedDataTable modelTable;
	private BufferedDataTable dataTable;

	private Map<String, List<Map<String, String>>> assignmentsMap;

	private List<String> models;
	private Map<String, String> modelNames;
	private Map<String, String> modelFormulas;
	private Map<String, String> dependentVariables;
	private Map<String, Map<String, String>> independentVariableCategories;
	private Map<String, Map<String, String>> independentVariableUnits;
	private Map<Integer, List<String>> dependentParameters;
	private Map<Integer, String> primaryModelNames;
	private Map<String, String> independentParameterCategories;

	private Map<String, JPanel> boxPanels;
	private Map<String, JPanel> buttonPanels;
	private Map<String, List<Map<String, JComboBox<String>>>> comboBoxes;
	private Map<String, List<JButton>> addButtons;
	private Map<String, List<JButton>> removeButtons;

	private boolean isValid;

	public SecondaryJoiner(BufferedDataTable modelTable,
			BufferedDataTable dataTable) {
		this.modelTable = modelTable;
		this.dataTable = dataTable;

		readModelTable();
		readDataTable();
	}

	@Override
	public JComponent createPanel(String assignments) {
		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel();

		boxPanels = new LinkedHashMap<>();
		buttonPanels = new LinkedHashMap<>();
		comboBoxes = new LinkedHashMap<>();
		addButtons = new LinkedHashMap<>();
		removeButtons = new LinkedHashMap<>();
		panel.setLayout(new BorderLayout());
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		assignmentsMap = XmlConverter.xmlToObject(assignments,
				new LinkedHashMap<String, List<Map<String, String>>>());

		for (String modelID : models) {
			List<Map<String, String>> modelAssignments = new ArrayList<>();
			List<Map<String, JComboBox<String>>> modelBoxes = new ArrayList<>();
			List<JButton> modelAddButtons = new ArrayList<>();
			List<JButton> modelRemoveButtons = new ArrayList<>();

			if (assignmentsMap.containsKey(modelID)) {
				modelAssignments = assignmentsMap.get(modelID);
			}

			JPanel modelPanel = new JPanel();
			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();

			leftPanel.setLayout(new GridLayout(0, 1));
			rightPanel.setLayout(new GridLayout(0, 1));

			for (Map<String, String> assignment : modelAssignments) {
				Map<String, JComboBox<String>> boxes = new LinkedHashMap<>();
				JPanel assignmentPanel = new JPanel();

				assignmentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

				JComboBox<String> depBox = new JComboBox<>(getDepParams()
						.toArray(new String[0]));

				depBox.setSelectedItem(assignment.get(dependentVariables
						.get(modelID)));
				depBox.addActionListener(this);
				boxes.put(dependentVariables.get(modelID), depBox);
				assignmentPanel.add(new JLabel(dependentVariables.get(modelID)
						+ ":"));
				assignmentPanel.add(depBox);

				for (String indepVar : independentVariableCategories.get(
						modelID).keySet()) {
					JComboBox<String> indepBox = new JComboBox<>(
							getIndepParamsFromCategory(
									independentVariableCategories.get(modelID)
											.get(indepVar)).toArray(
									new String[0]));

					indepBox.setSelectedItem(assignment.get(indepVar));
					indepBox.addActionListener(this);
					boxes.put(indepVar, indepBox);
					assignmentPanel.add(new JLabel(indepVar + ":"));
					assignmentPanel.add(indepBox);
				}

				modelBoxes.add(boxes);
				leftPanel.add(assignmentPanel);

				JPanel buttonPanel = new JPanel();
				JButton addButton = new JButton("+");
				JButton removeButton = new JButton("-");

				addButton.addActionListener(this);
				removeButton.addActionListener(this);
				modelAddButtons.add(addButton);
				modelRemoveButtons.add(removeButton);
				buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
				buttonPanel.add(removeButton);
				buttonPanel.add(addButton);
				rightPanel.add(buttonPanel);
			}

			JPanel buttonPanel = new JPanel();
			JButton addButton = new JButton("+");

			addButton.addActionListener(this);
			modelAddButtons.add(addButton);
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel.add(addButton);
			leftPanel.add(new JPanel());
			rightPanel.add(buttonPanel);

			boxPanels.put(modelID, leftPanel);
			buttonPanels.put(modelID, rightPanel);
			comboBoxes.put(modelID, modelBoxes);
			addButtons.put(modelID, modelAddButtons);
			removeButtons.put(modelID, modelRemoveButtons);
			modelPanel.setBorder(BorderFactory.createTitledBorder(modelNames
					.get(modelID)));
			modelPanel.setLayout(new BorderLayout());
			modelPanel.setToolTipText(modelFormulas.get(modelID));
			modelPanel.add(leftPanel, BorderLayout.CENTER);
			modelPanel.add(rightPanel, BorderLayout.EAST);
			topPanel.add(modelPanel);
		}

		panel.add(topPanel, BorderLayout.NORTH);
		checkIfInputIsValid();

		return new JScrollPane(panel);
	}

	@Override
	public String getAssignments() {
		Map<String, List<Map<String, String>>> assignmentsMap = new LinkedHashMap<>();

		for (String model : comboBoxes.keySet()) {
			List<Map<String, String>> modelAssignments = new ArrayList<>();

			for (Map<String, JComboBox<String>> modelBoxes : comboBoxes
					.get(model)) {
				Map<String, String> assignment = new LinkedHashMap<>();

				for (String var : modelBoxes.keySet()) {
					JComboBox<String> box = modelBoxes.get(var);

					assignment.put(var, (String) box.getSelectedItem());
				}

				modelAssignments.add(assignment);
			}

			assignmentsMap.put(model, modelAssignments);
		}

		return XmlConverter.objectToXml(assignmentsMap);
	}

	@Override
	public BufferedDataTable getOutputTable(String assignments,
			ExecutionContext exec) throws CanceledExecutionException,
			ConvertException {
		BufferedDataContainer buf = exec.createDataContainer(SchemaFactory
				.createM12DataSchema().createSpec());

		assignmentsMap = XmlConverter.xmlToObject(assignments,
				new LinkedHashMap<String, List<Map<String, String>>>());

		Map<Integer, Integer> globalIds = new LinkedHashMap<>();

		for (String model : assignmentsMap.keySet()) {
			for (Map<String, String> replace : assignmentsMap.get(model)) {
				KnimeRelationReader modelReader = new KnimeRelationReader(
						SchemaFactory.createM2Schema(), modelTable);
				Set<Integer> usedEstIDs = new LinkedHashSet<>();

				while (modelReader.hasMoreElements()) {
					KnimeTuple modelTuple = modelReader.nextElement();
					PmmXmlDoc modelXmlSec = modelTuple
							.getPmmXml(Model2Schema.ATT_MODELCATALOG);
					String modelIDSec = ((CatalogModelXml) modelXmlSec.get(0))
							.id + "";
					Integer estIDSec = ((EstModelXml) modelTuple.getPmmXml(
							Model2Schema.ATT_ESTMODEL).get(0)).id;

					if (!model.equals(modelIDSec) || !usedEstIDs.add(estIDSec)) {
						continue;
					}

					String formulaSec = ((CatalogModelXml) modelXmlSec.get(0))
							.formula;
					PmmXmlDoc depVarSec = modelTuple
							.getPmmXml(Model2Schema.ATT_DEPENDENT);
					String depVarSecName = ((DepXml) depVarSec.get(0))
							.name;
					PmmXmlDoc indepVarsSec = modelTuple
							.getPmmXml(Model2Schema.ATT_INDEPENDENT);
					PmmXmlDoc newIndepVarsSec = new PmmXmlDoc();
					List<String> oldIndepVars = new ArrayList<>();

					if (!replace.containsKey(depVarSecName)) {
						continue;
					}

					String s = replace.get(depVarSecName);
					String newDepVarSecName = s.substring(0, s.indexOf(" "));
					String modelName = s.substring(s.indexOf("(") + 1,
							s.length() - 1);
					int modelID = -1;

					for (int id : primaryModelNames.keySet()) {
						if (primaryModelNames.get(id).equals(modelName)) {
							modelID = id;
							break;
						}
					}

					formulaSec = MathUtilities.replaceVariable(formulaSec,
							depVarSecName, newDepVarSecName);
					((DepXml) depVarSec.get(0)).name = newDepVarSecName;

					boolean error = false;

					for (PmmXmlElementConvertable el : indepVarsSec
							.getElementSet()) {
						IndepXml iv = (IndepXml) el;

						if (!replace.containsKey(iv.name)) {
							error = true;
							break;
						}

						oldIndepVars.add(iv.name);
						formulaSec = MathUtilities.replaceVariable(formulaSec,
								iv.name, replace.get(iv.name));
						iv.name = replace.get(iv.name);
						newIndepVarsSec.add(iv);
					}

					if (error) {
						continue;
					}

					((CatalogModelXml) modelXmlSec.get(0)).formula = formulaSec;

					List<KnimeTuple> dataTuples = PmmUtilities.getTuples(
							dataTable, SchemaFactory.createM1DataSchema());
					Map<Integer, List<KnimeTuple>> tuplesByPrimID = new LinkedHashMap<>();
					Map<Integer, Map<String, String>> miscUnits = new LinkedHashMap<>();

					for (KnimeTuple dataTuple : dataTuples) {
						CatalogModelXml modelXml = (CatalogModelXml) dataTuple
								.getPmmXml(Model1Schema.ATT_MODELCATALOG)
								.get(0);

						if (!tuplesByPrimID.containsKey(modelXml.id)) {
							tuplesByPrimID.put(modelXml.id,
									new ArrayList<KnimeTuple>());
						}

						tuplesByPrimID.get(modelXml.id).add(dataTuple);
					}

					for (int primID : tuplesByPrimID.keySet()) {
						miscUnits.put(primID, PmmUtilities
								.getMiscUnits(tuplesByPrimID.get(primID)));
					}

					for (KnimeTuple dataTuple : dataTuples) {
						int id = ((CatalogModelXml) dataTuple.getPmmXml(
								Model1Schema.ATT_MODELCATALOG).get(0)).id;

						if (id != modelID) {
							continue;
						}

						PmmXmlDoc params = dataTuple
								.getPmmXml(Model1Schema.ATT_PARAMETER);
						PmmXmlDoc miscs = dataTuple
								.getPmmXml(TimeSeriesSchema.ATT_MISC);
						Map<String, String> paramsConvertTo = new LinkedHashMap<>();

						if (!CellIO.getNameList(params).contains(
								newDepVarSecName)) {
							continue;
						}

						for (String var : oldIndepVars) {
							String unit = independentVariableUnits.get(model)
									.get(var);
							String newVar = replace.get(var);

							if (unit != null) {
								paramsConvertTo.put(newVar, unit);
							} else {
								paramsConvertTo.put(newVar, miscUnits.get(id)
										.get(newVar));
							}
						}

						for (PmmXmlElementConvertable el : miscs
								.getElementSet()) {
							MiscXml element = (MiscXml) el;

							if (paramsConvertTo.containsKey(element.name)) {
								Category cat = Categories
										.getCategory(Categories
												.getCategoryByUnit(
														element.unit)
												.getName());
								String unit = paramsConvertTo.get(element
										.name);

								element.value = cat.convert(
										element.value, element.unit,
										unit);
								element.unit = unit;
							}
						}

						dataTuple.setValue(TimeSeriesSchema.ATT_MISC, miscs);

						KnimeTuple tuple = new KnimeTuple(
								SchemaFactory.createM12DataSchema(),
								modelTuple, dataTuple);

						tuple.setValue(Model2Schema.ATT_MODELCATALOG,
								modelXmlSec);
						tuple.setValue(Model2Schema.ATT_DEPENDENT, depVarSec);
						tuple.setValue(Model2Schema.ATT_INDEPENDENT,
								newIndepVarsSec);
						tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
								Model2Schema.NOTWRITABLE);

						if (!globalIds.containsKey(id)) {
							globalIds.put(id,
									MathUtilities.getRandomNegativeInt());
						}

						tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID,
								globalIds.get(id));

						buf.addRowToTable(tuple);
					}
				}
			}
		}

		buf.close();

		return buf.getTable();
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	private void readModelTable() {
		models = new ArrayList<>();
		modelNames = new LinkedHashMap<>();
		modelFormulas = new LinkedHashMap<>();
		dependentVariables = new LinkedHashMap<>();
		independentVariableCategories = new LinkedHashMap<>();
		independentVariableUnits = new LinkedHashMap<>();

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createM2Schema(), modelTable);

		while (reader.hasMoreElements()) {
			KnimeTuple row = reader.nextElement();
			PmmXmlDoc modelXml = row.getPmmXml(Model2Schema.ATT_MODELCATALOG);
			DepXml depXml = (DepXml) row.getPmmXml(Model2Schema.ATT_DEPENDENT)
					.get(0);
			String modelID = ((CatalogModelXml) modelXml.get(0)).id + "";

			if (dependentVariables.containsKey(modelID)) {
				continue;
			}

			models.add(modelID);
			modelNames.put(modelID,
					((CatalogModelXml) modelXml.get(0)).name);
			modelFormulas.put(modelID,
					((CatalogModelXml) modelXml.get(0)).formula);
			dependentVariables.put(modelID, depXml.name);

			Map<String, String> indepCategories = new LinkedHashMap<>();
			Map<String, String> indepUnits = new LinkedHashMap<>();

			for (PmmXmlElementConvertable el : row.getPmmXml(
					Model2Schema.ATT_INDEPENDENT).getElementSet()) {
				IndepXml element = (IndepXml) el;

				indepCategories.put(element.name, element.category);
				indepUnits.put(element.name, element.unit);
			}

			independentVariableCategories.put(modelID, indepCategories);
			independentVariableUnits.put(modelID, indepUnits);
		}
	}

	private void readDataTable() {
		dependentParameters = new LinkedHashMap<>();
		primaryModelNames = new LinkedHashMap<>();
		independentParameterCategories = new LinkedHashMap<>();

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createM1DataSchema(), dataTable);

		while (reader.hasMoreElements()) {
			KnimeTuple row = reader.nextElement();
			CatalogModelXml modelXml = (CatalogModelXml) row.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);

			if (!primaryModelNames.containsKey(modelXml.id)) {
				List<String> params = new ArrayList<>();

				for (PmmXmlElementConvertable el : row.getPmmXml(
						Model1Schema.ATT_PARAMETER).getElementSet()) {
					ParamXml element = (ParamXml) el;

					params.add(element.getName());
				}

				primaryModelNames.put(modelXml.id, modelXml.name);
				dependentParameters.put(modelXml.id, params);
			}

			for (PmmXmlElementConvertable el : row.getPmmXml(
					TimeSeriesSchema.ATT_MISC).getElementSet()) {
				MiscXml element = (MiscXml) el;

				independentParameterCategories.put(element.name,
						Categories.getCategoryByUnit(element.unit)
								.getName());
			}
		}
	}

	private List<String> getDepParams() {
		List<String> params = new ArrayList<>();

		for (int modelID : dependentParameters.keySet()) {
			for (String param : dependentParameters.get(modelID)) {
				params.add(param + " (" + primaryModelNames.get(modelID) + ")");
			}
		}

		return params;
	}

	private List<String> getIndepParamsFromCategory(String category) {
		List<String> params = new ArrayList<>();

		for (String param : independentParameterCategories.keySet()) {
			if (category == null
					|| independentParameterCategories.get(param).equals(
							category)) {
				params.add(param);
			}
		}

		return params;
	}

	private void addOrRemoveButtonPressed(JButton button) {
		for (String model : addButtons.keySet()) {
			List<JButton> modelAddButtons = addButtons.get(model);
			List<JButton> modelRemoveButtons = removeButtons.get(model);
			List<Map<String, JComboBox<String>>> modelBoxes = comboBoxes
					.get(model);
			JPanel leftPanel = boxPanels.get(model);
			JPanel rightPanel = buttonPanels.get(model);

			if (modelAddButtons.contains(button)) {
				int index = modelAddButtons.indexOf(button);
				Map<String, JComboBox<String>> boxes = new LinkedHashMap<>();
				JPanel assignmentPanel = new JPanel();

				assignmentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

				JComboBox<String> depBox = new JComboBox<>(getDepParams()
						.toArray(new String[0]));

				depBox.setSelectedItem(null);
				depBox.addActionListener(this);
				boxes.put(dependentVariables.get(model), depBox);
				assignmentPanel.add(new JLabel(dependentVariables.get(model)
						+ ":"));
				assignmentPanel.add(depBox);

				for (String indepVar : independentVariableCategories.get(model)
						.keySet()) {
					JComboBox<String> indepBox = new JComboBox<>(
							getIndepParamsFromCategory(
									independentVariableCategories.get(model)
											.get(indepVar)).toArray(
									new String[0]));

					indepBox.setSelectedItem(null);
					indepBox.addActionListener(this);
					boxes.put(indepVar, indepBox);
					assignmentPanel.add(new JLabel(indepVar + ":"));
					assignmentPanel.add(indepBox);
				}

				modelBoxes.add(index, boxes);
				leftPanel.add(assignmentPanel, index);

				JPanel buttonPanel = new JPanel();
				JButton addButton = new JButton("+");
				JButton removeButton = new JButton("-");

				addButton.addActionListener(this);
				removeButton.addActionListener(this);
				modelAddButtons.add(index, addButton);
				modelRemoveButtons.add(index, removeButton);
				buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
				buttonPanel.add(removeButton);
				buttonPanel.add(addButton);
				rightPanel.add(buttonPanel, index);
				leftPanel.revalidate();
				rightPanel.revalidate();

				break;
			} else if (modelRemoveButtons.contains(button)) {
				int index = modelRemoveButtons.indexOf(button);

				modelAddButtons.remove(index);
				modelRemoveButtons.remove(index);
				modelBoxes.remove(index);
				leftPanel.remove(index);
				rightPanel.remove(index);
				leftPanel.revalidate();
				rightPanel.revalidate();

				break;
			}
		}
	}

	private void checkIfInputIsValid() {
		Map<String, JComboBox<String>> depVarBoxes = new LinkedHashMap<>();
		isValid = true;

		for (String model : comboBoxes.keySet()) {
			String depVar = dependentVariables.get(model);

			for (Map<String, JComboBox<String>> boxes : comboBoxes.get(model)) {
				JComboBox<String> box = boxes.get(depVar);

				if (box.getSelectedItem() == null) {
					isValid = false;
				} else {
					JComboBox<String> sameValueBox = depVarBoxes.get(box
							.getSelectedItem());

					if (sameValueBox != null) {
						box.setForeground(Color.RED);
						sameValueBox.setForeground(Color.RED);
						isValid = false;
					} else {
						box.setForeground(Color.BLACK);
						depVarBoxes.put((String) box.getSelectedItem(), box);
					}
				}
			}
		}

		for (String model : comboBoxes.keySet()) {
			String depVar = dependentVariables.get(model);

			for (Map<String, JComboBox<String>> boxes : comboBoxes.get(model)) {
				Map<String, JComboBox<String>> indepVarBoxes = new LinkedHashMap<>();

				for (String var : boxes.keySet()) {
					if (var.equals(depVar)) {
						continue;
					}

					JComboBox<String> box = boxes.get(var);

					if (box.getSelectedItem() == null) {
						isValid = false;
					} else {
						JComboBox<String> sameValueBox = indepVarBoxes.get(box
								.getSelectedItem());

						if (sameValueBox != null) {
							box.setForeground(Color.RED);
							sameValueBox.setForeground(Color.RED);
							isValid = false;
						} else {
							box.setForeground(Color.BLACK);
							indepVarBoxes.put((String) box.getSelectedItem(),
									box);
						}
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			checkIfInputIsValid();
		} else if (e.getSource() instanceof JButton) {
			addOrRemoveButtonPressed((JButton) e.getSource());
			checkIfInputIsValid();
		}
	}

}
