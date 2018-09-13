package de.bund.bfr.knime.pmm.common.reader;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.file.ExperimentalDataFile;
import de.bund.bfr.pmfml.file.ManualSecondaryModelFile;
import de.bund.bfr.pmfml.file.ManualTertiaryModelFile;
import de.bund.bfr.pmfml.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmfml.file.PrimaryModelWDataFile;
import de.bund.bfr.pmfml.file.PrimaryModelWODataFile;
import de.bund.bfr.pmfml.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmfml.model.ExperimentalData;
import de.bund.bfr.pmfml.model.ManualSecondaryModel;
import de.bund.bfr.pmfml.model.ManualTertiaryModel;
import de.bund.bfr.pmfml.model.OneStepSecondaryModel;
import de.bund.bfr.pmfml.model.OneStepTertiaryModel;
import de.bund.bfr.pmfml.model.PrimaryModelWData;
import de.bund.bfr.pmfml.model.PrimaryModelWOData;
import de.bund.bfr.pmfml.model.TwoStepSecondaryModel;
import de.bund.bfr.pmfml.model.TwoStepTertiaryModel;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.sbml.Limits;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.Uncertainties;

public class ReaderUtils {

  private ReaderUtils() {}

  /**
   * Parses a list of constraints and returns a dictionary that maps variables and their limit
   * values.
   * 
   * @param constraints
   */
  public static Map<String, Limits> parseConstraints(final ListOf<Constraint> constraints) {
    Map<String, Limits> paramLimits = new HashMap<>();

    for (Constraint currConstraint : constraints) {
      LimitsConstraint lc = new LimitsConstraint(currConstraint);
      Limits lcLimits = lc.getLimits();
      paramLimits.put(lcLimits.getVar(), lcLimits);
    }

    return paramLimits;
  }

  /**
   * Parses misc items.
   * 
   * @param miscs . Dictionary that maps miscs names and their values.
   * @return
   */
  public static PmmXmlDoc parseMiscs(Map<String, Double> miscs) {
    PmmXmlDoc cell = new PmmXmlDoc();

    if (miscs != null) {
      // First misc item has id -1 and the rest of items have negative
      // ints
      for (Entry<String, Double> entry : miscs.entrySet()) {
        String name = entry.getKey();
        Double value = entry.getValue();

        List<String> categories;
        String description, unit;

        switch (name) {
          case "Temperature":
            categories = Arrays.asList(Categories.getTempCategory().getName());
            description = name;
            unit = Categories.getTempCategory().getStandardUnit();

            cell.add(new MiscXml(-1, name, description, value, categories, unit));
            break;

          case "pH":
            categories = Arrays.asList(Categories.getPhCategory().getName());
            description = name;
            unit = Categories.getPhUnit();

            cell.add(new MiscXml(-2, name, description, value, categories, unit));
            break;

          case "aw":
            categories = Arrays.asList(Categories.getAwCategory().getName());
            description = name;
            unit = Categories.getAwCategory().getStandardUnit();
            cell.add(new MiscXml(-3, name, description, value, categories, unit));
            break;
        }
      }
    }
    return cell;
  }

  /**
   * Creates time series
   */
  public static PmmXmlDoc createTimeSeries(String timeUnit, String concUnit,
      String concUnitObjectType, double[][] data) {

    PmmXmlDoc mdData = new PmmXmlDoc();

    Double concStdDev = null;
    Integer numberOfMeasurements = null;

    for (double[] point : data) {
      double conc = point[0];
      double time = point[1];
      String name = "t" + mdData.size();

      TimeSeriesXml t =
          new TimeSeriesXml(name, time, timeUnit, conc, concUnit, concStdDev, numberOfMeasurements);
      t.setConcentrationUnitObjectType(concUnitObjectType);
      mdData.add(t);
    }

    return mdData;
  }

  public static EstModelXml uncertainties2EstModel(Uncertainties uncertainties) {
    int estModelId = uncertainties.getID();
    String modelName = uncertainties.getModelName();
    Double sse = uncertainties.getSSE();
    Double rms = uncertainties.getRMS();
    Double r2 = uncertainties.getR2();
    Double aic = uncertainties.getAIC();
    Double bic = uncertainties.getBIC();
    Integer dof = uncertainties.getDOF();
    EstModelXml estModel = new EstModelXml(estModelId, modelName, sse, rms, r2, aic, bic, dof);
    return estModel;
  }

  public static CatalogModelXml model1Rule2CatModel(ModelRule rule) {
    int formulaId = rule.getPmmlabID();
    String formulaName = rule.getFormulaName();
    ModelClass modelClass = rule.getModelClass();

    String formulaString = rule.getFormula();
    formulaString = formulaString.replace("time", "Time");
    formulaString = formulaString.replace("log(", "ln(");

    String formula = String.format("Value=%s", formulaString);

    CatalogModelXml catModel =
        new CatalogModelXml(formulaId, formulaName, formula, modelClass.ordinal());
    return catModel;
  }

  public static CatalogModelXml model2Rule2CatModel(ModelRule rule) {
    int formulaId = rule.getPmmlabID();
    String formulaName = rule.getFormulaName();
    ModelClass modelClass = rule.getModelClass();

    String formulaString = rule.getFormula();
    formulaString = formulaString.replace("time", "Time");
    formulaString = formulaString.replace("log(", "ln(");

    String formula = String.format("%s=%s", rule.getVariable(), formulaString);

    CatalogModelXml catModel =
        new CatalogModelXml(formulaId, formulaName, formula, modelClass.ordinal());
    return catModel;
  }

  public static KnimeTuple mergeTuples(KnimeTuple dataTuple, KnimeTuple m1Tuple, KnimeTuple m2Tuple) {

    KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM12DataSchema());

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

  private static final Map<ModelType, Reader> READERS = new HashMap<>();
  static {
    READERS.put(ModelType.EXPERIMENTAL_DATA, new ExperimentalDataReader());
    READERS.put(ModelType.PRIMARY_MODEL_WDATA, new PrimaryModelWDataReader());
    READERS.put(ModelType.PRIMARY_MODEL_WODATA, new PrimaryModelWODataReader());
    READERS.put(ModelType.TWO_STEP_SECONDARY_MODEL, new TwoStepSecondaryModelReader());
    READERS.put(ModelType.ONE_STEP_SECONDARY_MODEL, new OneStepSecondaryModelReader());
    READERS.put(ModelType.MANUAL_SECONDARY_MODEL, new ManualSecondaryModelReader());
    READERS.put(ModelType.TWO_STEP_TERTIARY_MODEL, new TwoStepTertiaryModelReader());
    READERS.put(ModelType.ONE_STEP_TERTIARY_MODEL, new OneStepTertiaryModelReader());
    READERS.put(ModelType.MANUAL_TERTIARY_MODEL, new ManualTertiaryModelReader());
  }
  
  public static BufferedDataContainer[] readPMF(File file, boolean isPMFX,
      ExecutionContext exec, ModelType modelType) throws Exception {

    Reader reader = READERS.get(modelType);
    return reader.read(file, isPMFX, exec);
  }

  /**
   * Reader interface
   * 
   * @author Miguel Alba
   */
  private interface Reader {
    /**
     * Read models from a CombineArchive and returns a Knime table with them
     * 
     * @param isPMFX. If true the reads PMFX file. Else then read PMF file.
     * @throws Exception
     */
    BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception;
  }

  private static class ExperimentalDataReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec dataSpec = SchemaFactory.createDataSchema().createSpec();
      BufferedDataContainer dataContainer = exec.createDataContainer(dataSpec);

      // Reads in experimental data from file
      List<ExperimentalData> eds = ExperimentalDataFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (ExperimentalData ed : eds) {
        KnimeTuple tuple = new DataTuple(ed.getDoc()).knimeTuple;
        dataContainer.addRowToTable(tuple);
        exec.setProgress((float) dataContainer.size() / eds.size());
      }

      dataContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          eds.stream().map(ExperimentalData::getDoc).map(FSMRUtils::processData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {dataContainer, fsmrContainer};
    }
  }

  private static class PrimaryModelWDataReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM1DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Reads in models from file
      List<PrimaryModelWData> models = PrimaryModelWDataFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (PrimaryModelWData model : models) {
        modelContainer.addRowToTable(parse(model));
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          models.stream().map(PrimaryModelWData::getModelDoc)
              .map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private static KnimeTuple parse(PrimaryModelWData pm) {
      // Add cells to the row
      KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

      // time series cells
      KnimeTuple dataTuple = new DataTuple(pm.getDataDoc()).knimeTuple;
      row.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
      row.setValue(TimeSeriesSchema.ATT_COMBASEID,
          dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
      row.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
      row.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
      row.setValue(TimeSeriesSchema.ATT_TIMESERIES,
          dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
      row.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
      row.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
      row.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
      row.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
      row.setValue(TimeSeriesSchema.ATT_METADATA,
          dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

      // primary model cells
      KnimeTuple m1Tuple = new Model1Tuple(pm.getModelDoc()).knimeTuple;
      row.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
      row.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
      row.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
      row.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
      row.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
      row.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
      row.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
      row.setValue(Model1Schema.ATT_DATABASEWRITABLE,
          m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
      row.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
      row.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));
      return row;
    }
  }

  private static class PrimaryModelWODataReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM1DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Reads in models from file
      List<PrimaryModelWOData> models = PrimaryModelWODataFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (PrimaryModelWOData model : models) {
        modelContainer.addRowToTable(parse(model));
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Creates tuples and adds them to the container
      List<KnimeTuple> fsmrTuples =
          models.stream().map(PrimaryModelWOData::getDoc)
              .map(FSMRUtils::processModelWithMicrobialData)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with 'fsmrTuple'
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private static KnimeTuple parse(PrimaryModelWOData pm) {
      // Add cells to the row
      KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

      // time series cells
      KnimeTuple dataTuple = new DataTuple(pm.getDoc()).knimeTuple;
      row.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
      row.setValue(TimeSeriesSchema.ATT_COMBASEID,
          dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
      row.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
      row.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
      row.setValue(TimeSeriesSchema.ATT_TIMESERIES,
          dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
      row.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
      row.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
      row.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
      row.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
      row.setValue(TimeSeriesSchema.ATT_METADATA,
          dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

      // primary model cells
      KnimeTuple m1Tuple = new Model1Tuple(pm.getDoc()).knimeTuple;
      row.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
      row.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
      row.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
      row.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
      row.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
      row.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
      row.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
      row.setValue(Model1Schema.ATT_DATABASEWRITABLE,
          m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
      row.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
      row.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

      return row;
    }
  }

  private static class TwoStepSecondaryModelReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Reads in models from file
      List<TwoStepSecondaryModel> models = TwoStepSecondaryModelFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (TwoStepSecondaryModel tssm : models) {
        parse(tssm).forEach(modelContainer::addRowToTable);
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          models.stream().map(FSMRUtils::processTwoStepSecondaryModel)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR templates
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private static List<KnimeTuple> parse(TwoStepSecondaryModel tssm) {
      // create n rows for n secondary models
      List<KnimeTuple> rows = new LinkedList<>();

      KnimeTuple m2Tuple = new Model2Tuple(tssm.getSecDoc().getModel()).knimeTuple;

      for (PrimaryModelWData pmwd : tssm.getPrimModels()) {
        KnimeTuple dataTuple;
        if (pmwd.getDataDoc() != null) {
          dataTuple = new DataTuple(pmwd.getDataDoc()).knimeTuple;
        } else {
          dataTuple = new DataTuple(pmwd.getModelDoc()).knimeTuple;
        }
        KnimeTuple m1Tuple = new Model1Tuple(pmwd.getModelDoc()).knimeTuple;

        KnimeTuple row = ReaderUtils.mergeTuples(dataTuple, m1Tuple, m2Tuple);
        rows.add(row);
      }

      return rows;
    }
  }

  private static class OneStepSecondaryModelReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Reads in models from file
      List<OneStepSecondaryModel> models = OneStepSecondaryModelFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (OneStepSecondaryModel ossm : models) {
        parse(ossm).forEach(modelContainer::addRowToTable);
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          models.stream().map(FSMRUtils::processOneStepSecondaryModel)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with the FSMR templates
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private static List<KnimeTuple> parse(OneStepSecondaryModel ossm) {
      List<KnimeTuple> rows = new LinkedList<>();

      // Parses primary model
      KnimeTuple primTuple = new Model1Tuple(ossm.getModelDoc()).knimeTuple;

      // Parses secondary model
      CompSBMLDocumentPlugin secCompPlugin =
          (CompSBMLDocumentPlugin) ossm.getModelDoc().getPlugin(CompConstants.shortLabel);
      ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
      KnimeTuple secTuple = new Model2Tuple(secModel).knimeTuple;

      // Parses data files
      for (NuMLDocument numlDoc : ossm.getDataDocs()) {
        KnimeTuple dataTuple = new DataTuple(numlDoc).knimeTuple;
        rows.add(ReaderUtils.mergeTuples(dataTuple, primTuple, secTuple));
      }

      return rows;
    }
  }

  private static class ManualSecondaryModelReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM2Schema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Reads in models from file
      List<ManualSecondaryModel> models = ManualSecondaryModelFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (ManualSecondaryModel model : models) {
        KnimeTuple tuple = new Model2Tuple(model.getDoc().getModel()).knimeTuple;
        modelContainer.addRowToTable(tuple);
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Creates tuples and adds them to the container
      List<KnimeTuple> fsmrTuples =
          models.stream().map(FSMRUtils::processManualSecondaryModel)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates cotnainer with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }
  }

  private static class TwoStepTertiaryModelReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Read in models from file
      List<TwoStepTertiaryModel> models = TwoStepTertiaryModelFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (TwoStepTertiaryModel tssm : models) {
        parse(tssm).forEach(modelContainer::addRowToTable);
        exec.setProgress((float) modelContainer.size() / models.size());
      }
      modelContainer.close();

      // Creates tuples and adds them to the container
      List<KnimeTuple> fsmrTuples =
          models.stream().map(FSMRUtils::processTwoStepTertiaryModel)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private static List<KnimeTuple> parse(TwoStepTertiaryModel tstm) {

      List<KnimeTuple> secTuples = new LinkedList<>();
      for (SBMLDocument secDoc : tstm.getSecDocs()) {
        secTuples.add(new Model2Tuple(secDoc.getModel()).knimeTuple);
      }

      List<KnimeTuple> tuples = new LinkedList<>();
      for (PrimaryModelWData pm : tstm.getPrimModels()) {
        KnimeTuple dataTuple = new DataTuple(pm.getDataDoc()).knimeTuple;
        KnimeTuple m1Tuple = new Model1Tuple(pm.getModelDoc()).knimeTuple;
        for (KnimeTuple m2Tuple : secTuples) {
          tuples.add(ReaderUtils.mergeTuples(dataTuple, m1Tuple, m2Tuple));
        }
      }

      return tuples;
    }
  }

  private static class OneStepTertiaryModelReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Read in models from file
      List<OneStepTertiaryModel> models = OneStepTertiaryModelFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (OneStepTertiaryModel ostm : models) {
        parse(ostm).forEach(modelContainer::addRowToTable);
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          models.stream().map(FSMRUtils::processOneStepTertiaryModel)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private static List<KnimeTuple> parse(OneStepTertiaryModel ostm) {

      KnimeTuple primTuple = new Model1Tuple(ostm.getTertiaryDoc()).knimeTuple;
      List<KnimeTuple> secTuples = new LinkedList<>();
      for (SBMLDocument secDoc : ostm.getSecDocs()) {
        secTuples.add(new Model2Tuple(secDoc.getModel()).knimeTuple);
      }

      List<KnimeTuple> tuples = new LinkedList<>();

      int instanceCounter = 1;

      for (NuMLDocument numlDoc : ostm.getDataDocs()) {
        KnimeTuple dataTuple = new DataTuple(numlDoc).knimeTuple;
        for (KnimeTuple secTuple : secTuples) {
          KnimeTuple tuple = ReaderUtils.mergeTuples(dataTuple, primTuple, secTuple);
          tuple.setValue(TimeSeriesSchema.ATT_CONDID, instanceCounter);
          tuples.add(tuple);
        }
        instanceCounter++;
      }

      return tuples;
    }
  }

  private static class ManualTertiaryModelReader implements Reader {

    public BufferedDataContainer[] read(File file, boolean isPMFX, ExecutionContext exec)
        throws Exception {
      // Creates table spec and container
      DataTableSpec modelSpec = SchemaFactory.createM12DataSchema().createSpec();
      BufferedDataContainer modelContainer = exec.createDataContainer(modelSpec);

      // Read in models from file
      List<ManualTertiaryModel> models = ManualTertiaryModelFile.read(file.toPath());

      // Creates tuples and adds them to the container
      for (ManualTertiaryModel mtm : models) {
        parse(mtm).forEach(modelContainer::addRowToTable);
        exec.setProgress((float) modelContainer.size() / models.size());
      }

      modelContainer.close();

      // Gets KNIME tuples with the FSMR templates
      List<KnimeTuple> fsmrTuples =
          models.stream().map(FSMRUtils::processManualTertiaryModel)
              .map(FSMRUtils::createTupleFromTemplate).collect(Collectors.toList());

      // Creates container with OpenFSMR tuples
      DataTableSpec fsmrSpec = new OpenFSMRSchema().createSpec();
      BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
      fsmrTuples.forEach(fsmrContainer::addRowToTable);
      fsmrContainer.close();

      return new BufferedDataContainer[] {modelContainer, fsmrContainer};
    }

    private static List<KnimeTuple> parse(ManualTertiaryModel mtm) {

      KnimeTuple dataTuple = new DataTuple(mtm.getTertiaryDoc()).knimeTuple;
      KnimeTuple m1Tuple = new Model1Tuple(mtm.getTertiaryDoc()).knimeTuple;

      List<KnimeTuple> rows = new LinkedList<>();
      for (SBMLDocument secDoc : mtm.getSecDocs()) {
        KnimeTuple m2Tuple = new Model2Tuple(secDoc.getModel()).knimeTuple;
        rows.add(ReaderUtils.mergeTuples(dataTuple, m1Tuple, m2Tuple));
      }

      return rows;
    }
  }

}
