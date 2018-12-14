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
package de.bund.bfr.knime.pmm.extendedtable.items;

import org.jdom2.Element;

public interface LiteratureItemI {

	public String getElementName();
	
	public Element toXmlElement();
	public String toString();
	
	public Integer getId();
	public void setId(Integer id);
	
	public String getAuthor();
	public void setAuthor(String author);
	
	public String getTitle();
	public void setTitle(String title);
	
	public String getAbstractText();
	public void setAbstractText(String abstractText);
	
	public Integer getYear();
	public void setYear(Integer year);
	
	public String getJournal();
	public void setJournal(String journal);
	
	public String getVolume();
	public void setVolume(String volume);
	
	public String getIssue();
	public void setIssue(String issue);
	
	public Integer getPage();
	public void setPage(Integer page);
	
	public Integer getApprovalMode();
	public void setApprovalMode(Integer approvalMode);
	
	public String getWebsite();
	public void setWebsite(String website);
	
	public Integer getType();
	public void setType(Integer type);
	
	public String getComment();
	public void setComment(String comment);
	
	public String getDbuuid();
	public void setDbuuid(String dbuuid);
}