package de.bund.bfr.knime.pmm.fskx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.bund.bfr.pmfml.ModelClass;

public class FskMetaDataTest {

	@SuppressWarnings("static-method")
	@Test
	public void testConstructor() {

		FskMetaData metaData = new FskMetaData();

		assertNull(metaData.modelName);
		assertNull(metaData.modelId);
		assertNull(metaData.modelLink);
		assertNull(metaData.organism);
		assertNull(metaData.organismDetails);
		assertNull(metaData.matrix);
		assertNull(metaData.matrixDetails);
		assertNull(metaData.creator);
		assertNull(metaData.familyName);
		assertNull(metaData.contact);
		assertNull(metaData.software);
		assertNull(metaData.referenceDescription);
		assertNull(metaData.referenceDescriptionLink);
		assertNull(metaData.createdDate);
		assertNull(metaData.modifiedDate);
		assertNull(metaData.rights);
		assertNull(metaData.notes);
		assertFalse(metaData.curated);
		assertNull(metaData.type);
		assertEquals(ModelClass.UNKNOWN, metaData.subject);
		assertNull(metaData.foodProcess);
		// dependentVariable
		assertTrue(metaData.independentVariables.isEmpty());
		assertFalse(metaData.hasData);
	}

}
