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

public class Scope {

	public Product product = new Product();
	public Hazard hazard = new Hazard();
	public PopulationGroup populationGroup = new PopulationGroup();

	/** General comment on the model. */
	public String generalComment = "";

	/** Temporal information on which the model applies. */
	public String temporalInformation = "";

	/** Information on which the model applies. */
	public final List<String> region = new ArrayList<>();

	/** Countries on which the model applies. */
	public final List<String> country = new ArrayList<>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((generalComment == null) ? 0 : generalComment.hashCode());
		result = prime * result + ((hazard == null) ? 0 : hazard.hashCode());
		result = prime * result + ((populationGroup == null) ? 0 : populationGroup.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((region == null) ? 0 : region.hashCode());
		result = prime * result + ((temporalInformation == null) ? 0 : temporalInformation.hashCode());
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
		Scope other = (Scope) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (generalComment == null) {
			if (other.generalComment != null)
				return false;
		} else if (!generalComment.equals(other.generalComment))
			return false;
		if (hazard == null) {
			if (other.hazard != null)
				return false;
		} else if (!hazard.equals(other.hazard))
			return false;
		if (populationGroup == null) {
			if (other.populationGroup != null)
				return false;
		} else if (!populationGroup.equals(other.populationGroup))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		if (temporalInformation == null) {
			if (other.temporalInformation != null)
				return false;
		} else if (!temporalInformation.equals(other.temporalInformation))
			return false;
		return true;
	}
}
