/**
 */
package metadata.impl;

import metadata.MetadataPackage;
import metadata.Parameter;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.Reference;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterID <em>Parameter ID</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterClassification <em>Parameter Classification</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterName <em>Parameter Name</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterDescription <em>Parameter Description</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterType <em>Parameter Type</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterUnit <em>Parameter Unit</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterUnitCategory <em>Parameter Unit Category</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterDataType <em>Parameter Data Type</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterSource <em>Parameter Source</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterSubject <em>Parameter Subject</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterDistribution <em>Parameter Distribution</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterValue <em>Parameter Value</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterVariabilitySubject <em>Parameter Variability Subject</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterValueMin <em>Parameter Value Min</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterValueMax <em>Parameter Value Max</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getParameterError <em>Parameter Error</em>}</li>
 *   <li>{@link metadata.impl.ParameterImpl#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParameterImpl extends MinimalEObjectImpl.Container implements Parameter {
	/**
	 * The default value of the '{@link #getParameterID() <em>Parameter ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterID()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterID() <em>Parameter ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterID()
	 * @generated
	 * @ordered
	 */
	protected String parameterID = PARAMETER_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterClassification() <em>Parameter Classification</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterClassification()
	 * @generated
	 * @ordered
	 */
	protected static final ParameterClassification PARAMETER_CLASSIFICATION_EDEFAULT = ParameterClassification.CONSTANT;

	/**
	 * The cached value of the '{@link #getParameterClassification() <em>Parameter Classification</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterClassification()
	 * @generated
	 * @ordered
	 */
	protected ParameterClassification parameterClassification = PARAMETER_CLASSIFICATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterName() <em>Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterName()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterName() <em>Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterName()
	 * @generated
	 * @ordered
	 */
	protected String parameterName = PARAMETER_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterDescription() <em>Parameter Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterDescription() <em>Parameter Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterDescription()
	 * @generated
	 * @ordered
	 */
	protected String parameterDescription = PARAMETER_DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterType() <em>Parameter Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterType()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterType() <em>Parameter Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterType()
	 * @generated
	 * @ordered
	 */
	protected String parameterType = PARAMETER_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterUnit() <em>Parameter Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterUnit()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_UNIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterUnit() <em>Parameter Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterUnit()
	 * @generated
	 * @ordered
	 */
	protected String parameterUnit = PARAMETER_UNIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterUnitCategory() <em>Parameter Unit Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterUnitCategory()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_UNIT_CATEGORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterUnitCategory() <em>Parameter Unit Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterUnitCategory()
	 * @generated
	 * @ordered
	 */
	protected String parameterUnitCategory = PARAMETER_UNIT_CATEGORY_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterDataType() <em>Parameter Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterDataType()
	 * @generated
	 * @ordered
	 */
	protected static final ParameterType PARAMETER_DATA_TYPE_EDEFAULT = ParameterType.INTEGER;

	/**
	 * The cached value of the '{@link #getParameterDataType() <em>Parameter Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterDataType()
	 * @generated
	 * @ordered
	 */
	protected ParameterType parameterDataType = PARAMETER_DATA_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterSource() <em>Parameter Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterSource()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_SOURCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterSource() <em>Parameter Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterSource()
	 * @generated
	 * @ordered
	 */
	protected String parameterSource = PARAMETER_SOURCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterSubject() <em>Parameter Subject</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterSubject()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_SUBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterSubject() <em>Parameter Subject</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterSubject()
	 * @generated
	 * @ordered
	 */
	protected String parameterSubject = PARAMETER_SUBJECT_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterDistribution() <em>Parameter Distribution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterDistribution()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_DISTRIBUTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterDistribution() <em>Parameter Distribution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterDistribution()
	 * @generated
	 * @ordered
	 */
	protected String parameterDistribution = PARAMETER_DISTRIBUTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterValue() <em>Parameter Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterValue()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterValue() <em>Parameter Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterValue()
	 * @generated
	 * @ordered
	 */
	protected String parameterValue = PARAMETER_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterVariabilitySubject() <em>Parameter Variability Subject</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterVariabilitySubject()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_VARIABILITY_SUBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterVariabilitySubject() <em>Parameter Variability Subject</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterVariabilitySubject()
	 * @generated
	 * @ordered
	 */
	protected String parameterVariabilitySubject = PARAMETER_VARIABILITY_SUBJECT_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterValueMin() <em>Parameter Value Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterValueMin()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_VALUE_MIN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterValueMin() <em>Parameter Value Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterValueMin()
	 * @generated
	 * @ordered
	 */
	protected String parameterValueMin = PARAMETER_VALUE_MIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterValueMax() <em>Parameter Value Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterValueMax()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_VALUE_MAX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterValueMax() <em>Parameter Value Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterValueMax()
	 * @generated
	 * @ordered
	 */
	protected String parameterValueMax = PARAMETER_VALUE_MAX_EDEFAULT;

	/**
	 * The default value of the '{@link #getParameterError() <em>Parameter Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterError()
	 * @generated
	 * @ordered
	 */
	protected static final String PARAMETER_ERROR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParameterError() <em>Parameter Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterError()
	 * @generated
	 * @ordered
	 */
	protected String parameterError = PARAMETER_ERROR_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReference() <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReference()
	 * @generated
	 * @ordered
	 */
	protected Reference reference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterID() {
		return parameterID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterID(String newParameterID) {
		String oldParameterID = parameterID;
		parameterID = newParameterID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_ID, oldParameterID, parameterID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterClassification getParameterClassification() {
		return parameterClassification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterClassification(ParameterClassification newParameterClassification) {
		ParameterClassification oldParameterClassification = parameterClassification;
		parameterClassification = newParameterClassification == null ? PARAMETER_CLASSIFICATION_EDEFAULT : newParameterClassification;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_CLASSIFICATION, oldParameterClassification, parameterClassification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterName(String newParameterName) {
		String oldParameterName = parameterName;
		parameterName = newParameterName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_NAME, oldParameterName, parameterName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterDescription() {
		return parameterDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterDescription(String newParameterDescription) {
		String oldParameterDescription = parameterDescription;
		parameterDescription = newParameterDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_DESCRIPTION, oldParameterDescription, parameterDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterType() {
		return parameterType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterType(String newParameterType) {
		String oldParameterType = parameterType;
		parameterType = newParameterType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_TYPE, oldParameterType, parameterType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterUnit() {
		return parameterUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterUnit(String newParameterUnit) {
		String oldParameterUnit = parameterUnit;
		parameterUnit = newParameterUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_UNIT, oldParameterUnit, parameterUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterUnitCategory() {
		return parameterUnitCategory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterUnitCategory(String newParameterUnitCategory) {
		String oldParameterUnitCategory = parameterUnitCategory;
		parameterUnitCategory = newParameterUnitCategory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_UNIT_CATEGORY, oldParameterUnitCategory, parameterUnitCategory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterType getParameterDataType() {
		return parameterDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterDataType(ParameterType newParameterDataType) {
		ParameterType oldParameterDataType = parameterDataType;
		parameterDataType = newParameterDataType == null ? PARAMETER_DATA_TYPE_EDEFAULT : newParameterDataType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_DATA_TYPE, oldParameterDataType, parameterDataType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterSource() {
		return parameterSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterSource(String newParameterSource) {
		String oldParameterSource = parameterSource;
		parameterSource = newParameterSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_SOURCE, oldParameterSource, parameterSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterSubject() {
		return parameterSubject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterSubject(String newParameterSubject) {
		String oldParameterSubject = parameterSubject;
		parameterSubject = newParameterSubject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_SUBJECT, oldParameterSubject, parameterSubject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterDistribution() {
		return parameterDistribution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterDistribution(String newParameterDistribution) {
		String oldParameterDistribution = parameterDistribution;
		parameterDistribution = newParameterDistribution;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_DISTRIBUTION, oldParameterDistribution, parameterDistribution));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterValue() {
		return parameterValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterValue(String newParameterValue) {
		String oldParameterValue = parameterValue;
		parameterValue = newParameterValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_VALUE, oldParameterValue, parameterValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterVariabilitySubject() {
		return parameterVariabilitySubject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterVariabilitySubject(String newParameterVariabilitySubject) {
		String oldParameterVariabilitySubject = parameterVariabilitySubject;
		parameterVariabilitySubject = newParameterVariabilitySubject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_VARIABILITY_SUBJECT, oldParameterVariabilitySubject, parameterVariabilitySubject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterValueMin() {
		return parameterValueMin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterValueMin(String newParameterValueMin) {
		String oldParameterValueMin = parameterValueMin;
		parameterValueMin = newParameterValueMin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_VALUE_MIN, oldParameterValueMin, parameterValueMin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterValueMax() {
		return parameterValueMax;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterValueMax(String newParameterValueMax) {
		String oldParameterValueMax = parameterValueMax;
		parameterValueMax = newParameterValueMax;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_VALUE_MAX, oldParameterValueMax, parameterValueMax));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParameterError() {
		return parameterError;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameterError(String newParameterError) {
		String oldParameterError = parameterError;
		parameterError = newParameterError;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__PARAMETER_ERROR, oldParameterError, parameterError));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Reference getReference() {
		if (reference != null && reference.eIsProxy()) {
			InternalEObject oldReference = (InternalEObject)reference;
			reference = (Reference)eResolveProxy(oldReference);
			if (reference != oldReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetadataPackage.PARAMETER__REFERENCE, oldReference, reference));
			}
		}
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Reference basicGetReference() {
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReference(Reference newReference) {
		Reference oldReference = reference;
		reference = newReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PARAMETER__REFERENCE, oldReference, reference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.PARAMETER__PARAMETER_ID:
				return getParameterID();
			case MetadataPackage.PARAMETER__PARAMETER_CLASSIFICATION:
				return getParameterClassification();
			case MetadataPackage.PARAMETER__PARAMETER_NAME:
				return getParameterName();
			case MetadataPackage.PARAMETER__PARAMETER_DESCRIPTION:
				return getParameterDescription();
			case MetadataPackage.PARAMETER__PARAMETER_TYPE:
				return getParameterType();
			case MetadataPackage.PARAMETER__PARAMETER_UNIT:
				return getParameterUnit();
			case MetadataPackage.PARAMETER__PARAMETER_UNIT_CATEGORY:
				return getParameterUnitCategory();
			case MetadataPackage.PARAMETER__PARAMETER_DATA_TYPE:
				return getParameterDataType();
			case MetadataPackage.PARAMETER__PARAMETER_SOURCE:
				return getParameterSource();
			case MetadataPackage.PARAMETER__PARAMETER_SUBJECT:
				return getParameterSubject();
			case MetadataPackage.PARAMETER__PARAMETER_DISTRIBUTION:
				return getParameterDistribution();
			case MetadataPackage.PARAMETER__PARAMETER_VALUE:
				return getParameterValue();
			case MetadataPackage.PARAMETER__PARAMETER_VARIABILITY_SUBJECT:
				return getParameterVariabilitySubject();
			case MetadataPackage.PARAMETER__PARAMETER_VALUE_MIN:
				return getParameterValueMin();
			case MetadataPackage.PARAMETER__PARAMETER_VALUE_MAX:
				return getParameterValueMax();
			case MetadataPackage.PARAMETER__PARAMETER_ERROR:
				return getParameterError();
			case MetadataPackage.PARAMETER__REFERENCE:
				if (resolve) return getReference();
				return basicGetReference();
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
			case MetadataPackage.PARAMETER__PARAMETER_ID:
				setParameterID((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_CLASSIFICATION:
				setParameterClassification((ParameterClassification)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_NAME:
				setParameterName((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_DESCRIPTION:
				setParameterDescription((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_TYPE:
				setParameterType((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_UNIT:
				setParameterUnit((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_UNIT_CATEGORY:
				setParameterUnitCategory((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_DATA_TYPE:
				setParameterDataType((ParameterType)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_SOURCE:
				setParameterSource((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_SUBJECT:
				setParameterSubject((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_DISTRIBUTION:
				setParameterDistribution((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_VALUE:
				setParameterValue((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_VARIABILITY_SUBJECT:
				setParameterVariabilitySubject((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_VALUE_MIN:
				setParameterValueMin((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_VALUE_MAX:
				setParameterValueMax((String)newValue);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_ERROR:
				setParameterError((String)newValue);
				return;
			case MetadataPackage.PARAMETER__REFERENCE:
				setReference((Reference)newValue);
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
			case MetadataPackage.PARAMETER__PARAMETER_ID:
				setParameterID(PARAMETER_ID_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_CLASSIFICATION:
				setParameterClassification(PARAMETER_CLASSIFICATION_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_NAME:
				setParameterName(PARAMETER_NAME_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_DESCRIPTION:
				setParameterDescription(PARAMETER_DESCRIPTION_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_TYPE:
				setParameterType(PARAMETER_TYPE_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_UNIT:
				setParameterUnit(PARAMETER_UNIT_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_UNIT_CATEGORY:
				setParameterUnitCategory(PARAMETER_UNIT_CATEGORY_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_DATA_TYPE:
				setParameterDataType(PARAMETER_DATA_TYPE_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_SOURCE:
				setParameterSource(PARAMETER_SOURCE_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_SUBJECT:
				setParameterSubject(PARAMETER_SUBJECT_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_DISTRIBUTION:
				setParameterDistribution(PARAMETER_DISTRIBUTION_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_VALUE:
				setParameterValue(PARAMETER_VALUE_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_VARIABILITY_SUBJECT:
				setParameterVariabilitySubject(PARAMETER_VARIABILITY_SUBJECT_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_VALUE_MIN:
				setParameterValueMin(PARAMETER_VALUE_MIN_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_VALUE_MAX:
				setParameterValueMax(PARAMETER_VALUE_MAX_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__PARAMETER_ERROR:
				setParameterError(PARAMETER_ERROR_EDEFAULT);
				return;
			case MetadataPackage.PARAMETER__REFERENCE:
				setReference((Reference)null);
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
			case MetadataPackage.PARAMETER__PARAMETER_ID:
				return PARAMETER_ID_EDEFAULT == null ? parameterID != null : !PARAMETER_ID_EDEFAULT.equals(parameterID);
			case MetadataPackage.PARAMETER__PARAMETER_CLASSIFICATION:
				return parameterClassification != PARAMETER_CLASSIFICATION_EDEFAULT;
			case MetadataPackage.PARAMETER__PARAMETER_NAME:
				return PARAMETER_NAME_EDEFAULT == null ? parameterName != null : !PARAMETER_NAME_EDEFAULT.equals(parameterName);
			case MetadataPackage.PARAMETER__PARAMETER_DESCRIPTION:
				return PARAMETER_DESCRIPTION_EDEFAULT == null ? parameterDescription != null : !PARAMETER_DESCRIPTION_EDEFAULT.equals(parameterDescription);
			case MetadataPackage.PARAMETER__PARAMETER_TYPE:
				return PARAMETER_TYPE_EDEFAULT == null ? parameterType != null : !PARAMETER_TYPE_EDEFAULT.equals(parameterType);
			case MetadataPackage.PARAMETER__PARAMETER_UNIT:
				return PARAMETER_UNIT_EDEFAULT == null ? parameterUnit != null : !PARAMETER_UNIT_EDEFAULT.equals(parameterUnit);
			case MetadataPackage.PARAMETER__PARAMETER_UNIT_CATEGORY:
				return PARAMETER_UNIT_CATEGORY_EDEFAULT == null ? parameterUnitCategory != null : !PARAMETER_UNIT_CATEGORY_EDEFAULT.equals(parameterUnitCategory);
			case MetadataPackage.PARAMETER__PARAMETER_DATA_TYPE:
				return parameterDataType != PARAMETER_DATA_TYPE_EDEFAULT;
			case MetadataPackage.PARAMETER__PARAMETER_SOURCE:
				return PARAMETER_SOURCE_EDEFAULT == null ? parameterSource != null : !PARAMETER_SOURCE_EDEFAULT.equals(parameterSource);
			case MetadataPackage.PARAMETER__PARAMETER_SUBJECT:
				return PARAMETER_SUBJECT_EDEFAULT == null ? parameterSubject != null : !PARAMETER_SUBJECT_EDEFAULT.equals(parameterSubject);
			case MetadataPackage.PARAMETER__PARAMETER_DISTRIBUTION:
				return PARAMETER_DISTRIBUTION_EDEFAULT == null ? parameterDistribution != null : !PARAMETER_DISTRIBUTION_EDEFAULT.equals(parameterDistribution);
			case MetadataPackage.PARAMETER__PARAMETER_VALUE:
				return PARAMETER_VALUE_EDEFAULT == null ? parameterValue != null : !PARAMETER_VALUE_EDEFAULT.equals(parameterValue);
			case MetadataPackage.PARAMETER__PARAMETER_VARIABILITY_SUBJECT:
				return PARAMETER_VARIABILITY_SUBJECT_EDEFAULT == null ? parameterVariabilitySubject != null : !PARAMETER_VARIABILITY_SUBJECT_EDEFAULT.equals(parameterVariabilitySubject);
			case MetadataPackage.PARAMETER__PARAMETER_VALUE_MIN:
				return PARAMETER_VALUE_MIN_EDEFAULT == null ? parameterValueMin != null : !PARAMETER_VALUE_MIN_EDEFAULT.equals(parameterValueMin);
			case MetadataPackage.PARAMETER__PARAMETER_VALUE_MAX:
				return PARAMETER_VALUE_MAX_EDEFAULT == null ? parameterValueMax != null : !PARAMETER_VALUE_MAX_EDEFAULT.equals(parameterValueMax);
			case MetadataPackage.PARAMETER__PARAMETER_ERROR:
				return PARAMETER_ERROR_EDEFAULT == null ? parameterError != null : !PARAMETER_ERROR_EDEFAULT.equals(parameterError);
			case MetadataPackage.PARAMETER__REFERENCE:
				return reference != null;
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
		result.append(" (parameterID: ");
		result.append(parameterID);
		result.append(", parameterClassification: ");
		result.append(parameterClassification);
		result.append(", parameterName: ");
		result.append(parameterName);
		result.append(", parameterDescription: ");
		result.append(parameterDescription);
		result.append(", parameterType: ");
		result.append(parameterType);
		result.append(", parameterUnit: ");
		result.append(parameterUnit);
		result.append(", parameterUnitCategory: ");
		result.append(parameterUnitCategory);
		result.append(", parameterDataType: ");
		result.append(parameterDataType);
		result.append(", parameterSource: ");
		result.append(parameterSource);
		result.append(", parameterSubject: ");
		result.append(parameterSubject);
		result.append(", parameterDistribution: ");
		result.append(parameterDistribution);
		result.append(", parameterValue: ");
		result.append(parameterValue);
		result.append(", parameterVariabilitySubject: ");
		result.append(parameterVariabilitySubject);
		result.append(", parameterValueMin: ");
		result.append(parameterValueMin);
		result.append(", parameterValueMax: ");
		result.append(parameterValueMax);
		result.append(", parameterError: ");
		result.append(parameterError);
		result.append(')');
		return result.toString();
	}

} //ParameterImpl
