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
package de.bund.bfr.knime.pmm.microbialdataedit;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.XmlConverter;

public class SettingsHelper {

	
	protected static final String CFGKEY_ADDEDCONDITIONS = "AddedConditions";
	protected static final String CFGKEY_ADDEDCONDITIONVALUES = "AddedConditionValues";
	protected static final String CFGKEY_ADDEDCONDITIONUNITS = "AddedConditionUnits";
	protected static final String CFGKEY_CONDITIONS = "Conditions";
	protected static final String CFGKEY_CONDITIONVALUES = "ConditionValues";
	protected static final String CFGKEY_CONDITIONUNITS = "ConditionUnits";
	protected static final String CFGKEY_AGENTS = "Agents";
	protected static final String CFGKEY_AGENTDETAILS = "AgentDetails";
	protected static final String CFGKEY_MATRICES = "Matrices";
	protected static final String CFGKEY_MATRIXDETAILS = "MatrixDetails";
	protected static final String CFGKEY_COMMENTS = "Comments";
	protected static final String CFGKEY_QUALITYSCORES = "QualityScores";
	protected static final String CFGKEY_CHECKS = "Checks";
	protected static final String CFGKEY_TIMESERIES = "TimeSeries";
	protected static final String CFGKEY_REFERENCES = "References";

	private Map<Integer, MiscXml> addedConditions;
	private Map<Integer, Map<String, Double>> addedConditionValues;
	private Map<Integer, Map<String, String>> addedConditionUnits;
	private Map<Integer, MiscXml> conditions;
	private Map<Integer, Map<String, Double>> conditionValues;
	private Map<Integer, Map<String, String>> conditionUnits;
	private Map<String, AgentXml> agents;
	private Map<String, String> agentDetails;
	private Map<String, MatrixXml> matrices;
	private Map<String, String> matrixDetails;
	private Map<String, String> comments;
	private Map<String, Integer> qualityScores;
	private Map<String, Boolean> checks;
	private Map<String, List<TimeSeriesXml>> timeSeries;
	private Map<String, List<LiteratureItem>> references;

	public SettingsHelper() {
		addedConditions = new LinkedHashMap<>();
		addedConditionValues = new LinkedHashMap<>();
		addedConditionUnits = new LinkedHashMap<>();
		conditions = new LinkedHashMap<>();
		conditionValues = new LinkedHashMap<>();
		conditionUnits = new LinkedHashMap<>();
		agents = new LinkedHashMap<>();
		agentDetails = new LinkedHashMap<>();
		matrices = new LinkedHashMap<>();
		matrixDetails = new LinkedHashMap<>();
		comments = new LinkedHashMap<>();
		qualityScores = new LinkedHashMap<>();
		checks = new LinkedHashMap<>();
		timeSeries = new LinkedHashMap<>();
		references = new LinkedHashMap<>();
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			addedConditions = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_ADDEDCONDITIONS),
					new LinkedHashMap<Integer, MiscXml>());
		} catch (InvalidSettingsException e) {
		}
		try {
			addedConditionValues = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_ADDEDCONDITIONVALUES),
					new LinkedHashMap<Integer, Map<String, Double>>());
		} catch (InvalidSettingsException e) {
		}
		try {
			addedConditionUnits = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_ADDEDCONDITIONUNITS),
					new LinkedHashMap<Integer, Map<String, String>>());
		} catch (InvalidSettingsException e2) {
		}
		try {
			conditions = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_CONDITIONS),
					new LinkedHashMap<Integer, MiscXml>());
		} catch (InvalidSettingsException e) {
		}
		try {
			conditionValues = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_CONDITIONVALUES),
					new LinkedHashMap<Integer, Map<String, Double>>());
		} catch (InvalidSettingsException e) {
		}

		try {
			conditionUnits = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_CONDITIONUNITS),
					new LinkedHashMap<Integer, Map<String, String>>());
		} catch (InvalidSettingsException e) {
		}

		try {
			agents = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_AGENTS),
					new LinkedHashMap<String, AgentXml>());
		} catch (InvalidSettingsException e) {
		}

		try {
			agentDetails = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_AGENTDETAILS),
					new LinkedHashMap<String, String>());
		} catch (InvalidSettingsException e1) {
		}

		try {
			matrices = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_MATRICES),
					new LinkedHashMap<String, MatrixXml>());
		} catch (InvalidSettingsException e) {
		}

		try {
			matrixDetails = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_MATRIXDETAILS),
					new LinkedHashMap<String, String>());
		} catch (InvalidSettingsException e1) {
		}

		try {
			comments = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_COMMENTS),
					new LinkedHashMap<String, String>());
		} catch (InvalidSettingsException e) {
		}

		try {
			qualityScores = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_QUALITYSCORES),
					new LinkedHashMap<String, Integer>());
		} catch (InvalidSettingsException e) {
		}

		try {
			checks = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_CHECKS),
					new LinkedHashMap<String, Boolean>());
		} catch (InvalidSettingsException e) {
		}

		try {
			timeSeries = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_TIMESERIES),
					new LinkedHashMap<String, List<TimeSeriesXml>>());
		} catch (InvalidSettingsException e) {
		}

		try {
			references = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_REFERENCES),
					new LinkedHashMap<String, List<LiteratureItem>>());
		} catch (InvalidSettingsException e) {
		}
	}

	public void saveSettings(NodeSettingsWO settings) {
		settings.addString(CFGKEY_ADDEDCONDITIONS,
				XmlConverter.objectToXml(addedConditions));
		settings.addString(CFGKEY_ADDEDCONDITIONVALUES,
				XmlConverter.objectToXml(addedConditionValues));
		settings.addString(CFGKEY_ADDEDCONDITIONUNITS,
				XmlConverter.objectToXml(addedConditionUnits));
		settings.addString(CFGKEY_CONDITIONS,
				XmlConverter.objectToXml(conditions));
		settings.addString(CFGKEY_CONDITIONVALUES,
				XmlConverter.objectToXml(conditionValues));
		settings.addString(CFGKEY_CONDITIONUNITS,
				XmlConverter.objectToXml(conditionUnits));
		settings.addString(CFGKEY_AGENTS, XmlConverter.objectToXml(agents));
		settings.addString(CFGKEY_AGENTDETAILS,
				XmlConverter.objectToXml(agentDetails));
		settings.addString(CFGKEY_MATRICES, XmlConverter.objectToXml(matrices));
		settings.addString(CFGKEY_MATRIXDETAILS,
				XmlConverter.objectToXml(matrixDetails));
		settings.addString(CFGKEY_COMMENTS, XmlConverter.objectToXml(comments));
		settings.addString(CFGKEY_QUALITYSCORES,
				XmlConverter.objectToXml(qualityScores));
		settings.addString(CFGKEY_CHECKS, XmlConverter.objectToXml(checks));
		settings.addString(CFGKEY_TIMESERIES,
				XmlConverter.objectToXml(timeSeries));
		settings.addString(CFGKEY_REFERENCES,
				XmlConverter.objectToXml(references));
	}

	public Map<Integer, MiscXml> getAddedConditions() {
		return addedConditions;
	}

	public void setAddedConditions(Map<Integer, MiscXml> addedConditions) {
		this.addedConditions = addedConditions;
	}

	public Map<Integer, Map<String, Double>> getAddedConditionValues() {
		return addedConditionValues;
	}

	public void setAddedConditionValues(
			Map<Integer, Map<String, Double>> addedConditionValues) {
		this.addedConditionValues = addedConditionValues;
	}

	public Map<Integer, Map<String, String>> getAddedConditionUnits() {
		return addedConditionUnits;
	}

	public void setAddedConditionUnits(
			Map<Integer, Map<String, String>> addedConditionUnits) {
		this.addedConditionUnits = addedConditionUnits;
	}

	public Map<Integer, MiscXml> getConditions() {
		return conditions;
	}

	public void setConditions(Map<Integer, MiscXml> conditions) {
		this.conditions = conditions;
	}

	public Map<Integer, Map<String, Double>> getConditionValues() {
		return conditionValues;
	}

	public void setConditionValues(
			Map<Integer, Map<String, Double>> conditionValues) {
		this.conditionValues = conditionValues;
	}

	public Map<Integer, Map<String, String>> getConditionUnits() {
		return conditionUnits;
	}

	public void setConditionUnits(
			Map<Integer, Map<String, String>> conditionUnits) {
		this.conditionUnits = conditionUnits;
	}

	public Map<String, AgentXml> getAgents() {
		return agents;
	}

	public void setAgents(Map<String, AgentXml> agents) {
		this.agents = agents;
	}

	public Map<String, String> getAgentDetails() {
		return agentDetails;
	}

	public void setAgentDetails(Map<String, String> agentDetails) {
		this.agentDetails = agentDetails;
	}

	public Map<String, MatrixXml> getMatrices() {
		return matrices;
	}

	public void setMatrices(Map<String, MatrixXml> matrices) {
		this.matrices = matrices;
	}

	public Map<String, String> getMatrixDetails() {
		return matrixDetails;
	}

	public void setMatrixDetails(Map<String, String> matrixDetails) {
		this.matrixDetails = matrixDetails;
	}

	public Map<String, String> getComments() {
		return comments;
	}

	public void setComments(Map<String, String> comments) {
		this.comments = comments;
	}

	public Map<String, Integer> getQualityScores() {
		return qualityScores;
	}

	public void setQualityScores(Map<String, Integer> qualityScores) {
		this.qualityScores = qualityScores;
	}

	public Map<String, Boolean> getChecks() {
		return checks;
	}

	public void setChecks(Map<String, Boolean> checks) {
		this.checks = checks;
	}

	public Map<String, List<TimeSeriesXml>> getTimeSeries() {
		return timeSeries;
	}

	public void setTimeSeries(Map<String, List<TimeSeriesXml>> timeSeries) {
		this.timeSeries = timeSeries;
	}

	public Map<String, List<LiteratureItem>> getReferences() {
		return references;
	}

	public void setReferences(Map<String, List<LiteratureItem>> references) {
		this.references = references;
	}
}
