/**
 */
package metadata.impl;

import java.util.Date;

import metadata.MetadataPackage;
import metadata.Product;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Product</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.ProductImpl#getProductName <em>Product Name</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getProductDescription <em>Product Description</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getProductUnit <em>Product Unit</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getProductionMethod <em>Production Method</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getPackaging <em>Packaging</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getProductTreatment <em>Product Treatment</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getOriginCountry <em>Origin Country</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getOriginArea <em>Origin Area</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getFisheriesArea <em>Fisheries Area</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getProductionDate <em>Production Date</em>}</li>
 *   <li>{@link metadata.impl.ProductImpl#getExpiryDate <em>Expiry Date</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProductImpl extends MinimalEObjectImpl.Container implements Product {
	/**
   * The default value of the '{@link #getProductName() <em>Product Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductName()
   * @generated
   * @ordered
   */
	protected static final String PRODUCT_NAME_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getProductName() <em>Product Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductName()
   * @generated
   * @ordered
   */
	protected String productName = PRODUCT_NAME_EDEFAULT;

	/**
   * The default value of the '{@link #getProductDescription() <em>Product Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductDescription()
   * @generated
   * @ordered
   */
	protected static final String PRODUCT_DESCRIPTION_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getProductDescription() <em>Product Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductDescription()
   * @generated
   * @ordered
   */
	protected String productDescription = PRODUCT_DESCRIPTION_EDEFAULT;

	/**
   * The default value of the '{@link #getProductUnit() <em>Product Unit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductUnit()
   * @generated
   * @ordered
   */
	protected static final String PRODUCT_UNIT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getProductUnit() <em>Product Unit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductUnit()
   * @generated
   * @ordered
   */
	protected String productUnit = PRODUCT_UNIT_EDEFAULT;

	/**
   * The default value of the '{@link #getProductionMethod() <em>Production Method</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductionMethod()
   * @generated
   * @ordered
   */
	protected static final String PRODUCTION_METHOD_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getProductionMethod() <em>Production Method</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductionMethod()
   * @generated
   * @ordered
   */
	protected String productionMethod = PRODUCTION_METHOD_EDEFAULT;

	/**
   * The default value of the '{@link #getPackaging() <em>Packaging</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getPackaging()
   * @generated
   * @ordered
   */
	protected static final String PACKAGING_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getPackaging() <em>Packaging</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getPackaging()
   * @generated
   * @ordered
   */
	protected String packaging = PACKAGING_EDEFAULT;

	/**
   * The default value of the '{@link #getProductTreatment() <em>Product Treatment</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductTreatment()
   * @generated
   * @ordered
   */
	protected static final String PRODUCT_TREATMENT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getProductTreatment() <em>Product Treatment</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductTreatment()
   * @generated
   * @ordered
   */
	protected String productTreatment = PRODUCT_TREATMENT_EDEFAULT;

	/**
   * The default value of the '{@link #getOriginCountry() <em>Origin Country</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getOriginCountry()
   * @generated
   * @ordered
   */
	protected static final String ORIGIN_COUNTRY_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getOriginCountry() <em>Origin Country</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getOriginCountry()
   * @generated
   * @ordered
   */
	protected String originCountry = ORIGIN_COUNTRY_EDEFAULT;

	/**
   * The default value of the '{@link #getOriginArea() <em>Origin Area</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getOriginArea()
   * @generated
   * @ordered
   */
	protected static final String ORIGIN_AREA_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getOriginArea() <em>Origin Area</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getOriginArea()
   * @generated
   * @ordered
   */
	protected String originArea = ORIGIN_AREA_EDEFAULT;

	/**
   * The default value of the '{@link #getFisheriesArea() <em>Fisheries Area</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getFisheriesArea()
   * @generated
   * @ordered
   */
	protected static final String FISHERIES_AREA_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getFisheriesArea() <em>Fisheries Area</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getFisheriesArea()
   * @generated
   * @ordered
   */
	protected String fisheriesArea = FISHERIES_AREA_EDEFAULT;

	/**
   * The default value of the '{@link #getProductionDate() <em>Production Date</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductionDate()
   * @generated
   * @ordered
   */
	protected static final Date PRODUCTION_DATE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getProductionDate() <em>Production Date</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getProductionDate()
   * @generated
   * @ordered
   */
	protected Date productionDate = PRODUCTION_DATE_EDEFAULT;

	/**
   * The default value of the '{@link #getExpiryDate() <em>Expiry Date</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getExpiryDate()
   * @generated
   * @ordered
   */
	protected static final Date EXPIRY_DATE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getExpiryDate() <em>Expiry Date</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getExpiryDate()
   * @generated
   * @ordered
   */
	protected Date expiryDate = EXPIRY_DATE_EDEFAULT;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected ProductImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return MetadataPackage.Literals.PRODUCT;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getProductName() {
    return productName;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setProductName(String newProductName) {
    String oldProductName = productName;
    productName = newProductName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__PRODUCT_NAME, oldProductName, productName));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getProductDescription() {
    return productDescription;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setProductDescription(String newProductDescription) {
    String oldProductDescription = productDescription;
    productDescription = newProductDescription;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__PRODUCT_DESCRIPTION, oldProductDescription, productDescription));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getProductUnit() {
    return productUnit;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setProductUnit(String newProductUnit) {
    String oldProductUnit = productUnit;
    productUnit = newProductUnit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__PRODUCT_UNIT, oldProductUnit, productUnit));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getProductionMethod() {
    return productionMethod;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setProductionMethod(String newProductionMethod) {
    String oldProductionMethod = productionMethod;
    productionMethod = newProductionMethod;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__PRODUCTION_METHOD, oldProductionMethod, productionMethod));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getPackaging() {
    return packaging;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setPackaging(String newPackaging) {
    String oldPackaging = packaging;
    packaging = newPackaging;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__PACKAGING, oldPackaging, packaging));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getProductTreatment() {
    return productTreatment;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setProductTreatment(String newProductTreatment) {
    String oldProductTreatment = productTreatment;
    productTreatment = newProductTreatment;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__PRODUCT_TREATMENT, oldProductTreatment, productTreatment));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getOriginCountry() {
    return originCountry;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setOriginCountry(String newOriginCountry) {
    String oldOriginCountry = originCountry;
    originCountry = newOriginCountry;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__ORIGIN_COUNTRY, oldOriginCountry, originCountry));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getOriginArea() {
    return originArea;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setOriginArea(String newOriginArea) {
    String oldOriginArea = originArea;
    originArea = newOriginArea;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__ORIGIN_AREA, oldOriginArea, originArea));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getFisheriesArea() {
    return fisheriesArea;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setFisheriesArea(String newFisheriesArea) {
    String oldFisheriesArea = fisheriesArea;
    fisheriesArea = newFisheriesArea;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__FISHERIES_AREA, oldFisheriesArea, fisheriesArea));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Date getProductionDate() {
    return productionDate;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setProductionDate(Date newProductionDate) {
    Date oldProductionDate = productionDate;
    productionDate = newProductionDate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__PRODUCTION_DATE, oldProductionDate, productionDate));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Date getExpiryDate() {
    return expiryDate;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setExpiryDate(Date newExpiryDate) {
    Date oldExpiryDate = expiryDate;
    expiryDate = newExpiryDate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.PRODUCT__EXPIRY_DATE, oldExpiryDate, expiryDate));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
      case MetadataPackage.PRODUCT__PRODUCT_NAME:
        return getProductName();
      case MetadataPackage.PRODUCT__PRODUCT_DESCRIPTION:
        return getProductDescription();
      case MetadataPackage.PRODUCT__PRODUCT_UNIT:
        return getProductUnit();
      case MetadataPackage.PRODUCT__PRODUCTION_METHOD:
        return getProductionMethod();
      case MetadataPackage.PRODUCT__PACKAGING:
        return getPackaging();
      case MetadataPackage.PRODUCT__PRODUCT_TREATMENT:
        return getProductTreatment();
      case MetadataPackage.PRODUCT__ORIGIN_COUNTRY:
        return getOriginCountry();
      case MetadataPackage.PRODUCT__ORIGIN_AREA:
        return getOriginArea();
      case MetadataPackage.PRODUCT__FISHERIES_AREA:
        return getFisheriesArea();
      case MetadataPackage.PRODUCT__PRODUCTION_DATE:
        return getProductionDate();
      case MetadataPackage.PRODUCT__EXPIRY_DATE:
        return getExpiryDate();
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
      case MetadataPackage.PRODUCT__PRODUCT_NAME:
        setProductName((String)newValue);
        return;
      case MetadataPackage.PRODUCT__PRODUCT_DESCRIPTION:
        setProductDescription((String)newValue);
        return;
      case MetadataPackage.PRODUCT__PRODUCT_UNIT:
        setProductUnit((String)newValue);
        return;
      case MetadataPackage.PRODUCT__PRODUCTION_METHOD:
        setProductionMethod((String)newValue);
        return;
      case MetadataPackage.PRODUCT__PACKAGING:
        setPackaging((String)newValue);
        return;
      case MetadataPackage.PRODUCT__PRODUCT_TREATMENT:
        setProductTreatment((String)newValue);
        return;
      case MetadataPackage.PRODUCT__ORIGIN_COUNTRY:
        setOriginCountry((String)newValue);
        return;
      case MetadataPackage.PRODUCT__ORIGIN_AREA:
        setOriginArea((String)newValue);
        return;
      case MetadataPackage.PRODUCT__FISHERIES_AREA:
        setFisheriesArea((String)newValue);
        return;
      case MetadataPackage.PRODUCT__PRODUCTION_DATE:
        setProductionDate((Date)newValue);
        return;
      case MetadataPackage.PRODUCT__EXPIRY_DATE:
        setExpiryDate((Date)newValue);
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
      case MetadataPackage.PRODUCT__PRODUCT_NAME:
        setProductName(PRODUCT_NAME_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__PRODUCT_DESCRIPTION:
        setProductDescription(PRODUCT_DESCRIPTION_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__PRODUCT_UNIT:
        setProductUnit(PRODUCT_UNIT_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__PRODUCTION_METHOD:
        setProductionMethod(PRODUCTION_METHOD_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__PACKAGING:
        setPackaging(PACKAGING_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__PRODUCT_TREATMENT:
        setProductTreatment(PRODUCT_TREATMENT_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__ORIGIN_COUNTRY:
        setOriginCountry(ORIGIN_COUNTRY_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__ORIGIN_AREA:
        setOriginArea(ORIGIN_AREA_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__FISHERIES_AREA:
        setFisheriesArea(FISHERIES_AREA_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__PRODUCTION_DATE:
        setProductionDate(PRODUCTION_DATE_EDEFAULT);
        return;
      case MetadataPackage.PRODUCT__EXPIRY_DATE:
        setExpiryDate(EXPIRY_DATE_EDEFAULT);
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
      case MetadataPackage.PRODUCT__PRODUCT_NAME:
        return PRODUCT_NAME_EDEFAULT == null ? productName != null : !PRODUCT_NAME_EDEFAULT.equals(productName);
      case MetadataPackage.PRODUCT__PRODUCT_DESCRIPTION:
        return PRODUCT_DESCRIPTION_EDEFAULT == null ? productDescription != null : !PRODUCT_DESCRIPTION_EDEFAULT.equals(productDescription);
      case MetadataPackage.PRODUCT__PRODUCT_UNIT:
        return PRODUCT_UNIT_EDEFAULT == null ? productUnit != null : !PRODUCT_UNIT_EDEFAULT.equals(productUnit);
      case MetadataPackage.PRODUCT__PRODUCTION_METHOD:
        return PRODUCTION_METHOD_EDEFAULT == null ? productionMethod != null : !PRODUCTION_METHOD_EDEFAULT.equals(productionMethod);
      case MetadataPackage.PRODUCT__PACKAGING:
        return PACKAGING_EDEFAULT == null ? packaging != null : !PACKAGING_EDEFAULT.equals(packaging);
      case MetadataPackage.PRODUCT__PRODUCT_TREATMENT:
        return PRODUCT_TREATMENT_EDEFAULT == null ? productTreatment != null : !PRODUCT_TREATMENT_EDEFAULT.equals(productTreatment);
      case MetadataPackage.PRODUCT__ORIGIN_COUNTRY:
        return ORIGIN_COUNTRY_EDEFAULT == null ? originCountry != null : !ORIGIN_COUNTRY_EDEFAULT.equals(originCountry);
      case MetadataPackage.PRODUCT__ORIGIN_AREA:
        return ORIGIN_AREA_EDEFAULT == null ? originArea != null : !ORIGIN_AREA_EDEFAULT.equals(originArea);
      case MetadataPackage.PRODUCT__FISHERIES_AREA:
        return FISHERIES_AREA_EDEFAULT == null ? fisheriesArea != null : !FISHERIES_AREA_EDEFAULT.equals(fisheriesArea);
      case MetadataPackage.PRODUCT__PRODUCTION_DATE:
        return PRODUCTION_DATE_EDEFAULT == null ? productionDate != null : !PRODUCTION_DATE_EDEFAULT.equals(productionDate);
      case MetadataPackage.PRODUCT__EXPIRY_DATE:
        return EXPIRY_DATE_EDEFAULT == null ? expiryDate != null : !EXPIRY_DATE_EDEFAULT.equals(expiryDate);
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
    result.append(" (productName: ");
    result.append(productName);
    result.append(", productDescription: ");
    result.append(productDescription);
    result.append(", productUnit: ");
    result.append(productUnit);
    result.append(", productionMethod: ");
    result.append(productionMethod);
    result.append(", packaging: ");
    result.append(packaging);
    result.append(", productTreatment: ");
    result.append(productTreatment);
    result.append(", originCountry: ");
    result.append(originCountry);
    result.append(", originArea: ");
    result.append(originArea);
    result.append(", fisheriesArea: ");
    result.append(fisheriesArea);
    result.append(", productionDate: ");
    result.append(productionDate);
    result.append(", expiryDate: ");
    result.append(expiryDate);
    result.append(')');
    return result.toString();
  }

} //ProductImpl
