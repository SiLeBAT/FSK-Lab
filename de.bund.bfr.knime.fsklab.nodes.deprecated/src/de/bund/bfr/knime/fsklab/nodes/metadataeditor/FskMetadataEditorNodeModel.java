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
package de.bund.bfr.knime.fsklab.nodes.metadataeditor;

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

/**
 * Fsk meta data editor node model.
 */
@Deprecated
final class FskMetadataEditorNodeModel
    extends AbstractWizardNodeModel<FskMetadataEditorViewRepresentation, FskMetadataEditorViewValue>
    implements PortObjectHolder {

  private FskPortObject m_port;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private static final String VIEW_NAME =
      new FskMetadataEditorNodeFactory().getInteractiveViewName();

  public FskMetadataEditorNodeModel() {
    super(IN_TYPES, OUT_TYPES, VIEW_NAME);
  }

  @Override
  public FskMetadataEditorViewRepresentation createEmptyViewRepresentation() {
    return new FskMetadataEditorViewRepresentation();
  }

  @Override
  public FskMetadataEditorViewValue createEmptyViewValue() {
    return new FskMetadataEditorViewValue();
  }

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.nodes.metadataeditor";
  }

  @Override
  public boolean isHideInWizard() {
    return false;
  }

  @Override
  public ValidationError validateViewValue(FskMetadataEditorViewValue viewContent) {
    return null;
  }

  @Override
  public void saveCurrentValue(NodeSettingsWO content) {}

  @Override
  public FskMetadataEditorViewValue getViewValue() {
    FskMetadataEditorViewValue val;
    synchronized (getLock()) {
      val = super.getViewValue();
      if (val == null) {
        val = createEmptyViewValue();
      }
      if (val.metadata == null && m_port != null && m_port.template != null) {
        val.metadata = m_port.template;
      }
    }
    return val;
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return inSpecs;
  }

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec) {

    FskPortObject inObj = (FskPortObject) inObjects[0];
    FskPortObject outObj = new FskPortObject();
    // Clone input object
    outObj.model = inObj.model;
    outObj.param = inObj.param;
    outObj.viz = inObj.viz;
    outObj.template = inObj.template;
    outObj.workspace = inObj.workspace;
    outObj.libs.addAll(inObj.libs);

    synchronized (getLock()) {
      FskMetadataEditorViewValue val = getViewValue();

      // If not executed
      if (val.metadata == null) {
        val.metadata = inObj.template;
        m_port = inObj;
      }

      // Takes modified metadata from val
      outObj.template = val.metadata;
      m_port = inObj;
    }

    exec.setProgress(1);
    return new PortObject[] {outObj};
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
}
