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
	 * Returns the value of the '<em><b>Region</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Region</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Region</em>' attribute list.
	 * @see metadata.MetadataPackage#getSpatialInformation_Region()
	 * @model
	 * @generated
	 */
	EList<String> getRegion();

	/**
	 * Returns the value of the '<em><b>Country</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Country</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Country</em>' attribute list.
	 * @see metadata.MetadataPackage#getSpatialInformation_Country()
	 * @model
	 * @generated
	 */
	EList<String> getCountry();

} // SpatialInformation
