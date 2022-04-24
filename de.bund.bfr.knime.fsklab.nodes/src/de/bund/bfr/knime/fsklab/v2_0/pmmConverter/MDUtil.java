package de.bund.bfr.knime.fsklab.v2_0.pmmConverter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import org.jdom2.JDOMException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

public class MDUtil {
  public static File writeJson(List<KnimeTuple> tuples, String modelName, Path path)
      throws IOException {
    if (tuples == null)
      return null;
    StringBuilder jsonCollector = new StringBuilder();
    jsonCollector.append("{");
    tuples.forEach(knimeTuble -> {
      if (knimeTuble.getCell("MD_Data") == null)
        return;
      String timeSeriesXml = knimeTuble.getCell("MD_Data").toString();
      String CondID = knimeTuble.getCell("CondID").toString();
      String CombaseID = knimeTuble.getCell("CombaseID").toString();
      jsonCollector.append("\"" + CondID + "_" + CombaseID + "\":{");
      String Organism = knimeTuble.getCell("Organism").toString();
      PmmXmlDoc organismXml;
      try {
        organismXml = new PmmXmlDoc(Organism);
        organismXml.getElementSet().forEach(element -> {
          ObjectMapper mapper = new ObjectMapper();
          try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
            jsonCollector.append("\"" + "organism\" : " + json + ",");
            // System.out.println(json);
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } catch (JDOMException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      String Matrix = knimeTuble.getCell("Matrix").toString();
      PmmXmlDoc matrixXml;
      try {
        matrixXml = new PmmXmlDoc(Matrix);
        matrixXml.getElementSet().forEach(element -> {
          ObjectMapper mapper = new ObjectMapper();
          try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
            jsonCollector.append("\"" + "matrix\" : " + json + ",");
            // System.out.println(json);
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } catch (JDOMException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      String Misc = knimeTuble.getCell("Misc").toString();
      PmmXmlDoc miscXml;
      jsonCollector.append("\"" + "Misc\" : [");
      try {
        miscXml = new PmmXmlDoc(Misc);
        for (int miscIndex = 0; miscIndex < miscXml.getElementSet().size(); miscIndex++) {
          PmmXmlElementConvertable element = miscXml.getElementSet().get(miscIndex);
          ObjectMapper mapper = new ObjectMapper();
          try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
            if (miscIndex == miscXml.getElementSet().size() - 1)
              jsonCollector.append(json);
            else
              jsonCollector.append(json + ",");
            // System.out.println(json +"}");
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        } ;

        jsonCollector.append(" ],");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (JDOMException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      String MD_Info = knimeTuble.getCell("MD_Info").toString();
      PmmXmlDoc mD_InfoXml;
      try {
        mD_InfoXml = new PmmXmlDoc(MD_Info);
        mD_InfoXml.getElementSet().forEach(element -> {
          ObjectMapper mapper = new ObjectMapper();
          try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
            jsonCollector.append("\"" + "mD_Info\" : " + json + ",");
            // jsonCollector.append(json);
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } catch (JDOMException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      String MD_Literatur = knimeTuble.getCell("MD_Literatur").toString();
      PmmXmlDoc MD_LiteraturXML;
      try {
        MD_LiteraturXML = new PmmXmlDoc(MD_Literatur);
        MD_LiteraturXML.getElementSet().forEach(element -> {
          ObjectMapper mapper = new ObjectMapper();
          try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
            jsonCollector.append("\"" + "MD_Literatur\" : " + json + ",");
            // jsonCollector.append(json);
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        });
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } catch (JDOMException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      String MD_DB_UID = knimeTuble.getCell("MD_DB_UID").toString();

      PmmXmlDoc timeSeries = null;
      jsonCollector.append("\"" + "timeSeries\" : [");
      try {
        timeSeries = new PmmXmlDoc(timeSeriesXml);
        for (int timeIndex = 0; timeIndex < timeSeries.getElementSet().size(); timeIndex++) {
          PmmXmlElementConvertable element = timeSeries.getElementSet().get(timeIndex);
          ObjectMapper mapper = new ObjectMapper();
          try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
            if (timeIndex == timeSeries.getElementSet().size() - 1)
              jsonCollector.append(json);
            else
              jsonCollector.append(json + ",");
            // System.out.println(json +"}");
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        } ;

        jsonCollector.append(" ]");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (JDOMException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      jsonCollector.append("},");
    });
    String preJSON = jsonCollector.substring(0, jsonCollector.length() - 1);
    preJSON += "}";

    File tempFile = File.createTempFile(modelName, ".json", path.toFile());
    try (PrintWriter out = new PrintWriter(tempFile)) {
      out.write(preJSON.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    // File tempFile = File.createTempFile("MyAppName-", ".tmp");
    tempFile.deleteOnExit();
    return tempFile;
  }
}
