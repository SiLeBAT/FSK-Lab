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

import java.util.concurrent.Callable;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.foodprocess.ui.FoodProcessUi;
import de.bund.bfr.knime.pcml.port.PCMLPortObjectSpec;
import de.bund.bfr.knime.pmm.estimatedmodelreader.EmReaderUi;

public class MySettingsRefreshCaller implements Callable<Void> {
	
	private FoodProcessUi fpui;
	private PortObjectSpec[] specs;
	private EmReaderUi estmodelui;
	
	public MySettingsRefreshCaller(FoodProcessUi fpui, final PortObjectSpec[] specs, EmReaderUi estmodelui) {
		this.fpui = fpui;
		this.specs = specs;
		this.estmodelui = estmodelui;
	}
	  @Override
	  public Void call() {
			try {
				PCMLPortObjectSpec instantMix = FoodProcessNodeModel.calculateInstantMixture(specs, fpui.getSettings(), null);
				if (instantMix != null && instantMix.getAgents() != null && !instantMix.getAgents().isEmpty()) {
					estmodelui.refreshParamSettings(instantMix.getTemperature(), instantMix.getPH_value(), instantMix.getAw_value());
				}
			}
			catch (InvalidSettingsException e) {
				e.printStackTrace();
			}
		  return null;
	  }
} 