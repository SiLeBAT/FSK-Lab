/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.Exposure;
import metadata.MetadataPackage;
import metadata.ModelEquation;
import metadata.ModelMath;
import metadata.Parameter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Math</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.ModelMathImpl#getQualityMeasures <em>Quality Measures</em>}</li>
 *   <li>{@link metadata.impl.ModelMathImpl#getFittingProcedure <em>Fitting Procedure</em>}</li>
 *   <li>{@link metadata.impl.ModelMathImpl#getEvent <em>Event</em>}</li>
 *   <li>{@link metadata.impl.ModelMathImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link metadata.impl.ModelMathImpl#getModelEquation <em>Model Equation</em>}</li>
 *   <li>{@link metadata.impl.ModelMathImpl#getExposure <em>Exposure</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelMathImpl extends MinimalEObjectImpl.Container implements ModelMath {
	/**
	 * The cached value of the '{@link #getQualityMeasures() <em>Quality Measures</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQualityMeasures()
	 * @generated
	 * @ordered
	 */
	protected EList<String> qualityMeasures;

	/**
	 * The default value of the '{@link #getFittingProcedure() <em>Fitting Procedure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFittingProcedure()
	 * @generated
	 * @ordered
	 */
	protected static final String FITTING_PROCEDURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFittingProcedure() <em>Fitting Procedure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFittingProcedure()
	 * @generated
	 * @ordered
	 */
	protected String fittingProcedure = FITTING_PROCEDURE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEvent() <em>Event</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvent()
	 * @generated
	 * @ordered
	 */
	protected EList<String> event;

	/**
	 * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter()
	 * @generated
	 * @ordered
	 */
	protected EList<Parameter> parameter;

	/**
	 * The cached value of the '{@link #getModelEquation() <em>Model Equation</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelEquation()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelEquation> modelEquation;

	/**
	 * The cached value of the '{@link #getExposure() <em>Exposure</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExposure()
	 * @generated
	 * @ordered
	 */
	protected Exposure exposure;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelMathImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.MODEL_MATH;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getQualityMeasures() {
		if (qualityMeasures == null) {
			qualityMeasures = new EDataTypeUniqueEList<String>(String.class, this, MetadataPackage.MODEL_MATH__QUALITY_MEASURES);
		}
		return qualityMeasures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFittingProcedure() {
		return fittingProcedure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFittingProcedure(String newFittingProcedure) {
		String oldFittingProcedure = fittingProcedure;
		fittingProcedure = newFittingProcedure;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_MATH__FITTING_PROCEDURE, oldFittingProcedure, fittingProcedure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getEvent() {
		if (event == null) {
			event = new EDataTypeUniqueEList<String>(String.class, this, MetadataPackage.MODEL_MATH__EVENT);
		}
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Parameter> getParameter() {
		if (parameter == null) {
			parameter = new EObjectContainmentEList<Parameter>(Parameter.class, this, MetadataPackage.MODEL_MATH__PARAMETER);
		}
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelEquation> getModelEquation() {
		if (modelEquation == null) {
			modelEquation = new EObjectContainmentEList<ModelEquation>(ModelEquation.class, this, MetadataPackage.MODEL_MATH__MODEL_EQUATION);
		}
		return modelEquation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Exposure getExposure() {
		return exposure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExposure(Exposure newExposure, NotificationChain msgs) {
		Exposure oldExposure = exposure;
		exposure = newExposure;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_MATH__EXPOSURE, oldExposure, newExposure);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExposure(Exposure newExposure) {
		if (newExposure != exposure) {
			NotificationChain msgs = null;
			if (exposure != null)
				msgs = ((InternalEObject)exposure).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.MODEL_MATH__EXPOSURE, null, msgs);
			if (newExposure != null)
				msgs = ((InternalEObject)newExposure).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.MODEL_MATH__EXPOSURE, null, msgs);
			msgs = basicSetExposure(newExposure, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_MATH__EXPOSURE, newExposure, newExposure));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MetadataPackage.MODEL_MATH__PARAMETER:
				return ((InternalEList<?>)getParameter()).basicRemove(otherEnd, msgs);
			case MetadataPackage.MODEL_MATH__MODEL_EQUATION:
				return ((InternalEList<?>)getModelEquation()).basicRemove(otherEnd, msgs);
			case MetadataPackage.MODEL_MATH__EXPOSURE:
				return basicSetExposure(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.MODEL_MATH__QUALITY_MEASURES:
				return getQualityMeasures();
			case MetadataPackage.MODEL_MATH__FITTING_PROCEDURE:
				return getFittingProcedure();
			case MetadataPackage.MODEL_MATH__EVENT:
				return getEvent();
			case MetadataPackage.MODEL_MATH__PARAMETER:
				return getParameter();
			case MetadataPackage.MODEL_MATH__MODEL_EQUATION:
				return getModelEquation();
			case MetadataPackage.MODEL_MATH__EXPOSURE:
				return getExposure();
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
			case MetadataPackage.MODEL_MATH__QUALITY_MEASURES:
				getQualityMeasures().clear();
				getQualityMeasures().addAll((Collection<? extends String>)newValue);
				return;
			case MetadataPackage.MODEL_MATH__FITTING_PROCEDURE:
				setFittingProcedure((String)newValue);
				return;
			case MetadataPackage.MODEL_MATH__EVENT:
				getEvent().clear();
				getEvent().addAll((Collection<? extends String>)newValue);
				return;
			case MetadataPackage.MODEL_MATH__PARAMETER:
				getParameter().clear();
				getParameter().addAll((Collection<? extends Parameter>)newValue);
				return;
			case MetadataPackage.MODEL_MATH__MODEL_EQUATION:
				getModelEquation().clear();
				getModelEquation().addAll((Collection<? extends ModelEquation>)newValue);
				return;
			case MetadataPackage.MODEL_MATH__EXPOSURE:
				setExposure((Exposure)newValue);
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
			case MetadataPackage.MODEL_MATH__QUALITY_MEASURES:
				getQualityMeasures().clear();
				return;
			case MetadataPackage.MODEL_MATH__FITTING_PROCEDURE:
				setFittingProcedure(FITTING_PROCEDURE_EDEFAULT);
				return;
			case MetadataPackage.MODEL_MATH__EVENT:
				getEvent().clear();
				return;
			case MetadataPackage.MODEL_MATH__PARAMETER:
				getParameter().clear();
				return;
			case MetadataPackage.MODEL_MATH__MODEL_EQUATION:
				getModelEquation().clear();
				return;
			case MetadataPackage.MODEL_MATH__EXPOSURE:
				setExposure((Exposure)null);
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
			case MetadataPackage.MODEL_MATH__QUALITY_MEASURES:
				return qualityMeasures != null && !qualityMeasures.isEmpty();
			case MetadataPackage.MODEL_MATH__FITTING_PROCEDURE:
				return FITTING_PROCEDURE_EDEFAULT == null ? fittingProcedure != null : !FITTING_PROCEDURE_EDEFAULT.equals(fittingProcedure);
			case MetadataPackage.MODEL_MATH__EVENT:
				return event != null && !event.isEmpty();
			case MetadataPackage.MODEL_MATH__PARAMETER:
				return parameter != null && !parameter.isEmpty();
			case MetadataPackage.MODEL_MATH__MODEL_EQUATION:
				return modelEquation != null && !modelEquation.isEmpty();
			case MetadataPackage.MODEL_MATH__EXPOSURE:
				return exposure != null;
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
		result.append(" (qualityMeasures: ");
		result.append(qualityMeasures);
		result.append(", fittingProcedure: ");
		result.append(fittingProcedure);
		result.append(", event: ");
		result.append(event);
		result.append(')');
		return result.toString();
	}

} //ModelMathImpl
