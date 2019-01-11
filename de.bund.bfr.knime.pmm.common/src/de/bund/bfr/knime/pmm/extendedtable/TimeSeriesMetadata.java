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
package de.bund.bfr.knime.pmm.extendedtable;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.DOMOutputter;

import de.bund.bfr.knime.pmm.extendedtable.items.AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml;

public class TimeSeriesMetadata {

	private static final String ELEMENT_PMMDOC = "PmmDoc";

	public AgentXml agentXml;
	public MatrixXml matrixXml;
	public List<LiteratureItem> literatureItems;

	public TimeSeriesMetadata() {
		agentXml = null;
		matrixXml = null;
		literatureItems = new ArrayList<>();
	}

	public org.w3c.dom.Document getW3C() {
		try {
			Document doc = toXmlDocument();
			return new DOMOutputter().output(doc);
		} catch (JDOMException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private Document toXmlDocument() {
		Document doc = new Document();
		Element rootElement = new Element(ELEMENT_PMMDOC);
		doc.setRootElement(rootElement);

		if (agentXml != null) {
			rootElement.addContent(agentXml.toXmlElement());
		}
		if (matrixXml != null) {
			rootElement.addContent(matrixXml.toXmlElement());
		}
		literatureItems.stream().map(LiteratureItem::toXmlElement).forEach(rootElement::addContent);

		return doc;
	}
}
