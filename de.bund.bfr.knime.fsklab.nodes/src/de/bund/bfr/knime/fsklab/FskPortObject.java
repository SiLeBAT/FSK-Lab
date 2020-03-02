package de.bund.bfr.knime.fsklab;
/*
 ***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.emfjson.jackson.module.EMFModule;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.util.FileUtil;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.bund.bfr.knime.fsklab.nodes.common.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.common.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.common.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.common.ui.UIUtils;
import de.bund.bfr.knime.fsklab.rakip.RakipModule;
import de.bund.bfr.knime.fsklab.rakip.RakipUtil;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
import metadata.SwaggerUtil;

/**
 * A port object for an FSK model port providing R scripts and model meta data.
 *
 * @author Miguel Alba, BfR, Berlin.
 */
public class FskPortObject implements PortObject {

	private static NodeLogger LOGGER = NodeLogger.getLogger(FskPortObject.class);

	/**
	 * Convenience access member for <code>new PortType(FSKPortObject.class)</code>
	 */
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FskPortObject.class);
	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(FskPortObject.class, true);

	/** Model script. */
	public String model;

	/** Visualization script. */
	public String viz;

	/** Paths to resources: plain text files and R workspace files (.rdata). */
	private final String workingDirectory;

	/** Path to plot. */
	private String plot;

	/** README. */
	private String readme;
	/**
	 * R workspace file with the results of running the model. It may be null if the
	 * model has not been run.
	 */
	public Path workspace;

	/** Path to spreadsheet. */
	private final String spreadsheet;

	/** List of R packages. */
	public final List<String> packages;

	private static int numOfInstances = 0;

	public int objectNum;
	public int selectedSimulationIndex = 0;
	public final List<FskSimulation> simulations = new ArrayList<>();

	public Model modelMetadata;

	public FskPortObject(final String model, final String viz, final Model modelMetadata, final Path workspace,
			final List<String> packages, final String workingDirectory, final String plot, final String readme,
			final String spreadsheet) throws IOException {

		this.model = model;
		this.viz = viz;

		this.modelMetadata = modelMetadata;

		this.workspace = workspace;
		this.packages = packages;

		this.workingDirectory = workingDirectory;

		this.plot = plot;

		this.readme = StringUtils.defaultString(readme);
		this.spreadsheet = StringUtils.defaultString(spreadsheet);

		objectNum = numOfInstances;
		numOfInstances += 1;
	}

	public FskPortObject(final String workingDirectory, String readme, final List<String> packages) throws IOException {
		this.workingDirectory = workingDirectory;
		this.packages = packages;

		this.readme = readme;
		spreadsheet = "";
	}

	@Override
	public FskPortObjectSpec getSpec() {
		return FskPortObjectSpec.INSTANCE;
	}

	@Override
	public String getSummary() {
		return "FSK Object";
	}

	/**
	 * @return empty string if not set.
	 */
	public String getWorkingDirectory() {
		return workingDirectory != null ? workingDirectory : "";
	}

	/**
	 * @return empty string if not set.
	 */
	public String getPlot() {
		return plot != null ? plot : "";
	}

	public void setPlot(final String plot) {
		if (plot != null && !plot.isEmpty()) {
			this.plot = plot;
		}
	}

	/**
	 * @return empty string if not set.
	 */
	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
	}

	/**
	 * @return empty string if not set.
	 */
	public String getSpreadsheet() {
		return StringUtils.defaultString(spreadsheet);
	}

	/**
	 * Serializer used to save this port object.
	 *
	 * @return a {@link FskPortObject}.
	 */
	public static final class Serializer extends PortObjectSerializer<FskPortObject> {

		private static final String MODEL = "model.R";
		private static final String VIZ = "viz.R";
		private static final String META_DATA = "metaData";

		private static final String CFG_GENERAL_INFORMATION = "generalInformation";

		private static final String WORKSPACE = "workspace";
		private static final String SIMULATION = "simulation";
		private static final String SIMULATION_INDEX = "simulationIndex";

		private static final String WORKING_DIRECTORY = "workingDirectory";

		private static final String PLOT = "plot";

		private static final String README = "readme";

		private static final String SPREADSHEET = "spreadsheet";

		/** Object mapper for 1.0.2 metadata. */
		private static final ObjectMapper MAPPER102;

		/** Object mapper for 1.0.3 metadata. */
		private static final ObjectMapper MAPPER103;

		/** Object mapper for 1.0.4 metadata. */
		private static final ObjectMapper MAPPER104;

		public static Map<String, Class<? extends Model>> modelClasses;

		static {
			try {
				// ObjectMapper defaults to use a JsonFactory that automatically closes
				// the stream. When further entries are added to the archive the stream
				// is closed and fails. The AUTO_CLOSE_TARGET needs to be disabled.
				final JsonFactory jsonFactory = new JsonFactory();
				jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
				jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

				MAPPER102 = new ObjectMapper(jsonFactory);
				MAPPER102.registerModule(new RakipModule());

				MAPPER103 = new ObjectMapper(jsonFactory);
				MAPPER103.registerModule(new EMFModule());

				MAPPER104 = new ObjectMapper(jsonFactory);
				MAPPER104.registerModule(new ThreeTenModule());

				modelClasses = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
				modelClasses.put("genericModel", GenericModel.class);
				modelClasses.put("dataModel", DataModel.class);
				modelClasses.put("predictiveModel", PredictiveModel.class);
				modelClasses.put("otherModel", OtherModel.class);
				modelClasses.put("exposureModel", ExposureModel.class);
				modelClasses.put("toxicologicalModel", ToxicologicalModel.class);
				modelClasses.put("doseResponseModel", DoseResponseModel.class);
				modelClasses.put("processModel", ProcessModel.class);
				modelClasses.put("consumptionModel", ConsumptionModel.class);
				modelClasses.put("healthModel", HealthModel.class);
				modelClasses.put("riskModel", RiskModel.class);
				modelClasses.put("qraModel", QraModel.class);

			} catch (final Throwable throwable) {
				LOGGER.error("Failure during static initialization", throwable);
				throw throwable;
			}
		}

		@Override
		public void savePortObject(final FskPortObject portObject, final PortObjectZipOutputStream out,
				final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

			// model entry (file with model script)
			out.putNextEntry(new ZipEntry(MODEL));
			IOUtils.write(portObject.model, out, "UTF-8");
			out.closeEntry();

			// viz entry (file with visualization script)
			out.putNextEntry(new ZipEntry(VIZ));
			IOUtils.write(portObject.viz, out, "UTF-8");
			out.closeEntry();

			out.putNextEntry(new ZipEntry("modelType"));
			IOUtils.write(portObject.modelMetadata.getModelType(), out, "UTF-8");
			out.closeEntry();

			out.putNextEntry(new ZipEntry("swagger"));
			MAPPER104.writeValue(out, portObject.modelMetadata);
			out.closeEntry();

			// workspace entry
			if (portObject.workspace != null) {
				out.putNextEntry(new ZipEntry(WORKSPACE));
				Files.copy(portObject.workspace, out);
				out.closeEntry();
			}

			// libraries
			if (!portObject.packages.isEmpty()) {
				out.putNextEntry(new ZipEntry("library.list"));
				IOUtils.writeLines(portObject.packages, "\n", out, StandardCharsets.UTF_8);
				out.closeEntry();
			}

			// Save working directory
			if (StringUtils.isNotEmpty(portObject.workingDirectory)) {
				out.putNextEntry(new ZipEntry(WORKING_DIRECTORY));
				IOUtils.write(portObject.workingDirectory, out, "UTF-8");
				out.closeEntry();
			}

			// Save plot
			if (StringUtils.isNotEmpty(portObject.plot)) {
				out.putNextEntry(new ZipEntry(PLOT));
				IOUtils.write(portObject.plot, out, "UTF-8");
				out.closeEntry();
			}

			// Save README
			if (StringUtils.isNotEmpty(portObject.readme)) {
				out.putNextEntry(new ZipEntry(README));
				IOUtils.write(portObject.readme, out, "UTF-8");
				out.closeEntry();
			}

			// Save spreadsheet
			if (StringUtils.isNotEmpty(portObject.spreadsheet)) {
				out.putNextEntry(new ZipEntry(SPREADSHEET));
				IOUtils.write(portObject.spreadsheet, out, "UTF-8");
				out.closeEntry();
			}

			// Save simulations
			if (!portObject.simulations.isEmpty()) {
				out.putNextEntry(new ZipEntry(SIMULATION));

				try {
					final ObjectOutputStream oos = new ObjectOutputStream(out);
					oos.writeObject(portObject.simulations);
				} catch (final IOException exception) {
					// TODO: deal with exception
				}
				out.closeEntry();
			}

			// Save selected simulation index
			out.putNextEntry(new ZipEntry(SIMULATION_INDEX));

			try {
				final ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(portObject.selectedSimulationIndex);
			} catch (final IOException exception) {
				// TODO: deal with exception
			}
			out.closeEntry();

			out.close();
		}

		@Override
		@SuppressWarnings("unchecked")
		public FskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec)
				throws IOException, CanceledExecutionException {

			String modelScript = "";
			String visualizationScript = "";

			final Path workspacePath = FileUtil.createTempFile("workspace", ".r").toPath();
			List<String> packages = new ArrayList<>();

			Model modelMetadata = null;

			String workingDirectory = ""; // Empty string if not set

			String plot = ""; // Empty string if not set
			String readme = ""; // Empty string if not set
			String spreadsheet = ""; // Empty string if not set

			List<FskSimulation> simulations = new ArrayList<>();
			int selectedSimulationIndex = 0;

			ZipEntry entry;
			while ((entry = in.getNextEntry()) != null) {
				final String entryName = entry.getName();

				if (entryName.equals(MODEL)) {
					modelScript = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(VIZ)) {
					visualizationScript = IOUtils.toString(in, "UTF-8");
				}

				// If found old deprecated metadata, restore it and convert it to new EMF
				// metadata
				else if (entryName.equals(META_DATA)) {

					final de.bund.bfr.knime.fsklab.rakip.GenericModel genericModel = MAPPER102.readValue(in,
							de.bund.bfr.knime.fsklab.rakip.GenericModel.class);

					final GenericModel gm = new GenericModel();
					gm.setModelType("genericModel");
					gm.setGeneralInformation(RakipUtil.convert(genericModel.generalInformation));
					gm.setScope(RakipUtil.convert(genericModel.scope));
					gm.setDataBackground(RakipUtil.convert(genericModel.dataBackground));
					gm.setModelMath(RakipUtil.convert(genericModel.modelMath));

					modelMetadata = gm;
				}

				else if (entryName.equals(CFG_GENERAL_INFORMATION)) {
					// Read deprecated EMF metadata
					final metadata.GeneralInformation deprecatedInformation = MAPPER103.readValue(in,
							metadata.GeneralInformation.class);
					in.getNextEntry();

					final metadata.Scope deprecatedScope = MAPPER103.readValue(in, metadata.Scope.class);
					in.getNextEntry();

					final metadata.DataBackground deprecatedBackground = MAPPER103.readValue(in,
							metadata.DataBackground.class);
					in.getNextEntry();

					final metadata.ModelMath deprecatedMath = MAPPER103.readValue(in, metadata.ModelMath.class);
					in.getNextEntry();

					// Convert to new metadata schema
					final GenericModel gm = new GenericModel();
					gm.setModelType("genericModel");
					gm.setGeneralInformation(SwaggerUtil.convert(deprecatedInformation));
					gm.setScope(SwaggerUtil.convert(deprecatedScope));
					gm.setDataBackground(SwaggerUtil.convert(deprecatedBackground));
					gm.setModelMath(SwaggerUtil.convert(deprecatedMath));
					modelMetadata = gm;
				} else if (entryName.equals("modelType")) {
					// deserialize new models
					final String modelClass = IOUtils.toString(in, "UTF-8");
					in.getNextEntry();

					modelMetadata = MAPPER104.readValue(in, modelClasses.get(modelClass));
				} else if (entryName.equals(WORKSPACE)) {
					Files.copy(in, workspacePath, StandardCopyOption.REPLACE_EXISTING);
				} else if (entryName.equals("library.list")) {
					packages = IOUtils.readLines(in, "UTF-8");
				} else if (entryName.equals(WORKING_DIRECTORY)) {
					workingDirectory = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(PLOT)) {
					plot = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(README)) {
					readme = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(SPREADSHEET)) {
					spreadsheet = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(SIMULATION)) {
					try {
						final ObjectInputStream ois = new ObjectInputStream(in);
						simulations = (List<FskSimulation>) ois.readObject();
					} catch (final ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

				else if (entryName.equals(SIMULATION_INDEX)) {
					final ObjectInputStream ois = new ObjectInputStream(in);
					try {
						selectedSimulationIndex = ((Integer) ois.readObject()).intValue();
					} catch (final ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			in.close();

			final FskPortObject portObj = new FskPortObject(modelScript, visualizationScript, modelMetadata,
					workspacePath, packages, workingDirectory, plot, readme, spreadsheet);

			if (!simulations.isEmpty()) {
				portObj.simulations.addAll(simulations);
			}
			portObj.selectedSimulationIndex = selectedSimulationIndex;
			return portObj;
		}
	}

	@Override
	public JComponent[] getViews() {
		final JPanel modelScriptPanel = new ScriptPanel("Model script", model, false, false);
		final JPanel vizScriptPanel = new ScriptPanel("Visualization script", viz, false, false);
		final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		final String jsonInString = gson.toJson(modelMetadata);
		final String modelMetadataAsJSON = "<html><pre id='json'>"
				+ StringEscapeUtils.unescapeJson(StringEscapeUtils.unescapeJson(gson.toJson(jsonInString)))
				+ "</pre></html>";

		final JTextPane tree = new JTextPane();

		tree.setContentType("text/html");
		tree.setText(modelMetadataAsJSON);

		final JScrollPane metaDataPane = new JScrollPane(tree);
		metaDataPane.setName("Meta data");

		final JPanel librariesPanel = UIUtils.createLibrariesPanel(packages);

		final JPanel simulationsPanel = new SimulationsPanel();

		// Readme
		final JTextArea readmeArea = new JTextArea(readme);
		readmeArea.setEnabled(false);
		readmeArea.setWrapStyleWord(true);
		readmeArea.setLineWrap(true);

		final JPanel readmePanel = new JPanel(new BorderLayout());
		readmePanel.setName("README");
		readmePanel.add(new JScrollPane(readmeArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));

		return new JComponent[] { modelScriptPanel, vizScriptPanel, metaDataPane, librariesPanel, simulationsPanel,
				readmePanel };
	}

	private class SimulationsPanel extends FPanel {

		private static final long serialVersionUID = -4887698302872695689L;

		private final FormPanel formPanel;
		private final Map<Object, Icon> icons = new HashMap<Object, Icon>();
		private final String SELETCTED_SIMULATION_STR = "selected";

		public SimulationsPanel() {
			// Panel to show parameters (show initially the simulation 0)
			formPanel = new FormPanel(simulations.get(selectedSimulationIndex).getParameters());
			icons.put(selectedSimulationIndex, UIUtils.getResourceImageIcon("selectedsimulation.png"));
			createUI();
		}

		private void createUI() {

			final FPanel simulationPanel = new FPanel();
			simulationPanel.setLayout(new BorderLayout());
			final JScrollPane parametersPane = new JScrollPane(
					UIUtils.createTitledPanel(UIUtils.createNorthPanel(formPanel), "Parameters"));
			parametersPane.setBorder(null);

			simulationPanel.add(parametersPane, BorderLayout.WEST);

			// Panel to show preview of generated script out of parameters
			final String previewScript = buildParameterScript(simulations.get(selectedSimulationIndex));
			final ScriptPanel scriptPanel = new ScriptPanel("Preview", previewScript, false, true);
			simulationPanel.add(UIUtils.createTitledPanel(scriptPanel, "Preview script"), BorderLayout.CENTER);

			// Panel to select simulation
			final FskSimulation[] simulationsArray = simulations.toArray(new FskSimulation[simulations.size()]);

			final JComboBox<FskSimulation> simulationList = new JComboBox<>(simulationsArray);
			simulationList.setRenderer(new IconListRenderer(icons, simulationsArray));
			simulationList.addActionListener(e -> {
				// Get selected simulation
				if (simulationList.getSelectedIndex() != -1) {
					final FskSimulation selectedSimulation = (FskSimulation) simulationList.getSelectedItem();

					// Update parameters
					formPanel.setValues(selectedSimulation.getParameters());

					// Update previewPanel
					final String previewScript1 = buildParameterScript(selectedSimulation);
					scriptPanel.setText(previewScript1);
				}
			});
			simulationList.setSelectedIndex(selectedSimulationIndex);
			final JPanel selectionPanel = new JPanel();
			selectionPanel.setBackground(Color.WHITE);
			selectionPanel.add(simulationList);
			// selectionPanel.add(new
			// JLabel(simulationsArray[selectedSimulationIndex].getName()+" is the selected
			// simulation to be used by the FSK Runner to run the model"));
			final JPanel simulationSelection = UIUtils
					.createCenterPanel(UIUtils.createHorizontalPanel(new JLabel("Simulation:"), selectionPanel));

			// Build simulations panel
			setLayout(new BorderLayout());
			setName("Simulations");
			add(simulationSelection, BorderLayout.NORTH);
			add(simulationPanel, BorderLayout.CENTER);
		}

		class IconListRenderer extends DefaultListCellRenderer {
			private static final long serialVersionUID = 1L;
			private Map<Object, Icon> icons = null;
			private final String selectedSimulationName;

			public IconListRenderer(Map<Object, Icon> icons, FskSimulation[] simulationsArray) {
				this.icons = icons;
				selectedSimulationName = simulationsArray[selectedSimulationIndex].getName();
			}

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				// Get icon to use for the list item value
				Icon icon = icons.get(value);
				if (index == selectedSimulationIndex
						|| index == -1 && value.toString().trim().equals(selectedSimulationName.trim())) {
					icon = icons.get(selectedSimulationIndex);
				}
				// Set icon to display for value
				label.setIcon(icon);
				return label;
			}
		}

		class FormPanel extends FPanel {

			private static final long serialVersionUID = 4324891441984883445L;
			private final JTextField[] fields;

			FormPanel(LinkedHashMap<String, String> parameters) {
				fields = new JTextField[parameters.size()];
				for (int i = 0; i < parameters.size(); i++) {
					fields[i] = new JTextField();
				}

				createUI(parameters);
			}

			private void createUI(LinkedHashMap<String, String> parameters) {

				// Create labels
				final List<FLabel> labels = parameters.keySet().stream().map(FLabel::new).collect(Collectors.toList());

				// Create field panels
				final List<JPanel> fieldPanels = new ArrayList<>(parameters.size());

				int i = 0;
				for (final String value : parameters.values()) {
					final JPanel panel = createFieldPanel(fields[i], value);
					fieldPanels.add(panel);
					i++;
				}

				final int n = labels.size();

				final FPanel leftPanel = new FPanel();
				leftPanel.setLayout(new GridLayout(n, 1, 5, 5));
				labels.forEach(leftPanel::add);

				final FPanel rightPanel = new FPanel();
				rightPanel.setLayout(new GridLayout(n, 1, 5, 5));
				fieldPanels.forEach(rightPanel::add);

				setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				setLayout(new BorderLayout(5, 5));
				add(leftPanel, BorderLayout.WEST);
				add(rightPanel, BorderLayout.CENTER);
			}

			public void setValues(LinkedHashMap<String, String> parameters) {
				int i = 0;
				for (final String value : parameters.values()) {
					fields[i].setText(value);
					i++;
				}
			}

			private JPanel createFieldPanel(JTextField field, String value) {
				field.setColumns(30);
				field.setBackground(UIUtils.WHITE);
				field.setText(value);
				field.setHorizontalAlignment(JTextField.RIGHT);
				field.setEditable(false);
				field.setBorder(null);

				final JButton copyButton = UIUtils.createCopyButton();
				copyButton.setVisible(false);

				field.addFocusListener(new FieldListener(copyButton));

				copyButton.addActionListener(e -> {
					final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(new StringSelection(field.getText()), null);
				});

				final JPanel fieldPanel = new JPanel(new BorderLayout());
				fieldPanel.setBackground(UIUtils.WHITE);
				fieldPanel.add(field, BorderLayout.CENTER);
				fieldPanel.add(copyButton, BorderLayout.EAST);

				final Border matteBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, UIUtils.BLUE);
				final Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
				final Border compoundBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
				fieldPanel.setBorder(compoundBorder);

				fieldPanel.setPreferredSize(new Dimension(100, 20));

				return fieldPanel;
			}

			private class FieldListener implements FocusListener {

				private final JButton button;

				public FieldListener(JButton button) {
					this.button = button;
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					button.setVisible(true);
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					button.setVisible(false);
				}
			}
		}

	}

	/** Builds string with R parameters script out. */
	private static String buildParameterScript(FskSimulation simulation) {

		String paramScript = "";
		for (final Map.Entry<String, String> entry : simulation.getParameters().entrySet()) {
			final String parameterName = entry.getKey();
			final String parameterValue = entry.getValue();

			paramScript += parameterName + " <- " + parameterValue + "\n";
		}

		return paramScript;
	}

	@Override
	public String toString() {
		return modelMetadata != null ? modelMetadata.toString() : "";
	}
}
