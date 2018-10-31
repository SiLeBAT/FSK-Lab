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
package de.bund.bfr.knime.pmm.common.generictablemodel;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCell;

import de.bund.bfr.knime.pmm.common.PmmException;

public class KnimeSchema {
	
	private LinkedList<KnimeAttribute> attributeSet;
	private LinkedHashMap<String, Integer> attributeMap;
	
	public KnimeSchema() {
		attributeSet = new LinkedList<>();
		attributeMap = new LinkedHashMap<>();
	}
	
	/** Merges the two schemata a and b to a new schema.
	 * 
	 * @param a
	 * @param b
	 * @throws PmmException 
	 */
	public KnimeSchema(KnimeSchema a, KnimeSchema b) throws PmmException {
		if (a == null || b == null) throw new PmmException( "Schema must not be null." );
		
		int m = a.size();
		int n = b.size();
		
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				if (a.getName(i).equals(b.getName(j))) throw new PmmException( "Duplicate names are not allowed." );
		
		attributeSet = new LinkedList<>();
		attributeSet.addAll(a.attributeSet);
		attributeSet.addAll(b.attributeSet);
		
		generateMap();
	}
	
	public static KnimeSchema merge( KnimeSchema a, KnimeSchema b ) throws PmmException {
		return new KnimeSchema( a, b );
	}
		
	public void addIntAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_INT );
	}
	
	public void addDoubleAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_DOUBLE );
	}
	
	public void addStringAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_STRING );
	}	
	
	public void addXmlAttribute( final String name ) throws PmmException {
		addAttribute( name, KnimeAttribute.TYPE_XML );
	}
	
	public boolean conforms( DataColumnSpec[] spec ) throws PmmException {
		
		if( spec == null )
			return false;
		
		return conforms( new DataTableSpec( spec ) );
	}
	
	public boolean conforms( DataTable table ) throws PmmException {
		
		if( table == null )
			return false;
		
		return conforms( table.getDataTableSpec() );
	}
	
	public boolean conforms(DataTableSpec tspec) throws PmmException {
		if (tspec == null) return false;
		
		int n = tspec.getNumColumns();
		for (KnimeAttribute col : attributeSet) {
			
			boolean present = false;
			for (int i = 0; i < n; i++) {				
				DataColumnSpec cspec = tspec.getColumnSpec(i);
				
				if (col.isInt() && cspec.getType() != IntCell.TYPE)
					continue;
				if (col.isDouble() && cspec.getType() != DoubleCell.TYPE)
					continue;
				
				if (col.name.equals(cspec.getName())) {
					present = true;
					break;
				}				
			}			
			if (!present) return false;
		}		
		return true;
	}
	
	/** Returns true if the other schema is a subset of the attributes of this schema.
	 * 
	 * @param other
	 * @return
	 */
	public boolean conforms( KnimeSchema other ) {
		
		int i, j;
		boolean found;
		
		for( i = 0; i < other.size(); i++ ) {
			
			found = false;
			for( j = 0; j < size(); j++ )
				if( other.getType( i ) == getType( j ) )
					if( other.getName( i ).equals( getName( j ) ) ) {
						found = true;
						break;
					}
			if( found == false )
				return false;
		}
		
		return true;
	}
	
	public boolean containsAtt( String name ) {
		
		int i;
		
		for( i = 0; i < size(); i++ )
			if( getName( i ).equals( name ) )
				return true;
		
		return false;
	}
	
	public DataTableSpec createSpec() {		
		DataType t;
		
		DataColumnSpec[] spec = new DataColumnSpec[size()];
		for (int i = 0; i < size(); i++ ) {			
			KnimeAttribute col = attributeSet.get(i);			
			switch(col.type) {			
				case KnimeAttribute.TYPE_INT :
					t = IntCell.TYPE; break;					
				case KnimeAttribute.TYPE_DOUBLE :
					t = DoubleCell.TYPE; break;
				case KnimeAttribute.TYPE_XML :
					t = XMLCell.TYPE; break; // XMLCell StringCell
				default :
					t = StringCell.TYPE;
			}
			
			spec[i] = new DataColumnSpecCreator(col.name, t).createSpec();
		}
		
		return new DataTableSpec( spec );
	}
	
	private void generateMap() {
		attributeMap = new LinkedHashMap<>();
		for (int i=0;i<attributeSet.size();i++) {
			attributeMap.put(attributeSet.get(i).name, i);
		}		
	}
	public int getIndex(String attName) throws PmmException {
		if (attributeMap == null) {
			generateMap();
		}
		return attributeMap.get(attName);
		/*
		else {
			throw new PmmException("An attribute with the name " +attName+" is not part of the schema.");
		}
		for (int i = 0; i < size(); i++) {
			if (attributeSet.get(i).getName().equals(attName)) return i;
		}
		*/
	}
	
	public String getName(final int i) {
		return attributeSet.get(i).name;
	}
	
	public int getType(final int i) { return attributeSet.get(i).type; }
	
	public int size() {
		return attributeSet.size();
	}
	
	private void addAttribute(final KnimeAttribute col) throws PmmException {		
		if (col == null) throw new PmmException("Attribute must not be null.");		
		attributeSet.add(col);
		attributeMap.put(col.name, attributeMap.size());
	}
	
	private void addAttribute( final String name, final int type ) throws PmmException {
		addAttribute( new KnimeAttribute( name, type ) );
	}
	
	public static String getAttribute(String attribute, int level) {
		if (level == 1) return attribute;
		else if (level == 2 || level == 3) return attribute + "Sec";
		else return null;
	}
	
}
