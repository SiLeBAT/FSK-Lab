/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DietaryAssessmentMethod {

  public String collectionTool = "";
  public int numberOfNonConsecutiveOneDay = 0;
  public String softwareTool = "";

  public final List<String> numberOfFoodItems = new ArrayList<>();
  public final List<String> recordTypes = new ArrayList<>();
  public final List<String> foodDescriptors = new ArrayList<>();

  @Override
  public int hashCode() {
    return Objects.hash(collectionTool, foodDescriptors, numberOfFoodItems,
        numberOfNonConsecutiveOneDay, recordTypes, softwareTool);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    DietaryAssessmentMethod other = (DietaryAssessmentMethod) obj;
    return Objects.equals(collectionTool, other.collectionTool)
        && Objects.equals(foodDescriptors, other.foodDescriptors)
        && Objects.equals(numberOfFoodItems, other.numberOfFoodItems)
        && numberOfNonConsecutiveOneDay == other.numberOfNonConsecutiveOneDay
        && Objects.equals(recordTypes, other.recordTypes)
        && Objects.equals(softwareTool, other.softwareTool);
  }
}
