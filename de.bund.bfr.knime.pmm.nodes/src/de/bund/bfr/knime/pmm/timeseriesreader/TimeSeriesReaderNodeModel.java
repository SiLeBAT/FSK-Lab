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
package de.bund.bfr.knime.pmm.timeseriesreader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.LinkedHashMap;

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
import de.bund.bfr.knime.pmm.common.DBUtilities;
import de.bund.bfr.knime.pmm.common.DbIo;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;

/**
 * This is the model implementation of TimeSeriesReader.
 * 
 * 
 * @author Jorgen Brandt
 */
public class TimeSeriesReaderNodeModel extends NodeModel {
	/*
	 * static final String PARAM_FILENAME = "filename"; static final String
	 * PARAM_LOGIN = "login"; static final String PARAM_PASSWD = "passwd";
	 * static final String PARAM_OVERRIDE = "override";
	 */
	static final String PARAM_MATRIXSTRING = "matrixString";
	static final String PARAM_AGENTSTRING = "agentString";
	static final String PARAM_LITERATURESTRING = "literatureString";
	static final String PARAM_MATRIXID = "matrixID";
	static final String PARAM_AGENTID = "agentID";
	static final String PARAM_LITERATUREID = "literatureID";
	static final String PARAM_PARAMETERS = "parameters";
	static final String PARAM_PARAMETERNAME = "parameterName";
	static final String PARAM_PARAMETERMIN = "parameterMin";
	static final String PARAM_PARAMETERMAX = "parameterMax";
	/*
	 * private String filename; private String login; private String passwd;
	 * private boolean override;
	 */
	private String matrixString;
	private String agentString;
	private String literatureString;
	private int matrixID, agentID, literatureID;

	private LinkedHashMap<String, Double[]> parameter;

	private Connection conn = null;

	/**
	 * Constructor for the node model.
	 */
	protected TimeSeriesReaderNodeModel() {

		super(0, 1);
		/*
		 * filename = ""; login = ""; passwd = ""; override = false;
		 */
		matrixString = "";
		agentString = "";
		literatureString = "";

		parameter = new LinkedHashMap<>();
	}

	private String getWhere() {
		String result = " WHERE TRUE";

		if (agentID > 0) result += " AND \"Agens\" = " + agentID;
		if (agentString != null && !agentString.trim().isEmpty()) {
			result += " AND (INSTR(LCASE(\"Agensname\"), '" + agentString.toLowerCase() + "') > 0" + " OR INSTR(LCASE(\"AgensDetail\"), '" + agentString.toLowerCase() + "') > 0)";
		}
		if (matrixID > 0) result += " AND \"Matrix\" = " + matrixID;
		if (matrixString != null && !matrixString.trim().isEmpty()) {
			result += " AND (INSTR(LCASE(\"Matrixname\"), '" + matrixString.toLowerCase() + "') > 0" + " OR INSTR(LCASE(\"MatrixDetail\"), '" + matrixString.toLowerCase()
					+ "') > 0)";
		}
		if (literatureID > 0) result += " AND \"Literatur\" = " + literatureID;
		if (literatureString != null && !literatureString.trim().isEmpty()) {
			// passesFilter sollte diesen Part erstmal übernehmen... System.err.println("Literature Check to be implemented!!!");
		}
		for (String par : parameter.keySet()) {
			Double[] dbl = parameter.get(par);
			if (dbl[0] == null && dbl[1] == null) continue;
			if (par.equalsIgnoreCase(AttributeUtilities.ATT_TEMPERATURE)) {
				if (dbl[0] != null) result += " AND (\"Temperatur\" >= " + dbl[0] + " OR \"Temperatur\" IS NULL)";
				if (dbl[1] != null) result += " AND (\"Temperatur\" <= " + dbl[1] + " OR \"Temperatur\" IS NULL)";
			} else if (par.equalsIgnoreCase(AttributeUtilities.ATT_PH)) {
				if (dbl[0] != null) result += " AND (\"pH\" >= " + dbl[0] + " OR \"pH\" IS NULL)";
				if (dbl[1] != null) result += " AND (\"pH\" <= " + dbl[1] + " OR \"pH\" IS NULL)";
			} else if (par.equalsIgnoreCase(AttributeUtilities.ATT_AW)) {
				if (dbl[0] != null) result += " AND (\"aw\" >= " + dbl[0] + " OR \"aw\" IS NULL)";
				if (dbl[1] != null) result += " AND (\"aw\" <= " + dbl[1] + " OR \"aw\" IS NULL)";
			} else {
				// passesFilter sollte diesen Part erstmal übernehmen... System.err.println("Literature Check to be implemented!!!");
			}
		}
		//System.err.println(result);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
		boolean filterEnabled = false;

		if (!matrixString.isEmpty() || !agentString.isEmpty() || !literatureString.isEmpty() || matrixID > 0 || agentID > 0 || literatureID > 0 || parameter.size() > 0) filterEnabled = true;

		// fetch time series
		Bfrdb db = null;
		try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {
		}
		/*
		 * if( override ) { db = new Bfrdb( filename, login, passwd ); conn =
		 * db.getConnection(); } else { db = new
		 * Bfrdb(DBKernel.getLocalConn(true)); conn = null; }
		 */

		String dbuuid = db.getDBUUID();

		String where = getWhere();
		ResultSet result = db.selectTs(where);

		// initialize data buffer
		BufferedDataContainer buf = exec.createDataContainer(new TimeSeriesSchema().createSpec());
		int i = 0;//, j=0;
		//CellIO.tttxcmldoc = 0;
		while (result.next()) {
			//System.err.println(j+"\t"+i);
			PmmXmlDoc tsDoc = DbIo.convertStringLists2TSXmlDoc(result.getArray("Zeit"), result.getArray("ZeitEinheit"), result.getArray("Konzentration"),
					result.getArray("KonzentrationsEinheit"), result.getArray("KonzentrationsObjectType"), result.getArray("Standardabweichung"),
					result.getArray("Wiederholungen"), null, null);

			if (tsDoc.size() > 0) {
				// initialize row
				PmmTimeSeries tuple = new PmmTimeSeries();
				// fill row
				int condID = result.getInt(Bfrdb.ATT_CONDITIONID);
				tuple.setCondId(condID);
				tuple.setCombaseId(result.getString("CombaseID"));
				//PmmXmlDoc miscDoc = null; miscDoc = db.getMiscXmlDoc(result);
				PmmXmlDoc miscDoc = DbIo.convertArrays2MiscXmlDoc(result.getArray("SonstigesID"), result.getArray("Parameter"), result.getArray("Beschreibung"),
						result.getArray("SonstigesWert"), result.getArray("Einheit"), dbuuid);
				if (result.getObject(Bfrdb.ATT_TEMPERATURE) != null) {
					double dbl = result.getDouble(Bfrdb.ATT_TEMPERATURE);
					MiscXml mx = new MiscXml(AttributeUtilities.ATT_TEMPERATURE_ID, AttributeUtilities.ATT_TEMPERATURE, AttributeUtilities.ATT_TEMPERATURE, dbl,
							Arrays.asList(Categories.getTempCategory().getName()), Categories.getTempCategory().getStandardUnit(), dbuuid);//null,"°C");
					miscDoc.add(mx);
				}
				if (result.getObject(Bfrdb.ATT_PH) != null) {
					double dbl = result.getDouble(Bfrdb.ATT_PH);
					MiscXml mx = new MiscXml(AttributeUtilities.ATT_PH_ID, AttributeUtilities.ATT_PH, AttributeUtilities.ATT_PH, dbl,
							Arrays.asList(Categories.getPhCategory().getName()), Categories.getPhCategory().getAllUnits().toArray(new String[0])[0], dbuuid);
					miscDoc.add(mx);
				}
				if (result.getObject(Bfrdb.ATT_AW) != null) {
					double dbl = result.getDouble(Bfrdb.ATT_AW);
					MiscXml mx = new MiscXml(AttributeUtilities.ATT_AW_ID, AttributeUtilities.ATT_AW, AttributeUtilities.ATT_AW, dbl,
							Arrays.asList(Categories.getAwCategory().getName()), Categories.getAwCategory().getAllUnits().toArray(new String[0])[1], dbuuid);
					miscDoc.add(mx);
				}
				if (result.getObject(Bfrdb.ATT_PRESSURE) != null) {
					double dbl = result.getDouble(Bfrdb.ATT_PRESSURE);
					MiscXml mx = new MiscXml(AttributeUtilities.ATT_PRESSURE_ID, AttributeUtilities.ATT_PRESSURE, AttributeUtilities.ATT_PRESSURE, dbl,
							null, "bar", dbuuid);
					miscDoc.add(mx);
				}
				tuple.addMiscs(miscDoc);

				PmmXmlDoc mdInfoDoc = new PmmXmlDoc();
				Boolean checked = null;
				Integer qualityScore = null;
				if (result.getObject("Geprueft") != null) checked = result.getBoolean("Geprueft");
				if (result.getObject("Guetescore") != null) qualityScore = result.getInt("Guetescore");
				MdInfoXml mdix = new MdInfoXml(condID, "i" + condID, result.getString("Kommentar"), qualityScore, checked);
				mdInfoDoc.add(mdix);
				tuple.setMdInfo(mdInfoDoc);

				tuple.setAgent(result.getInt(Bfrdb.ATT_AGENTID), result.getString(Bfrdb.ATT_AGENTNAME), result.getString(Bfrdb.ATT_AGENTDETAIL), dbuuid);
				tuple.setMatrix(result.getInt(Bfrdb.ATT_MATRIXID), result.getString(Bfrdb.ATT_MATRIXNAME), result.getString(Bfrdb.ATT_MATRIXDETAIL), dbuuid);
				tuple.setMdData(tsDoc);
				//tuple.setComment( result.getString( Bfrdb.ATT_COMMENT ) );
				tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dbuuid);

				String s = result.getString(Bfrdb.ATT_LITERATUREID);
				if (s != null) {
					PmmXmlDoc l = new PmmXmlDoc();
					LiteratureItem li = DBUtilities.getLiteratureItem(conn, Integer.valueOf(s), dbuuid);
					li.setDbuuid(dbuuid);
					l.add(li);
					tuple.setLiterature(l);
				}

				// add row to data buffer
				if (!filterEnabled || MdReaderUi.passesFilter(matrixString, agentString, literatureString, matrixID, agentID, literatureID, parameter, tuple)) {
					buf.addRowToTable(new DefaultRow(String.valueOf(i++), tuple));
				}
			} else {
				//j++;
			}
		}
		//System.err.println("PmmTimeSeries: xmlDocCreation: " + CellIO.tttxcmldoc);
		// close data buffer
		buf.close();
		result.close();
		db.close();

		return new BufferedDataTable[] { buf.getTable() };
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { new TimeSeriesSchema().createSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		/*
		 * settings.addString( PARAM_FILENAME, filename ); settings.addString(
		 * PARAM_LOGIN, login ); settings.addString( PARAM_PASSWD, passwd );
		 * settings.addBoolean( PARAM_OVERRIDE, override );
		 */
		settings.addString(PARAM_MATRIXSTRING, matrixString);
		settings.addString(PARAM_AGENTSTRING, agentString);
		settings.addString(PARAM_LITERATURESTRING, literatureString);
		settings.addInt(PARAM_MATRIXID, matrixID);
		settings.addInt(PARAM_AGENTID, agentID);
		settings.addInt(PARAM_LITERATUREID, literatureID);

		Config c = settings.addConfig(PARAM_PARAMETERS);
		String[] pars = new String[parameter.size()];
		String[] mins = new String[parameter.size()];
		String[] maxs = new String[parameter.size()];
		int i = 0;
		for (String par : parameter.keySet()) {
			Double[] dbl = parameter.get(par);
			pars[i] = par;
			mins[i] = "" + dbl[0];
			maxs[i] = "" + dbl[1];
			i++;
		}
		c.addStringArray(PARAM_PARAMETERNAME, pars);
		c.addStringArray(PARAM_PARAMETERMIN, mins);
		c.addStringArray(PARAM_PARAMETERMAX, maxs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		/*
		 * filename = settings.getString( PARAM_FILENAME ); login =
		 * settings.getString( PARAM_LOGIN ); passwd = settings.getString(
		 * PARAM_PASSWD ); override = settings.getBoolean( PARAM_OVERRIDE );
		 */
		matrixString = settings.getString(PARAM_MATRIXSTRING);
		agentString = settings.getString(PARAM_AGENTSTRING);
		literatureString = settings.getString(PARAM_LITERATURESTRING);
		matrixID = settings.containsKey(PARAM_MATRIXID) ? settings.getInt(PARAM_MATRIXID) : 0;
		agentID = settings.containsKey(PARAM_AGENTID) ? settings.getInt(PARAM_AGENTID) : 0;
		literatureID = settings.containsKey(PARAM_LITERATUREID) ? settings.getInt(PARAM_LITERATUREID) : 0;

		Config c = settings.getConfig(PARAM_PARAMETERS);
		String[] pars = c.getStringArray(PARAM_PARAMETERNAME);
		String[] mins = c.getStringArray(PARAM_PARAMETERMIN);
		String[] maxs = c.getStringArray(PARAM_PARAMETERMAX);

		parameter = new LinkedHashMap<>();
		for (int i = 0; i < pars.length; i++) {
			Double[] dbl = new Double[2];
			if (!mins[i].equals("null")) dbl[0] = Double.parseDouble(mins[i]);
			if (!maxs[i].equals("null")) dbl[1] = Double.parseDouble(maxs[i]);
			if (dbl[0] != null || dbl[1] != null) parameter.put(pars[i], dbl);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
	}

}
