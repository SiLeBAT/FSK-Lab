package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.common.DepXml;

/**
 * PmmLab dep. Holds:
 * <ul>
 * <li>name
 * <li>origname
 * <li>min
 * <li>max
 * <li>category
 * <li>unit
 * <li>description
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Dep implements ViewValue {

	public String name;
	public String origname;
	public Double min;
	public Double max;
	public String category;
	public String unit;
	public String description;

	/**
	 * Saves dep properties into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>String "name"
	 * <li>String "origname"
	 * <li>Double "min"
	 * <li>Double "max"
	 * <li>String "category"
	 * <li>String "unit"
	 * <li>String "description"
	 * </ul>
	 * 
	 * @param settings
	 *            where to save the {@link Dep} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addString("origname", origname, settings);
		SettingsHelper.addDouble("min", min, settings);
		SettingsHelper.addDouble("max", max, settings);
		SettingsHelper.addString("category", category, settings);
		SettingsHelper.addString("unit", unit, settings);
		SettingsHelper.addString("description", description, settings);
	}

	/**
	 * Loads dep properties from a {@link NodeSettingsRO} with properties:
	 * <ul>
	 * <li>String "name"
	 * <li>String "origname"
	 * <li>Double "min"
	 * <li>Double "max"
	 * <li>String "category"
	 * <li>String "unit"
	 * <li>String "description"
	 * </ul>
	 * 
	 * @param settings
	 *            The settings where to load the {@link Dep} from
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		name = SettingsHelper.getString("name", settings);
		origname = SettingsHelper.getString("origname", settings);
		min = SettingsHelper.getDouble("min", settings);
		max = SettingsHelper.getDouble("max", settings);
		category = SettingsHelper.getString("category", settings);
		unit = SettingsHelper.getString("unit", settings);
		description = SettingsHelper.getString("description", settings);
	}

	/**
	 * Creates a Dep from a DepXml.
	 * 
	 * @param depXml
	 */
	public static Dep toDep(DepXml depXml) {
		Dep dep = new Dep();
		dep.name = depXml.name;
		dep.origname = depXml.origName;
		dep.min = depXml.min;
		dep.max = depXml.max;
		dep.category = depXml.category;
		dep.unit = depXml.unit;
		dep.description = depXml.description;

		return dep;
	}

	public DepXml toDepXml() {
		DepXml depXml = new DepXml(name, origname, category, unit, description);
		depXml.min = min;
		depXml.max = max;

		return depXml;
	}
}
