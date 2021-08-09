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
package de.bund.bfr.knime.pcml.node.xls2pcml;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.xmlbeans.XmlCursor;
import org.knime.core.data.DataRow;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.IntValue;
import org.knime.core.data.StringValue;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.knime.pcml.port.PCMLPortObjectSpec;
import de.bund.bfr.knime.pcml.port.PCMLUtil;
import de.bund.bfr.pcml10.NameAndDatabaseId;
import de.bund.bfr.pcml10.PCMLDocument;
import de.bund.bfr.pcml10.DataTableDocument.DataTable;
import de.bund.bfr.pcml10.InlineTableDocument.InlineTable;
import de.bund.bfr.pcml10.InportDocument.Inport;
import de.bund.bfr.pcml10.OutportRefDocument.OutportRef;
import de.bund.bfr.pcml10.PCMLDocument.PCML;
import de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData;
import de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain;
import de.bund.bfr.pcml10.ProcessDataDocument.ProcessData;
import de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode;
import de.bund.bfr.pcml10.RowDocument.Row;

public class XLS2PCMLNodeModel extends NodeModel {

	static final PortType[] inPortTypes = { BufferedDataTable.TYPE, BufferedDataTable.TYPE, BufferedDataTable.TYPE };
	static final PortType[] outPortTypes = { PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class) };

	protected XLS2PCMLNodeModel() {
		super(inPortTypes, outPortTypes);
	}

	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { new PCMLPortObjectSpec() };
	}

	@Override
	protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
		PCMLDocument pcmlDoc = PCMLUtil.create();
		createPcData(pcmlDoc, inObjects);

		PCMLPortObject pcmlPort = PCMLPortObject.create(pcmlDoc.toString());
		return new PortObject[] { pcmlPort };
	}

	private void createPcData(PCMLDocument pcmlDoc, final PortObject[] inObjects) {
		PCML pcml = pcmlDoc.getPCML();
		ProcessChain pChain = pcml.addNewProcessChain();

		// Fetches model metadata from the 1st table (metadata table)
		List<ModelMetadata> modelMetadataList = fetchMetadata((BufferedDataTable) inObjects[0]);
		// Fetches concentrations from the 2nd table (concentrations table)
		List<ConcentrationData> concs = fetchConcentrations((BufferedDataTable) inObjects[1]);
		// Fetches conditons from the 3rd table (conditions table)
		List<ConditionsData> conds = fetchConditions((BufferedDataTable) inObjects[2]);

		// For each process
		int currModelID = -1;
		String currProcName = "";
		for (ConditionsData cond : conds) {
			if (currModelID == cond.modelID && currProcName.equals(cond.procName)) {
				continue;
			}
			currModelID = cond.modelID;
			currProcName = cond.procName;

			// Creates a ProcessNode
			ProcessNode processNode = pChain.addNewProcessNode();
			processNode.setId(UUID.randomUUID().toString());
			// Fills food ProcessNode
			NameAndDatabaseId process = processNode.addNewProcess();
			String procName = String.format("%d_%s", currModelID, currProcName);
			process.setName(procName);
		}

		// 4. Set inport Refs
		ProcessNode[] processNodes = pChain.getProcessNodeArray();
		for (int i = 0; i < processNodes.length; i++) {
			Inport inport = processNodes[i].addNewInport();
			OutportRef outRef = inport.addNewOutportRef();
			outRef.setOutIndex(i);
		}

		ProcessChainData processChainData = pcml.addNewProcessChainData();
		
		// Creates column list
		ExpColumnList colList = new ExpColumnList(PCMLUtil.getPCMLNamespace(pcmlDoc));

		for (ProcessNode processNode : pChain.getProcessNodeArray()) {

			// Gets model id and process name
			NameAndDatabaseId proc = processNode.getProcess();
			String[] tokens = proc.getName().split("_");
			int modelID = Integer.parseInt(tokens[0]);
			String procName = tokens[1];

			// Gets conditions with model id (filter conditions)
			LinkedList<ConditionsData> procConds = new LinkedList<>();
			for (ConditionsData cond : conds) {
				if (cond.modelID == modelID && cond.procName.equals(procName)) {
					procConds.add(cond);
				}
			}

			int lastTime = procConds.getLast().time;

			// Get concentrations with model id and in the time range of this
			// process
			LinkedList<ConcentrationData> procConcs = new LinkedList<>();
			for (ConcentrationData conc : concs) {
				if (conc.modelID == modelID && conc.time <= lastTime) {
					procConcs.add(conc);
				}
			}

			LinkedList<DataPoint> dataPoints = getDataPoints(procConcs, procConds);

			// Adds process data
			ProcessData pData = processChainData.addNewProcessData();
			pData.setRef(processNode.getId());

			// Absolute time at process start is always zero
			pData.setTime(dataPoints.getFirst().time);

			DataTable table = createDataTable(colList, dataPoints);
			pData.setDataTable(table);
		}
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void reset() {
	}

	/**
	 * Fetch data from the metadata table.
	 */
	private List<ModelMetadata> fetchMetadata(BufferedDataTable table) {

		List<ModelMetadata> modelMetadataList = new LinkedList<>();
		CloseableRowIterator itr = table.iterator();

		while (itr.hasNext()) {
			DataRow row = itr.next();

			// Fetches data from the value
			int modelID = ((IntValue) row.getCell(0)).getIntValue();
			String matrix = ((StringValue) row.getCell(1)).getStringValue();
			String matrixDetails = ((StringValue) row.getCell(2)).getStringValue();
			String agent = ((StringValue) row.getCell(3)).getStringValue();
			String agentDetails = ((StringValue) row.getCell(4)).getStringValue();

			// Creates and stores model metadata
			ModelMetadata modelMetadata = new ModelMetadata(modelID, matrix, matrixDetails, agent, agentDetails);
			modelMetadataList.add(modelMetadata);
		}

		return modelMetadataList;
	}

	/**
	 * Fetch data from the concentrations table.
	 */
	private List<ConcentrationData> fetchConcentrations(BufferedDataTable table) {

		List<ConcentrationData> concs = new LinkedList<>();

		CloseableRowIterator itr = table.iterator();
		while (itr.hasNext()) {
			DataRow row = itr.next();

			if (row.getCell(2).isMissing())
				continue;

			// Fetches data from table
			int modelID = ((IntValue) row.getCell(0)).getIntValue();
			int time = ((IntValue) row.getCell(1)).getIntValue();
			float conc = (float) ((DoubleValue) row.getCell(2)).getDoubleValue();

			concs.add(new ConcentrationData(modelID, time, conc));
		}

		return concs;
	}

	/**
	 * Fetch data from the conditions table.
	 */
	private List<ConditionsData> fetchConditions(BufferedDataTable table) {

		/**
		 * Two types of conditions tables:
		 * <ol>
		 * <li>Short tables. 4 columns: ID, Time, Temperature, pH.
		 * <li>Big tables. 10 columns: ID, Time, Temperature, pH, salt
		 * concentration, water activity, water concentration, air humidity,
		 * process name, and comment
		 * </ol>
		 */
		boolean shortTable = table.getDataTableSpec().getNumColumns() == 4;

		List<ConditionsData> conds = new LinkedList<>();

		CloseableRowIterator itr = table.iterator();

		while (itr.hasNext()) {
			DataRow row = itr.next();

			// Fetches data from table
			int modelID = ((IntValue) row.getCell(0)).getIntValue();
			int time = ((IntValue) row.getCell(1)).getIntValue();

			Float temperature;
			if (row.getCell(2).isMissing()) {
				temperature = null;
			} else {
				temperature = (float) ((DoubleValue) row.getCell(2)).getDoubleValue();
			}

			Float pH;
			if (row.getCell(3).isMissing()) {
				pH = null;
			} else {
				pH = (float) ((DoubleValue) row.getCell(3)).getDoubleValue();
			}

			Float saltConcentration = null;
			Float waterActivity = null;
			Float waterConcentration = null;
			Integer airHumidity = null;
			String processName = null;
			String comment = null;

			if (!shortTable) {
				if (!row.getCell(4).isMissing()) {
					saltConcentration = (float) ((DoubleValue) row.getCell(4)).getDoubleValue();
				}

				if (!row.getCell(5).isMissing()) {
					waterActivity = (float) ((DoubleValue) row.getCell(5)).getDoubleValue();
				}

				if (!row.getCell(6).isMissing()) {
					waterConcentration = (float) ((DoubleValue) row.getCell(6)).getDoubleValue();
				}

				if (!row.getCell(7).isMissing()) {
					airHumidity = ((IntValue) row.getCell(7)).getIntValue();
				}

				if (!row.getCell(8).isMissing()) {
					processName = ((StringValue) row.getCell(8)).getStringValue();
				}

				if (!row.getCell(9).isMissing()) {
					comment = ((StringValue) row.getCell(9)).getStringValue();
				}
			}

			ConditionsData cond = new ConditionsData(modelID, time, temperature, pH, saltConcentration, waterActivity,
					waterConcentration, airHumidity, processName, comment);
			conds.add(cond);
		}

		return conds;
	}

	private LinkedList<DataPoint> getDataPoints(LinkedList<ConcentrationData> concs, LinkedList<ConditionsData> conds) {

		// Gets time values
		List<Integer> timeValues = new LinkedList<>();
		for (ConcentrationData conc : concs) {
			timeValues.add(conc.time);
		}
		for (ConditionsData cond : conds) {
			timeValues.add(cond.time);
		}
		Collections.sort(timeValues);

		// Builds list of points
		LinkedList<DataPoint> dataPoints = new LinkedList<>();
		for (int time : timeValues) {
			Float conc = null;
			Float temp = null;
			Float pH = null;
			Float nacl = null;
			Float aw = null;
			Float waterConc = null;
			Integer airHum = null;

			for (ConcentrationData concData : concs) {
				if (concData.time == time) {
					conc = concData.conc;
					break;
				}
			}

			for (ConditionsData condData : conds) {
				if (condData.time == time) {
					temp = condData.temp;
					pH = condData.pH;
					nacl = condData.nacl;
					aw = condData.aw;
					waterConc = condData.waterConc;
					airHum = condData.airHum;
					break;
				}
			}

			// Builds and adds data point
			dataPoints.add(new DataPoint(time, conc, temp, pH, nacl, aw, waterConc, airHum));
		}

		return dataPoints;
	}

	private DataTable createDataTable(ExpColumnList colList, List<DataPoint> dataPoints) {
		DataTable table = (DataTable) DataTable.Factory.newInstance();
		
		table.setColumnList(colList.columnList);

		// Sets data sets
		InlineTable inlineTable = table.addNewInlineTable();

		for (DataPoint dataPoint : dataPoints) {
			Row row = inlineTable.addNewRow();
			XmlCursor cur = row.newCursor();
			cur.toFirstContentToken();

			cur.insertElementWithText(colList.timeColName, Integer.toString(dataPoint.time));

			if (dataPoint.conc == null) {
				cur.insertElement(colList.concColName);
			} else {
				cur.insertElementWithText(colList.concColName, Double.toString(dataPoint.conc));
			}

			if (dataPoint.temp == null) {
				cur.insertElement(colList.tempColName);
			} else {
				cur.insertElementWithText(colList.tempColName, dataPoint.temp.toString());
			}

			if (dataPoint.pH == null) {
				cur.insertElement(colList.pHColName);
			} else {
				cur.insertElementWithText(colList.pHColName, dataPoint.pH.toString());
			}

			if (dataPoint.nacl == null) {
				cur.insertElement(colList.naclColName);
			} else {
				cur.insertElementWithText(colList.naclColName, dataPoint.nacl.toString());
			}

			if (dataPoint.aw == null) {
				cur.insertElement(colList.awColName);
			} else {
				cur.insertElementWithText(colList.awColName, dataPoint.aw.toString());
			}

			if (dataPoint.waterConc == null) {
				cur.insertElement(colList.waterConcColName);
			} else {
				cur.insertElementWithText(colList.waterConcColName, dataPoint.waterConc.toString());
			}

			if (dataPoint.airHum == null) {
				cur.insertElement(colList.airHumColName);
			} else {
				cur.insertElementWithText(colList.airHumColName, dataPoint.airHum.toString());
			}

			cur.dispose();
		}

		return table;
	}
}

class ModelMetadata {

	int modelID;
	String matrix;
	String matrixDetails;
	String agent;
	String agentDetails;

	public ModelMetadata(int modelID, String matrix, String matrixDetails, String agent, String agentDetails) {
		this.modelID = modelID;
		this.matrix = matrix;
		this.matrixDetails = matrixDetails;
		this.agent = agent;
		this.agentDetails = agentDetails;
	}
}

class ConcentrationData {
	int modelID;
	int time;
	float conc;

	public ConcentrationData(int modelID, int time, float conc) {
		this.modelID = modelID;
		this.time = time;
		this.conc = conc;
	}
}

class ConditionsData {
	int modelID;
	int time;
	Float temp; // temperature
	Float pH;
	Float nacl; // salt concentration
	Float aw; // water activity
	Float waterConc; // water concentration
	Integer airHum; // air humidity
	String procName; // process name
	String comment;

	public ConditionsData(int modelID, int time, Float temp, Float pH, Float nacl, Float aw, Float waterConc,
			Integer airHum, String procName, String comment) {
		this.modelID = modelID;
		this.time = time;
		this.temp = temp;
		this.pH = pH;
		this.nacl = nacl;
		this.aw = aw;
		this.waterConc = waterConc;
		this.airHum = airHum;
		this.procName = procName;
		this.comment = comment;
	}
}

class DataPoint {
	int time;
	Float conc; // concentration
	Float temp; // temperature
	Float pH;
	Float nacl; // salt concentration
	Float aw; // water activity
	Float waterConc; // water concentration
	Integer airHum; // air humidity

	DataPoint(int time, Float conc, Float temp, Float pH, Float nacl, Float aw, Float waterConc, Integer airHum) {
		this.time = time;
		this.conc = conc;
		this.temp = temp;
		this.pH = pH;
		this.nacl = nacl;
		this.aw = aw;
		this.waterConc = waterConc;
		this.airHum = airHum;
	}
}
