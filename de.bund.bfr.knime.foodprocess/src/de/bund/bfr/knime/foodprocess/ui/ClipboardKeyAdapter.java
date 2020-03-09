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
package de.bund.bfr.knime.foodprocess.ui;

import java.awt.Toolkit; 
import java.awt.datatransfer.Clipboard; 
import java.awt.datatransfer.DataFlavor; 
import java.awt.datatransfer.StringSelection; 
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane; 
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** 
 * KeyAdapter to detect Windows standard cut, copy and paste keystrokes on a JTable and put them to the clipboard 
 * in Excel friendly plain text format. Assumes that null represents an empty column for cut operations. 
 * Replaces line breaks and tabs in copied cells to spaces in the clipboard. 
 * 
 * @see java.awt.event.KeyAdapter 
 * @see javax.swing.JTable 
 */ 
public class ClipboardKeyAdapter extends KeyAdapter {

        private static final String LINE_BREAK = "\n"; 
        private static final String CELL_BREAK = "\t"; 
        private static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard(); 
        
        private final JTable table; 
        
        public ClipboardKeyAdapter(JTable table) { 
                this.table = table; 
        } 
        
        @Override 
        public void keyReleased(KeyEvent event) { 
            if (event.getKeyCode()==KeyEvent.VK_DELETE) {
                cancelEditing(); 
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int numSelRows=table.getSelectedRowCount(); 
                int[] rowsSelected=table.getSelectedRows(); 
                for (int i=0;i<numSelRows;i++) {
                    model.removeRow(rowsSelected[i] - i);
                    /*
                	for (int j=rowsSelected[i] - i;j<table.getRowCount()-1;j++) {
                    	table.setValueAt(table.getValueAt(j+1, 0), j, 0);
                    	table.setValueAt(table.getValueAt(j+1, 1), j, 1);
                		if (table.getValueAt(j+1, 0) == null || table.getValueAt(j+1, 0).toString().isEmpty()) break;
                	}
                	*/
                }
            }
            else if (event.isControlDown()) { 
                    if (event.getKeyCode()==KeyEvent.VK_C) { // Copy                        
                            cancelEditing(); 
                            copyToClipboard(false); 
                    } else if (event.getKeyCode()==KeyEvent.VK_X) { // Cut 
                            cancelEditing(); 
                            copyToClipboard(true); 
                    } else if (event.getKeyCode()==KeyEvent.VK_V) { // Paste 
                            cancelEditing(); 
                            pasteFromClipboard();           
                    } 
            } 
        } 
        
        private void copyToClipboard(boolean isCut) { 
                int numCols=table.getSelectedColumnCount(); 
                int numRows=table.getSelectedRowCount(); 
                int[] rowsSelected=table.getSelectedRows(); 
                int[] colsSelected=table.getSelectedColumns(); 
                if (numRows!=rowsSelected[rowsSelected.length-1]-rowsSelected[0]+1 || numRows!=rowsSelected.length || 
                                numCols!=colsSelected[colsSelected.length-1]-colsSelected[0]+1 || numCols!=colsSelected.length) {

                        JOptionPane.showMessageDialog(null, "Invalid Copy Selection", "Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
                        return; 
                } 
                
                StringBuffer excelStr=new StringBuffer(); 
                for (int i=0; i<numRows; i++) { 
                        for (int j=0; j<numCols; j++) { 
                                excelStr.append(escape(table.getValueAt(rowsSelected[i], colsSelected[j]))); 
                                if (isCut) { 
                                        table.setValueAt(null, rowsSelected[i], colsSelected[j]); 
                                } 
                                if (j<numCols-1) { 
                                        excelStr.append(CELL_BREAK); 
                                } 
                        } 
                        excelStr.append(LINE_BREAK); 
                } 
                
                StringSelection sel  = new StringSelection(excelStr.toString()); 
                CLIPBOARD.setContents(sel, sel); 
        } 
        
        private void pasteFromClipboard() { 
                int startRow=table.getSelectedRows()[0]; 
                int startCol=table.getSelectedColumns()[0];

                String pasteString = ""; 
                try { 
                        pasteString = (String)(CLIPBOARD.getContents(this).getTransferData(DataFlavor.stringFlavor)); 
                } catch (Exception e) { 
                        JOptionPane.showMessageDialog(null, "Invalid Paste Type", "Invalid Paste Type", JOptionPane.ERROR_MESSAGE);
                        return; 
                } 
                
                String[] lines = pasteString.split(LINE_BREAK); 
                for (int i=0 ; i<lines.length; i++) { 
                        String[] cells = lines[i].split(CELL_BREAK); 
                        for (int j=0 ; j<cells.length; j++) { 
                                if (table.getRowCount()>startRow+i && table.getColumnCount()>startCol+j) { 
                                        table.setValueAt(cells[j], startRow+i, startCol+j); 
                                } 
                        } 
                } 
        } 
        
        private void cancelEditing() { 
                if (table.getCellEditor() != null) { 
                        table.getCellEditor().cancelCellEditing(); 
            } 
        } 
        
        private String escape(Object cell) { 
                return cell.toString().replace(LINE_BREAK, " ").replace(CELL_BREAK, " "); 
        } 
} 