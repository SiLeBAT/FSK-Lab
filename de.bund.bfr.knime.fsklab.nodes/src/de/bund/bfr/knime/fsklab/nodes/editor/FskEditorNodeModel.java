package de.bund.bfr.knime.fsklab.nodes.editor;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import com.google.common.base.Objects;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;

public class FskEditorNodeModel extends NoInternalsModel {

	private final FskEditorNodeSettings settings;

	public FskEditorNodeModel() {
		super(new PortType[] { FskPortObject.TYPE_OPTIONAL }, // input port
				new PortType[] { FskPortObject.TYPE }); // output port
		settings = new FskEditorNodeSettings();
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
		// do nothing
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
		FskPortObject outObj;

		// If there is an input model
		if (inObjects.length > 0 && inObjects[0] != null) {
			FskPortObject inObj = (FskPortObject) inObjects[0];

			// if input mode has not changed (the original script stored in
			// settings match the input model)
			if (Objects.equal(settings.originalModelScript.getStringValue(), inObj.model)
					&& Objects.equal(settings.originalParametersScript.getStringValue(), inObj.param)
					&& Objects.equal(settings.originalVisualizationScript.getStringValue(), inObj.viz)) {
				outObj = inObj;
				outObj.model = settings.modifiedModelScript.getStringValue();
				outObj.param = settings.modifiedParametersScript.getStringValue();
				outObj.viz = settings.modifiedVisualizationScript.getStringValue();
			} else {
				settings.originalModelScript.setStringValue(inObj.model);
				settings.originalParametersScript.setStringValue(inObj.param);
				settings.originalVisualizationScript.setStringValue(inObj.viz);
				
				settings.modifiedModelScript.setStringValue(inObj.model);
				settings.modifiedParametersScript.setStringValue(inObj.param);
				settings.modifiedVisualizationScript.setStringValue(inObj.viz);
				
				outObj = inObj;
			}
		}
		// If there is no input model then it will return the model created in
		// the UI
		else {
			outObj = new FskPortObject();
			outObj.model = settings.modifiedModelScript.getStringValue();
			outObj.param = settings.modifiedParametersScript.getStringValue();
			outObj.viz = settings.modifiedVisualizationScript.getStringValue();
		}

		return new PortObject[] { outObj };
	}
}
