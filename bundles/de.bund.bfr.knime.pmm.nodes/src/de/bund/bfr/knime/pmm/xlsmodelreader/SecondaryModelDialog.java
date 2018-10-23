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
package de.bund.bfr.knime.pmm.xlsmodelreader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.ui.UI;

public class SecondaryModelDialog extends JDialog implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private List<String> fileColumnList;
	private SettingsHelper set;
	private String param;

	private JButton okButton;
	private JButton cancelButton;
	private JScrollPane configPanel;

	private KnimeTuple tuple;
	private Map<String, String> mappings;
	private Map<String, String> paramErrors;
	private Map<String, String> mins;
	private Map<String, String> maxs;
	private Map<String, String> categories;
	private Map<String, String> units;
	private String rmse;
	private String r2;
	private String aic;
	private String dataPoints;

	private JButton modelButton;
	private JButton reloadButton;
	private Map<String, JComboBox<String>> modelBoxes;
	private Map<String, JComboBox<String>> paramErrorBoxes;
	private Map<String, JComboBox<String>> minBoxes;
	private Map<String, JComboBox<String>> maxBoxes;
	private Map<String, JComboBox<String>> categoryBoxes;
	private Map<String, JComboBox<String>> unitBoxes;
	private JComboBox<String> rmseBox;
	private JComboBox<String> r2Box;
	private JComboBox<String> aicBox;
	private JComboBox<String> dataPointsBox;

	public SecondaryModelDialog(Component parent, List<String> fileColumnList, SettingsHelper set, String param) {
		super(SwingUtilities.getWindowAncestor(parent), "Secondary Model", DEFAULT_MODALITY_TYPE);
		this.fileColumnList = fileColumnList;
		this.set = set;
		this.param = param;

		tuple = set.getSecModelTuples().get(param);
		mappings = new LinkedHashMap<>(set.getSecModelMappings().get(param));
		paramErrors = new LinkedHashMap<>(set.getSecModelParamErrors().get(param));
		mins = new LinkedHashMap<>(set.getSecModelIndepMins().get(param));
		maxs = new LinkedHashMap<>(set.getSecModelIndepMaxs().get(param));
		categories = new LinkedHashMap<>(set.getSecModelIndepCategories().get(param));
		units = new LinkedHashMap<>(set.getSecModelIndepUnits().get(param));
		rmse = set.getSecModelRmse().get(param);
		r2 = set.getSecModelR2().get(param);
		aic = set.getSecModelAic().get(param);
		dataPoints = set.getSecModelDataPoints().get(param);

		NullToDoNotUse(mappings);
		NullToDoNotUse(paramErrors);
		NullToDoNotUse(mins);
		NullToDoNotUse(maxs);

		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		configPanel = null;

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		setLayout(new BorderLayout());
		add(buttonPanel, BorderLayout.SOUTH);
		updateConfigPanel();
		setLocationRelativeTo(parent);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			DoNotUseToNull(mappings);
			DoNotUseToNull(paramErrors);
			DoNotUseToNull(mins);
			DoNotUseToNull(maxs);

			set.getSecModelTuples().put(param, tuple);
			set.getSecModelMappings().put(param, mappings);
			set.getSecModelParamErrors().put(param, paramErrors);
			set.getSecModelIndepMins().put(param, mins);
			set.getSecModelIndepMaxs().put(param, maxs);
			set.getSecModelIndepCategories().put(param, categories);
			set.getSecModelIndepUnits().put(param, units);
			set.getSecModelRmse().put(param, rmse.equals(SettingsHelper.DO_NOT_USE) ? null : rmse);
			set.getSecModelR2().put(param, r2.equals(SettingsHelper.DO_NOT_USE) ? null : r2);
			set.getSecModelAic().put(param, aic.equals(SettingsHelper.DO_NOT_USE) ? null : aic);
			set.getSecModelDataPoints().put(param, dataPoints.equals(SettingsHelper.DO_NOT_USE) ? null : dataPoints);
			dispose();
		} else if (e.getSource() == cancelButton) {
			dispose();
		} else if (e.getSource() == modelButton) {
			Integer id;

			if (tuple != null) {
				id = DBKernel.openSecModelDBWindow(modelButton,
						((CatalogModelXml) tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG).get(0)).id);
			} else {
				id = DBKernel.openSecModelDBWindow(modelButton, null);
			}

			if (id != null) {
				Bfrdb db = new Bfrdb(DBKernel.getLocalConn(true));

				try {
					tuple = db.getSecModelById(id);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				updateConfigPanel();
			}
		} else if (e.getSource() == reloadButton) {
			Integer id = ((CatalogModelXml) tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG).get(0)).id;

			if (id != null) {
				Bfrdb db = new Bfrdb(DBKernel.getLocalConn(true));

				try {
					set.getSecModelTuples().put(param, db.getSecModelById(id));
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				updateConfigPanel();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == rmseBox) {
			rmse = (String) rmseBox.getSelectedItem();
		} else if (e.getSource() == r2Box) {
			r2 = (String) r2Box.getSelectedItem();
		} else if (e.getSource() == aicBox) {
			aic = (String) aicBox.getSelectedItem();
		} else if (e.getSource() == dataPointsBox) {
			dataPoints = (String) dataPointsBox.getSelectedItem();
		} else {
			for (String param2 : modelBoxes.keySet()) {
				if (e.getSource() == modelBoxes.get(param2)) {
					mappings.put(param2, (String) modelBoxes.get(param2).getSelectedItem());
					return;
				}
			}

			for (String param2 : paramErrorBoxes.keySet()) {
				if (e.getSource() == paramErrorBoxes.get(param2)) {
					paramErrors.put(param2, (String) paramErrorBoxes.get(param2).getSelectedItem());
					return;
				}
			}

			for (String param2 : minBoxes.keySet()) {
				if (e.getSource() == minBoxes.get(param2)) {
					mins.put(param2, (String) minBoxes.get(param2).getSelectedItem());
					return;
				}
			}

			for (String param2 : maxBoxes.keySet()) {
				if (e.getSource() == maxBoxes.get(param2)) {
					maxs.put(param2, (String) maxBoxes.get(param2).getSelectedItem());
					return;
				}
			}

			for (String param2 : categoryBoxes.keySet()) {
				if (e.getSource() == categoryBoxes.get(param2)) {
					categories.put(param2, (String) categoryBoxes.get(param2).getSelectedItem());
					updateConfigPanel();
					return;
				}
			}

			for (String param2 : unitBoxes.keySet()) {
				if (e.getSource() == unitBoxes.get(param2)) {
					units.put(param2, (String) unitBoxes.get(param2).getSelectedItem());
					return;
				}
			}
		}
	}

	private void updateConfigPanel() {
		if (configPanel != null) {
			remove(configPanel);
		}

		JPanel panel = new JPanel();

		panel.setLayout(new GridBagLayout());

		int row = 0;
		List<String> options = new ArrayList<>();

		options.add(SettingsHelper.DO_NOT_USE);
		options.addAll(fileColumnList);

		JPanel secButtonPanel = new JPanel();

		if (tuple != null) {
			modelButton = new JButton(
					((CatalogModelXml) tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG).get(0)).name);
			reloadButton = new JButton(SettingsHelper.RELOAD);
			reloadButton.setEnabled(true);
		} else {
			modelButton = new JButton(SettingsHelper.SELECT);
			reloadButton = new JButton(SettingsHelper.RELOAD);
			reloadButton.setEnabled(false);
		}

		secButtonPanel.setLayout(new BorderLayout());
		secButtonPanel.add(modelButton, BorderLayout.WEST);
		secButtonPanel.add(reloadButton, BorderLayout.EAST);

		modelButton.addActionListener(this);
		reloadButton.addActionListener(this);
		panel.add(new JLabel(param + ":"), createConstraints(0, row));
		panel.add(secButtonPanel, createConstraints(1, row));
		row++;

		modelBoxes = new LinkedHashMap<>();
		paramErrorBoxes = new LinkedHashMap<>();
		minBoxes = new LinkedHashMap<>();
		maxBoxes = new LinkedHashMap<>();
		categoryBoxes = new LinkedHashMap<>();
		unitBoxes = new LinkedHashMap<>();

		if (tuple != null) {
			for (PmmXmlElementConvertable el : tuple.getPmmXml(Model2Schema.ATT_PARAMETER).getElementSet()) {
				ParamXml secParam = (ParamXml) el;
				JComboBox<String> box = new JComboBox<>(options.toArray(new String[0]));
				JComboBox<String> errorBox = new JComboBox<>(options.toArray(new String[0]));

				mappings.put(secParam.getName(),
						UI.select(box, mappings.get(secParam.getName()), SettingsHelper.DO_NOT_USE));
				paramErrors.put(secParam.getName(),
						UI.select(errorBox, paramErrors.get(secParam.getName()), SettingsHelper.DO_NOT_USE));

				box.addItemListener(this);
				errorBox.addItemListener(this);
				modelBoxes.put(secParam.getName(), box);
				paramErrorBoxes.put(secParam.getName(), errorBox);

				panel.add(new JLabel(secParam.getName() + ":"), createConstraints(0, row));
				panel.add(box, createConstraints(1, row));
				row++;
				panel.add(new JLabel(secParam.getName() + " Error:"), createConstraints(0, row));
				panel.add(errorBox, createConstraints(1, row));
				row++;
			}

			for (PmmXmlElementConvertable el2 : tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT).getElementSet()) {
				IndepXml indep = (IndepXml) el2;
				// String[] categoryChoice = Categories.getAllCategories()
				// .toArray(new String[0]);
				// String defaultCategory = indep.getCategory() != null ? indep
				// .getCategory() : categoryChoice[0];

				JComboBox<String> minBox = new JComboBox<>(options.toArray(new String[0]));
				JComboBox<String> maxBox = new JComboBox<>(options.toArray(new String[0]));
				JComboBox<String> categoryBox = new JComboBox<>(new String[] { indep.category });

				mins.put(indep.name, UI.select(minBox, mins.get(indep.name), SettingsHelper.DO_NOT_USE));
				maxs.put(indep.name, UI.select(maxBox, maxs.get(indep.name), SettingsHelper.DO_NOT_USE));
				categories.put(indep.name, indep.category);

				// String[] unitChoice = Categories
				// .getCategory(categories.get(indep.getName()))
				// .getAllUnits().toArray(new String[0]);
				// String defaultUnit = indep.getUnit() != null ?
				// indep.getUnit()
				// : unitChoice[0];
				JComboBox<String> unitBox = new JComboBox<>(new String[] { indep.unit });

				units.put(indep.name, indep.unit);

				minBox.addItemListener(this);
				maxBox.addItemListener(this);
				// categoryBox.addItemListener(this);
				// unitBox.addItemListener(this);
				minBoxes.put(indep.name, minBox);
				maxBoxes.put(indep.name, maxBox);
				categoryBoxes.put(indep.name, categoryBox);
				unitBoxes.put(indep.name, unitBox);

				panel.add(new JLabel("Min " + indep.name + ":"), createConstraints(0, row));
				panel.add(minBox, createConstraints(1, row));
				row++;
				panel.add(new JLabel("Max " + indep.name + ":"), createConstraints(0, row));
				panel.add(maxBox, createConstraints(1, row));
				row++;
				panel.add(new JLabel(indep.name + " Category:"), createConstraints(0, row));
				panel.add(categoryBox, createConstraints(1, row));
				row++;
				panel.add(new JLabel(indep.name + " Unit:"), createConstraints(0, row));
				panel.add(unitBox, createConstraints(1, row));
				row++;
			}
		}

		rmseBox = new JComboBox<>(options.toArray(new String[0]));
		r2Box = new JComboBox<>(options.toArray(new String[0]));
		aicBox = new JComboBox<>(options.toArray(new String[0]));
		dataPointsBox = new JComboBox<>(options.toArray(new String[0]));

		rmse = UI.select(rmseBox, rmse, SettingsHelper.DO_NOT_USE);
		r2 = UI.select(r2Box, r2, SettingsHelper.DO_NOT_USE);
		aic = UI.select(aicBox, aic, SettingsHelper.DO_NOT_USE);
		dataPoints = UI.select(dataPointsBox, dataPoints, SettingsHelper.DO_NOT_USE);

		rmseBox.addItemListener(this);
		r2Box.addItemListener(this);
		aicBox.addItemListener(this);
		dataPointsBox.addItemListener(this);

		panel.add(new JLabel("RMSE:"), createConstraints(0, row));
		panel.add(rmseBox, createConstraints(1, row));
		row++;
		panel.add(new JLabel("R2:"), createConstraints(0, row));
		panel.add(r2Box, createConstraints(1, row));
		row++;
		panel.add(new JLabel("AIC:"), createConstraints(0, row));
		panel.add(aicBox, createConstraints(1, row));
		row++;
		panel.add(new JLabel("N Data:"), createConstraints(0, row));
		panel.add(dataPointsBox, createConstraints(1, row));
		row++;

		JPanel westPanel = new JPanel();

		westPanel.setLayout(new BorderLayout());
		westPanel.add(panel, BorderLayout.WEST);

		JPanel northPanel = new JPanel();

		northPanel.setLayout(new BorderLayout());
		northPanel.add(westPanel, BorderLayout.NORTH);

		configPanel = new JScrollPane(northPanel);
		add(configPanel, BorderLayout.CENTER);
		pack();
	}

	private GridBagConstraints createConstraints(int x, int y) {
		return new GridBagConstraints(x, y, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0);
	}

	private void DoNotUseToNull(Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue().equals(SettingsHelper.DO_NOT_USE)) {
				entry.setValue(null);
			}
		}
	}

	private void NullToDoNotUse(Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue() == null) {
				entry.setValue(SettingsHelper.DO_NOT_USE);
			}
		}
	}
}
