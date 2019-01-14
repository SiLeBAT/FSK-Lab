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
package de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel;

import de.bund.bfr.knime.pmm.common.PmmException;

public class TimeSeriesSchema extends de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema {

	public static final String ATT_METADATA = "MD_Metadata";

	public TimeSeriesSchema() {

		try {
			addXmlAttribute(ATT_METADATA);
		} catch (PmmException ex) {
			ex.printStackTrace(System.err);
		}
	}
}
