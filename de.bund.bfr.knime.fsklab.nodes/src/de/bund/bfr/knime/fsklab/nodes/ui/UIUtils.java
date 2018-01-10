/*
 ***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import org.knime.core.util.FileUtil;
import org.knime.core.util.SimpleFileFilter;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.swing.UI;

public class UIUtils {

  private UIUtils() {}

  /** Creates a panel with the names of a number of libraries. */
  public static JPanel createLibrariesPanel(final Collection<File> libs) {

    final JPanel panel = new JPanel(new BorderLayout());
    panel.setName("Libraries list");

    final String[] libNames = libs.stream().map(File::getName).toArray(String[]::new);
    final JList<String> list = new JList<>(libNames);
    list.setLayoutOrientation(JList.VERTICAL);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    panel.add(new JScrollPane(list));

    return panel;
  }

  /**
   * Do not use original JTable! On Mac grid lines are not visible.
   * 
   * @return JTable with light gray grid lines.
   */
  public static JTable createTable(final TableModel model) {
    final JTable table = new JTable(model);
    table.setGridColor(Color.lightGray);
    return table;
  }

  public static JButton createAddButton() {
    JButton addButton = new JButton();
    addButton
        .setIcon(new ImageIcon(UIUtils.class.getResource("/img/ic_add_box_black_24dp_1x.png")));
    addButton.setContentAreaFilled(false);
    addButton.setToolTipText("Add");

    return addButton;
  }

  public static JButton createFileUploadButton() {
    JButton addButton = new JButton();
    addButton
        .setIcon(new ImageIcon(UIUtils.class.getResource("/img/ic_file_upload_black_24dp_1x.png")));
    addButton.setContentAreaFilled(false);
    addButton.setToolTipText("File upload");

    return addButton;
  }

  public static JButton createEditButton() {
    JButton addButton = new JButton();
    addButton
        .setIcon(new ImageIcon(UIUtils.class.getResource("/img/ic_mode_edit_black_24dp_1x.png")));
    addButton.setContentAreaFilled(false);
    addButton.setToolTipText("Edit");

    return addButton;
  }

  public static JButton createRemoveButton() {
    JButton addButton = new JButton();
    addButton.setIcon(
        new ImageIcon(UIUtils.class.getResource("/img/ic_remove_circle_black_24dp_1x.png")));
    addButton.setContentAreaFilled(false);
    addButton.setToolTipText("Remove");

    return addButton;
  }


  /**
   * Creates panel to add/remove resource files.
   * 
   * @param parent Parent component
   * @param listModel Model of the {@link JList} displaying the resources.
   */
  public static JPanel createResourcesPanel(final Component parent,
      final DefaultListModel<Path> listModel) {
    JList<Path> list = new JList<>(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    list.setLayoutOrientation(JList.VERTICAL);

    final JButton addButton = UIUtils.createAddButton();
    final JButton removeButton = UIUtils.createRemoveButton();
    final JPanel buttonsPanel = UI.createHorizontalPanel(addButton, removeButton);

    // Initialize chooser before the event so that it remembers the current
    // directory between uses.
    final JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.addChoosableFileFilter(new SimpleFileFilter("txt", "Plain text file"));
    fc.addChoosableFileFilter(new SimpleFileFilter("rdata", "R workspace file"));
    fc.setAcceptAllFileFilterUsed(false); // do not use the AcceptAll FileFilter

    addButton.addActionListener(event -> {
      final int returnVal = fc.showOpenDialog(parent);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        final Path selectedFile = fc.getSelectedFile().toPath();
        if (!listModel.contains(selectedFile)) {
          listModel.addElement(selectedFile);
        }
      }
    });

    removeButton.addActionListener(event -> {
      final int selectedIndex = list.getSelectedIndex();
      if (selectedIndex != -1) {
        listModel.remove(selectedIndex);
      }
    });

    final JPanel northPanel = UI.createNorthPanel(new JScrollPane(list));
    northPanel.add(UI.createCenterPanel(buttonsPanel), BorderLayout.SOUTH);

    return northPanel;
  }

  // BfR main colours
  public static Color WHITE = Color.WHITE;
  public static Color BLUE = new Color(3, 78, 162);
  public static Color LIGHT_GRAY = new Color(204, 204, 204);
  public static Color BLACK = Color.BLACK;

  // BfR secondary colours
  public static Color LIGHT_BLUE = new Color(140, 190, 218);
  public static Color LIGHT_CYAN = new Color(209, 237, 244);
  public static Color OCHER = new Color(241, 199, 83);
  public static Color OCHER_LIGHT = new Color(246, 231, 187);
  public static Color MINT_GREEN = new Color(52, 172, 159);
  public static Color DARK_GRAY = new Color(102, 102, 102);

  public static Font FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
  public static Font BOLD_FONT = new Font(Font.DIALOG, Font.BOLD, 12);

  public static JButton createBrowseButton(String text, JTextField field, int dialogType,
      FileFilter fileFilter) {

    FBrowseButton button = new FBrowseButton(text);

    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser;
        try {
          File file = FileUtil.getFileFromURL(FileUtil.toURL(field.getText()));
          fileChooser = new JFileChooser(file);
        } catch (Exception ex) {
          fileChooser = new JFileChooser();
        }

        fileChooser.setFileFilter(fileFilter);
        fileChooser.setAcceptAllFileFilterUsed(false); // disable the All file filter
        fileChooser.setDialogType(dialogType);

        int response;
        if (dialogType == JFileChooser.OPEN_DIALOG) {
          response = fileChooser.showOpenDialog(button);
        } else {
          response = fileChooser.showSaveDialog(button);
        }

        if (response == JFileChooser.APPROVE_OPTION) {
          field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
      }
    });

    return button;
  }

  /**
   * Create form panel for labels, fields and buttons.
   * <p>
   * Layout: | Label | Field | Button |
   */
  public static FPanel createFormPanel(List<FLabel> labels, List<JTextField> fields,
      List<JButton> buttons) {
    int n = labels.size();

    FPanel leftPanel = new FPanel();
    leftPanel.setLayout(new GridLayout(n, 1, 5, 5));
    labels.forEach(leftPanel::add);

    FPanel centerPanel = new FPanel();
    centerPanel.setLayout(new GridLayout(n, 1, 5, 5));
    fields.forEach(centerPanel::add);

    FPanel rightPanel = new FPanel();
    rightPanel.setLayout(new GridLayout(n, 1, 5, 5));
    buttons.forEach(rightPanel::add);

    FPanel formPanel = new FPanel();
    formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    formPanel.setLayout(new BorderLayout(5, 5));

    formPanel.add(leftPanel, BorderLayout.WEST);
    formPanel.add(centerPanel, BorderLayout.CENTER);
    formPanel.add(rightPanel, BorderLayout.EAST);

    return formPanel;
  }

  /**
   * Create form panel for labels and fields.
   * <p>
   * Layout: | Label | Field |
   */
  public static FPanel createFormPanel(List<FLabel> labels, List<JComponent> components) {

    int n = labels.size();

    FPanel leftPanel = new FPanel();
    leftPanel.setLayout(new GridLayout(n, 1, 5, 5));
    labels.forEach(leftPanel::add);

    FPanel rightPanel = new FPanel();
    rightPanel.setLayout(new GridLayout(n, 1, 5, 5));
    components.forEach(rightPanel::add);

    FPanel formPanel = new FPanel();
    formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    formPanel.setLayout(new BorderLayout(5, 5));
    formPanel.add(leftPanel, BorderLayout.WEST);
    formPanel.add(rightPanel, BorderLayout.CENTER);

    return formPanel;
  }

  public static FPanel createWestPanel(Component component) {
    FPanel northPanel = new FPanel();

    northPanel.setLayout(new BorderLayout());
    northPanel.add(component, BorderLayout.WEST);

    return northPanel;
  }

  /**
   * Return unicode string from property file.
   * <p>
   * If error return ISO-8559-1 string.
   */
  public static String getUnicodeString(String key) {

    /*
     * Resourcebundle is reading strings from property files with ISO-8559-1 encoding even though
     * the files are saved with UTF-8. They need to be converted.
     */
    String latinString = FskPlugin.getDefault().MESSAGES_BUNDLE.getString(key);
    try {
      String unicodeString = new String(latinString.getBytes("ISO-8859-1"), "UTF-8");
      return unicodeString;
    } catch (UnsupportedEncodingException exception) {
      exception.printStackTrace(System.err);
      return latinString;
    }

  }
}
