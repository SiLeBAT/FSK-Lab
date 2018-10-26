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
package de.bund.bfr.knime.pmm.modelanddatajoiner;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class SettingsHelper {

	protected static final String CFGKEY_JOINTYPE = "JoinType";
	protected static final String CFGKEY_ASSIGNMENTS = "Assignments";

	protected static final String NO_JOIN = "";
	protected static final String PRIMARY_JOIN = "Primary Join";
	protected static final String SECONDARY_JOIN = "Secondary Join";
	protected static final String COMBINED_JOIN = "Combined Join";
	protected static final String DEFAULT_JOINTYPE = NO_JOIN;

	private String joinType;
	private String assignments;

	public SettingsHelper() {
		joinType = DEFAULT_JOINTYPE;
		assignments = null;
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			joinType = settings.getString(CFGKEY_JOINTYPE);
		} catch (InvalidSettingsException e) {
		}

		try {
			assignments = settings.getString(CFGKEY_ASSIGNMENTS);
		} catch (InvalidSettingsException e) {
		}
	}

	public void saveSettings(NodeSettingsWO settings) {
		settings.addString(CFGKEY_JOINTYPE, joinType);
		settings.addString(CFGKEY_ASSIGNMENTS, assignments);
	}

	public String getJoinType() {
		return joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public String getAssignments() {
		return assignments;
	}

	public void setAssignments(String assignments) {
		this.assignments = assignments;
	}
}
