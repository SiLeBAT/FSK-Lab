package de.bund.bfr.knime.pmm.extendedtable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.bund.bfr.knime.pmm.extendedtable.items.AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml;

@SuppressWarnings("static-method")
public class TimeSeriesMetadataTest {

	@Test
	public void testConstructor() throws Exception {

		final TimeSeriesMetadata metadata = new TimeSeriesMetadata();

		assertNull(metadata.agentXml);
		assertNull(metadata.matrixXml);
		assertTrue(metadata.literatureItems.isEmpty());

		assertNotNull(metadata.getW3C());
	}

	@Test
	public void testW3C() throws Exception {

		// Test with empty metadata
		assertNotNull(new TimeSeriesMetadata().getW3C());

		// Test with "filled" metadata
		final TimeSeriesMetadata filledMetadata = new TimeSeriesMetadata();
		filledMetadata.agentXml = new AgentXml(AgentXml.Type.MD);
		filledMetadata.matrixXml = new MatrixXml(MatrixXml.Type.MD);

		assertNotNull(filledMetadata.getW3C());
	}
}
