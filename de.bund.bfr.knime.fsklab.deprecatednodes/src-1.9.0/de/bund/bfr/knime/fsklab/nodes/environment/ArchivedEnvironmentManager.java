package de.bund.bfr.knime.fsklab.nodes.environment;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.jdom2.JDOMException;
import org.knime.core.util.FileUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

/**
 * ArchivedEnvironmentManager handles working directories contained within FSKX archives. If the
 * passed archive path is null, empty string, does not exist or has no resource entries,
 * {@link ArchivedEnvironmentManager#getEnvironment()} returns an empty optional File.
 * 
 * @author Miguel de Alba, BfR, Berlin.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ArchivedEnvironmentManager implements EnvironmentManager {

  /** Absolute path to the COMBINE archive in disk. */
  private final String archivePath;

  /** Paths to the resource entries in the COMBINE archive. */
  private final String[] entries;

  public ArchivedEnvironmentManager() {
    this("", new String[0]);
  }

  public ArchivedEnvironmentManager(String archivePath, String[] entries) {
    this.archivePath = archivePath;
    this.entries = entries;
  }

  public String getArchivePath() {
    return archivePath;
  }

  public String[] getEntries() {
    return entries;
  }

  @Override
  public Optional<Path> getEnvironment() {

    if (archivePath == null || archivePath.isEmpty())
      return Optional.empty();
    
    Path archiveFile; 
    
    try {
      URL url = FileUtil.toURL(archivePath);
      archiveFile = FileUtil.resolveToPath(url);
    }catch(Exception e) {
      return Optional.empty();
    }
    
   
    if (Files.notExists(archiveFile))
      return Optional.empty();

    if (entries == null || entries.length == 0)
      return Optional.empty();

    try (CombineArchive archive = new CombineArchive(archiveFile.toFile())) {

      Path environment = Files.createTempDirectory("workingDirectory");
      for (String entryLocation : entries) {
        ArchiveEntry resourceEntry = archive.getEntry(entryLocation);

        Path extractedResourcePath = environment.resolve(resourceEntry.getFileName());
        resourceEntry.extractFile(extractedResourcePath.toFile());
      }

      return Optional.of(environment);

    } catch (IOException | JDOMException | ParseException | CombineArchiveException e) {
      return Optional.empty();
    }
  }

  @Override
  public void deleteEnvironment(Path path) {
    FileUtils.deleteQuietly(path.toFile());
  }
}
