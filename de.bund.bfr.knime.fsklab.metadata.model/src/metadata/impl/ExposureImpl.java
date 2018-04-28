/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.Exposure;
import metadata.MetadataPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Exposure</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.ExposureImpl#getMethodologicalTreatmentOfLeftCensoredData <em>Methodological Treatment Of Left Censored Data</em>}</li>
 *   <li>{@link metadata.impl.ExposureImpl#getLevelOfContaminationAfterLeftCensoredDataTreatment <em>Level Of Contamination After Left Censored Data Treatment</em>}</li>
 *   <li>{@link metadata.impl.ExposureImpl#getTypeOfExposure <em>Type Of Exposure</em>}</li>
 *   <li>{@link metadata.impl.ExposureImpl#getScenario <em>Scenario</em>}</li>
 *   <li>{@link metadata.impl.ExposureImpl#getUncertaintyEstimation <em>Uncertainty Estimation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExposureImpl extends MinimalEObjectImpl.Container implements Exposure {
	/**
	 * The cached value of the '{@link #getMethodologicalTreatmentOfLeftCensoredData() <em>Methodological Treatment Of Left Censored Data</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethodologicalTreatmentOfLeftCensoredData()
	 * @generated
	 * @ordered
	 */
	protected EList<String> methodologicalTreatmentOfLeftCensoredData;

	/**
	 * The cached value of the '{@link #getLevelOfContaminationAfterLeftCensoredDataTreatment() <em>Level Of Contamination After Left Censored Data Treatment</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLevelOfContaminationAfterLeftCensoredDataTreatment()
	 * @generated
	 * @ordered
	 */
	protected EList<String> levelOfContaminationAfterLeftCensoredDataTreatment;

	/**
	 * The default value of the '{@link #getTypeOfExposure() <em>Type Of Exposure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeOfExposure()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_OF_EXPOSURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTypeOfExposure() <em>Type Of Exposure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeOfExposure()
	 * @generated
	 * @ordered
	 */
	protected String typeOfExposure = TYPE_OF_EXPOSURE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getScenario() <em>Scenario</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenario()
	 * @generated
	 * @ordered
	 */
	protected EList<String> scenario;

	/**
	 * The default value of the '{@link #getUncertaintyEstimation() <em>Uncertainty Estimation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUncertaintyEstimation()
	 * @generated
	 * @ordered
	 */
	protected static final String UNCERTAINTY_ESTIMATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUncertaintyEstimation() <em>Uncertainty Estimation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUncertaintyEstimation()
	 * @generated
	 * @ordered
	 */
	protected String uncertaintyEstimation = UNCERTAINTY_ESTIMATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExposureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.EXPOSURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getMethodologicalTreatmentOfLeftCensoredData() {
		if (methodologicalTreatmentOfLeftCensoredData == null) {
			methodologicalTreatmentOfLeftCensoredData = new EDataTypeUniqueEList<String>(String.class, this, MetadataPackage.EXPOSURE__METHODOLOGICAL_TREATMENT_OF_LEFT_CENSORED_DATA);
		}
		return methodologicalTreatmentOfLeftCensoredData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getLevelOfContaminationAfterLeftCensoredDataTreatment() {
		if (levelOfContaminationAfterLeftCensoredDataTreatment == null) {
			levelOfContaminationAfterLeftCensoredDataTreatment = new EDataTypeUniqueEList<String>(String.class, this, MetadataPackage.EXPOSURE__LEVEL_OF_CONTAMINATION_AFTER_LEFT_CENSORED_DATA_TREATMENT);
		}
		return levelOfContaminationAfterLeftCensoredDataTreatment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTypeOfExposure() {
		return typeOfExposure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypeOfExposure(String newTypeOfExposure) {
		String oldTypeOfExposure = typeOfExposure;
		typeOfExposure = newTypeOfExposure;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.EXPOSURE__TYPE_OF_EXPOSURE, oldTypeOfExposure, typeOfExposure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getScenario() {
		if (scenario == null) {
			scenario = new EDataTypeUniqueEList<String>(String.class, this, MetadataPackage.EXPOSURE__SCENARIO);
		}
		return scenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUncertaintyEstimation() {
		return uncertaintyEstimation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUncertaintyEstimation(String newUncertaintyEstimation) {
		String oldUncertaintyEstimation = uncertaintyEstimation;
		uncertaintyEstimation = newUncertaintyEstimation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.EXPOSURE__UNCERTAINTY_ESTIMATION, oldUncertaintyEstimation, uncertaintyEstimation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.EXPOSURE__METHODOLOGICAL_TREATMENT_OF_LEFT_CENSORED_DATA:
				return getMethodologicalTreatmentOfLeftCensoredData();
			case MetadataPackage.EXPOSURE__LEVEL_OF_CONTAMINATION_AFTER_LEFT_CENSORED_DATA_TREATMENT:
				return getLevelOfContaminationAfterLeftCensoredDataTreatment();
			case MetadataPackage.EXPOSURE__TYPE_OF_EXPOSURE:
				return getTypeOfExposure();
			case MetadataPackage.EXPOSURE__SCENARIO:
				return getScenario();
			case MetadataPackage.EXPOSURE__UNCERTAINTY_ESTIMATION:
				return getUncertaintyEstimation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MetadataPackage.EXPOSURE__METHODOLOGICAL_TREATMENT_OF_LEFT_CENSORED_DATA:
				getMethodologicalTreatmentOfLeftCensoredData().clear();
				getMethodologicalTreatmentOfLeftCensoredData().addAll((Collection<? extends String>)newValue);
				return;
			case MetadataPackage.EXPOSURE__LEVEL_OF_CONTAMINATION_AFTER_LEFT_CENSORED_DATA_TREATMENT:
				getLevelOfContaminationAfterLeftCensoredDataTreatment().clear();
				getLevelOfContaminationAfterLeftCensoredDataTreatment().addAll((Collection<? extends String>)newValue);
				return;
			case MetadataPackage.EXPOSURE__TYPE_OF_EXPOSURE:
				setTypeOfExposure((String)newValue);
				return;
			case MetadataPackage.EXPOSURE__SCENARIO:
				getScenario().clear();
				getScenario().addAll((Collection<? extends String>)newValue);
				return;
			case MetadataPackage.EXPOSURE__UNCERTAINTY_ESTIMATION:
				setUncertaintyEstimation((String)newValue);
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
			case MetadataPackage.EXPOSURE__METHODOLOGICAL_TREATMENT_OF_LEFT_CENSORED_DATA:
				getMethodologicalTreatmentOfLeftCensoredData().clear();
				return;
			case MetadataPackage.EXPOSURE__LEVEL_OF_CONTAMINATION_AFTER_LEFT_CENSORED_DATA_TREATMENT:
				getLevelOfContaminationAfterLeftCensoredDataTreatment().clear();
				return;
			case MetadataPackage.EXPOSURE__TYPE_OF_EXPOSURE:
				setTypeOfExposure(TYPE_OF_EXPOSURE_EDEFAULT);
				return;
			case MetadataPackage.EXPOSURE__SCENARIO:
				getScenario().clear();
				return;
			case MetadataPackage.EXPOSURE__UNCERTAINTY_ESTIMATION:
				setUncertaintyEstimation(UNCERTAINTY_ESTIMATION_EDEFAULT);
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
			case MetadataPackage.EXPOSURE__METHODOLOGICAL_TREATMENT_OF_LEFT_CENSORED_DATA:
				return methodologicalTreatmentOfLeftCensoredData != null && !methodologicalTreatmentOfLeftCensoredData.isEmpty();
			case MetadataPackage.EXPOSURE__LEVEL_OF_CONTAMINATION_AFTER_LEFT_CENSORED_DATA_TREATMENT:
				return levelOfContaminationAfterLeftCensoredDataTreatment != null && !levelOfContaminationAfterLeftCensoredDataTreatment.isEmpty();
			case MetadataPackage.EXPOSURE__TYPE_OF_EXPOSURE:
				return TYPE_OF_EXPOSURE_EDEFAULT == null ? typeOfExposure != null : !TYPE_OF_EXPOSURE_EDEFAULT.equals(typeOfExposure);
			case MetadataPackage.EXPOSURE__SCENARIO:
				return scenario != null && !scenario.isEmpty();
			case MetadataPackage.EXPOSURE__UNCERTAINTY_ESTIMATION:
				return UNCERTAINTY_ESTIMATION_EDEFAULT == null ? uncertaintyEstimation != null : !UNCERTAINTY_ESTIMATION_EDEFAULT.equals(uncertaintyEstimation);
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
		result.append(" (methodologicalTreatmentOfLeftCensoredData: ");
		result.append(methodologicalTreatmentOfLeftCensoredData);
		result.append(", levelOfContaminationAfterLeftCensoredDataTreatment: ");
		result.append(levelOfContaminationAfterLeftCensoredDataTreatment);
		result.append(", typeOfExposure: ");
		result.append(typeOfExposure);
		result.append(", scenario: ");
		result.append(scenario);
		result.append(", uncertaintyEstimation: ");
		result.append(uncertaintyEstimation);
		result.append(')');
		return result.toString();
	}

} //ExposureImpl
