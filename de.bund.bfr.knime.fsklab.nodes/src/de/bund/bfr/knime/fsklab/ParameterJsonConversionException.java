package de.bund.bfr.knime.fsklab;

/**
 * This exception is used in the {@link de.bund.bfr.knime.fsklab.nodes.PythonJsonHandler} when a
 * parameter has an unsupported data type and/or cannot be serialized to JSON string.
 */
public class ParameterJsonConversionException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -2993479528312638078L;


  /**
   * Constructs an <code>ParameterJsonConversionException</code> with the specified detail message.
   * 
   * @param parameter name of the parameter that could not be serialized
   */
  public ParameterJsonConversionException(String parameter) {

    // super(err);
    FskErrorMessages.parameterJsonConversionError(parameter);

  }
}
