/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Math</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.ModelMath#getQualityMeasures <em>Quality Measures</em>}</li>
 *   <li>{@link metadata.ModelMath#getFittingProcedure <em>Fitting Procedure</em>}</li>
 *   <li>{@link metadata.ModelMath#getEvent <em>Event</em>}</li>
 *   <li>{@link metadata.ModelMath#getParameter <em>Parameter</em>}</li>
 *   <li>{@link metadata.ModelMath#getModelEquation <em>Model Equation</em>}</li>
 *   <li>{@link metadata.ModelMath#getExposure <em>Exposure</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getModelMath()
 * @model
 * @generated
 */
public interface ModelMath extends EObject {
	/**
	 * Returns the value of the '<em><b>Quality Measures</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quality Measures</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quality Measures</em>' attribute list.
	 * @see metadata.MetadataPackage#getModelMath_QualityMeasures()
	 * @model
	 * @generated
	 */
	EList<String> getQualityMeasures();

	/**
	 * Returns the value of the '<em><b>Fitting Procedure</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fitting Procedure</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fitting Procedure</em>' attribute.
	 * @see #setFittingProcedure(String)
	 * @see metadata.MetadataPackage#getModelMath_FittingProcedure()
	 * @model
	 * @generated
	 */
	String getFittingProcedure();

	/**
	 * Sets the value of the '{@link metadata.ModelMath#getFittingProcedure <em>Fitting Procedure</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fitting Procedure</em>' attribute.
	 * @see #getFittingProcedure()
	 * @generated
	 */
	void setFittingProcedure(String value);

	/**
	 * Returns the value of the '<em><b>Event</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event</em>' attribute list.
	 * @see metadata.MetadataPackage#getModelMath_Event()
	 * @model
	 * @generated
	 */
	EList<String> getEvent();

	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' containment reference list.
	 * @see metadata.MetadataPackage#getModelMath_Parameter()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<Parameter> getParameter();

	/**
	 * Returns the value of the '<em><b>Model Equation</b></em>' containment reference list.
	 * The list contents are of type {@link metadata.ModelEquation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Equation</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Equation</em>' containment reference list.
	 * @see metadata.MetadataPackage#getModelMath_ModelEquation()
	 * @model containment="true"
	 * @generated
	 */
	EList<ModelEquation> getModelEquation();

	/**
	 * Returns the value of the '<em><b>Exposure</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exposure</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exposure</em>' containment reference.
	 * @see #setExposure(Exposure)
	 * @see metadata.MetadataPackage#getModelMath_Exposure()
	 * @model containment="true"
	 * @generated
	 */
	Exposure getExposure();

	/**
	 * Sets the value of the '{@link metadata.ModelMath#getExposure <em>Exposure</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exposure</em>' containment reference.
	 * @see #getExposure()
	 * @generated
	 */
	void setExposure(Exposure value);

} // ModelMath
