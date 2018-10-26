package de.bund.bfr.knime.pmm.editor;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.js.common.ModelList;

final public class ModelEditorViewConfig {

	private static final String MODELS = "models";

	private ModelList m_models = new ModelList();

	public ModelList getModels() {
		return m_models;
	}

	public void setModels(ModelList models) {
		m_models = models;
	}

	/** Loads config into NodeModel. */
	public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_models = new ModelList();
		m_models.loadFromNodeSettings(settings.getNodeSettings(MODELS));
	}

//	public void loadSettingsForDialog(final NodeSettingsRO settings) throws InvalidSettingsException {
//		m_models.loadFromNodeSettings(settings.getNodeSettings(MODELS));
//	}

	/** Saves current config to settings object. */
	public void saveSettings(final NodeSettingsWO settings) {
		m_models.saveToNodeSettings(settings.addNodeSettings(MODELS));
	}
}
