package de.bund.bfr.knime.fsklab.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import metadata.GeneralInformation;
import org.junit.Test;
import org.knime.core.node.workflow.ConnectionContainer;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.NodeOutPort;
import org.rosuda.REngine.REXP;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.r.client.RController;


public class TestJoiner extends WorkflowTestCase {
	private final String expectedJoinedModel_Name = "Simple Model 1 | Simple Model 2";
	private final String expectedJoinedModel_OutputwithoutConnections = "20.0";
	private final String expectedJoinedModel_OutputwithConnections = "40.0";
	

	private NodeID m_joinerNode;
	private NodeID m_runnerNode;
	private NodeID m_ReaderNode1;
	private NodeID m_ReaderNode2;
	@Test
	public void testJoinerWithoutConnections() throws Exception {
		NodeID baseID = loadAndSetWorkflow();
		m_ReaderNode1 = new NodeID(baseID, 1);
		m_ReaderNode2 = new NodeID(baseID, 2);
		m_joinerNode = new NodeID(baseID, 3);
		m_runnerNode = new NodeID(baseID, 4);
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
		NodeContainer runnerNodeContainer = getManager().getNodeContainer(m_runnerNode);
		// port 0 is always flow variable port
		NodeOutPort runnerOutPort = runnerNodeContainer.getOutPort(1);
		try (RController controller = new RController()) {
			controller.loadWorkspace(((CombinedFskPortObject)runnerOutPort.getPortObject()).workspace.toFile());
			REXP evalexpression = controller.eval("SecondModelOutput", true);
			assertEquals(expectedJoinedModel_OutputwithoutConnections, evalexpression.asString());
		}
		
		getManager().resetAndConfigureNode(m_ReaderNode1);
		getManager().resetAndConfigureNode(m_ReaderNode2);
		getManager().resetAndConfigureNode(m_joinerNode);
		getManager().resetAndConfigureNode(m_runnerNode);
		
	}
	
	/*@Test
	public void testJoinerWithConnections() throws Exception {
		NodeID baseID = loadAndSetWorkflow();
		m_runnerNode = new NodeID(baseID, 6);
		
		//TODO check the results after running with connections already set 
		executeAndWait(m_runnerNode);
		NodeContainer runnerNodeContainer = getManager().getNodeContainer(m_runnerNode);
		System.out.println(((CombinedFskPortObject)runnerNodeContainer.getOutPort(1).getPortObject()).generalInformation.getName());
		System.out.println(((CombinedFskPortObject)runnerNodeContainer.getOutPort(1).getPortObject()).getJoinerRelation());
		// port 0 is always flow variable port
		NodeOutPort runnerOutPort = runnerNodeContainer.getOutPort(1);
		try (RController controller = new RController()) {
			controller.loadWorkspace(((CombinedFskPortObject)runnerOutPort.getPortObject()).workspace.toFile());
			REXP evalexpression = controller.eval("SecondModelOutput", true);
			assertEquals(expectedJoinedModel_OutputwithConnections, evalexpression.asString());
		}
		
		getManager().resetAndConfigureNode(m_runnerNode);
		
	}*/
	
}
