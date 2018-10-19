/*******************************************************************************
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
 *******************************************************************************/
package de.bund.bfr.knime.pmm.pmfreader.fsk;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.JFileChooser;

import org.jdom2.Element;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.common.reader.ReaderUtils;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.file.PMFMetadataNode;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * <code>NodeFactory</code> for the {@link de.bund.bfr.knime.pmm.pmfreader.fsk} node
 * 
 * Author: Miguel de Alba Aparicio
 */
public class NodeFactory extends org.knime.core.node.NodeFactory<NodeModel> {

  private SettingsModelString modelString = new SettingsModelString("filename", "");

  /**
   * {@inheritDoc}
   */
  @Override
  public NodeModel createNodeModel() {
    return new NodeModel(0, 2) {

      @Override
      protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
        modelString.validateSettings(settings);
      }

      @Override
      protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
          throws InvalidSettingsException {
        modelString.loadSettingsFrom(settings);
      }

      @Override
      protected void saveSettingsTo(NodeSettingsWO settings) {
        modelString.saveSettingsTo(settings);
      }

      @Override
      protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
          throws IOException, CanceledExecutionException {}

      @Override
      protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
          throws IOException, CanceledExecutionException {}

      @Override
      protected void reset() {}

      @Override
      protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
        return new DataTableSpec[] {null, null};
      }

      @Override
      protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec)
          throws Exception {
        String filepath = modelString.getStringValue();

        if (filepath.endsWith(".pmfx")) {
          // Open archive
          File file = KnimeUtils.getFile(filepath);
          CombineArchive ca = new CombineArchive(file, true);

          // Get model type
          ModelType type = getModelTypeFromArchive(ca);
          ca.close(); // Close archive

          BufferedDataContainer[] containers = ReaderUtils.readPMF(file, true, exec, type);
          return new BufferedDataTable[] {containers[0].getTable(), containers[1].getTable()};
        }

        else if (filepath.endsWith(".fskx")) {
          // Open archive
          File file = KnimeUtils.getFile(filepath);
          CombineArchive ca = new CombineArchive(file, true);

          // TODO: import URIS from FSK-Lab
          URI rURI = new URI("http://purl.org/NET/mediatypes/application/r");
          if (ca.hasEntriesWithFormat(rURI)) {
            ca.close();
            throw new InvalidSettingsException("Passed archive contains FSK model, not PMF ones.");
          } else {
            // Get model type
            ModelType type = getModelTypeFromArchive(ca);
            ca.close();

            BufferedDataContainer[] containers = ReaderUtils.readPMF(file, true, exec, type);
            return new BufferedDataTable[] {containers[0].getTable(), containers[1].getTable()};
          }
        }

        else {
          throw new InvalidSettingsException("Not a valid file: " + filepath);
        }
      }

      /**
       * @param archive Opened CombineArchive. This archive is not closed.
       * @return model type of the models in the archive
       */
      private ModelType getModelTypeFromArchive(CombineArchive archive) {
        MetaDataObject mdo = archive.getDescriptions().get(0);
        Element metaParent = mdo.getXmlDescription();
        return (new PMFMetadataNode(metaParent)).getModelType();
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getNrNodeViews() {
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NodeView<NodeModel> createNodeView(final int viewIndex, final NodeModel nodeModel) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasDialog() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NodeDialogPane createNodeDialogPane() {
    DialogComponentFileChooser fileChooser = new DialogComponentFileChooser(modelString,
        "filename-history", JFileChooser.OPEN_DIALOG, ".pmfx", ".fskx");

    // Add widgets
    DefaultNodeSettingsPane pane = new DefaultNodeSettingsPane();
    pane.addDialogComponent(fileChooser);

    return pane;
  }
}

