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

	// hier fest verdrahtet und von den zentralen KnimSchema Variablen unabhängig
	// gemacht.
	// Hintergrund ist, dass sonst bei KnimeSchema-Änderungen der MMC seine
	// gespeicherten Daten vergisst!
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

	public String modelDbUuid;
	public String estimatedModelDbUuid;

	public String modelName;

	public Integer modelClass;

	public String fittedModelName;

	private String formula;

	private int level;

	public int modelId;

	public int estModelId;

	private Double rsquared;
	private Double rss;
	private Double rms;
	private Double aic;
	private Double bic;

	public int condId;

	public Integer globalModelId = null;

	public Boolean isChecked;

	public Integer qualityScore;

	public String comment;

	private static final String ATT_LEVEL = "Level";

	public String warning = "";

	public ParametricModel() {
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

	public ParametricModel(final String modelName, final String formula, DepXml depXml, final int level,
			final int modelId, final int estModelId) {
		this(modelName, formula, depXml, level, modelId);
		this.estModelId = estModelId;
	}

	public ParametricModel(final String modelName, final String formula, DepXml depXml, final int level,
			final int modelId) {
		this(modelName, formula, depXml, level);
		this.modelId = modelId;
	}

	public ParametricModel(final String modelName, final String formula, DepXml depXml, final int level) {
		this();
		this.modelName = modelName;
		setFormula(formula);
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
					this.modelDbUuid = cmx.dbuuid;
					setFormula(cmx.formula);
					break;
				}
			}
		}

		// this.modelId = row.getInt(Model1Schema.getAttribute(Model1Schema.ATT_MODELID,
		// level));
		// this.modelName =
		// row.getString(Model1Schema.getAttribute(Model1Schema.ATT_MODELNAME, level));
		// setFormula(row.getString(Model1Schema.getAttribute(Model1Schema.ATT_FORMULA,
		// level)));
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
					this.estModelId = emx.id;
					this.rms = emx.rms;
					this.rsquared = emx.r2;
					this.aic = emx.aic;
					this.bic = emx.bic;
					this.qualityScore = emx.qualityScore;
					this.isChecked = emx.checked;
					this.comment = emx.comment;
					this.fittedModelName = emx.name;
					this.estimatedModelDbUuid = emx.dbuuid;
					break;
				}
			}
		}
		/*
		 * Integer rowEstM1ID =
		 * row.getInt(Model1Schema.getAttribute(Model1Schema.ATT_ESTMODELID, level));
		 * this.setEstModelId(rowEstM1ID == null ? MathUtilities.getRandomNegativeInt()
		 * : rowEstM1ID); this.rms =
		 * row.getDouble(Model1Schema.getAttribute(Model1Schema.ATT_RMS, level));
		 * this.rsquared =
		 * row.getDouble(Model1Schema.getAttribute(Model1Schema.ATT_RSQUARED, level));
		 * this.aic = row.getDouble(Model1Schema.getAttribute(Model1Schema.ATT_AIC,
		 * level)); this.bic =
		 * row.getDouble(Model1Schema.getAttribute(Model1Schema.ATT_BIC, level));
		 */
		if (this.rms == null)
			this.rms = Double.NaN;
		if (this.rsquared == null)
			this.rsquared = Double.NaN;
		if (this.aic == null)
			this.aic = Double.NaN;
		if (this.bic == null)
			this.bic = Double.NaN;
		if (newTsID != null)
			this.condId = newTsID;

		if (level == 2) {
			globalModelId = row.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
		}
	}

	public ParametricModel(final Element modelElement) {
		this();

		modelDbUuid = modelElement.getAttributeValue(ATT_MDBUUID);
		estimatedModelDbUuid = modelElement.getAttributeValue(ATT_EMDBUUID);
		modelName = modelElement.getAttributeValue(ATT_MODELNAME);
		if (modelElement.getAttributeValue("FittedModelName") != null
				&& !modelElement.getAttributeValue("FittedModelName").isEmpty())
			fittedModelName = modelElement.getAttributeValue("FittedModelName");
		modelClass = XmlHelper.getInt(modelElement, ATT_MODELCLASS);
		level = Integer.valueOf(modelElement.getAttributeValue(ATT_LEVEL));
		modelId = Integer.valueOf(modelElement.getAttributeValue(ATT_MODELID));
		estModelId = Integer.valueOf(modelElement.getAttributeValue(ATT_ESTMODELID));
		if (modelElement.getAttributeValue(ATT_GLOBALMODELID) != null)
			globalModelId = Integer.valueOf(modelElement.getAttributeValue(ATT_GLOBALMODELID));

		if (modelElement.getAttributeValue(ATT_CHECKED) != null
				&& !modelElement.getAttributeValue(ATT_CHECKED).isEmpty())
			isChecked = Boolean.valueOf(modelElement.getAttributeValue(ATT_CHECKED));
		if (modelElement.getAttributeValue(ATT_COMMENT) != null
				&& !modelElement.getAttributeValue(ATT_COMMENT).isEmpty())
			comment = modelElement.getAttributeValue(ATT_COMMENT);
		if (modelElement.getAttributeValue(ATT_QSCORE) != null && !modelElement.getAttributeValue(ATT_QSCORE).isEmpty())
			qualityScore = Integer.valueOf(modelElement.getAttributeValue(ATT_QSCORE));
		if (modelElement.getAttributeValue(ATT_RSS) != null)
			rss = Double.valueOf(modelElement.getAttributeValue(ATT_RSS));
		if (modelElement.getAttributeValue(ATT_RMS) != null)
			rms = Double.valueOf(modelElement.getAttributeValue(ATT_RMS));
		if (modelElement.getAttributeValue(ATT_AIC) != null)
			aic = Double.valueOf(modelElement.getAttributeValue(ATT_AIC));
		if (modelElement.getAttributeValue(ATT_BIC) != null)
			bic = Double.valueOf(modelElement.getAttributeValue(ATT_BIC));
		rsquared = Double.valueOf(modelElement.getAttributeValue(ATT_RSQUARED));
		condId = Integer.valueOf(modelElement.getAttributeValue(ATT_CONDID));

		HashMap<String, String> rMap = new HashMap<>();
		for (Element el : modelElement.getChildren()) {
			if (el.getName().equals(ATT_FORMULA)) {
				formula = el.getText();
			} else if (el.getName().equals(ELEMENT_PARAM)) {
				boolean minNull = el.getAttributeValue(ATT_MINVALUE) == null
						|| el.getAttributeValue(ATT_MINVALUE).equals("null");
				boolean maxNull = el.getAttributeValue(ATT_MAXVALUE) == null
						|| el.getAttributeValue(ATT_MAXVALUE).equals("null");
				boolean valNull = el.getAttributeValue(ATT_VALUE) == null
						|| el.getAttributeValue(ATT_VALUE).equals("null");
				boolean errNull = el.getAttributeValue(ATT_PARAMERR) == null
						|| el.getAttributeValue(ATT_PARAMERR).equals("null");
				boolean categoryNull = el.getAttributeValue("Category") == null
						|| el.getAttributeValue("Category").equals("null");
				boolean unitNull = el.getAttributeValue("Unit") == null || el.getAttributeValue("Unit").equals("null");

				Double min = minNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MINVALUE));
				Double max = maxNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MAXVALUE));
				Double val = valNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_VALUE));
				Double err = errNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_PARAMERR));
				String category = categoryNull ? null : el.getAttributeValue("Category");
				String unit = unitNull ? null : el.getAttributeValue("Unit");

				ParamXml px = new ParamXml(el.getAttributeValue(ATT_PARAMNAME), XmlHelper.getBoolean(el, ATT_IS_START),
						val, err, min, max, null, null, category, unit);
				parameter.add(px);
			} else if (el.getName().equals(ATT_INDEPVAR)) {
				boolean minNull = el.getAttributeValue(ATT_MININDEP) == null
						|| el.getAttributeValue(ATT_MININDEP).equals("null");
				boolean maxNull = el.getAttributeValue(ATT_MAXINDEP) == null
						|| el.getAttributeValue(ATT_MAXINDEP).equals("null");
				boolean categoryNull = el.getAttributeValue("Category") == null
						|| el.getAttributeValue("Category").equals("null");
				boolean unitNull = el.getAttributeValue("Unit") == null || el.getAttributeValue("Unit").equals("null");
				Double min = minNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MININDEP));
				Double max = maxNull ? Double.NaN : Double.valueOf(el.getAttributeValue(ATT_MAXINDEP));
				String category = categoryNull ? null : el.getAttributeValue("Category");
				String unit = unitNull ? null : el.getAttributeValue("Unit");
				IndepXml ix = new IndepXml(el.getAttributeValue(ATT_PARAMNAME), min, max, category, unit);
				independent.add(ix);
			} else if (el.getName().equals(ATT_RMAP)) {
				rMap.put(el.getAttributeValue("NEW"), el.getAttributeValue("OLD"));
			} else if (el.getName().equals(ATT_DEPVAR)) {
				depXml = new DepXml(el.getAttributeValue(ATT_PARAMNAME), el.getAttributeValue("Category"),
						el.getAttributeValue("Unit"));
			} else if (el.getName().equals(ATT_MLIT)) {
				try {
					modelLit = new PmmXmlDoc(el.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (el.getName().equals(ATT_EMLIT)) {
				try {
					estLit = new PmmXmlDoc(el.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (el.getName().equals(ATT_INDEP)) {
				try {
					independent = new PmmXmlDoc(el.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (el.getName().equals(ATT_DEP)) {
				try {
					PmmXmlDoc dep = new PmmXmlDoc(el.getText());
					if (dep.size() > 0)
						depXml = (DepXml) dep.get(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (el.getName().equals(ATT_PARAM)) {
				try {
					parameter = new PmmXmlDoc(el.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				assert false;
			}
		}
		for (String name : rMap.keySet()) {
			String origName = rMap.get(name);
			if (depXml.origName.equals(origName)) {
				depXml.name = name;
			} else {
				for (PmmXmlElementConvertable el : parameter.getElementSet()) {
					if (el instanceof ParamXml) {
						ParamXml px = (ParamXml) el;
						if (px.origName.equals(origName)) {
							px.name = name;
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

	// depXml
	public DepXml getDepXml() {
		return depXml;
	}

	public void setDepXml(DepXml depXml) {
		this.depXml = depXml;
	}

	public void setDepVar(final String depVar) {
		setDepVar(depVar, false);
	}

	public void setDepVar(final String depVar, boolean origNameAlso) {
		if (depXml == null)
			depXml = new DepXml(depVar);
		else {
			depXml.name = depVar;
			if (origNameAlso)
				depXml.origName = depVar;
		}
	}

	public String getDepDescription() {
		return depXml == null ? null : depXml.description;
	}

	public void setDepDescription(String description) {
		if (depXml != null)
			depXml.description = description;
	}

	public String getDepCategory() {
		return depXml == null ? null : depXml.category;
	}

	public void setDepCategory(String category) {
		if (depXml != null)
			depXml.category = category;
	}

	public String getDepUnit() {
		return depXml == null ? null : depXml.unit;
	}

	public void setDepUnit(String unit) {
		if (depXml != null)
			depXml.unit = unit;
	}

	// independent
	public PmmXmlDoc getIndependent() {
		return independent;
	}

	public void setIndependent(PmmXmlDoc independent) {
		this.independent = independent;
	}

	public boolean containsIndep(final String name) {
		return findIndep(name) != null;
	}

	public void addIndepVar(final String varName) {
		addIndepVar(varName, Double.NaN, Double.NaN);
	}

	public void addIndepVar(final String varName, final Double min, final Double max) {
		addIndepVar(varName, min, max, null, null, null);
	}

	public void addIndepVar(final String varName, final Double min, final Double max, String category, String unit,
			String description) {
		IndepXml ix = new IndepXml(varName, min, max, category, unit);
		if (description != null)
			ix.description = description;
		independent.add(ix);
	}

	private IndepXml findIndep(final String name) {
		for (PmmXmlElementConvertable el : independent.getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml indep = (IndepXml) el;
				if (indep.name.equals(name)) {
					return indep;
				}
			}
		}
		return null;
	}

	public String getIndepDescription(final String name) {
		IndepXml indep = findIndep(name);
		return indep == null ? null : indep.description;
	}

	public void setIndepDescription(final String name, final String description) {
		IndepXml indep = findIndep(name);
		if (indep != null)
			indep.description = description;
	}

	public Double getIndepMax(final String name) {
		IndepXml indep = findIndep(name);
		return indep == null ? null : indep.max;
	}

	public void setIndepMax(final String name, final Double max) {
		IndepXml indep = findIndep(name);
		if (indep != null)
			indep.max = max;
	}

	public Double getIndepMin(final String name) {
		IndepXml indep = findIndep(name);
		return indep == null ? null : indep.min;
	}

	public void setIndepMin(final String name, final Double min) {
		IndepXml indep = findIndep(name);
		if (indep != null)
			indep.min = min;
	}

	public String getIndepUnit(final String name) {
		IndepXml indep = findIndep(name);
		return indep == null ? null : indep.unit;
	}

	public void setIndepUnit(final String name, final String unit) {
		IndepXml indep = findIndep(name);
		if (indep != null)
			indep.unit = unit;
	}

	public String getIndepCategory(final String name) {
		IndepXml indep = findIndep(name);
		return indep == null ? null : indep.category;
	}

	public void setIndepCategory(final String name, final String category) {
		IndepXml indep = findIndep(name);
		if (indep != null)
			indep.category = category;
	}

	// other

	// parameter
	public PmmXmlDoc getParameter() {
		return parameter;
	}

	public void setParameter(PmmXmlDoc parameter) {
		this.parameter = parameter;
	}

	public void addParam(final String paramName, final Boolean isStartParam) {
		addParam(paramName, isStartParam, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
	}

	public void addParam(final String paramName, final Boolean isStartParam, final Double value, final Double error) {
		addParam(paramName, isStartParam, value, error, Double.NaN, Double.NaN);
	}

	public void addParam(final String paramName, final Boolean isStartParam, final Double value, final Double error,
			final Double min, final Double max) {
		addParam(paramName, isStartParam, value, error, min, max, null, null, null);
	}

	public void addParam(final String paramName, final Boolean isStartParam, final Double value, final Double error,
			final Double min, final Double max, String category, String unit, String description) {
		ParamXml px = new ParamXml(paramName, isStartParam, value, error, min, max, null, null, category, unit);
		if (description != null)
			px.description = description;
		parameter.add(px);
	}

	private ParamXml findParam(final String name) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.name.equals(name)) {
					return px;
				}
			}
		}
		return null;
	}

	public Double getParamMin(final String name) {
		ParamXml param = findParam(name);
		return param == null ? null : param.min;
	}

	public void setParamMin(final String name, final Double min) {
		ParamXml param = findParam(name);
		if (param != null)
			param.min = min;
	}

	public Double getParamMax(final String name) {
		ParamXml param = findParam(name);
		return param == null ? null : param.max;
	}

	public void setParamMax(final String name, final Double max) {
		ParamXml param = findParam(name);
		if (param != null)
			param.max = max;
	}

	public String getParamUnit(final String name) {
		ParamXml param = findParam(name);
		return param == null ? null : param.unit;
	}

	public void setParamUnit(final String name, final String unit) {
		ParamXml param = findParam(name);
		if (param != null)
			param.unit = unit;
	}

	public String getParamCategory(final String name) {
		ParamXml param = findParam(name);
		return param == null ? null : param.category;
	}

	public void setParamCategory(final String name, final String category) {
		ParamXml param = findParam(name);
		if (param != null)
			param.category = category;
	}

	public String getParamDescription(final String name) {
		ParamXml param = findParam(name);
		return param == null ? null : param.description;
	}

	public void setParamDescription(final String name, final String description) {
		ParamXml param = findParam(name);
		if (param != null)
			param.description = description;
	}

	public Boolean getParamIsStart(final String paramName) {
		ParamXml param = findParam(paramName);
		return param == null ? null : param.isStartParam;
	}

	public void setParamIsStart(final String name, final Boolean isStart) {
		ParamXml param = findParam(name);
		if (param != null)
			param.isStartParam = isStart;
	}

	public Double getParamValue(final String paramName) {
		ParamXml param = findParam(paramName);
		return param == null ? null : param.value;
	}

	public void setParamValue(final String name, final Double value) {
		ParamXml param = findParam(name);
		if (param != null)
			param.value = value;
	}

	public Double getParamError(final String paramName) {
		ParamXml param = findParam(paramName);
		return param == null ? null : param.error;
	}

	public void setParamError(final String name, final Double error) {
		ParamXml param = findParam(name);
		if (param != null)
			param.error = error;
	}

	// estLit
	public PmmXmlDoc getEstModelLit() {
		return estLit;
	}

	public void setEstLit(PmmXmlDoc estLit) {
		this.estLit = estLit;
	}

	public void addEstModelLit(final LiteratureItem item) {
		estLit.add(item);
	}

	public void removeEstModelLits() {
		estLit = new PmmXmlDoc();
	}

	// modelLit
	public PmmXmlDoc getModelLit() {
		return modelLit;
	}

	public void setMLit(PmmXmlDoc modelLit) {
		this.modelLit = modelLit;
	}

	public void removeModelLits() {
		modelLit = new PmmXmlDoc();
	}

	public void addModelLit(final LiteratureItem item) {
		modelLit.add(item);
	}

	// AIC
	public Double getAic() {
		return aic;
	}

	public void setAic(final Double aic) {
		this.aic = (aic == null) ? Double.NaN : aic;
	}

	public boolean hasAic() {
		return !Double.isNaN(aic);
	}

	// BIC
	public Double getBic() {
		return bic;
	}

	public void setBic(final Double bic) {
		this.bic = (bic == null) ? Double.NaN : bic;
	}

	public boolean hasBic() {
		return !Double.isNaN(bic);
	}

	// RMS
	public Double getRms() {
		return rms;
	}

	public void setRms(final Double rms) throws PmmException {
		if (rms == null)
			this.rms = Double.NaN;
		else {
			if (Double.isInfinite(rms)) {
				throw new PmmException("RMS must be a real positive number.");
			}

			if (rms < 0) {
				throw new PmmException("RMS must be a real positive number.");
			}

			this.rms = rms;
		}
	}

	public boolean hasRms() {
		return !Double.isNaN(rms);
	}

	// rss
	public Double getRss() {
		return rss;
	}

	public void setRss(final Double rss) throws PmmException {
		if (rss == null)
			this.rss = Double.NaN;
		else {
			if (Double.isInfinite(rss)) {
				throw new PmmException("RSS must be a real positive number.");
			}

			if (rss < 0) {
				throw new PmmException("RSS must be a real positive number.");
			}

			this.rss = rss;
		}
	}

	// rsquared
	public Double getRsquared() {
		return rsquared;
	}

	public void setRsquared(final Double r2) throws PmmException {
		if (r2 == null)
			this.rsquared = Double.NaN;
		else {
			if (r2 > 1) {
				throw new PmmException("Rsquared must not exceed 1: " + r2);
			}
			rsquared = r2;
		}
	}

	// formula
	public String revertFormula() {
		String result = formula;
		if (depXml != null && !depXml.name.equals(depXml.origName)) {
			result = MathUtilities.replaceVariable(result, depXml.name, depXml.origName);
		}
		if (parameter != null && parameter.getElementSet() != null) {
			for (PmmXmlElementConvertable el : parameter.getElementSet()) {
				if (el instanceof ParamXml) {
					ParamXml px = (ParamXml) el;
					if (!px.name.equals(px.origName)) {
						result = MathUtilities.replaceVariable(result, px.name, px.origName);
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

	public void setFormula(final String formula) {

		this.formula = formula.replaceAll("~", "=");
		this.formula = this.formula.replaceAll("\\s", "");
	}

	public String getFormula() {
		return formula;
	}

	// level
	public int getLevel() {
		return level;
	}

	// other ...
	public PmmXmlDoc getCatModel() {
		PmmXmlDoc catModel = new PmmXmlDoc();
		CatalogModelXml cmx = new CatalogModelXml(modelId, modelName, getFormula(), modelClass, modelDbUuid);
		catModel.add(cmx);
		return catModel;
	}

	public PmmXmlDoc getEstModel() {
		PmmXmlDoc emDoc = new PmmXmlDoc();
		EstModelXml emx = new EstModelXml(estModelId, fittedModelName, null, getRms(), getRsquared(), getAic(),
				getBic(), null, isChecked, qualityScore, estimatedModelDbUuid);
		emx.comment = comment;
		emDoc.add(emx);
		return emDoc;
	}

	public ParametricModel clone() {
		ParametricModel clonedPM = new ParametricModel(modelName, formula, depXml, level, modelId, estModelId);
		clonedPM.modelClass = modelClass;
		clonedPM.modelDbUuid = modelDbUuid;
		clonedPM.estimatedModelDbUuid = estimatedModelDbUuid;

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
		clonedPM.condId = condId;
		clonedPM.isChecked = isChecked;
		clonedPM.comment = comment;
		clonedPM.qualityScore = qualityScore;
		clonedPM.fittedModelName = fittedModelName;

		if (depXml != null)
			clonedPM.setDepXml(new DepXml(depXml.toXmlElement()));
		try {
			if (independent != null)
				clonedPM.setIndependent(new PmmXmlDoc(independent.toXmlString()));
			if (parameter != null)
				clonedPM.setParameter(new PmmXmlDoc(parameter.toXmlString()));
			if (estLit != null)
				clonedPM.setEstLit(new PmmXmlDoc(estLit.toXmlString()));
			if (modelLit != null)
				clonedPM.setMLit(new PmmXmlDoc(modelLit.toXmlString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		clonedPM.globalModelId = globalModelId;

		return clonedPM;
	}

	public void removeIndepVar(final String varName) {
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

	public void removeParam(final String varName) {
		for (PmmXmlElementConvertable el : parameter.getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.name.equals(varName)) {
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

	public void removeParams() {
		parameter = new PmmXmlDoc();
	}

	public void removeIndepVars() {
		independent = new PmmXmlDoc();
	}

	public Double getParamP(final String paramName) {
		ParamXml param = findParam(paramName);
		return param == null ? null : param.P;
	}

	public Double getParamT(final String paramName) {
		ParamXml param = findParam(paramName);
		return param == null ? null : param.t;
	}

	public boolean containsParam(final String name) {
		return findParam(name) != null;
	}

	public SortedMap<String, Boolean> getAllParVars() {
		SortedMap<String, Boolean> result = new TreeMap<>();
		if (parameter != null && parameter.getElementSet() != null) {
			for (PmmXmlElementConvertable el : parameter.getElementSet()) {
				if (el instanceof ParamXml) {
					ParamXml px = (ParamXml) el;
					result.put(px.name, false);
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

	public String getDepVar() {
		return depXml == null ? null : depXml.name;
	}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_PARAMETRICMODEL);
		modelElement.setAttribute(ATT_MDBUUID, modelDbUuid == null ? "" : modelDbUuid);
		modelElement.setAttribute(ATT_EMDBUUID, estimatedModelDbUuid == null ? "" : estimatedModelDbUuid);
		modelElement.setAttribute(ATT_MODELNAME, modelName == null ? "" : modelName);
		modelElement.setAttribute(ATT_MODELCLASS, XmlHelper.getNonNull(modelClass));
		modelElement.setAttribute("FittedModelName", fittedModelName == null ? "" : fittedModelName);
		modelElement.setAttribute(ATT_LEVEL, String.valueOf(level));
		modelElement.setAttribute(ATT_MODELID, String.valueOf(modelId));
		modelElement.setAttribute(ATT_ESTMODELID, String.valueOf(estModelId));
		modelElement.setAttribute(ATT_GLOBALMODELID, String.valueOf(globalModelId));
		modelElement.setAttribute(ATT_CONDID, String.valueOf(condId));
		modelElement.setAttribute(ATT_RSS, String.valueOf(rss));
		modelElement.setAttribute(ATT_RMS, String.valueOf(rms));
		modelElement.setAttribute(ATT_AIC, String.valueOf(aic));
		modelElement.setAttribute(ATT_BIC, String.valueOf(bic));
		modelElement.setAttribute(ATT_CHECKED, isChecked == null ? "" : String.valueOf(isChecked));
		modelElement.setAttribute(ATT_COMMENT, comment == null ? "" : comment);
		modelElement.setAttribute(ATT_QSCORE, qualityScore == null ? "" : String.valueOf(qualityScore));

		modelElement.setAttribute(ATT_RSQUARED, String.valueOf(rsquared));

		Element element = new Element(ATT_FORMULA);
		element.addContent(formula);
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

	public KnimeTuple getKnimeTuple() throws PmmException {
		KnimeTuple tuple;

		if (level == 1) {

			tuple = new KnimeTuple(new Model1Schema());

			PmmXmlDoc cmDoc = getCatModel();// new PmmXmlDoc();
			tuple.setValue(Model1Schema.ATT_MODELCATALOG, cmDoc);

			// tuple.setValue( Model1Schema.ATT_FORMULA, getFormula() );
			PmmXmlDoc depDoc = new PmmXmlDoc();
			depDoc.add(depXml == null ? new DepXml(getDepVar(), getDepCategory(), getDepUnit()) : depXml);
			tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);
			// tuple.setValue( Model1Schema.ATT_MODELNAME, getModelName() );
			// tuple.setValue( Model1Schema.ATT_MODELID, getModelId() );
			// tuple.setValue( Model1Schema.ATT_ESTMODELID, getEstModelId() );
			// tuple.setValue( Model1Schema.ATT_RMS, getRms() );
			// tuple.setValue( Model1Schema.ATT_AIC, getAic() );
			// tuple.setValue( Model1Schema.ATT_BIC, getBic() );
			// tuple.setValue( Model1Schema.ATT_RSQUARED, getRsquared() );

			PmmXmlDoc emDoc = getEstModel();// new PmmXmlDoc();
			tuple.setValue(Model1Schema.ATT_ESTMODEL, emDoc);

			tuple.setValue(Model1Schema.ATT_PARAMETER, parameter);

			tuple.setValue(Model1Schema.ATT_INDEPENDENT, independent);

			tuple.setValue(Model1Schema.ATT_MLIT, modelLit);
			tuple.setValue(Model1Schema.ATT_EMLIT, estLit);
		} else {

			tuple = new KnimeTuple(new Model2Schema());

			PmmXmlDoc cmDoc = getCatModel();// new PmmXmlDoc();
			tuple.setValue(Model2Schema.ATT_MODELCATALOG, cmDoc);

			// tuple.setValue( Model2Schema.ATT_FORMULA, getFormula() );

			PmmXmlDoc depDoc = new PmmXmlDoc();
			depDoc.add(depXml == null ? new DepXml(getDepVar(), getDepCategory(), getDepUnit()) : depXml);
			tuple.setValue(Model2Schema.ATT_DEPENDENT, depDoc);
			/*
			 * tuple.setValue( Model2Schema.ATT_MODELNAME, getModelName() ); tuple.setValue(
			 * Model2Schema.ATT_MODELID, getModelId() ); tuple.setValue(
			 * Model2Schema.ATT_ESTMODELID, getEstModelId() ); tuple.setValue(
			 * Model2Schema.ATT_RMS, getRms() ); tuple.setValue( Model2Schema.ATT_AIC,
			 * getAic() ); tuple.setValue( Model2Schema.ATT_BIC, getBic() ); tuple.setValue(
			 * Model2Schema.ATT_RSQUARED, getRsquared() );
			 */
			PmmXmlDoc emDoc = getEstModel();// new PmmXmlDoc();
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
