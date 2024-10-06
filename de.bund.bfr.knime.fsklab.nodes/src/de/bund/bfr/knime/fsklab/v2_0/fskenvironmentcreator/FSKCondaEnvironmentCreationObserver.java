package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.knime.conda.Conda;
import org.knime.conda.CondaCanceledExecutionException;
import org.knime.conda.CondaEnvironmentCreationMonitor;
import org.knime.conda.CondaEnvironmentIdentifier;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.util.Version;




public class FSKCondaEnvironmentCreationObserver {

    private static final String IN_PROGRESS_MESSAGE = "Creating Conda environment...";
    
    private static final String DEFAULT_PYTHON2_ENV_PREFIX = "py2_fsk";

    private static final String DEFAULT_PYTHON3_ENV_PREFIX = "py3_fsk";
    
    private static final String DEFAULT_R3_ENV_PREFIX = "R3_fsk";

    private static final String DEFAULT_R4_ENV_PREFIX = "R4_fsk";

    private final CopyOnWriteArrayList<CondaEnvironmentCreationStatusListener> m_listeners =
        new CopyOnWriteArrayList<>();

    private final CondaEnvVersion m_CondaEnvVersion;

    private CondaEnvironmentCreationMonitor m_currentCreationMonitor;

    // Not meant for saving/loading. We just want observable values here to communicate with the view:

    private static final String DUMMY_CFG_KEY = "dummy";

    private final SettingsModelBoolean m_environmentCreationEnabled = new SettingsModelBoolean(DUMMY_CFG_KEY, true);

    /**
     * The created instance is {@link #getIsEnvironmentCreationEnabled() disabled by default}.
     *
     * @param environmentCondaEnvVersion The Python version of the Conda environments created by this instance.
     */
    public FSKCondaEnvironmentCreationObserver(CondaEnvVersion environmentCondaEnvVersion) {
        m_CondaEnvVersion = environmentCondaEnvVersion;
    }

    /**
     * @return The Conda version of the environments created by this instance.
     */
    public CondaEnvVersion getCondaEnvVersion() {
        return m_CondaEnvVersion;
    }

    /**
     * @return The enabled state of this instance. Only enabled instances (i.e., instances whose enabled model returns
     *         {@link SettingsModelBoolean#getBooleanValue() <code>true</code>}) are able to
     *         {@link #startEnvironmentCreation(String, CondaEnvironmentCreationStatus) initiate environment creation}.
     */
    public SettingsModelBoolean getIsEnvironmentCreationEnabled() {
        return m_environmentCreationEnabled;
    }

    /**
     * @param suffix a suffix for the environment name. Can be empty.
     * @return The default environment name for the next environment created by this instance. Returns an empty string
     *         in case calling Conda failed.<br>
     *         Note that this method makes no guarantees about the uniqueness of the returned name if invoked in
     *         parallel to an ongoing environment creation process.
     */
    protected String getDefaultEnvironmentName(final String suffix) {
        try {
            final Conda conda = new Conda();
            conda.testInstallation();
            final String defaultEnvironmentName;
            if (m_CondaEnvVersion.equals(CondaEnvVersion.PYTHON2)) {
                defaultEnvironmentName = "FSK_"+CondaEnvVersion.PYTHON2.getId()+"_"+ suffix;
            } else if (m_CondaEnvVersion.equals(CondaEnvVersion.PYTHON3)) {
                defaultEnvironmentName = "FSK_"+CondaEnvVersion.PYTHON3.getId()+"_"+ suffix;
            } else if (m_CondaEnvVersion.equals(CondaEnvVersion.R3)) {
                defaultEnvironmentName = "FSK_"+CondaEnvVersion.R3.getId()+"_"+ suffix;
            } else if (m_CondaEnvVersion.equals(CondaEnvVersion.R4)) {
                defaultEnvironmentName = "FSK_"+CondaEnvVersion.R4.getId()+"_"+ suffix;
            }else {
                throw new IllegalStateException("Python version '" + m_CondaEnvVersion
                    + "' is neither Python 2 nor Python " + "3 and R version "+m_CondaEnvVersion+ "is neither R 3 nor R 4."
                        + " This is an implementation error.");
            }
            return defaultEnvironmentName;
        } catch (final Exception ex) {
            return "";
        }
    }

    /**
     * Initiates a new Conda environment creation process. Only allowed if this instance is
     * {@link #getIsEnvironmentCreationEnabled() enabled}.
     *
     * @param environmentName The name of the environment. Must not already exist in the local Conda installation. May
     *            be {@code null} or empty in which case a unique default name is used.
     * @param pathToEnvFile The path to the environment definition file. If {@code null}, {@code CondaEnvVersion} is
     *            consulted to find a predefined environment definition.
     * @param CondaEnvVersion The conda environment version of the environment. Must match a version for which a predefined
     *            environment file is available. Ignored if {@code pathToEnvFile} is not {@code null} (in which case
     *            {@code null} can be passed here).
     * @param status The status object that is will be notified about changes in the state of the initiated creation
     *            process. Can also be used to {@link #cancelEnvironmentCreation(CondaEnvironmentCreationStatus) cancel}
     *            the creation process. A new status object must be used for each new creation process.
     */
    protected synchronized void startEnvironmentCreation(final String environmentName, final String pathToEnvFile,
        final Version CondaEnvVersion, final CondaEnvironmentCreationStatus status) {
        if (!m_environmentCreationEnabled.getBooleanValue()) {
            throw new IllegalStateException("Environment creation is not enabled.");
        }
        if (m_currentCreationMonitor != null) {
            throw new IllegalStateException("Environment generation was tried to be started although one is already in "
                + "progress. This is currently not supported and therefore an implementation error.");
        }
        m_currentCreationMonitor = new StateUpdatingCondaEnvironmentCreationMonitor(status);
        new Thread(() -> {
            try {
                onEnvironmentCreationStarting(status);
                final Conda conda = new Conda();
                conda.testInstallation();
                final CondaEnvironmentIdentifier createdEnvironment;
                if (pathToEnvFile != null) {
                    createdEnvironment = createEnvironmentFromFile(conda, m_CondaEnvVersion,
                        pathToEnvFile, environmentName, m_currentCreationMonitor);
                } else {
                    createdEnvironment = createDefaultEnvironment(conda, environmentName,
                        CondaEnvVersion, m_currentCreationMonitor);
                }
                onEnvironmentCreationFinished(status, createdEnvironment);
            } catch (final CondaCanceledExecutionException ex) {
                onEnvironmentCreationCanceled(status);
            } catch (final Exception ex) {
                NodeLogger.getLogger(FSKCondaEnvironmentCreationObserver.class).debug(ex.getMessage(), ex);
                onEnvironmentCreationFailed(status, ex.getMessage());
            } finally {
                synchronized (FSKCondaEnvironmentCreationObserver.this) {
                    m_currentCreationMonitor = null;
                }
            }
        }).start();
    }

    /**
     * Cancels the active new Conda environment creation process.
     * <P>
     * Note: Currently unused since the observer only allows one active creation process at a time.
     *
     * @param status The status of the creation process to cancel.
     */
    public synchronized void cancelEnvironmentCreation(final CondaEnvironmentCreationStatus status) {
        if (m_currentCreationMonitor != null) {
            m_currentCreationMonitor.cancel();
            status.m_statusMessage.setStringValue("Canceling environment creation...");
        }
    }

    private void onEnvironmentCreationStarting(final CondaEnvironmentCreationStatus status) {
        status.m_statusMessage.setStringValue(IN_PROGRESS_MESSAGE);
        for (final CondaEnvironmentCreationStatusListener listener : m_listeners) {
            listener.condaEnvironmentCreationStarting(status);
        }
    }

    private void onEnvironmentCreationFinished(final CondaEnvironmentCreationStatus status,
        final CondaEnvironmentIdentifier createdEnvironment) {
        status.m_statusMessage.setStringValue(
            "Environment creation finished.\nNew environment's name: '" + createdEnvironment.getName() + "'.");
        for (final CondaEnvironmentCreationStatusListener listener : m_listeners) {
            listener.condaEnvironmentCreationFinished(status, createdEnvironment);
        }
    }

    private void onEnvironmentCreationCanceled(final CondaEnvironmentCreationStatus status) {
        status.m_statusMessage.setStringValue("Environment creation was canceled.");
        for (final CondaEnvironmentCreationStatusListener listener : m_listeners) {
            listener.condaEnvironmentCreationCanceled(status);
        }
    }

    private void onEnvironmentCreationFailed(final CondaEnvironmentCreationStatus status, final String errorMessage) {
        status.m_statusMessage.setStringValue("Environment creation failed: " + errorMessage);
        for (final CondaEnvironmentCreationStatusListener listener : m_listeners) {
            listener.condaEnvironmentCreationFailed(status, errorMessage);
        }
    }

    /**
     * @param listener A listener which will be notified about changes in the status of the any environment creation
     *            process started by this instance.
     * @param prepend {@code true} if {@code listener} shall be prepended to the list of listeners, {@code false} if it
     *            shall be appended.
     */
    public void addEnvironmentCreationStatusListener(final CondaEnvironmentCreationStatusListener listener,
        final boolean prepend) {
        if (!m_listeners.contains(listener)) {
            if (prepend) {
                m_listeners.add(0, listener);
            } else {
                m_listeners.add(listener);
            }
        }
    }

    /**
     * @param listener The listener to remove.
     * @return {@code true} if the listener was present before removal.
     */
    public boolean removeEnvironmentCreationStatusListener(final CondaEnvironmentCreationStatusListener listener) {
        return m_listeners.remove(listener);
    }

    /**
     * Listener which will be notified about changes in the status of all environment creations initiated by the
     * enclosing observer.
     */
    public interface CondaEnvironmentCreationStatusListener {

        /**
         * Called asynchronously, that is, possibly not in a UI thread.
         *
         * @param status The status of the corresponding creation process.
         */
        void condaEnvironmentCreationStarting(CondaEnvironmentCreationStatus status);

        /**
         * Called asynchronously, that is, possibly not in a UI thread.
         *
         * @param status The status of the corresponding creation process.
         * @param createdEnvironment A description of the created environment.
         */
        void condaEnvironmentCreationFinished(CondaEnvironmentCreationStatus status,
            CondaEnvironmentIdentifier createdEnvironment);

        /**
         * Called asynchronously, that is, possibly not in a UI thread.
         *
         * @param status The status of the corresponding creation process.
         */
        void condaEnvironmentCreationCanceled(CondaEnvironmentCreationStatus status);

        /**
         * Called asynchronously, that is, possibly not in a UI thread.
         *
         * @param status The status of the corresponding creation process.
         * @param errorMessage The message of the error that made environment creation fail.
         */
        void condaEnvironmentCreationFailed(CondaEnvironmentCreationStatus status, String errorMessage);
    }

    /**
     * Encapsulates the state of a single Conda environment creation process.
     */
    public static class CondaEnvironmentCreationStatus {

        private static final String DEFAULT_STRING_VALUE = "";

        private static final int DEFAULT_INT_VALUE = 0;

        // Not meant for saving/loading. We just want observable values here to communicate with the view:

        private final SettingsModelString m_statusMessage =
            new SettingsModelString(DUMMY_CFG_KEY, DEFAULT_STRING_VALUE);

        private final SettingsModelInteger m_progress = new SettingsModelInteger(DUMMY_CFG_KEY, DEFAULT_INT_VALUE);

        private final SettingsModelString m_errorLog = new SettingsModelString(DUMMY_CFG_KEY, DEFAULT_STRING_VALUE);

        /**
         * @return The status message of the current environment creation process.
         */
        public SettingsModelString getStatusMessage() {
            return m_statusMessage;
        }

        /**
         * @return The package download progress of the current environment creation process.
         */
        public SettingsModelInteger getProgress() {
            return m_progress;
        }

        /**
         * @return The error log of the current environment creation process.
         */
        public SettingsModelString getErrorLog() {
            return m_errorLog;
        }
    }

    private static final class StateUpdatingCondaEnvironmentCreationMonitor extends CondaEnvironmentCreationMonitor {

        private final CondaEnvironmentCreationStatus m_status;

        private StateUpdatingCondaEnvironmentCreationMonitor(final CondaEnvironmentCreationStatus status) {
            m_status = status;

        }

        @Override
        protected void handlePackageDownloadProgress(final String currentPackage, final boolean packageFinished,
            final double progress) {
            if (!packageFinished) {
                m_status.m_statusMessage.setStringValue("Downloading package '" + currentPackage + "'...");
            } else {
                m_status.m_statusMessage.setStringValue(IN_PROGRESS_MESSAGE);
            }
            m_status.m_progress.setIntValue((int)(progress * 100));
        }

        @Override
        protected void handleWarningMessage(final String warning) {
            handleErrorMessage(warning);
        }

        @Override
        protected void handleErrorMessage(final String message) {
            m_status.m_errorLog.setStringValue(m_status.m_errorLog.getStringValue() + message + "\n");
        }
    }
    
    public static CondaEnvironmentIdentifier createEnvironmentFromFile(final Conda conda,
        final CondaEnvVersion condaEnvVersion, final String pathToFile, String environmentName,
        final CondaEnvironmentCreationMonitor monitor) throws IOException, CondaCanceledExecutionException {
        if (environmentName == null || environmentName.isEmpty()) {
            environmentName = getDefaultEnvironmentName(conda, condaEnvVersion, "");
        } else {
            final List<String> existingEnvironmentNames = conda.getEnvironmentNames();
            if (existingEnvironmentNames.contains(environmentName)) {
                throw new IOException(
                    "Conda environment '" + environmentName + "' already exists. Please use a different, unique name.");
            }
        }
        return conda.createEnvironmentFromFile(pathToFile, environmentName, monitor);
    }
    private static String getDefaultEnvironmentName(final Conda conda, final CondaEnvVersion condaEnvVersion,
        final String suffix) throws IOException {
        String environmentPrefix;
        switch (condaEnvVersion) {
            case PYTHON2:
              environmentPrefix = DEFAULT_PYTHON2_ENV_PREFIX;
                break;
            case PYTHON3:
              environmentPrefix = DEFAULT_PYTHON3_ENV_PREFIX;
                break;
            case R3:
              environmentPrefix = DEFAULT_R3_ENV_PREFIX;
                break;
            default:
              environmentPrefix = DEFAULT_R4_ENV_PREFIX;
                break;
        }

       String environmentName = environmentPrefix;
        long possibleEnvironmentSuffix = 1;
        final List<String> environmentNames = conda.getEnvironmentNames();
        while (environmentNames.contains(environmentName)) {
            environmentName = environmentPrefix + "_" + possibleEnvironmentSuffix;
            possibleEnvironmentSuffix++;
        }
        return environmentName;
    }
    
    public static CondaEnvironmentIdentifier createDefaultEnvironment(final Conda conda,
        final String environmentName, final Version pythonVersion, final CondaEnvironmentCreationMonitor monitor)
        throws IOException, CondaCanceledExecutionException {
        final CondaEnvVersion pythonMajorVersion = pythonVersion.getMajor() == 3 //
            ? CondaEnvVersion.PYTHON3 //
            : CondaEnvVersion.PYTHON2;
        return createEnvironmentFromFile(conda, pythonMajorVersion,
            "/Users/ahmadswaid/git/FSK-Labt/de.bund.bfr.knime.fsklab.nodes/src/de/bund/bfr/knime/fsklab/v2_0/fskenvironmentcreator/R3_env.yaml", environmentName, monitor);
    }
}
