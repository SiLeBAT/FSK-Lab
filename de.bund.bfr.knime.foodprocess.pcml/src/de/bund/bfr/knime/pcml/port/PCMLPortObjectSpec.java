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
package de.bund.bfr.knime.pcml.port;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContent;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.util.Agent;
import de.bund.bfr.knime.util.Matrix;
import de.bund.bfr.knime.util.ValueAndUnit;

/**
 * The spec of the process chain port type.
 * @author Heiko Hofer
 *
 */
public class PCMLPortObjectSpec implements PortObjectSpec {
	private static final String NEWMATRIXDEF = "newmatrixdef";
	private static final String NEWMATRIXDEF_ID = "newmatrixdef_id";
	private static final String MATRIX = "matrix";
	private static final String MATRIX_ID = "matrix id";
	private static final String MATRIX_FRACTION = "matrix fraction";
	private static final String NEWAGENTDEF = "newagentdef";
	private static final String NEWAGENTDEF_ID = "newagentdef_id";
	private static final String AGENT = "agent";
	private static final String AGENT_ID = "agent id";
	private static final String AGENT_QUANTITY = "agent quantity";
	private static final String AGENT_UNIT = "agent unit";
	private static final String AGENT_OBJECT = "agent object";
	private static final String VOLUME = "volume";
	private static final String TEMPERATURE = "temperature";
	private static final String PRESSURE = "pressure";
	private static final String PH_VALUE = "pH-value";
	private static final String AW_VALUE = "aw-value";

	/** the matrices. */
	private final Matrix newMatrixDef;
	private final Map<Matrix, Double> matrices;
	/** the agenses. */
	private final Agent newAgentDef;
	private final Map<Agent, ValueAndUnit> agents;
	/** the volume or null. */
	private final Double volume;
	/** the temperature or null. */
	private final Double temperature;
	/** the pressure or null. */
	private final Double pressure;
	/** the pH value or null. */
	private final Double pH_value;
	/** the water activity or null. */
	private final Double aw_value;
	
	public Matrix getNewMatrixDef() {
		return newMatrixDef;
	}
	public Map<Matrix, Double> getMatrices() {
		return matrices;
	}
	public Agent getNewAgentDef() {
		return newAgentDef;
	}
	public Map<Agent, ValueAndUnit> getAgents() {
		return agents;
	}
	public Double getVolume() {
		return volume;
	}
	public Double getTemperature() {
		return temperature;
	}
	public Double getPressure() {
		return pressure;
	}
	public Double getPH_value() {
		return pH_value;
	}
	public Double getAw_value() {
		return aw_value;
	}
	
	/** Static serializer as demanded from {@link PortObjectSpec} framework. */
    private static PortObjectSpecSerializer<PCMLPortObjectSpec> serializer;
    
    /**
     * Returns true when this port is used by this process node, i.e. matrices
     * are sent out of this port.
     * @return true when port is used
     */
    public boolean isUsed() {
    	return matrices.size() > 0 || agents.size() > 0;
    }
    
    /**
     * Creates a new port object spec for a port that is not used.
     * 
     */
    @SuppressWarnings("unchecked")
	public PCMLPortObjectSpec() {
		this(null, Collections.EMPTY_MAP, null, Collections.EMPTY_MAP, null, null, null, null, null);
	}
    
	/**
     * Creates a new port object spec with the given parameters. The matrix 
     * and agent arrays are mandatory but the other parameters are optional. 
     * They describe the physical properties of the matrix.
     * 
     * @param matrices the matrices
     * @param agents the agents
     * @param volume the volume or null
     * @param temperature the temperature of the matrix or null
     * @param pressure the pressure or null
     * @param pH the pH-value of the matrix or null
     * @param aw the the water activity or null
     */
    public PCMLPortObjectSpec(final Matrix newMatrixDef, final Map<Matrix, Double> matrices, 
    		final Agent newAgentDef, final Map<Agent, ValueAndUnit> agents,
    		final Double volume,
			final Double temperature, 
			final Double pressure, 
			final Double pH, 
			final Double aw) {
		if (null == matrices) {
			throw new NullPointerException("The matrix must be defined");
		}
		this.newMatrixDef = newMatrixDef;
		this.matrices = matrices;
		this.newAgentDef = newAgentDef;
		this.agents = agents;
		this.volume = volume;
		this.temperature = temperature;
		this.pressure = pressure;
		this.pH_value = pH;
		this.aw_value = aw;
	}



	/**
     * Static serializer as demanded from {@link PortObjectSpec} framework.
     * @return serializer for PCML (reads and writes PCML files)
     */
    public static PortObjectSpecSerializer<PCMLPortObjectSpec>
            getPortObjectSpecSerializer() {
        if (serializer == null) {
            serializer = new PCMLPortObjectSpecSerializer();
        }
        return serializer;
    }
    
	@Override
	public JComponent[] getViews() {
		//return new JComponent[0];
		
		if (isUsed()) {
			return new JComponent[] {new PCMLPortObjectSpecView(this)};
		} else {
			return new JComponent[0];
		}
		
	}

	/**
	 * Serialize to the given model content.
	 * @param cnt the model content
	 */
	public void save(final ModelContent config) {
		saveMatrices(config);
		saveAgents(config);
        if (null != volume) {
        	config.addDouble(VOLUME, volume);
        }
        if (null != temperature) {
            config.addDouble(TEMPERATURE, temperature);
        }
        if (null != pressure) {
        	config.addDouble(PRESSURE, pressure);
        }
        if (null != pH_value) {
        	config.addDouble(PH_VALUE, pH_value);
        }
        if (null != aw_value) {
        	config.addDouble(AW_VALUE, aw_value);
        }
	}


	private void saveMatrices(final ModelContent config) {
		String[] s = new String[matrices.size()]; 
        int[] id = new int[matrices.size()];
        double[] fraction = new double[matrices.size()];
        int i = 0;
        for (Matrix matrix : matrices.keySet()) {
        	s[i] = matrix.getName();
        	id[i] = matrix.getId();
        	fraction[i] = matrices.get(matrix);
        	i++;
        }
        config.addString(NEWMATRIXDEF, newMatrixDef != null ? newMatrixDef.getName() : "");
        config.addInt(NEWMATRIXDEF_ID, newMatrixDef != null ? newMatrixDef.getId() : -1);
        config.addStringArray(MATRIX, s);
        config.addIntArray(MATRIX_ID, id);
        config.addDoubleArray(MATRIX_FRACTION, fraction);
	}

	private void saveAgents(final ModelContent config) {
		String[] s = new String[agents.size()];
		int[] id = new int[agents.size()];
		double[] quantity = new double[agents.size()];
		String[] unit = new String[agents.size()];
		String[] object = new String[agents.size()];
        int i = 0;
        for (Agent agent : agents.keySet()) {
        	s[i] = agent.getName();
        	id[i] = agent.getId();
        	if (agents.get(agent).getValue() == null) quantity[i] = 0.0;
        	else quantity[i] = agents.get(agent).getValue();
        	unit[i] = agents.get(agent).getUnit();
        	object[i] = agents.get(agent).getObject();
        	i++;
        }		
        config.addString(NEWAGENTDEF, newAgentDef != null ? newAgentDef.getName() : "");
        config.addInt(NEWAGENTDEF_ID, newAgentDef != null ? newAgentDef.getId() : -1);
        config.addStringArray(AGENT, s);
        config.addIntArray(AGENT_ID, id);
        config.addDoubleArray(AGENT_QUANTITY, quantity);
        config.addStringArray(AGENT_UNIT, unit);
        config.addStringArray(AGENT_OBJECT, object);
	}

	/**
	 * Deserialize from the given model content 
	 * @param cnt the model content
	 * @return the PCMLPortObjectSpec read from the model content
	 */
	public static PCMLPortObjectSpec load(final ModelContentRO config) throws
		InvalidSettingsException {
		
		Map<Matrix, Double> matrices = loadMatrices(config);
		Matrix newMatrix = getNewMatrix(config);
		Map<Agent, ValueAndUnit> agents = loadAgents(config);
		Agent newAgent = getNewAgent(config);
		Double volumne = config.containsKey(VOLUME)
			? config.getDouble(VOLUME) : null;
		Double temperature = config.containsKey(TEMPERATURE)
			? config.getDouble(TEMPERATURE) : null;
		Double pressure = config.containsKey(PRESSURE)
			? config.getDouble(PRESSURE) : null;
		Double pH = config.containsKey(PH_VALUE)
			? config.getDouble(PH_VALUE) : null;
		Double aw = config.containsKey(AW_VALUE)
				? config.getDouble(AW_VALUE) : null;
		PCMLPortObjectSpec spec = new PCMLPortObjectSpec(newMatrix, matrices, 
				newAgent, agents, volumne, 
				temperature, pressure, pH, aw);
		return spec;
	}
	
	private static Matrix getNewMatrix(final ModelContentRO config) {
		return new Matrix(config.getString(NEWMATRIXDEF, ""), config.getInt(NEWMATRIXDEF_ID, -1));
	}
	private static Agent getNewAgent(final ModelContentRO config) {
		return new Agent(config.getString(NEWAGENTDEF, ""), config.getInt(NEWAGENTDEF_ID, -1));
	}

	private static Map<Matrix, Double> loadMatrices(final ModelContentRO config) {
		String[] s = config.getStringArray(MATRIX, new String[0]);
        int[] id = config.getIntArray(MATRIX_ID, new int[0]);
        double[] percentage = config.getDoubleArray(
        		MATRIX_FRACTION, new double[0]);
        Map<Matrix, Double> matrices = new LinkedHashMap<Matrix, Double>();
        for (int i = 0; i < s.length; i++) {
        	matrices.put(new Matrix(s[i], id[i]), percentage[i]);
        }
        return matrices;
	}	

	private static Map<Agent, ValueAndUnit> loadAgents(final ModelContentRO config) {
		String[] s = config.getStringArray(AGENT, new String[0]);
		int[] id = config.getIntArray(AGENT_ID, new int[0]);
		double[] quantity = config.getDoubleArray(AGENT_QUANTITY, new double[0]);
		String[] unit = config.getStringArray(AGENT_UNIT, new String[0]);
		String[] object = config.getStringArray(AGENT_OBJECT, new String[0]);
		Map<Agent, ValueAndUnit> agents = new LinkedHashMap<Agent, ValueAndUnit>();
        for (int i = 0; i < s.length; i++) {
        	agents.put(new Agent(s[i], id[i]), new ValueAndUnit(quantity[i], unit[i], object[i]));
        }
        return agents;  
	}

}
