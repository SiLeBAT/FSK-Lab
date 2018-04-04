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

public class Parameter {

  public static enum DataTypes {
    Integer,
    Double,
    Number,
    Date,
    File,
    Boolean,
    VectorOfNumbers,
    VectorOfStrings,
    MatrixOfNumbers,
    MatrixOfStrings,
    Other,
    Object
  }

  public String id = "";

  public String classification = "";

  public String name = "";

  public String description = "";

  public String type = "";

  public String unit = "";

  public String unitCategory = "";

  public DataTypes dataType;

  public String source = "";

  public String subject = "";

  public String distribution = "";

  public String value = "";

  public String minValue = "";

  public String maxValue = "";

  public String reference = "";

  public String variabilitySubject = "";

  public String rangeOfApplicability = "";

  public final List<String> modelApplicability = new ArrayList<>();

  public Double error = .0;

  public Parameter() {}

  public Parameter(Parameter another) {
    this.id = another.id; // you can access
    this.classification = another.classification; // you can access
    this.name = another.name; // you can access
    this.description = another.description; // you can access
    this.unit = another.unit; // you can access
    this.unitCategory = another.unitCategory; // you can access
    this.dataType = another.dataType; // you can access
    this.source = another.source; // you can access
    this.subject = another.subject; // you can access
    this.distribution = another.distribution; // you can access
    this.value = another.value; // you can access
    this.reference = another.reference; // you can access
    this.variabilitySubject = another.variabilitySubject; // you can access
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, type, unit, unitCategory, dataType, source, subject,
        distribution, value, reference, variabilitySubject, rangeOfApplicability,
        modelApplicability, error);
  }

  public String getDescription() {
    String description = "<html>";
    description += "name : " + name + "<br>";
    description += "classification : " + classification + "<br>";
    description += "description : " + description + "<br>";
    description += "unitCategory : " + unitCategory + "<br>";
    description += "dataType : " + dataType + "<br>";
    description += "source : " + source + "<br>";
    description += "subject : " + subject + "<br>";
    description += "distribution : " + distribution + "<br>";
    description += "reference : " + reference + "<br>";
    description += "variabilitySubject : " + variabilitySubject + "<br>";
    description += "</html>";
    return description;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Parameter other = (Parameter) obj;
    return Objects.equals(id, other.id) && Objects.equals(name, other.name)
        && Objects.equals(description, other.description) && Objects.equals(type, other.type)
        && Objects.equals(unit, other.unit) && Objects.equals(unitCategory, other.unitCategory)
        && Objects.equals(dataType, other.dataType) && Objects.equals(source, other.source)
        && Objects.equals(subject, other.subject)
        && Objects.equals(distribution, other.distribution) && Objects.equals(value, other.value)
        && Objects.equals(reference, other.reference)
        && Objects.equals(variabilitySubject, other.variabilitySubject)
        && Objects.equals(rangeOfApplicability, other.rangeOfApplicability)
        && Objects.equals(modelApplicability, other.modelApplicability)
        && Objects.equals(error, other.error);
  }
}
