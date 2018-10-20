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
package de.bund.bfr.knime.pmm.primarymodelviewandselect;

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
		Set<String> idSet = new LinkedHashSet<>();
		List<KnimeTuple> tuples;
		List<KnimeTuple> newTuples = null;

		if (schemaContainsData) {
			tuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM1DataSchema());
		} else {
			tuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM1Schema());
		}

		allIds = new ArrayList<>();
		allTuples = new ArrayList<>();
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
			stringColumns.put(MdInfoXml.ATT_COMMENT, new ArrayList<String>());
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
					AttributeUtilities.DATAID, ChartConstants.STATUS);

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
			standardVisibleColumns = new ArrayList<>(Arrays.asList(
					ChartSelectionPanel.DATA, ChartSelectionPanel.FORMULA,
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
					Model1Schema.ATT_ESTMODEL).get(0)).getId();
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

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);

			CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			PmmXmlDoc estModelXml = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
			String modelName = modelXml.name;
			String formula = MathUtilities.getAllButBoundaryCondition(modelXml
					.formula);
			String depVar = depXml.getName();
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			List<String> indepVars = CellIO.getNameList(indepXml);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			Plotable plotable = null;
			Map<String, Double> parameters = new LinkedHashMap<>();
			Map<String, Double> paramData = new LinkedHashMap<>();
			Map<String, List<Double>> variables = new LinkedHashMap<>();
			Map<String, Double> varMin = new LinkedHashMap<>();
			Map<String, Double> varMax = new LinkedHashMap<>();
			Map<String, Map<String, Double>> covariances = new LinkedHashMap<>();
			String timeUnit = Categories.getTimeCategory().getStandardUnit();
			String concentrationUnit = depXml.getUnit();

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				if (element.getName().equals(AttributeUtilities.TIME)) {
					variables.put(element.getName(),
							new ArrayList<>(Arrays.asList(0.0)));
					varMin.put(element.getName(), element.getMin());
					varMax.put(element.getName(), element.getMax());
					timeUnit = element.getUnit();
				}
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;
				Map<String, Double> cov = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el2 : paramXml.getElementSet()) {
					cov.put(((ParamXml) el2).getName(), element
							.getCorrelation(((ParamXml) el2).getOrigName()));
				}

				parameters.put(element.getName(), element.getValue());
				covariances.put(element.getName(), cov);
				paramData.put(element.getName(), element.getValue());
				paramData.put(element.getName() + ": SE", element.getError());
				paramData.put(element.getName() + ": t", element.getT());
				paramData.put(element.getName() + ": Pr > |t|", element.getP());
			}

			formulas.add(formula);
			parameterData.add(paramData);

			if (schemaContainsData) {
				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				Map<String, Double> miscValues = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					miscValues.put(element.getName(), element.getValue());
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

					timeList.add(element.getTime());
					logcList.add(element.getConcentration());
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

				shortLegend.put(id, modelName + " (" + dataName + ")");
				longLegend
						.put(id, modelName + " (" + dataName + ") " + formula);
				stringColumns.get(AttributeUtilities.DATAID).add(dataName);
				stringColumns.get(TimeSeriesSchema.ATT_AGENT).add(
						agent.name);
				stringColumns.get(AttributeUtilities.AGENT_DETAILS).add(
						agent.detail);
				stringColumns.get(TimeSeriesSchema.ATT_MATRIX).add(
						matrix.getName());
				stringColumns.get(AttributeUtilities.MATRIX_DETAILS).add(
						matrix.getDetail());
				stringColumns.get(MdInfoXml.ATT_COMMENT).add(
						((EstModelXml) estModelXml.get(0)).getComment());
				doubleColumns.get(Model1Schema.SSE).add(
						((EstModelXml) estModelXml.get(0)).getSse());
				doubleColumns.get(Model1Schema.MSE).add(
						MathUtilities.getMSE(((EstModelXml) estModelXml.get(0))
								.getRms()));
				doubleColumns.get(Model1Schema.RMSE).add(
						((EstModelXml) estModelXml.get(0)).getRms());
				doubleColumns.get(Model1Schema.RSQUARED).add(
						((EstModelXml) estModelXml.get(0)).getR2());
				doubleColumns.get(Model1Schema.AIC).add(
						((EstModelXml) estModelXml.get(0)).getAic());
				data.add(dataPoints);

				if (newTuples != null) {
					PmmXmlDoc newEstModelXml = newTuples.get(nr).getPmmXml(
							Model1Schema.ATT_ESTMODEL);

					doubleColumns.get(Model1Schema.SSE + " (Local)").add(
							((EstModelXml) newEstModelXml.get(0)).getSse());
					doubleColumns.get(Model1Schema.MSE + " (Local)").add(
							MathUtilities.getMSE(((EstModelXml) newEstModelXml
									.get(0)).getRms()));
					doubleColumns.get(Model1Schema.RMSE + " (Local)").add(
							((EstModelXml) newEstModelXml.get(0)).getRms());
					doubleColumns.get(Model1Schema.RSQUARED + " (Local)").add(
							((EstModelXml) newEstModelXml.get(0)).getR2());
					doubleColumns.get(Model1Schema.AIC + " (Local)").add(
							((EstModelXml) newEstModelXml.get(0)).getAic());
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

						if (miscParams.get(i).equals(element.getName())) {
							value = element.getValue();
							unit = element.getUnit();
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

				plotable = new Plotable(Plotable.FUNCTION);
				shortLegend.put(id, modelName);
				longLegend.put(id, modelName + " " + formula);
				doubleColumns.get(Model1Schema.SSE).add(
						((EstModelXml) estModelXml.get(0)).getSse());
				doubleColumns.get(Model1Schema.MSE).add(
						MathUtilities.getMSE(((EstModelXml) estModelXml.get(0))
								.getRms()));
				doubleColumns.get(Model1Schema.RMSE).add(
						((EstModelXml) estModelXml.get(0)).getRms());
				doubleColumns.get(Model1Schema.RSQUARED).add(
						((EstModelXml) estModelXml.get(0)).getR2());
				doubleColumns.get(Model1Schema.AIC).add(
						((EstModelXml) estModelXml.get(0)).getAic());
			}

			Map<String, List<String>> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();

			categories.put(AttributeUtilities.TIME,
					Arrays.asList(Categories.getTime()));
			categories.put(AttributeUtilities.CONCENTRATION,
					Categories.getConcentrations());
			units.put(AttributeUtilities.TIME, timeUnit);
			units.put(AttributeUtilities.CONCENTRATION, concentrationUnit);

			plotable.setFunction(modelXml.formula);
			plotable.setFunctionParameters(parameters);
			plotable.setFunctionArguments(variables);
			plotable.setMinValue(depXml.getMin());
			plotable.setMaxValue(depXml.getMax());
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionValue(depVar);
			plotable.setCovariances(covariances);
			plotable.setDegreesOfFreedom(((EstModelXml) estModelXml.get(0))
					.getDof());
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
					((EstModelXml) estModelXml.get(0)).getName());
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
