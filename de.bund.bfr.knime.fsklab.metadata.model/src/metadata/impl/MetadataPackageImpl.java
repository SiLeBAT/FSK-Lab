/**
 */
package metadata.impl;

import metadata.Assay;
import metadata.Contact;
import metadata.DataBackground;
import metadata.DietaryAssessmentMethod;
import metadata.Event;
import metadata.Exposure;
import metadata.GeneralInformation;
import metadata.Hazard;
import metadata.Laboratory;
import metadata.MetadataFactory;
import metadata.MetadataPackage;
import metadata.ModelCategory;
import metadata.ModelEquation;
import metadata.ModelMath;
import metadata.ModificationDate;
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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MetadataPackageImpl extends EPackageImpl implements MetadataPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass generalInformationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelCategoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass assayEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass studyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataBackgroundEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass studySampleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dietaryAssessmentMethodEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modificationDateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass productEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass hazardEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass populationGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scopeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass laboratoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass spatialInformationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contactEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelMathEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelEquationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass exposureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stringObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum publicationTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterClassificationEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType uriEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see metadata.MetadataPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MetadataPackageImpl() {
		super(eNS_URI, MetadataFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MetadataPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MetadataPackage init() {
		if (isInited) return (MetadataPackage)EPackage.Registry.INSTANCE.getEPackage(MetadataPackage.eNS_URI);

		// Obtain or create and register package
		MetadataPackageImpl theMetadataPackage = (MetadataPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MetadataPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MetadataPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theMetadataPackage.createPackageContents();

		// Initialize created meta-data
		theMetadataPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMetadataPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MetadataPackage.eNS_URI, theMetadataPackage);
		return theMetadataPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeneralInformation() {
		return generalInformationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Name() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Source() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Identifier() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_CreationDate() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Rights() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Available() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Format() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Language() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Software() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_LanguageWrittenIn() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Status() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Objective() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeneralInformation_Description() {
		return (EAttribute)generalInformationEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeneralInformation_ModelCategory() {
		return (EReference)generalInformationEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeneralInformation_Modificationdate() {
		return (EReference)generalInformationEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeneralInformation_Author() {
		return (EReference)generalInformationEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeneralInformation_Creators() {
		return (EReference)generalInformationEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeneralInformation_Reference() {
		return (EReference)generalInformationEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelCategory() {
		return modelCategoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModelCategory_ModelClass() {
		return (EAttribute)modelCategoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModelCategory_ModelClassComment() {
		return (EAttribute)modelCategoryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModelCategory_BasicProcess() {
		return (EAttribute)modelCategoryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelCategory_ModelSubClass() {
		return (EReference)modelCategoryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAssay() {
		return assayEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_AssayName() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_AssayDescription() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_PercentageOfMoisture() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_PercentageOfFat() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_LimitOfDetection() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_LimitOfQuantification() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_LeftCensoredData() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_RangeOfContamination() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssay_UncertaintyValue() {
		return (EAttribute)assayEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStudy() {
		return studyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyIdentifier() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyTitle() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyDescription() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyDesignType() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyAssayMeasurementType() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyAssayTechnologyType() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyAssayTechnologyPlatform() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_AccreditationProcedureForTheAssayTechnology() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyProtocolName() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyProtocolType() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyProtocolDescription() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyProtocolURI() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyProtocolVersion() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyProtocolParametersName() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyProtocolComponentsName() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudy_StudyProtocolComponentsType() {
		return (EAttribute)studyEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataBackground() {
		return dataBackgroundEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataBackground_Study() {
		return (EReference)dataBackgroundEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataBackground_Studysample() {
		return (EReference)dataBackgroundEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataBackground_Dietaryassessmentmethod() {
		return (EReference)dataBackgroundEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataBackground_Laboratory() {
		return (EReference)dataBackgroundEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataBackground_Assay() {
		return (EReference)dataBackgroundEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStudySample() {
		return studySampleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_SampleName() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_ProtocolOfSampleCollection() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_SamplingStrategy() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_TypeOfSamplingProgram() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_SamplingMethod() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_SamplingPlan() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_SamplingWeight() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_SamplingSize() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_LotSizeUnit() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStudySample_SamplingPoint() {
		return (EAttribute)studySampleEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDietaryAssessmentMethod() {
		return dietaryAssessmentMethodEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDietaryAssessmentMethod_CollectionTool() {
		return (EAttribute)dietaryAssessmentMethodEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDietaryAssessmentMethod_NumberOfNonConsecutiveOneDay() {
		return (EAttribute)dietaryAssessmentMethodEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDietaryAssessmentMethod_SoftwareTool() {
		return (EAttribute)dietaryAssessmentMethodEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDietaryAssessmentMethod_NumberOfFoodItems() {
		return (EAttribute)dietaryAssessmentMethodEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDietaryAssessmentMethod_RecordTypes() {
		return (EAttribute)dietaryAssessmentMethodEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDietaryAssessmentMethod_FoodDescriptors() {
		return (EAttribute)dietaryAssessmentMethodEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModificationDate() {
		return modificationDateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModificationDate_Value() {
		return (EAttribute)modificationDateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProduct() {
		return productEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_ProductName() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_ProductDescription() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_ProductUnit() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_ProductionMethod() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_Packaging() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_ProductTreatment() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_OriginCountry() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_OriginArea() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_FisheriesArea() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_ProductionDate() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProduct_ExpiryDate() {
		return (EAttribute)productEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHazard() {
		return hazardEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_HazardType() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_HazardName() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_HazardDescription() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_HazardUnit() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_AdverseEffect() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_SourceOfContamination() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_BenchmarkDose() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_MaximumResidueLimit() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_NoObservedAdverseAffectLevel() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_LowestObservedAdverseAffectLevel() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_AcceptableOperatorExposureLevel() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_AcuteReferenceDose() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_AcceptableDailyIntake() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHazard_HazardIndSum() {
		return (EAttribute)hazardEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPopulationGroup() {
		return populationGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPopulationGroup_PopulationName() {
		return (EAttribute)populationGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPopulationGroup_TargetPopulation() {
		return (EAttribute)populationGroupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_PopulationSpan() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_PopulationDescription() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_Bmi() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_SpecialDietGroups() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_Region() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_Country() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_PopulationRiskFactor() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_Season() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPopulationGroup_PopulationGender() {
		return (EAttribute)populationGroupEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_PatternConsumption() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPopulationGroup_PopulationAge() {
		return (EReference)populationGroupEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScope() {
		return scopeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScope_GeneralComment() {
		return (EAttribute)scopeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getScope_TemporalInformation() {
		return (EAttribute)scopeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScope_Product() {
		return (EReference)scopeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScope_Hazard() {
		return (EReference)scopeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScope_PopulationGroup() {
		return (EReference)scopeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScope_SpatialInformation() {
		return (EReference)scopeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLaboratory() {
		return laboratoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLaboratory_LaboratoryName() {
		return (EAttribute)laboratoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLaboratory_LaboratoryCountry() {
		return (EAttribute)laboratoryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLaboratory_LaboratoryAccreditation() {
		return (EReference)laboratoryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSpatialInformation() {
		return spatialInformationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpatialInformation_Region() {
		return (EReference)spatialInformationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSpatialInformation_Country() {
		return (EReference)spatialInformationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEvent() {
		return eventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_Event() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getContact() {
		return contactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_Title() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_FamilyName() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_GivenName() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_Email() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_Telephone() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_StreetAddress() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_Country() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_City() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_ZipCode() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_Region() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_TimeZone() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_Gender() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_Note() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getContact_Organization() {
		return (EAttribute)contactEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReference() {
		return referenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_IsReferenceDescription() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationType() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationDate() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_Pmid() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_Doi() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_AuthorList() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationTitle() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationAbstract() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationJournal() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationVolume() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationIssue() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationStatus() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_PublicationWebsite() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReference_Comment() {
		return (EAttribute)referenceEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelMath() {
		return modelMathEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModelMath_FittingProcedure() {
		return (EAttribute)modelMathEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelMath_Parameter() {
		return (EReference)modelMathEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelMath_ModelEquation() {
		return (EReference)modelMathEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelMath_Exposure() {
		return (EReference)modelMathEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelMath_QualityMeasures() {
		return (EReference)modelMathEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelMath_Event() {
		return (EReference)modelMathEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameter() {
		return parameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterID() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterClassification() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterName() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterDescription() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterType() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterUnit() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterUnitCategory() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterDataType() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterSource() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterSubject() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterDistribution() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterValue() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterVariabilitySubject() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterValueMin() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterValueMax() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_ParameterError() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameter_Reference() {
		return (EReference)parameterEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelEquation() {
		return modelEquationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModelEquation_ModelEquationName() {
		return (EAttribute)modelEquationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModelEquation_ModelEquationClass() {
		return (EAttribute)modelEquationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getModelEquation_ModelEquation() {
		return (EAttribute)modelEquationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelEquation_Reference() {
		return (EReference)modelEquationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModelEquation_HypothesisOfTheModel() {
		return (EReference)modelEquationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExposure() {
		return exposureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposure_TypeOfExposure() {
		return (EAttribute)exposureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExposure_UncertaintyEstimation() {
		return (EAttribute)exposureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExposure_MethodologicalTreatmentOfLeftCensoredData() {
		return (EReference)exposureEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExposure_LevelOfContaminationAfterLeftCensoredDataTreatment() {
		return (EReference)exposureEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExposure_Scenario() {
		return (EReference)exposureEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStringObject() {
		return stringObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringObject_Value() {
		return (EAttribute)stringObjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getPublicationType() {
		return publicationTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getParameterClassification() {
		return parameterClassificationEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getParameterType() {
		return parameterTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getURI() {
		return uriEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetadataFactory getMetadataFactory() {
		return (MetadataFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		generalInformationEClass = createEClass(GENERAL_INFORMATION);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__NAME);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__SOURCE);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__IDENTIFIER);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__CREATION_DATE);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__RIGHTS);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__AVAILABLE);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__FORMAT);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__LANGUAGE);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__SOFTWARE);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__STATUS);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__OBJECTIVE);
		createEAttribute(generalInformationEClass, GENERAL_INFORMATION__DESCRIPTION);
		createEReference(generalInformationEClass, GENERAL_INFORMATION__MODEL_CATEGORY);
		createEReference(generalInformationEClass, GENERAL_INFORMATION__MODIFICATIONDATE);
		createEReference(generalInformationEClass, GENERAL_INFORMATION__AUTHOR);
		createEReference(generalInformationEClass, GENERAL_INFORMATION__CREATORS);
		createEReference(generalInformationEClass, GENERAL_INFORMATION__REFERENCE);

		modelCategoryEClass = createEClass(MODEL_CATEGORY);
		createEAttribute(modelCategoryEClass, MODEL_CATEGORY__MODEL_CLASS);
		createEAttribute(modelCategoryEClass, MODEL_CATEGORY__MODEL_CLASS_COMMENT);
		createEAttribute(modelCategoryEClass, MODEL_CATEGORY__BASIC_PROCESS);
		createEReference(modelCategoryEClass, MODEL_CATEGORY__MODEL_SUB_CLASS);

		assayEClass = createEClass(ASSAY);
		createEAttribute(assayEClass, ASSAY__ASSAY_NAME);
		createEAttribute(assayEClass, ASSAY__ASSAY_DESCRIPTION);
		createEAttribute(assayEClass, ASSAY__PERCENTAGE_OF_MOISTURE);
		createEAttribute(assayEClass, ASSAY__PERCENTAGE_OF_FAT);
		createEAttribute(assayEClass, ASSAY__LIMIT_OF_DETECTION);
		createEAttribute(assayEClass, ASSAY__LIMIT_OF_QUANTIFICATION);
		createEAttribute(assayEClass, ASSAY__LEFT_CENSORED_DATA);
		createEAttribute(assayEClass, ASSAY__RANGE_OF_CONTAMINATION);
		createEAttribute(assayEClass, ASSAY__UNCERTAINTY_VALUE);

		studyEClass = createEClass(STUDY);
		createEAttribute(studyEClass, STUDY__STUDY_IDENTIFIER);
		createEAttribute(studyEClass, STUDY__STUDY_TITLE);
		createEAttribute(studyEClass, STUDY__STUDY_DESCRIPTION);
		createEAttribute(studyEClass, STUDY__STUDY_DESIGN_TYPE);
		createEAttribute(studyEClass, STUDY__STUDY_ASSAY_MEASUREMENT_TYPE);
		createEAttribute(studyEClass, STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE);
		createEAttribute(studyEClass, STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM);
		createEAttribute(studyEClass, STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY);
		createEAttribute(studyEClass, STUDY__STUDY_PROTOCOL_NAME);
		createEAttribute(studyEClass, STUDY__STUDY_PROTOCOL_TYPE);
		createEAttribute(studyEClass, STUDY__STUDY_PROTOCOL_DESCRIPTION);
		createEAttribute(studyEClass, STUDY__STUDY_PROTOCOL_URI);
		createEAttribute(studyEClass, STUDY__STUDY_PROTOCOL_VERSION);
		createEAttribute(studyEClass, STUDY__STUDY_PROTOCOL_PARAMETERS_NAME);
		createEAttribute(studyEClass, STUDY__STUDY_PROTOCOL_COMPONENTS_NAME);
		createEAttribute(studyEClass, STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE);

		dataBackgroundEClass = createEClass(DATA_BACKGROUND);
		createEReference(dataBackgroundEClass, DATA_BACKGROUND__STUDY);
		createEReference(dataBackgroundEClass, DATA_BACKGROUND__STUDYSAMPLE);
		createEReference(dataBackgroundEClass, DATA_BACKGROUND__DIETARYASSESSMENTMETHOD);
		createEReference(dataBackgroundEClass, DATA_BACKGROUND__LABORATORY);
		createEReference(dataBackgroundEClass, DATA_BACKGROUND__ASSAY);

		studySampleEClass = createEClass(STUDY_SAMPLE);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__SAMPLE_NAME);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__PROTOCOL_OF_SAMPLE_COLLECTION);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__SAMPLING_STRATEGY);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__TYPE_OF_SAMPLING_PROGRAM);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__SAMPLING_METHOD);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__SAMPLING_PLAN);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__SAMPLING_WEIGHT);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__SAMPLING_SIZE);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__LOT_SIZE_UNIT);
		createEAttribute(studySampleEClass, STUDY_SAMPLE__SAMPLING_POINT);

		dietaryAssessmentMethodEClass = createEClass(DIETARY_ASSESSMENT_METHOD);
		createEAttribute(dietaryAssessmentMethodEClass, DIETARY_ASSESSMENT_METHOD__COLLECTION_TOOL);
		createEAttribute(dietaryAssessmentMethodEClass, DIETARY_ASSESSMENT_METHOD__NUMBER_OF_NON_CONSECUTIVE_ONE_DAY);
		createEAttribute(dietaryAssessmentMethodEClass, DIETARY_ASSESSMENT_METHOD__SOFTWARE_TOOL);
		createEAttribute(dietaryAssessmentMethodEClass, DIETARY_ASSESSMENT_METHOD__NUMBER_OF_FOOD_ITEMS);
		createEAttribute(dietaryAssessmentMethodEClass, DIETARY_ASSESSMENT_METHOD__RECORD_TYPES);
		createEAttribute(dietaryAssessmentMethodEClass, DIETARY_ASSESSMENT_METHOD__FOOD_DESCRIPTORS);

		modificationDateEClass = createEClass(MODIFICATION_DATE);
		createEAttribute(modificationDateEClass, MODIFICATION_DATE__VALUE);

		productEClass = createEClass(PRODUCT);
		createEAttribute(productEClass, PRODUCT__PRODUCT_NAME);
		createEAttribute(productEClass, PRODUCT__PRODUCT_DESCRIPTION);
		createEAttribute(productEClass, PRODUCT__PRODUCT_UNIT);
		createEAttribute(productEClass, PRODUCT__PRODUCTION_METHOD);
		createEAttribute(productEClass, PRODUCT__PACKAGING);
		createEAttribute(productEClass, PRODUCT__PRODUCT_TREATMENT);
		createEAttribute(productEClass, PRODUCT__ORIGIN_COUNTRY);
		createEAttribute(productEClass, PRODUCT__ORIGIN_AREA);
		createEAttribute(productEClass, PRODUCT__FISHERIES_AREA);
		createEAttribute(productEClass, PRODUCT__PRODUCTION_DATE);
		createEAttribute(productEClass, PRODUCT__EXPIRY_DATE);

		hazardEClass = createEClass(HAZARD);
		createEAttribute(hazardEClass, HAZARD__HAZARD_TYPE);
		createEAttribute(hazardEClass, HAZARD__HAZARD_NAME);
		createEAttribute(hazardEClass, HAZARD__HAZARD_DESCRIPTION);
		createEAttribute(hazardEClass, HAZARD__HAZARD_UNIT);
		createEAttribute(hazardEClass, HAZARD__ADVERSE_EFFECT);
		createEAttribute(hazardEClass, HAZARD__SOURCE_OF_CONTAMINATION);
		createEAttribute(hazardEClass, HAZARD__BENCHMARK_DOSE);
		createEAttribute(hazardEClass, HAZARD__MAXIMUM_RESIDUE_LIMIT);
		createEAttribute(hazardEClass, HAZARD__NO_OBSERVED_ADVERSE_AFFECT_LEVEL);
		createEAttribute(hazardEClass, HAZARD__LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL);
		createEAttribute(hazardEClass, HAZARD__ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL);
		createEAttribute(hazardEClass, HAZARD__ACUTE_REFERENCE_DOSE);
		createEAttribute(hazardEClass, HAZARD__ACCEPTABLE_DAILY_INTAKE);
		createEAttribute(hazardEClass, HAZARD__HAZARD_IND_SUM);

		populationGroupEClass = createEClass(POPULATION_GROUP);
		createEAttribute(populationGroupEClass, POPULATION_GROUP__POPULATION_NAME);
		createEAttribute(populationGroupEClass, POPULATION_GROUP__TARGET_POPULATION);
		createEReference(populationGroupEClass, POPULATION_GROUP__POPULATION_SPAN);
		createEReference(populationGroupEClass, POPULATION_GROUP__POPULATION_DESCRIPTION);
		createEReference(populationGroupEClass, POPULATION_GROUP__BMI);
		createEReference(populationGroupEClass, POPULATION_GROUP__SPECIAL_DIET_GROUPS);
		createEReference(populationGroupEClass, POPULATION_GROUP__REGION);
		createEReference(populationGroupEClass, POPULATION_GROUP__COUNTRY);
		createEReference(populationGroupEClass, POPULATION_GROUP__POPULATION_RISK_FACTOR);
		createEReference(populationGroupEClass, POPULATION_GROUP__SEASON);
		createEAttribute(populationGroupEClass, POPULATION_GROUP__POPULATION_GENDER);
		createEReference(populationGroupEClass, POPULATION_GROUP__PATTERN_CONSUMPTION);
		createEReference(populationGroupEClass, POPULATION_GROUP__POPULATION_AGE);

		scopeEClass = createEClass(SCOPE);
		createEAttribute(scopeEClass, SCOPE__GENERAL_COMMENT);
		createEAttribute(scopeEClass, SCOPE__TEMPORAL_INFORMATION);
		createEReference(scopeEClass, SCOPE__PRODUCT);
		createEReference(scopeEClass, SCOPE__HAZARD);
		createEReference(scopeEClass, SCOPE__POPULATION_GROUP);
		createEReference(scopeEClass, SCOPE__SPATIAL_INFORMATION);

		laboratoryEClass = createEClass(LABORATORY);
		createEAttribute(laboratoryEClass, LABORATORY__LABORATORY_NAME);
		createEAttribute(laboratoryEClass, LABORATORY__LABORATORY_COUNTRY);
		createEReference(laboratoryEClass, LABORATORY__LABORATORY_ACCREDITATION);

		spatialInformationEClass = createEClass(SPATIAL_INFORMATION);
		createEReference(spatialInformationEClass, SPATIAL_INFORMATION__REGION);
		createEReference(spatialInformationEClass, SPATIAL_INFORMATION__COUNTRY);

		eventEClass = createEClass(EVENT);
		createEAttribute(eventEClass, EVENT__EVENT);

		contactEClass = createEClass(CONTACT);
		createEAttribute(contactEClass, CONTACT__TITLE);
		createEAttribute(contactEClass, CONTACT__FAMILY_NAME);
		createEAttribute(contactEClass, CONTACT__GIVEN_NAME);
		createEAttribute(contactEClass, CONTACT__EMAIL);
		createEAttribute(contactEClass, CONTACT__TELEPHONE);
		createEAttribute(contactEClass, CONTACT__STREET_ADDRESS);
		createEAttribute(contactEClass, CONTACT__COUNTRY);
		createEAttribute(contactEClass, CONTACT__CITY);
		createEAttribute(contactEClass, CONTACT__ZIP_CODE);
		createEAttribute(contactEClass, CONTACT__REGION);
		createEAttribute(contactEClass, CONTACT__TIME_ZONE);
		createEAttribute(contactEClass, CONTACT__GENDER);
		createEAttribute(contactEClass, CONTACT__NOTE);
		createEAttribute(contactEClass, CONTACT__ORGANIZATION);

		referenceEClass = createEClass(REFERENCE);
		createEAttribute(referenceEClass, REFERENCE__IS_REFERENCE_DESCRIPTION);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_TYPE);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_DATE);
		createEAttribute(referenceEClass, REFERENCE__PMID);
		createEAttribute(referenceEClass, REFERENCE__DOI);
		createEAttribute(referenceEClass, REFERENCE__AUTHOR_LIST);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_TITLE);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_ABSTRACT);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_JOURNAL);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_VOLUME);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_ISSUE);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_STATUS);
		createEAttribute(referenceEClass, REFERENCE__PUBLICATION_WEBSITE);
		createEAttribute(referenceEClass, REFERENCE__COMMENT);

		modelMathEClass = createEClass(MODEL_MATH);
		createEAttribute(modelMathEClass, MODEL_MATH__FITTING_PROCEDURE);
		createEReference(modelMathEClass, MODEL_MATH__PARAMETER);
		createEReference(modelMathEClass, MODEL_MATH__MODEL_EQUATION);
		createEReference(modelMathEClass, MODEL_MATH__EXPOSURE);
		createEReference(modelMathEClass, MODEL_MATH__QUALITY_MEASURES);
		createEReference(modelMathEClass, MODEL_MATH__EVENT);

		parameterEClass = createEClass(PARAMETER);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_ID);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_CLASSIFICATION);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_NAME);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_DESCRIPTION);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_TYPE);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_UNIT);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_UNIT_CATEGORY);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_DATA_TYPE);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_SOURCE);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_SUBJECT);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_DISTRIBUTION);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_VALUE);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_VARIABILITY_SUBJECT);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_VALUE_MIN);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_VALUE_MAX);
		createEAttribute(parameterEClass, PARAMETER__PARAMETER_ERROR);
		createEReference(parameterEClass, PARAMETER__REFERENCE);

		modelEquationEClass = createEClass(MODEL_EQUATION);
		createEAttribute(modelEquationEClass, MODEL_EQUATION__MODEL_EQUATION_NAME);
		createEAttribute(modelEquationEClass, MODEL_EQUATION__MODEL_EQUATION_CLASS);
		createEAttribute(modelEquationEClass, MODEL_EQUATION__MODEL_EQUATION);
		createEReference(modelEquationEClass, MODEL_EQUATION__REFERENCE);
		createEReference(modelEquationEClass, MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL);

		exposureEClass = createEClass(EXPOSURE);
		createEAttribute(exposureEClass, EXPOSURE__TYPE_OF_EXPOSURE);
		createEAttribute(exposureEClass, EXPOSURE__UNCERTAINTY_ESTIMATION);
		createEReference(exposureEClass, EXPOSURE__METHODOLOGICAL_TREATMENT_OF_LEFT_CENSORED_DATA);
		createEReference(exposureEClass, EXPOSURE__LEVEL_OF_CONTAMINATION_AFTER_LEFT_CENSORED_DATA_TREATMENT);
		createEReference(exposureEClass, EXPOSURE__SCENARIO);

		stringObjectEClass = createEClass(STRING_OBJECT);
		createEAttribute(stringObjectEClass, STRING_OBJECT__VALUE);

		// Create enums
		publicationTypeEEnum = createEEnum(PUBLICATION_TYPE);
		parameterClassificationEEnum = createEEnum(PARAMETER_CLASSIFICATION);
		parameterTypeEEnum = createEEnum(PARAMETER_TYPE);

		// Create data types
		uriEDataType = createEDataType(URI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(generalInformationEClass, GeneralInformation.class, "GeneralInformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGeneralInformation_Name(), ecorePackage.getEString(), "name", null, 1, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Source(), ecorePackage.getEString(), "source", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Identifier(), ecorePackage.getEString(), "identifier", null, 1, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_CreationDate(), ecorePackage.getEDate(), "creationDate", null, 1, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Rights(), ecorePackage.getEString(), "rights", null, 1, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Available(), ecorePackage.getEBoolean(), "available", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Format(), ecorePackage.getEString(), "format", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Language(), ecorePackage.getEString(), "language", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Software(), ecorePackage.getEString(), "software", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_LanguageWrittenIn(), ecorePackage.getEString(), "languageWrittenIn", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Status(), ecorePackage.getEString(), "status", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Objective(), ecorePackage.getEString(), "objective", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeneralInformation_Description(), ecorePackage.getEString(), "description", null, 0, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeneralInformation_ModelCategory(), this.getModelCategory(), null, "modelCategory", null, 0, -1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeneralInformation_Modificationdate(), this.getModificationDate(), null, "modificationdate", null, 0, -1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeneralInformation_Author(), this.getContact(), null, "author", null, 1, 1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeneralInformation_Creators(), this.getContact(), null, "creators", null, 0, -1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeneralInformation_Reference(), this.getReference(), null, "reference", null, 1, -1, GeneralInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelCategoryEClass, ModelCategory.class, "ModelCategory", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModelCategory_ModelClass(), ecorePackage.getEString(), "modelClass", null, 1, 1, ModelCategory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getModelCategory_ModelClassComment(), ecorePackage.getEString(), "modelClassComment", null, 0, 1, ModelCategory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getModelCategory_BasicProcess(), ecorePackage.getEString(), "basicProcess", null, 0, 1, ModelCategory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelCategory_ModelSubClass(), this.getStringObject(), null, "modelSubClass", null, 0, -1, ModelCategory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(assayEClass, Assay.class, "Assay", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAssay_AssayName(), ecorePackage.getEString(), "assayName", null, 1, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssay_AssayDescription(), ecorePackage.getEString(), "assayDescription", null, 0, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssay_PercentageOfMoisture(), ecorePackage.getEString(), "percentageOfMoisture", null, 0, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssay_PercentageOfFat(), ecorePackage.getEString(), "percentageOfFat", null, 0, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssay_LimitOfDetection(), ecorePackage.getEString(), "limitOfDetection", null, 0, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssay_LimitOfQuantification(), ecorePackage.getEString(), "limitOfQuantification", null, 0, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssay_LeftCensoredData(), ecorePackage.getEString(), "leftCensoredData", null, 0, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssay_RangeOfContamination(), ecorePackage.getEString(), "rangeOfContamination", null, 0, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssay_UncertaintyValue(), ecorePackage.getEString(), "uncertaintyValue", null, 0, 1, Assay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(studyEClass, Study.class, "Study", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStudy_StudyIdentifier(), ecorePackage.getEString(), "studyIdentifier", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyTitle(), ecorePackage.getEString(), "studyTitle", null, 1, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyDescription(), ecorePackage.getEString(), "studyDescription", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyDesignType(), ecorePackage.getEString(), "studyDesignType", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyAssayMeasurementType(), ecorePackage.getEString(), "studyAssayMeasurementType", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyAssayTechnologyType(), ecorePackage.getEString(), "studyAssayTechnologyType", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyAssayTechnologyPlatform(), ecorePackage.getEString(), "studyAssayTechnologyPlatform", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_AccreditationProcedureForTheAssayTechnology(), ecorePackage.getEString(), "accreditationProcedureForTheAssayTechnology", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyProtocolName(), ecorePackage.getEString(), "studyProtocolName", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyProtocolType(), ecorePackage.getEString(), "studyProtocolType", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyProtocolDescription(), ecorePackage.getEString(), "studyProtocolDescription", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyProtocolURI(), this.getURI(), "studyProtocolURI", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyProtocolVersion(), ecorePackage.getEString(), "studyProtocolVersion", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyProtocolParametersName(), ecorePackage.getEString(), "studyProtocolParametersName", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyProtocolComponentsName(), ecorePackage.getEString(), "studyProtocolComponentsName", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudy_StudyProtocolComponentsType(), ecorePackage.getEString(), "studyProtocolComponentsType", null, 0, 1, Study.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataBackgroundEClass, DataBackground.class, "DataBackground", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataBackground_Study(), this.getStudy(), null, "study", null, 1, 1, DataBackground.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataBackground_Studysample(), this.getStudySample(), null, "studysample", null, 0, -1, DataBackground.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataBackground_Dietaryassessmentmethod(), this.getDietaryAssessmentMethod(), null, "dietaryassessmentmethod", null, 0, 1, DataBackground.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataBackground_Laboratory(), this.getLaboratory(), null, "laboratory", null, 0, 1, DataBackground.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataBackground_Assay(), this.getAssay(), null, "assay", null, 0, -1, DataBackground.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(studySampleEClass, StudySample.class, "StudySample", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStudySample_SampleName(), ecorePackage.getEString(), "sampleName", null, 1, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_ProtocolOfSampleCollection(), ecorePackage.getEString(), "protocolOfSampleCollection", null, 1, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_SamplingStrategy(), ecorePackage.getEString(), "samplingStrategy", null, 0, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_TypeOfSamplingProgram(), ecorePackage.getEString(), "typeOfSamplingProgram", null, 0, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_SamplingMethod(), ecorePackage.getEString(), "samplingMethod", null, 0, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_SamplingPlan(), ecorePackage.getEString(), "samplingPlan", null, 1, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_SamplingWeight(), ecorePackage.getEString(), "samplingWeight", null, 1, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_SamplingSize(), ecorePackage.getEString(), "samplingSize", null, 1, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_LotSizeUnit(), ecorePackage.getEString(), "lotSizeUnit", null, 0, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStudySample_SamplingPoint(), ecorePackage.getEString(), "samplingPoint", null, 0, 1, StudySample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dietaryAssessmentMethodEClass, DietaryAssessmentMethod.class, "DietaryAssessmentMethod", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDietaryAssessmentMethod_CollectionTool(), ecorePackage.getEString(), "collectionTool", null, 0, 1, DietaryAssessmentMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDietaryAssessmentMethod_NumberOfNonConsecutiveOneDay(), ecorePackage.getEInt(), "numberOfNonConsecutiveOneDay", null, 0, 1, DietaryAssessmentMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDietaryAssessmentMethod_SoftwareTool(), ecorePackage.getEString(), "softwareTool", null, 0, 1, DietaryAssessmentMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDietaryAssessmentMethod_NumberOfFoodItems(), ecorePackage.getEString(), "numberOfFoodItems", null, 0, 1, DietaryAssessmentMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDietaryAssessmentMethod_RecordTypes(), ecorePackage.getEString(), "recordTypes", null, 0, 1, DietaryAssessmentMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDietaryAssessmentMethod_FoodDescriptors(), ecorePackage.getEString(), "foodDescriptors", null, 0, 1, DietaryAssessmentMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modificationDateEClass, ModificationDate.class, "ModificationDate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModificationDate_Value(), ecorePackage.getEDate(), "value", null, 0, 1, ModificationDate.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(productEClass, Product.class, "Product", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProduct_ProductName(), ecorePackage.getEString(), "productName", null, 1, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_ProductDescription(), ecorePackage.getEString(), "productDescription", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_ProductUnit(), ecorePackage.getEString(), "productUnit", null, 1, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_ProductionMethod(), ecorePackage.getEString(), "productionMethod", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_Packaging(), ecorePackage.getEString(), "packaging", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_ProductTreatment(), ecorePackage.getEString(), "productTreatment", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_OriginCountry(), ecorePackage.getEString(), "originCountry", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_OriginArea(), ecorePackage.getEString(), "originArea", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_FisheriesArea(), ecorePackage.getEString(), "fisheriesArea", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_ProductionDate(), ecorePackage.getEDate(), "productionDate", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProduct_ExpiryDate(), ecorePackage.getEDate(), "expiryDate", null, 0, 1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(hazardEClass, Hazard.class, "Hazard", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHazard_HazardType(), ecorePackage.getEString(), "hazardType", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_HazardName(), ecorePackage.getEString(), "hazardName", null, 1, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_HazardDescription(), ecorePackage.getEString(), "hazardDescription", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_HazardUnit(), ecorePackage.getEString(), "hazardUnit", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_AdverseEffect(), ecorePackage.getEString(), "adverseEffect", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_SourceOfContamination(), ecorePackage.getEString(), "sourceOfContamination", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_BenchmarkDose(), ecorePackage.getEString(), "benchmarkDose", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_MaximumResidueLimit(), ecorePackage.getEString(), "maximumResidueLimit", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_NoObservedAdverseAffectLevel(), ecorePackage.getEString(), "noObservedAdverseAffectLevel", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_LowestObservedAdverseAffectLevel(), ecorePackage.getEString(), "lowestObservedAdverseAffectLevel", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_AcceptableOperatorExposureLevel(), ecorePackage.getEString(), "acceptableOperatorExposureLevel", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_AcuteReferenceDose(), ecorePackage.getEString(), "acuteReferenceDose", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_AcceptableDailyIntake(), ecorePackage.getEString(), "acceptableDailyIntake", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHazard_HazardIndSum(), ecorePackage.getEString(), "hazardIndSum", null, 0, 1, Hazard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(populationGroupEClass, PopulationGroup.class, "PopulationGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPopulationGroup_PopulationName(), ecorePackage.getEString(), "populationName", null, 1, 1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPopulationGroup_TargetPopulation(), ecorePackage.getEString(), "targetPopulation", null, 0, 1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_PopulationSpan(), this.getStringObject(), null, "populationSpan", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_PopulationDescription(), this.getStringObject(), null, "populationDescription", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_Bmi(), this.getStringObject(), null, "bmi", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_SpecialDietGroups(), this.getStringObject(), null, "specialDietGroups", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_Region(), this.getStringObject(), null, "region", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_Country(), this.getStringObject(), null, "country", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_PopulationRiskFactor(), this.getStringObject(), null, "populationRiskFactor", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_Season(), this.getStringObject(), null, "season", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPopulationGroup_PopulationGender(), theXMLTypePackage.getString(), "populationGender", null, 0, 1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_PatternConsumption(), this.getStringObject(), null, "patternConsumption", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPopulationGroup_PopulationAge(), this.getStringObject(), null, "populationAge", null, 0, -1, PopulationGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scopeEClass, Scope.class, "Scope", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getScope_GeneralComment(), ecorePackage.getEString(), "generalComment", null, 0, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getScope_TemporalInformation(), ecorePackage.getEString(), "temporalInformation", null, 0, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScope_Product(), this.getProduct(), null, "product", null, 0, -1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScope_Hazard(), this.getHazard(), null, "hazard", null, 0, -1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScope_PopulationGroup(), this.getPopulationGroup(), null, "populationGroup", null, 1, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getScope_SpatialInformation(), this.getSpatialInformation(), null, "spatialInformation", null, 0, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(laboratoryEClass, Laboratory.class, "Laboratory", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLaboratory_LaboratoryName(), ecorePackage.getEString(), "laboratoryName", null, 0, 1, Laboratory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLaboratory_LaboratoryCountry(), ecorePackage.getEString(), "laboratoryCountry", null, 0, 1, Laboratory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLaboratory_LaboratoryAccreditation(), this.getStringObject(), null, "laboratoryAccreditation", null, 0, -1, Laboratory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(spatialInformationEClass, SpatialInformation.class, "SpatialInformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSpatialInformation_Region(), this.getStringObject(), null, "region", null, 0, -1, SpatialInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSpatialInformation_Country(), this.getStringObject(), null, "country", null, 0, -1, SpatialInformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eventEClass, Event.class, "Event", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEvent_Event(), ecorePackage.getEString(), "event", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contactEClass, Contact.class, "Contact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getContact_Title(), ecorePackage.getEString(), "title", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_FamilyName(), ecorePackage.getEString(), "familyName", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_GivenName(), ecorePackage.getEString(), "givenName", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_Email(), ecorePackage.getEString(), "email", null, 1, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_Telephone(), ecorePackage.getEString(), "telephone", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_StreetAddress(), ecorePackage.getEString(), "streetAddress", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_Country(), ecorePackage.getEString(), "country", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_City(), ecorePackage.getEString(), "city", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_ZipCode(), ecorePackage.getEString(), "zipCode", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_Region(), ecorePackage.getEString(), "region", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_TimeZone(), ecorePackage.getEString(), "timeZone", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_Gender(), ecorePackage.getEString(), "gender", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_Note(), ecorePackage.getEString(), "note", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContact_Organization(), ecorePackage.getEString(), "organization", null, 0, 1, Contact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(referenceEClass, Reference.class, "Reference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getReference_IsReferenceDescription(), ecorePackage.getEBoolean(), "isReferenceDescription", null, 1, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationType(), this.getPublicationType(), "publicationType", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationDate(), ecorePackage.getEDate(), "publicationDate", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_Pmid(), ecorePackage.getEString(), "pmid", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_Doi(), ecorePackage.getEString(), "doi", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_AuthorList(), ecorePackage.getEString(), "authorList", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationTitle(), ecorePackage.getEString(), "publicationTitle", null, 1, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationAbstract(), ecorePackage.getEString(), "publicationAbstract", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationJournal(), ecorePackage.getEString(), "publicationJournal", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationVolume(), ecorePackage.getEInt(), "publicationVolume", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationIssue(), ecorePackage.getEInt(), "publicationIssue", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationStatus(), ecorePackage.getEString(), "publicationStatus", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_PublicationWebsite(), ecorePackage.getEString(), "publicationWebsite", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReference_Comment(), ecorePackage.getEString(), "comment", null, 0, 1, Reference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelMathEClass, ModelMath.class, "ModelMath", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModelMath_FittingProcedure(), ecorePackage.getEString(), "fittingProcedure", null, 0, 1, ModelMath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelMath_Parameter(), this.getParameter(), null, "parameter", null, 1, -1, ModelMath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelMath_ModelEquation(), this.getModelEquation(), null, "modelEquation", null, 0, -1, ModelMath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelMath_Exposure(), this.getExposure(), null, "exposure", null, 0, 1, ModelMath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelMath_QualityMeasures(), this.getStringObject(), null, "qualityMeasures", null, 0, -1, ModelMath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelMath_Event(), this.getStringObject(), null, "event", null, 0, -1, ModelMath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterEClass, Parameter.class, "Parameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParameter_ParameterID(), ecorePackage.getEString(), "parameterID", null, 1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterClassification(), this.getParameterClassification(), "parameterClassification", null, 1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterName(), ecorePackage.getEString(), "parameterName", null, 1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterDescription(), ecorePackage.getEString(), "parameterDescription", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterType(), ecorePackage.getEString(), "parameterType", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterUnit(), ecorePackage.getEString(), "parameterUnit", null, 1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterUnitCategory(), ecorePackage.getEString(), "parameterUnitCategory", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterDataType(), this.getParameterType(), "parameterDataType", null, 1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterSource(), ecorePackage.getEString(), "parameterSource", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterSubject(), ecorePackage.getEString(), "parameterSubject", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterDistribution(), ecorePackage.getEString(), "parameterDistribution", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterValue(), ecorePackage.getEString(), "parameterValue", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterVariabilitySubject(), ecorePackage.getEString(), "parameterVariabilitySubject", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterValueMin(), ecorePackage.getEString(), "parameterValueMin", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterValueMax(), ecorePackage.getEString(), "parameterValueMax", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_ParameterError(), ecorePackage.getEString(), "parameterError", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameter_Reference(), this.getReference(), null, "reference", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelEquationEClass, ModelEquation.class, "ModelEquation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModelEquation_ModelEquationName(), ecorePackage.getEString(), "modelEquationName", null, 1, 1, ModelEquation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getModelEquation_ModelEquationClass(), ecorePackage.getEString(), "modelEquationClass", null, 0, 1, ModelEquation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getModelEquation_ModelEquation(), ecorePackage.getEString(), "modelEquation", null, 1, 1, ModelEquation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelEquation_Reference(), this.getReference(), null, "reference", null, 0, -1, ModelEquation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelEquation_HypothesisOfTheModel(), this.getStringObject(), null, "hypothesisOfTheModel", null, 0, -1, ModelEquation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(exposureEClass, Exposure.class, "Exposure", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExposure_TypeOfExposure(), ecorePackage.getEString(), "typeOfExposure", null, 1, 1, Exposure.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExposure_UncertaintyEstimation(), ecorePackage.getEString(), "uncertaintyEstimation", null, 0, 1, Exposure.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExposure_MethodologicalTreatmentOfLeftCensoredData(), this.getStringObject(), null, "methodologicalTreatmentOfLeftCensoredData", null, 0, -1, Exposure.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExposure_LevelOfContaminationAfterLeftCensoredDataTreatment(), this.getStringObject(), null, "levelOfContaminationAfterLeftCensoredDataTreatment", null, 0, -1, Exposure.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExposure_Scenario(), this.getStringObject(), null, "scenario", null, 0, -1, Exposure.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stringObjectEClass, StringObject.class, "StringObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStringObject_Value(), ecorePackage.getEString(), "value", null, 0, 1, StringObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(publicationTypeEEnum, PublicationType.class, "PublicationType");
		addEEnumLiteral(publicationTypeEEnum, PublicationType.ABST);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.ADVS);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.AGGR);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.ANCIENT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.ART);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.BILL);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.BLOG);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.BOOK);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.CASE);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.CHAP);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.CHART);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.CLSWK);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.COMP);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.CONF);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.CPAPER);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.CTLG);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.DATA);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.DBASE);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.DICT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.EBOOK);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.ECHAP);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.EDBOOK);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.EJOUR);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.ELECT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.ENCYC);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.EQUA);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.FIGURE);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.GEN);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.GOVDOC);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.GRANT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.HEAR);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.ICOMM);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.INPR);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.JOUR);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.JFULL);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.LEGAL);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.MANSCPT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.MAP);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.MGZN);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.MPCT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.MULTI);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.MUSIC);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.NEWS);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.PAMP);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.PAT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.PCOMM);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.RPRT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.SER);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.SLIDE);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.SOUND);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.STAND);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.STAT);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.THES);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.UNPB);
		addEEnumLiteral(publicationTypeEEnum, PublicationType.VIDEO);

		initEEnum(parameterClassificationEEnum, ParameterClassification.class, "ParameterClassification");
		addEEnumLiteral(parameterClassificationEEnum, ParameterClassification.CONSTANT);
		addEEnumLiteral(parameterClassificationEEnum, ParameterClassification.INPUT);
		addEEnumLiteral(parameterClassificationEEnum, ParameterClassification.OUTPUT);

		initEEnum(parameterTypeEEnum, ParameterType.class, "ParameterType");
		addEEnumLiteral(parameterTypeEEnum, ParameterType.INTEGER);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.DOUBLE);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.NUMBER);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.DATE);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.FILE);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.BOOLEAN);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.VECTOR_OF_NUMBERS);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.VECTOR_OF_STRINGS);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.MATRIX_OF_NUMBERS);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.MATRIX_OF_STRINGS);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.OBJECT);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.OTHER);

		// Initialize data types
		initEDataType(uriEDataType, java.net.URI.class, "URI", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //MetadataPackageImpl
