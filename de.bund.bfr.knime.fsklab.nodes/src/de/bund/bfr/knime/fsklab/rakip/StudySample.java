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

public class StudySample {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collectionProtocol == null) ? 0 : collectionProtocol.hashCode());
		result = prime * result + ((fatPercentage == null) ? 0 : fatPercentage.hashCode());
		result = prime * result + ((lotSizeUnit == null) ? 0 : lotSizeUnit.hashCode());
		result = prime * result + ((moisturePercentage == null) ? 0 : moisturePercentage.hashCode());
		result = prime * result + ((sample == null) ? 0 : sample.hashCode());
		result = prime * result + ((samplingMethod == null) ? 0 : samplingMethod.hashCode());
		result = prime * result + ((samplingPlan == null) ? 0 : samplingPlan.hashCode());
		result = prime * result + ((samplingPoint == null) ? 0 : samplingPoint.hashCode());
		result = prime * result + ((samplingProgramType == null) ? 0 : samplingProgramType.hashCode());
		result = prime * result + ((samplingSize == null) ? 0 : samplingSize.hashCode());
		result = prime * result + ((samplingStrategy == null) ? 0 : samplingStrategy.hashCode());
		result = prime * result + ((samplingWeight == null) ? 0 : samplingWeight.hashCode());
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
		StudySample other = (StudySample) obj;
		if (collectionProtocol == null) {
			if (other.collectionProtocol != null)
				return false;
		} else if (!collectionProtocol.equals(other.collectionProtocol))
			return false;
		if (fatPercentage == null) {
			if (other.fatPercentage != null)
				return false;
		} else if (!fatPercentage.equals(other.fatPercentage))
			return false;
		if (lotSizeUnit == null) {
			if (other.lotSizeUnit != null)
				return false;
		} else if (!lotSizeUnit.equals(other.lotSizeUnit))
			return false;
		if (moisturePercentage == null) {
			if (other.moisturePercentage != null)
				return false;
		} else if (!moisturePercentage.equals(other.moisturePercentage))
			return false;
		if (sample == null) {
			if (other.sample != null)
				return false;
		} else if (!sample.equals(other.sample))
			return false;
		if (samplingMethod == null) {
			if (other.samplingMethod != null)
				return false;
		} else if (!samplingMethod.equals(other.samplingMethod))
			return false;
		if (samplingPlan == null) {
			if (other.samplingPlan != null)
				return false;
		} else if (!samplingPlan.equals(other.samplingPlan))
			return false;
		if (samplingPoint == null) {
			if (other.samplingPoint != null)
				return false;
		} else if (!samplingPoint.equals(other.samplingPoint))
			return false;
		if (samplingProgramType == null) {
			if (other.samplingProgramType != null)
				return false;
		} else if (!samplingProgramType.equals(other.samplingProgramType))
			return false;
		if (samplingSize == null) {
			if (other.samplingSize != null)
				return false;
		} else if (!samplingSize.equals(other.samplingSize))
			return false;
		if (samplingStrategy == null) {
			if (other.samplingStrategy != null)
				return false;
		} else if (!samplingStrategy.equals(other.samplingStrategy))
			return false;
		if (samplingWeight == null) {
			if (other.samplingWeight != null)
				return false;
		} else if (!samplingWeight.equals(other.samplingWeight))
			return false;
		return true;
	}

	public String sample = "";
	public Double moisturePercentage = 0.0;
	public Double fatPercentage = 0.0;
	public String collectionProtocol = "";
	public String samplingStrategy = "";
	public String samplingProgramType = "";
	public String samplingMethod = "";
	public String samplingPlan = "";
	public String samplingWeight = "";
	public String samplingSize = "";
	public String lotSizeUnit = "";
	public String samplingPoint = "";
}
