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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.commons.lang3.StringUtils;
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
import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Email;
import ezvcard.property.Gender;
import ezvcard.property.Organization;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;
import ezvcard.property.Timezone;
import metadata.Assay;
import metadata.Contact;
import metadata.DataBackground;
import metadata.DietaryAssessmentMethod;
import metadata.Exposure;
import metadata.GeneralInformation;
import metadata.Hazard;
import metadata.Laboratory;
import metadata.MetadataFactory;
import metadata.MetadataPackage;
import metadata.MetadataTree;
import metadata.ModelCategory;
import metadata.ModelEquation;
import metadata.ModelMath;
import metadata.Parameter;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.PopulationGroup;
import metadata.Product;
import metadata.PublicationType;
import metadata.Reference;
import metadata.Scope;
import metadata.SpatialInformation;
import metadata.StringObject;
import metadata.Study;
import metadata.StudySample;

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

          generalInformation = MetadataUtil.convert(genericModel.generalInformation);
          scope = MetadataUtil.convert(genericModel.scope);
          dataBackground = MetadataUtil.convert(genericModel.dataBackground);
          modelMath = MetadataUtil.convert(genericModel.modelMath);
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

  private static class MetadataUtil {

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.GeneralInformation} to an EMF
     * {@link metadata.GeneralInformation}.
     */
    private static GeneralInformation convert(
        de.bund.bfr.knime.fsklab.rakip.GeneralInformation deprecatedGeneralInformation) {

      GeneralInformation emfGeneralInformation =
          MetadataFactory.eINSTANCE.createGeneralInformation();

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.name)) {
        emfGeneralInformation.setName(deprecatedGeneralInformation.name);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.source)) {
        emfGeneralInformation.setSource(deprecatedGeneralInformation.source);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.identifier)) {
        emfGeneralInformation.setIdentifier(deprecatedGeneralInformation.identifier);
      }

      if (deprecatedGeneralInformation.creationDate != null) {
        emfGeneralInformation.setCreationDate(deprecatedGeneralInformation.creationDate);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.rights)) {
        emfGeneralInformation.setRights(deprecatedGeneralInformation.rights);
      }

      emfGeneralInformation.setAvailable(deprecatedGeneralInformation.isAvailable);

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.format)) {
        emfGeneralInformation.setFormat(deprecatedGeneralInformation.format);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.language)) {
        emfGeneralInformation.setLanguage(deprecatedGeneralInformation.language);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.software)) {
        emfGeneralInformation.setSoftware(deprecatedGeneralInformation.software);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.languageWrittenIn)) {
        emfGeneralInformation.setLanguageWrittenIn(deprecatedGeneralInformation.languageWrittenIn);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.status)) {
        emfGeneralInformation.setStatus(deprecatedGeneralInformation.status);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.objective)) {
        emfGeneralInformation.setFormat(deprecatedGeneralInformation.format);
      }

      if (StringUtils.isNotEmpty(deprecatedGeneralInformation.description)) {
        emfGeneralInformation.setDescription(deprecatedGeneralInformation.description);
      }

      // TODO: creators

      if (deprecatedGeneralInformation.modelCategory != null) {
        ModelCategory modelCategory = convert(deprecatedGeneralInformation.modelCategory);
        emfGeneralInformation.getModelCategory().add(modelCategory);
      }

      // TODO: modification date

      return emfGeneralInformation;
    }

    /**
     * Convert VCard to an EMF {@link metadata.Contact}.
     */
    private static Contact convert(VCard vcard) {

      Contact contact = MetadataFactory.eINSTANCE.createContact();

      if (!vcard.getStructuredNames().isEmpty()) {

        StructuredName structuredName = vcard.getStructuredName();

        // title
        if (!structuredName.getPrefixes().isEmpty()) {
          contact.setTitle(structuredName.getPrefixes().get(0));
        }

        // family name
        String family = structuredName.getFamily();
        if (StringUtils.isNotEmpty(family)) {
          contact.setFamilyName(family);
        }

        // given name
        String givenName = structuredName.getGiven();
        if (StringUtils.isNotEmpty(givenName)) {
          contact.setGivenName(givenName);
        }
      }

      // email
      List<Email> emails = vcard.getEmails();
      if (emails != null && !emails.isEmpty()) {
        contact.setEmail(emails.get(0).getValue());
      }

      // telephone
      List<Telephone> telephones = vcard.getTelephoneNumbers();
      if (telephones != null && !telephones.isEmpty()) {
        contact.setTelephone(telephones.get(0).getText());
      }

      if (!vcard.getAddresses().isEmpty()) {

        Address address = vcard.getAddresses().get(0);

        String streetAddress = address.getStreetAddress();
        if (streetAddress != null) {
          contact.setStreetAddress(streetAddress);
        }

        String country = address.getCountry();
        if (country != null) {
          contact.setCountry(country);
        }

        String city = address.getLocality();
        if (city != null) {
          contact.setCity(city);
        }

        String zipCode = address.getPostalCode();
        if (zipCode != null) {
          contact.setZipCode(zipCode);
        }

        String region = address.getRegion();
        if (region != null) {
          contact.setRegion(region);
        }
      }

      // timezone
      Timezone timezone = vcard.getTimezone();
      if (timezone != null) {
        contact.setTimeZone(timezone.getText());
      }

      // gender
      Gender gender = vcard.getGender();
      if (gender != null) {
        contact.setGender(gender.getGender());
      }

      // note
      if (!vcard.getNotes().isEmpty()) {
        String note = vcard.getNotes().get(0).getValue();
        contact.setNote(note);
      }

      // organization
      Organization organization = vcard.getOrganization();
      if (organization != null) {
        contact.setOrganization(organization.getValues().get(0));
      }

      return contact;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.ModelCategory} to an EMF
     * {@link metadata.ModelCategory}.
     */
    private static ModelCategory convert(
        de.bund.bfr.knime.fsklab.rakip.ModelCategory deprecatedModelCategory) {

      ModelCategory modelCategory = MetadataFactory.eINSTANCE.createModelCategory();

      if (StringUtils.isNotEmpty(deprecatedModelCategory.modelClass)) {
        modelCategory.setModelClass(deprecatedModelCategory.modelClass);
      }

      if (deprecatedModelCategory.modelSubClass != null) {
        modelCategory.getModelSubClass()
            .addAll(toStringObjectList(deprecatedModelCategory.modelSubClass));
      }

      if (StringUtils.isNotEmpty(deprecatedModelCategory.modelClassComment)) {
        modelCategory.setModelClassComment(deprecatedModelCategory.modelClassComment);
      }

      if (deprecatedModelCategory.basicProcess != null
          && deprecatedModelCategory.basicProcess.size() > 0) {
        String basicProcess = deprecatedModelCategory.basicProcess.get(0);
        modelCategory.setBasicProcess(basicProcess);
      }

      return modelCategory;
    }

    /**
     * Convert RIS reference, {@link com.gmail.gcolaianni5.jris.bean.Record} to an EMF
     * {@link metadata.Reference}.
     */
    private static Reference convert(com.gmail.gcolaianni5.jris.bean.Record record) {

      Reference reference = MetadataFactory.eINSTANCE.createReference();

      // Is reference description is not included in RIS
      reference.setIsReferenceDescription(false);

      if (record.getType() != null) {
        PublicationType publicationType = PublicationType.valueOf(record.getType().name());
        reference.setPublicationType(publicationType);
      }

      final String publicationDate = record.getPrimaryDate();
      if (StringUtils.isNotEmpty(publicationDate)) {
        try {
          String dateFormatStr = "yyyy-MM-dd";
          SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
          Date date = dateFormat.parse(publicationDate);
          reference.setPublicationDate(date);
        } catch (ParseException e) {
        }
      }

      // PMID is not included in RIS

      final String doi = record.getDoi();
      if (StringUtils.isNotEmpty(doi)) {
        reference.setDoi(doi);
      }

      List<String> firstAuthors = record.getFirstAuthors();
      if (firstAuthors != null && !firstAuthors.isEmpty()) {
        String contatenatedAuthors = String.join(";", firstAuthors);
        reference.setAuthorList(contatenatedAuthors);
      }

      final String title = record.getTitle();
      if (StringUtils.isNotEmpty(title)) {
        reference.setPublicationTitle(title);
      }

      final String abstr = record.getAbstr();
      if (StringUtils.isNotEmpty(abstr)) {
        reference.setPublicationAbstract(abstr);
      }

      final String journal = record.getSecondaryTitle();
      if (StringUtils.isNotEmpty(journal)) {
        reference.setPublicationJournal(journal);
      }

      final String volumeNumber = record.getVolumeNumber();
      if (StringUtils.isNotEmpty(volumeNumber)) {
        try {
          int volume = Integer.parseInt(volumeNumber);
          reference.setPublicationVolume(volume);
        } catch (Exception exception) {
        }
      }

      final Integer issueNumber = record.getIssueNumber();
      if (issueNumber != null) {
        reference.setPublicationIssue(issueNumber);
      }

      final String url = record.getUrl();
      if (StringUtils.isNotEmpty(url)) {
        reference.setPublicationWebsite(url);
      }

      // comment is not included in RIS

      return reference;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Scope} to an EMF
     * {@link metadata.Scope}.
     */
    private static Scope convert(de.bund.bfr.knime.fsklab.rakip.Scope deprecatedScope) {

      Scope scope = MetadataFactory.eINSTANCE.createScope();

      if (StringUtils.isNotEmpty(deprecatedScope.generalComment)) {
        scope.setGeneralComment(deprecatedScope.generalComment);
      }

      if (StringUtils.isNotEmpty(deprecatedScope.temporalInformation)) {
        scope.setTemporalInformation(deprecatedScope.temporalInformation);
      }

      if (deprecatedScope.product != null) {
        Product product = convert(deprecatedScope.product);
        scope.getProduct().add(product);
      }

      if (deprecatedScope.hazard != null) {
        Hazard hazard = convert(deprecatedScope.hazard);
        scope.getHazard().add(hazard);
      }

      if (deprecatedScope.populationGroup != null) {
        PopulationGroup populationGroup = convert(deprecatedScope.populationGroup);
        scope.setPopulationGroup(populationGroup);
      }

      SpatialInformation spatialInformation = MetadataFactory.eINSTANCE.createSpatialInformation();

      if (deprecatedScope.region != null && !deprecatedScope.region.isEmpty()) {
        spatialInformation.getRegion().addAll(toStringObjectList(deprecatedScope.region));
      }

      if (deprecatedScope.country != null && !deprecatedScope.country.isEmpty()) {
        spatialInformation.getCountry().addAll(toStringObjectList(deprecatedScope.country));
      }

      return scope;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Product} to an EMF
     * {@link metadata.Product}.
     */
    private static Product convert(de.bund.bfr.knime.fsklab.rakip.Product deprecatedProduct) {

      Product product = MetadataFactory.eINSTANCE.createProduct();

      if (StringUtils.isNotEmpty(deprecatedProduct.environmentName)) {
        product.setProductName(deprecatedProduct.environmentName);
      }

      if (StringUtils.isNotEmpty(deprecatedProduct.environmentDescription)) {
        product.setProductDescription(deprecatedProduct.environmentDescription);
      }

      if (StringUtils.isNotEmpty(deprecatedProduct.environmentUnit)) {
        product.setProductUnit(deprecatedProduct.environmentUnit);
      }

      if (StringUtils.isNotEmpty(deprecatedProduct.originCountry)) {
        product.setOriginCountry(deprecatedProduct.originCountry);
      }

      if (StringUtils.isNotEmpty(deprecatedProduct.fisheriesArea)) {
        product.setFisheriesArea(deprecatedProduct.fisheriesArea);
      }

      if (deprecatedProduct.productionDate != null) {
        product.setProductionDate(deprecatedProduct.productionDate);
      }

      if (deprecatedProduct.expirationDate != null) {
        product.setExpiryDate(deprecatedProduct.expirationDate);
      }

      if (deprecatedProduct.productionMethod != null
          && !deprecatedProduct.productionMethod.isEmpty()) {
        String productionMethod = deprecatedProduct.productionMethod.get(0);
        product.setProductionMethod(productionMethod);
      }

      if (deprecatedProduct.packaging != null && !deprecatedProduct.packaging.isEmpty()) {
        String packaging = deprecatedProduct.packaging.get(0);
        product.setPackaging(packaging);
      }

      if (deprecatedProduct.productTreatment != null
          && !deprecatedProduct.productTreatment.isEmpty()) {
        String productTreatment = deprecatedProduct.productTreatment.get(0);
        product.setProductTreatment(productTreatment);
      }

      if (StringUtils.isNotEmpty(deprecatedProduct.originArea)) {
        product.setOriginArea(deprecatedProduct.originArea);
      }

      return product;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Hazard} to an EMF
     * {@link metadata.Hazard}.
     */
    private static Hazard convert(de.bund.bfr.knime.fsklab.rakip.Hazard deprecatedHazard) {

      Hazard hazard = MetadataFactory.eINSTANCE.createHazard();

      if (StringUtils.isNotEmpty(deprecatedHazard.hazardType)) {
        hazard.setHazardType(deprecatedHazard.hazardType);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.hazardName)) {
        hazard.setHazardName(deprecatedHazard.hazardName);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.hazardDescription)) {
        hazard.setHazardDescription(deprecatedHazard.hazardDescription);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.hazardUnit)) {
        hazard.setHazardUnit(deprecatedHazard.hazardUnit);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.adverseEffect)) {
        hazard.setAdverseEffect(deprecatedHazard.adverseEffect);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.sourceOfContamination)) {
        hazard.setSourceOfContamination(deprecatedHazard.sourceOfContamination);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.bmd)) {
        hazard.setBenchmarkDose(deprecatedHazard.bmd);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.mrl)) {
        hazard.setMaximumResidueLimit(deprecatedHazard.mrl);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.noael)) {
        hazard.setNoObservedAdverseAffectLevel(deprecatedHazard.noael);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.aoel)) {
        hazard.setAcceptableOperatorExposureLevel(deprecatedHazard.aoel);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.ard)) {
        hazard.setAcuteReferenceDose(deprecatedHazard.ard);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.adi)) {
        hazard.setAcceptableDailyIntake(deprecatedHazard.adi);
      }

      if (StringUtils.isNotEmpty(deprecatedHazard.hazardIndSum)) {
        hazard.setHazardIndSum(deprecatedHazard.hazardIndSum);
      }

      return hazard;
    }

    static List<StringObject> toStringObjectList(List<String> input) {
      List<StringObject> stringObjectList = new ArrayList<>();
      for (String o : input) {
        StringObject sO = MetadataFactory.eINSTANCE.createStringObject();
        sO.setValue(o);
        stringObjectList.add(sO);
      }

      return stringObjectList;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.PopulationGroup} to an EMF
     * {@link metadata.PopulationGroup}.
     */
    private static PopulationGroup convert(
        de.bund.bfr.knime.fsklab.rakip.PopulationGroup deprecatedPopulationGroup) {

      PopulationGroup populationGroup = MetadataFactory.eINSTANCE.createPopulationGroup();

      if (StringUtils.isNotEmpty(deprecatedPopulationGroup.populationName)) {
        populationGroup.setPopulationName(deprecatedPopulationGroup.populationName);
      }

      if (StringUtils.isNotEmpty(deprecatedPopulationGroup.targetPopulation)) {
        populationGroup.setTargetPopulation(deprecatedPopulationGroup.targetPopulation);
      }

      if (deprecatedPopulationGroup.populationSpan != null
          && !deprecatedPopulationGroup.populationSpan.isEmpty()) {
        populationGroup.getPopulationSpan()
            .addAll(toStringObjectList(deprecatedPopulationGroup.populationSpan));
      }

      if (deprecatedPopulationGroup.populationDescription != null
          && !deprecatedPopulationGroup.populationDescription.isEmpty()) {
        populationGroup.getPopulationDescription()
            .addAll(toStringObjectList(deprecatedPopulationGroup.populationSpan));
      }

      if (deprecatedPopulationGroup.populationAge != null
          && !deprecatedPopulationGroup.populationAge.isEmpty()) {
        populationGroup.getPopulationAge()
            .addAll(toStringObjectList(deprecatedPopulationGroup.populationAge));
      }

      if (StringUtils.isNotEmpty(deprecatedPopulationGroup.populationGender)) {
        populationGroup.setPopulationGender(deprecatedPopulationGroup.populationGender);
      }

      if (deprecatedPopulationGroup.bmi != null && !deprecatedPopulationGroup.bmi.isEmpty()) {
        populationGroup.getBmi().addAll(toStringObjectList(deprecatedPopulationGroup.bmi));
      }

      if (deprecatedPopulationGroup.specialDietGroups != null
          && !deprecatedPopulationGroup.specialDietGroups.isEmpty()) {
        populationGroup.getSpecialDietGroups().addAll(populationGroup.getSpecialDietGroups());
      }

      if (deprecatedPopulationGroup.patternConsumption != null
          && !deprecatedPopulationGroup.patternConsumption.isEmpty()) {
        populationGroup.getPatternConsumption().addAll(populationGroup.getPatternConsumption());
      }

      if (deprecatedPopulationGroup.region != null && !deprecatedPopulationGroup.region.isEmpty()) {
        populationGroup.getRegion().addAll(toStringObjectList(deprecatedPopulationGroup.region));
      }

      if (deprecatedPopulationGroup.country != null
          && !deprecatedPopulationGroup.country.isEmpty()) {
        populationGroup.getCountry().addAll(toStringObjectList(deprecatedPopulationGroup.country));
      }

      if (deprecatedPopulationGroup.populationRiskFactor != null
          && !deprecatedPopulationGroup.populationRiskFactor.isEmpty()) {
        populationGroup.getPopulationRiskFactor()
            .addAll(toStringObjectList(deprecatedPopulationGroup.populationRiskFactor));
      }

      if (deprecatedPopulationGroup.season != null && !deprecatedPopulationGroup.season.isEmpty()) {
        populationGroup.getSeason().addAll(toStringObjectList(deprecatedPopulationGroup.season));
      }

      return populationGroup;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Study} to an EMF
     * {@link metadata.Study}.
     */
    private static Study convert(de.bund.bfr.knime.fsklab.rakip.Study deprecatedStudy) {

      Study study = MetadataFactory.eINSTANCE.createStudy();

      if (StringUtils.isNotEmpty(deprecatedStudy.id)) {
        study.setStudyIdentifier(deprecatedStudy.id);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.title)) {
        study.setStudyTitle(deprecatedStudy.title);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.description)) {
        study.setStudyDescription(deprecatedStudy.description);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.designType)) {
        study.setStudyDesignType(deprecatedStudy.designType);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.measurementType)) {
        study.setStudyAssayMeasurementType(deprecatedStudy.measurementType);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.technologyType)) {
        study.setStudyAssayTechnologyType(deprecatedStudy.technologyType);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.technologyPlatform)) {
        study.setStudyAssayTechnologyPlatform(deprecatedStudy.technologyPlatform);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.accreditationProcedure)) {
        study
            .setAccreditationProcedureForTheAssayTechnology(deprecatedStudy.accreditationProcedure);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.protocolName)) {
        study.setStudyProtocolName(deprecatedStudy.protocolName);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.protocolType)) {
        study.setStudyProtocolType(deprecatedStudy.protocolType);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.protocolDescription)) {
        study.setStudyProtocolDescription(deprecatedStudy.protocolDescription);
      }

      if (deprecatedStudy.protocolUri != null) {
        study.setStudyProtocolURI(deprecatedStudy.protocolUri);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.protocolVersion)) {
        study.setStudyProtocolVersion(deprecatedStudy.protocolVersion);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.parametersName)) {
        study.setStudyProtocolParametersName(deprecatedStudy.parametersName);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.componentsName)) {
        study.setStudyProtocolComponentsName(deprecatedStudy.componentsName);
      }

      if (StringUtils.isNotEmpty(deprecatedStudy.componentsType)) {
        study.setStudyProtocolComponentsType(deprecatedStudy.componentsType);
      }

      return study;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.StudySample} to an EMF
     * {@link metadata.StudySample}.
     */
    private static StudySample convert(
        de.bund.bfr.knime.fsklab.rakip.StudySample deprecatedStudySample) {

      StudySample studySample = MetadataFactory.eINSTANCE.createStudySample();

      if (StringUtils.isNotEmpty(deprecatedStudySample.sample)) {
        studySample.setSampleName(deprecatedStudySample.sample);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.collectionProtocol)) {
        studySample.setProtocolOfSampleCollection(deprecatedStudySample.collectionProtocol);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.samplingStrategy)) {
        studySample.setSamplingStrategy(deprecatedStudySample.samplingStrategy);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.samplingProgramType)) {
        studySample.setTypeOfSamplingProgram(deprecatedStudySample.samplingProgramType);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.samplingMethod)) {
        studySample.setSamplingMethod(deprecatedStudySample.samplingMethod);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.samplingPlan)) {
        studySample.setSamplingPlan(deprecatedStudySample.samplingPlan);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.samplingWeight)) {
        studySample.setSamplingWeight(deprecatedStudySample.samplingWeight);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.samplingSize)) {
        studySample.setSamplingSize(deprecatedStudySample.samplingSize);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.lotSizeUnit)) {
        studySample.setLotSizeUnit(deprecatedStudySample.lotSizeUnit);
      }

      if (StringUtils.isNotEmpty(deprecatedStudySample.samplingPoint)) {
        studySample.setSamplingPoint(deprecatedStudySample.samplingPoint);
      }

      return studySample;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod} to an
     * EMF {@link metadata.DietaryAssessmentMethod}.
     */
    private static DietaryAssessmentMethod convert(
        de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod deprecatedDietaryAssessmentMethod) {

      DietaryAssessmentMethod dietaryAssessmentMethod =
          MetadataFactory.eINSTANCE.createDietaryAssessmentMethod();

      if (StringUtils.isNotEmpty(deprecatedDietaryAssessmentMethod.collectionTool)) {
        dietaryAssessmentMethod.setCollectionTool(deprecatedDietaryAssessmentMethod.collectionTool);
      }

      dietaryAssessmentMethod.setNumberOfNonConsecutiveOneDay(
          deprecatedDietaryAssessmentMethod.numberOfNonConsecutiveOneDay);

      if (StringUtils.isNotEmpty(deprecatedDietaryAssessmentMethod.softwareTool)) {
        dietaryAssessmentMethod.setSoftwareTool(deprecatedDietaryAssessmentMethod.softwareTool);
      }

      if (deprecatedDietaryAssessmentMethod.numberOfFoodItems != null
          && !deprecatedDietaryAssessmentMethod.numberOfFoodItems.isEmpty()) {
        String numberOfFoodItems = deprecatedDietaryAssessmentMethod.numberOfFoodItems.get(0);
        dietaryAssessmentMethod.setNumberOfFoodItems(numberOfFoodItems);
      }

      if (deprecatedDietaryAssessmentMethod.recordTypes != null
          && !deprecatedDietaryAssessmentMethod.recordTypes.isEmpty()) {
        String recordType = deprecatedDietaryAssessmentMethod.recordTypes.get(0);
        dietaryAssessmentMethod.setRecordTypes(recordType);
      }

      if (deprecatedDietaryAssessmentMethod.foodDescriptors != null
          && !deprecatedDietaryAssessmentMethod.foodDescriptors.isEmpty()) {
        String foodDescriptor = deprecatedDietaryAssessmentMethod.foodDescriptors.get(0);
        dietaryAssessmentMethod.setFoodDescriptors(foodDescriptor);
      }

      return dietaryAssessmentMethod;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Laboratory} to an EMF
     * {@link metadata.Laboratory}.
     */
    private static Laboratory convert(
        de.bund.bfr.knime.fsklab.rakip.Laboratory deprecatedLaboratory) {

      Laboratory laboratory = MetadataFactory.eINSTANCE.createLaboratory();

      if (StringUtils.isNotEmpty(deprecatedLaboratory.accreditation)) {
        StringObject sO = MetadataFactory.eINSTANCE.createStringObject();
        sO.setValue(deprecatedLaboratory.accreditation);
        laboratory.getLaboratoryAccreditation().add(sO);
      }

      if (StringUtils.isNotEmpty(deprecatedLaboratory.name)) {
        laboratory.setLaboratoryName(deprecatedLaboratory.name);
      }

      if (StringUtils.isNotEmpty(deprecatedLaboratory.country)) {
        laboratory.setLaboratoryCountry(deprecatedLaboratory.country);
      }

      return laboratory;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Assay} to an EMF
     * {@link metadata.Assay}.
     */
    private static Assay convert(de.bund.bfr.knime.fsklab.rakip.Assay deprecatedAssay) {

      Assay assay = MetadataFactory.eINSTANCE.createAssay();

      if (StringUtils.isNotEmpty(deprecatedAssay.name)) {
        assay.setAssayName(deprecatedAssay.name);
      }

      if (StringUtils.isNotEmpty(deprecatedAssay.description)) {
        assay.setAssayDescription(deprecatedAssay.description);
      }

      if (StringUtils.isNotEmpty(deprecatedAssay.moisturePercentage)) {
        assay.setPercentageOfMoisture(deprecatedAssay.moisturePercentage);
      }

      if (StringUtils.isNotEmpty(deprecatedAssay.fatPercentage)) {
        assay.setPercentageOfFat(deprecatedAssay.fatPercentage);
      }

      if (StringUtils.isNotEmpty(deprecatedAssay.detectionLimit)) {
        assay.setLimitOfDetection(deprecatedAssay.detectionLimit);
      }

      if (StringUtils.isNotEmpty(deprecatedAssay.quantificationLimit)) {
        assay.setLimitOfQuantification(deprecatedAssay.quantificationLimit);
      }

      if (StringUtils.isNotEmpty(deprecatedAssay.leftCensoredData)) {
        assay.setLeftCensoredData(deprecatedAssay.leftCensoredData);
      }

      if (StringUtils.isNotEmpty(deprecatedAssay.contaminationRange)) {
        assay.setRangeOfContamination(deprecatedAssay.contaminationRange);
      }

      if (StringUtils.isNotBlank(deprecatedAssay.uncertaintyValue)) {
        assay.setUncertaintyValue(deprecatedAssay.uncertaintyValue);
      }

      return assay;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.DataBackground} to an EMF
     * {@link metadata.DataBackground}.
     */
    private static DataBackground convert(
        de.bund.bfr.knime.fsklab.rakip.DataBackground deprecatedDataBackground) {

      DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();

      if (deprecatedDataBackground.study != null) {
        Study study = convert(deprecatedDataBackground.study);
        dataBackground.setStudy(study);
      }

      if (deprecatedDataBackground.studySample != null) {
        StudySample studySample = convert(deprecatedDataBackground.studySample);
        dataBackground.getStudysample().add(studySample);
      }

      if (deprecatedDataBackground.dietaryAssessmentMethod != null) {
        DietaryAssessmentMethod dietaryAssessmentMethod =
            convert(deprecatedDataBackground.dietaryAssessmentMethod);
        dataBackground.setDietaryassessmentmethod(dietaryAssessmentMethod);
      }

      if (deprecatedDataBackground.laboratory != null) {
        Laboratory laboratory = convert(deprecatedDataBackground.laboratory);
        dataBackground.setLaboratory(laboratory);
      }

      if (deprecatedDataBackground.assay != null) {
        Assay assay = convert(deprecatedDataBackground.assay);
        dataBackground.getAssay().add(assay);
      }

      return dataBackground;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.ModelMath} to an EMF
     * {@link metadata.ModelMath}.
     */
    private static ModelMath convert(de.bund.bfr.knime.fsklab.rakip.ModelMath deprecatedModelMath) {

      ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();

      // TODO: quality measures

      if (StringUtils.isNotEmpty(deprecatedModelMath.fittingProcedure)) {
        modelMath.setFittingProcedure(deprecatedModelMath.fittingProcedure);
      }

      if (deprecatedModelMath.event != null && !deprecatedModelMath.event.isEmpty()) {
        modelMath.getEvent().addAll(toStringObjectList(deprecatedModelMath.event));
      }

      if (deprecatedModelMath.parameter != null) {
        List<Parameter> parameters = deprecatedModelMath.parameter.stream()
            .map(MetadataUtil::convert).collect(Collectors.toList());
        modelMath.getParameter().addAll(parameters);
      }

      if (deprecatedModelMath.modelEquation != null) {
        List<ModelEquation> modelEquations = deprecatedModelMath.modelEquation.stream()
            .map(MetadataUtil::convert).collect(Collectors.toList());
        modelMath.getModelEquation().addAll(modelEquations);
      }

      if (deprecatedModelMath.exposure != null) {
        Exposure exposure = convert(deprecatedModelMath.exposure);
        modelMath.setExposure(exposure);
      }

      return modelMath;
    }

    /**
     * Convert deprecated RAKIP {@link de.bund.bfr.knime.fsklab.rakip.Parameter} to an EMF
     * {@link metadata.Parameter}.
     */
    private static Parameter convert(de.bund.bfr.knime.fsklab.rakip.Parameter deprecatedParameter) {

      Parameter parameter = MetadataFactory.eINSTANCE.createParameter();

      if (StringUtils.isNotEmpty(deprecatedParameter.id)) {
        parameter.setParameterID(deprecatedParameter.id);
      }

      if (deprecatedParameter.classification != null) {

        ParameterClassification parameterClassification;

        switch (deprecatedParameter.classification) {
          case input:
            parameterClassification = ParameterClassification.INPUT;
            break;
          case output:
            parameterClassification = ParameterClassification.OUTPUT;
            break;
          default:
            parameterClassification = ParameterClassification.CONSTANT;
        }

        parameter.setParameterClassification(parameterClassification);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.name)) {
        parameter.setParameterName(deprecatedParameter.name);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.description)) {
        parameter.setParameterDescription(deprecatedParameter.description);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.type)) {
        parameter.setParameterType(deprecatedParameter.type);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.unit)) {
        parameter.setParameterUnit(deprecatedParameter.unit);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.unitCategory)) {
        parameter.setParameterUnitCategory(deprecatedParameter.unitCategory);
      }

      if (deprecatedParameter.dataType != null) {

        ParameterType parameterType;

        switch (deprecatedParameter.dataType) {
          case Integer:
            parameterType = ParameterType.INTEGER;
            break;
          case Double:
            parameterType = ParameterType.DOUBLE;
            break;
          case Number:
            parameterType = ParameterType.NUMBER;
            break;
          case Date:
            parameterType = ParameterType.DATE;
            break;
          case File:
            parameterType = ParameterType.FILE;
            break;
          case Boolean:
            parameterType = ParameterType.BOOLEAN;
            break;
          case VectorOfNumbers:
            parameterType = ParameterType.VECTOR_OF_NUMBERS;
            break;
          case VectorOfStrings:
            parameterType = ParameterType.VECTOR_OF_STRINGS;
            break;
          case MatrixOfNumbers:
            parameterType = ParameterType.MATRIX_OF_NUMBERS;
            break;
          case MatrixOfStrings:
            parameterType = ParameterType.MATRIX_OF_STRINGS;
            break;
          case Object:
            parameterType = ParameterType.OBJECT;
            break;
          default:
            parameterType = ParameterType.OTHER;
        }

        parameter.setParameterDataType(parameterType);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.source)) {
        parameter.setParameterSource(deprecatedParameter.source);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.subject)) {
        parameter.setParameterSubject(deprecatedParameter.subject);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.distribution)) {
        parameter.setParameterDistribution(deprecatedParameter.distribution);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.value)) {
        parameter.setParameterValue(deprecatedParameter.value);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.variabilitySubject)) {
        parameter.setParameterVariabilitySubject(deprecatedParameter.variabilitySubject);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.minValue)) {
        parameter.setParameterValueMin(deprecatedParameter.minValue);
      }

      if (StringUtils.isNotEmpty(deprecatedParameter.maxValue)) {
        parameter.setParameterValueMax(deprecatedParameter.maxValue);
      }

      if (deprecatedParameter.error != null) {
        parameter.setParameterError(deprecatedParameter.error.toString());
      }

      return parameter;
    }

    private static ModelEquation convert(
        de.bund.bfr.knime.fsklab.rakip.ModelEquation deprecatedModelEquation) {

      ModelEquation modelEquation = MetadataFactory.eINSTANCE.createModelEquation();

      if (StringUtils.isNotEmpty(deprecatedModelEquation.equationName)) {
        modelEquation.setModelEquationName(deprecatedModelEquation.equationName);
      }

      if (StringUtils.isNotEmpty(deprecatedModelEquation.equationClass)) {
        modelEquation.setModelEquationClass(deprecatedModelEquation.equationClass);
      }

      if (StringUtils.isNotEmpty(deprecatedModelEquation.equation)) {
        modelEquation.setModelEquation(deprecatedModelEquation.equation);
      }

      // hypothesis of the model is not included in deprecatedModelEquation

      // reference
      if (deprecatedModelEquation.equationReference != null) {
        List<Reference> references = modelEquation.getReference();
        deprecatedModelEquation.equationReference.stream().map(MetadataUtil::convert)
            .forEach(references::add);
      }

      return modelEquation;
    }

    private static Exposure convert(de.bund.bfr.knime.fsklab.rakip.Exposure deprecatedExposure) {

      Exposure exposure = MetadataFactory.eINSTANCE.createExposure();

      if (StringUtils.isNotEmpty(deprecatedExposure.treatment)) {
        StringObject stringObject = MetadataFactory.eINSTANCE.createStringObject();
        stringObject.setValue(deprecatedExposure.treatment);
        exposure.getMethodologicalTreatmentOfLeftCensoredData().add(stringObject);
      }

      if (StringUtils.isNotEmpty(deprecatedExposure.contaminationLevel)) {
        StringObject stringObject = MetadataFactory.eINSTANCE.createStringObject();
        stringObject.setValue(deprecatedExposure.contaminationLevel);
        exposure.getLevelOfContaminationAfterLeftCensoredDataTreatment().add(stringObject);
      }

      if (StringUtils.isNotEmpty(deprecatedExposure.exposureType)) {
        exposure.setTypeOfExposure(deprecatedExposure.exposureType);
      }

      if (StringUtils.isNotEmpty(deprecatedExposure.scenario)) {
        StringObject stringObject = MetadataFactory.eINSTANCE.createStringObject();
        stringObject.setValue(deprecatedExposure.scenario);
        exposure.getScenario().add(stringObject);
      }

      if (StringUtils.isNotEmpty(deprecatedExposure.uncertaintyEstimation)) {
        exposure.setUncertaintyEstimation(deprecatedExposure.uncertaintyEstimation);
      }

      return exposure;
    }
  }
}
