package de.bund.bfr.knime.fsklab.nodes.common.ui;

import java.awt.Color;
import javax.swing.AbstractSpinnerModel;
import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/** FSK Spinner. */
public class FSpinner extends JSpinner {

  // serialVersionUID
  private static final long serialVersionUID = 7141736401534090284L;

  public FSpinner(AbstractSpinnerModel model, boolean isMandatory) {

    super(model);

    JTextField textField = (JTextField) ((DefaultEditor) getEditor()).getTextField();
    textField.setColumns(5);
    textField.setFont(UIUtils.FONT);
    textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    Color borderColor = isMandatory ? UIUtils.RED : UIUtils.BLUE;
    setBorder(BorderFactory.createLineBorder(borderColor));
  }
}
