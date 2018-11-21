package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * PmmLab literature item. Holds:
 * <ul>
 * <li>author
 * <li>year
 * <li>title
 * <li>abstract
 * <li>journal
 * <li>volume
 * <li>issue
 * <li>page
 * <li>approval mode
 * <li>website
 * <li>type
 * <li>comment
 * <li>id
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Literature implements ViewValue {

	public Integer id;
	public String author;
	public String title;
	public String abstractText;
	public Integer year;
	public String journal;
	public String volume;
	public String issue;
	public Integer page;
	public Integer approvalMode;
	public String website;
	public Integer type;
	public String comment;
	public String dbuuid;

	/**
	 * Save to settings with properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "author"
	 * <li>String "title"
	 * <li>String "abstract"
	 * <li>Integer "year"
	 * <li>String "journal"
	 * <li>String "volume"
	 * <li>String "issue"
	 * <li>Integer "page"
	 * <li>Integer "approvalMode"
	 * <li>String "website"
	 * <li>Integer "type"
	 * <li>String "comment"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt("id", id, settings);
		SettingsHelper.addString("author", author, settings);
		SettingsHelper.addString("title", title, settings);
		SettingsHelper.addString("abstract", abstractText, settings);
		SettingsHelper.addInt("year", year, settings);
		SettingsHelper.addString("journal", journal, settings);
		SettingsHelper.addString("volume", volume, settings);
		SettingsHelper.addString("issue", issue, settings);
		SettingsHelper.addInt("page", page, settings);
		SettingsHelper.addInt("approvalMode", approvalMode, settings);
		SettingsHelper.addString("website", website, settings);
		SettingsHelper.addInt("type", type, settings);
		SettingsHelper.addString("comment", comment, settings);
		SettingsHelper.addString("dbuuid", dbuuid, settings);
	}

	/**
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>Integer "id"
	 *            <li>String "author"
	 *            <li>String "title"
	 *            <li>String "abstract"
	 *            <li>Integer "year"
	 *            <li>String "journal"
	 *            <li>String "volume"
	 *            <li>String "issue"
	 *            <li>Integer "page"
	 *            <li>Integer "approvalMode"
	 *            <li>String "website"
	 *            <li>Integer "type"
	 *            <li>String "comment"
	 *            <li>String "dbuuid"
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger("id", settings);
		author = SettingsHelper.getString("author", settings);
		title = SettingsHelper.getString("title", settings);
		abstractText = SettingsHelper.getString("abstract", settings);
		year = SettingsHelper.getInteger("year", settings);
		journal = SettingsHelper.getString("journal", settings);
		volume = SettingsHelper.getString("volume", settings);
		issue = SettingsHelper.getString("issue", settings);
		page = SettingsHelper.getInteger("page", settings);
		approvalMode = SettingsHelper.getInteger("approvalMode", settings);
		website = SettingsHelper.getString("website", settings);
		type = SettingsHelper.getInteger("type", settings);
		comment = SettingsHelper.getString("comment", settings);
		dbuuid = SettingsHelper.getString("dbuuid", settings);
	}

	public static Literature toLiterature(LiteratureItem literatureItem) {
		Literature literature = new Literature();
		literature.id = literatureItem.id;
		literature.author = literatureItem.author;
		literature.title = literatureItem.title;
		literature.abstractText = literatureItem.abstractText;
		literature.year = literatureItem.year;
		literature.journal = literatureItem.journal;
		literature.volume = literatureItem.volume;
		literature.issue = literatureItem.issue;
		literature.page = literatureItem.page;
		literature.approvalMode = literatureItem.approvalMode;
		literature.website = literatureItem.website;
		literature.type = literatureItem.type;
		literature.comment = literatureItem.comment;
		literature.dbuuid = literatureItem.dbuuid;

		return literature;
	}

	public LiteratureItem toLiteratureItem() {
		return new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page, approvalMode,
				website, type, comment, id, dbuuid);
	}
}
