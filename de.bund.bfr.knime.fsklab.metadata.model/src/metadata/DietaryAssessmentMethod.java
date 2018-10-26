/**
 */
package metadata;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dietary Assessment Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.DietaryAssessmentMethod#getCollectionTool <em>Collection Tool</em>}</li>
 *   <li>{@link metadata.DietaryAssessmentMethod#getNumberOfNonConsecutiveOneDay <em>Number Of Non Consecutive One Day</em>}</li>
 *   <li>{@link metadata.DietaryAssessmentMethod#getSoftwareTool <em>Software Tool</em>}</li>
 *   <li>{@link metadata.DietaryAssessmentMethod#getNumberOfFoodItems <em>Number Of Food Items</em>}</li>
 *   <li>{@link metadata.DietaryAssessmentMethod#getRecordTypes <em>Record Types</em>}</li>
 *   <li>{@link metadata.DietaryAssessmentMethod#getFoodDescriptors <em>Food Descriptors</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getDietaryAssessmentMethod()
 * @model
 * @generated
 */
public interface DietaryAssessmentMethod extends EObject {
	/**
   * Returns the value of the '<em><b>Collection Tool</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Collection Tool</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Collection Tool</em>' attribute.
   * @see #setCollectionTool(String)
   * @see metadata.MetadataPackage#getDietaryAssessmentMethod_CollectionTool()
   * @model
   * @generated
   */
	String getCollectionTool();

	/**
   * Sets the value of the '{@link metadata.DietaryAssessmentMethod#getCollectionTool <em>Collection Tool</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Collection Tool</em>' attribute.
   * @see #getCollectionTool()
   * @generated
   */
	void setCollectionTool(String value);

	/**
   * Returns the value of the '<em><b>Number Of Non Consecutive One Day</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Non Consecutive One Day</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Number Of Non Consecutive One Day</em>' attribute.
   * @see #setNumberOfNonConsecutiveOneDay(int)
   * @see metadata.MetadataPackage#getDietaryAssessmentMethod_NumberOfNonConsecutiveOneDay()
   * @model
   * @generated
   */
	int getNumberOfNonConsecutiveOneDay();

	/**
   * Sets the value of the '{@link metadata.DietaryAssessmentMethod#getNumberOfNonConsecutiveOneDay <em>Number Of Non Consecutive One Day</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Number Of Non Consecutive One Day</em>' attribute.
   * @see #getNumberOfNonConsecutiveOneDay()
   * @generated
   */
	void setNumberOfNonConsecutiveOneDay(int value);

	/**
   * Returns the value of the '<em><b>Software Tool</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Software Tool</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Software Tool</em>' attribute.
   * @see #setSoftwareTool(String)
   * @see metadata.MetadataPackage#getDietaryAssessmentMethod_SoftwareTool()
   * @model
   * @generated
   */
	String getSoftwareTool();

	/**
   * Sets the value of the '{@link metadata.DietaryAssessmentMethod#getSoftwareTool <em>Software Tool</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Software Tool</em>' attribute.
   * @see #getSoftwareTool()
   * @generated
   */
	void setSoftwareTool(String value);

	/**
   * Returns the value of the '<em><b>Number Of Food Items</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Food Items</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Number Of Food Items</em>' attribute.
   * @see #setNumberOfFoodItems(String)
   * @see metadata.MetadataPackage#getDietaryAssessmentMethod_NumberOfFoodItems()
   * @model
   * @generated
   */
	String getNumberOfFoodItems();

	/**
   * Sets the value of the '{@link metadata.DietaryAssessmentMethod#getNumberOfFoodItems <em>Number Of Food Items</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Number Of Food Items</em>' attribute.
   * @see #getNumberOfFoodItems()
   * @generated
   */
	void setNumberOfFoodItems(String value);

	/**
   * Returns the value of the '<em><b>Record Types</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record Types</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Record Types</em>' attribute.
   * @see #setRecordTypes(String)
   * @see metadata.MetadataPackage#getDietaryAssessmentMethod_RecordTypes()
   * @model
   * @generated
   */
	String getRecordTypes();

	/**
   * Sets the value of the '{@link metadata.DietaryAssessmentMethod#getRecordTypes <em>Record Types</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Record Types</em>' attribute.
   * @see #getRecordTypes()
   * @generated
   */
	void setRecordTypes(String value);

	/**
   * Returns the value of the '<em><b>Food Descriptors</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Food Descriptors</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Food Descriptors</em>' attribute.
   * @see #setFoodDescriptors(String)
   * @see metadata.MetadataPackage#getDietaryAssessmentMethod_FoodDescriptors()
   * @model
   * @generated
   */
	String getFoodDescriptors();

	/**
   * Sets the value of the '{@link metadata.DietaryAssessmentMethod#getFoodDescriptors <em>Food Descriptors</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Food Descriptors</em>' attribute.
   * @see #getFoodDescriptors()
   * @generated
   */
	void setFoodDescriptors(String value);

} // DietaryAssessmentMethod
