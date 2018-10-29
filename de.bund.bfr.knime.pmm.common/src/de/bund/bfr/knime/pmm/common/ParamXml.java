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

import java.util.HashMap;

import org.jdom2.Element;

public class ParamXml implements PmmXmlElementConvertable {

	public static final String ELEMENT_PARAM = "param";

	public String name;
	public String origName;
	public Boolean isStartParam = false;
	public Double value;
	public Double error;
	public Double min;
	public Double max;
	public Double P;
	public Double t;
	public Double minGuess;
	public Double maxGuess;
	public String category;
	public String unit;
	public String description;
	private HashMap<String, Double> correlations;

	/** Fully parameterized constructor. */
	public ParamXml(String name, String origName, Boolean isStartParam, Double value, Double error, Double min,
			Double max, Double P, Double t, Double minGuess, Double maxGuess, String category, String unit,
			String description, HashMap<String, Double> correlations) {
		this.name = name;
		this.origName = origName;
		this.isStartParam = (isStartParam == null ? false : isStartParam);
		this.value = value;
		this.error = error;
		this.min = min;
		this.max = max;
		this.P = P;
		this.t = t;
		this.minGuess = minGuess;
		this.maxGuess = maxGuess;
		this.category = category;
		this.unit = unit;
		this.description = description;
		this.correlations = correlations;
	}

	/**
	 * Constructor with name, isStartParam and value.
	 * 
	 * <ul>
	 * <li>origName is assigned name.
	 * <li>error, min, max, P, t, minGuess, maxGuess, category, unit, description
	 * are null.
	 * <li>correlations is assigned an empty map.
	 * </ul>
	 */
	public ParamXml(String name, Boolean isStartParam, Double value) {
		this(name, name, isStartParam, value, null, null, null, null, null, null, null, null, null, null,
				new HashMap<String, Double>());
	}

	/**
	 * Constructor with name, isStartParam, value, error, min, max, P and t.
	 * 
	 * <ul>
	 * <li>origName is assigned name.
	 * <li>minGuess, maxGuess, category, unit, description are null.
	 * <li>correlations is assigned an empty map.
	 * </ul>
	 */
	public ParamXml(String name, Boolean isStartParam, Double value, Double error, Double min, Double max, Double P,
			Double t) {
		this(name, name, isStartParam, value, error, min, max, P, t, null, null, null, null, null,
				new HashMap<String, Double>());
	}

	/**
	 * Constructor with name, isStartParam, value, error, min, max, P, t, category
	 * and unit.
	 * 
	 * <ul>
	 * <li>origName is assigned name.
	 * <li>minGuess, maxGuess and description are null.
	 * <li>correlations is assigned an empty map.
	 * </ul>
	 */
	public ParamXml(String name, Boolean isStartParam, Double value, Double error, Double min, Double max, Double P,
			Double t, String category, String unit) {
		this(name, name, isStartParam, value, error, min, max, P, t, null, null, category, unit, null,
				new HashMap<String, Double>());
	}

	/**
	 * Copy constructor. Take every property from a {@link org.jdom2.Element} with
	 * properties:
	 * 
	 * <ul>
	 * <li>String "name"
	 * <li>String "origname"
	 * <li>Boolean "isStart"
	 * <li>Double "value"
	 * <li>Double "error"
	 * <li>Double "min"
	 * <li>Double "max"
	 * <li>Double "P"
	 * <li>Double "t"
	 * <li>Double "minGuess"
	 * <li>Double "maxGuess"
	 * <li>String "category"
	 * <li>String "unit"
	 * <li>String "description"
	 * <li>Multiple children with name "correlation" and attributes: string
	 * "origname" and double "value"
	 * </ul>
	 */
	public ParamXml(Element el) {
		this(XmlHelper.getString(el, "name"), XmlHelper.getString(el, "origname"),
				XmlHelper.getBoolean(el, "isStart"), XmlHelper.getDouble(el, "value"),
				XmlHelper.getDouble(el, "error"), XmlHelper.getDouble(el, "min"), XmlHelper.getDouble(el, "max"),
				XmlHelper.getDouble(el, "P"), XmlHelper.getDouble(el, "t"), XmlHelper.getDouble(el, "minGuess"),
				XmlHelper.getDouble(el, "maxGuess"), XmlHelper.getString(el, "category"),
				XmlHelper.getString(el, "unit"), XmlHelper.getString(el, "description"),
				new HashMap<String, Double>());

		for (Element e : el.getChildren()) {
			if (e.getName().equals("correlation")) {
				String n = e.getAttributeValue("origname");
				Double d = XmlHelper.getDouble(e, "value");

				correlations.put(n, d);
			}
		}
	}

	/**
	 * Generate a {@link org.jdom2.Element} with name "param" and properties:
	 * 
	 * <ul>
	 * <li>String "name"
	 * <li>String "origname"
	 * <li>Boolean "isStart"
	 * <li>Double "value"
	 * <li>Double "error"
	 * <li>Double "min"
	 * <li>Double "max"
	 * <li>Double "P"
	 * <li>Double "t"
	 * <li>Double "minGuess"
	 * <li>Double "maxGuess"
	 * <li>String "category"
	 * <li>String "unit"
	 * <li>String "description"
	 * <li>Multiple children with name "correlation" and attributes: string
	 * "origname" and double "value"
	 * </ul>
	 */
	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_PARAM);

		ret.setAttribute("name", XmlHelper.getNonNull(name));
		ret.setAttribute("origname", XmlHelper.getNonNull(origName));
		ret.setAttribute("isStart", XmlHelper.getNonNull(isStartParam));
		ret.setAttribute("value", XmlHelper.getNonNull(value));
		ret.setAttribute("error", XmlHelper.getNonNull(error));
		ret.setAttribute("min", XmlHelper.getNonNull(min));
		ret.setAttribute("max", XmlHelper.getNonNull(max));
		ret.setAttribute("P", XmlHelper.getNonNull(P));
		ret.setAttribute("t", XmlHelper.getNonNull(t));
		ret.setAttribute("minGuess", XmlHelper.getNonNull(minGuess));
		ret.setAttribute("maxGuess", XmlHelper.getNonNull(maxGuess));
		ret.setAttribute("category", XmlHelper.getNonNull(category));
		ret.setAttribute("unit", XmlHelper.getNonNull(unit));
		ret.setAttribute("description", XmlHelper.getNonNull(description));

		if (correlations != null) {
			for (String origname : correlations.keySet()) {
				Element element = new Element("correlation");
				Double d = correlations.get(origname);

				element.setAttribute("origname", origname);
				element.setAttribute("value", XmlHelper.getNonNull(d));
				ret.addContent(element);
			}
		}

		return ret;
	}

	public void addCorrelation(String otherOrigname, Double value) {
		correlations.put(otherOrigname, value);
	}

	public Double getCorrelation(String otherOrigname) {
		if (correlations.containsKey(otherOrigname)) {
			return correlations.get(otherOrigname);
		} else {
			return null;
		}
	}

	public HashMap<String, Double> getAllCorrelations() {
		return correlations;
	}
}
