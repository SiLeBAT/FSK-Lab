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

import java.net.URI;
import java.net.URISyntaxException;

public class URIS {

	public static final URI zip;
	public static final URI tgz;
	public static final URI tar_gz;
	
	public static final URI r;
	public static final URI pmf;
	public static final URI sbml;
	public static final URI matlab;
	public static final URI php;

	static {
		try {
			zip = new URI("http://purl.org/NET/mediatypes/application/zip");
			tgz = new URI("http://purl.org/NET/mediatypes/application/x-tgz");
			tar_gz = new URI("http://purl.org/NET/mediatypes/application/x-tar.gz");
			
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
