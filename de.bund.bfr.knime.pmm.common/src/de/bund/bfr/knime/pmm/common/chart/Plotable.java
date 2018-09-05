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
package de.bund.bfr.knime.pmm.common.chart;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.distribution.TDistribution;
import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

public class Plotable {

	private static final int FUNCTION_STEPS = 1000;
	private static final double EPSILON = 1e-5;

	public static final int DATASET = 0;
	public static final int DATASET_STRICT = 1;
	public static final int FUNCTION = 2;
	public static final int BOTH = 3;
	public static final int BOTH_STRICT = 4;
	public static final int FUNCTION_SAMPLE = 5;

	private int type;
	private Map<String, List<Double>> valueLists;
	private String function;
	private String functionValue;
	private Double minValue;
	private Double maxValue;
	private Map<String, List<Double>> functionArguments;
	private Map<String, Double> functionParameters;
	private Map<String, Map<String, Double>> covariances;
	private Map<String, Double> minArguments;
	private Map<String, Double> maxArguments;
	private Map<String, List<String>> categories;
	private Map<String, String> units;
	private List<Double> samples;
	private Integer degreesOfFreedom;

	public Plotable(int type) {
		this.type = type;
		valueLists = new LinkedHashMap<>();
		functionArguments = new LinkedHashMap<>();
		minArguments = new LinkedHashMap<>();
		maxArguments = new LinkedHashMap<>();
		categories = new LinkedHashMap<>();
		units = new LinkedHashMap<>();
		functionParameters = new LinkedHashMap<>();
		covariances = new LinkedHashMap<>();
		samples = new ArrayList<>();
		degreesOfFreedom = null;
	}

	public int getType() {
		return type;
	}

	public List<Double> getValueList(String parameter) {
		return valueLists.get(parameter);
	}

	public void addValueList(String parameter, List<Double> valueList) {
		valueLists.put(parameter, valueList);
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getFunctionValue() {
		return functionValue;
	}

	public void setFunctionValue(String functionValue) {
		this.functionValue = functionValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}
	
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Map<String, List<Double>> getFunctionArguments() {
		return functionArguments;
	}

	public Map<String, List<Double>> getPossibleArgumentValues(boolean useData,
			boolean useMinMax) {
		Map<String, List<Double>> args = new LinkedHashMap<>();

		for (String var : functionArguments.keySet()) {
			Double min = minArguments.get(var);
			Double max = maxArguments.get(var);

			if (useData && valueLists.get(var) != null) {
				Set<Double> valuesSet = new LinkedHashSet<>(valueLists.get(var));
				List<Double> valuesList = new ArrayList<>(valuesSet);

				valuesList.removeAll(Arrays.asList((Object) null));
				Collections.sort(valuesList);
				args.put(var, valuesList);
			} else if (useMinMax && min != null) {
				args.put(var, new ArrayList<>(Arrays.asList(min)));
			} else if (useMinMax && max != null) {
				args.put(var, new ArrayList<>(Arrays.asList(max)));
			} else {
				args.put(var, new ArrayList<Double>());
			}
		}

		return args;
	}

	public void setFunctionArguments(Map<String, List<Double>> functionArguments) {
		this.functionArguments = functionArguments;
	}

	public Map<String, Double> getFunctionParameters() {
		return functionParameters;
	}

	public void setFunctionParameters(Map<String, Double> functionParameters) {
		this.functionParameters = functionParameters;
	}

	public Map<String, Map<String, Double>> getCovariances() {
		return covariances;
	}

	public void setCovariances(Map<String, Map<String, Double>> covariances) {
		this.covariances = covariances;
	}

	public Map<String, Double> getMinArguments() {
		return minArguments;
	}

	public void setMinArguments(Map<String, Double> minArguments) {
		this.minArguments = minArguments;
	}

	public Map<String, Double> getMaxArguments() {
		return maxArguments;
	}

	public void setMaxArguments(Map<String, Double> maxArguments) {
		this.maxArguments = maxArguments;
	}

	public Map<String, List<String>> getCategories() {
		return categories;
	}

	public void setCategories(Map<String, List<String>> categories) {
		this.categories = categories;
	}

	public Map<String, String> getUnits() {
		return units;
	}

	public void setUnits(Map<String, String> units) {
		this.units = units;
	}

	public List<Double> getSamples() {
		return samples;
	}

	public void setSamples(List<Double> samples) {
		this.samples = samples;
	}

	public Integer getDegreesOfFreedom() {
		return degreesOfFreedom;
	}

	public void setDegreesOfFreedom(Integer degreesOfFreedom) {
		this.degreesOfFreedom = degreesOfFreedom;
	}

	public double[][] getPoints(String paramX, String paramY, String unitX,
			String unitY, String transformX, String transformY)
			throws ConvertException {
		return getPoints(paramX, paramY, unitX, unitY, transformX, transformY,
				getStandardChoice());
	}

	public double[][] getPoints(String paramX, String paramY, String unitX,
			String unitY, String transformX, String transformY,
			Map<String, Integer> choice) throws ConvertException {
		List<Double> xList = valueLists.get(paramX);
		List<Double> yList = valueLists.get(paramY);

		if (xList == null || yList == null) {
			return null;
		}

		List<Boolean> usedPoints = new ArrayList<>(Collections.nCopies(
				xList.size(), true));

		if (type == BOTH_STRICT || type == DATASET_STRICT) {
			for (String arg : functionArguments.keySet()) {
				if (!arg.equals(paramX) && valueLists.containsKey(arg)) {
					Double fixedValue = functionArguments.get(arg).get(
							choice.get(arg));
					List<Double> values = valueLists.get(arg);

					for (int i = 0; i < values.size(); i++) {
						if (!fixedValue.equals(values.get(i))) {
							usedPoints.set(i, false);
						}
					}
				}
			}

			if (!usedPoints.contains(true)) {
				return null;
			}
		}

		List<Point2D.Double> points = new ArrayList<>(xList.size());

		for (int i = 0; i < xList.size(); i++) {
			Double x = xList.get(i);
			Double y = yList.get(i);

			if (x != null) {
				x = convertToUnit(paramX, x, unitX);
				x = transform(x, transformX);
			}

			if (y != null) {
				y = convertToUnit(paramY, y, unitY);
				y = transform(y, transformY);
			}

			if (usedPoints.get(i) && isValidValue(x) && isValidValue(y)) {
				points.add(new Point2D.Double(x, y));
			}
		}

		Collections.sort(points, new Comparator<Point2D.Double>() {

			@Override
			public int compare(Point2D.Double p1, Point2D.Double p2) {
				return Double.compare(p1.x, p2.x);
			}
		});

		double[][] pointsArray = new double[2][points.size()];

		for (int i = 0; i < points.size(); i++) {
			pointsArray[0][i] = points.get(i).x;
			pointsArray[1][i] = points.get(i).y;
		}

		return pointsArray;
	}

	public double[][] getFunctionPoints(String paramX, String paramY,
			String unitX, String unitY, String transformX, String transformY,
			double minX, double maxX, double minY, double maxY)
			throws ConvertException {
		return getFunctionPoints(paramX, paramY, unitX, unitY, transformX,
				transformY, minX, maxX, minY, maxY, getStandardChoice());
	}

	public double[][] getFunctionPoints(String paramX, String paramY,
			String unitX, String unitY, String transformX, String transformY,
			double minX, double maxX, double minY, double maxY,
			Map<String, Integer> choice) throws ConvertException {
		if (function == null) {
			return null;
		}

		if (!function.startsWith(paramY + "=")
				|| !functionArguments.containsKey(paramX)) {
			return null;
		}

		double[][] points = new double[2][FUNCTION_STEPS];
		DJep parser = MathUtilities.createParser();
		Node f = null;

		for (String param : functionParameters.keySet()) {
			if (functionParameters.get(param) == null) {
				return null;
			}

			parser.addConstant(param, functionParameters.get(param));
		}

		for (String param : functionArguments.keySet()) {
			if (!param.equals(paramX)) {
				parser.addConstant(param,
						functionArguments.get(param).get(choice.get(param)));
			}
		}

		parser.addVariable(paramX, 0.0);

		try {
			f = parser.parse(function.replace(paramY + "=", ""));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int j = 0; j < FUNCTION_STEPS; j++) {
			double x = minX + (double) j / (double) (FUNCTION_STEPS - 1)
					* (maxX - minX);

			parser.setVarValue(
					paramX,
					convertFromUnit(paramX, inverseTransform(x, transformX),
							unitX));

			try {
				Object number = parser.evaluate(f);
				Double y;

				if (isInRange(number)) {
					y = (Double) number;
					y = convertToUnit(paramY, y, unitY);
					y = transform(y, transformY);

					if (y == null || y < minY || y > maxY || y.isInfinite()) {
						y = Double.NaN;
					}
				} else {
					y = Double.NaN;
				}

				points[0][j] = x;
				points[1][j] = y;
			} catch (ParseException e) {
				points[0][j] = x;
				points[1][j] = Double.NaN;
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		return points;
	}

	public double[][] getFunctionErrors(String paramX, String paramY,
			String unitX, String unitY, String transformX, String transformY,
			double minX, double maxX, double minY, double maxY)
			throws ConvertException {
		return getFunctionErrors(paramX, paramY, unitX, unitY, transformX,
				transformY, minX, maxX, minY, maxY, getStandardChoice());
	}

	public double[][] getFunctionErrors(String paramX, String paramY,
			String unitX, String unitY, String transformX, String transformY,
			double minX, double maxX, double minY, double maxY,
			Map<String, Integer> choice) throws ConvertException {
		if (function == null) {
			return null;
		}

		if (!function.startsWith(paramY + "=")
				|| !functionArguments.containsKey(paramX)) {
			return null;
		}

		double[][] points = new double[2][FUNCTION_STEPS];
		DJep parser = MathUtilities.createParser();
		Node f = null;
		List<String> paramList = new ArrayList<>(covariances.keySet());

		for (String param : functionParameters.keySet()) {
			if (functionParameters.get(param) == null) {
				return null;
			}

			parser.addConstant(param, functionParameters.get(param));
		}

		if (paramList.isEmpty()) {
			return null;
		}

		for (String param : paramList) {
			if (covariances.get(param) == null) {
				return null;
			}

			for (String param2 : paramList) {
				if (covariances.get(param).get(param2) == null) {
					return null;
				}
			}
		}

		for (String param : functionArguments.keySet()) {
			if (!param.equals(paramX)) {
				parser.addConstant(param,
						functionArguments.get(param).get(choice.get(param)));
			}
		}

		Map<String, Node> derivatives = new LinkedHashMap<>();

		parser.addVariable(paramX, 0.0);

		try {
			f = parser.parse(function.replace(paramY + "=", ""));

			for (String param : paramList) {
				derivatives.put(param, parser.differentiate(f, param));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int n = 0; n < FUNCTION_STEPS; n++) {
			double x = minX + (double) n / (double) (FUNCTION_STEPS - 1)
					* (maxX - minX);

			parser.setVarValue(
					paramX,
					convertFromUnit(paramX, inverseTransform(x, transformX),
							unitX));

			try {
				Double y = 0.0;
				boolean failed = false;

				for (String param : paramList) {
					Object obj = parser.evaluate(derivatives.get(param));

					if (!(obj instanceof Double)) {
						failed = true;
						break;
					}

					y += (Double) obj * (Double) obj
							* covariances.get(param).get(param);
				}

				for (int i = 0; i < paramList.size() - 1; i++) {
					for (int j = i + 1; j < paramList.size(); j++) {
						Object obj1 = parser.evaluate(derivatives.get(paramList
								.get(i)));
						Object obj2 = parser.evaluate(derivatives.get(paramList
								.get(j)));

						if (!(obj1 instanceof Double)
								|| !(obj2 instanceof Double)) {
							failed = true;
							break;
						}

						double cov = covariances.get(paramList.get(i)).get(
								paramList.get(j));

						y += 2.0 * (Double) obj1 * (Double) obj2 * cov;
					}
				}

				points[0][n] = x;

				if (!failed) {
					// 95% interval
					TDistribution dist = new TDistribution(degreesOfFreedom);

					y = Math.sqrt(y)
							* dist.inverseCumulativeProbability(1.0 - 0.05 / 2.0);
					y = convertToUnit(paramY, y, unitY);
					y = transform(y, transformY);

					if (y != null) {
						points[1][n] = y;
					} else {
						points[1][n] = Double.NaN;
					}
				} else {
					points[1][n] = Double.NaN;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		return points;
	}

	public double[][] getFunctionSamplePoints(String paramX, String paramY,
			String unitX, String unitY, String transformX, String transformY,
			double minX, double maxX, double minY, double maxY,
			List<String> warnings) throws ConvertException {
		return getFunctionSamplePoints(paramX, paramY, unitX, unitY,
				transformX, transformY, minX, maxX, minY, maxY,
				getStandardChoice(), warnings);
	}

	public double[][] getFunctionSamplePoints(String paramX, String paramY,
			String unitX, String unitY, String transformX, String transformY,
			double minX, double maxX, double minY, double maxY,
			Map<String, Integer> choice, List<String> warnings)
			throws ConvertException {
		if (function == null || samples.isEmpty()) {
			return null;
		}

		if (!function.startsWith(paramY + "=")
				|| !functionArguments.containsKey(paramX)) {
			return null;
		}

		DJep parser = MathUtilities.createParser();

		for (String param : functionParameters.keySet()) {
			if (functionParameters.get(param) == null) {
				return null;
			}

			parser.addConstant(param, functionParameters.get(param));
		}

		for (String param : functionArguments.keySet()) {
			if (!param.equals(paramX)) {
				parser.addConstant(param,
						functionArguments.get(param).get(choice.get(param)));
			}
		}

		parser.addVariable(paramX, 0.0);

		Node f = null;

		try {
			f = parser.parse(function.replace(paramY + "=", ""));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		double[][] points = new double[2][samples.size()];
		boolean containsValidPoint = false;

		for (int i = 0; i < samples.size(); i++) {
			Double x = samples.get(i);

			if (x == null || x < minX || x > maxX) {
				points[0][i] = Double.NaN;
				points[1][i] = Double.NaN;
				continue;
			}

			parser.setVarValue(
					paramX,
					convertFromUnit(paramX, inverseTransform(x, transformX),
							unitX));

			try {
				Object number = parser.evaluate(f);
				Double y;

				if (isInRange(number)) {
					y = (Double) number;
					y = convertToUnit(paramY, y, unitY);
					y = transform(y, transformY);

					if (y == null || y < minY || y > maxY || y.isInfinite()) {
						y = Double.NaN;
					} else {
						containsValidPoint = true;
					}
				} else {
					y = Double.NaN;
				}

				points[0][i] = x;
				points[1][i] = y;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		if (!containsValidPoint) {
			return null;
		}

		if (warnings != null) {
			for (Double d : samples) {
				if (d != null) {
					Double min = minArguments.get(AttributeUtilities.TIME);
					Double max = maxArguments.get(AttributeUtilities.TIME);
					String unit = units.get(AttributeUtilities.TIME);

					min = Categories.getTimeCategory()
							.convert(min, unit, unitX);
					max = Categories.getTimeCategory()
							.convert(max, unit, unitX);

					if (min != null && d < min) {
						warnings.add("Time exceeds the validity range of the model: "
								+ d
								+ " "
								+ unitX
								+ " < min value "
								+ min
								+ " "
								+ unitX);
					} else if (max != null && d > max) {
						warnings.add("Time exceeds the validity range of the model: "
								+ d
								+ " "
								+ unitX
								+ " > max value "
								+ max
								+ " "
								+ unitX);
					}
				}
			}
		}

		return points;
	}

	public double[][] getFunctionSamplePointsErrors(String paramX,
			String paramY, String unitX, String unitY, String transformX,
			String transformY, double minX, double maxX, double minY,
			double maxY) throws ConvertException {
		return getFunctionSamplePointsErrors(paramX, paramY, unitX, unitY,
				transformX, transformY, minX, maxX, minY, maxY,
				getStandardChoice());
	}

	public double[][] getFunctionSamplePointsErrors(String paramX,
			String paramY, String unitX, String unitY, String transformX,
			String transformY, double minX, double maxX, double minY,
			double maxY, Map<String, Integer> choice) throws ConvertException {
		if (function == null) {
			return null;
		}

		if (!function.startsWith(paramY + "=")
				|| !functionArguments.containsKey(paramX)) {
			return null;
		}

		double[][] points = new double[2][samples.size()];
		boolean containsValidPoint = false;
		DJep parser = MathUtilities.createParser();
		Node f = null;

		for (String param : functionParameters.keySet()) {
			if (functionParameters.get(param) == null
					|| covariances.get(param) == null) {
				return null;
			}

			for (String param2 : functionParameters.keySet()) {
				if (covariances.get(param).get(param2) == null) {
					return null;
				}
			}

			parser.addConstant(param, functionParameters.get(param));
		}

		for (String param : functionArguments.keySet()) {
			if (!param.equals(paramX)) {
				parser.addConstant(param,
						functionArguments.get(param).get(choice.get(param)));
			}
		}

		Map<String, Node> derivatives = new LinkedHashMap<>();

		parser.addVariable(paramX, 0.0);

		try {
			f = parser.parse(function.replace(paramY + "=", ""));

			for (String param : functionParameters.keySet()) {
				derivatives.put(param, parser.differentiate(f, param));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int n = 0; n < samples.size(); n++) {
			Double x = samples.get(n);

			if (x == null || x < minX || x > maxX) {
				points[0][n] = Double.NaN;
				points[1][n] = Double.NaN;
				continue;
			}

			parser.setVarValue(
					paramX,
					convertFromUnit(paramX, inverseTransform(x, transformX),
							unitX));

			try {
				Double y = 0.0;
				boolean failed = false;
				List<String> paramList = new ArrayList<>(
						functionParameters.keySet());

				for (String param : paramList) {
					Object obj = parser.evaluate(derivatives.get(param));

					if (!(obj instanceof Double)) {
						failed = true;
						break;
					}

					y += (Double) obj * (Double) obj
							* covariances.get(param).get(param);
				}

				for (int i = 0; i < paramList.size() - 1; i++) {
					for (int j = i + 1; j < paramList.size(); j++) {
						Object obj1 = parser.evaluate(derivatives.get(paramList
								.get(i)));
						Object obj2 = parser.evaluate(derivatives.get(paramList
								.get(j)));

						if (!(obj1 instanceof Double)
								|| !(obj2 instanceof Double)) {
							failed = true;
							break;
						}

						double cov = covariances.get(paramList.get(i)).get(
								paramList.get(j));

						y += 2.0 * (Double) obj1 * (Double) obj2 * cov;
					}
				}

				points[0][n] = x;

				if (!failed) {
					// 95% interval
					TDistribution dist = new TDistribution(degreesOfFreedom);

					y = Math.sqrt(y)
							* dist.inverseCumulativeProbability(1.0 - 0.05 / 2.0);
					y = convertToUnit(paramY, y, unitY);
					y = transform(y, transformY);

					if (y != null) {
						points[1][n] = y;
						containsValidPoint = true;
					} else {
						points[1][n] = Double.NaN;
					}
				} else {
					points[1][n] = Double.NaN;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		if (!containsValidPoint) {
			return null;
		}

		return points;
	}

	public double[][] getInverseFunctionSamplePoints(String paramX,
			String paramY, String unitX, String unitY, String transformX,
			String transformY, double minX, double maxX, double minY,
			double maxY, List<String> warnings) throws ConvertException {
		return getInverseFunctionSamplePoints(paramX, paramY, unitX, unitY,
				transformX, transformY, minX, maxX, minY, maxY,
				getStandardChoice(), warnings);
	}

	public double[][] getInverseFunctionSamplePoints(String paramX,
			String paramY, String unitX, String unitY, String transformX,
			String transformY, double minX, double maxX, double minY,
			double maxY, Map<String, Integer> choice, List<String> warnings)
			throws ConvertException {
		if (function == null || samples.isEmpty()) {
			return null;
		}

		if (!function.startsWith(paramY + "=")
				|| !functionArguments.containsKey(paramX)) {
			return null;
		}

		DJep parser = MathUtilities.createParser();

		for (String param : functionParameters.keySet()) {
			if (functionParameters.get(param) == null) {
				return null;
			}

			parser.addConstant(param, functionParameters.get(param));
		}

		for (String param : functionArguments.keySet()) {
			if (!param.equals(paramX)) {
				parser.addConstant(param,
						functionArguments.get(param).get(choice.get(param)));
			}
		}

		parser.addVariable(paramX, 0.0);

		Node f = null;

		try {
			f = parser.parse(function.replace(paramY + "=", ""));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		double[][] points = new double[2][samples.size()];
		boolean containsValidPoint = false;

		for (int i = 0; i < samples.size(); i++) {
			Double y = samples.get(i);
			Double x = getValueX(paramX, paramY, unitX, unitY, transformX,
					transformY, y, minX, maxX, null, null, parser, f);

			if (MathUtilities.isValid(x) && MathUtilities.isValid(y)) {
				containsValidPoint = true;
			}

			points[0][i] = MathUtilities.isValid(x) ? x : Double.NaN;
			points[1][i] = MathUtilities.isValid(y) ? y : Double.NaN;
		}

		if (!containsValidPoint) {
			return null;
		}

		return points;
	}

	public boolean isPlotable() {
		if (type == FUNCTION || type == FUNCTION_SAMPLE) {
			for (String param : functionParameters.keySet()) {
				if (functionParameters.get(param) == null) {
					return false;
				}
			}

			return true;
		} else {
			List<String> paramsX = new ArrayList<>(valueLists.keySet());
			List<String> paramsY = new ArrayList<>();

			if (type == DATASET || type == DATASET_STRICT) {
				paramsY = paramsX;
			} else if (type == BOTH || type == BOTH_STRICT) {
				if (functionValue != null) {
					paramsY = Arrays.asList(functionValue);
				}
			}

			for (String paramX : paramsX) {
				for (String paramY : paramsY) {
					if (isPlotable(paramX, paramY)) {
						return true;
					}
				}
			}

			return false;
		}
	}

	public List<Map<String, Integer>> getAllChoices() {
		List<Map<String, Integer>> choices = new ArrayList<>();
		List<String> argList = new ArrayList<>(functionArguments.keySet());
		List<Integer> choice = new ArrayList<>(Collections.nCopies(
				argList.size(), 0));
		boolean done = false;

		while (!done) {
			Map<String, Integer> map = new LinkedHashMap<>();

			for (int i = 0; i < argList.size(); i++) {
				map.put(argList.get(i), choice.get(i));
			}

			choices.add(map);

			for (int i = 0;; i++) {
				if (i >= argList.size()) {
					done = true;
					break;
				}

				choice.set(i, choice.get(i) + 1);

				if (choice.get(i) >= functionArguments.get(argList.get(i))
						.size()) {
					choice.set(i, 0);
				} else {
					break;
				}
			}
		}

		return choices;
	}

	public int getNumberOfCombinations() {
		int nMax = 0;

		if (functionArguments.size() == 1) {
			String arg = new ArrayList<>(functionArguments.keySet()).get(0);

			if (valueLists.containsKey(arg) && valueLists.get(arg).size() != 0) {
				return 1;
			}
		}

		for (String arg0 : functionArguments.keySet()) {
			int n = 1;
			boolean valueFound = false;

			for (String arg : functionArguments.keySet()) {
				if (!arg.equals(arg0) && valueLists.containsKey(arg)) {
					Set<Double> set = new LinkedHashSet<>(valueLists.get(arg));

					n *= set.size();
					valueFound = true;
				}
			}

			if (valueFound) {
				nMax = Math.max(nMax, n);
			}
		}

		return nMax;
	}

	public Double convertToUnit(String param, Double value, String unit)
			throws ConvertException {
		String currentUnit = units.get(param);
		Category category = Categories.getCategoryByUnit(currentUnit);

		return category.convert(value, currentUnit, unit);
	}

	public Double convertFromUnit(String param, Double value, String unit)
			throws ConvertException {
		String newUnit = units.get(param);
		Category category = Categories.getCategoryByUnit(newUnit);

		return category.convert(value, unit, newUnit);
	}

	private Map<String, Integer> getStandardChoice() {
		if (functionArguments == null) {
			return null;
		}

		Map<String, Integer> choice = new LinkedHashMap<>();

		for (String arg : functionArguments.keySet()) {
			choice.put(arg, 0);
		}

		return choice;
	}

	private boolean isPlotable(String paramX, String paramY) {
		boolean dataSetPlotable = false;
		boolean functionPlotable = false;
		List<Double> xs = valueLists.get(paramX);
		List<Double> ys = valueLists.get(paramY);

		if (xs != null && ys != null) {
			for (int i = 0; i < xs.size(); i++) {
				if (isValidValue(xs.get(i)) && isValidValue(ys.get(i))) {
					dataSetPlotable = true;
					break;
				}
			}
		}

		if (function != null && functionValue.equals(paramY)
				&& functionArguments.containsKey(paramX)) {
			boolean notValid = false;

			for (Double value : functionParameters.values()) {
				if (!isValidValue(value)) {
					notValid = true;
					break;
				}
			}

			if (!notValid) {
				functionPlotable = true;
			}
		}

		if (type == DATASET) {
			return dataSetPlotable;
		} else if (type == FUNCTION || type == FUNCTION_SAMPLE) {
			return functionPlotable;
		} else if (type == BOTH || type == BOTH_STRICT) {
			return dataSetPlotable && functionPlotable;
		}

		return false;
	}

	private boolean isValidValue(Double value) {
		return value != null && !value.isNaN() && !value.isInfinite();
	}

	private Double getValueX(String paramX, String paramY, String unitX,
			String unitY, String transformX, String transformY, Double y,
			double minX, double maxX, Double minY, Double maxY, DJep parser,
			Node f) {
		if (y == null) {
			return null;
		}

		if (minY == null) {
			parser.setVarValue(
					paramX,
					convertFromUnit(paramX, inverseTransform(minX, transformX),
							unitX));

			try {
				minY = transform(
						convertToUnit(paramY, (Double) parser.evaluate(f),
								unitY), transformY);
			} catch (ParseException e) {
			} catch (ClassCastException e) {
			}
		}

		if (maxY == null) {
			parser.setVarValue(
					paramX,
					convertFromUnit(paramX, inverseTransform(maxX, transformX),
							unitX));

			try {
				maxY = transform(
						convertToUnit(paramY, (Double) parser.evaluate(f),
								unitY), transformY);
			} catch (ParseException e) {
			} catch (ClassCastException e) {
			}
		}

		if (!MathUtilities.isValid(minY) || !MathUtilities.isValid(maxY)) {
			return null;
		}

		if (Math.abs(minX - maxX) < EPSILON) {
			return minX;
		}

		double midX = (minX + maxX) / 2;
		Double midY = null;

		parser.setVarValue(
				paramX,
				convertFromUnit(paramX, inverseTransform(midX, transformX),
						unitX));

		try {
			midY = transform(
					convertToUnit(paramY, (Double) parser.evaluate(f), unitY),
					transformY);
		} catch (ParseException e) {
		} catch (ClassCastException e) {
		}

		if ((minY <= y && midY >= y) || (minY >= y && midY <= y)) {
			return getValueX(paramX, paramY, unitX, unitY, transformX,
					transformY, y, minX, midX, minY, midY, parser, f);
		}

		if ((midY <= y && maxY >= y) || (midY >= y && maxY <= y)) {
			return getValueX(paramX, paramY, unitX, unitY, transformX,
					transformY, y, midX, maxX, midY, maxY, parser, f);
		}

		return null;
	}

	private boolean isInRange(Object number) {
		if (!MathUtilities.isValid(number)) {
			return false;
		}

		double value = (Double) number;

		if (minValue != null && value < minValue) {
			return false;
		}

		if (maxValue != null && value > maxValue) {
			return false;
		}

		return true;
	}

	public static Double transform(Double value, String transform) {
		if (value == null) {
			return null;
		} else if (transform.equals(ChartConstants.NO_TRANSFORM)) {
			return value;
		} else if (transform.equals(ChartConstants.SQRT_TRANSFORM)) {
			return Math.sqrt(value);
		} else if (transform.equals(ChartConstants.LOG_TRANSFORM)) {
			return Math.log(value);
		} else if (transform.equals(ChartConstants.LOG10_TRANSFORM)) {
			return Math.log10(value);
		} else if (transform.equals(ChartConstants.EXP_TRANSFORM)) {
			return Math.exp(value);
		} else if (transform.equals(ChartConstants.EXP10_TRANSFORM)) {
			return Math.pow(10.0, value);
		} else if (transform.equals(ChartConstants.DIVX_TRANSFORM)) {
			return 1 / value;
		} else if (transform.equals(ChartConstants.DIVX2_TRANSFORM)) {
			return 1 / value / value;
		}

		return null;
	}

	public static Double inverseTransform(Double value, String transform) {
		if (value == null) {
			return null;
		} else if (transform.equals(ChartConstants.NO_TRANSFORM)) {
			return value;
		} else if (transform.equals(ChartConstants.SQRT_TRANSFORM)) {
			return value * value;
		} else if (transform.equals(ChartConstants.LOG_TRANSFORM)) {
			return Math.exp(value);
		} else if (transform.equals(ChartConstants.LOG10_TRANSFORM)) {
			return Math.pow(10.0, value);
		} else if (transform.equals(ChartConstants.EXP_TRANSFORM)) {
			return Math.log(value);
		} else if (transform.equals(ChartConstants.EXP10_TRANSFORM)) {
			return Math.log10(value);
		} else if (transform.equals(ChartConstants.DIVX_TRANSFORM)) {
			return 1 / value;
		} else if (transform.equals(ChartConstants.DIVX2_TRANSFORM)) {
			return 1 / Math.sqrt(value);
		}

		return null;
	}
}