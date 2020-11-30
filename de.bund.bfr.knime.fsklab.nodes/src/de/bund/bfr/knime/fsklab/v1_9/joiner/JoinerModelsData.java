package de.bund.bfr.knime.fsklab.v1_9.joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.metadata.swagger.Parameter;

public class JoinerModelsData {
  public String firstModelType;
  public String secondModelType;
  public String thirdModelType;
  public String fourthModelType;
  public Parameter[] firstModelParameters;
  public Parameter[] secondModelParameters;
  public Parameter[] thirdModelParameters;
  public Parameter[] fourthModelParameters;

  public String firstModelName;
  public String secondModelName;
  public String thirdModelName;
  public String fourthModelName;

  public String[] firstModel;
  public String[] secondModel;
  public String[] thirdModel;
  public String[] fourthModel;
  public boolean interactiveMode = false;
  public int numberOfModels = 0;
  public List<FskSimulation> joinedSimulation = new ArrayList<>();
  public Map <String, Map<String,String>> modelsParamsOriginalNames = new HashMap<>();

  @Override
  public int hashCode() {
    return Objects.hash(firstModelParameters, secondModelParameters, thirdModelParameters,
        fourthModelParameters, firstModelName, secondModelName, thirdModelName, fourthModelName,
        firstModelType, secondModelType, thirdModelType, fourthModelType,modelsParamsOriginalNames);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }

    JoinerModelsData other = (JoinerModelsData) obj;
    return Arrays.deepEquals(firstModelParameters, other.firstModelParameters)
        && Arrays.deepEquals(secondModelParameters, other.secondModelParameters)
        && Arrays.deepEquals(thirdModelParameters, other.thirdModelParameters)
        && Arrays.deepEquals(fourthModelParameters, other.fourthModelParameters)
        && firstModelName.equals(other.firstModelName)
        && secondModelName.equals(other.secondModelName)
        && thirdModelName.equals(other.thirdModelName)
        && fourthModelName.equals(other.fourthModelName)
        && firstModelType.equals(other.firstModelType)
        && secondModelType.equals(other.secondModelType)
        && thirdModelType.equals(other.thirdModelType)
        && fourthModelType.equals(other.fourthModelType)
        && modelsParamsOriginalNames.equals(other.modelsParamsOriginalNames);
  }
}
