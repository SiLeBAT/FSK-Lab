package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

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

	// Configuration keys
	static final String ID = "id";
	static final String NAME = "name";
	static final String FORMULA = "formula";
	static final String MODEL_CLASS = "modelClass";
	static final String COMMENT = "comment";
	static final String DBUUID = "dbuuid";

	private Integer id;
	private String name;
	private String formula;
	private Integer modelClass;
	private String comment;
	private String dbuuid;

	/**
	 * Returns the id of this {@link CatalogModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the id of this {@link CatalogModel}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the name of this {@link CatalogModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link CatalogModel}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the formula of this {@link CatalogModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the formula of this {@link CatalogModel}
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * Returns the model class of this {@link CatalogModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the model class of this {@link CatalogModel}
	 */
	public Integer getModelClass() {
		return modelClass;
	}

	/**
	 * Returns the comment of this {@link CatalogModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the comment of this {@link CatalogModel}
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns the dbuuid of this {@link CatalogModel}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the dbuuid of this {@link CatalogModel}
	 */
	public String getDbuuid() {
		return dbuuid;
	}

	/**
	 * Sets the id of this {@link CatalogModel}.
	 * 
	 * @param id
	 *            the id to be set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Sets the name of this {@link CatalogModel}.
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
	 * Sets the formula value of this {@link CatalogModel}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param formula
	 *            the formula to be set
	 */
	public void setFormula(final String formula) {
		this.formula = Strings.emptyToNull(formula);
	}

	/**
	 * Sets the model class of this {@link CatalogModel}.
	 * 
	 * @param modelClass
	 *            the model class to be set
	 */
	public void setModelClass(final Integer modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * Sets the comment of this {@link CatalogModel}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param comment
	 *            the comment to be set
	 */
	public void setComment(final String comment) {
		this.comment = Strings.emptyToNull(comment);
	}

	/**
	 * Sets the dbuuid of this {@link CatalogModel}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param dbuuid
	 *            the dbuuid to be set
	 */
	public void setDbuuid(final String dbuuid) {
		this.dbuuid = Strings.emptyToNull(dbuuid);
	}

	/**
	 * Saves catalog model properties into a {@link CatalogModel}.
	 * 
	 * @param settings
	 *            settings where to save the {@link CatalogModel} properties
	 */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(FORMULA, formula, settings);
		SettingsHelper.addInt(MODEL_CLASS, modelClass, settings);
		SettingsHelper.addString(COMMENT, comment, settings);
		SettingsHelper.addString(DBUUID, dbuuid, settings);
	}

	/**
	 * Loads catalog model properties from a {@link CatalogModel}.
	 * 
	 * @param settings
	 *            the settings where to load the {@link CatalogModel} from
	 */
	public void loadFromNodeSettings(final NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		name = SettingsHelper.getString(NAME, settings);
		formula = SettingsHelper.getString(FORMULA, settings);
		modelClass = SettingsHelper.getInteger(MODEL_CLASS, settings);
		comment = SettingsHelper.getString(COMMENT, settings);
		dbuuid = SettingsHelper.getString(DBUUID, settings);
	}

	/**
	 * Creates a CatalogModel from a CatalogModelXml.
	 * 
	 * @param catalogModelXml
	 */
	public static CatalogModel toCatalogModel(CatalogModelXml catalogModelXml) {
		CatalogModel catalogModel = new CatalogModel();
			catalogModel.setId(catalogModelXml.id);
		catalogModel.setName(catalogModelXml.name);
		catalogModel.setFormula(catalogModelXml.formula);
		catalogModel.setModelClass(catalogModelXml.modelClass);
		catalogModel.setComment(catalogModelXml.comment);
		catalogModel.setDbuuid(catalogModelXml.dbuuid);

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
