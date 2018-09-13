/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

public enum FskMetaDataFields {

  name("Model name", (byte) 1), id("Model id", (byte) 2), model_link("Model link",
      (byte) 0), species("Organism name", (byte) 3), species_details("Organism details", (byte) 4),

  matrix("Environment name", (byte) 5), matrix_details("Environment details", (byte) 6),

  creator("Creator", (byte) 7), family_name("Family name", (byte) 0), contact("Contact", (byte) 0),

  software("Software", (byte) 0),

  reference_description("Reference description",
      (byte) 8), reference_description_link("Reference description link", (byte) 0),

  created_date("Created date", (byte) 9), modified_date("Modified date", (byte) 10),

  rights("Rights", (byte) 11), notes("Notes", (byte) 12), curation_status("Curation status",
      (byte) 0), type("Type", (byte) 13), subject("Subject", (byte) 14),

  food_process("Food process", (byte) 0),

  depvars("Dependent variables", (byte) 21), depvars_units("Dependent variables units",
      (byte) 22), depvars_types("Dependent variables types", (byte) 0), depvars_mins(
          "Dependent variables mins",
          (byte) 23), depvars_maxs("Dependent variables maxs", (byte) 24),

  indepvars("Independent variables", (byte) 25), indepvars_units("Independent variables units",
      (byte) 26), indepvars_types("Independent variables types", (byte) 0), indepvars_mins(
          "Independent variables mins", (byte) 27), indepvars_maxs("Independent variables maxs",
              (byte) 28), indepvars_values("Independent variables values", (byte) 0),

  has_data("Has data", (byte) 0);

  /** full name */
  public final String fullname;

  /** Row number in spreadsheet. 0 if missing in spreadsheet. */
  public final byte row;

  FskMetaDataFields(String fullname, byte row) {
    this.fullname = fullname;
    this.row = row;
  }
}
