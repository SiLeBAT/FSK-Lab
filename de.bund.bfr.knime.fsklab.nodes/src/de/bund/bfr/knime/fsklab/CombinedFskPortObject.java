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
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.nodes.ui.UTF8Control;
import de.bund.bfr.knime.fsklab.rakip.Assay;
import de.bund.bfr.knime.fsklab.rakip.DataBackground;
import de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod;
import de.bund.bfr.knime.fsklab.rakip.GeneralInformation;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.Hazard;
import de.bund.bfr.knime.fsklab.rakip.Laboratory;
import de.bund.bfr.knime.fsklab.rakip.ModelEquation;
import de.bund.bfr.knime.fsklab.rakip.ModelMath;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.knime.fsklab.rakip.PopulationGroup;
import de.bund.bfr.knime.fsklab.rakip.Product;
import de.bund.bfr.knime.fsklab.rakip.Scope;
import de.bund.bfr.knime.fsklab.rakip.Study;
import de.bund.bfr.knime.fsklab.rakip.StudySample;
import ezvcard.VCard;
import ezvcard.property.StructuredName;

/**
 * A port object for an combined FSK model port providing two FSK Models.
 * 
 * @author Ahmad Swaid, BfR, Berlin.
 */
public class CombinedFskPortObject extends FskPortObject {
  
  final FskPortObject firstFskPortObject ;
  final FskPortObject secondFskPortObject ;
  List<JoinRelation> joinerRelation ;
 

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

  private static ResourceBundle bundle =
      ResourceBundle.getBundle("MessagesBundle", new UTF8Control());

  public CombinedFskPortObject(final String model, final String param, final String viz,
      final GenericModel genericModel, final Path workspace, final Set<File> libs,
      final Path workingDirectory, final FskPortObject firstFskPortObject,final FskPortObject secondFskPortObject) throws IOException {
    super(model, param, viz, genericModel, workspace, libs, workingDirectory);
    this.firstFskPortObject = firstFskPortObject;
    this.secondFskPortObject = secondFskPortObject;
    objectNum = numOfInstances;
    numOfInstances += 1;
  }
  public CombinedFskPortObject(final Path workingDirectory, final Set<File> libs, final FskPortObject firstFskPortObject,final FskPortObject secondFskPortObject) throws IOException {
    super( workingDirectory, libs);
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
    public void savePortObject(final CombinedFskPortObject portObject, final PortObjectZipOutputStream out,
        final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
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
        final String stringVal = objectMapper.writeValueAsString(portObject.firstFskPortObject.genericModel);
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
        List<String> libNames = portObject.firstFskPortObject.libs.stream().map(f -> f.getName().split("\\_")[0])
            .collect(Collectors.toList());
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
      //Second FSK Object
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
        final String stringVal = objectMapper.writeValueAsString(portObject.secondFskPortObject.genericModel);
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
        List<String> libNames = portObject.secondFskPortObject.libs.stream().map(f -> f.getName().split("\\_")[0])
            .collect(Collectors.toList());
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
      List <JoinRelation> joinerRelation = new ArrayList<JoinRelation>();
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
        }
        else if (entryName.equals(JOINER_RELATION)) {
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
      
      
      
      final CombinedFskPortObject portObj = new CombinedFskPortObject(FileUtil.createTempDir("combined").toPath(),new HashSet<>(),fportObj,sportObj);
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

    final JScrollPane metaDataPane1 =
        new JScrollPane(firstFskPortObject.genericModel != null ? createTree(firstFskPortObject.genericModel) : new JTree());
    metaDataPane1.setName("Meta1 data");

    final JPanel librariesPanel1 = UIUtils.createLibrariesPanel(firstFskPortObject.libs);
    final JPanel resourcesPanel1 = createResourcesViewPanel(firstFskPortObject.workingDirectory);

    JPanel simulationsPanel1 = new SimulationsPanel(firstFskPortObject,1);
    
    //
    JPanel modelScriptPanel2 = new ScriptPanel("Model2 script", secondFskPortObject.model, false);
    JPanel paramScriptPanel2 = new ScriptPanel("Param2 script", secondFskPortObject.param, false);
    JPanel vizScriptPanel2 = new ScriptPanel("Visualization2 script", secondFskPortObject.viz, false);

    final JScrollPane metaDataPane2 =
        new JScrollPane(secondFskPortObject.genericModel != null ? createTree(secondFskPortObject.genericModel) : new JTree());
    metaDataPane2.setName("Meta2 data");

    final JPanel librariesPanel2 = UIUtils.createLibrariesPanel(secondFskPortObject.libs);
    final JPanel resourcesPanel2 = createResourcesViewPanel(secondFskPortObject.workingDirectory);

    JPanel simulationsPanel2 = new SimulationsPanel(secondFskPortObject,2);
    
    

    return new JComponent[] {modelScriptPanel1, paramScriptPanel1, vizScriptPanel1, metaDataPane1,
        librariesPanel1, resourcesPanel1, simulationsPanel1,modelScriptPanel2, paramScriptPanel2, vizScriptPanel2, metaDataPane2,
        librariesPanel2, resourcesPanel2, simulationsPanel2};
  }

  // Metadata pane stuff

  /**
   * Create a tree node for a string property and add it to a passed node. If the value is
   * {@code null} or blank then no new node is added.
   * 
   * @param node Existing node where the new node is added. Cannot be {@code null}.
   * @param key Key in resource bundle for the string label of the property. Cannot be {@code null}
   *        or blank.
   * @param value Can be {@code null} or blank.
   */
  private static void add(final DefaultMutableTreeNode node, final String key, final String value) {
    if (StringUtils.isNotBlank(value)) {
      final String label = bundle.getString(key);
      node.add(new DefaultMutableTreeNode(label + ": " + value));
    }
  }

  /**
   * Create a tree node for a date property and add it to a passed node. If the value is
   * {@code null} then no new node is added. The date is formatted with the 'yyyy-MM-dd' format.
   * 
   * @param node Existing node where the new node is added. Cannot be {@code null}.
   * @param key Key in resource bundle for the string label of the property. Cannot be {@code null}
   *        or blank.
   * @param date Cannnot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final String key, final Date date) {
    final String label = bundle.getString(key);
    final String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
    node.add(new DefaultMutableTreeNode(label + ": " + dateStr));
  }

  /**
   * Create a tree node for a Double property and add it to a passed node. If the value is
   * {@code null} then no new node is added.
   * 
   * @param node Existing node where the new node is added. Cannot be {@code null}.
   * @param key Key in resource bundle for the string label of the property. Cannot be {@code null}
   *        or blank.
   * @param value double
   */

  private static void add(final DefaultMutableTreeNode node, final String key, final double value) {
    final String label = bundle.getString(key);
    node.add(new DefaultMutableTreeNode(label + ": " + value));
  }

  /**
   * Create a tree node for a {@code List<String>} property and add it to a passed node. If the
   * value is {@code null} or empty then no new node is added.
   * 
   * @param node Existing node where the new node is added. Cannot be {@code null}.
   * @param key Key in resource bundle for the string label of the property. Cannot be {@code null}
   *        or blank.
   * @param value Can be null or empty.
   */
  private static void add(final DefaultMutableTreeNode node, final String key,
      final List<String> value) {

    if (value != null && !value.isEmpty()) {
      final String label = bundle.getString(key);
      final DefaultMutableTreeNode listNode = new DefaultMutableTreeNode(label);
      value.stream().map(DefaultMutableTreeNode::new).forEach(listNode::add);
      node.add(listNode);
    }
  }

  /**
   * Creates and adds a number of tree nodes with several properties of a passed Record object. If
   * the passed record is null then no nodes are added.
   * <p>
   * The Record properties to be added are: type, date, authors, title, abstract, volume, issue and
   * website.
   * 
   * @param node Existing node where the new nodes for the properties are added. Cannot be
   *        {@code null}.
   * @param record Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final Record record) {

    final String prefix = "EditReferencePanel_";

    // isReferenceDescription is not supported

    final Type recordType = record.getType();
    if (recordType != null) {
      add(node, prefix + "typeLabel", recordType.toString());
    }

    final String date = record.getDate();
    add(node, prefix + "dateLabel", date);

    // PubMedId is not supported

    List<String> authors =
        record.getAuthors().stream().filter(Objects::nonNull).collect(Collectors.toList());

    if (authors != null) {
      add(node, prefix + "authorListLabel", authors);
    }

    final String title = record.getTitle();
    add(node, prefix + "titleLabel", title);

    final String abstr = record.getAbstr();
    add(node, prefix + "abstractLabel", abstr);

    final String secondaryTitle = record.getSecondaryTitle();
    add(node, prefix + "journalLabel", secondaryTitle);

    final String volumeNumber = record.getVolumeNumber();
    add(node, prefix + "volumeLabel", volumeNumber);

    final Integer issueNumber = record.getIssueNumber();
    if (issueNumber != null) {
      add(node, prefix + "issueLabel", issueNumber.toString());
    }

    // page not supported

    // status not supported

    final String websiteLink = record.getWebsiteLink();
    add(node, prefix + "websiteLabel", websiteLink);

    // comment not supported
  }

  /**
   * Create and add a number of tree nodes with several properties of a passed {@code VCard} object.
   * If the passed {@code VCard} object is {@code null} the no nodes are added.
   * <p>
   * The {@code VCard} properties to be added are:
   * <ul>
   * <li>Nickname as given name
   * <li>Formatted name as family name
   * <li>First e-mail as contact information
   * </ul>
   * 
   * @param node Existing node where the new nodes for the properties are added. Cannot be
   *        {@code null}.
   * @param vcard Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final VCard vcard) {
    final String prefix = "EditCreatorPanel_";

    StructuredName structuredName = vcard.getStructuredName();
    if (structuredName != null) {
      add(node, prefix + "givenNameLabel", structuredName.getGiven());
      add(node, prefix + "familyNameLabel", structuredName.getFamily());
    }

    if (!vcard.getEmails().isEmpty()) {
      final String value = vcard.getEmails().get(0).toString();
      add(node, prefix + "contactLabel", value);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Product} and adds them to a passed
   * tree node. If the passed {@code Product} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param product Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final Product product) {

    final String prefix = "EditProductPanel_";

    add(node, prefix + "envNameLabel", product.environmentName);
    add(node, prefix + "envDescriptionLabel", product.environmentDescription);
    add(node, prefix + "envUnitLabel", product.environmentUnit);

    add(node, prefix + "productionMethodLabel", product.productionMethod);
    add(node, prefix + "originCountryLabel", product.originCountry);
    add(node, prefix + "originAreaLabel", product.originArea);
    add(node, prefix + "fisheriesAreaLabel", product.fisheriesArea);
    if (product.productionDate != null) {
      add(node, prefix + "productionDateLabel", product.productionDate);
    }
    if (product.expirationDate != null) {
      add(node, prefix + "expirationDateLabel", product.expirationDate);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Hazard} and adds them to a passed tree
   * node. If the passed {@code Hazard} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param hazard Can be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final Hazard hazard) {

    final String prefix = "EditHazardPanel_";

    add(node, prefix + "hazardTypeLabel", hazard.hazardType);
    add(node, prefix + "hazardNameLabel", hazard.hazardName);
    add(node, prefix + "hazardDescriptionLabel", hazard.hazardDescription);
    add(node, prefix + "hazardUnitLabel", hazard.hazardUnit);
    add(node, prefix + "adverseEffectLabel", hazard.adverseEffect);
    add(node, prefix + "originLabel", hazard.sourceOfContamination);
    add(node, prefix + "bmdLabel", hazard.bmd);
    add(node, prefix + "maxResidueLimitLabel", hazard.mrl);
    add(node, prefix + "noObservedAdverseLabel", hazard.noael);
    add(node, prefix + "lowestObserveLabel", hazard.loael);
    add(node, prefix + "acceptableOperatorLabel", hazard.aoel);
    add(node, prefix + "acuteReferenceDoseLabel", hazard.ard);
    add(node, prefix + "acceptableDailyIntakeLabel", hazard.adi);
    add(node, prefix + "indSumLabel", hazard.hazardIndSum);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code PopulationGroup} and adds them to a
   * passed tree node. If the passed {@code PopulationGroup} is {@code null} then no nodes are
   * added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param populationGroup Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node,
      final PopulationGroup populationGroup) {

    final String prefix = "EditPopulationGroupPanel_";

    add(node, prefix + "populationNameLabel", populationGroup.populationName);
    add(node, prefix + "populationSpanLabel", populationGroup.populationSpan);
    add(node, prefix + "populationDescriptionLabel", populationGroup.populationDescription);
    add(node, prefix + "populationAgeLabel", populationGroup.populationAge);
    add(node, prefix + "populationGenderLabel", populationGroup.populationGender);
    add(node, prefix + "bmiLabel", populationGroup.bmi);
    add(node, prefix + "specialDietGroupsLabel", populationGroup.specialDietGroups);
    add(node, prefix + "patternConsumptionLabel", populationGroup.specialDietGroups);
    add(node, prefix + "regionLabel", populationGroup.region);
    add(node, prefix + "countryLabel", populationGroup.country);
    add(node, prefix + "riskAndPopulationLabel", populationGroup.populationRiskFactor);
    add(node, prefix + "seasonLabel", populationGroup.season);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code GeneralInformation} and adds them to a
   * passed tree node. If the passed {@code GeneralInformation} is {@code null} then no nodes are
   * added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param generalInformation Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node,
      final GeneralInformation generalInformation) {

    final String prefix = "GeneralInformationPanel_";

    add(node, prefix + "studyNameLabel", generalInformation.name);
    add(node, prefix + "sourceLabel", generalInformation.source);
    add(node, prefix + "identifierLabel", generalInformation.identifier);

    // Remove null values in list
    final List<VCard> creators = new ArrayList();

   /* final List<VCard> creators =
        generalInformation.creators.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (!creators.isEmpty()) {
      // Parent node that holds all the creators
      final DefaultMutableTreeNode creatorsNode = new DefaultMutableTreeNode("Creators");

      for (final VCard creator : creators) {
        final DefaultMutableTreeNode creatorNode = new DefaultMutableTreeNode("Creator");
        add(creatorNode, creator);
        creatorsNode.add(creatorNode);
      }

      node.add(creatorsNode);
    }*/

    if (generalInformation.creationDate != null) {
      add(node, prefix + "creationDateLabel", generalInformation.creationDate);
    }

    // Remove null values in list
    final List<Date> modificationDate = generalInformation.modificationDate.stream()
        .filter(Objects::nonNull).collect(Collectors.toList());
    if (!modificationDate.isEmpty()) {
      final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Modification dates");
      for (final Date date : modificationDate) {
        add(parentNode, prefix + "modificationDateLabel", date);
      }
    }

    add(node, prefix + "rightsLabel", generalInformation.rights);

    // TODO: isAvailable

    add(node, prefix + "urlLabel",
        generalInformation.url != null ? generalInformation.url.toString() : "");

    // Remove null values in list
    final List<Record> reference =
        generalInformation.reference.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (!reference.isEmpty()) {
      final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("References");
      for (final Record record : reference) {
        final DefaultMutableTreeNode refNode = new DefaultMutableTreeNode("Reference");
        add(refNode, record);
        parentNode.add(refNode);
      }
      node.add(parentNode);
    }

    add(node, prefix + "languageLabel", generalInformation.language);
    add(node, prefix + "softwareLabel", generalInformation.software);
    add(node, prefix + "languageWrittenInLabel", generalInformation.languageWrittenIn);
    add(node, prefix + "statusLabel", generalInformation.status);
    add(node, prefix + "objectiveLabel", generalInformation.objective);
    add(node, prefix + "descriptionLabel", generalInformation.description);
  }

  /**
   * Creates tree nodes for the properties of a passed Scope. If the passed Scope is {@code null}
   * then no nodes are added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param scope Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final Scope scope) {

    final String prefix = "ScopePanel_";

    {
      final String key = prefix + "productLabel";
      final String label = bundle.getString(key);

      // Create productNode
      final DefaultMutableTreeNode productNode = new DefaultMutableTreeNode(label);
      final Product product = scope.product;
      if (product != null) {
        add(productNode, product);
      }

      node.add(productNode);
    }

    {
      final String key = prefix + "hazardLabel";
      final String label = bundle.getString(key);

      // Create hazardNode
      final DefaultMutableTreeNode hazardNode = new DefaultMutableTreeNode(label);
      final Hazard hazard = scope.hazard;
      if (hazard != null) {
        add(hazardNode, hazard);
      }

      node.add(hazardNode);
    }

    {
      final DefaultMutableTreeNode pgNode = new DefaultMutableTreeNode("Population group");
      final PopulationGroup populationGroup = scope.populationGroup;
      if (populationGroup != null) {
        add(pgNode, populationGroup);
      }
      node.add(pgNode);
    }

    add(node, prefix + "commentLabel", scope.generalComment);
    add(node, prefix + "temporalInformationLabel", scope.temporalInformation);
    add(node, prefix + "regionLabel", scope.region);
    add(node, prefix + "countryLabel", scope.country);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code DataBackground} and adds them to a
   * passed tree node. If the passed {@code DataBackground} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param dataBackground Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final DataBackground dataBackground) {

    final String prefix = "DataBackgroundPanel_";

    final Study study = dataBackground.study;
    if (study != null) {
      final DefaultMutableTreeNode studyNode = new DefaultMutableTreeNode("Study");
      add(studyNode, study);
      node.add(studyNode);
    }

    final StudySample studySample = dataBackground.studySample;
    if (studySample != null) {
      final String key = prefix + "studySampleLabel";
      final String label = bundle.getString(key);

      final DefaultMutableTreeNode sampleNode = new DefaultMutableTreeNode(label);
      add(sampleNode, studySample);
      node.add(sampleNode);
    }

    final DietaryAssessmentMethod dietaryAssessmentMethod = dataBackground.dietaryAssessmentMethod;
    if (dietaryAssessmentMethod != null) {
      final String key = prefix + "dietaryAssessmentMethodLabel";
      final String label = bundle.getString(key);

      final DefaultMutableTreeNode damNode = new DefaultMutableTreeNode(label);
      add(damNode, dietaryAssessmentMethod);
      node.add(damNode);
    }

    final Laboratory lab = dataBackground.laboratory;
    if (lab != null) {
      final String key = prefix + "laboratoryLabel";
      final String label = bundle.getString(key);

      final DefaultMutableTreeNode labNode = new DefaultMutableTreeNode(label);
      add(labNode, lab);
      node.add(labNode);
    }

    final Assay assay = dataBackground.assay;
    if (assay != null) {
      final String key = prefix + "assayLabel";
      final String label = bundle.getString(key);
      final DefaultMutableTreeNode assayNode = new DefaultMutableTreeNode(label);
      add(assayNode, assay);
      node.add(assayNode);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Study} and adds them to a passed tree
   * node. If the passed {@code Study} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param study Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final Study study) {

    final String prefix = "StudyPanel_";

    add(node, prefix + "studyTitleLabel", study.title);
    add(node, prefix + "studyDescriptionLabel", study.description);
    add(node, prefix + "studyDesignTypeLabel", study.designType);
    add(node, prefix + "studyAssayMeasurementsTypeLabel", study.measurementType);
    add(node, prefix + "studyAssayTechnologyTypeLabel", study.technologyType);
    add(node, prefix + "studyAssayTechnologyPlatformLabel", study.technologyPlatform);
    add(node, prefix + "accreditationProcedureLabel", study.accreditationProcedure);
    add(node, prefix + "protocolNameLabel", study.protocolName);
    add(node, prefix + "protocolTypeLabel", study.protocolType);
    add(node, prefix + "protocolDescriptionLabel", study.protocolDescription);

    final URI protocolUri = study.protocolUri;
    if (protocolUri != null) {
      final String value = protocolUri.toString();
      add(node, prefix + "protocolURILabel", value);
    }

    add(node, prefix + "protocolVersionLabel", study.protocolVersion);
    add(node, prefix + "parametersLabel", study.parametersName);
    add(node, prefix + "componentsTypeLabel", study.componentsType);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code StudySample} and adds them to a passed
   * tree node. If the passed {@code StudySample} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param studySample Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final StudySample studySample) {

    final String prefix = "EditStudySamplePanel_";

    add(node, prefix + "sampleNameLabel", studySample.sample);
    if (studySample.collectionProtocol != null) {
      add(node, prefix + "sampleProtocolLabel", studySample.collectionProtocol);
    }
    add(node, prefix + "samplingStrategyLabel", studySample.samplingStrategy);
    add(node, prefix + "samplingTypeLabel", studySample.samplingProgramType);
    add(node, prefix + "samplingMethodLabel", studySample.samplingMethod);
    add(node, prefix + "samplingPlanLabel", studySample.samplingPlan);
    add(node, prefix + "samplingWeightLabel", studySample.samplingWeight);
    add(node, prefix + "samplingSizeLabel", studySample.samplingSize);
    add(node, prefix + "lotSizeUnitLabel", studySample.lotSizeUnit);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code DietaryAssessmentMethod} and adds them
   * to a passed tree node. If the passed {@code DietaryAssessmentMethod} is {@code null} then no
   * nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param method Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final DietaryAssessmentMethod method) {

    // Prefix in resource bundle
    final String prefix = "EditDietaryAssessmentMethodPanel_";

    add(node, prefix + "dataCollectionToolLabel", method.collectionTool);
    add(node, prefix + "nonConsecutiveOneDaysLabel",
        Integer.toString(method.numberOfNonConsecutiveOneDay));
    add(node, prefix + "dietarySoftwareToolLabel", method.softwareTool);
    add(node, prefix + "foodItemNumberLabel", method.numberOfFoodItems);
    add(node, prefix + "recordTypeLabel", method.recordTypes);
    add(node, prefix + "foodDescriptorsLabel", method.foodDescriptors);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Assay} and adds them to a passed tree
   * node. If the passed {@code Assay} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param assay Cannot be {@code null}.
   */
  private static void add(final DefaultMutableTreeNode node, final Assay assay) {
    final String prefix = "EditAssayPanel_";
    add(node, prefix + "nameLabel", assay.name);
    add(node, prefix + "descriptionLabel", assay.description);
    add(node, prefix + "moisturePercentageLabel", assay.moisturePercentage);
    add(node, prefix + "detectionLimitLabel", assay.detectionLimit);
    add(node, prefix + "quantificationLimitLabel", assay.quantificationLimit);
    add(node, prefix + "leftCensoredDataLabel", assay.leftCensoredData);
    add(node, prefix + "contaminationRangeLabel", assay.contaminationRange);
    add(node, prefix + "uncertaintyLabel", assay.uncertaintyValue);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Laboratory} and adds them to a passed
   * tree node. If the passed {@code Laboratory} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param laboratory Cannot be {@code null}.
   */
  private static void add(final DefaultMutableTreeNode node, final Laboratory laboratory) {
    final String prefix = "EditLaboratoryPanel_";
    add(node, prefix + "accreditationLabel", laboratory.accreditation);
    add(node, prefix + "nameLabel", laboratory.name);
    add(node, prefix + "countryLabel", laboratory.country);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Parameter} and adds them to a passed
   * tree node. If the passed {@code Parameter} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param parameter Cannot be {@code null}.
   */
  private static void add(final DefaultMutableTreeNode node, final Parameter parameter) {

    final String prefix = "EditParameterPanel_";

    add(node, prefix + "idLabel", parameter.id);
    add(node, prefix + "classificationLabel", parameter.classification.toString());
    add(node, prefix + "parameterNameLabel", parameter.name);
    add(node, prefix + "descriptionLabel", parameter.description);
    add(node, prefix + "unitLabel", parameter.unit);
    add(node, prefix + "unitCategoryLabel", parameter.unitCategory);
    add(node, prefix + "dataTypeLabel", parameter.dataType);
    add(node, prefix + "sourceLabel", parameter.source);
    add(node, prefix + "subjectLabel", parameter.subject);
    add(node, prefix + "distributionLabel", parameter.distribution);
    add(node, prefix + "valueLabel", parameter.value);
    add(node, prefix + "referenceLabel", parameter.reference);
    add(node, prefix + "variabilitySubjectLabel", parameter.variabilitySubject);
    add(node, prefix + "applicabilityLabel", parameter.modelApplicability);
    if (parameter.error != null) {
      add(node, prefix + "errorLabel", parameter.error.doubleValue());
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code ModelEquation} and adds them to a
   * passed tree node. If the passed {@code ModelEquation} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param modelEquation Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final ModelEquation modelEquation) {

    final String prefix = "EditModelEquationPanel_";

    add(node, prefix + "nameLabel", modelEquation.equationName);
    add(node, prefix + "classLabel", modelEquation.equationClass);

    final List<Record> equationReference = modelEquation.equationReference.stream()
        .filter(Objects::nonNull).collect(Collectors.toList());
    if (!equationReference.isEmpty()) {
      final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("References");
      for (final Record ref : equationReference) {
        final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("Reference");
        add(childNode, ref);
        parentNode.add(childNode);
      }
      node.add(parentNode);
    }

    add(node, prefix + "scriptLabel", modelEquation.equation);
  }

  /**
   * Creates tree nodes for the properties of a passed {@code ModelMath} and adds them to a passed
   * tree node. If the passed {@code ModelMath} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param modelMath Cannot be {@code null}.
   */

  private static void add(final DefaultMutableTreeNode node, final ModelMath modelMath) {

    final List<Parameter> parameter =
        modelMath.parameter.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (!parameter.isEmpty()) {
      final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Parameters");
      for (Parameter param : parameter) {
        final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("Parameter");
        add(childNode, param);
        parentNode.add(childNode);
      }
      node.add(parentNode);
    }

    add(node, "ModelMath.SSE", modelMath.sse);
    add(node, "ModelMath.MSE", modelMath.mse);
    add(node, "ModelMath.RMSE", modelMath.rmse);
    add(node, "ModelMath.R2", modelMath.rSquared);
    add(node, "ModelMath.AIC", modelMath.aic);
    add(node, "ModelMath.BIC", modelMath.bic);

    if (modelMath.modelEquation != null && !modelMath.modelEquation.isEmpty()) {
      final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Model equations");
      for (final ModelEquation equation : modelMath.modelEquation) {
        final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("Model equation");
        add(childNode, equation);
        parentNode.add(childNode);
      }
      node.add(parentNode);
    }

    // add(node, "Fitting procedure", modelMath.fittingProcedure);

    // TODO: exposure

    final List<String> event =
        modelMath.event.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (event != null && !event.isEmpty()) {
      final DefaultMutableTreeNode listNode = new DefaultMutableTreeNode("Events");
      event.stream().map(DefaultMutableTreeNode::new).forEach(listNode::add);
      node.add(listNode);
    }
  }

  private static JTree createTree(GenericModel genericModel) {

    final DefaultMutableTreeNode generalInformationNode =
        new DefaultMutableTreeNode("General information");
    if (genericModel.generalInformation != null) {
      add(generalInformationNode, genericModel.generalInformation);
    }

    final DefaultMutableTreeNode scopeNode = new DefaultMutableTreeNode("Scope");
    if (genericModel.scope != null) {
      add(scopeNode, genericModel.scope);
    }

    final DefaultMutableTreeNode dataBackgroundNode = new DefaultMutableTreeNode("Data background");
    if (genericModel.dataBackground != null) {
      add(dataBackgroundNode, genericModel.dataBackground);
    }

    final DefaultMutableTreeNode modelMathNode = new DefaultMutableTreeNode("Model math");
    if (genericModel.modelMath != null) {
      add(modelMathNode, genericModel.modelMath);
    }

    final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
    rootNode.add(generalInformationNode);
    rootNode.add(scopeNode);
    rootNode.add(dataBackgroundNode);
    rootNode.add(modelMathNode);

    final JTree tree = new JTree(rootNode);
    tree.setRootVisible(false);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    return tree;
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

    public SimulationsPanel(FskPortObject portObject,int modelsimulation) {

      // Panel to show parameters (show initially the simulation 0)
      FskSimulation defaultSimulation = portObject.simulations.get(0);
      JPanel formPanel = createFormPane(defaultSimulation);
      parametersPane = new JScrollPane(formPanel);

      // Panel to show preview of generated script out of parameters
      String previewScript = NodeUtils.buildParameterScript(defaultSimulation);
      scriptPanel = new ScriptPanel("Preview", previewScript, false);

      simulationPanel = new FPanel();

      createUI(portObject,modelsimulation);
    }

    private void createUI(FskPortObject portObject,int modelsimulation) {

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
      setName("Simulations"+modelsimulation);
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
