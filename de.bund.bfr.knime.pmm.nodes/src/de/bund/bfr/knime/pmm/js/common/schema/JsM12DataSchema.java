package de.bund.bfr.knime.pmm.js.common.schema;

import java.io.File;
import java.io.IOException;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.pmm.js.common.Agent;
import de.bund.bfr.knime.pmm.js.common.CatalogModel;
import de.bund.bfr.knime.pmm.js.common.Dep;
import de.bund.bfr.knime.pmm.js.common.EstModel;
import de.bund.bfr.knime.pmm.js.common.IndepList;
import de.bund.bfr.knime.pmm.js.common.LiteratureList;
import de.bund.bfr.knime.pmm.js.common.Matrix;
import de.bund.bfr.knime.pmm.js.common.MdInfo;
import de.bund.bfr.knime.pmm.js.common.MiscList;
import de.bund.bfr.knime.pmm.js.common.ParamList;
import de.bund.bfr.knime.pmm.js.common.SettingsHelper;
import de.bund.bfr.knime.pmm.js.common.TimeSeriesList;
import de.bund.bfr.knime.pmm.js.common.ViewValue;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class JsM12DataSchema implements ViewValue {
  // Model1 fields
  private CatalogModel catalogModel = new CatalogModel();
  private Dep dep = new Dep();
  private ParamList paramList = new ParamList();
  private IndepList indepList = new IndepList();
  private EstModel estModel = new EstModel();
  private LiteratureList mLit = new LiteratureList();
  private LiteratureList emLit = new LiteratureList();
  private Boolean dbWritable;
  private String uuid;

  // TimeSeriesSchema fields
  private Integer condId;
  private String combaseId;
  private MiscList miscList = new MiscList();
  private Agent agent = new Agent();
  private Matrix matrix = new Matrix();
  private TimeSeriesList timeSeriesList = new TimeSeriesList();
  private MdInfo mdInfo = new MdInfo();
  private LiteratureList mdLit = new LiteratureList();

  // Model2
  private JsM2SchemaList m2List = new JsM2SchemaList();

  /**
   * Returns the catalog model of this {@link JsM12DataSchema}.
   * 
   * If not set returns null.
   *
   * @return the catalog model of this {@link JsM12DataSchema}.
   */
  public CatalogModel getCatalogModel() {
    return catalogModel;
  }

  /**
   * Sets the catalog model of this {@link JsM12DataSchema}.
   *
   * @param catalogModel the catalog model to be set
   */
  public void setCatalogModel(CatalogModel catalogModel) {
    this.catalogModel = catalogModel;
  }

  /**
   * Returns the dependent variable of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @return the dependent varaible of this {@link JsM12DataSchema}
   */
  public Dep getDep() {
    return dep;
  }

  /**
   * Sets the dependent variable of this {@link JsM12DataSchema}.
   *
   * @param dep the dependent variable to be set
   */
  public void setDep(Dep dep) {
    this.dep = dep;
  }

  /**
   * Returns the parameters list of this {@link JsM12DataSchema}.
   * 
   * If not set returns null.
   *
   * @return the parameters list of this {@link JsM12DataSchema}
   */
  public ParamList getParamList() {
    return paramList;
  }

  /**
   * Sets the parameters list of this {@link JsM12DataSchema}.
   * 
   * @param paramList the parameters list to be set
   */
  public void setParamList(ParamList paramList) {
    this.paramList = paramList;
  }

  /**
   * Returns the independent variables list of this {@link JsM12DataSchema}.
   * 
   * If not set returns null.
   * 
   * @return the independent variables list of this {@link JsM12DataSchema}
   */
  public IndepList getIndepList() {
    return indepList;
  }

  /**
   * Sets the independent variables list of this {@link JsM12DataSchema}.
   * 
   * @param the independent variables list of this {@link JsM12DataSchema}
   */
  public void setIndepList(IndepList indepList) {
    this.indepList = indepList;
  }

  /**
   * Returns the estimated model of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @return the estimated model of this {@link JsM12DataSchema}
   */
  public EstModel getEstModel() {
    return estModel;
  }

  /**
   * Sets the estimated model of this {@link JsM12DataSchema}.
   *
   * @param estModel the estimated model of this {@link JsM12DataSchema}
   */
  public void setEstModel(EstModel estModel) {
    this.estModel = estModel;
  }

  /**
   * Returns the model literature list of this {@link JsM12DataSchema}.
   * 
   * If not set returns null.
   *
   * @return the model literature list of this {@link JsM12DataSchema}
   */
  public LiteratureList getmLit() {
    return mLit;
  }

  /**
   * Sets the model literature list of this {@link JsM12DataSchema}.
   *
   * @param mLit the model literature list of this {@link JsM12DataSchema}
   */
  public void setmLit(LiteratureList mLit) {
    this.mLit = mLit;
  }

  /**
   * Returns the estimated model literature list of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @returns the estimated model literature list of this {@link JsM12DataSchema}
   */
  public LiteratureList getEmLit() {
    return emLit;
  }

  /**
   * Sets the estimated model literature list of this {@link JsM12DataSchema}.
   *
   * @param the estimated model literature list of this {@link JsM12DataSchema}
   */
  public void setEmLit(LiteratureList emLit) {
    this.emLit = emLit;
  }

  /**
   * Returns whether the database of this {@link JsM12DataSchema} is writable.
   * 
   * If not set returns null.
   *
   * @return whether the database of this {@link JsM12DataSchema} is writable
   */
  public Boolean getDatabaseWritable() {
    return dbWritable;
  }

  /**
   * Sets whether the database of this {@link JsM12DataSchema} is writable.
   *
   * @param dbWritable whether the database of this {@link JsM12DataSchema} is writable
   */
  public void setDatabaseWritable(Boolean dbWritable) {
    this.dbWritable = dbWritable;
  }

  /**
   * Returns the database UUID of this {@link JsM12DataSchema}.
   * 
   * If not set returns null.
   * 
   * @return the database UUID of this {@link JsM12DataSchema}
   */
  public String getDbuuid() {
    return uuid;
  }

  /**
   * Sets the database UUID of this {@link JsM12DataSchema}.
   * 
   * @param uuid the database UUID to be set
   */
  public void setDbuuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * Returns the condId of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @return the condId of this {@link JsM12DataSchema}
   */
  public Integer getCondId() {
    return condId;
  }

  /**
   * Sets the condId of this {@link JsM12DataSchema}.
   *
   * @param condId the condId to be set
   */
  public void setCondId(Integer condId) {
    this.condId = condId;
  }

  /**
   * Returns the Combase id of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @return the combase id of this {@link JsM12DataSchema}
   */
  public String getCombaseId() {
    return combaseId;
  }

  /**
   * Sets the combase id of this {@link JsM12DataSchema}.
   *
   * @param combaseId the combase id to be set
   */
  public void setCombaseId(String combaseId) {
    this.combaseId = combaseId;
  }

  /**
   * Returns the misc list of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @return the misc list of this {@link JsM12DataSchema}
   */
  public MiscList getMiscList() {
    return miscList;
  }

  /**
   * Sets the misc list of this {@link JsM12DataSchema}.
   *
   * @param miscList the misc list to be set
   */
  public void setMiscList(MiscList miscList) {
    this.miscList = miscList;
  }

  /**
   * Returns the agent of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @return the agent of this {@link JsM12DataSchema}
   */
  public Agent getAgent() {
    return agent;
  }

  /**
   * Sets the agent of this {@link JsM12DataSchema}.
   * 
   * @param agent the agent to be set
   */
  public void setAgent(Agent agent) {
    this.agent = agent;
  }

  /**
   * Returns the matrix of this {@link JsM12DataSchema}.
   * 
   * If not set returns null.
   *
   * @return the matrix of this {@link JsM12DataSchema}
   */
  public Matrix getMatrix() {
    return matrix;
  }

  /**
   * Sets the matrix of this {@link JsM12DataSchema}.
   *
   * @param matrix the matrix to be set
   */
  public void setMatrix(Matrix matrix) {
    this.matrix = matrix;
  }

  /**
   * Returns the TimeSeries list of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @return the TimeSeries list of this {@link JsM12DataSchema}
   */
  public TimeSeriesList getTimeSeriesList() {
    return timeSeriesList;
  }

  /**
   * Sets the TimeSeries list of this {@link JsM12DataSchema}.
   * 
   * @param timeSeriesList the TimeSeries list to be set
   */
  public void setTimeSeriesList(TimeSeriesList timeSeriesList) {
    this.timeSeriesList = timeSeriesList;
  }

  /**
   * Returns the model info of this {@link JsM12DataSchema}.
   * 
   * If not set returns null.
   *
   * @return the model info of this {@link JsM12DataSchema}
   */
  public MdInfo getMdInfo() {
    return mdInfo;
  }

  /**
   * Sets the model info of this {@link JsM12DataSchema}.
   *
   * @param mdInfo the model info to be set
   */
  public void setMdInfo(MdInfo mdInfo) {
    this.mdInfo = mdInfo;
  }

  /**
   * Returns the microbiological data's literature list of this {@link JsM12DataSchema}.
   * 
   * If not set returns null.
   *
   * @return the microbiological data's literature list of this {@link JsM12DataSchema}
   */
  public LiteratureList getLiteratureList() {
    return mdLit;
  }

  /**
   * Sets the microbiological data's literature list of this {@link JsM12DataSchema}.
   *
   * If not set returns null.
   *
   * @return the microbiological data's literature list of this {@link JsM12DataSchema}
   */
  public void setLiteratureList(LiteratureList literatureList) {
    this.mdLit = literatureList;
  }

  public JsM2SchemaList getM2List() {
    return m2List;
  }

  public void setM2List(JsM2SchemaList m2List) {
    this.m2List = m2List;
  }

  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    // Model1 fields
    catalogModel.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_MODELCATALOG));
    dep.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_DEPENDENT));
    paramList.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_PARAMETER));
    estModel.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_ESTMODEL));
    mLit.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_MLIT));
    emLit.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_EMLIT));
    SettingsHelper.addBoolean(JsM1Schema.ATT_DATABASEWRITABLE, dbWritable, settings);
    SettingsHelper.addString(JsM1Schema.ATT_DBUUID, uuid, settings);

    // TimeSeriesSchema fields
    SettingsHelper.addInt(JsTimeSeriesSchema.ATT_CONDID, condId, settings);
    SettingsHelper.addString(JsTimeSeriesSchema.ATT_COMBASEID, combaseId, settings);
    miscList.saveToNodeSettings(settings.addNodeSettings(JsTimeSeriesSchema.ATT_MISC));
    agent.saveToNodeSettings(settings.addNodeSettings(JsTimeSeriesSchema.ATT_AGENT));
    matrix.saveToNodeSettings(settings.addNodeSettings(JsTimeSeriesSchema.ATT_MATRIX));
    timeSeriesList.saveToNodeSettings(settings.addNodeSettings(JsTimeSeriesSchema.ATT_TIMESERIES));
    mdInfo.saveToNodeSettings(settings.addNodeSettings(JsTimeSeriesSchema.ATT_MDINFO));
    mdLit.saveToNodeSettings(settings.addNodeSettings(JsTimeSeriesSchema.ATT_LITMD));

    // Saves Model2 list
    m2List.saveToNodeSettings(settings.addNodeSettings(JsM12Schema.ATT_MODEL2));
  }

  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // Model1 fields
    catalogModel.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_MODELCATALOG));
    dep.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_DEPENDENT));
    paramList.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_PARAMETER));
    estModel.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_ESTMODEL));
    mLit.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_MLIT));
    emLit.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_EMLIT));
    dbWritable = SettingsHelper.getBoolean(JsM1Schema.ATT_DATABASEWRITABLE, settings);
    uuid = SettingsHelper.getString(JsM1Schema.ATT_DBUUID, settings);

    // TimeSeriesSchema fields
    condId = SettingsHelper.getInteger(JsTimeSeriesSchema.ATT_CONDID, settings);
    combaseId = SettingsHelper.getString(JsTimeSeriesSchema.ATT_COMBASEID, settings);
    miscList.loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_MISC));
    agent.loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_AGENT));
    matrix.loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_MATRIX));
    timeSeriesList
        .loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_TIMESERIES));
    mdInfo.loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_MDINFO));
    mdLit.loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_LITMD));

    // Loads Model2 list
    m2List.loadFromNodeSettings(settings.getNodeSettings(JsM12Schema.ATT_MODEL2));
  }

  public String toString() {
    ObjectMapper mapper = new ObjectMapper();

    try {

      String jsonInString = mapper.writeValueAsString(this);
      mapper.writeValue(new File("/staff.json"), this);

      // System.out.println(jsonInString);
      return jsonInString;

    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return "hiiiii";


  }
}
