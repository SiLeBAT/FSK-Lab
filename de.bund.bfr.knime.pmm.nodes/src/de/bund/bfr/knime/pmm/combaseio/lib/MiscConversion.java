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
package de.bund.bfr.knime.pmm.combaseio.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class MiscConversion {

	private Map<String, MiscXml> combaseToPmmMap;
	private Map<String, String> pmmToCombaseMap;

	public MiscConversion() {
		List<String> combaseNames = new ArrayList<>();
		List<MiscXml> pmmLabMiscs = new ArrayList<>();

		combaseNames.add("alta fermentation product in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "ALTA", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("acetic acid (possibly as salt) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "acetic_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("anaerobic environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "anaerobic", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("ascorbic acid (possibly as salt) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "ascorbic_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("benzoic acid (possibly as salt) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "benzoic_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("citric acid (possibly as salt) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "citric_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("carbon-dioxide in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "CO_2", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("other species in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "competition", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("cut (minced, chopped, ground, etc)");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "cut", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("dried food");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "dried", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("ethylenenediaminetetraacetic acid in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "EDTA", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("ethanol in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "ethanol", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("fat in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "fat", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("frozen food");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "frozen", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("fructose in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "fructose", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("glucose in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "glucose", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("glycerol in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "glycerol", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("hydrochloric acid in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "HCl", "", null,
				Arrays.asList("Mass Concentration"), "g/L"));
		combaseNames.add(
				"inoculation in/on previously heated (cooked, baked, pasteurized, etc) but not sterilised food/medium");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "heated", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("in an environment that has been irradiated");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "irradiated", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("irradiation at constant rate during the observation time");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "irradiation", "", null,
				Arrays.asList("Energy Content"), "kGy"));
		combaseNames.add("lactic acid (possibly as salt) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "lactic_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("modified atmosphere environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "Modified_Atmosphere", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("malic acid in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "malic_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("moisture in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "moisture", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("glycerol monolaurate (emulsifier) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "monolaurin", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("nitrogen in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "N_2", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("sodium chloride in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "NaCl", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("nisin in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "nisin", "", null, null, null));
		combaseNames.add("sodium or potassium nitrite in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "nitrite", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("oxygen (aerobic conditions) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "O_2", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("propionic acid (possibly as salt) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "propionic_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("raw");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "raw", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("shaken (agitated, stirred)");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "shaken", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("smoked food");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "smoked", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("sorbic acid (possibly as salt) in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "sorbic_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("sterilised before inoculation");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "sterile", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("sucrose in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "sucrose", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("sugar in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "sugar", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("vacuum-packed");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "vacuum", "", null,
				Arrays.asList("True/False Value"), "True/False"));
		combaseNames.add("oregano essential oil in the environment");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "oregano", "", null,
				Arrays.asList("Arbitrary Fraction"), "%"));
		combaseNames.add("pressure controlled");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "pressure", "", null,
				Arrays.asList("Pressure"), "MPa"));
		combaseNames.add("in presence of diacetic acid (possibly as salt)");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "diacetic_acid", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));
		combaseNames.add("in presence of betaine");
		pmmLabMiscs.add(new MiscXml(MathUtilities.getRandomNegativeInt(), "betaine", "", null,
				Arrays.asList("Arbitrary Fraction"), "ppm"));

		combaseToPmmMap = new LinkedHashMap<>();
		pmmToCombaseMap = new LinkedHashMap<>();

		for (int i = 0; i < combaseNames.size(); i++) {
			combaseToPmmMap.put(combaseNames.get(i).toLowerCase(), pmmLabMiscs.get(i));
			pmmToCombaseMap.put(pmmLabMiscs.get(i).getName().toLowerCase(), combaseNames.get(i));
		}
	}

	public MiscXml combaseToPmm(String combaseName) {
		if (!combaseToPmmMap.containsKey(combaseName.toLowerCase())) {
			throw new RuntimeException("Unknown Condition: " + combaseName);
		}

		return combaseToPmmMap.get(combaseName.toLowerCase());
	}

	public String pmmToCombase(MiscXml misc) {
		if (!pmmToCombaseMap.containsKey(misc.getName().toLowerCase())) {
			throw new RuntimeException("Unknown Condition: " + misc.getName());
		}

		return pmmToCombaseMap.get(misc.getName().toLowerCase());
	}
}
