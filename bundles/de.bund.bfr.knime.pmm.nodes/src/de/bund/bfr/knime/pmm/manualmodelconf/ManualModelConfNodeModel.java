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
package de.bund.bfr.knime.pmm.manualmodelconf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JOptionPane;

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

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.manualmodelconf.ui.MMC_M;
import de.bund.bfr.knime.pmm.manualmodelconf.ui.MMC_TS;

/**
 * This is the model implementation of ManualModelConf.
 * 
 *
 * @author ManualModelConf
 */
public class ManualModelConfNodeModel extends NodeModel {
	
	protected static final String PARAM_XMLSTRING = "xmlString";
	protected static final String PARAM_XMLDIFFSTRING = "xmlDiffString";
	protected static final String PARAM_TSXMLSTRING = "tsXmlString";
	protected static final String PARAM_TSONESTEP = "oneStepFitTss";
		
	private PmmXmlDoc doc = null;
	private PmmXmlDoc docTS = null;
	private HashMap<Integer, HashSet<Integer>> oneStepFitTs = null;
	private String diffString = null;
	
	private boolean hasEditFeature;
	private boolean formulaCreator;
	
	/**
     * Constructor for the node model.
     */
    protected ManualModelConfNodeModel() {    
        this(false, false);        
    }
    protected ManualModelConfNodeModel(boolean hasEditFeature, boolean formulaCreator) {    
        super(hasEditFeature ? 1 : 0, 1);
        this.hasEditFeature = hasEditFeature;
        this.formulaCreator = formulaCreator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	//mergeInportNSettings(inData);
    	if (doc != null) {        	
        	KnimeSchema ks = getSchema();
        	BufferedDataContainer buf = exec.createDataContainer(ks.createSpec());

        	KnimeTuple tupleM1 = null;
        	List<KnimeTuple> rowSec = new ArrayList<>();
        	HashSet<PmmTimeSeries> tstuples = new HashSet<PmmTimeSeries>();
        	PmmTimeSeries tstuple = new PmmTimeSeries();
        	int globalID = MathUtilities.getRandomNegativeInt();
        	for (PmmXmlElementConvertable el : doc.getElementSet()) {
        		if (el instanceof ParametricModel) {
	        		ParametricModel model = (ParametricModel) el;	 
	    			if (model.getLevel() == 1) {
	    				if (model.getIndependent().size() > 0) {
	    					if (tupleM1 != null) {
	    						if (hasEditFeature && tstuples != null && tstuples.size() > 1) {
	    							for (PmmTimeSeries tst : tstuples) {
		    							doBuf(tupleM1, tst, rowSec, buf, ks);	    								
	    							}
	    						}
	    						else {
	    							doBuf(tupleM1, tstuple, rowSec, buf, ks);
	    						}
	    					}
	    					tupleM1 = model.getKnimeTuple();
	    					tupleM1.setValue(Model1Schema.ATT_DATABASEWRITABLE, 1);
	    					rowSec = new ArrayList<>();
	    					tstuples = new HashSet<PmmTimeSeries>();
	    					tstuple = new PmmTimeSeries();
	    		        	if (!formulaCreator) {
	    		            	if (docTS != null) {
	    		                	for (PmmXmlElementConvertable ell : docTS.getElementSet()) {        		
	    		                		if (ell instanceof PmmTimeSeries) {
	    		                			PmmTimeSeries ts = (PmmTimeSeries) ell;
	    		                			boolean addIt = ts.getCondId().intValue() == model.getCondId();
	    		                    		if (!addIt && hasEditFeature && oneStepFitTs != null && oneStepFitTs.containsKey(model.getEstModelId())) {
	    		                    			if (oneStepFitTs.get(model.getEstModelId()).contains(ts.getCondId().intValue())) {
	    		                    				addIt = true;
	    		                    			}
	    		                    		}
	    		                			if (addIt) {
	    		                				tstuple = ts;
	    		                				tstuples.add(ts);
	    		                				//break;
	    		                			}
	    		                		}
	    		                	}
	    		            	}        		
	    		        	}
	    				}
	    			}
	    			else {
	    	    		// SecondaryModel
	    				//if (model.getIndepVarSet().size() > 0) {
	    				if (model.getDepXml().origName == null) {
	    					model.getDepXml().origName = model.getDepXml().name;
	    				}
	    					KnimeTuple tupleM2 = model.getKnimeTuple();
	    					
	    					if (!hasEditFeature) tupleM2.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, null);

	    					tupleM2.setValue(Model2Schema.ATT_DATABASEWRITABLE, 1);
	    					tupleM2.setValue(Model2Schema.ATT_DATABASEWRITABLE, 1);
	    					if (!hasEditFeature) tupleM2.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalID); //  && !formulaCreator
	    		    		rowSec.add(tupleM2);					
	    				//}	    		
	    			}
        		}
        	}
			if (hasEditFeature && tstuples != null && tstuples.size() > 1) {
				for (PmmTimeSeries tst : tstuples) {
					doBuf(tupleM1, tst, rowSec, buf, ks);	    								
				}
			}
			else {
				doBuf(tupleM1, tstuple, rowSec, buf, ks);
			}

        	buf.close();
            return new BufferedDataTable[]{ buf.getTable()};
    	}
    	else {
    		return null;
    	}
    }
    private void doBuf(KnimeTuple tupleM1, PmmTimeSeries tstuple, List<KnimeTuple> rowSec, BufferedDataContainer buf, KnimeSchema ks) {
    	if (tupleM1 != null) {
    		// Set primary variable names to TimeSeriesSchema.TIME and TimeSeriesSchema.LOGC
    		PmmXmlDoc modelXml = tupleM1.getPmmXml(Model1Schema.ATT_MODELCATALOG);
    		String formula = ((CatalogModelXml) modelXml.get(0)).formula;
    		PmmXmlDoc depVar = tupleM1.getPmmXml(Model1Schema.ATT_DEPENDENT);
    		PmmXmlDoc indepVar = tupleM1.getPmmXml(Model1Schema.ATT_INDEPENDENT);        		
    		
    		if (depVar.size() == 1) {
    			formula = MathUtilities.replaceVariable(formula, ((DepXml) depVar.get(0)).name, AttributeUtilities.CONCENTRATION);
    			((DepXml) depVar.get(0)).name = AttributeUtilities.CONCENTRATION;        			
    			((DepXml) depVar.get(0)).origName = AttributeUtilities.CONCENTRATION;        			
    		}
    		
    		if (indepVar.size() == 1) {
    			formula = MathUtilities.replaceVariable(formula, ((IndepXml) indepVar.get(0)).getName(), AttributeUtilities.TIME);
    			((IndepXml) indepVar.get(0)).setName(AttributeUtilities.TIME);
    			((IndepXml) indepVar.get(0)).setOrigName(AttributeUtilities.TIME);
    		}
    		
    		((CatalogModelXml) modelXml.get(0)).formula = formula;
    		if (formulaCreator) {
        		PmmXmlDoc estXml = tupleM1.getPmmXml(Model1Schema.ATT_ESTMODEL);
        		EstModelXml exl =  (EstModelXml) estXml.get(0);    
        		((CatalogModelXml) modelXml.get(0)).comment = exl.comment;
        		exl.comment = "";
        		tupleM1.setValue(Model1Schema.ATT_ESTMODEL, estXml);
        		
    		}
    		tupleM1.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
    		tupleM1.setValue(Model1Schema.ATT_DEPENDENT, depVar);
    		tupleM1.setValue(Model1Schema.ATT_INDEPENDENT, indepVar);
    		
    		if (!formulaCreator) {
            	KnimeSchema ts = new TimeSeriesSchema();
            	KnimeSchema m1 = new Model1Schema();
            	KnimeSchema tsm1 = KnimeSchema.merge(ts, m1);
    			tupleM1 = KnimeTuple.merge(tsm1, tstuple, tupleM1);
    		}
        	if (rowSec.size() > 0) {
        		for (int i=0;i<rowSec.size();i++) {
            		buf.addRowToTable(new DefaultRow(String.valueOf(buf.size()), KnimeTuple.merge(ks, tupleM1, rowSec.get(i))));    		
        		}
        	}
        	else { // nur TSM1 generieren
        		buf.addRowToTable(new DefaultRow(String.valueOf(buf.size()), tupleM1));
        	}
    	}
    	else if (rowSec.size() == 1) {
        	//KnimeTuple emptyTupleM1 = new KnimeTuple(new Model1Schema());
        	//buf.addRowToTable(new DefaultRow(String.valueOf(0), KnimeTuple.merge(ks, emptyTupleM1, rowSec.get(0))));    		
        	buf.addRowToTable(new DefaultRow(String.valueOf(0), rowSec.get(0)));    		
    	}
    	else {
    		if (!formulaCreator) buf.addRowToTable(new DefaultRow(String.valueOf(0), tstuple));    		
    	}    	
    }

    private boolean hasSecondary() {
    	if (doc != null) {
        	for (PmmXmlElementConvertable el : doc.getElementSet()) {        		
        		if (el instanceof ParametricModel) {
	        		ParametricModel model = (ParametricModel) el;
	        		if (model.getLevel() == 2) { //  && model.getIndepVarSet().size() > 0
						return true;
					}
        		}
        	}
    	}
    	return false;
    }
    private boolean hasPrimary() {
    	if (doc != null) {
        	for (PmmXmlElementConvertable el : doc.getElementSet()) {        		
        		if (el instanceof ParametricModel) {
	        		ParametricModel model = (ParametricModel) el;
	        		if (model.getLevel() == 1 && model.getIndependent().size() > 0) {
						return true;
					}
        		}
        	}
    	}
    	return false;
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
    	if (doc == null) {
			throw new InvalidSettingsException("No Settings available");
		}
    	KnimeSchema ks = getSchema();
        return new DataTableSpec[] {ks == null ? null : ks.createSpec()}; // , createXmlSpec() 
    }
    private KnimeSchema getSchema() {
    	KnimeSchema ks = null;
		try {
			boolean hp = hasPrimary();
			boolean hs = hasSecondary();
			if (hp && hs) {
				if (formulaCreator) ks = KnimeSchema.merge(new Model1Schema(), new Model2Schema());
				else ks = KnimeSchema.merge(new TimeSeriesSchema(), KnimeSchema.merge(new Model1Schema(), new Model2Schema()));
			}
	    	else if (hp) {
	    		if (formulaCreator) ks = new Model1Schema();
	    		else ks = KnimeSchema.merge(new TimeSeriesSchema(), new Model1Schema());
	    	}	
	    	else if (hs) {
	    		ks = new Model2Schema();
	    	}	
	    	else {
	    		if (formulaCreator) ks = null;
	    		else ks = new TimeSeriesSchema();
	    	}	
		}
		catch (PmmException e) {
			e.printStackTrace();
		}
		return ks;
    }
    
    @SuppressWarnings("unused")
	private void mergeInportNSettings(BufferedDataTable[] inData) {
		if (inData != null && inData.length == 1) {
			HashMap<Integer, ParametricModel> mlist = new HashMap<>();
			HashMap<Integer, PmmTimeSeries> tslist = new HashMap<>();
	    	if (doc != null) {        	
				for (int i = 0; i < doc.size(); i++) {
					PmmXmlElementConvertable el = doc.get(i);
					if (el instanceof ParametricModel) {
						ParametricModel pm = (ParametricModel) el;
						mlist.put(pm.getEstModelId(), pm);
					}
				}
	    	}
	    	if (docTS != null) {        	
				for (int i = 0; i < docTS.size(); i++) {
					PmmXmlElementConvertable el = docTS.get(i);
					if (el instanceof PmmTimeSeries) {
						PmmTimeSeries ts = (PmmTimeSeries) el;
						tslist.put(ts.getCondId(), ts);
					}
				}
			}
		    DataTableSpec inSpec = inData[0].getDataTableSpec();
		    try {
			    KnimeSchema tsSchema = new TimeSeriesSchema();
			    KnimeSchema inSchema1 = new Model1Schema();
			    KnimeSchema inSchema2 = new Model2Schema();
			    boolean hasTs = tsSchema.conforms(inSpec);
			    boolean hasM1 = inSchema1.conforms(inSpec);
			    boolean hasM2 = inSchema2.conforms(inSpec);
		    	
			    KnimeSchema finalSchema = null;
		    	if (hasM1 && hasM2) finalSchema = KnimeSchema.merge(inSchema1, inSchema2);
		    	else if (hasM1) finalSchema = inSchema1;
		    	else if (hasM2) finalSchema = inSchema2;
		    	if (hasTs) finalSchema = (finalSchema == null) ? tsSchema : KnimeSchema.merge(tsSchema, finalSchema);
		    	if (finalSchema != null) {
		    		KnimeRelationReader reader = new KnimeRelationReader(finalSchema, inData[0]);
		    		HashMap<Integer, PmmTimeSeries> tss = new HashMap<>();
		    		HashMap<Integer, ParametricModel> m1s = new HashMap<>();
		    		HashMap<Integer, ParametricModel> m2s = new HashMap<>();
		    		HashMap<ParametricModel, HashMap<String, ParametricModel>> m_secondaryModels = new HashMap<>();
		    		Integer condID = null;
		    		Integer m1EstID = null, m2EstID;
		    		while (reader.hasMoreElements()) {
		    			KnimeTuple row = reader.nextElement();
		    			if (hasTs) {
			    			PmmTimeSeries ts = new PmmTimeSeries(row);
			    			condID = ts.getCondId();
			    			//System.err.println(condID);
			    			if (tslist.containsKey(condID)) tss.put(condID, tslist.get(condID));
			    			else tss.put(condID, ts);
		    			}
		    			if (hasM1) {
			    			ParametricModel pm1 = new ParametricModel(row, 1, hasTs ? condID : null);
			    			m1EstID = pm1.getEstModelId();
			    			if (!m1s.containsKey(m1EstID)) {
				    			if (mlist.containsKey(m1EstID)) m1s.put(m1EstID, mlist.get(m1EstID));
				    			else m1s.put(m1EstID, pm1);			    				
			    			}
			    			if (hasM2) {
			    				ParametricModel pm2 = new ParametricModel(row, 2, null);
			    				m2EstID = pm2.getEstModelId();
				    			if (!m2s.containsKey(m2EstID)) {
					    			if (mlist.containsKey(m2EstID)) m2s.put(m2EstID, mlist.get(m2EstID));
					    			else m2s.put(m2EstID, pm2);			    				
				    			}
				    			if (!m_secondaryModels.containsKey(m1s.get(m1EstID))) m_secondaryModels.put(m1s.get(m1EstID), new HashMap<String, ParametricModel>());
				    			HashMap<String, ParametricModel> hm = m_secondaryModels.get(m1s.get(m1EstID));
				    			hm.put(pm2.getDepVar(), m2s.get(m2EstID));
			    			}
		    			}
		    		}
		    		MMC_TS m_mmcts = new MMC_TS();
		    		MMC_M m_mmcm = new MMC_M(JOptionPane.getRootFrame(), 1, "", false, m_mmcts);
		    		m_mmcm.setConnection(DBKernel.getLocalConn(true));
		    		m_mmcm.setInputData(m1s.values(), m_secondaryModels, tss);
		    		m_mmcm.stopCellEditing();
		    		try {
						doc = m_mmcm.listToDoc();
					} catch (InvalidSettingsException e) {
						e.printStackTrace();
					}
		    		docTS = m_mmcm.tssToDoc();
		    	}
		    }
		    catch (PmmException e) {}    	
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo( final NodeSettingsWO settings ) {
    	// OneStepFitTss
		if (oneStepFitTs != null) settings.addString(ManualModelConfNodeModel.PARAM_TSONESTEP, XmlConverter.objectToXml(oneStepFitTs));
    	// Modelle
		if (hasEditFeature) {
			settings.addString(ManualModelConfNodeModel.PARAM_XMLDIFFSTRING, diffString);
		}
    	if (doc != null) {
    		String xmlStr = doc.toXmlString();
    		//System.err.println(xmlStr);
			settings.addString(PARAM_XMLSTRING, xmlStr);
		}
    	if (!formulaCreator) {
        	if (docTS != null) {
        		String tsXmlStr = docTS.toXmlString();
        		//System.err.println(xmlStr);
    			settings.addString(PARAM_TSXMLSTRING, tsXmlStr);
    		}
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom( final NodeSettingsRO settings )
            throws InvalidSettingsException {
		// OneStepFitTss
		if (settings.containsKey(PARAM_TSONESTEP)) {
			oneStepFitTs = XmlConverter.xmlToObject(settings.getString(PARAM_TSONESTEP), new HashMap<Integer, HashSet<Integer>>());
		}

		if (hasEditFeature && settings.containsKey(PARAM_XMLDIFFSTRING)) {
			diffString = settings.getString(ManualModelConfNodeModel.PARAM_XMLDIFFSTRING);
		}
		// Modelle
    	try {
			if (settings.containsKey(PARAM_XMLSTRING)) {
				String xmlStr = settings.getString(PARAM_XMLSTRING);
	    		//System.err.println(xmlStr);
				doc = new PmmXmlDoc(xmlStr);
			}
		}
    	catch (Exception e1) {
			e1.printStackTrace();
    		throw new InvalidSettingsException("Invalid model parameters");
		}
    	try {
	    	if (!formulaCreator) {
	    		if (settings.containsKey(PARAM_TSXMLSTRING)) {
	    			String tsXmlStr = settings.getString(PARAM_TSXMLSTRING);
	        		docTS = new PmmXmlDoc(tsXmlStr);
	    		}
	    		else { // old version...
	            	// TimeSeries
	    			String CFGKEY_AGENT = "Agent";
	    			String CFGKEY_MATRIX = "Matrix";
	    			String CFGKEY_AGENTID = "AgentID";
	    			String CFGKEY_MATRIXID = "MatrixID";
	    			String CFGKEY_AGENTDETAIL = "AgentDetail";
	    			String CFGKEY_MATRIXDETAIL = "MatrixDetail";
	    			String CFGKEY_COMMENT = "Comment";
	    			String CFGKEY_TEMPERATURE = "Temperature";
	    			String CFGKEY_PH = "pH";
	    			String CFGKEY_AW = "aw";
	    			String agent, agentDetail, matrix, matrixDetail, comment;
	    			Integer agentID, matrixID;
	    			Double temperature, ph, waterActivity;
	    			
	    			settings.containsKey(CFGKEY_AGENT);
	        		try {agent = settings.getString(CFGKEY_AGENT);}
	        		catch (InvalidSettingsException e) {agent = null;}
	        		try {agentID = settings.getInt(CFGKEY_AGENTID);}
	        		catch (InvalidSettingsException e) {agentID = null;}
	        		try {agentDetail = settings.getString(CFGKEY_AGENTDETAIL);}
	        		catch (InvalidSettingsException e) {agentDetail = null;}
	        		try {matrix = settings.getString(CFGKEY_MATRIX);}
	        		catch (InvalidSettingsException e) {matrix = null;}
	        		try {matrixID = settings.getInt(CFGKEY_MATRIXID);}
	        		catch (InvalidSettingsException e) {matrixID = null;}
	        		try {matrixDetail = settings.getString(CFGKEY_MATRIXDETAIL);}
	        		catch (InvalidSettingsException e) {matrixDetail = null;}
	        		try {comment = settings.getString(CFGKEY_COMMENT);}
	        		catch (InvalidSettingsException e) {comment = null;}
	        		try {temperature = settings.getDouble(CFGKEY_TEMPERATURE);}
	        		catch (InvalidSettingsException e) {temperature = null;}
	        		try {ph = settings.getDouble(CFGKEY_PH);}
	        		catch (InvalidSettingsException e) {ph = null;}
	        		try {waterActivity = settings.getDouble(CFGKEY_AW);}
	        		catch (InvalidSettingsException e) {waterActivity = null;}	   
	        		
	    			PmmTimeSeries tstuple = new PmmTimeSeries();
	            		PmmXmlDoc matDoc = new PmmXmlDoc(); 
	    				MatrixXml mx = new MatrixXml(matrixID, matrix, matrixDetail);
	    				matDoc.add(mx);
	            		tstuple.setValue(TimeSeriesSchema.ATT_MATRIX, matDoc);
	            		PmmXmlDoc agtDoc = new PmmXmlDoc(); 
	    				AgentXml ax = new AgentXml(agentID, agent, agentDetail);
	    				agtDoc.add(ax);
	            		tstuple.setValue(TimeSeriesSchema.ATT_AGENT, agtDoc);
	        			PmmXmlDoc mdInfoDoc = new PmmXmlDoc();
	        			int ri = MathUtilities.getRandomNegativeInt();
	        			MdInfoXml mdix = new MdInfoXml(ri, "i"+ri, comment, null, null);
	        			mdInfoDoc.add(mdix);
	        			tstuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);
	        			tstuple.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID,AttributeUtilities.ATT_TEMPERATURE,AttributeUtilities.ATT_TEMPERATURE,temperature,null,"°C");
	        			tstuple.addMisc(AttributeUtilities.ATT_PH_ID,AttributeUtilities.ATT_PH,AttributeUtilities.ATT_PH,ph,null,null);
	        			tstuple.addMisc(AttributeUtilities.ATT_AW_ID,AttributeUtilities.ATT_AW,AttributeUtilities.ATT_AW,waterActivity,null,null);

	        		docTS = new PmmXmlDoc();
	        		docTS.add(tstuple);
	    		}
	    	}
		}
    	catch (Exception e1) {
			e1.printStackTrace();
			docTS.add(new PmmTimeSeries());
    		throw new InvalidSettingsException("Invalid model parameters");
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings( final NodeSettingsRO settings )
            throws InvalidSettingsException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {}
    
    /*
    private DataTableSpec createXmlSpec() {
    	
    	DataColumnSpec[] spec;
    	
    	spec = new DataColumnSpec[ 1 ];
    	spec[ 0 ] = new DataColumnSpecCreator( "XmlString", StringCell.TYPE ).createSpec();
    	
    	return new DataTableSpec( spec );
    }
*/
}

