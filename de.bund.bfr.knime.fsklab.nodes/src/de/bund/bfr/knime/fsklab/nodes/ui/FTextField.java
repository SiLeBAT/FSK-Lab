package de.bund.bfr.knime.fsklab.nodes.ui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class FTextField extends JTextField {

  private static final long serialVersionUID = 5362866566567567932L;

  public FTextField() {
    this(false);
  }

  public FTextField(boolean mandatory) {
    super(30);

    Color borderColor = mandatory ? UIUtils.RED : UIUtils.BLUE;
    Border matteBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor);
    Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    Border compoundBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
    setBorder(compoundBorder);

    setFont(UIUtils.FONT);
    setBackground(UIUtils.WHITE);
  }
}
