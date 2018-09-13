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
package de.bund.bfr.knime.pmm.common.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.type.Complex;

public class ParameterOptimizer {

	private static final int MAX_EVAL = 10000;

	private List<String> parameters;
	private List<Double> minParameterValues;
	private List<Double> maxParameterValues;
	private List<Double> targetValues;
	private List<String> arguments;
	private List<List<Double>> argumentValues;

	private Node function;
	private List<Node> derivatives;

	private DJep parser;

	private LeastSquaresOptimizer.Optimum optimizerValues;

	private boolean successful;
	private List<Double> parameterValues;
	private Double sse;
	private Double rms;
	private Double rSquare;
	private Double aic;
	private List<Double> parameterStandardErrors;
	private List<Double> parameterTValues;
	private List<Double> parameterPValues;
	private List<List<Double>> covariances;

	public ParameterOptimizer(String formula, List<String> parameters,
			List<Double> minParameterValues, List<Double> maxParameterValues,
			List<Double> minGuessValues, List<Double> maxGuessValues,
			List<Double> targetValues, List<String> arguments,
			List<List<Double>> argumentValues, boolean enforceLimits)
			throws ParseException {
		this.parameters = parameters;
		this.minParameterValues = minGuessValues;
		this.maxParameterValues = maxGuessValues;
		this.targetValues = targetValues;
		this.arguments = arguments;
		this.argumentValues = argumentValues;

		if (enforceLimits) {
			for (int i = 0; i < parameters.size(); i++) {
				Double min = minParameterValues.get(i);
				Double max = maxParameterValues.get(i);
				if (min != null) {
					formula += "+1000000*(" + parameters.get(i) + "<" + min
							+ ")";
				}
				if (max != null) {
					formula += "+1000000*(" + parameters.get(i) + ">" + max
							+ ")";
				}
			}
		}

		parser = MathUtilities.createParser();
		function = parser.parse(formula.substring(formula.indexOf("=") + 1));
		derivatives = new ArrayList<>(parameters.size());

		for (String arg : arguments) {
			parser.addVariable(arg, 0.0);
		}

		for (String param : parameters) {
			parser.addVariable(param, 0.0);
			derivatives.add(parser.differentiate(function, param));
		}
	}

	public void optimize(AtomicInteger progress, int nParameterSpace,
			int nLevenberg, boolean stopWhenSuccessful) {
		List<Double> paramMin = new ArrayList<>();
		List<Integer> paramStepCount = new ArrayList<>();
		List<Double> paramStepSize = new ArrayList<>();
		int maxCounter = 1;
		int paramsWithRange = 0;
		int maxStepCount = 10;

		for (int i = 0; i < parameters.size(); i++) {
			Double min = minParameterValues.get(i);
			Double max = maxParameterValues.get(i);

			if (min != null && max != null) {
				paramsWithRange++;
			}
		}

		if (paramsWithRange != 0) {
			maxStepCount = (int) Math.pow(nParameterSpace,
					1.0 / paramsWithRange);
			maxStepCount = Math.max(maxStepCount, 2);
			maxStepCount = Math.min(maxStepCount, 10);
		}

		for (int i = 0; i < parameters.size(); i++) {
			Double min = minParameterValues.get(i);
			Double max = maxParameterValues.get(i);

			if (min != null && max != null) {
				paramMin.add(min);
				paramStepCount.add(maxStepCount);
				maxCounter *= maxStepCount;

				if (max > min) {
					paramStepSize.add((max - min) / (maxStepCount - 1));
				} else {
					paramStepSize.add(1.0);
				}
			} else if (min != null) {
				if (min != 0.0) {
					paramMin.add(min);
				} else {
					paramMin.add(MathUtilities.EPSILON);
				}

				paramStepCount.add(1);
				paramStepSize.add(1.0);
			} else if (max != null) {
				if (max != 0.0) {
					paramMin.add(max);
				} else {
					paramMin.add(-MathUtilities.EPSILON);
				}

				paramStepCount.add(1);
				paramStepSize.add(1.0);
			} else {
				paramMin.add(MathUtilities.EPSILON);
				paramStepCount.add(1);
				paramStepSize.add(1.0);
			}
		}

		List<List<Double>> bestValues = new ArrayList<>();
		List<Double> bestError = new ArrayList<>();

		for (int i = 0; i < nLevenberg; i++) {
			bestValues.add(new ArrayList<>(Collections.nCopies(
					parameters.size(), i + 1.0)));
			bestError.add(Double.POSITIVE_INFINITY);
		}

		List<Integer> paramStepIndex = new ArrayList<>(Collections.nCopies(
				parameters.size(), 0));
		boolean done = false;
		int counter = 0;

		while (!done) {
			progress.set(Float.floatToIntBits((float) counter
					/ (float) maxCounter));
			counter++;

			List<Double> values = new ArrayList<>();
			double error = 0.0;

			for (int i = 0; i < parameters.size(); i++) {
				double value = paramMin.get(i) + paramStepIndex.get(i)
						* paramStepSize.get(i);

				values.add(value);
				parser.setVarValue(parameters.get(i), value);
			}

			for (int i = 0; i < targetValues.size(); i++) {
				for (int j = 0; j < arguments.size(); j++) {
					parser.setVarValue(arguments.get(j), argumentValues.get(j)
							.get(i));
				}

				try {
					double value = (Double) parser.evaluate(function);
					double diff = targetValues.get(i) - value;

					error += diff * diff;
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (ClassCastException e) {
					error = Double.POSITIVE_INFINITY;
					break;
				}
			}

			for (int i = nLevenberg; i >= 0; i--) {
				if (i == 0 || !(error < bestError.get(i - 1))) {
					if (i != nLevenberg) {
						bestError.add(i, error);
						bestValues.add(i, values);
						bestError.remove(nLevenberg);
						bestValues.remove(nLevenberg);
					}

					break;
				}
			}

			for (int i = 0;; i++) {
				if (i >= parameters.size()) {
					done = true;
					break;
				}

				paramStepIndex.set(i, paramStepIndex.get(i) + 1);

				if (paramStepIndex.get(i) >= paramStepCount.get(i)) {
					paramStepIndex.set(i, 0);
				} else {
					break;
				}
			}
		}

		successful = false;

		for (List<Double> startValues : bestValues) {
			try {
				optimize(startValues);

				double cost = optimizerValues.getCost();

				if (!successful || cost * cost < sse) {
					useCurrentResults(startValues);

					if (rSquare != 0.0) {
						successful = true;

						if (stopWhenSuccessful) {
							break;
						}
					}
				}
			} catch (TooManyEvaluationsException e) {
				break;
			} catch (ConvergenceException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isSuccessful() {
		return successful;
	}

	public List<Double> getParameterValues() {
		return parameterValues;
	}

	public Double getSse() {
		return sse;
	}

	public Double getRMS() {
		return rms;
	}

	public Double getRSquare() {
		return rSquare;
	}

	public Double getAIC() {
		return aic;
	}

	public List<Double> getParameterStandardErrors() {
		return parameterStandardErrors;
	}

	public List<Double> getParameterTValues() {
		return parameterTValues;
	}

	public List<Double> getParameterPValues() {
		return parameterPValues;
	}

	public List<List<Double>> getCovariances() {
		return covariances;
	}

	private void optimize(List<Double> startValues) throws Exception {
		double[] targets = new double[targetValues.size()];
		double[] startValueArray = new double[startValues.size()];

		for (int i = 0; i < targetValues.size(); i++) {
			targets[i] = targetValues.get(i);
		}

		for (int i = 0; i < startValues.size(); i++) {
			startValueArray[i] = startValues.get(i);
		}

		OptimizerFunction optimizerFunction = new OptimizerFunction(parser,
				function, parameters, arguments, argumentValues, targetValues);
		OptimizerFunctionJacobian optimizerFunctionJacobian = new OptimizerFunctionJacobian(
				parser, function, parameters, derivatives, arguments,
				argumentValues, targetValues);

		LeastSquaresBuilder builder = new LeastSquaresBuilder()
				.model(optimizerFunction, optimizerFunctionJacobian)
				.maxEvaluations(MAX_EVAL).maxIterations(MAX_EVAL)
				.target(targets).start(startValueArray);

		optimizerValues = new LevenbergMarquardtOptimizer().optimize(builder
				.build());
	}

	private void useCurrentResults(List<Double> startValues) {
		double cost = optimizerValues.getCost();

		parameterValues = new ArrayList<>(parameters.size());
		sse = cost * cost;
		rms = MathUtilities
				.getRMSE(sse, parameters.size(), targetValues.size());
		rSquare = MathUtilities.getRSquared(sse, targetValues);
		aic = MathUtilities.akaikeCriterion(parameters.size(),
				targetValues.size(), sse);

		for (int i = 0; i < parameters.size(); i++) {
			parameterValues.add(optimizerValues.getPoint().getEntry(i));
		}

		try {
			if (targetValues.size() <= parameters.size()) {
				throw new RuntimeException();
			}

			double[][] covMatrix = optimizerValues.getCovariances(1e-14)
					.getData();
			double factor = sse / (targetValues.size() - parameters.size());

			parameterStandardErrors = new ArrayList<>(parameters.size());
			parameterTValues = new ArrayList<>(parameters.size());
			parameterPValues = new ArrayList<>(parameters.size());
			covariances = new ArrayList<>();

			for (int i = 0; i < parameters.size(); i++) {
				double error = Math.sqrt(factor * covMatrix[i][i]);

				parameterStandardErrors.add(error);

				double tValue = optimizerValues.getPoint().getEntry(i) / error;
				int degreesOfFreedom = targetValues.size() - parameters.size();

				parameterTValues.add(tValue);
				parameterPValues.add(MathUtilities.getPValue(tValue,
						degreesOfFreedom));
			}

			for (int i = 0; i < parameters.size(); i++) {
				List<Double> cov = new ArrayList<>();

				for (int j = 0; j < parameters.size(); j++) {
					cov.add(factor * covMatrix[i][j]);
				}

				covariances.add(cov);
			}
		} catch (Exception e) {
			parameterStandardErrors = Collections.nCopies(parameters.size(),
					null);
			parameterTValues = Collections.nCopies(parameters.size(), null);
			parameterPValues = Collections.nCopies(parameters.size(), null);
			covariances = new ArrayList<>();

			for (int i = 0; i < parameters.size(); i++) {
				List<Double> nullList = Collections.nCopies(parameters.size(),
						null);

				covariances.add(nullList);
			}
		}
	}

	// private double[][] computeCovarianceMatrix(List<Double> startValues,
	// int iterations) {
	// int n = targetValues.size();
	// List<List<Double>> paramValuesList = new ArrayList<>();
	// int iteration = 0;
	// double factor = optimizer.getChiSquare()
	// / (targetValues.size() - parameters.size());
	//
	// while (iteration < iterations) {
	// List<Double> newTargetValues = new ArrayList<>();
	// List<List<Double>> newArgumentValues = new ArrayList<>();
	//
	// for (int j = 0; j < arguments.size(); j++) {
	// newArgumentValues.add(new ArrayList<Double>());
	// }
	//
	// for (int i = 0; i < n; i++) {
	// int randomPick = MathUtilities.getRandomGenerator().nextInt(n);
	//
	// newTargetValues.add(targetValues.get(randomPick));
	//
	// for (int j = 0; j < arguments.size(); j++) {
	// newArgumentValues.get(j).add(
	// argumentValues.get(j).get(randomPick));
	// }
	// }
	//
	// OptimizerFunction optimizerFunction = new OptimizerFunction(parser,
	// function, parameters, arguments, newArgumentValues,
	// newTargetValues);
	// OptimizerFunctionJacobian optimizerFunctionJacobian = new
	// OptimizerFunctionJacobian(
	// parser, function, parameters, derivatives, arguments,
	// newArgumentValues, newTargetValues);
	// double[] targets = new double[newTargetValues.size()];
	// double[] weights = new double[newTargetValues.size()];
	// double[] startValueArray = new double[startValues.size()];
	//
	// for (int i = 0; i < newTargetValues.size(); i++) {
	// targets[i] = newTargetValues.get(i);
	// weights[i] = 1.0;
	// }
	//
	// for (int i = 0; i < startValues.size(); i++) {
	// startValueArray[i] = startValues.get(i);
	// }
	//
	// LevenbergMarquardtOptimizer optimizer = new
	// LevenbergMarquardtOptimizer();
	//
	// try {
	// PointVectorValuePair optimizerValues = optimizer.optimize(
	// new ModelFunction(optimizerFunction),
	// new ModelFunctionJacobian(optimizerFunctionJacobian),
	// new MaxEval(MAX_EVAL), new Target(targets), new Weight(
	// weights), new InitialGuess(startValueArray));
	// List<Double> paramValues = new ArrayList<>();
	//
	// for (int i = 0; i < parameters.size(); i++) {
	// paramValues.add(optimizerValues.getPoint()[i]);
	// }
	//
	// paramValuesList.add(paramValues);
	// System.out.println(iteration++);
	// } catch (Exception e) {
	// }
	// }
	//
	// List<Double> meanValues = new ArrayList<>(Collections.nCopies(
	// parameters.size(), 0.0));
	// List<Double> variances = new ArrayList<>(Collections.nCopies(
	// parameters.size(), 0.0));
	//
	// for (List<Double> paramValues : paramValuesList) {
	// for (int i = 0; i < parameters.size(); i++) {
	// meanValues.set(i, meanValues.get(i) + paramValues.get(i));
	// }
	// }
	//
	// for (int i = 0; i < parameters.size(); i++) {
	// meanValues.set(i, meanValues.get(i) / iterations);
	// }
	//
	// for (List<Double> paramValues : paramValuesList) {
	// for (int i = 0; i < parameters.size(); i++) {
	// double d = Math
	// .pow(paramValues.get(i) - meanValues.get(i), 2.0);
	//
	// variances.set(i, variances.get(i) + d);
	// }
	// }
	//
	// for (int i = 0; i < parameters.size(); i++) {
	// variances.set(i, variances.get(i) / iterations * factor);
	// }
	//
	// for (int i = 0; i < parameters.size(); i++) {
	// System.out.println(parameters.get(i) + "\t" + meanValues.get(i)
	// + "\t" + Math.sqrt(variances.get(i)));
	// }
	//
	// return null;
	// }

	private static class OptimizerFunction implements
			MultivariateVectorFunction {

		private DJep parser;
		private Node function;
		private String[] parameters;
		private String[] arguments;
		private double[][] argumentValues;
		private double[] targetValues;

		public OptimizerFunction(DJep parser, Node function,
				List<String> parameters, List<String> arguments,
				List<List<Double>> argumentValues, List<Double> targetValues) {
			this.parser = parser;
			this.function = function;
			this.parameters = parameters.toArray(new String[0]);
			this.arguments = arguments.toArray(new String[0]);
			this.argumentValues = new double[targetValues.size()][arguments
					.size()];
			this.targetValues = new double[targetValues.size()];

			for (int i = 0; i < targetValues.size(); i++) {
				this.targetValues[i] = targetValues.get(i);

				for (int j = 0; j < arguments.size(); j++) {
					this.argumentValues[i][j] = argumentValues.get(j).get(i);
				}
			}
		}

		@Override
		public double[] value(double[] point) throws IllegalArgumentException {
			double[] retValue = new double[targetValues.length];

			for (int i = 0; i < parameters.length; i++) {
				parser.setVarValue(parameters[i], point[i]);
			}

			try {
				for (int i = 0; i < targetValues.length; i++) {
					for (int j = 0; j < arguments.length; j++) {
						parser.setVarValue(arguments[j], argumentValues[i][j]);
					}

					Object number = parser.evaluate(function);

					if (number instanceof Complex) {
						retValue[i] = Double.NaN;
					} else {
						retValue[i] = (Double) number;
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return retValue;
		}
	}

	private static class OptimizerFunctionJacobian implements
			MultivariateMatrixFunction {

		private DJep parser;
		private Node function;
		private String[] parameters;
		private Node[] derivatives;
		private String[] arguments;
		private double[][] argumentValues;
		private double[] targetValues;

		private List<List<Integer>> changeLists;

		public OptimizerFunctionJacobian(DJep parser, Node function,
				List<String> parameters, List<Node> derivatives,
				List<String> arguments, List<List<Double>> argumentValues,
				List<Double> targetValues) {
			this.parser = parser;
			this.function = function;
			this.parameters = parameters.toArray(new String[0]);
			this.derivatives = derivatives.toArray(new Node[0]);
			this.arguments = arguments.toArray(new String[0]);
			this.argumentValues = new double[targetValues.size()][arguments
					.size()];
			this.targetValues = new double[targetValues.size()];

			for (int i = 0; i < targetValues.size(); i++) {
				this.targetValues[i] = targetValues.get(i);

				for (int j = 0; j < arguments.size(); j++) {
					this.argumentValues[i][j] = argumentValues.get(j).get(i);
				}
			}

			changeLists = createChangeLists();
		}

		@Override
		public double[][] value(double[] point) throws IllegalArgumentException {
			double[][] retValue = new double[targetValues.length][parameters.length];

			try {
				for (int i = 0; i < targetValues.length; i++) {
					for (int j = 0; j < derivatives.length; j++) {
						retValue[i][j] = evalWithSingularityCheck(j,
								argumentValues[i], point);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return retValue;
		}

		private double evalWithSingularityCheck(int index, double[] argValues,
				double[] paramValues) throws ParseException {
			for (int i = 0; i < parameters.length; i++) {
				parser.setVarValue(parameters[i], paramValues[i]);
			}

			for (List<Integer> list : changeLists) {
				for (int i = 0; i < arguments.length; i++) {
					double d = list.get(i) * MathUtilities.EPSILON;

					parser.setVarValue(arguments[i], argValues[i] + d);
				}

				Object number = parser.evaluate(derivatives[index]);

				if (number instanceof Double && !Double.isNaN((Double) number)) {
					return (Double) number;
				}
			}

			for (List<Integer> list : changeLists) {
				for (int i = 0; i < arguments.length; i++) {
					double d = list.get(i) * MathUtilities.EPSILON;

					parser.setVarValue(arguments[i], argValues[i] + d);
				}

				parser.setVarValue(parameters[index], paramValues[index]
						- MathUtilities.EPSILON);

				Object number1 = parser.evaluate(function);

				parser.setVarValue(parameters[index], paramValues[index]
						+ MathUtilities.EPSILON);

				Object number2 = parser.evaluate(function);

				if (number1 instanceof Double
						&& !Double.isNaN((Double) number1)
						&& number2 instanceof Double
						&& !Double.isNaN((Double) number2)) {
					return ((Double) number2 - (Double) number1)
							/ (2 * MathUtilities.EPSILON);
				}
			}

			return Double.NaN;
		}

		private List<List<Integer>> createChangeLists() {
			int n = arguments.length;
			boolean done = false;
			List<List<Integer>> changeLists = new ArrayList<>();
			List<Integer> list = new ArrayList<>(Collections.nCopies(n, -1));

			while (!done) {
				changeLists.add(new ArrayList<>(list));

				for (int i = 0;; i++) {
					if (i >= n) {
						done = true;
						break;
					}

					list.set(i, list.get(i) + 1);

					if (list.get(i) > 1) {
						list.set(i, -1);
					} else {
						break;
					}
				}
			}

			Collections.sort(changeLists, new Comparator<List<Integer>>() {

				@Override
				public int compare(List<Integer> l1, List<Integer> l2) {
					int n1 = 0;
					int n2 = 0;

					for (int i : l1) {
						if (i == 0) {
							n1++;
						}
					}

					for (int i : l2) {
						if (i == 0) {
							n2++;
						}
					}

					if (n1 < n2) {
						return 1;
					} else if (n1 > n2) {
						return -1;
					} else {
						return 0;
					}
				}
			});

			return changeLists;
		}
	}

}
