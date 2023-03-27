package de.bund.bfr.knime.fsklab.v2_0.pmmConverter;

import java.util.Date;
import java.util.List;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.SBMLFactory;

public class PMFWriter {
  private static boolean identicalEstModels(List<KnimeTuple> tuples) {
    int id = ((EstModelXml) tuples.get(0).getPmmXml(Model1Schema.ATT_ESTMODEL).get(0)).id;
    for (KnimeTuple tuple : tuples.subList(1, tuples.size())) {
      EstModelXml estModel = (EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL).get(0);
      if (id != estModel.id) {
        return false;
      }
    }
    return true;
  }

  public static void write(BufferedDataTable inData, String dir, String mdName, boolean isSecondary,
      ExecutionContext exec) throws Exception {
    KnimeSchema schema = null;
    ModelType modelType = null;
    List<KnimeTuple> tuples;

    DataTableSpec spec = inData.getSpec();
    // Table has the structure Model1 + Model2 + Data
    if (SchemaFactory.conformsM12DataSchema(spec)) {
      schema = SchemaFactory.createM12DataSchema();
      tuples = PmmUtilities.getTuples(inData, schema);
      if (WriterUtils.hasData(tuples)) {
        boolean identical = identicalEstModels(tuples);
        if (isSecondary) {
          modelType =
              identical ? ModelType.ONE_STEP_SECONDARY_MODEL : ModelType.TWO_STEP_SECONDARY_MODEL;
        } else {
          modelType =
              identical ? ModelType.ONE_STEP_TERTIARY_MODEL : ModelType.TWO_STEP_TERTIARY_MODEL;
        }
      } else {
        modelType = ModelType.MANUAL_TERTIARY_MODEL;
      }
    }

    // Table has Model1 + Data
    else if (SchemaFactory.conformsM1DataSchema(spec)) {
      schema = SchemaFactory.createM1DataSchema();
      tuples = PmmUtilities.getTuples(inData, schema);

      // Check every tuple. If any tuple has data (number of data points >
      // 0) then assigns PRIMARY_MODEL_WDATA. Otherwise it assigns
      // PRIMARY_MODEL_WODATA
      modelType = ModelType.PRIMARY_MODEL_WODATA;
      for (KnimeTuple tuple : tuples) {
        PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
        if (mdData.size() > 0) {
          modelType = ModelType.PRIMARY_MODEL_WDATA;
          break;
        }
      }
    }

    // Table only has data
    else if (SchemaFactory.conformsDataSchema(spec)) {
      schema = SchemaFactory.createDataSchema();
      tuples = PmmUtilities.getTuples(inData, schema);
      modelType = ModelType.EXPERIMENTAL_DATA;
    }

    // Table only has secondary model cells
    else if (SchemaFactory.conformsM2Schema(spec)) {
      schema = SchemaFactory.createM2Schema();
      tuples = PmmUtilities.getTuples(inData, schema);
      modelType = ModelType.MANUAL_SECONDARY_MODEL;
    } else {
      throw new Exception();
    }

    // Retrieve info from dialog
    Metadata metadata = SBMLFactory.createMetadata();
    metadata.setGivenName("PMF To FSK Converter");
    metadata.setFamilyName("PMF To FSK Converter");
    metadata.setContact("PMF To FSK Converter");


    metadata.setCreatedDate(new Date().toString());
    // metadata.setModifiedDate(new Date(settings.modifiedDate).toString());

    metadata.setType(modelType);
    metadata.setRights("");
    metadata.setReferenceLink("");

    String modelNotes = "Model was written for archive purpose";


    WriterUtils.write(tuples, true, dir, mdName, metadata, false, modelNotes, exec, modelType);
  }
}
