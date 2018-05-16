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
 ***************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataPackage;
import metadata.ModelMath;
import metadata.Scope;

public class EditorNodeSettings {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(EditorNodeSettings.class);

  // Configuration keys
  private static final String CFG_ORIGINAL_MODEL_SCRIPT = "originalModelScript";
  private static final String CFG_ORIGINAL_PARAMETERS_SCRIPT = "originalParametersScript";
  private static final String CFG_ORIGINAL_VISUALIZATION_SCRIPT = "originalVisualizationScript";

  private static final String CFG_MODIFIED_MODEL_SCRIPT = "modifiedModelScript";
  private static final String CFG_MODIFIED_PARAMETERS_SCRIPT = "modifiedParametersScript";
  private static final String CFG_MODIFIED_VISUALIZATION_SCRIPT = "modifiedVisualizationScript";

  private static final String CFG_GENERAL_INFORMATION = "generalInformation";
  private static final String CFG_SCOPE = "scope";
  private static final String CFG_DATA_BACKGROUND = "dataBackground";
  private static final String CFG_MODEL_MATH = "modelMath";

  private static final String CFG_RESOURCES = "resources";

  String originalModelScript;
  String originalParametersScript;
  String originalVisualizationScript;

  String modifiedModelScript;
  String modifiedParametersScript;
  String modifiedVisualizationScript;

  GeneralInformation generalInformation;
  Scope scope;
  DataBackground dataBackground;
  ModelMath modelMath;

  /** Paths to resources: plain text files and R workspace files (.rdata). */
  public List<Path> resources = new ArrayList<>();

  /**
   * Saves the settings into the given node settings object.
   * 
   * @param settings a node settings object
   * @throws JsonProcessingException
   */
  void saveSettings(final NodeSettingsWO settings) {

    settings.addString(CFG_ORIGINAL_MODEL_SCRIPT, originalModelScript);
    settings.addString(CFG_ORIGINAL_PARAMETERS_SCRIPT, originalParametersScript);
    settings.addString(CFG_ORIGINAL_VISUALIZATION_SCRIPT, originalVisualizationScript);

    settings.addString(CFG_MODIFIED_MODEL_SCRIPT, modifiedModelScript);
    settings.addString(CFG_MODIFIED_PARAMETERS_SCRIPT, modifiedParametersScript);
    settings.addString(CFG_MODIFIED_VISUALIZATION_SCRIPT, modifiedVisualizationScript);

    if (generalInformation != null) {
      saveSettings(settings, CFG_GENERAL_INFORMATION, generalInformation);
    }

    if (scope != null) {
      saveSettings(settings, CFG_SCOPE, scope);
    }

    if (dataBackground != null) {
      saveSettings(settings, CFG_DATA_BACKGROUND, dataBackground);
    }

    if (modelMath != null) {
      saveSettings(settings, CFG_MODEL_MATH, modelMath);
    }

    final String[] resourcesArray = resources.stream().map(Path::toString).toArray(String[]::new);
    settings.addStringArray(CFG_RESOURCES, resourcesArray);
  }

  /**
   * Loads the settings from the given node settings object.
   * 
   * @param settings a node settings object.
   * @throws InvalidSettingsException
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

    originalModelScript = settings.getString(CFG_ORIGINAL_MODEL_SCRIPT);
    originalParametersScript = settings.getString(CFG_ORIGINAL_PARAMETERS_SCRIPT);
    originalVisualizationScript = settings.getString(CFG_ORIGINAL_VISUALIZATION_SCRIPT);

    modifiedModelScript = settings.getString(CFG_MODIFIED_MODEL_SCRIPT);
    modifiedParametersScript = settings.getString(CFG_MODIFIED_PARAMETERS_SCRIPT);
    modifiedVisualizationScript = settings.getString(CFG_MODIFIED_VISUALIZATION_SCRIPT);

    // load meta data
    if (settings.containsKey(CFG_GENERAL_INFORMATION)) {
      generalInformation = getEObject(settings, CFG_GENERAL_INFORMATION, GeneralInformation.class);
    }
    if (settings.containsKey(CFG_SCOPE)) {
      scope = getEObject(settings, CFG_SCOPE, Scope.class);
    }
    if (settings.containsKey(CFG_DATA_BACKGROUND)) {
      dataBackground = getEObject(settings, CFG_DATA_BACKGROUND, DataBackground.class);
    }
    if (settings.containsKey(CFG_MODEL_MATH)) {
      modelMath = getEObject(settings, CFG_MODEL_MATH, ModelMath.class);
    }

    // Uses empty array if CFG_RESOURCES is missing (in case of old nodes).
    resources.clear();
    Arrays.stream(settings.getStringArray(CFG_RESOURCES, new String[0])).map(Paths::get)
        .forEach(resources::add);
  }

  private static void saveSettings(final NodeSettingsWO settings, final String key,
      final EObject eObject) {

    try {
      ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;
      String jsonStr = objectMapper.writeValueAsString(eObject);
      settings.addString(key, jsonStr);

    } catch (JsonProcessingException exception) {
      LOGGER.warn("Error saving " + key);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> T getEObject(NodeSettingsRO settings, String key, Class<T> valueType)
      throws InvalidSettingsException {

    String jsonStr = settings.getString(key);

    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;

    try {
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
          .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory(mapper));
      resourceSet.getPackageRegistry().put(MetadataPackage.eNS_URI, MetadataPackage.eINSTANCE);

      Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
      InputStream stream = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));
      resource.load(stream, null);

      return (T) resource.getContents().get(0);
    } catch (IOException exception) {
      throw new InvalidSettingsException(exception);
    }
  }
}
