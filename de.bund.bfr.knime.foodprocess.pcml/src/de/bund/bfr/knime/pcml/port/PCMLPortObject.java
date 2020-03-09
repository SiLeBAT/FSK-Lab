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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.knime.core.data.util.NonClosableInputStream;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.FormulaEvaluator;
import de.bund.bfr.knime.util.Matrix;
import de.bund.bfr.knime.util.ValueAndUnit;
import de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient;
import de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient;
import de.bund.bfr.pcml10.NameAndDatabaseId;
import de.bund.bfr.pcml10.OutportDocument.Outport;
import de.bund.bfr.pcml10.PCMLDocument;
import de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain;
import de.bund.bfr.pcml10.ProcessNodeType;
import de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode;

/**
 * The PortObject of the PCML Port.
 * 
 * @author Heiko Hofer
 *
 */
public class PCMLPortObject implements PortObject {
	private static final String PARENT_ID = "parentId";
	private static final String OUTPORT_INDEX = "outportIndex";	
	
	/** The PCML Document. */
	private PCMLDocument pcmlDoc;
	/** The port spec. */
	private PCMLPortObjectSpec pcmlSpec;
	/** The id of the PCML node that has created this PortObject. */
	private String parentId;
	/** The index of the outport this PortObject comes from. */
	private int outportIndex;
	
	/** Static serializer as demanded from {@link PortObject} framework.*/
	private static PCMLPortObjectSerializer serializer;
	
	
    /**
     * Static serializer as demanded from {@link PortObject} framework.
     * @return serializer for PCML (reads and writes PCML files)
     */
    public static final PortObjectSerializer<PCMLPortObject>
            getPortObjectSerializer() {
        if (serializer == null) {
            serializer = new PCMLPortObjectSerializer();
        }
        return serializer;
    }

    /** Create an empty port object. 
     * @param pcml The PCML document
     * @param partenId the id of the ProcessNode that created this Port. Note,
     * that this ID must be found in the given PCML.
     * @param the index of the outport
     * @param the spec of the outport
     */
    public PCMLPortObject(final PCMLDocument pcml, 
    		final String parentId, final int outportIndex,
    		final PCMLPortObjectSpec pcmlSpec) {
    	this.pcmlDoc = pcml;
    	this.pcmlSpec = pcmlSpec;
		this.parentId = parentId;
		this.outportIndex = outportIndex;
    }
    
    /** Used for deserilisation in PCMLPortObjectSerializer. */
    PCMLPortObject() {
    	// nothing todo
	}

	public PCMLDocument getPcmlDoc() {
		return pcmlDoc;
	}
	public String getParentId() {
		return parentId;
	}
	public int getOutportIndex() {
		return outportIndex;
	}

	/**
     * Returns true when this port is used by this process node, i.e. matrices
     * are sent out of this port.
     * @return true when port is used
     */
    public boolean isUsed() {
    	return pcmlSpec.isUsed();
    }    
    
	/**
     * {@inheritDoc}
     */    
	@Override
	public String getSummary() {
		StringBuilder sb = new StringBuilder();
        sb.append("PCML document");
        return sb.toString();
	}

	/**
     * {@inheritDoc}
     */	
	@Override
	public PortObjectSpec getSpec() {
		return pcmlSpec;
	}

	/**
     * {@inheritDoc}
     */	
	@Override
	public JComponent[] getViews() {
		if (isUsed()) {
			return new JComponent[] {new PCMLPortObjectView(this)};
		} else {
			return new JComponent[] {
					new JLabel("Port is not used by this node")};
		}
	}

    /**
     * Writes the port object to valid PCML.
     *
     * @param out stream where the document is written to
     * @throws IOException if the file cannot be written to the stream
     */
	public void savePCML(final OutputStream out) throws IOException {
		PCMLFormatter.save(pcmlDoc, out);
        out.close();
	}
	
	/**
     * Initializes the PCML port object based on the xml string.
     * @param is the PCML string
     * @throws IOException if the file cannot be found
     * @throws XmlException if something goes wrong during reading
     */
    void loadFrom(final String pcml)
            throws IOException, XmlException {
    	ByteArrayInputStream is =
            new ByteArrayInputStream(pcml.getBytes("UTF-8"));
    	this.loadFrom(is); 
    }		

	/**
     * Initializes the PCML port object based on the xml input stream.
     * @param is the PCML input stream
     * @throws IOException if the file cannot be found
     * @throws XmlException if something goes wrong during reading
     */
    void loadFrom(final InputStream is)
            throws IOException, XmlException {
    	// use NonClosableInputStream to prevent multiple calling of close.
    	// We do it in the next line.
        XmlObject xmlDoc = PCMLDocument.Factory.parse(
        		new NonClosableInputStream(is));

        is.close();
        pcmlDoc = (PCMLDocument)xmlDoc;
        // read PCMLPortObjectSpec from stream.
        ProcessChain pc = pcmlDoc.getPCML().getProcessChain();
        ProcessNode[] processNodes = pc.getProcessNodeArray();        
        if (processNodes != null && processNodes.length > 0 && processNodes[processNodes.length - 1].getOutportArray().length > 0) {        	
        	ProcessNode processNode = processNodes[processNodes.length - 1];
        	// read the id
        	parentId = processNode.getId();
        	outportIndex = 0;
        		
        	Outport outport = processNode.getOutportArray()[outportIndex];        	
        	Map<Matrix, Double> matrices = new LinkedHashMap<Matrix, Double>();
        	Matrix matrix = null;
        	if (!processNode.getType().equals(ProcessNodeType.CONTAMINATING)) {
            	// read the matrices        	
        		NameAndDatabaseId ndoutmatrix = outport.getMatrix();
        		if (ndoutmatrix != null) matrix = new Matrix(ndoutmatrix.getName(), ndoutmatrix.isSetDbId() ? ndoutmatrix.getDbId() : -1);
            	MatrixIncredient[] pcmlMatrices = outport.getMatrixRecipe().getMatrixIncredientArray();
            	for (int i = 0; i < pcmlMatrices.length; i++) {
            		MatrixIncredient pcmlMatrix = pcmlMatrices[i];
            		NameAndDatabaseId ndmatrix = pcmlMatrix.getMatrix();
            		matrices.put(new Matrix(ndmatrix.getName(),
            				ndmatrix.isSetDbId() ? ndmatrix.getDbId() : -1),
            				pcmlMatrix.getFraction());
            	}        		
        	}
        	Map<Agent, ValueAndUnit> agents = new LinkedHashMap<Agent, ValueAndUnit>();
        	if (!processNode.getType().equals(ProcessNodeType.ADMIXING)) {
            	// read the agents
            	if (outport.getAgentRecipe() != null) {
    	        	AgentIncredient[] pcmlAgents = 
    	        		outport.getAgentRecipe().getAgentIncredientArray();	        	
    	        	for (int i = 0; i < pcmlAgents.length; i++) {
    	        		AgentIncredient pcmlAgent = pcmlAgents[i];
    	        		NameAndDatabaseId ndagent = pcmlAgent.getAgent();
    	        		agents.put(new Agent(ndagent.getName(),
    	        				ndagent.isSetDbId() ? ndagent.getDbId() : -1),
    	        				new ValueAndUnit(pcmlAgent.getFraction(), pcmlAgent.getUnit(), pcmlAgent.getObject()));
    	        	}
            	}
        	}

        	pcmlSpec = new PCMLPortObjectSpec(matrix, matrices, null, agents, 
            		FormulaEvaluator.isNumber(outport.getVolume()) 
            			? FormulaEvaluator.getDouble(outport.getVolume()) : null,
					FormulaEvaluator.isNumber(outport.getTemperature()) 
        				? FormulaEvaluator.getDouble(outport.getTemperature()) : null,
					FormulaEvaluator.isNumber(outport.getPressure()) 
        				? FormulaEvaluator.getDouble(outport.getPressure()) : null,
					FormulaEvaluator.isNumber(outport.getPH()) 
        				? FormulaEvaluator.getDouble(outport.getPH()) : null,
					FormulaEvaluator.isNumber(outport.getAw()) 
        				? FormulaEvaluator.getDouble(outport.getAw()) : null);
        } else {
        	pcmlSpec = new PCMLPortObjectSpec();
        }
    }	
    
    /**
     * Creates the PCML port object based on a PCML document. Note, that 
     * further properties and the PortSpec are guessed from the document.
     * @param pcml the PCML document.
     */
	public static PCMLPortObject create(final String pcml) 
    		throws IOException, XmlException {
		ByteArrayInputStream is =
	        new ByteArrayInputStream(pcml.getBytes("UTF-8"));
		return PCMLPortObject.create(is);
	}
	
    /**
     * Creates the PCML port object based on a PCML document. Note, that 
     * further properties and the PortSpec are guessed from the document.
     * @param is stream that gives the PCML document.
     */	
	public static PCMLPortObject create(final InputStream is)
    		throws IOException, XmlException {
		PCMLPortObject portObject = new PCMLPortObject();
		portObject.loadFrom(is);
		return portObject;
	}	
	
	/**
     * Initializes the PCML port object based on the xml input stream.
     * @param spec the referring spec of this object
     * @param is the PCML input stream
     * @throws IOException if the file cannot be found
     * @throws XmlException if something goes wrong during reading
     */
    void loadPCML(final PCMLPortObjectSpec spec, final InputStream is)
            throws IOException, XmlException {
    	// use NonClosableInputStream to prevent multiple calling of close.
    	// We do it in the next line.
    	NonClosableInputStream ncis = new NonClosableInputStream(is);
        XmlObject xmlDoc = PCMLDocument.Factory.parse(ncis);
        is.close();
        pcmlDoc = (PCMLDocument)xmlDoc;
        pcmlSpec = spec;
    }
        
	/**
	 * Serialize properties to the given model content.
	 * @param cnt the model content
	 */
	void saveProperties(final ModelContent config) {
		config.addString(PARENT_ID, parentId);
		config.addInt(OUTPORT_INDEX, outportIndex);		
	}
	
	/**
	 * Load properties from model content 
	 * @param cnt the model content
	 */
	 void loadProperties(final ModelContentRO config) throws
		InvalidSettingsException {
		parentId = config.getString(PARENT_ID);
		outportIndex = config.getInt(OUTPORT_INDEX);
	}


}
