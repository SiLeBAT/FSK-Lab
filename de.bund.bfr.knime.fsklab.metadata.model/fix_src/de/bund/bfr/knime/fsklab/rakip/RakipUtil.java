package de.bund.bfr.knime.fsklab.rakip;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.LocalDate;

import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;

import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;

public class RakipUtil {

	public static de.bund.bfr.metadata.swagger.GenericModelGeneralInformation convert(
			GeneralInformation deprecatedGI) {

		de.bund.bfr.metadata.swagger.GenericModelGeneralInformation generalInformation = new de.bund.bfr.metadata.swagger.GenericModelGeneralInformation();

		generalInformation.setName(deprecatedGI.name);
		generalInformation.setSource(deprecatedGI.source);
		generalInformation.setIdentifier(deprecatedGI.identifier);
		generalInformation.setCreationDate(toLocalDate(deprecatedGI.creationDate));

		for (Date date : deprecatedGI.modificationDate) {
			generalInformation.addModificationDateItem(toLocalDate(date));
		}

		generalInformation.rights(deprecatedGI.rights);
		generalInformation.setAvailability(Boolean.toString(deprecatedGI.isAvailable));
		if (deprecatedGI.url != null) {
			generalInformation.setUrl(deprecatedGI.url.toString());
		}
		generalInformation.setFormat(deprecatedGI.format);

		for (Record record : deprecatedGI.reference) {
			generalInformation.addReferenceItem(convert(record));
		}

		generalInformation.setLanguage(deprecatedGI.language);
		generalInformation.setSoftware(deprecatedGI.software);
		generalInformation.setLanguageWrittenIn(deprecatedGI.languageWrittenIn);

		if (deprecatedGI.modelCategory != null) {
			generalInformation.setModelCategory(convert(deprecatedGI.modelCategory));
		}

		generalInformation.setStatus(deprecatedGI.status);
		generalInformation.setObjective(deprecatedGI.objective);
		generalInformation.setDescription(deprecatedGI.description);

		return generalInformation;
	}

	public static de.bund.bfr.metadata.swagger.Reference convert(com.gmail.gcolaianni5.jris.bean.Record record) {

		de.bund.bfr.metadata.swagger.Reference reference = new de.bund.bfr.metadata.swagger.Reference();

		// Is reference description is not included in RIS
		reference.setIsReferenceDescription(false);

		if (record.getType() != null) {
			reference.setPublicationType(PUBLICATION_TYPE.get(record.getType()));
		}

		// Parse RIS date to LocalDate. Since both use ISO 8601 there is no need to add
		// the date format.
		final String publicationDate = record.getPrimaryDate();
		if (StringUtils.isNotEmpty(publicationDate)) {
			reference.setDate(LocalDate.parse(publicationDate));
		}

		reference.setDoi(record.getDoi());

		List<String> firstAuthors = record.getFirstAuthors();
		if (firstAuthors != null && !firstAuthors.isEmpty()) {
			reference.setAuthorList(String.join(";", firstAuthors));
		}

		reference.setTitle(record.getTitle());
		reference.setAbstract(record.getAbstr());
		reference.setVolume(record.getVolumeNumber());

		if (record.getIssueNumber() != null) {
			reference.setIssue(record.getIssueNumber().toString());
		}

		reference.setWebsite(record.getUrl());

		return reference;
	}

	public static de.bund.bfr.metadata.swagger.ModelCategory convert(ModelCategory deprecated) {
		de.bund.bfr.metadata.swagger.ModelCategory modelCategory = new de.bund.bfr.metadata.swagger.ModelCategory();

		modelCategory.setModelClass(deprecated.modelClass);
		modelCategory.setModelSubClass(deprecated.modelSubClass);
		modelCategory.setModelClassComment(deprecated.modelClassComment);
		modelCategory.setBasicProcess(deprecated.basicProcess);

		return modelCategory;
	}

	public static de.bund.bfr.metadata.swagger.GenericModelScope convert(Scope deprecated) {
		de.bund.bfr.metadata.swagger.GenericModelScope scope = new de.bund.bfr.metadata.swagger.GenericModelScope();

		if (deprecated.product != null) {
			scope.addProductItem(convert(deprecated.product));
		}

		if (deprecated.hazard != null) {
			scope.addHazardItem(convert(deprecated.hazard));
		}

		if (deprecated.populationGroup != null) {
			scope.addPopulationGroupItem(convert(deprecated.populationGroup));
		}

		scope.setGeneralComment(deprecated.generalComment);
		scope.setTemporalInformation(deprecated.temporalInformation);
		// TODO: spatialInformation

		return scope;
	}

	public static de.bund.bfr.metadata.swagger.Product convert(Product deprecated) {
		de.bund.bfr.metadata.swagger.Product product = new de.bund.bfr.metadata.swagger.Product();

		product.setName(deprecated.environmentName);
		product.setDescription(deprecated.environmentDescription);
		product.setUnit(deprecated.environmentUnit);
		product.setMethod(deprecated.productionMethod);
		product.setPackaging(deprecated.packaging);
		product.setTreatment(deprecated.productTreatment);
		product.setOriginCountry(deprecated.originCountry);
		product.setOriginArea(deprecated.originArea);
		product.setFisheriesArea(deprecated.fisheriesArea);

		if (deprecated.productionDate != null) {
			product.setProductionDate(toLocalDate(deprecated.productionDate));
		}

		if (deprecated.expirationDate != null) {
			product.setExpiryDate(toLocalDate(deprecated.expirationDate));
		}

		return product;
	}

	public static de.bund.bfr.metadata.swagger.Hazard convert(Hazard deprecated) {
		de.bund.bfr.metadata.swagger.Hazard hazard = new de.bund.bfr.metadata.swagger.Hazard();

		hazard.setType(deprecated.hazardType);
		hazard.setName(deprecated.hazardName);
		hazard.setDescription(deprecated.hazardDescription);
		hazard.setUnit(deprecated.hazardUnit);
		hazard.setAdverseEffect(deprecated.adverseEffect);
		hazard.setSourceOfContamination(deprecated.sourceOfContamination);
		hazard.setBenchmarkDose(deprecated.bmd);
		hazard.setMaximumResidueLimit(deprecated.mrl);
		hazard.setNoObservedAdverseAffectLevel(deprecated.noael);
		hazard.setLowestObservedAdverseAffectLevel(deprecated.loael);
		hazard.setAcceptableOperatorsExposureLevel(deprecated.aoel);
		hazard.setAcuteReferenceDose(deprecated.ard);
		hazard.setAcceptableDailyIntake(deprecated.adi);
		hazard.setIndSum(deprecated.hazardIndSum);

		return hazard;
	}

	public static de.bund.bfr.metadata.swagger.PopulationGroup convert(PopulationGroup deprecated) {
		de.bund.bfr.metadata.swagger.PopulationGroup pg = new de.bund.bfr.metadata.swagger.PopulationGroup();

		pg.setName(deprecated.populationName);
		pg.setTargetPopulation(deprecated.targetPopulation);
		pg.setPopulationSpan(deprecated.populationSpan);
		pg.setPopulationDescription(deprecated.populationDescription);
		pg.setPopulationAge(deprecated.populationAge);
		pg.setPopulationGender(deprecated.populationGender);
		pg.setBmi(deprecated.bmi);
		pg.setSpecialDietGroups(deprecated.specialDietGroups);
		pg.setPatternConsumption(deprecated.patternConsumption);
		pg.setRegion(deprecated.region);
		pg.setCountry(deprecated.country);
		pg.setPopulationRiskFactor(deprecated.populationRiskFactor);
		pg.setSeason(deprecated.season);

		return pg;
	}

	public static de.bund.bfr.metadata.swagger.GenericModelDataBackground convert(DataBackground deprecated) {
		de.bund.bfr.metadata.swagger.GenericModelDataBackground db = new de.bund.bfr.metadata.swagger.GenericModelDataBackground();

		if (deprecated.study != null) {
			db.setStudy(convert(deprecated.study));
		}

		if (deprecated.studySample != null) {
			db.addStudySampleItem(convert(deprecated.studySample));
		}

		if (deprecated.dietaryAssessmentMethod != null) {
			db.addDietaryAssessmentMethodItem(convert(deprecated.dietaryAssessmentMethod));
		}

		if (deprecated.laboratory != null) {
			db.addLaboratoryItem(convert(deprecated.laboratory));
		}

		if (deprecated.assay != null) {
			db.addAssayItem(convert(deprecated.assay));
		}

		return db;
	}

	public static de.bund.bfr.metadata.swagger.Study convert(Study deprecated) {
		de.bund.bfr.metadata.swagger.Study study = new de.bund.bfr.metadata.swagger.Study();

		study.setIdentifier(deprecated.id);
		study.setTitle(deprecated.title);
		study.setDescription(deprecated.description);
		study.setDesignType(deprecated.designType);
		study.setAssayMeasurementType(deprecated.measurementType);
		study.setAssayTechnologyType(deprecated.technologyType);
		study.setAssayTechnologyPlatform(deprecated.technologyPlatform);
		study.setAccreditationProcedureForTheAssayTechnology(deprecated.accreditationProcedure);
		study.setProtocolName(deprecated.protocolName);
		study.setProtocolType(deprecated.protocolType);
		study.setProtocolDescription(deprecated.protocolDescription);
		if (deprecated.protocolUri != null) {
			study.setProtocolURI(deprecated.protocolUri.toString());
		}
		study.setProtocolVersion(deprecated.protocolVersion);
		study.setProtocolParametersName(deprecated.parametersName);
		study.setProtocolComponentsName(deprecated.componentsName);
		study.setProtocolComponentsType(deprecated.componentsType);

		return study;
	}

	public static de.bund.bfr.metadata.swagger.StudySample convert(StudySample deprecated) {
		de.bund.bfr.metadata.swagger.StudySample studySample = new de.bund.bfr.metadata.swagger.StudySample();
		studySample.setSampleName(deprecated.sample);
		studySample.setProtocolOfSampleCollection(deprecated.collectionProtocol);
		studySample.setSamplingStrategy(deprecated.samplingStrategy);
		studySample.setTypeOfSamplingProgram(deprecated.samplingProgramType);
		studySample.setSamplingMethod(deprecated.samplingMethod);
		studySample.setSamplingPlan(deprecated.samplingPlan);
		studySample.setSamplingWeight(deprecated.samplingWeight);
		studySample.setSamplingSize(deprecated.samplingSize);
		studySample.setLotSizeUnit(deprecated.lotSizeUnit);
		studySample.samplingPoint(deprecated.samplingPoint);

		return studySample;
	}

	public static de.bund.bfr.metadata.swagger.DietaryAssessmentMethod convert(DietaryAssessmentMethod deprecated) {
		de.bund.bfr.metadata.swagger.DietaryAssessmentMethod method = new de.bund.bfr.metadata.swagger.DietaryAssessmentMethod();
		method.setCollectionTool(deprecated.collectionTool);
		method.setNumberOfNonConsecutiveOneDay(Integer.toString(deprecated.numberOfNonConsecutiveOneDay));
		method.setSoftwareTool(deprecated.softwareTool);
		method.setNumberOfFoodItems(deprecated.numberOfFoodItems);
		method.setRecordTypes(deprecated.recordTypes);
		method.setFoodDescriptors(deprecated.foodDescriptors);

		return method;
	}

	public static de.bund.bfr.metadata.swagger.Laboratory convert(Laboratory deprecated) {
		de.bund.bfr.metadata.swagger.Laboratory laboratory = new de.bund.bfr.metadata.swagger.Laboratory();

		if (StringUtils.isNotEmpty(deprecated.accreditation)) {
			laboratory.addAccreditationItem(deprecated.accreditation);
		}

		laboratory.setName(deprecated.name);
		laboratory.country(deprecated.country);

		return laboratory;
	}

	public static de.bund.bfr.metadata.swagger.Assay convert(Assay deprecated) {
		de.bund.bfr.metadata.swagger.Assay assay = new de.bund.bfr.metadata.swagger.Assay();
		assay.setName(deprecated.name);
		assay.setDescription(deprecated.description);
		assay.setMoisturePercentage(deprecated.moisturePercentage);
		assay.setFatPercentage(deprecated.fatPercentage);
		assay.setDetectionLimit(deprecated.detectionLimit);
		assay.setQuantificationLimit(deprecated.quantificationLimit);
		assay.setLeftCensoredData(deprecated.leftCensoredData);
		assay.setContaminationRange(deprecated.contaminationRange);
		assay.setUncertaintyValue(deprecated.uncertaintyValue);

		return assay;
	}

	public static de.bund.bfr.metadata.swagger.GenericModelModelMath convert(ModelMath deprecated) {
		de.bund.bfr.metadata.swagger.GenericModelModelMath math = new de.bund.bfr.metadata.swagger.GenericModelModelMath();

		deprecated.parameter.stream().map(RakipUtil::convert).forEach(math::addParameterItem);

		de.bund.bfr.metadata.swagger.QualityMeasures measures = new de.bund.bfr.metadata.swagger.QualityMeasures();
		measures.setSse(new BigDecimal(deprecated.sse));
		measures.setMse(new BigDecimal(deprecated.mse));
		measures.setRmse(new BigDecimal(deprecated.rmse));
		measures.setAic(new BigDecimal(deprecated.aic));
		measures.setBic(new BigDecimal(deprecated.bic));
		math.addQualityMeasuresItem(measures);

		deprecated.modelEquation.stream().map(RakipUtil::convert).forEach(math::addModelEquationItem);
		math.setFittingProcedure(deprecated.fittingProcedure);

		if (deprecated.exposure != null) {
			math.addExposureItem(convert(deprecated.exposure));
		}

		math.setEvent(deprecated.event);

		return math;
	}

	public static de.bund.bfr.metadata.swagger.Parameter convert(Parameter deprecated) {
		de.bund.bfr.metadata.swagger.Parameter parameter = new de.bund.bfr.metadata.swagger.Parameter();
		parameter.setId(deprecated.id);
		parameter.setClassification(CLASSIF.get(deprecated.classification));
		parameter.setName(deprecated.name);
		parameter.setDescription(deprecated.description);
		parameter.setUnit(deprecated.unit);
		parameter.setUnitCategory(deprecated.unitCategory);
		parameter.setDataType(TYPES.get(deprecated.dataType));
		parameter.setSource(deprecated.source);
		parameter.setSubject(deprecated.subject);
		parameter.setDistribution(deprecated.distribution);
		parameter.setValue(deprecated.value);
		// String reference cannot be converted to Reference object.
		parameter.setVariabilitySubject(deprecated.variabilitySubject);
		parameter.setMinValue(deprecated.minValue);
		parameter.setMaxValue(deprecated.maxValue);

		if (deprecated.error != null) {
			parameter.setError(deprecated.error.toString());
		}

		return parameter;
	}

	public static de.bund.bfr.metadata.swagger.ModelEquation convert(ModelEquation deprecated) {
		de.bund.bfr.metadata.swagger.ModelEquation equation = new de.bund.bfr.metadata.swagger.ModelEquation();
		equation.setName(deprecated.equationName);
		equation.setModelEquationClass(deprecated.equationClass);
		deprecated.equationReference.stream().map(RakipUtil::convert).forEach(equation::addReferenceItem);
		equation.setModelEquation(deprecated.equation);

		return equation;
	}

	public static de.bund.bfr.metadata.swagger.Exposure convert(Exposure deprecated) {
		de.bund.bfr.metadata.swagger.Exposure exposure = new de.bund.bfr.metadata.swagger.Exposure();

		if (StringUtils.isNotEmpty(deprecated.treatment)) {
			exposure.addTreatmentItem(deprecated.treatment);
		}

		if (StringUtils.isNotEmpty(deprecated.contaminationLevel)) {
			exposure.addContaminationItem(deprecated.contaminationLevel);
		}

		exposure.setType(deprecated.exposureType);

		if (StringUtils.isNotEmpty(deprecated.scenario)) {
			exposure.addScenarioItem(deprecated.scenario);
		}

		exposure.setUncertaintyEstimation(deprecated.uncertaintyEstimation);

		return exposure;
	}

	/** Internal map used to convert RIS types to 1.0.4 Reference types. */
	private static Map<Type, PublicationTypeEnum> PUBLICATION_TYPE;
	static {
		PUBLICATION_TYPE = new HashMap<>();

		PUBLICATION_TYPE.put(Type.ABST, PublicationTypeEnum.ABST);
		PUBLICATION_TYPE.put(Type.ADVS, PublicationTypeEnum.ADVS);
		PUBLICATION_TYPE.put(Type.AGGR, PublicationTypeEnum.AGGR);
		PUBLICATION_TYPE.put(Type.ANCIENT, PublicationTypeEnum.ANCIENT);
		PUBLICATION_TYPE.put(Type.ART, PublicationTypeEnum.ART);
		PUBLICATION_TYPE.put(Type.BILL, PublicationTypeEnum.BILL);
		PUBLICATION_TYPE.put(Type.BLOG, PublicationTypeEnum.BLOG);
		PUBLICATION_TYPE.put(Type.BOOK, PublicationTypeEnum.BOOK);
		PUBLICATION_TYPE.put(Type.CASE, PublicationTypeEnum.CASE);
		PUBLICATION_TYPE.put(Type.CHAP, PublicationTypeEnum.CHAP);
		PUBLICATION_TYPE.put(Type.CHART, PublicationTypeEnum.CHART);
		PUBLICATION_TYPE.put(Type.CLSWK, PublicationTypeEnum.CLSWK);
		PUBLICATION_TYPE.put(Type.COMP, PublicationTypeEnum.COMP);
		PUBLICATION_TYPE.put(Type.CONF, PublicationTypeEnum.CONF);
		PUBLICATION_TYPE.put(Type.CPAPER, PublicationTypeEnum.CPAPER);
		PUBLICATION_TYPE.put(Type.CTLG, PublicationTypeEnum.CTLG);
		PUBLICATION_TYPE.put(Type.DATA, PublicationTypeEnum.DATA);
		PUBLICATION_TYPE.put(Type.DBASE, PublicationTypeEnum.DBASE);
		PUBLICATION_TYPE.put(Type.DICT, PublicationTypeEnum.DICT);
		PUBLICATION_TYPE.put(Type.EBOOK, PublicationTypeEnum.EBOOK);
		PUBLICATION_TYPE.put(Type.ECHAP, PublicationTypeEnum.ECHAP);
		PUBLICATION_TYPE.put(Type.EDBOOK, PublicationTypeEnum.EDBOOK);
		PUBLICATION_TYPE.put(Type.EJOUR, PublicationTypeEnum.EJOUR);
		// Typo in PublicationTypeEnum. It should be 'ELEC'
		PUBLICATION_TYPE.put(Type.ELEC, PublicationTypeEnum.ELECT);
		PUBLICATION_TYPE.put(Type.ENCYC, PublicationTypeEnum.ENCYC);
		PUBLICATION_TYPE.put(Type.EQUA, PublicationTypeEnum.EQUA);
		PUBLICATION_TYPE.put(Type.FIGURE, PublicationTypeEnum.FIGURE);
		PUBLICATION_TYPE.put(Type.GEN, PublicationTypeEnum.GEN);
		PUBLICATION_TYPE.put(Type.GOVDOC, PublicationTypeEnum.GOVDOC);
		PUBLICATION_TYPE.put(Type.GRANT, PublicationTypeEnum.GRANT);
		PUBLICATION_TYPE.put(Type.HEAR, PublicationTypeEnum.HEAR);
		PUBLICATION_TYPE.put(Type.ICOMM, PublicationTypeEnum.ICOMM);
		PUBLICATION_TYPE.put(Type.INPR, PublicationTypeEnum.INPR);
		PUBLICATION_TYPE.put(Type.JOUR, PublicationTypeEnum.JOUR);
		PUBLICATION_TYPE.put(Type.JFULL, PublicationTypeEnum.JFULL);
		PUBLICATION_TYPE.put(Type.LEGAL, PublicationTypeEnum.LEGAL);
		PUBLICATION_TYPE.put(Type.MANSCPT, PublicationTypeEnum.MANSCPT);
		PUBLICATION_TYPE.put(Type.MAP, PublicationTypeEnum.MAP);
		PUBLICATION_TYPE.put(Type.MGZN, PublicationTypeEnum.MGZN);
		PUBLICATION_TYPE.put(Type.MPCT, PublicationTypeEnum.MPCT);
		PUBLICATION_TYPE.put(Type.MULTI, PublicationTypeEnum.MULTI);
		PUBLICATION_TYPE.put(Type.MUSIC, PublicationTypeEnum.MUSIC);
		// Typo in PublicationTypeEnum. It should be 'NEWS'
		PUBLICATION_TYPE.put(Type.NEWS, PublicationTypeEnum.NEW);
		PUBLICATION_TYPE.put(Type.PAMP, PublicationTypeEnum.PAMP);
		PUBLICATION_TYPE.put(Type.PAT, PublicationTypeEnum.PAT);
		PUBLICATION_TYPE.put(Type.PCOMM, PublicationTypeEnum.PCOMM);
		PUBLICATION_TYPE.put(Type.RPRT, PublicationTypeEnum.RPRT);
		PUBLICATION_TYPE.put(Type.SER, PublicationTypeEnum.SER);
		PUBLICATION_TYPE.put(Type.SLIDE, PublicationTypeEnum.SLIDE);
		PUBLICATION_TYPE.put(Type.SOUND, PublicationTypeEnum.SOUND);
		PUBLICATION_TYPE.put(Type.STAND, PublicationTypeEnum.STAND);
		PUBLICATION_TYPE.put(Type.STAT, PublicationTypeEnum.STAT);
		PUBLICATION_TYPE.put(Type.THES, PublicationTypeEnum.THES);
		PUBLICATION_TYPE.put(Type.UNPB, PublicationTypeEnum.UNPB);
		PUBLICATION_TYPE.put(Type.VIDEO, PublicationTypeEnum.VIDEO);
	}

	/** Internal map used to convert parameter classification to 1.0.4. */
	public static Map<Parameter.Classification, de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum> CLASSIF;
	static {
		CLASSIF = new HashMap<>();

		CLASSIF.put(Parameter.Classification.input, de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.INPUT);
		CLASSIF.put(Parameter.Classification.output, de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.OUTPUT);
		CLASSIF.put(Parameter.Classification.constant,
				de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.CONSTANT);
	}

	/** Internal map used to convert parameter types to 1.0.4. */
	public static Map<Parameter.DataTypes, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum> TYPES;
	static {
		TYPES = new HashMap<>();

		TYPES.put(Parameter.DataTypes.Integer, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.INTEGER);
		TYPES.put(Parameter.DataTypes.Double, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.DOUBLE);
		TYPES.put(Parameter.DataTypes.Number, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.NUMBER);
		TYPES.put(Parameter.DataTypes.Date, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.DATE);
		TYPES.put(Parameter.DataTypes.File, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.FILE);
		TYPES.put(Parameter.DataTypes.Boolean, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.BOOLEAN);
		TYPES.put(Parameter.DataTypes.VectorOfNumbers,
				de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.VECTOROFNUMBERS);
		TYPES.put(Parameter.DataTypes.VectorOfStrings,
				de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.VECTOROFSTRINGS);
		TYPES.put(Parameter.DataTypes.MatrixOfNumbers,
				de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.MATRIXOFNUMBERS);
		TYPES.put(Parameter.DataTypes.MatrixOfStrings,
				de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.MATRIXOFSTRINGS);
		TYPES.put(Parameter.DataTypes.Other, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.OBJECT);
		TYPES.put(Parameter.DataTypes.Object, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.OBJECT);
	}

	@SuppressWarnings("deprecation")
	private static LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}
}
