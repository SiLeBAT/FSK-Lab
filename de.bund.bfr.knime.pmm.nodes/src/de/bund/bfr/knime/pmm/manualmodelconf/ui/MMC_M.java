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

import java.awt.*;
import java.awt.event.*;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import de.bund.bfr.knime.pmm.common.ui.*;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;
import org.knime.core.node.InvalidSettingsException;
import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.SymbolTable;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.DBUtilities;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.resources.Resources;

/**
 * @author Armin Weiser
 */
public class MMC_M extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LABEL_OWNMODEL = "Manually defined formula";

	private Frame m_parentFrame = null;
	@SuppressWarnings("unchecked")
	private JComboBox<ParametricModel>[] threeBoxes = new JComboBox[3];
	private HashMap<ParametricModel, HashMap<String, ParametricModel>> m_secondaryModels = null;
	private DefaultListModel<ParametricModel> listModel;
	private MMC_TS m_mmcts;
	private HashMap<Integer, PmmTimeSeries> tss;
	private boolean dontFireList = false;

	private Connection m_conn = null;
	private String dbuuid = null;
	private boolean dontTouch = false;

	private boolean modelNameChangedManually = false;
	private boolean formulaCreator;
	private boolean isEditor = false;

	public void setEditor(boolean isEditor) {
		this.isEditor = isEditor;
	}

	public MMC_M() {
		this(null, 1, "", false, null);
	}

	public MMC_M(final Frame parentFrame, final int level, final String paramName, boolean formulaCreator, MMC_TS m_mmcts) {
		this.m_parentFrame = parentFrame;
		this.formulaCreator = formulaCreator;
		this.m_mmcts = m_mmcts;
		initComponents();
		m_secondaryModels = new HashMap<>();
		depVarLabel.setText(paramName);
		if (level == 1) {
			radioButton1.setSelected(true);
			// depVarLabel.setVisible(false);
		} else {
			depVarLabel.setVisible(true);
			radioButton2.setSelected(true);
			radioButton1.setEnabled(false);
			radioButton2.setEnabled(false);
			radioButton3.setEnabled(false);
		}

		for (String s : DBKernel.myDBi.getHashMap("ModelType").values()) {
			typeBox.addItem(s);
		}
		scrollPane3.setVisible(false); // List1

		if (formulaCreator) {
			label11.setVisible(false);
			label10.setVisible(false);
			label3.setVisible(false);
			label4.setVisible(false);
			label5.setVisible(false);
			label6.setVisible(false);
			label8.setVisible(false);
			label12.setVisible(false);
			fittedModelName.setVisible(false);
			r2Field.setVisible(false);
			rmsField.setVisible(false);
			aicField.setVisible(false);
			bicField.setVisible(false);
			checkBox1.setVisible(false);
			qScoreBox.setVisible(false);
			table.getColumnModel().getColumn(3).setMinWidth(0);
			table.getColumnModel().getColumn(3).setMaxWidth(0);
			table.getColumnModel().getColumn(3).setWidth(0);
			table.getColumnModel().getColumn(4).setMinWidth(0);
			table.getColumnModel().getColumn(4).setMaxWidth(0);
			table.getColumnModel().getColumn(4).setWidth(0);
		}
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(400);

		referencesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent lse) {
				if (!lse.getValueIsAdjusting()) {
					if (referencesTable.getSelectedRowCount() > 0) {
						button2.setEnabled(true);
						button3.setEnabled(true);
					} else {
						button2.setEnabled(false);
						button3.setEnabled(false);
					}
				}
			}
		});
	}

	public ParametricModel getPM() {
		ParametricModel pm = table.getPM(); 
		refreshRefsInPM(pm);
		return pm;
	}

	public void setPM(ParametricModel pm) {
		if (pm != null) {
			if (pm.getLevel() == 2) {
				if (!radioButton2.isSelected()) {
					radioButton2.setSelected(true);
					setComboBox();
				}
			}
			modelnameField.setText(pm.modelName);
			if (pm.modelClass != null) {
				typeBox.setSelectedItem(DBKernel.myDBi.getHashMap("ModelType").get(pm.modelClass));
			} else {
				typeBox.setSelectedItem(null);
			}
			fittedModelName.setText(pm.getFittedModelName());
			String depVar = depVarLabel.getText();
			if (!depVar.isEmpty()) {
				String formula = pm.getFormula();
				int index = formula.indexOf("=");
				if (index >= 0) {
					// String oldDepVar = formula.substring(0, index);
					// System.err.println(oldDepVar + "=" + depVar);
					// pm.getDepXml().setOrigName(oldDepVar);//.addVarParMap(depVar,
					// oldDepVar);

					// formula =
					// MathUtilities.replaceVariable(formula.substring(index),
					// depVar, depVar+depVar);
					formula = depVar + formula.substring(index);
					pm.setDepVar(depVar, false); // true; Hier: false wichtig, sonst geht was beim Speichern schief, siehe Ticket #329
					pm.setFormula(formula);
				}
			}
			formulaArea.setText(MathUtilities.getAllButBoundaryCondition(pm.getFormula()));
			boundaryArea.setText(MathUtilities.getBoundaryCondition(pm.getFormula()));
			table.setPM(pm, m_secondaryModels.get(pm), radioButton3);
			setDblTextVal(rmsField, pm.getRms());
			setDblTextVal(r2Field, pm.getRsquared());
			setDblTextVal(aicField, pm.getAic());
			setDblTextVal(bicField, pm.getBic());
			while (referencesTable.getRowCount() > 0)
				((DefaultTableModel) referencesTable.getModel()).removeRow(0);
			insertRefs(pm.getEstModelLit(), true);
			insertRefs(pm.getModelLit(), false);
			if (pm.isChecked != null && pm.isChecked) checkBox1.setSelected(true);
			else checkBox1.setSelected(false);
			if (pm.qualityScore != null) qScoreBox.setSelectedIndex(pm.qualityScore);
			else qScoreBox.setSelectedIndex(0);
			if (pm.comment != null) textField1.setText(pm.comment);
			else textField1.setText("");
			insertNselectPMintoBox(pm);
		}
	}

	private void insertRefs(PmmXmlDoc modelLit, boolean isEM) {
		for (PmmXmlElementConvertable el : modelLit.getElementSet()) {
			if (el instanceof LiteratureItem) {
				//Vector<LiteratureItem> vli = new Vector<>();
				LiteratureItem li = (LiteratureItem) el;
				//vli.add(li);
				Object[] o = new Object[2];
				o[0] = isEM;//(formulaCreator || !table.isEstimated()) ? Boolean.FALSE : Boolean.TRUE;
				o[1] = li;
				((DefaultTableModel) referencesTable.getModel()).addRow(o);
			}
		}
	}

	private void setDblTextVal(DoubleTextField tf, Double value) {
		if (value == null || Double.isNaN(value)) tf.setText("");
		else tf.setValue(value);
	}

	public void setConnection(final Connection conn) {
		this.m_conn = conn;
		try {
			Bfrdb db = new Bfrdb(m_conn);
			dbuuid = db.getDBUUID();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		setComboBox();
	}

	private void setComboBox() {
		remove(modelNameBox);
		modelNameBox = threeBoxes[getSelRadio() - 1];
		if (modelNameBox == null) {
			modelNameBox = new JComboBox<>();
			modelNameBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					modelNameBoxActionPerformed(e);
				}
			});
			threeBoxes[getSelRadio() - 1] = modelNameBox;
		}
		add(modelNameBox, CC.xywh(5, 5, 15, 1));
		if (modelNameBox.getItemCount() == 0) {
			loadFromDB();
		}
	}

	private void manageDBMinMax(ResultSet result, ParametricModel pm) throws SQLException {
		Array array = result.getArray(Bfrdb.ATT_PARAMNAME);
		Array arrayMin = result.getArray(Bfrdb.ATT_MINVALUE);
		Array arrayMax = result.getArray(Bfrdb.ATT_MAXVALUE);
		Array arrayCat = result.getArray("ParCategory");
		Array arrayUnit = result.getArray("ParUnit");
		Array arrayDesc = result.getArray("ParamDescription");
		Array arrayParamType = result.getArray(Bfrdb.ATT_PARAMTYPE);
		if (array != null && arrayMin != null && arrayMax != null) {
			try {
				Object[] o = (Object[]) array.getArray();
				Object[] oMin = (Object[]) arrayMin.getArray();
				Object[] oMax = (Object[]) arrayMax.getArray();
				Object[] oCat = (Object[]) arrayCat.getArray();
				Object[] oUnit = (Object[]) arrayUnit.getArray();
				Object[] oDesc = (Object[]) arrayDesc.getArray();
				Object[] oPT = (Object[]) arrayParamType.getArray();
				if (o != null && o.length > 0) {
					for (int ii = 0; ii < o.length; ii++) {
						pm.addParam(o[ii].toString(), null, Double.NaN, Double.NaN);
						if (oMin != null && oMin.length > ii && oMin[ii] != null) {
							pm.setParamMin(o[ii].toString(), Double.parseDouble(oMin[ii].toString()));
						}
						if (oMax != null && oMax.length > ii && oMax[ii] != null) {
							pm.setParamMax(o[ii].toString(), Double.parseDouble(oMax[ii].toString()));
						}
						if (oCat != null && oCat.length > ii && oCat[ii] != null) {
							pm.setParamCategory(o[ii].toString(), oCat[ii].toString());
						}
						if (oUnit != null && oUnit.length > ii && oUnit[ii] != null) {
							pm.setParamUnit(o[ii].toString(), oUnit[ii].toString());
						}
						if (oDesc != null && oDesc.length > ii && oDesc[ii] != null) {
							pm.setParamDescription(o[ii].toString(), oDesc[ii].toString());
						}
						if (oPT != null && oPT.length > ii && oPT[ii] != null) {
							boolean isStart = ((Integer) oPT[ii]).intValue() == 4;
							pm.setParamIsStart(o[ii].toString(), isStart);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private String manageIndep(ParametricModel pm, ResultSet rs) throws SQLException {
		String result = null;
		Array array = rs.getArray(Bfrdb.ATT_INDEP);
		Array min = rs.getArray(Bfrdb.ATT_MININDEP);
		Array max = rs.getArray(Bfrdb.ATT_MAXINDEP);
		Array category = rs.getArray("IndepCategory");
		Array unit = rs.getArray("IndepUnit");
		Array desc = rs.getArray("IndepDescription");
		if (array != null) {
			try {
				Object[] o = (Object[]) array.getArray();
				Object[] mi = (Object[]) min.getArray();
				Object[] ma = (Object[]) max.getArray();
				Object[] c = (Object[]) category.getArray();
				Object[] u = (Object[]) unit.getArray();
				Object[] d = (Object[]) desc.getArray();
				if (o != null && o.length > 0) {
					for (int i = 0; i < o.length; i++) {
						pm.addIndepVar(o[i].toString(), mi == null || mi[i] == null ? null : Double.parseDouble(mi[i].toString()),
								ma == null || ma[i] == null ? null : Double.parseDouble(ma[i].toString()), c == null || c[i] == null ? null : c[i].toString(), u == null
										|| u[i] == null ? null : u[i].toString(), d[i] == null ? null : d[i].toString());
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void stopCellEditing() {
		if (table.isEditing()) {
			table.getCellEditor().stopCellEditing();
		}
	}

	private void parseFormula(ParametricModel oldPM, ParametricModel newPM) {
		String formula = MathUtilities.getFormula(formulaArea.getText(), boundaryArea.getText());
		formula = formula.replaceAll("\n", "");
		formula = formula.replaceAll("\\s", "");
		formula = formula.replace("~", "=").trim();

		int index = formula.indexOf("=");
		if (index < 0) {
			return;
		}

		formulaArea.setText(MathUtilities.getAllButBoundaryCondition(formula));
		boundaryArea.setText(MathUtilities.getBoundaryCondition(formula));
		String depVar = formula.substring(0, index).trim();
		newPM.setDepVar(depVar, true);
		newPM.setDepCategory(oldPM.getDepCategory());
		newPM.setDepDescription(oldPM.getDepDescription());

		DJep parser = MathUtilities.createParser();
		try {
			parser.parse(formula);
			SymbolTable st = parser.getSymbolTable();
			for (Object o : st.keySet()) {
				String os = o.toString();
				if (!os.equals(depVar)) {
					if (oldPM.containsIndep(os)) {
						newPM.addIndepVar(os, oldPM.getIndepMin(os), oldPM.getIndepMax(os), oldPM.getIndepCategory(os), oldPM.getIndepUnit(os), oldPM.getIndepDescription(os));
					} else if (oldPM.containsParam(os)) {
						newPM.addParam(os, oldPM.getParamIsStart(os), oldPM.getParamValue(os), oldPM.getParamError(os), oldPM.getParamMin(os), oldPM.getParamMax(os), oldPM.getParamCategory(os),
								oldPM.getParamUnit(os), oldPM.getParamDescription(os));
					} else {
						newPM.addParam(os, null);
					}
				}
			}
		} catch (ParseException e) {
			if (!e.getErrorInfo().startsWith("Unexpected \"<EOF>\"") && !e.getErrorInfo().startsWith("Encountered \"-\" at")) {
				e.printStackTrace();
			}
		}
	}

	private void modelNameBoxActionPerformed(ActionEvent e) {
		if (dontTouch) return;
		table.clearTable();
		// if (!dontRemoveSec && m_secondaryModels != null)
		// m_secondaryModels.clear();
		formulaArea.setText("");
		boundaryArea.setText("");
		modelnameField.setText("");
		fittedModelName.setText("");

		ParametricModel pm = (ParametricModel) modelNameBox.getSelectedItem();
		if (pm != null) {
			setPM(pm);
		} else if (modelNameBox.getItemCount() > 1) {
			System.err.println("pm = null???\t" + modelNameBox.getSelectedItem() + "\t" + modelNameBox.getItemCount());
		} else {
			// System.err.println("pm = null???\t" +
			// modelNameBox.getItemCount());
		}
		if (pm != null && !m_secondaryModels.containsKey(pm)) {
			m_secondaryModels.put(pm, new HashMap<String, ParametricModel>());
		}
	}

	private void insertNselectPMintoBox(ParametricModel pm) {
		int i = 0;
		for (i = 0; i < modelNameBox.getItemCount(); i++) {
			if (pm.modelId == modelNameBox.getItemAt(i).modelId) {
				// if (pm.hashCode() == ((ParametricModel)
				// modelNameBox.getItemAt(i)).hashCode()) {
				break;
			}
		}
		dontTouch = true;
		if (i == modelNameBox.getItemCount()) {
			modelNameBox.addItem(pm);
			// System.err.println("added3:" + pm + "\t" + pm.hashCode());
		} else if (!pm.equals(modelNameBox.getItemAt(i))) {
			modelNameBox.removeItemAt(i);
			modelNameBox.insertItemAt(pm, i);
		}
		modelNameBox.setSelectedItem(pm);
		dontTouch = false;
	}

	private void formulaAreaFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		String newFormula = MathUtilities.getFormula(formulaArea.getText(), boundaryArea.getText());

		if (pm != null && !pm.getFormula().equals(newFormula)) {
			String newMN = getNewModelname(pm);
			ParametricModel newPM = new ParametricModel(newMN, newFormula, pm.getDepXml(), pm.getLevel(), MathUtilities.getRandomNegativeInt());
			newPM.modelClass = pm.modelClass;
			refreshRefsInPM(newPM);

			insertNselectPMintoBox(newPM);
			parseFormula(pm, newPM);
			cloneSecondary(pm, newPM);
			modelNameBox.setSelectedItem(newPM);
			table.repaint();
		}
	}

	private void formulaAreaKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			formulaAreaFocusLost(null);
		}
	}

	private String getNewModelname(ParametricModel pm) {
		if (pm == null) return null;
		String mn = pm.modelName;
		if (modelNameChangedManually) return mn;
		int lio = mn.lastIndexOf(" (v");
		String result = mn;
		try {
			if (lio >= 0) {
				String number = mn.substring(lio + 3, mn.length() - 1);
				Long.parseLong(number);
				result = mn.substring(0, lio);
			}
		} catch (Exception e) {
		}
		result += " (v" + System.currentTimeMillis() + ")";
		return result;
	}

	private void modelnameFieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null && !pm.modelName.equals(modelnameField.getText())) {
			ParametricModel newPM = pm.clone();
			newPM.modelName = modelnameField.getText();
			newPM.modelId = MathUtilities.getRandomNegativeInt();
			cloneSecondary(pm, newPM);
			/*
			 * System.err.println(pm.getParamValue("a0") + "\t" + pm + "\t" +
			 * pm.hashCode() + "\n" + newPM.getParamValue("a0") + "\t" + newPM +
			 * "\t" + newPM.hashCode() + "\n" + table.getValueAt(0, 2) + "\t" +
			 * table.getPM() + "\t" + table.getPM().hashCode() + "\t" +
			 * table.getPM().getParamValue("a0"));
			 */
			insertNselectPMintoBox(newPM);
			modelNameBox.setSelectedItem(newPM);
			modelNameChangedManually = true;
		}
	}

	private void cloneSecondary(ParametricModel pm, ParametricModel newPM) {
		HashMap<String, ParametricModel> smOld = m_secondaryModels.get(pm);
		if (smOld != null && !m_secondaryModels.containsKey(newPM)) {
			HashMap<String, ParametricModel> smNew = new HashMap<>();
			for (String key : smOld.keySet()) {
				if (smOld.get(key) != null) smNew.put(key, smOld.get(key).clone());
			}
			m_secondaryModels.put(newPM, smNew);
		}
	}

	private void modelnameFieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			modelnameFieldFocusLost(null);
		}
	}

	private int getLastClickedCol(final MouseEvent e, JTable table) {
		int lastClickedCol;
		int val = 0;
		for (lastClickedCol = 0; lastClickedCol < table.getColumnCount(); lastClickedCol++) {
			val += table.getColumnModel().getColumn(lastClickedCol).getWidth();
			if (val >= e.getX()) {
				break;
			}
		}
		return lastClickedCol;
	}

	private int getLastClickedRow(final MouseEvent e, JTable table) {
		int lastClickedRow;// = e.getY()/this.getTable().getRowHeight();
		int val = 0;
		for (lastClickedRow = 0; lastClickedRow < table.getRowCount(); lastClickedRow++) {
			val += table.getRowHeight(lastClickedRow);
			if (val >= e.getY()) {
				break;
			}
		}
		return lastClickedRow;
	}

	private void tableMouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			/*
			 * int row = table.getSelectedRow(); int col =
			 * table.getSelectedColumn();
			 */
			int row = getLastClickedRow(e, table);
			int col = getLastClickedCol(e, table);
			if (col == 1) {
				ParametricModel pm = getPM();
				if (pm != null) {
					String param = table.getValueAt(row, col).toString();
					String defCategory, defUnit;
					Object isIndep = table.getValueAt(row, 2);
					if (row == 0) {
						if (e.isShiftDown()) {
							pm.setDepCategory(null);
							pm.setDepUnit(null);
							return;
						}
						defCategory = pm.getDepCategory();
						defUnit = pm.getDepUnit();
					} else {
						if (isIndep != null && isIndep instanceof Boolean && ((Boolean) isIndep)) {
							if (e.isShiftDown()) {
								pm.setIndepCategory(param, null);
								pm.setIndepUnit(param, null);
								return;
							}
							defCategory = pm.getIndepCategory(param);
							defUnit = pm.getIndepUnit(param);
						} else {
							if (e.isShiftDown()) {
								pm.setParamCategory(param, null);
								pm.setParamUnit(param, null);
								return;
							}
							defCategory = pm.getParamCategory(param);
							defUnit = pm.getParamUnit(param);
						}
					}

					String categoryStr = (String) JOptionPane.showInputDialog(table, "Bitte eine Einheitenkategorie angeben für " + param + ":", "Einheit des Parameters " + param,
							JOptionPane.QUESTION_MESSAGE, null, Categories.getAllCategories().toArray(), defCategory);

					if (categoryStr != null) {
						Category category = Categories.getCategory(categoryStr);
						String unit = (String) JOptionPane.showInputDialog(null, "Bitte eine Einheit angeben für " + param + " (Kategorie: " + categoryStr + "):",
								"Einheit des Parameters " + param, JOptionPane.QUESTION_MESSAGE, null, category.getAllUnits().toArray(new String[0]),
								defUnit != null && !defUnit.isEmpty() ? defUnit : category.getStandardUnit());

						//if (unit != null) {
						if (row == 0) {
							pm.setDepCategory(categoryStr);
							pm.setDepUnit(unit);
						} else if (isIndep != null && isIndep instanceof Boolean && ((Boolean) isIndep)) {
							pm.setIndepCategory(param, categoryStr);
							pm.setIndepUnit(param, unit);
						} else {
							pm.setParamCategory(param, categoryStr);
							pm.setParamUnit(param, unit);
						}
						//}
					}
				}
			} else if (col == 0 && row > 0 && radioButton3.isSelected()) {
				Object isIndep = table.getValueAt(row, 2);
				if (isIndep == null || isIndep instanceof Boolean && !((Boolean) isIndep)) {
					SecDialog secondaryDialog = new SecDialog(m_parentFrame);
					secondaryDialog.setModal(true);
					secondaryDialog.setIconImage(Resources.getInstance().getDefaultIcon());
					String param = table.getValueAt(row, 0).toString();
					MMC_M m2 = new MMC_M(null, 2, param, formulaCreator, null);
					m2.setEditor(isEditor);
					m2.setConnection(m_conn);
					HashMap<String, ParametricModel> sm = m_secondaryModels.get(table.getPM());
					m2.setPM(sm.get(param));
					secondaryDialog.setPanel(m2, param, sm);
					secondaryDialog.pack();

					secondaryDialog.setLocationRelativeTo(this);
					// secondaryDialog.setAlwaysOnTop(true);
					secondaryDialog.setVisible(true);

				}
			}
		}
	}

	private void radioButtonActionPerformed(ActionEvent e) {
		if (m_conn != null) {
			/*
			 * int level = radioButton2.isSelected() ? 2 : 1; ParametricModel pm
			 * = table.getPM();
			 * 
			 * //if (!radioButton3.isSelected()) m_secondaryModels.clear(); if
			 * (level != pm.getLevel())
			 */

			setComboBox();
			modelNameBox.repaint();
			this.revalidate();
			modelNameBoxActionPerformed(null);
		}
	}

	private int getSelRadio() {
		return radioButton1.isSelected() ? 1 : radioButton2.isSelected() ? 2 : 3;
	}

	private ParametricModel finalizePM(int lastSelIndex) {
		ParametricModel pm = table.getPM();
		if (table.hasChanged()) {
			if (pm != null && pm.modelName.equals(modelnameField.getText())) {
				modelnameField.setText(getNewModelname(pm));
				modelnameFieldFocusLost(null);
				pm = (ParametricModel) modelNameBox.getSelectedItem();
			}
		}
		refreshRefsInPM(pm);

		if (listModel != null && lastSelIndex >= 0 && lastSelIndex < listModel.size()) {
			dontFireList = true;
			listModel.remove(lastSelIndex);
			listModel.add(lastSelIndex, pm);
			// list1.setSelectedIndex(selIndex);
			list1.revalidate();
			dontFireList = false;
		}

		// change other identical secondaryModels
		if (m_secondaryModels.containsKey(pm)) {
			HashMap<String, ParametricModel> hm = m_secondaryModels.get(pm);
			for (ParametricModel pm_ : hm.values()) {
				for (HashMap<String, ParametricModel> hmAll : m_secondaryModels.values()) {
					for (ParametricModel pmAll : hmAll.values()) {
						if (!pm_.equals(pmAll) && pmAll.getEstModelId() == pm_.getEstModelId()) {
							System.err.println("WEW");
						}
					}
				}
			}
		}

		// fetch TS
		if (m_mmcts != null && tss != null) {
			PmmTimeSeries ts = m_mmcts.getTS();
			tss.put(ts.getCondId(), ts);
		}
		return pm;
	}

	public String listToXmlString() throws InvalidSettingsException {
		PmmXmlDoc doc = listToDoc();
		if (doc == null) return "";
		return doc.toXmlString();
	}
	public PmmXmlDoc listToDoc() throws InvalidSettingsException {
		if (listModel == null || listModel.size() == 0) {
			if (m_mmcts != null && m_mmcts.getCondId() != null) getPM().condId = m_mmcts.getCondId();
			return toDoc();
		}
		PmmXmlDoc doc = new PmmXmlDoc();
		for (int i = 0; i < listModel.size(); i++) {
			if (list1.isSelectedIndex(i)) finalizePM(i);
			ParametricModel pm1 = listModel.get(i);

			if (pm1.getIndependent().getElementSet().isEmpty()) {
				throw new InvalidSettingsException("No Independent Variable is specified");
			}

			doc.add(pm1);
			if (m_secondaryModels.containsKey(pm1)) {
				for (Map.Entry<String, ParametricModel> entry : m_secondaryModels.get(pm1).entrySet()) {
					String key = entry.getKey();
					if (pm1.containsParam(key)) {
						ParametricModel value = entry.getValue();
						doc.add(value);
					}
				}
			}
		}
		return doc;
	}

	public String tssToXmlString() {
		PmmXmlDoc doc = tssToDoc();
		if (doc == null) return "";
		return doc.toXmlString();
	}
	public PmmXmlDoc tssToDoc() {
		if (formulaCreator) return null;
		PmmXmlDoc doc = new PmmXmlDoc();
		if (tss == null) {
			PmmTimeSeries ts = m_mmcts.getTS();
			doc.add(ts);
		} else {
			for (PmmTimeSeries ts : tss.values()) {
				doc.add(ts);
			}
		}
		return doc;
	}

	private PmmXmlDoc toDoc() throws InvalidSettingsException {
		PmmXmlDoc doc = new PmmXmlDoc();

		ParametricModel pm = finalizePM(-1);

		if (pm.getLevel() == 1 && pm.getIndependent().getElementSet().isEmpty()) {
			throw new InvalidSettingsException("No Independent Variable is specified");
		}

		doc.add(pm);

		if (radioButton3.isSelected()) {
			for (Map.Entry<String, ParametricModel> entry : m_secondaryModels.get(pm).entrySet()) {
				String key = entry.getKey();
				if (pm.containsParam(key)) {
					ParametricModel value = entry.getValue();
					doc.add(value);
				}
			}
		}
		return doc;
	}

	public void setInputData(Collection<ParametricModel> m1s, HashMap<ParametricModel, HashMap<String, ParametricModel>> m_secondaryModels, HashMap<Integer, PmmTimeSeries> tss) {
		this.tss = tss;
		this.m_secondaryModels = m_secondaryModels;
		if (m1s.size() == 0) {
			scrollPane3.setVisible(false);
			if (m_secondaryModels.size() > 0) {
				for (Entry<ParametricModel, HashMap<String, ParametricModel>> e : m_secondaryModels.entrySet()) {
					for (Entry<String, ParametricModel> hm : e.getValue().entrySet()) {
						setPM(hm.getValue());
						break;
					}
					break;
				}
			}
			table.repaint();
		}
		else {
			dontFireList = true;
			listModel = new DefaultListModel<>();
			list1.setModel(listModel);
			for (ParametricModel pm : m1s) {
				listModel.addElement(pm);
			}
			scrollPane3.setVisible(true);
			isEditor = true;
			dontFireList = false;
			if (listModel.getSize() > 0) list1.setSelectedIndex(0);
			list1.setVisible(true);
		}
	}

	public void setFromXmlString(final String xmlString) {
		try {
			PmmXmlDoc doc = new PmmXmlDoc(xmlString);
			// fetch model set
			ParametricModel theModel = null;
			HashMap<String, ParametricModel> sm = new HashMap<>();
			for (int i = 0; i < doc.size(); i++) {
				PmmXmlElementConvertable el = doc.get(i);
				if (el instanceof ParametricModel) {
					ParametricModel pm = (ParametricModel) el;

					if (pm.getLevel() == 1) {
						theModel = pm;
					} else {
						if (theModel == null) theModel = pm;
						sm.put(pm.getDepVar(), pm);
					}
				}
			}

			if (theModel != null) {
				if (theModel.getLevel() == 1) {
					m_secondaryModels.put(theModel, sm);
				}
				if (sm.size() > 0) {
					radioButton3.setSelected(true);
				}
				setComboBox();
				setPM(theModel);

				if (theModel.getLevel() == 2) radioButton2.setSelected(true);
				else if (sm.size() > 0) radioButton3.setSelected(true);
				else radioButton1.setSelected(true);
			}
			/*
			 * List<Integer> li = new ArrayList<Integer>(); for( LiteratureItem
			 * item : primModel.getModelLit()) { li.add(item.getId()); } for(
			 * LiteratureItem item : primModel.getEstModelLit()) {
			 * li.add(item.getId()); } ParametricModel pmc = modelCatalog.get(
			 * modelNameBox.getSelectedItem() ); modLitMat.put(pmc.getModelId(),
			 * li);
			 * 
			 * updatePossibleLiterature(); updateLiterature();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void button1ActionPerformed(ActionEvent e) {
		// New
		boolean b = (formulaCreator || !table.isEstimated());
		doLit(null, !b);
	}

	private void button2ActionPerformed(ActionEvent e) {
		// Delete
		deleteSelLitRow();
	}

	private void button3ActionPerformed(ActionEvent e) {
		// Edit
		LiteratureItem li = (LiteratureItem) referencesTable.getValueAt(referencesTable.getSelectedRow(), 1);
		Boolean b = (Boolean) referencesTable.getValueAt(referencesTable.getSelectedRow(), 0);
		// if (li != null) doLit(li.getId());
		doLit(li, b);
	}

	private void deleteSelLitRow() {
		((DefaultTableModel) referencesTable.getModel()).removeRow(referencesTable.getSelectedRow());
		refreshRefsInPM(getPM());
	}

	private void refreshRefsInPM(ParametricModel pm) {
		pm.removeEstModelLits();
		pm.removeModelLits();
		for (int i = 0; i < referencesTable.getRowCount(); i++) {
			LiteratureItem li = (LiteratureItem) referencesTable.getValueAt(i, 1);
			Boolean b = (Boolean) referencesTable.getValueAt(i, 0);
			if (!b) pm.addModelLit(li);
			else pm.addEstModelLit(li);
		}
	}

	private void doLit(LiteratureItem oldLi, Boolean isEM) {
		MyTable lit = DBKernel.myDBi.getTable("Literatur");
		Integer litID = (oldLi != null && (dbuuid != null && dbuuid.equals(oldLi.dbuuid))) ? oldLi.id : null;
		Integer newVal = (Integer) DBKernel.mainFrame.openNewWindow(lit, litID, (Object) "Literatur", null, 1, 1, null, true, null, this);
		if (newVal != null && newVal instanceof Integer) {
			LiteratureItem li = DBUtilities.getLiteratureItem(newVal);
			Object[] o = new Object[2];
			o[0] = isEM;
			o[1] = li;
			//Vector<LiteratureItem> vli = new Vector<>();
			//vli.add(li);
			if (oldLi != null) {
				int selRow = referencesTable.getSelectedRow();
				deleteSelLitRow();
				((DefaultTableModel) referencesTable.getModel()).insertRow(selRow, o);
			} else {
				((DefaultTableModel) referencesTable.getModel()).addRow(o);
			}
		}
		refreshRefsInPM(getPM());
	}

	private void r2FieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			try {
				pm.setRsquared(r2Field.getValue());
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void r2FieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			r2FieldFocusLost(null);
		}
	}

	private void rmsFieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			try {
				pm.setRms(rmsField.getValue());
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void rmsFieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			rmsFieldFocusLost(null);
		}
	}

	private void aicFieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			pm.setAic(aicField.getValue());
		}
	}

	private void aicFieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			aicFieldFocusLost(null);
		}
	}

	private void bicFieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			pm.setBic(bicField.getValue());
		}
	}

	private void bicFieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			bicFieldFocusLost(null);
		}
	}

	private void qScoreBoxActionPerformed(ActionEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			try {
				pm.qualityScore = qScoreBox.getSelectedIndex();
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void checkBox1ActionPerformed(ActionEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			try {
				pm.isChecked = checkBox1.isSelected();
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void list1ValueChanged(ListSelectionEvent e) {
		if (!dontFireList && !e.getValueIsAdjusting() && list1.getSelectedIndex() >= 0) {
			if (list1.getSelectedIndex() != e.getFirstIndex()) finalizePM(e.getFirstIndex());
			ParametricModel pm1 = list1.getSelectedValue();
			if (m_secondaryModels.get(pm1) != null && m_secondaryModels.get(pm1).size() > 0) {
				radioButton3.setSelected(true);
			}
			setComboBox();
			setPM(pm1);

			m_mmcts.setTS(tss.get(pm1.condId));
			
			table.repaint();
		}
	}

	private void fittedModelNameFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			try {
				if (!isEditor) pm.setEstModelId(MathUtilities.getRandomNegativeInt());
				pm.setFittedModelName(fittedModelName.getText());
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void fittedModelNameKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			fittedModelNameFocusLost(null);
		}
	}

	private void button4ActionPerformed(ActionEvent e) {
		loadFromDB();
	}

	private void loadFromDB() {
		ParametricModel ppm = table.getPM();
		modelNameBox.removeAllItems();
		// if (m_secondaryModels != null) m_secondaryModels.clear();
		int level = radioButton2.isSelected() ? 2 : 1;
		ParametricModel pm = new ParametricModel(LABEL_OWNMODEL, "", null, level);
		modelNameBox.addItem(pm);
		boolean addPreviousSelectedPM = (ppm != null && !ppm.modelName.equals(LABEL_OWNMODEL) && (ppm.getLevel() == 2 && getSelRadio() == 2 || pm.getLevel() == 1
				&& getSelRadio() != 2));
		if (addPreviousSelectedPM) modelNameBox.addItem(ppm);
		// System.err.println("added1:" + pm + "\t" + pm.hashCode());
		try {
			Bfrdb db = new Bfrdb(m_conn);
			ResultSet result = db.selectModel(level);
			while (result.next()) {
				int modelID = result.getInt(Bfrdb.ATT_MODELID);
				Object visible = DBKernel.getValue(db.getConnection(), "Modellkatalog", "ID", "" + modelID, "visible");
				if (visible == null || (visible instanceof Boolean && (Boolean) visible)) {
					String modelName = result.getString(Bfrdb.ATT_NAME);
					String formula = result.getString("Formel");
					String depDesc = result.getString("DepDescription");

					DepXml dx = new DepXml(result.getString(Bfrdb.ATT_DEP), result.getString("DepCategory"), result.getString("DepUnit"));
					dx.description = depDesc;
					pm = new ParametricModel(modelName, formula, dx, level, modelID);
					pm.setMDbUuid(db.getDBUUID());
					pm.modelClass = result.getInt("Klasse");
					pm.setDepDescription(depDesc);
					String s = result.getString("LitMID");
					if (s != null) pm.setMLit(db.getLiteratureXml(s, db.getDBUUID()));
					manageDBMinMax(result, pm);
					manageIndep(pm, result);

					modelNameBox.addItem(pm);
					// System.err.println("added2:" + pm + "\t" +
					// pm.hashCode());
				}
			}
			result.getStatement().close();
			result.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (addPreviousSelectedPM) modelNameBox.setSelectedItem(ppm);
	}

	private void typeBoxActionPerformed(ActionEvent e) {
		String typeString = (String) typeBox.getSelectedItem();
		Integer type = null;

		for (Map.Entry<Object, String> entry : DBKernel.myDBi.getHashMap("ModelType").entrySet()) {
			if (entry.getValue().equals(typeString)) {
				type = (Integer) entry.getKey();
			}
		}

		if (table != null && table.getPM() != null) {
			table.getPM().modelClass = type;
		}
	}

	private void textField1FocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			try {
				pm.comment = textField1.getText();
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void textField1KeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			textField1FocusLost(null);
		}
	}

	@SuppressWarnings({ "serial" })
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		depVarLabel = new JLabel();
		scrollPane3 = new JScrollPane();
		list1 = new JList<>();
		label7 = new JLabel();
		radioButton1 = new JRadioButton();
		radioButton2 = new JRadioButton();
		radioButton3 = new JRadioButton();
		modelNameLabel = new JLabel();
		modelNameBox = new JComboBox<>();
		button4 = new JButton();
		label1 = new JLabel();
		modelnameField = new JTextField();
		typeLabel = new JLabel();
		typeBox = new JComboBox<>();
		label2 = new JLabel();
		formulaArea = new JTextField();
		formulaApply = new JButton();
		label13 = new JLabel();
		boundaryArea = new JTextField();
		boundaryApply = new JButton();
		tableLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		table = new ModelTableModel();
		label12 = new JLabel();
		fittedModelName = new JTextField();
		label8 = new JLabel();
		label3 = new JLabel();
		r2Field = new DoubleTextField(true);
		label4 = new JLabel();
		rmsField = new DoubleTextField(true);
		label5 = new JLabel();
		aicField = new DoubleTextField(true);
		label6 = new JLabel();
		bicField = new DoubleTextField(true);
		scrollPane2 = new JScrollPane();
		referencesTable = new JTable();
		label9 = new JLabel();
		button1 = new JButton();
		button3 = new JButton();
		button2 = new JButton();
		label10 = new JLabel();
		label11 = new JLabel();
		qScoreBox = new JComboBox<Color>(new Color[] {Color.WHITE, Color.GREEN, Color.YELLOW, Color.RED});
		qScoreBox.setRenderer(new DefaultListCellRenderer() {
					private static final long serialVersionUID = 1L;

					private Color color = Color.WHITE;
					private boolean isSelected = false;

					@Override
					public Component getListCellRendererComponent(JList<?> list,
							Object value, int index, boolean isSelected,
							boolean cellHasFocus) {
						color = (Color) value;
						this.isSelected = isSelected;

						return super.getListCellRendererComponent(list, value, index,
								isSelected, cellHasFocus);
					}

					@Override
					protected void paintComponent(Graphics g) {
						Rectangle rect = g.getClipBounds();

						if (rect != null) {
							g.setColor(color);
							g.fillRect(rect.x, rect.y, rect.width, rect.height);

							if (isSelected) {
								g.setColor(UIManager.getDefaults().getColor(
										"List.selectionBackground"));
							} else {
								g.setColor(UIManager.getDefaults().getColor(
										"List.background"));
							}

							((Graphics2D) g).setStroke(new BasicStroke(5));
							g.drawRect(rect.x, rect.y, rect.width, rect.height);
						}
					}
		});
		checkBox1 = new JCheckBox();
		label14 = new JLabel();
		scrollPane4 = new JScrollPane();
		textField1 = new JTextArea();

		//======== this ========
		setBorder(new CompoundBorder(
			new TitledBorder("Model Properties"),
			Borders.DLU2));
		setLayout(new FormLayout(
			"4*(default, $lcgap), default:grow, 2*($lcgap, default), $lcgap, default:grow, 2*($lcgap, default), $lcgap, default:grow",
			"default, $rgap, default, $ugap, 2*(default, $pgap), default, $lgap, 2*(default, $ugap), default, $lgap, default, $ugap, default, $lgap, fill:default:grow, 1dlu, default, $pgap, 35dlu"));
		((FormLayout)getLayout()).setColumnGroups(new int[][] {{5, 11, 17}, {7, 13, 19}, {9, 15, 21}});

		//---- depVarLabel ----
		depVarLabel.setText("Parameter");
		depVarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		depVarLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(depVarLabel, CC.xywh(3, 1, 19, 1));

		//======== scrollPane3 ========
		{

			//---- list1 ----
			list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list1.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					list1ValueChanged(e);
				}
			});
			scrollPane3.setViewportView(list1);
		}
		add(scrollPane3, CC.xywh(1, 1, 1, 25));

		//---- label7 ----
		label7.setText("Model type:");
		add(label7, CC.xy(3, 3));

		//---- radioButton1 ----
		radioButton1.setText("primary");
		radioButton1.setSelected(true);
		radioButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton1, CC.xywh(5, 3, 5, 1));

		//---- radioButton2 ----
		radioButton2.setText("secondary");
		radioButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton2, CC.xywh(11, 3, 5, 1));

		//---- radioButton3 ----
		radioButton3.setText("primary (secondary)");
		radioButton3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton3, CC.xywh(17, 3, 5, 1));

		//---- modelNameLabel ----
		modelNameLabel.setText("Formula from DB:");
		add(modelNameLabel, CC.xy(3, 5));

		//---- modelNameBox ----
		modelNameBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modelNameBoxActionPerformed(e);
			}
		});
		add(modelNameBox, CC.xywh(5, 5, 15, 1));

		//---- button4 ----
		button4.setText("Refresh");
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button4ActionPerformed(e);
			}
		});
		add(button4, CC.xy(21, 5));

		//---- label1 ----
		label1.setText("Formula Name:");
		add(label1, CC.xy(3, 7));

		//---- modelnameField ----
		modelnameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				modelnameFieldFocusLost(e);
			}
		});
		modelnameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				modelnameFieldKeyReleased(e);
			}
		});
		add(modelnameField, CC.xywh(5, 7, 11, 1));

		//---- typeLabel ----
		typeLabel.setText("Type:");
		typeLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		add(typeLabel, CC.xy(17, 7));

		//---- typeBox ----
		typeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				typeBoxActionPerformed(e);
			}
		});
		add(typeBox, CC.xywh(19, 7, 3, 1));

		//---- label2 ----
		label2.setText("Formula:");
		add(label2, CC.xy(3, 9));

		//---- formulaArea ----
		formulaArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				formulaAreaFocusLost(e);
			}
		});
		formulaArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				formulaAreaKeyReleased(e);
			}
		});
		add(formulaArea, CC.xywh(5, 9, 15, 1));

		//---- formulaApply ----
		formulaApply.setText("Apply");
		add(formulaApply, CC.xy(21, 9));

		//---- label13 ----
		label13.setText("Boundary Conditions:");
		add(label13, CC.xy(3, 11));

		//---- boundaryArea ----
		boundaryArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				formulaAreaFocusLost(e);
			}
		});
		boundaryArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				formulaAreaKeyReleased(e);
			}
		});
		add(boundaryArea, CC.xywh(5, 11, 15, 1));

		//---- boundaryApply ----
		boundaryApply.setText("Apply");
		add(boundaryApply, CC.xy(21, 11));

		//---- tableLabel ----
		tableLabel.setText("Parameter Definition:");
		add(tableLabel, CC.xy(3, 13));

		//======== scrollPane1 ========
		{

			//---- table ----
			table.setPreferredScrollableViewportSize(new Dimension(450, 175));
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					tableMouseClicked(e);
				}
			});
			scrollPane1.setViewportView(table);
		}
		add(scrollPane1, CC.xywh(5, 13, 17, 1));

		//---- label12 ----
		label12.setText("Model Name:");
		add(label12, CC.xy(3, 15));

		//---- fittedModelName ----
		fittedModelName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				fittedModelNameFocusLost(e);
			}
		});
		fittedModelName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				fittedModelNameKeyReleased(e);
			}
		});
		add(fittedModelName, CC.xywh(5, 15, 17, 1));

		//---- label8 ----
		label8.setText("Goodness of fit:");
		add(label8, CC.xywh(3, 17, 1, 3));

		//---- label3 ----
		label3.setText("R\u00b2:");
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		add(label3, CC.xy(5, 17));

		//---- r2Field ----
		r2Field.setColumns(7);
		r2Field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				r2FieldFocusLost(e);
			}
		});
		r2Field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				r2FieldKeyReleased(e);
			}
		});
		add(r2Field, CC.xy(7, 17));

		//---- label4 ----
		label4.setText("RMS:");
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		add(label4, CC.xy(11, 17));

		//---- rmsField ----
		rmsField.setColumns(7);
		rmsField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				rmsFieldFocusLost(e);
			}
		});
		rmsField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				rmsFieldKeyReleased(e);
			}
		});
		add(rmsField, CC.xy(13, 17));

		//---- label5 ----
		label5.setText("AIC:");
		add(label5, CC.xy(5, 19));

		//---- aicField ----
		aicField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				aicFieldFocusLost(e);
			}
		});
		aicField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				aicFieldKeyReleased(e);
			}
		});
		add(aicField, CC.xy(7, 19));

		//---- label6 ----
		label6.setText("BIC:");
		add(label6, CC.xy(11, 19));

		//---- bicField ----
		bicField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				bicFieldFocusLost(e);
			}
		});
		bicField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				bicFieldKeyReleased(e);
			}
		});
		add(bicField, CC.xy(13, 19));

		//======== scrollPane2 ========
		{
			scrollPane2.setPreferredSize(new Dimension(452, 120));

			//---- referencesTable ----
			referencesTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"EstModel", "Reference"
				}
			) {
				Class<?>[] columnTypes = new Class<?>[] {
					Boolean.class, Object.class
				};
				boolean[] columnEditable = new boolean[] {
					true, false
				};
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return columnEditable[columnIndex];
				}
			});
			{
				TableColumnModel cm = referencesTable.getColumnModel();
				cm.getColumn(0).setMaxWidth(100);
				cm.getColumn(0).setPreferredWidth(60);
			}
			scrollPane2.setViewportView(referencesTable);
		}
		add(scrollPane2, CC.xywh(5, 21, 17, 1));

		//---- label9 ----
		label9.setText("References:");
		add(label9, CC.xywh(3, 21, 1, 3));

		//---- button1 ----
		button1.setText("New Reference");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});
		add(button1, CC.xywh(5, 23, 5, 1));

		//---- button3 ----
		button3.setText("Edit Reference");
		button3.setEnabled(false);
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button3ActionPerformed(e);
			}
		});
		add(button3, CC.xywh(11, 23, 5, 1));

		//---- button2 ----
		button2.setText("Delete Reference");
		button2.setEnabled(false);
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button2ActionPerformed(e);
			}
		});
		add(button2, CC.xywh(17, 23, 5, 1));

		//---- label10 ----
		label10.setText("Subjective quality:");
		add(label10, CC.xy(3, 25));

		//---- label11 ----
		label11.setText("QualityScore:");
		add(label11, CC.xywh(5, 25, 3, 1));

		//---- qScoreBox ----
		qScoreBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				qScoreBoxActionPerformed(e);
			}
		});
		add(qScoreBox, CC.xy(9, 25));

		//---- checkBox1 ----
		checkBox1.setText("Checked");
		checkBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkBox1ActionPerformed(e);
			}
		});
		add(checkBox1, CC.xy(13, 25));

		//---- label14 ----
		label14.setText("Comment:");
		label14.setHorizontalAlignment(SwingConstants.RIGHT);
		add(label14, CC.xywh(15, 25, 3, 1));

		//======== scrollPane4 ========
		{

			//---- textField1 ----
			textField1.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					textField1FocusLost(e);
				}
			});
			textField1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					textField1KeyReleased(e);
				}
			});
			scrollPane4.setViewportView(textField1);
		}
		add(scrollPane4, CC.xywh(19, 25, 3, 1, CC.DEFAULT, CC.FILL));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(radioButton1);
		buttonGroup1.add(radioButton2);
		buttonGroup1.add(radioButton3);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel depVarLabel;
	private JScrollPane scrollPane3;
	private JList<ParametricModel> list1;
	private JLabel label7;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JRadioButton radioButton3;
	private JLabel modelNameLabel;
	private JComboBox<ParametricModel> modelNameBox;
	private JButton button4;
	private JLabel label1;
	private JTextField modelnameField;
	private JLabel typeLabel;
	private JComboBox<String> typeBox;
	private JLabel label2;
	private JTextField formulaArea;
	private JButton formulaApply;
	private JLabel label13;
	private JTextField boundaryArea;
	private JButton boundaryApply;
	private JLabel tableLabel;
	private JScrollPane scrollPane1;
	private ModelTableModel table;
	private JLabel label12;
	private JTextField fittedModelName;
	private JLabel label8;
	private JLabel label3;
	private DoubleTextField r2Field;
	private JLabel label4;
	private DoubleTextField rmsField;
	private JLabel label5;
	private DoubleTextField aicField;
	private JLabel label6;
	private DoubleTextField bicField;
	private JScrollPane scrollPane2;
	private JTable referencesTable;
	private JLabel label9;
	private JButton button1;
	private JButton button3;
	private JButton button2;
	private JLabel label10;
	private JLabel label11;
	private JComboBox<Color> qScoreBox;
	private JCheckBox checkBox1;
	private JLabel label14;
	private JScrollPane scrollPane4;
	private JTextArea textField1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
