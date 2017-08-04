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
package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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

import com.gmail.gcolaianni5.jris.bean.Record;

import de.bund.bfr.fskml.MissingValueError;
import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.rakip.generic.GeneralInformation;
import de.bund.bfr.rakip.generic.GenericModel;
import de.bund.bfr.rakip.generic.Hazard;
import de.bund.bfr.rakip.generic.ModelCategory;
import de.bund.bfr.rakip.generic.ModelMath;
import de.bund.bfr.rakip.generic.Parameter;
import de.bund.bfr.rakip.generic.ParameterClassification;
import de.bund.bfr.rakip.generic.Product;
import de.bund.bfr.rakip.generic.Scope;
import ezvcard.VCard;

class CreatorNodeModel extends NoInternalsModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(CreatorNodeModel.class);

	private CreatorNodeSettings settings = new CreatorNodeSettings();

	// Input and output port types
	private static final PortType[] IN_TYPES = {};
	private static final PortType[] OUT_TYPES = { FskPortObject.TYPE };

	/** {@inheritDoc} */
	public CreatorNodeModel() {
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
		// Reads model script
		final String modelScriptPath = settings.modelScript.getStringValue();
		if (StringUtils.isEmpty(modelScriptPath)) {
			throw new InvalidSettingsException("Model script is not provided");
		}
		final RScript modelRScript = readScript(modelScriptPath);
		final String modelScript = modelRScript.getScript();

		// Reads parameters script
		final String paramScriptPath = settings.paramScript.getStringValue();
		final String paramScript;
		if (StringUtils.isNotEmpty(paramScriptPath)) {
			paramScript = readScript(paramScriptPath).getScript();
		} else {
			paramScript = "";
		}

		// Reads visualization script
		final String visualizationScriptPath = settings.vizScript.getStringValue();
		final String visualizationScript;
		if (StringUtils.isNotEmpty(visualizationScriptPath)) {
			visualizationScript = readScript(visualizationScriptPath).getScript();
		} else {
			visualizationScript = "";
		}

		// Reads model meta data
		final String metaDataPath = settings.metaDataDoc.getStringValue();
		if (StringUtils.isEmpty(metaDataPath)) {
			throw new InvalidSettingsException("Model metadata is not provided");
		}

		final GenericModel genericModel;
		final File metaDataFile = FileUtil.getFileFromURL(FileUtil.toURL(metaDataPath));
		try (XSSFWorkbook workbook = new XSSFWorkbook(metaDataFile)) {
			final XSSFSheet sheet = workbook.getSheetAt(0);

			// Process metadata
			final GeneralInformation generalInformation = getGeneralInformation(sheet);
			final Scope scope = getScope(sheet);
			final ModelMath modelMath = getModelMath(sheet);
			genericModel = new GenericModel(generalInformation, scope, null, modelMath, null);
			genericModel.getGeneralInformation().setSoftware("R");

			// Set variable values and types from parameters script
			{
				Map<String, String> vars = getVariablesFromAssignments(paramScript);
				List<Parameter> indeps = genericModel.getModelMath().getParameter().stream()
						.filter(it -> it.getClassification().equals(ParameterClassification.input))
						.collect(Collectors.toList());
				indeps.forEach(it -> {
					final String value = vars.get(it.getName());
					it.setValue(value);
				});
			}
		} catch (IOException | InvalidFormatException e) {
			throw new InvalidSettingsException("Invalid metadata");
		}

		final FskPortObject portObj = new FskPortObject(modelScript, paramScript, visualizationScript, genericModel,
				null, Collections.emptySet());

		// libraries
		List<String> libraries = modelRScript.getLibraries();
		if (!libraries.isEmpty()) {
			try {
				// Install missing libraries
				final LibRegistry libReg = LibRegistry.instance();
				List<String> missingLibs = libraries.stream().filter(lib -> !libReg.isInstalled(lib))
						.collect(Collectors.toList());
				if (!missingLibs.isEmpty()) {
					libReg.installLibs(missingLibs);
				}

				Set<Path> libPaths = libReg.getPaths(libraries);
				libPaths.forEach(l -> portObj.libs.add(l.toFile()));
			} catch (RException | REXPMismatchException e) {
				LOGGER.error(e.getMessage());
			}
		}

		return new PortObject[] { portObj };
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
			LOGGER.error(e.getMessage());
			throw new IOException(trimmedPath + ": cannot be read");
		}
	}

	private static String getString(final XSSFSheet sheet, final int rowNumber) {
		final XSSFRow row = sheet.getRow(rowNumber);
		if (row == null)
			throw new IllegalArgumentException("Missing row: #" + rowNumber);
		return row.getCell(5).getStringCellValue();
	}

	private static GeneralInformation getGeneralInformation(final XSSFSheet sheet) throws MalformedURLException {

		final String name = StringUtils.defaultString(getString(sheet, 1));
		final String source = "";
		final String identifier = StringUtils.defaultString(getString(sheet, 2));
		final List<VCard> creators = Collections.emptyList();
		final Date creationDate = sheet.getRow(9).getCell(5).getDateCellValue();
		final List<Date> modifiedDate = Collections.singletonList(sheet.getRow(10).getCell(5).getDateCellValue());
		final String rights = StringUtils.trimToEmpty(getString(sheet, 11));
		final boolean isAvailable = true;

		final String urlString = getString(sheet, 16);
		final URL modelUrl = new URL(StringUtils.defaultIfEmpty(urlString, "http://bfr.bund.de"));

		final String format = "";
		final List<Record> reference = Collections.emptyList();
		final String language = "";
		final String software = "";
		final String languageWrittenIn = "";
		final ModelCategory modelCategory = null;
		final String status = "";
		final String objective = "";
		final String description = "";

		return new GeneralInformation(name, source, identifier, creators, creationDate, modifiedDate, rights,
				isAvailable, modelUrl, format, reference, language, software, languageWrittenIn, modelCategory, status,
				objective, description);
	}

	private static Scope getScope(final XSSFSheet sheet) {

		final Hazard hazard;
		{
			final String hazardType = "";
			final String hazardName = StringUtils.defaultString(getString(sheet, 3));
			final String hazardDescription = StringUtils.defaultString(getString(sheet, 4));
			final String hazardUnit = "";
			final String adverseEffect = null;
			final String origin = null;
			final String benchmarkDose = null;
			final String maximumResidueLimit = null;
			final String noObservedAdverse = null;
			final String lowestObservedAdverse = null;
			final String acceptableOperator = null;
			final String acuteReferenceDose = null;
			final String acceptableDailyIntake = null;
			final String hazardIndSum = null;
			final String laboratoryName = null;
			final String laboratoryCountry = null;
			final String detectionLimit = null;
			final String quantificationLimit = null;
			final String leftCensoredData = null;
			final String rangeOfContamination = null;

			hazard = new Hazard(hazardType, hazardName, hazardDescription, hazardUnit, adverseEffect, origin,
					benchmarkDose, maximumResidueLimit, noObservedAdverse, lowestObservedAdverse, acceptableOperator,
					acuteReferenceDose, acceptableDailyIntake, hazardIndSum, laboratoryName, laboratoryCountry,
					detectionLimit, quantificationLimit, leftCensoredData, rangeOfContamination);
		}

		final Product product;
		{
			final String environmentName = StringUtils.defaultString(getString(sheet, 5));
			final String environmentDescription = StringUtils.defaultString(getString(sheet, 6));
			final String environmentUnit = "";
			final List<String> productionMethod = Collections.emptyList();
			final List<String> packaging = Collections.emptyList();
			final List<String> productTreatment = Collections.emptyList();
			final String originCountry = null;
			final String areaOfOrigin = null;
			final String fisheriesArea = null;
			final Date productionDate = null;
			final Date expirationDate = null;

			product = new Product(environmentName, environmentDescription, environmentUnit, productionMethod, packaging,
					productTreatment, originCountry, areaOfOrigin, fisheriesArea, productionDate, expirationDate);
		}

		return new Scope(product, hazard, null, null, null, Collections.emptyList(), Collections.emptyList());
	}

	private static ModelMath getModelMath(final XSSFSheet sheet) {

		final ModelMath modelMath = new ModelMath();

		// Dependent variables
		final List<String> depNames = Arrays.stream(getString(sheet, 21).split("\\|\\|")).map(String::trim)
				.collect(Collectors.toList());
		final List<String> depUnits = Arrays.stream(getString(sheet, 22).split("\\|\\|")).map(String::trim)
				.collect(Collectors.toList());
		for (int i = 0; i < depNames.size(); i++) {
			final String id = "";
			final ParameterClassification classification = ParameterClassification.input;
			final String name = depNames.get(i);
			final String description = "";
			final String unit = depUnits.get(i);
			final String unitCategory = "";
			final String dataType = "";
			final String source = "";
			final String subject = "";
			final String distribution = "";
			final String value = "";
			final String reference = "";
			final String variabilitySubject = "";
			final List<String> modelApplicability = Collections.emptyList();
			final Double error = null;

			final Parameter param = new Parameter(id, classification, name, description, unit, unitCategory, dataType,
					source, subject, distribution, value, reference, variabilitySubject, modelApplicability, error);
			modelMath.getParameter().add(param);
		}

		// Independent variables
		final List<String> indepNames = Arrays.stream(getString(sheet, 25).split("\\|\\|")).map(String::trim)
				.collect(Collectors.toList());
		final List<String> indepUnits = Arrays.stream(getString(sheet, 26).split("\\|\\|")).map(String::trim)
				.collect(Collectors.toList());
		for (int i = 0; i < indepNames.size(); i++) {
			final String id = "";
			final ParameterClassification classification = ParameterClassification.output;
			final String name = indepNames.get(i);
			final String description = "";
			final String unit = indepUnits.get(i);
			final String unitCategory = "";
			final String dataType = "";
			final String source = "";
			final String subject = "";
			final String distribution = "";
			final String value = "";
			final String reference = "";
			final String variabilitySubject = "";
			final List<String> modelApplicability = Collections.emptyList();
			final Double error = null;

			final Parameter param = new Parameter(id, classification, name, description, unit, unitCategory, dataType,
					source, subject, distribution, value, reference, variabilitySubject, modelApplicability, error);
			modelMath.getParameter().add(param);
		}

		return modelMath;
	}

	private static class Assignment {

		enum Type {
			/** R command with the = assignment operator. E.g. x = value */
			equals,
			/** R command with the <- assignment operator. E.g. x <- value */
			left,
			/**
			 * R command with the <<- scoping assignment operator. E.g. x <<- value
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
