package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class TimeSeries implements ViewValue {

	public String name;
	public Double time;
	public String timeUnit;
	public String origTimeUnit;
	public Double concentration;
	public String concentrationUnit;
	public String concentrationUnitObjectType;
	public String origConcentrationUnit;
	public Double concentrationStdDev;
	public Integer numberOfMeasurements;

	/**
	 * Saves time series properties into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>String "name"
	 * <li>Double "time"
	 * <li>String "timeUnit"
	 * <li>String "origTimeUnit"
	 * <li>String "concentration"
	 * <li>String "concentrationUnit"
	 * <li>String "concentrationUnitObjectType"
	 * <li>String "origConcentrationUnit"
	 * <li>String "concentrationStdDev"
	 * <li>Integer "numberOfMeasurements"
	 * </ul>
	 * 
	 * @param settings
	 *            settings where to save the {@link TimeSeries} properties
	 */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addDouble("time", time, settings);
		SettingsHelper.addString("timeUnit", timeUnit, settings);
		SettingsHelper.addString("origTimeUnit", origTimeUnit, settings);
		SettingsHelper.addString("origConcentrationUnit", origConcentrationUnit, settings);
		SettingsHelper.addDouble("concentration", concentration, settings);
		SettingsHelper.addString("concentrationUnit", concentrationUnit, settings);
		SettingsHelper.addString("concentrationUnitObjectType", concentrationUnitObjectType, settings);
		SettingsHelper.addString("origConcentrationUnit", origConcentrationUnit, settings);
		SettingsHelper.addDouble("concentrationStdDev", concentrationStdDev, settings);
		SettingsHelper.addInt("numberOfMeasurements", numberOfMeasurements, settings);
	}

	/**
	 * Loads time series properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>String "name"
	 *            <li>Double "time"
	 *            <li>String "timeUnit"
	 *            <li>String "origTimeUnit"
	 *            <li>String "concentration"
	 *            <li>String "concentrationUnit"
	 *            <li>String "concentrationUnitObjectType"
	 *            <li>String "origConcentrationUnit"
	 *            <li>String "concentrationStdDev"
	 *            <li>Integer "numberOfMeasurements"
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		name = SettingsHelper.getString("name", settings);
		time = SettingsHelper.getDouble("time", settings);
		timeUnit = SettingsHelper.getString("timeUnit", settings);
		origTimeUnit = SettingsHelper.getString("origTimeUnit", settings);
		concentration = SettingsHelper.getDouble("concentration", settings);
		concentrationUnit = SettingsHelper.getString("concentrationUnit", settings);
		concentrationUnitObjectType = SettingsHelper.getString("concentrationUnitObjectType", settings);
		origConcentrationUnit = SettingsHelper.getString("origConcentrationUnit", settings);
		concentrationStdDev = SettingsHelper.getDouble("concentrationStdDev", settings);
		numberOfMeasurements = SettingsHelper.getInteger("numberOfMeasurements", settings);
	}

	/**
	 * Creates a TimeSeries from a TimeSeriesXml.
	 * 
	 * @param timeSeriesXml
	 */
	public static TimeSeries toTimeSeries(TimeSeriesXml timeSeriesXml) {
		TimeSeries timeSeries = new TimeSeries();
		timeSeries.name = timeSeriesXml.name;
		timeSeries.time = timeSeriesXml.time;
		timeSeries.timeUnit = timeSeriesXml.timeUnit;
		timeSeries.origTimeUnit = timeSeriesXml.origTimeUnit;
		timeSeries.concentration = timeSeriesXml.concentration;
		timeSeries.concentrationUnit = timeSeriesXml.concentrationUnit;
		timeSeries.concentrationUnitObjectType = timeSeriesXml.concentrationUnitObjectType;
		timeSeries.origConcentrationUnit = timeSeriesXml.origConcentrationUnit;
		timeSeries.concentrationStdDev = timeSeriesXml.concentrationStdDev;
		timeSeries.numberOfMeasurements = timeSeriesXml.numberOfMeasurements;

		return timeSeries;
	}

	/**
	 * Creates an equivalent TimeSeries.
	 * 
	 * @return an equivalent TimeSeries
	 */
	public TimeSeriesXml toTimeSeriesXml() {
		return new TimeSeriesXml(name, time, timeUnit, origTimeUnit, concentration, concentrationUnit,
				concentrationUnitObjectType, origConcentrationUnit, concentrationStdDev, numberOfMeasurements);
	}
}
