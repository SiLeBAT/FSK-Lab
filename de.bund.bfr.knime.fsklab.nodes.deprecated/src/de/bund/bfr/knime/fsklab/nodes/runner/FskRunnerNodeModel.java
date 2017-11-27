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
package de.bund.bfr.knime.fsklab.nodes.runner;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.knime.base.node.util.exttool.ExtToolOutputNodeModel;
import org.knime.core.data.DataRow;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.BufferedDataTable;
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
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.nodes.FskMetaDataFields;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.RunnerNodeInternalSettings;
import de.bund.bfr.knime.fsklab.nodes.RunnerNodeSettings;
import de.bund.bfr.knime.fsklab.nodes.Variable;
import de.bund.bfr.knime.fsklab.nodes.controller.ConsoleLikeRExecutor;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;

@Deprecated
class FskRunnerNodeModel extends ExtToolOutputNodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

  /** Output spec for a PNG image. */
  private static final ImagePortObjectSpec PNG_SPEC = new ImagePortObjectSpec(PNGImageContent.TYPE);

  private final RunnerNodeInternalSettings internalSettings = new RunnerNodeInternalSettings();

  private RunnerNodeSettings settings = new RunnerNodeSettings();

  // Input and output port types
  private static final PortType[] IN_TYPES = {FskPortObject.TYPE, BufferedDataTable.TYPE_OPTIONAL};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL};

  public FskRunnerNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  // --- internal settings methods ---

  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    internalSettings.loadInternals(nodeInternDir);
  }

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    internalSettings.saveInternals(nodeInternDir);
  }

  @Override
  protected void reset() {
    internalSettings.reset();
  }

  // --- node settings methods ---

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    this.settings.saveSettingsTo(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    this.settings.validateSettings(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    this.settings.loadValidatedSettingsFrom(settings);
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FSK_SPEC, PNG_SPEC};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    exec.checkCanceled();
    FskPortObject fskObj = (FskPortObject) inObjects[0];

    if (fskObj.template.independentVariables != null
        && !fskObj.template.independentVariables.isEmpty()) {
      String newScript = "";

      boolean onError = false;
      for (Variable v : fskObj.template.independentVariables) {
        if (StringUtils.isAnyEmpty(v.name, v.value)) {
          onError = true;
          break;
        }
        newScript += v.name + " <- " + v.value + "\n";
      }

      if (!onError) {
        fskObj.param = newScript;
      }
    }

    // If a metadata table is connected then update the model metadata
    else if (inObjects.length == 2 && inObjects[1] != null) {
      BufferedDataTable metadataTable = (BufferedDataTable) inObjects[1];
      if (metadataTable.size() == 1) {
        try (CloseableRowIterator iterator = metadataTable.iterator()) {
          DataRow dataRow = iterator.next();
          iterator.close();

          // Gets independent variables and their values
          StringCell varCell = (StringCell) dataRow.getCell(FskMetaDataFields.indepvars.ordinal());
          String[] vars = varCell.getStringValue().split("\\|\\|");

          StringCell valuesCell =
              (StringCell) dataRow.getCell(FskMetaDataFields.indepvars_values.ordinal());
          String[] values = valuesCell.getStringValue().split("\\|\\|");

          if (vars != null && values != null && vars.length == values.length) {
            boolean onError = false;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < vars.length; i++) {
              if (StringUtils.isNoneEmpty(vars[i], values[i])) {
                sb.append(vars[i] + " <- " + values[i] + "\n");
              } else {
                onError = true;
              }
            }

            if (!onError) {
              fskObj.param = sb.toString();
            }
          }
        }
      }
    }

    try (RController controller = new RController()) {
      fskObj =
          runSnippet(controller, (FskPortObject) inObjects[0], exec.createSubExecutionContext(1.0));
    }

    try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
      final PNGImageContent content = new PNGImageContent(fis);
      internalSettings.plot = content.getImage();
      ImagePortObject imgObj = new ImagePortObject(content, PNG_SPEC);
      return new PortObject[] {fskObj, imgObj};
    } catch (IOException e) {
      LOGGER.warn("There is no image created");
      return new PortObject[] {fskObj};
    }
  }

  private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj,
      final ExecutionContext exec) throws Exception {

    final ConsoleLikeRExecutor executor = new ConsoleLikeRExecutor(controller);

    NodeUtils.runSnippet(controller, executor, fskObj.model, fskObj.param, fskObj.viz, exec,
        internalSettings.imageFile, settings);

    // Save workspace
    if (fskObj.workspace == null) {
      fskObj.workspace = FileUtil.createTempFile("workspace", ".R");
    }
    controller.saveWorkspace(fskObj.workspace.toPath(), exec);

    // process the return value of error capturing and update error
    // and output views accordingly
    if (!executor.getStdErr().isEmpty()) {
      setExternalOutput(getLinkedListFromOutput(executor.getStdErr()));
    }

    if (!executor.getStdErr().isEmpty()) {
      final LinkedList<String> output = getLinkedListFromOutput(executor.getStdErr());
      setExternalErrorOutput(output);

      for (final String line : output) {
        if (line.startsWith(ConsoleLikeRExecutor.ERROR_PREFIX)) {
          throw new RException("Error in R code: \"" + line + "\"", null);
        }
      }
    }

    // cleanup temporary variables of output capturing and consoleLikeCommand stuff
    exec.setMessage("Cleaning up");
    executor.cleanup(exec);

    return fskObj;
  }

  private static final LinkedList<String> getLinkedListFromOutput(final String output) {
    return Arrays.stream(output.split("\\r?\\n")).collect(Collectors.toCollection(LinkedList::new));
  }

  Image getResultImage() {
    return internalSettings.plot;
  }
}
