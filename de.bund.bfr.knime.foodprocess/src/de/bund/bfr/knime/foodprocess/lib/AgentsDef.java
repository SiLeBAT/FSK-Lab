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
package de.bund.bfr.knime.foodprocess.lib;

import java.awt.Component;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.knime.core.node.InvalidSettingsException;

public class AgentsDef {
	
	private JTable agentsDef;
	private JRadioButton recipeGuess;
	private JRadioButton manualDef;
	
	public JTable getAgentsDef() {
		return agentsDef;
	}
	public JRadioButton getRecipeGuess() {
		return recipeGuess;
	}
	public JRadioButton getManualDef() {
		return manualDef;
	}

	public AgentsDef( ) {	
		recipeGuess = new JRadioButton();
		recipeGuess.setText("Guess from Recipe");
		recipeGuess.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				recipeGuessStateChanged(e);
			}
		});
		
		manualDef = new JRadioButton();
		manualDef.setText("Define manually");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(recipeGuess);
		buttonGroup.add(manualDef);
		
		agentsDef = new JTable();
		setModel4AgentsDef(null, null);
		
		TableCellRenderer cellRend = new DefaultTableCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5696466457779114281L;

			@Override
			public Component getTableCellRendererComponent(JTable arg0,
					Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
				// TODO Auto-generated method stub
				Component c = super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5);
				if (recipeGuess.isSelected()) {					
					c.setBackground(UIManager.getLookAndFeelDefaults().getColor("TextField.inactiveBackground"));
					c.setForeground(UIManager.getLookAndFeelDefaults().getColor("TextField.inactiveForeground"));
				}
				else {
					c.setBackground(UIManager.getLookAndFeelDefaults().getColor("TextField.background"));
					c.setForeground(UIManager.getLookAndFeelDefaults().getColor("TextField.foreground"));
				}
				return c;
			}
		};
		agentsDef.setDefaultRenderer(Double.class, cellRend);
		agentsDef.setDefaultRenderer(String.class, cellRend);
	}
	public AgentsSetting getSetting() throws InvalidSettingsException {		
		AgentsSetting as;

		as = new AgentsSetting();
		
		as.setRecipeGuess( recipeGuess.isSelected() );
		as.setManualDef(manualDef.isSelected());

		String[] agentsDefStr = new String[agentsDef.getModel().getRowCount()];
		double[][] agentsDefDbl = new double[4][agentsDef.getModel().getRowCount()];
		for (int i=0;i<agentsDef.getModel().getRowCount();i++) {
			agentsDefStr[i] = (String) agentsDef.getModel().getValueAt(i, 0);
			for (int j=0;j<4;j++) {
				if (agentsDef.getModel().getValueAt(i, j+1) != null) {
					if ((Double) agentsDef.getModel().getValueAt(i, j+1) < 0) {
						throw new InvalidSettingsException("Values below zero "
						           + "are not allowed.");
					}
					else {
						agentsDefDbl[j][i] = (Double) agentsDef.getModel().getValueAt(i, j+1);										
					}
				}
				else {
					agentsDefDbl[j][i] = -1;
				}
			}
		}
		as.setAgentsDef(agentsDefStr);
		as.setAgentsDef1(agentsDefDbl[0]);
		as.setAgentsDef2(agentsDefDbl[1]);
		as.setAgentsDef3(agentsDefDbl[2]);
		as.setAgentsDef4(agentsDefDbl[3]);
		
		return as;
	}
	
	public void setSetting( final AgentsSetting as ) {		
		recipeGuess.setSelected( as.isRecipeGuess() );
		manualDef.setSelected( as.isManualDef() );
		
		String[] agentsDefStr = as.getAgentsDef();
		double[][] agentsDefDbl = new double[][] {as.getAgentsDef1(),as.getAgentsDef2(),as.getAgentsDef3(),as.getAgentsDef4()};
		setModel4AgentsDef(agentsDefStr, agentsDefDbl);
	}
	private void setModel4AgentsDef(final String[] agentsDefStr, final double[][] agentsDefDbl) {
		Object[][] tableContent = null;
		if (agentsDefStr != null) {
			tableContent = new Object[agentsDefStr.length][5];
			for (int i=0;i<agentsDefStr.length;i++) {
				tableContent[i][0] = agentsDefStr[i];
				tableContent[i][1] = agentsDefDbl[0][i] == -1 ? null : agentsDefDbl[0][i];;
				tableContent[i][2] = agentsDefDbl[1][i] == -1 ? null : agentsDefDbl[1][i];
				tableContent[i][3] = agentsDefDbl[2][i] == -1 ? null : agentsDefDbl[2][i];
				tableContent[i][4] = agentsDefDbl[3][i] == -1 ? null : agentsDefDbl[3][i];
			}
		}
		else {
			tableContent = new Object[][] {{"Agent 1", 2.,null,7.,null}};
		}
		
		agentsDef.setModel(new DefaultTableModel(
				tableContent,
				new String[] {
					"Agent Name", "Out Port 1", "Out Port 2", "Out Port 3", "Out Port 4"
				}
			) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -8631551676994066139L;
				Class<?>[] columnTypes = new Class<?>[] {
					String.class, Double.class, Double.class, Double.class, Double.class
				};
				boolean[] columnEditable = new boolean[] {
					false, true, true, true, true
				};
				@Override
				public Class<?> getColumnClass(final int columnIndex) {
					return columnTypes[columnIndex];
				}
				@Override
				public boolean isCellEditable(final int rowIndex, final int columnIndex) {
					return columnEditable[columnIndex];
				}
		});		
	}
	private void recipeGuessStateChanged(ChangeEvent e) {
		agentsDef.setEnabled(!recipeGuess.isSelected());
	}
}
