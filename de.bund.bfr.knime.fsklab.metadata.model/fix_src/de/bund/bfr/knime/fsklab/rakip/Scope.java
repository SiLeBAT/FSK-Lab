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

public class Scope {

  public Product product = new Product();
  public Hazard hazard = new Hazard();
  public PopulationGroup populationGroup = new PopulationGroup();

  /** General comment on the model. */
  public String generalComment = "";

  /** Temporal information on which the model applies. */
  public String temporalInformation = "";

  /** Information on which the model applies. */
  public final List<String> region = new ArrayList<>();

  /** Countries on which the model applies. */
  public final List<String> country = new ArrayList<>();

  @Override
  public int hashCode() {
    return Objects.hash(country, generalComment, hazard, populationGroup, product, region,
        temporalInformation);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Scope other = (Scope) obj;
    return Objects.equals(country, other.country)
        && Objects.equals(generalComment, other.generalComment)
        && Objects.equals(hazard, other.hazard)
        && Objects.equals(populationGroup, other.populationGroup)
        && Objects.equals(product, other.product) && Objects.equals(region, other.region)
        && Objects.equals(temporalInformation, other.temporalInformation);
  }
}
