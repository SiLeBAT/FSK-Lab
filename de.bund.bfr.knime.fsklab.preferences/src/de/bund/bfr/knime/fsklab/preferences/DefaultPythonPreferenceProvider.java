package de.bund.bfr.knime.fsklab.preferences;

public class DefaultPythonPreferenceProvider implements PythonPreferenceProvider {

	private final String m_pythonHome;
	
	public DefaultPythonPreferenceProvider(final String pythonHome) {
		m_pythonHome = pythonHome;
	}
	
	@Override
	public String getPythonHome() {
		return m_pythonHome;
	}
}
