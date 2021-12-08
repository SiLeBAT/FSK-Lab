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

import javax.xml.namespace.QName;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.pcml10.ColumnDocument.Column;
import de.bund.bfr.pcml10.ColumnListDocument.ColumnList;
import de.bund.bfr.pcml10.NameAndDatabaseId;

/** Column list of an experiment */
public class ExpColumnList {

	/** Time column name. */
	public final QName timeColName;
	
	/** Concentration column name. */
	public final QName concColName;
	
	/** Temperature column name. */
	public final QName tempColName;
	
	/** pH column name. */
	public final QName pHColName;
	
	/** Salt concentration column name. */
	public final QName naclColName;
	
	/** Water activity column name. */
	public final QName awColName;
	
	/** Water concentration column name. */
	public final QName waterConcColName;
	
	/** Air humidity column name. */
	public final QName airHumColName;
	
	/** Matrix column name. */
	public final QName matrixColName;
	
	/** Agent column name. */
	public final QName agentColName;

	public final ColumnList columnList;

	public ExpColumnList(String pcmlNamespace) {

		columnList = (ColumnList) ColumnList.Factory.newInstance();

		// Time column
		timeColName = new QName(pcmlNamespace, "c0");
		createColumn(AttributeUtilities.TIME + " [sec]", "c0");

		// Concentration column
		concColName = new QName(pcmlNamespace, "c1");
		createColumn("Concentration []", "c1");

		// Temperature column
		tempColName = new QName(pcmlNamespace, "c2");
		createColumn(AttributeUtilities.ATT_TEMPERATURE + " [°C]", "c2");

		// pH column
		pHColName = new QName(pcmlNamespace, "c3");
		createColumn(AttributeUtilities.ATT_PH, "c3");

		// salt concentration column
		naclColName = new QName(pcmlNamespace, "c4");
		createColumn("NaCl [%]", "c4");

		// water activity column
		awColName = new QName(pcmlNamespace, "c5");
		createColumn(AttributeUtilities.ATT_AW, "c5");

		// water concentration column
		waterConcColName = new QName(pcmlNamespace, "c6");
		createColumn("waterConcentration [%]", "c6");

		// air humidity
		airHumColName = new QName(pcmlNamespace, "c7");
		createColumn("airHumidity [%]", "c7");

		// matrix column
		Column matrixCol = columnList.addNewColumn();
		NameAndDatabaseId matrixNadid = matrixCol.addNewMatrix();
		matrixNadid.setName("Beef [kg]");
		matrixNadid.setDbId(-1);
		matrixColName = new QName(pcmlNamespace, "c8");
		matrixCol.setName(matrixColName.getLocalPart());

		// species column
		Column agentCol = columnList.addNewColumn();
		NameAndDatabaseId agentNadid = agentCol.addNewMatrix();
		agentNadid.setName("Salmonella spp.");
		agentNadid.setDbId(-1);
		agentColName = new QName(pcmlNamespace, "c9");
		agentCol.setName(agentColName.getLocalPart());
	}
	
	private void createColumn(String id, String name) {
		Column newColumn = columnList.addNewColumn();
		newColumn.addNewColumnId().setName(id);
		newColumn.setName(name);
	}
}