package de.bund.bfr.knime.fsklab.nodes;

import java.io.IOException;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.SimulationEntity;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.ModelMath;
import metadata.Scope;

public class SimulatorNodeSettings {

  // Configuration keys
  private static final String CFG_METADATA = "metaData";
  private static final String CFG_SIMULATION_LIST = "simulations";

  // Setting models
  private static NodeLogger LOGGER = NodeLogger.getLogger("SimulatorNodeSettings");
  private List<SimulationEntity> listOfSimulation;

  private static final String CFG_GENERAL_INFORMATION = "generalInformation";
  private static final String CFG_SCOPE = "scope";
  private static final String CFG_DATA_BACKGROUND = "dataBackground";
  private static final String CFG_MODEL_MATH = "modelMath";

  
  GeneralInformation generalInformation;
  Scope scope;
  DataBackground dataBackground;
  ModelMath modelMath;
  void saveSettings(final NodeSettingsWO settings) {

    final ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;

    // save meta data
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

    if (listOfSimulation != null && listOfSimulation.size() > 0) {
      try {
        String stringVal = objectMapper.writeValueAsString(listOfSimulation);
        settings.addString(CFG_SIMULATION_LIST, stringVal);
      } catch (JsonProcessingException exception) {
        LOGGER.warn("Error saving meta data", exception);
      }
    }
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
    final ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;

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

    if (settings.containsKey(CFG_SIMULATION_LIST)) {
      final String stringVal = settings.getString(CFG_SIMULATION_LIST);

      try {
        listOfSimulation = (List<SimulationEntity>) objectMapper.readValue(stringVal,
            new TypeReference<List<SimulationEntity>>() {});
      } catch (IOException e) {
        throw new InvalidSettingsException(e);
      }
    }

    // Uses empty array if CFG_RESOURCES is missing (in case of old nodes).
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
  private static <T> T getEObject(NodeSettingsRO settings, String key, Class<T> valueType)
      throws InvalidSettingsException {

    ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
    String jsonStr = settings.getString(key);
    try {
      return mapper.readValue(jsonStr, valueType);
    } catch (IOException exception) {
      throw new InvalidSettingsException(exception);
    }
  }

  public List<SimulationEntity> getListOfSimulation() {
    return listOfSimulation;
  }

  public void setListOfSimulation(List<SimulationEntity> listOfSimulation) {
    this.listOfSimulation = listOfSimulation;
  }
}
