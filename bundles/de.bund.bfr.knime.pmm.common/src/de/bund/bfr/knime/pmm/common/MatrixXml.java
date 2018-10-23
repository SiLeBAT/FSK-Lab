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

public class MatrixXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_MATRIX = "matrix";

	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_DETAIL = "detail";
	private static final String ATT_DBUUID = "dbuuid";

	public Integer id;
	public String name;
	public String detail;
	public String dbuuid;

	public MatrixXml(Integer id, String name, String detail) {
		this(id, name, detail, null);
	}

	public MatrixXml() {
		this(MathUtilities.getRandomNegativeInt(), null, null, null);
	}

	public MatrixXml(MatrixXml matrix) {
		this(matrix.id, matrix.name, matrix.detail, matrix.dbuuid);
	}

	public MatrixXml(Integer id, String name, String detail, String dbuuid) {
		this.id = id;
		this.name = name;
		this.detail = detail;
		this.dbuuid = dbuuid;
	}

	public MatrixXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME), XmlHelper.getString(el, ATT_DETAIL),
				XmlHelper.getString(el, ATT_DBUUID));
	}

	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_MATRIX);

		ret.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		ret.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		ret.setAttribute(ATT_DETAIL, XmlHelper.getNonNull(detail));
		ret.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

		return ret;
	}
}
