package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

/**
 * PmmLab catalog model. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>formula
 * <li>model class
 * <li>comment
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class CatalogModel implements ViewValue {

	public Integer id;
	public String name;
	public String formula;
	public Integer modelClass;
	public String comment;
	public String dbuuid;

	/**
	 * Saves catalog model properties into a {@link CatalogModel} with properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "formula"
	 * <li>Integer "modelClass"
	 * <li>String "comment"
	 * <li>String "dbuuid"
	 * </ul>
	 * 
	 * @param settings
	 *            settings where to save the {@link CatalogModel} properties
	 */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		SettingsHelper.addInt("id", id, settings);
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addString("formula", formula, settings);
		SettingsHelper.addInt("modelClass", modelClass, settings);
		SettingsHelper.addString("comment", comment, settings);
		SettingsHelper.addString("dbuuid", dbuuid, settings);
	}

	/**
	 * Loads catalog model properties from a {@link CatalogModel}.
	 * 
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>Integer "id"
	 *            <li>String "name"
	 *            <li>String "formula"
	 *            <li>Integer "modelClass"
	 *            <li>String "comment"
	 *            <li>String "dbuuid"
	 *            </ul>
	 */
	public void loadFromNodeSettings(final NodeSettingsRO settings) {
		id = SettingsHelper.getInteger("id", settings);
		name = SettingsHelper.getString("name", settings);
		formula = SettingsHelper.getString("formula", settings);
		modelClass = SettingsHelper.getInteger("modelClass", settings);
		comment = SettingsHelper.getString("comment", settings);
		dbuuid = SettingsHelper.getString("dbuuid", settings);
	}

	/**
	 * Creates a CatalogModel from a CatalogModelXml.
	 * 
	 * @param catalogModelXml
	 */
	public static CatalogModel toCatalogModel(CatalogModelXml catalogModelXml) {
		CatalogModel catalogModel = new CatalogModel();
		catalogModel.id = catalogModelXml.id;
		catalogModel.name = catalogModelXml.name;
		catalogModel.formula = catalogModelXml.formula;
		catalogModel.modelClass = catalogModelXml.modelClass;
		catalogModel.comment = catalogModelXml.comment;
		catalogModel.dbuuid = catalogModelXml.dbuuid;

		return catalogModel;
	}

	/**
	 * Returns an equivalent CatalogModelXml.
	 * 
	 * @return an equivalent CatalogModelXml
	 */
	public CatalogModelXml toCatalogModelXml() {
		CatalogModelXml catalogModelXml = new CatalogModelXml(id, name, formula, modelClass, dbuuid);
		catalogModelXml.comment = comment;

		return catalogModelXml;
	}
}
