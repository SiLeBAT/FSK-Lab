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
package de.bund.bfr.knime.fsklab.v1_9.runner;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;

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

  /** Clear the contents of the image file. */
  public void reset() {
//    plot = null;
    FileUtils.deleteQuietly(imageFile);
  }
}
