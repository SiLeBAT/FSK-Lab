/**
 */
package metadata.util;

import metadata.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see metadata.MetadataPackage
 * @generated
 */
public class MetadataSwitch<T> extends Switch<T> {
	/**
   * The cached model package
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected static MetadataPackage modelPackage;

	/**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public MetadataSwitch() {
    if (modelPackage == null) {
      modelPackage = MetadataPackage.eINSTANCE;
    }
  }

	/**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
    return ePackage == modelPackage;
  }

	/**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
    switch (classifierID) {
      case MetadataPackage.GENERAL_INFORMATION: {
        GeneralInformation generalInformation = (GeneralInformation)theEObject;
        T result = caseGeneralInformation(generalInformation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.MODEL_CATEGORY: {
        ModelCategory modelCategory = (ModelCategory)theEObject;
        T result = caseModelCategory(modelCategory);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.ASSAY: {
        Assay assay = (Assay)theEObject;
        T result = caseAssay(assay);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.STUDY: {
        Study study = (Study)theEObject;
        T result = caseStudy(study);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.DATA_BACKGROUND: {
        DataBackground dataBackground = (DataBackground)theEObject;
        T result = caseDataBackground(dataBackground);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.STUDY_SAMPLE: {
        StudySample studySample = (StudySample)theEObject;
        T result = caseStudySample(studySample);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.DIETARY_ASSESSMENT_METHOD: {
        DietaryAssessmentMethod dietaryAssessmentMethod = (DietaryAssessmentMethod)theEObject;
        T result = caseDietaryAssessmentMethod(dietaryAssessmentMethod);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.MODIFICATION_DATE: {
        ModificationDate modificationDate = (ModificationDate)theEObject;
        T result = caseModificationDate(modificationDate);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.PRODUCT: {
        Product product = (Product)theEObject;
        T result = caseProduct(product);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.HAZARD: {
        Hazard hazard = (Hazard)theEObject;
        T result = caseHazard(hazard);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.POPULATION_GROUP: {
        PopulationGroup populationGroup = (PopulationGroup)theEObject;
        T result = casePopulationGroup(populationGroup);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.SCOPE: {
        Scope scope = (Scope)theEObject;
        T result = caseScope(scope);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.LABORATORY: {
        Laboratory laboratory = (Laboratory)theEObject;
        T result = caseLaboratory(laboratory);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.SPATIAL_INFORMATION: {
        SpatialInformation spatialInformation = (SpatialInformation)theEObject;
        T result = caseSpatialInformation(spatialInformation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.EVENT: {
        Event event = (Event)theEObject;
        T result = caseEvent(event);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.CONTACT: {
        Contact contact = (Contact)theEObject;
        T result = caseContact(contact);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.REFERENCE: {
        Reference reference = (Reference)theEObject;
        T result = caseReference(reference);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.MODEL_MATH: {
        ModelMath modelMath = (ModelMath)theEObject;
        T result = caseModelMath(modelMath);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.PARAMETER: {
        Parameter parameter = (Parameter)theEObject;
        T result = caseParameter(parameter);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.MODEL_EQUATION: {
        ModelEquation modelEquation = (ModelEquation)theEObject;
        T result = caseModelEquation(modelEquation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.EXPOSURE: {
        Exposure exposure = (Exposure)theEObject;
        T result = caseExposure(exposure);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MetadataPackage.STRING_OBJECT: {
        StringObject stringObject = (StringObject)theEObject;
        T result = caseStringObject(stringObject);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>General Information</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>General Information</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseGeneralInformation(GeneralInformation object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Model Category</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Category</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseModelCategory(ModelCategory object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Assay</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Assay</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseAssay(Assay object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Study</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Study</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseStudy(Study object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Data Background</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Data Background</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseDataBackground(DataBackground object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Study Sample</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Study Sample</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseStudySample(StudySample object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Dietary Assessment Method</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Dietary Assessment Method</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseDietaryAssessmentMethod(DietaryAssessmentMethod object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Modification Date</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Modification Date</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseModificationDate(ModificationDate object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Product</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Product</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseProduct(Product object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Hazard</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Hazard</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseHazard(Hazard object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Population Group</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Population Group</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T casePopulationGroup(PopulationGroup object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Scope</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Scope</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseScope(Scope object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Laboratory</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Laboratory</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseLaboratory(Laboratory object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Spatial Information</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Spatial Information</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseSpatialInformation(SpatialInformation object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Event</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Event</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseEvent(Event object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Contact</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Contact</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseContact(Contact object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Reference</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseReference(Reference object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Model Math</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Math</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseModelMath(ModelMath object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Parameter</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseParameter(Parameter object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Model Equation</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Equation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseModelEquation(ModelEquation object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>Exposure</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Exposure</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseExposure(Exposure object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>String Object</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Object</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
	public T caseStringObject(StringObject object) {
    return null;
  }

	/**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
	@Override
	public T defaultCase(EObject object) {
    return null;
  }

} //MetadataSwitch
