package de.bund.bfr.knime.fsklab.nodes.editor;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NoInternalsModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

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
		FskPortObject outObj = new FskPortObject();

		// If there is an input model
		if (inObjects.length > 0 && inObjects[0] != null) {
			FskPortObject inObj = (FskPortObject) inObjects[0];

			// If the input model has changed
			if (settings.objectNumber.getIntValue() != inObj.objectNum) {
				// Discard settings and replace them with input model
				settings.objectNumber.setIntValue(inObj.objectNum);
				settings.modelScript.setStringValue(inObj.model);
				settings.paramScript.setStringValue(inObj.param);
				settings.vizScript.setStringValue(inObj.viz);
				
				// Assigns input model
				outObj = inObj;
			} else {
				// Return model from settings (numObj remains unchanged)
				outObj = inObj;
				outObj.model = settings.modelScript.getStringValue();
				outObj.param = settings.paramScript.getStringValue();
				outObj.viz = settings.vizScript.getStringValue();
			}
		}
		// If there is no input model then it will return the model created in the UI
		else {
			outObj = new FskPortObject();
			outObj.model = settings.modelScript.getStringValue();
			outObj.param = settings.paramScript.getStringValue();
			outObj.viz = settings.vizScript.getStringValue();
			outObj.objectNum = settings.objectNumber.getIntValue();
		}
		
		return new PortObject[] { outObj };
	}
}
