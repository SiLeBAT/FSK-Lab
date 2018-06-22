/**
 */
package metadata;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assay</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Assay#getAssayName <em>Assay Name</em>}</li>
 *   <li>{@link metadata.Assay#getAssayDescription <em>Assay Description</em>}</li>
 *   <li>{@link metadata.Assay#getPercentageOfMoisture <em>Percentage Of Moisture</em>}</li>
 *   <li>{@link metadata.Assay#getPercentageOfFat <em>Percentage Of Fat</em>}</li>
 *   <li>{@link metadata.Assay#getLimitOfDetection <em>Limit Of Detection</em>}</li>
 *   <li>{@link metadata.Assay#getLimitOfQuantification <em>Limit Of Quantification</em>}</li>
 *   <li>{@link metadata.Assay#getLeftCensoredData <em>Left Censored Data</em>}</li>
 *   <li>{@link metadata.Assay#getRangeOfContamination <em>Range Of Contamination</em>}</li>
 *   <li>{@link metadata.Assay#getUncertaintyValue <em>Uncertainty Value</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getAssay()
 * @model
 * @generated
 */
public interface Assay extends EObject {
	/**
   * Returns the value of the '<em><b>Assay Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assay Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Assay Name</em>' attribute.
   * @see #setAssayName(String)
   * @see metadata.MetadataPackage#getAssay_AssayName()
   * @model required="true"
   * @generated
   */
	String getAssayName();

	/**
   * Sets the value of the '{@link metadata.Assay#getAssayName <em>Assay Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Assay Name</em>' attribute.
   * @see #getAssayName()
   * @generated
   */
	void setAssayName(String value);

	/**
   * Returns the value of the '<em><b>Assay Description</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assay Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Assay Description</em>' attribute.
   * @see #setAssayDescription(String)
   * @see metadata.MetadataPackage#getAssay_AssayDescription()
   * @model
   * @generated
   */
	String getAssayDescription();

	/**
   * Sets the value of the '{@link metadata.Assay#getAssayDescription <em>Assay Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Assay Description</em>' attribute.
   * @see #getAssayDescription()
   * @generated
   */
	void setAssayDescription(String value);

	/**
   * Returns the value of the '<em><b>Percentage Of Moisture</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Percentage Of Moisture</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Percentage Of Moisture</em>' attribute.
   * @see #setPercentageOfMoisture(String)
   * @see metadata.MetadataPackage#getAssay_PercentageOfMoisture()
   * @model
   * @generated
   */
	String getPercentageOfMoisture();

	/**
   * Sets the value of the '{@link metadata.Assay#getPercentageOfMoisture <em>Percentage Of Moisture</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Percentage Of Moisture</em>' attribute.
   * @see #getPercentageOfMoisture()
   * @generated
   */
	void setPercentageOfMoisture(String value);

	/**
   * Returns the value of the '<em><b>Percentage Of Fat</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Percentage Of Fat</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Percentage Of Fat</em>' attribute.
   * @see #setPercentageOfFat(String)
   * @see metadata.MetadataPackage#getAssay_PercentageOfFat()
   * @model
   * @generated
   */
	String getPercentageOfFat();

	/**
   * Sets the value of the '{@link metadata.Assay#getPercentageOfFat <em>Percentage Of Fat</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Percentage Of Fat</em>' attribute.
   * @see #getPercentageOfFat()
   * @generated
   */
	void setPercentageOfFat(String value);

	/**
   * Returns the value of the '<em><b>Limit Of Detection</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Limit Of Detection</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Limit Of Detection</em>' attribute.
   * @see #setLimitOfDetection(String)
   * @see metadata.MetadataPackage#getAssay_LimitOfDetection()
   * @model
   * @generated
   */
	String getLimitOfDetection();

	/**
   * Sets the value of the '{@link metadata.Assay#getLimitOfDetection <em>Limit Of Detection</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Limit Of Detection</em>' attribute.
   * @see #getLimitOfDetection()
   * @generated
   */
	void setLimitOfDetection(String value);

	/**
   * Returns the value of the '<em><b>Limit Of Quantification</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Limit Of Quantification</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Limit Of Quantification</em>' attribute.
   * @see #setLimitOfQuantification(String)
   * @see metadata.MetadataPackage#getAssay_LimitOfQuantification()
   * @model
   * @generated
   */
	String getLimitOfQuantification();

	/**
   * Sets the value of the '{@link metadata.Assay#getLimitOfQuantification <em>Limit Of Quantification</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Limit Of Quantification</em>' attribute.
   * @see #getLimitOfQuantification()
   * @generated
   */
	void setLimitOfQuantification(String value);

	/**
   * Returns the value of the '<em><b>Left Censored Data</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Censored Data</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Left Censored Data</em>' attribute.
   * @see #setLeftCensoredData(String)
   * @see metadata.MetadataPackage#getAssay_LeftCensoredData()
   * @model
   * @generated
   */
	String getLeftCensoredData();

	/**
   * Sets the value of the '{@link metadata.Assay#getLeftCensoredData <em>Left Censored Data</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Left Censored Data</em>' attribute.
   * @see #getLeftCensoredData()
   * @generated
   */
	void setLeftCensoredData(String value);

	/**
   * Returns the value of the '<em><b>Range Of Contamination</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Range Of Contamination</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Range Of Contamination</em>' attribute.
   * @see #setRangeOfContamination(String)
   * @see metadata.MetadataPackage#getAssay_RangeOfContamination()
   * @model
   * @generated
   */
	String getRangeOfContamination();

	/**
   * Sets the value of the '{@link metadata.Assay#getRangeOfContamination <em>Range Of Contamination</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Range Of Contamination</em>' attribute.
   * @see #getRangeOfContamination()
   * @generated
   */
	void setRangeOfContamination(String value);

	/**
   * Returns the value of the '<em><b>Uncertainty Value</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uncertainty Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Uncertainty Value</em>' attribute.
   * @see #setUncertaintyValue(String)
   * @see metadata.MetadataPackage#getAssay_UncertaintyValue()
   * @model
   * @generated
   */
	String getUncertaintyValue();

	/**
   * Sets the value of the '{@link metadata.Assay#getUncertaintyValue <em>Uncertainty Value</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Uncertainty Value</em>' attribute.
   * @see #getUncertaintyValue()
   * @generated
   */
	void setUncertaintyValue(String value);

} // Assay
