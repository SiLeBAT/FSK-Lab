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
package de.bund.bfr.knime.pcml.port;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlTokenSource;
import org.dmg.pmml.PMMLDocument;


/**
 * Utility class to save and display a PCML document.
 * 
 * @author Heiko Hofer
 */
public final class PCMLFormatter {
    private static final XmlOptions TEXT_SAVE_XML_OPTIONS;

    static {
        Map<String, String> suggestedNamespaces = new HashMap<String, String>();
        suggestedNamespaces.put("http://www.w3.org/2001/XMLSchema-instance",
                "xsi");
        suggestedNamespaces.put(PMMLDocument.type.getDocumentElementName()
                .getNamespaceURI(), "");
        TEXT_SAVE_XML_OPTIONS = new XmlOptions();
        TEXT_SAVE_XML_OPTIONS.setSaveOuter();
        TEXT_SAVE_XML_OPTIONS.setSaveAggressiveNamespaces();
        TEXT_SAVE_XML_OPTIONS.setSavePrettyPrint();
        TEXT_SAVE_XML_OPTIONS.setCharacterEncoding("UTF-8");
        TEXT_SAVE_XML_OPTIONS.setSaveSuggestedPrefixes(suggestedNamespaces);
    }

    private PCMLFormatter() {
        // utility class with static methods only
    }

    /**
     * Generates a nicely formatted string out of a PMML fragment.
     *
     * @param xmlObject the xml object to format into a string.
     * @return the string representation of the xml object
     */
    public static String xmlText(final XmlObject xmlObject) {
        return xmlObject.xmlText(TEXT_SAVE_XML_OPTIONS);
    }

    /**
     * @param xml the XMLTokenSource to be written
     * @param out the output stream to write to
     * @throws IOException if the xml cannot be written to the stream
     */
    public static void save(final XmlTokenSource xml, final OutputStream out)
            throws IOException {
        xml.save(out, TEXT_SAVE_XML_OPTIONS);
    }

    /**
     * @return  Xml options for a nice output of a xml document.
     */
    public static XmlOptions getOptions() {
        return TEXT_SAVE_XML_OPTIONS;
    }

}

