/**
 */
package metadata;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>model Applicability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.modelApplicability#getModelApplicability <em>Model Applicability</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getmodelApplicability()
 * @model
 * @generated
 */
public interface modelApplicability extends EObject {
	/**
	 * Returns the value of the '<em><b>Model Applicability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Applicability</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Applicability</em>' attribute.
	 * @see #setModelApplicability(String)
	 * @see metadata.MetadataPackage#getmodelApplicability_ModelApplicability()
	 * @model
	 * @generated
	 */
	String getModelApplicability();

	/**
	 * Sets the value of the '{@link metadata.modelApplicability#getModelApplicability <em>Model Applicability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Applicability</em>' attribute.
	 * @see #getModelApplicability()
	 * @generated
	 */
	void setModelApplicability(String value);

} // modelApplicability
