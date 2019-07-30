
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.NodeOutPort;

import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.RunnerNodeModel;
import metadata.SwaggerUtil;


public class JoinerTest extends WorkflowTestCase {
	private final String expectedJoinedModel_Name = "Simple Model 1 | Simple Model 2";
	private final String expectedJoinedModel_OutputwithoutConnections = "20.0";
	private final String expectedValueWithSimulation = "200.0";
	private final String expectedJoinedModel_OutputwithConnections = "40.0";
	private final String expectedsecondJoinedModel_OutputwithConnections = "160.0";
	private final String expectedthirdconversionJoinedModel_OutputwithConnections = "5120.0";
	private final String expectedthirdNormalJoinedModel_OutputwithConnections = "2560.0";

	private NodeID m_joinerNode1;
	private NodeID m_runnerNode1;
	private NodeID m_joinerNode2;
	private NodeID m_runnerNode2;
	private NodeID m_runnerNode10;
	private NodeID m_runnerNode12;
	private NodeID m_runnerNode114;
	private NodeID m_runnerNode116;

	private NodeID m_ReaderNode1;
	private NodeID m_ReaderNode2;
	private NodeID m_ReaderNode3;
	private NodeID m_ReaderNode4;
	private NodeID m_ReaderNode11;
	private NodeID m_ReaderNode14;

	@Before
	public void prepearRunnerForTest() {
		RunnerNodeModel.isTest = true;
	}

	@Test
	public void testJoiner() throws Exception {
		NodeID baseID = loadAndSetWorkflow("TestJoiner");

		m_ReaderNode1 = new NodeID(baseID, 1);
		m_ReaderNode2 = new NodeID(baseID, 2);
		m_joinerNode1 = new NodeID(baseID, 3);
		m_runnerNode1 = new NodeID(baseID, 4);

		m_ReaderNode3 = new NodeID(baseID, 7);
		m_ReaderNode4 = new NodeID(baseID, 8);
		m_ReaderNode14 = new NodeID(baseID, 14);
		m_ReaderNode11 = new NodeID(baseID, 11);
		m_joinerNode2 = new NodeID(baseID, 5);
		m_runnerNode2 = new NodeID(baseID, 6);

		m_runnerNode10 = new NodeID(baseID, 10);
		m_runnerNode12 = new NodeID(baseID, 12);
		m_runnerNode114 = new NodeID(baseID, 114);
		m_runnerNode116 = new NodeID(baseID, 116);

		// TODO check the results after running with connections already set
		executeAndWait(m_runnerNode1);

		NodeContainer joinerNodeContainer = getManager().getNodeContainer(m_joinerNode1);
		// port 0 is always flow variable port
		NodeOutPort outPort = joinerNodeContainer.getOutPort(1);
		assertNotNull(outPort);
		// Check first the joined metadata
		// TODO test joined generalInformation
		/*
		 * GeneralInformation gi = ((CombinedFskPortObject)
		 * outPort.getPortObject()).generalInformation;
		 * assertNotNull(((CombinedFskPortObject)
		 * outPort.getPortObject()).generalInformation);
		 * assertEquals(expectedJoinedModel_Name, gi.getName()); // TODO test joined
		 * dataBackground assertNotNull(((CombinedFskPortObject)
		 * outPort.getPortObject()).dataBackground); // TODO test joined modelMath
		 * assertNotNull(((CombinedFskPortObject) outPort.getPortObject()).modelMath);
		 * // TODO test joined scope assertNotNull(((CombinedFskPortObject)
		 * outPort.getPortObject()).scope); // TODO check the a results after running
		 */
		// without connection
		NodeContainer runner1NodeContainer = getManager().getNodeContainer(m_runnerNode1);
		// port 0 is always flow variable port
		NodeOutPort runner1OutPort = runner1NodeContainer.getOutPort(1);

		assertEquals(expectedJoinedModel_OutputwithoutConnections,
				SwaggerUtil.getParameter(
						((CombinedFskPortObject) runner1OutPort.getPortObject()).getSecondFskPortObject().modelMetadata)
						.get(1).getValue());

		// with connection first level of join
		executeAndWait(m_runnerNode2);
		NodeContainer runner2NodeContainer = getManager().getNodeContainer(m_runnerNode2);
		// port 0 is always a flow variable port
		NodeOutPort runner2OutPort = runner2NodeContainer.getOutPort(1);

		assertEquals(expectedJoinedModel_OutputwithConnections,
				SwaggerUtil
						.getParameter(((CombinedFskPortObject) runner2OutPort.getPortObject()).getSecondFskPortObject().modelMetadata)
						.get(1).getValue());

		executeAndWait(m_runnerNode12);
		NodeContainer runner12NodeContainer = getManager().getNodeContainer(m_runnerNode12);
		// port 0 is always a flow variable port
		NodeOutPort runner12OutPort = runner12NodeContainer.getOutPort(1);

		assertEquals(expectedJoinedModel_OutputwithConnections,
				SwaggerUtil
				.getParameter(((CombinedFskPortObject) runner12OutPort.getPortObject()).getSecondFskPortObject().modelMetadata)
						.get(1).getValue());

		// with connection second level of join
		executeAndWait(m_runnerNode10);
		NodeContainer runner10NodeContainer = getManager().getNodeContainer(m_runnerNode10);
		// port 0 is always a flow variable port
		NodeOutPort runner10OutPort = runner10NodeContainer.getOutPort(1);

		assertEquals(expectedsecondJoinedModel_OutputwithConnections,
				SwaggerUtil.getParameter(((CombinedFskPortObject) ((CombinedFskPortObject) runner10OutPort.getPortObject())
						.getSecondFskPortObject()).getSecondFskPortObject().modelMetadata).get(1)
								.getValue());
		// with connection, third level of join with writing and reading back the joined
		// Object to test the writer and the reader with it.
		// 1. with conversion in use
		executeAndWait(m_runnerNode116);
		NodeContainer runner116NodeContainer = getManager().getNodeContainer(m_runnerNode116);
		// port 0 is always a flow variable port
		NodeOutPort runner116OutPort = runner116NodeContainer.getOutPort(1);

		assertEquals(expectedthirdconversionJoinedModel_OutputwithConnections,
				SwaggerUtil.getParameter(((CombinedFskPortObject) ((CombinedFskPortObject) ((CombinedFskPortObject) runner116OutPort
						.getPortObject()).getSecondFskPortObject()).getSecondFskPortObject())
								.getSecondFskPortObject().modelMetadata).get(1).getValue());
		// 2. without conversion
		executeAndWait(m_runnerNode114);
		NodeContainer runner114NodeContainer = getManager().getNodeContainer(m_runnerNode114);
		// port 0 is always a flow variable port
		NodeOutPort runner114OutPort = runner114NodeContainer.getOutPort(1);

		assertEquals(expectedthirdNormalJoinedModel_OutputwithConnections,SwaggerUtil.getParameter(
				((CombinedFskPortObject) ((CombinedFskPortObject) ((CombinedFskPortObject) runner114OutPort
						.getPortObject()).getSecondFskPortObject()).getSecondFskPortObject())
								.getSecondFskPortObject().modelMetadata).get(1).getValue());

		// wait until R processes are killed

		getManager().resetAndConfigureNode(m_ReaderNode1);
		getManager().resetAndConfigureNode(m_ReaderNode2);
		getManager().resetAndConfigureNode(m_joinerNode1);
		getManager().resetAndConfigureNode(m_runnerNode1);

		getManager().resetAndConfigureNode(m_ReaderNode3);
		getManager().resetAndConfigureNode(m_ReaderNode4);
		getManager().resetAndConfigureNode(m_joinerNode2);
		getManager().resetAndConfigureNode(m_runnerNode2);

		getManager().resetAndConfigureNode(m_runnerNode12);
		getManager().resetAndConfigureNode(m_runnerNode10);
		getManager().resetAndConfigureNode(m_runnerNode116);
		getManager().resetAndConfigureNode(m_runnerNode114);
		getManager().resetAndConfigureNode(m_ReaderNode11);
		getManager().resetAndConfigureNode(m_ReaderNode14);

	}

	@After
	public void reset() {
		RunnerNodeModel.isTest = false;
	}

}
