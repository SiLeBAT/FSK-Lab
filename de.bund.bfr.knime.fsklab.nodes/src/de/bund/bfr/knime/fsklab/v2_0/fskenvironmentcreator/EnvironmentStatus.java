package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;

public class EnvironmentStatus {
  
  public String EnvironmentName;
  public boolean envExist;
  
  public EnvironmentStatus(String environmentName, boolean envExist) {
    EnvironmentName = environmentName;
    this.envExist = envExist;
  }
  

  public String getEnvironmentName() {
    return EnvironmentName;
  }

  public void setEnvironmentName(String environmentName) {
    EnvironmentName = environmentName;
  }

  public boolean isEnvExist() {
    return envExist;
  }

  public void setEnvExist(boolean envExist) {
    this.envExist = envExist;
  }
  

}
