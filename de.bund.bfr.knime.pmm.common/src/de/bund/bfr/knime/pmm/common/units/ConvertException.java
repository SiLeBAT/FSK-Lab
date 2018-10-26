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

public class ConvertException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String fromUnit;
	private String toUnit;

	public ConvertException(String fromUnit, String toUnit) {
		this.fromUnit = fromUnit;
		this.toUnit = toUnit;
	}

	public String getFromUnit() {
		return fromUnit;
	}

	public String getToUnit() {
		return toUnit;
	}

	@Override
	public String getMessage() {
		return "Cannot convert from " + fromUnit + " to " + toUnit;
	}
}
