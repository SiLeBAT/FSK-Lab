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

import org.jdom2.Element;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class AgentXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_AGENT = "agent";

	public Integer id;
	public String name;
	public String detail;
	public String dbuuid;

	/** id is given a random negative int. name, detail and dbuuid are null. */
	public AgentXml() {
		this(MathUtilities.getRandomNegativeInt(), null, null, null);
	}

	/** Constructor with id, name and detail. dbuuid is null. */
	public AgentXml(Integer id, String name, String detail) {
		this(id, name, detail, null);
	}

	/** Copy constructor. Take every property from agent. */
	public AgentXml(AgentXml agent) {
		this(agent.id, agent.name, agent.detail, agent.dbuuid);
	}

	/** Fully parameterized constructor. */
	public AgentXml(Integer id, String name, String detail, String dbuuid) {
		this.id = id;
		this.name = name;
		this.detail = detail;
		this.dbuuid = dbuuid;
	}

	/**
	 * Copy constructor. Take every property from a {@link org.jdom2.Element} with properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "detail"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	public AgentXml(Element el) {
		this(XmlHelper.getInt(el, "id"), XmlHelper.getString(el, "name"), XmlHelper.getString(el, "detail"),
				XmlHelper.getString(el, "dbuuid"));
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "agent" and properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "detail"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_AGENT);

		ret.setAttribute("id", XmlHelper.getNonNull(id));
		ret.setAttribute("name", XmlHelper.getNonNull(name));
		ret.setAttribute("detail", XmlHelper.getNonNull(detail));
		ret.setAttribute("dbuuid", XmlHelper.getNonNull(dbuuid));

		return ret;
	}
}
