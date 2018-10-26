/*
 ***************************************************************************************************
 * Copyright (c) 2018 Federal Institute for Risk Assessment (BfR), Germany
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComboBox;

public class FComboBox extends JComboBox<String> {

  private static final long serialVersionUID = 7640357309488676143L;

  /**
   * Creates a FComboBox that contains the elements in the specified list. The combo box will not
   * have tooltips for the entries. This is a utility constructor for
   * <code>FComboBox(values, null)</code>.
   * 
   * @param values list of strings to insert into the combo box
   */
  public FComboBox(final List<String> values) {
    this(values, null);
  }

  /**
   * Creates a FComboBox that contains the elements in the specified list. Every entry contains a
   * tooltip with the specified comments.
   * 
   * @param values list of entries to insert into the combo box. Must have the same length as
   *        comments.
   * @param comments a List of comments of every value. Must have the same length as values.
   */
  public FComboBox(final List<String> values, final List<String> comments) {
    super(values.toArray(new String[values.size()]));
    setSelectedIndex(-1);

    if (comments != null && comments.size() == values.size()) {
      setRenderer(new ComboBoxToolTipRenderer(comments));
      addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          int selectedIndex = getSelectedIndex();
          if (selectedIndex != -1) {
            setToolTipText(comments.get(selectedIndex));
          }
        }
      });
    }
  }
}
