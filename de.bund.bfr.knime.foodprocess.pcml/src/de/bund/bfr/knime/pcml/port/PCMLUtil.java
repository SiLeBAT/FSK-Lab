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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.knime.core.node.KNIMEConstants;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;

import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.Matrix;
import de.bund.bfr.knime.util.ValueAndUnit;
import de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient;
import de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe;
import de.bund.bfr.pcml10.ApplicationDocument.Application;
import de.bund.bfr.pcml10.HeaderDocument.Header;
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

public class PCMLUtil {
	/** Creates an empty PCMLDocument. */
	public static PCMLDocument create() {
		PCMLDocument doc = (PCMLDocument) PCMLDocumentFactory.Builder.newInstance();
		PCML pcml = doc.addNewPCML();
		pcml.setVersion("1.0");

		Header header = pcml.addNewHeader();

		String owner = System.getProperty("user.name");
		if (owner == null || owner.isEmpty()) {
			owner = "KNIME";
		}
		header.setCopyright(owner);

		Application application = header.addNewApplication();
		application.setName("KNIME");
		application.setVersion(KNIMEConstants.MAJOR + "."
				+ KNIMEConstants.MINOR + "." + KNIMEConstants.REV);
		header.setApplication(application);

		return doc;
	}
	
	/** Adds an outport to the ProcessNode that is initialized with the given
	 * properties of the spec
	 * @param processNode
	 * @param spec
	 * @return the added Outport
	 */
	public static Outport addOutport(final ProcessNode processNode, 
			final PCMLPortObjectSpec spec) {
		Outport outport = processNode.addNewOutport();
		// add matrices
		MatrixRecipe matrixRecipe = outport.addNewMatrixRecipe();
		for (Entry<Matrix, Double> matrix : spec.getMatrices().entrySet()) {
			MatrixIncredient incredient = matrixRecipe.addNewMatrixIncredient();
			NameAndDatabaseId pcmlMatrix = incredient.addNewMatrix();
			if (matrix.getKey().getId() > -1) {
				pcmlMatrix.setDbId(matrix.getKey().getId());
			}
			pcmlMatrix.setName(matrix.getKey().getName());
			incredient.setFraction(matrix.getValue());
		}
		// add agents
		AgentRecipe agentRecipe = outport.addNewAgentRecipe();
		for (Entry<Agent, ValueAndUnit> agent : spec.getAgents().entrySet()) {
			AgentIncredient incredient = agentRecipe.addNewAgentIncredient();
			NameAndDatabaseId pcmlAgent = incredient.addNewAgent();
			if (agent.getKey().getId() > -1) {
				pcmlAgent.setDbId(agent.getKey().getId());
			}
			pcmlAgent.setName(agent.getKey().getName());
			if (agent.getValue().getValue() == null) incredient.setFraction(0);
			else incredient.setFraction(agent.getValue().getValue());
			incredient.setUnit(agent.getValue().getUnit());				
		}
		// add physical properties
		if (null != spec.getVolume()) {
			outport.setVolume(Double.toString(spec.getVolume()));
		}
		if (null != spec.getTemperature()) {
			outport.setTemperature(Double.toString(spec.getTemperature()));
		}
		if (null != spec.getPressure()) {
			outport.setPressure(Double.toString(spec.getPressure()));
		}
		if (null != spec.getPH_value()) {
			outport.setPH(Double.toString(spec.getPH_value()));
		}
		if (null != spec.getAw_value()) {
			outport.setAw(Double.toString(spec.getAw_value()));
		}
		return outport;
	}

	/** Merges the PCMLDocument of the given ports to a single document. */
	public static PCMLDocument merge(final PortObject[] portObjects) {		
		PCMLDocument doc = ((PCMLPortObject)portObjects[0]).getPcmlDoc();
		// do a deep copy
		doc = (PCMLDocument)doc.copy();
		for (int i = 1; i < portObjects.length; i++) {
			if (null != portObjects[i]) {
				PCMLUtil.append(doc, 
						((PCMLPortObject)portObjects[i]).getPcmlDoc());
			}
		}
		return doc;
	}
	
	/** Appends the second PCMLDocument to the first. 
	 * @param doc the document that will be appended
	 * @param toAppend appends doc with elements of this document 
	 */
	public static void append(final PCMLDocument doc, 
			final PCMLDocument toAppend) {
		Map<String, XmlObject> newNodes = PCMLUtil.append(doc, toAppend, 
				"ProcessNode");
		// TODO: this might be automated by getting the parent of ProcessNode.
		// Does such an automation make really sense?
//		XmlCursor cursor = xbean.newCursor();
//		Cursor.toParent();
//		XmlObject parentXbean = cursor.getObject();
//		cursor.dispose();

		ProcessChain processChain = doc.getPCML().getProcessChain() != null ?
				doc.getPCML().getProcessChain() 
				: doc.getPCML().addNewProcessChain();
		for (XmlObject xmlObject : newNodes.values()) {
			ProcessNode processNode = (ProcessNode)xmlObject;
			processChain.addNewProcessNode().set(processNode);
		}
		
		// Add ProcessNodeData elements of all new nodes
		ProcessChainData processChainData = 
			doc.getPCML().getProcessChainData() != null 
				? doc.getPCML().getProcessChainData() 
				: doc.getPCML().addNewProcessChainData();
		for (XmlObject xmlObject : newNodes.values()) {
			ProcessNode processNode = (ProcessNode)xmlObject;
			String pID = processNode.getId();
			
			XmlObject[] xmlObjects = toAppend.selectPath(
				      "declare namespace s='" + getPCMLNamespace(doc) + "' " +
				      ".//s:ProcessData");
			for (XmlObject dataObject : xmlObjects) {
				ProcessData pData = (ProcessData)dataObject;
				if (pData.getRef().equals(pID)) {
					processChainData.addNewProcessData().set(pData);
				}
			}
		}
	}
	
	private static Map<String, XmlObject> append(final PCMLDocument doc, 
			final PCMLDocument toAppend, final String element) {
		Map<String, XmlObject> docIds = PCMLUtil.getIds(doc, element);
		Map<String, XmlObject> toAppendIds = PCMLUtil.getIds(toAppend, element);

		toAppendIds.keySet().removeAll(docIds.keySet());
		return toAppendIds;
	}	

	private static Map<String, XmlObject> getIds(final PCMLDocument doc, 
			final String element) {
		XmlObject[] xmlObjects = doc.selectPath(
			      "declare namespace s='" + getPCMLNamespace(doc) + "' " +
			      ".//s:" + element);
		Map<String, XmlObject> map = new LinkedHashMap<String, XmlObject>();
		for (XmlObject xmlObject : xmlObjects) {
			// get the id
			String id = xmlObject.getDomNode().getAttributes()
				.getNamedItem("id").getNodeValue();
			map.put(id, xmlObject);
		}
		return map;
	}
	
	/** Get the namespace ot the PCML element in the given docuement. */
	public static String getPCMLNamespace(final PCMLDocument doc) {
		return "http://www.bfr.bund.de/PCML-1_0";
	}

	/** Validate PCML document and sent errors to the logger.*/
	public static boolean validate(final PCMLDocument pcmlDoc, 
			final NodeLogger logger) {
		// Set up the validation error listener.
		@SuppressWarnings("rawtypes")
		ArrayList validationErrors = new ArrayList();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		// During validation, errors are added to the ArrayList
		boolean valid = pcmlDoc.validate(validationOptions);

		// Print the errors if the XML is invalid.
		if (!valid) {
			for (Object validationError : validationErrors) {
				logger.debug(validationError);
			}
		}
		return valid;
	}
}