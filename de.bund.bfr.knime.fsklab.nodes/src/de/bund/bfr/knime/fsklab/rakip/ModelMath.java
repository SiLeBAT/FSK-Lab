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

public class ModelMath {

	public final List<Parameter> parameter = new ArrayList<>(0);

	public double sse = .0;

	public double mse = .0;

	public double rmse = .0;

	public double rSquared = .0;

	public double aic = .0;

	public double bic = .0;

	public List<ModelEquation> modelEquation = new ArrayList<>(0);

	public String fittingProcedure = "";

	public Exposure exposure = new Exposure();

	public final List<String> event = new ArrayList<>(0);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(aic);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(bic);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((exposure == null) ? 0 : exposure.hashCode());
		result = prime * result + ((fittingProcedure == null) ? 0 : fittingProcedure.hashCode());
		result = prime * result + ((modelEquation == null) ? 0 : modelEquation.hashCode());
		temp = Double.doubleToLongBits(mse);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		temp = Double.doubleToLongBits(rSquared);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rmse);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sse);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		ModelMath other = (ModelMath) obj;
		if (Double.doubleToLongBits(aic) != Double.doubleToLongBits(other.aic))
			return false;
		if (Double.doubleToLongBits(bic) != Double.doubleToLongBits(other.bic))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (exposure == null) {
			if (other.exposure != null)
				return false;
		} else if (!exposure.equals(other.exposure))
			return false;
		if (fittingProcedure == null) {
			if (other.fittingProcedure != null)
				return false;
		} else if (!fittingProcedure.equals(other.fittingProcedure))
			return false;
		if (modelEquation == null) {
			if (other.modelEquation != null)
				return false;
		} else if (!modelEquation.equals(other.modelEquation))
			return false;
		if (Double.doubleToLongBits(mse) != Double.doubleToLongBits(other.mse))
			return false;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		if (Double.doubleToLongBits(rSquared) != Double.doubleToLongBits(other.rSquared))
			return false;
		if (Double.doubleToLongBits(rmse) != Double.doubleToLongBits(other.rmse))
			return false;
		if (Double.doubleToLongBits(sse) != Double.doubleToLongBits(other.sse))
			return false;
		return true;
	}
}
