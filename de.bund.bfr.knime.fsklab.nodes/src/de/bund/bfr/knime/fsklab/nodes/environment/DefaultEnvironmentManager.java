package de.bund.bfr.knime.fsklab.nodes.environment;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.knime.core.util.FileUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import de.bund.bfr.knime.fsklab.preferences.PreferenceInitializer;

/**
 * 
 * Provides a default environment that creates a temporary working directory.
 * 
 * The reason for this class is that a model might directly need a working directory 
 * to run, but in case the model creates new files on its own, we need the location
 * of the working directory to store the generated files for later use.
 *  
 * 
 * @author SchueleT
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class DefaultEnvironmentManager implements EnvironmentManager {

 private String environmentPath;
  
  public DefaultEnvironmentManager() {
    
    environmentPath = "";
  }

 
  @Override
  public Optional<Path> getEnvironment() {

    try {
      environmentPath = FileUtil.createTempDir("defaultWorkingDirectory",new File(PreferenceInitializer.getFSKWorkingDirectory())).getAbsolutePath();
    }catch(Exception e) {
      e.printStackTrace();
    }
    
    Path environment = Paths.get(environmentPath);
   
    return Optional.of(environment);
  }
  
  @Override
  public void deleteEnvironment(Path path) {
    FileUtil.deleteRecursively(path.toFile());  
  }

}
