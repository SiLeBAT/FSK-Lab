package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Model1DataTuple implements ViewValue {

	private static final String CONDID = "CondID";
	private static final String COMBASEID = "CombaseID";
	private static final String MISC = "Misc";
	private static final String AGENT = "Organism";
	private static final String MATRIX = "Matrix";
	private static final String TIMESERIES = "MD_Data";
	private static final String MDINFO = "MD_Info";
	private static final String LITMD = "MD_Literatur";
	private static final String DBUUID = "M_DB_UID";
	private static final String MODELCATALOG = "CatModel";
	private static final String MODELCATALOG_SEC = "CatModelSec";
	private static final String ESTMODEL = "EstModel";
	private static final String DEPENDENT = "Dependent";
	private static final String PARAMETER = "Parameter";
	private static final String INDEPENDENT = "Independent";
	private static final String INDEPENDENT_SEC = "IndependentSec";
	private static final String MLIT = "M_Literatur";
	private static final String EMLIT = "EM_Literatur";
	private static final String DATABASEWRITABLE = "DatabaseWritable";

	public Integer condId;
	public Integer globalModelId;
	public String combaseId;
	public Agent agent = new Agent();
	public Matrix matrix = new Matrix();
	public TimeSeriesList timeSeriesList = new TimeSeriesList();
	public MiscList miscList = new MiscList();
	public MdInfo mdInfo = new MdInfo();
	public LiteratureList litMd = new LiteratureList();
	public String dbuuid;
	public CatalogModel catModel = new CatalogModel();
	public CatalogModel catModelSec = new CatalogModel();
	public EstModel estModel = new EstModel();
	public Dep dep = new Dep();
	public ParamList params = new ParamList();
	public ParamList paramsSec = new ParamList();
	public IndepList indeps = new IndepList();
	public IndepList indepsSec = new IndepList();
	public LiteratureList mLit = new LiteratureList();
	public LiteratureList emLit = new LiteratureList();
	public Boolean databaseWritable;

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(CONDID, condId, settings);
		SettingsHelper.addString(COMBASEID, combaseId, settings);
		agent.saveToNodeSettings(settings.addNodeSettings(AGENT));
		matrix.saveToNodeSettings(settings.addNodeSettings(MATRIX));
		timeSeriesList.saveToNodeSettings(settings.addNodeSettings(TIMESERIES));
		miscList.saveToNodeSettings(settings.addNodeSettings(MISC));
		mdInfo.saveToNodeSettings(settings.addNodeSettings(MDINFO));
		litMd.saveToNodeSettings(settings.addNodeSettings(LITMD));
		SettingsHelper.addString(DBUUID, dbuuid, settings);
		catModel.saveToNodeSettings(settings.addNodeSettings(MODELCATALOG));
		catModelSec.saveToNodeSettings(settings.addNodeSettings(MODELCATALOG_SEC));
		estModel.saveToNodeSettings(settings.addNodeSettings(ESTMODEL));
		dep.saveToNodeSettings(settings.addNodeSettings(DEPENDENT));
		params.saveToNodeSettings(settings.addNodeSettings(PARAMETER));
		indeps.saveToNodeSettings(settings.addNodeSettings(INDEPENDENT));
		indepsSec.saveToNodeSettings(settings.addNodeSettings(INDEPENDENT_SEC));
		mLit.saveToNodeSettings(settings.addNodeSettings(MLIT));
		emLit.saveToNodeSettings(settings.addNodeSettings(EMLIT));
		SettingsHelper.addBoolean(DATABASEWRITABLE, databaseWritable, settings);
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		condId = SettingsHelper.getInteger(CONDID, settings);
		combaseId = SettingsHelper.getString(COMBASEID, settings);
		agent.loadFromNodeSettings(settings.getNodeSettings(AGENT));
		matrix.loadFromNodeSettings(settings.getNodeSettings(MATRIX));
		timeSeriesList.loadFromNodeSettings(settings.getNodeSettings(TIMESERIES));
		miscList.loadFromNodeSettings(settings.getNodeSettings(MISC));
		mdInfo.loadFromNodeSettings(settings.getNodeSettings(MDINFO));
		litMd.loadFromNodeSettings(settings.getNodeSettings(LITMD));
		dbuuid = SettingsHelper.getString(DBUUID, settings);
		catModel.loadFromNodeSettings(settings.getNodeSettings(MODELCATALOG));
		catModelSec.loadFromNodeSettings(settings.getNodeSettings(MODELCATALOG_SEC));
		estModel.loadFromNodeSettings(settings.getNodeSettings(ESTMODEL));
		dep.loadFromNodeSettings(settings.getNodeSettings(DEPENDENT));
		params.loadFromNodeSettings(settings.getNodeSettings(PARAMETER));
		indeps.loadFromNodeSettings(settings.getNodeSettings(INDEPENDENT));
		indepsSec.loadFromNodeSettings(settings.getNodeSettings(INDEPENDENT_SEC));
		mLit.loadFromNodeSettings(settings.getNodeSettings(MLIT));
		emLit.loadFromNodeSettings(settings.getNodeSettings(EMLIT));
		databaseWritable = SettingsHelper.getBoolean(DATABASEWRITABLE, settings);
	}
}