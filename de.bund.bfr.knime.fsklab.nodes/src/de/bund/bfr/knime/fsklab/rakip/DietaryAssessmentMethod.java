/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;

public class DietaryAssessmentMethod {

	public String collectionTool = "";
	public int numberOfNonConsecutiveOneDay = 0;
	public String softwareTool = "";

	public final List<String> numberOfFoodItems = new ArrayList<>();
	public final List<String> recordTypes = new ArrayList<>();
	public final List<String> foodDescriptors = new ArrayList<>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collectionTool == null) ? 0 : collectionTool.hashCode());
		result = prime * result + ((foodDescriptors == null) ? 0 : foodDescriptors.hashCode());
		result = prime * result + ((numberOfFoodItems == null) ? 0 : numberOfFoodItems.hashCode());
		result = prime * result + numberOfNonConsecutiveOneDay;
		result = prime * result + ((recordTypes == null) ? 0 : recordTypes.hashCode());
		result = prime * result + ((softwareTool == null) ? 0 : softwareTool.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DietaryAssessmentMethod other = (DietaryAssessmentMethod) obj;
		if (collectionTool == null) {
			if (other.collectionTool != null)
				return false;
		} else if (!collectionTool.equals(other.collectionTool))
			return false;
		if (foodDescriptors == null) {
			if (other.foodDescriptors != null)
				return false;
		} else if (!foodDescriptors.equals(other.foodDescriptors))
			return false;
		if (numberOfFoodItems == null) {
			if (other.numberOfFoodItems != null)
				return false;
		} else if (!numberOfFoodItems.equals(other.numberOfFoodItems))
			return false;
		if (numberOfNonConsecutiveOneDay != other.numberOfNonConsecutiveOneDay)
			return false;
		if (recordTypes == null) {
			if (other.recordTypes != null)
				return false;
		} else if (!recordTypes.equals(other.recordTypes))
			return false;
		if (softwareTool == null) {
			if (other.softwareTool != null)
				return false;
		} else if (!softwareTool.equals(other.softwareTool))
			return false;
		return true;
	}
}
