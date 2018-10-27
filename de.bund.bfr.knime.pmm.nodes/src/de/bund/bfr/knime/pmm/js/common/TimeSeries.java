package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class TimeSeries implements ViewValue {

	// Configuration keys
	static final String NAME = "name";
	static final String TIME = "time";
	static final String TIME_UNIT = "timeUnit";
	static final String ORIG_TIME_UNIT = "origTimeUnit";
	static final String CONCENTRATION = "concentration";
	static final String CONCENTRATION_UNIT = "concentrationUnit";
	static final String CONCENTRATION_UNIT_OBJECT_TYPE = "concentrationUnitObjectType";
	static final String ORIG_CONCENTRATION_UNIT = "origConcentrationUnit";
	static final String CONCENTRATION_STDDEV = "concentrationStdDev";
	static final String NUMBER_OF_MEASUREMENTS = "numberOfMeasurements";

	private String name;
	private Double time;
	private String timeUnit;
	private String origTimeUnit;
	private Double concentration;
	private String concentrationUnit;
	private String concentrationUnitObjectType;
	private String origConcentrationUnit;
	private Double concentrationStdDev;
	private Integer numberOfMeasurements;

	/**
	 * Returns the name of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link TimeSeries}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the time of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the time of this {@link TimeSeries}
	 */
	public Double getTime() {
		return time;
	}

	/**
	 * Returns the time unit of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the time unit of this {@link TimeSeries}
	 */
	public String getTimeUnit() {
		return timeUnit;
	}

	/**
	 * Returns the original time unit of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the original time unit of this {@link TimeSeries}
	 */
	public String getOrigTimeUnit() {
		return origTimeUnit;
	}

	/**
	 * Returns the concentration of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the concentration of this {@link TimeSeries}
	 */
	public Double getConcentration() {
		return concentration;
	}

	/**
	 * Returns the concentration unit of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the concentration unit of this {@link TimeSeries}
	 */
	public String getConcentrationUnit() {
		return concentrationUnit;
	}

	/**
	 * Returns the concentration unit object type of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the concentration unit object type of this {@link TimeSeries}
	 */
	public String getConcentrationUnitObjectType() {
		return concentrationUnitObjectType;
	}

	/**
	 * Returns the original concentration unit of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the original concentration unit of this {@link TimeSeries}
	 */
	public String getOrigConcentrationUnit() {
		return origConcentrationUnit;
	}

	/**
	 * Returns the concentration standard deviation of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the concentration standard deviation of this {@link TimeSeries}
	 */
	public Double getConcentrationStdDev() {
		return concentrationStdDev;
	}

	/**
	 * Returns the number of measurements of this {@link TimeSeries}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the number of measurements of this {@link TimeSeries}
	 */
	public Integer getNumberOfMeasurements() {
		return numberOfMeasurements;
	}

	/**
	 * Sets the name of this {@link TimeSeries}.
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
	 * Sets the time of this {@link TimeSeries}.
	 * 
	 * @param time
	 *            the time to be set
	 */
	public void setTime(final Double time) {
		this.time = time;
	}

	/**
	 * Sets the time unit of this {@link TimeSeries}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param timeUnit
	 *            the time unit to be set
	 */
	public void setTimeUnit(final String timeUnit) {
		this.timeUnit = Strings.emptyToNull(timeUnit);
	}

	/**
	 * Sets the original time unit of this {@link TimeSeries}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param origTimeUnit
	 *            the original time unit to be set
	 */
	public void setOrigTimeUnit(final String origTimeUnit) {
		this.origTimeUnit = Strings.emptyToNull(origTimeUnit);
	}

	/**
	 * Sets the concentration of this {@link TimeSeries}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param concentration
	 *            the concentration to be set
	 */
	public void setConcentration(final Double concentration) {
		this.concentration = concentration;
	}

	/**
	 * Sets the concentration unit of this {@link TimeSeries}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param concentrationUnit
	 *            the concentration unit to be set
	 */
	public void setConcentrationUnit(final String concentrationUnit) {
		this.concentrationUnit = Strings.emptyToNull(concentrationUnit);
	}

	/**
	 * Sets the concentration unit object type of this {@link TimeSeries}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param concentrationUnitObjectType
	 *            the concentration unit object type to be set
	 */
	public void setConcentrationUnitObjectType(final String concentrationUnitObjectType) {
		this.concentrationUnitObjectType = Strings.emptyToNull(concentrationUnitObjectType);
	}

	/**
	 * Sets the original concentration unit of this {@link TimeSeries}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param origConcentrationUnit
	 *            the original concentration unit
	 */
	public void setOrigConcentrationUnit(final String origConcentrationUnit) {
		this.origConcentrationUnit = Strings.emptyToNull(origConcentrationUnit);
	}

	/**
	 * Sets the concentration standard deviation of this {@link TimeSeries}.
	 * 
	 * @param concentrationStdDev
	 *            the concentration standard deviation to be set
	 */
	public void setConcentrationStdDev(final Double concentrationStdDev) {
		this.concentrationStdDev = concentrationStdDev;
	}

	/**
	 * Sets the number of measurements of this {@link TimeSeries}.
	 * 
	 * @param numberOfMeasurements
	 *            the number of measurements to be set
	 */
	public void setNumberOfMeasurements(final Integer numberOfMeasurements) {
		this.numberOfMeasurements = numberOfMeasurements;
	}

	/**
	 * Saves time series properties into a {@link NodeSettingsWO}.
	 * 
	 * @param settings
	 *            settings where to save the {@link TimeSeries} properties
	 */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addDouble(TIME, time, settings);
		SettingsHelper.addString(TIME_UNIT, timeUnit, settings);
		SettingsHelper.addString(ORIG_TIME_UNIT, origTimeUnit, settings);
		SettingsHelper.addString(ORIG_CONCENTRATION_UNIT, origTimeUnit, settings);
		SettingsHelper.addDouble(CONCENTRATION, concentration, settings);
		SettingsHelper.addString(CONCENTRATION_UNIT, concentrationUnit, settings);
		SettingsHelper.addString(CONCENTRATION_UNIT_OBJECT_TYPE, concentrationUnitObjectType, settings);
		SettingsHelper.addString(ORIG_CONCENTRATION_UNIT, origConcentrationUnit, settings);
		SettingsHelper.addDouble(CONCENTRATION_STDDEV, concentrationStdDev, settings);
		SettingsHelper.addInt(NUMBER_OF_MEASUREMENTS, numberOfMeasurements, settings);
	}

	/**
	 * Loads time series properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            The settings where to load the {@link TimeSeries} from
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		name = SettingsHelper.getString(NAME, settings);
		time = SettingsHelper.getDouble(TIME, settings);
		timeUnit = SettingsHelper.getString(TIME_UNIT, settings);
		origTimeUnit = SettingsHelper.getString(ORIG_TIME_UNIT, settings);
		concentration = SettingsHelper.getDouble(CONCENTRATION, settings);
		concentrationUnit = SettingsHelper.getString(CONCENTRATION_UNIT, settings);
		concentrationUnitObjectType = SettingsHelper.getString(CONCENTRATION_UNIT_OBJECT_TYPE, settings);
		origConcentrationUnit = SettingsHelper.getString(ORIG_CONCENTRATION_UNIT, settings);
		concentrationStdDev = SettingsHelper.getDouble(CONCENTRATION_STDDEV, settings);
		numberOfMeasurements = SettingsHelper.getInteger(NUMBER_OF_MEASUREMENTS, settings);
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
