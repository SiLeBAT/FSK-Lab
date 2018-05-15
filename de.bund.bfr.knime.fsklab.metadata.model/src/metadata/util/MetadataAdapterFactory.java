/**
 */
package metadata.util;

import metadata.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see metadata.MetadataPackage
 * @generated
 */
public class MetadataAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MetadataPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetadataAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MetadataPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetadataSwitch<Adapter> modelSwitch =
		new MetadataSwitch<Adapter>() {
			@Override
			public Adapter caseGeneralInformation(GeneralInformation object) {
				return createGeneralInformationAdapter();
			}
			@Override
			public Adapter caseModelCategory(ModelCategory object) {
				return createModelCategoryAdapter();
			}
			@Override
			public Adapter caseAssay(Assay object) {
				return createAssayAdapter();
			}
			@Override
			public Adapter caseStudy(Study object) {
				return createStudyAdapter();
			}
			@Override
			public Adapter caseDataBackground(DataBackground object) {
				return createDataBackgroundAdapter();
			}
			@Override
			public Adapter caseStudySample(StudySample object) {
				return createStudySampleAdapter();
			}
			@Override
			public Adapter caseDietaryAssessmentMethod(DietaryAssessmentMethod object) {
				return createDietaryAssessmentMethodAdapter();
			}
			@Override
			public Adapter caseModificationDate(ModificationDate object) {
				return createModificationDateAdapter();
			}
			@Override
			public Adapter caseProduct(Product object) {
				return createProductAdapter();
			}
			@Override
			public Adapter caseHazard(Hazard object) {
				return createHazardAdapter();
			}
			@Override
			public Adapter casePopulationGroup(PopulationGroup object) {
				return createPopulationGroupAdapter();
			}
			@Override
			public Adapter caseScope(Scope object) {
				return createScopeAdapter();
			}
			@Override
			public Adapter caseLaboratory(Laboratory object) {
				return createLaboratoryAdapter();
			}
			@Override
			public Adapter caseSpatialInformation(SpatialInformation object) {
				return createSpatialInformationAdapter();
			}
			@Override
			public Adapter caseEvent(Event object) {
				return createEventAdapter();
			}
			@Override
			public Adapter caseContact(Contact object) {
				return createContactAdapter();
			}
			@Override
			public Adapter caseReference(Reference object) {
				return createReferenceAdapter();
			}
			@Override
			public Adapter caseModelMath(ModelMath object) {
				return createModelMathAdapter();
			}
			@Override
			public Adapter caseParameter(Parameter object) {
				return createParameterAdapter();
			}
			@Override
			public Adapter caseModelEquation(ModelEquation object) {
				return createModelEquationAdapter();
			}
			@Override
			public Adapter caseExposure(Exposure object) {
				return createExposureAdapter();
			}
			@Override
			public Adapter caseStringObject(StringObject object) {
				return createStringObjectAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link metadata.GeneralInformation <em>General Information</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.GeneralInformation
	 * @generated
	 */
	public Adapter createGeneralInformationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.ModelCategory <em>Model Category</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.ModelCategory
	 * @generated
	 */
	public Adapter createModelCategoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Assay <em>Assay</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Assay
	 * @generated
	 */
	public Adapter createAssayAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Study <em>Study</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Study
	 * @generated
	 */
	public Adapter createStudyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.DataBackground <em>Data Background</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.DataBackground
	 * @generated
	 */
	public Adapter createDataBackgroundAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.StudySample <em>Study Sample</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.StudySample
	 * @generated
	 */
	public Adapter createStudySampleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.DietaryAssessmentMethod <em>Dietary Assessment Method</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.DietaryAssessmentMethod
	 * @generated
	 */
	public Adapter createDietaryAssessmentMethodAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.ModificationDate <em>Modification Date</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.ModificationDate
	 * @generated
	 */
	public Adapter createModificationDateAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Product <em>Product</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Product
	 * @generated
	 */
	public Adapter createProductAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Hazard <em>Hazard</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Hazard
	 * @generated
	 */
	public Adapter createHazardAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.PopulationGroup <em>Population Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.PopulationGroup
	 * @generated
	 */
	public Adapter createPopulationGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Scope <em>Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Scope
	 * @generated
	 */
	public Adapter createScopeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Laboratory <em>Laboratory</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Laboratory
	 * @generated
	 */
	public Adapter createLaboratoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.SpatialInformation <em>Spatial Information</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.SpatialInformation
	 * @generated
	 */
	public Adapter createSpatialInformationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Event <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Event
	 * @generated
	 */
	public Adapter createEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Contact <em>Contact</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Contact
	 * @generated
	 */
	public Adapter createContactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Reference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Reference
	 * @generated
	 */
	public Adapter createReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.ModelMath <em>Model Math</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.ModelMath
	 * @generated
	 */
	public Adapter createModelMathAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Parameter
	 * @generated
	 */
	public Adapter createParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.ModelEquation <em>Model Equation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.ModelEquation
	 * @generated
	 */
	public Adapter createModelEquationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.Exposure <em>Exposure</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.Exposure
	 * @generated
	 */
	public Adapter createExposureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link metadata.StringObject <em>String Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see metadata.StringObject
	 * @generated
	 */
	public Adapter createStringObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //MetadataAdapterFactory
