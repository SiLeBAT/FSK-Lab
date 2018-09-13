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

	private int id;
	private String unit;
	private String description;
	private String name;
	private String kind_of_property_quantity;
	private String notation_case_sensitive;
	private String convert_to;
	private String conversion_function_factor;
	private String inverse_conversion_function_factor;
	private String object_type;
	private String display_in_GUI_as;
	private String MathML_string;
	private String Priority_for_display_in_GUI;
	
	private Map<Integer, UnitsFromDB> ghm;
	
	public UnitsFromDB() {
		
	}
	private UnitsFromDB(int id, String unit, String description, String name, String kind_of_property_quantity,
			String notation_case_sensitive, String convert_to, String conversion_function_factor, String inverse_conversion_function_factor,
			String object_type, String display_in_GUI_as, String MathML_string, String Priority_for_display_in_GUI) {
		this.id = id;
		this.unit = unit;
		this.description = description;
		this.name = name;
		this.kind_of_property_quantity = kind_of_property_quantity;
		this.notation_case_sensitive = notation_case_sensitive;
		this.convert_to = convert_to;
		this.conversion_function_factor = conversion_function_factor;
		this.inverse_conversion_function_factor = inverse_conversion_function_factor;
		this.object_type = object_type;
		this.display_in_GUI_as = display_in_GUI_as;
		this.MathML_string = MathML_string;
		this.Priority_for_display_in_GUI = Priority_for_display_in_GUI;
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
					ghm.put(ufdb.getId(), ufdb);
				} while(rs.next());
			}
		}
		catch (SQLException e) {e.printStackTrace();}
	}
	
	public Map<Integer, UnitsFromDB> getMap() {
		return ghm;
	}

	public int getId() {
		return id;
	}

	public String getUnit() {
		return unit;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getKind_of_property_quantity() {
		return kind_of_property_quantity;
	}

	public String getNotation_case_sensitive() {
		return notation_case_sensitive;
	}

	public String getConvert_to() {
		return convert_to;
	}

	public String getConversion_function_factor() {
		return conversion_function_factor;
	}

	public String getInverse_conversion_function_factor() {
		return inverse_conversion_function_factor;
	}

	public String getObject_type() {
		return object_type;
	}

	public String getDisplay_in_GUI_as() {
		return display_in_GUI_as;
	}

	public String getMathML_string() {
		return MathML_string;
	}

	public String getPriority_for_display_in_GUI() {
		return Priority_for_display_in_GUI;
	}
}
