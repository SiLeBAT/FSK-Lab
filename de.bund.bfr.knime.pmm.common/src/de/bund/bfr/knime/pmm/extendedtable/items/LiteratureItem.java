/*******************************************************************************
 * Copyright (c) 2018 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.pmm.extendedtable.items;

import org.jdom2.Element;

public abstract class LiteratureItem {

	public abstract String getElementName();
	
	public abstract Element toXmlElement();
	public abstract String toString();
	
	public Integer id;
	public String author;
	public String title;
	public String abstractText;
	public Integer year;
	public String journal;
	public String volume;
	public String issue;
	public Integer page;
	public Integer approvalMode;
	public String website;
	public Integer type;
	public String comment;
	public String dbuuid;
}