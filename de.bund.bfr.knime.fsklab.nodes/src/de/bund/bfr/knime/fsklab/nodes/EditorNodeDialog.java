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
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import de.bund.bfr.knime.fsklab.nodes.ui.FSpinner;
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
import de.bund.bfr.knime.fsklab.rakip.Laboratory;
import de.bund.bfr.knime.fsklab.rakip.ModelEquation;
import de.bund.bfr.knime.fsklab.rakip.ModelMath;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.knime.fsklab.rakip.PopulationGroup;
import de.bund.bfr.knime.fsklab.rakip.Product;
import de.bund.bfr.knime.fsklab.rakip.Scope;
import de.bund.bfr.knime.fsklab.rakip.Study;
import de.bund.bfr.knime.fsklab.rakip.StudySample;
import de.bund.bfr.swing.AutoSuggestField;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;

public class EditorNodeDialog extends DataAwareNodeDialogPane {

  private final ScriptPanel modelScriptPanel = new ScriptPanel("Model script", "", true);
  private final ScriptPanel paramScriptPanel = new ScriptPanel("Parameters script", "", true);
  private final ScriptPanel vizScriptPanel = new ScriptPanel("Visualization script", "", true);
  private final GeneralInformationPanel generalInformationPanel = new GeneralInformationPanel();
  private final ScopePanel scopePanel = new ScopePanel();
  private final DataBackgroundPanel dataBackgroundPanel = new DataBackgroundPanel();
  private final ModelMathPanel modelMathPanel = new ModelMathPanel();

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

    // Save resources
    this.settings.resources.clear();
    this.settings.resources.addAll(Collections.list(listModel.elements()));

    this.settings.saveSettings(settings);
  }

  private static NodeLogger LOGGER = NodeLogger.getLogger("EditNodeDialog");

  private static final Map<String, Set<String>> vocabs = new HashMap<>();
  static {

    try (
        final InputStream stream = EditorNodeDialog.class
            .getResourceAsStream("/FSKLab_Config_Controlled Vocabularies.xlsx");
        final XSSFWorkbook workbook = new XSSFWorkbook(stream)) {

      final List<String> sheets = Arrays.asList("Source", "Rights", "Format", "Publication Type",
          "Publication Status", "Software", "Language", "Language written in", "Model Class",
          "Model Sub-Class", "Source", "Rights", "Format", "Publication Type", "Publication Status",
          "Software", "Language", "Language written in", "Model Class", "Model Sub-Class",
          "Basic process", "Status", "Product-matrix name", "Product-matrix unit",
          "Method of production", "Packaging", "Product treatment", "Country of origin",
          "Area of origin", "Fisheries area", "Hazard type", "Hazard name", "Hazard unit",
          "Hazard ind-sum", "Population name", "Laboratory country", "Region", "Country",
          "Study Assay Technology Type", "Accreditation procedure Ass.Tec", "Sampling strategy",
          "Type of sampling program", "Sampling method", "Lot size unit", "Sampling point",
          "Method tool to collect data", "Type of records", "Food descriptors",
          "Laboratory accreditation", "Parameter classification", "Parameter type",
          "Parameter unit", "Parameter unit category", "Parameter data type", "Parameter source",
          "Parameter subject", "Parameter distribution", "Model equation class-distr",
          "Fitting procedure", "Type of exposure");

      for (final String sheet : sheets) {
        final Set<String> vocabulary = readVocabFromSheet(workbook, sheet);
        vocabs.put(sheet, vocabulary);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

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
        LOGGER.warn("Controlled vocabulary " + sheetname + ": wrong value " + cell);
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
    private static AutoSuggestField createAutoSuggestField(final Set<String> possibleValues,
        boolean mandatory) {
      final AutoSuggestField field = new AutoSuggestField(10);
      field.setPossibleValues(possibleValues);
      field.setPreferredSize(new Dimension(100, field.getPreferredSize().height));

      Color borderColor = mandatory ? UIUtils.RED : UIUtils.BLUE;
      field.setBorder(BorderFactory.createLineBorder(borderColor));
      return field;
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

    /** Create borderless JScrollPane. */
    private static JScrollPane createScrollPane(JComponent comp) {
      JScrollPane pane = new JScrollPane(comp);
      pane.setBorder(BorderFactory.createEmptyBorder());

      return pane;
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
  }

  private class EditAssayPanel extends EditPanel<Assay> {

    private static final long serialVersionUID = -1195181696127795655L;

    private final FTextField nameField; // mandatory
    private final FTextArea descriptionField; // optional
    private final FTextField moisturePercentageField; // optional
    private final FTextField detectionLimitField; // optional
    private final FTextField quantificationLimitField; // optional
    private final FTextField leftCensoredDataField; // optional
    private final FTextField contaminationRangeField; // optional
    private final FTextField uncertaintyValueField; // optional

    EditAssayPanel(final boolean isAdvanced) {
      super(new BorderLayout());

      nameField = new FTextField(true);
      descriptionField = new FTextArea();
      moisturePercentageField = new FTextField();
      detectionLimitField = new FTextField();
      quantificationLimitField = new FTextField();
      leftCensoredDataField = new FTextField();
      contaminationRangeField = new FTextField();
      uncertaintyValueField = new FTextField();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "EditAssayPanel_";

      // name
      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "name"));
      fields.add(nameField);

      // moisture percentage
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "moisturePercentage"));
        fields.add(moisturePercentageField);
      }

      // detection limit
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "detectionLimit"));
        fields.add(detectionLimitField);
      }

      // quantification limit
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "quantificationLimit"));
        fields.add(quantificationLimitField);
      }

      // left censored data
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "leftCensoredData"));
        fields.add(leftCensoredDataField);
      }

      // contamination range
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "contaminationRange"));
        fields.add(contaminationRangeField);
      }

      // uncertainty value
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "uncertainty"));
        fields.add(uncertaintyValueField);
      }

      // northPanel
      final FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(UIUtils.createFormPanel(labels, fields));

      // description
      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "description");
        JScrollPane descriptionPane = GUIFactory.createScrollPane(descriptionField);

        FPanel textAreaPanel =
            UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(descriptionPane));
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final Assay assay) {
      if (assay != null) {
        nameField.setText(assay.name);
        descriptionField.setText(assay.description);
        moisturePercentageField.setText(assay.moisturePercentage);
        detectionLimitField.setText(assay.detectionLimit);
        quantificationLimitField.setText(assay.quantificationLimit);
        leftCensoredDataField.setText(assay.leftCensoredData);
        contaminationRangeField.setText(assay.contaminationRange);
        uncertaintyValueField.setText(assay.uncertaintyValue);
      }
    }

    @Override
    Assay get() {

      final Assay assay = new Assay();
      assay.name = nameField.getText();
      assay.description = descriptionField.getText();
      assay.moisturePercentage = moisturePercentageField.getText();
      assay.detectionLimit = detectionLimitField.getText();
      assay.quantificationLimit = quantificationLimitField.getText();
      assay.leftCensoredData = leftCensoredDataField.getText();
      assay.contaminationRange = contaminationRangeField.getText();
      assay.uncertaintyValue = uncertaintyValueField.getText();

      return assay;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(1);
      if (nameField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString("EditAssayPanel_nameLabel"));
      }

      return errors;
    }
  }

  private class EditLaboratoryPanel extends EditPanel<Laboratory> {

    private static final long serialVersionUID = -1483775354630337114L;

    private final AutoSuggestField accreditationField;
    private final FTextField nameField;
    private final FTextField countryField;

    EditLaboratoryPanel(final boolean isAdvanced) {
      super(new BorderLayout());

      accreditationField =
          GUIFactory.createAutoSuggestField(vocabs.get("Laboratory accreditation"), true);
      nameField = new FTextField();
      countryField = new FTextField();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "EditLaboratoryPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // accreditation
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "accreditation"));
      fields.add(accreditationField);

      // name
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "name"));
        fields.add(nameField);
      }

      // country
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "country"));
        fields.add(countryField);
      }

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(UIUtils.createFormPanel(labels, fields));

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final Laboratory laboratory) {
      if (laboratory != null) {
        accreditationField.setSelectedItem(laboratory.accreditation);
        nameField.setText(laboratory.name);
        countryField.setText(laboratory.country);
      }
    }

    @Override
    Laboratory get() {

      Laboratory laboratory = new Laboratory();
      laboratory.accreditation = (String) accreditationField.getSelectedItem();
      laboratory.name = nameField.getText();
      laboratory.country = countryField.getText();

      return laboratory;
    }

    @Override
    List<String> validatePanel() {

      List<String> errors = new ArrayList<>();
      if (!hasValidValue(accreditationField)) {
        errors.add("Missing " + bundle.getString("EditLaboratoryPanel_accreditationLabel"));
      }

      return errors;
    }
  }

  private class EditDietaryAssessmentMethodPanel extends EditPanel<DietaryAssessmentMethod> {

    private static final long serialVersionUID = -931984426171199928L;

    private final AutoSuggestField dataCollectionToolField; // mandatory
    private final FTextField nonConsecutiveOneDayField; // mandatory
    private final FTextField dietarySoftwareToolField; // optional
    private final FTextField foodItemNumberField; // optional
    private final FTextField recordTypeField; // optional
    private final JComboBox<String> foodDescriptionField; // optional

    EditDietaryAssessmentMethodPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      dataCollectionToolField =
          GUIFactory.createAutoSuggestField(vocabs.get("Method tool to collect data"), true);
      nonConsecutiveOneDayField = new FTextField(true);
      dietarySoftwareToolField = new FTextField();
      foodItemNumberField = new FTextField();
      recordTypeField = new FTextField();
      foodDescriptionField = GUIFactory.createComboBox(vocabs.get("Food descriptors"));

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      String prefix = "EditDietaryAssessmentMethodPanel_";

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
      FPanel northPanel = new FPanel();
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

      String prefix = "EditDietaryAssessmentMethodPanel_";

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(dataCollectionToolField)) {
        errors.add("Missing " + bundle.getString(prefix + "dataCollectionToolLabel"));
      }
      if (nonConsecutiveOneDayField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "nonConsecutiveOneDaysLabel"));
      }

      return errors;
    }
  }

  private class EditStudyPanel extends EditPanel<Study> {

    private static final long serialVersionUID = 6346824373141232020L;

    private final FTextField studyIdentifierField; // mandatory
    private final FTextField studyTitleField; // mandatory
    private final FTextArea studyDescriptionField; // optional
    private final FTextField studyDesignTypeField; // optional
    private final FTextField studyAssayMeasurementsTypeField; // optional
    private final AutoSuggestField studyAssayTechnologyTypeField; // optional
    private final FTextField studyAssayTechnologyPlatformField; // optional
    private final AutoSuggestField accreditationProcedureField; // optional
    private final FTextField studyProtocolNameField; // optional
    private final FTextField studyProtocolTypeField; // optional
    private final FTextField studyProtocolDescriptionField; // optional
    private final FTextField studyProtocolURIField; // optional
    private final FTextField studyProtocolVersionField; // optional
    private final FTextField studyProtocolParametersField; // optional
    private final FTextField studyProtocolComponentsTypeField; // optional

    EditStudyPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      studyIdentifierField = new FTextField(true);
      studyTitleField = new FTextField(true);
      studyDescriptionField = new FTextArea();
      studyDesignTypeField = new FTextField();
      studyAssayMeasurementsTypeField = new FTextField();
      studyAssayTechnologyTypeField =
          GUIFactory.createAutoSuggestField(vocabs.get("Study Assay Technology Type"), false);
      studyAssayTechnologyPlatformField = new FTextField();
      accreditationProcedureField =
          GUIFactory.createAutoSuggestField(vocabs.get("Accreditation procedure Ass.Tec"), false);
      studyProtocolNameField = new FTextField();
      studyProtocolTypeField = new FTextField();
      studyProtocolDescriptionField = new FTextField();
      studyProtocolURIField = new FTextField();
      studyProtocolVersionField = new FTextField();
      studyProtocolParametersField = new FTextField();
      studyProtocolComponentsTypeField = new FTextField();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();
      final String prefix = "StudyPanel_";

      // study identifier
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyIdentifier"));
      fields.add(studyIdentifierField);

      // study title
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyTitle"));
      fields.add(studyTitleField);

      // study design type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyDesignType"));
        fields.add(studyDesignTypeField);
      }

      // study assay measurements type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyAssayMeasurementsType"));
        fields.add(studyAssayMeasurementsTypeField);
      }

      // study assay technology type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyAssayTechnologyType"));
        fields.add(studyAssayTechnologyTypeField);
      }

      // study assay technology platform
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "studyAssayTechnologyPlatform"));
        fields.add(studyAssayTechnologyPlatformField);
      }

      // accreditation procedure for the assay technology
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "accreditationProcedure"));
        fields.add(accreditationProcedureField);
      }

      // study protocol name
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolName"));
        fields.add(studyProtocolNameField);
      }

      // study protocol type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolType"));
        fields.add(studyProtocolTypeField);
      }

      // study protocol description
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolDescription"));
        fields.add(studyProtocolDescriptionField);
      }

      // study protocol URI
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolURI"));
        fields.add(studyProtocolURIField);
      }

      // study protocol version
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "protocolVersion"));
        fields.add(studyProtocolVersionField);
      }

      // study protocol parameters name
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "parameters"));
        fields.add(studyProtocolParametersField);
      }

      // study protocol components
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "componentsType"));
        fields.add(studyProtocolComponentsTypeField);
      }

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(UIUtils.createFormPanel(labels, fields));

      // text areas
      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "studyDescription");
        JScrollPane studyDescriptionPane = GUIFactory.createScrollPane(studyDescriptionField);
        northPanel.add(
            UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(studyDescriptionPane)));
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(Study study) {
      if (study != null) {
        studyIdentifierField.setText(study.id);
        studyTitleField.setText(study.title);
        studyDesignTypeField.setText(study.designType);
        studyAssayTechnologyTypeField.setSelectedItem(study.technologyType);
        studyAssayMeasurementsTypeField.setText(study.measurementType);
        studyAssayTechnologyPlatformField.setText(study.technologyPlatform);
        accreditationProcedureField.setSelectedItem(study.accreditationProcedure);
        studyProtocolNameField.setText(study.protocolName);
        studyProtocolTypeField.setText(study.protocolType);
        studyProtocolDescriptionField.setText(study.description);
        if (study.protocolUri != null) {
          studyProtocolURIField.setText(study.protocolUri.toString());
        }
        studyProtocolVersionField.setText(study.protocolVersion);
        studyProtocolParametersField.setText(study.parametersName);
        // TODO components name
        studyProtocolComponentsTypeField.setText(study.componentsType);
        studyDescriptionField.setText(study.description);
      }
    }

    @Override
    Study get() {

      Study study = new Study();

      study.id = studyIdentifierField.getText();
      study.title = studyTitleField.getText();
      study.designType = studyDesignTypeField.getText();
      study.technologyType = (String) studyAssayTechnologyTypeField.getSelectedItem();
      study.measurementType = studyAssayMeasurementsTypeField.getText();
      study.technologyPlatform = studyAssayTechnologyPlatformField.getText();
      study.accreditationProcedure = (String) accreditationProcedureField.getSelectedItem();
      study.protocolName = studyProtocolNameField.getText();
      study.protocolType = studyProtocolTypeField.getText();
      study.protocolDescription = studyProtocolDescriptionField.getText();
      try {
        study.protocolUri = new URI(studyProtocolURIField.getText());
      } catch (URISyntaxException e) {
      }
      study.protocolVersion = studyProtocolVersionField.getText();
      study.parametersName = studyProtocolParametersField.getText();
      // TODO: Components name
      study.componentsType = studyProtocolComponentsTypeField.getText();
      study.description = studyDescriptionField.getText();

      return study;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "StudyPanel_";

      List<String> errors = new ArrayList<>();

      if (studyIdentifierField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "studyIdentifierLabel"));
      }
      if (studyTitleField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "studyTitleLabel"));
      }

      return errors;
    }
  }

  private class EditHazardPanel extends EditPanel<Hazard> {

    private static final long serialVersionUID = -1981279747311233487L;

    private final AutoSuggestField hazardTypeField; // mandatory
    private final AutoSuggestField hazardNameField; // mandatory
    private final FTextArea hazardDescriptionField; // optional
    private final AutoSuggestField hazardUnitField; // mandatory
    private final FTextField adverseEffectField; // optional
    private final FTextField originField; // optional
    private final FTextField bmdField; // optional
    private final FTextField maxResidueLimitField; // optional
    private final FTextField noObservedAdverseField; // optional
    private final FTextField acceptableOperatorField; // optional
    private final FTextField acuteReferenceDoseField; // optional
    private final FTextField acceptableDailyIntakeField; // optional
    private final AutoSuggestField indSumField; // optional

    EditHazardPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      hazardTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard type"), true);
      hazardNameField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard name"), true);
      hazardDescriptionField = new FTextArea();
      hazardUnitField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard unit"), true);
      adverseEffectField = new FTextField();
      originField = new FTextField();
      bmdField = new FTextField();
      maxResidueLimitField = new FTextField();
      noObservedAdverseField = new FTextField();
      acceptableOperatorField = new FTextField();
      acuteReferenceDoseField = new FTextField();
      acceptableDailyIntakeField = new FTextField();
      indSumField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard ind-sum"), false);

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "EditHazardPanel_";

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
        fields.add(adverseEffectField);

        // origin
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "origin"));
        fields.add(originField);

        // bmd
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "bmd"));
        fields.add(bmdField);

        // max residue limit
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "maxResidueLimit"));
        fields.add(maxResidueLimitField);

        // no observed adverse
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "noObservedAdverse"));
        fields.add(noObservedAdverseField);

        // acceptable opeartor
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "acceptableOperator"));
        fields.add(acceptableOperatorField);

        // acute reference dose
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "acuteReferenceDose"));
        fields.add(acuteReferenceDoseField);

        // ind sum
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "indSum"));
        fields.add(indSumField);

        // acceptable daily intake
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "acceptableDailyIntake"));
        fields.add(acceptableDailyIntakeField);
      }

      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "hazardDescription");
        JScrollPane hazardDescriptionPane = GUIFactory.createScrollPane(hazardDescriptionField);

        FPanel textAreaPanel =
            UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(hazardDescriptionPane));
        northPanel.add(textAreaPanel);
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(Hazard hazard) {
      if (hazard != null) {
        hazardTypeField.setSelectedItem(hazard.hazardType);
        hazardNameField.setSelectedItem(hazard.hazardName);
        hazardDescriptionField.setText(hazard.hazardDescription);
        hazardUnitField.setSelectedItem(hazard.hazardUnit);
        adverseEffectField.setText(hazard.adverseEffect);
        originField.setText(hazard.sourceOfContamination);
        bmdField.setText(hazard.bmd);
        maxResidueLimitField.setText(hazard.mrl);
        noObservedAdverseField.setText(hazard.noael);
        acceptableOperatorField.setText(hazard.aoel);
        acuteReferenceDoseField.setText(hazard.ard);
        indSumField.setSelectedItem(hazard.hazardIndSum);
        acceptableDailyIntakeField.setText(hazard.adi);
      }
    }

    @Override
    Hazard get() {

      final Hazard hazard = new Hazard();
      hazard.hazardType = (String) hazardTypeField.getSelectedItem();
      hazard.hazardName = (String) hazardNameField.getSelectedItem();
      hazard.hazardUnit = (String) hazardUnitField.getSelectedItem();

      hazard.hazardDescription = hazardDescriptionField.getText();
      hazard.adverseEffect = adverseEffectField.getText();
      hazard.sourceOfContamination = originField.getText();
      hazard.bmd = bmdField.getText();
      hazard.mrl = maxResidueLimitField.getText();
      hazard.noael = noObservedAdverseField.getText();
      hazard.aoel = acceptableOperatorField.getText();
      hazard.ard = acuteReferenceDoseField.getText();
      hazard.adi = acceptableDailyIntakeField.getText();
      hazard.hazardIndSum = (String) indSumField.getSelectedItem();

      return hazard;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "EditHazardPanel_";
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
  }

  private class EditModelEquationPanel extends EditPanel<ModelEquation> {

    private static final long serialVersionUID = 3586499490386620791L;

    private final FTextField equationNameField; // mandatory
    private final AutoSuggestField equationClassField; // optional
    private final ReferencePanel referencePanel; // optional
    private final FTextArea scriptField; // mandatory

    EditModelEquationPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      equationNameField = new FTextField(true);
      equationClassField =
          GUIFactory.createAutoSuggestField(vocabs.get("Model equation class-distr"), false);
      referencePanel = new ReferencePanel(isAdvanced);
      scriptField = new FTextArea(true);

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      // Create labels
      String prefix = "EditModelEquationPanel_";

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

      {
        List<FLabel> labels = new ArrayList<>();
        List<JComponent> fields = new ArrayList<>();

        // equation name
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "name"));
        fields.add(equationNameField);

        // equation class
        if (isAdvanced) {
          labels.add(GUIFactory.createLabelWithToolTip(prefix + "class"));
          fields.add(equationClassField);
        }

        northPanel.add(UIUtils.createFormPanel(labels, fields));
      }

      // reference panel
      if (isAdvanced) {
        northPanel.add(referencePanel);
      }

      // description
      {
        FLabel label = new FLabel(bundle.getString(prefix + "scriptLabel"));
        JScrollPane scriptPane = GUIFactory.createScrollPane(scriptField);
        northPanel.add(UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(scriptPane)));
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final ModelEquation modelEquation) {

      if (modelEquation != null) {
        equationNameField.setText(modelEquation.equationName);
        equationClassField.setSelectedItem(modelEquation.equationClass);
        referencePanel.init(modelEquation.equationReference);
        scriptField.setText(modelEquation.equation);
      }
    }

    @Override
    List<String> validatePanel() {

      String prefix = "EditModelEquationPanel_";

      final List<String> errors = new ArrayList<>();
      if (equationNameField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "nameLabel"));
      }
      if (scriptField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "scriptLabel"));
      }
      return errors;
    }

    @Override
    ModelEquation get() {
      final ModelEquation modelEquation = new ModelEquation();
      modelEquation.equationName = equationNameField.getText();
      modelEquation.equation = scriptField.getText();
      modelEquation.equationClass = (String) equationClassField.getSelectedItem();
      modelEquation.equationReference.addAll(referencePanel.tableModel.records);

      return modelEquation;
    }
  }

  private class EditParameterPanel extends EditPanel<Parameter> {

    private static final long serialVersionUID = 1826555468897327895L;

    private final FTextField idField; // mandatory
    private final JComboBox<Parameter.Classification> classificationField; // mandatory
    private final FTextField nameField; // mandatory
    private final FTextArea descriptionField;
    private final AutoSuggestField typeField;
    private final AutoSuggestField unitField; // mandatory
    private final AutoSuggestField unitCategoryField; // mandatory
    private final AutoSuggestField dataTypeField; // mandatory
    private final AutoSuggestField sourceField;
    private final AutoSuggestField subjectField;
    private final AutoSuggestField distributionField;
    private final FTextField valueField;
    private final FTextField referenceField;
    private final FTextArea variabilitySubjectField;
    private final FTextArea applicabilityField;

    private SpinnerNumberModel errorSpinnerModel;

    public EditParameterPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      idField = new FTextField(true);
      classificationField = new JComboBox<>(Parameter.Classification.values());
      nameField = new FTextField(true);
      descriptionField = new FTextArea();
      typeField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter type"), false);
      unitField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter unit"), true);
      unitCategoryField =
          GUIFactory.createAutoSuggestField(vocabs.get("Parameter unit category"), true);
      dataTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter data type"), true);
      sourceField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter source"), false);
      subjectField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter subject"), false);
      distributionField =
          GUIFactory.createAutoSuggestField(vocabs.get("Parameter distribution"), false);
      valueField = new FTextField();
      referenceField = new FTextField();
      variabilitySubjectField = new FTextArea();
      applicabilityField = new FTextArea();
      errorSpinnerModel = GUIFactory.createSpinnerDoubleModel();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      String prefix = "EditParameterPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // id
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "id"));
      fields.add(idField);

      // classification
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "classification"));
      fields.add(classificationField);

      // name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "parameterName"));
      fields.add(nameField);

      // type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "type"));
        fields.add(typeField);
      }

      // unit
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "unit"));
      fields.add(unitField);

      // unit category
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "unitCategory"));
      fields.add(unitCategoryField);

      // data type
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "dataType"));
      fields.add(dataTypeField);

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
        fields.add(valueField);
      }

      // reference
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "reference"));
        fields.add(referenceField);
      }

      // error
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "error"));
        fields.add(new FSpinner(errorSpinnerModel, false));
      }

      // Build UI
      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        labels.clear();
        fields.clear();

        // description
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "description"));
        fields.add(GUIFactory.createScrollPane(descriptionField));

        // variability
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "variabilitySubject"));
        fields.add(GUIFactory.createScrollPane(variabilitySubjectField));

        // applicability
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "applicability"));
        fields.add(GUIFactory.createScrollPane(applicabilityField));

        northPanel.add(UIUtils.createFormPanel(labels, fields));
      }
      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(Parameter t) {
      if (t != null) {
        idField.setText(t.id);
        classificationField.setSelectedItem(t.classification);
        nameField.setText(t.name);
        descriptionField.setText(t.description);
        typeField.setSelectedItem(t.type);
        unitField.setSelectedItem(t.unit);
        unitCategoryField.setSelectedItem(t.unitCategory);
        dataTypeField.setSelectedItem(t.dataType);
        sourceField.setSelectedItem(t.source);
        subjectField.setSelectedItem(t.subject);
        distributionField.setSelectedItem(t.distribution);
        valueField.setText(t.value);
        referenceField.setText(t.reference);
        if (!t.modelApplicability.isEmpty()) {
          applicabilityField.setText(t.modelApplicability.get(0));
        }
        variabilitySubjectField.setText(t.variabilitySubject);
        errorSpinnerModel.setValue(t.error);
      }
    }

    @Override
    Parameter get() {

      final Parameter param = new Parameter();
      param.id = idField.getText();
      param.classification = (Parameter.Classification) classificationField.getSelectedItem();
      param.name = nameField.getText();
      param.description = descriptionField.getText();
      param.type = (String) typeField.getSelectedItem();
      param.unit = (String) unitField.getSelectedItem();
      param.unitCategory = (String) unitCategoryField.getSelectedItem();
      param.dataType = (String) dataTypeField.getSelectedItem();
      param.source = (String) sourceField.getSelectedItem();
      param.subject = (String) subjectField.getSelectedItem();
      param.distribution = (String) distributionField.getSelectedItem();
      param.value = valueField.getText();
      param.reference = referenceField.getText();
      if (!applicabilityField.getText().isEmpty()) {
        param.modelApplicability.add(applicabilityField.getText());
      }
      param.variabilitySubject = variabilitySubjectField.getText();
      param.error = errorSpinnerModel.getNumber().doubleValue();

      return param;
    }

    @Override
    List<String> validatePanel() {

      final String prefix = "EditParameterPanel_";
      final List<String> errors = new ArrayList<>();
      if (idField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "idLabel"));
      }
      if (classificationField.getSelectedIndex() == -1) {
        errors.add("Missing " + bundle.getString(prefix + "classificationLabel"));
      }
      if (nameField.getText().isEmpty()) {
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
  }

  private class EditPopulationGroupPanel extends EditPanel<PopulationGroup> {

    private static final long serialVersionUID = -4520186348489618333L;

    private final FTextField populationNameField; // mandatory
    private final FTextField targetPopulationField; // optional
    private final FTextField populationSpanField; // optional
    private final FTextArea populationDescriptionField; // optional
    private final FTextField populationAgeField; // optional
    private final FTextField populationGenderField; // optional
    private final FTextField bmiField; // optional
    private final FTextField specialDietGroupField; // optional
    private final FTextField patternConsumptionField; // optional
    private final JComboBox<String> regionField; // optional
    private final JComboBox<String> countryField; // optional
    private final FTextField riskField; // optional
    private final FTextField seasonField; // optional

    public EditPopulationGroupPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      populationNameField = new FTextField(true);
      targetPopulationField = new FTextField();
      populationSpanField = new FTextField();
      populationDescriptionField = new FTextArea();
      populationAgeField = new FTextField();
      populationGenderField = new FTextField();
      bmiField = new FTextField();
      specialDietGroupField = new FTextField();
      patternConsumptionField = new FTextField();
      regionField = GUIFactory.createComboBox(vocabs.get("Region"));
      countryField = GUIFactory.createComboBox(vocabs.get("Country"));
      riskField = new FTextField();
      seasonField = new FTextField();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "EditPopulationGroupPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // population name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "populationName"));
      fields.add(populationNameField);

      if (isAdvanced) {

        // target population
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "targetPopulation"));
        fields.add(targetPopulationField);

        // population span
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "populationSpan"));
        fields.add(populationSpanField);

        // population age
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "populationAge"));
        fields.add(populationAgeField);

        // population gender
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "populationGender"));
        fields.add(populationGenderField);

        // bmi
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "bmi"));
        fields.add(bmiField);

        // special diet group
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "specialDietGroups"));
        fields.add(specialDietGroupField);

        // pattern consumption
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "patternConsumption"));
        fields.add(patternConsumptionField);

        // region
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "region"));
        fields.add(regionField);

        // country
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "country"));
        fields.add(countryField);

        // risk
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "riskAndPopulation"));
        fields.add(riskField);

        // season
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "season"));
        fields.add(seasonField);
      }

      final FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "populationDescription");
        JScrollPane populationDescriptionPane =
            GUIFactory.createScrollPane(populationDescriptionField);

        northPanel.add(UIUtils.createFormPanel(Arrays.asList(label),
            Arrays.asList(populationDescriptionPane)));
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final PopulationGroup t) {
      if (t != null) {
        populationNameField.setText(t.populationName);
        targetPopulationField.setText(t.targetPopulation);

        if (!t.populationSpan.isEmpty()) {
          populationSpanField.setText(t.populationSpan.get(0));
        }

        if (!t.populationDescription.isEmpty()) {
          populationDescriptionField.setText(t.populationDescription.get(0));
        }

        if (!t.populationAge.isEmpty()) {
          populationAgeField.setText(t.populationAge.get(0));
        }

        populationGenderField.setText(t.populationGender);

        if (!t.bmi.isEmpty()) {
          bmiField.setText(t.bmi.get(0));
        }

        if (!t.specialDietGroups.isEmpty()) {
          specialDietGroupField.setText(t.specialDietGroups.get(0));
        }

        if (!t.patternConsumption.isEmpty()) {
          patternConsumptionField.setText(t.patternConsumption.get(0));
        }

        regionField.setSelectedItem(t.region);

        countryField.setSelectedItem(t.country);

        if (!t.populationRiskFactor.isEmpty()) {
          riskField.setText(t.populationRiskFactor.get(0));
        }

        if (!t.season.isEmpty()) {
          seasonField.setText(t.season.get(0));
        }
      }
    }

    @Override
    PopulationGroup get() {
      final PopulationGroup populationGroup = new PopulationGroup();
      populationGroup.populationName = populationNameField.getText();
      populationGroup.targetPopulation = targetPopulationField.getText();

      final String populationSpan = populationSpanField.getText();
      if (!populationSpan.isEmpty()) {
        populationGroup.populationSpan.add(populationSpan);
      }

      final String populationDescription = populationDescriptionField.getText();
      if (!populationDescription.isEmpty()) {
        populationGroup.populationDescription.add(populationDescription);
      }

      final String populationAge = populationAgeField.getText();
      if (!populationAge.isEmpty()) {
        populationGroup.populationAge.add(populationAge);
      }

      populationGroup.populationGender = populationGenderField.getText();

      final String bmi = bmiField.getText();
      if (!bmi.isEmpty()) {
        populationGroup.bmi.add(bmi);
      }

      final String specialDietGroup = specialDietGroupField.getText();
      if (!specialDietGroup.isEmpty()) {
        populationGroup.specialDietGroups.add(specialDietGroup);
      }

      final String patternConsumption = patternConsumptionField.getText();
      if (!patternConsumption.isEmpty()) {
        populationGroup.patternConsumption.add(patternConsumption);
      }

      if (regionField.getSelectedIndex() != -1) {
        populationGroup.region.add((String) regionField.getSelectedItem());
      }

      if (countryField.getSelectedIndex() != -1) {
        populationGroup.country.add((String) countryField.getSelectedItem());
      }

      final String risk = riskField.getText();
      if (!risk.isEmpty()) {
        populationGroup.populationRiskFactor.add(risk);
      }

      final String season = seasonField.getText();
      if (!season.isEmpty()) {
        populationGroup.season.add(season);
      }

      return populationGroup;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(1);
      if (populationNameField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString("EditPopulationGroupPanel_populationNameLabel"));
      }
      return errors;
    }
  }

  private class EditProductPanel extends EditPanel<Product> {

    private static final long serialVersionUID = -7400646603919832139L;

    private final AutoSuggestField envNameField; // mandatory
    private final FTextArea envDescriptionField; // optional
    private final AutoSuggestField envUnitField; // mandatory
    private final JComboBox<String> productionMethodField; // optional
    private final JComboBox<String> packagingField; // optional
    private final JComboBox<String> productTreatmentField; // optional
    private final AutoSuggestField originCountryField; // optional
    private final AutoSuggestField originAreaField; // optional
    private final AutoSuggestField fisheriesAreaField; // optional
    private final FixedDateChooser productionField; // optional
    private final FixedDateChooser expirationField; // optional

    public EditProductPanel(boolean isAdvanced) {

      super(new BorderLayout());

      envNameField = GUIFactory.createAutoSuggestField(vocabs.get("Product-matrix name"), true);
      envDescriptionField = new FTextArea();
      envUnitField = GUIFactory.createAutoSuggestField(vocabs.get("Product-matrix unit"), true);
      productionMethodField = GUIFactory.createComboBox(vocabs.get("Method of production"));
      packagingField = GUIFactory.createComboBox(vocabs.get("Packaging"));
      productTreatmentField = GUIFactory.createComboBox(vocabs.get("Product treatment"));
      originCountryField =
          GUIFactory.createAutoSuggestField(vocabs.get("Country of origin"), false);
      originAreaField = GUIFactory.createAutoSuggestField(vocabs.get("Area of origin"), false);
      fisheriesAreaField = GUIFactory.createAutoSuggestField(vocabs.get("Fisheries area"), false);
      productionField = new FixedDateChooser();
      expirationField = new FixedDateChooser();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      String prefix = "EditProductPanel_";

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
        fields.add(productionMethodField);

        // packaging
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "packaging"));
        fields.add(packagingField);

        // product treatment
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "productTreatment"));
        fields.add(productTreatmentField);

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
        fields.add(productionField);

        // expiration date
        labels.add(GUIFactory.createLabelWithToolTip(prefix + "expirationDate"));
        fields.add(expirationField);
      }

      // Build UI
      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(prefix + "envDescription");
        JScrollPane envDescriptionPane = GUIFactory.createScrollPane(envDescriptionField);

        northPanel
            .add(UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(envDescriptionPane)));
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final Product t) {

      if (t != null) {
        envNameField.setSelectedItem(t.environmentName);
        envDescriptionField.setText(t.environmentDescription);
        envUnitField.setSelectedItem(t.environmentUnit);
        // TODO: productonMethodComboBox
        // TODO: packagingComboBox
        // TODO: productTreatmentComboBox
        originCountryField.setSelectedItem(t.originCountry);
        originAreaField.setSelectedItem(t.originArea);
        fisheriesAreaField.setSelectedItem(t.fisheriesArea);
        productionField.setDate(t.productionDate);
        expirationField.setDate(t.expirationDate);
      }
    }

    @Override
    Product get() {

      final Product product = new Product();
      product.environmentName = (String) envNameField.getSelectedItem();
      product.environmentDescription = envDescriptionField.getText();
      product.environmentUnit = (String) envUnitField.getSelectedItem();
      Arrays.stream(productionMethodField.getSelectedObjects()).map(it -> (String) it)
          .forEach(product.productionMethod::add);
      Arrays.stream(packagingField.getSelectedObjects()).map(it -> (String) it)
          .forEach(product.packaging::add);
      Arrays.stream(productTreatmentField.getSelectedObjects()).map(it -> (String) it)
          .forEach(product.productTreatment::add);
      product.originCountry = (String) originCountryField.getSelectedItem();
      product.originArea = (String) originAreaField.getSelectedItem();
      product.fisheriesArea = (String) fisheriesAreaField.getSelectedItem();
      product.productionDate = productionField.getDate();
      product.expirationDate = expirationField.getDate();

      return product;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "EditProductPanel_";
      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(envNameField)) {
        errors.add("Missing " + bundle.getString(prefix + "envNameLabel"));
      }
      if (!hasValidValue(envUnitField)) {
        errors.add("Missing " + bundle.getString(prefix + "envUnitLabel"));
      }

      return errors;
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

  private class EditReferencePanel extends EditPanel<Record> {

    private static final long serialVersionUID = -6874752919377124455L;

    private static final String dateFormatStr = "yyyy-MM-dd";

    // Spinner models starting with 0 and taking positive ints only
    private final SpinnerNumberModel volumeSpinnerModel; // optional
    private final SpinnerNumberModel issueSpinnerModel; // optional

    private final JCheckBox isReferenceDescriptionField; // optional
    private final JComboBox<String> typeField; // optional
    private final FixedDateChooser dateField; // optional
    private final FTextField pmidField; // optional
    private final FTextField doiField; // mandatory
    private final FTextField authorListField; // optional
    private final FTextField titleField; // mandatory
    private final FTextArea abstractField; // optional
    private final FTextField journalField; // optional
    private final FTextField pageField; // optional
    private final FTextField statusField; // optional
    private final FTextField websiteField; // optional
    private final FTextArea commentField; // optional

    EditReferencePanel(final boolean isAdvanced) {

      super(new BorderLayout());

      // Create fields
      volumeSpinnerModel = GUIFactory.createSpinnerIntegerModel();
      issueSpinnerModel = GUIFactory.createSpinnerIntegerModel();

      isReferenceDescriptionField = new JCheckBox("Is reference description *");
      typeField = GUIFactory.createComboBox(referenceTypeLabels.values());
      dateField = new FixedDateChooser();
      pmidField = new FTextField();
      doiField = new FTextField(true);
      authorListField = new FTextField();
      titleField = new FTextField(true);
      abstractField = new FTextArea();
      journalField = new FTextField();
      pageField = new FTextField();
      statusField = new FTextField();
      websiteField = new FTextField();
      commentField = new FTextArea();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      // Create labels
      String prefix = "EditReferencePanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // type
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "typeLabel")));
        fields.add(typeField);
      }

      // date
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "dateLabel")));
        fields.add(dateField);
      }

      // pmid
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "pmidLabel")));
        fields.add(pmidField);
      }

      // doi
      labels.add(new FLabel(bundle.getString(prefix + "doiLabel")));
      fields.add(doiField);

      // author
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "authorListLabel")));
        fields.add(authorListField);
      }

      // title
      labels.add(new FLabel(bundle.getString(prefix + "titleLabel")));
      fields.add(titleField);

      // journal
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "journalLabel")));
        fields.add(journalField);
      }

      // volume
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "volumeLabel")));
        fields.add(new FSpinner(volumeSpinnerModel, false));
      }

      // issue
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "issueLabel")));
        fields.add(new FSpinner(issueSpinnerModel, false));
      }

      // page
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "pageLabel")));
        fields.add(pageField);
      }

      // status
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "statusLabel")));
        fields.add(statusField);
      }

      // website
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "websiteLabel")));
        fields.add(websiteField);
      }

      // Build UI
      isReferenceDescriptionField.setBackground(UIUtils.WHITE);

      // isReferenceDescription panel
      FPanel isReferenceDescriptionPanel = UIUtils.createWestPanel(isReferenceDescriptionField);

      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(isReferenceDescriptionPanel);
      northPanel.add(formPanel);

      if (isAdvanced) {
        List<FLabel> labels2 = Arrays.asList(new FLabel(bundle.getString(prefix + "abstractLabel")),
            new FLabel(bundle.getString(prefix + "commentLabel")));
        List<JComponent> fields2 = Arrays.asList(GUIFactory.createScrollPane(abstractField),
            GUIFactory.createScrollPane(commentField));
        northPanel.add(UIUtils.createFormPanel(labels2, fields2));
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final Record t) {
      if (t != null) {
        final Type type = t.getType();
        if (type != null) {
          typeField.setSelectedItem(referenceTypeLabels.get(type));
        }

        final String dateString = t.getDate();
        if (dateString != null) {
          try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
            dateField.setDate(dateFormat.parse(dateString));
          } catch (final ParseException exception) {
            LOGGER.warn("Invalid date", exception);
          }
        }

        // TODO: PMID
        doiField.setText(t.getDoi());

        final List<String> authors = t.getAuthors();
        if (authors != null) {
          authorListField.setText(String.join(";", authors));
        }

        titleField.setText(t.getTitle());
        abstractField.setText(t.getAbstr());
        journalField.setText(t.getSecondaryTitle());

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

        // TODO: Page
        // TODO: Status

        websiteField.setText(t.getWebsiteLink());

        // TODO: Comment
      }
    }

    @Override
    Record get() {

      final Record record = new Record();
      // TODO: isReferenceDescriptionCheckBox

      final int selectedTypeIndex = typeField.getSelectedIndex();
      if (selectedTypeIndex != -1) {
        final Type type = referenceTypeLabels.keySet()
            .toArray(new Type[referenceTypeLabels.size()])[selectedTypeIndex];
        record.setType(type);
      }

      final Date date = dateField.getDate();
      if (date != null) {
        record.setDate(new SimpleDateFormat(dateFormatStr).format(date));
      }

      // TODO: PMID

      record.setDoi(doiField.getText());

      final String authors = authorListField.getText();
      if (authors != null) {
        Arrays.stream(authors.split(";")).forEach(record::addAuthor);
      }

      record.setTitle(titleField.getText());
      record.setAbstr(abstractField.getText());
      record.setSecondaryTitle(journalField.getText());

      final Number volumeNumber = volumeSpinnerModel.getNumber();
      if (volumeNumber != null) {
        record.setVolumeNumber(volumeNumber.toString());
      }

      final Number issueNumber = issueSpinnerModel.getNumber();
      if (issueNumber != null) {
        record.setIssueNumber(issueNumber.intValue());
      }

      // TODO: Page
      // TODO: Status

      record.setWebsiteLink(websiteField.getText());

      // TODO: comment

      return record;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "EditReferencePanel_";
      final List<String> errors = new ArrayList<>(2);
      if (doiField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "doiLabel"));
      }
      if (titleField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "titleLabel"));
      }

      return errors;
    }
  }

  private class EditStudySamplePanel extends EditPanel<StudySample> {

    private static final long serialVersionUID = -4740851101237646103L;

    private final FTextField sampleNameField; // mandatory
    private final FTextField sampleProtocolField; // optional
    private final AutoSuggestField samplingStrategyField; // optional
    private final AutoSuggestField samplingTypeField; // optional
    private final AutoSuggestField samplingMethodField; // optional
    private final FTextField samplingPlanField; // mandatory
    private final FTextField samplingWeightField; // mandatory
    private final FTextField samplingSizeField; // mandatory
    private final AutoSuggestField lotSizeUnitField; // optional
    private final AutoSuggestField samplingPointField; // optional

    public EditStudySamplePanel(final boolean isAdvanced) {

      super(new BorderLayout());

      sampleNameField = new FTextField(true);
      sampleProtocolField = new FTextField(true);
      samplingStrategyField =
          GUIFactory.createAutoSuggestField(vocabs.get("Sampling strategy"), false);
      samplingTypeField =
          GUIFactory.createAutoSuggestField(vocabs.get("Type of sampling program"), false);
      samplingMethodField = GUIFactory.createAutoSuggestField(vocabs.get("Sampling method"), false);
      samplingPlanField = new FTextField(true);
      samplingWeightField = new FTextField(true);
      samplingSizeField = new FTextField(true);
      lotSizeUnitField = GUIFactory.createAutoSuggestField(vocabs.get("Lot size unit"), false);
      samplingPointField = GUIFactory.createAutoSuggestField(vocabs.get("Sampling point"), false);

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      String prefix = "EditStudySamplePanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // sample name
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "sampleName"));
      fields.add(sampleNameField);

      // sample protocol label
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "sampleProtocol"));
      fields.add(sampleProtocolField);

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
      fields.add(samplingPlanField);

      // sampling weight
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingWeight"));
      fields.add(samplingWeightField);

      // sampling size
      labels.add(GUIFactory.createLabelWithToolTip(prefix + "samplingSize"));
      fields.add(samplingSizeField);

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
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);
      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final StudySample t) {
      if (t != null) {
        sampleNameField.setText(t.sample);
        sampleProtocolField.setText(t.collectionProtocol);
        samplingStrategyField.setSelectedItem(t.samplingStrategy);
        samplingTypeField.setSelectedItem(t.samplingProgramType);
        samplingMethodField.setSelectedItem(t.samplingMethod);
        samplingPlanField.setText(t.samplingPlan);
        samplingWeightField.setText(t.samplingWeight);
        samplingSizeField.setText(t.samplingSize);
        lotSizeUnitField.setSelectedItem(t.lotSizeUnit);
        samplingPointField.setSelectedItem(t.samplingPoint);
      }
    }

    @Override
    StudySample get() {

      final StudySample studySample = new StudySample();
      studySample.sample = sampleNameField.getText();
      studySample.collectionProtocol = sampleProtocolField.getText();
      studySample.samplingPlan = samplingPlanField.getText();
      studySample.samplingWeight = samplingWeightField.getText();
      studySample.samplingSize = samplingSizeField.getText();
      studySample.samplingStrategy = (String) samplingStrategyField.getSelectedItem();
      studySample.samplingProgramType = (String) samplingTypeField.getSelectedItem();
      studySample.samplingMethod = (String) samplingMethodField.getSelectedItem();
      studySample.lotSizeUnit = (String) lotSizeUnitField.getSelectedItem();
      studySample.samplingPoint = (String) samplingPointField.getSelectedItem();

      return studySample;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "EditStudySamplePanel_";
      final List<String> errors = new ArrayList<>(5);
      if (sampleNameField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "sampleNameLabel"));
      }
      if (sampleProtocolField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "sampleProtocolLabel"));
      }
      if (samplingPlanField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "samplingPlanLabel"));
      }
      if (samplingWeightField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "samplingWeightLabel"));
      }
      if (samplingSizeField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "samplingSizeLabel"));
      }

      return errors;
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

    private final JTextField studyNameField; // mandatory
    private final JTextField sourceField; // optional
    private final JTextField identifierField; // mandatory
    private final CreatorPanel creatorPanel;
    private final FixedDateChooser creationField;
    private final AutoSuggestField rightsField; // mandatory
    private final JCheckBox availabilityField;
    private final JTextField urlField;
    private final AutoSuggestField formatField; // optional
    private final ReferencePanel referencePanel;
    private final AutoSuggestField languageField; // optional
    private final AutoSuggestField softwareField; // optional
    private final AutoSuggestField languageWrittenInField; // optional
    private final AutoSuggestField statusField; // optional
    private final FTextArea objectiveField; // optional
    private final FTextArea descriptionField; // optional

    public GeneralInformationPanel() {

      super(new BorderLayout());

      // Create fields
      advancedCheckBox = new JCheckBox("Advanced");
      studyNameField = new FTextField(true);
      sourceField = new FTextField(true);
      identifierField = new FTextField(true);
      creatorPanel = new CreatorPanel();
      creationField = new FixedDateChooser();
      rightsField = GUIFactory.createAutoSuggestField(vocabs.get("Rights"), true);
      availabilityField = new JCheckBox();
      urlField = new FTextField(true);
      formatField = GUIFactory.createAutoSuggestField(vocabs.get("Format"), false);
      referencePanel = new ReferencePanel(advancedCheckBox.isSelected());
      languageField = GUIFactory.createAutoSuggestField(vocabs.get("Language"), false);
      softwareField = GUIFactory.createAutoSuggestField(vocabs.get("Software"), false);
      languageWrittenInField =
          GUIFactory.createAutoSuggestField(vocabs.get("Language written in"), false);
      statusField = GUIFactory.createAutoSuggestField(vocabs.get("Status"), false);
      objectiveField = new FTextArea();
      descriptionField = new FTextArea();

      createUI();
    }

    private void createUI() {

      // Create labels
      String prefix = "GeneralInformationPanel_";

      FLabel studyNameLabel = GUIFactory.createLabelWithToolTip(prefix + "studyName");
      FLabel identifierLabel = GUIFactory.createLabelWithToolTip(prefix + "identifier");
      FLabel creationDateLabel = GUIFactory.createLabelWithToolTip(prefix + "creationDate");
      FLabel rightsLabel = GUIFactory.createLabelWithToolTip(prefix + "rights");
      FLabel availabilityLabel = GUIFactory.createLabelWithToolTip(prefix + "availability");
      FLabel sourceLabel = GUIFactory.createLabelWithToolTip(prefix + "source");
      FLabel urlLabel = GUIFactory.createLabelWithToolTip(prefix + "url");
      FLabel formatLabel = GUIFactory.createLabelWithToolTip(prefix + "format");
      FLabel languageLabel = GUIFactory.createLabelWithToolTip(prefix + "language");
      FLabel softwareLabel = GUIFactory.createLabelWithToolTip(prefix + "software");
      FLabel languageWrittenInLabel =
          GUIFactory.createLabelWithToolTip(prefix + "languageWrittenIn");
      FLabel statusLabel = GUIFactory.createLabelWithToolTip(prefix + "status");

      availabilityField.setBackground(UIUtils.WHITE);

      // Hide initially advanced components
      final List<JComponent> advancedComponents = Arrays.asList(formatField, languageField,
          softwareField, languageWrittenInField, statusField, objectiveField, descriptionField);
      advancedComponents.forEach(it -> it.setEnabled(false));

      // formPanel
      FPanel creationDatePanel = UIUtils.createWestPanel(creationField);
      FPanel availabilityPanel = UIUtils.createWestPanel(availabilityField);
      final JPanel formPanel = UIUtils.createFormPanel(
          Arrays.asList(studyNameLabel, identifierLabel, creationDateLabel, rightsLabel,
              availabilityLabel, urlLabel, sourceLabel, formatLabel, languageLabel, softwareLabel,
              languageWrittenInLabel, statusLabel),
          Arrays.asList(studyNameField, identifierField, creationDatePanel, rightsField,
              availabilityPanel, urlField, sourceField, formatField, languageField, softwareField,
              languageWrittenInField, statusField));

      advancedCheckBox.addItemListener(event -> {
        final boolean showAdvanced = advancedCheckBox.isSelected();
        advancedComponents.forEach(it -> it.setEnabled(showAdvanced));
        referencePanel.isAdvanced = showAdvanced;
      });

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(formPanel);

      {
        List<FLabel> labels = Arrays.asList(GUIFactory.createLabelWithToolTip(prefix + "objective"),
            GUIFactory.createLabelWithToolTip(prefix + "description"));
        List<JComponent> fields = Arrays.asList(GUIFactory.createScrollPane(objectiveField),
            GUIFactory.createScrollPane(descriptionField));

        northPanel.add(UIUtils.createFormPanel(labels, fields));
      }
      northPanel.add(creatorPanel);
      northPanel.add(referencePanel);

      add(northPanel, BorderLayout.NORTH);
    }

    void init(final GeneralInformation generalInformation) {

      if (generalInformation != null) {
        studyNameField.setText(generalInformation.name);
        sourceField.setText(generalInformation.source);
        identifierField.setText(generalInformation.identifier);
        creatorPanel.init(generalInformation.creators);
        creationField.setDate(generalInformation.creationDate);
        rightsField.setSelectedItem(generalInformation.rights);
        availabilityField.setSelected(generalInformation.isAvailable);
        if (generalInformation.url != null) {
          urlField.setText(generalInformation.url.toString());
        }
        formatField.setSelectedItem(generalInformation.format);
        referencePanel.init(generalInformation.reference);
        languageField.setSelectedItem(generalInformation.language);
        softwareField.setSelectedItem(generalInformation.software);
        languageWrittenInField.setSelectedItem(generalInformation.languageWrittenIn);
        statusField.setSelectedItem(generalInformation.status);
        objectiveField.setText(generalInformation.objective);
        descriptionField.setText(generalInformation.description);
      }
    }

    GeneralInformation get() {

      final GeneralInformation generalInformation = new GeneralInformation();
      generalInformation.name = studyNameField.getText();
      generalInformation.source = sourceField.getText();
      generalInformation.identifier = identifierField.getText();
      generalInformation.creationDate = creationField.getDate();
      generalInformation.rights = (String) rightsField.getSelectedItem();
      generalInformation.isAvailable = availabilityField.isSelected();

      final String urlText = urlField.getText();
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
      generalInformation.objective = objectiveField.getText();
      generalInformation.description = descriptionField.getText();

      return generalInformation;
    }
  }

  private class ReferencePanel extends FPanel {

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

      setLayout(new BorderLayout());
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIUtils.BLUE),
          "References"));

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

      FPanel panel = UIUtils.createTablePanel(coolTable);

      FPanel buttonsPanel =
          UIUtils.createHorizontalPanel(addButton, fileUploadButton, editButton, removeButton);
      FPanel centeredPanel = UIUtils.createCenterPanel(buttonsPanel);
      panel.add(centeredPanel, BorderLayout.SOUTH);

      add(panel);
    }

    void init(final List<Record> references) {
      references.forEach(tableModel::add);
    }
  }

  private class CreatorPanel extends FPanel {

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

        StructuredName structuredName = vcard.getStructuredName();
        String givenName = structuredName.getGiven();
        String familyName = structuredName.getFamily();
        String contact = vcard.getEmails().get(0).getValue();
        addRow(new String[] {givenName, familyName, contact});
      }

      void modify(final int rowNumber, final VCard vcard) {
        vcards.set(rowNumber, vcard);


        StructuredName structuredName = vcard.getStructuredName();
        setValueAt(structuredName.getGiven(), rowNumber, 0);
        setValueAt(structuredName.getFamily(), rowNumber, 1);
        setValueAt(vcard.getEmails().get(0).getValue(), rowNumber, 2);
      }

      void remove(final int rowNumber) {
        vcards.remove(rowNumber);
        removeRow(rowNumber);
      }
    }

    public CreatorPanel() {

      setLayout(new BorderLayout());

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIUtils.BLUE),
          "Creators"));

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

      FPanel panel = UIUtils.createTablePanel(myTable);

      FPanel buttonsPanel =
          UIUtils.createHorizontalPanel(addButton, fileUploadButton, editButton, removeButton);

      FPanel centeredPanel = UIUtils.createCenterPanel(buttonsPanel);
      panel.add(centeredPanel, BorderLayout.SOUTH);

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

    private final FTextField givenNameField; // mandatory
    private final FTextField familyNameField; // mandatory
    private final FTextField contactField; // mandatory

    public EditCreatorPanel() {
      super(new BorderLayout());
      givenNameField = new FTextField(true);
      familyNameField = new FTextField(true);
      contactField = new FTextField(true);

      createUI();
    }

    private void createUI() {
      // Create labels
      String prefix = "EditCreatorPanel_";
      FLabel givenNameLabel = new FLabel(bundle.getString(prefix + "givenNameLabel"));
      FLabel familyNameLabel = new FLabel(bundle.getString(prefix + "familyNameLabel"));
      FLabel contactLabel = new FLabel(bundle.getString(prefix + "contactLabel"));

      final FPanel formPanel =
          UIUtils.createFormPanel(Arrays.asList(givenNameLabel, familyNameLabel, contactLabel),
              Arrays.asList(givenNameField, familyNameField, contactField));
      final FPanel northPanel = UIUtils.createNorthPanel(formPanel);
      add(northPanel);
    }

    void init(final VCard creator) {

      if (creator != null) {

        StructuredName structuredName = creator.getStructuredName();
        if (structuredName != null) {
          givenNameField.setText(structuredName.getGiven());
          familyNameField.setText(structuredName.getFamily());
        }
        if (!creator.getEmails().isEmpty())
          contactField.setText(creator.getEmails().get(0).getValue());
      }
    }

    @Override
    VCard get() {

      final VCard vCard = new VCard();

      StructuredName structuredName = new StructuredName();
      structuredName.setGiven(givenNameField.getText());
      structuredName.setFamily(givenNameField.getText());

      final String contactText = contactField.getText();
      if (StringUtils.isNotEmpty(contactText)) {
        vCard.addEmail(contactText);
      }

      return vCard;
    }

    @Override
    List<String> validatePanel() {

      String prefix = "EditCreatorPanel_";

      final List<String> errors = new ArrayList<>(3);
      if (givenNameField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "givenNameLabel"));
      }
      if (familyNameField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "familyNameLabel"));
      }
      if (contactField.getText().isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "contactLabel"));
      }

      return errors;
    }
  }

  private class ScopePanel extends TopLevelPanel<Scope> {

    private static final long serialVersionUID = 8153319336584952056L;

    final JButton productButton = new JButton();
    final JButton hazardButton = new JButton();
    final JButton populationButton = new JButton();

    final FTextArea commentTextArea = new FTextArea();

    final FixedDateChooser dateChooser = new FixedDateChooser();
    final AutoSuggestField regionField =
        GUIFactory.createAutoSuggestField(vocabs.get("Region"), false);
    final AutoSuggestField countryField =
        GUIFactory.createAutoSuggestField(vocabs.get("Country"), false);

    private Scope scope = null;

    ScopePanel() {

      super(new BorderLayout());

      final JCheckBox advancedCheckBox = new JCheckBox("Advanced");

      String prefix = "ScopePanel_";

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
      FPanel formPanel = UIUtils.createFormPanel(
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
        JScrollPane commentPane = GUIFactory.createScrollPane(commentTextArea);
        northPanel.add(UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(commentPane)));
      }
      add(northPanel, BorderLayout.NORTH);
    }

    void init(final Scope scope) {
      if (scope != null) {

        this.scope = scope;

        if (StringUtils.isNoneEmpty(scope.product.environmentName, scope.product.environmentUnit)) {
          productButton
              .setText(scope.product.environmentName + "_" + scope.product.environmentUnit);
        }
        if (StringUtils.isNoneEmpty(scope.hazard.hazardName, scope.hazard.hazardUnit)) {
          hazardButton.setText(scope.hazard.hazardName + "_" + scope.hazard.hazardUnit);
        }
        if (StringUtils.isNotEmpty(scope.populationGroup.populationName)) {
          populationButton.setText(scope.populationGroup.populationName);
        }

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

    private final JButton studyButton;
    private final JButton studySampleButton;
    private final JButton dietaryAssessmentMethodButton;
    private final JButton laboratoryButton;
    private final JButton assayButton;

    private DataBackground dataBackground = new DataBackground();

    DataBackgroundPanel() {

      super(new BorderLayout());

      studyButton = new JButton();
      studyButton.setToolTipText("Click me to add Study");
      studyButton.addActionListener(event -> {
        EditStudyPanel editPanel = new EditStudyPanel(advancedCheckBox.isSelected());
        editPanel.init(dataBackground.study);

        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create Study");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          Study study = editPanel.get();
          // Update button's text
          studyButton.setText(study.title);
          dataBackground.study = study;
        }
      });

      studySampleButton = new JButton();
      studySampleButton.setToolTipText("Click me to add Study Sample");
      studySampleButton.addActionListener(event -> {

        EditStudySamplePanel editStudySamplePanel =
            new EditStudySamplePanel(advancedCheckBox.isSelected());
        editStudySamplePanel.init(dataBackground.studySample);

        final ValidatableDialog dlg =
            new ValidatableDialog(editStudySamplePanel, "Create Study sample");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final StudySample studySample = editStudySamplePanel.get();
          // Update button's text
          studySampleButton.setText(studySample.sample);

          dataBackground.studySample = studySample;
        }
      });

      dietaryAssessmentMethodButton = new JButton();
      dietaryAssessmentMethodButton.setToolTipText("Click me to add Dietary assessment method");
      dietaryAssessmentMethodButton.addActionListener(event -> {
        EditDietaryAssessmentMethodPanel editPanel =
            new EditDietaryAssessmentMethodPanel(advancedCheckBox.isSelected());
        editPanel.init(dataBackground.dietaryAssessmentMethod);
        final ValidatableDialog dlg =
            new ValidatableDialog(editPanel, "Create dietary assessment method");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final DietaryAssessmentMethod method = editPanel.get();
          // Update button's text
          dietaryAssessmentMethodButton.setText(method.collectionTool);
          dataBackground.dietaryAssessmentMethod = method;
        }
      });

      laboratoryButton = new JButton();
      laboratoryButton.setToolTipText("Click me to add Laboratory");
      laboratoryButton.addActionListener(event -> {
        EditLaboratoryPanel editPanel = new EditLaboratoryPanel(advancedCheckBox.isSelected());
        editPanel.init(dataBackground.laboratory);
        ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create laboratory");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          Laboratory lab = editPanel.get();
          // Update button's text
          laboratoryButton.setText(lab.accreditation);
          dataBackground.laboratory = lab;
        }
      });

      assayButton = new JButton();
      assayButton.setToolTipText("Click me to add Assay");
      assayButton.addActionListener(event -> {
        EditAssayPanel editPanel = new EditAssayPanel(advancedCheckBox.isSelected());
        editPanel.init(dataBackground.assay);
        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create assay");
        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Assay assay = editPanel.get();
          // Update button's text
          assayButton.setText(assay.name);
          dataBackground.assay = assay;
        }
      });

      String prefix = "DataBackgroundPanel_";
      FLabel studyLabel = new FLabel(bundle.getString(prefix + "studyLabel"));
      FLabel studySampleLabel = new FLabel(bundle.getString(prefix + "studySampleLabel"));
      FLabel dietaryAssessmentMethodLabel =
          new FLabel(bundle.getString(prefix + "dietaryAssessmentMethodLabel"));
      FLabel laboratoryAccreditationLabel =
          new FLabel(bundle.getString(prefix + "laboratoryLabel"));
      FLabel assayLabel = new FLabel(bundle.getString(prefix + "assayLabel"));

      // formPanel
      FPanel formPanel = UIUtils.createFormPanel(
          Arrays.asList(studyLabel, studySampleLabel, dietaryAssessmentMethodLabel,
              laboratoryAccreditationLabel, assayLabel),
          Arrays.asList(studyButton, studySampleButton, dietaryAssessmentMethodButton,
              laboratoryButton, assayButton));

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
      northPanel.add(formPanel);
      add(northPanel, BorderLayout.NORTH);
    }

    void init(final DataBackground dataBackground) {

      this.dataBackground = dataBackground;

      if (StringUtils.isNotEmpty(dataBackground.study.title)) {
        studyButton.setText(dataBackground.study.title);
      }
      if (StringUtils.isNotEmpty(dataBackground.studySample.sample)) {
        studySampleButton.setText(dataBackground.studySample.sample);
      }
      if (StringUtils.isNotEmpty(dataBackground.dietaryAssessmentMethod.collectionTool)) {
        dietaryAssessmentMethodButton
            .setText(dataBackground.dietaryAssessmentMethod.collectionTool);
      }
      if (StringUtils.isNotEmpty(dataBackground.laboratory.accreditation)) {
        laboratoryButton.setText(dataBackground.laboratory.accreditation);
      }
      if (StringUtils.isNotEmpty(dataBackground.assay.name)) {
        assayButton.setText(dataBackground.assay.name);
      }
    }

    DataBackground get() {
      final DataBackground dataBackground = new DataBackground();

      dataBackground.study = this.dataBackground.study;
      dataBackground.studySample = this.dataBackground.studySample;
      dataBackground.dietaryAssessmentMethod = this.dataBackground.dietaryAssessmentMethod;
      dataBackground.laboratory = this.dataBackground.laboratory;
      dataBackground.assay = this.dataBackground.assay;

      return dataBackground;
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

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIUtils.BLUE),
          "Parameters"));

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

      final FPanel panel = UIUtils.createTablePanel(myTable);

      final FPanel buttonsPanel =
          UIUtils.createHorizontalPanel(addButton, editButton, removeButton);
      panel.add(UIUtils.createCenterPanel(buttonsPanel), BorderLayout.SOUTH);

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

      FPanel ssePanel = new FPanel();
      ssePanel.add(new FLabel("SSE"));
      ssePanel.add(new FSpinner(sseSpinnerModel, false));

      FPanel msePanel = new FPanel();
      msePanel.add(new FLabel("MSE"));
      msePanel.add(new FSpinner(mseSpinnerModel, false));

      FPanel rmsePanel = new FPanel();
      rmsePanel.add(new FLabel("RMSE"));
      rmsePanel.add(new FSpinner(rmseSpinnerModel, false));

      FPanel r2Panel = new FPanel();
      r2Panel.add(new FLabel("r-Squared"));
      r2Panel.add(new FSpinner(r2SpinnerModel, false));

      FPanel aicPanel = new FPanel();
      aicPanel.add(new FLabel("AIC"));
      aicPanel.add(new FSpinner(aicSpinnerModel, false));

      FPanel bicPanel = new FPanel();
      bicPanel.add(new FLabel("BIC"));
      bicPanel.add(new FSpinner(bicSpinnerModel, false));

      FPanel horizontalPanel =
          UIUtils.createHorizontalPanel(ssePanel, msePanel, rmsePanel, r2Panel, aicPanel, bicPanel);
      FPanel centeredPanel = UIUtils.createCenterPanel(horizontalPanel);
      add(centeredPanel);

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIUtils.BLUE),
          "Quality measures"));
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

      setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIUtils.BLUE),
          "Model equation"));

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

      final FPanel panel = UIUtils.createTablePanel(myTable);
      final FPanel buttonsPanel =
          UIUtils.createHorizontalPanel(addButton, editButton, removeButton);
      panel.add(UIUtils.createCenterPanel(buttonsPanel), BorderLayout.SOUTH);

      add(panel);
    }

    void toggleMode() {
      this.isAdvanced = !this.isAdvanced;
    }

    void init(final List<ModelEquation> modelEquations) {
      modelEquations.forEach(tableModel::add);
    }
  }
}
