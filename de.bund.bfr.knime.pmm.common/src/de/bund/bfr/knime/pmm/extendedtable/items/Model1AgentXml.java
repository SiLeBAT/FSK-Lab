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

import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlHelper;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class Model1AgentXml extends AgentXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_AGENT = "model1Agent";

	/** id is given a random negative int. name, detail and dbuuid are null. */
	public Model1AgentXml() {
		this(MathUtilities.getRandomNegativeInt(), null, null, null);
	}

	/** Constructor with id, name and detail. dbuuid is null. */
	public Model1AgentXml(Integer id, String name, String detail) {
		this(id, name, detail, null);
	}

	/** Copy constructor. Take every property from agent. */
	public Model1AgentXml(AgentXml agent) {
		this(agent.id, agent.name, agent.detail, agent.dbuuid);
	}

	/** Fully parameterized constructor. */
	public Model1AgentXml(Integer id, String name, String detail, String dbuuid) {
		this.id = id;
		this.name = name;
		this.detail = detail;
		this.dbuuid = dbuuid;
	}

	/**
	 * Copy constructor. Take every property from a {@link org.jdom2.Element} with
	 * properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "detail"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	public Model1AgentXml(Element el) {
		this(XmlHelper.getInt(el, "id"), XmlHelper.getString(el, "name"), XmlHelper.getString(el, "detail"),
				XmlHelper.getString(el, "dbuuid"));
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "model1Agent" and properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "detail"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_AGENT);

		ret.setAttribute("id", XmlHelper.getNonNull(id));
		ret.setAttribute("name", XmlHelper.getNonNull(name));
		ret.setAttribute("detail", XmlHelper.getNonNull(detail));
		ret.setAttribute("dbuuid", XmlHelper.getNonNull(dbuuid));

		return ret;
	}

	public String getElementName() {
		return "modelLiterature";
	}
}