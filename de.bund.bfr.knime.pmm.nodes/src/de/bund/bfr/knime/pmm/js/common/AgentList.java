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
package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * List of PmmLab agents.
 * 
 * @see Agent
 * @author Miguel de Alba
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class AgentList {

	private int numAgents;
	private Agent[] agents;

	/**
	 * Returns an array with all the agents in the list.
	 * 
	 * If not set returns null.
	 * 
	 * @return an array with all the agents in the list
	 */
	public Agent[] getAgents() {
		return agents;
	}

	/**
	 * Sets the agents in the list.
	 * 
	 * @param agents
	 *            array of agents to be set
	 */
	public void setAgents(final Agent[] agents) {
		numAgents = agents.length;
		this.agents = agents;
	}

	/**
	 * Saves the list of agents into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>Integer "numAgents"
	 * <li>Variable number of string properties with prefix "agents" and suffix the
	 * number of the agent. E.g. "agents0", "agents1", ...
	 * </ul>
	 * 
	 * @param settings
	 *            settings where to save the {@link AgentList} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt("numAgents", numAgents);
		for (int i = 0; i < numAgents; i++) {
			agents[i].saveToNodeSettings(settings.addNodeSettings("agents" + i));
		}
	}

	/**
	 * Loads properties of the agent list from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>Integer "numAgents"
	 *            <li>Variable number of string properties with prefix "agents" and
	 *            suffix the number of the agent. E.g. "agents0", "agents1", ...
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		try {
			numAgents = settings.getInt("numAgents");
			agents = new Agent[numAgents];
			for (int i = 0; i < numAgents; i++) {
				agents[i] = new Agent();
				agents[i].loadFromNodeSettings(settings.getNodeSettings("agents" + i));
			}
		} catch (InvalidSettingsException e) {
		}
	}
}
