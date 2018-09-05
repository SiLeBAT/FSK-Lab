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

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class TableWithOverwrite extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6782674430592418376L;

	public final static String EXCLUDE = "F2";
	
	private boolean isBlankEditor = false;
	
	public TableWithOverwrite() {
		super();
	}
	
	public TableWithOverwrite(final TableModel tm) {
		super(tm);
	}
	
	public TableWithOverwrite(final Object[][] o, final String[] s) {
		super(o, s);
	}
	
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
		if (! EXCLUDE.equals(KeyEvent.getKeyText(e.getKeyCode()))) {
			isBlankEditor = true;
		}
		
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
		
		isBlankEditor = false;
		return retValue;
	}
}
