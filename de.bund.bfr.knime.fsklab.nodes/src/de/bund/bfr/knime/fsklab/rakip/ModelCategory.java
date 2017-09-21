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

public class ModelCategory {

	/** Type of model. Ultimate goal of the global model. */
	public String modelClass = "";

	/** Model sub class. Sub-classification of the model given modelClass. */
	public final List<String> modelSubClass = new ArrayList<>();

	public String modelClassComment = "";

	public final List<String> modelSubSubClass = new ArrayList<>();

	/** Impact of the specific process in the hazard. */
	public final List<String> basicProcess = new ArrayList<>();
}
