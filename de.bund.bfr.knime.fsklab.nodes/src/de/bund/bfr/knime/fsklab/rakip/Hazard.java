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

public class Hazard {

  public String hazardType = "";

  public String hazardName = "";

  public String hazardDescription = "";

  public String hazardUnit = "";

  public String adverseEffect = "";

  public String sourceOfContamination = "";

  /** Benchmark dose (BMD) */
  public String bmd = "";

  /** Maximum residue limit (MRL). */
  public String mrl = "";

  /** No Observed Adverse Affect Level (NOAEL). */
  public String noael = "";

  /** Lowest Observed Adverse Effect Level (LOAEL). */
  public String loael = "";

  /** Acceptable Operator Adverse Effect Level (AOEL). */
  public String aoel = "";

  /** Acute Reference Dose (ARfD). */
  public String ard = "";

  /** Acceptable Daily Intake (ADI). */
  public String adi = "";

  public String hazardIndSum = "";

  @Override
  public int hashCode() {
    return Objects.hash(adi, aoel, ard, adverseEffect, bmd,
        hazardDescription, hazardIndSum, hazardName, hazardType, hazardUnit, loael, mrl, noael,
        sourceOfContamination);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Hazard other = (Hazard) obj;

    return Objects.equals(adi, other.adi)
        && Objects.equals(aoel, other.aoel)
        && Objects.equals(ard, other.ard)
        && Objects.equals(adverseEffect, other.adverseEffect) && Objects.equals(bmd, other.bmd)
        && Objects.equals(hazardDescription, other.hazardDescription)
        && Objects.equals(hazardIndSum, other.hazardIndSum)
        && Objects.equals(hazardName, other.hazardName)
        && Objects.equals(hazardType, other.hazardType)
        && Objects.equals(hazardUnit, other.hazardUnit) && Objects.equals(loael, other.loael)
        && Objects.equals(mrl, other.mrl) && Objects.equals(noael, other.noael)
        && Objects.equals(sourceOfContamination, other.sourceOfContamination);
  }
}
