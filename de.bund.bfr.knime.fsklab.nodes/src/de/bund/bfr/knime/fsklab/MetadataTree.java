package de.bund.bfr.knime.fsklab;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.EList;
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;
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
import metadata.Contact;
import metadata.ModelCategory;
import metadata.ModificationDate;
import metadata.PublicationType;
import metadata.Reference;

class MetadataTree {

  private static ResourceBundle bundle =
      ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
  private static ResourceBundle bundle2 =
      ResourceBundle.getBundle("metadatatree", new UTF8Control());

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

    List<String> authors = record.getAuthors();
    if (authors != null && !authors.isEmpty()) {
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
    final List<VCard> creators =
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
    }

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
    add(node, prefix + "dataTypeLabel", parameter.dataType.toString());
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

  static JTree createTree(GenericModel genericModel) {

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

  //

  // TODO: To replace #add
  private static void add2(final DefaultMutableTreeNode node, final String key,
      final boolean value) {
    final String label = bundle2.getString(key);
    node.add(new DefaultMutableTreeNode(label + ": " + value));
  }

  private static void add2(final DefaultMutableTreeNode node, final String key, final int value) {
    final String label = bundle2.getString(key);
    node.add(new DefaultMutableTreeNode(label + ": " + value));
  }

  private static void add2(final DefaultMutableTreeNode node, final String key,
      final String value) {
    if (StringUtils.isNotBlank(value)) {
      final String label = bundle2.getString(key);
      node.add(new DefaultMutableTreeNode(label + ": " + value));
    }
  }

  private static void add2(final DefaultMutableTreeNode node, final String key, final URI value) {
    if (value != null) {
      final String label = bundle2.getString(key);
      node.add(new DefaultMutableTreeNode(label + ": " + value));
    }
  }

  private static void add2(final DefaultMutableTreeNode node, final String key, final Date value) {
    if (value != null) {
      final String label = bundle2.getString(key);
      node.add(new DefaultMutableTreeNode(label + ": " + value));
    }
  }

  private static void add2(final DefaultMutableTreeNode node, final String key,
      EList<String> value) {

    if (value != null && !value.isEmpty()) {
      String label = bundle2.getString(key);
      DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
      value.stream().map(DefaultMutableTreeNode::new).forEach(parentNode::add);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final Contact contact) {

    add2(node, "Contact.title", contact.getTitle());
    add2(node, "Contact.familyName", contact.getFamilyName());
    add2(node, "Contact.email", contact.getEmail());
    add2(node, "Contact.telephone", contact.getTelephone());
    add2(node, "Contact.streetAddress", contact.getStreetAddress());
    add2(node, "Contact.country", contact.getCountry());
    add2(node, "Contact.city", contact.getCity());
    add2(node, "Contact.zipCode", contact.getZipCode());
    add2(node, "Contact.postOfficeBox", contact.getPostOfficeBox());
    add2(node, "Contact.region", contact.getRegion());
    add2(node, "Contact.nickname", contact.getNickname());
    add2(node, "Contact.timeZone", contact.getTimeZone());
    add2(node, "Contact.gender", contact.getGender());
    add2(node, "Contact.name", contact.getName());
    add2(node, "Contact.url", contact.getUrl());
    add2(node, "Contact.note", contact.getNote());
    add2(node, "Contact.logo", contact.getLogo());
    add2(node, "Contact.organization", contact.getOrganization());
    add2(node, "Contact.fullName", contact.getFn());
  }

  private static void add(final DefaultMutableTreeNode node, final ModelCategory modelCategory) {

    add2(node, "ModelCategory.modelClass", modelCategory.getModelClass());
    add2(node, "ModelCategory.modelSubClass", modelCategory.getModelSubClass());
    add2(node, "ModelCategory.modelClassComment", modelCategory.getModelClassComment());
    add2(node, "ModelCategory.basicProcess", modelCategory.getBasicProcess());
  }

  private static void add(final DefaultMutableTreeNode node, final Reference reference) {

    add2(node, "Reference.isReferenceDescription", reference.isIsReferenceDescription());

    PublicationType publicationType = reference.getPublicationType();
    if (publicationType != null) {
      add2(node, "Reference.publicationType", publicationType.name());
    }

    add2(node, "Reference.publicationDate", reference.getPublicationDate());
    add2(node, "Reference.pmid", reference.getPmid());
    add2(node, "Reference.doi", reference.getDoi());
    add2(node, "Reference.authorList", reference.getAuthorList());
    add2(node, "Reference.publicationTitle", reference.getPublicationTitle());
    add2(node, "Reference.publicationAbstract", reference.getPublicationAbstract());
    add2(node, "Reference.publicationJournal", reference.getPublicationJournal());
    add2(node, "Reference.publicationVolume", reference.getPublicationVolume());
    add2(node, "Reference.publicationIssue", reference.getPublicationIssue());
    add2(node, "Reference.publicationStatus", reference.getPublicationStatus());
    add2(node, "Reference.publicationWebsite", reference.getPublicationWebsite());
    add2(node, "Reference.comment", reference.getComment());
  }

  private static void add(final DefaultMutableTreeNode node,
      final metadata.GeneralInformation generalInformation) {

    add2(node, "GeneralInformation.name", generalInformation.getName());
    add2(node, "GeneralInformation.source", generalInformation.getSource());
    add2(node, "GeneralInformation.identifier", generalInformation.getIdentifier());
    add2(node, "GeneralInformation.creationDate", generalInformation.getCreationDate());
    add2(node, "GeneralInformation.rights", generalInformation.getRights());
    add2(node, "GeneralInformation.available", generalInformation.isAvailable());
    add2(node, "GeneralInformation.format", generalInformation.getFormat());
    add2(node, "GeneralInformation.language", generalInformation.getLanguage());
    add2(node, "GeneralInformation.software", generalInformation.getSoftware());

    add2(node, "GeneralInformation.languageWrittenIn", generalInformation.getLanguage());
    add2(node, "GeneralInformation.status", generalInformation.getStatus());
    add2(node, "GeneralInformation.objective", generalInformation.getObjective());
    add2(node, "GeneralInformation.description", generalInformation.getDescription());

    // author
    Contact author = generalInformation.getAuthor();
    if (author != null) {
      String label = bundle2.getString("GeneralInformation.author");
      DefaultMutableTreeNode authorNode = new DefaultMutableTreeNode(label);
      add(authorNode, author);
      node.add(authorNode);
    }

    // creators
    List<Contact> creators = generalInformation.getCreators();
    if (creators != null && !creators.isEmpty()) {
      String label = bundle2.getString("GeneralInformation.creators");
      DefaultMutableTreeNode creatorsNode = new DefaultMutableTreeNode(label);
      creators.stream().map(DefaultMutableTreeNode::new).forEach(creatorsNode::add);
      node.add(creatorsNode);
    }

    // model category
    List<ModelCategory> modelCategories = generalInformation.getModelCategory();
    if (modelCategories != null && !modelCategories.isEmpty()) {
      String label = bundle2.getString("GeneralInformation.modelCategories");
      DefaultMutableTreeNode modelCategoriesNode = new DefaultMutableTreeNode(label);
      modelCategories.stream().map(DefaultMutableTreeNode::new).forEach(modelCategoriesNode::add);
      node.add(modelCategoriesNode);
    }

    // reference
    List<Reference> references = generalInformation.getReference();
    if (references != null && !references.isEmpty()) {
      String label = bundle2.getString("GeneralInformation.references");
      DefaultMutableTreeNode referencesNode = new DefaultMutableTreeNode(label);
      references.stream().map(DefaultMutableTreeNode::new).forEach(referencesNode::add);
      node.add(referencesNode);
    }

    // modification date
    List<ModificationDate> modificationDates = generalInformation.getModificationdate();
    if (modificationDates != null && !modificationDates.isEmpty()) {
      String label = bundle2.getString("GeneralInformation.modificationDates");
      DefaultMutableTreeNode modificationDatesNode = new DefaultMutableTreeNode(label);
      modificationDates.stream().map(ModificationDate::getValue).map(DefaultMutableTreeNode::new)
          .forEach(modificationDatesNode::add);
      node.add(modificationDatesNode);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.Product product) {

    add2(node, "Product.productName", product.getProductName());
    add2(node, "Product.productDescription", product.getProductDescription());
    add2(node, "Product.productUnit", product.getProductUnit());
    add2(node, "Product.productionMethod", product.getProductionMethod());
    add2(node, "Product.packaging", product.getPackaging());
    add2(node, "Product.productTreatment", product.getProductTreatment());
    add2(node, "Product.originCountry", product.getOriginCountry());
    add2(node, "Product.originArea", product.getOriginArea());
    add2(node, "Product.fisheriesArea", product.getFisheriesArea());
    add2(node, "Production.productionDate", product.getProductionDate());
    add2(node, "Product.expiryDate", product.getExpiryDate());
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.Hazard hazard) {

    add2(node, "Hazard.hazardType", hazard.getHazardType());
    add2(node, "Hazard.hazardName", hazard.getHazardName());
    add2(node, "Hazard.hazardDescription", hazard.getHazardDescription());
    add2(node, "Hazard.hazardUnit", hazard.getHazardUnit());
    add2(node, "Hazard.adverseEffect", hazard.getAdverseEffect());
    add2(node, "Hazard.sourceOfContamination", hazard.getSourceOfContamination());
    add2(node, "Hazard.benchmarkDose", hazard.getBenchmarkDose());
    add2(node, "Hazard.maximumResidueLimit", hazard.getMaximumResidueLimit());
    add2(node, "Hazard.noObservedAdverseEffectLevel", hazard.getNoObservedAdverseAffectLevel());
    add2(node, "Hazard.acceptableDailyIntake", hazard.getAcceptableDailyIntake());
    add2(node, "Hazard.acuteReferenceDose", hazard.getAcuteReferenceDose());
    add2(node, "Hazard.acceptableDailyIntake", hazard.getAcceptableDailyIntake());
    add2(node, "Hazard.hazardIndSum", hazard.getHazardIndSum());
  }

  private static void add(final DefaultMutableTreeNode node,
      final metadata.PopulationGroup populationGroup) {

    add2(node, "PopulationGroup.populationName", populationGroup.getPopulationName());
    add2(node, "PopulationGroup.targetPopulation", populationGroup.getTargetPopulation());
    add2(node, "PopulationGroup.populationSpan", populationGroup.getPopulationSpan());
    add2(node, "PopulationGroup.populationDescription", populationGroup.getPopulationDescription());
    add2(node, "PopulationGroup.populationAge", populationGroup.getPopulationAge());
    add2(node, "PopulationGroup.populationGender", populationGroup.getPopulationGender());
    add2(node, "PopulationGroup.bmi", populationGroup.getBmi());
    add2(node, "PopulationGroup.specialDietGroups", populationGroup.getSpecialDietGroups());
    add2(node, "PopulationGroup.patternConsumption", populationGroup.getPatternConsumption());
    add2(node, "PopulationGroup.region", populationGroup.getRegion());
    add2(node, "PopulationGroup.country", populationGroup.getCountry());
    add2(node, "PopulationGroup.populationRiskFactor", populationGroup.getPopulationRiskFactor());
    add2(node, "PopulationGroup.season", populationGroup.getSeason());
  }

  private static void add(final DefaultMutableTreeNode node,
      final metadata.SpatialInformation spatialInformation) {
    add2(node, "SpatialInformation.region", spatialInformation.getRegion());
    add2(node, "SpatialInformation.country", spatialInformation.getCountry());
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.Scope scope) {

    add2(node, "Scope.generalComment", scope.getGeneralComment());
    add2(node, "Scope.temporalInformation", scope.getTemporalInformation());

    // product
    List<metadata.Product> products = scope.getProduct();
    if (products != null && !products.isEmpty()) {
      String label = bundle2.getString("Scope.product");
      DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
      products.stream().map(DefaultMutableTreeNode::new).forEach(parentNode::add);
      node.add(parentNode);
    }

    // hazard
    List<metadata.Hazard> hazards = scope.getHazard();
    if (hazards != null && !hazards.isEmpty()) {
      String label = bundle2.getString("Scope.hazard");
      DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
      hazards.stream().map(DefaultMutableTreeNode::new).forEach(parentNode::add);
      node.add(parentNode);
    }

    // population group
    metadata.PopulationGroup populationGroup = scope.getPopulationGroup();
    if (populationGroup != null) {
      String label = bundle2.getString("Scope.populationGroup");
      DefaultMutableTreeNode populationGroupNode = new DefaultMutableTreeNode(label);
      add(populationGroupNode, populationGroup);
      node.add(populationGroupNode);
    }

    // spatial information
    metadata.SpatialInformation spatialInformation = scope.getSpatialInformation();
    if (spatialInformation != null) {
      String label = bundle2.getString("Scope.spatialInformation");
      DefaultMutableTreeNode spatialInformationNode = new DefaultMutableTreeNode(label);
      add(spatialInformationNode, spatialInformation);
      node.add(spatialInformationNode);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.Study study) {

    add2(node, "Study.studyIdentifier", study.getStudyIdentifier());
    add2(node, "Study.studyTitle", study.getStudyTitle());
    add2(node, "Study.studyDescription", study.getStudyDescription());
    add2(node, "Study.studyDesignType", study.getStudyDesignType());
    add2(node, "Study.studyAssayMeasurementType", study.getStudyAssayMeasurementType());
    add2(node, "Study.studyAssayTechnologyType", study.getStudyAssayTechnologyType());
    add2(node, "Study.studyAssayTechnologyPlatform", study.getStudyAssayTechnologyPlatform());
    add2(node, "Study.accreditationProcedureForTheAssayTechnology",
        study.getAccreditationProcedureForTheAssayTechnology());
    add2(node, "Study.studyProtocolName", study.getStudyProtocolName());
    add2(node, "Study.studyProtocolType", study.getStudyProtocolType());
    add2(node, "Study.studyProtocolDescription", study.getStudyProtocolDescription());
    add2(node, "Study.studyProtocolURI", study.getStudyProtocolURI());
    add2(node, "Study.studyProtocolVersion", study.getStudyProtocolVersion());
    add2(node, "Study.studyProtocolComponentsName", study.getStudyProtocolComponentsName());
    add2(node, "Study.studyProtocolComponentsType", study.getStudyProtocolComponentsType());
  }

  private static void add(final DefaultMutableTreeNode node,
      final metadata.StudySample studySample) {

    add2(node, "StudySample.sampleName", studySample.getSampleName());
    add2(node, "StudySample.protocolOfSampleCollection",
        studySample.getProtocolOfSampleCollection());
    add2(node, "StudySample.samplingStrategy", studySample.getSamplingStrategy());
    add2(node, "StudySample.typeOfSamplingProgram", studySample.getTypeOfSamplingProgram());
    add2(node, "StudySample.samplingMethod", studySample.getSamplingMethod());
    add2(node, "StudySample.samplingPlan", studySample.getSamplingPlan());
    add2(node, "StudySample.samplingWeight", studySample.getSamplingWeight());
    add2(node, "StudySample.samplingSize", studySample.getSamplingSize());
    add2(node, "StudySample.lotSizeUnit", studySample.getLotSizeUnit());
    add2(node, "StudySample.samplingPoint", studySample.getSamplingPoint());
  }

  private static void add(final DefaultMutableTreeNode node,
      final metadata.DietaryAssessmentMethod dietaryAssessmentMethod) {

    add2(node, "DietaryAssessmentMethod.collectionTool",
        dietaryAssessmentMethod.getCollectionTool());
    add2(node, "DietaryAssessmentMethod.numberOfNonConsecutiveOneDay",
        dietaryAssessmentMethod.getNumberOfNonConsecutiveOneDay());
    add2(node, "DietaryAssessmentMethod.softwareTool", dietaryAssessmentMethod.getSoftwareTool());
    add2(node, "DietaryAssessmentMethod.numberOfItems",
        dietaryAssessmentMethod.getNumberOfFoodItems());
    add2(node, "DietaryAssessmentMethod.recordTypes", dietaryAssessmentMethod.getRecordTypes());
    add2(node, "DietaryAssessmentMethod.foodDescriptors",
        dietaryAssessmentMethod.getFoodDescriptors());
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.Laboratory laboratory) {

    add2(node, "Laboratory.laboratoryAccreditation", laboratory.getLaboratoryAccreditation());
    add2(node, "Laboratory.laboratoryName", laboratory.getLaboratoryName());
    add2(node, "Laboratory.laboratoryCountry", laboratory.getLaboratoryCountry());
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.Assay assay) {

    add2(node, "Assay.assayName", assay.getAssayName());
    add2(node, "Assay.assayDescription", assay.getAssayDescription());
    add2(node, "Assay.percentageOfMoisture", assay.getPercentageOfMoisture());
    add2(node, "Assay.percentageOfFat", assay.getPercentageOfFat());
    add2(node, "Assay.limitOfDetection", assay.getLimitOfDetection());
    add2(node, "Assay.limitOfQuantification", assay.getLimitOfQuantification());
    add2(node, "Assay.leftCensoredData", assay.getLeftCensoredData());
    add2(node, "Assay.rangeOfContamination", assay.getRangeOfContamination());
    add2(node, "Asasy.uncertaintyValue", assay.getUncertaintyValue());
  }

  private static void add(final DefaultMutableTreeNode node,
      final metadata.DataBackground dataBackground) {

    // study
    metadata.Study study = dataBackground.getStudy();
    if (study != null) {
      String label = bundle2.getString("DataBackground.study");
      DefaultMutableTreeNode studyNode = new DefaultMutableTreeNode(label);
      add(studyNode, study);
      node.add(studyNode);
    }

    // study sample
    List<metadata.StudySample> studySample = dataBackground.getStudysample();
    if (studySample != null && !studySample.isEmpty()) {
      String label = bundle2.getString("DataBackground.studySample");
      DefaultMutableTreeNode studySampleNode = new DefaultMutableTreeNode(label);
      studySample.stream().map(DefaultMutableTreeNode::new).forEach(studySampleNode::add);
      node.add(studySampleNode);
    }
    
    // dietary assessment method
    metadata.DietaryAssessmentMethod dietaryAssessmentMethod =
        dataBackground.getDietaryassessmentmethod();
    if (dietaryAssessmentMethod != null) {
      String label = bundle2.getString("DataBackground.dietaryAssessmentMethod");
      DefaultMutableTreeNode dietaryAssessmentMethodNode = new DefaultMutableTreeNode(label);
      add(dietaryAssessmentMethodNode, dietaryAssessmentMethod);
      node.add(dietaryAssessmentMethodNode);
    }

    // laboratory
    metadata.Laboratory laboratory = dataBackground.getLaboratory();
    if (laboratory != null) {
      String label = bundle2.getString("DataBackground.laboratory");
      DefaultMutableTreeNode laboratoryNode = new DefaultMutableTreeNode(label);
      add(laboratoryNode, laboratory);
      node.add(laboratoryNode);
    }

    // assay
    List<metadata.Assay> assay = dataBackground.getAssay();
    if (assay != null && !assay.isEmpty()) {
      String label = bundle2.getString("DataBackground.assay");
      DefaultMutableTreeNode assayNode = new DefaultMutableTreeNode(label);
      assay.stream().map(DefaultMutableTreeNode::new).forEach(assayNode::add);
      node.add(assayNode);
    }
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.Parameter parameter) {
    
    add2(node, "Parameter.parameterId", parameter.getParameterID());

    // parameter classification
    metadata.ParameterClassification parameterClassification =
        parameter.getParameterClassification();
    if (parameterClassification != null) {
      add2(node, "Parameter.parameterClassification", parameterClassification.name());
    }
    
    add2(node, "Parameter.parameterName", parameter.getParameterName());
    add2(node, "Parameter.parameterDescription", parameter.getParameterDescription());
    add2(node, "Parameter.parameterType", parameter.getParameterType());
    add2(node, "Parameter.parameterUnit", parameter.getParameterUnit());
    add2(node, "Parameter.parameterUnitCategory", parameter.getParameterUnitCategory());
    add2(node, "Parameter.parameterDataType", parameter.getParameterDataType());
    add2(node, "Parameter.parameterSource", parameter.getParameterSource());
    add2(node, "Parameter.parameterSubject", parameter.getParameterSubject());
    add2(node, "Parameter.parameterDistribution", parameter.getParameterDistribution());
    add2(node, "Parameter.parameterValue", parameter.getParameterValue());
    add2(node, "Parameter.parameterVariabilitySubject", parameter.getParameterVariabilitySubject());
    add2(node, "Parameter.parameterValueMin", parameter.getParameterValueMin());
    add2(node, "Parameter.parameterValueMax", parameter.getParameterValueMax());
    add2(node, "Parameter.parameterError", parameter.getParameterError());
  }

  private static void add(final DefaultMutableTreeNode node,
      final metadata.ModelEquation modelEquation) {
    
    add2(node, "ModelEquation.modelEquationName", modelEquation.getModelEquationName());
    add2(node, "ModelEquation.modelEquationClass", modelEquation.getModelEquationClass());
    add2(node, "ModelEquation.modelEquation", modelEquation.getModelEquation());
    add2(node, "ModelEquation.hypothesisOfTheModel", modelEquation.getHypothesisOfTheModel());
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.Exposure exposure) {

    add2(node, "Exposure.methodologicalTreatmentOfLeftCensoredData",
        exposure.getMethodologicalTreatmentOfLeftCensoredData());
    add2(node, "Exposure.levelOfContaminationAfterLeftCensoredDataTreatment",
        exposure.getLevelOfContaminationAfterLeftCensoredDataTreatment());
    add2(node, "Exposure.typeOfExposure", exposure.getTypeOfExposure());
    add2(node, "Exposure.scenario", exposure.getScenario());
    add2(node, "Exposure.uncertaintyEstimation", exposure.getUncertaintyEstimation());
  }

  private static void add(final DefaultMutableTreeNode node, final metadata.ModelMath modelMath) {

    add2(node, "ModelMath.qualityMeasures", modelMath.getQualityMeasures());
    add2(node, "ModelMath.fittingProcedure", modelMath.getFittingProcedure());
    add2(node, "ModelMath.event", modelMath.getEvent());

    // parameter
    List<metadata.Parameter> parameter = modelMath.getParameter();
    if (parameter != null && !parameter.isEmpty()) {
      String label = bundle2.getString("ModelMath.parameter");
      DefaultMutableTreeNode parameterNode = new DefaultMutableTreeNode(label);
      parameter.stream().map(DefaultMutableTreeNode::new).forEach(parameterNode::add);
      node.add(parameterNode);
    }

    // model equation
    List<metadata.ModelEquation> modelEquation = modelMath.getModelEquation();
    if (modelEquation != null && !modelEquation.isEmpty()) {
      String label = bundle2.getString("ModelMath.modelEquation");
      DefaultMutableTreeNode modelEquationNode = new DefaultMutableTreeNode(label);
      modelEquation.stream().map(DefaultMutableTreeNode::new).forEach(modelEquationNode::add);
      node.add(modelEquationNode);
    }

    // exposure
    metadata.Exposure exposure = modelMath.getExposure();
    if (exposure != null) {
      String label = bundle2.getString("ModelMath.exposure");
      DefaultMutableTreeNode exposureNode = new DefaultMutableTreeNode(label);
      add(exposureNode, exposure);
      node.add(exposureNode);
    }
  }
}
