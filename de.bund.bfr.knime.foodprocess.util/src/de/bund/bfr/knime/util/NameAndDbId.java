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
package de.bund.bfr.knime.util;

import java.util.Objects;

/**
 * A identifier which is either defined by a name or defined by a database ID
 * 
 * @author Heiko Hofer
 */
public class NameAndDbId {
	private final String name;
	private final int id;

	/**
	 * Creates a new instance.
	 * 
	 * @param name the name
	 */
	public NameAndDbId(final String name) {
		this(name, -1);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param name the name
	 * @param id   the id of the field in the database use -1 if field does not have
	 *             an id. matrices.
	 */
	public NameAndDbId(final String name, final int id) {
		this.name = name;
		this.id = id < 0 ? -1 : id;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NameAndDbId))
			return false;

		NameAndDbId other = (NameAndDbId) obj;
		return id == other.id && Objects.equals(name, other.name);
	}
}
