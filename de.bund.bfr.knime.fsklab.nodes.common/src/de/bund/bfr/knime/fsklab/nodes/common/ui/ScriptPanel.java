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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;

import org.fife.ui.rtextarea.RTextScrollPane;

/** JPanel with an R script */
public class ScriptPanel extends FPanel {

	private static final long serialVersionUID = -4493061426461816058L;
	private final RSnippetTextArea textArea;
	private JTree scriptTree;

	public ScriptPanel(final String title, boolean copiable) {
		setLayout(new BorderLayout());
		setName(title);
		textArea = new RSnippetTextArea();
		textArea.setLineWrap(true);

		if (copiable) {
			RTextScrollPane textPane = new RTextScrollPane(textArea);
			textPane.setFoldIndicatorEnabled(true);
			textPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			
			add(textPane, BorderLayout.CENTER);

			JButton copyButton = UIUtils.createCopyButton();
			copyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(new StringSelection(textArea.getText()), null);
				}
			});
			add(copyButton, BorderLayout.EAST);
		}
	}

	public ScriptPanel(final String title, final String script, final boolean editable, boolean copiable) {
		setLayout(new BorderLayout());
		setName(title);
		textArea = new RSnippetTextArea();
		textArea.setLineWrap(true);
		textArea.setText(script);
		textArea.setEditable(editable);
		textArea.setEnabled(editable);
		
		RTextScrollPane textPane = new RTextScrollPane(textArea);
		textPane.setFoldIndicatorEnabled(true);
		textPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(textPane, BorderLayout.CENTER);

		if (copiable) {
			JButton copyButton = UIUtils.createCopyButton();
			copyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(new StringSelection(textArea.getText()), null);
				}
			});
			add(copyButton, BorderLayout.EAST);
		}
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
