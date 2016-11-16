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
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import de.bund.bfr.knime.fsklab.nodes.FskTemplateSettings;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;

public class FskEditorNodeModel extends NodeModel {

	private static final PortType[] inPortTypes = new PortType[] { FskPortObject.TYPE };
	private static final PortType[] outPortTypes = new PortType[] { FskPortObject.TYPE };

	static final String OBJECT_NUMBER = "object number";
	static final String MODEL_SCRIPT = "model script";
	static final String PARAM_SCRIPT = "parameters script";
	static final String VIZ_SCRIPT = "visualization script";
	static final String META_DATA = "meta data";

	private final SettingsModelInteger objectNumber;
	private final SettingsModelString modelScript;
	private final SettingsModelString paramScript;
	private final SettingsModelString vizScript;
	private final FskTemplateSettings templateSettings;

	public FskEditorNodeModel() {
		super(inPortTypes, outPortTypes);

		// R scripts are initialized with empty strings but are soon as the node
		// its connected with an
		// input they should take the scripts in the input
		objectNumber = new SettingsModelInteger(OBJECT_NUMBER, 0);
		modelScript = new SettingsModelString(MODEL_SCRIPT, "");
		paramScript = new SettingsModelString(PARAM_SCRIPT, "");
		vizScript = new SettingsModelString(VIZ_SCRIPT, "");
		templateSettings = new FskTemplateSettings();
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
	}

	// --- node settings methods ---
	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		objectNumber.loadSettingsFrom(settings);
		modelScript.loadSettingsFrom(settings);
		paramScript.loadSettingsFrom(settings);
		vizScript.loadSettingsFrom(settings);
		templateSettings.loadFromNodeSettings(settings.getNodeSettings(FskEditorNodeModel.META_DATA));
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		objectNumber.loadSettingsFrom(settings);
		modelScript.loadSettingsFrom(settings);
		paramScript.loadSettingsFrom(settings);
		vizScript.loadSettingsFrom(settings);
		templateSettings.loadFromNodeSettings(settings.getNodeSettings(FskEditorNodeModel.META_DATA));
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		objectNumber.saveSettingsTo(settings);
		modelScript.saveSettingsTo(settings);
		paramScript.saveSettingsTo(settings);
		vizScript.saveSettingsTo(settings);
		templateSettings.saveToNodeSettings(settings.addNodeSettings(FskEditorNodeModel.META_DATA));
	}

	// --- other methods ---
	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { FskPortObjectSpec.INSTANCE };
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		FskPortObject inObj = (FskPortObject) inObjects[0];

		// Creates output FSK object
		// - Takes scripts and meta data from node settings
		// - Takes R workspace and libraries from input object (not saved yet in
		// settings)

		// If the node is new and its dialog hasn't been opened (has not
		// settings yet) then assigns the input
		if (modelScript.getStringValue().isEmpty() && paramScript.getStringValue().isEmpty()
				&& vizScript.getStringValue().isEmpty()) {
			modelScript.setStringValue(inObj.model);
			paramScript.setStringValue(inObj.param);
			vizScript.setStringValue(inObj.viz);
			templateSettings.template = inObj.template;
		}

		FskPortObject outObj = new FskPortObject(modelScript.getStringValue(), paramScript.getStringValue(),
				vizScript.getStringValue(), templateSettings.template, inObj.workspace, inObj.libs);

		return new PortObject[] { outObj };
	}
}
