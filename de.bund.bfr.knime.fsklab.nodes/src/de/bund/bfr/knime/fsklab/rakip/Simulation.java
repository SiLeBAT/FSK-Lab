/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.knime.fsklab.rakip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Simulation {

	/** Simulation algorithm. */
	public String algorithm = "";

	/** URI of the model applies for simulation/prediction. */
	public String simulatedModel = "";

	public final List<String> parameterSettings = new ArrayList<>();

	/** General description of the simulation. */
	public String description = "";

	/** Information on the parameters to be plotted. */
	public final List<String> plotSettings = new ArrayList<>();

	/** Pointer to software code (R script). */
	public String visualizationScript = "";

	@Override
	public int hashCode() {
		return Objects.hash(algorithm, description, parameterSettings, plotSettings, simulatedModel,
				visualizationScript);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Simulation other = (Simulation) obj;
		return Objects.equals(algorithm, other.algorithm) && Objects.equals(description, other.description)
				&& Objects.equals(parameterSettings, other.parameterSettings)
				&& Objects.equals(plotSettings, other.plotSettings)
				&& Objects.equals(simulatedModel, other.simulatedModel)
				&& Objects.equals(visualizationScript, other.visualizationScript);
	}
}
