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

	private static final String ATT_NAME = "name";
	private static final String ATT_ORIGNAME = "origname";
	private static final String ATT_IS_START = "isStart";
	private static final String ATT_VALUE = "value";
	private static final String ATT_ERROR = "error";
	private static final String ATT_MIN = "min";
	private static final String ATT_MAX = "max";
	private static final String ATT_P = "P";
	private static final String ATT_T = "t";
	private static final String ATT_MINGUESS = "minGuess";
	private static final String ATT_MAXGUESS = "maxGuess";
	private static final String ATT_CATEGORY = "category";
	private static final String ATT_UNIT = "unit";
	private static final String ATT_DESCRIPTION = "description";
	private static final String ATT_CORRELATION = "correlation";

	private String name;
	private String origName;
	private Boolean isStartParam = false;
	private Double value;
	private Double error;
	private Double min;
	private Double max;
	private Double P;
	private Double t;
	private Double minGuess;
	private Double maxGuess;
	private String category;
	private String unit;
	private String description;
	private HashMap<String, Double> correlations;

	public ParamXml(String name, String origName, Boolean isStartParam, Double value, Double error,
			Double min, Double max, Double P, Double t, Double minGuess,
			Double maxGuess, String category, String unit, String description,
			HashMap<String, Double> correlations) {
		this.name = name;
		this.origName = origName;
		this.isStartParam = (isStartParam==null?false:isStartParam);
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

	public ParamXml(String name, Boolean isStartParam, Double value) {
		this(name, name, isStartParam, value, null, null, null, null, null, null, null, null,
				null, null, new HashMap<String, Double>());
	}

	public ParamXml(String name, Boolean isStartParam, Double value, Double error, Double min,
			Double max, Double P, Double t) {
		this(name, name, isStartParam, value, error, min, max, P, t, null, null, null, null,
				null, new HashMap<String, Double>());
	}

	public ParamXml(String name, Boolean isStartParam, Double value, Double error, Double min,
			Double max, Double P, Double t, String category, String unit) {
		this(name, name, isStartParam, value, error, min, max, P, t, null, null, category,
				unit, null, new HashMap<String, Double>());
	}

	public ParamXml(Element el) {
		this(XmlHelper.getString(el, ATT_NAME), XmlHelper.getString(el,
				ATT_ORIGNAME), XmlHelper.getBoolean(el, ATT_IS_START), XmlHelper.getDouble(el, ATT_VALUE), XmlHelper
				.getDouble(el, ATT_ERROR), XmlHelper.getDouble(el, ATT_MIN),
				XmlHelper.getDouble(el, ATT_MAX), XmlHelper
						.getDouble(el, ATT_P), XmlHelper.getDouble(el, ATT_T),
				XmlHelper.getDouble(el, ATT_MINGUESS), XmlHelper.getDouble(el,
						ATT_MAXGUESS), XmlHelper.getString(el, ATT_CATEGORY),
				XmlHelper.getString(el, ATT_UNIT), XmlHelper.getString(el,
						ATT_DESCRIPTION), new HashMap<String, Double>());

		for (Element e : el.getChildren()) {
			if (e.getName().equals(ATT_CORRELATION)) {
				String n = e.getAttributeValue(ATT_ORIGNAME);
				Double d = XmlHelper.getDouble(e, ATT_VALUE);

				correlations.put(n, d);
			}
		}
	}

	@Override
	public Element toXmlElement() {
		Element ret = new Element(ELEMENT_PARAM);

		ret.setAttribute(ATT_NAME, XmlHelper.getNonNull(name));
		ret.setAttribute(ATT_ORIGNAME, XmlHelper.getNonNull(origName));
		ret.setAttribute(ATT_IS_START, XmlHelper.getNonNull(isStartParam));
		ret.setAttribute(ATT_VALUE, XmlHelper.getNonNull(value));
		ret.setAttribute(ATT_ERROR, XmlHelper.getNonNull(error));
		ret.setAttribute(ATT_MIN, XmlHelper.getNonNull(min));
		ret.setAttribute(ATT_MAX, XmlHelper.getNonNull(max));
		ret.setAttribute(ATT_P, XmlHelper.getNonNull(P));
		ret.setAttribute(ATT_T, XmlHelper.getNonNull(t));
		ret.setAttribute(ATT_MINGUESS, XmlHelper.getNonNull(minGuess));
		ret.setAttribute(ATT_MAXGUESS, XmlHelper.getNonNull(maxGuess));
		ret.setAttribute(ATT_CATEGORY, XmlHelper.getNonNull(category));
		ret.setAttribute(ATT_UNIT, XmlHelper.getNonNull(unit));
		ret.setAttribute(ATT_DESCRIPTION, XmlHelper.getNonNull(description));

		if (correlations != null) {
			for (String origname : correlations.keySet()) {
				Element element = new Element(ATT_CORRELATION);
				Double d = correlations.get(origname);

				element.setAttribute(ATT_ORIGNAME, origname);
				element.setAttribute(ATT_VALUE, XmlHelper.getNonNull(d));
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrigName() {
		return origName;
	}

	public void setOrigName(String origName) {
		this.origName = origName;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public boolean isStartParam() {
		return isStartParam;
	}
	public void setIsStartParam(boolean isStartParam) {
		this.isStartParam = isStartParam;
	}
	public Double getError() {
		return error;
	}

	public void setError(Double error) {
		this.error = error;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getP() {
		return P;
	}

	public void setP(Double p) {
		P = p;
	}

	public Double getT() {
		return t;
	}

	public void setT(Double t) {
		this.t = t;
	}

	public Double getMinGuess() {
		return minGuess;
	}

	public void setMinGuess(Double minGuess) {
		this.minGuess = minGuess;
	}

	public Double getMaxGuess() {
		return maxGuess;
	}

	public void setMaxGuess(Double maxGuess) {
		this.maxGuess = maxGuess;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
