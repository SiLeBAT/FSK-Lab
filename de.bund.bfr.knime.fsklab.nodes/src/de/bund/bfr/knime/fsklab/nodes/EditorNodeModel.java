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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;

public class EditorNodeModel extends NoInternalsModel {

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE_OPTIONAL};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private final EditorNodeSettings settings = new EditorNodeSettings();

  EditorNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  // --- node settings methods ---

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    this.settings.loadSettings(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    (new EditorNodeSettings()).loadSettings(settings);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    this.settings.saveSettings(settings);
  }

  // --- other methods ---
  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  @Override
  protected void reset() {}

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    FskPortObject outObj;

    // If there is an input model
    if (inObjects.length > 0 && inObjects[0] != null) {
      FskPortObject inObj = (FskPortObject) inObjects[0];

      // if input model has not changed (the original script stored in
      // settings match the input model)
      if (Objects.equals(settings.originalModelScript, inObj.model)
          && Objects.equals(settings.originalVisualizationScript, inObj.viz)) {
        outObj = inObj;
        outObj.model = settings.modifiedModelScript;
        outObj.viz = settings.modifiedVisualizationScript;

        outObj.generalInformation = settings.generalInformation;
        outObj.scope = settings.scope;
        outObj.dataBackground = settings.dataBackground;
        outObj.modelMath = settings.modelMath;
      } else {
        settings.originalModelScript = inObj.model;
        settings.originalVisualizationScript = inObj.viz;

        settings.modifiedModelScript = inObj.model;
        settings.modifiedVisualizationScript = inObj.viz;

        settings.generalInformation = inObj.generalInformation;
        settings.scope = inObj.scope;
        settings.dataBackground = inObj.dataBackground;
        settings.modelMath = inObj.modelMath;

        outObj = inObj;
      }
    }
    /* If there is no input model then it will return the model created in the UI. */
    else {

      // The new model created in the UI is not executed, thus the path to
      // the plot is an empty string.
      String plotPath = "";

      outObj = new FskPortObject(settings.modifiedModelScript, settings.modifiedVisualizationScript,
          settings.generalInformation, settings.scope, settings.dataBackground, settings.modelMath,
          null, new ArrayList<>(), "", plotPath);
    }

    // Adds and installs libraries
    final Set<String> librariesSet = new HashSet<>();
    librariesSet.addAll(new RScript(outObj.model).getLibraries());
    librariesSet.addAll(new RScript(outObj.viz).getLibraries());
    outObj.packages.addAll(new ArrayList<>(librariesSet));
    
    // Retrieve used packages in scripts
    final List<String> libraries = new ArrayList<>();
    libraries.addAll(new RScript(outObj.model).getLibraries());
    libraries.addAll(new RScript(outObj.viz).getLibraries());

    return new PortObject[] {outObj};
  }
}
