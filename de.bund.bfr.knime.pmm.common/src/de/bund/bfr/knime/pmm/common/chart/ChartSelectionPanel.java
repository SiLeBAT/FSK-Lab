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
package de.bund.bfr.knime.pmm.common.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.ui.FormattedDoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.SpacePanel;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;
import de.bund.bfr.knime.pmm.common.ui.TimeSeriesDialog;
import de.bund.bfr.knime.pmm.common.ui.UI;

public class ChartSelectionPanel extends JPanel implements ActionListener,
		CellEditorListener, ListSelectionListener {

	private static final String ID = "ID";
	private static final String SELECTED = "Selected";
	public static final String COLOR = "Color";
	public static final String SHAPE = "Shape";
	public static final String DATA = "Data Points";
	public static final String PARAMETERS = "Parameters";
	public static final String VARIABLES = "Variables";
	public static final String FORMULA = "Math Formula";

	private static final long serialVersionUID = 1L;

	private static final int MIN_COLUMN_WIDTH = 15;
	private static final int MAX_COLUMN_WIDTH = 2147483647;
	private static final int PREFERRED_COLUMN_WIDTH = 75;

	private List<SelectionListener> listeners;

	private ColorAndShapeCreator colorAndShapes;

	private List<String> invalidIds;

	private List<String> ids;
	private boolean selectionExlusive;
	private List<List<TimeSeriesXml>> data;
	private List<String> formulas;
	private List<Map<String, Double>> parameters;
	private List<Map<String, String>> variables;
	private Map<String, List<String>> stringColumns;
	private Map<String, List<Double>> qualityColumns;
	private List<String> conditions;
	private List<List<Double>> conditionValues;
	private List<List<Double>> conditionMinValues;
	private List<List<Double>> conditionMaxValues;
	private List<List<String>> conditionUnits;

	private List<String> conditionStandardUnits;
	private List<String> visualizationColumns;
	private List<String> miscellaneousColumns;

	private JTable selectTable;
	private JComponent optionsPanel;

	private JScrollPane tableScrollPane;
	private JButton selectAllButton;
	private JButton unselectAllButton;
	private JButton invertSelectionButton;
	private JButton customizeColumnsButton;
	private JButton resizeColumnsButton;
	private Map<String, JComboBox<String>> comboBoxes;

	private boolean hasConditionRanges;

	public ChartSelectionPanel(List<String> ids, boolean selectionsExclusive,
			Map<String, List<String>> stringColumns,
			Map<String, List<Double>> qualityColumns, List<String> conditions,
			List<List<Double>> conditionValues,
			List<List<Double>> conditionMinValues,
			List<List<Double>> conditionMaxValues,
			List<List<String>> conditionUnits, List<String> visibleColumns,
			List<String> filterableColumns, List<List<TimeSeriesXml>> data,
			List<Map<String, Double>> parameters,
			List<Map<String, String>> variables, List<String> formulas) {
		
		this(ids, selectionsExclusive, stringColumns, qualityColumns,
				conditions, conditionValues, conditionMinValues,
				conditionMaxValues, conditionUnits, visibleColumns,
				filterableColumns, data, parameters, variables, formulas, null);
	}

	public ChartSelectionPanel(List<String> ids, boolean selectionsExclusive,
			Map<String, List<String>> stringColumns,
			Map<String, List<Double>> qualityColumns, List<String> conditions,
			List<List<Double>> conditionValues,
			List<List<Double>> conditionMinValues,
			List<List<Double>> conditionMaxValues,
			List<List<String>> conditionUnits, List<String> visibleColumns,
			List<String> filterableStringColumns,
			List<List<TimeSeriesXml>> data,
			List<Map<String, Double>> parameters,
			List<Map<String, String>> variables, List<String> formulas,
			List<Integer> colorCounts) {
		if (stringColumns == null) {
			stringColumns = new LinkedHashMap<>();
		}

		if (qualityColumns == null) {
			qualityColumns = new LinkedHashMap<>();
		}

		if (conditions == null) {
			conditions = new ArrayList<>();
		}

		this.ids = ids;
		this.selectionExlusive = selectionsExclusive;
		this.data = data;
		this.formulas = formulas;
		this.parameters = parameters;
		this.variables = variables;
		this.stringColumns = stringColumns;
		this.qualityColumns = qualityColumns;
		this.conditions = conditions;
		this.conditionValues = conditionValues;
		this.conditionMinValues = conditionMinValues;
		this.conditionMaxValues = conditionMaxValues;
		this.conditionUnits = conditionUnits;

		invalidIds = new ArrayList<>();
		conditionStandardUnits = new ArrayList<>();

		for (int i = 0; i < conditions.size(); i++) {
			String standardUnit = null;
			boolean multipleUnits = false;

			for (String unit : conditionUnits.get(i)) {
				if (standardUnit == null) {
					standardUnit = unit;
				} else if (unit == null || unit.equals(standardUnit)) {
					// Do nothing
				} else {
					standardUnit = null;
					multipleUnits = true;
					break;
				}
			}

			if (!multipleUnits) {
				conditionStandardUnits.add(standardUnit);
			} else {
				conditionStandardUnits = null;
				break;
			}
		}

		visualizationColumns = Arrays.asList(COLOR, SHAPE);
		miscellaneousColumns = new ArrayList<>();

		if (data != null) {
			miscellaneousColumns.add(DATA);
		}

		if (formulas != null) {
			miscellaneousColumns.add(FORMULA);
		}

		if (parameters != null) {
			miscellaneousColumns.add(PARAMETERS);
		}

		if (variables != null) {
			miscellaneousColumns.add(VARIABLES);
		}

		miscellaneousColumns.addAll(stringColumns.keySet());

		if (conditionValues == null && conditionMinValues != null
				&& conditionMaxValues != null) {
			hasConditionRanges = true;
		} else {
			hasConditionRanges = false;
		}

		listeners = new ArrayList<>();

		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));

		if (!filterableStringColumns.isEmpty()) {
			JPanel upperPanel1 = new JPanel();
			JPanel filterPanel = new JPanel();

			upperPanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
			filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			comboBoxes = new LinkedHashMap<>();

			for (String column : filterableStringColumns) {
				List<String> values = new ArrayList<>();
				Set<String> valueSet = new LinkedHashSet<>(
						stringColumns.get(column));

				valueSet.remove(null);
				values.add("");
				values.addAll(valueSet);
				Collections.sort(values);

				JComboBox<String> box = new JComboBox<>(
						values.toArray(new String[0]));

				box.addActionListener(this);
				filterPanel.add(new JLabel(column + ":"));
				filterPanel.add(box);
				comboBoxes.put(column, box);
			}

			upperPanel1.add(filterPanel);
			upperPanel.add(new SpacePanel(upperPanel1));
		}

		JPanel upperPanel2 = new JPanel();

		upperPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		if (!selectionsExclusive) {
			JPanel selectPanel = new JPanel();

			selectAllButton = new JButton("All");
			selectAllButton.addActionListener(this);
			unselectAllButton = new JButton("None");
			unselectAllButton.addActionListener(this);
			invertSelectionButton = new JButton("Invert");
			invertSelectionButton.addActionListener(this);

			selectPanel.setBorder(BorderFactory.createTitledBorder("Select"));
			selectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			selectPanel.add(selectAllButton);
			selectPanel.add(unselectAllButton);
			selectPanel.add(invertSelectionButton);
			upperPanel2.add(selectPanel);
		}

		JPanel columnPanel = new JPanel();

		customizeColumnsButton = new JButton("Customize");
		customizeColumnsButton.addActionListener(this);
		resizeColumnsButton = new JButton("Set Optimal Width");
		resizeColumnsButton.addActionListener(this);
		columnPanel.setBorder(BorderFactory.createTitledBorder("Columns"));
		columnPanel.add(customizeColumnsButton);
		columnPanel.add(resizeColumnsButton);

		upperPanel2.add(columnPanel);
		upperPanel.add(new SpacePanel(upperPanel2));

		SelectTableModel model;

		if (colorCounts == null) {
			colorAndShapes = new ColorAndShapeCreator(ids.size());
			model = new SelectTableModel(colorAndShapes.getColorList(),
					colorAndShapes.getShapeNameList(), false);
		} else {
			List<List<Color>> colorLists = new ArrayList<>();
			List<List<String>> shapeLists = new ArrayList<>();

			colorAndShapes = new ColorAndShapeCreator(
					Collections.max(colorCounts));

			for (int n : colorCounts) {
				ArrayList<Color> colors = new ArrayList<>();
				ArrayList<String> shapes = new ArrayList<>();

				for (int i = 0; i < n; i++) {
					colors.add(colorAndShapes.getColorList().get(i));
					shapes.add(colorAndShapes.getShapeNameList().get(i));
				}

				colorLists.add(colors);
				shapeLists.add(shapes);
			}

			model = new SelectTableModel(colorLists, shapeLists, true);
		}

		selectTable = new JTable(model);
		selectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectTable.getSelectionModel().addListSelectionListener(this);
		selectTable.setRowHeight((new JComboBox<String>()).getPreferredSize().height);
		selectTable.setRowSorter(new SelectTableRowSorter(model, null));
		selectTable.getColumn(ID).setMinWidth(0);
		selectTable.getColumn(ID).setMaxWidth(0);
		selectTable.getColumn(ID).setPreferredWidth(0);
		selectTable.getColumn(SELECTED).setCellEditor(new CheckBoxEditor());
		selectTable.getColumn(SELECTED).setCellRenderer(new CheckBoxRenderer());
		selectTable.getColumn(SELECTED).getCellEditor()
				.addCellEditorListener(this);

		if (colorCounts == null) {
			selectTable.getColumn(COLOR).setCellEditor(new ColorEditor());
			selectTable.getColumn(COLOR).setCellRenderer(new ColorRenderer());
			selectTable.getColumn(SHAPE).setCellEditor(
					new DefaultCellEditor(new JComboBox<>(
							ColorAndShapeCreator.SHAPE_NAMES)));
		} else {
			selectTable.getColumn(COLOR).setCellEditor(new ColorListEditor());
			selectTable.getColumn(COLOR).setCellRenderer(
					new ColorListRenderer());
			selectTable.getColumn(SHAPE).setCellEditor(new ShapeListEditor());
			selectTable.getColumn(SHAPE).setCellRenderer(
					new ShapeListRenderer());
		}

		selectTable.getColumn(COLOR).getCellEditor()
				.addCellEditorListener(this);
		selectTable.getColumn(SHAPE).getCellEditor()
				.addCellEditorListener(this);
		selectTable.getColumn(DATA).setCellEditor(new TimeSeriesEditor());
		selectTable.getColumn(DATA).setCellRenderer(new ViewRenderer());
		selectTable.getColumn(FORMULA).setCellEditor(new FormulaEditor());
		selectTable.getColumn(FORMULA).setCellRenderer(new ViewRenderer());
		selectTable.getColumn(PARAMETERS).setCellEditor(new ParameterEditor());
		selectTable.getColumn(PARAMETERS).setCellRenderer(new ViewRenderer());
		selectTable.getColumn(VARIABLES).setCellEditor(new VariableEditor());
		selectTable.getColumn(VARIABLES).setCellRenderer(new ViewRenderer());
		selectTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		applyColumnSelection(visibleColumns);

		optionsPanel = new SpacePanel(upperPanel);
		tableScrollPane = new JScrollPane(selectTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		setLayout(new BorderLayout());
		add(optionsPanel, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
	}

	public void selectFirstRow() {
		selectTable.getSelectionModel().setSelectionInterval(0, 0);
	}

	public String getFocusedID() {
		int row = selectTable.getSelectedRow();

		if (row != -1) {
			return (String) selectTable.getValueAt(row, 0);
		}

		return null;
	}

	public void setInvalidIds(List<String> invalidIds) {
		this.invalidIds = invalidIds;
	}

	public List<String> getSelectedIDs() {
		List<String> selectedIDs = new ArrayList<>();

		for (int i = 0; i < selectTable.getRowCount(); i++) {
			if ((Boolean) selectTable.getValueAt(i, 1)) {
				selectedIDs.add((String) selectTable.getValueAt(i, 0));
			}
		}

		return selectedIDs;
	}

	public void setSelectedIDs(List<String> selectedIDs) {
		Set<String> idSet = new LinkedHashSet<>(selectedIDs);

		for (int i = 0; i < selectTable.getRowCount(); i++) {
			if (idSet.contains(selectTable.getValueAt(i, 0))) {
				selectTable.setValueAt(true, i, 1);
			} else {
				selectTable.setValueAt(false, i, 1);
			}
		}

		fireSelectionChanged();
	}

	public String getFilter(String column) {
		if (comboBoxes.containsKey(column)) {
			return (String) comboBoxes.get(column).getSelectedItem();
		} else {
			return null;
		}
	}

	public void setFilter(String column, String filter) {
		if (comboBoxes.containsKey(column)) {
			if (filter != null) {
				comboBoxes.get(column).setSelectedItem(filter);
			} else {
				comboBoxes.get(column).setSelectedItem("");
			}

			applyFilters();
		}
	}

	public Map<String, Color> getColors() {
		Map<String, Color> paints = new LinkedHashMap<>(
				selectTable.getRowCount());

		for (int i = 0; i < selectTable.getRowCount(); i++) {
			paints.put((String) selectTable.getValueAt(i, 0),
					(Color) selectTable.getValueAt(i, 2));
		}

		return paints;
	}

	public void setColors(Map<String, Color> colors) {
		for (int i = 0; i < selectTable.getRowCount(); i++) {
			Color color = colors.get(selectTable.getValueAt(i, 0));

			if (color != null) {
				selectTable.setValueAt(color, i, 2);
			}
		}
	}

	public Map<String, Shape> getShapes() {
		Map<String, Shape> shapes = new LinkedHashMap<>(
				selectTable.getRowCount());
		Map<String, Shape> shapeMap = colorAndShapes.getShapeByNameMap();

		for (int i = 0; i < selectTable.getRowCount(); i++) {
			shapes.put((String) selectTable.getValueAt(i, 0),
					shapeMap.get(selectTable.getValueAt(i, 3)));
		}

		return shapes;
	}

	public void setShapes(Map<String, Shape> shapes) {
		Map<Shape, String> shapeMap = colorAndShapes.getNameByShapeMap();

		for (int i = 0; i < selectTable.getRowCount(); i++) {
			Shape shape = shapes.get(selectTable.getValueAt(i, 0));

			if (shape != null) {
				selectTable.setValueAt(shapeMap.get(shape), i, 3);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<Color>> getColorLists() {
		Map<String, List<Color>> paints = new LinkedHashMap<>(
				selectTable.getRowCount());

		for (int i = 0; i < selectTable.getRowCount(); i++) {
			paints.put((String) selectTable.getValueAt(i, 0),
					(List<Color>) selectTable.getValueAt(i, 2));
		}

		return paints;
	}

	public void setColorLists(Map<String, List<Color>> colorLists) {
		for (int i = 0; i < selectTable.getRowCount(); i++) {
			List<Color> colorList = colorLists
					.get(selectTable.getValueAt(i, 0));

			if (colorList != null) {
				selectTable.setValueAt(colorList, i, 2);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<Shape>> getShapeLists() {
		Map<String, List<Shape>> shapes = new LinkedHashMap<>(
				selectTable.getRowCount());
		Map<String, Shape> shapeMap = colorAndShapes.getShapeByNameMap();

		for (int i = 0; i < selectTable.getRowCount(); i++) {
			List<Shape> list = new ArrayList<>();

			for (String name : (List<String>) selectTable.getValueAt(i, 3)) {
				list.add(shapeMap.get(name));
			}

			shapes.put((String) selectTable.getValueAt(i, 0), list);
		}

		return shapes;
	}

	public void setShapeLists(Map<String, List<Shape>> shapeLists) {
		Map<Shape, String> shapeMap = colorAndShapes.getNameByShapeMap();

		for (int i = 0; i < selectTable.getRowCount(); i++) {
			List<Shape> shapeList = shapeLists
					.get(selectTable.getValueAt(i, 0));

			if (shapeList != null) {
				List<String> list = new ArrayList<>();

				for (Shape shape : shapeList) {
					list.add(shapeMap.get(shape));
				}

				selectTable.setValueAt(list, i, 3);
			}
		}
	}

	public List<String> getVisibleColumns() {
		List<String> visibleColumns = new ArrayList<>();
		List<String> columns = new ArrayList<>();

		columns.addAll(Arrays.asList(COLOR, SHAPE, DATA, FORMULA, PARAMETERS,
				VARIABLES));
		columns.addAll(stringColumns.keySet());
		columns.addAll(qualityColumns.keySet());

		for (String column : columns) {
			if (selectTable.getColumn(column).getMaxWidth() != 0) {
				visibleColumns.add(column);
			}
		}

		for (int i = 0; i < conditions.size(); i++) {
			String column = conditions.get(i);

			if (conditionStandardUnits != null) {
				String unit = conditionStandardUnits.get(i);

				if (!hasConditionRanges
						&& selectTable.getColumn(column + " (" + unit + ")")
								.getMaxWidth() != 0) {
					visibleColumns.add(column);
				} else if (hasConditionRanges
						&& selectTable.getColumn(
								"Min " + column + " (" + unit + ")")
								.getMaxWidth() != 0) {
					visibleColumns.add(column);
				}
			} else {
				if (!hasConditionRanges
						&& selectTable.getColumn(column).getMaxWidth() != 0) {
					visibleColumns.add(column);
				} else if (hasConditionRanges
						&& selectTable.getColumn("Min " + column).getMaxWidth() != 0) {
					visibleColumns.add(column);
				}
			}
		}

		return visibleColumns;
	}

	public Map<String, Integer> getColumnWidths() {
		Map<String, Integer> widths = new LinkedHashMap<>();
		Enumeration<TableColumn> columns = selectTable.getColumnModel()
				.getColumns();

		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();

			if (column.getWidth() != 0) {
				widths.put((String) column.getHeaderValue(), column.getWidth());
			}
		}

		return widths;
	}

	public void setColumnWidths(Map<String, Integer> widths) {
		Enumeration<TableColumn> columns = selectTable.getColumnModel()
				.getColumns();

		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();

			if (widths.containsKey(column.getHeaderValue())) {
				column.setPreferredWidth(widths.get(column.getHeaderValue()));
			}
		}
	}

	public void addSelectionListener(SelectionListener listener) {
		listeners.add(listener);
	}

	public void removeSelectionListener(SelectionListener listener) {
		listeners.remove(listener);
	}

	public void fireSelectionChanged() {
		for (SelectionListener listener : listeners) {
			listener.selectionChanged();
		}
	}

	public void fireInfoSelectionChanged() {
		for (SelectionListener listener : listeners) {
			listener.focusChanged();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectAllButton) {
			for (int i = 0; i < selectTable.getRowCount(); i++) {
				selectTable.setValueAt(true, i, 1);
			}
		} else if (e.getSource() == unselectAllButton) {
			for (int i = 0; i < selectTable.getRowCount(); i++) {
				selectTable.setValueAt(false, i, 1);
			}
		} else if (e.getSource() == invertSelectionButton) {
			for (int i = 0; i < selectTable.getRowCount(); i++) {
				selectTable.setValueAt(!(Boolean) selectTable.getValueAt(i, 1),
						i, 1);
			}
		} else if (e.getSource() == customizeColumnsButton) {
			ColumnSelectionDialog dialog = new ColumnSelectionDialog(
					getVisibleColumns());

			dialog.setVisible(true);

			if (dialog.isApproved()) {
				applyColumnSelection(dialog.getSelection());
			}
		} else if (e.getSource() == resizeColumnsButton) {
			packColumns();
		} else {
			applyFilters();
		}

		fireSelectionChanged();
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		fireSelectionChanged();
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		fireInfoSelectionChanged();
	}

	private void applyFilters() {
		Map<String, String> filters = new LinkedHashMap<>();

		for (String column : comboBoxes.keySet()) {
			JComboBox<String> box = comboBoxes.get(column);

			if (!box.getSelectedItem().equals("")) {
				filters.put(column, (String) box.getSelectedItem());
			}
		}

		selectTable.setRowSorter(new SelectTableRowSorter(
				(SelectTableModel) selectTable.getModel(), filters));
	}

	private void applyColumnSelection(List<String> visibleColumns) {
		List<String> columns = new ArrayList<>();

		columns.addAll(Arrays.asList(COLOR, SHAPE, DATA, FORMULA, PARAMETERS,
				VARIABLES));
		columns.addAll(stringColumns.keySet());
		columns.addAll(qualityColumns.keySet());

		for (String column : columns) {
			setColumnVisible(column, visibleColumns.contains(column));
		}

		if (!hasConditionRanges) {
			for (int i = 0; i < conditions.size(); i++) {
				String column = conditions.get(i);

				if (conditionStandardUnits != null) {
					String unit = conditionStandardUnits.get(i);

					setColumnVisible(column + " (" + unit + ")",
							visibleColumns.contains(column));
				} else {
					setColumnVisible(column, visibleColumns.contains(column));
					setColumnVisible(column + " Unit",
							visibleColumns.contains(column));
				}
			}
		} else {
			for (int i = 0; i < conditions.size(); i++) {
				String column = conditions.get(i);

				if (conditionStandardUnits != null) {
					String unit = conditionStandardUnits.get(i);

					setColumnVisible("Min " + column + " (" + unit + ")",
							visibleColumns.contains(column));
					setColumnVisible("Max " + column + " (" + unit + ")",
							visibleColumns.contains(column));
				} else {
					setColumnVisible("Min " + column,
							visibleColumns.contains(column));
					setColumnVisible("Max " + column,
							visibleColumns.contains(column));
					setColumnVisible(column + " Unit",
							visibleColumns.contains(column));
				}
			}
		}
	}

	private void setColumnVisible(String column, boolean value) {
		if (value) {
			selectTable.getColumn(column).setMinWidth(MIN_COLUMN_WIDTH);
			selectTable.getColumn(column).setMaxWidth(MAX_COLUMN_WIDTH);

			if (selectTable.getColumn(column).getPreferredWidth() == 0) {
				selectTable.getColumn(column).setPreferredWidth(
						PREFERRED_COLUMN_WIDTH);
			}
		} else {
			selectTable.getColumn(column).setMinWidth(0);
			selectTable.getColumn(column).setMaxWidth(0);
			selectTable.getColumn(column).setPreferredWidth(0);
		}
	}

	private void packColumns() {

		for (int c = 0; c < selectTable.getColumnCount(); c++) {
			TableColumn col = selectTable.getColumnModel().getColumn(c);

			if (col.getPreferredWidth() == 0) {
				continue;
			}

			TableCellRenderer renderer = col.getHeaderRenderer();
			Component comp = selectTable
					.getTableHeader()
					.getDefaultRenderer()
					.getTableCellRendererComponent(selectTable,
							col.getHeaderValue(), false, false, 0, 0);
			int width = comp.getPreferredSize().width;

			for (int r = 0; r < selectTable.getRowCount(); r++) {
				renderer = selectTable.getCellRenderer(r, c);
				comp = renderer.getTableCellRendererComponent(selectTable,
						selectTable.getValueAt(r, c), false, false, r, c);
				width = Math.max(width, comp.getPreferredSize().width);
			}

			col.setPreferredWidth(width += 5);
		}

		revalidate();
	}

	public static interface SelectionListener {

		public void selectionChanged();

		public void focusChanged();
	}

	private abstract class AbstractSelectTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private boolean listBased;

		private List<Boolean> selections;
		private List<Color> colors;
		private List<List<Color>> colorLists;
		private List<String> shapes;
		private List<List<String>> shapeLists;

		@SuppressWarnings("unchecked")
		public AbstractSelectTableModel(List<?> colors, List<?> shapes,
				boolean listBased) {
			this.listBased = listBased;

			if (!listBased) {
				this.colors = (List<Color>) colors;
				this.shapes = (List<String>) shapes;
			} else {
				this.colorLists = (List<List<Color>>) colors;
				this.shapeLists = (List<List<String>>) shapes;
			}

			selections = new ArrayList<>(Collections.nCopies(ids.size(), false));
		}

		@Override
		public int getColumnCount() {
			int conditionCount = conditions.size();

			if (!hasConditionRanges) {
				conditionCount *= 2;
			} else {
				conditionCount *= 3;
			}

			if (conditionStandardUnits != null) {
				conditionCount -= conditions.size();
			}

			return 8 + stringColumns.size() + qualityColumns.size()
					+ conditionCount;
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return ID;
			case 1:
				return SELECTED;
			case 2:
				return COLOR;
			case 3:
				return SHAPE;
			case 4:
				return DATA;
			case 5:
				return FORMULA;
			case 6:
				return PARAMETERS;
			case 7:
				return VARIABLES;
			default:
				int i1 = column - 8;
				int i2 = i1 - stringColumns.size();
				int i3 = i2 - qualityColumns.size();

				if (i1 < stringColumns.size()) {
					return new ArrayList<>(stringColumns.keySet()).get(i1);
				} else if (i2 < qualityColumns.size()) {
					return new ArrayList<>(qualityColumns.keySet()).get(i2);
				} else if (!hasConditionRanges) {
					if (conditionStandardUnits != null) {
						return conditions.get(i3) + " ("
								+ conditionStandardUnits.get(i3) + ")";
					} else {
						if (i3 % 2 == 0) {
							return conditions.get(i3 / 2);
						} else {
							return conditions.get(i3 / 2) + " Unit";
						}
					}
				} else {
					if (conditionStandardUnits != null) {
						if (i3 % 2 == 0) {
							return "Min " + conditions.get(i3 / 2) + " ("
									+ conditionStandardUnits.get(i3 / 2) + ")";
						} else {
							return "Max " + conditions.get(i3 / 2) + " ("
									+ conditionStandardUnits.get(i3 / 2) + ")";
						}
					} else {
						if (i3 % 3 == 0) {
							return "Min " + conditions.get(i3 / 3);
						} else if (i3 % 3 == 1) {
							return "Max " + conditions.get(i3 / 3);
						} else {
							return conditions.get(i3 / 3) + " Unit";
						}
					}
				}
			}
		}

		@Override
		public int getRowCount() {
			return ids.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			switch (column) {
			case 0:
				return ids.get(row);
			case 1:
				return selections.get(row);
			case 2:
				if (!listBased) {
					return colors.get(row);
				} else {
					return colorLists.get(row);
				}
			case 3:
				if (!listBased) {
					return shapes.get(row);
				} else {
					return shapeLists.get(row);
				}
			case 4:
				return data != null ? data.get(row) : null;
			case 5:
				return formulas != null ? formulas.get(row) : null;
			case 6:
				return parameters != null ? parameters.get(row) : null;
			case 7:
				return variables != null ? variables.get(row) : null;
			default:
				int i1 = column - 8;
				int i2 = i1 - stringColumns.size();
				int i3 = i2 - qualityColumns.size();

				if (i1 < stringColumns.size()) {
					return new ArrayList<>(stringColumns.values()).get(i1).get(
							row);
				} else if (i2 < qualityColumns.size()) {
					return new ArrayList<>(qualityColumns.values()).get(i2)
							.get(row);
				} else if (!hasConditionRanges) {
					if (conditionStandardUnits != null) {
						return conditionValues.get(i3).get(row);
					} else {
						if (i3 % 2 == 0) {
							return conditionValues.get(i3 / 2).get(row);
						} else {
							return conditionUnits.get(i3 / 2).get(row);
						}
					}
				} else {
					if (conditionStandardUnits != null) {
						if (i3 % 2 == 0) {
							return conditionMinValues.get(i3 / 2).get(row);
						} else {
							return conditionMaxValues.get(i3 / 2).get(row);
						}
					} else {
						if (i3 % 3 == 0) {
							return conditionMinValues.get(i3 / 3).get(row);
						} else if (i3 % 3 == 1) {
							return conditionMaxValues.get(i3 / 3).get(row);
						} else {
							return conditionUnits.get(i3 / 3).get(row);
						}
					}
				}
			}
		}

		@Override
		public Class<?> getColumnClass(int column) {
			switch (column) {
			case 0:
				return String.class;
			case 1:
				return Boolean.class;
			case 2:
				if (!listBased) {
					return Color.class;
				} else {
					return List.class;
				}
			case 3:
				if (!listBased) {
					return String.class;
				} else {
					return List.class;
				}
			case 4:
				return List.class;
			case 5:
				return String.class;
			case 6:
				return Map.class;
			case 7:
				return Map.class;
			default:
				int i1 = column - 8;
				int i2 = i1 - stringColumns.size();
				int i3 = i2 - qualityColumns.size();

				if (i1 < stringColumns.size()) {
					return String.class;
				} else if (i2 < qualityColumns.size()) {
					return Double.class;
				} else if (!hasConditionRanges) {
					if (conditionStandardUnits != null) {
						return Double.class;
					} else {
						if (i3 % 2 == 0) {
							return Double.class;
						} else {
							return String.class;
						}
					}
				} else {
					if (conditionStandardUnits != null) {
						if (i3 % 2 == 0) {
							return Double.class;
						} else {
							return Double.class;
						}
					} else {
						if (i3 % 3 == 0) {
							return Double.class;
						} else if (i3 % 3 == 1) {
							return Double.class;
						} else {
							return String.class;
						}
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValueAt(Object value, int row, int column) {
			switch (column) {
			case 1:
				selections.set(row, (Boolean) value);
				break;
			case 2:
				if (!listBased) {
					colors.set(row, (Color) value);
				} else {
					colorLists.set(row, (List<Color>) value);
				}
				break;
			case 3:
				if (!listBased) {
					shapes.set(row, (String) value);
				} else {
					shapeLists.set(row, (List<String>) value);
				}
				break;
			default:
				// Do nothing
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 1 || column == 2 || column == 3 || column == 4
					|| column == 5 || column == 6 || column == 7;
		}
	}

	private class SelectTableModel extends AbstractSelectTableModel {

		private static final long serialVersionUID = 1L;

		public SelectTableModel(List<?> colors, List<?> shapes,
				boolean listBased) {
			super(colors, shapes, listBased);
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			super.setValueAt(value, row, column);

			if (selectionExlusive && column == 1 && value.equals(Boolean.TRUE)) {
				for (int i = 0; i < getRowCount(); i++) {
					if (i != row) {
						super.setValueAt(false, i, 1);
						fireTableCellUpdated(i, 1);
					}
				}
			}

			fireTableCellUpdated(row, column);
		}
	}

	private class ViewRenderer implements TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return new JButton("View");
		}
	}

	private class ColorRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		public ColorRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object color, boolean isSelected, boolean hasFocus, int row,
				int column) {
			setBackground((Color) color);

			return this;
		}
	}

	private class ColorEditor extends AbstractCellEditor implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		private JButton colorButton;

		public ColorEditor() {
			colorButton = new JButton();
			colorButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Color newColor = JColorChooser.showDialog(colorButton,
							"Choose Color", colorButton.getBackground());

					if (newColor != null) {
						colorButton.setBackground(newColor);
						stopCellEditing();
					}
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			colorButton.setBackground((Color) value);

			return colorButton;
		}

		@Override
		public Object getCellEditorValue() {
			return colorButton.getBackground();
		}

	}

	private class ColorListRenderer extends JComponent implements
			TableCellRenderer {

		private static final long serialVersionUID = 1L;

		private List<Color> colorList;

		public ColorListRenderer() {
			colorList = new ArrayList<>();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object color, boolean isSelected, boolean hasFocus, int row,
				int column) {
			colorList = (List<Color>) color;

			return this;
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (colorList.isEmpty()) {
				super.paintComponent(g);
			} else {
				double w = (double) getWidth() / (double) colorList.size();

				for (int i = 0; i < colorList.size(); i++) {
					g.setColor(colorList.get(i));
					g.fillRect((int) (i * w), 0, (int) Math.max(w, 1),
							getHeight());
				}
			}
		}
	}

	private class ColorListEditor extends AbstractCellEditor implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private List<Color> colorList;

		public ColorListEditor() {
			button = new JButton();
			colorList = new ArrayList<>();
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					ColorListDialog dialog = new ColorListDialog(colorList);

					dialog.setVisible(true);

					if (dialog.isApproved()) {
						colorList = dialog.getColorList();
						stopCellEditing();
					}
				}
			});
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			colorList = (List<Color>) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return colorList;
		}
	}

	private class ShapeListRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		public ShapeListRenderer() {
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object color, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return this;
		}
	}

	private class ShapeListEditor extends AbstractCellEditor implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private List<String> shapeList;

		public ShapeListEditor() {
			button = new JButton();
			shapeList = new ArrayList<>();
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					ShapeListDialog dialog = new ShapeListDialog(shapeList);

					dialog.setVisible(true);

					if (dialog.isApproved()) {
						shapeList = dialog.getShapeList();
						stopCellEditing();
					}
				}
			});
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			shapeList = (List<String>) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return shapeList;
		}
	}

	private class TimeSeriesEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private List<TimeSeriesXml> timeSeries;

		public TimeSeriesEditor() {
			button = new JButton("View");
			button.addActionListener(this);
			timeSeries = new ArrayList<>();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			timeSeries = (List<TimeSeriesXml>) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return timeSeries;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			TimeSeriesDialog dialog = new TimeSeriesDialog(button, timeSeries,
					false);

			dialog.setVisible(true);
		}
	}

	private class FormulaEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private String formula;

		public FormulaEditor() {
			button = new JButton("View");
			button.addActionListener(this);
			formula = "";
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			formula = (String) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return formula;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			FormulaDialog dialog = new FormulaDialog(formula);

			dialog.setVisible(true);
		}
	}

	private class ParameterEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private Map<String, Double> parameters;

		public ParameterEditor() {
			button = new JButton("View");
			button.addActionListener(this);
			parameters = new LinkedHashMap<>();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			parameters = (Map<String, Double>) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return parameters;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ParameterDialog dialog = new ParameterDialog(parameters);

			dialog.setVisible(true);
		}
	}

	private class VariableEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton button;
		private Map<String, String> variables;

		public VariableEditor() {
			button = new JButton("View");
			button.addActionListener(this);
			variables = new LinkedHashMap<>();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			variables = (Map<String, String>) value;

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return variables;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			VariableDialog dialog = new VariableDialog(variables);

			dialog.setVisible(true);
		}
	}

	private class CheckBoxRenderer extends JCheckBox implements
			TableCellRenderer {

		private static final long serialVersionUID = -8337460338388283099L;

		public CheckBoxRenderer() {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
			setBorderPainted(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			int statusColumn = -1;

			for (int i = 0; i < table.getColumnCount(); i++) {
				if (table.getColumnName(i).equals(ChartConstants.STATUS)) {
					statusColumn = i;
					break;
				}
			}

			if (invalidIds.contains(table.getValueAt(row, 0))) {
				setEnabled(false);
			} else {
				setEnabled(true);
			}

			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else if (statusColumn != -1) {
				String statusValue = (String) table.getValueAt(row,
						statusColumn);

				if (statusValue.equals(ChartConstants.OK)) {
					setForeground(table.getForeground());
					setBackground(table.getBackground());
				} else if (statusValue.equals(ChartConstants.FAILED)) {
					setForeground(Color.RED);
					setBackground(Color.RED);
				} else if (statusValue.equals(ChartConstants.OUT_OF_LIMITS)) {
					setForeground(Color.YELLOW);
					setBackground(Color.YELLOW);
				} else if (statusValue.equals(ChartConstants.NO_COVARIANCE)) {
					setForeground(Color.YELLOW);
					setBackground(Color.YELLOW);
				} else if (statusValue.equals(ChartConstants.NOT_SIGNIFICANT)) {
					setForeground(Color.YELLOW);
					setBackground(Color.YELLOW);
				}
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}

			setSelected((value != null && ((Boolean) value).booleanValue()));

			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			} else {
				setBorder(new EmptyBorder(1, 1, 1, 1));
			}

			return this;
		}
	}

	private class CheckBoxEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 1L;

		public CheckBoxEditor() {
			super(new JCheckBox());
			((JCheckBox) getComponent())
					.setHorizontalAlignment(SwingConstants.CENTER);
		}
	}

	private class SelectTableRowSorter extends TableRowSorter<SelectTableModel> {

		private Map<Integer, String> filters;

		public SelectTableRowSorter(SelectTableModel model,
				Map<String, String> filters) {
			super(model);
			this.filters = new LinkedHashMap<>();

			if (filters != null) {
				for (String column : filters.keySet()) {
					for (int i = 0; i < model.getColumnCount(); i++) {
						if (column.equals(model.getColumnName(i))) {
							this.filters.put(i, filters.get(column));
						}
					}
				}

				addFilters();
			}
		}

		@Override
		public void toggleSortOrder(int column) {
			List<? extends SortKey> sortKeys = getSortKeys();

			if (sortKeys.size() > 0) {
				if (sortKeys.get(0).getColumn() == column
						&& sortKeys.get(0).getSortOrder() == SortOrder.DESCENDING) {
					setSortKeys(null);
					return;
				}
			}

			super.toggleSortOrder(column);
		}

		private void addFilters() {
			setRowFilter(new RowFilter<SelectTableModel, Object>() {

				@Override
				public boolean include(
						javax.swing.RowFilter.Entry<? extends SelectTableModel, ? extends Object> entry) {
					for (int column : filters.keySet()) {
						if (!entry.getStringValue(column).equals(
								filters.get(column))) {
							return false;
						}
					}

					return true;
				}
			});
		}
	}

	private class ColorListDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;

		private boolean approved;
		private List<Color> colorList;

		private List<JButton> colorButtons;

		private JButton okButton;
		private JButton cancelButton;

		public ColorListDialog(List<Color> initialColors) {
			super(SwingUtilities.getWindowAncestor(ChartSelectionPanel.this),
					"Color Palette", DEFAULT_MODALITY_TYPE);

			approved = false;
			colorList = null;

			colorButtons = new ArrayList<>();
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel centerPanel = new JPanel();
			JPanel bottomPanel = new JPanel();

			centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			centerPanel
					.setLayout(new GridLayout(initialColors.size(), 1, 5, 5));

			for (Color color : initialColors) {
				JButton button = new JButton();

				button.setBackground(color);
				button.setPreferredSize(new Dimension(
						button.getPreferredSize().width, 20));
				button.addActionListener(this);
				colorButtons.add(button);
				centerPanel.add(button);
			}

			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			JPanel scrollPanel = new JPanel();

			scrollPanel.setLayout(new BorderLayout());
			scrollPanel.add(centerPanel, BorderLayout.NORTH);

			setLayout(new BorderLayout());
			add(new JScrollPane(scrollPanel), BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setLocationRelativeTo(ChartSelectionPanel.this);
			UI.adjustDialog(this);
		}

		public boolean isApproved() {
			return approved;
		}

		public List<Color> getColorList() {
			return colorList;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				approved = true;
				colorList = new ArrayList<>();

				for (JButton button : colorButtons) {
					colorList.add(button.getBackground());
				}

				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			} else {
				JButton button = (JButton) e.getSource();
				Color newColor = JColorChooser.showDialog(button,
						"Choose Color", button.getBackground());

				if (newColor != null) {
					button.setBackground(newColor);
				}
			}
		}
	}

	private class ShapeListDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;

		private boolean approved;
		private List<String> shapeList;

		private List<JComboBox<String>> shapeBoxes;

		private JButton okButton;
		private JButton cancelButton;

		public ShapeListDialog(List<String> initialShapes) {
			super(SwingUtilities.getWindowAncestor(ChartSelectionPanel.this),
					"Shape Palette", DEFAULT_MODALITY_TYPE);

			approved = false;
			shapeList = null;

			shapeBoxes = new ArrayList<>();
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel centerPanel = new JPanel();
			JPanel bottomPanel = new JPanel();

			centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			centerPanel
					.setLayout(new GridLayout(initialShapes.size(), 1, 5, 5));

			for (String shape : initialShapes) {
				JComboBox<String> box = new JComboBox<>(
						ColorAndShapeCreator.SHAPE_NAMES);

				box.setSelectedItem(shape);
				shapeBoxes.add(box);
				centerPanel.add(box);
			}

			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			JPanel scrollPanel = new JPanel();

			scrollPanel.setLayout(new BorderLayout());
			scrollPanel.add(centerPanel, BorderLayout.NORTH);

			setLayout(new BorderLayout());
			add(new JScrollPane(scrollPanel), BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setLocationRelativeTo(ChartSelectionPanel.this);
			UI.adjustDialog(this);
		}

		public boolean isApproved() {
			return approved;
		}

		public List<String> getShapeList() {
			return shapeList;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				approved = true;
				shapeList = new ArrayList<>();

				for (JComboBox<String> box : shapeBoxes) {
					shapeList.add((String) box.getSelectedItem());
				}

				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}
	}

	private class FormulaDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton okButton;

		public FormulaDialog(String formula) {
			super(SwingUtilities.getWindowAncestor(ChartSelectionPanel.this),
					"Formula", DEFAULT_MODALITY_TYPE);

			okButton = new JButton("OK");
			okButton.addActionListener(this);

			StringTextField field = new StringTextField(true);

			field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			field.setValue(formula);
			field.setEditable(false);
			field.setPreferredSize(new Dimension(
					field.getPreferredSize().width + 10, field
							.getPreferredSize().height));

			JPanel mainPanel = new JPanel();

			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(field, BorderLayout.NORTH);

			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);

			setLayout(new BorderLayout());
			add(new JScrollPane(mainPanel), BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setLocationRelativeTo(ChartSelectionPanel.this);
			UI.adjustDialog(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	private class ParameterDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton okButton;

		public ParameterDialog(Map<String, Double> parameters) {
			super(SwingUtilities.getWindowAncestor(ChartSelectionPanel.this),
					"Parameters", DEFAULT_MODALITY_TYPE);

			okButton = new JButton("OK");
			okButton.addActionListener(this);

			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();

			leftPanel.setLayout(new GridLayout(parameters.size(), 1, 5, 5));
			rightPanel.setLayout(new GridLayout(parameters.size(), 1, 5, 5));

			for (String param : parameters.keySet()) {
				FormattedDoubleTextField field = new FormattedDoubleTextField(
						true);
				Double value = parameters.get(param);

				field.setValue(value);
				field.setEditable(false);

				leftPanel.add(new JLabel(param + ":"));
				rightPanel.add(field);
			}

			JPanel panel = new JPanel();

			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			panel.setLayout(new BorderLayout(5, 5));
			panel.add(leftPanel, BorderLayout.WEST);
			panel.add(rightPanel, BorderLayout.CENTER);

			JPanel mainPanel = new JPanel();

			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(panel, BorderLayout.NORTH);

			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);

			setLayout(new BorderLayout());
			add(new JScrollPane(mainPanel), BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setLocationRelativeTo(ChartSelectionPanel.this);
			UI.adjustDialog(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	private class VariableDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;

		private JButton okButton;

		public VariableDialog(Map<String, String> variables) {
			super(SwingUtilities.getWindowAncestor(ChartSelectionPanel.this),
					"Variables", DEFAULT_MODALITY_TYPE);

			okButton = new JButton("OK");
			okButton.addActionListener(this);

			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();

			leftPanel.setLayout(new GridLayout(variables.size(), 1, 5, 5));
			rightPanel.setLayout(new GridLayout(variables.size(), 1, 5, 5));

			for (String param : variables.keySet()) {
				StringTextField field = new StringTextField(true);
				String value = variables.get(param);

				field.setValue(" " + value);
				field.setHorizontalAlignment(SwingConstants.RIGHT);
				field.setEditable(false);

				leftPanel.add(new JLabel(param + ":"));
				rightPanel.add(field);
			}

			JPanel panel = new JPanel();

			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			panel.setLayout(new BorderLayout(5, 5));
			panel.add(leftPanel, BorderLayout.WEST);
			panel.add(rightPanel, BorderLayout.CENTER);

			JPanel mainPanel = new JPanel();

			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(panel, BorderLayout.NORTH);

			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			bottomPanel.add(okButton);

			setLayout(new BorderLayout());
			add(new JScrollPane(mainPanel), BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setLocationRelativeTo(ChartSelectionPanel.this);
			UI.adjustDialog(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	private class ColumnSelectionDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		private boolean approved;
		private List<String> selection;
		private Map<String, JCheckBox> selectionBoxes;
		private JCheckBox visualizationBox;
		private JCheckBox miscellaneousBox;
		private JCheckBox qualityBox;
		private JCheckBox conditionsBox;

		private JButton okButton;
		private JButton cancelButton;

		public ColumnSelectionDialog(List<String> initialSelection) {
			super(SwingUtilities.getWindowAncestor(ChartSelectionPanel.this),
					"Column Selection", DEFAULT_MODALITY_TYPE);
			approved = false;
			selection = null;
			selectionBoxes = new LinkedHashMap<>();

			JPanel visualizationPanel = new JPanel();

			visualizationPanel.setLayout(new GridLayout(visualizationColumns
					.size() + 1, 1, 5, 5));
			visualizationBox = new JCheckBox("All");
			visualizationBox.addActionListener(this);
			visualizationPanel.add(visualizationBox);

			for (String column : visualizationColumns) {
				JCheckBox box = new JCheckBox(column);

				if (initialSelection.contains(column)) {
					box.setSelected(true);
				} else {
					box.setSelected(false);
				}

				box.addActionListener(this);
				selectionBoxes.put(column, box);
				visualizationPanel.add(box);
			}

			JPanel miscellaneousPanel = new JPanel();

			miscellaneousPanel.setLayout(new GridLayout(miscellaneousColumns
					.size() + 1, 1, 5, 5));
			miscellaneousBox = new JCheckBox("All");
			miscellaneousBox.addActionListener(this);
			miscellaneousPanel.add(miscellaneousBox);

			for (String column : miscellaneousColumns) {
				JCheckBox box = new JCheckBox(column);

				if (initialSelection.contains(column)) {
					box.setSelected(true);
				} else {
					box.setSelected(false);
				}

				box.addActionListener(this);
				selectionBoxes.put(column, box);
				miscellaneousPanel.add(box);
			}

			JPanel qualityPanel = new JPanel();

			qualityPanel.setLayout(new GridLayout(qualityColumns.size() + 1, 1,
					5, 5));
			qualityBox = new JCheckBox("All");
			qualityBox.addActionListener(this);
			qualityPanel.add(qualityBox);

			for (String column : qualityColumns.keySet()) {
				JCheckBox box = new JCheckBox(column);

				if (initialSelection.contains(column)) {
					box.setSelected(true);
				} else {
					box.setSelected(false);
				}

				box.addActionListener(this);
				selectionBoxes.put(column, box);
				qualityPanel.add(box);
			}

			JPanel conditionsPanel = new JPanel();

			conditionsPanel.setLayout(new GridLayout(conditions.size() + 1, 1,
					5, 5));
			conditionsBox = new JCheckBox("All");
			conditionsBox.addActionListener(this);
			conditionsPanel.add(conditionsBox);

			for (String column : conditions) {
				JCheckBox box = new JCheckBox(column);

				if (initialSelection.contains(column)) {
					box.setSelected(true);
				} else {
					box.setSelected(false);
				}

				box.addActionListener(this);
				selectionBoxes.put(column, box);
				conditionsPanel.add(box);
			}

			updateCheckBoxes();

			JPanel centerPanel = new JPanel();

			centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

			centerPanel.add(createNorthPanel(visualizationPanel,
					"Visualization"));

			if (!miscellaneousColumns.isEmpty()) {
				centerPanel.add(createNorthPanel(miscellaneousPanel,
						"Miscellaneous"));
			}

			if (!qualityColumns.isEmpty()) {
				centerPanel.add(createNorthPanel(qualityPanel,
						"Quality Criteria"));
			}

			if (!conditions.isEmpty()) {
				centerPanel
						.add(createNorthPanel(conditionsPanel, "Conditions"));
			}

			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(centerPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setLocationRelativeTo(ChartSelectionPanel.this);
			UI.adjustDialog(this);
		}

		public boolean isApproved() {
			return approved;
		}

		public List<String> getSelection() {
			return selection;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				approved = true;
				selection = new ArrayList<>();

				for (String column : selectionBoxes.keySet()) {
					if (selectionBoxes.get(column).isSelected()) {
						selection.add(column);
					}
				}

				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			} else if (e.getSource() == visualizationBox) {
				if (visualizationBox.isSelected()) {
					setColumnsTo(visualizationColumns, true);
				} else {
					setColumnsTo(visualizationColumns, false);
				}
			} else if (e.getSource() == miscellaneousBox) {
				if (miscellaneousBox.isSelected()) {
					setColumnsTo(miscellaneousColumns, true);
				} else {
					setColumnsTo(miscellaneousColumns, false);
				}
			} else if (e.getSource() == qualityBox) {
				if (qualityBox.isSelected()) {
					setColumnsTo(qualityColumns.keySet(), true);
				} else {
					setColumnsTo(qualityColumns.keySet(), false);
				}
			} else if (e.getSource() == conditionsBox) {
				if (conditionsBox.isSelected()) {
					setColumnsTo(conditions, true);
				} else {
					setColumnsTo(conditions, false);
				}
			} else {
				updateCheckBoxes();
			}
		}

		private JComponent createNorthPanel(JComponent comp, String name) {
			JPanel panel = new JPanel();

			panel.setBorder(BorderFactory.createTitledBorder(name));
			panel.setLayout(new BorderLayout());
			panel.add(comp, BorderLayout.NORTH);
			panel.setPreferredSize(new Dimension(Math.max(
					panel.getPreferredSize().width, 100), panel
					.getPreferredSize().height));

			return new JScrollPane(panel,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		}

		private void updateCheckBoxes() {
			boolean allVisualizationSelected = true;
			boolean allMiscellaneousSelected = true;
			boolean allQualitySelected = true;
			boolean allConditionsSelected = true;

			for (String column : visualizationColumns) {
				if (!selectionBoxes.get(column).isSelected()) {
					allVisualizationSelected = false;
					break;
				}
			}

			for (String column : miscellaneousColumns) {
				if (!selectionBoxes.get(column).isSelected()) {
					allMiscellaneousSelected = false;
					break;
				}
			}

			for (String column : qualityColumns.keySet()) {
				if (!selectionBoxes.get(column).isSelected()) {
					allQualitySelected = false;
					break;
				}
			}

			for (String column : conditions) {
				if (!selectionBoxes.get(column).isSelected()) {
					allConditionsSelected = false;
					break;
				}
			}

			if (allVisualizationSelected) {
				visualizationBox.setSelected(true);
			} else {
				visualizationBox.setSelected(false);
			}

			if (allMiscellaneousSelected) {
				miscellaneousBox.setSelected(true);
			} else {
				miscellaneousBox.setSelected(false);
			}

			if (allQualitySelected) {
				qualityBox.setSelected(true);
			} else {
				qualityBox.setSelected(false);
			}

			if (allConditionsSelected) {
				conditionsBox.setSelected(true);
			} else {
				conditionsBox.setSelected(false);
			}
		}

		private void setColumnsTo(Collection<String> columns, boolean value) {
			for (String column : columns) {
				selectionBoxes.get(column).setSelected(value);
			}
		}
	}

}
