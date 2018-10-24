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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.units.Categories;

public class CombaseReader {

	private Map<String, Integer> newAgentIDs = new LinkedHashMap<>();
	private Map<String, Integer> newMatrixIDs = new LinkedHashMap<>();
	private Map<String, MiscXml> newMiscs = new LinkedHashMap<>();
	private MiscConversion conversion;

	private List<PmmTimeSeries> result;

	public CombaseReader(final String filename) throws FileNotFoundException, IOException {
		conversion = new MiscConversion();
		result = new ArrayList<>();

		File file = KnimeUtils.getFile(filename);

		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), "UTF-16LE"))) {
				PmmTimeSeries data;

				while ((data = step(reader)) != null) {
					result.add(data);
				}
			}

			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				PmmTimeSeries data;

				while ((data = stepNew(reader)) != null) {
					result.add(data);
				}
			}
		}
	}

	public List<PmmTimeSeries> getResult() {
		return result;
	}

	private PmmTimeSeries stepNew(BufferedReader reader) throws IOException {
		// initialize next time series
		PmmTimeSeries next = new PmmTimeSeries();

		while (true) {
			String line = reader.readLine();

			if (line == null) {
				return null;
			}

			if (line.trim().isEmpty() && next.getCombaseId() != null) {
				return next;
			}

			if (!line.contains(",")) {
				continue;
			}

			String key = line.substring(0, line.indexOf(",")).trim();
			String data = line.substring(line.indexOf(",") + 1).trim();

			// fetch record id
			if (key.equals("ComBase ID")) {
				next.setCombaseId(data);
				continue;
			}

			// fetch organism
			if (key.equals("Organism")) {
				// next.setAgentDetail( token[ 1 ] );
				setAgent(next, data);
				continue;
			}

			// fetch environment
			if (key.equals("Matrix")) {
				// next.setMatrixDetail(token[1]);
				setMatrix(next, data);
				continue;
			}

			// fetch temperature
			if (key.startsWith("Temperature(")) {
				Double value = parse(data);
				// next.setTemperature(value);
				next.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID, AttributeUtilities.ATT_TEMPERATURE,
						AttributeUtilities.ATT_TEMPERATURE, value,
						Arrays.asList(Categories.getTempCategory().getName()),
						Categories.getTempCategory().getStandardUnit());
				continue;
			}

			// fetch pH
			if (key.equals("pH")) {
				Double value = parse(data);
				// next.setPh(value);
				next.addMisc(AttributeUtilities.ATT_PH_ID, AttributeUtilities.ATT_PH, AttributeUtilities.ATT_PH, value,
						Arrays.asList(Categories.getPhCategory().getName()), Categories.getPhUnit());
				continue;
			}

			// fetch water activity
			if (key.equals("Aw")) {
				Double value = parse(data);
				// next.setWaterActivity(value);
				next.addMisc(AttributeUtilities.ATT_AW_ID, AttributeUtilities.ATT_AW, AttributeUtilities.ATT_AW, value,
						Arrays.asList(Categories.getAwCategory().getName()), Categories.getAwUnit());
				continue;
			}

			// fetch conditions
			if (key.equals("Conditions")) {
				PmmXmlDoc xml = combase2XMLNew(data);
				next.addMiscs(xml);
				continue;
			}

			if (key.equals("Max.rate(logc.conc/h)")) {
				next.setMaximumRate(parse(data));
				continue;
			}

			if (key.equals("Logcs:")) {
				while (true) {
					line = reader.readLine();

					if (line == null)
						return next;

					if (!line.contains(",")) {
						break;
					}

					String dataPoint = line.substring(line.indexOf(",") + 1).trim();
					double t = parse(dataPoint.substring(0, dataPoint.indexOf(",")).trim());
					double logc = parse(dataPoint.substring(dataPoint.indexOf(",") + 1).trim());

					if (Double.isNaN(t) || Double.isNaN(logc)) {
						continue;
					}

					next.add(t, Categories.getTimeCategory().getStandardUnit(), logc,
							Categories.getConcentrationCategories().get(0).getStandardUnit());
				}
				break;
			}
		}

		return next;
	}

	private PmmTimeSeries step(BufferedReader reader) throws IOException {
		// initialize next time series
		PmmTimeSeries next = new PmmTimeSeries();

		while (true) {
			String line = reader.readLine();

			if (line == null) {
				return null;
			}

			if (line.trim().isEmpty() && next.getCombaseId() != null) {
				return next;
			}

			// split up token
			String[] token = line.split("\t");

			if (token.length < 2)
				continue;

			if (token[0].isEmpty())
				continue;

			for (int i = 0; i < token.length; i++) {
				// token[i] =
				// token[i].replaceAll("[^a-zA-Z0-9° \\.\\(\\)_/\\+\\-\\*,:]",
				// "");
				token[i] = token[i].replaceAll("\"", "");
			}
			String key = token[0].toLowerCase().trim();
			// utf16lemessage[0] = (byte)0xFF; utf16lemessage[1] = (byte)0xFE;
			if (key.length() > 1 && key.charAt(0) == 65279)
				key = key.substring(1);

			// fetch record id
			if (key.equals("recordid")) {
				next.setCombaseId(token[1]);
				continue;
			}

			// fetch organism
			if (key.equals("organism")) {
				// next.setAgentDetail( token[ 1 ] );
				setAgent(next, token[1]);
				continue;
			}

			// fetch environment
			if (key.equals("environment")) {
				// next.setMatrixDetail(token[1]);
				setMatrix(next, token[1]);
				continue;
			}

			// fetch temperature
			if (key.equals("temperature")) {
				int pos = token[1].indexOf(" ");
				if (!token[1].endsWith(" °C"))
					throw new PmmException("Temperature unit must be [°C]");
				Double value = parse(token[1].substring(0, pos));
				// next.setTemperature(value);
				next.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID, AttributeUtilities.ATT_TEMPERATURE,
						AttributeUtilities.ATT_TEMPERATURE, value,
						Arrays.asList(Categories.getTempCategory().getName()),
						Categories.getTempCategory().getStandardUnit());
				continue;
			}

			// fetch pH
			if (key.equals("ph")) {
				Double value = parse(token[1]);
				// next.setPh(value);
				next.addMisc(AttributeUtilities.ATT_PH_ID, AttributeUtilities.ATT_PH, AttributeUtilities.ATT_PH, value,
						Arrays.asList(Categories.getPhCategory().getName()), Categories.getPhUnit());
				continue;
			}

			// fetch water activity
			if (key.equals("water activity")) {
				Double value = parse(token[1]);
				// next.setWaterActivity(value);
				next.addMisc(AttributeUtilities.ATT_AW_ID, AttributeUtilities.ATT_AW, AttributeUtilities.ATT_AW, value,
						Arrays.asList(Categories.getAwCategory().getName()), Categories.getAwUnit());
				continue;
			}

			// fetch conditions
			if (key.equals("conditions")) {
				PmmXmlDoc xml = combase2XML(token[1]);
				next.addMiscs(xml);
				continue;
			}

			if (key.equals("maximum rate")) {
				next.setMaximumRate(parse(token[1]));
				continue;
			}

			if (key.startsWith("time") && token[1].equals("logc")) {
				if (!key.endsWith("(h)"))
					throw new IOException("Time unit must be [h].");
				while (true) {
					line = reader.readLine();
					if (line == null)
						return next;
					if (line.replaceAll("\\t\"", "").isEmpty())
						break;
					token = line.split("\t");
					for (int i = 0; i < token.length; i++) {
						token[i] = token[i].replaceAll("[^a-zA-Z0-9° \\.\\(\\)/,]", "");
					}
					if (token.length < 2) {
						break;
					}
					double t = parse(token[0]);
					double logc = parse(token[1]);
					if (Double.isNaN(t) || Double.isNaN(logc)) {
						continue;
					}
					next.add(t, Categories.getTimeCategory().getStandardUnit(), logc,
							Categories.getConcentrationCategories().get(0).getStandardUnit());
				}
				break;
			}
		}

		return next;
	}

	private static double parse(String num) {
		double n = Double.NaN;

		num = num.toLowerCase();
		num = num.trim();
		if (num.equals("no growth"))
			return 0;

		try {
			num = num.replaceAll("[a-zA-Z\\(\\)\\s]", "");
			num = num.replaceAll(",", ".");
			n = Double.valueOf(num);
		} catch (Exception e) {
		}

		return n;
	}

	private PmmXmlDoc combase2XMLNew(String misc) {
		PmmXmlDoc result = null;
		if (misc != null) {
			result = new PmmXmlDoc();
			for (String s : misc.split(";")) {

				int valueSep = s.indexOf(':');
				String name = null;
				Double value = null;

				if (valueSep != -1) {
					String valueString = s.substring(valueSep + 1).trim();

					name = s.substring(0, valueSep).trim();

					if (valueString.charAt(valueString.length() - 1) == ')') {
						int unitSep = valueString.lastIndexOf('(');
						String unitString = valueString.substring(unitSep + 1, valueString.length() - 1).trim();

						valueString = valueString.replace(unitString, "").trim();
					}

					value = parse(valueString);
				} else {
					name = s;
					value = 1.0;
				}

				// ersetzen mehrerer Spaces im Text durch lediglich eines, Bsp.:
				// "was ist los?" -> "was ist los?"
				String description = name.trim().replaceAll(" +", " ");
				MiscXml mx = getMiscXml(description, value);
				// new MiscXml(newIDs.get(description),
				// getCombaseName(description), description, dbl, unit);
				result.add(mx);
			}
		}
		return result;
	}

	private PmmXmlDoc combase2XML(String misc) {
		PmmXmlDoc result = null;
		if (misc != null) {
			result = new PmmXmlDoc();
			List<String> conds = condSplit(misc);
			for (int i = 0; i < conds.size(); i++) {
				String val = conds.get(i).trim();
				int index = val.indexOf(':');
				int index2 = 0;
				// String unit = null;
				Double dbl = null;
				if (index >= 0) {
					try {
						dbl = Double.parseDouble(val.substring(index + 1));
						if (val.charAt(index - 1) == ')') {
							for (index2 = index - 1; index2 >= 0 && val.charAt(index2) != '('; index2--) {
								;
							}
							// unit = val.substring(index2 + 1, index - 1);
							val = val.substring(0, index2);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					dbl = 1.0;
				}
				// ersetzen mehrerer Spaces im Text durch lediglich eines, Bsp.:
				// "was ist los?" -> "was ist los?"
				String description = val.trim().replaceAll(" +", " ");
				MiscXml mx = getMiscXml(description, dbl);
				// new MiscXml(newIDs.get(description),
				// getCombaseName(description), description, dbl, unit);
				result.add(mx);
			}
		}
		return result;
	}

	private void setMatrix(PmmTimeSeries next, String matrixname) {
		Integer id = null;
		String matrixdetail = null;
		int index = matrixname.indexOf("(");
		if (index > 0) {
			matrixdetail = matrixname.substring(index).trim();
			matrixname = matrixname.substring(0, index).trim();
		}
		if (!newMatrixIDs.containsKey(matrixname)) {
			id = DBKernel.getID("Matrices", "Matrixname", matrixname);
			if (id == null) {
				System.err.println(matrixname + "... unknown Matrix ID...");
				id = MathUtilities.getRandomNegativeInt();
			}
			newMatrixIDs.put(matrixname, id);
		} else
			id = newMatrixIDs.get(matrixname);
		matrixdetail = id < 0 ? matrixname + " (" + matrixdetail + ")" : matrixdetail;
		next.setMatrix(id, id < 0 ? null : matrixname, matrixdetail, null);
	}

	private void setAgent(PmmTimeSeries next, String agentsname) {
		Integer id = null;
		if (!newAgentIDs.containsKey(agentsname)) {
			id = DBKernel.getID("Agenzien", "Agensname", agentsname);
			if (id == null) {
				System.err.println(agentsname + "... unknown Agens ID...");
				id = MathUtilities.getRandomNegativeInt();
			}
			newAgentIDs.put(agentsname, id);
		} else
			id = newAgentIDs.get(agentsname);
		next.setAgent(id, id < 0 ? null : agentsname, id < 0 ? agentsname : null, null);
	}

	private MiscXml getMiscXml(String description, Double dbl) {
		if (!newMiscs.containsKey(description)) {
			MiscXml m = conversion.combaseToPmm(description);
			Integer id = (Integer) DBKernel.getValue(null, "SonstigeParameter", "Parameter", m.name, "ID");

			m.id = id;
			newMiscs.put(description, m);
		}

		MiscXml misc = new MiscXml(newMiscs.get(description));

		misc.value = dbl;

		return misc;
	}

	private List<String> condSplit(final String misc) {
		if (misc == null) {
			return null;
		}
		List<String> result = new ArrayList<>();
		StringTokenizer tok = new StringTokenizer(misc, ",");
		int openParenthesis = 0;
		while (tok.hasMoreTokens()) {
			String nextToken = tok.nextToken();
			if (openParenthesis > 0) {
				nextToken = result.get(result.size() - 1) + "," + nextToken;
				result.remove(result.size() - 1);
			}
			result.add(nextToken);
			openParenthesis = 0;
			int index = -1;
			while ((index = nextToken.indexOf("(", index + 1)) >= 0) {
				openParenthesis++;
			}
			while ((index = nextToken.indexOf(")", index + 1)) >= 0) {
				openParenthesis--;
			}
		}
		return result;
	}
}
