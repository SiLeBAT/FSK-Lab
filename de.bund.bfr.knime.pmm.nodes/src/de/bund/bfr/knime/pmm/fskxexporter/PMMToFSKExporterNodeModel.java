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
package de.bund.bfr.knime.pmm.fskxexporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import org.knime.base.data.xml.SvgCell;
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
import org.knime.core.node.port.image.ImagePortObjectSpec;
import de.bund.bfr.knime.fsklab.nodes.NodeUtils;
import de.bund.bfr.knime.fsklab.nodes.environment.EnvironmentManager;
import de.bund.bfr.knime.fsklab.v2_0.CombinedFskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObjectSpec;
import de.bund.bfr.knime.fsklab.v2_0.editor.FSKEditorJSNodeDialog.ModelType;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
//import de.bund.bfr.metadata.swagger.Model;
//import de.bund.bfr.metadata.swagger.PredictiveModel;

public class PMMToFSKExporterNodeModel extends NodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

  /** Output spec for an SVG image. */
  private static final ImagePortObjectSpec SVG_SPEC = new ImagePortObjectSpec(SvgCell.TYPE);

  private final PMMToFSKNodeInternalSettings internalSettings = new PMMToFSKNodeInternalSettings();

  private PMMToFSKNodeSettings nodeSettings = new PMMToFSKNodeSettings();
  private FskPortObject fskObj = null;
  // Input and output port types
  private static final PortType[] IN_TYPES = {BufferedDataTable.TYPE};
  private static final PortType[] OUT_TYPES = {FskPortObject.TYPE};

  // isTest field is only used by maven build
  public static boolean isTest = false;
  PMModelReadder pmmodelReader = new PMModelReadder();
 
  
  public PMMToFSKExporterNodeModel() {
    super(IN_TYPES, OUT_TYPES);
  }

  // --- internal settings methods ---

  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // No internal settings
  }

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // No internals settings
  }

  private void cleanGeneratedResources(FskPortObject portObject) {
    if (portObject != null && portObject.getGeneratedResourcesDirectory().isPresent()) {
      try {
        if (portObject instanceof CombinedFskPortObject) {
          cleanGeneratedResources(((CombinedFskPortObject) portObject).getFirstFskPortObject());
          cleanGeneratedResources(((CombinedFskPortObject) portObject).getSecondFskPortObject());

        }
        if (portObject.getGeneratedResourcesDirectory().get().exists()) {
          Files.walk(portObject.getGeneratedResourcesDirectory().get().toPath())
              .sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
                try {
                  file.delete();
                } catch (Exception ex) {
                  ex.printStackTrace();
                }
              });
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected void reset() {
    nodeSettings.simulation = "";
    internalSettings.reset();
    cleanGeneratedResources(fskObj);
    fskObj = null;
  }

  // --- node settings methods ---

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    nodeSettings.save(settings);
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // does not validate anything
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    nodeSettings.load(settings);
  }

  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FSK_SPEC};
  }

  @Override
  protected PortObject[] execute(PortObject[] inData, ExecutionContext exec) throws Exception {
    BufferedDataTable dataTable = (BufferedDataTable)inData[0];
    if(SchemaFactory.conformsM1Schema(dataTable.getSpec())){
      pmmodelReader.readPrimaryTable(dataTable);
    }else if(SchemaFactory.conformsM2Schema(dataTable.getSpec())){
      pmmodelReader.readSecondaryTable(dataTable);
    }else {
      //tertiary Model then join
    }
    java.util.Optional<EnvironmentManager> environmentManager = java.util.Optional.empty();;
    
    //PredictiveModel modelMetadata = (PredictiveModel) NodeUtils.initializeModel(ModelType.predictiveModel);
    //modelMetadata.getGeneralInformation().setName("thing");
    
    //final FskPortObject portObj = new FskPortObject("", "", modelMetadata, Path.of(""),
        //new ArrayList(), environmentManager, "", "");
    return new PortObject[] {null};
  }

  



}
