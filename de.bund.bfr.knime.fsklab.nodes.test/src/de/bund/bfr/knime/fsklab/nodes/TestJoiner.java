package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import metadata.GeneralInformation;
import org.junit.Test;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.NodeOutPort;

import de.bund.bfr.knime.fsklab.CombinedFskPortObject;


public class TestJoiner extends WorkflowTestCase {
	private final String expectedJoinedModel_Name = "Simple Model 1 | Simple Model 2";
	private NodeID m_joinerNode;

	@Test
	public void testJoinerWithoutConnections() throws Exception {
		NodeID baseID = loadAndSetWorkflow();
		m_joinerNode = new NodeID(baseID, 3);
		executeAllAndWait();
		NodeContainer joinerNodeContainer = getManager().getNodeContainer(m_joinerNode);
		// port 0 is always flow variable port
		NodeOutPort outPort = joinerNodeContainer.getOutPort(1);
		assertNotNull(outPort);
		//Check first the joined metadata 
		//TODO test joined generalInformation
		GeneralInformation gi= ((CombinedFskPortObject)outPort.getPortObject()).generalInformation;
		assertNotNull(((CombinedFskPortObject)outPort.getPortObject()).generalInformation);
		assertEquals(expectedJoinedModel_Name, gi.getName());
		//TODO test joined dataBackground
		assertNotNull(((CombinedFskPortObject)outPort.getPortObject()).dataBackground);
		//TODO test joined modelMath
		assertNotNull(((CombinedFskPortObject)outPort.getPortObject()).modelMath);
		//TODO test joined scope
		assertNotNull(((CombinedFskPortObject)outPort.getPortObject()).scope);
		
		//TODO check the results after running
		
		
		
		
		getManager().resetAndConfigureAll();
		
	}
	
}
