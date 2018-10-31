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
package de.bund.bfr.knime.pmm.common.generictablemodel;

import de.bund.bfr.knime.pmm.common.PmmException;

public class KnimeAttribute {

	public static final int TYPE_STRING = 0;
	public static final int TYPE_INT = 1;
	public static final int TYPE_DOUBLE = 2;
	public static final int TYPE_XML = 3;

	public final String name;
	public final int type;

	public KnimeAttribute(final String name, final int type)
			throws PmmException {

		if (name == null)
			throw new PmmException("Column name must not be null.");

		if (name.isEmpty())
			throw new PmmException("Column name must not be empty.");
		
		if (type < 0 || type > 3)
			throw new PmmException("Unknown column type");

		this.name = name;
		this.type = type;
	}

	public boolean isDouble() {
		return type == TYPE_DOUBLE;
	}

	public boolean isInt() {
		return type == TYPE_INT;
	}
}
