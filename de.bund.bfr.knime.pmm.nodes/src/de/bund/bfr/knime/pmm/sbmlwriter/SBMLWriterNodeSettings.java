/*******************************************************************************
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
 *******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlwriter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * Node settings helper class for the SBML Writer node.
 *
 * @author Miguel de Alba
 */
public class SBMLWriterNodeSettings {
	
	// Configuration keys
	private static final String CFG_OVERWRITE = "Overwrite";
	private static final String CFG_OUT_PATH = "outPath";
	private static final String CFG_VARIABLE_PARAM = "variableParams";
	private static final String CFG_CREATOR_GIVEN_NAME = "CreatorGivenName";
	private static final String CFG_CREATOR_FAMILY_NAME = "CreatorFamilyName";
	private static final String CFG_CREATOR_CONTACT = "CreatorContact";
	private static final String CFG_CREATED_DATE = "CreationDateVALUEKEYSETTINGS";
	private static final String CFG_LAST_MODIFIED_DATE = "ModifiedDateVALUEKEYSETTINGS";
	private static final String CFG_REFERENCE = "Reference";

	// Member variables
	public boolean overwrite = false;
	public String outPath = "";
	public String variableParams = "";
	public String creatorGivenName = "";
	public String creatorFamilyName = "";
	public String creatorContact = "";
	public long createdDate = 0;
	public long modifiedDate = 0;	
	public String reference = "";

	public void load(final NodeSettingsRO settings) throws InvalidSettingsException {
		overwrite = settings.getBoolean(CFG_OVERWRITE);
		outPath = settings.getString(CFG_OUT_PATH);
		variableParams = settings.getString(CFG_VARIABLE_PARAM);
		creatorGivenName = settings.getString(CFG_CREATOR_GIVEN_NAME);
		creatorFamilyName = settings.getString(CFG_CREATOR_FAMILY_NAME);
		creatorContact = settings.getString(CFG_CREATOR_CONTACT);
		createdDate = settings.getLong(CFG_CREATED_DATE);
		modifiedDate = settings.getLong(CFG_LAST_MODIFIED_DATE);
		reference = settings.getString(CFG_REFERENCE);
	}

	public void save(final NodeSettingsWO settings) {
		settings.addBoolean(CFG_OVERWRITE, overwrite);
		settings.addString(CFG_OUT_PATH, outPath);
		settings.addString(CFG_VARIABLE_PARAM, variableParams);
		settings.addString(CFG_CREATOR_GIVEN_NAME, creatorGivenName);
		settings.addString(CFG_CREATOR_FAMILY_NAME, creatorFamilyName);
		settings.addString(CFG_CREATOR_CONTACT, creatorContact);
		settings.addLong(CFG_CREATED_DATE, createdDate);
		settings.addLong(CFG_LAST_MODIFIED_DATE, modifiedDate);
		settings.addString(CFG_REFERENCE, reference);
	}
}
