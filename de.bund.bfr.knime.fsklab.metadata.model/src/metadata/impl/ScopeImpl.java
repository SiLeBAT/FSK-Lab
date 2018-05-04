/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.Hazard;
import metadata.MetadataPackage;
import metadata.PopulationGroup;
import metadata.Product;
import metadata.Scope;
import metadata.SpatialInformation;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scope</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.ScopeImpl#getGeneralComment <em>General Comment</em>}</li>
 *   <li>{@link metadata.impl.ScopeImpl#getTemporalInformation <em>Temporal Information</em>}</li>
 *   <li>{@link metadata.impl.ScopeImpl#getProduct <em>Product</em>}</li>
 *   <li>{@link metadata.impl.ScopeImpl#getHazard <em>Hazard</em>}</li>
 *   <li>{@link metadata.impl.ScopeImpl#getPopulationGroup <em>Population Group</em>}</li>
 *   <li>{@link metadata.impl.ScopeImpl#getSpatialInformation <em>Spatial Information</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScopeImpl extends MinimalEObjectImpl.Container implements Scope {
	/**
	 * The default value of the '{@link #getGeneralComment() <em>General Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneralComment()
	 * @generated
	 * @ordered
	 */
	protected static final String GENERAL_COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGeneralComment() <em>General Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneralComment()
	 * @generated
	 * @ordered
	 */
	protected String generalComment = GENERAL_COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getTemporalInformation() <em>Temporal Information</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemporalInformation()
	 * @generated
	 * @ordered
	 */
	protected static final String TEMPORAL_INFORMATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTemporalInformation() <em>Temporal Information</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemporalInformation()
	 * @generated
	 * @ordered
	 */
	protected String temporalInformation = TEMPORAL_INFORMATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getProduct() <em>Product</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProduct()
	 * @generated
	 * @ordered
	 */
	protected EList<Product> product;

	/**
	 * The cached value of the '{@link #getHazard() <em>Hazard</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHazard()
	 * @generated
	 * @ordered
	 */
	protected EList<Hazard> hazard;

	/**
	 * The cached value of the '{@link #getPopulationGroup() <em>Population Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPopulationGroup()
	 * @generated
	 * @ordered
	 */
	protected PopulationGroup populationGroup;

	/**
	 * The cached value of the '{@link #getSpatialInformation() <em>Spatial Information</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpatialInformation()
	 * @generated
	 * @ordered
	 */
	protected SpatialInformation spatialInformation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScopeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.SCOPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getGeneralComment() {
		return generalComment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGeneralComment(String newGeneralComment) {
		String oldGeneralComment = generalComment;
		generalComment = newGeneralComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.SCOPE__GENERAL_COMMENT, oldGeneralComment, generalComment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTemporalInformation() {
		return temporalInformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTemporalInformation(String newTemporalInformation) {
		String oldTemporalInformation = temporalInformation;
		temporalInformation = newTemporalInformation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.SCOPE__TEMPORAL_INFORMATION, oldTemporalInformation, temporalInformation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Product> getProduct() {
		if (product == null) {
			product = new EObjectContainmentEList<Product>(Product.class, this, MetadataPackage.SCOPE__PRODUCT);
		}
		return product;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Hazard> getHazard() {
		if (hazard == null) {
			hazard = new EObjectResolvingEList<Hazard>(Hazard.class, this, MetadataPackage.SCOPE__HAZARD);
		}
		return hazard;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PopulationGroup getPopulationGroup() {
		if (populationGroup != null && populationGroup.eIsProxy()) {
			InternalEObject oldPopulationGroup = (InternalEObject)populationGroup;
			populationGroup = (PopulationGroup)eResolveProxy(oldPopulationGroup);
			if (populationGroup != oldPopulationGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetadataPackage.SCOPE__POPULATION_GROUP, oldPopulationGroup, populationGroup));
			}
		}
		return populationGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PopulationGroup basicGetPopulationGroup() {
		return populationGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPopulationGroup(PopulationGroup newPopulationGroup) {
		PopulationGroup oldPopulationGroup = populationGroup;
		populationGroup = newPopulationGroup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.SCOPE__POPULATION_GROUP, oldPopulationGroup, populationGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpatialInformation getSpatialInformation() {
		return spatialInformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSpatialInformation(SpatialInformation newSpatialInformation, NotificationChain msgs) {
		SpatialInformation oldSpatialInformation = spatialInformation;
		spatialInformation = newSpatialInformation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MetadataPackage.SCOPE__SPATIAL_INFORMATION, oldSpatialInformation, newSpatialInformation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpatialInformation(SpatialInformation newSpatialInformation) {
		if (newSpatialInformation != spatialInformation) {
			NotificationChain msgs = null;
			if (spatialInformation != null)
				msgs = ((InternalEObject)spatialInformation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.SCOPE__SPATIAL_INFORMATION, null, msgs);
			if (newSpatialInformation != null)
				msgs = ((InternalEObject)newSpatialInformation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.SCOPE__SPATIAL_INFORMATION, null, msgs);
			msgs = basicSetSpatialInformation(newSpatialInformation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.SCOPE__SPATIAL_INFORMATION, newSpatialInformation, newSpatialInformation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MetadataPackage.SCOPE__PRODUCT:
				return ((InternalEList<?>)getProduct()).basicRemove(otherEnd, msgs);
			case MetadataPackage.SCOPE__SPATIAL_INFORMATION:
				return basicSetSpatialInformation(null, msgs);
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
			case MetadataPackage.SCOPE__GENERAL_COMMENT:
				return getGeneralComment();
			case MetadataPackage.SCOPE__TEMPORAL_INFORMATION:
				return getTemporalInformation();
			case MetadataPackage.SCOPE__PRODUCT:
				return getProduct();
			case MetadataPackage.SCOPE__HAZARD:
				return getHazard();
			case MetadataPackage.SCOPE__POPULATION_GROUP:
				if (resolve) return getPopulationGroup();
				return basicGetPopulationGroup();
			case MetadataPackage.SCOPE__SPATIAL_INFORMATION:
				return getSpatialInformation();
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
			case MetadataPackage.SCOPE__GENERAL_COMMENT:
				setGeneralComment((String)newValue);
				return;
			case MetadataPackage.SCOPE__TEMPORAL_INFORMATION:
				setTemporalInformation((String)newValue);
				return;
			case MetadataPackage.SCOPE__PRODUCT:
				getProduct().clear();
				getProduct().addAll((Collection<? extends Product>)newValue);
				return;
			case MetadataPackage.SCOPE__HAZARD:
				getHazard().clear();
				getHazard().addAll((Collection<? extends Hazard>)newValue);
				return;
			case MetadataPackage.SCOPE__POPULATION_GROUP:
				setPopulationGroup((PopulationGroup)newValue);
				return;
			case MetadataPackage.SCOPE__SPATIAL_INFORMATION:
				setSpatialInformation((SpatialInformation)newValue);
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
			case MetadataPackage.SCOPE__GENERAL_COMMENT:
				setGeneralComment(GENERAL_COMMENT_EDEFAULT);
				return;
			case MetadataPackage.SCOPE__TEMPORAL_INFORMATION:
				setTemporalInformation(TEMPORAL_INFORMATION_EDEFAULT);
				return;
			case MetadataPackage.SCOPE__PRODUCT:
				getProduct().clear();
				return;
			case MetadataPackage.SCOPE__HAZARD:
				getHazard().clear();
				return;
			case MetadataPackage.SCOPE__POPULATION_GROUP:
				setPopulationGroup((PopulationGroup)null);
				return;
			case MetadataPackage.SCOPE__SPATIAL_INFORMATION:
				setSpatialInformation((SpatialInformation)null);
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
			case MetadataPackage.SCOPE__GENERAL_COMMENT:
				return GENERAL_COMMENT_EDEFAULT == null ? generalComment != null : !GENERAL_COMMENT_EDEFAULT.equals(generalComment);
			case MetadataPackage.SCOPE__TEMPORAL_INFORMATION:
				return TEMPORAL_INFORMATION_EDEFAULT == null ? temporalInformation != null : !TEMPORAL_INFORMATION_EDEFAULT.equals(temporalInformation);
			case MetadataPackage.SCOPE__PRODUCT:
				return product != null && !product.isEmpty();
			case MetadataPackage.SCOPE__HAZARD:
				return hazard != null && !hazard.isEmpty();
			case MetadataPackage.SCOPE__POPULATION_GROUP:
				return populationGroup != null;
			case MetadataPackage.SCOPE__SPATIAL_INFORMATION:
				return spatialInformation != null;
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
		result.append(" (generalComment: ");
		result.append(generalComment);
		result.append(", temporalInformation: ");
		result.append(temporalInformation);
		result.append(')');
		return result.toString();
	}

} //ScopeImpl
