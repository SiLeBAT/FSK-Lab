package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.MdInfoXml;

/**
 * PmmLab model info. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>comment
 * <li>quality score
 * <li>checked
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class MdInfo implements ViewValue {

	// Configuration keys
	static final String ID = "ID";
	static final String NAME = "Name";
	static final String COMMENT = "Comment";
	static final String QUALITYSCORE = "QualityScore";
	static final String CHECKED = "Checked";

	private Integer id;
	private String name;
	private String comment;
	private Integer qualityScore;
	private Boolean checked;

	/**
	 * Returns the id of this {@link MdInfo}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the id of this {@link MdInfo}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Returns the name of this {@link MdInfo}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the name of this {@link MdInfo}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the comment of this {@link MdInfo}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the comment of this {@link MdInfo}
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns the quality score of this {@link MdInfo}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the quality score of this {@link MdInfo}
	 */
	public Integer getQualityScore() {
		return qualityScore;
	}

	/**
	 * Returns the checked status of this {@link MdInfo}.
	 * 
	 * If not set returns null.
	 * 
	 * @return the checked status of this {@link MdInfo}
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * Sets the id of this {@link MdInfo}.
	 * 
	 * @param id the id to be set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Sets the name of this {@link MdInfo}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param name the name to be set
	 */
	public void setName(final String name) {
		this.name = Strings.emptyToNull(name);
	}

	/**
	 * Sets the comment of this {@link MdInfo}.
	 * 
	 * Empty strings are converted to null.
	 * 
	 * @param comment the comment to be set
	 */
	public void setComment(final String comment) {
		this.comment = Strings.emptyToNull(comment);
	}

	/**
	 * Sets the quality score of this {@link MdInfo}.
	 * 
	 * @param qualityScore the quality score to be set
	 */
	public void setQualityScore(final Integer qualityScore) {
		this.qualityScore = qualityScore;
	}

	/**
	 * Sets the checked status of this {@link MdInfo}.
	 * 
	 * @param checked the checked status to be set
	 */
	public void setChecked(final Boolean checked) {
		this.checked = checked;
	}

	/** Saves model info properties into a {@link NodeSettingsWO}. */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt(ID, id, settings);
		SettingsHelper.addString(NAME, name, settings);
		SettingsHelper.addString(COMMENT, comment, settings);
		SettingsHelper.addInt(QUALITYSCORE, qualityScore, settings);
		SettingsHelper.addBoolean(CHECKED, checked, settings);
	}

	/** Loads model info properties from a {@link NodeSettingsRO}. */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger(ID, settings);
		name = SettingsHelper.getString(NAME, settings);
		comment = SettingsHelper.getString(COMMENT, settings);
		qualityScore = SettingsHelper.getInteger(QUALITYSCORE, settings);
		checked = SettingsHelper.getBoolean(CHECKED, settings);
	}
	
	/**
	 * Creates an MdInfo from an {@link MdInfoXml}.
	 * 
	 * @param mdInfoXml
	 */
	public static MdInfo toMdInfo(MdInfoXml mdInfoXml) {
		MdInfo mdInfo = new MdInfo();
		mdInfo.setId(mdInfoXml.getId());
		mdInfo.setName(mdInfoXml.getName());
		mdInfo.setComment(mdInfoXml.getComment());
		mdInfo.setQualityScore(mdInfoXml.getQualityScore());
		mdInfo.setChecked(mdInfoXml.getChecked());
		
		return mdInfo;
	}
	
	/**
	 * Returns an equivalent MdInfoXml.
	 * 
	 * @return an equivalent MdInfoXml
	 */
	public MdInfoXml toMdInfoXml() {
		return new MdInfoXml(id, name, comment, qualityScore, checked);
	}
}
