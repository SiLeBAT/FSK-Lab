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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

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

import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
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

    final JScrollPane metaDataPane = new JScrollPane(createTree(genericModel));
    metaDataPane.setName("Meta data");

    return new JComponent[] {modelScriptPanel, paramScriptPanel, vizScriptPanel, metaDataPane,
        new LibrariesPanel()};
  }

  /** JPanel with list of R libraries. */
  private class LibrariesPanel extends JPanel {

    private static final long serialVersionUID = -5084804515050256443L;

    LibrariesPanel() {
      super(new BorderLayout());
      setName("Libraries list");

      String[] libNames = new String[libs.size()];
      int i = 0;
      for (File lib : libs) {
        libNames[i] = lib.getName();
        i++;
      }

      JList<String> list = new JList<>(libNames);
      list.setLayoutOrientation(JList.VERTICAL);
      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      add(new JScrollPane(list));
    }
  }

  // Metadata pane stuff
  private static void add(final DefaultMutableTreeNode node, final String label,
      final String value) {
    if (StringUtils.isNotBlank(value)) {
      node.add(new DefaultMutableTreeNode(label + ": " + value));
    }
  }

  private static void add(final DefaultMutableTreeNode node, final String label, final Date date) {
    node.add(
        new DefaultMutableTreeNode(label + ": " + new SimpleDateFormat("yyyy-MM-dd").format(date)));
  }

  private static void add(final DefaultMutableTreeNode node, final String label,
      final List<String> value) {
    final DefaultMutableTreeNode listNode = new DefaultMutableTreeNode(label);
    value.stream().map(DefaultMutableTreeNode::new).forEach(listNode::add);
    node.add(listNode);
  }

  private static void add(final DefaultMutableTreeNode node, final Record record) {

    final String prefix = "GM.EditReferencePanel";

    // isReferenceDescription is not supported

    final Type recordType = record.getType();
    if (recordType != null) {
      final String key = prefix + "typeLabel";
      final String label = bundle.getString(key);
      final String value = recordType.toString();
      add(node, label, value);
    }

    final String date = record.getDate();
    if (StringUtils.isNotBlank(date)) {
      final String key = prefix + "dateLabel";
      final String label = bundle.getString(key);
      add(node, label, date);
    }

    // PubMedId is not supported

    final List<String> authors = record.getAuthors();
    if (authors != null && !authors.isEmpty()) {
      final String key = prefix + "authorListLabel";
      final String label = bundle.getString(key);
      add(node, label, authors);
    }

    final String title = record.getTitle();
    if (StringUtils.isNotBlank(title)) {
      final String key = prefix + "titleLabel";
      final String label = bundle.getString(key);
      add(node, label, title);
    }

    final String abstr = record.getAbstr();
    if (StringUtils.isNotBlank(abstr)) {
      final String key = prefix + "abstractLabel";
      final String label = bundle.getString(key);
      add(node, label, abstr);
    }

    final String secondaryTitle = record.getSecondaryTitle();
    if (StringUtils.isNotBlank(secondaryTitle)) {
      final String key = prefix + "journalLabel";
      final String label = bundle.getString(key);
      add(node, label, secondaryTitle);
    }

    final String volumeNumber = record.getVolumeNumber();
    if (StringUtils.isNotBlank(volumeNumber)) {
      final String key = prefix + "volumeLabel";
      final String label = bundle.getString(key);
      add(node, label, volumeNumber);
    }

    final Integer issueNumber = record.getIssueNumber();
    {
      final String key = prefix + "issueLabel";
      final String label = bundle.getString(key);
      final String value = issueNumber.toString();
      add(node, label, value);
    }

    // page not supported

    // status not supported

    final String websiteLink = record.getWebsiteLink();
    if (StringUtils.isNotBlank(websiteLink)) {
      final String key = prefix + "websiteLabel";
      final String label = bundle.getString(key);
      add(node, label, websiteLink);
    }

    // comment not supported
  }

  private static void add(DefaultMutableTreeNode node, final VCard vcard) {

    final String prefix = "GM.EditCreatorPanel.";

    final ezvcard.property.Nickname nickname = vcard.getNickname();
    if (nickname != null) {
      final String key = prefix + "givenNameLabel";
      final String label = bundle.getString(key);
      final String value = nickname.toString();
      add(node, label, value);
    }

    final ezvcard.property.FormattedName formattedName = vcard.getFormattedName();
    if (formattedName != null) {
      final String key = prefix + "familyNameLabel";
      final String label = bundle.getString(key);
      final String value = formattedName.toString();
      add(node, label, value);
    }

    if (!vcard.getEmails().isEmpty()) {
      final String key = prefix + "contactLabel";
      final String label = bundle.getString(key);
      final String value = vcard.getEmails().get(0).toString();
      add(node, label, value);
    }
  }

  private static void add(DefaultMutableTreeNode node, final Product product) {

    final String prefix = "GM.EditProductPanel.";

    final String environmentName = product.environmentName;
    if (StringUtils.isNotBlank(environmentName)) {
      final String key = prefix + "envNameLabel";
      final String label = bundle.getString(key);
      add(node, label, environmentName);
    }

    final String environmentDescription = product.environmentDescription;
    if (StringUtils.isNotBlank(environmentDescription)) {
      final String key = prefix + "envDescriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, environmentDescription);
    }

    final String environmentUnit = product.environmentUnit;
    if (StringUtils.isNotBlank(environmentUnit)) {
      final String key = prefix + "envUnitLabel";
      final String label = bundle.getString(key);
      add(node, label, environmentUnit);
    }

    final List<String> productionMethod = product.productionMethod;
    if (!productionMethod.isEmpty()) {
      final String key = prefix + "productionMethodLabel";
      final String label = bundle.getString(key);
      add(node, label, productionMethod);
    }

    final String originCountry = product.originCountry;
    if (StringUtils.isNotBlank(originCountry)) {
      final String key = prefix + "originCountryLabel";
      final String label = bundle.getString(key);
      add(node, label, product.originCountry);
    }

    final String originArea = product.originArea;
    if (StringUtils.isNotBlank(originArea)) {
      final String key = prefix + "originAreaLabel";
      final String label = bundle.getString(key);
      add(node, label, originArea);
    }

    final String fisheriesArea = product.fisheriesArea;
    if (StringUtils.isNoneBlank(fisheriesArea)) {
      final String key = prefix + "fisheriesAreaLabel";
      final String label = bundle.getString(key);
      add(node, label, fisheriesArea);
    }

    final Date productionDate = product.productionDate;
    if (productionDate != null) {
      final String key = prefix + "productionDateLabel";
      final String label = bundle.getString(key);
      add(node, label, productionDate);
    }

    final Date expirationDate = product.expirationDate;
    if (expirationDate != null) {
      final String key = prefix + "expirationDateLabel";
      final String label = bundle.getString(key);
      add(node, label, expirationDate);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final Hazard hazard) {

    final String prefix = "GM.EditHazardPanel.";

    final String hazardType = hazard.hazardType;
    if (StringUtils.isNotBlank(hazardType)) {
      final String key = prefix + "hazardTypeLabel";
      final String label = bundle.getString(key);
      add(node, label, hazardType);
    }

    final String hazardName = hazard.hazardName;
    if (StringUtils.isNotBlank(hazardName)) {
      final String key = prefix + "hazardNameLabel";
      final String label = bundle.getString(key);
      add(node, label, hazardName);
    }

    final String hazardDescription = hazard.hazardDescription;
    if (StringUtils.isNotBlank(hazardDescription)) {
      final String key = prefix + "hazardDescriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, hazardDescription);
    }

    final String hazardUnit = hazard.hazardUnit;
    if (StringUtils.isNotBlank(hazardUnit)) {
      final String key = prefix + "hazardUnitLabel";
      final String label = bundle.getString(key);
      add(node, label, hazardUnit);
    }

    final String adverseEffect = hazard.adverseEffect;
    if (StringUtils.isNotBlank(adverseEffect)) {
      final String key = prefix + "adverseEffectLabel";
      final String label = bundle.getString(key);
      add(node, label, adverseEffect);
    }

    final String origin = hazard.origin;
    if (StringUtils.isNotBlank(origin)) {
      final String key = prefix + "originLabel";
      final String label = bundle.getString(key);
      add(node, label, origin);
    }

    final String benchmarkDose = hazard.benchmarkDose;
    if (StringUtils.isNotBlank(benchmarkDose)) {
      final String key = prefix + "bmdLabel";
      final String label = bundle.getString(key);
      add(node, label, benchmarkDose);
    }

    final String maximumResidueLimit = hazard.maximumResidueLimit;
    if (StringUtils.isNotBlank(maximumResidueLimit)) {
      final String key = prefix + "maxResidueLimitLabel";
      final String label = bundle.getString(key);
      add(node, label, maximumResidueLimit);
    }

    final String noObservedAdverse = hazard.noObservedAdverse;
    if (StringUtils.isNotBlank(noObservedAdverse)) {
      final String key = prefix + "noObservedAdverseLabel";
      final String label = bundle.getString(key);
      add(node, label, noObservedAdverse);
    }

    final String lowestObservedAdverse = hazard.lowestObservedAdverse;
    if (StringUtils.isNotBlank(lowestObservedAdverse)) {
      final String key = prefix + "lowestObserveLabel";
      final String label = bundle.getString(key);
      add(node, label, lowestObservedAdverse);
    }

    final String acceptableOperator = hazard.acceptableOperator;
    if (StringUtils.isNotBlank(acceptableOperator)) {
      final String key = prefix + "acceptableOperatorLabel";
      final String label = bundle.getString(key);
      add(node, label, acceptableOperator);
    }

    final String acuteReferenceDose = hazard.acuteReferenceDose;
    if (StringUtils.isNotBlank(acuteReferenceDose)) {
      final String key = prefix + "acuteReferenceDoseLabel";
      final String label = bundle.getString(key);
      add(node, label, acuteReferenceDose);
    }

    final String acceptableDailyIntake = hazard.acceptableDailyIntake;
    if (StringUtils.isNotBlank(acceptableDailyIntake)) {
      final String key = prefix + "acceptableDailyIntake";
      final String label = bundle.getString(key);
      add(node, label, acceptableDailyIntake);
    }

    final String hazardIndSum = hazard.hazardIndSum;
    if (StringUtils.isNotBlank(hazardIndSum)) {
      final String key = prefix + "indSumLabel";
      final String label = bundle.getString(key);
      add(node, label, hazardIndSum);
    }

    final String laboratoryName = hazard.laboratoryName;
    if (StringUtils.isNotBlank(laboratoryName)) {
      final String key = prefix + "labNameLabel";
      final String label = bundle.getString(key);
      add(node, label, laboratoryName);
    }

    final String laboratoryCountry = hazard.laboratoryCountry;
    if (StringUtils.isNotBlank(laboratoryCountry)) {
      final String key = prefix + "labCountryLabel";
      final String label = bundle.getString(key);
      add(node, label, laboratoryCountry);
    }

    final String detectionLimit = hazard.detectionLimit;
    if (StringUtils.isNotBlank(detectionLimit)) {
      final String key = prefix + "detectionLimitLabel";
      final String label = bundle.getString(key);
      add(node, label, detectionLimit);
    }

    final String quantificationLimit = hazard.quantificationLimit;
    if (StringUtils.isNotBlank(quantificationLimit)) {
      final String key = prefix + "quantificationLimitLabel";
      final String label = bundle.getString(key);
      add(node, label, quantificationLimit);
    }

    final String leftCensoredData = hazard.leftCensoredData;
    if (StringUtils.isNotBlank(leftCensoredData)) {
      final String key = prefix + "leftCensoredDataLabel";
      final String label = bundle.getString(key);
      add(node, label, leftCensoredData);
    }

    final String contaminationRange = hazard.rangeOfContamination;
    if (StringUtils.isNotBlank(contaminationRange)) {
      final String key = prefix + "contaminationRangeLabel";
      final String label = bundle.getString(key);
      add(node, label, contaminationRange);
    }
  }

  private static void add(final DefaultMutableTreeNode node,
      final PopulationGroup populationGroup) {

    final String prefix = "GM.EditPopulationGroupPanel.";

    final String populationName = populationGroup.populationName;
    if (StringUtils.isNotBlank(populationName)) {
      final String key = prefix + "populationNameLabel";
      final String label = bundle.getString(key);
      add(node, label, populationName);
    }

    final List<String> populationSpan = populationGroup.populationSpan;
    if (!populationSpan.isEmpty()) {
      final String key = prefix + "populationSpanLabel";
      final String label = bundle.getString(key);
      add(node, label, populationSpan);
    }

    final List<String> populationDescription = populationGroup.populationDescription;
    if (!populationDescription.isEmpty()) {
      final String key = prefix + "populationDescriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, populationDescription);
    }

    final List<String> populationAge = populationGroup.populationAge;
    if (!populationAge.isEmpty()) {
      final String key = prefix + "populationAgeLabel";
      final String label = bundle.getString(key);
      add(node, label, populationAge);
    }

    final String populationGender = populationGroup.populationGender;
    if (StringUtils.isNotBlank(populationGender)) {
      final String key = prefix + "populationGenderLabel";
      final String label = bundle.getString(key);
      add(node, label, populationGender);
    }

    final List<String> bmi = populationGroup.bmi;
    if (!bmi.isEmpty()) {
      final String key = prefix + "bmiLabel";
      final String label = bundle.getString(key);
      add(node, label, bmi);
    }

    final List<String> specialDietGroups = populationGroup.specialDietGroups;
    if (!specialDietGroups.isEmpty()) {
      final String key = prefix + "specialDietGroupsLabel";
      final String label = bundle.getString(key);
      add(node, label, specialDietGroups);
    }

    final List<String> patternConsumption = populationGroup.patternConsumption;
    if (!patternConsumption.isEmpty()) {
      final String key = prefix + "patternConsumptionLabel";
      final String label = bundle.getString(key);
      add(node, label, patternConsumption);
    }

    final List<String> region = populationGroup.region;
    if (!region.isEmpty()) {
      final String key = prefix + "regionLabel";
      final String label = bundle.getString(key);
      add(node, label, region);
    }

    final List<String> country = populationGroup.country;
    if (!country.isEmpty()) {
      final String key = prefix + "countryLabel";
      final String label = bundle.getString(key);
      add(node, label, country);
    }

    final List<String> populationRiskFactor = populationGroup.populationRiskFactor;
    if (!populationRiskFactor.isEmpty()) {
      final String key = prefix + "riskAndPopulationLabel";
      final String label = bundle.getString(key);
      add(node, label, populationRiskFactor);
    }

    final List<String> season = populationGroup.season;
    if (!season.isEmpty()) {
      final String key = prefix + "seasonLabel";
      final String label = bundle.getString(key);
      add(node, label, season);
    }
  }

  private static void add(final DefaultMutableTreeNode node,
      final GeneralInformation generalInformation) {

    final String prefix = "GM.GeneralInformationPanel.";

    final String name = generalInformation.name;
    if (StringUtils.isNotBlank(name)) {
      final String key = prefix + "studyNameLabel";
      final String label = bundle.getString(key);
      add(node, label, name);
    }

    final String identifier = generalInformation.identifier;
    if (StringUtils.isNotBlank(identifier)) {
      final String key = prefix + "identifierLabel";
      final String label = bundle.getString(key);
      add(node, label, identifier);
    }

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

    {
      final String key = prefix + "creationDateLabel";
      final String label = bundle.getString(key);
      add(node, label, generalInformation.creationDate);
    }

    final List<Date> modificationDate = generalInformation.modificationDate;
    if (!modificationDate.isEmpty()) {
      final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Modification dates");
      modificationDate.forEach(it -> add(parentNode, "Modification date", it));
    }

    final String rights = generalInformation.rights;
    if (StringUtils.isNotBlank(rights)) {
      final String key = prefix + "rightsLabel";
      final String label = bundle.getString(key);
      add(node, label, rights);
    }

    // TODO: isAvailable

    {
      final String key = prefix + "urlLabel";
      final String label = bundle.getString(key);
      final String value = generalInformation.url.toString();
      add(node, label, value);
    }

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

    final String language = generalInformation.language;
    if (StringUtils.isNotBlank(language)) {
      final String key = prefix + "languageLabel";
      final String label = bundle.getString(key);
      add(node, label, language);
    }

    final String software = generalInformation.software;
    if (StringUtils.isNotBlank(software)) {
      final String key = prefix + "softwareLabel";
      final String label = bundle.getString(key);
      add(node, label, software);
    }

    final String languageWrittenIn = generalInformation.languageWrittenIn;
    if (StringUtils.isNotBlank(languageWrittenIn)) {
      final String key = prefix + "languageWrittenInLabel";
      final String label = bundle.getString(key);
      add(node, label, languageWrittenIn);
    }

    final String status = generalInformation.status;
    if (StringUtils.isNotBlank(status)) {
      final String key = prefix + "statusLabel";
      final String label = bundle.getString(key);
      add(node, label, status);
    }

    final String objective = generalInformation.objective;
    if (StringUtils.isNotBlank(objective)) {
      final String key = prefix + "objectiveLabel";
      final String label = bundle.getString(key);
      add(node, label, objective);
    }

    final String description = generalInformation.description;
    if (StringUtils.isNotBlank(description)) {
      final String key = prefix + "descriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, description);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final Scope scope) {

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

    final String generalComment = scope.generalComment;
    if (StringUtils.isNotBlank(generalComment)) {
      final String key = prefix + "commentLabel";
      final String label = bundle.getString(key);
      add(node, label, generalComment);
    }

    final String temporalInformation = scope.temporalInformation;
    if (StringUtils.isNotBlank(temporalInformation)) {
      final String key = prefix + "temporalInformationLabel";
      final String label = bundle.getString(key);
      add(node, label, temporalInformation);
    }

    final List<String> region = scope.region;
    if (!region.isEmpty()) {
      final String key = prefix + "regionLabel";
      final String label = bundle.getString(key);
      add(node, label, region);
    }

    final List<String> country = scope.country;
    if (!country.isEmpty()) {
      final String key = prefix + "countryLabel";
      final String label = bundle.getString(key);
      add(node, label, country);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final DataBackground dataBackground) {

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

    final DietaryAssessmentMethod dietaryAssessmentMethod = dataBackground.dietaryAssessmentMethod;
    if (dietaryAssessmentMethod != null) {
      final String key = prefix + "dietaryAssessmentMethodLabel";
      final String label = bundle.getString(key);

      final DefaultMutableTreeNode damNode = new DefaultMutableTreeNode(label);
      add(damNode, dietaryAssessmentMethod);
      node.add(damNode);
    }

    final List<String> laboratoryAccreditation = dataBackground.laboratoryAccreditation;
    if (!laboratoryAccreditation.isEmpty()) {
      final String key = prefix + "laboratoryAccreditationLabel";
      final String label = bundle.getString(key);
      add(node, label, laboratoryAccreditation);
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

  private static void add(final DefaultMutableTreeNode node, final Study study) {

    final String prefix = "GM.StudyPanel.";

    final String title = study.title;
    if (StringUtils.isNotBlank(title)) {
      final String key = prefix + "studyTitleLabel";
      final String label = bundle.getString(key);
      add(node, label, title);
    }

    final String description = study.description;
    if (StringUtils.isNotBlank(description)) {
      final String key = prefix + "studyDescriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, description);
    }

    final String designType = study.designType;
    if (StringUtils.isNotBlank(designType)) {
      final String key = prefix + "studyDesignTypeLabel";
      final String label = bundle.getString(key);
      add(node, label, designType);
    }

    final String measurementType = study.measurementType;
    if (StringUtils.isNotBlank(measurementType)) {
      final String key = prefix + "studyAssayMeasurementsTypeLabel";
      final String label = bundle.getString(key);
      add(node, label, measurementType);
    }

    final String technologyType = study.technologyType;
    if (StringUtils.isNotBlank(technologyType)) {
      final String key = prefix + "studyAssayTechnologyTypeLabel";
      final String label = bundle.getString(key);
      add(node, label, technologyType);
    }

    final String technologyPlatform = study.technologyPlatform;
    if (StringUtils.isNotBlank(technologyPlatform)) {
      final String key = prefix + "studyAssayTechnologyPlatformLabel";
      final String label = bundle.getString(key);
      add(node, label, technologyPlatform);
    }

    final String accreditationProcedure = study.accreditationProcedure;
    if (StringUtils.isNotBlank(accreditationProcedure)) {
      final String key = prefix + "accreditationProcedureLabel";
      final String label = bundle.getString(key);
      add(node, label, accreditationProcedure);
    }

    final String protocolName = study.protocolName;
    if (StringUtils.isNotBlank(protocolName)) {
      final String key = prefix + "protocolNameLabel";
      final String label = bundle.getString(key);
      add(node, label, protocolName);
    }

    final String protocolType = study.protocolType;
    if (StringUtils.isNotBlank(protocolType)) {
      final String key = prefix + "protocolTypeLabel";
      final String label = bundle.getString(key);
      add(node, label, protocolType);
    }

    final String protocolDescription = study.protocolDescription;
    if (StringUtils.isNotBlank(protocolDescription)) {
      final String key = prefix + "protocolDescriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, protocolDescription);
    }

    final URI protocolUri = study.protocolUri;
    if (protocolUri != null) {
      final String key = prefix + "protocolURILabel";
      final String label = bundle.getString(key);
      final String value = protocolUri.toString();
      add(node, label, value);
    }

    final String protocolVersion = study.protocolVersion;
    if (StringUtils.isNotBlank(protocolVersion)) {
      final String key = prefix + "protocolVersionLabel";
      final String label = bundle.getString(key);
      add(node, label, protocolVersion);
    }

    final String parametersName = study.parametersName;
    if (StringUtils.isNotBlank(parametersName)) {
      final String key = prefix + "parametersLabel";
      final String label = bundle.getString(key);
      add(node, label, parametersName);
    }

    final String componentsType = study.componentsType;
    if (StringUtils.isNotBlank(componentsType)) {
      final String key = prefix + "componentsTypeLabel";
      final String label = bundle.getString(key);
      add(node, label, study.componentsType);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final StudySample studySample) {

    final String prefix = "GM.EditStudySamplePanel.";

    final String sample = studySample.sample;
    if (StringUtils.isNotBlank(sample)) {
      final String key = prefix + "sampleNameLabel";
      final String label = bundle.getString(key);
      add(node, label, sample);
    }

    final Double moisturePercentage = studySample.moisturePercentage;
    if (moisturePercentage != null) {
      final String key = prefix + "moisturePercentageLabel";
      final String label = bundle.getString(key);
      final String value = moisturePercentage.toString();
      add(node, label, value);
    }

    final Double fatPercentage = studySample.fatPercentage;
    if (fatPercentage != null) {
      final String key = prefix + "fatPercentageLabel";
      final String label = bundle.getString(key);
      final String value = fatPercentage.toString();
      add(node, label, value);
    }

    final String collectionProtocol = studySample.collectionProtocol;
    if (StringUtils.isNotBlank(collectionProtocol)) {
      final String key = prefix + "sampleProtocolLabel";
      final String label = bundle.getString(key);
      add(node, label, collectionProtocol);
    }

    final String samplingStrategy = studySample.samplingStrategy;
    if (StringUtils.isNotBlank(samplingStrategy)) {
      final String key = prefix + "samplingStrategyLabel";
      final String label = bundle.getString(key);
      add(node, label, samplingStrategy);
    }

    final String samplingMethod = studySample.samplingMethod;
    if (StringUtils.isNotBlank(samplingMethod)) {
      final String key = prefix + "samplingMethodLabel";
      final String label = bundle.getString(key);
      add(node, label, samplingMethod);
    }

    final String samplingPlan = studySample.samplingPlan;
    if (StringUtils.isNotBlank(samplingPlan)) {
      final String key = prefix + "samplingPlanLabel";
      final String label = bundle.getString(key);
      add(node, label, samplingPlan);
    }

    final String samplingWeight = studySample.samplingWeight;
    if (StringUtils.isNotBlank(samplingWeight)) {
      final String key = prefix + "samplingWeightLabel";
      final String label = bundle.getString(key);
      add(node, label, samplingWeight);
    }

    final String samplingSize = studySample.samplingSize;
    if (StringUtils.isNotBlank(samplingSize)) {
      final String key = prefix + "samplingSizeLabel";
      final String label = bundle.getString(key);
      add(node, label, samplingSize);
    }

    final String lotSizeUnit = studySample.lotSizeUnit;
    if (StringUtils.isNotBlank(lotSizeUnit)) {
      final String key = prefix + "lotSizeUnitLabel";
      final String label = bundle.getString(key);
      add(node, label, lotSizeUnit);
    }

    final String samplingPoint = studySample.samplingPoint;
    if (StringUtils.isNotBlank(samplingPoint)) {
      final String key = prefix + "samplingPointLabel";
      final String label = bundle.getString(key);
      add(node, label, samplingPoint);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final DietaryAssessmentMethod method) {

    // Prefix in resource bundle
    final String prefix = "GM.EditDietaryAssessmentMethodPanel.";

    final String collectionTool = method.collectionTool;
    if (StringUtils.isNotBlank(collectionTool)) {
      final String key = prefix + "dataCollectionToolLabel";
      final String label = bundle.getString(key);
      add(node, label, collectionTool);
    }

    {
      final String key = prefix + "nonConsecutiveOneDaysLabel";
      final String label = bundle.getString(key);
      final String value = Integer.toString(method.numberOfNonConsecutiveOneDay);
      add(node, label, value);
    }

    final String softwareTool = method.softwareTool;
    if (StringUtils.isNotBlank(softwareTool)) {
      final String key = prefix + "dietarySoftwareToolLabel";
      final String label = bundle.getString(key);
      add(node, label, softwareTool);
    }

    final List<String> numberOfFoodItems = method.numberOfFoodItems;
    if (!numberOfFoodItems.isEmpty()) {

      final String key = prefix + "foodItemNumberLabel";
      final String label = bundle.getString(key);
      add(node, label, numberOfFoodItems);
    }

    final List<String> recordTypes = method.recordTypes;
    if (!recordTypes.isEmpty()) {
      final String key = prefix + "recordTypeLabel";
      final String label = bundle.getString(key);
      add(node, label, recordTypes);
    }

    final List<String> foodDescriptors = method.foodDescriptors;
    if (!foodDescriptors.isEmpty()) {
      final String key = prefix + "foodDescriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, foodDescriptors);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final Assay assay) {

    final String name = assay.name;
    if (StringUtils.isNotBlank(name)) {
      final String key = "GM.EditAssayPanel.nameLabel";
      final String label = bundle.getString(key);
      add(node, label, name);
    }

    final String description = assay.description;
    if (StringUtils.isNotBlank(description)) {
      final String key = "GM.EditAssayPanel.descriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, description);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final Parameter parameter) {

    final String prefix = "GM.EditParameterPanel.";

    final String id = parameter.id;
    if (StringUtils.isNotBlank(id)) {
      final String key = prefix + "idLabel";
      final String label = bundle.getString(key);
      add(node, label, id);
    }

    {
      final String key = prefix + "classificationLabel";
      final String label = bundle.getString(key);
      final String value = parameter.classification.toString();
      add(node, label, value);
    }

    final String name = parameter.name;
    if (StringUtils.isNotBlank(name)) {
      final String key = prefix + "parameterNameLabel";
      final String label = bundle.getString(key);
      add(node, label, name);
    }

    final String description = parameter.description;
    if (StringUtils.isNotBlank(description)) {
      final String key = prefix + "descriptionLabel";
      final String label = bundle.getString(key);
      add(node, label, description);
    }

    final String unit = parameter.unit;
    if (StringUtils.isNotBlank(unit)) {
      final String key = prefix + "unitLabel";
      final String label = bundle.getString(key);
      add(node, label, unit);
    }

    final String unitCategory = parameter.unitCategory;
    if (StringUtils.isNotBlank(unitCategory)) {
      final String key = prefix + "unitCategoryLabel";
      final String label = bundle.getString(key);
      add(node, label, unitCategory);
    }

    final String dataType = parameter.dataType;
    if (StringUtils.isNotBlank(dataType)) {
      final String key = prefix + "dataTypeLabel";
      final String label = bundle.getString(key);
      add(node, label, dataType);
    }

    final String source = parameter.source;
    if (StringUtils.isNotBlank(source)) {
      final String key = prefix + "sourceLabel";
      final String label = bundle.getString(key);
      add(node, label, source);
    }

    final String subject = parameter.subject;
    if (StringUtils.isNotBlank(subject)) {
      final String key = prefix + "subjectLabel";
      final String label = bundle.getString(key);
      add(node, label, subject);
    }

    final String distribution = parameter.distribution;
    if (StringUtils.isNotBlank(distribution)) {
      final String key = prefix + "distributionLabel";
      final String label = bundle.getString(key);
      add(node, label, distribution);
    }

    final String value = parameter.value;
    if (StringUtils.isNotBlank(value)) {
      final String key = prefix + "valueLabel";
      final String label = bundle.getString(key);
      add(node, label, value);
    }

    final String reference = parameter.reference;
    if (StringUtils.isNotBlank(reference)) {
      final String key = prefix + "referenceLabel";
      final String label = bundle.getString(key);
      add(node, label, reference);
    }

    final String variabilitySubject = parameter.variabilitySubject;
    if (StringUtils.isNotBlank(variabilitySubject)) {
      final String key = prefix + "variabilitySubjectLabel";
      final String label = bundle.getString(key);
      add(node, label, variabilitySubject);
    }

    final List<String> modelApplicability = parameter.modelApplicability;
    if (!modelApplicability.isEmpty()) {
      final String key = prefix + "applicabilityLabel";
      final String label = bundle.getString(key);
      add(node, label, modelApplicability);
    }

    final Double error = parameter.error;
    if (error != null) {
      final String key = prefix + "errorLabel";
      final String label = bundle.getString(key);
      add(node, label, error.toString());
    }
  }

  private static void add(final DefaultMutableTreeNode node, final ModelEquation modelEquation) {

    final String prefix = "GM.EditModelEquationPanel.";

    final String equationName = modelEquation.equationName;
    if (StringUtils.isNotBlank(equationName)) {
      final String key = prefix + "nameLabel";
      final String label = bundle.getString(key);
      add(node, label, equationName);
    }

    final String equationClass = modelEquation.equationClass;
    if (StringUtils.isNotBlank(equationClass)) {
      final String key = prefix + "classLabel";
      final String label = bundle.getString(key);
      add(node, label, equationClass);
    }

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

    final String equation = modelEquation.equation;
    if (StringUtils.isNotBlank(equation)) {
      final String key = prefix + "scriptLabel";
      final String label = bundle.getString(key);
      add(node, label, equation);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final ModelMath modelMath) {

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

    final Double sse = modelMath.sse;
    if (sse != null) {
      final String value = sse.toString();
      add(node, "SSE", value);
    }

    final Double mse = modelMath.mse;
    if (mse != null) {
      final String value = mse.toString();
      add(node, "MSE", value);
    }

    final Double rmse = modelMath.rmse;
    if (rmse != null) {
      final String value = rmse.toString();
      add(node, "RMSE", value);
    }

    final Double rSquared = modelMath.rSquared;
    if (rSquared != null) {
      final String value = rSquared.toString();
      add(node, "r-Squared", value);
    }

    final Double aic = modelMath.aic;
    if (aic != null) {
      final String value = aic.toString();
      add(node, "aic", value);
    }

    final Double bic = modelMath.bic;
    if (bic != null) {
      final String value = bic.toString();
      add(node, "bic", value);
    }

    final ModelEquation modelEquation = modelMath.modelEquation;
    if (modelEquation != null) {
      final DefaultMutableTreeNode equationNode = new DefaultMutableTreeNode("Model equation");
      add(equationNode, modelEquation);
      node.add(equationNode);
    }

    final String fittingProcedure = modelMath.fittingProcedure;
    if (StringUtils.isNotBlank(fittingProcedure)) {
      add(node, "Fitting procedure", fittingProcedure);
    }

    // TODO: exposure

    final List<String> event = modelMath.event;
    if (!event.isEmpty()) {
      add(node, "Events", event);
    }
  }

  private static JTree createTree(GenericModel genericModel) {

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
