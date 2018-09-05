/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.Creator;
import org.sbml.jsbml.History;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.text.parser.FormulaParser;
import org.sbml.jsbml.text.parser.ParseException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

/**
 * This is the model implementation of SBMLWriter.
 *
 * @author Christian Thoens
 * @author Miguel de Alba
 */
public class SBMLWriterNodeModel extends NodeModel {

	private final SBMLWriterNodeSettings nodeSettings = new SBMLWriterNodeSettings();

	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected SBMLWriterNodeModel() {
		super(1, 0);
		schema = null;
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		TableReader reader = new TableReader(PmmUtilities.getTuples(inData[0], schema), nodeSettings.variableParams,
				nodeSettings.creatorGivenName, nodeSettings.creatorFamilyName, nodeSettings.creatorContact,
				new Date(nodeSettings.createdDate), new Date(nodeSettings.modifiedDate), nodeSettings.reference);
		Map<String, File> files = new LinkedHashMap<>();

		for (String name : reader.getDocuments().keySet()) {
			File file = new File(nodeSettings.outPath + "/" + name + ".sbml.xml");

			if (!nodeSettings.overwrite && file.exists()) {
				throw new IOException(file.getAbsolutePath() + " already exists");
			}

			files.put(name, file);
		}

		for (String name : reader.getDocuments().keySet()) {
			SBMLWriter.write(reader.getDocuments().get(name), files.get(name), name, "1.0");
		}

		return new BufferedDataTable[] {};
	}

	@Override
	protected void reset() {
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		if (SchemaFactory.createM12DataSchema().conforms(inSpecs[0])) {
			schema = SchemaFactory.createM12DataSchema();
		} else if (SchemaFactory.createM1DataSchema().conforms(inSpecs[0])) {
			schema = SchemaFactory.createM1DataSchema();
		} else if (nodeSettings.outPath == null || nodeSettings.variableParams == null) {
			throw new InvalidSettingsException("Node must be configured");
		} else {
			throw new InvalidSettingsException("Invalid Input");
		}

		return new DataTableSpec[] {};
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		nodeSettings.save(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		nodeSettings.load(settings);
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		// Does nothing
	}

	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	private static class TableReader {

		private final static String COMPARTMENT_MISSING = "CompartmentMissing";
		private final static String SPECIES_MISSING = "SpeciesMissing";

		private Map<String, SBMLDocument> documents;

		public TableReader(List<KnimeTuple> tuples, String varParams, String creatorGivenName, String creatorFamilyName,
				String creatorContact, Date createdDate, Date modifiedDate, String reference)
				throws IOException, XMLStreamException {
			boolean isTertiaryModel = tuples.get(0).getSchema().conforms(SchemaFactory.createM12Schema());
			Set<Integer> idSet = new LinkedHashSet<>();
			Map<KnimeTuple, List<KnimeTuple>> tupleMap;

			if (isTertiaryModel) {
				tupleMap = new ModelCombiner(tuples, true, null, null).getTupleCombinations();
			} else {
				tupleMap = new LinkedHashMap<>();

				for (KnimeTuple tuple : tuples) {
					tupleMap.put(tuple, Arrays.asList(tuple));
				}
			}

			documents = new LinkedHashMap<>();

			for (KnimeTuple tuple : tupleMap.keySet()) {
				replaceCelsiusAndFahrenheit(tuple);
				renameLog(tuple);

				CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
				EstModelXml estXml = (EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL).get(0);
				DepXml depXml = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
				AgentXml organismXml = (AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0);
				MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0);
				Integer id = estXml.getId();

				if (!idSet.add(id)) {
					continue;
				}

				History history = new History();

				if (createdDate != null) {
					history.setCreatedDate(createdDate);
				}

				if (modifiedDate != null) {
					history.setModifiedDate(modifiedDate);
				}

				history.addCreator(new Creator(creatorGivenName, creatorFamilyName, null, creatorContact));

				String modelName = ((EstModelXml) tupleMap.get(tuple).get(0).getPmmXml(Model1Schema.ATT_ESTMODEL)
						.get(0)).getName();

				if (modelName == null || modelName.trim().isEmpty()) {
					throw new IOException("Model Name missing");
				}

				String modelID = createId(modelName);
				SBMLDocument doc = new SBMLDocument(2, 4);
				Model model = doc.createModel(modelID);

				model.setMetaId("Meta_" + modelID);
				model.setName(modelID);
				model.setHistory(history);
				// model.setNotes(XMLNode.convertStringToXMLNode("<notes>" +
				// reference
				// + "</notes>"));

				Compartment c;
				Species s;

				if (matrixXml.getName() != null) {
					c = model.createCompartment(createId(matrixXml.getName()));
					c.setName(matrixXml.getName());
				} else {
					c = model.createCompartment(COMPARTMENT_MISSING);
					c.setName(COMPARTMENT_MISSING);
				}

				if (organismXml.getName() != null) {
					s = model.createSpecies(createId(organismXml.getName()), c);
					s.setName(organismXml.getName());
				} else {
					s = model.createSpecies(SPECIES_MISSING, c);
					s.setName(SPECIES_MISSING);
				}

				ListOf<Rule> rules = new ListOf<>(2, 4);
				ListOf<Constraint> constraints = new ListOf<>(2, 4);
				Parameter depParam = model.createParameter(depXml.getName());
				String depSbmlUnit = Categories.getCategoryByUnit(depXml.getUnit()).getSBML(depXml.getUnit());

				depParam.setValue(0.0);
				depParam.setConstant(false);

				if (depSbmlUnit != null) {
					UnitDefinition unit = SBMLUtilities.fromXml(depSbmlUnit);
					Unit.Kind kind = SBMLUtilities.simplify(unit);
					UnitDefinition modelUnit = model.getUnitDefinition(unit.getId());

					if (kind != null) {
						depParam.setUnits(kind);
					} else if (modelUnit != null) {
						depParam.setUnits(modelUnit);
					} else {
						depParam.setUnits(SBMLUtilities.addUnitToModel(model, unit));
					}
				}

				String formula = modelXml.getFormula().substring(modelXml.getFormula().indexOf("=") + 1);

				try {
					rules.add(new AssignmentRule(parse(formula), depParam));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				for (PmmXmlElementConvertable el : tuple.getPmmXml(Model1Schema.ATT_PARAMETER).getElementSet()) {
					ParamXml paramXml = (ParamXml) el;
					Parameter param = model.createParameter(paramXml.getName());

					if (paramXml.getName().equals(varParams)) {
						param.setConstant(false);
					} else {
						param.setConstant(true);
					}

					if (paramXml.getValue() != null) {
						param.setValue(paramXml.getValue());
					} else {
						param.setValue(0.0);
					}
				}

				for (PmmXmlElementConvertable el : tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).getElementSet()) {
					IndepXml indepXml = (IndepXml) el;

					if (indepXml.getName().equals(AttributeUtilities.TIME)) {
						indepXml.setName("time");
					}

					String name = indepXml.getName();
					Parameter param = model.createParameter(name);
					String sbmlUnit = Categories.getCategoryByUnit(indepXml.getUnit()).getSBML(indepXml.getUnit());

					param.setValue(0.0);
					param.setConstant(false);

					Double min = indepXml.getMin();
					Double max = indepXml.getMax();

					if (MathUtilities.isValid(min)) {
						try {
							constraints.add(new Constraint(parse(name + ">=" + min), 2, 4));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					if (MathUtilities.isValid(max)) {
						try {
							constraints.add(new Constraint(parse(name + "<=" + max), 2, 4));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					if (sbmlUnit != null) {
						UnitDefinition unit = SBMLUtilities.fromXml(sbmlUnit);
						Unit.Kind kind = SBMLUtilities.simplify(unit);
						UnitDefinition modelUnit = model.getUnitDefinition(unit.getId());

						if (kind != null) {
							param.setUnits(kind);
						} else if (modelUnit != null) {
							param.setUnits(modelUnit);
						} else {
							param.setUnits(SBMLUtilities.addUnitToModel(model, unit));
						}
					}
				}

				model.setListOfRules(rules);
				model.setListOfConstraints(constraints);

				if (documents.containsKey(modelID)) {
					throw new IOException("Duplicate model name: " + modelID);
				}

				documents.put(modelID, doc);
			}
		}

		public Map<String, SBMLDocument> getDocuments() {
			return documents;
		}

		private static void renameLog(KnimeTuple tuple) {
			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			CatalogModelXml model = (CatalogModelXml) modelXml.get(0);

			model.setFormula(MathUtilities.replaceVariable(model.getFormula(), "log", "log10"));
			tuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
		}

		private static void replaceCelsiusAndFahrenheit(KnimeTuple tuple) {
			final String CELSIUS = "°C";
			final String FAHRENHEIT = "°F";
			final String KELVIN = "K";

			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			CatalogModelXml model = (CatalogModelXml) modelXml.get(0);
			Category temp = Categories.getTempCategory();

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml indep = (IndepXml) el;

				if (CELSIUS.equals(indep.getUnit())) {
					try {
						String replacement = "(" + temp.getConversionString(indep.getName(), KELVIN, CELSIUS) + ")";

						model.setFormula(
								MathUtilities.replaceVariable(model.getFormula(), indep.getName(), replacement));
						indep.setUnit(KELVIN);
						indep.setMin(temp.convert(indep.getMin(), CELSIUS, KELVIN));
						indep.setMax(temp.convert(indep.getMax(), CELSIUS, KELVIN));
					} catch (ConvertException e) {
						e.printStackTrace();
					}
				} else if (FAHRENHEIT.equals(indep.getUnit())) {
					try {
						String replacement = "(" + temp.getConversionString(indep.getName(), KELVIN, FAHRENHEIT) + ")";

						model.setFormula(
								MathUtilities.replaceVariable(model.getFormula(), indep.getName(), replacement));
						indep.setUnit(FAHRENHEIT);
						indep.setMin(temp.convert(indep.getMin(), FAHRENHEIT, KELVIN));
						indep.setMax(temp.convert(indep.getMax(), FAHRENHEIT, KELVIN));
					} catch (ConvertException e) {
						e.printStackTrace();
					}
				}
			}

			tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepXml);
			tuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
		}

		private static String createId(String s) {
			return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
		}

		private static ASTNode parse(String s) throws ParseException {
			return new FormulaParser(new StringReader(s)).parse();
		}
	}
}
