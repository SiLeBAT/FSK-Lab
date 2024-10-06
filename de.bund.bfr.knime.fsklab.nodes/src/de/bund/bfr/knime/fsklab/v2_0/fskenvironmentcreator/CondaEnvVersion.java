package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;


public enum CondaEnvVersion {


  // Python versions
  PYTHON2("python2", "Python 2"),
  PYTHON3("python3", "Python 3"),

  // R versions
  R3("R-3", "R 3"),
  R4("R-4", "R 4");

  private final String m_id;
  private final String m_name;

  private CondaEnvVersion(final String id, final String name) {
      m_id = id;
      m_name = name;
  }

  /**
   * Get the CondaEnvVersion by its id.
   *
   * @param versionId The id of the version to return.
   * @return The CondaEnvVersion for the given id.
   */
  public static CondaEnvVersion fromId(final String versionId) {
      for (CondaEnvVersion version : values()) {
          if (version.getId().equals(versionId)) {
              return version;
          }
      }
      throw new IllegalStateException("Version '" + versionId + "' is neither a valid Python nor R version.");
  }

  /**
   * Get the CondaEnvVersion by its name.
   *
   * @param versionName The name of the version to return.
   * @return The CondaEnvVersion for the given name.
   */
  public static CondaEnvVersion fromName(final String versionName) {
      for (CondaEnvVersion version : values()) {
          if (version.getName().equals(versionName)) {
              return version;
          }
      }
      throw new IllegalStateException("Version '" + versionName + "' is neither a valid Python nor R version.");
  }

  /**
   * @return The id of this version. Suitable for serialization etc.
   */
  public String getId() {
      return m_id;
  }

  /**
   * @return The friendly name of this version. Suitable for use in a user interface.
   */
  public String getName() {
      return m_name;
  }
}