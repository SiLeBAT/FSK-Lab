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
package de.bund.bfr.knime.foodprocess.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.knime.core.data.DataTable;
import org.knime.core.data.container.DataContainer;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;

import de.bund.bfr.knime.pcml.node.pcmltotable.PCMLDataTable;
import de.bund.bfr.knime.pcml.port.PCMLPortObject;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartUtilities;
import de.bund.bfr.pcml10.PCMLDocument;

/**
 * This is the model implementation of View.
 * 
 * 
 * @author Christian Thoens
 */
public class ViewNodeModel extends NodeModel {

	protected static final String CFG_FILENAME = "FittedParameterView.zip";
	protected static final String CFG_USEDPARAMETERS = "UsedParameters";
	protected static final String CFG_EQUIDISTANT = "EquidistantPs";
	protected static final String CFG_XUNITS = "xUnits";
	protected static final String CFG_PLOTLINES = "plotLines";
	protected static final String CFG_PLOTPOINTS = "plotPoints";

	private DataTable table;
	private List<String> usedParameters;
	private String xUnits;
	private boolean equidistantProcesses;
	private boolean plotLines = true; // if true plot lines
	private boolean plotPoints = false; // if true plot points
	
	/**
	 * Constructor for the node model.
	 */
	protected ViewNodeModel() {
        super(new PortType[] {
        		PortTypeRegistry.getInstance().getPortType(PCMLPortObject.class, false),
            }, new PortType[] {
            	BufferedDataTable.TYPE, ImagePortObject.TYPE
            });
		usedParameters = new ArrayList<String>();
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
			throws Exception {
    	PCMLPortObject pcmlPortObject = (PCMLPortObject)inObjects[0];
    	PCMLDocument pcmlDoc = pcmlPortObject.getPcmlDoc();
    	PCMLDataTable pcmlData = new PCMLDataTable(pcmlDoc);
    	
    	BufferedDataTable outTable = pcmlData.execute(exec);
    	table = outTable;

		return new PortObject[] {outTable, new ImagePortObject(
				ChartUtilities.convertToPNGImageContent(createChart(), 640, 480),
				new ImagePortObjectSpec(PNGImageContent.TYPE))};
	}
	public JFreeChart createChart() {
    	final MyChartCreator mcc = new MyChartCreator(null);
    	JFreeChart jfc = mcc.createChart(table, usedParameters, xUnits, equidistantProcesses, plotLines, plotPoints);
    	LegendItemSource source = new LegendItemSource() {
        	LegendItemCollection lic = new LegendItemCollection(); 
        	{
	        	List<ProcessLegendElement> pl = mcc.getProcessLegend();
	        	for (ProcessLegendElement ple : pl) {
	            	lic.add(new LegendItem(ple.getText(), ple.getBackground()));
	        	}
        	}
            public LegendItemCollection getLegendItems() {
                return lic;
            }
        };
        jfc.addLegend(new LegendTitle(source));
        jfc.getLegend().setPosition(RectangleEdge.BOTTOM);
    	return jfc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		return new PortObjectSpec[] { null, new ImagePortObjectSpec(PNGImageContent.TYPE) };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFG_USEDPARAMETERS,
				XmlConverter.objectToXml(usedParameters));
		settings.addString(CFG_XUNITS, xUnits);
		settings.addBoolean(CFG_EQUIDISTANT, equidistantProcesses);
		settings.addBoolean(CFG_PLOTLINES, plotLines);
		settings.addBoolean(CFG_PLOTPOINTS, plotPoints);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		usedParameters = XmlConverter.xmlToObject(settings.getString(CFG_USEDPARAMETERS),
						new ArrayList<String>());
		if (settings.containsKey(CFG_XUNITS)) xUnits = settings.getString(CFG_XUNITS);
		if (settings.containsKey(CFG_EQUIDISTANT)) equidistantProcesses = settings.getBoolean(CFG_EQUIDISTANT);
		if (settings.containsKey(CFG_PLOTLINES)) plotLines = settings.getBoolean(CFG_PLOTLINES);
		if (settings.containsKey(CFG_PLOTPOINTS)) plotPoints = settings.getBoolean(CFG_PLOTPOINTS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		File f = new File(internDir, CFG_FILENAME);

		table = DataContainer.readFromZip(f);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		File f = new File(internDir, CFG_FILENAME);

		DataContainer.writeToZip(table, f, exec);
	}

}