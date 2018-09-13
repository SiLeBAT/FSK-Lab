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
package de.bund.bfr.knime.pmm.js.modelplotter;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

/**
 * Model Plotter dialog pane. 
 * Shows just one text field for the chart title and one for constant Y0.
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 *
 */
public final class ModelPlotterNodeDialogPane extends NodeDialogPane {

	private static final int TEXT_FIELD_SIZE = 20;
	
	private final JTextField m_chartTitleTextField;
	
	private final SpinnerModel m_y0Model;
	private final JSpinner m_y0;

	private final SpinnerModel m_minXAxisModel;
	private final JSpinner m_minXAxis;

	private final SpinnerModel m_maxXAxisModel;
	private final JSpinner m_maxXAxis;
	
	private final SpinnerModel m_minYAxisModel;
	private final JSpinner m_minYAxis;

	private final SpinnerModel m_maxYAxisModel;
	private final JSpinner m_maxYAxis;	
	
	/**
	 * Constructor of {@code ModelPlotterNodeDialogPane}, creating the dialog pane with the dialog
	 * items.
	 */
	public ModelPlotterNodeDialogPane() {
		m_chartTitleTextField = new JTextField(TEXT_FIELD_SIZE);
		
		m_y0Model = new SpinnerNumberModel(ModelPlotterViewConfig.DEF_Y0,
				ModelPlotterViewConfig.MIN_Y0, ModelPlotterViewConfig.MAX_Y0, 0.1);
		m_y0 = new JSpinner();
		m_y0.setModel(m_y0Model);
		m_y0.setEditor(new JSpinner.NumberEditor(m_y0, "0.0000"));
		
		m_minXAxisModel = new SpinnerNumberModel(ModelPlotterViewConfig.DEF_MIN_X_AXIS,
				ModelPlotterViewConfig.MIN_MIN_X_AXIS, ModelPlotterViewConfig.MAX_MIN_X_AXIS, 10);
		m_minXAxis = new JSpinner();
		m_minXAxis.setModel(m_minXAxisModel);
		m_minXAxis.setEditor(new JSpinner.NumberEditor(m_minXAxis, ""));
		
		m_maxXAxisModel = new SpinnerNumberModel(ModelPlotterViewConfig.DEF_MAX_X_AXIS,
				ModelPlotterViewConfig.MAX_MIN_X_AXIS, ModelPlotterViewConfig.MAX_MAX_X_AXIS, 10);
		m_maxXAxis = new JSpinner();
		m_maxXAxis.setModel(m_maxXAxisModel);
		m_maxXAxis.setEditor(new JSpinner.NumberEditor(m_maxXAxis, ""));
		
		m_minYAxisModel = new SpinnerNumberModel(ModelPlotterViewConfig.DEF_MIN_Y_AXIS,
				ModelPlotterViewConfig.MIN_MIN_Y_AXIS, ModelPlotterViewConfig.MAX_MIN_Y_AXIS, 5);
		m_minYAxis = new JSpinner();
		m_minYAxis.setModel(m_minYAxisModel);
		m_minYAxis.setEditor(new JSpinner.NumberEditor(m_minYAxis, ""));
		
		m_maxYAxisModel = new SpinnerNumberModel(ModelPlotterViewConfig.DEF_MAX_Y_AXIS,
				ModelPlotterViewConfig.MAX_MIN_Y_AXIS, ModelPlotterViewConfig.MAX_MAX_Y_AXIS, 5);
		m_maxYAxis = new JSpinner();
		m_maxYAxis.setModel(m_maxYAxisModel);
		m_maxYAxis.setEditor(new JSpinner.NumberEditor(m_maxYAxis, ""));		
		
		addTab("Options", initDialog());
	}
	
    private Component initDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHWEST;        
        panel.add(new JLabel("Chart title:"), c);
        
        c.gridx = 1;
        c.gridy = 0;
        panel.add(m_chartTitleTextField, c);
        
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Y0 (log10(count/g)):"), c);        

        c.gridx = 1;
        c.gridy = 1;
        panel.add(m_y0, c);
        
        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Min x-axis"), c);        

        c.gridx = 1;
        c.gridy = 2;
        panel.add(m_minXAxis, c);
        
        c.gridx = 2;
        c.gridy = 2;
        panel.add(new JLabel("Max x-axis"), c);        

        c.gridx = 3;
        c.gridy = 2;
        panel.add(m_maxXAxis, c);
        
        c.gridx = 0;
        c.gridy = 3;
        panel.add(new JLabel("Min y-axis"), c);        

        c.gridx = 1;
        c.gridy = 3;
        panel.add(m_minYAxis, c);
        
        c.gridx = 2;
        c.gridy = 3;
        panel.add(new JLabel("Max y-axis"), c);        

        c.gridx = 3;
        c.gridy = 3;
        panel.add(m_maxYAxis, c);
        
        return panel;
    }
	
	/* (non-Javadoc)
	 * @see org.knime.core.node.NodeDialogPane#saveSettingsTo(org.knime.core.node.NodeSettingsWO)
	 */
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		ModelPlotterViewConfig config = new ModelPlotterViewConfig();
		config.setChartTitle(m_chartTitleTextField.getText());
		config.setY0((Double)m_y0Model.getValue());
		config.setMinXAxis((Integer)m_minXAxisModel.getValue());
		config.setMaxXAxis((Integer)m_maxXAxisModel.getValue());
		config.setMinYAxis((Integer)m_minYAxisModel.getValue());
		config.setMaxYAxis((Integer)m_maxYAxisModel.getValue());
		
		config.saveSettings(settings);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
            throws NotConfigurableException {
    	ModelPlotterViewConfig config = new ModelPlotterViewConfig();
        config.loadSettingsForDialog(settings);
        m_chartTitleTextField.setText(config.getChartTitle());
        m_y0Model.setValue(config.getY0());
        m_minXAxisModel.setValue(config.getMinXAxis());
        m_maxXAxisModel.setValue(config.getMaxXAxis());
        m_minYAxisModel.setValue(config.getMinYAxis());
        m_maxYAxisModel.setValue(config.getMaxYAxis());        
    }
}
