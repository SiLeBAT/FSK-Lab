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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.math.ParameterOptimizer;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class PrimaryEstimationThread implements Runnable {

	private KnimeTuple tuple;

	private Map<String, Map<String, Point2D.Double>> parameterGuesses;

	private boolean enforceLimits;
	private int nParameterSpace;
	private int nLevenberg;
	private boolean stopWhenSuccessful;

	private AtomicInteger runningThreads;
	private AtomicInteger finishedThreads;

	public PrimaryEstimationThread(KnimeTuple tuple,
			Map<String, Map<String, Point2D.Double>> parameterGuesses,
			boolean enforceLimits, int nParameterSpace, int nLevenberg,
			boolean stopWhenSuccessful, AtomicInteger runningThreads,
			AtomicInteger finishedThreads) {
		this.tuple = tuple;
		this.parameterGuesses = parameterGuesses;
		this.enforceLimits = enforceLimits;
		this.nParameterSpace = nParameterSpace;
		this.nLevenberg = nLevenberg;
		this.stopWhenSuccessful = stopWhenSuccessful;
		this.runningThreads = runningThreads;
		this.finishedThreads = finishedThreads;
	}

	@Override
	public void run() {
		try {
			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String modelID = ((CatalogModelXml) modelXml.get(0)).getId() + "";
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			List<String> parameters = new ArrayList<>();
			List<Double> minParameterValues = new ArrayList<>();
			List<Double> maxParameterValues = new ArrayList<>();
			List<Double> minGuessValues = new ArrayList<>();
			List<Double> maxGuessValues = new ArrayList<>();
			PmmXmlDoc timeSeriesXml = tuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			List<Double> targetValues = new ArrayList<>();
			List<Double> timeValues = new ArrayList<>();
			List<String> arguments = Arrays.asList(AttributeUtilities.TIME);
			Map<String, Point2D.Double> guesses = parameterGuesses
					.get(ModelEstimationNodeModel.PRIMARY + modelID);

			for (PmmXmlElementConvertable el : timeSeriesXml.getElementSet()) {
				TimeSeriesXml element = (TimeSeriesXml) el;

				timeValues.add(element.getTime());
				targetValues.add(element.getConcentration());
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				parameters.add(element.getName());
				minParameterValues.add(element.getMin());
				maxParameterValues.add(element.getMax());

				if (guesses != null && guesses.containsKey(element.getName())) {
					Point2D.Double guess = guesses.get(element.getName());

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
				List<Double> nullList = Collections.nCopies(parameters.size(),
						null);

				covariances.add(nullList);
			}

			Double sse = null;
			Double rms = null;
			Double rSquare = null;
			Double aic = null;
			Integer dof = null;
			Double minIndep = null;
			Double maxIndep = null;
			Integer estID = null;
			boolean successful = false;
			ParameterOptimizer optimizer = null;

			if (!targetValues.isEmpty() && !timeValues.isEmpty()) {
				List<List<Double>> argumentValues = new ArrayList<>();

				argumentValues.add(timeValues);
				MathUtilities.removeNullValues(targetValues, argumentValues);

				minIndep = Collections.min(argumentValues.get(0));
				maxIndep = Collections.max(argumentValues.get(0));
				optimizer = new ParameterOptimizer(formula, parameters,
						minParameterValues, maxParameterValues, minGuessValues,
						maxGuessValues, targetValues, arguments,
						argumentValues, enforceLimits);
				optimizer.optimize(new AtomicInteger(), nParameterSpace,
						nLevenberg, stopWhenSuccessful);
				successful = optimizer.isSuccessful();
			}

			if (successful) {
				parameterValues = optimizer.getParameterValues();
				parameterErrors = optimizer.getParameterStandardErrors();
				parameterTValues = optimizer.getParameterTValues();
				parameterPValues = optimizer.getParameterPValues();
				covariances = optimizer.getCovariances();
				sse = optimizer.getSse();
				rms = optimizer.getRMS();
				rSquare = optimizer.getRSquare();
				aic = optimizer.getAIC();
				dof = targetValues.size() - parameters.size();
				estID = MathUtilities.getRandomNegativeInt();
			}

			for (int i = 0; i < paramXml.getElementSet().size(); i++) {
				ParamXml element = (ParamXml) paramXml.get(i);

				element.setValue(parameterValues.get(i));
				element.setError(parameterErrors.get(i));
				element.setT(parameterTValues.get(i));
				element.setP(parameterPValues.get(i));

				for (int j = 0; j < paramXml.getElementSet().size(); j++) {
					element.addCorrelation(
							((ParamXml) paramXml.get(j)).getOrigName(),
							covariances.get(i).get(j));
				}
			}

			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);

			((IndepXml) indepXml.get(0)).setMin(minIndep);
			((IndepXml) indepXml.get(0)).setMax(maxIndep);

			PmmXmlDoc estModelXml = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);

			((EstModelXml) estModelXml.get(0)).setId(estID);
			((EstModelXml) estModelXml.get(0)).setName(createModelName(tuple));
			((EstModelXml) estModelXml.get(0)).setSse(sse);
			((EstModelXml) estModelXml.get(0)).setRms(rms);
			((EstModelXml) estModelXml.get(0)).setR2(rSquare);
			((EstModelXml) estModelXml.get(0)).setAic(aic);
			((EstModelXml) estModelXml.get(0)).setDof(dof);

			tuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);
			tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepXml);
			tuple.setValue(Model1Schema.ATT_ESTMODEL, estModelXml);
			runningThreads.decrementAndGet();
			finishedThreads.incrementAndGet();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String createModelName(KnimeTuple tuple) {
		String combaseId = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		int condId = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
		AgentXml agent = (AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT)
				.get(0);
		MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);

		String dataName = combaseId != null ? combaseId : String
				.valueOf(condId);
		String agentName = agent.name != null ? agent.name : agent.detail;
		String matrixName = matrix.getName() != null ? matrix.getName()
				: matrix.getDetail();
		String modelName = ((CatalogModelXml) tuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0)).getName();

		return dataName + "_" + agentName + "_" + matrixName + "_" + modelName;
	}
}
