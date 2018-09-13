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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MDAgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MDLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MDMatrixXml;

public class TimeSeriesMetadata {

	private static final String ELEMENT_PMMDOC = "PmmDoc";

	private MDAgentXml agentXml;
	private MDMatrixXml matrixXml;
	private List<MDLiteratureItem> literatureItems;
	private String warning;

	public TimeSeriesMetadata() {
		agentXml = null;
		matrixXml = null;
		literatureItems = new ArrayList<>();
		warning = "";
	}

	public TimeSeriesMetadata(String xmlString) throws IOException, JDOMException {
		this();
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xmlString));

		Element rootElement = doc.getRootElement();
		parseElement(rootElement);
	}

	private void parseElement(Element rootElement) {

		Element agentElement = rootElement.getChild(AgentXml.ELEMENT_AGENT);
		if (agentElement != null) {
			agentXml = new MDAgentXml(agentElement);
		}

		Element matrixElement = rootElement.getChild(MatrixXml.ELEMENT_MATRIX);
		if (matrixElement != null) {
			matrixXml = new MDMatrixXml(matrixElement);
		}

		for (Element literatureElement : rootElement.getChildren(MDLiteratureItem.ELEMENT_LITERATURE)) {
			literatureItems.add(new MDLiteratureItem(literatureElement));
		}
	}

	public void addWarning(String warning) {
		this.warning += warning;
	}

	public String getWarning() {
		return warning;
	}

	public void setAgentXml(MDAgentXml agentXml) {
		this.agentXml = agentXml;
	}

	public void clearAgentXml() {
		this.agentXml = null;
	}

	public void setMatrixXml(MDMatrixXml matrixXml) {
		this.matrixXml = matrixXml;
	}

	public void clearMatrixXml() {
		this.matrixXml = null;
	}

	public void addLiteratureItem(MDLiteratureItem literatureItem) {
		literatureItems.add(literatureItem);
	}

	public void removeLiteratureItem(LiteratureItem literatureItem) {
		literatureItems.remove(literatureItem);
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

	public Document toXmlDocument() {
		Document doc = new Document();
		Element rootElement = new Element(ELEMENT_PMMDOC);
		doc.setRootElement(rootElement);

		if (agentXml != null) {
			rootElement.addContent(agentXml.toXmlElement());
		}
		if (matrixXml != null) {
			rootElement.addContent(matrixXml.toXmlElement());
		}
		for (MDLiteratureItem literatureItem : literatureItems) {
			rootElement.addContent(literatureItem.toXmlElement());
		}

		return doc;
	}
}
