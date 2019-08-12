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

public class PopulationGroup {

  public String populationName = "";

  public String targetPopulation = "";

  public final List<String> populationSpan = new ArrayList<>();

  public final List<String> populationDescription = new ArrayList<>();

  public final List<String> populationAge = new ArrayList<>();

  public String populationGender = "";

  public final List<String> bmi = new ArrayList<>();

  public final List<String> specialDietGroups = new ArrayList<>();

  public final List<String> patternConsumption = new ArrayList<>();

  public final List<String> region = new ArrayList<>();

  public final List<String> country = new ArrayList<>();

  public final List<String> populationRiskFactor = new ArrayList<>();

  public final List<String> season = new ArrayList<>();

  @Override
  public int hashCode() {
    return Objects.hash(populationName, targetPopulation, populationSpan, populationDescription,
        populationAge, populationGender, bmi, specialDietGroups, patternConsumption, region,
        country, populationRiskFactor, season);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    PopulationGroup other = (PopulationGroup) obj;

    return Objects.equals(populationName, other.populationName)
        && Objects.equals(targetPopulation, other.targetPopulation)
        && Objects.equals(populationSpan, other.populationSpan)
        && Objects.equals(populationDescription, other.populationDescription)
        && Objects.equals(populationAge, other.populationAge)
        && Objects.equals(populationGender, other.populationGender)
        && Objects.equals(bmi, other.bmi)
        && Objects.equals(specialDietGroups, other.specialDietGroups)
        && Objects.equals(patternConsumption, other.patternConsumption)
        && Objects.equals(region, other.region) && Objects.equals(country, other.country)
        && Objects.equals(populationRiskFactor, other.populationRiskFactor)
        && Objects.equals(season, other.season);
  }
}
