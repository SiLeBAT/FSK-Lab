/**
 */
package metadata;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Hazard</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.Hazard#getHazardType <em>Hazard Type</em>}</li>
 *   <li>{@link metadata.Hazard#getHazardName <em>Hazard Name</em>}</li>
 *   <li>{@link metadata.Hazard#getHazardDescription <em>Hazard Description</em>}</li>
 *   <li>{@link metadata.Hazard#getHazardUnit <em>Hazard Unit</em>}</li>
 *   <li>{@link metadata.Hazard#getAdverseEffect <em>Adverse Effect</em>}</li>
 *   <li>{@link metadata.Hazard#getSourceOfContamination <em>Source Of Contamination</em>}</li>
 *   <li>{@link metadata.Hazard#getBenchmarkDose <em>Benchmark Dose</em>}</li>
 *   <li>{@link metadata.Hazard#getMaximumResidueLimit <em>Maximum Residue Limit</em>}</li>
 *   <li>{@link metadata.Hazard#getNoObservedAdverseAffectLevel <em>No Observed Adverse Affect Level</em>}</li>
 *   <li>{@link metadata.Hazard#getLowestObservedAdverseAffectLevel <em>Lowest Observed Adverse Affect Level</em>}</li>
 *   <li>{@link metadata.Hazard#getAcceptableOperatorExposureLevel <em>Acceptable Operator Exposure Level</em>}</li>
 *   <li>{@link metadata.Hazard#getAcuteReferenceDose <em>Acute Reference Dose</em>}</li>
 *   <li>{@link metadata.Hazard#getAcceptableDailyIntake <em>Acceptable Daily Intake</em>}</li>
 *   <li>{@link metadata.Hazard#getHazardIndSum <em>Hazard Ind Sum</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getHazard()
 * @model
 * @generated
 */
public interface Hazard extends EObject {
	/**
   * Returns the value of the '<em><b>Hazard Type</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hazard Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Hazard Type</em>' attribute.
   * @see #setHazardType(String)
   * @see metadata.MetadataPackage#getHazard_HazardType()
   * @model
   * @generated
   */
	String getHazardType();

	/**
   * Sets the value of the '{@link metadata.Hazard#getHazardType <em>Hazard Type</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hazard Type</em>' attribute.
   * @see #getHazardType()
   * @generated
   */
	void setHazardType(String value);

	/**
   * Returns the value of the '<em><b>Hazard Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hazard Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Hazard Name</em>' attribute.
   * @see #setHazardName(String)
   * @see metadata.MetadataPackage#getHazard_HazardName()
   * @model required="true"
   * @generated
   */
	String getHazardName();

	/**
   * Sets the value of the '{@link metadata.Hazard#getHazardName <em>Hazard Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hazard Name</em>' attribute.
   * @see #getHazardName()
   * @generated
   */
	void setHazardName(String value);

	/**
   * Returns the value of the '<em><b>Hazard Description</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hazard Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Hazard Description</em>' attribute.
   * @see #setHazardDescription(String)
   * @see metadata.MetadataPackage#getHazard_HazardDescription()
   * @model
   * @generated
   */
	String getHazardDescription();

	/**
   * Sets the value of the '{@link metadata.Hazard#getHazardDescription <em>Hazard Description</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hazard Description</em>' attribute.
   * @see #getHazardDescription()
   * @generated
   */
	void setHazardDescription(String value);

	/**
   * Returns the value of the '<em><b>Hazard Unit</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hazard Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Hazard Unit</em>' attribute.
   * @see #setHazardUnit(String)
   * @see metadata.MetadataPackage#getHazard_HazardUnit()
   * @model
   * @generated
   */
	String getHazardUnit();

	/**
   * Sets the value of the '{@link metadata.Hazard#getHazardUnit <em>Hazard Unit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hazard Unit</em>' attribute.
   * @see #getHazardUnit()
   * @generated
   */
	void setHazardUnit(String value);

	/**
   * Returns the value of the '<em><b>Adverse Effect</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adverse Effect</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Adverse Effect</em>' attribute.
   * @see #setAdverseEffect(String)
   * @see metadata.MetadataPackage#getHazard_AdverseEffect()
   * @model
   * @generated
   */
	String getAdverseEffect();

	/**
   * Sets the value of the '{@link metadata.Hazard#getAdverseEffect <em>Adverse Effect</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Adverse Effect</em>' attribute.
   * @see #getAdverseEffect()
   * @generated
   */
	void setAdverseEffect(String value);

	/**
   * Returns the value of the '<em><b>Source Of Contamination</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Of Contamination</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Source Of Contamination</em>' attribute.
   * @see #setSourceOfContamination(String)
   * @see metadata.MetadataPackage#getHazard_SourceOfContamination()
   * @model
   * @generated
   */
	String getSourceOfContamination();

	/**
   * Sets the value of the '{@link metadata.Hazard#getSourceOfContamination <em>Source Of Contamination</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Source Of Contamination</em>' attribute.
   * @see #getSourceOfContamination()
   * @generated
   */
	void setSourceOfContamination(String value);

	/**
   * Returns the value of the '<em><b>Benchmark Dose</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Benchmark Dose</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Benchmark Dose</em>' attribute.
   * @see #setBenchmarkDose(String)
   * @see metadata.MetadataPackage#getHazard_BenchmarkDose()
   * @model
   * @generated
   */
	String getBenchmarkDose();

	/**
   * Sets the value of the '{@link metadata.Hazard#getBenchmarkDose <em>Benchmark Dose</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Benchmark Dose</em>' attribute.
   * @see #getBenchmarkDose()
   * @generated
   */
	void setBenchmarkDose(String value);

	/**
   * Returns the value of the '<em><b>Maximum Residue Limit</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum Residue Limit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Maximum Residue Limit</em>' attribute.
   * @see #setMaximumResidueLimit(String)
   * @see metadata.MetadataPackage#getHazard_MaximumResidueLimit()
   * @model
   * @generated
   */
	String getMaximumResidueLimit();

	/**
   * Sets the value of the '{@link metadata.Hazard#getMaximumResidueLimit <em>Maximum Residue Limit</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Maximum Residue Limit</em>' attribute.
   * @see #getMaximumResidueLimit()
   * @generated
   */
	void setMaximumResidueLimit(String value);

	/**
   * Returns the value of the '<em><b>No Observed Adverse Affect Level</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>No Observed Adverse Affect Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>No Observed Adverse Affect Level</em>' attribute.
   * @see #setNoObservedAdverseAffectLevel(String)
   * @see metadata.MetadataPackage#getHazard_NoObservedAdverseAffectLevel()
   * @model
   * @generated
   */
	String getNoObservedAdverseAffectLevel();

	/**
   * Sets the value of the '{@link metadata.Hazard#getNoObservedAdverseAffectLevel <em>No Observed Adverse Affect Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>No Observed Adverse Affect Level</em>' attribute.
   * @see #getNoObservedAdverseAffectLevel()
   * @generated
   */
	void setNoObservedAdverseAffectLevel(String value);

	/**
   * Returns the value of the '<em><b>Lowest Observed Adverse Affect Level</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lowest Observed Adverse Affect Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Lowest Observed Adverse Affect Level</em>' attribute.
   * @see #setLowestObservedAdverseAffectLevel(String)
   * @see metadata.MetadataPackage#getHazard_LowestObservedAdverseAffectLevel()
   * @model
   * @generated
   */
	String getLowestObservedAdverseAffectLevel();

	/**
   * Sets the value of the '{@link metadata.Hazard#getLowestObservedAdverseAffectLevel <em>Lowest Observed Adverse Affect Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Lowest Observed Adverse Affect Level</em>' attribute.
   * @see #getLowestObservedAdverseAffectLevel()
   * @generated
   */
	void setLowestObservedAdverseAffectLevel(String value);

	/**
   * Returns the value of the '<em><b>Acceptable Operator Exposure Level</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Acceptable Operator Exposure Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Acceptable Operator Exposure Level</em>' attribute.
   * @see #setAcceptableOperatorExposureLevel(String)
   * @see metadata.MetadataPackage#getHazard_AcceptableOperatorExposureLevel()
   * @model
   * @generated
   */
	String getAcceptableOperatorExposureLevel();

	/**
   * Sets the value of the '{@link metadata.Hazard#getAcceptableOperatorExposureLevel <em>Acceptable Operator Exposure Level</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Acceptable Operator Exposure Level</em>' attribute.
   * @see #getAcceptableOperatorExposureLevel()
   * @generated
   */
	void setAcceptableOperatorExposureLevel(String value);

	/**
   * Returns the value of the '<em><b>Acute Reference Dose</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Acute Reference Dose</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Acute Reference Dose</em>' attribute.
   * @see #setAcuteReferenceDose(String)
   * @see metadata.MetadataPackage#getHazard_AcuteReferenceDose()
   * @model
   * @generated
   */
	String getAcuteReferenceDose();

	/**
   * Sets the value of the '{@link metadata.Hazard#getAcuteReferenceDose <em>Acute Reference Dose</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Acute Reference Dose</em>' attribute.
   * @see #getAcuteReferenceDose()
   * @generated
   */
	void setAcuteReferenceDose(String value);

	/**
   * Returns the value of the '<em><b>Acceptable Daily Intake</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Acceptable Daily Intake</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Acceptable Daily Intake</em>' attribute.
   * @see #setAcceptableDailyIntake(String)
   * @see metadata.MetadataPackage#getHazard_AcceptableDailyIntake()
   * @model
   * @generated
   */
	String getAcceptableDailyIntake();

	/**
   * Sets the value of the '{@link metadata.Hazard#getAcceptableDailyIntake <em>Acceptable Daily Intake</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Acceptable Daily Intake</em>' attribute.
   * @see #getAcceptableDailyIntake()
   * @generated
   */
	void setAcceptableDailyIntake(String value);

	/**
   * Returns the value of the '<em><b>Hazard Ind Sum</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hazard Ind Sum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Hazard Ind Sum</em>' attribute.
   * @see #setHazardIndSum(String)
   * @see metadata.MetadataPackage#getHazard_HazardIndSum()
   * @model
   * @generated
   */
	String getHazardIndSum();

	/**
   * Sets the value of the '{@link metadata.Hazard#getHazardIndSum <em>Hazard Ind Sum</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hazard Ind Sum</em>' attribute.
   * @see #getHazardIndSum()
   * @generated
   */
	void setHazardIndSum(String value);

} // Hazard
