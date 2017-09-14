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
package de.bund.bfr.knime.fsklab.nodes.controller

import com.sun.jna.Platform
import de.bund.bfr.knime.fsklab.nodes.rbin.RBinUtil
import de.bund.bfr.knime.fsklab.nodes.rbin.RBinUtil.InvalidRHomeException
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceInitializer
import de.bund.bfr.knime.fsklab.nodes.rserve.RConnectionFactory
import org.knime.core.node.CanceledExecutionException
import org.knime.core.node.ExecutionMonitor
import org.knime.core.node.NodeLogger
import org.knime.core.util.FileUtil
import org.knime.core.util.ThreadUtils
import org.rosuda.REngine.REXP
import org.rosuda.REngine.REXPMismatchException
import org.rosuda.REngine.REngineException
import org.rosuda.REngine.Rserve.RConnection
import org.rosuda.REngine.Rserve.RserveException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

/**
 * RController.
 *
 * This class manages some way of communicating with R, executing R code and
 * moving data back and forth.
 *
 * Currently, this class is a singleton and enforces mutual exclusion.
 */
class RController
/**
 * Constructor. Calls [.initialize]. To avoid initialization, use
 * [.RController].
 */
@Throws(IRController.RException::class)
constructor() : IRController {

    private val LOGGER = NodeLogger.getLogger(javaClass)

    private var m_connection: RConnectionFactory.RConnectionResource? = null

    private var m_rProps: Properties? = null

    private var m_initialized = false
    private val m_useNodeContext = false

    init {
        initialize()
    }

    // --- Initialization & RConnection lifecycle ---
    @Throws(IRController.RException::class)
    override fun initialize() {
        initR()
    }

    override fun isInitialized(): Boolean = m_initialized

    /**
     * Check if the RController is initialized and throws [IRController.RControllerNotInitializedException] if not.
     */
    private fun checkInitialized() {
        if (!m_initialized || m_connection == null) {
            throw IRController.RControllerNotInitializedException()
        }
        if (m_connection?.isRInstanceAlive == false) {
            throw RuntimeException("RServe process terminated unexpectedly")
        }
        if (m_connection?.isAvailable == true) {
            // resource should never be available, if held by this RController
            // Available means available to acquire for other RControllers.
            throw RuntimeException("Invalid resource state: lost ownership of connection resource.")
        }
    }

    @Throws(IRController.RException::class)
    override fun close() {
        if (m_connection != null) {
            m_connection!!.release()
            m_connection = null
        }

        m_initialized = false
    }

    /**
     * Terminate and relaunch the R process this controller is connected to.
     * This is currently the only way to interrupt command execution.
     */
    @Throws(Exception::class)
    private fun terminateAndRelaunch() {
        LOGGER.debug("Terminate R process")

        terminateRProcess()

        try {
            m_connection = initRConnection()
            m_initialized = m_connection != null && m_connection!!.get()!!.isConnected
        } catch (e: Exception) {
            throw Exception("Initializing R with Rserve failed")
        }

    }

    /**
     * Terminate the R process started for this RController.
     */
    private fun terminateRProcess() {
        if (m_connection != null) {
            m_connection!!.destroy(true)
        }

        m_initialized = false
    }

    /**
     * Check if the connection is still valid and recover if not.
     */
    @Throws(Exception::class)
    private fun checkConnectionAndRecover() {
        if (m_connection?.get()?.isConnected == true && m_connection?.isRInstanceAlive == true) {
            // connection is fine
            return
        }

        // all of the session data has been lost. We cannot recover from that.
        terminateAndRelaunch()
    }

    /**
     * Create and initialize a R connection
     *
     * @return the new RConnection
     */
    @Throws(RserveException::class, IOException::class)
    private fun initRConnection(): RConnectionFactory.RConnectionResource {
        val resource = RConnectionFactory.createConnection()

        if (resource.get()?.isConnected == false) {
            throw IOException("Could not initialize RController: Resource was not connected")
        }

        return resource
    }

    /**
     * Initialize the underlying REngine with a backend.
     *
     * @throws IOException
     */
    @Throws(IRController.RException::class)
    private fun initR() {
        try {
            val rHome = RPreferenceInitializer.r3Provider.rHome
            RBinUtil.checkRHome(rHome)

            m_rProps = RBinUtil.retrieveRProperties()

            if (m_rProps?.containsKey("major") == false) {
                throw IRController.RException(
                        "Cannot determine major version of R. Please check the R installation defined in the KNIME preferences.")
            }

            if (m_rProps?.getProperty("Rserve.path").isNullOrEmpty()) {
                m_rProps = try {
                    installRserve()
                    RBinUtil.retrieveRProperties()
                } catch (e: IOException) {
                    RPreferenceInitializer.invalidateR3PreferenceProviderCache()
                    throw IRController.RException("Could not find and install Rserve package. Please install it manually in your R installation by running \"install.packages('Rserve')\".")
                }

            }
            m_connection = initRConnection()

        } catch (ex: InvalidRHomeException) {
            throw IRController.RException("R Home is invalid", ex)
        } catch (e: RserveException) {
            throw IRController.RException("Exception occured during R initialization.", e)
        } catch (e: IOException) {
            throw IRController.RException("Exception occured during R initialization.", e)
        }

        m_initialized = m_connection?.get()?.isConnected ?: m_initialized

        if (m_rProps?.getProperty("miniCRAN.path").isNullOrEmpty()) {
            m_rProps = try {
                installMiniCran()
                RBinUtil.retrieveRProperties()
            } catch (e: IOException) {
                RPreferenceInitializer.invalidateR3PreferenceProviderCache()
                throw IRController.RException("Could not find and install miniCRAN package. Please install it manually in your R installation by running \"install.packages('miniCRAN')\".")
            }
        }

        if (Platform.isWindows()) {
            try {
                val rMemoryLimit = m_rProps!!["memory.limit"].toString().trim { it <= ' ' }
                // set memory to the one of the used R
                eval("memory.limit($rMemoryLimit);")
            } catch (e: Exception) {
                LOGGER.error("R initialisation failed. " + e.message)
                throw RuntimeException(e)
            }

        } else if (Platform.isMac()) {
            // produce a warning message if 'Cairo' package is not installed.
            try {
                val ret = eval("find.package('Cairo')")
                if (ret.asString().isNullOrEmpty()) {
                    // under Mac we need Cairo package to use png()/bmp() etc
                    // devices.
                    throw IRController.RException("")
                }

            } catch (e: IRController.RException) {
                LOGGER.warn(
                        "The package 'Cairo' needs to be installed in your R installation for bitmap graphics devices to work properly. Please install it in R using \"install.packages('Cairo')\".")
            } catch (e: REXPMismatchException) {
                LOGGER.warn("The package 'Cairo' needs to be installed in your R installation for bitmap graphics devices to work properly. Please install it in R using \"install.packages('Cairo')\".")
            }

        }
    }

    /**
     * Install Rserve just in case the R environment provided by the user does
     * not have it installed.
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun installRserve() {
        when {
            Platform.isWindows() -> installLib("/de/bund/bfr/knime/pmm/fskx/res/Rserve_1.8-0.zip", "Rserve_1.8-0", ".zip")
            Platform.isMac() -> installLib("/de/bund/bfr/knime/pmm/fskx/res/Rserve_1.7-3", "Rserve_1.7-3", ".tgz")
            Platform.isLinux() -> installLib("/de/bund/bfr/knime/pmm/fskx/res/Rserve_1.8-5.tar.gz", "Rserve_1.8-5", ".tar.gz")
            else -> throw RuntimeException("Non suppported platform, sorry. ${System.getProperty("os.name")}")
        }
    }

    /**
     * Installs an R library.
     *
     * @param path Path to resource
     * @param name Library name, such as "triangle", "maps", etc. May include version numbers.
     * @param ext Library extension. Includes dot. E.g. ".zip", ".tgz" or ".tar.gz".
     */
    @Throws(IOException::class)
    private fun installLib(path: String, name: String, ext: String) {
        javaClass.getResourceAsStream(path).use { inputStream ->
            val tempFile = FileUtil.createTempFile(name, ext)
            FileOutputStream(tempFile).use { outputStream -> FileUtil.copy(inputStream, outputStream) }

            val tempPath = tempFile.absolutePath
            val rBinPath = RPreferenceInitializer.r3Provider.getRBinPath("R")
            val cmd = "$rBinPath CMD INSTALL $tempPath"

            Runtime.getRuntime().exec(cmd)
        }
    }

    /**
     * Install Rserve just in case the R environment provided by the user does
     * not have it installed.
     *
     * @throws IOException
     * @throws [IRController.RException]
     */
    @Throws(IOException::class, IRController.RException::class)
    private fun installMiniCran() {

        when {
            Platform.isWindows() -> installLib("/de/bund/bfr/knime/pmm/fskx/res/miniCRAN_0.2.5", "miniCRAN_0.2.5", ".zip")
            Platform.isMac() -> installLib("/de/bund/bfr/knime/pmm/fskx/res/miniCRAN_0.2.6", "miniCRAN_0.2.6", ".tgz")
            Platform.isLinux() -> eval("install.packages('miniCRAN', repos = 'http://cran.us.r-project.org')")
            else -> throw RuntimeException("Non suppported platform, sorry. ${System.getProperty("os.name")}")
        }
    }

    // --- Simple Getters ---
    override val rEngine: RConnection?
        get() {
            checkInitialized()
            return m_connection?.get()
        }

    // --- R evaluation ---
    @Throws(IRController.RException::class)
    override fun eval(expr: String): REXP {
        try {
            rEngine!!.let {
                synchronized(it) {
                    return it.parseAndEval(expr, null, true)
                }
            }
        } catch (e: REngineException) {
            throw IRController.RException(IRController.RException.MSG_EVAL_FAILED, e)
        } catch (exception: NullPointerException) {
            // Thrown by !! when rEngine is null
            throw IRController.RException(IRController.RException.MSG_EVAL_FAILED, exception)
        }
    }

    @Throws(IRController.RException::class, CanceledExecutionException::class)
    override fun monitoredEval(cmd: String, exec: ExecutionMonitor): REXP {
        checkInitialized()
        return try {
            MonitoredEval(exec).run(cmd) ?: throw IRController.RException(IRController.RException.MSG_EVAL_FAILED)
        } catch (e: Exception) {
            throw IRController.RException(IRController.RException.MSG_EVAL_FAILED, e)
        }

    }

    // --- Monitored Evaluation helpers ---

    /**
     * Evaluation of R code with a monitor in a separate thread to cancel the
     * code execution in case the execution of the node is cancelled.
     */
    private inner class MonitoredEval
    /**
     * Constructor
     *
     * @param m_exec for tracking progress and checking cancelled state.
     */
    internal constructor(private val m_exec: ExecutionMonitor) {

        private val m_interval = 200

        /*
		 * Run the Callable in a thread and make sure to cancel it, in case
		 * execution is cancelled.
		 */
        private fun monitor(task: () -> REXP): REXP? {
            val runningTask = FutureTask(task)
            val t = if (m_useNodeContext)
                ThreadUtils.threadWithContext(runningTask, "R-Evaluation")
            else
                Thread(runningTask, "R-Evaluation")
            t.start()

            try {
                while (!runningTask.isDone) {
                    Thread.sleep(m_interval.toLong())
                    m_exec.checkCanceled()
                }

                return runningTask.get()
            } catch (e: InterruptedException) {
                try {
                    if (!runningTask.isDone) {
                        t.interrupt()

                        // The eval() call blocks somewhere in RTalk class,
                        // where it waits for a socket. If we close that, we
                        // should be able to force the interruption of our
                        // evaluation thread.
                        terminateAndRelaunch()
                        // FIXME: Causes a "Socket closed" stack trace to be
                        // printed. Should be cought instead, but needs to be
                        // fixed in REngine first see
                        // https://github.com/s-u/REngine/issues/6
                    }
                } catch (e1: Exception) {
                    LOGGER.warn("Could not terminate R correctly.")
                }

            } catch (e: CanceledExecutionException) {
                try {
                    if (!runningTask.isDone) {
                        t.interrupt()
                        terminateAndRelaunch()
                    }
                } catch (e1: Exception) {
                    LOGGER.warn("Could not terminate R correctly.")
                }

            } catch (e: ExecutionException) {
                try {
                    if (!runningTask.isDone) {
                        t.interrupt()
                        terminateAndRelaunch()
                    }
                } catch (e1: Exception) {
                    LOGGER.warn("Could not terminate R correctly.")
                }

            }

            return null
        }

        /**
         * Run R code
         *
         * @return Result of the code
         * @throws CanceledExecutionException
         * when execution was cancelled
         * @throws IOException
         * when initialization of R or Rserve failed when attempting
         * to recover
         */
        @Throws(REngineException::class, REXPMismatchException::class, CanceledExecutionException::class, Exception::class)
        internal fun run(cmd: String): REXP? {
            val future = startMonitoredThread({ eval(cmd) })

            return try {
                // wait for evaluation to complete
                future.get()
            } catch (e: InterruptedException) {
                null
            } catch (e: ExecutionException) {
                null
            } finally {
                // Make sure to recover in case user terminated or crashed our
                // server
                checkConnectionAndRecover()
            }
        }

        /*
		 * Execute a Callable in a monitored thread
		 */
        internal fun startMonitoredThread(task: () -> REXP): Future<REXP> {
            val ret = FutureTask<REXP> { monitor(task) }

            if (m_useNodeContext) {
                ThreadUtils.threadWithContext(ret, "R-Monitor").start()
            } else {
                Thread(ret, "R-Monitor").start()
            }
            return ret
        }
    }
}
