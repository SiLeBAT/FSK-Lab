package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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

	public Integer id;
	public String name;
	public String comment;
	public Integer qualityScore;
	public Boolean checked;

	/**
	 * Saves model info properties into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>Integer "ID"
	 * <li>String "Name"
	 * <li>String "Comment"
	 * <li>String "QualityScore"
	 * <li>String "Checked"
	 * </ul>
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt("ID", id, settings);
		SettingsHelper.addString("Name", name, settings);
		SettingsHelper.addString("Comment", comment, settings);
		SettingsHelper.addInt("QualityScore", qualityScore, settings);
		SettingsHelper.addBoolean("Checked", checked, settings);
	}

	/**
	 * Loads model info properties from a {@link NodeSettingsRO} with properties:
	 * <ul>
	 * <li>Integer "ID"
	 * <li>String "Name"
	 * <li>String "Comment"
	 * <li>String "QualityScore"
	 * <li>String "Checked"
	 * </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger("ID", settings);
		name = SettingsHelper.getString("Name", settings);
		comment = SettingsHelper.getString("Comment", settings);
		qualityScore = SettingsHelper.getInteger("QualityScore", settings);
		checked = SettingsHelper.getBoolean("Checked", settings);
	}

	/**
	 * Creates an MdInfo from an {@link MdInfoXml}.
	 * 
	 * @param mdInfoXml
	 */
	public static MdInfo toMdInfo(MdInfoXml mdInfoXml) {
		MdInfo mdInfo = new MdInfo();
		mdInfo.id = mdInfoXml.id;
		mdInfo.name = mdInfoXml.name;
		mdInfo.comment = mdInfoXml.comment;
		mdInfo.qualityScore = mdInfoXml.qualityScore;
		mdInfo.checked = mdInfoXml.checked;

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
