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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.js.modelplotter.ModelPlotterViewValue.Variable;
//import de.bund.bfr.knime.pmm.js.modelplotter.modern.ModelPlotterViewRepresentation.Variable;
import de.bund.bfr.knime.pmm.predictorview.TableReader;

/**
 * Model Plotter node model.
 * Reading all plotables functions of input table and preparing the first plotable for 
 * JavaScript view. 
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 *
 */
public final class ModelPlotterNodeModel extends AbstractWizardNodeModel<ModelPlotterViewRepresentation, ModelPlotterViewValue> {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(ModelPlotterNodeModel.class);
	
	static final String FLOWVAR_FUNCTION_ORIG = "Original Function";
	static final String FLOWVAR_FUNCTION_FULL = "Full Function";
	static final String FLOWVAR_FUNCTION_APPLIED = "Applied Function";
	
	private static final String PMM_MODEL_ARG_TIME = "Time";
	private static final String PMM_MODEL_ARG_TEMP = "temp";
	private static final String PMM_MODEL_ARG_AW = "aw";
	private static final String PMM_MODEL_ARG_CO2 = "CO2";
	private static final String PMM_MODEL_ARG_PS = "PhysiologicalState";
	private static final String PMM_MODEL_ARG_PH = "pH";
	
	private final ModelPlotterViewConfig m_config;
	
	private boolean m_executed = false;
	
    /**
     * Constructor of {@code ModelPlotterNodeModel}.
     */
    protected ModelPlotterNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, 
			  new PortType[] { BufferedDataTable.TYPE, BufferedDataTable.TYPE }, 
			  (new ModelPlotterNodeFactory()).getInteractiveViewName());
        m_config = new ModelPlotterViewConfig();
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public ModelPlotterViewRepresentation createEmptyViewRepresentation() {
        return new ModelPlotterViewRepresentation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelPlotterViewValue createEmptyViewValue() {
        return new ModelPlotterViewValue();
    }

	@Override
	public String getJavascriptObjectID() {
		return "de.bund.bfr.knime.pmm.js.modelplotter";
	}

	@Override
	public boolean isHideInWizard() {
		return m_config.getHideInwizard();
	}

	@Override
	public ValidationError validateViewValue(ModelPlotterViewValue viewContent) {
        synchronized (getLock()) {
            // Nothing to do.
        }
        return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO content) {
		// Nothing to do.
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		if (!SchemaFactory.createM1Schema()
				.conforms((DataTableSpec) inSpecs[0])) {
			throw new InvalidSettingsException("Wrong input!");
		}
		
		return createOutputDataTableSpecs();
	}
	
	/* (non-Javadoc)
	 * @see org.knime.js.core.node.AbstractWizardNodeModel#performExecute(org.knime.core.node.port.PortObject[], org.knime.core.node.ExecutionContext)
	 */
	@Override
	protected PortObject[] performExecute(PortObject[] inObjects,
			ExecutionContext exec) throws Exception {
		
		BufferedDataTable table = (BufferedDataTable)inObjects[0];
		TableReader reader = new TableReader(
				ModelPlotterNodeModel.getTuples(table),
				new LinkedHashMap<String, String>(),
				new LinkedHashMap<String, String>(), true);
		
		// read all plotables
		Map<String, Plotable> plotables = reader.getPlotables();
		Plotable p;
		
		// warn if more than one plotables
		if (plotables.size() <= 0) {
			setWarningMessage("No model functions to plot in input data table!");
			LOGGER.error("No model functions to plot in input data table!");
			throw new IllegalStateException("No model functions to plot in input data table!");
		} else if (plotables.size() > 1) {
			setWarningMessage("More than one model function to plot in input data table."
					+ " Plotting first function only.");
		}
		
		// get first plotable
		p = plotables.entrySet().iterator().next().getValue();
		
		ModelPlotterViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
			setViewValue(viewValue);
		}
		if (!m_executed) {
			// CONFIG of JavaScript view
			viewValue.setChartTitle(m_config.getChartTitle());
			viewValue.setY0(m_config.getY0());
			viewValue.setMinXAxis(m_config.getMinXAxis());
			viewValue.setMaxXAxis(m_config.getMaxXAxis());
			viewValue.setMinYAxis(m_config.getMinYAxis());
			viewValue.setMaxYAxis(m_config.getMaxYAxis());

			// set units
			viewValue.setxUnit(p.getUnits().get("Time"));
			viewValue.setyUnit(p.getUnits().get("Value"));

			// DATA: specify function (substring after '=')
			viewValue.setFunc(p.getFunction().substring(
					p.getFunction().indexOf("=") + 1));

			// DATA: specify arguments that can be adjusted via sliders in
			// JavaScript view
			List<Variable> variables = new LinkedList<>();
			Map<String, List<Double>> args = p.getFunctionArguments();
			for (Map.Entry<String, List<Double>> a : args.entrySet()) {
				// ignore time argument
				if (!a.getKey().equals(PMM_MODEL_ARG_TIME)) {
					Variable v = new Variable();
					v.setName(a.getKey());

					// set min value
					Double min = p.getMinArguments().get(a.getKey());
					if (min == null) {
						min = 0.0;
					}
					v.setMin(min);

					// set max value
					Double max = p.getMaxArguments().get(a.getKey());
					if (max == null) {
						max = 0.0;
					}
					v.setMax(max);

					// set default value (different for each argument)
					if (a.getKey().equals(PMM_MODEL_ARG_AW)) {
						v.setDef(0.997);
					} else if (a.getKey().equals(PMM_MODEL_ARG_CO2)) {
						v.setDef(0);
					} else if (a.getKey().equals(PMM_MODEL_ARG_PH)) {
						v.setDef(7.0);
					} else if (a.getKey().equals(PMM_MODEL_ARG_PS)) {
						v.setDef(0.0005);
					} else if (a.getKey().equals(PMM_MODEL_ARG_TEMP)) {
						v.setDef(20);
					}

					// add variable
					variables.add(v);
				}
			}
			viewValue.setVariables(variables);

			// DATA: specify constants and values
			Map<String, Double> constants = new HashMap<>();
			Map<String, Double> params = p.getFunctionParameters();
			for (Map.Entry<String, Double> param : params.entrySet()) {
				Double val = param.getValue();
				if (val == null) {
					val = 0.0;
				}
				constants.put(param.getKey(), val);
			}
			viewValue.setConstants(constants);
			
			setViewValue(viewValue);
			m_executed = true;
		}		
		
		exec.setProgress(1);
		
		pushFlowVariableString(FLOWVAR_FUNCTION_ORIG, getViewValue().getFunc());
		pushFlowVariableString(FLOWVAR_FUNCTION_FULL, getViewValue().getFunctionFull());		
		return createOutputDataTables(exec);
	}

	private DataTableSpec[] createOutputDataTableSpecs() {
		DataColumnSpec constantName = new DataColumnSpecCreator(
				"Constant name", StringCell.TYPE).createSpec();
		DataColumnSpec constantValue = new DataColumnSpecCreator(
				"Constant value", DoubleCell.TYPE).createSpec();
		DataTableSpec constantSpec = new DataTableSpec(constantName, constantValue);
		
		DataColumnSpec varName = new DataColumnSpecCreator(
				"Variable name", StringCell.TYPE).createSpec();
		DataColumnSpec varValue = new DataColumnSpecCreator(
				"Variable value", DoubleCell.TYPE).createSpec();
		DataColumnSpec varMin = new DataColumnSpecCreator(
				"Variable min ", DoubleCell.TYPE).createSpec();		
		DataColumnSpec varMax = new DataColumnSpecCreator(
				"Variable max", DoubleCell.TYPE).createSpec();		
		DataTableSpec varSpec = new DataTableSpec(varName, varValue, varMin, varMax);		
		
		return new DataTableSpec[]{ constantSpec, varSpec };		
	}
	
	private BufferedDataTable[] createOutputDataTables(final ExecutionContext exec) {
		ModelPlotterViewValue value = getViewValue();
		DataTableSpec[] outSpces = createOutputDataTableSpecs();
		
		BufferedDataContainer constBC = exec.createDataContainer(outSpces[0]);
		long i = 0;
		if (value.getConstants() != null) {
			for (Entry<String, Double> e : value.getConstants().entrySet()) {
				RowKey key = RowKey.createRowKey(i);
				constBC.addRowToTable(new DefaultRow(key, new StringCell(e
						.getKey()), new DoubleCell(e.getValue())));
				i++;
			}
		}
		constBC.close();
		
		BufferedDataContainer varBC = exec.createDataContainer(outSpces[1]);
		i = 0;
		if (value.getVariables() != null) {
			for (Variable v : value.getVariables()) {
				RowKey key = RowKey.createRowKey(i);
				varBC.addRowToTable(new DefaultRow(key, new StringCell(v
						.getName()), new DoubleCell(v.getDef()),
						new DoubleCell(v.getMin()), new DoubleCell(v.getMax())));
				i++;
			}
		}
		varBC.close();
		
		return new BufferedDataTable[]{ constBC.getTable(), varBC.getTable() };
	}
	
	@Override
	protected void performReset() {
		m_executed = false;
	}

	@Override
	protected void useCurrentValueAsDefault() {
		// save value #getViewValue() as config for default values after restart 
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_config.saveSettings(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		new ModelPlotterViewConfig().loadSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_config.loadSettings(settings);
	}
	
	private static List<KnimeTuple> getTuples(DataTable table) {
		boolean isTertiaryModel = SchemaFactory.createM12Schema().conforms(
				table);
		boolean containsData = SchemaFactory.createDataSchema().conforms(table);

		if (isTertiaryModel) {
			if (containsData) {
				return PmmUtilities.getTuples(table,
						SchemaFactory.createM12DataSchema());
			} else {
				return PmmUtilities.getTuples(table,
						SchemaFactory.createM12Schema());
			}
		} else {
			if (containsData) {
				return PmmUtilities.getTuples(table,
						SchemaFactory.createM1DataSchema());
			} else {
				return PmmUtilities.getTuples(table,
						SchemaFactory.createM1Schema());
			}
		}
	}

	@Override
	public void setHideInWizard(boolean hide) {
	}   	
}
