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
package de.bund.bfr.knime.pcml.node.pcmltotable;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.pcml10.PCMLDocument;

/**
 * This is the model implementation of "PCML to Data Table" node.
 * 
 * @author Heiko Hofer
 */
public class PCMLToDataTableNodeModel extends NodeModel {
	private final PCMLToDataTableNodeSettings settings;
	
    /**
     * Constructor for the node model.
     */
    protected PCMLToDataTableNodeModel() {    	
        super(new PortType[] {
        	PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class, false),
        }, new PortType[] {
        	BufferedDataTable.TYPE,
        });
        settings = new PCMLToDataTableNodeSettings();
    }

    /**
     * {@inheritDoc}
     */      
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
    		throws InvalidSettingsException {
    	// Spec at output is not known in advance. It depends on the PCML.
    	return null;
    }
    
    /**
     * {@inheritDoc}
     */    
    @Override
    protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec)
    		throws Exception {
    	PCMLPortObject pcmlPortObject = (PCMLPortObject)inObjects[0];
    	PCMLDocument pcmlDoc = pcmlPortObject.getPcmlDoc();
    	PCMLDataTable pcmlData = new PCMLDataTable(pcmlDoc);
    	
    	BufferedDataTable outTable = pcmlData.execute(exec);
    	return new PortObject[]{outTable};
    }

	/**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    	// no internals, nothing to reset
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         this.settings.saveSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        this.settings.loadSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	PCMLToDataTableNodeSettings s = new PCMLToDataTableNodeSettings();
        s.loadSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	// no internals, nothing to load
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	// no internals, nothing to save
    }

}

