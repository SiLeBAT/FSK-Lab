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
package de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

public class Model1Schema extends KnimeSchema {

	public static final String ATT_MODELCATALOG = "CatModel";
	public static final String ATT_ESTMODEL = "EstModel";
	public static final String ATT_DEPENDENT = "Dependent";
	public static final String ATT_PARAMETER = "Parameter";
	public static final String ATT_INDEPENDENT = "Independent";
	public static final String ATT_MLIT = "M_Literatur";
	public static final String ATT_EMLIT = "EM_Literatur";
	public static final String ATT_DATABASEWRITABLE = "DatabaseWritable";
	public static final String ATT_DBUUID = "M_DB_UID";
	public static final String ATT_METADATA = "Model_Metadata";
	
	public static final int WRITABLE = 1;
	public static final int NOTWRITABLE = 0;
	public static final String MODEL = "Primary Model";
	public static final String NAME = "Name";
	public static final String FORMULA = "Formula";
	public static final String SSE = "SSE";
	public static final String MSE = "MSE";
	public static final String RMSE = "RMSE";
	public static final String RSQUARED = "Rsquared";
	public static final String AIC = "AIC";
	
	public Model1Schema() {
		try {
			addXmlAttribute(ATT_MODELCATALOG);
			addXmlAttribute(ATT_DEPENDENT);
			addXmlAttribute(ATT_INDEPENDENT);
			addXmlAttribute(ATT_PARAMETER);
			addXmlAttribute(ATT_ESTMODEL);
			addXmlAttribute(ATT_MLIT);
			addXmlAttribute(ATT_EMLIT);
			addIntAttribute(ATT_DATABASEWRITABLE);
			addStringAttribute(ATT_DBUUID);
			addXmlAttribute(ATT_METADATA);
		} catch (PmmException ex) {
			ex.printStackTrace(System.err);
		}
	}
}
