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
package de.bund.bfr.knime.pmm.pmfwriter.fsk;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

class NodeSettings {

	// Configuration keys
	private static final String CFG_OUT_PATH = "outPath";
	private static final String CFG_MODEL_NAME = "modelName";
	private static final String CFG_GIVEN_NAME = "creatorGivenName";
	private static final String CFG_FAMILY_NAME = "creatorFamilyName";
	private static final String CFG_CONTACT = "creatorContact";
	private static final String CFG_CREATED_DATE = "createdDate";
	private static final String CFG_MODIFIED_DATE = "modifiedDate";
	private static final String CFG_IS_SECONDARY = "isSecondary";
	private static final String CFG_OVERWRITE = "overwrite";
	private static final String CFG_SPLIT_MODELS = "splitModels";
	private static final String CFG_REFERENCE_LINK = "referenceLink";
	private static final String CFG_LICENSE = "license";
	private static final String CFG_NOTES = "notes";

	public String outPath = "";
	public String modelName = "";
	public String creatorGivenName = "";
	public String creatorFamilyName = "";
	public String creatorContact = "";

	/** Creation date in milliseconds since 1970 in UTC time. */
	public long createdDate;

	/** Last modification date in milliseconds since 1970 in UTC time. */
	public long modifiedDate;

	public boolean isSecondary = false;
	public boolean overwrite = false;
	public boolean splitModels = false;
	public String referenceDescriptionLink = "";
	public String license = "";
	public String notes = "";

	public void load(final NodeSettingsRO settings) throws InvalidSettingsException {
		outPath = settings.getString(CFG_OUT_PATH);
		modelName = settings.getString(CFG_MODEL_NAME);
		creatorGivenName = settings.getString(CFG_GIVEN_NAME);
		creatorFamilyName = settings.getString(CFG_FAMILY_NAME);
		creatorContact = settings.getString(CFG_CONTACT);
		createdDate = settings.getLong(CFG_CREATED_DATE);
		modifiedDate = settings.getLong(CFG_MODIFIED_DATE);
		isSecondary = settings.getBoolean(CFG_IS_SECONDARY);
		overwrite = settings.getBoolean(CFG_OVERWRITE);
		splitModels = settings.getBoolean(CFG_SPLIT_MODELS);
		referenceDescriptionLink = settings.getString(CFG_REFERENCE_LINK);
		license = settings.getString(CFG_LICENSE);
		notes = settings.getString(CFG_NOTES);
	}

	public void save(final NodeSettingsWO settings) {
		settings.addString(CFG_OUT_PATH, outPath);
		settings.addString(CFG_MODEL_NAME, modelName);
		settings.addString(CFG_GIVEN_NAME, creatorGivenName);
		settings.addString(CFG_FAMILY_NAME, creatorFamilyName);
		settings.addString(CFG_CONTACT, creatorContact);
		settings.addLong(CFG_CREATED_DATE, createdDate);
		settings.addLong(CFG_MODIFIED_DATE, modifiedDate);
		settings.addBoolean(CFG_IS_SECONDARY, isSecondary);
		settings.addBoolean(CFG_OVERWRITE, overwrite);
		settings.addBoolean(CFG_SPLIT_MODELS, splitModels);
		settings.addString(CFG_REFERENCE_LINK, referenceDescriptionLink);
		settings.addString(CFG_LICENSE, license);
		settings.addString(CFG_NOTES, notes);
	}
}