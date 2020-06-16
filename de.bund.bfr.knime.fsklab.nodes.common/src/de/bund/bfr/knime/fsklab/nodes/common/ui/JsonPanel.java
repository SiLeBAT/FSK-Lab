package de.bund.bfr.knime.fsklab.nodes.common.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class JsonPanel extends JPanel {

	private static final long serialVersionUID = 6805607008635433977L;
	private final RSyntaxTextArea textArea;

	public JsonPanel(final String title, final String json) {
		setLayout(new BorderLayout());
		setName(title);

		textArea = new RSyntaxTextArea(20, 60);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
		textArea.setCodeFoldingEnabled(true);
		textArea.setText(json);
		textArea.setEditable(false);

		RTextScrollPane textPane = new RTextScrollPane(textArea);
		textPane.setFoldIndicatorEnabled(true);
		textPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		textPane.setBorder(null);

		add(textPane, BorderLayout.CENTER);
	}

	public String getJson() {
		return textArea.getText();
	}

	public void setJson(String json) {
		textArea.setText(json);
	}
}
