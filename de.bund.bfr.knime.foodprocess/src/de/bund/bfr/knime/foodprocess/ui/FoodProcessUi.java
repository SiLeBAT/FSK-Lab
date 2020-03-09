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

import java.awt.*;

import javax.swing.*;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import com.jgoodies.forms.factories.*;

import org.knime.core.node.InvalidSettingsException;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import de.bund.bfr.knime.foodprocess.FoodProcessNodeModel;
import de.bund.bfr.knime.foodprocess.lib.AgentsDef;
import de.bund.bfr.knime.foodprocess.lib.FoodProcessDef;
import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;
import de.bund.bfr.knime.foodprocess.lib.InPortDef;
import de.bund.bfr.knime.foodprocess.lib.OutPortDef;
import de.bund.bfr.knime.foodprocess.lib.ParametersDef;

/**
 * @author Armin Weiser
 */
public class FoodProcessUi extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8499308737142764556L;
	FoodProcessDef foodProcessDef;
	
	public FoodProcessUi() {
		myInitComponents();
	}

	public void setSettings(final FoodProcessSetting fps) {
		foodProcessDef.setSetting(fps);
	}
	public FoodProcessSetting getSettings() throws InvalidSettingsException {
		return foodProcessDef.getSetting();
	}
	private void myInitComponents() {
		initComponents();
		tabbedPane1.remove(2); // Agents
		tabbedPane1.remove(0); // In-Port
		removeAllRelevantComponents(this);
		removeAllRelevantComponents(panel2);
		removeAllRelevantComponents(panel4);
		removeAllRelevantComponents(panel6);
		
		foodProcessDef = new FoodProcessDef(FoodProcessNodeModel.N_PORT_IN, FoodProcessNodeModel.N_PORT_OUT);
		ParametersDef foodProcessParamsDef = foodProcessDef.getParametersDef();
		
		panel3.add(foodProcessDef.getNameField(), CC.xywh(3, 1, 7, 1));

		panel1.add(foodProcessParamsDef.getTemperature(), CC.xy(3, 1));
		panel1.add(foodProcessParamsDef.getTemperature_func(), CC.xy(5, 1));
		panel1.add(foodProcessParamsDef.getTemperatureUnit(), CC.xy(7, 1));

		panel1.add(foodProcessParamsDef.getPh(), CC.xy(3, 3));
		panel1.add(foodProcessParamsDef.getPh_func(), CC.xy(5, 3));
		
		panel1.add(foodProcessParamsDef.getAw(), CC.xy(3, 5));
		panel1.add(foodProcessParamsDef.getAw_func(), CC.xy(5, 5));
		
		panel1.add(foodProcessParamsDef.getPressure(), CC.xy(3, 7));
		panel1.add(foodProcessParamsDef.getPressure_func(), CC.xy(5, 7));
		panel1.add(foodProcessParamsDef.getPressureUnit(), CC.xy(7, 7));

		panel3.remove(label2);panel3.remove(label20);
/*
		panel3.add(foodProcessDef.getCapacityField(), CC.xy(3, 3));
		panel3.add(foodProcessDef.getCapacityNomBox(), CC.xy(5, 3));
		panel3.add(foodProcessDef.getCapacityDenomBox(), CC.xy(9, 3));
		*/
		panel3.add(foodProcessDef.getDurationField(), CC.xy(3, 5));
		panel3.add(foodProcessDef.getDurationBox(), CC.xy(5, 5));
		
		panel3.add(foodProcessDef.getComputationCountField(), CC.xy(3, 7));

		InPortDef[] inPortDef = foodProcessDef.getInPortDef();
		panel4.add(foodProcessDef.getExpertIn(panel7), CC.xy(1, 3));
		for(int i = 0; i < FoodProcessNodeModel.N_PORT_IN; i++ ) {
			/*
			JLabel label = new JLabel("In-Port " + (i+1));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel4.add(label, CC.xywh(4*i+5, 1, 3, 1));
			*/
			ParametersDef portParameterDef = inPortDef[i].getParametersDef();
			panel7.add(portParameterDef.getVolume(), CC.xy(4*i+5, 3));
			panel7.add(portParameterDef.getVolume_func(), CC.xy(4*i+7, 3));
			panel7.add(portParameterDef.getTemperature(), CC.xy(4*i+5, 5));
			panel7.add(portParameterDef.getTemperature_func(), CC.xy(4*i+7, 5));
			panel7.add(portParameterDef.getPh(), CC.xy(4*i+5, 7));
			panel7.add(portParameterDef.getPh_func(), CC.xy(4*i+7, 7));
			panel7.add(portParameterDef.getAw(), CC.xy(4*i+5, 9));
			panel7.add(portParameterDef.getAw_func(), CC.xy(4*i+7, 9));
			panel7.add(portParameterDef.getPressure(), CC.xy(4*i+5, 11));
			panel7.add(portParameterDef.getPressure_func(), CC.xy(4*i+7, 11));
		}
		// Use first In-Port Units as Unit-Container for all In-Ports
		ParametersDef portParameterDef = inPortDef[0].getParametersDef();
		panel7.add(portParameterDef.getVolumeUnit(), CC.xy(3, 3));
		panel7.add(portParameterDef.getTemperatureUnit(), CC.xy(3, 5));
		panel7.add(portParameterDef.getPressureUnit(), CC.xy(3, 11));

		OutPortDef[] outPortDef = foodProcessDef.getOutPortDef();
		panel2.add(foodProcessDef.getExpertOut(panel5), CC.xy(1, 7));
		for(int i = 0; i < FoodProcessNodeModel.N_PORT_OUT; i++ ) {
			/*
			JLabel label = new JLabel("Out-Port " + (i+1));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel2.add(label, CC.xywh(4*i+5, 1, 3, 1));
			*/
			panel2.add(outPortDef[i].getOutFlux(), CC.xy(4*i+7, 3));
			panel2.add(outPortDef[i].getNewMatrixDefinition(), CC.xywh(4*i+5, 5, 3, 1));
			/*
			for (int j=0;j < FoodProcessNodeModel.N_PORT_IN;j++) {
				panel2.add(outPortDef[i].getFromInPort()[j], CC.xywh(4*i+5, 11+2*j, 3, 1));				
			}
			*/
			portParameterDef = outPortDef[i].getParametersDef();
			panel5.add(portParameterDef.getVolume(), CC.xy(4*i+5, 3));
			panel5.add(portParameterDef.getVolume_func(), CC.xy(4*i+7, 3));
			panel5.add(portParameterDef.getTemperature(), CC.xy(4*i+5, 5));
			panel5.add(portParameterDef.getTemperature_func(), CC.xy(4*i+7, 5));
			panel5.add(portParameterDef.getPh(), CC.xy(4*i+5, 7));
			panel5.add(portParameterDef.getPh_func(), CC.xy(4*i+7, 7));
			panel5.add(portParameterDef.getAw(), CC.xy(4*i+5, 9));
			panel5.add(portParameterDef.getAw_func(), CC.xy(4*i+7, 9));
			panel5.add(portParameterDef.getPressure(), CC.xy(4*i+5, 11));
			panel5.add(portParameterDef.getPressure_func(), CC.xy(4*i+7, 11));
		}
		// Use first Out-Port Units as Unit-Container for all Out-Ports
		portParameterDef = outPortDef[0].getParametersDef();
		panel5.add(portParameterDef.getVolumeUnit(), CC.xy(3, 3));
		panel5.add(portParameterDef.getTemperatureUnit(), CC.xy(3, 5));
		panel5.add(portParameterDef.getPressureUnit(), CC.xy(3, 11));
		
		AgentsDef agentsDef = foodProcessDef.getAgentsDef();
		panel6.add(agentsDef.getRecipeGuess(), CC.xy(1, 1));
		panel6.add(agentsDef.getManualDef(), CC.xy(1, 3));
		agentsDef.getRecipeGuess().setSelected(true);
		scrollPane1.setViewportView(agentsDef.getAgentsDef());
		panel6.add(scrollPane1, CC.xy(1, 5));
	}
	private void removeAllRelevantComponents(JPanel panel) {
		if (panel != null) {
			for (int i=0;i<panel.getComponentCount();i++) {
				Component c = panel.getComponent(i);
				if (c instanceof JPanel) {
					removeAllRelevantComponents((JPanel) c);
				}
				else if (c instanceof JFormattedTextField || c instanceof JComboBox || c instanceof JCheckBox ||
						c instanceof JButton || c instanceof JTextField) {
					//System.err.println(c);
					panel.remove(c);
					i--;
				}
			}		
		}
	}


	@SuppressWarnings({"serial"})
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel3 = new JPanel();
		label1 = new JLabel();
		textField1 = new JTextField();
		label2 = new JLabel();
		formattedTextField1 = new JFormattedTextField();
		comboBox2 = new JComboBox<>();
		label20 = new JLabel();
		comboBox3 = new JComboBox<>();
		label3 = new JLabel();
		formattedTextField2 = new JFormattedTextField();
		comboBox4 = new JComboBox<>();
		label4 = new JLabel();
		formattedTextField3 = new JFormattedTextField();
		panel1 = new JPanel();
		label5 = new JLabel();
		formattedTextField4 = new JFormattedTextField();
		button1 = new JButton();
		comboBox6 = new JComboBox<>();
		label6 = new JLabel();
		formattedTextField5 = new JFormattedTextField();
		button2 = new JButton();
		label7 = new JLabel();
		formattedTextField6 = new JFormattedTextField();
		button3 = new JButton();
		label8 = new JLabel();
		formattedTextField7 = new JFormattedTextField();
		button4 = new JButton();
		comboBox9 = new JComboBox<>();
		tabbedPane1 = new JTabbedPane();
		panel4 = new JPanel();
		label38 = new JLabel();
		label40 = new JLabel();
		label42 = new JLabel();
		label44 = new JLabel();
		checkBox3 = new JCheckBox();
		label12 = new JLabel();
		panel7 = new JPanel();
		label105 = new JLabel();
		label106 = new JLabel();
		comboBox42 = new JComboBox<>();
		formattedTextField114 = new JFormattedTextField();
		button20 = new JButton();
		formattedTextField115 = new JFormattedTextField();
		button21 = new JButton();
		formattedTextField116 = new JFormattedTextField();
		button22 = new JButton();
		formattedTextField117 = new JFormattedTextField();
		button23 = new JButton();
		label107 = new JLabel();
		comboBox43 = new JComboBox<>();
		formattedTextField118 = new JFormattedTextField();
		button24 = new JButton();
		formattedTextField119 = new JFormattedTextField();
		button75 = new JButton();
		formattedTextField120 = new JFormattedTextField();
		button76 = new JButton();
		formattedTextField121 = new JFormattedTextField();
		button77 = new JButton();
		label108 = new JLabel();
		formattedTextField122 = new JFormattedTextField();
		button78 = new JButton();
		formattedTextField123 = new JFormattedTextField();
		button79 = new JButton();
		formattedTextField124 = new JFormattedTextField();
		button80 = new JButton();
		formattedTextField125 = new JFormattedTextField();
		button81 = new JButton();
		label109 = new JLabel();
		formattedTextField126 = new JFormattedTextField();
		button82 = new JButton();
		formattedTextField127 = new JFormattedTextField();
		button83 = new JButton();
		formattedTextField128 = new JFormattedTextField();
		button84 = new JButton();
		formattedTextField129 = new JFormattedTextField();
		button85 = new JButton();
		label110 = new JLabel();
		comboBox44 = new JComboBox<>();
		formattedTextField130 = new JFormattedTextField();
		button86 = new JButton();
		formattedTextField131 = new JFormattedTextField();
		button87 = new JButton();
		formattedTextField132 = new JFormattedTextField();
		button88 = new JButton();
		formattedTextField133 = new JFormattedTextField();
		button89 = new JButton();
		panel2 = new JPanel();
		label13 = new JLabel();
		label14 = new JLabel();
		label15 = new JLabel();
		label16 = new JLabel();
		label17 = new JLabel();
		label18 = new JLabel();
		formattedTextField74 = new JFormattedTextField();
		formattedTextField75 = new JFormattedTextField();
		formattedTextField76 = new JFormattedTextField();
		formattedTextField77 = new JFormattedTextField();
		label19 = new JLabel();
		button6 = new JButton();
		button7 = new JButton();
		button8 = new JButton();
		button13 = new JButton();
		checkBox1 = new JCheckBox();
		label9 = new JLabel();
		panel5 = new JPanel();
		label93 = new JLabel();
		label94 = new JLabel();
		comboBox36 = new JComboBox<>();
		formattedTextField94 = new JFormattedTextField();
		button9 = new JButton();
		formattedTextField95 = new JFormattedTextField();
		button10 = new JButton();
		formattedTextField96 = new JFormattedTextField();
		button11 = new JButton();
		formattedTextField97 = new JFormattedTextField();
		button12 = new JButton();
		label95 = new JLabel();
		comboBox37 = new JComboBox<>();
		formattedTextField98 = new JFormattedTextField();
		button14 = new JButton();
		formattedTextField102 = new JFormattedTextField();
		button48 = new JButton();
		formattedTextField106 = new JFormattedTextField();
		button52 = new JButton();
		formattedTextField110 = new JFormattedTextField();
		button56 = new JButton();
		label96 = new JLabel();
		formattedTextField99 = new JFormattedTextField();
		button45 = new JButton();
		formattedTextField103 = new JFormattedTextField();
		button49 = new JButton();
		formattedTextField107 = new JFormattedTextField();
		button53 = new JButton();
		formattedTextField111 = new JFormattedTextField();
		button57 = new JButton();
		label97 = new JLabel();
		formattedTextField100 = new JFormattedTextField();
		button46 = new JButton();
		formattedTextField104 = new JFormattedTextField();
		button50 = new JButton();
		formattedTextField108 = new JFormattedTextField();
		button54 = new JButton();
		formattedTextField112 = new JFormattedTextField();
		button58 = new JButton();
		label98 = new JLabel();
		comboBox38 = new JComboBox<>();
		formattedTextField101 = new JFormattedTextField();
		button47 = new JButton();
		formattedTextField105 = new JFormattedTextField();
		button51 = new JButton();
		formattedTextField109 = new JFormattedTextField();
		button55 = new JButton();
		formattedTextField113 = new JFormattedTextField();
		button59 = new JButton();
		button5 = new JButton();
		label10 = new JLabel();
		panel6 = new JPanel();
		radioButton1 = new JRadioButton();
		radioButton2 = new JRadioButton();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		label11 = new JLabel();

		//======== this ========
		setMinimumSize(new Dimension(671, 430));
		setPreferredSize(new Dimension(674, 440));
		setLayout(new FormLayout(
			"default:grow, $lcgap, default:grow",
			"default, $lgap, fill:203dlu:grow"));

		//======== panel3 ========
		{
			panel3.setBorder(new TitledBorder("Process Properties"));
			panel3.setLayout(new FormLayout(
				"default, $lcgap, default:grow, $lcgap, default, $lcgap, center:min, $lcgap, default",
				"default:grow, 3*($lgap, default), $lgap, default:grow"));

			//---- label1 ----
			label1.setText("Process Name");
			panel3.add(label1, CC.xy(1, 1));
			panel3.add(textField1, CC.xywh(3, 1, 7, 1));

			//---- label2 ----
			label2.setText("Capacity");
			panel3.add(label2, CC.xy(1, 3));

			//---- formattedTextField1 ----
			formattedTextField1.setColumns(5);
			panel3.add(formattedTextField1, CC.xy(3, 3));

			//---- comboBox2 ----
			comboBox2.setModel(new DefaultComboBoxModel<>(new String[] {
				"l",
				"kg"
			}));
			panel3.add(comboBox2, CC.xy(5, 3));

			//---- label20 ----
			label20.setText("/");
			label20.setHorizontalAlignment(SwingConstants.CENTER);
			label20.setPreferredSize(new Dimension(4, 14));
			panel3.add(label20, CC.xy(7, 3));

			//---- comboBox3 ----
			comboBox3.setModel(new DefaultComboBoxModel<>(new String[] {
				" ",
				"s",
				"min",
				"h",
				"d",
				"m",
				"y"
			}));
			panel3.add(comboBox3, CC.xy(9, 3));

			//---- label3 ----
			label3.setText("Duration");
			panel3.add(label3, CC.xy(1, 5));
			panel3.add(formattedTextField2, CC.xy(3, 5));

			//---- comboBox4 ----
			comboBox4.setModel(new DefaultComboBoxModel<>(new String[] {
				"s",
				"min",
				"h",
				"d",
				"m",
				"y"
			}));
			panel3.add(comboBox4, CC.xy(5, 5));

			//---- label4 ----
			label4.setText("#Computations");
			panel3.add(label4, CC.xy(1, 7));
			panel3.add(formattedTextField3, CC.xy(3, 7));
		}
		add(panel3, CC.xy(1, 1));

		//======== panel1 ========
		{
			panel1.setBorder(new TitledBorder("Process Parameters"));
			panel1.setLayout(new FormLayout(
				"default, $lcgap, default:grow, 2*($lcgap, default)",
				"4*(default, $lgap), default:grow"));
			((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{5, 7}});

			//---- label5 ----
			label5.setText("Temperature");
			panel1.add(label5, CC.xy(1, 1));

			//---- formattedTextField4 ----
			formattedTextField4.setColumns(10);
			panel1.add(formattedTextField4, CC.xy(3, 1));

			//---- button1 ----
			button1.setText("...");
			panel1.add(button1, CC.xy(5, 1));

			//---- comboBox6 ----
			comboBox6.setModel(new DefaultComboBoxModel<>(new String[] {
				"\u00b0C",
				"\u00b0F",
				"K"
			}));
			panel1.add(comboBox6, CC.xy(7, 1));

			//---- label6 ----
			label6.setText("pH");
			panel1.add(label6, CC.xy(1, 3));

			//---- formattedTextField5 ----
			formattedTextField5.setColumns(10);
			panel1.add(formattedTextField5, CC.xy(3, 3));

			//---- button2 ----
			button2.setText("...");
			panel1.add(button2, CC.xy(5, 3));

			//---- label7 ----
			label7.setText("aw");
			panel1.add(label7, CC.xy(1, 5));

			//---- formattedTextField6 ----
			formattedTextField6.setColumns(10);
			panel1.add(formattedTextField6, CC.xy(3, 5));

			//---- button3 ----
			button3.setText("...");
			panel1.add(button3, CC.xy(5, 5));

			//---- label8 ----
			label8.setText("Pressure");
			panel1.add(label8, CC.xy(1, 7));

			//---- formattedTextField7 ----
			formattedTextField7.setColumns(10);
			panel1.add(formattedTextField7, CC.xy(3, 7));

			//---- button4 ----
			button4.setText("...");
			panel1.add(button4, CC.xy(5, 7));

			//---- comboBox9 ----
			comboBox9.setModel(new DefaultComboBoxModel<>(new String[] {
				"bar",
				"Pa"
			}));
			panel1.add(comboBox9, CC.xy(7, 7));
		}
		add(panel1, CC.xy(3, 1));

		//======== tabbedPane1 ========
		{
			tabbedPane1.setPreferredSize(new Dimension(469, 380));

			//======== panel4 ========
			{
				panel4.setBorder(Borders.TABBED_DIALOG_BORDER);
				panel4.setLayout(new FormLayout(
					"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
					"2*(default, $lgap), fill:default:grow"));
				((FormLayout)panel4.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});

				//---- label38 ----
				label38.setText("In-Port 1");
				label38.setHorizontalAlignment(SwingConstants.CENTER);
				panel4.add(label38, CC.xywh(5, 1, 3, 1));

				//---- label40 ----
				label40.setText("In-Port 2");
				label40.setHorizontalAlignment(SwingConstants.CENTER);
				panel4.add(label40, CC.xywh(9, 1, 3, 1));

				//---- label42 ----
				label42.setText("In-Port 3");
				label42.setHorizontalAlignment(SwingConstants.CENTER);
				panel4.add(label42, CC.xywh(13, 1, 3, 1));

				//---- label44 ----
				label44.setText("In-Port 4");
				label44.setHorizontalAlignment(SwingConstants.CENTER);
				panel4.add(label44, CC.xywh(17, 1, 3, 1));

				//---- checkBox3 ----
				checkBox3.setText("Advanced");
				panel4.add(checkBox3, CC.xy(1, 3));

				//---- label12 ----
				label12.setText("Expert mode allows for special settings, example see documentation");
				label12.setForeground(Color.gray);
				panel4.add(label12, CC.xywh(5, 3, 15, 1));

				//======== panel7 ========
				{
					panel7.setLayout(new FormLayout(
						"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
						"6*(default, $lgap), default:grow"));
					((FormLayout)panel7.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});

					//---- label105 ----
					label105.setText("Parameters");
					label105.setFont(new Font("Segoe UI", Font.BOLD, 12));
					panel7.add(label105, CC.xy(1, 1));

					//---- label106 ----
					label106.setText("Volume");
					panel7.add(label106, CC.xy(1, 3));

					//---- comboBox42 ----
					comboBox42.setModel(new DefaultComboBoxModel<>(new String[] {
						"l",
						"kg"
					}));
					panel7.add(comboBox42, CC.xy(3, 3));
					panel7.add(formattedTextField114, CC.xy(5, 3));

					//---- button20 ----
					button20.setText("...");
					panel7.add(button20, CC.xy(7, 3));
					panel7.add(formattedTextField115, CC.xy(9, 3));

					//---- button21 ----
					button21.setText("...");
					panel7.add(button21, CC.xy(11, 3));
					panel7.add(formattedTextField116, CC.xy(13, 3));

					//---- button22 ----
					button22.setText("...");
					panel7.add(button22, CC.xy(15, 3));
					panel7.add(formattedTextField117, CC.xy(17, 3));

					//---- button23 ----
					button23.setText("...");
					panel7.add(button23, CC.xy(19, 3));

					//---- label107 ----
					label107.setText("Temperature");
					panel7.add(label107, CC.xy(1, 5));

					//---- comboBox43 ----
					comboBox43.setModel(new DefaultComboBoxModel<>(new String[] {
						"\u00b0C",
						"\u00b0F",
						"K"
					}));
					panel7.add(comboBox43, CC.xy(3, 5));
					panel7.add(formattedTextField118, CC.xy(5, 5));

					//---- button24 ----
					button24.setText("...");
					panel7.add(button24, CC.xy(7, 5));
					panel7.add(formattedTextField119, CC.xy(9, 5));

					//---- button75 ----
					button75.setText("...");
					panel7.add(button75, CC.xy(11, 5));
					panel7.add(formattedTextField120, CC.xy(13, 5));

					//---- button76 ----
					button76.setText("...");
					panel7.add(button76, CC.xy(15, 5));
					panel7.add(formattedTextField121, CC.xy(17, 5));

					//---- button77 ----
					button77.setText("...");
					panel7.add(button77, CC.xy(19, 5));

					//---- label108 ----
					label108.setText("pH");
					panel7.add(label108, CC.xy(1, 7));
					panel7.add(formattedTextField122, CC.xy(5, 7));

					//---- button78 ----
					button78.setText("...");
					panel7.add(button78, CC.xy(7, 7));
					panel7.add(formattedTextField123, CC.xy(9, 7));

					//---- button79 ----
					button79.setText("...");
					panel7.add(button79, CC.xy(11, 7));
					panel7.add(formattedTextField124, CC.xy(13, 7));

					//---- button80 ----
					button80.setText("...");
					panel7.add(button80, CC.xy(15, 7));
					panel7.add(formattedTextField125, CC.xy(17, 7));

					//---- button81 ----
					button81.setText("...");
					panel7.add(button81, CC.xy(19, 7));

					//---- label109 ----
					label109.setText("aw");
					panel7.add(label109, CC.xy(1, 9));
					panel7.add(formattedTextField126, CC.xy(5, 9));

					//---- button82 ----
					button82.setText("...");
					panel7.add(button82, CC.xy(7, 9));
					panel7.add(formattedTextField127, CC.xy(9, 9));

					//---- button83 ----
					button83.setText("...");
					panel7.add(button83, CC.xy(11, 9));
					panel7.add(formattedTextField128, CC.xy(13, 9));

					//---- button84 ----
					button84.setText("...");
					panel7.add(button84, CC.xy(15, 9));
					panel7.add(formattedTextField129, CC.xy(17, 9));

					//---- button85 ----
					button85.setText("...");
					panel7.add(button85, CC.xy(19, 9));

					//---- label110 ----
					label110.setText("Pressure");
					panel7.add(label110, CC.xy(1, 11));

					//---- comboBox44 ----
					comboBox44.setModel(new DefaultComboBoxModel<>(new String[] {
						"bar",
						"Pa"
					}));
					panel7.add(comboBox44, CC.xy(3, 11));
					panel7.add(formattedTextField130, CC.xy(5, 11));

					//---- button86 ----
					button86.setText("...");
					panel7.add(button86, CC.xy(7, 11));
					panel7.add(formattedTextField131, CC.xy(9, 11));

					//---- button87 ----
					button87.setText("...");
					panel7.add(button87, CC.xy(11, 11));
					panel7.add(formattedTextField132, CC.xy(13, 11));

					//---- button88 ----
					button88.setText("...");
					panel7.add(button88, CC.xy(15, 11));
					panel7.add(formattedTextField133, CC.xy(17, 11));

					//---- button89 ----
					button89.setText("...");
					panel7.add(button89, CC.xy(19, 11));
				}
				panel4.add(panel7, CC.xywh(1, 5, 19, 1));
			}
			tabbedPane1.addTab("In Ports", panel4);

			//======== panel2 ========
			{
				panel2.setBorder(Borders.TABBED_DIALOG_BORDER);
				panel2.setLayout(new FormLayout(
					"2*(default, $lcgap), 7*(default:grow, $lcgap), default:grow",
					"4*(default, $lgap), fill:default:grow"));
				((FormLayout)panel2.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});

				//---- label13 ----
				label13.setText("Out-Port 1");
				label13.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label13, CC.xywh(5, 1, 3, 1));

				//---- label14 ----
				label14.setText("Out-Port 2");
				label14.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label14, CC.xywh(9, 1, 3, 1));

				//---- label15 ----
				label15.setText("Out-Port 3");
				label15.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label15, CC.xywh(13, 1, 3, 1));

				//---- label16 ----
				label16.setText("Out-Port 4");
				label16.setHorizontalAlignment(SwingConstants.CENTER);
				panel2.add(label16, CC.xywh(17, 1, 3, 1));

				//---- label17 ----
				label17.setText("Out Flux");
				panel2.add(label17, CC.xy(1, 3));

				//---- label18 ----
				label18.setText("quant.");
				label18.setHorizontalAlignment(SwingConstants.CENTER);
				label18.setToolTipText("Quantifier/Weight for the outport - empty means nothing");
				panel2.add(label18, CC.xy(3, 3));
				panel2.add(formattedTextField74, CC.xy(7, 3));
				panel2.add(formattedTextField75, CC.xy(11, 3));
				panel2.add(formattedTextField76, CC.xy(15, 3));
				panel2.add(formattedTextField77, CC.xy(19, 3));

				//---- label19 ----
				label19.setText("New Matrix Definition");
				panel2.add(label19, CC.xy(1, 5));

				//---- button6 ----
				button6.setText("(select matrix)");
				panel2.add(button6, CC.xywh(5, 5, 3, 1));

				//---- button7 ----
				button7.setText("(select matrix)");
				panel2.add(button7, CC.xywh(9, 5, 3, 1));

				//---- button8 ----
				button8.setText("(select matrix)");
				panel2.add(button8, CC.xywh(13, 5, 3, 1));

				//---- button13 ----
				button13.setText("(select matrix)");
				panel2.add(button13, CC.xywh(17, 5, 3, 1));

				//---- checkBox1 ----
				checkBox1.setText("Advanced");
				panel2.add(checkBox1, CC.xywh(1, 7, 2, 1));

				//---- label9 ----
				label9.setText("Expert mode allows for special settings, example see documentation");
				label9.setForeground(Color.gray);
				panel2.add(label9, CC.xywh(5, 7, 15, 1));

				//======== panel5 ========
				{
					panel5.setLayout(new FormLayout(
						"2*(default, $lcgap), default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
						"6*(default, $lgap), fill:default, $lgap, default:grow"));
					((FormLayout)panel5.getLayout()).setColumnGroups(new int[][] {{5, 9, 13, 17}});

					//---- label93 ----
					label93.setText("Parameters");
					label93.setFont(new Font("Segoe UI", Font.BOLD, 12));
					panel5.add(label93, CC.xy(1, 1));

					//---- label94 ----
					label94.setText("Volume");
					panel5.add(label94, CC.xy(1, 3));

					//---- comboBox36 ----
					comboBox36.setModel(new DefaultComboBoxModel<>(new String[] {
						"l",
						"kg"
					}));
					panel5.add(comboBox36, CC.xy(3, 3));
					panel5.add(formattedTextField94, CC.xy(5, 3));

					//---- button9 ----
					button9.setText("...");
					panel5.add(button9, CC.xy(7, 3));
					panel5.add(formattedTextField95, CC.xy(9, 3));

					//---- button10 ----
					button10.setText("...");
					panel5.add(button10, CC.xy(11, 3));
					panel5.add(formattedTextField96, CC.xy(13, 3));

					//---- button11 ----
					button11.setText("...");
					panel5.add(button11, CC.xy(15, 3));
					panel5.add(formattedTextField97, CC.xy(17, 3));

					//---- button12 ----
					button12.setText("...");
					panel5.add(button12, CC.xy(19, 3));

					//---- label95 ----
					label95.setText("Temperature");
					panel5.add(label95, CC.xy(1, 5));

					//---- comboBox37 ----
					comboBox37.setModel(new DefaultComboBoxModel<>(new String[] {
						"\u00b0C",
						"\u00b0F",
						"K"
					}));
					panel5.add(comboBox37, CC.xy(3, 5));
					panel5.add(formattedTextField98, CC.xy(5, 5));

					//---- button14 ----
					button14.setText("...");
					panel5.add(button14, CC.xy(7, 5));
					panel5.add(formattedTextField102, CC.xy(9, 5));

					//---- button48 ----
					button48.setText("...");
					panel5.add(button48, CC.xy(11, 5));
					panel5.add(formattedTextField106, CC.xy(13, 5));

					//---- button52 ----
					button52.setText("...");
					panel5.add(button52, CC.xy(15, 5));
					panel5.add(formattedTextField110, CC.xy(17, 5));

					//---- button56 ----
					button56.setText("...");
					panel5.add(button56, CC.xy(19, 5));

					//---- label96 ----
					label96.setText("pH");
					panel5.add(label96, CC.xy(1, 7));
					panel5.add(formattedTextField99, CC.xy(5, 7));

					//---- button45 ----
					button45.setText("...");
					panel5.add(button45, CC.xy(7, 7));
					panel5.add(formattedTextField103, CC.xy(9, 7));

					//---- button49 ----
					button49.setText("...");
					panel5.add(button49, CC.xy(11, 7));
					panel5.add(formattedTextField107, CC.xy(13, 7));

					//---- button53 ----
					button53.setText("...");
					panel5.add(button53, CC.xy(15, 7));
					panel5.add(formattedTextField111, CC.xy(17, 7));

					//---- button57 ----
					button57.setText("...");
					panel5.add(button57, CC.xy(19, 7));

					//---- label97 ----
					label97.setText("aw");
					panel5.add(label97, CC.xy(1, 9));
					panel5.add(formattedTextField100, CC.xy(5, 9));

					//---- button46 ----
					button46.setText("...");
					panel5.add(button46, CC.xy(7, 9));
					panel5.add(formattedTextField104, CC.xy(9, 9));

					//---- button50 ----
					button50.setText("...");
					panel5.add(button50, CC.xy(11, 9));
					panel5.add(formattedTextField108, CC.xy(13, 9));

					//---- button54 ----
					button54.setText("...");
					panel5.add(button54, CC.xy(15, 9));
					panel5.add(formattedTextField112, CC.xy(17, 9));

					//---- button58 ----
					button58.setText("...");
					panel5.add(button58, CC.xy(19, 9));

					//---- label98 ----
					label98.setText("Pressure");
					panel5.add(label98, CC.xy(1, 11));

					//---- comboBox38 ----
					comboBox38.setModel(new DefaultComboBoxModel<>(new String[] {
						"bar",
						"Pa"
					}));
					panel5.add(comboBox38, CC.xy(3, 11));
					panel5.add(formattedTextField101, CC.xy(5, 11));

					//---- button47 ----
					button47.setText("...");
					panel5.add(button47, CC.xy(7, 11));
					panel5.add(formattedTextField105, CC.xy(9, 11));

					//---- button51 ----
					button51.setText("...");
					panel5.add(button51, CC.xy(11, 11));
					panel5.add(formattedTextField109, CC.xy(13, 11));

					//---- button55 ----
					button55.setText("...");
					panel5.add(button55, CC.xy(15, 11));
					panel5.add(formattedTextField113, CC.xy(17, 11));

					//---- button59 ----
					button59.setText("...");
					panel5.add(button59, CC.xy(19, 11));

					//---- button5 ----
					button5.setText("Recipe");
					panel5.add(button5, CC.xy(1, 13));

					//---- label10 ----
					label10.setText("Recipe allows for special settings for the matrices, example see documentation");
					label10.setForeground(Color.gray);
					panel5.add(label10, CC.xywh(5, 13, 15, 1));
				}
				panel2.add(panel5, CC.xywh(1, 9, 19, 1));
			}
			tabbedPane1.addTab("Out Ports", panel2);

			//======== panel6 ========
			{
				panel6.setBorder(Borders.TABBED_DIALOG_BORDER);
				panel6.setPreferredSize(new Dimension(464, 220));
				panel6.setLayout(new FormLayout(
					"default:grow",
					"2*(default, $lgap), default:grow, $lgap, fill:default:grow"));

				//---- radioButton1 ----
				radioButton1.setText("Guess from Recipe");
				panel6.add(radioButton1, CC.xy(1, 1));

				//---- radioButton2 ----
				radioButton2.setText("Define manually");
				panel6.add(radioButton2, CC.xy(1, 3));

				//======== scrollPane1 ========
				{

					//---- table1 ----
					table1.setModel(new DefaultTableModel(
						new Object[][] {
							{"Agent 1", null, null, null, null},
						},
						new String[] {
							"Agent Name", "Out Port 1", "Out Port 2", "Out Port 3", "Out Port 4"
						}
					) {
						Class<?>[] columnTypes = new Class<?>[] {
							String.class, Double.class, Double.class, Double.class, Double.class
						};
						boolean[] columnEditable = new boolean[] {
							false, true, true, true, true
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
					scrollPane1.setViewportView(table1);
				}
				panel6.add(scrollPane1, CC.xy(1, 5));

				//---- label11 ----
				label11.setText("Agents are given by weight relative to the conentration, example see documentation");
				label11.setForeground(Color.gray);
				panel6.add(label11, CC.xy(1, 7));
			}
			tabbedPane1.addTab("Agents", panel6);
		}
		add(tabbedPane1, CC.xywh(1, 3, 3, 1));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(radioButton1);
		buttonGroup1.add(radioButton2);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel3;
	private JLabel label1;
	private JTextField textField1;
	private JLabel label2;
	private JFormattedTextField formattedTextField1;
	private JComboBox<String> comboBox2;
	private JLabel label20;
	private JComboBox<String> comboBox3;
	private JLabel label3;
	private JFormattedTextField formattedTextField2;
	private JComboBox<String> comboBox4;
	private JLabel label4;
	private JFormattedTextField formattedTextField3;
	private JPanel panel1;
	private JLabel label5;
	private JFormattedTextField formattedTextField4;
	private JButton button1;
	private JComboBox<String> comboBox6;
	private JLabel label6;
	private JFormattedTextField formattedTextField5;
	private JButton button2;
	private JLabel label7;
	private JFormattedTextField formattedTextField6;
	private JButton button3;
	private JLabel label8;
	private JFormattedTextField formattedTextField7;
	private JButton button4;
	private JComboBox<String> comboBox9;
	private JTabbedPane tabbedPane1;
	private JPanel panel4;
	private JLabel label38;
	private JLabel label40;
	private JLabel label42;
	private JLabel label44;
	private JCheckBox checkBox3;
	private JLabel label12;
	private JPanel panel7;
	private JLabel label105;
	private JLabel label106;
	private JComboBox<String> comboBox42;
	private JFormattedTextField formattedTextField114;
	private JButton button20;
	private JFormattedTextField formattedTextField115;
	private JButton button21;
	private JFormattedTextField formattedTextField116;
	private JButton button22;
	private JFormattedTextField formattedTextField117;
	private JButton button23;
	private JLabel label107;
	private JComboBox<String> comboBox43;
	private JFormattedTextField formattedTextField118;
	private JButton button24;
	private JFormattedTextField formattedTextField119;
	private JButton button75;
	private JFormattedTextField formattedTextField120;
	private JButton button76;
	private JFormattedTextField formattedTextField121;
	private JButton button77;
	private JLabel label108;
	private JFormattedTextField formattedTextField122;
	private JButton button78;
	private JFormattedTextField formattedTextField123;
	private JButton button79;
	private JFormattedTextField formattedTextField124;
	private JButton button80;
	private JFormattedTextField formattedTextField125;
	private JButton button81;
	private JLabel label109;
	private JFormattedTextField formattedTextField126;
	private JButton button82;
	private JFormattedTextField formattedTextField127;
	private JButton button83;
	private JFormattedTextField formattedTextField128;
	private JButton button84;
	private JFormattedTextField formattedTextField129;
	private JButton button85;
	private JLabel label110;
	private JComboBox<String> comboBox44;
	private JFormattedTextField formattedTextField130;
	private JButton button86;
	private JFormattedTextField formattedTextField131;
	private JButton button87;
	private JFormattedTextField formattedTextField132;
	private JButton button88;
	private JFormattedTextField formattedTextField133;
	private JButton button89;
	private JPanel panel2;
	private JLabel label13;
	private JLabel label14;
	private JLabel label15;
	private JLabel label16;
	private JLabel label17;
	private JLabel label18;
	private JFormattedTextField formattedTextField74;
	private JFormattedTextField formattedTextField75;
	private JFormattedTextField formattedTextField76;
	private JFormattedTextField formattedTextField77;
	private JLabel label19;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	private JButton button13;
	private JCheckBox checkBox1;
	private JLabel label9;
	private JPanel panel5;
	private JLabel label93;
	private JLabel label94;
	private JComboBox<String> comboBox36;
	private JFormattedTextField formattedTextField94;
	private JButton button9;
	private JFormattedTextField formattedTextField95;
	private JButton button10;
	private JFormattedTextField formattedTextField96;
	private JButton button11;
	private JFormattedTextField formattedTextField97;
	private JButton button12;
	private JLabel label95;
	private JComboBox<String> comboBox37;
	private JFormattedTextField formattedTextField98;
	private JButton button14;
	private JFormattedTextField formattedTextField102;
	private JButton button48;
	private JFormattedTextField formattedTextField106;
	private JButton button52;
	private JFormattedTextField formattedTextField110;
	private JButton button56;
	private JLabel label96;
	private JFormattedTextField formattedTextField99;
	private JButton button45;
	private JFormattedTextField formattedTextField103;
	private JButton button49;
	private JFormattedTextField formattedTextField107;
	private JButton button53;
	private JFormattedTextField formattedTextField111;
	private JButton button57;
	private JLabel label97;
	private JFormattedTextField formattedTextField100;
	private JButton button46;
	private JFormattedTextField formattedTextField104;
	private JButton button50;
	private JFormattedTextField formattedTextField108;
	private JButton button54;
	private JFormattedTextField formattedTextField112;
	private JButton button58;
	private JLabel label98;
	private JComboBox<String> comboBox38;
	private JFormattedTextField formattedTextField101;
	private JButton button47;
	private JFormattedTextField formattedTextField105;
	private JButton button51;
	private JFormattedTextField formattedTextField109;
	private JButton button55;
	private JFormattedTextField formattedTextField113;
	private JButton button59;
	private JButton button5;
	private JLabel label10;
	private JPanel panel6;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JLabel label11;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
