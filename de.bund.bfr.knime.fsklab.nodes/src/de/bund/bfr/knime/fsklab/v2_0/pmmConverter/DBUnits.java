/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *******************************************************************************/
package de.bund.bfr.knime.fsklab.v2_0.pmmConverter;

import java.util.HashMap;
import java.util.Map;

import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;

/** Holds DB units, which can be accessed using the display strings. */
public class DBUnits {

  static Map<String, UnitsFromDB> dbUnits;

  // This class is so ridiculously expensive, it should not instantiated more
  // than once
  private DBUnits() {}

  /** Get units from DB with their display strings as keys */
  public synchronized static Map<String, UnitsFromDB> getDBUnits() {
    if (dbUnits == null) {
      // Create unit map with unit display as keys
      dbUnits = new HashMap<>();
      for (UnitsFromDB ufdb : UnitsFromDB.askDB().values()) {
          dbUnits.put(ufdb.displayInGuiAs, ufdb);
      }
    }
    return dbUnits;
  }
}
