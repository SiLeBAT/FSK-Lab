package de.bund.bfr.knime.pmm.js.common.schema;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
public class JsM12Schema implements ViewValue {
	
	static final String ATT_MODEL2 = "Model2";
	
	// model 1 schema fields
	private CatalogModel catalogModel = new CatalogModel();
	private Dep dep = new Dep();
	private ParamList paramList = new ParamList();
	private IndepList indepList = new IndepList();
	private EstModel estModel = new EstModel();
	private LiteratureList mLit = new LiteratureList();
	private LiteratureList emLit = new LiteratureList();
	private Boolean dbWritable;
	private String dbuuid;
	
	// model 2 schemas
	private JsM2SchemaList m2List = new JsM2SchemaList();

	public CatalogModel getCatalogModel() {
		return catalogModel;
	}

	public void setCatalogModel(CatalogModel catalogModel) {
		this.catalogModel = catalogModel;
	}

	public Dep getDep() {
		return dep;
	}

	public void setDep(Dep dep) {
		this.dep = dep;
	}

	public ParamList getParamList() {
		return paramList;
	}

	public void setParamList(ParamList paramList) {
		this.paramList = paramList;
	}
	
	public IndepList getIndepList() {
		return indepList;
	}
	
	public void setIndepList(IndepList indepList) {
		this.indepList = indepList;
	}

	public EstModel getEstModel() {
		return estModel;
	}

	public void setEstModel(EstModel estModel) {
		this.estModel = estModel;
	}

	public LiteratureList getmLit() {
		return mLit;
	}

	public void setmLit(LiteratureList mLit) {
		this.mLit = mLit;
	}

	public LiteratureList getEmLit() {
		return emLit;
	}

	public void setEmLit(LiteratureList emLit) {
		this.emLit = emLit;
	}

	public Boolean getDbWritable() {
		return dbWritable;
	}

	public void setDbWritable(Boolean dbWritable) {
		this.dbWritable = dbWritable;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}

	public JsM2SchemaList getM2List() {
		return m2List;
	}

	public void setM2List(JsM2SchemaList m2List) {
		this.m2List = m2List;
	}

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		// Saves Model1 fields
		catalogModel.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_MODELCATALOG));
		dep.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_DEPENDENT));
		paramList.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_PARAMETER));
		estModel.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_ESTMODEL));
		mLit.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_MLIT));
		emLit.saveToNodeSettings(settings.addNodeSettings(JsM1Schema.ATT_EMLIT));
		SettingsHelper.addBoolean(JsM1Schema.ATT_DATABASEWRITABLE, dbWritable, settings);
		SettingsHelper.addString(JsM1Schema.ATT_DBUUID, dbuuid, settings);

		// Saves list of Model2
		m2List.saveToNodeSettings(settings.addNodeSettings(ATT_MODEL2));
	}
	
	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		// Loads Model1 fields
		catalogModel.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_MODELCATALOG));
		dep.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_DEPENDENT));
		paramList.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_PARAMETER));
		estModel.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_ESTMODEL));
		mLit.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_MLIT));
		emLit.loadFromNodeSettings(settings.getNodeSettings(JsM1Schema.ATT_EMLIT));
		dbWritable = SettingsHelper.getBoolean(JsM1Schema.ATT_DATABASEWRITABLE, settings);
		dbuuid = SettingsHelper.getString(JsM1Schema.ATT_DBUUID, settings);
		
		// Loads list of Model2
		m2List.loadFromNodeSettings(settings.getNodeSettings(ATT_MODEL2));
	}
}
