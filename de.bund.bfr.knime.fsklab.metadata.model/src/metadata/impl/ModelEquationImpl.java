/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.MetadataPackage;
import metadata.ModelEquation;
import metadata.Reference;
import metadata.StringObject;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Equation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.ModelEquationImpl#getModelEquationName <em>Model Equation Name</em>}</li>
 *   <li>{@link metadata.impl.ModelEquationImpl#getModelEquationClass <em>Model Equation Class</em>}</li>
 *   <li>{@link metadata.impl.ModelEquationImpl#getModelEquation <em>Model Equation</em>}</li>
 *   <li>{@link metadata.impl.ModelEquationImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link metadata.impl.ModelEquationImpl#getHypothesisOfTheModel <em>Hypothesis Of The Model</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelEquationImpl extends MinimalEObjectImpl.Container implements ModelEquation {
	/**
	 * The default value of the '{@link #getModelEquationName() <em>Model Equation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelEquationName()
	 * @generated
	 * @ordered
	 */
	protected static final String MODEL_EQUATION_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModelEquationName() <em>Model Equation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelEquationName()
	 * @generated
	 * @ordered
	 */
	protected String modelEquationName = MODEL_EQUATION_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getModelEquationClass() <em>Model Equation Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelEquationClass()
	 * @generated
	 * @ordered
	 */
	protected static final String MODEL_EQUATION_CLASS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModelEquationClass() <em>Model Equation Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelEquationClass()
	 * @generated
	 * @ordered
	 */
	protected String modelEquationClass = MODEL_EQUATION_CLASS_EDEFAULT;

	/**
	 * The default value of the '{@link #getModelEquation() <em>Model Equation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelEquation()
	 * @generated
	 * @ordered
	 */
	protected static final String MODEL_EQUATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModelEquation() <em>Model Equation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelEquation()
	 * @generated
	 * @ordered
	 */
	protected String modelEquation = MODEL_EQUATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReference() <em>Reference</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReference()
	 * @generated
	 * @ordered
	 */
	protected EList<Reference> reference;

	/**
	 * The cached value of the '{@link #getHypothesisOfTheModel() <em>Hypothesis Of The Model</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHypothesisOfTheModel()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> hypothesisOfTheModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelEquationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.MODEL_EQUATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getModelEquationName() {
		return modelEquationName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelEquationName(String newModelEquationName) {
		String oldModelEquationName = modelEquationName;
		modelEquationName = newModelEquationName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_NAME, oldModelEquationName, modelEquationName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getModelEquationClass() {
		return modelEquationClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelEquationClass(String newModelEquationClass) {
		String oldModelEquationClass = modelEquationClass;
		modelEquationClass = newModelEquationClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_CLASS, oldModelEquationClass, modelEquationClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getModelEquation() {
		return modelEquation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelEquation(String newModelEquation) {
		String oldModelEquation = modelEquation;
		modelEquation = newModelEquation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_EQUATION__MODEL_EQUATION, oldModelEquation, modelEquation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Reference> getReference() {
		if (reference == null) {
			reference = new EObjectContainmentEList<Reference>(Reference.class, this, MetadataPackage.MODEL_EQUATION__REFERENCE);
		}
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getHypothesisOfTheModel() {
		if (hypothesisOfTheModel == null) {
			hypothesisOfTheModel = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL);
		}
		return hypothesisOfTheModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MetadataPackage.MODEL_EQUATION__REFERENCE:
				return ((InternalEList<?>)getReference()).basicRemove(otherEnd, msgs);
			case MetadataPackage.MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL:
				return ((InternalEList<?>)getHypothesisOfTheModel()).basicRemove(otherEnd, msgs);
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
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_NAME:
				return getModelEquationName();
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_CLASS:
				return getModelEquationClass();
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION:
				return getModelEquation();
			case MetadataPackage.MODEL_EQUATION__REFERENCE:
				return getReference();
			case MetadataPackage.MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL:
				return getHypothesisOfTheModel();
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
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_NAME:
				setModelEquationName((String)newValue);
				return;
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_CLASS:
				setModelEquationClass((String)newValue);
				return;
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION:
				setModelEquation((String)newValue);
				return;
			case MetadataPackage.MODEL_EQUATION__REFERENCE:
				getReference().clear();
				getReference().addAll((Collection<? extends Reference>)newValue);
				return;
			case MetadataPackage.MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL:
				getHypothesisOfTheModel().clear();
				getHypothesisOfTheModel().addAll((Collection<? extends StringObject>)newValue);
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
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_NAME:
				setModelEquationName(MODEL_EQUATION_NAME_EDEFAULT);
				return;
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_CLASS:
				setModelEquationClass(MODEL_EQUATION_CLASS_EDEFAULT);
				return;
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION:
				setModelEquation(MODEL_EQUATION_EDEFAULT);
				return;
			case MetadataPackage.MODEL_EQUATION__REFERENCE:
				getReference().clear();
				return;
			case MetadataPackage.MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL:
				getHypothesisOfTheModel().clear();
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
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_NAME:
				return MODEL_EQUATION_NAME_EDEFAULT == null ? modelEquationName != null : !MODEL_EQUATION_NAME_EDEFAULT.equals(modelEquationName);
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION_CLASS:
				return MODEL_EQUATION_CLASS_EDEFAULT == null ? modelEquationClass != null : !MODEL_EQUATION_CLASS_EDEFAULT.equals(modelEquationClass);
			case MetadataPackage.MODEL_EQUATION__MODEL_EQUATION:
				return MODEL_EQUATION_EDEFAULT == null ? modelEquation != null : !MODEL_EQUATION_EDEFAULT.equals(modelEquation);
			case MetadataPackage.MODEL_EQUATION__REFERENCE:
				return reference != null && !reference.isEmpty();
			case MetadataPackage.MODEL_EQUATION__HYPOTHESIS_OF_THE_MODEL:
				return hypothesisOfTheModel != null && !hypothesisOfTheModel.isEmpty();
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
		result.append(" (modelEquationName: ");
		result.append(modelEquationName);
		result.append(", modelEquationClass: ");
		result.append(modelEquationClass);
		result.append(", modelEquation: ");
		result.append(modelEquation);
		result.append(')');
		return result.toString();
	}

} //ModelEquationImpl
