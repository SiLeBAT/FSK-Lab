/**
 */
package metadata.impl;

import metadata.MetadataPackage;
import metadata.modelApplicability;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>model Applicability</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.modelApplicabilityImpl#getModelApplicability <em>Model Applicability</em>}</li>
 * </ul>
 *
 * @generated
 */
public class modelApplicabilityImpl extends MinimalEObjectImpl.Container implements modelApplicability {
	/**
	 * The default value of the '{@link #getModelApplicability() <em>Model Applicability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelApplicability()
	 * @generated
	 * @ordered
	 */
	protected static final String MODEL_APPLICABILITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModelApplicability() <em>Model Applicability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelApplicability()
	 * @generated
	 * @ordered
	 */
	protected String modelApplicability = MODEL_APPLICABILITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected modelApplicabilityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.MODEL_APPLICABILITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getModelApplicability() {
		return modelApplicability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelApplicability(String newModelApplicability) {
		String oldModelApplicability = modelApplicability;
		modelApplicability = newModelApplicability;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_APPLICABILITY__MODEL_APPLICABILITY, oldModelApplicability, modelApplicability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.MODEL_APPLICABILITY__MODEL_APPLICABILITY:
				return getModelApplicability();
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
			case MetadataPackage.MODEL_APPLICABILITY__MODEL_APPLICABILITY:
				setModelApplicability((String)newValue);
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
			case MetadataPackage.MODEL_APPLICABILITY__MODEL_APPLICABILITY:
				setModelApplicability(MODEL_APPLICABILITY_EDEFAULT);
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
			case MetadataPackage.MODEL_APPLICABILITY__MODEL_APPLICABILITY:
				return MODEL_APPLICABILITY_EDEFAULT == null ? modelApplicability != null : !MODEL_APPLICABILITY_EDEFAULT.equals(modelApplicability);
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
		result.append(" (modelApplicability: ");
		result.append(modelApplicability);
		result.append(')');
		return result.toString();
	}

} //modelApplicabilityImpl
