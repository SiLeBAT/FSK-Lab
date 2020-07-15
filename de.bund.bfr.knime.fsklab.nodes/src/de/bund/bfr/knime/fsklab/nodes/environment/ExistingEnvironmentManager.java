package de.bund.bfr.knime.fsklab.nodes.environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
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

  @Override
  public Optional<Path> getEnvironment() {

    if (environmentPath == null || environmentPath.isEmpty())
      return Optional.empty();

    Path environment = Paths.get(environmentPath);
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
}
