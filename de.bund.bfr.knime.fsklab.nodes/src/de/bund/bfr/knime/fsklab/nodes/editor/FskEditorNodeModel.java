package de.bund.bfr.knime.fsklab.nodes.editor;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;

public class FskEditorNodeModel extends NodeModel {

	private static final PortType[] inPortTypes = new PortType[] { FskPortObject.TYPE_OPTIONAL };
	private static final PortType[] outPortTypes = new PortType[] { FskPortObject.TYPE };

	private final FskEditorNodeSettings settings;

	public FskEditorNodeModel() {
		super(inPortTypes, outPortTypes);
		settings = new FskEditorNodeSettings();
	}

	// --- internal settings methods ---
	/** {@inheritDoc} */
	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// no internal settings
	}

	/** {@inheritDoc} */
	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// no internal settings
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
		this.settings.modelScript.setStringValue("");
		this.settings.paramScript.setStringValue("");
		this.settings.vizScript.setStringValue("");
	}

	// --- node settings methods ---
	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		this.settings.loadValidatedSettingsFrom(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		this.settings.validateSettings(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		this.settings.saveSettingsTo(settings);
	}

	// --- other methods ---
	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { FskPortObjectSpec.INSTANCE };
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		// If there is no input port connected create and return an FskPortObject from settings
		if (inObjects[0] == null) {
			FskPortObject fskObj = new FskPortObject();
			fskObj.model = settings.modelScript.getStringValue();
			fskObj.param = settings.paramScript.getStringValue();
			fskObj.viz  = settings.vizScript.getStringValue();
			return new PortObject[] { fskObj };
		}
		
		// If there is an input port connected but the settings have no changes
		// yet, then returns the input model in the input port
		FskPortObject inObj = (FskPortObject) inObjects[0];
		if (settings.modelScript.getStringValue().isEmpty() &&
			settings.paramScript.getStringValue().isEmpty() &&
			settings.vizScript.getStringValue().isEmpty()) {

			settings.modelScript.setStringValue(inObj.model);
			settings.paramScript.setStringValue(inObj.param);
			settings.vizScript.setStringValue(inObj.viz);

			return new PortObject[] { inObj };	
		}

		// If there is an input port and changes in the settings then return
		// the input model after applying the changes
		inObj.model = settings.modelScript.getStringValue();
		inObj.param = settings.paramScript.getStringValue();
		inObj.viz = settings.vizScript.getStringValue();

		return new PortObject[] { inObj };
	}
}
