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

import com.gmail.gcolaianni5.jris.bean.Record;

public class ModelEquation {

	/** Model equation name. */
	public String equationName = "";

	/** Model equation class. */
	public String equationClass = "";

	/** Model equation references (RIS). */
	public final List<Record> equationReference = new ArrayList<>();

	/** Model equation or script. */
	public String equation = "";

	@Override
	public int hashCode() {
		return Objects.hash(equation, equationClass, equationName, equationReference);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		ModelEquation other = (ModelEquation) obj;
		return Objects.equals(equation, other.equation) && Objects.equals(equationClass, other.equationClass)
				&& Objects.equals(equationName, other.equationName)
				&& Objects.equals(equationReference, other.equationReference);
	}

}
