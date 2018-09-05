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

import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.xml.XppDriver;

import de.bund.bfr.knime.pmm.common.chart.ColorAndShapeCreator;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

public class XmlConverter {

	private XmlConverter() {
	}

	public static XStream getXStream() {
		return new XStream(null, new XppDriver(), new ClassLoaderReference(
				Activator.class.getClassLoader()));
	}

	public static String objectToXml(Object obj) {
		return getXStream().toXML(obj);
	}

	@SuppressWarnings("unchecked")
	public static <T> T xmlToObject(String xml, T obj) {
		try {
			return (T) getXStream().fromXML(xml);
		} catch (Exception e) {
			return obj;
		}
	}

	public static String tupleToXml(KnimeTuple tuple) {
		List<Object> list = new ArrayList<>();

		if (tuple == null) {
			return objectToXml(list);
		}

		KnimeSchema schema = tuple.getSchema();

		list.add(schema);

		for (int i = 0; i < schema.size(); i++) {
			switch (schema.getType(i)) {
			case KnimeAttribute.TYPE_INT:
				list.add(tuple.getInt(schema.getName(i)));
				break;
			case KnimeAttribute.TYPE_DOUBLE:
				list.add(tuple.getDouble(schema.getName(i)));
				break;
			case KnimeAttribute.TYPE_STRING:
				list.add(tuple.getString(schema.getName(i)));
				break;
			case KnimeAttribute.TYPE_XML:
				list.add(tuple.getPmmXml(schema.getName(i)));
			}
		}

		return objectToXml(list);
	}

	public static String tupleListToXml(List<KnimeTuple> tuples) {
		List<String> stringList = new ArrayList<>();

		for (KnimeTuple tuple : tuples) {
			stringList.add(tupleToXml(tuple));
		}

		return objectToXml(stringList);
	}

	public static String tupleMapToXml(Map<String, KnimeTuple> tuples) {
		Map<String, String> stringMap = new LinkedHashMap<>();

		for (String key : tuples.keySet()) {
			stringMap.put(key, tupleToXml(tuples.get(key)));
		}

		return objectToXml(stringMap);
	}

	public static String colorMapToXml(Map<String, Color> map) {
		return objectToXml(colorMapToStringMap(map));
	}

	public static String shapeMapToXml(Map<String, Shape> map) {
		return objectToXml(shapeMapToStringMap(map));
	}

	public static String colorListMapToXml(Map<String, List<Color>> map) {
		return objectToXml(colorListMapToStringMap(map));
	}

	public static String shapeListMapToXml(Map<String, List<Shape>> map) {
		return objectToXml(shapeListMapToStringMap(map));
	}

	public static KnimeTuple xmlToTuple(String xml) {
		List<Object> list = xmlToObject(xml, new ArrayList<>());

		if (!list.isEmpty()) {
			KnimeSchema schema = (KnimeSchema) list.get(0);
			KnimeTuple tuple = new KnimeTuple(schema);

			for (int i = 0; i < schema.size(); i++) {
				tuple.setValue(schema.getName(i), list.get(i + 1));
			}

			return tuple;
		} else {
			return null;
		}
	}

	public static List<KnimeTuple> xmlToTupleList(String xml) {
		List<String> stringList = xmlToObject(xml, new ArrayList<String>());
		List<KnimeTuple> tuples = new ArrayList<>();

		for (String s : stringList) {
			tuples.add(xmlToTuple(s));
		}

		return tuples;
	}

	public static Map<String, KnimeTuple> xmlToTupleMap(String xml) {
		Map<String, String> stringMap = xmlToObject(xml,
				new LinkedHashMap<String, String>());
		Map<String, KnimeTuple> tuples = new LinkedHashMap<>();

		for (String key : stringMap.keySet()) {
			tuples.put(key, xmlToTuple(stringMap.get(key)));
		}

		return tuples;
	}

	public static Map<String, Color> xmlToColorMap(String xml) {
		return stringMapToColorMap(xmlToObject(xml,
				new LinkedHashMap<String, String>()));
	}

	public static Map<String, Shape> xmlToShapeMap(String xml) {
		return stringMapToShapeMap(xmlToObject(xml,
				new LinkedHashMap<String, String>()));
	}

	public static Map<String, List<Color>> xmlToColorListMap(String xml) {
		return stringMapToColorListMap(xmlToObject(xml,
				new LinkedHashMap<String, List<String>>()));
	}

	public static Map<String, List<Shape>> xmlToShapeListMap(String xml) {
		return stringMapToShapeListMap(xmlToObject(xml,
				new LinkedHashMap<String, List<String>>()));
	}

	private static Map<String, String> colorMapToStringMap(
			Map<String, Color> map) {
		Map<String, String> newMap = new LinkedHashMap<>();

		for (Map.Entry<String, Color> entry : map.entrySet()) {
			String colorString = "#"
					+ Integer.toHexString(entry.getValue().getRGB()).substring(
							2);

			newMap.put(entry.getKey(), colorString);
		}

		return newMap;
	}

	private static Map<String, Color> stringMapToColorMap(
			Map<String, String> map) {
		Map<String, Color> newMap = new LinkedHashMap<>();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			newMap.put(entry.getKey(), Color.decode(entry.getValue()));
		}

		return newMap;
	}

	private static Map<String, String> shapeMapToStringMap(
			Map<String, Shape> map) {
		Map<String, String> newMap = new LinkedHashMap<>();
		Map<Shape, String> shapeMap = (new ColorAndShapeCreator(0))
				.getNameByShapeMap();

		for (Map.Entry<String, Shape> entry : map.entrySet()) {
			newMap.put(entry.getKey(), shapeMap.get(entry.getValue()));
		}

		return newMap;
	}

	private static Map<String, Shape> stringMapToShapeMap(
			Map<String, String> map) {
		Map<String, Shape> newMap = new LinkedHashMap<>();
		Map<String, Shape> shapeMap = (new ColorAndShapeCreator(0))
				.getShapeByNameMap();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			newMap.put(entry.getKey(), shapeMap.get(entry.getValue()));
		}

		return newMap;
	}

	private static Map<String, List<String>> colorListMapToStringMap(
			Map<String, List<Color>> map) {
		Map<String, List<String>> newMap = new LinkedHashMap<>();

		for (Map.Entry<String, List<Color>> entry : map.entrySet()) {
			List<String> list = new ArrayList<>();

			for (Color color : entry.getValue()) {
				list.add("#" + Integer.toHexString(color.getRGB()).substring(2));
			}

			newMap.put(entry.getKey(), list);
		}

		return newMap;
	}

	private static Map<String, List<Color>> stringMapToColorListMap(
			Map<String, List<String>> map) {
		Map<String, List<Color>> newMap = new LinkedHashMap<>();

		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			List<Color> list = new ArrayList<>();

			for (String s : entry.getValue()) {
				list.add(Color.decode(s));
			}

			newMap.put(entry.getKey(), list);
		}

		return newMap;
	}

	private static Map<String, List<String>> shapeListMapToStringMap(
			Map<String, List<Shape>> map) {
		Map<String, List<String>> newMap = new LinkedHashMap<>();
		Map<Shape, String> shapeMap = (new ColorAndShapeCreator(0))
				.getNameByShapeMap();

		for (Map.Entry<String, List<Shape>> entry : map.entrySet()) {
			List<String> list = new ArrayList<>();

			for (Shape shape : entry.getValue()) {
				list.add(shapeMap.get(shape));
			}

			newMap.put(entry.getKey(), list);
		}

		return newMap;
	}

	private static Map<String, List<Shape>> stringMapToShapeListMap(
			Map<String, List<String>> map) {
		Map<String, List<Shape>> newMap = new LinkedHashMap<>();
		Map<String, Shape> shapeMap = (new ColorAndShapeCreator(0))
				.getShapeByNameMap();

		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			List<Shape> list = new ArrayList<>();

			for (String s : entry.getValue()) {
				list.add(shapeMap.get(s));
			}

			newMap.put(entry.getKey(), list);
		}

		return newMap;
	}

}
