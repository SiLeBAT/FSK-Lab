/**
 */
package metadata;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>General Information</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.GeneralInformation#getName <em>Name</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getSource <em>Source</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getCreationDate <em>Creation Date</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getRights <em>Rights</em>}</li>
 *   <li>{@link metadata.GeneralInformation#isAvailable <em>Available</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getFormat <em>Format</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getLanguage <em>Language</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getSoftware <em>Software</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getLanguageWrittenIn <em>Language Written In</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getStatus <em>Status</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getObjective <em>Objective</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getDescription <em>Description</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getModelCategory <em>Model Category</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getModificationdate <em>Modificationdate</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getAuthor <em>Author</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getCreators <em>Creators</em>}</li>
 *   <li>{@link metadata.GeneralInformation#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getGeneralInformation()
 * @model
 * @generated
 */
public interface GeneralInformation extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see #setSource(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Source()
	 * @model
	 * @generated
	 */
	String getSource();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getSource <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' attribute.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(String value);

	/**
	 * Returns the value of the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Identifier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Identifier</em>' attribute.
	 * @see #setIdentifier(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Identifier()
	 * @model required="true"
	 * @generated
	 */
	String getIdentifier();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getIdentifier <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Identifier</em>' attribute.
	 * @see #getIdentifier()
	 * @generated
	 */
	void setIdentifier(String value);

	/**
	 * Returns the value of the '<em><b>Creation Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Creation Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Creation Date</em>' attribute.
	 * @see #setCreationDate(Date)
	 * @see metadata.MetadataPackage#getGeneralInformation_CreationDate()
	 * @model required="true"
	 * @generated
	 */
	Date getCreationDate();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getCreationDate <em>Creation Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Creation Date</em>' attribute.
	 * @see #getCreationDate()
	 * @generated
	 */
	void setCreationDate(Date value);

	/**
	 * Returns the value of the '<em><b>Rights</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rights</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rights</em>' attribute.
	 * @see #setRights(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Rights()
	 * @model required="true"
	 * @generated
	 */
	String getRights();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getRights <em>Rights</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rights</em>' attribute.
	 * @see #getRights()
	 * @generated
	 */
	void setRights(String value);

	/**
	 * Returns the value of the '<em><b>Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Available</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Available</em>' attribute.
	 * @see #setAvailable(boolean)
	 * @see metadata.MetadataPackage#getGeneralInformation_Available()
	 * @model
	 * @generated
	 */
	boolean isAvailable();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#isAvailable <em>Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Available</em>' attribute.
	 * @see #isAvailable()
	 * @generated
	 */
	void setAvailable(boolean value);

	/**
	 * Returns the value of the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Format</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Format</em>' attribute.
	 * @see #setFormat(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Format()
	 * @model
	 * @generated
	 */
	String getFormat();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getFormat <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Format</em>' attribute.
	 * @see #getFormat()
	 * @generated
	 */
	void setFormat(String value);

	/**
	 * Returns the value of the '<em><b>Language</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Language</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Language</em>' attribute.
	 * @see #setLanguage(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Language()
	 * @model
	 * @generated
	 */
	String getLanguage();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getLanguage <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Language</em>' attribute.
	 * @see #getLanguage()
	 * @generated
	 */
	void setLanguage(String value);

	/**
	 * Returns the value of the '<em><b>Software</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Software</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Software</em>' attribute.
	 * @see #setSoftware(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Software()
	 * @model
	 * @generated
	 */
	String getSoftware();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getSoftware <em>Software</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Software</em>' attribute.
	 * @see #getSoftware()
	 * @generated
	 */
	void setSoftware(String value);

	/**
	 * Returns the value of the '<em><b>Language Written In</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Language Written In</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Language Written In</em>' attribute.
	 * @see #setLanguageWrittenIn(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_LanguageWrittenIn()
	 * @model
	 * @generated
	 */
	String getLanguageWrittenIn();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getLanguageWrittenIn <em>Language Written In</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Language Written In</em>' attribute.
	 * @see #getLanguageWrittenIn()
	 * @generated
	 */
	void setLanguageWrittenIn(String value);

	/**
	 * Returns the value of the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Status</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' attribute.
	 * @see #setStatus(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Status()
	 * @model
	 * @generated
	 */
	String getStatus();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getStatus <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status</em>' attribute.
	 * @see #getStatus()
	 * @generated
	 */
	void setStatus(String value);

	/**
	 * Returns the value of the '<em><b>Objective</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Objective</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Objective</em>' attribute.
	 * @see #setObjective(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Objective()
	 * @model
	 * @generated
	 */
	String getObjective();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getObjective <em>Objective</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Objective</em>' attribute.
	 * @see #getObjective()
	 * @generated
	 */
	void setObjective(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see metadata.MetadataPackage#getGeneralInformation_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Model Category</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.ModelCategory}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Category</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Category</em>' containment reference list.
	 * @see metadata.MetadataPackage#getGeneralInformation_ModelCategory()
	 * @model containment="true"
	 * @generated
	 */
	EList<ModelCategory> getModelCategory();

	/**
	 * Returns the value of the '<em><b>Modificationdate</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.ModificationDate}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modificationdate</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modificationdate</em>' containment reference list.
	 * @see metadata.MetadataPackage#getGeneralInformation_Modificationdate()
	 * @model containment="true"
	 * @generated
	 */
	EList<ModificationDate> getModificationdate();

	/**
	 * Returns the value of the '<em><b>Author</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Author</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Author</em>' containment reference.
	 * @see #setAuthor(Contact)
	 * @see metadata.MetadataPackage#getGeneralInformation_Author()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Contact getAuthor();

	/**
	 * Sets the value of the '{@link metadata.GeneralInformation#getAuthor <em>Author</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Author</em>' containment reference.
	 * @see #getAuthor()
	 * @generated
	 */
	void setAuthor(Contact value);

	/**
	 * Returns the value of the '<em><b>Creators</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.Contact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Creators</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Creators</em>' containment reference list.
	 * @see metadata.MetadataPackage#getGeneralInformation_Creators()
	 * @model containment="true"
	 * @generated
	 */
	EList<Contact> getCreators();

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.Reference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' containment reference list.
	 * @see metadata.MetadataPackage#getGeneralInformation_Reference()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<Reference> getReference();

} // GeneralInformation
