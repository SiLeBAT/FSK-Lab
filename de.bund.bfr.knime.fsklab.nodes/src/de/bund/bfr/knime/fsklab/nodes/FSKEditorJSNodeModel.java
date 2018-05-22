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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataPackage;
import metadata.ModelMath;
import metadata.Scope;



/**
 * Fsk Editor JS node model.
 */

final class FSKEditorJSNodeModel
    extends AbstractWizardNodeModel<FSKEditorJSViewRepresentation, FSKEditorJSViewValue>
    implements PortObjectHolder {
  private static final NodeLogger LOGGER = NodeLogger.getLogger(FSKEditorJSViewValue.class);

  private final FSKEditorJSNodeSettings nodeSettings = new FSKEditorJSNodeSettings();
  private FskPortObject m_port;
  final static ResourceSet resourceSet = new ResourceSetImpl();
  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private static final String VIEW_NAME =
      new FSKEditorJSNodeFactory().getInteractiveViewName();

  public FSKEditorJSNodeModel() {
    super(IN_TYPES, OUT_TYPES, VIEW_NAME);
  }

  @Override
  public FSKEditorJSViewRepresentation createEmptyViewRepresentation() {
    return new FSKEditorJSViewRepresentation();
  }

  @Override
  public FSKEditorJSViewValue createEmptyViewValue() {
    
    return new FSKEditorJSViewValue();
  }

  @Override
  public String getJavascriptObjectID() {
    return "de.bund.bfr.knime.fsklab.js.FSKEditorJS";
  }

  @Override
  public boolean isHideInWizard() {
    return false;
  }

  @Override
  public ValidationError validateViewValue(FSKEditorJSViewValue viewContent) {
    return null;
  }

  @Override
  public void saveCurrentValue(NodeSettingsWO content) {}

  @Override
  public FSKEditorJSViewValue getViewValue() {
    FSKEditorJSViewValue val;
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

    return new PortObjectSpec[] { FskPortObjectSpec.INSTANCE };
  }

  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec) throws Exception{

    FskPortObject inObj1 = (FskPortObject) inObjects[0];
  
    FskPortObject outObj = inObj1;

    // Clone input object
   

    

    synchronized (getLock()) {
      FSKEditorJSViewValue fskEditorProxyValue = getViewValue();

      // If not executed
      if (fskEditorProxyValue.getGeneralInformation() == null) {
        fskEditorProxyValue.setGeneralInformation(FromEOjectToJSON(inObj1.generalInformation));
        fskEditorProxyValue.setScope(FromEOjectToJSON(inObj1.scope));
        fskEditorProxyValue.setDataBackground(FromEOjectToJSON(inObj1.dataBackground));
        fskEditorProxyValue.setModelMath(FromEOjectToJSON(inObj1.modelMath));
        fskEditorProxyValue.setFirstModelScript(inObj1.model);
        fskEditorProxyValue.setFirstModelViz(inObj1.viz);
       
        exec.setProgress(1);
      }
      outObj.generalInformation = getEObjectFromJson(fskEditorProxyValue.getGeneralInformation(),GeneralInformation.class);
      outObj.scope = getEObjectFromJson(fskEditorProxyValue.getScope(),Scope.class);
      outObj.dataBackground = getEObjectFromJson(fskEditorProxyValue.getDataBackground(),DataBackground.class);
      outObj.modelMath = getEObjectFromJson(fskEditorProxyValue.getModelMath(),ModelMath.class);
      outObj.model = fskEditorProxyValue.getFirstModelScript();
      outObj.viz = fskEditorProxyValue.getFirstModelViz();
      
    }
    
   
    return new PortObject[] {outObj};
  }

  private static String FromEOjectToJSON(final EObject eObject) throws JsonProcessingException {

    
      ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
      String jsonStr = objectMapper.writeValueAsString(eObject);
      return jsonStr;
   
  }

  private static <T> T getEObjectFromJson(String jsonStr, Class<T> valueType)
      throws InvalidSettingsException {

    
    try {
      ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory(objectMapper));
      resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
          MetadataPackage.eINSTANCE);
      Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
      InputStream stream = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));
      resource.load(stream, null);
      
      return (T) resource.getContents().get(0);
    } catch (IOException exception) {
      throw new InvalidSettingsException(exception);
    }
    
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
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
  }

 

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
