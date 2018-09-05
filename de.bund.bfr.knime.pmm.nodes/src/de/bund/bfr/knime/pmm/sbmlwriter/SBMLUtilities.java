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
package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class SBMLUtilities {

	public static String toXml(UnitDefinition unit) {
		SBMLDocument doc = new SBMLDocument(unit.getLevel(), unit.getVersion());
		Model model = doc.createModel("ID");

		model.addUnitDefinition(unit);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			SBMLWriter.write(doc, out, "test", "1.0", ' ', (short) 0);

			String xml = out.toString(StandardCharsets.UTF_8.name());
			String from = "<listOfUnitDefinitions>";
			String to = "</listOfUnitDefinitions>";

			return xml.substring(xml.indexOf(from) + from.length(),
					xml.indexOf(to)).replace("\n", "");
		} catch (SBMLException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static UnitDefinition fromXml(String xml) {
		String preXml = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>"
				+ "<sbml xmlns=\"http://www.sbml.org/sbml/level2/version4\" level=\"2\" version=\"4\">"
				+ "<model id=\"ID\">" + "<listOfUnitDefinitions>";
		String postXml = "</listOfUnitDefinitions>" + "</model>" + "</sbml>";

		try {
			UnitDefinition def = SBMLReader.read(preXml + xml + postXml)
					.getModel().getUnitDefinition(0);

			Matcher m = Pattern.compile("name=\"\\w+\"").matcher(xml);

			if (m.find()) {
				String transform = m.group().replace("name=", "")
						.replace("\"", "");

				def.setAnnotation(createTransformationAnnotation(transform));
			}

			return def;
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static UnitDefinition addUnitToModel(Model model, UnitDefinition unit) {
		UnitDefinition u = model.createUnitDefinition(unit.getId());

		for (int i = 0; i < unit.getNumUnits(); i++) {
			u.addUnit(new Unit(unit.getUnit(i)));
		}

		u.setAnnotation(unit.getAnnotation());

		return u;
	}

	public static Unit.Kind simplify(UnitDefinition unit) {
		unit = unit.clone().simplify();

		if (unit.isUnitKind()) {
			Unit u = unit.getUnit(0);

			if (u.getScale() == 0 && u.getExponent() == 1
					&& u.getMultiplier() == 1) {
				return unit.getUnit(0).getKind();
			}
		}

		return null;
	}

	public static Annotation createTransformationAnnotation(String name) {
		Annotation annotation = new Annotation();
		XMLAttributes attributes = new XMLAttributes();

		attributes.add("name", name);
		attributes
				.add("xmlns",
						"http://sourceforge.net/projects/microbialmodelingexchange/files/Units");
		annotation.setNonRDFAnnotation(new XMLNode(new XMLTriple(
				"transformation", null, null), attributes));

		return annotation;
	}
}
