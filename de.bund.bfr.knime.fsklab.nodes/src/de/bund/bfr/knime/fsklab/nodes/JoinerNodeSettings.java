package de.bund.bfr.knime.fsklab.nodes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.FSKJoinRelation;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;

public class JoinerNodeSettings {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(JoinerNodeSettings.class);

  // Configuration keys
  private static final String CFG_METADATA = "metaDataBaseModel";
  private static final String CFG_JGM_METADATA = "joinedGenericModelsmetaData";
  private static final String CFG_FSKJOINRELATION = "fskjoinrelation";

  GenericModel genericModel;
  List<GenericModel> joinedGenericModels;
  List<FSKJoinRelation> listOfFSKJoinRelation;

  /** Paths to resources: plain text files and R workspace files (.rdata). */
  public List<Path> resources = new ArrayList<>();

  /**
   * Saves the settings into the given node settings object.
   * 
   * @param settings a node settings object
   * @throws JsonProcessingException
   */
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

    if (joinedGenericModels != null) {
      try {
        String stringVal = objectMapper.writeValueAsString(joinedGenericModels);
        settings.addString(CFG_JGM_METADATA, stringVal);
      } catch (JsonProcessingException exception) {
        LOGGER.warn("Error saving meta data", exception);
      }
    }

    if (listOfFSKJoinRelation != null) {
      objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
      try {
        String stringVal = objectMapper.writeValueAsString(listOfFSKJoinRelation);
        settings.addString(CFG_FSKJOINRELATION, stringVal);
        LOGGER.info("Saving successed");
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
      final String stringVal = settings.getString(CFG_METADATA);
      try {
        genericModel = objectMapper.readValue(stringVal, GenericModel.class);
      } catch (IOException e) {
        throw new InvalidSettingsException(e);
      }
    }

    if (settings.containsKey(CFG_JGM_METADATA)) {
      final String stringVal = settings.getString(CFG_JGM_METADATA);
      try {
        @SuppressWarnings("unchecked")
        List<GenericModel> loadedModels =
            (List<GenericModel>) objectMapper.readValue(stringVal, List.class);
        joinedGenericModels = loadedModels;
      } catch (IOException e) {
        throw new InvalidSettingsException(e);
      }
    }

    if (settings.containsKey(CFG_FSKJOINRELATION)) {
      final String stringVal = settings.getString(CFG_FSKJOINRELATION);
      try {
        @SuppressWarnings("unchecked")
        List<FSKJoinRelation> relations = (List<FSKJoinRelation>) objectMapper.readValue(stringVal,
            new TypeReference<List<FSKJoinRelation>>() {});
        listOfFSKJoinRelation = relations;
      } catch (IOException e) {
        throw new InvalidSettingsException(e);
      }
    }

  }
}
