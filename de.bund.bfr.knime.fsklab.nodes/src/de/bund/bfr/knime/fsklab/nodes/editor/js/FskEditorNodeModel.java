package de.bund.bfr.knime.fsklab.nodes.editor.js;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

public class FskEditorNodeModel extends AbstractWizardNodeModel<FskEditorViewRepresentation, FskEditorViewValue>
		implements PortObjectHolder {

	private FskPortObject m_port;

	public FskEditorNodeModel() {
		super(new PortType[] { FskPortObject.TYPE },  // input port
				new PortType[] { FskPortObject.TYPE },  // output port
				(new FskEditorNodeFactory().getInteractiveViewName())); // view name
	}

	@Override
	public FskEditorViewRepresentation createEmptyViewRepresentation() {
		return new FskEditorViewRepresentation();
	}

	@Override
	public FskEditorViewValue createEmptyViewValue() {
		return new FskEditorViewValue();
	}

	@Override
	public String getJavascriptObjectID() {
		return "de.bund.bfr.knime.fsklab.nodes.editor.js";
	};

	@Override
	public boolean isHideInWizard() {
		return false;
	}

	@Override
	public ValidationError validateViewValue(FskEditorViewValue viewContent) {
		return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO content) {
	}

	@Override
	public FskEditorViewValue getViewValue() {
		FskEditorViewValue val;
		synchronized (getLock()) {
			val = super.getViewValue();
			if (val == null) {
				val = createEmptyViewValue();
			}
			if (val.modelScript.isEmpty() && val.paramScript.isEmpty() && val.vizScript.isEmpty() && m_port != null) {
				val.modelScript = m_port.model;
				val.paramScript = m_port.param;
				val.vizScript = m_port.viz;
			}
		}
		return val;
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return inSpecs;
	}

	@Override
	protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		FskPortObject obj = (FskPortObject) inObjects[0];

		synchronized (getLock()) {
			FskEditorViewValue val = getViewValue();

			// If not executed
			if (val.modelScript.isEmpty() && val.paramScript.isEmpty() && val.vizScript.isEmpty()) {
				val.modelScript = obj.model;
				val.paramScript = obj.param;
				val.vizScript = obj.viz;
				m_port = obj;
			}

			// Takes modified scripts from val
			obj.model = val.modelScript;
			obj.param = val.paramScript;
			obj.viz = val.vizScript;
			m_port = obj;

		}

		exec.setProgress(1);
		return new PortObject[] { obj };
	}

	@Override
	protected void performReset() {
		m_port = null;
	}

	@Override
	protected void useCurrentValueAsDefault() {
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	public PortObject[] getInternalPortObjects() {
		return new PortObject[] { m_port };
	}

	@Override
	public void setInternalPortObjects(PortObject[] portObjects) {
		m_port = (FskPortObject) portObjects[0];
	}
}
