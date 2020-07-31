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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.FskPortObject;

public class RunnerNodeInternalSettings {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("RunnerNodeInternalSettings");

  /**
   * Non-null image file to use for this current node. Initialized to temp location.
   */
  public File imageFile = null;

  public Image plot = null;
  public List<File> resourceFiles = null;
  public List<File> internalFiles = null;
  public FskPortObject portObj = null;
  public RunnerNodeInternalSettings() {
    try {
      resourceFiles = new ArrayList<File>();
      internalFiles = new ArrayList<File>();
      imageFile = FileUtil.createTempFile("FskxRunner-", ".svg");
    } catch (IOException e) {
      LOGGER.error("Cannot create temporary file.", e);
      throw new RuntimeException(e);
    }
  }

  /** Loads the saved image. */
  public void loadInternals(File nodeInternDir) throws IOException {
    
    // do we need to load the resource Files
    // yes: in case we reset the node, the list needs to be cleared and deleted
      final File resource = new File(nodeInternDir,"camp-alt.csv");
      internalFiles.add(resource);
    
//    final File file = new File(nodeInternDir, "Rplot.svg");
//
//    if (file.exists() && file.canRead()) {
//      FileUtil.copy(file, imageFile);
//      try (InputStream is = new FileInputStream(imageFile)) {
//        plot = new PNGImageContent(is).getImage();
//      }
//    }
  }

  /** Saves the saved image. */
  public void saveInternals(File nodeInternDir, FskPortObject fskObj) throws IOException {
    
    fskObj.resourceFiles.clear();
    List<File> temp_dirs = new ArrayList<File>(); 
    for(File f : resourceFiles) {
      
 
      temp_dirs.add(new File(f.getParent()));
      
      final File file = new File(nodeInternDir, f.getName());
      
      FileUtil.copy(f, file);
      internalFiles.add(file);
      fskObj.resourceFiles.add(file.getAbsolutePath());
      
    }
    
    //internalFiles.forEach(f -> fskObj.resourceFiles.add(f.getAbsolutePath()));
    for(File dir : temp_dirs) {
      try {
        FileUtil.deleteRecursively(dir);  
      }catch(Exception e) {
        
      }
      
    }
      

//    if (plot != null) {
//      final File file = new File(nodeInternDir, "Rplot.svg");
//      FileUtil.copy(imageFile, file);
//    }
  }

  /** Clear the contents of the image file. */
  public void reset() {
    plot = null;
    FileUtils.deleteQuietly(imageFile);
    for(File f : internalFiles) {
      FileUtils.deleteQuietly(f);
    }
    internalFiles.clear();
    resourceFiles.clear();
  }
}
