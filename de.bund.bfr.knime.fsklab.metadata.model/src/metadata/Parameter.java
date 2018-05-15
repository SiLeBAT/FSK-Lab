/**
 */
package metadata;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Parameter#getParameterID <em>Parameter ID</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterClassification <em>Parameter Classification</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterName <em>Parameter Name</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterDescription <em>Parameter Description</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterType <em>Parameter Type</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterUnit <em>Parameter Unit</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterUnitCategory <em>Parameter Unit Category</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterDataType <em>Parameter Data Type</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterSource <em>Parameter Source</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterSubject <em>Parameter Subject</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterDistribution <em>Parameter Distribution</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterValue <em>Parameter Value</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterVariabilitySubject <em>Parameter Variability Subject</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterValueMin <em>Parameter Value Min</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterValueMax <em>Parameter Value Max</em>}</li>
 *   <li>{@link metadata.Parameter#getParameterError <em>Parameter Error</em>}</li>
 *   <li>{@link metadata.Parameter#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getParameter()
 * @model
 * @generated
 */
public interface Parameter extends EObject {
	/**
	 * Returns the value of the '<em><b>Parameter ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter ID</em>' attribute.
	 * @see #setParameterID(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterID()
	 * @model required="true"
	 * @generated
	 */
	String getParameterID();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterID <em>Parameter ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter ID</em>' attribute.
	 * @see #getParameterID()
	 * @generated
	 */
	void setParameterID(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Classification</b></em>' attribute.
	 * The literals are from the enumeration {@link metadata.ParameterClassification}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Classification</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Classification</em>' attribute.
	 * @see metadata.ParameterClassification
	 * @see #setParameterClassification(ParameterClassification)
	 * @see metadata.MetadataPackage#getParameter_ParameterClassification()
	 * @model required="true"
	 * @generated
	 */
	ParameterClassification getParameterClassification();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterClassification <em>Parameter Classification</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Classification</em>' attribute.
	 * @see metadata.ParameterClassification
	 * @see #getParameterClassification()
	 * @generated
	 */
	void setParameterClassification(ParameterClassification value);

	/**
	 * Returns the value of the '<em><b>Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Name</em>' attribute.
	 * @see #setParameterName(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterName()
	 * @model required="true"
	 * @generated
	 */
	String getParameterName();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterName <em>Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Name</em>' attribute.
	 * @see #getParameterName()
	 * @generated
	 */
	void setParameterName(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Description</em>' attribute.
	 * @see #setParameterDescription(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterDescription()
	 * @model
	 * @generated
	 */
	String getParameterDescription();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterDescription <em>Parameter Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Description</em>' attribute.
	 * @see #getParameterDescription()
	 * @generated
	 */
	void setParameterDescription(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Type</em>' attribute.
	 * @see #setParameterType(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterType()
	 * @model
	 * @generated
	 */
	String getParameterType();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterType <em>Parameter Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Type</em>' attribute.
	 * @see #getParameterType()
	 * @generated
	 */
	void setParameterType(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Unit</em>' attribute.
	 * @see #setParameterUnit(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterUnit()
	 * @model required="true"
	 * @generated
	 */
	String getParameterUnit();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterUnit <em>Parameter Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Unit</em>' attribute.
	 * @see #getParameterUnit()
	 * @generated
	 */
	void setParameterUnit(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Unit Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Unit Category</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Unit Category</em>' attribute.
	 * @see #setParameterUnitCategory(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterUnitCategory()
	 * @model
	 * @generated
	 */
	String getParameterUnitCategory();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterUnitCategory <em>Parameter Unit Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Unit Category</em>' attribute.
	 * @see #getParameterUnitCategory()
	 * @generated
	 */
	void setParameterUnitCategory(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Data Type</b></em>' attribute.
	 * The literals are from the enumeration {@link metadata.ParameterType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Data Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Data Type</em>' attribute.
	 * @see metadata.ParameterType
	 * @see #setParameterDataType(ParameterType)
	 * @see metadata.MetadataPackage#getParameter_ParameterDataType()
	 * @model required="true"
	 * @generated
	 */
	ParameterType getParameterDataType();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterDataType <em>Parameter Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Data Type</em>' attribute.
	 * @see metadata.ParameterType
	 * @see #getParameterDataType()
	 * @generated
	 */
	void setParameterDataType(ParameterType value);

	/**
	 * Returns the value of the '<em><b>Parameter Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Source</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Source</em>' attribute.
	 * @see #setParameterSource(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterSource()
	 * @model
	 * @generated
	 */
	String getParameterSource();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterSource <em>Parameter Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Source</em>' attribute.
	 * @see #getParameterSource()
	 * @generated
	 */
	void setParameterSource(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Subject</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Subject</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Subject</em>' attribute.
	 * @see #setParameterSubject(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterSubject()
	 * @model
	 * @generated
	 */
	String getParameterSubject();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterSubject <em>Parameter Subject</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Subject</em>' attribute.
	 * @see #getParameterSubject()
	 * @generated
	 */
	void setParameterSubject(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Distribution</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Distribution</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Distribution</em>' attribute.
	 * @see #setParameterDistribution(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterDistribution()
	 * @model
	 * @generated
	 */
	String getParameterDistribution();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterDistribution <em>Parameter Distribution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Distribution</em>' attribute.
	 * @see #getParameterDistribution()
	 * @generated
	 */
	void setParameterDistribution(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Value</em>' attribute.
	 * @see #setParameterValue(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterValue()
	 * @model
	 * @generated
	 */
	String getParameterValue();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterValue <em>Parameter Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Value</em>' attribute.
	 * @see #getParameterValue()
	 * @generated
	 */
	void setParameterValue(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Variability Subject</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Variability Subject</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Variability Subject</em>' attribute.
	 * @see #setParameterVariabilitySubject(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterVariabilitySubject()
	 * @model
	 * @generated
	 */
	String getParameterVariabilitySubject();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterVariabilitySubject <em>Parameter Variability Subject</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Variability Subject</em>' attribute.
	 * @see #getParameterVariabilitySubject()
	 * @generated
	 */
	void setParameterVariabilitySubject(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Value Min</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Value Min</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Value Min</em>' attribute.
	 * @see #setParameterValueMin(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterValueMin()
	 * @model
	 * @generated
	 */
	String getParameterValueMin();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterValueMin <em>Parameter Value Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Value Min</em>' attribute.
	 * @see #getParameterValueMin()
	 * @generated
	 */
	void setParameterValueMin(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Value Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Value Max</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Value Max</em>' attribute.
	 * @see #setParameterValueMax(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterValueMax()
	 * @model
	 * @generated
	 */
	String getParameterValueMax();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterValueMax <em>Parameter Value Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Value Max</em>' attribute.
	 * @see #getParameterValueMax()
	 * @generated
	 */
	void setParameterValueMax(String value);

	/**
	 * Returns the value of the '<em><b>Parameter Error</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Error</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Error</em>' attribute.
	 * @see #setParameterError(String)
	 * @see metadata.MetadataPackage#getParameter_ParameterError()
	 * @model
	 * @generated
	 */
	String getParameterError();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getParameterError <em>Parameter Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Error</em>' attribute.
	 * @see #getParameterError()
	 * @generated
	 */
	void setParameterError(String value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' reference.
	 * @see #setReference(Reference)
	 * @see metadata.MetadataPackage#getParameter_Reference()
	 * @model
	 * @generated
	 */
	Reference getReference();

	/**
	 * Sets the value of the '{@link metadata.Parameter#getReference <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(Reference value);

} // Parameter
