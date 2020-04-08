package metadata;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.threeten.bp.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;

import de.bund.bfr.metadata.swagger.Assay;
import de.bund.bfr.metadata.swagger.Contact;
import de.bund.bfr.metadata.swagger.DietaryAssessmentMethod;
import de.bund.bfr.metadata.swagger.GenericModelDataBackground;
import de.bund.bfr.metadata.swagger.GenericModelGeneralInformation;
import de.bund.bfr.metadata.swagger.GenericModelModelMath;
import de.bund.bfr.metadata.swagger.GenericModelScope;
import de.bund.bfr.metadata.swagger.Hazard;
import de.bund.bfr.metadata.swagger.Laboratory;
import de.bund.bfr.metadata.swagger.ModelCategory;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum;
import de.bund.bfr.metadata.swagger.PopulationGroup;
import de.bund.bfr.metadata.swagger.Product;
import de.bund.bfr.metadata.swagger.Reference;
import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;
import de.bund.bfr.metadata.swagger.Study;
import de.bund.bfr.metadata.swagger.StudySample;
import de.bund.bfr.metadata.swagger.ModelEquation;
import de.bund.bfr.metadata.swagger.Exposure;

public class EmfMetadataModule extends SimpleModule {

	private static final long serialVersionUID = 7262661075920787239L;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	private static final ObjectMapper originalMapper = new ObjectMapper().registerModule(new ThreeTenModule());

	public EmfMetadataModule() {
		super("EmfMetadataModule", Version.unknownVersion());

		addDeserializer(GenericModelGeneralInformation.class, new GeneralInformationDeserializer());
		addDeserializer(GenericModelScope.class, new ScopeDeserializer());
		addDeserializer(GenericModelDataBackground.class, new DataBackgroundDeserializer());
		addDeserializer(GenericModelModelMath.class, new ModelMathDeserializer());
	}

	class GeneralInformationDeserializer extends JsonDeserializer<GenericModelGeneralInformation> {

		@Override
		public GenericModelGeneralInformation deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {

			JsonNode node = parser.readValueAsTree();

			if (node.has("eClass")) {
				// EMF information
				GenericModelGeneralInformation information = new GenericModelGeneralInformation();

				if (node.has("name")) {
					information.setName(node.get("name").asText());
				}

				if (node.has("source")) {
					information.setSource(node.get("source").asText());
				}

				if (node.has("identifier")) {
					information.setIdentifier(node.get("identifier").asText());
				}

				if (node.has("creationDate")) {
					try {
						Date date = dateFormat.parse(node.get("creationDate").asText());
						information.setCreationDate(toLocalDate(date));
					} catch (ParseException e) {
						// do nothing
					}
				}

				if (node.has("rights")) {
					information.setRights(node.get("rights").asText());
				}

				if (node.has("available")) {
					information.setAvailability(node.get("available").asText());
				}

				if (node.has("format")) {
					information.setFormat(node.get("format").asText());
				}

				if (node.has("language")) {
					information.setLanguage(node.get("language").asText());
				}

				if (node.has("software")) {
					information.setSoftware(node.get("software").asText());
				}

				if (node.has("languageWrittenIn")) {
					information.setLanguageWrittenIn(node.get("languageWrittenIn").asText());
				}

				if (node.has("status")) {
					information.setStatus(node.get("status").asText());
				}

				if (node.has("objective")) {
					information.setObjective(node.get("objective").asText());
				}

				if (node.has("description")) {
					information.setDescription(node.get("description").asText());
				}

				if (node.has("modelCategory")) {
					JsonNode firstCategory = node.get("modelCategory").get(0);
					information.setModelCategory(convertModelCategory(firstCategory));
				}

				// FIXME: modificationdate is not saved fully to JSON
//				if (node.has("modificationdate")) {
//					for (JsonNode child : node.get("modificationdate")) {
//						try {
//							Date date = dateFormat.parse(child.get("value").asText());
//							information.addModificationDateItem(toLocalDate(date));
//						} catch (ParseException e) {
//							e.printStackTrace();
//						}
//					}
//				}

				if (node.has("author")) {
					information.addAuthorItem(convertContact(node.get("author")));
				}

				if (node.has("creators")) {
					for (JsonNode child : node.get("creators")) {
						information.addCreatorItem(convertContact(child));
					}
				}

				if (node.has("reference")) {
					for (JsonNode child : node.get("reference")) {
						information.addReferenceItem(convertReference(child));
					}
				}

				return information;
			}
			else {
				// Swagger information
				return originalMapper.treeToValue(node, GenericModelGeneralInformation.class);
			}
		}
	}

	class ScopeDeserializer extends JsonDeserializer<GenericModelScope> {

		@Override
		public GenericModelScope deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {

			JsonNode node = parser.readValueAsTree();

			if (node.has("eClass")) {
				// EMF scope
				GenericModelScope scope = new GenericModelScope();

				if (node.has("generalComment")) {
					scope.setGeneralComment(node.get("generalComment").asText());
				}

				if (node.has("temporalInformation")) {
					scope.setTemporalInformation(node.get("temporalInformation").asText());
				}

				if (node.has("product")) {
					for (JsonNode child : node.get("product")) {
						scope.addProductItem(convertProduct(child));
					}
				}

				if (node.has("hazard")) {
					for (JsonNode child : node.get("hazard")) {
						scope.addHazardItem(convertHazard(child));
					}
				}

				if (node.has("populationGroup")) {
					for (JsonNode child : node.get("populationGroup")) {
						scope.addPopulationGroupItem(convertPopulationGroup(child));
					}
				}

				// spatialInformation cannot be converted

				return scope;
			} else {
				// Swagger scope
				return originalMapper.treeToValue(node, GenericModelScope.class);
			}
		}
	}

	class DataBackgroundDeserializer extends JsonDeserializer<GenericModelDataBackground> {

		@Override
		public GenericModelDataBackground deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {

			JsonNode node = parser.readValueAsTree();

			if (node.has("eClass")) {
				// EMF data background
				GenericModelDataBackground background = new GenericModelDataBackground();

				if (node.has("study")) {
					background.setStudy(convertStudy(node.get("study")));
				}

				if (node.has("studySample")) {
					for (JsonNode child : node.get("studySample")) {
						background.addStudySampleItem(convertStudySample(child));
					}
				}

				if (node.has("dietaryAssessmentMethod")) {
					for (JsonNode child : node.get("dietaryAssessmentMethod")) {
						background.addDietaryAssessmentMethodItem(convertDietaryAssessmentMethod(child));
					}
				}

				if (node.has("laboratory")) {
					for (JsonNode child : node.get("laboratory")) {
						background.addLaboratoryItem(convertLaboratory(child));
					}
				}

				if (node.has("assay")) {
					for (JsonNode child : node.get("assay")) {
						background.addAssayItem(convertAssay(child));
					}
				}

				return background;
			} else {
				// Swagger data background
				return originalMapper.treeToValue(node, GenericModelDataBackground.class);
			}
		}
	}

	class ModelMathDeserializer extends JsonDeserializer<GenericModelModelMath> {

		@Override
		public GenericModelModelMath deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {

			JsonNode node = parser.readValueAsTree();

			if (node.has("eClass")) {
				// EMF math
				GenericModelModelMath math = new GenericModelModelMath();

				if (node.has("fittingProcedure")) {
					math.setFittingProcedure(node.get("fittingProcedure").asText());
				}

				if (node.has("parameter")) {
					for (JsonNode child : node.get("parameter")) {
						math.addParameterItem(convertParameter(child));
					}
				}

				if (node.has("modelEquation")) {
					for (JsonNode child : node.get("modelEquation")) {
						math.addModelEquationItem(convertModelEquation(child));
					}
				}

				if (node.has("exposure")) {
					math.addExposureItem(convertExposure(node.get("exposure")));
				}

				// TODO: qualityMeasures (list<StringObject>)

				if (node.has("event")) {
					for (JsonNode child : node.get("event")) {
						math.addEventItem(child.get("value").asText());
					}
				}

				return math;
			} else {
				// Swagger math
				return originalMapper.treeToValue(node, GenericModelModelMath.class);
			}
		}

	}

	private static LocalDate toLocalDate(Date date) {

		ZoneId defaultZoneId = ZoneId.systemDefault();

		int year = date.toInstant().atZone(defaultZoneId).toLocalDate().getYear();
		int month = date.toInstant().atZone(defaultZoneId).toLocalDate().getMonthValue();
		int day = date.toInstant().atZone(defaultZoneId).toLocalDate().getDayOfMonth();

		return LocalDate.of(year, month, day);
	}

	static Contact convertContact(JsonNode node) {
		Contact contact = new Contact();

		if (node.has("title")) {
			contact.setTitle(node.get("title").asText());
		}

		if (node.has("familyName")) {
			contact.setFamilyName(node.get("familyName").asText());
		}

		if (node.has("givenName")) {
			contact.setGivenName(node.get("givenName").asText());
		}

		if (node.has("email")) {
			contact.setEmail(node.get("email").asText());
		}

		if (node.has("telephone")) {
			contact.setTelephone(node.get("telephone").asText());
		}

		if (node.has("streetAddress")) {
			contact.setStreetAddress(node.get("streetAddress").asText());
		}

		if (node.has("country")) {
			contact.setCountry(node.get("country").asText());
		}

		if (node.has("zipCode")) {
			contact.setZipCode(node.get("zipCode").asText());
		}

		if (node.has("region")) {
			contact.setRegion(node.get("region").asText());
		}

		if (node.has("timeZone")) {
			contact.setTimeZone(node.get("timeZone").asText());
		}

		if (node.has("gender")) {
			contact.setGender(node.get("gender").asText());
		}

		if (node.has("note")) {
			contact.setNote(node.get("note").asText());
		}

		if (node.has("organization")) {
			contact.setOrganization(node.get("organization").asText());
		}

		return contact;
	}

	static ModelCategory convertModelCategory(JsonNode node) {

		ModelCategory category = new ModelCategory();

		if (node.has("modelClass")) {
			category.setModelClass(node.get("modelClass").asText());
		}

		if (node.has("modelClassComment")) {
			category.setModelClassComment(node.get("modelClassComment").asText());
		}

		if (node.has("basicProcess")) {
			category.addBasicProcessItem(node.get("basicProcess").asText());
		}

		if (node.has("modelSubClass")) {
			for (JsonNode child : node.get("modelSubClass")) {
				category.addModelSubClassItem(child.get("value").asText());
			}
		}

		return category;
	}

	static Reference convertReference(JsonNode node) {
		Reference reference = new Reference();

		reference.setIsReferenceDescription(
				node.has("isReferenceDescription") ? node.get("isReferenceDescription").asBoolean() : false);

		if (node.has("publicationType")) {
			String typeText = node.get("publicationType").asText();
			reference.setPublicationType(PUBLICATION_TYPE.get(typeText));
		}

		if (node.has("publicationDate")) {
			try {
				Date date = dateFormat.parse(node.get("publicationDate").asText());
				LocalDate localDate = toLocalDate(date);
				reference.setDate(localDate);
			} catch (ParseException e) {
				// do nothing
			}
		}

		if (node.has("pmid")) {
			reference.setPmid(node.get("pmid").asText());
		}

		if (node.has("doi")) {
			reference.setDoi(node.get("doi").asText());
		}

		// authorList

		if (node.has("publicationTitle")) {
			reference.setTitle(node.get("publicationTitle").asText());
		}

		if (node.has("publicationAbstract")) {
			reference.setAbstract(node.get("publicationAbstract").asText());
		}

		if (node.has("publicationJournal")) {
			reference.setJournal(node.get("publicationJournal").asText());
		}

		if (node.has("publicationVolume")) {
			reference.setVolume(node.get("publicationVolume").asText());
		}

		if (node.has("publicationIssue")) {
			reference.setIssue(node.get("publicationIssue").asText());
		}

		if (node.has("publicationStatus")) {
			reference.setStatus(node.get("publicationStatus").asText());
		}

		if (node.has("publicationWebsite")) {
			reference.setWebsite(node.get("publicationWebsite").asText());
		}

		return reference;
	}

	static Product convertProduct(JsonNode node) {

		Product product = new Product();

		if (node.has("productName")) {
			product.setName(node.get("productName").asText());
		}

		if (node.has("productDescription")) {
			product.setDescription(node.get("productDescription").asText());
		}

		if (node.has("productUnit")) {
			product.setUnit(node.get("productUnit").asText());
		}

		if (node.has("productionMethod")) {
			product.addMethodItem(node.get("productionMethod").asText());
		}

		if (node.has("packaging")) {
			product.addPackagingItem(node.get("packaging").asText());
		}

		if (node.has("productTreatment")) {
			product.addTreatmentItem(node.get("productTreatment").asText());
		}

		if (node.has("originCountry")) {
			product.setOriginCountry(node.get("originCountry").asText());
		}

		if (node.has("originArea")) {
			product.setOriginArea(node.get("originArea").asText());
		}

		if (node.has("fisheriesArea")) {
			product.setFisheriesArea(node.get("fisheriesArea").asText());
		}

		if (node.has("productionDate")) {
			try {
				Date date = dateFormat.parse(node.get("productionDate").asText());
				product.setProductionDate(toLocalDate(date));
			} catch (ParseException e) {
				// do nothing
			}
		}

		if (node.has("expiryDate")) {
			try {
				Date date = dateFormat.parse(node.get("expiryDate").asText());
				product.setExpiryDate(toLocalDate(date));
			} catch (ParseException e) {
				// do nothing
			}
		}

		return product;
	}

	static Hazard convertHazard(JsonNode node) {
		Hazard hazard = new Hazard();

		if (node.has("hazardType")) {
			hazard.setType(node.get("hazardType").asText());
		}

		if (node.has("hazardName")) {
			hazard.setName(node.get("hazardName").asText());
		}

		if (node.has("hazardDescription")) {
			hazard.setDescription(node.get("hazardDescription").asText());
		}

		if (node.has("hazardUnit")) {
			hazard.setUnit(node.get("hazardUnit").asText());
		}

		if (node.has("adverseEffect")) {
			hazard.setAdverseEffect(node.get("adverseEffect").asText());
		}

		if (node.has("sourceOfContamination")) {
			hazard.setSourceOfContamination(node.get("sourceOfContamination").asText());
		}

		if (node.has("benchmarkDose")) {
			hazard.setBenchmarkDose(node.get("benchmarkDose").asText());
		}

		if (node.has("maximumResidueLimit")) {
			hazard.setMaximumResidueLimit(node.get("maximumResidueLimit").asText());
		}

		if (node.has("noObservedAdverseAffectLevel")) {
			hazard.setNoObservedAdverseAffectLevel(node.get("noObservedAdverseAffectLevel").asText());
		}

		if (node.has("lowestObservedAdverseAffectLevel")) {
			hazard.setLowestObservedAdverseAffectLevel(node.get("lowestObservedAdverseAffectLevel").asText());
		}

		if (node.has("acceptableOperatorExposureLevel")) {
			hazard.setAcceptableOperatorsExposureLevel(node.get("acceptableOperatorExposureLevel").asText());
		}

		if (node.has("acuteReferenceDose")) {
			hazard.setAcuteReferenceDose(node.get("acuteReferenceDose").asText());
		}

		if (node.has("acceptableDailyIntake")) {
			hazard.setAcceptableDailyIntake(node.get("acceptableDailyIntake").asText());
		}

		if (node.has("hazardIndSum")) {
			hazard.setIndSum(node.get("hazardIndSum").asText());
		}

		return hazard;
	}

	static PopulationGroup convertPopulationGroup(JsonNode node) {

		PopulationGroup group = new PopulationGroup();

		if (node.has("populationName")) {
			group.setName(node.get("populationName").asText());
		}

		if (node.has("targetPopulation")) {
			group.setTargetPopulation(node.get("targetPopulation").asText());
		}

		if (node.has("populationSpan")) {
			for (JsonNode child : node.get("populationSpan")) {
				group.addPopulationSpanItem(child.get("value").asText());
			}
		}

		if (node.has("populationDescription")) {
			for (JsonNode child : node.get("populationDescription")) {
				group.addPopulationDescriptionItem(child.get("value").asText());
			}
		}

		if (node.has("bmi")) {
			for (JsonNode child : node.get("bmi")) {
				group.addBmiItem(child.get("value").asText());
			}
		}

		if (node.has("specialDietGroups")) {
			for (JsonNode child : node.get("specialDietGroups")) {
				group.addSpecialDietGroupsItem(child.get("value").asText());
			}
		}

		if (node.has("region")) {
			for (JsonNode child : node.get("region")) {
				group.addRegionItem(child.get("value").asText());
			}
		}

		if (node.has("country")) {
			for (JsonNode child : node.get("country")) {
				group.addCountryItem(child.get("value").asText());
			}
		}

		if (node.has("populationRiskFactor")) {
			for (JsonNode child : node.get("populationRiskFactor")) {
				group.addPopulationRiskFactorItem(child.get("value").asText());
			}
		}

		if (node.has("season")) {
			for (JsonNode child : node.get("season")) {
				group.addSeasonItem(child.get("value").asText());
			}
		}

		if (node.has("populationRiskFactor")) {
			for (JsonNode child : node.get("populationRiskFactor")) {
				group.addPopulationRiskFactorItem(child.get("value").asText());
			}
		}

		if (node.has("season")) {
			for (JsonNode child : node.get("season")) {
				group.addSeasonItem(child.get("value").asText());
			}
		}

		if (node.has("populationGender")) {
			group.setPopulationGender(node.get("populationGender").asText());
		}

		if (node.has("patternConsumption")) {
			for (JsonNode child : node.get("patternConsumption")) {
				group.addPatternConsumptionItem(child.get("value").asText());
			}
		}

		if (node.has("populationAge")) {
			for (JsonNode child : node.get("populationAge")) {
				group.addPopulationAgeItem(child.get("value").asText());
			}
		}

		return group;
	}

	static Assay convertAssay(JsonNode node) {

		Assay assay = new Assay();

		if (node.has("assayName")) {
			assay.setName(node.get("assayName").asText());
		}

		if (node.has("assayDescription")) {
			assay.setDescription(node.get("assayDescription").asText());
		}

		if (node.has("percentageOfMoisture")) {
			assay.setMoisturePercentage(node.get("percentageOfMoisture").asText());
		}

		if (node.has("percentageOfFat")) {
			assay.setFatPercentage(node.get("percentageOfFat").asText());
		}

		if (node.has("limitOfDetection")) {
			assay.setDetectionLimit(node.get("limitOfDetection").asText());
		}

		if (node.has("limitOfQuantification")) {
			assay.setQuantificationLimit(node.get("limitOfQuantification").asText());
		}

		if (node.has("leftCensoredData")) {
			assay.setLeftCensoredData(node.get("leftCensoredData").asText());
		}

		if (node.has("rangeOfContamination")) {
			assay.setContaminationRange(node.get("rangeOfContamination").asText());
		}

		if (node.has("uncertaintyValue")) {
			assay.setUncertaintyValue(node.get("uncertaintyValue").asText());
		}

		return assay;
	}

	static Laboratory convertLaboratory(JsonNode node) {

		Laboratory laboratory = new Laboratory();

		if (node.has("laboratoryName")) {
			laboratory.setName(node.get("laboratoryName").asText());
		}

		if (node.has("laboratoryCountry")) {
			laboratory.setCountry(node.get("laboratoryCountry").asText());
		}

		if (node.has("laboratoryAccreditation")) {
			for (JsonNode child : node.get("laboratoryAccreditation")) {
				laboratory.addAccreditationItem(child.get("value").asText());
			}
		}

		return laboratory;
	}

	static StudySample convertStudySample(JsonNode node) {

		StudySample sample = new StudySample();

		if (node.has("sampleName")) {
			sample.setSampleName(node.get("sampleName").asText());
		}

		if (node.has("protocolOfSampleCollection")) {
			sample.setProtocolOfSampleCollection(node.get("protocolOfSampleCollection").asText());
		}

		if (node.has("samplingStrategy")) {
			sample.setSamplingStrategy(node.get("samplingStrategy").asText());
		}

		if (node.has("typeOfSamplingProgram")) {
			sample.setTypeOfSamplingProgram(node.get("typeOfSamplingProgram").asText());
		}

		if (node.has("samplingMethod")) {
			sample.setSamplingMethod(node.get("samplingMethod").asText());
		}

		if (node.has("samplingPlan")) {
			sample.setSamplingPlan(node.get("samplingPlan").asText());
		}

		if (node.has("samplingWeight")) {
			sample.setSamplingWeight(node.get("samplingWeight").asText());
		}

		if (node.has("samplingSize")) {
			sample.setSamplingSize(node.get("samplingSize").asText());
		}

		if (node.has("lotSizeUnit")) {
			sample.setLotSizeUnit(node.get("lotSizeUnit").asText());
		}

		if (node.has("samplingPoint")) {
			sample.setSamplingPoint(node.get("samplingPoint").asText());
		}

		return sample;
	}

	static DietaryAssessmentMethod convertDietaryAssessmentMethod(JsonNode node) {

		DietaryAssessmentMethod method = new DietaryAssessmentMethod();

		if (node.has("collectionTool")) {
			method.setCollectionTool(node.get("collectionTool").asText());
		}

		if (node.has("numberOfNonConsecutiveOneDay")) {
			method.setNumberOfNonConsecutiveOneDay(node.get("numberOfNonConsecutiveOneDay").asText());
		}

		if (node.has("softwareTool")) {
			method.setSoftwareTool(node.get("softwareTool").asText());
		}

		if (node.has("numberOfFoodItems")) {
			method.addNumberOfFoodItemsItem(node.get("numberOfFoodItems").asText());
		}

		if (node.has("recordTypes")) {
			method.addRecordTypesItem(node.get("recordTypes").asText());
		}

		if (node.has("foodDescriptors")) {
			method.addFoodDescriptorsItem(node.get("foodDescriptors").asText());
		}

		return method;
	}

	static Study convertStudy(JsonNode node) {

		Study study = new Study();

		if (node.has("studyIdentifier")) {
			study.setIdentifier(node.get("studyIdentifier").asText());
		}

		if (node.has("studyTitle")) {
			study.setTitle(node.get("studyTitle").asText());
		}

		if (node.has("studyDescription")) {
			study.setDescription(node.get("studyDescription").asText());
		}

		if (node.has("studyDesignType")) {
			study.setDesignType(node.get("studyDesignType").asText());
		}

		if (node.has("studyAssayMeasurementType")) {
			study.setAssayMeasurementType(node.get("studyAssayMeasurementType").asText());
		}

		if (node.has("studyAssayTechnologyType")) {
			study.setAssayTechnologyType(node.get("studyAssayTechnologyType").asText());
		}

		if (node.has("studyAssayTechnologyPlatform")) {
			study.setAssayTechnologyPlatform(node.get("studyAssayTechnologyPlatform").asText());
		}

		if (node.has("accreditationProcedureForTheAssayTechnology")) {
			study.setAccreditationProcedureForTheAssayTechnology(
					node.get("accreditationProcedureForTheAssayTechnology").asText());
		}

		if (node.has("studyProtocolName")) {
			study.setProtocolName(node.get("studyProtocolName").asText());
		}

		if (node.has("studyProtocolType")) {
			study.setProtocolType(node.get("studyProtocolType").asText());
		}

		if (node.has("studyProtocolDescription")) {
			study.setProtocolDescription(node.get("studyProtocolDescription").asText());
		}

		if (node.has("studyProtocolURI")) {
			study.setProtocolURI(node.get("studyProtocolURI").asText());
		}

		if (node.has("studyProtocolVersion")) {
			study.setProtocolVersion(node.get("studyProtocolVersion").asText());
		}

		if (node.has("studyProtocolParametersName")) {
			study.setProtocolParametersName(node.get("studyProtocolParametersName").asText());
		}

		if (node.has("studyProtocolComponentsName")) {
			study.setProtocolComponentsName(node.get("studyProtocolComponentsName").asText());
		}

		if (node.has("studyProtocolComponentsType")) {
			study.setProtocolComponentsType(node.get("studyProtocolComponentsType").asText());
		}

		return study;
	}

	static Parameter convertParameter(JsonNode node) {

		Parameter parameter = new Parameter();

		if (node.has("parameterID")) {
			parameter.setId(node.get("parameterID").asText());
		}

		if (node.has("parameterClassification")) {
			String classificationLiteral = node.get("parameterClassification").asText();
			parameter.setClassification(CLASSIF.get(classificationLiteral));
		}

		if (node.has("parameterName")) {
			parameter.setName(node.get("parameterName").asText());
		}

		if (node.has("parameterDescription")) {
			parameter.setDescription(node.get("parameterDescription").asText());
		}

		// TODO: parameterType

		if (node.has("parameterUnit")) {
			parameter.setUnit(node.get("parameterUnit").asText());
		}

		if (node.has("parameterUnitCategory")) {
			parameter.setUnitCategory(node.get("parameterUnitCategory").asText());
		}

		if (node.has("parameterDataType")) {
			String typeLiteral = node.get("parameterDataType").asText();
			parameter.setDataType(TYPES.get(typeLiteral));
		}

		if (node.has("parameterSource")) {
			parameter.setSource(node.get("parameterSource").asText());
		}

		if (node.has("parameterSubject")) {
			parameter.setSubject(node.get("parameterSubject").asText());
		}

		if (node.has("parameterDistribution")) {
			parameter.setDistribution(node.get("parameterDistribution").asText());
		}

		if (node.has("parameterValue")) {
			parameter.setValue(node.get("parameterValue").asText());
		}

		if (node.has("parameterVariabilitySubject")) {
			parameter.setVariabilitySubject(node.get("parameterVariabilitySubject").asText());
		}

		if (node.has("parameterValueMin")) {
			parameter.setMinValue(node.get("parameterValueMin").asText());
		}

		if (node.has("parameterValueMax")) {
			parameter.setMaxValue(node.get("parameterValueMax").asText());
		}

		if (node.has("parameterError")) {
			parameter.setError(node.get("parameterError").asText());
		}

		if (node.has("reference")) {
			parameter.setReference(convertReference(node.get("reference")));
		}

		return parameter;
	}

	static ModelEquation convertModelEquation(JsonNode node) {

		ModelEquation equation = new ModelEquation();

		if (node.has("modelEquationName")) {
			equation.setName(node.get("modelEquationName").asText());
		}

		if (node.has("modelEquationClass")) {
			equation.setModelEquationClass(node.get("modelEquationClass").asText());
		}

		if (node.has("modelEquation")) {
			equation.setModelEquation(node.get("modelEquation").asText());
		}

		if (node.has("reference")) {
			for (JsonNode child : node.get("reference")) {
				equation.addReferenceItem(convertReference(child));
			}
		}

		if (node.has("hypothesisOfTheModel")) {
			for (JsonNode child : node.get("hypothesisOfTheModel")) {
				equation.addModelHypothesisItem(child.get("value").asText());
			}
		}

		return equation;
	}

	static Exposure convertExposure(JsonNode node) {

		Exposure exposure = new Exposure();

		if (node.has("typeOfExposure")) {
			exposure.setType(node.get("typeOfExposure").asText());
		}

		if (node.has("uncertaintyEstimation")) {
			exposure.setUncertaintyEstimation(node.get("uncertaintyEstimation").asText());
		}

		if (node.has("methodologicalTreatmentOfLeftCensoredData")) {
			for (JsonNode child : node.get("methodologicalTreatmentOfLeftCensoredData")) {
				exposure.addTreatmentItem(child.get("value").asText());
			}
		}

		if (node.has("levelOfContaminationAfterLeftCensoredDataTreatment")) {
			for (JsonNode child : node.get("levelOfContaminationAfterLeftCensoredDataTreatment")) {
				exposure.addContaminationItem(child.get("value").asText());
			}
		}

		if (node.has("scenario")) {
			for (JsonNode child : node.get("scenario")) {
				exposure.addScenarioItem(child.get("value").asText());
			}
		}

		return exposure;
	}

	/**
	 * Internal map used to convert RIS types (literal) to 1.0.4 Reference types.
	 */
	private static Map<String, PublicationTypeEnum> PUBLICATION_TYPE;
	static {
		PUBLICATION_TYPE = new HashMap<>();

		PUBLICATION_TYPE.put(PublicationType.ABST.getLiteral(), PublicationTypeEnum.ABST);
		PUBLICATION_TYPE.put(PublicationType.ADVS.getLiteral(), PublicationTypeEnum.ADVS);
		PUBLICATION_TYPE.put(PublicationType.AGGR.getLiteral(), PublicationTypeEnum.AGGR);
		PUBLICATION_TYPE.put(PublicationType.ANCIENT.getLiteral(), PublicationTypeEnum.ANCIENT);
		PUBLICATION_TYPE.put(PublicationType.ART.getLiteral(), PublicationTypeEnum.ART);
		PUBLICATION_TYPE.put(PublicationType.BILL.getLiteral(), PublicationTypeEnum.BILL);
		PUBLICATION_TYPE.put(PublicationType.BLOG.getLiteral(), PublicationTypeEnum.BLOG);
		PUBLICATION_TYPE.put(PublicationType.BOOK.getLiteral(), PublicationTypeEnum.BOOK);
		PUBLICATION_TYPE.put(PublicationType.CASE.getLiteral(), PublicationTypeEnum.CASE);
		PUBLICATION_TYPE.put(PublicationType.CHAP.getLiteral(), PublicationTypeEnum.CHAP);
		PUBLICATION_TYPE.put(PublicationType.CHART.getLiteral(), PublicationTypeEnum.CHART);
		PUBLICATION_TYPE.put(PublicationType.CLSWK.getLiteral(), PublicationTypeEnum.CLSWK);
		PUBLICATION_TYPE.put(PublicationType.COMP.getLiteral(), PublicationTypeEnum.COMP);
		PUBLICATION_TYPE.put(PublicationType.CONF.getLiteral(), PublicationTypeEnum.CONF);
		PUBLICATION_TYPE.put(PublicationType.CPAPER.getLiteral(), PublicationTypeEnum.CPAPER);
		PUBLICATION_TYPE.put(PublicationType.CTLG.getLiteral(), PublicationTypeEnum.CTLG);
		PUBLICATION_TYPE.put(PublicationType.DATA.getLiteral(), PublicationTypeEnum.DATA);
		PUBLICATION_TYPE.put(PublicationType.DBASE.getLiteral(), PublicationTypeEnum.DBASE);
		PUBLICATION_TYPE.put(PublicationType.DICT.getLiteral(), PublicationTypeEnum.DICT);
		PUBLICATION_TYPE.put(PublicationType.EBOOK.getLiteral(), PublicationTypeEnum.EBOOK);
		PUBLICATION_TYPE.put(PublicationType.ECHAP.getLiteral(), PublicationTypeEnum.ECHAP);
		PUBLICATION_TYPE.put(PublicationType.EDBOOK.getLiteral(), PublicationTypeEnum.EDBOOK);
		PUBLICATION_TYPE.put(PublicationType.EJOUR.getLiteral(), PublicationTypeEnum.EJOUR);
		PUBLICATION_TYPE.put(PublicationType.ELECT.getLiteral(), PublicationTypeEnum.ELECT);
		PUBLICATION_TYPE.put(PublicationType.ENCYC.getLiteral(), PublicationTypeEnum.ENCYC);
		PUBLICATION_TYPE.put(PublicationType.EQUA.getLiteral(), PublicationTypeEnum.EQUA);
		PUBLICATION_TYPE.put(PublicationType.FIGURE.getLiteral(), PublicationTypeEnum.FIGURE);
		PUBLICATION_TYPE.put(PublicationType.GEN.getLiteral(), PublicationTypeEnum.GEN);
		PUBLICATION_TYPE.put(PublicationType.GOVDOC.getLiteral(), PublicationTypeEnum.GOVDOC);
		PUBLICATION_TYPE.put(PublicationType.GRANT.getLiteral(), PublicationTypeEnum.GRANT);
		PUBLICATION_TYPE.put(PublicationType.HEAR.getLiteral(), PublicationTypeEnum.HEAR);
		PUBLICATION_TYPE.put(PublicationType.ICOMM.getLiteral(), PublicationTypeEnum.ICOMM);
		PUBLICATION_TYPE.put(PublicationType.INPR.getLiteral(), PublicationTypeEnum.INPR);
		PUBLICATION_TYPE.put(PublicationType.JOUR.getLiteral(), PublicationTypeEnum.JOUR);
		PUBLICATION_TYPE.put(PublicationType.JFULL.getLiteral(), PublicationTypeEnum.JFULL);
		PUBLICATION_TYPE.put(PublicationType.LEGAL.getLiteral(), PublicationTypeEnum.LEGAL);
		PUBLICATION_TYPE.put(PublicationType.MANSCPT.getLiteral(), PublicationTypeEnum.MANSCPT);
		PUBLICATION_TYPE.put(PublicationType.MAP.getLiteral(), PublicationTypeEnum.MAP);
		PUBLICATION_TYPE.put(PublicationType.MGZN.getLiteral(), PublicationTypeEnum.MGZN);
		PUBLICATION_TYPE.put(PublicationType.MPCT.getLiteral(), PublicationTypeEnum.MPCT);
		PUBLICATION_TYPE.put(PublicationType.MULTI.getLiteral(), PublicationTypeEnum.MULTI);
		PUBLICATION_TYPE.put(PublicationType.MUSIC.getLiteral(), PublicationTypeEnum.MUSIC);
		// Typo in PublicationTypeEnum. It should be 'NEWS'
		PUBLICATION_TYPE.put(PublicationType.NEWS.getLiteral(), PublicationTypeEnum.NEW);
		PUBLICATION_TYPE.put(PublicationType.PAMP.getLiteral(), PublicationTypeEnum.PAMP);
		PUBLICATION_TYPE.put(PublicationType.PAT.getLiteral(), PublicationTypeEnum.PAT);
		PUBLICATION_TYPE.put(PublicationType.PCOMM.getLiteral(), PublicationTypeEnum.PCOMM);
		PUBLICATION_TYPE.put(PublicationType.RPRT.getLiteral(), PublicationTypeEnum.RPRT);
		PUBLICATION_TYPE.put(PublicationType.SER.getLiteral(), PublicationTypeEnum.SER);
		PUBLICATION_TYPE.put(PublicationType.SLIDE.getLiteral(), PublicationTypeEnum.SLIDE);
		PUBLICATION_TYPE.put(PublicationType.SOUND.getLiteral(), PublicationTypeEnum.SOUND);
		PUBLICATION_TYPE.put(PublicationType.STAND.getLiteral(), PublicationTypeEnum.STAND);
		PUBLICATION_TYPE.put(PublicationType.STAT.getLiteral(), PublicationTypeEnum.STAT);
		PUBLICATION_TYPE.put(PublicationType.THES.getLiteral(), PublicationTypeEnum.THES);
		PUBLICATION_TYPE.put(PublicationType.UNPB.getLiteral(), PublicationTypeEnum.UNPB);
		PUBLICATION_TYPE.put(PublicationType.VIDEO.getLiteral(), PublicationTypeEnum.VIDEO);
	}

	/** Internal map used to convert parameter classification literals to 1.0.4. */
	private static Map<String, ClassificationEnum> CLASSIF;
	static {
		CLASSIF = new HashMap<>();
		CLASSIF.put(metadata.ParameterClassification.INPUT.getLiteral(), ClassificationEnum.INPUT);
		CLASSIF.put(metadata.ParameterClassification.OUTPUT.getLiteral(), ClassificationEnum.OUTPUT);
		CLASSIF.put(metadata.ParameterClassification.CONSTANT.getLiteral(), ClassificationEnum.CONSTANT);
	}

	/** Internal map used to convert parameter types (literal) to 1.0.4. */
	public static Map<String, DataTypeEnum> TYPES;
	static {
		TYPES = new HashMap<>();

		TYPES.put(metadata.ParameterType.NULL.getLiteral(), DataTypeEnum.OBJECT);
		TYPES.put(metadata.ParameterType.INTEGER.getLiteral(), DataTypeEnum.INTEGER);
		TYPES.put(metadata.ParameterType.DOUBLE.getLiteral(), DataTypeEnum.DOUBLE);
		TYPES.put(metadata.ParameterType.NUMBER.getLiteral(), DataTypeEnum.NUMBER);
		TYPES.put(metadata.ParameterType.DATE.getLiteral(), DataTypeEnum.DATE);
		TYPES.put(metadata.ParameterType.FILE.getLiteral(), DataTypeEnum.FILE);
		TYPES.put(metadata.ParameterType.BOOLEAN.getLiteral(), DataTypeEnum.BOOLEAN);
		TYPES.put(metadata.ParameterType.VECTOR_OF_NUMBERS.getLiteral(), DataTypeEnum.VECTOROFNUMBERS);
		TYPES.put(metadata.ParameterType.VECTOR_OF_STRINGS.getLiteral(), DataTypeEnum.VECTOROFSTRINGS);
		TYPES.put(metadata.ParameterType.MATRIX_OF_NUMBERS.getLiteral(), DataTypeEnum.MATRIXOFNUMBERS);
		TYPES.put(metadata.ParameterType.MATRIX_OF_STRINGS.getLiteral(), DataTypeEnum.MATRIXOFSTRINGS);
		TYPES.put(metadata.ParameterType.OBJECT.getLiteral(), DataTypeEnum.OBJECT);
		TYPES.put(metadata.ParameterType.OTHER.getLiteral(), DataTypeEnum.OBJECT);
		TYPES.put(metadata.ParameterType.STRING.getLiteral(), DataTypeEnum.STRING);
	}
}
