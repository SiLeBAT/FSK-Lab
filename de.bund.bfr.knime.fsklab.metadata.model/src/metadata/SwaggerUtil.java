package metadata;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.threeten.bp.LocalDate;

import de.bund.bfr.metadata.swagger.ConsumptionModel;
import de.bund.bfr.metadata.swagger.DataModel;
import de.bund.bfr.metadata.swagger.DoseResponseModel;
import de.bund.bfr.metadata.swagger.ExposureModel;
import de.bund.bfr.metadata.swagger.GenericModel;
import de.bund.bfr.metadata.swagger.HealthModel;
import de.bund.bfr.metadata.swagger.Model;
import de.bund.bfr.metadata.swagger.OtherModel;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.PredictiveModel;
import de.bund.bfr.metadata.swagger.ProcessModel;
import de.bund.bfr.metadata.swagger.QraModel;
import de.bund.bfr.metadata.swagger.Reference.PublicationTypeEnum;
import de.bund.bfr.metadata.swagger.RiskModel;
import de.bund.bfr.metadata.swagger.ToxicologicalModel;

public class SwaggerUtil {

	public static Map<String, Class<? extends Model>> modelClasses = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	static {
		modelClasses.put("GenericModel", GenericModel.class);
		modelClasses.put("DataModel", DataModel.class);
		modelClasses.put("PredictiveModel", PredictiveModel.class);
		modelClasses.put("OtherModel", OtherModel.class);
		modelClasses.put("ExposureModel", ExposureModel.class);
		modelClasses.put("ToxicologicalModel", ToxicologicalModel.class);
		modelClasses.put("DoseResponseModel", DoseResponseModel.class);
		modelClasses.put("ProcessModel", ProcessModel.class);
		modelClasses.put("ConsumptionModel", ConsumptionModel.class);
		modelClasses.put("HealthModel", HealthModel.class);
		modelClasses.put("RiskModel", RiskModel.class);
		modelClasses.put("QraModel", QraModel.class);
	}
	/** Internal map used to convert RIS types to 1.0.4 Reference types. */
	public static Map<PublicationType, PublicationTypeEnum> PUBLICATION_TYPE;
	static {
		PUBLICATION_TYPE = new HashMap<PublicationType, PublicationTypeEnum>();

		PUBLICATION_TYPE.put(PublicationType.ABST, PublicationTypeEnum.ABST);
		PUBLICATION_TYPE.put(PublicationType.ADVS, PublicationTypeEnum.ADVS);
		PUBLICATION_TYPE.put(PublicationType.AGGR, PublicationTypeEnum.AGGR);
		PUBLICATION_TYPE.put(PublicationType.ANCIENT, PublicationTypeEnum.ANCIENT);
		PUBLICATION_TYPE.put(PublicationType.ART, PublicationTypeEnum.ART);
		PUBLICATION_TYPE.put(PublicationType.BILL, PublicationTypeEnum.BILL);
		PUBLICATION_TYPE.put(PublicationType.BLOG, PublicationTypeEnum.BLOG);
		PUBLICATION_TYPE.put(PublicationType.BOOK, PublicationTypeEnum.BOOK);
		PUBLICATION_TYPE.put(PublicationType.CASE, PublicationTypeEnum.CASE);
		PUBLICATION_TYPE.put(PublicationType.CHAP, PublicationTypeEnum.CHAP);
		PUBLICATION_TYPE.put(PublicationType.CHART, PublicationTypeEnum.CHART);
		PUBLICATION_TYPE.put(PublicationType.CLSWK, PublicationTypeEnum.CLSWK);
		PUBLICATION_TYPE.put(PublicationType.COMP, PublicationTypeEnum.COMP);
		PUBLICATION_TYPE.put(PublicationType.CONF, PublicationTypeEnum.CONF);
		PUBLICATION_TYPE.put(PublicationType.CPAPER, PublicationTypeEnum.CPAPER);
		PUBLICATION_TYPE.put(PublicationType.CTLG, PublicationTypeEnum.CTLG);
		PUBLICATION_TYPE.put(PublicationType.DATA, PublicationTypeEnum.DATA);
		PUBLICATION_TYPE.put(PublicationType.DBASE, PublicationTypeEnum.DBASE);
		PUBLICATION_TYPE.put(PublicationType.DICT, PublicationTypeEnum.DICT);
		PUBLICATION_TYPE.put(PublicationType.EBOOK, PublicationTypeEnum.EBOOK);
		PUBLICATION_TYPE.put(PublicationType.ECHAP, PublicationTypeEnum.ECHAP);
		PUBLICATION_TYPE.put(PublicationType.EDBOOK, PublicationTypeEnum.EDBOOK);
		PUBLICATION_TYPE.put(PublicationType.EJOUR, PublicationTypeEnum.EJOUR);
		PUBLICATION_TYPE.put(PublicationType.ELECT, PublicationTypeEnum.ELECT);
		PUBLICATION_TYPE.put(PublicationType.ENCYC, PublicationTypeEnum.ENCYC);
		PUBLICATION_TYPE.put(PublicationType.EQUA, PublicationTypeEnum.EQUA);
		PUBLICATION_TYPE.put(PublicationType.FIGURE, PublicationTypeEnum.FIGURE);
		PUBLICATION_TYPE.put(PublicationType.GEN, PublicationTypeEnum.GEN);
		PUBLICATION_TYPE.put(PublicationType.GOVDOC, PublicationTypeEnum.GOVDOC);
		PUBLICATION_TYPE.put(PublicationType.GRANT, PublicationTypeEnum.GRANT);
		PUBLICATION_TYPE.put(PublicationType.HEAR, PublicationTypeEnum.HEAR);
		PUBLICATION_TYPE.put(PublicationType.ICOMM, PublicationTypeEnum.ICOMM);
		PUBLICATION_TYPE.put(PublicationType.INPR, PublicationTypeEnum.INPR);
		PUBLICATION_TYPE.put(PublicationType.JOUR, PublicationTypeEnum.JOUR);
		PUBLICATION_TYPE.put(PublicationType.JFULL, PublicationTypeEnum.JFULL);
		PUBLICATION_TYPE.put(PublicationType.LEGAL, PublicationTypeEnum.LEGAL);
		PUBLICATION_TYPE.put(PublicationType.MANSCPT, PublicationTypeEnum.MANSCPT);
		PUBLICATION_TYPE.put(PublicationType.MAP, PublicationTypeEnum.MAP);
		PUBLICATION_TYPE.put(PublicationType.MGZN, PublicationTypeEnum.MGZN);
		PUBLICATION_TYPE.put(PublicationType.MPCT, PublicationTypeEnum.MPCT);
		PUBLICATION_TYPE.put(PublicationType.MULTI, PublicationTypeEnum.MULTI);
		PUBLICATION_TYPE.put(PublicationType.MUSIC, PublicationTypeEnum.MUSIC);
		// Typo in PublicationTypeEnum. It should be 'NEWS'
		PUBLICATION_TYPE.put(PublicationType.NEWS, PublicationTypeEnum.NEW);
		PUBLICATION_TYPE.put(PublicationType.PAMP, PublicationTypeEnum.PAMP);
		PUBLICATION_TYPE.put(PublicationType.PAT, PublicationTypeEnum.PAT);
		PUBLICATION_TYPE.put(PublicationType.PCOMM, PublicationTypeEnum.PCOMM);
		PUBLICATION_TYPE.put(PublicationType.RPRT, PublicationTypeEnum.RPRT);
		PUBLICATION_TYPE.put(PublicationType.SER, PublicationTypeEnum.SER);
		PUBLICATION_TYPE.put(PublicationType.SLIDE, PublicationTypeEnum.SLIDE);
		PUBLICATION_TYPE.put(PublicationType.SOUND, PublicationTypeEnum.SOUND);
		PUBLICATION_TYPE.put(PublicationType.STAND, PublicationTypeEnum.STAND);
		PUBLICATION_TYPE.put(PublicationType.STAT, PublicationTypeEnum.STAT);
		PUBLICATION_TYPE.put(PublicationType.THES, PublicationTypeEnum.THES);
		PUBLICATION_TYPE.put(PublicationType.UNPB, PublicationTypeEnum.UNPB);
		PUBLICATION_TYPE.put(PublicationType.VIDEO, PublicationTypeEnum.VIDEO);
	}

	/** Internal map used to convert parameter classification to 1.0.4. */
	public static Map<metadata.ParameterClassification, de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum> CLASSIF;
	static {
		CLASSIF = new HashMap<>();

		CLASSIF.put(metadata.ParameterClassification.INPUT,
				de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.INPUT);
		CLASSIF.put(metadata.ParameterClassification.OUTPUT,
				de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.OUTPUT);
		CLASSIF.put(metadata.ParameterClassification.CONSTANT,
				de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum.CONSTANT);
	}

	/** Internal map used to convert parameter types to 1.0.4. */
	public static Map<metadata.ParameterType, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum> TYPES;
	static {
		TYPES = new HashMap<>();

		TYPES.put(metadata.ParameterType.NULL, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.OBJECT);
		TYPES.put(metadata.ParameterType.INTEGER, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.INTEGER);
		TYPES.put(metadata.ParameterType.DOUBLE, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.DOUBLE);
		TYPES.put(metadata.ParameterType.NUMBER, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.NUMBER);
		TYPES.put(metadata.ParameterType.DATE, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.DATE);
		TYPES.put(metadata.ParameterType.FILE, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.FILE);
		TYPES.put(metadata.ParameterType.BOOLEAN, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.BOOLEAN);
		TYPES.put(metadata.ParameterType.VECTOR_OF_NUMBERS,
				de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.VECTOROFNUMBERS);
		TYPES.put(metadata.ParameterType.VECTOR_OF_STRINGS,
				de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.VECTOROFSTRINGS);
		TYPES.put(metadata.ParameterType.MATRIX_OF_NUMBERS,
				de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.MATRIXOFNUMBERS);
		TYPES.put(metadata.ParameterType.MATRIX_OF_STRINGS,
				de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.MATRIXOFSTRINGS);
		TYPES.put(metadata.ParameterType.OBJECT, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.OBJECT);
		TYPES.put(metadata.ParameterType.OTHER, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.OBJECT);
		TYPES.put(metadata.ParameterType.STRING, de.bund.bfr.metadata.swagger.Parameter.DataTypeEnum.STRING);
	}

	public static LocalDate toLocalDate(Date date) {
		
		ZoneId defaultZoneId = ZoneId.systemDefault();
		
		int year = date.toInstant().atZone(defaultZoneId).toLocalDate().getYear();
		int month = date.toInstant().atZone(defaultZoneId).toLocalDate().getMonthValue();
		int day = date.toInstant().atZone(defaultZoneId).toLocalDate().getDayOfMonth();
		
		return LocalDate.of(year, month, day);
	}

	public static List<Parameter> getParameter(Model model) {
		List<Parameter> parameters;

		final String modelType = model.getModelType();
		if (modelType.equalsIgnoreCase("genericModel")) {
			parameters = ((GenericModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("dataModel")) {
			parameters = ((DataModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("predictiveModel")) {
			parameters = ((PredictiveModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("otherModel")) {
			parameters = ((OtherModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("exposureModel")) {
			parameters = ((ExposureModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("toxicologicalModel")) {
			parameters = ((ToxicologicalModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("doseResponseModel")) {
			parameters = ((DoseResponseModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("processModel")) {
			parameters = ((ProcessModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("consumptionModel")) {
			parameters = ((ConsumptionModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("riskModel")) {
			parameters = ((RiskModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("qraModel")) {
			parameters = ((QraModel) model).getModelMath().getParameter();
		} else if (modelType.equalsIgnoreCase("healthModel")) {
			parameters = ((HealthModel) model).getModelMath().getParameter();
		} else {
			parameters = null;
		}

		return parameters;
	}

	public static void setParameter(Model model, List<Parameter> pList) {

		final String modelType = model.getModelType();
		if (modelType.equalsIgnoreCase("genericModel")) {
			((GenericModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("dataModel")) {
			((DataModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("predictiveModel")) {
			((PredictiveModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("otherModel")) {
			((OtherModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("exposureModel")) {
			((ExposureModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("toxicologicalModel")) {
			((ToxicologicalModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("doseResponseModel")) {
			((DoseResponseModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("processModel")) {
			((ProcessModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("consumptionModel")) {
			((ConsumptionModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("riskModel")) {
			((RiskModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("healthModel")) {
			((HealthModel) model).getModelMath().setParameter(pList);
		} else if (modelType.equalsIgnoreCase("qraModel")) {
			((QraModel) model).getModelMath().setParameter(pList);
		}

	}

	/** @return language written in. Null if missing. */
	public static String getLanguageWrittenIn(Model model) {
		String language;

		final String modelType = model.getModelType();
		if (modelType.equalsIgnoreCase("genericModel")) {
			language = ((GenericModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("dataModel")) {
			language = null;
		} else if (modelType.equalsIgnoreCase("predictiveModel")) {
			language = ((PredictiveModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("otherModel")) {
			language = ((OtherModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("exposureModel")) {
			language = ((ExposureModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("toxicologicalModel")) {
			language = ((ToxicologicalModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("doseResponseModel")) {
			language = ((DoseResponseModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("processModel")) {
			language = ((ProcessModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("consumptionModel")) {
			language = ((ConsumptionModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("riskModel")) {
			language = ((RiskModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("qraModel")) {
			language = ((QraModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else if (modelType.equalsIgnoreCase("healthModel")) {
			language = ((HealthModel) model).getGeneralInformation().getLanguageWrittenIn();
		} else {
			language = null;
		}

		return language;
	}

	/** @return language written in. Null if missing. */
	public static String getModelName(Model model) {
		String name;

		final String modelType = model.getModelType();
		if (modelType.equalsIgnoreCase("genericModel")) {
			name = ((GenericModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("dataModel")) {
			name = ((DataModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("predictiveModel")) {
			name = ((PredictiveModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("otherModel")) {
			name = ((OtherModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("exposureModel")) {
			name = ((ExposureModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("toxicologicalModel")) {
			name = ((ToxicologicalModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("doseResponseModel")) {
			name = ((DoseResponseModel) model).getGeneralInformation().getModelName();
		} else if (modelType.equalsIgnoreCase("processModel")) {
			name = ((ProcessModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("consumptionModel")) {
			name = ((ConsumptionModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("riskModel")) {
			name = ((RiskModel) model).getGeneralInformation().getName();
		} else if (modelType.equalsIgnoreCase("qraModel")) {
			name = ((QraModel) model).getGeneralInformation().getName();
		}  else if (modelType.equalsIgnoreCase("healthModel")) {
			name = ((HealthModel) model).getGeneralInformation().getName();
		} else {
			name = null;
		}

		return name;
	}

	public static Object getGeneralInformation(Model model) {
		Object information;

		final String modelType = model.getModelType();
		if (modelType.equalsIgnoreCase("genericModel")) {
			information = ((GenericModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("dataModel")) {
			information = ((DataModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("predictiveModel")) {
			information = ((PredictiveModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("otherModel")) {
			information = ((OtherModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("exposureModel")) {
			information = ((ExposureModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("toxicologicalModel")) {
			information = ((ToxicologicalModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("doseResponseModel")) {
			information = ((DoseResponseModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("processModel")) {
			information = ((ProcessModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("consumptionModel")) {
			information = ((ConsumptionModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("riskModel")) {
			information = ((RiskModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("qraModel")) {
			information = ((QraModel) model).getGeneralInformation();
		} else if (modelType.equalsIgnoreCase("healthModel")) {
			information = ((HealthModel) model).getGeneralInformation();
		} else {
			information = null;
		}

		return information;
	}

	public static Object getScope(Model model) {
		Object scope;

		final String modelType = model.getModelType();
		if (modelType.equalsIgnoreCase("genericModel")) {
			scope = ((GenericModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("dataModel")) {
			scope = ((DataModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("predictiveModel")) {
			scope = ((PredictiveModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("otherModel")) {
			scope = ((OtherModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("exposureModel")) {
			scope = ((ExposureModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("toxicologicalModel")) {
			scope = ((ToxicologicalModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("doseResponseModel")) {
			scope = ((DoseResponseModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("processModel")) {
			scope = ((ProcessModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("consumptionModel")) {
			scope = ((ConsumptionModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("riskModel")) {
			scope = ((RiskModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("qraModel")) {
			scope = ((QraModel) model).getScope();
		} else if (modelType.equalsIgnoreCase("healthModel")) {
			scope = ((HealthModel) model).getScope();
		} else {
			scope = null;
		}

		return scope;
	}

	public static Object getDataBackground(Model model) {
		Object background;

		final String modelType = model.getModelType();
		if (modelType.equalsIgnoreCase("genericModel")) {
			background = ((GenericModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("dataModel")) {
			background = ((DataModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("predictiveModel")) {
			background = ((PredictiveModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("otherModel")) {
			background = ((OtherModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("exposureModel")) {
			background = ((ExposureModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("toxicologicalModel")) {
			background = ((ToxicologicalModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("doseResponseModel")) {
			background = ((DoseResponseModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("processModel")) {
			background = ((ProcessModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("consumptionModel")) {
			background = ((ConsumptionModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("riskModel")) {
			background = ((RiskModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("qraModel")) {
			background = ((QraModel) model).getDataBackground();
		} else if (modelType.equalsIgnoreCase("healthModel")) {
			background = ((HealthModel) model).getDataBackground();
		} else {
			background = null;
		}

		return background;
	}

	public static Object getModelMath(Model model) {
		Object math;

		final String modelType = model.getModelType();
		if (modelType.equalsIgnoreCase("genericModel")) {
			math = ((GenericModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("dataModel")) {
			math = ((DataModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("predictiveModel")) {
			math = ((PredictiveModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("otherModel")) {
			math = ((OtherModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("exposureModel")) {
			math = ((ExposureModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("toxicologicalModel")) {
			math = ((ToxicologicalModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("doseResponseModel")) {
			math = ((DoseResponseModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("processModel")) {
			math = ((ProcessModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("consumptionModel")) {
			math = ((ConsumptionModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("riskModel")) {
			math = ((RiskModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("qraModel")) {
			math = ((QraModel) model).getModelMath();
		} else if (modelType.equalsIgnoreCase("healthModel")) {
			math = ((HealthModel) model).getModelMath();
		} else {
			math = null;
		}

		return math;
	}

	public static Parameter cloneParameter(Parameter old) {
		Parameter param = new Parameter();
		param.setClassification(old.getClassification());
		param.setDataType(old.getDataType());
		param.setDescription(old.getDescription());
		param.setError(old.getError());
		param.setId(old.getId());
		param.setMaxValue(old.getMaxValue());
		param.setMinValue(old.getMinValue());
		param.setName(old.getName());
		param.setReference(old.getReference());
		param.setSource(old.getSource());
		param.setSubject(old.getSubject());
		param.setUnit(old.getUnit());
		param.setValue(old.getValue());
		param.setVariabilitySubject(old.getVariabilitySubject());
		return param;
	}

}
