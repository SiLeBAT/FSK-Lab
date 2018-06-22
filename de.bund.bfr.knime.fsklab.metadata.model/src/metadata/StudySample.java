/**
 */
package metadata;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Study Sample</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.StudySample#getSampleName <em>Sample Name</em>}</li>
 *   <li>{@link metadata.StudySample#getProtocolOfSampleCollection <em>Protocol Of Sample Collection</em>}</li>
 *   <li>{@link metadata.StudySample#getSamplingStrategy <em>Sampling Strategy</em>}</li>
 *   <li>{@link metadata.StudySample#getTypeOfSamplingProgram <em>Type Of Sampling Program</em>}</li>
 *   <li>{@link metadata.StudySample#getSamplingMethod <em>Sampling Method</em>}</li>
 *   <li>{@link metadata.StudySample#getSamplingPlan <em>Sampling Plan</em>}</li>
 *   <li>{@link metadata.StudySample#getSamplingWeight <em>Sampling Weight</em>}</li>
 *   <li>{@link metadata.StudySample#getSamplingSize <em>Sampling Size</em>}</li>
 *   <li>{@link metadata.StudySample#getLotSizeUnit <em>Lot Size Unit</em>}</li>
 *   <li>{@link metadata.StudySample#getSamplingPoint <em>Sampling Point</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getStudySample()
 * @model
 * @generated
 */
public interface StudySample extends EObject {
	/**
   * Returns the value of the '<em><b>Sample Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sample Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Sample Name</em>' attribute.
   * @see #setSampleName(String)
   * @see metadata.MetadataPackage#getStudySample_SampleName()
   * @model required="true"
   * @generated
   */
	String getSampleName();

	/**
   * Sets the value of the '{@link metadata.StudySample#getSampleName <em>Sample Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sample Name</em>' attribute.
   * @see #getSampleName()
   * @generated
   */
	void setSampleName(String value);

	/**
   * Returns the value of the '<em><b>Protocol Of Sample Collection</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Protocol Of Sample Collection</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Protocol Of Sample Collection</em>' attribute.
   * @see #setProtocolOfSampleCollection(String)
   * @see metadata.MetadataPackage#getStudySample_ProtocolOfSampleCollection()
   * @model required="true"
   * @generated
   */
	String getProtocolOfSampleCollection();

	/**
   * Sets the value of the '{@link metadata.StudySample#getProtocolOfSampleCollection <em>Protocol Of Sample Collection</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Protocol Of Sample Collection</em>' attribute.
   * @see #getProtocolOfSampleCollection()
   * @generated
   */
	void setProtocolOfSampleCollection(String value);

	/**
   * Returns the value of the '<em><b>Sampling Strategy</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sampling Strategy</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Sampling Strategy</em>' attribute.
   * @see #setSamplingStrategy(String)
   * @see metadata.MetadataPackage#getStudySample_SamplingStrategy()
   * @model
   * @generated
   */
	String getSamplingStrategy();

	/**
   * Sets the value of the '{@link metadata.StudySample#getSamplingStrategy <em>Sampling Strategy</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sampling Strategy</em>' attribute.
   * @see #getSamplingStrategy()
   * @generated
   */
	void setSamplingStrategy(String value);

	/**
   * Returns the value of the '<em><b>Type Of Sampling Program</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Of Sampling Program</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Type Of Sampling Program</em>' attribute.
   * @see #setTypeOfSamplingProgram(String)
   * @see metadata.MetadataPackage#getStudySample_TypeOfSamplingProgram()
   * @model
   * @generated
   */
	String getTypeOfSamplingProgram();

	/**
   * Sets the value of the '{@link metadata.StudySample#getTypeOfSamplingProgram <em>Type Of Sampling Program</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type Of Sampling Program</em>' attribute.
   * @see #getTypeOfSamplingProgram()
   * @generated
   */
	void setTypeOfSamplingProgram(String value);

	/**
   * Returns the value of the '<em><b>Sampling Method</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sampling Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Sampling Method</em>' attribute.
   * @see #setSamplingMethod(String)
   * @see metadata.MetadataPackage#getStudySample_SamplingMethod()
   * @model
   * @generated
   */
	String getSamplingMethod();

	/**
   * Sets the value of the '{@link metadata.StudySample#getSamplingMethod <em>Sampling Method</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sampling Method</em>' attribute.
   * @see #getSamplingMethod()
   * @generated
   */
	void setSamplingMethod(String value);

	/**
   * Returns the value of the '<em><b>Sampling Plan</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sampling Plan</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Sampling Plan</em>' attribute.
   * @see #setSamplingPlan(String)
   * @see metadata.MetadataPackage#getStudySample_SamplingPlan()
   * @model required="true"
   * @generated
   */
	String getSamplingPlan();

	/**
   * Sets the value of the '{@link metadata.StudySample#getSamplingPlan <em>Sampling Plan</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sampling Plan</em>' attribute.
   * @see #getSamplingPlan()
   * @generated
   */
	void setSamplingPlan(String value);

	/**
   * Returns the value of the '<em><b>Sampling Weight</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sampling Weight</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Sampling Weight</em>' attribute.
   * @see #setSamplingWeight(String)
   * @see metadata.MetadataPackage#getStudySample_SamplingWeight()
   * @model required="true"
   * @generated
   */
	String getSamplingWeight();

	/**
   * Sets the value of the '{@link metadata.StudySample#getSamplingWeight <em>Sampling Weight</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sampling Weight</em>' attribute.
   * @see #getSamplingWeight()
   * @generated
   */
	void setSamplingWeight(String value);

	/**
   * Returns the value of the '<em><b>Sampling Size</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sampling Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Sampling Size</em>' attribute.
   * @see #setSamplingSize(String)
   * @see metadata.MetadataPackage#getStudySample_SamplingSize()
   * @model required="true"
   * @generated
   */
	String getSamplingSize();

	/**
   * Sets the value of the '{@link metadata.StudySample#getSamplingSize <em>Sampling Size</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sampling Size</em>' attribute.
   * @see #getSamplingSize()
   * @generated
   */
	void setSamplingSize(String value);

	/**
   * Returns the value of the '<em><b>Lot Size Unit</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lot Size Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Lot Size Unit</em>' attribute.
   * @see #setLotSizeUnit(String)
   * @see metadata.MetadataPackage#getStudySample_LotSizeUnit()
   * @model
   * @generated
   */
	String getLotSizeUnit();

	/**
   * Sets the value of the '{@link metadata.StudySample#getLotSizeUnit <em>Lot Size Unit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Lot Size Unit</em>' attribute.
   * @see #getLotSizeUnit()
   * @generated
   */
	void setLotSizeUnit(String value);

	/**
   * Returns the value of the '<em><b>Sampling Point</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sampling Point</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Sampling Point</em>' attribute.
   * @see #setSamplingPoint(String)
   * @see metadata.MetadataPackage#getStudySample_SamplingPoint()
   * @model
   * @generated
   */
	String getSamplingPoint();

	/**
   * Sets the value of the '{@link metadata.StudySample#getSamplingPoint <em>Sampling Point</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sampling Point</em>' attribute.
   * @see #getSamplingPoint()
   * @generated
   */
	void setSamplingPoint(String value);

} // StudySample
