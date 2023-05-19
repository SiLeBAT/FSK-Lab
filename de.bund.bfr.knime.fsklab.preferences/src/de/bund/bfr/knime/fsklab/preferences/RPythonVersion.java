package de.bund.bfr.knime.fsklab.preferences;
import org.knime.python2.PythonVersion;
public enum RPythonVersion {
	
	R("r", "R"),
	/**
     * Python 2
     */
    PYTHON2("python2", "Python 2"),

    /**
     * Python 3
     */
    PYTHON3("python3", "Python 3");

	/**
	 * @param versionId The {@link #getId() id} of the {@link PythonVersion} to return.
	 * @return The type of the given id.
	 */
	public static RPythonVersion fromId(final String versionId) {
	    final RPythonVersion type;
	    if (PYTHON2.getId().equals(versionId)) {
	        type = RPythonVersion.PYTHON2;
	    } else if (PYTHON3.getId().equals(versionId)) {
	        type = RPythonVersion.PYTHON3;
	    } else if (R.getId().equals(versionId)) {
	        type = RPythonVersion.R;
	    } else {
	        throw new IllegalStateException("Python version '" + versionId + "' is neither Python 2 nor Python 3. This "
	            + "is an implementation error.");
	    }
	    return type;
	}
	
	/**
	 * @param versionName The {@link #getName() name} of the {@link PythonVersion} to return.
	 * @return The type of the given name.
	 */
	public static RPythonVersion fromName(final String versionName) {
	    final RPythonVersion type;
	    if (PYTHON2.getName().equals(versionName)) {
	        type = RPythonVersion.PYTHON2;
	    } else if (PYTHON3.getName().equals(versionName)) {
	        type = RPythonVersion.PYTHON3;
	    } else if (R.getName().equals(versionName)) {
	        type = RPythonVersion.R;
	    } else {
	        throw new IllegalStateException("Python version '" + versionName + "' is neither Python 2 nor Python 3. "
	            + "This is an implementation error.");
	    }
	    return type;
	}
	
	private final String m_id;
	
	private final String m_name;
	
	private RPythonVersion(final String id, final String name) {
	    m_id = id;
	    m_name = name;
	}
	
	/**
	 * @return The id of this Python version. Suitable for serialization etc.
	 */
	public String getId() {
	    return m_id;
	}
	
	/**
	 * @return The friendly name of this Python version. Suitable for use in a user interface.
	 */
	public String getName() {
	    return m_name;
	}
}
