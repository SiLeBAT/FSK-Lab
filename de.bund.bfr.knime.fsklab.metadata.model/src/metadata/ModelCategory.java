/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Category</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.ModelCategory#getModelClass <em>Model Class</em>}</li>
 *   <li>{@link metadata.ModelCategory#getModelClassComment <em>Model Class Comment</em>}</li>
 *   <li>{@link metadata.ModelCategory#getBasicProcess <em>Basic Process</em>}</li>
 *   <li>{@link metadata.ModelCategory#getModelSubClass <em>Model Sub Class</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getModelCategory()
 * @model
 * @generated
 */
public interface ModelCategory extends EObject {
	/**
	 * Returns the value of the '<em><b>Model Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Class</em>' attribute.
	 * @see #setModelClass(String)
	 * @see metadata.MetadataPackage#getModelCategory_ModelClass()
	 * @model required="true"
	 * @generated
	 */
	String getModelClass();

	/**
	 * Sets the value of the '{@link metadata.ModelCategory#getModelClass <em>Model Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Class</em>' attribute.
	 * @see #getModelClass()
	 * @generated
	 */
	void setModelClass(String value);

	/**
	 * Returns the value of the '<em><b>Model Class Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Class Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Class Comment</em>' attribute.
	 * @see #setModelClassComment(String)
	 * @see metadata.MetadataPackage#getModelCategory_ModelClassComment()
	 * @model
	 * @generated
	 */
	String getModelClassComment();

	/**
	 * Sets the value of the '{@link metadata.ModelCategory#getModelClassComment <em>Model Class Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Class Comment</em>' attribute.
	 * @see #getModelClassComment()
	 * @generated
	 */
	void setModelClassComment(String value);

	/**
	 * Returns the value of the '<em><b>Basic Process</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Basic Process</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Basic Process</em>' attribute.
	 * @see #setBasicProcess(String)
	 * @see metadata.MetadataPackage#getModelCategory_BasicProcess()
	 * @model
	 * @generated
	 */
	String getBasicProcess();

	/**
	 * Sets the value of the '{@link metadata.ModelCategory#getBasicProcess <em>Basic Process</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Basic Process</em>' attribute.
	 * @see #getBasicProcess()
	 * @generated
	 */
	void setBasicProcess(String value);

	/**
	 * Returns the value of the '<em><b>Model Sub Class</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.StringObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Sub Class</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Sub Class</em>' containment reference list.
	 * @see metadata.MetadataPackage#getModelCategory_ModelSubClass()
	 * @model containment="true"
	 * @generated
	 */
	EList<StringObject> getModelSubClass();

} // ModelCategory
