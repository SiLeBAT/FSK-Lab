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
package de.bund.bfr.knime.fsklab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.emfjson.jackson.module.EMFModule;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.util.FileUtil;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
import jakarta.json.*;
import jakarta.json.JsonValue;
import metadata.SwaggerUtil;

/**
 * A port object for an combined FSK model port providing two FSK Models.
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */
public class CombinedFskPortObject extends FskPortObject {
	private static NodeLogger LOGGER = NodeLogger.getLogger(CombinedFskPortObject.class);
	final FskPortObject firstFskPortObject;

	public FskPortObject getFirstFskPortObject() {
		return firstFskPortObject;
	}

	public FskPortObject getSecondFskPortObject() {
		return secondFskPortObject;
	}

	final FskPortObject secondFskPortObject;
	List<JoinRelation> joinerRelation;

	public List<JoinRelation> getJoinerRelation() {
		return joinerRelation;
	}

	public void setJoinerRelation(List<JoinRelation> joinerRelation) {
		this.joinerRelation = joinerRelation;
	}

	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FskPortObject.class);

	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(FskPortObject.class, true);

	public static final String[] RESOURCE_EXTENSIONS = new String[] { "txt", "RData", "csv" };

	private static int numOfInstances = 0;
	private static Random ran = new Random();

	public CombinedFskPortObject(final String model, final String param, final String viz, final Model modelMetadata,
			final Path workspace, final List<String> packages, final String workingDirectory, final String plot,
			final FskPortObject firstFskPortObject, final FskPortObject secondFskPortObject) throws IOException {
		super(model, viz, modelMetadata, workspace, packages, workingDirectory, plot, "", "");
		this.firstFskPortObject = firstFskPortObject;
		this.secondFskPortObject = secondFskPortObject;
		objectNum = numOfInstances;
		numOfInstances += 1;
	}

	public CombinedFskPortObject(final String workingDirectory, final List<String> packages,
			final FskPortObject firstFskPortObject, final FskPortObject secondFskPortObject) throws IOException {
		super(workingDirectory, "", packages);
		this.firstFskPortObject = firstFskPortObject;
		this.secondFskPortObject = secondFskPortObject;
		objectNum = numOfInstances;
		numOfInstances += 1;
	}

	public CombinedFskPortObject(final String model, final String viz, final Model modelMetadata,
			final String workingDirectory, final List<String> packages, final FskPortObject firstFskPortObject,
			final FskPortObject secondFskPortObject) throws IOException {
		super(workingDirectory, "", packages);
		this.model = model;
		this.viz = viz;
		this.firstFskPortObject = firstFskPortObject;
		this.secondFskPortObject = secondFskPortObject;
		this.modelMetadata = modelMetadata;

		objectNum = numOfInstances;
		numOfInstances += 1;
	}

	@Override
	public FskPortObjectSpec getSpec() {
		return FskPortObjectSpec.INSTANCE;
	}

	@Override
	public String getSummary() {
		return "Combined FSK Object";
	}

	/**
	 * Serializer used to save this port object.
	 * 
	 * @return a {@link CombinedFskPortObject}.
	 */
	public static final class Serializer extends PortObjectSerializer<CombinedFskPortObject> {
		private static final String COMBINED = "COMBINED";
		private static final String MODEL = "model.R";
		private static final String VIZ = "viz.R";
		private static final String META_DATA = "metaData";

		private static final String CFG_GENERAL_INFORMATION = "generalInformation";
		private static final String JOINED_GENERAL_INFORMATION = "joinedGeneralInformation";
		private static final String JOINED_SIMULATION = "joinedsimulation";
		private static final String JOINED_WORKSPACE = "joinedworkspace";
		private static final String JOINED_VIZ = "joinedviz.R";

		private static final String WORKSPACE = "workspace";
		private static final String SIMULATION = "simulation";
		private static final String SIMULATION_INDEX = "xSimulationIndex";

		private static final String WORKING_DIRECTORY = "workingDirectory";

		private static final String PLOT = "plot";

		private static final String README = "readme";

		private static final String SPREADSHEET = "spreadsheet";

		private static final String JOINER_RELATION = "joinrelation";

		private static final String LIBRARY_LIST = "library.list";
		private static final String BREAK = "break";

		
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
				JsonFactory jsonFactory = new JsonFactory();
				jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
				jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

				MAPPER102 = new ObjectMapper(jsonFactory);
				MAPPER102.registerModule(new RakipModule());

				MAPPER103 = new ObjectMapper(jsonFactory);
				MAPPER103.registerModule(new EMFModule());

				MAPPER104 = new ObjectMapper(jsonFactory);
				MAPPER104.registerModule(new ThreeTenModule());

				modelClasses =  new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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

			} catch (Throwable throwable) {
				LOGGER.error("Failure during static initialization", throwable);
				throw throwable;
			}
		}

		@Override
		public void savePortObject(final CombinedFskPortObject portObject, final PortObjectZipOutputStream out,
				final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
			saveFSKPortObject(portObject, out, exec);
			out.close();
		}

		public void saveFSKPortObject(FskPortObject portObject, final PortObjectZipOutputStream out,
				final ExecutionMonitor exec) throws IOException {

			// First FSK Object
			// model entry (file with model script)
			if (portObject instanceof CombinedFskPortObject) {
				// write tag value to check the type of the fsk port object when read it back
				int level = ran.nextInt();
				out.putNextEntry(new ZipEntry(COMBINED + level));
				IOUtils.write(COMBINED + level, out, "UTF-8");
				out.closeEntry();

				CombinedFskPortObject joinedPortObject = (CombinedFskPortObject) portObject;

				if (joinedPortObject.joinerRelation == null) {
					joinedPortObject.joinerRelation = new ArrayList<>();
				}
				writeEObjectList(JOINER_RELATION + level, joinedPortObject.joinerRelation, out);
				// workspace entry
				if (portObject.workspace != null) {
					out.putNextEntry(new ZipEntry(JOINED_WORKSPACE + level));
					Files.copy(portObject.workspace, out);
					out.closeEntry();
				}
				// Save simulations
				if (!portObject.simulations.isEmpty()) {
					out.putNextEntry(new ZipEntry(JOINED_SIMULATION + level));

					try {
						ObjectOutputStream oos = new ObjectOutputStream(out);
						oos.writeObject(portObject.simulations);
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					out.closeEntry();
				}

				// Write Joined Object Meta data: model type and metadata
				out.putNextEntry(new ZipEntry("modelType" + level));
				IOUtils.write(portObject.modelMetadata.getModelType(), out, "UTF-8");
				out.closeEntry();

				out.putNextEntry(new ZipEntry("swagger" + level));
				MAPPER104.writeValue(out, portObject.modelMetadata);
				out.closeEntry();

				// joined viz entry (file with visualization script)
				out.putNextEntry(new ZipEntry(JOINED_VIZ + level));
				IOUtils.write(portObject.viz, out, "UTF-8");
				out.closeEntry();

				saveFSKPortObject(joinedPortObject.getFirstFskPortObject(), out, exec);
				saveFSKPortObject(joinedPortObject.getSecondFskPortObject(), out, exec);

			} else {
				// model entry (file with model script)
				int level = ran.nextInt();
				out.putNextEntry(new ZipEntry(MODEL + level));
				IOUtils.write(portObject.model, out, "UTF-8");
				out.closeEntry();

				// viz entry (file with visualization script)
				out.putNextEntry(new ZipEntry(VIZ + level));
				IOUtils.write(portObject.viz, out, "UTF-8");
				out.closeEntry();

				// model type and metadata
				out.putNextEntry(new ZipEntry("modelType" + level));
				IOUtils.write(portObject.modelMetadata.getModelType(), out, "UTF-8");
				out.closeEntry();

				out.putNextEntry(new ZipEntry("swagger" + level));
				MAPPER104.writeValue(out, portObject.modelMetadata);
				out.closeEntry();

				// workspace entry
				if (portObject.workspace != null) {
					out.putNextEntry(new ZipEntry(WORKSPACE + level));
					Files.copy(portObject.workspace, out);
					out.closeEntry();
				}

				// libraries
				if (!portObject.packages.isEmpty()) {
					out.putNextEntry(new ZipEntry(LIBRARY_LIST + level));
					IOUtils.writeLines(portObject.packages, "\n", out, StandardCharsets.UTF_8);
					out.closeEntry();
				}

				// Save working directory
				if (StringUtils.isNotEmpty(portObject.getWorkingDirectory())) {
					out.putNextEntry(new ZipEntry(WORKING_DIRECTORY + level));
					IOUtils.write(portObject.getWorkingDirectory(), out, "UTF-8");
					out.closeEntry();
				}

				// Save plot
				if (StringUtils.isNotEmpty(portObject.getPlot())) {
					out.putNextEntry(new ZipEntry(PLOT + level));
					IOUtils.write(portObject.getPlot(), out, "UTF-8");
					out.closeEntry();
				}

				// Save README
				if (StringUtils.isNotEmpty(portObject.getReadme())) {
					out.putNextEntry(new ZipEntry(README + level));
					IOUtils.write(portObject.getReadme(), out, "UTF-8");
					out.closeEntry();
				}

				// Save spreadsheet
				if (StringUtils.isNotEmpty(portObject.getSpreadsheet())) {
					out.putNextEntry(new ZipEntry(SPREADSHEET + level));
					IOUtils.write(portObject.getSpreadsheet(), out, "UTF-8");
					out.closeEntry();
				}

				// Save simulations
				if (!portObject.simulations.isEmpty()) {
					out.putNextEntry(new ZipEntry(SIMULATION + level));

					try {
						ObjectOutputStream oos = new ObjectOutputStream(out);
						oos.writeObject(portObject.simulations);
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					out.closeEntry();
				}

				// Save selected simulation index
				out.putNextEntry(new ZipEntry(SIMULATION_INDEX + level));

				try {
					ObjectOutputStream oos = new ObjectOutputStream(out);
					oos.writeObject(portObject.selectedSimulationIndex);
					out.closeEntry();
					out.putNextEntry(new ZipEntry(BREAK + level));
					IOUtils.write("", out, "UTF-8");
					out.closeEntry();
				} catch (IOException exception) {
					exception.printStackTrace();
				}

			}
		}

		@SuppressWarnings("unchecked")
		public FskPortObject loadFSKPortObject(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec)
				throws IOException, CanceledExecutionException {

			String modelScript = "";
			String visualizationScript = "";

			Path workspacePath = FileUtil.createTempFile("workspace", ".r").toPath();
			List<String> packages = new ArrayList<>();

			Model modelMetadata = null;

			String workingDirectory = ""; // Empty string if not set

			String plot = ""; // Empty string if not set
			String readme = ""; // Empty string if not set
			String spreadsheet = ""; // Empty string if not set

			List<FskSimulation> simulations = new ArrayList<>();
			int selectedSimulationIndex = 0;
			List<JoinRelation> joinerRelation = new ArrayList<>();

			ZipEntry entry;
			while ((entry = in.getNextEntry()) != null) {
				String entryName = entry.getName();
				// check if the entry contains combined FSK object
				if (entryName.startsWith(COMBINED)) {
					String level = entryName.substring(COMBINED.length(), entryName.length());
					// read relations
					// jump one step since we know it has relation in the next part
					entry = in.getNextEntry();
					entryName = entry.getName();
					if (entryName.startsWith(JOINER_RELATION + level)) {
						try {
							joinerRelation = ((List<JoinRelation>) readEObjectList(in, JoinRelation.class));
						} catch (InvalidSettingsException e) {
							e.printStackTrace();
						}

					}
					entry = in.getNextEntry();
					entryName = entry.getName();

					if (entryName.startsWith(JOINED_WORKSPACE + level)) {
						Files.copy(in, workspacePath, StandardCopyOption.REPLACE_EXISTING);
						entry = in.getNextEntry();
						entryName = entry.getName();
						if (entryName.startsWith(JOINED_SIMULATION + level)) {
							try {
								ObjectInputStream ois = new ObjectInputStream(in);
								simulations = ((List<FskSimulation>) ois.readObject());
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					} else if (entryName.startsWith(JOINED_SIMULATION + level)) {
						try {
							ObjectInputStream ois = new ObjectInputStream(in);
							simulations = ((List<FskSimulation>) ois.readObject());
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					entry = in.getNextEntry();
					entryName = entry.getName();
					if (entryName.equals("modelType"+ level)) {
						// deserialize new models
						String modelClass = IOUtils.toString(in, "UTF-8");
						in.getNextEntry();

						modelMetadata = MAPPER104.readValue(in, modelClasses.get(modelClass));
					}
					else if (entryName.startsWith(JOINED_GENERAL_INFORMATION + level)) {
						metadata.GeneralInformation deprecatedInformation = readEObject(in,
								metadata.GeneralInformation.class);
						in.getNextEntry();
						metadata.Scope deprecatedScope = readEObject(in, metadata.Scope.class);
						in.getNextEntry();
						metadata.DataBackground deprecatedBackground = readEObject(in, metadata.DataBackground.class);
						in.getNextEntry();
						metadata.ModelMath deprecatedMath = readEObject(in, metadata.ModelMath.class);

						// Read 1.0.3 metadata
						GenericModel gm = new GenericModel();
						gm.setModelType("genericModel");
						gm.setGeneralInformation(SwaggerUtil.convert(deprecatedInformation));
						gm.setScope(SwaggerUtil.convert(deprecatedScope));
						gm.setDataBackground(SwaggerUtil.convert(deprecatedBackground));
						gm.setModelMath(SwaggerUtil.convert(deprecatedMath));

						modelMetadata = gm;
					}

					entry = in.getNextEntry();
					entryName = entry.getName();
					if (entryName.startsWith(JOINED_VIZ + level)) {
						visualizationScript = IOUtils.toString(in, "UTF-8");
					}

					// read first FSKObject
					FskPortObject firstFSKObject = loadFSKPortObject(in, spec, exec);
					// read second FSKObject
					FskPortObject secondFSKObject = loadFSKPortObject(in, spec, exec);

					// build combined object out of the previous objects
					final CombinedFskPortObject portObj = new CombinedFskPortObject(modelScript, visualizationScript,
							modelMetadata, FileUtil.createTempDir("combined").getAbsolutePath(), new ArrayList<>(),
							firstFSKObject, secondFSKObject);
					portObj.workspace = workspacePath;
					if (!joinerRelation.isEmpty()) {
						portObj.setJoinerRelation(joinerRelation);
					}
					if (!simulations.isEmpty()) {
						portObj.simulations.addAll(simulations);
					}
					return portObj;

				} else {

					if (entryName.startsWith(MODEL)) {
						modelScript = IOUtils.toString(in, "UTF-8");
					} else if (entryName.startsWith(VIZ)) {
						visualizationScript = IOUtils.toString(in, "UTF-8");
					}
					
					if (entryName.startsWith("modelType")) {
						// deserialize new models
						String modelClass = IOUtils.toString(in, "UTF-8");
						in.getNextEntry();

						modelMetadata = MAPPER104.readValue(in, modelClasses.get(modelClass));
					}
					// If found old deprecated metadata, restore it and convert it to new EMF
					// metadata
					else if (entryName.startsWith(META_DATA)) {

						final String metaDataAsString = IOUtils.toString(in, "UTF-8");
						ObjectMapper mapper = new ObjectMapper().registerModule(new RakipModule());
						de.bund.bfr.knime.fsklab.rakip.GenericModel deprecatedModel = mapper.readValue(metaDataAsString,
								de.bund.bfr.knime.fsklab.rakip.GenericModel.class);

						GenericModel gm = new GenericModel();
						gm.setModelType("genericModel");
						gm.setGeneralInformation(RakipUtil.convert(deprecatedModel.generalInformation));
						gm.setScope(RakipUtil.convert(deprecatedModel.scope));
						gm.setDataBackground(RakipUtil.convert(deprecatedModel.dataBackground));
						gm.setModelMath(RakipUtil.convert(deprecatedModel.modelMath));
					}

					else if (entryName.startsWith(CFG_GENERAL_INFORMATION)) {
						metadata.GeneralInformation deprecatedInformation = readEObject(in,
								metadata.GeneralInformation.class);
						in.getNextEntry();

						metadata.Scope deprecatedScope = readEObject(in, metadata.Scope.class);
						in.getNextEntry();

						metadata.DataBackground deprecatedBackground = readEObject(in, metadata.DataBackground.class);
						in.getNextEntry();

						metadata.ModelMath deprecatedMath = readEObject(in, metadata.ModelMath.class);
						in.getNextEntry();

						GenericModel gm = new GenericModel();
						gm.setModelType("genericModel");
						gm.setGeneralInformation(SwaggerUtil.convert(deprecatedInformation));
						gm.setScope(SwaggerUtil.convert(deprecatedScope));
						gm.setDataBackground(SwaggerUtil.convert(deprecatedBackground));
						gm.setModelMath(SwaggerUtil.convert(deprecatedMath));
						modelMetadata = gm;
					} else if (entryName.startsWith(WORKSPACE)) {
						Files.copy(in, workspacePath, StandardCopyOption.REPLACE_EXISTING);
					} else if (entryName.startsWith(LIBRARY_LIST)) {
						packages = IOUtils.readLines(in, "UTF-8");
					} else if (entryName.startsWith(WORKING_DIRECTORY)) {
						workingDirectory = IOUtils.toString(in, "UTF-8");
					} else if (entryName.startsWith(PLOT)) {
						plot = IOUtils.toString(in, "UTF-8");
					} else if (entryName.startsWith(README)) {
						readme = IOUtils.toString(in, "UTF-8");
					} else if (entryName.startsWith(SPREADSHEET)) {
						spreadsheet = IOUtils.toString(in, "UTF-8");
					} else if (entryName.startsWith(SIMULATION)) {
						try {
							ObjectInputStream ois = new ObjectInputStream(in);
							simulations = ((List<FskSimulation>) ois.readObject());
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}

					else if (entryName.startsWith(SIMULATION_INDEX)) {
						ObjectInputStream ois = new ObjectInputStream(in);
						try {
							selectedSimulationIndex = ((Integer) ois.readObject()).intValue();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					} else if (entryName.startsWith(BREAK)) {
						break;
					}

				}
			}
			final FskPortObject portObj = new FskPortObject(modelScript, visualizationScript, modelMetadata,
					workspacePath, packages, workingDirectory, plot, readme, spreadsheet);

			if (!simulations.isEmpty()) {
				portObj.simulations.addAll(simulations);
			}
			portObj.selectedSimulationIndex = selectedSimulationIndex;
			return portObj;
		}

		@Override
		public CombinedFskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec,
				ExecutionMonitor exec) throws IOException, CanceledExecutionException {
			CombinedFskPortObject portObj = (CombinedFskPortObject) loadFSKPortObject(in, spec, exec);
			in.close();
			return portObj;
		}

		private <T> T readEObject(PortObjectZipInputStream zipStream, Class<T> valueType) throws IOException {
			String jsonStr = IOUtils.toString(zipStream, "UTF-8");
			ObjectMapper mapper = EMFModule.setupDefaultMapper();
			return mapper.readValue(jsonStr, valueType);
		}

		private static <T> T getEObjectFromJson(String jsonStr, Class<T> valueType)
				throws InvalidSettingsException, JsonParseException, JsonMappingException, IOException {
			ObjectMapper mapper = EMFModule.setupDefaultMapper();
			return mapper.readValue(jsonStr, valueType);
		}

		@SuppressWarnings("unchecked")
		private <T> List<T> readEObjectList(PortObjectZipInputStream zipStream, Class<T> valueType)
				throws IOException, InvalidSettingsException {
			List<JoinRelation> joinerRelation = new ArrayList<>();
			String jsonStr = IOUtils.toString(zipStream, "UTF-8");

			if (jsonStr != null) {

				JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
				JsonArray relationJsonArray = jsonReader.readArray();
				jsonReader.close();
				
				for (JsonValue element : relationJsonArray) {
					JsonObject sourceTargetRelation = ((JsonObject) element);
					JoinRelation jR = new JoinRelation();
					if (sourceTargetRelation.containsKey("command")) {
						jR.setCommand(sourceTargetRelation.getString("command"));
					}
					if (sourceTargetRelation.containsKey("sourceParam")) {
						jR.setSourceParam(getEObjectFromJson(sourceTargetRelation.get("sourceParam").toString(),
								Parameter.class));
					}
					if (sourceTargetRelation.containsKey("targetParam")) {
						jR.setTargetParam(getEObjectFromJson(sourceTargetRelation.get("targetParam").toString(),
								Parameter.class));
					}
					joinerRelation.add(jR);

				}

			}

			return (List<T>) joinerRelation;
		}

		private static <T extends List> void writeEObjectList(String entryName, T value, PortObjectZipOutputStream out)
				throws IOException {

			out.putNextEntry(new ZipEntry(entryName));
			String jsonStr = "[";
			for (Object o : value) {
				JoinRelation jR = ((JoinRelation) o);
				String repre = jR.getJsonReresentaion();
				jsonStr += repre + ",";
			}
			if (jsonStr.length() > 1) {
				jsonStr = jsonStr.substring(0, jsonStr.length() - 1) + "]";
			} else {
				jsonStr += "]";
			}
			IOUtils.write(jsonStr, out, "UTF-8");

			out.closeEntry();
		}

	}

	class CommandScript {
		String id;
		String script;

		public CommandScript(String id, String script) {
			this.id = id;
			this.script = script;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getScript() {
			return script;
		}

		public void setScript(String script) {
			this.script = script;
		}

		public String toString() {
			return id;
		}

	}

	public void buildScriptNodes(DefaultMutableTreeNode top, FskPortObject currentPortObject, boolean modelScriptFlag) {
		if (currentPortObject instanceof CombinedFskPortObject) {
			DefaultMutableTreeNode anotherJoinedModel = new DefaultMutableTreeNode("joined");
			buildScriptNodes(anotherJoinedModel, ((CombinedFskPortObject) currentPortObject).getFirstFskPortObject(),
					modelScriptFlag);
			if (modelScriptFlag) {
				StringBuilder script = new StringBuilder();
				String language = "";
				if (((CombinedFskPortObject) currentPortObject).getJoinerRelation() != null
						&& ((CombinedFskPortObject) currentPortObject).getJoinerRelation().size() > 0) {
					((CombinedFskPortObject) currentPortObject).getJoinerRelation().stream().forEach(connection -> {
						script.append(connection.getTargetParam().getId() + " <- " + connection.getCommand() + "\n");
					});

					language = ((CombinedFskPortObject) currentPortObject).getJoinerRelation().get(0)
							.getLanguage_written_in();
				}
				anotherJoinedModel.add(new DefaultMutableTreeNode(new CommandScript(
						"Joining Model Script" + (language != null ? "( " + language + " )" : ""), script.toString())));
			}
			buildScriptNodes(anotherJoinedModel, ((CombinedFskPortObject) currentPortObject).getSecondFskPortObject(),
					modelScriptFlag);

			top.add(anotherJoinedModel);
		} else {
			DefaultMutableTreeNode childModel = new DefaultMutableTreeNode(currentPortObject);
			top.add(childModel);
		}
	}

	/** {Override} */
	@Override
	public JComponent[] getViews() {
		JPanel modelScriptPanel = new ScriptPanel("Model script", false);
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Model Scripts");
		buildScriptNodes(top, this, true);
		JTree modelTree = new JTree(top);
		modelTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

				if (node == null)
					return;

				Object script = node.getUserObject();
				if (node.isLeaf()) {
					if (script instanceof FskPortObject) {
						FskPortObject selectedFSK = (FskPortObject) script;
						((ScriptPanel) modelScriptPanel).setText(selectedFSK.model);
					} else {
						((ScriptPanel) modelScriptPanel).setText(((CommandScript) script).getScript());
					}
				}

			}
		});
		modelTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		modelTree.setVisible(true);
		((ScriptPanel) modelScriptPanel).setScriptTree(modelTree);

		JPanel vizScriptPanel = new ScriptPanel("Visualization script", false);
		DefaultMutableTreeNode visTop = new DefaultMutableTreeNode("Visualization Scripts");
		buildScriptNodes(visTop, this, false);
		JTree visTree = new JTree(visTop);
		visTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) visTree.getLastSelectedPathComponent();

				if (node == null)
					return;

				Object script = node.getUserObject();
				if (node.isLeaf()) {
					FskPortObject selectedFSK = (FskPortObject) script;
					((ScriptPanel) vizScriptPanel).setText(selectedFSK.viz);
				}

			}
		});
		visTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		visTree.setVisible(true);
		((ScriptPanel) vizScriptPanel).setScriptTree(visTree);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonString = gson.toJson(modelMetadata);

		final JScrollPane metaDataPane = new JScrollPane(new JTextArea(jsonString));
		metaDataPane.setName("Meta data");

		final JPanel librariesPanel = UIUtils.createLibrariesPanel(packages);

		JPanel simulationsPanel = new SimulationsPanel();

		// Readme
		JTextArea readmeArea = new JTextArea("");
		readmeArea.setEnabled(false);

		JPanel readmePanel = new ScriptPanel("README", false);
		DefaultMutableTreeNode readmetop = new DefaultMutableTreeNode("Readme");
		buildScriptNodes(readmetop, this, false);
		JTree readmeTree = new JTree(readmetop);
		readmeTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) readmeTree.getLastSelectedPathComponent();

				if (node == null)
					return;

				Object script = node.getUserObject();
				if (node.isLeaf()) {
					FskPortObject selectedFSK = (FskPortObject) script;
					((ScriptPanel) readmePanel).setText(selectedFSK.getReadme());
				}

			}
		});
		readmeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		readmeTree.setVisible(true);
		((ScriptPanel) readmePanel).setScriptTree(readmeTree);

		return new JComponent[] { modelScriptPanel, vizScriptPanel, metaDataPane, librariesPanel, simulationsPanel,
				readmePanel };
	}

	private class SimulationsPanel extends FPanel {

		private static final long serialVersionUID = -4887698302872695689L;

		private final FormPanel formPanel;
		private Map<Object, Icon> icons = new HashMap<Object, Icon>();
		private final String SELETCTED_SIMULATION_STR = "selected";

		public SimulationsPanel() {
			// Panel to show parameters (show initially the simulation 0)
			formPanel = new FormPanel(simulations.get(selectedSimulationIndex).getParameters());
			icons.put(selectedSimulationIndex, UIUtils.getResourceImageIcon("selectedsimulation.png"));
			createUI();
		}

		private void createUI() {

			FPanel simulationPanel = new FPanel();
			simulationPanel.setLayout(new BorderLayout());
			JScrollPane parametersPane = new JScrollPane(
					UIUtils.createTitledPanel(UIUtils.createNorthPanel(formPanel), "Parameters"));
			parametersPane.setBorder(null);

			simulationPanel.add(parametersPane, BorderLayout.WEST);

			// Panel to show preview of generated script out of parameters
			String previewScript = buildParameterScript(simulations.get(selectedSimulationIndex));
			ScriptPanel scriptPanel = new ScriptPanel("Preview", previewScript, false, true);
			simulationPanel.add(UIUtils.createTitledPanel(scriptPanel, "Preview script"), BorderLayout.CENTER);

			// Panel to select simulation
			FskSimulation[] simulationsArray = simulations.toArray(new FskSimulation[simulations.size()]);

			JComboBox<FskSimulation> simulationList = new JComboBox<FskSimulation>(simulationsArray);
			simulationList.setRenderer(new IconListRenderer(icons, simulationsArray));
			simulationList.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Get selected simulation
					if (simulationList.getSelectedIndex() != -1) {
						FskSimulation selectedSimulation = (FskSimulation) simulationList.getSelectedItem();

						// Update parameters
						formPanel.setValues(selectedSimulation.getParameters());

						// Update previewPanel
						String previewScript = buildParameterScript(selectedSimulation);
						scriptPanel.setText(previewScript);
					}
				}
			});
			simulationList.setSelectedIndex(selectedSimulationIndex);
			JPanel selectionPanel = new JPanel();
			selectionPanel.setBackground(Color.WHITE);
			selectionPanel.add(simulationList);
			// selectionPanel.add(new
			// JLabel(simulationsArray[selectedSimulationIndex].getName()+" is the selected
			// simulation to be used by the FSK Runner to run the model"));
			JPanel simulationSelection = UIUtils
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
			private FskSimulation[] simulationsArray;
			private String selectedSimulationName;

			public IconListRenderer(Map<Object, Icon> icons, FskSimulation[] simulationsArray) {
				this.icons = icons;
				this.simulationsArray = simulationsArray;
				this.selectedSimulationName = simulationsArray[selectedSimulationIndex].getName();
			}

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				// Get icon to use for the list item value
				Icon icon = icons.get(value);
				if (index == selectedSimulationIndex
						|| (index == -1 && value.toString().trim().equals(selectedSimulationName.trim()))) {
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
				List<FLabel> labels = parameters.keySet().stream().map(FLabel::new).collect(Collectors.toList());

				// Create field panels
				List<JPanel> fieldPanels = new ArrayList<>(parameters.size());

				int i = 0;
				for (String value : parameters.values()) {
					JPanel panel = createFieldPanel(fields[i], value);
					fieldPanels.add(panel);
					i++;
				}

				int n = labels.size();

				FPanel leftPanel = new FPanel();
				leftPanel.setLayout(new GridLayout(n, 1, 5, 5));
				labels.forEach(leftPanel::add);

				FPanel rightPanel = new FPanel();
				rightPanel.setLayout(new GridLayout(n, 1, 5, 5));
				fieldPanels.forEach(rightPanel::add);

				setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				setLayout(new BorderLayout(5, 5));
				add(leftPanel, BorderLayout.WEST);
				add(rightPanel, BorderLayout.CENTER);
			}

			public void setValues(LinkedHashMap<String, String> parameters) {
				int i = 0;
				for (String value : parameters.values()) {
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

				JButton copyButton = UIUtils.createCopyButton();
				copyButton.setVisible(false);

				field.addFocusListener(new FieldListener(copyButton));

				copyButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(new StringSelection(field.getText()), null);
					}
				});

				JPanel fieldPanel = new JPanel(new BorderLayout());
				fieldPanel.setBackground(UIUtils.WHITE);
				fieldPanel.add(field, BorderLayout.CENTER);
				fieldPanel.add(copyButton, BorderLayout.EAST);

				Border matteBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, UIUtils.BLUE);
				Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
				Border compoundBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
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
		for (Map.Entry<String, String> entry : simulation.getParameters().entrySet()) {
			String parameterName = entry.getKey();
			String parameterValue = entry.getValue();

			paramScript += parameterName + " <- " + parameterValue + "\n";
		}

		return paramScript;
	}
}
