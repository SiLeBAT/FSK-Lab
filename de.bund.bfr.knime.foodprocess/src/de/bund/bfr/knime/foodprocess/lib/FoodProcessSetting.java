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
package de.bund.bfr.knime.foodprocess.lib;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

public class FoodProcessSetting {
	private final String PARAM_PROCESSNAME = "processName";
	private final String PARAM_DURATION = "duration";
	private final String PARAM_NUMBERCOMPUTATIONS = "numberComputations";
	/*
	private final String PARAM_CAPACITY = "capacity";
	private final String PARAM_CAPACITY_UNIT_VOLUME = "capacityUnitVolume";
	private final String PARAM_CAPACITY_UNIT_TIME = "capacityUnitTime";
	*/
	private final String PARAM_DURATION_UNIT = "durationUnit";
	private final String PARAM_PARAMETERS = "parameters";
	private final String IN_PORT_SETTING = "in_port_setting";
	private final String PARAM_EXPERT_IN = "expertIn";
	private final String OUT_PORT_SETTING = "out_port_setting";
	private final String PARAM_EXPERT_OUT = "expertOut";
	private final String PARAM_AGENTS = "agents";
	
	private String processName;
	private Double duration;
	private Integer numberComputation;
/*
	private Double capacity;
	private String capacityUnitVolume;
	private String capacityUnitTime;
	*/
	private String durationUnit;
	
	private ParametersSetting parametersSetting;
	
	private InPortSetting[] inPortSetting;
	private boolean expertIn;
	
	private OutPortSetting[] outPortSetting;
	private boolean expertOut;
	
	private AgentsSetting agentsSetting;

	private int n_outports;
	private int n_inports;

	public boolean isExpertOut() {
		return expertOut;
	}
	public boolean isExpertIn() {
		return expertIn;
	}
	public void setExpertIn(boolean expertIn) {
		this.expertIn = expertIn;
	}
	public void setExpertOut(boolean expertOut) {
		this.expertOut = expertOut;
	}
	public void setParametersSetting(ParametersSetting parametersSetting) {
		this.parametersSetting = parametersSetting;
	}
	public void setAgentsSetting(AgentsSetting agentsSetting) {
		this.agentsSetting = agentsSetting;
	}
	public void setInPortSetting(InPortSetting[] inPortSetting) {
		this.inPortSetting = inPortSetting;
	}
	public void setOutPortSetting(OutPortSetting[] outPortSetting) {
		this.outPortSetting = outPortSetting;
	}
	public OutPortSetting[] getOutPortSetting() {
		return outPortSetting;
	}
	public String getDurationUnit() {
		return durationUnit;
	}
	public InPortSetting[] getInPortSetting() {
		return inPortSetting;
	}
	public ParametersSetting getParametersSetting() {
		return parametersSetting;
	}
	/*
	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}
	public void setCapacityUnitTime(String capacityUnitTime) {
		this.capacityUnitTime = capacityUnitTime;
	}
	public void setCapacityUnitVolume(String capacityUnitVolume) {
		this.capacityUnitVolume = capacityUnitVolume;
	}
	*/
	public Double getDuration() {
		return duration;
	}
	public void setDuration(Double duration) {
		this.duration = duration;
	}
	public String getProcessName() {
		return processName;
	}
	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public Integer getNumberComputation() {
		return numberComputation;
	}
	public void setNumberComputation(Integer numberComputation) {
		this.numberComputation = numberComputation;
	}
	public AgentsSetting getAgentsSetting() {
		return agentsSetting;
	}
	/*
	public Double getCapacity() {
		return capacity;
	}
	public String getCapacityUnitTime() {
		return capacityUnitTime;
	}
	public String getCapacityUnitVolume() {
		return capacityUnitVolume;
	}
	*/

	public FoodProcessSetting( final int n_inports, final int n_outports ) {		
		int i;
		
		this.n_inports = n_inports;
		this.n_outports = n_outports;

		parametersSetting = new ParametersSetting();
		
		inPortSetting = new InPortSetting[ n_inports ];
		for( i = 0; i < n_inports; i++ ) {
			inPortSetting[ i ] = new InPortSetting();
		}
		
		outPortSetting = new OutPortSetting[ n_outports ];
		for( i = 0; i < n_outports; i++ ) {
			outPortSetting[ i ] = new OutPortSetting( n_inports );
		}
		outPortSetting[ 0 ].setOutFlux(100.0);
		
		agentsSetting = new AgentsSetting();
	}
		
	/** Saves current parameters to settings object.
     * @param config to save to
     */
	public void saveSettings( final Config config ) {		
		int i;
		Config c;
		
		config.addString(PARAM_PROCESSNAME, processName);
		if (duration != null) {
			config.addDouble(PARAM_DURATION, duration);
		}
		if (numberComputation != null) {
			config.addInt(PARAM_NUMBERCOMPUTATIONS, numberComputation);
		}
		/*
		if (capacity != null) {
			config.addDouble(PARAM_CAPACITY, capacity);
		}
		config.addString(PARAM_CAPACITY_UNIT_VOLUME, capacityUnitVolume);
		config.addString(PARAM_CAPACITY_UNIT_TIME, capacityUnitTime);
		*/
		config.addString(PARAM_DURATION_UNIT, durationUnit);
		
		c = config.addConfig(PARAM_PARAMETERS);
		//assert c != null;		
		//assert parametersSetting != null;		
		parametersSetting.saveSettings( c );
		
		//assert inPortSetting != null;
		for( i = 0; i < inPortSetting.length; i++ ) {			
			c = config.addConfig( IN_PORT_SETTING + "_" + i );			
			//assert c != null;			
			//assert inPortSetting[ i ] != null;			
			inPortSetting[ i ].saveSettings( c );
		}
		config.addBoolean( PARAM_EXPERT_IN, expertIn );
		
		//assert outPortSetting != null;
		for( i = 0; i < outPortSetting.length; i++ ) {			
			c = config.addConfig( OUT_PORT_SETTING + "_" + i );			
			//assert c != null;			
			//assert outPortSetting[ i ] != null;			
			outPortSetting[ i ].saveSettings( c );
		}
		config.addBoolean( PARAM_EXPERT_OUT, expertOut );
		
		c = config.addConfig(PARAM_AGENTS);
		//assert c != null;		
		//assert agentsSetting != null;		
		agentsSetting.saveSettings( c );
	}

    /** Loads parameters in NodeModel.
     * @param config to load from
     * @throws InvalidSettingsException if settings are inconsistent
     */
	public void loadSettings( final Config config ) throws InvalidSettingsException {		
		int i;
		Config c;
		
		processName = config.getString(PARAM_PROCESSNAME);
		duration = config.containsKey(PARAM_DURATION) ? config.getDouble(PARAM_DURATION) : null;
		numberComputation = config.containsKey(PARAM_NUMBERCOMPUTATIONS) ? config.getInt(PARAM_NUMBERCOMPUTATIONS) : null;
		/*
		capacity = config.containsKey(PARAM_CAPACITY) ? config.getDouble(PARAM_CAPACITY) : null;
		capacityUnitVolume = config.getString(PARAM_CAPACITY_UNIT_VOLUME);
		capacityUnitTime = config.getString(PARAM_CAPACITY_UNIT_TIME);
		*/
		durationUnit = config.getString(PARAM_DURATION_UNIT);
		
		parametersSetting = new ParametersSetting();
		c = config.getConfig(PARAM_PARAMETERS);
		parametersSetting.loadSettings( c );

		inPortSetting = new InPortSetting[ n_inports ];
		for( i = 0; i < n_inports; i++ ) {			
			c = config.getConfig( IN_PORT_SETTING+"_"+i );
			inPortSetting[ i ] = new InPortSetting();
			inPortSetting[ i ].loadSettings( c );
		}
		expertIn = config.getBoolean( PARAM_EXPERT_IN );

		outPortSetting = new OutPortSetting[ n_outports ];
		for( i = 0; i < n_outports; i++ ) {		
			//if (config.containsKey(OUT_PORT_SETTING+"_"+i)) {
				c = config.getConfig(OUT_PORT_SETTING+"_"+i);
				outPortSetting[ i ] = new OutPortSetting( n_inports );
				outPortSetting[ i ].loadSettings( c );				
			//}
		}		
		if (config.containsKey(PARAM_EXPERT_OUT)) expertOut = config.getBoolean( PARAM_EXPERT_OUT );
		
		agentsSetting = new AgentsSetting();
		if (config.containsKey(PARAM_AGENTS)) {
			c = config.getConfig(PARAM_AGENTS);
			agentsSetting.loadSettings( c );
		}
	}

    /** Loads parameters in Dialog.
     * @param config to load from
     */
	public void loadSettingsForDialog(final Config config) {		
		try {
			loadSettings( config );
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
			//assert false;
		}
	}
}
