package de.bund.bfr.knime.fsklab.v1_9.joiner;

import java.util.Arrays;
import java.util.Objects;
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

  @Override
  public int hashCode() {
    return Objects.hash(firstModelParameters, secondModelParameters, thirdModelParameters,
        fourthModelParameters, firstModelName, secondModelName, thirdModelName, fourthModelName,
        firstModelType, secondModelType, thirdModelType, fourthModelType);
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
        && fourthModelType.equals(other.fourthModelType);
  }
}
