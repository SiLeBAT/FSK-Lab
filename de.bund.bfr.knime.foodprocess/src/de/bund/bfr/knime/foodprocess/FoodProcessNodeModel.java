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
package de.bund.bfr.knime.foodprocess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.JPanel;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.hsh.bfr.db.DBKernel;
import org.jfree.data.xy.XYSeries;
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
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;
import de.bund.bfr.knime.foodprocess.lib.OutPortSetting;
import de.bund.bfr.knime.foodprocess.lib.ParametersSetting;
import de.bund.bfr.knime.foodprocess.ui.MyChartDialog;
import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.knime.pcml.port.PCMLPortObjectSpec;
import de.bund.bfr.knime.pcml.port.PCMLUtil;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.chart.ChartAllPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSamplePanel;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;
import de.bund.bfr.knime.pmm.estimatedmodelreader.EmReaderUi;
import de.bund.bfr.knime.pmm.predictorview.PredictorViewNodeDialog;
import de.bund.bfr.knime.pmm.predictorview.SettingsHelper;
import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.FormulaEvaluator;
import de.bund.bfr.knime.util.Matrix;
import de.bund.bfr.knime.util.ValueAndUnit;
import de.bund.bfr.pcml10.ColumnDocument.Column;
import de.bund.bfr.pcml10.ColumnListDocument.ColumnList;
import de.bund.bfr.pcml10.DataTableDocument.DataTable;
import de.bund.bfr.pcml10.InlineTableDocument.InlineTable;
import de.bund.bfr.pcml10.InportDocument.Inport;
import de.bund.bfr.pcml10.NameAndDatabaseId;
import de.bund.bfr.pcml10.OutportDocument.Outport;
import de.bund.bfr.pcml10.OutportRefDocument.OutportRef;
import de.bund.bfr.pcml10.PCMLDocument;
import de.bund.bfr.pcml10.PCMLDocument.PCML;
import de.bund.bfr.pcml10.ProcessChainDataDocument.ProcessChainData;
import de.bund.bfr.pcml10.ProcessDataDocument.ProcessData;
import de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode;
import de.bund.bfr.pcml10.ProcessNodeType;
import de.bund.bfr.pcml10.ProcessParameters;
import de.bund.bfr.pcml10.RowDocument.Row;

/**
 * This is the model implementation of FoodProcess.
 * 
 *
 * @author Jorgen Brandt
 */
public class FoodProcessNodeModel extends NodeModel {
	public static final int N_PORT_IN = 4;
	public static final int N_PORT_OUT = 4;
	public static String defaultBacterialUnit = "log10(count/g)";
	private static boolean doModelsOut = false;
	
	private FoodProcessNodeSettings settings;
	private HashMap<String, EmReaderUi> emReaderUiMap;
	private static String warnings = "";
	private static Category presCat = Categories.getCategory("Pressure");
	
	private static final PortType MANDATORY_PCML_TYPE = PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class, false);
	private static final PortType OPTIONAL_PCML_TYPE = PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class, true);

	/**
     * Constructor for the node model.
     */
    protected FoodProcessNodeModel() {    	
        super(new PortType[] { MANDATORY_PCML_TYPE, OPTIONAL_PCML_TYPE, OPTIONAL_PCML_TYPE, OPTIONAL_PCML_TYPE},
        		doModelsOut ?
        		new PortType[] { MANDATORY_PCML_TYPE, MANDATORY_PCML_TYPE, MANDATORY_PCML_TYPE, MANDATORY_PCML_TYPE, BufferedDataTable.TYPE} :
            	new PortType[] { MANDATORY_PCML_TYPE, MANDATORY_PCML_TYPE, MANDATORY_PCML_TYPE, MANDATORY_PCML_TYPE });
        settings = new FoodProcessNodeSettings();
        emReaderUiMap = new HashMap<String, EmReaderUi>();
    }


    /**
     * {@inheritDoc}
     */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		FoodProcessSetting fps = settings.getFoodProcessSetting();
        PortObjectSpec[] outSpecs = new PortObjectSpec[N_PORT_OUT+(doModelsOut?1:0)];
		PCMLPortObjectSpec instantMix = calculateInstantMixture(inSpecs, fps, this);
        for (int i = 0; i < N_PORT_OUT; i++) {
            outSpecs[i] = createPCMLSpec(i, instantMix, fps, null);
        }
		if (fps.getNumberComputation() == null || fps.getDuration() == null) {
			setWarningMessage((this.getWarningMessage() != null ? this.getWarningMessage() + "\n" : "") +
					"Amount of Computations and duration are essential parameters for meaningful computations.");			
		}
		if (doModelsOut) outSpecs[N_PORT_OUT] = SchemaFactory.createM1DataSchema().createSpec();
        return outSpecs;
    }    
    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inObjects, 
    		final ExecutionContext exec) {
        PCMLDocument pcmlDoc = PCMLUtil.merge(inObjects);

        // 1. Create Ports
        PortObjectSpec[] inSpecs = new PortObjectSpec[N_PORT_IN];
    	for (int i = 0; i < N_PORT_IN; i++) {
    		inSpecs[i] = inObjects[i] == null ? null : inObjects[i].getSpec();
    	}
		FoodProcessSetting fps = settings.getFoodProcessSetting();
		PCMLPortObjectSpec instantMix = calculateInstantMixture(inSpecs, fps, this);
    	

    	// 2. Create a processNode
    	PCML pcml = pcmlDoc.getPCML();
        ProcessNode processNode = pcml.getProcessChain().addNewProcessNode();
        String processNodeID = UUID.randomUUID().toString();
        processNode.setId(processNodeID);
        processNode.setType(ProcessNodeType.PROCESSING);

		
		// 3. Fill FoodprocessNode
		NameAndDatabaseId process = processNode.addNewProcess();
		process.setName(fps.getProcessName());
		// parameters of the process
		// firstly assume: same Unit!
		ProcessParameters param = processNode.addNewParameters();
		if (fps.getDuration() != null) {
			param.setDuration(fps.getDuration() * FormulaEvaluator.getSeconds(fps.getDurationUnit()));
		}
		if (fps.getNumberComputation() != null) param.setNumberComputations(fps.getNumberComputation());
        ParametersSetting ps = fps.getParametersSetting();
		if (ps.getTemperature() == null || ps.getTemperature().isEmpty()) param.setTemperature(instantMix.getTemperature()+"");
		else param.setTemperature(getTemperature(ps));
		if (ps.getAw() == null || ps.getAw().isEmpty()) param.setAw(instantMix.getAw_value()+"");
		else param.setAw(ps.getAw());
		if (ps.getPh() == null || ps.getPh().isEmpty()) param.setPH(instantMix.getPH_value()+"");
		else param.setPH(ps.getPh());
		if (ps.getPressure() == null || ps.getPressure().isEmpty()) param.setPressure(instantMix.getPressure()+"");
		else param.setPressure(getPressure(ps));
        
        // 4. Set inport Refs
        for (int i = 0; i < inObjects.length; i++) {
        	PortObject inObject = inObjects[i];
            if (null != inObject) {
                PCMLPortObject in = (PCMLPortObject)inObject;               
                Inport inport = processNode.addNewInport();
                OutportRef outRef = inport.addNewOutportRef();
                outRef.setRef(in.getParentId());
                outRef.setOutIndex(i);
            }
        }
        
        // 5. calculate the universal time for each ProcessNode relative to this node!
		Map<String, ProcessNode> processNodes = createProcessNodeMap(pcmlDoc);		
		Map<String, ProcessData> processChainData = createProcessChainDataMap(pcmlDoc);		
		calculateNewTimes(processNodes, processChainData, processNode, 0.0);
        
		// 6. set the process chain data
		ProcessChainData pcData = pcml.getProcessChainData();
		if (pcData == null) pcData = pcml.addNewProcessChainData();
		ProcessData p1Data = pcData.addNewProcessData();
		p1Data.setRef(processNodeID);

		p1Data.setTime(0.0); // absolute time at process start is always zero. Previous nodes have negative timestamps
		DataTable table = p1Data.addNewDataTable();
		ColumnList columnList = table.addNewColumnList();
		// Column Temperature
		Column temperature = columnList.addNewColumn();
		temperature.addNewColumnId().setName(AttributeUtilities.ATT_TEMPERATURE + " [°C]");
		QName tempCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c1");
		temperature.setName(tempCol.getLocalPart());
		// Column pH
		Column ph = columnList.addNewColumn();
		ph.addNewColumnId().setName(AttributeUtilities.ATT_PH);
		QName phCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c2");
		ph.setName(phCol.getLocalPart());
		// Column aw
		Column aw = columnList.addNewColumn();
		aw.addNewColumnId().setName(AttributeUtilities.ATT_AW);
		QName awCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c3");
		aw.setName(awCol.getLocalPart());
		// Column pressure
		Column pressure = columnList.addNewColumn();
		pressure.addNewColumnId().setName("pressure [bar]");
		QName pressureCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c4");
		pressure.setName(pressureCol.getLocalPart());
		// Column TotalVolume
		Column totalVolume = columnList.addNewColumn();
		totalVolume.addNewColumnId().setName("totalVolume [kg]");
		QName totalVolumeCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c5");
		totalVolume.setName(totalVolumeCol.getLocalPart());

		// Columns Matrices/Agents
		int index = 6;
		Map<QName, Double> matrixCols = new HashMap<QName, Double>();
		Map<QName, ValueAndUnit> agentCols = new HashMap<QName, ValueAndUnit>();
		for (Matrix m : instantMix.getMatrices().keySet()) {
			Column matrix = columnList.addNewColumn();
			NameAndDatabaseId nadbid = matrix.addNewMatrix();
			nadbid.setName(m.getName() + " [kg]");
			nadbid.setDbId(m.getId());
			QName matrixCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c" + index);
			matrix.setName(matrixCol.getLocalPart());
			matrixCols.put(matrixCol, instantMix.getMatrices().get(m) * instantMix.getVolume());
			index++;
		}
		Map<QName, Agent> qnameMap = new HashMap<QName, Agent>();
		for (Agent a : instantMix.getAgents().keySet()) {
			ValueAndUnit vau = instantMix.getAgents().get(a);
			Column agent = columnList.addNewColumn();
			NameAndDatabaseId nadbid = agent.addNewAgent();
			nadbid.setName(a.getName() + " [" + vau.getObjectUnit() + "]");
			nadbid.setDbId(a.getId());
			QName agentCol = new QName(PCMLUtil.getPCMLNamespace(pcmlDoc), "c" + index);
			agent.setName(agentCol.getLocalPart());
			agentCols.put(agentCol, vau);
			qnameMap.put(agentCol, a);
			index++;
		}

        InlineTable inlineTable = table.addNewInlineTable();
        
		Map<Agent, ValueAndUnit> lastAgentsQ = new LinkedHashMap<Agent, ValueAndUnit>();
        String lastTemp = "", lastPH = "", lastAW = "", lastPres = "";
        // 7. exec Table
		warnings = "";
		MyChartDialog mcd = new MyChartDialog();
		if (fps.getNumberComputation() != null && fps.getDuration() != null) {
			//double ds = FormulaEvaluator.getSeconds(fps.getDurationUnit());

			double stepWidth = fps.getDuration() / fps.getNumberComputation();
			XYSeries xy_T = mcd.getSerie(param.getTemperature(), 0.0, fps.getDuration(), stepWidth);
			XYSeries xy_ph = mcd.getSerie(param.getPH(), 0.0, fps.getDuration(), stepWidth);
			XYSeries xy_aw = mcd.getSerie(param.getAw(), 0.0, fps.getDuration(), stepWidth);
			XYSeries xy_pres = mcd.getSerie(param.getPressure(), 0.0, fps.getDuration(), stepWidth);

			HashMap<String, PredictionValues> pvMap = new HashMap<String, PredictionValues>(); 
	        for (Agent a : instantMix.getAgents().keySet()) {
	        	EmReaderUi emrui = emReaderUiMap.get(a.getName());
	        	if (emrui == null) emrui = emReaderUiMap.get(FoodProcessNodeDialog.defaultAgentname);
	        	SettingsHelper set;
	        	if (emrui == null) set = new SettingsHelper();
	        	else set = emrui.getSet();
	        	set.setDisplayHighlighted(false);
	        	PredictionValues pv = new PredictionValues(set);
				PredictorViewNodeDialog pvnd = null;
				ChartAllPanel chartPanel = null;
				if (set != null && set.getSelectedTuples() != null && set.getSelectedTuples().size() > 0 && set.getSelectedTuples().get(0) != null) {
					// PredictorViewNodeDialog
		    		pvnd = new PredictorViewNodeDialog(set.getSelectedTuples(), set, true, false);
					pv.setPvnd(pvnd);
					// ChartAllPanel
		    		JPanel mainComponent = pvnd.getMainComponent();
		    		chartPanel = (ChartAllPanel)mainComponent.getComponent(0);
		    		pv.setChartPanel(chartPanel);
		    		// unitTime
		    		pv.setUnitTime(set.getUnitX());
		    		String selModelID = null;
		    		List <KnimeTuple> kts = set.getSelectedTuples();
		    		if (kts.size() > 0) {
		    			KnimeTuple kt = kts.get(0);
		    			PmmXmlDoc doc = kt.getPmmXml(Model1Schema.ATT_MODELCATALOG);
		    			if (doc != null) {
		    				for (PmmXmlElementConvertable el : doc.getElementSet()) {
		    					if (el instanceof CatalogModelXml) {
		    						CatalogModelXml x = (CatalogModelXml) el;
		    						selModelID = ""+x.id;
		    						break;
		    					}
		    				}
		    			}
		    			// log10N0, lag
			    		if (selModelID != null) {
				    		if (set.getNewConcentrationParameters().containsKey(selModelID) && set.getNewConcentrationParameters().get(selModelID) != null) {
				    			pv.setLog10N0(set.getNewConcentrationParameters().get(selModelID));
				    		}
				    		if (set.getNewLagParameters().containsKey(selModelID) && set.getNewLagParameters().get(selModelID) != null) {
				    			pv.setLag(set.getNewLagParameters().get(selModelID));
				    		}
			    		}
			    		// unitLog10N0
		    			doc = kt.getPmmXml(Model1Schema.ATT_PARAMETER);
		    			if (doc != null) {
		    				for (PmmXmlElementConvertable el : doc.getElementSet()) {
		    					if (el instanceof ParamXml) {
		    						ParamXml x = (ParamXml) el;
		    						if (x.name.equals(pv.getLog10N0())) pv.setUnitLog10N0(x.unit);
		    						//else if (x.getName().equals(lag)) unitLag = x.getUnit();
		    					}
		    				}
		    			}
		    			// unitTemp, unitTime, unitPres
		    			doc = kt.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		    			if (doc != null) {
		    				for (PmmXmlElementConvertable el : doc.getElementSet()) {
		    					if (el instanceof IndepXml) {
		    						IndepXml x = (IndepXml) el;
		    						if (x.name.equals(AttributeUtilities.ATT_TEMPERATURE) && x.unit != null) pv.setUnitTemp(x.unit);
		    						else if (x.name.equals(AttributeUtilities.TIME) && x.unit != null) pv.setUnitTime(x.unit);
		    						else if (x.name.equals("Pressure") && x.unit != null) pv.setUnitPres(x.unit);
		    					}
		    				}
		    			}	    	
		    			// unitLog10N
		    			doc = kt.getPmmXml(Model1Schema.ATT_DEPENDENT);
		    			if (doc != null) {
		    				for (PmmXmlElementConvertable el : doc.getElementSet()) {
		    					if (el instanceof DepXml) {
		    						DepXml x = (DepXml) el;
		    						pv.setUnitLog10N(x.unit);
		    						break;
		    					}
		    				}
		    			}	    			
		    		}
				}
	        	pvMap.put(a.getName(), pv);
	        }
						
			Double lastCalcedConc = null;
			for (double t=0;t<=fps.getDuration();t+=stepWidth) { // start with t=ls better, because t=0 is occupied by the previous process node? Achtung: falls Änderung: (increment time by the time step of the process node) muss auch in PCMLDataTable.execute() geändert (in der Schleife nach oben verlegt) werden!!!
				Row row = inlineTable.addNewRow();
				XmlCursor cur = row.newCursor();
				cur.toFirstContentToken();
				Double tempVal = getYVal(xy_T, t);
				if (tempVal == null) {
					cur.insertElement(tempCol);
				} else {
					cur.insertElementWithText(tempCol, tempVal.toString());
					lastTemp = tempVal.toString();
				}
				Double phVal = getYVal(xy_ph, t);
				if (phVal == null) {
					cur.insertElement(phCol);
				} else {
					cur.insertElementWithText(phCol, phVal.toString());
					lastPH = phVal.toString();
				}
				Double awVal = getYVal(xy_aw, t);
				if (awVal == null) {
					cur.insertElement(awCol);
				} else {
					cur.insertElementWithText(awCol, awVal.toString());
					lastAW = awVal.toString();
				}
				Double presVal = getYVal(xy_pres, t);
				if (presVal == null) {
					cur.insertElement(pressureCol);
				} else {
					cur.insertElementWithText(pressureCol, presVal.toString());
					lastPres = presVal.toString();
				}
				
				cur.insertElementWithText(totalVolumeCol, (instantMix.getVolume() / 1000.0)+"");
				
		        for (Map.Entry<QName, Double> entry : matrixCols.entrySet()) {
					cur.insertElementWithText(entry.getKey(), "" + (entry.getValue() / 1000.0)); // inSpec.getVolume() * 
		        }
		        for (Map.Entry<QName, ValueAndUnit> entry : agentCols.entrySet()) {
		        	Agent a = qnameMap.get(entry.getKey());
		        	ValueAndUnit vau = entry.getValue();

		        	Double newVal = getNewConc(vau, pvMap.get(a.getName()), ps, tempVal, phVal, awVal, presVal, t, fps.getDurationUnit(), lastCalcedConc, stepWidth);
					//System.err.println(t + "\t" + newVal);

		        	lastCalcedConc = newVal;

		        	if (newVal != null) agentCols.put(entry.getKey(), new ValueAndUnit(newVal, vau.getUnit(), vau.getObject()));

					cur.insertElementWithText(entry.getKey(), "" + newVal);
					lastAgentsQ.put(qnameMap.get(entry.getKey()), new ValueAndUnit(newVal, vau.getUnit(), vau.getObject()));
					// absolute cfus:
					// Math.pow(10, c_Bacillus) * c_totalVol
		        }
				cur.dispose();
				double progress = t / fps.getDuration();
				exec.setProgress(progress);
				try {exec.checkCanceled();}
				catch (CanceledExecutionException e) {break;}
			}
		}
		else {
			// Invalid configuration
		}
		
    	PCMLPortObjectSpec[] outSpecs =  new PCMLPortObjectSpec[N_PORT_OUT];
    	for (int i = 0; i < N_PORT_OUT; i++) {
    		outSpecs[i] = createPCMLSpec(i, instantMix, fps, lastAgentsQ);
    	}

    	// Set outports
		OutPortSetting[] ops = fps.getOutPortSetting();
        PortObject[] out = new PortObject[N_PORT_OUT+(doModelsOut?1:0)];
        for (int i = 0; i < N_PORT_OUT; i++) {
            Outport outport = PCMLUtil.addOutport(processNode, outSpecs[i]);
            if (outSpecs[i].isUsed()) {
    			if (ops[i].getMatrix() != null) {
        			NameAndDatabaseId nm = outport.addNewMatrix();
        			nm.setName(ops[i].getMatrix().getName());
        			nm.setDbId(ops[i].getMatrix().getId());
    			}
    			
    			outport.setTemperature(lastTemp);
    			outport.setPH(lastPH);
    			outport.setAw(lastAW);
    			outport.setPressure(lastPres);
    			//outport.setFlowSpeed("1");
    			
                //outport.setVolume(outSpecs[i].getVolume().toString());
                /*
                Map<Matrix, Double> matrices = outSpecs[i].getMatrices();
                for (Map.Entry<Matrix, Double> entry : matrices.entrySet()) {
            		MatrixRecipe mRecipe = outport.addNewMatrixRecipe();
            		MatrixIncredient mIncredient = mRecipe.addNewMatrixIncredient();
            		NameAndDatabaseId matrix = mIncredient.addNewMatrix();
            		matrix.setName(entry.getKey().getName());
            		matrix.setDbId(entry.getKey().getId());  
            		mIncredient.setFraction(entry.getValue());
                }
                Map<Agent, Double> agents = outSpecs[i].getAgents();
                for (Map.Entry<Agent, Double> entry : agents.entrySet()) {
            		AgentRecipe mRecipe = outport.addNewAgentRecipe();
            		AgentIncredient mIncredient = mRecipe.addNewAgentIncredient();
            		NameAndDatabaseId agent = mIncredient.addNewAgent();
            		agent.setName(entry.getKey().getName());
            		agent.setDbId(entry.getKey().getId());  
            		mIncredient.setQuantity(entry.getValue());
                }
                */
            }
            else {
                outport.setVolume("0.0");
                outport.setFlowSpeed("0.0");
            }
            //out[i] = PCMLPortObject.create(pcmlDoc.toString());
            out[i] = new PCMLPortObject(pcmlDoc, processNodeID, i, outSpecs[i]);
        }
        if (doModelsOut) {
        	BufferedDataContainer buf = exec.createDataContainer(SchemaFactory.createM1DataSchema().createSpec());
            for (Agent a : instantMix.getAgents().keySet()) {
            	EmReaderUi emrui = emReaderUiMap.get(a.getName());
            	if (emrui == null) emrui = emReaderUiMap.get(FoodProcessNodeDialog.defaultAgentname);

            	SettingsHelper set = emrui.getSet();
            	List <KnimeTuple> kts = set.getSelectedTuples();// : set.getSelectedOldTuples();
            	
            	for (int i = 0; i < kts.size(); i++) {
            		buf.addRowToTable(new DefaultRow(String.valueOf(i), kts.get(i)));
            	}
            	
            	// close data buffer
            }
        	buf.close();
            out[N_PORT_OUT] = buf.getTable();
        }
/*        
        // Set outports     
        for (int i = 0; i < N_PORT_OUT; i++) {
            out[i] = new PCMLPortObject(pcmlDoc, processNodeID, i, outSpecs[i]);
        }
 */   	
        if (!warnings.isEmpty()) this.setWarningMessage(warnings);
        return out;
    }
    private Double getNewConc(ValueAndUnit vau, PredictionValues pv, ParametersSetting ps, Double tempVal, Double phVal, Double awVal, Double presVal, double t, String timeUnit, Double lastConc, double stepWidth) {
    	Double newVal = vau.getValue();
    	boolean isPercent = false;

    	PredictorViewNodeDialog pvnd = pv.getPvnd();
			if (pvnd != null) {
				Map<String, Double> gpv = pv.getSet().getParamXValues();
				Collection<String> col = pvnd.getReader().getTempParam().values();
				String tempAlt = col != null && col.size() > 0 ? col.toArray()[0].toString() : null;
				col = pvnd.getReader().getPhParam().values();
				String phAlt = col != null && col.size() > 0 ? col.toArray()[0].toString() : null;
				col = pvnd.getReader().getAwParam().values();
				String awAlt = col != null && col.size() > 0 ? col.toArray()[0].toString() : null;

				for (String key : gpv.keySet()) {
					if (key != null) {
						if (tempAlt != null && key.equals(tempAlt)) gpv.put(tempAlt, convert(Categories.getTempCategory(), "°C", tempVal, Categories.getTempCategory().getStandardUnit()));
						else if (phAlt != null && key.equals(phAlt)) gpv.put(phAlt, phVal);
						else if (awAlt != null && key.equals(awAlt)) gpv.put(awAlt, awVal);
						else if (key.equals("Pressure")) gpv.put("Pressure", convert(presCat, "bar", presVal, presCat.getStandardUnit()));
						else if (ps.getPresAlt() != null && key.equals(ps.getPresAlt())) gpv.put(ps.getPresAlt(), convert(presCat, "bar", presVal, presCat.getStandardUnit()));
						else if (key.equals(pv.getLag())) {
							gpv.put(pv.getLag(), 0.0);
						}
						/*
						else if (key.equals(pv.getLog10N0())) {
							if (lastConc == null) { // t == 0
								String unitN0 = (pv.getUnitLog10N0() == null ? pv.getUnitLog10N() : pv.getUnitLog10N0());
								isPercent = unitN0.equals("%") && !vau.getUnit().toLowerCase().startsWith("log10");
								if (isPercent) { // ok? StandardUnit?!?
									gpv.put(pv.getLog10N0(), 100.0);
									//if (vau.getCategory().getName().equals("Arbitrary Fraction")) 
								}
								else { // always StandardUnit for the configPanel.... Chris (Mail vom 3. April 2014 16.39): "...Das ConfigPanel verwaltet alle Größen in der jeweiligen DefaultUnit. Denn falls mehrere Modelle mit verschiedenen Einheiten ausgewählt sind, muss man sich ja für eine Einheit entscheiden, da benutze ich einfach immer die DefaultUnit. In dem Workflow konvertierst du Y0 nach "ln(...)", da es die Einheit des Modells ist, das ConfigPanel erwartet aber "log10(...)"...."
									if (pv.getUnitLog10N0() != null) {
										unitN0 = Categories.getCategoryByUnit(vau.getUnit()).getStandardUnit();
									}
									gpv.put(pv.getLog10N0(), convert(vau.getCategory(), vau.getUnit(), newVal, unitN0));
								}
							}									
						}
						*/
					}
				}
				if (tempVal != null && (tempAlt == null || !gpv.containsKey(tempAlt))) {
					if (warnings.indexOf("Temperature not defined in Model\n") < 0) warnings += "Temperature not defined in Model\n";
					//System.err.println(tempVal);
				}
				if (phVal != null && (phAlt == null || !gpv.containsKey(phAlt))) {
					if (warnings.indexOf("pH not defined in Model\n") < 0) warnings += "pH not defined in Model\n";
				}
				if (awVal != null && (awAlt == null || !gpv.containsKey(awAlt))) {
					if (warnings.indexOf("aw not defined in Model\n") < 0) warnings += "aw not defined in Model\n";
				}
				if (presVal != null && !gpv.containsKey("Pressure")) {
					if (warnings.indexOf("Pressure not defined in Model\n") < 0) warnings += "Pressure not defined in Model\n";
				}
				pv.getChartPanel().getConfigPanel().setParamXValues(gpv);

				Double newT = 0.0;
				if (lastConc != null) {
					Double lastConcConverted = convert(vau.getCategory(), vau.getUnit(), lastConc, pv.getSet().getUnitY());
					newT = getTime(pvnd, lastConcConverted);
					if (newT == null) {
						if (warnings.indexOf("Some concentrations are not calculatable\n") < 0) warnings += "Some concentrations are not calculatable\n";
						System.err.println("not calculatable: " + lastConc + "\t" + lastConcConverted + "\t" + t + "\t" + newT + "\t" + tempVal + "\t" + phVal + "\t" + awVal);
						//getTime(pvnd, lastConcConverted);
						//System.err.println(parser.evaluate(f) + "\t" + paramX + "\t" + paramY + "\t" + unitX + "\t" + unitY + "\t" + y + "\t" + minX + "\t" + inverseTransform(minX, transformX) + "\t" + convertFromUnit(paramX, inverseTransform(minX, transformX),unitX));
						return null;
					}
					else {
						newT = convert(Categories.getTimeCategory(), pv.getUnitTime(), newT,timeUnit );
						newT += stepWidth;
					}
				}
				Double newTime = convert(Categories.getTimeCategory(), timeUnit, newT, pv.getUnitTime()); //  Categories.getTimeCategory().getStandardUnit()
				Double theNewVal = getNextLogC(pvnd, newTime);
				/*
				System.err.println(gpv);
				System.err.println(pv.getSet().getUnitY());
				*/
				if (theNewVal != null) {
					// runden auf 10 Nachkommastellen, sonst kann es sein, dass ein Wert > Ymax herauskommt, der bei der nächsten Iteration nach unendlich geht
					theNewVal = Math.round(theNewVal * 10000000000.0) / 10000000000.0;
					if (isPercent) newVal = newVal * theNewVal / 100.0;
					else newVal = convert(vau.getCategory(), pv.getSet().getUnitY(), theNewVal, vau.getUnit()); // pv.getUnitLog10N()
					//System.err.println(gpv);
					//System.err.println(gpv + "\t" + newTime + "\t" + newVal);
				}
				else {
					if (warnings.indexOf("Agent conc calculation failed\n") < 0) warnings += "Agent conc calculation failed\n";
					System.err.println("Time = " + t + " " + timeUnit + "\tTEMPERATURE = " + tempVal + " °C\tpH = " + phVal + "\taw = " + awVal + "\tpressure = " + presVal + " bar");
				}
			}    	
			return newVal;
    }
    private Double getTime(PredictorViewNodeDialog pvnd, double logC) {
		List<Double> tv = new ArrayList<Double>();
		tv.add(logC);
		ChartSamplePanel samplePanel = pvnd.getSamplePanel();
		ChartConfigPanel c = pvnd.getChartAllPanel().getConfigPanel();
		samplePanel.setTimeValues(tv);
		samplePanel.setInverse(true);
		samplePanel.fireTimeValuesChanged();
		Double newTime = samplePanel.getTimeSeriesTable().getLogc(0);
		double minX = c.getMinX();
		double maxX = c.getMaxX();
		for (int i=0;i<10;i++) {
			if (newTime != null) break;
			newTime = samplePanel.getTimeSeriesTable().getLogc(0);
			c.setMinX(c.getMinX() - 50);
			c.setMaxX(c.getMaxX() + 50);
		}
		c.setMinX(minX);c.setMaxX(maxX);
		for (String w : pvnd.getWarnings()) {
			if (warnings.indexOf(w + "\n") < 0) warnings += w + "\n";
		}
		return newTime;
    }
    private Double getNextLogC(PredictorViewNodeDialog pvnd, double time) {
		List<Double> tv = new ArrayList<Double>();
		tv.add(time);
		ChartSamplePanel samplePanel = pvnd.getSamplePanel();
		samplePanel.setTimeValues(tv);
		samplePanel.setInverse(false);
		samplePanel.fireTimeValuesChanged();
		for (String w : pvnd.getWarnings()) {
			if (warnings.indexOf(w + "\n") < 0) warnings += w + "\n";
		}
		Double newLogC = samplePanel.getTimeSeriesTable().getLogc(0);
		return newLogC;
    }
    private static Double convert(Category cat, String fromUnit, Double value, String toUnit) {
    	Double newValue;
		try {
			newValue = cat.convert(value, fromUnit, toUnit);
		}
		catch (ConvertException e) {
			newValue = value;
			if (warnings.indexOf(fromUnit + " <-> " + toUnit) < 0) {
				warnings += "Problems converting '" + cat.getName() + "': " + fromUnit + " <-> " + toUnit + "\n";
			}
			//e.printStackTrace();
		}
		return newValue;
    }
	/** Create a list of all process nodes for fast access. This is map of the
	 * process nodes id to the object itself.
	 */
	private Map<String, ProcessNode> createProcessNodeMap(final PCMLDocument pcmlDoc) {
		Map<String, ProcessNode> processNodes = new HashMap<String, ProcessNode>();
		
		for (ProcessNode processNode : pcmlDoc.getPCML().getProcessChain().getProcessNodeArray()) {
			processNodes.put(processNode.getId(), processNode);
		}
		
		return processNodes;
	}
	private void calculateNewTimes(final Map<String, ProcessNode> processNodes, final Map<String, ProcessData> processChainData,
			final ProcessNode processNode, final Double time) {
		Inport[] in = processNode.getInportArray();
		for (int i = 0; i < in.length; i++) {
			OutportRef opr = in[i].getOutportRef();
			ProcessNode pn = processNodes.get(opr.getRef());
			ProcessData pd = processChainData.get(opr.getRef());
			if (pd == null) {
				if (pn.getType() != ProcessNodeType.ADMIXING && pn.getType() != ProcessNodeType.CONTAMINATING) {
					System.err.println("WW pd == null -> " + pn);
				}
			}
			else {
				Double newTime = time  - pn.getParameters().getDuration();
				pd.setTime(newTime);
				calculateNewTimes(processNodes, processChainData, pn, newTime);
			}
		}		
	}
	/** Create a list of all process nodes for fast access. This is map of the
	 * process nodes id to the object itself.
	 */
	private Map<String, ProcessData> createProcessChainDataMap(final PCMLDocument pcmlDoc) {
		Map<String, ProcessData> processChainData = new HashMap<String, ProcessData>();
		
		if (pcmlDoc.getPCML().getProcessChainData() != null) {
			for (ProcessData pcd : pcmlDoc.getPCML().getProcessChainData().getProcessDataArray()) {
				processChainData.put(pcd.getRef(), pcd);
			}			
		}
		
		return processChainData;
	}
    private Double getYVal(final XYSeries xy, final double xVal) {
    	Double result = null;
    	if (xy != null) {
        	for (int i=0;i<xy.getItemCount();i++) {
        		if (xy.getDataItem(i).getXValue() == xVal) {
        			result = xy.getDataItem(i).getYValue();
        				break;
        		}
        	}    		
    	}
    	return result;
    }

    static PCMLPortObjectSpec calculateInstantMixture(final PortObjectSpec[] inSpecs, final FoodProcessSetting fps, FoodProcessNodeModel fpnm) {
		HashMap<Matrix, Double> newMatrixMix = new HashMap<Matrix, Double>();
		HashMap<Agent, ValueAndUnit> newAgentMix = new HashMap<Agent, ValueAndUnit>();
		Double fpVolume = null, newTemperature = null, newpH = null, newAw = null, newPressure = null;//, fpFlowSpeed = null, flowSpeedSum = null;
		
		if (fpnm != null) {
			if (fps.getNumberComputation() == null) {
				fpnm.setWarningMessage((fpnm.getWarningMessage() != null ? fpnm.getWarningMessage() + "\n" : "") +
						"Number of Computations not set...");			
			}
			else if (fps.getDuration() == null) {
				fpnm.setWarningMessage((fpnm.getWarningMessage() != null ? fpnm.getWarningMessage() + "\n" : "") +
						"Duration not set...");			
			}
		}

		for (int i=0;i<N_PORT_IN;i++) {
			if (inSpecs[i] != null) {
				PCMLPortObjectSpec inSpec = (PCMLPortObjectSpec) inSpecs[i];
				if (inSpec.getMatrices().size() > 0) {
					Double tVol = inSpec.getVolume();
					// Matrices need to be included, Agents not for volume calculation						
					if (tVol != null) {
						if (fpVolume == null) fpVolume = 0.0;
						fpVolume += tVol;
					}						
				}
			}
		}

		// secondly: calculate the other parameters
        ParametersSetting ps = fps.getParametersSetting();
		for (int i=0;i<N_PORT_IN;i++) {
			if (inSpecs[i] != null) {

				PCMLPortObjectSpec inSpec = (PCMLPortObjectSpec) inSpecs[i];
				if (inSpec.getVolume() != null && fpVolume != null && fpVolume > 0) {
					double relation = inSpec.getVolume() / fpVolume;
					
					if (inSpec.getNewMatrixDef() != null && inSpec.getNewMatrixDef().getId() > 0) {
						double newFraction = relation;
						newMatrixMix.put(inSpec.getNewMatrixDef(), newFraction);
					}
					else {
						// calculate matrices, take care of recurrences
						Map<Matrix, Double> matrices = inSpec.getMatrices();
				        for (Map.Entry<Matrix, Double> entry : matrices.entrySet()) {
							double newFraction = entry.getValue() * relation;
							if (newMatrixMix.containsKey(entry.getKey())) {
								newFraction += newMatrixMix.get(entry.getKey());
								newMatrixMix.remove(entry.getKey());
							}
							newMatrixMix.put(entry.getKey(), newFraction);
				        }
					}

			        // calculate agents, take care of recurrences
					Map<Agent, ValueAndUnit> agents = inSpec.getAgents();
			        for (Map.Entry<Agent, ValueAndUnit> entry : agents.entrySet()) {
			        	Agent a = entry.getKey();
			        	ValueAndUnit vau = entry.getValue();
						double newFraction = calcNewAgentFraction(vau, vau.getValue(), relation);
						if (newAgentMix.containsKey(a)) {
							if (newAgentMix.get(a).getUnit().equals(vau.getUnit())) { // ppm oder log10 count/g - es kann nur einer überleben!!! D.h. innerhalb eines Workflows muss ein Agent dieselbe Einheitenkategorie haben!!!
								newFraction = calcNewAgentFraction(vau, newFraction, relation, newAgentMix.get(a).getValue());
								newAgentMix.remove(a);
							}
						}
						newAgentMix.put(a, new ValueAndUnit(newFraction, vau.getUnit(), vau.getObject()));
			        }    	

					// calculate temperature -> simply calculate the mean value, weighted by the volume
			        newTemperature = calculateWeightedMean(
							newTemperature,
							inSpec.getTemperature(),
							inSpec.getVolume(),
							fpVolume,
							getTemperature(ps));
					
					// calculate pH -> simply calculate the mean value, weighted by the volume
			        newpH = calculateWeightedMean(
							newpH,
							inSpec.getPH_value(),
							inSpec.getVolume(),
							fpVolume,
							ps.getPh());
					
					// calculate aw -> simply calculate the mean value, weighted by the volume
					newAw = calculateWeightedMean(
							newAw,
							inSpec.getAw_value(),
							inSpec.getVolume(),
							fpVolume,
							ps.getAw());
					
					// calculate pressure -> simply calculate the mean value, weighted by the volume
					newPressure = calculateWeightedMean(
							newPressure,
							inSpec.getPressure(),
							inSpec.getVolume(),
							fpVolume,
							getPressure(ps));
					
				}
			}
		}

        
		return new PCMLPortObjectSpec(null, newMatrixMix,
				null, newAgentMix,
				fpVolume,
				newTemperature,
				newPressure,
				newpH,
				newAw);
    }
    private static double calcNewAgentFraction(ValueAndUnit vau, double newFraction, double relation) {
    	double result;
		if (vau.getUnit().equals(defaultBacterialUnit)) { // log10 count/g
			result = Math.pow(10, vau.getValue()) * relation;
			result = Math.log10(result);
		}
		else {
			result = newFraction * relation;
		}    
		return result;
    }
    private static double calcNewAgentFraction(ValueAndUnit vau, double newFraction, double relation, Double stockFraction) {
    	double result;
		if (vau.getUnit().equals(defaultBacterialUnit)) { // log10 count/g
			result = Math.pow(10, vau.getValue()) * relation + Math.pow(10, stockFraction);
			result = Math.log10(result);
		}
		else {
			result = newFraction * relation + stockFraction;
		}    
		return result;
    }
    private static String getPressure(ParametersSetting ps) {
    	String result = ps.getPressure();
    	try {
	    	result = ps.getPressure() == null || ps.getPressure().isEmpty() ?
	    			null : 
	    				DBKernel.getDoubleStr(convert(presCat, ps.getPressureUnit(), Double.parseDouble(ps.getPressure()), "bar"));    
    	}
    	catch (Exception e) {}
    	return result;
    }
    private static String getTemperature(ParametersSetting ps) {
    	String result = ps.getTemperature();
    	try {
	    	result = ps.getTemperature() == null || ps.getTemperature().isEmpty() ?
	    			null : 
	    				DBKernel.getDoubleStr(convert(Categories.getTempCategory(), ps.getTemperatureUnit(), Double.parseDouble(ps.getTemperature()), "°C"));
    	}
    	catch (Exception e) {}
    	return result;
    }
    
    private PCMLPortObjectSpec createPCMLSpec(final int outIndex, PCMLPortObjectSpec instantMix, final FoodProcessSetting fps,
    		Map<Agent, ValueAndUnit> lastAgentsQ) {
		OutPortSetting[] ops = fps.getOutPortSetting();
		// erst einmal erhalten alle OutPorts denselben Mix - im Nachgang für jeden OutPORT INDIVIDUELL MACHEN, "Expert mode"
		
		PCMLPortObjectSpec outSpec;
		// take care of unused Outports (OutFlux = 0 or undefined)
		if (ops[outIndex].getOutFlux() == null || ops[outIndex].getOutFlux() == 0) {
			outSpec = new PCMLPortObjectSpec();
		}
		else {
			double outFluxSum = 0;
			for (int i=0;i<N_PORT_OUT;i++) {
				outFluxSum += ops[i].getOutFlux() == null ? 0 : ops[i].getOutFlux();
			}
			
			double outFluxRelation = ops[outIndex].getOutFlux() / outFluxSum;
			Map<Agent, ValueAndUnit> agents = instantMix.getAgents();
			Map<Agent, ValueAndUnit> relAgents = new LinkedHashMap<Agent, ValueAndUnit>();
			for (Agent a : agents.keySet()) {
				if (lastAgentsQ != null && lastAgentsQ.containsKey(a)) relAgents.put(a, lastAgentsQ.get(a)); // outFluxRelation
				else relAgents.put(a, agents.get(a)); // outFluxRelation
			}
 			outSpec = new PCMLPortObjectSpec(ops[outIndex].getMatrix(), instantMix.getMatrices(),
					null, relAgents,
					instantMix.getVolume() == null ? null : instantMix.getVolume() * outFluxRelation,
							instantMix.getTemperature(),
							instantMix.getPressure(),
							instantMix.getPH_value(),
							instantMix.getAw_value());
		}
		return outSpec;
    }
    private static Double calculateWeightedMean(Double sum, final Double newVal, final Double partVol, final Double totalVol,
    		final String defaultValue) {
    	// hier erstmal trivial ohne grosse Mischerei...
    	if (defaultValue != null && !defaultValue.isEmpty()) {
    		try{sum = Double.parseDouble(defaultValue);}catch(Exception e){}
    		if (sum == null) sum = Double.NaN; // wenn = null, dann wird im View-Dialog lein solcher Parameter angeboten..., das ist falsch für Intervalle, z.B. 1-50:21
    	}
    	else {
    		if (newVal != null) {
    			if (sum == null) {
    				sum = 0.0;
    			}
    			sum += newVal * partVol / totalVol;
    		}    	
    	}
		return sum;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    	// no internals, nothing to reset
    }

	public void setSetting( final FoodProcessNodeSettings settings ) {		
		this.settings = settings;
	}
	public FoodProcessNodeSettings getSetting() {		
		return settings;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         this.settings.saveSettings(settings);
         try {
         	settings.addStringArray("Agents", emReaderUiMap.keySet().toArray(new String[]{}));
         	for (String agentname : emReaderUiMap.keySet()) {
         		Config c = settings.addConfig("Agent_" + agentname);
             	c = c.addConfig("EstModelReaderUi");
             	EmReaderUi emReaderUi = this.emReaderUiMap.get(agentname);

		    	if (emReaderUi != null) {
		         	emReaderUi.saveSettingsTo(c);
		    	}
         	}
         }
         catch (Exception e) {}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        this.settings.loadSettings(settings);
        //loadDBGui(settings);
        loadEstModelGui(settings);
    }
    private void loadEstModelGui(final NodeSettingsRO settings) throws InvalidSettingsException {
    	try {
    		emReaderUiMap = new HashMap<String, EmReaderUi>();
    		if (settings.containsKey("EstModelReaderUi")) {
				Config c = settings.getConfig("EstModelReaderUi");
             	EmReaderUi emReaderUi = new EmReaderUi();                 	
             	emReaderUi.setSettings(c);
             	emReaderUiMap.put(FoodProcessNodeDialog.defaultAgentname, emReaderUi);
    		}
    		else if (settings.containsKey("Agents")) {
             	String[] sa = settings.getStringArray("Agents");
             	if (sa != null) {
                 	for (String agentname : sa) {
                     	Config c = settings.getConfig("Agent_" + agentname);
                     	c = c.getConfig("EstModelReaderUi");
                     	EmReaderUi emReaderUi = new EmReaderUi();                 	
                     	emReaderUi.setSettings(c);
                     	emReaderUiMap.put(agentname, emReaderUi);

                 	}         		
             	}
         	}
    	}
    	catch (Exception e) {}
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	
    	FoodProcessNodeSettings s = new FoodProcessNodeSettings();
        s.loadSettings(settings); // kann InvalidSettingsException werfen
        // TODO: Check settings if not already done by the settings object in the previous line.
        //loadDBGui(settings);
        //loadEstModelGui(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	// no internals, nothing to load
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    	// no internals, nothing to save
    }
}
