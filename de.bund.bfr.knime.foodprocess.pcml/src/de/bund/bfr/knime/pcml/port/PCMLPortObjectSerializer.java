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

import org.knime.core.data.util.NonClosableInputStream;
import org.knime.core.data.util.NonClosableOutputStream;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.port.PortObject.PortObjectSerializer;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;

/**
 * @author Heiko Hofer
 */
public class PCMLPortObjectSerializer extends 
		PortObjectSerializer<PCMLPortObject> {
    private static final String PCML_FILE = "pcml.xml";
    private static final String PROPERTIES_FILE = "properties.xml";
    
	/**
     * {@inheritDoc}
     */
	@Override
	public void savePortObject(final PCMLPortObject portObject,			
			final PortObjectZipOutputStream out, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		NonClosableOutputStream noCloseOut = new NonClosableOutputStream(out);
		ModelContent cnt = new ModelContent(PROPERTIES_FILE);
		portObject.saveProperties(cnt);
		out.putNextEntry(new ZipEntry(PROPERTIES_FILE));
        cnt.saveToXML(noCloseOut);    		
        out.putNextEntry(new ZipEntry(PCML_FILE));
        portObject.savePCML(noCloseOut);
        out.close();    
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public PCMLPortObject loadPortObject(final PortObjectZipInputStream in,
			final PortObjectSpec spec, final ExecutionMonitor exec) 
			throws IOException,
			CanceledExecutionException {
		NonClosableInputStream noCloseIn = new NonClosableInputStream(in);
		PCMLPortObject portObject = new PCMLPortObject();
		// read properties
        ZipEntry entry = in.getNextEntry();
        if (!PROPERTIES_FILE.equals(entry.getName())) {
            throw new IOException("Expected '" + PROPERTIES_FILE 
                    + "' zip entry, got " + entry.getName());
        }
        ModelContentRO cnt = ModelContent.loadFromXML(noCloseIn);        
        try {
			portObject.loadProperties(cnt);
		} catch (InvalidSettingsException e1) {
			throw new RuntimeException(e1.getMessage(), e1);
		}
        // read PCML
        String entryName = in.getNextEntry().getName();      
        if (!entryName.equals(PCML_FILE)) {
            throw new IOException("Found unexpected zip entry "
                    + entryName + "! Expected " + PCML_FILE);
        }
        try {
        	portObject.loadPCML((PCMLPortObjectSpec)spec, noCloseIn);
        } catch (Exception e) {
            throw new IOException(e);
        }
        in.close();
        return portObject;
	}

}
