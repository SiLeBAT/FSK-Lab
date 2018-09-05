package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

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

	private Integer condId;
	private Integer globalModelId;
	private String combaseId;
	private Agent agent = new Agent();
	private Matrix matrix = new Matrix();
	private TimeSeriesList timeSeriesList = new TimeSeriesList();
	private MiscList miscList = new MiscList();
	private MdInfo mdInfo = new MdInfo();
	private LiteratureList litMd = new LiteratureList();
	private String dbuuid;
	private CatalogModel catModel = new CatalogModel();
	private CatalogModel catModelSec = new CatalogModel();
	private EstModel estModel = new EstModel();
	private Dep dep = new Dep();
	private ParamList params = new ParamList();
	private ParamList paramsSec = new ParamList();
	private IndepList indeps = new IndepList();
	private IndepList indepsSec = new IndepList();
	private LiteratureList mLit = new LiteratureList();
	private LiteratureList emLit = new LiteratureList();
	private Boolean databaseWritable;

	public Integer getCondId() {
		return condId;
	}

	public Integer getGlobalModelId() {
		return globalModelId;
	};
	
	public String getCombaseId() {
		return combaseId;
	}

	public Agent getAgent() {
		return agent;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public TimeSeriesList getTimeSeriesList() {
		return timeSeriesList;
	}

	public MiscList getMiscList() {
		return miscList;
	}

	public MdInfo getMdInfo() {
		return mdInfo;
	}

	public LiteratureList getLitMd() {
		return litMd;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public CatalogModel getCatModel() {
		return catModel;
	}
	
	public CatalogModel getCatModelSec() {
		return catModelSec;
	}

	public EstModel getEstModel() {
		return estModel;
	}

	public Dep getDep() {
		return dep;
	}

	public ParamList getParams() {
		return params;
	}		
	
	public ParamList getParamsSec() {
		return paramsSec;
	}

	public IndepList getIndeps() {
		return indeps;
	}
	
	public IndepList getIndepsSec() {
		return indepsSec;
	}

	public LiteratureList getmLit() {
		return mLit;
	}

	public LiteratureList getEmLit() {
		return emLit;
	}

	public Boolean isDatabaseWritable() {
		return databaseWritable;
	}

	public void setCondId(final Integer condId) {
		this.condId = condId;
	}
	
	public void setGlobalModelId(final Integer globalModelId) {
		this.globalModelId = globalModelId;
	};

	public void setCombaseId(final String combaseId) {
		this.combaseId = Strings.emptyToNull(combaseId);
	}

	public void setAgent(final Agent agent) {
		this.agent = agent;
	}

	public void setMatrix(final Matrix matrix) {
		this.matrix = matrix;
	}

	public void setTimeSeriesList(final TimeSeriesList timeSeriesList) {
		this.timeSeriesList = timeSeriesList;
	}

	public void setMiscList(final MiscList miscList) {
		this.miscList = miscList;
	}

	public void setMdInfo(final MdInfo mdInfo) {
		this.mdInfo = mdInfo;
	}

	public void setLitMd(final LiteratureList litMd) {
		this.litMd = litMd;
	}

	public void setDbuuid(final String dbuuid) {
		this.dbuuid = Strings.emptyToNull(dbuuid);
	}

	public void setCatModel(final CatalogModel catModel) {
		this.catModel = catModel;
	}
	
	public void setCatModelSec(final CatalogModel catModelSec) {
		this.catModelSec = catModelSec;
	}

	public void setEstModel(final EstModel estModel) {
		this.estModel = estModel;
	}

	public void setDep(final Dep dep) {
		this.dep = dep;
	}

	public void setParams(final ParamList params) {
		this.params = params;
	}
	
	public void setParamsSec(final ParamList params) {
		this.paramsSec = params;
	}

	public void setIndeps(final IndepList indeps) {
		this.indeps = indeps;
	}
	
	public void setIndepsSec(final IndepList indepsSec) {
		this.indepsSec = indepsSec;
	}

	public void setmLit(final LiteratureList mLit) {
		this.mLit = mLit;
	}

	public void setEmLit(final LiteratureList emLit) {
		this.emLit = emLit;
	}

	public void setDatabaseWritable(final Boolean databaseWritable) {
		this.databaseWritable = databaseWritable;
	}

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