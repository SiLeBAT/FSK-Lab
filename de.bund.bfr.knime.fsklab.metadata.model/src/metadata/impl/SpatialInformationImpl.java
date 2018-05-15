/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.MetadataPackage;
import metadata.SpatialInformation;
import metadata.StringObject;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spatial Information</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.SpatialInformationImpl#getRegion <em>Region</em>}</li>
 *   <li>{@link metadata.impl.SpatialInformationImpl#getCountry <em>Country</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpatialInformationImpl extends MinimalEObjectImpl.Container implements SpatialInformation {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpatialInformationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.SPATIAL_INFORMATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StringObject> getRegion() {
		if (region == null) {
			region = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.SPATIAL_INFORMATION__REGION);
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
			country = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.SPATIAL_INFORMATION__COUNTRY);
		}
		return country;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MetadataPackage.SPATIAL_INFORMATION__REGION:
				return ((InternalEList<?>)getRegion()).basicRemove(otherEnd, msgs);
			case MetadataPackage.SPATIAL_INFORMATION__COUNTRY:
				return ((InternalEList<?>)getCountry()).basicRemove(otherEnd, msgs);
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
			case MetadataPackage.SPATIAL_INFORMATION__REGION:
				return getRegion();
			case MetadataPackage.SPATIAL_INFORMATION__COUNTRY:
				return getCountry();
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
			case MetadataPackage.SPATIAL_INFORMATION__REGION:
				getRegion().clear();
				getRegion().addAll((Collection<? extends StringObject>)newValue);
				return;
			case MetadataPackage.SPATIAL_INFORMATION__COUNTRY:
				getCountry().clear();
				getCountry().addAll((Collection<? extends StringObject>)newValue);
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
			case MetadataPackage.SPATIAL_INFORMATION__REGION:
				getRegion().clear();
				return;
			case MetadataPackage.SPATIAL_INFORMATION__COUNTRY:
				getCountry().clear();
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
			case MetadataPackage.SPATIAL_INFORMATION__REGION:
				return region != null && !region.isEmpty();
			case MetadataPackage.SPATIAL_INFORMATION__COUNTRY:
				return country != null && !country.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SpatialInformationImpl
