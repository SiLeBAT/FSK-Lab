/*
 ***************************************************************************************************
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.pmm.fskx.creator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;

//
import com.google.common.base.Strings;

import de.bund.bfr.fskml.RScript;
import de.bund.bfr.knime.fsklab.nodes.Variable;
import de.bund.bfr.knime.fsklab.nodes.Variable.DataType;
import de.bund.bfr.knime.fsklab.nodes.port.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.port.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.r.client.LibRegistry;
import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import metadata.LegacyMetadataImporter;

public class FskxCreatorNodeModel extends ExtToolOutputNodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(FskxCreatorNodeModel.class);

  // configuration key of the libraries directory
  static final String CFGKEY_DIR_LIBS = "dirLibs";

  // configuration key of the selected libraries
  static final String CFGKEY_LIBS = "libs";

  // configuration key of the path of the R model script
  static final String CFGKEY_MODEL_SCRIPT = "modelScript";

  // configuration key of the path of the R parameters script
  static final String CFGKEY_PARAM_SCRIPT = "paramScript";

  // configuration key of the path of the R visualization script
  static final String CFGKEY_VISUALIZATION_SCRIPT = "visualizationScript";

  // configuration key of the path of the XLSX spreadsheet with the model meta
  // data
  static final String CFGKEY_SPREADSHEET = "spreadsheet";

  private final static PortType[] inPortTypes = new PortType[] {};
  private final static PortType[] outPortTypes = new PortType[] {FskPortObject.TYPE};

  // Settings models
  private final SettingsModelString m_modelScript =
      new SettingsModelString(CFGKEY_MODEL_SCRIPT, null);
  private final SettingsModelString m_paramScript =
      new SettingsModelString(CFGKEY_PARAM_SCRIPT, null);
  private final SettingsModelString m_vizScript =
      new SettingsModelString(CFGKEY_VISUALIZATION_SCRIPT, null);
  private final SettingsModelString m_metaDataDoc =
      new SettingsModelString(CFGKEY_SPREADSHEET, null);
  private final SettingsModelString m_libDirectory = new SettingsModelString(CFGKEY_DIR_LIBS, null);
  private final SettingsModelStringArray m_selectedLibs =
      new SettingsModelStringArray(CFGKEY_LIBS, null);

  public FskxCreatorNodeModel() {
    super(inPortTypes, outPortTypes);
  }

  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // nothing
  }

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // nothing
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    m_modelScript.saveSettingsTo(settings);
    m_paramScript.saveSettingsTo(settings);
    m_vizScript.saveSettingsTo(settings);
    m_metaDataDoc.saveSettingsTo(settings);
    m_libDirectory.saveSettingsTo(settings);
    m_selectedLibs.saveSettingsTo(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    m_modelScript.validateSettings(settings);
    m_paramScript.validateSettings(settings);
    m_vizScript.validateSettings(settings);
    m_metaDataDoc.validateSettings(settings);
    m_libDirectory.validateSettings(settings);
    m_selectedLibs.validateSettings(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    m_modelScript.loadSettingsFrom(settings);
    m_paramScript.loadSettingsFrom(settings);
    m_vizScript.loadSettingsFrom(settings);
    m_metaDataDoc.loadSettingsFrom(settings);
    m_libDirectory.loadSettingsFrom(settings);
    m_selectedLibs.loadSettingsFrom(settings);
  }

  @Override
  protected void reset() {
    // does nothing
  }

  /**
   * @throws InvalidFormatException if the specified metadata file does not exist.
   */
  @Override
  protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
      throws InvalidSettingsException, IOException, InvalidFormatException {

    FskPortObject portObj = new FskPortObject();

    List<String> libraries = new ArrayList<>();

    // Reads model script
    try {
      RScript rScript = readScript(m_modelScript.getStringValue()); // May throw IOException

      portObj.model = rScript.getScript();
      libraries.addAll(rScript.getLibraries());
    } catch (IOException e) {
      portObj.model = "";
    }

    // Reads parameters script
    try {
      RScript rScript = readScript(m_paramScript.getStringValue()); // May throw IOException

      portObj.param = rScript.getScript();
      libraries.addAll(rScript.getLibraries());
    } catch (IOException e) {
      portObj.param = "";
    }

    // Reads visualization script
    try {
      RScript rScript = readScript(m_vizScript.getStringValue()); // May throw IOException

      portObj.viz = rScript.getScript();
      libraries.addAll(rScript.getLibraries());
    } catch (IOException e) {
      portObj.viz = "";
    }

    // Reads model meta data
    File metadataFile = FileUtil.getFileFromURL(FileUtil.toURL(m_metaDataDoc.getStringValue()));
    try (XSSFWorkbook workbook = new XSSFWorkbook(metadataFile)) {
    	XSSFSheet sheet = workbook.getSheetAt(0);
    	portObj.template = new LegacyMetadataImporter().processSpreadsheet(sheet);
    }
    portObj.template.software = FskMetaData.Software.R;

    // Set variable values from parameters script
    {
      Map<String, String> vars = getVariablesFromAssignments(portObj.param);
      for (Variable v : portObj.template.independentVariables) {
        if (vars.containsKey(v.name.trim())) {
          v.value = vars.get(v.name.trim());
        }
      }
    }

    // Set types of variables
    {
      // TODO: usually the type of the depvar is numeric although it
      // should be checked
      portObj.template.dependentVariable.type = DataType.numeric;

      /*
       * TODO: FskMetaData is keeping only numeric types for independent variables so it does not
       * make sense to try to obtain the type here since it will always be numeric. Once the rest of
       * types are supported in FskMetaData the following code should be update to retrieve the
       * types.
       */
      for (Variable v : portObj.template.independentVariables) {
        v.type = DataType.numeric;
      }
    }

    // Libraries
    if (!libraries.isEmpty()) {
      try {
        // Find out missing libraries
        final LibRegistry libReg = LibRegistry.instance();
        List<String> missingLibs =
            libraries.stream().filter(lib -> !libReg.isInstalled(lib)).collect(Collectors.toList());

        // Install missing libraries
        if (!missingLibs.isEmpty()) {
          libReg.installLibs(missingLibs);

          Set<Path> libPaths = libReg.getPaths(libraries);
          libPaths.forEach(l -> portObj.libs.add(l.toFile()));
        }
      } catch (RException | REXPMismatchException e) {
        LOGGER.error(e.getMessage());
      }
    }

    return new PortObject[] {portObj};
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  /**
   * Reads R script.
   * 
   * @param path File path to R model script.
   * @throws InvalidSettingsException if {@link path} is null or whitespace.
   * @throws IOException if the file cannot be read.
   */
  private static RScript readScript(final String path)
      throws InvalidSettingsException, IOException {

    // throws InvalidSettingsException if path is null
    if (path == null) {
      throw new InvalidSettingsException("Unespecified script");
    }

    // throws InvalidSettingsException if path is whitespace
    String trimmedPath = Strings.emptyToNull(path.trim());
    if (trimmedPath == null) {
      throw new InvalidSettingsException("Unespecified model script");
    }

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

  private static class Assignment {

    enum Type {
      /** R command with the = assignment operator. E.g. x = value */
      equals,
      /** R command with the <- assignment operator. E.g. x <- value */
      left,
      /** R command with the <<- scoping assignment operator. E.g. x <<- value */
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
