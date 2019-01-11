package de.bund.bfr.knime.fsklab.nodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Collection;

import org.eclipse.core.runtime.FileLocator;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowLoadHelper;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.node.workflow.WorkflowPersistor.LoadResultEntry.LoadResultEntryType;
import org.knime.core.node.workflow.WorkflowPersistor.WorkflowLoadResult;

public class WorkflowTestCase {
	private final NodeLogger m_logger = NodeLogger.getLogger(getClass());

	private WorkflowManager m_manager;

	protected NodeID loadAndSetWorkflow() throws Exception {
		File workflowDir = getDefaultWorkflowDirectory();
		System.out.println(workflowDir);
		return loadAndSetWorkflow(workflowDir);
	}

	protected NodeID loadAndSetWorkflow(final File workflowDir) throws Exception {
		WorkflowManager m = loadWorkflow(workflowDir, new ExecutionMonitor()).getWorkflowManager();
		setManager(m);
		return m.getID();
	}

	protected WorkflowLoadResult loadWorkflow(final File workflowDir, final ExecutionMonitor exec) throws Exception {
		return loadWorkflow(workflowDir, exec, new WorkflowLoadHelper(workflowDir));
	}

	protected WorkflowLoadResult loadWorkflow(final File workflowDir, final ExecutionMonitor exec,
			final WorkflowLoadHelper loadHelper) throws Exception {
		WorkflowLoadResult loadResult = WorkflowManager.ROOT.load(workflowDir, exec, loadHelper, false);
		Collection<NodeContainer> nodeContainers = loadResult.getLoadedInstance().getNodeContainers();
		System.out.println(nodeContainers);
		WorkflowManager m = loadResult.getWorkflowManager();
		if (m == null) {
			throw new Exception("Errors reading workflow: " + loadResult.getFilteredError("", LoadResultEntryType.Ok));
		} else {
			switch (loadResult.getType()) {
			case Ok:
				break;
			default:
				m_logger.info("Errors reading workflow (proceeding anyway): ");
				dumpLineBreakStringToLog(loadResult.getFilteredError("", LoadResultEntryType.Warning));
			}
		}
		return loadResult;
	}

	/**
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	protected File getDefaultWorkflowDirectory() throws Exception {
		String classSimpleName = getClass().getSimpleName();
		classSimpleName = classSimpleName.substring(0, 1).toLowerCase() + classSimpleName.substring(1);
		return getWorkflowDirectory("/workflows/"+classSimpleName);
	}

	protected File getWorkflowDirectory(final String pathRelativeToTestClass) throws Exception {
		ClassLoader l = getClass().getClassLoader();
		//String workflowDirString = getClass().getPackage().getName();
		// workflowDirPath = workflowDirString.replace('.', '/');
		String workflowDirPath = pathRelativeToTestClass;
		URL workflowURL = l.getResource(workflowDirPath);
		if (workflowURL == null) {
			throw new Exception("Can't load workflow that's expected to be in directory " + workflowDirPath);
		}

		if (!"file".equals(workflowURL.getProtocol())) {
			workflowURL = FileLocator.resolve(workflowURL);
		}

		File workflowDir = new File(workflowURL.getFile());
		if (!workflowDir.isDirectory()) {
			throw new Exception("Can't load workflow directory: " + workflowDir);
		}
		return workflowDir;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	protected void setManager(final WorkflowManager manager) {
		m_manager = manager;
	}

	/**
	 * @return the manager
	 */
	protected WorkflowManager getManager() {
		return m_manager;
	}

	protected void dumpLineBreakStringToLog(final String s) throws IOException {
		BufferedReader r = new BufferedReader(new StringReader(s));
		String line;
		while ((line = r.readLine()) != null) {
			m_logger.info(line);
		}
		r.close();
	}
	protected void executeAllAndWait() throws Exception {
		m_manager.executeAllAndWaitUntilDone();
    }
	protected void reset(final NodeID... ids) {
        for (NodeID id : ids) {
        	m_manager.resetAndConfigureNode(id);
        }
    }
	
	  
}
