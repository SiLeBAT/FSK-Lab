package de.bund.bfr.knime.pmm.common.writer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.pmfml.PMFUtil;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.MetadataAnnotation;
import de.bund.bfr.pmfml.sbml.Model1Annotation;
import de.bund.bfr.pmfml.sbml.PMFCoefficient;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.ReferenceImpl;
import de.bund.bfr.pmfml.sbml.Uncertainties;

public class Model1Parser {

	SBMLDocument sbmlDocument;

	public Model1Parser(KnimeTuple tuple, Metadata metadata, String notes) {

		TableReader.replaceCelsiusAndFahrenheit(tuple);
		TableReader.renameLog(tuple);

		// Retrieves TimeSeriesSchema cells
		AgentXml agentXml = (AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0);
		String condId = Integer.toString(tuple.getInt(TimeSeriesSchema.ATT_CONDID));
		PmmXmlDoc miscDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

		// Retrieves Model1Schema cells
		CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
		IndepXml indep = (IndepXml) tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).get(0);
		PmmXmlDoc paramsDoc = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);

		String modelId = PMFUtil.createId("model" + estModel.id);

		// Creates SBMLDocument for the primary model
		sbmlDocument = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);

		// Enables Hierarchical Composition package and adds namespaces
		sbmlDocument.enablePackage(CompConstants.shortLabel);
		TableReader.addNamespaces(sbmlDocument);

		// Adds document annotation
		sbmlDocument.setAnnotation(new MetadataAnnotation(metadata).getAnnotation());

		// Creates model and names it
		Model model = sbmlDocument.createModel(modelId);
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

		// Gets model references
		List<LiteratureItem> mLits = new LinkedList<>();
		PmmXmlDoc mLitDoc = tuple.getPmmXml(Model1Schema.ATT_MLIT);
		for (PmmXmlElementConvertable item : mLitDoc.getElementSet()) {
			mLits.add((LiteratureItem) item);
		}

		// Gets estimated model references
		List<Reference> emLits = new LinkedList<>();
		PmmXmlDoc emLitDoc = tuple.getPmmXml(Model1Schema.ATT_EMLIT);
		for (PmmXmlElementConvertable item : emLitDoc.getElementSet()) {
			emLits.add(WriterUtils.literatureItem2Reference((LiteratureItem) item));
		}

		Uncertainties uncertainties = WriterUtils.estModel2Uncertainties(estModel);
		model.setAnnotation(new Model1Annotation(uncertainties, emLits, condId).getAnnotation());

		// Creates and adds compartment to model
		PMFCompartment compartment = WriterUtils.matrixXml2Compartment(matrixXml, miscDoc);

		model.addCompartment(compartment.getCompartment());

		// Creates species and adds it to the model
		PMFSpecies species = WriterUtils.createSpecies(agentXml, dep.unit, compartment.getId());
		model.addSpecies(species.getSpecies());

		// Adds dep constraint
		LimitsConstraint depLc = new LimitsConstraint(species.getId(), dep.min, dep.max);
		if (depLc.getConstraint() != null) {
			model.addConstraint(depLc.getConstraint());
		}

		// Adds independent parameter
		Parameter indepParam = new Parameter(Categories.getTime());
		indepParam.setValue(0.0);
		indepParam.setConstant(false);
		indepParam.setUnits(indep.getUnit());
		model.addParameter(indepParam);

		// Adds indep constraint
		if (!indep.getName().isEmpty()) {
			LimitsConstraint lc = new LimitsConstraint(indep.getName(), indep.getMin(), indep.getMax());
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		// Adds constant parameters
		List<ParamXml> constXmls = new LinkedList<>();
		for (PmmXmlElementConvertable item : paramsDoc.getElementSet()) {
			constXmls.add((ParamXml) item);
		}

		for (ParamXml paramXml : constXmls) {
			// Adds constant parameter
			PMFCoefficient coefficient = WriterUtils.paramXml2Coefficient(paramXml);
			model.addParameter(coefficient.getParameter());

			// Adds constraint
			LimitsConstraint lc = new LimitsConstraint(paramXml.getName(), paramXml.getMin(), paramXml.getMax());
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		// Adds unit definitions
		List<IndepXml> indepXmls = new LinkedList<>(Arrays.asList(indep));
		try {
			TableReader.addUnitDefinitions(model, dep, indepXmls, constXmls);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Creates rule of the model and adds it to the rest of rules
		Reference[] modelReferences = new ReferenceImpl[mLits.size()];
		for (int i = 0; i < mLits.size(); i++) {
			modelReferences[i] = WriterUtils.literatureItem2Reference(mLits.get(i));
		}

		model.addRule(WriterUtils.createM1Rule(catModel, species.getId(), modelReferences).getRule());
	}

	public SBMLDocument getDocument() {
		return sbmlDocument;
	}
}
