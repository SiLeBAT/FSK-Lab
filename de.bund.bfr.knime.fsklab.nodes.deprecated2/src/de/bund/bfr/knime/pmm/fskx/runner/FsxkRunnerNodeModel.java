package de.bund.bfr.knime.pmm.fskx.runner;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
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
import org.knime.ext.r.node.local.port.RPortObject;
import org.knime.ext.r.node.local.port.RPortObjectSpec;
import org.rosuda.REngine.REXPMismatchException;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.RunnerNodeInternalSettings;
import de.bund.bfr.knime.fsklab.nodes.controller.ConsoleLikeRExecutor;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.nodes.port.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.port.FskPortObjectSpec;
import de.bund.bfr.knime.pmm.fskx.FskMetaDataTuple;

public class FsxkRunnerNodeModel extends ExtToolOutputNodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

  /** Output spec for an R object. */
  private static final RPortObjectSpec R_SPEC = RPortObjectSpec.INSTANCE;

  /** Output spec for a PNG image. */
  private static final ImagePortObjectSpec PNG_SPEC = new ImagePortObjectSpec(PNGImageContent.TYPE);

  private static final PortType[] inPortTypes =
      new PortType[] {FskPortObject.TYPE, BufferedDataTable.TYPE_OPTIONAL};
  private static final PortType[] outPortTypes =
      new PortType[] {FskPortObject.TYPE, RPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL};

  private final RunnerNodeInternalSettings internalSettings = new RunnerNodeInternalSettings();

  public FsxkRunnerNodeModel() {
    super(inPortTypes, outPortTypes);
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
    // no settings
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // no settings
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    // no settings
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FSK_SPEC, R_SPEC, PNG_SPEC};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    exec.checkCanceled();
    FskPortObject fskObj = (FskPortObject) inObjects[0];

    if (!fskObj.template.independentVariables.isEmpty()) {
      fskObj.param = fskObj.template.independentVariables.stream()
          .map(v -> v.name + " <- " + v.value).collect(Collectors.joining("\n"));
    }

    // If a metadata table is connected then update the model metadata
    else if (inObjects.length == 2 && inObjects[1] != null) {
      BufferedDataTable metadataTable = (BufferedDataTable) inObjects[1];
      if (metadataTable.size() == 1) {
        try (CloseableRowIterator iterator = metadataTable.iterator()) {
          DataRow dataRow = iterator.next();
          iterator.close();

          // Gets independent variables and their values
          StringCell varCell =
              (StringCell) dataRow.getCell(FskMetaDataTuple.Key.indepvars.ordinal());
          String[] vars = varCell.getStringValue().split("\\|\\|");

          StringCell valuesCell =
              (StringCell) dataRow.getCell(FskMetaDataTuple.Key.indepvars_values.ordinal());
          String[] values = valuesCell.getStringValue().split("\\|\\|");

          if (vars != null && values != null && vars.length == values.length) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < vars.length; i++) {
              sb.append(vars[i] + " <- " + values[i] + "\n");
            }
            fskObj.param = sb.toString();
          }
        }
      }
    }

    try (RController controller = new RController()) {
      fskObj = runSnippet(controller, fskObj, exec, internalSettings.imageFile);
    }
    RPortObject rObj = new RPortObject(fskObj.workspace);

    try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
      final PNGImageContent content = new PNGImageContent(fis);
      internalSettings.plot = content.getImage();
      ImagePortObject imgObj = new ImagePortObject(content, PNG_SPEC);
      return new PortObject[] {fskObj, rObj, imgObj};
    } catch (IOException e) {
      LOGGER.warn("There is no image created");
      return new PortObject[] {fskObj, rObj};
    }
  }

  private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj,
      final ExecutionMonitor monitor, final File imageFile) throws RException,
      CanceledExecutionException, InterruptedException, IOException, REXPMismatchException {

    ConsoleLikeRExecutor executor = new ConsoleLikeRExecutor(controller);

    monitor.setMessage("Setting up output capturing");
    executor.setupOutputCapturing(monitor);

    monitor.setMessage("Add paths to libraries");
    controller.addPackagePath(LibRegistry.instance().getInstallationPath());

    monitor.setMessage("Run parameters script");
    executor.executeIgnoreResult(fskObj.param, monitor);

    monitor.setMessage("Run model script");
    executor.executeIgnoreResult(fskObj.model, monitor);

    monitor.setMessage("Run visualization script");
    try {
      NodeUtils.plot(imageFile, fskObj.viz, 640, 640, 12, "NA", executor, monitor);
    } catch (final RException exception) {
      LOGGER.warn("Visualization script failed", exception);
    }

    monitor.setMessage("Restore library paths");
    controller.restorePackagePath();

    monitor.setMessage("Collecting captured output");
    executor.finishOutputCapturing(monitor);

    // Save workspace
    if (fskObj.workspace == null) {
      fskObj.workspace = FileUtil.createTempFile("workspace", ".R");
    }
    controller.saveWorkspace(fskObj.workspace.toPath(), monitor);

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
    monitor.setMessage("Cleaning up");
    executor.cleanup(monitor);

    return fskObj;
  }

  private static final LinkedList<String> getLinkedListFromOutput(final String output) {
    return Arrays.stream(output.split("\\r?\\n")).collect(Collectors.toCollection(LinkedList::new));
  }

  Image getResultImage() {
    return internalSettings.plot;
  }
}
