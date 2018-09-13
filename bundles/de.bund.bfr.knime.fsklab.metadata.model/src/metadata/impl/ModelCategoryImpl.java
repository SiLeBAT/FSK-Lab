/**
 */
package metadata.impl;

import java.util.Collection;

import metadata.MetadataPackage;
import metadata.ModelCategory;
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
 * An implementation of the model object '<em><b>Model Category</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.ModelCategoryImpl#getModelClass <em>Model Class</em>}</li>
 *   <li>{@link metadata.impl.ModelCategoryImpl#getModelClassComment <em>Model Class Comment</em>}</li>
 *   <li>{@link metadata.impl.ModelCategoryImpl#getBasicProcess <em>Basic Process</em>}</li>
 *   <li>{@link metadata.impl.ModelCategoryImpl#getModelSubClass <em>Model Sub Class</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelCategoryImpl extends MinimalEObjectImpl.Container implements ModelCategory {
	/**
   * The default value of the '{@link #getModelClass() <em>Model Class</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getModelClass()
   * @generated
   * @ordered
   */
	protected static final String MODEL_CLASS_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getModelClass() <em>Model Class</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getModelClass()
   * @generated
   * @ordered
   */
	protected String modelClass = MODEL_CLASS_EDEFAULT;

	/**
   * The default value of the '{@link #getModelClassComment() <em>Model Class Comment</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getModelClassComment()
   * @generated
   * @ordered
   */
	protected static final String MODEL_CLASS_COMMENT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getModelClassComment() <em>Model Class Comment</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getModelClassComment()
   * @generated
   * @ordered
   */
	protected String modelClassComment = MODEL_CLASS_COMMENT_EDEFAULT;

	/**
   * The default value of the '{@link #getBasicProcess() <em>Basic Process</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getBasicProcess()
   * @generated
   * @ordered
   */
	protected static final String BASIC_PROCESS_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getBasicProcess() <em>Basic Process</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getBasicProcess()
   * @generated
   * @ordered
   */
	protected String basicProcess = BASIC_PROCESS_EDEFAULT;

	/**
   * The cached value of the '{@link #getModelSubClass() <em>Model Sub Class</em>}' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getModelSubClass()
   * @generated
   * @ordered
   */
	protected EList<StringObject> modelSubClass;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected ModelCategoryImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return MetadataPackage.Literals.MODEL_CATEGORY;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getModelClass() {
    return modelClass;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setModelClass(String newModelClass) {
    String oldModelClass = modelClass;
    modelClass = newModelClass;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_CATEGORY__MODEL_CLASS, oldModelClass, modelClass));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getModelClassComment() {
    return modelClassComment;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setModelClassComment(String newModelClassComment) {
    String oldModelClassComment = modelClassComment;
    modelClassComment = newModelClassComment;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_CATEGORY__MODEL_CLASS_COMMENT, oldModelClassComment, modelClassComment));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getBasicProcess() {
    return basicProcess;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setBasicProcess(String newBasicProcess) {
    String oldBasicProcess = basicProcess;
    basicProcess = newBasicProcess;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.MODEL_CATEGORY__BASIC_PROCESS, oldBasicProcess, basicProcess));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<StringObject> getModelSubClass() {
    if (modelSubClass == null) {
      modelSubClass = new EObjectContainmentEList<StringObject>(StringObject.class, this, MetadataPackage.MODEL_CATEGORY__MODEL_SUB_CLASS);
    }
    return modelSubClass;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case MetadataPackage.MODEL_CATEGORY__MODEL_SUB_CLASS:
        return ((InternalEList<?>)getModelSubClass()).basicRemove(otherEnd, msgs);
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
      case MetadataPackage.MODEL_CATEGORY__MODEL_CLASS:
        return getModelClass();
      case MetadataPackage.MODEL_CATEGORY__MODEL_CLASS_COMMENT:
        return getModelClassComment();
      case MetadataPackage.MODEL_CATEGORY__BASIC_PROCESS:
        return getBasicProcess();
      case MetadataPackage.MODEL_CATEGORY__MODEL_SUB_CLASS:
        return getModelSubClass();
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
      case MetadataPackage.MODEL_CATEGORY__MODEL_CLASS:
        setModelClass((String)newValue);
        return;
      case MetadataPackage.MODEL_CATEGORY__MODEL_CLASS_COMMENT:
        setModelClassComment((String)newValue);
        return;
      case MetadataPackage.MODEL_CATEGORY__BASIC_PROCESS:
        setBasicProcess((String)newValue);
        return;
      case MetadataPackage.MODEL_CATEGORY__MODEL_SUB_CLASS:
        getModelSubClass().clear();
        getModelSubClass().addAll((Collection<? extends StringObject>)newValue);
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
      case MetadataPackage.MODEL_CATEGORY__MODEL_CLASS:
        setModelClass(MODEL_CLASS_EDEFAULT);
        return;
      case MetadataPackage.MODEL_CATEGORY__MODEL_CLASS_COMMENT:
        setModelClassComment(MODEL_CLASS_COMMENT_EDEFAULT);
        return;
      case MetadataPackage.MODEL_CATEGORY__BASIC_PROCESS:
        setBasicProcess(BASIC_PROCESS_EDEFAULT);
        return;
      case MetadataPackage.MODEL_CATEGORY__MODEL_SUB_CLASS:
        getModelSubClass().clear();
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
      case MetadataPackage.MODEL_CATEGORY__MODEL_CLASS:
        return MODEL_CLASS_EDEFAULT == null ? modelClass != null : !MODEL_CLASS_EDEFAULT.equals(modelClass);
      case MetadataPackage.MODEL_CATEGORY__MODEL_CLASS_COMMENT:
        return MODEL_CLASS_COMMENT_EDEFAULT == null ? modelClassComment != null : !MODEL_CLASS_COMMENT_EDEFAULT.equals(modelClassComment);
      case MetadataPackage.MODEL_CATEGORY__BASIC_PROCESS:
        return BASIC_PROCESS_EDEFAULT == null ? basicProcess != null : !BASIC_PROCESS_EDEFAULT.equals(basicProcess);
      case MetadataPackage.MODEL_CATEGORY__MODEL_SUB_CLASS:
        return modelSubClass != null && !modelSubClass.isEmpty();
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
    result.append(" (modelClass: ");
    result.append(modelClass);
    result.append(", modelClassComment: ");
    result.append(modelClassComment);
    result.append(", basicProcess: ");
    result.append(basicProcess);
    result.append(')');
    return result.toString();
  }

} //ModelCategoryImpl
