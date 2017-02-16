/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.knime.core.node.NodeLogger;

import de.bund.bfr.knime.fsklab.nodes.rsnippet.RSnippetDocument;

/**
 * A text area for the R snippet expression.
 * 
 * @author Heiko Hofer.
 */
@SuppressWarnings("serial")
public class RSnippetTextArea extends RSyntaxTextArea {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(RSnippetTextArea.class);

  /** Create a new component. */
  public RSnippetTextArea() {

    // initial text != null causes a NullPointerException
    super(new RSnippetDocument(), null, 20, 60);

    setDocument(new RSnippetDocument());
    try {
      applySyntaxColors();
    } catch (Exception e) {
      LOGGER.debug(e.getMessage(), e);
    }

    setSyntaxEditingStyle(RSnippetDocument.SYNTAX_STYLE_R);
  }

  private void applySyntaxColors() throws IOException {
    Package pack = RSnippetTextArea.class.getPackage();
    String base = pack.getName().replace(".", "/") + "/";
    URL url = getClass().getClassLoader().getResource(base + "r_syntax_style.xml");
    try (InputStream in = url.openStream()) {
    	Theme theme = Theme.load(in);
    	theme.apply(this);
    }
  }
}
