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
package de.bund.bfr.knime.foodprocess.addons;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.config.Config;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;

import de.bund.bfr.knime.foodprocess.FoodProcessNodeModel;
import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.knime.pcml.port.PCMLPortObjectSpec;
import de.bund.bfr.knime.pcml.port.PCMLUtil;
import de.bund.bfr.knime.util.FormulaEvaluator;
import de.bund.bfr.pcml10.AgentIncredientDocument.AgentIncredient;
import de.bund.bfr.pcml10.AgentRecipeDocument.AgentRecipe;
import de.bund.bfr.pcml10.NameAndDatabaseId;
import de.bund.bfr.pcml10.PCMLDocument;
import de.bund.bfr.pcml10.ProcessNodeType;
import de.bund.bfr.pcml10.ProcessParameters;
import de.bund.bfr.pcml10.MatrixIncredientDocument.MatrixIncredient;
import de.bund.bfr.pcml10.MatrixRecipeDocument.MatrixRecipe;
import de.bund.bfr.pcml10.OutportDocument.Outport;
import de.bund.bfr.pcml10.PCMLDocument.PCML;
import de.bund.bfr.pcml10.ProcessChainDocument.ProcessChain;
import de.bund.bfr.pcml10.ProcessNodeDocument.ProcessNode;


/**
 * This is the model implementation of Ingredients.
 * 
 *
 * @author BfR
 */
public class AddonNodeModel extends NodeModel {
    
	static final String PARAM_VOLUME = "volume";
	static final String PARAM_VOLUME_UNIT = "volumeUnit";
	static final String PARAM_MATRICES = "Matrices";
	static final String PARAM_AGENTS = "Agents";
	static final String PARAM_ID = "ID";
	static final String PARAM_NAME = "Name";

	private double[] volumeDef; // resp. concentration
	private String[] volumeUnitDef;
	private int[] iarr;
	private String[] narr;
	
	private boolean forMatrices;
	
	/**
     * Constructor for the node model.
     */
    protected AddonNodeModel(boolean forMatrices) {
        super(new PortType[] {
        }, new PortType[] {
        		PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class, false),
        });
        this.forMatrices = forMatrices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    	PCMLDocument pcmlDoc = createDoc();
    	
    	PCMLPortObject out = PCMLPortObject.create(pcmlDoc.toString());
		
		boolean valid = PCMLUtil.validate(out.getPcmlDoc(), NodeLogger.getLogger(this.getClass()));
		if (!valid) {
			setWarningMessage("PCML Document does not conform the standard. This may cause unexpected errors.");
		}

    	return new PortObject[]{out};
    }
    private PCMLDocument createDoc() {
		PCMLDocument pcmlDoc = PCMLUtil.create();
		
		PCML pcml = pcmlDoc.getPCML();
		ProcessChain pChain = pcml.addNewProcessChain();
		// create a new process node
		ProcessNode p = pChain.addNewProcessNode();
		p.setId("p" + UUID.randomUUID().toString());
		p.setType(forMatrices ? ProcessNodeType.ADMIXING : ProcessNodeType.CONTAMINATING);
		// name of the process node
		NameAndDatabaseId process = p.addNewProcess();
		process.setName(forMatrices ? "Admixer" : "Contaminator");
		// parameters of the process
		ProcessParameters param = p.addNewParameters();
		//param.setCapacity(5.0); // volumeDef volumeUnitDef
		param.setDuration(0);
		// the outport
		Outport out = p.addNewOutport();
		double totalVolume = 0;
		for (int i=0;i<volumeDef.length;i++) {
			if (!Double.isNaN(volumeDef[i])) totalVolume += volumeDef[i] * FormulaEvaluator.getGrams(volumeUnitDef[i]);
		}
		if (forMatrices) {
			out.setVolume(totalVolume+"");
			MatrixRecipe mRecipe = out.addNewMatrixRecipe();
			for (int i=0;i<iarr.length;i++) {
				MatrixIncredient mIncredient = mRecipe.addNewMatrixIncredient();
				NameAndDatabaseId matrix = mIncredient.addNewMatrix();
				matrix.setName(narr[i]);
				matrix.setDbId(iarr[i]);
				if (Double.isNaN(volumeDef[i])) mIncredient.setFraction(0);
				else mIncredient.setFraction(volumeDef[i] * FormulaEvaluator.getGrams(volumeUnitDef[i]) / totalVolume);
			}
		}
		else {
			out.setVolume(totalVolume+"");
			AgentRecipe aRecipe = out.addNewAgentRecipe();
			for (int i=0;i<iarr.length;i++) {
				AgentIncredient aIncredient = aRecipe.addNewAgentIncredient();
				NameAndDatabaseId agent = aIncredient.addNewAgent();
				agent.setName(narr[i]);
				agent.setDbId(iarr[i]);
				if (volumeUnitDef[i].equals("cfu")) {
					aIncredient.setFraction(Math.log10(volumeDef[i] / totalVolume));
					aIncredient.setUnit(FoodProcessNodeModel.defaultBacterialUnit);
					aIncredient.setObject(volumeUnitDef[i]);
				}
				else {
					aIncredient.setFraction(1000000 * volumeDef[i] * FormulaEvaluator.getGrams(volumeUnitDef[i]) / totalVolume);
					aIncredient.setUnit("ppm");
				}
			}
		}
       /*
		// the process chain data
		ProcessChainData pcData = pcml.addNewProcessChainData();
		ProcessData p1Data = pcData.addNewProcessData();
		p1Data.setRef("add" + MathUtilities.getRandomNegativeInt());

		p1Data.setTime(0.0); // absolute time at process start is always zero. Previous nodes have negative timestamps
		DataTable table = p1Data.addNewDataTable();
*/
		return pcmlDoc;		
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
    	PCMLPortObjectSpec outSpec = new PCMLPortObjectSpec();
    	return new PortObjectSpec[]{outSpec};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	if (volumeDef != null) {
	    	settings.addDoubleArray(PARAM_VOLUME, volumeDef);
	    	settings.addStringArray(PARAM_VOLUME_UNIT, volumeUnitDef);
	    	Config c = settings.addConfig(forMatrices ? PARAM_MATRICES : PARAM_AGENTS);
	    	c.addIntArray(PARAM_ID, iarr);
	    	c.addStringArray(PARAM_NAME, narr);
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	volumeDef = settings.containsKey(PARAM_VOLUME) ? settings.getDoubleArray(PARAM_VOLUME) : null;
    	volumeUnitDef = settings.containsKey(PARAM_VOLUME_UNIT) ? settings.getStringArray(PARAM_VOLUME_UNIT) : null;
    	Config c = settings.containsKey(forMatrices ? PARAM_MATRICES : PARAM_AGENTS) ? settings.getConfig(forMatrices ? PARAM_MATRICES : PARAM_AGENTS) : null;
		iarr = (c == null) ? null : c.getIntArray(PARAM_ID);
		narr = (c == null) ? null : c.getStringArray(PARAM_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
		try {
			loadValidatedSettingsFrom(settings);
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace();
			assert false;
		}		
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    	// no internals, nothing to reset
    }
    
	public void setSetting(LinkedList<Double> volumeDef, LinkedList<String> volumeUnitDef, 
			LinkedList<Integer> iarr, LinkedList<String> narr, boolean forMatrices) {		
		this.volumeDef = new double[volumeDef.size()];
		this.volumeUnitDef = new String[volumeDef.size()];
		this.iarr = new int[volumeDef.size()];
		this.narr = new String[volumeDef.size()];
		for (int i=0;i<volumeDef.size();i++) {
			this.volumeDef[i] = volumeDef.get(i);
			this.volumeUnitDef[i] = volumeUnitDef.get(i);
			this.iarr[i] = iarr.get(i);
			this.narr[i] = narr.get(i);
		}
		this.forMatrices = forMatrices;
	}
}

