package de.bund.bfr.knime.pmm.js.common;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.ParamXml;

/**
 * PmmLab parameter. Holds:
 * <ul>
 * <li>name
 * <li>original name
 * <li>is start value
 * <li>value
 * <li>error
 * <li>minimum value
 * <li>maximum value
 * <li>P
 * <li>T
 * <li>minimum guess
 * <li>maximum guess
 * <li>unit
 * <li>description
 * <li>correlation names
 * <li>correlation values
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Param implements ViewValue {

	// Configuration keys
	static final String NAME = "name";
	static final String ORIGNAME = "origname";
	static final String ISSTART = "isStart";
	static final String VALUE = "value";
	static final String ERROR = "error";
	static final String MIN = "min";
	static final String MAX = "max";
	static final String P = "P";
	static final String T = "t";
	static final String MINGUESS = "minGuess";
	static final String MAXGUESS = "maxGuess";
	static final String CATEGORY = "category";
	static final String UNIT = "unit";
	static final String DESCRIPTION = "description";
	static final String CORRELATION_NAMES = "correlationNames";
	static final String CORRELATION_VALUES = "correlationValues";

	private String name;
	private String origname;
	private Boolean isStart;
	private Double value;
	private Double error;
	private Double min;
	private Double max;
	private Double p;
	private Double t;
	private Double minGuess;
	private Double maxGuess;
	private String category;
	private String unit;
	private String description;
	private String[] correlationNames;
	private double[] correlationValues;

	/**
	 * Returns the name of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link Param}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the original name of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the original name of this {@link Param}
	 */
	public String getOrigName() {
		return origname;
	}

	/**
	 * Returns whether this {@link Param} is a start parameter.
	 * 
	 * If not set returns null.
	 * 
	 * @return whether this {@link Param} is a start parameter
	 */
	public Boolean isStart() {
		return isStart;
	}

	/**
	 * Returns the value of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the value of this {@link Param}
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * Returns the error of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the value of this {@link Param}.
	 */
	public Double getError() {
		return error;
	}

	/**
	 * Returns the minimumm value of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the minimum value of this {@link Param}
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * Returns the maximum value of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the maximum value of this {@link Param}
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * Returns the P of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the P of this {@link Param}
	 */
	public Double getP() {
		return p;
	}

	/**
	 * Returns the T of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the T of this {@link Param}
	 */
	public Double getT() {
		return t;
	}

	/**
	 * Returns the minimum guess of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the minimum guess of this {@link Param}
	 */
	public Double getMinGuess() {
		return minGuess;
	}

	/**
	 * Returns the maximum guess of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the maximum guess of this {@link Param}
	 */
	public Double getMaxGuess() {
		return maxGuess;
	}

	/**
	 * Returns the category of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the category of this {@link Param}
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the unit of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the unit of this {@link Param}
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the description of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the description of this {@link Param}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the correlation names of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the correlation names of this {@link Param}
	 */
	public String[] getCorrelationNames() {
		return correlationNames;
	}

	/**
	 * Returns the correlation values of this {@link Param}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the correlation values of this {@link Param}
	 */
	public double[] getCorrelationValues() {
		return correlationValues;
	}

	/**
	 * Sets the name of this {@link Param}.
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
	 * Sets the original name of this {@link Param}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param origName
	 *            the original name to be set
	 */
	public void setOrigName(final String origName) {
		this.origname = Strings.emptyToNull(origName);
	}

	/**
	 * Sets the state of this {@link Param} as start parameter.
	 * 
	 * @param isStart
	 *            state to be set
	 */
	public void setIsStart(final Boolean isStart) {
		this.isStart = isStart;
	}

	/**
	 * Sets the value of this {@link Param}.
	 * 
	 * @param value
	 *            the value to be set
	 */
	public void setValue(final Double value) {
		this.value = value;
	}

	/**
	 * Sets the error of this {@link Param}.
	 * 
	 * @param error
	 *            the error to be set
	 */
	public void setError(final Double error) {
		this.error = error;
	}

	/**
	 * Sets the minimum value of this {@link Param}.
	 * 
	 * @param min
	 *            the minimum value to be set
	 */
	public void setMin(final Double min) {
		this.min = min;
	}

	/**
	 * Sets the maximum value of this {@link Param}.
	 * 
	 * @param max
	 *            the maximum value to be set
	 */
	public void setMax(final Double max) {
		this.max = max;
	}

	/**
	 * Sets the P of this {@link Param}.
	 * 
	 * @param p
	 *            the P to be set
	 */
	public void setP(final Double p) {
		this.p = p;
	}

	/**
	 * Sets the T of this {@link Param}.
	 * 
	 * @param t
	 *            the T to be set
	 */
	public void setT(final Double t) {
		this.t = t;
	}

	/**
	 * Sets the minimum guess of this {@link Param}.
	 * 
	 * @param minGuess
	 *            the minimum guess to set
	 */
	public void setMinGuess(final Double minGuess) {
		this.minGuess = minGuess;
	}

	/**
	 * Sets the maximum guess of this {@link Param}.
	 * 
	 * @param maxGuess
	 *            the maximum guess to set
	 */
	public void setMaxGuess(final Double maxGuess) {
		this.maxGuess = maxGuess;
	}

	/**
	 * Sets the category of this {@link Param}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param category
	 *            the category to be set
	 */
	public void setCategory(final String category) {
		this.category = Strings.emptyToNull(category);
	}

	/**
	 * Sets the unit of this {@link Param}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param unit
	 *            the unit to be set
	 */
	public void setUnit(final String unit) {
		this.unit = Strings.emptyToNull(unit);
	}

	/**
	 * Sets the description of this {@link Param}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param description
	 *            the description of this {@link Param}
	 */
	public void setDescription(final String description) {
		this.description = Strings.emptyToNull(description);
	}

	/**
	 * Sets the correlation names of this {@link Param}.
	 * 
	 * @param correlationNames
	 *            correlation names to be set
	 */
	public void setCorrelationNames(final String[] correlationNames) {
		this.correlationNames = correlationNames;
	}

	/**
	 * Sets the correlation values of this {@link Param}.
	 * 
	 * @param correlationValues
	 *            correlation values to be set
	 */
	public void setCorrelationValues(final double[] correlationValues) {
		this.correlationValues = correlationValues;
	}

	/**
	 * Saves param properties into a {@link NodeSettingsWO}.
	 * 
	 * @param settings
	 *            where to save the {@link Param} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(ORIGNAME, origname, settings);
		SettingsHelper.addBoolean(ISSTART, isStart, settings);
		SettingsHelper.addDouble(VALUE, value, settings);
		SettingsHelper.addDouble(ERROR, error, settings);
		SettingsHelper.addDouble(MIN, min, settings);
		SettingsHelper.addDouble(MAX, max, settings);
		SettingsHelper.addDouble(P, p, settings);
		SettingsHelper.addDouble(T, t, settings);
		SettingsHelper.addDouble(MINGUESS, minGuess, settings);
		SettingsHelper.addDouble(MAXGUESS, maxGuess, settings);
		SettingsHelper.addString(CATEGORY, category, settings);
		SettingsHelper.addString(UNIT, unit, settings);
		SettingsHelper.addString(DESCRIPTION, description, settings);
		settings.addStringArray(CORRELATION_NAMES, correlationNames);
		settings.addDoubleArray(CORRELATION_VALUES, correlationValues);
	}

	/**
	 * Loads param properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            The settings where to load the {@link Param} from
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		name = SettingsHelper.getString(NAME, settings);
		origname = SettingsHelper.getString(ORIGNAME, settings);
		isStart = SettingsHelper.getBoolean(ISSTART, settings);
		value = SettingsHelper.getDouble(VALUE, settings);
		error = SettingsHelper.getDouble(ERROR, settings);
		min = SettingsHelper.getDouble(MIN, settings);
		max = SettingsHelper.getDouble(MAX, settings);
		p = SettingsHelper.getDouble(P, settings);
		t = SettingsHelper.getDouble(T, settings);
		minGuess = SettingsHelper.getDouble(MINGUESS, settings);
		maxGuess = SettingsHelper.getDouble(MAXGUESS, settings);
		category = SettingsHelper.getString(CATEGORY, settings);
		unit = SettingsHelper.getString(UNIT, settings);
		description = SettingsHelper.getString(DESCRIPTION, settings);
		try {
			correlationNames = settings.getStringArray(CORRELATION_NAMES);
			correlationValues = settings.getDoubleArray(CORRELATION_VALUES);
		} catch (InvalidSettingsException e) {
			correlationNames = null;
			correlationValues = null;
		}
	}
	
	/**
	 * Creates a Param from a ParamXml.
	 * 
	 * @param paramXml
	 */
	public static Param toParam(ParamXml paramXml) {
		Param param = new Param();
		param.setName(paramXml.name);
		param.setOrigName(paramXml.origName);
		param.setIsStart(paramXml.isStartParam);
		param.setValue(paramXml.value);
		param.setError(paramXml.error);
		param.setMin(paramXml.min);
		param.setMax(paramXml.max);
		param.setP(paramXml.P);
		param.setT(paramXml.t);
		param.setMinGuess(paramXml.minGuess);
		param.setMaxGuess(paramXml.maxGuess);
		param.setCategory(paramXml.category);
		param.setUnit(paramXml.unit);
		param.setDescription(paramXml.description);
		
		HashMap<String, Double> obtainedCorrelations = paramXml.correlations;
		String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
		double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
		int j = 0;
		for (Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
			obtainedCorrelationNames[j] = entry.getKey();
			if(entry.getValue() != null)
				obtainedCorrelationValues[j] = entry.getValue();
			j++;
		}
		
		param.setCorrelationNames(obtainedCorrelationNames);
		param.setCorrelationValues(obtainedCorrelationValues);
		
		return param;
	}
	
	/**
	 * Returns an equivalent ParamXml.
	 * 
	 * @return an equivalent ParamXml
	 */
	public ParamXml toParamXml() {
		HashMap<String, Double> correlations = new HashMap<>();
		if (correlationNames != null && correlationValues != null) {
			for (int i = 0; i < correlationNames.length; i++) {
				correlations.put(correlationNames[i], correlationValues[i]);
			}
		}
		
		return new ParamXml(name, origname, isStart, value, error, min, max, p, t, minGuess, maxGuess, category, unit, description, correlations);
	}
}
