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

	QName timeColName; // time column name
	QName concColName; // concentration column name
	QName tempColName; // temperature column name
	QName pHColName; // pH column name
	QName naclColName; // salt concentration column name
	QName awColName; // water activity column name
	QName waterConcColName; // water concentration column name
	QName airHumColName; // air humidity column name
	QName matrixColName; // matrix column name
	QName agentColName; // agent column name

	ColumnList columnList;

	public ExpColumnList(String pcmlNamespace) {

		columnList = ColumnList.Factory.newInstance();

		// Time column
		Column timeCol = columnList.addNewColumn();
		timeCol.addNewColumnId().setName(AttributeUtilities.TIME + " [sec]");
		timeColName = new QName(pcmlNamespace, "c0");
		timeCol.setName(timeColName.getLocalPart());

		// Concentration column
		Column concCol = columnList.addNewColumn();
		concCol.addNewColumnId().setName("Concentration []");
		concColName = new QName(pcmlNamespace, "c1");
		concCol.setName(concColName.getLocalPart());

		// Temperature column
		Column tempCol = columnList.addNewColumn();
		tempCol.addNewColumnId().setName(AttributeUtilities.ATT_TEMPERATURE + " [°C]");
		tempColName = new QName(pcmlNamespace, "c2");
		tempCol.setName(tempColName.getLocalPart());

		// pH column
		Column pHCol = columnList.addNewColumn();
		pHCol.addNewColumnId().setName(AttributeUtilities.ATT_PH);
		pHColName = new QName(pcmlNamespace, "c3");
		pHCol.setName(pHColName.getLocalPart());

		// salt concentration column
		Column naclCol = columnList.addNewColumn();
		naclCol.addNewColumnId().setName("NaCl [%]");
		naclColName = new QName(pcmlNamespace, "c4");
		naclCol.setName(naclColName.getLocalPart());

		// water activity column
		Column awCol = columnList.addNewColumn();
		awCol.addNewColumnId().setName(AttributeUtilities.ATT_AW);
		awColName = new QName(pcmlNamespace, "c5");
		awCol.setName(awColName.getLocalPart());

		// water concentration column
		Column waterConcCol = columnList.addNewColumn();
		waterConcCol.addNewColumnId().setName("waterConcentration [%]");
		waterConcColName = new QName(pcmlNamespace, "c6");
		waterConcCol.setName(waterConcColName.getLocalPart());

		// air humidity
		Column airHumCol = columnList.addNewColumn();
		airHumCol.addNewColumnId().setName("airHumidity [%]");
		airHumColName = new QName(pcmlNamespace, "c7");
		airHumCol.setName(airHumColName.getLocalPart());

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

	public QName getTimeColName() {
		return timeColName;
	}

	public QName getConcColName() {
		return concColName;
	}

	public QName getTempColName() {
		return tempColName;
	}

	public QName getpHColName() {
		return pHColName;
	}

	public QName getNaclColName() {
		return naclColName;
	}

	public QName getAwColName() {
		return awColName;
	}

	public QName getWaterConcColName() {
		return waterConcColName;
	}

	public QName getAirHumColName() {
		return airHumColName;
	}

	public QName getMatrixColName() {
		return matrixColName;
	}

	public QName getAgentColName() {
		return agentColName;
	}

	public ColumnList getColumnList() {
		return columnList;
	}
}