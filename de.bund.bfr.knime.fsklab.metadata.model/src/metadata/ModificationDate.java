/**
 */
package metadata;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Modification Date</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.ModificationDate#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getModificationDate()
 * @model
 * @generated
 */
public interface ModificationDate extends EObject {
	/**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(Date)
   * @see metadata.MetadataPackage#getModificationDate_Value()
   * @model transient="true"
   * @generated
   */
	Date getValue();

	/**
   * Sets the value of the '{@link metadata.ModificationDate#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
	void setValue(Date value);

} // ModificationDate
