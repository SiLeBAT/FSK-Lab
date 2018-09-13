package de.bund.bfr.knime.pmm.js.common.schema;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
public class JsM1DataSchema implements ViewValue {

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

	/**
	 * Returns the catalog model of this {@link JsM1DataSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the catalog model of this {@link JsM1DataSchema}.
	 */
	public CatalogModel getCatalogModel() {
		return catalogModel;
	}

	/**
	 * Sets the catalog model of this {@link JsM1DataSchema}.
	 *
	 * @param catalogModel
	 *            the catalog model to be set
	 */
	public void setCatalogModel(CatalogModel catalogModel) {
		this.catalogModel = catalogModel;
	}

	/**
	 * Returns the dependent variable of this {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the dependent varaible of this {@link JsM1DataSchema}
	 */
	public Dep getDep() {
		return dep;
	}

	/**
	 * Sets the dependent variable of this {@link JsM1DataSchema}.
	 *
	 * @param dep
	 *            the dependent variable to be set
	 */
	public void setDep(Dep dep) {
		this.dep = dep;
	}

	/**
	 * Returns the parameters list of this {@link JsM1DataSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the parameters list of this {@link JsM1DataSchema}
	 */
	public ParamList getParamList() {
		return paramList;
	}

	/**
	 * Sets the parameters list of this {@link JsM1DataSchema}.
	 * 
	 * @param paramList
	 *            the parameters list to be set
	 */
	public void setParamList(ParamList paramList) {
		this.paramList = paramList;
	}

	/**
	 * Returns the independent variables list of this {@link JsM1DataSchema}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the independent variables list of this {@link JsM1DataSchema}
	 */
	public IndepList getIndepList() {
		return indepList;
	}

	/**
	 * Sets the independent variable list of this {@link JsM1DataSchema}.
	 * 
	 * @param indepList
	 *            the independent variable list to be set
	 */
	public void setIndepList(IndepList indepList) {
		this.indepList = indepList;
	}

	/**
	 * Returns the estimated model of this {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the estimated model of this {@link JsM1DataSchema}
	 */
	public EstModel getEstModel() {
		return estModel;
	}

	/**
	 * Sets the estimated model of this {@link JsM1DataSchema}.
	 *
	 * @param estModel
	 *            the estimated model of this {@link JsM1DataSchema}
	 */
	public void setEstModel(EstModel estModel) {
		this.estModel = estModel;
	}

	/**
	 * Returns the model literature list of this {@link JsM1DataSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the model literature list of this {@link JsM1DataSchema}
	 */
	public LiteratureList getmLit() {
		return mLit;
	}

	/**
	 * Sets the model literature list of this {@link JsM1DataSchema}.
	 *
	 * @param mLit
	 *            the model literature list of this {@link JsM1DataSchema}
	 */
	public void setmLit(LiteratureList mLit) {
		this.mLit = mLit;
	}

	/**
	 * Returns the estimated model literature list of this
	 * {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @returns the estimated model literature list of this
	 *          {@link JsM1DataSchema}
	 */
	public LiteratureList getEmLit() {
		return emLit;
	}

	/**
	 * Sets the estimated model literature list of this {@link JsM1DataSchema}.
	 *
	 * @param the
	 *            estimated model literature list of this {@link JsM1DataSchema}
	 */
	public void setEmLit(LiteratureList emLit) {
		this.emLit = emLit;
	}

	/**
	 * Returns whether the database of this {@link JsM1DataSchema} is writable.
	 * 
	 * If not set returns null.
	 *
	 * @return whether the database of this {@link JsM1DataSchema} is writable
	 */
	public Boolean getDatabaseWritable() {
		return dbWritable;
	}

	/**
	 * Sets whether the database of this {@link JsM1DataSchema} is writable.
	 *
	 * @param dbWritable
	 *            whether the database of this {@link JsM1DataSchema} is
	 *            writable
	 */
	public void setDatabaseWritable(Boolean dbWritable) {
		this.dbWritable = dbWritable;
	}

	/**
	 * Returns the database UUID of this {@link JsM1DataSchema}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the database UUID of this {@link JsM1DataSchema}
	 */
	public String getDbuuid() {
		return uuid;
	}

	/**
	 * Sets the database UUID of this {@link JsM1DataSchema}.
	 * 
	 * @param uuid
	 *            the database UUID to be set
	 */
	public void setDbuuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Returns the condId of this {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the condId of this {@link JsM1DataSchema}
	 */
	public Integer getCondId() {
		return condId;
	}

	/**
	 * Sets the condId of this {@link JsM1DataSchema}.
	 *
	 * @param condId
	 *            the condId to be set
	 */
	public void setCondId(Integer condId) {
		this.condId = condId;
	}

	/**
	 * Returns the Combase id of this {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the combase id of this {@link JsM1DataSchema}
	 */
	public String getCombaseId() {
		return combaseId;
	}

	/**
	 * Sets the combase id of this {@link JsM1DataSchema}.
	 *
	 * @param combaseId
	 *            the combase id to be set
	 */
	public void setCombaseId(String combaseId) {
		this.combaseId = combaseId;
	}

	/**
	 * Returns the misc list of this {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the misc list of this {@link JsM1DataSchema}
	 */
	public MiscList getMiscList() {
		return miscList;
	}

	/**
	 * Sets the misc list of this {@link JsM1DataSchema}.
	 *
	 * @param miscList
	 *            the misc list to be set
	 */
	public void setMiscList(MiscList miscList) {
		this.miscList = miscList;
	}

	/**
	 * Returns the agent of this {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the agent of this {@link JsM1DataSchema}
	 */
	public Agent getAgent() {
		return agent;
	}

	/**
	 * Sets the agent of this {@link JsM1DataSchema}.
	 * 
	 * @param agent
	 *            the agent to be set
	 */
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Returns the matrix of this {@link JsM1DataSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the matrix of this {@link JsM1DataSchema}
	 */
	public Matrix getMatrix() {
		return matrix;
	}

	/**
	 * Sets the matrix of this {@link JsM1DataSchema}.
	 *
	 * @param matrix
	 *            the matrix to be set
	 */
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	/**
	 * Returns the TimeSeries list of this {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the TimeSeries list of this {@link JsM1DataSchema}
	 */
	public TimeSeriesList getTimeSeriesList() {
		return timeSeriesList;
	}

	/**
	 * Sets the TimeSeries list of this {@link JsM1DataSchema}.
	 * 
	 * @param timeSeriesList
	 *            the TimeSeries list to be set
	 */
	public void setTimeSeriesList(TimeSeriesList timeSeriesList) {
		this.timeSeriesList = timeSeriesList;
	}

	/**
	 * Returns the model info of this {@link JsM1DataSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the model info of this {@link JsM1DataSchema}
	 */
	public MdInfo getMdInfo() {
		return mdInfo;
	}

	/**
	 * Sets the model info of this {@link JsM1DataSchema}.
	 *
	 * @param mdInfo
	 *            the model info to be set
	 */
	public void setMdInfo(MdInfo mdInfo) {
		this.mdInfo = mdInfo;
	}

	/**
	 * Returns the microbiological data's literature list of this
	 * {@link JsM1DataSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the microbiological data's literature list of this
	 *         {@link JsM1DataSchema}
	 */
	public LiteratureList getLiteratureList() {
		return mdLit;
	}

	/**
	 * Sets the microbiological data's literature list of this
	 * {@link JsM1DataSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the microbiological data's literature list of this
	 *         {@link JsM1DataSchema}
	 */
	public void setLiteratureList(LiteratureList literatureList) {
		this.mdLit = literatureList;
	}

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		// Model1 fields
		catalogModel.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_MODELCATALOG));
		dep.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_DEPENDENT));
		paramList.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_PARAMETER));
		indepList.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_INDEPENDENT));
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
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		// Model1 fields
		catalogModel.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_MODELCATALOG));
		dep.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_DEPENDENT));
		paramList.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_PARAMETER));
		indepList.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_INDEPENDENT));
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
		timeSeriesList.loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_TIMESERIES));
		mdInfo.loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_MDINFO));
		mdLit.loadFromNodeSettings(settings.getNodeSettings(JsTimeSeriesSchema.ATT_LITMD));
	}
}
