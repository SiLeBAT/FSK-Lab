/**
 */
package metadata;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Product</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Product#getProductName <em>Product Name</em>}</li>
 *   <li>{@link metadata.Product#getProductDescription <em>Product Description</em>}</li>
 *   <li>{@link metadata.Product#getProductUnit <em>Product Unit</em>}</li>
 *   <li>{@link metadata.Product#getProductionMethod <em>Production Method</em>}</li>
 *   <li>{@link metadata.Product#getPackaging <em>Packaging</em>}</li>
 *   <li>{@link metadata.Product#getProductTreatment <em>Product Treatment</em>}</li>
 *   <li>{@link metadata.Product#getOriginCountry <em>Origin Country</em>}</li>
 *   <li>{@link metadata.Product#getOriginArea <em>Origin Area</em>}</li>
 *   <li>{@link metadata.Product#getFisheriesArea <em>Fisheries Area</em>}</li>
 *   <li>{@link metadata.Product#getProductionDate <em>Production Date</em>}</li>
 *   <li>{@link metadata.Product#getExpiryDate <em>Expiry Date</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getProduct()
 * @model
 * @generated
 */
public interface Product extends EObject {
	/**
	 * Returns the value of the '<em><b>Product Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Product Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Product Name</em>' attribute.
	 * @see #setProductName(String)
	 * @see metadata.MetadataPackage#getProduct_ProductName()
	 * @model required="true"
	 * @generated
	 */
	String getProductName();

	/**
	 * Sets the value of the '{@link metadata.Product#getProductName <em>Product Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Product Name</em>' attribute.
	 * @see #getProductName()
	 * @generated
	 */
	void setProductName(String value);

	/**
	 * Returns the value of the '<em><b>Product Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Product Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Product Description</em>' attribute.
	 * @see #setProductDescription(String)
	 * @see metadata.MetadataPackage#getProduct_ProductDescription()
	 * @model
	 * @generated
	 */
	String getProductDescription();

	/**
	 * Sets the value of the '{@link metadata.Product#getProductDescription <em>Product Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Product Description</em>' attribute.
	 * @see #getProductDescription()
	 * @generated
	 */
	void setProductDescription(String value);

	/**
	 * Returns the value of the '<em><b>Product Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Product Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Product Unit</em>' attribute.
	 * @see #setProductUnit(String)
	 * @see metadata.MetadataPackage#getProduct_ProductUnit()
	 * @model required="true"
	 * @generated
	 */
	String getProductUnit();

	/**
	 * Sets the value of the '{@link metadata.Product#getProductUnit <em>Product Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Product Unit</em>' attribute.
	 * @see #getProductUnit()
	 * @generated
	 */
	void setProductUnit(String value);

	/**
	 * Returns the value of the '<em><b>Production Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Production Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Production Method</em>' attribute.
	 * @see #setProductionMethod(String)
	 * @see metadata.MetadataPackage#getProduct_ProductionMethod()
	 * @model
	 * @generated
	 */
	String getProductionMethod();

	/**
	 * Sets the value of the '{@link metadata.Product#getProductionMethod <em>Production Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Production Method</em>' attribute.
	 * @see #getProductionMethod()
	 * @generated
	 */
	void setProductionMethod(String value);

	/**
	 * Returns the value of the '<em><b>Packaging</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Packaging</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Packaging</em>' attribute.
	 * @see #setPackaging(String)
	 * @see metadata.MetadataPackage#getProduct_Packaging()
	 * @model
	 * @generated
	 */
	String getPackaging();

	/**
	 * Sets the value of the '{@link metadata.Product#getPackaging <em>Packaging</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Packaging</em>' attribute.
	 * @see #getPackaging()
	 * @generated
	 */
	void setPackaging(String value);

	/**
	 * Returns the value of the '<em><b>Product Treatment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Product Treatment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Product Treatment</em>' attribute.
	 * @see #setProductTreatment(String)
	 * @see metadata.MetadataPackage#getProduct_ProductTreatment()
	 * @model
	 * @generated
	 */
	String getProductTreatment();

	/**
	 * Sets the value of the '{@link metadata.Product#getProductTreatment <em>Product Treatment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Product Treatment</em>' attribute.
	 * @see #getProductTreatment()
	 * @generated
	 */
	void setProductTreatment(String value);

	/**
	 * Returns the value of the '<em><b>Origin Country</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Country</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Country</em>' attribute.
	 * @see #setOriginCountry(String)
	 * @see metadata.MetadataPackage#getProduct_OriginCountry()
	 * @model
	 * @generated
	 */
	String getOriginCountry();

	/**
	 * Sets the value of the '{@link metadata.Product#getOriginCountry <em>Origin Country</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Country</em>' attribute.
	 * @see #getOriginCountry()
	 * @generated
	 */
	void setOriginCountry(String value);

	/**
	 * Returns the value of the '<em><b>Origin Area</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Area</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Area</em>' attribute.
	 * @see #setOriginArea(String)
	 * @see metadata.MetadataPackage#getProduct_OriginArea()
	 * @model
	 * @generated
	 */
	String getOriginArea();

	/**
	 * Sets the value of the '{@link metadata.Product#getOriginArea <em>Origin Area</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Area</em>' attribute.
	 * @see #getOriginArea()
	 * @generated
	 */
	void setOriginArea(String value);

	/**
	 * Returns the value of the '<em><b>Fisheries Area</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fisheries Area</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fisheries Area</em>' attribute.
	 * @see #setFisheriesArea(String)
	 * @see metadata.MetadataPackage#getProduct_FisheriesArea()
	 * @model
	 * @generated
	 */
	String getFisheriesArea();

	/**
	 * Sets the value of the '{@link metadata.Product#getFisheriesArea <em>Fisheries Area</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fisheries Area</em>' attribute.
	 * @see #getFisheriesArea()
	 * @generated
	 */
	void setFisheriesArea(String value);

	/**
	 * Returns the value of the '<em><b>Production Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Production Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Production Date</em>' attribute.
	 * @see #setProductionDate(Date)
	 * @see metadata.MetadataPackage#getProduct_ProductionDate()
	 * @model
	 * @generated
	 */
	Date getProductionDate();

	/**
	 * Sets the value of the '{@link metadata.Product#getProductionDate <em>Production Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Production Date</em>' attribute.
	 * @see #getProductionDate()
	 * @generated
	 */
	void setProductionDate(Date value);

	/**
	 * Returns the value of the '<em><b>Expiry Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expiry Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expiry Date</em>' attribute.
	 * @see #setExpiryDate(Date)
	 * @see metadata.MetadataPackage#getProduct_ExpiryDate()
	 * @model
	 * @generated
	 */
	Date getExpiryDate();

	/**
	 * Sets the value of the '{@link metadata.Product#getExpiryDate <em>Expiry Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expiry Date</em>' attribute.
	 * @see #getExpiryDate()
	 * @generated
	 */
	void setExpiryDate(Date value);

} // Product
