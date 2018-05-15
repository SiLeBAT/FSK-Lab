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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.RakipUtil;
import metadata.DataBackground;
import metadata.GeneralInformation;
import metadata.MetadataFactory;
import metadata.MetadataPackage;
import metadata.MetadataTree;
import metadata.ModelMath;
import metadata.Scope;

/**
 * A port object for an FSK model port providing R scripts and model meta data.
 * 
 * @author Miguel Alba, BfR, Berlin.
 */
public class FskPortObject implements PortObject {

  /**
   * Convenience access member for <code>new PortType(FSKPortObject.class)</code>
   */
  public static final PortType TYPE =
      PortTypeRegistry.getInstance().getPortType(FskPortObject.class);
  public static final PortType TYPE_OPTIONAL =
      PortTypeRegistry.getInstance().getPortType(FskPortObject.class, true);

  public static final String[] RESOURCE_EXTENSIONS = new String[] {"txt", "RData", "csv"};

  /** Model script. */
  public String model;

  /** Parameters script. */
  public String param;

  /** Visualization script. */
  public String viz;

  /** Paths to resources: plain text files and R workspace files (.rdata). */
  public final Path workingDirectory;

  /**
   * R workspace file with the results of running the model. It may be null if the model has not
   * been run.
   */
  public Path workspace;

  /** List of library files. Files of the libraries used by the model. */
  public final Set<File> libs;

  private static int numOfInstances = 0;

  public int objectNum;

  public final List<FskSimulation> simulations = new ArrayList<>();

  // EMF metadata
  public GeneralInformation generalInformation =
      MetadataFactory.eINSTANCE.createGeneralInformation();
  public Scope scope = MetadataFactory.eINSTANCE.createScope();
  public DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();
  public ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();

  public FskPortObject(final String model, final String param, final String viz,
      final GeneralInformation generalInformation, final Scope scope,
      final DataBackground dataBackground, final ModelMath modelMath, final Path workspace,
      final Set<File> libs, final Path workingDirectory) throws IOException {
    this.model = model;
    this.param = param;
    this.viz = viz;

    this.generalInformation = generalInformation;
    this.scope = scope;
    this.dataBackground = dataBackground;
    this.modelMath = modelMath;

    this.workspace = workspace;
    this.libs = libs;

    this.workingDirectory = workingDirectory;

    objectNum = numOfInstances;
    numOfInstances += 1;
  }

  public FskPortObject(final Path workingDirectory, final Set<File> libs) throws IOException {
    this.workingDirectory = workingDirectory;
    this.libs = libs;
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
   * Serializer used to save this port object.
   * 
   * @return a {@link FskPortObject}.
   */
  public static final class Serializer extends PortObjectSerializer<FskPortObject> {

    private static final String MODEL = "model.R";
    private static final String PARAM = "param.R";
    private static final String VIZ = "viz.R";
    private static final String META_DATA = "metaData";

    private static final String CFG_GENERAL_INFORMATION = "generalInformation";
    private static final String CFG_SCOPE = "scope";
    private static final String CFG_DATA_BACKGROUND = "dataBackground";
    private static final String CFG_MODEL_MATH = "modelMath";

    private static final String WORKSPACE = "workspace";
    private static final String SIMULATION = "simulation";

    final ResourceSet resourceSet = new ResourceSetImpl();

    @Override
    public void savePortObject(final FskPortObject portObject, final PortObjectZipOutputStream out,
        final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

      // model entry (file with model script)
      out.putNextEntry(new ZipEntry(MODEL));
      IOUtils.write(portObject.model, out, "UTF-8");
      out.closeEntry();

      // param entry (file with param script)
      out.putNextEntry(new ZipEntry(PARAM));
      IOUtils.write(portObject.param, out, "UTF-8");

      out.closeEntry();

      // viz entry (file with visualization script)
      out.putNextEntry(new ZipEntry(VIZ));
      IOUtils.write(portObject.viz, out, "UTF-8");
      out.closeEntry();

      writeEObject(CFG_GENERAL_INFORMATION, portObject.generalInformation, out);
      writeEObject(CFG_SCOPE, portObject.scope, out);
      writeEObject(CFG_DATA_BACKGROUND, portObject.dataBackground, out);
      writeEObject(CFG_MODEL_MATH, portObject.modelMath, out);

      // workspace entry
      if (portObject.workspace != null) {
        out.putNextEntry(new ZipEntry(WORKSPACE));
        Files.copy(portObject.workspace, out);
        out.closeEntry();
      }

      // libraries
      if (!portObject.libs.isEmpty()) {
        out.putNextEntry(new ZipEntry("library.list"));
        List<String> libNames = portObject.libs.stream().map(f -> f.getName().split("\\_")[0])
            .collect(Collectors.toList());
        IOUtils.writeLines(libNames, "\n", out, StandardCharsets.UTF_8);
        out.closeEntry();
      }

      // Save resources
      final List<Path> resources =
          Files.list(portObject.workingDirectory).collect(Collectors.toList());
      for (final Path resource : resources) {
        final String filename = resource.getFileName().toString();

        if (FilenameUtils.isExtension(filename, RESOURCE_EXTENSIONS)) {
          out.putNextEntry(new ZipEntry(filename));
          Files.copy(resource, out);
          out.closeEntry();
        }
      }

      // Save simulations
      if (!portObject.simulations.isEmpty()) {
        out.putNextEntry(new ZipEntry(SIMULATION));

        try {
          ObjectOutputStream oos = new ObjectOutputStream(out);
          oos.writeObject(portObject.simulations);
        } catch (IOException exception) {
          // TODO: deal with exception
        }
        out.closeEntry();
      }

      out.close();
    }

    @Override
    @SuppressWarnings("unchecked")
    public FskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec,
        ExecutionMonitor exec) throws IOException, CanceledExecutionException {

      String modelScript = "";
      String parametersScript = "";
      String visualizationScript = "";

      GeneralInformation generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();
      Scope scope = MetadataFactory.eINSTANCE.createScope();
      DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();
      ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();

      Path workspacePath = FileUtil.createTempFile("workspace", ".r").toPath();
      Set<File> libs = new HashSet<>();

      Path workingDirectory = FileUtil.createTempDir("workingDirectory").toPath();

      List<FskSimulation> simulations = new ArrayList<>();

      ZipEntry entry;
      while ((entry = in.getNextEntry()) != null) {
        String entryName = entry.getName();

        if (entryName.equals(MODEL)) {
          modelScript = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(PARAM)) {
          parametersScript = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(VIZ)) {
          visualizationScript = IOUtils.toString(in, "UTF-8");
        }
        // If found old deprecated metadata, restore it and convert it to new EMF metadata
        else if (entryName.equals(META_DATA)) {

          final String metaDataAsString = IOUtils.toString(in, "UTF-8");
          ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
          GenericModel genericModel = mapper.readValue(metaDataAsString, GenericModel.class);

          generalInformation = RakipUtil.convert(genericModel.generalInformation);
          scope = RakipUtil.convert(genericModel.scope);
          dataBackground = RakipUtil.convert(genericModel.dataBackground);
          modelMath = RakipUtil.convert(genericModel.modelMath);
        }

        else if (entryName.equals(CFG_GENERAL_INFORMATION)) {
          generalInformation = readEObject(in, GeneralInformation.class);
        } else if (entryName.equals(CFG_SCOPE)) {
          scope = readEObject(in, Scope.class);
        } else if (entryName.equals(CFG_DATA_BACKGROUND)) {
          dataBackground = readEObject(in, DataBackground.class);
        } else if (entryName.equals(CFG_MODEL_MATH)) {
          modelMath = readEObject(in, ModelMath.class);
        } else if (entryName.equals(WORKSPACE)) {
          Files.copy(in, workspacePath, StandardCopyOption.REPLACE_EXISTING);
        } else if (entryName.equals("library.list")) {
          List<String> libNames = IOUtils.readLines(in, "UTF-8");

          try {
            LibRegistry libRegistry = LibRegistry.instance();
            // Install missing libraries
            List<String> missingLibs = new LinkedList<>();
            for (String lib : libNames) {
              if (!libRegistry.isInstalled(lib)) {
                missingLibs.add(lib);
              }
            }
            if (!missingLibs.isEmpty()) {
              libRegistry.installLibs(missingLibs);
            }
            // Adds to libs the Paths of the libraries converted to Files
            libRegistry.getPaths(libNames).forEach(p -> libs.add(p.toFile()));
          } catch (RException | REXPMismatchException error) {
            throw new IOException(error.getMessage());
          }
        }

        // Load resources
        else if (FilenameUtils.isExtension(entryName, RESOURCE_EXTENSIONS)) {
          // Creates path to resource. E.g.: <workingDir>/resource.txt
          Path resource = workingDirectory.resolve(entryName);
          Files.copy(in, resource);
        }

        else if (entryName.equals(SIMULATION)) {
          try {
            ObjectInputStream ois = new ObjectInputStream(in);
            simulations = ((List<FskSimulation>) ois.readObject());
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      }

      in.close();

      final FskPortObject portObj =
          new FskPortObject(modelScript, parametersScript, visualizationScript, generalInformation,
              scope, dataBackground, modelMath, workspacePath, libs, workingDirectory);

      if (!simulations.isEmpty()) {
        portObj.simulations.addAll(simulations);
      }

      return portObj;
    }

    @SuppressWarnings("unchecked")
    private <T> T readEObject(PortObjectZipInputStream zipStream, Class<T> valueType)
        throws IOException {

      String jsonStr = IOUtils.toString(zipStream, "UTF-8");

      ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
          .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JsonResourceFactory(mapper));
      resourceSet.getPackageRegistry().put(MetadataPackage.eINSTANCE.getNsURI(),
          MetadataPackage.eINSTANCE);

      Resource resource = resourceSet.createResource(URI.createURI("*.extension"));
      InputStream inStream = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));
      resource.load(inStream, null);

      return (T) resource.getContents().get(0);
    }

    /**
     * Create {@link ZipEntry} with Json string representing a metadata class.
     * 
     * @throws IOException
     */
    private static <T extends EObject> void writeEObject(String entryName, T value,
        PortObjectZipOutputStream out) throws IOException {

      out.putNextEntry(new ZipEntry(entryName));

      ObjectMapper mapper = FskPlugin.getDefault().OBJECT_MAPPER;
      String jsonStr = mapper.writeValueAsString(value);
      IOUtils.write(jsonStr, out, "UTF-8");

      out.closeEntry();
    }
  }

  @Override
  public JComponent[] getViews() {
    JPanel modelScriptPanel = new ScriptPanel("Model script", model, false);
    JPanel paramScriptPanel = new ScriptPanel("Param script", param, false);
    JPanel vizScriptPanel = new ScriptPanel("Visualization script", viz, false);

    JTree tree = MetadataTree.createTree(generalInformation, scope, dataBackground, modelMath);
    final JScrollPane metaDataPane = new JScrollPane(tree);
    metaDataPane.setName("Meta data");

    final JPanel librariesPanel = UIUtils.createLibrariesPanel(libs);
    final JPanel resourcesPanel = createResourcesViewPanel(workingDirectory);

    JPanel simulationsPanel = new SimulationsPanel();

    return new JComponent[] {modelScriptPanel, paramScriptPanel, vizScriptPanel, metaDataPane,
        librariesPanel, resourcesPanel, simulationsPanel};
  }

  /** Creates a panel with a list of resource files. */
  private static final JPanel createResourcesViewPanel(final Path workingDirectory) {

    final JPanel panel = new JPanel(new BorderLayout());
    panel.setName("Resources list");

    String[] filenames;
    try {
      filenames = Files.list(workingDirectory).map(Path::getFileName).map(Path::toString)
          .toArray(String[]::new);
    } catch (IOException e) {
      filenames = new String[0];
    }

    final JList<String> list = new JList<>(filenames);
    list.setLayoutOrientation(JList.VERTICAL);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    panel.add(new JScrollPane(list));

    return panel;
  }

  private class SimulationsPanel extends FPanel {

    private static final long serialVersionUID = -4887698302872695689L;

    private JScrollPane parametersPane;

    private final ScriptPanel scriptPanel;
    private final FPanel simulationPanel;

    public SimulationsPanel() {

      // Panel to show parameters (show initially the simulation 0)
      FskSimulation defaultSimulation = simulations.get(0);
      JPanel formPanel = createFormPane(defaultSimulation);
      parametersPane = new JScrollPane(formPanel);

      // Panel to show preview of generated script out of parameters
      String previewScript = NodeUtils.buildParameterScript(defaultSimulation);
      scriptPanel = new ScriptPanel("Preview", previewScript, false);

      simulationPanel = new FPanel();

      createUI();
    }

    private void createUI() {

      simulationPanel.setLayout(new BoxLayout(simulationPanel, BoxLayout.Y_AXIS));
      simulationPanel.add(parametersPane);
      simulationPanel.add(UIUtils.createTitledPanel(scriptPanel, "Preview script"));

      // Panel to select simulation
      String[] simulationNames =
          simulations.stream().map(FskSimulation::getName).toArray(String[]::new);
      JList<String> list = new JList<>(simulationNames);
      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      list.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {

          // Get selected simulation
          int selectedIndex = list.getSelectedIndex();
          if (selectedIndex != -1) {

            // Update parameters panel
            simulationPanel.remove(parametersPane);

            FskSimulation selectedSimulation = simulations.get(selectedIndex);
            JPanel formPanel = createFormPane(selectedSimulation);

            parametersPane = new JScrollPane(formPanel);
            simulationPanel.add(parametersPane, 0);

            revalidate();
            repaint();

            // Update previewPanel
            String previewScript = NodeUtils.buildParameterScript(selectedSimulation);
            scriptPanel.setText(previewScript);
          }
        }
      });
      list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      JScrollPane browsePanel = new JScrollPane(list);

      // Build simulations panel
      setLayout(new BorderLayout());
      setName("Simulations");
      add(browsePanel, BorderLayout.WEST);
      add(simulationPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPane(FskSimulation simulation) {

      List<FLabel> nameLabels = new ArrayList<>(simulations.size());
      List<JComponent> valueLabels = new ArrayList<>(simulations.size());
      for (Map.Entry<String, String> entry : simulation.getParameters().entrySet()) {
        nameLabels.add(new FLabel(entry.getKey()));

        FTextField field = new FTextField();
        field.setText(entry.getValue());
        valueLabels.add(field);
      }

      FPanel formPanel = UIUtils.createFormPanel(nameLabels, valueLabels);

      return formPanel;
    }
  }
}
