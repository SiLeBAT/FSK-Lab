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
package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.SortedMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;

public class ModelTableModel extends JTable {

	private static final long serialVersionUID = -6782674430592418376L;
		
	// If changing here: please look at: getValueAt(), setValueAt(), isCellEditable(), getColumnClass(), MyTableCellRenderer
	private String[] columns = new String[]{"Parameter", "Unit", "Independent", "Value", "StandardErr", "Min", "Max", "Description"};
	private HashMap<String, ParametricModel> m_secondaryModels = null;
	private JRadioButton radioButton3 = null;
	private ParametricModel thePM;
	private boolean hasChanged = false;
	private boolean repaintAgain = false;
	private HashMap<String, Boolean> rowHasChanged;
	
	private boolean isBlankEditor = false;

	public ModelTableModel() {
		super();
		BooleanTableModel btm = new BooleanTableModel();
		rowHasChanged = new HashMap<>();
		this.setModel(btm);	
		this.setDefaultRenderer(Object.class, new MyTableCellRenderer());
		this.setDefaultRenderer(Boolean.class, new MyTableCellRenderer());
		this.setDefaultRenderer(Double.class, new MyTableCellRenderer());
		this.getTableHeader().setReorderingAllowed(false);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	public void setPM(ParametricModel pm, HashMap<String, ParametricModel> secondaryModels, JRadioButton radioButton3) {
		thePM = pm;
		m_secondaryModels = secondaryModels;
		this.radioButton3 = radioButton3;
		thePM.validateParams();
		this.revalidate();
		hasChanged = false;
		rowHasChanged = new HashMap<>();
	}
	public ParametricModel getPM() {
		return thePM;
	}
	public boolean hasChanged() {
		return hasChanged;
	}
	public boolean isEstimated() {
		if (thePM.getRsquared() != null && !Double.isNaN(thePM.getRsquared()) ||
				thePM.getRms() != null && !Double.isNaN(thePM.getRms()) ||
				thePM.getAic() != null && !Double.isNaN(thePM.getAic()) ||
				thePM.getBic() != null && !Double.isNaN(thePM.getBic()))
			return true;
		for (PmmXmlElementConvertable el : thePM.getParameter().getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				if (px.getError() != null && !Double.isNaN(px.getError()) ||
						px.getValue() != null && !Double.isNaN(px.getValue())) {
					return true;
				}
			}
		}

		return false;
	}
	public void clearTable() {
		thePM = new ParametricModel("", "", null, 1);
		this.revalidate();
		hasChanged = false;
		rowHasChanged = new HashMap<>();
	}

	// Here: functionality: always overwrite cell except for pressed F2, which means: activate cell
	@Override
	public Component prepareEditor(final TableCellEditor editor, final int row, final int column) {
		Component c = super.prepareEditor(editor, row, column);		
		if (isBlankEditor) {
			((JTextField) c).setText("");
		}		
		return c;
	}
	@Override
	protected boolean processKeyBinding(final KeyStroke ks, final KeyEvent e, final int condition, final boolean pressed) {
		char ch = e.getKeyChar();
		if (ch == ',') {
  			e.setKeyChar('.');
		}
		if (!KeyEvent.getKeyText(e.getKeyCode()).equals("F2")) {
			isBlankEditor = true;
		}		
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);		
		isBlankEditor = false;
		return retValue;
	}
	
	private class BooleanTableModel extends AbstractTableModel {
 
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public int getRowCount() {
        	if (thePM == null) return 0;
        	else return thePM.getAllParVars().size() + 1;
        }
 
        public int getColumnCount() {
            return columns.length;
        }
 
        public Object getValueAt(int rowIndex, int columnIndex) {
        	if (thePM == null) return null;
        	if (rowIndex == 0) {
        		DepXml dx = thePM.getDepXml();
        		if (columnIndex < 2) return thePM.getDepVar();
        		else if (columnIndex == 5 && dx != null) return dx.getMin();
        		else if (columnIndex == 6 && dx != null) return dx.getMax();
        		else if (columnIndex > 6) return thePM.getDepDescription();
        		else return null;
        	}
        	SortedMap<String, Boolean> sm = thePM.getAllParVars();
        	Object[] oa = sm.keySet().toArray();
        	if (rowIndex > 0 && rowIndex <= oa.length) {
            	String rowID = oa[rowIndex - 1].toString();
            	boolean isIndep = sm.get(rowID);
            	if (columnIndex == 0) return rowID;
            	if (columnIndex == 1) return rowID;
            	if (columnIndex == 2) return isIndep;
            	if (columnIndex == 3) return isIndep ? null : 
            		thePM.getParamValue(rowID) == null || Double.isNaN(thePM.getParamValue(rowID)) ? null : thePM.getParamValue(rowID);
            	if (columnIndex == 4) return isIndep ? null : 
            		thePM.getParamError(rowID) == null || Double.isNaN(thePM.getParamError(rowID)) ? null : thePM.getParamError(rowID);
            	if (columnIndex == 5) return isIndep ? (thePM.getIndepMin(rowID) == null || Double.isNaN(thePM.getIndepMin(rowID)) ? null : thePM.getIndepMin(rowID)) :
            		(thePM.getParamMin(rowID) == null || Double.isNaN(thePM.getParamMin(rowID)) ? null : thePM.getParamMin(rowID));
            	if (columnIndex == 6) return isIndep ? (thePM.getIndepMax(rowID) == null || Double.isNaN(thePM.getIndepMax(rowID)) ? null : thePM.getIndepMax(rowID)) :
            		(thePM.getParamMax(rowID) == null || Double.isNaN(thePM.getParamMax(rowID)) ? null : thePM.getParamMax(rowID));
            	if (columnIndex == 7) return isIndep ? (thePM.getIndepDescription(rowID) == null ? null : thePM.getIndepDescription(rowID)) :
            		(thePM.getParamDescription(rowID) == null ? null : thePM.getParamDescription(rowID));
        	}
        	return null;
        }
 
        public void setValueAt(Object o, int rowIndex, int columnIndex) {
        	if (thePM == null) return;
        	if (rowIndex == 0) {
        		DepXml dx = thePM.getDepXml();
        		if (columnIndex == 1) {
        			rowHasChanged.put(thePM.getDepVar(), true);
            		hasChanged = true;
            		repaintAgain = true;
        		}
        		else if (columnIndex == 5 && dx != null && (o == null || o instanceof Double)) {
        			dx.setMin((Double) o);
        			rowHasChanged.put(thePM.getDepVar(), true);
            		hasChanged = true;
            		repaintAgain = true;
        		}
        		else if (columnIndex == 6 && dx != null && (o == null || o instanceof Double)) {
        			dx.setMax((Double) o);
        			rowHasChanged.put(thePM.getDepVar(), true);
            		hasChanged = true;
            		repaintAgain = true;
        		}
        		else if (columnIndex == 7) {
            		thePM.setDepDescription(o == null ? "" : o.toString());
        			rowHasChanged.put(thePM.getDepVar(), true);
            		hasChanged = true;
            		repaintAgain = true;
        		}
        		return;
        	}
        	SortedMap<String, Boolean> sm = thePM.getAllParVars();
        	Object[] oa = sm.keySet().toArray();
        	if (rowIndex <= oa.length) {
            	String paramName = oa[rowIndex - 1].toString();
            	if (columnIndex == 2 && o instanceof Boolean) {
            		boolean isIndep = (Boolean) o;
            		if (isIndep) {
            			thePM.addIndepVar(paramName, thePM.getParamMin(paramName), thePM.getParamMax(paramName), thePM.getParamCategory(paramName), thePM.getParamUnit(paramName), thePM.getParamDescription(paramName));
            			thePM.removeParam(paramName);
                		if (thePM.getLevel() == 1) { // only one indepVar allowed
                			for (int i=1;i<this.getRowCount();i++) {
                				if (i != rowIndex && this.getValueAt(i, 2) != null && (Boolean) this.getValueAt(i, 2)) {
                					this.setValueAt(false, i, 2);
                				}
                			}
                		}
            		}
            		else {
            			thePM.addParam(paramName, null, Double.NaN, Double.NaN, thePM.getIndepMin(paramName), thePM.getIndepMax(paramName), thePM.getIndepCategory(paramName), thePM.getIndepUnit(paramName), thePM.getIndepDescription(paramName));
            			thePM.removeIndepVar(paramName);
            		}
                	hasChanged = true;
            		repaintAgain = true;
            	}
            	else {
                	boolean isIndep = sm.get(paramName);
                	if (isIndep) {
                    	if (columnIndex == 5 && (o == null || o instanceof Double)) thePM.setIndepMin(paramName, (Double) o);
                    	if (columnIndex == 6 && (o == null || o instanceof Double)) thePM.setIndepMax(paramName, (Double) o);
                    	if (columnIndex == 7) {
                    		thePM.setIndepDescription(paramName, o == null ? "" : o.toString());
                    		hasChanged = true;
                    		repaintAgain = true;
                    	}
                	}
                	else {
                    	if (columnIndex == 3 && (o == null || o instanceof Double)) thePM.setParamValue(paramName, (Double) o);
                    	if (columnIndex == 4 && (o == null || o instanceof Double)) thePM.setParamError(paramName, (Double) o);
                    	if (columnIndex == 5 && (o == null || o instanceof Double)) {
                    		thePM.setParamMin(paramName, (Double) o);
                        	hasChanged = true;
                    		repaintAgain = true;
                    	}
                    	if (columnIndex == 6 && (o == null || o instanceof Double)) {
                    		thePM.setParamMax(paramName, (Double) o);
                    		hasChanged = true;
                    		repaintAgain = true;
                    	}
                    	if (columnIndex == 7) {
                    		thePM.setParamDescription(paramName, o == null ? "" : o.toString());
                    		hasChanged = true;
                    		repaintAgain = true;
                    	}
                	}
            	}
            	//super.fireTableCellUpdated(rowIndex, columnIndex);
            	rowHasChanged.put(paramName, true);
        	}
        }
 
        public String getColumnName(int columnIndex) {
        	return columns[columnIndex];
        }
 
		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			if (rowIndex == 0) return (columnIndex >= 5);
		    Boolean indep = (Boolean) this.getValueAt(rowIndex, 2);
		    if (indep == null) indep = false;
			return columnIndex == 2 || columnIndex > 4 || (!indep && (columnIndex == 4 || columnIndex == 3));
		}

        public Class<?> getColumnClass(int columnIndex) {
        	if (columnIndex == 0) return Object.class;
        	else if (columnIndex == 1) return Object.class;
        	else if (columnIndex == 2) return Boolean.class;
        	else if (columnIndex == 3) return Double.class;
        	else if (columnIndex == 4) return Double.class;
        	else if (columnIndex == 5) return Double.class;
        	else if (columnIndex == 6) return Double.class;
        	else if (columnIndex == 7) return String.class;
        	else return Object.class;
        }
    }

	private class MyTableCellRenderer implements TableCellRenderer {
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
			  JComponent c;
			  if (columnIndex == 0) {
				    Boolean indep = (Boolean) table.getValueAt(rowIndex, 2);
				    if (indep == null) indep = false;
				    String text = "";
				    if (value != null) {
				    	text = value.toString();
				    	if (rowHasChanged.get(value) != null && rowHasChanged.get(value)) text += "*";
				    }
				    JComponent editor;
				    if (thePM.getLevel() == 1 && !indep && rowIndex > 0 && radioButton3.isSelected()) {
				    	editor = new JButton(text);					  
				    }
				    else {
					    editor = new JTextField(text);					  
				    }
				    //editor.setEnabled(false);
				    boolean hasSecondary = !indep && m_secondaryModels != null && m_secondaryModels.containsKey(value);
				    editor.setFont(editor.getFont().deriveFont(hasSecondary ? Font.BOLD : Font.PLAIN));
				    editor.setToolTipText(hasSecondary ? m_secondaryModels.get(value).getModelName() : "");
				    editor.setBackground(new Color(244, 244, 244));
				    editor.setForeground(Color.BLACK);
				    c = editor;
			  }
			  else if (columnIndex == 1) {
				    Boolean indep = (Boolean) table.getValueAt(rowIndex, 2);
				    if (indep == null) indep = false;
				    String text = "";
				    if (value != null) {
				    	text = value.toString();
				    	String category = indep ? thePM.getIndepCategory(text) : thePM.getParamCategory(text);
				    	String unit = indep ? thePM.getIndepUnit(text) : thePM.getParamUnit(text);
				    	if (rowIndex == 0) {
				    		category = thePM.getDepCategory();
				    		unit = thePM.getDepUnit();
				    	}
				    	if (unit != null && !unit.isEmpty()) text = category + " -> " + unit;
				    	else text = "";
				    }
				    JComponent editor = new JTextField(text);
				    if (rowIndex == 0) editor.setEnabled(false);
				    editor.setBackground(new Color(244, 244, 244));
				    editor.setForeground(Color.BLACK);
				    c = editor;
			  }
			  else if (columnIndex == 2) {
				  JCheckBox checkbox = new JCheckBox();
				  checkbox.setHorizontalAlignment(SwingConstants.CENTER);
				  checkbox.setText("");
				  if (rowIndex == 0) {checkbox.setEnabled(false); if (thePM.getLevel() == 1) checkbox.setText("Value");}
				  else {checkbox.setEnabled(true); }
				  checkbox.setSelected(value == null ? false : (Boolean) value);
				  if (checkbox.isSelected() && thePM.getLevel() == 1) checkbox.setText("Time");
				  checkbox.setBackground(Color.WHITE);
				  c = checkbox;
			  }
			  else if (columnIndex == 7) {
				  JTextField text = new JTextField();
				  text.setEnabled(true);
				  text.setText(value == null ? "" : value.toString());
				  text.setBackground(Color.WHITE);
				  c = text;
			  }
			  else {
				    DoubleTextField editor = new DoubleTextField(true);
				    if (value != null && value instanceof Double && !Double.isNaN((Double) value)) editor.setText(value.toString());
				    else editor.setText(null);
				    if (columnIndex == 3 || columnIndex == 4) { // Value, Error
					    Boolean indep = (Boolean) table.getValueAt(rowIndex, 2);
					    if (indep == null) indep = false;
					    editor.setEnabled(!indep);				    	
				    }
				    if (columnIndex == 5 || columnIndex == 6) editor.setEnabled(true); // Min, Max
				    else if (rowIndex == 0) editor.setEnabled(false);
				    //if (isSelected)
				    c = editor;
			  }
			  
			  try {
				  if (isSelected && hasFocus) {
					  c.setBackground(Color.CYAN);
					  c.setForeground(Color.BLACK);
				  }
				  else if (isSelected) {
					  UIDefaults uiDefaults = UIManager.getDefaults();
					  Color selBGColor = (Color) uiDefaults.get("Table.selectionBackground");
					  Color selFGColor = (Color) uiDefaults.get("Table.selectionForeground");
					  c.setBackground(selBGColor);			
					  c.setForeground(selFGColor);
				  }
			  }
			  catch (Exception e) {}
			  
			  c.setBorder(null);
			  if (repaintAgain) {
				  table.repaint();
				  if (rowIndex == getRowCount() - 1 && columnIndex == getColumnCount() - 1) repaintAgain = false;
			  }
			  return c;
		  }
		}
}
