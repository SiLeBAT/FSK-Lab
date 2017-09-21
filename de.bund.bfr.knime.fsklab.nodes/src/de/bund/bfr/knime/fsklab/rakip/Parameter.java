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

public class Parameter {

	public static enum Classification {
		input, output, constant
	}

	public String id = "";

	public Classification classification = Classification.constant;

	public String name = "";

	public String description = "";

	public String unit = "";

	public String unitCategory = "";

	public String dataType = "";

	public String source = "";

	public String subject = "";

	public String distribution = "";

	public String value = "";

	public String reference = "";

	public String variabilitySubject;

	public final List<String> modelApplicability = new ArrayList<>();

	public Double error = .0;
}
