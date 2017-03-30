/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes.creator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knime.base.node.util.exttool.ExtToolOutputNodeModel;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;

import de.bund.bfr.fskml.MissingValueError;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData;
import de.bund.bfr.knime.fsklab.nodes.FskMetaDataFields;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData.DataType;
import de.bund.bfr.knime.fsklab.nodes.Util;
import de.bund.bfr.knime.fsklab.nodes.Variable;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

class FskCreatorNodeModel extends ExtToolOutputNodeModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(FskCreatorNodeModel.class);

	private FskCreatorNodeSettings settings = new FskCreatorNodeSettings();
	
	// Input and output port types
	private static final PortType[] IN_TYPES = {};
	private static final PortType[] OUT_TYPES = { FskPortObject.TYPE };

	/** {@inheritDoc} */
	public FskCreatorNodeModel() {
		super(IN_TYPES, OUT_TYPES);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing
	}

	/** {@inheritDoc} */
	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		this.settings.saveSettings(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		this.settings.validateSettings(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		this.settings.loadValidatedSettingsFrom(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
		// does nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws MissingValueError
	 * @throws Exception
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
			throws InvalidSettingsException, IOException {
		try {
			FskPortObject portObj = new FskPortObject();

			// Reads model script
			if (StringUtils.isEmpty(settings.modelScript.getStringValue())) {
				throw new InvalidSettingsException("Model script is not provided");
			}
			RScript modelScript = readScript(settings.modelScript.getStringValue());
			portObj.model = modelScript.getScript();

			// Reads parameters script
			if (StringUtils.isEmpty(settings.paramScript.getStringValue())) {
				portObj.param = "";
			} else {
				portObj.param = readScript(settings.paramScript.getStringValue()).getScript();
			}

			// Reads visualization script
			if (StringUtils.isEmpty(settings.vizScript.getStringValue())) {
				portObj.viz = readScript(settings.vizScript.getStringValue()).getScript();
			} else {
				portObj.viz = "";
			}

			// Reads model meta data
			if (StringUtils.isNotEmpty(settings.metaDataDoc.getStringValue())) {
				File metaDataFile = FileUtil.getFileFromURL(FileUtil.toURL(settings.metaDataDoc.getStringValue()));
				try (XSSFWorkbook workbook = new XSSFWorkbook(metaDataFile)) {
					portObj.template = SpreadsheetHandler.processSpreadsheet(workbook.getSheetAt(0));
				} catch (Exception e) {
					// TODO: handle exception
				}
				portObj.template.software = FskMetaData.Software.R;

				// Try to figure out the type of the dependent variables
				for (int i = 0; i < portObj.template.dependentVariables.size(); i++) {
					DataType dt = Util.getValueType(portObj.template.dependentVariables.get(i).value);
					portObj.template.dependentVariables.get(i).type = dt;
				}
				
				// Set variable values and types from parameters script
				{
					Map<String, String> vars = getVariablesFromAssignments(portObj.param);
					for (Variable v : portObj.template.independentVariables) {
						if (vars.containsKey(v.name.trim())) {
							v.value = vars.get(v.name.trim());
							v.type = Util.getValueType(v.value);
						}
					}
				}
			}

			if (!modelScript.getLibraries().isEmpty()) {
				try {
					// Install missing libraries
					LibRegistry libReg = LibRegistry.instance();
					List<String> missingLibs = modelScript.getLibraries().stream()
							.filter(lib -> !libReg.isInstalled(lib)).collect(Collectors.toList());
					if (!missingLibs.isEmpty()) {
						libReg.installLibs(missingLibs);
					}

					Set<Path> libPaths = libReg.getPaths(modelScript.getLibraries());
					libPaths.forEach(l -> portObj.libs.add(l.toFile()));
				} catch (RException | REXPMismatchException e) {
					LOGGER.error(e.getMessage());
				}
			}

			return new PortObject[] { portObj };
		} catch (Exception e) {
			StringWriter thstack = new StringWriter();

			e.printStackTrace(new PrintWriter(thstack));

			NodeLogger.getLogger("Miguel's Logger").error(thstack.toString());
			throw e;
		}
	}

	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { FskPortObjectSpec.INSTANCE };
	}

	/**
	 * Reads R script.
	 * 
	 * @param path
	 *            File path to R model script. If is assured not to be null or
	 *            empty.
	 * @throws InvalidSettingsException
	 *             if {@link path} is null or whitespace.
	 * @throws IOException
	 *             if the file cannot be read.
	 */
	private static RScript readScript(final String path) throws InvalidSettingsException, IOException {
		String trimmedPath = StringUtils.trimToNull(path.trim());

		// path is not null or whitespace, thus try to read it
		try {
			// may throw IOException
			File fScript = FileUtil.getFileFromURL(FileUtil.toURL(trimmedPath));
			RScript script = new RScript(fScript);
			return script;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new IOException(trimmedPath + ": cannot be read");
		}
	}

	private static class SpreadsheetHandler {

		static FskMetaData processSpreadsheet(final XSSFSheet sheet) {

			FskMetaData template = new FskMetaData();

			template.modelId = getStringVal(sheet, FskMetaDataFields.id.row);
			template.modelName = getStringVal(sheet, FskMetaDataFields.name.row);

			// organism data
			template.organism = getStringVal(sheet, FskMetaDataFields.species.row);
			template.organismDetails = getStringVal(sheet, FskMetaDataFields.species_details.row);

			// matrix data
			template.matrix = getStringVal(sheet, FskMetaDataFields.matrix.row);
			template.matrixDetails = getStringVal(sheet, FskMetaDataFields.matrix_details.row);

			template.creator = getStringVal(sheet, FskMetaDataFields.creator.row);

			// no family name in the spreadsheet
			// no contact in the spreadsheet

			template.referenceDescription = getStringVal(sheet, FskMetaDataFields.reference_description.row);

			template.createdDate = sheet.getRow(FskMetaDataFields.created_date.row).getCell(5).getDateCellValue();
			template.modifiedDate = sheet.getRow(FskMetaDataFields.modified_date.row).getCell(5).getDateCellValue();

			template.rights = getStringVal(sheet, FskMetaDataFields.rights.row);

			// model type
			{
				try {
					String modelType = getStringVal(sheet, FskMetaDataFields.type.row);
					template.type = ModelType.valueOf(modelType);
				}
				// if modelTypeAsString is not a valid ModelType
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}

			// model subject
			{
				String subject = getStringVal(sheet, FskMetaDataFields.subject.row);
				try {
					template.subject = ModelClass.valueOf(subject);
				} catch (IllegalArgumentException e) {
					template.subject = ModelClass.UNKNOWN;
					e.printStackTrace();
				}
			}

			// model notes
			template.notes = getStringVal(sheet, FskMetaDataFields.notes.row);

			// dep var. Type is not in the spreadsheet.
			{
				String[] names = getStringVal(sheet, FskMetaDataFields.depvars.row).split("\\|\\|");
				String[] units = getStringVal(sheet, FskMetaDataFields.depvars_units.row).split("\\|\\|");
				String[] mins = getStringVal(sheet, FskMetaDataFields.depvars_mins.row).split("\\|\\|");
				String[] maxs = getStringVal(sheet, FskMetaDataFields.depvars_maxs.row).split("\\|\\|");
				
				for (int i = 0; i < names.length; i++) {
					Variable v = new Variable();
					v.name = names[i];
					v.unit = units[i];
					v.min = mins[i];
					v.max = maxs[i];
					// no values or types in the spreadsheet
					v.value = "";
					template.dependentVariables.add(v);
				}
			}

			// indep vars
			{
				String[] names = getStringVal(sheet, FskMetaDataFields.indepvars.row).split("\\|\\|");
				String[] units = getStringVal(sheet, FskMetaDataFields.indepvars_units.row).split("\\|\\|");
				String[] mins = getStringVal(sheet, FskMetaDataFields.indepvars_mins.row).split("\\|\\|");
				String[] maxs = getStringVal(sheet, FskMetaDataFields.indepvars_maxs.row).split("\\|\\|");

				for (int i = 0; i < names.length; i++) {
					Variable v = new Variable();
					v.name = names[i].trim();
					v.unit = units[i].trim();
					v.min = mins[i].trim();
					v.max = maxs[i].trim();
					// no values or types in the spreadsheet
					v.value = "";
					template.independentVariables.add(v);
				}
			}

			template.hasData = false;

			return template;
		}

		/**
		 * Gets the string value for the fifth column which holds the value for
		 * that row.
		 */
		private static String getStringVal(final XSSFSheet sheet, final byte rownum) {
			XSSFCell cell = sheet.getRow(rownum).getCell(5);

			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				return Double.toString(cell.getNumericCellValue());
			return cell.getStringCellValue();
		}
	}

	private static class Assignment {

		enum Type {
			/** R command with the = assignment operator. E.g. x = value */
			equals,
			/** R command with the <- assignment operator. E.g. x <- value */
			left,
			/**
			 * R command with the <<- scoping assignment operator. E.g. x <<-
			 * value
			 */
			super_left,
			/** R command with the -> assignment operator. E.g. value -> x */
			right,
			/** R command with the ->> assignment operator. E.g. value ->> x */
			super_right
		}

		String variable;
		String value;

		public Assignment(String line, Assignment.Type type) {
			if (type == Type.equals) {
				String[] tokens = line.split("||");
				variable = tokens[0].trim();
				value = tokens[1].trim();
			} else if (type == Type.left) {
				String[] tokens = line.split("<-");
				variable = tokens[0].trim();
				value = tokens[1].trim();
			} else if (type == Type.super_left) {
				String[] tokens = line.split("<<-");
				variable = tokens[0].trim();
				value = tokens[1].trim();
			} else if (type == Type.right) {
				String[] tokens = line.split("->");
				variable = tokens[1].trim();
				value = tokens[0].trim();
			} else if (type == Type.super_right) {
				String[] tokens = line.split("->>");
				variable = tokens[1].trim();
				value = tokens[0].trim();
			}
		}
	}

	private Map<String, String> getVariablesFromAssignments(String paramScript) {
		Map<String, String> vars = new HashMap<>();
		for (String line : paramScript.split("\\r?\\n")) {
			line = line.trim();
			if (line.startsWith("#"))
				continue;

			if (line.indexOf("=") != -1) {
				Assignment a = new Assignment(line, Assignment.Type.equals);
				vars.put(a.variable, a.value);
			} else if (line.indexOf("<-") != -1) {
				Assignment a = new Assignment(line, Assignment.Type.left);
				vars.put(a.variable, a.value);
			} else if (line.indexOf("<<-") != -1) {
				Assignment a = new Assignment(line, Assignment.Type.super_left);
				vars.put(a.variable, a.value);
			} else if (line.indexOf("->>") != -1) {
				Assignment a = new Assignment(line, Assignment.Type.right);
				vars.put(a.variable, a.value);
			} else if (line.indexOf("->") != -1) {
				Assignment a = new Assignment(line, Assignment.Type.super_right);
				vars.put(a.variable, a.value);
			}
		}

		return vars;
	}
}
