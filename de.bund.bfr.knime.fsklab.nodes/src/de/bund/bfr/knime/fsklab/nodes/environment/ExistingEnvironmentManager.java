package de.bund.bfr.knime.fsklab.nodes.environment;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import org.knime.core.util.FileUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * ExistingEnvironmentManager keeps a working directory already existing in disk. If the passed
 * directory is null, empty string, does not exist or is empty,
 * {@link ExistingEnvironmentManager#getEnvironment()} returns an empty optional File.
 * 
 * @author Miguel de Alba, BfR, Berlin.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ExistingEnvironmentManager implements EnvironmentManager {

  private final String environmentPath;

  public ExistingEnvironmentManager() {
    this("");
  }

  public ExistingEnvironmentManager(String environmentPath) {
    this.environmentPath = environmentPath;
  }

  public String getEnvironmentPath() {
    return environmentPath;
  }

  @Override
  public Optional<Path> getEnvironment() {

    if (environmentPath == null || environmentPath.isEmpty())
      return Optional.empty();

    Path environment; 
    try {
      URL url = FileUtil.toURL(environmentPath);
      environment = FileUtil.resolveToPath(url);
    }catch(Exception e) {
      return Optional.empty();
    }
    
    try {
      if (Files.notExists(environment) || !Files.list(environment).findAny().isPresent())
        return Optional.empty();
    } catch (IOException e) {
      return Optional.empty();
    }

    return Optional.of(environment);
  }

  @Override
  public void deleteEnvironment(Path path) {
    // The path existed previously and should not be removed.
  }

  @Override
  public String[] getEntries() {
    File existingWorkingDirectory = new File(environmentPath);
    File[] files = existingWorkingDirectory.listFiles(File::isFile);
    String entries[] = Arrays.stream(files).map(File::getName)
        .toArray(String[]::new);
   
    return entries;
  }
  
  
}
