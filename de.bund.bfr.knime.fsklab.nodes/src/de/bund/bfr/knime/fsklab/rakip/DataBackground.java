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

public class DataBackground {

	public Study study = new Study();
	public StudySample studySample = new StudySample();
	public DietaryAssessmentMethod dietaryAssessmentMethod = new DietaryAssessmentMethod();
	public final List<String> laboratoryAccreditation = new ArrayList<>();
	public Assay assay = new Assay();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assay == null) ? 0 : assay.hashCode());
		result = prime * result + ((dietaryAssessmentMethod == null) ? 0 : dietaryAssessmentMethod.hashCode());
		result = prime * result + ((laboratoryAccreditation == null) ? 0 : laboratoryAccreditation.hashCode());
		result = prime * result + ((study == null) ? 0 : study.hashCode());
		result = prime * result + ((studySample == null) ? 0 : studySample.hashCode());
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
		DataBackground other = (DataBackground) obj;
		if (assay == null) {
			if (other.assay != null)
				return false;
		} else if (!assay.equals(other.assay))
			return false;
		if (dietaryAssessmentMethod == null) {
			if (other.dietaryAssessmentMethod != null)
				return false;
		} else if (!dietaryAssessmentMethod.equals(other.dietaryAssessmentMethod))
			return false;
		if (laboratoryAccreditation == null) {
			if (other.laboratoryAccreditation != null)
				return false;
		} else if (!laboratoryAccreditation.equals(other.laboratoryAccreditation))
			return false;
		if (study == null) {
			if (other.study != null)
				return false;
		} else if (!study.equals(other.study))
			return false;
		if (studySample == null) {
			if (other.studySample != null)
				return false;
		} else if (!studySample.equals(other.studySample))
			return false;
		return true;
	}
}
