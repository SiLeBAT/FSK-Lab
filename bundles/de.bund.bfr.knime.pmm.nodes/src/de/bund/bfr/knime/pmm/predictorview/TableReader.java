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
package de.bund.bfr.knime.pmm.predictorview;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
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
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;

public class TableReader {

	private static final String IDENTIFIER = "Identifier";

	private List<String> ids;
	private Map<String, KnimeTuple> tupleMap;
	private Map<String, List<String>> stringColumns;
	private Map<String, List<Double>> doubleColumns;
	private List<String> formulas;
	private List<Map<String, Double>> parameterData;
	private List<Map<String, String>> variableData;
	private List<String> conditions;
	private List<List<Double>> conditionValues;
	private List<List<Double>> conditionMinValues;
	private List<List<Double>> conditionMaxValues;
	private List<List<String>> conditionUnits;
	private List<String> standardVisibleColumns;
	private List<String> filterableStringColumns;
	private Map<String, String> newInitParams;
	private Map<String, String> newLagParams;
	private Map<KnimeTuple, List<KnimeTuple>> combinedTuples;
	private Map<String, String> units;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;
	private Map<String, String> shortIds;

	private Map<String, String> tempParam;
	private Map<String, String> phParam;
	private Map<String, String> awParam;

	public TableReader(List<KnimeTuple> tuples, Map<String, String> initParams,
			Map<String, String> lagParams, boolean defaultBehaviour) {
		Set<String> idSet = new LinkedHashSet<>();
		boolean isTertiaryModel = tuples.get(0).getSchema()
				.conforms(SchemaFactory.createM12Schema());
		boolean containsData = tuples.get(0).getSchema()
				.conforms(SchemaFactory.createDataSchema());
		List<String> miscParams = null;
		Map<KnimeTuple, List<KnimeTuple>> combined = new LinkedHashMap<>();
		List<KnimeTuple> rawTuples = tuples;

		newInitParams = new LinkedHashMap<>();
		newLagParams = new LinkedHashMap<>();

		if (isTertiaryModel) {
			combined = new ModelCombiner(tuples, containsData, initParams,
					lagParams).getTupleCombinations();

			tuples = new ArrayList<>(combined.keySet());

			try {
				List<KnimeTuple> newTuples = QualityMeasurementComputation
						.computePrimary(tuples, false);

				for (int i = 0; i < tuples.size(); i++) {
					combined.put(newTuples.get(i), combined.get(tuples.get(i)));
					combined.remove(tuples.get(i));
				}

				tuples = newTuples;
			} catch (Exception e) {
			}

			if (!defaultBehaviour) {
				combinedTuples = new LinkedHashMap<>();

				for (KnimeTuple t1 : combined.keySet()) {
					combinedTuples.put(t1, new ArrayList<KnimeTuple>());

					for (KnimeTuple t2 : combined.get(t1)) {
						combinedTuples.get(t1).addAll(
								getAllDataTuples(t2, rawTuples));
					}
				}

				for (KnimeTuple tuple : tuples) {
					List<KnimeTuple> usedTuples = combinedTuples.get(tuple);

					if (!usedTuples.isEmpty()) {
						String oldID = ((CatalogModelXml) usedTuples.get(0)
								.getPmmXml(Model1Schema.ATT_MODELCATALOG)
								.get(0)).id
								+ "";
						String newID = ((CatalogModelXml) tuple.getPmmXml(
								Model1Schema.ATT_MODELCATALOG).get(0)).id
								+ "";

						if (initParams.containsKey(oldID)) {
							newInitParams.put(newID, initParams.get(oldID));
						}

						if (lagParams.containsKey(oldID)) {
							newLagParams.put(newID, lagParams.get(oldID));
						}
					}
				}
			}
		} else {
			newInitParams.putAll(initParams);
			newLagParams.putAll(lagParams);

			if (!tuples.isEmpty()) {
				if (tuples.get(0).getPmmXml(Model1Schema.ATT_INDEPENDENT)
						.size() > 1) {
					containsData = false;
				}
			}
		}

		ids = new ArrayList<>();
		tupleMap = new LinkedHashMap<>();
		plotables = new LinkedHashMap<>();
		shortLegend = new LinkedHashMap<>();
		longLegend = new LinkedHashMap<>();
		shortIds = new LinkedHashMap<>();
		tempParam = new LinkedHashMap<>();
		phParam = new LinkedHashMap<>();
		awParam = new LinkedHashMap<>();
		formulas = new ArrayList<>();
		parameterData = new ArrayList<>();
		variableData = new ArrayList<>();
		doubleColumns = new LinkedHashMap<>();
		doubleColumns.put(Model1Schema.SSE, new ArrayList<Double>());
		doubleColumns.put(Model1Schema.MSE, new ArrayList<Double>());
		doubleColumns.put(Model1Schema.RMSE, new ArrayList<Double>());
		doubleColumns.put(Model1Schema.RSQUARED, new ArrayList<Double>());
		doubleColumns.put(Model1Schema.AIC, new ArrayList<Double>());
		conditions = null;
		conditionValues = null;
		conditionMinValues = null;
		conditionMaxValues = null;
		conditionUnits = null;

		if (isTertiaryModel) {
			stringColumns = new LinkedHashMap<>();
			stringColumns.put(IDENTIFIER, new ArrayList<String>());
			stringColumns.put(Model1Schema.NAME, new ArrayList<String>());
			stringColumns.put(ChartConstants.STATUS, new ArrayList<String>());
			stringColumns.put(Model1Schema.FORMULA, new ArrayList<String>());
			stringColumns.put(Model1Schema.ATT_EMLIT, new ArrayList<String>());
			stringColumns.put(Model2Schema.FORMULA, new ArrayList<String>());
			stringColumns.put(TimeSeriesSchema.ATT_AGENT,
					new ArrayList<String>());
			stringColumns.put(TimeSeriesSchema.ATT_MATRIX,
					new ArrayList<String>());
			stringColumns.put(AttributeUtilities.AGENT_DETAILS,
					new ArrayList<String>());
			stringColumns.put(AttributeUtilities.MATRIX_DETAILS,
					new ArrayList<String>());
			stringColumns.put(MdInfoXml.ATT_COMMENT, new ArrayList<String>());
			standardVisibleColumns = new ArrayList<>(
					Arrays.asList(ChartSelectionPanel.FORMULA,
							ChartSelectionPanel.PARAMETERS));
			standardVisibleColumns.addAll(stringColumns.keySet());
			standardVisibleColumns.addAll(doubleColumns.keySet());
			filterableStringColumns = Arrays.asList(ChartConstants.STATUS);

			miscParams = PmmUtilities.getIndeps(tuples);
			miscParams.remove(AttributeUtilities.TIME);
			conditions = new ArrayList<>();
			conditionMinValues = new ArrayList<>();
			conditionMaxValues = new ArrayList<>();
			conditionUnits = new ArrayList<>();

			for (String param : miscParams) {
				conditions.add(param);
				conditionMinValues.add(new ArrayList<Double>());
				conditionMaxValues.add(new ArrayList<Double>());
				conditionUnits.add(new ArrayList<String>());
				standardVisibleColumns.add(param);
			}
		} else {
			if (containsData) {
				stringColumns = new LinkedHashMap<>();
				stringColumns.put(IDENTIFIER, new ArrayList<String>());
				stringColumns.put(ChartConstants.STATUS,
						new ArrayList<String>());
				stringColumns
						.put(Model1Schema.FORMULA, new ArrayList<String>());
				stringColumns.put(Model1Schema.ATT_EMLIT,
						new ArrayList<String>());
				stringColumns.put(Model1Schema.NAME, new ArrayList<String>());
				stringColumns.put(AttributeUtilities.DATAID,
						new ArrayList<String>());
				stringColumns.put(TimeSeriesSchema.ATT_AGENT,
						new ArrayList<String>());
				stringColumns.put(TimeSeriesSchema.ATT_MATRIX,
						new ArrayList<String>());
				stringColumns.put(AttributeUtilities.AGENT_DETAILS,
						new ArrayList<String>());
				stringColumns.put(AttributeUtilities.MATRIX_DETAILS,
						new ArrayList<String>());
				stringColumns.put(MdInfoXml.ATT_COMMENT,
						new ArrayList<String>());
				standardVisibleColumns = new ArrayList<>(Arrays.asList(
						ChartSelectionPanel.FORMULA,
						ChartSelectionPanel.PARAMETERS));
				standardVisibleColumns.addAll(stringColumns.keySet());
				standardVisibleColumns.addAll(doubleColumns.keySet());
				filterableStringColumns = Arrays.asList(ChartConstants.STATUS);

				miscParams = PmmUtilities.getMiscParams(tuples);
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
				stringColumns.put(IDENTIFIER, new ArrayList<String>());
				stringColumns.put(ChartConstants.STATUS,
						new ArrayList<String>());
				stringColumns
						.put(Model1Schema.FORMULA, new ArrayList<String>());
				stringColumns.put(Model1Schema.ATT_EMLIT,
						new ArrayList<String>());
				stringColumns.put(Model1Schema.NAME, new ArrayList<String>());
				standardVisibleColumns = new ArrayList<>(Arrays.asList(
						ChartSelectionPanel.FORMULA,
						ChartSelectionPanel.PARAMETERS));
				standardVisibleColumns.addAll(stringColumns.keySet());
				standardVisibleColumns.addAll(doubleColumns.keySet());
				filterableStringColumns = Arrays.asList(Model1Schema.FORMULA,
						ChartConstants.STATUS);
			}
		}

		Map<String, List<KnimeTuple>> dataTuples = new LinkedHashMap<>();

		if (isTertiaryModel && containsData) {
			for (KnimeTuple tuple : tuples) {
				String id = ((EstModelXml) tuple.getPmmXml(
						Model1Schema.ATT_ESTMODEL).get(0)).id
						+ "";

				if (!dataTuples.containsKey(id)) {
					dataTuples.put(id, new ArrayList<KnimeTuple>());
				}

				dataTuples.get(id).add(tuple);
			}
		}

		int index = 1;

		for (KnimeTuple tuple : tuples) {
			String id = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).id
					+ "";

			if (!isTertiaryModel && containsData) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (!idSet.add(id)) {
				continue;
			}

			String primId;

			if (isTertiaryModel) {
				primId = ((CatalogModelXml) combined.get(tuple).get(0)
						.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0))
						.id + "";
			} else {
				primId = ((CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).id
						+ "";
			}

			ids.add(id);
			tupleMap.put(id, tuple);

			CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
			String modelName = modelXml.name;
			String dbuuid = modelXml.dbuuid;
			String formula = MathUtilities.getAllButBoundaryCondition(modelXml
					.formula);
			String depVar = depXml.name;
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			Map<String, List<Double>> variables = new LinkedHashMap<>();
			Map<String, Double> varMin = new LinkedHashMap<>();
			Map<String, Double> varMax = new LinkedHashMap<>();
			Map<String, Double> parameters = new LinkedHashMap<>();
			Map<String, Double> paramData = new LinkedHashMap<>();
			Map<String, Map<String, Double>> covariances = new LinkedHashMap<>();
			String initParam = initParams.get(primId);
			String lagParam = lagParams.get(primId);
			Map<String, List<String>> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();
			Plotable plotable = new Plotable(Plotable.FUNCTION_SAMPLE);

			categories.put(depXml.name,
					Arrays.asList(depXml.category));
			units.put(depXml.name, depXml.unit);

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				variables.put(element.name, new ArrayList<Double>());
				varMin.put(element.name, element.min);
				varMax.put(element.name, element.max);

				categories.put(element.name,
						Arrays.asList(element.category));
				units.put(element.name, element.unit);

				if (Categories.getTempCategory().equals(
						Categories.getCategoryByUnit(element.unit))) {
					tempParam.put(id, element.name);
				}

				if (Categories.getPhUnit().equals(element.unit)) {
					phParam.put(id, element.name);
				}

				if (Categories.getAwUnit().equals(element.unit)) {
					awParam.put(id, element.name);
				}
			}

			Double minConcentration = null;
			Double maxConcentration = null;

			if (isTertiaryModel && containsData) {
				Point2D range = getConcentrationRange(dataTuples.get(id));

				if (range != null) {
					minConcentration = range.getX();
					maxConcentration = range.getY();
				}
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				if (element.getName().equals(initParam)
						|| element.getName().equals(lagParam)) {
					variables.put(element.getName(), new ArrayList<Double>());
					units.put(element.getName(), element.getUnit());
					categories.put(element.getName(),
							Arrays.asList(element.getCategory()));

					if (element.getName().equals(initParam)
							&& minConcentration != null
							&& maxConcentration != null) {
						varMin.put(element.getName(), minConcentration);
						varMax.put(element.getName(), maxConcentration);
					} else {
						varMin.put(element.getName(), element.getMin());
						varMax.put(element.getName(), element.getMax());
					}

					if (element.getValue() != null) {
						plotable.addValueList(
								element.getName(),
								new ArrayList<>(Arrays.asList(element
										.getValue())));
					} else {
						plotable.addValueList(element.getName(),
								new ArrayList<Double>());
					}
				} else {
					parameters.put(element.getName(), element.getValue());
					paramData.put(element.getName() + (element.getUnit()!=null?" ("+element.getUnit()+")":"") , element.getValue());
					paramData.put(element.getName() + ": SE",
							element.getError());
					paramData.put(element.getName() + ": t", element.getT());
					paramData.put(element.getName() + ": Pr > |t|",
							element.getP());
				}

				if (initParam == null && lagParam == null) {
					Map<String, Double> cov = new LinkedHashMap<>();

					for (PmmXmlElementConvertable el2 : paramXml
							.getElementSet()) {
						cov.put(((ParamXml) el2).getName(), element
								.getCorrelation(((ParamXml) el2).getOrigName()));
					}

					covariances.put(element.getName(), cov);
				}
			}

			formulas.add(formula);
			parameterData.add(paramData);

			PmmXmlDoc estModelXml = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
			String literature = "";

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_EMLIT).getElementSet()) {
				literature += "," + el;
			}

			if (!literature.isEmpty()) {
				literature = literature.substring(1);
			}

			shortLegend.put(id, index + "");
			longLegend.put(id, index + "");
			shortIds.put(id, index + "");
			stringColumns.get(IDENTIFIER).add(index + "");
			stringColumns.get(Model1Schema.FORMULA).add(modelName);			
			stringColumns.get(Model1Schema.ATT_EMLIT).add(literature);
			stringColumns.get(Model1Schema.NAME).add(
					((EstModelXml) estModelXml.get(0)).name);
			index++;

			if (isTertiaryModel) {
				Set<String> secModels = new LinkedHashSet<>();

				for (KnimeTuple t : combined.get(tuple)) {
					secModels.add(((CatalogModelXml) t.getPmmXml(
							Model2Schema.ATT_MODELCATALOG).get(0)).name);
				}

				String secString = "";

				for (String s : secModels) {
					secString += "," + s;
				}

				stringColumns.get(Model2Schema.FORMULA).add(
						secString.substring(1));
			}

			if (isTertiaryModel || containsData) {
				AgentXml agent = (AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0);
				MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0);

				stringColumns.get(TimeSeriesSchema.ATT_AGENT).add(
						agent.name);
				stringColumns.get(TimeSeriesSchema.ATT_MATRIX).add(
						matrix.name);
				stringColumns.get(AttributeUtilities.AGENT_DETAILS).add(
						agent.detail);
				stringColumns.get(AttributeUtilities.MATRIX_DETAILS).add(
						matrix.detail);
				stringColumns.get(MdInfoXml.ATT_COMMENT).add(
						((MdInfoXml) tuple.getPmmXml(
								TimeSeriesSchema.ATT_MDINFO).get(0))
								.getComment());
			}

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

			plotable.setFunction(modelXml.formula);
			plotable.setFunctionValue(depVar);
			plotable.setFunctionArguments(variables);
			plotable.setMinValue(depXml.min);
			plotable.setMaxValue(depXml.max);
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionParameters(parameters);
			plotable.setCovariances(covariances);
			plotable.setDegreesOfFreedom(((EstModelXml) estModelXml.get(0))
					.dof);
			plotable.setCategories(categories);
			plotable.setUnits(units);

			if (isTertiaryModel) {
				if (containsData) {
					for (int i = 0; i < miscParams.size(); i++) {
						String unit = null;

						for (PmmXmlElementConvertable el : tuple.getPmmXml(
								TimeSeriesSchema.ATT_MISC).getElementSet()) {
							MiscXml element = (MiscXml) el;

							if (miscParams.get(i).equals(element.getName())) {
								unit = element.getUnit();
								break;
							}
						}

						conditionUnits.get(i).add(unit);

						if (unit != null) {
							units.put(miscParams.get(i), unit);
							categories.put(
									miscParams.get(i),
									Arrays.asList(Categories.getCategoryByUnit(
											unit).getName()));
						}
					}
				}

				for (int i = 0; i < miscParams.size(); i++) {
					Double min = null;
					Double max = null;
					String unit = null;

					for (PmmXmlElementConvertable el : tuple.getPmmXml(
							Model1Schema.ATT_INDEPENDENT).getElementSet()) {
						IndepXml element = (IndepXml) el;

						if (miscParams.get(i).equals(element.name)) {
							min = element.min;
							max = element.max;
							unit = element.unit;
							break;
						}
					}

					conditionMinValues.get(i).add(min);
					conditionMaxValues.get(i).add(max);

					if (!containsData) {
						conditionUnits.get(i).add(unit);
					} else {
						List<String> cu = conditionUnits.get(i);

						if (!cu.isEmpty() && cu.get(cu.size() - 1) == null) {
							cu.set(cu.size() - 1, unit);
						}
					}
				}
			} else if (containsData) {
				String dataName;

				if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
				}

				stringColumns.get(AttributeUtilities.DATAID).add(dataName);

				for (int i = 0; i < miscParams.size(); i++) {
					Double value = null;
					String unit = null;

					for (PmmXmlElementConvertable el : tuple.getPmmXml(
							TimeSeriesSchema.ATT_MISC).getElementSet()) {
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
			}

			Map<String, String> varData = new LinkedHashMap<>();

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				if (element.min != null) {
					varData.put(element.name + " Min", element.min
							+ " " + units.get(element.name));
				}

				if (element.max != null) {
					varData.put(element.name + " Max", element.max
							+ " " + units.get(element.name));
				}
			}

			variableData.add(varData);

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

		units = new LinkedHashMap<>();

		for (Plotable plotable : plotables.values()) {
			units.putAll(plotable.getUnits());
		}
	}

	public List<String> getIds() {
		return ids;
	}

	public Map<String, KnimeTuple> getTupleMap() {
		return tupleMap;
	}

	public Map<String, Plotable> getPlotables() {
		return plotables;
	}

	public Map<String, String> getShortIds() {
		return shortIds;
	}

	public Map<String, List<String>> getStringColumns() {
		return stringColumns;
	}

	public Map<String, List<Double>> getDoubleColumns() {
		return doubleColumns;
	}

	public List<String> getFormulas() {
		return formulas;
	}

	public List<Map<String, Double>> getParameterData() {
		return parameterData;
	}

	public List<Map<String, String>> getVariableData() {
		return variableData;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public List<List<Double>> getConditionValues() {
		return conditionValues;
	}

	public List<List<Double>> getConditionMinValues() {
		return conditionMinValues;
	}

	public List<List<Double>> getConditionMaxValues() {
		return conditionMaxValues;
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

	public Map<String, String> getShortLegend() {
		return shortLegend;
	}

	public Map<String, String> getLongLegend() {
		return longLegend;
	}

	public Map<String, String> getNewInitParams() {
		return newInitParams;
	}

	public Map<String, String> getNewLagParams() {
		return newLagParams;
	}

	public Map<KnimeTuple, List<KnimeTuple>> getCombinedTuples() {
		return combinedTuples;
	}

	public Map<String, String> getUnits() {
		return units;
	}

	public Map<String, String> getTempParam() {
		return tempParam;
	}

	public Map<String, String> getPhParam() {
		return phParam;
	}

	public Map<String, String> getAwParam() {
		return awParam;
	}

	private List<KnimeTuple> getAllDataTuples(KnimeTuple current,
			List<KnimeTuple> all) {
		List<KnimeTuple> tuples = new ArrayList<>();
		Integer primId = ((CatalogModelXml) current.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0)).id;
		Integer secEstId = ((EstModelXml) current.getPmmXml(
				Model2Schema.ATT_ESTMODEL).get(0)).id;

		for (KnimeTuple tuple : all) {
			Integer pId = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).id;
			Integer sId = ((EstModelXml) tuple.getPmmXml(
					Model2Schema.ATT_ESTMODEL).get(0)).id;

			if (primId.equals(pId) && secEstId.equals(sId)) {
				tuples.add(tuple);
			}
		}

		return tuples;
	}

	private static Point2D getConcentrationRange(List<KnimeTuple> tuples) {
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;

		for (KnimeTuple tuple : tuples) {
			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					TimeSeriesSchema.ATT_TIMESERIES).getElementSet()) {
				Double value = ((TimeSeriesXml) el).getConcentration();

				if (value != null) {
					min = Math.min(value, min);
					max = Math.max(value, max);
				}
			}
		}

		if (MathUtilities.isValid(min) && MathUtilities.isValid(max)) {
			return new Point2D.Double(min, max);
		}

		return null;
	}

}
