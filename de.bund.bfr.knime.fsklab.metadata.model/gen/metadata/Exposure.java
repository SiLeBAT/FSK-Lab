/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exposure</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Exposure#getTypeOfExposure <em>Type Of Exposure</em>}</li>
 *   <li>{@link metadata.Exposure#getUncertaintyEstimation <em>Uncertainty Estimation</em>}</li>
 *   <li>{@link metadata.Exposure#getMethodologicalTreatmentOfLeftCensoredData <em>Methodological Treatment Of Left Censored Data</em>}</li>
 *   <li>{@link metadata.Exposure#getLevelOfContaminationAfterLeftCensoredDataTreatment <em>Level Of Contamination After Left Censored Data Treatment</em>}</li>
 *   <li>{@link metadata.Exposure#getScenario <em>Scenario</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getExposure()
 * @model
 * @generated
 */
public interface Exposure extends EObject {
	/**
	 * Returns the value of the '<em><b>Type Of Exposure</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Of Exposure</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Of Exposure</em>' attribute.
	 * @see #setTypeOfExposure(String)
	 * @see metadata.MetadataPackage#getExposure_TypeOfExposure()
	 * @model required="true"
	 * @generated
	 */
	String getTypeOfExposure();

	/**
	 * Sets the value of the '{@link metadata.Exposure#getTypeOfExposure <em>Type Of Exposure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Of Exposure</em>' attribute.
	 * @see #getTypeOfExposure()
	 * @generated
	 */
	void setTypeOfExposure(String value);

	/**
	 * Returns the value of the '<em><b>Uncertainty Estimation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uncertainty Estimation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uncertainty Estimation</em>' attribute.
	 * @see #setUncertaintyEstimation(String)
	 * @see metadata.MetadataPackage#getExposure_UncertaintyEstimation()
	 * @model
	 * @generated
	 */
	String getUncertaintyEstimation();

	/**
	 * Sets the value of the '{@link metadata.Exposure#getUncertaintyEstimation <em>Uncertainty Estimation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uncertainty Estimation</em>' attribute.
	 * @see #getUncertaintyEstimation()
	 * @generated
	 */
	void setUncertaintyEstimation(String value);

	/**
	 * Returns the value of the '<em><b>Methodological Treatment Of Left Censored Data</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StringObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Methodological Treatment Of Left Censored Data</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Methodological Treatment Of Left Censored Data</em>' containment reference list.
	 * @see metadata.MetadataPackage#getExposure_MethodologicalTreatmentOfLeftCensoredData()
	 * @model containment="true"
	 * @generated
	 */
	EList<StringObject> getMethodologicalTreatmentOfLeftCensoredData();

	/**
	 * Returns the value of the '<em><b>Level Of Contamination After Left Censored Data Treatment</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StringObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Level Of Contamination After Left Censored Data Treatment</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Level Of Contamination After Left Censored Data Treatment</em>' containment reference list.
	 * @see metadata.MetadataPackage#getExposure_LevelOfContaminationAfterLeftCensoredDataTreatment()
	 * @model containment="true"
	 * @generated
	 */
	EList<StringObject> getLevelOfContaminationAfterLeftCensoredDataTreatment();

	/**
	 * Returns the value of the '<em><b>Scenario</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StringObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario</em>' containment reference list.
	 * @see metadata.MetadataPackage#getExposure_Scenario()
	 * @model containment="true"
	 * @generated
	 */
	EList<StringObject> getScenario();

} // Exposure
