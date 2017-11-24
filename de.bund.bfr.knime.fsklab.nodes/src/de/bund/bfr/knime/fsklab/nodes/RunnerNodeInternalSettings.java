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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;

public class RunnerNodeInternalSettings {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("RunnerNodeInternalSettings");

  /**
   * Non-null image file to use for this current node. Initialized to temp location.
   */
  public File imageFile = null;

  public Image plot = null;

  public RunnerNodeInternalSettings() {
    try {
      imageFile = FileUtil.createTempFile("FskxRunner-", ".png");
    } catch (IOException e) {
      LOGGER.error("Cannot create temporary file.", e);
      throw new RuntimeException(e);
    }
    imageFile.deleteOnExit();
  }

  /** Loads the saved image. */
  public void loadInternals(File nodeInternDir) throws IOException {
    final File file = new File(nodeInternDir, "Rplot.png");

    if (file.exists() && file.canRead()) {
      FileUtil.copy(file, imageFile);
      try (InputStream is = new FileInputStream(imageFile)) {
        plot = new PNGImageContent(is).getImage();
      }
    }
  }

  /** Saves the saved image. */
  public void saveInternals(File nodeInternDir) throws IOException {
    if (plot != null) {
      final File file = new File(nodeInternDir, "Rplot.png");
      FileUtil.copy(imageFile, file);
    }
  }

  /** Clear the contents of the image file. */
  public void reset() {
    plot = null;

    if (imageFile != null) {
      try (OutputStream erasor = new FileOutputStream(imageFile)) {
        erasor.write((new String()).getBytes());
      } catch (final FileNotFoundException e) {
        LOGGER.error("Temporary file is removed.", e);
      } catch (final IOException e) {
        LOGGER.error("Cannot write temporary file.", e);
      }
    }
  }
}
