/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Background</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.DataBackground#getStudy <em>Study</em>}</li>
 *   <li>{@link metadata.DataBackground#getStudysample <em>Studysample</em>}</li>
 *   <li>{@link metadata.DataBackground#getDietaryassessmentmethod <em>Dietaryassessmentmethod</em>}</li>
 *   <li>{@link metadata.DataBackground#getLaboratory <em>Laboratory</em>}</li>
 *   <li>{@link metadata.DataBackground#getAssay <em>Assay</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getDataBackground()
 * @model
 * @generated
 */
public interface DataBackground extends EObject {
	/**
	 * Returns the value of the '<em><b>Study</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study</em>' containment reference.
	 * @see #setStudy(Study)
	 * @see metadata.MetadataPackage#getDataBackground_Study()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Study getStudy();

	/**
	 * Sets the value of the '{@link metadata.DataBackground#getStudy <em>Study</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study</em>' containment reference.
	 * @see #getStudy()
	 * @generated
	 */
	void setStudy(Study value);

	/**
	 * Returns the value of the '<em><b>Studysample</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StudySample}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Studysample</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Studysample</em>' containment reference list.
	 * @see metadata.MetadataPackage#getDataBackground_Studysample()
	 * @model containment="true"
	 * @generated
	 */
	EList<StudySample> getStudysample();

	/**
	 * Returns the value of the '<em><b>Dietaryassessmentmethod</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.DietaryAssessmentMethod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dietaryassessmentmethod</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dietaryassessmentmethod</em>' containment reference list.
	 * @see metadata.MetadataPackage#getDataBackground_Dietaryassessmentmethod()
	 * @model containment="true"
	 * @generated
	 */
	EList<DietaryAssessmentMethod> getDietaryassessmentmethod();

	/**
	 * Returns the value of the '<em><b>Laboratory</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.Laboratory}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laboratory</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laboratory</em>' containment reference list.
	 * @see metadata.MetadataPackage#getDataBackground_Laboratory()
	 * @model containment="true"
	 * @generated
	 */
	EList<Laboratory> getLaboratory();

	/**
	 * Returns the value of the '<em><b>Assay</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.Assay}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assay</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assay</em>' containment reference list.
	 * @see metadata.MetadataPackage#getDataBackground_Assay()
	 * @model containment="true"
	 * @generated
	 */
	EList<Assay> getAssay();

} // DataBackground
