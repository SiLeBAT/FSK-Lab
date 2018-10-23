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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

public class ModelReaderUi extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 20120828;

	public static final String PARAM_LEVEL = "level";
	public static final String PARAM_MODELCLASS = "modelClass";
	public static final String PARAM_MODELFILTERENABLED = "modelFilterEnabled";
	public static final String PARAM_MODELLISTINT = "modelListInt";

	protected static final String LABEL_UNSPEC = "Unspecified only";
	private static final String LABEL_PRIM = "Primary";
	private static final String LABEL_SEC = "Secondary";
	private static final String LABEL_TERT = "Combined Primary/Secondary";
	private static final int LEVEL_PRIM = 1;
	private static final int LEVEL_SEC = 2;

	private JCheckBox modelNameSwitch;
	private JComboBox<String> levelBox, classBox;
	private JPanel modelPanel;
	private JPanel panel;

	private LinkedHashMap<Integer, String> modelTypeSetPrim;
	private LinkedHashMap<Integer, String> modelTypeSetSec;
	private LinkedHashMap<Integer, JCheckBox> modelBoxSetPrim;
	private LinkedHashMap<Integer, JCheckBox> modelBoxSetSec;

	public ModelReaderUi() {
		this(false);
	}
	public ModelReaderUi(boolean fitted) {

		JPanel panel0;

		clearModelSet();

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(300, 200));

		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Level"));
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(250, 45));
		add(panel, BorderLayout.NORTH);

		panel0 = new JPanel();
		panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));
		panel.add(panel0);

		panel0.add(new JLabel("Level   "));

		if (fitted) levelBox = new JComboBox<>(new String[] { LABEL_PRIM, LABEL_TERT, LABEL_SEC });
		else levelBox = new JComboBox<>(new String[] { LABEL_PRIM, LABEL_SEC });
		levelBox.addActionListener(this);
		levelBox.setPreferredSize(new Dimension(50, 25));
		panel0.add(levelBox);

		panel0.add(new JLabel("Type:   "));

		classBox = new JComboBox<>();// new String[] {"All","growth","inactivation","survival"}
		classBox.addItem("All");
		for (String s : DBKernel.myDBi.getHashMap("ModelType").values()) {
			classBox.addItem(s);
		}
		classBox.addActionListener(this);
		classBox.setPreferredSize(new Dimension(50, 25));
		panel0.add(classBox);

		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Model"));
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(250, 150));
		add(panel, BorderLayout.CENTER);

		modelNameSwitch = new JCheckBox("Filter by formula");
		modelNameSwitch.addActionListener(this);
		panel.add(modelNameSwitch, BorderLayout.NORTH);

		/*
		 * modelNameBox = new JComboBox(); modelNameBox.setEnabled( false );
		 * panel.add( modelNameBox, BorderLayout.CENTER );
		 */

		modelPanel = new JPanel();
		modelPanel.setLayout(new GridLayout(0, 1));
		// modelPanel.setPreferredSize( new Dimension( 150, 200 ) );
		JScrollPane pane;

		pane = new JScrollPane(modelPanel);
		// pane.setPreferredSize( new Dimension( 200, 10 ) );
		panel.add(pane);
	}

	public void clearModelSet() {
		modelBoxSetPrim = new LinkedHashMap<>();
		modelBoxSetSec = new LinkedHashMap<>();
		modelTypeSetPrim = new LinkedHashMap<>();
		modelTypeSetSec = new LinkedHashMap<>();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == modelNameSwitch) {
			updateModelNameEnabled();
			return;
		}
		if (arg0.getSource() == levelBox) {
			updateModelName();
			return;
		}
		if (arg0.getSource() == classBox) {
			updateModelName();
			return;
		}
	}

	public void addModelPrim(final int id, final String name, final String modelType, boolean visible) throws PmmException {
		if (visible) {
			if (name == null) throw new PmmException("Model name must not be null.");

			//modelIdPrim.put(name + " (" + modelType + ")", id);
			modelBoxSetPrim.put(id, new JCheckBox(name + " (" + modelType + ")"));
			modelTypeSetPrim.put(id, modelType);
			updateModelName();
		}
	}

	public void addModelSec(final int id, final String name, final String modelType, boolean visible) throws PmmException {
		if (visible) {
			if (name == null) throw new PmmException("Model name must not be null.");

			//modelIdSec.put(name + " (" + modelType + ")", id);
			modelBoxSetSec.put(id, new JCheckBox(name + " (" + modelType + ")"));
			modelTypeSetSec.put(id, modelType);
			updateModelName();
		}
	}

	public int getLevel() {
		return levelBox.getSelectedIndex() + 1;
	}
	public String getModelClass() {
		return classBox.getSelectedItem().toString();
	}

	public boolean isPrim() {
		return getLevel() == LEVEL_PRIM;
	}

	public boolean isSec() {
		return getLevel() == LEVEL_SEC;
	}

	public boolean isModelFilterEnabled() {
		return modelNameSwitch.isSelected();
	}

	private boolean modelNameEnabled(final String name) {
		for (JCheckBox box : modelBoxSetPrim.values()) {
			if (box.getText().equals(name)) return true;		
			if (box.getText().startsWith(name) && box.getText().lastIndexOf(" (") == name.length())  return true;
		}

		for (JCheckBox box : modelBoxSetSec.values()) {
			if (box.getText().equals(name)) return true;			
			if (box.getText().startsWith(name) && box.getText().lastIndexOf(" (") == name.length())  return true;
		}

		return false;
	}

	public void setLevel(int level) throws PmmException {
		//if (!(level == 1 || level == 2)) throw new PmmException("Level must be in {1, 2}");

		if (level == 1 || level == 2 || level == 3) levelBox.setSelectedIndex(level - 1);
	}
	public void setModelClass(String modelClass) throws PmmException {
		classBox.setSelectedItem(modelClass == null || modelClass.isEmpty() ? "All" : modelClass);
	}

	private void updateModelName() {
		modelPanel.removeAll();
		modelPanel.setVisible(false);

		if (isPrim()) addBoxes2Panel(modelBoxSetPrim, modelPanel, modelTypeSetPrim);
		else addBoxes2Panel(modelBoxSetSec, modelPanel, modelTypeSetSec);

		updateModelNameEnabled();

		modelPanel.setVisible(true);
		panel.validate();
	}
	private void addBoxes2Panel(LinkedHashMap<Integer, JCheckBox> modelBox, JPanel modelPanel, LinkedHashMap<Integer, String> modelTypeSet) {
		Object o = classBox.getSelectedItem();
		for (Integer id : modelBox.keySet()) {
			JCheckBox box = modelBox.get(id);
			if (o == null || o.toString().equals("All") || modelTypeSet.get(id).indexOf(o.toString()) >= 0) {
				modelPanel.add(box);			
			}
		}
	}

	private void updateModelNameEnabled() {
		if (modelNameSwitch.isSelected()) {
			for (JCheckBox box : modelBoxSetPrim.values()) box.setEnabled(true);
			for (JCheckBox box : modelBoxSetSec.values()) box.setEnabled(true);
		} else {
			for (JCheckBox box : modelBoxSetPrim.values()) box.setEnabled(false);
			for (JCheckBox box : modelBoxSetSec.values()) box.setEnabled(false);
		}
	}

	public boolean complies(KnimeTuple tuple) throws PmmException {

		if (isModelFilterEnabled()) {
			PmmXmlDoc x = tuple.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_MODELCATALOG, tuple.getSchema().conforms(new Model1Schema()) ? 1 : 2));
			if (x != null) {
				for (PmmXmlElementConvertable el : x.getElementSet()) {
					if (el instanceof CatalogModelXml) {
						CatalogModelXml cmx = (CatalogModelXml) el;
						if (modelNameEnabled(cmx.name)) return true;
						break;
					}
				}
			}
		}
		else
			return true;
		/*
			if (modelNameEnabled(tuple.getString(Model1Schema.ATT_MODELNAME)))
				return true;
*/
		return false;
	}

	public int[] getModelList() {
		HashSet<Integer> ret = new HashSet<>();

		for (Integer key : modelBoxSetPrim.keySet()) {
			if (modelBoxSetPrim.containsKey(key)) {
				JCheckBox box = modelBoxSetPrim.get(key);
				if (box.isSelected()) {
					ret.add(key);
				}
			}
		}

		for (Integer key : modelBoxSetSec.keySet()) {
			if (modelBoxSetSec.containsKey(key)) {
				JCheckBox box = modelBoxSetSec.get(key);
				if (box.isSelected()) {
					ret.add(key);
				}			
			}
		}
		int[] result = new int[ret.size()];
		int i=0;
		for (int id : ret) {
			result[i] = id;
			i++;
		}
		
		return result;
	}

	public void enableModelList(final int[] idlist) {
		if (idlist == null || idlist.length == 0) return;
		// disable everything
		for (JCheckBox box : modelBoxSetPrim.values()) {
			box.setSelected(false);
		}
		for (JCheckBox box : modelBoxSetSec.values()) {
			box.setSelected(false);
		}

		// enable model if appropriate
		for (Integer id : idlist) {
			if (modelBoxSetPrim.containsKey(id)) {
				modelBoxSetPrim.get(id).setSelected(true);
			}
			if (modelBoxSetSec.containsKey(id)) {
				modelBoxSetSec.get(id).setSelected(true);
			}
		}
	}
/*
	@Override
	public String toString() {
		return getModelList();
	}
*/
	public void setModelFilterEnabled(final boolean en) {

		if (en != isModelFilterEnabled())
			modelNameSwitch.doClick();
	}

	public static boolean passesFilter(final String literatureString, final Integer literatureID, final KnimeTuple tuple, final int level) throws PmmException {
		if (literatureString == null || literatureString.trim().isEmpty()) return true;
			PmmXmlDoc litXmlDoc = tuple.getPmmXml(level == 1 ? Model1Schema.ATT_EMLIT : Model2Schema.ATT_EMLIT);
        	for (PmmXmlElementConvertable el : litXmlDoc.getElementSet()) {
        		if (el instanceof LiteratureItem) {
        			LiteratureItem lit = (LiteratureItem) el;
        			if (literatureID > 0) {
        				int id = lit.id;
        				if (literatureID == id) return true;
        			}
        			else {
            			String s = lit.author;
            			String sd = lit.title;
            			if (s == null) s = ""; else s = s.toLowerCase();
            			if (sd == null) sd = ""; else sd = sd.toLowerCase();
            			if (s.equals(literatureString.toLowerCase()) || sd.equals(literatureString.toLowerCase())) return true;
        			}
        		}
        	}
			litXmlDoc = tuple.getPmmXml(level == 1 ? Model1Schema.ATT_MLIT : Model2Schema.ATT_MLIT);
        	for (PmmXmlElementConvertable el : litXmlDoc.getElementSet()) {
        		if (el instanceof LiteratureItem) {
        			LiteratureItem lit = (LiteratureItem) el;
        			if (literatureID > 0) {
        				int id = lit.id;
        				if (literatureID == id) return true;
        			}
        			else {
            			String s = lit.author;
            			String sd = lit.title;
            			if (s == null) s = ""; else s = s.toLowerCase();
            			if (sd == null) sd = ""; else sd = sd.toLowerCase();
            			if (s.equals(literatureString.toLowerCase()) || sd.equals(literatureString.toLowerCase())) return true;
        			}
        		}
        	}
        return false;
	}
	public static boolean passesFilter(final int[] modelList, final KnimeTuple tuple, final int level) throws PmmException {

		if (modelList == null || modelList.length == 0)
			return false;

		Integer id = null;
		PmmXmlDoc x = tuple.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_MODELCATALOG, level));
		if (x != null) {
			for (PmmXmlElementConvertable el : x.getElementSet()) {
				if (el instanceof CatalogModelXml) {
					CatalogModelXml cmx = (CatalogModelXml) el;
					id = cmx.id;
					break;
				}
			}
		}
		/*
		if (tuple.getSchema().conforms(new Model1Schema()))
			id = tuple.getInt(Model1Schema.ATT_MODELID);
		else
			id = tuple.getInt(Model2Schema.ATT_MODELID);
		 */
		for (int candidate : modelList)
			if (candidate == id)
				return true;

		return false;
	}

	public void addLevelListener(ActionListener listener) {
		levelBox.addActionListener(listener);
	}

    public void saveSettingsTo(Config c) {
    	c.addInt( ModelReaderUi.PARAM_LEVEL, getLevel() );
    	c.addString(ModelReaderUi.PARAM_MODELCLASS, getModelClass());
    	c.addBoolean( ModelReaderUi.PARAM_MODELFILTERENABLED, isModelFilterEnabled() );
    	c.addIntArray(ModelReaderUi.PARAM_MODELLISTINT, getModelList());
    }	
	public void setSettings(Config c) throws InvalidSettingsException {		
		setLevel(c.getInt(ModelReaderUi.PARAM_LEVEL, 1));
		setModelClass(c.getString(ModelReaderUi.PARAM_MODELCLASS));
		setModelFilterEnabled(c.getBoolean( ModelReaderUi.PARAM_MODELFILTERENABLED ));
		if (c.containsKey(PARAM_MODELLISTINT)) enableModelList(c.getIntArray(ModelReaderUi.PARAM_MODELLISTINT));
		else if (c.containsKey("modelList")) {
			String ids = c.getString("modelList");
			if (ids != null && ids.length() > 0) {
				String[] token = ids.split(",");
				int[] idis = new int[token.length];
				int i=0;
				for (String s : token)  {
					idis[i] = Integer.parseInt(s);
					i++;
				}
				enableModelList(idis);
			}
		}
	}
}
