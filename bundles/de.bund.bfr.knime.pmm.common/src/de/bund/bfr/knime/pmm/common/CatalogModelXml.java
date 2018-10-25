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

public class CatalogModelXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_CATALOGMODEL = "catalogmodelxml";

	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_FORMULA = "formula";
	private static final String ATT_MODEL_CLASS = "modelClass";
	private static final String ATT_COMMENT = "comment";
	private static final String ATT_DBUUID = "dbuuid";

	public Integer id;
	public String name;
	public String formula;
	public Integer modelClass;
	public String comment;
	public String dbuuid;

	/**
	 * Constructor with id, name, formula and model class. comment and dbuuid are
	 * null.
	 */
	public CatalogModelXml(Integer id, String name, String formula, Integer modelClass) {
		this(id, name, formula, modelClass, null);
	}

	/**
	 * Fully parameterized constructor.
	 * 
	 * @param id
	 *            if null a random negative int is assigned.
	 */
	public CatalogModelXml(Integer id, String name, String formula, Integer modelClass, String dbuuid) {
		this.id = id == null ? MathUtilities.getRandomNegativeInt() : id;
		this.name = name;
		this.formula = formula;
		this.modelClass = modelClass;
		this.dbuuid = dbuuid;
	}

	/**
	 * Copy constructor. Take every property from a {@link org.jdom2.Element} with properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "formula>
	 * <li>Integer "modelClass"
	 * <li>String "dbuuid"
	 * <li>String "comment"
	 * </ul>
	 */
	public CatalogModelXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME), XmlHelper.getString(el, ATT_FORMULA),
				XmlHelper.getInt(el, ATT_MODEL_CLASS), XmlHelper.getString(el, ATT_DBUUID));
		this.comment = XmlHelper.getString(el, ATT_COMMENT);
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "catalogmodelxml" and properties:
	 * <ul>
	 * <li>Integer "id"
	 * <li>String "name"
	 * <li>String "formula>
	 * <li>Integer "modelClass"
	 * <li>String "dbuuid"
	 * <li>String "comment"
	 * </ul>
	 */
	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_CATALOGMODEL);

		modelElement.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		modelElement.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		modelElement.setAttribute(ATT_FORMULA, XmlHelper.getNonNull(formula));
		modelElement.setAttribute(ATT_MODEL_CLASS, XmlHelper.getNonNull(modelClass));
		modelElement.setAttribute(ATT_COMMENT, XmlHelper.getNonNull(comment));
		modelElement.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

		return modelElement;
	}
}
