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
package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

public class ModelCombiner {

	private Map<KnimeTuple, List<KnimeTuple>> tupleCombinations;
	private Map<KnimeTuple, Map<KnimeTuple, Map<String, String>>> parameterRenaming;

	public ModelCombiner(List<KnimeTuple> tuples, boolean containsData, Map<String, String> initParams,
			Map<String, String> lagParams) {
		if (initParams == null) {
			initParams = new LinkedHashMap<>();
		}

		if (lagParams == null) {
			lagParams = new LinkedHashMap<>();
		}

		tupleCombinations = getTuplesToCombine(tuples, containsData);
		parameterRenaming = new LinkedHashMap<>();

		for (KnimeTuple newTuple : tupleCombinations.keySet()) {
			List<KnimeTuple> usedTuples = tupleCombinations.get(newTuple);
			Map<KnimeTuple, Map<String, String>> rename = new LinkedHashMap<>();

			for (KnimeTuple tuple : usedTuples) {
				rename.put(tuple, new LinkedHashMap<String, String>());

				String modelID = ((CatalogModelXml) tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0)).id + "";
				String depVarSec = ((DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0)).name;

				if (depVarSec.equals(initParams.get(modelID)) || depVarSec.equals(lagParams.get(modelID))) {
					continue;
				}

				String formulaSec = ((CatalogModelXml) tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG).get(0))
						.formula;
				PmmXmlDoc indepVarsSec = tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
				PmmXmlDoc paramsSec = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);

				for (PmmXmlElementConvertable el : paramsSec.getElementSet()) {
					ParamXml element = (ParamXml) el;
					int index = 1;
					String paramName = element.getName();
					String newParamName = paramName;

					while (CellIO.getNameList(newTuple.getPmmXml(Model1Schema.ATT_PARAMETER)).contains(newParamName)) {
						index++;
						newParamName = paramName + index;
					}

					rename.get(tuple).put(paramName, newParamName);

					if (index > 1) {
						formulaSec = MathUtilities.replaceVariable(formulaSec, paramName, newParamName);
						element.setName(newParamName);
					}

					element.getAllCorrelations().clear();
				}

				String replacement = "(" + formulaSec.replace(depVarSec + "=", "") + ")";
				String formula = ((CatalogModelXml) newTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0))
						.formula;
				PmmXmlDoc newParams = newTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
				PmmXmlDoc newIndepVars = newTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);

				newParams.getElementSet().remove(CellIO.getNameList(newParams).indexOf(depVarSec));
				newParams.getElementSet().addAll(paramsSec.getElementSet());

				for (PmmXmlElementConvertable el : indepVarsSec.getElementSet()) {
					IndepXml element = (IndepXml) el;

					if (!CellIO.getNameList(newIndepVars).contains(element.getName())) {
						newIndepVars.getElementSet().add(element);
					} else {
						IndepXml original = null;

						for (PmmXmlElementConvertable el2 : newIndepVars.getElementSet()) {
							if (((IndepXml) el2).getName().equals(element.getName())) {
								original = (IndepXml) el2;
								break;
							}
						}

						Double min = element.getMin();
						Double max = element.getMax();

						if (original.getUnit() != null && !original.getUnit().equals(element.getUnit())) {
							Category cat = Categories.getCategoryByUnit(original.getUnit());

							try {
								String conversion = "(" + cat.getConversionString(element.getName(), original.getUnit(),
										element.getUnit()) + ")";

								replacement = MathUtilities.replaceVariable(replacement, element.getName(), conversion);
								min = cat.convert(min, element.getUnit(), original.getUnit());
								max = cat.convert(max, element.getUnit(), original.getUnit());
							} catch (ConvertException e) {
								e.printStackTrace();
							}
						}

						if (min != null) {
							if (original.getMin() != null) {
								original.setMin(Math.min(original.getMin(), min));
							} else {
								original.setMin(min);
							}
						}

						if (max != null) {
							if (original.getMax() != null) {
								original.setMax(Math.max(original.getMax(), max));
							} else {
								original.setMax(max);
							}
						}
					}
				}

				PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);

				((CatalogModelXml) modelXml.get(0))
						.formula = MathUtilities.replaceVariable(formula, depVarSec, replacement);
				

				newTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
				newTuple.setValue(Model1Schema.ATT_INDEPENDENT, newIndepVars);
				newTuple.setValue(Model1Schema.ATT_PARAMETER, newParams);
			}

			int newID = ((CatalogModelXml) newTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0)).id;

			for (KnimeTuple tuple : usedTuples) {
				newID += ((CatalogModelXml) tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG).get(0)).id;
			}

			newID = MathUtilities.generateID(newID);

			PmmXmlDoc modelXml = newTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			PmmXmlDoc estModelXml = newTuple.getPmmXml(Model1Schema.ATT_ESTMODEL);

			((CatalogModelXml) modelXml.get(0)).id = newID;
			((EstModelXml) estModelXml.get(0)).id = usedTuples.get(0).getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			((EstModelXml) estModelXml.get(0)).sse = null;
			((EstModelXml) estModelXml.get(0)).rms = null;
			((EstModelXml) estModelXml.get(0)).r2 = null;
			((EstModelXml) estModelXml.get(0)).aic = null;

			newTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
			newTuple.setValue(Model1Schema.ATT_ESTMODEL, estModelXml);
			newTuple.setValue(Model1Schema.ATT_DBUUID, null);
			newTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.NOTWRITABLE);

			parameterRenaming.put(newTuple, rename);
		}

		if (containsData) {
			updateMetaData(tuples, tupleCombinations);
		}

		updateParamValues(tuples, tupleCombinations);
		updatePrimaryIndepRanges(tuples, tupleCombinations);
	}

	public Map<KnimeTuple, List<KnimeTuple>> getTupleCombinations() {
		return tupleCombinations;
	}

	public Map<KnimeTuple, Map<KnimeTuple, Map<String, String>>> getParameterRenaming() {
		return parameterRenaming;
	}

	private static Map<KnimeTuple, List<KnimeTuple>> getTuplesToCombine(List<KnimeTuple> tuples, boolean containsData) {
		KnimeSchema outSchema = null;

		if (containsData) {
			outSchema = SchemaFactory.createM1DataSchema();
		} else {
			outSchema = SchemaFactory.createM1Schema();
		}

		Map<String, KnimeTuple> newTuples = new LinkedHashMap<>();
		Map<String, List<KnimeTuple>> usedTupleLists = new LinkedHashMap<>();
		Map<String, Set<String>> replacements = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			String id = null;

			try {
				id = String.valueOf(tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));
			} catch (Exception e) {
				continue;
			}

			if (containsData) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (!newTuples.containsKey(id)) {
				KnimeTuple newTuple = new KnimeTuple(outSchema);

				for (int i = 0; i < outSchema.size(); i++) {
					String attr = outSchema.getName(i);

					newTuple.setCell(attr, tuple.getCell(attr));
				}

				PmmXmlDoc params = newTuple.getPmmXml(Model1Schema.ATT_PARAMETER);

				for (PmmXmlElementConvertable el : params.getElementSet()) {
					ParamXml element = (ParamXml) el;

					element.setValue(null);
				}

				newTuple.setValue(Model1Schema.ATT_PARAMETER, params);

				newTuples.put(id, newTuple);
				usedTupleLists.put(id, new ArrayList<KnimeTuple>());
				replacements.put(id, new LinkedHashSet<String>());
			}

			String depVarSec = ((DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0)).name;

			if (replacements.get(id).add(depVarSec)) {
				usedTupleLists.get(id).add(tuple);
			}
		}

		Map<KnimeTuple, List<KnimeTuple>> toCombine = new LinkedHashMap<>();

		for (String id : newTuples.keySet()) {
			toCombine.put(newTuples.get(id), usedTupleLists.get(id));
		}

		return toCombine;
	}

	private static void updateMetaData(List<KnimeTuple> tuples, Map<KnimeTuple, List<KnimeTuple>> tupleCombinations) {
		Map<Integer, Set<String>> organisms = new LinkedHashMap<>();
		Map<Integer, Set<String>> matrices = new LinkedHashMap<>();
		Map<Integer, Set<String>> organismDetails = new LinkedHashMap<>();
		Map<Integer, Set<String>> matrixDetails = new LinkedHashMap<>();
		Map<Integer, Set<String>> comments = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			int id = -1;

			try {
				id = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			} catch (Exception e) {
				continue;
			}

			if (!organisms.containsKey(id)) {
				organisms.put(id, new LinkedHashSet<String>());
				matrices.put(id, new LinkedHashSet<String>());
				organismDetails.put(id, new LinkedHashSet<String>());
				matrixDetails.put(id, new LinkedHashSet<String>());
				comments.put(id, new LinkedHashSet<String>());
			}

			String organism = ((AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0)).name;
			String matrix = ((MatrixXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0)).getName();
			String organismDetail = ((AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0)).detail;
			String matrixDetail = ((MatrixXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0)).getDetail();
			String comment = ((MdInfoXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO).get(0)).getComment();

			if (organism != null) {
				organisms.get(id).add(organism);
			}

			if (matrix != null) {
				matrices.get(id).add(matrix);
			}

			if (organismDetail != null) {
				organismDetails.get(id).add(organismDetail);
			}

			if (matrixDetail != null) {
				matrixDetails.get(id).add(matrixDetail);
			}

			if (comment != null) {
				comments.get(id).add(comment);
			}
		}

		for (KnimeTuple tuple : tupleCombinations.keySet()) {
			int id = tupleCombinations.get(tuple).get(0).getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			String organism = "";
			String matrix = "";
			String organismDetail = "";
			String matrixDetail = "";
			String comment = "";

			for (String o : organisms.get(id)) {
				organism += "," + o;
			}

			if (!organism.isEmpty()) {
				organism = organism.substring(1);
			}

			for (String m : matrices.get(id)) {
				matrix += "," + m;
			}

			if (!matrix.isEmpty()) {
				matrix = matrix.substring(1);
			}

			for (String o : organismDetails.get(id)) {
				organismDetail += "," + o;
			}

			if (!organismDetail.isEmpty()) {
				organismDetail = organismDetail.substring(1);
			}

			for (String m : matrixDetails.get(id)) {
				matrixDetail += "," + m;
			}

			if (!matrixDetail.isEmpty()) {
				matrixDetail = matrixDetail.substring(1);
			}

			for (String c : comments.get(id)) {
				comment += "," + c;
			}

			if (!comment.isEmpty()) {
				comment = comment.substring(1);
			}

			AgentXml organismXml = new AgentXml();
			MatrixXml matrixXml = new MatrixXml();
			MdInfoXml infoXml = new MdInfoXml(null, null, comment, null, null);

			organismXml.name = organism;
			organismXml.detail = organismDetail;
			matrixXml.setName(matrix);
			matrixXml.setDetail(matrixDetail);

			tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(organismXml));
			tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(matrixXml));
			tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(infoXml));
		}
	}

	private static void updateParamValues(List<KnimeTuple> tuples,
			Map<KnimeTuple, List<KnimeTuple>> tupleCombinations) {
		Map<Integer, Map<String, Double>> paramSums = new LinkedHashMap<>();
		Map<Integer, Map<String, Integer>> paramCounts = new LinkedHashMap<>();
		Map<Integer, Map<String, Double>> paramValues = new LinkedHashMap<>();
		Map<Integer, Map<String, Double>> errorValues = new LinkedHashMap<>();
		Map<Integer, Map<String, Double>> tValues = new LinkedHashMap<>();
		Map<Integer, Map<String, Double>> pValues = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			int id = -1;

			try {
				id = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			} catch (Exception e) {
				continue;
			}

			if (!paramSums.containsKey(id)) {
				Map<String, Double> sums = new LinkedHashMap<>();
				Map<String, Integer> counts = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : tuple.getPmmXml(Model1Schema.ATT_PARAMETER).getElementSet()) {
					ParamXml param = (ParamXml) el;

					sums.put(param.getName(), 0.0);
					counts.put(param.getName(), 0);
				}

				paramSums.put(id, sums);
				paramCounts.put(id, counts);
				paramValues.put(id, new LinkedHashMap<>());
				errorValues.put(id, new LinkedHashMap<>());
				tValues.put(id, new LinkedHashMap<>());
				pValues.put(id, new LinkedHashMap<>());
			}

			Map<String, Double> sums = paramSums.get(id);
			Map<String, Integer> counts = paramCounts.get(id);
			Map<String, Double> errors = errorValues.get(id);
			Map<String, Double> ts = tValues.get(id);
			Map<String, Double> ps = pValues.get(id);

			for (PmmXmlElementConvertable el : tuple.getPmmXml(Model1Schema.ATT_PARAMETER).getElementSet()) {
				ParamXml param = (ParamXml) el;
				String name = param.getName();

				if (param.getValue() != null) {
					sums.put(name, sums.get(name) + param.getValue());
					counts.put(name, counts.get(name) + 1);
				}

				if (!errors.containsKey(name)) {
					errors.put(name, param.getError());
				} else if (!Objects.equals(errors.get(name), param.getError())) {
					errors.put(name, null);
				}

				if (!ts.containsKey(name)) {
					ts.put(name, param.getT());
				} else if (!Objects.equals(ts.get(name), param.getT())) {
					ts.put(name, null);
				}

				if (!ps.containsKey(name)) {
					ps.put(name, param.getP());
				} else if (!Objects.equals(ps.get(name), param.getP())) {
					ps.put(name, null);
				}
			}
		}

		for (Integer id : paramSums.keySet()) {
			for (String param : paramSums.get(id).keySet()) {
				int count = paramCounts.get(id).get(param);

				if (count != 0) {
					paramValues.get(id).put(param, paramSums.get(id).get(param) / count);
				}
			}
		}

		for (KnimeTuple tuple : tupleCombinations.keySet()) {
			int id = tupleCombinations.get(tuple).get(0).getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml param = (ParamXml) el;

				if (param.getValue() == null && paramValues.get(id).get(param.getName()) != null) {
					param.setValue(paramValues.get(id).get(param.getName()));
					param.getAllCorrelations().clear();
					param.setError(errorValues.get(id).get(param.getName()));
					param.setT(tValues.get(id).get(param.getName()));
					param.setP(pValues.get(id).get(param.getName()));
				}
			}

			tuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);
		}
	}

	private static void updatePrimaryIndepRanges(List<KnimeTuple> tuples,
			Map<KnimeTuple, List<KnimeTuple>> tupleCombinations) {
		Map<Integer, Map<String, Double>> indepMin = new LinkedHashMap<>();
		Map<Integer, Map<String, Double>> indepMax = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			int id = -1;

			try {
				id = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			} catch (Exception e) {
				continue;
			}

			if (!indepMin.containsKey(id)) {
				Map<String, Double> min = new LinkedHashMap<>();
				Map<String, Double> max = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).getElementSet()) {
					IndepXml indep = (IndepXml) el;

					min.put(indep.getName(), Double.POSITIVE_INFINITY);
					max.put(indep.getName(), Double.NEGATIVE_INFINITY);
				}

				indepMin.put(id, min);
				indepMax.put(id, max);
			}

			Map<String, Double> min = indepMin.get(id);
			Map<String, Double> max = indepMax.get(id);

			for (PmmXmlElementConvertable el : tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).getElementSet()) {
				IndepXml indep = (IndepXml) el;

				if (indep.getMin() != null) {
					min.put(indep.getName(), Math.min(min.get(indep.getName()), indep.getMin()));
				}

				if (indep.getMax() != null) {
					max.put(indep.getName(), Math.max(max.get(indep.getName()), indep.getMax()));
				}
			}
		}

		for (KnimeTuple tuple : tupleCombinations.keySet()) {
			int id = tupleCombinations.get(tuple).get(0).getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			Map<String, Double> mins = indepMin.get(id);
			Map<String, Double> maxs = indepMax.get(id);
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml indep = (IndepXml) el;

				if (mins.containsKey(indep.getName())) {
					Double min = mins.get(indep.getName());
					Double max = maxs.get(indep.getName());

					indep.setMin(!min.isInfinite() ? min : null);
					indep.setMax(!max.isInfinite() ? max : null);
				}
			}

			tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepXml);
		}
	}
}
