package de.bund.bfr.knime.fsklab.nodes;

import java.net.URI;
import java.net.URISyntaxException;

public class URIS {

	public static final URI zip;
	public static final URI r;
	public static final URI pmf;
	public static final URI sbml;
	public static final URI matlab;
	public static final URI php;

	static {
		try {
			zip = new URI("http://purl.org/NET/mediatypes/application/zip");
			r = new URI("http://purl.org/NET/mediatypes/application/r");
			pmf = new URI("http://purl.org/NET/mediatypes/application/x-pmf");
			sbml = new URI("http://purl.org/NET/mediatypes/application/sbml+xml");
			matlab = new URI("http://purl.org/NET/mediatypes/text/x-matlab");
			php = new URI("http://purl.org/NET/mediatypes/text/x-php");
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
