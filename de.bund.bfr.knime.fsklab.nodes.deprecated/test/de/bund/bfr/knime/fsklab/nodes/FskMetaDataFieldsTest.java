/*
 ***************************************************************************************************
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FskMetaDataFieldsTest {

	@Test
	public void testFullNames() {
		
		assertEquals("Model name", FskMetaDataFields.name.fullname);
		assertEquals("Model id", FskMetaDataFields.id.fullname);
		assertEquals("Model link", FskMetaDataFields.model_link.fullname);
		
		assertEquals("Organism name", FskMetaDataFields.species.fullname);
		assertEquals("Organism details", FskMetaDataFields.species_details.fullname);
		
		assertEquals("Environment name", FskMetaDataFields.matrix.fullname);
		assertEquals("Environment details", FskMetaDataFields.matrix_details.fullname);
		
		assertEquals("Creator", FskMetaDataFields.creator.fullname);
		assertEquals("Family name", FskMetaDataFields.family_name.fullname);
		assertEquals("Contact", FskMetaDataFields.contact.fullname);
		
		assertEquals("Software", FskMetaDataFields.software.fullname);
		
		assertEquals("Reference description", FskMetaDataFields.reference_description.fullname);
		assertEquals("Reference description link", FskMetaDataFields.reference_description_link.fullname);
		
		assertEquals("Created date", FskMetaDataFields.created_date.fullname);
		assertEquals("Modified date", FskMetaDataFields.modified_date.fullname);
		
		assertEquals("Rights", FskMetaDataFields.rights.fullname);
		assertEquals("Notes", FskMetaDataFields.notes.fullname);
		assertEquals("Curation status", FskMetaDataFields.curation_status.fullname);
		
		assertEquals("Type", FskMetaDataFields.type.fullname);
		assertEquals("Subject", FskMetaDataFields.subject.fullname);
		
		assertEquals("Food process", FskMetaDataFields.food_process.fullname);
		
		assertEquals("Dependent variables", FskMetaDataFields.depvars.fullname);
		assertEquals("Dependent variables units", FskMetaDataFields.depvars_units.fullname);
		assertEquals("Dependent variables types", FskMetaDataFields.depvars_types.fullname);
		assertEquals("Dependent variables mins", FskMetaDataFields.depvars_mins.fullname);
		assertEquals("Dependent variables maxs", FskMetaDataFields.depvars_maxs.fullname);
		
		assertEquals("Independent variables", FskMetaDataFields.indepvars.fullname);
		assertEquals("Independent variables units", FskMetaDataFields.indepvars_units.fullname);
		assertEquals("Independent variables types", FskMetaDataFields.indepvars_types.fullname);
		assertEquals("Independent variables mins", FskMetaDataFields.indepvars_mins.fullname);
		assertEquals("Independent variables maxs", FskMetaDataFields.indepvars_maxs.fullname);
		assertEquals("Independent variables values", FskMetaDataFields.indepvars_values.fullname);
	}
	
	@Test
	public void testRowNumbers() {
		
		assertEquals(1, FskMetaDataFields.name.row);
		assertEquals(2, FskMetaDataFields.id.row);
		assertEquals(0, FskMetaDataFields.model_link.row);
		
		assertEquals(3, FskMetaDataFields.species.row);
		assertEquals(4, FskMetaDataFields.species_details.row);
		
		assertEquals(5, FskMetaDataFields.matrix.row);
		assertEquals(6, FskMetaDataFields.matrix_details.row);
		
		assertEquals(7, FskMetaDataFields.creator.row);
		assertEquals(0, FskMetaDataFields.family_name.row);
		assertEquals(0, FskMetaDataFields.contact.row);
		
		assertEquals(0, FskMetaDataFields.software.row);
		
		assertEquals(8, FskMetaDataFields.reference_description.row);
		assertEquals(0, FskMetaDataFields.reference_description_link.row);
		
		assertEquals(9, FskMetaDataFields.created_date.row);
		assertEquals(10, FskMetaDataFields.modified_date.row);
		
		assertEquals(11, FskMetaDataFields.rights.row);
		assertEquals(12, FskMetaDataFields.notes.row);
		assertEquals(0, FskMetaDataFields.curation_status.row);
		
		assertEquals(13, FskMetaDataFields.type.row);
		assertEquals(14, FskMetaDataFields.subject.row);
		
		assertEquals(0, FskMetaDataFields.food_process.row);
		
		assertEquals(21, FskMetaDataFields.depvars.row);
		assertEquals(22, FskMetaDataFields.depvars_units.row);
		assertEquals(0, FskMetaDataFields.depvars_types.row);
		assertEquals(23, FskMetaDataFields.depvars_mins.row);
		assertEquals(24, FskMetaDataFields.depvars_maxs.row);
		
		assertEquals(25, FskMetaDataFields.indepvars.row);
		assertEquals(26, FskMetaDataFields.indepvars_units.row);
		assertEquals(0, FskMetaDataFields.indepvars_types.row);
		assertEquals(27, FskMetaDataFields.indepvars_mins.row);
		assertEquals(28, FskMetaDataFields.indepvars_maxs.row);
		assertEquals(0, FskMetaDataFields.indepvars_values.row);
	}

}
