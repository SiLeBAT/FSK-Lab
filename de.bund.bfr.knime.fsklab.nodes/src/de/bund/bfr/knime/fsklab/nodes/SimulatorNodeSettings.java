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

  private static final String CFG_Model_Script = "ModelScript";
  

  private String modelStript;
  private List<SimulationEntity> listOfSimulation;

  void saveSettings(final NodeSettingsWO settings) {

    final ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;

    // save model Script
    if (modelStript != null) {
      settings.addString( CFG_Model_Script, modelStript);
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
    if (settings.containsKey(CFG_Model_Script)) {
      modelStript = settings.getString(CFG_Model_Script);
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
 

  public List<SimulationEntity> getListOfSimulation() {
    return listOfSimulation;
  }

  public void setListOfSimulation(List<SimulationEntity> listOfSimulation) {
    this.listOfSimulation = listOfSimulation;
  }
  
  public String getModelStript() {
    return modelStript;
  }

  public void setModelStript(String modelStript) {
    this.modelStript = modelStript;
  }

}
