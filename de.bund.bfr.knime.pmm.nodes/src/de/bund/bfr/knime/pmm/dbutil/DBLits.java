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
package de.bund.bfr.knime.pmm.dbutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class DBLits {

  static List<LiteratureItem> dbLits = null;

  private DBLits() {}

  /** Get literature items */
  public synchronized static List<LiteratureItem> getDBLits() {
    if (dbLits == null) {
      dbLits = new LinkedList<>();

      try (ResultSet rs = DBKernel.getResultSet("SELECT * FROM \"Literatur\"", true)) {
        while (rs.next()) {
          Integer id = rs.getInt("ID");
          String author = rs.getString("Erstautor");
          String title = rs.getString("Titel");
          String mAbstract = rs.getString("Abstract");
          String journal = rs.getString("Journal");
          String volume = rs.getString("Volume");
          String issue = rs.getString("Issue");
          String website = rs.getString("Webseite");
          String comment = rs.getString("Kommentar");
          Integer year = rs.getInt("Jahr");
          Integer page = rs.getInt("Seite");
          Integer approvalMode = rs.getInt("FreigabeModus");
          Integer type = rs.getInt("Literaturtyp");

          LiteratureItem li = new LiteratureItem(author, year, title, mAbstract, journal, volume,
              issue, page, approvalMode, website, type, comment, id);
          dbLits.add(li);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return dbLits;
  }
}
