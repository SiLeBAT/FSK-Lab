/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Equation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.ModelEquation#getModelEquationName <em>Model Equation Name</em>}</li>
 *   <li>{@link metadata.ModelEquation#getModelEquationClass <em>Model Equation Class</em>}</li>
 *   <li>{@link metadata.ModelEquation#getModelEquation <em>Model Equation</em>}</li>
 *   <li>{@link metadata.ModelEquation#getReference <em>Reference</em>}</li>
 *   <li>{@link metadata.ModelEquation#getHypothesisOfTheModel <em>Hypothesis Of The Model</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getModelEquation()
 * @model
 * @generated
 */
public interface ModelEquation extends EObject {
	/**
	 * Returns the value of the '<em><b>Model Equation Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Equation Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Equation Name</em>' attribute.
	 * @see #setModelEquationName(String)
	 * @see metadata.MetadataPackage#getModelEquation_ModelEquationName()
	 * @model required="true"
	 * @generated
	 */
	String getModelEquationName();

	/**
	 * Sets the value of the '{@link metadata.ModelEquation#getModelEquationName <em>Model Equation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Equation Name</em>' attribute.
	 * @see #getModelEquationName()
	 * @generated
	 */
	void setModelEquationName(String value);

	/**
	 * Returns the value of the '<em><b>Model Equation Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Equation Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Equation Class</em>' attribute.
	 * @see #setModelEquationClass(String)
	 * @see metadata.MetadataPackage#getModelEquation_ModelEquationClass()
	 * @model
	 * @generated
	 */
	String getModelEquationClass();

	/**
	 * Sets the value of the '{@link metadata.ModelEquation#getModelEquationClass <em>Model Equation Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Equation Class</em>' attribute.
	 * @see #getModelEquationClass()
	 * @generated
	 */
	void setModelEquationClass(String value);

	/**
	 * Returns the value of the '<em><b>Model Equation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Equation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Equation</em>' attribute.
	 * @see #setModelEquation(String)
	 * @see metadata.MetadataPackage#getModelEquation_ModelEquation()
	 * @model required="true"
	 * @generated
	 */
	String getModelEquation();

	/**
	 * Sets the value of the '{@link metadata.ModelEquation#getModelEquation <em>Model Equation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Equation</em>' attribute.
	 * @see #getModelEquation()
	 * @generated
	 */
	void setModelEquation(String value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.Reference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' containment reference list.
	 * @see metadata.MetadataPackage#getModelEquation_Reference()
	 * @model containment="true"
	 * @generated
	 */
	EList<Reference> getReference();

	/**
	 * Returns the value of the '<em><b>Hypothesis Of The Model</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StringObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hypothesis Of The Model</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hypothesis Of The Model</em>' containment reference list.
	 * @see metadata.MetadataPackage#getModelEquation_HypothesisOfTheModel()
	 * @model containment="true"
	 * @generated
	 */
	EList<StringObject> getHypothesisOfTheModel();

} // ModelEquation
