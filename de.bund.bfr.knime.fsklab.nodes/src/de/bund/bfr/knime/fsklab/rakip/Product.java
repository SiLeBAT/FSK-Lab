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
}
