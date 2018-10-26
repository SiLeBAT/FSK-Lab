/**
 */
package metadata;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Reference#isIsReferenceDescription <em>Is Reference Description</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationType <em>Publication Type</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationDate <em>Publication Date</em>}</li>
 *   <li>{@link metadata.Reference#getPmid <em>Pmid</em>}</li>
 *   <li>{@link metadata.Reference#getDoi <em>Doi</em>}</li>
 *   <li>{@link metadata.Reference#getAuthorList <em>Author List</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationTitle <em>Publication Title</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationAbstract <em>Publication Abstract</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationJournal <em>Publication Journal</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationVolume <em>Publication Volume</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationIssue <em>Publication Issue</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationStatus <em>Publication Status</em>}</li>
 *   <li>{@link metadata.Reference#getPublicationWebsite <em>Publication Website</em>}</li>
 *   <li>{@link metadata.Reference#getComment <em>Comment</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getReference()
 * @model
 * @generated
 */
public interface Reference extends EObject {
	/**
   * Returns the value of the '<em><b>Is Reference Description</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Reference Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Is Reference Description</em>' attribute.
   * @see #setIsReferenceDescription(boolean)
   * @see metadata.MetadataPackage#getReference_IsReferenceDescription()
   * @model required="true"
   * @generated
   */
	boolean isIsReferenceDescription();

	/**
   * Sets the value of the '{@link metadata.Reference#isIsReferenceDescription <em>Is Reference Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Is Reference Description</em>' attribute.
   * @see #isIsReferenceDescription()
   * @generated
   */
	void setIsReferenceDescription(boolean value);

	/**
   * Returns the value of the '<em><b>Publication Type</b></em>' attribute.
   * The literals are from the enumeration {@link metadata.PublicationType}.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Type</em>' attribute.
   * @see metadata.PublicationType
   * @see #setPublicationType(PublicationType)
   * @see metadata.MetadataPackage#getReference_PublicationType()
   * @model
   * @generated
   */
	PublicationType getPublicationType();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationType <em>Publication Type</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Type</em>' attribute.
   * @see metadata.PublicationType
   * @see #getPublicationType()
   * @generated
   */
	void setPublicationType(PublicationType value);

	/**
   * Returns the value of the '<em><b>Publication Date</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Date</em>' attribute.
   * @see #setPublicationDate(Date)
   * @see metadata.MetadataPackage#getReference_PublicationDate()
   * @model
   * @generated
   */
	Date getPublicationDate();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationDate <em>Publication Date</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Date</em>' attribute.
   * @see #getPublicationDate()
   * @generated
   */
	void setPublicationDate(Date value);

	/**
   * Returns the value of the '<em><b>Pmid</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pmid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Pmid</em>' attribute.
   * @see #setPmid(String)
   * @see metadata.MetadataPackage#getReference_Pmid()
   * @model
   * @generated
   */
	String getPmid();

	/**
   * Sets the value of the '{@link metadata.Reference#getPmid <em>Pmid</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pmid</em>' attribute.
   * @see #getPmid()
   * @generated
   */
	void setPmid(String value);

	/**
   * Returns the value of the '<em><b>Doi</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Doi</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Doi</em>' attribute.
   * @see #setDoi(String)
   * @see metadata.MetadataPackage#getReference_Doi()
   * @model
   * @generated
   */
	String getDoi();

	/**
   * Sets the value of the '{@link metadata.Reference#getDoi <em>Doi</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Doi</em>' attribute.
   * @see #getDoi()
   * @generated
   */
	void setDoi(String value);

	/**
   * Returns the value of the '<em><b>Author List</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Author List</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Author List</em>' attribute.
   * @see #setAuthorList(String)
   * @see metadata.MetadataPackage#getReference_AuthorList()
   * @model
   * @generated
   */
	String getAuthorList();

	/**
   * Sets the value of the '{@link metadata.Reference#getAuthorList <em>Author List</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Author List</em>' attribute.
   * @see #getAuthorList()
   * @generated
   */
	void setAuthorList(String value);

	/**
   * Returns the value of the '<em><b>Publication Title</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Title</em>' attribute.
   * @see #setPublicationTitle(String)
   * @see metadata.MetadataPackage#getReference_PublicationTitle()
   * @model required="true"
   * @generated
   */
	String getPublicationTitle();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationTitle <em>Publication Title</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Title</em>' attribute.
   * @see #getPublicationTitle()
   * @generated
   */
	void setPublicationTitle(String value);

	/**
   * Returns the value of the '<em><b>Publication Abstract</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Abstract</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Abstract</em>' attribute.
   * @see #setPublicationAbstract(String)
   * @see metadata.MetadataPackage#getReference_PublicationAbstract()
   * @model
   * @generated
   */
	String getPublicationAbstract();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationAbstract <em>Publication Abstract</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Abstract</em>' attribute.
   * @see #getPublicationAbstract()
   * @generated
   */
	void setPublicationAbstract(String value);

	/**
   * Returns the value of the '<em><b>Publication Journal</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Journal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Journal</em>' attribute.
   * @see #setPublicationJournal(String)
   * @see metadata.MetadataPackage#getReference_PublicationJournal()
   * @model
   * @generated
   */
	String getPublicationJournal();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationJournal <em>Publication Journal</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Journal</em>' attribute.
   * @see #getPublicationJournal()
   * @generated
   */
	void setPublicationJournal(String value);

	/**
   * Returns the value of the '<em><b>Publication Volume</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Volume</em>' attribute.
   * @see #setPublicationVolume(int)
   * @see metadata.MetadataPackage#getReference_PublicationVolume()
   * @model
   * @generated
   */
	int getPublicationVolume();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationVolume <em>Publication Volume</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Volume</em>' attribute.
   * @see #getPublicationVolume()
   * @generated
   */
	void setPublicationVolume(int value);

	/**
   * Returns the value of the '<em><b>Publication Issue</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Issue</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Issue</em>' attribute.
   * @see #setPublicationIssue(int)
   * @see metadata.MetadataPackage#getReference_PublicationIssue()
   * @model
   * @generated
   */
	int getPublicationIssue();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationIssue <em>Publication Issue</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Issue</em>' attribute.
   * @see #getPublicationIssue()
   * @generated
   */
	void setPublicationIssue(int value);

	/**
   * Returns the value of the '<em><b>Publication Status</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Status</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Status</em>' attribute.
   * @see #setPublicationStatus(String)
   * @see metadata.MetadataPackage#getReference_PublicationStatus()
   * @model
   * @generated
   */
	String getPublicationStatus();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationStatus <em>Publication Status</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Status</em>' attribute.
   * @see #getPublicationStatus()
   * @generated
   */
	void setPublicationStatus(String value);

	/**
   * Returns the value of the '<em><b>Publication Website</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Publication Website</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Publication Website</em>' attribute.
   * @see #setPublicationWebsite(String)
   * @see metadata.MetadataPackage#getReference_PublicationWebsite()
   * @model
   * @generated
   */
	String getPublicationWebsite();

	/**
   * Sets the value of the '{@link metadata.Reference#getPublicationWebsite <em>Publication Website</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Publication Website</em>' attribute.
   * @see #getPublicationWebsite()
   * @generated
   */
	void setPublicationWebsite(String value);

	/**
   * Returns the value of the '<em><b>Comment</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Comment</em>' attribute.
   * @see #setComment(String)
   * @see metadata.MetadataPackage#getReference_Comment()
   * @model
   * @generated
   */
	String getComment();

	/**
   * Sets the value of the '{@link metadata.Reference#getComment <em>Comment</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Comment</em>' attribute.
   * @see #getComment()
   * @generated
   */
	void setComment(String value);

} // Reference
