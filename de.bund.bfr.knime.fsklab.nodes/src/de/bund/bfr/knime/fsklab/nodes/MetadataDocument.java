package de.bund.bfr.knime.fsklab.nodes;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
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

import com.google.common.base.Strings;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.PMFUtil;
import de.bund.bfr.pmfml.sbml.Limits;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.SBMLFactory;

public class MetadataDocument {

	public SBMLDocument doc;

	public MetadataDocument(FskMetaData template) {
		// Creates SBMLDocument for the primary model
		doc = new SBMLDocument(3, 1);

		// Adds namespaces to the sbmlDocument
		doc.addDeclaredNamespace("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		doc.addDeclaredNamespace("xmlns:pmml", "http://www.dmg.org/PMML-4_2");
		doc.addDeclaredNamespace("xmlns:pmf", "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
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
		AssignmentRule rule = new AssignmentRule(3, 1);
		rule.setVariable(depParam.getId());
		ModelClass modelClass = template.subject == null ? ModelClass.UNKNOWN : template.subject;
		rule.setAnnotation(new RuleAnnotation(modelClass).annotation);
		model.addRule(rule);
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

		template.subject = new RuleAnnotation(rule.getAnnotation()).modelClass;

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

	public class MetadataAnnotation {

		private static final String METADATA_TAG = "metadata"; // Metadata tag
		private static final String METADATA_NS = "pmf"; // Metadata namespace

		// Namespace and tag for the creator: <dc:creator>
		private final static String CREATOR_NS = "dc";
		private final static String CREATOR_TAG = "creator";

		// Namespace and tag for the created date: <dcterms:created>
		private final static String CREATED_NS = "dcterms";
		private final static String CREATED_TAG = "created";

		// Namespace and tag for the modified date: <dcterms:modified>
		private final static String MODIFIED_NS = "dcterms";
		private final static String MODIFIED_TAG = "modified";

		// Namespace and tag for the type: <dc:type>
		private final static String TYPE_NS = "dc";
		private final static String TYPE_TAG = "type";

		// Namespace and tag for the rights: <dc:rights>
		private final static String RIGHTS_NS = "dc";
		private final static String RIGHTS_TAG = "rights";

		// Namespace and tag for the reference description: <dc:description>
		private final static String REFDESC_NS = "dc";
		private final static String REFDESC_TAG = "description";

		// Namespace and tag for the reference description link: <dc:source>
		private final static String REFDESCLINK_NS = "dc";
		private final static String REFDESCLINK_TAG = "source";

		public Annotation annotation;

		// Fields with metadata
		public String givenName;
		public String familyName;
		public String contact;
		public Date createdDate;
		public Date modifiedDate;
		public ModelType type;
		public String rights;
		public String referenceDescription;
		public String referenceDescriptionLink;

		public MetadataAnnotation(final FskMetaData metadata) {

			XMLTriple pmfTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
			XMLNode pmfNode = new XMLNode(pmfTriple);

			// Builds creator node
			if (!Strings.isNullOrEmpty(metadata.creator) || !Strings.isNullOrEmpty(metadata.familyName)
					|| !Strings.isNullOrEmpty(metadata.contact)) {
				givenName = Strings.nullToEmpty(metadata.creator);
				familyName = Strings.nullToEmpty(metadata.familyName);
				contact = Strings.nullToEmpty(metadata.contact);

				String creator = givenName + "." + familyName + "." + contact;
				XMLNode creatorNode = new XMLNode(new XMLTriple(CREATOR_TAG, null, CREATOR_NS));
				creatorNode.addChild(new XMLNode(creator));
				pmfNode.addChild(creatorNode);
			}

			// Builds created date node
			if (metadata.createdDate != null) {
				XMLNode createdNode = new XMLNode(new XMLTriple(CREATED_TAG, "", CREATED_NS));
				createdNode.addChild(new XMLNode(FskMetaData.dateFormat.format(metadata.createdDate)));
				pmfNode.addChild(createdNode);
			}

			// Builds modified date node
			if (metadata.modifiedDate != null) {
				XMLNode modifiedNode = new XMLNode(new XMLTriple(MODIFIED_TAG, "", MODIFIED_NS));
				modifiedNode.addChild(new XMLNode(FskMetaData.dateFormat.format(metadata.modifiedDate)));
				pmfNode.addChild(modifiedNode);
			}

			// Builds type node
			if (metadata.type != null) {
				XMLNode typeNode = new XMLNode(new XMLTriple(TYPE_TAG, "", TYPE_NS));
				typeNode.addChild(new XMLNode(metadata.type.name()));
				pmfNode.addChild(typeNode);
			}

			// Builds rights node
			if (!Strings.isNullOrEmpty(metadata.rights)) {
				XMLNode rightsNode = new XMLNode(new XMLTriple(RIGHTS_TAG, "", RIGHTS_NS));
				rightsNode.addChild(new XMLNode(metadata.rights));
				pmfNode.addChild(rightsNode);
			}

			// Builds reference description node
			if (!Strings.isNullOrEmpty(metadata.referenceDescription)) {
				XMLNode refdescNode = new XMLNode(new XMLTriple(REFDESC_TAG, "", REFDESC_NS));
				refdescNode.addChild(new XMLNode(metadata.referenceDescription));
				pmfNode.addChild(refdescNode);
			}

			// Builds reference description link node
			if (!Strings.isNullOrEmpty(metadata.referenceDescriptionLink)) {
				XMLNode refdescLinkNode = new XMLNode(new XMLTriple(REFDESCLINK_TAG, "", REFDESCLINK_NS));
				refdescLinkNode.addChild(new XMLNode(metadata.referenceDescriptionLink));
				pmfNode.addChild(refdescLinkNode);
			}

			// Create annotation
			annotation = new Annotation();
			annotation.setNonRDFAnnotation(pmfNode);
		}

		public MetadataAnnotation(final Annotation annotation) {
			XMLNode pmfNode = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");

			// Reads creatorNode
			XMLNode creatorNode = pmfNode.getChildElement(CREATOR_TAG, "");
			if (creatorNode != null) {
				String[] tempStrings = creatorNode.getChild(0).getCharacters().split("\\.", 3);
				givenName = Strings.nullToEmpty(tempStrings[0]);
				familyName = Strings.nullToEmpty(tempStrings[1]);
				contact = Strings.nullToEmpty(tempStrings[2]);
			}

			// Reads created date
			XMLNode createdNode = pmfNode.getChildElement(CREATED_TAG, "");
			if (createdNode != null) {
				try {
					createdDate = FskMetaData.dateFormat.parse(createdNode.getChild(0).getCharacters());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// Reads modified date
			XMLNode modifiedNode = pmfNode.getChildElement(MODIFIED_TAG, "");
			if (modifiedNode != null) {
				try {
					modifiedDate = FskMetaData.dateFormat.parse(modifiedNode.getChild(0).getCharacters());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// Reads model type
			XMLNode typeNode = pmfNode.getChildElement(TYPE_TAG, "");
			if (typeNode != null) {
				type = ModelType.valueOf(typeNode.getChild(0).getCharacters());
			}

			// Reads rights
			XMLNode rightsNode = pmfNode.getChildElement(RIGHTS_TAG, "");
			if (rightsNode != null) {
				rights = rightsNode.getChild(0).getCharacters();
			}

			// Reads reference description
			XMLNode refdescNode = pmfNode.getChildElement(REFDESC_TAG, "");
			if (refdescNode != null) {
				referenceDescription = refdescNode.getChild(0).getCharacters();
			}

			// Reads reference description link
			XMLNode refdescLinkNode = pmfNode.getChildElement(REFDESCLINK_TAG, "");
			if (refdescLinkNode != null) {
				referenceDescriptionLink = refdescLinkNode.getChild(0).getCharacters();
			}

			// Copies annotation
			this.annotation = annotation;
		}
	}

	/**
	 * Reduced version of PMF-ML ModelRuleAnnotation with only the model class.
	 */
	public class RuleAnnotation {

		public static final String SUBJECT_NAME = "subject";
		public static final String SUBJECT_URI = "pmmlab";

		public ModelClass modelClass;
		public Annotation annotation;

		public RuleAnnotation(final ModelClass modelClass) {
			// Builds metadata node
			XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
			XMLNode pmfNode = new XMLNode(pmfTriple);

			// Builds model class node
			XMLTriple modelClassTriple = new XMLTriple(SUBJECT_NAME, null, SUBJECT_URI);
			XMLNode modelClassNode = new XMLNode(modelClassTriple);
			modelClassNode.addChild(new XMLNode(modelClass.fullName()));
			pmfNode.addChild(modelClassNode);

			// Create annotation
			annotation = new Annotation();
			annotation.setNonRDFAnnotation(pmfNode);
		}

		public RuleAnnotation(final Annotation annotation) {

			XMLNode pmfNode = annotation.getNonRDFannotation().getChildElement("metadata", "");

			// Reads model class node
			XMLNode modelClassNode = pmfNode.getChildElement(SUBJECT_NAME, "");
			if (modelClassNode != null) {
				modelClass = ModelClass.fromName(modelClassNode.getChild(0).getCharacters());
			}

			// Copies annotation
			this.annotation = annotation;
		}
	}
}
