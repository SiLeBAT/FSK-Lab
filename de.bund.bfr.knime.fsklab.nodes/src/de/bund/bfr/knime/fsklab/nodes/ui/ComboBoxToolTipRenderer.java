package de.bund.bfr.knime.fsklab.nodes.ui;

import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import org.knime.core.node.util.CheckUtils;

/**
 * Custom ListCellRenderer that shows a different tool tip for every entry in a combo box.
 */
public class ComboBoxToolTipRenderer extends DefaultListCellRenderer {

  private static final long serialVersionUID = -4586180696576409266L;
  private List<String> toolTips;

  public ComboBoxToolTipRenderer(List<String> toolTips) {
    CheckUtils.checkArgumentNotNull(toolTips);
    this.toolTips = toolTips;
  }

  @Override
  public Component getListCellRendererComponent(JList<?> list, Object value, int index,
      boolean isSelected, boolean cellHasFocus) {
    JComponent comp = (JComponent) super.getListCellRendererComponent(list, value, index,
        isSelected, cellHasFocus);

    if (index > -1 && value != null) {
      list.setToolTipText(toolTips.get(index));
    }

    return comp;
  }
}