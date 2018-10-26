/**
 */
package metadata.impl;

import java.util.Collection;
import java.util.Date;

import metadata.Contact;
import metadata.GeneralInformation;
import metadata.MetadataPackage;
import metadata.ModelCategory;
import metadata.ModificationDate;
import metadata.Reference;

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
 * An implementation of the model object '<em><b>General Information</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getName <em>Name</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getSource <em>Source</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getCreationDate <em>Creation Date</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getRights <em>Rights</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#isAvailable <em>Available</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getSoftware <em>Software</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getLanguageWrittenIn <em>Language Written In</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getObjective <em>Objective</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getModelCategory <em>Model Category</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getModificationdate <em>Modificationdate</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getAuthor <em>Author</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getCreators <em>Creators</em>}</li>
 *   <li>{@link metadata.impl.GeneralInformationImpl#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeneralInformationImpl extends MinimalEObjectImpl.Container implements GeneralInformation {
	/**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
	protected static final String NAME_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
	protected String name = NAME_EDEFAULT;

	/**
   * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getSource()
   * @generated
   * @ordered
   */
	protected static final String SOURCE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getSource()
   * @generated
   * @ordered
   */
	protected String source = SOURCE_EDEFAULT;

	/**
   * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getIdentifier()
   * @generated
   * @ordered
   */
	protected static final String IDENTIFIER_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getIdentifier()
   * @generated
   * @ordered
   */
	protected String identifier = IDENTIFIER_EDEFAULT;

	/**
   * The default value of the '{@link #getCreationDate() <em>Creation Date</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getCreationDate()
   * @generated
   * @ordered
   */
	protected static final Date CREATION_DATE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getCreationDate() <em>Creation Date</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getCreationDate()
   * @generated
   * @ordered
   */
	protected Date creationDate = CREATION_DATE_EDEFAULT;

	/**
   * The default value of the '{@link #getRights() <em>Rights</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getRights()
   * @generated
   * @ordered
   */
	protected static final String RIGHTS_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getRights() <em>Rights</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getRights()
   * @generated
   * @ordered
   */
	protected String rights = RIGHTS_EDEFAULT;

	/**
   * The default value of the '{@link #isAvailable() <em>Available</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #isAvailable()
   * @generated
   * @ordered
   */
	protected static final boolean AVAILABLE_EDEFAULT = false;

	/**
   * The cached value of the '{@link #isAvailable() <em>Available</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #isAvailable()
   * @generated
   * @ordered
   */
	protected boolean available = AVAILABLE_EDEFAULT;

	/**
   * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getFormat()
   * @generated
   * @ordered
   */
	protected static final String FORMAT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getFormat()
   * @generated
   * @ordered
   */
	protected String format = FORMAT_EDEFAULT;

	/**
   * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getLanguage()
   * @generated
   * @ordered
   */
	protected static final String LANGUAGE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getLanguage()
   * @generated
   * @ordered
   */
	protected String language = LANGUAGE_EDEFAULT;

	/**
   * The default value of the '{@link #getSoftware() <em>Software</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getSoftware()
   * @generated
   * @ordered
   */
	protected static final String SOFTWARE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getSoftware() <em>Software</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getSoftware()
   * @generated
   * @ordered
   */
	protected String software = SOFTWARE_EDEFAULT;

	/**
   * The default value of the '{@link #getLanguageWrittenIn() <em>Language Written In</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getLanguageWrittenIn()
   * @generated
   * @ordered
   */
	protected static final String LANGUAGE_WRITTEN_IN_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getLanguageWrittenIn() <em>Language Written In</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getLanguageWrittenIn()
   * @generated
   * @ordered
   */
	protected String languageWrittenIn = LANGUAGE_WRITTEN_IN_EDEFAULT;

	/**
   * The default value of the '{@link #getStatus() <em>Status</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getStatus()
   * @generated
   * @ordered
   */
	protected static final String STATUS_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getStatus() <em>Status</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getStatus()
   * @generated
   * @ordered
   */
	protected String status = STATUS_EDEFAULT;

	/**
   * The default value of the '{@link #getObjective() <em>Objective</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getObjective()
   * @generated
   * @ordered
   */
	protected static final String OBJECTIVE_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getObjective() <em>Objective</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getObjective()
   * @generated
   * @ordered
   */
	protected String objective = OBJECTIVE_EDEFAULT;

	/**
   * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
   * The cached value of the '{@link #getModelCategory() <em>Model Category</em>}' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getModelCategory()
   * @generated
   * @ordered
   */
	protected EList<ModelCategory> modelCategory;

	/**
   * The cached value of the '{@link #getModificationdate() <em>Modificationdate</em>}' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getModificationdate()
   * @generated
   * @ordered
   */
	protected EList<ModificationDate> modificationdate;

	/**
   * The cached value of the '{@link #getAuthor() <em>Author</em>}' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getAuthor()
   * @generated
   * @ordered
   */
	protected Contact author;

	/**
   * The cached value of the '{@link #getCreators() <em>Creators</em>}' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getCreators()
   * @generated
   * @ordered
   */
	protected EList<Contact> creators;

	/**
   * The cached value of the '{@link #getReference() <em>Reference</em>}' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getReference()
   * @generated
   * @ordered
   */
	protected EList<Reference> reference;

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected GeneralInformationImpl() {
    super();
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return MetadataPackage.Literals.GENERAL_INFORMATION;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getName() {
    return name;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setName(String newName) {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__NAME, oldName, name));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getSource() {
    return source;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setSource(String newSource) {
    String oldSource = source;
    source = newSource;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__SOURCE, oldSource, source));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getIdentifier() {
    return identifier;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setIdentifier(String newIdentifier) {
    String oldIdentifier = identifier;
    identifier = newIdentifier;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__IDENTIFIER, oldIdentifier, identifier));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Date getCreationDate() {
    return creationDate;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setCreationDate(Date newCreationDate) {
    Date oldCreationDate = creationDate;
    creationDate = newCreationDate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__CREATION_DATE, oldCreationDate, creationDate));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getRights() {
    return rights;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setRights(String newRights) {
    String oldRights = rights;
    rights = newRights;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__RIGHTS, oldRights, rights));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public boolean isAvailable() {
    return available;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setAvailable(boolean newAvailable) {
    boolean oldAvailable = available;
    available = newAvailable;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__AVAILABLE, oldAvailable, available));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getFormat() {
    return format;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setFormat(String newFormat) {
    String oldFormat = format;
    format = newFormat;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__FORMAT, oldFormat, format));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getLanguage() {
    return language;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setLanguage(String newLanguage) {
    String oldLanguage = language;
    language = newLanguage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__LANGUAGE, oldLanguage, language));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getSoftware() {
    return software;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setSoftware(String newSoftware) {
    String oldSoftware = software;
    software = newSoftware;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__SOFTWARE, oldSoftware, software));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getLanguageWrittenIn() {
    return languageWrittenIn;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setLanguageWrittenIn(String newLanguageWrittenIn) {
    String oldLanguageWrittenIn = languageWrittenIn;
    languageWrittenIn = newLanguageWrittenIn;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN, oldLanguageWrittenIn, languageWrittenIn));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getStatus() {
    return status;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setStatus(String newStatus) {
    String oldStatus = status;
    status = newStatus;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__STATUS, oldStatus, status));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getObjective() {
    return objective;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setObjective(String newObjective) {
    String oldObjective = objective;
    objective = newObjective;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__OBJECTIVE, oldObjective, objective));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getDescription() {
    return description;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setDescription(String newDescription) {
    String oldDescription = description;
    description = newDescription;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__DESCRIPTION, oldDescription, description));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<ModelCategory> getModelCategory() {
    if (modelCategory == null) {
      modelCategory = new EObjectContainmentEList<ModelCategory>(ModelCategory.class, this, MetadataPackage.GENERAL_INFORMATION__MODEL_CATEGORY);
    }
    return modelCategory;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<ModificationDate> getModificationdate() {
    if (modificationdate == null) {
      modificationdate = new EObjectContainmentEList<ModificationDate>(ModificationDate.class, this, MetadataPackage.GENERAL_INFORMATION__MODIFICATIONDATE);
    }
    return modificationdate;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Contact getAuthor() {
    return author;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public NotificationChain basicSetAuthor(Contact newAuthor, NotificationChain msgs) {
    Contact oldAuthor = author;
    author = newAuthor;
    if (eNotificationRequired()) {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__AUTHOR, oldAuthor, newAuthor);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setAuthor(Contact newAuthor) {
    if (newAuthor != author) {
      NotificationChain msgs = null;
      if (author != null)
        msgs = ((InternalEObject)author).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.GENERAL_INFORMATION__AUTHOR, null, msgs);
      if (newAuthor != null)
        msgs = ((InternalEObject)newAuthor).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MetadataPackage.GENERAL_INFORMATION__AUTHOR, null, msgs);
      msgs = basicSetAuthor(newAuthor, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.GENERAL_INFORMATION__AUTHOR, newAuthor, newAuthor));
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<Contact> getCreators() {
    if (creators == null) {
      creators = new EObjectContainmentEList<Contact>(Contact.class, this, MetadataPackage.GENERAL_INFORMATION__CREATORS);
    }
    return creators;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public EList<Reference> getReference() {
    if (reference == null) {
      reference = new EObjectContainmentEList<Reference>(Reference.class, this, MetadataPackage.GENERAL_INFORMATION__REFERENCE);
    }
    return reference;
  }

	/**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
    switch (featureID) {
      case MetadataPackage.GENERAL_INFORMATION__MODEL_CATEGORY:
        return ((InternalEList<?>)getModelCategory()).basicRemove(otherEnd, msgs);
      case MetadataPackage.GENERAL_INFORMATION__MODIFICATIONDATE:
        return ((InternalEList<?>)getModificationdate()).basicRemove(otherEnd, msgs);
      case MetadataPackage.GENERAL_INFORMATION__AUTHOR:
        return basicSetAuthor(null, msgs);
      case MetadataPackage.GENERAL_INFORMATION__CREATORS:
        return ((InternalEList<?>)getCreators()).basicRemove(otherEnd, msgs);
      case MetadataPackage.GENERAL_INFORMATION__REFERENCE:
        return ((InternalEList<?>)getReference()).basicRemove(otherEnd, msgs);
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
      case MetadataPackage.GENERAL_INFORMATION__NAME:
        return getName();
      case MetadataPackage.GENERAL_INFORMATION__SOURCE:
        return getSource();
      case MetadataPackage.GENERAL_INFORMATION__IDENTIFIER:
        return getIdentifier();
      case MetadataPackage.GENERAL_INFORMATION__CREATION_DATE:
        return getCreationDate();
      case MetadataPackage.GENERAL_INFORMATION__RIGHTS:
        return getRights();
      case MetadataPackage.GENERAL_INFORMATION__AVAILABLE:
        return isAvailable();
      case MetadataPackage.GENERAL_INFORMATION__FORMAT:
        return getFormat();
      case MetadataPackage.GENERAL_INFORMATION__LANGUAGE:
        return getLanguage();
      case MetadataPackage.GENERAL_INFORMATION__SOFTWARE:
        return getSoftware();
      case MetadataPackage.GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN:
        return getLanguageWrittenIn();
      case MetadataPackage.GENERAL_INFORMATION__STATUS:
        return getStatus();
      case MetadataPackage.GENERAL_INFORMATION__OBJECTIVE:
        return getObjective();
      case MetadataPackage.GENERAL_INFORMATION__DESCRIPTION:
        return getDescription();
      case MetadataPackage.GENERAL_INFORMATION__MODEL_CATEGORY:
        return getModelCategory();
      case MetadataPackage.GENERAL_INFORMATION__MODIFICATIONDATE:
        return getModificationdate();
      case MetadataPackage.GENERAL_INFORMATION__AUTHOR:
        return getAuthor();
      case MetadataPackage.GENERAL_INFORMATION__CREATORS:
        return getCreators();
      case MetadataPackage.GENERAL_INFORMATION__REFERENCE:
        return getReference();
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
      case MetadataPackage.GENERAL_INFORMATION__NAME:
        setName((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__SOURCE:
        setSource((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__IDENTIFIER:
        setIdentifier((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__CREATION_DATE:
        setCreationDate((Date)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__RIGHTS:
        setRights((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__AVAILABLE:
        setAvailable((Boolean)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__FORMAT:
        setFormat((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__LANGUAGE:
        setLanguage((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__SOFTWARE:
        setSoftware((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN:
        setLanguageWrittenIn((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__STATUS:
        setStatus((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__OBJECTIVE:
        setObjective((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__DESCRIPTION:
        setDescription((String)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__MODEL_CATEGORY:
        getModelCategory().clear();
        getModelCategory().addAll((Collection<? extends ModelCategory>)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__MODIFICATIONDATE:
        getModificationdate().clear();
        getModificationdate().addAll((Collection<? extends ModificationDate>)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__AUTHOR:
        setAuthor((Contact)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__CREATORS:
        getCreators().clear();
        getCreators().addAll((Collection<? extends Contact>)newValue);
        return;
      case MetadataPackage.GENERAL_INFORMATION__REFERENCE:
        getReference().clear();
        getReference().addAll((Collection<? extends Reference>)newValue);
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
      case MetadataPackage.GENERAL_INFORMATION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__SOURCE:
        setSource(SOURCE_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__IDENTIFIER:
        setIdentifier(IDENTIFIER_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__CREATION_DATE:
        setCreationDate(CREATION_DATE_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__RIGHTS:
        setRights(RIGHTS_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__AVAILABLE:
        setAvailable(AVAILABLE_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__FORMAT:
        setFormat(FORMAT_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__LANGUAGE:
        setLanguage(LANGUAGE_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__SOFTWARE:
        setSoftware(SOFTWARE_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN:
        setLanguageWrittenIn(LANGUAGE_WRITTEN_IN_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__STATUS:
        setStatus(STATUS_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__OBJECTIVE:
        setObjective(OBJECTIVE_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__DESCRIPTION:
        setDescription(DESCRIPTION_EDEFAULT);
        return;
      case MetadataPackage.GENERAL_INFORMATION__MODEL_CATEGORY:
        getModelCategory().clear();
        return;
      case MetadataPackage.GENERAL_INFORMATION__MODIFICATIONDATE:
        getModificationdate().clear();
        return;
      case MetadataPackage.GENERAL_INFORMATION__AUTHOR:
        setAuthor((Contact)null);
        return;
      case MetadataPackage.GENERAL_INFORMATION__CREATORS:
        getCreators().clear();
        return;
      case MetadataPackage.GENERAL_INFORMATION__REFERENCE:
        getReference().clear();
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
      case MetadataPackage.GENERAL_INFORMATION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case MetadataPackage.GENERAL_INFORMATION__SOURCE:
        return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
      case MetadataPackage.GENERAL_INFORMATION__IDENTIFIER:
        return IDENTIFIER_EDEFAULT == null ? identifier != null : !IDENTIFIER_EDEFAULT.equals(identifier);
      case MetadataPackage.GENERAL_INFORMATION__CREATION_DATE:
        return CREATION_DATE_EDEFAULT == null ? creationDate != null : !CREATION_DATE_EDEFAULT.equals(creationDate);
      case MetadataPackage.GENERAL_INFORMATION__RIGHTS:
        return RIGHTS_EDEFAULT == null ? rights != null : !RIGHTS_EDEFAULT.equals(rights);
      case MetadataPackage.GENERAL_INFORMATION__AVAILABLE:
        return available != AVAILABLE_EDEFAULT;
      case MetadataPackage.GENERAL_INFORMATION__FORMAT:
        return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
      case MetadataPackage.GENERAL_INFORMATION__LANGUAGE:
        return LANGUAGE_EDEFAULT == null ? language != null : !LANGUAGE_EDEFAULT.equals(language);
      case MetadataPackage.GENERAL_INFORMATION__SOFTWARE:
        return SOFTWARE_EDEFAULT == null ? software != null : !SOFTWARE_EDEFAULT.equals(software);
      case MetadataPackage.GENERAL_INFORMATION__LANGUAGE_WRITTEN_IN:
        return LANGUAGE_WRITTEN_IN_EDEFAULT == null ? languageWrittenIn != null : !LANGUAGE_WRITTEN_IN_EDEFAULT.equals(languageWrittenIn);
      case MetadataPackage.GENERAL_INFORMATION__STATUS:
        return STATUS_EDEFAULT == null ? status != null : !STATUS_EDEFAULT.equals(status);
      case MetadataPackage.GENERAL_INFORMATION__OBJECTIVE:
        return OBJECTIVE_EDEFAULT == null ? objective != null : !OBJECTIVE_EDEFAULT.equals(objective);
      case MetadataPackage.GENERAL_INFORMATION__DESCRIPTION:
        return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
      case MetadataPackage.GENERAL_INFORMATION__MODEL_CATEGORY:
        return modelCategory != null && !modelCategory.isEmpty();
      case MetadataPackage.GENERAL_INFORMATION__MODIFICATIONDATE:
        return modificationdate != null && !modificationdate.isEmpty();
      case MetadataPackage.GENERAL_INFORMATION__AUTHOR:
        return author != null;
      case MetadataPackage.GENERAL_INFORMATION__CREATORS:
        return creators != null && !creators.isEmpty();
      case MetadataPackage.GENERAL_INFORMATION__REFERENCE:
        return reference != null && !reference.isEmpty();
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
    result.append(" (name: ");
    result.append(name);
    result.append(", source: ");
    result.append(source);
    result.append(", identifier: ");
    result.append(identifier);
    result.append(", creationDate: ");
    result.append(creationDate);
    result.append(", rights: ");
    result.append(rights);
    result.append(", available: ");
    result.append(available);
    result.append(", format: ");
    result.append(format);
    result.append(", language: ");
    result.append(language);
    result.append(", software: ");
    result.append(software);
    result.append(", languageWrittenIn: ");
    result.append(languageWrittenIn);
    result.append(", status: ");
    result.append(status);
    result.append(", objective: ");
    result.append(objective);
    result.append(", description: ");
    result.append(description);
    result.append(')');
    return result.toString();
  }

} //GeneralInformationImpl
