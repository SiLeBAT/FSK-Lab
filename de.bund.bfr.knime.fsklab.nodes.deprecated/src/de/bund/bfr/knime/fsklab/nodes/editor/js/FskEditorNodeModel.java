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
package de.bund.bfr.knime.fsklab.nodes.editor.js;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

@Deprecated
class FskEditorNodeModel
    extends AbstractWizardNodeModel<FskEditorViewRepresentation, FskEditorViewValue>
    implements PortObjectHolder {

  private FskPortObject m_port;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private static final String VIEW_NAME = new FskEditorNodeFactory().getInteractiveViewName();

  public FskEditorNodeModel() {
    super(IN_TYPES, OUT_TYPES, VIEW_NAME);
  }

  @Override
  public FskEditorViewRepresentation createEmptyViewRepresentation() {
    return new FskEditorViewRepresentation();
  }

  @Override
  public FskEditorViewValue createEmptyViewValue() {
    return new FskEditorViewValue();
  }

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.nodes.editor.js";
  };

  @Override
  public boolean isHideInWizard() {
    return false;
  }

  @Override
  public ValidationError validateViewValue(FskEditorViewValue viewContent) {
    return null;
  }

  @Override
  public void saveCurrentValue(NodeSettingsWO content) {}

  @Override
  public FskEditorViewValue getViewValue() {
    FskEditorViewValue val;
    synchronized (getLock()) {
      val = super.getViewValue();
      if (val == null) {
        val = createEmptyViewValue();
      }
      if (val.modelScript.isEmpty() && val.paramScript.isEmpty() && val.vizScript.isEmpty()
          && m_port != null) {
        val.modelScript = m_port.model;
        val.paramScript = m_port.param;
        val.vizScript = m_port.viz;
      }
    }
    return val;
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return inSpecs;
  }

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {
    FskPortObject obj = (FskPortObject) inObjects[0];

    synchronized (getLock()) {
      FskEditorViewValue val = getViewValue();

      // If not executed
      if (val.modelScript.isEmpty() && val.paramScript.isEmpty() && val.vizScript.isEmpty()) {
        val.modelScript = obj.model;
        val.paramScript = obj.param;
        val.vizScript = obj.viz;
        m_port = obj;
      }

      // Takes modified scripts from val
      obj.model = val.modelScript;
      obj.param = val.paramScript;
      obj.viz = val.vizScript;
      m_port = obj;

    }

    exec.setProgress(1);
    return new PortObject[] {obj};
  }

  @Override
  protected void performReset() {
    m_port = null;
  }

  @Override
  protected void useCurrentValueAsDefault() {}

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {}

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {}

  @Override
  public PortObject[] getInternalPortObjects() {
    return new PortObject[] {m_port};
  }

  @Override
  public void setInternalPortObjects(PortObject[] portObjects) {
    m_port = (FskPortObject) portObjects[0];
  }

  public void setHideInWizard(boolean hide) {}
}
