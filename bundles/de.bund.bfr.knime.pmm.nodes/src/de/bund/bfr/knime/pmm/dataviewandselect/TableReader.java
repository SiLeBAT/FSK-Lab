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
package de.bund.bfr.knime.pmm.dataviewandselect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.node.BufferedDataTable;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;

public class TableReader {

	private List<String> allIds;
	private List<KnimeTuple> allTuples;

	private List<String> ids;

	private Map<String, List<String>> stringColumns;
	private List<List<TimeSeriesXml>> data;
	private List<String> conditions;
	private List<List<Double>> conditionValues;
	private List<List<String>> conditionUnits;
	private List<String> standardVisibleColumns;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(BufferedDataTable table) {
		allIds = new ArrayList<>();
		allTuples = PmmUtilities.getTuples(table,
				SchemaFactory.createDataSchema());
		ids = new ArrayList<>();
		plotables = new LinkedHashMap<>();
		stringColumns = new LinkedHashMap<>();
		stringColumns.put(AttributeUtilities.DATAID, new ArrayList<String>());
		stringColumns.put(TimeSeriesSchema.ATT_AGENT, new ArrayList<String>());
		stringColumns.put(AttributeUtilities.AGENT_DETAILS,
				new ArrayList<String>());
		stringColumns.put(TimeSeriesSchema.ATT_MATRIX, new ArrayList<String>());
		stringColumns.put(AttributeUtilities.MATRIX_DETAILS,
				new ArrayList<String>());
		stringColumns.put(MdInfoXml.ATT_COMMENT, new ArrayList<String>());
		stringColumns.put(TimeSeriesSchema.ATT_LITMD, new ArrayList<String>());
		conditions = new ArrayList<>();
		conditionValues = new ArrayList<>();
		conditionUnits = new ArrayList<>();
		data = new ArrayList<>();
		shortLegend = new LinkedHashMap<>();
		longLegend = new LinkedHashMap<>();
		standardVisibleColumns = new ArrayList<>(stringColumns.keySet());
		standardVisibleColumns.add(ChartSelectionPanel.DATA);

		Set<String> idSet = new LinkedHashSet<>();
		List<String> miscParams = PmmUtilities.getMiscParams(allTuples);

		for (String param : miscParams) {
			conditions.add(param);
			conditionValues.add(new ArrayList<Double>());
			conditionUnits.add(new ArrayList<String>());
			standardVisibleColumns.add(param);
		}

		for (KnimeTuple tuple : allTuples) {
			String id = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);

			allIds.add(id);

			if (idSet.contains(id)) {
				continue;
			}

			idSet.add(id);
			ids.add(id);

			PmmXmlDoc timeSeriesXml = tuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			List<Double> timeList = new ArrayList<>();
			List<Double> logcList = new ArrayList<>();
			List<TimeSeriesXml> dataPoints = new ArrayList<>();
			String timeUnit = Categories.getTimeCategory().getStandardUnit();
			String concentrationUnit = Categories.getConcentrationCategories()
					.get(0).getStandardUnit();
			String dataName;

			for (PmmXmlElementConvertable el : timeSeriesXml.getElementSet()) {
				TimeSeriesXml element = (TimeSeriesXml) el;

				timeList.add(element.getTime());
				logcList.add(element.getConcentration());
				dataPoints.add(element);
				timeUnit = element.getTimeUnit();
				concentrationUnit = element.getConcentrationUnit();
			}

			if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
				dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			} else {
				dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
			}

			AgentXml agent = (AgentXml) tuple.getPmmXml(
					TimeSeriesSchema.ATT_AGENT).get(0);
			MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
					TimeSeriesSchema.ATT_MATRIX).get(0);
			String literature = "";

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					TimeSeriesSchema.ATT_LITMD).getElementSet()) {
				literature += "," + el;
			}

			if (!literature.isEmpty()) {
				literature = literature.substring(1);
			}

			stringColumns.get(AttributeUtilities.DATAID).add(dataName);
			stringColumns.get(TimeSeriesSchema.ATT_AGENT).add(agent.name);
			stringColumns.get(AttributeUtilities.AGENT_DETAILS).add(
					agent.detail);
			stringColumns.get(TimeSeriesSchema.ATT_MATRIX)
					.add(matrix.name);
			stringColumns.get(AttributeUtilities.MATRIX_DETAILS).add(
					matrix.detail);
			stringColumns.get(MdInfoXml.ATT_COMMENT).add(
					((MdInfoXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO)
							.get(0)).comment);
			stringColumns.get(TimeSeriesSchema.ATT_LITMD).add(literature);
			data.add(dataPoints);
			shortLegend.put(id, dataName);
			longLegend.put(id, dataName + " " + agent.name);

			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (int i = 0; i < miscParams.size(); i++) {
				boolean paramFound = false;

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (miscParams.get(i).equals(element.getName())) {
						conditionValues.get(i).add(element.getValue());
						conditionUnits.get(i).add(element.getUnit());
						paramFound = true;
						break;
					}
				}

				if (!paramFound) {
					conditionValues.get(i).add(null);
					conditionUnits.get(i).add(null);
				}
			}

			Plotable plotable = new Plotable(Plotable.DATASET);
			Map<String, List<String>> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();

			categories.put(AttributeUtilities.TIME,
					Arrays.asList(Categories.getTime()));
			categories.put(AttributeUtilities.CONCENTRATION,
					Categories.getConcentrations());
			units.put(AttributeUtilities.TIME, timeUnit);
			units.put(AttributeUtilities.CONCENTRATION, concentrationUnit);

			plotable.setCategories(categories);
			plotable.setUnits(units);

			if (!timeList.isEmpty() && !logcList.isEmpty()) {
				plotable.addValueList(AttributeUtilities.TIME, timeList);
				plotable.addValueList(AttributeUtilities.CONCENTRATION,
						logcList);
			}

			plotables.put(id, plotable);
		}
	}

	public List<String> getAllIds() {
		return allIds;
	}

	public List<KnimeTuple> getAllTuples() {
		return allTuples;
	}

	public List<String> getIds() {
		return ids;
	}

	public Map<String, List<String>> getStringColumns() {
		return stringColumns;
	}

	public List<List<TimeSeriesXml>> getData() {
		return data;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public List<List<Double>> getConditionValues() {
		return conditionValues;
	}

	public List<List<String>> getConditionUnits() {
		return conditionUnits;
	}

	public List<String> getStandardVisibleColumns() {
		return standardVisibleColumns;
	}

	public Map<String, Plotable> getPlotables() {
		return plotables;
	}

	public Map<String, String> getShortLegend() {
		return shortLegend;
	}

	public Map<String, String> getLongLegend() {
		return longLegend;
	}

}
