package metadata;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class MetadataTree {

	private static MetadataTreeBundle bundle = new MetadataTreeBundle();

	public static JTree createTree(GeneralInformation generalInformation, Scope scope, DataBackground dataBackground,
			ModelMath modelMath) {

		DefaultMutableTreeNode generalInformationNode = new DefaultMutableTreeNode(bundle.getGeneralInformation());
		if (generalInformation != null) {
			add(generalInformationNode, generalInformation);
		}

		DefaultMutableTreeNode scopeNode = new DefaultMutableTreeNode(bundle.getScope());
		if (scope != null) {
			add(scopeNode, scope);
		}

		DefaultMutableTreeNode dataBackgroundNode = new DefaultMutableTreeNode(bundle.getDataBackground());
		if (dataBackground != null) {
			add(dataBackgroundNode, dataBackground);
		}

		DefaultMutableTreeNode modelMathNode = new DefaultMutableTreeNode(bundle.getModelMath());
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

	private static void add(final DefaultMutableTreeNode node, final Contact contact) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (contact.eIsSet(pkg.getContact_Title())) {
			String msg = bundle.getContact_title() + ": " + contact.getTitle();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_FamilyName())) {
			String msg = bundle.getContact_familyName() + ": " + contact.getFamilyName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_GivenName())) {
			String msg = bundle.getContact_givenName() + ": " + contact.getGivenName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_Email())) {
			String msg = bundle.getContact_email() + ": " + contact.getEmail();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_Telephone())) {
			String msg = bundle.getContact_telephone();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_StreetAddress())) {
			String msg = bundle.getContact_streetAddress() + ": " + contact.getStreetAddress();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_Country())) {
			String msg = bundle.getContact_country() + ": " + contact.getCountry();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_City())) {
			String msg = bundle.getContact_city() + ": " + contact.getCity();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_ZipCode())) {
			String msg = bundle.getContact_zipCode() + ": " + contact.getZipCode();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_Region())) {
			String msg = bundle.getContact_region() + ": " + contact.getRegion();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_TimeZone())) {
			String msg = bundle.getContact_timeZone() + ": " + contact.getTimeZone();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_Gender())) {
			String msg = bundle.getContact_gender() + ": " + contact.getGender();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_Note())) {
			String msg = bundle.getContact_note() + ": " + contact.getNote();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (contact.eIsSet(pkg.getContact_Organization())) {
			String msg = bundle.getContact_organization() + ": " + contact.getOrganization();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final ModelCategory modelCategory) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (modelCategory.eIsSet(pkg.getModelCategory_ModelClass())) {
			String msg = bundle.getModelCategory_modelClass() + ": " + modelCategory.getModelClass();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (modelCategory.eIsSet(pkg.getModelCategory_ModelSubClass())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getModelCategory_modelSubClass());
			modelCategory.getModelSubClass().stream().map(DefaultMutableTreeNode::new).forEach(parentNode::add);
		}

		if (modelCategory.eIsSet(pkg.getModelCategory_ModelClassComment())) {
			String msg = bundle.getModelCategory_modelClassComment() + ": " + modelCategory.getModelClassComment();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (modelCategory.eIsSet(pkg.getModelCategory_BasicProcess())) {
			String msg = bundle.getModelCategory_basicProcess() + ": " + modelCategory.getBasicProcess();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Reference reference) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (reference.eIsSet(pkg.getReference_IsReferenceDescription())) {
			String msg = bundle.getReference_isReferenceDescription() + ": " + reference.isIsReferenceDescription();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationType())) {
			String msg = bundle.getReference_publicationType() + ": " + reference.getPublicationType();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationDate())) {
			String msg = bundle.getReference_publicationDate() + ": " + reference.getPublicationDate();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_Pmid())) {
			String msg = bundle.getReference_pmid() + ": " + reference.getPmid();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_Doi())) {
			String msg = bundle.getReference_doi() + ": " + reference.getDoi();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_AuthorList())) {
			String msg = bundle.getReference_authorList() + ": " + reference.getAuthorList();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationTitle())) {
			String msg = bundle.getReference_publicationTitle() + ": " + reference.getPublicationTitle();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationAbstract())) {
			String msg = bundle.getReference_publicationAbstract() + ": " + reference.getPublicationAbstract();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationJournal())) {
			String msg = bundle.getReference_publicationJournal() + ": " + reference.getPublicationJournal();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationVolume())) {
			String msg = bundle.getReference_publicationVolume() + ": " + reference.getPublicationVolume();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationIssue())) {
			String msg = bundle.getReference_publicationIssue() + ": " + reference.getPublicationIssue();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationStatus())) {
			String msg = bundle.getReference_publicationStatus() + ": " + reference.getPublicationStatus();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_PublicationWebsite())) {
			String msg = bundle.getReference_publicationWebsite() + ": " + reference.getPublicationWebsite();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (reference.eIsSet(pkg.getReference_Comment())) {
			String msg = bundle.getReference_comment() + ": " + reference.getComment();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final GeneralInformation generalInformation) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Name())) {
			String msg = bundle.getGeneralInformation_name() + ": " + generalInformation.getName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Source())) {
			String msg = bundle.getGeneralInformation_source() + ": " + generalInformation.getSource();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Identifier())) {
			String msg = bundle.getGeneralInformation_identifier() + ": " + generalInformation.getIdentifier();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_CreationDate())) {
			String msg = bundle.getGeneralInformation_creationDate() + ": " + generalInformation.getCreationDate();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Rights())) {
			String msg = bundle.getGeneralInformation_rights() + ": " + generalInformation.getRights();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Available())) {
			String msg = bundle.getGeneralInformation_available() + ": " + generalInformation.isAvailable();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Format())) {
			String msg = bundle.getGeneralInformation_format() + ": " + generalInformation.getFormat();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Language())) {
			String msg = bundle.getGeneralInformation_language() + ": " + generalInformation.getLanguage();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Software())) {
			String msg = bundle.getGeneralInformation_software() + ": " + generalInformation.getSoftware();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_LanguageWrittenIn())) {
			String msg = bundle.getGeneralInformation_languageWrittenIn() + ": "
					+ generalInformation.getLanguageWrittenIn();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Status())) {
			String msg = bundle.getGeneralInformation_status() + ": " + generalInformation.getStatus();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Objective())) {
			String msg = bundle.getGeneralInformation_objective() + ": " + generalInformation.getObjective();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (generalInformation.eIsSet(pkg.getGeneralInformation_Description())) {
			String msg = bundle.getGeneralInformation_description() + ": " + generalInformation.getDescription();
			node.add(new DefaultMutableTreeNode(msg));
		}

		// author
		if (generalInformation.eIsSet(pkg.getGeneralInformation_Author())) {
			DefaultMutableTreeNode authorNode = new DefaultMutableTreeNode(bundle.getGeneralInformation_author());
			add(authorNode, generalInformation.getAuthor());
			node.add(authorNode);
		}

		// creators
		if (generalInformation.eIsSet(pkg.getGeneralInformation_Creators())) {
			String label = bundle.getGeneralInformation_creators();
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

			String label = bundle.getGeneralInformation_modelCategory();
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

			String label = bundle.getGeneralInformation_references();
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
			String label = bundle.getGeneralInformation_modificationDates();
			DefaultMutableTreeNode modificationDatesNode = new DefaultMutableTreeNode(label);
			generalInformation.getModificationdate().stream().map(ModificationDate::getValue)
					.map(DefaultMutableTreeNode::new).forEach(modificationDatesNode::add);
			node.add(modificationDatesNode);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Product product) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (product.eIsSet(pkg.getProduct_ProductName())) {
			String msg = bundle.getProduct_productName() + ": " + product.getProductName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_ProductDescription())) {
			String msg = bundle.getProduct_productDescription() + ": " + product.getProductDescription();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_ProductUnit())) {
			String msg = bundle.getProduct_productUnit() + ": " + product.getProductUnit();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_ProductionMethod())) {
			String msg = bundle.getProduct_productionMethod() + ": " + product.getProductionMethod();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_Packaging())) {
			String msg = bundle.getProduct_packaging() + ": " + product.getPackaging();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_ProductTreatment())) {
			String msg = bundle.getProduct_productTreatment() + ": " + product.getProductTreatment();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_OriginCountry())) {
			String msg = bundle.getProduct_originCountry() + ": " + product.getOriginCountry();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_OriginArea())) {
			String msg = bundle.getProduct_originArea() + ": " + product.getOriginArea();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_FisheriesArea())) {
			String msg = bundle.getProduct_fisheriesArea() + ": " + product.getFisheriesArea();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_ProductionDate())) {
			String msg = bundle.getProduct_productionDate() + ": " + product.getProductionDate();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (product.eIsSet(pkg.getProduct_ExpiryDate())) {
			String msg = bundle.getProduct_expiryDate() + ": " + product.getExpiryDate();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Hazard hazard) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (hazard.eIsSet(pkg.getHazard_HazardType())) {
			String msg = bundle.getHazard_hazardType() + ": " + hazard.getHazardType();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_HazardName())) {
			String msg = bundle.getHazard_hazardName() + ": " + hazard.getHazardName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_HazardDescription())) {
			String msg = bundle.getHazard_hazardDescription() + ": " + hazard.getHazardDescription();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_HazardUnit())) {
			String msg = bundle.getHazard_hazardUnit() + ": " + hazard.getHazardUnit();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_AdverseEffect())) {
			String msg = bundle.getHazard_adverseEffect() + ": " + hazard.getAdverseEffect();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_SourceOfContamination())) {
			String msg = bundle.getHazard_sourceOfContamination() + ": " + hazard.getSourceOfContamination();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_BenchmarkDose())) {
			String msg = bundle.getHazard_benchmarkDose() + ": " + hazard.getBenchmarkDose();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_MaximumResidueLimit())) {
			String msg = bundle.getHazard_maximumResidueLimit() + ": " + hazard.getMaximumResidueLimit();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_NoObservedAdverseAffectLevel())) {
			String msg = bundle.getHazard_noObservedAdverseAffectLevel() + ": "
					+ hazard.getNoObservedAdverseAffectLevel();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_AcceptableDailyIntake())) {
			String msg = bundle.getHazard_acceptableDailyIntake() + ": " + hazard.getAcceptableDailyIntake();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_AcuteReferenceDose())) {
			String msg = bundle.getHazard_acuteReferenceDose() + ": " + hazard.getAcuteReferenceDose();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (hazard.eIsSet(pkg.getHazard_HazardIndSum())) {
			String msg = bundle.getHazard_hazardIndSum() + ": " + hazard.getHazardIndSum();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final PopulationGroup populationGroup) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationName())) {
			String msg = bundle.getPopulationGroup_populationName() + ": " + populationGroup.getPopulationName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_TargetPopulation())) {
			String msg = bundle.getPopulationGroup_targetPopulation() + ": " + populationGroup.getTargetPopulation();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationSpan())) {
			String msg = bundle.getPopulationGroup_populationSpan() + ": " + populationGroup.getPopulationSpan();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationDescription())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
					bundle.getPopulationGroup_populationDescription());
			populationGroup.getPopulationDescription().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new)
					.forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationAge())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getPopulationGroup_populationAge());
			populationGroup.getPopulationAge().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationGender())) {
			String msg = bundle.getPopulationGroup_populationGender() + ": " + populationGroup.getPopulationGender();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_Bmi())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getPopulationGroup_bmi());
			populationGroup.getBmi().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_SpecialDietGroups())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
					bundle.getPopulationGroup_specialDietGroups());
			populationGroup.getSpecialDietGroups().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PatternConsumption())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
					bundle.getPopulationGroup_patternConsumption());
			populationGroup.getPatternConsumption().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_Region())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getPopulationGroup_region());
			populationGroup.getRegion().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_Country())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getPopulationGroup_country());
			populationGroup.getCountry().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_PopulationRiskFactor())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
					bundle.getPopulationGroup_populationRiskFactor());
			populationGroup.getPopulationRiskFactor().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new)
					.forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (populationGroup.eIsSet(pkg.getPopulationGroup_Season())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getPopulationGroup_season());
			populationGroup.getSeason().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final SpatialInformation spatialInformation) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (spatialInformation.eIsSet(pkg.getSpatialInformation_Region())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getSpatialInformation_region());
			spatialInformation.getRegion().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (spatialInformation.eIsSet(pkg.getSpatialInformation_Country())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getSpatialInformation_country());
			spatialInformation.getCountry().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Scope scope) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (scope.eIsSet(pkg.getScope_GeneralComment())) {
			String msg = bundle.getScope_generalComment() + ": " + scope.getGeneralComment();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (scope.eIsSet(pkg.getScope_TemporalInformation())) {
			String msg = bundle.getScope_temporalInformation() + ": " + scope.getTemporalInformation();
			node.add(new DefaultMutableTreeNode(msg));
		}

		// product
		if (scope.eIsSet(pkg.getScope_Product())) {
			String label = bundle.getScope_product();
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
			String label = bundle.getScope_hazard();
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
			String label = bundle.getScope_populationGroup();
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
			DefaultMutableTreeNode spatialInformationNode = new DefaultMutableTreeNode(
					bundle.getScope_spatialInformation());
			add(spatialInformationNode, scope.getSpatialInformation());
			node.add(spatialInformationNode);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Study study) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (study.eIsSet(pkg.getStudy_StudyIdentifier())) {
			String msg = bundle.getStudy_studyIdentifier() + ": " + study.getStudyIdentifier();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyTitle())) {
			String msg = bundle.getStudy_studyTitle() + ": " + study.getStudyTitle();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyDescription())) {
			String msg = bundle.getStudy_studyDescription() + ": " + study.getStudyDescription();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyDesignType())) {
			String msg = bundle.getStudy_studyDesignType() + ": " + study.getStudyDesignType();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyAssayMeasurementType())) {
			String msg = bundle.getStudy_studyAssayMeasurementType() + ": " + study.getStudyAssayMeasurementType();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyAssayTechnologyType())) {
			String msg = bundle.getStudy_studyAssayTechnologyType() + ": " + study.getStudyAssayTechnologyType();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyAssayTechnologyPlatform())) {
			String msg = bundle.getStudy_studyAssayTechnologyPlatform() + ": "
					+ study.getStudyAssayTechnologyPlatform();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_AccreditationProcedureForTheAssayTechnology())) {
			String msg = bundle.getStudy_accreditationProcedureForTheAssayTechnology() + ": "
					+ study.getAccreditationProcedureForTheAssayTechnology();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolName())) {
			String msg = bundle.getStudy_studyProtocolName() + ": " + study.getStudyProtocolName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolType())) {
			String msg = bundle.getStudy_studyProtocolType() + ": " + study.getStudyProtocolName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolDescription())) {
			String msg = bundle.getStudy_studyProtocolDescription() + ": " + study.getStudyProtocolDescription();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolURI())) {
			String msg = bundle.getStudy_studyProtocolURI() + ": " + study.getStudyProtocolURI();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolVersion())) {
			String msg = bundle.getStudy_studyProtocolVersion() + ": " + study.getStudyProtocolVersion();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolComponentsName())) {
			String msg = bundle.getStudy_studyProtocolComponentsName() + ": " + study.getStudyProtocolComponentsName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (study.eIsSet(pkg.getStudy_StudyProtocolComponentsType())) {
			String msg = bundle.getStudy_studyProtocolComponentsType() + ": " + study.getStudyProtocolComponentsType();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final StudySample studySample) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (studySample.eIsSet(pkg.getStudySample_SampleName())) {
			String msg = bundle.getStudySample_sampleName() + ": " + studySample.getSampleName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_ProtocolOfSampleCollection())) {
			String msg = bundle.getStudySample_protocolOfSampleCollection() + ": "
					+ studySample.getProtocolOfSampleCollection();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingStrategy())) {
			String msg = bundle.getStudySample_samplingStrategy() + ": " + studySample.getSamplingStrategy();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_TypeOfSamplingProgram())) {
			String msg = bundle.getStudySample_typeOfSamplingProgram() + ": " + studySample.getTypeOfSamplingProgram();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingMethod())) {
			String msg = bundle.getStudySample_samplingMethod() + ": " + studySample.getSamplingMethod();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingPlan())) {
			String msg = bundle.getStudySample_samplingPlan() + ": " + studySample.getSamplingPlan();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingWeight())) {
			String msg = bundle.getStudySample_samplingWeight() + ": " + studySample.getSamplingWeight();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingSize())) {
			String msg = bundle.getStudySample_samplingSize() + ": " + studySample.getSamplingSize();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_LotSizeUnit())) {
			String msg = bundle.getStudySample_lotSizeUnit() + ": " + studySample.getLotSizeUnit();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (studySample.eIsSet(pkg.getStudySample_SamplingPoint())) {
			String msg = bundle.getStudySample_samplingPoint() + ": " + studySample.getSamplingPoint();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final DietaryAssessmentMethod method) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (method.eIsSet(pkg.getDietaryAssessmentMethod_CollectionTool())) {
			String msg = bundle.getDietaryAssessmentMethod_collectionTool() + ": " + method.getCollectionTool();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (method.eIsSet(pkg.getDietaryAssessmentMethod_NumberOfNonConsecutiveOneDay())) {
			String msg = bundle.getDietaryAssessmentMethod_numberOfNonConsecutiveOneDay() + ": "
					+ method.getNumberOfNonConsecutiveOneDay();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (method.eIsSet(pkg.getDietaryAssessmentMethod_SoftwareTool())) {
			String msg = bundle.getDietaryAssessmentMethod_softwareTool() + ": " + method.getSoftwareTool();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (method.eIsSet(pkg.getDietaryAssessmentMethod_NumberOfFoodItems())) {
			String msg = bundle.getDietaryAssessmentMethod_numberOfFoodItems() + ": " + method.getNumberOfFoodItems();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (method.eIsSet(pkg.getDietaryAssessmentMethod_RecordTypes())) {
			String msg = bundle.getDietaryAssessmentMethod_recordTypes() + ": " + method.getRecordTypes();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (method.eIsSet(pkg.getDietaryAssessmentMethod_FoodDescriptors())) {
			String msg = bundle.getDietaryAssessmentMethod_foodDescriptors() + ": " + method.getFoodDescriptors();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Laboratory laboratory) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryAccreditation())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
					bundle.getLaboratory_laboratoryAccreditation());
			laboratory.getLaboratoryAccreditation().stream().map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(new DefaultMutableTreeNode(parentNode));
		}

		if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryName())) {
			String msg = bundle.getLaboratory_laboratoryName() + ": " + laboratory.getLaboratoryName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryCountry())) {
			String msg = bundle.getLaboratory_laboratoryCountry() + ": " + laboratory.getLaboratoryCountry();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Assay assay) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (assay.eIsSet(pkg.getAssay_AssayName())) {
			String msg = bundle.getAssay_assayName() + ": " + assay.getAssayName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (assay.eIsSet(pkg.getAssay_AssayDescription())) {
			String msg = bundle.getAssay_assayDescription() + ": " + assay.getAssayDescription();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (assay.eIsSet(pkg.getAssay_PercentageOfMoisture())) {
			String msg = bundle.getAssay_percentageOfMoisture() + ": " + assay.getPercentageOfMoisture();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (assay.eIsSet(pkg.getAssay_PercentageOfFat())) {
			String msg = bundle.getAssay_percentageOfFat() + ": " + assay.getPercentageOfFat();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (assay.eIsSet(pkg.getAssay_LimitOfDetection())) {
			String msg = bundle.getAssay_limitOfDetection() + ": " + assay.getLimitOfDetection();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (assay.eIsSet(pkg.getAssay_LimitOfQuantification())) {
			String msg = bundle.getAssay_limitOfQuantification() + ": " + assay.getLimitOfQuantification();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (assay.eIsSet(pkg.getAssay_LeftCensoredData())) {
			String msg = bundle.getAssay_leftCensoredData() + ": " + assay.getLeftCensoredData();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (assay.eIsSet(pkg.getAssay_RangeOfContamination())) {
			String msg = bundle.getAssay_rangeOfContamination() + ": " + assay.getRangeOfContamination();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (assay.eIsSet(pkg.getAssay_UncertaintyValue())) {
			String msg = bundle.getAssay_uncertaintyValue() + ": " + assay.getUncertaintyValue();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final DataBackground dataBackground) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		// study
		if (dataBackground.eIsSet(pkg.getDataBackground_Study())) {
			DefaultMutableTreeNode studyNode = new DefaultMutableTreeNode(bundle.getDataBackground_study());
			add(studyNode, dataBackground.getStudy());
			node.add(studyNode);
		}

		// study sample
		if (dataBackground.eIsSet(pkg.getDataBackground_StudySample())) {
			String label = bundle.getDataBackground_studySample();
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (StudySample studySample : dataBackground.getStudySample()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, studySample);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		// dietary assessment method
		if (dataBackground.eIsSet(pkg.getDataBackground_DietaryAssessmentMethod())) {
			String label = bundle.getDataBackground_dietaryAssessmentMethod();
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			for (DietaryAssessmentMethod method : dataBackground.getDietaryAssessmentMethod()) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(label);
				add(childNode, method);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		// laboratory
		if (dataBackground.eIsSet(pkg.getDataBackground_Laboratory())) {
			String label = bundle.getDataBackground_laboratory();
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
			String label = bundle.getDataBackground_assay();
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
			String msg = bundle.getParameter_parameterId() + ": " + parameter.getParameterID();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterClassification())) {
			String msg = bundle.getParameter_parameterClassification() + ": "
					+ parameter.getParameterClassification().name();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterName())) {
			String msg = bundle.getParameter_parameterName() + ": " + parameter.getParameterName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterDescription())) {
			String msg = bundle.getParameter_parameterDescription() + ": " + parameter.getParameterDescription();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterType())) {
			String msg = bundle.getParameter_parameterType() + ": " + parameter.getParameterType();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterUnit())) {
			String msg = bundle.getParameter_parameterUnit() + ": " + parameter.getParameterUnit();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterUnitCategory())) {
			String msg = bundle.getParameter_parameterUnitCategory() + ": " + parameter.getParameterUnitCategory();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterDataType())) {
			String msg = bundle.getParameter_parameterDataType() + ": " + parameter.getParameterDataType().getLiteral();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterSource())) {
			String msg = bundle.getParameter_parameterSource() + ": " + parameter.getParameterSource();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterSubject())) {
			String msg = bundle.getParameter_parameterSubject() + ": " + parameter.getParameterSubject();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterDistribution())) {
			String msg = bundle.getParameter_parameterDistribution() + ": " + parameter.getParameterDistribution();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterValue())) {
			String msg = bundle.getParameter_parameterDistribution() + ": " + parameter.getParameterDistribution();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterVariabilitySubject())) {
			String msg = bundle.getParameter_parameterVariabilitySubject() + ": "
					+ parameter.getParameterVariabilitySubject();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterValueMin())) {
			String msg = bundle.getParameter_parameterValueMin() + ": " + parameter.getParameterValueMin();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterValueMax())) {
			String msg = bundle.getParameter_parameterValueMax() + ": " + parameter.getParameterValueMax();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (parameter.eIsSet(pkg.getParameter_ParameterError())) {
			String msg = bundle.getParameter_parameterError() + ": " + parameter.getParameterError();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final ModelEquation modelEquation) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (modelEquation.eIsSet(pkg.getModelEquation_ModelEquationName())) {
			String msg = bundle.getModelEquation_modelEquationName() + ": " + modelEquation.getModelEquationName();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (modelEquation.eIsSet(pkg.getModelEquation_ModelEquationClass())) {
			String msg = bundle.getModelEquation_modelEquationClass() + ": " + modelEquation.getModelEquationClass();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (modelEquation.eIsSet(pkg.getModelEquation_ModelEquation())) {
			String msg = bundle.getModelEquation_modelEquation() + ": " + modelEquation.getModelEquation();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (modelEquation.eIsSet(pkg.getModelEquation_HypothesisOfTheModel())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
					bundle.getModelEquation_hypothesisOfTheModel());
			modelEquation.getHypothesisOfTheModel().stream().map(StringObject::getValue)
					.map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(parentNode);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Exposure exposure) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (exposure.eIsSet(pkg.getExposure_MethodologicalTreatmentOfLeftCensoredData())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
					bundle.getExposure_methodologicalTreatmentOfLeftCensoredData());
			exposure.getMethodologicalTreatmentOfLeftCensoredData().stream().map(StringObject::getValue)
					.map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(parentNode);
		}

		if (exposure.eIsSet(pkg.getExposure_LevelOfContaminationAfterLeftCensoredDataTreatment())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
					bundle.getExposure_levelOfContaminationAfterLeftCensoredDataTreatment());
			exposure.getLevelOfContaminationAfterLeftCensoredDataTreatment().stream().map(StringObject::getValue)
					.map(DefaultMutableTreeNode::new).forEach(parentNode::add);
			node.add(parentNode);
		}

		if (exposure.eIsSet(pkg.getExposure_TypeOfExposure())) {
			String msg = bundle.getExposure_typeOfExposure() + ": " + exposure.getTypeOfExposure();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (exposure.eIsSet(pkg.getExposure_Scenario())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getExposure_scenario());
			exposure.getScenario().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new)
					.forEach(parentNode::add);
			node.add(parentNode);
		}

		if (exposure.eIsSet(pkg.getExposure_UncertaintyEstimation())) {
			String msg = bundle.getExposure_uncertaintyEstimation() + ": " + exposure.getUncertaintyEstimation();
			node.add(new DefaultMutableTreeNode(msg));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final ModelMath modelMath) {

		MetadataPackage pkg = MetadataPackage.eINSTANCE;

		if (modelMath.eIsSet(pkg.getModelMath_QualityMeasures())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getModelMath_qualityMeasures());
			modelMath.getQualityMeasures().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new)
					.forEach(parentNode::add);
			node.add(parentNode);
		}

		if (modelMath.eIsSet(pkg.getModelMath_FittingProcedure())) {
			String msg = bundle.getModelMath_fittingProcedure() + ": " + modelMath.getFittingProcedure();
			node.add(new DefaultMutableTreeNode(msg));
		}

		if (modelMath.eIsSet(pkg.getModelMath_Event())) {
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(bundle.getModelMath_event());
			modelMath.getEvent().stream().map(StringObject::getValue).map(DefaultMutableTreeNode::new)
					.forEach(parentNode::add);
			node.add(parentNode);
		}

		// parameter
		if (modelMath.eIsSet(pkg.getModelMath_Parameter())) {
			String label = bundle.getModelMath_parameter();
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
			String label = bundle.getModelMath_modelEquation();
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
			DefaultMutableTreeNode exposureNode = new DefaultMutableTreeNode(bundle.getModelMath_exposure());
			add(exposureNode, modelMath.getExposure());
			node.add(exposureNode);
		}
	}
}
