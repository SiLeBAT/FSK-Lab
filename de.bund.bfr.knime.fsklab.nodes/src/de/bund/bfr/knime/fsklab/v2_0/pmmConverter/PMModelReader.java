package de.bund.bfr.knime.fsklab.v2_0.pmmConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.threeten.bp.LocalDate;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.environment.DefaultEnvironmentManager;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.metadata.swagger.ModelEquation;
import de.bund.bfr.metadata.swagger.Parameter;
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

  HashMap<String, ParametricModel> m1s;
  HashMap<String, ParametricModel> m2s;
  HashMap<ParametricModel, HashMap<String, ParametricModel>> m_secondaryModels;

  protected void readPrimaryTableIntoParametricModel(BufferedDataTable dataTable) {

    DataTableSpec inSpec = dataTable.getDataTableSpec();
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
          if (hasTs) {
            PmmTimeSeries ts = new PmmTimeSeries(row);
            condID = ts.getCondId();
            // System.err.println(condID);
            tss.put(condID, ts);
          }
          if (hasM1) {
            ParametricModel pm1 = new ParametricModel(row, 1, hasTs ? condID : null);
            m1EstID = pm1.estModelId;
            if (!m1s.containsKey(PRIMARY + pm1.modelId)) {
              // m1s.put(m1EstID, pm1);
              m1s.put(PRIMARY + pm1.modelId, pm1);
            }


            if (hasM2) {
              ParametricModel pm2 = new ParametricModel(row, 2, null);
              m2EstID = pm2.estModelId;
              if (!m2s.containsKey(SECONDARY + pm2.modelId)) {
                m2s.put(SECONDARY + pm2.modelId, pm2);
              }
              if (!m_secondaryModels.containsKey(m1s.get(m1EstID)))
                m_secondaryModels.put(m1s.get(m1EstID), new HashMap<String, ParametricModel>());
              HashMap<String, ParametricModel> hm = m_secondaryModels.get(m1s.get(m1EstID));
              hm.put(pm2.getDepVar(), m2s.get(m2EstID));
            }
          } else if (hasM2) {
            ParametricModel pm2 = new ParametricModel(row, 2, null);
            m2EstID = pm2.estModelId;
            if (!m2s.containsKey(SECONDARY + pm2.modelId)) {
              m2s.put(SECONDARY + pm2.modelId, pm2);
            }
            m_secondaryModels.put(null, new HashMap<String, ParametricModel>());
            HashMap<String, ParametricModel> hm = m_secondaryModels.get(null);
            hm.put(pm2.getDepVar(), m2s.get(m2EstID));
          }
        }

      }
    } catch (PmmException e) {
    }
  }

  public FskPortObject convertParametricModelToFSKObject(ParametricModel parametricModel) {
    PredictiveModel modelMetadata = (PredictiveModel) NodeUtils.initializePridictiveModell();
    modelMetadata.setGeneralInformation(extractGeneralInformation(parametricModel));
    modelMetadata.setModelMath(extractModelMath(parametricModel));
    java.util.Optional<EnvironmentManager> environmentManager =
        Optional.of(new DefaultEnvironmentManager());
    FskPortObject portObj = null;
    try {
      portObj = new FskPortObject("Model script", "Vis Script", modelMetadata, Paths.get("."),
          new ArrayList(), environmentManager, "", "");
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

    for (PmmXmlElementConvertable el : parametricModel.getParameter().getElementSet()) {
      if (el instanceof ParamXml) {
        ParamXml px = (ParamXml) el;
        Parameter parameter = new Parameter();
        parameter.setName(px.name);
        parameter.setId(px.name);
        parameter.setDescription(px.description);
        parameter.setUnit(px.unit);
        parameter.setValue("" + px.value);
        parameter.setError("" + px.error);
        parameter.setMaxValue("" + px.max);
        parameter.setMinValue("" + px.min);

        modelMath.getParameter().add(parameter);
      }
    }
    ModelEquation modelEquation = new ModelEquation();
    modelEquation.setModelEquation(parametricModel.getFormula());
    modelMath.addModelEquationItem(modelEquation);

    QualityMeasures qualityMeasures = new QualityMeasures();
    qualityMeasures.setBic(new BigDecimal(parametricModel.getBic()));
    qualityMeasures.setAic(new BigDecimal(parametricModel.getAic()));
    parametricModel.setRss(null);
    qualityMeasures.setRsquared(new BigDecimal(parametricModel.getRsquared()));
    qualityMeasures.setRmse(new BigDecimal(parametricModel.getRms()));
    modelMath.addQualityMeasuresItem(qualityMeasures);

    return modelMath;
  }

  protected void readPrimaryTable(BufferedDataTable table) {

    modelNames = new LinkedHashMap<>();
    parameters = new LinkedHashMap<>();
    minValues = new LinkedHashMap<>();
    maxValues = new LinkedHashMap<>();
    KnimeRelationReader reader = null;
    if (SchemaFactory.conformsM1Schema(table.getSpec()))
      reader = new KnimeRelationReader(SchemaFactory.createM1Schema(), table);
    else
      reader = new KnimeRelationReader(SchemaFactory.createM2Schema(), table);
    while (reader.hasMoreElements()) {
      KnimeTuple tuple = reader.nextElement();
      PmmXmlDoc modelXml = null;
      if (SchemaFactory.conformsM1Schema(table.getSpec()))
        modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
      else
        modelXml = tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
      String id = PRIMARY + ((CatalogModelXml) modelXml.get(0)).id;

      if (!modelNames.containsKey(id)) {
        PmmXmlDoc params = null;
        if (SchemaFactory.conformsM1Schema(table.getSpec()))
          params = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
        else
          params = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);
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
