package metadata;

import java.net.URI;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.eclipse.emf.common.util.EList;

import de.bund.bfr.knime.fsklab.util.UTF8Control;

public class MetadataTree {

	private static ResourceBundle bundle = ResourceBundle.getBundle("metadatatree", new UTF8Control());

	public static JTree createTree(GeneralInformation generalInformation, Scope scope, DataBackground dataBackground,
			ModelMath modelMath) {

		DefaultMutableTreeNode generalInformationNode = new DefaultMutableTreeNode(
				bundle.getString("GeneralInformation"));
		if (generalInformation != null) {
			add(generalInformationNode, generalInformation);
		}

		DefaultMutableTreeNode scopeNode = new DefaultMutableTreeNode(bundle.getString("Scope"));
		if (scope != null) {
			add(scopeNode, scope);
		}

		DefaultMutableTreeNode dataBackgroundNode = new DefaultMutableTreeNode(bundle.getString("DataBackground"));
		if (dataBackground != null) {
			add(dataBackgroundNode, dataBackground);
		}

		DefaultMutableTreeNode modelMathNode = new DefaultMutableTreeNode(bundle.getString("ModelMath"));
		if (modelMath != null) {
			add(modelMathNode, modelMath);
		}

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		rootNode.add(generalInformationNode);
		rootNode.add(scopeNode);
		rootNode.add(dataBackgroundNode);
		rootNode.add(modelMathNode);

		final JTree tree = new JTree(rootNode);
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		return tree;
	}

	private static void add(final DefaultMutableTreeNode node, final String key, final boolean value) {
		final String label = bundle.getString(key);
		node.add(new DefaultMutableTreeNode(label + ": " + value));
	}

	private static void add(final DefaultMutableTreeNode node, final String key, final int value) {
		final String label = bundle.getString(key);
		node.add(new DefaultMutableTreeNode(label + ": " + value));
	}

	private static void add(final DefaultMutableTreeNode node, final String key, final String value) {
		final String label = bundle.getString(key);
		node.add(new DefaultMutableTreeNode(label + ": " + value));
	}

	private static void add(final DefaultMutableTreeNode node, final String key, final URI value) {
		final String label = bundle.getString(key);
		node.add(new DefaultMutableTreeNode(label + ": " + value));
	}

	private static void add(final DefaultMutableTreeNode node, final String key, final Date value) {
		final String label = bundle.getString(key);
		node.add(new DefaultMutableTreeNode(label + ": " + value));
	}

	/**
	 * Create a tree node for a {@code List<StringObject>} property and add it to a
	 * passed node. The passed value cannot be null or empty.
	 */
	private static void add(final DefaultMutableTreeNode node, final String key, EList<StringObject> value) {
		String label = bundle.getString(key);
		DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
		value.stream().map(DefaultMutableTreeNode::new).forEach(parentNode::add);
	}

	private static void add(final DefaultMutableTreeNode node, final Contact contact) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (contact.eIsSet(pkg.getContact_Title())) {
			add(node, "Contact.title", contact.getTitle());
		}

		if (contact.eIsSet(pkg.getContact_FamilyName())) {
			add(node, "Contact.familyName", contact.getFamilyName());
		}

		if (contact.eIsSet(pkg.getContact_GivenName())) {
			add(node, "Contact.givenName", contact.getGivenName());
		}

		if (contact.eIsSet(pkg.getContact_Email())) {
			add(node, "Contact.email", contact.getEmail());
		}

		if (contact.eIsSet(pkg.getContact_Telephone())) {
			add(node, "Contact.telephone", contact.getTelephone());
		}

		if (contact.eIsSet(pkg.getContact_StreetAddress())) {
			add(node, "Contact.streetAddress", contact.getStreetAddress());
		}

		if (contact.eIsSet(pkg.getContact_Country())) {
			add(node, "Contact.country", contact.getCountry());
		}

		if (contact.eIsSet(pkg.getContact_City())) {
			add(node, "Contact.city", contact.getCity());
		}

		if (contact.eIsSet(pkg.getContact_ZipCode())) {
			add(node, "Contact.zipCode", contact.getZipCode());
		}

		if (contact.eIsSet(pkg.getContact_Region())) {
			add(node, "Contact.region", contact.getRegion());
		}

		if (contact.eIsSet(pkg.getContact_TimeZone())) {
			add(node, "Contact.timeZone", contact.getTimeZone());
		}

		if (contact.eIsSet(pkg.getContact_Gender())) {
			add(node, "Contact.gender", contact.getGender());
		}

		if (contact.eIsSet(pkg.getContact_Note())) {
			add(node, "Contact.note", contact.getNote());
		}

		if (contact.eIsSet(pkg.getContact_Organization())) {
			add(node, "Contact.organization", contact.getOrganization());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final ModelCategory modelCategory) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (modelCategory.eIsSet(pkg.getModelCategory_ModelClass())) {
			add(node, "ModelCategory.modelClass", modelCategory.getModelClass());
		}

		if (modelCategory.eIsSet(pkg.getModelCategory_ModelSubClass())) {
			add(node, "ModelCategory.modelSubClass", modelCategory.getModelSubClass());
		}

		if (modelCategory.eIsSet(pkg.getModelCategory_ModelClassComment())) {
			add(node, "ModelCategory.modelClassComment", modelCategory.getModelClassComment());
		}

		if (modelCategory.eIsSet(pkg.getModelCategory_BasicProcess())) {
			add(node, "ModelCategory.basicProcess", modelCategory.getBasicProcess());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Reference reference) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (reference.eIsSet(pkg.getReference_IsReferenceDescription())) {
			add(node, "Reference.isReferenceDescription", reference.isIsReferenceDescription());
		}

		if (reference.eIsSet(pkg.getReference_PublicationType())) {
			add(node, "Reference.publicationType", reference.getPublicationType().name());
		}

		if (reference.eIsSet(pkg.getReference_PublicationDate())) {
			add(node, "Reference.publicationDate", reference.getPublicationDate());
		}

		if (reference.eIsSet(pkg.getReference_Pmid())) {
			add(node, "Reference.pmid", reference.getPmid());
		}

		if (reference.eIsSet(pkg.getReference_Doi())) {
			add(node, "Reference.doi", reference.getDoi());
		}

		if (reference.eIsSet(pkg.getReference_AuthorList())) {
			add(node, "Reference.authorList", reference.getAuthorList());
		}

		if (reference.eIsSet(pkg.getReference_PublicationTitle())) {
			add(node, "Reference.publicationTitle", reference.getPublicationTitle());
		}

		if (reference.eIsSet(pkg.getReference_PublicationAbstract())) {
			add(node, "Reference.publicationAbstract", reference.getPublicationAbstract());
		}

		if (reference.eIsSet(pkg.getReference_PublicationJournal())) {
			add(node, "Reference.publicationJournal", reference.getPublicationJournal());
		}

		if (reference.eIsSet(pkg.getReference_PublicationVolume())) {
			add(node, "Reference.publicationVolume", reference.getPublicationVolume());
		}

		if (reference.eIsSet(pkg.getReference_PublicationIssue())) {
			add(node, "Reference.publicationIssue", reference.getPublicationIssue());
		}

		if (reference.eIsSet(pkg.getReference_PublicationStatus())) {
			add(node, "Reference.publicationStatus", reference.getPublicationStatus());
		}

		if (reference.eIsSet(pkg.getReference_PublicationWebsite())) {
			add(node, "Reference.publicationWebsite", reference.getPublicationWebsite());
		}

		if (reference.eIsSet(pkg.getReference_Comment())) {
			add(node, "Reference.comment", reference.getComment());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final GeneralInformation generalInformation) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Name())) {
			add(node, "GeneralInformation.name", generalInformation.getName());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Source())) {
			add(node, "GeneralInformation.source", generalInformation.getSource());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Identifier())) {
			add(node, "GeneralInformation.identifier", generalInformation.getIdentifier());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_CreationDate())) {
			add(node, "GeneralInformation.creationDate", generalInformation.getCreationDate());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Rights())) {
			add(node, "GeneralInformation.rights", generalInformation.getRights());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Available())) {
			add(node, "GeneralInformation.available", generalInformation.isAvailable());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Format())) {
			add(node, "GeneralInformation.format", generalInformation.getFormat());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Language())) {
			add(node, "GeneralInformation.language", generalInformation.getLanguage());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Software())) {
			add(node, "GeneralInformation.software", generalInformation.getSoftware());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Language())) {
			add(node, "GeneralInformation.languageWrittenIn", generalInformation.getLanguage());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Status())) {
			add(node, "GeneralInformation.status", generalInformation.getStatus());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Objective())) {
			add(node, "GeneralInformation.objective", generalInformation.getObjective());
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Description())) {
			add(node, "GeneralInformation.description", generalInformation.getDescription());
		}

		// author
		if (generalInformation.eIsSet(pkg.getGeneralInformation_Author())) {
			String label = bundle.getString("GeneralInformation.author");
			DefaultMutableTreeNode authorNode = new DefaultMutableTreeNode(label);
			add(authorNode, generalInformation.getAuthor());
			node.add(authorNode);
		}

		// creators
		if (generalInformation.eIsSet(pkg.getGeneralInformation_Creators())) {
			String label = bundle.getString("GeneralInformation.creators");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);

			for (Contact creator : generalInformation.getCreators()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, creator);
				parentNode.add(childNode);
			}

			node.add(parentNode);
		}

		// model category
		if (generalInformation.eIsSet(pkg.getGeneralInformation_ModelCategory())) {

			String label = bundle.getString("GeneralInformation.modelCategories");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);

			for (ModelCategory modelCategory : generalInformation.getModelCategory()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, modelCategory);
				parentNode.add(childNode);
			}

			node.add(parentNode);
		}

		// reference
		if (generalInformation.eIsSet(pkg.getGeneralInformation_Reference())) {

			String label = bundle.getString("GeneralInformation.references");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);

			for (Reference reference : generalInformation.getReference()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, reference);
				parentNode.add(childNode);
			}

			node.add(parentNode);
		}

		// modification date
		if (generalInformation.eIsSet(pkg.getGeneralInformation_Modificationdate())) {
			String label = bundle.getString("GeneralInformation.modificationDates");
			DefaultMutableTreeNode modificationDatesNode = new DefaultMutableTreeNode(label);
			generalInformation.getModificationdate().stream().map(ModificationDate::getValue)
					.map(DefaultMutableTreeNode::new).forEach(modificationDatesNode::add);
			node.add(modificationDatesNode);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Product product) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (product.eIsSet(pkg.getProduct_ProductName())) {
			add(node, "Product.productName", product.getProductName());
		}

		if (product.eIsSet(pkg.getProduct_ProductDescription())) {
			add(node, "Product.productDescription", product.getProductDescription());
		}

		if (product.eIsSet(pkg.getProduct_ProductUnit())) {
			add(node, "Product.productUnit", product.getProductUnit());
		}

		if (product.eIsSet(pkg.getProduct_ProductionMethod())) {
			add(node, "Product.productionMethod", product.getProductionMethod());
		}

		if (product.eIsSet(pkg.getProduct_Packaging())) {
			add(node, "Product.packaging", product.getPackaging());
		}

		if (product.eIsSet(pkg.getProduct_ProductTreatment())) {
			add(node, "Product.productTreatment", product.getProductTreatment());
		}

		if (product.eIsSet(pkg.getProduct_OriginCountry())) {
			add(node, "Product.originCountry", product.getOriginCountry());
		}

		if (product.eIsSet(pkg.getProduct_OriginArea())) {
			add(node, "Product.originArea", product.getOriginArea());
		}

		if (product.eIsSet(pkg.getProduct_FisheriesArea())) {
			add(node, "Product.fisheriesArea", product.getFisheriesArea());
		}

		if (product.eIsSet(pkg.getProduct_ProductionDate())) {
			add(node, "Production.productionDate", product.getProductionDate());
		}

		if (product.eIsSet(pkg.getProduct_ExpiryDate())) {
			add(node, "Product.expiryDate", product.getExpiryDate());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Hazard hazard) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (hazard.eIsSet(pkg.getHazard_HazardType())) {
			add(node, "Hazard.hazardType", hazard.getHazardType());
		}

		if (hazard.eIsSet(pkg.getHazard_HazardName())) {
			add(node, "Hazard.hazardName", hazard.getHazardName());
		}

		if (hazard.eIsSet(pkg.getHazard_HazardDescription())) {
			add(node, "Hazard.hazardDescription", hazard.getHazardDescription());
		}

		if (hazard.eIsSet(pkg.getHazard_HazardUnit())) {
			add(node, "Hazard.hazardUnit", hazard.getHazardUnit());
		}

		if (hazard.eIsSet(pkg.getHazard_AdverseEffect())) {
			add(node, "Hazard.adverseEffect", hazard.getAdverseEffect());
		}

		if (hazard.eIsSet(pkg.getHazard_SourceOfContamination())) {
			add(node, "Hazard.sourceOfContamination", hazard.getSourceOfContamination());
		}

		if (hazard.eIsSet(pkg.getHazard_BenchmarkDose())) {
			add(node, "Hazard.benchmarkDose", hazard.getBenchmarkDose());
		}

		if (hazard.eIsSet(pkg.getHazard_MaximumResidueLimit())) {
			add(node, "Hazard.maximumResidueLimit", hazard.getMaximumResidueLimit());
		}

		if (hazard.eIsSet(pkg.getHazard_NoObservedAdverseAffectLevel())) {
			add(node, "Hazard.noObservedAdverseEffectLevel", hazard.getNoObservedAdverseAffectLevel());
		}

		if (hazard.eIsSet(pkg.getHazard_AcceptableDailyIntake())) {
			add(node, "Hazard.acceptableDailyIntake", hazard.getAcceptableDailyIntake());
		}

		if (hazard.eIsSet(pkg.getHazard_AcuteReferenceDose())) {
			add(node, "Hazard.acuteReferenceDose", hazard.getAcuteReferenceDose());
		}

		if (hazard.eIsSet(pkg.getHazard_AcceptableDailyIntake())) {
			add(node, "Hazard.acceptableDailyIntake", hazard.getAcceptableDailyIntake());
		}

		if (hazard.eIsSet(pkg.getHazard_HazardIndSum())) {
			add(node, "Hazard.hazardIndSum", hazard.getHazardIndSum());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final PopulationGroup populationGroup) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationName())) {
			add(node, "PopulationGroup.populationName", populationGroup.getPopulationName());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_TargetPopulation())) {
			add(node, "PopulationGroup.targetPopulation", populationGroup.getTargetPopulation());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationSpan())) {
			add(node, "PopulationGroup.populationSpan", populationGroup.getPopulationSpan());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationDescription())) {
			add(node, "PopulationGroup.populationDescription", populationGroup.getPopulationDescription());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationAge())) {
			add(node, "PopulationGroup.populationAge", populationGroup.getPopulationAge());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationGender())) {
			add(node, "PopulationGroup.populationGender", populationGroup.getPopulationGender());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_Bmi())) {
			add(node, "PopulationGroup.bmi", populationGroup.getBmi());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_SpecialDietGroups())) {
			add(node, "PopulationGroup.specialDietGroups", populationGroup.getSpecialDietGroups());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PatternConsumption())) {
			add(node, "PopulationGroup.patternConsumption", populationGroup.getPatternConsumption());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_Region())) {
			add(node, "PopulationGroup.region", populationGroup.getRegion());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_Country())) {
			add(node, "PopulationGroup.country", populationGroup.getCountry());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationRiskFactor())) {
			add(node, "PopulationGroup.populationRiskFactor", populationGroup.getPopulationRiskFactor());
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_Season())) {
			add(node, "PopulationGroup.season", populationGroup.getSeason());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final SpatialInformation spatialInformation) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (spatialInformation.eIsSet(pkg.getSpatialInformation_Region())) {
			add(node, "SpatialInformation.region", spatialInformation.getRegion());
		}

		if (spatialInformation.eIsSet(pkg.getSpatialInformation_Country())) {
			add(node, "SpatialInformation.country", spatialInformation.getCountry());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Scope scope) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (scope.eIsSet(pkg.getScope_GeneralComment())) {
			add(node, "Scope.generalComment", scope.getGeneralComment());
		}

		if (scope.eIsSet(pkg.getScope_TemporalInformation())) {
			add(node, "Scope.temporalInformation", scope.getTemporalInformation());
		}

		// product
		if (scope.eIsSet(pkg.getScope_Product())) {
			String label = bundle.getString("Scope.product");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (Product product : scope.getProduct()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, product);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		// hazard
		if (scope.eIsSet(pkg.getScope_Hazard())) {
			String label = bundle.getString("Scope.hazard");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (Hazard hazard : scope.getHazard()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, hazard);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		// population group
		if (scope.eIsSet(pkg.getScope_PopulationGroup())) {
			String label = bundle.getString("Scope.populationGroup");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (PopulationGroup populationGroup : scope.getPopulationGroup()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, populationGroup);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		// spatial information
		if (scope.eIsSet(pkg.getScope_SpatialInformation())) {
			String label = bundle.getString("Scope.spatialInformation");
			DefaultMutableTreeNode spatialInformationNode = new DefaultMutableTreeNode(label);
			add(spatialInformationNode, scope.getSpatialInformation());
			node.add(spatialInformationNode);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Study study) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (study.eIsSet(pkg.getStudy_StudyIdentifier())) {
			add(node, "Study.studyIdentifier", study.getStudyIdentifier());
		}

		if (study.eIsSet(pkg.getStudy_StudyTitle())) {
			add(node, "Study.studyTitle", study.getStudyTitle());
		}

		if (study.eIsSet(pkg.getStudy_StudyDescription())) {
			add(node, "Study.studyDescription", study.getStudyDescription());
		}

		if (study.eIsSet(pkg.getStudy_StudyDesignType())) {
			add(node, "Study.studyDesignType", study.getStudyDesignType());
		}

		if (study.eIsSet(pkg.getStudy_StudyAssayMeasurementType())) {
			add(node, "Study.studyAssayMeasurementType", study.getStudyAssayMeasurementType());
		}

		if (study.eIsSet(pkg.getStudy_StudyAssayTechnologyType())) {
			add(node, "Study.studyAssayTechnologyType", study.getStudyAssayTechnologyType());
		}

		if (study.eIsSet(pkg.getStudy_StudyAssayTechnologyPlatform())) {
			add(node, "Study.studyAssayTechnologyPlatform", study.getStudyAssayTechnologyPlatform());
		}

		if (study.eIsSet(pkg.getStudy_AccreditationProcedureForTheAssayTechnology())) {
			add(node, "Study.accreditationProcedureForTheAssayTechnology",
					study.getAccreditationProcedureForTheAssayTechnology());
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolName())) {
			add(node, "Study.studyProtocolName", study.getStudyProtocolName());
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolType())) {
			add(node, "Study.studyProtocolType", study.getStudyProtocolType());
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolDescription())) {
			add(node, "Study.studyProtocolDescription", study.getStudyProtocolDescription());
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolURI())) {
			add(node, "Study.studyProtocolURI", study.getStudyProtocolURI());
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolVersion())) {
			add(node, "Study.studyProtocolVersion", study.getStudyProtocolVersion());
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolComponentsName())) {
			add(node, "Study.studyProtocolComponentsName", study.getStudyProtocolComponentsName());
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolComponentsType())) {
			add(node, "Study.studyProtocolComponentsType", study.getStudyProtocolComponentsType());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final StudySample studySample) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (studySample.eIsSet(pkg.getStudySample_SampleName())) {
			add(node, "StudySample.sampleName", studySample.getSampleName());
		}

		if (studySample.eIsSet(pkg.getStudySample_ProtocolOfSampleCollection())) {
			add(node, "StudySample.protocolOfSampleCollection", studySample.getProtocolOfSampleCollection());
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingStrategy())) {
			add(node, "StudySample.samplingStrategy", studySample.getSamplingStrategy());
		}

		if (studySample.eIsSet(pkg.getStudySample_TypeOfSamplingProgram())) {
			add(node, "StudySample.typeOfSamplingProgram", studySample.getTypeOfSamplingProgram());
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingMethod())) {
			add(node, "StudySample.samplingMethod", studySample.getSamplingMethod());
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingPlan())) {
			add(node, "StudySample.samplingPlan", studySample.getSamplingPlan());
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingWeight())) {
			add(node, "StudySample.samplingWeight", studySample.getSamplingWeight());
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingSize())) {
			add(node, "StudySample.samplingSize", studySample.getSamplingSize());
		}

		if (studySample.eIsSet(pkg.getStudySample_LotSizeUnit())) {
			add(node, "StudySample.lotSizeUnit", studySample.getLotSizeUnit());
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingPoint())) {
			add(node, "StudySample.samplingPoint", studySample.getSamplingPoint());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final DietaryAssessmentMethod dietaryAssessmentMethod) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (dietaryAssessmentMethod.eIsSet(pkg.getDietaryAssessmentMethod_CollectionTool())) {
			add(node, "DietaryAssessmentMethod.collectionTool", dietaryAssessmentMethod.getCollectionTool());
		}

		if (dietaryAssessmentMethod.eIsSet(pkg.getDietaryAssessmentMethod_NumberOfNonConsecutiveOneDay())) {
			add(node, "DietaryAssessmentMethod.numberOfNonConsecutiveOneDay",
					dietaryAssessmentMethod.getNumberOfNonConsecutiveOneDay());
		}

		if (dietaryAssessmentMethod.eIsSet(pkg.getDietaryAssessmentMethod_SoftwareTool())) {
			add(node, "DietaryAssessmentMethod.softwareTool", dietaryAssessmentMethod.getSoftwareTool());
		}

		if (dietaryAssessmentMethod.eIsSet(pkg.getDietaryAssessmentMethod_NumberOfFoodItems())) {
			add(node, "DietaryAssessmentMethod.numberOfItems", dietaryAssessmentMethod.getNumberOfFoodItems());
		}

		if (dietaryAssessmentMethod.eIsSet(pkg.getDietaryAssessmentMethod_RecordTypes())) {
			add(node, "DietaryAssessmentMethod.recordTypes", dietaryAssessmentMethod.getRecordTypes());
		}

		if (dietaryAssessmentMethod.eIsSet(pkg.getDietaryAssessmentMethod_FoodDescriptors())) {
			add(node, "DietaryAssessmentMethod.foodDescriptors", dietaryAssessmentMethod.getFoodDescriptors());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Laboratory laboratory) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryAccreditation())) {
			add(node, "Laboratory.laboratoryAccreditation", laboratory.getLaboratoryAccreditation());
		}

		if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryName())) {
			add(node, "Laboratory.laboratoryName", laboratory.getLaboratoryName());
		}

		if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryCountry())) {
			add(node, "Laboratory.laboratoryCountry", laboratory.getLaboratoryCountry());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Assay assay) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (assay.eIsSet(pkg.getAssay_AssayName())) {
			add(node, "Assay.assayName", assay.getAssayName());
		}

		if (assay.eIsSet(pkg.getAssay_AssayDescription())) {
			add(node, "Assay.assayDescription", assay.getAssayDescription());
		}

		if (assay.eIsSet(pkg.getAssay_PercentageOfMoisture())) {
			add(node, "Assay.percentageOfMoisture", assay.getPercentageOfMoisture());
		}

		if (assay.eIsSet(pkg.getAssay_PercentageOfFat())) {
			add(node, "Assay.percentageOfFat", assay.getPercentageOfFat());
		}

		if (assay.eIsSet(pkg.getAssay_LimitOfDetection())) {
			add(node, "Assay.limitOfDetection", assay.getLimitOfDetection());
		}

		if (assay.eIsSet(pkg.getAssay_LimitOfQuantification())) {
			add(node, "Assay.limitOfQuantification", assay.getLimitOfQuantification());
		}

		if (assay.eIsSet(pkg.getAssay_LeftCensoredData())) {
			add(node, "Assay.leftCensoredData", assay.getLeftCensoredData());
		}

		if (assay.eIsSet(pkg.getAssay_RangeOfContamination())) {
			add(node, "Assay.rangeOfContamination", assay.getRangeOfContamination());
		}

		if (assay.eIsSet(pkg.getAssay_UncertaintyValue())) {
			add(node, "Asasy.uncertaintyValue", assay.getUncertaintyValue());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final DataBackground dataBackground) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		// study
		if (dataBackground.eIsSet(pkg.getDataBackground_Study())) {
			String label = bundle.getString("DataBackground.study");
			DefaultMutableTreeNode studyNode = new DefaultMutableTreeNode(label);
			add(studyNode, dataBackground.getStudy());
			node.add(studyNode);
		}

		// study sample
		if (dataBackground.eIsSet(pkg.getDataBackground_Studysample())) {
			String label = bundle.getString("DataBackground.studySample");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (StudySample studySample : dataBackground.getStudysample()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, studySample);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		// dietary assessment method
		if (dataBackground.eIsSet(pkg.getDataBackground_Dietaryassessmentmethod())) {	
			String label = bundle.getString("DataBackground.dietaryAssessmentMethod");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (DietaryAssessmentMethod method : dataBackground.getDietaryassessmentmethod()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, method);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		// laboratory
		if (dataBackground.eIsSet(pkg.getDataBackground_Laboratory())) {
			String label = bundle.getString("DataBackground.laboratory");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (Laboratory laboratory : dataBackground.getLaboratory()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, laboratory);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		// assay
		if (dataBackground.eIsSet(pkg.getDataBackground_Assay())) {
			String label = bundle.getString("DataBackground.assay");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (Assay assay : dataBackground.getAssay()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, assay);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Parameter parameter) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (parameter.eIsSet(pkg.getParameter_ParameterID())) {
			add(node, "Parameter.parameterId", parameter.getParameterID());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterClassification())) {
			add(node, "Parameter.parameterClassification", parameter.getParameterClassification().name());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterName())) {
			add(node, "Parameter.parameterName", parameter.getParameterName());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterDescription())) {
			add(node, "Parameter.parameterDescription", parameter.getParameterDescription());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterType())) {
			add(node, "Parameter.parameterType", parameter.getParameterType());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterUnit())) {
			add(node, "Parameter.parameterUnit", parameter.getParameterUnit());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterUnitCategory())) {
			add(node, "Parameter.parameterUnitCategory", parameter.getParameterUnitCategory());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterDataType())) {
			add(node, "Parameter.parameterDataType", parameter.getParameterDataType().name());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterSource())) {
			add(node, "Parameter.parameterSource", parameter.getParameterSource());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterSubject())) {
			add(node, "Parameter.parameterSubject", parameter.getParameterSubject());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterDistribution())) {
			add(node, "Parameter.parameterDistribution", parameter.getParameterDistribution());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterValue())) {
			add(node, "Parameter.parameterValue", parameter.getParameterValue());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterVariabilitySubject())) {
			add(node, "Parameter.parameterVariabilitySubject", parameter.getParameterVariabilitySubject());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterValueMin())) {
			add(node, "Parameter.parameterValueMin", parameter.getParameterValueMin());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterValueMax())) {
			add(node, "Parameter.parameterValueMax", parameter.getParameterValueMax());
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterError())) {
			add(node, "Parameter.parameterError", parameter.getParameterError());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final ModelEquation modelEquation) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (modelEquation.eIsSet(pkg.getModelEquation_ModelEquationName())) {
			add(node, "ModelEquation.modelEquationName", modelEquation.getModelEquationName());
		}

		if (modelEquation.eIsSet(pkg.getModelEquation_ModelEquationClass())) {
			add(node, "ModelEquation.modelEquationClass", modelEquation.getModelEquationClass());
		}

		if (modelEquation.eIsSet(pkg.getModelEquation_ModelEquation())) {
			add(node, "ModelEquation.modelEquation", modelEquation.getModelEquation());
		}

		if (modelEquation.eIsSet(pkg.getModelEquation_HypothesisOfTheModel())) {
			add(node, "ModelEquation.hypothesisOfTheModel", modelEquation.getHypothesisOfTheModel());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Exposure exposure) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (exposure.eIsSet(pkg.getExposure_MethodologicalTreatmentOfLeftCensoredData())) {
			add(node, "Exposure.methodologicalTreatmentOfLeftCensoredData",
					exposure.getMethodologicalTreatmentOfLeftCensoredData());
		}

		if (exposure.eIsSet(pkg.getExposure_LevelOfContaminationAfterLeftCensoredDataTreatment())) {
			add(node, "Exposure.levelOfContaminationAfterLeftCensoredDataTreatment",
					exposure.getLevelOfContaminationAfterLeftCensoredDataTreatment());
		}

		if (exposure.eIsSet(pkg.getExposure_TypeOfExposure())) {
			add(node, "Exposure.typeOfExposure", exposure.getTypeOfExposure());
		}

		if (exposure.eIsSet(pkg.getExposure_Scenario())) {
			add(node, "Exposure.scenario", exposure.getScenario());
		}

		if (exposure.eIsSet(pkg.getExposure_UncertaintyEstimation())) {
			add(node, "Exposure.uncertaintyEstimation", exposure.getUncertaintyEstimation());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final ModelMath modelMath) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (modelMath.eIsSet(pkg.getModelMath_QualityMeasures())) {
			add(node, "ModelMath.qualityMeasures", modelMath.getQualityMeasures());
		}

		if (modelMath.eIsSet(pkg.getModelMath_FittingProcedure())) {
			add(node, "ModelMath.fittingProcedure", modelMath.getFittingProcedure());
		}

		if (modelMath.eIsSet(pkg.getModelMath_Event())) {
			add(node, "ModelMath.event", modelMath.getEvent());
		}

		// parameter
		if (modelMath.eIsSet(pkg.getModelMath_Parameter())) {
			String label = bundle.getString("ModelMath.parameter");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (Parameter param : modelMath.getParameter()) {
				DefaultMutableTreeNode parameterNode = new DefaultMutableTreeNode(label);
				add(parameterNode, param);
				parentNode.add(parameterNode);
			}
			node.add(parentNode);
		}

		// model equation
		if (modelMath.eIsSet(pkg.getModelMath_ModelEquation())) {
			String label = bundle.getString("ModelMath.modelEquation");
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (ModelEquation modelEquation : modelMath.getModelEquation()) {
				DefaultMutableTreeNode modelEquationNode = new DefaultMutableTreeNode(label);
				add(modelEquationNode, modelEquation);
				parentNode.add(modelEquationNode);
			}
			node.add(parentNode);
		}

		// exposure
		if (modelMath.eIsSet(pkg.getModelMath_Exposure())) {
			String label = bundle.getString("ModelMath.exposure");
			DefaultMutableTreeNode exposureNode = new DefaultMutableTreeNode(label);
			add(exposureNode, modelMath.getExposure());
			node.add(exposureNode);
		}
	}
}
