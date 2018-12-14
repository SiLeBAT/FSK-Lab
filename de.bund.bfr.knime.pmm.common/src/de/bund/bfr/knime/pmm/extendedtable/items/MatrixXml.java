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

import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XmlHelper;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class MatrixXml implements PmmXmlElementConvertable {
	
	public enum Type {
		MD, Model1, Model2
	}
	
	private final Type type;
	
	public Integer id;
	public String name;
	public String detail;
	public String dbuuid;

	/** Constructor with id, name and detail. dbuuid is null. */
	public MatrixXml(Type type, Integer id, String name, String detail) {
		this(type, id, name, detail, null);
	}

	/** id is given a random negative id. name, detail and dbuuid are null. */
	public MatrixXml(Type type) {
		this(type, MathUtilities.getRandomNegativeInt(), null, null, null);
	}

	/** Copy constructor. Take every property from another {@link MDMatrixXml}. */
	public MatrixXml(MatrixXml matrix) {
		this(matrix.type, matrix.id, matrix.name, matrix.detail, matrix.dbuuid);
	}

	/** Fully parameterized constructor. */
	public MatrixXml(Type type, Integer id, String name, String detail, String dbuuid) {
		this.type = type;
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
	public MatrixXml(Element el) {
		String name = el.getName();
		if (name.equals("mdMatrix")) {
			this.type = Type.MD;
		} else if (name.equals("model1Matrix")) {
			this.type = Type.Model1;
		} else if (name.equals("model2Matrix")) {
			this.type = Type.Model2;
		} else {
			throw new RuntimeException("Invalid MatrixXml " + name);
		}

		this.id = XmlHelper.getInt(el, "id");
		this.name = XmlHelper.getString(el, "name");
		this.detail = XmlHelper.getString(el, "detail");
		this.dbuuid = XmlHelper.getString(el, "dbuuid");
	}

	public String getElementName() {
		String name = "";
		if (type == Type.MD) {
			name = "mdMatrix";
		} else if (type == Type.Model1) {
			name = "model1Matrix";
		} else if (type == Type.Model2) {
			name = "model2Matrix";
		}

		return name;
	}

	public Element toXmlElement() {
		Element ret = new Element(getElementName());

		ret.setAttribute("id", XmlHelper.getNonNull(id));
		ret.setAttribute("name", XmlHelper.getNonNull(name));
		ret.setAttribute("detail", XmlHelper.getNonNull(detail));
		ret.setAttribute("dbuuid", XmlHelper.getNonNull(dbuuid));

		return ret;
	}
}
