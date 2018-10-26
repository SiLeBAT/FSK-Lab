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
package de.bund.bfr.knime.pmm.xml2table;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

public class SettingsHelper {
	
	private static final String CFG_COLUMN = "Column";
	private static final String CFG_XML_ELEMENTS = "XmlElements";
	
	private String column;
	private String[] xmlElements;
	
	public SettingsHelper() {
		column = null;
		xmlElements = new String[0];
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			column = settings.getString(CFG_COLUMN);
		} catch (InvalidSettingsException e) {
		}
		
		try {
			xmlElements = settings.getStringArray(CFG_XML_ELEMENTS);
		} catch (InvalidSettingsException e) {
		}
	}
	
	public void saveSettings(NodeSettingsWO settings) {
		settings.addString(CFG_COLUMN, column);
		settings.addStringArray(CFG_XML_ELEMENTS, xmlElements);
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String[] getXmlElements() {
		return xmlElements;
	}

	public void setXmlElements(String[] xmlElements) {
		this.xmlElements = xmlElements;
	}
}
