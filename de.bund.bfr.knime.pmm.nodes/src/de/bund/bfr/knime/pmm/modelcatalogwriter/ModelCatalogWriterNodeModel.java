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
package de.bund.bfr.knime.pmm.modelcatalogwriter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

/**
 * This is the model implementation of ModelCatalogWriter.
 * 
 * 
 * @author Jorgen Brandt
 */
public class ModelCatalogWriterNodeModel extends NodeModel {
	/*
	 * static final String PARAM_FILENAME = "filename"; static final String
	 * PARAM_LOGIN = "login"; static final String PARAM_PASSWD = "passwd";
	 * static final String PARAM_OVERRIDE = "override";
	 * 
	 * private String filename; private String login; private String passwd;
	 * private boolean override;
	 */

	/**
	 * Constructor for the node model.
	 */
	protected ModelCatalogWriterNodeModel() {
		super(1, 0);
		/*
		 * filename = ""; login = ""; passwd = ""; override = false;
		 */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {

		Bfrdb db = null;
		try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {
		}
		/*
		 * if( override ) { db = new Bfrdb( filename, login, passwd ); } else {
		 * db = new Bfrdb(DBKernel.getLocalConn(true)); }
		 */
		Connection conn = db.getConnection();
		conn.setReadOnly(false);

		long n = inData[0].size();

		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		boolean model1Conform = inSchema.conforms(new Model1Schema());
		boolean model2Conform = inSchema.conforms(new Model2Schema());
		HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds = new HashMap<>();
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		String dbuuid = db.getDBUUID();

		int j = 0;
		List<Integer> alreadySaved = new ArrayList<>();
		String warnings = "";
		while (reader.hasMoreElements()) {
			exec.setProgress((double) j++ / n);

			KnimeTuple row = reader.nextElement();
			if (model1Conform) {
				//Integer rowMcID = row.getInt(Model1Schema.ATT_MODELID);
				CatalogModelXml cmx = null;
				PmmXmlDoc catModel = row.getPmmXml(Model1Schema.ATT_MODELCATALOG);
				if (catModel != null) {
					for (PmmXmlElementConvertable el : catModel.getElementSet()) {
						if (el instanceof CatalogModelXml) {
							cmx = (CatalogModelXml) el;
							break;
						}
					}
				}
				if (cmx.id != null && !alreadySaved.contains(cmx.id)) {
					alreadySaved.add(cmx.id);
					String modelName = cmx.name;//row.getString(Model1Schema.ATT_MODELNAME);
					String formula = cmx.formula;//row.getString(Model1Schema.ATT_FORMULA);
					PmmXmlDoc depXml = row.getPmmXml(Model1Schema.ATT_DEPENDENT);
					DepXml dx = (DepXml) depXml.getElementSet().get(0);
					PmmXmlDoc paramXml = row.getPmmXml(Model1Schema.ATT_PARAMETER);
					PmmXmlDoc indepXml = row.getPmmXml(Model1Schema.ATT_INDEPENDENT);

					PmmXmlDoc mLitXmlDoc = row.getPmmXml(Model1Schema.ATT_MLIT);
					PmmXmlDoc emLitXmlDoc = row.getPmmXml(Model1Schema.ATT_EMLIT);

					ParametricModel pm = new ParametricModel(modelName, formula, dx, 1, cmx.id);
					pm.setModelClass(cmx.modelClass);
					pm.setComment(cmx.comment);
					pm.setParameter(paramXml);
					pm.setIndependent(indepXml);
					pm.setFormula(pm.revertFormula());
					pm.setMLit(mLitXmlDoc);
					pm.setEstLit(emLitXmlDoc);

					String[] attrs = new String[] { Model1Schema.ATT_MODELCATALOG, Model1Schema.ATT_MLIT };
					String[] dbTablenames = new String[] { "Modellkatalog", "Literatur" };

					boolean checkAnywayDueToNegativeId = (pm.getModelId() < 0);
					String rowuuid = row.getString(Model1Schema.ATT_DBUUID);
					if (rowuuid == null) rowuuid = cmx.dbuuid;
					foreignDbIds = checkIDs(conn, true, dbuuid, row, pm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
					db.insertM(pm);
					foreignDbIds = checkIDs(conn, false, dbuuid, row, pm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
					if (!pm.warning.trim().isEmpty()) warnings += pm.warning;
				}
			}
			if (model2Conform) {
				//Integer rowMcID = row.getInt(Model2Schema.ATT_MODELID);
				CatalogModelXml cmx = null;
				PmmXmlDoc catModel = row.getPmmXml(Model2Schema.ATT_MODELCATALOG);
				if (catModel != null) {
					for (PmmXmlElementConvertable el : catModel.getElementSet()) {
						if (el instanceof CatalogModelXml) {
							cmx = (CatalogModelXml) el;
							break;
						}
					}
				}
				if (cmx.id != null && !alreadySaved.contains(cmx.id)) {
					alreadySaved.add(cmx.id);
					String modelName = cmx.name;//row.getString(Model2Schema.ATT_MODELNAME);
					String formula = cmx.formula;//row.getString(Model2Schema.ATT_FORMULA);
					PmmXmlDoc depXml = row.getPmmXml(Model2Schema.ATT_DEPENDENT);
					DepXml dx = (DepXml) depXml.getElementSet().get(0);

					PmmXmlDoc paramXml = row.getPmmXml(Model2Schema.ATT_PARAMETER);
					PmmXmlDoc indepXml = row.getPmmXml(Model2Schema.ATT_INDEPENDENT);

					PmmXmlDoc mLitXmlDoc = row.getPmmXml(Model2Schema.ATT_MLIT);
					PmmXmlDoc emLitXmlDoc = row.getPmmXml(Model2Schema.ATT_EMLIT);

					ParametricModel pm = new ParametricModel(modelName, formula, dx, 2, cmx.id);
					pm.setModelClass(cmx.modelClass);
					pm.setComment(cmx.comment);
					pm.setParameter(paramXml);
					pm.setIndependent(indepXml);
					pm.setFormula(pm.revertFormula());
					pm.setMLit(mLitXmlDoc);
					pm.setEstLit(emLitXmlDoc);

					String[] attrs = new String[] { Model2Schema.ATT_MODELCATALOG, Model2Schema.ATT_MLIT };
					String[] dbTablenames = new String[] { "Modellkatalog", "Literatur" };

					boolean checkAnywayDueToNegativeId = (pm.getModelId() < 0);
					String rowuuid = row.getString(Model2Schema.ATT_DBUUID);
					if (rowuuid == null) rowuuid = cmx.dbuuid;
					foreignDbIds = checkIDs(conn, true, dbuuid, row, pm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
					db.insertM(pm);
					foreignDbIds = checkIDs(conn, false, dbuuid, row, pm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
					if (!pm.warning.trim().isEmpty()) warnings += pm.warning;
					//}
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

	// Modelle
	private HashMap<String, HashMap<String, HashMap<Integer, Integer>>> checkIDs(Connection conn, boolean before, String dbuuid, KnimeTuple row, ParametricModel pm,
			HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds, String[] schemaAttr, String[] dbTablename, String rowuuid, boolean checkAnywayDueToNegativeId)
			throws PmmException {
		if (checkAnywayDueToNegativeId || rowuuid == null || !rowuuid.equals(dbuuid)) {
			if (!foreignDbIds.containsKey(rowuuid)) foreignDbIds.put(rowuuid, new HashMap<String, HashMap<Integer, Integer>>());
			HashMap<String, HashMap<Integer, Integer>> d = foreignDbIds.get(rowuuid);

			for (int i = 0; i < schemaAttr.length; i++) {
				if (!d.containsKey(dbTablename[i])) d.put(dbTablename[i], new HashMap<Integer, Integer>());
				if (before) DBKernel.getKnownIDs4PMM(conn, d.get(dbTablename[i]), dbTablename[i], rowuuid);
				HashMap<Integer, Integer> h = CellIO.setMIDs(before, schemaAttr[i], dbTablename[i], d.get(dbTablename[i]), row, pm);
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
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		getInSchema(inSpecs[0]);
		return null;
	}

	private KnimeSchema getInSchema(final DataTableSpec inSpec) throws InvalidSettingsException {
		KnimeSchema result = null;
		String errorMsg = "Unexpected format - Model definitions are not present in the columns of the incoming table";
		KnimeSchema inSchema = new Model1Schema();
		try {
			if (inSchema.conforms(inSpec)) {
				result = inSchema;
			}
		} catch (PmmException e) {
		}
		inSchema = new Model2Schema();
		try {
			if (inSchema.conforms(inSpec)) {
				if (result == null) {
					result = inSchema;
				} else {
					result = KnimeSchema.merge(result, inSchema);
				}
			}
		} catch (PmmException e) {
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
		 * settings.addString( PARAM_FILENAME, filename ); settings.addString(
		 * PARAM_LOGIN, login ); settings.addString( PARAM_PASSWD, passwd );
		 * settings.addBoolean( PARAM_OVERRIDE, override );
		 */
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
