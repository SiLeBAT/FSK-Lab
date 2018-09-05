/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *******************************************************************************/
package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * List of PmmLab literature references.
 * 
 * @see Literature
 * @author Miguel de Alba
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class LiteratureList {
  static final String NUM_LITERATURE = "numLiterature";
  static final String LITERATURE = "literature";

  private int numLiterature;
  private Literature[] literature;

  /**
   * Returns an array with all the literature references in the list.
   * 
   * If not set returns null.
   * 
   * @return an array with all the literature references in the list
   */
  public Literature[] getLiterature() {
    return literature;
  }

  /**
   * Sets the literature references in the list.
   * 
   * @param literature array of literature references to be set
   */
  public void setLiterature(final Literature[] literature) {
    numLiterature = literature != null ? literature.length : 0;
    this.literature = literature;
  }

  /**
   * Saves the list of literature references into a {@link LiteratureList}.
   * 
   * @param settings settings where to save the {@link LiteratureList} properties.
   */
  public void saveToNodeSettings(NodeSettingsWO settings) {
    SettingsHelper.addInt(NUM_LITERATURE, numLiterature, settings);
    for (int i = 0; i < numLiterature; i++) {
      literature[i].saveToNodeSettings(settings.addNodeSettings(LITERATURE + i));
    }
  }

  /**
   * Load properties of the literature references from a {@link LiteratureList}.
   * 
   * @param settings the settings where to load the {@link LiteratureList} from
   */
  public void loadFromNodeSettings(NodeSettingsRO settings) {
    try {
      numLiterature = settings.getInt(NUM_LITERATURE);
      literature = new Literature[numLiterature];
      for (int i = 0; i < numLiterature; i++) {
        literature[i] = new Literature();
        literature[i].loadFromNodeSettings(settings.getNodeSettings(LITERATURE + i));
      }
    } catch (InvalidSettingsException e) {
    }
  }
}
