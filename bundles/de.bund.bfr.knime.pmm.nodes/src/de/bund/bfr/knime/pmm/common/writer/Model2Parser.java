package de.bund.bfr.knime.pmm.common.writer;

import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.MetadataAnnotation;
import de.bund.bfr.pmfml.sbml.Model2Annotation;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.PMFCoefficient;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.SecDep;
import de.bund.bfr.pmfml.sbml.SecIndep;
import de.bund.bfr.pmfml.sbml.Uncertainties;

public class Model2Parser {

	SBMLDocument sbmlDocument;

	public Model2Parser(KnimeTuple tuple, Metadata metadata, String notes) {

		TableReader.replaceCelsiusAndFahrenheit(tuple);
		TableReader.renameLog(tuple);

		// Retrieve Model2Schema cells
		CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) tuple.getPmmXml(Model2Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0);
		PmmXmlDoc indepDoc = tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
		PmmXmlDoc paramsDoc = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);
		PmmXmlDoc mLitDoc = tuple.getPmmXml(Model2Schema.ATT_MLIT);
		PmmXmlDoc emLitDoc = tuple.getPmmXml(Model2Schema.ATT_EMLIT);
		int globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

		// Creates SBMLDocument for the secondary model
		sbmlDocument = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);

		// Enables Hierarchical Composition package and adds namespaces
		sbmlDocument.enablePackage(CompConstants.shortLabel);
		TableReader.addNamespaces(sbmlDocument);

		// Adds document annotation
		sbmlDocument.setAnnotation(new MetadataAnnotation(metadata).getAnnotation());
		if (notes != null) {
			try {
				sbmlDocument.setNotes(notes);
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}

		// Creates model and names it
		Model model = sbmlDocument.createModel("model_" + dep.name);
		if (estModel.getName() != null) {
			model.setName(estModel.getName());
		}

		// Gets model references
		Reference[] mLits = new Reference[mLitDoc.size()];
		for (int i = 0; i < mLitDoc.size(); i++) {
			mLits[i] = WriterUtils.literatureItem2Reference((LiteratureItem) mLitDoc.get(i));
		}

		// Gets estimated model references
		Reference[] emLits = new Reference[emLitDoc.size()];
		for (int i = 0; i < emLitDoc.size(); i++) {
			emLits[i] = WriterUtils.literatureItem2Reference((LiteratureItem) emLitDoc.get(i));
		}

		// Adds model annotation
		Uncertainties uncertainties = WriterUtils.estModel2Uncertainties(estModel);
		model.setAnnotation(new Model2Annotation(globalModelID, uncertainties, emLits).getAnnotation());

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

		// Adds dep
		Parameter depParam = new SecDep(dep.name, dep.description, dep.unit).getParam();
		// Adds dep constraint
		LimitsConstraint depLc = new LimitsConstraint(dep.name, dep.min, dep.max);
		if (depLc.getConstraint() != null) {
			model.addConstraint(depLc.getConstraint());
		}
		model.addParameter(depParam);

		// Adds independent parameters
		for (IndepXml indepXml : indepXmls) {
			// Creates SBML parameter
			SecIndep secIndep = new SecIndep(indepXml.getName(), indepXml.getDescription(), indepXml.getUnit());
			model.addParameter(secIndep.getParam());
			// Adds constraint
			LimitsConstraint lc = new LimitsConstraint(indepXml.getName(), indepXml.getMin(), indepXml.getMax());
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

		// Adds unit definitions
		try {
			TableReader.addUnitDefinitions(model, dep, indepXmls, constXmls);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Creates rule of the model and adds it to the rest of rules
		ModelRule rule = WriterUtils.createM2Rule(catModel, mLits);
		model.addRule(rule.getRule());
	}

	public SBMLDocument getDocument() {
		return sbmlDocument;
	}
}