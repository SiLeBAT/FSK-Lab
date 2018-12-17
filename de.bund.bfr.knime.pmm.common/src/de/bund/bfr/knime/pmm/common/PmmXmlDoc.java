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
package de.bund.bfr.knime.pmm.common;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.DOMOutputter;
import org.jdom2.output.XMLOutputter;

public class PmmXmlDoc {

	private static final String ELEMENT_PMMDOC = "PmmDoc";

	private List<PmmXmlElementConvertable> elementSet;
	private String warning = "";

	public PmmXmlDoc() {
		elementSet = new ArrayList<>();
	}

	public PmmXmlDoc(PmmXmlElementConvertable rootElement) {
		elementSet = new ArrayList<>();
		elementSet.add(rootElement);
	}

	public PmmXmlDoc(String xmlString) throws IOException, JDOMException {
		this();
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(xmlString));

		Element rootElement = doc.getRootElement();
		parseElement(rootElement);
	}

	private void parseElement(Element rootElement) {
		for (Element el : rootElement.getChildren()) {
			/*
			 * if (el instanceof PmmXmlElementConvertable) { elementSet.add(new
			 * ParametricModel(el)); }
			 */
			switch (el.getName()) {
			case ParametricModel.ELEMENT_PARAMETRICMODEL:
				elementSet.add(new ParametricModel(el));
				break;
			case MiscXml.ELEMENT_MISC:
				elementSet.add(new MiscXml(el));
				break;
			case ParamXml.ELEMENT_PARAM:
				elementSet.add(new ParamXml(el));
				break;
			case IndepXml.ELEMENT_INDEP:
				elementSet.add(new IndepXml(el));
				break;
			case DepXml.ELEMENT_DEPENDENT:
				elementSet.add(new DepXml(el));
				break;
			case TimeSeriesXml.ELEMENT_TIMESERIES:
				elementSet.add(new TimeSeriesXml(el));
				break;
			case MdInfoXml.ELEMENT_MDINFO:
				elementSet.add(new MdInfoXml(el));
				break;
			case LiteratureItem.ELEMENT_LITERATURE:
				elementSet.add(new LiteratureItem(el));
				break;
			case "MLiteratureItem":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem(el));
				break;
			case "MDLiteratureItem":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem(el));
				break;
			case "EstimatedModelLiterature":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.LiteratureItem(el));
				break;
			case CatalogModelXml.ELEMENT_CATALOGMODEL:
				elementSet.add(new CatalogModelXml(el));
				break;
			case EstModelXml.ELEMENT_ESTMODEL:
				elementSet.add(new EstModelXml(el));
				break;
			case AgentXml.ELEMENT_AGENT:
				elementSet.add(new AgentXml(el));
				break;
			case "mdAgent":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.AgentXml(el));
				break;
			case "model1Agent":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.AgentXml(el));
				break;
			case "model2Agent":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.AgentXml(el));
				break;
			case MatrixXml.ELEMENT_MATRIX:
				elementSet.add(new MatrixXml(el));
				break;
			case "mdMatrix":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml(el));
				break;
			case "model1Matrix":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml(el));
				break;
			case "model2Matrix":
				elementSet.add(new de.bund.bfr.knime.pmm.extendedtable.items.MatrixXml(el));
				break;
			case PmmTimeSeries.ELEMENT_TIMESERIES:
				elementSet.add(new PmmTimeSeries(el));
				break;
			}
		}
	}

	public void addWarning(String warning) {
		this.warning += warning;
	}

	public String getWarning() {
		return warning;
	}

	public void add(PmmXmlElementConvertable el) {
		elementSet.add(el);
	}

	public void remove(PmmXmlElementConvertable el) {
		elementSet.remove(el);
	}

	public org.w3c.dom.Document getW3C() {
		try {
			Document doc = toXmlDocument();
			return new DOMOutputter().output(doc);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Document toXmlDocument() {
		Document doc = new Document();
		Element rootElement = new Element(ELEMENT_PMMDOC);
		doc.setRootElement(rootElement);

		for (PmmXmlElementConvertable element : elementSet) {
			rootElement.addContent(element.toXmlElement());
		}
		return doc;
	}

	public String toXmlString() {
		Document doc = toXmlDocument();
		XMLOutputter xmlo = new XMLOutputter();
		return xmlo.outputString(doc);
	}

	public int size() {
		return elementSet.size();
	}

	public PmmXmlElementConvertable get(int i) {
		if (elementSet.size() > 0)
			return elementSet.get(i);
		else
			return null;
	}

	public void set(int i, PmmXmlElementConvertable el) {
		elementSet.set(i, el);
	}

	public List<PmmXmlElementConvertable> getElementSet() {
		return elementSet;
	}

	public PmmXmlDoc clonePMs() {
		PmmXmlDoc doc = new PmmXmlDoc();
		for (PmmXmlElementConvertable el : this.getElementSet()) {
			if (el instanceof ParametricModel) {
				ParametricModel model = (ParametricModel) el;
				doc.add(model.clone());
			} else
				doc.add(el);
		}
		return doc;
	}

}
