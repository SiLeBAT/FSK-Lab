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
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Product {

	public String environmentName = "";
	public String environmentDescription = "";
	public String environmentUnit = "";
	public final List<String> productionMethod = new ArrayList<>();
	public final List<String> packaging = new ArrayList<>();
	public final List<String> productTreatment = new ArrayList<>();
	public String originCountry = "";
	public String originArea = "";
	public String fisheriesArea = "";
	public Date productionDate = new Date();
	public Date expirationDate = new Date();

	@Override
	public int hashCode() {
		return Objects.hash(environmentDescription, environmentName, environmentUnit, expirationDate, fisheriesArea,
				originArea, originCountry, packaging, productTreatment, productionDate, productionMethod);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Product other = (Product) obj;
		return Objects.equals(environmentDescription, other.environmentDescription) &&
				Objects.equals(environmentName, other.environmentName) &&
				Objects.equals(environmentUnit, other.environmentUnit) &&
				Objects.equals(expirationDate, other.expirationDate) &&
				Objects.equals(fisheriesArea, other.fisheriesArea) &&
				Objects.equals(originArea, other.originArea) &&
				Objects.equals(originCountry, other.originCountry) &&
				Objects.equals(packaging, other.packaging) &&
				Objects.equals(productTreatment, other.productTreatment) &&
				Objects.equals(productionDate, other.productionDate) &&
				Objects.equals(productionMethod, other.productionMethod);
	}
}
