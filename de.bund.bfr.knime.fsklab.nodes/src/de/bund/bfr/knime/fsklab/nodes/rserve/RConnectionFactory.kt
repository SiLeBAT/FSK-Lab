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
package de.bund.bfr.knime.fsklab.nodes.rserve

import com.sun.jna.Platform
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException
import de.bund.bfr.knime.fsklab.nodes.controller.RController
import de.bund.bfr.knime.fsklab.nodes.rbin.preferences.RPreferenceInitializer
import org.knime.core.node.KNIMEConstants
import org.knime.core.node.NodeLogger
import org.knime.core.util.FileUtil
import org.knime.core.util.KNIMETimer
import org.rosuda.REngine.Rserve.RConnection
import org.rosuda.REngine.Rserve.RserveException
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.ServerSocket
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * RConnectionFactory
 *
 * Factory for [RConnection] and R processes.
 *
 * @author Miguel Alba
 */
object RConnectionFactory {

    private val LOGGER = NodeLogger.getLogger(RController::class.java)

    // connect to a localhost default port Rserve
    private val DEBUG_RSERVE = true

    private val m_resources = ArrayList<RConnectionResource>()

    private val tempDir: File

    init {
        // create temporary directory for R
        tempDir = try {
            // try creating a subdirectory in the KNIME temp folder to have all the R stuff in one place
            FileUtil.createTempDir("knime-r-tmp", KNIMEConstants.getKNIMETempPath().toFile())
        } catch (exception: IOException) {
            // this should never happen, but if it does, using the existing KNIME temp folder directly should work
            // well enough
            LOGGER.warn("Could not create temporary directory for R integration.", exception)
            KNIMEConstants.getKNIMETempPath().toFile()
        }
    }

    /*
	 * Whether the shutdown hooks have been added. Using atomic boolean enables
	 * us to make sure we only add the shutdown hooks once.
	 */
    private val m_initialized = AtomicBoolean(false)

    /**
     * @return Configuration file for RServer
     */
    private fun createRserverConfig(): File {
        val file = File(tempDir, "Rserve.conf")
        try {
            FileWriter(file).use { writer ->
                writer.write("maxinbuff 0\n") // unlimited
                writer.write("encoding utf8\n") // encoding for java clients
            }
        } catch (e: IOException) {
            LOGGER.warn("Could not write configuration file for Rserve.", e)
        }

        return file
    }

    /**
     * Start an Rserve process with a given Rserve executable command.
     *
     * @param command Rserve executable command
     * @param host Host of the Rserve server
     * @param port Port to start the Rserve server on
     * @return the started Rserve process
     */
    @Throws(IOException::class)
    private fun launchRserveProcess(command: String, host: String, port: Int?): Process {
        val rHome = RPreferenceInitializer.r3Provider.rHome

        val configFile = createRserverConfig()
        val builder = ProcessBuilder()
        builder.command(command, "--RS-port", port!!.toString(), "--RS-conf \"${configFile.absolutePath}\"", "--vanilla")

        val env = builder.environment()
        if (Platform.isWindows()) {
            // on windows, the Rserve executable is not reside in the R bin
            // folder, but still requires the R.dll, so we need to put the R
            // bin folder on path
            env.put("PATH", rHome + File.pathSeparator + rHome
                    + (if (Platform.is64Bit()) "\\bin\\x64\\" else "\\bin\\i386\\") + File.pathSeparator + env["PATH"])
        } else {
            // on Unix we need priorize the "R_HOME/lib" folder in the
            // LD_LIBRARY_PATH to ensure that the shared libraries of the
            // selected R installation are used.
            env.put("LD_LIBRARY_PATH",
                    rHome + File.separator + "lib" + File.pathSeparator + env["LD_LIBRARY_PATH"])

        }
        // R HOME is required for Rserve/R to know where default libraries are located
        env.put("R_HOME", rHome)

        // so that we can clean up everything RServe splits out
        env.put("TMPDIR", tempDir.absolutePath)

        return builder.start()
    }

    /**
     * Attempt to start Rserve and create a connection to it.
     *
     * @param command command necessary to start Rserve ("Rserve.exe" on Windows)
     * @param host For creating the RConnection in RInstance. Launching a remote process, this should always be
     *              "127.0.0.1"
     * @param port Port to launch the Rserve process on.
     * @return `true` if Rserve is running or was successfully started, `false`
     * @throws IOException if Rserve could not be launched. This may be the case if R is either not found or does not
     *              have Rserve package installed.
     */
    @Throws(IOException::class)
    private fun launchRserve(command: String, host: String, port: Int?): RInstance {
        // if debugging, launch debug version of Rserve.
        val cmd = if (DEBUG_RSERVE && Platform.isWindows()) command.replace(".exe", "_d.exe") else command

        val commandFile = File(cmd)
        if (!commandFile.exists()) {
            throw IOException("Command not found: $cmd")
        }
        if (!commandFile.canExecute()) {
            throw IOException("Command is not an executable: $cmd")
        }

        var rInstance: RInstance? = null
        try {
            val p = launchRserveProcess(command, host, port)

            // wrap the process, requires host and port to create RConnections
            // later.
            rInstance = RInstance(p, host, port!!)

            /*
             * Consume output of process, to ensure buffer does not fill up,
             * which blocks processes on some OSs. Also, we can log errors in
             * the external process this way.
             */
            thread(name = "R Output Reader (port: $port)") {
                try {
                    // intentionally print to stdout. This is only for debugging and would otherwise completely
                    // flood the log, which could then not be read simultaneously
                    p.inputStream.bufferedReader().forEachLine { if (DEBUG_RSERVE) println(it) }
                } catch (exception: IOException) {
                }  // Nothing to do
            }

            thread(name = "R Error Reader (port: $port)") {
                try {
                    p.inputStream.bufferedReader().forEachLine { LOGGER.debug(it) }
                } catch (exception: IOException) {
                }  // Nothing to do
            }

            // try connecting up to 5 times over the course of 500ms. Attempts
            // may fail if Rserve is currently starting up.
            for (i in 1..4) {
                try {
                    val connection = rInstance.createConnection()
                    if (connection != null) {
                        LOGGER.debug("Connected to Rserve in $i attempts.")
                        break
                    }
                } catch (e: RserveException) {
                    LOGGER.debug("An attempt ($i/5) to connect to Rserve failed.", e)
                    Thread.sleep((2 xor i * 100).toLong())
                }

            }
            try {
                if (rInstance.lastConnection == null) {
                    // try one last (5th) time.
                    rInstance.createConnection()
                }
            } catch (e: RserveException) {
                LOGGER.debug("Last attempt (5/5) to connect to Rserve failed.", e)
                throw IOException("Could not connect to RServe (host: $host, port: $port).")
            }

            return rInstance
        } catch (x: Exception) {
            if (rInstance != null) {
                // terminate the R process in case still running
                rInstance.close()
            }
            throw IOException("Could not start Rserve process.", x)
        }

    }

    /**
     * Create a new [RConnection], creating a new R instance beforehand,
     * unless a connection of an existing instance has been closed in which case
     * an R instance will be reused.
     *
     *
     * The method does not check [RConnection.isConnected].
     *
     * @return an RConnectionResource which has already been acquired, never
     * `null`
     * @throws IOException
     * if Rserve could not be launched. This may be the case if R is
     * either not found or does not have Rserve package installed.
     * Or if there was no open port found.
     */
    @Throws(RserveException::class, IOException::class)
    fun createConnection(): RConnectionResource {
        initializeShutdownHook() // checks for re-initialization

        // synchronizing on the entire class would completely lag out KNIME for
        // some reason
        synchronized(m_resources) {
            // try to reuse an existing instance. Ensures there is max one R
            // instance per parallel executed node.
            for (resource in m_resources) {
                if (resource.acquireIfAvailable()) {
                    // connections are closed when released => we need to
                    // reconnect
                    resource.underlyingRInstance!!.createConnection()

                    return resource
                }
            }
            // no existing resource is available. Create a new one.
            val path = RPreferenceInitializer.r3Provider.getRServeBinPath()
            val port = findFreePort()
            val instance = launchRserve(path, "127.0.0.1", port)
            val resource = RConnectionResource(instance)
            resource.acquire()
            m_resources.add(resource)
            return resource
        }
    }

    /**
     * Find a free port to launch Rserve on
     */
    @Throws(IOException::class)
    private fun findFreePort(): Int {
        try {
            ServerSocket(0).use { socket -> return socket.localPort }
        } catch (e: IOException) {
            throw IOException("Could not find a free port for Rserve. Is KNIME not permitted to open ports?", e)
        }

    }

    /**
     * Add the Shutdown hook. Does nothing if already called once.
     */
    private fun initializeShutdownHook() {
        // if (m_initialized != false) return;
        // else m_initialized = true;
        if (m_initialized.compareAndSet(false, true)) {
            /* already initialized */
            return
        }

        // m_initialized was false, we need to initialized.

        /** Cleanup remaining Rserve processes on VM exit.  */
        Runtime.getRuntime().addShutdownHook(object : Thread("R Processes Cleanup") {
            override fun run() {
                synchronized(m_resources) {
                    m_resources.stream()
                            .filter { it?.underlyingRInstance != null }
                            .forEach { it.destroy(false) }
                }
            }
        })

        // m_initialized already set to true in compareAndSet
    }

    /**
     * This class holds an RInstance and returns its RConnection of
     * [RConnectionResource.get]. If released, a timeout will make sure
     * that the underlying [RInstance] is shutdown after a certain amount
     * of time.
     *
     * @property underlyingRInstance The RInstance which holds this resources RConnection.
     *
     * @author Jonathan Hale
     */
    class RConnectionResource constructor(var underlyingRInstance: RInstance?) {

        /** @return `true` if the resource has not been acquired yet. */
        @get:Synchronized
        var isAvailable = true
            private set

        private var m_pendingDestructionTask: TimerTask? = null

        init {
            if (underlyingRInstance == null) {
                throw NullPointerException("The RInstance provided to an RConnectionResource may not be null.")
            }
        }

        /**
         * Acquire ownership of this resource. Only the factory should be able
         * to this.
         */
        @Synchronized internal fun acquire() {
            if (underlyingRInstance == null) {
                throw NullPointerException("The resource has been destroyed already")
            }

            if (isAvailable) {
                doAcquire()
            } else {
                throw IllegalStateException("Resource cannot be acquired, it is owned already.")
            }
        }

        /**
         * Acquire ownership of this resource, if it is available. Only the
         * factory should be able to do this.
         *
         * @return Whether the resource has been acquired.
         */
        @Synchronized internal fun acquireIfAvailable(): Boolean {
            if (underlyingRInstance == null) {
                throw NullPointerException("The resouce has been destroyed already")
            }

            if (isAvailable) {
                doAcquire()
                return true
            }
            return false
        }

        /**
         * Do everything necessary for acquiring this resource.
         */
        private fun doAcquire() {
            isAvailable = false

            if (m_pendingDestructionTask != null) {
                m_pendingDestructionTask!!.cancel()
                m_pendingDestructionTask = null
            }
        }

        /**
         * Note: Always call [.acquire] of [.acquireIfAvailable]
         * before using this method.
         *
         * @return The value of the resource.
         * @throws IllegalAccessError
         * If the resource has not been acquired yet.
         */
        fun get(): RConnection? {
            if (isAvailable) {
                throw IllegalAccessError("Please RConnectionResource#acquire() first before calling get.")
            }
            return underlyingRInstance?.lastConnection ?: throw NullPointerException("The resource has been closed already")
        }

        /**
         * Release ownership of this resource for it to be reacquired.
         *
         * @throws RException
         * If the RConnection could not be closed/detached
         */
        @Synchronized
        @Throws(RException::class)
        fun release() {
            if (!isAvailable) {
                // Either m_pendingDestructionTask is null, which means
                // this resource is being held, or the resource is available
                // and has destruction pending.
                assert(m_pendingDestructionTask == null)

                isAvailable = true

                if (underlyingRInstance!!.lastConnection != null && underlyingRInstance!!.lastConnection!!.isConnected) {
                    // connection was not closed before release. Clean that up.
                    val connection = underlyingRInstance!!.lastConnection
                    try {

                        // m_instance.getLastConnection().detach(); would be the
                        // way to go, but...
                        // FIXME: https://github.com/s-u/REngine/issues/7

                        // clear workspace in the same method used in
                        // RController. This is copied (!) code,
                        // since considered a (hopefully) temporary option until
                        // the above issue is resolved.
                        if (Platform.isWindows()) {
                            // unloader function
                            val b = """
                                | unloader <- function() {\n
                                |   defaults = getOption(\"defaultPackages\")\n
                                |   installed = (.packages())\n
                                |   for (pkg in installed){\n
                                |     if (!as.character(pkg) %in% defaults)) {\n
                                |       if (!(pkg == \"base\")){\n
                                |         package_name = paste(\"package:\", as.character(pkg), sep=\"\")\n"
                                |         detach(package_name, character.only = TRUE)\n"
                                |       }\n
                                |     }\n
                                |   }\n
                                | }\n
                                | unloader();\n
                                | rm(list = ls());
                                """.trimMargin()
                            connection!!.eval(b // also includes the
                            )
                        }
                        // unix automatically gets independent workspaces for every connection
                    } catch (e: RserveException) {
                        throw RException(
                                "Could not detach connection to R, could leak objects to other workspaces.", e)
                    } finally {
                        connection!!.close()
                    }
                }

                m_pendingDestructionTask = object : TimerTask() {
                    override fun run() {
                        try {
                            synchronized(this@RConnectionResource) {
                                if (isAvailable) {
                                    // if not acquired in the meantime, destroy
                                    // the resource
                                    destroy(true)
                                }
                            }
                        } catch (t: Throwable) {
                            // FIXME: There is a known bug where TimerTasks in
                            // KnimeTimer can crash KNIME. We are simply making
                            // 100% sure this will not happen here by catching
                            // everything.
                        }

                    }

                }
                KNIMETimer.getInstance().schedule(m_pendingDestructionTask!!, RPROCESS_TIMEOUT.toLong())
            } // else: release had been called already, but we allow this.
        }

        /**
         * Destroy the underlying resource.
         *
         * @param remove
         * Whether to automatically remove this resource from
         * m_resources.
         */
        @Synchronized
        fun destroy(remove: Boolean) {
            if (underlyingRInstance == null) {
                throw NullPointerException("The resource has been destroyed already.")
            }

            isAvailable = false
            underlyingRInstance!!.close()

            if (remove) {
                synchronized(m_resources) {
                    m_resources.remove(this)
                }
            }
            underlyingRInstance = null

            // cleanup TimerTask
            if (m_pendingDestructionTask != null) {
                m_pendingDestructionTask!!.cancel()
                m_pendingDestructionTask = null
            }
        }

        /**
         * @return whether the underlying RInstance is up and running.
         */
        val isRInstanceAlive: Boolean
            get() = underlyingRInstance != null && underlyingRInstance!!.isAlive

        companion object {
            private val RPROCESS_TIMEOUT = 60000
        }

    }
}

/**
 * An instance of R running in an external process. The process is
 * terminated of [.close].
 *
 * @param process An Rserve process
 * @param host Host on which the Rserve process is running
 * @param port Port on which Rserve is running
 */
class RInstance(private val process: Process?, private val host: String, private val port: Int)
    : AutoCloseable {

    internal var lastConnection: RConnection? = null
        private set

    @Throws(RserveException::class)
    internal fun createConnection(): RConnection {
        val newConnection = RConnection(host, port)
        lastConnection = newConnection

        return newConnection
    }

    override fun close() {

        // close connection to process, if existent
        lastConnection?.let { if (it.isConnected) it.close() }

        // terminate processes the nicer way
        process!!.destroy()

        // make sure the processes really are terminated
        if (process.isAlive) {
            process.destroyForcibly()
        }
    }

    /** @return Whether this instance is up and running. */
    internal val isAlive: Boolean
        get() = process?.isAlive == true
}
