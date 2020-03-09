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
package de.bund.bfr.knime.pcml.node.xmltopcml;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * The settings object for the "XML to PCML" node.
 */
public class XMLToPCMLNodeSettings {

	private static final String COLUMN = "column";
	
	
	private String column;

	public XMLToPCMLNodeSettings() {
		column = null;
	}

	/** Saves current parameters to settings object.
     * @param settings to save to
     */
    public void saveSettings(final NodeSettingsWO settings) {    	
        settings.addString(COLUMN, column);
    }

    /** Loads parameters in NodeModel.
     * @param settings to load from
     * @throws InvalidSettingsException if settings are inconsistent
     */
    public void loadSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        column = settings.getString(COLUMN);
    }


    /** Loads parameters in Dialog.
     * @param settings to load from
     */
    public void loadSettingsForDialog(final NodeSettingsRO settings) {
        column = settings.getString(COLUMN, COLUMN);
    }

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
}
