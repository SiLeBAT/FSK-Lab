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
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.distribution.TDistribution;
import org.lsmp.djep.djep.DJep;
import org.lsmp.djep.djep.DiffRulesI;
import org.lsmp.djep.djep.diffRules.MacroDiffRules;
import org.lsmp.djep.xjep.MacroFunction;
import org.nfunk.jep.ASTConstant;
import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.ASTVarNode;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

public class MathUtilities {

	public static double EPSILON = 0.00001;

	private static Random random = null;

	private MathUtilities() {
	}

	public static String nodeToString(Node n) throws Exception {
		if (n instanceof ASTFunNode) {
			String s = n.toString() + "(";

			for (int i = 0; i < n.jjtGetNumChildren(); i++) {
				if (i != 0) {
					s += ",";
				}

				s += nodeToString(n.jjtGetChild(i));
			}

			return s + ")";
		} else if (n instanceof ASTConstant) {
			return n.toString();
		} else if (n instanceof ASTVarNode) {
			return n.toString();
		} else {
			throw new Exception("Unknown Node");
		}
	}

	public static void removeNullValues(List<Double> targetValues, List<List<Double>> argumentValues) {
		for (int i = 0; i < targetValues.size(); i++) {
			boolean remove = false;

			if (targetValues.get(i) == null) {
				remove = true;
				continue;
			}

			if (!remove) {
				for (int j = 0; j < argumentValues.size(); j++) {
					if (argumentValues.get(j).get(i) == null) {
						remove = true;
						break;
					}
				}
			}

			if (remove) {
				targetValues.remove(i);

				for (int j = 0; j < argumentValues.size(); j++) {
					argumentValues.get(j).remove(i);
				}

				i--;
			}
		}
	}

	public static Random getRandomGenerator() {
		if (random == null) {
			random = new Random();
		}

		return random;
	}

	public static int getRandomNegativeInt() {
		if (random == null)
			random = new Random();
		int value = random.nextInt();
		if (value > 0)
			value = -value;
		return value;
	}

	public static double computeSum(List<Double> values) {
		double sum = 0.0;

		for (double v : values) {
			sum += v;
		}

		return sum;
	}

	public static DJep createParser() {
		DJep parser = new DJep();

		parser.setAllowAssignment(true);
		parser.setAllowUndeclared(true);
		parser.setImplicitMul(true);
		parser.addStandardFunctions();
		parser.addStandardDiffRules();
		parser.removeVariable("x");

		try {
			parser.addFunction("log10", new MacroFunction("log10", 1, "ln(x)/ln(10)", parser));
			parser.addDiffRule(new MacroDiffRules(parser, "log10", "1/(x*ln(10))"));

			parser.addDiffRule(new ZeroDiffRule("<"));
			parser.addDiffRule(new ZeroDiffRule(">"));
			parser.addDiffRule(new ZeroDiffRule("<="));
			parser.addDiffRule(new ZeroDiffRule(">="));
			parser.addDiffRule(new ZeroDiffRule("&&"));
			parser.addDiffRule(new ZeroDiffRule("||"));
			parser.addDiffRule(new ZeroDiffRule("=="));
			parser.addDiffRule(new ZeroDiffRule("!="));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return parser;
	}

	public static String replaceVariable(String formula, String var, String newVar) {
		if (var.equals(newVar)) {
			return formula;
		}

		String newFormular = " " + formula + " ";

		for (int i = 1; i < newFormular.length() - var.length(); i++) {
			boolean matches = newFormular.substring(i, i + var.length()).equals(var);
			boolean start = !isVariableCharacter(newFormular.charAt(i - 1));
			boolean end = !isVariableCharacter(newFormular.charAt(i + var.length()));

			if (matches && start && end) {
				String prefix = newFormular.substring(0, i);
				String postfix = newFormular.substring(i + var.length());

				newFormular = prefix + newVar + postfix;
				i = prefix.length() + newVar.length();
			}
		}

		return newFormular.replace(" ", "");
	}

	public static boolean isFunctionDefinedFor(String formula, List<String> parameters, List<Double> parameterValues,
			String variable, double minValue, double maxValue, int steps) {
		DJep parser = createParser();
		Node function = null;

		parser.addVariable(variable, 0.0);

		for (int i = 0; i < parameters.size(); i++) {
			parser.addConstant(parameters.get(i), parameterValues.get(i));
		}

		try {
			function = parser.parse(formula.substring(formula.indexOf("=") + 1));

			for (int i = 0; i < steps; i++) {
				double value = minValue + (double) i / (double) (steps - 1) * (maxValue - minValue);

				parser.setVarValue(variable, value);

				if (!(parser.evaluate(function) instanceof Double)) {
					return false;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static List<Double> evaluateFunction(String formula, List<String> parameters, List<Double> parameterValues,
			List<String> variables, List<List<Double>> variableValues) {
		List<Double> values = new ArrayList<>();
		DJep parser = createParser();

		for (int i = 0; i < parameters.size(); i++) {
			parser.addConstant(parameters.get(i), parameterValues.get(i));
		}

		for (int i = 0; i < variables.size(); i++) {
			parser.addVariable(variables.get(i), 0.0);
		}

		try {
			Node function = parser.parse(formula.substring(formula.indexOf("=") + 1));

			for (int i = 0; i < variableValues.get(0).size(); i++) {
				for (int j = 0; j < variables.size(); j++) {
					parser.setVarValue(variables.get(j), variableValues.get(j).get(i));
				}

				values.add((Double) parser.evaluate(function));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return values;
	}

	private static class ZeroDiffRule implements DiffRulesI {

		private String name;

		public ZeroDiffRule(String name) {
			this.name = name;
		}

		@Override
		public Node differentiate(ASTFunNode node, String var, Node[] children, Node[] dchildren, DJep djep)
				throws ParseException {
			return djep.getNodeFactory().buildConstantNode(0.0);
		}

		@Override
		public String getName() {
			return name;
		}

	}

	public static Double getMSE(Double rmse) {
		if (rmse == null) {
			return null;
		}

		return rmse * rmse;
	}

	public static Double getRMSE(double sse, double numParam, double numSample) {
		if (numParam >= numSample) {
			return null;
		}

		return Math.sqrt(sse / (numSample - numParam));
	}

	public static Double getRMSE(double sse, double numSample) {
		return Math.sqrt(sse / numSample);
	}

	public static Double getRSquared(double sse, List<Double> targetValues) {
		double targetMean = MathUtilities.computeSum(targetValues) / targetValues.size();
		double targetTotalSumOfSquares = 0.0;

		for (int i = 0; i < targetValues.size(); i++) {
			targetTotalSumOfSquares += Math.pow(targetValues.get(i) - targetMean, 2.0);
		}

		double rSquared = 1 - sse / targetTotalSumOfSquares;

		// rSquare < 0 möglich, siehe hier:
		// http://mars.wiwi.hu-berlin.de/mediawiki/sk/index.php/Bestimmtheitsmass
		return Math.max(rSquared, 0.0);
	}

	public static Double akaikeCriterion(final int numParam, final int numSample, final double sse) {
		if (numSample <= numParam + 2) {
			return null;
		}

		return numSample * Math.log(sse / numSample) + 2 * (numParam + 1)
				+ 2 * (numParam + 1) * (numParam + 2) / (numSample - numParam - 2);
	}

	public static double getPValue(double tValue, int degreesOfFreedom) {
		TDistribution dist = new TDistribution(degreesOfFreedom);

		return 1.0 - dist.probability(-Math.abs(tValue), Math.abs(tValue));
	}

	public static boolean isVariableCharacter(char ch) {
		return Character.isLetterOrDigit(ch) || ch == '_' || ch == '$';
	}

	public static int generateID(int seed) {
		Random rand = new Random(seed);
		int id = rand.nextInt();

		if (id > 0) {
			id = -id;
		}

		return id;
	}

	public static String getBoundaryCondition(String formula) {
		Pattern p = Pattern.compile("\\*\\(\\(\\(\\(\\(.+\\)\\)\\)\\)\\)$");
		Matcher m = p.matcher(formula);

		while (m.find()) {
			String s = m.group();

			s = s.substring(6, s.length() - 5);

			if (countOccurences(s, '(') == countOccurences(s, ')')) {
				return s;
			}
		}

		return null;
	}

	public static String getAllButBoundaryCondition(String formula) {
		String cond = getBoundaryCondition(formula);

		if (cond != null) {
			String f = formula.replace("*(((((" + cond + ")))))", "");

			int i = f.indexOf("=");

			if (f.charAt(i + 1) == '(' && f.endsWith(")")) {
				String withoutBrackets = f.substring(i + 2, f.length() - 1);

				try {
					createParser().parse(withoutBrackets);
					f = f.substring(0, i + 1) + withoutBrackets;
				} catch (ParseException e) {
					// Do not remove brackets when expression without brackets
					// cannot be parsed
				}
			}

			return f;
		} else {
			return formula;
		}
	}

	public static String getFormula(String formula, String boundaryCondition) {
		if (boundaryCondition != null && !boundaryCondition.isEmpty()) {
			int i = formula.indexOf("=");

			return formula.substring(0, i + 1) + "(" + formula.substring(i + 1) + ")*(((((" + boundaryCondition
					+ ")))))";
		} else {
			return formula;
		}
	}

	public static boolean isValid(Object value) {
		return value instanceof Double && !((Double) value).isNaN() && !((Double) value).isInfinite();
	}

	private static int countOccurences(String s, char c) {
		int n = 0;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				n++;
			}
		}

		return n;
	}

}
