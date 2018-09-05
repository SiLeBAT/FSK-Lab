package de.bund.bfr.knime.pmm.dbdelete;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * Node settings helper class for the "DatabaseDelete" node.
 * 
 * @author Miguel de Alba
 */
class DatabaseDeleteNodeSettings {

	// Configuration keys
	private static final String CFG_DELETE_TEST_CONDITIONS = "deleteTestConditions";
	private static final String CFG_DELETE_PRIMARY_MODELS = "deletePrimaryModels";
	private static final String CFG_DELETE_SECONDARY_MODELS = "deleteSecondaryModels";

	// Member variables
	public boolean deleteTestConditions;
	public boolean deletePrimaryModels;
	public boolean deleteSecondaryModels;

	/**
	 * Load settings.
	 * <p>
	 * If {@link #CFG_DELETE_PRIMARY_MODELS} or {@link #CFG_DELETE_SECONDARY_MODELS}
	 * are missing assign false.
	 * 
	 * @throws InvalidSettingsException
	 *             if {@link #CFG_DELETE_TEST_CONDITIONS} is missing.
	 */
	public void load(final NodeSettingsRO settings) throws InvalidSettingsException {
		deleteTestConditions = settings.getBoolean(CFG_DELETE_TEST_CONDITIONS);
		deletePrimaryModels = settings.getBoolean(CFG_DELETE_PRIMARY_MODELS, false);
		deleteSecondaryModels = settings.getBoolean(CFG_DELETE_SECONDARY_MODELS, false);
	}

	public void save(final NodeSettingsWO settings) {
		settings.addBoolean(CFG_DELETE_TEST_CONDITIONS, deleteTestConditions);
		settings.addBoolean(CFG_DELETE_PRIMARY_MODELS, deletePrimaryModels);
		settings.addBoolean(CFG_DELETE_SECONDARY_MODELS, deleteSecondaryModels);
	}
}
