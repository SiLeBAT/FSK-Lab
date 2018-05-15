/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Laboratory</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Laboratory#getLaboratoryName <em>Laboratory Name</em>}</li>
 *   <li>{@link metadata.Laboratory#getLaboratoryCountry <em>Laboratory Country</em>}</li>
 *   <li>{@link metadata.Laboratory#getLaboratoryAccreditation <em>Laboratory Accreditation</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getLaboratory()
 * @model
 * @generated
 */
public interface Laboratory extends EObject {
	/**
	 * Returns the value of the '<em><b>Laboratory Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laboratory Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laboratory Name</em>' attribute.
	 * @see #setLaboratoryName(String)
	 * @see metadata.MetadataPackage#getLaboratory_LaboratoryName()
	 * @model
	 * @generated
	 */
	String getLaboratoryName();

	/**
	 * Sets the value of the '{@link metadata.Laboratory#getLaboratoryName <em>Laboratory Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laboratory Name</em>' attribute.
	 * @see #getLaboratoryName()
	 * @generated
	 */
	void setLaboratoryName(String value);

	/**
	 * Returns the value of the '<em><b>Laboratory Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laboratory Country</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laboratory Country</em>' attribute.
	 * @see #setLaboratoryCountry(String)
	 * @see metadata.MetadataPackage#getLaboratory_LaboratoryCountry()
	 * @model
	 * @generated
	 */
	String getLaboratoryCountry();

	/**
	 * Sets the value of the '{@link metadata.Laboratory#getLaboratoryCountry <em>Laboratory Country</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laboratory Country</em>' attribute.
	 * @see #getLaboratoryCountry()
	 * @generated
	 */
	void setLaboratoryCountry(String value);

	/**
	 * Returns the value of the '<em><b>Laboratory Accreditation</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StringObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laboratory Accreditation</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laboratory Accreditation</em>' containment reference list.
	 * @see metadata.MetadataPackage#getLaboratory_LaboratoryAccreditation()
	 * @model containment="true"
	 * @generated
	 */
	EList<StringObject> getLaboratoryAccreditation();

} // Laboratory
