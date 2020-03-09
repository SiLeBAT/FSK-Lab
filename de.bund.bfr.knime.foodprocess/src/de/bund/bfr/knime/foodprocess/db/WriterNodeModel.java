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
package de.bund.bfr.knime.foodprocess.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.node.workflow.ConnectionContainer;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowManager;

import de.bund.bfr.knime.foodprocess.FoodProcessNodeDialog;
import de.bund.bfr.knime.foodprocess.addons.AddonC;
import de.bund.bfr.knime.foodprocess.addons.AddonNodeDialog;
import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;
import de.bund.bfr.knime.foodprocess.lib.ParametersSetting;
import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;
import de.bund.bfr.knime.util.Matrix;


/**
 * This is the model implementation of Writer.
 * 
 *
 * @author BfR
 */
public class WriterNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected WriterNodeModel() {
        super(new PortType[] {PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class, false)}, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final PortObject[] inObjects,
            final ExecutionContext exec) throws Exception {

    	saveWF();
        return new BufferedDataTable[]{};
    }
    private void updateDB(String tablename, String[] fields, String[] vals, Integer id) {
    	String pstr = "";
    	for (int i = 0 ; i < fields.length ; i++) {
    		pstr += "," + DBKernel.delimitL(fields[i]) + "='" + vals[i] + "'";
    	}
    	if (pstr.length() > 0) pstr = pstr.substring(1);
    	String sql = "UPDATE " + DBKernel.delimitL(tablename) + " SET " + pstr + " WHERE " + DBKernel.delimitL("ID") + "=" + id;
    	DBKernel.sendRequest(sql, false);
    }
    private Integer insert2DB(String tablename, String[] fields, String[] vals) {
    	Integer resultID = null;
    	String fieldstr = "";
    	String valstr = "";
    	for (int i = 0 ; i < fields.length ; i++) {
    		if (vals[i] != null && !vals[i].equalsIgnoreCase("null")) {
        		fieldstr += "," + DBKernel.delimitL(fields[i]);
        		valstr += "," + "'" + vals[i] + "'";    			
    		}
    	}
    	if (fieldstr.length() > 0) fieldstr = fieldstr.substring(1);
    	if (valstr.length() > 0) {
    		valstr = valstr.substring(1);
    		try {
    	    	Connection conn = DBKernel.getLocalConn(false);
    			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + DBKernel.delimitL(tablename) + " (" + fieldstr + ") VALUES (" + valstr + ")", Statement.RETURN_GENERATED_KEYS);
    			if (ps.executeUpdate() > 0) {
    				ResultSet rs = ps.getGeneratedKeys();
    				if (rs.next()) {
    					resultID = rs.getInt(1);
    				} else {
    					System.err.println("getGeneratedKeys failed!\n" + ps);
    				}
    				rs.close();
    			}
    			ps.close();
    		} catch (SQLException e) {
    			System.err.println(fieldstr + "\t" + valstr);
    			e.printStackTrace();
    		}
    	}
		return resultID;
    }
	private void saveWF() throws Exception {
		for (NodeContainer nc : WorkflowManager.ROOT.getNodeContainers()) {
			if (nc instanceof WorkflowManager) {
				WorkflowManager wfm = (WorkflowManager) nc;
				for (WriterNodeModel m : wfm.findNodes(WriterNodeModel.class, true).values()) {
					if (m == this) {
						Integer wfID = insert2DB("ProzessWorkflow", new String[]{"Name"}, new String[]{wfm.getName()});
						if (wfID != null) saveNodes(wfm, wfID);
						else this.setWarningMessage("Saving Workflow into database failed1!");
						break;
					}
				}
			}
		}
	}
	private void saveNodes(WorkflowManager parent, Integer wfID) {
		/*
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("ProzessElemente") + " () VALUES ()", false);
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Matrices") + " () VALUES ()", false);
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Zutatendaten") + " () VALUES ()", false);
		*/
		HashMap<NodeID, Integer> hm = new HashMap<NodeID, Integer>(); 
		HashMap<NodeID, HashSet<Integer>> hma = new HashMap<NodeID, HashSet<Integer>>(); 
		HashMap<NodeID, List<Integer>> hmb = new HashMap<NodeID, List<Integer>>(); 
		for (NodeContainer ncs : parent.getNodeContainers()) {
			try {
				NodeDialogPane ndp = ncs.getDialogPaneWithSettings();
				if (ndp instanceof FoodProcessNodeDialog) {
					FoodProcessNodeDialog fpnd = (FoodProcessNodeDialog) ndp;
					FoodProcessSetting fps = fpnd.getSettings().getFoodProcessSetting();
					System.err.println(fps.getProcessName() + "\t" + fps.getDuration() + "\t" + ncs.getID());
				    Integer dID = insertDblKZ(fps.getDuration()+"");
					//Integer dID = insert2DB("DoubleKennzahlen", new String[]{"Wert"}, new String[]{""+fps.getDuration()});
					String du = fps.getDurationUnit();
					String duu = "";
				    if (du != null) {
				    	duu = du.equals("h") ? "Stunde" : du.equals("s") ? "Sekunde" :
					    	du.equals("d") ? "Tag" : du.equals("min") ? "Minute" : "";
				    }
				    Integer tID = insertDblKZ(getTemperature(fps.getParametersSetting()));
				    Integer phID = insertDblKZ(fps.getParametersSetting().getPh());
				    Integer awID = insertDblKZ(fps.getParametersSetting().getAw());
					//Integer phID = insert2DB("DoubleKennzahlen", new String[]{"Wert"}, new String[]{""+DBKernel.getDoubleStr(fps.getParametersSetting().getPh())});
					//Integer awID = insert2DB("DoubleKennzahlen", new String[]{"Wert"}, new String[]{""+DBKernel.getDoubleStr(fps.getParametersSetting().getAw())});
				    Integer prID = insertDblKZ(getPressure(fps.getParametersSetting()));
					Integer retID = insert2DB("Prozessdaten", new String[]{"Workflow","ProzessDetail","Dauer","DauerEinheit","Temperatur","pH","aw","Druck"}, new String[]{wfID+"",fps.getProcessName(),dID+"",duu,tID+"",phID+"",awID+"",prID+""});
					//fps.getParametersSetting().getVolume(); fps.getParametersSetting().getVolumeUnit();
					
					hm.put(ncs.getID(), retID);
					
					List<Integer> hs = new ArrayList<Integer>(); 
					for (int i=0;i<4;i++) {
						Matrix m = fps.getOutPortSetting()[i].getMatrix();
						Double flout = fps.getOutPortSetting()[i].getOutFlux();			
						if (m != null && flout != null && flout > 0) {
						    int unit = 24; // 24=Prozent
						    Integer volID = insertDblKZ(flout+"");
							Integer retpID = insert2DB("Zutatendaten", new String[]{"Prozessdaten","Zutat_Produkt","Unitmenge","UnitEinheit","Matrix","MatrixDetail"}, new String[]{retID+"","Produkt",volID+"",unit+"",m.getId()+"",m.getName()});
							hs.add(retpID);
						}
					}
					hmb.put(ncs.getID(), hs);
				}
				else if (ndp instanceof AddonNodeDialog) {
					AddonNodeDialog and = (AddonNodeDialog) ndp;
					AddonC ac = and.getSettingsPanel();
					double[] volumeDef = ac.getVolumeDef();
					if (volumeDef != null) {
						String[] volumeDefUnit = ac.getVolumeUnitDef();
						int[] iarr = ac.getIArr();
						String[]  narr = ac.getNArr();
						System.err.print(ncs.getName() + ":");
						if (ncs.getName().equals("Ingredients")) {
							HashSet<Integer> hs = new HashSet<Integer>(); 
							for (int i=0;i<volumeDef.length;i++) {
								System.err.println(iarr[i] + "_" + narr[i] + "\t" + volumeDef[i] + "\t" + volumeDefUnit[i]);
							    int unit = 2; // gramm							    
							    Integer volID = insertDblKZ(((volumeDefUnit[i].equals("kg") ? 1000 : volumeDefUnit[i].equals("t") ? 1000000 : 1) * volumeDef[i])+"");
								Integer retID = insert2DB("Zutatendaten", new String[]{"Zutat_Produkt","Unitmenge","UnitEinheit","Matrix","MatrixDetail"}, new String[]{"Zutat",volID+"",unit+"",iarr[i]+"",narr[i]+""});
								hs.add(retID);
							}
							hma.put(ncs.getID(), hs);
						}
						else if (ncs.getName().equals("Agents")) {
						}
						else {
							System.err.println("Hääähh???");
						}
			    	}
				}
			}
			catch (NotConfigurableException e) {
				e.printStackTrace();
			}
			if (ncs instanceof WorkflowManager) { // Metaknoten...
				saveNodes((WorkflowManager) ncs, wfID);
				System.err.println("still to handle...");
			}
		}
		for (ConnectionContainer ccs : parent.getConnectionContainers()) {
			if (hm.containsKey(ccs.getSource()) && hm.containsKey(ccs.getDest())) {
				insert2DB("Prozess_Verbindungen", new String[]{"Ausgangsprozess","Zielprozess"}, new String[]{hm.get(ccs.getSource())+"",hm.get(ccs.getDest())+""});
			}
			else if (hma.containsKey(ccs.getSource()) && hm.containsKey(ccs.getDest())) {
				HashSet<Integer> hs = hma.get(ccs.getSource());
				for (int i : hs) {
					updateDB("Zutatendaten", new String[]{"Prozessdaten"}, new String[]{hm.get(ccs.getDest())+""}, i);
				}
			}
			else {
				System.err.println("WSDW???\t" + ccs.getSource() + " -> " + ccs.getDest());
				System.err.println();
			}
		}
	}
	private Integer insertDblKZ(String value) {
		Integer result = null;
		if (value != null && !value.trim().isEmpty()) {
			if (value.indexOf(";") >= 0) {
				result = insert2DB("DoubleKennzahlen", new String[]{"Funktion (Zeit)"}, new String[]{value});
			}
			else {
				result = insert2DB("DoubleKennzahlen", new String[]{"Wert"}, new String[]{value});
			}			
		}
		return result;
			
	}
    private String getPressure(ParametersSetting ps) {
    	String result = ps.getPressure();
    	try {
	    	result = ps.getPressure() == null || ps.getPressure().isEmpty() ?
	    			null : 
	    				""+convert(Categories.getCategory("Pressure"), ps.getPressureUnit(), Double.parseDouble(ps.getPressure()), "bar");    
    	}
    	catch (Exception e) {}
    	return result;
    }
    private String getTemperature(ParametersSetting ps) {
    	String result = ps.getTemperature();
    	try {
	    	result = ps.getTemperature() == null || ps.getTemperature().isEmpty() ?
	    			null : 
	    				""+convert(Categories.getTempCategory(), ps.getTemperatureUnit(), Double.parseDouble(ps.getTemperature()), "°C");
    	}
    	catch (Exception e) {}
    	return result;
    }
    private Double convert(Category cat, String fromUnit, Double value, String toUnit) {
    	Double newValue;
		try {
			newValue = cat.convert(value, fromUnit, toUnit);
		}
		catch (ConvertException e) {
			newValue = value;
			String warnings = "";
			if (warnings.indexOf(fromUnit + " <-> " + toUnit) < 0) {
				warnings += "Problems converting '" + cat.getName() + "': " + fromUnit + " <-> " + toUnit + "\n";
			}
			//e.printStackTrace();
		}
		return newValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

}

