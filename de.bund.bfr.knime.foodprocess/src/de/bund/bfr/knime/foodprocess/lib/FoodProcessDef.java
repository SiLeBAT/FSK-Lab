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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knime.core.node.InvalidSettingsException;

public class FoodProcessDef {
	
	public static final String[] COMBO_VOLUMEUNIT = {"l", "ml", "g","kg"};
	public static final String[] COMBO_TEMPERATUREUNIT = {"°C", "°F", "K"};
	public static final String[] COMBO_PRESSUREUNIT = {"bar", "Pa"};
	public static final String[] COMBO_TIMEUNIT = {"s", "min", "h", "d"};

	private final String LABEL_EXPERT = "Expert";

	private JTextField nameField;
	private JFormattedTextField durationField;
	private JComboBox<String> durationBox;
	private JFormattedTextField numberComputationField;
	
	private ParametersDef parametersDef;

	private InPortDef[] inPortDef;
	private JCheckBox expertIn;
	
	private OutPortDef[] outPortDef;
	private JCheckBox expertOut;

	private AgentsDef agentsDef;
	
	private JPanel panelIn = null, panelOut = null;

	public JTextField getNameField() {
		return nameField;
	}
	public JComboBox<String> getCapacityNomBox() {
		return null;//capacityNomBox;
	}
	public JComboBox<String> getCapacityDenomBox() {
		return null;//capacityDenomBox;
	}
	public JComboBox<String> getDurationBox() {
		return durationBox;
	}
	public JFormattedTextField getCapacityField() {
		return null;//capacityField;
	}
	public JFormattedTextField getDurationField() {
		return durationField;
	}
	public JFormattedTextField getComputationCountField() {
		return numberComputationField;
	}
	public ParametersDef getParametersDef() {
		return parametersDef;
	}
	public InPortDef[] getInPortDef() {
		return inPortDef;
	}
	public OutPortDef[] getOutPortDef() {
		return outPortDef;
	}
	public JCheckBox getExpertIn(JPanel panelIn) {
		this.panelIn = panelIn;
		expertInActionPerformed(null);
		return expertIn;
	}
	public JCheckBox getExpertOut(JPanel panelOut) {
		this.panelOut = panelOut;
		expertOutActionPerformed(null);
		return expertOut;
	}
	public AgentsDef getAgentsDef() {
		return agentsDef;
	}

	private int n_outports;
	private int n_inports;

	public FoodProcessDef( final int n_inports, final int n_outports ) {		
		int i;
		NumberFormat nf;
		
		nf = NumberFormat.getNumberInstance( java.util.Locale.US );
		
		this.n_inports = n_inports;
		this.n_outports = n_outports;
		
		nameField = new JTextField();
		nameField.setEditable(true);
		durationField = new JFormattedTextField( nf );
		durationField.setColumns(5);
		durationBox = new JComboBox<String>(COMBO_TIMEUNIT);
		durationBox.setSelectedIndex(1);
		numberComputationField = new JFormattedTextField( nf );
		numberComputationField.setColumns(5);

		parametersDef = new ParametersDef();
		
		inPortDef = new InPortDef[ n_inports ];
		for( i = 0; i < n_inports; i++ ) {
			inPortDef[ i ] = new InPortDef(inPortDef);
		}
		expertIn = new JCheckBox( LABEL_EXPERT );
		expertIn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				expertInActionPerformed(e);
			}
		});
		
		outPortDef = new OutPortDef[ n_outports ];
		for( i = 0; i < n_outports; i++ ) {
			outPortDef[ i ] = new OutPortDef( n_inports, n_outports, outPortDef );
		}
		expertOut = new JCheckBox( LABEL_EXPERT );
		expertOut.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				expertOutActionPerformed(e);
			}
		});
		
		agentsDef = new AgentsDef();
		
		expertInActionPerformed(null);
		expertOutActionPerformed(null);
	}
	
	public FoodProcessSetting getSetting() throws InvalidSettingsException {		
		FoodProcessSetting fps;
		int i;
		Double val;
		
		fps = new FoodProcessSetting( n_inports, n_outports );
		
		//fps.setProcessName((String) nameBox.getSelectedItem());
		fps.setProcessName(nameField.getText());
		val = (durationField.getValue() == null) ? null : ((Number)durationField.getValue()).doubleValue();
		fps.setDuration(val);
		fps.setDurationUnit((String) durationBox.getSelectedItem());
		Integer iVal = (numberComputationField.getValue() == null) ? null : ((Number)numberComputationField.getValue()).intValue();
		fps.setNumberComputation(iVal);

		fps.setParametersSetting(parametersDef.getSetting());
		
		InPortSetting[] inPortSetting = new InPortSetting[n_inports]; 
		for( i = 0; i < n_inports; i++ ) {
			inPortSetting[i] = inPortDef[ i ].getSetting();
		}
		fps.setInPortSetting(inPortSetting);
		fps.setExpertIn( expertOut.isSelected() );
		
		OutPortSetting[] outPortSetting = new OutPortSetting[n_outports]; 
		for( i = 0; i < n_outports; i++ ) {
			outPortSetting[i] = outPortDef[ i ].getSetting();
		}
		fps.setOutPortSetting(outPortSetting);
		fps.setExpertOut( expertOut.isSelected() );
		
		fps.setAgentsSetting(agentsDef.getSetting());
		
		return fps;
	}
	
	public void setSetting( final FoodProcessSetting fps ) {		
		int i;
		Double val;
		
		nameField.setText(fps.getProcessName());
		
		val = fps.getDuration();
		durationField.setValue( val );
		if (fps.getDurationUnit() != null && !fps.getDurationUnit().isEmpty()) durationBox.setSelectedItem(fps.getDurationUnit());
		
		numberComputationField.setValue(fps.getNumberComputation());
				
		parametersDef.setSetting(fps.getParametersSetting());
		
		for( i = 0; i < n_inports; i++ ) {
			if( inPortDef[ i ] == null ) {
				inPortDef[ i ] = new InPortDef(inPortDef);
			}
			inPortDef[ i ].setSetting( fps.getInPortSetting()[i] );
		}
		expertIn.setSelected( fps.isExpertIn() );

		for( i = 0; i < n_outports; i++ ) {
			if( outPortDef[ i ] == null ) {
				outPortDef[ i ] = new OutPortDef( n_inports, n_outports, outPortDef );
			}
			outPortDef[ i ].setSetting( fps.getOutPortSetting()[i] );
		}
		expertOut.setSelected( fps.isExpertOut() );
		
		agentsDef.setSetting(fps.getAgentsSetting());
	}	
	private void expertInActionPerformed(final ItemEvent e) {
		for(int i = 0; i < n_inports; i++ ) {
			inPortDef[ i ].getParametersDef().setEnabledBase(expertIn.isSelected());
			inPortDef[ i ].getParametersDef().setEnabledUnits(expertIn.isSelected());
		}
		if (panelIn != null) panelIn.setVisible(expertIn.isSelected());
	}	
	private void expertOutActionPerformed(final ItemEvent e) {
		for(int i = 0; i < n_outports; i++ ) {
			outPortDef[ i ].getParametersDef().setEnabledBase(expertOut.isSelected());
			outPortDef[ i ].getParametersDef().setEnabledUnits(expertOut.isSelected());
		}
		if (panelOut != null) panelOut.setVisible(expertOut.isSelected());
	}	
}
