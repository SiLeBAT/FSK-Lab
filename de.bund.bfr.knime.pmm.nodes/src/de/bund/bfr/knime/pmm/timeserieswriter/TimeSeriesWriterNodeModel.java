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
package de.bund.bfr.knime.pmm.timeserieswriter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of TimeSeriesWriter.
 * 
 *
 * @author Jorgen Brandt
 */
public class TimeSeriesWriterNodeModel extends NodeModel {
	/*
	static final String PARAM_FILENAME = "filename";
	static final String PARAM_LOGIN = "login";
	static final String PARAM_PASSWD = "passwd";
	static final String PARAM_OVERRIDE = "override";

	private String filename;
	private String login;
	private String passwd;
	private boolean override;
	*/
    
    /**
     * Constructor for the node model.
     */
    protected TimeSeriesWriterNodeModel() {
        super( 1, 0 );
        /*
        filename = "";
        login = "";
        passwd = "";
        override = false;
        */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	Bfrdb db = null;
    	try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {}
    	/*
    	if( override ) {
			db = new Bfrdb( filename, login, passwd );
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}
		*/
    	Connection conn = db.getConnection();
    	conn.setReadOnly(false);
    	
    	long n = inData[0].size();
    	
		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		HashMap<Integer, PmmTimeSeries> alreadyInsertedTs = new HashMap<>();
		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds = new HashMap<>();
    	String dbuuid = db.getDBUUID();

		int j = 0;
		String warnings = "";
		while (reader.hasMoreElements()) {
    		exec.setProgress( ( double )j++/n );
    		
			KnimeTuple row = reader.nextElement();
			
			PmmTimeSeries ts = new PmmTimeSeries(row);	
			int rowTsID = ts.getCondId();
			if (alreadyInsertedTs.containsKey(rowTsID)) {
				ts = alreadyInsertedTs.get(rowTsID);
			}
			else {
				String[] attrs = new String[] {TimeSeriesSchema.ATT_CONDID, TimeSeriesSchema.ATT_MISC, TimeSeriesSchema.ATT_AGENT,
						TimeSeriesSchema.ATT_MATRIX, TimeSeriesSchema.ATT_LITMD};
				String[] dbTablenames = new String[] {"Versuchsbedingungen", "Sonstiges", "Agenzien", "Matrices", "Literatur"};

				boolean checkAnywayDueToNegativeId = (ts.getCondId() < 0);
				String rowuuid = row.getString(TimeSeriesSchema.ATT_DBUUID);
				if (rowuuid == null) rowuuid = ts.getDbuuid();
				if (rowuuid == null && ts.getMatrix() != null && ts.getMatrix().size() > 0) {
					rowuuid = ((MatrixXml) ts.getMatrix().get(0)).getDbuuid();
				}
				foreignDbIds = checkIDs(conn, true, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);				
				db.insertTs(ts);				
				foreignDbIds = checkIDs(conn, false, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
				
				alreadyInsertedTs.put(rowTsID, ts);
				
				if (ts.getWarning() != null && !ts.getWarning().trim().isEmpty()) {
					if (warnings.indexOf(ts.getWarning() + "\n") < 0) {
						warnings += ts.getWarning() + "\n";						
					}
				}
			}
		}
		DBKernel.setKnownIDs4PMM(conn, foreignDbIds);
		if (!warnings.isEmpty()) {
			this.setWarningMessage(warnings.trim());
		}			
    	conn.setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", false));
    	db.close();
        return null;
    }
    // TimeSeries
    private HashMap<String, HashMap<String, HashMap<Integer, Integer>>> checkIDs(Connection conn, boolean before, String dbuuid, KnimeTuple row, KnimeTuple ts,
    		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds,
    		String[] schemaAttr, String[] dbTablename, String rowuuid, boolean checkAnywayDueToNegativeId) throws PmmException {
		if (checkAnywayDueToNegativeId || rowuuid == null || !rowuuid.equals(dbuuid)) {
			if (!foreignDbIds.containsKey(rowuuid)) foreignDbIds.put(rowuuid, new HashMap<String, HashMap<Integer, Integer>>());
			HashMap<String, HashMap<Integer, Integer>> d = foreignDbIds.get(rowuuid);
			
			for (int i=0;i<schemaAttr.length;i++) {
				if (!d.containsKey(dbTablename[i])) d.put(dbTablename[i], new HashMap<Integer, Integer>());
				if (before) DBKernel.getKnownIDs4PMM(conn, d.get(dbTablename[i]), dbTablename[i], rowuuid);
				HashMap<Integer, Integer> h = CellIO.setTsIDs(before, schemaAttr[i], d.get(dbTablename[i]), row, ts);
				d.put(dbTablename[i], h);
				//if (!before) DBKernel.setKnownIDs4PMM(conn, d.get(dbTablename[i]), dbTablename[i], rowuuid);
			}
			foreignDbIds.put(rowuuid, d);
		}    	
		return foreignDbIds;
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
    	getInSchema(inSpecs[0]);    	
        return null;//new DataTableSpec[]{};
    }
    private KnimeSchema getInSchema(final DataTableSpec inSpec) throws InvalidSettingsException {
    	KnimeSchema result = null;
    	String errorMsg = "Unexpected format - Microbial data is not present in the columns of the incoming table";
    	KnimeSchema inSchema = new TimeSeriesSchema();
    	try {
    		if (inSchema.conforms(inSpec)) {
    			result = inSchema;
   			}
    	}
    	catch (PmmException e) {
    	}
		if (result == null) {
			throw new InvalidSettingsException(errorMsg);
		}
    	return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	/*
    	settings.addString( PARAM_FILENAME, filename );
    	settings.addString( PARAM_LOGIN, login );
    	settings.addString( PARAM_PASSWD, passwd );
    	settings.addBoolean( PARAM_OVERRIDE, override );
    	*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	/*
    	filename = settings.getString( PARAM_FILENAME );
    	login = settings.getString( PARAM_LOGIN );
    	passwd = settings.getString( PARAM_PASSWD );
    	override = settings.getBoolean( PARAM_OVERRIDE );
    	*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
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

}

