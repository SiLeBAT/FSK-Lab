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

import java.util.Objects;

public class Exposure {

	public String treatment = "";
	public String contaminationLevel = "";
	public String exposureType = "";
	public String scenario = "";
	public String uncertaintyEstimation = "";

	@Override
	public int hashCode() {
		return Objects.hash(contaminationLevel, exposureType, scenario, treatment, uncertaintyEstimation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Exposure other = (Exposure) obj;
		return Objects.equals(contaminationLevel, other.contaminationLevel) &&
				Objects.equals(exposureType, other.exposureType) &&
				Objects.equals(scenario, other.scenario) &&
				Objects.equals(treatment, other.treatment) &&
				Objects.equals(uncertaintyEstimation, other.uncertaintyEstimation);
	}
}
