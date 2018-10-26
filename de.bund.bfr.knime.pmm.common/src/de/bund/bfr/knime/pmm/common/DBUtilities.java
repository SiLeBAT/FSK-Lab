/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.common;

import java.sql.Connection;

import org.hsh.bfr.db.DBKernel;

public class DBUtilities {

	private DBUtilities() {
	}

	public static LiteratureItem getLiteratureItem(Integer id) {
		return getLiteratureItem(null, id, null);
	}

	public static LiteratureItem getLiteratureItem(Connection conn, Integer id, String dbuuid) {
		String author = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Erstautor");
		String title = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Titel");
		String mAbstract = (String) DBKernel.getValue(conn, "Literatur", "ID",
				id + "", "Abstract");
		String journal = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Journal");
		String volume = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Volume");
		String issue = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Issue");
		String website = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Webseite");
		String comment = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Kommentar");
		Integer year = (Integer) DBKernel.getValue("Literatur", "ID", id + "",
				"Jahr");
		Integer page = (Integer) DBKernel.getValue("Literatur", "ID", id + "",
				"Seite");
		Integer approvalMode = (Integer) DBKernel.getValue("Literatur", "ID",
				id + "", "FreigabeModus");
		Integer type = (Integer) DBKernel.getValue("Literatur", "ID", id + "",
				"Literaturtyp");

		LiteratureItem li = new LiteratureItem(author, year, title, mAbstract, journal,
				volume, issue, page, approvalMode, website, type, comment, id);
		li.dbuuid = dbuuid;

		return li;
	}
}
