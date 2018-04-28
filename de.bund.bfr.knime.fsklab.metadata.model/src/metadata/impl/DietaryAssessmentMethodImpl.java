/**
 */
package metadata.impl;

import metadata.DietaryAssessmentMethod;
import metadata.MetadataPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dietary Assessment Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.DietaryAssessmentMethodImpl#getCollectionTool <em>Collection Tool</em>}</li>
 *   <li>{@link metadata.impl.DietaryAssessmentMethodImpl#getNumberOfNonConsecutiveOneDay <em>Number Of Non Consecutive One Day</em>}</li>
 *   <li>{@link metadata.impl.DietaryAssessmentMethodImpl#getSoftwareTool <em>Software Tool</em>}</li>
 *   <li>{@link metadata.impl.DietaryAssessmentMethodImpl#getNumberOfFoodItems <em>Number Of Food Items</em>}</li>
 *   <li>{@link metadata.impl.DietaryAssessmentMethodImpl#getRecordTypes <em>Record Types</em>}</li>
 *   <li>{@link metadata.impl.DietaryAssessmentMethodImpl#getFoodDescriptors <em>Food Descriptors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DietaryAssessmentMethodImpl extends MinimalEObjectImpl.Container implements DietaryAssessmentMethod {
	/**
	 * The default value of the '{@link #getCollectionTool() <em>Collection Tool</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionTool()
	 * @generated
	 * @ordered
	 */
	protected static final String COLLECTION_TOOL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCollectionTool() <em>Collection Tool</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionTool()
	 * @generated
	 * @ordered
	 */
	protected String collectionTool = COLLECTION_TOOL_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumberOfNonConsecutiveOneDay() <em>Number Of Non Consecutive One Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfNonConsecutiveOneDay()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_NON_CONSECUTIVE_ONE_DAY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfNonConsecutiveOneDay() <em>Number Of Non Consecutive One Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfNonConsecutiveOneDay()
	 * @generated
	 * @ordered
	 */
	protected int numberOfNonConsecutiveOneDay = NUMBER_OF_NON_CONSECUTIVE_ONE_DAY_EDEFAULT;

	/**
	 * The default value of the '{@link #getSoftwareTool() <em>Software Tool</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoftwareTool()
	 * @generated
	 * @ordered
	 */
	protected static final String SOFTWARE_TOOL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSoftwareTool() <em>Software Tool</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoftwareTool()
	 * @generated
	 * @ordered
	 */
	protected String softwareTool = SOFTWARE_TOOL_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumberOfFoodItems() <em>Number Of Food Items</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfFoodItems()
	 * @generated
	 * @ordered
	 */
	protected static final String NUMBER_OF_FOOD_ITEMS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNumberOfFoodItems() <em>Number Of Food Items</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfFoodItems()
	 * @generated
	 * @ordered
	 */
	protected String numberOfFoodItems = NUMBER_OF_FOOD_ITEMS_EDEFAULT;

	/**
	 * The default value of the '{@link #getRecordTypes() <em>Record Types</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRecordTypes()
	 * @generated
	 * @ordered
	 */
	protected static final String RECORD_TYPES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRecordTypes() <em>Record Types</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRecordTypes()
	 * @generated
	 * @ordered
	 */
	protected String recordTypes = RECORD_TYPES_EDEFAULT;

	/**
	 * The default value of the '{@link #getFoodDescriptors() <em>Food Descriptors</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFoodDescriptors()
	 * @generated
	 * @ordered
	 */
	protected static final String FOOD_DESCRIPTORS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFoodDescriptors() <em>Food Descriptors</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFoodDescriptors()
	 * @generated
	 * @ordered
	 */
	protected String foodDescriptors = FOOD_DESCRIPTORS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DietaryAssessmentMethodImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.DIETARY_ASSESSMENT_METHOD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCollectionTool() {
		return collectionTool;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCollectionTool(String newCollectionTool) {
		String oldCollectionTool = collectionTool;
		collectionTool = newCollectionTool;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DIETARY_ASSESSMENT_METHOD__COLLECTION_TOOL, oldCollectionTool, collectionTool));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumberOfNonConsecutiveOneDay() {
		return numberOfNonConsecutiveOneDay;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfNonConsecutiveOneDay(int newNumberOfNonConsecutiveOneDay) {
		int oldNumberOfNonConsecutiveOneDay = numberOfNonConsecutiveOneDay;
		numberOfNonConsecutiveOneDay = newNumberOfNonConsecutiveOneDay;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_NON_CONSECUTIVE_ONE_DAY, oldNumberOfNonConsecutiveOneDay, numberOfNonConsecutiveOneDay));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSoftwareTool() {
		return softwareTool;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSoftwareTool(String newSoftwareTool) {
		String oldSoftwareTool = softwareTool;
		softwareTool = newSoftwareTool;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DIETARY_ASSESSMENT_METHOD__SOFTWARE_TOOL, oldSoftwareTool, softwareTool));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNumberOfFoodItems() {
		return numberOfFoodItems;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfFoodItems(String newNumberOfFoodItems) {
		String oldNumberOfFoodItems = numberOfFoodItems;
		numberOfFoodItems = newNumberOfFoodItems;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_FOOD_ITEMS, oldNumberOfFoodItems, numberOfFoodItems));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRecordTypes() {
		return recordTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRecordTypes(String newRecordTypes) {
		String oldRecordTypes = recordTypes;
		recordTypes = newRecordTypes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DIETARY_ASSESSMENT_METHOD__RECORD_TYPES, oldRecordTypes, recordTypes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFoodDescriptors() {
		return foodDescriptors;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFoodDescriptors(String newFoodDescriptors) {
		String oldFoodDescriptors = foodDescriptors;
		foodDescriptors = newFoodDescriptors;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DIETARY_ASSESSMENT_METHOD__FOOD_DESCRIPTORS, oldFoodDescriptors, foodDescriptors));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__COLLECTION_TOOL:
				return getCollectionTool();
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_NON_CONSECUTIVE_ONE_DAY:
				return getNumberOfNonConsecutiveOneDay();
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__SOFTWARE_TOOL:
				return getSoftwareTool();
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_FOOD_ITEMS:
				return getNumberOfFoodItems();
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__RECORD_TYPES:
				return getRecordTypes();
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__FOOD_DESCRIPTORS:
				return getFoodDescriptors();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__COLLECTION_TOOL:
				setCollectionTool((String)newValue);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_NON_CONSECUTIVE_ONE_DAY:
				setNumberOfNonConsecutiveOneDay((Integer)newValue);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__SOFTWARE_TOOL:
				setSoftwareTool((String)newValue);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_FOOD_ITEMS:
				setNumberOfFoodItems((String)newValue);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__RECORD_TYPES:
				setRecordTypes((String)newValue);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__FOOD_DESCRIPTORS:
				setFoodDescriptors((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__COLLECTION_TOOL:
				setCollectionTool(COLLECTION_TOOL_EDEFAULT);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_NON_CONSECUTIVE_ONE_DAY:
				setNumberOfNonConsecutiveOneDay(NUMBER_OF_NON_CONSECUTIVE_ONE_DAY_EDEFAULT);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__SOFTWARE_TOOL:
				setSoftwareTool(SOFTWARE_TOOL_EDEFAULT);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_FOOD_ITEMS:
				setNumberOfFoodItems(NUMBER_OF_FOOD_ITEMS_EDEFAULT);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__RECORD_TYPES:
				setRecordTypes(RECORD_TYPES_EDEFAULT);
				return;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__FOOD_DESCRIPTORS:
				setFoodDescriptors(FOOD_DESCRIPTORS_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__COLLECTION_TOOL:
				return COLLECTION_TOOL_EDEFAULT == null ? collectionTool != null : !COLLECTION_TOOL_EDEFAULT.equals(collectionTool);
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_NON_CONSECUTIVE_ONE_DAY:
				return numberOfNonConsecutiveOneDay != NUMBER_OF_NON_CONSECUTIVE_ONE_DAY_EDEFAULT;
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__SOFTWARE_TOOL:
				return SOFTWARE_TOOL_EDEFAULT == null ? softwareTool != null : !SOFTWARE_TOOL_EDEFAULT.equals(softwareTool);
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__NUMBER_OF_FOOD_ITEMS:
				return NUMBER_OF_FOOD_ITEMS_EDEFAULT == null ? numberOfFoodItems != null : !NUMBER_OF_FOOD_ITEMS_EDEFAULT.equals(numberOfFoodItems);
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__RECORD_TYPES:
				return RECORD_TYPES_EDEFAULT == null ? recordTypes != null : !RECORD_TYPES_EDEFAULT.equals(recordTypes);
			case MetadataPackage.DIETARY_ASSESSMENT_METHOD__FOOD_DESCRIPTORS:
				return FOOD_DESCRIPTORS_EDEFAULT == null ? foodDescriptors != null : !FOOD_DESCRIPTORS_EDEFAULT.equals(foodDescriptors);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (collectionTool: ");
		result.append(collectionTool);
		result.append(", numberOfNonConsecutiveOneDay: ");
		result.append(numberOfNonConsecutiveOneDay);
		result.append(", softwareTool: ");
		result.append(softwareTool);
		result.append(", numberOfFoodItems: ");
		result.append(numberOfFoodItems);
		result.append(", recordTypes: ");
		result.append(recordTypes);
		result.append(", foodDescriptors: ");
		result.append(foodDescriptors);
		result.append(')');
		return result.toString();
	}

} //DietaryAssessmentMethodImpl
