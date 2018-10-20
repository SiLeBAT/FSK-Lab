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
package de.bund.bfr.knime.pmm.estimatedmodelwriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.WorkflowManager;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of EstimatedModelWriter.
 * 
 * 
 * @author Jorgen Brandt
 */
public class EstimatedModelWriterNodeModel extends NodeModel {
	/*
	 * static final String PARAM_FILENAME = "filename"; static final String
	 * PARAM_LOGIN = "login"; static final String PARAM_PASSWD = "passwd";
	 * static final String PARAM_OVERRIDE = "override";
	 * 
	 * private String filename; private String login; private String passwd;
	 * private boolean override;
	 */
	private HashMap<Integer, ParametricModel> alreadyInsertedModel = null;
	private HashMap<Integer, ParametricModel> alreadyInsertedEModel = null;
	private Connection conn = null;
	private String warnings = "";
	private HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds = null;
	private Bfrdb db = null;
	
	/**
	 * Constructor for the node model.
	 */
	protected EstimatedModelWriterNodeModel() {

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

		db = null;
		/*
		 * if( override ) { db = new Bfrdb( filename, login, passwd ); } else {
		 * db = new Bfrdb(DBKernel.getLocalConn(true)); }
		 */
		try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {
		}
		conn = db.getConnection();
		conn.setReadOnly(false);
		/*
		 * IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot(); for
		 * (IResource resource : root.members()) {
		 * //resource.getWorkspace().save(true, null);
		 * //System.err.println(resource.getName()); if
		 * (resource.getName().equals("Estimation")) { File dest = new
		 * File("/temp/pmmlabfolder"); if (dest.exists()) dest.delete();
		 * FileUtils.copyDirectory(new File(resource.getLocationURI()), dest,
		 * true); } } if (true) return null;
		 */
		long n = inData[0].size();

		KnimeSchema inSchema = getInSchema(inData[0].getDataTableSpec());
		boolean tsConform = inSchema.conforms(new TimeSeriesSchema());
		boolean model1Conform = inSchema.conforms(new Model1Schema());
		boolean model2Conform = inSchema.conforms(new Model2Schema());
		boolean secOnly = model2Conform && !model1Conform && !tsConform;
		Integer rowEstM2ID = null;
		KnimeRelationReader reader = new KnimeRelationReader(inSchema, inData[0]);
		foreignDbIds = new HashMap<>();
		String dbuuid = db.getDBUUID();

		HashMap<Integer, List<Integer>> secModels = new HashMap<>();
		HashMap<Integer, HashSet<Integer>> globalModels = new HashMap<>();
		ParametricModel ppm = null, spm;

		int j = 0;
		alreadyInsertedModel = new HashMap<>();
		alreadyInsertedEModel = new HashMap<>();
		HashMap<Integer, Integer> alreadyInsertedGModel = new HashMap<>();
		HashMap<Integer, PmmTimeSeries> alreadyInsertedTs = new HashMap<>();
		boolean M1Writable = false, M2Writable = false;
		warnings = "";
		Integer wfID = saveWF(exec);
		while (reader.hasMoreElements()) {
			exec.setProgress((double) j++ / n);

			KnimeTuple row = reader.nextElement();

			Integer newTsID = null;
			if (!secOnly) {
				// TimeSeries
				PmmTimeSeries ts = new PmmTimeSeries(row);
				int rowTsID = ts.getCondId();
				if (alreadyInsertedTs.containsKey(rowTsID)) {
					ts = alreadyInsertedTs.get(rowTsID);
					newTsID = ts.getCondId();
				} else {
					String[] attrs = new String[] { TimeSeriesSchema.ATT_CONDID, TimeSeriesSchema.ATT_MISC, TimeSeriesSchema.ATT_AGENT, TimeSeriesSchema.ATT_MATRIX,
							TimeSeriesSchema.ATT_LITMD };
					String[] dbTablenames = new String[] { "Versuchsbedingungen", "Sonstiges", "Agenzien", "Matrices", "Literatur" };

					boolean checkAnywayDueToNegativeId = (ts.getCondId() < 0);
					String rowuuid = row.getString(TimeSeriesSchema.ATT_DBUUID);
					if (rowuuid == null) rowuuid = ts.getDbuuid();
					if (rowuuid == null && ts.getMatrix() != null && ts.getMatrix().size() > 0) {
						rowuuid = ((MatrixXml) ts.getMatrix().get(0)).getDbuuid();
					}
					
					foreignDbIds = checkIDs(conn, true, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
					newTsID = db.insertTs(ts);
					foreignDbIds = checkIDs(conn, false, dbuuid, row, ts, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
					
					//ts.setCondId(newTsID);
					alreadyInsertedTs.put(rowTsID, ts);

					String text = ts.getWarning();
					if (text != null && !text.trim().isEmpty()) {
						if (warnings.indexOf(text) < 0) warnings += text + "\n";
					}
				}
			}

			if (newTsID != null) {
				Integer newPrimEstID = null;
				EstModelXml emx = null;
				PmmXmlDoc estModel = row.getPmmXml(Model1Schema.ATT_ESTMODEL);
				if (estModel != null) {
					for (PmmXmlElementConvertable el : estModel.getElementSet()) {
						if (el instanceof EstModelXml) {
							emx = (EstModelXml) el;
							break;
						}
					}
				}
				Integer rowEstM1ID = emx.getId();//row.getInt(Model1Schema.ATT_ESTMODELID);
				Integer dw = row.getInt(Model1Schema.ATT_DATABASEWRITABLE);
				M1Writable = (dw != null && dw == 1);
				if (M1Writable) {
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

					Integer rowMcID = cmx.id;//row.getInt(Model1Schema.ATT_MODELID);
					String modelName = cmx.name;//row.getString(Model1Schema.ATT_MODELNAME);
					String formula = cmx.formula;//row.getString(Model1Schema.ATT_FORMULA);
					PmmXmlDoc depXml = row.getPmmXml(Model1Schema.ATT_DEPENDENT);
					DepXml dx = (DepXml) depXml.getElementSet().get(0);

					PmmXmlDoc paramXml = row.getPmmXml(Model1Schema.ATT_PARAMETER);
					PmmXmlDoc indepXml = row.getPmmXml(Model1Schema.ATT_INDEPENDENT);

					PmmXmlDoc mLitXmlDoc = row.getPmmXml(Model1Schema.ATT_MLIT);
					PmmXmlDoc emLitXmlDoc = row.getPmmXml(Model1Schema.ATT_EMLIT);

					Double rms = emx.getRms();//row.getDouble(Model1Schema.ATT_RMS);
					Double r2 = emx.getR2();//row.getDouble(Model1Schema.ATT_RSQUARED);
					Double aic = emx.getAic();//row.getDouble(Model1Schema.ATT_AIC);
					Double bic = emx.getBic();//row.getDouble(Model1Schema.ATT_BIC);

					// Modellkatalog primary
					if (alreadyInsertedModel.containsKey(rowMcID)) {
						ppm = alreadyInsertedModel.get(rowMcID);
					} else {
						ppm = new ParametricModel(modelName, formula, dx, 1, rowMcID); // , rowEstM1ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM1ID
						ppm.setModelClass(cmx.modelClass);
						ppm.setParameter(paramXml);
						ppm.setIndependent(indepXml);
						ppm.setFormula(ppm.revertFormula());
						ppm.setMLit(mLitXmlDoc);

						String[] attrs = new String[] { Model1Schema.ATT_MODELCATALOG, Model1Schema.ATT_MLIT };
						String[] dbTablenames = new String[] { "Modellkatalog", "Literatur" };

						boolean checkAnywayDueToNegativeId = (rowMcID < 0);
						String rowuuid = row.getString(Model1Schema.ATT_DBUUID);
						if (rowuuid == null) rowuuid = cmx.dbuuid;
						
						foreignDbIds = checkIDs(conn, true, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
						db.insertM(ppm);
						foreignDbIds = checkIDs(conn, false, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);

						alreadyInsertedModel.put(rowMcID, ppm);
						if (!ppm.getWarning().trim().isEmpty()) warnings += ppm.getWarning();
					}
					try {
						ppm.setFittedModelName(emx.getName());
						ppm.setRms(rms == null ? Double.NaN : rms);
						ppm.setRsquared(r2 == null ? Double.NaN : r2);
						ppm.setAic(aic == null ? Double.NaN : aic);
						ppm.setBic(bic == null ? Double.NaN : bic);
						ppm.setQualityScore(emx.getQualityScore());
						ppm.setChecked(emx.getChecked());
						ppm.setComment(emx.getComment());
					} catch (Exception e) {
						warnings += e.getMessage() + " -> ID: " + rowEstM1ID;
						MyLogger.handleException(e);
					}

					ppm.setCondId(newTsID);
					if (alreadyInsertedEModel.containsKey(rowEstM1ID)) {
						newPrimEstID = alreadyInsertedEModel.get(rowEstM1ID).getEstModelId();
					} else {
						ppm.setEstModelId(rowEstM1ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM1ID);
						ppm.setParameter(paramXml);
						ppm.setIndependent(indepXml);
						ppm.setDepXml(dx);
						ppm.setEstLit(emLitXmlDoc);

						String[] attrs = new String[] { Model1Schema.ATT_ESTMODEL, Model1Schema.ATT_EMLIT };
						String[] dbTablenames = new String[] { "GeschaetzteModelle", "Literatur" };

						boolean checkAnywayDueToNegativeId = (ppm.getEstModelId() < 0);
						String rowuuid = row.getString(Model1Schema.ATT_DBUUID);
						if (rowuuid == null) rowuuid = emx.getDbuuid();
						
						foreignDbIds = checkIDs(conn, true, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
						newPrimEstID = db.insertEm(ppm, wfID);
						foreignDbIds = checkIDs(conn, false, dbuuid, row, ppm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);

						if (newPrimEstID != null) {
							//ppm.setEstModelId(newPrimEstID);
							alreadyInsertedEModel.put(rowEstM1ID, ppm.clone());
						}
						if (!ppm.getWarning().trim().isEmpty()) warnings += ppm.getWarning();
					}
				} else {
					String text = "Estimated primary model (ID: " + rowEstM1ID + //row.getInt(Model1Schema.ATT_ESTMODELID) +
							") is not storable due to joining with unassociated kinetic data\n";
					if (warnings.indexOf(text) < 0) warnings += text;
				}
				
				if (model2Conform && newPrimEstID != null) {
					dw = row.getInt(Model2Schema.ATT_DATABASEWRITABLE);
					M2Writable = (dw != null && dw == 1);
					//rowEstM2ID = row.getInt(Model2Schema.ATT_ESTMODELID);
					emx = null;
					estModel = row.getPmmXml(Model2Schema.ATT_ESTMODEL);
					if (estModel != null) {
						for (PmmXmlElementConvertable el : estModel.getElementSet()) {
							if (el instanceof EstModelXml) {
								emx = (EstModelXml) el;
								break;
							}
						}
					}
					rowEstM2ID = emx.getId();
					//System.err.println(newPrimEstID + "\t" + rowEstM2ID);
					if (M2Writable) {
						spm = writeM2(row, emx, ppm, dbuuid, wfID);
						
						
						if (!secModels.containsKey(spm.getEstModelId())) secModels.put(spm.getEstModelId(), new ArrayList<Integer>());
						secModels.get(spm.getEstModelId()).add(newPrimEstID);
						Integer gmSchemaID = row.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
						Integer newGlobalModelId;
						if (alreadyInsertedGModel.containsKey(gmSchemaID)) {
							newGlobalModelId = alreadyInsertedGModel.get(gmSchemaID);
						} else {
							foreignDbIds = checkID(conn, true, dbuuid, row, gmSchemaID, null, foreignDbIds, row.getString(Model2Schema.ATT_DBUUID));
							newGlobalModelId = db.insertGm(row.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));
							foreignDbIds = checkID(conn, false, dbuuid, row, gmSchemaID, newGlobalModelId, foreignDbIds, row.getString(Model2Schema.ATT_DBUUID));
							alreadyInsertedGModel.put(gmSchemaID, newGlobalModelId);
						}
						if (!globalModels.containsKey(newGlobalModelId)) globalModels.put(newGlobalModelId, new HashSet<Integer>());
						globalModels.get(newGlobalModelId).add(spm.getEstModelId());
						//}
					} else {
						String text = "Estimated secondary model (ID: " + rowEstM2ID + ") is not storable due to joining with unassociated primary model\n";
						if (warnings.indexOf(text) < 0) warnings += text;
					}
				} else {
					//System.err.println("newPrimEstID: " + newPrimEstID);
				}
			}
			else if (secOnly) {
				Integer dw = row.getInt(Model2Schema.ATT_DATABASEWRITABLE);
				M2Writable = (dw != null && dw == 1);
				//rowEstM2ID = row.getInt(Model2Schema.ATT_ESTMODELID);
				EstModelXml emx = null;
				PmmXmlDoc estModel = row.getPmmXml(Model2Schema.ATT_ESTMODEL);
				if (estModel != null) {
					for (PmmXmlElementConvertable el : estModel.getElementSet()) {
						if (el instanceof EstModelXml) {
							emx = (EstModelXml) el;
							break;
						}
					}
				}
				rowEstM2ID = emx.getId();
				//System.err.println(newPrimEstID + "\t" + rowEstM2ID);
				if (M2Writable) {
					spm = writeM2(row, emx, ppm, dbuuid, wfID);
				}
			}
		}
		if (model2Conform && !secOnly) {
			if (M2Writable) {
				for (Integer gmId : globalModels.keySet()) {
					HashSet<Integer> secModelIDs = globalModels.get(gmId);
					for (Integer estModelId : secModelIDs) {
						db.insertEm2(estModelId, secModels.get(estModelId), gmId);
					}
				}
			} else {
				String text = "Estimated secondary model (ID: " + rowEstM2ID + ") is not storable due to joining with unassociated primary model\n";
				if (warnings.indexOf(text) < 0) warnings += text;
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
	private ParametricModel writeM2(KnimeTuple row, EstModelXml emx, ParametricModel ppm, String dbuuid, Integer wfID) {
		ParametricModel spm = null;
		Integer rowEstM2ID = emx.getId();
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
		Integer rowMcID = cmx.id;//row.getInt(Model2Schema.ATT_MODELID);
		String modelName = cmx.name;//row.getString(Model2Schema.ATT_MODELNAME);
		String formula = cmx.formula;//row.getString(Model2Schema.ATT_FORMULA);
		PmmXmlDoc depXml = row.getPmmXml(Model2Schema.ATT_DEPENDENT);
		DepXml dx = (DepXml) depXml.getElementSet().get(0);

		PmmXmlDoc paramXml = row.getPmmXml(Model2Schema.ATT_PARAMETER);
		PmmXmlDoc indepXml = row.getPmmXml(Model2Schema.ATT_INDEPENDENT);

		PmmXmlDoc mLitXmlDoc = row.getPmmXml(Model2Schema.ATT_MLIT);
		PmmXmlDoc emLitXmlDoc = row.getPmmXml(Model2Schema.ATT_EMLIT);

		Double rms = emx.getRms();//row.getDouble(Model2Schema.ATT_RMS);
		Double r2 = emx.getR2();//row.getDouble(Model2Schema.ATT_RSQUARED);
		Double aic = emx.getAic();//row.getDouble(Model2Schema.ATT_AIC);
		Double bic = emx.getBic();//row.getDouble(Model2Schema.ATT_BIC);

		// Modellkatalog secondary
		if (alreadyInsertedModel.containsKey(rowMcID)) {
			spm = alreadyInsertedModel.get(rowMcID);
		} else {
			spm = new ParametricModel(modelName, formula, dx, 2, rowMcID, rowEstM2ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM2ID);
			spm.setModelClass(cmx.modelClass);
			spm.setParameter(paramXml);
			spm.setIndependent(indepXml);
			spm.setFormula(spm.revertFormula());
			spm.setMLit(mLitXmlDoc);

			String[] attrs = new String[] { Model2Schema.ATT_MODELCATALOG, Model2Schema.ATT_MLIT };
			String[] dbTablenames = new String[] { "Modellkatalog", "Literatur" };

			boolean checkAnywayDueToNegativeId = (rowMcID < 0);
			String rowuuid = row.getString(Model2Schema.ATT_DBUUID);
			if (rowuuid == null) rowuuid = cmx.dbuuid;
			
			foreignDbIds = checkIDs(conn, true, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
			db.insertM(spm);
			foreignDbIds = checkIDs(conn, false, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);

			alreadyInsertedModel.put(rowMcID, spm);
			if (!spm.getWarning().trim().isEmpty()) warnings += spm.getWarning();
		}

		if (alreadyInsertedEModel.containsKey(rowEstM2ID)) {
			spm = alreadyInsertedEModel.get(rowEstM2ID);
		} else {
			try {
				spm.setFittedModelName(emx.getName());
				spm.setRms(rms);
				spm.setRsquared(r2);
				spm.setAic(aic);
				spm.setBic(bic);
				spm.setQualityScore(emx.getQualityScore());
				spm.setChecked(emx.getChecked());
				spm.setComment(emx.getComment());
			} catch (Exception e) {
				warnings += e.getMessage() + " -> ID: " + rowEstM2ID;
				MyLogger.handleException(e);
			}
			spm.setEstModelId(rowEstM2ID == null ? MathUtilities.getRandomNegativeInt() : rowEstM2ID);
			spm.setParameter(paramXml);
			spm.setIndependent(indepXml);
			spm.setDepXml(dx);
			spm.setEstLit(emLitXmlDoc);

			String[] attrs = new String[] { Model2Schema.ATT_ESTMODEL, Model2Schema.ATT_EMLIT };
			String[] dbTablenames = new String[] { "GeschaetzteModelle", "Literatur" };

			boolean checkAnywayDueToNegativeId = (spm.getEstModelId() < 0);
			String rowuuid = row.getString(Model2Schema.ATT_DBUUID);
			if (rowuuid == null) rowuuid = emx.getDbuuid();
			foreignDbIds = checkIDs(conn, true, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
			db.insertEm(spm, wfID, ppm);				
			foreignDbIds = checkIDs(conn, false, dbuuid, row, spm, foreignDbIds, attrs, dbTablenames, rowuuid, checkAnywayDueToNegativeId);
			alreadyInsertedEModel.put(rowEstM2ID, spm.clone());
			if (!spm.getWarning().trim().isEmpty()) warnings += spm.getWarning();
		}		
		
		return spm;
	}

	// GlobalModels
	private HashMap<String, HashMap<String, HashMap<Integer, Integer>>> checkID(Connection conn, boolean before, String dbuuid, KnimeTuple row, Integer oldID, Integer newID,
			HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds, String rowuuid) throws PmmException {
		if (rowuuid == null || !rowuuid.equals(dbuuid)) {
			if (!foreignDbIds.containsKey(rowuuid)) foreignDbIds.put(rowuuid, new HashMap<String, HashMap<Integer, Integer>>());
			HashMap<String, HashMap<Integer, Integer>> d = foreignDbIds.get(rowuuid);

			if (!d.containsKey("GlobalModels")) d.put("GlobalModels", new HashMap<Integer, Integer>());
			if (before) DBKernel.getKnownIDs4PMM(conn, d.get("GlobalModels"), "GlobalModels", rowuuid);

			if (oldID != null) {
				if (d.get("GlobalModels").containsKey(oldID)) {
					if (before) row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, d.get("GlobalModels").get(oldID));//schemaTuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, d.get("GlobalModels").get(id));
					else if (d.get("GlobalModels").get(oldID).intValue() != row.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID).intValue()) {
						System.err.println("fillNewIDsIntoForeign ... shouldn't happen");
					}
				} else {
					if (before) row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, MathUtilities.getRandomNegativeInt());//d.get("GlobalModels").put(oldID, MathUtilities.getRandomNegativeInt());
					else d.get("GlobalModels").put(oldID, newID);
				}
			}
			//if (!before) DBKernel.setKnownIDs4PMM(conn, d.get("GlobalModels"), "GlobalModels", rowuuid);

			foreignDbIds.put(rowuuid, d);
		}
		return foreignDbIds;
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

	// TimeSeries
	private HashMap<String, HashMap<String, HashMap<Integer, Integer>>> checkIDs(Connection conn, boolean before, String dbuuid, KnimeTuple row, KnimeTuple ts,
			HashMap<String, HashMap<String, HashMap<Integer, Integer>>> foreignDbIds, String[] schemaAttr, String[] dbTablename, String rowuuid, boolean checkAnywayDueToNegativeId)
			throws PmmException {
		if (checkAnywayDueToNegativeId || rowuuid == null || !rowuuid.equals(dbuuid)) {
			if (!foreignDbIds.containsKey(rowuuid)) foreignDbIds.put(rowuuid, new HashMap<String, HashMap<Integer, Integer>>());
			HashMap<String, HashMap<Integer, Integer>> d = foreignDbIds.get(rowuuid);

			for (int i = 0; i < schemaAttr.length; i++) {
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

	private Integer saveWF(final ExecutionContext exec) throws Exception {
		Integer result = null;
		for (NodeContainer nc : WorkflowManager.ROOT.getNodeContainers()) {
			if (nc instanceof WorkflowManager) {
				WorkflowManager wfm = (WorkflowManager) nc;
				for (EstimatedModelWriterNodeModel m : wfm.findNodes(EstimatedModelWriterNodeModel.class, true).values()) {
					if (m == this) {
						File wfdir = wfm.getWorkingDir().getFile();
						wfm.save(wfdir, exec, true);
						String wfname = wfdir.getName();
						String zipfile = System.getProperty("java.io.tmpdir") + "/" + wfname + "_" + System.currentTimeMillis() + ".zip";
						zipDirectory(wfdir, zipfile);

						String sql = "INSERT INTO " + DBKernel.delimitL("PMMLabWorkflows") + " (" + DBKernel.delimitL("Workflow") + ") VALUES ('" + wfname + "');";
						PreparedStatement psmt = DBKernel.getDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						if (psmt.executeUpdate() > 0) {
							result = DBKernel.getLastInsertedID(psmt);
							File zipFile = new File(zipfile);
							DBKernel.insertBLOB("PMMLabWorkflows", "Workflow", zipFile, result);
						}
						psmt.close();
					}
				}
			}
		}
		return result;
	}

	private void zipDirectory(File dir, String zipDirName) {
		try {
			List<String> filesListInDir = populateFilesList(null, dir);
			//now zip files one by one
			//create ZipOutputStream to write to the zip file
			FileOutputStream fos = new FileOutputStream(zipDirName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String filePath : filesListInDir) {
				//for ZipEntry we need to keep only relative file path, so we used substring on absolute path
				ZipEntry ze = new ZipEntry(filePath.substring(dir.getParentFile().getAbsolutePath().length() + 1, filePath.length()));
				zos.putNextEntry(ze);
				//read the file and write to ZipOutputStream
				FileInputStream fis = new FileInputStream(filePath);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
				fis.close();
			}
			zos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> populateFilesList(List<String> filesListInDir, File dir) throws IOException {
		if (filesListInDir == null) filesListInDir = new ArrayList<>();
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (!file.getName().equals(".knimeLock")) filesListInDir.add(file.getAbsolutePath());
			} else {
				filesListInDir = populateFilesList(filesListInDir, file);
			}
		}
		return filesListInDir;
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
		KnimeSchema inSchema = new TimeSeriesSchema();
		boolean hasTS = false;
		try {
			if (inSchema.conforms(inSpec)) {
				result = inSchema;
				hasTS = true;
			}
		} catch (PmmException e) {
		}
		
		boolean hasM1 = false;
		inSchema = new Model1Schema();
		try {
			if (inSchema.conforms(inSpec)) {
				result = (result == null ? inSchema : KnimeSchema.merge(result, inSchema));
				hasM1 = true;
			}
		} catch (PmmException e) {
		}
		
		boolean hasM2 = false;
		inSchema = new Model2Schema();
		try {
			if (inSchema.conforms(inSpec)) {
				result = (result == null ? inSchema : KnimeSchema.merge(result, inSchema));
				hasM2 = true;
			}
		} catch (PmmException e) {
		}
		
		if (hasM2 && !hasM1 && !hasTS) { // ok, save, only secondary model, like z-value
			;
		}
		else if (!hasTS) {
			throw new InvalidSettingsException("Unexpected format - it is not possible to save fitted models without microbial data information");
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
