/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.common.chart;

public interface ChartConstants {

	public static final String STATUS = "Status";

	public static final String OK = "Ok";
	public static final String FAILED = "Failed";
	public static final String OUT_OF_LIMITS = "Coeff Out Of Limit";
	public static final String NO_COVARIANCE = "No Covariance Matrix";
	public static final String NOT_SIGNIFICANT = "Coeff Not Significant";

	public static final String NO_TRANSFORM = "";
	public static final String SQRT_TRANSFORM = "sqrt";
	public static final String LOG_TRANSFORM = "ln";
	public static final String LOG10_TRANSFORM = "log10";
	public static final String EXP_TRANSFORM = "exp";
	public static final String EXP10_TRANSFORM = "10^";
	public static final String DIVX_TRANSFORM = "1/x";
	public static final String DIVX2_TRANSFORM = "1/x^2";

	public static final String[] TRANSFORMS = { NO_TRANSFORM, SQRT_TRANSFORM,
			LOG_TRANSFORM, LOG10_TRANSFORM, EXP_TRANSFORM, EXP10_TRANSFORM,
			DIVX_TRANSFORM, DIVX2_TRANSFORM };

}
