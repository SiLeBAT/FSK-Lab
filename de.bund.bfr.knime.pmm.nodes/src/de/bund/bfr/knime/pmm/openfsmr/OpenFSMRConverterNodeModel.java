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
package de.bund.bfr.knime.pmm.openfsmr;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.file.ExperimentalDataFile;
import de.bund.bfr.pmfml.file.ManualSecondaryModelFile;
import de.bund.bfr.pmfml.file.ManualTertiaryModelFile;
import de.bund.bfr.pmfml.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmfml.file.PMFMetadataNode;
import de.bund.bfr.pmfml.file.PrimaryModelWDataFile;
import de.bund.bfr.pmfml.file.PrimaryModelWODataFile;
import de.bund.bfr.pmfml.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmfml.model.ExperimentalData;
import de.bund.bfr.pmfml.model.ManualSecondaryModel;
import de.bund.bfr.pmfml.model.ManualTertiaryModel;
import de.bund.bfr.pmfml.model.OneStepSecondaryModel;
import de.bund.bfr.pmfml.model.OneStepTertiaryModel;
import de.bund.bfr.pmfml.model.PrimaryModelWData;
import de.bund.bfr.pmfml.model.PrimaryModelWOData;
import de.bund.bfr.pmfml.model.TwoStepSecondaryModel;
import de.bund.bfr.pmfml.model.TwoStepTertiaryModel;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * This is the model implementation of OpenFSMRConverter.
 * 
 * Author: Miguel de Alba
 */
public class OpenFSMRConverterNodeModel extends NodeModel {

  // configuration keys
  public static final String CFGKEY_DIR = "directory";
  public static final String CFGKEY_FILES = "files";

  // defaults for persistent state
  public static final String DEFAULT_DIR = "c:/";
  public static final String[] DEFAULT_FILES = new String[0];

  // persistent state
  private SettingsModelString selectedDirectory = new SettingsModelString(CFGKEY_DIR, DEFAULT_DIR);
  private SettingsModelStringArray selectedFiles =
      new SettingsModelStringArray(CFGKEY_FILES, DEFAULT_FILES);

  private static final Map<ModelType, Converter> CONVERTERS = new HashMap<>();
  static {
    CONVERTERS.put(ModelType.EXPERIMENTAL_DATA, new ExperimentalDataConverter());
    CONVERTERS.put(ModelType.PRIMARY_MODEL_WDATA, new PrimaryModelWithDataConverter());
    CONVERTERS.put(ModelType.PRIMARY_MODEL_WODATA, new PrimaryModelWithoutDataConverter());
    CONVERTERS.put(ModelType.TWO_STEP_SECONDARY_MODEL, new TwoStepSecondaryModelConverter());
    CONVERTERS.put(ModelType.ONE_STEP_SECONDARY_MODEL, new OneStepSecondaryModelConverter());
    CONVERTERS.put(ModelType.MANUAL_SECONDARY_MODEL, new ManualSecondaryModelConverter());
    CONVERTERS.put(ModelType.TWO_STEP_TERTIARY_MODEL, new TwoStepTertiaryModelConverter());
    CONVERTERS.put(ModelType.ONE_STEP_TERTIARY_MODEL, new OneStepTertiaryModelConverter());
    CONVERTERS.put(ModelType.MANUAL_TERTIARY_MODEL, new ManualTertiaryModelConverter());
  }

  private static final DataTableSpec TABLE_SPEC = new OpenFSMRSchema().createSpec();

  /** Constructor for the node model. */
  protected OpenFSMRConverterNodeModel() {
    // 0 input ports and 1 output port
    super(0, 1);
  }

  /** {@inheritDoc} */
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) {

    BufferedDataContainer container = exec.createDataContainer(TABLE_SPEC);

    for (String selectedFile : selectedFiles.getStringArrayValue()) {
      // Builds full path
      String fullpath = selectedDirectory.getStringValue() + "/" + selectedFile;

      File file;
      try {
        file = KnimeUtils.getFile(fullpath);
      } catch (InvalidPathException | MalformedURLException e) {
        setWarningMessage(fullpath + " could not be accessed. Skipping file.");
        e.printStackTrace();
        continue;
      }

      ModelType modelType;

      try (CombineArchive ca = new CombineArchive(file, true)) {
        MetaDataObject mdo = ca.getDescriptions().get(0);
        Element metaParent = mdo.getXmlDescription();
        PMFMetadataNode pmfMetadataNode = new PMFMetadataNode(metaParent);
        modelType = pmfMetadataNode.getModelType();
      } catch (IOException | JDOMException | ParseException | CombineArchiveException e) {
        setWarningMessage(fullpath + " CombineArchive cannot be opened. Skipping file.");
        e.printStackTrace();
        continue;
      }

      // Gets metadata templates from file and add them to the KNIME table
      try {
        for (FSMRTemplate template : CONVERTERS.get(modelType).convert(file)) {
          KnimeTuple tuple = FSMRUtils.createTupleFromTemplate(template);
          container.addRowToTable(tuple);
        }

      } catch (Exception e) {
        setWarningMessage(fullpath + " metadata could not be retrieved. Skipping file.");
        e.printStackTrace();
        continue;
      }
    }

    container.close();
    return new BufferedDataTable[] {container.getTable()};
  }

  @Override
  protected void reset() {}

  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {TABLE_SPEC};
  }

  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    selectedDirectory.saveSettingsTo(settings);
    selectedFiles.saveSettingsTo(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    selectedDirectory.loadSettingsFrom(settings);
    selectedFiles.loadSettingsFrom(settings);
  }

  @Override
  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    selectedDirectory.validateSettings(settings);
    selectedFiles.validateSettings(settings);
  }

  @Override
  protected void loadInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  @Override
  protected void saveInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}
}


/**
 * OpenFSMR template converter.
 * 
 * Convert all the models within a PMFX file to OpenFSMR templates. PMF files are also supported.
 * 
 * @author Miguel Alba
 */
interface Converter {

  List<FSMRTemplate> convert(final File file) throws Exception;
}


class ExperimentalDataConverter implements Converter {

  /** Obtain an OpenFSMR template per data file. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<ExperimentalData> eds = ExperimentalDataFile.read(file.toPath());
    return eds.stream().map(ExperimentalData::getDoc).map(FSMRUtils::processData)
        .collect(Collectors.toList());
  }
}


class PrimaryModelWithDataConverter implements Converter {

  /** Obtain an OpenFSMR template per primary model. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<PrimaryModelWData> pms = PrimaryModelWDataFile.read(file.toPath());

    return pms.stream().map(PrimaryModelWData::getModelDoc)
        .map(FSMRUtils::processModelWithMicrobialData).collect(Collectors.toList());
  }
}


class PrimaryModelWithoutDataConverter implements Converter {

  /** Obtain an OpenFSMR template per primary model. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<PrimaryModelWOData> pms = PrimaryModelWODataFile.read(file.toPath());

    return pms.stream().map(PrimaryModelWOData::getDoc)
        .map(FSMRUtils::processModelWithMicrobialData).collect(Collectors.toList());
  }
}


class TwoStepSecondaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per secondary model. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<TwoStepSecondaryModel> sms = TwoStepSecondaryModelFile.read(file.toPath());

    return sms.stream().map(FSMRUtils::processTwoStepSecondaryModel).collect(Collectors.toList());
  }
}


class OneStepSecondaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per secondary model. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<OneStepSecondaryModel> sms = OneStepSecondaryModelFile.read(file.toPath());

    return sms.stream().map(FSMRUtils::processOneStepSecondaryModel).collect(Collectors.toList());
  }
}


class ManualSecondaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per secondary model. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<ManualSecondaryModel> sms = ManualSecondaryModelFile.read(file.toPath());

    return sms.stream().map(FSMRUtils::processManualSecondaryModel).collect(Collectors.toList());
  }
}


class TwoStepTertiaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per tertiary model. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<TwoStepTertiaryModel> tms = TwoStepTertiaryModelFile.read(file.toPath());

    return tms.stream().map(FSMRUtils::processTwoStepTertiaryModel).collect(Collectors.toList());
  }
}


class OneStepTertiaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per tertiary model. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<OneStepTertiaryModel> tms = OneStepTertiaryModelFile.read(file.toPath());

    return tms.stream().map(FSMRUtils::processOneStepTertiaryModel).collect(Collectors.toList());
  }
}


class ManualTertiaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per tertiary model. */
  public List<FSMRTemplate> convert(final File file) throws Exception {
    List<ManualTertiaryModel> tms = ManualTertiaryModelFile.read(file.toPath());

    return tms.stream().map(FSMRUtils::processManualTertiaryModel).collect(Collectors.toList());
  }
}
