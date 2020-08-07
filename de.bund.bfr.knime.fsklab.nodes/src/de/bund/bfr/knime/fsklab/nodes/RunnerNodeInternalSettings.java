/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.environment.GeneratedResourceFiles;

public class RunnerNodeInternalSettings {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("RunnerNodeInternalSettings");

  /**
   * Non-null image file to use for this current node. Initialized to temp location.
   */
  public File imageFile = null;


/**
 * TODO: Check if saving the image in a separate file is still necessary.
 * */
  public RunnerNodeInternalSettings() {
    try {
    
      imageFile = FileUtil.createTempFile("FskxRunner-", ".svg");
    } catch (IOException e) {
      LOGGER.error("Cannot create temporary file.", e);
      throw new RuntimeException(e);
    }
  }


  /** save generated resource files in the /internal folder*/
  public void saveInternals(File nodeInternDir, FskPortObject fskObj) throws IOException {

    if(fskObj instanceof CombinedFskPortObject) {

      FskPortObject firstObj    = ((CombinedFskPortObject)fskObj).getFirstFskPortObject();
      FskPortObject secondObj   = ((CombinedFskPortObject)fskObj).getSecondFskPortObject();

      saveInternals(nodeInternDir,firstObj);
      saveInternals(nodeInternDir,secondObj);

    } else {

      // FSK Object contains paths to resource files (if there are any).
      // On saving thie workflow, these files are copied to the /internal 
      // folder of the Runner node that created the files
      List<File> temp_dirs = new ArrayList<File>(); 
      for(Path path : fskObj.generatedResourceFiles.getResourcePaths()) {

        File resourceFile = path.toFile();
        
        temp_dirs.add(resourceFile.getParentFile());

        final File internalFile = new File(nodeInternDir, resourceFile.getName());

        FileUtil.copy(resourceFile, internalFile);
        
      }//for

      // Directories with temporary files are deleted since their copies are in the /internal folder.
      for(File dir : temp_dirs) {
        FileUtils.deleteQuietly(dir);  
      }
    }//else
  }//saveInternals

  /** Clear the contents of the image file. */
  public void reset() {
//    plot = null;
    FileUtils.deleteQuietly(imageFile);
  }
}
