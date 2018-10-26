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
package de.bund.bfr.knime.pmm.dbutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.MatrixXml;

/** Holds DB Matrices which can be accessed using their ids. */
public class DBMatrices {

	static List<MatrixXml> dbMatrices;

	private DBMatrices() {
	}

	/** Get matrices from DB with their ids as keys */
	public synchronized static List<MatrixXml> getDBMatrices() {
		if (dbMatrices == null) {
			dbMatrices = new LinkedList<>();
			try (ResultSet rs = DBKernel.getResultSet("SELECT * FROM \"Matrices\"", true)) {
			  while (rs.next()) {
			    int matrixId = rs.getInt("ID");
			    String matrixName = rs.getString("Matrixname");
			    dbMatrices.add(new MatrixXml(matrixId, matrixName, null));
			  }
			} catch (SQLException e) {
			  e.printStackTrace();
			}
		}

		return dbMatrices;
	}
}