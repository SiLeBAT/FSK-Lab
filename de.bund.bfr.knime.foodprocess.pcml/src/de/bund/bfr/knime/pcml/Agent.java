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
package de.bund.bfr.knime.pcml;

/**
 * A wrapper class for a agent.
 * @author Heiko Hofer
 *
 */
public class Agent {
	private String name;
	private int id;
	private double quantity;
	
	/**
	 * Create a agent.
	 * @param name the name of the agent
	 */
	public Agent(final String name) {
		this(name, -1, 1.0);
	}
	
	/**
	 * Creates a new agent.
	 * @param name the agent
	 * @param id the id of the agent in the database use -1 if matrix does
	 * not have an id. 
	 * @param quantity the quantity of this matrix.
	 */
	public Agent(final String name, final int id, final double quantity) {
		this.name = name;
		this.id = id;
		this.quantity = quantity;
	}	
	
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public double getQuantity() {
		return quantity;
	}
}
