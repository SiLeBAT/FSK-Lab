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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JOptionPane;

import org.hsh.bfr.db.DBKernel;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.ValueChange;
import org.jdom2.JDOMException;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.manualmodelconf.ui.MMC_M;
import de.bund.bfr.knime.pmm.manualmodelconf.ui.MMC_TS;

/**
 * <code>NodeDialog</code> for the "ManualModelConf" Node.
 * 
 * @author ManualModelConf
 */
public class ManualModelEditorNodeDialog extends DataAwareNodeDialogPane {
	
	private MMC_M m_mmcm;
	private MMC_TS m_mmcts;
	private HashMap<Integer, HashSet<Integer>> oneStepFitTs;
	
	PmmXmlDoc inputDoc = null; 

	/**
     * New pane for configuring the ManualModelConf node.
     */
    protected ManualModelEditorNodeDialog() {
    	try {    
    		m_mmcts = new MMC_TS();
    		m_mmcm = new MMC_M(JOptionPane.getRootFrame(), 1, "", false, m_mmcts);
    		m_mmcm.setConnection(DBKernel.getLocalConn(true));
    		oneStepFitTs = new HashMap<>();
    		this.addTab("Model Definition", m_mmcm);    	    		
        	//this.addTab("Microbial Data", m_mmcts);        			    		
    	}
    	catch( Exception e ) {
    		e.printStackTrace( System.err );
    	}
    }

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings)
			throws InvalidSettingsException {	
		//m_confui.stopCellEditing();
		m_mmcm.stopCellEditing();
		//settings.addString( ManualModelConfNodeModel.PARAM_XMLSTRING, m_confui.toXmlString() );
		HashMap<Integer, HashMap<String, Object[]>> diffMap = getDiff(inputDoc, m_mmcm.listToDoc());
		settings.addString( ManualModelConfNodeModel.PARAM_XMLDIFFSTRING, XmlConverter.objectToXml(diffMap));
		
		String xml = m_mmcm.listToXmlString();
		settings.addString( ManualModelConfNodeModel.PARAM_XMLSTRING, xml);
		String tStr = m_mmcm.tssToXmlString();
		settings.addString( ManualModelConfNodeModel.PARAM_TSXMLSTRING, tStr );//-1673022417
		
		settings.addString(ManualModelConfNodeModel.PARAM_TSONESTEP, XmlConverter.objectToXml(oneStepFitTs));
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] inData) throws NotConfigurableException {
		String mStr = null;
		String tsStr = null;
		
		// OneStepFitTss
		try {
			if (settings.containsKey(ManualModelConfNodeModel.PARAM_TSONESTEP)) {
				oneStepFitTs = XmlConverter.xmlToObject(settings.getString(ManualModelConfNodeModel.PARAM_TSONESTEP), new HashMap<Integer, HashSet<Integer>>());
			}
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace();
		}
		// MMC_M
		try {
			if (settings.containsKey(ManualModelConfNodeModel.PARAM_XMLSTRING)) {
				mStr = settings.getString(ManualModelConfNodeModel.PARAM_XMLSTRING);
			}
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace();
		}
		//MMC_TS
		try {
			if (settings.containsKey(ManualModelConfNodeModel.PARAM_TSXMLSTRING)) {
				tsStr = settings.getString(ManualModelConfNodeModel.PARAM_TSXMLSTRING);
			}
		}
		catch (Exception e) {} // e.printStackTrace();

		inputDoc = null;
		if (inData != null && inData.length == 1) {
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
			    			tss.put(condID, ts);
		    			}
		    			if (hasM1) {
			    			ParametricModel pm1 = new ParametricModel(row, 1, hasTs ? condID : null);
			    			m1EstID = pm1.getEstModelId();
			    			if (!m1s.containsKey(m1EstID)) {
			    				m1s.put(m1EstID, pm1);			    				
			    			}
			    			if (!oneStepFitTs.containsKey(m1EstID)) oneStepFitTs.put(m1EstID, new HashSet<Integer>());
			    			HashSet<Integer> hs = oneStepFitTs.get(m1EstID);
			    			hs.add(pm1.condId);
			    			if (hasM2) {
			    				ParametricModel pm2 = new ParametricModel(row, 2, null);
			    				m2EstID = pm2.getEstModelId();
				    			if (!m2s.containsKey(m2EstID)) {
				    				m2s.put(m2EstID, pm2);			    				
				    			}
				    			if (!m_secondaryModels.containsKey(m1s.get(m1EstID))) m_secondaryModels.put(m1s.get(m1EstID), new HashMap<String, ParametricModel>());
				    			HashMap<String, ParametricModel> hm = m_secondaryModels.get(m1s.get(m1EstID));
				    			hm.put(pm2.getDepVar(), m2s.get(m2EstID));
			    			}
		    			}
		    			else if (hasM2) {
		    				ParametricModel pm2 = new ParametricModel(row, 2, null);
		    				m2EstID = pm2.getEstModelId();
			    			if (!m2s.containsKey(m2EstID)) {
			    				m2s.put(m2EstID, pm2);			    				
			    			}
			    			m_secondaryModels.put(null, new HashMap<String, ParametricModel>());
			    			HashMap<String, ParametricModel> hm = m_secondaryModels.get(null);
			    			hm.put(pm2.getDepVar(), m2s.get(m2EstID));
		    			}
		    		}
		    		m_mmcm.setInputData(m1s.values(), m_secondaryModels, tss);
		    		try {
		    			inputDoc = m_mmcm.listToDoc().clonePMs();
					} catch (InvalidSettingsException e) {
						e.printStackTrace();
					}

		    		try {
		    			if (settings.containsKey(ManualModelConfNodeModel.PARAM_XMLDIFFSTRING)) { // new behaviour
		    				HashMap<Integer, HashMap<String, Object[]>> diffMap = XmlConverter.xmlToObject(settings.getString(ManualModelConfNodeModel.PARAM_XMLDIFFSTRING), new HashMap<Integer, HashMap<String, Object[]>>());
		    				for (Integer id : diffMap.keySet()) {
		    					HashMap<String, Object[]> hm = diffMap.get(id);
		    					if (m1s.containsKey(id)) {
		    						for (String property : hm.keySet()) {
		    							Object[] o = hm.get(property);
					    		    	if (o != null) setValue(m1s.get(id), property, o[0], o[1]);
		    						}
		    					}
		    					else { // secondary Models
		    						checkSec(m_secondaryModels, id, hm);
		    					}
		    				}
		    			}
		    			else {
	    					try {
	    						
	    						if (mStr != null && !mStr.isEmpty()) {
	    							PmmXmlDoc mDoc = new PmmXmlDoc(mStr);
	    							for (int i = 0; i < mDoc.size(); i++) {
	    								PmmXmlElementConvertable el = mDoc.get(i);
	    								if (el instanceof ParametricModel) {
	    									ParametricModel pm = (ParametricModel) el;
	    									if (m1s.containsKey(pm.getEstModelId())) m1s.put(pm.getEstModelId(), pm);
	    									else {
	    			    						checkSec(m_secondaryModels, pm);
	    									}
	    								}
	    							}
	    						}
	    						
	    						if (tsStr != null && !tsStr.isEmpty()) {
	    							PmmXmlDoc tsDoc = new PmmXmlDoc(tsStr);
	    							for (int i = 0; i < tsDoc.size(); i++) {
	    								PmmXmlElementConvertable el = tsDoc.get(i);
	    								if (el instanceof PmmTimeSeries) {
	    									PmmTimeSeries ts = (PmmTimeSeries) el;
	    									if (tss.containsKey(ts.getCondId())) tss.put(ts.getCondId(), ts);
	    								}
	    							}
	    						}
	    					}
	    					catch (IOException e) {
	    						e.printStackTrace();
	    					}
	    					catch (JDOMException e) {
	    						e.printStackTrace();
	    					}
		    			}
			    		m_mmcm.setInputData(m1s.values(), m_secondaryModels, tss);
		    		}
		    		catch( InvalidSettingsException e ) {
		    			e.printStackTrace();
		    		}
		    	}
		    }
		    catch (PmmException e) {}
		}
		else {
			if (tsStr != null) m_mmcts.setTS(tsStr);
			if (mStr != null) m_mmcm.setFromXmlString(mStr);
		}
	}
	private void checkSec(HashMap<ParametricModel, HashMap<String, ParametricModel>> m_secondaryModels, ParametricModel pm) {
		boolean idFound = false;
		for (HashMap<String, ParametricModel> sm : m_secondaryModels.values()) {
			for (String dep : sm.keySet()) {
				ParametricModel pms = sm.get(dep);
				if (pms.getEstModelId() == pm.getEstModelId()) {
					idFound = true;
					sm.put(dep, pm);
					break;
				}
			}
			if (idFound) break;
		}		
	}
	private void checkSec(HashMap<ParametricModel, HashMap<String, ParametricModel>> m_secondaryModels, int id, HashMap<String, Object[]> hm) {
		boolean idFound = false;
		for (HashMap<String, ParametricModel> sm : m_secondaryModels.values()) {
			for (ParametricModel pm : sm.values()) {
				if (pm.getEstModelId() == id) {
					idFound = true;
					for (String property : hm.keySet()) {
						Object[] o = hm.get(property);
	    		    	if (o != null) setValue(pm, property, o[0], o[1]);
					}
					break;
				}
			}
			if (idFound) break;
		}		
	}
	
	private HashMap<Integer, HashMap<String, Object[]>> getDiff(PmmXmlDoc inputDoc, PmmXmlDoc outputDoc) {
		HashMap<Integer, HashMap<String, Object[]>> result = new HashMap<>();
		   Javers javers = JaversBuilder.javers().build();

		   /*
		    Diff diff = javers.compare(inputDoc, outputDoc);
		    System.out.println(diff);
*/
		    for (PmmXmlElementConvertable out : outputDoc.getElementSet()) {
	    		if (out instanceof ParametricModel) {
	        		ParametricModel mOut = (ParametricModel) out;	 
	    	    	for (PmmXmlElementConvertable in : inputDoc.getElementSet()) {
	    	    		if (in instanceof ParametricModel) {
	    	        		ParametricModel mIn = (ParametricModel) in;	 
	    	        		if (mOut.getEstModelId() == mIn.getEstModelId()) {
	    	        		    Diff diff = javers.compare(mIn, mOut);
	    	        		    if (diff.getChanges().size() > 0) {
		    	        		    System.out.println(mOut.getEstModelId() + "\n" + diff);
		    	        		    for (ValueChange c : diff.getChangesByType(ValueChange.class)) {
		    	        		    	//System.out.println(c.getProperty().getName());
		    	        		    	//System.out.println(c.getLeft());
		    	        		    	//System.out.println(c.getRight());
		    	        		    	if (!result.containsKey(mOut.getEstModelId())) result.put(mOut.getEstModelId(), new HashMap<String, Object[]>());
		    	        		    	HashMap<String, Object[]> hm = result.get(mOut.getEstModelId());
		    	        		    	String gid = c.getAffectedGlobalId().value();
		    	        		    	if (gid.indexOf("#") > 0) gid = gid.substring(gid.indexOf("#") + 1) + "#"; // parameter#elementSet/2#
		    	        		    	else gid = "";
		    	        		    	hm.put(gid + c.getProperty().getName(), new Object[]{c.getLeft(),c.getRight()});
		    	        		    	/*
		    	        		    	System.out.println(c);
		    	        		    	System.out.println(mIn.getFittedModelName());
		    	        		    	setValue(mIn, c.getProperty().getName(), c.getRight());
		    	        		    	System.out.println(mIn.getFittedModelName());
		    	        		    	*/
		    	        		    }
		    	        		    for (NewObject c : diff.getChangesByType(NewObject.class)) {
		    	        		    	if (!result.containsKey(mOut.getEstModelId())) result.put(mOut.getEstModelId(), new HashMap<String, Object[]>());
		    	        		    	HashMap<String, Object[]> hm = result.get(mOut.getEstModelId());
		    	        		    	String gid = c.getAffectedGlobalId().value();
		    	        		    	if (gid.indexOf("#") > 0) gid = gid.substring(gid.indexOf("#") + 1) + "#"; // estLit#elementSet/0#
		    	        		    	else {
		    	        		    		System.err.println(gid);
		    	        		    		//gid = "";
		    	        		    	}
		    	        		    	hm.put(gid, new Object[]{null,c.getAffectedObject().get()});
		    	        		    }
		    	        		    for (ObjectRemoved c : diff.getChangesByType(ObjectRemoved.class)) {
		    	        		    	if (!result.containsKey(mOut.getEstModelId())) result.put(mOut.getEstModelId(), new HashMap<String, Object[]>());
		    	        		    	HashMap<String, Object[]> hm = result.get(mOut.getEstModelId());
		    	        		    	String gid = c.getAffectedGlobalId().value();
		    	        		    	if (gid.indexOf("#") > 0) gid = gid.substring(gid.indexOf("#") + 1) + "#"; // estLit#elementSet/0#
		    	        		    	else {
		    	        		    		System.err.println(gid);
		    	        		    		//gid = "";
		    	        		    	}
		    	        		    	hm.put(gid, new Object[]{c.getAffectedObject().get(), null});
		    	        		    }
	    	        		    }
	    	        			break;
	    	        		}
	    	    		}
	    	    	}
	    		}
	    	}
		   return result;
	}
	private void setValue(Object o, String fieldName, Object oldValue, Object newValue) {
		// todo: checken, ob "oldValue = oldValue"?
		try {
			if (fieldName.indexOf("#") >= 0) { // z.B. '#parameter#elementSet/2'
				String fn = fieldName.substring(0, fieldName.indexOf("#"));// "parameter"
				String param = fieldName.substring(fieldName.lastIndexOf("#") + 1); // max
				Field f = o.getClass().getDeclaredField(fn);	
				if (f.getType() == PmmXmlDoc.class) {
					f.setAccessible(true);
					PmmXmlDoc doc = (PmmXmlDoc) f.get(o);
					
					String indexS = fieldName.substring(fieldName.indexOf("#elementSet/") + "#elementSet/".length(), fieldName.lastIndexOf("#"));// 2
					Integer index = Integer.parseInt(indexS);
					PmmXmlElementConvertable pxec = (index < doc.getElementSet().size()) ? doc.get(index) : null;
					if (newValue == null && oldValue instanceof PmmXmlElementConvertable) { // ObjectRemoved
						PmmXmlElementConvertable pxecO = (PmmXmlElementConvertable) oldValue;
						Javers javers = JaversBuilder.javers().build();
						for (PmmXmlElementConvertable pxecL : doc.getElementSet()) {
							Diff diff = javers.compare(pxecO, pxecL);
							if (diff.getChanges().size() == 0) {
								doc.remove(pxecL);
								break;
							}
						}
					}
					else if (oldValue == null && newValue instanceof PmmXmlElementConvertable) { // NewObject
						doc.add((PmmXmlElementConvertable) newValue);
					}
					else {
						Field field = pxec.getClass().getDeclaredField(param);
						field.setAccessible(true);
						field.set(pxec, newValue);
					}
				}
				else if (f.getType() == DepXml.class) {
					f.setAccessible(true);
					DepXml dx = (DepXml) f.get(o);

					Field field = dx.getClass().getDeclaredField(param);
					field.setAccessible(true);
					field.set(dx, newValue);
				}
				else {
					System.out.println(f.getType());
				}
			}
			else {
				Field field = o.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(o, newValue);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}

