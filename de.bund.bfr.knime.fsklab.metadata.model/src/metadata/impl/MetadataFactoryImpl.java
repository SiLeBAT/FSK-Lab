/**
 */
package metadata.impl;

import java.net.URI;

import metadata.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MetadataFactoryImpl extends EFactoryImpl implements MetadataFactory {
	/**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public static MetadataFactory init() {
    try {
      MetadataFactory theMetadataFactory = (MetadataFactory)EPackage.Registry.INSTANCE.getEFactory(MetadataPackage.eNS_URI);
      if (theMetadataFactory != null) {
        return theMetadataFactory;
      }
    }
    catch (Exception exception) {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new MetadataFactoryImpl();
  }

	/**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public MetadataFactoryImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public EObject create(EClass eClass) {
    switch (eClass.getClassifierID()) {
      case MetadataPackage.GENERAL_INFORMATION: return createGeneralInformation();
      case MetadataPackage.MODEL_CATEGORY: return createModelCategory();
      case MetadataPackage.ASSAY: return createAssay();
      case MetadataPackage.STUDY: return createStudy();
      case MetadataPackage.DATA_BACKGROUND: return createDataBackground();
      case MetadataPackage.STUDY_SAMPLE: return createStudySample();
      case MetadataPackage.DIETARY_ASSESSMENT_METHOD: return createDietaryAssessmentMethod();
      case MetadataPackage.MODIFICATION_DATE: return createModificationDate();
      case MetadataPackage.PRODUCT: return createProduct();
      case MetadataPackage.HAZARD: return createHazard();
      case MetadataPackage.POPULATION_GROUP: return createPopulationGroup();
      case MetadataPackage.SCOPE: return createScope();
      case MetadataPackage.LABORATORY: return createLaboratory();
      case MetadataPackage.SPATIAL_INFORMATION: return createSpatialInformation();
      case MetadataPackage.EVENT: return createEvent();
      case MetadataPackage.CONTACT: return createContact();
      case MetadataPackage.REFERENCE: return createReference();
      case MetadataPackage.MODEL_MATH: return createModelMath();
      case MetadataPackage.PARAMETER: return createParameter();
      case MetadataPackage.MODEL_EQUATION: return createModelEquation();
      case MetadataPackage.EXPOSURE: return createExposure();
      case MetadataPackage.STRING_OBJECT: return createStringObject();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
    switch (eDataType.getClassifierID()) {
      case MetadataPackage.PUBLICATION_TYPE:
        return createPublicationTypeFromString(eDataType, initialValue);
      case MetadataPackage.PARAMETER_CLASSIFICATION:
        return createParameterClassificationFromString(eDataType, initialValue);
      case MetadataPackage.PARAMETER_TYPE:
        return createParameterTypeFromString(eDataType, initialValue);
      case MetadataPackage.URI:
        return createURIFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
    switch (eDataType.getClassifierID()) {
      case MetadataPackage.PUBLICATION_TYPE:
        return convertPublicationTypeToString(eDataType, instanceValue);
      case MetadataPackage.PARAMETER_CLASSIFICATION:
        return convertParameterClassificationToString(eDataType, instanceValue);
      case MetadataPackage.PARAMETER_TYPE:
        return convertParameterTypeToString(eDataType, instanceValue);
      case MetadataPackage.URI:
        return convertURIToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public GeneralInformation createGeneralInformation() {
    GeneralInformationImpl generalInformation = new GeneralInformationImpl();
    return generalInformation;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ModelCategory createModelCategory() {
    ModelCategoryImpl modelCategory = new ModelCategoryImpl();
    return modelCategory;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Assay createAssay() {
    AssayImpl assay = new AssayImpl();
    return assay;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Study createStudy() {
    StudyImpl study = new StudyImpl();
    return study;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public DataBackground createDataBackground() {
    DataBackgroundImpl dataBackground = new DataBackgroundImpl();
    return dataBackground;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public StudySample createStudySample() {
    StudySampleImpl studySample = new StudySampleImpl();
    return studySample;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public DietaryAssessmentMethod createDietaryAssessmentMethod() {
    DietaryAssessmentMethodImpl dietaryAssessmentMethod = new DietaryAssessmentMethodImpl();
    return dietaryAssessmentMethod;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ModificationDate createModificationDate() {
    ModificationDateImpl modificationDate = new ModificationDateImpl();
    return modificationDate;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Product createProduct() {
    ProductImpl product = new ProductImpl();
    return product;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Hazard createHazard() {
    HazardImpl hazard = new HazardImpl();
    return hazard;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public PopulationGroup createPopulationGroup() {
    PopulationGroupImpl populationGroup = new PopulationGroupImpl();
    return populationGroup;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Scope createScope() {
    ScopeImpl scope = new ScopeImpl();
    return scope;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Laboratory createLaboratory() {
    LaboratoryImpl laboratory = new LaboratoryImpl();
    return laboratory;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public SpatialInformation createSpatialInformation() {
    SpatialInformationImpl spatialInformation = new SpatialInformationImpl();
    return spatialInformation;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Event createEvent() {
    EventImpl event = new EventImpl();
    return event;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Contact createContact() {
    ContactImpl contact = new ContactImpl();
    return contact;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Reference createReference() {
    ReferenceImpl reference = new ReferenceImpl();
    return reference;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ModelMath createModelMath() {
    ModelMathImpl modelMath = new ModelMathImpl();
    return modelMath;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Parameter createParameter() {
    ParameterImpl parameter = new ParameterImpl();
    return parameter;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ModelEquation createModelEquation() {
    ModelEquationImpl modelEquation = new ModelEquationImpl();
    return modelEquation;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Exposure createExposure() {
    ExposureImpl exposure = new ExposureImpl();
    return exposure;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public StringObject createStringObject() {
    StringObjectImpl stringObject = new StringObjectImpl();
    return stringObject;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public PublicationType createPublicationTypeFromString(EDataType eDataType, String initialValue) {
    PublicationType result = PublicationType.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String convertPublicationTypeToString(EDataType eDataType, Object instanceValue) {
    return instanceValue == null ? null : instanceValue.toString();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ParameterClassification createParameterClassificationFromString(EDataType eDataType, String initialValue) {
    ParameterClassification result = ParameterClassification.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String convertParameterClassificationToString(EDataType eDataType, Object instanceValue) {
    return instanceValue == null ? null : instanceValue.toString();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public ParameterType createParameterTypeFromString(EDataType eDataType, String initialValue) {
    ParameterType result = ParameterType.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String convertParameterTypeToString(EDataType eDataType, Object instanceValue) {
    return instanceValue == null ? null : instanceValue.toString();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public URI createURIFromString(EDataType eDataType, String initialValue) {
    return (URI)super.createFromString(eDataType, initialValue);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String convertURIToString(EDataType eDataType, Object instanceValue) {
    return super.convertToString(eDataType, instanceValue);
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public MetadataPackage getMetadataPackage() {
    return (MetadataPackage)getEPackage();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
	@Deprecated
	public static MetadataPackage getPackage() {
    return MetadataPackage.eINSTANCE;
  }

} //MetadataFactoryImpl
