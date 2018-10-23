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
package de.bund.bfr.knime.pmm.estimatedmodelreader;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.config.Config;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DBUtilities;
import de.bund.bfr.knime.pmm.common.DbIo;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.ModelReaderUi;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;
import de.bund.bfr.knime.pmm.predictorview.SettingsHelper;
import de.bund.bfr.knime.pmm.timeseriesreader.MdReaderUi;

/**
 * This is the model implementation of EstimatedModelReader.
 * 
 *
 * @author Jorgen Brandt
 */
public class EstimatedModelReaderNodeModel extends NodeModel {
	/*
	static final String PARAM_FILENAME = "filename";
	static final String PARAM_LOGIN = "login";
	static final String PARAM_PASSWD = "passwd";
	static final String PARAM_OVERRIDE = "override";
	*/
	static final String PARAM_MATRIXSTRING = "matrixString";
	static final String PARAM_AGENTSTRING = "agentString";
	static final String PARAM_LITERATURESTRING = "literatureString";
	static final String PARAM_MATRIXID = "matrixID";
	static final String PARAM_AGENTID = "agentID";
	static final String PARAM_LITERATUREID = "literatureID";
	static final String PARAM_LEVEL = "level";
	static final String PARAM_MODELCLASS = "modelClass";
	static final String PARAM_MODELFILTERENABLED = "modelFilterEnabled";
	static final String PARAM_MODELLISTINT = "modelList";

	static final String PARAM_PARAMETERS = "parameters";
	static final String PARAM_PARAMETERNAME = "parameterName";
	static final String PARAM_PARAMETERMIN = "parameterMin";
	static final String PARAM_PARAMETERMAX = "parameterMax";
/*
	private String filename;
	private String login;
	private String passwd;
	private boolean override;
	*/
	private int level;
	private String modelClass;
	private boolean modelFilterEnabled;
	private int[] modelList;
	private int qualityMode;
	private double qualityThresh;
	private String matrixString;
	private String agentString;
	private String literatureString;
	private int matrixID, agentID, literatureID;
	private boolean withoutMdData;
	private SettingsHelper set;
	
	private static Config estModelReaderUiSettings;
	
	private LinkedHashMap<String, Double[]> parameter;
	
	private Connection conn = null;

	static final String PARAM_QUALITYMODE = "qualityFilterMode";
	static final String PARAM_QUALITYTHRESH = "qualityThreshold";
	
	/**
     * Constructor for the node model.
     */
    protected EstimatedModelReaderNodeModel() {
    	
        super(0, 1);
        /*
        filename = "";
        login = "";
        passwd = "";
        */
        level = 1;
        modelFilterEnabled = false;
        modelList = null;
        qualityThresh = .8;
        qualityMode = EmReaderUi.MODE_OFF;
        agentString = "";
        matrixString = "";
        literatureString = "";

        parameter = new LinkedHashMap<>();
        set = new SettingsHelper();
    }

    public static List<KnimeTuple> getKnimeTuples(Bfrdb db, Connection conn, KnimeSchema schema, int level, boolean withoutMdData, EstimatedModelReaderNodeModel emrnm) throws SQLException {
    	return getKnimeTuples(db, conn, schema, level, withoutMdData, null, emrnm);
    }
    public static List<KnimeTuple> getKnimeTuples(Bfrdb db, Connection conn, KnimeSchema schema, int level, boolean withoutMdData, String where, EstimatedModelReaderNodeModel emrnm) throws SQLException {
    	return getKnimeTuples(db, conn, schema, level, withoutMdData, -1, 0, "", "", "", -1, -1, -1, null, false, null, where, emrnm);
    }    
	public static List<KnimeTuple> getKnimeTuples(Bfrdb db, Connection conn, KnimeSchema schema,
    		int level, boolean withoutMdData, int qualityMode, double qualityThresh,
    		String matrixString, String agentString, String literatureString, int matrixID, int agentID, int literatureID, LinkedHashMap<String, Double[]> parameter,
    		boolean modelFilterEnabled, int[] modelList, String where, EstimatedModelReaderNodeModel emrnm) throws SQLException {
    	
    	if (emrnm != null) {
         	try {
             	EmReaderUi emrui = new EmReaderUi();                 	
             	emrui.setSettings(estModelReaderUiSettings);
            	SettingsHelper set = emrui.getSet();
            	List <KnimeTuple> kts = level == 1 ? set.getSelectedTuples() : set.getSelectedOldTuples();
            	if (kts.size() > 0) return kts;
    		}
         	catch (InvalidSettingsException e1) {}
    	}

    	List<KnimeTuple> resultSet = new ArrayList<>(); 

     	String dbuuid = db.getDBUUID();
    	ResultSet result = null;
    	if (where != null)	result = (level == 1 ? db.selectEstModel(1, -1, where, false) : level == 2 ? db.selectEstModel(2, -1, where, false) : db.selectEstModel(3, -1, where, false));
    	else result = (level == 1 ? db.selectEstModel(1) : level == 2 ? db.selectEstModel(2) : db.selectEstModel(3));
    	
    	while (result.next()) {
			String addWarningMsg = "";
    		
    		// initialize row
    		KnimeTuple tuple = new KnimeTuple(schema);
    		    	
    		if (!withoutMdData && level < 3) {
        		// fill ts
    			int condID = result.getInt(Bfrdb.ATT_CONDITIONID);
        		tuple.setValue(TimeSeriesSchema.ATT_CONDID, condID);
        		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, result.getString("CombaseID"));    			

        		//PmmXmlDoc miscDoc = null; miscDoc = db.getMiscXmlDoc(result);
        		PmmXmlDoc miscDoc = DbIo.convertArrays2MiscXmlDoc(result.getArray("SonstigesID"), result.getArray("Parameter"),
        				result.getArray("Beschreibung"), result.getArray("SonstigesWert"), result.getArray("Einheit"), dbuuid);
        		if (result.getObject(Bfrdb.ATT_TEMPERATURE) != null) {
            		double dbl = result.getDouble(Bfrdb.ATT_TEMPERATURE);
        			MiscXml mx = new MiscXml(AttributeUtilities.ATT_TEMPERATURE_ID,AttributeUtilities.ATT_TEMPERATURE,AttributeUtilities.ATT_TEMPERATURE,dbl,Arrays.asList(Categories.getTempCategory().getName()),Categories.getTempCategory().getStandardUnit(), dbuuid);//"°C"); // Categories.getTempCategory().getStandardUnit()
        			miscDoc.add(mx);
        		}
        		if (result.getObject(Bfrdb.ATT_PH) != null) {
        			double dbl = result.getDouble(Bfrdb.ATT_PH);
        			MiscXml mx = new MiscXml(AttributeUtilities.ATT_PH_ID,AttributeUtilities.ATT_PH,AttributeUtilities.ATT_PH,dbl,Arrays.asList(Categories.getPhCategory().getName()),Categories.getPhCategory().getAllUnits().toArray(new String[0])[0], dbuuid);
        			miscDoc.add(mx);
        		}
        		if (result.getObject(Bfrdb.ATT_AW) != null) {
        			double dbl = result.getDouble(Bfrdb.ATT_AW);
        			MiscXml mx = new MiscXml(AttributeUtilities.ATT_AW_ID,AttributeUtilities.ATT_AW,AttributeUtilities.ATT_AW,dbl,Arrays.asList(Categories.getAwCategory().getName()),Categories.getAwCategory().getAllUnits().toArray(new String[0])[1], dbuuid);
        			miscDoc.add(mx);
        		}
				if (result.getObject(Bfrdb.ATT_PRESSURE) != null) {
					double dbl = result.getDouble(Bfrdb.ATT_PRESSURE);
					MiscXml mx = new MiscXml(AttributeUtilities.ATT_PRESSURE_ID, AttributeUtilities.ATT_PRESSURE, AttributeUtilities.ATT_PRESSURE, dbl,
							null, "bar", dbuuid);
					miscDoc.add(mx);
				}
        		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);

        		PmmXmlDoc matDoc = new PmmXmlDoc(); 
    			MatrixXml mx = new MatrixXml(result.getInt(Bfrdb.ATT_MATRIXID), result.getString(Bfrdb.ATT_MATRIXNAME), result.getString(Bfrdb.ATT_MATRIXDETAIL), dbuuid);
    			matDoc.add(mx);
    			tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matDoc);
        		PmmXmlDoc agtDoc = new PmmXmlDoc(); 
    			AgentXml ax = new AgentXml(result.getInt(Bfrdb.ATT_AGENTID), result.getString(Bfrdb.ATT_AGENTNAME), result.getString(Bfrdb.ATT_AGENTDETAIL), dbuuid);
    			agtDoc.add(ax);
    			tuple.setValue(TimeSeriesSchema.ATT_AGENT, agtDoc);

    			String modelUnit = result.getString("DepUnit");
    			Array a = result.getArray("IndepUnit");
				Object[] timeUnits = (a == null) ? null : (Object[])a.getArray();
    			String timeUnit = (timeUnits != null && timeUnits.length > 0 ? timeUnits[0].toString() : null);
        		PmmXmlDoc tsDoc = DbIo.convertStringLists2TSXmlDoc(result.getArray("Zeit"), result.getArray("ZeitEinheitDV"),
        				result.getArray("Konzentration"), result.getArray("KonzentrationsEinheit"), result.getArray("KonzentrationsObjectType"),
        				result.getArray("Standardabweichung"), result.getArray("Wiederholungen"), modelUnit, timeUnit);
        		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, tsDoc);
        		//tuple.setValue(TimeSeriesSchema.ATT_COMMENT, result.getString( Bfrdb.ATT_COMMENT));
        		PmmXmlDoc mdInfoDoc = new PmmXmlDoc();
        		Boolean checked = null;
        		Integer qualityScore = null;
    			if (result.getObject("MDGeprueft") != null) checked = result.getBoolean("MDGeprueft");
    			if (result.getObject("MDGuetescore") != null) qualityScore = result.getInt("MDGuetescore");
        		MdInfoXml mdix = new MdInfoXml(condID, "i"+condID, result.getString("MDKommentar"), qualityScore, checked);
        		mdInfoDoc.add(mdix);
        		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);
        		
        		String s = result.getString(Bfrdb.ATT_LITERATUREID);
        		if (s != null) {
        			PmmXmlDoc l = new PmmXmlDoc();        			
        			LiteratureItem li = DBUtilities.getLiteratureItem(conn, Integer.valueOf(s), dbuuid);
        			l.add(li);
    				tuple.setValue(TimeSeriesSchema.ATT_LITMD,l);
    			}

        		tuple.setValue( TimeSeriesSchema.ATT_DBUUID, dbuuid );
    		}
    		
			PmmXmlDoc cmDoc;
			CatalogModelXml cmx;
			Object cls;
    		DepXml dx;
    		PmmXmlDoc depDoc;
    		Object[] da;
    		Array a;
    		String dep;
    		EstModelXml emx;
	    	Double rms = null;
	    	Double r2 = null;
	    	Double aic = null;
	    	Double bic = null;
	    	PmmXmlDoc emDoc;
	    	int emid;
	    	String s;
	    	String formula;
	    	LinkedHashMap<String, String> varMap;
    		if (level < 3) {
    		
	    		// fill m1
	    		formula = result.getString("Formel");
	    		if (formula != null) formula = formula.replaceAll( "~", "=" ).replaceAll( "\\s", "" );
	
	    		// Time=t,Log10C=LOG10N
	    		varMap = DbIo.getVarParMap(result.getString(Bfrdb.ATT_VARMAPTO));
	    		//varMap.put(AttributeUtilities.TIME, "t"); varMap.put(AttributeUtilities.CONCENTRATION, "LOG10N");
	    		//varMap.put("t", AttributeUtilities.TIME); varMap.put("LOG10N", AttributeUtilities.CONCENTRATION);
	
	    		for (String to : varMap.keySet())	{
	    			formula = MathUtilities.replaceVariable(formula, varMap.get(to), to);
	    		}
				if (!varMap.containsKey(AttributeUtilities.TIME)) formula = MathUtilities.replaceVariable(formula, "t", AttributeUtilities.TIME);
				if (!varMap.containsKey(AttributeUtilities.CONCENTRATION)) formula = MathUtilities.replaceVariable(formula, "LOG10N", AttributeUtilities.CONCENTRATION);
    		
				cmDoc = new PmmXmlDoc();
				cmx = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), formula, null, dbuuid); 
	    		cls = DBKernel.getValue(conn,"Modellkatalog", "ID", result.getInt(Bfrdb.ATT_MODELID)+"", "Klasse");
	    		cmx.modelClass = (Integer) cls;
				cmDoc.add(cmx);
				tuple.setValue(Model1Schema.ATT_MODELCATALOG, cmDoc);

	    		depDoc = new PmmXmlDoc();
	    		dep = result.getString(Bfrdb.ATT_DEP);
				if (!varMap.containsKey(AttributeUtilities.CONCENTRATION) && dep.equals("LOG10N")) dep = AttributeUtilities.CONCENTRATION;
	    		if (varMap.containsKey(dep)) {
	    			dx = new DepXml(varMap.get(dep), result.getString("DepCategory"), result.getString("DepUnit"));
	    			dx.name = dep;
	    		}
	    		else {
	    			dx = new DepXml(dep, result.getString("DepCategory"), result.getString("DepUnit"));
	    		}
	    		a = result.getArray("DepDescription");
	    		da = (Object[])a.getArray();
				if (da != null && da[0] != null) dx.description = da[0].toString();
	    		depDoc.add(dx);
	    		tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);
				if (emrnm != null && (dx.unit == null || dx.unit.isEmpty())) addWarningMsg += "\nUnit not defined for dependant variable '" + dx.name + "' in model with ID " + cmx.id + "!";
	    		
	    		emid = result.getInt(Bfrdb.ATT_ESTMODELID);
				emDoc = new PmmXmlDoc();
				
				if (result.getObject(Bfrdb.ATT_RMS) != null) rms = result.getDouble(Bfrdb.ATT_RMS);
				if (result.getObject(Bfrdb.ATT_RSQUARED) != null) r2 = result.getDouble(Bfrdb.ATT_RSQUARED);
				if (result.getObject("AIC") != null) aic = result.getDouble("AIC");
				if (result.getObject("BIC") != null) bic = result.getDouble("BIC");
							
				emx = new EstModelXml(emid, result.getString("FittedModelName"), null, rms, r2, aic, bic, null); // "EM_" + emid
				emx.dbuuid = dbuuid;
				if (result.getObject("Geprueft") != null) emx.checked = result.getBoolean("Geprueft");
				if (result.getObject("Guetescore") != null) emx.qualityScore = result.getInt("Guetescore");
				if (result.getObject("Kommentar") != null) emx.comment = result.getString("Kommentar");
				/*
	    		Object cmt = DBKernel.getValue(conn,"GeschaetzteModelle", "ID", ""+emid, "Kommentar");
	    		if (cmt != null) emx.setComment((String) cmt);
	*/
				emDoc.add(emx);
				tuple.setValue(Model1Schema.ATT_ESTMODEL, emDoc);

				PmmXmlDoc ixml = DbIo.convertArrays2IndepXmlDoc(varMap, result.getArray(Bfrdb.ATT_INDEP),
	    				result.getArray(Bfrdb.ATT_MININDEP), result.getArray(Bfrdb.ATT_MAXINDEP), result.getArray("IndepCategory"),
	    				result.getArray("IndepUnit"), result.getArray("IndepDescription"), true);
	    		tuple.setValue(Model1Schema.ATT_INDEPENDENT, ixml);
				if (emrnm != null && !ixml.getWarning().isEmpty()) addWarningMsg += "\n" + ixml.getWarning() + "in model with ID " + cmx.id + "!";

				tuple.setValue(Model1Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(varMap, result.getArray(Bfrdb.ATT_PARAMNAME),
	    				result.getArray(Bfrdb.ATT_VALUE), result.getArray("ZeitEinheit"), null, result.getArray("Einheiten"), result.getArray("StandardError"), result.getArray(Bfrdb.ATT_MIN),
	    				result.getArray(Bfrdb.ATT_MAX), result.getArray("ParamDescription"), result.getArray(Bfrdb.ATT_PARAMTYPE), result.getArray("ParamP"), result.getArray("Paramt"), cmx.id, emid));
	    		
	    		s = result.getString("LitMID");
	    		if (s != null) tuple.setValue(Model1Schema.ATT_MLIT, getLiterature(conn, s, dbuuid));
	    		s = result.getString("LitEmID");
	    		if (s != null) tuple.setValue(Model1Schema.ATT_EMLIT, getLiterature(conn, s, dbuuid));
	    		
	    		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, withoutMdData ? Model1Schema.NOTWRITABLE : Model1Schema.WRITABLE);
	    		tuple.setValue(Model1Schema.ATT_DBUUID, dbuuid);
			}
    		
    		// fill m2
    		if (level >= 2) {
	    		formula = result.getString("Formel2");
	    		if (formula != null) {
					formula = formula.replaceAll("~", "=").replaceAll("\\s", "");
				}
        		varMap = DbIo.getVarParMap(result.getString(Bfrdb.ATT_VARMAPTO+"2"));
        		for (String to : varMap.keySet())	{
        			formula = MathUtilities.replaceVariable(formula, varMap.get(to), to);
        		}

    			cmDoc = new PmmXmlDoc();
    			cmx = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID+"2"), result.getString(Bfrdb.ATT_NAME+"2"), formula, null, dbuuid); 
        		cls = DBKernel.getValue(conn,"Modellkatalog", "ID", result.getInt(Bfrdb.ATT_MODELID+"2")+"", "Klasse");
        		cmx.modelClass = (Integer) cls;
    			   			
	    		emid = result.getInt(Bfrdb.ATT_ESTMODELID+"2");
	    		depDoc = new PmmXmlDoc();
	    		dep = result.getString(Bfrdb.ATT_DEP+"2");
	    		if (varMap.containsKey(dep)) {
	    			dx = new DepXml(varMap.get(dep), result.getString("DepCategory2"), result.getString("DepUnit2"));
	    			dx.name = dep;
	    		}
	    		else {
	    			dx = new DepXml(dep, result.getString("DepCategory2"), result.getString("DepUnit2"));
	    		}
	    		if (!varMap.containsKey(dep)) {
		    		Object responseId = DBKernel.getValue("GeschaetzteModelle", "ID", ""+emid, "Response");
		    		if (responseId != null) {
		    			Object pname = DBKernel.getValue("ModellkatalogParameter", "ID", ""+responseId, "Parametername");
		    			if (pname != null) {
	    					formula = MathUtilities.replaceVariable(formula, dep, pname.toString());
	    					cmx.formula = formula;
		    				dx.name = pname.toString();
		    			}
		    		}
	    		}
	    		//dx.setName(dep);
	    		a = result.getArray("DepDescription2");
	    		da = (Object[])a.getArray();
				if (da != null && da[0] != null) dx.description = da[0].toString();
    			//dx.setDescription(result.getString("DepDescription"));
	    		depDoc.add(dx);
	    		tuple.setValue(Model2Schema.ATT_DEPENDENT, depDoc);

    			cmDoc.add(cmx);
    			tuple.setValue(Model2Schema.ATT_MODELCATALOG, cmDoc);

				emDoc = new PmmXmlDoc();
		    	rms = null;
				if (result.getObject(Bfrdb.ATT_RMS+"2") != null) rms = result.getDouble(Bfrdb.ATT_RMS+"2");
		    	r2 = null;
				if (result.getObject(Bfrdb.ATT_RSQUARED+"2") != null) r2 = result.getDouble(Bfrdb.ATT_RSQUARED+"2");
		    	aic = null;
				if (result.getObject("AIC2") != null) aic = result.getDouble("AIC2");
		    	bic = null;
				if (result.getObject("BIC2") != null) bic = result.getDouble("BIC2");
				emx = new EstModelXml(emid, result.getString("FittedModelName2"), null, rms, r2, aic, bic, null);
				emx.dbuuid = dbuuid;
				if (result.getObject("Geprueft2") != null) emx.checked = result.getBoolean("Geprueft2");
				if (result.getObject("Guetescore2") != null) emx.qualityScore = result.getInt("Guetescore2");
				if (result.getObject("Kommentar2") != null) emx.comment = result.getString("Kommentar2");
				/*
	    		cmt = DBKernel.getValue(conn,"GeschaetzteModelle", "ID", ""+emid, "Kommentar");
	    		if (cmt != null) emx.setComment((String) cmt);
	    		*/
				emDoc.add(emx);
				tuple.setValue(Model2Schema.ATT_ESTMODEL, emDoc);
				
				if (level < 3) {
	    			Object gmid = result.getObject("GlobalModel");
		    		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, gmid);//gmid == null ? MathUtilities.getRandomNegativeInt() : (Integer) gmid);
				}

	    		tuple.setValue(Model2Schema.ATT_INDEPENDENT, DbIo.convertArrays2IndepXmlDoc(varMap, result.getArray(Bfrdb.ATT_INDEP+"2"),
	    				result.getArray(Bfrdb.ATT_MININDEP+"2"), result.getArray(Bfrdb.ATT_MAXINDEP+"2"), result.getArray("IndepCategory2"),
	    				result.getArray("IndepUnit2"), result.getArray("IndepDescription2"), false));
	    		tuple.setValue(Model2Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(varMap, result.getArray(Bfrdb.ATT_PARAMNAME+"2"),
	    				result.getArray(Bfrdb.ATT_VALUE+"2"), result.getArray("ZeitEinheit2"), null, result.getArray("Einheiten2"), result.getArray("StandardError2"), result.getArray(Bfrdb.ATT_MIN+"2"),
	    				result.getArray(Bfrdb.ATT_MAX+"2"), result.getArray("ParamDescription2"), result.getArray(Bfrdb.ATT_PARAMTYPE+"2"), result.getArray("ParamP2"), result.getArray("Paramt2"), cmx.id, emid));

	    		s = result.getString("LitMID2");
	    		if (s != null) tuple.setValue(Model2Schema.ATT_MLIT, getLiterature(conn, s, dbuuid));
	    		s = result.getString("LitEmID2");
	    		if (s != null) tuple.setValue(Model2Schema.ATT_EMLIT, getLiterature(conn, s, dbuuid));

	    		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, withoutMdData ? Model2Schema.NOTWRITABLE : Model2Schema.WRITABLE);
	    		tuple.setValue(Model2Schema.ATT_DBUUID, dbuuid);
    		}
    		
    		if (EmReaderUi.passesFilter(
				level, qualityMode, qualityThresh,
				matrixString, agentString, literatureString, matrixID, agentID, literatureID, parameter,
				modelFilterEnabled, modelList, tuple)) {					
					resultSet.add(tuple);
					if (!addWarningMsg.isEmpty()) emrnm.setWarningMessage(emrnm.getWarningMessage() + addWarningMsg);
    			}
    	}
    	
    	if (level == 2) {    		
    		Map<Integer,List<KnimeTuple>> tuplesBySecEstId = new LinkedHashMap<>();
    		
    		for (KnimeTuple tuple : resultSet) {
    			int secEstId = ((EstModelXml) tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0)).id;
    			
    			if (!tuplesBySecEstId.containsKey(secEstId)) {
    				tuplesBySecEstId.put(secEstId, new ArrayList<KnimeTuple>());
    			}
    			
    			tuplesBySecEstId.get(secEstId).add(tuple);
    		}
    		
    		if (!withoutMdData) {
        		for (KnimeTuple tuple : resultSet) {
        			int secEstId = ((EstModelXml) tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0)).id;
        			Map<String, String> units = PmmUtilities.getMiscUnits(tuplesBySecEstId.get(secEstId));
        			
        			for (PmmXmlElementConvertable xml : tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT).getElementSet()) {
        				IndepXml indep = (IndepXml) xml;
        				
        				if (indep.getUnit() != null) {
        					units.put(indep.getName(), indep.getUnit());
        				}
        			}
        			
        			PmmXmlDoc miscXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
        			
        			for (PmmXmlElementConvertable xml : miscXml.getElementSet()) {
        				MiscXml misc = (MiscXml) xml;
        				String unit = units.get(misc.getName());
        				
        				if (misc.getUnit() != null && !misc.getUnit().equals(unit)) {
        					Category cat = Categories.getCategoryByUnit(misc.getUnit());
        					
        					try {
    							misc.setValue(cat.convert(misc.getValue(), misc.getUnit(), unit));							
    							misc.setUnit(unit);
    						} catch (ConvertException e) {
    							e.printStackTrace();
    						}    					
        				}
        			}
        			
        			tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXml);
        		}    		
    		}
    	}
    	
    	return resultSet;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute( final BufferedDataTable[] inData,
            final ExecutionContext exec )throws Exception {    	
        // fetch database connection
    	Bfrdb db = null;
    	/*
    	if (override) {
			db = new Bfrdb( filename, login, passwd );
			conn = db.getConnection();
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
			conn = null;
		}
		*/
    	try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {}
    	
    	KnimeSchema schema = EstimatedModelReaderNodeModel.createSchema(withoutMdData, level);
		    	
    	// initialize data buffer
    	BufferedDataContainer buf = exec.createDataContainer(schema.createSpec());
    	List<KnimeTuple> tuples = EstimatedModelReaderNodeModel.getKnimeTuples(db, conn, schema, level, withoutMdData,
    			qualityMode, qualityThresh,	matrixString, agentString, literatureString, matrixID, agentID, literatureID, parameter,
				modelFilterEnabled, modelList, null, this);
    	
    	for (int i = 0; i < tuples.size(); i++) {
    		buf.addRowToTable(new DefaultRow(String.valueOf(i), tuples.get(i)));
    	}
    	
    	// close data buffer
    	buf.close();
    	db.close();

        return new BufferedDataTable[]{ buf.getTable() };
    }
    private static PmmXmlDoc getLiterature(Connection conn, String s, String dbuuid) {
		PmmXmlDoc l = new PmmXmlDoc();
		String [] ids = s.split(",");
		for (String id : ids) {			
			LiteratureItem li = DBUtilities.getLiteratureItem(conn, Integer.valueOf(id), dbuuid);
			l.add(li);
		}    
		return l;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {}

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure( final DataTableSpec[] inSpecs )
            throws InvalidSettingsException {
    	
    	DataTableSpec[] outSpec;
    	
    	outSpec = null;
    	try {
    		outSpec = new DataTableSpec[]{ EstimatedModelReaderNodeModel.createSchema(withoutMdData, level).createSpec() };
    	}
    	catch( PmmException ex ) {
    		ex.printStackTrace();
    	}
    	
    	return outSpec;
    }

    public static KnimeSchema createSchema(boolean withoutMdData, int level) throws PmmException {    	
    	if (level == 3) {
			return new Model2Schema();
		}
    	KnimeSchema schema;
    	if (withoutMdData) {
    		schema = new Model1Schema();    		
    	}
    	else {
    		schema = KnimeSchema.merge(new TimeSeriesSchema(), new Model1Schema());
    	}
    	
    	if (level == 2) {
			schema = KnimeSchema.merge(schema, new Model2Schema());
		}
    	return schema;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo( final NodeSettingsWO settings ) {
        try {        	
        	/*
          	Config c = settings.addConfig("DbConfigurationUi");
         	c.addString(DbConfigurationUi.PARAM_FILENAME, filename);
         	c.addString(DbConfigurationUi.PARAM_LOGIN, login);
         	c.addString(DbConfigurationUi.PARAM_PASSWD, passwd);
         	c.addBoolean(DbConfigurationUi.PARAM_OVERRIDE, override);
*/
        	Config c = settings.addConfig("EstModelReaderUi");
         	
         	Config c3 = c.addConfig("ModelReaderUi");
        	c3.addInt( ModelReaderUi.PARAM_LEVEL, level );
        	c3.addString(ModelReaderUi.PARAM_MODELCLASS, modelClass);
        	c3.addBoolean( ModelReaderUi.PARAM_MODELFILTERENABLED, modelFilterEnabled );
        	c3.addIntArray(ModelReaderUi.PARAM_MODELLISTINT, modelList);
        	
         	Config c4 = c.addConfig("MdReaderUi");    	
        	c4.addString( MdReaderUi.PARAM_MATRIXSTRING, matrixString );
        	c4.addString( MdReaderUi.PARAM_AGENTSTRING, agentString );
        	c4.addString( MdReaderUi.PARAM_LITERATURESTRING, literatureString );
        	c4.addInt(MdReaderUi.PARAM_MATRIXID, matrixID);
        	c4.addInt(MdReaderUi.PARAM_AGENTID, agentID);
        	c4.addInt(MdReaderUi.PARAM_LITERATUREID, literatureID);
        	
        	c.addInt( EmReaderUi.PARAM_QUALITYMODE, qualityMode );
        	c.addDouble( EmReaderUi.PARAM_QUALITYTHRESH, qualityThresh );
        	this.set.saveConfig(c.addConfig("PredictorSettings"));

        	c.addBoolean(EmReaderUi.PARAM_NOMDDATA, withoutMdData);

        	Config c2 = c.addConfig(EmReaderUi.PARAM_PARAMETERS);
    		String[] pars = new String[parameter.size()];
    		String[] mins = new String[parameter.size()];
    		String[] maxs = new String[parameter.size()];
    		int i=0;
    		for (String par : parameter.keySet()) {
    			Double[] dbl = parameter.get(par);
    			pars[i] = par;
    			mins[i] = ""+dbl[0];
    			maxs[i] = ""+dbl[1];
    			i++;
    		}
    		c2.addStringArray(EmReaderUi.PARAM_PARAMETERNAME, pars);
    		c2.addStringArray(EmReaderUi.PARAM_PARAMETERMIN, mins);
    		c2.addStringArray(EmReaderUi.PARAM_PARAMETERMAX, maxs);        	 
         }
         catch (Exception e) {}
    	/*
    	settings.addString( PARAM_FILENAME, filename );
    	settings.addString( PARAM_LOGIN, login );
    	settings.addString( PARAM_PASSWD, passwd );
    	settings.addBoolean( PARAM_OVERRIDE, override );
    	settings.addInt( PARAM_LEVEL, level );
    	settings.addString(PARAM_MODELCLASS, modelClass);
    	settings.addBoolean( PARAM_MODELFILTERENABLED, modelFilterEnabled );
    	settings.addString( PARAM_MODELLIST, modelList );
    	settings.addInt( PARAM_QUALITYMODE, qualityMode );
    	settings.addDouble( PARAM_QUALITYTHRESH, qualityThresh );
    	settings.addString( PARAM_MATRIXSTRING, matrixString );
    	settings.addString( PARAM_AGENTSTRING, agentString );
    	settings.addString( PARAM_LITERATURESTRING, literatureString );
    	settings.addInt(PARAM_MATRIXID, matrixID);
    	settings.addInt(PARAM_AGENTID, agentID);
    	settings.addInt(PARAM_LITERATUREID, literatureID);
    	
		Config c = settings.addConfig(PARAM_PARAMETERS);
		String[] pars = new String[parameter.size()];
		String[] mins = new String[parameter.size()];
		String[] maxs = new String[parameter.size()];
		int i=0;
		for (String par : parameter.keySet()) {
			Double[] dbl = parameter.get(par);
			pars[i] = par;
			mins[i] = ""+dbl[0];
			maxs[i] = ""+dbl[1];
			i++;
		}
		c.addStringArray(PARAM_PARAMETERNAME, pars);
		c.addStringArray(PARAM_PARAMETERMIN, mins);
		c.addStringArray(PARAM_PARAMETERMAX, maxs);
		*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        //loadDBGui(settings);
        loadEstModelGui(settings);    		
    }
/*
    private void loadDBGui(final NodeSettingsRO settings) throws InvalidSettingsException {
    	try {
    		Config c = settings.getConfig("DbConfigurationUi");
    		filename = c.getString(DbConfigurationUi.PARAM_FILENAME);
    		login = c.getString(DbConfigurationUi.PARAM_LOGIN);
    		passwd = c.getString(DbConfigurationUi.PARAM_PASSWD);
    		override = c.getBoolean(DbConfigurationUi.PARAM_OVERRIDE);    	
    	}
    	catch (Exception e) {}
    }
    */
    private void loadEstModelGui(final NodeSettingsRO settings) throws InvalidSettingsException {
    	try {
    		Config c = settings.getConfig("EstModelReaderUi");
    		estModelReaderUiSettings = c;

    		Config c3 = c.getConfig("ModelReaderUi");
        	level = c3.getInt( ModelReaderUi.PARAM_LEVEL );
        	modelClass = c3.getString(ModelReaderUi.PARAM_MODELCLASS);
        	modelFilterEnabled = c3.getBoolean( ModelReaderUi.PARAM_MODELFILTERENABLED );
        	if (c3.containsKey(ModelReaderUi.PARAM_MODELLISTINT)) modelList = c3.getIntArray(ModelReaderUi.PARAM_MODELLISTINT);
    		else if (c3.containsKey("modelList")) {
    			String ids = c3.getString("modelList");
    			if (ids != null && ids.length() > 0) {
    				String[] token = ids.split(",");
    				int[] idis = new int[token.length];
    				int i=0;
    				for (String s : token)  {
    					idis[i] = Integer.parseInt(s);
    					i++;
    				}
    				modelList = idis;
    			}
    		}
        	
    		Config c4 = c.getConfig("MdReaderUi");
        	matrixString = c4.getString( MdReaderUi.PARAM_MATRIXSTRING );
        	agentString = c4.getString( MdReaderUi.PARAM_AGENTSTRING );
        	literatureString = c4.getString( MdReaderUi.PARAM_LITERATURESTRING );
        	matrixID = c4.containsKey(MdReaderUi.PARAM_MATRIXID) ? c4.getInt(MdReaderUi.PARAM_MATRIXID) : 0;
        	agentID = c4.containsKey(MdReaderUi.PARAM_AGENTID) ? c4.getInt(MdReaderUi.PARAM_AGENTID) : 0;
        	literatureID = c4.containsKey(MdReaderUi.PARAM_LITERATUREID) ? c4.getInt(MdReaderUi.PARAM_LITERATUREID) : 0;

        	qualityMode = c.getInt( EmReaderUi.PARAM_QUALITYMODE );
        	qualityThresh = c.getDouble( EmReaderUi.PARAM_QUALITYTHRESH );
        	this.set.loadConfig(c.getConfig("PredictorSettings"));
        	
    		withoutMdData = c.getBoolean(EmReaderUi.PARAM_NOMDDATA);

    		Config c2 = c.getConfig(EmReaderUi.PARAM_PARAMETERS);
    		String[] pars = c2.getStringArray(EmReaderUi.PARAM_PARAMETERNAME);
    		String[] mins = c2.getStringArray(EmReaderUi.PARAM_PARAMETERMIN);
    		String[] maxs = c2.getStringArray(EmReaderUi.PARAM_PARAMETERMAX);

            parameter = new LinkedHashMap<>();
    		for (int i=0;i<pars.length;i++) {
    			Double[] dbl = new Double[2];
    			if (!mins[i].equals("null")) dbl[0] = Double.parseDouble(mins[i]);
    			if (!maxs[i].equals("null")) dbl[1] = Double.parseDouble(maxs[i]);
    			parameter.put(pars[i], dbl);
    		}    	    		
    	}
    	catch (Exception e) {}
    }
    
    protected void loadOldValidatedSettingsFrom( final NodeSettingsRO settings )
            throws InvalidSettingsException {
    	/*
    	filename = settings.getString( PARAM_FILENAME );
    	login = settings.getString( PARAM_LOGIN );
    	passwd = settings.getString( PARAM_PASSWD );
    	override = settings.getBoolean( PARAM_OVERRIDE );
    	*/
    	level = settings.getInt( PARAM_LEVEL );
    	modelClass = settings.getString(PARAM_MODELCLASS);
    	modelFilterEnabled = settings.getBoolean( PARAM_MODELFILTERENABLED );
    	if (settings.containsKey(PARAM_MODELLISTINT)) modelList = settings.getIntArray(ModelReaderUi.PARAM_MODELLISTINT);
		else if (settings.containsKey("modelList")) {
			String ids = settings.getString("modelList");
			if (ids != null && ids.length() > 0) {
				String[] token = ids.split(",");
				int[] idis = new int[token.length];
				int i=0;
				for (String s : token)  {
					idis[i] = Integer.parseInt(s);
					i++;
				}
				modelList = idis;
			}
		}
    	qualityMode = settings.getInt( PARAM_QUALITYMODE );
    	qualityThresh = settings.getDouble( PARAM_QUALITYTHRESH );
    	matrixString = settings.getString( PARAM_MATRIXSTRING );
    	agentString = settings.getString( PARAM_AGENTSTRING );
    	literatureString = settings.getString( PARAM_LITERATURESTRING );
    	matrixID = settings.containsKey(PARAM_MATRIXID) ? settings.getInt(PARAM_MATRIXID) : 0;
    	agentID = settings.containsKey(PARAM_AGENTID) ? settings.getInt(PARAM_AGENTID) : 0;
    	literatureID = settings.containsKey(PARAM_LITERATUREID) ? settings.getInt(PARAM_LITERATUREID) : 0;

		Config c = settings.getConfig(PARAM_PARAMETERS);
		String[] pars = c.getStringArray(PARAM_PARAMETERNAME);
		String[] mins = c.getStringArray(PARAM_PARAMETERMIN);
		String[] maxs = c.getStringArray(PARAM_PARAMETERMAX);

        parameter = new LinkedHashMap<>();
		for (int i=0;i<pars.length;i++) {
			Double[] dbl = new Double[2];
			if (!mins[i].equals("null")) dbl[0] = Double.parseDouble(mins[i]);
			if (!maxs[i].equals("null")) dbl[1] = Double.parseDouble(maxs[i]);
			parameter.put(pars[i], dbl);
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings( final NodeSettingsRO settings )
            throws InvalidSettingsException {
    	// level.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals( final File internDir,
            final ExecutionMonitor exec )throws IOException,
            CanceledExecutionException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals( final File internDir,
            final ExecutionMonitor exec )throws IOException,
            CanceledExecutionException {}
        
}

