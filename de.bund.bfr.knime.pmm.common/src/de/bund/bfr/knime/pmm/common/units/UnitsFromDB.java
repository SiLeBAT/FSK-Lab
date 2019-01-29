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
package de.bund.bfr.knime.pmm.common.units;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hsh.bfr.db.DBKernel;


public class UnitsFromDB {

	public final int id;
	public final String unit;
	public final String description;
	public final String name;
	public final String kindOfPropertyQuantity;
	public final String notationCaseSensitive;
	public final String convertTo;
	public final String conversionFunctionFactor;
	public final String inverseConversionFunctionFactor;
	public final String objectType;
	public final String displayInGuiAs;
	public final String mathMlString;
	public final String priorityForDisplayInGui;
	
	private Map<Integer, UnitsFromDB> ghm;
	
	public UnitsFromDB() {
		id = 0;
		unit = null;
		description = null;
		name = null;
		kindOfPropertyQuantity = null;
		notationCaseSensitive = null;
		convertTo = null;
		conversionFunctionFactor = null;
		inverseConversionFunctionFactor = null;
		objectType = null;
		displayInGuiAs = null;
		mathMlString = null;
		priorityForDisplayInGui = null;
	}
	
	private UnitsFromDB(int id, String unit, String description, String name, String kind_of_property_quantity,
			String notation_case_sensitive, String convert_to, String conversion_function_factor, String inverse_conversion_function_factor,
			String object_type, String display_in_GUI_as, String MathML_string, String Priority_for_display_in_GUI) {
		this.id = id;
		this.unit = unit;
		this.description = description;
		this.name = name;
		this.kindOfPropertyQuantity = kind_of_property_quantity;
		this.notationCaseSensitive = notation_case_sensitive;
		this.convertTo = convert_to;
		this.conversionFunctionFactor = conversion_function_factor;
		this.inverseConversionFunctionFactor = inverse_conversion_function_factor;
		this.objectType = object_type;
		this.displayInGuiAs = display_in_GUI_as;
		this.mathMlString = MathML_string;
		this.priorityForDisplayInGui = Priority_for_display_in_GUI;
	}
	
	public void askDB() {
		ghm = new LinkedHashMap<>();
		ResultSet rs = DBKernel.getResultSet("SELECT * FROM " + DBKernel.delimitL("Einheiten"), true);
		try {
			if (rs != null && rs.first()) {
				do {
					UnitsFromDB ufdb = new UnitsFromDB(rs.getInt("ID"), rs.getString("Einheit"), rs.getString("Beschreibung"), rs.getString("name"), rs.getString("kind of property / quantity"),
							rs.getString("notation case sensitive"), rs.getString("convert to"), rs.getString("conversion function / factor"), rs.getString("inverse conversion function / factor"),
							rs.getString("object type"), rs.getString("display in GUI as"), rs.getString("MathML string"), rs.getString("Priority for display in GUI"));
					ghm.put(ufdb.id, ufdb);
				} while(rs.next());
			}
		}
		catch (SQLException e) {e.printStackTrace();}
	}
	
	public Map<Integer, UnitsFromDB> getMap() {
		return ghm;
	}
}
