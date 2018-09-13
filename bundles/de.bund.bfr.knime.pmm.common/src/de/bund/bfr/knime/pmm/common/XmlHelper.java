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

public class XmlHelper {

	private XmlHelper() {
	}

	public static String getString(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		return el.getAttributeValue(attr);
	}

	public static Integer getInt(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		try {
			return Integer.valueOf(el.getAttributeValue(attr));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double getDouble(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		try {
			return Double.valueOf(el.getAttributeValue(attr));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Boolean getBoolean(Element el, String attr) {
		if (el == null || el.getAttributeValue(attr) == null
				|| el.getAttributeValue(attr).isEmpty()) {
			return null;
		}

		return Boolean.valueOf(el.getAttributeValue(attr));
	}

	public static String getNonNull(Object o) {
		if (o == null) {
			return "";
		}

		if (o instanceof Double
				&& (((Double) o).isNaN() || ((Double) o).isNaN())) {
			return "";
		}

		return o.toString();
	}

	public static String removeDirt(String toClean) {
		String cleaned = (toClean == null ? "" : toClean);
		cleaned = cleaned.toString().replace("&amp;", "&"); // .replace("\n",
															// " ");
															// //.replaceAll("[^A-Za-zäöüßÄÖÜ0-9+-.,;': ()°%?&=<>/]",
															// "");
		cleaned = cleanInvalidXmlChars(cleaned);
		/*
		 * if (toClean != null && !toClean.equals(cleaned)) {
		 * System.err.println(toClean); System.err.println(cleaned); }
		 */
		return cleaned;
	}

	public static String cleanInvalidXmlChars(String text) {
		String re = "[^^\u0009\r\n\u0020-\uD7FF\uE000-\uFFFD]";
		return text.replaceAll(re, " ");
	}
}
