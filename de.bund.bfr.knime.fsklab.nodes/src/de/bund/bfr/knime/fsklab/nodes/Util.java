package de.bund.bfr.knime.fsklab.nodes;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;

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
