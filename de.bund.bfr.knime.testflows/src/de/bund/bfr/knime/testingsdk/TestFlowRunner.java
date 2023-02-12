package de.bund.bfr.knime.testingsdk;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.rules.ErrorCollector;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.workflow.UnsupportedWorkflowVersionException;
import org.knime.core.util.LockFailedException;
import org.knime.testing.core.TestrunConfiguration;
import org.knime.workbench.core.util.ImageRepository;
import org.knime.workbench.core.util.ImageRepository.SharedImages;
import org.knime.workbench.repository.RepositoryManager;

import de.bund.bfr.knime.testingsdk.core.ng.WorkflowCloseTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowCloseViewsTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowDeprecationTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowDialogsTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowExecuteTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowHiliteTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowLoadTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowLogMessagesTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowMemLeakTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowNodeMessagesTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowOpenViewsTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowSaveTest;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowTestContext;
import de.bund.bfr.knime.testingsdk.core.ng.WorkflowUncaughtExceptionsTest;

/**
 * Runner which will execute a single workflow and perform test checks.
 * 
 * Based on org.knime.testing.core.ng.TestflowRunnerApplication and
 * WorkflowTestSuite
 * 
 * Because that class is not exported by org.knime.testing plugin we copied the
 * reusable classes to here.
 * 
 * The TestflowRunnerApplication implementation is running the test harness it
 * conflicts with the tycho test harness, , this implementation reports errors
 * using the Junit error collector instead of writing it own xml files.
 *
 * Differences from TestflowRunnerApplication CLI arguments vs
 * TestrunConfiguration flags * -include replaced by Tycho test selection *
 * -root replaced with single workflow directory call to runTestWorkflow *
 * -server, not implemented * -xmlResult and -xmlResultDir replace with Tycho
 * surefire reports * -outputToSeparateFile, not implemented, use surefire
 * report for stdout/stderr * -loadSaveLoad, not implemented * -untestedNodes,
 * not implemented * -timeout, can be replaced with tycho
 * forkedProcessTimeoutInSeconds parameter * -stacktraceOnTimeout, replaced with
 * maven -e flag * -preferences, not implemented * -workflow.variable, not
 * implemented
 */
public class TestFlowRunner {
	private ErrorCollector collector = new ErrorCollector();
	private TestrunConfiguration m_runConfiguration;

	public TestFlowRunner(ErrorCollector collector, TestrunConfiguration runConfiguration) {
		super();
		this.collector = collector;
		this.m_runConfiguration = runConfiguration;
		if (m_runConfiguration.isLoadSaveLoad()) {
			throw new UnsupportedOperationException("LoadSaveLoad is not supported");
		}
	}

	public void runTestWorkflow(File workflowDir) throws IOException, InvalidSettingsException,
			CanceledExecutionException, UnsupportedWorkflowVersionException, LockFailedException, InterruptedException {
		File testcaseRoot = workflowDir;
		String workflowName = workflowDir.getName();
		IProgressMonitor monitor = null;

		// this is to load the repository plug-in
		RepositoryManager.INSTANCE.toString();
		// and this initialized the image repository in the main thread;
		// otherwise resolving old node factories
		// in FileSingleNodeContainerPersistor will fail (see bug# 4464)
		ImageRepository.getImage(SharedImages.Busy);

		WorkflowTestContext m_context = new WorkflowTestContext(m_runConfiguration);

		WorkflowLogMessagesTest workflowLogMessagesTest;
		workflowLogMessagesTest = new WorkflowLogMessagesTest();
		workflowLogMessagesTest.aboutToStart();

		WorkflowUncaughtExceptionsTest workflowUncaughtExceptionsTest = new WorkflowUncaughtExceptionsTest(workflowName,
				monitor, m_context);
		workflowUncaughtExceptionsTest.aboutToStart();

		WorkflowLoadTest workflowLoadTest = new WorkflowLoadTest(workflowDir, testcaseRoot, workflowName, monitor,
				m_runConfiguration, m_context);
		workflowLoadTest.run(collector);

		// WorkflowDeprecationTest
		if (m_runConfiguration.isReportDeprecatedNodes()) {
			WorkflowDeprecationTest workflowDeprecationTest = new WorkflowDeprecationTest(workflowName, monitor,
					m_context);
			workflowDeprecationTest.run(collector);
		}

		// WorkflowOpenViewsTest
		if (m_runConfiguration.isTestViews()) {
			WorkflowOpenViewsTest workflowOpenViewsTest = new WorkflowOpenViewsTest(workflowName, monitor, m_context);
			workflowOpenViewsTest.run(collector);
		}

		WorkflowExecuteTest workflowExecuteTest = new WorkflowExecuteTest(workflowName, monitor, m_context,
				m_runConfiguration);
		workflowExecuteTest.run(collector);

		if (m_runConfiguration.isCheckNodeMessages()) {
			WorkflowNodeMessagesTest workflowNodeMessagesTest = new WorkflowNodeMessagesTest(workflowName, monitor,
					m_context);
			workflowNodeMessagesTest.run(collector);
		}

		// WorkflowDialogsTest
		if (m_runConfiguration.isTestDialogs()) {
			WorkflowDialogsTest workflowDialogsTest = new WorkflowDialogsTest(workflowName, monitor, m_context);
			workflowDialogsTest.run(collector);
		}

		// WorkflowHiliteTest
		WorkflowHiliteTest workflowHiliteTest = new WorkflowHiliteTest(workflowName, monitor, m_context);
		workflowHiliteTest.run(collector);

		// WorkflowCloseViewsTest
		if (m_runConfiguration.isTestViews()) {
			WorkflowCloseViewsTest workflowCloseViewsTest = new WorkflowCloseViewsTest(workflowName, monitor,
					m_context);
			workflowCloseViewsTest.run(collector);
		}

		// save
		File saveLocation = m_runConfiguration.getSaveLocation();
		if (saveLocation != null) {
			WorkflowSaveTest workflowSaveTest = new WorkflowSaveTest(workflowName, monitor, saveLocation, m_context);
			workflowSaveTest.run(collector);
		}

		// WorkflowCloseTest
		if (m_runConfiguration.isCloseWorkflowAfterTest()) {
			WorkflowCloseTest workflowCloseTest = new WorkflowCloseTest();
			workflowCloseTest.run(collector, m_context);
		}

		// WorkflowLogMessagesTest
		if (m_runConfiguration.isCheckLogMessages()) {
			workflowLogMessagesTest.run(collector, m_context.getTestflowConfiguration());
		} else {
			workflowLogMessagesTest.aboutToStop();
		}

		// WorkflowUncaughtExceptionsTest
		workflowUncaughtExceptionsTest.run(collector);

		// WorkflowMemLeakTest
		if (m_runConfiguration.isCheckMemoryLeaks()) {
			WorkflowMemLeakTest workflowMemLeakTest = new WorkflowMemLeakTest(workflowName, monitor, m_runConfiguration,
					m_context);
			workflowMemLeakTest.run(collector);
		}
	}

}
