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

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class GenericModel {

	public GeneralInformation generalInformation = new GeneralInformation();
	public Scope scope = new Scope();
	public DataBackground dataBackground = new DataBackground();
	public ModelMath modelMath = new ModelMath();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataBackground == null) ? 0 : dataBackground.hashCode());
		result = prime * result + ((generalInformation == null) ? 0 : generalInformation.hashCode());
		result = prime * result + ((modelMath == null) ? 0 : modelMath.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericModel other = (GenericModel) obj;
		if (dataBackground == null) {
			if (other.dataBackground != null)
				return false;
		} else if (!dataBackground.equals(other.dataBackground))
			return false;
		if (generalInformation == null) {
			if (other.generalInformation != null)
				return false;
		} else if (!generalInformation.equals(other.generalInformation))
			return false;
		if (modelMath == null) {
			if (other.modelMath != null)
				return false;
		} else if (!modelMath.equals(other.modelMath))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}
}
