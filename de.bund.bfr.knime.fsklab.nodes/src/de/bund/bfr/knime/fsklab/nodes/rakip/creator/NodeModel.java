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
package de.bund.bfr.knime.fsklab.nodes.rakip.creator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;

import com.google.common.collect.Lists;

import de.bund.bfr.fskml.MissingValueError;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.nodes.FskMetaData;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.rakip.port.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.rakip.port.FskPortObjectSpec;
import de.bund.bfr.rakip.generic.GeneralInformation;
import de.bund.bfr.rakip.generic.GenericModel;
import de.bund.bfr.rakip.generic.Hazard;
import de.bund.bfr.rakip.generic.ModelMath;
import de.bund.bfr.rakip.generic.Parameter;
import de.bund.bfr.rakip.generic.ParameterClassification;
import de.bund.bfr.rakip.generic.Product;
import de.bund.bfr.rakip.generic.Scope;

class NodeModel extends NoInternalsModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(NodeModel.class);

	private NodeSettings settings = new NodeSettings();

	// Input and output port types
	private static final PortType[] IN_TYPES = {};
	private static final PortType[] OUT_TYPES = { FskPortObject.TYPE };

	/** {@inheritDoc} */
	public NodeModel() {
		super(IN_TYPES, OUT_TYPES);
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

				// process metadata
				try (XSSFWorkbook workbook = new XSSFWorkbook(metaDataFile)) {
					XSSFSheet sheet = workbook.getSheetAt(0);
					portObj.genericModel = SpreadsheetHandler2.processSpreadsheet(sheet);

					portObj.genericModel.getGeneralInformation().setSoftware("R");

					// Set variable values and types from parameters script
					Map<String, String> vars = getVariablesFromAssignments(portObj.param);
					List<Parameter> indepParams = portObj.genericModel.getModelMath().getParameter().stream()
							.filter(it -> it.getClassification() == ParameterClassification.input)
							.collect(Collectors.toList());
					for (Parameter param : indepParams) {
						if (vars.containsKey(param.getName().trim())) {
							String newVal = vars.get(param.getName().trim());
							String newType = getValueType(newVal);

							param.setValue(newVal);
							param.setDataType(newType);
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
			return new PortObject[] {};
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

	// Converts metadata in spreadsheet to GenericModel
	private static class SpreadsheetHandler2 {

		static GenericModel processSpreadsheet(final XSSFSheet sheet)
				throws IllegalArgumentException, MalformedURLException {

			GeneralInformation generalInformation;
			{
				String modelName = getString(sheet, 1);
				String modelId = getString(sheet, 2);
				String modelRights = getString(sheet, 11);

				// creation date
				XSSFRow creationDateRow = sheet.getRow(9);
				if (creationDateRow == null)
					throw new IllegalArgumentException("Row with creation date is missing");
				XSSFCell creationDateCell = creationDateRow.getCell(5);
				Date creationDate;
				try {
					creationDate = creationDateCell.getDateCellValue();
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(
							"Wrong data type for creation date: [" + creationDateCell.getRawValue() + "]");
				}

				List<Date> modificationDates = Lists.newArrayList();
				XSSFRow modifiedDateRow = sheet.getRow(10);
				if (modifiedDateRow == null)
					throw new IllegalArgumentException("Row with modified date is missing");

				XSSFCell modifiedDateCell = modifiedDateRow.getCell(5);
				try {
					Date modifiedDate = modifiedDateCell.getDateCellValue();
					modificationDates.add(modifiedDate);
				} catch (IllegalArgumentException e) {
					// Do not do anything since the modification date is
					// optional
				}

				URL modelUrl = new URL(getString(sheet, 16));

				// TODO: process model type
				// TODO: process model subject

				generalInformation = new GeneralInformation(modelName, null, modelId, Lists.newArrayList(),
						creationDate, modificationDates, modelRights, true, modelUrl, null, Lists.newArrayList(), null,
						null, null, null, null, null, null);
			}

			Scope scope;
			{
				String hazardName = getString(sheet, 3);
				String hazardDetails = getOptionalString(sheet, 4);
				Hazard hazard = new Hazard("", hazardName, hazardDetails, "", "", "", "", "", "", "", "", "", "", "",
						"", "", "", "", "", "");

				String environmentName = getString(sheet, 5);
				String environmentDetails = getOptionalString(sheet, 6);
				Product product = new Product(environmentName, environmentDetails, "", Lists.newArrayList(),
						Lists.newArrayList(), Lists.newArrayList(), "", "", "", null, null);

				scope = new Scope(product, hazard, null, "", "", Lists.newArrayList(), Lists.newArrayList());
			}

			String creator = getString(sheet, 7);

			// no family name in the spreadsheet
			// no contact in the spreadsheet

			String referenceDescription = getString(sheet, 8);

			// model notes
			String notes = getString(sheet, 12);

			ModelMath modelMath = new ModelMath();
			// TODO: dep var. Type is not in the spreadsheet
			{
				String[] names = getString(sheet, 21).split("\\|\\|");
				String[] units = getString(sheet, 22).split("\\|\\|");

				for (int i = 0; i < names.length; i++) {

					String id = "";
					ParameterClassification classification = ParameterClassification.output;
					String name = names[i].trim();
					String description = null;
					String unit = units[i].trim();
					String unitCategory = "";
					String dataType = "";
					String source = null;
					String subject = null;
					String distribution = null;
					String value = null;
					String reference = null;
					String variabilitySubject = null;
					List<String> modelApplicability = Lists.newArrayList();
					Double error = null;

					Parameter parameter = new Parameter(id, classification, name, description, unit, unitCategory,
							dataType, source, subject, distribution, value, reference, variabilitySubject,
							modelApplicability, error);
					modelMath.getParameter().add(parameter);
				}
			}
			{
				String[] names = getString(sheet, 25).split("\\|\\|");
				String[] units = getString(sheet, 26).split("\\|\\|");

				for (int i = 0; i < names.length; i++) {

					String id = "";
					ParameterClassification classification = ParameterClassification.input;
					String name = names[i].trim();
					String description = null;
					String unit = units[i].trim();
					String unitCategory = "";
					String dataType = "";
					String source = null;
					String subject = null;
					String distribution = null;
					String value = null;
					String reference = null;
					String variabilitySubject = null;
					List<String> modelApplicability = Lists.newArrayList();
					Double error = null;

					Parameter parameter = new Parameter(id, classification, name, description, unit, unitCategory,
							dataType, source, subject, distribution, value, reference, variabilitySubject,
							modelApplicability, error);
					modelMath.getParameter().add(parameter);
				}
			}

			return new GenericModel(generalInformation, scope, null, modelMath, null);
		}

		/**
		 * Gets the string value for the 5th column which holds the property
		 * value.
		 * 
		 * @param sheet
		 * @param rowNumber
		 *            Number of the row describing a property
		 * @return property value
		 * @throws IllegalArgumentException
		 *             If the row or cell is missing or the property value has a
		 *             wrong type
		 */
		private static String getString(final XSSFSheet sheet, final int rowNumber) throws IllegalArgumentException {

			XSSFRow row = sheet.getRow(rowNumber);
			if (row == null)
				throw new IllegalArgumentException("Missing row: #" + rowNumber);

			XSSFCell cell = row.getCell(5);

			try {
				return cell.getStringCellValue();
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Wrong data type");
			}
		}

		/**
		 * Gets the string value for the 5th column which hold the property
		 * value
		 * 
		 * @param sheet
		 * @param rowNumber
		 *            Number of the row describing a property
		 * @return property value or null if missing
		 */
		private static String getOptionalString(final XSSFSheet sheet, final int rowNumber)
				throws IllegalArgumentException {

			XSSFRow row = sheet.getRow(rowNumber);
			if (row == null)
				throw new IllegalArgumentException("Missing row: #" + rowNumber);

			XSSFCell cell = row.getCell(5);

			try {
				return cell.getStringCellValue();
			} catch (IllegalArgumentException e) {
				throw null;
			}
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

	/**
	 * Returns the {@link FskMetaData.DataType} of a value.
	 * 
	 * <ul>
	 * <li>"Array of size x,y,z" for Matlab like arrays, c(0, 1, 2, ...)
	 * <li>"Double" for real numbers.
	 * <li>"Integer" for integer numbers.
	 * <li>"Categorical value" for any other variable. E.g. "zero", "eins",
	 * "dos".
	 * </ul>
	 */
	private static String getValueType(final String value) {
		if (value.startsWith("c(") && value.endsWith(")")) {
			return "Array of size x,y,z";
		} else {
			try {
				Integer.parseInt(value);
				return "Integer";
			} catch (NumberFormatException e1) {
				try {
					Double.parseDouble(value);
					return "Double";
				} catch (NumberFormatException e2) {
					return "Categorical value";
				}
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
