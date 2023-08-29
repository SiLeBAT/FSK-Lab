package de.bund.bfr.knime.fsklab.nodes.environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.knime.core.util.FileUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import de.bund.bfr.knime.fsklab.preferences.PreferenceInitializer;
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
  private String[] entries;
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
  public AddedFilesEnvironmentManager(EnvironmentManager manager, String[] entries) {
    this.manager = manager;
    this.entries = entries;
    
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
    
    if (entries == null || entries.length == 0)
      return environment;
 
    return copyFilesToEnvironment(environment);
  }

  private Optional<Path> copyFilesToEnvironment(Optional<Path> environment){
    clearRedundantEntries();
    List<Path> filePaths = new ArrayList<>();
    try {
      for (String filePath : entries) {
        Path p = FileUtil.resolveToPath(FileUtil.toURL(filePath));
        if(Files.exists(p))
          filePaths.add(p);
      }

    }catch(Exception e) {
      return environment;
    }
    

    
    try {
      //Path environment = Files.createTempDirectory("workingDirectory");
      if(!environment.isPresent()) {
        environment = Optional.of(Files.createTempDirectory(Paths.get(PreferenceInitializer.getFSKWorkingDirectory()),"workingDirectory"));
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
    clearRedundantEntries();
    String[] allEntries = manager.getEntries(); 
    if( allEntries != null) {
      List<String> resultList = new ArrayList<>(allEntries.length + entries.length);
      Collections.addAll(resultList, allEntries);
      Collections.addAll(resultList, entries);
      String [] resultArray = new String[resultList.size()];
      return resultList.toArray(resultArray);
  
    } else 
        return entries;
  }
 
  private void clearRedundantEntries() {
    String[] allEntries = manager.getEntries(); 
    List<String> newList = new ArrayList<>(entries.length);

    if( allEntries != null) {
      List<String> resultList = new ArrayList<>(allEntries.length);
      List<String> oldList = new ArrayList<>(entries.length);
      Collections.addAll(resultList, allEntries);
      Collections.addAll(oldList, entries);
      for(String entry : oldList) {
        if(!resultList.contains(entry))
          newList.add(entry);
      }
      entries = newList.toArray(new String[newList.size()]);
      
    }
  }
}
