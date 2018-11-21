/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.common.EstModelXml;

/**
 * PmmLab estimated model. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>sse
 * <li>rms
 * <li>r2
 * <li>aic
 * <li>bic
 * <li>dof
 * <li>quality score
 * <li>checked
 * <li>comment
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class EstModel implements ViewValue {

	public Integer id;
	public String name;
	public Double sse;
	public Double rms;
	public Double r2;
	public Double aic;
	public Double bic;
	public Integer dof;
	public Integer qualityScore;
	public Boolean checked;
	public String comment;
	public String dbuuid;

	/**
	 * Saves estimated model properties into a {@link NodeSettingsWO} with
	 * properties:
	 * <ul>
	 * <li>String "id"
	 * <li>String "name"
	 * <li>Double "sse"
	 * <li>Double "rms"
	 * <li>Double "r2"
	 * <li>Double "aic"
	 * <li>Double "bic"
	 * <li>Integer "dof"
	 * <li>Integer "qualityScore"
	 * <li>Boolean "checked"
	 * <li>String "comment"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt("id", id, settings);
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addDouble("sse", sse, settings);
		SettingsHelper.addDouble("rms", rms, settings);
		SettingsHelper.addDouble("r2", r2, settings);
		SettingsHelper.addDouble("aic", aic, settings);
		SettingsHelper.addDouble("bic", bic, settings);
		SettingsHelper.addInt("dof", dof, settings);
		SettingsHelper.addInt("qualityScore", qualityScore, settings);
		SettingsHelper.addBoolean("checked", checked, settings);
		SettingsHelper.addString("comment", comment, settings);
		SettingsHelper.addString("dbuuid", dbuuid, settings);
	}

	/**
	 * Loads estimated model properties from a {@link NodeSettingsRO} with
	 * properties:
	 * <ul>
	 * <li>String "id"
	 * <li>String "name"
	 * <li>Double "sse"
	 * <li>Double "rms"
	 * <li>Double "r2"
	 * <li>Double "aic"
	 * <li>Double "bic"
	 * <li>Integer "dof"
	 * <li>Integer "qualityScore"
	 * <li>Boolean "checked"
	 * <li>String "comment"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger("id", settings);
		name = SettingsHelper.getString("name", settings);
		sse = SettingsHelper.getDouble("sse", settings);
		rms = SettingsHelper.getDouble("rms", settings);
		r2 = SettingsHelper.getDouble("r2", settings);
		aic = SettingsHelper.getDouble("aic", settings);
		bic = SettingsHelper.getDouble("bic", settings);
		dof = SettingsHelper.getInteger("dof", settings);
		qualityScore = SettingsHelper.getInteger("qualityScore", settings);
		checked = SettingsHelper.getBoolean("checked", settings);
		comment = SettingsHelper.getString("comment", settings);
		dbuuid = SettingsHelper.getString("dbuuid", settings);
	}

	/** Creates an {@link EstModel} from an {@link EstModelXml}. */
	public static EstModel toEstModel(EstModelXml estModelXml) {
		EstModel estModel = new EstModel();
		estModel.id = estModelXml.id;
		estModel.name = estModelXml.name;
		estModel.sse = estModelXml.sse;
		estModel.rms = estModelXml.rms;
		estModel.r2 = estModelXml.r2;
		estModel.aic = estModelXml.aic;
		estModel.bic = estModelXml.bic;
		estModel.dof = estModelXml.dof;
		estModel.qualityScore = estModelXml.qualityScore;
		estModel.checked = estModelXml.checked;
		estModel.comment = estModelXml.comment;
		estModel.dbuuid = estModelXml.dbuuid;

		return estModel;
	}

	public EstModelXml toEstModelXml() {
		EstModelXml estModelXml = new EstModelXml(id, name, sse, rms, r2, aic, bic, dof, checked, qualityScore, dbuuid);
		estModelXml.comment = comment;

		return estModelXml;
	}
}
