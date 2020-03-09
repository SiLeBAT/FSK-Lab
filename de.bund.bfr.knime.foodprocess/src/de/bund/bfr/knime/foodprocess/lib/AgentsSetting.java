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

public class AgentsSetting {
	
	private final String PARAM_AGENTS = "agentsDef";
	private final String PARAM_AGENTS_1 = "agentsDef1";
	private final String PARAM_AGENTS_2 = "agentsDef2";
	private final String PARAM_AGENTS_3 = "agentsDef3";
	private final String PARAM_AGENTS_4 = "agentsDef4";
	private final String PARAM_RECIPE_GUESS = "recipeGuess";
	private final String PARAM_MANUAL_DEF = "manualDef";
	
	private boolean recipeGuess;
	private boolean manualDef;
	
	private String[] agentsDef;
	private double[] agentsDef1;
	private double[] agentsDef2;
	private double[] agentsDef3;
	private double[] agentsDef4;
	
	//private ParametersSetting parametersSetting;
	
	public void setManualDef(boolean manualDef) {
		this.manualDef = manualDef;
	}
	public void setRecipeGuess(boolean recipeGuess) {
		this.recipeGuess = recipeGuess;
	}
	public String[] getAgentsDef() {
		return agentsDef;
	}
	public double[] getAgentsDef1() {
		return agentsDef1;
	}
	public double[] getAgentsDef2() {
		return agentsDef2;
	}
	public double[] getAgentsDef3() {
		return agentsDef3;
	}
	public double[] getAgentsDef4() {
		return agentsDef4;
	}
	public boolean isManualDef() {
		return manualDef;
	}
	public boolean isRecipeGuess() {
		return recipeGuess;
	}
	public void setAgentsDef(String[] agentsDef) {
		this.agentsDef = agentsDef;
	}
	public void setAgentsDef1(double[] agentsDef1) {
		this.agentsDef1 = agentsDef1;
	}
	public void setAgentsDef2(double[] agentsDef2) {
		this.agentsDef2 = agentsDef2;
	}
	public void setAgentsDef3(double[] agentsDef3) {
		this.agentsDef3 = agentsDef3;
	}
	public void setAgentsDef4(double[] agentsDef4) {
		this.agentsDef4 = agentsDef4;
	}
	
	public AgentsSetting() {
		//parametersSetting = new ParametersSetting();
	}
	
	public void saveSettings( final Config config ) {		
		if (agentsDef != null) {
			config.addStringArray(PARAM_AGENTS, agentsDef);
			config.addDoubleArray(PARAM_AGENTS_1, agentsDef1);
			config.addDoubleArray(PARAM_AGENTS_2, agentsDef2);
			config.addDoubleArray(PARAM_AGENTS_3, agentsDef3);
			config.addDoubleArray(PARAM_AGENTS_4, agentsDef4);
		}

		config.addBoolean( PARAM_RECIPE_GUESS, recipeGuess );
		config.addBoolean( PARAM_MANUAL_DEF, manualDef );
	}
	
	public void loadSettings( final Config config ) throws InvalidSettingsException {		
		recipeGuess = config.getBoolean( PARAM_RECIPE_GUESS );
		manualDef = config.getBoolean( PARAM_MANUAL_DEF );
		
		agentsDef = config.containsKey(PARAM_AGENTS) ? config.getStringArray(PARAM_AGENTS) : null;
		agentsDef1 = config.containsKey(PARAM_AGENTS_1) ? config.getDoubleArray(PARAM_AGENTS_1) : null;
		agentsDef2 = config.containsKey(PARAM_AGENTS_2) ? config.getDoubleArray(PARAM_AGENTS_2) : null;
		agentsDef3 = config.containsKey(PARAM_AGENTS_3) ? config.getDoubleArray(PARAM_AGENTS_3) : null;
		agentsDef4 = config.containsKey(PARAM_AGENTS_4) ? config.getDoubleArray(PARAM_AGENTS_4) : null;
	}
	
	public void loadSettingsForDialog( final Config config ) {		
		try {
			loadSettings( config );
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
			assert false;
		}		
	}
}
