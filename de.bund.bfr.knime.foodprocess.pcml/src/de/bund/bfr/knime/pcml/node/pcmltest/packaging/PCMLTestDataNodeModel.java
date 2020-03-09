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
package de.bund.bfr.knime.pcml.node.pcmltest.packaging;

import java.io.File;
import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
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
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.pcml10.ColumnDocument.Column;
import de.bund.bfr.pcml10.ColumnListDocument.ColumnList;
import de.bund.bfr.pcml10.DataTableDocument.DataTable;
import de.bund.bfr.pcml10.InlineTableDocument.InlineTable;
import de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient;
import de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe;
import de.bund.bfr.pcml10.NameAndDatabaseId;
import de.bund.bfr.pcml10.OutportDocument.Outport;
import de.bund.bfr.pcml10.PCMLDocument;
import de.bund.bfr.pcml10.PCMLDocument.PCML;
import de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData;
import de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain;
import de.bund.bfr.pcml10.ProcessDataDocument.ProcessData;
import de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode;
import de.bund.bfr.pcml10.ProcessNodeType;
import de.bund.bfr.pcml10.ProcessParameters;
import de.bund.bfr.pcml10.RowDocument.Row;

/**
 * This is the model implementation of XML To PCML node.
 * 
 * @author Heiko Hofer
 */
public class PCMLTestDataNodeModel extends NodeModel {
	private final PCMLTestDataNodeSettings settings;
	
    /**
     * Constructor for the node model.
     */
    protected PCMLTestDataNodeModel() {    	
        super(new PortType[] {
        }, new PortType[] {
        	PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class, false),
        });
        settings = new PCMLTestDataNodeSettings();
    }

    /**
     * {@inheritDoc}
     */      
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
    		throws InvalidSettingsException {
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
    	PCMLDocument pcmlDoc = createDoc();
    	
    	PCMLPortObject out = PCMLPortObject.create(pcmlDoc.toString());
		
		boolean valid = PCMLUtil.validate(out.getPcmlDoc(), 
				NodeLogger.getLogger(this.getClass()));
		if (!valid) {
			setWarningMessage("PCML Document does not conform the "
					+ "standard. This may cause unexpected errors.");
		}

    	return new PortObject[]{out};
    }

    private PCMLDocument createDoc() {
		PCMLDocument pcmlDoc = PCMLUtil.create();
		PCML pcml = pcmlDoc.getPCML();
		ProcessChain pChain = pcml.addNewProcessChain();
		// create a new process node
		ProcessNode p = pChain.addNewProcessNode();
		p.setId("p1");
		p.setType(ProcessNodeType.ADMIXING);
		// name of the process node
		NameAndDatabaseId process = p.addNewProcess();
		process.setName("Kuh");
		// parameters of the process
		ProcessParameters param = p.addNewParameters();
		param.setCapacity(5.0);
		param.setDuration(3);
		param.setNumberComputations(3);
		param.setTemperature("37");
		param.setAw("3.0");
		param.setPH("6");
		param.setPressure("1.2");
		// the outport
		Outport out = p.addNewOutport();
		out.setVolume("1.0");
		MatrixRecipe mRecipe = out.addNewMatrixRecipe();
		MatrixIncredient mIncredient = mRecipe.addNewMatrixIncredient();
		NameAndDatabaseId matrix = mIncredient.addNewMatrix();
		matrix.setName("Milch");
		matrix.setDbId(7564);
		mIncredient.setFraction(1);
		

		// the process chain data
		ProcessChainData pcData = pcml.addNewProcessChainData();
		ProcessData p1Data = pcData.addNewProcessData();
		p1Data.setRef("p1");
		p1Data.setTime(0.0);
		DataTable table = p1Data.addNewDataTable();
		ColumnList columnList = table.addNewColumnList();
		// Column Temperature
		Column temperature = columnList.addNewColumn();
		temperature.addNewColumnId().setName(AttributeUtilities.ATT_TEMPERATURE);
		QName tempCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c1");
		temperature.setName(tempCol.getLocalPart());
		// Column Milk
		Column milk = columnList.addNewColumn();
		milk.addNewMatrix().set(matrix);
		QName milkCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c2");
		milk.setName(milkCol.getLocalPart());
		
		InlineTable inlineTable = table.addNewInlineTable();
		Row row1 = inlineTable.addNewRow();
		XmlCursor cur1 = row1.newCursor();
		cur1.toFirstContentToken();
		cur1.insertElementWithText(tempCol, "36.56");
		cur1.insertElementWithText(milkCol, "0.0");
		cur1.dispose();
		
		Row row2 = inlineTable.addNewRow();
		XmlCursor cur2 = row2.newCursor();
		cur2.toFirstContentToken();
		cur2.insertElementWithText(tempCol, "36.89");
		cur2.insertElementWithText(milkCol, "0.4");
		cur2.dispose();
		
		Row row3 = inlineTable.addNewRow();
		XmlCursor cur3 = row3.newCursor();
		cur3.toFirstContentToken();
		cur3.insertElementWithText(tempCol, "37.0");
		cur3.insertElementWithText(milkCol, "1.0");		
		cur3.dispose();

		return pcmlDoc;
		
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
    	PCMLTestDataNodeSettings s = new PCMLTestDataNodeSettings();
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

