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
package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataTable;

import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

public class PmmUtilities {

	public static List<KnimeTuple> getTuples(DataTable table, KnimeSchema schema) {
		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		List<KnimeTuple> tuples = new ArrayList<>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		return tuples;
	}

	public static List<String> getIndeps(List<KnimeTuple> tuples) {
		Set<String> indepSet = new LinkedHashSet<>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc indep = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			indep.getElementSet().stream().map(it -> ((IndepXml)it).name).forEach(indepSet::add);
		}

		return new ArrayList<>(indepSet);
	}

	public static List<String> getMiscParams(List<KnimeTuple> tuples) {
		Set<String> paramSet = new LinkedHashSet<>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				paramSet.add(element.name);
			}
		}

		return new ArrayList<>(paramSet);
	}

	public static Map<String, List<String>> getMiscCategories(
			List<KnimeTuple> tuples) {
		Map<String, List<String>> map = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				map.put(element.name, element.categories);
			}
		}

		return map;
	}

	public static Map<String, String> getMiscUnits(List<KnimeTuple> tuples) {
		Map<String, Map<String, Integer>> occurences = new LinkedHashMap<>();
		Map<String, String> map = new LinkedHashMap<>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				if (!occurences.containsKey(element.name)) {
					occurences.put(element.name,
							new LinkedHashMap<String, Integer>());
				}

				Integer value = occurences.get(element.name).get(
						element.unit);

				if (value != null) {
					occurences.get(element.name).put(element.unit,
							value + 1);
				} else {
					occurences.get(element.name).put(element.unit, 1);
				}
			}
		}

		for (String name : occurences.keySet()) {
			String maxUnit = null;
			int maxN = 0;

			for (String unit : occurences.get(name).keySet()) {
				int n = occurences.get(name).get(unit);

				if (n > maxN) {
					maxUnit = unit;
					maxN = n;
				}
			}

			map.put(name, maxUnit);
		}

		return map;
	}

	public static boolean isOutOfRange(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			ParamXml element = (ParamXml) el;

			if (element.value != null) {
				if (element.min != null
						&& element.value < element.min) {
					return true;
				}

				if (element.max != null
						&& element.value > element.max) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean covarianceMatrixMissing(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			if (((ParamXml) el).error == null) {
				return true;
			}
		}

		return false;
	}

	public static boolean isNotSignificant(PmmXmlDoc paramXml) {
		for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
			Double p = ((ParamXml) el).P;

			if (p != null && p > 0.95) {
				return true;
			}
		}

		return false;
	}

}
