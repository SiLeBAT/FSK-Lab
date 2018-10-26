/**
 */
package metadata.impl;

import metadata.Hazard;
import metadata.MetadataPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Hazard</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.HazardImpl#getHazardType <em>Hazard Type</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getHazardName <em>Hazard Name</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getHazardDescription <em>Hazard Description</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getHazardUnit <em>Hazard Unit</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getAdverseEffect <em>Adverse Effect</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getSourceOfContamination <em>Source Of Contamination</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getBenchmarkDose <em>Benchmark Dose</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getMaximumResidueLimit <em>Maximum Residue Limit</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getNoObservedAdverseAffectLevel <em>No Observed Adverse Affect Level</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getLowestObservedAdverseAffectLevel <em>Lowest Observed Adverse Affect Level</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getAcceptableOperatorExposureLevel <em>Acceptable Operator Exposure Level</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getAcuteReferenceDose <em>Acute Reference Dose</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getAcceptableDailyIntake <em>Acceptable Daily Intake</em>}</li>
 *   <li>{@link metadata.impl.HazardImpl#getHazardIndSum <em>Hazard Ind Sum</em>}</li>
 * </ul>
 *
 * @generated
 */
public class HazardImpl extends MinimalEObjectImpl.Container implements Hazard {
	/**
   * The default value of the '{@link #getHazardType() <em>Hazard Type</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardType()
   * @generated
   * @ordered
   */
	protected static final String HAZARD_TYPE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getHazardType() <em>Hazard Type</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardType()
   * @generated
   * @ordered
   */
	protected String hazardType = HAZARD_TYPE_EDEFAULT;

	/**
   * The default value of the '{@link #getHazardName() <em>Hazard Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardName()
   * @generated
   * @ordered
   */
	protected static final String HAZARD_NAME_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getHazardName() <em>Hazard Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardName()
   * @generated
   * @ordered
   */
	protected String hazardName = HAZARD_NAME_EDEFAULT;

	/**
   * The default value of the '{@link #getHazardDescription() <em>Hazard Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardDescription()
   * @generated
   * @ordered
   */
	protected static final String HAZARD_DESCRIPTION_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getHazardDescription() <em>Hazard Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardDescription()
   * @generated
   * @ordered
   */
	protected String hazardDescription = HAZARD_DESCRIPTION_EDEFAULT;

	/**
   * The default value of the '{@link #getHazardUnit() <em>Hazard Unit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardUnit()
   * @generated
   * @ordered
   */
	protected static final String HAZARD_UNIT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getHazardUnit() <em>Hazard Unit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardUnit()
   * @generated
   * @ordered
   */
	protected String hazardUnit = HAZARD_UNIT_EDEFAULT;

	/**
   * The default value of the '{@link #getAdverseEffect() <em>Adverse Effect</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAdverseEffect()
   * @generated
   * @ordered
   */
	protected static final String ADVERSE_EFFECT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getAdverseEffect() <em>Adverse Effect</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAdverseEffect()
   * @generated
   * @ordered
   */
	protected String adverseEffect = ADVERSE_EFFECT_EDEFAULT;

	/**
   * The default value of the '{@link #getSourceOfContamination() <em>Source Of Contamination</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getSourceOfContamination()
   * @generated
   * @ordered
   */
	protected static final String SOURCE_OF_CONTAMINATION_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getSourceOfContamination() <em>Source Of Contamination</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getSourceOfContamination()
   * @generated
   * @ordered
   */
	protected String sourceOfContamination = SOURCE_OF_CONTAMINATION_EDEFAULT;

	/**
   * The default value of the '{@link #getBenchmarkDose() <em>Benchmark Dose</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getBenchmarkDose()
   * @generated
   * @ordered
   */
	protected static final String BENCHMARK_DOSE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getBenchmarkDose() <em>Benchmark Dose</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getBenchmarkDose()
   * @generated
   * @ordered
   */
	protected String benchmarkDose = BENCHMARK_DOSE_EDEFAULT;

	/**
   * The default value of the '{@link #getMaximumResidueLimit() <em>Maximum Residue Limit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getMaximumResidueLimit()
   * @generated
   * @ordered
   */
	protected static final String MAXIMUM_RESIDUE_LIMIT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getMaximumResidueLimit() <em>Maximum Residue Limit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getMaximumResidueLimit()
   * @generated
   * @ordered
   */
	protected String maximumResidueLimit = MAXIMUM_RESIDUE_LIMIT_EDEFAULT;

	/**
   * The default value of the '{@link #getNoObservedAdverseAffectLevel() <em>No Observed Adverse Affect Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getNoObservedAdverseAffectLevel()
   * @generated
   * @ordered
   */
	protected static final String NO_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getNoObservedAdverseAffectLevel() <em>No Observed Adverse Affect Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getNoObservedAdverseAffectLevel()
   * @generated
   * @ordered
   */
	protected String noObservedAdverseAffectLevel = NO_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT;

	/**
   * The default value of the '{@link #getLowestObservedAdverseAffectLevel() <em>Lowest Observed Adverse Affect Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getLowestObservedAdverseAffectLevel()
   * @generated
   * @ordered
   */
	protected static final String LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getLowestObservedAdverseAffectLevel() <em>Lowest Observed Adverse Affect Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getLowestObservedAdverseAffectLevel()
   * @generated
   * @ordered
   */
	protected String lowestObservedAdverseAffectLevel = LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT;

	/**
   * The default value of the '{@link #getAcceptableOperatorExposureLevel() <em>Acceptable Operator Exposure Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAcceptableOperatorExposureLevel()
   * @generated
   * @ordered
   */
	protected static final String ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getAcceptableOperatorExposureLevel() <em>Acceptable Operator Exposure Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAcceptableOperatorExposureLevel()
   * @generated
   * @ordered
   */
	protected String acceptableOperatorExposureLevel = ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL_EDEFAULT;

	/**
   * The default value of the '{@link #getAcuteReferenceDose() <em>Acute Reference Dose</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAcuteReferenceDose()
   * @generated
   * @ordered
   */
	protected static final String ACUTE_REFERENCE_DOSE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getAcuteReferenceDose() <em>Acute Reference Dose</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAcuteReferenceDose()
   * @generated
   * @ordered
   */
	protected String acuteReferenceDose = ACUTE_REFERENCE_DOSE_EDEFAULT;

	/**
   * The default value of the '{@link #getAcceptableDailyIntake() <em>Acceptable Daily Intake</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAcceptableDailyIntake()
   * @generated
   * @ordered
   */
	protected static final String ACCEPTABLE_DAILY_INTAKE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getAcceptableDailyIntake() <em>Acceptable Daily Intake</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAcceptableDailyIntake()
   * @generated
   * @ordered
   */
	protected String acceptableDailyIntake = ACCEPTABLE_DAILY_INTAKE_EDEFAULT;

	/**
   * The default value of the '{@link #getHazardIndSum() <em>Hazard Ind Sum</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardIndSum()
   * @generated
   * @ordered
   */
	protected static final String HAZARD_IND_SUM_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getHazardIndSum() <em>Hazard Ind Sum</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getHazardIndSum()
   * @generated
   * @ordered
   */
	protected String hazardIndSum = HAZARD_IND_SUM_EDEFAULT;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected HazardImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return MetadataPackage.Literals.HAZARD;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getHazardType() {
    return hazardType;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setHazardType(String newHazardType) {
    String oldHazardType = hazardType;
    hazardType = newHazardType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__HAZARD_TYPE, oldHazardType, hazardType));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getHazardName() {
    return hazardName;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setHazardName(String newHazardName) {
    String oldHazardName = hazardName;
    hazardName = newHazardName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__HAZARD_NAME, oldHazardName, hazardName));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getHazardDescription() {
    return hazardDescription;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setHazardDescription(String newHazardDescription) {
    String oldHazardDescription = hazardDescription;
    hazardDescription = newHazardDescription;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__HAZARD_DESCRIPTION, oldHazardDescription, hazardDescription));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getHazardUnit() {
    return hazardUnit;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setHazardUnit(String newHazardUnit) {
    String oldHazardUnit = hazardUnit;
    hazardUnit = newHazardUnit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__HAZARD_UNIT, oldHazardUnit, hazardUnit));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getAdverseEffect() {
    return adverseEffect;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setAdverseEffect(String newAdverseEffect) {
    String oldAdverseEffect = adverseEffect;
    adverseEffect = newAdverseEffect;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__ADVERSE_EFFECT, oldAdverseEffect, adverseEffect));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getSourceOfContamination() {
    return sourceOfContamination;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setSourceOfContamination(String newSourceOfContamination) {
    String oldSourceOfContamination = sourceOfContamination;
    sourceOfContamination = newSourceOfContamination;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__SOURCE_OF_CONTAMINATION, oldSourceOfContamination, sourceOfContamination));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getBenchmarkDose() {
    return benchmarkDose;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setBenchmarkDose(String newBenchmarkDose) {
    String oldBenchmarkDose = benchmarkDose;
    benchmarkDose = newBenchmarkDose;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__BENCHMARK_DOSE, oldBenchmarkDose, benchmarkDose));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getMaximumResidueLimit() {
    return maximumResidueLimit;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setMaximumResidueLimit(String newMaximumResidueLimit) {
    String oldMaximumResidueLimit = maximumResidueLimit;
    maximumResidueLimit = newMaximumResidueLimit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__MAXIMUM_RESIDUE_LIMIT, oldMaximumResidueLimit, maximumResidueLimit));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getNoObservedAdverseAffectLevel() {
    return noObservedAdverseAffectLevel;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setNoObservedAdverseAffectLevel(String newNoObservedAdverseAffectLevel) {
    String oldNoObservedAdverseAffectLevel = noObservedAdverseAffectLevel;
    noObservedAdverseAffectLevel = newNoObservedAdverseAffectLevel;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__NO_OBSERVED_ADVERSE_AFFECT_LEVEL, oldNoObservedAdverseAffectLevel, noObservedAdverseAffectLevel));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getLowestObservedAdverseAffectLevel() {
    return lowestObservedAdverseAffectLevel;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setLowestObservedAdverseAffectLevel(String newLowestObservedAdverseAffectLevel) {
    String oldLowestObservedAdverseAffectLevel = lowestObservedAdverseAffectLevel;
    lowestObservedAdverseAffectLevel = newLowestObservedAdverseAffectLevel;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL, oldLowestObservedAdverseAffectLevel, lowestObservedAdverseAffectLevel));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getAcceptableOperatorExposureLevel() {
    return acceptableOperatorExposureLevel;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setAcceptableOperatorExposureLevel(String newAcceptableOperatorExposureLevel) {
    String oldAcceptableOperatorExposureLevel = acceptableOperatorExposureLevel;
    acceptableOperatorExposureLevel = newAcceptableOperatorExposureLevel;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL, oldAcceptableOperatorExposureLevel, acceptableOperatorExposureLevel));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getAcuteReferenceDose() {
    return acuteReferenceDose;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setAcuteReferenceDose(String newAcuteReferenceDose) {
    String oldAcuteReferenceDose = acuteReferenceDose;
    acuteReferenceDose = newAcuteReferenceDose;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__ACUTE_REFERENCE_DOSE, oldAcuteReferenceDose, acuteReferenceDose));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getAcceptableDailyIntake() {
    return acceptableDailyIntake;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setAcceptableDailyIntake(String newAcceptableDailyIntake) {
    String oldAcceptableDailyIntake = acceptableDailyIntake;
    acceptableDailyIntake = newAcceptableDailyIntake;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__ACCEPTABLE_DAILY_INTAKE, oldAcceptableDailyIntake, acceptableDailyIntake));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getHazardIndSum() {
    return hazardIndSum;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setHazardIndSum(String newHazardIndSum) {
    String oldHazardIndSum = hazardIndSum;
    hazardIndSum = newHazardIndSum;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.HAZARD__HAZARD_IND_SUM, oldHazardIndSum, hazardIndSum));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case MetadataPackage.HAZARD__HAZARD_TYPE:
        return getHazardType();
      case MetadataPackage.HAZARD__HAZARD_NAME:
        return getHazardName();
      case MetadataPackage.HAZARD__HAZARD_DESCRIPTION:
        return getHazardDescription();
      case MetadataPackage.HAZARD__HAZARD_UNIT:
        return getHazardUnit();
      case MetadataPackage.HAZARD__ADVERSE_EFFECT:
        return getAdverseEffect();
      case MetadataPackage.HAZARD__SOURCE_OF_CONTAMINATION:
        return getSourceOfContamination();
      case MetadataPackage.HAZARD__BENCHMARK_DOSE:
        return getBenchmarkDose();
      case MetadataPackage.HAZARD__MAXIMUM_RESIDUE_LIMIT:
        return getMaximumResidueLimit();
      case MetadataPackage.HAZARD__NO_OBSERVED_ADVERSE_AFFECT_LEVEL:
        return getNoObservedAdverseAffectLevel();
      case MetadataPackage.HAZARD__LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL:
        return getLowestObservedAdverseAffectLevel();
      case MetadataPackage.HAZARD__ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL:
        return getAcceptableOperatorExposureLevel();
      case MetadataPackage.HAZARD__ACUTE_REFERENCE_DOSE:
        return getAcuteReferenceDose();
      case MetadataPackage.HAZARD__ACCEPTABLE_DAILY_INTAKE:
        return getAcceptableDailyIntake();
      case MetadataPackage.HAZARD__HAZARD_IND_SUM:
        return getHazardIndSum();
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
      case MetadataPackage.HAZARD__HAZARD_TYPE:
        setHazardType((String)newValue);
        return;
      case MetadataPackage.HAZARD__HAZARD_NAME:
        setHazardName((String)newValue);
        return;
      case MetadataPackage.HAZARD__HAZARD_DESCRIPTION:
        setHazardDescription((String)newValue);
        return;
      case MetadataPackage.HAZARD__HAZARD_UNIT:
        setHazardUnit((String)newValue);
        return;
      case MetadataPackage.HAZARD__ADVERSE_EFFECT:
        setAdverseEffect((String)newValue);
        return;
      case MetadataPackage.HAZARD__SOURCE_OF_CONTAMINATION:
        setSourceOfContamination((String)newValue);
        return;
      case MetadataPackage.HAZARD__BENCHMARK_DOSE:
        setBenchmarkDose((String)newValue);
        return;
      case MetadataPackage.HAZARD__MAXIMUM_RESIDUE_LIMIT:
        setMaximumResidueLimit((String)newValue);
        return;
      case MetadataPackage.HAZARD__NO_OBSERVED_ADVERSE_AFFECT_LEVEL:
        setNoObservedAdverseAffectLevel((String)newValue);
        return;
      case MetadataPackage.HAZARD__LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL:
        setLowestObservedAdverseAffectLevel((String)newValue);
        return;
      case MetadataPackage.HAZARD__ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL:
        setAcceptableOperatorExposureLevel((String)newValue);
        return;
      case MetadataPackage.HAZARD__ACUTE_REFERENCE_DOSE:
        setAcuteReferenceDose((String)newValue);
        return;
      case MetadataPackage.HAZARD__ACCEPTABLE_DAILY_INTAKE:
        setAcceptableDailyIntake((String)newValue);
        return;
      case MetadataPackage.HAZARD__HAZARD_IND_SUM:
        setHazardIndSum((String)newValue);
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
      case MetadataPackage.HAZARD__HAZARD_TYPE:
        setHazardType(HAZARD_TYPE_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__HAZARD_NAME:
        setHazardName(HAZARD_NAME_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__HAZARD_DESCRIPTION:
        setHazardDescription(HAZARD_DESCRIPTION_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__HAZARD_UNIT:
        setHazardUnit(HAZARD_UNIT_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__ADVERSE_EFFECT:
        setAdverseEffect(ADVERSE_EFFECT_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__SOURCE_OF_CONTAMINATION:
        setSourceOfContamination(SOURCE_OF_CONTAMINATION_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__BENCHMARK_DOSE:
        setBenchmarkDose(BENCHMARK_DOSE_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__MAXIMUM_RESIDUE_LIMIT:
        setMaximumResidueLimit(MAXIMUM_RESIDUE_LIMIT_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__NO_OBSERVED_ADVERSE_AFFECT_LEVEL:
        setNoObservedAdverseAffectLevel(NO_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL:
        setLowestObservedAdverseAffectLevel(LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL:
        setAcceptableOperatorExposureLevel(ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__ACUTE_REFERENCE_DOSE:
        setAcuteReferenceDose(ACUTE_REFERENCE_DOSE_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__ACCEPTABLE_DAILY_INTAKE:
        setAcceptableDailyIntake(ACCEPTABLE_DAILY_INTAKE_EDEFAULT);
        return;
      case MetadataPackage.HAZARD__HAZARD_IND_SUM:
        setHazardIndSum(HAZARD_IND_SUM_EDEFAULT);
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
      case MetadataPackage.HAZARD__HAZARD_TYPE:
        return HAZARD_TYPE_EDEFAULT == null ? hazardType != null : !HAZARD_TYPE_EDEFAULT.equals(hazardType);
      case MetadataPackage.HAZARD__HAZARD_NAME:
        return HAZARD_NAME_EDEFAULT == null ? hazardName != null : !HAZARD_NAME_EDEFAULT.equals(hazardName);
      case MetadataPackage.HAZARD__HAZARD_DESCRIPTION:
        return HAZARD_DESCRIPTION_EDEFAULT == null ? hazardDescription != null : !HAZARD_DESCRIPTION_EDEFAULT.equals(hazardDescription);
      case MetadataPackage.HAZARD__HAZARD_UNIT:
        return HAZARD_UNIT_EDEFAULT == null ? hazardUnit != null : !HAZARD_UNIT_EDEFAULT.equals(hazardUnit);
      case MetadataPackage.HAZARD__ADVERSE_EFFECT:
        return ADVERSE_EFFECT_EDEFAULT == null ? adverseEffect != null : !ADVERSE_EFFECT_EDEFAULT.equals(adverseEffect);
      case MetadataPackage.HAZARD__SOURCE_OF_CONTAMINATION:
        return SOURCE_OF_CONTAMINATION_EDEFAULT == null ? sourceOfContamination != null : !SOURCE_OF_CONTAMINATION_EDEFAULT.equals(sourceOfContamination);
      case MetadataPackage.HAZARD__BENCHMARK_DOSE:
        return BENCHMARK_DOSE_EDEFAULT == null ? benchmarkDose != null : !BENCHMARK_DOSE_EDEFAULT.equals(benchmarkDose);
      case MetadataPackage.HAZARD__MAXIMUM_RESIDUE_LIMIT:
        return MAXIMUM_RESIDUE_LIMIT_EDEFAULT == null ? maximumResidueLimit != null : !MAXIMUM_RESIDUE_LIMIT_EDEFAULT.equals(maximumResidueLimit);
      case MetadataPackage.HAZARD__NO_OBSERVED_ADVERSE_AFFECT_LEVEL:
        return NO_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT == null ? noObservedAdverseAffectLevel != null : !NO_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT.equals(noObservedAdverseAffectLevel);
      case MetadataPackage.HAZARD__LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL:
        return LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT == null ? lowestObservedAdverseAffectLevel != null : !LOWEST_OBSERVED_ADVERSE_AFFECT_LEVEL_EDEFAULT.equals(lowestObservedAdverseAffectLevel);
      case MetadataPackage.HAZARD__ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL:
        return ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL_EDEFAULT == null ? acceptableOperatorExposureLevel != null : !ACCEPTABLE_OPERATOR_EXPOSURE_LEVEL_EDEFAULT.equals(acceptableOperatorExposureLevel);
      case MetadataPackage.HAZARD__ACUTE_REFERENCE_DOSE:
        return ACUTE_REFERENCE_DOSE_EDEFAULT == null ? acuteReferenceDose != null : !ACUTE_REFERENCE_DOSE_EDEFAULT.equals(acuteReferenceDose);
      case MetadataPackage.HAZARD__ACCEPTABLE_DAILY_INTAKE:
        return ACCEPTABLE_DAILY_INTAKE_EDEFAULT == null ? acceptableDailyIntake != null : !ACCEPTABLE_DAILY_INTAKE_EDEFAULT.equals(acceptableDailyIntake);
      case MetadataPackage.HAZARD__HAZARD_IND_SUM:
        return HAZARD_IND_SUM_EDEFAULT == null ? hazardIndSum != null : !HAZARD_IND_SUM_EDEFAULT.equals(hazardIndSum);
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
    result.append(" (hazardType: ");
    result.append(hazardType);
    result.append(", hazardName: ");
    result.append(hazardName);
    result.append(", hazardDescription: ");
    result.append(hazardDescription);
    result.append(", hazardUnit: ");
    result.append(hazardUnit);
    result.append(", adverseEffect: ");
    result.append(adverseEffect);
    result.append(", sourceOfContamination: ");
    result.append(sourceOfContamination);
    result.append(", benchmarkDose: ");
    result.append(benchmarkDose);
    result.append(", maximumResidueLimit: ");
    result.append(maximumResidueLimit);
    result.append(", noObservedAdverseAffectLevel: ");
    result.append(noObservedAdverseAffectLevel);
    result.append(", lowestObservedAdverseAffectLevel: ");
    result.append(lowestObservedAdverseAffectLevel);
    result.append(", acceptableOperatorExposureLevel: ");
    result.append(acceptableOperatorExposureLevel);
    result.append(", acuteReferenceDose: ");
    result.append(acuteReferenceDose);
    result.append(", acceptableDailyIntake: ");
    result.append(acceptableDailyIntake);
    result.append(", hazardIndSum: ");
    result.append(hazardIndSum);
    result.append(')');
    return result.toString();
  }

} //HazardImpl
