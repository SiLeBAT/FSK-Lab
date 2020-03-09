/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.foodprocess;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;

/**
 * The settings object for the FoodProcess node.
 */
public class FoodProcessNodeSettings {
	public static final String VERSION_1_X = "version 1.x";
	
	private static final String VERSION = "version";
	private static final String FOOD_PROCESS_SETTING = "food_process_setting";
	
	private String version;
	private FoodProcessSetting foodProcessSetting;
	
	public void setFoodProcessSetting(FoodProcessSetting foodProcessSetting) {
		this.foodProcessSetting = foodProcessSetting;
	}
	public FoodProcessSetting getFoodProcessSetting() {
		return foodProcessSetting;
	}

	public FoodProcessNodeSettings() {
		version = VERSION_1_X;
		foodProcessSetting = new FoodProcessSetting(FoodProcessNodeModel.N_PORT_IN,FoodProcessNodeModel.N_PORT_OUT);
	}

	/** Saves current parameters to settings object.
     * @param settings to save to
     */
    public void saveSettings(final NodeSettingsWO settings) {     	
        settings.addString(VERSION, version);
        foodProcessSetting.saveSettings(settings.addConfig(FOOD_PROCESS_SETTING));
    }

    /** Loads parameters in NodeModel.
     * @param settings to load from
     * @throws InvalidSettingsException if settings are inconsistent
     */
    public void loadSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {    	
        version = settings.getString(VERSION);
        foodProcessSetting.loadSettings(settings.getConfig(FOOD_PROCESS_SETTING));
    }


    /** Loads parameters in Dialog.
     * @param settings to load from
     */
    public void loadSettingsForDialog(final NodeSettingsRO settings) {    	
        try {
            version = settings.getString(VERSION, VERSION_1_X);
            foodProcessSetting.loadSettingsForDialog(settings.getConfig(FOOD_PROCESS_SETTING));
        } catch (InvalidSettingsException e) {
        	// Rethrow using a runtime exception
            throw new IllegalStateException(e);
        }
    }

}
