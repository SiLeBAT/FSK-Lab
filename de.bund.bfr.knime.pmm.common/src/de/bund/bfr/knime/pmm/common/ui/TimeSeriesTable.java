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
package de.bund.bfr.knime.pmm.common.ui;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

public class TimeSeriesTable extends JTable implements ActionListener {

	private static final long serialVersionUID = 1L;

	private int rowCount;
	private String timeColumnName;
	private List<String> cColumnNames;

	public TimeSeriesTable(List<TimeSeriesXml> timeSeries,
			boolean timeEditable, boolean logcEditable) {
		this(timeSeries.size(), 1, timeEditable, logcEditable);

		for (int i = 0; i < timeSeries.size(); i++) {
			setTime(i, timeSeries.get(i).time);
			setLogc(i, timeSeries.get(i).concentration);
		}
	}

	public TimeSeriesTable(int rowCount, int cColumnCount,
			boolean timeEditable, boolean logcEditable) {
		this.rowCount = rowCount;
		timeColumnName = AttributeUtilities.getName(AttributeUtilities.TIME);
		cColumnNames = new ArrayList<>();

		for (int i = 0; i < cColumnCount; i++) {
			String name = AttributeUtilities
					.getName(AttributeUtilities.CONCENTRATION);

			if (cColumnCount > 1) {
				name += " " + (i + 1);
			}

			cColumnNames.add(name);
		}

		setModel(new TimeSeriesTableModel(rowCount, cColumnCount));
		getTimeColumn().setCellEditor(new DoubleCellEditor(timeEditable));
		getTimeColumn().setCellRenderer(new DoubleCellRenderer());

		for (TableColumn column : getCColumns()) {
			column.setCellEditor(new DoubleCellEditor(logcEditable));
			column.setCellRenderer(new DoubleCellRenderer());
		}

		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setCellSelectionEnabled(true);
		registerKeyboardAction(this, "Copy", KeyStroke.getKeyStroke(
				KeyEvent.VK_C, ActionEvent.CTRL_MASK, false),
				JComponent.WHEN_FOCUSED);
		registerKeyboardAction(this, "Paste", KeyStroke.getKeyStroke(
				KeyEvent.VK_V, ActionEvent.CTRL_MASK, false),
				JComponent.WHEN_FOCUSED);
	}

	public String getTimeColumnName() {
		return timeColumnName;
	}

	public void setTimeColumnName(String timeColumnName) {
		getColumnModel().getColumn(0).setHeaderValue(timeColumnName);
		getTableHeader().repaint();
		this.timeColumnName = timeColumnName;
	}

	public List<String> getCColumnNames() {
		return cColumnNames;
	}

	public void setCColumnName(int i, String cColumnName) {
		getColumnModel().getColumn(i + 1).setHeaderValue(cColumnName);
		getTableHeader().repaint();
		cColumnNames.set(i, cColumnName);
	}

	public TableColumn getTimeColumn() {
		return getColumn(getTimeColumnName());
	}

	public List<TableColumn> getCColumns() {
		List<TableColumn> columns = new ArrayList<>();

		for (String name : getCColumnNames()) {
			columns.add(getColumn(name));
		}

		return columns;
	}

	public List<TimeSeriesXml> getTimeSeries() {
		List<TimeSeriesXml> timeSeries = new ArrayList<>();

		for (int i = 0; i < rowCount; i++) {
			timeSeries.add(new TimeSeriesXml(null, getTime(i), null,
					getLogc(i), null, null, null));
		}

		return timeSeries;
	}

	public Double getTime(int row) {
		return (Double) getModel().getValueAt(row, 0);
	}

	public void setTime(int row, Double time) {
		getModel().setValueAt(time, row, 0);
	}

	public Double getLogc(int row) {
		return (Double) getModel().getValueAt(row, 1);
	}

	public void setLogc(int row, Double logc) {
		if (!cColumnNames.isEmpty()) {
			getModel().setValueAt(logc, row, 1);
		}
	}

	public void setLogc(int row, int cColumn, Double logc) {
		getModel().setValueAt(logc, row, cColumn + 1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Copy")) {
			performCopy();
		} else if (e.getActionCommand().equals("Paste")) {
			performPaste();
		}
	}

	private void performCopy() {
		StringBuilder sbf = new StringBuilder();
		int numcols = getSelectedColumnCount();
		int numrows = getSelectedRowCount();
		int[] rowsselected = getSelectedRows();
		int[] colsselected = getSelectedColumns();

		for (int i = 0; i < numrows; i++) {
			for (int j = 0; j < numcols; j++) {
				sbf.append(getValueAt(rowsselected[i], colsselected[j]));

				if (j < numcols - 1) {
					sbf.append("\t");
				}
			}
			sbf.append("\n");
		}

		StringSelection stsel = new StringSelection(sbf.toString());

		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(stsel, stsel);
	}

	private void performPaste() {
		int startRow = getSelectedRows()[0];
		int startCol = getSelectedColumns()[0];
		Clipboard system = Toolkit.getDefaultToolkit().getSystemClipboard();
		String trstring = null;

		try {
			trstring = (String) system.getContents(this).getTransferData(
					DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] rows = trstring.split("\n");

		for (int i = 0; i < rows.length; i++) {
			String[] cells = rows[i].split("\t");

			for (int j = 0; j < cells.length; j++) {
				if (startRow + i < getRowCount()
						&& startCol + j < getColumnCount()) {
					try {
						setValueAt(
								Double.parseDouble(cells[j].replace(",", ".")),
								startRow + i, startCol + j);
					} catch (NumberFormatException e) {
					}
				}
			}
		}

		repaint();
	}

	private class TimeSeriesTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private int rowCount;
		private int cColumnCount;

		private List<Double> timeList;
		private List<List<Double>> concentrationLists;

		public TimeSeriesTableModel(int rowCount, int cColumnCount) {
			this.rowCount = rowCount;
			this.cColumnCount = cColumnCount;
			timeList = new ArrayList<>(rowCount);
			concentrationLists = new ArrayList<>();

			for (int i = 0; i < cColumnCount; i++) {
				concentrationLists.add(new ArrayList<Double>());
			}

			for (int i = 0; i < rowCount; i++) {
				timeList.add(null);

				for (int j = 0; j < cColumnCount; j++) {
					concentrationLists.get(j).add(null);
				}
			}
		}

		@Override
		public int getRowCount() {
			return rowCount;
		}

		@Override
		public int getColumnCount() {
			return cColumnCount + 1;
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return timeColumnName;
			default:
				return cColumnNames.get(column - 1);
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return Double.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return timeList.get(rowIndex);
			default:
				return concentrationLists.get(columnIndex - 1).get(rowIndex);
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				timeList.set(rowIndex, (Double) aValue);
				break;
			default:
				concentrationLists.get(columnIndex - 1).set(rowIndex,
						(Double) aValue);
				break;
			}
		}

	}

	private class DoubleCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		protected void setValue(Object value) {
			if (value != null) {
				NumberFormat format = new DecimalFormat("0.####",
						new DecimalFormatSymbols(Locale.US));

				setText(format.format((Double) value));
			} else {
				super.setValue(value);
			}
		}

	}

	private class DoubleCellEditor extends AbstractCellEditor implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		private boolean editable;

		private DoubleTextField field;

		public DoubleCellEditor(boolean editable) {
			this.editable = editable;
			field = new DoubleTextField(true);
		}

		@Override
		public Object getCellEditorValue() {
			return field.getValue();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (value != null) {
				field.setText(value.toString());
			} else {
				field.setText("");
			}

			return field;
		}

		@Override
		public boolean isCellEditable(EventObject e) {
			if (!editable) {
				return false;
			}

			if (e instanceof MouseEvent) {
				return ((MouseEvent) e).getClickCount() >= 2;
			}

			return true;
		}
	}

}
