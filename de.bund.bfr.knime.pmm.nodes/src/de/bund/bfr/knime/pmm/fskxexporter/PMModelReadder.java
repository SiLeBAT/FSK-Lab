package de.bund.bfr.knime.pmm.fskxexporter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.knime.core.node.BufferedDataTable;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

public class PMModelReadder {
  protected Map<String, String> modelNames;
  protected Map<String, List<String>> parameters;
  protected Map<String, Map<String, Double>> minValues;
  protected Map<String, Map<String, Double>> maxValues;
  protected static final String PRIMARY = "Primary";
  protected static final String SECONDARY = "Secondary";

  protected void readPrimaryTable(BufferedDataTable table) {
    modelNames = new LinkedHashMap<>();
    parameters = new LinkedHashMap<>();
    minValues = new LinkedHashMap<>();
    maxValues = new LinkedHashMap<>();

    KnimeRelationReader reader = new KnimeRelationReader(SchemaFactory.createM1Schema(), table);

    while (reader.hasMoreElements()) {
      KnimeTuple tuple = reader.nextElement();
      PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
      String id = PRIMARY + ((CatalogModelXml) modelXml.get(0)).id;

      if (!modelNames.containsKey(id)) {
        PmmXmlDoc params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
        List<String> paramNames = new ArrayList<>();
        Map<String, Double> min = new LinkedHashMap<>();
        Map<String, Double> max = new LinkedHashMap<>();

        for (PmmXmlElementConvertable el : params.getElementSet()) {
          ParamXml element = (ParamXml) el;

          paramNames.add(element.name);
          min.put(element.name, element.min);
          max.put(element.name, element.max);
        }

        modelNames.put(id, ((CatalogModelXml) modelXml.get(0)).name);
        parameters.put(id, paramNames);
        minValues.put(id, min);
        maxValues.put(id, max);
      }
    }
  }

  protected void readSecondaryTable(BufferedDataTable table) {
    readPrimaryTable(table);

    KnimeRelationReader reader = new KnimeRelationReader(SchemaFactory.createM2Schema(), table);

    while (reader.hasMoreElements()) {
      KnimeTuple tuple = reader.nextElement();
      PmmXmlDoc modelXml = tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
      String id = SECONDARY + ((CatalogModelXml) modelXml.get(0)).id;

      if (!modelNames.containsKey(id)) {
        PmmXmlDoc params = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);
        List<String> paramNames = new ArrayList<>();
        Map<String, Double> min = new LinkedHashMap<>();
        Map<String, Double> max = new LinkedHashMap<>();

        for (PmmXmlElementConvertable el : params.getElementSet()) {
          ParamXml element = (ParamXml) el;

          paramNames.add(element.name);
          min.put(element.name, element.min);
          max.put(element.name, element.max);
        }

        modelNames.put(id, ((CatalogModelXml) modelXml.get(0)).name);
        parameters.put(id, paramNames);
        minValues.put(id, min);
        maxValues.put(id, max);
      }
    }
  }
}
