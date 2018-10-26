/**
 */
package metadata;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Contact#getTitle <em>Title</em>}</li>
 *   <li>{@link metadata.Contact#getFamilyName <em>Family Name</em>}</li>
 *   <li>{@link metadata.Contact#getGivenName <em>Given Name</em>}</li>
 *   <li>{@link metadata.Contact#getEmail <em>Email</em>}</li>
 *   <li>{@link metadata.Contact#getTelephone <em>Telephone</em>}</li>
 *   <li>{@link metadata.Contact#getStreetAddress <em>Street Address</em>}</li>
 *   <li>{@link metadata.Contact#getCountry <em>Country</em>}</li>
 *   <li>{@link metadata.Contact#getCity <em>City</em>}</li>
 *   <li>{@link metadata.Contact#getZipCode <em>Zip Code</em>}</li>
 *   <li>{@link metadata.Contact#getRegion <em>Region</em>}</li>
 *   <li>{@link metadata.Contact#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link metadata.Contact#getGender <em>Gender</em>}</li>
 *   <li>{@link metadata.Contact#getNote <em>Note</em>}</li>
 *   <li>{@link metadata.Contact#getOrganization <em>Organization</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getContact()
 * @model
 * @generated
 */
public interface Contact extends EObject {
	/**
   * Returns the value of the '<em><b>Title</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Title</em>' attribute.
   * @see #setTitle(String)
   * @see metadata.MetadataPackage#getContact_Title()
   * @model
   * @generated
   */
	String getTitle();

	/**
   * Sets the value of the '{@link metadata.Contact#getTitle <em>Title</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Title</em>' attribute.
   * @see #getTitle()
   * @generated
   */
	void setTitle(String value);

	/**
   * Returns the value of the '<em><b>Family Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Family Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Family Name</em>' attribute.
   * @see #setFamilyName(String)
   * @see metadata.MetadataPackage#getContact_FamilyName()
   * @model
   * @generated
   */
	String getFamilyName();

	/**
   * Sets the value of the '{@link metadata.Contact#getFamilyName <em>Family Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Family Name</em>' attribute.
   * @see #getFamilyName()
   * @generated
   */
	void setFamilyName(String value);

	/**
   * Returns the value of the '<em><b>Given Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Given Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Given Name</em>' attribute.
   * @see #setGivenName(String)
   * @see metadata.MetadataPackage#getContact_GivenName()
   * @model
   * @generated
   */
	String getGivenName();

	/**
   * Sets the value of the '{@link metadata.Contact#getGivenName <em>Given Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Given Name</em>' attribute.
   * @see #getGivenName()
   * @generated
   */
	void setGivenName(String value);

	/**
   * Returns the value of the '<em><b>Email</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Email</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Email</em>' attribute.
   * @see #setEmail(String)
   * @see metadata.MetadataPackage#getContact_Email()
   * @model required="true"
   * @generated
   */
	String getEmail();

	/**
   * Sets the value of the '{@link metadata.Contact#getEmail <em>Email</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Email</em>' attribute.
   * @see #getEmail()
   * @generated
   */
	void setEmail(String value);

	/**
   * Returns the value of the '<em><b>Telephone</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Telephone</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Telephone</em>' attribute.
   * @see #setTelephone(String)
   * @see metadata.MetadataPackage#getContact_Telephone()
   * @model
   * @generated
   */
	String getTelephone();

	/**
   * Sets the value of the '{@link metadata.Contact#getTelephone <em>Telephone</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Telephone</em>' attribute.
   * @see #getTelephone()
   * @generated
   */
	void setTelephone(String value);

	/**
   * Returns the value of the '<em><b>Street Address</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Street Address</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Street Address</em>' attribute.
   * @see #setStreetAddress(String)
   * @see metadata.MetadataPackage#getContact_StreetAddress()
   * @model
   * @generated
   */
	String getStreetAddress();

	/**
   * Sets the value of the '{@link metadata.Contact#getStreetAddress <em>Street Address</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Street Address</em>' attribute.
   * @see #getStreetAddress()
   * @generated
   */
	void setStreetAddress(String value);

	/**
   * Returns the value of the '<em><b>Country</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Country</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Country</em>' attribute.
   * @see #setCountry(String)
   * @see metadata.MetadataPackage#getContact_Country()
   * @model
   * @generated
   */
	String getCountry();

	/**
   * Sets the value of the '{@link metadata.Contact#getCountry <em>Country</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Country</em>' attribute.
   * @see #getCountry()
   * @generated
   */
	void setCountry(String value);

	/**
   * Returns the value of the '<em><b>City</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>City</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>City</em>' attribute.
   * @see #setCity(String)
   * @see metadata.MetadataPackage#getContact_City()
   * @model
   * @generated
   */
	String getCity();

	/**
   * Sets the value of the '{@link metadata.Contact#getCity <em>City</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>City</em>' attribute.
   * @see #getCity()
   * @generated
   */
	void setCity(String value);

	/**
   * Returns the value of the '<em><b>Zip Code</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Zip Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Zip Code</em>' attribute.
   * @see #setZipCode(String)
   * @see metadata.MetadataPackage#getContact_ZipCode()
   * @model
   * @generated
   */
	String getZipCode();

	/**
   * Sets the value of the '{@link metadata.Contact#getZipCode <em>Zip Code</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Zip Code</em>' attribute.
   * @see #getZipCode()
   * @generated
   */
	void setZipCode(String value);

	/**
   * Returns the value of the '<em><b>Region</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Region</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Region</em>' attribute.
   * @see #setRegion(String)
   * @see metadata.MetadataPackage#getContact_Region()
   * @model
   * @generated
   */
	String getRegion();

	/**
   * Sets the value of the '{@link metadata.Contact#getRegion <em>Region</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Region</em>' attribute.
   * @see #getRegion()
   * @generated
   */
	void setRegion(String value);

	/**
   * Returns the value of the '<em><b>Time Zone</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Zone</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Time Zone</em>' attribute.
   * @see #setTimeZone(String)
   * @see metadata.MetadataPackage#getContact_TimeZone()
   * @model
   * @generated
   */
	String getTimeZone();

	/**
   * Sets the value of the '{@link metadata.Contact#getTimeZone <em>Time Zone</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Time Zone</em>' attribute.
   * @see #getTimeZone()
   * @generated
   */
	void setTimeZone(String value);

	/**
   * Returns the value of the '<em><b>Gender</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Gender</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Gender</em>' attribute.
   * @see #setGender(String)
   * @see metadata.MetadataPackage#getContact_Gender()
   * @model
   * @generated
   */
	String getGender();

	/**
   * Sets the value of the '{@link metadata.Contact#getGender <em>Gender</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Gender</em>' attribute.
   * @see #getGender()
   * @generated
   */
	void setGender(String value);

	/**
   * Returns the value of the '<em><b>Note</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Note</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Note</em>' attribute.
   * @see #setNote(String)
   * @see metadata.MetadataPackage#getContact_Note()
   * @model
   * @generated
   */
	String getNote();

	/**
   * Sets the value of the '{@link metadata.Contact#getNote <em>Note</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Note</em>' attribute.
   * @see #getNote()
   * @generated
   */
	void setNote(String value);

	/**
   * Returns the value of the '<em><b>Organization</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Organization</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Organization</em>' attribute.
   * @see #setOrganization(String)
   * @see metadata.MetadataPackage#getContact_Organization()
   * @model
   * @generated
   */
	String getOrganization();

	/**
   * Sets the value of the '{@link metadata.Contact#getOrganization <em>Organization</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Organization</em>' attribute.
   * @see #getOrganization()
   * @generated
   */
	void setOrganization(String value);

} // Contact
