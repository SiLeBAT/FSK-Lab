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
package de.bund.bfr.knime.fsklab.nodes.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.knime.core.node.util.CheckUtils;


public class AutoSuggestField extends JComboBox<String> {

  private static final long serialVersionUID = -4710438233878687284L;

  private List<String> list;

  private boolean shouldHide;

  /**
   * Creates an AutoSuggestField with the specified number of columns that contains the elements in
   * the specified Set.
   * 
   * @param columns the number of columsn to use to calculate the preferred width; if columns is set
   *        to zero, the preferred width will be whatever naturally results from the component
   *        implementation
   * @param values a List of strings to insert into the field. Must have the same length as
   *        comments.
   * @param comments a List of comments of every value. Must have the same length as values.
   */
  public AutoSuggestField(final int columns, final List<String> values,
      final List<String> comments, final boolean isMandatory) {

    CheckUtils.checkArgumentNotNull(values);
    CheckUtils.checkArgumentNotNull(comments);
    if (values.size() != comments.size()) {
      throw new IllegalArgumentException(
          "[AutoSuggestField] values and comments must be of same length");
    }

    list = values;
    shouldHide = false;

    createUI(columns, comments, isMandatory);
  }

  private void createUI(final int columns, final List<String> comments,
      final boolean isMandatory) {

    setEditable(true);
    setSelectedIndex(-1);
    setRenderer(new ComboBoxToolTipRenderer(comments));
    // setPreferredSize(getPreferredSize());
    setPreferredSize(new Dimension(100, getPreferredSize().height));

    // Set comment of current selected entry as tool tip of the field
    addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex > -1) {
          setToolTipText(comments.get(selectedIndex));
        }
      }
    });

    Color borderColor = isMandatory ? UIUtils.RED : UIUtils.BLUE;
    setBorder(BorderFactory.createLineBorder(borderColor));

    // Set field
    JTextField field = (JTextField) getEditor().getEditorComponent();
    field.setText("");
    field.setColumns(columns);
    field.addKeyListener(new Listener());


    // Add items
    list.forEach(this::addItem);
  }

  private class Listener implements KeyListener {

    @Override
    public void keyPressed(KeyEvent event) {

      JTextField textField = (JTextField) event.getComponent();
      String query = textField.getText();

      shouldHide = false;

      switch (event.getKeyCode()) {

        case KeyEvent.VK_RIGHT:

          String lowerCaseText = query.toLowerCase();
          for (String entry : list) {
            if (entry.toLowerCase().startsWith(lowerCaseText)) {
              textField.setText(entry);
              return;
            }
          }
          break;

        case KeyEvent.VK_ENTER:
          if (!list.contains(query)) {
            list.add(query);
            setSuggestionModel(getSuggestedModel(query), query);
          }

          shouldHide = true;
          break;

        case KeyEvent.VK_ESCAPE:
          shouldHide = true;
          break;

        default:
          break;
      }

    }

    @Override
    public void keyReleased(KeyEvent event) {}

    @Override
    public void keyTyped(KeyEvent event) {
      EventQueue.invokeLater(() -> {

        String query = ((JTextField) event.getComponent()).getText();

        if (query.isEmpty()) {
          ComboBoxModel<String> model = new DefaultComboBoxModel<>(new Vector<>(list));
          setSuggestionModel(model, "");
          hidePopup();
        } else {

          ComboBoxModel<String> model = getSuggestedModel(query);
          if (model.getSize() == 0 || shouldHide) {
            hidePopup();
          } else {
            setSuggestionModel(model, query);
            showPopup();
          }
        }
      });
    }

    /**
     * @param text Query text.
     * @return ComboBoxModel with the entries matching the passed text.
     */
    private ComboBoxModel<String> getSuggestedModel(String text) {

      DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

      String lowercaseText = text.toLowerCase();

      for (String entry : list) {
        if (entry.toLowerCase().startsWith(lowercaseText)) {
          model.addElement(entry);
        }
      }

      return model;
    }

    private void setSuggestionModel(ComboBoxModel<String> model, String query) {
      setModel(model);
      setSelectedIndex(-1);
      ((JTextField) getEditor().getEditorComponent()).setText(query);
    }
  }
}
