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
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

public class CombinedJoiner implements Joiner {

	private BufferedDataTable modelTable;
	private BufferedDataTable dataTable;

	private Map<String, Map<String, JComboBox<String>>> primaryVariableBoxes;
	private Map<String, Map<String, JComboBox<String>>> secondaryVariableBoxes;

	private Map<String, String> primaryModelNames;
	private Map<String, String> secondaryModelNames;
	private Map<String, Map<String, String>> primaryVariableCategories;
	private Map<String, Map<String, String>> primaryVariableUnits;
	private Map<String, Map<String, String>> secondaryVariableCategories;
	private Map<String, Map<String, String>> secondaryVariableUnits;
	private Map<String, String> primaryParameterCategories;
	private Map<String, String> secondaryParameterCategories;

	private List<KnimeTuple> modelTuples;

	public CombinedJoiner(BufferedDataTable modelTable,
			BufferedDataTable dataTable) {
		this.modelTable = modelTable;
		this.dataTable = dataTable;

		readDataTable();
		readModelTable();
	}

	@Override
	public JComponent createPanel(String assignments) {
		Map<String, Map<String, String>> assignmentsMap = XmlConverter
				.xmlToObject(assignments,
						new LinkedHashMap<String, Map<String, String>>());
		JPanel panel = new JPanel();
		JPanel topPanel = new JPanel();

		panel.setLayout(new BorderLayout());
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		primaryVariableBoxes = new LinkedHashMap<>();
		secondaryVariableBoxes = new LinkedHashMap<>();

		for (String modelID : primaryModelNames.keySet()) {
			JPanel primaryPanel = new JPanel();
			Map<String, JComboBox<String>> boxes = new LinkedHashMap<>();
			Map<String, String> map = assignmentsMap.get(modelID);

			if (map == null) {
				map = new LinkedHashMap<>();
			}

			primaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			primaryPanel.setBorder(BorderFactory
					.createTitledBorder(primaryModelNames.get(modelID)));

			for (String var : primaryVariableCategories.get(modelID).keySet()) {
				JComboBox<String> box = new JComboBox<>(
						getPrimParamsFromCategory(
								primaryVariableCategories.get(modelID).get(var))
								.toArray(new String[0]));

				if (map.containsKey(var)) {
					box.setSelectedItem(map.get(var));
				} else {
					box.setSelectedItem(null);
				}

				boxes.put(var, box);
				primaryPanel.add(new JLabel(var + ":"));
				primaryPanel.add(box);
			}

			topPanel.add(primaryPanel);
			primaryVariableBoxes.put(modelID, boxes);
		}

		for (String modelID : secondaryVariableCategories.keySet()) {
			JPanel secondaryPanel = new JPanel();
			Map<String, JComboBox<String>> boxes = new LinkedHashMap<>();
			Map<String, String> map = assignmentsMap.get(modelID);

			if (map == null) {
				map = new LinkedHashMap<>();
			}

			secondaryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			secondaryPanel.setBorder(BorderFactory
					.createTitledBorder(secondaryModelNames.get(modelID)));

			for (String var : secondaryVariableCategories.get(modelID).keySet()) {
				JComboBox<String> box = new JComboBox<>(
						getSecParamsFromCategory(
								secondaryVariableCategories.get(modelID).get(
										var)).toArray(new String[0]));

				if (map.containsKey(var)) {
					box.setSelectedItem(map.get(var));
				} else {
					box.setSelectedItem(null);
				}

				boxes.put(var, box);
				secondaryPanel.add(new JLabel(var + ":"));
				secondaryPanel.add(box);
			}

			topPanel.add(secondaryPanel);
			secondaryVariableBoxes.put(modelID, boxes);
		}

		panel.add(topPanel, BorderLayout.NORTH);

		return new JScrollPane(panel);
	}

	@Override
	public String getAssignments() {
		Map<String, Map<String, String>> assignmentsMap = new LinkedHashMap<>();

		for (String modelID : primaryVariableBoxes.keySet()) {
			Map<String, String> primaryAssignments = new LinkedHashMap<>();

			for (String var : primaryVariableBoxes.get(modelID).keySet()) {
				String replacement = (String) primaryVariableBoxes.get(modelID)
						.get(var).getSelectedItem();

				primaryAssignments.put(var, replacement);
			}

			assignmentsMap.put(modelID, primaryAssignments);
		}

		for (String modelID : secondaryVariableBoxes.keySet()) {
			Map<String, String> secondaryAssignments = new LinkedHashMap<>();

			for (String var : secondaryVariableBoxes.get(modelID).keySet()) {
				String replacement = (String) secondaryVariableBoxes
						.get(modelID).get(var).getSelectedItem();

				secondaryAssignments.put(var, replacement);
			}

			assignmentsMap.put(modelID, secondaryAssignments);
		}

		return XmlConverter.objectToXml(assignmentsMap);
	}

	@Override
	public BufferedDataTable getOutputTable(String assignments,
			ExecutionContext exec) throws CanceledExecutionException,
			ConvertException {
		BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createM12DataSchema()
						.createSpec());
		Map<String, Map<String, String>> replacements = XmlConverter
				.xmlToObject(assignments,
						new LinkedHashMap<String, Map<String, String>>());
		long rowCount = modelTable.size() * dataTable.size();
		long index = 0;

		for (KnimeTuple modelTuple : modelTuples) {
			PmmXmlDoc modelXml = modelTuple
					.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String depVarSecName = ((DepXml) modelTuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).name;
			String modelID = ((CatalogModelXml) modelXml.get(0)).id + "";
			String modelIDSec = depVarSecName + " (" + modelID + ")";
			String formula = ((CatalogModelXml) modelXml.get(0)).formula;
			PmmXmlDoc depVar = modelTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
			String depVarName = ((DepXml) depVar.get(0)).name;
			PmmXmlDoc indepVar = modelTuple
					.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc newIndepVar = new PmmXmlDoc();
			PmmXmlDoc modelXmlSec = modelTuple
					.getPmmXml(Model2Schema.ATT_MODELCATALOG);
			String formulaSec = ((CatalogModelXml) modelXmlSec.get(0))
					.formula;
			PmmXmlDoc indepVarSec = modelTuple
					.getPmmXml(Model2Schema.ATT_INDEPENDENT);
			PmmXmlDoc newIndepVarSec = new PmmXmlDoc();
			Map<String, String> primAssign = replacements.get(modelID);
			Map<String, String> secAssign = replacements.get(modelIDSec);
			List<String> oldPrimVars = new ArrayList<>();
			List<String> oldSecVars = new ArrayList<>();
			boolean error = false;

			if (primAssign == null || secAssign == null
					|| !primAssign.containsKey(depVarName)) {
				index += dataTable.size();
				continue;
			}

			oldPrimVars.add(depVarName);
			formula = MathUtilities.replaceVariable(formula, depVarName,
					primAssign.get(depVarName));
			depVarName = primAssign.get(depVarName);
			((DepXml) depVar.get(0)).name = depVarName;

			for (PmmXmlElementConvertable el : indepVar.getElementSet()) {
				IndepXml iv = (IndepXml) el;

				if (!primAssign.containsKey(iv.name)) {
					error = true;
					break;
				}

				oldPrimVars.add(iv.name);
				formula = MathUtilities.replaceVariable(formula, iv.name,
						primAssign.get(iv.name));
				iv.name = primAssign.get(iv.name);
				newIndepVar.add(iv);
			}

			if (error) {
				index += dataTable.size();
				continue;
			}

			for (PmmXmlElementConvertable el : indepVarSec.getElementSet()) {
				IndepXml iv = (IndepXml) el;

				if (!secAssign.containsKey(iv.name)) {
					error = true;
					break;
				}

				oldSecVars.add(iv.name);
				formulaSec = MathUtilities.replaceVariable(formulaSec,
						iv.name, secAssign.get(iv.name));
				iv.name = secAssign.get(iv.name);
				newIndepVarSec.add(iv);
			}

			if (error) {
				index += dataTable.size();
				continue;
			}

			((CatalogModelXml) modelXml.get(0)).formula = formula;
			((CatalogModelXml) modelXmlSec.get(0)).formula = formulaSec;

			modelTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
			modelTuple.setValue(Model1Schema.ATT_DEPENDENT, depVar);
			modelTuple.setValue(Model1Schema.ATT_INDEPENDENT, newIndepVar);
			modelTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.NOTWRITABLE);
			modelTuple.setValue(Model2Schema.ATT_MODELCATALOG, modelXmlSec);
			modelTuple.setValue(Model2Schema.ATT_INDEPENDENT, newIndepVarSec);
			modelTuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
					Model1Schema.NOTWRITABLE);

			KnimeRelationReader dataReader = new KnimeRelationReader(
					SchemaFactory.createDataSchema(), dataTable);

			while (dataReader.hasMoreElements()) {
				KnimeTuple dataTuple = dataReader.nextElement();
				PmmXmlDoc timeSeries = dataTuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				PmmXmlDoc misc = dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				Map<String, String> paramsConvertTo = new LinkedHashMap<>();

				for (String var : oldPrimVars) {
					paramsConvertTo.put(primAssign.get(var),
							primaryVariableUnits.get(modelID).get(var));
				}

				for (String var : oldSecVars) {
					String unit = secondaryVariableUnits.get(modelIDSec).get(
							var);

					if (unit != null) {
						paramsConvertTo.put(secAssign.get(var), unit);
					}
				}

				String timeUnit = paramsConvertTo.get(AttributeUtilities.TIME);
				String concentrationUnit = paramsConvertTo
						.get(AttributeUtilities.CONCENTRATION);
				Category concentrationCategory = Categories
						.getCategoryByUnit(concentrationUnit);

				for (PmmXmlElementConvertable el : timeSeries.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					element.setTime(Categories.getTimeCategory().convert(
							element.getTime(), element.getTimeUnit(), timeUnit));
					element.setConcentration(concentrationCategory.convert(
							element.getConcentration(),
							element.getConcentrationUnit(), concentrationUnit));
					element.setTimeUnit(timeUnit);
					element.setConcentrationUnit(concentrationUnit);
				}

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (paramsConvertTo.containsKey(element.name)) {
						Category cat = Categories.getCategoryByUnit(element
								.unit);
						String unit = paramsConvertTo.get(element.name);

						element.value = cat.convert(element.value,
								element.unit, unit);
						element.unit = unit;
					}
				}

				dataTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeries);
				dataTuple.setValue(TimeSeriesSchema.ATT_MISC, misc);

				KnimeTuple tuple = new KnimeTuple(
						SchemaFactory.createM12DataSchema(), modelTuple,
						dataTuple);

				container.addRowToTable(tuple);
				exec.checkCanceled();
				exec.setProgress((double) index / (double) rowCount, "");
				index++;
			}
		}

		container.close();

		return container.getTable();
	}

	@Override
	public boolean isValid() {
		return true;
	}

	private void readDataTable() {
		primaryParameterCategories = new LinkedHashMap<>();
		secondaryParameterCategories = new LinkedHashMap<>();

		primaryParameterCategories.put(AttributeUtilities.TIME,
				Categories.getTime());
		primaryParameterCategories.put(AttributeUtilities.CONCENTRATION,
				Categories.NO_CATEGORY);

		KnimeRelationReader reader = new KnimeRelationReader(
				SchemaFactory.createDataSchema(), dataTable);

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				secondaryParameterCategories.put(element.name, Categories
						.getCategoryByUnit(element.unit).getName());
			}
		}
	}

	private void readModelTable() {
		boolean containsData = SchemaFactory.createM12DataSchema().conforms(
				modelTable);
		Map<KnimeTuple, List<KnimeTuple>> tuples;

		if (containsData) {
			tuples = new ModelCombiner(PmmUtilities.getTuples(modelTable,
					SchemaFactory.createM12DataSchema()), true, null, null)
					.getTupleCombinations();
		} else {
			tuples = new ModelCombiner(PmmUtilities.getTuples(modelTable,
					SchemaFactory.createM12Schema()), false, null, null)
					.getTupleCombinations();
		}

		Set<Integer> ids = new LinkedHashSet<>();
		Set<Integer> estIDs = new LinkedHashSet<>();

		modelTuples = new ArrayList<>();

		for (KnimeTuple tuple : tuples.keySet()) {
			int id = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).id;
			Integer estID = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).id;

			if (estID != null) {
				if (estIDs.add(estID)) {
					modelTuples.addAll(tuples.get(tuple));
				}
			} else {
				if (ids.add(id)) {
					modelTuples.addAll(tuples.get(tuple));
				}
			}
		}

		primaryModelNames = new LinkedHashMap<>();
		secondaryModelNames = new LinkedHashMap<>();
		primaryVariableCategories = new LinkedHashMap<>();
		primaryVariableUnits = new LinkedHashMap<>();
		secondaryVariableCategories = new LinkedHashMap<>();
		secondaryVariableUnits = new LinkedHashMap<>();

		for (KnimeTuple tuple : modelTuples) {
			CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			String depVarSec = ((DepXml) tuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0)).name;
			String modelID = modelXml.id + "";
			String modelIDSec = depVarSec + " (" + modelXml.id + ")";

			if (!primaryModelNames.containsKey(modelID)) {
				Map<String, String> categories = new LinkedHashMap<>();
				Map<String, String> units = new LinkedHashMap<>();
				DepXml depXml = (DepXml) tuple.getPmmXml(
						Model1Schema.ATT_DEPENDENT).get(0);

				categories.put(depXml.name, depXml.category);
				units.put(depXml.name, depXml.unit);

				for (PmmXmlElementConvertable el : tuple.getPmmXml(
						Model1Schema.ATT_INDEPENDENT).getElementSet()) {
					IndepXml element = (IndepXml) el;

					categories.put(element.name, element.category);
					units.put(element.name, element.unit);
				}

				primaryModelNames.put(modelID, modelXml.name);
				primaryVariableCategories.put(modelID, categories);
				primaryVariableUnits.put(modelID, units);
			}

			if (!secondaryModelNames.containsKey(modelIDSec)) {
				Map<String, String> categories = new LinkedHashMap<>();
				Map<String, String> units = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : tuple.getPmmXml(
						Model2Schema.ATT_INDEPENDENT).getElementSet()) {
					IndepXml element = (IndepXml) el;

					categories.put(element.name, element.category);
					units.put(element.name, element.unit);
				}

				if (containsData) {
					for (PmmXmlElementConvertable el : tuple.getPmmXml(
							TimeSeriesSchema.ATT_MISC).getElementSet()) {
						MiscXml element = (MiscXml) el;

						if (categories.containsKey(element.name)
								&& categories.get(element.name) == null) {
							categories.put(element.name, Categories
									.getCategoryByUnit(element.unit)
									.getName());
							units.put(element.name, element.unit);
						}
					}
				}

				secondaryModelNames.put(modelIDSec,
						depVarSec + " (" + modelXml.name + ")");
				secondaryVariableCategories.put(modelIDSec, categories);
				secondaryVariableUnits.put(modelIDSec, units);
			}
		}
	}

	private List<String> getPrimParamsFromCategory(String category) {
		List<String> params = new ArrayList<>();

		for (String param : primaryParameterCategories.keySet()) {
			String paramCat = primaryParameterCategories.get(param);

			if (category == null || paramCat.equals(category)) {
				params.add(param);
			} else if (paramCat.equals(Categories.NO_CATEGORY)) {
				if (Categories.getConcentrations().contains(category)) {
					params.add(param);
				}
			}
		}

		return params;
	}

	private List<String> getSecParamsFromCategory(String category) {
		List<String> params = new ArrayList<>();

		for (String param : secondaryParameterCategories.keySet()) {
			if (category == null
					|| secondaryParameterCategories.get(param).equals(category)) {
				params.add(param);
			}
		}

		return params;
	}

}
