/**
 */
package metadata;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see metadata.MetadataFactory
 * @model kind="package"
 * @generated
 */
public interface MetadataPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "metadata";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/metadata";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "metadata";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MetadataPackage eINSTANCE = metadata.impl.MetadataPackageImpl.init();

	/**
	 * The meta object id for the '{@link metadata.impl.GeneralInformationImpl <em>General Information</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.GeneralInformationImpl
	 * @see metadata.impl.MetadataPackageImpl#getGeneralInformation()
	 * @generated
	 */
	int GENERAL_INFORMATION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__SOURCE = 1;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__IDENTIFIER = 2;

	/**
	 * The feature id for the '<em><b>Creation Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__CREATION_DATE = 3;

	/**
	 * The feature id for the '<em><b>Rights</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__RIGHTS = 4;

	/**
	 * The feature id for the '<em><b>Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__AVAILABLE = 5;

	/**
	 * The feature id for the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__FORMAT = 6;

	/**
	 * The feature id for the '<em><b>Language</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__LANGUAGE = 7;

	/**
	 * The feature id for the '<em><b>Software</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__SOFTWARE = 8;

	/**
	 * The feature id for the '<em><b>Language Written In</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN = 9;

	/**
	 * The feature id for the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__STATUS = 10;

	/**
	 * The feature id for the '<em><b>Objective</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__OBJECTIVE = 11;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__DESCRIPTION = 12;

	/**
	 * The feature id for the '<em><b>Modelcategory</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__MODELCATEGORY = 13;

	/**
	 * The feature id for the '<em><b>Modificationdate</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__MODIFICATIONDATE = 14;

	/**
	 * The feature id for the '<em><b>Author</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__AUTHOR = 15;

	/**
	 * The feature id for the '<em><b>Creators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__CREATORS = 16;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION__REFERENCE = 17;

	/**
	 * The number of structural features of the '<em>General Information</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION_FEATURE_COUNT = 18;

	/**
	 * The operation id for the '<em>Equals</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION___EQUALS__GENERALINFORMATION = 0;

	/**
	 * The number of operations of the '<em>General Information</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERAL_INFORMATION_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link metadata.impl.ModelCategoryImpl <em>Model Category</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ModelCategoryImpl
	 * @see metadata.impl.MetadataPackageImpl#getModelCategory()
	 * @generated
	 */
	int MODEL_CATEGORY = 1;

	/**
	 * The feature id for the '<em><b>Model Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_CATEGORY__MODEL_CLASS = 0;

	/**
	 * The feature id for the '<em><b>Model Sub Class</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_CATEGORY__MODEL_SUB_CLASS = 1;

	/**
	 * The feature id for the '<em><b>Model Class Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_CATEGORY__MODEL_CLASS_COMMENT = 2;

	/**
	 * The feature id for the '<em><b>Basic Process</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_CATEGORY__BASIC_PROCESS = 3;

	/**
	 * The number of structural features of the '<em>Model Category</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_CATEGORY_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Model Category</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_CATEGORY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.AssayImpl <em>Assay</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.AssayImpl
	 * @see metadata.impl.MetadataPackageImpl#getAssay()
	 * @generated
	 */
	int ASSAY = 2;

	/**
	 * The feature id for the '<em><b>Assay Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__ASSAY_NAME = 0;

	/**
	 * The feature id for the '<em><b>Assay Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__ASSAY_DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Percentage Of Moisture</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__PERCENTAGE_OF_MOISTURE = 2;

	/**
	 * The feature id for the '<em><b>Percentage Of Fat</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__PERCENTAGE_OF_FAT = 3;

	/**
	 * The feature id for the '<em><b>Limit Of Detection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__LIMIT_OF_DETECTION = 4;

	/**
	 * The feature id for the '<em><b>Limit Of Quantification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__LIMIT_OF_QUANTIFICATION = 5;

	/**
	 * The feature id for the '<em><b>Left Censored Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__LEFT_CENSORED_DATA = 6;

	/**
	 * The feature id for the '<em><b>Range Of Contamination</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__RANGE_OF_CONTAMINATION = 7;

	/**
	 * The feature id for the '<em><b>Uncertainty Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY__UNCERTAINTY_VALUE = 8;

	/**
	 * The number of structural features of the '<em>Assay</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY_FEATURE_COUNT = 9;

	/**
	 * The number of operations of the '<em>Assay</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSAY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.StudyImpl <em>Study</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.StudyImpl
	 * @see metadata.impl.MetadataPackageImpl#getStudy()
	 * @generated
	 */
	int STUDY = 3;

	/**
	 * The feature id for the '<em><b>Study Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_IDENTIFIER = 0;

	/**
	 * The feature id for the '<em><b>Study Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_TITLE = 1;

	/**
	 * The feature id for the '<em><b>Study Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_DESCRIPTION = 2;

	/**
	 * The feature id for the '<em><b>Study Design Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_DESIGN_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Study Assay Measurement Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_ASSAY_MEASUREMENT_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Study Assay Technology Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Study Assay Technology Platform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM = 6;

	/**
	 * The feature id for the '<em><b>Accreditation Procedure For The Assay Technology</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY = 7;

	/**
	 * The feature id for the '<em><b>Study Protocol Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_PROTOCOL_NAME = 8;

	/**
	 * The feature id for the '<em><b>Study Protocol Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_PROTOCOL_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Study Protocol Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_PROTOCOL_DESCRIPTION = 10;

	/**
	 * The feature id for the '<em><b>Study Protocol URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_PROTOCOL_URI = 11;

	/**
	 * The feature id for the '<em><b>Study Protocol Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_PROTOCOL_VERSION = 12;

	/**
	 * The feature id for the '<em><b>Study Protocol Components Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_PROTOCOL_COMPONENTS_NAME = 13;

	/**
	 * The feature id for the '<em><b>Study Protocol Components Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE = 14;

	/**
	 * The number of structural features of the '<em>Study</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_FEATURE_COUNT = 15;

	/**
	 * The number of operations of the '<em>Study</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.DataBackgroundImpl <em>Data Background</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.DataBackgroundImpl
	 * @see metadata.impl.MetadataPackageImpl#getDataBackground()
	 * @generated
	 */
	int DATA_BACKGROUND = 4;

	/**
	 * The feature id for the '<em><b>Study</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_BACKGROUND__STUDY = 0;

	/**
	 * The feature id for the '<em><b>Studysample</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_BACKGROUND__STUDYSAMPLE = 1;

	/**
	 * The feature id for the '<em><b>Dietaryassessmentmethod</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_BACKGROUND__DIETARYASSESSMENTMETHOD = 2;

	/**
	 * The feature id for the '<em><b>Laboratory</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_BACKGROUND__LABORATORY = 3;

	/**
	 * The feature id for the '<em><b>Assay</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_BACKGROUND__ASSAY = 4;

	/**
	 * The number of structural features of the '<em>Data Background</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_BACKGROUND_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Data Background</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_BACKGROUND_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.StudySampleImpl <em>Study Sample</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.StudySampleImpl
	 * @see metadata.impl.MetadataPackageImpl#getStudySample()
	 * @generated
	 */
	int STUDY_SAMPLE = 5;

	/**
	 * The feature id for the '<em><b>Sample Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__SAMPLE_NAME = 0;

	/**
	 * The feature id for the '<em><b>Protocol Of Sample Collection</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__PROTOCOL_OF_SAMPLE_COLLECTION = 1;

	/**
	 * The feature id for the '<em><b>Sampling Strategy</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__SAMPLING_STRATEGY = 2;

	/**
	 * The feature id for the '<em><b>Type Of Sampling Program</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__TYPE_OF_SAMPLING_PROGRAM = 3;

	/**
	 * The feature id for the '<em><b>Sampling Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__SAMPLING_METHOD = 4;

	/**
	 * The feature id for the '<em><b>Sampling Plan</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__SAMPLING_PLAN = 5;

	/**
	 * The feature id for the '<em><b>Sampling Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__SAMPLING_WEIGHT = 6;

	/**
	 * The feature id for the '<em><b>Sampling Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__SAMPLING_SIZE = 7;

	/**
	 * The feature id for the '<em><b>Lot Size Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__LOT_SIZE_UNIT = 8;

	/**
	 * The feature id for the '<em><b>Sampling Point</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE__SAMPLING_POINT = 9;

	/**
	 * The number of structural features of the '<em>Study Sample</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE_FEATURE_COUNT = 10;

	/**
	 * The number of operations of the '<em>Study Sample</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STUDY_SAMPLE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.DietaryAssessmentMethodImpl <em>Dietary Assessment Method</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.DietaryAssessmentMethodImpl
	 * @see metadata.impl.MetadataPackageImpl#getDietaryAssessmentMethod()
	 * @generated
	 */
	int DIETARY_ASSESSMENT_METHOD = 6;

	/**
	 * The feature id for the '<em><b>Collection Tool</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIETARY_ASSESSMENT_METHOD__COLLECTION_TOOL = 0;

	/**
	 * The feature id for the '<em><b>Number Of Non Consecutive One Day</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIETARY_ASSESSMENT_METHOD__NUMBER_OF_NON_CONSECUTIVE_ONE_DAY = 1;

	/**
	 * The feature id for the '<em><b>Software Tool</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIETARY_ASSESSMENT_METHOD__SOFTWARE_TOOL = 2;

	/**
	 * The feature id for the '<em><b>Number Of Food Items</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIETARY_ASSESSMENT_METHOD__NUMBER_OF_FOOD_ITEMS = 3;

	/**
	 * The feature id for the '<em><b>Record Types</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIETARY_ASSESSMENT_METHOD__RECORD_TYPES = 4;

	/**
	 * The feature id for the '<em><b>Food Descriptors</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIETARY_ASSESSMENT_METHOD__FOOD_DESCRIPTORS = 5;

	/**
	 * The number of structural features of the '<em>Dietary Assessment Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIETARY_ASSESSMENT_METHOD_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Dietary Assessment Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIETARY_ASSESSMENT_METHOD_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ModificationDateImpl <em>Modification Date</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ModificationDateImpl
	 * @see metadata.impl.MetadataPackageImpl#getModificationDate()
	 * @generated
	 */
	int MODIFICATION_DATE = 7;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODIFICATION_DATE__VALUE = 0;

	/**
	 * The number of structural features of the '<em>Modification Date</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODIFICATION_DATE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Modification Date</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODIFICATION_DATE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ProductImpl <em>Product</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ProductImpl
	 * @see metadata.impl.MetadataPackageImpl#getProduct()
	 * @generated
	 */
	int PRODUCT = 8;

	/**
	 * The feature id for the '<em><b>Product Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__PRODUCT_NAME = 0;

	/**
	 * The feature id for the '<em><b>Product Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__PRODUCT_DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Product Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__PRODUCT_UNIT = 2;

	/**
	 * The feature id for the '<em><b>Production Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__PRODUCTION_METHOD = 3;

	/**
	 * The feature id for the '<em><b>Packaging</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__PACKAGING = 4;

	/**
	 * The feature id for the '<em><b>Product Treatment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__PRODUCT_TREATMENT = 5;

	/**
	 * The feature id for the '<em><b>Origin Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__ORIGIN_COUNTRY = 6;

	/**
	 * The feature id for the '<em><b>Origin Area</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__ORIGIN_AREA = 7;

	/**
	 * The feature id for the '<em><b>Fisheries Area</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__FISHERIES_AREA = 8;

	/**
	 * The feature id for the '<em><b>Production Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__PRODUCTION_DATE = 9;

	/**
	 * The feature id for the '<em><b>Expiry Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT__EXPIRY_DATE = 10;

	/**
	 * The number of structural features of the '<em>Product</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT_FEATURE_COUNT = 11;

	/**
	 * The number of operations of the '<em>Product</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRODUCT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.HazardImpl <em>Hazard</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.HazardImpl
	 * @see metadata.impl.MetadataPackageImpl#getHazard()
	 * @generated
	 */
	int HAZARD = 9;

	/**
	 * The feature id for the '<em><b>Hazard Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__HAZARD_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Hazard Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__HAZARD_NAME = 1;

	/**
	 * The feature id for the '<em><b>Hazard Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__HAZARD_DESCRIPTION = 2;

	/**
	 * The feature id for the '<em><b>Hazard Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__HAZARD_UNIT = 3;

	/**
	 * The feature id for the '<em><b>Adverse Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__ADVERSE_EFFECT = 4;

	/**
	 * The feature id for the '<em><b>Source Of Contamination</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__SOURCE_OF_CONTAMINATION = 5;

	/**
	 * The feature id for the '<em><b>Benchmark Dose</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__BENCHMARK_DOSE = 6;

	/**
	 * The feature id for the '<em><b>Maximum Residue Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__MAXIMUM_RESIDUE_LIMIT = 7;

	/**
	 * The feature id for the '<em><b>No Observed Adverse Affect Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__NO_OBSERVED_ADVERSE_AFFECT_LEVEL = 8;

	/**
	 * The feature id for the '<em><b>Lowest Observed Adverse Affect Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL = 9;

	/**
	 * The feature id for the '<em><b>Acceptable Operator Exposure Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL = 10;

	/**
	 * The feature id for the '<em><b>Acute Reference Dose</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__ACUTE_REFERENCE_DOSE = 11;

	/**
	 * The feature id for the '<em><b>Acceptable Daily Intake</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__ACCEPTABLE_DAILY_INTAKE = 12;

	/**
	 * The feature id for the '<em><b>Hazard Ind Sum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD__HAZARD_IND_SUM = 13;

	/**
	 * The number of structural features of the '<em>Hazard</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD_FEATURE_COUNT = 14;

	/**
	 * The number of operations of the '<em>Hazard</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HAZARD_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.PopulationGroupImpl <em>Population Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.PopulationGroupImpl
	 * @see metadata.impl.MetadataPackageImpl#getPopulationGroup()
	 * @generated
	 */
	int POPULATION_GROUP = 10;

	/**
	 * The feature id for the '<em><b>Population Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__POPULATION_NAME = 0;

	/**
	 * The feature id for the '<em><b>Target Population</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__TARGET_POPULATION = 1;

	/**
	 * The feature id for the '<em><b>Population Span</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__POPULATION_SPAN = 2;

	/**
	 * The feature id for the '<em><b>Population Description</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__POPULATION_DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Population Age</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__POPULATION_AGE = 4;

	/**
	 * The feature id for the '<em><b>Population Gender</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__POPULATION_GENDER = 5;

	/**
	 * The feature id for the '<em><b>Bmi</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__BMI = 6;

	/**
	 * The feature id for the '<em><b>Special Diet Groups</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__SPECIAL_DIET_GROUPS = 7;

	/**
	 * The feature id for the '<em><b>Pattern Consumption</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__PATTERN_CONSUMPTION = 8;

	/**
	 * The feature id for the '<em><b>Region</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__REGION = 9;

	/**
	 * The feature id for the '<em><b>Country</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__COUNTRY = 10;

	/**
	 * The feature id for the '<em><b>Population Risk Factor</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__POPULATION_RISK_FACTOR = 11;

	/**
	 * The feature id for the '<em><b>Season</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP__SEASON = 12;

	/**
	 * The number of structural features of the '<em>Population Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP_FEATURE_COUNT = 13;

	/**
	 * The number of operations of the '<em>Population Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POPULATION_GROUP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ScopeImpl <em>Scope</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ScopeImpl
	 * @see metadata.impl.MetadataPackageImpl#getScope()
	 * @generated
	 */
	int SCOPE = 11;

	/**
	 * The feature id for the '<em><b>General Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE__GENERAL_COMMENT = 0;

	/**
	 * The feature id for the '<em><b>Temporal Information</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE__TEMPORAL_INFORMATION = 1;

	/**
	 * The feature id for the '<em><b>Product</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE__PRODUCT = 2;

	/**
	 * The feature id for the '<em><b>Hazard</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE__HAZARD = 3;

	/**
	 * The feature id for the '<em><b>Populationgroup</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE__POPULATIONGROUP = 4;

	/**
	 * The feature id for the '<em><b>Spatial Information</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE__SPATIAL_INFORMATION = 5;

	/**
	 * The number of structural features of the '<em>Scope</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Scope</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCOPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.LaboratoryImpl <em>Laboratory</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.LaboratoryImpl
	 * @see metadata.impl.MetadataPackageImpl#getLaboratory()
	 * @generated
	 */
	int LABORATORY = 12;

	/**
	 * The feature id for the '<em><b>Laboratory Accreditation</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABORATORY__LABORATORY_ACCREDITATION = 0;

	/**
	 * The feature id for the '<em><b>Laboratory Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABORATORY__LABORATORY_NAME = 1;

	/**
	 * The feature id for the '<em><b>Laboratory Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABORATORY__LABORATORY_COUNTRY = 2;

	/**
	 * The number of structural features of the '<em>Laboratory</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABORATORY_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Laboratory</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LABORATORY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.SpatialInformationImpl <em>Spatial Information</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.SpatialInformationImpl
	 * @see metadata.impl.MetadataPackageImpl#getSpatialInformation()
	 * @generated
	 */
	int SPATIAL_INFORMATION = 13;

	/**
	 * The feature id for the '<em><b>Region</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_INFORMATION__REGION = 0;

	/**
	 * The feature id for the '<em><b>Country</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_INFORMATION__COUNTRY = 1;

	/**
	 * The number of structural features of the '<em>Spatial Information</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_INFORMATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Spatial Information</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPATIAL_INFORMATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.modelApplicabilityImpl <em>model Applicability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.modelApplicabilityImpl
	 * @see metadata.impl.MetadataPackageImpl#getmodelApplicability()
	 * @generated
	 */
	int MODEL_APPLICABILITY = 14;

	/**
	 * The feature id for the '<em><b>Model Applicability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_APPLICABILITY__MODEL_APPLICABILITY = 0;

	/**
	 * The number of structural features of the '<em>model Applicability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_APPLICABILITY_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>model Applicability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_APPLICABILITY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.EventImpl <em>Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.EventImpl
	 * @see metadata.impl.MetadataPackageImpl#getEvent()
	 * @generated
	 */
	int EVENT = 15;

	/**
	 * The feature id for the '<em><b>Event</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__EVENT = 0;

	/**
	 * The number of structural features of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ContactImpl <em>Contact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ContactImpl
	 * @see metadata.impl.MetadataPackageImpl#getContact()
	 * @generated
	 */
	int CONTACT = 16;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__TITLE = 0;

	/**
	 * The feature id for the '<em><b>Family Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__FAMILY_NAME = 1;

	/**
	 * The feature id for the '<em><b>Email</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__EMAIL = 2;

	/**
	 * The feature id for the '<em><b>Telephone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__TELEPHONE = 3;

	/**
	 * The feature id for the '<em><b>Street Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__STREET_ADDRESS = 4;

	/**
	 * The feature id for the '<em><b>Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__COUNTRY = 5;

	/**
	 * The feature id for the '<em><b>City</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__CITY = 6;

	/**
	 * The feature id for the '<em><b>Zip Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__ZIP_CODE = 7;

	/**
	 * The feature id for the '<em><b>Post Office Box</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__POST_OFFICE_BOX = 8;

	/**
	 * The feature id for the '<em><b>Region</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__REGION = 9;

	/**
	 * The feature id for the '<em><b>Nickname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__NICKNAME = 10;

	/**
	 * The feature id for the '<em><b>Time Zone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__TIME_ZONE = 11;

	/**
	 * The feature id for the '<em><b>Gender</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__GENDER = 12;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__NAME = 13;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__URL = 14;

	/**
	 * The feature id for the '<em><b>Note</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__NOTE = 15;

	/**
	 * The feature id for the '<em><b>Logo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__LOGO = 16;

	/**
	 * The feature id for the '<em><b>Organization</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__ORGANIZATION = 17;

	/**
	 * The feature id for the '<em><b>Fn</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT__FN = 18;

	/**
	 * The number of structural features of the '<em>Contact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT_FEATURE_COUNT = 19;

	/**
	 * The number of operations of the '<em>Contact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTACT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ReferenceImpl <em>Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ReferenceImpl
	 * @see metadata.impl.MetadataPackageImpl#getReference()
	 * @generated
	 */
	int REFERENCE = 17;

	/**
	 * The feature id for the '<em><b>Is Reference Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__IS_REFERENCE_DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Publication Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Publication Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_DATE = 2;

	/**
	 * The feature id for the '<em><b>Pmid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PMID = 3;

	/**
	 * The feature id for the '<em><b>Doi</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__DOI = 4;

	/**
	 * The feature id for the '<em><b>Author List</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__AUTHOR_LIST = 5;

	/**
	 * The feature id for the '<em><b>Publication Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_TITLE = 6;

	/**
	 * The feature id for the '<em><b>Publication Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_ABSTRACT = 7;

	/**
	 * The feature id for the '<em><b>Publication Journal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_JOURNAL = 8;

	/**
	 * The feature id for the '<em><b>Publication Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_VOLUME = 9;

	/**
	 * The feature id for the '<em><b>Publication Issue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_ISSUE = 10;

	/**
	 * The feature id for the '<em><b>Publication Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_STATUS = 11;

	/**
	 * The feature id for the '<em><b>Publication Website</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PUBLICATION_WEBSITE = 12;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE__COMMENT = 13;

	/**
	 * The number of structural features of the '<em>Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_FEATURE_COUNT = 14;

	/**
	 * The number of operations of the '<em>Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ModelMathImpl <em>Model Math</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ModelMathImpl
	 * @see metadata.impl.MetadataPackageImpl#getModelMath()
	 * @generated
	 */
	int MODEL_MATH = 18;

	/**
	 * The feature id for the '<em><b>Quality Measures</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_MATH__QUALITY_MEASURES = 0;

	/**
	 * The feature id for the '<em><b>Fitting Procedure</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_MATH__FITTING_PROCEDURE = 1;

	/**
	 * The feature id for the '<em><b>Event</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_MATH__EVENT = 2;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_MATH__PARAMETER = 3;

	/**
	 * The feature id for the '<em><b>Modelequation</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_MATH__MODELEQUATION = 4;

	/**
	 * The feature id for the '<em><b>Exposure</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_MATH__EXPOSURE = 5;

	/**
	 * The number of structural features of the '<em>Model Math</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_MATH_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Model Math</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_MATH_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ParameterImpl
	 * @see metadata.impl.MetadataPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 19;

	/**
	 * The feature id for the '<em><b>Parameter ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_ID = 0;

	/**
	 * The feature id for the '<em><b>Parameter Classification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_CLASSIFICATION = 1;

	/**
	 * The feature id for the '<em><b>Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_NAME = 2;

	/**
	 * The feature id for the '<em><b>Parameter Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Parameter Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Parameter Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_UNIT = 5;

	/**
	 * The feature id for the '<em><b>Parameter Unit Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_UNIT_CATEGORY = 6;

	/**
	 * The feature id for the '<em><b>Parameter Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_DATA_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Parameter Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_SOURCE = 8;

	/**
	 * The feature id for the '<em><b>Parameter Subject</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_SUBJECT = 9;

	/**
	 * The feature id for the '<em><b>Parameter Distribution</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_DISTRIBUTION = 10;

	/**
	 * The feature id for the '<em><b>Parameter Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_VALUE = 11;

	/**
	 * The feature id for the '<em><b>Parameter Variability Subject</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_VARIABILITY_SUBJECT = 12;

	/**
	 * The feature id for the '<em><b>Parameter Value Min</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_VALUE_MIN = 13;

	/**
	 * The feature id for the '<em><b>Parameter Value Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_VALUE_MAX = 14;

	/**
	 * The feature id for the '<em><b>Parameter Error</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PARAMETER_ERROR = 15;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__REFERENCE = 16;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = 17;

	/**
	 * The number of operations of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ModelEquationImpl <em>Model Equation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ModelEquationImpl
	 * @see metadata.impl.MetadataPackageImpl#getModelEquation()
	 * @generated
	 */
	int MODEL_EQUATION = 20;

	/**
	 * The feature id for the '<em><b>Model Equation Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_EQUATION__MODEL_EQUATION_NAME = 0;

	/**
	 * The feature id for the '<em><b>Model Equation Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_EQUATION__MODEL_EQUATION_CLASS = 1;

	/**
	 * The feature id for the '<em><b>Model Equation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_EQUATION__MODEL_EQUATION = 2;

	/**
	 * The feature id for the '<em><b>Hypothesis Of The Model</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_EQUATION__REFERENCE = 4;

	/**
	 * The number of structural features of the '<em>Model Equation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_EQUATION_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Model Equation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_EQUATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.impl.ExposureImpl <em>Exposure</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.impl.ExposureImpl
	 * @see metadata.impl.MetadataPackageImpl#getExposure()
	 * @generated
	 */
	int EXPOSURE = 21;

	/**
	 * The feature id for the '<em><b>Methodological Treatment Of Left Censored Data</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE__METHODOLOGICAL_TREATMENT_OF_LEFT_CENSORED_DATA = 0;

	/**
	 * The feature id for the '<em><b>Level Of Contamination After Left Censored Data Treatment</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE__LEVEL_OF_CONTAMINATION_AFTER_LEFT_CENSORED_DATA_TREATMENT = 1;

	/**
	 * The feature id for the '<em><b>Type Of Exposure</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE__TYPE_OF_EXPOSURE = 2;

	/**
	 * The feature id for the '<em><b>Scenario</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE__SCENARIO = 3;

	/**
	 * The feature id for the '<em><b>Uncertainty Estimation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE__UNCERTAINTY_ESTIMATION = 4;

	/**
	 * The number of structural features of the '<em>Exposure</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Exposure</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPOSURE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link metadata.PublicationType <em>Publication Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.PublicationType
	 * @see metadata.impl.MetadataPackageImpl#getPublicationType()
	 * @generated
	 */
	int PUBLICATION_TYPE = 22;

	/**
	 * The meta object id for the '{@link metadata.ParameterClassification <em>Parameter Classification</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.ParameterClassification
	 * @see metadata.impl.MetadataPackageImpl#getParameterClassification()
	 * @generated
	 */
	int PARAMETER_CLASSIFICATION = 23;

	/**
	 * The meta object id for the '{@link metadata.ParameterType <em>Parameter Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see metadata.ParameterType
	 * @see metadata.impl.MetadataPackageImpl#getParameterType()
	 * @generated
	 */
	int PARAMETER_TYPE = 24;

	/**
	 * The meta object id for the '<em>URI</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.net.URI
	 * @see metadata.impl.MetadataPackageImpl#getURI()
	 * @generated
	 */
	int URI = 25;


	/**
	 * Returns the meta object for class '{@link metadata.GeneralInformation <em>General Information</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>General Information</em>'.
	 * @see metadata.GeneralInformation
	 * @generated
	 */
	EClass getGeneralInformation();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see metadata.GeneralInformation#getName()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Name();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see metadata.GeneralInformation#getSource()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Source();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see metadata.GeneralInformation#getIdentifier()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Identifier();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getCreationDate <em>Creation Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Creation Date</em>'.
	 * @see metadata.GeneralInformation#getCreationDate()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_CreationDate();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getRights <em>Rights</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rights</em>'.
	 * @see metadata.GeneralInformation#getRights()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Rights();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#isAvailable <em>Available</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Available</em>'.
	 * @see metadata.GeneralInformation#isAvailable()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Available();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getFormat <em>Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Format</em>'.
	 * @see metadata.GeneralInformation#getFormat()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Format();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getLanguage <em>Language</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Language</em>'.
	 * @see metadata.GeneralInformation#getLanguage()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Language();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getSoftware <em>Software</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Software</em>'.
	 * @see metadata.GeneralInformation#getSoftware()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Software();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getLanguageWrittenIn <em>Language Written In</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Language Written In</em>'.
	 * @see metadata.GeneralInformation#getLanguageWrittenIn()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_LanguageWrittenIn();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getStatus <em>Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Status</em>'.
	 * @see metadata.GeneralInformation#getStatus()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Status();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getObjective <em>Objective</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Objective</em>'.
	 * @see metadata.GeneralInformation#getObjective()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Objective();

	/**
	 * Returns the meta object for the attribute '{@link metadata.GeneralInformation#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see metadata.GeneralInformation#getDescription()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EAttribute getGeneralInformation_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.GeneralInformation#getModelcategory <em>Modelcategory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modelcategory</em>'.
	 * @see metadata.GeneralInformation#getModelcategory()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EReference getGeneralInformation_Modelcategory();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.GeneralInformation#getModificationdate <em>Modificationdate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modificationdate</em>'.
	 * @see metadata.GeneralInformation#getModificationdate()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EReference getGeneralInformation_Modificationdate();

	/**
	 * Returns the meta object for the containment reference '{@link metadata.GeneralInformation#getAuthor <em>Author</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Author</em>'.
	 * @see metadata.GeneralInformation#getAuthor()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EReference getGeneralInformation_Author();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.GeneralInformation#getCreators <em>Creators</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Creators</em>'.
	 * @see metadata.GeneralInformation#getCreators()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EReference getGeneralInformation_Creators();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.GeneralInformation#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Reference</em>'.
	 * @see metadata.GeneralInformation#getReference()
	 * @see #getGeneralInformation()
	 * @generated
	 */
	EReference getGeneralInformation_Reference();

	/**
	 * Returns the meta object for the '{@link metadata.GeneralInformation#equals(metadata.GeneralInformation) <em>Equals</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Equals</em>' operation.
	 * @see metadata.GeneralInformation#equals(metadata.GeneralInformation)
	 * @generated
	 */
	EOperation getGeneralInformation__Equals__GeneralInformation();

	/**
	 * Returns the meta object for class '{@link metadata.ModelCategory <em>Model Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Category</em>'.
	 * @see metadata.ModelCategory
	 * @generated
	 */
	EClass getModelCategory();

	/**
	 * Returns the meta object for the attribute '{@link metadata.ModelCategory#getModelClass <em>Model Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Class</em>'.
	 * @see metadata.ModelCategory#getModelClass()
	 * @see #getModelCategory()
	 * @generated
	 */
	EAttribute getModelCategory_ModelClass();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.ModelCategory#getModelSubClass <em>Model Sub Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Model Sub Class</em>'.
	 * @see metadata.ModelCategory#getModelSubClass()
	 * @see #getModelCategory()
	 * @generated
	 */
	EAttribute getModelCategory_ModelSubClass();

	/**
	 * Returns the meta object for the attribute '{@link metadata.ModelCategory#getModelClassComment <em>Model Class Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Class Comment</em>'.
	 * @see metadata.ModelCategory#getModelClassComment()
	 * @see #getModelCategory()
	 * @generated
	 */
	EAttribute getModelCategory_ModelClassComment();

	/**
	 * Returns the meta object for the attribute '{@link metadata.ModelCategory#getBasicProcess <em>Basic Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Basic Process</em>'.
	 * @see metadata.ModelCategory#getBasicProcess()
	 * @see #getModelCategory()
	 * @generated
	 */
	EAttribute getModelCategory_BasicProcess();

	/**
	 * Returns the meta object for class '{@link metadata.Assay <em>Assay</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Assay</em>'.
	 * @see metadata.Assay
	 * @generated
	 */
	EClass getAssay();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getAssayName <em>Assay Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Assay Name</em>'.
	 * @see metadata.Assay#getAssayName()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_AssayName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getAssayDescription <em>Assay Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Assay Description</em>'.
	 * @see metadata.Assay#getAssayDescription()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_AssayDescription();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getPercentageOfMoisture <em>Percentage Of Moisture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Percentage Of Moisture</em>'.
	 * @see metadata.Assay#getPercentageOfMoisture()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_PercentageOfMoisture();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getPercentageOfFat <em>Percentage Of Fat</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Percentage Of Fat</em>'.
	 * @see metadata.Assay#getPercentageOfFat()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_PercentageOfFat();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getLimitOfDetection <em>Limit Of Detection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Limit Of Detection</em>'.
	 * @see metadata.Assay#getLimitOfDetection()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_LimitOfDetection();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getLimitOfQuantification <em>Limit Of Quantification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Limit Of Quantification</em>'.
	 * @see metadata.Assay#getLimitOfQuantification()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_LimitOfQuantification();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getLeftCensoredData <em>Left Censored Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left Censored Data</em>'.
	 * @see metadata.Assay#getLeftCensoredData()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_LeftCensoredData();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getRangeOfContamination <em>Range Of Contamination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Range Of Contamination</em>'.
	 * @see metadata.Assay#getRangeOfContamination()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_RangeOfContamination();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Assay#getUncertaintyValue <em>Uncertainty Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uncertainty Value</em>'.
	 * @see metadata.Assay#getUncertaintyValue()
	 * @see #getAssay()
	 * @generated
	 */
	EAttribute getAssay_UncertaintyValue();

	/**
	 * Returns the meta object for class '{@link metadata.Study <em>Study</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Study</em>'.
	 * @see metadata.Study
	 * @generated
	 */
	EClass getStudy();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyIdentifier <em>Study Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Identifier</em>'.
	 * @see metadata.Study#getStudyIdentifier()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyIdentifier();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyTitle <em>Study Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Title</em>'.
	 * @see metadata.Study#getStudyTitle()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyTitle();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyDescription <em>Study Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Description</em>'.
	 * @see metadata.Study#getStudyDescription()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyDescription();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyDesignType <em>Study Design Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Design Type</em>'.
	 * @see metadata.Study#getStudyDesignType()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyDesignType();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyAssayMeasurementType <em>Study Assay Measurement Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Assay Measurement Type</em>'.
	 * @see metadata.Study#getStudyAssayMeasurementType()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyAssayMeasurementType();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyAssayTechnologyType <em>Study Assay Technology Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Assay Technology Type</em>'.
	 * @see metadata.Study#getStudyAssayTechnologyType()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyAssayTechnologyType();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyAssayTechnologyPlatform <em>Study Assay Technology Platform</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Assay Technology Platform</em>'.
	 * @see metadata.Study#getStudyAssayTechnologyPlatform()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyAssayTechnologyPlatform();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getAccreditationProcedureForTheAssayTechnology <em>Accreditation Procedure For The Assay Technology</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Accreditation Procedure For The Assay Technology</em>'.
	 * @see metadata.Study#getAccreditationProcedureForTheAssayTechnology()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_AccreditationProcedureForTheAssayTechnology();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyProtocolName <em>Study Protocol Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Protocol Name</em>'.
	 * @see metadata.Study#getStudyProtocolName()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyProtocolName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyProtocolType <em>Study Protocol Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Protocol Type</em>'.
	 * @see metadata.Study#getStudyProtocolType()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyProtocolType();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyProtocolDescription <em>Study Protocol Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Protocol Description</em>'.
	 * @see metadata.Study#getStudyProtocolDescription()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyProtocolDescription();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyProtocolURI <em>Study Protocol URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Protocol URI</em>'.
	 * @see metadata.Study#getStudyProtocolURI()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyProtocolURI();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyProtocolVersion <em>Study Protocol Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Protocol Version</em>'.
	 * @see metadata.Study#getStudyProtocolVersion()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyProtocolVersion();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyProtocolComponentsName <em>Study Protocol Components Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Protocol Components Name</em>'.
	 * @see metadata.Study#getStudyProtocolComponentsName()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyProtocolComponentsName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Study#getStudyProtocolComponentsType <em>Study Protocol Components Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Study Protocol Components Type</em>'.
	 * @see metadata.Study#getStudyProtocolComponentsType()
	 * @see #getStudy()
	 * @generated
	 */
	EAttribute getStudy_StudyProtocolComponentsType();

	/**
	 * Returns the meta object for class '{@link metadata.DataBackground <em>Data Background</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Background</em>'.
	 * @see metadata.DataBackground
	 * @generated
	 */
	EClass getDataBackground();

	/**
	 * Returns the meta object for the containment reference '{@link metadata.DataBackground#getStudy <em>Study</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Study</em>'.
	 * @see metadata.DataBackground#getStudy()
	 * @see #getDataBackground()
	 * @generated
	 */
	EReference getDataBackground_Study();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.DataBackground#getStudysample <em>Studysample</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Studysample</em>'.
	 * @see metadata.DataBackground#getStudysample()
	 * @see #getDataBackground()
	 * @generated
	 */
	EReference getDataBackground_Studysample();

	/**
	 * Returns the meta object for the containment reference '{@link metadata.DataBackground#getDietaryassessmentmethod <em>Dietaryassessmentmethod</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Dietaryassessmentmethod</em>'.
	 * @see metadata.DataBackground#getDietaryassessmentmethod()
	 * @see #getDataBackground()
	 * @generated
	 */
	EReference getDataBackground_Dietaryassessmentmethod();

	/**
	 * Returns the meta object for the containment reference '{@link metadata.DataBackground#getLaboratory <em>Laboratory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Laboratory</em>'.
	 * @see metadata.DataBackground#getLaboratory()
	 * @see #getDataBackground()
	 * @generated
	 */
	EReference getDataBackground_Laboratory();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.DataBackground#getAssay <em>Assay</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Assay</em>'.
	 * @see metadata.DataBackground#getAssay()
	 * @see #getDataBackground()
	 * @generated
	 */
	EReference getDataBackground_Assay();

	/**
	 * Returns the meta object for class '{@link metadata.StudySample <em>Study Sample</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Study Sample</em>'.
	 * @see metadata.StudySample
	 * @generated
	 */
	EClass getStudySample();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getSampleName <em>Sample Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sample Name</em>'.
	 * @see metadata.StudySample#getSampleName()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_SampleName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getProtocolOfSampleCollection <em>Protocol Of Sample Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Protocol Of Sample Collection</em>'.
	 * @see metadata.StudySample#getProtocolOfSampleCollection()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_ProtocolOfSampleCollection();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getSamplingStrategy <em>Sampling Strategy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sampling Strategy</em>'.
	 * @see metadata.StudySample#getSamplingStrategy()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_SamplingStrategy();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getTypeOfSamplingProgram <em>Type Of Sampling Program</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Of Sampling Program</em>'.
	 * @see metadata.StudySample#getTypeOfSamplingProgram()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_TypeOfSamplingProgram();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getSamplingMethod <em>Sampling Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sampling Method</em>'.
	 * @see metadata.StudySample#getSamplingMethod()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_SamplingMethod();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getSamplingPlan <em>Sampling Plan</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sampling Plan</em>'.
	 * @see metadata.StudySample#getSamplingPlan()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_SamplingPlan();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getSamplingWeight <em>Sampling Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sampling Weight</em>'.
	 * @see metadata.StudySample#getSamplingWeight()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_SamplingWeight();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getSamplingSize <em>Sampling Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sampling Size</em>'.
	 * @see metadata.StudySample#getSamplingSize()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_SamplingSize();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getLotSizeUnit <em>Lot Size Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lot Size Unit</em>'.
	 * @see metadata.StudySample#getLotSizeUnit()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_LotSizeUnit();

	/**
	 * Returns the meta object for the attribute '{@link metadata.StudySample#getSamplingPoint <em>Sampling Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sampling Point</em>'.
	 * @see metadata.StudySample#getSamplingPoint()
	 * @see #getStudySample()
	 * @generated
	 */
	EAttribute getStudySample_SamplingPoint();

	/**
	 * Returns the meta object for class '{@link metadata.DietaryAssessmentMethod <em>Dietary Assessment Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dietary Assessment Method</em>'.
	 * @see metadata.DietaryAssessmentMethod
	 * @generated
	 */
	EClass getDietaryAssessmentMethod();

	/**
	 * Returns the meta object for the attribute '{@link metadata.DietaryAssessmentMethod#getCollectionTool <em>Collection Tool</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Collection Tool</em>'.
	 * @see metadata.DietaryAssessmentMethod#getCollectionTool()
	 * @see #getDietaryAssessmentMethod()
	 * @generated
	 */
	EAttribute getDietaryAssessmentMethod_CollectionTool();

	/**
	 * Returns the meta object for the attribute '{@link metadata.DietaryAssessmentMethod#getNumberOfNonConsecutiveOneDay <em>Number Of Non Consecutive One Day</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Non Consecutive One Day</em>'.
	 * @see metadata.DietaryAssessmentMethod#getNumberOfNonConsecutiveOneDay()
	 * @see #getDietaryAssessmentMethod()
	 * @generated
	 */
	EAttribute getDietaryAssessmentMethod_NumberOfNonConsecutiveOneDay();

	/**
	 * Returns the meta object for the attribute '{@link metadata.DietaryAssessmentMethod#getSoftwareTool <em>Software Tool</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Software Tool</em>'.
	 * @see metadata.DietaryAssessmentMethod#getSoftwareTool()
	 * @see #getDietaryAssessmentMethod()
	 * @generated
	 */
	EAttribute getDietaryAssessmentMethod_SoftwareTool();

	/**
	 * Returns the meta object for the attribute '{@link metadata.DietaryAssessmentMethod#getNumberOfFoodItems <em>Number Of Food Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Food Items</em>'.
	 * @see metadata.DietaryAssessmentMethod#getNumberOfFoodItems()
	 * @see #getDietaryAssessmentMethod()
	 * @generated
	 */
	EAttribute getDietaryAssessmentMethod_NumberOfFoodItems();

	/**
	 * Returns the meta object for the attribute '{@link metadata.DietaryAssessmentMethod#getRecordTypes <em>Record Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Record Types</em>'.
	 * @see metadata.DietaryAssessmentMethod#getRecordTypes()
	 * @see #getDietaryAssessmentMethod()
	 * @generated
	 */
	EAttribute getDietaryAssessmentMethod_RecordTypes();

	/**
	 * Returns the meta object for the attribute '{@link metadata.DietaryAssessmentMethod#getFoodDescriptors <em>Food Descriptors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Food Descriptors</em>'.
	 * @see metadata.DietaryAssessmentMethod#getFoodDescriptors()
	 * @see #getDietaryAssessmentMethod()
	 * @generated
	 */
	EAttribute getDietaryAssessmentMethod_FoodDescriptors();

	/**
	 * Returns the meta object for class '{@link metadata.ModificationDate <em>Modification Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Modification Date</em>'.
	 * @see metadata.ModificationDate
	 * @generated
	 */
	EClass getModificationDate();

	/**
	 * Returns the meta object for the attribute '{@link metadata.ModificationDate#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see metadata.ModificationDate#getValue()
	 * @see #getModificationDate()
	 * @generated
	 */
	EAttribute getModificationDate_Value();

	/**
	 * Returns the meta object for class '{@link metadata.Product <em>Product</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Product</em>'.
	 * @see metadata.Product
	 * @generated
	 */
	EClass getProduct();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getProductName <em>Product Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Product Name</em>'.
	 * @see metadata.Product#getProductName()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_ProductName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getProductDescription <em>Product Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Product Description</em>'.
	 * @see metadata.Product#getProductDescription()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_ProductDescription();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getProductUnit <em>Product Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Product Unit</em>'.
	 * @see metadata.Product#getProductUnit()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_ProductUnit();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getProductionMethod <em>Production Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Production Method</em>'.
	 * @see metadata.Product#getProductionMethod()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_ProductionMethod();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getPackaging <em>Packaging</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Packaging</em>'.
	 * @see metadata.Product#getPackaging()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_Packaging();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getProductTreatment <em>Product Treatment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Product Treatment</em>'.
	 * @see metadata.Product#getProductTreatment()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_ProductTreatment();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getOriginCountry <em>Origin Country</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin Country</em>'.
	 * @see metadata.Product#getOriginCountry()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_OriginCountry();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getOriginArea <em>Origin Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin Area</em>'.
	 * @see metadata.Product#getOriginArea()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_OriginArea();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getFisheriesArea <em>Fisheries Area</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fisheries Area</em>'.
	 * @see metadata.Product#getFisheriesArea()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_FisheriesArea();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getProductionDate <em>Production Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Production Date</em>'.
	 * @see metadata.Product#getProductionDate()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_ProductionDate();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Product#getExpiryDate <em>Expiry Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expiry Date</em>'.
	 * @see metadata.Product#getExpiryDate()
	 * @see #getProduct()
	 * @generated
	 */
	EAttribute getProduct_ExpiryDate();

	/**
	 * Returns the meta object for class '{@link metadata.Hazard <em>Hazard</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Hazard</em>'.
	 * @see metadata.Hazard
	 * @generated
	 */
	EClass getHazard();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getHazardType <em>Hazard Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hazard Type</em>'.
	 * @see metadata.Hazard#getHazardType()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_HazardType();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getHazardName <em>Hazard Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hazard Name</em>'.
	 * @see metadata.Hazard#getHazardName()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_HazardName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getHazardDescription <em>Hazard Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hazard Description</em>'.
	 * @see metadata.Hazard#getHazardDescription()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_HazardDescription();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getHazardUnit <em>Hazard Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hazard Unit</em>'.
	 * @see metadata.Hazard#getHazardUnit()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_HazardUnit();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getAdverseEffect <em>Adverse Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Adverse Effect</em>'.
	 * @see metadata.Hazard#getAdverseEffect()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_AdverseEffect();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getSourceOfContamination <em>Source Of Contamination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Of Contamination</em>'.
	 * @see metadata.Hazard#getSourceOfContamination()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_SourceOfContamination();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getBenchmarkDose <em>Benchmark Dose</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Benchmark Dose</em>'.
	 * @see metadata.Hazard#getBenchmarkDose()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_BenchmarkDose();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getMaximumResidueLimit <em>Maximum Residue Limit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum Residue Limit</em>'.
	 * @see metadata.Hazard#getMaximumResidueLimit()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_MaximumResidueLimit();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getNoObservedAdverseAffectLevel <em>No Observed Adverse Affect Level</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>No Observed Adverse Affect Level</em>'.
	 * @see metadata.Hazard#getNoObservedAdverseAffectLevel()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_NoObservedAdverseAffectLevel();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getLowestObservedAdverseAffectLevel <em>Lowest Observed Adverse Affect Level</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lowest Observed Adverse Affect Level</em>'.
	 * @see metadata.Hazard#getLowestObservedAdverseAffectLevel()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_LowestObservedAdverseAffectLevel();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getAcceptableOperatorExposureLevel <em>Acceptable Operator Exposure Level</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Acceptable Operator Exposure Level</em>'.
	 * @see metadata.Hazard#getAcceptableOperatorExposureLevel()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_AcceptableOperatorExposureLevel();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getAcuteReferenceDose <em>Acute Reference Dose</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Acute Reference Dose</em>'.
	 * @see metadata.Hazard#getAcuteReferenceDose()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_AcuteReferenceDose();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getAcceptableDailyIntake <em>Acceptable Daily Intake</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Acceptable Daily Intake</em>'.
	 * @see metadata.Hazard#getAcceptableDailyIntake()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_AcceptableDailyIntake();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Hazard#getHazardIndSum <em>Hazard Ind Sum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hazard Ind Sum</em>'.
	 * @see metadata.Hazard#getHazardIndSum()
	 * @see #getHazard()
	 * @generated
	 */
	EAttribute getHazard_HazardIndSum();

	/**
	 * Returns the meta object for class '{@link metadata.PopulationGroup <em>Population Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Population Group</em>'.
	 * @see metadata.PopulationGroup
	 * @generated
	 */
	EClass getPopulationGroup();

	/**
	 * Returns the meta object for the attribute '{@link metadata.PopulationGroup#getPopulationName <em>Population Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Population Name</em>'.
	 * @see metadata.PopulationGroup#getPopulationName()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_PopulationName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.PopulationGroup#getTargetPopulation <em>Target Population</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Population</em>'.
	 * @see metadata.PopulationGroup#getTargetPopulation()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_TargetPopulation();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getPopulationSpan <em>Population Span</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Population Span</em>'.
	 * @see metadata.PopulationGroup#getPopulationSpan()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_PopulationSpan();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getPopulationDescription <em>Population Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Population Description</em>'.
	 * @see metadata.PopulationGroup#getPopulationDescription()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_PopulationDescription();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getPopulationAge <em>Population Age</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Population Age</em>'.
	 * @see metadata.PopulationGroup#getPopulationAge()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_PopulationAge();

	/**
	 * Returns the meta object for the attribute '{@link metadata.PopulationGroup#getPopulationGender <em>Population Gender</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Population Gender</em>'.
	 * @see metadata.PopulationGroup#getPopulationGender()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_PopulationGender();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getBmi <em>Bmi</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Bmi</em>'.
	 * @see metadata.PopulationGroup#getBmi()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_Bmi();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getSpecialDietGroups <em>Special Diet Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Special Diet Groups</em>'.
	 * @see metadata.PopulationGroup#getSpecialDietGroups()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_SpecialDietGroups();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getPatternConsumption <em>Pattern Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Pattern Consumption</em>'.
	 * @see metadata.PopulationGroup#getPatternConsumption()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_PatternConsumption();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getRegion <em>Region</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Region</em>'.
	 * @see metadata.PopulationGroup#getRegion()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_Region();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getCountry <em>Country</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Country</em>'.
	 * @see metadata.PopulationGroup#getCountry()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_Country();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getPopulationRiskFactor <em>Population Risk Factor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Population Risk Factor</em>'.
	 * @see metadata.PopulationGroup#getPopulationRiskFactor()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_PopulationRiskFactor();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.PopulationGroup#getSeason <em>Season</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Season</em>'.
	 * @see metadata.PopulationGroup#getSeason()
	 * @see #getPopulationGroup()
	 * @generated
	 */
	EAttribute getPopulationGroup_Season();

	/**
	 * Returns the meta object for class '{@link metadata.Scope <em>Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scope</em>'.
	 * @see metadata.Scope
	 * @generated
	 */
	EClass getScope();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Scope#getGeneralComment <em>General Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>General Comment</em>'.
	 * @see metadata.Scope#getGeneralComment()
	 * @see #getScope()
	 * @generated
	 */
	EAttribute getScope_GeneralComment();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Scope#getTemporalInformation <em>Temporal Information</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Temporal Information</em>'.
	 * @see metadata.Scope#getTemporalInformation()
	 * @see #getScope()
	 * @generated
	 */
	EAttribute getScope_TemporalInformation();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.Scope#getProduct <em>Product</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Product</em>'.
	 * @see metadata.Scope#getProduct()
	 * @see #getScope()
	 * @generated
	 */
	EReference getScope_Product();

	/**
	 * Returns the meta object for the reference list '{@link metadata.Scope#getHazard <em>Hazard</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Hazard</em>'.
	 * @see metadata.Scope#getHazard()
	 * @see #getScope()
	 * @generated
	 */
	EReference getScope_Hazard();

	/**
	 * Returns the meta object for the reference '{@link metadata.Scope#getPopulationgroup <em>Populationgroup</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Populationgroup</em>'.
	 * @see metadata.Scope#getPopulationgroup()
	 * @see #getScope()
	 * @generated
	 */
	EReference getScope_Populationgroup();

	/**
	 * Returns the meta object for the containment reference '{@link metadata.Scope#getSpatialInformation <em>Spatial Information</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Spatial Information</em>'.
	 * @see metadata.Scope#getSpatialInformation()
	 * @see #getScope()
	 * @generated
	 */
	EReference getScope_SpatialInformation();

	/**
	 * Returns the meta object for class '{@link metadata.Laboratory <em>Laboratory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Laboratory</em>'.
	 * @see metadata.Laboratory
	 * @generated
	 */
	EClass getLaboratory();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.Laboratory#getLaboratoryAccreditation <em>Laboratory Accreditation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Laboratory Accreditation</em>'.
	 * @see metadata.Laboratory#getLaboratoryAccreditation()
	 * @see #getLaboratory()
	 * @generated
	 */
	EAttribute getLaboratory_LaboratoryAccreditation();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Laboratory#getLaboratoryName <em>Laboratory Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laboratory Name</em>'.
	 * @see metadata.Laboratory#getLaboratoryName()
	 * @see #getLaboratory()
	 * @generated
	 */
	EAttribute getLaboratory_LaboratoryName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Laboratory#getLaboratoryCountry <em>Laboratory Country</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Laboratory Country</em>'.
	 * @see metadata.Laboratory#getLaboratoryCountry()
	 * @see #getLaboratory()
	 * @generated
	 */
	EAttribute getLaboratory_LaboratoryCountry();

	/**
	 * Returns the meta object for class '{@link metadata.SpatialInformation <em>Spatial Information</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spatial Information</em>'.
	 * @see metadata.SpatialInformation
	 * @generated
	 */
	EClass getSpatialInformation();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.SpatialInformation#getRegion <em>Region</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Region</em>'.
	 * @see metadata.SpatialInformation#getRegion()
	 * @see #getSpatialInformation()
	 * @generated
	 */
	EAttribute getSpatialInformation_Region();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.SpatialInformation#getCountry <em>Country</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Country</em>'.
	 * @see metadata.SpatialInformation#getCountry()
	 * @see #getSpatialInformation()
	 * @generated
	 */
	EAttribute getSpatialInformation_Country();

	/**
	 * Returns the meta object for class '{@link metadata.modelApplicability <em>model Applicability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>model Applicability</em>'.
	 * @see metadata.modelApplicability
	 * @generated
	 */
	EClass getmodelApplicability();

	/**
	 * Returns the meta object for the attribute '{@link metadata.modelApplicability#getModelApplicability <em>Model Applicability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Applicability</em>'.
	 * @see metadata.modelApplicability#getModelApplicability()
	 * @see #getmodelApplicability()
	 * @generated
	 */
	EAttribute getmodelApplicability_ModelApplicability();

	/**
	 * Returns the meta object for class '{@link metadata.Event <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event</em>'.
	 * @see metadata.Event
	 * @generated
	 */
	EClass getEvent();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Event#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Event</em>'.
	 * @see metadata.Event#getEvent()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_Event();

	/**
	 * Returns the meta object for class '{@link metadata.Contact <em>Contact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Contact</em>'.
	 * @see metadata.Contact
	 * @generated
	 */
	EClass getContact();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see metadata.Contact#getTitle()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Title();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getFamilyName <em>Family Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Family Name</em>'.
	 * @see metadata.Contact#getFamilyName()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_FamilyName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getEmail <em>Email</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Email</em>'.
	 * @see metadata.Contact#getEmail()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Email();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getTelephone <em>Telephone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Telephone</em>'.
	 * @see metadata.Contact#getTelephone()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Telephone();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getStreetAddress <em>Street Address</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Street Address</em>'.
	 * @see metadata.Contact#getStreetAddress()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_StreetAddress();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getCountry <em>Country</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Country</em>'.
	 * @see metadata.Contact#getCountry()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Country();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getCity <em>City</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>City</em>'.
	 * @see metadata.Contact#getCity()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_City();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getZipCode <em>Zip Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Zip Code</em>'.
	 * @see metadata.Contact#getZipCode()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_ZipCode();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getPostOfficeBox <em>Post Office Box</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Post Office Box</em>'.
	 * @see metadata.Contact#getPostOfficeBox()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_PostOfficeBox();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getRegion <em>Region</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Region</em>'.
	 * @see metadata.Contact#getRegion()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Region();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getNickname <em>Nickname</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nickname</em>'.
	 * @see metadata.Contact#getNickname()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Nickname();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getTimeZone <em>Time Zone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Zone</em>'.
	 * @see metadata.Contact#getTimeZone()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_TimeZone();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getGender <em>Gender</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Gender</em>'.
	 * @see metadata.Contact#getGender()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Gender();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see metadata.Contact#getName()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Name();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getUrl <em>Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Url</em>'.
	 * @see metadata.Contact#getUrl()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Url();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getNote <em>Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Note</em>'.
	 * @see metadata.Contact#getNote()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Note();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getLogo <em>Logo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Logo</em>'.
	 * @see metadata.Contact#getLogo()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Logo();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getOrganization <em>Organization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Organization</em>'.
	 * @see metadata.Contact#getOrganization()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Organization();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Contact#getFn <em>Fn</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fn</em>'.
	 * @see metadata.Contact#getFn()
	 * @see #getContact()
	 * @generated
	 */
	EAttribute getContact_Fn();

	/**
	 * Returns the meta object for class '{@link metadata.Reference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference</em>'.
	 * @see metadata.Reference
	 * @generated
	 */
	EClass getReference();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#isIsReferenceDescription <em>Is Reference Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Reference Description</em>'.
	 * @see metadata.Reference#isIsReferenceDescription()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_IsReferenceDescription();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationType <em>Publication Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Type</em>'.
	 * @see metadata.Reference#getPublicationType()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationType();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationDate <em>Publication Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Date</em>'.
	 * @see metadata.Reference#getPublicationDate()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationDate();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPmid <em>Pmid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pmid</em>'.
	 * @see metadata.Reference#getPmid()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_Pmid();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getDoi <em>Doi</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Doi</em>'.
	 * @see metadata.Reference#getDoi()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_Doi();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getAuthorList <em>Author List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Author List</em>'.
	 * @see metadata.Reference#getAuthorList()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_AuthorList();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationTitle <em>Publication Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Title</em>'.
	 * @see metadata.Reference#getPublicationTitle()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationTitle();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationAbstract <em>Publication Abstract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Abstract</em>'.
	 * @see metadata.Reference#getPublicationAbstract()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationAbstract();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationJournal <em>Publication Journal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Journal</em>'.
	 * @see metadata.Reference#getPublicationJournal()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationJournal();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationVolume <em>Publication Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Volume</em>'.
	 * @see metadata.Reference#getPublicationVolume()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationVolume();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationIssue <em>Publication Issue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Issue</em>'.
	 * @see metadata.Reference#getPublicationIssue()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationIssue();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationStatus <em>Publication Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Status</em>'.
	 * @see metadata.Reference#getPublicationStatus()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationStatus();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getPublicationWebsite <em>Publication Website</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Publication Website</em>'.
	 * @see metadata.Reference#getPublicationWebsite()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_PublicationWebsite();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Reference#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see metadata.Reference#getComment()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_Comment();

	/**
	 * Returns the meta object for class '{@link metadata.ModelMath <em>Model Math</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Math</em>'.
	 * @see metadata.ModelMath
	 * @generated
	 */
	EClass getModelMath();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.ModelMath#getQualityMeasures <em>Quality Measures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Quality Measures</em>'.
	 * @see metadata.ModelMath#getQualityMeasures()
	 * @see #getModelMath()
	 * @generated
	 */
	EAttribute getModelMath_QualityMeasures();

	/**
	 * Returns the meta object for the attribute '{@link metadata.ModelMath#getFittingProcedure <em>Fitting Procedure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fitting Procedure</em>'.
	 * @see metadata.ModelMath#getFittingProcedure()
	 * @see #getModelMath()
	 * @generated
	 */
	EAttribute getModelMath_FittingProcedure();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.ModelMath#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Event</em>'.
	 * @see metadata.ModelMath#getEvent()
	 * @see #getModelMath()
	 * @generated
	 */
	EAttribute getModelMath_Event();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.ModelMath#getParameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameter</em>'.
	 * @see metadata.ModelMath#getParameter()
	 * @see #getModelMath()
	 * @generated
	 */
	EReference getModelMath_Parameter();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.ModelMath#getModelequation <em>Modelequation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modelequation</em>'.
	 * @see metadata.ModelMath#getModelequation()
	 * @see #getModelMath()
	 * @generated
	 */
	EReference getModelMath_Modelequation();

	/**
	 * Returns the meta object for the containment reference '{@link metadata.ModelMath#getExposure <em>Exposure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Exposure</em>'.
	 * @see metadata.ModelMath#getExposure()
	 * @see #getModelMath()
	 * @generated
	 */
	EReference getModelMath_Exposure();

	/**
	 * Returns the meta object for class '{@link metadata.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see metadata.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterID <em>Parameter ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter ID</em>'.
	 * @see metadata.Parameter#getParameterID()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterID();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterClassification <em>Parameter Classification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Classification</em>'.
	 * @see metadata.Parameter#getParameterClassification()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterClassification();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterName <em>Parameter Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Name</em>'.
	 * @see metadata.Parameter#getParameterName()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterDescription <em>Parameter Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Description</em>'.
	 * @see metadata.Parameter#getParameterDescription()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterDescription();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterType <em>Parameter Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Type</em>'.
	 * @see metadata.Parameter#getParameterType()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterType();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterUnit <em>Parameter Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Unit</em>'.
	 * @see metadata.Parameter#getParameterUnit()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterUnit();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterUnitCategory <em>Parameter Unit Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Unit Category</em>'.
	 * @see metadata.Parameter#getParameterUnitCategory()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterUnitCategory();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterDataType <em>Parameter Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Data Type</em>'.
	 * @see metadata.Parameter#getParameterDataType()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterDataType();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterSource <em>Parameter Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Source</em>'.
	 * @see metadata.Parameter#getParameterSource()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterSource();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterSubject <em>Parameter Subject</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Subject</em>'.
	 * @see metadata.Parameter#getParameterSubject()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterSubject();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterDistribution <em>Parameter Distribution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Distribution</em>'.
	 * @see metadata.Parameter#getParameterDistribution()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterDistribution();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterValue <em>Parameter Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Value</em>'.
	 * @see metadata.Parameter#getParameterValue()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterValue();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterVariabilitySubject <em>Parameter Variability Subject</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Variability Subject</em>'.
	 * @see metadata.Parameter#getParameterVariabilitySubject()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterVariabilitySubject();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterValueMin <em>Parameter Value Min</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Value Min</em>'.
	 * @see metadata.Parameter#getParameterValueMin()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterValueMin();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterValueMax <em>Parameter Value Max</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Value Max</em>'.
	 * @see metadata.Parameter#getParameterValueMax()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterValueMax();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Parameter#getParameterError <em>Parameter Error</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parameter Error</em>'.
	 * @see metadata.Parameter#getParameterError()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_ParameterError();

	/**
	 * Returns the meta object for the reference '{@link metadata.Parameter#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see metadata.Parameter#getReference()
	 * @see #getParameter()
	 * @generated
	 */
	EReference getParameter_Reference();

	/**
	 * Returns the meta object for class '{@link metadata.ModelEquation <em>Model Equation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Equation</em>'.
	 * @see metadata.ModelEquation
	 * @generated
	 */
	EClass getModelEquation();

	/**
	 * Returns the meta object for the attribute '{@link metadata.ModelEquation#getModelEquationName <em>Model Equation Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Equation Name</em>'.
	 * @see metadata.ModelEquation#getModelEquationName()
	 * @see #getModelEquation()
	 * @generated
	 */
	EAttribute getModelEquation_ModelEquationName();

	/**
	 * Returns the meta object for the attribute '{@link metadata.ModelEquation#getModelEquationClass <em>Model Equation Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Equation Class</em>'.
	 * @see metadata.ModelEquation#getModelEquationClass()
	 * @see #getModelEquation()
	 * @generated
	 */
	EAttribute getModelEquation_ModelEquationClass();

	/**
	 * Returns the meta object for the attribute '{@link metadata.ModelEquation#getModelEquation <em>Model Equation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Equation</em>'.
	 * @see metadata.ModelEquation#getModelEquation()
	 * @see #getModelEquation()
	 * @generated
	 */
	EAttribute getModelEquation_ModelEquation();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.ModelEquation#getHypothesisOfTheModel <em>Hypothesis Of The Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Hypothesis Of The Model</em>'.
	 * @see metadata.ModelEquation#getHypothesisOfTheModel()
	 * @see #getModelEquation()
	 * @generated
	 */
	EAttribute getModelEquation_HypothesisOfTheModel();

	/**
	 * Returns the meta object for the containment reference list '{@link metadata.ModelEquation#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Reference</em>'.
	 * @see metadata.ModelEquation#getReference()
	 * @see #getModelEquation()
	 * @generated
	 */
	EReference getModelEquation_Reference();

	/**
	 * Returns the meta object for class '{@link metadata.Exposure <em>Exposure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exposure</em>'.
	 * @see metadata.Exposure
	 * @generated
	 */
	EClass getExposure();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.Exposure#getMethodologicalTreatmentOfLeftCensoredData <em>Methodological Treatment Of Left Censored Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Methodological Treatment Of Left Censored Data</em>'.
	 * @see metadata.Exposure#getMethodologicalTreatmentOfLeftCensoredData()
	 * @see #getExposure()
	 * @generated
	 */
	EAttribute getExposure_MethodologicalTreatmentOfLeftCensoredData();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.Exposure#getLevelOfContaminationAfterLeftCensoredDataTreatment <em>Level Of Contamination After Left Censored Data Treatment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Level Of Contamination After Left Censored Data Treatment</em>'.
	 * @see metadata.Exposure#getLevelOfContaminationAfterLeftCensoredDataTreatment()
	 * @see #getExposure()
	 * @generated
	 */
	EAttribute getExposure_LevelOfContaminationAfterLeftCensoredDataTreatment();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Exposure#getTypeOfExposure <em>Type Of Exposure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Of Exposure</em>'.
	 * @see metadata.Exposure#getTypeOfExposure()
	 * @see #getExposure()
	 * @generated
	 */
	EAttribute getExposure_TypeOfExposure();

	/**
	 * Returns the meta object for the attribute list '{@link metadata.Exposure#getScenario <em>Scenario</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Scenario</em>'.
	 * @see metadata.Exposure#getScenario()
	 * @see #getExposure()
	 * @generated
	 */
	EAttribute getExposure_Scenario();

	/**
	 * Returns the meta object for the attribute '{@link metadata.Exposure#getUncertaintyEstimation <em>Uncertainty Estimation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uncertainty Estimation</em>'.
	 * @see metadata.Exposure#getUncertaintyEstimation()
	 * @see #getExposure()
	 * @generated
	 */
	EAttribute getExposure_UncertaintyEstimation();

	/**
	 * Returns the meta object for enum '{@link metadata.PublicationType <em>Publication Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Publication Type</em>'.
	 * @see metadata.PublicationType
	 * @generated
	 */
	EEnum getPublicationType();

	/**
	 * Returns the meta object for enum '{@link metadata.ParameterClassification <em>Parameter Classification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Parameter Classification</em>'.
	 * @see metadata.ParameterClassification
	 * @generated
	 */
	EEnum getParameterClassification();

	/**
	 * Returns the meta object for enum '{@link metadata.ParameterType <em>Parameter Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Parameter Type</em>'.
	 * @see metadata.ParameterType
	 * @generated
	 */
	EEnum getParameterType();

	/**
	 * Returns the meta object for data type '{@link java.net.URI <em>URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>URI</em>'.
	 * @see java.net.URI
	 * @model instanceClass="java.net.URI"
	 * @generated
	 */
	EDataType getURI();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MetadataFactory getMetadataFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link metadata.impl.GeneralInformationImpl <em>General Information</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.GeneralInformationImpl
		 * @see metadata.impl.MetadataPackageImpl#getGeneralInformation()
		 * @generated
		 */
		EClass GENERAL_INFORMATION = eINSTANCE.getGeneralInformation();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__NAME = eINSTANCE.getGeneralInformation_Name();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__SOURCE = eINSTANCE.getGeneralInformation_Source();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__IDENTIFIER = eINSTANCE.getGeneralInformation_Identifier();

		/**
		 * The meta object literal for the '<em><b>Creation Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__CREATION_DATE = eINSTANCE.getGeneralInformation_CreationDate();

		/**
		 * The meta object literal for the '<em><b>Rights</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__RIGHTS = eINSTANCE.getGeneralInformation_Rights();

		/**
		 * The meta object literal for the '<em><b>Available</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__AVAILABLE = eINSTANCE.getGeneralInformation_Available();

		/**
		 * The meta object literal for the '<em><b>Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__FORMAT = eINSTANCE.getGeneralInformation_Format();

		/**
		 * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__LANGUAGE = eINSTANCE.getGeneralInformation_Language();

		/**
		 * The meta object literal for the '<em><b>Software</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__SOFTWARE = eINSTANCE.getGeneralInformation_Software();

		/**
		 * The meta object literal for the '<em><b>Language Written In</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN = eINSTANCE.getGeneralInformation_LanguageWrittenIn();

		/**
		 * The meta object literal for the '<em><b>Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__STATUS = eINSTANCE.getGeneralInformation_Status();

		/**
		 * The meta object literal for the '<em><b>Objective</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__OBJECTIVE = eINSTANCE.getGeneralInformation_Objective();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERAL_INFORMATION__DESCRIPTION = eINSTANCE.getGeneralInformation_Description();

		/**
		 * The meta object literal for the '<em><b>Modelcategory</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERAL_INFORMATION__MODELCATEGORY = eINSTANCE.getGeneralInformation_Modelcategory();

		/**
		 * The meta object literal for the '<em><b>Modificationdate</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERAL_INFORMATION__MODIFICATIONDATE = eINSTANCE.getGeneralInformation_Modificationdate();

		/**
		 * The meta object literal for the '<em><b>Author</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERAL_INFORMATION__AUTHOR = eINSTANCE.getGeneralInformation_Author();

		/**
		 * The meta object literal for the '<em><b>Creators</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERAL_INFORMATION__CREATORS = eINSTANCE.getGeneralInformation_Creators();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GENERAL_INFORMATION__REFERENCE = eINSTANCE.getGeneralInformation_Reference();

		/**
		 * The meta object literal for the '<em><b>Equals</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation GENERAL_INFORMATION___EQUALS__GENERALINFORMATION = eINSTANCE.getGeneralInformation__Equals__GeneralInformation();

		/**
		 * The meta object literal for the '{@link metadata.impl.ModelCategoryImpl <em>Model Category</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ModelCategoryImpl
		 * @see metadata.impl.MetadataPackageImpl#getModelCategory()
		 * @generated
		 */
		EClass MODEL_CATEGORY = eINSTANCE.getModelCategory();

		/**
		 * The meta object literal for the '<em><b>Model Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_CATEGORY__MODEL_CLASS = eINSTANCE.getModelCategory_ModelClass();

		/**
		 * The meta object literal for the '<em><b>Model Sub Class</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_CATEGORY__MODEL_SUB_CLASS = eINSTANCE.getModelCategory_ModelSubClass();

		/**
		 * The meta object literal for the '<em><b>Model Class Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_CATEGORY__MODEL_CLASS_COMMENT = eINSTANCE.getModelCategory_ModelClassComment();

		/**
		 * The meta object literal for the '<em><b>Basic Process</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_CATEGORY__BASIC_PROCESS = eINSTANCE.getModelCategory_BasicProcess();

		/**
		 * The meta object literal for the '{@link metadata.impl.AssayImpl <em>Assay</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.AssayImpl
		 * @see metadata.impl.MetadataPackageImpl#getAssay()
		 * @generated
		 */
		EClass ASSAY = eINSTANCE.getAssay();

		/**
		 * The meta object literal for the '<em><b>Assay Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__ASSAY_NAME = eINSTANCE.getAssay_AssayName();

		/**
		 * The meta object literal for the '<em><b>Assay Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__ASSAY_DESCRIPTION = eINSTANCE.getAssay_AssayDescription();

		/**
		 * The meta object literal for the '<em><b>Percentage Of Moisture</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__PERCENTAGE_OF_MOISTURE = eINSTANCE.getAssay_PercentageOfMoisture();

		/**
		 * The meta object literal for the '<em><b>Percentage Of Fat</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__PERCENTAGE_OF_FAT = eINSTANCE.getAssay_PercentageOfFat();

		/**
		 * The meta object literal for the '<em><b>Limit Of Detection</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__LIMIT_OF_DETECTION = eINSTANCE.getAssay_LimitOfDetection();

		/**
		 * The meta object literal for the '<em><b>Limit Of Quantification</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__LIMIT_OF_QUANTIFICATION = eINSTANCE.getAssay_LimitOfQuantification();

		/**
		 * The meta object literal for the '<em><b>Left Censored Data</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__LEFT_CENSORED_DATA = eINSTANCE.getAssay_LeftCensoredData();

		/**
		 * The meta object literal for the '<em><b>Range Of Contamination</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__RANGE_OF_CONTAMINATION = eINSTANCE.getAssay_RangeOfContamination();

		/**
		 * The meta object literal for the '<em><b>Uncertainty Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSAY__UNCERTAINTY_VALUE = eINSTANCE.getAssay_UncertaintyValue();

		/**
		 * The meta object literal for the '{@link metadata.impl.StudyImpl <em>Study</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.StudyImpl
		 * @see metadata.impl.MetadataPackageImpl#getStudy()
		 * @generated
		 */
		EClass STUDY = eINSTANCE.getStudy();

		/**
		 * The meta object literal for the '<em><b>Study Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_IDENTIFIER = eINSTANCE.getStudy_StudyIdentifier();

		/**
		 * The meta object literal for the '<em><b>Study Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_TITLE = eINSTANCE.getStudy_StudyTitle();

		/**
		 * The meta object literal for the '<em><b>Study Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_DESCRIPTION = eINSTANCE.getStudy_StudyDescription();

		/**
		 * The meta object literal for the '<em><b>Study Design Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_DESIGN_TYPE = eINSTANCE.getStudy_StudyDesignType();

		/**
		 * The meta object literal for the '<em><b>Study Assay Measurement Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_ASSAY_MEASUREMENT_TYPE = eINSTANCE.getStudy_StudyAssayMeasurementType();

		/**
		 * The meta object literal for the '<em><b>Study Assay Technology Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE = eINSTANCE.getStudy_StudyAssayTechnologyType();

		/**
		 * The meta object literal for the '<em><b>Study Assay Technology Platform</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM = eINSTANCE.getStudy_StudyAssayTechnologyPlatform();

		/**
		 * The meta object literal for the '<em><b>Accreditation Procedure For The Assay Technology</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY = eINSTANCE.getStudy_AccreditationProcedureForTheAssayTechnology();

		/**
		 * The meta object literal for the '<em><b>Study Protocol Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_PROTOCOL_NAME = eINSTANCE.getStudy_StudyProtocolName();

		/**
		 * The meta object literal for the '<em><b>Study Protocol Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_PROTOCOL_TYPE = eINSTANCE.getStudy_StudyProtocolType();

		/**
		 * The meta object literal for the '<em><b>Study Protocol Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_PROTOCOL_DESCRIPTION = eINSTANCE.getStudy_StudyProtocolDescription();

		/**
		 * The meta object literal for the '<em><b>Study Protocol URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_PROTOCOL_URI = eINSTANCE.getStudy_StudyProtocolURI();

		/**
		 * The meta object literal for the '<em><b>Study Protocol Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_PROTOCOL_VERSION = eINSTANCE.getStudy_StudyProtocolVersion();

		/**
		 * The meta object literal for the '<em><b>Study Protocol Components Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_PROTOCOL_COMPONENTS_NAME = eINSTANCE.getStudy_StudyProtocolComponentsName();

		/**
		 * The meta object literal for the '<em><b>Study Protocol Components Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE = eINSTANCE.getStudy_StudyProtocolComponentsType();

		/**
		 * The meta object literal for the '{@link metadata.impl.DataBackgroundImpl <em>Data Background</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.DataBackgroundImpl
		 * @see metadata.impl.MetadataPackageImpl#getDataBackground()
		 * @generated
		 */
		EClass DATA_BACKGROUND = eINSTANCE.getDataBackground();

		/**
		 * The meta object literal for the '<em><b>Study</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_BACKGROUND__STUDY = eINSTANCE.getDataBackground_Study();

		/**
		 * The meta object literal for the '<em><b>Studysample</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_BACKGROUND__STUDYSAMPLE = eINSTANCE.getDataBackground_Studysample();

		/**
		 * The meta object literal for the '<em><b>Dietaryassessmentmethod</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_BACKGROUND__DIETARYASSESSMENTMETHOD = eINSTANCE.getDataBackground_Dietaryassessmentmethod();

		/**
		 * The meta object literal for the '<em><b>Laboratory</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_BACKGROUND__LABORATORY = eINSTANCE.getDataBackground_Laboratory();

		/**
		 * The meta object literal for the '<em><b>Assay</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_BACKGROUND__ASSAY = eINSTANCE.getDataBackground_Assay();

		/**
		 * The meta object literal for the '{@link metadata.impl.StudySampleImpl <em>Study Sample</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.StudySampleImpl
		 * @see metadata.impl.MetadataPackageImpl#getStudySample()
		 * @generated
		 */
		EClass STUDY_SAMPLE = eINSTANCE.getStudySample();

		/**
		 * The meta object literal for the '<em><b>Sample Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__SAMPLE_NAME = eINSTANCE.getStudySample_SampleName();

		/**
		 * The meta object literal for the '<em><b>Protocol Of Sample Collection</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__PROTOCOL_OF_SAMPLE_COLLECTION = eINSTANCE.getStudySample_ProtocolOfSampleCollection();

		/**
		 * The meta object literal for the '<em><b>Sampling Strategy</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__SAMPLING_STRATEGY = eINSTANCE.getStudySample_SamplingStrategy();

		/**
		 * The meta object literal for the '<em><b>Type Of Sampling Program</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__TYPE_OF_SAMPLING_PROGRAM = eINSTANCE.getStudySample_TypeOfSamplingProgram();

		/**
		 * The meta object literal for the '<em><b>Sampling Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__SAMPLING_METHOD = eINSTANCE.getStudySample_SamplingMethod();

		/**
		 * The meta object literal for the '<em><b>Sampling Plan</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__SAMPLING_PLAN = eINSTANCE.getStudySample_SamplingPlan();

		/**
		 * The meta object literal for the '<em><b>Sampling Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__SAMPLING_WEIGHT = eINSTANCE.getStudySample_SamplingWeight();

		/**
		 * The meta object literal for the '<em><b>Sampling Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__SAMPLING_SIZE = eINSTANCE.getStudySample_SamplingSize();

		/**
		 * The meta object literal for the '<em><b>Lot Size Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__LOT_SIZE_UNIT = eINSTANCE.getStudySample_LotSizeUnit();

		/**
		 * The meta object literal for the '<em><b>Sampling Point</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STUDY_SAMPLE__SAMPLING_POINT = eINSTANCE.getStudySample_SamplingPoint();

		/**
		 * The meta object literal for the '{@link metadata.impl.DietaryAssessmentMethodImpl <em>Dietary Assessment Method</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.DietaryAssessmentMethodImpl
		 * @see metadata.impl.MetadataPackageImpl#getDietaryAssessmentMethod()
		 * @generated
		 */
		EClass DIETARY_ASSESSMENT_METHOD = eINSTANCE.getDietaryAssessmentMethod();

		/**
		 * The meta object literal for the '<em><b>Collection Tool</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIETARY_ASSESSMENT_METHOD__COLLECTION_TOOL = eINSTANCE.getDietaryAssessmentMethod_CollectionTool();

		/**
		 * The meta object literal for the '<em><b>Number Of Non Consecutive One Day</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIETARY_ASSESSMENT_METHOD__NUMBER_OF_NON_CONSECUTIVE_ONE_DAY = eINSTANCE.getDietaryAssessmentMethod_NumberOfNonConsecutiveOneDay();

		/**
		 * The meta object literal for the '<em><b>Software Tool</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIETARY_ASSESSMENT_METHOD__SOFTWARE_TOOL = eINSTANCE.getDietaryAssessmentMethod_SoftwareTool();

		/**
		 * The meta object literal for the '<em><b>Number Of Food Items</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIETARY_ASSESSMENT_METHOD__NUMBER_OF_FOOD_ITEMS = eINSTANCE.getDietaryAssessmentMethod_NumberOfFoodItems();

		/**
		 * The meta object literal for the '<em><b>Record Types</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIETARY_ASSESSMENT_METHOD__RECORD_TYPES = eINSTANCE.getDietaryAssessmentMethod_RecordTypes();

		/**
		 * The meta object literal for the '<em><b>Food Descriptors</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIETARY_ASSESSMENT_METHOD__FOOD_DESCRIPTORS = eINSTANCE.getDietaryAssessmentMethod_FoodDescriptors();

		/**
		 * The meta object literal for the '{@link metadata.impl.ModificationDateImpl <em>Modification Date</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ModificationDateImpl
		 * @see metadata.impl.MetadataPackageImpl#getModificationDate()
		 * @generated
		 */
		EClass MODIFICATION_DATE = eINSTANCE.getModificationDate();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODIFICATION_DATE__VALUE = eINSTANCE.getModificationDate_Value();

		/**
		 * The meta object literal for the '{@link metadata.impl.ProductImpl <em>Product</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ProductImpl
		 * @see metadata.impl.MetadataPackageImpl#getProduct()
		 * @generated
		 */
		EClass PRODUCT = eINSTANCE.getProduct();

		/**
		 * The meta object literal for the '<em><b>Product Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__PRODUCT_NAME = eINSTANCE.getProduct_ProductName();

		/**
		 * The meta object literal for the '<em><b>Product Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__PRODUCT_DESCRIPTION = eINSTANCE.getProduct_ProductDescription();

		/**
		 * The meta object literal for the '<em><b>Product Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__PRODUCT_UNIT = eINSTANCE.getProduct_ProductUnit();

		/**
		 * The meta object literal for the '<em><b>Production Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__PRODUCTION_METHOD = eINSTANCE.getProduct_ProductionMethod();

		/**
		 * The meta object literal for the '<em><b>Packaging</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__PACKAGING = eINSTANCE.getProduct_Packaging();

		/**
		 * The meta object literal for the '<em><b>Product Treatment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__PRODUCT_TREATMENT = eINSTANCE.getProduct_ProductTreatment();

		/**
		 * The meta object literal for the '<em><b>Origin Country</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__ORIGIN_COUNTRY = eINSTANCE.getProduct_OriginCountry();

		/**
		 * The meta object literal for the '<em><b>Origin Area</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__ORIGIN_AREA = eINSTANCE.getProduct_OriginArea();

		/**
		 * The meta object literal for the '<em><b>Fisheries Area</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__FISHERIES_AREA = eINSTANCE.getProduct_FisheriesArea();

		/**
		 * The meta object literal for the '<em><b>Production Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__PRODUCTION_DATE = eINSTANCE.getProduct_ProductionDate();

		/**
		 * The meta object literal for the '<em><b>Expiry Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRODUCT__EXPIRY_DATE = eINSTANCE.getProduct_ExpiryDate();

		/**
		 * The meta object literal for the '{@link metadata.impl.HazardImpl <em>Hazard</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.HazardImpl
		 * @see metadata.impl.MetadataPackageImpl#getHazard()
		 * @generated
		 */
		EClass HAZARD = eINSTANCE.getHazard();

		/**
		 * The meta object literal for the '<em><b>Hazard Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__HAZARD_TYPE = eINSTANCE.getHazard_HazardType();

		/**
		 * The meta object literal for the '<em><b>Hazard Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__HAZARD_NAME = eINSTANCE.getHazard_HazardName();

		/**
		 * The meta object literal for the '<em><b>Hazard Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__HAZARD_DESCRIPTION = eINSTANCE.getHazard_HazardDescription();

		/**
		 * The meta object literal for the '<em><b>Hazard Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__HAZARD_UNIT = eINSTANCE.getHazard_HazardUnit();

		/**
		 * The meta object literal for the '<em><b>Adverse Effect</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__ADVERSE_EFFECT = eINSTANCE.getHazard_AdverseEffect();

		/**
		 * The meta object literal for the '<em><b>Source Of Contamination</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__SOURCE_OF_CONTAMINATION = eINSTANCE.getHazard_SourceOfContamination();

		/**
		 * The meta object literal for the '<em><b>Benchmark Dose</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__BENCHMARK_DOSE = eINSTANCE.getHazard_BenchmarkDose();

		/**
		 * The meta object literal for the '<em><b>Maximum Residue Limit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__MAXIMUM_RESIDUE_LIMIT = eINSTANCE.getHazard_MaximumResidueLimit();

		/**
		 * The meta object literal for the '<em><b>No Observed Adverse Affect Level</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__NO_OBSERVED_ADVERSE_AFFECT_LEVEL = eINSTANCE.getHazard_NoObservedAdverseAffectLevel();

		/**
		 * The meta object literal for the '<em><b>Lowest Observed Adverse Affect Level</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL = eINSTANCE.getHazard_LowestObservedAdverseAffectLevel();

		/**
		 * The meta object literal for the '<em><b>Acceptable Operator Exposure Level</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL = eINSTANCE.getHazard_AcceptableOperatorExposureLevel();

		/**
		 * The meta object literal for the '<em><b>Acute Reference Dose</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__ACUTE_REFERENCE_DOSE = eINSTANCE.getHazard_AcuteReferenceDose();

		/**
		 * The meta object literal for the '<em><b>Acceptable Daily Intake</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__ACCEPTABLE_DAILY_INTAKE = eINSTANCE.getHazard_AcceptableDailyIntake();

		/**
		 * The meta object literal for the '<em><b>Hazard Ind Sum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HAZARD__HAZARD_IND_SUM = eINSTANCE.getHazard_HazardIndSum();

		/**
		 * The meta object literal for the '{@link metadata.impl.PopulationGroupImpl <em>Population Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.PopulationGroupImpl
		 * @see metadata.impl.MetadataPackageImpl#getPopulationGroup()
		 * @generated
		 */
		EClass POPULATION_GROUP = eINSTANCE.getPopulationGroup();

		/**
		 * The meta object literal for the '<em><b>Population Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__POPULATION_NAME = eINSTANCE.getPopulationGroup_PopulationName();

		/**
		 * The meta object literal for the '<em><b>Target Population</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__TARGET_POPULATION = eINSTANCE.getPopulationGroup_TargetPopulation();

		/**
		 * The meta object literal for the '<em><b>Population Span</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__POPULATION_SPAN = eINSTANCE.getPopulationGroup_PopulationSpan();

		/**
		 * The meta object literal for the '<em><b>Population Description</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__POPULATION_DESCRIPTION = eINSTANCE.getPopulationGroup_PopulationDescription();

		/**
		 * The meta object literal for the '<em><b>Population Age</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__POPULATION_AGE = eINSTANCE.getPopulationGroup_PopulationAge();

		/**
		 * The meta object literal for the '<em><b>Population Gender</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__POPULATION_GENDER = eINSTANCE.getPopulationGroup_PopulationGender();

		/**
		 * The meta object literal for the '<em><b>Bmi</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__BMI = eINSTANCE.getPopulationGroup_Bmi();

		/**
		 * The meta object literal for the '<em><b>Special Diet Groups</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__SPECIAL_DIET_GROUPS = eINSTANCE.getPopulationGroup_SpecialDietGroups();

		/**
		 * The meta object literal for the '<em><b>Pattern Consumption</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__PATTERN_CONSUMPTION = eINSTANCE.getPopulationGroup_PatternConsumption();

		/**
		 * The meta object literal for the '<em><b>Region</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__REGION = eINSTANCE.getPopulationGroup_Region();

		/**
		 * The meta object literal for the '<em><b>Country</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__COUNTRY = eINSTANCE.getPopulationGroup_Country();

		/**
		 * The meta object literal for the '<em><b>Population Risk Factor</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__POPULATION_RISK_FACTOR = eINSTANCE.getPopulationGroup_PopulationRiskFactor();

		/**
		 * The meta object literal for the '<em><b>Season</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POPULATION_GROUP__SEASON = eINSTANCE.getPopulationGroup_Season();

		/**
		 * The meta object literal for the '{@link metadata.impl.ScopeImpl <em>Scope</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ScopeImpl
		 * @see metadata.impl.MetadataPackageImpl#getScope()
		 * @generated
		 */
		EClass SCOPE = eINSTANCE.getScope();

		/**
		 * The meta object literal for the '<em><b>General Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCOPE__GENERAL_COMMENT = eINSTANCE.getScope_GeneralComment();

		/**
		 * The meta object literal for the '<em><b>Temporal Information</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCOPE__TEMPORAL_INFORMATION = eINSTANCE.getScope_TemporalInformation();

		/**
		 * The meta object literal for the '<em><b>Product</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCOPE__PRODUCT = eINSTANCE.getScope_Product();

		/**
		 * The meta object literal for the '<em><b>Hazard</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCOPE__HAZARD = eINSTANCE.getScope_Hazard();

		/**
		 * The meta object literal for the '<em><b>Populationgroup</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCOPE__POPULATIONGROUP = eINSTANCE.getScope_Populationgroup();

		/**
		 * The meta object literal for the '<em><b>Spatial Information</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCOPE__SPATIAL_INFORMATION = eINSTANCE.getScope_SpatialInformation();

		/**
		 * The meta object literal for the '{@link metadata.impl.LaboratoryImpl <em>Laboratory</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.LaboratoryImpl
		 * @see metadata.impl.MetadataPackageImpl#getLaboratory()
		 * @generated
		 */
		EClass LABORATORY = eINSTANCE.getLaboratory();

		/**
		 * The meta object literal for the '<em><b>Laboratory Accreditation</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABORATORY__LABORATORY_ACCREDITATION = eINSTANCE.getLaboratory_LaboratoryAccreditation();

		/**
		 * The meta object literal for the '<em><b>Laboratory Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABORATORY__LABORATORY_NAME = eINSTANCE.getLaboratory_LaboratoryName();

		/**
		 * The meta object literal for the '<em><b>Laboratory Country</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LABORATORY__LABORATORY_COUNTRY = eINSTANCE.getLaboratory_LaboratoryCountry();

		/**
		 * The meta object literal for the '{@link metadata.impl.SpatialInformationImpl <em>Spatial Information</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.SpatialInformationImpl
		 * @see metadata.impl.MetadataPackageImpl#getSpatialInformation()
		 * @generated
		 */
		EClass SPATIAL_INFORMATION = eINSTANCE.getSpatialInformation();

		/**
		 * The meta object literal for the '<em><b>Region</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPATIAL_INFORMATION__REGION = eINSTANCE.getSpatialInformation_Region();

		/**
		 * The meta object literal for the '<em><b>Country</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPATIAL_INFORMATION__COUNTRY = eINSTANCE.getSpatialInformation_Country();

		/**
		 * The meta object literal for the '{@link metadata.impl.modelApplicabilityImpl <em>model Applicability</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.modelApplicabilityImpl
		 * @see metadata.impl.MetadataPackageImpl#getmodelApplicability()
		 * @generated
		 */
		EClass MODEL_APPLICABILITY = eINSTANCE.getmodelApplicability();

		/**
		 * The meta object literal for the '<em><b>Model Applicability</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_APPLICABILITY__MODEL_APPLICABILITY = eINSTANCE.getmodelApplicability_ModelApplicability();

		/**
		 * The meta object literal for the '{@link metadata.impl.EventImpl <em>Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.EventImpl
		 * @see metadata.impl.MetadataPackageImpl#getEvent()
		 * @generated
		 */
		EClass EVENT = eINSTANCE.getEvent();

		/**
		 * The meta object literal for the '<em><b>Event</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__EVENT = eINSTANCE.getEvent_Event();

		/**
		 * The meta object literal for the '{@link metadata.impl.ContactImpl <em>Contact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ContactImpl
		 * @see metadata.impl.MetadataPackageImpl#getContact()
		 * @generated
		 */
		EClass CONTACT = eINSTANCE.getContact();

		/**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__TITLE = eINSTANCE.getContact_Title();

		/**
		 * The meta object literal for the '<em><b>Family Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__FAMILY_NAME = eINSTANCE.getContact_FamilyName();

		/**
		 * The meta object literal for the '<em><b>Email</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__EMAIL = eINSTANCE.getContact_Email();

		/**
		 * The meta object literal for the '<em><b>Telephone</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__TELEPHONE = eINSTANCE.getContact_Telephone();

		/**
		 * The meta object literal for the '<em><b>Street Address</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__STREET_ADDRESS = eINSTANCE.getContact_StreetAddress();

		/**
		 * The meta object literal for the '<em><b>Country</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__COUNTRY = eINSTANCE.getContact_Country();

		/**
		 * The meta object literal for the '<em><b>City</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__CITY = eINSTANCE.getContact_City();

		/**
		 * The meta object literal for the '<em><b>Zip Code</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__ZIP_CODE = eINSTANCE.getContact_ZipCode();

		/**
		 * The meta object literal for the '<em><b>Post Office Box</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__POST_OFFICE_BOX = eINSTANCE.getContact_PostOfficeBox();

		/**
		 * The meta object literal for the '<em><b>Region</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__REGION = eINSTANCE.getContact_Region();

		/**
		 * The meta object literal for the '<em><b>Nickname</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__NICKNAME = eINSTANCE.getContact_Nickname();

		/**
		 * The meta object literal for the '<em><b>Time Zone</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__TIME_ZONE = eINSTANCE.getContact_TimeZone();

		/**
		 * The meta object literal for the '<em><b>Gender</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__GENDER = eINSTANCE.getContact_Gender();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__NAME = eINSTANCE.getContact_Name();

		/**
		 * The meta object literal for the '<em><b>Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__URL = eINSTANCE.getContact_Url();

		/**
		 * The meta object literal for the '<em><b>Note</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__NOTE = eINSTANCE.getContact_Note();

		/**
		 * The meta object literal for the '<em><b>Logo</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__LOGO = eINSTANCE.getContact_Logo();

		/**
		 * The meta object literal for the '<em><b>Organization</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__ORGANIZATION = eINSTANCE.getContact_Organization();

		/**
		 * The meta object literal for the '<em><b>Fn</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONTACT__FN = eINSTANCE.getContact_Fn();

		/**
		 * The meta object literal for the '{@link metadata.impl.ReferenceImpl <em>Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ReferenceImpl
		 * @see metadata.impl.MetadataPackageImpl#getReference()
		 * @generated
		 */
		EClass REFERENCE = eINSTANCE.getReference();

		/**
		 * The meta object literal for the '<em><b>Is Reference Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__IS_REFERENCE_DESCRIPTION = eINSTANCE.getReference_IsReferenceDescription();

		/**
		 * The meta object literal for the '<em><b>Publication Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_TYPE = eINSTANCE.getReference_PublicationType();

		/**
		 * The meta object literal for the '<em><b>Publication Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_DATE = eINSTANCE.getReference_PublicationDate();

		/**
		 * The meta object literal for the '<em><b>Pmid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PMID = eINSTANCE.getReference_Pmid();

		/**
		 * The meta object literal for the '<em><b>Doi</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__DOI = eINSTANCE.getReference_Doi();

		/**
		 * The meta object literal for the '<em><b>Author List</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__AUTHOR_LIST = eINSTANCE.getReference_AuthorList();

		/**
		 * The meta object literal for the '<em><b>Publication Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_TITLE = eINSTANCE.getReference_PublicationTitle();

		/**
		 * The meta object literal for the '<em><b>Publication Abstract</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_ABSTRACT = eINSTANCE.getReference_PublicationAbstract();

		/**
		 * The meta object literal for the '<em><b>Publication Journal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_JOURNAL = eINSTANCE.getReference_PublicationJournal();

		/**
		 * The meta object literal for the '<em><b>Publication Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_VOLUME = eINSTANCE.getReference_PublicationVolume();

		/**
		 * The meta object literal for the '<em><b>Publication Issue</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_ISSUE = eINSTANCE.getReference_PublicationIssue();

		/**
		 * The meta object literal for the '<em><b>Publication Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_STATUS = eINSTANCE.getReference_PublicationStatus();

		/**
		 * The meta object literal for the '<em><b>Publication Website</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__PUBLICATION_WEBSITE = eINSTANCE.getReference_PublicationWebsite();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCE__COMMENT = eINSTANCE.getReference_Comment();

		/**
		 * The meta object literal for the '{@link metadata.impl.ModelMathImpl <em>Model Math</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ModelMathImpl
		 * @see metadata.impl.MetadataPackageImpl#getModelMath()
		 * @generated
		 */
		EClass MODEL_MATH = eINSTANCE.getModelMath();

		/**
		 * The meta object literal for the '<em><b>Quality Measures</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_MATH__QUALITY_MEASURES = eINSTANCE.getModelMath_QualityMeasures();

		/**
		 * The meta object literal for the '<em><b>Fitting Procedure</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_MATH__FITTING_PROCEDURE = eINSTANCE.getModelMath_FittingProcedure();

		/**
		 * The meta object literal for the '<em><b>Event</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_MATH__EVENT = eINSTANCE.getModelMath_Event();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_MATH__PARAMETER = eINSTANCE.getModelMath_Parameter();

		/**
		 * The meta object literal for the '<em><b>Modelequation</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_MATH__MODELEQUATION = eINSTANCE.getModelMath_Modelequation();

		/**
		 * The meta object literal for the '<em><b>Exposure</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_MATH__EXPOSURE = eINSTANCE.getModelMath_Exposure();

		/**
		 * The meta object literal for the '{@link metadata.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ParameterImpl
		 * @see metadata.impl.MetadataPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Parameter ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_ID = eINSTANCE.getParameter_ParameterID();

		/**
		 * The meta object literal for the '<em><b>Parameter Classification</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_CLASSIFICATION = eINSTANCE.getParameter_ParameterClassification();

		/**
		 * The meta object literal for the '<em><b>Parameter Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_NAME = eINSTANCE.getParameter_ParameterName();

		/**
		 * The meta object literal for the '<em><b>Parameter Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_DESCRIPTION = eINSTANCE.getParameter_ParameterDescription();

		/**
		 * The meta object literal for the '<em><b>Parameter Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_TYPE = eINSTANCE.getParameter_ParameterType();

		/**
		 * The meta object literal for the '<em><b>Parameter Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_UNIT = eINSTANCE.getParameter_ParameterUnit();

		/**
		 * The meta object literal for the '<em><b>Parameter Unit Category</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_UNIT_CATEGORY = eINSTANCE.getParameter_ParameterUnitCategory();

		/**
		 * The meta object literal for the '<em><b>Parameter Data Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_DATA_TYPE = eINSTANCE.getParameter_ParameterDataType();

		/**
		 * The meta object literal for the '<em><b>Parameter Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_SOURCE = eINSTANCE.getParameter_ParameterSource();

		/**
		 * The meta object literal for the '<em><b>Parameter Subject</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_SUBJECT = eINSTANCE.getParameter_ParameterSubject();

		/**
		 * The meta object literal for the '<em><b>Parameter Distribution</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_DISTRIBUTION = eINSTANCE.getParameter_ParameterDistribution();

		/**
		 * The meta object literal for the '<em><b>Parameter Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_VALUE = eINSTANCE.getParameter_ParameterValue();

		/**
		 * The meta object literal for the '<em><b>Parameter Variability Subject</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_VARIABILITY_SUBJECT = eINSTANCE.getParameter_ParameterVariabilitySubject();

		/**
		 * The meta object literal for the '<em><b>Parameter Value Min</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_VALUE_MIN = eINSTANCE.getParameter_ParameterValueMin();

		/**
		 * The meta object literal for the '<em><b>Parameter Value Max</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_VALUE_MAX = eINSTANCE.getParameter_ParameterValueMax();

		/**
		 * The meta object literal for the '<em><b>Parameter Error</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__PARAMETER_ERROR = eINSTANCE.getParameter_ParameterError();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER__REFERENCE = eINSTANCE.getParameter_Reference();

		/**
		 * The meta object literal for the '{@link metadata.impl.ModelEquationImpl <em>Model Equation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ModelEquationImpl
		 * @see metadata.impl.MetadataPackageImpl#getModelEquation()
		 * @generated
		 */
		EClass MODEL_EQUATION = eINSTANCE.getModelEquation();

		/**
		 * The meta object literal for the '<em><b>Model Equation Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_EQUATION__MODEL_EQUATION_NAME = eINSTANCE.getModelEquation_ModelEquationName();

		/**
		 * The meta object literal for the '<em><b>Model Equation Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_EQUATION__MODEL_EQUATION_CLASS = eINSTANCE.getModelEquation_ModelEquationClass();

		/**
		 * The meta object literal for the '<em><b>Model Equation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_EQUATION__MODEL_EQUATION = eINSTANCE.getModelEquation_ModelEquation();

		/**
		 * The meta object literal for the '<em><b>Hypothesis Of The Model</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL = eINSTANCE.getModelEquation_HypothesisOfTheModel();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL_EQUATION__REFERENCE = eINSTANCE.getModelEquation_Reference();

		/**
		 * The meta object literal for the '{@link metadata.impl.ExposureImpl <em>Exposure</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.impl.ExposureImpl
		 * @see metadata.impl.MetadataPackageImpl#getExposure()
		 * @generated
		 */
		EClass EXPOSURE = eINSTANCE.getExposure();

		/**
		 * The meta object literal for the '<em><b>Methodological Treatment Of Left Censored Data</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE__METHODOLOGICAL_TREATMENT_OF_LEFT_CENSORED_DATA = eINSTANCE.getExposure_MethodologicalTreatmentOfLeftCensoredData();

		/**
		 * The meta object literal for the '<em><b>Level Of Contamination After Left Censored Data Treatment</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE__LEVEL_OF_CONTAMINATION_AFTER_LEFT_CENSORED_DATA_TREATMENT = eINSTANCE.getExposure_LevelOfContaminationAfterLeftCensoredDataTreatment();

		/**
		 * The meta object literal for the '<em><b>Type Of Exposure</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE__TYPE_OF_EXPOSURE = eINSTANCE.getExposure_TypeOfExposure();

		/**
		 * The meta object literal for the '<em><b>Scenario</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE__SCENARIO = eINSTANCE.getExposure_Scenario();

		/**
		 * The meta object literal for the '<em><b>Uncertainty Estimation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPOSURE__UNCERTAINTY_ESTIMATION = eINSTANCE.getExposure_UncertaintyEstimation();

		/**
		 * The meta object literal for the '{@link metadata.PublicationType <em>Publication Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.PublicationType
		 * @see metadata.impl.MetadataPackageImpl#getPublicationType()
		 * @generated
		 */
		EEnum PUBLICATION_TYPE = eINSTANCE.getPublicationType();

		/**
		 * The meta object literal for the '{@link metadata.ParameterClassification <em>Parameter Classification</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.ParameterClassification
		 * @see metadata.impl.MetadataPackageImpl#getParameterClassification()
		 * @generated
		 */
		EEnum PARAMETER_CLASSIFICATION = eINSTANCE.getParameterClassification();

		/**
		 * The meta object literal for the '{@link metadata.ParameterType <em>Parameter Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see metadata.ParameterType
		 * @see metadata.impl.MetadataPackageImpl#getParameterType()
		 * @generated
		 */
		EEnum PARAMETER_TYPE = eINSTANCE.getParameterType();

		/**
		 * The meta object literal for the '<em>URI</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.net.URI
		 * @see metadata.impl.MetadataPackageImpl#getURI()
		 * @generated
		 */
		EDataType URI = eINSTANCE.getURI();

	}

} //MetadataPackage
