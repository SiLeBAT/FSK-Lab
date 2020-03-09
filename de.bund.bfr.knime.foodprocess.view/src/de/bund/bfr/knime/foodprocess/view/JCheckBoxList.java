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


import java.awt.Component;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent; 
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox; 
import javax.swing.JList; 
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel; 

public class JCheckBoxList extends JList<JCheckboxWithObject> { 

  /**
	 * 
	 */
	private static final long serialVersionUID = 1499726221263952919L;

	private JCheckBoxListPanel cblp;
	
	public JCheckBoxList(JCheckBoxListPanel cblp) { 
		this.cblp = cblp;
		
	    setCellRenderer(new CellRenderer()); 
	    addMouseListener(new MouseAdapter() { 
	      public void mousePressed(MouseEvent e) { 
	        int index = locationToIndex(e.getPoint()); 
	        if (index != -1) { 
	          JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index); 
	          checkbox.setSelected(!checkbox.isSelected()); 
	          checkBoxChecked(checkbox);
	          repaint(); 
	        } 
	      } 
	    }); 
	    setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
	  } 
	public void checkBoxChecked(JCheckBox cb) {
		cblp.checkBoxChecked(cb);
	}
	
	  protected class CellRenderer implements ListCellRenderer<Object> { 
	    public Component getListCellRendererComponent(JList<?> list, Object value, 
	        int index, boolean isSelected, boolean cellHasFocus) { 
	      JCheckBox checkbox = (JCheckBox) value; 
	
	      if (isSelected) { 
	        // checkbox.setBorderPainted(true); 
	        // checkbox.setForeground(UIManager.getColor("List.selectionForeground")); 
	        // checkbox.setBackground(UIManager.getColor("List.selectionBackground")); 
	      } else { 
	        // checkbox.setBorderPainted(false); 
	        // checkbox.setForeground(UIManager.getColor("List.foreground")); 

	    	  //checkbox.setBackground(UIManager.getColor("List.background")); 
	      } 
	      return checkbox; 
	    } 
	  } 
	
	  public void selectAll() { 
	    int size = this.getModel().getSize(); 
	    for (int i = 0; i < size; i++) { 
	      JCheckBox checkbox = this.getModel().getElementAt(i); 
	      checkbox.setSelected(true); 
	    } 
	    this.repaint(); 
	  } 
	
	  public void deselectAll() { 
	    int size = this.getModel().getSize(); 
	    for (int i = 0; i < size; i++) { 
	      JCheckBox checkbox = this.getModel().getElementAt(i); 
	      checkbox.setSelected(false); 
	    } 
	    this.repaint(); 
	  } 
	  
	  public List<String> getSelections() {
		  List<String> result = new ArrayList<String>();
		    int size = this.getModel().getSize(); 
		    for (int i = 0; i < size; i++) { 
		      JCheckboxWithObject checkbox = this.getModel().getElementAt(i); 
		      if (checkbox.isSelected()) {
		    	  result.add(checkbox.getObject().toString());
		      }
		    } 
		    return result;
	  }
}