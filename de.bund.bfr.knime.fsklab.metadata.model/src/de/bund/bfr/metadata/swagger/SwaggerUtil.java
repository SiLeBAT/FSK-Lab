package de.bund.bfr.metadata.swagger;



import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.threeten.bp.LocalDate;

import com.gmail.gcolaianni5.jris.bean.Type;

import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;
import metadata.StringObject;

public class SwaggerUtil {

	public static de.bund.bfr.metadata.swagger.GenericModelGeneralInformation convert(metadata.GeneralInformation deprecatedGeneralInformation) {
		// TODO Auto-generated method stub
		GenericModelGeneralInformation swaggerGI = new GenericModelGeneralInformation();

	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getName())) {
	      swaggerGI.setName(deprecatedGeneralInformation.getName());
	    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getSource())) {
		      swaggerGI.setSource(deprecatedGeneralInformation.getSource());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getDescription())) {
		      swaggerGI.setDescription(deprecatedGeneralInformation.getDescription());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getIdentifier())) {
		      swaggerGI.setIdentifier(deprecatedGeneralInformation.getIdentifier());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getLanguage())) {
		      swaggerGI.setLanguage(deprecatedGeneralInformation.getLanguage());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getLanguageWrittenIn())) {
		      swaggerGI.setLanguageWrittenIn(deprecatedGeneralInformation.getLanguageWrittenIn());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getObjective())) {
		      swaggerGI.setObjective(deprecatedGeneralInformation.getObjective());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getRights())) {
		      swaggerGI.setRights(deprecatedGeneralInformation.getRights());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getSoftware())) {
		      swaggerGI.setSoftware(deprecatedGeneralInformation.getSoftware());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getSource())) {
		      swaggerGI.setSource(deprecatedGeneralInformation.getSource());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getStatus())) {
		      swaggerGI.setStatus(deprecatedGeneralInformation.getStatus());
		    }
	    if (StringUtils.isNotEmpty(deprecatedGeneralInformation.getFormat())) {
		      swaggerGI.setFormat(deprecatedGeneralInformation.getFormat());
		    }
	    
	   
	    swaggerGI.setAvailability(Boolean.toString(deprecatedGeneralInformation.isAvailable()));
	    for (metadata.Reference record : deprecatedGeneralInformation.getReference()) {
			swaggerGI.addReferenceItem(convert(record));
		}
	    if (deprecatedGeneralInformation.getCreationDate() != null) {
	      swaggerGI.setCreationDate(toLocalDate(deprecatedGeneralInformation.getCreationDate()));
	    }
		for (metadata.ModificationDate date : deprecatedGeneralInformation.getModificationdate()) {
			swaggerGI.addModificationDateItem(toLocalDate(date.getValue()));
		}
		
		if (deprecatedGeneralInformation.getModelCategory() != null) {
			swaggerGI.setModelCategory(convert(deprecatedGeneralInformation.getModelCategory().get(0)));
		}
		
		
		
		if (deprecatedGeneralInformation.getAuthor() != null) {
			swaggerGI.addAuthorItem(convert(deprecatedGeneralInformation.getAuthor()));
		}
		
		if (deprecatedGeneralInformation.getCreators() != null) {
			for(metadata.Contact creator : deprecatedGeneralInformation.getCreators()) {
				swaggerGI.addCreatorItem(convert(creator));
			}
		}
	
		
		return swaggerGI; 
	}
	/**
	 * TODO: replace {@link #convert(ModelCategory)}
	 * Convert deprecated ModelCategory to 1.0.4
	 */
	public static de.bund.bfr.metadata.swagger.ModelCategory convert(metadata.ModelCategory deprecated) {
		de.bund.bfr.metadata.swagger.ModelCategory modelCategory = new de.bund.bfr.metadata.swagger.ModelCategory();
		
		modelCategory.setModelClass(deprecated.getModelClass());
		List<String> subs = new ArrayList<String>();
		for(StringObject sub : deprecated.getModelSubClass()) {
			subs.add(sub.getValue());
		}
		modelCategory.setModelSubClass(subs);
		
		modelCategory.setModelClassComment(deprecated.getModelClassComment());
		modelCategory.setBasicProcess( Collections.singletonList(deprecated.getBasicProcess()));
		
		return modelCategory;
	}
	
	
	public static de.bund.bfr.metadata.swagger.Reference convert(metadata.Reference record) {

		de.bund.bfr.metadata.swagger.Reference reference = new de.bund.bfr.metadata.swagger.Reference();

		// Is reference description is not included in RIS
		reference.setIsReferenceDescription(false);
		
		if (record.getPublicationType() != null) {
			reference.addPublicationTypeItem(PUBLICATION_TYPE.get(record.getPublicationType()));
		}

		// Parse RIS date to LocalDate. Since both use ISO 8601 there is no need to add
		// the date format.
		
		reference.setDate(toLocalDate(record.getPublicationDate()));
		
		reference.setDoi(record.getDoi());

		reference.setAuthorList(record.getAuthorList());
		
		reference.setTitle(record.getPublicationTitle());
		reference.setAbstract(record.getPublicationAbstract());
		reference.setVolume(Integer.toString(record.getPublicationVolume()));

		
		reference.setIssue(Integer.toString(record.getPublicationIssue()));
		
		reference.setWebsite(record.getPublicationWebsite());

		return reference;
	}
	
	
	public static de.bund.bfr.metadata.swagger.Contact convert(metadata.Contact deprecated) {
		
		de.bund.bfr.metadata.swagger.Contact contact = new de.bund.bfr.metadata.swagger.Contact();
		
		if(StringUtils.isNotEmpty(deprecated.getCountry()))
			contact.setCountry(deprecated.getCountry());
		if(StringUtils.isNotEmpty(deprecated.getEmail()))
			contact.setEmail(deprecated.getEmail());
		if(StringUtils.isNotEmpty(deprecated.getFamilyName()))
			contact.setFamilyName(deprecated.getFamilyName());
		if(StringUtils.isNotEmpty(deprecated.getGender()))
			contact.setGender(deprecated.getGender());
		if(StringUtils.isNotEmpty(deprecated.getGivenName()))
			contact.setGivenName(deprecated.getGivenName());
		if(StringUtils.isNotEmpty(deprecated.getNote()))
			contact.setNote(deprecated.getNote());
		if(StringUtils.isNotEmpty(deprecated.getOrganization()))
			contact.setOrganization(deprecated.getOrganization());
		if(StringUtils.isNotEmpty(deprecated.getRegion()))
			contact.setRegion(deprecated.getRegion());
		if(StringUtils.isNotEmpty(deprecated.getStreetAddress()))
			contact.setStreetAddress(deprecated.getStreetAddress());
		if(StringUtils.isNotEmpty(deprecated.getTelephone()))
			contact.setTelephone(deprecated.getTelephone());
		if(StringUtils.isNotEmpty(deprecated.getTimeZone()))
			contact.setTimeZone(deprecated.getTimeZone());
		if(StringUtils.isNotEmpty(deprecated.getTitle()))
			contact.setTitle(deprecated.getTitle());
		if(StringUtils.isNotEmpty(deprecated.getZipCode()))
			contact.setZipCode(deprecated.getZipCode());
		
		
		return contact;
	}
	
	
	public static de.bund.bfr.metadata.swagger.GenericModelModelMath convert(metadata.ModelMath emfMM){
		
		de.bund.bfr.metadata.swagger.GenericModelModelMath swaggerMM = new de.bund.bfr.metadata.swagger.GenericModelModelMath();
		
		//Events
		if(emfMM.getEvent() != null) {
			for (StringObject e : emfMM.getEvent()) {
				swaggerMM.addEventItem(e.getValue());
			}
		}
		
		//Exposure
		if(emfMM.getExposure() != null) {
			swaggerMM.addExposureItem(convert(emfMM.getExposure()));
		}
		
		//model exuation
		if(emfMM.getModelEquation() != null) {
			for(metadata.ModelEquation equation : emfMM.getModelEquation()) {
				swaggerMM.addModelEquationItem(convert(equation));				
			}
		}
		//parameter
		if(emfMM.getParameter() != null) {
			for(metadata.Parameter parameter : emfMM.getParameter()) {
				swaggerMM.addParameterItem(convert(parameter));
			}
		}
		
		
		//quality measures
		if(emfMM.getQualityMeasures() != null) {
			for(StringObject item : emfMM.getQualityMeasures()) {
				de.bund.bfr.metadata.swagger.QualityMeasures qm = new de.bund.bfr.metadata.swagger.QualityMeasures();

				JsonObject measures = Json.createReader(new StringReader(item.getValue())).readObject();
				if(measures.containsKey("SSE"))
					qm.setSSE(measures.getJsonNumber("SSE").bigDecimalValue());
				if(measures.containsKey("AIC"))
					qm.setAIC(measures.getJsonNumber("AIC").bigDecimalValue());
				if(measures.containsKey("BIC"))
					qm.setBIC(measures.getJsonNumber("BIC").bigDecimalValue());
				if(measures.containsKey("MSE"))
					qm.setMSE(measures.getJsonNumber("MSE").bigDecimalValue());
				if(measures.containsKey("RMSE"))
					qm.setRMSE(measures.getJsonNumber("RMSE").bigDecimalValue());
				if(measures.containsKey("rsquared"))
					qm.setRsquared(measures.getJsonNumber("rsquared").bigDecimalValue());
				
				swaggerMM.addQualityMeasuresItem(qm);		
			}
		}
		
		//fittingProcedure
		if(StringUtils.isNotEmpty(emfMM.getFittingProcedure()))
			swaggerMM.setFittingProcedure(emfMM.getFittingProcedure());
		
		return swaggerMM;
	}
	public static de.bund.bfr.metadata.swagger.Parameter convert(metadata.Parameter deprecated){
		
		de.bund.bfr.metadata.swagger.Parameter parameter = new de.bund.bfr.metadata.swagger.Parameter();
		
		if(StringUtils.isNotEmpty(deprecated.getParameterDescription()))
			parameter.setDescription(deprecated.getParameterDescription());
		//TODO: PARAMETER TYPE ENUM
		//parameter.setDataType(deprecated.getParameterDataType());
		if(StringUtils.isNotEmpty(deprecated.getParameterDescription()))
			parameter.setDescription(deprecated.getParameterDescription());
		if(StringUtils.isNotEmpty(deprecated.getParameterDistribution()))
			parameter.setDistribution(deprecated.getParameterDistribution());
		if(StringUtils.isNotEmpty(deprecated.getParameterError()))
			parameter.setError(deprecated.getParameterError());
		if(StringUtils.isNotEmpty(deprecated.getParameterID()))
			parameter.setId(deprecated.getParameterID());
		if(StringUtils.isNotEmpty(deprecated.getParameterValueMax()))
			parameter.setMaxValue(deprecated.getParameterValueMax());
		if(StringUtils.isNotEmpty(deprecated.getParameterValueMin()))
			parameter.setMinValue(deprecated.getParameterValueMin());
		if(StringUtils.isNotEmpty(deprecated.getParameterName()))
			parameter.setName(deprecated.getParameterName());
		if(deprecated.getReference() != null)
			parameter.setReference(convert(deprecated.getReference()));
		if(StringUtils.isNotEmpty(deprecated.getParameterSource()))
			parameter.setSource(deprecated.getParameterSource());
		if(StringUtils.isNotEmpty(deprecated.getParameterSubject()))
			parameter.setSubject(deprecated.getParameterSubject());
		if(StringUtils.isNotEmpty(deprecated.getParameterUnit()))
			parameter.setUnit(deprecated.getParameterUnit());
		if(StringUtils.isNotEmpty(deprecated.getParameterUnitCategory()))
			parameter.setUnitCategory(deprecated.getParameterUnitCategory());
		if(StringUtils.isNotEmpty(deprecated.getParameterValue()))
			parameter.setValue(deprecated.getParameterValue());
		if(StringUtils.isNotEmpty(deprecated.getParameterVariabilitySubject()))
			parameter.setVariabilitySubject(deprecated.getParameterVariabilitySubject());
		
		
		return parameter;
	}
	
	public static de.bund.bfr.metadata.swagger.ModelEquation convert(metadata.ModelEquation deprecated){
		
		de.bund.bfr.metadata.swagger.ModelEquation modelEq = new de.bund.bfr.metadata.swagger.ModelEquation();
		
		//hypothesis
		if(deprecated.getHypothesisOfTheModel() != null) {
			for(StringObject item : deprecated.getHypothesisOfTheModel() ) {
				modelEq.addModelHypothesisItem(item.getValue());		
			}
		}
		
		//reference
		if(deprecated.getReference() != null) {
			for(metadata.Reference item : deprecated.getReference()) {
				modelEq.addReferenceItem(convert(item));		
			}
		}
		
		if(StringUtils.isNotEmpty(deprecated.getModelEquation()))
			modelEq.setModelEquation(deprecated.getModelEquation());
		
		
		
		return modelEq;
	}
	public static de.bund.bfr.metadata.swagger.Exposure convert(metadata.Exposure deprecated){
		
		de.bund.bfr.metadata.swagger.Exposure exposure = new de.bund.bfr.metadata.swagger.Exposure();
		//contamination
		if(deprecated.getLevelOfContaminationAfterLeftCensoredDataTreatment() != null) {
			for(StringObject item : deprecated.getLevelOfContaminationAfterLeftCensoredDataTreatment()) {
				exposure.addContaminationItem(item.getValue());
			}
		}
		
		//scenario
		if(deprecated.getScenario() != null) {
			for(StringObject item : deprecated.getScenario()) {
				exposure.addScenarioItem(item.getValue());
			}
		}
		//treatment
		if(deprecated.getMethodologicalTreatmentOfLeftCensoredData() != null) {
			for(StringObject item : deprecated.getMethodologicalTreatmentOfLeftCensoredData()) {
				exposure.addTreatmentItem(item.getValue());
			}
		}
		if(StringUtils.isNotEmpty(deprecated.getTypeOfExposure()))
			exposure.setType(deprecated.getTypeOfExposure());
		if(StringUtils.isNotEmpty(deprecated.getUncertaintyEstimation()))
			exposure.setUncertaintyEstimation(deprecated.getUncertaintyEstimation());
		
		return exposure;
	}

	public static de.bund.bfr.metadata.swagger.GenericModelScope convert(metadata.Scope emfScope){
		de.bund.bfr.metadata.swagger.GenericModelScope swaggerS = new de.bund.bfr.metadata.swagger.GenericModelScope();
		
		//hazard
		if(emfScope.getHazard() != null) {
			for(metadata.Hazard item : emfScope.getHazard()) {
				swaggerS.addHazardItem(convert(item));		
			}
		}
		//populationGroup
		if(emfScope.getPopulationGroup() != null) {
			for(metadata.PopulationGroup item : emfScope.getPopulationGroup()) {
				swaggerS.addPopulationGroupItem(convert(item));		
			}
		}
		//product
		if(emfScope.getProduct() != null) {
			for(metadata.Product item : emfScope.getProduct()) {
				swaggerS.addProductItem(convert(item));
			}
		}
		
		//TODO: SPACIAL INFORMATION
//		if(emfScope.getSpatialInformation() != null) {
//			metadata.SpatialInformation sp = emfScope.getSpatialInformation();
//			swaggerS.addSpatialInformationItem(sp.get)
//		}
//		swaggerS.get
		if(StringUtils.isNotEmpty(emfScope.getGeneralComment()))
			swaggerS.setGeneralComment(emfScope.getGeneralComment());
		if(StringUtils.isNotEmpty(emfScope.getTemporalInformation()))
			swaggerS.setTemporalInformation(emfScope.getTemporalInformation());
		return swaggerS;
	}
	public static de.bund.bfr.metadata.swagger.Product convert(metadata.Product deprecated){
		
		de.bund.bfr.metadata.swagger.Product product = new de.bund.bfr.metadata.swagger.Product();
		//methodItem
		if(StringUtils.isNotEmpty(deprecated.getProductionMethod()))
			product.addMethodItem(deprecated.getProductionMethod());
		
		//packaging
		if(StringUtils.isNotEmpty(deprecated.getPackaging()))
			product.addPackagingItem(deprecated.getPackaging());
		//treatment
		if(StringUtils.isNotEmpty(deprecated.getProductTreatment()))
			product.addTreatmentItem(deprecated.getProductTreatment());
		
		if(StringUtils.isNotEmpty(deprecated.getProductDescription()))
			product.setDescription(deprecated.getProductDescription());
		if(deprecated.getExpiryDate() != null)
			product.setExpiryDate(toLocalDate(deprecated.getExpiryDate()));
		if(StringUtils.isNotEmpty(deprecated.getFisheriesArea()))
			product.setFisheriesArea(deprecated.getFisheriesArea());
		if(StringUtils.isNotEmpty(deprecated.getProductName()))
			product.setName(deprecated.getProductName());
		if(StringUtils.isNotEmpty(deprecated.getOriginArea()))
			product.setOriginArea(deprecated.getOriginArea());
		if(StringUtils.isNotEmpty(deprecated.getOriginCountry()))
			product.setOriginCountry(deprecated.getOriginCountry());
		if(deprecated.getProductionDate() != null)
			product.setProductionDate(toLocalDate(deprecated.getProductionDate()));
		if(StringUtils.isNotEmpty(deprecated.getProductUnit()))
			product.setUnit(deprecated.getProductUnit());
		
		return product;
	}
	
	public static de.bund.bfr.metadata.swagger.PopulationGroup convert(metadata.PopulationGroup deprecated){
		de.bund.bfr.metadata.swagger.PopulationGroup pop = new de.bund.bfr.metadata.swagger.PopulationGroup();
		
		for(StringObject item : deprecated.getBmi()) {
			pop.addBmiItem(item.getValue());
		}
		for(StringObject item : deprecated.getCountry()) {
			pop.addCountryItem(item.getValue());
		}
		for(StringObject item : deprecated.getPatternConsumption()) {
			pop.addPatternConsumptionItem(item.getValue());
		}
		for(StringObject item : deprecated.getPopulationAge()) {
			pop.addPopulationAgeItem(item.getValue());
		}
		for(StringObject item : deprecated.getPopulationDescription()) {
			pop.addPopulationDescriptionItem(item.getValue());
		}
		for(StringObject item : deprecated.getPopulationRiskFactor()) {
			pop.addPopulationRiskFactorItem(item.getValue());
		}
		for(StringObject item : deprecated.getPopulationSpan()) {
			pop.addPopulationSpanItem(item.getValue());
		}
		for(StringObject item : deprecated.getRegion()) {
			pop.addRegionItem(item.getValue());
		}
		for(StringObject item : deprecated.getSeason()) {
			pop.addSeasonItem(item.getValue());
		}
		for(StringObject item : deprecated.getSpecialDietGroups()) {
			pop.addSpecialDietGroupsItem(item.getValue());
		}
		
		if(StringUtils.isNotEmpty(deprecated.getPopulationName()))
			pop.setName(deprecated.getPopulationName());
		if(StringUtils.isNotEmpty(deprecated.getPopulationGender()))
			pop.setPopulationGender(deprecated.getPopulationGender());
		if(StringUtils.isNotEmpty(deprecated.getTargetPopulation()))
			pop.setTargetPopulation(deprecated.getTargetPopulation());
		
				
		return pop;
	}
	public static de.bund.bfr.metadata.swagger.Hazard convert(metadata.Hazard deprecated){
		de.bund.bfr.metadata.swagger.Hazard hazard = new de.bund.bfr.metadata.swagger.Hazard();
		
		if(StringUtils.isNotEmpty(deprecated.getAcceptableDailyIntake()))
			hazard.setAcceptableDailyIntake(deprecated.getAcceptableDailyIntake());
		if(StringUtils.isNotEmpty(deprecated.getAcceptableOperatorExposureLevel()))
			hazard.setAcceptableOperatorsExposureLevel(deprecated.getAcceptableOperatorExposureLevel());
		if(StringUtils.isNotEmpty(deprecated.getAcuteReferenceDose()))
			hazard.setAcuteReferenceDose(deprecated.getAcuteReferenceDose());
		if(StringUtils.isNotEmpty(deprecated.getAdverseEffect()))
			hazard.setAdverseEffect(deprecated.getAdverseEffect());
		if(StringUtils.isNotEmpty(deprecated.getBenchmarkDose()))
			hazard.setBenchmarkDose(deprecated.getBenchmarkDose());
		if(StringUtils.isNotEmpty(deprecated.getHazardDescription()))
			hazard.setDescription(deprecated.getHazardDescription());
		if(StringUtils.isNotEmpty(deprecated.getHazardIndSum()))
			hazard.setIndSum(deprecated.getHazardIndSum());
		if(StringUtils.isNotEmpty(deprecated.getLowestObservedAdverseAffectLevel()))
			hazard.setLowestObservedAdverseAffectLevel(deprecated.getLowestObservedAdverseAffectLevel());
		if(StringUtils.isNotEmpty(deprecated.getMaximumResidueLimit()))
			hazard.setMaximumResidueLimit(deprecated.getMaximumResidueLimit());
		if(StringUtils.isNotEmpty(deprecated.getHazardName()))
			hazard.setName(deprecated.getHazardName());
		if(StringUtils.isNotEmpty(deprecated.getNoObservedAdverseAffectLevel()))
			hazard.setNoObservedAdverseAffectLevel(deprecated.getNoObservedAdverseAffectLevel());
		if(StringUtils.isNotEmpty(deprecated.getSourceOfContamination()))
			hazard.setSourceOfContamination(deprecated.getSourceOfContamination());
		if(StringUtils.isNotEmpty(deprecated.getHazardType()))
			hazard.setType(deprecated.getHazardType());
		if(StringUtils.isNotEmpty(deprecated.getHazardUnit()))
			hazard.setUnit(deprecated.getHazardUnit());
		
		
		return hazard;
	}


	public static de.bund.bfr.metadata.swagger.GenericModelDataBackground convert(metadata.DataBackground emfDBG){
		de.bund.bfr.metadata.swagger.GenericModelDataBackground swaggerDBG = new de.bund.bfr.metadata.swagger.GenericModelDataBackground();
		
		//assay
		if(emfDBG.getAssay() != null) {
			for(metadata.Assay item : emfDBG.getAssay()) {
				swaggerDBG.addAssayItem(convert(item));
			}
		}
		
		//dietaryassessment
		if(emfDBG.getDietaryAssessmentMethod() != null) {
			for(metadata.DietaryAssessmentMethod item : emfDBG.getDietaryAssessmentMethod()) {
				swaggerDBG.addDietaryAssessmentMethodItem(convert(item));
			}
		}
		
		//laboratroy
		if(emfDBG.getLaboratory() != null) {
			for(metadata.Laboratory item : emfDBG.getLaboratory()) {
				swaggerDBG.addLaboratoryItem(convert(item));
			}
		}
		
		//study sample
		if(emfDBG.getStudySample() != null) {
			for(metadata.StudySample item : emfDBG.getStudySample()) {
				swaggerDBG.addStudySampleItem(convert(item));
			}
		}
		
		//study
		if(emfDBG.getStudy() != null)
			swaggerDBG.setStudy(convert(emfDBG.getStudy()));
		
		
		return swaggerDBG;
	} 
	public static de.bund.bfr.metadata.swagger.Assay convert(metadata.Assay deprecated){
		de.bund.bfr.metadata.swagger.Assay assay = new de.bund.bfr.metadata.swagger.Assay();
		
		if(StringUtils.isNotEmpty(deprecated.getAssayDescription()))
			assay.setDescription(deprecated.getAssayDescription());
		if(StringUtils.isNotEmpty(deprecated.getRangeOfContamination()))
			assay.setContaminationRange(deprecated.getRangeOfContamination());
		if(StringUtils.isNotEmpty(deprecated.getLimitOfDetection()))
			assay.setDetectionLimit(deprecated.getLimitOfDetection());
		if(StringUtils.isNotEmpty(deprecated.getPercentageOfFat()))
			assay.setFatPercentage(deprecated.getPercentageOfFat());
		if(StringUtils.isNotEmpty(deprecated.getLeftCensoredData()))
			assay.setLeftCensoredData(deprecated.getLeftCensoredData());
		if(StringUtils.isNotEmpty(deprecated.getPercentageOfMoisture()))
			assay.setMoisturePercentage(deprecated.getPercentageOfMoisture());
		if(StringUtils.isNotEmpty(deprecated.getAssayName()))
			assay.setName(deprecated.getAssayName());
		if(StringUtils.isNotEmpty(deprecated.getLimitOfQuantification()))
			assay.setQuantificationLimit(deprecated.getLimitOfQuantification());
		if(StringUtils.isNotEmpty(deprecated.getUncertaintyValue()))
			assay.setUncertaintyValue(deprecated.getUncertaintyValue());
		
		return assay;
	}
	
	public static de.bund.bfr.metadata.swagger.DietaryAssessmentMethod convert(metadata.DietaryAssessmentMethod deprecated){
		de.bund.bfr.metadata.swagger.DietaryAssessmentMethod diet = new de.bund.bfr.metadata.swagger.DietaryAssessmentMethod();
		
		if(StringUtils.isNotEmpty(deprecated.getFoodDescriptors()))
			diet.addFoodDescriptorsItem(deprecated.getFoodDescriptors());
		if(StringUtils.isNotEmpty(deprecated.getNumberOfFoodItems()))
			diet.addNumberOfFoodItemsItem(deprecated.getNumberOfFoodItems());
		if(StringUtils.isNotEmpty(deprecated.getRecordTypes()))
			diet.addRecordTypesItem(deprecated.getRecordTypes());
		if(StringUtils.isNotEmpty(deprecated.getCollectionTool()))
			diet.setCollectionTool(deprecated.getCollectionTool());
		if(deprecated.getNumberOfNonConsecutiveOneDay() != 0)
			diet.setNumberOfNonConsecutiveOneDay(Integer.toString(deprecated.getNumberOfNonConsecutiveOneDay()));
		if(StringUtils.isNotEmpty(deprecated.getSoftwareTool()))
			diet.setSoftwareTool(deprecated.getSoftwareTool());
		
		return diet;
	}
	
	public static de.bund.bfr.metadata.swagger.Laboratory convert(metadata.Laboratory deprecated){
		de.bund.bfr.metadata.swagger.Laboratory lab = new de.bund.bfr.metadata.swagger.Laboratory();
		
		if(deprecated.getLaboratoryAccreditation() != null) {
			for(StringObject item : deprecated.getLaboratoryAccreditation()) {
				lab.addAccreditationItem(item.getValue());
			}
		}
		
		if(StringUtils.isNotEmpty(deprecated.getLaboratoryCountry()))
			lab.setCountry(deprecated.getLaboratoryCountry());
		if(StringUtils.isNotEmpty(deprecated.getLaboratoryName()))
			lab.setName(deprecated.getLaboratoryName());
		
		return lab;
	}
	
	public static de.bund.bfr.metadata.swagger.StudySample convert(metadata.StudySample deprecated){
		de.bund.bfr.metadata.swagger.StudySample sample = new de.bund.bfr.metadata.swagger.StudySample();
		
		if(StringUtils.isNotEmpty(deprecated.getLotSizeUnit()))
			sample.setLotSizeUnit(deprecated.getLotSizeUnit());
		if(StringUtils.isNotEmpty(deprecated.getProtocolOfSampleCollection()))
			sample.setProtocolOfSampleCollection(deprecated.getProtocolOfSampleCollection());
		if(StringUtils.isNotEmpty(deprecated.getSampleName()))
			sample.setSampleName(deprecated.getSampleName());
		if(StringUtils.isNotEmpty(deprecated.getSamplingMethod()))
			sample.setSamplingMethod(deprecated.getSamplingMethod());
		if(StringUtils.isNotEmpty(deprecated.getSamplingPlan()))
			sample.setSamplingPlan(deprecated.getSamplingPlan());
		if(StringUtils.isNotEmpty(deprecated.getSamplingPoint()))
			sample.setSamplingPoint(deprecated.getSamplingPoint());
		if(StringUtils.isNotEmpty(deprecated.getSamplingSize()))
			sample.setSamplingSize(deprecated.getSamplingSize());
		if(StringUtils.isNotEmpty(deprecated.getSamplingStrategy()))
			sample.setSamplingStrategy(deprecated.getSamplingStrategy());
		if(StringUtils.isNotEmpty(deprecated.getSamplingWeight()))
			sample.setSamplingWeight(deprecated.getSamplingWeight());
		if(StringUtils.isNotEmpty(deprecated.getTypeOfSamplingProgram()))
			sample.setTypeOfSamplingProgram(deprecated.getTypeOfSamplingProgram());
		return sample;
	}
	
	public static de.bund.bfr.metadata.swagger.Study convert(metadata.Study deprecated){
		de.bund.bfr.metadata.swagger.Study study = new de.bund.bfr.metadata.swagger.Study();
		
		if(StringUtils.isNotEmpty(deprecated.getAccreditationProcedureForTheAssayTechnology()))
			study.setAccreditationProcedureForTheAssayTechnology(deprecated.getAccreditationProcedureForTheAssayTechnology());
		if(StringUtils.isNotEmpty(deprecated.getStudyAssayMeasurementType()))
			study.setAssayMeasurementType(deprecated.getStudyAssayMeasurementType());
		if(StringUtils.isNotEmpty(deprecated.getStudyAssayTechnologyPlatform()))
			study.setAssayTechnologyPlatform(deprecated.getStudyAssayTechnologyPlatform());
		if(StringUtils.isNotEmpty(deprecated.getStudyAssayTechnologyType()))
			study.setAssayTechnologyType(deprecated.getStudyAssayTechnologyType());
		if(StringUtils.isNotEmpty(deprecated.getStudyDescription()))
			study.setDescription(deprecated.getStudyDescription());
		if(StringUtils.isNotEmpty(deprecated.getStudyDesignType()))
			study.setDesignType(deprecated.getStudyDesignType());
		if(StringUtils.isNotEmpty(deprecated.getStudyIdentifier()))
			study.setIdentifier(deprecated.getStudyIdentifier());
		if(StringUtils.isNotEmpty(deprecated.getStudyProtocolComponentsName()))
			study.setProtocolComponentsName(deprecated.getStudyProtocolComponentsName());
		if(StringUtils.isNotEmpty(deprecated.getStudyProtocolComponentsType()))
			study.setProtocolComponentsType(deprecated.getStudyProtocolComponentsType());
		if(StringUtils.isNotEmpty(deprecated.getStudyProtocolDescription()))
			study.setProtocolDescription(deprecated.getStudyProtocolDescription());
		if(StringUtils.isNotEmpty(deprecated.getStudyProtocolName()))
			study.setProtocolName(deprecated.getStudyProtocolName());
		if(StringUtils.isNotEmpty(deprecated.getStudyProtocolParametersName()))
			study.setProtocolParametersName(deprecated.getStudyProtocolParametersName());
		if(StringUtils.isNotEmpty(deprecated.getStudyProtocolType()))
			study.setProtocolType(deprecated.getStudyProtocolType());
		if(deprecated.getStudyProtocolURI() != null)
			study.setProtocolURI(deprecated.getStudyProtocolURI().toString());
		if(StringUtils.isNotEmpty(deprecated.getStudyProtocolVersion()))
			study.setProtocolVersion(deprecated.getStudyProtocolVersion());
		if(StringUtils.isNotEmpty(deprecated.getStudyTitle()))
			study.setTitle(deprecated.getStudyTitle());
		return study;
	}
	/** Internal map used to convert RIS types to 1.0.4 Reference types. */
	private static Map<Type, PublicationTypeEnum> PUBLICATION_TYPE;
	static {
		PUBLICATION_TYPE = new HashMap<Type, PublicationTypeEnum>();

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
	@SuppressWarnings("deprecation")
	private static LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}
}
