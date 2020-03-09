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

import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;

public class ValueAndUnit {

	private Double value;
	private String unit;
	private String object;
	private Category category;
	
	public Category getCategory() {
		return category;
	}

	public Double getValue() {
		return value;
	}

	public String getUnit() {
		return unit;
	}

	public String getObject() {
		return object;
	}

	public String getObjectUnit() {
		if (unit != null && object != null && unit.indexOf("count") >= 0) return unit.replace("count", object);
		return unit;
	}

	public ValueAndUnit(final Double value, final String unit, final String object) {
		this.value = value;
		this.unit = unit;
		this.object = object;
		category = Categories.getCategoryByUnit(unit);
	}
}
