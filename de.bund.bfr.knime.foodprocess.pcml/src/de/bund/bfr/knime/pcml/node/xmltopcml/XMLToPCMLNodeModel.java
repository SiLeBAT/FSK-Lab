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
package de.bund.bfr.knime.pcml.node.xmltopcml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.xml.XMLValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.knime.pcml.port.PCMLPortObjectSpec;
import de.bund.bfr.knime.pcml.port.PCMLUtil;

/**
 * This is the model implementation of XML To PCML node.
 * 
 * @author Heiko Hofer
 */
public class XMLToPCMLNodeModel extends NodeModel {
	private final XMLToPCMLNodeSettings settings;
	
    /**
     * Constructor for the node model.
     */
    protected XMLToPCMLNodeModel() {    	
        super(new PortType[] {
        	BufferedDataTable.TYPE,
        }, new PortType[] {
        	PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class, false),
        });
        settings = new XMLToPCMLNodeSettings();
    }

    /**
     * {@inheritDoc}
     */      
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
    		throws InvalidSettingsException {
    	DataTableSpec inSpec = (DataTableSpec)inSpecs[0];
    	if (settings.getColumn() == null) {
    		List<String> compatibleCols = new ArrayList<String>();
    		
            for (DataColumnSpec c : inSpec) {
                if (c.getType().isCompatible(XMLValue.class)) {
                    compatibleCols.add(c.getName());
                }
            }
            if (compatibleCols.size() == 1) {
                // auto-configure
                settings.setColumn(compatibleCols.get(0));
            } else if (compatibleCols.size() > 1) {
                // auto-guessing
                settings.setColumn(compatibleCols.get(0));
                setWarningMessage("Auto guessing: using column \""
                        + compatibleCols.get(0) + "\".");
            } else {
                throw new InvalidSettingsException("No XML "
                        + "column in input table.");
            }
    	}

    	int index = inSpec.findColumnIndex(settings.getColumn());
    	if (-1 == index) {
    		throw new InvalidSettingsException("Cannot find column \""
    				+ settings.getColumn() + "\" in the input.");
    	}
    	PCMLPortObjectSpec outSpec = new PCMLPortObjectSpec();
    	return new PortObjectSpec[]{outSpec};
    }
    
    /**
     * {@inheritDoc}
     */    
    @Override
    protected PortObject[] execute(final PortObject[] inObjects, 
    		final ExecutionContext exec)
    		throws Exception {
    	BufferedDataTable in = (BufferedDataTable)inObjects[0];
    	int index = in.getSpec().findColumnIndex(settings.getColumn());
    	
    	PCMLPortObject out = null;
    	if (in.size() > 0) {
    		DataRow row = in.iterator().next();
    		StringValue value = (StringValue)row.getCell(index);
    		out = PCMLPortObject.create(value.getStringValue());
    		boolean valid = PCMLUtil.validate(out.getPcmlDoc(), 
    				NodeLogger.getLogger(this.getClass()));
    		if (!valid) {
    			setWarningMessage("PCML Document does not conform the "
    					+ "standard. This may cause unexpected errors.");
    		}
    	}
    	if (in.size() > 1) {
    		setWarningMessage("Skip rows except the first one");
    	}
    	return new PortObject[]{out};
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
    	XMLToPCMLNodeSettings s = new XMLToPCMLNodeSettings();
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
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    	// no internals, nothing to reset
    }


}

