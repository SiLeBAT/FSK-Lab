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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

class JoinerNodeSettings {
    private static final String CFG_JOIN_SCRIPT = "JoinScript";    
    private static final String CFG_GENERAL_INFORMATION = "generalInformation";
    private static final String CFG_SCOPE = "scope";
    private static final String CFG_DATA_BACKGROUND = "dataBackground";
    private static final String CFG_MODEL_MATH = "modelMath";

    String generalInformation;
    String scope;
    String dataBackground;
    String modelMath;
    
    /** Path to model script. */
    public String joinScript = "";

    void load(final NodeSettingsRO settings) throws InvalidSettingsException {
      joinScript = settings.getString(CFG_JOIN_SCRIPT, "");
      generalInformation = settings.getString(CFG_GENERAL_INFORMATION, "");
      scope = settings.getString(CFG_SCOPE, "");
      dataBackground = settings.getString(CFG_DATA_BACKGROUND, "");
      modelMath = settings.getString(CFG_MODEL_MATH, "");
    }

    void save(final NodeSettingsWO settings) {
      settings.addString(CFG_JOIN_SCRIPT, joinScript);
      settings.addString(CFG_GENERAL_INFORMATION, generalInformation);
      settings.addString(CFG_SCOPE, scope);
      settings.addString(CFG_DATA_BACKGROUND, dataBackground);
      settings.addString(CFG_MODEL_MATH, modelMath);
    }
  }
