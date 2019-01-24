/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlreader;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.reader.DataTuple;
import de.bund.bfr.knime.pmm.common.reader.Model1Tuple;
import de.bund.bfr.knime.pmm.common.reader.Model2Tuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmfml.ModelType;

public class SBMLReaderNodeModel extends NodeModel {

  // configuration keys
  public static final String CFGKEY_FILE = "filename";

  // persistent state
  private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, "");

  private ModelType modelType;
  private DataTableSpec tableSpec;

  /**
   * Constructor for the node model
   */
  protected SBMLReaderNodeModel() {
    // 0 input ports and 1 input port
    super(0, 1);
  }

  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws Exception {

    // Gets model type from the document annotation
    SBMLDocument doc = new SBMLReader().readSBML(filename.getStringValue());

    KnimeTuple tuple;

    if (modelType == ModelType.PRIMARY_MODEL_WDATA || modelType == ModelType.PRIMARY_MODEL_WODATA) {
      tuple = readPrimaryModel(doc);
    } else if (modelType == ModelType.TWO_STEP_SECONDARY_MODEL) {
      tuple = readTwoStepSecondaryModel(doc);
    } else if (modelType == ModelType.ONE_STEP_SECONDARY_MODEL) {
      tuple = readOneStepSecondaryModel(doc);
    } else if (modelType == ModelType.MANUAL_SECONDARY_MODEL) {
      tuple = readManualSecondaryModel(doc);
    } else {
      throw new InvalidSettingsException("Invalid model type.\n"
          + "This error should have been detected in #validateSettings");
    }

    BufferedDataContainer container = exec.createDataContainer(tableSpec);
    container.addRowToTable(tuple);
    container.close();
    exec.setProgress(1.0);

    return new BufferedDataTable[] {container.getTable()};
  }

  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {

    /*
     * The modelType is assigned in the validate method. Possible values are: PRIMARY_MODEL_WDATA,
     * PRIMARY_MODEL_WODATA, TWO_STEP_SECONDARY_MODEL, ONE_STEP_SECONDARY_MODEL and
     * MANUAL_SECONDARY_MODEL
     */
    if (modelType == ModelType.PRIMARY_MODEL_WDATA || modelType == ModelType.PRIMARY_MODEL_WODATA) {
      tableSpec = SchemaFactory.createM1DataSchema().createSpec();
    } else if (modelType == ModelType.TWO_STEP_SECONDARY_MODEL
        || modelType == ModelType.MANUAL_SECONDARY_MODEL) {
      tableSpec = SchemaFactory.createM2Schema().createSpec();
    } else if (modelType == ModelType.ONE_STEP_SECONDARY_MODEL) {
      tableSpec = SchemaFactory.createM12DataSchema().createSpec();
    } else {
      throw new InvalidSettingsException("Invalid model type.\n"
          + "This error should have been detected in #validateSettings");
    }

    return new DataTableSpec[] {tableSpec};
  }

  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    filename.saveSettingsTo(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    filename.loadSettingsFrom(settings);
  }

  @Override
  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

    final String filenameVal = settings.getString(CFGKEY_FILE);

    if (filenameVal.isEmpty()) {
      throw new InvalidSettingsException("Node is not configured. Missing file path");
    }

    if (filenameVal.endsWith(".numl")) {
      throw new InvalidSettingsException("SBML Reader does not support NuML. NuML Reader does.");
    }

    // Gets model type from the document annotation
    SBMLDocument doc;
    try {
      doc = new SBMLReader().readSBML(filenameVal);
    } catch (XMLStreamException | IOException e) {
      throw new InvalidSettingsException(filenameVal + " cannot be opened");
    }

    XMLNode docMetadata = doc.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
    XMLNode typeNode = docMetadata.getChildElement("type", "");
    String modelTypeString = typeNode.getChild(0).getCharacters();
    modelType = ModelType.valueOf(modelTypeString);

    if (modelType == ModelType.TWO_STEP_TERTIARY_MODEL
        || modelType == ModelType.ONE_STEP_TERTIARY_MODEL
        || modelType == ModelType.MANUAL_TERTIARY_MODEL) {
      throw new InvalidSettingsException("Tertiary models not supported currently");
    }
  }

  @Override
  protected void loadInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  @Override
  protected void reset() {}

  protected void saveInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  // --- utility methods ---
  private static KnimeTuple readPrimaryModel(final SBMLDocument doc) {
    return mergeTuples(new DataTuple(doc).knimeTuple, new Model1Tuple(doc).knimeTuple);
  }

  private static KnimeTuple readTwoStepSecondaryModel(final SBMLDocument doc) {
    return new Model2Tuple(doc.getModel()).knimeTuple;
  }

  private static KnimeTuple readOneStepSecondaryModel(final SBMLDocument doc) {
    // Parses data columns
    final KnimeTuple dataTuple = new DataTuple(doc).knimeTuple;

    // Parses primary model
    final KnimeTuple m1Tuple = new Model1Tuple(doc).knimeTuple;

    // Parses secondary model
    CompSBMLDocumentPlugin secCompPlugin =
        (CompSBMLDocumentPlugin) doc.getPlugin(CompConstants.shortLabel);
    ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
    final KnimeTuple m2Tuple = new Model2Tuple(secModel).knimeTuple;

    final KnimeTuple row = mergeTuples(dataTuple, m1Tuple, m2Tuple);
    return row;
  }

  private static KnimeTuple readManualSecondaryModel(final SBMLDocument doc) {
    return new Model2Tuple(doc.getModel()).knimeTuple;
  }
  
  // --- other utility methods ---
  
  private static KnimeTuple mergeTuples(final KnimeTuple dataTuple, final KnimeTuple m1Tuple) {
    final KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM1DataSchema());

    // Copies data columns
    tuple.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
    tuple.setValue(TimeSeriesSchema.ATT_COMBASEID,
        dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
    tuple.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
    tuple.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
    tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES,
        dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
    tuple.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
    tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
    tuple.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
    tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
    tuple.setValue(TimeSeriesSchema.ATT_METADATA,
        dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

    // Copies model1 columns
    tuple.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
    tuple.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
    tuple.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
    tuple.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
    tuple.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
    tuple.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
    tuple.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
    tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
        m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
    tuple.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
    tuple.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

    return tuple;
  }

  private static KnimeTuple mergeTuples(final KnimeTuple dataTuple, final KnimeTuple m1Tuple,
      final KnimeTuple m2Tuple) {

    final KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM12DataSchema());

    tuple.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
    tuple.setValue(TimeSeriesSchema.ATT_COMBASEID,
        dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
    tuple.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
    tuple.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
    tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES,
        dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
    tuple.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
    tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
    tuple.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
    tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
    tuple.setValue(TimeSeriesSchema.ATT_METADATA,
        dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

    // Copies model1 columns
    tuple.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
    tuple.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
    tuple.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
    tuple.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
    tuple.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
    tuple.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
    tuple.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
    tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
        m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
    tuple.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
    tuple.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

    // Copies model2 columns
    tuple.setValue(Model2Schema.ATT_MODELCATALOG, m2Tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG));
    tuple.setValue(Model2Schema.ATT_DEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_DEPENDENT));
    tuple.setValue(Model2Schema.ATT_INDEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
    tuple.setValue(Model2Schema.ATT_PARAMETER, m2Tuple.getPmmXml(Model2Schema.ATT_PARAMETER));
    tuple.setValue(Model2Schema.ATT_ESTMODEL, m2Tuple.getPmmXml(Model2Schema.ATT_ESTMODEL));
    tuple.setValue(Model2Schema.ATT_MLIT, m2Tuple.getPmmXml(Model2Schema.ATT_MLIT));
    tuple.setValue(Model2Schema.ATT_EMLIT, m2Tuple.getPmmXml(Model2Schema.ATT_EMLIT));
    tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
        m2Tuple.getInt(Model2Schema.ATT_DATABASEWRITABLE));
    tuple.setValue(Model2Schema.ATT_DBUUID, m2Tuple.getString(Model2Schema.ATT_DBUUID));
    tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID,
        m2Tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));
    tuple.setValue(Model2Schema.ATT_METADATA, m2Tuple.getPmmXml(Model2Schema.ATT_METADATA));

    return tuple;
  }
}
