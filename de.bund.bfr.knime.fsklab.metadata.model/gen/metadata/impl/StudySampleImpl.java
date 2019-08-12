/**
 */
package metadata.impl;

import metadata.MetadataPackage;
import metadata.StudySample;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Study Sample</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.StudySampleImpl#getSampleName <em>Sample Name</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getProtocolOfSampleCollection <em>Protocol Of Sample Collection</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getSamplingStrategy <em>Sampling Strategy</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getTypeOfSamplingProgram <em>Type Of Sampling Program</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getSamplingMethod <em>Sampling Method</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getSamplingPlan <em>Sampling Plan</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getSamplingWeight <em>Sampling Weight</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getSamplingSize <em>Sampling Size</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getLotSizeUnit <em>Lot Size Unit</em>}</li>
 *   <li>{@link metadata.impl.StudySampleImpl#getSamplingPoint <em>Sampling Point</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StudySampleImpl extends MinimalEObjectImpl.Container implements StudySample {
	/**
	 * The default value of the '{@link #getSampleName() <em>Sample Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSampleName()
	 * @generated
	 * @ordered
	 */
	protected static final String SAMPLE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSampleName() <em>Sample Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSampleName()
	 * @generated
	 * @ordered
	 */
	protected String sampleName = SAMPLE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getProtocolOfSampleCollection() <em>Protocol Of Sample Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProtocolOfSampleCollection()
	 * @generated
	 * @ordered
	 */
	protected static final String PROTOCOL_OF_SAMPLE_COLLECTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProtocolOfSampleCollection() <em>Protocol Of Sample Collection</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProtocolOfSampleCollection()
	 * @generated
	 * @ordered
	 */
	protected String protocolOfSampleCollection = PROTOCOL_OF_SAMPLE_COLLECTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getSamplingStrategy() <em>Sampling Strategy</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingStrategy()
	 * @generated
	 * @ordered
	 */
	protected static final String SAMPLING_STRATEGY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSamplingStrategy() <em>Sampling Strategy</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingStrategy()
	 * @generated
	 * @ordered
	 */
	protected String samplingStrategy = SAMPLING_STRATEGY_EDEFAULT;

	/**
	 * The default value of the '{@link #getTypeOfSamplingProgram() <em>Type Of Sampling Program</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeOfSamplingProgram()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_OF_SAMPLING_PROGRAM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTypeOfSamplingProgram() <em>Type Of Sampling Program</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeOfSamplingProgram()
	 * @generated
	 * @ordered
	 */
	protected String typeOfSamplingProgram = TYPE_OF_SAMPLING_PROGRAM_EDEFAULT;

	/**
	 * The default value of the '{@link #getSamplingMethod() <em>Sampling Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingMethod()
	 * @generated
	 * @ordered
	 */
	protected static final String SAMPLING_METHOD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSamplingMethod() <em>Sampling Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingMethod()
	 * @generated
	 * @ordered
	 */
	protected String samplingMethod = SAMPLING_METHOD_EDEFAULT;

	/**
	 * The default value of the '{@link #getSamplingPlan() <em>Sampling Plan</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingPlan()
	 * @generated
	 * @ordered
	 */
	protected static final String SAMPLING_PLAN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSamplingPlan() <em>Sampling Plan</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingPlan()
	 * @generated
	 * @ordered
	 */
	protected String samplingPlan = SAMPLING_PLAN_EDEFAULT;

	/**
	 * The default value of the '{@link #getSamplingWeight() <em>Sampling Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingWeight()
	 * @generated
	 * @ordered
	 */
	protected static final String SAMPLING_WEIGHT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSamplingWeight() <em>Sampling Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingWeight()
	 * @generated
	 * @ordered
	 */
	protected String samplingWeight = SAMPLING_WEIGHT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSamplingSize() <em>Sampling Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingSize()
	 * @generated
	 * @ordered
	 */
	protected static final String SAMPLING_SIZE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSamplingSize() <em>Sampling Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingSize()
	 * @generated
	 * @ordered
	 */
	protected String samplingSize = SAMPLING_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLotSizeUnit() <em>Lot Size Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLotSizeUnit()
	 * @generated
	 * @ordered
	 */
	protected static final String LOT_SIZE_UNIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLotSizeUnit() <em>Lot Size Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLotSizeUnit()
	 * @generated
	 * @ordered
	 */
	protected String lotSizeUnit = LOT_SIZE_UNIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSamplingPoint() <em>Sampling Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingPoint()
	 * @generated
	 * @ordered
	 */
	protected static final String SAMPLING_POINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSamplingPoint() <em>Sampling Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSamplingPoint()
	 * @generated
	 * @ordered
	 */
	protected String samplingPoint = SAMPLING_POINT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StudySampleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.STUDY_SAMPLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSampleName() {
		return sampleName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSampleName(String newSampleName) {
		String oldSampleName = sampleName;
		sampleName = newSampleName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__SAMPLE_NAME, oldSampleName, sampleName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProtocolOfSampleCollection() {
		return protocolOfSampleCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProtocolOfSampleCollection(String newProtocolOfSampleCollection) {
		String oldProtocolOfSampleCollection = protocolOfSampleCollection;
		protocolOfSampleCollection = newProtocolOfSampleCollection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__PROTOCOL_OF_SAMPLE_COLLECTION, oldProtocolOfSampleCollection, protocolOfSampleCollection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSamplingStrategy() {
		return samplingStrategy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSamplingStrategy(String newSamplingStrategy) {
		String oldSamplingStrategy = samplingStrategy;
		samplingStrategy = newSamplingStrategy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__SAMPLING_STRATEGY, oldSamplingStrategy, samplingStrategy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTypeOfSamplingProgram() {
		return typeOfSamplingProgram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypeOfSamplingProgram(String newTypeOfSamplingProgram) {
		String oldTypeOfSamplingProgram = typeOfSamplingProgram;
		typeOfSamplingProgram = newTypeOfSamplingProgram;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__TYPE_OF_SAMPLING_PROGRAM, oldTypeOfSamplingProgram, typeOfSamplingProgram));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSamplingMethod() {
		return samplingMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSamplingMethod(String newSamplingMethod) {
		String oldSamplingMethod = samplingMethod;
		samplingMethod = newSamplingMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__SAMPLING_METHOD, oldSamplingMethod, samplingMethod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSamplingPlan() {
		return samplingPlan;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSamplingPlan(String newSamplingPlan) {
		String oldSamplingPlan = samplingPlan;
		samplingPlan = newSamplingPlan;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__SAMPLING_PLAN, oldSamplingPlan, samplingPlan));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSamplingWeight() {
		return samplingWeight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSamplingWeight(String newSamplingWeight) {
		String oldSamplingWeight = samplingWeight;
		samplingWeight = newSamplingWeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__SAMPLING_WEIGHT, oldSamplingWeight, samplingWeight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSamplingSize() {
		return samplingSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSamplingSize(String newSamplingSize) {
		String oldSamplingSize = samplingSize;
		samplingSize = newSamplingSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__SAMPLING_SIZE, oldSamplingSize, samplingSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLotSizeUnit() {
		return lotSizeUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLotSizeUnit(String newLotSizeUnit) {
		String oldLotSizeUnit = lotSizeUnit;
		lotSizeUnit = newLotSizeUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__LOT_SIZE_UNIT, oldLotSizeUnit, lotSizeUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSamplingPoint() {
		return samplingPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSamplingPoint(String newSamplingPoint) {
		String oldSamplingPoint = samplingPoint;
		samplingPoint = newSamplingPoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY_SAMPLE__SAMPLING_POINT, oldSamplingPoint, samplingPoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.STUDY_SAMPLE__SAMPLE_NAME:
				return getSampleName();
			case MetadataPackage.STUDY_SAMPLE__PROTOCOL_OF_SAMPLE_COLLECTION:
				return getProtocolOfSampleCollection();
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_STRATEGY:
				return getSamplingStrategy();
			case MetadataPackage.STUDY_SAMPLE__TYPE_OF_SAMPLING_PROGRAM:
				return getTypeOfSamplingProgram();
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_METHOD:
				return getSamplingMethod();
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_PLAN:
				return getSamplingPlan();
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_WEIGHT:
				return getSamplingWeight();
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_SIZE:
				return getSamplingSize();
			case MetadataPackage.STUDY_SAMPLE__LOT_SIZE_UNIT:
				return getLotSizeUnit();
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_POINT:
				return getSamplingPoint();
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
			case MetadataPackage.STUDY_SAMPLE__SAMPLE_NAME:
				setSampleName((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__PROTOCOL_OF_SAMPLE_COLLECTION:
				setProtocolOfSampleCollection((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_STRATEGY:
				setSamplingStrategy((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__TYPE_OF_SAMPLING_PROGRAM:
				setTypeOfSamplingProgram((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_METHOD:
				setSamplingMethod((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_PLAN:
				setSamplingPlan((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_WEIGHT:
				setSamplingWeight((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_SIZE:
				setSamplingSize((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__LOT_SIZE_UNIT:
				setLotSizeUnit((String)newValue);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_POINT:
				setSamplingPoint((String)newValue);
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
			case MetadataPackage.STUDY_SAMPLE__SAMPLE_NAME:
				setSampleName(SAMPLE_NAME_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__PROTOCOL_OF_SAMPLE_COLLECTION:
				setProtocolOfSampleCollection(PROTOCOL_OF_SAMPLE_COLLECTION_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_STRATEGY:
				setSamplingStrategy(SAMPLING_STRATEGY_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__TYPE_OF_SAMPLING_PROGRAM:
				setTypeOfSamplingProgram(TYPE_OF_SAMPLING_PROGRAM_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_METHOD:
				setSamplingMethod(SAMPLING_METHOD_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_PLAN:
				setSamplingPlan(SAMPLING_PLAN_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_WEIGHT:
				setSamplingWeight(SAMPLING_WEIGHT_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_SIZE:
				setSamplingSize(SAMPLING_SIZE_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__LOT_SIZE_UNIT:
				setLotSizeUnit(LOT_SIZE_UNIT_EDEFAULT);
				return;
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_POINT:
				setSamplingPoint(SAMPLING_POINT_EDEFAULT);
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
			case MetadataPackage.STUDY_SAMPLE__SAMPLE_NAME:
				return SAMPLE_NAME_EDEFAULT == null ? sampleName != null : !SAMPLE_NAME_EDEFAULT.equals(sampleName);
			case MetadataPackage.STUDY_SAMPLE__PROTOCOL_OF_SAMPLE_COLLECTION:
				return PROTOCOL_OF_SAMPLE_COLLECTION_EDEFAULT == null ? protocolOfSampleCollection != null : !PROTOCOL_OF_SAMPLE_COLLECTION_EDEFAULT.equals(protocolOfSampleCollection);
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_STRATEGY:
				return SAMPLING_STRATEGY_EDEFAULT == null ? samplingStrategy != null : !SAMPLING_STRATEGY_EDEFAULT.equals(samplingStrategy);
			case MetadataPackage.STUDY_SAMPLE__TYPE_OF_SAMPLING_PROGRAM:
				return TYPE_OF_SAMPLING_PROGRAM_EDEFAULT == null ? typeOfSamplingProgram != null : !TYPE_OF_SAMPLING_PROGRAM_EDEFAULT.equals(typeOfSamplingProgram);
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_METHOD:
				return SAMPLING_METHOD_EDEFAULT == null ? samplingMethod != null : !SAMPLING_METHOD_EDEFAULT.equals(samplingMethod);
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_PLAN:
				return SAMPLING_PLAN_EDEFAULT == null ? samplingPlan != null : !SAMPLING_PLAN_EDEFAULT.equals(samplingPlan);
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_WEIGHT:
				return SAMPLING_WEIGHT_EDEFAULT == null ? samplingWeight != null : !SAMPLING_WEIGHT_EDEFAULT.equals(samplingWeight);
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_SIZE:
				return SAMPLING_SIZE_EDEFAULT == null ? samplingSize != null : !SAMPLING_SIZE_EDEFAULT.equals(samplingSize);
			case MetadataPackage.STUDY_SAMPLE__LOT_SIZE_UNIT:
				return LOT_SIZE_UNIT_EDEFAULT == null ? lotSizeUnit != null : !LOT_SIZE_UNIT_EDEFAULT.equals(lotSizeUnit);
			case MetadataPackage.STUDY_SAMPLE__SAMPLING_POINT:
				return SAMPLING_POINT_EDEFAULT == null ? samplingPoint != null : !SAMPLING_POINT_EDEFAULT.equals(samplingPoint);
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
		result.append(" (sampleName: ");
		result.append(sampleName);
		result.append(", protocolOfSampleCollection: ");
		result.append(protocolOfSampleCollection);
		result.append(", samplingStrategy: ");
		result.append(samplingStrategy);
		result.append(", typeOfSamplingProgram: ");
		result.append(typeOfSamplingProgram);
		result.append(", samplingMethod: ");
		result.append(samplingMethod);
		result.append(", samplingPlan: ");
		result.append(samplingPlan);
		result.append(", samplingWeight: ");
		result.append(samplingWeight);
		result.append(", samplingSize: ");
		result.append(samplingSize);
		result.append(", lotSizeUnit: ");
		result.append(lotSizeUnit);
		result.append(", samplingPoint: ");
		result.append(samplingPoint);
		result.append(')');
		return result.toString();
	}

} //StudySampleImpl
