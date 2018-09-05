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
import de.bund.bfr.knime.pmm.js.common.CatalogModel;
import de.bund.bfr.knime.pmm.js.common.Dep;
import de.bund.bfr.knime.pmm.js.common.EstModel;
import de.bund.bfr.knime.pmm.js.common.IndepList;
import de.bund.bfr.knime.pmm.js.common.LiteratureList;
import de.bund.bfr.knime.pmm.js.common.ParamList;
import de.bund.bfr.knime.pmm.js.common.SettingsHelper;
import de.bund.bfr.knime.pmm.js.common.ViewValue;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class JsM2Schema implements ViewValue {

  static final String ATT_MODELCATALOG = "CatalogModel_Sec";
  static final String ATT_ESTMODEL = "EstModel_Sec";
  static final String ATT_DEPENDENT = "Dependent_Sec";
  static final String ATT_PARAMETER = "Parameter_Sec";
  static final String ATT_INDEPENDENT = "Independent_Sec";
  static final String ATT_MLIT = "M_Literatur_Sec";
  static final String ATT_EMLIT = "EM_Literatur_Sec";
  static final String ATT_DB_WRITABLE = "Db_Writable_Sec";
  static final String ATT_DBUUID = "Md_Db_Uuid";
  static final String ATT_GLOBAL_MODEL_ID = "GlobalModelId";

  private CatalogModel catalogModel = new CatalogModel();
  private EstModel estModel = new EstModel();
  private Dep dep = new Dep();
  private ParamList paramList = new ParamList();
  private IndepList indepList = new IndepList();
  private LiteratureList mLit = new LiteratureList();
  private LiteratureList emLit = new LiteratureList();
  private Boolean dbWritable;
  private String uuid;
  private Integer globalModelId;

  /**
   * Returns the catalog model of this {@link JsM2Schema}.
   * 
   * If not set returns null.
   *
   * @return the catalog model of this {@link JsM2Schema}.
   */
  public CatalogModel getCatalogModel() {
    return catalogModel;
  }

  /**
   * Sets the catalog model of this {@link JsM2Schema}.
   *
   * @param catalogModel the catalog model to be set
   */
  public void setCatalogModel(CatalogModel catalogModel) {
    this.catalogModel = catalogModel;
  }

  /**
   * Returns the dependent variable of this {@link JsM2Schema}.
   *
   * If not set returns null.
   *
   * @return the dependent varaible of this {@link JsM2Schema}
   */
  public Dep getDep() {
    return dep;
  }

  /**
   * Sets the dependent variable of this {@link JsM2Schema}.
   *
   * @param dep the dependent variable to be set
   */
  public void setDep(Dep dep) {
    this.dep = dep;
  }

  /**
   * Returns the parameters list of this {@link JsM2Schema}.
   * 
   * If not set returns null.
   *
   * @return the parameters list of this {@link JsM2Schema}
   */
  public ParamList getParamList() {
    return paramList;
  }

  /**
   * Sets the parameters list of this {@link JsM2Schema}.
   * 
   * @param paramList the parameters list to be set
   */
  public void setParamList(ParamList paramList) {
    this.paramList = paramList;
  }

  /**
   * Returns the independent variables list of this {@link JsM2Schema}.
   * 
   * If not set returns null.
   * 
   * @return the independent variables list of this {@link JsM2Schema}
   */
  public IndepList getIndepList() {
    return indepList;
  }

  /**
   * Sets the independent variables list of this {@link JsM2Schema}.
   * 
   * @param the independent variables list to be set
   */
  public void setIndepList(IndepList indepList) {
    this.indepList = indepList;
  }

  /**
   * Returns the estimated model of this {@link JsM2Schema}.
   *
   * If not set returns null.
   *
   * @return the estimated model of this {@link JsM2Schema}
   */
  public EstModel getEstModel() {
    return estModel;
  }

  /**
   * Sets the estimated model of this {@link JsM2Schema}.
   *
   * @param estModel the estimated model of this {@link JsM2Schema}
   */
  public void setEstModel(EstModel estModel) {
    this.estModel = estModel;
  }

  /**
   * Returns the model literature list of this {@link JsM2Schema}.
   * 
   * If not set returns null.
   *
   * @return the model literature list of this {@link JsM2Schema}
   */
  public LiteratureList getmLit() {
    return mLit;
  }

  /**
   * Sets the model literature list of this {@link JsM2Schema}.
   *
   * @param mLit the model literature list of this {@link JsM2Schema}
   */
  public void setmLit(LiteratureList mLit) {
    this.mLit = mLit;
  }

  /**
   * Returns the estimated model literature list of this {@link JsM2Schema}.
   *
   * If not set returns null.
   *
   * @returns the estimated model literature list of this {@link JsM2Schema}
   */
  public LiteratureList getEmLit() {
    return emLit;
  }

  /**
   * Sets the estimated model literature list of this {@link JsM2Schema}.
   *
   * @param the estimated model literature list of this {@link JsM2Schema}
   */
  public void setEmLit(LiteratureList emLit) {
    this.emLit = emLit;
  }

  /**
   * Returns whether the database of this {@link JsM2Schema} is writable.
   * 
   * If not set returns null.
   *
   * @return whether the database of this {@link JsM2Schema} is writable
   */
  public Boolean getDatabaseWritable() {
    return dbWritable;
  }

  /**
   * Sets whether the database of this {@link JsM2Schema} is writable.
   *
   * @param dbWritable whether the database of this {@link JsM2Schema} is writable
   */
  public void setDatabaseWritable(Boolean dbWritable) {
    this.dbWritable = dbWritable;
  }

  /**
   * Returns the database UUID of this {@link JsM2Schema}.
   * 
   * If not set returns null.
   * 
   * @return the database UUID of this {@link JsM2Schema}
   */
  public String getDbuuid() {
    return uuid;
  }

  /**
   * Sets the database UUID of this {@link JsM2Schema}.
   * 
   * @param uuid the database UUID to be set
   */
  public void setDbuuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * Returns the global model id of this {@link JsM2Schema}.
   *
   * If not set returns null.
   *
   * @return the global model id of this {@link JsM2Schema}
   */
  public Integer getGlobalModelId() {
    return globalModelId;
  }

  /**
   * Sets the global model id of this {@link JsM2Schema}.
   *
   * @param globalModelId the global model id to be set
   */
  public void setGlobalModelId(Integer globalModelId) {
    this.globalModelId = globalModelId;
  }

  /**
   * Saves Model2Schema properties into a {@link NodeSettingsWO}.
   *
   * @param settings the settings where to save the {@link JsM2Schema} properties
   */
  @Override
  public void saveToNodeSettings(NodeSettingsWO settings) {
    catalogModel.saveToNodeSettings(settings.addNodeSettings(ATT_MODELCATALOG));
    dep.saveToNodeSettings(settings.addNodeSettings(ATT_DEPENDENT));
    paramList.saveToNodeSettings(settings.addNodeSettings(ATT_PARAMETER));
    indepList.saveToNodeSettings(settings.addNodeSettings(ATT_INDEPENDENT));
    mLit.saveToNodeSettings(settings.addNodeSettings(ATT_MLIT));
    emLit.saveToNodeSettings(settings.addNodeSettings(ATT_EMLIT));
    SettingsHelper.addBoolean(ATT_DB_WRITABLE, dbWritable, settings);
    SettingsHelper.addString(ATT_DBUUID, uuid, settings);
    SettingsHelper.addInt(ATT_GLOBAL_MODEL_ID, globalModelId, settings);
  }

  /**
   * Loads Model2Schema properties from a {@link NodeSettingsWO}.
   *
   * @param settings the settings where to load the {@link JsM2Schema} from
   */
  @Override
  public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    catalogModel.loadFromNodeSettings(settings.getNodeSettings(ATT_MODELCATALOG));
    dep.loadFromNodeSettings(settings.getNodeSettings(ATT_DEPENDENT));
    paramList.loadFromNodeSettings(settings.getNodeSettings(ATT_PARAMETER));
    indepList.loadFromNodeSettings(settings.getNodeSettings(ATT_INDEPENDENT));
    mLit.loadFromNodeSettings(settings.getNodeSettings(ATT_MLIT));
    emLit.loadFromNodeSettings(settings.getNodeSettings(ATT_EMLIT));
    dbWritable = SettingsHelper.getBoolean(ATT_DB_WRITABLE, settings);
    uuid = SettingsHelper.getString(ATT_DBUUID, settings);
    globalModelId = SettingsHelper.getInteger(ATT_GLOBAL_MODEL_ID, settings);
  }

  public String toString() {
    ObjectMapper mapper = new ObjectMapper();

    try {

      String jsonInString = mapper.writeValueAsString(this);
      mapper.writeValue(new File("/staff.json"), this);
      return jsonInString;

    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return "jsonerror";


  }
}
