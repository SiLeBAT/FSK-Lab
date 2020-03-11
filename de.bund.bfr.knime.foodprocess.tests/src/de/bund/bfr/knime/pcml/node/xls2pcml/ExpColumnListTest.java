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
package de.bund.bfr.knime.pcml.node.xls2pcml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.bund.bfr.knime.pcml.node.xls2pcml.ExpColumnList;

public class ExpColumnListTest {

	ExpColumnList colList;

	@Before
	public void setUp() {
		colList = new ExpColumnList("http://www.bfr.bund.de/PCML-1_0");
	}

	@Test
	public void testColumnList() {
		assertTrue(colList.getColumnList().sizeOfColumnArray() > 0);
	}

	@Test
	public void testColumnNames() {

		// Tests time column name
		assertNotNull(colList.getTimeColName());
		assertFalse(colList.getTimeColName().getLocalPart().isEmpty());

		// Tests concentration column name
		assertNotNull(colList.getConcColName());
		assertFalse(colList.getConcColName().getLocalPart().isEmpty());

		// Tests temperature column name
		assertNotNull(colList.getTempColName());
		assertFalse(colList.getTempColName().getLocalPart().isEmpty());

		// Tests pH column name
		assertNotNull(colList.getpHColName());
		assertFalse(colList.getpHColName().getLocalPart().isEmpty());

		// Tests salt concentration column name
		assertNotNull(colList.getNaclColName());
		assertFalse(colList.getNaclColName().getLocalPart().isEmpty());

		// Tests water activity column name
		assertNotNull(colList.getAwColName());
		assertFalse(colList.getAwColName().getLocalPart().isEmpty());

		// Tests air humidity column name
		assertNotNull(colList.getAirHumColName());
		assertFalse(colList.getAwColName().getLocalPart().isEmpty());

		// Tests matrix column name
		assertNotNull(colList.getMatrixColName());
		assertFalse(colList.getMatrixColName().getLocalPart().isEmpty());

		// Tests agent column name
		assertNotNull(colList.getAgentColName());
		assertFalse(colList.getAgentColName().getLocalPart().isEmpty());
	}
}
