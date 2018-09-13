package de.bund.bfr.knime.pmm.js.common.schema;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.js.common.Agent;
import de.bund.bfr.knime.pmm.js.common.LiteratureList;
import de.bund.bfr.knime.pmm.js.common.Matrix;
import de.bund.bfr.knime.pmm.js.common.MdInfo;
import de.bund.bfr.knime.pmm.js.common.MiscList;
import de.bund.bfr.knime.pmm.js.common.SettingsHelper;
import de.bund.bfr.knime.pmm.js.common.TimeSeriesList;
import de.bund.bfr.knime.pmm.js.common.ViewValue;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class JsTimeSeriesSchema implements ViewValue {

	static final String ATT_CONDID = "CondID";
	static final String ATT_COMBASEID = "CombaseID";
	static final String ATT_MISC = "Misc";
	static final String ATT_AGENT = "Organism";
	static final String ATT_MATRIX = "Matrix";
	static final String ATT_TIMESERIES = "MD_Data";
	static final String ATT_MDINFO = "MD_Info";
	static final String ATT_LITMD = "MD_Literature";
	static final String ATT_DBUUID = "MD_DB_UUID";

	private Integer condId;
	private String combaseId;
	private MiscList miscList = new MiscList();
	private Agent agent = new Agent();
	private Matrix matrix = new Matrix();
	private TimeSeriesList timeSeriesList = new TimeSeriesList();
	private MdInfo mdInfo = new MdInfo();
	private LiteratureList literatureList = new LiteratureList();
	private String dbuuid;
	
	/**
	 * Returns the condId of this {@link JsTimeSeriesSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the condId of this {@link JsTimeSeriesSchema}
	 */
	public Integer getCondId() {
		return condId;
	}

	/**
	 * Sets the condId of this {@link JsTimeSeriesSchema}.
	 *
	 * @param condId the condId to be set
	 */
	public void setCondId(Integer condId) {
		this.condId = condId;
	}

	/**
	 * Returns the Combase id of this {@link JsTimeSeriesSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the combase id of this {@link JsTimeSeriesSchema}
	 */
	public String getCombaseId() {
		return combaseId;
	}

	/**
	 * Sets the combase id of this {@link JsTimeSeriesSchema}.
 	 *
 	 * @param combaseId the combase id to be set
 	 */
	public void setCombaseId(String combaseId) {
		this.combaseId = combaseId;
	}

	/**
	 * Returns the misc list of this {@link JsTimeSeriesSchema}.
 	 *
	 * If not set returns null.
	 *
	 * @return the misc list of this {@link JsTimeSeriesSchema}
	 */
	public MiscList getMiscList() {
		return miscList;
	}

	/**
	 * Sets the misc list of this {@link JsTimeSeriesSchema}.
	 *
	 * @param miscList the misc list to be set
	 */
	public void setMiscList(MiscList miscList) {
		this.miscList = miscList;
	}

	/**
	 * Returns the agent of this {@link JsTimeSeriesSchema}.
 	 *
 	 * If not set returns null.
	 *
	 * @return the agent of this {@link JsTimeSeriesSchema}
	 */
	public Agent getAgent() {
		return agent;
	}

	/**
	 * Sets the agent of this {@link JsTimeSeriesSchema}.
	 * 
	 * @param agent the agent to be set
	 */
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Returns the matrix of this {@link JsTimeSeriesSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the matrix of this {@link JsTimeSeriesSchema}
	 */
	public Matrix getMatrix() {
		return matrix;
	}

	/**
	 * Sets the matrix of this {@link JsTimeSeriesSchema}.
	 *
	 * @param matrix the matrix to be set
	 */
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	/**
	 * Returns the TimeSeries list of this {@link JsTimeSeriesSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the TimeSeries list of this {@link JsTimeSeriesSchema}
	 */
	public TimeSeriesList getTimeSeriesList() {
		return timeSeriesList;
	}

	/**
	 * Sets the TimeSeries list of this {@link JsTimeSeriesSchema}.
	 * 
	 * @param timeSeriesList the TimeSeries list to be set
	 */
	public void setTimeSeriesList(TimeSeriesList timeSeriesList) {
		this.timeSeriesList = timeSeriesList;
	}

	/**
	 * Returns the model info of this {@link JsTimeSeriesSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the model info of this {@link JsTimeSeriesSchema}
	 */
	public MdInfo getMdInfo() {
		return mdInfo;
	}

	/**
	 * Sets the model info of this {@link JsTimeSeriesSchema}.
	 *
	 * @param mdInfo the model info to be set
	 */
	public void setMdInfo(MdInfo mdInfo) {
		this.mdInfo = mdInfo;
	}

	/**
	 * Returns the literature list of this {@link JsTimeSeriesSchema}.
	 * 
	 * If not set returns null.
	 *
	 * @return the literature list of this {@link JsTimeSeriesSchema}
	 */
	public LiteratureList getLiteratureList() {
		return literatureList;
	}

	/**
	 * Sets the literature list of this {@link JsTimeSeriesSchema}.
	 *
	 * If not set returns null.
	 *
	 * @return the literature list of this {@link JsTimeSeriesSchema}
	 */
	public void setLiteratureList(LiteratureList literatureList) {
		this.literatureList = literatureList;
	}

	/**
	 * Returns the database UUID of this {@link JsTimeSeriesSchema}.
	 *
	 * If not set returns null.
     *
     * @return the database UUID of this {@link JsTimeSeriesSchema}
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/**
	 * Sets the database UUID of this {@link JsTimeSeriesSchema}.
	 *
	 * @param dbuuid the database UUID to be set
	 */
	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
	
	
	/**
	 * Saves TimeSeriesSchema properties into a {@link NodeSettingsWO}.
	 *
	 * @param settings
	 * 		the settings where to save the {@link JsTimeSeriesSchema} properties
	 */
	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(ATT_CONDID, condId, settings);
		SettingsHelper.addString(ATT_COMBASEID, combaseId, settings);
		miscList.saveToNodeSettings(settings.addNodeSettings(ATT_MISC));
		agent.saveToNodeSettings(settings.addNodeSettings(ATT_AGENT));
		matrix.saveToNodeSettings(settings.addNodeSettings(ATT_MATRIX));
		timeSeriesList.saveToNodeSettings(settings.addNodeSettings(ATT_TIMESERIES));
		mdInfo.saveToNodeSettings(settings.addNodeSettings(ATT_MDINFO));
		literatureList.saveToNodeSettings(settings.addNodeSettings(ATT_LITMD));
		SettingsHelper.addString(ATT_DBUUID, dbuuid, settings);
	}
	
	/**
	 * Loads TimeSeriesSchema properties from a {@link NodeSettingsRO}.
	 *
	 * @param settings
	 *		The settings where to load the {@link JsTimeSeriesSchema} from
	 */
	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		condId = SettingsHelper.getInteger(ATT_CONDID, settings);
		combaseId = SettingsHelper.getString(ATT_COMBASEID, settings);
		miscList.loadFromNodeSettings(settings.getNodeSettings(ATT_MISC));
		agent.loadFromNodeSettings(settings.getNodeSettings(ATT_AGENT));
		matrix.loadFromNodeSettings(settings.getNodeSettings(ATT_MATRIX));
		timeSeriesList.loadFromNodeSettings(settings.getNodeSettings(ATT_TIMESERIES));
		mdInfo.loadFromNodeSettings(settings.getNodeSettings(ATT_MDINFO));
		literatureList.loadFromNodeSettings(settings.getNodeSettings(ATT_LITMD));
		dbuuid = SettingsHelper.getString(ATT_DBUUID, settings);
	}
}
