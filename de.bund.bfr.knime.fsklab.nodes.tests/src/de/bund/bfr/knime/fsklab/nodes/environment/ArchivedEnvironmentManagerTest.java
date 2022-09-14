package de.bund.bfr.knime.fsklab.nodes.environment;

import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class ArchivedEnvironmentManagerTest {

  @Test
  public void testEmptyConstructor() {
    ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager();
    assertTrue(environmentManager.getArchivePath().isEmpty());
    assertEquals(0, environmentManager.getEntries().length);
  }

  @Test
  public void testGetEnvironment_nullArchivePath_shouldReturnEmptyOptional() {
    ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager(null, null);
    assertFalse(environmentManager.getEnvironment().isPresent());
  }

  @Test
  public void testGetEnvironment_emptyArchivePath_shouldReturnEmptyOptional() {
    ArchivedEnvironmentManager environmentManager = new ArchivedEnvironmentManager("", null);
    assertFalse(environmentManager.getEnvironment().isPresent());
  }

  @Test
  public void testGetEnvironment_nonExistingArchivePath_shouldReturnEmptyOptional() {
    ArchivedEnvironmentManager environmentManager =
        new ArchivedEnvironmentManager("@@NonExistingPath", null);
    assertFalse(environmentManager.getEnvironment().isPresent());
  }

  @Test
  public void testGetEnvironment_nullEntries_shouldNotReturnEmptyOptional() {
    ArchivedEnvironmentManager environmentManager =
        new ArchivedEnvironmentManager("files/model_without_resources.fskx", null);
    assertTrue(environmentManager.getEnvironment().isPresent());
  }

  @Test
  public void testGetEnvironment_emptyEntries_shouldNotReturnEmptyOptional() {
    ArchivedEnvironmentManager environmentManager =
        new ArchivedEnvironmentManager("files/model_without_resources.fskx", new String[0]);
    assertTrue(environmentManager.getEnvironment().isPresent());
  }

  @Test
  public void testGetEnvironment_withResources_shouldReturnPresentOptional() throws IOException {
    ArchivedEnvironmentManager environmentManager =
        new ArchivedEnvironmentManager("files/model_with_resources.fskx",
            new String[] {"./model-pars.csv", "./campy-obs-censoring.csv"});

    assertTrue(environmentManager.getEnvironment().isPresent());
    Path environment = environmentManager.getEnvironment().get();

    assertTrue(Files.exists(environment));
    assertTrue(Files.isDirectory(environment));
    
    try (Stream<Path> stream = Files.list(environment)) {
    	List<Path> files = stream.collect(Collectors.toList());
    	assertEquals(2, files.size());
    }
  }
}
