/*
 * ------------------------------------------------------------------ Copyright by KNIME GmbH,
 * Konstanz, Germany Website: http://www.knime.org; Email: contact@knime.org
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License, Version 3, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7:
 *
 * KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs. Hence, KNIME and ECLIPSE are
 * both independent programs and are not derived from each other. Should, however, the
 * interpretation of the GNU GPL Version 3 ("License") under any applicable laws result in KNIME and
 * ECLIPSE being a combined program, KNIME GMBH herewith grants you the additional permission to use
 * and propagate KNIME together with ECLIPSE with only the license terms in place for ECLIPSE
 * applying to ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the license terms of
 * ECLIPSE themselves allow for the respective use and propagation of ECLIPSE together with KNIME.
 *
 * Additional permission relating to nodes for KNIME that extend the Node Extension (and in
 * particular that are based on subclasses of NodeModel, NodeDialog, and NodeView) and that only
 * interoperate with KNIME through standard APIs ("Nodes"): Nodes are deemed to be separate and
 * independent programs and to not be covered works. Notwithstanding anything to the contrary in the
 * License, the License does not apply to Nodes, you are not required to license Nodes under the
 * License, and you are granted a license to prepare and propagate Nodes, in each case even if such
 * Nodes are propagated with or for interoperation with KNIME. The owner of a Node may freely choose
 * the license terms applicable to such Node, including when such Node is propagated with or for
 * interoperation with KNIME. ---------------------------------------------------------------------
 *
 * History 17.09.2007 (thiel): created
 */
package de.bund.bfr.knime.fsklab.r.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.knime.core.node.KNIMEConstants;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import org.knime.core.util.KNIMETimer;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.sun.jna.Platform;

import de.bund.bfr.knime.fsklab.preferences.RPreferenceInitializer;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.r.client.RController;

/**
 * RConnectionFactory
 * <p>
 * Factory for {@link RConnection} and R processes.
 *
 * @author Miguel Alba
 */
public class RConnectionFactory {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(RController.class);

  // connect to a localhost default port Rserve
  private static final boolean DEBUG_RSERVE = false;

  private static final ArrayList<RConnectionResource> m_resources = new ArrayList<>();

  private static final File tempDir;

  static {
    // create temporary directory for R
    File dir;
    try {
      // try creating a subdirectory in the KNIME temp folder to have all
      // R stuff in one place
      dir = FileUtil.createTempDir("knime-r-tmp", KNIMEConstants.getKNIMETempPath().toFile());
    } catch (final IOException e) {
      // this should never happen, but if it does, using the existing
      // KNIME temp folder directly
      // should work well enough.
      LOGGER.warn("Could not create temporary directory for R integration.", e);
      dir = KNIMEConstants.getKNIMETempPath().toFile();
    }

    tempDir = dir;
  }

  /*
   * Whether the shutdown hooks have been added. Using atomic boolean enables us to make sure we
   * only add the shutdown hooks once.
   */
  private static final AtomicBoolean m_initialized = new AtomicBoolean(false);

  /**
   * @return Configuration file for RServer
   */
  private static File createRserverConfig() {
    final File file = new File(tempDir, "Rserve.conf");
    try (FileWriter writer = new FileWriter(file)) {
      writer.write("maxinbuff 0\n"); // unlimited
      writer.write("encoding utf8\n"); // encoding for java clients
    } catch (IOException e) {
      LOGGER.warn("Could not write configuration file for Rserve.", e);
    }

    return file;
  }

  /**
   * Start an Rserve process with a given Rserve executable command.
   *
   * @param command Rserve executable command
   * @param host Host of the Rserve server
   * @param port Port to start the Rserve server on
   * @return the started Rserve process
   */
  private static Process launchRserveProcess(final String command, final String host,
      final Integer port) throws IOException {
    final String rHome = RPreferenceInitializer.getR3Provider().getRHome();

    final File configFile = createRserverConfig();
    final ProcessBuilder builder = new ProcessBuilder();
    builder.command(command, "--RS-port", port.toString(),
        "--RS-conf \"" + configFile.getAbsolutePath() + "\"", "--vanilla");

    final Map<String, String> env = builder.environment();
    if (Platform.isWindows()) {
      // on windows, the Rserve executable is not reside in the R bin
      // folder, but still requires the R.dll, so we need to put the R
      // bin folder on path

      // archProperty is "i386" for 32 bit or "x86_64" for 64 bit
      final String archProperty =
          RPreferenceInitializer.getR3Provider().getProperties().getProperty("arch");

      // "x64" for 64 bit and "i386" for 32 bit.
      final String arch = archProperty.equals("x86_64") ? "x64" : "i386";

      env.put("PATH", rHome + File.pathSeparator + rHome + "\\bin\\" + arch + "\\"
          + File.pathSeparator + env.get("PATH"));

    } else {
      // on Unix we need priorize the "R_HOME/lib" folder in the
      // LD_LIBRARY_PATH to ensure that the shared libraries of the
      // selected R installation are used.
      env.put("LD_LIBRARY_PATH",
          rHome + File.separator + "lib" + File.pathSeparator + env.get("LD_LIBRARY_PATH"));

    }
    // R HOME is required for Rserve/R to know where default libraries are
    // located
    env.put("R_HOME", rHome);

    // so that we can clean up everything RServe splits out
    env.put("TMPDIR", tempDir.getAbsolutePath());

    return builder.start();
  }

  /**
   * Attempt to start Rserve and create a connection to it.
   *
   * @param cmd command necessary to start Rserve ("Rserve.exe" on Windows)
   * @param host For creating the RConnection in RInstance. Launching a remote process, this should
   *        always be "127.0.0.1"
   * @param port Port to launch the Rserve process on.
   * @return <code>true</code> if Rserve is running or was successfully started, <code>false</code>
   * @throws IOException if Rserve could not be launched. This may be the case if R is either not
   *         found or does not have Rserve package installed.
   */
  private static RInstance launchRserve(final String command, final String host, final Integer port)
      throws IOException {
    // if debugging, launch debug version of Rserve.
    final String cmd =
        (DEBUG_RSERVE && Platform.isWindows()) ? command.replace(".exe", "_d.exe") : command;

    File commandFile = new File(cmd);
    if (!commandFile.exists()) {
      throw new IOException("Command not found: " + cmd);
    }
    if (!commandFile.canExecute()) {
      throw new IOException("Command is not an executable: " + cmd);
    }

    RInstance rInstance = null;
    try {
      final Process p = launchRserveProcess(command, host, port);

      // wrap the process, requires host and port to create RConnections
      // later.
      rInstance = new RInstance(p, host, port);

      /*
       * Consume output of process, to ensure buffer does not fill up, which blocks processes on
       * some OSs. Also, we can log errors in the external process this way.
       */
      new StreamReaderThread(p.getInputStream(), "R Output Reader (port: " + port + ")", (line) -> {
        if (DEBUG_RSERVE) {
          // intentionally print to stdout. This is only for debugging
          // and would otherwise
          // completely flood the log, which could then not be read
          // simultaneously.
          System.out.println(line);
        } /* else discard */
      }).start();
      new StreamReaderThread(p.getErrorStream(), "R Error Reader (port:" + port + ")",
          LOGGER::debug).start();

      // try connecting up to 5 times over the course of 500ms. Attempts
      // may fail if Rserve is currently starting up.
      for (int i = 1; i <= 4; i++) {
        try {
          RConnection connection = rInstance.createConnection();
          if (connection != null) {
            LOGGER.debug("Connected to Rserve in " + i + " attempts.");
            break;
          }
        } catch (RserveException e) {
          LOGGER.debug("An attempt (" + i + "/5) to connect to Rserve failed.", e);
          Thread.sleep(2 ^ i * 100);
        }
      }
      try {
        if (rInstance.getLastConnection() == null) {
          // try one last (5th) time.
          rInstance.createConnection();
        }
      } catch (RserveException e) {
        LOGGER.debug("Last attempt (5/5) to connect to Rserve failed.", e);
        throw new IOException(
            "Could not connect to RServe (host: " + host + ", port: " + port + ").");
      }

      return rInstance;
    } catch (Exception x) {
      if (rInstance != null) {
        // terminate the R process in case still running
        rInstance.close();
      }
      throw new IOException("Could not start Rserve process.", x);
    }
  }

  /**
   * Create a new {@link RConnection}, creating a new R instance beforehand, unless a connection of
   * an existing instance has been closed in which case an R instance will be reused.
   * <p>
   * The method does not check {@link RConnection#isConnected()}.
   *
   * @return an RConnectionResource which has already been acquired, never <code>null</code>
   * @throws IOException if Rserve could not be launched. This may be the case if R is either not
   *         found or does not have Rserve package installed. Or if there was no open port found.
   */
  public static RConnectionResource createConnection() throws RserveException, IOException {
    initializeShutdownHook(); // checks for re-initialization

    // synchronizing on the entire class would completely lag out KNIME for
    // some reason
    synchronized (m_resources) {
      // try to reuse an existing instance. Ensures there is max one R
      // instance per parallel executed node.
      for (RConnectionResource resource : m_resources) {
        if (resource.acquireIfAvailable()) {
          // connections are closed when released => we need to
          // reconnect
          resource.getUnderlyingRInstance().createConnection();

          return resource;
        }
      }
      // no existing resource is available. Create a new one.
      String path = RPreferenceInitializer.getR3Provider().getRServeBinPath().toString();
      int port = findFreePort();
      final RInstance instance = launchRserve(path, "127.0.0.1", port);
      RConnectionResource resource = new RConnectionResource(instance);
      resource.acquire();
      m_resources.add(resource);
      return resource;
    }
  }

  /**
   * Find a free port to launch Rserve on
   */
  private static int findFreePort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new IOException(
          "Could not find a free port for Rserve. Is KNIME not permitted to open ports?", e);
    }
  }

  /**
   * Add the Shutdown hook. Does nothing if already called once.
   */
  private static void initializeShutdownHook() {
    // if (m_initialized != false) return;
    // else m_initialized = true;
    if (m_initialized.compareAndSet(false, true)) {
      /* already initialized */
      return;
    }

    // m_initialized was false, we need to initialized.

    /** Cleanup remaining Rserve processes on VM exit. */
    Runtime.getRuntime().addShutdownHook(new Thread("R Processes Cleanup") {
      @Override
      public void run() {
        synchronized (m_resources) {
          m_resources.stream()
              .filter(resource -> resource != null && resource.getUnderlyingRInstance() != null)
              .forEach(resource -> resource.destroy(false));
        }
      }
    });

    // m_initialized already set to true in compareAndSet
  }

  /**
   * An instance of R running in an external process. The process is terminated of {@link #close()}.
   */
  private static class RInstance implements AutoCloseable {
    private final Process m_process;
    private final String m_host;
    private final int m_port;

    private RConnection m_lastConnection = null;

    /**
     * Constructor
     *
     * @param p An Rserve process
     * @param host Host on which the Rserve process is running.
     * @param port Port on which Rserve is running.
     */
    private RInstance(final Process p, final String host, final int port) {
      m_process = p;
      m_host = host;
      m_port = port;
    }

    RConnection createConnection() throws RserveException {
      m_lastConnection = new RConnection(m_host, m_port);
      return m_lastConnection;
    }

    RConnection getLastConnection() {
      return m_lastConnection;
    }

    @Override
    public void close() {

      // close connection to process, if existent
      if (m_lastConnection != null && m_lastConnection.isConnected()) {
        m_lastConnection.close();
      }

      // terminate processes the nicer way
      m_process.destroy();

      // make sure the processes really are terminated
      if (m_process.isAlive()) {
        m_process.destroyForcibly();
      }
    }

    /**
     * @return Whether this instance is up and running.
     */
    private boolean isAlive() {
      return m_process != null && m_process.isAlive();
    }
  }

  /**
   * Thread which processes an InputStream line by line via a processing function specified by the
   * user.
   */
  private static final class StreamReaderThread extends Thread {

    /**
     * Interface for functions processing input line by line.
     *
     * @author Jonathan Hale
     */
    @FunctionalInterface
    interface LineProcessor {
      void processLine(String s);
    }

    private final InputStream m_stream;
    private final LineProcessor m_processor;

    /**
     * Constructor
     *
     * @param stream to read from
     * @param name for the Thread
     * @param processor to process lines
     */
    StreamReaderThread(final InputStream stream, final String name, final LineProcessor processor) {
      super(name);
      m_stream = stream;
      m_processor = processor;
    }

    @Override
    public void run() {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(m_stream));
      try {
        String line;
        while ((line = reader.readLine()) != null && !isInterrupted()) {
          m_processor.processLine(line);
        }
      } catch (IOException e) {
        // nothing to do
      }
    }
  }

  /**
   * This class holds an RInstance and returns its RConnection of {@link RConnectionResource#get()}.
   * If released, a timeout will make sure that the underlying {@link RInstance} is shutdown after a
   * certain amount of time.
   *
   * @author Jonathan Hale
   */
  public static class RConnectionResource {

    private boolean m_available = true;
    private RInstance m_instance;
    private TimerTask m_pendingDestructionTask = null;

    private static final int RPROCESS_TIMEOUT = 60000;

    /**
     * Constructor
     *
     * @param inst RInstance which will provide the value of this resource.
     */
    private RConnectionResource(final RInstance inst) {
      if (inst == null) {
        throw new NullPointerException(
            "The RInstance provided to an RConnectionResource may not be null.");
      }

      m_instance = inst;
    }

    /**
     * Acquire ownership of this resource. Only the factory should be able to this.
     */
    private synchronized void acquire() {
      if (m_instance == null) {
        throw new NullPointerException("The resource has been destroyed already");
      }

      if (m_available) {
        doAcquire();
      } else {
        throw new IllegalStateException("Resource cannot be acquired, it is owned already.");
      }
    }

    /**
     * Acquire ownership of this resource, if it is available. Only the factory should be able to do
     * this.
     *
     * @return Whether the resource has been acquired.
     */
    private synchronized boolean acquireIfAvailable() {
      if (m_instance == null) {
        throw new NullPointerException("The resouce has been destroyed already");
      }

      if (m_available) {
        doAcquire();
        return true;
      }
      return false;
    }

    /**
     * Do everything necessary for acquiring this resource.
     */
    private void doAcquire() {
      m_available = false;

      if (m_pendingDestructionTask != null) {
        m_pendingDestructionTask.cancel();
        m_pendingDestructionTask = null;
      }
    }

    /**
     * @return The RInstance which holds this resources RConnection.
     */
    private RInstance getUnderlyingRInstance() {
      return m_instance;
    }

    /**
     * @return <code>true</code> if the resource has not been acquired yet.
     */
    public synchronized boolean isAvailable() {
      return m_available;
    }

    /**
     * Note: Always call {@link #acquire()} of {@link #acquireIfAvailable()} before using this
     * method.
     *
     * @return The value of the resource.
     * @throws IllegalAccessError If the resource has not been acquired yet.
     */
    public RConnection get() {
      if (m_available) {
        throw new IllegalAccessError(
            "Please RConnectionResource#acquire() first before calling get.");
      }
      if (m_instance == null) {
        throw new NullPointerException("The resource has been closed already");
      }

      return m_instance.getLastConnection();
    }

    /**
     * Release ownership of this resource for it to be reacquired.
     *
     * @throws RException If the RConnection could not be closed/detached
     */
    public synchronized void release() throws RException {
      if (!m_available) {
        // Either m_pendingDestructionTask is null, which means
        // this resource is being held, or the resource is available
        // and has destruction pending.
        assert m_pendingDestructionTask == null;

        m_available = true;

        if (m_instance.getLastConnection() != null
            && m_instance.getLastConnection().isConnected()) {
          // connection was not closed before release. Clean that up.
          final RConnection connection = m_instance.getLastConnection();
          try {

            // m_instance.getLastConnection().detach(); would be the
            // way to go, but...
            // FIXME: https://github.com/s-u/REngine/issues/7

            // clear workspace in the same method used in
            // RController. This is copied (!) code,
            // since considered a (hopefully) temporary option until
            // the above issue is resolved.
            if (Platform.isWindows()) {
              String b = "unloader <- function() {\n"
                  + "  defaults = getOption(\"defaultPackages\")\n"
                  + "  installed = (.packages())\n" + "  for (pkg in installed){\n"
                  + "      if (!(as.character(pkg) %in% defaults)) {\n"
                  + "          if(!(pkg == \"base\")){\n"
                  + "              package_name = paste(\"package:\", as.character(pkg), sep=\"\")\n"
                  + "              detach(package_name, character.only = TRUE)\n" + "          }\n"
                  + "      }\n" + "  }\n" + "}\n" + "unloader();\n" + "rm(list = ls());";
              // unloader function
              connection.eval(b // also includes the
              );
            } // unix automatically gets independent workspaces for
              // every connection
          } catch (RserveException e) {
            throw new RException(
                "Could not detach connection to R, could leak objects to other workspaces.", e);
          } finally {
            connection.close();
          }
        }

        m_pendingDestructionTask = new TimerTask() {
          @Override
          public void run() {
            try {
              synchronized (RConnectionResource.this) {
                if (m_available) {
                  // if not acquired in the meantime, destroy
                  // the resource
                  destroy(true);
                }
              }
            } catch (Throwable t) {
              // FIXME: There is a known bug where TimerTasks in
              // KnimeTimer can crash KNIME. We are simply making
              // 100% sure this will not happen here by catching
              // everything.
            }
          }

        };
        KNIMETimer.getInstance().schedule(m_pendingDestructionTask, RPROCESS_TIMEOUT);
      } // else: release had been called already, but we allow this.
    }

    /**
     * Destroy the underlying resource.
     *
     * @param remove Whether to automatically remove this resource from m_resources.
     */
    public synchronized void destroy(final boolean remove) {
      if (m_instance == null) {
        throw new NullPointerException("The resource has been destroyed already.");
      }

      m_available = false;
      m_instance.close();

      if (remove) {
        synchronized (m_resources) {
          m_resources.remove(this);
        }
      }
      m_instance = null;

      // cleanup TimerTask
      if (m_pendingDestructionTask != null) {
        m_pendingDestructionTask.cancel();
        m_pendingDestructionTask = null;
      }
    }

    /**
     * @return whether the underlying RInstance is up and running.
     */
    public boolean isRInstanceAlive() {
      return m_instance != null && m_instance.isAlive();
    }

  }

}
