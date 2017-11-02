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

	@Override
	public int hashCode() {
		return Objects.hash(classification, dataType, description, distribution, error, id, modelApplicability, name,
				reference, source, subject, unit, unitCategory, value, variabilitySubject);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Parameter other = (Parameter) obj;
		return Objects.equals(id, other.id) && Objects.equals(classification, other.classification)
				&& Objects.equals(name, other.name) && Objects.equals(description, other.description)
				&& Objects.equals(unit, other.unit) && Objects.equals(unitCategory, other.unitCategory)
				&& Objects.equals(dataType, other.dataType) && Objects.equals(source, other.source)
				&& Objects.equals(subject, other.subject) && Objects.equals(distribution, other.distribution)
				&& Objects.equals(value, other.value) && Objects.equals(reference, other.reference)
				&& Objects.equals(variabilitySubject, other.variabilitySubject)
				&& Objects.equals(modelApplicability, other.modelApplicability) && Objects.equals(error, other.error);
	}

}
