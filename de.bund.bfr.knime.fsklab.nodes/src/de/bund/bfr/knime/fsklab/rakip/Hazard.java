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

public class Hazard {

	public String hazardType = "";

	public String hazardName = "";

	public String hazardDescription = "";

	public String hazardUnit = "";

	public String adverseEffect = "";

	public String origin = "";

	public String benchmarkDose = "";

	public String maximumResidueLimit = "";

	public String noObservedAdverse = "";

	public String lowestObservedAdverse = "";

	public String acceptableOperator = "";

	public String acuteReferenceDose = "";

	public String acceptableDailyIntake = "";

	public String hazardIndSum = "";

	public String laboratoryName = "";

	public String laboratoryCountry = "";

	public String detectionLimit = "";

	public String quantificationLimit = "";

	public String leftCensoredData = "";

	public String rangeOfContamination = "";

	@Override
	public int hashCode() {
		return Objects.hash(acceptableDailyIntake, acceptableOperator, acuteReferenceDose, adverseEffect, benchmarkDose,
				detectionLimit, hazardDescription, hazardIndSum, hazardName, hazardType, hazardUnit, laboratoryCountry,
				laboratoryName, leftCensoredData, lowestObservedAdverse, maximumResidueLimit, noObservedAdverse, origin,
				quantificationLimit, rangeOfContamination);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Hazard other = (Hazard) obj;

		return Objects.equals(acceptableDailyIntake, other.acceptableDailyIntake)
				&& Objects.equals(acceptableOperator, other.acceptableOperator)
				&& Objects.equals(acuteReferenceDose, other.acuteReferenceDose)
				&& Objects.equals(adverseEffect, other.adverseEffect)
				&& Objects.equals(benchmarkDose, other.benchmarkDose)
				&& Objects.equals(detectionLimit, other.detectionLimit)
				&& Objects.equals(hazardDescription, other.hazardDescription)
				&& Objects.equals(hazardIndSum, other.hazardIndSum) && Objects.equals(hazardName, other.hazardName)
				&& Objects.equals(hazardType, other.hazardType) && Objects.equals(hazardUnit, other.hazardUnit)
				&& Objects.equals(laboratoryCountry, other.laboratoryCountry)
				&& Objects.equals(laboratoryName, other.laboratoryName)
				&& Objects.equals(leftCensoredData, other.leftCensoredData)
				&& Objects.equals(lowestObservedAdverse, other.lowestObservedAdverse)
				&& Objects.equals(maximumResidueLimit, other.maximumResidueLimit)
				&& Objects.equals(noObservedAdverse, other.noObservedAdverse) && Objects.equals(origin, other.origin)
				&& Objects.equals(quantificationLimit, other.quantificationLimit)
				&& Objects.equals(rangeOfContamination, other.rangeOfContamination);
	}

}
