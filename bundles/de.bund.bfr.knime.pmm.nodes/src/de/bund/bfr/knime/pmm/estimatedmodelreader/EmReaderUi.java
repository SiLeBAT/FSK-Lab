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

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.*;
import javax.swing.border.*;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.ui.*;
import de.bund.bfr.knime.pmm.predictorview.PredictorViewNodeDialog;
import de.bund.bfr.knime.pmm.predictorview.SettingsHelper;
import de.bund.bfr.knime.pmm.predictorview.TableReader;
import de.bund.bfr.knime.pmm.timeseriesreader.*;

/**
 * @author Armin Weiser
 */
public class EmReaderUi extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7838365860944621209L;
	
	public static final String PARAM_PARAMETERS = "parameters";
	public static final String PARAM_PARAMETERNAME = "parameterName";
	public static final String PARAM_PARAMETERMIN = "parameterMin";
	public static final String PARAM_PARAMETERMAX = "parameterMax";
	public static final String PARAM_QUALITYMODE = "qualityFilterMode";
	public static final String PARAM_QUALITYTHRESH = "qualityThreshold";

	public static final String PARAM_NOMDDATA = "withoutMdData";
	
	private Bfrdb db;
	
	public static final int MODE_OFF = 0;
	public static final int MODE_R2 = 1;
	public static final int MODE_RMS = 2;
	
	private Double agentConc;
	private Double defTemp, defPh, defAw;
	private Callable<Void> refresher;
	
	private SettingsHelper set = null;
	
	public SettingsHelper getSet() {
		return set;
	}
	public EmReaderUi() {
		this(null);
	}
	public EmReaderUi(Bfrdb db) {
		this(db,null);
	}	
	public EmReaderUi(Bfrdb db, String[] itemListMisc) {								
		this(db,itemListMisc, true, true, true);
	}
	public EmReaderUi(Bfrdb db, String[] itemListMisc,
			boolean showModelOptions, boolean showQualityOptions, boolean showMDOptions) {		
		this.db = db;
		initComponents();		
		
		if (!showModelOptions) modelReaderUi.setVisible(false);
		if (!showQualityOptions) qualityPanel.setVisible(false);
		if (!showMDOptions) mdReaderUi.setVisible(false);
		set = new SettingsHelper();
	}
/*
	private String getWhereCondition(int level, String param, String param2, Double min, Double max) {
		String result =
				(min != null ? " AND (\"" + param + "\" >= " + min + " OR \"" + param + "\" IS NULL" +
						(level == 2 ? " OR POSITION_ARRAY('" + param2 + "' IN \"Independent2\") > 0 AND \"maxIndep2\"[POSITION_ARRAY('" + param2 + "' IN \"Independent2\")] >= " + min : "") +
						")" : "") +
		
				(max != null ? " AND (\"" + param + "\" <= " + max + " OR \"" + param + "\" IS NULL" +
						(level == 2 ? " OR POSITION_ARRAY('" + param2 + "' IN \"Independent2\") > 0 AND \"minIndep2\"[POSITION_ARRAY('" + param2 + "' IN \"Independent2\")] <= " + max : "") +
						")" : "");		
		return result;
	}
	*/
	private void getDataTable(Bfrdb db) {
		try {
			try {
				if (refresher != null) refresher.call();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
			
			//create table TTEST ("ID" INTEGER, "Referenz" INTEGER, "Agens" INTEGER, "AgensDetail" VARCHAR(255), "Matrix" INTEGER, "MatrixDetail" VARCHAR(255), "Temperatur" Double, "pH" Double, "aw" Double, "CO2" Double, "Druck" Double, "Luftfeuchtigkeit" Double, "Sonstiges" INTEGER, "Kommentar" VARCHAR(1023), "Guetescore" INTEGER, "Geprueft" BOOLEAN);
			HashMap<String, Integer> codeLength = new HashMap<>();
			codeLength.put("ADV", 4);
			codeLength.put("BLS", 3);
			codeLength.put("TOP", 4);
			codeLength.put("GS1", 3);
			codeLength.put("Combase", 5);
			codeLength.put("FA", 4);
			codeLength.put("SiLeBAT", 4);
			codeLength.put("Nährmedien", 13);
			codeLength.put("VET", 5);
			String where = " TRUE ";
			if (mdReaderUi.getAgentID() > 0) {
				String matchingIDs = "" + mdReaderUi.getAgentID();
				ResultSet rs = DBKernel.getResultSet(db.getConnection(), "SELECT \"CodeSystem\",\"Code\" FROM " + DBKernel.delimitL("Codes_Agenzien") +
						" WHERE \"Basis\" = " + mdReaderUi.getAgentID(), false);
				if (rs != null && rs.first()) {
					do {
						String cs = rs.getString("CodeSystem");
						if (cs != null && codeLength.containsKey(cs)) {
							int codeLen = codeLength.get(cs);
							String aCode = rs.getString("Code");
							if (aCode == null) aCode = "12345678901234";
							if (aCode.length() >= codeLen) {
								ResultSet rs2 = DBKernel.getResultSet(db.getConnection(), "SELECT \"Basis\" FROM " + DBKernel.delimitL("Codes_Agenzien") +
										" WHERE " + DBKernel.delimitL("CodeSystem") + "='" + rs.getString("CodeSystem") +
										"' AND LEFT(" + DBKernel.delimitL("Code") + "," + codeLen + ") = '" + aCode.substring(0, codeLen) + "'", false);
								if (rs2 != null && rs2.first()) {
									do {
										matchingIDs += "," + rs2.getInt("Basis");
									} while (rs2.next());
								}							
							}
						}
					} while (rs.next());
				}

				where += " AND (\"Agens\" IS NULL OR \"Agens\" IN (" + matchingIDs + "))";
			}

			codeLength.put("ADV", 5);
			if (mdReaderUi.getMatrixID() > 0) {
				String matchingIDs = "" + mdReaderUi.getMatrixID();
				ResultSet rs = DBKernel.getResultSet(db.getConnection(), "SELECT \"CodeSystem\",\"Code\" FROM " + DBKernel.delimitL("Codes_Matrices") +
						" WHERE \"Basis\" = " + mdReaderUi.getMatrixID(), false);
				if (rs != null && rs.first()) {
					do {
						String cs = rs.getString("CodeSystem");
						if (cs != null && codeLength.containsKey(cs)) {
							int codeLen = codeLength.get(cs);
							String mCode = rs.getString("Code");
							if (mCode == null) mCode = "12345678901234";
							if (mCode.length() >= codeLen) {
								ResultSet rs2 = DBKernel.getResultSet(db.getConnection(), "SELECT \"Basis\" FROM " + DBKernel.delimitL("Codes_Matrices") +
										" WHERE " + DBKernel.delimitL("CodeSystem") + "='" + rs.getString("CodeSystem") +
										"' AND LEFT(" + DBKernel.delimitL("Code") + "," + codeLen + ") = '" + mCode.substring(0, codeLen) + "'", false);
								if (rs2 != null && rs2.first()) {
									do {
										matchingIDs += "," + rs2.getInt("Basis");
									} while (rs2.next());
								}							
							}
						}
					} while (rs.next());
				}

				where += " AND (\"Matrix\" IS NULL OR \"Matrix\" IN (" + matchingIDs + "))";
			}
			
    		int level = modelReaderUi.getLevel();
			LinkedHashMap<String, Double[]> parameter = new LinkedHashMap<>();
			LinkedHashMap<String, DoubleTextField[]> params = mdReaderUi.getParameter();
			for (String key : params.keySet()) {
				DoubleTextField[] dtf = params.get(key);
    			Double[] dbl = new Double[2];
    			dbl[0] = dtf[0].getValue();
    			dbl[1] = dtf[1].getValue();
    			parameter.put(key, dbl);				
			}
    		/*
			for (String key : params.keySet()) {
				DoubleTextField[] dtf = params.get(key);
				if (key.equals(AttributeUtilities.ATT_TEMPERATURE)) {
		    		if (defTemp != null) set.getParamXValues().put(AttributeUtilities.ATT_TEMPERATURE, defTemp); // temp
					where += getWhereCondition(level, "Temperatur", AttributeUtilities.ATT_TEMPERATURE, dtf[0].getValue(), dtf[1].getValue());							
				}
				else if (key.equals(AttributeUtilities.ATT_PH)) {
		    		if (defPh != null) set.getParamXValues().put(AttributeUtilities.ATT_PH, defPh);
					where += getWhereCondition(level, "pH", "pH", dtf[0].getValue(), dtf[1].getValue());
				}
				else if (key.equals(AttributeUtilities.ATT_AW)) {
		    		if (defAw != null) set.getParamXValues().put(AttributeUtilities.ATT_AW, defAw);
					where += getWhereCondition(level, "aw", "aw", dtf[0].getValue(), dtf[1].getValue());
				}
			}
    		if (agentConc != null && set.getNewConcentrationParameters() != null) {
    			for (String val : set.getNewConcentrationParameters().values()) {
    				set.getParamXValues().put(val, agentConc);
    			}
    		}
    		*/
			// SELECT "Independent2","minIndep2","maxIndep2" FROM "CACHE_selectEstModel2"  WHERE (POSITION_ARRAY('Temperature' IN "Independent2") > 0 AND "maxIndep2"[POSITION_ARRAY('Temperature' IN "Independent2")] >= 10.0) AND (POSITION_ARRAY('Temperature' IN "Independent2") > 0 AND "minIndep2"[POSITION_ARRAY('Temperature' IN "Independent2")] <= 30.0)
	    	try {
	    		boolean withoutMdData = withoutData.isSelected();
	    		List<KnimeTuple> hs = null;
				try {
					hs = EstimatedModelReaderNodeModel.getKnimeTuples(db, db.getConnection(),
							EstimatedModelReaderNodeModel.createSchema(withoutMdData, level), level, withoutMdData,
							getQualityMode(), getQualityThresh(), mdReaderUi.getMatrixString(), mdReaderUi.getAgentString(), mdReaderUi.getLiteratureString(),
							mdReaderUi.getMatrixID(), mdReaderUi.getAgentID(), mdReaderUi.getLiteratureID(), parameter,
							modelReaderUi.isModelFilterEnabled(), modelReaderUi.getModelList(), where, null);
				} catch (PmmException e) {
					e.printStackTrace();
				} catch (InvalidSettingsException e) {
					e.printStackTrace();
				}

	    		Frame parentFrame = JOptionPane.getFrameForComponent(this);
		    	if (hs != null && hs.size() > 0) {
		    		PredictorViewNodeDialog pvnd = new PredictorViewNodeDialog(hs, set, false, true);		    		
		    		TableReader tr = pvnd.getReader();
		    		//LinkedHashMap<String, DoubleTextField[]> params = mdReaderUi.getParameter();
					//DoubleTextField[] dtf = params.get(AttributeUtilities.ATT_TEMPERATURE);
		    		for (String s : tr.getTempParam().values()) {
			    		if (defTemp != null && !defTemp.isNaN()) set.getParamXValues().put(s, defTemp);
						//if (dtf != null) where += getWhereCondition(level, "Temperatur", s, dtf[0].getValue(), dtf[1].getValue());							
		    		}
					//dtf = params.get(AttributeUtilities.ATT_AW);
		    		for (String s : tr.getAwParam().values()) {
			    		if (defAw != null && !defAw.isNaN()) set.getParamXValues().put(s, defAw);
						//if (dtf != null) where += getWhereCondition(level, "aw", s, dtf[0].getValue(), dtf[1].getValue());							
		    		}
					//dtf = params.get(AttributeUtilities.ATT_PH);
		    		for (String s : tr.getPhParam().values()) {
			    		if (defPh != null && !defPh.isNaN()) set.getParamXValues().put(s, defPh);
						//if (dtf != null) where += getWhereCondition(level, "pH", s, dtf[0].getValue(), dtf[1].getValue());							
		    		}
		    		/*
					try {
						hs = EstimatedModelReaderNodeModel.getKnimeTuples(db, db.getConnection(),
								EstimatedModelReaderNodeModel.createSchema(withoutMdData, level), level, withoutMdData,
								getQualityMode(), getQualityThresh(), mdReaderUi.getMatrixString(), mdReaderUi.getAgentString(), mdReaderUi.getLiteratureString(),
								mdReaderUi.getMatrixID(), mdReaderUi.getAgentID(), mdReaderUi.getLiteratureID(), parameter,
								modelReaderUi.isModelFilterEnabled(), modelReaderUi.getModelList(), where, null);
					} catch (PmmException e) {
						e.printStackTrace();
					} catch (InvalidSettingsException e) {
						e.printStackTrace();
					}
					*/
					//if (hs != null && hs.size() > 0) {
						insertInitVals();
			    		pvnd = new PredictorViewNodeDialog(hs, set, false, true);
					
			    		JPanel mainComponent = pvnd.getMainComponent();
	
			    		JDialog dialog = new JDialog(parentFrame);
			    		dialog.setLayout(new BorderLayout());
			    		dialog.setModal(true);
			    		dialog.add(mainComponent, BorderLayout.CENTER);
			    		dialog.add(getOkApplyCancelPanel(dialog, pvnd), BorderLayout.SOUTH);
			    		dialog.pack();
			    		centerOnScreen(dialog, true, true);
			    		dialog.pack();
			    			
			    		dialog.setVisible(true);		
			    		
					//}
		    	}
		    	else {
		  		    JOptionPane.showMessageDialog(parentFrame, "No models found for defined filter!", "No models found", JOptionPane.INFORMATION_MESSAGE);
		    	}
			}
	    	catch (SQLException e) {
				e.printStackTrace();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void insertInitVals() {
		if (agentConc != null && set.getNewConcentrationParameters() != null) {
			for (String val : set.getNewConcentrationParameters().values()) {
				set.getParamXValues().put(val, agentConc);
			}
		}
		/*
		if (lag != null && set.getLagParameters() != null) {
			for (String val : set.getLagParameters().values()) {
				set.getParamXValues().put(val, lag);
			}
		}
		*/		
	}
	private void setSelectedFilterResults() {
		String labelText = "<html>";
		List<KnimeTuple> kts = set.getSelectedTuples();
		for (KnimeTuple kt : kts) {
			if (kt != null) {
				PmmXmlDoc estModel = kt.getPmmXml(Model1Schema.ATT_ESTMODEL);
				if (estModel != null) {
					for (PmmXmlElementConvertable el : estModel.getElementSet()) {
						if (el instanceof EstModelXml) {
							EstModelXml emx = (EstModelXml) el;
							labelText += emx.getId() + " -> " + emx.getName() + "<br>";
							break;
						}
					}
				}
			}
		}
		labelText += "</html>";
		selectedFilterResults.setText(labelText); // "<html>Hello World!<br>blahblahblah</html>"
	}
	private void centerOnScreen(final Component c, final boolean absolute, final boolean reduceSize) {
	    final int width = c.getWidth();
	    int height = c.getHeight();
	    if (reduceSize) {
	    	height -= 100;
		    c.setPreferredSize(new Dimension(width, height));	    	
	    }
	    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screenSize.width / 2) - (width / 2);
	    int y = (screenSize.height / 2) - (height / 2);
	    if (!absolute) {
	        x /= 2;
	        y /= 2;
	    }
	    c.setLocation(x, y);
	}
	private void qualityButtonActionPerformed(ActionEvent e) {
		if (qualityButtonNone.isSelected()) qualityField.setEnabled(false);
		else qualityField.setEnabled(true);
	}

	private void doFilterActionPerformed(ActionEvent e) {
		if (getLevel() < 3) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			getDataTable(db);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		else {
    		Frame parentFrame = JOptionPane.getFrameForComponent(this);
  		    JOptionPane.showMessageDialog(parentFrame, "Not possible for Secondary Models", "Impossible for Secondary Models", JOptionPane.INFORMATION_MESSAGE);			
		}
	}
	public void addModelPrim(final int id, final String name, final String modelType, boolean visible) throws PmmException {
		modelReaderUi.addModelPrim(id, name, modelType, visible);
	}
	
	public void addModelSec(final int id, final String name, final String modelType, boolean visible) throws PmmException {
		modelReaderUi.addModelSec(id, name, modelType, visible);
	}
	
	public void setMatrixID(Integer id) throws InvalidSettingsException {
		mdReaderUi.setMatrixID(id);
	}
	public void setAgentID(Integer id) throws InvalidSettingsException {
		mdReaderUi.setAgensID(id);
	}
	public void setLiteratureID(Integer id) throws InvalidSettingsException {
		mdReaderUi.setLiteratureID(id);
	}
	public void setMatrixString( final String str ) throws InvalidSettingsException {
		mdReaderUi.setMatrixString(str);
	}
	public void setAgentString( final String str ) throws InvalidSettingsException {
		mdReaderUi.setAgentString(str);
	}
	public void setLiteratureString( final String str ) throws InvalidSettingsException {
		mdReaderUi.setLiteratureString(str);
	}
	public void setParameter(LinkedHashMap<String, DoubleTextField[]> params) {
		mdReaderUi.setParameter(params);
	}
	public void clearModelSet() { modelReaderUi.clearModelSet(); }
	public void enableModelList( int[] idList ) { modelReaderUi.enableModelList( idList ); }
	public String getAgentString() { return mdReaderUi.getAgentString(); }
	public Integer getAgentID() {return mdReaderUi.getAgentID();}
	public Integer getMatrixID() {return mdReaderUi.getMatrixID();}
	public Integer getLiteratureID() {return mdReaderUi.getLiteratureID();}
	public String getLiteratureString() { return mdReaderUi.getLiteratureString(); }
	public LinkedHashMap<String, DoubleTextField[]> getParameter() { return mdReaderUi.getParameter(); }
	public int getLevel() { return modelReaderUi.getLevel(); }
	public String getModelClass() { return modelReaderUi.getModelClass(); }
	public String getMatrixString() { return mdReaderUi.getMatrixString(); }
	public int[] getModelList() { return modelReaderUi.getModelList(); }
	
	public void setMiscItems(String[] itemListMisc) {
		mdReaderUi.setMiscItems(itemListMisc);
	}
	public double getQualityThresh() throws InvalidSettingsException {
		
		if( !qualityField.isValueValid() )
			throw new InvalidSettingsException( "Threshold quality invalid." );
		
		return qualityField.getValue();
	}
	
	public int getQualityMode() {
		
		if( qualityButtonNone.isSelected() )
			return MODE_OFF;
		
		if( qualityButtonRms.isSelected() )
			return MODE_RMS;
		
		return MODE_R2;
	}
	
	public boolean isModelFilterEnabled() { return modelReaderUi.isModelFilterEnabled(); }
	
	public void setLevel( int level ) throws PmmException { modelReaderUi.setLevel( level ); }
	public void setModelClass( String modelClass ) throws PmmException { modelReaderUi.setModelClass( modelClass ); }
	public void setModelFilterEnabled( boolean en ) { modelReaderUi.setModelFilterEnabled( en ); }
	
	public void setQualityMode( final int mode ) throws PmmException {
		
		
		switch( mode ) {
		
			case MODE_OFF :
				qualityButtonNone.setSelected( true );
				qualityField.setEnabled( false );
				break;
				
			case MODE_R2 :
				qualityButtonR2.setSelected( true );
				qualityField.setEnabled( true );
				break;
				
			case MODE_RMS :
				qualityButtonRms.setSelected( true );
				qualityField.setEnabled( true );
				break;
		
			default :
				throw new PmmException( "Invalid quality filter mode." );
		}
	}
	
	public void setQualityThresh( final double thresh ) {
		qualityField.setText( String.valueOf( thresh ) );
	}
	
    public static boolean passesFilter(
    		final int level,
    		final int qualityMode,
    		final double qualityThresh,
    		final String matrixString,
    		final String agentString,
    		final String literatureString,
    		int matrixID, int agentID, int literatureID,
    		final LinkedHashMap<String, Double[]> parameter,
    		boolean modelFilterEnabled,
    		final int[] modelList,
    		final KnimeTuple tuple )
    throws PmmException {

		if (!MdReaderUi.passesFilter(matrixString, agentString, null, matrixID, agentID, 0, parameter, tuple)) {
			return false;
		}    	
		if (!ModelReaderUi.passesFilter(literatureString, literatureID, tuple, level) && !MdReaderUi.passesFilter(literatureString, literatureID, tuple)) {
			return false;
		}    	
    	if (modelFilterEnabled && !ModelReaderUi.passesFilter(modelList, tuple, level)) {
    		return false;
    	}
        	
		PmmXmlDoc x = tuple.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_ESTMODEL, level));
		EstModelXml emx = null;
		if (x != null) {
			for (PmmXmlElementConvertable el : x.getElementSet()) {
				if (el instanceof EstModelXml) {
					emx = (EstModelXml) el;
					break;
				}
			}
		}

		switch( qualityMode ) {
    	
    		case MODE_OFF :
    			return true;
    			
    		case MODE_RMS :    			
    			if (emx != null && emx.getRms() <= qualityThresh) return true;    			
    			else return false;
    			
    		case MODE_R2 :    			
    			if (emx != null && emx.getR2() != null && emx.getR2() >= qualityThresh) return true;    			
    			else return false;
    			
    		default :
    			throw new PmmException( "Unrecognized Quality Filter mode." );
    	}
    }
    
    public void saveSettingsTo(Config c) {
    	if (set != null) set.saveConfig(c.addConfig("PredictorSettings"));
    	
     	modelReaderUi.saveSettingsTo(c.addConfig("ModelReaderUi"));
     	mdReaderUi.saveSettingsTo(c.addConfig("MdReaderUi"));
    	
    	c.addInt( EmReaderUi.PARAM_QUALITYMODE, this.getQualityMode() );
    	c.addDouble( EmReaderUi.PARAM_QUALITYTHRESH, qualityField.getValue());
    	
    	c.addBoolean(PARAM_NOMDDATA, withoutData.isSelected());
    	    	
		Config c2 = c.addConfig(EstimatedModelReaderNodeModel.PARAM_PARAMETERS);
    	LinkedHashMap<String, DoubleTextField[]> params = this.getParameter();
    	if (params != null && params.size() > 0) {
    		String[] pars = new String[params.size()];
    		String[] mins = new String[params.size()];
    		String[] maxs = new String[params.size()];
    		int i=0;
    		for (String par : params.keySet()) {
    			DoubleTextField[] dbl = params.get(par);
    			pars[i] = par;
    			mins[i] = ""+dbl[0].getValue();
    			maxs[i] = ""+dbl[1].getValue();
    			i++;
    		}
    		c2.addStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERNAME, pars);
    		c2.addStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERMIN, mins);
    		c2.addStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERMAX, maxs);
    	}
    }	
	public void refreshParamSettings(Double defTemp, Double defPh, Double defAw) {
    	this.defTemp = defTemp;
    	this.defPh = defPh;
    	this.defAw = defAw;
	}
	public void setSettings(Config c) throws InvalidSettingsException {		
		setSettings(c, null, null, null, null, null, null, null);
	}
	public void setSettings(Config c, Integer defAgent, Integer defMatrix, Double defTemp, Double defPh, Double defAw, Double agentConc, Callable<Void> refresher) throws InvalidSettingsException {
    	this.defTemp = defTemp;
    	this.defPh = defPh;
    	this.defAw = defAw;
    	this.agentConc = agentConc;
    	this.refresher = refresher;
		LinkedHashMap<String, DoubleTextField[]> params = new LinkedHashMap<>();
		if (c != null) {
			if (c.containsKey("PredictorSettings")) {
				set = new SettingsHelper();
				set.loadConfig(c.getConfig("PredictorSettings"));
				setSelectedFilterResults();
			}

			modelReaderUi.setLevel(1);
	     	if (c.containsKey("ModelReaderUi")) modelReaderUi.setSettings(c.getConfig("ModelReaderUi"));
	     	if (c.containsKey("MdReaderUi")) mdReaderUi.setSettings(c.getConfig("MdReaderUi"));

			this.setQualityMode(c.getInt(EmReaderUi.PARAM_QUALITYMODE));
			this.setQualityThresh(c.getDouble(EmReaderUi.PARAM_QUALITYTHRESH));

			if (c.containsKey(PARAM_NOMDDATA)) withoutData.setSelected(c.getBoolean(PARAM_NOMDDATA));
			    				
			Config c2 = c.getConfig(EmReaderUi.PARAM_PARAMETERS);
			if (c2.containsKey(EmReaderUi.PARAM_PARAMETERNAME)) {
	    		String[] pars = c2.getStringArray(EmReaderUi.PARAM_PARAMETERNAME);
	    		String[] mins = c2.getStringArray(EmReaderUi.PARAM_PARAMETERMIN);
	    		String[] maxs = c2.getStringArray(EmReaderUi.PARAM_PARAMETERMAX);

	    		for (int i=0;i<pars.length;i++) {
	    			DoubleTextField[] dbl = new DoubleTextField[2];
	    			dbl[0] = new DoubleTextField(true);
	    			dbl[1] = new DoubleTextField(true);
	    			if (!mins[i].equals("null")) dbl[0].setValue(Double.parseDouble(mins[i]));
	    			if (!maxs[i].equals("null")) dbl[1].setValue(Double.parseDouble(maxs[i]));
	    			params.put(pars[i], dbl);
	    		}
			}
		}
		if (params.size() == 0) fillWithDefaults(defAgent, defMatrix, defTemp, defPh, defAw, params);     		
		this.setParameter(params);     		
	}
	private void fillWithDefaults(Integer defAgent, Integer defMatrix, Double defTemp, Double defPh, Double defAw, LinkedHashMap<String, DoubleTextField[]> params) throws InvalidSettingsException {
		if (defAgent != null) {
			//c.getConfig("MdReaderUi").addInt(MdReaderUi.PARAM_AGENTID, defAgent);
			mdReaderUi.setAgensID(defAgent);
			mdReaderUi.setAgentString(""+DBKernel.getValue(db.getConnection(), "Agenzien", "ID", defAgent+"", "Agensname"));
		}
		if (defMatrix != null) {
			//c.getConfig("MdReaderUi").addInt(MdReaderUi.PARAM_MATRIXID, defMatrix);
			mdReaderUi.setMatrixID(defMatrix);
			mdReaderUi.setMatrixString(""+DBKernel.getValue(db.getConnection(), "Matrices", "ID", defMatrix+"", "Matrixname"));
		}
		if (defTemp != null && !defTemp.isNaN() && !defTemp.isInfinite()) {
			DoubleTextField[] dtf = params.get(AttributeUtilities.ATT_TEMPERATURE);
			if (dtf == null) {
				dtf = new DoubleTextField[2];
				dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
				params.put(AttributeUtilities.ATT_TEMPERATURE, dtf);
			}
			dtf[0].setValue(defTemp - 10);
			dtf[1].setValue(defTemp + 10);
		}
		if (defPh != null && !defPh.isNaN() && !defPh.isInfinite()) {
			DoubleTextField[] dtf = params.get(AttributeUtilities.ATT_PH);
			if (dtf == null) {
				dtf = new DoubleTextField[2];
				dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
				params.put(AttributeUtilities.ATT_PH, dtf);
			}
			dtf[0].setValue(defPh - 1);
			dtf[1].setValue(defPh + 1);
		}
		if (defAw != null && !defAw.isNaN() && !defAw.isInfinite()) {
			DoubleTextField[] dtf = params.get(AttributeUtilities.ATT_AW);
			if (dtf == null) {
				dtf = new DoubleTextField[2];
				dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
				params.put(AttributeUtilities.ATT_AW, dtf);
			}
			dtf[0].setValue(defAw - 0.1);
			dtf[1].setValue(defAw + 0.1);
		}
	}
	private JPanel getOkApplyCancelPanel(final JDialog dialog, final PredictorViewNodeDialog pvnd) {
		JPanel panel = new JPanel();
        JButton okButton = new JButton("Ok");
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
	    		set = pvnd.getSettings();
	    		setSelectedFilterResults();
	    		dialog.dispose();
            }
        });            
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
	    		set = pvnd.getSettings();
	    		insertInitVals();
	    		pvnd.getChartAllPanel().getConfigPanel().setParamXValues(set.getParamXValues());
	    		setSelectedFilterResults();
            }
        });            
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	dialog.dispose();
            }
        });
        panel.setSize(300,130);
        panel.setLayout(new FlowLayout());
        panel.add(okButton);       
        panel.add(applyButton);       
        panel.add(cancelButton);
        return panel;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		withoutData = new JCheckBox();
		modelReaderUi = new ModelReaderUi(true);
		qualityPanel = new JPanel();
		qualityButtonNone = new JRadioButton();
		qualityButtonRms = new JRadioButton();
		qualityButtonR2 = new JRadioButton();
		label3 = new JLabel();
		qualityField = new DoubleTextField();
		mdReaderUi = new MdReaderUi();
		panel6 = new JPanel();
		doFilter = new JButton();
		selectedFilterResults = new JLabel();

		//======== this ========
		setLayout(new FormLayout(
			"default:grow",
			"default, 3*($lgap, fill:default), $lgap, fill:default:grow"));

		//---- withoutData ----
		withoutData.setText("Load models without associated microbial data");
		add(withoutData, CC.xy(1, 1));
		add(modelReaderUi, CC.xy(1, 3));

		//======== qualityPanel ========
		{
			qualityPanel.setBorder(new TitledBorder("Estimation Quality"));
			qualityPanel.setLayout(new FormLayout(
				"4*(default, $lcgap), default:grow",
				"default"));

			//---- qualityButtonNone ----
			qualityButtonNone.setText("Do not filter");
			qualityButtonNone.setSelected(true);
			qualityButtonNone.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					qualityButtonActionPerformed(e);
				}
			});
			qualityPanel.add(qualityButtonNone, CC.xy(1, 1));

			//---- qualityButtonRms ----
			qualityButtonRms.setText("Filter by RMS");
			qualityButtonRms.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					qualityButtonActionPerformed(e);
				}
			});
			qualityPanel.add(qualityButtonRms, CC.xy(3, 1));

			//---- qualityButtonR2 ----
			qualityButtonR2.setText("Filter by R squared");
			qualityButtonR2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					qualityButtonActionPerformed(e);
				}
			});
			qualityPanel.add(qualityButtonR2, CC.xy(5, 1));

			//---- label3 ----
			label3.setText("Quality threshold:");
			qualityPanel.add(label3, CC.xy(7, 1));

			//---- qualityField ----
			qualityField.setValue(0.8);
			qualityField.setEnabled(false);
			qualityPanel.add(qualityField, CC.xy(9, 1));
		}
		add(qualityPanel, CC.xy(1, 5));
		add(mdReaderUi, CC.xy(1, 7));

		//======== panel6 ========
		{
			panel6.setBorder(new TitledBorder("Results"));
			panel6.setLayout(new FormLayout(
				"default:grow",
				"default, $lgap, default"));

			//---- doFilter ----
			doFilter.setText("Select Models");
			doFilter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doFilterActionPerformed(e);
				}
			});
			panel6.add(doFilter, CC.xy(1, 1));
			panel6.add(selectedFilterResults, CC.xy(1, 3));
		}
		add(panel6, CC.xy(1, 9));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(qualityButtonNone);
		buttonGroup1.add(qualityButtonRms);
		buttonGroup1.add(qualityButtonR2);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JCheckBox withoutData;
	private ModelReaderUi modelReaderUi;
	private JPanel qualityPanel;
	private JRadioButton qualityButtonNone;
	private JRadioButton qualityButtonRms;
	private JRadioButton qualityButtonR2;
	private JLabel label3;
	private DoubleTextField qualityField;
	private MdReaderUi mdReaderUi;
	private JPanel panel6;
	private JButton doFilter;
	private JLabel selectedFilterResults;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
