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
		return Objects.hash(aic, bic, event, exposure, fittingProcedure, modelEquation, mse, parameter, rSquared, rmse,
				sse);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		ModelMath other = (ModelMath) obj;
		return Double.compare(aic, other.aic) == 0 && Double.compare(bic, other.bic) == 0
				&& Objects.equals(event, other.event) && Objects.equals(exposure, other.exposure)
				&& Objects.equals(fittingProcedure, other.fittingProcedure)
				&& Objects.equals(modelEquation, other.modelEquation) && Double.compare(mse, other.mse) == 0
				&& Objects.equals(parameter, other.parameter) && Double.compare(rSquared, other.rSquared) == 0
				&& Double.compare(rmse, other.rmse) == 0 && Double.compare(sse, other.sse) == 0;
	}
}
