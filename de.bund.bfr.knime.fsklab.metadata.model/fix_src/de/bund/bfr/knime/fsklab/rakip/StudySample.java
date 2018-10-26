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

public class StudySample {

  public String sample = "";
  public String collectionProtocol = "";
  public String samplingStrategy = "";
  public String samplingProgramType = "";
  public String samplingMethod = "";
  public String samplingPlan = "";
  public String samplingWeight = "";
  public String samplingSize = "";
  public String lotSizeUnit = "";

  @Override
  public int hashCode() {
    return Objects.hash(collectionProtocol, lotSizeUnit, sample, samplingMethod, samplingPlan,
        samplingPoint, samplingProgramType, samplingSize, samplingStrategy, samplingWeight);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    StudySample other = (StudySample) obj;
    return Objects.equals(collectionProtocol, other.collectionProtocol)
        && Objects.equals(lotSizeUnit, other.lotSizeUnit) && Objects.equals(sample, other.sample)
        && Objects.equals(samplingMethod, other.samplingMethod)
        && Objects.equals(samplingPlan, other.samplingPlan)
        && Objects.equals(samplingPoint, other.samplingPoint)
        && Objects.equals(samplingProgramType, other.samplingProgramType)
        && Objects.equals(samplingSize, other.samplingSize)
        && Objects.equals(samplingStrategy, other.samplingStrategy)
        && Objects.equals(samplingWeight, other.samplingWeight);
  }

  public String samplingPoint = "";
}
