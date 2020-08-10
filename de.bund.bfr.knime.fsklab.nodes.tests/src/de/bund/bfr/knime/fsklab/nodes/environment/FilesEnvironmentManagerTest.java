package de.bund.bfr.knime.fsklab.nodes.environment;

import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

public class FilesEnvironmentManagerTest {
  
  @Test
  public void testEmptyConstructor() throws Exception {
    FilesEnvironmentManager environmentManager = new FilesEnvironmentManager();
    assertEquals(0, environmentManager.getFiles().length);
  }

  @Test
  public void testGetEnvironment_nullFiles_shouldReturnEmptyOptional() {
    FilesEnvironmentManager environmentManager = new FilesEnvironmentManager(null);
    assertFalse(environmentManager.getEnvironment().isPresent());
  }

  @Test
  public void testGetEnvironment_emptyFiles_shouldReturnEmptyOptional() {
    FilesEnvironmentManager environmentManager = new FilesEnvironmentManager(new String[0]);
    assertFalse(environmentManager.getEnvironment().isPresent());
  }

  @Test
  public void testGetEnvironment_nonExistingFiles_shouldReturnEmptyOptional() {
    FilesEnvironmentManager environmentManager =
        new FilesEnvironmentManager(new String[] {"@@NonExistingPath"});
    assertFalse(environmentManager.getEnvironment().isPresent());
  }

  @Test
  public void testGetEnvironment_existingFiles_shouldReturnPresentOptional() throws IOException {
    FilesEnvironmentManager environmentManager = new FilesEnvironmentManager(
        new String[] {"files/campy-obs-censoring.csv", "files/model-pars.csv"});

    Optional<Path> optional = environmentManager.getEnvironment();
    assertTrue(optional.isPresent());

    Path environment = optional.get();
    assertTrue(Files.exists(environment));
    assertTrue(Files.isDirectory(environment));

    List<Path> files = Files.list(environment).collect(Collectors.toList());
    assertEquals(2, files.size());
  }
}
