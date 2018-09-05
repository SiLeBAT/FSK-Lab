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
package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import de.bund.bfr.knime.pmm.common.chart.ChartConstants;

public class AttributeUtilities {

	public static final String DATAID = "DataID";

	public static final String TIME = "Time";
	public static final String CONCENTRATION = "Value";
	public static final String ATT_TEMPERATURE = "Temperature";
	public static final String ATT_PH = "pH";
	public static final String ATT_AW = "aw";
	public static final String ATT_PRESSURE = "pressure";
	public static final int ATT_TEMPERATURE_ID = -1;
	public static final int ATT_PH_ID = -2;
	public static final int ATT_AW_ID = -3;
	public static final int ATT_PRESSURE_ID = -4;

	public static final String AGENT_DETAILS = TimeSeriesSchema.ATT_AGENT
			+ " Details";
	public static final String MATRIX_DETAILS = TimeSeriesSchema.ATT_MATRIX
			+ " Details";

	private AttributeUtilities() {
	}

	public static String getName(String attr) {
		if (attr.equals(ATT_AW)) {
			return "Water Activity";
		} else if (attr.equals(TimeSeriesSchema.ATT_LITMD)) {
			return "References";
		} else {
			return attr;
		}
	}

	public static String getName(String attr, String transform) {
		if (transform.equals(ChartConstants.NO_TRANSFORM)) {
			return getName(attr);
		} else {
			return transform + "(" + getName(attr) + ")";
		}
	}

	public static String getNameWithUnit(String attr, String unit) {
		if (unit != null) {
			return getName(attr) + " [" + unit + "]";
		} else {
			return getName(attr);
		}
	}

	public static String getNameWithUnit(String attr, String unit,
			String transform) {
		if (transform == null || transform.equals(ChartConstants.NO_TRANSFORM)) {
			return getNameWithUnit(attr, unit);
		} else if (unit != null) {
			return getName(attr, transform) + " [" + transform + "(" + unit
					+ ")]";
		} else {
			return getName(attr, transform);
		}
	}

}
