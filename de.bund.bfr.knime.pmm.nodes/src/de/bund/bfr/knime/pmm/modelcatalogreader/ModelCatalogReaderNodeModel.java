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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

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

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DbIo;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.ui.ModelReaderUi;

/**
 * This is the model implementation of ModelCatalogReader.
 * 
 * 
 * @author Jorgen Brandt
 */
public class ModelCatalogReaderNodeModel extends NodeModel {

	static final String PARAM_LEVEL = "level";
	static final String PARAM_MODELCLASS = "modelClass";
	static final String PARAM_MODELFILTERENABLED = "modelFilterEnabled";
	static final String PARAM_MODELLISTINT = "modelListInt";
	/*
	 * static final String PARAM_FILENAME = "filename"; static final String
	 * PARAM_LOGIN = "login"; static final String PARAM_PASSWD = "passwd";
	 * static final String PARAM_OVERRIDE = "override";
	 */
	/*
	 * private String filename; private String login; private String passwd;
	 * private boolean override;
	 */
	private int level;
	private String modelClass;
	private int[] modelList;
	private boolean modelFilterEnabled;

	private Connection conn = null;

	/**
	 * Constructor for the node model.
	 */
	protected ModelCatalogReaderNodeModel() {
		super(0, 1);
		/*
		 * filename = ""; login = ""; passwd = ""; override = false;
		 */
		level = 1;
		modelList = null;
		modelFilterEnabled = false;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {

		ResultSet result;
		Bfrdb db;
		BufferedDataContainer buf;
		KnimeSchema schema;
		KnimeTuple tuple;
		String formula;
		String dbuuid;

		// fetch time series

		// fetch database connection
		db = null;
		try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {
		}
		/*
		 * if( override ) { db = new Bfrdb( filename, login, passwd ); conn =
		 * db.getConnection(); } else { db = new
		 * Bfrdb(DBKernel.getLocalConn(true)); conn = null; }
		 */
		dbuuid = db.getDBUUID();

		String message = "";

		if (level == 1) {

			result = db.selectModel(1);

			schema = new Model1Schema();

			buf = exec.createDataContainer(schema.createSpec());

			int i = 0;
			while (result.next()) {
				int modelID = result.getInt(Bfrdb.ATT_MODELID);
				boolean takeIt = false;
				Object visible = DBKernel.getValue(db.getConnection(), "Modellkatalog", "ID", ""+modelID, "visible");
				if (visible == null || (visible instanceof Boolean && (Boolean) visible)) {
					takeIt = modelClass == null || modelClass.equals("All");
					if (!takeIt) {
						Object cls = DBKernel.getValue(conn, "Modellkatalog", "ID", modelID + "", "Klasse");
						String mcls = DBKernel.myDBi.getHashMap("ModelType").get(cls);
						//takeIt = (modelClass.indexOf(mcls) >= 0);
						takeIt = (mcls != null && mcls.indexOf(modelClass) >= 0);
					}					
				}
				if (takeIt) {
					String addWarningMsg = "";
					// initialize row
					tuple = new KnimeTuple(schema);

					// fill row
					formula = result.getString("Formel");
					if (formula != null) {
						formula = formula.replaceAll("~", "=").replaceAll("\\s", "");
					}

					PmmXmlDoc cmDoc = new PmmXmlDoc();
					CatalogModelXml cmx = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), formula, null, dbuuid);
					cmx.setModelClass(result.getInt("Klasse"));
					cmDoc.add(cmx);
					tuple.setValue(Model1Schema.ATT_MODELCATALOG, cmDoc);

					PmmXmlDoc depDoc = new PmmXmlDoc();
					DepXml dx = new DepXml(result.getString(Bfrdb.ATT_DEP), result.getString("DepCategory"), result.getString("DepUnit"));
					dx.setDescription(result.getString("DepDescription"));
					depDoc.add(dx);
					tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);
					if (dx.getUnit() == null || dx.getUnit().isEmpty()) addWarningMsg += "\nUnit not defined for dependant variable '" + dx.getName() + "' in model with ID "
							+ cmx.getId() + "!";

					PmmXmlDoc ixml = DbIo.convertArrays2IndepXmlDoc(null, result.getArray(Bfrdb.ATT_INDEP), null, null, result.getArray("IndepCategory"),
							result.getArray("IndepUnit"), result.getArray("IndepDescription"), true);
					tuple.setValue(Model1Schema.ATT_INDEPENDENT, ixml);
					if (!ixml.getWarning().isEmpty()) addWarningMsg += "\n" + ixml.getWarning() + "in model with ID " + cmx.getId() + "!";

					tuple.setValue(Model1Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(null, result.getArray(Bfrdb.ATT_PARAMNAME), null, null,
							result.getArray("ParCategory"), result.getArray("ParUnit"), null, result.getArray(Bfrdb.ATT_MINVALUE), result.getArray(Bfrdb.ATT_MAXVALUE),
							result.getArray("ParamDescription"), result.getArray(Bfrdb.ATT_PARAMTYPE), null, null, null, null));

					if (result.getString("DepUnit") == null || result.getString("DepUnit").isEmpty() || result.getString("IndepUnit") == null
							|| result.getString("IndepUnit").isEmpty()) {
						message = "Unit assignment missing for one or more formula(s)";
					}

					//int ri = MathUtilities.getRandomNegativeInt();
					PmmXmlDoc emDoc = new PmmXmlDoc();
					EstModelXml emx = new EstModelXml(null, null, null, null, null, null, null, null);
					emDoc.add(emx);
					tuple.setValue(Model1Schema.ATT_ESTMODEL, emDoc);

					String s = result.getString("LitMID");
					if (s != null) {
						tuple.setValue(Model1Schema.ATT_MLIT, db.getLiteratureXml(s, dbuuid));
					}

					tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
					tuple.setValue(Model1Schema.ATT_DBUUID, dbuuid);

					// add row to data buffer
					if (!modelFilterEnabled || ModelReaderUi.passesFilter(modelList, tuple, level)) {
						buf.addRowToTable(new DefaultRow(String.valueOf(i++), tuple));
						if (!addWarningMsg.isEmpty()) this.setWarningMessage(this.getWarningMessage() + addWarningMsg);
					}
				}
			}
		} else {
			result = db.selectModel(2);

			schema = new Model2Schema();

			buf = exec.createDataContainer(schema.createSpec());

			int i = 0;
			while (result.next()) {
				int modelID = result.getInt(Bfrdb.ATT_MODELID);
				boolean takeIt = false;
				Object visible = DBKernel.getValue(db.getConnection(), "Modellkatalog", "ID", ""+modelID, "visible");
				if (visible == null || (visible instanceof Boolean && (Boolean) visible)) {
					takeIt = modelClass == null || modelClass.equals("All");
					if (!takeIt) {
						Object cls = DBKernel.getValue(conn, "Modellkatalog", "ID", modelID + "", "Klasse");
						String mcls = DBKernel.myDBi.getHashMap("ModelType").get(cls);
						//takeIt = (modelClass.indexOf(mcls) >= 0);
						takeIt = (mcls != null && mcls.indexOf(modelClass) >= 0);
					}					
				}
				if (takeIt) {
					String addWarningMsg = "";
					// initialize row
					tuple = new KnimeTuple(schema);

					// fill row
					formula = result.getString("Formel");
					if (formula != null) {
						formula = formula.replaceAll("~", "=").replaceAll("\\s", "");
					}

					PmmXmlDoc cmDoc = new PmmXmlDoc();
					CatalogModelXml cmx = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), formula, null, dbuuid);
					cmx.setModelClass(result.getInt("Klasse"));
					cmDoc.add(cmx);
					tuple.setValue(Model2Schema.ATT_MODELCATALOG, cmDoc);

					//tuple.setValue( Model2Schema.ATT_FORMULA, formula );
					//tuple.setValue( Model2Schema.ATT_PARAMNAME, DbIo.convertArray2String(result.getArray( Bfrdb.ATT_PARAMNAME ) ));
					//tuple.setValue( Model2Schema.ATT_DEPVAR, result.getString( Bfrdb.ATT_DEP ) );
					PmmXmlDoc depDoc = new PmmXmlDoc();
					DepXml dx = new DepXml(result.getString(Bfrdb.ATT_DEP), result.getString("DepCategory"), result.getString("DepUnit"));
					dx.setDescription(result.getString("DepDescription"));
					depDoc.add(dx);
					tuple.setValue(Model2Schema.ATT_DEPENDENT, depDoc);
					if (dx.getUnit() == null || dx.getUnit().isEmpty()) addWarningMsg += "\nUnit not defined for dependant variable '" + dx.getName() + "' in model with ID "
							+ cmx.getId() + "!";
					//tuple.setValue( Model2Schema.ATT_INDEPVAR, DbIo.convertArray2String(result.getArray( Bfrdb.ATT_INDEP ) ));
					//tuple.setValue( Model2Schema.ATT_MODELNAME, result.getString( Bfrdb.ATT_NAME ) );
					//tuple.setValue( Model2Schema.ATT_MODELID, result.getInt( Bfrdb.ATT_MODELID ) );
					//tuple.setValue( Model2Schema.ATT_MINVALUE, DbIo.convertArray2String(result.getArray( Bfrdb.ATT_MINVALUE ) ));
					//tuple.setValue( Model2Schema.ATT_MAXVALUE, DbIo.convertArray2String(result.getArray( Bfrdb.ATT_MAXVALUE ) ));
					PmmXmlDoc ixml = DbIo.convertArrays2IndepXmlDoc(null, result.getArray(Bfrdb.ATT_INDEP), null, null, result.getArray("IndepCategory"),
							result.getArray("IndepUnit"), result.getArray("IndepDescription"), false);
					tuple.setValue(Model2Schema.ATT_INDEPENDENT, ixml);
					if (!ixml.getWarning().isEmpty()) addWarningMsg += "\n" + ixml.getWarning() + "in model with ID " + cmx.getId() + "!";

					tuple.setValue(Model2Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(null, result.getArray(Bfrdb.ATT_PARAMNAME), null, null,
							result.getArray("ParCategory"), result.getArray("ParUnit"), null, result.getArray(Bfrdb.ATT_MINVALUE), result.getArray(Bfrdb.ATT_MAXVALUE),
							result.getArray("ParamDescription"), result.getArray(Bfrdb.ATT_PARAMTYPE), null, null, null, null));

					//int ri = MathUtilities.getRandomNegativeInt();
					PmmXmlDoc emDoc = new PmmXmlDoc();
					EstModelXml emx = new EstModelXml(null, null, null, null, null, null, null, null);
					emDoc.add(emx);
					tuple.setValue(Model2Schema.ATT_ESTMODEL, emDoc);

					tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, null);

					String s = result.getString("LitMID");
					if (s != null) {
						tuple.setValue(Model2Schema.ATT_MLIT, db.getLiteratureXml(s, dbuuid));
					}

					tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
					tuple.setValue(Model2Schema.ATT_DBUUID, dbuuid);

					// add row to data buffer
					if (!modelFilterEnabled || ModelReaderUi.passesFilter(modelList, tuple, level)) {
						buf.addRowToTable(new DefaultRow(String.valueOf(i++), tuple));
						if (!addWarningMsg.isEmpty()) this.setWarningMessage(this.getWarningMessage() + addWarningMsg);
					}
				}

			}
		}

		// close data buffer
		buf.close();
		db.close();

		if (!message.isEmpty()) exec.setMessage(message);
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

		if (level == 1) {
			return new DataTableSpec[] { new Model1Schema().createSpec() };
		} else {
			return new DataTableSpec[] { new Model2Schema().createSpec() };
		}
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
		settings.addInt(PARAM_LEVEL, level);
		settings.addString(PARAM_MODELCLASS, modelClass);
		settings.addIntArray(PARAM_MODELLISTINT, modelList);
		settings.addBoolean(PARAM_MODELFILTERENABLED, modelFilterEnabled);
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
		level = settings.getInt(PARAM_LEVEL);
		modelClass = settings.getString(PARAM_MODELCLASS);
		if (settings.containsKey(PARAM_MODELLISTINT)) modelList = settings.getIntArray(PARAM_MODELLISTINT);
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
		modelFilterEnabled = settings.getBoolean(PARAM_MODELFILTERENABLED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		int s;

		s = settings.getInt(PARAM_LEVEL);
		if (!(s == 1 || s == 2)) {
			throw new InvalidSettingsException("Parameter level must be " + "in {1,2}");
		}
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
