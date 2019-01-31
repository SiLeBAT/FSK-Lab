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
package de.bund.bfr.knime.pmm.common.units;

import java.util.Arrays;
import java.util.List;

public class NoCategory implements Category {

	public static final String NO_UNIT = "No Unit";

	@Override
	public String getName() {
		return Categories.NO_CATEGORY;
	}

	@Override
	public List<String> getAllUnits() {
		return Arrays.asList(NO_UNIT);
	}

	@Override
	public String getStandardUnit() {
		return NO_UNIT;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		return value;
	}

	@Override
	public String getConversionString(String var, String fromUnit, String toUnit) throws ConvertException {
		return var;
	}

	@Override
	public String getSBML(String unit) {
		return null;
	}
}
