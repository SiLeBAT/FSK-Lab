package de.bund.bfr.knime.fsklab.nodes.v1_9;

import java.io.IOException;
import java.io.InputStream;
import com.fasterxml.jackson.databind.ObjectMapper;



public class FskPortObjectUtil {

  private FskPortObjectUtil() {
  }


  /**
   * Method to deserialize an object by temporarily setting a new classloader to work around a
   * deserialization bug in KNIME.
   * 
   * @param <T> type of the object to be deserialized.
   * @param classLoader of the FskPortObject.serializer instance
   * @param mapper: ObjectMapper
   * @param in : InputStream of object to be deserialized
   * @param valueType: Class of the object to be deserialized.
   * @return deserialized Object
   * @throws IOException
   */
  public static <T> T deserializeAfterClassloaderReset(ClassLoader classLoader, ObjectMapper mapper,
      InputStream in, Class<T> valueType) throws IOException {

    // Back up and configure class loader of current thread
    ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(classLoader);

    // deserialize working directory
    T actualManager = mapper.readValue(in, valueType);

    // Restore class loader
    Thread.currentThread().setContextClassLoader(originalClassLoader);

    return (T) actualManager;
  }
}
