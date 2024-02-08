package de.bund.bfr.knime.fsklab;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Utility class for the packages information files. It is used for serializing/deserializing
 * packages information.
 */
public class PackagesInfo {

  @JsonProperty("Language")
  private String language;

  @JsonProperty("PackageList")
  private List<VersionedPackage> packages = new ArrayList<>();

  //No-argument constructor
  public PackagesInfo() {
  }

  public PackagesInfo(final String language, final List<VersionedPackage> packages) {
    this.language = language;
    this.packages = packages;
  }
  
  @JsonIgnore
  public List<String> getPackageNames() {
    return packages.stream()
            .map(VersionedPackage::getPackageName)
            .collect(Collectors.toList());
  }
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
  public List<VersionedPackage> getPackages() {
    return packages;
  }

  public void setPackages(List<VersionedPackage> packages) {
    this.packages = packages;
  }
}