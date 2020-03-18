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
package de.bund.bfr.knime.util;

public final class FormulaEvaluator {

	/**
	 * Returns true if given formula is a number (a constant)
	 * @param formula the formula
	 * @return true when given formula is a number
	 */
	public static boolean isNumber(final String formula) {
		if (null == formula) {
			return false;
		}
		try {
			Double.valueOf(formula);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Returns the value when isNumer is true. Throws otherwise an exception
	 * @param formula the formula
	 * @return the value when the formula is a constant
	 * @throws NumberFormatException when formula cannot be converted to a number
	 */
	public static double getDouble(final String formula) throws NumberFormatException {
		return Double.valueOf(formula);
	}

	public static double getSeconds(String unit) {
		if (unit == null) return 1;
		if (unit.equalsIgnoreCase("min")) return 60;
    	if (unit.equalsIgnoreCase("h")) return 60*60;
    	if (unit.equalsIgnoreCase("d")) return 24*60*60;
    	if (unit.equalsIgnoreCase("m")) return 24*60*60*30;
    	if (unit.equalsIgnoreCase("y")) return 24*60*60*365;
    	return 1;
    }

	public static double getGrams(String unit) {
		if (unit == null) return 1;
    	else if (unit.equalsIgnoreCase("kg")) return 1000;
    	else if (unit.equalsIgnoreCase("t")) return 1000000;
    	else if (unit.equalsIgnoreCase("cfu")) return Math.pow(10, -12); // Nauta
    	else return 1;
    }
}
