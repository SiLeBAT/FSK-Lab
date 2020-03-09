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
package de.bund.bfr.knime.foodprocess.view;

import java.awt.Color;

import javax.swing.JCheckBox; 

import de.bund.bfr.knime.foodprocess.FoodProcessNodeModel;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

public class JCheckboxWithObject extends JCheckBox { 
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 3879388584261786210L;
	private Object object; 
	  
	  public JCheckboxWithObject(Object object) { 
		  setObject(object); 
	  } 
	  
	  public Object getObject() { 
	    return object; 
	  } 
	
	  public void setObject(Object object) { 
	    this.object = object; 
	    this.setText(object.toString()); 
	    manageColor();
	  } 
	  
	  private void manageColor() {
		  Color fg = Color.BLACK;
		  Color bg = Color.WHITE;
		  if (object != null) {
			  String param = object.toString();
			  if (isTemperature(param)) {
				  bg = Color.BLUE;
				  fg = Color.WHITE;
			  }
			  else if (isAW(param)) {
				  bg = new Color(160, 82, 45);
				  fg = Color.WHITE;
			  }
			  else if (isPH(param)) {
				  bg = Color.GREEN;
			  }
			  else if (isPressure(param)) {
				  bg = Color.BLACK;
				  fg = Color.WHITE;
			  }
			  else if (isAgent(param)) {
				  bg = Color.RED;
				  fg = Color.WHITE;
			  }
			  else if (isMatrix(param)) {
				  bg = Color.GRAY;
				  fg = Color.WHITE;
			  }
			  else if (isWithUnit(param)) {
				  bg = Color.LIGHT_GRAY;
				  fg = Color.BLACK;
			  }
		  }
		  this.setBackground(bg);
		  this.setForeground(fg);
	  }
	  
	  public static boolean isAgent(String param) {
		  String dbu = FoodProcessNodeModel.defaultBacterialUnit;
		  String dpu = "ppm";
		  if (param != null) {
			  String lParam = param.toLowerCase();
				return lParam.endsWith("[" + dpu + "]") ||
						lParam.endsWith("[" + dbu.replace("count", "cfu") + "]") ||
						lParam.endsWith("[" + dbu.replace("count", "pfu") + "]") ||
						lParam.endsWith("[" + dbu.replace("count", "spores") + "]");
		  }
		  return false;
		}
	  public static boolean isMatrix(String param) {
			return param != null && param.endsWith("[kg]");
	  }
	  public static boolean isPressure(String param) {
			return param != null && param.equals("pressure [bar]");
	  }
	  public static boolean isPH(String param) {
			return param != null && param.equals(AttributeUtilities.ATT_PH);
	  }
	  public static boolean isAW(String param) {
			return param != null && param.equals(AttributeUtilities.ATT_AW);
	  }
	  public static boolean isTemperature(String param) {
			return param != null && param.equals(AttributeUtilities.ATT_TEMPERATURE + " [°C]");
	  }
	  public static boolean isWithUnit(String param) {
			return param != null && param.endsWith("]") && param.indexOf("]") > 0;
	  }
}