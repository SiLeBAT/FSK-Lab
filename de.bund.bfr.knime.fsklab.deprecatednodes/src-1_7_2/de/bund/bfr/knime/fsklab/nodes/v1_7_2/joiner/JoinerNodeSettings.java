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
package de.bund.bfr.knime.fsklab.nodes.v1_7_2.joiner;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

class JoinerNodeSettings {
    private static final String CFG_JOIN_SCRIPT = "JoinScript";    
    private static final String CFG_MODEL_METADATA = "modelMetaData";
   
    private static final String CFG_MODEL_MATH1 = "modelMath1";
    private static final String CFG_MODEL_MATH2 = "modelMath2";

    String modelMetaData;
  
    String modelMath1;
    String modelMath2;
    
    /** Path to model script. */
    public String joinScript = "";

    void load(final NodeSettingsRO settings) throws InvalidSettingsException {
      joinScript = settings.getString(CFG_JOIN_SCRIPT, "");
      modelMetaData = settings.getString(CFG_MODEL_METADATA, "");
      
      modelMath1 = settings.getString(CFG_MODEL_MATH1, "");
      modelMath2 = settings.getString(CFG_MODEL_MATH2, "");
    }

    void save(final NodeSettingsWO settings) {
      settings.addString(CFG_JOIN_SCRIPT, joinScript);
      settings.addString(CFG_MODEL_METADATA, modelMetaData);
      
      settings.addString(CFG_MODEL_MATH1, modelMath1);
      settings.addString(CFG_MODEL_MATH2, modelMath2);
    }
  }
