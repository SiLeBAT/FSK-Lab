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
package de.bund.bfr.knime.fsklab.nodes.common.ui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTree;
import org.fife.ui.rtextarea.RTextScrollPane;

/** JPanel with an R script */
public class ScriptPanel extends JPanel {

  private static final long serialVersionUID = -4493061426461816058L;
  private final RSnippetTextArea textArea;
  private JTree scriptTree;

  public ScriptPanel(final String title) {
    super(new BorderLayout());
    setName(title);
    textArea = new RSnippetTextArea();
    textArea.setLineWrap(true);
    add(new RTextScrollPane(textArea), BorderLayout.CENTER);

  }

  public ScriptPanel(final String title, final String script, final boolean editable) {
    super(new BorderLayout());
    setName(title);
    textArea = new RSnippetTextArea();
    textArea.setLineWrap(true);
    textArea.setText(script);
    textArea.setEditable(editable);
    add(new RTextScrollPane(textArea));
  }

  public String getText() {
    return textArea.getText();
  }


  public void setText(String text) {
    textArea.setText(text);
  }

  public JTree getScriptTree() {
    return scriptTree;
  }

  public void setScriptTree(JTree scriptTree) {
    this.scriptTree = scriptTree;
    add(scriptTree, BorderLayout.WEST);
  }



}
