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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

public class CombaseWriter {

	private LinkedList<PmmTimeSeries> buffer;
	private String filename;
	private MiscConversion conversion;

	public CombaseWriter(final String filename) {
		conversion = new MiscConversion();
		this.filename = filename;
		buffer = new LinkedList<>();
	}

	public void add(final PmmTimeSeries candidate) throws PmmException {

		if (candidate == null)
			throw new PmmException("Candidate must not be null.");

		buffer.add(candidate);
	}

	public void flush() throws UnsupportedEncodingException, FileNotFoundException, IOException, PmmException {
		flush16le(); // "UTF-16LE"
	}

	public void flush16le() throws UnsupportedEncodingException, FileNotFoundException, IOException, PmmException {
		StringBuffer buf = new StringBuffer();
		for (PmmTimeSeries candidate : buffer) {
			String organism = "";
			String matrix = "";

			if (candidate.getAgentName() != null) {
				organism = candidate.getAgentName();
			} else if (candidate.getAgentDetail() != null) {
				organism = candidate.getAgentDetail();
			}

			if (candidate.getMatrixName() != null) {
				matrix = candidate.getMatrixName();
			} else if (candidate.getMatrixDetail() != null) {
				matrix = candidate.getMatrixDetail();
			}

			if (candidate.hasCombaseId()) {
				buf.append("\"RecordID\"\t\"" + candidate.getCombaseId() + "\"\n");
			}

			if (candidate.hasAgent()) {
				buf.append("\"Organism\"\t\"" + organism + "\"\n");
			}

			if (candidate.hasMatrix()) {
				buf.append("\"Environment\"\t\"" + matrix + "\"\n");
			}

			if (candidate.hasTemperature()) {
				buf.append("\"Temperature\"\t\"" + candidate.getTemperature() + " °C\"\n");
			}

			if (candidate.hasPh()) {
				buf.append("\"pH\"\t\"" + candidate.getPh() + "\"\n");
			}

			if (candidate.hasWaterActivity()) {
				buf.append("\"Water Activity\"\t\"" + candidate.getWaterActivity() + "\"\n");
			}

			if (candidate.hasMisc()) {
				PmmXmlDoc doc = candidate.getMisc();
				if (doc != null) {
					// String xmlStr = doc.toXmlString();
					String cb = xml2Combase(doc);
					if (cb != null)
						buf.append("\"Conditions\"\t\"" + cb + "\"\n");
				}
			}

			/*
			 * if( candidate.hasMaximumRate() ) { buf.append(
			 * "\"Maximum Rate\"\t\""+candidate.getMaximumRate()+"\"\n" ); }
			 * 
			 * if( candidate.hasDoublingTime() ) { buf.append(
			 * "\"Doubling Time (h)\"\t\""+candidate.getDoublingTime()+"\"\n" );
			 * }
			 */

			buf.append("\"Time (h)\"\t\"logc\"\n");

			if (!candidate.isEmpty()) {
				PmmXmlDoc tsXmlDoc = candidate.getTimeSeries();
				for (PmmXmlElementConvertable el : tsXmlDoc.getElementSet()) {
					if (el instanceof TimeSeriesXml) {
						TimeSeriesXml tsx = (TimeSeriesXml) el;
						buf.append("\"" + tsx.time + "\"\t\"" + tsx.concentration + "\"\n");
					}
				}
			}

			buf.append("\n\n\n");
		}
		OutputStream out = new FileOutputStream(KnimeUtils.getFile(filename));
		out.write(encodeString(buf.toString()));
		out.close();
	}

	public static byte[] encodeString(final String message) {

		byte[] tmp = null;
		try {
			tmp = message.getBytes("UTF-16LE");
		} catch (UnsupportedEncodingException e) {
			// should not possible
			AssertionError ae = new AssertionError("Could not encode UTF-16LE");
			ae.initCause(e);
			throw ae;
		}

		// use brute force method to add BOM
		byte[] utf16lemessage = new byte[2 + tmp.length];
		utf16lemessage[0] = (byte) 0xFF;
		utf16lemessage[1] = (byte) 0xFE;
		System.arraycopy(tmp, 0, utf16lemessage, 2, tmp.length);
		return utf16lemessage;
	}

	private String xml2Combase(PmmXmlDoc misc) {
		List<Integer> tempPhAwIds = Arrays.asList(AttributeUtilities.ATT_TEMPERATURE_ID, AttributeUtilities.ATT_PH_ID,
				AttributeUtilities.ATT_AW_ID);
		String result = null;
		if (misc != null) {
			result = "";
			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				if (el instanceof MiscXml) {
					MiscXml mx = (MiscXml) el;

					if (tempPhAwIds.contains(mx.id)) {
						continue;
					}

					if (!result.isEmpty())
						result += ", ";
					result += conversion.pmmToCombase(mx);
					if (mx.unit != null && !mx.unit.isEmpty())
						result += " (" + mx.unit + ")";
					if (mx.value != null && !Double.isNaN(mx.value))
						result += ":" + mx.value;
				}
			}
		}
		return result;
	}
}
