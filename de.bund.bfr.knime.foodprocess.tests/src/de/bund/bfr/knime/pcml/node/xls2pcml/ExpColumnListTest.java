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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ExpColumnListTest {

	ExpColumnList colList;

	@Before
	public void setUp() {
		colList = new ExpColumnList("http://www.bfr.bund.de/PCML-1_0");
	}

	@Test
	public void testColumnList() {
		assertTrue(colList.columnList.sizeOfColumnArray() > 0);
	}

	@Test
	public void testColumnNames() {

		assertEquals("c0", colList.timeColName.getLocalPart());
		assertEquals("c1", colList.concColName.getLocalPart());
		assertEquals("c2", colList.tempColName.getLocalPart());
		assertEquals("c3", colList.pHColName.getLocalPart());
		assertEquals("c4", colList.naclColName.getLocalPart());
		assertEquals("c5", colList.awColName.getLocalPart());
		assertEquals("c6", colList.waterConcColName.getLocalPart());
		assertEquals("c7", colList.airHumColName.getLocalPart());
		assertEquals("c8", colList.matrixColName.getLocalPart());
		assertEquals("c9", colList.agentColName.getLocalPart());
	}
}
