package de.bund.bfr.knime.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.nodes.URIS;

public class URISTest {

	@Test
	public void test() {
		assertEquals("http://purl.org/NET/mediatypes/application/zip", URIS.zip);
		assertEquals("http://purl.org/NET/mediatypes/application/r", URIS.r);
		assertEquals("http://purl.org/NET/mediatypes/application/x-pmf", URIS.pmf);
		assertEquals("http://purl.org/NET/mediatypes/application/sbml+xml", URIS.sbml);
		assertEquals("http://purl.org/NET/mediatypes/text/x-matlab", URIS.matlab);
		assertEquals("http://purl.org/NET/mediatypes/text/x-php", URIS.php);
	}
}
