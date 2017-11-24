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

public class DataBackground {

  public Study study = new Study();
  public StudySample studySample = new StudySample();
  public DietaryAssessmentMethod dietaryAssessmentMethod = new DietaryAssessmentMethod();
  public final List<String> laboratoryAccreditation = new ArrayList<>();
  public Assay assay = new Assay();

  @Override
  public int hashCode() {
    return Objects.hash(assay, dietaryAssessmentMethod, laboratoryAccreditation, study,
        studySample);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    DataBackground other = (DataBackground) obj;
    return Objects.equals(assay, other.assay)
        && Objects.equals(dietaryAssessmentMethod, other.dietaryAssessmentMethod)
        && Objects.equals(laboratoryAccreditation, other.laboratoryAccreditation)
        && Objects.equals(study, other.study) && Objects.equals(studySample, other.studySample);
  }
}
