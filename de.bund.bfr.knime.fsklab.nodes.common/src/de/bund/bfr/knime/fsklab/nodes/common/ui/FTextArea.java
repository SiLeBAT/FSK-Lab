package de.bund.bfr.knime.fsklab.nodes.common.ui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class FTextArea extends JTextArea {

  private static final long serialVersionUID = -849829257117221319L;

  public FTextArea() {
    this(false);
  }

  public FTextArea(boolean mandatory) {
    super(5, 30);

    setLineWrap(true);

    Color borderColor = mandatory ? UIUtils.RED : UIUtils.BLUE;
    Border matteBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor);
    Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    Border compoundBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
    setBorder(compoundBorder);

    setFont(UIUtils.FONT);
    setBackground(UIUtils.WHITE);
  }
}
