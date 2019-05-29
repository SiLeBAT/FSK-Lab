
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.FileLocator;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeContainerParent;
import org.knime.core.node.workflow.NodeContainerState;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.NodeStateChangeListener;
import org.knime.core.node.workflow.NodeStateEvent;
import org.knime.core.node.workflow.SubNodeContainer;
import org.knime.core.node.workflow.WorkflowLoadHelper;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.node.workflow.WorkflowPersistor.LoadResultEntry.LoadResultEntryType;
import org.knime.core.node.workflow.WorkflowPersistor.WorkflowLoadResult;

import junit.framework.Assert;

public class WorkflowTestCase {
	private final NodeLogger m_logger = NodeLogger.getLogger(getClass());

	private WorkflowManager m_manager;

	protected NodeID loadAndSetWorkflow(String folder) throws Exception {
		File workflowDir = getDefaultWorkflowDirectory(folder);
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
	protected File getDefaultWorkflowDirectory(String folder) throws Exception {
		String classSimpleName = getClass().getSimpleName();
		classSimpleName = classSimpleName.substring(0, 1).toLowerCase() + classSimpleName.substring(1);
		return getWorkflowDirectory("/workflows/"+folder);
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
	protected void executeAndWait(final NodeID... ids)
	        throws Exception {
	        NodeID prefix = null;
	        WorkflowManager parent = null;
	        for (NodeID id : ids) {
	            if (prefix == null) {
	                prefix = id.getPrefix();
	                parent = findParent(id);
	            } else if (!prefix.equals(id.getPrefix())) {
	                throw new IllegalArgumentException("Mixing NodeIDs of "
	                        + "different levels " + Arrays.toString(ids));
	            }
	        }
	        if (parent != null) {
	            parent.executeUpToHere(ids);
	        }
	        for (NodeID id : ids) {
	            waitWhileNodeInExecution(id);
	        }
	    }
	protected void waitWhileNodeInExecution(final NodeID id) throws Exception {
        waitWhileNodeInExecution(findNodeContainer(id));
    }
	 protected NodeContainer findNodeContainer(final NodeID id) {
	        WorkflowManager parent = findParent(id);
	        return parent.getNodeContainer(id);
	    }
    protected void waitWhileNodeInExecution(final NodeContainer node)
    throws Exception {
        waitWhile(node, new Hold() {
            @Override
            protected boolean shouldHold() {
            	NodeContainerState s = node.getNodeContainerState();
                return s.isExecutionInProgress();
            }
        });
    }
    protected void waitWhile(final NodeContainer nc,
            final Hold hold) throws Exception {
        if (!hold.shouldHold()) {
            return;
        }

        final ReentrantLock lock = nc instanceof WorkflowManager ? ((WorkflowManager)nc).getReentrantLockInstance()
            : nc.getParent().getReentrantLockInstance();
        final Condition condition = lock.newCondition();
        NodeStateChangeListener l = new NodeStateChangeListener() {
            /** {@inheritDoc} */
            @Override
            public void stateChanged(final NodeStateEvent state) {
                lock.lock();
                try {
                    m_logger.info("Received " + state);
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        };
        lock.lock();
        nc.addNodeStateChangeListener(l);
        try {
            while (hold.shouldHold()) {
                int secToWait = hold.getSecondsToWaitAtMost();
                if (secToWait > 0) {
                    condition.await(secToWait, TimeUnit.SECONDS);
                    break;
                } else {
                    condition.await(5, TimeUnit.SECONDS);
                }
            }
        } finally {
            lock.unlock();
            nc.removeNodeStateChangeListener(l);
        }
    }
    protected static abstract class Hold {
        protected abstract boolean shouldHold();
        protected int getSecondsToWaitAtMost() {
            return -1;
        }
    }
    protected WorkflowManager findParent(final NodeID id) {
        CheckUtils.checkArgumentNotNull(m_manager, "WFM not set");
        final NodeID mgrID = m_manager.getID();
        CheckUtils.checkArgument(id.hasPrefix(mgrID), "NodeID %s has not same prefix as WFM: %s", id, mgrID);

        // this is tricky since we have to deal with subnode containers also. subnode contain a wfm with id suffix "0".
        // this could be a node to query in a subnode
        // 0       - ID of m_manager
        // 0:1     - ID of contained subnode container
        // 0:1:0   - ID of WFM in subnode container
        // 0:1:0:2 - ID of node within subnode

        // here is node contained in a meta node
        // 0       - ID of m_manager
        // 0:2     - ID of contained meta node / wfm
        // 0:1:2   - ID of node within meta node

        Stack<NodeID> prefixStack = new Stack<>();
        NodeID currentID = id;
        while (!currentID.hasSamePrefix(mgrID)) {
            currentID = currentID.getPrefix();
            prefixStack.push(currentID);
        }
        NodeContainerParent currentParent = m_manager;
        while (!prefixStack.isEmpty()) {
            if (currentParent instanceof WorkflowManager) {
                currentParent = ((WorkflowManager)currentParent).getNodeContainer(
                    prefixStack.pop(), NodeContainerParent.class, true);
            } else if (currentParent instanceof SubNodeContainer) {
                SubNodeContainer subnode = (SubNodeContainer)currentParent;
                NodeID expectedWFMID = prefixStack.pop();
                final WorkflowManager innerWFM = subnode.getWorkflowManager();
                Assert.assertEquals(innerWFM.getID(), expectedWFMID);
                currentParent = innerWFM;
            }
        }
        return (WorkflowManager)currentParent;
    }
}
