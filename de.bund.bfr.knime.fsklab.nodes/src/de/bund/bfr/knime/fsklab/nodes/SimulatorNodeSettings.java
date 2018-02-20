package de.bund.bfr.knime.fsklab.nodes;

import java.io.IOException;
import java.util.List;
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
import de.bund.bfr.knime.fsklab.rakip.GenericModel;

public class SimulatorNodeSettings {

  // Configuration keys
  private static final String CFG_METADATA = "metaData";
  private static final String CFG_SIMULATION_LIST = "simulations";

  // Setting models
  private static NodeLogger LOGGER = NodeLogger.getLogger("SimulatorNodeSettings");
  private List<SimulationEntity> listOfSimulation;

  GenericModel genericModel;

  void saveSettings(final NodeSettingsWO settings) {

    final ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;

    // save meta data
    if (genericModel != null) {
      try {
        String stringVal = objectMapper.writeValueAsString(genericModel);
        settings.addString(CFG_METADATA, stringVal);
      } catch (JsonProcessingException exception) {
        LOGGER.warn("Error saving meta data", exception);
      }
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
    if (settings.containsKey(CFG_METADATA)) {
      final String stringVal = settings.getString("metaData");
      try {
        genericModel = objectMapper.readValue(stringVal, GenericModel.class);
      } catch (IOException e) {
        throw new InvalidSettingsException(e);
      }
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
}
