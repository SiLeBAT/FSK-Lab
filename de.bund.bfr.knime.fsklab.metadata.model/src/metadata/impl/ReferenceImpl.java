/**
 */
package metadata.impl;

import java.util.Date;

import metadata.MetadataPackage;
import metadata.PublicationType;
import metadata.Reference;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.ReferenceImpl#isIsReferenceDescription <em>Is Reference Description</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationType <em>Publication Type</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationDate <em>Publication Date</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPmid <em>Pmid</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getDoi <em>Doi</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getAuthorList <em>Author List</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationTitle <em>Publication Title</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationAbstract <em>Publication Abstract</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationJournal <em>Publication Journal</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationVolume <em>Publication Volume</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationIssue <em>Publication Issue</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationStatus <em>Publication Status</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getPublicationWebsite <em>Publication Website</em>}</li>
 *   <li>{@link metadata.impl.ReferenceImpl#getComment <em>Comment</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReferenceImpl extends MinimalEObjectImpl.Container implements Reference {
	/**
	 * The default value of the '{@link #isIsReferenceDescription() <em>Is Reference Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsReferenceDescription()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_REFERENCE_DESCRIPTION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsReferenceDescription() <em>Is Reference Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsReferenceDescription()
	 * @generated
	 * @ordered
	 */
	protected boolean isReferenceDescription = IS_REFERENCE_DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationType() <em>Publication Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationType()
	 * @generated
	 * @ordered
	 */
	protected static final PublicationType PUBLICATION_TYPE_EDEFAULT = PublicationType.ABST;

	/**
	 * The cached value of the '{@link #getPublicationType() <em>Publication Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationType()
	 * @generated
	 * @ordered
	 */
	protected PublicationType publicationType = PUBLICATION_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationDate() <em>Publication Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date PUBLICATION_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublicationDate() <em>Publication Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationDate()
	 * @generated
	 * @ordered
	 */
	protected Date publicationDate = PUBLICATION_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPmid() <em>Pmid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPmid()
	 * @generated
	 * @ordered
	 */
	protected static final String PMID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPmid() <em>Pmid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPmid()
	 * @generated
	 * @ordered
	 */
	protected String pmid = PMID_EDEFAULT;

	/**
	 * The default value of the '{@link #getDoi() <em>Doi</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDoi()
	 * @generated
	 * @ordered
	 */
	protected static final String DOI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDoi() <em>Doi</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDoi()
	 * @generated
	 * @ordered
	 */
	protected String doi = DOI_EDEFAULT;

	/**
	 * The default value of the '{@link #getAuthorList() <em>Author List</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthorList()
	 * @generated
	 * @ordered
	 */
	protected static final String AUTHOR_LIST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAuthorList() <em>Author List</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthorList()
	 * @generated
	 * @ordered
	 */
	protected String authorList = AUTHOR_LIST_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationTitle() <em>Publication Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String PUBLICATION_TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublicationTitle() <em>Publication Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationTitle()
	 * @generated
	 * @ordered
	 */
	protected String publicationTitle = PUBLICATION_TITLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationAbstract() <em>Publication Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final String PUBLICATION_ABSTRACT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublicationAbstract() <em>Publication Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationAbstract()
	 * @generated
	 * @ordered
	 */
	protected String publicationAbstract = PUBLICATION_ABSTRACT_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationJournal() <em>Publication Journal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationJournal()
	 * @generated
	 * @ordered
	 */
	protected static final String PUBLICATION_JOURNAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublicationJournal() <em>Publication Journal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationJournal()
	 * @generated
	 * @ordered
	 */
	protected String publicationJournal = PUBLICATION_JOURNAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationVolume() <em>Publication Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int PUBLICATION_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPublicationVolume() <em>Publication Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationVolume()
	 * @generated
	 * @ordered
	 */
	protected int publicationVolume = PUBLICATION_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationIssue() <em>Publication Issue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationIssue()
	 * @generated
	 * @ordered
	 */
	protected static final int PUBLICATION_ISSUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPublicationIssue() <em>Publication Issue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationIssue()
	 * @generated
	 * @ordered
	 */
	protected int publicationIssue = PUBLICATION_ISSUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationStatus() <em>Publication Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationStatus()
	 * @generated
	 * @ordered
	 */
	protected static final String PUBLICATION_STATUS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublicationStatus() <em>Publication Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationStatus()
	 * @generated
	 * @ordered
	 */
	protected String publicationStatus = PUBLICATION_STATUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublicationWebsite() <em>Publication Website</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationWebsite()
	 * @generated
	 * @ordered
	 */
	protected static final String PUBLICATION_WEBSITE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublicationWebsite() <em>Publication Website</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublicationWebsite()
	 * @generated
	 * @ordered
	 */
	protected String publicationWebsite = PUBLICATION_WEBSITE_EDEFAULT;

	/**
	 * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected String comment = COMMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsReferenceDescription() {
		return isReferenceDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsReferenceDescription(boolean newIsReferenceDescription) {
		boolean oldIsReferenceDescription = isReferenceDescription;
		isReferenceDescription = newIsReferenceDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__IS_REFERENCE_DESCRIPTION, oldIsReferenceDescription, isReferenceDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PublicationType getPublicationType() {
		return publicationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationType(PublicationType newPublicationType) {
		PublicationType oldPublicationType = publicationType;
		publicationType = newPublicationType == null ? PUBLICATION_TYPE_EDEFAULT : newPublicationType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_TYPE, oldPublicationType, publicationType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getPublicationDate() {
		return publicationDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationDate(Date newPublicationDate) {
		Date oldPublicationDate = publicationDate;
		publicationDate = newPublicationDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_DATE, oldPublicationDate, publicationDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPmid() {
		return pmid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPmid(String newPmid) {
		String oldPmid = pmid;
		pmid = newPmid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PMID, oldPmid, pmid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDoi() {
		return doi;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDoi(String newDoi) {
		String oldDoi = doi;
		doi = newDoi;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__DOI, oldDoi, doi));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAuthorList() {
		return authorList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAuthorList(String newAuthorList) {
		String oldAuthorList = authorList;
		authorList = newAuthorList;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__AUTHOR_LIST, oldAuthorList, authorList));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPublicationTitle() {
		return publicationTitle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationTitle(String newPublicationTitle) {
		String oldPublicationTitle = publicationTitle;
		publicationTitle = newPublicationTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_TITLE, oldPublicationTitle, publicationTitle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPublicationAbstract() {
		return publicationAbstract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationAbstract(String newPublicationAbstract) {
		String oldPublicationAbstract = publicationAbstract;
		publicationAbstract = newPublicationAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_ABSTRACT, oldPublicationAbstract, publicationAbstract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPublicationJournal() {
		return publicationJournal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationJournal(String newPublicationJournal) {
		String oldPublicationJournal = publicationJournal;
		publicationJournal = newPublicationJournal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_JOURNAL, oldPublicationJournal, publicationJournal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPublicationVolume() {
		return publicationVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationVolume(int newPublicationVolume) {
		int oldPublicationVolume = publicationVolume;
		publicationVolume = newPublicationVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_VOLUME, oldPublicationVolume, publicationVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPublicationIssue() {
		return publicationIssue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationIssue(int newPublicationIssue) {
		int oldPublicationIssue = publicationIssue;
		publicationIssue = newPublicationIssue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_ISSUE, oldPublicationIssue, publicationIssue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPublicationStatus() {
		return publicationStatus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationStatus(String newPublicationStatus) {
		String oldPublicationStatus = publicationStatus;
		publicationStatus = newPublicationStatus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_STATUS, oldPublicationStatus, publicationStatus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPublicationWebsite() {
		return publicationWebsite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPublicationWebsite(String newPublicationWebsite) {
		String oldPublicationWebsite = publicationWebsite;
		publicationWebsite = newPublicationWebsite;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__PUBLICATION_WEBSITE, oldPublicationWebsite, publicationWebsite));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComment(String newComment) {
		String oldComment = comment;
		comment = newComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.REFERENCE__COMMENT, oldComment, comment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.REFERENCE__IS_REFERENCE_DESCRIPTION:
				return isIsReferenceDescription();
			case MetadataPackage.REFERENCE__PUBLICATION_TYPE:
				return getPublicationType();
			case MetadataPackage.REFERENCE__PUBLICATION_DATE:
				return getPublicationDate();
			case MetadataPackage.REFERENCE__PMID:
				return getPmid();
			case MetadataPackage.REFERENCE__DOI:
				return getDoi();
			case MetadataPackage.REFERENCE__AUTHOR_LIST:
				return getAuthorList();
			case MetadataPackage.REFERENCE__PUBLICATION_TITLE:
				return getPublicationTitle();
			case MetadataPackage.REFERENCE__PUBLICATION_ABSTRACT:
				return getPublicationAbstract();
			case MetadataPackage.REFERENCE__PUBLICATION_JOURNAL:
				return getPublicationJournal();
			case MetadataPackage.REFERENCE__PUBLICATION_VOLUME:
				return getPublicationVolume();
			case MetadataPackage.REFERENCE__PUBLICATION_ISSUE:
				return getPublicationIssue();
			case MetadataPackage.REFERENCE__PUBLICATION_STATUS:
				return getPublicationStatus();
			case MetadataPackage.REFERENCE__PUBLICATION_WEBSITE:
				return getPublicationWebsite();
			case MetadataPackage.REFERENCE__COMMENT:
				return getComment();
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
			case MetadataPackage.REFERENCE__IS_REFERENCE_DESCRIPTION:
				setIsReferenceDescription((Boolean)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_TYPE:
				setPublicationType((PublicationType)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_DATE:
				setPublicationDate((Date)newValue);
				return;
			case MetadataPackage.REFERENCE__PMID:
				setPmid((String)newValue);
				return;
			case MetadataPackage.REFERENCE__DOI:
				setDoi((String)newValue);
				return;
			case MetadataPackage.REFERENCE__AUTHOR_LIST:
				setAuthorList((String)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_TITLE:
				setPublicationTitle((String)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_ABSTRACT:
				setPublicationAbstract((String)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_JOURNAL:
				setPublicationJournal((String)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_VOLUME:
				setPublicationVolume((Integer)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_ISSUE:
				setPublicationIssue((Integer)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_STATUS:
				setPublicationStatus((String)newValue);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_WEBSITE:
				setPublicationWebsite((String)newValue);
				return;
			case MetadataPackage.REFERENCE__COMMENT:
				setComment((String)newValue);
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
			case MetadataPackage.REFERENCE__IS_REFERENCE_DESCRIPTION:
				setIsReferenceDescription(IS_REFERENCE_DESCRIPTION_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_TYPE:
				setPublicationType(PUBLICATION_TYPE_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_DATE:
				setPublicationDate(PUBLICATION_DATE_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PMID:
				setPmid(PMID_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__DOI:
				setDoi(DOI_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__AUTHOR_LIST:
				setAuthorList(AUTHOR_LIST_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_TITLE:
				setPublicationTitle(PUBLICATION_TITLE_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_ABSTRACT:
				setPublicationAbstract(PUBLICATION_ABSTRACT_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_JOURNAL:
				setPublicationJournal(PUBLICATION_JOURNAL_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_VOLUME:
				setPublicationVolume(PUBLICATION_VOLUME_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_ISSUE:
				setPublicationIssue(PUBLICATION_ISSUE_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_STATUS:
				setPublicationStatus(PUBLICATION_STATUS_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__PUBLICATION_WEBSITE:
				setPublicationWebsite(PUBLICATION_WEBSITE_EDEFAULT);
				return;
			case MetadataPackage.REFERENCE__COMMENT:
				setComment(COMMENT_EDEFAULT);
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
			case MetadataPackage.REFERENCE__IS_REFERENCE_DESCRIPTION:
				return isReferenceDescription != IS_REFERENCE_DESCRIPTION_EDEFAULT;
			case MetadataPackage.REFERENCE__PUBLICATION_TYPE:
				return publicationType != PUBLICATION_TYPE_EDEFAULT;
			case MetadataPackage.REFERENCE__PUBLICATION_DATE:
				return PUBLICATION_DATE_EDEFAULT == null ? publicationDate != null : !PUBLICATION_DATE_EDEFAULT.equals(publicationDate);
			case MetadataPackage.REFERENCE__PMID:
				return PMID_EDEFAULT == null ? pmid != null : !PMID_EDEFAULT.equals(pmid);
			case MetadataPackage.REFERENCE__DOI:
				return DOI_EDEFAULT == null ? doi != null : !DOI_EDEFAULT.equals(doi);
			case MetadataPackage.REFERENCE__AUTHOR_LIST:
				return AUTHOR_LIST_EDEFAULT == null ? authorList != null : !AUTHOR_LIST_EDEFAULT.equals(authorList);
			case MetadataPackage.REFERENCE__PUBLICATION_TITLE:
				return PUBLICATION_TITLE_EDEFAULT == null ? publicationTitle != null : !PUBLICATION_TITLE_EDEFAULT.equals(publicationTitle);
			case MetadataPackage.REFERENCE__PUBLICATION_ABSTRACT:
				return PUBLICATION_ABSTRACT_EDEFAULT == null ? publicationAbstract != null : !PUBLICATION_ABSTRACT_EDEFAULT.equals(publicationAbstract);
			case MetadataPackage.REFERENCE__PUBLICATION_JOURNAL:
				return PUBLICATION_JOURNAL_EDEFAULT == null ? publicationJournal != null : !PUBLICATION_JOURNAL_EDEFAULT.equals(publicationJournal);
			case MetadataPackage.REFERENCE__PUBLICATION_VOLUME:
				return publicationVolume != PUBLICATION_VOLUME_EDEFAULT;
			case MetadataPackage.REFERENCE__PUBLICATION_ISSUE:
				return publicationIssue != PUBLICATION_ISSUE_EDEFAULT;
			case MetadataPackage.REFERENCE__PUBLICATION_STATUS:
				return PUBLICATION_STATUS_EDEFAULT == null ? publicationStatus != null : !PUBLICATION_STATUS_EDEFAULT.equals(publicationStatus);
			case MetadataPackage.REFERENCE__PUBLICATION_WEBSITE:
				return PUBLICATION_WEBSITE_EDEFAULT == null ? publicationWebsite != null : !PUBLICATION_WEBSITE_EDEFAULT.equals(publicationWebsite);
			case MetadataPackage.REFERENCE__COMMENT:
				return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
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
		result.append(" (isReferenceDescription: ");
		result.append(isReferenceDescription);
		result.append(", publicationType: ");
		result.append(publicationType);
		result.append(", publicationDate: ");
		result.append(publicationDate);
		result.append(", pmid: ");
		result.append(pmid);
		result.append(", doi: ");
		result.append(doi);
		result.append(", authorList: ");
		result.append(authorList);
		result.append(", publicationTitle: ");
		result.append(publicationTitle);
		result.append(", publicationAbstract: ");
		result.append(publicationAbstract);
		result.append(", publicationJournal: ");
		result.append(publicationJournal);
		result.append(", publicationVolume: ");
		result.append(publicationVolume);
		result.append(", publicationIssue: ");
		result.append(publicationIssue);
		result.append(", publicationStatus: ");
		result.append(publicationStatus);
		result.append(", publicationWebsite: ");
		result.append(publicationWebsite);
		result.append(", comment: ");
		result.append(comment);
		result.append(')');
		return result.toString();
	}

} //ReferenceImpl
