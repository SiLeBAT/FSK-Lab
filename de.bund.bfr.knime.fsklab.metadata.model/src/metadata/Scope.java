/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scope</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Scope#getGeneralComment <em>General Comment</em>}</li>
 *   <li>{@link metadata.Scope#getTemporalInformation <em>Temporal Information</em>}</li>
 *   <li>{@link metadata.Scope#getProduct <em>Product</em>}</li>
 *   <li>{@link metadata.Scope#getHazard <em>Hazard</em>}</li>
 *   <li>{@link metadata.Scope#getPopulationgroup <em>Populationgroup</em>}</li>
 *   <li>{@link metadata.Scope#getSpatialInformation <em>Spatial Information</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getScope()
 * @model
 * @generated
 */
public interface Scope extends EObject {
	/**
	 * Returns the value of the '<em><b>General Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>General Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>General Comment</em>' attribute.
	 * @see #setGeneralComment(String)
	 * @see metadata.MetadataPackage#getScope_GeneralComment()
	 * @model
	 * @generated
	 */
	String getGeneralComment();

	/**
	 * Sets the value of the '{@link metadata.Scope#getGeneralComment <em>General Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>General Comment</em>' attribute.
	 * @see #getGeneralComment()
	 * @generated
	 */
	void setGeneralComment(String value);

	/**
	 * Returns the value of the '<em><b>Temporal Information</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Temporal Information</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Temporal Information</em>' attribute.
	 * @see #setTemporalInformation(String)
	 * @see metadata.MetadataPackage#getScope_TemporalInformation()
	 * @model
	 * @generated
	 */
	String getTemporalInformation();

	/**
	 * Sets the value of the '{@link metadata.Scope#getTemporalInformation <em>Temporal Information</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal Information</em>' attribute.
	 * @see #getTemporalInformation()
	 * @generated
	 */
	void setTemporalInformation(String value);

	/**
	 * Returns the value of the '<em><b>Product</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.Product}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Product</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Product</em>' containment reference list.
	 * @see metadata.MetadataPackage#getScope_Product()
	 * @model containment="true"
	 * @generated
	 */
	EList<Product> getProduct();

	/**
	 * Returns the value of the '<em><b>Hazard</b></em>' reference list.
	 * The list contents are of type {@link metadata.Hazard}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hazard</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hazard</em>' reference list.
	 * @see metadata.MetadataPackage#getScope_Hazard()
	 * @model
	 * @generated
	 */
	EList<Hazard> getHazard();

	/**
	 * Returns the value of the '<em><b>Populationgroup</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Populationgroup</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Populationgroup</em>' reference.
	 * @see #setPopulationgroup(PopulationGroup)
	 * @see metadata.MetadataPackage#getScope_Populationgroup()
	 * @model
	 * @generated
	 */
	PopulationGroup getPopulationgroup();

	/**
	 * Sets the value of the '{@link metadata.Scope#getPopulationgroup <em>Populationgroup</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Populationgroup</em>' reference.
	 * @see #getPopulationgroup()
	 * @generated
	 */
	void setPopulationgroup(PopulationGroup value);

	/**
	 * Returns the value of the '<em><b>Spatial Information</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spatial Information</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spatial Information</em>' containment reference.
	 * @see #setSpatialInformation(SpatialInformation)
	 * @see metadata.MetadataPackage#getScope_SpatialInformation()
	 * @model containment="true"
	 * @generated
	 */
	SpatialInformation getSpatialInformation();

	/**
	 * Sets the value of the '{@link metadata.Scope#getSpatialInformation <em>Spatial Information</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spatial Information</em>' containment reference.
	 * @see #getSpatialInformation()
	 * @generated
	 */
	void setSpatialInformation(SpatialInformation value);

} // Scope
