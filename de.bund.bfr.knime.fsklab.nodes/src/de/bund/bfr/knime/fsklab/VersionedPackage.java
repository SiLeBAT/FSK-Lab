package de.bund.bfr.knime.fsklab;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Utility class for serializing package information to JSON. The properties "Package" and
 * "Version" are kept for backward-compatibility.
 */
public class VersionedPackage {

  @JsonProperty("Package")
  private String packageName;

  @JsonProperty("Version")
  private String version;

  public VersionedPackage() {
  }
  
  public VersionedPackage(final String packageName, final String version) {
    this.packageName = packageName;
    this.version = version;
  }
  
  public String getPackageName() {
    return packageName;
  }

}
