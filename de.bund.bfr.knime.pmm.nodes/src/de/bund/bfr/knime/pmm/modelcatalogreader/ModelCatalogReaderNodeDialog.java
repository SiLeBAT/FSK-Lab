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
package de.bund.bfr.knime.pmm.modelcatalogreader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.ui.ModelReaderUi;

/**
 * <code>NodeDialog</code> for the "ModelCatalogReader" Node.
 * 
 * @author Jorgen Brandt
 */
public class ModelCatalogReaderNodeDialog extends NodeDialogPane implements ActionListener {

	// private JComboBox levelBox;
	//private DbConfigurationUi dbui;
	private ModelReaderUi filterui;
	
    /**
     * New pane for configuring the ModelReader node.
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws PmmException 
     */
    protected ModelCatalogReaderNodeDialog() {    	
    	JPanel panel = new JPanel();
    	    	
    	//dbui = new DbConfigurationUi( true );    	    	
    	//dbui.getApplyButton().addActionListener( this );    	
    	filterui = new ModelReaderUi();
    	
    	panel.setLayout(new BorderLayout());
    	//panel.add(dbui, BorderLayout.NORTH);
    	panel.add(filterui, BorderLayout.CENTER);
    	
    	addTab("MC Filter", panel);
    	//addTab("Database connection", dbui);
    	
    	try {
    		updateModelName();
    	}
    	catch( Exception e ) {
    		e.printStackTrace( System.err );
    	}
    }

	@Override
	protected void saveSettingsTo( NodeSettingsWO settings )
			throws InvalidSettingsException {
		/*
		settings.addString(ModelCatalogReaderNodeModel.PARAM_FILENAME, dbui.getFilename());
		settings.addString(ModelCatalogReaderNodeModel.PARAM_LOGIN, dbui.getLogin());
		settings.addString(ModelCatalogReaderNodeModel.PARAM_PASSWD, dbui.getPasswd());
		settings.addBoolean(ModelCatalogReaderNodeModel.PARAM_OVERRIDE, dbui.isOverride());
		*/
		settings.addInt(ModelCatalogReaderNodeModel.PARAM_LEVEL, filterui.getLevel());
		settings.addString(ModelCatalogReaderNodeModel.PARAM_MODELCLASS, filterui.getModelClass());
		settings.addBoolean(ModelCatalogReaderNodeModel.PARAM_MODELFILTERENABLED, filterui.isModelFilterEnabled());
		settings.addIntArray(ModelCatalogReaderNodeModel.PARAM_MODELLISTINT, filterui.getModelList());
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {
		try {
			try {
				updateModelName();
			} catch (Exception e) {}
			/*
			dbui.setFilename(settings.getString(ModelCatalogReaderNodeModel.PARAM_FILENAME));
			dbui.setLogin(settings.getString(ModelCatalogReaderNodeModel.PARAM_LOGIN));
			dbui.setPasswd(settings.getString(ModelCatalogReaderNodeModel.PARAM_PASSWD));
			dbui.setOverride(settings.getBoolean(ModelCatalogReaderNodeModel.PARAM_OVERRIDE));
			*/
			filterui.setLevel(settings.getInt(ModelCatalogReaderNodeModel.PARAM_LEVEL));
			filterui.setModelClass(settings.getString(ModelCatalogReaderNodeModel.PARAM_MODELCLASS));
			filterui.setModelFilterEnabled(settings.getBoolean(ModelCatalogReaderNodeModel.PARAM_MODELFILTERENABLED));
			if (settings.containsKey(ModelCatalogReaderNodeModel.PARAM_MODELLISTINT)) filterui.enableModelList(settings.getIntArray(ModelCatalogReaderNodeModel.PARAM_MODELLISTINT));
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
					filterui.enableModelList(idis);
				}
			}
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
		}
		catch( PmmException e ) {
			e.printStackTrace( System.err );
		} 
		
	}

	private void updateModelName() throws ClassNotFoundException, SQLException, PmmException {
        // fetch database connection
    	Bfrdb db;
    	ResultSet result;
    	
    	filterui.clearModelSet();

        db = null;
        /*
    	if (dbui.getOverride()) {
			db = new Bfrdb(dbui.getFilename(), dbui.getLogin(), dbui.getPasswd());
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}
		*/
    	try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {}
    	
    	result = db.selectModel(1);    	    	
    	while (result.next()) {
			int modelID = result.getInt(Bfrdb.ATT_MODELID);
			Object visible = DBKernel.getValue(db.getConnection(), "Modellkatalog", "ID", "" + modelID, "visible");
			filterui.addModelPrim(modelID, result.getString(Bfrdb.ATT_NAME), DBKernel.myDBi.getHashMap("ModelType").get(result.getInt("Klasse")), visible == null
					|| (visible instanceof Boolean && (Boolean) visible));
    	}
    	result = db.selectModel(2);    	
    	while (result.next()) {
			int modelID = result.getInt(Bfrdb.ATT_MODELID);
			Object visible = DBKernel.getValue(db.getConnection(), "Modellkatalog", "ID", "" + modelID, "visible");
			filterui.addModelSec(modelID, result.getString(Bfrdb.ATT_NAME), DBKernel.myDBi.getHashMap("ModelType").get(result.getInt("Klasse")), visible == null
					|| (visible instanceof Boolean && (Boolean) visible));
    	}    	
	}

	@Override
	public void actionPerformed( ActionEvent arg0 ) {
		
		try {
			
			updateModelName();
		}
		catch( Exception e ) {
			
			e.printStackTrace( System.err );
		}
		
	}
	
}

