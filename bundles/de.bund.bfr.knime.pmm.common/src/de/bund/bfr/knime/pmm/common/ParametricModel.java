/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.common;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

public class ParametricModel implements PmmXmlElementConvertable {

	public static final String ELEMENT_PARAMETRICMODEL = "ParametricModel";
	
	// hier fest verdrahtet und von den zentralen KnimSchema Variablen unabhängig gemacht.
	// Hintergrund ist, dass sonst bei KnimeSchema-Änderungen der MMC seine gespeicherten Daten vergisst!
	private static final String ATT_FORMULA = "Formula";
	private static final String ATT_INDEP = "IndependentXml";
	private static final String ATT_DEP = "DependentXml";
	private static final String ATT_PARAM = "ParameterXml";
	
	private static final String ATT_PARAMNAME = "ParamName";
	private static final String ATT_DEPVAR = "DepVar";
	private static final String ATT_INDEPVAR = "IndepVar";
	private static final String ATT_VALUE = "Value";
	private static final String ATT_MDBUUID = "M_DB_UID";
	private static final String ATT_MODELNAME = "ModelName";
	private static final String ATT_MODELID = "ModelCatalogId";
	private static final String ATT_MODELCLASS = "ModelClass";
	private static final String ATT_EMDBUUID = "EM_DB_UID";
	private static final String ATT_ESTMODELID = "EstModelId";
	private static final String ATT_GLOBALMODELID = "GlobalModelId";
	private static final String ATT_RMS = "RMS";
	private static final String ATT_RSS = "RSS";
	private static final String ATT_AIC = "AIC";
	private static final String ATT_BIC = "BIC";
	private static final String ATT_RSQUARED = "Rsquared";
	private static final String ATT_MINVALUE = "MinValue";
	private static final String ATT_MAXVALUE = "MaxValue";
	private static final String ATT_MININDEP = "MinIndep";
	private static final String ATT_MAXINDEP = "MaxIndep";
	private static final String ATT_PARAMERR = "StandardError";
	private static final String ATT_IS_START = "IsStart";
	private static final String ATT_RMAP = "rMap";
	private static final String ATT_MLIT = "ModelLiterature";
	private static final String ATT_EMLIT = "EstimatedModelLiterature";

	private static final String ELEMENT_PARAM = "Parameter";
	private static final String ATT_CONDID = "CondId";

	private static final String ATT_CHECKED = "ModelChecked";
	private static final String ATT_QSCORE = "ModelQualityScore";
	private static final String ATT_COMMENT = "ModelComment";

	private DepXml depXml = null;
	private PmmXmlDoc independent = null;
	private PmmXmlDoc parameter = null;
	private PmmXmlDoc estLit = null;
	private PmmXmlDoc modelLit = null;

	private String m_dbuuid;
	private String em_dbuuid;
	private String modelName;
	private Integer modelClass;
	private String fittedModelName;
	public String getFittedModelName() {
		return fittedModelName;
	}
	public void setFittedModelName(String fittedModelName) {
		this.fittedModelName = fittedModelName;
	}

	private String formula;

	private int level;

	private int modelId;
	private int estModelId;
	private Double rsquared;
	private Double rss;
	private Double rms;
	private Double aic;
	private Double bic;
	private int condId;
	
	private Integer globalModelId = null;

	public Integer getGlobalModelId() {
		return globalModelId;
	}
	public void setGlobalModelId(Integer globalModelId) {
		this.globalModelId = globalModelId;
	}

	private Boolean isChecked;
	private Integer qualityScore;
	private String comment;
		
	private static final String ATT_LEVEL = "Level";
	private String warningMsg = "";
	
	protected ParametricModel() {
		initVars();
	}
	private void initVars() {
		rss = Double.NaN;
		rsquared = Double.NaN;
		rms = Double.NaN;
		aic = Double.NaN;
		bic = Double.NaN;
		isChecked = null;
		qualityScore = null;
		comment = null;
		modelId = MathUtilities.getRandomNegativeInt();
		estModelId = MathUtilities.getRandomNegativeInt();
		globalModelId = MathUtilities.getRandomNegativeInt();
		
		estLit = new PmmXmlDoc();
		modelLit = new PmmXmlDoc();

		independent = new PmmXmlDoc();
		parameter = new PmmXmlDoc();		
		
	}
	
	public ParametricModel(final String modelName, final String formula, DepXml depXml, final int level, final int modelId, final int estModelId) {		
		this(modelName, formula, depXml, level, modelId);		
		this.estModelId = estModelId;		
	}
	
	public ParametricModel(final String modelName, final String formula, DepXml depXml, final int level, final int modelId) {		
		this(modelName, formula, depXml, level);				
		this.modelId = modelId;
	}
	
	
	public ParametricModel(final String modelName, final String formula, DepXml depXml, final int level) {
		this();			
		this.modelName = modelName;
		setFormula( formula );
		this.depXml = depXml;
		this.level = level;
	}
	
	public ParametricModel(KnimeTuple row, int level, Integer newTsID) throws PmmException {	
		initVars();
		this.level = level;
		
		PmmXmlDoc x = row.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_MODELCATALOG, level));
		if (x != null) {
			for (PmmXmlElementConvertable el : x.getElementSet()) {
				if (el instanceof CatalogModelXml) {
					CatalogModelXml cmx = (CatalogModelXml) el;
					this.modelId = cmx.id;
					this.modelName = cmx.name;
					this.m_dbuuid = cmx.dbuuid;
					setFormula(cmx.formula);
					break;
				}
			}
		}
					
		//this.modelId = row.getInt(Model1Schema.getAttribute(Model1Schema.ATT_MODELID, level));
		//this.modelName = row.getString(Model1Schema.getAttribute(Model1Schema.ATT_MODELNAME, level));
		//setFormula(row.getString(Model1Schema.getAttribute(Model1Schema.ATT_FORMULA, level)));
		PmmXmlDoc depXml = row.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_DEPENDENT, level));
		this.depXml = (DepXml) depXml.getElementSet().get(0);

		this.parameter = row.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_PARAMETER, level));
		this.independent = row.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_INDEPENDENT, level));
		
		this.modelLit = row.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_MLIT, level));
		this.estLit = row.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_EMLIT, level));
		
		x = row.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_ESTMODEL, level));
		if (x != null) {
			for (PmmXmlElementConvertable el : x.getElementSet()) {
				if (el instanceof EstModelXml) {
					EstModelXml emx = (EstModelXml) el;
					this.setEstModelId(emx.id);
					this.rms = emx.rms;
					this.rsquared = emx.r2;
					this.aic = emx.aic;
					this.bic = emx.bic;
					this.qualityScore = emx.qualityScore;
					this.isChecked = emx.checked;
					this.comment = emx.comment;
					this.fittedModelName = emx.name;
					this.em_dbuuid = emx.dbuuid;
					break;
				}
			}
		}
		/*
		Integer rowEstM1ID = row.getInt(Model1Schema.getAttribute(Model1Schema.ATT_ESTMODELID, level));
		this.setEstModelId(rowEstM1ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM1ID);
		this.rms = row.getDouble(Model1Schema.getAttribute(Model1Schema.ATT_RMS, level));
		this.rsquared = row.getDouble(Model1Schema.getAttribute(Model1Schema.ATT_RSQUARED, level));
		this.aic = row.getDouble(Model1Schema.getAttribute(Model1Schema.ATT_AIC, level));
		this.bic = row.getDouble(Model1Schema.getAttribute(Model1Schema.ATT_BIC, level));
		*/
		if (this.rms == null) this.rms = Double.NaN;
		if (this.rsquared == null) this.rsquared = Double.NaN;
		if (this.aic == null) this.aic = Double.NaN;
		if (this.bic == null) this.bic = Double.NaN;
		if (newTsID != null) this.setCondId(newTsID);
		
		if (level == 2) {
			globalModelId = row.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
		}
	}
	public ParametricModel(final Element modelElement) {
		this();
		
		m_dbuuid = modelElement.getAttributeValue( ATT_MDBUUID );
		em_dbuuid = modelElement.getAttributeValue( ATT_EMDBUUID );		
		modelName = modelElement.getAttributeValue( ATT_MODELNAME );
		if (modelElement.getAttributeValue("FittedModelName") != null && !modelElement.getAttributeValue("FittedModelName").isEmpty()) fittedModelName = modelElement.getAttributeValue("FittedModelName");
		modelClass = XmlHelper.getInt(modelElement, ATT_MODELCLASS);
		level = Integer.valueOf( modelElement.getAttributeValue( ATT_LEVEL ) );
		modelId = Integer.valueOf( modelElement.getAttributeValue( ATT_MODELID ) );
		estModelId = Integer.valueOf( modelElement.getAttributeValue( ATT_ESTMODELID ) );
		if (modelElement.getAttributeValue(ATT_GLOBALMODELID) != null) globalModelId = Integer.valueOf(modelElement.getAttributeValue(ATT_GLOBALMODELID));
		
		if (modelElement.getAttributeValue(ATT_CHECKED) != null && !modelElement.getAttributeValue(ATT_CHECKED).isEmpty()) isChecked = Boolean.valueOf( modelElement.getAttributeValue(ATT_CHECKED) );
		if (modelElement.getAttributeValue(ATT_COMMENT) != null && !modelElement.getAttributeValue(ATT_COMMENT).isEmpty()) comment = modelElement.getAttributeValue(ATT_COMMENT);
		if (modelElement.getAttributeValue(ATT_QSCORE) != null && !modelElement.getAttributeValue(ATT_QSCORE).isEmpty()) qualityScore = Integer.valueOf( modelElement.getAttributeValue(ATT_QSCORE) );
		if (modelElement.getAttributeValue(ATT_RSS) != null) rss = Double.valueOf( modelElement.getAttributeValue(ATT_RSS) );
		if (modelElement.getAttributeValue(ATT_RMS) != null) rms = Double.valueOf( modelElement.getAttributeValue(ATT_RMS) );
		if (modelElement.getAttributeValue(ATT_AIC) != null) aic = Double.valueOf( modelElement.getAttributeValue(ATT_AIC) );
		if (modelElement.getAttributeValue(ATT_BIC) != null) bic = Double.valueOf( modelElement.getAttributeValue(ATT_BIC) );
		rsquared = Double.valueOf( modelElement.getAttributeValue( ATT_RSQUARED ) );
		condId = Integer.valueOf( modelElement.getAttributeValue( ATT_CONDID ) );
		
		HashMap<String, String> rMap = new HashMap<>();
		for (Element el : modelElement.getChildren()) {
			if (el.getName().equals(ATT_FORMULA)) {
				formula = el.getText();
			}
			else if (el.getName().equals(ELEMENT_PARAM)) {
				boolean minNull = el.getAttributeValue(ATT_MINVALUE) == null || el.getAttributeValue(ATT_MINVALUE).equals("null");
				boolean maxNull = el.getAttributeValue(ATT_MAXVALUE) == null || el.getAttributeValue(ATT_MAXVALUE).equals("null");
				boolean valNull = el.getAttributeValue(ATT_VALUE) == null || el.getAttributeValue(ATT_VALUE).equals("null");
				boolean errNull = el.getAttributeValue(ATT_PARAMERR) == null || el.getAttributeValue(ATT_PARAMERR).equals("null");
				boolean categoryNull = el.getAttributeValue("Category") == null || el.getAttributeValue("Category").equals("null");
				boolean unitNull = el.getAttributeValue("Unit") == null || el.getAttributeValue("Unit").equals("null");
				
				Double min = minNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MINVALUE));
				Double max = maxNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MAXVALUE));
				Double val = valNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_VALUE));
				Double err = errNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_PARAMERR));
				String category = categoryNull ? null : el.getAttributeValue("Category");
				String unit = unitNull ? null : el.getAttributeValue("Unit");

				ParamXml px = new ParamXml(el.getAttributeValue(ATT_PARAMNAME), XmlHelper.getBoolean(el, ATT_IS_START),
						val,
						err, min, max, null, null, category, unit);
				parameter.add(px);
			}
			else if (el.getName().equals(ATT_INDEPVAR)) {
				boolean minNull = el.getAttributeValue(ATT_MININDEP) == null || el.getAttributeValue(ATT_MININDEP).equals("null");
				boolean maxNull = el.getAttributeValue(ATT_MAXINDEP) == null || el.getAttributeValue(ATT_MAXINDEP).equals("null");
				boolean categoryNull = el.getAttributeValue("Category") == null || el.getAttributeValue("Category").equals("null");
				boolean unitNull = el.getAttributeValue("Unit") == null || el.getAttributeValue("Unit").equals("null");
				Double min = minNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MININDEP));
				Double max = maxNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MAXINDEP));
				String category = categoryNull ? null : el.getAttributeValue("Category");
				String unit = unitNull ? null : el.getAttributeValue("Unit");
				IndepXml ix = new IndepXml(el.getAttributeValue(ATT_PARAMNAME), min, max, category, unit);
				independent.add(ix);
			}
			else if (el.getName().equals(ATT_RMAP)) {
				rMap.put(el.getAttributeValue("NEW"), el.getAttributeValue("OLD"));
			}
			else if (el.getName().equals(ATT_DEPVAR)) {
				depXml = new DepXml(el.getAttributeValue(ATT_PARAMNAME), el.getAttributeValue("Category"), el.getAttributeValue("Unit"));
			}
			else if (el.getName().equals(ATT_MLIT)) {
				try {
					modelLit = new PmmXmlDoc(el.getText());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (el.getName().equals(ATT_EMLIT)) {
				try {
					estLit = new PmmXmlDoc(el.getText());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (el.getName().equals(ATT_INDEP)) {
				try {
					independent = new PmmXmlDoc(el.getText());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (el.getName().equals(ATT_DEP)) {
				try {
					PmmXmlDoc dep = new PmmXmlDoc(el.getText());
					if (dep.size() > 0) depXml = (DepXml) dep.get(0);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (el.getName().equals(ATT_PARAM)) {
				try {
					parameter = new PmmXmlDoc(el.getText());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				assert false;
			}
		}
		for (String name : rMap.keySet()) {
			String origName = rMap.get(name);
			if (depXml.origName.equals(origName)) {
				depXml.name = name;
			}
			else {
				for (PmmXmlElementConvertable el : parameter.getElementSet()) {
					if (el instanceof ParamXml) {
						ParamXml px = (ParamXml) el;
						if (px.getOrigName().equals(origName)) {
							px.setName(name);
							break;
						}
					}
				}
				for (PmmXmlElementConvertable el : independent.getElementSet()) {
					if (el instanceof IndepXml) {
						IndepXml ix = (IndepXml) el;
						if (ix.origName.equals(origName)) {
							ix.name = name;
							break;
						}
					}
				}
			}
		}
	}
	public String getWarning() {
		return warningMsg;
	}
	public void setWarning(String warningMsg) {
		this.warningMsg = warningMsg;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment() {
		return comment;
	}
	public void setChecked(Boolean checked) {
		this.isChecked = checked;
	}
	public Boolean isChecked() {
		return isChecked;
	}
	public void setQualityScore(Integer qScore) {
		this.qualityScore = qScore;
	}
	public Integer getQualityScore() {
		return qualityScore;
	}
	public void setDepXml(DepXml depXml) {this.depXml = depXml;}
	public void setIndependent(PmmXmlDoc independent) {this.independent = independent;}
	public void setParameter(PmmXmlDoc parameter) {this.parameter = parameter;}
	public void setEstLit(PmmXmlDoc estLit) {this.estLit = estLit;}
	public void setMLit(PmmXmlDoc modelLit) {this.modelLit = modelLit;}
	public DepXml getDepXml() {return depXml;}
	public PmmXmlDoc getIndependent() {return independent;}
	public PmmXmlDoc getParameter() {return parameter;}
	public PmmXmlDoc getEstModelLit() {return estLit;}
	public PmmXmlDoc getModelLit() {return modelLit;}
	public PmmXmlDoc getCatModel() {		
		PmmXmlDoc catModel = new PmmXmlDoc();
		CatalogModelXml cmx = new CatalogModelXml(getModelId(), getModelName(), getFormula(), getModelClass(), getMDbUuid()); 
		catModel.add(cmx);
		return catModel;
	}
	public PmmXmlDoc getEstModel() {
		PmmXmlDoc emDoc = new PmmXmlDoc();
		int emid = getEstModelId();
		EstModelXml emx = new EstModelXml(emid, fittedModelName, null, getRms(), getRsquared(), getAic(), getBic(), null, isChecked, qualityScore, getEMDbUuid()); // "EM_" + emid
		emx.comment = comment;
		emDoc.add(emx);
		return emDoc;
	}
	

	public ParametricModel clone() {
		ParametricModel clonedPM = new ParametricModel(modelName, formula, depXml, level, modelId, estModelId);
		clonedPM.setModelClass(modelClass);
		clonedPM.setMDbUuid(m_dbuuid);
		clonedPM.setEMDbUuid(em_dbuuid);

		try {
			clonedPM.setRms(rms);
		} catch (PmmException e) {
			e.printStackTrace();
		}
		clonedPM.setAic(aic);
		clonedPM.setBic(bic);
		try {
			clonedPM.setRss(rss);
		} catch (PmmException e) {
			e.printStackTrace();
		}
		try {
			clonedPM.setRsquared(rsquared);
		} catch (PmmException e) {
			e.printStackTrace();
		}
		clonedPM.setCondId(condId);
		
		clonedPM.setChecked(isChecked);
		clonedPM.setComment(comment);
		clonedPM.setQualityScore(qualityScore);
		clonedPM.setFittedModelName(fittedModelName);
		
		if (depXml != null) clonedPM.setDepXml(new DepXml(depXml.toXmlElement()));
		try {
			if (independent != null) clonedPM.setIndependent(new PmmXmlDoc(independent.toXmlString()));
			if (parameter != null) clonedPM.setParameter(new PmmXmlDoc(parameter.toXmlString()));
			if (estLit != null) clonedPM.setEstLit(new PmmXmlDoc(estLit.toXmlString()));
			if (modelLit != null) clonedPM.setMLit(new PmmXmlDoc(modelLit.toXmlString()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		clonedPM.setGlobalModelId(globalModelId);

		return clonedPM;
	}
	
	public void setRsquared(final Double r2) throws PmmException {				
		if (r2 == null) this.rsquared = Double.NaN;
		else {
			if( r2 > 1 ) {
				throw new PmmException("Rsquared must not exceed 1: " + r2);
			}		
			rsquared = r2;
		}
	}
	public void setRss( final Double rss ) throws PmmException {		
		if (rss == null) this.rss = Double.NaN;
		else {
			if( Double.isInfinite( rss ) ) {
				throw new PmmException( "RSS must be a real positive number." );
			}
			
			if( rss < 0 ) {
				throw new PmmException( "RSS must be a real positive number." );
			}
			
			this.rss = rss;
		}
	}
	
	public void setCondId( final int condId ) { this.condId = condId; }
	
	public void addParam(final String paramName, final Boolean isStartParam) {
		addParam(paramName, isStartParam, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
	}
	public void addParam( final String paramName, final Boolean isStartParam, final Double value, final Double error ) {
		addParam(paramName, isStartParam, value, error, Double.NaN, Double.NaN);
	}
	
	public void addParam( final String paramName, final Boolean isStartParam, final Double value, final Double error, final Double min, final Double max ) {
		addParam(paramName, isStartParam, value, error, min, max, null, null, null);
	}
	public void addParam(final String paramName, final Boolean isStartParam, final Double value, final Double error, final Double min, final Double max, String category, String unit, String description) {
		ParamXml px = new ParamXml(paramName, isStartParam, value, error, min, max, null, null, category, unit);
		if (description != null) px.setDescription(description);
		parameter.add(px);
	}
		
	public void setIndepDescription(final String name, final String description) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					ix.description = description;
					break;
				}
			}
		}
	}
	public void setIndepMax(final String name, final Double max) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					ix.max = max;
					break;
				}
			}
		}
	}
	public void setIndepMin(final String name, final Double min) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					ix.min = min;
					break;
				}
			}
		}
	}
	public void setIndepUnit( final String name, final String unit ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					ix.unit = unit;
					break;
				}
			}
		}
	}
	public void setIndepCategory(final String name, final String category) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					ix.category = category;
					break;
				}
			}
		}
	}
	public void setParamIsStart( final String name, final Boolean isStart ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setIsStartParam(isStart);
					break;
				}
			}
		}
	}	
	public void setParamValue( final String name, final Double value ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setValue(value);
					break;
				}
			}
		}
	}	
	public void setParamError( final String name, final Double error ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setError(error);
					break;
				}
			}
		}
	}	
	public void setParamMin( final String name, final Double min ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setMin(min);
					break;
				}
			}
		}
	}	
	public void setParamDescription(final String name, final String description) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setDescription(description);
					break;
				}
			}
		}
	}
	public void setParamMax( final String name, final Double max ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setMax(max);
					break;
				}
			}
		}
	}
	public void setParamUnit(final String name, final String unit) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setUnit(unit);
					break;
				}
			}
		}
	}
	public void setParamCategory(final String name, final String category) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					px.setCategory(category);
					break;
				}
			}
		}
	}
	
	public void setDepVar(final String depVar) {
		setDepVar(depVar, false);
	}
	public void setDepVar(final String depVar, boolean origNameAlso) {
		if (depXml == null) depXml = new DepXml(depVar);
		else {
			depXml.name = depVar;
			if (origNameAlso) depXml.origName = depVar;
		}
	}
	public void setRms( final Double rms ) throws PmmException {		
		if (rms == null) this.rms = Double.NaN;
		else {
			if( Double.isInfinite( rms ) ) {
				throw new PmmException( "RMS must be a real positive number." );
			}
			
			if( rms < 0 ) {
				throw new PmmException( "RMS must be a real positive number." );
			}
			
			this.rms = rms;
		}
	}
	public void setAic(final Double aic) {this.aic = (aic == null) ? Double.NaN : aic;}
	public void setBic(final Double bic) {this.bic = (bic == null) ? Double.NaN : bic;}
	
	public void removeIndepVar( final String varName ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(varName)) {
					independent.getElementSet().remove(el);
					break;
				}
			}
		}
	}
	public void removeParam( final String varName ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(varName)) {
					parameter.getElementSet().remove(el);
					break;
				}
			}
		}
	}
	public void validateParams() {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				removeParam(ix.name);
			}
		}
	}
	public void addIndepVar( final String varName ) {
		addIndepVar(varName, Double.NaN, Double.NaN);
	}
	public void addIndepVar( final String varName, final Double min, final Double max ) {
		addIndepVar(varName, min, max, null, null, null);
	}
	public void addIndepVar(final String varName, final Double min, final Double max, String category, String unit, String description) {
		IndepXml ix = new IndepXml(varName, min, max, category, unit);
		if (description != null) ix.description = description;
		independent.add(ix);
	}
	
	public void removeParams() {
		parameter = new PmmXmlDoc();
	}
	public void removeIndepVars() {
		independent = new PmmXmlDoc();
	}
	public void removeEstModelLits() {
		estLit = new PmmXmlDoc();
	}
	public void addEstModelLit(final LiteratureItem item) {
		estLit.add(item);
	}
	
	public void removeModelLits() {
		modelLit = new PmmXmlDoc();
	}
	public void addModelLit(final LiteratureItem item) {
		modelLit.add(item);
	}
	
	public Double getParamP(final String paramName) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(paramName)) {
					return px.getP();
				}
			}
		}
		return null;
	}
	public Double getParamT(final String paramName) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(paramName)) {
					return px.getT();
				}
			}
		}
		return null;
	}
	public Boolean getParamIsStart( final String paramName ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(paramName)) {
					return px.isStartParam();
				}
			}
		}
		return null;
	}
	public Double getParamValue( final String paramName ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(paramName)) {
					return px.getValue();
				}
			}
		}
		return null;
	}
	public Double getParamError( final String paramName ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(paramName)) {
					return px.getError();
				}
			}
		}
		return null;
	}
	
	public Double getParamMin( final String name ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return px.getMin();
				}
			}
		}
		return null;
	}
	
	public String getParamDescription( final String name ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return px.getDescription();
				}
			}
		}
		return null;
	}
	public Double getParamMax( final String name ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return px.getMax();
				}
			}
		}
		return null;
	}
	
	public String getParamUnit(final String name) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return px.getUnit();
				}
			}
		}
		return null;
	}
	public String getParamCategory(final String name) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return px.getCategory();
				}
			}
		}
		return null;
	}
	
	public boolean containsIndep( final String name ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean containsParam( final String name ) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	public Double getIndepMin( final String name ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					return ix.min;
				}
			}
		}
		return null;
	}

	public String getIndepDescription(final String name) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					return ix.description;
				}
			}
		}
		return null;
	}
	public Double getIndepMax( final String name ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					return ix.max;
				}
			}
		}
		return null;
	}
	
	public String getIndepUnit( final String name ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					return ix.unit;
				}
			}
		}
		return null;
	}
	public String getIndepCategory( final String name ) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				if (ix.name.equals(name)) {
					return ix.category;
				}
			}
		}
		return null;
	}
	public void setDepDescription(String description) {
		if (depXml != null) depXml.description = description;
	}
	public String getDepDescription() {
		return depXml == null ? null : depXml.description;
	}
	public String getDepCategory() {
		return depXml == null ? null : depXml.category;
	}
	public String getDepUnit() {
		return depXml == null ? null : depXml.unit;
	}
	public void setDepUnit(String unit) {
		if (depXml != null) depXml.unit = unit;
	}
	public void setDepCategory(String category) {
		if (depXml != null) depXml.category = category;
	}
	
	public SortedMap<String, Boolean> getAllParVars(){
		SortedMap<String, Boolean> result = new TreeMap<>();
		if (parameter != null && parameter.getElementSet() != null) {
			for (PmmXmlElementConvertable el : parameter.getElementSet()) {
				if (el instanceof ParamXml) {
					ParamXml px = (ParamXml) el;
					result.put(px.getName(), false);
				}
			}			
		}
		if (independent != null && independent.getElementSet() != null) {
			for (PmmXmlElementConvertable el : independent.getElementSet()) {
				if (el instanceof IndepXml) {
					IndepXml ix = (IndepXml) el;
					result.put(ix.name, true);
				}
			}
		}
		return result;
	}
	public String revertFormula() {
		String result = formula;
		if (depXml != null && !depXml.name.equals(depXml.origName)) {
			result = MathUtilities.replaceVariable(result, depXml.name, depXml.origName);			
		}
		if (parameter != null && parameter.getElementSet() != null) {
			for (PmmXmlElementConvertable el : parameter.getElementSet()) {
				if (el instanceof ParamXml) {
					ParamXml px = (ParamXml) el;
					if (!px.getName().equals(px.getOrigName())) {
						result = MathUtilities.replaceVariable(result, px.getName(), px.getOrigName());									
					}
				}
			}			
		}
		if (independent != null && independent.getElementSet() != null) {
			for (PmmXmlElementConvertable el : independent.getElementSet()) {
				if (el instanceof IndepXml) {
					IndepXml ix = (IndepXml) el;
					if (!ix.name.equals(ix.origName)) {
						result = MathUtilities.replaceVariable(result, ix.name, ix.origName);									
					}
				}
			}
		}
		return result;
	}
	
	public void setFormula( final String formula ) {
		
		this.formula = formula.replaceAll( "~", "=" );
		this.formula = this.formula.replaceAll( "\\s", "" );
	}
	
	public String getFormula() {
		return formula;
	}
	
	public int getLevel() { return level; }
	public int getModelId() { return modelId; }
	public void setModelId(final int modelId) {this.modelId = modelId;}
	public void setModelName(final String modelName) {this.modelName = modelName;}
	public void setModelClass(final Integer modelClass) {this.modelClass = modelClass;}
	public void setMDbUuid(final String dbuuid) {this.m_dbuuid = dbuuid;}
	public void setEMDbUuid(final String dbuuid) {this.em_dbuuid = dbuuid;}
	public int getEstModelId() { return estModelId; }
	public void setEstModelId(final int estModelId) {this.estModelId = estModelId;}
	public int getCondId() { return condId; }
	public Double getRss() { return rss; }
	public Double getRsquared() { return rsquared; }
	public Double getRms() { return rms; }
	public Double getAic() { return aic; }
	public Double getBic() { return bic; }
	public String getModelName() { return modelName; }
	public Integer getModelClass() { return modelClass; }
	public String getMDbUuid() { return m_dbuuid; }
	public String getEMDbUuid() { return em_dbuuid; }
	
	public String getDepVar() {return depXml == null ? null : depXml.name;}
	
	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_PARAMETRICMODEL);		
		modelElement.setAttribute( ATT_MDBUUID, m_dbuuid == null ? "" : m_dbuuid );
		modelElement.setAttribute( ATT_EMDBUUID, em_dbuuid == null ? "" : em_dbuuid );
		modelElement.setAttribute( ATT_MODELNAME, modelName );
		modelElement.setAttribute( ATT_MODELCLASS, XmlHelper.getNonNull(modelClass) );
		modelElement.setAttribute("FittedModelName", fittedModelName == null ? "" : fittedModelName);
		modelElement.setAttribute( ATT_LEVEL, String.valueOf( level ) );
		modelElement.setAttribute( ATT_MODELID, String.valueOf( modelId ) );
		modelElement.setAttribute( ATT_ESTMODELID, String.valueOf( estModelId ) );
		modelElement.setAttribute(ATT_GLOBALMODELID, String.valueOf(globalModelId));
		modelElement.setAttribute( ATT_CONDID, String.valueOf( condId ) );
		modelElement.setAttribute( ATT_RSS, String.valueOf( rss ) );
		modelElement.setAttribute( ATT_RMS, String.valueOf( rms ) );
		modelElement.setAttribute( ATT_AIC, String.valueOf( aic ) );
		modelElement.setAttribute( ATT_BIC, String.valueOf( bic ) );
		modelElement.setAttribute(ATT_CHECKED, isChecked == null ? "" : String.valueOf( isChecked ) );
		modelElement.setAttribute(ATT_COMMENT, comment == null ? "" : comment);
		modelElement.setAttribute( ATT_QSCORE, qualityScore == null ? "" : String.valueOf( qualityScore ) );

		
		modelElement.setAttribute( ATT_RSQUARED, String.valueOf( rsquared ) );
		
		Element element = new Element( ATT_FORMULA );
		element.addContent( formula );
		modelElement.addContent(element);
		
		element = new Element(ATT_PARAM);
		element.addContent(parameter.toXmlString());
		modelElement.addContent(element);
		
		element = new Element(ATT_INDEP);
		element.addContent(independent.toXmlString());
		modelElement.addContent(element);
		
		if (depXml != null) {
			element = new Element(ATT_DEP);
			PmmXmlDoc pd = new PmmXmlDoc();
			pd.add(depXml);
			element.addContent(pd.toXmlString());
			modelElement.addContent(element);
		}

		element = new Element(ATT_MLIT);
		element.addContent(modelLit.toXmlString());
		modelElement.addContent(element);
		element = new Element(ATT_EMLIT);
		element.addContent(estLit.toXmlString());
		modelElement.addContent(element);
		
		return modelElement;
	}
	
	public boolean hasAic() { return !Double.isNaN( aic ); }
	public boolean hasBic() { return !Double.isNaN( bic ); }
	public boolean hasRms() { return !Double.isNaN( rms ); }
	
	public KnimeTuple getKnimeTuple() throws PmmException {		
		KnimeTuple tuple;
		
		if( level == 1 ) {
					
			tuple = new KnimeTuple( new Model1Schema() );
			
			PmmXmlDoc cmDoc = getCatModel();//new PmmXmlDoc();
			tuple.setValue(Model1Schema.ATT_MODELCATALOG, cmDoc);
			
			//tuple.setValue( Model1Schema.ATT_FORMULA, getFormula() );
    		PmmXmlDoc depDoc = new PmmXmlDoc();
    		depDoc.add(depXml == null ? new DepXml(getDepVar(), getDepCategory(), getDepUnit()) : depXml);
    		tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);
			//tuple.setValue( Model1Schema.ATT_MODELNAME, getModelName() );
			//tuple.setValue( Model1Schema.ATT_MODELID, getModelId() );
			//tuple.setValue( Model1Schema.ATT_ESTMODELID, getEstModelId() );
			//tuple.setValue( Model1Schema.ATT_RMS, getRms() );
			//tuple.setValue( Model1Schema.ATT_AIC, getAic() );
			//tuple.setValue( Model1Schema.ATT_BIC, getBic() );
			//tuple.setValue( Model1Schema.ATT_RSQUARED, getRsquared() );
			
			PmmXmlDoc emDoc = getEstModel();//new PmmXmlDoc();
			tuple.setValue(Model1Schema.ATT_ESTMODEL, emDoc);

    		tuple.setValue(Model1Schema.ATT_PARAMETER, parameter);

    		tuple.setValue(Model1Schema.ATT_INDEPENDENT, independent);

    		tuple.setValue(Model1Schema.ATT_MLIT, modelLit);
    		tuple.setValue(Model1Schema.ATT_EMLIT, estLit);
		}
		else {
			
			tuple = new KnimeTuple( new Model2Schema() );
			
			PmmXmlDoc cmDoc = getCatModel();//new PmmXmlDoc();
			tuple.setValue(Model2Schema.ATT_MODELCATALOG, cmDoc);

			//tuple.setValue( Model2Schema.ATT_FORMULA, getFormula() );

    		PmmXmlDoc depDoc = new PmmXmlDoc();
    		depDoc.add(depXml == null ? new DepXml(getDepVar(), getDepCategory(), getDepUnit()) : depXml);
    		tuple.setValue(Model2Schema.ATT_DEPENDENT, depDoc);
    		/*
			tuple.setValue( Model2Schema.ATT_MODELNAME, getModelName() );
			tuple.setValue( Model2Schema.ATT_MODELID, getModelId() );
			tuple.setValue( Model2Schema.ATT_ESTMODELID, getEstModelId() );
			tuple.setValue( Model2Schema.ATT_RMS, getRms() );
			tuple.setValue( Model2Schema.ATT_AIC, getAic() );
			tuple.setValue( Model2Schema.ATT_BIC, getBic() );
			tuple.setValue( Model2Schema.ATT_RSQUARED, getRsquared() );
			*/
			PmmXmlDoc emDoc = getEstModel();//new PmmXmlDoc();
			tuple.setValue(Model2Schema.ATT_ESTMODEL, emDoc);

    		tuple.setValue(Model2Schema.ATT_PARAMETER, parameter);

    		tuple.setValue(Model2Schema.ATT_INDEPENDENT, independent);

    		tuple.setValue(Model2Schema.ATT_MLIT, modelLit);
    		tuple.setValue(Model2Schema.ATT_EMLIT, estLit);
    		
    		if (globalModelId != null) {
    			tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelId);
    		}
		}
				
		return tuple;
	}
	
	public String toString() {
		return (fittedModelName != null && !fittedModelName.isEmpty()) ? fittedModelName : modelName;
	}
}
