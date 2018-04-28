/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.Assay;
import metadata.DataBackground;
import metadata.DietaryAssessmentMethod;
import metadata.Laboratory;
import metadata.MetadataPackage;
import metadata.Study;
import metadata.StudySample;

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
 * An implementation of the model object '<em><b>Data Background</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.DataBackgroundImpl#getStudy <em>Study</em>}</li>
 *   <li>{@link metadata.impl.DataBackgroundImpl#getStudysample <em>Studysample</em>}</li>
 *   <li>{@link metadata.impl.DataBackgroundImpl#getDietaryassessmentmethod <em>Dietaryassessmentmethod</em>}</li>
 *   <li>{@link metadata.impl.DataBackgroundImpl#getLaboratory <em>Laboratory</em>}</li>
 *   <li>{@link metadata.impl.DataBackgroundImpl#getAssay <em>Assay</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataBackgroundImpl extends MinimalEObjectImpl.Container implements DataBackground {
	/**
	 * The cached value of the '{@link #getStudy() <em>Study</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudy()
	 * @generated
	 * @ordered
	 */
	protected Study study;

	/**
	 * The cached value of the '{@link #getStudysample() <em>Studysample</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudysample()
	 * @generated
	 * @ordered
	 */
	protected EList<StudySample> studysample;

	/**
	 * The cached value of the '{@link #getDietaryassessmentmethod() <em>Dietaryassessmentmethod</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDietaryassessmentmethod()
	 * @generated
	 * @ordered
	 */
	protected DietaryAssessmentMethod dietaryassessmentmethod;

	/**
	 * The cached value of the '{@link #getLaboratory() <em>Laboratory</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLaboratory()
	 * @generated
	 * @ordered
	 */
	protected Laboratory laboratory;

	/**
	 * The cached value of the '{@link #getAssay() <em>Assay</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssay()
	 * @generated
	 * @ordered
	 */
	protected EList<Assay> assay;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataBackgroundImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.DATA_BACKGROUND;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Study getStudy() {
		return study;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStudy(Study newStudy, NotificationChain msgs) {
		Study oldStudy = study;
		study = newStudy;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MetadataPackage.DATA_BACKGROUND__STUDY, oldStudy, newStudy);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudy(Study newStudy) {
		if (newStudy != study) {
			NotificationChain msgs = null;
			if (study != null)
				msgs = ((InternalEObject)study).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.DATA_BACKGROUND__STUDY, null, msgs);
			if (newStudy != null)
				msgs = ((InternalEObject)newStudy).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.DATA_BACKGROUND__STUDY, null, msgs);
			msgs = basicSetStudy(newStudy, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DATA_BACKGROUND__STUDY, newStudy, newStudy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StudySample> getStudysample() {
		if (studysample == null) {
			studysample = new EObjectContainmentEList<StudySample>(StudySample.class, this, MetadataPackage.DATA_BACKGROUND__STUDYSAMPLE);
		}
		return studysample;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DietaryAssessmentMethod getDietaryassessmentmethod() {
		return dietaryassessmentmethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDietaryassessmentmethod(DietaryAssessmentMethod newDietaryassessmentmethod, NotificationChain msgs) {
		DietaryAssessmentMethod oldDietaryassessmentmethod = dietaryassessmentmethod;
		dietaryassessmentmethod = newDietaryassessmentmethod;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD, oldDietaryassessmentmethod, newDietaryassessmentmethod);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDietaryassessmentmethod(DietaryAssessmentMethod newDietaryassessmentmethod) {
		if (newDietaryassessmentmethod != dietaryassessmentmethod) {
			NotificationChain msgs = null;
			if (dietaryassessmentmethod != null)
				msgs = ((InternalEObject)dietaryassessmentmethod).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD, null, msgs);
			if (newDietaryassessmentmethod != null)
				msgs = ((InternalEObject)newDietaryassessmentmethod).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD, null, msgs);
			msgs = basicSetDietaryassessmentmethod(newDietaryassessmentmethod, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD, newDietaryassessmentmethod, newDietaryassessmentmethod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Laboratory getLaboratory() {
		return laboratory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLaboratory(Laboratory newLaboratory, NotificationChain msgs) {
		Laboratory oldLaboratory = laboratory;
		laboratory = newLaboratory;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MetadataPackage.DATA_BACKGROUND__LABORATORY, oldLaboratory, newLaboratory);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLaboratory(Laboratory newLaboratory) {
		if (newLaboratory != laboratory) {
			NotificationChain msgs = null;
			if (laboratory != null)
				msgs = ((InternalEObject)laboratory).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.DATA_BACKGROUND__LABORATORY, null, msgs);
			if (newLaboratory != null)
				msgs = ((InternalEObject)newLaboratory).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.DATA_BACKGROUND__LABORATORY, null, msgs);
			msgs = basicSetLaboratory(newLaboratory, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.DATA_BACKGROUND__LABORATORY, newLaboratory, newLaboratory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Assay> getAssay() {
		if (assay == null) {
			assay = new EObjectContainmentEList<Assay>(Assay.class, this, MetadataPackage.DATA_BACKGROUND__ASSAY);
		}
		return assay;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MetadataPackage.DATA_BACKGROUND__STUDY:
				return basicSetStudy(null, msgs);
			case MetadataPackage.DATA_BACKGROUND__STUDYSAMPLE:
				return ((InternalEList<?>)getStudysample()).basicRemove(otherEnd, msgs);
			case MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD:
				return basicSetDietaryassessmentmethod(null, msgs);
			case MetadataPackage.DATA_BACKGROUND__LABORATORY:
				return basicSetLaboratory(null, msgs);
			case MetadataPackage.DATA_BACKGROUND__ASSAY:
				return ((InternalEList<?>)getAssay()).basicRemove(otherEnd, msgs);
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
			case MetadataPackage.DATA_BACKGROUND__STUDY:
				return getStudy();
			case MetadataPackage.DATA_BACKGROUND__STUDYSAMPLE:
				return getStudysample();
			case MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD:
				return getDietaryassessmentmethod();
			case MetadataPackage.DATA_BACKGROUND__LABORATORY:
				return getLaboratory();
			case MetadataPackage.DATA_BACKGROUND__ASSAY:
				return getAssay();
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
			case MetadataPackage.DATA_BACKGROUND__STUDY:
				setStudy((Study)newValue);
				return;
			case MetadataPackage.DATA_BACKGROUND__STUDYSAMPLE:
				getStudysample().clear();
				getStudysample().addAll((Collection<? extends StudySample>)newValue);
				return;
			case MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD:
				setDietaryassessmentmethod((DietaryAssessmentMethod)newValue);
				return;
			case MetadataPackage.DATA_BACKGROUND__LABORATORY:
				setLaboratory((Laboratory)newValue);
				return;
			case MetadataPackage.DATA_BACKGROUND__ASSAY:
				getAssay().clear();
				getAssay().addAll((Collection<? extends Assay>)newValue);
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
			case MetadataPackage.DATA_BACKGROUND__STUDY:
				setStudy((Study)null);
				return;
			case MetadataPackage.DATA_BACKGROUND__STUDYSAMPLE:
				getStudysample().clear();
				return;
			case MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD:
				setDietaryassessmentmethod((DietaryAssessmentMethod)null);
				return;
			case MetadataPackage.DATA_BACKGROUND__LABORATORY:
				setLaboratory((Laboratory)null);
				return;
			case MetadataPackage.DATA_BACKGROUND__ASSAY:
				getAssay().clear();
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
			case MetadataPackage.DATA_BACKGROUND__STUDY:
				return study != null;
			case MetadataPackage.DATA_BACKGROUND__STUDYSAMPLE:
				return studysample != null && !studysample.isEmpty();
			case MetadataPackage.DATA_BACKGROUND__DIETARYASSESSMENTMETHOD:
				return dietaryassessmentmethod != null;
			case MetadataPackage.DATA_BACKGROUND__LABORATORY:
				return laboratory != null;
			case MetadataPackage.DATA_BACKGROUND__ASSAY:
				return assay != null && !assay.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DataBackgroundImpl
