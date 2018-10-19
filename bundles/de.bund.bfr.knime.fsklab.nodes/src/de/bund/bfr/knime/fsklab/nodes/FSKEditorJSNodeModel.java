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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.io.FileUtils;
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
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.util.FileUtil;
import org.knime.js.core.node.AbstractWizardNodeModel;
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
  private static final int BUFFER_SIZE = 4096;

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

    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }


  /**
   * Downloads a file from a URL
   * 
   * @param fileURL HTTP URL of the file to be downloaded
   * @param workingDir path of the directory to save the file
   * @throws IOException
   */
  public void downloadFileToWorkingDir(String fileURL, String workingDir, String JWT)
      throws IOException {
    URL url = new URL(fileURL);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    httpConn.setRequestProperty("Authorization", "Bearer" + JWT);
    int responseCode = httpConn.getResponseCode();

    // always check HTTP response code first
    if (responseCode == HttpURLConnection.HTTP_OK) {
      String fileName = "";
      String disposition = httpConn.getHeaderField("Content-Disposition");
      String contentType = httpConn.getContentType();
      int contentLength = httpConn.getContentLength();

      if (disposition != null) {
        // extracts file name from header field
        int index = disposition.indexOf("filename=");
        if (index > 0) {
          fileName = disposition.substring(index + 10, disposition.length() - 1);
        }
      } else {
        // extracts file name from URL
        fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
      }

      // opens input stream from the HTTP connection
      InputStream inputStream = httpConn.getInputStream();
      String saveFilePath = workingDir + File.separator + fileName;

      // opens an output stream to save into file
      FileOutputStream outputStream = new FileOutputStream(saveFilePath);

      int bytesRead = -1;
      byte[] buffer = new byte[BUFFER_SIZE];
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      outputStream.close();
      inputStream.close();

      System.out.println("File downloaded");
    } else {
      System.out.println("No file to download. Server replied HTTP code: " + responseCode);
    }
    httpConn.disconnect();
  }

  /**
   * Connect to get JWT token and Downloads files
   * 
   * @param serverName that host the resources
   * @param resources array of resource URL on the server
   * @param workingDir path of the directory to save the file
   */
  public void connectAndDownloadFilesOnServer(String serverName, String[] resources,
      String workingDir) {
    try {
      String JWT = "";
      String requestString = serverName + "knime/rest/session";
      URL url = new URL(requestString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

      String output;
      System.out.println("Output from Server .... \n");

      while ((output = br.readLine()) != null) {
        JWT += output;
      }
      conn.disconnect();
      for (String fileRequestString : resources) {
        LOGGER.info("JS EDITOR  " + serverName+">>>>>>"+
            fileRequestString+">>>>>>"+workingDir);
        downloadFileToWorkingDir(fileRequestString, workingDir, JWT);
      }
      // TODO remove the temp folder on the server
    } catch (MalformedURLException e) {

      e.printStackTrace();

    } catch (IOException e) {

      e.printStackTrace();

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
      outObj = inObj1;

      outObj.generalInformation =
          getEObjectFromJson(fskEditorProxyValue.getGeneralInformation(), GeneralInformation.class);
      outObj.scope = getEObjectFromJson(fskEditorProxyValue.getScope(), Scope.class);
      outObj.dataBackground =
          getEObjectFromJson(fskEditorProxyValue.getDataBackground(), DataBackground.class);
      outObj.modelMath = getEObjectFromJson(fskEditorProxyValue.getModelMath(), ModelMath.class);

      // Create simulation
      if (outObj.modelMath.getParameter() != null && outObj.modelMath.getParameter().size() > 0
          && outObj.simulations.size() == 0) {
        FskSimulation defaultSimulation =
            NodeUtils.createDefaultSimulation(outObj.modelMath.getParameter());
        outObj.simulations.add(defaultSimulation);
      }

      outObj.model = fskEditorProxyValue.getFirstModelScript();
      outObj.viz = fskEditorProxyValue.getFirstModelViz();
      LOGGER.info("JS EDITOR  " + fskEditorProxyValue.getResourcesFiles());
      //resources will only be available via online mode of the editor
      if (fskEditorProxyValue.getResourcesFiles()!=null && fskEditorProxyValue.getResourcesFiles().length != 0 ) {
        
        connectAndDownloadFilesOnServer(fskEditorProxyValue.getServerName(),
            fskEditorProxyValue.getResourcesFiles(), outObj.getWorkingDirectory());
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
