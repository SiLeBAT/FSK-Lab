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
package de.bund.bfr.knime.pmm.modelestimation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.math.ParameterOptimizer;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class OneStepEstimationThread implements Runnable {

	private BufferedDataTable inTable;
	private KnimeSchema schema;
	private BufferedDataContainer container;

	private Map<String, Map<String, Point2D.Double>> parameterGuesses;

	private boolean enforceLimits;
	private int nParameterSpace;
	private int nLevenberg;
	private boolean stopWhenSuccessful;

	private AtomicInteger progress;

	public OneStepEstimationThread(BufferedDataTable inTable,
			KnimeSchema schema, BufferedDataContainer container,
			Map<String, Map<String, Point2D.Double>> parameterGuesses,
			boolean enforceLimits, int nParameterSpace, int nLevenberg,
			boolean stopWhenSuccessful, AtomicInteger progress) {
		this.inTable = inTable;
		this.schema = schema;
		this.container = container;
		this.parameterGuesses = parameterGuesses;
		this.enforceLimits = enforceLimits;
		this.nParameterSpace = nParameterSpace;
		this.nLevenberg = nLevenberg;
		this.stopWhenSuccessful = stopWhenSuccessful;
		this.progress = progress;
	}

	@Override
	public void run() {
		try {
			KnimeRelationReader reader = new KnimeRelationReader(schema,
					inTable);
			List<KnimeTuple> seiTuples = new ArrayList<>();

			while (reader.hasMoreElements()) {
				seiTuples.add(reader.nextElement());
			}

			for (KnimeTuple tuple : seiTuples) {
				PmmXmlDoc params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
				String primID = ((CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).id
						+ "";
				Map<String, Point2D.Double> primaryGuesses = parameterGuesses
						.get(ModelEstimationNodeModel.PRIMARY + primID);

				if (primaryGuesses == null) {
					primaryGuesses = new LinkedHashMap<>();
				}

				for (PmmXmlElementConvertable el : params.getElementSet()) {
					ParamXml element = (ParamXml) el;

					if (primaryGuesses.containsKey(element.getName())) {
						Point2D.Double guess = primaryGuesses.get(element
								.getName());

						if (!Double.isNaN(guess.x)) {
							element.setMinGuess(guess.x);
						} else {
							element.setMinGuess(null);
						}

						if (!Double.isNaN(guess.y)) {
							element.setMaxGuess(guess.y);
						} else {
							element.setMaxGuess(null);
						}
					} else {
						element.setMinGuess(element.getMin());
						element.setMaxGuess(element.getMax());
					}
				}

				String secID = ((CatalogModelXml) tuple.getPmmXml(
						Model2Schema.ATT_MODELCATALOG).get(0)).id
						+ "";
				PmmXmlDoc secParams = tuple
						.getPmmXml(Model2Schema.ATT_PARAMETER);
				Map<String, Point2D.Double> secGuesses = parameterGuesses
						.get(ModelEstimationNodeModel.SECONDARY + secID);

				if (secGuesses == null) {
					secGuesses = new LinkedHashMap<>();
				}

				for (PmmXmlElementConvertable el : secParams.getElementSet()) {
					ParamXml element = (ParamXml) el;

					if (secGuesses.containsKey(element.getName())) {
						Point2D.Double guess = secGuesses
								.get(element.getName());

						if (!Double.isNaN(guess.x)) {
							element.setMinGuess(guess.x);
						} else {
							element.setMinGuess(null);
						}

						if (!Double.isNaN(guess.y)) {
							element.setMaxGuess(guess.y);
						} else {
							element.setMaxGuess(null);
						}
					} else {
						element.setMinGuess(element.getMin());
						element.setMaxGuess(element.getMax());
					}
				}

				tuple.setValue(Model1Schema.ATT_PARAMETER, params);
				tuple.setValue(Model2Schema.ATT_PARAMETER, secParams);
			}

			ModelCombiner combiner = new ModelCombiner(seiTuples, true, null,
					null);
			List<KnimeTuple> tuples = new ArrayList<>(combiner
					.getTupleCombinations().keySet());
			Map<KnimeTuple, Map<KnimeTuple, Map<String, String>>> renamings = combiner
					.getParameterRenaming();
			Map<Integer, List<List<Double>>> argumentValuesMap = new LinkedHashMap<>();
			Map<Integer, List<Double>> targetValuesMap = new LinkedHashMap<>();

			for (KnimeTuple tuple : tuples) {
				int id = ((CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0)).id;
				PmmXmlDoc indepXml = tuple
						.getPmmXml(Model1Schema.ATT_INDEPENDENT);
				List<String> arguments = CellIO.getNameList(indepXml);
				PmmXmlDoc timeSeriesXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

				List<Double> targetValues = new ArrayList<>();
				List<Double> timeList = new ArrayList<>();
				Map<String, List<Double>> miscLists = new LinkedHashMap<>();
				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					timeList.add(element.getTime());
					targetValues.add(element.getConcentration());
				}

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;
					List<Double> list = new ArrayList<>(Collections.nCopies(
							timeList.size(), element.value));

					miscLists.put(element.name, list);
				}

				if (!targetValuesMap.containsKey(id)) {
					targetValuesMap.put(id, new ArrayList<Double>());
					argumentValuesMap.put(id, new ArrayList<List<Double>>());

					for (int i = 0; i < arguments.size(); i++) {
						argumentValuesMap.get(id).add(new ArrayList<Double>());
					}
				}

				targetValuesMap.get(id).addAll(targetValues);

				for (int i = 0; i < arguments.size(); i++) {
					if (arguments.get(i).equals(AttributeUtilities.TIME)) {
						argumentValuesMap.get(id).get(i).addAll(timeList);
					} else {
						argumentValuesMap.get(id).get(i)
								.addAll(miscLists.get(arguments.get(i)));
					}
				}
			}

			Map<Integer, PmmXmlDoc> paramMap = new LinkedHashMap<>();
			Map<Integer, PmmXmlDoc> indepMap = new LinkedHashMap<>();
			Map<Integer, PmmXmlDoc> estModelMap = new LinkedHashMap<>();
			int n = tuples.size();

			for (int i = 0; i < n; i++) {
				KnimeTuple tuple = tuples.get(i);
				PmmXmlDoc modelXml = tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG);
				int id = ((CatalogModelXml) modelXml.get(0)).id;

				if (!paramMap.containsKey(id)) {
					String formula = ((CatalogModelXml) modelXml.get(0))
							.formula;
					PmmXmlDoc paramXml = tuple
							.getPmmXml(Model1Schema.ATT_PARAMETER);
					PmmXmlDoc indepXml = tuple
							.getPmmXml(Model1Schema.ATT_INDEPENDENT);
					List<String> parameters = new ArrayList<>();
					List<String> paramOrigNames = new ArrayList<>();
					List<Double> minParameterValues = new ArrayList<>();
					List<Double> maxParameterValues = new ArrayList<>();
					List<Double> minGuessValues = new ArrayList<>();
					List<Double> maxGuessValues = new ArrayList<>();
					List<Double> targetValues = targetValuesMap.get(id);
					List<String> arguments = CellIO.getNameList(indepXml);
					List<List<Double>> argumentValues = argumentValuesMap
							.get(id);

					for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
						ParamXml element = (ParamXml) el;

						parameters.add(element.getName());
						paramOrigNames.add(element.getOrigName());
						minParameterValues.add(element.getMin());
						maxParameterValues.add(element.getMax());
						minGuessValues.add(element.getMinGuess());
						maxGuessValues.add(element.getMaxGuess());
					}

					MathUtilities
							.removeNullValues(targetValues, argumentValues);

					List<Double> parameterValues = Collections.nCopies(
							parameters.size(), null);
					List<Double> parameterErrors = Collections.nCopies(
							parameters.size(), null);
					List<Double> parameterTValues = Collections.nCopies(
							parameters.size(), null);
					List<Double> parameterPValues = Collections.nCopies(
							parameters.size(), null);
					List<List<Double>> covariances = new ArrayList<>();

					for (int j = 0; j < parameters.size(); j++) {
						List<Double> nullList = Collections.nCopies(
								parameters.size(), null);

						covariances.add(nullList);
					}

					Double sse = null;
					Double rms = null;
					Double rSquared = null;
					Double aic = null;
					Integer dof = null;
					Integer estID = MathUtilities.getRandomNegativeInt();
					List<Double> minValues = Collections.nCopies(
							arguments.size(), null);
					List<Double> maxValues = Collections.nCopies(
							arguments.size(), null);
					boolean successful = false;
					ParameterOptimizer optimizer = null;

					if (!targetValues.isEmpty()) {
						optimizer = new ParameterOptimizer(formula, parameters,
								minParameterValues, maxParameterValues,
								minGuessValues, maxGuessValues, targetValues,
								arguments, argumentValues, enforceLimits);
						optimizer.optimize(progress, nParameterSpace,
								nLevenberg, stopWhenSuccessful);
						successful = optimizer.isSuccessful();
					}

					if (successful) {
						parameterValues = optimizer.getParameterValues();
						parameterErrors = optimizer
								.getParameterStandardErrors();
						parameterTValues = optimizer.getParameterTValues();
						parameterPValues = optimizer.getParameterPValues();
						covariances = optimizer.getCovariances();
						sse = optimizer.getSse();
						rms = optimizer.getRMS();
						rSquared = optimizer.getRSquare();
						aic = optimizer.getAIC();
						dof = targetValues.size() - parameters.size();
						minValues = new ArrayList<>();
						maxValues = new ArrayList<>();

						for (List<Double> values : argumentValues) {
							minValues.add(Collections.min(values));
							maxValues.add(Collections.max(values));
						}
					}

					for (int j = 0; j < paramXml.getElementSet().size(); j++) {
						ParamXml element = (ParamXml) paramXml.get(j);

						element.setValue(parameterValues.get(j));
						element.setError(parameterErrors.get(j));
						element.setT(parameterTValues.get(j));
						element.setP(parameterPValues.get(j));

						for (int k = 0; k < paramXml.getElementSet().size(); k++) {
							element.addCorrelation(
									((ParamXml) paramXml.get(k)).getOrigName(),
									covariances.get(j).get(k));
						}
					}

					for (int j = 0; j < indepXml.getElementSet().size(); j++) {
						IndepXml element = (IndepXml) indepXml.get(j);

						element.min = minValues.get(j);
						element.max = maxValues.get(j);
					}

					PmmXmlDoc estModelXml = tuple
							.getPmmXml(Model1Schema.ATT_ESTMODEL);

					((EstModelXml) estModelXml.get(0)).id = estID;
					((EstModelXml) estModelXml.get(0)).sse = sse;
					((EstModelXml) estModelXml.get(0)).rms = rms;
					((EstModelXml) estModelXml.get(0)).r2 = rSquared;
					((EstModelXml) estModelXml.get(0)).aic = aic;
					((EstModelXml) estModelXml.get(0)).dof = dof;

					paramMap.put(id, paramXml);
					indepMap.put(id, indepXml);
					estModelMap.put(id, estModelXml);
				}

				int index = 1;

				for (KnimeTuple t : renamings.get(tuple).keySet()) {
					PmmXmlDoc primParamXml = t
							.getPmmXml(Model1Schema.ATT_PARAMETER);

					for (PmmXmlElementConvertable el : primParamXml
							.getElementSet()) {
						ParamXml param = (ParamXml) el;
						ParamXml p = getParam(paramMap.get(id), param.getName());

						if (p != null) {
							param.setValue(p.getValue());
							param.setError(p.getError());
							param.setT(p.getT());
							param.setP(p.getP());
						}
					}

					t.setValue(Model1Schema.ATT_PARAMETER, primParamXml);

					PmmXmlDoc secParamXml = t
							.getPmmXml(Model2Schema.ATT_PARAMETER);

					for (PmmXmlElementConvertable el : secParamXml
							.getElementSet()) {
						ParamXml param = (ParamXml) el;
						ParamXml p = getParam(paramMap.get(id),
								renamings.get(tuple).get(t)
										.get(param.getName()));

						if (p != null) {
							param.setValue(p.getValue());
							param.setError(p.getError());
							param.setT(p.getT());
							param.setP(p.getP());
						}
					}

					t.setValue(Model2Schema.ATT_PARAMETER, secParamXml);

					PmmXmlDoc primIndepXml = t
							.getPmmXml(Model1Schema.ATT_INDEPENDENT);

					for (PmmXmlElementConvertable el : primIndepXml
							.getElementSet()) {
						IndepXml indep = (IndepXml) el;
						IndepXml d = getIndep(indepMap.get(id), indep.name);

						if (d != null) {
							indep.min = d.min;
							indep.max = d.max;
							indep.unit = d.unit;
						}
					}

					t.setValue(Model1Schema.ATT_INDEPENDENT, primIndepXml);

					PmmXmlDoc secIndepXml = t
							.getPmmXml(Model2Schema.ATT_INDEPENDENT);

					for (PmmXmlElementConvertable el : secIndepXml
							.getElementSet()) {
						IndepXml indep = (IndepXml) el;
						IndepXml d = getIndep(indepMap.get(id), indep.name);

						if (d != null) {
							indep.min = d.min;
							indep.max = d.max;
							indep.unit = d.unit;
						}
					}

					t.setValue(Model2Schema.ATT_INDEPENDENT, secIndepXml);

					Integer estID = ((EstModelXml) tuple.getPmmXml(
							Model1Schema.ATT_ESTMODEL).get(0)).id;

					t.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(
							new EstModelXml(estID, createModelName(tuple),
									null, null, null, null, null, null)));
					t.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(
							new EstModelXml(estID + index, null, null, null,
									null, null, null, null)));
					t.setValue(Model1Schema.ATT_DATABASEWRITABLE,
							Model1Schema.WRITABLE);
					t.setValue(Model2Schema.ATT_DATABASEWRITABLE,
							Model2Schema.WRITABLE);

					container.addRowToTable(t);
					index++;
				}
			}

			container.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static ParamXml getParam(PmmXmlDoc xml, String paramName) {
		for (PmmXmlElementConvertable el : xml.getElementSet()) {
			if (((ParamXml) el).getName().equals(paramName)) {
				return (ParamXml) el;
			}
		}

		return null;
	}

	private static IndepXml getIndep(PmmXmlDoc xml, String indepName) {
		for (PmmXmlElementConvertable el : xml.getElementSet()) {
			if (((IndepXml) el).name.equals(indepName)) {
				return (IndepXml) el;
			}
		}

		return null;
	}

	private String createModelName(KnimeTuple tuple) {
		AgentXml agent = (AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT)
				.get(0);
		MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);

		String agentName = agent.name != null ? agent.name : agent.detail;
		String matrixName = matrix.name != null ? matrix.name
				: matrix.detail;
		String modelName = ((CatalogModelXml) tuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0)).name;

		return agentName + "_" + matrixName + "_" + modelName;
	}
}
