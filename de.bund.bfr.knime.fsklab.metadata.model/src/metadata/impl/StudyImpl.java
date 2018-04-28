/**
 */
package metadata.impl;

import java.net.URI;

import metadata.MetadataPackage;
import metadata.Study;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Study</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link metadata.impl.StudyImpl#getStudyIdentifier <em>Study Identifier</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyTitle <em>Study Title</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyDescription <em>Study Description</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyDesignType <em>Study Design Type</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyAssayMeasurementType <em>Study Assay Measurement Type</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyAssayTechnologyType <em>Study Assay Technology Type</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyAssayTechnologyPlatform <em>Study Assay Technology Platform</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getAccreditationProcedureForTheAssayTechnology <em>Accreditation Procedure For The Assay Technology</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyProtocolName <em>Study Protocol Name</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyProtocolType <em>Study Protocol Type</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyProtocolDescription <em>Study Protocol Description</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyProtocolURI <em>Study Protocol URI</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyProtocolVersion <em>Study Protocol Version</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyProtocolComponentsName <em>Study Protocol Components Name</em>}</li>
 *   <li>{@link metadata.impl.StudyImpl#getStudyProtocolComponentsType <em>Study Protocol Components Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StudyImpl extends MinimalEObjectImpl.Container implements Study {
	/**
	 * The default value of the '{@link #getStudyIdentifier() <em>Study Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyIdentifier()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_IDENTIFIER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyIdentifier() <em>Study Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyIdentifier()
	 * @generated
	 * @ordered
	 */
	protected String studyIdentifier = STUDY_IDENTIFIER_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyTitle() <em>Study Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyTitle() <em>Study Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyTitle()
	 * @generated
	 * @ordered
	 */
	protected String studyTitle = STUDY_TITLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyDescription() <em>Study Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyDescription() <em>Study Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyDescription()
	 * @generated
	 * @ordered
	 */
	protected String studyDescription = STUDY_DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyDesignType() <em>Study Design Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyDesignType()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_DESIGN_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyDesignType() <em>Study Design Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyDesignType()
	 * @generated
	 * @ordered
	 */
	protected String studyDesignType = STUDY_DESIGN_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyAssayMeasurementType() <em>Study Assay Measurement Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyAssayMeasurementType()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_ASSAY_MEASUREMENT_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyAssayMeasurementType() <em>Study Assay Measurement Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyAssayMeasurementType()
	 * @generated
	 * @ordered
	 */
	protected String studyAssayMeasurementType = STUDY_ASSAY_MEASUREMENT_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyAssayTechnologyType() <em>Study Assay Technology Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyAssayTechnologyType()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_ASSAY_TECHNOLOGY_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyAssayTechnologyType() <em>Study Assay Technology Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyAssayTechnologyType()
	 * @generated
	 * @ordered
	 */
	protected String studyAssayTechnologyType = STUDY_ASSAY_TECHNOLOGY_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyAssayTechnologyPlatform() <em>Study Assay Technology Platform</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyAssayTechnologyPlatform()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_ASSAY_TECHNOLOGY_PLATFORM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyAssayTechnologyPlatform() <em>Study Assay Technology Platform</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyAssayTechnologyPlatform()
	 * @generated
	 * @ordered
	 */
	protected String studyAssayTechnologyPlatform = STUDY_ASSAY_TECHNOLOGY_PLATFORM_EDEFAULT;

	/**
	 * The default value of the '{@link #getAccreditationProcedureForTheAssayTechnology() <em>Accreditation Procedure For The Assay Technology</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccreditationProcedureForTheAssayTechnology()
	 * @generated
	 * @ordered
	 */
	protected static final String ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAccreditationProcedureForTheAssayTechnology() <em>Accreditation Procedure For The Assay Technology</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccreditationProcedureForTheAssayTechnology()
	 * @generated
	 * @ordered
	 */
	protected String accreditationProcedureForTheAssayTechnology = ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyProtocolName() <em>Study Protocol Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolName()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_PROTOCOL_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyProtocolName() <em>Study Protocol Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolName()
	 * @generated
	 * @ordered
	 */
	protected String studyProtocolName = STUDY_PROTOCOL_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyProtocolType() <em>Study Protocol Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolType()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_PROTOCOL_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyProtocolType() <em>Study Protocol Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolType()
	 * @generated
	 * @ordered
	 */
	protected String studyProtocolType = STUDY_PROTOCOL_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyProtocolDescription() <em>Study Protocol Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_PROTOCOL_DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyProtocolDescription() <em>Study Protocol Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolDescription()
	 * @generated
	 * @ordered
	 */
	protected String studyProtocolDescription = STUDY_PROTOCOL_DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyProtocolURI() <em>Study Protocol URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolURI()
	 * @generated
	 * @ordered
	 */
	protected static final URI STUDY_PROTOCOL_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyProtocolURI() <em>Study Protocol URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolURI()
	 * @generated
	 * @ordered
	 */
	protected URI studyProtocolURI = STUDY_PROTOCOL_URI_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyProtocolVersion() <em>Study Protocol Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_PROTOCOL_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyProtocolVersion() <em>Study Protocol Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolVersion()
	 * @generated
	 * @ordered
	 */
	protected String studyProtocolVersion = STUDY_PROTOCOL_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyProtocolComponentsName() <em>Study Protocol Components Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolComponentsName()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_PROTOCOL_COMPONENTS_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyProtocolComponentsName() <em>Study Protocol Components Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolComponentsName()
	 * @generated
	 * @ordered
	 */
	protected String studyProtocolComponentsName = STUDY_PROTOCOL_COMPONENTS_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getStudyProtocolComponentsType() <em>Study Protocol Components Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolComponentsType()
	 * @generated
	 * @ordered
	 */
	protected static final String STUDY_PROTOCOL_COMPONENTS_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStudyProtocolComponentsType() <em>Study Protocol Components Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStudyProtocolComponentsType()
	 * @generated
	 * @ordered
	 */
	protected String studyProtocolComponentsType = STUDY_PROTOCOL_COMPONENTS_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StudyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetadataPackage.Literals.STUDY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyIdentifier() {
		return studyIdentifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyIdentifier(String newStudyIdentifier) {
		String oldStudyIdentifier = studyIdentifier;
		studyIdentifier = newStudyIdentifier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_IDENTIFIER, oldStudyIdentifier, studyIdentifier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyTitle() {
		return studyTitle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyTitle(String newStudyTitle) {
		String oldStudyTitle = studyTitle;
		studyTitle = newStudyTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_TITLE, oldStudyTitle, studyTitle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyDescription() {
		return studyDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyDescription(String newStudyDescription) {
		String oldStudyDescription = studyDescription;
		studyDescription = newStudyDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_DESCRIPTION, oldStudyDescription, studyDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyDesignType() {
		return studyDesignType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyDesignType(String newStudyDesignType) {
		String oldStudyDesignType = studyDesignType;
		studyDesignType = newStudyDesignType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_DESIGN_TYPE, oldStudyDesignType, studyDesignType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyAssayMeasurementType() {
		return studyAssayMeasurementType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyAssayMeasurementType(String newStudyAssayMeasurementType) {
		String oldStudyAssayMeasurementType = studyAssayMeasurementType;
		studyAssayMeasurementType = newStudyAssayMeasurementType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_ASSAY_MEASUREMENT_TYPE, oldStudyAssayMeasurementType, studyAssayMeasurementType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyAssayTechnologyType() {
		return studyAssayTechnologyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyAssayTechnologyType(String newStudyAssayTechnologyType) {
		String oldStudyAssayTechnologyType = studyAssayTechnologyType;
		studyAssayTechnologyType = newStudyAssayTechnologyType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE, oldStudyAssayTechnologyType, studyAssayTechnologyType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyAssayTechnologyPlatform() {
		return studyAssayTechnologyPlatform;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyAssayTechnologyPlatform(String newStudyAssayTechnologyPlatform) {
		String oldStudyAssayTechnologyPlatform = studyAssayTechnologyPlatform;
		studyAssayTechnologyPlatform = newStudyAssayTechnologyPlatform;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM, oldStudyAssayTechnologyPlatform, studyAssayTechnologyPlatform));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAccreditationProcedureForTheAssayTechnology() {
		return accreditationProcedureForTheAssayTechnology;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAccreditationProcedureForTheAssayTechnology(String newAccreditationProcedureForTheAssayTechnology) {
		String oldAccreditationProcedureForTheAssayTechnology = accreditationProcedureForTheAssayTechnology;
		accreditationProcedureForTheAssayTechnology = newAccreditationProcedureForTheAssayTechnology;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY, oldAccreditationProcedureForTheAssayTechnology, accreditationProcedureForTheAssayTechnology));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyProtocolName() {
		return studyProtocolName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyProtocolName(String newStudyProtocolName) {
		String oldStudyProtocolName = studyProtocolName;
		studyProtocolName = newStudyProtocolName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_PROTOCOL_NAME, oldStudyProtocolName, studyProtocolName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyProtocolType() {
		return studyProtocolType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyProtocolType(String newStudyProtocolType) {
		String oldStudyProtocolType = studyProtocolType;
		studyProtocolType = newStudyProtocolType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_PROTOCOL_TYPE, oldStudyProtocolType, studyProtocolType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyProtocolDescription() {
		return studyProtocolDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyProtocolDescription(String newStudyProtocolDescription) {
		String oldStudyProtocolDescription = studyProtocolDescription;
		studyProtocolDescription = newStudyProtocolDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_PROTOCOL_DESCRIPTION, oldStudyProtocolDescription, studyProtocolDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public URI getStudyProtocolURI() {
		return studyProtocolURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyProtocolURI(URI newStudyProtocolURI) {
		URI oldStudyProtocolURI = studyProtocolURI;
		studyProtocolURI = newStudyProtocolURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_PROTOCOL_URI, oldStudyProtocolURI, studyProtocolURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyProtocolVersion() {
		return studyProtocolVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyProtocolVersion(String newStudyProtocolVersion) {
		String oldStudyProtocolVersion = studyProtocolVersion;
		studyProtocolVersion = newStudyProtocolVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_PROTOCOL_VERSION, oldStudyProtocolVersion, studyProtocolVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyProtocolComponentsName() {
		return studyProtocolComponentsName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyProtocolComponentsName(String newStudyProtocolComponentsName) {
		String oldStudyProtocolComponentsName = studyProtocolComponentsName;
		studyProtocolComponentsName = newStudyProtocolComponentsName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_NAME, oldStudyProtocolComponentsName, studyProtocolComponentsName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStudyProtocolComponentsType() {
		return studyProtocolComponentsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStudyProtocolComponentsType(String newStudyProtocolComponentsType) {
		String oldStudyProtocolComponentsType = studyProtocolComponentsType;
		studyProtocolComponentsType = newStudyProtocolComponentsType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE, oldStudyProtocolComponentsType, studyProtocolComponentsType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetadataPackage.STUDY__STUDY_IDENTIFIER:
				return getStudyIdentifier();
			case MetadataPackage.STUDY__STUDY_TITLE:
				return getStudyTitle();
			case MetadataPackage.STUDY__STUDY_DESCRIPTION:
				return getStudyDescription();
			case MetadataPackage.STUDY__STUDY_DESIGN_TYPE:
				return getStudyDesignType();
			case MetadataPackage.STUDY__STUDY_ASSAY_MEASUREMENT_TYPE:
				return getStudyAssayMeasurementType();
			case MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE:
				return getStudyAssayTechnologyType();
			case MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM:
				return getStudyAssayTechnologyPlatform();
			case MetadataPackage.STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY:
				return getAccreditationProcedureForTheAssayTechnology();
			case MetadataPackage.STUDY__STUDY_PROTOCOL_NAME:
				return getStudyProtocolName();
			case MetadataPackage.STUDY__STUDY_PROTOCOL_TYPE:
				return getStudyProtocolType();
			case MetadataPackage.STUDY__STUDY_PROTOCOL_DESCRIPTION:
				return getStudyProtocolDescription();
			case MetadataPackage.STUDY__STUDY_PROTOCOL_URI:
				return getStudyProtocolURI();
			case MetadataPackage.STUDY__STUDY_PROTOCOL_VERSION:
				return getStudyProtocolVersion();
			case MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_NAME:
				return getStudyProtocolComponentsName();
			case MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE:
				return getStudyProtocolComponentsType();
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
			case MetadataPackage.STUDY__STUDY_IDENTIFIER:
				setStudyIdentifier((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_TITLE:
				setStudyTitle((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_DESCRIPTION:
				setStudyDescription((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_DESIGN_TYPE:
				setStudyDesignType((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_ASSAY_MEASUREMENT_TYPE:
				setStudyAssayMeasurementType((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE:
				setStudyAssayTechnologyType((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM:
				setStudyAssayTechnologyPlatform((String)newValue);
				return;
			case MetadataPackage.STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY:
				setAccreditationProcedureForTheAssayTechnology((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_NAME:
				setStudyProtocolName((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_TYPE:
				setStudyProtocolType((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_DESCRIPTION:
				setStudyProtocolDescription((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_URI:
				setStudyProtocolURI((URI)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_VERSION:
				setStudyProtocolVersion((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_NAME:
				setStudyProtocolComponentsName((String)newValue);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE:
				setStudyProtocolComponentsType((String)newValue);
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
			case MetadataPackage.STUDY__STUDY_IDENTIFIER:
				setStudyIdentifier(STUDY_IDENTIFIER_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_TITLE:
				setStudyTitle(STUDY_TITLE_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_DESCRIPTION:
				setStudyDescription(STUDY_DESCRIPTION_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_DESIGN_TYPE:
				setStudyDesignType(STUDY_DESIGN_TYPE_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_ASSAY_MEASUREMENT_TYPE:
				setStudyAssayMeasurementType(STUDY_ASSAY_MEASUREMENT_TYPE_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE:
				setStudyAssayTechnologyType(STUDY_ASSAY_TECHNOLOGY_TYPE_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM:
				setStudyAssayTechnologyPlatform(STUDY_ASSAY_TECHNOLOGY_PLATFORM_EDEFAULT);
				return;
			case MetadataPackage.STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY:
				setAccreditationProcedureForTheAssayTechnology(ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_NAME:
				setStudyProtocolName(STUDY_PROTOCOL_NAME_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_TYPE:
				setStudyProtocolType(STUDY_PROTOCOL_TYPE_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_DESCRIPTION:
				setStudyProtocolDescription(STUDY_PROTOCOL_DESCRIPTION_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_URI:
				setStudyProtocolURI(STUDY_PROTOCOL_URI_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_VERSION:
				setStudyProtocolVersion(STUDY_PROTOCOL_VERSION_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_NAME:
				setStudyProtocolComponentsName(STUDY_PROTOCOL_COMPONENTS_NAME_EDEFAULT);
				return;
			case MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE:
				setStudyProtocolComponentsType(STUDY_PROTOCOL_COMPONENTS_TYPE_EDEFAULT);
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
			case MetadataPackage.STUDY__STUDY_IDENTIFIER:
				return STUDY_IDENTIFIER_EDEFAULT == null ? studyIdentifier != null : !STUDY_IDENTIFIER_EDEFAULT.equals(studyIdentifier);
			case MetadataPackage.STUDY__STUDY_TITLE:
				return STUDY_TITLE_EDEFAULT == null ? studyTitle != null : !STUDY_TITLE_EDEFAULT.equals(studyTitle);
			case MetadataPackage.STUDY__STUDY_DESCRIPTION:
				return STUDY_DESCRIPTION_EDEFAULT == null ? studyDescription != null : !STUDY_DESCRIPTION_EDEFAULT.equals(studyDescription);
			case MetadataPackage.STUDY__STUDY_DESIGN_TYPE:
				return STUDY_DESIGN_TYPE_EDEFAULT == null ? studyDesignType != null : !STUDY_DESIGN_TYPE_EDEFAULT.equals(studyDesignType);
			case MetadataPackage.STUDY__STUDY_ASSAY_MEASUREMENT_TYPE:
				return STUDY_ASSAY_MEASUREMENT_TYPE_EDEFAULT == null ? studyAssayMeasurementType != null : !STUDY_ASSAY_MEASUREMENT_TYPE_EDEFAULT.equals(studyAssayMeasurementType);
			case MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_TYPE:
				return STUDY_ASSAY_TECHNOLOGY_TYPE_EDEFAULT == null ? studyAssayTechnologyType != null : !STUDY_ASSAY_TECHNOLOGY_TYPE_EDEFAULT.equals(studyAssayTechnologyType);
			case MetadataPackage.STUDY__STUDY_ASSAY_TECHNOLOGY_PLATFORM:
				return STUDY_ASSAY_TECHNOLOGY_PLATFORM_EDEFAULT == null ? studyAssayTechnologyPlatform != null : !STUDY_ASSAY_TECHNOLOGY_PLATFORM_EDEFAULT.equals(studyAssayTechnologyPlatform);
			case MetadataPackage.STUDY__ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY:
				return ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY_EDEFAULT == null ? accreditationProcedureForTheAssayTechnology != null : !ACCREDITATION_PROCEDURE_FOR_THE_ASSAY_TECHNOLOGY_EDEFAULT.equals(accreditationProcedureForTheAssayTechnology);
			case MetadataPackage.STUDY__STUDY_PROTOCOL_NAME:
				return STUDY_PROTOCOL_NAME_EDEFAULT == null ? studyProtocolName != null : !STUDY_PROTOCOL_NAME_EDEFAULT.equals(studyProtocolName);
			case MetadataPackage.STUDY__STUDY_PROTOCOL_TYPE:
				return STUDY_PROTOCOL_TYPE_EDEFAULT == null ? studyProtocolType != null : !STUDY_PROTOCOL_TYPE_EDEFAULT.equals(studyProtocolType);
			case MetadataPackage.STUDY__STUDY_PROTOCOL_DESCRIPTION:
				return STUDY_PROTOCOL_DESCRIPTION_EDEFAULT == null ? studyProtocolDescription != null : !STUDY_PROTOCOL_DESCRIPTION_EDEFAULT.equals(studyProtocolDescription);
			case MetadataPackage.STUDY__STUDY_PROTOCOL_URI:
				return STUDY_PROTOCOL_URI_EDEFAULT == null ? studyProtocolURI != null : !STUDY_PROTOCOL_URI_EDEFAULT.equals(studyProtocolURI);
			case MetadataPackage.STUDY__STUDY_PROTOCOL_VERSION:
				return STUDY_PROTOCOL_VERSION_EDEFAULT == null ? studyProtocolVersion != null : !STUDY_PROTOCOL_VERSION_EDEFAULT.equals(studyProtocolVersion);
			case MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_NAME:
				return STUDY_PROTOCOL_COMPONENTS_NAME_EDEFAULT == null ? studyProtocolComponentsName != null : !STUDY_PROTOCOL_COMPONENTS_NAME_EDEFAULT.equals(studyProtocolComponentsName);
			case MetadataPackage.STUDY__STUDY_PROTOCOL_COMPONENTS_TYPE:
				return STUDY_PROTOCOL_COMPONENTS_TYPE_EDEFAULT == null ? studyProtocolComponentsType != null : !STUDY_PROTOCOL_COMPONENTS_TYPE_EDEFAULT.equals(studyProtocolComponentsType);
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
		result.append(" (studyIdentifier: ");
		result.append(studyIdentifier);
		result.append(", studyTitle: ");
		result.append(studyTitle);
		result.append(", studyDescription: ");
		result.append(studyDescription);
		result.append(", studyDesignType: ");
		result.append(studyDesignType);
		result.append(", studyAssayMeasurementType: ");
		result.append(studyAssayMeasurementType);
		result.append(", studyAssayTechnologyType: ");
		result.append(studyAssayTechnologyType);
		result.append(", studyAssayTechnologyPlatform: ");
		result.append(studyAssayTechnologyPlatform);
		result.append(", accreditationProcedureForTheAssayTechnology: ");
		result.append(accreditationProcedureForTheAssayTechnology);
		result.append(", studyProtocolName: ");
		result.append(studyProtocolName);
		result.append(", studyProtocolType: ");
		result.append(studyProtocolType);
		result.append(", studyProtocolDescription: ");
		result.append(studyProtocolDescription);
		result.append(", studyProtocolURI: ");
		result.append(studyProtocolURI);
		result.append(", studyProtocolVersion: ");
		result.append(studyProtocolVersion);
		result.append(", studyProtocolComponentsName: ");
		result.append(studyProtocolComponentsName);
		result.append(", studyProtocolComponentsType: ");
		result.append(studyProtocolComponentsType);
		result.append(')');
		return result.toString();
	}

} //StudyImpl
