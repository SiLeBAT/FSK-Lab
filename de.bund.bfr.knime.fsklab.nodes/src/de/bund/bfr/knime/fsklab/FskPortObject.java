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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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

import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.rakip.Assay;
import de.bund.bfr.knime.fsklab.rakip.DataBackground;
import de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod;
import de.bund.bfr.knime.fsklab.rakip.GeneralInformation;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.Hazard;
import de.bund.bfr.knime.fsklab.rakip.ModelEquation;
import de.bund.bfr.knime.fsklab.rakip.ModelMath;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.knime.fsklab.rakip.PopulationGroup;
import de.bund.bfr.knime.fsklab.rakip.Product;
import de.bund.bfr.knime.fsklab.rakip.RakipModule;
import de.bund.bfr.knime.fsklab.rakip.Scope;
import de.bund.bfr.knime.fsklab.rakip.Study;
import de.bund.bfr.knime.fsklab.rakip.StudySample;
import ezvcard.VCard;

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

  private static final ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle");

  /** Model script. */
  public String model;

  /** Parameters script. */
  public String param;

  /** Visualization script. */
  public String viz;

  /** Model meta data. */
  public GenericModel genericModel;

  /** R workspace file. */
  public File workspace;

  /** R library files. */
  public final Set<File> libs;

  private static int numOfInstances = 0;

  public int objectNum;

  public FskPortObject(final String model, final String param, final String viz,
      final GenericModel genericModel, final File workspace, final Set<File> libs) {
    this.model = model;
    this.param = param;
    this.viz = viz;
    this.genericModel = genericModel;
    this.workspace = workspace;
    this.libs = libs;

    objectNum = numOfInstances;
    numOfInstances += 1;
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
    private static final String WORKSPACE = "workspace";

    private static final ObjectMapper objectMapper =
        new ObjectMapper().registerModule(new RakipModule());

    /** {@inheritDoc} */
    @Override
    public void savePortObject(final FskPortObject portObject, final PortObjectZipOutputStream out,
        final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
      // model entry (file with model script)
      out.putNextEntry(new ZipEntry(MODEL));
      IOUtils.write(portObject.model, out);
      out.closeEntry();

      // param entry (file with param script)
      out.putNextEntry(new ZipEntry(PARAM));
      IOUtils.write(portObject.param, out);
      out.closeEntry();

      // viz entry (file with visualization script)
      out.putNextEntry(new ZipEntry(VIZ));
      IOUtils.write(portObject.viz, out);
      out.closeEntry();

      // template entry (file with model meta data)
      if (portObject.genericModel != null) {
        out.putNextEntry(new ZipEntry(META_DATA));
        final String stringVal = objectMapper.writeValueAsString(portObject.genericModel);
        IOUtils.write(stringVal, out);
        out.closeEntry();
      }

      // workspace entry
      if (portObject.workspace != null) {
        out.putNextEntry(new ZipEntry(WORKSPACE));
        try (FileInputStream fis = new FileInputStream(portObject.workspace)) {
          FileUtil.copy(fis, out);
        }
        out.closeEntry();
      }

      if (!portObject.libs.isEmpty()) {
        out.putNextEntry(new ZipEntry("library.list"));
        List<String> libNames = portObject.libs.stream().map(f -> f.getName().split("\\_")[0])
            .collect(Collectors.toList());
        IOUtils.writeLines(libNames, "\n", out, "UTF-8");
        out.closeEntry();
      }

      out.close();
    }

    @Override
    public FskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec,
        ExecutionMonitor exec) throws IOException, CanceledExecutionException {

      // FskPortObject portObj = new FskPortObject();

      String modelScript = "";
      String parametersScript = "";
      String visualizationScript = "";
      GenericModel genericModel = null;
      File workspaceFile = null;
      Set<File> libs = Collections.emptySet();

      ZipEntry entry;
      while ((entry = in.getNextEntry()) != null) {
        String entryName = entry.getName();

        if (entryName.equals(MODEL)) {
          modelScript = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(PARAM)) {
          parametersScript = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(VIZ)) {
          visualizationScript = IOUtils.toString(in, "UTF-8");
        } else if (entryName.equals(META_DATA)) {
          final String metaDataAsString = IOUtils.toString(in, "UTF-8");
          genericModel = objectMapper.readValue(metaDataAsString, GenericModel.class);
        } else if (entryName.equals(WORKSPACE)) {
          workspaceFile = FileUtil.createTempFile("workspace", ".r");
          try (FileOutputStream fos = new FileOutputStream(workspaceFile)) {
            FileUtil.copy(in, fos);
          }
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
            // Adds to libs the Paths of the libraries converted to
            // Files
            libRegistry.getPaths(libNames).forEach(p -> libs.add(p.toFile()));
          } catch (RException | REXPMismatchException error) {
            throw new IOException(error.getMessage());
          }
        }
      }

      in.close();

      final FskPortObject portObj = new FskPortObject(modelScript, parametersScript,
          visualizationScript, genericModel, workspaceFile, libs);
      return portObj;
    }
  }

  /** {Override} */
  @Override
  public JComponent[] getViews() {
    JPanel modelScriptPanel = new ScriptPanel("Model script", model, false);
    JPanel paramScriptPanel = new ScriptPanel("Param script", param, false);
    JPanel vizScriptPanel = new ScriptPanel("Visualization script", viz, false);

    final JScrollPane metaDataPane =
        new JScrollPane(genericModel != null ? createTree(genericModel) : new JTree());
    metaDataPane.setName("Meta data");

    return new JComponent[] {modelScriptPanel, paramScriptPanel, vizScriptPanel, metaDataPane,
        UIUtils.createLibrariesPanel(libs)};
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
  private static void add(@NonNull final DefaultMutableTreeNode node, @NonNull final String key,
      @Nullable final String value) {
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
   * @param date Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node, @NonNull final String key,
      @Nullable final Date date) {
    if (date != null) {
      final String label = bundle.getString(key);
      final String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
      node.add(new DefaultMutableTreeNode(label + ": " + dateStr));
    }
  }

  /**
   * Create a tree node for a Double property and add it to a passed node. If the value is
   * {@code null} then no new node is added.
   * 
   * @param node Existing node where the new node is added. Cannot be {@code null}.
   * @param key Key in resource bundle for the string label of the property. Cannot be {@code null}
   *        or blank.
   * @param value Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node, @NonNull final String key,
      @Nullable final Double value) {
    if (value != null) {
      final String label = bundle.getString(key);
      node.add(new DefaultMutableTreeNode(label + ": " + value));
    }
  }

  /**
   * Create a tree node for a {@code List<String>} property and add it to a passed node. If the
   * value is {@code null} or empty then no new node is added.
   * 
   * @param node Existing node where the new node is added. Cannot be {@code null}.
   * @param key Key in resource bundle for the string label of the property. Cannot be {@code null}
   *        or blank.
   * @param value Can be {@code null} or empty.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node, @NonNull final String key,
      @Nullable final List<String> value) {

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
   * @param record Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final Record record) {

    if (record != null) {
      final String prefix = "GM.EditReferencePanel";

      // isReferenceDescription is not supported

      final Type recordType = record.getType();
      if (recordType != null) {
        add(node, prefix + "typeLabel", recordType.toString());
      }

      final String date = record.getDate();
      add(node, prefix + "dateLabel", date);

      // PubMedId is not supported

      final List<String> authors = record.getAuthors();
      add(node, prefix + "authorListLabel", authors);

      final String title = record.getTitle();
      add(node, prefix + "titleLabel", title);

      final String abstr = record.getAbstr();
      add(node, prefix + "abstractLabel", abstr);

      final String secondaryTitle = record.getSecondaryTitle();
      add(node, prefix + "journalLabel", secondaryTitle);

      final String volumeNumber = record.getVolumeNumber();
      add(node, prefix + "volumeLabel", volumeNumber);

      final Integer issueNumber = record.getIssueNumber();
      add(node, prefix + "issueLabel", issueNumber.toString());

      // page not supported

      // status not supported

      final String websiteLink = record.getWebsiteLink();
      add(node, prefix + "websiteLabel", websiteLink);

      // comment not supported
    }
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
   * @param vcard Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node, @Nullable final VCard vcard) {

    if (vcard != null) {
      final String prefix = "GM.EditCreatorPanel.";

      final ezvcard.property.Nickname nickname = vcard.getNickname();
      if (nickname != null) {
        add(node, prefix + "givenNameLabel", nickname.toString());
      }

      final ezvcard.property.FormattedName formattedName = vcard.getFormattedName();
      if (formattedName != null) {
        add(node, prefix + "familyNameLabel", formattedName.toString());
      }

      if (!vcard.getEmails().isEmpty()) {
        final String value = vcard.getEmails().get(0).toString();
        add(node, prefix + "contactLabel", value);
      }
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Product} and adds them to a passed
   * tree node. If the passed {@code Product} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param product Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final Product product) {

    if (product != null) {
      final String prefix = "GM.EditProductPanel.";

      add(node, prefix + "envNameLabel", product.environmentName);
      add(node, prefix + "envDescriptionLabel", product.environmentDescription);
      add(node, prefix + "envUnitLabel", product.environmentUnit);
      add(node, prefix + "productionMethodLabel", product.productionMethod);
      add(node, prefix + "originCountryLabel", product.originCountry);
      add(node, prefix + "originAreaLabel", product.originArea);
      add(node, prefix + "fisheriesAreaLabel", product.fisheriesArea);
      add(node, prefix + "productionDateLabel", product.productionDate);
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
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final Hazard hazard) {

    if (hazard != null) {
      final String prefix = "GM.EditHazardPanel.";

      add(node, prefix + "hazardTypeLabel", hazard.hazardType);
      add(node, prefix + "hazardNameLabel", hazard.hazardName);
      add(node, prefix + "hazardDescriptionLabel", hazard.hazardDescription);
      add(node, prefix + "hazardUnitLabel", hazard.hazardUnit);
      add(node, prefix + "adverseEffectLabel", hazard.adverseEffect);
      add(node, prefix + "originLabel", hazard.origin);
      add(node, prefix + "bmdLabel", hazard.benchmarkDose);
      add(node, prefix + "maxResidueLimitLabel", hazard.maximumResidueLimit);
      add(node, prefix + "noObservedAdverseLabel", hazard.noObservedAdverse);
      add(node, prefix + "lowestObserveLabel", hazard.lowestObservedAdverse);
      add(node, prefix + "acceptableOperatorLabel", hazard.acceptableOperator);
      add(node, prefix + "acuteReferenceDoseLabel", hazard.acuteReferenceDose);
      add(node, prefix + "acceptableDailyIntake", hazard.acceptableDailyIntake);
      add(node, prefix + "indSumLabel", hazard.hazardIndSum);
      add(node, prefix + "labNameLabel", hazard.laboratoryName);
      add(node, prefix + "labCountryLabel", hazard.laboratoryCountry);
      add(node, prefix + "detectionLimitLabel", hazard.detectionLimit);
      add(node, prefix + "quantificationLimitLabel", hazard.quantificationLimit);
      add(node, prefix + "leftCensoredDataLabel", hazard.leftCensoredData);
      add(node, prefix + "contaminationRangeLabel", hazard.rangeOfContamination);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code PopulationGroup} and adds them to a
   * passed tree node. If the passed {@code PopulationGroup} is {@code null} then no nodes are
   * added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param populationGroup Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final PopulationGroup populationGroup) {

    if (populationGroup != null) {
      final String prefix = "GM.EditPopulationGroupPanel.";

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
  }

  /**
   * Creates tree nodes for the properties of a passed {@code GeneralInformation} and adds them to a
   * passed tree node. If the passed {@code GeneralInformation} is {@code null} then no nodes are
   * added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param generalInformation Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final GeneralInformation generalInformation) {

    if (generalInformation != null) {
      final String prefix = "GM.GeneralInformationPanel.";

      add(node, prefix + "studyNameLabel", generalInformation.name);
      add(node, prefix + "sourceLabel", generalInformation.source);
      add(node, prefix + "identifierLabel", generalInformation.identifier);

      final List<VCard> creators = generalInformation.creators;
      if (!creators.isEmpty()) {
        // Parent node that holds all the creators
        final DefaultMutableTreeNode creatorsNode = new DefaultMutableTreeNode("Creators");

        for (VCard creator : creators) {
          final DefaultMutableTreeNode creatorNode = new DefaultMutableTreeNode("Creator");
          add(creatorNode, creator);
          creatorsNode.add(creatorNode);
        }

        node.add(creatorsNode);
      }

      add(node, prefix + "creationDateLabel", generalInformation.creationDate);

      final List<Date> modificationDate = generalInformation.modificationDate;
      if (!modificationDate.isEmpty()) {
        final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Modification dates");
        modificationDate.forEach(it -> add(parentNode, "Modification date", it));
      }

      add(node, prefix + "rightsLabel", generalInformation.rights);

      // TODO: isAvailable

      add(node, prefix + "urlLabel", generalInformation.url.toString());

      final List<Record> reference = generalInformation.reference;
      if (!reference.isEmpty()) {
        final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("References");
        for (Record record : reference) {
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
  }

  /**
   * Creates tree nodes for the properties of a passed Scope. If the passed Scope is {@code null}
   * then no nodes are added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param scope Cannot be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node, @Nullable final Scope scope) {

    if (scope != null) {
      final String prefix = "GM.ScopePanel.";

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
  }

  /**
   * Creates tree nodes for the properties of a passed {@code DataBackground} and adds them to a
   * passed tree node. If the passed {@code DataBackground} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param dataBackground Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final DataBackground dataBackground) {

    if (dataBackground != null) {
      final String prefix = "GM.DataBackgroundPanel.";

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

      final DietaryAssessmentMethod dietaryAssessmentMethod =
          dataBackground.dietaryAssessmentMethod;
      if (dietaryAssessmentMethod != null) {
        final String key = prefix + "dietaryAssessmentMethodLabel";
        final String label = bundle.getString(key);

        final DefaultMutableTreeNode damNode = new DefaultMutableTreeNode(label);
        add(damNode, dietaryAssessmentMethod);
        node.add(damNode);
      }

      add(node, prefix + "laboratoryAccreditationLabel", dataBackground.laboratoryAccreditation);

      final Assay assay = dataBackground.assay;
      if (assay != null) {
        final String key = prefix + "assayLabel";
        final String label = bundle.getString(key);
        final DefaultMutableTreeNode assayNode = new DefaultMutableTreeNode(label);
        add(assayNode, assay);
        node.add(assayNode);
      }
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Study} and adds them to a passed tree
   * node. If the passed {@code Study} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties nodes are added. Cannot be {@code null}.
   * @param study Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node, @Nullable final Study study) {

    if (study != null) {
      final String prefix = "GM.StudyPanel.";

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
  }

  /**
   * Creates tree nodes for the properties of a passed {@code StudySample} and adds them to a passed
   * tree node. If the passed {@code StudySample} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param studySample Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final StudySample studySample) {

    if (studySample != null) {
      final String prefix = "GM.EditStudySamplePanel.";

      add(node, prefix + "sampleNameLabel", studySample.sample);
      add(node, prefix + "moisturePercentageLabel", studySample.moisturePercentage);
      add(node, prefix + "fatPercentageLabel", studySample.fatPercentage);
      add(node, prefix + "sampleProtocolLabel", studySample.collectionProtocol);
      add(node, prefix + "samplingStrategyLabel", studySample.samplingStrategy);
      add(node, prefix + "samplingMethodLabel", studySample.samplingMethod);
      add(node, prefix + "samplingPlanLabel", studySample.samplingPlan);
      add(node, prefix + "samplingWeightLabel", studySample.samplingWeight);
      add(node, prefix + "samplingSizeLabel", studySample.samplingSize);
      add(node, prefix + "lotSizeUnitLabel", studySample.lotSizeUnit);
      add(node, prefix + "samplingPointLabel", studySample.lotSizeUnit);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code DietaryAssessmentMethod} and adds them
   * to a passed tree node. If the passed {@code DietaryAssessmentMethod} is {@code null} then no
   * nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param method Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final DietaryAssessmentMethod method) {

    if (method != null) {

      // Prefix in resource bundle
      final String prefix = "GM.EditDietaryAssessmentMethodPanel.";

      add(node, prefix + "dataCollectionToolLabel", method.collectionTool);
      add(node, prefix + "nonConsecutiveOneDaysLabel",
          Integer.toString(method.numberOfNonConsecutiveOneDay));
      add(node, prefix + "dietarySoftwareToolLabel", method.softwareTool);
      add(node, prefix + "foodItemNumberLabel", method.numberOfFoodItems);
      add(node, prefix + "recordTypeLabel", method.recordTypes);
      add(node, prefix + "foodDescriptionLabel", method.foodDescriptors);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Assay} and adds them to a passed tree
   * node. If the passed {@code Assay} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param assay Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node, @Nullable final Assay assay) {
    if (assay != null) {
      add(node, "GM.EditAssayPanel.nameLabel", assay.name);
      add(node, "GM.EditAssayPanel.descriptionLabel", assay.description);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code Parameter} and adds them to a passed
   * tree node. If the passed {@code Parameter} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param parameter Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final Parameter parameter) {

    if (parameter != null) {
      final String prefix = "GM.EditParameterPanel.";

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
      add(node, prefix + "errorLabel", parameter.error);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code ModelEquation} and adds them to a
   * passed tree node. If the passed {@code ModelEquation} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param modelEquation Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final ModelEquation modelEquation) {

    if (modelEquation != null) {
      final String prefix = "GM.EditModelEquationPanel.";

      add(node, prefix + "nameLabel", modelEquation.equationName);
      add(node, prefix + "classLabel", modelEquation.equationClass);

      final List<Record> equationReference = modelEquation.equationReference;
      if (!equationReference.isEmpty()) {
        final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("References");
        for (Record ref : equationReference) {
          final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("Reference");
          add(childNode, ref);
          parentNode.add(childNode);
        }
        node.add(parentNode);
      }

      add(node, prefix + "scriptLabel", modelEquation.equation);
    }
  }

  /**
   * Creates tree nodes for the properties of a passed {@code ModelMath} and adds them to a passed
   * tree node. If the passed {@code ModelMath} is {@code null} then no nodes are added.
   * 
   * @param node Existing node where the properties are added. Cannot be {@code null}.
   * @param modelMath Can be {@code null}.
   */
  private static void add(@NonNull final DefaultMutableTreeNode node,
      @Nullable final ModelMath modelMath) {

    if (modelMath != null) {
      final List<Parameter> parameter = modelMath.parameter;
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

      final ModelEquation modelEquation = modelMath.modelEquation;
      if (modelEquation != null) {
        final DefaultMutableTreeNode equationNode = new DefaultMutableTreeNode("Model equation");
        add(equationNode, modelEquation);
        node.add(equationNode);
      }

      add(node, "Fitting procedure", modelMath.fittingProcedure);

      // TODO: exposure

      final List<String> event = modelMath.event;
      if (event != null && !event.isEmpty()) {
        final DefaultMutableTreeNode listNode = new DefaultMutableTreeNode("Events");
        event.stream().map(DefaultMutableTreeNode::new).forEach(listNode::add);
        node.add(listNode);
      }
    }
  }

  private static JTree createTree(@NonNull GenericModel genericModel) {

    final DefaultMutableTreeNode generalInformationNode =
        new DefaultMutableTreeNode("General information");
    add(generalInformationNode, genericModel.generalInformation);

    final DefaultMutableTreeNode scopeNode = new DefaultMutableTreeNode("Scope");
    add(scopeNode, genericModel.scope);

    final DefaultMutableTreeNode dataBackgroundNode = new DefaultMutableTreeNode("Data background");
    if (genericModel.dataBackground != null) {
      add(dataBackgroundNode, genericModel.dataBackground);
    }

    final DefaultMutableTreeNode modelMathNode = new DefaultMutableTreeNode("Model math");
    if (genericModel.modelMath != null) {
      add(modelMathNode, genericModel.modelMath);
    }

    final DefaultMutableTreeNode simulationNode = new DefaultMutableTreeNode("Simulation");
    // TODO: simulation

    final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
    rootNode.add(generalInformationNode);
    rootNode.add(scopeNode);
    rootNode.add(dataBackgroundNode);
    rootNode.add(modelMathNode);
    rootNode.add(simulationNode);

    final JTree tree = new JTree(rootNode);
    tree.setRootVisible(false);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    return tree;
  }
}
