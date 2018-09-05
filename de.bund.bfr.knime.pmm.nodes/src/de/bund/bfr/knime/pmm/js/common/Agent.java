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

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.AgentXml;

/**
 * PmmLab agent. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>detail
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Agent implements ViewValue {

	// Configuration keys
	static final String ID = "id";
	static final String NAME = "name";
	static final String DETAIL = "detail";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private String detail;
	private String dbuuid;

	/**
	 * Returns the id of this {@link Agent}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the id of this {@link Agent}.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the name of this {@link Agent}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link Agent}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the detail of this {@link Agent}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the detail of this {@link Agent}.
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Returns the dbuuid of this {@link Agent}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the dbuuid of this {@link Agent}
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/**
	 * Sets the id of this {@link Agent}.
	 * 
	 * @param id
	 *            the id to be set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Sets the name of this {@link Agent}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param name
	 *            the name to be set
	 */
	public void setName(final String name) {
		this.name = Strings.emptyToNull(name);
	}

	/**
	 * Sets the detail of this {@link Agent}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param detail
	 *            the detail to be set
	 */
	public void setDetail(final String detail) {
		this.detail = Strings.emptyToNull(detail);
	}

	/**
	 * Sets the dbuuid of this {@link Agent}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param dbuuid
	 *            the dbuuid to be set
	 */
	public void setDbuuid(final String dbuuid) {
		this.dbuuid = Strings.nullToEmpty(dbuuid);
	}

	/**
	 * Saves agent properties into a {@link NodeSettingsWO}.
	 * 
	 * @param settings
	 *            settings where to save the {@link Agent} properties
	 */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(DETAIL, detail, settings);
		SettingsHelper.addString(DBUUID, dbuuid, settings);
	}

	/**
	 * Loads agent properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            The settings where to load the {@link Agent} from
	 */
	public void loadFromNodeSettings(final NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		name = SettingsHelper.getString(NAME, settings);
		detail = SettingsHelper.getString(DETAIL, settings);
		dbuuid = SettingsHelper.getString(DBUUID, settings);
	}
	
	/**
	 * Creates an Agent from an {@link AgentXml}.
	 * 
	 * @param agentXml
	 */
	public static Agent toAgent(AgentXml agentXml) {
		Agent agent = new Agent();
		agent.setId(agentXml.getId());
		agent.setName(agentXml.getName());
		agent.setDetail(agentXml.getDetail());
		agent.setDbuuid(agentXml.getDbuuid());
		
		return agent;
	}
	
	/**
	 * Returns an equivalent AgentXml.
	 * 
	 * @return an equivalent AgentXml
	 */
	public AgentXml toAgentXml() {
		return new AgentXml(id, name, detail, dbuuid);
	}
}
