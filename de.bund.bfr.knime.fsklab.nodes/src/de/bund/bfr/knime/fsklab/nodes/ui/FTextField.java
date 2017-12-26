package de.bund.bfr.knime.fsklab.nodes.ui;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FTextField extends JTextField {

  private static final long serialVersionUID = 5362866566567567932L;

  public FTextField() {
    super(30);

    Border matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.BLUE);
    Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    Border compoundBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
    setBorder(compoundBorder);

    setFont(UIUtils.FONT);
    setBackground(UIUtils.WHITE);
  }
}
