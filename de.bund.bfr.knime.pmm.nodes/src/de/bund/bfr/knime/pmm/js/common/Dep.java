package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

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

	private String name;
	private String origname;
	private Double min;
	private Double max;
	private String category;
	private String unit;
	private String description;

	/**
	 * Returns the name of this {@link Dep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link Dep}.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the original name of this {@link Dep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the original name of this {@link Dep}
	 */
	public String getOrigname() {
		return origname;
	}

	/**
	 * Returns the minimum value of this {@link Dep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the minimum value of this {@link Dep}
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * Returns the maximum value of this {@link Dep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the maximum value of this {@link Dep}
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * Returns the category of this {@link Dep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the category of this {@link Dep}
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the unit of this {@link Dep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the unit of this {@link Dep}
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the description of this {@link Dep}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the description of this {@link Dep}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the name of this {@link Dep}.
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
	 * Sets the original name of this {@link Dep}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param origname
	 *            the original name to be set
	 */
	public void setOrigname(final String origname) {
		this.origname = Strings.emptyToNull(origname);
	}

	/**
	 * Sets the minimum value of this {@link Dep}.
	 * 
	 * @param min
	 *            the minimum value to be set
	 */
	public void setMin(final Double min) {
		this.min = min;
	}

	/**
	 * Sets the maximum value of this {@link Dep}.
	 * 
	 * @param max
	 *            the maximum value to be set
	 */
	public void setMax(final Double max) {
		this.max = max;
	}

	/**
	 * Sets the category of this {@link Dep}.
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
	 * Sets the unit of this {@link Dep}.
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
	 * Sets the description of this {@link Dep}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param description
	 *            the description to be set
	 */
	public void setDescription(final String description) {
		this.description = Strings.emptyToNull(description);
	}

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
		dep.setName(depXml.name);
		dep.setOrigname(depXml.origName);
		if (depXml.min != null)
			dep.setMin(depXml.min);
		if (depXml.max != null)
			dep.setMax(depXml.max);
		dep.setCategory(depXml.category);
		dep.setUnit(depXml.unit);
		dep.setDescription(depXml.description);

		return dep;
	}

	public DepXml toDepXml() {
		DepXml depXml = new DepXml(name, origname, category, unit, description);
		depXml.min = min;
		depXml.max = max;

		return depXml;
	}
}
