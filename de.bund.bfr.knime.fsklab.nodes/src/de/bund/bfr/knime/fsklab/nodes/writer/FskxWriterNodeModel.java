/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.fsklab.nodes.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;
import org.sbml.jsbml.xml.stax.SBMLWriter;

import de.bund.bfr.knime.fsklab.nodes.FskMetaData;
import de.bund.bfr.knime.fsklab.nodes.RMetaDataNode;
import de.bund.bfr.knime.fsklab.nodes.URIS;
import de.bund.bfr.knime.fsklab.nodes.Variable;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.PMFUtil;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.MetadataAnnotation;
import de.bund.bfr.pmfml.sbml.MetadataImpl;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.ReferenceSBMLNode;
import de.bund.bfr.pmfml.sbml.SBMLFactory;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 */
public class FskxWriterNodeModel extends NodeModel {

	// Configuration keys
	public static final String CFG_FILE = "file";

	private final SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);

	private static final PortType[] inPortTypes = { FskPortObject.TYPE };
	private static final PortType[] outPortTypes = {};

	public FskxWriterNodeModel() {
		super(inPortTypes, outPortTypes);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws Exception
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec) throws Exception {

		FskPortObject portObject = (FskPortObject) inData[0];

		try {
			Files.deleteIfExists(Paths.get(filePath.getStringValue()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Previous file with same name could not be overwritten");
		}

		// try to create CombineArchive
		try (CombineArchive archive = new CombineArchive(new File(filePath.getStringValue()))) {

			RMetaDataNode metaDataNode = new RMetaDataNode();

			// Adds model script
			if (portObject.model != null) {
				archive.addEntry(createScriptFile(portObject.model), "model.r", URIS.r);
				metaDataNode.setMainScript("model.r");
			}

			// Adds parameters script
			if (portObject.param != null) {
				archive.addEntry(createScriptFile(portObject.param), "param.r", URIS.r);
				metaDataNode.setParamScript("param.r");
			}

			// Adds visualization script
			if (portObject.viz != null) {
				archive.addEntry(createScriptFile(portObject.viz), "visualization.r", URIS.r);
				metaDataNode.setVisualizationScript("visualization.r");
			}

			// Adds R workspace file
			if (portObject.workspace != null) {
				archive.addEntry(portObject.workspace, "workspace.r", URIS.r);
				metaDataNode.setWorkspaceFile("workspace.r");
			}

			// Adds model meta data
			if (portObject.template != null) {
				SBMLDocument doc = createSbmlDocument(portObject.template);

				File f = FileUtil.createTempFile("metaData", ".pmf");
				try {
					new SBMLWriter().write(doc, f);
					archive.addEntry(f, "metaData.pmf", URIS.pmf);
				} catch (SBMLException | XMLStreamException e) {
					e.printStackTrace();
				}
			}

			// Adds R libraries
			for (File lib : portObject.libs) {
				archive.addEntry(lib, lib.getName(), URIS.zip);
			}

			archive.addDescription(new DefaultMetaDataObject(metaDataNode.getNode()));
			archive.pack();
		} catch (Exception e) {
			try {
				Files.delete(Paths.get(filePath.getStringValue()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new Exception("File could not created");
		}

		return new PortObject[] {};
	}

	private static File createScriptFile(String script) throws IOException {
		File f = FileUtil.createTempFile("script", ".r");
		try (FileWriter fw = new FileWriter(f)) {
			fw.write(script);
		}

		return f;
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
		// does nothing
	}

	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] {};
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		filePath.saveSettingsTo(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filePath.loadSettingsFrom(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		filePath.validateSettings(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing
	}

	/** {@inheritDoc} */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing
	}

	/** Creates SBMLDocument out of a OpenFSMR template. */
	private static SBMLDocument createSbmlDocument(final FskMetaData template) {

		// Creates SBMLDocument for the primary model
		final SBMLDocument sbmlDocument = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);

		// Adds namespaces to the sbmlDocument
		TableReader.addNamespaces(sbmlDocument);

		// Adds document annotation
		Metadata metaData = new MetadataImpl();
		if (template.creator != null && !template.creator.isEmpty()) {
			metaData.setGivenName(template.creator);
		}
		if (template.familyName != null && !template.familyName.isEmpty()) {
			metaData.setFamilyName(template.familyName);
		}
		if (template.contact != null && !template.contact.isEmpty()) {
			metaData.setContact(template.contact);
		}
		if (template.createdDate != null) {
			metaData.setCreatedDate(FskMetaData.dateFormat.format(template.createdDate));
		}
		if (template.modifiedDate != null) {
			metaData.setModifiedDate(FskMetaData.dateFormat.format(template.modifiedDate));
		}
		if (template.type != null) {
			metaData.setType(template.type);
		}
		if (template.rights != null && !template.rights.isEmpty()) {
			metaData.setRights(template.rights);
		}
		if (template.referenceDescriptionLink != null) {
			metaData.setReferenceLink(template.referenceDescriptionLink.toString());
		}

		sbmlDocument.setAnnotation(new MetadataAnnotation(metaData).getAnnotation());

		// Creates model and names it
		Model model = sbmlDocument.createModel(PMFUtil.createId(template.modelId));
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
			LimitsConstraint lc = new LimitsConstraint(template.dependentVariable.name.replaceAll("\\.", "\\_"), min, max);
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
		int modelId = - new Random().nextInt(Integer.MAX_VALUE);
		Reference[] references = new Reference[0];

		AssignmentRule rule = new AssignmentRule(3, 1);
		rule.setVariable(depParam.getId());
		rule.setAnnotation(new ModelRuleAnnotation(formulaName, modelClass, modelId, references).annotation);
		model.addRule(rule);

		return sbmlDocument;
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
	
	private static class TableReader {
		public final static int LEVEL = 3;
		public final static int VERSION = 1;

		public static void addNamespaces(SBMLDocument doc) {
			doc.addDeclaredNamespace("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			doc.addDeclaredNamespace("xmlns:pmml", "http://www.dmg.org/PMML-4_2");
			doc.addDeclaredNamespace("xmlns:pmf", "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
			doc.addDeclaredNamespace("xmlns:dc", "http://purl.org/dc/elements/1.1");
			doc.addDeclaredNamespace("xmlns:dcterms", "http://purl.org/dc/terms/");
			doc.addDeclaredNamespace("xmlns:pmmlab",
					"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
			doc.addDeclaredNamespace("xmlns:numl", "http://www.numl.org/numl/level1/version1");
			doc.addDeclaredNamespace("xmlns:xlink", "http//www.w3.org/1999/xlink");
		}
	}
}
