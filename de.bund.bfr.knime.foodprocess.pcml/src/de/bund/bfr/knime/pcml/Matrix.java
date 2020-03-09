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
 * A matrix is defined by either a string or a string with the corresponding 
 * id in a matrix database.
 * 
 * @author Heiko Hofer
 */
public class Matrix {
	private final String name;
	private final int id;
	private double percentage;
	
	/**
	 * Creates a new matrix.
	 * @param name the matrix 
	 */
	public Matrix(final String name) {
		this(name, -1, 1.0);
	}
	
	/**
	 * Creates a new matrix.
	 * @param name the matrix
	 * @param id the id of the matrix in the database use -1 if matrix does
	 * not have an id. 
	 * @param percentage the percentage of this matrix from the mixture of 
	 * matrices.
	 */
	public Matrix(final String name, final int id, final double percentage) {
		this.name = name;
		this.id = id;
		this.percentage = percentage;
	}
	
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public double getPercentage() {
		return percentage;
	}
}
