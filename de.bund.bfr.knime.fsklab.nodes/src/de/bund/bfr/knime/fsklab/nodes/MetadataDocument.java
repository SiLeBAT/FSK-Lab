package de.bund.bfr.knime.fsklab.nodes;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.jsoup.Jsoup;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.InitialAssignment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.arrays.ArraysConstants;
import org.sbml.jsbml.ext.arrays.ArraysSBasePlugin;
import org.sbml.jsbml.ext.arrays.Dimension;
import org.sbml.jsbml.ext.arrays.Index;
import org.sbml.jsbml.util.filters.Filter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.PMFUtil;
import de.bund.bfr.pmfml.sbml.Limits;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.ReferenceSBMLNode;
import de.bund.bfr.pmfml.sbml.SBMLFactory;

public class MetadataDocument {

	public SBMLDocument doc;
	
	public MetadataDocument(FskMetaData template) {
		// Creates SBMLDocument for the primary model
		doc = new SBMLDocument(3, 1);

		// Adds namespaces to the sbmlDocument
		doc.addDeclaredNamespace("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		doc.addDeclaredNamespace("xmlns:pmml", "http://www.dmg.org/PMML-4_2");
		doc.addDeclaredNamespace("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		doc.addDeclaredNamespace("xmlns:dc", "http://purl.org/dc/elements/1.1");
		doc.addDeclaredNamespace("xmlns:dcterms", "http://purl.org/dc/terms/");
		doc.addDeclaredNamespace("xmlns:pmmlab",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		doc.addDeclaredNamespace("xmlns:numl", "http://www.numl.org/numl/level1/version1");
		doc.addDeclaredNamespace("xmlns:xlink", "http//www.w3.org/1999/xlink");

		// Adds document annotation
		doc.setAnnotation(new MetadataAnnotation(template).annotation);

		// Creates model and names it
		Model model = doc.createModel(PMFUtil.createId(template.modelId));
		if (template.modelName != null && !template.modelName.isEmpty()) {
			model.setName(template.modelName);
		}

		// Sets model notes
		if (template.notes != null && !template.notes.isEmpty()) {
			try {
				model.setNotes(template.notes);
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}

		// Creates and adds compartment to the model
		PMFCompartment compartment = SBMLFactory.createPMFCompartment(PMFUtil.createId(template.matrix),
				template.matrix);
		compartment.setDetail(template.matrixDetails);
		model.addCompartment(compartment.getCompartment());

		// Creates and adds species to the model
		String speciesId = PMFUtil.createId(template.organism);
		String speciesName = template.organism;
		String speciesUnit = PMFUtil.createId(template.dependentVariable.unit);
		PMFSpecies species = SBMLFactory.createPMFSpecies(compartment.getId(), speciesId, speciesName, speciesUnit);
		model.addSpecies(species.getSpecies());

		// Add unit definitions here (before parameters)
		Set<String> unitsSet = new LinkedHashSet<>();
		unitsSet.add(template.dependentVariable.unit.trim());
		template.independentVariables.forEach(v -> unitsSet.add(v.unit.trim()));
		for (String unit : unitsSet) {
			UnitDefinition ud = model.createUnitDefinition(PMFUtil.createId(unit));
			ud.setName(unit);
		}

		// Adds dep parameter
		Parameter depParam = new Parameter(PMFUtil.createId(template.dependentVariable.name));
		depParam.setName(template.dependentVariable.name);
		depParam.setUnits(PMFUtil.createId(template.dependentVariable.unit));
		model.addParameter(depParam);

		// Adds dep constraint
		try {
			double min = Double.parseDouble(template.dependentVariable.min);
			double max = Double.parseDouble(template.dependentVariable.max);
			LimitsConstraint lc = new LimitsConstraint(template.dependentVariable.name.replaceAll("\\.", "\\_"), min,
					max);
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		// Adds independent parameters
		for (Variable v : template.independentVariables) {
			String var = v.name;
			Parameter param = model.createParameter(PMFUtil.createId(var));
			param.setName(var);

			switch (v.type) {
			case integer:
				param.setValue(Integer.parseInt(v.value));
				break;
			case numeric:
				param.setValue(Double.parseDouble(v.value));
				break;
			case array:
				// TODO: Add array
				try {
					param.setValue(0);
					addArrayToParameter(param, v.value, v.name);	
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				break;
			case character:
				// TODO: Add character
				break;
			}

			try {
				param.setUnits(PMFUtil.createId(v.unit));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			try {
				double min = Double.parseDouble(v.min);
				double max = Double.parseDouble(v.max);
				LimitsConstraint lc = new LimitsConstraint(param.getId(), min, max);
				if (lc.getConstraint() != null) {
					model.addConstraint(lc.getConstraint());
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		// Add rule
		String formulaName = "Missing formula name";
		ModelClass modelClass = template.subject == null ? ModelClass.UNKNOWN : template.subject;
		int modelId = -new Random().nextInt(Integer.MAX_VALUE);
		Reference[] references = new Reference[0];

		AssignmentRule rule = new AssignmentRule(3, 1);
		rule.setVariable(depParam.getId());
		rule.setAnnotation(new ModelRuleAnnotation(formulaName, modelClass, modelId, references).annotation);
		model.addRule(rule);
	}
	
	private static class ModelRuleAnnotation {

		private Annotation annotation;

		private static final String FORMULA_TAG = "formulaName";
		private static final String SUBJECT_TAG = "subject";
		private static final String PMMLAB_ID = "pmmlabID";

		private ModelRuleAnnotation(String formulaName, ModelClass modelClass, int pmmlabID, Reference[] references) {
			// Builds metadata node
			XMLNode metadataNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));
			this.annotation = new Annotation();
			this.annotation.setNonRDFAnnotation(metadataNode);

			// Creates annotation for formula name
			XMLNode nameNode = new XMLNode(new XMLTriple(FORMULA_TAG, null, "pmmlab"));
			nameNode.addChild(new XMLNode(formulaName));
			metadataNode.addChild(nameNode);

			// Creates annotation for modelClass
			XMLNode modelClassNode = new XMLNode(new XMLTriple(SUBJECT_TAG, null, "pmmlab"));
			modelClassNode.addChild(new XMLNode(modelClass.fullName()));
			metadataNode.addChild(modelClassNode);

			// Create annotation for pmmlabID
			XMLNode idNode = new XMLNode(new XMLTriple(PMMLAB_ID, null, "pmmlab"));
			idNode.addChild(new XMLNode(new Integer(pmmlabID).toString()));
			metadataNode.addChild(idNode);

			// Builds reference nodes
			for (Reference ref : references) {
				metadataNode.addChild(new ReferenceSBMLNode(ref).getNode());
			}
		}
	}
	
	private static void addArrayToParameter(Parameter parameter, String value, String var) throws ParseException {
		String cleanArray = value.substring(2, value.length() - 1);
		String[] tokens = cleanArray.split(",");
		List<Double> array = Arrays.stream(tokens).map(Double::parseDouble).collect(Collectors.toList());
		int size = tokens.length;

		// Create dimension within parameter
		ArraysSBasePlugin arrayPlugin = (ArraysSBasePlugin) parameter.getPlugin(ArraysConstants.shortLabel);
		Dimension dim = arrayPlugin.createDimension("d0");
		dim.setSize(Integer.toString(size));
		dim.setArrayDimension(0);

		// Create initial assignment
		InitialAssignment ia = parameter.getModel().createInitialAssignment();
		ia.setVariable(var);
		
		// Create math of initial assignment with a selector function
		ia.setMath(new SelectorNode(array, ia).node);

		ArraysSBasePlugin iaPlugin = (ArraysSBasePlugin) ia.getPlugin(ArraysConstants.shortLabel);
		
		// Add dimension to initial assignment
		iaPlugin.addDimension(dim.clone());
		
		// Add index to initial assignment
		Index index = iaPlugin.createIndex();
		index.setReferencedAttribute("symbol");
		index.setArrayDimension(0);
		index.setMath(new ASTNode("d0"));
	}
	
	public MetadataDocument(final SBMLDocument doc) {
		this.doc = doc;
	}
	
	public FskMetaData getMetaData() {
		FskMetaData template = new FskMetaData();

		Model model = doc.getModel();
		AssignmentRule rule = (AssignmentRule) model.getRule(0);

		// caches limits
		List<Limits> limits = model.getListOfConstraints().stream().map(LimitsConstraint::new)
				.map(LimitsConstraint::getLimits).collect(Collectors.toList());

		template.modelId = model.getId();
		template.modelName = model.getName();

		// organism data
		if (model.getNumSpecies() > 0) {
			PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
			template.organism = species.getName();
			if (species.isSetDetail()) {
				template.organismDetails = species.getDetail();
			}
		}

		// matrix data
		if (model.getNumCompartments() > 0) {
			PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
			template.matrix = compartment.getName();
			if (compartment.isSetDetail()) {
				template.matrixDetails = compartment.getDetail();
			}
		}

		// creator
		MetadataAnnotation annot = new MetadataAnnotation(doc.getAnnotation());
		template.creator = annot.givenName;
		template.familyName = annot.familyName;
		template.contact = annot.contact;
		template.createdDate = annot.createdDate;
		template.modifiedDate = annot.modifiedDate;
		template.type = annot.type;
		template.rights = annot.rights;
		template.referenceDescription = annot.referenceDescription;
		template.referenceDescriptionLink = annot.referenceDescriptionLink;

		template.subject = new ModelRule(rule).getModelClass();

		// model notes
		if (model.isSetNotes()) {
			try {
				template.notes = Jsoup.parse(model.getNotesString()).text();
			} catch (XMLStreamException e) {
				System.err.println("Error accesing the notes of " + model);
				e.printStackTrace();
			}
		}

		// dependent variable data
		{
			String depId = rule.getVariable();

			// Gets parameter for the dependent variable and sets it
			Parameter param = model.getParameter(depId);
			template.dependentVariable.name = param.getName();
			template.dependentVariable.type = DataType.numeric;

			// Gets and sets dependent variable unit
			String unitId = param.getUnits();
			if (!unitId.equals("dimensionless")) {
				UnitDefinition unitDef = model.getUnitDefinition(unitId);
				if (unitDef != null) {
					template.dependentVariable.unit = unitDef.getName();
				}
			}

			// Sets dependent variable min & max
			for (Limits lim : limits) {
				if (lim.getVar().replaceAll("\\_", "\\.").equals(depId)) {
					if (lim.getMin() != null)
						template.dependentVariable.min = lim.getMin().toString();
					if (lim.getMax() != null)
						template.dependentVariable.max = lim.getMax().toString();
					break;
				}
			}
		}

		// independent variable data
		{
			String depId = rule.getVariable();

			List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
				@Override
				public boolean accepts(Object o) {
					return !((Parameter) o).getId().equals(depId);
				}
			});

			final int numParams = indepParams.size();

			for (int p = 0; p < numParams; p++) {
				Parameter param = indepParams.get(p);
				Variable variable = new Variable();

				variable.name = param.getName().trim();

				// unit
				String unitId = param.getUnits();
				variable.unit = "";
				if (!unitId.equals("dimensionless")) {
					UnitDefinition unitDef = model.getUnitDefinition(unitId);
					if (unitDef != null) {
						variable.unit = unitDef.getName();
					}
				}

				Limits paramLimits = limits.stream().filter(lim -> lim.getVar().equals(param.getId())).findFirst()
						.get();
				variable.min = paramLimits.getMin().toString();
				variable.max = paramLimits.getMax().toString();

				if (param.getNumPlugins() > 0) {
					variable.type = DataType.array;

					InitialAssignment ia = model.getInitialAssignment(variable.name);
					List<Double> array = new SelectorNode(ia.getMath()).getArray();

					variable.value = "c(" + array.stream().map(d -> Double.toString(d)).collect(Collectors.joining(","))
							+ ")";

				} else {
					variable.type = param.getValue() % 1 == 0 ? DataType.integer : DataType.numeric;
					variable.value = Double.toString(param.getValue());
				}

				template.independentVariables.add(variable);
			}
		}

		template.hasData = false;
		
		return template;
	}
}
