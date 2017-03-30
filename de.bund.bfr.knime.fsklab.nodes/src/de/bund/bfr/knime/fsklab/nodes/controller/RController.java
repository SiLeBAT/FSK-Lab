/*
 * ------------------------------------------------------------------
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   17.09.2007 (thiel): created
 */
package de.bund.bfr.knime.fsklab.nodes.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import org.knime.core.util.ThreadUtils;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.sun.jna.Platform;

import de.bund.bfr.knime.fsklab.nodes.rbin.RBinUtil;
import de.bund.bfr.knime.fsklab.nodes.rbin.RBinUtil.InvalidRHomeException;
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceInitializer;
import de.bund.bfr.knime.fsklab.nodes.rserve.RConnectionFactory;
import de.bund.bfr.knime.fsklab.nodes.rserve.RConnectionFactory.RConnectionResource;

/**
 * RController.
 * <p>
 * This class manages some way of communicating with R, executing R code and
 * moving data back and forth.
 * <p>
 * Currently, this class is a singleton and enforces mutual exclusion.
 * <p>
 */
public class RController implements IRController {

	private final NodeLogger LOGGER = NodeLogger.getLogger(getClass());

	private RConnectionResource m_connection;

	private Properties m_rProps;

	private boolean m_initialized = false;
	private boolean m_useNodeContext = false;

	/**
	 * Constructor. Calls {@link #initialize()}. To avoid initialization, use
	 * {@link #RController(boolean)}.
	 */
	public RController() throws RException {
		initialize();
	}

	// --- Initialization & RConnection lifecycle ---
	@Override
	public void initialize() throws RException {
		initR();
	}

	/**
	 * Check if the RController is initialized and throws
	 * {@link RControllerNotInitializedException} if not.
	 */
	private void checkInitialized() {
		if (!m_initialized || m_connection == null) {
			throw new RControllerNotInitializedException();
		}
		if (!m_connection.isRInstanceAlive()) {
			throw new RuntimeException("RServe process terminated unexpectedly");
		}
		if (m_connection.isAvailable()) {
			// resource should never be available, if held by this RController
			// Available means available to acquire for other RControllers.
			throw new RuntimeException("Invalid resource state: lost ownership of connection resource.");
		}
	}

	@Override
	public void close() throws RException {
		if (m_connection != null) {
			m_connection.release();
			m_connection = null;
		}

		m_initialized = false;
	}

	/**
	 * Terminate and relaunch the R process this controller is connected to.
	 * This is currently the only way to interrupt command execution.
	 */
	private void terminateAndRelaunch() throws Exception {
		LOGGER.debug("Terminate R process");

		terminateRProcess();

		try {
			m_connection = initRConnection();
			m_initialized = m_connection != null && m_connection.get().isConnected();
		} catch (Exception e) {
			throw new Exception("Initializing R with Rserve failed");
		}
	}

	/**
	 * Terminate the R process started for this RController.
	 */
	private void terminateRProcess() {
		if (m_connection != null) {
			m_connection.destroy(true);
		}

		m_initialized = false;
	}

	/**
	 * Check if the connection is still valid and recover if not.
	 */
	private void checkConnectionAndRecover() throws Exception {
		if (m_connection != null && m_connection.get().isConnected() && m_connection.isRInstanceAlive()) {
			// connection is fine
			return;
		}

		// all of the session data has been lost. We cannot recover from that.
		terminateAndRelaunch();
	}

	/**
	 * Create and initialize a R connection
	 *
	 * @return the new RConnection
	 */
	private static RConnectionResource initRConnection() throws RserveException, IOException {
		final RConnectionResource resource = RConnectionFactory.createConnection();

		if (!resource.get().isConnected()) {
			throw new IOException("Could not initialize RController: Resource was not connected");
		}

		return resource;
	}

	/**
	 * Initialize the underlying REngine with a backend.
	 * 
	 * @throws IOException
	 */
	private void initR() throws RException {
		try {
			final String rHome = RPreferenceInitializer.getR3Provider().getRHome();
			RBinUtil.checkRHome(rHome);

			m_rProps = RBinUtil.retrieveRProperties();

			if (!m_rProps.containsKey("major")) {
				throw new RException(
						"Cannot determine major version of R. Please check the R installation defined in the KNIME preferences.");
			}

			final String rserveProp = m_rProps.getProperty("Rserve.path");
			if (rserveProp == null || rserveProp.isEmpty()) {
				try {
					installRserve();
					m_rProps = RBinUtil.retrieveRProperties();
				} catch (IOException e) {
					RPreferenceInitializer.invalidateR3PreferenceProviderCache();
					throw new RException("Could not find and install Rserve package. "
							+ "Please install it manually in your R installation by running \"install.packages('Rserve')\".");
				}
			}
			m_connection = initRConnection();

		} catch (final InvalidRHomeException ex) {
			throw new RException("R Home is invalid", ex);
		} catch (final RserveException | IOException e) {
			throw new RException("Exception occured during R initialization.", e);
		}

		m_initialized = (m_connection != null && m_connection.get().isConnected());

		final String miniCranProp = m_rProps.getProperty("miniCRAN.path");
		if (miniCranProp == null || miniCranProp.isEmpty()) {
			try {
				installMiniCran();
				m_rProps = RBinUtil.retrieveRProperties();
			} catch (IOException e) {
				RPreferenceInitializer.invalidateR3PreferenceProviderCache();
				throw new RException("Could not find and install miniCRAN package. "
						+ "Please install it manually in your R installation by running \"install.packages('miniCRAN')\".");
			}
		}

		if (Platform.isWindows()) {
			try {
				final String rMemoryLimit = m_rProps.get("memory.limit").toString().trim();
				// set memory to the one of the used R
				eval("memory.limit(" + rMemoryLimit + ");");
			} catch (Exception e) {
				LOGGER.error("R initialisation failed. " + e.getMessage());
				throw new RuntimeException(e);
			}
		} else if (Platform.isMac()) {
			// produce a warning message if 'Cairo' package is not installed.
			try {
				final REXP ret = eval("find.package('Cairo')");
				final String cairoPath = ret.asString();

				if (cairoPath == null || cairoPath.isEmpty()) {
					// under Mac we need Cairo package to use png()/bmp() etc
					// devices.
					throw new RException("");
				}

			} catch (RException | REXPMismatchException e) {
				LOGGER.warn(
						"The package 'Cairo' needs to be installed in your R installation for bitmap graphics devices to work properly. Please install it in R using \"install.packages('Cairo')\".");
			}
		}
	}

	/**
	 * Install Rserve just in case the R environment provided by the user does
	 * not have it installed.
	 * 
	 * @throws IOException
	 */
	private void installRserve() throws IOException {
		if (Platform.isWindows()) {
			installLib("/de/bund/bfr/knime/pmm/fskx/res/Rserve_1.8-0.zip", "Rserve_1.8-0", ".zip");
		} else if (Platform.isMac()) {
			installLib("/de/bund/bfr/knime/pmm/fskx/res/Rserve_1.7-3", "Rserve_1.7-3", ".tgz");
		} else if (Platform.isLinux()) {
			installLib("/de/bund/bfr/knime/pmm/fskx/res/Rserve_1.8-5.tar.gz", "Rserve_1.8-5", ".tar.gz");
		} else {
			throw new RuntimeException("Non suppported platform, sorry." + System.getProperty("os.name"));
		}
	}

	/**
	 * Installs an R library.
	 * 
	 * @param path
	 *            Path to resource
	 * @param name
	 *            Library name, such as "triangle", "maps", etc. May include
	 *            version numbers.
	 * @param ext
	 *            Library extension. Includes dot. E.g. ".zip", ".tgz" or
	 *            ".tar.gz".
	 */
	private void installLib(String path, String name, String ext) throws IOException {
		try (InputStream is = getClass().getResourceAsStream(path)) {
			File tempFile = FileUtil.createTempFile(name, ext);
			try (FileOutputStream os = new FileOutputStream(tempFile)) {
				FileUtil.copy(is, os);
			}

			String tempPath = tempFile.getAbsolutePath();
			String rBinPath = RPreferenceInitializer.getR3Provider().getRBinPath("R");
			String cmd = rBinPath + " CMD INSTALL " + tempPath;

			Runtime.getRuntime().exec(cmd);
		}
	}

	/**
	 * Install Rserve just in case the R environment provided by the user does
	 * not have it installed.
	 * 
	 * @throws IOException
	 * @throws RException
	 */
	private void installMiniCran() throws IOException, RException {

		if (Platform.isWindows()) {
			installLib("/de/bund/bfr/knime/pmm/fskx/res/miniCRAN_0.2.5", "miniCRAN_0.2.5", ".zip");
		} else if (Platform.isMac()) {
			installLib("/de/bund/bfr/knime/pmm/fskx/res/miniCRAN_0.2.6", "miniCRAN_0.2.6", ".tgz");
		} else if (Platform.isLinux()) {
			eval("install.packages('miniCRAN', repos = 'http://cran.us.r-project.org')");
		} else {
			throw new RuntimeException("Non suppported platform, sorry." + System.getProperty("os.name"));
		}
	}

	// --- Simple Getters ---
	@Override
	public RConnection getREngine() {
		checkInitialized();
		return m_connection.get();
	}

	@Override
	public boolean isInitialized() {
		return m_initialized;
	}

	// --- R evaluation ---
	@Override
	public REXP eval(final String expr) throws RException {
		try {
			synchronized (getREngine()) {
				return getREngine().parseAndEval(expr, null, true);
			}
		} catch (REngineException e) {
			throw new RException(RException.MSG_EVAL_FAILED, e);
		}
	}

	@Override
	public REXP monitoredEval(final String expr, final ExecutionMonitor exec)
			throws RException, CanceledExecutionException {
		checkInitialized();
		try {
			return new MonitoredEval(exec).run(expr);
		} catch (Exception e) {
			throw new RException(RException.MSG_EVAL_FAILED, e);
		}
	}

	// --- Monitored Evaluation helpers ---

	/**
	 * Evaluation of R code with a monitor in a separate thread to cancel the
	 * code execution in case the execution of the node is cancelled.
	 */
	private final class MonitoredEval {

		private final int m_interval = 200;
		private final ExecutionMonitor m_exec;

		/**
		 * Constructor
		 *
		 * @param exec
		 *            for tracking progress and checking cancelled state.
		 */
		MonitoredEval(final ExecutionMonitor exec) {
			m_exec = exec;
		}

		/*
		 * Run the Callable in a thread and make sure to cancel it, in case
		 * execution is cancelled.
		 */
		private REXP monitor(final Callable<REXP> task) {
			final FutureTask<REXP> runningTask = new FutureTask<>(task);
			final Thread t = (m_useNodeContext) ? ThreadUtils.threadWithContext(runningTask, "R-Evaluation")
					: new Thread(runningTask, "R-Evaluation");
			t.start();

			try {
				while (!runningTask.isDone()) {
					Thread.sleep(m_interval);
					m_exec.checkCanceled();
				}

				return runningTask.get();
			} catch (InterruptedException | CanceledExecutionException | ExecutionException e) {
				try {
					if (!runningTask.isDone()) {
						t.interrupt();

						// The eval() call blocks somewhere in RTalk class,
						// where it waits for a socket. If we close that, we
						// should be able to force the interruption of our
						// evaluation thread.
						terminateAndRelaunch();
						// FIXME: Causes a "Socket closed" stack trace to be
						// printed. Should be cought instead, but needs to be
						// fixed in REngine first see
						// https://github.com/s-u/REngine/issues/6
					}
				} catch (final Exception e1) {
					LOGGER.warn("Could not terminate R correctly.");
				}
			}
			return null;
		}

		/**
		 * Run R code
		 *
		 * @return Result of the code
		 * @throws CanceledExecutionException
		 *             when execution was cancelled
		 * @throws IOException
		 *             when initialization of R or Rserve failed when attempting
		 *             to recover
		 */
		REXP run(final String cmd)
				throws REngineException, REXPMismatchException, CanceledExecutionException, Exception {
			final Future<REXP> future = startMonitoredThread(() -> eval(cmd));

			try {
				// wait for evaluation to complete
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				return null;
			} finally {
				// Make sure to recover in case user terminated or crashed our
				// server
				checkConnectionAndRecover();
			}
		}

		/*
		 * Execute a Callable in a monitored thread
		 */
		Future<REXP> startMonitoredThread(final Callable<REXP> task) {
			final FutureTask<REXP> ret = new FutureTask<>(() -> monitor(task));

			if (m_useNodeContext) {
				ThreadUtils.threadWithContext(ret, "R-Monitor").start();
			} else {
				new Thread(ret, "R-Monitor").start();
			}
			return ret;
		}
	}
}
