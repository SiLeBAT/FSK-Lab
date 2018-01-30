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

import java.net.URI;
import java.util.Objects;

public class Study {

  public String id;

  /** Study tile. */
  public String title = "";

  /** Study description. */
  public String description = "";

  /** Study type. */
  public String designType = "";

  /** Observed measured in the assay. */
  public String measurementType = "";

  /** Employed technology to observe this measurement. */
  public String technologyType = "";

  /** Used technology platform. */
  public String technologyPlatform = "";

  /** Used accreditation procedure. */
  public String accreditationProcedure = "";
  public String protocolName = "";
  public String protocolType = "";

  /** Type of the protocol (e.g. Extraction protocol). */
  public String protocolDescription = "";
  public URI protocolUri;
  public String protocolVersion = "";

  /** Parameters used when executing this protocol. */
  public String parametersName = "";
  public String componentsName = "";
  public String componentsType = "";

  @Override
  public int hashCode() {
    return Objects.hash(id, accreditationProcedure, componentsName, componentsType, description,
        designType, measurementType, parametersName, protocolDescription, protocolName,
        protocolType, protocolUri, protocolVersion, technologyPlatform, technologyType, title);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Study other = (Study) obj;
    return Objects.equals(id, other.id)
        && Objects.equals(accreditationProcedure, other.accreditationProcedure)
        && Objects.equals(componentsName, other.componentsName)
        && Objects.equals(componentsType, other.componentsType)
        && Objects.equals(description, other.description)
        && Objects.equals(designType, other.designType)
        && Objects.equals(measurementType, other.measurementType)
        && Objects.equals(parametersName, other.parametersName)
        && Objects.equals(protocolDescription, other.protocolDescription)
        && Objects.equals(protocolName, other.protocolName)
        && Objects.equals(protocolType, other.protocolType)
        && Objects.equals(protocolUri, other.protocolUri)
        && Objects.equals(protocolVersion, other.protocolVersion)
        && Objects.equals(technologyPlatform, other.technologyPlatform)
        && Objects.equals(technologyType, other.technologyType)
        && Objects.equals(title, other.title);
  }
}
