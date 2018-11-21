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

import de.bund.bfr.knime.pmm.common.MatrixXml;

/**
 * PmmLab matrix. Holds:
 * <ul>
 * <li>id
 * <li>name
 * <li>detail
 * <li>dbuuid
 * </ul>
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Matrix implements ViewValue {

	public Integer id;
	public String name;
	public String detail;
	public String dbuuid;

	/**
	 * Saves matrix properties into a {@link NodeSettingsWO} with properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "detail"
	 * <li>String "dbuuid"
	 * </ul>
	 * 
	 * @param settings
	 *            settings where to save the {@link Matrix} properties
	 */
	public void saveToNodeSettings(NodeSettingsWO settings) {
		SettingsHelper.addInt("id", id, settings);
		SettingsHelper.addString("name", name, settings);
		SettingsHelper.addString("detail", detail, settings);
		SettingsHelper.addString("dbuuid", dbuuid, settings);
	}

	/**
	 * Loads matrix properties from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 *            with properties:
	 *            <ul>
	 *            <li>Integer "id"
	 *            <li>String "name"
	 *            <li>String "detail"
	 *            <li>String "dbuuid"
	 *            </ul>
	 */
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		id = SettingsHelper.getInteger("id", settings);
		name = SettingsHelper.getString("name", settings);
		detail = settings.getString("detail", detail);
		dbuuid = settings.getString("dbuuid", dbuuid);
	}

	/**
	 * Creates a Matrix from a MatrixXml.
	 * 
	 * @param matrixXml
	 */
	public static Matrix toMatrix(MatrixXml matrixXml) {
		Matrix matrix = new Matrix();
		matrix.id = matrixXml.id;
		matrix.name = matrixXml.name;
		matrix.detail = matrixXml.detail;
		matrix.dbuuid = matrixXml.dbuuid;

		return matrix;
	}

	/**
	 * Returns an equivalent MatrixXml.
	 * 
	 * @return an equivalent MatrixXml
	 */
	public MatrixXml toMatrixXml() {
		return new MatrixXml(id, name, detail, dbuuid);
	}
}
