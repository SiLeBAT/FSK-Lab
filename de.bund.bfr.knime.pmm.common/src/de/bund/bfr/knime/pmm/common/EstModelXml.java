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

public class EstModelXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_ESTMODEL = "estmodelxml";

	public Integer id;
	public String name;
	public Double sse;
	public Double rms;
	public Double r2;
	public Double aic;
	public Double bic;
	public Integer dof;
	public Integer qualityScore;
	public Boolean checked;
	public String comment;
	public String dbuuid;

	/**
	 * Constructor with id, name, sse, rms, r2, aic, bic and dof, checked,
	 * qualityScore and dbuuid is null.
	 */
	public EstModelXml(Integer id, String name, Double sse, Double rms, Double r2, Double aic, Double bic,
			Integer dof) {
		this(id, name, sse, rms, r2, aic, bic, dof, null, null, null);
	}

	/**
	 * Constructor with id, name, sse, rms, r2, aic, bic, dof, checked and
	 * qualityScore. dbuuid is null.
	 */
	public EstModelXml(Integer id, String name, Double sse, Double rms, Double r2, Double aic, Double bic, Integer dof,
			Boolean checked, Integer qualityScore) {
		this(id, name, sse, rms, r2, aic, bic, dof, checked, qualityScore, null);
	}

	/** Fully parameterized constructor. */
	public EstModelXml(Integer id, String name, Double sse, Double rms, Double r2, Double aic, Double bic, Integer dof,
			Boolean checked, Integer qualityScore, String dbuuid) {
		this.id = id;
		this.name = name;
		this.sse = sse;
		this.rms = rms;
		this.r2 = r2;
		this.aic = aic;
		this.bic = bic;
		this.dof = dof;
		this.qualityScore = qualityScore;
		this.checked = checked;
		this.dbuuid = dbuuid;
	}

	/**
	 * Copy constructor. Take every property from a {@link org.jdom2.Element} with
	 * properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>Double "sse"
	 * <li>Double "rms"
	 * <li>Double "r2"
	 * <li>Double "aic"
	 * <li>Double "bic"
	 * <li>Integer "dof"
	 * <li>Integer "qualityScore"
	 * <li>Boolean "checked"
	 * <li>String "comment"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	public EstModelXml(Element el) {
		this(XmlHelper.getInt(el, "id"), XmlHelper.getString(el, "name"), XmlHelper.getDouble(el, "sse"),
				XmlHelper.getDouble(el, "rms"), XmlHelper.getDouble(el, "r2"), XmlHelper.getDouble(el, "aic"),
				XmlHelper.getDouble(el, "bic"), XmlHelper.getInt(el, "dof"), XmlHelper.getBoolean(el, "checked"),
				XmlHelper.getInt(el, "qualityScore"), XmlHelper.getString(el, "dbuuid"));
		this.comment = XmlHelper.getString(el, "comment");
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "estmodelxml" and properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>Double "sse"
	 * <li>Double "rms"
	 * <li>Double "r2"
	 * <li>Double "aic"
	 * <li>Double "bic"
	 * <li>Integer "dof"
	 * <li>Integer "qualityScore"
	 * <li>Boolean "checked"
	 * <li>String "comment"
	 * <li>String "dbuuid"
	 * </ul>
	 */
	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_ESTMODEL);

		ret.setAttribute("id", XmlHelper.getNonNull(id));
		ret.setAttribute("name", XmlHelper.getNonNull(name));
		ret.setAttribute("sse", XmlHelper.getNonNull(sse));
		ret.setAttribute("rms", XmlHelper.getNonNull(rms));
		ret.setAttribute("r2", XmlHelper.getNonNull(r2));
		ret.setAttribute("aic", XmlHelper.getNonNull(aic));
		ret.setAttribute("bic", XmlHelper.getNonNull(bic));
		ret.setAttribute("dof", XmlHelper.getNonNull(dof));
		ret.setAttribute("qualityScore", XmlHelper.getNonNull(qualityScore));
		ret.setAttribute("checked", XmlHelper.getNonNull(checked));
		ret.setAttribute("comment", XmlHelper.getNonNull(comment));
		ret.setAttribute("dbuuid", XmlHelper.getNonNull(dbuuid));

		return ret;
	}
}
