package metadata;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.ConsumptionModelScope;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DataModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DataModelModelMath;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.DoseResponseModelGeneralInformation;
import de.bund.bfr.metadata.swagger.DoseResponseModelModelMath;
import de.bund.bfr.metadata.swagger.DoseResponseModelScope;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.ExposureModelScope;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.OtherModelDataBackground;
import de.bund.bfr.metadata.swagger.OtherModelGeneralInformation;
import de.bund.bfr.metadata.swagger.OtherModelModelMath;
import de.bund.bfr.metadata.swagger.OtherModelScope;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.PredictiveModelDataBackground;
import de.bund.bfr.metadata.swagger.PredictiveModelGeneralInformation;
import de.bund.bfr.metadata.swagger.PredictiveModelModelMath;
import de.bund.bfr.metadata.swagger.PredictiveModelScope;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.ProcessModelScope;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModelScope;

@SuppressWarnings("static-method")
public class SwaggerUtilTest {

	private static StringObject createStringObject(String string) {
		final StringObject so = metadata.MetadataFactory.eINSTANCE.createStringObject();
		so.setValue(string);

		return so;
	}

	@Test
	public void testGetGeneralInformation() {

		assertThat(SwaggerUtil.getGeneralInformation(createGenericModel()), instanceOf(GenericModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createDataModel()), instanceOf(DataModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createPredictiveModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createOtherModel()), instanceOf(OtherModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createExposureModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createToxicologicalModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createDoseResponseModel()), instanceOf(DoseResponseModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createProcessModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createConsumptionModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createRiskModel()), instanceOf(PredictiveModelGeneralInformation.class));
		assertThat(SwaggerUtil.getGeneralInformation(createQraModel()), instanceOf(PredictiveModelGeneralInformation.class));
	}

	@Test
	public void testGetScope() {
		assertThat(SwaggerUtil.getScope(createGenericModel()), instanceOf(GenericModelScope.class));
		assertThat(SwaggerUtil.getScope(createDataModel()), instanceOf(GenericModelScope.class));
		assertThat(SwaggerUtil.getScope(createPredictiveModel()), instanceOf(PredictiveModelScope.class));
		assertThat(SwaggerUtil.getScope(createOtherModel()), instanceOf(OtherModelScope.class));
		assertThat(SwaggerUtil.getScope(createExposureModel()), instanceOf(ExposureModelScope.class));
		assertThat(SwaggerUtil.getScope(createToxicologicalModel()), instanceOf(ToxicologicalModelScope.class));
		assertThat(SwaggerUtil.getScope(createDoseResponseModel()), instanceOf(DoseResponseModelScope.class));
		assertThat(SwaggerUtil.getScope(createProcessModel()), instanceOf(ProcessModelScope.class));
		assertThat(SwaggerUtil.getScope(createConsumptionModel()), instanceOf(ConsumptionModelScope.class));
		assertThat(SwaggerUtil.getScope(createRiskModel()),  instanceOf(ExposureModelScope.class));
		assertThat(SwaggerUtil.getScope(createQraModel()), instanceOf(ExposureModelScope.class));
	}

	@Test
	public void testGetDataBackground() {
		assertThat(SwaggerUtil.getDataBackground(createGenericModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createDataModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createPredictiveModel()), instanceOf(PredictiveModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createOtherModel()), instanceOf(OtherModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createExposureModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createToxicologicalModel()), instanceOf(PredictiveModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createDoseResponseModel()), instanceOf(PredictiveModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createProcessModel()), instanceOf(PredictiveModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createConsumptionModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createRiskModel()), instanceOf(GenericModelDataBackground.class));
		assertThat(SwaggerUtil.getDataBackground(createQraModel()), instanceOf(GenericModelDataBackground.class));
	}

	@Test
	public void testGetModelMath() {
		assertThat(SwaggerUtil.getModelMath(createGenericModel()), instanceOf(GenericModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createDataModel()), instanceOf(DataModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createPredictiveModel()), instanceOf(PredictiveModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createOtherModel()), instanceOf(OtherModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createExposureModel()), instanceOf(GenericModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createToxicologicalModel()), instanceOf(GenericModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createDoseResponseModel()), instanceOf(DoseResponseModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createProcessModel()), instanceOf(PredictiveModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createConsumptionModel()), instanceOf(PredictiveModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createRiskModel()), instanceOf(GenericModelModelMath.class));
		assertThat(SwaggerUtil.getModelMath(createQraModel()), instanceOf(GenericModelModelMath.class));
	}

	@Test
	public void testGetLanguageWrittenIn() {
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createGenericModel()));
		assertNull(SwaggerUtil.getLanguageWrittenIn(createDataModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createPredictiveModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createOtherModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createExposureModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createToxicologicalModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createDoseResponseModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createProcessModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createConsumptionModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createRiskModel()));
		assertEquals("R", SwaggerUtil.getLanguageWrittenIn(createQraModel()));
	}

	@Test
	public void testGetModelName() {
		assertEquals("name", SwaggerUtil.getModelName(createGenericModel()));
		assertEquals("name", SwaggerUtil.getModelName(createDataModel()));
		assertEquals("name", SwaggerUtil.getModelName(createPredictiveModel()));
		assertEquals("name", SwaggerUtil.getModelName(createOtherModel()));
		assertEquals("name", SwaggerUtil.getModelName(createExposureModel()));
		assertEquals("name", SwaggerUtil.getModelName(createToxicologicalModel()));
		assertEquals("name", SwaggerUtil.getModelName(createDoseResponseModel()));
		assertEquals("name", SwaggerUtil.getModelName(createProcessModel()));
		assertEquals("name", SwaggerUtil.getModelName(createConsumptionModel()));
		assertEquals("name", SwaggerUtil.getModelName(createRiskModel()));
		assertEquals("name", SwaggerUtil.getModelName(createQraModel()));
	}

	private static GenericModel createGenericModel() {
		final GenericModel model = new GenericModel();
		model.setModelType("genericModel");

		final GenericModelGeneralInformation information = new GenericModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");

		model.setGeneralInformation(information);
		model.setScope(new GenericModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new GenericModelModelMath());

		return model;
	}

	private static DataModel createDataModel() {
		final DataModel model = new DataModel();
		model.setModelType("dataModel");

		final DataModelGeneralInformation information = new DataModelGeneralInformation();
		information.setName("name");
		model.setGeneralInformation(information);

		model.setScope(new GenericModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new DataModelModelMath());

		return model;
	}

	private static PredictiveModel createPredictiveModel() {
		final PredictiveModel model = new PredictiveModel();
		model.setModelType("predictiveModel");

		final PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new PredictiveModelScope());
		model.setDataBackground(new PredictiveModelDataBackground());
		model.setModelMath(new PredictiveModelModelMath());

		return model;
	}

	private static OtherModel createOtherModel() {
		final OtherModel model = new OtherModel();
		model.setModelType("otherModel");

		final OtherModelGeneralInformation information = new OtherModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new OtherModelScope());
		model.setDataBackground(new OtherModelDataBackground());
		model.setModelMath(new OtherModelModelMath());

		return model;
	}

	private static ExposureModel createExposureModel() {
		final ExposureModel model = new ExposureModel();
		model.setModelType("exposureModel");

		final PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ExposureModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new GenericModelModelMath());

		return model;
	}

	private static ToxicologicalModel createToxicologicalModel() {
		final ToxicologicalModel model = new ToxicologicalModel();
		model.setModelType("toxicologicalModel");

		final PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ToxicologicalModelScope());
		model.setDataBackground(new PredictiveModelDataBackground());
		model.setModelMath(new GenericModelModelMath());

		return model;
	}

	private static DoseResponseModel createDoseResponseModel() {
		final DoseResponseModel model = new DoseResponseModel();
		model.setModelType("doseResponseModel");

		final DoseResponseModelGeneralInformation information = new DoseResponseModelGeneralInformation();
		information.setModelName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new DoseResponseModelScope());
		model.setDataBackground(new PredictiveModelDataBackground());
		model.setModelMath(new DoseResponseModelModelMath());

		return model;
	}

	private static ProcessModel createProcessModel() {
		final ProcessModel model = new ProcessModel();
		model.setModelType("processModel");

		final PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ProcessModelScope());
		model.setDataBackground(new PredictiveModelDataBackground());
		model.setModelMath(new PredictiveModelModelMath());

		return model;
	}

	private static ConsumptionModel createConsumptionModel() {
		final ConsumptionModel model = new ConsumptionModel();
		model.setModelType("consumptionModel");

		final PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ConsumptionModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new PredictiveModelModelMath());

		return model;
	}

	private static RiskModel createRiskModel() {
		final RiskModel model = new RiskModel();
		model.setModelType("riskModel");

		final PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ExposureModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new GenericModelModelMath());

		return model;
	}

	private static QraModel createQraModel() {
		final QraModel model = new QraModel();
		model.setModelType("qraModel");

		final PredictiveModelGeneralInformation information = new PredictiveModelGeneralInformation();
		information.setName("name");
		information.setLanguageWrittenIn("R");
		model.setGeneralInformation(information);

		model.setScope(new ExposureModelScope());
		model.setDataBackground(new GenericModelDataBackground());
		model.setModelMath(new GenericModelModelMath());

		return model;
	}
	
	private static Date createDate(int year, int month, int date) {
		return new Date(year - 1900, month, date);
	}
}
