/**
 */
package metadata;

import java.net.URI;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Study</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Study#getStudyIdentifier <em>Study Identifier</em>}</li>
 *   <li>{@link metadata.Study#getStudyTitle <em>Study Title</em>}</li>
 *   <li>{@link metadata.Study#getStudyDescription <em>Study Description</em>}</li>
 *   <li>{@link metadata.Study#getStudyDesignType <em>Study Design Type</em>}</li>
 *   <li>{@link metadata.Study#getStudyAssayMeasurementType <em>Study Assay Measurement Type</em>}</li>
 *   <li>{@link metadata.Study#getStudyAssayTechnologyType <em>Study Assay Technology Type</em>}</li>
 *   <li>{@link metadata.Study#getStudyAssayTechnologyPlatform <em>Study Assay Technology Platform</em>}</li>
 *   <li>{@link metadata.Study#getAccreditationProcedureForTheAssayTechnology <em>Accreditation Procedure For The Assay Technology</em>}</li>
 *   <li>{@link metadata.Study#getStudyProtocolName <em>Study Protocol Name</em>}</li>
 *   <li>{@link metadata.Study#getStudyProtocolType <em>Study Protocol Type</em>}</li>
 *   <li>{@link metadata.Study#getStudyProtocolDescription <em>Study Protocol Description</em>}</li>
 *   <li>{@link metadata.Study#getStudyProtocolURI <em>Study Protocol URI</em>}</li>
 *   <li>{@link metadata.Study#getStudyProtocolVersion <em>Study Protocol Version</em>}</li>
 *   <li>{@link metadata.Study#getStudyProtocolComponentsName <em>Study Protocol Components Name</em>}</li>
 *   <li>{@link metadata.Study#getStudyProtocolComponentsType <em>Study Protocol Components Type</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getStudy()
 * @model
 * @generated
 */
public interface Study extends EObject {
	/**
	 * Returns the value of the '<em><b>Study Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Identifier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Identifier</em>' attribute.
	 * @see #setStudyIdentifier(String)
	 * @see metadata.MetadataPackage#getStudy_StudyIdentifier()
	 * @model
	 * @generated
	 */
	String getStudyIdentifier();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyIdentifier <em>Study Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Identifier</em>' attribute.
	 * @see #getStudyIdentifier()
	 * @generated
	 */
	void setStudyIdentifier(String value);

	/**
	 * Returns the value of the '<em><b>Study Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Title</em>' attribute.
	 * @see #setStudyTitle(String)
	 * @see metadata.MetadataPackage#getStudy_StudyTitle()
	 * @model required="true"
	 * @generated
	 */
	String getStudyTitle();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyTitle <em>Study Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Title</em>' attribute.
	 * @see #getStudyTitle()
	 * @generated
	 */
	void setStudyTitle(String value);

	/**
	 * Returns the value of the '<em><b>Study Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Description</em>' attribute.
	 * @see #setStudyDescription(String)
	 * @see metadata.MetadataPackage#getStudy_StudyDescription()
	 * @model
	 * @generated
	 */
	String getStudyDescription();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyDescription <em>Study Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Description</em>' attribute.
	 * @see #getStudyDescription()
	 * @generated
	 */
	void setStudyDescription(String value);

	/**
	 * Returns the value of the '<em><b>Study Design Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Design Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Design Type</em>' attribute.
	 * @see #setStudyDesignType(String)
	 * @see metadata.MetadataPackage#getStudy_StudyDesignType()
	 * @model
	 * @generated
	 */
	String getStudyDesignType();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyDesignType <em>Study Design Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Design Type</em>' attribute.
	 * @see #getStudyDesignType()
	 * @generated
	 */
	void setStudyDesignType(String value);

	/**
	 * Returns the value of the '<em><b>Study Assay Measurement Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Assay Measurement Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Assay Measurement Type</em>' attribute.
	 * @see #setStudyAssayMeasurementType(String)
	 * @see metadata.MetadataPackage#getStudy_StudyAssayMeasurementType()
	 * @model
	 * @generated
	 */
	String getStudyAssayMeasurementType();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyAssayMeasurementType <em>Study Assay Measurement Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Assay Measurement Type</em>' attribute.
	 * @see #getStudyAssayMeasurementType()
	 * @generated
	 */
	void setStudyAssayMeasurementType(String value);

	/**
	 * Returns the value of the '<em><b>Study Assay Technology Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Assay Technology Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Assay Technology Type</em>' attribute.
	 * @see #setStudyAssayTechnologyType(String)
	 * @see metadata.MetadataPackage#getStudy_StudyAssayTechnologyType()
	 * @model
	 * @generated
	 */
	String getStudyAssayTechnologyType();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyAssayTechnologyType <em>Study Assay Technology Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Assay Technology Type</em>' attribute.
	 * @see #getStudyAssayTechnologyType()
	 * @generated
	 */
	void setStudyAssayTechnologyType(String value);

	/**
	 * Returns the value of the '<em><b>Study Assay Technology Platform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Assay Technology Platform</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Assay Technology Platform</em>' attribute.
	 * @see #setStudyAssayTechnologyPlatform(String)
	 * @see metadata.MetadataPackage#getStudy_StudyAssayTechnologyPlatform()
	 * @model
	 * @generated
	 */
	String getStudyAssayTechnologyPlatform();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyAssayTechnologyPlatform <em>Study Assay Technology Platform</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Assay Technology Platform</em>' attribute.
	 * @see #getStudyAssayTechnologyPlatform()
	 * @generated
	 */
	void setStudyAssayTechnologyPlatform(String value);

	/**
	 * Returns the value of the '<em><b>Accreditation Procedure For The Assay Technology</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Accreditation Procedure For The Assay Technology</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accreditation Procedure For The Assay Technology</em>' attribute.
	 * @see #setAccreditationProcedureForTheAssayTechnology(String)
	 * @see metadata.MetadataPackage#getStudy_AccreditationProcedureForTheAssayTechnology()
	 * @model
	 * @generated
	 */
	String getAccreditationProcedureForTheAssayTechnology();

	/**
	 * Sets the value of the '{@link metadata.Study#getAccreditationProcedureForTheAssayTechnology <em>Accreditation Procedure For The Assay Technology</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Accreditation Procedure For The Assay Technology</em>' attribute.
	 * @see #getAccreditationProcedureForTheAssayTechnology()
	 * @generated
	 */
	void setAccreditationProcedureForTheAssayTechnology(String value);

	/**
	 * Returns the value of the '<em><b>Study Protocol Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Protocol Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Protocol Name</em>' attribute.
	 * @see #setStudyProtocolName(String)
	 * @see metadata.MetadataPackage#getStudy_StudyProtocolName()
	 * @model
	 * @generated
	 */
	String getStudyProtocolName();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyProtocolName <em>Study Protocol Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Protocol Name</em>' attribute.
	 * @see #getStudyProtocolName()
	 * @generated
	 */
	void setStudyProtocolName(String value);

	/**
	 * Returns the value of the '<em><b>Study Protocol Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Protocol Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Protocol Type</em>' attribute.
	 * @see #setStudyProtocolType(String)
	 * @see metadata.MetadataPackage#getStudy_StudyProtocolType()
	 * @model
	 * @generated
	 */
	String getStudyProtocolType();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyProtocolType <em>Study Protocol Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Protocol Type</em>' attribute.
	 * @see #getStudyProtocolType()
	 * @generated
	 */
	void setStudyProtocolType(String value);

	/**
	 * Returns the value of the '<em><b>Study Protocol Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Protocol Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Protocol Description</em>' attribute.
	 * @see #setStudyProtocolDescription(String)
	 * @see metadata.MetadataPackage#getStudy_StudyProtocolDescription()
	 * @model
	 * @generated
	 */
	String getStudyProtocolDescription();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyProtocolDescription <em>Study Protocol Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Protocol Description</em>' attribute.
	 * @see #getStudyProtocolDescription()
	 * @generated
	 */
	void setStudyProtocolDescription(String value);

	/**
	 * Returns the value of the '<em><b>Study Protocol URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Protocol URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Protocol URI</em>' attribute.
	 * @see #setStudyProtocolURI(URI)
	 * @see metadata.MetadataPackage#getStudy_StudyProtocolURI()
	 * @model dataType="metadata.URI"
	 * @generated
	 */
	URI getStudyProtocolURI();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyProtocolURI <em>Study Protocol URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Protocol URI</em>' attribute.
	 * @see #getStudyProtocolURI()
	 * @generated
	 */
	void setStudyProtocolURI(URI value);

	/**
	 * Returns the value of the '<em><b>Study Protocol Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Protocol Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Protocol Version</em>' attribute.
	 * @see #setStudyProtocolVersion(String)
	 * @see metadata.MetadataPackage#getStudy_StudyProtocolVersion()
	 * @model
	 * @generated
	 */
	String getStudyProtocolVersion();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyProtocolVersion <em>Study Protocol Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Protocol Version</em>' attribute.
	 * @see #getStudyProtocolVersion()
	 * @generated
	 */
	void setStudyProtocolVersion(String value);

	/**
	 * Returns the value of the '<em><b>Study Protocol Components Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Protocol Components Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Protocol Components Name</em>' attribute.
	 * @see #setStudyProtocolComponentsName(String)
	 * @see metadata.MetadataPackage#getStudy_StudyProtocolComponentsName()
	 * @model
	 * @generated
	 */
	String getStudyProtocolComponentsName();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyProtocolComponentsName <em>Study Protocol Components Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Protocol Components Name</em>' attribute.
	 * @see #getStudyProtocolComponentsName()
	 * @generated
	 */
	void setStudyProtocolComponentsName(String value);

	/**
	 * Returns the value of the '<em><b>Study Protocol Components Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Study Protocol Components Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Study Protocol Components Type</em>' attribute.
	 * @see #setStudyProtocolComponentsType(String)
	 * @see metadata.MetadataPackage#getStudy_StudyProtocolComponentsType()
	 * @model
	 * @generated
	 */
	String getStudyProtocolComponentsType();

	/**
	 * Sets the value of the '{@link metadata.Study#getStudyProtocolComponentsType <em>Study Protocol Components Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Study Protocol Components Type</em>' attribute.
	 * @see #getStudyProtocolComponentsType()
	 * @generated
	 */
	void setStudyProtocolComponentsType(String value);

} // Study
