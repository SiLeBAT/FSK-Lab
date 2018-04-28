/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.MetadataPackage;
import metadata.SpatialInformation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

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
	 * The cached value of the '{@link #getRegion() <em>Region</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegion()
	 * @generated
	 * @ordered
	 */
	protected EList<String> region;

	/**
	 * The cached value of the '{@link #getCountry() <em>Country</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountry()
	 * @generated
	 * @ordered
	 */
	protected EList<String> country;

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
	public EList<String> getRegion() {
		if (region == null) {
			region = new EDataTypeUniqueEList<String>(String.class, this, MetadataPackage.SPATIAL_INFORMATION__REGION);
		}
		return region;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getCountry() {
		if (country == null) {
			country = new EDataTypeUniqueEList<String>(String.class, this, MetadataPackage.SPATIAL_INFORMATION__COUNTRY);
		}
		return country;
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
				getRegion().addAll((Collection<? extends String>)newValue);
				return;
			case MetadataPackage.SPATIAL_INFORMATION__COUNTRY:
				getCountry().clear();
				getCountry().addAll((Collection<? extends String>)newValue);
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

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (region: ");
		result.append(region);
		result.append(", country: ");
		result.append(country);
		result.append(')');
		return result.toString();
	}

} //SpatialInformationImpl
