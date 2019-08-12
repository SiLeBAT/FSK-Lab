/**
 */
package metadata.impl;

import metadata.Assay;
import metadata.MetadataPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assay</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.AssayImpl#getAssayName <em>Assay Name</em>}</li>
 *   <li>{@link metadata.impl.AssayImpl#getAssayDescription <em>Assay Description</em>}</li>
 *   <li>{@link metadata.impl.AssayImpl#getPercentageOfMoisture <em>Percentage Of Moisture</em>}</li>
 *   <li>{@link metadata.impl.AssayImpl#getPercentageOfFat <em>Percentage Of Fat</em>}</li>
 *   <li>{@link metadata.impl.AssayImpl#getLimitOfDetection <em>Limit Of Detection</em>}</li>
 *   <li>{@link metadata.impl.AssayImpl#getLimitOfQuantification <em>Limit Of Quantification</em>}</li>
 *   <li>{@link metadata.impl.AssayImpl#getLeftCensoredData <em>Left Censored Data</em>}</li>
 *   <li>{@link metadata.impl.AssayImpl#getRangeOfContamination <em>Range Of Contamination</em>}</li>
 *   <li>{@link metadata.impl.AssayImpl#getUncertaintyValue <em>Uncertainty Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssayImpl extends MinimalEObjectImpl.Container implements Assay {
	/**
	 * The default value of the '{@link #getAssayName() <em>Assay Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssayName()
	 * @generated
	 * @ordered
	 */
	protected static final String ASSAY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAssayName() <em>Assay Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssayName()
	 * @generated
	 * @ordered
	 */
	protected String assayName = ASSAY_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getAssayDescription() <em>Assay Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssayDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String ASSAY_DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAssayDescription() <em>Assay Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssayDescription()
	 * @generated
	 * @ordered
	 */
	protected String assayDescription = ASSAY_DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getPercentageOfMoisture() <em>Percentage Of Moisture</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercentageOfMoisture()
	 * @generated
	 * @ordered
	 */
	protected static final String PERCENTAGE_OF_MOISTURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPercentageOfMoisture() <em>Percentage Of Moisture</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercentageOfMoisture()
	 * @generated
	 * @ordered
	 */
	protected String percentageOfMoisture = PERCENTAGE_OF_MOISTURE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPercentageOfFat() <em>Percentage Of Fat</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercentageOfFat()
	 * @generated
	 * @ordered
	 */
	protected static final String PERCENTAGE_OF_FAT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPercentageOfFat() <em>Percentage Of Fat</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercentageOfFat()
	 * @generated
	 * @ordered
	 */
	protected String percentageOfFat = PERCENTAGE_OF_FAT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLimitOfDetection() <em>Limit Of Detection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimitOfDetection()
	 * @generated
	 * @ordered
	 */
	protected static final String LIMIT_OF_DETECTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLimitOfDetection() <em>Limit Of Detection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimitOfDetection()
	 * @generated
	 * @ordered
	 */
	protected String limitOfDetection = LIMIT_OF_DETECTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getLimitOfQuantification() <em>Limit Of Quantification</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimitOfQuantification()
	 * @generated
	 * @ordered
	 */
	protected static final String LIMIT_OF_QUANTIFICATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLimitOfQuantification() <em>Limit Of Quantification</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimitOfQuantification()
	 * @generated
	 * @ordered
	 */
	protected String limitOfQuantification = LIMIT_OF_QUANTIFICATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getLeftCensoredData() <em>Left Censored Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftCensoredData()
	 * @generated
	 * @ordered
	 */
	protected static final String LEFT_CENSORED_DATA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLeftCensoredData() <em>Left Censored Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftCensoredData()
	 * @generated
	 * @ordered
	 */
	protected String leftCensoredData = LEFT_CENSORED_DATA_EDEFAULT;

	/**
	 * The default value of the '{@link #getRangeOfContamination() <em>Range Of Contamination</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRangeOfContamination()
	 * @generated
	 * @ordered
	 */
	protected static final String RANGE_OF_CONTAMINATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRangeOfContamination() <em>Range Of Contamination</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRangeOfContamination()
	 * @generated
	 * @ordered
	 */
	protected String rangeOfContamination = RANGE_OF_CONTAMINATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getUncertaintyValue() <em>Uncertainty Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUncertaintyValue()
	 * @generated
	 * @ordered
	 */
	protected static final String UNCERTAINTY_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUncertaintyValue() <em>Uncertainty Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUncertaintyValue()
	 * @generated
	 * @ordered
	 */
	protected String uncertaintyValue = UNCERTAINTY_VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssayImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.ASSAY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAssayName() {
		return assayName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAssayName(String newAssayName) {
		String oldAssayName = assayName;
		assayName = newAssayName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__ASSAY_NAME, oldAssayName, assayName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAssayDescription() {
		return assayDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAssayDescription(String newAssayDescription) {
		String oldAssayDescription = assayDescription;
		assayDescription = newAssayDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__ASSAY_DESCRIPTION, oldAssayDescription, assayDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPercentageOfMoisture() {
		return percentageOfMoisture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPercentageOfMoisture(String newPercentageOfMoisture) {
		String oldPercentageOfMoisture = percentageOfMoisture;
		percentageOfMoisture = newPercentageOfMoisture;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__PERCENTAGE_OF_MOISTURE, oldPercentageOfMoisture, percentageOfMoisture));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPercentageOfFat() {
		return percentageOfFat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPercentageOfFat(String newPercentageOfFat) {
		String oldPercentageOfFat = percentageOfFat;
		percentageOfFat = newPercentageOfFat;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__PERCENTAGE_OF_FAT, oldPercentageOfFat, percentageOfFat));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLimitOfDetection() {
		return limitOfDetection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLimitOfDetection(String newLimitOfDetection) {
		String oldLimitOfDetection = limitOfDetection;
		limitOfDetection = newLimitOfDetection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__LIMIT_OF_DETECTION, oldLimitOfDetection, limitOfDetection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLimitOfQuantification() {
		return limitOfQuantification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLimitOfQuantification(String newLimitOfQuantification) {
		String oldLimitOfQuantification = limitOfQuantification;
		limitOfQuantification = newLimitOfQuantification;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__LIMIT_OF_QUANTIFICATION, oldLimitOfQuantification, limitOfQuantification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLeftCensoredData() {
		return leftCensoredData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftCensoredData(String newLeftCensoredData) {
		String oldLeftCensoredData = leftCensoredData;
		leftCensoredData = newLeftCensoredData;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__LEFT_CENSORED_DATA, oldLeftCensoredData, leftCensoredData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRangeOfContamination() {
		return rangeOfContamination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRangeOfContamination(String newRangeOfContamination) {
		String oldRangeOfContamination = rangeOfContamination;
		rangeOfContamination = newRangeOfContamination;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__RANGE_OF_CONTAMINATION, oldRangeOfContamination, rangeOfContamination));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUncertaintyValue() {
		return uncertaintyValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUncertaintyValue(String newUncertaintyValue) {
		String oldUncertaintyValue = uncertaintyValue;
		uncertaintyValue = newUncertaintyValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.ASSAY__UNCERTAINTY_VALUE, oldUncertaintyValue, uncertaintyValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.ASSAY__ASSAY_NAME:
				return getAssayName();
			case MetadataPackage.ASSAY__ASSAY_DESCRIPTION:
				return getAssayDescription();
			case MetadataPackage.ASSAY__PERCENTAGE_OF_MOISTURE:
				return getPercentageOfMoisture();
			case MetadataPackage.ASSAY__PERCENTAGE_OF_FAT:
				return getPercentageOfFat();
			case MetadataPackage.ASSAY__LIMIT_OF_DETECTION:
				return getLimitOfDetection();
			case MetadataPackage.ASSAY__LIMIT_OF_QUANTIFICATION:
				return getLimitOfQuantification();
			case MetadataPackage.ASSAY__LEFT_CENSORED_DATA:
				return getLeftCensoredData();
			case MetadataPackage.ASSAY__RANGE_OF_CONTAMINATION:
				return getRangeOfContamination();
			case MetadataPackage.ASSAY__UNCERTAINTY_VALUE:
				return getUncertaintyValue();
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
			case MetadataPackage.ASSAY__ASSAY_NAME:
				setAssayName((String)newValue);
				return;
			case MetadataPackage.ASSAY__ASSAY_DESCRIPTION:
				setAssayDescription((String)newValue);
				return;
			case MetadataPackage.ASSAY__PERCENTAGE_OF_MOISTURE:
				setPercentageOfMoisture((String)newValue);
				return;
			case MetadataPackage.ASSAY__PERCENTAGE_OF_FAT:
				setPercentageOfFat((String)newValue);
				return;
			case MetadataPackage.ASSAY__LIMIT_OF_DETECTION:
				setLimitOfDetection((String)newValue);
				return;
			case MetadataPackage.ASSAY__LIMIT_OF_QUANTIFICATION:
				setLimitOfQuantification((String)newValue);
				return;
			case MetadataPackage.ASSAY__LEFT_CENSORED_DATA:
				setLeftCensoredData((String)newValue);
				return;
			case MetadataPackage.ASSAY__RANGE_OF_CONTAMINATION:
				setRangeOfContamination((String)newValue);
				return;
			case MetadataPackage.ASSAY__UNCERTAINTY_VALUE:
				setUncertaintyValue((String)newValue);
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
			case MetadataPackage.ASSAY__ASSAY_NAME:
				setAssayName(ASSAY_NAME_EDEFAULT);
				return;
			case MetadataPackage.ASSAY__ASSAY_DESCRIPTION:
				setAssayDescription(ASSAY_DESCRIPTION_EDEFAULT);
				return;
			case MetadataPackage.ASSAY__PERCENTAGE_OF_MOISTURE:
				setPercentageOfMoisture(PERCENTAGE_OF_MOISTURE_EDEFAULT);
				return;
			case MetadataPackage.ASSAY__PERCENTAGE_OF_FAT:
				setPercentageOfFat(PERCENTAGE_OF_FAT_EDEFAULT);
				return;
			case MetadataPackage.ASSAY__LIMIT_OF_DETECTION:
				setLimitOfDetection(LIMIT_OF_DETECTION_EDEFAULT);
				return;
			case MetadataPackage.ASSAY__LIMIT_OF_QUANTIFICATION:
				setLimitOfQuantification(LIMIT_OF_QUANTIFICATION_EDEFAULT);
				return;
			case MetadataPackage.ASSAY__LEFT_CENSORED_DATA:
				setLeftCensoredData(LEFT_CENSORED_DATA_EDEFAULT);
				return;
			case MetadataPackage.ASSAY__RANGE_OF_CONTAMINATION:
				setRangeOfContamination(RANGE_OF_CONTAMINATION_EDEFAULT);
				return;
			case MetadataPackage.ASSAY__UNCERTAINTY_VALUE:
				setUncertaintyValue(UNCERTAINTY_VALUE_EDEFAULT);
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
			case MetadataPackage.ASSAY__ASSAY_NAME:
				return ASSAY_NAME_EDEFAULT == null ? assayName != null : !ASSAY_NAME_EDEFAULT.equals(assayName);
			case MetadataPackage.ASSAY__ASSAY_DESCRIPTION:
				return ASSAY_DESCRIPTION_EDEFAULT == null ? assayDescription != null : !ASSAY_DESCRIPTION_EDEFAULT.equals(assayDescription);
			case MetadataPackage.ASSAY__PERCENTAGE_OF_MOISTURE:
				return PERCENTAGE_OF_MOISTURE_EDEFAULT == null ? percentageOfMoisture != null : !PERCENTAGE_OF_MOISTURE_EDEFAULT.equals(percentageOfMoisture);
			case MetadataPackage.ASSAY__PERCENTAGE_OF_FAT:
				return PERCENTAGE_OF_FAT_EDEFAULT == null ? percentageOfFat != null : !PERCENTAGE_OF_FAT_EDEFAULT.equals(percentageOfFat);
			case MetadataPackage.ASSAY__LIMIT_OF_DETECTION:
				return LIMIT_OF_DETECTION_EDEFAULT == null ? limitOfDetection != null : !LIMIT_OF_DETECTION_EDEFAULT.equals(limitOfDetection);
			case MetadataPackage.ASSAY__LIMIT_OF_QUANTIFICATION:
				return LIMIT_OF_QUANTIFICATION_EDEFAULT == null ? limitOfQuantification != null : !LIMIT_OF_QUANTIFICATION_EDEFAULT.equals(limitOfQuantification);
			case MetadataPackage.ASSAY__LEFT_CENSORED_DATA:
				return LEFT_CENSORED_DATA_EDEFAULT == null ? leftCensoredData != null : !LEFT_CENSORED_DATA_EDEFAULT.equals(leftCensoredData);
			case MetadataPackage.ASSAY__RANGE_OF_CONTAMINATION:
				return RANGE_OF_CONTAMINATION_EDEFAULT == null ? rangeOfContamination != null : !RANGE_OF_CONTAMINATION_EDEFAULT.equals(rangeOfContamination);
			case MetadataPackage.ASSAY__UNCERTAINTY_VALUE:
				return UNCERTAINTY_VALUE_EDEFAULT == null ? uncertaintyValue != null : !UNCERTAINTY_VALUE_EDEFAULT.equals(uncertaintyValue);
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
		result.append(" (assayName: ");
		result.append(assayName);
		result.append(", assayDescription: ");
		result.append(assayDescription);
		result.append(", percentageOfMoisture: ");
		result.append(percentageOfMoisture);
		result.append(", percentageOfFat: ");
		result.append(percentageOfFat);
		result.append(", limitOfDetection: ");
		result.append(limitOfDetection);
		result.append(", limitOfQuantification: ");
		result.append(limitOfQuantification);
		result.append(", leftCensoredData: ");
		result.append(leftCensoredData);
		result.append(", rangeOfContamination: ");
		result.append(rangeOfContamination);
		result.append(", uncertaintyValue: ");
		result.append(uncertaintyValue);
		result.append(')');
		return result.toString();
	}

} //AssayImpl
