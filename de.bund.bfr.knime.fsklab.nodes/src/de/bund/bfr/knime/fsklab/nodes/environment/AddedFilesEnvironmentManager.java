package de.bund.bfr.knime.fsklab.nodes.environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.knime.core.util.FileUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/**
 * AddedFilesEnvironmentManager handles working directories made out of referenced files in 
 * addition to the working directories contained within FSKX archives. If the
 * references files array is null, empty or the files do not exist,
 * {@link AddedFilesEnvironmentManager#getEnvironment()} returns the return value of the original 
 * EnvironmentManager. 
 * 
 * @author Thomas Schueler, BfR, Berlin.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class AddedFilesEnvironmentManager implements EnvironmentManager {

  private EnvironmentManager manager;
  private String[] files;
  private List<Path> markedForRemoval = new ArrayList<Path>();
  
  public AddedFilesEnvironmentManager() {
    this(new DefaultEnvironmentManager(), new String[0]);
  }
  public AddedFilesEnvironmentManager(EnvironmentManager manager) {
    this(manager, new String[0]);
  }
  public AddedFilesEnvironmentManager(String[] files) {
    this(new DefaultEnvironmentManager(), files);
  }
  public AddedFilesEnvironmentManager(EnvironmentManager manager, String[] files) {
    this.manager = manager;
    this.files = files;
    
  }
  
  public List<Path> getMarkedForRemoval() {
    return markedForRemoval;
  }
  public EnvironmentManager getManager() {
    return manager;
  }
  
  @Override
  public Optional<Path> getEnvironment() {
    Optional<Path> environment = manager.getEnvironment();
    
    if (files == null || files.length == 0)
      return environment;
 
    return copyFilesToEnvironment(environment);
  }

  private Optional<Path> copyFilesToEnvironment(Optional<Path> environment){
    
    List<Path> filePaths = new ArrayList<>();
    try {
      for (String filePath : files) {
        filePaths.add(FileUtil.resolveToPath(FileUtil.toURL(filePath)));
      }

    }catch(Exception e) {
      return environment;
    }
    
    for (Path filePath : filePaths) {
      if (Files.notExists(filePath))
        return environment;
    }
    
    try {
      //Path environment = Files.createTempDirectory("workingDirectory");
      if(!environment.isPresent()) {
        environment = Optional.of(Files.createTempDirectory("workingDirectory"));
      }
        
      for (Path filePath : filePaths) {
        Path sourcePath = filePath;
        Path targetPath = environment.get().resolve(sourcePath.getFileName());
        if(Files.notExists(targetPath)) { // don't overwrite or replace file if it exists already
          Files.copy(sourcePath, targetPath);
          markedForRemoval.add(targetPath);
        }
      }
      
      return environment;
    } catch (IOException e) {
      return environment;
    }
  }
  
  
  public void clearAddedFiles() {
    
    for (Path filePath : markedForRemoval) {
      FileUtils.deleteQuietly(filePath.toFile());
    }
    markedForRemoval.clear();
  
  }
 
  @Override
  public void deleteEnvironment(Path path) {
    clearAddedFiles();
    manager.deleteEnvironment(path);
  }
  
  @Override
  public String[] getEntries() {
    return files;
  }
    
}
