/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab.nodes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.AbstractSpinnerModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.util.SimpleFileFilter;
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;
import com.gmail.gcolaianni5.jris.engine.JRis;
import com.gmail.gcolaianni5.jris.exception.JRisException;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextArea;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.ui.FixedDateChooser;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.nodes.ui.UTF8Control;
import de.bund.bfr.knime.fsklab.rakip.Assay;
import de.bund.bfr.knime.fsklab.rakip.DataBackground;
import de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod;
import de.bund.bfr.knime.fsklab.rakip.GeneralInformation;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.Hazard;
import de.bund.bfr.knime.fsklab.rakip.ModelEquation;
import de.bund.bfr.knime.fsklab.rakip.ModelMath;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.knime.fsklab.rakip.PopulationGroup;
import de.bund.bfr.knime.fsklab.rakip.Product;
import de.bund.bfr.knime.fsklab.rakip.Scope;
import de.bund.bfr.knime.fsklab.rakip.Simulation;
import de.bund.bfr.knime.fsklab.rakip.StudySample;
import de.bund.bfr.swing.AutoSuggestField;
import de.bund.bfr.swing.StringTextField;
import de.bund.bfr.swing.UI;
import ezvcard.Ezvcard;
import ezvcard.VCard;

public class EditorNodeDialog extends DataAwareNodeDialogPane {

  private final ScriptPanel modelScriptPanel = new ScriptPanel("Model script", "", true);
  private final ScriptPanel paramScriptPanel = new ScriptPanel("Parameters script", "", true);
  private final ScriptPanel vizScriptPanel = new ScriptPanel("Visualization script", "", true);
  private final GeneralInformationPanel generalInformationPanel = new GeneralInformationPanel();
  private final ScopePanel scopePanel = new ScopePanel();
  private final DataBackgroundPanel dataBackgroundPanel = new DataBackgroundPanel();
  private final ModelMathPanel modelMathPanel = new ModelMathPanel();
  private final SimulationPanel simulationPanel = new SimulationPanel();

  /** List model for resource files. */
  private final DefaultListModel<Path> listModel = new DefaultListModel<>();

  private static ResourceBundle bundle =
      ResourceBundle.getBundle("MessagesBundle", new UTF8Control());

  private EditorNodeSettings settings;

  EditorNodeDialog() {

    /*
     * Initialize settings (current values are garbage, need to be loaded from settings/input port).
     */
    settings = new EditorNodeSettings();
    settings.genericModel = new GenericModel();

    // Add ScriptPanels
    addTab(modelScriptPanel.getName(), modelScriptPanel);
    addTab(paramScriptPanel.getName(), paramScriptPanel);
    addTab(vizScriptPanel.getName(), vizScriptPanel);
    addTab("General information", generalInformationPanel, true);
    addTab("Scope", scopePanel, true);
    addTab("Data background", dataBackgroundPanel, true);
    addTab("Model math", modelMathPanel, true);
    addTab("Simulation", simulationPanel, true);
    addTab("Resource files", UIUtils.createResourcesPanel(getPanel(), listModel));

    updatePanels();
  }

  // Update the scripts in the ScriptPanels
  private void updatePanels() {
    modelScriptPanel.getTextArea().setText(settings.modifiedModelScript);
    paramScriptPanel.getTextArea().setText(settings.modifiedParametersScript);
    vizScriptPanel.getTextArea().setText(settings.modifiedVisualizationScript);

    generalInformationPanel.init(settings.genericModel.generalInformation);
    scopePanel.init(settings.genericModel.scope);
    dataBackgroundPanel.init(settings.genericModel.dataBackground);
    modelMathPanel.init(settings.genericModel.modelMath);
    simulationPanel.init(settings.genericModel.simulation);

    listModel.clear();
    settings.resources.forEach(listModel::addElement);
  }

  // --- settings methods ---
  /** Loads settings from input port. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {

    final EditorNodeSettings editorSettings = new EditorNodeSettings();
    try {
      editorSettings.loadSettings(settings);
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException("InvalidSettingsException", exception);
    }

    final FskPortObject inObj = (FskPortObject) input[0];

    /*
     * If input model has not changed (the original scripts stored in settings match the input
     * model).
     */
    if (Objects.equals(editorSettings.originalModelScript, inObj.model)
        && Objects.equals(editorSettings.originalParametersScript, inObj.param)
        && Objects.equals(editorSettings.originalVisualizationScript, inObj.viz)) {
      // Updates settings
      this.settings = editorSettings;
    } else {
      // Discard settings and replace them with input model
      this.settings.originalModelScript = inObj.model;
      this.settings.originalParametersScript = inObj.param;
      this.settings.originalVisualizationScript = inObj.viz;

      this.settings.modifiedModelScript = inObj.model;
      this.settings.modifiedParametersScript = inObj.param;
      this.settings.modifiedVisualizationScript = inObj.viz;

      this.settings.genericModel = inObj.genericModel;

      try {
        Files.list(inObj.workingDirectory).forEach(this.settings.resources::add);
      } catch (IOException exception) {
        throw new NotConfigurableException(exception.getMessage(), exception);
      }
    }

    updatePanels();
  }

  /** Loads settings from saved settings. */
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs)
      throws NotConfigurableException {
    try {
      this.settings.loadSettings(settings);
    } catch (InvalidSettingsException exception) {
      // throw new NotConfigurableException("InvalidSettingsException", exception);
      LOGGER.warn("Settings were not loaded", exception);
    }

    updatePanels();
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    // Save modified scripts to settings
    this.settings.modifiedModelScript = modelScriptPanel.getTextArea().getText();
    this.settings.modifiedParametersScript = paramScriptPanel.getTextArea().getText();
    this.settings.modifiedVisualizationScript = vizScriptPanel.getTextArea().getText();

    // Trim non-empty scripts
    this.settings.modifiedModelScript = StringUtils.trim(this.settings.modifiedModelScript);
    this.settings.modifiedParametersScript =
        StringUtils.trim(this.settings.modifiedParametersScript);
    this.settings.modifiedVisualizationScript =
        StringUtils.trim(this.settings.modifiedVisualizationScript);

    // Save metadata
    this.settings.genericModel.generalInformation = generalInformationPanel.get();
    this.settings.genericModel.scope = scopePanel.get();
    this.settings.genericModel.dataBackground = dataBackgroundPanel.get();
    this.settings.genericModel.modelMath = modelMathPanel.get();
    this.settings.genericModel.simulation = simulationPanel.get();

    // Save resources
    this.settings.resources.clear();
    this.settings.resources.addAll(Collections.list(listModel.elements()));

    this.settings.saveSettings(settings);
  }

  private static final Map<String, Set<String>> vocabs = new HashMap<>();
  static {

    try (
        final InputStream stream = EditorNodeDialog.class
            .getResourceAsStream("/FSKLab_Config_Controlled Vocabularies.xlsx");
        final XSSFWorkbook workbook = new XSSFWorkbook(stream)) {

      final List<String> sheets = Arrays.asList(

          // GeneralInformation controlled vocabularies
          "Rights", "Format", "Software", "Language", "Language written in", "Status",

          // Product controlled vocabularies
          "Product-matrix name", "Product-matrix unit", "Method of production", "Packaging",
          "Product treatment", "Country of origin", "Area of origin", "Fisheries area",

          // Hazard controlled vocabularies
          "Hazard type", "Hazard name", "Hazard unit", "Hazard ind sum", "Laboratory country",

          // PopulationGroup controlled vocabularies
          "Region", "Country",

          // DataBackground controlled vocabularies
          "Laboratory accreditation",

          // Study controlled vocabularies
          "Study Design Type", "Study Assay Measurement Type", "Study Assay Technology Type",
          "Accreditation procedure Ass.Tec", "Study Protocol Type",
          "Study Protocol Parameters Name", "Study Protocol Components Type",

          // StudySample controlled vocabularies
          "Sampling strategy", "Type of sampling program", "Sampling method", "Lot size unit",
          "Sampling point",

          // DietaryAssessmentMethod controlled vocabularies
          "Method. tool to collect data", "Food descriptors",

          // Parameter controlled vocabularies
          "Parameter classification", "Parameter unit", "Parameter type", "Parameter unit category",
          "Parameter data type", "Parameter source", "Parameter subject", "Parameter distribution");

      for (final String sheet : sheets) {
        final Set<String> vocabulary = readVocabFromSheet(workbook, sheet);
        vocabs.put(sheet, vocabulary);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static NodeLogger LOGGER = NodeLogger.getLogger("EditNodeDialog");

  /**
   * Read controlled vocabulary from spreadsheet.
   * 
   * @return Set with controlled vocabulary. If the sheet is not found returns empty set.
   */
  private static Set<String> readVocabFromSheet(final XSSFWorkbook workbook,
      final String sheetname) {

    final XSSFSheet sheet = workbook.getSheet(sheetname);
    if (sheet == null) {
      LOGGER.warn("Spreadsheet not found: " + sheetname);
      return Collections.emptySet();
    }

    final Set<String> vocab = new HashSet<>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0)
        continue;
      final Cell cell = row.getCell(0);
      if (cell == null)
        continue;
      try {
        final String cellValue = cell.getStringCellValue();
        if (StringUtils.isNotBlank(cellValue))
          vocab.add(cellValue);
      } catch (Exception e) {
        // FIXME: A NPE is produced here ...
        // LOGGER.warning("Controlled vocabulary " + sheetname + ": wrong value " +
        // cell);
      }
    }

    return vocab;
  }

  private static class GUIFactory {

    private static FLabel createLabelWithToolTip(String key) {
      String text = bundle.getString(key + "Label");
      String toolTip = bundle.getString(key + "Tooltip");
      return new FLabel(text, toolTip);
    }

    private static JPanel createAdvancedPanel(final JCheckBox checkbox) {

      final JPanel panel = new JPanel();
      panel.setBackground(Color.lightGray);
      panel.add(checkbox);

      return panel;
    }

    /**
     * @param possibleValues Set
     * @return a JComboBox with the passed possible values
     */
    private static JComboBox<String> createComboBox(final Collection<String> possibleValues) {
      final String[] array = possibleValues.stream().toArray(String[]::new);
      final JComboBox<String> comboBox = new JComboBox<>(array);
      comboBox.setSelectedIndex(-1);

      return comboBox;
    }

    /**
     * @param possibleValues Set
     * @return an AutoSuggestField with the passed possible values. The field has 10 columns.
     */
    private static AutoSuggestField createAutoSuggestField(final Set<String> possibleValues) {
      final AutoSuggestField field = new AutoSuggestField(10);
      field.setPossibleValues(possibleValues);
      field.setPreferredSize(new Dimension(100, field.getPreferredSize().height));
      return field;
    }

    /** Creates a JSpinner with 5 columns. */
    private static JSpinner createSpinner(final AbstractSpinnerModel model) {

      final JSpinner spinner = new JSpinner(model);
      ((DefaultEditor) spinner.getEditor()).getTextField().setColumns(5);

      return spinner;
    }

    /**
     * Creates a SpinnerNumberModel for integers with no limits and initial value 0.
     */
    private static SpinnerNumberModel createSpinnerIntegerModel() {
      return new SpinnerNumberModel(0, null, null, 1);
    }

    /**
     * Creates a SpinnerNumberModel for real numbers with no limits and initial value.
     */
    private static SpinnerNumberModel createSpinnerDoubleModel() {
      return new SpinnerNumberModel(0.0, null, null, .01);
    }

    /**
     * Creates a SpinnerNumberModel for percentages (doubles) and initial value 0.0.
     *
     * Has limits 0.0 and 1.0.
     */
    private static SpinnerNumberModel createSpinnerPercentageModel() {
      return new SpinnerNumberModel(0.0, 0.0, 1.0, .01);
    }
  }

  /** Validatable dialogs and panels. */
  private class ValidatableDialog extends JDialog {

    private static final long serialVersionUID = -6572257674130882251L;
    private final JOptionPane optionPane;

    ValidatableDialog(final ValidatablePanel panel, final String dialogTitle) {
      super((Frame) null, true);

      optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_OPTION);
      optionPane.setBackground(UIUtils.WHITE);
      setTitle(dialogTitle);

      // Handle window closing properly
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

      optionPane.addPropertyChangeListener(e -> {

        if (isVisible() && e.getSource() == optionPane
            && e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)
            && optionPane.getValue() != JOptionPane.UNINITIALIZED_VALUE) {

          int value = (int) optionPane.getValue();

          if (value == JOptionPane.YES_OPTION) {
            final List<String> errors = panel.validatePanel();
            if (errors.isEmpty()) {
              dispose();
            } else {
              final String msg = String.join("\n", errors);
              JOptionPane.showMessageDialog(this, msg, "Missing fields", JOptionPane.ERROR_MESSAGE);

              // Reset the JOptionPane's value. If you don't do this, if the user presses
              // the same button next time, no property change will be fired.
              optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
            }
          } else if (value == JOptionPane.NO_OPTION) {
            dispose();
          }
        }
      });

      setContentPane(optionPane);
      setResizable(false);
      pack();
      setLocationRelativeTo(null); // center dialog
      setVisible(true);
    }

    Object getValue() {
      return optionPane.getValue();
    }
  }

  abstract class ValidatablePanel extends JPanel {

    private static final long serialVersionUID = -314660860010487287L;

    ValidatablePanel(LayoutManager layout) {
      super(layout);
    }

    abstract List<String> validatePanel();
  }

  private abstract class EditPanel<T> extends ValidatablePanel {

    private static final long serialVersionUID = 5109496284766147394L;

    public EditPanel(LayoutManager layout) {
      super(layout);
    }

    abstract void init(final T t);

    abstract T get();

    /**
     * @return list of JComponents related to optional properties
     */
    abstract List<JComponent> getAdvancedComponents();

    /**
     * Hide or show the JComponents related to optional properties.
     */
    void toggleMode() {
      final List<JComponent> components = getAdvancedComponents();
      components.forEach(it -> it.setEnabled(!it.isEnabled()));
    }
  }

  /**
   * Panel to create/edit an assay.
   * 
   * <table summary="EditAssayPanel fields">
   * <tr>
   * <td>Name</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Description</td>
   * <td>Optional</td>
   * </tr>
   * </table>
   */
  private class EditAssayPanel extends EditPanel<Assay> {

    private static final long serialVersionUID = -1195181696127795655L;

    private final FTextField nameTextField;
    private final FTextArea descriptionTextArea;

    EditAssayPanel(final boolean isAdvanced) {
      super(new BorderLayout());

      nameTextField = new FTextField(true);
      descriptionTextArea = new FTextArea();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "editor_EditAssayPanel_";

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

      // name
      List<FLabel> labels = Arrays.asList(GUIFactory.createLabelWithToolTip(prefix + "name"));
      List<JComponent> fields = Arrays.asList(nameTextField);
      northPanel.add(UIUtils.createFormPanel(labels, fields));

      // description
      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "description");
        FPanel textAreaPanel =
            UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(descriptionTextArea));
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final Assay assay) {
      if (assay != null) {
        nameTextField.setText(assay.name);
        descriptionTextArea.setText(assay.description);
      }
    }

    @Override
    Assay get() {

      final Assay assay = new Assay();
      assay.name = nameTextField.getText();
      assay.description = descriptionTextArea.getText();

      return assay;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(1);
      if (!nameTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString("editor_EditAssayPanel_nameLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException("Not implemented");
    }
  }

  /**
   * Panel to create/modify a {@link DietaryAssessmentMethod}.
   * 
   * <table summary="EditDietaryAssessmentMethodPanel fields">
   * <tr>
   * <td>Data collection tool</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Non consecutive one day</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Dietary software tool</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Food item number</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Record type</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Food description</td>
   * <td>Optional</td>
   * </tr>
   * </table>
   */
  private class EditDietaryAssessmentMethodPanel extends EditPanel<DietaryAssessmentMethod> {

    private static final long serialVersionUID = -931984426171199928L;

    private final AutoSuggestField dataCollectionToolField;
    private final FTextField nonConsecutiveOneDayField;
    private final FTextField dietarySoftwareToolField;
    private final FTextField foodItemNumberField;
    private final FTextField recordTypeField;
    private final JComboBox<String> foodDescriptionField;

    EditDietaryAssessmentMethodPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      dataCollectionToolField =
          GUIFactory.createAutoSuggestField(vocabs.get("Method. tool to collect data"));
      nonConsecutiveOneDayField = new FTextField(true);
      dietarySoftwareToolField = new FTextField();
      foodItemNumberField = new FTextField();
      recordTypeField = new FTextField();
      foodDescriptionField = GUIFactory.createComboBox(vocabs.get("Food descriptors"));

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      String prefix = "editor_EditDietaryAssessmentMethodPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // data collection tool
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "dataCollectionTool"));
      fields.add(dataCollectionToolField);

      // non consecutive one day
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "nonConsecutiveOneDays"));
      fields.add(nonConsecutiveOneDayField);

      // dietary software tool
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "dietarySoftwareTool"));
        fields.add(dietarySoftwareToolField);
      }

      // food item number
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "foodItemNumber"));
        fields.add(foodItemNumberField);
      }

      // record type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "recordType"));
        fields.add(recordTypeField);
      }

      // food description
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "foodDescription"));
        fields.add(foodDescriptionField);
      }

      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(DietaryAssessmentMethod method) {
      if (method != null) {
        dataCollectionToolField.setSelectedItem(method.collectionTool);
        nonConsecutiveOneDayField.setText(Integer.toString(method.numberOfNonConsecutiveOneDay));
        dietarySoftwareToolField.setText(method.softwareTool);

        if (!method.numberOfFoodItems.isEmpty()) {
          foodItemNumberField.setText(method.numberOfFoodItems.get(0));
        }

        if (!method.recordTypes.isEmpty()) {
          recordTypeField.setText(method.recordTypes.get(0));
        }

        if (!method.foodDescriptors.isEmpty()) {
          foodDescriptionField.setSelectedItem(method.foodDescriptors.get(0));
        }
      }
    }

    @Override
    DietaryAssessmentMethod get() {

      final DietaryAssessmentMethod method = new DietaryAssessmentMethod();
      method.collectionTool = (String) dataCollectionToolField.getSelectedItem();

      final String nonConsecutiveOneDayTextFieldText = nonConsecutiveOneDayField.getText();
      if (StringUtils.isNotBlank(nonConsecutiveOneDayTextFieldText)) {
        try {
          method.numberOfNonConsecutiveOneDay = Integer.parseInt(nonConsecutiveOneDayTextFieldText);
        } catch (final NumberFormatException exception) {
          LOGGER.warn("numberOfNonConsecutiveOneDay", exception);
        }
      }

      method.softwareTool = dietarySoftwareToolField.getText();

      final String foodItemNumber = foodItemNumberField.getText();
      if (!foodItemNumber.isEmpty()) {
        method.numberOfFoodItems.add(foodItemNumber);
      }

      final String recordType = recordTypeField.getText();
      if (!recordType.isEmpty()) {
        method.recordTypes.add(recordType);
      }

      for (final Object o : foodDescriptionField.getSelectedObjects()) {
        method.foodDescriptors.add((String) o);
      }

      return method;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "editor_EditDietaryAssessmentMethodPanel_";

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(dataCollectionToolField)) {
        errors.add("Missing " + bundle.getString(prefix + "dataCollectionToolLabel"));
      }
      if (!nonConsecutiveOneDayField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "nonConsecutiveOneDaysLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException("Not implemented");
    }
  }

  /**
   * Panel to create/edit an {@link Hazard}.
   * 
   * <table summary="EditHazardPanel fields">
   * <tr>
   * <td>Hazard type</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Hazard name</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Hazard description</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Hazard unit</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Adverse effect</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Origin</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>BMD</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Maximum residue limit</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>No observed adverse</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Acceptable operator</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Acute reference dose</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Acceptable daily intake</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Ind sum</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Laboratory name</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Laboratory country</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Detection limit</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Quantification limit</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Left censored data</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Contamination range</td>
   * <td>Optional</td>
   * </tr>
   * </table>
   */
  private class EditHazardPanel extends EditPanel<Hazard> {

    private static final long serialVersionUID = -1981279747311233487L;

    private final AutoSuggestField hazardTypeField;
    private final AutoSuggestField hazardNameField;
    private final FTextArea hazardDescriptionTextArea;
    private final AutoSuggestField hazardUnitField;
    private final FTextField adverseEffectTextField;
    private final FTextField originTextField;
    private final FTextField bmdTextField;
    private final FTextField maxResidueLimitTextField;
    private final FTextField noObservedAdverseTextField;
    private final FTextField acceptableOperatorTextField;
    private final FTextField acuteReferenceDoseTextField;
    private final FTextField acceptableDailyIntakeTextField;
    private final AutoSuggestField indSumField;
    private final StringTextField labNameTextField;
    private final AutoSuggestField labCountryField;
    private final FTextField detectionLimitTextField;
    private final FTextField quantificationLimitTextField;
    private final FTextField leftCensoredDataTextField;
    private final FTextField contaminationRangeTextField;

    private final List<JComponent> advancedComponents;

    EditHazardPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      hazardTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard type"));
      hazardNameField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard name"));
      hazardDescriptionTextArea = new FTextArea();
      hazardUnitField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard unit"));
      adverseEffectTextField = new FTextField();
      originTextField = new FTextField();
      bmdTextField = new FTextField();
      maxResidueLimitTextField = new FTextField();
      noObservedAdverseTextField = new FTextField();
      acceptableOperatorTextField = new FTextField();
      acuteReferenceDoseTextField = new FTextField();
      acceptableDailyIntakeTextField = new FTextField();
      indSumField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard ind sum"));
      labNameTextField = new StringTextField(true, 30);
      labCountryField = GUIFactory.createAutoSuggestField(vocabs.get("Laboratory country"));
      detectionLimitTextField = new FTextField();
      quantificationLimitTextField = new FTextField();
      leftCensoredDataTextField = new FTextField();
      contaminationRangeTextField = new FTextField();

      advancedComponents = Arrays.asList(hazardDescriptionTextArea, adverseEffectTextField,
          originTextField, bmdTextField, maxResidueLimitTextField, acceptableOperatorTextField,
          noObservedAdverseTextField, acuteReferenceDoseTextField, acceptableDailyIntakeTextField,
          indSumField, labNameTextField, labCountryField, detectionLimitTextField,
          quantificationLimitTextField, leftCensoredDataTextField, contaminationRangeTextField);


      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "editor_EditHazardPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // hazard type
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "hazardType"));
      fields.add(hazardTypeField);

      // hazard name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "hazardName"));
      fields.add(hazardNameField);

      // hazard unit
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "hazardUnit"));
      fields.add(hazardUnitField);

      if (isAdvanced) {

        // adverse effect
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "adverseEffect"));
        fields.add(adverseEffectTextField);

        // origin
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "origin"));
        fields.add(originTextField);

        // bmd
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "bmd"));
        fields.add(bmdTextField);

        // max residue limit
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "maxResidueLimit"));
        fields.add(maxResidueLimitTextField);

        // no observed adverse
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "noObservedAdverse"));
        fields.add(noObservedAdverseTextField);

        // acceptable opeartor
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "acceptableOperator"));
        fields.add(acceptableOperatorTextField);

        // acute reference dose
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "acuteReferenceDose"));
        fields.add(acuteReferenceDoseTextField);

        // ind sum
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "indSum"));
        fields.add(indSumField);

        // acceptable daily intake
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "acceptableDailyIntake"));
        fields.add(acceptableDailyIntakeTextField);

        // lab name
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "labName"));
        fields.add(labNameTextField);

        // lab country
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "labCountry"));
        fields.add(labCountryField);

        // detection limit
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "detectionLimit"));
        fields.add(detectionLimitTextField);

        // quantification limit
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "quantificationLimit"));
        fields.add(quantificationLimitTextField);

        // left censored data
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "leftCensoredData"));
        fields.add(leftCensoredDataTextField);

        // contamination range
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "contaminationRange"));
        fields.add(contaminationRangeTextField);
      }

      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "hazardDescription");
        FPanel textAreaPanel = UIUtils.createFormPanel(Arrays.asList(label),
            Arrays.asList(new JScrollPane(hazardDescriptionTextArea)));
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);


      // If advanced mode, show advanced components
      advancedComponents.forEach(it -> it.setEnabled(isAdvanced));
    }

    @Override
    void init(Hazard hazard) {
      if (hazard != null) {
        hazardTypeField.setSelectedItem(hazard.hazardType);
        hazardNameField.setSelectedItem(hazard.hazardName);
        hazardDescriptionTextArea.setText(hazard.hazardDescription);
        hazardUnitField.setSelectedItem(hazard.hazardUnit);
        adverseEffectTextField.setText(hazard.adverseEffect);
        originTextField.setText(hazard.origin);
        bmdTextField.setText(hazard.benchmarkDose);
        maxResidueLimitTextField.setText(hazard.maximumResidueLimit);
        noObservedAdverseTextField.setText(hazard.noObservedAdverse);
        acceptableOperatorTextField.setText(hazard.acceptableOperator);
        acuteReferenceDoseTextField.setText(hazard.acuteReferenceDose);
        indSumField.setSelectedItem(hazard.hazardIndSum);
        acceptableDailyIntakeTextField.setText(hazard.acceptableDailyIntake);
        labNameTextField.setText(hazard.laboratoryName);
        labCountryField.setSelectedItem(hazard.laboratoryCountry);
        detectionLimitTextField.setText(hazard.detectionLimit);
        quantificationLimitTextField.setText(hazard.quantificationLimit);
        leftCensoredDataTextField.setText(hazard.leftCensoredData);
        contaminationRangeTextField.setText(hazard.rangeOfContamination);
      }
    }

    @Override
    Hazard get() {

      final Hazard hazard = new Hazard();
      hazard.hazardType = (String) hazardTypeField.getSelectedItem();
      hazard.hazardName = (String) hazardNameField.getSelectedItem();
      hazard.hazardUnit = (String) hazardUnitField.getSelectedItem();

      hazard.hazardDescription = hazardDescriptionTextArea.getText();
      hazard.adverseEffect = adverseEffectTextField.getText();
      hazard.origin = originTextField.getText();
      hazard.benchmarkDose = bmdTextField.getText();
      hazard.maximumResidueLimit = maxResidueLimitTextField.getText();
      hazard.noObservedAdverse = noObservedAdverseTextField.getText();
      hazard.acceptableOperator = acceptableOperatorTextField.getText();
      hazard.acuteReferenceDose = acuteReferenceDoseTextField.getText();
      hazard.acceptableDailyIntake = acceptableDailyIntakeTextField.getText();
      hazard.hazardIndSum = (String) indSumField.getSelectedItem();
      hazard.laboratoryName = labNameTextField.getText();
      hazard.laboratoryCountry = (String) labCountryField.getSelectedItem();
      hazard.detectionLimit = detectionLimitTextField.getText();
      hazard.leftCensoredData = leftCensoredDataTextField.getText();
      hazard.rangeOfContamination = contaminationRangeTextField.getText();

      return hazard;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "editor_EditHazardPanel_";
      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(hazardTypeField)) {
        errors.add("Missing " + bundle.getString(prefix + "hazardTypeLabel"));
      }
      if (!hasValidValue(hazardNameField)) {
        errors.add("Missing " + bundle.getString(prefix + "hazardNameLabel"));
      }
      if (!hasValidValue(hazardUnitField)) {
        errors.add("Missing " + bundle.getString(prefix + "hazardUnitLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException("Not implemented");
    }
  }

  /**
   * Panel to create/edit a {@link ModelEquation}.
   * 
   * <table summary="EditModelEquationPanel fields">
   * <tr>
   * <td>Equation name</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Equation class</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Script</td>
   * <td>Mandatory</td>
   * </tr>
   * </table>
   */
  private class EditModelEquationPanel extends EditPanel<ModelEquation> {

    private static final long serialVersionUID = 3586499490386620791L;

    private final FTextField equationNameTextField;
    private final FTextField equationClassTextField;
    private final ReferencePanel referencePanel;
    private final FTextArea scriptTextArea;

    EditModelEquationPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      equationNameTextField = new FTextField(true);
      equationClassTextField = new FTextField(true);
      referencePanel = new ReferencePanel(isAdvanced);
      scriptTextArea = new FTextArea();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      // Create labels
      String prefix = "editor_EditModelEquationPanel_";

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

      {
        List<FLabel> labels = new ArrayList<>();
        List<JComponent> fields = new ArrayList<>();

        // equation name
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "name"));
        fields.add(equationNameTextField);

        // equation class
        if (isAdvanced) {
          labels.add(GUIFactory.createLabelWithToolTip(prefix + "class"));
          fields.add(equationClassTextField);
        }

        northPanel.add(UIUtils.createFormPanel(labels, fields));
      }

      // reference panel
      if (isAdvanced) {
        northPanel.add(referencePanel);
      }

      // description
      if (isAdvanced) {
        FLabel label = new FLabel(bundle.getString(prefix + "scriptLabel"));
        northPanel
            .add(UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(scriptTextArea)));
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final ModelEquation modelEquation) {

      if (modelEquation != null) {
        equationNameTextField.setText(modelEquation.equationName);
        equationClassTextField.setText(modelEquation.equationClass);
        referencePanel.init(modelEquation.equationReference);
        scriptTextArea.setText(modelEquation.equation);
      }
    }

    @Override
    List<String> validatePanel() {

      String prefix = "editor_EditModelEquationPanel_";

      final List<String> errors = new ArrayList<>();
      if (!equationNameTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "nameLabel"));
      }
      if (!scriptTextArea.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "scriptLabel"));
      }
      return errors;
    }

    @Override
    ModelEquation get() {
      final ModelEquation modelEquation = new ModelEquation();
      modelEquation.equationName = equationNameTextField.getText();
      modelEquation.equation = scriptTextArea.getText();
      modelEquation.equationClass = equationClassTextField.getText();
      modelEquation.equationReference.addAll(referencePanel.tableModel.records);

      return modelEquation;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException("Not implemented");
    }
  }

  private class EditParameterPanel extends EditPanel<Parameter> {

    private static final long serialVersionUID = 1826555468897327895L;

    private final FTextField idTextField;
    private final JComboBox<Parameter.Classification> classificationComboBox;
    private final FTextField nameTextField;
    private final FTextArea descriptionTextArea;
    private final AutoSuggestField typeField;
    private final AutoSuggestField unitField;
    private final AutoSuggestField unitCategoryField;
    private final AutoSuggestField dataTypeField;
    private final AutoSuggestField sourceField;
    private final AutoSuggestField subjectField;
    private final AutoSuggestField distributionField;
    private final FTextField valueTextField;
    private final FTextField referenceTextField;
    private final FTextArea variabilitySubjectTextArea;
    private final FTextArea applicabilityTextArea;
    private SpinnerNumberModel errorSpinnerModel;

    public EditParameterPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      idTextField = new FTextField(true);
      classificationComboBox = new JComboBox<>(Parameter.Classification.values());
      nameTextField = new FTextField(true);
      descriptionTextArea = new FTextArea();
      typeField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter type"));
      unitField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter unit"));
      unitCategoryField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter unit category"));
      dataTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter data type"));
      sourceField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter source"));
      subjectField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter subject"));
      distributionField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter distribution"));
      valueTextField = new FTextField();
      referenceTextField = new FTextField();
      variabilitySubjectTextArea = new FTextArea();
      applicabilityTextArea = new FTextArea();
      errorSpinnerModel = GUIFactory.createSpinnerDoubleModel();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      String prefix = "editor_EditParameterPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // id
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "id"));
      fields.add(idTextField);

      // classification
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "classification"));
      fields.add(classificationComboBox);

      // name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "parameterName"));
      fields.add(nameTextField);

      // type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "type"));
        fields.add(typeField);
      }

      // unit
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "unit"));
        fields.add(unitField);
      }

      // unit category
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "unitCategory"));
        fields.add(unitCategoryField);
      }

      // data type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "dataType"));
        fields.add(dataTypeField);
      }

      // source
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "source"));
        fields.add(sourceField);
      }

      // subject
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "subject"));
        fields.add(subjectField);
      }

      // distribution
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "distribution"));
        fields.add(distributionField);
      }

      // value
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "value"));
        fields.add(valueTextField);
      }

      // reference
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "reference"));
        fields.add(referenceTextField);
      }

      // error
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "error"));
        fields.add(GUIFactory.createSpinner(errorSpinnerModel));
      }

      // Build UI
      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        labels.clear();
        fields.clear();

        // description
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "description"));
        fields.add(new JScrollPane(descriptionTextArea));

        // variability
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "variabilitySubject"));
        fields.add(new JScrollPane(variabilitySubjectTextArea));

        // applicability
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "applicability"));
        fields.add(new JScrollPane(applicabilityTextArea));

        FPanel textAreaPanel = UIUtils.createFormPanel(labels, fields);
        northPanel.add(textAreaPanel);
      }
      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(Parameter t) {
      if (t != null) {
        idTextField.setText(t.id);
        classificationComboBox.setSelectedItem(t.classification);
        nameTextField.setText(t.name);
        descriptionTextArea.setText(t.description);
        // TODO: typeField
        unitField.setSelectedItem(t.unit);
        unitCategoryField.setSelectedItem(t.unitCategory);
        dataTypeField.setSelectedItem(t.dataType);
        sourceField.setSelectedItem(t.dataType);
        subjectField.setSelectedItem(t.subject);
        distributionField.setSelectedItem(t.distribution);
        valueTextField.setText(t.value);
        referenceTextField.setText(t.reference);
        variabilitySubjectTextArea.setText(t.variabilitySubject);
        // TODO: fix model applicability
        errorSpinnerModel.setValue(t.error);
      }
    }

    @Override
    Parameter get() {

      final Parameter param = new Parameter();
      param.id = idTextField.getText();
      param.classification = (Parameter.Classification) classificationComboBox.getSelectedItem();
      param.name = nameTextField.getText();
      param.unit = (String) unitField.getSelectedItem();
      param.unitCategory = (String) unitCategoryField.getSelectedItem();
      param.dataType = (String) dataTypeField.getSelectedItem();
      // TODO: model applicability

      param.description = descriptionTextArea.getText();
      param.source = (String) sourceField.getSelectedItem();
      param.subject = (String) subjectField.getSelectedItem();
      param.distribution = (String) distributionField.getSelectedItem();
      param.value = valueTextField.getText();
      param.reference = referenceTextField.getText();
      param.variabilitySubject = variabilitySubjectTextArea.getText();
      param.error = errorSpinnerModel.getNumber().doubleValue();

      return param;
    }

    @Override
    List<String> validatePanel() {

      final String prefix = "editor_EditParameterPanel_";
      final List<String> errors = new ArrayList<>();
      if (!idTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "idLabel"));
      }
      if (classificationComboBox.getSelectedIndex() == -1) {
        errors.add("Missing " + bundle.getString(prefix + "classificationLabel"));
      }
      if (!nameTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "parameterNameLabel"));
      }
      if (!hasValidValue(unitField)) {
        errors.add("Missing " + bundle.getString(prefix + "unitLabel"));
      }
      if (!hasValidValue(unitCategoryField)) {
        errors.add("Missing " + bundle.getString(prefix + "unitCategoryLabel"));
      }
      if (!hasValidValue(dataTypeField)) {
        errors.add("Missing " + bundle.getString(prefix + "dataTypeLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException("Not implemented");
    }
  }

  /**
   * Panel to create/edit a {@link PopulationGroup}.
   * 
   * <table summary="EditPopulationGroupPanel fields">
   * <tr>
   * <td>Population name</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Target population</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Population span</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Population description</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Population age</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Population gender</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>BMI</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Special diet group</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Pattern consumption</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Region</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Country</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Risk</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Season</td>
   * <td>Optional</td>
   * </tr>
   * </table>
   */
  private class EditPopulationGroupPanel extends EditPanel<PopulationGroup> {

    private static final long serialVersionUID = -4520186348489618333L;

    private final FTextField populationNameTextField;
    private final FTextField targetPopulationTextField;
    private final FTextField populationSpanTextField;
    private final FTextArea populationDescriptionTextArea;
    private final FTextField populationAgeTextField;
    private final FTextField populationGenderTextField;
    private final FTextField bmiTextField;
    private final FTextField specialDietGroupTextField;
    private final FTextField patternConsumptionTextField;
    private final JComboBox<String> regionComboBox;
    private final JComboBox<String> countryComboBox;
    private final FTextField riskTextField;
    private final FTextField seasonTextField;

    public EditPopulationGroupPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      populationNameTextField = new FTextField(true);
      targetPopulationTextField = new FTextField();
      populationSpanTextField = new FTextField();
      populationDescriptionTextArea = new FTextArea();
      populationAgeTextField = new FTextField();
      populationGenderTextField = new FTextField();
      bmiTextField = new FTextField();
      specialDietGroupTextField = new FTextField();
      patternConsumptionTextField = new FTextField();
      regionComboBox = GUIFactory.createComboBox(vocabs.get("Region"));
      countryComboBox = GUIFactory.createComboBox(vocabs.get("Country"));
      riskTextField = new FTextField();
      seasonTextField = new FTextField();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "editor_EditPopulationGroupPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // population name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "populationName"));
      fields.add(populationNameTextField);

      if (isAdvanced) {

        // target population
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "targetPopulation"));
        fields.add(targetPopulationTextField);

        // population span
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "populationSpan"));
        fields.add(populationSpanTextField);

        // population age
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "populationAge"));
        fields.add(populationAgeTextField);

        // population gender
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "populationGender"));
        fields.add(populationGenderTextField);

        // bmi
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "bmi"));
        fields.add(bmiTextField);

        // special diet group
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "specialDietGroups"));
        fields.add(specialDietGroupTextField);

        // pattern consumption
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "patternConsumption"));
        fields.add(patternConsumptionTextField);

        // region
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "region"));
        fields.add(regionComboBox);

        // country
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "country"));
        fields.add(countryComboBox);

        // risk
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "riskAndPopulation"));
        fields.add(riskTextField);

        // season
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "season"));
        fields.add(seasonTextField);
      }

      final FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "populationDescription");
        FPanel textAreaPanel = UIUtils.createFormPanel(Arrays.asList(label),
            Arrays.asList(new JScrollPane(populationDescriptionTextArea)));
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final PopulationGroup t) {
      if (t != null) {
        populationNameTextField.setText(t.populationName);
        targetPopulationTextField.setText(t.targetPopulation);

        if (!t.populationSpan.isEmpty()) {
          populationSpanTextField.setText(t.populationSpan.get(0));
        }

        if (!t.populationDescription.isEmpty()) {
          populationDescriptionTextArea.setText(t.populationDescription.get(0));
        }

        if (!t.populationAge.isEmpty()) {
          populationAgeTextField.setText(t.populationAge.get(0));
        }

        populationGenderTextField.setText(t.populationGender);

        if (!t.bmi.isEmpty()) {
          bmiTextField.setText(t.bmi.get(0));
        }

        if (!t.specialDietGroups.isEmpty()) {
          specialDietGroupTextField.setText(t.specialDietGroups.get(0));
        }

        if (!t.patternConsumption.isEmpty()) {
          patternConsumptionTextField.setText(t.patternConsumption.get(0));
        }

        regionComboBox.setSelectedItem(t.region);

        countryComboBox.setSelectedItem(t.country);

        if (!t.populationRiskFactor.isEmpty()) {
          riskTextField.setText(t.populationRiskFactor.get(0));
        }

        if (!t.season.isEmpty()) {
          seasonTextField.setText(t.season.get(0));
        }
      }
    }

    @Override
    PopulationGroup get() {
      final PopulationGroup populationGroup = new PopulationGroup();
      populationGroup.populationName = populationNameTextField.getText();
      populationGroup.targetPopulation = targetPopulationTextField.getText();

      final String populationSpan = populationSpanTextField.getText();
      if (!populationSpan.isEmpty()) {
        populationGroup.populationSpan.add(populationSpan);
      }

      final String populationDescription = populationDescriptionTextArea.getText();
      if (!populationDescription.isEmpty()) {
        populationGroup.populationDescription.add(populationDescription);
      }

      final String populationAge = populationAgeTextField.getText();
      if (!populationAge.isEmpty()) {
        populationGroup.populationAge.add(populationAge);
      }

      populationGroup.populationGender = populationGenderTextField.getText();

      final String bmi = bmiTextField.getText();
      if (!bmi.isEmpty()) {
        populationGroup.bmi.add(bmi);
      }

      final String specialDietGroup = specialDietGroupTextField.getText();
      if (!specialDietGroup.isEmpty()) {
        populationGroup.specialDietGroups.add(specialDietGroup);
      }

      final String patternConsumption = patternConsumptionTextField.getText();
      if (!patternConsumption.isEmpty()) {
        populationGroup.patternConsumption.add(patternConsumption);
      }

      if (regionComboBox.getSelectedIndex() != -1) {
        populationGroup.region.add((String) regionComboBox.getSelectedItem());
      }

      if (countryComboBox.getSelectedIndex() != -1) {
        populationGroup.country.add((String) countryComboBox.getSelectedItem());
      }

      final String risk = riskTextField.getText();
      if (!risk.isEmpty()) {
        populationGroup.populationRiskFactor.add(risk);
      }

      final String season = seasonTextField.getText();
      if (!season.isEmpty()) {
        populationGroup.season.add(season);
      }

      return populationGroup;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(1);
      if (populationNameTextField.getText().isEmpty()) {
        errors.add(
            "Missing " + bundle.getString("editor_EditPopulationGroupPanel_populationNameLabel"));
      }
      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException();
    }
  }

  private class EditProductPanel extends EditPanel<Product> {

    private static final long serialVersionUID = -7400646603919832139L;

    private final AutoSuggestField envNameField;
    private final FTextArea envDescriptionTextArea;
    private final AutoSuggestField envUnitField;
    private final JComboBox<String> productionMethodComboBox;
    private final JComboBox<String> packagingComboBox;
    private final JComboBox<String> productTreatmentComboBox;
    private final AutoSuggestField originCountryField;
    private final AutoSuggestField originAreaField;
    private final AutoSuggestField fisheriesAreaField;
    private final FixedDateChooser productionDateChooser;
    private final FixedDateChooser expirationDateChooser;

    public EditProductPanel(boolean isAdvanced) {

      super(new BorderLayout());

      envNameField = GUIFactory.createAutoSuggestField(vocabs.get("Product-matrix name"));
      envDescriptionTextArea = new FTextArea();
      envUnitField = GUIFactory.createAutoSuggestField(vocabs.get("Product-matrix unit"));
      productionMethodComboBox = GUIFactory.createComboBox(vocabs.get("Method of production"));
      packagingComboBox = GUIFactory.createComboBox(vocabs.get("Packaging"));
      productTreatmentComboBox = GUIFactory.createComboBox(vocabs.get("Product treatment"));
      originCountryField = GUIFactory.createAutoSuggestField(vocabs.get("Country of origin"));
      originAreaField = GUIFactory.createAutoSuggestField(vocabs.get("Area of origin"));
      fisheriesAreaField = GUIFactory.createAutoSuggestField(vocabs.get("Fisheries area"));
      productionDateChooser = new FixedDateChooser();
      expirationDateChooser = new FixedDateChooser();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "editor_EditProductPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // environment name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "envName"));
      fields.add(envNameField);

      // environment unit
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "envUnit"));
      fields.add(envUnitField);

      if (isAdvanced) {

        // production method
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "productionMethod"));
        fields.add(productionMethodComboBox);

        // packaging
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "packaging"));
        fields.add(packagingComboBox);

        // product treatment
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "productTreatment"));
        fields.add(productTreatmentComboBox);

        // origin country
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "originCountry"));
        fields.add(originCountryField);

        // origin area
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "originArea"));
        fields.add(originAreaField);

        // fisheries area
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "fisheriesArea"));
        fields.add(fisheriesAreaField);

        // production date
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "productionDate"));
        fields.add(productionDateChooser);

        // expiration date
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "expirationDate"));
        fields.add(expirationDateChooser);
      }

      // Build UI
      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "envDescription");
        FPanel textAreaPanel = UIUtils.createFormPanel(Arrays.asList(label),
            Arrays.asList(new JScrollPane(envDescriptionTextArea)));
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final Product t) {

      if (t != null) {
        envNameField.setSelectedItem(t.environmentName);
        envDescriptionTextArea.setText(t.environmentDescription);
        envUnitField.setSelectedItem(t.environmentUnit);
        // TODO: productonMethodComboBox
        // TODO: packagingComboBox
        // TODO: productTreatmentComboBox
        originCountryField.setSelectedItem(t.originCountry);
        originAreaField.setSelectedItem(t.originArea);
        fisheriesAreaField.setSelectedItem(t.fisheriesArea);
        productionDateChooser.setDate(t.productionDate);
        expirationDateChooser.setDate(t.expirationDate);
      }
    }

    @Override
    Product get() {

      final Product product = new Product();
      product.environmentName = (String) envNameField.getSelectedItem();
      product.environmentDescription = envDescriptionTextArea.getText();
      product.environmentUnit = (String) envUnitField.getSelectedItem();
      Arrays.stream(productionMethodComboBox.getSelectedObjects()).map(it -> (String) it)
          .forEach(product.productionMethod::add);
      Arrays.stream(packagingComboBox.getSelectedObjects()).map(it -> (String) it)
          .forEach(product.packaging::add);
      Arrays.stream(productTreatmentComboBox.getSelectedObjects()).map(it -> (String) it)
          .forEach(product.productTreatment::add);
      product.originCountry = (String) originCountryField.getSelectedItem();
      product.originArea = (String) originAreaField.getSelectedItem();
      product.fisheriesArea = (String) fisheriesAreaField.getSelectedItem();
      product.productionDate = productionDateChooser.getDate();
      product.expirationDate = expirationDateChooser.getDate();

      return product;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "editor_EditProductPanel_";
      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(envNameField)) {
        errors.add("Missing " + bundle.getString(prefix + "envNameLabel"));
      }
      if (!hasValidValue(envUnitField)) {
        errors.add("Missing " + bundle.getString(prefix + "envUnitLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException("Not implemented");
    }
  }

  private static LinkedHashMap<Type, String> referenceTypeLabels;
  static {
    referenceTypeLabels = new LinkedHashMap<>();
    referenceTypeLabels.put(Type.ABST, "Abstract");
    referenceTypeLabels.put(Type.ADVS, "Audiovisual material");
    referenceTypeLabels.put(Type.AGGR, "Aggregated Database");
    referenceTypeLabels.put(Type.ANCIENT, "Ancient Text");
    referenceTypeLabels.put(Type.ART, "Art Work");
    referenceTypeLabels.put(Type.BILL, "Bill");
    referenceTypeLabels.put(Type.BLOG, "Blog");
    referenceTypeLabels.put(Type.BOOK, "Whole book");
    referenceTypeLabels.put(Type.CASE, "Case");
    referenceTypeLabels.put(Type.CHAP, "Book chapter");
    referenceTypeLabels.put(Type.CHART, "Chart");
    referenceTypeLabels.put(Type.CLSWK, "Classical Work");
    referenceTypeLabels.put(Type.COMP, "Computer program");
    referenceTypeLabels.put(Type.CONF, "Conference proceeding");
    referenceTypeLabels.put(Type.CPAPER, "Conference paper");
    referenceTypeLabels.put(Type.CTLG, "Catalog");
    referenceTypeLabels.put(Type.DATA, "Data file");
    referenceTypeLabels.put(Type.DBASE, "Online Database");
    referenceTypeLabels.put(Type.DICT, "Dictionary");
    referenceTypeLabels.put(Type.EBOOK, "Electronic Book");
    referenceTypeLabels.put(Type.ECHAP, "Electronic Book Section");
    referenceTypeLabels.put(Type.EDBOOK, "Edited Book");
    referenceTypeLabels.put(Type.EJOUR, "Electronic Article");
    referenceTypeLabels.put(Type.ELEC, "Web Page");
    referenceTypeLabels.put(Type.ENCYC, "Encyclopedia");
    referenceTypeLabels.put(Type.EQUA, "Equation");
    referenceTypeLabels.put(Type.FIGURE, "Figure");
    referenceTypeLabels.put(Type.GEN, "Generic");
    referenceTypeLabels.put(Type.GOVDOC, "Government Document");
    referenceTypeLabels.put(Type.GRANT, "Grant");
    referenceTypeLabels.put(Type.HEAR, "Hearing");
    referenceTypeLabels.put(Type.ICOMM, "Internet Communication");
    referenceTypeLabels.put(Type.INPR, "In Press");
    referenceTypeLabels.put(Type.JFULL, "Journal (full)");
    referenceTypeLabels.put(Type.JOUR, "Journal");
    referenceTypeLabels.put(Type.LEGAL, "Legal Rule or Regulation");
    referenceTypeLabels.put(Type.MANSCPT, "Manuscript");
    referenceTypeLabels.put(Type.MAP, "Map");
    referenceTypeLabels.put(Type.MGZN, "Magazine article");
    referenceTypeLabels.put(Type.MPCT, "Motion picture");
    referenceTypeLabels.put(Type.MULTI, "Online Multimedia");
    referenceTypeLabels.put(Type.MUSIC, "Music score");
    referenceTypeLabels.put(Type.NEWS, "Newspaper");
    referenceTypeLabels.put(Type.PAMP, "Pamphlet");
    referenceTypeLabels.put(Type.PAT, "Patent");
    referenceTypeLabels.put(Type.PCOMM, "Personal communication");
    referenceTypeLabels.put(Type.RPRT, "Report");
    referenceTypeLabels.put(Type.SER, "Serial publication");
    referenceTypeLabels.put(Type.SLIDE, "Slide");
    referenceTypeLabels.put(Type.SOUND, "Sound recording");
    referenceTypeLabels.put(Type.STAND, "Standard");
    referenceTypeLabels.put(Type.STAT, "Statute");
    referenceTypeLabels.put(Type.THES, "Thesis/Dissertation");
    referenceTypeLabels.put(Type.UNPB, "Unpublished work");
    referenceTypeLabels.put(Type.VIDEO, "Video recording");
  }

  /**
   * Panel to create/edit a {@link Record}.
   * 
   * <table summary="EditReferencePanel fields">
   * <tr>
   * <td>Is reference description?</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Type</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Date</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>PMID</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>DOI</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Authors</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Title</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Abstract</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Journal</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Volume</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Issue</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Page</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Status</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Website</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Comment</td>
   * <td>Optional</td>
   * </tr>
   * </table>
   */
  private class EditReferencePanel extends EditPanel<Record> {

    private static final long serialVersionUID = -6874752919377124455L;

    private static final String dateFormatStr = "yyyy-MM-dd";

    // Spinner models starting with 0 and taking positive ints only
    private final SpinnerNumberModel volumeSpinnerModel;
    private final SpinnerNumberModel issueSpinnerModel;

    private final JCheckBox isReferenceDescriptionCheckBox;
    private final JComboBox<String> typeComboBox;
    private final FixedDateChooser dateChooser;
    private final FTextField pmidTextField;
    private final FTextField doiTextField;
    private final FTextField authorListTextField;
    private final FTextField titleTextField;
    private final FTextArea abstractTextArea;
    private final FTextField journalTextField;
    private final FTextField pageTextField;
    private final FTextField statusTextField;
    private final FTextField websiteTextField;
    private final FTextArea commentTextArea;

    EditReferencePanel(final boolean isAdvanced) {

      super(new BorderLayout());

      // Create fields
      volumeSpinnerModel = GUIFactory.createSpinnerIntegerModel();
      issueSpinnerModel = GUIFactory.createSpinnerIntegerModel();

      isReferenceDescriptionCheckBox = new JCheckBox("Is reference description *");
      typeComboBox = GUIFactory.createComboBox(referenceTypeLabels.values());
      dateChooser = new FixedDateChooser();
      pmidTextField = new FTextField();
      doiTextField = new FTextField(true);
      authorListTextField = new FTextField();
      titleTextField = new FTextField(true);
      abstractTextArea = new FTextArea();
      journalTextField = new FTextField();
      pageTextField = new FTextField();
      statusTextField = new FTextField();
      websiteTextField = new FTextField();
      commentTextArea = new FTextArea();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      // Create labels
      String prefix = "editor_EditReferencePanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // type
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "typeLabel")));
        fields.add(typeComboBox);
      }

      // date
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "dateLabel")));
        fields.add(dateChooser);
      }

      // pmid
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "pmidLabel")));
        fields.add(pmidTextField);
      }

      // doi
      labels.add(new FLabel(bundle.getString(prefix + "doiLabel")));
      fields.add(doiTextField);

      // author
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "authorListLabel")));
        fields.add(authorListTextField);
      }

      // title
      labels.add(new FLabel(bundle.getString(prefix + "titleLabel")));
      fields.add(titleTextField);

      // journal
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "journalLabel")));
        fields.add(journalTextField);
      }

      // volume
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "volumeLabel")));
        fields.add(GUIFactory.createSpinner(volumeSpinnerModel));
      }

      // issue
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "issueLabel")));
        fields.add(GUIFactory.createSpinner(issueSpinnerModel));
      }

      // page
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "pageLabel")));
        fields.add(pageTextField);
      }

      // status
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "statusLabel")));
        fields.add(statusTextField);
      }

      // website
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "websiteLabel")));
        fields.add(websiteTextField);
      }

      // Build UI
      isReferenceDescriptionCheckBox.setBackground(UIUtils.WHITE);

      // isReferenceDescription panel
      FPanel isReferenceDescriptionPanel = UIUtils.createWestPanel(isReferenceDescriptionCheckBox);

      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(isReferenceDescriptionPanel);
      northPanel.add(formPanel);

      if (isAdvanced) {
        List<FLabel> labels2 = Arrays.asList(new FLabel(bundle.getString(prefix + "abstractLabel")),
            new FLabel(bundle.getString(prefix + "commentLabel")));
        List<JComponent> fields2 =
            Arrays.asList(new JScrollPane(abstractTextArea), new JScrollPane(commentTextArea));
        FPanel textAreaPanel = UIUtils.createFormPanel(labels2, fields2);
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final Record t) {
      if (t != null) {
        final Type type = t.getType();
        if (type != null) {
          typeComboBox.setSelectedItem(referenceTypeLabels.get(type));
        }

        final String dateString = t.getDate();
        if (dateString != null) {
          try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
            dateChooser.setDate(dateFormat.parse(dateString));
          } catch (final ParseException exception) {
            LOGGER.warn("Invalid date", exception);
          }
        }

        doiTextField.setText(t.getDoi());

        final List<String> authors = t.getAuthors();
        if (authors != null) {
          authorListTextField.setText(String.join(";", authors));
        }

        titleTextField.setText(t.getTitle());
        abstractTextArea.setText(t.getAbstr());
        journalTextField.setText(t.getSecondaryTitle());

        final String volumeNumberString = t.getVolumeNumber();
        if (volumeNumberString != null) {
          try {
            int volumeNumber = Integer.parseInt(volumeNumberString);
            volumeSpinnerModel.setValue(volumeNumber);
          } catch (final NumberFormatException exception) {
          }
        }

        final Integer issueNumber = t.getIssueNumber();
        if (issueNumber != null) {
          issueSpinnerModel.setValue(issueNumber);
        }
        websiteTextField.setText(t.getWebsiteLink());
      }
    }

    @Override
    Record get() {

      final Record record = new Record();
      // TODO: isReferenceDescriptionCheckBox

      final int selectedTypeIndex = typeComboBox.getSelectedIndex();
      if (selectedTypeIndex != -1) {
        final Type type = referenceTypeLabels.keySet()
            .toArray(new Type[referenceTypeLabels.size()])[selectedTypeIndex];
        record.setType(type);
      }

      final Date date = dateChooser.getDate();
      if (date != null) {
        record.setDate(new SimpleDateFormat(dateFormatStr).format(date));
      }

      record.setDoi(doiTextField.getText());

      final String authors = authorListTextField.getText();
      if (authors != null) {
        Arrays.stream(authors.split(";")).forEach(record::addAuthor);
      }

      record.setTitle(titleTextField.getText());
      record.setAbstr(abstractTextArea.getText());
      record.setSecondaryTitle(journalTextField.getText());

      final Number volumeNumber = volumeSpinnerModel.getNumber();
      if (volumeNumber != null) {
        record.setVolumeNumber(volumeNumber.toString());
      }

      final Number issueNumber = issueSpinnerModel.getNumber();
      if (issueNumber != null) {
        record.setIssueNumber(issueNumber.intValue());
      }

      // TODO: status
      record.setWebsiteLink(websiteTextField.getText());
      // TODO: comment

      return record;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "editor_EditReferencePanel_";
      final List<String> errors = new ArrayList<>(2);
      if (doiTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "doiLabel"));
      }
      if (titleTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "titleLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException("Not implemented");
    }
  }

  /**
   * Panel to create/edit a {@link StudySample}.
   * 
   * <table summary="EditStudySamplePanel fields">
   * <tr>
   * <td>Sample name</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Moisture percentage</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Fat percentage</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Sample protocol</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Sampling strategy</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Sampling type</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Sampling method</td>
   * <td>Optional</td>
   * </tr>
   * <tr>
   * <td>Sampling plan</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Sampling weight</td>
   * <td>Mandatory</td>
   * </tr>
   * <tr>
   * <td>Sampling size</td>
   * <td>Mandatory</td>
   * </tr>
   * </table>
   */
  private class EditStudySamplePanel extends EditPanel<StudySample> {

    private static final long serialVersionUID = -4740851101237646103L;

    private final SpinnerNumberModel moisturePercentageSpinnerModel;
    private final SpinnerNumberModel fatPercentageSpinnerModel;

    private final FTextField sampleNameTextField;
    private final FTextField sampleProtocolTextField;
    private final AutoSuggestField samplingStrategyField;
    private final AutoSuggestField samplingTypeField;
    private final AutoSuggestField samplingMethodField;
    private final FTextField samplingPlanTextField;
    private final FTextField samplingWeightTextField;
    private final FTextField samplingSizeTextField;
    private final AutoSuggestField lotSizeUnitField;
    private final AutoSuggestField samplingPointField;

    public EditStudySamplePanel(final boolean isAdvanced) {

      super(new BorderLayout());

      moisturePercentageSpinnerModel = GUIFactory.createSpinnerPercentageModel();
      fatPercentageSpinnerModel = GUIFactory.createSpinnerPercentageModel();

      sampleNameTextField = new FTextField(true);
      sampleProtocolTextField = new FTextField(true);
      samplingStrategyField = GUIFactory.createAutoSuggestField(vocabs.get("Sampling strategy"));
      samplingTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Type of sampling program"));
      samplingMethodField = GUIFactory.createAutoSuggestField(vocabs.get("Sampling method"));
      samplingPlanTextField = new FTextField(true);
      samplingWeightTextField = new FTextField(true);
      samplingSizeTextField = new FTextField(true);
      lotSizeUnitField = GUIFactory.createAutoSuggestField(vocabs.get("Lot size unit"));
      samplingPointField = GUIFactory.createAutoSuggestField(vocabs.get("Sampling point"));

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      String prefix = "editor_EditStudySamplePanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // sample name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "sampleName"));
      fields.add(sampleNameTextField);

      // moisture percentage
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "moisturePercentage"));
        fields.add(GUIFactory.createSpinner(moisturePercentageSpinnerModel));
      }

      // fat percentage
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "fatPercentage"));
        fields.add(GUIFactory.createSpinner(fatPercentageSpinnerModel));
      }

      // sample protocol label
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "sampleProtocol"));
      fields.add(sampleProtocolTextField);

      // sampling strategy label
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingStrategy"));
        fields.add(samplingStrategyField);
      }

      // sampling type label
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingType"));
        fields.add(samplingTypeField);
      }

      // sampling method label
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingMethod"));
        fields.add(samplingMethodField);
      }

      // sampling plan
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingPlan"));
      fields.add(samplingPlanTextField);

      // sampling weight
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingWeight"));
      fields.add(samplingWeightTextField);

      // sampling size
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingSize"));
      fields.add(samplingSizeTextField);

      // lot size unit
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "lotSizeUnit"));
        fields.add(lotSizeUnitField);
      }

      // sampling point
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingPoint"));
        fields.add(samplingPointField);
      }

      // formPanel
      final FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final StudySample t) {
      if (t != null) {
        sampleNameTextField.setText(t.sample);
        if (t.moisturePercentage != null) {
          moisturePercentageSpinnerModel.setValue(t.moisturePercentage);
        }
        if (t.fatPercentage != null) {
          fatPercentageSpinnerModel.setValue(t.fatPercentage);
        }
        sampleProtocolTextField.setText(t.collectionProtocol);
        samplingStrategyField.setSelectedItem(t.samplingStrategy);
        samplingTypeField.setSelectedItem(t.samplingProgramType);
        samplingMethodField.setSelectedItem(t.samplingMethod);
        samplingPlanTextField.setText(t.samplingPlan);
        samplingWeightTextField.setText(t.samplingWeight);
        samplingSizeTextField.setText(t.samplingSize);
        lotSizeUnitField.setSelectedItem(t.lotSizeUnit);
        samplingPointField.setSelectedItem(t.samplingPoint);
      }
    }

    @Override
    StudySample get() {

      final StudySample studySample = new StudySample();
      studySample.sample = sampleNameTextField.getText();
      studySample.collectionProtocol = sampleProtocolTextField.getText();
      studySample.samplingPlan = samplingPlanTextField.getText();
      studySample.samplingWeight = samplingWeightTextField.getText();
      studySample.samplingSize = samplingSizeTextField.getText();

      studySample.moisturePercentage = moisturePercentageSpinnerModel.getNumber().doubleValue();
      studySample.fatPercentage = fatPercentageSpinnerModel.getNumber().doubleValue();
      studySample.samplingStrategy = (String) samplingStrategyField.getSelectedItem();
      studySample.samplingProgramType = (String) samplingTypeField.getSelectedItem();
      studySample.samplingMethod = (String) samplingMethodField.getSelectedItem();
      studySample.lotSizeUnit = (String) lotSizeUnitField.getSelectedItem();
      studySample.samplingPoint = (String) samplingPointField.getSelectedItem();

      return studySample;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "editor_EditStudySamplePanel_";
      final List<String> errors = new ArrayList<>(5);
      if (!sampleNameTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "sampleNameLabel"));
      }
      if (!sampleProtocolTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "sampleProtocolLabel"));
      }
      if (!samplingPlanTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "samplingPlanLabel"));
      }
      if (!samplingWeightTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "samplingWeightLabel"));
      }
      if (!samplingSizeTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "samplingSizeLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      throw new UnsupportedOperationException("Not implemented");
    }
  }

  // Validation methods
  private static boolean hasValidValue(final AutoSuggestField field) {
    final JTextField textField = (JTextField) field.getEditor().getEditorComponent();
    return StringUtils.isNotBlank(textField.getText());
  }

  private abstract class TopLevelPanel<T> extends JPanel {

    private static final long serialVersionUID = 6410915237186157478L;

    public TopLevelPanel(final LayoutManager layout) {
      super(layout);
    }

    abstract void init(final T t);

    abstract T get();
  }

  private class GeneralInformationPanel extends TopLevelPanel<GeneralInformation> {

    private static final long serialVersionUID = 2705689661594031061L;

    private final JCheckBox advancedCheckBox;

    private final JTextField studyNameTextField;
    private final JTextField sourceTextField;
    private final JTextField identifierTextField;
    private final CreatorPanel creatorPanel;
    private final FixedDateChooser creationDateChooser;
    private final AutoSuggestField rightsField;
    private final JCheckBox availabilityCheckBox;
    private final JTextField urlTextField;
    private final AutoSuggestField formatField;
    private final ReferencePanel referencePanel;
    private final AutoSuggestField languageField;
    private final AutoSuggestField softwareField;
    private final AutoSuggestField languageWrittenInField;
    private final AutoSuggestField statusField;
    private final FTextArea objectiveTextArea;
    private final FTextArea descriptionTextArea;

    public GeneralInformationPanel() {

      super(new BorderLayout());

      // Create fields
      advancedCheckBox = new JCheckBox("Advanced");
      studyNameTextField = new FTextField(true);
      sourceTextField = new FTextField(true);
      identifierTextField = new FTextField(true);
      creatorPanel = new CreatorPanel();
      creationDateChooser = new FixedDateChooser();
      rightsField = GUIFactory.createAutoSuggestField(vocabs.get("Rights"));
      availabilityCheckBox = new JCheckBox();
      urlTextField = new FTextField(true);
      formatField = GUIFactory.createAutoSuggestField(vocabs.get("Format"));
      referencePanel = new ReferencePanel(advancedCheckBox.isSelected());
      languageField = GUIFactory.createAutoSuggestField(vocabs.get("Language"));
      softwareField = GUIFactory.createAutoSuggestField(vocabs.get("Software"));
      languageWrittenInField = GUIFactory.createAutoSuggestField(vocabs.get("Language written in"));
      statusField = GUIFactory.createAutoSuggestField(vocabs.get("Status"));
      objectiveTextArea = new FTextArea();
      descriptionTextArea = new FTextArea();

      createUI();
    }

    private void createUI() {

      // Create labels
      String prefix = "editor_GeneralInformationPanel_";

      FLabel studyNameLabel = GUIFactory.createLabelWithToolTip(prefix + "studyName");
      FLabel identifierLabel = GUIFactory.createLabelWithToolTip(prefix + "identifier");
      FLabel creationDateLabel = GUIFactory.createLabelWithToolTip(prefix + "creationDate");
      FLabel rightsLabel = GUIFactory.createLabelWithToolTip(prefix + "rights");
      FLabel availabilityLabel = GUIFactory.createLabelWithToolTip(prefix + "availability");
      FLabel sourceLabel = GUIFactory.createLabelWithToolTip(prefix + "source");
      FLabel urlLabel = GUIFactory.createLabelWithToolTip(prefix + "creationDate");
      FLabel formatLabel = GUIFactory.createLabelWithToolTip(prefix + "format");
      FLabel languageLabel = GUIFactory.createLabelWithToolTip(prefix + "language");
      FLabel softwareLabel = GUIFactory.createLabelWithToolTip(prefix + "software");
      FLabel languageWrittenInLabel =
          GUIFactory.createLabelWithToolTip(prefix + "languageWrittenIn");
      FLabel statusLabel = GUIFactory.createLabelWithToolTip(prefix + "status");

      availabilityCheckBox.setBackground(UIUtils.WHITE);

      // Hide initially advanced components
      final List<JComponent> advancedComponents =
          Arrays.asList(sourceTextField, formatField, languageField, softwareField,
              languageWrittenInField, statusField, objectiveTextArea, descriptionTextArea);
      advancedComponents.forEach(it -> it.setEnabled(false));

      // formPanel
      JPanel creationDatePanel = UIUtils.createWestPanel(creationDateChooser);
      JPanel availabilityPanel = UIUtils.createWestPanel(availabilityCheckBox);
      final JPanel formPanel = UIUtils.createFormPanel(
          Arrays.asList(studyNameLabel, identifierLabel, creationDateLabel, rightsLabel,
              availabilityLabel, urlLabel, sourceLabel, formatLabel, languageLabel, softwareLabel,
              languageWrittenInLabel, statusLabel),
          Arrays.asList(studyNameTextField, identifierTextField, creationDatePanel, rightsField,
              availabilityPanel, urlTextField, sourceTextField, formatField, languageField,
              softwareField, languageWrittenInField, statusField));

      advancedCheckBox.addItemListener(event -> {
        final boolean showAdvanced = advancedCheckBox.isSelected();
        advancedComponents.forEach(it -> it.setEnabled(showAdvanced));
        referencePanel.isAdvanced = showAdvanced;
      });

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(formPanel);

      {
        List<FLabel> labels = Arrays.asList(GUIFactory.createLabelWithToolTip(prefix + "objective"),
            GUIFactory.createLabelWithToolTip(prefix + "description"));
        List<JComponent> fields =
            Arrays.asList(new JScrollPane(objectiveTextArea), new JScrollPane(descriptionTextArea));
        FPanel textAreaPanel = UIUtils.createFormPanel(labels, fields);
        northPanel.add(textAreaPanel);
      }
      northPanel.add(creatorPanel);
      northPanel.add(referencePanel);

      northPanel.setBackground(UIUtils.WHITE);

      add(northPanel, BorderLayout.NORTH);
    }

    void init(final GeneralInformation generalInformation) {

      if (generalInformation != null) {
        studyNameTextField.setText(generalInformation.name);
        sourceTextField.setText(generalInformation.source);
        identifierTextField.setText(generalInformation.identifier);
        creatorPanel.init(generalInformation.creators);
        creationDateChooser.setDate(generalInformation.creationDate);
        rightsField.setSelectedItem(generalInformation.rights);
        availabilityCheckBox.setSelected(generalInformation.isAvailable);
        if (generalInformation.url != null) {
          urlTextField.setText(generalInformation.url.toString());
        }
        formatField.setSelectedItem(generalInformation.format);
        referencePanel.init(generalInformation.reference);
        languageField.setSelectedItem(generalInformation.language);
        softwareField.setSelectedItem(generalInformation.software);
        languageWrittenInField.setSelectedItem(generalInformation.languageWrittenIn);
        statusField.setSelectedItem(generalInformation.status);
        objectiveTextArea.setText(generalInformation.objective);
        descriptionTextArea.setText(generalInformation.description);
      }
    }

    GeneralInformation get() {

      final GeneralInformation generalInformation = new GeneralInformation();
      generalInformation.name = studyNameTextField.getText();
      generalInformation.source = sourceTextField.getText();
      generalInformation.identifier = identifierTextField.getText();
      generalInformation.creationDate = creationDateChooser.getDate();
      generalInformation.rights = (String) rightsField.getSelectedItem();
      generalInformation.isAvailable = availabilityCheckBox.isSelected();

      final String urlText = urlTextField.getText();
      if (!urlText.isEmpty()) {
        try {
          generalInformation.url = new URL(urlText);
        } catch (final MalformedURLException exception) {
          LOGGER.error(urlText + " is not a valid URL");
        }
      }

      generalInformation.creators.addAll(creatorPanel.tableModel.vcards);

      generalInformation.format = (String) formatField.getSelectedItem();
      generalInformation.reference.addAll(referencePanel.tableModel.records);

      generalInformation.language = (String) languageField.getSelectedItem();
      generalInformation.software = (String) softwareField.getSelectedItem();
      generalInformation.languageWrittenIn = (String) languageWrittenInField.getSelectedItem();
      generalInformation.status = (String) statusField.getSelectedItem();
      generalInformation.objective = objectiveTextArea.getText();
      generalInformation.description = descriptionTextArea.getText();

      return generalInformation;
    }
  }

  private class ReferencePanel extends JPanel {

    private static final long serialVersionUID = 7457092378015891750L;

    boolean isAdvanced;

    RecordTableModel tableModel = new RecordTableModel();

    // Non modifiable table model with headers
    class RecordTableModel extends DefaultTableModel {

      private static final long serialVersionUID = -3034772220080396221L;
      final ArrayList<Record> records = new ArrayList<>();

      RecordTableModel() {
        super(new Object[0][0], new String[] {"DOI", "Publication title"});
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

      void add(final Record record) {

        // Skip if record is already in the table
        for (final Record r : records) {
          if (r.equals(record))
            return;
        }

        records.add(record);

        final String doi = record.getDoi();
        final String publicationTitle = record.getTitle();
        addRow(new String[] {doi, publicationTitle});
      }

      void modify(final int rowNumber, final Record newRecord) {
        records.set(rowNumber, newRecord);

        setValueAt(newRecord.getDoi(), rowNumber, 0);
        setValueAt(newRecord.getTitle(), rowNumber, 1);
      }

      void remove(final int rowNumber) {
        records.remove(rowNumber);
        removeRow(rowNumber);
      }
    }

    public ReferencePanel(final boolean isAdvanced) {

      super(new BorderLayout());
      setBorder(BorderFactory.createTitledBorder("References"));

      this.isAdvanced = isAdvanced;

      final JTable coolTable = UIUtils.createTable(tableModel);

      // buttons
      final JButton addButton = UIUtils.createAddButton();
      addButton.addActionListener(event -> {

        final EditReferencePanel editPanel = new EditReferencePanel(this.isAdvanced);
        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create reference");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Record newRecord = editPanel.get();
          tableModel.add(newRecord);
        }
      });

      final JButton fileUploadButton = UIUtils.createFileUploadButton();
      fileUploadButton.addActionListener(event -> {

        // Configure file chooser
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(new SimpleFileFilter("ris", "RIS"));

        final int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          try {
            final List<Record> importedRecords = JRis.parse(fc.getSelectedFile());
            importedRecords.forEach(tableModel::add);
          } catch (final IOException | JRisException exception) {
            LOGGER.warn("Error importing RIS references", exception);
          }

        }
      });

      final JButton editButton = UIUtils.createEditButton();
      editButton.addActionListener(event -> {

        final int rowToEdit = coolTable.getSelectedRow();
        if (rowToEdit != -1) {

          final Record ref = tableModel.records.get(rowToEdit);

          final EditReferencePanel editPanel = new EditReferencePanel(this.isAdvanced);
          editPanel.init(ref);

          final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Modify reference");
          if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
            tableModel.modify(rowToEdit, editPanel.get());
          }
        }
      });

      final JButton removeButton = UIUtils.createRemoveButton();
      removeButton.addActionListener(event -> {
        final int rowToDelete = coolTable.getSelectedRow();
        if (rowToDelete != -1) {
          tableModel.remove(rowToDelete);
        }
      });

      final JPanel panel = UI.createTablePanel(coolTable);

      final JPanel buttonsPanel =
          UI.createHorizontalPanel(addButton, fileUploadButton, editButton, removeButton);
      panel.add(UI.createCenterPanel(buttonsPanel), BorderLayout.SOUTH);

      add(panel);
    }

    void init(final List<Record> references) {
      references.forEach(tableModel::add);
    }
  }

  private class CreatorPanel extends JPanel {

    private static final long serialVersionUID = 3543570665869685092L;
    TableModel tableModel = new TableModel();

    // Non modifiable table model with headers
    class TableModel extends DefaultTableModel {

      private static final long serialVersionUID = -2363056543695517576L;
      final ArrayList<VCard> vcards = new ArrayList<>();

      TableModel() {
        super(new Object[0][0], new String[] {"Given name", "Family name", "Contact"});
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

      void add(final VCard vcard) {

        // Skip if vcard is already in table
        for (final VCard currentCard : vcards) {
          if (currentCard.equals(vcard)) {
            return;
          }
        }
        vcards.add(vcard);

        final String givenName = vcard.getNickname().getValues().get(0);
        final String familyName = vcard.getFormattedName().getValue();
        final String contact = vcard.getEmails().get(0).getValue();
        addRow(new String[] {givenName, familyName, contact});
      }

      void modify(final int rowNumber, final VCard vcard) {
        vcards.set(rowNumber, vcard);

        setValueAt(vcard.getNickname().getValues().get(0), rowNumber, 0);
        setValueAt(vcard.getFormattedName().getValue(), rowNumber, 1);
        setValueAt(vcard.getEmails().get(0).getValue(), rowNumber, 2);
      }

      void remove(final int rowNumber) {
        vcards.remove(rowNumber);
        removeRow(rowNumber);
      }
    }

    public CreatorPanel() {

      super(new BorderLayout());

      setBorder(BorderFactory.createTitledBorder("Creators"));

      final JButton fileUploadButton = UIUtils.createFileUploadButton();
      fileUploadButton.addActionListener(event -> {

        // Configure file chooser
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new SimpleFileFilter(".vcf", ".VCF"));

        // Read file
        final int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          try {
            final List<VCard> vcards = Ezvcard.parse(fileChooser.getSelectedFile()).all();
            vcards.forEach(tableModel::add);
          } catch (final IOException exception) {
            LOGGER.warn("Error importing VCards", exception);
          }
        }
      });

      final JTable myTable = UIUtils.createTable(tableModel);

      // buttons
      final JButton addButton = UIUtils.createAddButton();
      addButton.addActionListener(event -> {

        final EditCreatorPanel editPanel = new EditCreatorPanel();
        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create creator");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final VCard newCard = editPanel.get();
          tableModel.add(newCard);
        }
      });

      final JButton editButton = UIUtils.createEditButton();
      editButton.addActionListener(event -> {

        final int rowToEdit = myTable.getSelectedRow();
        if (rowToEdit != -1) {

          final VCard creator = tableModel.vcards.get(rowToEdit);

          final EditCreatorPanel editPanel = new EditCreatorPanel();
          editPanel.init(creator);

          final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Modify creator");
          if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
            tableModel.modify(rowToEdit, editPanel.get());
          }
        }
      });

      final JButton removeButton = UIUtils.createRemoveButton();
      removeButton.addActionListener(event -> {
        final int rowToDelete = myTable.getSelectedRow();
        if (rowToDelete != -1) {
          tableModel.remove(rowToDelete);
        }
      });

      JPanel panel = UI.createTablePanel(myTable);

      JPanel buttonsPanel =
          UI.createHorizontalPanel(addButton, fileUploadButton, editButton, removeButton);
      panel.add(UI.createCenterPanel(buttonsPanel), BorderLayout.SOUTH);

      add(panel);
    }

    void init(final List<VCard> vcards) {
      tableModel.vcards.clear();
      tableModel.setRowCount(0);
      vcards.forEach(tableModel::add);
    }
  }

  private class EditCreatorPanel extends EditPanel<VCard> {

    private static final long serialVersionUID = 3472281253338213542L;

    private final FTextField givenNameTextField;
    private final FTextField familyNameTextField;
    private final FTextField contactTextField;

    public EditCreatorPanel() {
      super(new BorderLayout());
      givenNameTextField = new FTextField(true);
      familyNameTextField = new FTextField(true);
      contactTextField = new FTextField(true);

      createUI();
    }

    private void createUI() {
      // Create labels
      String prefix = "editor_EditCreatorPanel_";
      FLabel givenNameLabel = new FLabel(bundle.getString(prefix + "givenNameLabel"));
      FLabel familyNameLabel = new FLabel(bundle.getString(prefix + "familyNameLabel"));
      FLabel contactLabel = new FLabel(bundle.getString(prefix + "contactLabel"));

      final FPanel formPanel =
          UIUtils.createFormPanel(Arrays.asList(givenNameLabel, familyNameLabel, contactLabel),
              Arrays.asList(givenNameTextField, familyNameTextField, contactTextField));
      final FPanel northPanel = UIUtils.createNorthPanel(formPanel);
      add(northPanel);
    }

    void init(final VCard creator) {

      if (creator != null) {
        if (creator.getNickname() != null)
          givenNameTextField.setText(creator.getNickname().getValues().get(0));
        if (creator.getFormattedName() != null)
          familyNameTextField.setText(creator.getFormattedName().getValue());
        if (!creator.getEmails().isEmpty())
          contactTextField.setText(creator.getEmails().get(0).getValue());
      }
    }

    @Override
    VCard get() {

      final VCard vCard = new VCard();

      final String givenNameText = givenNameTextField.getText();
      if (StringUtils.isNotEmpty(givenNameText)) {
        vCard.setNickname(givenNameText);
      }

      final String familyNameText = familyNameTextField.getText();
      if (StringUtils.isNotEmpty(familyNameText)) {
        vCard.setFormattedName(familyNameText);
      }

      final String contactText = contactTextField.getText();
      if (StringUtils.isNotEmpty(contactText)) {
        vCard.addEmail(contactText);
      }

      return vCard;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "editor_EditCreatorPanel_";

      final List<String> errors = new ArrayList<>(3);
      if (givenNameTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "givenNameLabel"));
      }
      if (familyNameTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "familyNameLabel"));
      }
      if (contactTextField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "contactLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return Collections.emptyList();
    }
  }

  private class ScopePanel extends TopLevelPanel<Scope> {

    private static final long serialVersionUID = 8153319336584952056L;

    final JButton productButton = new JButton();
    final JButton hazardButton = new JButton();
    final JButton populationButton = new JButton();

    final FTextArea commentTextArea = new FTextArea();

    final FixedDateChooser dateChooser = new FixedDateChooser();
    final AutoSuggestField regionField = GUIFactory.createAutoSuggestField(vocabs.get("Region"));
    final AutoSuggestField countryField = GUIFactory.createAutoSuggestField(vocabs.get("Country"));

    private Scope scope = null;

    ScopePanel() {

      super(new BorderLayout());

      final JCheckBox advancedCheckBox = new JCheckBox("Advanced");

      String prefix = "editor_ScopePanel_";

      // Build UI
      productButton.setToolTipText("Click me to add a product");
      productButton.addActionListener(event -> {
        EditProductPanel editProductPanel = new EditProductPanel(advancedCheckBox.isSelected());
        if (scope != null) {
          editProductPanel.init(scope.product);
        }
        final ValidatableDialog dlg = new ValidatableDialog(editProductPanel, "Create a product");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Product product = editProductPanel.get();
          productButton
              .setText(String.format("%s_%s", product.environmentName, product.environmentUnit));
          scope.product = product;
        }
      });

      hazardButton.setToolTipText("Click me to add a hazard");
      hazardButton.addActionListener(event -> {
        EditHazardPanel editHazardPanel = new EditHazardPanel(advancedCheckBox.isSelected());
        if (scope != null) {
          editHazardPanel.init(scope.hazard);
        }
        final ValidatableDialog dlg = new ValidatableDialog(editHazardPanel, "Create a hazard");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Hazard hazard = editHazardPanel.get();
          hazardButton.setText(String.format("%s_%s", hazard.hazardName, hazard.hazardUnit));
          scope.hazard = hazard;
        }
      });

      populationButton.setToolTipText("Click me to add a Population group");
      populationButton.addActionListener(event -> {
        EditPopulationGroupPanel editPopulationGroupPanel =
            new EditPopulationGroupPanel(advancedCheckBox.isSelected());
        if (scope != null) {
          editPopulationGroupPanel.init(scope.populationGroup);
        }
        final ValidatableDialog dlg =
            new ValidatableDialog(editPopulationGroupPanel, "Create a Population Group");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final PopulationGroup populationGroup = editPopulationGroupPanel.get();
          populationButton.setText(populationGroup.populationName);
          scope.populationGroup = populationGroup;
        }
      });

      // Create labels
      final FLabel productLabel = new FLabel(bundle.getString(prefix + "productLabel"));
      final FLabel hazardLabel = new FLabel(bundle.getString(prefix + "hazardLabel"));
      final FLabel populationLabel = new FLabel(bundle.getString(prefix + "populationGroupLabel"));
      final FLabel temporalInformationLabel =
          GUIFactory.createLabelWithToolTip(prefix + "temporalInformation");
      final FLabel regionLabel = GUIFactory.createLabelWithToolTip(prefix + "region");
      final FLabel countryLabel = GUIFactory.createLabelWithToolTip(prefix + "country");

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(productLabel, hazardLabel, populationLabel, temporalInformationLabel,
              regionLabel, countryLabel),
          Arrays.asList(productButton, hazardButton, populationButton, dateChooser, regionField,
              countryField));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(formPanel);

      {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "comment");
        FPanel textAreaPanel = UIUtils.createFormPanel(Arrays.asList(label),
            Arrays.asList(new JScrollPane(commentTextArea)));
        northPanel.add(textAreaPanel);
      }
      add(northPanel, BorderLayout.NORTH);
    }

    void init(final Scope scope) {
      if (scope != null) {

        this.scope = scope;

        if (StringUtils.isNotBlank(scope.temporalInformation)) {
          try {
            dateChooser
                .setDate(new SimpleDateFormat("yyyy-MM-dd").parse(scope.temporalInformation));
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
        if (!scope.region.isEmpty()) {
          regionField.setSelectedItem(scope.region.get(0));
        }
        if (!scope.country.isEmpty()) {
          countryField.setSelectedItem(scope.country.get(0));
        }
      }
    }

    Scope get() {
      final Scope scope = new Scope();
      scope.product = this.scope.product;
      scope.hazard = this.scope.hazard;
      scope.populationGroup = this.scope.populationGroup;

      final Date date = dateChooser.getDate();
      if (date != null) {
        scope.temporalInformation =
            new SimpleDateFormat(dateChooser.getDateFormatString()).format(date);
      }

      final Object region = regionField.getSelectedItem();
      if (region != null) {
        scope.region.add((String) region);
      }

      final Object country = countryField.getSelectedItem();
      if (country != null) {
        scope.country.add((String) country);
      }

      return scope;
    }
  }

  private class DataBackgroundPanel extends TopLevelPanel<DataBackground> {

    private static final long serialVersionUID = 5789423098065477610L;

    final JCheckBox advancedCheckBox = new JCheckBox("Advanced");

    final AutoSuggestField laboratoryAccreditationField =
        GUIFactory.createAutoSuggestField(vocabs.get("Laboratory accreditation"));

    private final DataBackground dataBackground = new DataBackground();

    DataBackgroundPanel() {

      super(new BorderLayout());

      final StudyPanel studyPanel = new StudyPanel();
      studyPanel.setBorder(BorderFactory.createTitledBorder("Study"));

      final JButton studySampleButton = new JButton();
      studySampleButton.setToolTipText("Click me to add Study Sample");
      studySampleButton.addActionListener(event -> {

        EditStudySamplePanel editStudySamplePanel =
            new EditStudySamplePanel(advancedCheckBox.isSelected());

        final ValidatableDialog dlg =
            new ValidatableDialog(editStudySamplePanel, "Create Study sample");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final StudySample studySample = editStudySamplePanel.get();
          // Update button's text
          studySampleButton.setText(studySample.sample);

          dataBackground.studySample = studySample;
        }
      });

      final JButton dietaryAssessmentMethodButton = new JButton();
      dietaryAssessmentMethodButton.setToolTipText("Click me to add Dietary assessment method");
      dietaryAssessmentMethodButton.addActionListener(event -> {
        EditDietaryAssessmentMethodPanel editPanel =
            new EditDietaryAssessmentMethodPanel(advancedCheckBox.isSelected());
        final ValidatableDialog dlg =
            new ValidatableDialog(editPanel, "Create dietary assessment method");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final DietaryAssessmentMethod method = editPanel.get();
          // Update button's text
          dietaryAssessmentMethodButton.setText(method.collectionTool);
          dataBackground.dietaryAssessmentMethod = method;
        }
      });

      final JButton assayButton = new JButton();
      assayButton.setToolTipText("Click me to add Assay");
      assayButton.addActionListener(event -> {
        EditAssayPanel editPanel = new EditAssayPanel(advancedCheckBox.isSelected());
        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create assay");
        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Assay assay = editPanel.get();
          // Update button's text
          assayButton.setText(assay.name);
          dataBackground.assay = assay;
        }
      });

      String prefix = "editor_DataBackgroundPanel_";
      FLabel studySampleLabel = new FLabel(bundle.getString(prefix + "studySampleLabel"));
      FLabel dietaryAssessmentMethodLabel =
          new FLabel(bundle.getString(prefix + "dietaryAssessmentMethodLabel"));
      FLabel laboratoryAccreditationLabel =
          new FLabel(bundle.getString(prefix + "laboratoryAccreditationLabel"));
      FLabel assayLabel = new FLabel(bundle.getString(prefix + "assayLabel"));

      // Advanced `checkbox`
      advancedCheckBox.addItemListener(event -> {
        studyPanel.advancedComponents.forEach(it -> it.setEnabled(advancedCheckBox.isSelected()));
      });

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(studySampleLabel, dietaryAssessmentMethodLabel,
              laboratoryAccreditationLabel, assayLabel),
          Arrays.asList(studySampleButton, dietaryAssessmentMethodButton,
              laboratoryAccreditationField, assayButton));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(studyPanel);
      northPanel.add(formPanel);
      add(northPanel, BorderLayout.NORTH);
    }

    void init(final DataBackground dataBackground) {}

    DataBackground get() {
      final DataBackground dataBackground = new DataBackground();
      dataBackground.studySample = this.dataBackground.studySample;
      dataBackground.dietaryAssessmentMethod = this.dataBackground.dietaryAssessmentMethod;
      dataBackground.assay = this.dataBackground.assay;

      return dataBackground;
    }
  }

  private class StudyPanel extends JPanel {

    private static final long serialVersionUID = -6572236073945735826L;

    private final FTextField studyIdentifierTextField = new FTextField(true);
    private final FTextField studyTitleTextField = new FTextField(true);
    private final FTextArea studyDescriptionTextArea = new FTextArea();
    private final AutoSuggestField studyDesignTypeField;
    private final AutoSuggestField studyAssayMeasurementsTypeField;
    private final AutoSuggestField studyAssayTechnologyTypeField;
    private final FTextField studyAssayTechnologyPlatformTextField = new FTextField();
    private final AutoSuggestField accreditationProcedureField;
    private final FTextField studyProtocolNameTextField = new FTextField();
    private final AutoSuggestField studyProtocolTypeField;
    private final FTextField studyProtocolDescriptionTextField = new FTextField();
    private final FTextField studyProtocolURITextField = new FTextField();
    private final FTextField studyProtocolVersionTextField = new FTextField();
    private final AutoSuggestField studyProtocolParametersField;
    private final AutoSuggestField studyProtocolComponentsTypeField;

    private final List<JComponent> advancedComponents;

    StudyPanel() {

      super(new BorderLayout());

      studyDesignTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Study Design Type"));
      studyAssayMeasurementsTypeField =
          GUIFactory.createAutoSuggestField(vocabs.get("Study Assay Measurement Type"));
      studyAssayTechnologyTypeField =
          GUIFactory.createAutoSuggestField(vocabs.get("Study Assay Technology Type"));
      accreditationProcedureField =
          GUIFactory.createAutoSuggestField(vocabs.get("Accreditation procedure Ass.Tec"));
      studyProtocolTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Study Protocol Type"));
      studyProtocolParametersField =
          GUIFactory.createAutoSuggestField(vocabs.get("Study Protocol Parameters Name"));
      studyProtocolComponentsTypeField =
          GUIFactory.createAutoSuggestField(vocabs.get("Study Protocol Components Type"));

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();
      final String prefix = "editor_StudyPanel_";

      // study identifier
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyIdentifier"));
      fields.add(studyIdentifierTextField);

      // study title
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyTitle"));
      fields.add(studyTitleTextField);

      // study design type
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyDesignType"));
      fields.add(studyDesignTypeField);

      // study assay technology type
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyAssayTechnologyType"));
      fields.add(studyAssayTechnologyTypeField);

      // study assay measurements type
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyAssayMeasurementsType"));
      fields.add(studyAssayMeasurementsTypeField);

      // study assay technology platform
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyAssayTechnologyPlatform"));
      fields.add(studyAssayTechnologyPlatformTextField);

      // accreditation procedure for the assay technology
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "accreditationProcedure"));
      fields.add(accreditationProcedureField);

      // study protocol name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolName"));
      fields.add(studyProtocolNameTextField);

      // study protocol type
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolType"));
      fields.add(studyProtocolTypeField);

      // study protocol
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolDescription"));
      fields.add(studyProtocolDescriptionTextField);

      // study protocol URI
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolURI"));
      fields.add(studyProtocolURITextField);

      // study protocol parameters name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "parameters"));
      fields.add(studyProtocolParametersField);

      // study protocol components
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "componentsType"));
      fields.add(studyProtocolComponentsTypeField);

      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "studyDescription");
        FPanel textAreaPanel = UIUtils.createFormPanel(Arrays.asList(label),
            Arrays.asList(new JScrollPane(studyDescriptionTextArea)));
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);

      advancedComponents = Arrays.asList(studyDescriptionTextArea, studyDesignTypeField,
          studyAssayMeasurementsTypeField, studyAssayTechnologyTypeField,
          studyAssayTechnologyPlatformTextField, accreditationProcedureField,
          studyProtocolNameTextField, studyProtocolTypeField, studyProtocolDescriptionTextField,
          studyProtocolURITextField, studyProtocolVersionTextField, studyProtocolParametersField,
          studyProtocolComponentsTypeField);
      advancedComponents.forEach(it -> it.setEnabled(false));
    }
  }

  private class ModelMathPanel extends TopLevelPanel<ModelMath> {

    private static final long serialVersionUID = -7488943574135793595L;

    final JCheckBox advancedCheckBox = new JCheckBox("Advanced");

    private final ParametersPanel parametersPanel = new ParametersPanel(false);
    private final QualityMeasuresPanel qualityMeasuresPanel = new QualityMeasuresPanel();
    private final ModelEquationsPanel modelEquationsPanel = new ModelEquationsPanel(false);

    ModelMathPanel() {

      super(new BorderLayout());

      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(parametersPanel);
      northPanel.add(qualityMeasuresPanel);
      northPanel.add(modelEquationsPanel);
      add(northPanel, BorderLayout.NORTH);

      advancedCheckBox.addItemListener(event -> {
        parametersPanel.isAdvanced = advancedCheckBox.isSelected();
        modelEquationsPanel.toggleMode();
      });
    }

    @Override
    void init(final ModelMath modelMath) {

      if (modelMath != null) {

        parametersPanel.init(modelMath.parameter);

        // Initialize SSE, MSE, R2, RMSE, AIC and BIC
        qualityMeasuresPanel.sseSpinnerModel.setValue(modelMath.sse);
        qualityMeasuresPanel.mseSpinnerModel.setValue(modelMath.mse);
        qualityMeasuresPanel.r2SpinnerModel.setValue(modelMath.rSquared);
        qualityMeasuresPanel.rmseSpinnerModel.setValue(modelMath.rmse);
        qualityMeasuresPanel.aicSpinnerModel.setValue(modelMath.aic);
        qualityMeasuresPanel.bicSpinnerModel.setValue(modelMath.bic);

        // Initialize model equations
        modelEquationsPanel.init(modelMath.modelEquation);

        // TODO: init fitting procedure
        // TODO: init exposure
        // TODO: init events
      }
    }

    @Override
    ModelMath get() {

      final ModelMath modelMath = new ModelMath();

      // Save parameters
      modelMath.parameter.addAll(parametersPanel.tableModel.parameters);

      // Save SSE, MSE, R2, RMSE, AIC and BIC
      modelMath.sse = qualityMeasuresPanel.sseSpinnerModel.getNumber().doubleValue();
      modelMath.mse = qualityMeasuresPanel.mseSpinnerModel.getNumber().doubleValue();
      modelMath.rSquared = qualityMeasuresPanel.r2SpinnerModel.getNumber().doubleValue();
      modelMath.rmse = qualityMeasuresPanel.rmseSpinnerModel.getNumber().doubleValue();
      modelMath.aic = qualityMeasuresPanel.aicSpinnerModel.getNumber().doubleValue();
      modelMath.bic = qualityMeasuresPanel.bicSpinnerModel.getNumber().doubleValue();

      // Save model equations
      modelMath.modelEquation.addAll(modelEquationsPanel.tableModel.equations);

      // TODO: Save fitting procedure
      // TODO: Save exposure
      // TODO: Save events

      return modelMath;
    }
  }

  private class ParametersPanel extends JPanel {

    private static final long serialVersionUID = -5986975090954482038L;

    final TableModel tableModel = new TableModel();

    // Non modifiable table model with headers
    class TableModel extends DefaultTableModel {

      private static final long serialVersionUID = 1677268552154885327L;

      final ArrayList<Parameter> parameters = new ArrayList<>();

      TableModel() {
        super(new Object[0][0], new String[] {"ID", "Name", "Unit", "Unit category", "Data type"});
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

      void add(final Parameter param) {

        // Skip if param is already in table
        for (final Parameter currentParam : parameters) {
          if (currentParam.equals(param)) {
            return;
          }
        }
        parameters.add(param);

        addRow(new String[] {param.id, param.name, param.unit, param.unitCategory, param.dataType});
      }

      void modify(final int rowNumber, final Parameter param) {
        parameters.set(rowNumber, param);

        setValueAt(param.id, rowNumber, 0);
        setValueAt(param.name, rowNumber, 1);
        setValueAt(param.unit, rowNumber, 2);
        setValueAt(param.unitCategory, rowNumber, 3);
        setValueAt(param.dataType, rowNumber, 4);
      }

      void remove(final int rowNumber) {
        parameters.remove(rowNumber);
        removeRow(rowNumber);
      }
    }

    boolean isAdvanced;

    ParametersPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      this.isAdvanced = isAdvanced;

      setBorder(BorderFactory.createTitledBorder("Parameters"));

      final JTable myTable = UIUtils.createTable(tableModel);

      // buttons
      final JButton addButton = UIUtils.createAddButton();
      addButton.addActionListener(event -> {
        final EditParameterPanel editPanel = new EditParameterPanel(this.isAdvanced);
        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create parameter");
        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          tableModel.add(editPanel.get());
        }
      });

      final JButton editButton = UIUtils.createEditButton();
      editButton.addActionListener(event -> {
        final int rowToEdit = myTable.getSelectedRow();
        if (rowToEdit != -1) {

          final EditParameterPanel editPanel = new EditParameterPanel(this.isAdvanced);
          editPanel.init(tableModel.parameters.get(rowToEdit));

          final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Modify parameter");
          if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
            tableModel.modify(rowToEdit, editPanel.get());
          }
        }
      });

      final JButton removeButton = UIUtils.createRemoveButton();
      removeButton.addActionListener(event -> {
        final int rowToDelete = myTable.getSelectedRow();
        if (rowToDelete != -1) {
          tableModel.remove(rowToDelete);
        }
      });

      final JPanel panel = UI.createTablePanel(myTable);

      final JPanel buttonsPanel = UI.createHorizontalPanel(addButton, editButton, removeButton);
      panel.add(UI.createCenterPanel(buttonsPanel), BorderLayout.SOUTH);

      add(panel);
    }

    void init(final List<Parameter> parameters) {
      parameters.forEach(tableModel::add);
    }
  }

  private class QualityMeasuresPanel extends JPanel {

    private static final long serialVersionUID = -5829602676812905793L;

    final SpinnerNumberModel sseSpinnerModel = GUIFactory.createSpinnerDoubleModel();
    final SpinnerNumberModel mseSpinnerModel = GUIFactory.createSpinnerDoubleModel();
    final SpinnerNumberModel rmseSpinnerModel = GUIFactory.createSpinnerDoubleModel();
    final SpinnerNumberModel r2SpinnerModel = GUIFactory.createSpinnerDoubleModel();
    final SpinnerNumberModel aicSpinnerModel = GUIFactory.createSpinnerDoubleModel();
    final SpinnerNumberModel bicSpinnerModel = GUIFactory.createSpinnerDoubleModel();

    QualityMeasuresPanel() {

      super(new BorderLayout());

      final JPanel ssePanel = new JPanel();
      ssePanel.add(new JLabel("SSE"));
      ssePanel.add(GUIFactory.createSpinner(sseSpinnerModel));

      final JPanel msePanel = new JPanel();
      msePanel.add(new JLabel("MSE"));
      msePanel.add(GUIFactory.createSpinner(mseSpinnerModel));

      final JPanel rmsePanel = new JPanel();
      rmsePanel.add(new JLabel("RMSE"));
      rmsePanel.add(GUIFactory.createSpinner(rmseSpinnerModel));

      final JPanel r2Panel = new JPanel();
      r2Panel.add(new JLabel("r-Squared"));
      r2Panel.add(GUIFactory.createSpinner(r2SpinnerModel));

      final JPanel aicPanel = new JPanel();
      aicPanel.add(new JLabel("AIC"));
      aicPanel.add(GUIFactory.createSpinner(aicSpinnerModel));

      final JPanel bicPanel = new JPanel();
      bicPanel.add(new JLabel("BIC"));
      bicPanel.add(GUIFactory.createSpinner(bicSpinnerModel));

      final JPanel horizontalPanel =
          UI.createHorizontalPanel(ssePanel, msePanel, rmsePanel, r2Panel, aicPanel, bicPanel);
      final JPanel centeredPanel = UI.createCenterPanel(horizontalPanel);

      add(centeredPanel);

      setBorder(BorderFactory.createTitledBorder("Quality measures"));
    }
  }

  private class ModelEquationsPanel extends JPanel {

    private static final long serialVersionUID = 7194287921709100267L;

    final TableModel tableModel = new TableModel();

    boolean isAdvanced;

    class TableModel extends DefaultTableModel {

      private static final long serialVersionUID = 6615864381589787261L;

      final ArrayList<ModelEquation> equations = new ArrayList<>();

      public TableModel() {
        super(new Object[0][0], new String[] {"Name", "Equation"});
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

      void add(final ModelEquation equation) {

        // Skip if equation is already in table
        for (final ModelEquation e : equations) {
          if (e.equals(equation)) {
            return;
          }
        }
        equations.add(equation);

        addRow(new String[] {equation.equationName, equation.equation});
      }

      void modify(final int rowNumber, final ModelEquation equation) {
        equations.set(rowNumber, equation);

        setValueAt(equation.equationName, rowNumber, 0);
        setValueAt(equation.equation, rowNumber, 1);
      }

      void remove(final int rowNumber) {
        equations.remove(rowNumber);
        removeRow(rowNumber);
      }
    }

    ModelEquationsPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      this.isAdvanced = isAdvanced;

      setBorder(BorderFactory.createTitledBorder("Model equation"));

      final JTable myTable = UIUtils.createTable(tableModel);

      final JButton addButton = UIUtils.createAddButton();
      addButton.addActionListener(event -> {

        EditModelEquationPanel editPanel = new EditModelEquationPanel(this.isAdvanced);

        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create equation");
        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          tableModel.add(editPanel.get());
        }
      });

      final JButton editButton = UIUtils.createEditButton();
      editButton.addActionListener(event -> {
        final int rowToEdit = myTable.getSelectedRow();
        if (rowToEdit != -1) {

          EditModelEquationPanel editPanel = new EditModelEquationPanel(this.isAdvanced);
          editPanel.init(tableModel.equations.get(rowToEdit));

          final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Modify equation");
          if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
            tableModel.modify(rowToEdit, editPanel.get());
          }
        }
      });

      final JButton removeButton = UIUtils.createRemoveButton();
      removeButton.addActionListener(event -> {
        final int rowToDelete = myTable.getSelectedRow();
        if (rowToDelete != -1) {
          tableModel.remove(rowToDelete);
        }
      });

      final JPanel panel = UI.createTablePanel(myTable);

      final JPanel buttonsPanel = UI.createHorizontalPanel(addButton, editButton, removeButton);
      panel.add(UI.createCenterPanel(buttonsPanel), BorderLayout.SOUTH);

      add(panel);
    }

    void toggleMode() {
      this.isAdvanced = !this.isAdvanced;
    }

    void init(final List<ModelEquation> modelEquations) {
      modelEquations.forEach(tableModel::add);
    }
  }

  /**
   * Panel to edit a {@link Simulation}.
   * 
   * Fields:
   * <ul>
   * <li>Simulation algorithm: Mandatory
   * <li>Simulated model: Mandatory
   * <li>Simulation description: Optional
   * <li>Visualization script: Optional
   * </ul>
   */
  private class SimulationPanel extends TopLevelPanel<Simulation> {

    private static final long serialVersionUID = -371214370549912535L;

    private final FTextField algorithmField;
    private final FTextField modelField;
    private final FTextField scriptField;
    private final FTextArea descriptionField;

    public SimulationPanel() {

      super(new BorderLayout());

      algorithmField = new FTextField(true);
      modelField = new FTextField(true);
      scriptField = new FTextField();
      descriptionField = new FTextArea();

      createUI();
    }

    private void createUI() {
      String prefix = "editor_Simulation_";
      final FLabel algorithmLabel = new FLabel(bundle.getString(prefix + "Algorithm"));
      final FLabel modelLabel = new FLabel(bundle.getString(prefix + "Model"));
      final FLabel scriptLabel = new FLabel(bundle.getString(prefix + "Script"));

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // algorithm
      labels.add(new FLabel(bundle.getString(prefix + "Algorithm")));
      fields.add(algorithmField);

      // model
      labels.add(new FLabel(bundle.getString(prefix + "Model")));
      fields.add(modelField);

      // script
      labels.add(new FLabel(bundle.getString(prefix + "Script")));
      fields.add(scriptField);

      FPanel formPanel =
          UIUtils.createFormPanel(Arrays.asList(algorithmLabel, modelLabel, scriptLabel),
              Arrays.asList(algorithmField, modelField, scriptField));

      final JCheckBox advancedCheckBox = new JCheckBox("Advanced");
      advancedCheckBox.addItemListener(event -> {
        final boolean isAdvanced = advancedCheckBox.isSelected();
        scriptField.setEnabled(isAdvanced);
        descriptionField.setEnabled(isAdvanced);
      });

      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(formPanel);

      {
        FLabel label = new FLabel(bundle.getString("editor_Simulation_Description"));
        FPanel textAreaPanel = UIUtils.createFormPanel(Arrays.asList(label),
            Arrays.asList(new JScrollPane(descriptionField)));
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);

      // Initially the advanced mode is disabled, so advanced components must be
      // disabled
      scriptField.setEnabled(false);
      descriptionField.setEnabled(false);
    }

    @Override
    void init(final Simulation t) {
      if (t != null) {
        algorithmField.setText(t.algorithm);
        modelField.setText(t.simulatedModel);
        scriptField.setText(t.visualizationScript);
        descriptionField.setText(t.description);
      }
    }

    @Override
    Simulation get() {
      final Simulation simulation = new Simulation();
      simulation.algorithm = algorithmField.getText();
      simulation.simulatedModel = modelField.getText();
      simulation.visualizationScript = scriptField.getText();
      simulation.description = descriptionField.getText();

      return simulation;
    }
  }
}
