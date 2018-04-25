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
import java.io.File;
import java.io.IOException;
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
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
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

/**
 * A port object for an combined FSK model port providing two FSK Models.
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */
public class CombinedFskPortObject extends FskPortObject {

  final FskPortObject firstFskPortObject;
  final FskPortObject secondFskPortObject;
  List<JoinRelation> joinerRelation;

  public List<JoinRelation> getJoinerRelation() {
    return joinerRelation;
  }

  public void setJoinerRelation(List<JoinRelation> joinerRelation) {
    this.joinerRelation = joinerRelation;
  }

  public static final PortType TYPE =
      PortTypeRegistry.getInstance().getPortType(CombinedFskPortObject.class);

  public static final PortType TYPE_OPTIONAL =
      PortTypeRegistry.getInstance().getPortType(CombinedFskPortObject.class, true);

  public static final String[] RESOURCE_EXTENSIONS = new String[] {"txt", "RData", "csv"};

  private static int numOfInstances = 0;

  public CombinedFskPortObject(final String model, final String param, final String viz,
      final GenericModel genericModel, final Path workspace, final Set<File> libs,
      final Path workingDirectory, final FskPortObject firstFskPortObject,
      final FskPortObject secondFskPortObject) throws IOException {
    super(model, param, viz, genericModel, workspace, libs, workingDirectory);
    this.firstFskPortObject = firstFskPortObject;
    this.secondFskPortObject = secondFskPortObject;
    objectNum = numOfInstances;
    numOfInstances += 1;
  }

  public CombinedFskPortObject(final Path workingDirectory, final Set<File> libs,
      final FskPortObject firstFskPortObject, final FskPortObject secondFskPortObject)
      throws IOException {
    super(workingDirectory, libs);
    this.firstFskPortObject = firstFskPortObject;
    this.secondFskPortObject = secondFskPortObject;
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

    private static final String MODEL1 = "model1.R";
    private static final String PARAM1 = "param1.R";
    private static final String VIZ1 = "viz1.R";
    private static final String META_DATA1 = "metaData1";
    private static final String WORKSPACE1 = "workspace1";
    private static final String SIMULATION1 = "simulation1";

    private static final String MODEL2 = "model2.R";
    private static final String PARAM2 = "param2.R";
    private static final String VIZ2 = "viz2.R";
    private static final String META_DATA2 = "metaData2";
    private static final String WORKSPACE2 = "workspace2";
    private static final String SIMULATION2 = "simulation2";

    private static final String JOINER_RELATION = "joinerRelation";

    private static final ObjectMapper objectMapper = FskPlugin.getDefault().OBJECT_MAPPER;

    @Override
    public void savePortObject(final CombinedFskPortObject portObject,
        final PortObjectZipOutputStream out, final ExecutionMonitor exec)
        throws IOException, CanceledExecutionException {
      if (!portObject.joinerRelation.isEmpty()) {
        out.putNextEntry(new ZipEntry(JOINER_RELATION));

        try {
          ObjectOutputStream oos = new ObjectOutputStream(out);
          oos.writeObject(portObject.joinerRelation);
        } catch (IOException exception) {
          // TODO: deal with exception
        }
        out.closeEntry();
      }

      // First FSK Object
      // model entry (file with model script)
      out.putNextEntry(new ZipEntry(MODEL1));
      IOUtils.write(portObject.firstFskPortObject.model, out, "UTF-8");
      out.closeEntry();

      // param entry (file with param script)
      out.putNextEntry(new ZipEntry(PARAM1));
      IOUtils.write(portObject.firstFskPortObject.param, out, "UTF-8");
      out.closeEntry();

      // viz entry (file with visualization script)
      out.putNextEntry(new ZipEntry(VIZ1));
      IOUtils.write(portObject.firstFskPortObject.viz, out, "UTF-8");
      out.closeEntry();

      // template entry (file with model meta data)
      if (portObject.firstFskPortObject.genericModel != null) {
        out.putNextEntry(new ZipEntry(META_DATA1));
        final String stringVal =
            objectMapper.writeValueAsString(portObject.firstFskPortObject.genericModel);
        IOUtils.write(stringVal, out, "UTF-8");
        out.closeEntry();
      }

      // workspace entry
      if (portObject.firstFskPortObject.workspace != null) {
        out.putNextEntry(new ZipEntry(WORKSPACE1));
        Files.copy(portObject.firstFskPortObject.workspace, out);
        out.closeEntry();
      }

      // libraries
      if (!portObject.firstFskPortObject.libs.isEmpty()) {
        out.putNextEntry(new ZipEntry("library1.list"));
        List<String> libNames = portObject.firstFskPortObject.libs.stream()
            .map(f -> f.getName().split("\\_")[0]).collect(Collectors.toList());
        IOUtils.writeLines(libNames, "\n", out, StandardCharsets.UTF_8);
        out.closeEntry();
      }

      // Save resources
      final List<Path> resources1 =
          Files.list(portObject.firstFskPortObject.workingDirectory).collect(Collectors.toList());
      for (final Path resource : resources1) {
        final String filename = resource.getFileName().toString();

        if (FilenameUtils.isExtension(filename, RESOURCE_EXTENSIONS)) {
          out.putNextEntry(new ZipEntry(filename));
          Files.copy(resource, out);
          out.closeEntry();
        }
      }

      // Save simulations
      if (!portObject.firstFskPortObject.simulations.isEmpty()) {
        out.putNextEntry(new ZipEntry(SIMULATION1));

        try {
          ObjectOutputStream oos = new ObjectOutputStream(out);
          oos.writeObject(portObject.firstFskPortObject.simulations);
        } catch (IOException exception) {
          // TODO: deal with exception
        }
        out.closeEntry();
      }
      // Second FSK Object
      out.putNextEntry(new ZipEntry(MODEL2));
      IOUtils.write(portObject.secondFskPortObject.model, out, "UTF-8");
      out.closeEntry();

      // param entry (file with param script)
      out.putNextEntry(new ZipEntry(PARAM2));
      IOUtils.write(portObject.secondFskPortObject.param, out, "UTF-8");
      out.closeEntry();

      // viz entry (file with visualization script)
      out.putNextEntry(new ZipEntry(VIZ2));
      IOUtils.write(portObject.secondFskPortObject.viz, out, "UTF-8");
      out.closeEntry();

      // template entry (file with model meta data)
      if (portObject.secondFskPortObject.genericModel != null) {
        out.putNextEntry(new ZipEntry(META_DATA2));
        final String stringVal =
            objectMapper.writeValueAsString(portObject.secondFskPortObject.genericModel);
        IOUtils.write(stringVal, out, "UTF-8");
        out.closeEntry();
      }

      // workspace entry
      if (portObject.secondFskPortObject.workspace != null) {
        out.putNextEntry(new ZipEntry(WORKSPACE2));
        Files.copy(portObject.secondFskPortObject.workspace, out);
        out.closeEntry();
      }

      // libraries
      if (!portObject.secondFskPortObject.libs.isEmpty()) {
        out.putNextEntry(new ZipEntry("library2.list"));
        List<String> libNames = portObject.secondFskPortObject.libs.stream()
            .map(f -> f.getName().split("\\_")[0]).collect(Collectors.toList());
        IOUtils.writeLines(libNames, "\n", out, StandardCharsets.UTF_8);
        out.closeEntry();
      }

      // Save resources
      final List<Path> resources2 =
          Files.list(portObject.secondFskPortObject.workingDirectory).collect(Collectors.toList());
      for (final Path resource : resources2) {
        final String filename = resource.getFileName().toString();

        if (FilenameUtils.isExtension(filename, RESOURCE_EXTENSIONS)) {
          out.putNextEntry(new ZipEntry(filename));
          Files.copy(resource, out);
          out.closeEntry();
        }
      }

      // Save simulations
      if (!portObject.secondFskPortObject.simulations.isEmpty()) {
        out.putNextEntry(new ZipEntry(SIMULATION2));

        try {
          ObjectOutputStream oos = new ObjectOutputStream(out);
          oos.writeObject(portObject.secondFskPortObject.simulations);
        } catch (IOException exception) {
          // TODO: deal with exception
        }
        out.closeEntry();
      }

      out.close();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CombinedFskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec,
        ExecutionMonitor exec) throws IOException, CanceledExecutionException {
      List<JoinRelation> joinerRelation = new ArrayList<>();
      // First FSK Object
      String modelScript1 = "";
      String parametersScript1 = "";
      String visualizationScript1 = "";
      GenericModel genericModel1 = null;
      Path workspacePath1 = FileUtil.createTempFile("workspace", ".r").toPath();
      Set<File> libs1 = new HashSet<>();

      Path workingDirectory1 = FileUtil.createTempDir("workingDirectory").toPath();

      List<FskSimulation> simulations1 = new ArrayList<>();


      // Second FSK Object
      String modelScript2 = "";
      String parametersScript2 = "";
      String visualizationScript2 = "";
      GenericModel genericModel2 = null;
      Path workspacePath2 = FileUtil.createTempFile("workspace", ".r").toPath();
      Set<File> libs2 = new HashSet<>();

      Path workingDirectory2 = FileUtil.createTempDir("workingDirectory").toPath();

      List<FskSimulation> simulations2 = new ArrayList<>();


      ZipEntry entry;
      while ((entry = in.getNextEntry()) != null) {
        String entryName = entry.getName();
        // First FSK Object entries
        if (entryName.equals(MODEL1)) {
          modelScript1 = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(PARAM1)) {
          parametersScript1 = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(VIZ1)) {
          visualizationScript1 = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(META_DATA1)) {
          final String metaDataAsString = IOUtils.toString(in, "UTF-8");
          genericModel1 = objectMapper.readValue(metaDataAsString, GenericModel.class);
        } else if (entryName.equals(WORKSPACE1)) {
          Files.copy(in, workspacePath1, StandardCopyOption.REPLACE_EXISTING);
        } else if (entryName.equals("library1.list")) {
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
            libRegistry.getPaths(libNames).forEach(p -> libs1.add(p.toFile()));
          } catch (RException | REXPMismatchException error) {
            throw new IOException(error.getMessage());
          }
        }

        // Load resources
        else if (FilenameUtils.isExtension(entryName, RESOURCE_EXTENSIONS)) {
          // Creates path to resource. E.g.: <workingDir>/resource.txt
          Path resource = workingDirectory1.resolve(entryName);
          Files.copy(in, resource);
        }

        else if (entryName.equals(SIMULATION1)) {
          try {
            ObjectInputStream ois = new ObjectInputStream(in);
            simulations1 = ((List<FskSimulation>) ois.readObject());
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }

        // Second FSK Object entries


        if (entryName.equals(MODEL2)) {
          modelScript2 = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(PARAM2)) {
          parametersScript2 = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(VIZ2)) {
          visualizationScript2 = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(META_DATA2)) {
          final String metaDataAsString = IOUtils.toString(in, "UTF-8");
          genericModel2 = objectMapper.readValue(metaDataAsString, GenericModel.class);
        } else if (entryName.equals(WORKSPACE2)) {
          Files.copy(in, workspacePath2, StandardCopyOption.REPLACE_EXISTING);
        } else if (entryName.equals("library2.list")) {
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
            libRegistry.getPaths(libNames).forEach(p -> libs2.add(p.toFile()));
          } catch (RException | REXPMismatchException error) {
            throw new IOException(error.getMessage());
          }
        }

        // Load resources
        else if (FilenameUtils.isExtension(entryName, RESOURCE_EXTENSIONS)) {
          // Creates path to resource. E.g.: <workingDir>/resource.txt
          Path resource = workingDirectory2.resolve(entryName);
          Files.copy(in, resource);
        }

        else if (entryName.equals(SIMULATION2)) {
          try {
            ObjectInputStream ois = new ObjectInputStream(in);
            simulations2 = ((List<FskSimulation>) ois.readObject());
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        } else if (entryName.equals(JOINER_RELATION)) {
          try {
            ObjectInputStream ois = new ObjectInputStream(in);
            joinerRelation = ((List<JoinRelation>) ois.readObject());
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      }

      in.close();
      final FskPortObject fportObj = new FskPortObject(modelScript1, parametersScript1,
          visualizationScript1, genericModel1, workspacePath1, libs1, workingDirectory1);

      if (!simulations1.isEmpty()) {
        fportObj.simulations.addAll(simulations1);
      }


      final FskPortObject sportObj = new FskPortObject(modelScript2, parametersScript2,
          visualizationScript2, genericModel2, workspacePath2, libs2, workingDirectory2);

      if (!simulations2.isEmpty()) {
        sportObj.simulations.addAll(simulations2);
      }



      final CombinedFskPortObject portObj = new CombinedFskPortObject(
          FileUtil.createTempDir("combined").toPath(), new HashSet<>(), fportObj, sportObj);
      if (!joinerRelation.isEmpty()) {
        portObj.setJoinerRelation(joinerRelation);
      }

      return portObj;
    }

  }

  /** {Override} */
  @Override
  public JComponent[] getViews() {
    JPanel modelScriptPanel1 = new ScriptPanel("Model1 script", firstFskPortObject.model, false);
    JPanel paramScriptPanel1 = new ScriptPanel("Param1 script", firstFskPortObject.param, false);
    JPanel vizScriptPanel1 = new ScriptPanel("Visualization script", firstFskPortObject.viz, false);

    final JScrollPane metaDataPane1 = new JScrollPane(firstFskPortObject.genericModel != null
        ? MetadataTree.createTree(firstFskPortObject.genericModel)
        : new JTree());
    metaDataPane1.setName("Meta1 data");

    final JPanel librariesPanel1 = UIUtils.createLibrariesPanel(firstFskPortObject.libs);
    final JPanel resourcesPanel1 = createResourcesViewPanel(firstFskPortObject.workingDirectory);

    JPanel simulationsPanel1 = new SimulationsPanel(firstFskPortObject, 1);

    //
    JPanel modelScriptPanel2 = new ScriptPanel("Model2 script", secondFskPortObject.model, false);
    JPanel paramScriptPanel2 = new ScriptPanel("Param2 script", secondFskPortObject.param, false);
    JPanel vizScriptPanel2 =
        new ScriptPanel("Visualization2 script", secondFskPortObject.viz, false);

    final JScrollPane metaDataPane2 = new JScrollPane(secondFskPortObject.genericModel != null
        ? MetadataTree.createTree(secondFskPortObject.genericModel)
        : new JTree());
    metaDataPane2.setName("Meta2 data");

    final JPanel librariesPanel2 = UIUtils.createLibrariesPanel(secondFskPortObject.libs);
    final JPanel resourcesPanel2 = createResourcesViewPanel(secondFskPortObject.workingDirectory);

    JPanel simulationsPanel2 = new SimulationsPanel(secondFskPortObject, 2);

    return new JComponent[] {modelScriptPanel1, paramScriptPanel1, vizScriptPanel1, metaDataPane1,
        librariesPanel1, resourcesPanel1, simulationsPanel1, modelScriptPanel2, paramScriptPanel2,
        vizScriptPanel2, metaDataPane2, librariesPanel2, resourcesPanel2, simulationsPanel2};
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

    public SimulationsPanel(FskPortObject portObject, int modelsimulation) {

      // Panel to show parameters (show initially the simulation 0)
      FskSimulation defaultSimulation = portObject.simulations.get(0);
      JPanel formPanel = createFormPane(defaultSimulation);
      parametersPane = new JScrollPane(formPanel);

      // Panel to show preview of generated script out of parameters
      String previewScript = NodeUtils.buildParameterScript(defaultSimulation);
      scriptPanel = new ScriptPanel("Preview", previewScript, false);

      simulationPanel = new FPanel();

      createUI(portObject, modelsimulation);
    }

    private void createUI(FskPortObject portObject, int modelsimulation) {

      simulationPanel.setLayout(new BoxLayout(simulationPanel, BoxLayout.Y_AXIS));
      simulationPanel.add(parametersPane);
      simulationPanel.add(UIUtils.createTitledPanel(scriptPanel, "Preview script"));

      // Panel to select simulation
      String[] simulationNames =
          portObject.simulations.stream().map(FskSimulation::getName).toArray(String[]::new);
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

            FskSimulation selectedSimulation = portObject.simulations.get(selectedIndex);
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
      setName("Simulations" + modelsimulation);
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
