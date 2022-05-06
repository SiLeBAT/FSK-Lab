package de.bund.bfr.knime.fsklab.v2_0.pmmConverter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;
import org.threeten.bp.LocalDate;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.ExistingEnvironmentManager;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.metadata.swagger.ModelEquation;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.QualityMeasures;

public class PMModelReader {
  protected Map<String, String> modelNames;
  protected Map<String, List<String>> parameters;
  protected Map<String, Map<String, Double>> minValues;
  protected Map<String, Map<String, Double>> maxValues;
  protected static final String PRIMARY = "Primary";
  protected static final String SECONDARY = "Secondary";
  Map<String, Double> functionParameters;
  HashMap<String, ParametricModel> m1s;
  HashMap<String, ParametricModel> m2s;
  HashMap<ParametricModel, HashMap<String, ParametricModel>> m_secondaryModels;
  Optional<Path> workingDirectory;
  private boolean dataAvailable;
  private File dataFile;
  private HashMap<String, HashMap<String,String>> modelParams = new HashMap<String, HashMap<String,String>>();

  protected void readDataTableIntoParametricModel(BufferedDataTable dataTable, boolean loadData, ExecutionContext exec) {

    DataTableSpec inSpec = dataTable.getDataTableSpec();
    modelNames = new LinkedHashMap<>();
    try {
      KnimeSchema tsSchema = new TimeSeriesSchema();
      KnimeSchema inSchema1 = new Model1Schema();
      KnimeSchema inSchema2 = new Model2Schema();
      boolean hasTs = tsSchema.conforms(inSpec);
      boolean hasM1 = inSchema1.conforms(inSpec);
      boolean hasM2 = inSchema2.conforms(inSpec);

      KnimeSchema finalSchema = null;
      if (hasM1 && hasM2)
        finalSchema = KnimeSchema.merge(inSchema1, inSchema2);
      else if (hasM1)
        finalSchema = inSchema1;
      else if (hasM2)
        finalSchema = inSchema2;
      if (hasTs)
        finalSchema = (finalSchema == null) ? tsSchema : KnimeSchema.merge(tsSchema, finalSchema);
      List<KnimeTuple> tuples = new ArrayList<>();
      if (finalSchema != null) {
        KnimeRelationReader reader = new KnimeRelationReader(finalSchema, dataTable);
        HashMap<Integer, PmmTimeSeries> tss = new HashMap<>();
        m1s = new HashMap<>();
        m2s = new HashMap<>();
        m_secondaryModels = new HashMap<>();
        Integer condID = null;
        Integer m1EstID = null, m2EstID;

        while (reader.hasMoreElements()) {
          KnimeTuple row = reader.nextElement();
          tuples.add(row);
          if (hasTs) {
            PmmTimeSeries ts = new PmmTimeSeries(row);
            condID = ts.getCondId();
            // System.err.println(condID);
            tss.put(condID, ts);
          }
          if (hasM1) {
            ParametricModel pm1 = new ParametricModel(row, 1, hasTs ? condID : null);
            m1EstID = pm1.estModelId;
            modelNames.put(PRIMARY +"-"+ pm1.modelName+ pm1.modelId + (m1EstID!=null?m1EstID.toString():""), pm1.modelName);

            
            if (!m1s.containsKey(PRIMARY +"-"+ pm1.modelName+ pm1.modelId+ (m1EstID!=null?m1EstID.toString():""))) {
              // m1s.put(m1EstID, pm1);
              m1s.put(PRIMARY +"-"+ pm1.modelName+ pm1.modelId+ (m1EstID!=null?m1EstID.toString():""), pm1);
            }


            if (hasM2) {
              ParametricModel pm2 = new ParametricModel(row, 2, null);
              m2EstID = pm2.estModelId;
              modelNames.put(SECONDARY +"-"+ pm2.modelName+ pm2.modelId+ (m2EstID!=null?m2EstID.toString():""), pm2.modelName);
              
              if (!m2s.containsKey(SECONDARY +"-"+ pm2.modelName+ pm2.modelId+ (m2EstID!=null?m2EstID.toString():""))) {
                m2s.put(SECONDARY +"-"+ pm2.modelName+ pm2.modelId+ (m2EstID!=null?m2EstID.toString():""), pm2);
              }
              if (!m_secondaryModels.containsKey(m1s.get(PRIMARY +"-"+ pm1.modelName+ pm1.modelId+ (m1EstID!=null?m1EstID.toString():""))))
                m_secondaryModels.put(m1s.get(PRIMARY +"-"+ pm1.modelName+ pm1.modelId+ (m1EstID!=null?m1EstID.toString():"")),
                    new HashMap<String, ParametricModel>());
              HashMap<String, ParametricModel> hm =
                  m_secondaryModels.get(m1s.get(PRIMARY +"-"+ pm1.modelName+ pm1.modelId+ (m1EstID!=null?m1EstID.toString():"")));
              hm.put(pm2.getDepVar(), m2s.get(SECONDARY +"-"+ pm2.modelName+ pm2.modelId+ (m2EstID!=null?m2EstID.toString():"")));
            }
          } else if (hasM2) {
            ParametricModel pm2 = new ParametricModel(row, 2, null);
            m2EstID = pm2.estModelId;
            modelNames.put(SECONDARY +"-"+ pm2.modelName+ pm2.modelId, pm2.modelName+ (m2EstID!=null?m2EstID.toString():""));
            
            if (!m2s.containsKey(SECONDARY +"-"+ pm2.modelName+ pm2.modelId+ (m2EstID!=null?m2EstID.toString():""))) {
              m2s.put(SECONDARY +"-"+ pm2.modelName+ pm2.modelId+ (m2EstID!=null?m2EstID.toString():""), pm2);
            }
            m_secondaryModels.put(null, new HashMap<String, ParametricModel>());
            HashMap<String, ParametricModel> hm = m_secondaryModels.get(null);
            hm.put(pm2.getDepVar(), m2s.get(m2EstID));
          }
        }
      }
      workingDirectory = Optional.of(Files.createTempDirectory("workingDirectory"));

      if (inSpec.containsName("MD_Data") && loadData) {
        dataFile = MDUtil.writeJson(tuples, "ModelData", workingDirectory.get());
        dataAvailable = true;
      }
      try {
        if(exec!=null)
          PMFWriter.write(dataTable, workingDirectory.get().toFile().getAbsolutePath(), "PMFModel", true, exec);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } catch (PmmException e) {
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public FskPortObject convertParametricModelToFSKObject(ParametricModel parametricModel)
      throws IOException {
    PredictiveModel modelMetadata = (PredictiveModel) NodeUtils.initializePridictiveModell();
    modelMetadata.setGeneralInformation(extractGeneralInformation(parametricModel));
    modelMetadata.setModelMath(extractModelMath(parametricModel));
    FskSimulation newDefaultSimulation =
        NodeUtils.createDefaultSimulation(modelMetadata.getModelMath().getParameter());
    List<FskSimulation> simulations = Arrays.asList(newDefaultSimulation);
    java.util.Optional<EnvironmentManager> environmentManager;
    if (!dataAvailable) {
      environmentManager = Optional.empty();
    } else {
      environmentManager =
          Optional.of(new ExistingEnvironmentManager(workingDirectory.get().toString()));
    }
    FskPortObject portObj = null;
    StringBuilder indparm = new StringBuilder();
    if (parametricModel.getIndependent().getElementSet().size() > 0) {
      indparm.append(((IndepXml) parametricModel.getIndependent().getElementSet().get(0)).name);
    }
    try {
      String visScript = "plot(values,type = 'l', col = 'blue', xlab = '" + indparm + "', ylab = '"
          + parametricModel.getDepVar() + "(" + parametricModel.getDepUnit() + ")'," + "   main = '"
          + parametricModel.modelName + "')";
      portObj = new FskPortObject(generateModelScript(parametricModel), visScript, modelMetadata,
          null, Collections.emptyList(), environmentManager, "", "");
      portObj.simulations.addAll(simulations);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return portObj;
  }

  public FskPortObject convertTertiaryParametricModelToFSKObject(ParametricModel parametricModel)
      throws IOException {
    PredictiveModel modelMetadata = (PredictiveModel) NodeUtils.initializePridictiveModell();
    modelMetadata.setGeneralInformation(extractGeneralInformation(parametricModel));
    modelMetadata.setModelMath(extractTertiaryModelMath(parametricModel));
    FskSimulation newDefaultSimulation =
        NodeUtils.createDefaultSimulation(modelMetadata.getModelMath().getParameter());
    List<FskSimulation> simulations = Arrays.asList(newDefaultSimulation);
    java.util.Optional<EnvironmentManager> environmentManager;
    if (!dataAvailable) {
      environmentManager = Optional.empty();
    } else {
      environmentManager =
          Optional.of(new ExistingEnvironmentManager(workingDirectory.get().toString()));
    }
    FskPortObject portObj = null;
    StringBuilder indparm = new StringBuilder();

    indparm.append(((IndepXml) parametricModel.getIndependent().getElementSet().get(0)).name);
    try {
      String visScript = "plot(values,type = 'l', col = 'blue', xlab = '" + indparm.toString()
          + "', ylab = '" + parametricModel.getDepVar() + "(" + parametricModel.getDepUnit() + ")',"
          + "   main = '" + parametricModel.modelName + "')";
      portObj = new FskPortObject(generateTertiaryModelScript(parametricModel), visScript,
          modelMetadata, null, Collections.emptyList(), environmentManager, "", "");
      portObj.simulations.addAll(simulations);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return portObj;
  }

  public PredictiveModelGeneralInformation extractGeneralInformation(
      ParametricModel parametricModel) {
    PredictiveModelGeneralInformation generalInformation = new PredictiveModelGeneralInformation();
    generalInformation.setName(parametricModel.modelName);
    generalInformation.setIdentifier(parametricModel.modelDbUuid);
    generalInformation.setCreationDate(LocalDate.now());
    return generalInformation;
  }

  public PredictiveModelModelMath extractModelMath(ParametricModel parametricModel) {
    PredictiveModelModelMath modelMath = new PredictiveModelModelMath();
    functionParameters = new HashMap<String, Double>();

    buildModelMathParameters(parametricModel, modelMath,null);
    ModelEquation modelEquation = new ModelEquation();
    modelEquation.setModelEquation(parametricModel.getFormula());
    modelMath.addModelEquationItem(modelEquation);

    QualityMeasures qualityMeasures = new QualityMeasures();
    qualityMeasures.setBic(
        isDouble(parametricModel.getBic()) ? new BigDecimal(parametricModel.getBic()) : null);
    qualityMeasures.setAic(
        isDouble(parametricModel.getAic()) ? new BigDecimal(parametricModel.getAic()) : null);
    parametricModel.setRss(null);
    qualityMeasures.setRsquared(
        isDouble(parametricModel.getRsquared()) ? new BigDecimal(parametricModel.getRsquared())
            : null);
    qualityMeasures.setRmse(
        isDouble(parametricModel.getRms()) ? new BigDecimal(parametricModel.getRms()) : null);
    modelMath.addQualityMeasuresItem(qualityMeasures);

    return modelMath;
  }

  public PredictiveModelModelMath extractTertiaryModelMath(ParametricModel parametricModel) {
    PredictiveModelModelMath modelMath = new PredictiveModelModelMath();
    functionParameters = new HashMap<String, Double>();
    buildModelMathParameters(parametricModel, modelMath,null);

    HashMap<String, ParametricModel> secondaryModels = m_secondaryModels.get(parametricModel);
    AtomicInteger counter = new AtomicInteger(0);
    secondaryModels.forEach((DepVar, subParametricModel) -> {
      buildModelMathParameters(subParametricModel, modelMath, counter.addAndGet(1));
      removeDepVars(modelMath, DepVar);
    });
    ModelEquation modelEquation = new ModelEquation();
    modelEquation.setModelEquation(simplifyTertiaryFormula(parametricModel));
    modelMath.addModelEquationItem(modelEquation);

    QualityMeasures qualityMeasures = new QualityMeasures();
    qualityMeasures.setBic(
        isDouble(parametricModel.getBic()) ? new BigDecimal(parametricModel.getBic()) : null);
    qualityMeasures.setAic(
        isDouble(parametricModel.getAic()) ? new BigDecimal(parametricModel.getAic()) : null);
    parametricModel.setRss(null);
    qualityMeasures.setRsquared(
        isDouble(parametricModel.getRsquared()) ? new BigDecimal(parametricModel.getRsquared())
            : null);
    qualityMeasures.setRmse(
        isDouble(parametricModel.getRms()) ? new BigDecimal(parametricModel.getRms()) : null);
    modelMath.addQualityMeasuresItem(qualityMeasures);

    return modelMath;
  }

  public String simplifyTertiaryFormula(ParametricModel parametricModel) {
    String finalFormula = parametricModel.getFormula();
    HashMap<String, ParametricModel> secondaryModels = m_secondaryModels.get(parametricModel);
    int index = 1;
    for (Map.Entry<String, ParametricModel> entry : secondaryModels.entrySet()) {
      String DepVar = entry.getKey();
      ParametricModel subParametricModel = entry.getValue();
      String subFunction = fixDuplicatedParamNames(subParametricModel.modelName, subParametricModel.getFormula(), index++);
      
      subFunction = MathUtilities.getAllButBoundaryCondition(
          subFunction.replace( subParametricModel.getDepVar() + "=", ""));
      finalFormula = finalFormula.replaceAll(DepVar, "(" + subFunction + ")");

    }
    return finalFormula;
  }
  public String fixDuplicatedParamNames(String modelName,String subFunction, int index) {
    HashMap<String,String> subModelParams = modelParams.get(modelName+index);
    for (Map.Entry<String, String> entry : subModelParams.entrySet()) {
      subFunction = subFunction.replaceAll("\\b"+entry.getKey()+"\\b", entry.getValue());
    }
    return subFunction;
  }
  public void buildModelMathParameters(ParametricModel parametricModel,
      PredictiveModelModelMath modelMath, Integer index) {

    HashMap<String,String> subModelParams = new HashMap<String,String>();
    modelParams.put(parametricModel.modelName+index, subModelParams);
    for (PmmXmlElementConvertable el : parametricModel.getParameter().getElementSet()) {
      if (el instanceof ParamXml) {
        ParamXml px = (ParamXml) el;
        Parameter parameter = new Parameter();
        subModelParams.put(px.name, px.name+(index!=null?index.toString():""));
        parameter.setName(px.name+(index!=null?index.toString():""));
        parameter.setId(px.name+(index!=null?index.toString():""));
        parameter.setClassification(ClassificationEnum.INPUT);
        parameter.setDescription(px.description);
        parameter.setUnit(px.unit);
        parameter.setValue("" + px.value);
        parameter.setError("" + px.error);
        parameter.setMaxValue("" + px.max);
        parameter.setMinValue("" + px.min);
        modelMath.getParameter().add(parameter);
        functionParameters.put(px.name, px.value);
      }
    }
  }

  public boolean isDouble(Double num) {
    return num != null && !Double.isNaN(num) && !Double.isInfinite(num);
  }

  public void removeDepVars(PredictiveModelModelMath modelMath, String depVar) {
    if (functionParameters.containsKey(depVar)) {
      functionParameters.remove(depVar);
      Iterator<Parameter> iter = modelMath.getParameter().iterator();
      while (iter.hasNext()) {
        if (iter.next().getName().equals(depVar))
          iter.remove();
      }
    }
  }

  public String generateTertiaryModelScript(ParametricModel parametricModel) {
    HashMap<String, ParametricModel> secondaryModels = m_secondaryModels.get(parametricModel);
    Set<PmmXmlElementConvertable> subIndependentList = new HashSet();
    secondaryModels.forEach((DepVar, subParametricModel) -> {
      subIndependentList.addAll(subParametricModel.getIndependent().getElementSet());
    });
    StringBuilder code = new StringBuilder();
    code.append("library('SciViews')\n");
    code.append(loadDataScript());
    String function = simplifyTertiaryFormula(parametricModel);
    DJep parser = MathUtilities.createParser();

    for (String param : functionParameters.keySet()) {
      parser.addConstant(param, functionParameters.get(param));
    }
    List<PmmXmlElementConvertable> independentList =
        parametricModel.getIndependent().getElementSet();
    independentList.addAll(subIndependentList);


    String BoundaryCondition = MathUtilities.getBoundaryCondition(function);

    function = MathUtilities
        .getAllButBoundaryCondition(function.replace(parametricModel.getDepVar() + "=", ""));

    IndepXml variable = null;
    for (int indIndex = 0; indIndex < independentList.size(); indIndex++) {
      if (indIndex == 0)
        variable = (IndepXml) independentList.get(0);
      else {
        IndepXml otherIndependentVariable = (IndepXml) independentList.get(indIndex);
        code.append(otherIndependentVariable.name + " <- " + otherIndependentVariable.min + "\n");
      }
    }
    code.append("values <- c()\n");
    if (!StringUtils.isAllEmpty(BoundaryCondition))
      code.append("if(" + BoundaryCondition.replaceAll("\\*", " && ") + "){\n");
    
    // independentList.forEach(item -> {
    if (variable != null)
      code.append("for(" + variable.name + " in " + variable.min + ":" + variable.max + "){\n");
    // });
    code.append("values <- c( values," + function + ")\n");

    if (variable != null)
      code.append("}");


    if (!StringUtils.isAllEmpty(BoundaryCondition))
      code.append("}\n");
    return code.toString();
  }

  public String generateModelScript(ParametricModel parametricModel) {
    StringBuilder code = new StringBuilder();
    code.append("library('SciViews')\n");
    code.append(loadDataScript());
    String function = parametricModel.getFormula();
    DJep parser = MathUtilities.createParser();
    Node node = null;

    for (String param : functionParameters.keySet()) {
      parser.addConstant(param, functionParameters.get(param));
    }
    List<PmmXmlElementConvertable> independentList =
        parametricModel.getIndependent().getElementSet();



    String BoundaryCondition = MathUtilities.getBoundaryCondition(function);

    function = MathUtilities
        .getAllButBoundaryCondition(function.replace(parametricModel.getDepVar() + "=", ""));

    code.append("values <- c()\n");
    if (!StringUtils.isAllEmpty(BoundaryCondition))
      code.append("if(" + BoundaryCondition.replaceAll("\\*", " && ") + "){\n");
    IndepXml variable = null;
    for (int indIndex = 0; indIndex < independentList.size(); indIndex++) {
      if (indIndex == 0)
        variable = (IndepXml) independentList.get(0);
      else {
        IndepXml otherIndependentVariable = (IndepXml) independentList.get(indIndex);
        code.append(otherIndependentVariable.name + " <- " + otherIndependentVariable.min + "\n");
      }
    }
    // independentList.forEach(item -> {
    if (variable != null)
      code.append("for(" + variable.name + " in " + variable.min + ":" + variable.max + "){\n");
    // });
    code.append("values <- c( values," + function + ")\n");

    if (variable != null)
      code.append("}");


    if (!StringUtils.isAllEmpty(BoundaryCondition))
      code.append("}\n");

    return code.toString();
  }

  public String loadDataScript() {
    String dataLoaderScript = "";
    if (dataFile != null) {
      dataLoaderScript +=
          "library(rjson)\n" + "my.JSON <- fromJSON(file=\"" + dataFile.getName() + "\")\n"
              + "df <- lapply(my.JSON, function(timeSeries) # Loop through each \"timeSeries\"\n"
              + "  {\n" + "  # Convert each group to a data frame.\n"
              + "  # This assumes you have 10 elements each time\n"
              + "  data.frame(matrix(unlist(timeSeries), ncol=10, byrow=T))\n" + "  })\n" + "\n"
              + "# Now you have a list of data frames, connect them together in\n"
              + "# one single dataframe\n" + "df <- do.call(rbind, df)\n";
    }
    return dataLoaderScript;
  }
}
