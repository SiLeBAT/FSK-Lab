/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;

import com.google.common.io.Files;

public class RScript {

	/** R script. */
	public final String script;

	/** Names of the libraries imported in the R script. */
	public final List<String> libraries = new LinkedList<>();

	/** Names of the source files linked in the R script. */
	public final List<String> sources = new LinkedList<>();

	/**
	 * Process R script.
	 * 
	 * @param file.
	 * @throws IOException
	 *             if the file specified by path cannot be read.
	 */
	public RScript(final File file) throws IOException {
		String fileContents = Files.toString(file, Charsets.UTF_8); // throws
																	// IOException

		// If no errors are thrown, proceed to extract libraries and sources
		final String[] lines = fileContents.split("\\r?\\n");

		final Pattern libPattern = Pattern.compile("^\\s*\\b(library|require)\\((\"?.+\"?)\\)");
		final Pattern srcPattern = Pattern.compile("^\\s*\\b(source)\\((\"?.+\"?)\\)");

		StringBuilder sb = new StringBuilder();
		for (final String line : lines) {
			sb.append(line + '\n');

			final Matcher libMatcher = libPattern.matcher(line);
			final Matcher srcMatcher = srcPattern.matcher(line);

			if (libMatcher.find()) {
				final String libName = libMatcher.group(2).replace("\"", "");
				libraries.add(libName);
			} else if (srcMatcher.find()) {
				final String srcName = srcMatcher.group(2).replace("\"", "");
				sources.add(srcName);
			}
		}

		script = sb.toString();
	}
}
