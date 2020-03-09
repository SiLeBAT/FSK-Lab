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
package de.bund.bfr.knime.foodprocess.addons;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.util.Matrix;
import de.bund.bfr.knime.util.Agent;


/**
 * @author Armin Weiser
 */
public class AddonC extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3685456178796229005L;
	private List<JButton> l1 = new ArrayList<JButton>(); 
	private List<Matrix> lmat = new ArrayList<Matrix>(); 
	private List<Agent> lage = new ArrayList<Agent>(); 
	private List<DoubleTextField> l2 = new ArrayList<DoubleTextField>(); 
	private List<JComboBox<String>> l3 = new ArrayList<JComboBox<String>>(); 
	private List<JButton> l4 = new ArrayList<JButton>(); 
	private List<JButton> l5 = new ArrayList<JButton>(); 
	
	private boolean forMatrices;
	
    public static void main(final String[] args) {
    	JDialog f = new JDialog();
    	f.add(new AddonC(true));
        f.setVisible(true);
    }
	public AddonC(boolean forMatrices) {		
		this.forMatrices = forMatrices;
		initComponents();
	}

	public void setSettings(Config c, double[] volumeDef, String[] volumeUnitDef) {
		panel1.removeAll();
		l1.clear();
		lmat.clear();
		lage.clear();
		l2.clear();
		l3.clear();
		l4.clear();
		l5.clear();
		if (volumeDef == null || volumeDef.length < 1) {
			l1.add(button);
			if (forMatrices) lmat.add(null); else lage.add(null);
			l2.add(formattedTextField1);
			l3.add(comboBox2);
			l4.add(button2);
			l5.add(button3);	
		}
		else {
			try {
				int[] iarr = (c == null) ? null : c.getIntArray(AddonNodeModel.PARAM_ID);
				String[] narr = (c == null) ? null : c.getStringArray(AddonNodeModel.PARAM_NAME);
				for (int i=0;i<volumeDef.length;i++) {
					buttonPlusActionPerformed(null);
					l2.get(i).setValue(Double.isNaN(volumeDef[i]) ? null : volumeDef[i]);
					if (volumeUnitDef[i] != null) l3.get(i).setSelectedItem(volumeUnitDef[i]);
					if (iarr != null && i < iarr.length && narr != null && i < narr.length) {
						if (forMatrices) {
							Matrix m = new Matrix(narr[i], iarr[i]);
							l1.get(i).setText(m.getName());
							lmat.set(i, m);						
						}
						else {
							Agent a = new Agent(narr[i], iarr[i]);
							l1.get(i).setText(a.getName());
							lage.set(i, a);
						}
					}
				}
			}
			catch (InvalidSettingsException e) {
				e.printStackTrace();
			}
		}
		panel1.setVisible(false);
		removeAllFromPanel();
		addAll2Panel();
		panel1.revalidate();
		panel1.setVisible(true);		
	}
	public double[] getVolumeDef() {
		double[] result = new double[l2.size()];
		for (int i=0;i<l2.size();i++) {
			try {
				result[i] = l2.get(i).getText() == null ? Double.NaN : Double.parseDouble(l2.get(i).getText().replace(",", "."));
			}
			catch (Exception e) {}
		}
		return result;
	}
	public String[] getVolumeUnitDef() {
		String[] result = new String[l3.size()];
		for (int i=0;i<l3.size();i++) {
			result[i] = (String) l3.get(i).getSelectedItem();
		}
		return result;
	}
	public int[] getIArr() {
		int[] iarr;
		if (forMatrices) {
			iarr = new int[lmat.size()];
			for (int i=0;i<lmat.size();i++) {
				if (lmat.get(i) != null) {
					iarr[i] = lmat.get(i).getId();
				}
			}
		}
		else {
			iarr = new int[lage.size()];
			for (int i=0;i<lage.size();i++) {
				if (lage.get(i) != null) {
					iarr[i] = lage.get(i).getId();
				}
			}
		}
		return iarr;
	}
	public String[] getNArr() {
		String[] narr;
		if (forMatrices) {
			narr = new String[lmat.size()];
			for (int i=0;i<lmat.size();i++) {
				if (lmat.get(i) != null) {
					narr[i] = lmat.get(i).getName();				
				}
			}
		}
		else {
			narr = new String[lage.size()];
			for (int i=0;i<lage.size();i++) {
				if (lage.get(i) != null) {
					narr[i] = lage.get(i).getName();				
				}
			}
		}
		return narr;
	}
	public void saveSettings(Config cM) {
		int[] iarr;
		String[] narr;
		if (forMatrices) {
			iarr = new int[lmat.size()];
			narr = new String[lmat.size()];
			for (int i=0;i<lmat.size();i++) {
				if (lmat.get(i) != null) {
					iarr[i] = lmat.get(i).getId();
					narr[i] = lmat.get(i).getName();				
				}
			}
		}
		else {
			iarr = new int[lage.size()];
			narr = new String[lage.size()];
			for (int i=0;i<lage.size();i++) {
				if (lage.get(i) != null) {
					iarr[i] = lage.get(i).getId();
					narr[i] = lage.get(i).getName();				
				}
			}
		}
		cM.addIntArray(AddonNodeModel.PARAM_ID, iarr);
		cM.addStringArray(AddonNodeModel.PARAM_NAME, narr);
	}
	private void button2ActionPerformed(ActionEvent e) {
		buttonPlusActionPerformed(e);
		panel1.revalidate();
	}
	private void button3ActionPerformed(ActionEvent e) {
		buttonMinusActionPerformed(e);
		panel1.revalidate();
	}
	private void buttonMinusActionPerformed(ActionEvent e) {
		if (l5.size() > 1) {
			removeAllFromPanel();
			
			JButton b = (JButton) e.getSource();
			
			for (int i = 0;i<l5.size();i++) {
				if (b.equals(l5.get(i))) {
					l1.remove(i);
					if (forMatrices) lmat.remove(i); else lage.remove(i);
					l2.remove(i);
					l3.remove(i);
					l4.remove(i);
					l5.remove(i);
					break;
				}
			}
			
			addAll2Panel();
		}
	}
	private void buttonPlusActionPerformed(ActionEvent e) {
		removeAllFromPanel();
		
		JButton button = new JButton();
		button.setText("(select " + (forMatrices ? "Matrix" : "Agent") + ")");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonActionPerformed(e);
			}
		});
		DoubleTextField formattedTextField = new DoubleTextField();
		formattedTextField.setColumns(5);
		JComboBox<String> comboBoxEinheit = new JComboBox<String>();
		comboBoxEinheit.setModel(
				forMatrices ? new DefaultComboBoxModel<String>(new String[] {
				"g",
				"kg",
				"t"
			})
			:
				new DefaultComboBoxModel<String>(new String[] {
						"cfu",
						"g",
						"kg"
					})
			);
		JButton buttonPlus = new JButton();
		buttonPlus.setText("+");
		buttonPlus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPlusActionPerformed(e);
			}
		});
		JButton buttonMinus = new JButton();
		buttonMinus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonMinusActionPerformed(e);
			}
		});
		buttonMinus.setText("-");

		int i = 0;
		if (e != null) {
			JButton b = (JButton) e.getSource();
			for (;i<l4.size();i++) {
				if (b.equals(l4.get(i))) {
					break;
				}
			}			
		}
		else {
			i = l4.size();
		}
		if (i < l4.size()) {
			l1.add(i+1, button);
			if (forMatrices) lmat.add(i+1, null); else lage.add(i+1, null);
			l2.add(i+1, formattedTextField);
			l3.add(i+1, comboBoxEinheit);
			l4.add(i+1, buttonPlus);
			l5.add(i+1, buttonMinus);				
		}
		else {
			l1.add(button);
			if (forMatrices) lmat.add(null); else lage.add(null);
			l2.add(formattedTextField);
			l3.add(comboBoxEinheit);
			l4.add(buttonPlus);
			l5.add(buttonMinus);				
		}
		
		addAll2Panel();
	}
	private void removeAllFromPanel() {		
		//Enumeration e1 = Collections.enumeration(l1);
		for (int i=0;i<l1.size();i++) {
			panel1.remove(l1.get(i));			
			panel1.remove(l2.get(i));			
			panel1.remove(l3.get(i));			
			panel1.remove(l4.get(i));			
			panel1.remove(l5.get(i));			
		}
	}
	private void addAll2Panel() {
		String rowDef = "default";
		for (int i=1;i<l1.size();i++) {
			rowDef += ", $lgap, default";
		}
		panel1.setLayout(new FormLayout(
				"default:grow, 4*($lcgap, default)",
				rowDef));
			((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{5, 7, 9}});
		for (int i=0;i<l1.size();i++) {
			// Matrices/Agents
			panel1.add(l1.get(i), CC.xy(1, 2 * i + 1));
			// Amount
			panel1.add(l2.get(i), CC.xy(3, 2 * i + 1));
			// Einheit
			panel1.add(l3.get(i), CC.xy(5, 2 * i + 1));
			// buttonPlus
			panel1.add(l4.get(i), CC.xy(7, 2 * i + 1));
			//---- buttonMinus ----
			panel1.add(l5.get(i), CC.xy(9, 2 * i + 1));
		}
		
		panel1.repaint();
		this.revalidate();
	}

	private void buttonActionPerformed(ActionEvent e) {
		//System.err.println(e);
		int index = 0;
		JButton b = ((JButton) e.getSource());
		for (;index<l1.size();index++) {
			if (b.equals(l1.get(index))) {
				break;
			}
		}
		Integer id;
		if (forMatrices) id = (index >= lmat.size() || lmat.get(index) == null) ? null : lmat.get(index).getId();
		else id = (index >= lage.size() || lage.get(index) == null) ? null : lage.get(index).getId();
		MyTable myT = DBKernel.myDBi.getTable((forMatrices ? "Matrices" : "Agenzien"));
		Object newVal = DBKernel.mainFrame.openNewWindow(
				myT,
				id,
				(Object) (forMatrices ? "Matrix" : "Agent"),
				null,
				null,
				null,
				null,
				true, null, this, "Combase");
		if (newVal != null && newVal instanceof Integer) {
			String mname = ""+DBKernel.getValue((forMatrices ? "Matrices" : "Agenzien"), "ID", newVal+"", forMatrices ? "Matrixname" : "Agensname");
			b.setText(mname);
			if (forMatrices) {
				if (index < lmat.size()) {
					lmat.set(index, new Matrix(mname, (Integer) newVal));
					l1.set(index, b);
				}
			}
			else {
				if (index < lage.size()) {
					lage.set(index, new Agent(mname, (Integer) newVal));
					l1.set(index, b);
				}
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		scrollPane2 = new JScrollPane();
		panel1 = new JPanel();
		button = new JButton();
		formattedTextField1 = new DoubleTextField();
		comboBox2 = new JComboBox<String>();
		button2 = new JButton();
		button3 = new JButton();

		//======== this ========
		setBorder(new TitledBorder("Matrix Selection"));
		setPreferredSize(new Dimension(550, 74));
		setLayout(new FormLayout(
			"default:grow",
			"fill:default:grow"));

		//======== scrollPane2 ========
		{
			scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			//======== panel1 ========
			{
				panel1.setLayout(new FormLayout(
					"default:grow, 4*($lcgap, default)",
					"default, $lgap, default"));
				((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{5, 7, 9}});

				//---- button ----
				button.setText("(select " + (forMatrices ? "Matrix" : "Agent") + ")");
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						buttonActionPerformed(e);
					}
				});
				panel1.add(button, CC.xy(1, 1));

				//---- formattedTextField1 ----
				formattedTextField1.setColumns(15);
				panel1.add(formattedTextField1, CC.xy(3, 1));

				//---- comboBox2 ----
				comboBox2.setModel(
						forMatrices ? new DefaultComboBoxModel<String>(new String[] {
						"g",
						"kg",
						"t"
					})
					:
						new DefaultComboBoxModel<String>(new String[] {
								"cfu",
								"g",
								"kg"
							})
					);
				panel1.add(comboBox2, CC.xy(5, 1));

				//---- button2 ----
				button2.setText("+");
				button2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						button2ActionPerformed(e);
					}
				});
				panel1.add(button2, CC.xy(7, 1));

				//---- button3 ----
				button3.setText("-");
				button3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						button3ActionPerformed(e);
					}
				});
				panel1.add(button3, CC.xy(9, 1));
			}
			scrollPane2.setViewportView(panel1);
		}
		add(scrollPane2, CC.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JScrollPane scrollPane2;
	private JPanel panel1;
	private JButton button;
	private DoubleTextField formattedTextField1;
	private JComboBox<String> comboBox2;
	private JButton button2;
	private JButton button3;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
