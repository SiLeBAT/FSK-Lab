/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spatial Information</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.SpatialInformation#getRegion <em>Region</em>}</li>
 *   <li>{@link metadata.SpatialInformation#getCountry <em>Country</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getSpatialInformation()
 * @model
 * @generated
 */
public interface SpatialInformation extends EObject {
	/**
	 * Returns the value of the '<em><b>Region</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StringObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Region</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Region</em>' containment reference list.
	 * @see metadata.MetadataPackage#getSpatialInformation_Region()
	 * @model containment="true"
	 * @generated
	 */
	EList<StringObject> getRegion();

	/**
	 * Returns the value of the '<em><b>Country</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StringObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Country</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Country</em>' containment reference list.
	 * @see metadata.MetadataPackage#getSpatialInformation_Country()
	 * @model containment="true"
	 * @generated
	 */
	EList<StringObject> getCountry();

} // SpatialInformation
