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

	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_SSE = "sse";
	private static final String ATT_RMS = "rms";
	private static final String ATT_R2 = "r2";
	private static final String ATT_AIC = "aic";
	private static final String ATT_BIC = "bic";
	private static final String ATT_DOF = "dof";
	private static final String ATT_QUALITYSCORE = "qualityScore";
	private static final String ATT_CHECKED = "checked";
	private static final String ATT_COMMENT = "comment";
	private static final String ATT_DBUUID = "dbuuid";

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

	public EstModelXml(Integer id, String name, Double sse, Double rms,
			Double r2, Double aic, Double bic, Integer dof) {
		this(id, name, sse, rms, r2, aic, bic, dof, null, null, null);
	}

	public EstModelXml(Integer id, String name, Double sse, Double rms,
			Double r2, Double aic, Double bic, Integer dof, Boolean checked,
			Integer qualityScore) {
		this(id, name, sse, rms, r2, aic, bic, dof, checked, qualityScore, null);
	}

	public EstModelXml(Integer id, String name, Double sse, Double rms,
			Double r2, Double aic, Double bic, Integer dof, Boolean checked,
			Integer qualityScore, String dbuuid) {
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

	public EstModelXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME),
				XmlHelper.getDouble(el, ATT_SSE), XmlHelper.getDouble(el,
						ATT_RMS), XmlHelper.getDouble(el, ATT_R2), XmlHelper
						.getDouble(el, ATT_AIC), XmlHelper.getDouble(el,
						ATT_BIC), XmlHelper.getInt(el, ATT_DOF), XmlHelper
						.getBoolean(el, ATT_CHECKED), XmlHelper.getInt(el,
						ATT_QUALITYSCORE), XmlHelper.getString(el, ATT_DBUUID));
		this.comment = XmlHelper.getString(el, ATT_COMMENT);
	}

	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_ESTMODEL);

		ret.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		ret.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		ret.setAttribute(ATT_SSE, XmlHelper.getNonNull(sse));
		ret.setAttribute(ATT_RMS, XmlHelper.getNonNull(rms));
		ret.setAttribute(ATT_R2, XmlHelper.getNonNull(r2));
		ret.setAttribute(ATT_AIC, XmlHelper.getNonNull(aic));
		ret.setAttribute(ATT_BIC, XmlHelper.getNonNull(bic));
		ret.setAttribute(ATT_DOF, XmlHelper.getNonNull(dof));
		ret.setAttribute(ATT_QUALITYSCORE, XmlHelper.getNonNull(qualityScore));
		ret.setAttribute(ATT_CHECKED, XmlHelper.getNonNull(checked));
		ret.setAttribute(ATT_COMMENT, XmlHelper.getNonNull(comment));
		ret.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

		return ret;
	}
}
