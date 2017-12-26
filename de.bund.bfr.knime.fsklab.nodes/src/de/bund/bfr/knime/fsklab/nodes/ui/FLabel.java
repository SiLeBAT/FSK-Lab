package de.bund.bfr.knime.fsklab.nodes.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/** FSK label. */
public class FLabel extends JLabel {

  private static final long serialVersionUID = -4517449685604001603L;

  public FLabel(String text) {
    super(text);

    setForeground(UIUtils.WHITE);
    setBackground(UIUtils.BLUE);
    setOpaque(true);
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setFont(UIUtils.BOLD_FONT);
  }
}
