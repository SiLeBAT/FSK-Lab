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

public class InPortDef {
	
	private ParametersDef parametersDef;
	
	//private InPortDef[] inPortDef;

	public ParametersDef getParametersDef() {
		return parametersDef;
	}

	public InPortDef(final InPortDef[] inPortDef) {		
		//this.inPortDef = inPortDef;
		
		parametersDef = new ParametersDef();
		
	}
	
	public InPortSetting getSetting() {		
		InPortSetting ips;

		ips = new InPortSetting();
						
		ips.setParametersSetting(parametersDef.getSetting());
		
		return ips;
	}
	
	public void setSetting( InPortSetting ips ) {		
		parametersDef.setSetting(ips.getParametersSetting());
	}
}
