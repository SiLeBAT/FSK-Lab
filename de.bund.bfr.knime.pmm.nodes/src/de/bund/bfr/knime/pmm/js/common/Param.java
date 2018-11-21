package de.bund.bfr.knime.pmm.js.common;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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

	public String name;
	public String origName;
	public Boolean isStart;
	public Double value;
	public Double error;
	public Double min;
	public Double max;
	public Double p;
	public Double t;
	public Double minGuess;
	public Double maxGuess;
	public String category;
	public String unit;
	public String description;
	public String[] correlationNames;
	public double[] correlationValues;

	/**
	 * Saves param properties into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>String "name"
	 * <li>String "origname"
	 * <li>Boolean "isStart"
	 * <li>Double "value"
	 * <li>Double "error"
	 * <li>Double "min"
	 * <li>Double "max"
	 * <li>Double "P"
	 * <li>Double "t"
	 * <li>Double "minGuess"
	 * <li>Double "maxGuess"
	 * <li>String "category"
	 * <li>String "unit"
	 * <li>String "description"
	 * <li>StringArray "correlationNames"
	 * <li>StringArray "correlationValues"
	 * </ul>
	 * 
	 * @param settings
	 *            where to save the {@link Param} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addString("origname", origName, settings);
		SettingsHelper.addBoolean("isStart", isStart, settings);
		SettingsHelper.addDouble("value", value, settings);
		SettingsHelper.addDouble("error", error, settings);
		SettingsHelper.addDouble("min", min, settings);
		SettingsHelper.addDouble("max", max, settings);
		SettingsHelper.addDouble("P", p, settings);
		SettingsHelper.addDouble("t", t, settings);
		SettingsHelper.addDouble("minGuess", minGuess, settings);
		SettingsHelper.addDouble("maxGuess", maxGuess, settings);
		SettingsHelper.addString("category", category, settings);
		SettingsHelper.addString("unit", unit, settings);
		SettingsHelper.addString("description", description, settings);
		settings.addStringArray("correlationNames", correlationNames);
		settings.addDoubleArray("correlationValues", correlationValues);
	}

	/**
	 * Loads param properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>String "name"
	 *            <li>String "origname"
	 *            <li>Boolean "isStart"
	 *            <li>Double "value"
	 *            <li>Double "error"
	 *            <li>Double "min"
	 *            <li>Double "max"
	 *            <li>Double "P"
	 *            <li>Double "t"
	 *            <li>Double "minGuess"
	 *            <li>Double "maxGuess"
	 *            <li>String "category"
	 *            <li>String "unit"
	 *            <li>String "description"
	 *            <li>StringArray "correlationNames"
	 *            <li>StringArray "correlationValues"
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		name = SettingsHelper.getString("name", settings);
		origName = SettingsHelper.getString("origname", settings);
		isStart = SettingsHelper.getBoolean("isStart", settings);
		value = SettingsHelper.getDouble("value", settings);
		error = SettingsHelper.getDouble("error", settings);
		min = SettingsHelper.getDouble("min", settings);
		max = SettingsHelper.getDouble("max", settings);
		p = SettingsHelper.getDouble("P", settings);
		t = SettingsHelper.getDouble("t", settings);
		minGuess = SettingsHelper.getDouble("minGuess", settings);
		maxGuess = SettingsHelper.getDouble("maxGuess", settings);
		category = SettingsHelper.getString("category", settings);
		unit = SettingsHelper.getString("unit", settings);
		description = SettingsHelper.getString("description", settings);
		try {
			correlationNames = settings.getStringArray("correlationNames");
			correlationValues = settings.getDoubleArray("correlationValues");
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
		param.name = paramXml.name;
		param.origName = paramXml.origName;
		param.isStart = paramXml.isStartParam;
		param.value = paramXml.value;
		param.error = paramXml.error;
		param.min = paramXml.min;
		param.max = paramXml.max;
		param.p = paramXml.P;
		param.t = paramXml.t;
		param.minGuess = paramXml.minGuess;
		param.maxGuess = paramXml.maxGuess;
		param.category = paramXml.category;
		param.unit = paramXml.unit;
		param.description = paramXml.description;

		HashMap<String, Double> obtainedCorrelations = paramXml.correlations;
		String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
		double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
		int j = 0;
		for (Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
			obtainedCorrelationNames[j] = entry.getKey();
			if (entry.getValue() != null)
				obtainedCorrelationValues[j] = entry.getValue();
			j++;
		}

		param.correlationNames = obtainedCorrelationNames;
		param.correlationValues = obtainedCorrelationValues;

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

		return new ParamXml(name, origName, isStart, value, error, min, max, p, t, minGuess, maxGuess, category, unit,
				description, correlations);
	}
}
