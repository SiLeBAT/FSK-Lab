package de.bund.bfr.knime.pmm.extendedtable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.bund.bfr.knime.pmm.extendedtable.items.AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml;

public class Model2MetadataTest {

	@Test
	public void testConstructor() throws Exception {
		
		Model2Metadata metadata = new Model2Metadata();
		
		assertNull(metadata.agentXml);
		assertNull(metadata.matrixXml);
		assertTrue(metadata.modelLiteratureItems.isEmpty());
		assertTrue(metadata.estimatedModelLiteratureItems.isEmpty());
		
		assertNotNull(metadata.getW3C());
	}
	
	@Test
	public void testW3C() throws Exception {
		
		// Test with empty metadata
		assertNotNull(new Model2Metadata().getW3C());
		
		// Test with "filled" metadata
		Model2Metadata filledMetadata = new Model2Metadata();
		filledMetadata.agentXml = new AgentXml(AgentXml.Type.Model2);
		filledMetadata.matrixXml = new MatrixXml(MatrixXml.Type.Model2);
		
		assertNotNull(filledMetadata.getW3C());
	}
}
