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
// import de.bund.bfr.knime.UI;
import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.ui.FixedDateChooser;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
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
import de.bund.bfr.swing.StringTextArea;
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

    /**
     * Create a JLabel with no tooltip, retrieving its text from resource bundle. This is a
     * convenience method for {@link #createLabel(String, boolean)} where the property is not
     * mandatory.
     * 
     * @param textKey Key of the JLabel text in the resource bundle
     * @return JLabel
     */
    private static JLabel createLabel(final String textKey) {
      return createLabel(textKey, false);
    }

    /**
     * Create a JLabel with no tooltip, retrieving its text from resource bundle.
     * 
     * @param textKey Key of the JLabel text in the resource bundle
     * @param isMandatory Whether the property described by the JLabel is mandatory
     * @return JLabel
     */
    private static JLabel createLabel(final String textKey, final boolean isMandatory) {
      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      return new JLabel(bundle.getString(textKey) + (isMandatory ? "*" : ""));
    }

    /**
     * Create a JLabel retrieving text and tool tip text from resource bundle. This is a convenience
     * method for {@link #createLabel(String, String, boolean)} where the property is not mandatory.
     * 
     * @param textKey Key of the JLabel text in the resource bundle
     * @param toolTipKey Key of the tool tip text in the resource bundle
     * @return JLabel describing an optional property.
     */
    private static JLabel createLabel(final String textKey, final String toolTipKey) {
      return createLabel(textKey, toolTipKey, false);
    }

    /**
     * Create a JLabel retrieving text and tool tip text from resource bundle.
     * 
     * @param textKey Key of the JLabel text in the resource bundle
     * @param toolTipKey Key of the tool tip text in the resource bundle
     * @param isMandatory Whether the property described by the JLabel is mandatory
     * @return JLabel
     */
    private static JLabel createLabel(final String textKey, final String toolTipKey,
        final boolean isMandatory) {
      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final JLabel label = new JLabel(bundle.getString(textKey) + (isMandatory ? "*" : ""));
      label.setToolTipText(bundle.getString(toolTipKey));

      return label;
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

    private final StringTextField nameTextField = new StringTextField(false, 30);
    private final StringTextArea descriptionTextArea = new StringTextArea(true, 5, 30);

    private final List<JComponent> advancedComponents;

    EditAssayPanel(final boolean isAdvanced) {
      super(new BorderLayout());

      // Create labels
      final JLabel nameLabel = GUIFactory.createLabel("GM.EditAssayPanel.nameLabel",
          "GM.EditAssayPanel.nameTooltip", true);

      // Name
      final JPanel formPanel =
          UI.createOptionsPanel(Arrays.asList(nameLabel), Arrays.asList(nameTextField));

      // Description
      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      JPanel descriptionPanel = UI.createTitledPanel(new JScrollPane(descriptionTextArea),
          bundle.getString("GM.EditAssayPanel.descriptionLabel"));
      descriptionPanel.setToolTipText(bundle.getString("GM.EditAssayPanel.descriptionTooltip"));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      northPanel.add(descriptionPanel);
      add(northPanel, BorderLayout.NORTH);

      advancedComponents = Arrays.asList(descriptionTextArea, descriptionPanel);

      // If advanced mode shows advanced components
      advancedComponents.forEach(it -> it.setEnabled(isAdvanced));
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
      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>(1);
      if (!nameTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditAssayPanel.nameLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return advancedComponents;
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

    private final AutoSuggestField dataCollectionToolField =
        GUIFactory.createAutoSuggestField(vocabs.get("Method. tool to collect data"));
    private final StringTextField nonConsecutiveOneDayField = new StringTextField(true, 30);
    private final StringTextField dietarySoftwareToolField = new StringTextField(false, 30);
    private final StringTextField foodItemNumberField = new StringTextField(false, 30);
    private final StringTextField recordTypeField = new StringTextField(false, 30);
    private final JComboBox<String> foodDescriptionField =
        GUIFactory.createComboBox(vocabs.get("Food descriptors"));
    private final List<JComponent> advancedComponents;

    EditDietaryAssessmentMethodPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      // Create labels
      final JLabel dataCollectionToolLabel =
          GUIFactory.createLabel("GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel",
              "GM.EditDietaryAssessmentMethodPanel.dataCollectionToolTooltip", true);
      final JLabel nonConsecutiveOneDayLabel =
          GUIFactory.createLabel("GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDaysLabel",
              "GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDaysTooltip", true);
      final JLabel dietarySoftwareToolLabel =
          GUIFactory.createLabel("GM.EditDietaryAssessmentMethodPanel.dietarySoftwareToolLabel",
              "GM.EditDietaryAssessmentMethodPanel.dietarySoftwareToolTooltip");
      final JLabel foodItemNumberLabel =
          GUIFactory.createLabel("GM.EditDietaryAssessmentMethodPanel.foodItemNumberLabel",
              "GM.EditDietaryAssessmentMethodPanel.foodItemNumberTooltip");
      final JLabel recordTypeLabel =
          GUIFactory.createLabel("GM.EditDietaryAssessmentMethodPanel.recordTypeLabel",
              "GM.EditDietaryAssessmentMethodPanel.recordTypeTooltip");
      final JLabel foodDescriptionLabel =
          GUIFactory.createLabel("GM.EditDietaryAssessmentMethodPanel.foodDescriptionLabel",
              "GM.EditDietaryAssessmentMethodPanel.foodDescriptionTooltip");

      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(dataCollectionToolLabel, nonConsecutiveOneDayLabel,
              dietarySoftwareToolLabel, foodItemNumberLabel, recordTypeLabel, foodDescriptionLabel),
          Arrays.asList(dataCollectionToolField, nonConsecutiveOneDayField,
              dietarySoftwareToolField, foodItemNumberField, recordTypeField,
              foodDescriptionField));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      add(northPanel, BorderLayout.NORTH);

      advancedComponents = Arrays.asList(dietarySoftwareToolField, foodItemNumberField,
          recordTypeField, foodDescriptionField);

      // If simple mode hides advanced components
      if (!isAdvanced) {
        advancedComponents.forEach(it -> it.setEnabled(false));
      }
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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(dataCollectionToolField)) {
        errors.add("Missing "
            + bundle.getString("GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel"));
      }
      if (!nonConsecutiveOneDayField.isValueValid()) {
        errors.add("Missing "
            + bundle.getString("GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDaysLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return advancedComponents;
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
   * <td>Optional</td>
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

    private final AutoSuggestField hazardTypeField =
        GUIFactory.createAutoSuggestField(vocabs.get("Hazard type"));
    private final AutoSuggestField hazardNameField =
        GUIFactory.createAutoSuggestField(vocabs.get("Hazard name"));
    private final StringTextArea hazardDescriptionTextArea = new StringTextArea(true, 5, 30);
    private final AutoSuggestField hazardUnitField =
        GUIFactory.createAutoSuggestField(vocabs.get("Hazard unit"));
    private final StringTextField adverseEffectTextField = new StringTextField(true, 30);
    private final StringTextField originTextField = new StringTextField(true, 30);
    private final StringTextField bmdTextField = new StringTextField(true, 30);
    private final StringTextField maxResidueLimitTextField = new StringTextField(true, 30);
    private final StringTextField noObservedAdverseTextField = new StringTextField(true, 30);
    private final StringTextField acceptableOperatorTextField = new StringTextField(true, 30);
    private final StringTextField acuteReferenceDoseTextField = new StringTextField(true, 30);
    private final StringTextField acceptableDailyIntakeTextField = new StringTextField(true, 30);
    private final AutoSuggestField indSumField =
        GUIFactory.createAutoSuggestField(vocabs.get("Hazard ind sum"));
    private final StringTextField labNameTextField = new StringTextField(true, 30);
    private final AutoSuggestField labCountryField =
        GUIFactory.createAutoSuggestField(vocabs.get("Laboratory country"));
    private final StringTextField detectionLimitTextField = new StringTextField(true, 30);
    private final StringTextField quantificationLimitTextField = new StringTextField(true, 30);
    private final StringTextField leftCensoredDataTextField = new StringTextField(true, 30);
    private final StringTextField contaminationRangeTextField = new StringTextField(true, 30);

    private final List<JComponent> advancedComponents;

    EditHazardPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      final JLabel hazardTypeLabel = GUIFactory.createLabel("GM.EditHazardPanel.hazardTypeLabel",
          "GM.EditHazardPanel.hazardTypeTooltip", true);
      final JLabel hazardNameLabel = GUIFactory.createLabel("GM.EditHazardPanel.hazardNameLabel",
          "GM.EditHazardPanel.hazardNameTooltip", true);
      final JLabel hazardUnitLabel = GUIFactory.createLabel("GM.EditHazardPanel.hazardUnitLabel",
          "GM.EditHazardPanel.hazardUnitTooltip", true);
      final JLabel adverseEffectLabel = GUIFactory.createLabel(
          "GM.EditHazardPanel.adverseEffectLabel", "GM.EditHazardPanel.adverseEffectTooltip");
      final JLabel originLabel = GUIFactory.createLabel("GM.EditHazardPanel.originLabel",
          "GM.EditHazardPanel.originTooltip");
      final JLabel bmdLabel =
          GUIFactory.createLabel("GM.EditHazardPanel.bmdLabel", "GM.EditHazardPanel.bmdTooltip");
      final JLabel maxResidueLimitLabel = GUIFactory.createLabel(
          "GM.EditHazardPanel.maxResidueLimitLabel", "GM.EditHazardPanel.maxResidueLimitTooltip");
      final JLabel noObservedAdverseLabel =
          GUIFactory.createLabel("GM.EditHazardPanel.noObservedAdverseLabel",
              "GM.EditHazardPanel.noObservedAdverseTooltip");
      final JLabel acceptableOperatorLabel =
          GUIFactory.createLabel("GM.EditHazardPanel.acceptableOperatorLabel",
              "GM.EditHazardPanel.acceptableOperatorTooltip");
      final JLabel acuteReferenceDoseLabel =
          GUIFactory.createLabel("GM.EditHazardPanel.acuteReferenceDoseLabel",
              "GM.EditHazardPanel.acuteReferenceDoseTooltip");
      final JLabel indSumLabel = GUIFactory.createLabel("GM.EditHazardPanel.indSumLabel",
          "GM.EditHazardPanel.indSumTooltip");
      final JLabel acceptableDailyIntakeLabel =
          GUIFactory.createLabel("GM.EditHazardPanel.acceptableDailyIntakeLabel",
              "GM.EditHazardPanel.acceptableDailyIntakeTooltip");
      final JLabel labNameLabel = GUIFactory.createLabel("GM.EditHazardPanel.labNameLabel",
          "GM.EditHazardPanel.labNameTooltip");
      final JLabel labCountryLabel = GUIFactory.createLabel("GM.EditHazardPanel.labCountryLabel",
          "GM.EditHazardPanel.labCountryTooltip");
      final JLabel detectionLimitLabel = GUIFactory.createLabel(
          "GM.EditHazardPanel.detectionLimitLabel", "GM.EditHazardPanel.detectionLimitTooltip");
      final JLabel quantificationLimitLabel =
          GUIFactory.createLabel("GM.EditHazardPanel.quantificationLimitLabel",
              "GM.EditHazardPanel.quantificationLimitTooltip");
      final JLabel leftCensoredDataLabel = GUIFactory.createLabel(
          "GM.EditHazardPanel.leftCensoredDataLabel", "GM.EditHazardPanel.leftCensoredDataTooltip");
      final JLabel contaminationRangeLabel =
          GUIFactory.createLabel("GM.EditHazardPanel.contaminationRangeLabel",
              "GM.EditHazardPanel.contaminationRangeTooltip");

      // Wraps hazardDescriptionTextArea in a JScrollPane
      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      final JScrollPane hazardDescriptionPanel = new JScrollPane(hazardDescriptionTextArea);
      hazardDescriptionPanel.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.EditHazardPanel.hazardDescriptionLabel")));
      hazardDescriptionPanel
          .setToolTipText(bundle.getString("GM.EditHazardPanel.hazardDescriptionTooltip"));

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(hazardTypeLabel, hazardNameLabel, hazardUnitLabel, adverseEffectLabel,
              originLabel, bmdLabel, maxResidueLimitLabel, noObservedAdverseLabel,
              acceptableOperatorLabel, acuteReferenceDoseLabel, indSumLabel,
              acceptableDailyIntakeLabel, labNameLabel, labCountryLabel, detectionLimitLabel,
              quantificationLimitLabel, leftCensoredDataLabel, contaminationRangeLabel),
          Arrays.asList(hazardTypeField, hazardNameField, hazardUnitField, adverseEffectTextField,
              originTextField, bmdTextField, maxResidueLimitTextField, noObservedAdverseTextField,
              acceptableOperatorTextField, acuteReferenceDoseTextField, indSumField,
              acceptableDailyIntakeTextField, labNameTextField, labCountryField,
              detectionLimitTextField, quantificationLimitTextField, leftCensoredDataTextField,
              contaminationRangeTextField));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      northPanel.add(hazardDescriptionPanel);

      add(northPanel, BorderLayout.NORTH);

      advancedComponents = Arrays.asList(hazardDescriptionTextArea, adverseEffectTextField,
          originTextField, bmdTextField, maxResidueLimitTextField, acceptableOperatorTextField,
          noObservedAdverseTextField, acuteReferenceDoseTextField, acceptableDailyIntakeTextField,
          indSumField, labNameTextField, labCountryField, detectionLimitTextField,
          quantificationLimitTextField, leftCensoredDataTextField, contaminationRangeTextField);

      // If simple mode hide advanced components
      if (!isAdvanced) {
        getAdvancedComponents().forEach(it -> it.setEnabled(false));
      }
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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(hazardTypeField)) {
        errors.add("Missing " + bundle.getString("GM.EditHazardPanel.hazardTypeLabel"));
      }
      if (!hasValidValue(hazardNameField)) {
        errors.add("Missing " + bundle.getString("GM.EditHazardPanel.hazardNameLabel"));
      }
      if (!hasValidValue(hazardUnitField)) {
        errors.add("Missing " + bundle.getString("GM.EditHazardPanel.hazardUnitLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return advancedComponents;
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

    private final StringTextField equationNameTextField = new StringTextField(false, 30);
    private final StringTextField equationClassTextField = new StringTextField(true, 30);
    private final ReferencePanel referencePanel;
    private final StringTextArea scriptTextArea = new StringTextArea(false, 5, 30);

    private final List<JComponent> advancedComponents;

    EditModelEquationPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      // Create labels
      final JLabel equationNameLabel = GUIFactory.createLabel("GM.EditModelEquationPanel.nameLabel",
          "GM.EditModelEquationPanel.nameTooltip", true);
      final JLabel equationClassLabel = GUIFactory.createLabel(
          "GM.EditModelEquationPanel.classLabel", "GM.EditModelEquationPanel.classTooltip");

      referencePanel = new ReferencePanel(isAdvanced);

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      scriptTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.EditModelEquationPanel.scriptLabel")));
      scriptTextArea.setToolTipText(bundle.getString("GM.EditModelEquationPanel.scriptTooltip"));

      // formPanel
      final JPanel formPanel =
          UI.createOptionsPanel(Arrays.asList(equationNameLabel, equationClassLabel),
              Arrays.asList(equationNameTextField, equationClassTextField));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      northPanel.add(referencePanel);
      northPanel.add(new JScrollPane(scriptTextArea));
      add(northPanel, BorderLayout.NORTH);

      advancedComponents = Arrays.asList(equationClassTextField, referencePanel);

      // If simple mode hide advanced components
      if (!isAdvanced) {
        advancedComponents.forEach(it -> it.setEnabled(false));
      }
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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>();
      if (!equationNameTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditModelEquationPanel.nameLabel"));
      }
      if (!scriptTextArea.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditModelEquationPanel.scriptLabel"));
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
      return advancedComponents;
    }
  }

  private class EditParameterPanel extends EditPanel<Parameter> {

    private static final long serialVersionUID = 1826555468897327895L;

    private final StringTextField idTextField = new StringTextField(false, 30);
    private final JComboBox<Parameter.Classification> classificationComboBox =
        new JComboBox<>(Parameter.Classification.values());
    private final StringTextField nameTextField = new StringTextField(false, 30);
    private final StringTextArea descriptionTextArea = new StringTextArea(true, 5, 30);
    private final AutoSuggestField typeField =
        GUIFactory.createAutoSuggestField(vocabs.get("Parameter type"));
    private final AutoSuggestField unitField =
        GUIFactory.createAutoSuggestField(vocabs.get("Parameter unit"));
    private final AutoSuggestField unitCategoryField =
        GUIFactory.createAutoSuggestField(vocabs.get("Parameter unit category"));
    private final AutoSuggestField dataTypeField =
        GUIFactory.createAutoSuggestField(vocabs.get("Parameter data type"));
    private final AutoSuggestField sourceField =
        GUIFactory.createAutoSuggestField(vocabs.get("Parameter source"));
    private final AutoSuggestField subjectField =
        GUIFactory.createAutoSuggestField(vocabs.get("Parameter subject"));
    private final AutoSuggestField distributionField =
        GUIFactory.createAutoSuggestField(vocabs.get("Parameter distribution"));
    private final StringTextField valueTextField = new StringTextField(false, 30);
    private final StringTextField referenceTextField = new StringTextField(false, 30);
    private final StringTextArea variabilitySubjectTextArea = new StringTextArea(false, 5, 30);
    private final StringTextArea applicabilityTextArea = new StringTextArea(false, 5, 30);
    private SpinnerNumberModel errorSpinnerModel = GUIFactory.createSpinnerDoubleModel();

    private final List<JComponent> advancedComponents;

    public EditParameterPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      final JLabel idLabel = GUIFactory.createLabel("GM.EditParameterPanel.idLabel",
          "GM.EditParameterPanel.idTooltip", true);
      final JLabel classificationLabel =
          GUIFactory.createLabel("GM.EditParameterPanel.classificationLabel",
              "GM.EditParameterPanel.classificationTooltip", true);
      final JLabel nameLabel = GUIFactory.createLabel("GM.EditParameterPanel.parameterNameLabel",
          "GM.EditParameterPanel.parameterNameTooltip", true);
      final JLabel typeLabel = GUIFactory.createLabel("GM.EditParameterPanel.typeLabel",
          "GM.EditParameterPanel.typeTooltip");
      final JLabel unitLabel = GUIFactory.createLabel("GM.EditParameterPanel.unitLabel",
          "GM.EditParameterPanel.unitTooltip", true);
      final JLabel unitCategoryLabel =
          GUIFactory.createLabel("GM.EditParameterPanel.unitCategoryLabel",
              "GM.EditParameterPanel.unitCategoryTooltip", true);
      final JLabel dataTypeLabel = GUIFactory.createLabel("GM.EditParameterPanel.dataTypeLabel",
          "GM.EditParameterPanel.dataTypeTooltip", true);
      final JLabel sourceLabel = GUIFactory.createLabel("GM.EditParameterPanel.sourceLabel",
          "GM.EditParameterPanel.sourceTooltip");
      final JLabel subjectLabel = GUIFactory.createLabel("GM.EditParameterPanel.subjectLabel",
          "GM.EditParameterPanel.subjectTooltip");
      final JLabel distributionLabel = GUIFactory.createLabel(
          "GM.EditParameterPanel.distributionLabel", "GM.EditParameterPanel.distributionTooltip");
      final JLabel valueLabel = GUIFactory.createLabel("GM.EditParameterPanel.valueLabel",
          "GM.EditParameterPanel.valueTooltip");
      final JLabel referenceLabel = GUIFactory.createLabel("GM.EditParameterPanel.referenceLabel",
          "GM.EditParameterPanel.referenceTooltip");
      final JLabel errorLabel = GUIFactory.createLabel("GM.EditParameterPanel.errorLabel",
          "GM.EditParameterPanel.errorTooltip");

      // Build UI
      final JSpinner errorSpinner = GUIFactory.createSpinner(errorSpinnerModel);

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      descriptionTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.EditParameterPanel.descriptionLabel")));
      descriptionTextArea
          .setToolTipText(bundle.getString("GM.EditParameterPanel.descriptionTooltip"));

      variabilitySubjectTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.EditParameterPanel.variabilitySubjectLabel")));
      variabilitySubjectTextArea
          .setToolTipText(bundle.getString("GM.EditParameterPanel.variabilitySubjectTooltip"));

      applicabilityTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.EditParameterPanel.applicabilityLabel")));
      applicabilityTextArea
          .setToolTipText(bundle.getString("GM.EditParameterPanel.applicabilityTooltip"));

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(idLabel, classificationLabel, nameLabel, typeLabel, unitLabel,
              unitCategoryLabel, dataTypeLabel, sourceLabel, subjectLabel, distributionLabel,
              valueLabel, referenceLabel, errorLabel),
          Arrays.asList(idTextField, classificationComboBox, nameTextField, typeField, unitField,
              unitCategoryField, dataTypeField, sourceField, subjectField, distributionField,
              valueTextField, referenceTextField, errorSpinner));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      northPanel.add(new JScrollPane(descriptionTextArea));
      northPanel.add(new JScrollPane(variabilitySubjectTextArea));
      northPanel.add(new JScrollPane(applicabilityTextArea));
      add(northPanel, BorderLayout.NORTH);

      advancedComponents = Arrays.asList(descriptionTextArea, typeField, sourceField, subjectField,
          distributionField, valueTextField, referenceTextField, variabilitySubjectTextArea,
          applicabilityTextArea, errorSpinner);

      // If simple mode hide advanced components
      if (!isAdvanced) {
        advancedComponents.forEach(it -> it.setEnabled(false));
      }
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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>();
      if (!idTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.idLabel"));
      }
      if (classificationComboBox.getSelectedIndex() == -1) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.classificationLabel"));
      }
      if (!nameTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.parameterNameLabel"));
      }
      if (!hasValidValue(unitField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.unitLabel"));
      }
      if (!hasValidValue(unitCategoryField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.unitCategoryLabel"));
      }
      if (!hasValidValue(dataTypeField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.dataTypeLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return advancedComponents;
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

    private final StringTextField populationNameTextField = new StringTextField(false, 30);
    private final StringTextField targetPopulationTextField = new StringTextField(true, 30);
    private final StringTextField populationSpanTextField = new StringTextField(true, 30);
    private final StringTextArea populationDescriptionTextArea = new StringTextArea(true, 5, 30);
    private final StringTextField populationAgeTextField = new StringTextField(true, 30);
    private final StringTextField populationGenderTextField = new StringTextField(true, 30);
    private final StringTextField bmiTextField = new StringTextField(true, 30);
    private final StringTextField specialDietGroupTextField = new StringTextField(true, 30);
    private final StringTextField patternConsumptionTextField = new StringTextField(true, 30);
    private final JComboBox<String> regionComboBox =
        GUIFactory.createComboBox(vocabs.get("Region"));
    private final JComboBox<String> countryComboBox =
        GUIFactory.createComboBox(vocabs.get("Country"));
    private final StringTextField riskTextField = new StringTextField(true, 30);
    private final StringTextField seasonTextField = new StringTextField(true, 30);

    private final List<JComponent> advancedComponents;

    public EditPopulationGroupPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      final JLabel populationNameLabel =
          GUIFactory.createLabel("GM.EditPopulationGroupPanel.populationNameLabel",
              "GM.EditPopulationGroupPanel.populationNameTooltip", true);
      final JLabel targetPopulationLabel =
          GUIFactory.createLabel("GM.EditPopulationGroupPanel.targetPopulationLabel",
              "GM.EditPopulationGroupPanel.targetPopulationTooltip");
      final JLabel populationSpanLabel =
          GUIFactory.createLabel("GM.EditPopulationGroupPanel.populationSpanLabel",
              "GM.EditPopulationGroupPanel.populationSpanTooltip");
      final JLabel populationAgeLabel =
          GUIFactory.createLabel("GM.EditPopulationGroupPanel.populationAgeLabel",
              "GM.EditPopulationGroupPanel.populationAgeTooltip");
      final JLabel populationGenderLabel =
          GUIFactory.createLabel("GM.EditPopulationGroupPanel.populationGenderLabel",
              "GM.EditPopulationGroupPanel.populationGenderTooltip");
      final JLabel bmiLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.bmiLabel",
          "GM.EditPopulationGroupPanel.bmiTooltip");
      final JLabel specialDietGroupLabel =
          GUIFactory.createLabel("GM.EditPopulationGroupPanel.specialDietGroupsLabel",
              "GM.EditPopulationGroupPanel.specialDietGroupsTooltip");
      final JLabel patternConsumptionLabel =
          GUIFactory.createLabel("GM.EditPopulationGroupPanel.patternConsumptionLabel",
              "GM.EditPopulationGroupPanel.patternConsumptionTooltip");
      final JLabel regionLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.regionLabel",
          "GM.EditPopulationGroupPanel.regionTooltip");
      final JLabel countryLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.countryLabel",
          "GM.EditPopulationGroupPanel.countryTooltip");
      final JLabel riskLabel =
          GUIFactory.createLabel("GM.EditPopulationGroupPanel.riskAndPopulationLabel",
              "GM.EditPopulationGroupPanel.riskAndPopulationTooltip");
      final JLabel seasonLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.seasonLabel",
          "GM.EditPopulationGroupPanel.seasonTooltip");

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      populationDescriptionTextArea.setBorder(BorderFactory.createTitledBorder(
          bundle.getString("GM.EditPopulationGroupPanel.populationDescriptionLabel")));
      populationDescriptionTextArea.setToolTipText(
          bundle.getString("GM.EditPopulationGroupPanel.populationDescriptionTooltip"));

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(populationNameLabel, targetPopulationLabel, populationSpanLabel,
              populationAgeLabel, populationGenderLabel, bmiLabel, specialDietGroupLabel,
              patternConsumptionLabel, regionLabel, countryLabel, riskLabel, seasonLabel),
          Arrays.asList(populationNameTextField, targetPopulationTextField, populationSpanTextField,
              populationAgeTextField, populationGenderTextField, bmiTextField,
              specialDietGroupTextField, patternConsumptionTextField, regionComboBox,
              countryComboBox, riskTextField, seasonTextField));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      northPanel.add(new JScrollPane(populationDescriptionTextArea));
      add(northPanel, BorderLayout.NORTH);

      advancedComponents = Arrays.asList(targetPopulationTextField, populationSpanTextField,
          populationDescriptionTextArea, populationAgeTextField, populationGenderTextField,
          bmiTextField, specialDietGroupTextField, patternConsumptionTextField, regionComboBox,
          countryComboBox, riskTextField, seasonTextField);

      // If simple mode hide advanced components
      if (!isAdvanced) {
        advancedComponents.forEach(it -> it.setEnabled(false));
      }
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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>(1);
      if (!populationNameTextField.isValueValid()) {
        errors
            .add("Missing " + bundle.getString("GM.EditPopulationGroupPanel.populationNameLabel"));
      }
      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return advancedComponents;
    }
  }

  private class EditProductPanel extends EditPanel<Product> {

    private static final long serialVersionUID = -7400646603919832139L;

    private final AutoSuggestField envNameField =
        GUIFactory.createAutoSuggestField(vocabs.get("Product-matrix name"));
    private final StringTextArea envDescriptionTextArea = new StringTextArea(true, 5, 30);
    private final AutoSuggestField envUnitField =
        GUIFactory.createAutoSuggestField(vocabs.get("Product-matrix unit"));
    private final JComboBox<String> productionMethodComboBox =
        GUIFactory.createComboBox(vocabs.get("Method of production"));
    private final JComboBox<String> packagingComboBox =
        GUIFactory.createComboBox(vocabs.get("Packaging"));
    private final JComboBox<String> productTreatmentComboBox =
        GUIFactory.createComboBox(vocabs.get("Product treatment"));
    private final AutoSuggestField originCountryField =
        GUIFactory.createAutoSuggestField(vocabs.get("Country of origin"));
    private final AutoSuggestField originAreaField =
        GUIFactory.createAutoSuggestField(vocabs.get("Area of origin"));
    private final AutoSuggestField fisheriesAreaField =
        GUIFactory.createAutoSuggestField(vocabs.get("Fisheries area"));
    private final FixedDateChooser productionDateChooser = new FixedDateChooser();
    private final FixedDateChooser expirationDateChooser = new FixedDateChooser();

    private final List<JComponent> advancedComponents;

    public EditProductPanel(boolean isAdvanced) {

      super(new BorderLayout());

      final JLabel envNameLabel = GUIFactory.createLabel("GM.EditProductPanel.envNameLabel",
          "GM.EditProductPanel.envNameTooltip", true);
      final JLabel envUnitLabel = GUIFactory.createLabel("GM.EditProductPanel.envUnitLabel",
          "GM.EditProductPanel.envUnitTooltip", true);
      final JLabel productionMethodLabel =
          GUIFactory.createLabel("GM.EditProductPanel.productionMethodLabel",
              "GM.EditProductPanel.productionMethodTooltip");
      final JLabel packagingLabel = GUIFactory.createLabel("GM.EditProductPanel.packagingLabel",
          "GM.EditProductPanel.packagingTooltip");
      final JLabel productTreatmentLabel =
          GUIFactory.createLabel("GM.EditProductPanel.productTreatmentLabel",
              "GM.EditProductPanel.productTreatmentTooltip");
      final JLabel originCountryLabel = GUIFactory.createLabel(
          "GM.EditProductPanel.originCountryLabel", "GM.EditProductPanel.originCountryTooltip");
      final JLabel originAreaLabel = GUIFactory.createLabel("GM.EditProductPanel.originAreaLabel",
          "GM.EditProductPanel.originAreaTooltip");
      final JLabel fisheriesAreaLabel = GUIFactory.createLabel(
          "GM.EditProductPanel.fisheriesAreaLabel", "GM.EditProductPanel.fisheriesAreaTooltip");
      final JLabel productionDateLabel = GUIFactory.createLabel(
          "GM.EditProductPanel.productionDateLabel", "GM.EditProductPanel.productionDateTooltip");
      final JLabel expirationDateLabel = GUIFactory.createLabel(
          "GM.EditProductPanel.expirationDateLabel", "GM.EditProductPanel.expirationDateTooltip");

      // Build UI
      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      envDescriptionTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.EditProductPanel.envDescriptionLabel")));
      envDescriptionTextArea
          .setToolTipText(bundle.getString("GM.EditProductPanel.envDescriptionTooltip"));

      // formPanel
      JPanel productionDatePanel = UI.createWestPanel(productionDateChooser);
      JPanel expirationDatePanel = UI.createWestPanel(expirationDateChooser);
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(envNameLabel, envUnitLabel, productionMethodLabel, packagingLabel,
              productTreatmentLabel, originCountryLabel, originAreaLabel, fisheriesAreaLabel,
              productionDateLabel, expirationDateLabel),
          Arrays.asList(envNameField, envUnitField, productionMethodComboBox, packagingComboBox,
              productTreatmentComboBox, originCountryField, originAreaField, fisheriesAreaField,
              productionDatePanel, expirationDatePanel));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      northPanel.add(new JScrollPane(envDescriptionTextArea));
      add(northPanel, BorderLayout.NORTH);

      advancedComponents = Arrays.asList(envDescriptionTextArea, productionMethodComboBox,
          packagingComboBox, productTreatmentComboBox, originCountryField, originAreaField,
          fisheriesAreaField, productionDateChooser, expirationDateChooser);

      // If simple mode hides the advanced components
      if (!isAdvanced) {
        advancedComponents.forEach(it -> it.setEnabled(false));
      }
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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(envNameField)) {
        errors.add("Missing " + bundle.getString("GM.EditProductPanel.envNameLabel"));
      }
      if (!hasValidValue(envUnitField)) {
        errors.add("Missing " + bundle.getString("GM.EditProductPanel.envUnitLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return advancedComponents;
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

    private final JCheckBox isReferenceDescriptionCheckBox;
    private final JComboBox<String> typeComboBox =
        GUIFactory.createComboBox(referenceTypeLabels.values());
    private final FixedDateChooser dateChooser = new FixedDateChooser();
    private final StringTextField pmidTextField = new StringTextField(true, 30);
    private final StringTextField doiTextField = new StringTextField(false, 30);
    private final StringTextField authorListTextField = new StringTextField(true, 30);
    private final StringTextField titleTextField = new StringTextField(false, 30);
    private final StringTextArea abstractTextArea = new StringTextArea(true, 5, 30);
    private final StringTextField journalTextField = new StringTextField(true, 30);
    // Spinner models starting with 0 and taking positive ints only
    private final SpinnerNumberModel volumeSpinnerModel = new SpinnerNumberModel(0, 0, null, 1);
    private final SpinnerNumberModel issueSpinnerModel = new SpinnerNumberModel(0, 0, null, 1);
    private final StringTextField pageTextField = new StringTextField(true, 30);
    private final StringTextField statusTextField = new StringTextField(true, 30);
    private final StringTextField websiteTextField = new StringTextField(true, 30);
    private final StringTextArea commentTextArea = new StringTextArea(true, 5, 30);

    private final List<JComponent> advancedComponents;

    EditReferencePanel(final boolean isAdvanced) {

      super(new BorderLayout());

      // Create fields
      isReferenceDescriptionCheckBox = new JCheckBox("Is reference description *");

      // Create labels
      final JLabel typeLabel = GUIFactory.createLabel("GM.EditReferencePanel.typeLabel");
      final JLabel dateLabel = GUIFactory.createLabel("GM.EditReferencePanel.dateLabel");
      final JLabel pmidLabel = GUIFactory.createLabel("GM.EditReferencePanel.pmidLabel");
      final JLabel doiLabel = GUIFactory.createLabel("GM.EditReferencePanel.doiLabel", true);
      final JLabel authorListLabel =
          GUIFactory.createLabel("GM.EditReferencePanel.authorListLabel");
      final JLabel titleLabel = GUIFactory.createLabel("GM.EditReferencePanel.titleLabel", true);
      final JLabel journalLabel = GUIFactory.createLabel("GM.EditReferencePanel.journalLabel");
      final JLabel volumeLabel = GUIFactory.createLabel("GM.EditReferencePanel.volumeLabel");
      final JLabel issueLabel = GUIFactory.createLabel("GM.EditReferencePanel.issueLabel");
      final JLabel pageLabel = GUIFactory.createLabel("GM.EditReferencePanel.pageLabel");
      final JLabel statusLabel = GUIFactory.createLabel("GM.EditReferencePanel.statusLabel");
      final JLabel websiteLabel = GUIFactory.createLabel("GM.EditReferencePanel.websiteLabel");

      // Build UI
      final JSpinner volumeSpinner = GUIFactory.createSpinner(volumeSpinnerModel);
      final JSpinner issueSpinner = GUIFactory.createSpinner(issueSpinnerModel);

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      abstractTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.EditReferencePanel.abstractLabel")));
      commentTextArea.setBorder(
          BorderFactory.createTitledBorder(bundle.getString("GM.EditReferencePanel.commentLabel")));

      // isReferenceDescription panel
      final JPanel isReferenceDescriptionPanel = new JPanel(new BorderLayout());
      isReferenceDescriptionPanel.add(isReferenceDescriptionCheckBox, BorderLayout.WEST);

      // formPanel
      JPanel datePanel = UI.createWestPanel(dateChooser);
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(typeLabel, dateLabel, pmidLabel, doiLabel, authorListLabel, titleLabel,
              journalLabel, volumeLabel, issueLabel, pageLabel, statusLabel, websiteLabel),
          Arrays.asList(typeComboBox, datePanel, pmidTextField, doiTextField, authorListTextField,
              titleTextField, journalTextField, volumeSpinner, issueSpinner, pageTextField,
              statusTextField, websiteTextField));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(isReferenceDescriptionPanel);
      northPanel.add(formPanel);
      northPanel.add(new JScrollPane(abstractTextArea));
      northPanel.add(new JScrollPane(commentTextArea));
      add(northPanel, BorderLayout.NORTH);

      // If simple mode hide advanced components
      advancedComponents = Arrays.asList(typeComboBox, dateChooser, pmidTextField,
          authorListTextField, abstractTextArea, journalTextField, volumeSpinner, issueSpinner,
          pageTextField, statusTextField, websiteTextField, commentTextArea);
      if (!isAdvanced) {
        getAdvancedComponents().forEach(it -> it.setEnabled(false));
      }
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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>(2);
      if (!doiTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditReferencePanel.doiLabel"));
      }
      if (!titleTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditReferencePanel.titleLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return advancedComponents;
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

    private final StringTextField sampleNameTextField = new StringTextField(false, 30);
    private final SpinnerNumberModel moisturePercentageSpinnerModel =
        GUIFactory.createSpinnerPercentageModel();
    private final SpinnerNumberModel fatPercentageSpinnerModel =
        GUIFactory.createSpinnerPercentageModel();
    private final StringTextField sampleProtocolTextField = new StringTextField(false, 30);
    private final AutoSuggestField samplingStrategyField =
        GUIFactory.createAutoSuggestField(vocabs.get("Sampling strategy"));
    private final AutoSuggestField samplingTypeField =
        GUIFactory.createAutoSuggestField(vocabs.get("Type of sampling program"));
    private final AutoSuggestField samplingMethodField =
        GUIFactory.createAutoSuggestField(vocabs.get("Sampling method"));
    private final StringTextField samplingPlanTextField = new StringTextField(false, 30);
    private final StringTextField samplingWeightTextField = new StringTextField(false, 30);
    private final StringTextField samplingSizeTextField = new StringTextField(false, 30);
    private final AutoSuggestField lotSizeUnitField =
        GUIFactory.createAutoSuggestField(vocabs.get("Lot size unit"));
    private final AutoSuggestField samplingPointField =
        GUIFactory.createAutoSuggestField(vocabs.get("Sampling point"));

    private final List<JComponent> advancedComponents;

    public EditStudySamplePanel(final boolean isAdvanced) {

      super(new BorderLayout());

      // Create labels and fields
      final JLabel sampleNameLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.sampleNameLabel",
              "GM.EditStudySamplePanel.sampleNameTooltip", true);
      final JLabel moisturePercentageLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.moisturePercentageLabel",
              "GM.EditStudySamplePanel.moisturePercentageTooltip");
      final JLabel fatPercentageLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.fatPercentageLabel",
              "GM.EditStudySamplePanel.fatPercentageTooltip");
      final JLabel sampleProtocolLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.sampleProtocolLabel",
              "GM.EditStudySamplePanel.sampleProtocolTooltip", true);
      final JLabel samplingStrategyLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.samplingStrategyLabel",
              "GM.EditStudySamplePanel.samplingStrategyTooltip");
      final JLabel samplingTypeLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.samplingTypeLabel",
              "GM.EditStudySamplePanel.samplingTypeTooltip");
      final JLabel samplingMethodLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.samplingMethodLabel",
              "GM.EditStudySamplePanel.samplingMethodTooltip");
      final JLabel samplingPlanLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.samplingPlanLabel",
              "GM.EditStudySamplePanel.samplingPlanTooltip", true);
      final JLabel samplingWeightLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.samplingWeightLabel",
              "GM.EditStudySamplePanel.samplingWeightTooltip", true);
      final JLabel samplingSizeLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.samplingSizeLabel",
              "GM.EditStudySamplePanel.samplingSizeTooltip", true);
      final JLabel lotSizeUnitLabel = GUIFactory.createLabel(
          "GM.EditStudySamplePanel.lotSizeUnitLabel", "GM.EditStudySamplePanel.lotSizeUnitTooltip");
      final JLabel samplingPointLabel =
          GUIFactory.createLabel("GM.EditStudySamplePanel.samplingPointLabel",
              "GM.EditStudySamplePanel.samplingPointTooltip");

      // Build UI
      final JSpinner moisturePercentageSpinner =
          GUIFactory.createSpinner(moisturePercentageSpinnerModel);
      final JSpinner fatPercentageSpinner = GUIFactory.createSpinner(fatPercentageSpinnerModel);

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(sampleNameLabel, moisturePercentageLabel, fatPercentageLabel,
              sampleProtocolLabel, samplingStrategyLabel, samplingTypeLabel, samplingMethodLabel,
              samplingPlanLabel, samplingWeightLabel, samplingSizeLabel, lotSizeUnitLabel,
              samplingPointLabel),
          Arrays.asList(sampleNameTextField, moisturePercentageSpinner, fatPercentageSpinner,
              sampleProtocolTextField, samplingStrategyField, samplingTypeField,
              samplingMethodField, samplingPlanTextField, samplingWeightTextField,
              samplingSizeTextField, lotSizeUnitField, samplingPointField));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      add(northPanel, BorderLayout.NORTH);

      // If simple mode hide advanced components
      advancedComponents = Arrays.asList(moisturePercentageSpinner, fatPercentageSpinner,
          samplingStrategyField, samplingTypeField, samplingMethodField, samplingSizeTextField,
          lotSizeUnitField, samplingPointField);
      if (!isAdvanced) {
        getAdvancedComponents().forEach(it -> it.setEnabled(false));
      }
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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>(5);
      if (!sampleNameTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.sampleNameLabel"));
      }
      if (!sampleProtocolTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.sampleProtocolLabel"));
      }
      if (!samplingPlanTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.samplingPlanLabel"));
      }
      if (!samplingWeightTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.samplingWeightLabel"));
      }
      if (!samplingSizeTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.samplingSizeLabel"));
      }

      return errors;
    }

    @Override
    List<JComponent> getAdvancedComponents() {
      return advancedComponents;
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

    private final StringTextField studyNameTextField = new StringTextField(true, 30);
    private final StringTextField sourceTextField = new StringTextField(true, 30);
    private final StringTextField identifierTextField = new StringTextField(true, 30);
    private final CreatorPanel creatorPanel = new CreatorPanel();
    private final FixedDateChooser creationDateChooser = new FixedDateChooser();
    private final AutoSuggestField rightsField =
        GUIFactory.createAutoSuggestField(vocabs.get("Rights"));
    private final JCheckBox availabilityCheckBox = new JCheckBox();
    private final StringTextField urlTextField = new StringTextField(true, 30);
    private final AutoSuggestField formatField =
        GUIFactory.createAutoSuggestField(vocabs.get("Format"));
    private final ReferencePanel referencePanel;
    private final AutoSuggestField languageField =
        GUIFactory.createAutoSuggestField(vocabs.get("Language"));
    private final AutoSuggestField softwareField =
        GUIFactory.createAutoSuggestField(vocabs.get("Software"));
    private final AutoSuggestField languageWrittenInField =
        GUIFactory.createAutoSuggestField(vocabs.get("Language written in"));
    private final AutoSuggestField statusField =
        GUIFactory.createAutoSuggestField(vocabs.get("Status"));
    private final StringTextArea objectiveTextArea = new StringTextArea(true, 5, 30);
    private final StringTextArea descriptionTextArea = new StringTextArea(true, 5, 30);

    public GeneralInformationPanel() {

      super(new BorderLayout());

      // Create fields
      advancedCheckBox = new JCheckBox("Advanced");

      availabilityCheckBox.setText(FskPlugin.getDefault().MESSAGES_BUNDLE
          .getString("GM.GeneralInformationPanel.availabilityLabel"));
      availabilityCheckBox.setToolTipText(FskPlugin.getDefault().MESSAGES_BUNDLE
          .getString("GM.GeneralInformationPanel.availabilityTooltip"));

      referencePanel = new ReferencePanel(advancedCheckBox.isSelected());

      // Create labels
      final JLabel studyNameLabel =
          GUIFactory.createLabel("GM.GeneralInformationPanel.studyNameLabel",
              "GM.GeneralInformationPanel.studyNameTooltip");
      final JLabel sourceLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.sourceLabel",
          "GM.GeneralInformationPanel.sourceTooltip");
      final JLabel identifierLabel =
          GUIFactory.createLabel("GM.GeneralInformationPanel.identifierLabel",
              "GM.GeneralInformationPanel.identifierTooltip");
      final JLabel creationDateLabel =
          GUIFactory.createLabel("GM.GeneralInformationPanel.creationDateLabel",
              "GM.GeneralInformationPanel.creationDateTooltip");
      final JLabel rightsLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.rightsLabel",
          "GM.GeneralInformationPanel.rightsTooltip");
      final JLabel urlLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.urlLabel",
          "GM.GeneralInformationPanel.urlTooltip");
      final JLabel formatLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.formatLabel",
          "GM.GeneralInformationPanel.formatTooltip");
      final JLabel languageLabel = GUIFactory.createLabel(
          "GM.GeneralInformationPanel.languageLabel", "GM.GeneralInformationPanel.languageTooltip");
      final JLabel softwareLabel = GUIFactory.createLabel(
          "GM.GeneralInformationPanel.softwareLabel", "GM.GeneralInformationPanel.softwareTooltip");
      final JLabel languageWrittenInLabel =
          GUIFactory.createLabel("GM.GeneralInformationPanel.languageWrittenInLabel",
              "GM.GeneralInformationPanel.languageWrittenInTooltip");
      final JLabel statusLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.statusLabel",
          "GM.GeneralInformationPanel.statusTooltip");

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      objectiveTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.GeneralInformationPanel.objectiveLabel")));
      objectiveTextArea
          .setToolTipText(bundle.getString("GM.GeneralInformationPanel.objectiveTooltip"));

      descriptionTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.GeneralInformationPanel.descriptionLabel")));
      descriptionTextArea
          .setToolTipText(bundle.getString("GM.GeneralInformationPanel.descriptionTooltip"));

      // Hide initially advanced components
      final List<JComponent> advancedComponents =
          Arrays.asList(sourceTextField, formatField, languageField, softwareField,
              languageWrittenInField, statusField, objectiveTextArea, descriptionTextArea);
      advancedComponents.forEach(it -> it.setEnabled(false));

      // formPanel
      JPanel creationDatePanel = UI.createWestPanel(creationDateChooser);
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(studyNameLabel, identifierLabel, creationDateLabel, rightsLabel,
              availabilityCheckBox, urlLabel, sourceLabel, formatLabel, languageLabel,
              softwareLabel, languageWrittenInLabel, statusLabel),
          Arrays.asList(studyNameTextField, identifierTextField, creationDatePanel, rightsField,
              new JLabel(), urlTextField, sourceTextField, formatField, languageField,
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
      northPanel.add(new JScrollPane(objectiveTextArea));
      northPanel.add(new JScrollPane(descriptionTextArea));
      northPanel.add(creatorPanel);
      northPanel.add(referencePanel);
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

    private final StringTextField givenNameTextField = new StringTextField(false, 30);
    private final StringTextField familyNameTextField = new StringTextField(false, 30);
    private final StringTextField contactTextField = new StringTextField(false, 30);

    public EditCreatorPanel() {
      super(new BorderLayout());

      // Create labels
      final JLabel givenNameLabel = GUIFactory.createLabel("GM.EditCreatorPanel.givenNameLabel");
      final JLabel familyNameLabel = GUIFactory.createLabel("GM.EditCreatorPanel.familyNameLabel");
      final JLabel contactLabel = GUIFactory.createLabel("GM.EditCreatorPanel.contactLabel");

      final JPanel formPanel =
          UI.createOptionsPanel(Arrays.asList(givenNameLabel, familyNameLabel, contactLabel),
              Arrays.asList(givenNameTextField, familyNameTextField, contactTextField));

      final JPanel northPanel = UI.createNorthPanel(formPanel);

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

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final List<String> errors = new ArrayList<>(3);
      if (!givenNameTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditCreatorPanel.givenNameLabel"));
      }
      if (!familyNameTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditCreatorPanel.familyNameLabel"));
      }
      if (!contactTextField.isValueValid()) {
        errors.add("Missing " + bundle.getString("GM.EditCreatorPanel.contactLabel"));
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

    final StringTextArea commentTextArea = new StringTextArea(true, 5, 30);

    final FixedDateChooser dateChooser = new FixedDateChooser();
    final AutoSuggestField regionField = GUIFactory.createAutoSuggestField(vocabs.get("Region"));
    final AutoSuggestField countryField = GUIFactory.createAutoSuggestField(vocabs.get("Country"));

    private final EditProductPanel editProductPanel = new EditProductPanel(false);
    private final EditHazardPanel editHazardPanel = new EditHazardPanel(false);
    private final EditPopulationGroupPanel editPopulationGroupPanel =
        new EditPopulationGroupPanel(false);

    ScopePanel() {

      super(new BorderLayout());

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      commentTextArea.setBorder(
          BorderFactory.createTitledBorder(bundle.getString("GM.ScopePanel.commentLabel")));
      commentTextArea.setToolTipText(bundle.getString("GM.ScopePanel.commentTooltip"));

      // Build UI
      productButton.setToolTipText("Click me to add a product");
      productButton.addActionListener(event -> {
        final ValidatableDialog dlg = new ValidatableDialog(editProductPanel, "Create a product");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Product product = editProductPanel.get();
          productButton
              .setText(String.format("%s_%s", product.environmentName, product.environmentUnit));
        }
      });

      hazardButton.setToolTipText("Click me to add a hazard");
      hazardButton.addActionListener(event -> {
        final ValidatableDialog dlg = new ValidatableDialog(editHazardPanel, "Create a hazard");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Hazard hazard = editHazardPanel.get();
          hazardButton.setText(String.format("%s_%s", hazard.hazardName, hazard.hazardUnit));
        }
      });

      populationButton.setToolTipText("Click me to add a Population group");
      populationButton.addActionListener(event -> {
        final ValidatableDialog dlg =
            new ValidatableDialog(editPopulationGroupPanel, "Create a Population Group");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final PopulationGroup populationGroup = editPopulationGroupPanel.get();
          populationButton.setText(populationGroup.populationName);
        }
      });

      // Create labels
      final JLabel productLabel = GUIFactory.createLabel("GM.ScopePanel.productLabel");
      final JLabel hazardLabel = GUIFactory.createLabel("GM.ScopePanel.hazardLabel");
      final JLabel populationLabel = GUIFactory.createLabel("GM.ScopePanel.populationGroupLabel");
      final JLabel temporalInformationLabel = GUIFactory.createLabel(
          "GM.ScopePanel.temporalInformationLabel", "GM.ScopePanel.temporalInformationTooltip");
      final JLabel regionLabel =
          GUIFactory.createLabel("GM.ScopePanel.regionLabel", "GM.ScopePanel.regionTooltip");
      final JLabel countryLabel =
          GUIFactory.createLabel("GM.ScopePanel.countryLabel", "GM.ScopePanel.countryTooltip");

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(productLabel, hazardLabel, populationLabel, temporalInformationLabel,
              regionLabel, countryLabel),
          Arrays.asList(UI.createWestPanel(productButton), UI.createWestPanel(hazardButton),
              UI.createWestPanel(populationButton), dateChooser, regionField, countryField));

      // Advanced checkbox
      final JCheckBox advancedCheckBox = new JCheckBox("Advanced");
      advancedCheckBox.addItemListener(event -> {
        editProductPanel.toggleMode();
        editHazardPanel.toggleMode();
        editPopulationGroupPanel.toggleMode();
      });

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(formPanel);
      northPanel.add(new JScrollPane(commentTextArea));
      add(northPanel, BorderLayout.NORTH);
    }

    void init(final Scope scope) {
      if (scope != null) {
        editProductPanel.init(scope.product);
        editHazardPanel.init(scope.hazard);
        editPopulationGroupPanel.init(scope.populationGroup);
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
      scope.product = editProductPanel.get();
      scope.hazard = editHazardPanel.get();
      scope.populationGroup = editPopulationGroupPanel.get();

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

    private final EditStudySamplePanel editStudySamplePanel = new EditStudySamplePanel(false);
    private final EditDietaryAssessmentMethodPanel editDietaryAssessmentMethodPanel =
        new EditDietaryAssessmentMethodPanel(false);
    private final EditAssayPanel editAssayPanel = new EditAssayPanel(false);

    DataBackgroundPanel() {

      super(new BorderLayout());

      final StudyPanel studyPanel = new StudyPanel();
      studyPanel.setBorder(BorderFactory.createTitledBorder("Study"));

      final JButton studySampleButton = new JButton();
      studySampleButton.setToolTipText("Click me to add Study Sample");
      studySampleButton.addActionListener(event -> {

        final ValidatableDialog dlg =
            new ValidatableDialog(editStudySamplePanel, "Create Study sample");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final StudySample studySample = editStudySamplePanel.get();
          // Update button's text
          studySampleButton.setText(studySample.sample);
        }
      });

      final JButton dietaryAssessmentMethodButton = new JButton();
      dietaryAssessmentMethodButton.setToolTipText("Click me to add Dietary assessment method");
      dietaryAssessmentMethodButton.addActionListener(event -> {
        final ValidatableDialog dlg = new ValidatableDialog(editDietaryAssessmentMethodPanel,
            "Create dietary assessment method");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final DietaryAssessmentMethod method = editDietaryAssessmentMethodPanel.get();
          // Update button's text
          dietaryAssessmentMethodButton.setText(method.collectionTool);
        }
      });

      final JButton assayButton = new JButton();
      assayButton.setToolTipText("Click me to add Assay");
      assayButton.addActionListener(event -> {
        final ValidatableDialog dlg = new ValidatableDialog(editAssayPanel, "Create assay");
        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Assay assay = editAssayPanel.get();
          // Update button's text
          assayButton.setText(assay.name);
        }
      });

      final JLabel studySampleLabel =
          GUIFactory.createLabel("GM.DataBackgroundPanel.studySampleLabel");
      final JLabel dietaryAssessmentMethodLabel =
          GUIFactory.createLabel("GM.DataBackgroundPanel.dietaryAssessmentMethodLabel");
      final JLabel laboratoryAccreditationLabel =
          GUIFactory.createLabel("GM.DataBackgroundPanel.laboratoryAccreditationLabel");
      final JLabel assayLabel = GUIFactory.createLabel("GM.DataBackgroundPanel.assayLabel");

      // Advanced `checkbox`
      advancedCheckBox.addItemListener(event -> {
        studyPanel.advancedComponents.forEach(it -> it.setEnabled(advancedCheckBox.isSelected()));
        editStudySamplePanel.toggleMode();
        editDietaryAssessmentMethodPanel.toggleMode();
        editAssayPanel.toggleMode();
      });

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(studySampleLabel, dietaryAssessmentMethodLabel,
              laboratoryAccreditationLabel, assayLabel),
          Arrays.asList(UI.createWestPanel(studySampleButton),
              UI.createWestPanel(dietaryAssessmentMethodButton), laboratoryAccreditationField,
              UI.createWestPanel(assayButton)));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(studyPanel);
      northPanel.add(formPanel);
      add(northPanel, BorderLayout.NORTH);
    }

    void init(final DataBackground dataBackground) {
      if (dataBackground != null) {
        editStudySamplePanel.init(dataBackground.studySample);
        editDietaryAssessmentMethodPanel.init(dataBackground.dietaryAssessmentMethod);
        editAssayPanel.init(dataBackground.assay);
      }
    }

    DataBackground get() {
      final DataBackground dataBackground = new DataBackground();
      dataBackground.studySample = editStudySamplePanel.get();
      dataBackground.dietaryAssessmentMethod = editDietaryAssessmentMethodPanel.get();
      dataBackground.assay = editAssayPanel.get();

      return dataBackground;
    }
  }

  private class StudyPanel extends JPanel {

    private static final long serialVersionUID = -6572236073945735826L;

    private final StringTextField studyIdentifierTextField = new StringTextField(true, 30);
    private final StringTextField studyTitleTextField = new StringTextField(true, 30);
    private final StringTextArea studyDescriptionTextArea = new StringTextArea(true, 5, 30);
    private final AutoSuggestField studyDesignTypeField;
    private final AutoSuggestField studyAssayMeasurementsTypeField;
    private final AutoSuggestField studyAssayTechnologyTypeField;
    private final StringTextField studyAssayTechnologyPlatformTextField =
        new StringTextField(true, 30);
    private final AutoSuggestField accreditationProcedureField;
    private final StringTextField studyProtocolNameTextField = new StringTextField(true, 30);
    private final AutoSuggestField studyProtocolTypeField;
    private final StringTextField studyProtocolDescriptionTextField = new StringTextField(true, 30);
    private final StringTextField studyProtocolURITextField = new StringTextField(true, 30);
    private final StringTextField studyProtocolVersionTextField = new StringTextField(true, 30);
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

      // Create labels
      final JLabel studyIdentifierLabel = GUIFactory.createLabel(
          "GM.StudyPanel.studyIdentifierLabel", "GM.StudyPanel.studyIdentifierTooltip", true);
      final JLabel studyTitleLabel = GUIFactory.createLabel("GM.StudyPanel.studyTitleLabel",
          "GM.StudyPanel.studyTitleTooltip", true);
      final JLabel studyDesignTypeLabel = GUIFactory.createLabel(
          "GM.StudyPanel.studyDesignTypeLabel", "GM.StudyPanel.studyDesignTypeTooltip");
      final JLabel studyAssayMeasurementsTypeLabel =
          GUIFactory.createLabel("GM.StudyPanel.studyAssayMeasurementsTypeLabel",
              "GM.StudyPanel.studyAssayMeasurementsTypeTooltip");
      final JLabel studyAssayTechnologyTypeLabel =
          GUIFactory.createLabel("GM.StudyPanel.studyAssayTechnologyTypeLabel",
              "GM.StudyPanel.studyAssayTechnologyTypeTooltip");
      final JLabel studyAssayTechnologyPlatformLabel =
          GUIFactory.createLabel("GM.StudyPanel.studyAssayTechnologyPlatformLabel",
              "GM.StudyPanel.studyAssayTechnologyPlatformTooltip");
      final JLabel accreditationProcedureLabel =
          GUIFactory.createLabel("GM.StudyPanel.accreditationProcedureLabel",
              "GM.StudyPanel.accreditationProcedureTooltip");
      final JLabel studyProtocolNameLabel = GUIFactory
          .createLabel("GM.StudyPanel.protocolNameLabel", "GM.StudyPanel.protocolNameTooltip");
      final JLabel studyProtocolTypeLabel = GUIFactory
          .createLabel("GM.StudyPanel.protocolTypeLabel", "GM.StudyPanel.protocolTypeTooltip");
      final JLabel studyProtocolDescriptionLabel = GUIFactory.createLabel(
          "GM.StudyPanel.protocolDescriptionLabel", "GM.StudyPanel.protocolDescriptionTooltip");
      final JLabel studyProtocolURILabel = GUIFactory.createLabel("GM.StudyPanel.protocolURILabel",
          "GM.StudyPanel.protocolURITooltip");
      final JLabel studyProtocolVersionLabel = GUIFactory.createLabel(
          "GM.StudyPanel.protocolDescriptionLabel", "GM.StudyPanel.protocolDescriptionTooltip");
      final JLabel studyProtocolParametersLabel = GUIFactory
          .createLabel("GM.StudyPanel.parametersLabel", "GM.StudyPanel.parametersTooltip");
      final JLabel studyProtocolComponentsTypeLabel = GUIFactory
          .createLabel("GM.StudyPanel.componentsTypeLabel", "GM.StudyPanel.componentsTypeTooltip");

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
      studyDescriptionTextArea.setBorder(BorderFactory
          .createTitledBorder(bundle.getString("GM.StudyPanel.studyDescriptionLabel")));
      studyDescriptionTextArea
          .setToolTipText(bundle.getString("GM.StudyPanel.studyDescriptionTooltip"));

      // formPanel
      final JPanel formPanel = UI.createOptionsPanel(
          Arrays.asList(studyIdentifierLabel, studyTitleLabel, studyDesignTypeLabel,
              studyAssayTechnologyTypeLabel, studyAssayMeasurementsTypeLabel,
              studyAssayTechnologyPlatformLabel, accreditationProcedureLabel,
              studyProtocolNameLabel, studyProtocolTypeLabel, studyProtocolDescriptionLabel,
              studyProtocolURILabel, studyProtocolVersionLabel, studyProtocolParametersLabel,
              studyProtocolComponentsTypeLabel),
          Arrays.asList(studyIdentifierTextField, studyTitleTextField, studyDesignTypeField,
              studyAssayTechnologyTypeField, studyAssayMeasurementsTypeField,
              studyAssayTechnologyPlatformTextField, accreditationProcedureField,
              studyProtocolNameTextField, studyProtocolTypeField, studyProtocolDescriptionTextField,
              studyProtocolURITextField, studyProtocolVersionTextField,
              studyProtocolParametersField, studyProtocolComponentsTypeField));

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      northPanel.add(new JScrollPane(studyDescriptionTextArea));
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

    private final EditModelEquationPanel editPanel = new EditModelEquationPanel(false);

    ModelEquationsPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      setBorder(BorderFactory.createTitledBorder("Model equation"));

      final JTable myTable = UIUtils.createTable(tableModel);

      final JButton addButton = UIUtils.createAddButton();
      addButton.addActionListener(event -> {
        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create equation");
        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          tableModel.add(editPanel.get());
        }
      });

      final JButton editButton = UIUtils.createEditButton();
      editButton.addActionListener(event -> {
        final int rowToEdit = myTable.getSelectedRow();
        if (rowToEdit != -1) {

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
      editPanel.toggleMode();
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

    private final StringTextField algorithmField = new StringTextField(false, 30);
    private final StringTextField modelField = new StringTextField(false, 30);
    private final StringTextField scriptField = new StringTextField(true, 30);
    private final StringTextArea descriptionField = new StringTextArea(true, 5, 30);

    public SimulationPanel() {

      super(new BorderLayout());

      final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

      final JLabel algorithmLabel = new JLabel(bundle.getString("Simulation.Algorithm"));
      final JLabel modelLabel = new JLabel(bundle.getString("Simulation.Model"));
      final JLabel scriptLabel = new JLabel(bundle.getString("Simulation.Script"));

      final JPanel formPanel =
          UI.createOptionsPanel(Arrays.asList(algorithmLabel, modelLabel, scriptLabel),
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

      // descriptionField
      descriptionField
          .setBorder(BorderFactory.createTitledBorder(bundle.getString("Simulation.Description")));
      northPanel.add(new JScrollPane(descriptionField));

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
