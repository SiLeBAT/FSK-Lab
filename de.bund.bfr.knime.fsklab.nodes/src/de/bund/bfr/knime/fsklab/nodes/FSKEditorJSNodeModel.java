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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.web.ValidationError;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.util.FileUtil;
import org.knime.core.util.IRemoteFileUtilsService;
import org.knime.js.core.node.AbstractWizardNodeModel;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.FskSimulation;
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
  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx JS Editor Model");

  private final FSKEditorJSNodeSettings nodeSettings = new FSKEditorJSNodeSettings();
  private FskPortObject m_port;

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE_OPTIONAL};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  private static final String VIEW_NAME = new FSKEditorJSNodeFactory().getInteractiveViewName();

  static final AtomicLong TEMP_DIR_UNIFIER = new AtomicLong((int) (100000 * Math.random()));

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
  //This method is being called just when user click apply or close with saving options
  @Override
  public ValidationError validateViewValue(FSKEditorJSViewValue viewContent) {
    // TODO 
    /* Use SchemaFactory to create new Schema and Validator from it and then apply
     * validator.validate() to validate the EMF Object. Any error after the validate can be return
     * back to the javascript view as ValidationError Object which can contains the error message
     * EObject generalInformation;
     * try {
     * generalInformation = getEObjectFromJson(viewContent.getGeneralInformation(), GeneralInformation.class);
     * EObject feed = (EObject)generalInformation.eGet(generalInformation.eClass().getEStructuralFeature("feed")); 
     *
     * Diagnostician validator = Diagnostician.INSTANCE;
     *
     * // Validate the feed and inspect the resulting diagnostic.
     * org.eclipse.emf.common.util.Diagnostic diagnostic = validator.validate(feed);
     *
     * return new ValidationError(diagnostic.toString());
     * } catch (InvalidSettingsException e) {
     *             e.printStackTrace();
     * }
     * 
     * */
    
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

    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  /**
   * Downloads a file from a URL.The code here is considering that the fileURL is using KNIME
   * Protocol
   * 
   * @param fileURL HTTP URL of the file to be downloaded
   * @param workingDir path of the directory to save the file
   * @throws IOException
   * @throws URISyntaxException
   * @throws InvalidSettingsException
   */
  public void downloadFileToWorkingDir(String fileURL, String workingDir)
      throws IOException, URISyntaxException, InvalidSettingsException {
    String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
    String destinationPath = workingDir + File.separator + fileName;
    File fileTodownload = new File(destinationPath);
    LOGGER.info("JS EDITOR  path to write to: " + destinationPath);
    try (InputStream inStream = FileUtil.openInputStream(fileURL);
        OutputStream outStream = new FileOutputStream(fileTodownload)) {
      IOUtils.copy(inStream, outStream);
    }
  }



  @Override
  protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {

    FskPortObject inObj1;
    FskPortObject outObj;
   
    if (inObjects.length > 0 && inObjects[0] != null) {
      inObj1 = (FskPortObject) inObjects[0];
    } else {
      String workingDirectory = "";
      String readme = "";

      // Import readme
      if (!nodeSettings.getReadme().isEmpty()) {
        File readmeFile = FileUtil.getFileFromURL(FileUtil.toURL(nodeSettings.getReadme()));
        readme = FileUtils.readFileToString(readmeFile, "UTF-8");
      }

      if (!nodeSettings.getWorkingDirectory().isEmpty()) {
        workingDirectory = nodeSettings.getWorkingDirectory();
      } else {
        // each sub Model has it's own working directory to avoid resource conflict.
        // get current node's and workflow's context
        NodeContext nodeContext = NodeContext.getContext();
        WorkflowManager wfm = nodeContext.getWorkflowManager();
        WorkflowContext workflowContext = wfm.getContext();
        File currentWorkingDirectory =
            new File(workflowContext.getCurrentLocation(),
                nodeContext.getNodeContainer().getNameWithID().toString().replaceAll("\\W", "")
                    .replace(" ", "") + "_" + "workingDirectory"
                    + TEMP_DIR_UNIFIER.getAndIncrement());
        currentWorkingDirectory.mkdir();
        workingDirectory = currentWorkingDirectory.getPath();
      }
      inObj1 = new FskPortObject(workingDirectory, readme, new ArrayList<>());
      inObj1.model = "";
      inObj1.viz = "";
    }

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
        fskEditorProxyValue.setREADME(inObj1.getReadme());

        exec.setProgress(1);
      }
      else {
        if(fskEditorProxyValue.isNotCompleted()) {
          setWarningMessage("Output Parameters are not configured correctly");
        }
        if(StringUtils.isNotEmpty(fskEditorProxyValue.getValidationErrors())) {
          setWarningMessage( "\n"+(fskEditorProxyValue.getValidationErrors()).replaceAll("\"", "").replaceAll(",,,", "\n"));
        }
      }
      outObj = inObj1;

      outObj.generalInformation =
          getEObjectFromJson(fskEditorProxyValue.getGeneralInformation(), GeneralInformation.class);
      outObj.scope = getEObjectFromJson(fskEditorProxyValue.getScope(), Scope.class);
      outObj.dataBackground =
          getEObjectFromJson(fskEditorProxyValue.getDataBackground(), DataBackground.class);
      outObj.modelMath = getEObjectFromJson(fskEditorProxyValue.getModelMath(), ModelMath.class);

      // Create simulation
      if (outObj.modelMath.getParameter() != null && outObj.modelMath.getParameter().size() > 0) {
        FskSimulation defaultSimulation =
            NodeUtils.createDefaultSimulation(outObj.modelMath.getParameter());
        outObj.simulations.remove(0);
        outObj.simulations.add(defaultSimulation);
      }

      outObj.model = fskEditorProxyValue.getFirstModelScript();
      outObj.viz = fskEditorProxyValue.getFirstModelViz();

      // resources files via fskEditorProxyValue will be available only in online mode of the JS
      // editor
      if (fskEditorProxyValue.getResourcesFiles() != null
          && fskEditorProxyValue.getResourcesFiles().length != 0) {
        for (String fileRequestString : fskEditorProxyValue.getResourcesFiles()) {
          downloadFileToWorkingDir(fileRequestString, outObj.getWorkingDirectory());
        }
        // delete the parent folder of the uploaded files after moving them to the working
        // directory.
        // parentFolderPath is always uses KNIME protocol
        String firstFile = fskEditorProxyValue.getResourcesFiles()[0];
        String parentFolderPath = firstFile.substring(0, firstFile.lastIndexOf("/"));
        BundleContext ctx =
            FrameworkUtil.getBundle(IRemoteFileUtilsService.class).getBundleContext();
        ServiceReference<IRemoteFileUtilsService> ref =
            ctx.getServiceReference(IRemoteFileUtilsService.class);
        if (ref != null) {
          try {
            ctx.getService(ref).delete(new URL(parentFolderPath));
          } finally {
            ctx.ungetService(ref);
          }
        }

      }
      // Collect R packages
      final Set<String> librariesSet = new HashSet<>();
      librariesSet.addAll(new RScript(outObj.model).getLibraries());
      librariesSet.addAll(new RScript(outObj.viz).getLibraries());
      outObj.packages.addAll(new ArrayList<>(librariesSet));
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
    final ResourceSet resourceSet = new ResourceSetImpl();
    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
        .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory(mapper));
    resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
        MetadataPackage.eINSTANCE);

    Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
    InputStream inStream = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));
    try {
      resource.load(inStream, null);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return (T) resource.getContents().get(0);
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
