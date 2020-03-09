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
package de.bund.bfr.knime.foodprocess.lib;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

public class InPortSetting {
	
	private final String PARAM_PARAMETERS = "parameters";
	
	private ParametersSetting parametersSetting;
	
	public ParametersSetting getParametersSetting() {
		return parametersSetting;
	}
	public void setParametersSetting(ParametersSetting parametersSetting) {
		this.parametersSetting = parametersSetting;
	}

	public InPortSetting() {
		parametersSetting = new ParametersSetting();
	}
	
	public void saveSettings( Config config ) {		
		Config c = config.addConfig(PARAM_PARAMETERS);
		assert c != null;		
		assert parametersSetting != null;		
		parametersSetting.saveSettings( c );
	}
	
	public void loadSettings( Config config ) throws InvalidSettingsException {		
		Config c;
		
		parametersSetting = new ParametersSetting();
		c = config.getConfig(PARAM_PARAMETERS);
		parametersSetting.loadSettings( c );
	}
	
	public void loadSettingsForDialog( Config config ) {		
		try {
			loadSettings( config );
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
			assert false;
		}		
	}
}
