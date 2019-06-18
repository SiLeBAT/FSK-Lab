/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.MetadataPackage;
import metadata.PopulationGroup;
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
 * An implementation of the model object '<em><b>Population Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getPopulationName <em>Population Name</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getTargetPopulation <em>Target Population</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getPopulationSpan <em>Population Span</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getPopulationDescription <em>Population Description</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getBmi <em>Bmi</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getSpecialDietGroups <em>Special Diet Groups</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getRegion <em>Region</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getCountry <em>Country</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getPopulationRiskFactor <em>Population Risk Factor</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getSeason <em>Season</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getPopulationGender <em>Population Gender</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getPatternConsumption <em>Pattern Consumption</em>}</li>
 *   <li>{@link metadata.impl.PopulationGroupImpl#getPopulationAge <em>Population Age</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PopulationGroupImpl extends MinimalEObjectImpl.Container implements PopulationGroup {
	/**
	 * The default value of the '{@link #getPopulationName() <em>Population Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationName()
	 * @generated
	 * @ordered
	 */
	protected static final String POPULATION_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPopulationName() <em>Population Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationName()
	 * @generated
	 * @ordered
	 */
	protected String populationName = POPULATION_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTargetPopulation() <em>Target Population</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetPopulation()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_POPULATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTargetPopulation() <em>Target Population</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetPopulation()
	 * @generated
	 * @ordered
	 */
	protected String targetPopulation = TARGET_POPULATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPopulationSpan() <em>Population Span</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationSpan()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> populationSpan;

	/**
	 * The cached value of the '{@link #getPopulationDescription() <em>Population Description</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationDescription()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> populationDescription;

	/**
	 * The cached value of the '{@link #getBmi() <em>Bmi</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBmi()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> bmi;

	/**
	 * The cached value of the '{@link #getSpecialDietGroups() <em>Special Diet Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpecialDietGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> specialDietGroups;

	/**
	 * The cached value of the '{@link #getRegion() <em>Region</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegion()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> region;

	/**
	 * The cached value of the '{@link #getCountry() <em>Country</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountry()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> country;

	/**
	 * The cached value of the '{@link #getPopulationRiskFactor() <em>Population Risk Factor</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationRiskFactor()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> populationRiskFactor;

	/**
	 * The cached value of the '{@link #getSeason() <em>Season</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeason()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> season;

	/**
	 * The default value of the '{@link #getPopulationGender() <em>Population Gender</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationGender()
	 * @generated
	 * @ordered
	 */
	protected static final String POPULATION_GENDER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPopulationGender() <em>Population Gender</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationGender()
	 * @generated
	 * @ordered
	 */
	protected String populationGender = POPULATION_GENDER_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPatternConsumption() <em>Pattern Consumption</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPatternConsumption()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> patternConsumption;

	/**
	 * The cached value of the '{@link #getPopulationAge() <em>Population Age</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationAge()
	 * @generated
	 * @ordered
	 */
	protected EList<StringObject> populationAge;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PopulationGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.POPULATION_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPopulationName() {
		return populationName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPopulationName(String newPopulationName) {
		String oldPopulationName = populationName;
		populationName = newPopulationName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.POPULATION_GROUP__POPULATION_NAME, oldPopulationName, populationName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTargetPopulation() {
		return targetPopulation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetPopulation(String newTargetPopulation) {
		String oldTargetPopulation = targetPopulation;
		targetPopulation = newTargetPopulation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.POPULATION_GROUP__TARGET_POPULATION, oldTargetPopulation, targetPopulation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getPopulationSpan() {
		if (populationSpan == null) {
			populationSpan = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__POPULATION_SPAN);
		}
		return populationSpan;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getPopulationDescription() {
		if (populationDescription == null) {
			populationDescription = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__POPULATION_DESCRIPTION);
		}
		return populationDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getBmi() {
		if (bmi == null) {
			bmi = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__BMI);
		}
		return bmi;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getSpecialDietGroups() {
		if (specialDietGroups == null) {
			specialDietGroups = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__SPECIAL_DIET_GROUPS);
		}
		return specialDietGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getRegion() {
		if (region == null) {
			region = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__REGION);
		}
		return region;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getCountry() {
		if (country == null) {
			country = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__COUNTRY);
		}
		return country;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getPopulationRiskFactor() {
		if (populationRiskFactor == null) {
			populationRiskFactor = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__POPULATION_RISK_FACTOR);
		}
		return populationRiskFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getSeason() {
		if (season == null) {
			season = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__SEASON);
		}
		return season;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPopulationGender() {
		return populationGender;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPopulationGender(String newPopulationGender) {
		String oldPopulationGender = populationGender;
		populationGender = newPopulationGender;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.POPULATION_GROUP__POPULATION_GENDER, oldPopulationGender, populationGender));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getPatternConsumption() {
		if (patternConsumption == null) {
			patternConsumption = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__PATTERN_CONSUMPTION);
		}
		return patternConsumption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getPopulationAge() {
		if (populationAge == null) {
			populationAge = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.POPULATION_GROUP__POPULATION_AGE);
		}
		return populationAge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MetadataPackage.POPULATION_GROUP__POPULATION_SPAN:
				return ((InternalEList<?>)getPopulationSpan()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__POPULATION_DESCRIPTION:
				return ((InternalEList<?>)getPopulationDescription()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__BMI:
				return ((InternalEList<?>)getBmi()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__SPECIAL_DIET_GROUPS:
				return ((InternalEList<?>)getSpecialDietGroups()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__REGION:
				return ((InternalEList<?>)getRegion()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__COUNTRY:
				return ((InternalEList<?>)getCountry()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__POPULATION_RISK_FACTOR:
				return ((InternalEList<?>)getPopulationRiskFactor()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__SEASON:
				return ((InternalEList<?>)getSeason()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__PATTERN_CONSUMPTION:
				return ((InternalEList<?>)getPatternConsumption()).basicRemove(otherEnd, msgs);
			case MetadataPackage.POPULATION_GROUP__POPULATION_AGE:
				return ((InternalEList<?>)getPopulationAge()).basicRemove(otherEnd, msgs);
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
			case MetadataPackage.POPULATION_GROUP__POPULATION_NAME:
				return getPopulationName();
			case MetadataPackage.POPULATION_GROUP__TARGET_POPULATION:
				return getTargetPopulation();
			case MetadataPackage.POPULATION_GROUP__POPULATION_SPAN:
				return getPopulationSpan();
			case MetadataPackage.POPULATION_GROUP__POPULATION_DESCRIPTION:
				return getPopulationDescription();
			case MetadataPackage.POPULATION_GROUP__BMI:
				return getBmi();
			case MetadataPackage.POPULATION_GROUP__SPECIAL_DIET_GROUPS:
				return getSpecialDietGroups();
			case MetadataPackage.POPULATION_GROUP__REGION:
				return getRegion();
			case MetadataPackage.POPULATION_GROUP__COUNTRY:
				return getCountry();
			case MetadataPackage.POPULATION_GROUP__POPULATION_RISK_FACTOR:
				return getPopulationRiskFactor();
			case MetadataPackage.POPULATION_GROUP__SEASON:
				return getSeason();
			case MetadataPackage.POPULATION_GROUP__POPULATION_GENDER:
				return getPopulationGender();
			case MetadataPackage.POPULATION_GROUP__PATTERN_CONSUMPTION:
				return getPatternConsumption();
			case MetadataPackage.POPULATION_GROUP__POPULATION_AGE:
				return getPopulationAge();
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
			case MetadataPackage.POPULATION_GROUP__POPULATION_NAME:
				setPopulationName((String)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__TARGET_POPULATION:
				setTargetPopulation((String)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_SPAN:
				getPopulationSpan().clear();
				getPopulationSpan().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_DESCRIPTION:
				getPopulationDescription().clear();
				getPopulationDescription().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__BMI:
				getBmi().clear();
				getBmi().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__SPECIAL_DIET_GROUPS:
				getSpecialDietGroups().clear();
				getSpecialDietGroups().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__REGION:
				getRegion().clear();
				getRegion().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__COUNTRY:
				getCountry().clear();
				getCountry().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_RISK_FACTOR:
				getPopulationRiskFactor().clear();
				getPopulationRiskFactor().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__SEASON:
				getSeason().clear();
				getSeason().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_GENDER:
				setPopulationGender((String)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__PATTERN_CONSUMPTION:
				getPatternConsumption().clear();
				getPatternConsumption().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_AGE:
				getPopulationAge().clear();
				getPopulationAge().addAll((Collection<? extends StringObject>)newValue);
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
			case MetadataPackage.POPULATION_GROUP__POPULATION_NAME:
				setPopulationName(POPULATION_NAME_EDEFAULT);
				return;
			case MetadataPackage.POPULATION_GROUP__TARGET_POPULATION:
				setTargetPopulation(TARGET_POPULATION_EDEFAULT);
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_SPAN:
				getPopulationSpan().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_DESCRIPTION:
				getPopulationDescription().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__BMI:
				getBmi().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__SPECIAL_DIET_GROUPS:
				getSpecialDietGroups().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__REGION:
				getRegion().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__COUNTRY:
				getCountry().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_RISK_FACTOR:
				getPopulationRiskFactor().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__SEASON:
				getSeason().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_GENDER:
				setPopulationGender(POPULATION_GENDER_EDEFAULT);
				return;
			case MetadataPackage.POPULATION_GROUP__PATTERN_CONSUMPTION:
				getPatternConsumption().clear();
				return;
			case MetadataPackage.POPULATION_GROUP__POPULATION_AGE:
				getPopulationAge().clear();
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
			case MetadataPackage.POPULATION_GROUP__POPULATION_NAME:
				return POPULATION_NAME_EDEFAULT == null ? populationName != null : !POPULATION_NAME_EDEFAULT.equals(populationName);
			case MetadataPackage.POPULATION_GROUP__TARGET_POPULATION:
				return TARGET_POPULATION_EDEFAULT == null ? targetPopulation != null : !TARGET_POPULATION_EDEFAULT.equals(targetPopulation);
			case MetadataPackage.POPULATION_GROUP__POPULATION_SPAN:
				return populationSpan != null && !populationSpan.isEmpty();
			case MetadataPackage.POPULATION_GROUP__POPULATION_DESCRIPTION:
				return populationDescription != null && !populationDescription.isEmpty();
			case MetadataPackage.POPULATION_GROUP__BMI:
				return bmi != null && !bmi.isEmpty();
			case MetadataPackage.POPULATION_GROUP__SPECIAL_DIET_GROUPS:
				return specialDietGroups != null && !specialDietGroups.isEmpty();
			case MetadataPackage.POPULATION_GROUP__REGION:
				return region != null && !region.isEmpty();
			case MetadataPackage.POPULATION_GROUP__COUNTRY:
				return country != null && !country.isEmpty();
			case MetadataPackage.POPULATION_GROUP__POPULATION_RISK_FACTOR:
				return populationRiskFactor != null && !populationRiskFactor.isEmpty();
			case MetadataPackage.POPULATION_GROUP__SEASON:
				return season != null && !season.isEmpty();
			case MetadataPackage.POPULATION_GROUP__POPULATION_GENDER:
				return POPULATION_GENDER_EDEFAULT == null ? populationGender != null : !POPULATION_GENDER_EDEFAULT.equals(populationGender);
			case MetadataPackage.POPULATION_GROUP__PATTERN_CONSUMPTION:
				return patternConsumption != null && !patternConsumption.isEmpty();
			case MetadataPackage.POPULATION_GROUP__POPULATION_AGE:
				return populationAge != null && !populationAge.isEmpty();
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
		result.append(" (populationName: ");
		result.append(populationName);
		result.append(", targetPopulation: ");
		result.append(targetPopulation);
		result.append(", populationGender: ");
		result.append(populationGender);
		result.append(')');
		return result.toString();
	}

} //PopulationGroupImpl
