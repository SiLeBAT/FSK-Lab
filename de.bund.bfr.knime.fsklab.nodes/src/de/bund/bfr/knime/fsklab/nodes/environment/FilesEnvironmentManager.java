package de.bund.bfr.knime.fsklab.nodes.environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.knime.core.util.FileUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import de.bund.bfr.knime.fsklab.preferences.PreferenceInitializer;
/**
 * FilesEnvironmentManager handles working directories made out of referenced files. If the
 * references files array is null, empty or the files do not exist,
 * {@link FilesEnvironmentManager#getEnvironment()} returns an empty optional File.
 * 
 * @author Miguel de Alba, BfR, Berlin.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class FilesEnvironmentManager implements EnvironmentManager {

  private final String[] files;

  public FilesEnvironmentManager() {
    this(new String[0]);
  }

  public FilesEnvironmentManager(String[] files) {
    this.files = files;
  }

  public String[] getFiles() {
    return files;
  }

  @Override
  public Optional<Path> getEnvironment() {

    if (files == null || files.length == 0)
      return Optional.empty();

 
    List<Path> filePaths = new ArrayList<>();
    try {
      for (String filePath : files) {
        filePaths.add(FileUtil.resolveToPath(FileUtil.toURL(filePath)));
      }

    }catch(Exception e) {
      return Optional.empty();
    }
    
    for (Path filePath : filePaths) {
      if (Files.notExists(filePath))
        return Optional.empty();
    }

    try {
      Path environment = Files.createTempDirectory(Paths.get(PreferenceInitializer.getFSKWorkingDirectory()),"workingDirectory");
      for (Path filePath : filePaths) {
        Path sourcePath = filePath;
        Path targetPath = environment.resolve(sourcePath.getFileName());
        Files.copy(filePath, targetPath);
      }

      return Optional.of(environment);
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  @Override
  public void deleteEnvironment(Path path) {
    FileUtils.deleteQuietly(path.toFile());
  }
}
