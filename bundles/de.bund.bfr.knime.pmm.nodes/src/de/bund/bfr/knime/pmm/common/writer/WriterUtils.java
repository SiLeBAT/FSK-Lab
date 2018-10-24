package de.bund.bfr.knime.pmm.common.writer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.StringUtils;
import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.ExecutionContext;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.CompSBasePlugin;
import org.sbml.jsbml.ext.comp.ExternalModelDefinition;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.ext.comp.ReplacedBy;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.PMFUtil;
import de.bund.bfr.pmfml.file.ExperimentalDataFile;
import de.bund.bfr.pmfml.file.ManualSecondaryModelFile;
import de.bund.bfr.pmfml.file.ManualTertiaryModelFile;
import de.bund.bfr.pmfml.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmfml.file.PrimaryModelWDataFile;
import de.bund.bfr.pmfml.file.PrimaryModelWODataFile;
import de.bund.bfr.pmfml.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmfml.model.ExperimentalData;
import de.bund.bfr.pmfml.model.ManualSecondaryModel;
import de.bund.bfr.pmfml.model.ManualTertiaryModel;
import de.bund.bfr.pmfml.model.OneStepSecondaryModel;
import de.bund.bfr.pmfml.model.OneStepTertiaryModel;
import de.bund.bfr.pmfml.model.PrimaryModelWData;
import de.bund.bfr.pmfml.model.PrimaryModelWOData;
import de.bund.bfr.pmfml.model.TwoStepSecondaryModel;
import de.bund.bfr.pmfml.model.TwoStepTertiaryModel;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.sbml.Correlation;
import de.bund.bfr.pmfml.sbml.DataSourceNode;
import de.bund.bfr.pmfml.sbml.GlobalModelIdNode;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.MetadataAnnotation;
import de.bund.bfr.pmfml.sbml.Model2Annotation;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.ModelVariable;
import de.bund.bfr.pmfml.sbml.PMFCoefficient;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.PMFUnit;
import de.bund.bfr.pmfml.sbml.PMFUnitDefinition;
import de.bund.bfr.pmfml.sbml.PrimaryModelNode;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.ReferenceSBMLNode;
import de.bund.bfr.pmfml.sbml.ReferenceType;
import de.bund.bfr.pmfml.sbml.SBMLFactory;
import de.bund.bfr.pmfml.sbml.SecDep;
import de.bund.bfr.pmfml.sbml.SecIndep;
import de.bund.bfr.pmfml.sbml.Uncertainties;

public class WriterUtils {

	private WriterUtils() {
	}

	public static PMFCoefficient paramXml2Coefficient(ParamXml paramXml) {
		String name = paramXml.getName();
		double value = (paramXml.getValue() == null) ? 0.0 : paramXml.getValue();
		String unit = (paramXml.getUnit() == null) ? "dimensionless" : PMFUtil.createId(paramXml.getUnit());
		Double P = paramXml.getP();
		Double error = paramXml.getError();
		Double t = paramXml.getT();
		Boolean isStart = paramXml.isStartParam();

		Map<String, Double> correlationMap = paramXml.getAllCorrelations();
		Correlation[] correlations = new Correlation[correlationMap.size()];
		int i = 0;
		for (Map.Entry<String, Double> entry : correlationMap.entrySet()) {
			if (entry.getValue() == null) {
				correlations[i] = new Correlation(entry.getKey());
			} else {
				correlations[i] = new Correlation(entry.getKey(), entry.getValue());
			}
			i++;
		}

		String desc = paramXml.getDescription();

		PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(name, value, unit, P, error, t, correlations,
				desc, isStart);
		return coefficient;
	}

	public static Uncertainties estModel2Uncertainties(EstModelXml estModel) {
		Integer id = estModel.id;
		String modelName = estModel.name;
		String comment = estModel.comment;
		Double r2 = estModel.r2;
		Double rms = estModel.rms;
		Double sse = estModel.sse;
		Double aic = estModel.aic;
		Double bic = estModel.bic;
		Integer dof = estModel.dof;
		Uncertainties uncertainties = SBMLFactory.createUncertainties(id, modelName, comment, r2, rms, sse, aic, bic,
				dof);
		return uncertainties;
	}

	public static Reference literatureItem2Reference(LiteratureItem lit) {
		String author = lit.author;
		Integer year = lit.year;
		String title = lit.title;
		String abstractText = lit.abstractText;
		String journal = lit.journal;
		String volume = lit.volume;
		String issue = lit.issue;
		Integer page = lit.page;
		Integer approvalMode = lit.approvalMode;
		String website = lit.website;
		ReferenceType type = (lit.type == null) ? null : ReferenceType.fromValue(lit.type);
		String comment = lit.comment;

		Reference ref = SBMLFactory.createReference(author, year, title, abstractText, journal, volume, issue, page,
				approvalMode, website, type, comment);
		return ref;
	}

	public static PMFCompartment matrixXml2Compartment(MatrixXml matrixXml, PmmXmlDoc miscDoc) {
		ModelVariable[] modelVariables = new ModelVariable[miscDoc.size()];
		for (int i = 0; i < miscDoc.size(); i++) {
			MiscXml miscXml = (MiscXml) miscDoc.get(i);
			modelVariables[i] = new ModelVariable(miscXml.name, miscXml.value);
		}
		String compartmentDetail = matrixXml.detail;

		String compartmentId;
		String compartmentName;

		if (matrixXml.name == null) {
			compartmentId = "MISSING_COMPARTMENT";
			compartmentName = "MISSING_COMPARTMENT";
		} else {
			compartmentId = PMFUtil.createId(matrixXml.name);
			compartmentName = matrixXml.name;
		}

		// Gets PMF code from DB
		String[] colNames = { "CodeSystem", "Basis" };
		String[] colVals = { "PMF", matrixXml.id.toString() };
		String pmfCode = (String) DBKernel.getValue(null, "Codes_Matrices", colNames, colVals, "Codes");
		PMFCompartment compartment = SBMLFactory.createPMFCompartment(compartmentId, compartmentName, pmfCode,
				compartmentDetail, modelVariables);
		return compartment;
	}

	public static PMFSpecies createSpecies(AgentXml agentXml, String unit, String compartmentId) {
		// Creates species and adds it to the model
		String speciesId = PMFUtil.createId("species" + agentXml.id);
		String speciesUnit = (unit == null) ? "dimensionless" : PMFUtil.createId(unit);
		String speciesName = (String) DBKernel.getValue("Agenzien", "ID", agentXml.id.toString(), "Agensname");
		String casNumber;
		if (speciesName == null) {
			speciesName = agentXml.name;
			casNumber = null;
		} else {
			casNumber = (String) DBKernel.getValue("Agenzien", "ID", agentXml.id.toString(), "Cas_Nummer");
		}
		String speciesDetail = agentXml.detail;

		PMFSpecies species = SBMLFactory.createPMFSpecies(compartmentId, speciesId, speciesName, speciesUnit, casNumber,
				speciesDetail, null);
		return species;
	}

	public static PMFUnitDefinition createUnitFromDB(String unit) throws XMLStreamException {
		if (!DBUnits.getDBUnits().containsKey(unit)) {
			return null;
		}

		String id = PMFUtil.createId(unit);
		String name = unit;
		String transformation = null;
		PMFUnit[] units = null;

		UnitsFromDB dbUnit = DBUnits.getDBUnits().get(unit);

		String mathml = dbUnit.getMathML_string();
		if (mathml != null && !mathml.isEmpty()) {

			// Parse transformation name if present
			int transformationIndex = mathml.indexOf("<transformation");
			if (transformationIndex == -1) {
				transformation = null;
			} else {
				int endTransformationIndex = mathml.indexOf("<", transformationIndex + 1);
				int firstQuotePos = mathml.indexOf("\"", transformationIndex);
				int secQuotePos = mathml.indexOf("\"", firstQuotePos + 1);
				transformation = mathml.substring(firstQuotePos + 1, secQuotePos);

				// Removes the transformation annotation (this
				// annotation has a name which is not prefixed and then
				// cannot be parsed properly)
				mathml = mathml.substring(0, transformationIndex) + mathml.substring(endTransformationIndex);
			}

			// Remove namespace (all the DB units have this namespace
			// which is not necessary)
			mathml = mathml
					.replaceAll("xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"", "");

			String preXml = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>"
					+ "<sbml xmlns=\"http://www.sbml.org/sbml/level3/version1/core\"" + " level=\"3\" version=\"1\""
					+ " xmlns:pmf=\"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML\""
					+ "><model id=\"ID\">" + "<listOfUnitDefinitions>";

			String postXml = "</listOfUnitDefinitions></model></sbml>";
			String totalXml = preXml + mathml + postXml;

			// Create a new UnitDefinition from XML
			UnitDefinition xmlUnitDef = SBMLReader.read(totalXml).getModel().getUnitDefinition(0);

			ListOf<Unit> listOfUnits = xmlUnitDef.getListOfUnits();
			int numOfUnits = listOfUnits.size();
			units = new PMFUnit[numOfUnits];
			for (int i = 0; i < numOfUnits; i++) {
				Unit sbmlUnit = listOfUnits.get(i);
				Unit.Kind kind = sbmlUnit.getKind();
				double multiplier = sbmlUnit.isSetMultiplier() ? sbmlUnit.getMultiplier() : PMFUnit.DEFAULT_MULTIPLIER;
				int scale = sbmlUnit.isSetScale() ? sbmlUnit.getScale() : PMFUnit.DEFAULT_SCALE;
				double exponent = sbmlUnit.isSetExponent() ? sbmlUnit.getExponent() : PMFUnit.DEFAULT_EXPONENT;

				units[i] = new PMFUnit(multiplier, scale, kind, exponent);
			}
		}

		PMFUnitDefinition unitDefinition = new PMFUnitDefinition(id, name, transformation, units);
		return unitDefinition;
	}

	public static ModelRule createM1Rule(CatalogModelXml catModel, String variable, Reference[] references) {

		// Trims out the "Value=" from the formula
		int pos = catModel.formula.indexOf("=");
		String formula = catModel.formula.substring(pos + 1);

		// Removes boundary conditions
		formula = MathUtilities.getAllButBoundaryCondition(formula);
		// csymbol time needs a lower case t
		formula = formula.replace("Time", "time");

		ModelClass modelClass;
		if (catModel.modelClass == null) {
			modelClass = ModelClass.UNKNOWN;
		} else {
			modelClass = ModelClass.fromValue(catModel.modelClass);
		}

		String formulaName = StringUtils.defaultIfEmpty(catModel.name, "Missing formula name");

		int catModelId = catModel.id;

		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, catModelId, references);
		return rule;
	}

	public static ModelRule createM2Rule(CatalogModelXml catModel, Reference[] references) {
		// Parses variable from the formula
		int pos = catModel.formula.indexOf("=");
		String variable = catModel.formula.substring(0, pos);

		// The remaining chunk contains the actual formula
		String formula = catModel.formula.substring(pos + 1);

		// Removes boundary conditions
		formula = MathUtilities.getAllButBoundaryCondition(formula);

		ModelClass modelClass;
		if (catModel.modelClass == null) {
			modelClass = ModelClass.UNKNOWN;
		} else {
			modelClass = ModelClass.fromValue(catModel.modelClass);
		}

		String formulaName = StringUtils.defaultIfEmpty("Missing formula name", catModel.name);

		int catModelId = catModel.id;

		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, catModelId, references);
		return rule;
	}
	
	/** @return true if any tuple has a time series. */
	public static boolean hasData(List<KnimeTuple> tuples) {
		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			if (mdData != null && mdData.size() > 0) {
				return true;
			}
		}
		return false;
	}

	public static void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
			boolean splitModels, String notes, ExecutionContext exec, ModelType modelType) throws Exception {

		Parser parser;

		switch (modelType) {
		case EXPERIMENTAL_DATA:
			parser = new ExperimentalDataParser();
			break;
		case PRIMARY_MODEL_WDATA:
			parser = new PrimaryModelWDataParser();
			break;
		case PRIMARY_MODEL_WODATA:
			parser = new PrimaryModelWODataParser();
			break;
		case TWO_STEP_SECONDARY_MODEL:
			parser = new TwoStepSecondaryModelParser();
			break;
		case ONE_STEP_SECONDARY_MODEL:
			parser = new OneStepSecondaryModelParser();
			break;
		case MANUAL_SECONDARY_MODEL:
			parser = new ManualSecondaryModelParser();
			break;
		case TWO_STEP_TERTIARY_MODEL:
			parser = new TwoStepTertiaryModelParser();
			break;
		case ONE_STEP_TERTIARY_MODEL:
			parser = new OneStepTertiaryModelParser();
			break;
		case MANUAL_TERTIARY_MODEL:
			parser = new ManualTertiaryModelParser();
			break;
		default:
			throw new IllegalArgumentException("Invalid model type: " + modelType);
		}

		parser.write(tuples, isPMFX, dir, mdName, metadata, splitModels, notes, exec);
	}

	private interface Parser {
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception;
	}

	/**
	 * Parse tuples from a table with timeseries.
	 */
	private static class ExperimentalDataParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			List<ExperimentalData> eds = new LinkedList<>();
			for (int i = 0; i < tuples.size(); i++) {
				KnimeTuple tuple = tuples.get(i);

				String docName = String.format("%s_%d.numl", mdName, i);
				NuMLDocument doc = new DataParser(tuple, metadata, notes).getDocument();

				ExperimentalData ed = new ExperimentalData(docName, doc);
				eds.add(ed);
			}

			Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
			ExperimentalDataFile.write(path, eds);
		}
	}

	/**
	 * Parse tuples from a table with primary models with data.
	 */
	private static class PrimaryModelWDataParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			final String modelExtension = isPMFX ? "pmf" : "sbml";

			List<PrimaryModelWData> pms = new LinkedList<>();

			for (KnimeTuple tuple : tuples) {
				PrimaryModelWData pm;

				Model1Parser m1Parser = new Model1Parser(tuple, metadata, notes);
				SBMLDocument sbmlDoc = m1Parser.getDocument();
				String sbmlDocName = String.format("%s_%d.%s", mdName, pms.size(), modelExtension);

				if (tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).size() > 0) {
					DataParser dataParser = new DataParser(tuple, metadata, notes);
					NuMLDocument numlDoc = dataParser.getDocument();
					String numlDocName = String.format("%s_%d.numl", mdName, pms.size());

					// Adds DataSourceNode to the model
					XMLNode dsn = new DataSourceNode(numlDocName).getNode();
					sbmlDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata", "")
							.addChild(dsn);

					pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, numlDocName, numlDoc);
				} else {
					pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
				}
				pms.add(pm);
			}

			Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
			PrimaryModelWDataFile.write(path, pms);
		}
	}

	/**
	 * Parse tuples from a table with primary models without data.
	 */
	private static class PrimaryModelWODataParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			final String modelExtension = isPMFX ? "pmf" : "sbml";

			List<PrimaryModelWOData> pms = new LinkedList<>();
			for (KnimeTuple tuple : tuples) {
				Model1Parser m1Parser = new Model1Parser(tuple, metadata, notes);

				SBMLDocument sbmlDoc = m1Parser.getDocument();
				String sbmlDocName = String.format("%s_%d.%s", mdName, pms.size(), modelExtension);

				PrimaryModelWOData pm = new PrimaryModelWOData(sbmlDocName, sbmlDoc);
				pms.add(pm);
			}

			Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
			PrimaryModelWODataFile.write(path, pms);
		}
	}

	/**
	 * Parse tuples from a table with primary models without data.
	 */
	private static class TwoStepSecondaryModelParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			// Group tuples according to its secondary model
			Map<Integer, List<KnimeTuple>> secModelMap = new HashMap<>();
			for (KnimeTuple tuple : tuples) {
				int secModelId = ((EstModelXml) tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0)).id;
				if (secModelMap.containsKey(secModelId)) {
					secModelMap.get(secModelId).add(tuple);
				} else {
					List<KnimeTuple> tlist = new LinkedList<>();
					tlist.add(tuple);
					secModelMap.put(secModelId, tlist);
				}
			}

			// For the tuples of every secondary model
			List<TwoStepSecondaryModel> sms = new LinkedList<>();
			for (List<KnimeTuple> tupleList : secModelMap.values()) {
				int modelCounter = sms.size();
				sms.add(parse(tupleList, isPMFX, modelCounter, mdName, metadata, notes));
			}

			if (splitModels) {
				for (int numModel = 0; numModel < sms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					Path path = Paths.get(dir, modelName + (isPMFX ? ".pmfx" : ".pmf"));
					TwoStepSecondaryModelFile.write(path, sms.subList(numModel, numModel + 1));
				}
			} else {
				Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
				TwoStepSecondaryModelFile.write(path, sms);
			}
		}

		private static TwoStepSecondaryModel parse(List<KnimeTuple> tuples, boolean isPMFX, int modelNum, String mdName,
				Metadata metadata, String notes) {
			/**
			 * <ol>
			 * <li>Create n SBMLDocument for primary models</li>
			 * <li>Parse data and create n NuMLDocument</li>
			 * <li>Create SBMLDocument for secondary model</li>
			 * </ol>
			 */
			final String modelExtension = isPMFX ? "pmf" : "sbml";

			List<PrimaryModelWData> primModels = new LinkedList<>();
			for (int i = 0; i < tuples.size(); i++) {
				KnimeTuple tuple = tuples.get(i);
				PrimaryModelWData pm;

				Model1Parser m1Parser = new Model1Parser(tuple, metadata, notes);

				SBMLDocument sbmlDoc = m1Parser.getDocument();
				String sbmlDocName = String.format("%s.%s", sbmlDoc.getModel().getId(), modelExtension);

				XMLNode metadataNode = sbmlDoc.getModel().getAnnotation().getNonRDFannotation()
						.getChildElement("metadata", "");
				if (tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).size() > 0) {
					DataParser dataParser = new DataParser(tuple, metadata, notes);

					NuMLDocument numlDoc = dataParser.getDocument();
					String numlDocName = String.format("%s.numl", sbmlDoc.getModel().getId());

					// Adds DataSourceNode to the model
					DataSourceNode dsn = new DataSourceNode(numlDocName);
					metadataNode.addChild(dsn.getNode());

					pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, numlDocName, numlDoc);
				} else {
					pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
				}

				primModels.add(pm);
			}

			// We get the first tuple to query the Model2 columns which are the
			// same
			// for all the tuples of the secondary model
			KnimeTuple firstTuple = tuples.get(0);
			Model2Parser m2Parser = new Model2Parser(firstTuple, metadata, notes);

			SBMLDocument secDoc = m2Parser.getDocument();
			String secDocName = String.format("%s_%d.%s", mdName, modelNum, modelExtension);
			// Adds annotation for the primary models
			XMLNode metadataNode = secDoc.getModel().getAnnotation().getNonRDFannotation().getChildElement("metadata",
					"");
			for (PrimaryModelWData pmwd : primModels) {
				PrimaryModelNode node = new PrimaryModelNode(pmwd.getModelDocName());
				metadataNode.addChild(node.getNode());
			}

			// Creates and return TwoStepSecondaryModel
			return new TwoStepSecondaryModel(secDocName, secDoc, primModels);
		}
	}

	/**
	 * Parse tuples from a table with primary models without data.
	 */
	private static class OneStepSecondaryModelParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			// Group tuples according to its secondary model
			Map<Integer, List<KnimeTuple>> secModelMap = new HashMap<>();
			for (KnimeTuple tuple : tuples) {
				int secModelId = ((EstModelXml) tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0)).id;
				if (secModelMap.containsKey(secModelId)) {
					secModelMap.get(secModelId).add(tuple);
				} else {
					List<KnimeTuple> tlist = new LinkedList<>();
					tlist.add(tuple);
					secModelMap.put(secModelId, tlist);
				}
			}

			// For the tuples of every secondary model
			List<OneStepSecondaryModel> sms = new LinkedList<>();
			for (List<KnimeTuple> tupleList : secModelMap.values()) {
				int modelCounter = sms.size();
				sms.add(parse(tupleList, isPMFX, mdName, modelCounter, metadata, notes));
			}

			if (splitModels) {
				for (int numModel = 0; numModel < sms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					Path path = Paths.get(dir, modelName + (isPMFX ? ".pmfx" : ".pmf"));
					OneStepSecondaryModelFile.write(path, sms.subList(numModel, numModel + 1));
				}
			} else {
				Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
				OneStepSecondaryModelFile.write(path, sms);
			}
		}

		private static OneStepSecondaryModel parse(List<KnimeTuple> tuples, boolean isPMFX, String mdName, int modelNum,
				Metadata metadata, String notes) {

			final String modelExtension = isPMFX ? ".pmf" : ".sbml";
			KnimeTuple firstTuple = tuples.get(0);

			// Retrieve Model2Schema cells
			EstModelXml secEstModel = (EstModelXml) firstTuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0);

			Model1Parser m1Parser = new Model1Parser(firstTuple, metadata, notes);
			SBMLDocument doc = m1Parser.getDocument();
			String docName = String.format("%s_%d.%s", mdName, modelNum, modelExtension);

			Model model = doc.getModel();
			model.setId(PMFUtil.createId("model" + secEstModel.id));
			CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) doc.getPlugin(CompConstants.shortLabel);
			CompModelPlugin compModelPlugin = (CompModelPlugin) model.getPlugin(CompConstants.shortLabel);

			// Create secondary model
			Model secModel = new Model2Parser(firstTuple, metadata, notes).getDocument().getModel();
			ModelDefinition md = new ModelDefinition(secModel);
			compDocPlugin.addModelDefinition(md);

			Submodel submodel = compModelPlugin.createSubmodel("submodel");
			submodel.setModelRef(secModel.getId());

			// Parse data sets and create NuML documents
			XMLNode metadataNode = md.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
			List<NuMLDocument> numlDocs = new LinkedList<>();
			List<String> numlDocNames = new LinkedList<>();
			for (KnimeTuple tuple : tuples) {
				String numlDocName = String.format("data%d.numl", numlDocs.size());
				numlDocNames.add(numlDocName);

				DataParser dataParser = new DataParser(tuple, metadata, notes);
				NuMLDocument numlDoc = dataParser.getDocument();
				numlDocs.add(numlDoc);

				// Adds DataSourceNode to the model
				metadataNode.addChild(new DataSourceNode(numlDocName).getNode());
			}

			OneStepSecondaryModel ossm = new OneStepSecondaryModel(docName, doc, numlDocNames, numlDocs);
			return ossm;
		}
	}

	/**
	 * Parse tuples from a table with primary models without data.
	 */
	private static class ManualSecondaryModelParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			List<ManualSecondaryModel> sms = new LinkedList<>();
			for (KnimeTuple tuple : tuples) {
				int mdNum = sms.size();
				sms.add(parse(tuple, isPMFX, mdName, mdNum, metadata, notes));
			}

			if (splitModels) {
				for (int numModel = 0; numModel < sms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					Path path = Paths.get(dir, modelName + (isPMFX ? ".pmfx" : ".pmf"));
					ManualSecondaryModelFile.write(path, sms.subList(numModel, numModel + 1));
				}
			} else {
				Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
				ManualSecondaryModelFile.write(path, sms);
			}
		}

		private static ManualSecondaryModel parse(KnimeTuple tuple, boolean isPMFX, String mdName, int mdNum,
				Metadata metadata, String notes) {

			final String modelExtension = isPMFX ? "pmf" : "sbml";

			// Retrieve Model2Schema cells
			CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG).get(0);
			EstModelXml estModel = (EstModelXml) tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0);
			DepXml dep = (DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0);
			PmmXmlDoc indepDoc = tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
			PmmXmlDoc paramsDoc = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);
			PmmXmlDoc mLitDoc = tuple.getPmmXml(Model2Schema.ATT_MLIT);
			PmmXmlDoc emLitDoc = tuple.getPmmXml(Model2Schema.ATT_EMLIT);
			int globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

			// Gets independent parameters
			List<IndepXml> indepXmls = new LinkedList<>();
			for (PmmXmlElementConvertable item : indepDoc.getElementSet()) {
				indepXmls.add((IndepXml) item);
			}

			// Gets constant parameters
			List<ParamXml> constXmls = new LinkedList<>();
			for (PmmXmlElementConvertable item : paramsDoc.getElementSet()) {
				constXmls.add((ParamXml) item);
			}

			String docName = String.format("%s_%d.%s", mdName, mdNum, modelExtension);
			SBMLDocument doc = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);
			// Enables Hierarchical Composition package
			doc.enablePackage(CompConstants.shortLabel);

			// Adds document annotation
			doc.setAnnotation(new MetadataAnnotation(metadata).getAnnotation());

			TableReader.addNamespaces(doc);

			// Create model definition
			String modelId = "model_" + dep.name;
			Model model = doc.createModel(modelId);
			if (estModel.name != null) {
				model.setName(estModel.name);
			}

			if (notes != null) {
				try {
					model.setNotes(notes);
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}

			try {
				TableReader.addUnitDefinitions(model, dep, indepXmls, constXmls);
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Adds dep
			Parameter depParam = new SecDep(dep.name, dep.description, dep.unit).getParam();
			// Adds dep constraint
			LimitsConstraint depLc = new LimitsConstraint(dep.name, dep.min, dep.max);
			if (depLc.getConstraint() != null) {
				model.addConstraint(depLc.getConstraint());
			}
			model.addParameter(depParam);

			// Add independent parameters
			for (IndepXml indepXml : indepXmls) {
				// Creates SBML parameter
				// model.addParameter(new SecIndep(indepXml).getParam());
				SecIndep secIndep = new SecIndep(indepXml.name, indepXml.description, indepXml.unit);
				model.addParameter(secIndep.getParam());
				// Adds constraint
				LimitsConstraint lc = new LimitsConstraint(indepXml.name, indepXml.min, indepXml.max);
				if (lc.getConstraint() != null) {
					model.addConstraint(lc.getConstraint());
				}
			}

			// Adds constant parameters
			for (ParamXml paramXml : constXmls) {
				// Creates SBML parameter
				PMFCoefficient coefficient = WriterUtils.paramXml2Coefficient(paramXml);
				model.addParameter(coefficient.getParameter());

				// Adds constraint
				LimitsConstraint lc = new LimitsConstraint(paramXml.getName(), paramXml.getMin(), paramXml.getMax());
				if (lc.getConstraint() != null) {
					model.addConstraint(lc.getConstraint());
				}
			}

			// Gets model literature
			Reference[] mLits = new Reference[mLitDoc.size()];
			for (int i = 0; i < mLitDoc.size(); i++) {
				mLits[i] = WriterUtils.literatureItem2Reference((LiteratureItem) mLitDoc.get(i));
			}

			// Gets estimated model literature
			Reference[] emLits = new Reference[emLitDoc.size()];
			for (int i = 0; i < emLitDoc.size(); i++) {
				emLits[i] = WriterUtils.literatureItem2Reference((LiteratureItem) emLitDoc.get(i));
			}

			ModelRule rule2 = WriterUtils.createM2Rule(catModel, mLits);
			model.addRule(rule2.getRule());

			// Add annotation
			Uncertainties uncertainties = WriterUtils.estModel2Uncertainties(estModel);
			model.setAnnotation(new Model2Annotation(globalModelID, uncertainties, emLits).getAnnotation());

			return new ManualSecondaryModel(docName, doc);
		}
	}

	private static class TwoStepTertiaryModelParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			List<TwoStepTertiaryModel> tms = new LinkedList<>();

			// Sort global models
			Map<Integer, Map<Integer, List<KnimeTuple>>> gms = TableReader.sortGlobalModels(tuples);

			for (Map<Integer, List<KnimeTuple>> tertiaryInstances : gms.values()) {
				List<List<KnimeTuple>> tuplesList = new LinkedList<>();
				for (List<KnimeTuple> tertiaryInstance : tertiaryInstances.values()) {
					tuplesList.add(tertiaryInstance);
				}
				// We have a list of tertiary instances. Each instance has the
				// same
				// microbial data yet different data. Then we'll create a
				// TwoTertiaryModel from the first instance and create the data
				// from
				// every instance.
				int modelNum = tms.size();
				TwoStepTertiaryModel tm = parse(tuplesList, isPMFX, modelNum, mdName, metadata, notes);
				tms.add(tm);
			}

			if (splitModels) {
				for (int numModel = 0; numModel < tms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					Path path = Paths.get(dir, modelName + (isPMFX ? ".pmfx" : ".pmf"));
					TwoStepTertiaryModelFile.write(path, tms.subList(numModel, numModel + 1));
				}
			} else {
				Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
				TwoStepTertiaryModelFile.write(path, tms);
			}
		}

		private static TwoStepTertiaryModel parse(List<List<KnimeTuple>> tupleList, boolean isPMFX, int modelNum,
				String mdName, Metadata metadata, String notes) {

			final String modelExtension = isPMFX ? "pmf" : "sbml";

			List<PrimaryModelWData> primModels = new LinkedList<>();
			List<SBMLDocument> secDocs = new LinkedList<>();

			// Parse primary models and their data from every instance. Each
			// instance has an unique primary model and data set
			for (List<KnimeTuple> instance : tupleList) {
				// Get first tuple: All the tuples of an instance have the same
				// primary model
				KnimeTuple tuple = instance.get(0);
				int instanceNum = primModels.size();
				PrimaryModelWData pm;

				Model1Parser m1Parser = new Model1Parser(tuple, metadata, notes);

				SBMLDocument sbmlDoc = m1Parser.getDocument();
				String sbmlDocName = String.format("%s_%d_%d.%s", mdName, modelNum, instanceNum, modelExtension);
				XMLNode metadataNode = sbmlDoc.getModel().getAnnotation().getNonRDFannotation()
						.getChildElement("metadata", "");

				if (tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).size() > 0) {
					NuMLDocument numlDoc = new DataParser(tuple, metadata, notes).getDocument();
					String numlDocName = String.format("%s_%d_%d.numl", mdName, modelNum, instanceNum);

					// Adds DataSourceNode to the model
					metadataNode.addChild(new DataSourceNode(numlDocName).getNode());

					primModels.add(new PrimaryModelWData(sbmlDocName, sbmlDoc, numlDocName, numlDoc));

				} else {
					pm = new PrimaryModelWData(sbmlDocName, sbmlDoc, null, null);
					primModels.add(pm);
				}
			}

			// Parse secondary models from the first instance (all the instance
			// have
			// the same secondary models)
			List<KnimeTuple> firstInstance = tupleList.get(0);
			for (KnimeTuple tuple : firstInstance) {
				SBMLDocument secDoc = new Model2Parser(tuple, metadata, notes).getDocument();

				// Adds annotations for the primary models
				XMLNode metadataNode = secDoc.getModel().getAnnotation().getNonRDFannotation()
						.getChildElement("metadata", "");
				for (PrimaryModelWData pm : primModels) {
					if (pm.getDataDocName() != null) {
						metadataNode.addChild(new PrimaryModelNode(pm.getModelDocName()).getNode());
					}
				}

				secDocs.add(secDoc);
			}

			// Creates tertiary model
			String tertDocName = String.format("%s_%s.%s", mdName, modelNum, modelExtension);
			SBMLDocument tertDoc = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);
			// Enable Hierarchical Compositon package
			tertDoc.enablePackage(CompConstants.shortLabel);
			CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			TableReader.addNamespaces(tertDoc);

			// Adds document annotation
			tertDoc.setAnnotation(new MetadataAnnotation(metadata).getAnnotation());

			Model model = tertDoc.createModel("model");
			KnimeTuple aTuple = tupleList.get(0).get(0);

			// Builds metadata node
			XMLTriple metadataTriple = new XMLTriple("metadata", null, "pmf");
			XMLNode metadataNode = new XMLNode(metadataTriple);
			model.getAnnotation().setNonRDFAnnotation(metadataNode);

			// Builds global model id node
			int gmId = aTuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			metadataNode.addChild(new GlobalModelIdNode(gmId).getNode());

			// Get literature references
			PmmXmlDoc litDoc = aTuple.getPmmXml(Model1Schema.ATT_EMLIT);
			List<LiteratureItem> lits = new LinkedList<>();
			for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
				lits.add((LiteratureItem) item);
			}

			// Builds reference nodes
			for (LiteratureItem lit : lits) {
				Reference ref = WriterUtils.literatureItem2Reference(lit);
				metadataNode.addChild(new ReferenceSBMLNode(ref).getNode());
			}

			// Gets a primary model
			Model primModel = primModels.get(0).getModelDoc().getModel();

			// Adds species
			Species species = primModel.getSpecies(0);
			model.addSpecies(new Species(species));

			// Adds compartment
			Compartment compartment = primModel.getCompartment(0);
			model.addCompartment(new Compartment(compartment));

			// Adds rule
			AssignmentRule rule = (AssignmentRule) primModel.getRule(0);
			model.addRule(new AssignmentRule(rule));

			// Assigns parameters of the primary model
			for (Parameter p : primModel.getListOfParameters()) {
				Parameter p2 = new Parameter(p);
				if (p2.isSetAnnotation()) {
					p2.setAnnotation(new Annotation());
				}
				model.addParameter(p2);
			}

			CompModelPlugin modelPlugin = (CompModelPlugin) model.getPlugin(CompConstants.shortLabel);

			// Creates ExternalModelDefinition
			List<String> secDocNames = new LinkedList<>();
			for (SBMLDocument secDoc : secDocs) {
				// Gets model definition id from secDoc
				String mdId = secDoc.getModel().getId();

				String secDocName = String.format("%s.%s", secDoc.getModel().getId(), modelExtension);
				secDocNames.add(secDocName);

				// Creates and adds an ExternalModelDefinition to the tertiary
				// model
				ExternalModelDefinition emd = compDocPlugin.createExternalModelDefinition(mdId);
				emd.setSource(secDocName);
				emd.setModelRef(mdId);

				String depId = ((AssignmentRule) secDoc.getModel().getRule(0)).getVariable();

				// Creates submodel
				Submodel submodel = modelPlugin.createSubmodel("submodel_" + depId);
				submodel.setModelRef(mdId);

				Parameter parameter = model.getParameter(depId);

				CompSBasePlugin plugin = (CompSBasePlugin) parameter.getPlugin(CompConstants.shortLabel);
				ReplacedBy replacedBy = plugin.createReplacedBy();
				replacedBy.setIdRef(depId);
				replacedBy.setSubmodelRef(submodel.getId());
			}

			// Assigns unit definitions of the primary model
			model.setListOfUnitDefinitions(new ListOf<UnitDefinition>(primModel.getListOfUnitDefinitions()));

			TwoStepTertiaryModel tstm = new TwoStepTertiaryModel(tertDocName, tertDoc, primModels, secDocNames,
					secDocs);
			return tstm;
		}
	}

	/**
	 * One Step Fit Tertiary Model
	 * 
	 * @author Miguel Alba
	 */
	private static class OneStepTertiaryModelParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			List<OneStepTertiaryModel> tms = new LinkedList<>();

			// Sort global models
			Map<Integer, Map<Integer, List<KnimeTuple>>> gms = TableReader.sortGlobalModels(tuples);

			// Parse tertiary models
			for (Map<Integer, List<KnimeTuple>> tertiaryInstances : gms.values()) {
//				List<List<KnimeTuple>> tuplesList = new LinkedList<>();
//				for (List<KnimeTuple> tertiaryInstance : tertiaryInstances.values()) {
//					tuplesList.add(tertiaryInstance);
//				}
				List<List<KnimeTuple>> tuplesList = new LinkedList<>(tertiaryInstances.values());
				/**
				 * We have a list of tertiary instances. Each instance has the
				 * same microbial data yet different data. Then we'll create a
				 * TwoTertiaryModel from the first instance and create the data
				 * from every instance.
				 */
				int mdNum = tms.size();
				OneStepTertiaryModel tm = parse(tuplesList, isPMFX, mdName, mdNum, metadata, notes);
				tms.add(tm);
			}

			if (splitModels) {
				for (int numModel = 0; numModel < tms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					Path path = Paths.get(dir, modelName + (isPMFX ? ".pmfx" : ".pmf"));
					OneStepTertiaryModelFile.write(path, tms.subList(numModel, numModel + 1));
				}
			} else {
				Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
				OneStepTertiaryModelFile.write(path, tms);
			}
		}

		private static OneStepTertiaryModel parse(List<List<KnimeTuple>> tupleList, boolean isPMFX, String mdName,
				int mdNum, Metadata metadata, String notes) {

			final String modelExtension = isPMFX ? "pmf" : "sbml";

			List<String> numlDocNames = new LinkedList<>();
			List<NuMLDocument> numlDocs = new LinkedList<>();
			for (List<KnimeTuple> instance : tupleList) {
				// Get first tuple: All the tuples of an instance have the same
				// data
				KnimeTuple tuple = instance.get(0);
				if (tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).size() > 0) {
					int dataCounter = numlDocs.size();
					String numlDocName = String.format("data_%d_%d.numl", mdNum, dataCounter);
					numlDocNames.add(numlDocName);

					DataParser dataParser = new DataParser(tuple, metadata, notes);
					NuMLDocument numlDoc = dataParser.getDocument();
					numlDocs.add(numlDoc);
				}
			}

			// We'll get microbial data from the first instance
			List<KnimeTuple> firstInstance = tupleList.get(0);
			// and the primary model from the first tuple
			KnimeTuple firstTuple = firstInstance.get(0);

			Model1Parser m1Parser = new Model1Parser(firstTuple, metadata, notes);
			SBMLDocument tertDoc = m1Parser.getDocument();
			String tertDocName = String.format("%s_%s.%s", mdName, mdNum, modelExtension);
			CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);

			// Adds DataSourceNode to the tertiary model
			XMLNode tertMetadataNode = tertDoc.getModel().getAnnotation().getNonRDFannotation()
					.getChildElement("metadata", "");
			for (String numlDocName : numlDocNames) {
				tertMetadataNode.addChild(new DataSourceNode(numlDocName).getNode());
			}

			CompModelPlugin modelPlugin = (CompModelPlugin) tertDoc.getModel().getPlugin(CompConstants.shortLabel);

			// Add submodels and model definitions
			List<String> secDocNames = new LinkedList<>();
			List<SBMLDocument> secDocs = new LinkedList<>();
			for (KnimeTuple tuple : firstInstance) {

				SBMLDocument secDoc = new Model2Parser(tuple, metadata, notes).getDocument();

				String secModelId = secDoc.getModel().getId();
				String secDocName = String.format("%s.%s", secModelId, modelExtension);

				secDocNames.add(secDocName);
				secDocs.add(secDoc);

				// Creates and adds an ExternalModelDefinition
				ExternalModelDefinition emd = compDocPlugin.createExternalModelDefinition(secModelId);
				emd.setSource(secDocName);
				emd.setModelRef(secModelId);

				String depId = ((AssignmentRule) secDoc.getModel().getRule(0)).getVariable();

				Submodel submodel = modelPlugin.createSubmodel("submodel_" + depId);
				submodel.setModelRef(emd.getId());

				Parameter parameter = tertDoc.getModel().getParameter(depId);

				CompSBasePlugin plugin = (CompSBasePlugin) parameter.getPlugin(CompConstants.shortLabel);
				ReplacedBy replacedBy = plugin.createReplacedBy();
				replacedBy.setIdRef(depId);
				replacedBy.setSubmodelRef(submodel.getId());

				// Add annotation for the primary model
				XMLNode secMetadataNode = secDoc.getModel().getAnnotation().getNonRDFannotation()
						.getChildElement("metadata", "");
				secMetadataNode.addChild(new PrimaryModelNode(tertDocName).getNode());

				// Adds DataSourceNodes to the sec model
				for (String numlDocName : numlDocNames) {
					secMetadataNode.addChild(new DataSourceNode(numlDocName).getNode());
				}
			}

			OneStepTertiaryModel tstm = new OneStepTertiaryModel(tertDocName, tertDoc, secDocNames, secDocs,
					numlDocNames, numlDocs);
			return tstm;
		}
	}

	private static class ManualTertiaryModelParser implements Parser {

		@Override
		public void write(List<KnimeTuple> tuples, boolean isPMFX, String dir, String mdName, Metadata metadata,
				boolean splitModels, String notes, ExecutionContext exec) throws Exception {

			List<ManualTertiaryModel> tms = new LinkedList<>();

			// Sort tertiary models
			Map<Integer, List<KnimeTuple>> tertiaryModelMap = new HashMap<>();
			for (KnimeTuple tuple : tuples) {
				int primModelId = ((EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL).get(0)).id;

				// primary model in tuple is already in tertiaryModelMap
				if (tertiaryModelMap.containsKey(primModelId)) {
					tertiaryModelMap.get(primModelId).add(tuple);
				} else {
					LinkedList<KnimeTuple> tupleList = new LinkedList<>();
					tupleList.add(tuple);
					tertiaryModelMap.put(primModelId, tupleList);
				}
			}

			for (List<KnimeTuple> tupleList : tertiaryModelMap.values()) {
				int modelCounter = tms.size();
				tms.add(parse(tupleList, isPMFX, mdName, modelCounter, metadata, notes));
			}

			if (splitModels) {
				for (int numModel = 0; numModel < tms.size(); numModel++) {
					String modelName = mdName + Integer.toString(numModel);
					Path path = Paths.get(dir, modelName + (isPMFX ? ".pmfx" : ".pmf"));
					ManualTertiaryModelFile.write(path, tms.subList(numModel, numModel + 1));
				}
			} else {
				Path path = Paths.get(dir, mdName + (isPMFX ? ".pmfx" : ".pmf"));
				ManualTertiaryModelFile.write(path, tms);
			}
		}

		private static ManualTertiaryModel parse(List<KnimeTuple> tupleList, boolean isPMFX, String mdName,
				int modelNum, Metadata metadata, String notes) {

			final String modelExtension = isPMFX ? "pmf" : "sbml";

			// We'll get microbial data from the primary of the first tuple
			KnimeTuple firstTuple = tupleList.get(0);

			// Creates SBMLDocument for the tertiary model
			Model1Parser m1Parser = new Model1Parser(firstTuple, metadata, notes);
			SBMLDocument tertDoc = m1Parser.getDocument();
			String tertDocName = String.format("%s_%s.%s", mdName, modelNum, modelExtension);

			CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) tertDoc.getPlugin(CompConstants.shortLabel);
			CompModelPlugin compModelPlugin = (CompModelPlugin) tertDoc.getModel().getPlugin(CompConstants.shortLabel);

			// Add submodels and model definitions
			List<String> secDocNames = new LinkedList<>();
			List<SBMLDocument> secDocs = new LinkedList<>();

			for (KnimeTuple tuple : tupleList) {

				Model2Parser m2Parser = new Model2Parser(tuple, metadata, notes);
				SBMLDocument secDoc = m2Parser.getDocument();

				String emdId = secDoc.getModel().getId();
				String secDocName = String.format("%s_%s_%s.%s", mdName, modelNum, emdId, modelExtension);

				secDocNames.add(secDocName);
				secDocs.add(secDoc);

				// Creates ExternalModelDefinition
				ExternalModelDefinition emd = new ExternalModelDefinition(emdId, TableReader.LEVEL,
						TableReader.VERSION);
				emd.setSource(secDocName);
				emd.setModelRef(emdId);

				compDocPlugin.addExternalModelDefinition(emd);

				Submodel submodel = compModelPlugin.createSubmodel(emdId);
				submodel.setModelRef(emdId);

				String depId = ((AssignmentRule) secDoc.getModel().getRule(0)).getVariable();
				Parameter parameter = tertDoc.getModel().getParameter(depId);

				CompSBasePlugin plugin = (CompSBasePlugin) parameter.getPlugin(CompConstants.shortLabel);
				ReplacedBy replacedBy = plugin.createReplacedBy();
				replacedBy.setIdRef(depId);
				replacedBy.setSubmodelRef(emdId);
			}

			ManualTertiaryModel mtm = new ManualTertiaryModel(tertDocName, tertDoc, secDocNames, secDocs);
			return mtm;
		}
	}

}
