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

import java.util.HashSet;
import java.util.List;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.core.util.FileUtil;
import org.knime.js.core.node.AbstractWizardNodeModel;
import de.bund.bfr.knime.fsklab.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.CombinedFskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.JoinRelation;
import de.bund.bfr.knime.fsklab.ParameterizedModel;


/**
 * Fsk Joiner node model.
 */

final class JoinerNodeModel
    extends AbstractWizardNodeModel<JoinerViewRepresentation, JoinerViewValue>
    implements PortObjectHolder {
  private final JoinerNodeSettings nodeSettings = new JoinerNodeSettings();
  private FskPortObject m_port;
  //JoinerViewValue joinerProxyValue = new JoinerViewValue();
 
  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE,FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {CombinedFskPortObject.TYPE};

  private static final String VIEW_NAME =
      new JoinerNodeFactory().getInteractiveViewName();

  public JoinerNodeModel() {
    super(IN_TYPES, OUT_TYPES, VIEW_NAME);
  }

  @Override
  public JoinerViewRepresentation createEmptyViewRepresentation() {
    return new JoinerViewRepresentation();
  }

  @Override
  public JoinerViewValue createEmptyViewValue() {
    
    return new JoinerViewValue();
  }

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.js.joiner";
  }

  @Override
  public boolean isHideInWizard() {
    return false;
  }

  @Override
  public ValidationError validateViewValue(JoinerViewValue viewContent) {
    return null;
  }

  @Override
  public void saveCurrentValue(NodeSettingsWO content) {}

  @Override
  public JoinerViewValue getViewValue() {
    JoinerViewValue val;
    synchronized (getLock()) {
      val = super.getViewValue();
      if (val == null) {
        val = createEmptyViewValue();
      }
      
    }
    return val;
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] { CombinedFskPortObjectSpec.INSTANCE };
  }

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec) throws Exception{

    FskPortObject inObj1 = (FskPortObject) inObjects[0];
    FskPortObject inObj2 = (FskPortObject) inObjects[1];
    CombinedFskPortObject outObj = new CombinedFskPortObject(FileUtil.createTempDir("combined").toPath(),new HashSet<>(),inObj1,inObj2);
    
    // Clone input object
   

    

    synchronized (getLock()) {
      JoinerViewValue joinerProxyValue = getViewValue();

      // If not executed
      if (joinerProxyValue.getFirstModel() == null) {
        joinerProxyValue.setFirstModel(new ParameterizedModel(inObj1.genericModel.generalInformation.name, inObj1.genericModel.modelMath.parameter));
        joinerProxyValue.setSecondModel(new ParameterizedModel(inObj2.genericModel.generalInformation.name, inObj2.genericModel.modelMath.parameter));
       //val.metadata = inObj.template;
       // m_port = inObj;
        if(nodeSettings.jsonRepresentation != null && !nodeSettings.jsonRepresentation.equals("")) {
          joinerProxyValue.setJsonRepresentation(nodeSettings.jsonRepresentation);
        }
        exec.setProgress(1);
      }

      // Takes modified metadata from val
      // outObj.template = val.metadata;
      List <JoinRelation> joinerRelation = joinerProxyValue.getJoinRelations();
      for(JoinRelation jr :joinerRelation) {
        System.out.println(jr.getSourceParam().name);
        System.out.println(jr.getTargetParam().name);
      }
      nodeSettings.jsonRepresentation = joinerProxyValue.getJsonRepresentation();
      outObj.setJoinerRelation(joinerRelation);
    }
    
   
    return new PortObject[] {outObj};
  }

  @Override
  protected void performReset() {
    m_port = null;
  }

  @Override
  protected void useCurrentValueAsDefault() {}

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

 

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
