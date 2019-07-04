package de.bund.bfr.knime.fsklab.rakip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Date;

import org.junit.Test;
import org.threeten.bp.LocalDate;

import com.gmail.gcolaianni5.jris.bean.Record;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.Exposure;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.ModelEquation;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;

public class RakipUtilTest {

	@Test
	public void testConvertRecord() {

		Reference reference;
		{
			Record record = new Record();
			record.setType(com.gmail.gcolaianni5.jris.bean.Type.RPRT);
			record.setPrimaryDate("2018-01-01");
			record.setDoi("10.2903/j.efsa.2018.5134");
			record.addFirstAuthor("Miguel");
			record.setTitle("Listeria monocytogenes");
			record.setAbstr("abstract");
			record.setSecondaryTitle("journal");
			record.setVolumeNumber("volume number");
			record.setIssueNumber(0);
			record.setUrl("www.efsa.europa.eu");

			reference = RakipUtil.convert2(record);
		}

		assertFalse(reference.isIsReferenceDescription());
		assertEquals(PublicationTypeEnum.RPRT, reference.getPublicationType().get(0));
		assertEquals(LocalDate.of(2018, 1, 1), reference.getDate());
		assertEquals("10.2903/j.efsa.2018.5134", reference.getDoi());
		assertEquals("Miguel", reference.getAuthorList());
		assertEquals("Listeria monocytogenes", reference.getTitle());
		assertEquals("abstract", reference.getAbstract());
		assertEquals("volume number", reference.getVolume());
		assertEquals("0", reference.getIssue());
		assertEquals("www.efsa.europa.eu", reference.getWebsite());
	}

	@Test
	public void testConvertModelCategory() {

		ModelCategory modelCategory;
		{
			de.bund.bfr.knime.fsklab.rakip.ModelCategory deprecated = new de.bund.bfr.knime.fsklab.rakip.ModelCategory();
			deprecated.modelClass = "modelClass";
			deprecated.modelSubClass.add("subClass");
			deprecated.modelClassComment = "classComment";
			deprecated.basicProcess.add("basicProcess");

			modelCategory = RakipUtil.convert2(deprecated);
		}

		assertEquals("modelClass", modelCategory.getModelClass());
		assertEquals("subClass", modelCategory.getModelSubClass().get(0));
		assertEquals("classComment", modelCategory.getModelClassComment());
		assertEquals("basicProcess", modelCategory.getBasicProcess().get(0));
	}

	@Test
	public void testConvertScope() {

		GenericModelScope scope;
		{
			de.bund.bfr.knime.fsklab.rakip.Scope deprecated = new de.bund.bfr.knime.fsklab.rakip.Scope();
			deprecated.generalComment = "generalComment";
			deprecated.temporalInformation = "temporalInformation";
			deprecated.product = new de.bund.bfr.knime.fsklab.rakip.Product();
			deprecated.hazard = new de.bund.bfr.knime.fsklab.rakip.Hazard();
			deprecated.populationGroup = new de.bund.bfr.knime.fsklab.rakip.PopulationGroup();
			deprecated.region.add("region");
			deprecated.country.add("country");

			scope = RakipUtil.convert2(deprecated);
		}

		assertEquals("generalComment", scope.getGeneralComment());
		assertEquals("temporalInformation", scope.getTemporalInformation());
		assertTrue(scope.getProduct().size() > 0);
		assertTrue(scope.getHazard().size() > 0);
		assertEquals(1, scope.getPopulationGroup().size());
	}

	@Test
	public void testConvertProduct() {

		Product product;
		{
			de.bund.bfr.knime.fsklab.rakip.Product deprecated = new de.bund.bfr.knime.fsklab.rakip.Product();
			deprecated.environmentName = "environmentName";
			deprecated.environmentDescription = "environmentDescription";
			deprecated.environmentUnit = "environmentUnit";
			deprecated.productionMethod.add("productionMethod");
			deprecated.packaging.add("packaging");
			deprecated.productTreatment.add("productTreatment");
			deprecated.originCountry = "originCountry";
			deprecated.originArea = "originArea";
			deprecated.fisheriesArea = "fisheriesArea";
			deprecated.productionDate = new Date();
			deprecated.expirationDate = new Date();

			product = RakipUtil.convert2(deprecated);
		}

		assertEquals("environmentName", product.getName());
		assertEquals("environmentDescription", product.getDescription());
		assertEquals("environmentUnit", product.getUnit());
		assertEquals("productionMethod", product.getMethod().get(0));
		assertEquals("packaging", product.getPackaging().get(0));
		assertEquals("productTreatment", product.getTreatment().get(0));
		assertEquals("originCountry", product.getOriginCountry());
		assertEquals("originArea", product.getOriginArea());
		assertEquals("fisheriesArea", product.getFisheriesArea());
		assertNotNull(product.getProductionDate());
		assertNotNull(product.getExpiryDate());
	}

	@Test
	public void testConvertHazard() {

		Hazard hazard;
		{
			de.bund.bfr.knime.fsklab.rakip.Hazard deprecated = new de.bund.bfr.knime.fsklab.rakip.Hazard();
			deprecated.hazardType = "hazardType";
			deprecated.hazardName = "hazardName";
			deprecated.hazardDescription = "hazardDescription";
			deprecated.hazardUnit = "hazardUnit";
			deprecated.adverseEffect = "adverseEffect";
			deprecated.sourceOfContamination = "sourceOfContamination";
			deprecated.bmd = "bmd";
			deprecated.mrl = "mrl";
			deprecated.noael = "noael";
			deprecated.loael = "loael";
			deprecated.aoel = "aoel";
			deprecated.ard = "ard";
			deprecated.adi = "adi";
			deprecated.hazardIndSum = "hazardIndSum";

			hazard = RakipUtil.convert2(deprecated);
		}

		assertEquals("hazardType", hazard.getType());
		assertEquals("hazardName", hazard.getName());
		assertEquals("hazardDescription", hazard.getDescription());
		assertEquals("hazardUnit", hazard.getUnit());
		assertEquals("adverseEffect", hazard.getAdverseEffect());
		assertEquals("sourceOfContamination", hazard.getSourceOfContamination());
		assertEquals("bmd", hazard.getBenchmarkDose());
		assertEquals("mrl", hazard.getMaximumResidueLimit());
		assertEquals("noael", hazard.getNoObservedAdverseAffectLevel());
		assertEquals("loael", hazard.getLowestObservedAdverseAffectLevel());
		assertEquals("aoel", hazard.getAcceptableOperatorsExposureLevel());
		assertEquals("ard", hazard.getAcuteReferenceDose());
		assertEquals("adi", hazard.getAcceptableDailyIntake());
		assertEquals("hazardIndSum", hazard.getIndSum());
	}

	@Test
	public void testConvertPopulationGroup() {

		PopulationGroup populationGroup;
		{
			de.bund.bfr.knime.fsklab.rakip.PopulationGroup deprecated = new de.bund.bfr.knime.fsklab.rakip.PopulationGroup();
			deprecated.populationName = "populationName";
			deprecated.targetPopulation = "targetPopulation";
			deprecated.populationSpan.add("span");
			deprecated.populationDescription.add("description");
			deprecated.populationAge.add("populationAge");
			deprecated.populationGender = "populationGender";
			deprecated.bmi.add("bmi");
			deprecated.specialDietGroups.add("specialDietGroups");
			deprecated.patternConsumption.add("patternConsumption");
			deprecated.region.add("region");
			deprecated.country.add("country");
			deprecated.populationRiskFactor.add("factor");
			deprecated.season.add("season");

			populationGroup = RakipUtil.convert2(deprecated);
		}

		assertEquals("populationName", populationGroup.getName());
		assertEquals("targetPopulation", populationGroup.getTargetPopulation());
		assertEquals("span", populationGroup.getPopulationSpan().get(0));
		assertEquals("description", populationGroup.getPopulationDescription().get(0));
		assertEquals("populationAge", populationGroup.getPopulationAge().get(0));
		assertEquals("populationGender", populationGroup.getPopulationGender());
		assertEquals("bmi", populationGroup.getBmi().get(0));
		assertEquals("specialDietGroups", populationGroup.getSpecialDietGroups().get(0));
		assertEquals("patternConsumption", populationGroup.getPatternConsumption().get(0));
		assertEquals("region", populationGroup.getRegion().get(0));
		assertEquals("country", populationGroup.getCountry().get(0));
		assertEquals("factor", populationGroup.getPopulationRiskFactor().get(0));
		assertEquals("season", populationGroup.getSeason().get(0));
	}

	@Test
	public void testConvertDataBackground() {

		GenericModelDataBackground dataBackground;
		{
			de.bund.bfr.knime.fsklab.rakip.DataBackground deprecated = new de.bund.bfr.knime.fsklab.rakip.DataBackground();
			deprecated.study = new de.bund.bfr.knime.fsklab.rakip.Study();
			deprecated.studySample = new de.bund.bfr.knime.fsklab.rakip.StudySample();
			deprecated.dietaryAssessmentMethod = new de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod();
			deprecated.laboratory = new de.bund.bfr.knime.fsklab.rakip.Laboratory();
			deprecated.assay = new de.bund.bfr.knime.fsklab.rakip.Assay();

			dataBackground = RakipUtil.convert2(deprecated);
		}

		assertNotNull(dataBackground.getStudy());
		assertFalse(dataBackground.getStudySample().isEmpty());
		assertFalse(dataBackground.getDietaryAssessmentMethod().isEmpty());
		assertFalse(dataBackground.getLaboratory().isEmpty());
		assertFalse(dataBackground.getAssay().isEmpty());
	}

	@Test
	public void testConvertStudy() {

		Study study;
		{
			de.bund.bfr.knime.fsklab.rakip.Study deprecated = new de.bund.bfr.knime.fsklab.rakip.Study();
			deprecated.id = "id";
			deprecated.title = "title";
			deprecated.description = "description";
			deprecated.designType = "designType";
			deprecated.measurementType = "measurementType";
			deprecated.technologyType = "technologyType";
			deprecated.technologyPlatform = "technologyPlatform";
			deprecated.accreditationProcedure = "accreditationProcedure";
			deprecated.protocolName = "protocolName";
			deprecated.protocolType = "protocolType";
			deprecated.protocolDescription = "protocolDescription";
			deprecated.protocolUri = URI.create("https://bfr.bund.de");
			deprecated.protocolVersion = "protocolVersion";
			deprecated.parametersName = "parametersName";
			deprecated.componentsName = "componentsName";
			deprecated.componentsType = "componentsType";

			study = RakipUtil.convert2(deprecated);
		}

		assertEquals("id", study.getIdentifier());
		assertEquals("title", study.getTitle());
		assertEquals("description", study.getDescription());
		assertEquals("designType", study.getDesignType());
		assertEquals("measurementType", study.getAssayMeasurementType());
		assertEquals("technologyType", study.getAssayTechnologyType());
		assertEquals("technologyPlatform", study.getAssayTechnologyPlatform());
		assertEquals("accreditationProcedure", study.getAccreditationProcedureForTheAssayTechnology());
		assertEquals("protocolName", study.getProtocolName());
		assertEquals("protocolType", study.getProtocolType());
		assertEquals("protocolDescription", study.getProtocolDescription());
		assertEquals("https://bfr.bund.de", study.getProtocolURI());
		assertEquals("protocolVersion", study.getProtocolVersion());
		assertEquals("parametersName", study.getProtocolParametersName());
		assertEquals("componentsName", study.getProtocolComponentsName());
		assertEquals("componentsType", study.getProtocolComponentsType());
	}

	@Test
	public void testConvertStudySample() {

		StudySample studySample;
		{
			de.bund.bfr.knime.fsklab.rakip.StudySample deprecated = new de.bund.bfr.knime.fsklab.rakip.StudySample();
			deprecated.sample = "sample";
			deprecated.collectionProtocol = "collectionProtocol";
			deprecated.samplingStrategy = "samplingStrategy";
			deprecated.samplingProgramType = "samplingProgramType";
			deprecated.samplingMethod = "samplingMethod";
			deprecated.samplingPlan = "samplingPlan";
			deprecated.samplingWeight = "samplingWeight";
			deprecated.samplingSize = "samplingSize";
			deprecated.lotSizeUnit = "lotSizeUnit";

			studySample = RakipUtil.convert2(deprecated);
		}

		assertEquals("sample", studySample.getSampleName());
		assertEquals("collectionProtocol", studySample.getProtocolOfSampleCollection());
		assertEquals("samplingStrategy", studySample.getSamplingStrategy());
		assertEquals("samplingProgramType", studySample.getTypeOfSamplingProgram());
		assertEquals("samplingMethod", studySample.getSamplingMethod());
		assertEquals("samplingPlan", studySample.getSamplingPlan());
		assertEquals("samplingWeight", studySample.getSamplingWeight());
		assertEquals("samplingSize", studySample.getSamplingSize());
		assertEquals("lotSizeUnit", studySample.getLotSizeUnit());
		assertTrue(studySample.getSamplingPoint().isEmpty());
	}

	@Test
	public void testConvertDietaryAssessmentMethod() {

		DietaryAssessmentMethod method;
		{
			de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod deprecated = new de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod();
			deprecated.collectionTool = "collectionTool";
			deprecated.numberOfNonConsecutiveOneDay = 0;
			deprecated.softwareTool = "softwareTool";
			deprecated.numberOfFoodItems.add("0");
			deprecated.recordTypes.add("type");
			deprecated.foodDescriptors.add("descriptor");

			method = RakipUtil.convert2(deprecated);
		}

		assertEquals("collectionTool", method.getCollectionTool());
		assertEquals("0", method.getNumberOfNonConsecutiveOneDay());
		assertEquals("softwareTool", method.getSoftwareTool());
		assertEquals("0", method.getNumberOfFoodItems().get(0));
		assertEquals("type", method.getRecordTypes().get(0));
		assertEquals("descriptor", method.getFoodDescriptors().get(0));
	}
	
	@Test
	public void testConvertLaboratory() {
		
		Laboratory laboratory;
		{
			de.bund.bfr.knime.fsklab.rakip.Laboratory deprecated = new de.bund.bfr.knime.fsklab.rakip.Laboratory();
			deprecated.accreditation = "accreditation";
			deprecated.name = "name";
			deprecated.country = "country";
			
			laboratory = RakipUtil.convert2(deprecated);
		}
		
		assertEquals("accreditation", laboratory.getAccreditation().get(0));
		assertEquals("name", laboratory.getName());
		assertEquals("country", laboratory.getCountry());
	}
	
	@Test
	public void testConvertAssay() {
		
		Assay assay;
		{
			de.bund.bfr.knime.fsklab.rakip.Assay deprecated = new de.bund.bfr.knime.fsklab.rakip.Assay();
			deprecated.name = "name";
			deprecated.description = "description";
			deprecated.moisturePercentage = "moisturePercentage";
			deprecated.fatPercentage = "fatPercentage";
			deprecated.detectionLimit = "detectionLimit";
			deprecated.quantificationLimit = "quantificationLimit";
			deprecated.leftCensoredData = "leftCensoredData";
			deprecated.contaminationRange = "contaminationRange";
			deprecated.uncertaintyValue = "uncertaintyValue";
			
			assay = RakipUtil.convert2(deprecated);
		}
		
		assertEquals("name", assay.getName());
		assertEquals("description", assay.getDescription());
		assertEquals("moisturePercentage", assay.getMoisturePercentage());
		assertEquals("fatPercentage", assay.getFatPercentage());
		assertEquals("detectionLimit", assay.getDetectionLimit());
		assertEquals("quantificationLimit", assay.getQuantificationLimit());
		assertEquals("leftCensoredData", assay.getLeftCensoredData());
		assertEquals("contaminationRange", assay.getContaminationRange());
		assertEquals("uncertaintyValue", assay.getUncertaintyValue());
	}
	
	@Test
	public void testConvertModelMath() {

		GenericModelModelMath math;
		{
			de.bund.bfr.knime.fsklab.rakip.ModelMath deprecated = new de.bund.bfr.knime.fsklab.rakip.ModelMath();
			deprecated.sse = 0;
			deprecated.mse = 1;
			deprecated.rmse = 2;
			deprecated.rSquared = 3;
			deprecated.aic = 4;
			deprecated.bic = 5;
			deprecated.modelEquation.add(new de.bund.bfr.knime.fsklab.rakip.ModelEquation());
			deprecated.fittingProcedure = "fittingProcedure";
			deprecated.exposure = new de.bund.bfr.knime.fsklab.rakip.Exposure();
			deprecated.event.add("Event");
			
			math = RakipUtil.convert2(deprecated);
		}

		assertTrue(math.getParameter().isEmpty());
		assertFalse(math.getQualityMeasures().isEmpty());
		assertFalse(math.getModelEquation().isEmpty());
		assertEquals("fittingProcedure", math.getFittingProcedure());
		assertFalse(math.getExposure().isEmpty());
		assertEquals("Event", math.getEvent().get(0));
	}
	
	@Test
	public void testConvertParameter() {
		
		Parameter param;
		{
			de.bund.bfr.knime.fsklab.rakip.Parameter deprecated = new de.bund.bfr.knime.fsklab.rakip.Parameter();
			deprecated.id = "id";
			deprecated.classification = de.bund.bfr.knime.fsklab.rakip.Parameter.Classification.constant;
			deprecated.name = "name";
			deprecated.description = "description";
			deprecated.type = "type";
			deprecated.unit = "unit";
			deprecated.unitCategory = "unitCategory";
			deprecated.dataType = de.bund.bfr.knime.fsklab.rakip.Parameter.DataTypes.Boolean;
			deprecated.source = "source";
			deprecated.subject = "subject";
			deprecated.distribution = "distribution";
			deprecated.value = "value";
			deprecated.minValue = "false";
			deprecated.maxValue = "true";
			deprecated.reference = "reference";
			deprecated.variabilitySubject = "variabilitySubject";
			deprecated.rangeOfApplicability = "rangeOfApplicability";
			deprecated.modelApplicability.add("applicability");
			deprecated.error = 2.718;
			
			param = RakipUtil.convert2(deprecated);
		}
		
		assertEquals("id", param.getId());
		assertEquals(Parameter.ClassificationEnum.CONSTANT, param.getClassification());
		assertEquals("name", param.getName());
		assertEquals("description", param.getDescription());
		assertEquals("unit", param.getUnit());
		assertEquals("unitCategory", param.getUnitCategory());
		assertEquals(Parameter.DataTypeEnum.BOOLEAN, param.getDataType());
		assertEquals("source", param.getSource());
		assertEquals("subject", param.getSubject());
		assertEquals("distribution", param.getDistribution());
		assertEquals("value", param.getValue());
		assertNull(param.getReference()); // reference is not converted
		assertEquals("variabilitySubject", param.getVariabilitySubject());
		assertEquals("false", param.getMinValue());
		assertEquals("true", param.getMaxValue());
		assertEquals("2.718", param.getError());
	}
	
	@Test
	public void testConvertModelEquation() {
		
		ModelEquation equation;
		{
			de.bund.bfr.knime.fsklab.rakip.ModelEquation deprecated = new de.bund.bfr.knime.fsklab.rakip.ModelEquation();
			deprecated.equationName = "equationName";
			deprecated.equationClass = "equationClass";
			deprecated.equationReference.add(new Record());
			deprecated.equation = "equation";
			
			equation = RakipUtil.convert2(deprecated);
		}
		
		assertEquals("equationName", equation.getName());
		assertEquals("equationClass", equation.getPropertyClass());
		assertEquals(1, equation.getReference().size());
		assertEquals("equation", equation.getModelEquation());
		assertNull(equation.getModelHypothesis());
	}
	
	@Test
	public void testConvertExposure() {
		
		Exposure exposure;
		{
			de.bund.bfr.knime.fsklab.rakip.Exposure deprecated = new de.bund.bfr.knime.fsklab.rakip.Exposure();
			deprecated.treatment = "treatment";
			deprecated.contaminationLevel = "contaminationLevel";
			deprecated.exposureType = "exposureType";
			deprecated.scenario = "scenario";
			deprecated.uncertaintyEstimation = "uncertaintyEstimation";
			
			exposure = RakipUtil.convert2(deprecated);
		}
		
		assertEquals("treatment", exposure.getTreatment().get(0));
		assertEquals("contaminationLevel", exposure.getContamination().get(0));
		assertEquals("exposureType", exposure.getType());
		assertEquals("scenario", exposure.getScenario().get(0));
		assertEquals("uncertaintyEstimation", exposure.getUncertaintyEstimation());
	}
}
