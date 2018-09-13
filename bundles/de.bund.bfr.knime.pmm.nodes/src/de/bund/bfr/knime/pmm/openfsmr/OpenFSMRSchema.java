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
package de.bund.bfr.knime.pmm.openfsmr;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;

/**
 * Schema for the OpenFSMR template.
 *
 * @author Miguel Alba
 */
public class OpenFSMRSchema extends KnimeSchema {

	public static final String ATT_MODEL_NAME = "Model-Name";
	public static final String ATT_MODEL_ID = "Model-ID";
	public static final String ATT_MODEL_LINK = "Model-Link";
	public static final String ATT_ORGANISM_NAME = "PMF-Organism";
	public static final String ATT_ORGANISM_DETAIL = "PMF-OrganismDetails";
	public static final String ATT_ENVIRONMENT_NAME = "PMF-Environment";
	public static final String ATT_ENVIRONMENT_DETAIL = "PMF-EnvironmentDetails";
	public static final String ATT_MODEL_CREATOR = "Model-Creator";
	public static final String ATT_MODEL_FAMILY_NAME = "Model-FamilyName";
	public static final String ATT_MODEL_CONTACT = "Model-Contact";
	public static final String ATT_MODEL_REFERENCE_DESCRIPTION = "Model-ReferenceDescription";
	public static final String ATT_MODEL_REFERENCE_DESCRIPTION_LINK = "Model-ReferenceDescriptionLink";
	public static final String ATT_MODEL_CREATED_DATE = "Model-Created";
	public static final String ATT_MODEL_MODIFIED_DATE = "Model-Modified";
	public static final String ATT_MODEL_RIGHTS = "Model-Rights";
	public static final String ATT_MODEL_NOTES = "Model-Notes";
	public static final String ATT_MODEL_CURATION_STATUS = "Model-CurationStatus";
	public static final String ATT_MODEL_TYPE = "Model-Type";
	public static final String ATT_MODEL_SUBJECT = "Model-Subject";
	public static final String ATT_MODEL_FOOD_PROCESS = "Model-Foodprocess";
	public static final String ATT_MODEL_DEPENDENT_VARIABLE = "Model-DependentVariables";
	public static final String ATT_MODEL_DEPENDENT_VARIABLE_UNIT = "Model-DependentVariablesUnits";
	public static final String ATT_MODEL_DEPENDENT_VARIABLE_MIN = "Model-DependentVariableMinimum";
	public static final String ATT_MODEL_DEPENDENT_VARIABLE_MAX = "Model-DependentVariableMaximum";
	public static final String ATT_INDEPENDENT_VARIABLE = "Model-IndependentVariables";
	public static final String ATT_INDEPENDENT_VARIABLE_UNITS = "Model-IndependentVariableUnits";
	public static final String ATT_INDEPENDENT_VARIABLE_MINS = "Model-IndependentVariableMins";
	public static final String ATT_INDEPENDENT_VARIABLE_MAXS = "Model-IndependentVariableMaxs";
	public static final String ATT_HAS_DATA = "HasData?";

	public OpenFSMRSchema() {
		addStringAttribute(ATT_MODEL_NAME);
		addStringAttribute(ATT_MODEL_ID);
		addStringAttribute(ATT_MODEL_LINK);
		addStringAttribute(ATT_ORGANISM_NAME);
		addStringAttribute(ATT_ORGANISM_DETAIL);
		addStringAttribute(ATT_ENVIRONMENT_NAME);
		addStringAttribute(ATT_ENVIRONMENT_DETAIL);
		addStringAttribute(ATT_MODEL_CREATOR);
		addStringAttribute(ATT_MODEL_FAMILY_NAME);
		addStringAttribute(ATT_MODEL_CONTACT);
		addStringAttribute(ATT_MODEL_REFERENCE_DESCRIPTION);
		addStringAttribute(ATT_MODEL_REFERENCE_DESCRIPTION_LINK);
		addStringAttribute(ATT_MODEL_CREATED_DATE);
		addStringAttribute(ATT_MODEL_MODIFIED_DATE);
		addStringAttribute(ATT_MODEL_RIGHTS);
		addStringAttribute(ATT_MODEL_NOTES);
		addStringAttribute(ATT_MODEL_CURATION_STATUS);
		addStringAttribute(ATT_MODEL_TYPE);
		addStringAttribute(ATT_MODEL_SUBJECT);
		addStringAttribute(ATT_MODEL_FOOD_PROCESS);
		addStringAttribute(ATT_MODEL_DEPENDENT_VARIABLE);
		addStringAttribute(ATT_MODEL_DEPENDENT_VARIABLE_UNIT);
		addDoubleAttribute(ATT_MODEL_DEPENDENT_VARIABLE_MIN);
		addDoubleAttribute(ATT_MODEL_DEPENDENT_VARIABLE_MAX);
		addStringAttribute(ATT_INDEPENDENT_VARIABLE);
		addStringAttribute(ATT_INDEPENDENT_VARIABLE_UNITS);
		addStringAttribute(ATT_INDEPENDENT_VARIABLE_MINS);
		addStringAttribute(ATT_INDEPENDENT_VARIABLE_MAXS);
		addIntAttribute(ATT_HAS_DATA);
	}
}
