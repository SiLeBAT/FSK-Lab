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

import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.foodprocess.ui.MyChartDialog;

public class ParametersDef {

	private JTextField volume;
	private JButton volume_func;
	private JComboBox<String> volumeUnit;
	private JTextField temperature;
	private JTextField Temp_alt;
	private JButton temperature_func;
	private JComboBox<String> temperatureUnit;
	private JTextField ph;
	private JTextField Ph_alt;
	private JButton ph_func;
	private JTextField aw;
	private JTextField Aw_alt;
	private JButton aw_func;
	private JTextField pressure;
	private JTextField Pres_alt;
	private JButton pressure_func;
	private JComboBox<String> pressureUnit;
	public JTextField getVolume() {
		return volume;
	}
	public JTextField getTemperature() {
		return temperature;
	}
	public JTextField getTemperatureAlt() {
		return Temp_alt;
	}
	public JTextField getPh() {
		return ph;
	}
	public JTextField getPhAlt() {
		return Ph_alt;
	}
	public JTextField getAw() {
		return aw;
	}
	public JTextField getAwAlt() {
		return Aw_alt;
	}
	public JTextField getPressure() {
		return pressure;
	}
	public JTextField getPressureAlt() {
		return Pres_alt;
	}
	public JButton getVolume_func() {
		return volume_func;
	}
	public JButton getTemperature_func() {
		return temperature_func;
	}
	public JButton getPh_func() {
		return ph_func;
	}
	public JButton getAw_func() {
		return aw_func;
	}
	public JButton getPressure_func() {
		return pressure_func;
	}
	public JComboBox<String> getVolumeUnit() {
		return volumeUnit;
	}
	public JComboBox<String> getTemperatureUnit() {
		return temperatureUnit;
	}
	public JComboBox<String> getPressureUnit() {
		return pressureUnit;
	}

	public ParametersDef() {
		//NumberFormat nf = NumberFormat.getNumberInstance( java.util.Locale.US );

		volume = new JTextField();
		volume.setColumns(10);
		volume.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		
		volume_func = new JButton("...");
		volume_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "Percent", volume);
			}
		});
		volumeUnit = new JComboBox<String>(FoodProcessDef.COMBO_VOLUMEUNIT);
		volumeUnit.setSelectedIndex(0);
		
		temperature = new JTextField();
		temperature.setColumns(10);
		temperature.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		
		Temp_alt = new JTextField();
		Temp_alt.setToolTipText("Variable name used in Model");
		Temp_alt.setColumns(5);

		temperature_func = new JButton("...");
		temperature_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "Temperature", temperature);
			}
		});
		temperatureUnit = new JComboBox<String>( FoodProcessDef.COMBO_TEMPERATUREUNIT );
		temperatureUnit.setSelectedIndex(0);

		ph = new JTextField();
		ph.setColumns(10);
		ph.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		
		Ph_alt = new JTextField();
		Ph_alt.setToolTipText("Variable name used in Model");
		Ph_alt.setColumns(5);
		
		ph_func = new JButton("...");
		ph_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "pH", ph);
			}
		});

		aw = new JTextField();
		aw.setColumns(10);
		aw.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		
		Aw_alt = new JTextField();
		Aw_alt.setToolTipText("Variable name used in Model");
		Aw_alt.setColumns(5);
		
		aw_func = new JButton("...");
		aw_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "aw", aw);
			}
		});

		pressure = new JTextField();
		pressure.setColumns(10);
		pressure.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		
		Pres_alt = new JTextField();
		Pres_alt.setToolTipText("Variable name used in Model");
		Pres_alt.setColumns(5);
		
		pressure_func = new JButton("...");
		pressure_func.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				func_ActionPerformed(e, "Pressure", pressure);
			}
		});
		pressureUnit = new JComboBox<String>( FoodProcessDef.COMBO_PRESSUREUNIT );
		pressureUnit.setSelectedIndex(0);
	}
	public ParametersSetting getSetting() {
		ParametersSetting ps = new ParametersSetting();
		
		//val = volume.getValue() == null ? null : ((Number)volume.getValue()).doubleValue();
		ps.setVolume( volume.getText() );
		
		//val = temperature.getValue() == null ? null : ((Number)temperature.getValue()).doubleValue();
		//assert val >= -459.67;
		ps.setTemperature( temperature.getText() );
		
		ps.setTempAlt(Temp_alt.getText());
		
		//val = ph.getValue() == null ? null : ((Number)ph.getValue()).doubleValue();
		//assert val >= 0;
		//assert val <= 14;
		ps.setPh( ph.getText() );
		ps.setPhAlt( Ph_alt.getText() );
		
		//val = aw.getValue() == null ? null : ((Number)aw.getValue()).doubleValue();
		//assert val >= 0;
		//assert val <= 1;
		ps.setAw( aw.getText() );
		ps.setAwAlt( Aw_alt.getText() );
		
		//val = pressure.getValue() == null ? null : ((Number)pressure.getValue()).doubleValue();
		ps.setPressure( pressure.getText() );
		ps.setPresAlt( Pres_alt.getText() );
		
		ps.setTemperatureUnit( ( String )temperatureUnit.getSelectedItem() );	
		ps.setVolumeUnit(( String )volumeUnit.getSelectedItem());
		ps.setPressureUnit( ( String )pressureUnit.getSelectedItem() );
		
		
		return ps;
	}
	public void setSetting( final ParametersSetting ps ) {
		String val;
		
		val = ps.getVolume();
		//assert val >= 0;
		volume.setText( val );
		
		val = ps.getTemperature();
		//assert val >= -459.67;
		temperature.setText( val );
		
		Temp_alt.setText(ps.getTempAlt());
		
		val = ps.getPh();
		//assert val >= 0;
		//assert val <= 14;
		ph.setText( val );
		
		Ph_alt.setText(ps.getPhAlt());
		
		val = ps.getAw();
		//assert val >= 0;
		//assert val <= 1;
		aw.setText( val );
		
		Aw_alt.setText(ps.getAwAlt());
		
		val = ps.getPressure();
		pressure.setText( val );
		
		Pres_alt.setText(ps.getPresAlt());
		
		if (ps.getTemperatureUnit() != null && !ps.getTemperatureUnit().isEmpty()) temperatureUnit.setSelectedItem(ps.getTemperatureUnit());
		if (ps.getVolumeUnit() != null && !ps.getVolumeUnit().isEmpty()) volumeUnit.setSelectedItem(ps.getVolumeUnit());
		if (ps.getPressureUnit() != null && !ps.getPressureUnit().isEmpty()) pressureUnit.setSelectedItem(ps.getPressureUnit());
	}
	public void setEnabledBase( final boolean enabled ) {
		volume.setEnabled(enabled);
		volume_func.setEnabled(enabled);
		temperature.setEnabled(enabled);
		Temp_alt.setEnabled(enabled);
		temperature_func.setEnabled(enabled);
		ph.setEnabled(enabled);
		Ph_alt.setEnabled(enabled);
		ph_func.setEnabled(enabled);
		aw.setEnabled(enabled);
		Aw_alt.setEnabled(enabled);
		aw_func.setEnabled(enabled);
		pressure.setEnabled(enabled);
		Pres_alt.setEnabled(enabled);
		pressure_func.setEnabled(enabled);
	}
	public void setEnabledUnits( final boolean enabled ) {
		volumeUnit.setEnabled(enabled);
		temperatureUnit.setEnabled(enabled);
		pressureUnit.setEnabled(enabled);
	}
	private void func_ActionPerformed(final ActionEvent e, final String yAxis, final JTextField tf) {
		JButton button = (JButton) e.getSource();
        Container c = button.getParent();
        while (c != null) {
            if (c instanceof Frame) {
				break;
			}
            c = c.getParent();
        }
        MyChartDialog mcd = new MyChartDialog(( Window )c, tf.getText(), "Time", yAxis);
        mcd.setModal(true);
        mcd.setVisible(true);
        //button.setToolTipText(mcd.getDatenpunkte());
        tf.setText(mcd.getCleanDataset(mcd.getStrDataSet()));
	}
	private void thisKeyTyped(final KeyEvent e) {
	  	char ch = e.getKeyChar();
	  	if (ch == KeyEvent.VK_ENTER) {
	  		//
		}
	  	else if (ch == KeyEvent.VK_ESCAPE) {
	  		//
	  	}
	  	else {
		  	if (e.getSource() instanceof JTextField) {
		  		JTextField tf = (JTextField) e.getSource();
		  		if (tf != null) {
		  			String text = tf.getText();
		  			int cp = tf.getCaretPosition();
		  			if (ch == ',') {
		  				ch = '.';
		  				e.setKeyChar('.');
		  			}
		  			text = text.substring(0, cp) + ch + text.substring(cp);
		  			if (!DBKernel.isDouble(text)) {
						e.consume();
						//System.out.println(tf.getCaretPosition() + "\t" + (tf == textField10) + "\t" + DBKernel.isDouble(text) + "\t" + text);	  			
					}
		  		}
		  	}	  		
	  	}
	}
}
