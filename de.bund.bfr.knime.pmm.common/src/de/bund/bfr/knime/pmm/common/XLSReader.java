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
package de.bund.bfr.knime.pmm.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class XLSReader {

	public static String ID_COLUMN = "ID";
	public static String NAME_COLUMN = "Name";
	public static String CONCENTRATION_STDDEV_COLUMN = "Value StdDev";
	public static String CONCENTRATION_MEASURE_NUMBER = "Value Measurements";

	private List<String> warnings;
	private FormulaEvaluator evaluator;

	public XLSReader() {
		warnings = new ArrayList<>();
		evaluator = null;
	}

	public Map<String, KnimeTuple> getTimeSeriesTuples(File file, String sheet, Map<String, Object> columnMappings,
			String timeUnit, String concentrationUnit, String agentColumnName, Map<String, AgentXml> agentMappings,
			String matrixColumnName, Map<String, MatrixXml> matrixMappings, boolean preserveIds, List<Integer> usedIds)
					throws Exception {
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		warnings.clear();
		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (s == null) {
			throw new Exception("Sheet not found");
		}

		Map<String, KnimeTuple> tuples = new LinkedHashMap<>();
		Map<String, Integer> columns = getColumns(s);
		Map<String, Integer> miscColumns = new LinkedHashMap<>();
		Integer idColumn = null;
		Integer commentColumn = null;
		Integer timeColumn = null;
		Integer logcColumn = null;
		Integer stdDevColumn = null;
		Integer nMeasureColumn = null;
		Integer agentDetailsColumn = null;
		Integer matrixDetailsColumn = null;
		Integer agentColumn = null;
		Integer matrixColumn = null;
		String timeColumnName = null;
		String logcColumnName = null;
		String stdDevColumnName = null;
		String nMeasureColumnName = null;

		if (agentColumnName != null) {
			agentColumn = columns.get(agentColumnName);
		}

		if (matrixColumnName != null) {
			matrixColumn = columns.get(matrixColumnName);
		}

		for (String column : columns.keySet()) {
			if (columnMappings.containsKey(column)) {
				Object mapping = columnMappings.get(column);

				if (mapping instanceof MiscXml) {
					miscColumns.put(column, columns.get(column));
				} else if (mapping.equals(ID_COLUMN)) {
					idColumn = columns.get(column);
				} else if (mapping.equals("Comment")) {
					commentColumn = columns.get(column);
				} else if (mapping.equals(AttributeUtilities.TIME)) {
					timeColumn = columns.get(column);
					timeColumnName = column;
				} else if (mapping.equals(AttributeUtilities.CONCENTRATION)) {
					logcColumn = columns.get(column);
					logcColumnName = column;
				} else if (mapping.equals(XLSReader.CONCENTRATION_STDDEV_COLUMN)) {
					stdDevColumn = columns.get(column);
					stdDevColumnName = column;
				} else if (mapping.equals(XLSReader.CONCENTRATION_MEASURE_NUMBER)) {
					nMeasureColumn = columns.get(column);
					nMeasureColumnName = column;
				} else if (mapping.equals(AttributeUtilities.AGENT_DETAILS)) {
					agentDetailsColumn = columns.get(column);
				} else if (mapping.equals(AttributeUtilities.MATRIX_DETAILS)) {
					matrixDetailsColumn = columns.get(column);
				}
			}
		}

		List<Integer> newIds = new ArrayList<>();
		ListMultimap<String, Row> rowsById = LinkedListMultimap.create();

		if (idColumn != null) {
			for (int i = 1; !isEndOfFile(s, i); i++) {
				Row row = s.getRow(i);
				Cell idCell = row.getCell(idColumn);

				if (hasData(idCell)) {
					rowsById.put(getData(idCell), row);
				}
			}
		}

		for (Map.Entry<String, List<Row>> entry : Multimaps.asMap(rowsById).entrySet()) {
			KnimeTuple tuple = new KnimeTuple(SchemaFactory.createDataSchema());
			PmmXmlDoc timeSeriesXml = new PmmXmlDoc();
			String idString = entry.getKey();
			Row firstRow = entry.getValue().get(0);

			Cell commentCell = null;
			Cell agentDetailsCell = null;
			Cell matrixDetailsCell = null;
			Cell agentCell = null;
			Cell matrixCell = null;

			if (commentColumn != null) {
				commentCell = firstRow.getCell(commentColumn);
			}

			if (agentDetailsColumn != null) {
				agentDetailsCell = firstRow.getCell(agentDetailsColumn);
			}

			if (matrixDetailsColumn != null) {
				matrixDetailsCell = firstRow.getCell(matrixDetailsColumn);
			}

			if (agentColumn != null) {
				agentCell = firstRow.getCell(agentColumn);
			}

			if (matrixColumn != null) {
				matrixCell = firstRow.getCell(matrixColumn);
			}

			int id;

			if (preserveIds && !usedIds.isEmpty()) {
				id = usedIds.remove(0);
			} else {
				id = MathUtilities.getRandomNegativeInt();
			}

			newIds.add(id);
			tuple = new KnimeTuple(SchemaFactory.createDataSchema());
			tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, idString);
			tuple.setValue(TimeSeriesSchema.ATT_CONDID, id);
			timeSeriesXml = new PmmXmlDoc();

			PmmXmlDoc dataInfo = new PmmXmlDoc();
			PmmXmlDoc agentXml = new PmmXmlDoc();
			PmmXmlDoc matrixXml = new PmmXmlDoc();

			if (commentCell != null) {
				dataInfo.add(new MdInfoXml(null, null, getData(commentCell), null, null));
			} else {
				dataInfo.add(new MdInfoXml(null, null, null, null, null));
			}

			if (hasData(agentCell) && agentMappings.get(getData(agentCell)) != null) {
				agentXml.add(agentMappings.get(getData(agentCell)));
			} else {
				agentXml.add(new AgentXml());
			}

			if (hasData(matrixCell) && matrixMappings.get(getData(matrixCell)) != null) {
				matrixXml.add(matrixMappings.get(getData(matrixCell)));
			} else {
				matrixXml.add(new MatrixXml());
			}

			if (hasData(agentDetailsCell)) {
				((AgentXml) agentXml.get(0)).detail = getData(agentDetailsCell);
			}

			if (hasData(matrixDetailsCell)) {
				((MatrixXml) matrixXml.get(0)).detail = getData(matrixDetailsCell);
			}

			tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataInfo);
			tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
			tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);

			PmmXmlDoc miscXML = new PmmXmlDoc();

			for (String column : miscColumns.keySet()) {
				MiscXml misc = (MiscXml) columnMappings.get(column);
				Cell cell = firstRow.getCell(miscColumns.get(column));

				if (hasData(cell)) {
					try {
						misc.value = Double.parseDouble(getData(cell).replace(",", "."));
					} catch (NumberFormatException e) {
						warnings.add(column + " value in row " + (firstRow.getRowNum() + 1) + " is not valid ("
								+ getData(cell) + ")");
						misc.value = null;
					}
				} else {
					misc.value = null;
				}

				misc.origUnit = misc.unit;
				miscXML.add(misc);
			}

			tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);

			for (Row row : entry.getValue()) {
				Cell timeCell = null;
				Cell logcCell = null;
				Cell stdDevCell = null;
				Cell nMeasureCell = null;

				if (timeColumn != null) {
					timeCell = row.getCell(timeColumn);
				}

				if (logcColumn != null) {
					logcCell = row.getCell(logcColumn);
				}

				if (stdDevColumn != null) {
					stdDevCell = row.getCell(stdDevColumn);
				}

				if (nMeasureColumn != null) {
					nMeasureCell = row.getCell(nMeasureColumn);
				}

				Double time = null;
				Double logc = null;
				Double stdDev = null;
				Integer nMeasure = null;

				if (hasData(timeCell)) {
					try {
						time = Double.parseDouble(getData(timeCell).replace(",", "."));
					} catch (NumberFormatException e) {
						warnings.add(timeColumnName + " value in row " + (row.getRowNum() + 1) + " is not valid ("
								+ getData(timeCell) + ")");
					}
				} else if (timeColumn != null) {
					warnings.add(timeColumnName + " value in row " + (row.getRowNum() + 1) + " is missing");
				}

				if (hasData(logcCell)) {
					try {
						logc = Double.parseDouble(getData(logcCell).replace(",", "."));
					} catch (NumberFormatException e) {
						warnings.add(logcColumnName + " value in row " + (row.getRowNum() + 1) + " is not valid ("
								+ getData(logcCell) + ")");
					}
				} else if (logcColumn != null) {
					warnings.add(logcColumnName + " value in row " + (row.getRowNum() + 1) + " is missing");
				}

				if (hasData(stdDevCell)) {
					try {
						stdDev = Double.parseDouble(getData(stdDevCell).replace(",", "."));
					} catch (NumberFormatException e) {
						warnings.add(stdDevColumnName + " value in row " + (row.getRowNum() + 1) + " is not valid ("
								+ getData(stdDevCell) + ")");
					}
				} else if (stdDevColumn != null) {
					warnings.add(stdDevColumnName + " value in row " + (row.getRowNum() + 1) + " is missing");
				}

				if (hasData(nMeasureCell)) {
					try {
						String number = getData(nMeasureCell).replace(",", ".");

						if (number.contains(".")) {
							number = number.substring(0, number.indexOf("."));
						}

						nMeasure = Integer.parseInt(number);
					} catch (NumberFormatException e) {
						warnings.add(nMeasureColumnName + " value in row " + (row.getRowNum() + 1) + " is not valid ("
								+ getData(nMeasureCell) + ")");
					}
				} else if (nMeasureColumn != null) {
					warnings.add(nMeasureColumnName + " value in row " + (row.getRowNum() + 1) + " is missing");
				}

				for (String column : miscColumns.keySet()) {
					PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
					Cell cell = row.getCell(miscColumns.get(column));

					if (hasData(cell)) {
						try {
							String param = ((MiscXml) columnMappings.get(column)).name;
							double value = Double.parseDouble(getData(cell).replace(",", "."));

							if (!hasSameValue(param, value, misc)) {
								warnings.add("Variable conditions cannot be imported: " + "Only first value for "
										+ column + " is used");
							}
						} catch (NumberFormatException e) {
						}
					}
				}

				timeSeriesXml.add(new TimeSeriesXml(null, time, timeUnit, logc, concentrationUnit, stdDev, nMeasure));
			}

			tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);
			tuples.put(idString, tuple);
		}

		usedIds.clear();
		usedIds.addAll(newIds);

		return tuples;

	}

	public Map<String, KnimeTuple> getModelTuples(File file, String sheet, Map<String, Object> columnMappings,
			String agentColumnName, Map<String, AgentXml> agentMappings, String matrixColumnName,
			Map<String, MatrixXml> matrixMappings, KnimeTuple modelTuple, Map<String, String> modelMappings,
			Map<String, String> modelParamErrors, String modelDepMin, String modelDepMax, String modelDepUnit,
			String modelIndepMin, String modelIndepMax, String modelIndepUnit, String modelRmse, String modelR2,
			String modelAic, String modelDataPoints, Map<String, KnimeTuple> secModelTuples,
			Map<String, Map<String, String>> secModelMappings, Map<String, Map<String, String>> secModelParamErrors,
			Map<String, Map<String, String>> secModelIndepMins, Map<String, Map<String, String>> secModelIndepMaxs,
			Map<String, Map<String, String>> secModelIndepCategories,
			Map<String, Map<String, String>> secModelIndepUnits, Map<String, String> secModelRmse,
			Map<String, String> secModelR2, Map<String, String> secModelAic, Map<String, String> secModelDataPoints,
			boolean preserveIds, List<Integer> usedIds, Map<String, List<Integer>> secUsedIds,
			List<Integer> globalUsedIds) throws Exception {
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		warnings.clear();
		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (s == null) {
			throw new Exception("Sheet not found");
		}

		Map<String, KnimeTuple> tuples = new LinkedHashMap<>();
		Map<String, Integer> columns = getColumns(s);
		Map<String, Integer> miscColumns = new LinkedHashMap<>();
		Integer idColumn = null;
		Integer commentColumn = null;
		Integer agentDetailsColumn = null;
		Integer matrixDetailsColumn = null;
		Integer agentColumn = columns.get(agentColumnName);
		Integer matrixColumn = columns.get(matrixColumnName);
		Integer depMinColumn = columns.get(modelDepMin);
		Integer depMaxColumn = columns.get(modelDepMax);
		Integer indepMinColumn = columns.get(modelIndepMin);
		Integer indepMaxColumn = columns.get(modelIndepMax);
		Integer rmseColumn = columns.get(modelRmse);
		Integer r2Column = columns.get(modelR2);
		Integer aicColumn = columns.get(modelAic);
		Integer dataPointsColumn = columns.get(modelDataPoints);

		for (String column : columns.keySet()) {
			if (columnMappings.containsKey(column)) {
				Object mapping = columnMappings.get(column);

				if (mapping instanceof MiscXml) {
					miscColumns.put(column, columns.get(column));
				} else if (mapping.equals(NAME_COLUMN)) {
					idColumn = columns.get(column);
				} else if (mapping.equals("Comment")) {
					commentColumn = columns.get(column);
				} else if (mapping.equals(AttributeUtilities.AGENT_DETAILS)) {
					agentDetailsColumn = columns.get(column);
				} else if (mapping.equals(AttributeUtilities.MATRIX_DETAILS)) {
					matrixDetailsColumn = columns.get(column);
				}
			}
		}

		int index = 0;
		List<Integer> newIds = new ArrayList<>();
		Map<String, List<Integer>> newSecIds = new LinkedHashMap<>();
		List<Integer> newGlobalIds = new ArrayList<>();

		for (int rowNumber = 1;; rowNumber++) {
			if (isEndOfFile(s, rowNumber)) {
				break;
			}

			int globalID;

			if (preserveIds && !globalUsedIds.isEmpty()) {
				globalID = globalUsedIds.remove(0);
			} else {
				globalID = MathUtilities.getRandomNegativeInt();
			}

			newGlobalIds.add(globalID);
			KnimeTuple dataTuple = new KnimeTuple(SchemaFactory.createDataSchema());
			Row row = s.getRow(rowNumber);
			Cell idCell = getCell(row, idColumn);
			Cell commentCell = getCell(row, commentColumn);
			Cell agentDetailsCell = getCell(row, agentDetailsColumn);
			Cell matrixDetailsCell = getCell(row, matrixDetailsColumn);
			Cell agentCell = getCell(row, agentColumn);
			Cell matrixCell = getCell(row, matrixColumn);
			Cell depMinCell = getCell(row, depMinColumn);
			Cell depMaxCell = getCell(row, depMaxColumn);
			Cell indepMinCell = getCell(row, indepMinColumn);
			Cell indepMaxCell = getCell(row, indepMaxColumn);
			Cell rmseCell = getCell(row, rmseColumn);
			Cell r2Cell = getCell(row, r2Column);
			Cell aicCell = getCell(row, aicColumn);
			Cell dataPointsCell = getCell(row, dataPointsColumn);

			dataTuple.setValue(TimeSeriesSchema.ATT_CONDID, MathUtilities.getRandomNegativeInt());

			PmmXmlDoc dataInfo = new PmmXmlDoc();
			PmmXmlDoc agentXml = new PmmXmlDoc();
			PmmXmlDoc matrixXml = new PmmXmlDoc();

			if (hasData(commentCell)) {
				dataInfo.add(new MdInfoXml(null, null, getData(commentCell), null, null));
			} else {
				dataInfo.add(new MdInfoXml(null, null, null, null, null));

				if (commentColumn != null) {
					// warnings.add(MdInfoXml.ATT_COMMENT + " value in row "
					// + (rowNumber + 1) + " is missing");
				}
			}

			if (hasData(agentCell) && agentMappings.get(getData(agentCell)) != null) {
				agentXml.add(new AgentXml(agentMappings.get(getData(agentCell))));
			} else {
				agentXml.add(new AgentXml());

				if (agentColumn != null) {
					warnings.add(TimeSeriesSchema.ATT_AGENT + " value in row " + (rowNumber + 1) + " is missing");
				}
			}

			if (hasData(matrixCell) && matrixMappings.get(getData(matrixCell)) != null) {
				matrixXml.add(new MatrixXml(matrixMappings.get(getData(matrixCell))));
			} else {
				matrixXml.add(new MatrixXml());

				if (matrixColumn != null) {
					warnings.add(TimeSeriesSchema.ATT_MATRIX + " value in row " + (rowNumber + 1) + " is missing");
				}
			}

			if (hasData(agentDetailsCell)) {
				((AgentXml) agentXml.get(0)).detail = getData(agentDetailsCell);
			}

			if (hasData(matrixDetailsCell)) {
				((MatrixXml) matrixXml.get(0)).detail = getData(matrixDetailsCell);
			}

			dataTuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataInfo);
			dataTuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
			dataTuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);

			PmmXmlDoc miscXML = new PmmXmlDoc();

			for (String column : miscColumns.keySet()) {
				MiscXml misc = new MiscXml((MiscXml) columnMappings.get(column));
				Cell cell = row.getCell(miscColumns.get(column));

				if (hasData(cell)) {
					try {
						misc.value = Double.parseDouble(getData(cell).replace(",", "."));
					} catch (NumberFormatException e) {
						warnings.add(
								column + " value in row " + (rowNumber + 1) + " is not valid (" + getData(cell) + ")");
					}
				} else {
					warnings.add(column + " value in row " + (rowNumber + 1) + " is missing");
				}

				misc.origUnit = misc.unit;
				miscXML.add(misc);
			}

			dataTuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);

			PmmXmlDoc modelXml = modelTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			PmmXmlDoc paramXml = modelTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			PmmXmlDoc estXml = modelTuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
			PmmXmlDoc depXml = modelTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
			PmmXmlDoc indepXml = modelTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			int primId;

			if (preserveIds && !usedIds.isEmpty()) {
				primId = usedIds.remove(0);
			} else {
				primId = MathUtilities.getRandomNegativeInt();
			}

			newIds.add(primId);

			if (modelDepUnit != null && !modelDepUnit.equals(((DepXml) depXml.get(0)).unit)) {
				((DepXml) depXml.get(0)).unit = modelDepUnit;
				((CatalogModelXml) modelXml.get(0)).id = MathUtilities.getRandomNegativeInt();
			}

			if (hasData(depMinCell)) {
				try {
					((DepXml) depXml.get(0)).min = Double.parseDouble(getData(depMinCell).replace(",", "."));
				} catch (NumberFormatException e) {
					warnings.add(modelDepMin + " value in row " + (rowNumber + 1) + " is not valid ("
							+ getData(depMinCell) + ")");
				}
			} else if (modelDepMin != null) {
				warnings.add(modelDepMin + " value in row " + (rowNumber + 1) + " is missing");
			}

			if (hasData(depMaxCell)) {
				try {
					((DepXml) depXml.get(0)).max = Double.parseDouble(getData(depMaxCell).replace(",", "."));
				} catch (NumberFormatException e) {
					warnings.add(modelDepMax + " value in row " + (rowNumber + 1) + " is not valid ("
							+ getData(depMaxCell) + ")");
				}
			} else if (modelDepMax != null) {
				warnings.add(modelDepMax + " value in row " + (rowNumber + 1) + " is missing");
			}

			if (hasData(indepMinCell)) {
				try {
					((IndepXml) indepXml.get(0)).min = Double.parseDouble(getData(indepMinCell).replace(",", "."));
				} catch (NumberFormatException e) {
					warnings.add(modelIndepMin + " value in row " + (rowNumber + 1) + " is not valid ("
							+ getData(indepMinCell) + ")");
				}
			} else if (modelIndepMin != null) {
				warnings.add(modelIndepMin + " value in row " + (rowNumber + 1) + " is missing");
			}

			if (hasData(indepMaxCell)) {
				try {
					((IndepXml) indepXml.get(0)).max = Double.parseDouble(getData(indepMaxCell).replace(",", "."));
				} catch (NumberFormatException e) {
					warnings.add(modelIndepMax + " value in row " + (rowNumber + 1) + " is not valid ("
							+ getData(indepMaxCell) + ")");
				}
			} else if (modelIndepMax != null) {
				warnings.add(modelIndepMax + " value in row " + (rowNumber + 1) + " is missing");
			}

			if (modelIndepUnit != null && !modelIndepUnit.equals(((IndepXml) indepXml.get(0)).unit)) {
				((IndepXml) indepXml.get(0)).unit = modelIndepUnit;
				((CatalogModelXml) modelXml.get(0)).id = MathUtilities.getRandomNegativeInt();
			}

			((EstModelXml) estXml.get(0)).id = primId;
			((EstModelXml) estXml.get(0)).comment = getData(commentCell);

			if (hasData(rmseCell)) {
				try {
					((EstModelXml) estXml.get(0)).rms = Double.parseDouble(getData(rmseCell).replace(",", "."));
				} catch (NumberFormatException e) {
					warnings.add(modelRmse + " value in row " + (rowNumber + 1) + " is not valid (" + getData(rmseCell)
							+ ")");
				}
			}

			if (hasData(r2Cell)) {
				try {
					((EstModelXml) estXml.get(0)).r2 = Double.parseDouble(getData(r2Cell).replace(",", "."));
				} catch (NumberFormatException e) {
					warnings.add(
							modelR2 + " value in row " + (rowNumber + 1) + " is not valid (" + getData(r2Cell) + ")");
				}
			}

			if (hasData(aicCell)) {
				try {
					((EstModelXml) estXml.get(0)).aic = Double.parseDouble(getData(aicCell).replace(",", "."));
				} catch (NumberFormatException e) {
					warnings.add(
							modelAic + " value in row " + (rowNumber + 1) + " is not valid (" + getData(aicCell) + ")");
				}
			}

			if (hasData(dataPointsCell)) {
				String data = getData(dataPointsCell).replace(".0", "").replace(",0", "");

				try {
					((EstModelXml) estXml.get(0)).dof = Integer.parseInt(data) - paramXml.size();
				} catch (NumberFormatException e) {
					warnings.add(modelDataPoints + " value in row " + (rowNumber + 1) + " is not valid (" + data + ")");
				}
			}

			if (hasData(idCell)) {
				((EstModelXml) estXml.get(0)).name = getData(idCell);
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;
				String mapping = modelMappings.get(element.name);

				if (mapping != null) {
					Cell cell = row.getCell(columns.get(mapping));

					if (hasData(cell)) {
						try {
							element.value = Double.parseDouble(getData(cell).replace(",", "."));
						} catch (NumberFormatException e) {
							warnings.add(mapping + " value in row " + (rowNumber + 1) + " is not valid ("
									+ getData(cell) + ")");
						}
					} else {
						warnings.add(mapping + " value in row " + (rowNumber + 1) + " is missing");
					}
				}

				String errorMapping = modelParamErrors.get(element.name);

				if (errorMapping != null) {
					Cell cell = row.getCell(columns.get(errorMapping));

					if (hasData(cell)) {
						try {
							element.error = Double.parseDouble(getData(cell).replace(",", "."));
						} catch (NumberFormatException e) {
							warnings.add(errorMapping + " value in row " + (rowNumber + 1) + " is not valid ("
									+ getData(cell) + ")");
						}
					} else {
						warnings.add(errorMapping + " value in row " + (rowNumber + 1) + " is missing");
					}
				}
			}

			modelTuple.setValue(Model1Schema.ATT_DEPENDENT, depXml);
			modelTuple.setValue(Model1Schema.ATT_INDEPENDENT, indepXml);
			modelTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
			modelTuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);
			modelTuple.setValue(Model1Schema.ATT_ESTMODEL, estXml);

			if (secModelTuples.isEmpty()) {
				tuples.put(index + "", new KnimeTuple(SchemaFactory.createM1DataSchema(), modelTuple, dataTuple));
				index++;
			} else {
				for (String param : secModelTuples.keySet()) {
					KnimeTuple secTuple = secModelTuples.get(param);
					PmmXmlDoc secParamXml = secTuple.getPmmXml(Model2Schema.ATT_PARAMETER);
					PmmXmlDoc secDepXml = secTuple.getPmmXml(Model2Schema.ATT_DEPENDENT);
					PmmXmlDoc secEstXml = secTuple.getPmmXml(Model2Schema.ATT_ESTMODEL);
					PmmXmlDoc secModelXml = secTuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
					PmmXmlDoc secIndepXml = secTuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
					String formula = ((CatalogModelXml) secModelXml.get(0)).formula;
					int secID;

					if (preserveIds && secUsedIds.containsKey(param) && !secUsedIds.get(param).isEmpty()) {
						secID = secUsedIds.get(param).remove(0);
					} else {
						secID = MathUtilities.getRandomNegativeInt();
					}

					if (!newSecIds.containsKey(param)) {
						newSecIds.put(param, new ArrayList<Integer>());
					}

					newSecIds.get(param).add(secID);
					formula = MathUtilities.replaceVariable(formula, ((DepXml) secDepXml.get(0)).name, param);
					((CatalogModelXml) secModelXml.get(0)).formula = formula;
					((DepXml) secDepXml.get(0)).name = param;
					((EstModelXml) secEstXml.get(0)).id = secID;

					for (PmmXmlElementConvertable el : secParamXml.getElementSet()) {
						ParamXml element = (ParamXml) el;
						String mapping = secModelMappings.get(param).get(element.name);
						String error = secModelParamErrors.get(param).get(element.name);

						if (mapping != null) {
							Cell cell = row.getCell(columns.get(mapping));

							if (hasData(cell)) {
								try {
									element.value = Double.parseDouble(getData(cell).replace(",", "."));
								} catch (NumberFormatException e) {
									warnings.add(mapping + " value in row " + (rowNumber + 1) + " is not valid ("
											+ getData(cell) + ")");
								}
							} else {
								warnings.add(mapping + " value in row " + (rowNumber + 1) + " is missing");
							}
						}

						if (error != null) {
							Cell cell = row.getCell(columns.get(error));

							if (hasData(cell)) {
								try {
									element.error = Double.parseDouble(getData(cell).replace(",", "."));
								} catch (NumberFormatException e) {
									warnings.add(error + " value in row " + (rowNumber + 1) + " is not valid ("
											+ getData(cell) + ")");
								}
							} else {
								warnings.add(error + " value in row " + (rowNumber + 1) + " is missing");
							}
						}
					}

					for (PmmXmlElementConvertable el : secIndepXml.getElementSet()) {
						IndepXml element = (IndepXml) el;
						String category = secModelIndepCategories.get(param).get(element.name);
						String unit = secModelIndepUnits.get(param).get(element.name);

						if (category == null || unit == null) {
							continue;
						}

						if (!category.equals(element.category)) {
							element.category = category;
							((CatalogModelXml) secModelXml.get(0)).id = MathUtilities.getRandomNegativeInt();
						}

						if (!unit.equals(element.unit)) {
							element.unit = unit;
							((CatalogModelXml) secModelXml.get(0)).id = MathUtilities.getRandomNegativeInt();
						}

						String minColumn = secModelIndepMins.get(param).get(element.name);
						String maxColumn = secModelIndepMaxs.get(param).get(element.name);

						if (minColumn != null) {
							Cell minCell = row.getCell(columns.get(minColumn));

							if (hasData(minCell)) {
								try {
									element.min = Double.parseDouble(getData(minCell).replace(",", "."));
								} catch (NumberFormatException e) {
									warnings.add(minColumn + " value in row " + (rowNumber + 1) + " is not valid ("
											+ getData(minCell) + ")");
								}
							} else {
								warnings.add(minColumn + " value in row " + (rowNumber + 1) + " is missing");
							}
						}

						if (maxColumn != null) {
							Cell maxCell = row.getCell(columns.get(maxColumn));

							if (hasData(maxCell)) {
								try {
									element.max = Double.parseDouble(getData(maxCell).replace(",", "."));
								} catch (NumberFormatException e) {
									warnings.add(maxColumn + " value in row " + (rowNumber + 1) + " is not valid ("
											+ getData(maxCell) + ")");
								}
							} else {
								warnings.add(maxColumn + " value in row " + (rowNumber + 1) + " is missing");
							}
						}
					}

					String rmse = secModelRmse.get(param);
					String r2 = secModelR2.get(param);
					String aic = secModelAic.get(param);
					String dataPoints = secModelDataPoints.get(param);

					if (rmse != null) {
						Cell cell = row.getCell(columns.get(rmse));

						if (hasData(cell)) {
							try {
								((EstModelXml) secEstXml.get(0))
										.rms = Double.parseDouble(getData(cell).replace(",", "."));
							} catch (NumberFormatException e) {
								warnings.add(rmse + " value in row " + (rowNumber + 1) + " is not valid ("
										+ getData(cell) + ")");
							}
						} else {
							warnings.add(rmse + " value in row " + (rowNumber + 1) + " is missing");
						}
					}

					if (r2 != null) {
						Cell cell = row.getCell(columns.get(r2));

						if (hasData(cell)) {
							try {
								((EstModelXml) secEstXml.get(0))
										.r2 = Double.parseDouble(getData(cell).replace(",", "."));
							} catch (NumberFormatException e) {
								warnings.add(r2 + " value in row " + (rowNumber + 1) + " is not valid (" + getData(cell)
										+ ")");
							}
						} else {
							warnings.add(r2 + " value in row " + (rowNumber + 1) + " is missing");
						}
					}

					if (aic != null) {
						Cell cell = row.getCell(columns.get(aic));

						if (hasData(cell)) {
							try {
								((EstModelXml) secEstXml.get(0))
										.aic = Double.parseDouble(getData(cell).replace(",", "."));
							} catch (NumberFormatException e) {
								warnings.add(aic + " value in row " + (rowNumber + 1) + " is not valid ("
										+ getData(cell) + ")");
							}
						} else {
							warnings.add(aic + " value in row " + (rowNumber + 1) + " is missing");
						}
					}

					if (dataPoints != null) {
						Cell cell = row.getCell(columns.get(dataPoints));

						if (hasData(cell)) {
							String data = getData(cell).replace(".0", "").replace(",0", "");

							try {
								((EstModelXml) secEstXml.get(0)).dof = Integer.parseInt(data) - secParamXml.size();
							} catch (NumberFormatException e) {
								warnings.add(dataPoints + " value in row " + (rowNumber + 1) + " is not valid (" + data
										+ ")");
							}
						} else {
							warnings.add(dataPoints + " value in row " + (rowNumber + 1) + " is missing");
						}
					}

					secTuple.setValue(Model2Schema.ATT_MODELCATALOG, secModelXml);
					secTuple.setValue(Model2Schema.ATT_PARAMETER, secParamXml);
					secTuple.setValue(Model2Schema.ATT_DEPENDENT, secDepXml);
					secTuple.setValue(Model2Schema.ATT_ESTMODEL, secEstXml);
					secTuple.setValue(Model2Schema.ATT_INDEPENDENT, secIndepXml);
					secTuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalID);

					tuples.put(index + "", new KnimeTuple(SchemaFactory.createM12DataSchema(),
							new KnimeTuple(SchemaFactory.createM1DataSchema(), modelTuple, dataTuple), secTuple));
					index++;
				}
			}
		}

		usedIds.clear();
		usedIds.addAll(newIds);
		secUsedIds.clear();
		secUsedIds.putAll(newSecIds);
		globalUsedIds.clear();
		globalUsedIds.addAll(newGlobalIds);

		return tuples;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public List<String> getSheets(File file) throws Exception {
		List<String> sheets = new ArrayList<>();
		Workbook workbook = getWorkbook(file);

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheets.add(workbook.getSheetName(i));
		}

		return sheets;
	}

	public List<String> getColumns(File file, String sheet) throws Exception {
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (s == null) {
			throw new Exception("Sheet not found");
		}

		return new ArrayList<>(getColumns(s).keySet());
	}

	public Set<String> getValuesInColumn(File file, String sheet, String column) throws Exception {
		Set<String> valueSet = new LinkedHashSet<>();
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (s == null) {
			throw new Exception("Sheet not found");
		}

		Map<String, Integer> columns = getColumns(s);
		int columnId = columns.get(column);

		for (int i = 1; i <= s.getLastRowNum(); i++) {
			if (s.getRow(i) != null) {
				Cell cell = s.getRow(i).getCell(columnId);

				if (hasData(cell)) {
					valueSet.add(getData(cell));
				}
			}
		}

		return valueSet;
	}

	public List<Integer> getMissingData(File file, String sheet, String column) throws Exception {
		List<Integer> missing = new ArrayList<>();
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		Map<String, Integer> columns = getColumns(s);
		int columnId = columns.get(column);

		for (int i = 1; i <= s.getLastRowNum(); i++) {
			if (s.getRow(i) != null && !hasData(s.getRow(i).getCell(columnId))) {
				for (int c : columns.values()) {
					if (hasData(s.getRow(i).getCell(c))) {
						missing.add(i + 1);
						break;
					}
				}
			}
		}

		return missing;
	}

	private Workbook getWorkbook(File file) throws IOException, InvalidFormatException {
		if (file.exists()) {
			try (InputStream in = new FileInputStream(file)) {
				return WorkbookFactory.create(in);
			}
		} else {
			try (InputStream in = new URL(file.getPath()).openStream()) {
				return WorkbookFactory.create(in);
			}
		}
	}

	private Map<String, Integer> getColumns(Sheet sheet) {
		Map<String, Integer> columns = new LinkedHashMap<>();

		for (int i = 0;; i++) {
			Cell cell = sheet.getRow(0).getCell(i);

			if (!hasData(cell)) {
				break;
			}

			columns.put(getData(cell), i);
		}

		return columns;
	}

	private boolean isEndOfFile(Sheet sheet, int i) {
		Row row = sheet.getRow(i);

		if (row == null) {
			return true;
		}

		for (int j = 0;; j++) {
			Cell headerCell = sheet.getRow(0).getCell(j);
			Cell cell = sheet.getRow(i).getCell(j);

			if (!hasData(headerCell)) {
				return true;
			}

			if (hasData(cell)) {
				return false;
			}
		}
	}

	private boolean hasData(Cell cell) {
		return cell != null && !cell.toString().trim().isEmpty();
	}

	private String getData(Cell cell) {
		if (cell != null) {
			if (cell.getCellType() == CellType.FORMULA) {
				CellValue value = evaluator.evaluate(cell);

				switch (value.getCellType()) {
				case BOOLEAN:
					return value.getBooleanValue() + "";
				case NUMERIC:
					return value.getNumberValue() + "";
				case STRING:
					return value.getStringValue();
				default:
					return "";
				}
			} else {
				return cell.toString().trim();
			}
		}

		return null;
	}

	private Cell getCell(Row row, Integer column) {
		if (column == null) {
			return null;
		}

		return row.getCell(column);
	}

	private boolean hasSameValue(String param, Double value, PmmXmlDoc miscs) {
		for (PmmXmlElementConvertable el : miscs.getElementSet()) {
			MiscXml misc = (MiscXml) el;

			if (misc.name.equals(param) && !value.equals(misc.value)) {
				return false;
			}
		}

		return true;
	}
}
