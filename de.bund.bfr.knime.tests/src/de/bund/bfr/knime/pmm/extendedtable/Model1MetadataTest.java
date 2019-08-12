package de.bund.bfr.knime.pmm.extendedtable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.bund.bfr.knime.pmm.extendedtable.items.AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml;

@SuppressWarnings("static-method")
public class Model1MetadataTest {

	@Test
	public void testConstructor() throws Exception {

		final Model1Metadata metadata = new Model1Metadata();

		assertNull(metadata.agentXml);
		assertNull(metadata.matrixXml);
		assertTrue(metadata.modelLiteratureItems.isEmpty());
		assertTrue(metadata.estimatedModelLiteratureItems.isEmpty());

		assertNotNull(metadata.getW3C());
	}

	@Test
	public void testW3C() throws Exception {

		// Test with empty metadata
		assertNotNull(new Model1Metadata().getW3C());

		// Test with "filled" metadata
		final Model1Metadata filledMetadata = new Model1Metadata();
		filledMetadata.agentXml = new AgentXml(AgentXml.Type.MD);
		filledMetadata.matrixXml = new MatrixXml(MatrixXml.Type.MD);

		assertNotNull(filledMetadata.getW3C());
	}
}
