package de.bund.bfr.knime.pmm.common.reader;

import java.util.Map;

import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.Model2Metadata;
import de.bund.bfr.knime.pmm.extendedtable.items.AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.AgentXml.Type;
import de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.pmfml.sbml.Correlation;
import de.bund.bfr.pmfml.sbml.Limits;
import de.bund.bfr.pmfml.sbml.Model2Annotation;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.PMFCoefficient;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.SBMLFactory;
import de.bund.bfr.pmfml.sbml.SecDep;
import de.bund.bfr.pmfml.sbml.SecIndep;

public class Model2Tuple {

	public KnimeTuple knimeTuple;
	private static KnimeSchema schema = SchemaFactory.createM2Schema();

	public Model2Tuple(Model model) {

		Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());

		// Parses rule
		ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = ReaderUtils.model2Rule2CatModel(rule);

		// Parses dep
		String depName = rule.getRule().getVariable();

		SecDep secDep = new SecDep(model.getParameter(depName));
		DepXml depXml = new DepXml(secDep.getParam().getId());
		depXml.description = secDep.getDescription();
		if (secDep.getParam().isSetUnits()) {
			// Adds unit
			String unitID = secDep.getParam().getUnits();
			String unitName = model.getUnitDefinition(unitID).getName();
			depXml.unit = unitName;

			// Adds unit category
			Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
			if (dbUnits.containsKey(unitName)) {
				UnitsFromDB dbUnit = dbUnits.get(unitName);
				depXml.category = dbUnit.kindOfPropertyQuantity;
			}

			// Adds limits
			if (limits.containsKey(secDep.getParam().getId())) {
				Limits depLimits = limits.get(secDep.getParam().getId());
				depXml.max = depLimits.getMax();
				depXml.min = depLimits.getMin();
			}
		}

		PmmXmlDoc indeps = new PmmXmlDoc();
		PmmXmlDoc consts = new PmmXmlDoc();

		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				ParamXml paramXml = processCoefficient(param, model.getListOfUnitDefinitions(), limits);
				consts.add(paramXml);
			} else if (!param.getId().equals(depName)) {
				IndepXml indepXml = processIndep(param, model.getListOfUnitDefinitions(), limits);
				indeps.add(indepXml);
			}
		}

		// Get model annotations
		Model2Annotation m2Annot = new Model2Annotation(model.getAnnotation());

		// EstModel
		EstModelXml estModel = ReaderUtils.uncertainties2EstModel(m2Annot.getUncertainties());
		if (model.isSetName()) {
			estModel.name = model.getName();
		}

		Model2Metadata metadata = new Model2Metadata();

		if (model.getListOfSpecies().size() == 1) {
			PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
			metadata.agentXml = new AgentXml(Type.Model2, MathUtilities.getRandomNegativeInt(), species.getName(),
					species.getDetail(), null);
		}

		if (model.getListOfCompartments().size() == 1) {
			PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
			metadata.matrixXml = new MatrixXml(MatrixXml.Type.Model2, MathUtilities.getRandomNegativeInt(),
					compartment.getName(), compartment.getDetail(), null);
		}

		// Gets model literature
		PmmXmlDoc mLits = new PmmXmlDoc();
		for (Reference ref : rule.getReferences()) {
			final String author = ref.getAuthor();
			final Integer year = ref.getYear();
			final String title = ref.getTitle();
			final String abstractText = ref.getAbstractText();
			final String journal = ref.getJournal();
			final String volume = ref.getVolume();
			final String issue = ref.getIssue();
			final Integer page = ref.getPage();
			final Integer approvalMode = ref.getApprovalMode();
			final String website = ref.getWebsite();
			final Integer type = ref.isSetType() ? ref.getType().value() : null;
			final String comment = ref.getComment();

			LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			mLits.add(literatureItem);

			de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem mLiteratureItem = new de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem(
					de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem.Type.M, author, year, title, abstractText,
					journal, volume, issue, page, approvalMode, website, type, comment);
			metadata.modelLiteratureItems.add(mLiteratureItem);
		}

		// Gets estimated model literature
		PmmXmlDoc emLits = new PmmXmlDoc();
		for (Reference ref : m2Annot.getReferences()) {
			final String author = ref.getAuthor();
			final Integer year = ref.getYear();
			final String title = ref.getTitle();
			final String abstractText = ref.getAbstractText();
			final String journal = ref.getJournal();
			final String volume = ref.getVolume();
			final String issue = ref.getIssue();
			final Integer page = ref.getPage();
			final Integer approvalMode = ref.getApprovalMode();
			final String website = ref.getWebsite();
			final Integer type = ref.isSetType() ? ref.getType().value() : null;
			final String comment = ref.getComment();

			LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
					approvalMode, website, type, comment);
			emLits.add(lit);

			de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem emLiteratureItem = new de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem(
					de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem.Type.EM, author, year, title, abstractText,
					journal, volume, issue, page, approvalMode, website, type, comment);
			metadata.estimatedModelLiteratureItems.add(emLiteratureItem);
		}

		knimeTuple = new KnimeTuple(schema);
		knimeTuple.setValue(Model2Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		knimeTuple.setValue(Model2Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		knimeTuple.setValue(Model2Schema.ATT_INDEPENDENT, indeps);
		knimeTuple.setValue(Model2Schema.ATT_PARAMETER, consts);
		knimeTuple.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		knimeTuple.setValue(Model2Schema.ATT_MLIT, mLits);
		knimeTuple.setValue(Model2Schema.ATT_EMLIT, emLits);
		knimeTuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		knimeTuple.setValue(Model2Schema.ATT_DBUUID, "?");
		knimeTuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, m2Annot.getGlobalModelID());
		knimeTuple.setValue(Model2Schema.ATT_METADATA, metadata);
	}

	private ParamXml processCoefficient(Parameter param, ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {
		// Creates ParamXml and adds description
		ParamXml paramXml = new ParamXml(param.getId(), null, param.getValue());

		// Assigns unit and category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			paramXml.unit = unitName;
			paramXml.category = DBUnits.getDBUnits().get(unitName).kindOfPropertyQuantity;
		}

		PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
		if (coefficient.isSetDescription()) {
			paramXml.description = coefficient.getDescription();
		}

		// Adds correlations
		if (coefficient.isSetCorrelations()) {
			for (Correlation corr : coefficient.getCorrelations()) {
				paramXml.correlations.put(corr.getName(), corr.getValue());
			}
		}

		// Adds limits
		if (limits.containsKey(param.getId())) {
			Limits constLimits = limits.get(param.getId());
			paramXml.max = constLimits.getMax();
			paramXml.min = constLimits.getMin();
		}

		if (coefficient.isSetIsStart()) {
			paramXml.isStartParam = coefficient.getIsStart();
		}

		return paramXml;
	}

	private IndepXml processIndep(Parameter param, ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {

		// Adds limits
		Double min = null;
		Double max = null;
		if (limits.containsKey(param.getId())) {
			Limits indepLimits = limits.get(param.getId());
			min = indepLimits.getMin();
			max = indepLimits.getMax();
		}

		IndepXml indepXml = new IndepXml(param.getId(), min, max);

		SecIndep secIndep = new SecIndep(param);
		indepXml.description = secIndep.getDescription();

		// Adds unit and unit category
		String unitID = param.getUnits();
		if (!unitID.equals("dimensionless")) {
			String unitName = unitDefs.get(unitID).getName();
			indepXml.unit = unitName;

			if (DBUnits.getDBUnits().containsKey(unitName)) {
				UnitsFromDB ufdb = DBUnits.getDBUnits().get(unitName);
				indepXml.category = ufdb.kindOfPropertyQuantity;
			}
		}

		return indepXml;
	}
}
