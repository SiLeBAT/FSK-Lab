package de.bund.bfr.knime.fsklab.nodes;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.bund.bfr.knime.fsklab.v2_0.editor.FSKEditorJSNodeDialog.ModelType;
import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.DoseResponseModelGeneralInformation;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.OtherModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;

public class NodeUtilsTest {

	@Test
	public void testInitializeModel_genericModel_shouldReturnGenericModel() {
		Model model = NodeUtils.initializeModel(ModelType.genericModel);
		assertThat(model, instanceOf(GenericModel.class));
		assertEquals("genericModel", model.getModelType());
		
		GenericModelGeneralInformation information = ((GenericModel)model).getGeneralInformation();
		assertEquals("Generic model", information.getModelCategory().getModelClass());
	}

	@Test
	public void testInitializeModel_dataModel_shouldReturnDataModel() {
		Model model = NodeUtils.initializeModel(ModelType.dataModel);
		assertThat(model, instanceOf(DataModel.class));
		assertEquals("dataModel", model.getModelType());

		// TODO: Data model has no ModelCategory
	}

	@Test
	public void testInitializeModel_consumptionModel_shouldReturnConsumptionModel() {
		Model model = NodeUtils.initializeModel(ModelType.consumptionModel);
		assertThat(model, instanceOf(ConsumptionModel.class));
		assertEquals("consumptionModel", model.getModelType());
		
		PredictiveModelGeneralInformation information = ((ConsumptionModel) model).getGeneralInformation();
		assertEquals("Consumption model", information.getModelCategory().getModelClass());
	}
	
	@Test
	public void testInitializeModel_doseResponseModel_shouldReturnDoseResponseModel() {
		Model model = NodeUtils.initializeModel(ModelType.doseResponseModel);
		assertThat(model, instanceOf(DoseResponseModel.class));
		assertEquals("doseResponseModel", model.getModelType());
		
		DoseResponseModelGeneralInformation information = ((DoseResponseModel) model).getGeneralInformation();
		assertEquals("Dose-response model", information.getModelCategory().getModelClass());
	}

	@Test
	public void testInitializeModel_exposureModel_shouldReturnExposureModel() {
		Model model = NodeUtils.initializeModel(ModelType.exposureModel);
		assertThat(model, instanceOf(ExposureModel.class));
		assertEquals("exposureModel", model.getModelType());
		
		PredictiveModelGeneralInformation information = ((ExposureModel) model).getGeneralInformation();
		assertEquals("Exposure model", information.getModelCategory().getModelClass());
	}

	@Test
	public void testInitializeModel_healthModel_shouldReturnHealthModel() {
		Model model = NodeUtils.initializeModel(ModelType.healthModel);
		assertThat(model, instanceOf(HealthModel.class));
		assertEquals("healthModel", model.getModelType());
		
		PredictiveModelGeneralInformation information = ((HealthModel) model).getGeneralInformation();
		assertEquals("Health metrics model", information.getModelCategory().getModelClass());
	}

	@Test
	public void testInitializeModel_otherModel_shouldReturnOtherModel() {
		Model model = NodeUtils.initializeModel(ModelType.otherModel);
		assertThat(model, instanceOf(OtherModel.class));
		assertEquals("otherModel", model.getModelType());
		
		OtherModelGeneralInformation information = ((OtherModel) model).getGeneralInformation();
		assertEquals("Other empirical models", information.getModelCategory().getModelClass());
	}

	@Test
	public void testInitializeModel_predictiveModel_shouldReturnPredictiveModel() {
		Model model = NodeUtils.initializeModel(ModelType.predictiveModel);
		assertThat(model, instanceOf(PredictiveModel.class));
		assertEquals("predictiveModel", model.getModelType());
		
		PredictiveModelGeneralInformation information = ((PredictiveModel) model).getGeneralInformation();
		assertEquals("Predictive model", information.getModelCategory().getModelClass());
	}

	@Test
	public void testInitializeModel_processModel_shouldReturnProcessModel() {
		Model model = NodeUtils.initializeModel(ModelType.processModel);
		assertThat(model, instanceOf(ProcessModel.class));
		assertEquals("processModel", model.getModelType());
		
		PredictiveModelGeneralInformation information = ((ProcessModel) model).getGeneralInformation();
		assertEquals("Process model", information.getModelCategory().getModelClass());
	}
	
	@Test
	public void testInitializeModel_qraModel_shouldReturnQraModel() {
		Model model = NodeUtils.initializeModel(ModelType.qraModel);
		assertThat(model, instanceOf(QraModel.class));
		assertEquals("qraModel", model.getModelType());

		PredictiveModelGeneralInformation information = ((QraModel) model).getGeneralInformation();
		assertEquals("Quantitative risk assessment", information.getModelCategory().getModelClass());
	}
	
	@Test
	public void testInitializeModel_riskModel_shouldReturnRiskModel() {
		Model model = NodeUtils.initializeModel(ModelType.riskModel);
		assertThat(model, instanceOf(RiskModel.class));
		assertEquals("riskModel", model.getModelType());

		PredictiveModelGeneralInformation information = ((RiskModel) model).getGeneralInformation();
		assertEquals("Risk characterization model", information.getModelCategory().getModelClass());
	}

	@Test
	public void testInitializeModel_toxicologicalModel_shouldReturnToxicologicalModel() {
		Model model = NodeUtils.initializeModel(ModelType.toxicologicalModel);
		assertThat(model, instanceOf(ToxicologicalModel.class));
		assertEquals("toxicologicalModel", model.getModelType());

		PredictiveModelGeneralInformation information = ((ToxicologicalModel) model).getGeneralInformation();
		assertEquals("Toxicological reference value", information.getModelCategory().getModelClass());
	}

	@Test
	public void testInitializeModel_null_shouldReturnGenericModel() {
		Model model = NodeUtils.initializeModel(null);
		assertThat(model, instanceOf(GenericModel.class));
		assertEquals("genericModel", model.getModelType());

		GenericModelGeneralInformation information = ((GenericModel)model).getGeneralInformation();
		assertEquals("Generic model", information.getModelCategory().getModelClass());
	}
}
