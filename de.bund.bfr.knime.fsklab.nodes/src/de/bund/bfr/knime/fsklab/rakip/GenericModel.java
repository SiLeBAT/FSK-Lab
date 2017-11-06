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

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class GenericModel {

	public GeneralInformation generalInformation = new GeneralInformation();
	public Scope scope = new Scope();
	public DataBackground dataBackground = new DataBackground();
	public ModelMath modelMath = new ModelMath();
	public Simulation simulation = new Simulation();

	@Override
	public int hashCode() {
		return Objects.hash(dataBackground, generalInformation, modelMath, scope, simulation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		GenericModel other = (GenericModel) obj;
		return Objects.equals(dataBackground, other.dataBackground)
				&& Objects.equals(generalInformation, other.generalInformation)
				&& Objects.equals(modelMath, other.modelMath) && Objects.equals(scope, other.scope)
				&& Objects.equals(simulation, other.simulation);
	}
}
