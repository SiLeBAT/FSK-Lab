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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.math.ParameterOptimizer;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class SecondaryEstimationThread implements Runnable {

	private BufferedDataTable inTable;
	private KnimeSchema schema;
	private BufferedDataContainer container;

	private Map<String, Map<String, Point2D.Double>> parameterGuesses;

	private boolean enforceLimits;
	private int nParameterSpace;
	private int nLevenberg;
	private boolean stopWhenSuccessful;

	private ModelEstimationNodeModel parent;
	private AtomicInteger progress;

	public SecondaryEstimationThread(BufferedDataTable inTable,
			KnimeSchema schema, BufferedDataContainer container,
			Map<String, Map<String, Point2D.Double>> parameterGuesses,
			boolean enforceLimits, int nParameterSpace, int nLevenberg,
			boolean stopWhenSuccessful, ModelEstimationNodeModel parent,
			AtomicInteger progress) {
		this.inTable = inTable;
		this.schema = schema;
		this.container = container;
		this.parameterGuesses = parameterGuesses;
		this.enforceLimits = enforceLimits;
		this.nParameterSpace = nParameterSpace;
		this.nLevenberg = nLevenberg;
		this.stopWhenSuccessful = stopWhenSuccessful;
		this.parent = parent;
		this.progress = progress;
	}

	@Override
	public void run() {
		try {
			List<KnimeTuple> tuples = PmmUtilities.getTuples(inTable, schema);
			List<String> miscParams = PmmUtilities.getMiscParams(tuples);
			Map<String, List<Double>> depVarMap = new LinkedHashMap<>();
			Map<String, Map<String, List<Double>>> miscMaps = new LinkedHashMap<>();
			Set<String> ids = new LinkedHashSet<>();
			Map<Integer, Integer> globalIds = new LinkedHashMap<>();

			for (String param : miscParams) {
				miscMaps.put(param, new LinkedHashMap<String, List<Double>>());
			}

			for (KnimeTuple tuple : tuples) {
				DepXml depXml = (DepXml) tuple.getPmmXml(
						Model2Schema.ATT_DEPENDENT).get(0);
				CatalogModelXml primModelXml = (CatalogModelXml) tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
				String id = depXml.name + " (" + primModelXml.id
						+ ")";

				if (!globalIds.containsKey(primModelXml.id)) {
					globalIds.put(primModelXml.id,
							MathUtilities.getRandomNegativeInt());
				}

				if (ids.add(id)) {
					depVarMap.put(id, new ArrayList<Double>());
					miscMaps.put(id, new LinkedHashMap<String, List<Double>>());

					for (String param : miscParams) {
						miscMaps.get(param).put(id, new ArrayList<Double>());
					}
				}

				PmmXmlDoc params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
				Double value = null;
				Double minValue = null;
				Double maxValue = null;
				boolean valueMissing = false;

				for (PmmXmlElementConvertable el : params.getElementSet()) {
					ParamXml element = (ParamXml) el;
					String depVarSec = depXml.name;

					if (element.getName().equals(depVarSec)) {
						if (element.getValue() == null) {
							valueMissing = true;
						}

						value = element.getValue();
						minValue = element.getMin();
						maxValue = element.getMax();
					}
				}

				if (valueMissing) {
					continue;
				}

				if ((minValue != null && value < minValue)
						|| (maxValue != null && value > maxValue)) {
					parent.setWarning("Some primary parameters are out of their range of values");
				}

				depVarMap.get(id).add(value);

				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

				for (String param : miscParams) {
					Double paramValue = null;

					for (PmmXmlElementConvertable el : misc.getElementSet()) {
						MiscXml element = (MiscXml) el;

						if (param.equals(element.name)) {
							paramValue = element.value;
							break;
						}
					}

					miscMaps.get(param).get(id).add(paramValue);
				}
			}

			Map<String, PmmXmlDoc> paramMap = new LinkedHashMap<>();
			Map<String, PmmXmlDoc> indepMap = new LinkedHashMap<>();
			Map<String, PmmXmlDoc> estModelMap = new LinkedHashMap<>();

			for (KnimeTuple tuple : tuples) {
				DepXml depXml = (DepXml) tuple.getPmmXml(
						Model2Schema.ATT_DEPENDENT).get(0);
				CatalogModelXml primModelXml = (CatalogModelXml) tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
				String id = depXml.name + " (" + primModelXml.id
						+ ")";

				if (!paramMap.containsKey(id)) {
					PmmXmlDoc modelXml = tuple
							.getPmmXml(Model2Schema.ATT_MODELCATALOG);
					PmmXmlDoc paramXml = tuple
							.getPmmXml(Model2Schema.ATT_PARAMETER);
					PmmXmlDoc indepXml = tuple
							.getPmmXml(Model2Schema.ATT_INDEPENDENT);
					String formula = ((CatalogModelXml) modelXml.get(0))
							.formula;
					List<String> parameters = new ArrayList<>();
					List<Double> minParameterValues = new ArrayList<>();
					List<Double> maxParameterValues = new ArrayList<>();
					List<Double> minGuessValues = new ArrayList<>();
					List<Double> maxGuessValues = new ArrayList<>();
					List<Double> targetValues = depVarMap.get(id);
					List<String> arguments = CellIO.getNameList(indepXml);
					List<List<Double>> argumentValues = new ArrayList<>();
					String modelID = ((CatalogModelXml) modelXml.get(0))
							.id + "";
					Map<String, Point2D.Double> modelGuesses = parameterGuesses
							.get(ModelEstimationNodeModel.SECONDARY + modelID);

					if (modelGuesses == null) {
						modelGuesses = new LinkedHashMap<>();
					}

					for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
						ParamXml element = (ParamXml) el;

						parameters.add(element.getName());
						minParameterValues.add(element.getMin());
						maxParameterValues.add(element.getMax());

						if (modelGuesses.containsKey(element.getName())) {
							Point2D.Double guess = modelGuesses.get(element
									.getName());

							if (!Double.isNaN(guess.x)) {
								minGuessValues.add(guess.x);
							} else {
								minGuessValues.add(null);
							}

							if (!Double.isNaN(guess.y)) {
								maxGuessValues.add(guess.y);
							} else {
								maxGuessValues.add(null);
							}
						} else {
							minGuessValues.add(element.getMin());
							maxGuessValues.add(element.getMax());
						}
					}

					for (String arg : arguments) {
						if (miscParams.contains(arg)) {
							argumentValues.add(miscMaps.get(arg).get(id));
						}
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
							.getPmmXml(Model2Schema.ATT_ESTMODEL);

					((EstModelXml) estModelXml.get(0)).id = estID;
					((EstModelXml) estModelXml.get(0))
							.name = createModelName(tuple);
					((EstModelXml) estModelXml.get(0)).sse = sse;
					((EstModelXml) estModelXml.get(0)).rms = rms;
					((EstModelXml) estModelXml.get(0)).r2 = rSquared;
					((EstModelXml) estModelXml.get(0)).aic = aic;
					((EstModelXml) estModelXml.get(0)).dof = dof;

					paramMap.put(id, paramXml);
					indepMap.put(id, indepXml);
					estModelMap.put(id, estModelXml);
				}

				tuple.setValue(Model2Schema.ATT_PARAMETER, paramMap.get(id));
				tuple.setValue(Model2Schema.ATT_INDEPENDENT, indepMap.get(id));
				tuple.setValue(Model2Schema.ATT_ESTMODEL, estModelMap.get(id));
				tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
						Model2Schema.WRITABLE);
				tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID,
						globalIds.get(primModelXml.id));

				container.addRowToTable(tuple);
			}

			container.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String createModelName(KnimeTuple tuple) {
		AgentXml agent = (AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT)
				.get(0);
		MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);

		String depVar = ((DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
				.get(0)).name;
		String agentName = agent.name != null ? agent.name : agent.detail;
		String matrixName = matrix.name != null ? matrix.name
				: matrix.detail;
		String modelName = ((CatalogModelXml) tuple.getPmmXml(
				Model2Schema.ATT_MODELCATALOG).get(0)).name;

		return depVar + "_" + agentName + "_" + matrixName + "_" + modelName;
	}
}
