package de.bund.bfr.knime.pmm.fskx.runner;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Collectors;
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
import org.knime.core.node.NodeModel;
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
import de.bund.bfr.knime.fsklab.nodes.RunnerNodeInternalSettings;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.controller.RController;
import de.bund.bfr.knime.fsklab.nodes.port.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.port.FskPortObjectSpec;
import de.bund.bfr.knime.pmm.fskx.FskMetaDataTuple;

public class FsxkRunnerNodeModel extends NodeModel {

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
      fskObj = runSnippet(controller, (FskPortObject) inObjects[0]);
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

  private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj)
      throws IOException, RException, REXPMismatchException {

    // Add path
    LibRegistry libRegistry = LibRegistry.instance();
    String cmd = ".libPaths(c(\"" + libRegistry.getInstallationPath().toString().replace("\\", "/")
        + "\", .libPaths()))";
    String[] newPaths = controller.eval(cmd, true).asStrings();

    // Run model
    controller.eval(fskObj.param + "\n" + fskObj.model, false);

    // Save workspace
    if (fskObj.workspace == null) {
      fskObj.workspace = FileUtil.createTempFile("workspace", ".R");
    }
    controller.eval("save.image('" + fskObj.workspace.getAbsolutePath().replace("\\", "/") + "')",
        false);

    // Creates chart into m_imageFile
    try {
      controller.eval("png(\"" + internalSettings.imageFile.getAbsolutePath().replace("\\", "/")
          + "\", width=640, height=640, pointsize=12, bg=\"#ffffff\", res=\"NA\")", false);
      controller.eval(fskObj.viz + "\n", false);
      controller.eval("dev.off()", false);
    } catch (RException e) {
      LOGGER.warn("Visualization script failed");
    }

    // Restore .libPaths() to the original library path which happens to be
    // in the last position
    controller.eval(".libPaths()[" + newPaths.length + "]", false);

    return fskObj;
  }

  Image getResultImage() {
    return internalSettings.plot;
  }
}
