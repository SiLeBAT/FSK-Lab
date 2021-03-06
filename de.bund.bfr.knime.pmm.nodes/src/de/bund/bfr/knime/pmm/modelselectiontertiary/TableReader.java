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
package de.bund.bfr.knime.pmm.modelselectiontertiary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.node.BufferedDataTable;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.QualityMeasurementComputation;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;

public class TableReader {

	private List<String> allIds;
	private List<KnimeTuple> allTuples;
	private Map<KnimeTuple, List<KnimeTuple>> tupleCombinations;

	private List<String> ids;

	private Map<String, List<String>> stringColumns;
	private Map<String, List<Double>> doubleColumns;
	private List<List<TimeSeriesXml>> data;
	private List<String> formulas;
	private List<Map<String, Double>> parameterData;
	private List<String> conditions;
	private List<List<Double>> conditionValues;
	private List<List<String>> conditionUnits;
	private List<String> standardVisibleColumns;
	private List<String> filterableStringColumns;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(BufferedDataTable table, boolean schemaContainsData) {
		List<String> miscParams = null;
		List<KnimeTuple> tertiaryTuples;
		List<KnimeTuple> tuples = new ArrayList<>();
		List<KnimeTuple> newTuples = null;
		List<List<KnimeTuple>> usedTuples = new ArrayList<>();
		Set<String> idSet = new LinkedHashSet<>();

		if (schemaContainsData) {
			tertiaryTuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM12DataSchema());
		} else {
			tertiaryTuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM12Schema());
		}

		Map<KnimeTuple, List<KnimeTuple>> combinations = new ModelCombiner(
				tertiaryTuples, schemaContainsData, null, null)
				.getTupleCombinations();

		for (Map.Entry<KnimeTuple, List<KnimeTuple>> entry : combinations
				.entrySet()) {
			tuples.add(entry.getKey());
			usedTuples.add(entry.getValue());
		}

		allIds = new ArrayList<>();
		allTuples = new ArrayList<>();
		tupleCombinations = new LinkedHashMap<>();
		ids = new ArrayList<>();
		plotables = new LinkedHashMap<>();
		shortLegend = new LinkedHashMap<>();
		longLegend = new LinkedHashMap<>();

		if (schemaContainsData) {
			try {
				tuples = QualityMeasurementComputation.computePrimary(tuples,
						false);
			} catch (Exception e) {
			}

			try {
				newTuples = QualityMeasurementComputation.computePrimary(
						tuples, true);
			} catch (Exception e) {
			}

			miscParams = PmmUtilities.getMiscParams(tuples);
			stringColumns = new LinkedHashMap<>();
			stringColumns.put(Model1Schema.NAME, new ArrayList<String>());
			stringColumns.put(Model1Schema.FORMULA, new ArrayList<String>());
			stringColumns.put(Model1Schema.ATT_EMLIT, new ArrayList<String>());
			stringColumns.put(ChartConstants.STATUS, new ArrayList<String>());
			stringColumns.put(AttributeUtilities.DATAID,
					new ArrayList<String>());
			stringColumns.put(TimeSeriesSchema.ATT_AGENT,
					new ArrayList<String>());
			stringColumns.put(AttributeUtilities.AGENT_DETAILS,
					new ArrayList<String>());
			stringColumns.put(TimeSeriesSchema.ATT_MATRIX,
					new ArrayList<String>());
			stringColumns.put(AttributeUtilities.MATRIX_DETAILS,
					new ArrayList<String>());
			stringColumns.put("Comment", new ArrayList<String>());
			doubleColumns = new LinkedHashMap<>();
			doubleColumns.put(Model1Schema.SSE, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.MSE, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.RMSE, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.RSQUARED, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.AIC, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.SSE + " (Local)",
					new ArrayList<Double>());
			doubleColumns.put(Model1Schema.MSE + " (Local)",
					new ArrayList<Double>());
			doubleColumns.put(Model1Schema.RMSE + " (Local)",
					new ArrayList<Double>());
			doubleColumns.put(Model1Schema.RSQUARED + " (Local)",
					new ArrayList<Double>());
			doubleColumns.put(Model1Schema.AIC + " (Local)",
					new ArrayList<Double>());
			standardVisibleColumns = new ArrayList<>(Arrays.asList(
					ChartSelectionPanel.DATA, ChartSelectionPanel.FORMULA,
					ChartSelectionPanel.PARAMETERS));
			standardVisibleColumns.addAll(stringColumns.keySet());
			standardVisibleColumns.addAll(doubleColumns.keySet());
			filterableStringColumns = Arrays.asList(Model1Schema.FORMULA,
					ChartConstants.STATUS, AttributeUtilities.DATAID);

			data = new ArrayList<>();
			formulas = new ArrayList<>();
			parameterData = new ArrayList<>();
			conditions = new ArrayList<>();
			conditionValues = new ArrayList<>();
			conditionUnits = new ArrayList<>();

			for (String param : miscParams) {
				conditions.add(param);
				conditionValues.add(new ArrayList<Double>());
				conditionUnits.add(new ArrayList<String>());
				standardVisibleColumns.add(param);
			}
		} else {
			stringColumns = new LinkedHashMap<>();
			stringColumns.put(Model1Schema.NAME, new ArrayList<String>());
			stringColumns.put(Model1Schema.FORMULA, new ArrayList<String>());
			stringColumns.put(Model1Schema.ATT_EMLIT, new ArrayList<String>());
			stringColumns.put(ChartConstants.STATUS, new ArrayList<String>());
			doubleColumns = new LinkedHashMap<>();
			doubleColumns.put(Model1Schema.SSE, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.MSE, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.RMSE, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.RSQUARED, new ArrayList<Double>());
			doubleColumns.put(Model1Schema.AIC, new ArrayList<Double>());
			standardVisibleColumns = new ArrayList<>(
					Arrays.asList(ChartSelectionPanel.FORMULA,
							ChartSelectionPanel.PARAMETERS));
			standardVisibleColumns.addAll(stringColumns.keySet());
			standardVisibleColumns.addAll(doubleColumns.keySet());
			filterableStringColumns = Arrays.asList(Model1Schema.FORMULA,
					ChartConstants.STATUS);

			data = null;
			formulas = new ArrayList<>();
			parameterData = new ArrayList<>();
			conditions = null;
			conditionValues = null;
			conditionUnits = null;
		}

		for (int nr = 0; nr < tuples.size(); nr++) {
			KnimeTuple tuple = tuples.get(nr);
			Integer catID = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).id;
			Integer estID = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).id;
			String id = "";

			if (estID != null) {
				id += estID;
			} else {
				id += catID;
			}

			if (schemaContainsData) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			allIds.add(id);
			allTuples.add(tuple);
			tupleCombinations.put(tuple, usedTuples.get(nr));

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);

			CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
			String modelName = modelXml.name;
			String formula = MathUtilities.getAllButBoundaryCondition(modelXml
					.formula);
			String depVar = depXml.name;
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			List<String> indepVars = CellIO.getNameList(indepXml);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			List<Double> paramValues = new ArrayList<>();
			Plotable plotable = null;
			Map<String, Double> parameters = new LinkedHashMap<>();
			Map<String, Double> paramData = new LinkedHashMap<>();
			Map<String, List<Double>> variables = new LinkedHashMap<>();
			Map<String, Double> varMin = new LinkedHashMap<>();
			Map<String, Double> varMax = new LinkedHashMap<>();
			String timeUnit = Categories.getTimeCategory().getStandardUnit();
			String concentrationUnit = depXml.unit;

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				if (element.name.equals(AttributeUtilities.TIME)) {
					variables.put(element.name,
							new ArrayList<>(Arrays.asList(0.0)));
					varMin.put(element.name, element.min);
					varMax.put(element.name, element.max);
					timeUnit = element.unit;
				}
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				parameters.put(element.name, element.value);
				paramValues.add(element.value);
				paramData.put(element.name, element.value);
				paramData.put(element.name + ": SE", element.error);
				paramData.put(element.name + ": t", element.t);
				paramData.put(element.name + ": Pr > |t|", element.P);
			}

			formulas.add(formula);
			parameterData.add(paramData);

			if (schemaContainsData) {
				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				Map<String, Double> miscValues = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					miscValues.put(element.name, element.value);
				}

				for (int i = 0; i < indepVars.size(); i++) {
					if (!indepVars.get(i).equals(AttributeUtilities.TIME)) {
						if (miscValues.containsKey(indepVars.get(i))) {
							parameters.put(indepVars.get(i),
									miscValues.get(indepVars.get(i)));
						}
					}
				}

				PmmXmlDoc timeSeriesXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				List<Double> timeList = new ArrayList<>();
				List<Double> logcList = new ArrayList<>();
				List<TimeSeriesXml> dataPoints = new ArrayList<>();

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					timeList.add(element.time);
					logcList.add(element.concentration);
					dataPoints.add(element);
				}

				plotable = new Plotable(Plotable.BOTH);
				plotable.addValueList(AttributeUtilities.TIME, timeList);
				plotable.addValueList(AttributeUtilities.CONCENTRATION,
						logcList);

				String dataName;
				AgentXml agent = (AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0);
				MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0);

				if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
				}

				PmmXmlDoc estModelXml = tuple
						.getPmmXml(Model1Schema.ATT_ESTMODEL);

				shortLegend.put(id, modelName + " (" + dataName + ")");
				longLegend
						.put(id, modelName + " (" + dataName + ") " + formula);
				stringColumns.get(AttributeUtilities.DATAID).add(dataName);
				stringColumns.get(TimeSeriesSchema.ATT_AGENT).add(
						agent.name);
				stringColumns.get(AttributeUtilities.AGENT_DETAILS).add(
						agent.detail);
				stringColumns.get(TimeSeriesSchema.ATT_MATRIX).add(
						matrix.name);
				stringColumns.get(AttributeUtilities.MATRIX_DETAILS).add(
						matrix.detail);
				stringColumns.get("Comment").add(
						((MdInfoXml) tuple.getPmmXml(
								TimeSeriesSchema.ATT_MDINFO).get(0))
								.comment);
				doubleColumns.get(Model1Schema.SSE).add(
						((EstModelXml) estModelXml.get(0)).sse);
				doubleColumns.get(Model1Schema.MSE).add(
						MathUtilities.getMSE(((EstModelXml) estModelXml.get(0))
								.rms));
				doubleColumns.get(Model1Schema.RMSE).add(
						((EstModelXml) estModelXml.get(0)).rms);
				doubleColumns.get(Model1Schema.RSQUARED).add(
						((EstModelXml) estModelXml.get(0)).r2);
				doubleColumns.get(Model1Schema.AIC).add(
						((EstModelXml) estModelXml.get(0)).aic);
				data.add(dataPoints);

				if (newTuples != null) {
					PmmXmlDoc newEstModelXml = newTuples.get(nr).getPmmXml(
							Model1Schema.ATT_ESTMODEL);

					doubleColumns.get(Model1Schema.SSE + " (Local)").add(
							((EstModelXml) newEstModelXml.get(0)).sse);
					doubleColumns.get(Model1Schema.MSE + " (Local)").add(
							MathUtilities.getMSE(((EstModelXml) newEstModelXml
									.get(0)).rms));
					doubleColumns.get(Model1Schema.RMSE + " (Local)").add(
							((EstModelXml) newEstModelXml.get(0)).rms);
					doubleColumns.get(Model1Schema.RSQUARED + " (Local)").add(
							((EstModelXml) newEstModelXml.get(0)).r2);
					doubleColumns.get(Model1Schema.AIC + " (Local)").add(
							((EstModelXml) newEstModelXml.get(0)).aic);
				} else {
					doubleColumns.get(Model1Schema.SSE + " (Local)").add(null);
					doubleColumns.get(Model1Schema.MSE + " (Local)").add(null);
					doubleColumns.get(Model1Schema.RMSE + " (Local)").add(null);
					doubleColumns.get(Model1Schema.RSQUARED + " (Local)").add(
							null);
					doubleColumns.get(Model1Schema.AIC + " (Local)").add(null);
				}

				for (int i = 0; i < miscParams.size(); i++) {
					Double value = null;
					String unit = null;

					for (PmmXmlElementConvertable el : misc.getElementSet()) {
						MiscXml element = (MiscXml) el;

						if (miscParams.get(i).equals(element.name)) {
							value = element.value;
							unit = element.unit;
							break;
						}
					}

					conditionValues.get(i).add(value);
					conditionUnits.get(i).add(unit);
				}
			} else {
				for (int i = 0; i < indepVars.size(); i++) {
					if (!indepVars.get(i).equals(AttributeUtilities.TIME)) {
						parameters.put(indepVars.get(i), 0.0);
					}
				}

				PmmXmlDoc estModelXml = tuple
						.getPmmXml(Model1Schema.ATT_ESTMODEL);

				plotable = new Plotable(Plotable.FUNCTION);
				shortLegend.put(id, modelName);
				longLegend.put(id, modelName + " " + formula);
				doubleColumns.get(Model1Schema.SSE).add(
						((EstModelXml) estModelXml.get(0)).sse);
				doubleColumns.get(Model1Schema.MSE).add(
						MathUtilities.getMSE(((EstModelXml) estModelXml.get(0))
								.rms));
				doubleColumns.get(Model1Schema.RMSE).add(
						((EstModelXml) estModelXml.get(0)).rms);
				doubleColumns.get(Model1Schema.RSQUARED).add(
						((EstModelXml) estModelXml.get(0)).r2);
				doubleColumns.get(Model1Schema.AIC).add(
						((EstModelXml) estModelXml.get(0)).aic);
			}

			Map<String, List<String>> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();

			categories.put(AttributeUtilities.TIME,
					Arrays.asList(Categories.getTime()));
			categories.put(AttributeUtilities.CONCENTRATION,
					Categories.getConcentrations());
			units.put(AttributeUtilities.TIME, timeUnit);
			units.put(AttributeUtilities.CONCENTRATION, concentrationUnit);

			plotable.setMinValue(depXml.min);
			plotable.setMaxValue(depXml.max);
			plotable.setFunction(modelXml.formula);
			plotable.setFunctionParameters(parameters);
			plotable.setFunctionArguments(variables);
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionValue(depVar);
			plotable.setCategories(categories);
			plotable.setUnits(units);

			String literature = "";

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_EMLIT).getElementSet()) {
				literature += "," + el;
			}

			if (!literature.isEmpty()) {
				literature = literature.substring(1);
			}

			stringColumns.get(Model1Schema.NAME).add(
					((EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL)
							.get(0)).name);
			stringColumns.get(Model1Schema.FORMULA).add(modelName);
			stringColumns.get(Model1Schema.ATT_EMLIT).add(literature);

			if (!plotable.isPlotable()) {
				stringColumns.get(ChartConstants.STATUS).add(
						ChartConstants.FAILED);
			} else if (PmmUtilities.isOutOfRange(paramXml)) {
				stringColumns.get(ChartConstants.STATUS).add(
						ChartConstants.OUT_OF_LIMITS);
			} else if (PmmUtilities.covarianceMatrixMissing(paramXml)) {
				stringColumns.get(ChartConstants.STATUS).add(
						ChartConstants.NO_COVARIANCE);
			} else {
				stringColumns.get(ChartConstants.STATUS).add(ChartConstants.OK);
			}

			plotables.put(id, plotable);
		}
	}

	public List<String> getAllIds() {
		return allIds;
	}

	public List<KnimeTuple> getAllTuples() {
		return allTuples;
	}

	public Map<KnimeTuple, List<KnimeTuple>> getTupleCombinations() {
		return tupleCombinations;
	}

	public List<String> getIds() {
		return ids;
	}

	public Map<String, List<String>> getStringColumns() {
		return stringColumns;
	}

	public Map<String, List<Double>> getDoubleColumns() {
		return doubleColumns;
	}

	public List<List<TimeSeriesXml>> getData() {
		return data;
	}

	public List<String> getFormulas() {
		return formulas;
	}

	public List<Map<String, Double>> getParameterData() {
		return parameterData;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public List<List<Double>> getConditionValues() {
		return conditionValues;
	}

	public List<List<String>> getConditionUnits() {
		return conditionUnits;
	}

	public List<String> getStandardVisibleColumns() {
		return standardVisibleColumns;
	}

	public List<String> getFilterableStringColumns() {
		return filterableStringColumns;
	}

	public Map<String, Plotable> getPlotables() {
		return plotables;
	}

	public Map<String, String> getShortLegend() {
		return shortLegend;
	}

	public Map<String, String> getLongLegend() {
		return longLegend;
	}

}
