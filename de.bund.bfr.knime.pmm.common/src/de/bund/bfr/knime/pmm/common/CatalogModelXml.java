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

	private Integer id;
	private String name;
	private String formula;
	private Integer modelClass;
	private String comment;
	private String dbuuid;

	public CatalogModelXml(Integer id, String name, String formula,
			Integer modelClass) {
		this(id, name, formula, modelClass, null);
	}

	public CatalogModelXml(Integer id, String name, String formula,
			Integer modelClass, String dbuuid) {
		setId(id == null ? MathUtilities.getRandomNegativeInt() : id);
		setName(name);
		setFormula(formula);
		setModelClass(modelClass);
		setDbuuid(dbuuid);
	}

	public CatalogModelXml(Element el) {
		this(XmlHelper.getInt(el, ATT_ID), XmlHelper.getString(el, ATT_NAME),
				XmlHelper.getString(el, ATT_FORMULA), XmlHelper.getInt(el,
						ATT_MODEL_CLASS), XmlHelper.getString(el, ATT_DBUUID));
		this.setComment(XmlHelper.getString(el, ATT_COMMENT));
	}

	@Override
	public Element toXmlElement() {
		Element modelElement = new Element(ELEMENT_CATALOGMODEL);

		modelElement.setAttribute(ATT_ID, XmlHelper.getNonNull(id));
		modelElement.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		modelElement.setAttribute(ATT_FORMULA, XmlHelper.getNonNull(formula));
		modelElement.setAttribute(ATT_MODEL_CLASS,
				XmlHelper.getNonNull(modelClass));
		modelElement.setAttribute(ATT_COMMENT, XmlHelper.getNonNull(comment));
		modelElement.setAttribute(ATT_DBUUID, XmlHelper.getNonNull(dbuuid));

		return modelElement;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Integer getModelClass() {
		return modelClass;
	}

	public void setModelClass(Integer modelClass) {
		this.modelClass = modelClass;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}
}
