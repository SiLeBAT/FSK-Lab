/**
 */
package metadata;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Population Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link metadata.PopulationGroup#getPopulationName <em>Population Name</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getTargetPopulation <em>Target Population</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getPopulationSpan <em>Population Span</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getPopulationDescription <em>Population Description</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getPopulationAge <em>Population Age</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getPopulationGender <em>Population Gender</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getBmi <em>Bmi</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getSpecialDietGroups <em>Special Diet Groups</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getPatternConsumption <em>Pattern Consumption</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getRegion <em>Region</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getCountry <em>Country</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getPopulationRiskFactor <em>Population Risk Factor</em>}</li>
 *   <li>{@link metadata.PopulationGroup#getSeason <em>Season</em>}</li>
 * </ul>
 *
 * @see metadata.MetadataPackage#getPopulationGroup()
 * @model
 * @generated
 */
public interface PopulationGroup extends EObject {
	/**
	 * Returns the value of the '<em><b>Population Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Population Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Population Name</em>' attribute.
	 * @see #setPopulationName(String)
	 * @see metadata.MetadataPackage#getPopulationGroup_PopulationName()
	 * @model required="true"
	 * @generated
	 */
	String getPopulationName();

	/**
	 * Sets the value of the '{@link metadata.PopulationGroup#getPopulationName <em>Population Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Population Name</em>' attribute.
	 * @see #getPopulationName()
	 * @generated
	 */
	void setPopulationName(String value);

	/**
	 * Returns the value of the '<em><b>Target Population</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Population</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Population</em>' attribute.
	 * @see #setTargetPopulation(String)
	 * @see metadata.MetadataPackage#getPopulationGroup_TargetPopulation()
	 * @model
	 * @generated
	 */
	String getTargetPopulation();

	/**
	 * Sets the value of the '{@link metadata.PopulationGroup#getTargetPopulation <em>Target Population</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Population</em>' attribute.
	 * @see #getTargetPopulation()
	 * @generated
	 */
	void setTargetPopulation(String value);

	/**
	 * Returns the value of the '<em><b>Population Span</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Population Span</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Population Span</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_PopulationSpan()
	 * @model
	 * @generated
	 */
	EList<String> getPopulationSpan();

	/**
	 * Returns the value of the '<em><b>Population Description</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Population Description</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Population Description</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_PopulationDescription()
	 * @model
	 * @generated
	 */
	EList<String> getPopulationDescription();

	/**
	 * Returns the value of the '<em><b>Population Age</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Population Age</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Population Age</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_PopulationAge()
	 * @model
	 * @generated
	 */
	EList<String> getPopulationAge();

	/**
	 * Returns the value of the '<em><b>Population Gender</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Population Gender</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Population Gender</em>' attribute.
	 * @see #setPopulationGender(String)
	 * @see metadata.MetadataPackage#getPopulationGroup_PopulationGender()
	 * @model
	 * @generated
	 */
	String getPopulationGender();

	/**
	 * Sets the value of the '{@link metadata.PopulationGroup#getPopulationGender <em>Population Gender</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Population Gender</em>' attribute.
	 * @see #getPopulationGender()
	 * @generated
	 */
	void setPopulationGender(String value);

	/**
	 * Returns the value of the '<em><b>Bmi</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bmi</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bmi</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_Bmi()
	 * @model
	 * @generated
	 */
	EList<String> getBmi();

	/**
	 * Returns the value of the '<em><b>Special Diet Groups</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Special Diet Groups</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Special Diet Groups</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_SpecialDietGroups()
	 * @model
	 * @generated
	 */
	EList<String> getSpecialDietGroups();

	/**
	 * Returns the value of the '<em><b>Pattern Consumption</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pattern Consumption</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pattern Consumption</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_PatternConsumption()
	 * @model
	 * @generated
	 */
	EList<String> getPatternConsumption();

	/**
	 * Returns the value of the '<em><b>Region</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Region</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Region</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_Region()
	 * @model
	 * @generated
	 */
	EList<String> getRegion();

	/**
	 * Returns the value of the '<em><b>Country</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Country</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Country</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_Country()
	 * @model
	 * @generated
	 */
	EList<String> getCountry();

	/**
	 * Returns the value of the '<em><b>Population Risk Factor</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Population Risk Factor</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Population Risk Factor</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_PopulationRiskFactor()
	 * @model
	 * @generated
	 */
	EList<String> getPopulationRiskFactor();

	/**
	 * Returns the value of the '<em><b>Season</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Season</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Season</em>' attribute list.
	 * @see metadata.MetadataPackage#getPopulationGroup_Season()
	 * @model
	 * @generated
	 */
	EList<String> getSeason();

} // PopulationGroup
