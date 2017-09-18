/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import de.bund.bfr.knime.fsklab.nodes.Variable.DataType;

public class Util {

	/**
	 * Returns the {@link FskMetaData.DataType} of a value.
	 * 
	 * <ul>
	 * <li>{@code DataType#array} for Matlab like arrays, c(0, 1, 2, ...)
	 * <li>{@code DataType#numeric} for real numbers.
	 * <li>{@code DataType#integer} for integer numbers.
	 * <li>{@code DataType#character} for any other variable. E.g. "zero",
	 * "eins", "dos".
	 * </ul>
	 */
	public static DataType getValueType(final String value) {
		if (value.startsWith("c(") && value.endsWith(")")) {
			return DataType.array;
		} else {
			try {
				Integer.parseInt(value);
				return DataType.integer;
			} catch (NumberFormatException e1) {
				try {
					Double.parseDouble(value);
					return DataType.numeric;
				} catch (NumberFormatException e2) {
					return DataType.character;
				}
			}
		}
	}


}
