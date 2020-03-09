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
package de.bund.bfr.knime.pcml.port;

import java.io.IOException;
import java.util.zip.ZipEntry;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.port.PortObjectSpec.PortObjectSpecSerializer;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;

public class PCMLPortObjectSpecSerializer extends
		PortObjectSpecSerializer<PCMLPortObjectSpec> {
	private static final String FILENAME = "pcml_spec.xml";

	/**
     * {@inheritDoc}
     */
	@Override
	public void savePortObjectSpec(final PCMLPortObjectSpec spec,
			final PortObjectSpecZipOutputStream out) throws IOException {
		ModelContent cnt = new ModelContent(FILENAME);
        spec.save(cnt);
        out.putNextEntry(new ZipEntry(FILENAME));
        cnt.saveToXML(out);
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public PCMLPortObjectSpec loadPortObjectSpec(
			final PortObjectSpecZipInputStream in)
			throws IOException {
        ZipEntry entry = in.getNextEntry();
        if (!FILENAME.equals(entry.getName())) {
            throw new IOException("Expected '" + FILENAME 
                    + "' zip entry, got " + entry.getName());
        }
        ModelContentRO cnt = ModelContent.loadFromXML(in);        
        try {
            return PCMLPortObjectSpec.load(cnt);
        } catch (InvalidSettingsException e) {
            throw new IOException(e.getMessage(), e);
        }
	}

}
