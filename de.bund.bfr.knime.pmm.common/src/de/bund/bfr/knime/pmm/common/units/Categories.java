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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Categories {

	public static final String NO_CATEGORY = "No Category";

	public static List<String> getAllCategories() {
		return new ArrayList<>(CategoryReader.getInstance().getMap().keySet());
	}

	public static Category getCategory(String id) {
		if (id == null) {
			return new NoCategory();
		}

		Category category = CategoryReader.getInstance().getMap().get(id);

		if (category == null) {
			return new NoCategory();
		}

		return category;
	}

	public static Category getCategoryByUnit(String unit) {
		Category category = new NoCategory();

		for (Object o : getAllCategories()) {
			Category cat;

			if (o instanceof Category) {
				cat = (Category) o;
			} else if (o instanceof String) {
				cat = Categories.getCategory((String) o);
			} else {
				continue;
			}

			if (cat.getAllUnits().contains(unit)) {
				category = cat;
				break;
			}
		}

		return category;
	}

	public static List<String> getUnitsFromCategories(List<?> categories) {
		List<String> units = new ArrayList<>();

		if (categories != null) {
			for (Object o : categories) {
				if (o instanceof Category) {
					units.addAll(((Category) o).getAllUnits());
				} else if (o instanceof String) {
					units.addAll(Categories.getCategory((String) o).getAllUnits());
				}
			}
		}

		return units;
	}

	public static String getTime() {
		return "Time";
	}

	public static Category getTimeCategory() {
		return getCategory(getTime());
	}

	public static List<String> getConcentrations() {
		return Arrays.asList("Number Content (count/mass)", "Number Concentration (count/vol)",
				"Number Aeric (number/area)", "Number", "Number Concentration Difference", "Number Content Difference",
				"Number Difference", "Number Fraction", "Arbitrary Fraction", "Energy Content", "Energy Content Rate",
				"Mass", "Mass Concentration", "Mass Ratio", "Mole Content", "True/False Value", "Volume Concentration",
				"Volume Ratio");
	}

	public static List<Category> getConcentrationCategories() {
		return getConcentrations().stream().map(Categories::getCategory).collect(Collectors.toList());
	}

	public static Category getTempCategory() {
		return getCategory("Temperature");
	}

	public static Category getPhCategory() {
		return getCategory("Dimensionless quantity");
	}

	public static Category getAwCategory() {
		return getCategory("Dimensionless quantity");
	}

	public static String getPhUnit() {
		return "[pH]";
	}

	public static String getAwUnit() {
		return "[aw]";
	}
}
