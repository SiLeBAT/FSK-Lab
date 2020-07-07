package de.bund.bfr.knime.fsklab.nodes.environment;

import java.nio.file.Path;
import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY)
public interface EnvironmentManager {
  
  @JsonIgnore
  Optional<Path> getEnvironment();
}
