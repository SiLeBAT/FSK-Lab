package de.bund.bfr.knime.fsklab.nodes.environment;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.knime.core.internal.ReferencedFile;
import org.knime.core.node.workflow.NodeContext;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class GeneratedResourceFiles {
  
  private List<String> resourceFiles;
  private String internalDir;
  private List<Path> resourcePaths;

  public GeneratedResourceFiles() {
    NodeContext nodeContext = NodeContext.getContext();
    ReferencedFile nodeDirectory = nodeContext.getNodeContainer().getNodeContainerDirectory();
    this.resourceFiles = new ArrayList<>();
    this.resourcePaths = new ArrayList<>();
    this.internalDir =  nodeDirectory.getFile().getAbsolutePath() + File.separator + "internal";
  }
  
  public GeneratedResourceFiles(File internalDir) {
    
    this.resourceFiles = new ArrayList<>();
    this.resourcePaths = new ArrayList<>();
    this.internalDir = internalDir.getAbsolutePath();
  }
  
  public List<Path> getResourcePaths(){
    toResourcePaths();
    validatePaths();
    return this.resourcePaths;
  }
  
  public void addResourceFile(File resourceFile) {
    this.resourceFiles.add(resourceFile.getAbsolutePath());
    
  }
  
  private void validatePaths() {
    
    for(int i = 0; i < resourcePaths.size(); i++){
      Path oldPath = resourcePaths.get(i);

      if(Files.notExists(oldPath, LinkOption.NOFOLLOW_LINKS)) {
        Path newPath = Paths.get(this.internalDir + File.separator + oldPath.getFileName().toString());
        resourcePaths.set(i, newPath);
      }
    }
  }

  private void toResourcePaths(){
    
    this.resourcePaths = new ArrayList<>();
    
    this.resourceFiles.forEach(f -> resourcePaths.add(Paths.get(f)));
    
    
  }
 

}
