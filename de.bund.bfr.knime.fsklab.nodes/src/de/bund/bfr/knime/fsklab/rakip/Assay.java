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

import java.util.Objects;

public class Assay {

  public String name = "";
  public String description = "";
  public String moisturePercentage = "";
  public String fatPercentage = "";
  public String detectionLimit = "";
  public String quantificationLimit = "";
  public String leftCensoredData = "";
  public String contaminationRange = "";
  public String uncertaintyValue = "";

  @Override
  public int hashCode() {
    return Objects.hash(name, description, moisturePercentage, detectionLimit, quantificationLimit,
        leftCensoredData, contaminationRange, uncertaintyValue);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Assay other = (Assay) obj;
    return Objects.equals(name, other.name) && Objects.equals(description, other.description)
        && Objects.equals(moisturePercentage, other.moisturePercentage)
        && Objects.equals(detectionLimit, other.detectionLimit)
        && Objects.equals(quantificationLimit, other.quantificationLimit)
        && Objects.equals(leftCensoredData, other.leftCensoredData)
        && Objects.equals(contaminationRange, other.contaminationRange)
        && Objects.equals(uncertaintyValue, other.uncertaintyValue);
  }
}
