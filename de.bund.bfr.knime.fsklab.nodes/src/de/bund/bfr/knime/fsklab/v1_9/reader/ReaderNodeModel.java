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
package de.bund.bfr.knime.fsklab.v1_9.reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.apache.commons.io.IOUtils;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.workflow.NodeContext;
import org.knime.core.node.workflow.WorkflowContext;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObjectSpec;


public class ReaderNodeModel extends NoInternalsModel {

  private static final PortType[] IN_TYPES = {};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  static final String CFG_FILE = "filename";
  
  private final SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);

  
  public ReaderNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    filePath.saveSettingsTo(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    filePath.loadSettingsFrom(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    filePath.validateSettings(settings);
  }

  @Override
  protected void reset() {
    NodeContext nodeContext = NodeContext.getContext();
    WorkflowManager wfm = nodeContext.getWorkflowManager();
    WorkflowContext workflowContext = wfm.getContext();
    /*
     * find and delete only the working directory folder related to current reader node in the mean
     * that, we are not deleting folders which are representing the working directory of other
     * reader nodes which maybe exist in the same workflow
     */

    try {
      Files.walk(workflowContext.getCurrentLocation().toPath())
      .filter(path -> path.toString()
          .contains(nodeContext.getNodeContainer().getNameWithID().toString()
              .replaceAll("\\W", "").replace(" ", "") + "_" + "workingDirectory"))
      .sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
        try {
          file.delete();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    String warning = CheckUtils.checkSourceFile(filePath.getStringValue());
    if (warning != null) {
        setWarningMessage(warning);
    }
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    URL url = FileUtil.toURL(filePath.getStringValue());
    Path localPath = FileUtil.resolveToPath(url);

    FskPortObject inObject;

    if (localPath != null) {
      inObject = ReaderNodeUtil.readArchive(localPath.toFile());
    }
    // if path is an external URL the archive is downloaded to a temporary file
    else {
      File temporaryFile = FileUtil.createTempFile("model", "fskx");
      temporaryFile.delete();

      try (
          InputStream inStream =
          FileUtil.openStreamWithTimeout(new URL(filePath.getStringValue()), 10000);
          OutputStream outStream = new FileOutputStream(temporaryFile)) {
        IOUtils.copy(inStream, outStream);
      }

      inObject = ReaderNodeUtil.readArchive(temporaryFile);

      temporaryFile.delete();
    }

    return new PortObject[] {inObject};
  }

 
}
