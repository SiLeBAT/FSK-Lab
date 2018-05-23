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
import java.awt.Frame;
import java.awt.LayoutManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
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
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.util.SimpleFileFilter;
import org.sbml.jsbml.validator.SyntaxChecker;
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.engine.JRis;
import com.gmail.gcolaianni5.jris.exception.JRisException;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.ui.AutoSuggestField;
import de.bund.bfr.knime.fsklab.nodes.ui.FComboBox;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.FSpinner;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextArea;
import de.bund.bfr.knime.fsklab.nodes.ui.FTextField;
import de.bund.bfr.knime.fsklab.nodes.ui.FixedDateChooser;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.rakip.RakipUtil;
import de.bund.bfr.knime.fsklab.util.UTF8Control;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import metadata.Assay;
import metadata.Contact;
import metadata.DataBackground;
import metadata.DietaryAssessmentMethod;
import metadata.GeneralInformation;
import metadata.Hazard;
import metadata.Laboratory;
import metadata.MetadataFactory;
import metadata.MetadataPackage;
import metadata.ModelEquation;
import metadata.ModelMath;
import metadata.Parameter;
import metadata.ParameterClassification;
import metadata.ParameterType;
import metadata.PopulationGroup;
import metadata.Product;
import metadata.PublicationType;
import metadata.Reference;
import metadata.Scope;
import metadata.StringObject;
import metadata.Study;
import metadata.StudySample;

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

  private EditorNodeSettings settings;

  EditorNodeDialog() {

    /*
     * Initialize settings (current values are garbage, need to be loaded from settings/input port).
     */
    settings = new EditorNodeSettings();
    settings.generalInformation = MetadataFactory.eINSTANCE.createGeneralInformation();
    settings.scope = MetadataFactory.eINSTANCE.createScope();
    settings.dataBackground = MetadataFactory.eINSTANCE.createDataBackground();
    settings.modelMath = MetadataFactory.eINSTANCE.createModelMath();

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
    modelScriptPanel.setText(settings.modifiedModelScript);
    paramScriptPanel.setText(settings.modifiedParametersScript);
    vizScriptPanel.setText(settings.modifiedVisualizationScript);

    generalInformationPanel.init(settings.generalInformation);
    scopePanel.init(settings.scope);
    dataBackgroundPanel.init(settings.dataBackground);
    modelMathPanel.init(settings.modelMath);

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

      this.settings.generalInformation = inObj.generalInformation;
      this.settings.scope = inObj.scope;
      this.settings.dataBackground = inObj.dataBackground;
      this.settings.modelMath = inObj.modelMath;

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
      LOGGER.warn("Settings were not loaded", exception);
    }

    updatePanels();
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {

    // Save modified scripts to settings
    this.settings.modifiedModelScript = modelScriptPanel.getText();
    this.settings.modifiedParametersScript = paramScriptPanel.getText();
    this.settings.modifiedVisualizationScript = vizScriptPanel.getText();

    // Trim non-empty scripts
    this.settings.modifiedModelScript = StringUtils.trim(this.settings.modifiedModelScript);
    this.settings.modifiedParametersScript =
        StringUtils.trim(this.settings.modifiedParametersScript);
    this.settings.modifiedVisualizationScript =
        StringUtils.trim(this.settings.modifiedVisualizationScript);

    // Save metadata
    this.settings.generalInformation = generalInformationPanel.get();
    this.settings.scope = scopePanel.get();
    this.settings.dataBackground = dataBackgroundPanel.get();
    this.settings.modelMath = modelMathPanel.get();

    // Save resources
    this.settings.resources.clear();
    this.settings.resources.addAll(Collections.list(listModel.elements()));

    this.settings.saveSettings(settings);
  }

  private static NodeLogger LOGGER = NodeLogger.getLogger("EditNodeDialog");

  /**
   * Read controlled vocabulary from spreadsheet. The vocabulary is sorted alphabetically.
   * 
   * @param sheetName Name of the spreadsheet with the .xlsx extension
   * @return Tree map with controlled vocabulary. Keys are the vocabulary items and the values are
   *         the comments or descriptions of each item. If the sheet is not found returns empty map.
   */
  private static TreeMap<String, String> loadVocabulary(final String sheetName) {

    TreeMap<String, String> vocabulary = new TreeMap<>();

    try (
        InputStream stream =
            EditorNodeDialog.class.getResourceAsStream("/vocabularies/" + sheetName);
        XSSFWorkbook workbook = new XSSFWorkbook(stream)) {

      XSSFSheet sheet = workbook.getSheetAt(0);
      if (sheet == null) {
        LOGGER.warn("Spreadsheet not found");
        return vocabulary;
      }

      for (Row row : sheet) {
        if (row.getRowNum() == 0)
          continue;

        final Cell valueCell = row.getCell(0);
        final Cell commentCell = row.getCell(2);

        if (valueCell == null)
          continue;

        // Reads value. If error skip entry.
        String itemValue;
        try {
          itemValue = valueCell.getStringCellValue();
        } catch (Exception exception) {
          LOGGER.warnWithFormat("Controlled vocabulary %s: wrong comment %s", sheetName,
              valueCell.toString());
          continue;
        }

        // Reads description. If error assign empty string.
        String itemDescription;
        if (commentCell == null) {
          itemDescription = "";
        } else {
          try {
            itemDescription = commentCell.getStringCellValue();
          } catch (Exception exception) {
            LOGGER.warnWithFormat("Controlled vocabulary %s: wrong comment %s", sheetName,
                commentCell.toString());
            itemDescription = "";
          }
        }

        if (StringUtils.isNotBlank(itemValue))
          vocabulary.put(itemValue, itemDescription);
      }
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    return vocabulary;
  }

  private static class GUIFactory {

    private static FLabel createLabelWithToolTip(ResourceBundle bundle, String key) {
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
      setResizable(true);
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
    private final FTextField fatPercentageField; // optional
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
      fatPercentageField = new FTextField();
      detectionLimitField = new FTextField();
      quantificationLimitField = new FTextField();
      leftCensoredDataField = new FTextField();
      contaminationRangeField = new FTextField();
      uncertaintyValueField = new FTextField();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {
      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditAssayPanel_";

      // name
      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // name
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "name"));
      fields.add(nameField);

      if (isAdvanced) {
        // moisture percentage
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "moisturePercentage"));
        fields.add(moisturePercentageField);

        // fat percentage
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "fatPercentage"));
        fields.add(fatPercentageField);

        // detection limit
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "detectionLimit"));
        fields.add(detectionLimitField);

        // quantification limit
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "quantificationLimit"));
        fields.add(quantificationLimitField);

        // left censored data
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "leftCensoredData"));
        fields.add(leftCensoredDataField);

        // contamination range
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "contaminationRange"));
        fields.add(contaminationRangeField);

        // uncertainty value
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "uncertainty"));
        fields.add(uncertaintyValueField);
      }

      // northPanel
      final FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(UIUtils.createFormPanel(labels, fields));

      // description
      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(bundle, prefix + "description");
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

        nameField.setText(assay.getAssayName());
        descriptionField.setText(assay.getAssayDescription());
        moisturePercentageField.setText(assay.getPercentageOfMoisture());
        fatPercentageField.setText(assay.getPercentageOfFat());
        detectionLimitField.setText(assay.getLimitOfDetection());
        quantificationLimitField.setText(assay.getLimitOfQuantification());
        leftCensoredDataField.setText(assay.getLeftCensoredData());
        contaminationRangeField.setText(assay.getRangeOfContamination());
        uncertaintyValueField.setText(assay.getUncertaintyValue());
      }
    }

    @Override
    Assay get() {

      Assay assay = MetadataFactory.eINSTANCE.createAssay();

      assay.setAssayName(nameField.getText());
      assay.setAssayDescription(descriptionField.getText());
      assay.setPercentageOfMoisture(moisturePercentageField.getText());
      assay.setPercentageOfFat(fatPercentageField.getText());
      assay.setLimitOfDetection(detectionLimitField.getText());
      assay.setLimitOfQuantification(quantificationLimitField.getText());
      assay.setLeftCensoredData(leftCensoredDataField.getText());
      assay.setRangeOfContamination(contaminationRangeField.getText());
      assay.setUncertaintyValue(uncertaintyValueField.getText());

      return assay;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(1);
      if (nameField.getText().isEmpty()) {
        ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
        errors.add("Missing " + bundle.getString("EditAssayPanel_nameLabel"));
      }

      return errors;
    }
  }

  private class EditLaboratoryPanel extends EditPanel<Laboratory> {

    private static final long serialVersionUID = -1483775354630337114L;

    private final AutoSuggestField accreditationField;
    private final FTextField nameField;
    private final AutoSuggestField countryField;

    EditLaboratoryPanel(final boolean isAdvanced) {
      super(new BorderLayout());

      TreeMap<String, String> laboratoryAccreditationVocabulary =
          loadVocabulary("Laboratory accreditation.xlsx");
      TreeMap<String, String> laboratoryCountryVocabulary = loadVocabulary("Country.xlsx");

      List<String> accreditationValues =
          new ArrayList<>(laboratoryAccreditationVocabulary.keySet());
      List<String> accreditationComments =
          new ArrayList<>(laboratoryAccreditationVocabulary.values());
      accreditationField =
          new AutoSuggestField(10, accreditationValues, accreditationComments, true);

      nameField = new FTextField();

      List<String> countryValues = new ArrayList<>(laboratoryCountryVocabulary.keySet());
      List<String> countryComments = new ArrayList<>(laboratoryCountryVocabulary.values());
      countryField = new AutoSuggestField(10, countryValues, countryComments, false);

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditLaboratoryPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // accreditation
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "accreditation"));
      fields.add(accreditationField);

      if (isAdvanced) {
        // name
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "name"));
        fields.add(nameField);

        // country
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "country"));
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

        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryAccreditation())) {
          accreditationField.setSelectedItem(laboratory.getLaboratoryAccreditation().get(0));
        }

        if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryName())) {
          nameField.setText(laboratory.getLaboratoryName());
        }

        if (laboratory.eIsSet(pkg.getLaboratory_LaboratoryCountry())) {
          countryField.setSelectedItem(laboratory.getLaboratoryCountry());
        }
      }
    }

    @Override
    Laboratory get() {

      Laboratory laboratory = MetadataFactory.eINSTANCE.createLaboratory();

      if (accreditationField.getSelectedIndex() != -1) {
        StringObject stringObject =
            createStringObject((String) accreditationField.getSelectedItem());
        laboratory.getLaboratoryAccreditation().add(stringObject);
      }

      laboratory.setLaboratoryName(nameField.getText());

      if (countryField.getSelectedIndex() != -1) {
        laboratory.setLaboratoryCountry((String) countryField.getSelectedItem());
      }

      return laboratory;
    }

    @Override
    List<String> validatePanel() {

      List<String> errors = new ArrayList<>();
      if (!hasValidValue(accreditationField)) {

        ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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
    private final JComboBox<String> foodDescriptorsField; // optional

    EditDietaryAssessmentMethodPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      TreeMap<String, String> methodToolVocabulary =
          loadVocabulary("Method tool to collect data.xlsx");
      TreeMap<String, String> foodDescriptorsVocabulary = loadVocabulary("Food descriptors.xlsx");

      List<String> methodToolValues = new ArrayList<>(methodToolVocabulary.keySet());
      List<String> methodToolComments = new ArrayList<>(methodToolVocabulary.values());
      dataCollectionToolField =
          new AutoSuggestField(10, methodToolValues, methodToolComments, true);

      nonConsecutiveOneDayField = new FTextField(true);
      dietarySoftwareToolField = new FTextField();
      foodItemNumberField = new FTextField();
      recordTypeField = new FTextField();

      List<String> foodDescriptorValues = new ArrayList<>(foodDescriptorsVocabulary.keySet());
      List<String> foodDescriptorComments = new ArrayList<>(foodDescriptorsVocabulary.values());
      foodDescriptorsField = new FComboBox(foodDescriptorValues, foodDescriptorComments);

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditDietaryAssessmentMethodPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // data collection tool
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "dataCollectionTool"));
      fields.add(dataCollectionToolField);

      // non consecutive one day
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "nonConsecutiveOneDays"));
      fields.add(nonConsecutiveOneDayField);

      if (isAdvanced) {
        // dietary software tool
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "dietarySoftwareTool"));
        fields.add(dietarySoftwareToolField);

        // food item number
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "foodItemNumber"));
        fields.add(foodItemNumberField);

        // record type
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "recordType"));
        fields.add(recordTypeField);

        // food description
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "foodDescriptors"));
        fields.add(foodDescriptorsField);
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
        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        // collection tool
        if (method.eIsSet(pkg.getDietaryAssessmentMethod_CollectionTool())) {
          dataCollectionToolField.setSelectedItem(method.getCollectionTool());
        }

        // number of non consecutive one day
        if (method.eIsSet(pkg.getDietaryAssessmentMethod_NumberOfNonConsecutiveOneDay())) {
          int numberOfNonConsecutiveOneDay = method.getNumberOfNonConsecutiveOneDay();
          nonConsecutiveOneDayField.setText(Integer.toString(numberOfNonConsecutiveOneDay));
        }

        // software tool
        if (method.eIsSet(pkg.getDietaryAssessmentMethod_SoftwareTool())) {
          dietarySoftwareToolField.setText(method.getSoftwareTool());
        }

        // Number of food items
        if (method.eIsSet(pkg.getDietaryAssessmentMethod_NumberOfFoodItems())) {
          foodItemNumberField.setText(method.getNumberOfFoodItems());
        }

        // Record types
        if (method.eIsSet(pkg.getDietaryAssessmentMethod_RecordTypes())) {
          recordTypeField.setText(method.getRecordTypes());
        }

        // Food descriptors
        if (method.eIsSet(pkg.getDietaryAssessmentMethod_FoodDescriptors())) {
          foodDescriptorsField.setSelectedItem(method.getFoodDescriptors());
        }
      }
    }

    @Override
    DietaryAssessmentMethod get() {

      DietaryAssessmentMethod method = MetadataFactory.eINSTANCE.createDietaryAssessmentMethod();

      // collection tool
      if (dataCollectionToolField.getSelectedIndex() != -1) {
        method.setCollectionTool((String) dataCollectionToolField.getSelectedItem());
      }

      // non consecutive one day
      String nonConsecutiveOneDayFieldText = nonConsecutiveOneDayField.getText();
      if (!nonConsecutiveOneDayFieldText.isEmpty()) {
        try {
          method.setNumberOfNonConsecutiveOneDay(Integer.parseInt(nonConsecutiveOneDayFieldText));
        } catch (final NumberFormatException exception) {
          LOGGER.warn("numberOfNonConsecutiveOneDay", exception);
        }
      }

      method.setSoftwareTool(dietarySoftwareToolField.getText());
      method.setNumberOfFoodItems(foodItemNumberField.getText());
      method.setRecordTypes(recordTypeField.getText());

      // food descriptors
      if (foodDescriptorsField.getSelectedIndex() != -1) {
        method.setFoodDescriptors((String) foodDescriptorsField.getSelectedItem());
      }

      return method;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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
    private final FTextField studyProtocolComponentsNameField; // optional
    private final FTextField studyProtocolComponentsTypeField; // optional

    EditStudyPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      TreeMap<String, String> studyAssayTechnologyTypeVocabulary =
          loadVocabulary("Study Assay Technology Type.xlsx");
      TreeMap<String, String> accreditationProcedureVocabulary =
          loadVocabulary("Accreditation procedure Ass.Tec.xlsx");

      studyIdentifierField = new FTextField(true);
      studyTitleField = new FTextField(true);
      studyDescriptionField = new FTextArea();
      studyDesignTypeField = new FTextField();
      studyAssayMeasurementsTypeField = new FTextField();

      List<String> studyAssayTechnologyTypeValues =
          new ArrayList<>(studyAssayTechnologyTypeVocabulary.keySet());
      List<String> studyAssayTechnologyTypeComments =
          new ArrayList<>(studyAssayTechnologyTypeVocabulary.values());
      studyAssayTechnologyTypeField = new AutoSuggestField(10, studyAssayTechnologyTypeValues,
          studyAssayTechnologyTypeComments, false);

      studyAssayTechnologyPlatformField = new FTextField();

      List<String> accreditationProcedureValues =
          new ArrayList<>(accreditationProcedureVocabulary.keySet());
      List<String> accreditationProcedureComments =
          new ArrayList<>(accreditationProcedureVocabulary.values());
      accreditationProcedureField = new AutoSuggestField(10, accreditationProcedureValues,
          accreditationProcedureComments, false);

      studyProtocolNameField = new FTextField();
      studyProtocolTypeField = new FTextField();
      studyProtocolDescriptionField = new FTextField();
      studyProtocolURIField = new FTextField();
      studyProtocolVersionField = new FTextField();
      studyProtocolParametersField = new FTextField();
      studyProtocolComponentsNameField = new FTextField();
      studyProtocolComponentsTypeField = new FTextField();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      final String prefix = "StudyPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // study identifier
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "studyIdentifier"));
      fields.add(studyIdentifierField);

      // study title
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "studyTitle"));
      fields.add(studyTitleField);

      if (isAdvanced) {
        // study design type
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "studyDesignType"));
        fields.add(studyDesignTypeField);

        // study assay measurements type
        labels
            .add(GUIFactory.createLabelWithToolTip(bundle, prefix + "studyAssayMeasurementsType"));
        fields.add(studyAssayMeasurementsTypeField);

        // study assay technology type
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "studyAssayTechnologyType"));
        fields.add(studyAssayTechnologyTypeField);

        // study assay technology platform
        labels.add(
            GUIFactory.createLabelWithToolTip(bundle, prefix + "studyAssayTechnologyPlatform"));
        fields.add(studyAssayTechnologyPlatformField);

        // accreditation procedure for the assay technology
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "accreditationProcedure"));
        fields.add(accreditationProcedureField);

        // study protocol name
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "protocolName"));
        fields.add(studyProtocolNameField);

        // study protocol type
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "protocolType"));
        fields.add(studyProtocolTypeField);

        // study protocol description
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "protocolDescription"));
        fields.add(studyProtocolDescriptionField);

        // study protocol URI
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "protocolURI"));
        fields.add(studyProtocolURIField);

        // study protocol version
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "protocolVersion"));
        fields.add(studyProtocolVersionField);

        // study protocol parameters name
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "parameters"));
        fields.add(studyProtocolParametersField);

        // study protocol commponents name
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "componentsName"));
        fields.add(studyProtocolComponentsNameField);

        // study protocol components type
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "componentsType"));
        fields.add(studyProtocolComponentsTypeField);
      }

      // northPanel
      final JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(UIUtils.createFormPanel(labels, fields));

      // text areas
      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(bundle, prefix + "studyDescription");
        JScrollPane studyDescriptionPane = GUIFactory.createScrollPane(studyDescriptionField);
        northPanel.add(
            UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(studyDescriptionPane)));
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(Study study) {
      if (study != null) {
        studyIdentifierField.setText(study.getStudyIdentifier());
        studyTitleField.setText(study.getStudyTitle());
        studyDesignTypeField.setText(study.getStudyDesignType());
        studyAssayTechnologyTypeField.setSelectedItem(study.getStudyAssayTechnologyType());
        studyAssayMeasurementsTypeField.setText(study.getStudyAssayMeasurementType());
        studyAssayTechnologyPlatformField.setText(study.getStudyAssayTechnologyPlatform());
        accreditationProcedureField
            .setSelectedItem(study.getAccreditationProcedureForTheAssayTechnology());
        studyProtocolNameField.setText(study.getStudyProtocolName());
        studyProtocolTypeField.setText(study.getStudyProtocolType());
        studyProtocolDescriptionField.setText(study.getStudyDescription());
        if (study.getStudyProtocolURI() != null) {
          studyProtocolURIField.setText(study.getStudyProtocolURI().toString());
        }
        studyProtocolVersionField.setText(study.getStudyProtocolVersion());
        studyProtocolParametersField.setText(study.getStudyProtocolParametersName());
        studyProtocolComponentsNameField.setText(study.getStudyProtocolComponentsName());
        studyProtocolComponentsTypeField.setText(study.getStudyProtocolComponentsType());
      }
    }

    @Override
    Study get() {

      Study study = MetadataFactory.eINSTANCE.createStudy();

      study.setStudyIdentifier(studyIdentifierField.getText());
      study.setStudyTitle(studyTitleField.getText());
      study.setStudyDescription(studyDescriptionField.getText());
      study.setStudyDesignType(studyDesignTypeField.getText());

      if (studyAssayTechnologyTypeField.getSelectedIndex() != -1) {
        study.setStudyAssayTechnologyType((String) studyAssayTechnologyTypeField.getSelectedItem());
      }

      study.setStudyAssayMeasurementType(studyAssayMeasurementsTypeField.getText());
      study.setStudyAssayTechnologyPlatform(studyAssayTechnologyPlatformField.getText());

      if (accreditationProcedureField.getSelectedIndex() != -1) {
        study.setAccreditationProcedureForTheAssayTechnology(
            (String) accreditationProcedureField.getSelectedItem());
      }

      study.setStudyProtocolName(studyProtocolNameField.getText());
      study.setStudyProtocolType(studyProtocolTypeField.getText());
      study.setStudyProtocolDescription(studyProtocolDescriptionField.getText());

      try {
        study.setStudyProtocolURI(new URI(studyProtocolURIField.getText()));
      } catch (URISyntaxException e) {
      }

      study.setStudyProtocolVersion(studyProtocolVersionField.getText());
      study.setStudyProtocolParametersName(studyProtocolParametersField.getText());
      study.setStudyProtocolComponentsName(studyProtocolComponentsNameField.getText());
      study.setStudyProtocolComponentsType(studyProtocolComponentsTypeField.getText());

      return study;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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
    private final FTextField sourceOfContaminationField; // optional
    private final FTextField bmdField; // optional
    private final FTextField maxResidueLimitField; // optional
    private final FTextField noObservedAdverseField; // optional
    private final FTextField acceptableOperatorField; // optional
    private final FTextField acuteReferenceDoseField; // optional
    private final FTextField acceptableDailyIntakeField; // optional
    private final AutoSuggestField indSumField; // optional

    EditHazardPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      TreeMap<String, String> hazardTypeVocabulary = loadVocabulary("Hazard type.xlsx");
      TreeMap<String, String> hazardNameVocabulary = loadVocabulary("Hazard name.xlsx");
      TreeMap<String, String> hazardUnitVocabulary = loadVocabulary("Parameter unit.xlsx");
      TreeMap<String, String> hazardIndSumVocabulary = loadVocabulary("Hazard ind-sum.xlsx");

      List<String> hazardTypeValues = new ArrayList<>(hazardTypeVocabulary.keySet());
      List<String> hazardTypeComments = new ArrayList<>(hazardTypeVocabulary.values());
      hazardTypeField = new AutoSuggestField(10, hazardTypeValues, hazardTypeComments, true);

      List<String> hazardNameValues = new ArrayList<>(hazardNameVocabulary.keySet());
      List<String> hazardNameComments = new ArrayList<>(hazardNameVocabulary.values());
      hazardNameField = new AutoSuggestField(10, hazardNameValues, hazardNameComments, true);

      hazardDescriptionField = new FTextArea();

      List<String> hazardUnitValues = new ArrayList<>(hazardUnitVocabulary.keySet());
      List<String> hazardUnitComments = new ArrayList<>(hazardUnitVocabulary.values());
      hazardUnitField = new AutoSuggestField(10, hazardUnitValues, hazardUnitComments, true);

      adverseEffectField = new FTextField();
      sourceOfContaminationField = new FTextField();
      bmdField = new FTextField();
      maxResidueLimitField = new FTextField();
      noObservedAdverseField = new FTextField();
      acceptableOperatorField = new FTextField();
      acuteReferenceDoseField = new FTextField();
      acceptableDailyIntakeField = new FTextField();

      List<String> indSumValues = new ArrayList<>(hazardIndSumVocabulary.keySet());
      List<String> indSumComments = new ArrayList<>(hazardIndSumVocabulary.values());
      indSumField = new AutoSuggestField(10, indSumValues, indSumComments, false);

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditHazardPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // hazard type
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "hazardType"));
      fields.add(hazardTypeField);

      // hazard name
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "hazardName"));
      fields.add(hazardNameField);

      // hazard unit
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "hazardUnit"));
      fields.add(hazardUnitField);

      if (isAdvanced) {

        // adverse effect
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "adverseEffect"));
        fields.add(adverseEffectField);

        // origin
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "origin"));
        fields.add(sourceOfContaminationField);

        // bmd
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "bmd"));
        fields.add(bmdField);

        // max residue limit
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "maxResidueLimit"));
        fields.add(maxResidueLimitField);

        // no observed adverse
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "noObservedAdverse"));
        fields.add(noObservedAdverseField);

        // acceptable opeartor
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "acceptableOperator"));
        fields.add(acceptableOperatorField);

        // acute reference dose
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "acuteReferenceDose"));
        fields.add(acuteReferenceDoseField);

        // ind sum
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "indSum"));
        fields.add(indSumField);

        // acceptable daily intake
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "acceptableDailyIntake"));
        fields.add(acceptableDailyIntakeField);
      }

      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(bundle, prefix + "hazardDescription");
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

        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (hazard.eIsSet(pkg.getHazard_HazardType())) {
          hazardTypeField.setSelectedItem(hazard.getHazardType());
        }

        if (hazard.eIsSet(pkg.getHazard_HazardName())) {
          hazardNameField.setSelectedItem(hazard.getHazardName());
        }

        if (hazard.eIsSet(pkg.getHazard_HazardDescription())) {
          hazardDescriptionField.setText(hazard.getHazardDescription());
        }

        if (hazard.eIsSet(pkg.getHazard_HazardUnit())) {
          hazardUnitField.setSelectedItem(hazard.getHazardUnit());
        }

        if (hazard.eIsSet(pkg.getHazard_AdverseEffect())) {
          adverseEffectField.setText(hazard.getAdverseEffect());
        }

        if (hazard.eIsSet(pkg.getHazard_SourceOfContamination())) {
          sourceOfContaminationField.setText(hazard.getSourceOfContamination());
        }

        if (hazard.eIsSet(pkg.getHazard_BenchmarkDose())) {
          bmdField.setText(hazard.getBenchmarkDose());
        }

        if (hazard.eIsSet(pkg.getHazard_MaximumResidueLimit())) {
          maxResidueLimitField.setText(hazard.getMaximumResidueLimit());
        }

        if (hazard.eIsSet(pkg.getHazard_NoObservedAdverseAffectLevel())) {
          noObservedAdverseField.setText(hazard.getNoObservedAdverseAffectLevel());
        }

        if (hazard.eIsSet(pkg.getHazard_AcceptableOperatorExposureLevel())) {
          acceptableOperatorField.setText(hazard.getAcceptableOperatorExposureLevel());
        }

        if (hazard.eIsSet(pkg.getHazard_AcuteReferenceDose())) {
          acuteReferenceDoseField.setText(hazard.getAcuteReferenceDose());
        }

        if (hazard.eIsSet(pkg.getHazard_HazardIndSum())) {
          indSumField.setSelectedItem(hazard.getHazardIndSum());
        }

        if (hazard.eIsSet(pkg.getHazard_AcceptableDailyIntake())) {
          acceptableDailyIntakeField.setText(hazard.getAcceptableDailyIntake());
        }
      }
    }

    @Override
    Hazard get() {

      final Hazard hazard = MetadataFactory.eINSTANCE.createHazard();

      if (hazardTypeField.getSelectedIndex() != -1) {
        hazard.setHazardType((String) hazardTypeField.getSelectedItem());
      }

      if (hazardNameField.getSelectedIndex() != -1) {
        hazard.setHazardName((String) hazardNameField.getSelectedItem());
      }

      if (hazardUnitField.getSelectedIndex() != -1) {
        hazard.setHazardUnit((String) hazardUnitField.getSelectedItem());
      }

      hazard.setHazardDescription(hazardDescriptionField.getText());
      hazard.setAdverseEffect(adverseEffectField.getText());
      hazard.setSourceOfContamination(sourceOfContaminationField.getText());
      hazard.setBenchmarkDose(bmdField.getText());
      hazard.setMaximumResidueLimit(maxResidueLimitField.getText());
      hazard.setNoObservedAdverseAffectLevel(noObservedAdverseField.getText());
      hazard.setAcceptableOperatorExposureLevel(acceptableOperatorField.getText());
      hazard.setAcuteReferenceDose(acuteReferenceDoseField.getText());
      hazard.setAcceptableDailyIntake(acceptableDailyIntakeField.getText());

      if (indSumField.getSelectedIndex() != -1) {
        hazard.setHazardIndSum((String) indSumField.getSelectedItem());
      }

      return hazard;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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

      TreeMap<String, String> equationClassVocabulary =
          loadVocabulary("Model equation class-distr.xlsx");

      equationNameField = new FTextField(true);

      List<String> equationClassValues = new ArrayList<>(equationClassVocabulary.keySet());
      List<String> equationClassComments = new ArrayList<>(equationClassVocabulary.values());
      equationClassField =
          new AutoSuggestField(10, equationClassValues, equationClassComments, false);

      referencePanel = new ReferencePanel(isAdvanced);
      scriptField = new FTextArea(true);

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      // Create labels
      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditModelEquationPanel_";

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

      {
        List<FLabel> labels = new ArrayList<>();
        List<JComponent> fields = new ArrayList<>();

        // equation name
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "name"));
        fields.add(equationNameField);

        // equation class
        if (isAdvanced) {
          labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "class"));
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
        equationNameField.setText(modelEquation.getModelEquationName());
        equationClassField.setSelectedItem(modelEquation.getModelEquationClass());
        referencePanel.init(modelEquation.getReference());
        scriptField.setText(modelEquation.getModelEquation());
      }
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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
      final ModelEquation modelEquation = MetadataFactory.eINSTANCE.createModelEquation();
      modelEquation.setModelEquationName(equationNameField.getText());
      modelEquation.setModelEquation(scriptField.getText());
      if (equationClassField.getSelectedIndex() != -1) {
        modelEquation.setModelEquationClass((String) equationClassField.getSelectedItem());
      }
      modelEquation.getReference().addAll(referencePanel.tableModel.references);

      return modelEquation;
    }
  }

  private class EditParameterPanel extends EditPanel<Parameter> {

    private static final long serialVersionUID = 1826555468897327895L;

    private final FTextField idField; // mandatory
    private final JComboBox<ParameterClassification> classificationField; // mandatory
    private final FTextField nameField; // mandatory
    private final FTextArea descriptionField;
    private final AutoSuggestField typeField;
    private final AutoSuggestField unitField; // mandatory
    private final AutoSuggestField unitCategoryField;
    private final AutoSuggestField dataTypeField; // mandatory
    private final AutoSuggestField sourceField;
    private final AutoSuggestField subjectField;
    private final AutoSuggestField distributionField;
    private final FTextField valueField;
    private final FTextArea variabilitySubjectField;
    private final FTextField valueMinField;
    private final FTextField valueMaxField;

    private SpinnerNumberModel errorSpinnerModel;

    public EditParameterPanel(final boolean isAdvanced) {

      super(new BorderLayout());

      TreeMap<String, String> parameterTypeVocabulary = loadVocabulary("Parameter type.xlsx");
      TreeMap<String, String> parameterUnitVocabulary = loadVocabulary("Parameter unit.xlsx");
      TreeMap<String, String> unitCategoryVocabulary =
          loadVocabulary("Parameter unit category.xlsx");
      TreeMap<String, String> dataTypeVocabulary = loadVocabulary("Parameter data type.xlsx");
      TreeMap<String, String> sourceVocabulary = loadVocabulary("Parameter source.xlsx");
      TreeMap<String, String> subjectVocabulary = loadVocabulary("Parameter subject.xlsx");
      TreeMap<String, String> distributionVocabulary =
          loadVocabulary("Parameter distribution.xlsx");

      idField = new FTextField(true);
      classificationField = new JComboBox<>(ParameterClassification.values());
      nameField = new FTextField(true);
      descriptionField = new FTextArea();

      List<String> typeValues = new ArrayList<>(parameterTypeVocabulary.keySet());
      List<String> typeComments = new ArrayList<>(parameterTypeVocabulary.values());
      typeField = new AutoSuggestField(10, typeValues, typeComments, false);

      List<String> unitValues = new ArrayList<>(parameterUnitVocabulary.keySet());
      List<String> unitComments = new ArrayList<>(parameterUnitVocabulary.values());
      unitField = new AutoSuggestField(10, unitValues, unitComments, true);

      List<String> unitCategoryValues = new ArrayList<>(unitCategoryVocabulary.keySet());
      List<String> unitCategoryComments = new ArrayList<>(unitCategoryVocabulary.values());
      unitCategoryField = new AutoSuggestField(10, unitCategoryValues, unitCategoryComments, true);

      List<String> dataTypeValues = new ArrayList<>(dataTypeVocabulary.keySet());
      List<String> dataTypeComments = new ArrayList<>(dataTypeVocabulary.values());
      dataTypeField = new AutoSuggestField(10, dataTypeValues, dataTypeComments, true);

      List<String> sourceValues = new ArrayList<>(sourceVocabulary.keySet());
      List<String> sourceComments = new ArrayList<>(sourceVocabulary.values());
      sourceField = new AutoSuggestField(10, sourceValues, sourceComments, false);

      List<String> subjectValues = new ArrayList<>(subjectVocabulary.keySet());
      List<String> subjectComments = new ArrayList<>(subjectVocabulary.values());
      subjectField = new AutoSuggestField(10, subjectValues, subjectComments, false);

      List<String> distributionValues = new ArrayList<>(distributionVocabulary.keySet());
      List<String> distributionComments = new ArrayList<>(distributionVocabulary.values());
      distributionField = new AutoSuggestField(10, distributionValues, distributionComments, false);

      valueField = new FTextField();
      variabilitySubjectField = new FTextArea();
      valueMinField = new FTextField();
      valueMaxField = new FTextField();
      errorSpinnerModel = GUIFactory.createSpinnerDoubleModel();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditParameterPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // id
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "id"));
      fields.add(idField);

      // classification
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "classification"));
      fields.add(classificationField);

      // name
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "parameterName"));
      fields.add(nameField);

      // description
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "parameterDescription"));
        fields.add(descriptionField);
      }

      // type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "type"));
        fields.add(typeField);
      }

      // unit
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "unit"));
      fields.add(unitField);

      // unit category
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "unitCategory"));
        fields.add(unitCategoryField);
      }

      // data type
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "dataType"));
      fields.add(dataTypeField);

      // source
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "source"));
        fields.add(sourceField);
      }

      // subject
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "subject"));
        fields.add(subjectField);
      }

      // distribution
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "distribution"));
        fields.add(distributionField);
      }

      // value
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "value"));
        fields.add(valueField);
      }

      // variability subject
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "variabilitySubject"));
        fields.add(variabilitySubjectField);
      }

      // value min
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "valueMin"));
        fields.add(valueMinField);
      }

      // value max
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "valueMax"));
        fields.add(valueMaxField);
      }

      // error
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "error"));
        fields.add(new FSpinner(errorSpinnerModel, false));
      }

      // TODO: reference

      // Build UI
      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(Parameter t) {
      if (t != null) {

        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (t.eIsSet(pkg.getParameter_ParameterID())) {
          idField.setText(t.getParameterID());
        }

        if (t.eIsSet(pkg.getParameter_ParameterClassification())) {
          classificationField.setSelectedItem(t.getParameterClassification());
        }

        if (t.eIsSet(pkg.getParameter_ParameterName())) {
          nameField.setText(t.getParameterName());
        }

        if (t.eIsSet(pkg.getParameter_ParameterDescription())) {
          descriptionField.setText(t.getParameterDescription());
        }

        if (t.eIsSet(pkg.getParameter_ParameterType())) {
          typeField.setSelectedItem(t.getParameterType());
        }

        if (t.eIsSet(pkg.getParameter_ParameterUnit())) {
          unitField.setSelectedItem(t.getParameterUnit());
        }

        if (t.eIsSet(pkg.getParameter_ParameterUnitCategory())) {
          unitCategoryField.setSelectedItem(t.getParameterUnitCategory());
        }

        if (t.eIsSet(pkg.getParameter_ParameterDataType())) {
          dataTypeField.setSelectedItem(t.getParameterDataType().name());
        }

        if (t.eIsSet(pkg.getParameter_ParameterSource())) {
          sourceField.setSelectedItem(t.getParameterSource());
        }

        if (t.eIsSet(pkg.getParameter_ParameterSubject())) {
          subjectField.setSelectedItem(t.getParameterSubject());
        }

        if (t.eIsSet(pkg.getParameter_ParameterDistribution())) {
          distributionField.setSelectedItem(t.getParameterDistribution());
        }

        if (t.eIsSet(pkg.getParameter_ParameterValue())) {
          valueField.setText(t.getParameterValue());
        }

        if (t.eIsSet(pkg.getParameter_ParameterVariabilitySubject())) {
          variabilitySubjectField.setText(t.getParameterVariabilitySubject());
        }

        if (t.eIsSet(pkg.getParameter_ParameterValueMin())) {
          valueMinField.setText(t.getParameterValueMin());
        }

        if (t.eIsSet(pkg.getParameter_ParameterValueMax())) {
          valueMaxField.setText(t.getParameterValueMax());
        }

        if (t.eIsSet(pkg.getParameter_ParameterError())) {
          errorSpinnerModel.setValue(t.getParameterError());
        }
      }
    }

    @Override
    Parameter get() {

      final Parameter param = MetadataFactory.eINSTANCE.createParameter();
      param.setParameterID(idField.getText());
      param.setParameterClassification(
          (ParameterClassification) classificationField.getSelectedItem());
      param.setParameterName(nameField.getText());
      param.setParameterDescription(descriptionField.getText());
      param.setParameterType((String) typeField.getSelectedItem());
      param.setParameterUnit((String) unitField.getSelectedItem());
      param.setParameterUnitCategory((String) unitCategoryField.getSelectedItem());

      try {
        String dataTypeAsString = (String) dataTypeField.getSelectedItem();
        param.setParameterDataType(ParameterType.valueOf(dataTypeAsString));
      } catch (IllegalArgumentException ex) {
        param.setParameterDataType(ParameterType.OTHER);
      }

      param.setParameterSource((String) sourceField.getSelectedItem());
      param.setParameterSubject((String) subjectField.getSelectedItem());
      param.setParameterDistribution((String) distributionField.getSelectedItem());
      param.setParameterValue(valueField.getText());
      param.setParameterVariabilitySubject(variabilitySubjectField.getText());
      param.setParameterError(Double.toString(errorSpinnerModel.getNumber().doubleValue()));

      return param;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      final String prefix = "EditParameterPanel_";

      final List<String> errors = new ArrayList<>();

      String candidateId = idField.getText();
      if (candidateId.isEmpty()) {
        errors.add("Missing " + bundle.getString(prefix + "idLabel"));
      } else if (!SyntaxChecker.isValidId(candidateId, 3, 1)) {
        errors.add("ID is not valid");
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

      TreeMap<String, String> regionVocabulary = loadVocabulary("Region.xlsx");
      TreeMap<String, String> countryVocabulary = loadVocabulary("Country.xlsx");

      populationNameField = new FTextField(true);
      targetPopulationField = new FTextField();
      populationSpanField = new FTextField();
      populationDescriptionField = new FTextArea();
      populationAgeField = new FTextField();
      populationGenderField = new FTextField();
      bmiField = new FTextField();
      specialDietGroupField = new FTextField();
      patternConsumptionField = new FTextField();

      List<String> regionValues = new ArrayList<>(regionVocabulary.keySet());
      List<String> regionComments = new ArrayList<>(regionVocabulary.values());
      regionField = new FComboBox(regionValues, regionComments);

      List<String> countryValues = new ArrayList<>(countryVocabulary.keySet());
      List<String> countryComments = new ArrayList<>(countryVocabulary.values());
      countryField = new FComboBox(countryValues, countryComments);

      riskField = new FTextField();
      seasonField = new FTextField();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditPopulationGroupPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // population name
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "populationName"));
      fields.add(populationNameField);

      if (isAdvanced) {

        // target population
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "targetPopulation"));
        fields.add(targetPopulationField);

        // population span
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "populationSpan"));
        fields.add(populationSpanField);

        // population age
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "populationAge"));
        fields.add(populationAgeField);

        // population gender
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "populationGender"));
        fields.add(populationGenderField);

        // bmi
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "bmi"));
        fields.add(bmiField);

        // special diet group
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "specialDietGroups"));
        fields.add(specialDietGroupField);

        // pattern consumption
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "patternConsumption"));
        fields.add(patternConsumptionField);

        // region
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "region"));
        fields.add(regionField);

        // country
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "country"));
        fields.add(countryField);

        // risk
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "riskAndPopulation"));
        fields.add(riskField);

        // season
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "season"));
        fields.add(seasonField);
      }

      final FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(bundle, prefix + "populationDescription");
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
        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (t.eIsSet(pkg.getPopulationGroup_PopulationName())) {
          populationNameField.setText(t.getPopulationName());
        }

        if (t.eIsSet(pkg.getPopulationGroup_TargetPopulation())) {
          targetPopulationField.setText(t.getTargetPopulation());
        }

        if (t.eIsSet(pkg.getPopulationGroup_PopulationSpan())) {
          populationSpanField.setText(t.getPopulationSpan().get(0).getValue());
        }

        if (t.eIsSet(pkg.getPopulationGroup_PopulationDescription())) {
          populationDescriptionField.setText(t.getPopulationDescription().get(0).getValue());
        }

        if (t.eIsSet(pkg.getPopulationGroup_PopulationAge())) {
          populationAgeField.setText(t.getPopulationAge().get(0).getValue());
        }

        if (t.eIsSet(pkg.getPopulationGroup_PopulationGender())) {
          populationGenderField.setText(t.getPopulationGender());
        }

        if (t.eIsSet(pkg.getPopulationGroup_Bmi())) {
          bmiField.setText(t.getBmi().get(0).getValue());
        }

        if (t.eIsSet(pkg.getPopulationGroup_SpecialDietGroups())) {
          specialDietGroupField.setText(t.getSpecialDietGroups().get(0).getValue());
        }

        if (t.eIsSet(pkg.getPopulationGroup_PatternConsumption())) {
          patternConsumptionField.setText(t.getPatternConsumption().get(0).getValue());
        }

        if (t.eIsSet(pkg.getPopulationGroup_Region())) {
          regionField.setSelectedItem(t.getRegion());
        }

        if (t.eIsSet(pkg.getPopulationGroup_Country())) {
          countryField.setSelectedItem(t.getCountry());
        }

        if (t.eIsSet(pkg.getPopulationGroup_PopulationRiskFactor())) {
          riskField.setText(t.getPopulationRiskFactor().get(0).getValue());
        }

        if (t.eIsSet(pkg.getPopulationGroup_Season())) {
          seasonField.setText(t.getSeason().get(0).getValue());
        }
      }
    }

    @Override
    PopulationGroup get() {

      final PopulationGroup populationGroup = MetadataFactory.eINSTANCE.createPopulationGroup();

      populationGroup.setPopulationName(populationNameField.getText());
      populationGroup.setTargetPopulation(targetPopulationField.getText());

      final String populationSpan = populationSpanField.getText();
      if (!populationSpan.isEmpty()) {
        StringObject sO = createStringObject(populationSpan);
        populationGroup.getPopulationSpan().add(sO);
      }

      final String populationDescription = populationDescriptionField.getText();
      if (!populationDescription.isEmpty()) {
        StringObject sO = createStringObject(populationDescription);
        populationGroup.getPopulationDescription().add(sO);
      }

      final String populationAge = populationAgeField.getText();
      if (!populationAge.isEmpty()) {
        StringObject sO = createStringObject(populationAge);
        populationGroup.getPopulationAge().add(sO);
      }

      populationGroup.setPopulationGender(populationGenderField.getText());

      final String bmi = bmiField.getText();
      if (!bmi.isEmpty()) {
        StringObject sO = createStringObject(bmi);
        populationGroup.getBmi().add(sO);
      }

      final String specialDietGroup = specialDietGroupField.getText();
      if (!specialDietGroup.isEmpty()) {
        StringObject sO = createStringObject(specialDietGroup);
        populationGroup.getSpecialDietGroups().add(sO);
      }

      final String patternConsumption = patternConsumptionField.getText();
      if (!patternConsumption.isEmpty()) {
        StringObject sO = createStringObject(patternConsumption);
        populationGroup.getPatternConsumption().add(sO);
      }

      if (regionField.getSelectedIndex() != -1) {
        StringObject sO = createStringObject((String) regionField.getSelectedItem());
        populationGroup.getRegion().add(sO);
      }

      if (countryField.getSelectedIndex() != -1) {
        StringObject sO = createStringObject((String) countryField.getSelectedItem());
        populationGroup.getCountry().add(sO);
      }

      final String risk = riskField.getText();
      if (!risk.isEmpty()) {
        StringObject sO = createStringObject(risk);
        populationGroup.getPopulationRiskFactor().add(sO);
      }

      final String season = seasonField.getText();
      if (!season.isEmpty()) {
        StringObject sO = createStringObject(season);
        populationGroup.getSeason().add(sO);
      }

      return populationGroup;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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

      TreeMap<String, String> envNameVocabulary = loadVocabulary("Product-matrix name.xlsx");
      TreeMap<String, String> envUnitVocabulary = loadVocabulary("Parameter unit.xlsx");
      TreeMap<String, String> productionMethodVocabulary =
          loadVocabulary("Method of production.xlsx");
      TreeMap<String, String> packagingVocabulary = loadVocabulary("Packaging.xlsx");
      TreeMap<String, String> productTreatmentVocabulary = loadVocabulary("Product treatment.xlsx");
      TreeMap<String, String> originCountryVocabulary = loadVocabulary("Country.xlsx");
      TreeMap<String, String> originAreaVocabulary = loadVocabulary("Area of origin.xlsx");
      TreeMap<String, String> fisheriesAreaVocabulary = loadVocabulary("Fisheries area.xlsx");

      List<String> envNameValues = new ArrayList<>(envNameVocabulary.keySet());
      List<String> envNameComments = new ArrayList<>(envNameVocabulary.values());
      envNameField = new AutoSuggestField(10, envNameValues, envNameComments, true);

      envDescriptionField = new FTextArea();

      List<String> envUnitValues = new ArrayList<>(envUnitVocabulary.keySet());
      List<String> envUnitComments = new ArrayList<>(envUnitVocabulary.values());
      envUnitField = new AutoSuggestField(10, envUnitValues, envUnitComments, true);

      List<String> productionMethodValues = new ArrayList<>(productionMethodVocabulary.keySet());
      List<String> productionMethodComments = new ArrayList<>(productionMethodVocabulary.values());
      productionMethodField = new FComboBox(productionMethodValues, productionMethodComments);

      List<String> packagingValues = new ArrayList<>(packagingVocabulary.keySet());
      List<String> packaingComments = new ArrayList<>(packagingVocabulary.values());
      packagingField = new FComboBox(packagingValues, packaingComments);

      List<String> productTreatmentValues = new ArrayList<>(productTreatmentVocabulary.keySet());
      List<String> productTreatmentComments = new ArrayList<>(productTreatmentVocabulary.values());
      productTreatmentField = new FComboBox(productTreatmentValues, productTreatmentComments);

      List<String> originCountryValues = new ArrayList<>(originCountryVocabulary.keySet());
      List<String> originCountryComments = new ArrayList<>(originCountryVocabulary.values());
      originCountryField =
          new AutoSuggestField(10, originCountryValues, originCountryComments, false);

      List<String> originAreaValues = new ArrayList<>(originAreaVocabulary.keySet());
      List<String> originAreaComments = new ArrayList<>(originAreaVocabulary.values());
      originAreaField = new AutoSuggestField(10, originAreaValues, originAreaComments, false);

      List<String> fisheriesAreaValues = new ArrayList<>(fisheriesAreaVocabulary.keySet());
      List<String> fisheriesAreaComments = new ArrayList<>(fisheriesAreaVocabulary.values());
      fisheriesAreaField =
          new AutoSuggestField(10, fisheriesAreaValues, fisheriesAreaComments, false);

      productionField = new FixedDateChooser();
      expirationField = new FixedDateChooser();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditProductPanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // environment name
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "envName"));
      fields.add(envNameField);

      // environment unit
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "envUnit"));
      fields.add(envUnitField);

      if (isAdvanced) {

        // production method
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "productionMethod"));
        fields.add(productionMethodField);

        // packaging
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "packaging"));
        fields.add(packagingField);

        // product treatment
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "productTreatment"));
        fields.add(productTreatmentField);

        // origin country
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "originCountry"));
        fields.add(originCountryField);

        // origin area
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "originArea"));
        fields.add(originAreaField);

        // fisheries area
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "fisheriesArea"));
        fields.add(fisheriesAreaField);

        // production date
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "productionDate"));
        fields.add(productionField);

        // expiration date
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "expirationDate"));
        fields.add(expirationField);
      }

      // Build UI
      FPanel formPanel = UIUtils.createFormPanel(labels, fields);

      // northPanel
      FPanel northPanel = new FPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
      northPanel.add(formPanel);

      if (isAdvanced) {
        FLabel label = GUIFactory.createLabelWithToolTip(bundle, prefix + "envDescription");
        JScrollPane envDescriptionPane = GUIFactory.createScrollPane(envDescriptionField);

        northPanel
            .add(UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(envDescriptionPane)));
      }

      add(northPanel, BorderLayout.NORTH);
    }

    @Override
    void init(final Product t) {

      if (t != null) {

        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (t.eIsSet(pkg.getProduct_ProductName())) {
          envNameField.setSelectedItem(t.getProductName());
        }

        envDescriptionField.setText(t.getProductDescription());

        if (t.eIsSet(pkg.getProduct_ProductUnit())) {
          envUnitField.setSelectedItem(t.getProductUnit());
        }

        if (t.eIsSet(pkg.getProduct_ProductionMethod())) {
          productionMethodField.setSelectedItem(t.getProductionMethod());
        }

        if (t.eIsSet(pkg.getProduct_Packaging())) {
          packagingField.setSelectedItem(t.getPackaging());
        }

        if (t.eIsSet(pkg.getProduct_ProductTreatment())) {
          productTreatmentField.setSelectedItem(t.getProductTreatment());
        }

        if (t.eIsSet(pkg.getProduct_OriginCountry())) {
          originCountryField.setSelectedItem(t.getOriginCountry());
        }

        if (t.eIsSet(pkg.getProduct_OriginArea())) {
          originAreaField.setSelectedItem(t.getOriginArea());
        }

        if (t.eIsSet(pkg.getProduct_FisheriesArea())) {
          fisheriesAreaField.setSelectedItem(t.getFisheriesArea());
        }

        if (t.eIsSet(pkg.getProduct_ProductionDate())) {
          productionField.setDate(t.getProductionDate());
        }

        if (t.eIsSet(pkg.getProduct_ExpiryDate())) {
          expirationField.setDate(t.getExpiryDate());
        }
      }
    }

    @Override
    Product get() {

      final Product product = MetadataFactory.eINSTANCE.createProduct();

      if (envNameField.getSelectedIndex() != -1) {
        String selectedName = (String) envNameField.getSelectedItem();
        product.setProductName(selectedName);
      }

      product.setProductDescription(envDescriptionField.getText());

      if (envUnitField.getSelectedIndex() != -1) {
        String selectedUnit = (String) envUnitField.getSelectedItem();
        product.setProductUnit(selectedUnit);
      }

      if (productionMethodField.getSelectedIndex() != -1) {
        String selectedMethod = (String) productionMethodField.getSelectedItem();
        product.setProductionMethod(selectedMethod);
      }

      if (packagingField.getSelectedIndex() != -1) {
        String selectedPackaging = (String) packagingField.getSelectedItem();
        packagingField.setSelectedItem(selectedPackaging);
      }

      if (productTreatmentField.getSelectedIndex() != -1) {
        String selectedTreatment = (String) productTreatmentField.getSelectedItem();
        productTreatmentField.setSelectedItem(selectedTreatment);
      }

      if (originCountryField.getSelectedIndex() != -1) {
        String selectedCountry = (String) originCountryField.getSelectedItem();
        product.setOriginCountry(selectedCountry);
      }

      if (originAreaField.getSelectedIndex() != -1) {
        String selectedArea = (String) originAreaField.getSelectedItem();
        product.setOriginArea(selectedArea);
      }

      if (fisheriesAreaField.getSelectedIndex() != -1) {
        String selectedArea = (String) fisheriesAreaField.getSelectedItem();
        product.setFisheriesArea(selectedArea);
      }

      product.setProductionDate(productionField.getDate());
      product.setExpiryDate(expirationField.getDate());

      return product;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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

  private class EditReferencePanel extends EditPanel<Reference> {

    private static final long serialVersionUID = -6874752919377124455L;

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
    private final FTextField statusField; // optional
    private final FTextField websiteField; // optional
    private final FTextArea commentField; // optional

    EditReferencePanel(final boolean isAdvanced) {

      super(new BorderLayout());

      // Create fields
      volumeSpinnerModel = GUIFactory.createSpinnerIntegerModel();
      issueSpinnerModel = GUIFactory.createSpinnerIntegerModel();

      isReferenceDescriptionField = new JCheckBox("Is reference description *");

      // Load RIS reference types from resource bundle
      ResourceBundle risTypes = ResourceBundle.getBundle("ris_types");
      List<String> referenceTypes =
          risTypes.keySet().stream().map(risTypes::getString).collect(Collectors.toList());
      typeField = new FComboBox(referenceTypes);

      dateField = new FixedDateChooser();
      pmidField = new FTextField();
      doiField = new FTextField(true);
      authorListField = new FTextField();
      titleField = new FTextField(true);
      abstractField = new FTextArea();
      journalField = new FTextField();
      statusField = new FTextField();
      websiteField = new FTextField();
      commentField = new FTextArea();

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditReferencePanel_";

      // Create labels
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

      // abstract
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "abstractLabel")));
        fields.add(abstractField);
      }

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

      // comment
      if (isAdvanced) {
        labels.add(new FLabel(bundle.getString(prefix + "commentLabel")));
        fields.add(commentField);
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
    void init(final Reference reference) {
      if (reference != null) {

        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (reference.eIsSet(pkg.getReference_PublicationType())) {
          typeField.setSelectedItem(reference.getPublicationType());
        }

        if (reference.eIsSet(pkg.getReference_PublicationDate())) {
          dateField.setDate(reference.getPublicationDate());
        }

        if (reference.eIsSet(pkg.getReference_Pmid())) {
          pmidField.setText(reference.getPmid());
        }

        if (reference.eIsSet(pkg.getReference_Doi())) {
          doiField.setText(reference.getDoi());
        }

        if (reference.eIsSet(pkg.getReference_AuthorList())) {
          authorListField.setText(reference.getAuthorList());
        }

        if (reference.eIsSet(pkg.getReference_PublicationTitle())) {
          titleField.setText(reference.getPublicationTitle());
        }

        if (reference.eIsSet(pkg.getReference_PublicationAbstract())) {
          abstractField.setText(reference.getPublicationAbstract());
        }

        if (reference.eIsSet(pkg.getReference_PublicationJournal())) {
          journalField.setText(reference.getPublicationJournal());
        }

        if (reference.eIsSet(pkg.getReference_PublicationVolume())) {
          volumeSpinnerModel.setValue(reference.getPublicationVolume());
        }

        if (reference.eIsSet(pkg.getReference_PublicationIssue())) {
          issueSpinnerModel.setValue(reference.getPublicationIssue());
        }

        if (reference.eIsSet(pkg.getReference_PublicationStatus())) {
          statusField.setText(reference.getPublicationStatus());
        }

        if (reference.eIsSet(pkg.getReference_Comment())) {
          websiteField.setText(reference.getPublicationWebsite());
        }

        if (reference.eIsSet(pkg.getReference_Comment())) {
          commentField.setText(reference.getComment());
        }
      }
    }

    @Override
    Reference get() {

      final Reference reference = MetadataFactory.eINSTANCE.createReference();

      reference.setIsReferenceDescription(isReferenceDescriptionField.isSelected());

      if (typeField.getSelectedIndex() != -1) {
        reference.setPublicationType(PublicationType.get(typeField.getSelectedIndex()));
      }

      reference.setPublicationDate(dateField.getDate());
      reference.setPmid(pmidField.getText());
      reference.setDoi(doiField.getText());
      reference.setAuthorList(authorListField.getText());
      reference.setPublicationTitle(titleField.getText());
      reference.setPublicationAbstract(abstractField.getText());
      reference.setPublicationJournal(journalField.getText());
      reference.setPublicationVolume(volumeSpinnerModel.getNumber().intValue());
      reference.setPublicationIssue(issueSpinnerModel.getNumber().intValue());
      reference.setPublicationStatus(statusField.getText());
      reference.setPublicationWebsite(websiteField.getText());
      reference.setComment(commentField.getText());

      return reference;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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

      TreeMap<String, String> samplingStrategyVocabulary = loadVocabulary("Sampling strategy.xlsx");
      TreeMap<String, String> samplingTypeVocabulary =
          loadVocabulary("Type of sampling program.xlsx");
      TreeMap<String, String> samplingMethodVocabulary = loadVocabulary("Sampling point.xlsx");
      TreeMap<String, String> lotSizeUnitVocabulary = loadVocabulary("Parameter unit.xlsx");
      TreeMap<String, String> samplingPointVocabulary = loadVocabulary("Sampling point.xlsx");

      sampleNameField = new FTextField(true);
      sampleProtocolField = new FTextField(true);

      List<String> samplingStrategyValues = new ArrayList<>(samplingStrategyVocabulary.keySet());
      List<String> samplingStrategyComments = new ArrayList<>(samplingStrategyVocabulary.values());
      samplingStrategyField =
          new AutoSuggestField(10, samplingStrategyValues, samplingStrategyComments, false);

      List<String> samplingTypeValues = new ArrayList<>(samplingTypeVocabulary.keySet());
      List<String> samplingTypeComments = new ArrayList<>(samplingTypeVocabulary.values());
      samplingTypeField = new AutoSuggestField(10, samplingTypeValues, samplingTypeComments, false);

      List<String> samplingMethodValues = new ArrayList<>(samplingMethodVocabulary.keySet());
      List<String> samplingMethodComments = new ArrayList<>(samplingMethodVocabulary.values());
      samplingMethodField =
          new AutoSuggestField(10, samplingMethodValues, samplingMethodComments, false);

      samplingPlanField = new FTextField(true);
      samplingWeightField = new FTextField(true);
      samplingSizeField = new FTextField(true);

      List<String> lotSizeUnitValues = new ArrayList<>(lotSizeUnitVocabulary.keySet());
      List<String> lotSizeUnitComments = new ArrayList<>(lotSizeUnitVocabulary.values());
      lotSizeUnitField = new AutoSuggestField(10, lotSizeUnitValues, lotSizeUnitComments, false);

      List<String> samplingPointValues = new ArrayList<>(samplingPointVocabulary.keySet());
      List<String> samplingPointComments = new ArrayList<>(samplingPointVocabulary.values());
      samplingPointField =
          new AutoSuggestField(10, samplingPointValues, samplingPointComments, false);

      createUI(isAdvanced);
    }

    private void createUI(boolean isAdvanced) {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditStudySamplePanel_";

      List<FLabel> labels = new ArrayList<>();
      List<JComponent> fields = new ArrayList<>();

      // sample name
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "sampleName"));
      fields.add(sampleNameField);

      // sample protocol
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "sampleProtocol"));
      fields.add(sampleProtocolField);

      // sampling strategy
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "samplingStrategy"));
        fields.add(samplingStrategyField);
      }

      // sampling type
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "samplingType"));
        fields.add(samplingTypeField);
      }

      // sampling method
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "samplingMethod"));
        fields.add(samplingMethodField);
      }

      // sampling plan
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "samplingPlan"));
      fields.add(samplingPlanField);

      // sampling weight
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "samplingWeight"));
      fields.add(samplingWeightField);

      // sampling size
      labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "samplingSize"));
      fields.add(samplingSizeField);

      // lot size unit
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "lotSizeUnit"));
        fields.add(lotSizeUnitField);
      }

      // sampling point
      if (isAdvanced) {
        labels.add(GUIFactory.createLabelWithToolTip(bundle, prefix + "samplingPoint"));
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
        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (t.eIsSet(pkg.getStudySample_SampleName())) {
          sampleNameField.setText(t.getSampleName());
        }

        if (t.eIsSet(pkg.getStudySample_ProtocolOfSampleCollection())) {
          sampleProtocolField.setText(t.getProtocolOfSampleCollection());
        }

        if (t.eIsSet(pkg.getStudySample_SamplingStrategy())) {
          samplingStrategyField.setSelectedItem(t.getSamplingStrategy());
        }

        if (t.eIsSet(pkg.getStudySample_TypeOfSamplingProgram())) {
          samplingTypeField.setSelectedItem(t.getTypeOfSamplingProgram());
        }

        if (t.eIsSet(pkg.getStudySample_SamplingMethod())) {
          samplingMethodField.setSelectedItem(t.getSamplingMethod());
        }

        if (t.eIsSet(pkg.getStudySample_SamplingPlan())) {
          samplingPlanField.setText(t.getSamplingPlan());
        }

        if (t.eIsSet(pkg.getStudySample_SamplingWeight())) {
          samplingWeightField.setText(t.getSamplingWeight());
        }

        if (t.eIsSet(pkg.getStudySample_SamplingSize())) {
          samplingSizeField.setText(t.getSamplingSize());
        }

        if (t.eIsSet(pkg.getStudySample_LotSizeUnit())) {
          lotSizeUnitField.setSelectedItem(t.getLotSizeUnit());
        }

        if (t.eIsSet(pkg.getStudySample_SamplingPoint())) {
          samplingPointField.setSelectedItem(t.getSamplingPoint());
        }
      }
    }

    @Override
    StudySample get() {

      final StudySample studySample = MetadataFactory.eINSTANCE.createStudySample();
      studySample.setSampleName(sampleNameField.getText());
      studySample.setProtocolOfSampleCollection(sampleProtocolField.getText());
      studySample.setSamplingPlan(samplingPlanField.getText());
      studySample.setSamplingWeight(samplingWeightField.getText());
      studySample.setSamplingSize(samplingSizeField.getText());

      if (samplingStrategyField.getSelectedIndex() != -1) {
        studySample.setSamplingStrategy((String) samplingStrategyField.getSelectedItem());
      }

      if (samplingTypeField.getSelectedIndex() != -1) {
        studySample.setTypeOfSamplingProgram((String) samplingTypeField.getSelectedItem());
      }

      if (samplingMethodField.getSelectedIndex() != -1) {
        studySample.setSamplingMethod((String) samplingMethodField.getSelectedItem());
      }

      if (lotSizeUnitField.getSelectedIndex() != -1) {
        studySample.setLotSizeUnit((String) lotSizeUnitField.getSelectedItem());
      }

      if (samplingPointField.getSelectedIndex() != -1) {
        studySample.setSamplingPoint((String) samplingPointField.getSelectedItem());
      }

      return studySample;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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

      TreeMap<String, String> rightsVocabulary = loadVocabulary("Rights.xlsx");
      TreeMap<String, String> formatVocabulary = loadVocabulary("Format.xlsx");
      TreeMap<String, String> languageVocabulary = loadVocabulary("Language.xlsx");
      TreeMap<String, String> softwareVocabulary = loadVocabulary("Software.xlsx");
      TreeMap<String, String> languageWrittenInVocabulary =
          loadVocabulary("Language written in.xlsx");
      TreeMap<String, String> statusVocabulary = loadVocabulary("Status.xlsx");

      // Create fields
      advancedCheckBox = new JCheckBox("Advanced");
      studyNameField = new FTextField(true);
      sourceField = new FTextField(true);
      identifierField = new FTextField(true);
      creatorPanel = new CreatorPanel();
      creationField = new FixedDateChooser();

      List<String> rightValues = new ArrayList<>(rightsVocabulary.keySet());
      List<String> rightComments = new ArrayList<>(rightsVocabulary.values());
      rightsField = new AutoSuggestField(10, rightValues, rightComments, true);

      availabilityField = new JCheckBox();
      urlField = new FTextField(true);

      List<String> formatValues = new ArrayList<>(formatVocabulary.keySet());
      List<String> formatComments = new ArrayList<>(formatVocabulary.values());
      formatField = new AutoSuggestField(10, formatValues, formatComments, false);

      referencePanel = new ReferencePanel(advancedCheckBox.isSelected());

      List<String> languageValues = new ArrayList<>(languageVocabulary.keySet());
      List<String> languageComments = new ArrayList<>(languageVocabulary.values());
      languageField = new AutoSuggestField(10, languageValues, languageComments, false);

      List<String> softwareValues = new ArrayList<>(softwareVocabulary.keySet());
      List<String> softwareComments = new ArrayList<>(softwareVocabulary.values());
      softwareField = new AutoSuggestField(10, softwareValues, softwareComments, false);

      List<String> languageWrittenInValues = new ArrayList<>(languageWrittenInVocabulary.keySet());
      List<String> languageWrittenInComments =
          new ArrayList<>(languageWrittenInVocabulary.values());
      languageWrittenInField =
          new AutoSuggestField(10, languageWrittenInValues, languageWrittenInComments, false);

      List<String> statusValues = new ArrayList<>(statusVocabulary.keySet());
      List<String> statusComments = new ArrayList<>(statusVocabulary.values());
      statusField = new AutoSuggestField(10, statusValues, statusComments, false);

      objectiveField = new FTextArea();
      descriptionField = new FTextArea();

      createUI();
    }

    private void createUI() {

      // Create labels
      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "GeneralInformationPanel_";

      FLabel studyNameLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "studyName");
      FLabel identifierLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "identifier");
      FLabel creationDateLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "creationDate");
      FLabel rightsLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "rights");
      FLabel availabilityLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "availability");
      FLabel sourceLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "source");
      FLabel urlLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "url");
      FLabel formatLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "format");
      FLabel languageLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "language");
      FLabel softwareLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "software");
      FLabel languageWrittenInLabel =
          GUIFactory.createLabelWithToolTip(bundle, prefix + "languageWrittenIn");
      FLabel statusLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "status");

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
        List<FLabel> labels =
            Arrays.asList(GUIFactory.createLabelWithToolTip(bundle, prefix + "objective"),
                GUIFactory.createLabelWithToolTip(bundle, prefix + "description"));
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

        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Name())) {
          studyNameField.setText(generalInformation.getName());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Source())) {
          sourceField.setText(generalInformation.getSource());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Identifier())) {
          identifierField.setText(generalInformation.getIdentifier());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Creators())) {
          creatorPanel.init(generalInformation.getCreators());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_CreationDate())) {
          creationField.setDate(generalInformation.getCreationDate());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Rights())) {
          rightsField.setSelectedItem(generalInformation.getRights());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Available())) {
          availabilityField.setSelected(generalInformation.isAvailable());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Format())) {
          formatField.setSelectedItem(generalInformation.getFormat());
        }

        referencePanel.init(generalInformation.getReference());

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Language())) {
          languageField.setSelectedItem(generalInformation.getLanguage());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Software())) {
          softwareField.setSelectedItem(generalInformation.getSoftware());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_LanguageWrittenIn())) {
          languageWrittenInField.setSelectedItem(generalInformation.getLanguageWrittenIn());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Status())) {
          statusField.setSelectedItem(generalInformation.getStatus());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Objective())) {
          objectiveField.setText(generalInformation.getObjective());
        }

        if (generalInformation.eIsSet(pkg.getGeneralInformation_Description())) {
          descriptionField.setText(generalInformation.getDescription());
        }
      }
    }

    GeneralInformation get() {

      final GeneralInformation generalInformation =
          MetadataFactory.eINSTANCE.createGeneralInformation();
      generalInformation.setName(studyNameField.getText());
      generalInformation.setSource(sourceField.getText());
      generalInformation.setIdentifier(identifierField.getText());
      generalInformation.setCreationDate(creationField.getDate());
      generalInformation.setRights((String) rightsField.getSelectedItem());
      generalInformation.setAvailable(availabilityField.isSelected());
      generalInformation.getCreators().addAll(creatorPanel.tableModel.contacts);

      if (formatField.getSelectedIndex() != -1) {
        generalInformation.setFormat((String) formatField.getSelectedItem());
      }

      generalInformation.getReference().addAll(referencePanel.tableModel.references);

      if (languageField.getSelectedIndex() != -1) {
        generalInformation.setLanguage((String) languageField.getSelectedItem());
      }

      if (softwareField.getSelectedIndex() != -1) {
        generalInformation.setSoftware((String) softwareField.getSelectedItem());
      }

      if (languageWrittenInField.getSelectedIndex() != -1) {
        generalInformation.setLanguageWrittenIn((String) languageWrittenInField.getSelectedItem());
      }

      if (statusField.getSelectedIndex() != -1) {
        generalInformation.setStatus((String) statusField.getSelectedItem());
      }

      generalInformation.setObjective(objectiveField.getText());
      generalInformation.setDescription(descriptionField.getText());

      return generalInformation;
    }
  }

  private class ReferencePanel extends FPanel {

    private static final long serialVersionUID = 7457092378015891750L;

    boolean isAdvanced;

    ReferenceTableModel tableModel = new ReferenceTableModel();

    // Non modifiable table model with headers
    class ReferenceTableModel extends DefaultTableModel {

      private static final long serialVersionUID = -3034772220080396221L;
      final ArrayList<Reference> references = new ArrayList<>();

      ReferenceTableModel() {
        super(new Object[0][0], new String[] {"DOI", "Publication title"});
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

      void add(final Reference reference) {

        // Skip if record is already in the table
        for (final Reference currentReference : references) {
          if (currentReference.equals(reference)) {
            return;
          }
        }
        references.add(reference);

        addRow(new String[] {reference.getDoi(), reference.getPublicationTitle()});
      }

      void modify(final int rowNumber, final Reference newReference) {
        references.set(rowNumber, newReference);

        setValueAt(newReference.getDoi(), rowNumber, 0);
        setValueAt(newReference.getPublicationTitle(), rowNumber, 1);
      }

      void remove(final int rowNumber) {
        references.remove(rowNumber);
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
          final Reference newReference = editPanel.get();
          tableModel.add(newReference);
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
            // Import RIS reference items
            List<Record> importedRecords = JRis.parse(fc.getSelectedFile());
            // Convert to Reference
            List<Reference> references =
                importedRecords.stream().map(RakipUtil::convert).collect(Collectors.toList());
            // Add converted references
            references.forEach(tableModel::add);
          } catch (final IOException | JRisException exception) {
            LOGGER.warn("Error importing RIS references", exception);
          }
        }
      });

      final JButton editButton = UIUtils.createEditButton();
      editButton.addActionListener(event -> {

        final int rowToEdit = coolTable.getSelectedRow();
        if (rowToEdit != -1) {

          final Reference ref = tableModel.references.get(rowToEdit);

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

    void init(final List<Reference> references) {
      references.forEach(tableModel::add);
    }
  }

  private class CreatorPanel extends FPanel {

    private static final long serialVersionUID = 3543570665869685092L;
    TableModel tableModel = new TableModel();

    // Non modifiable table model with headers
    class TableModel extends DefaultTableModel {

      private static final long serialVersionUID = -2363056543695517576L;
      final ArrayList<Contact> contacts = new ArrayList<>();

      TableModel() {
        super(new Object[0][0], new String[] {"Given name", "Family name", "Contact"});
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }

      void add(final Contact contact) {

        // Skip if contact is already in table
        for (final Contact currentContact : contacts) {
          if (currentContact.equals(contact)) {
            return;
          }
        }
        contacts.add(contact);

        addRow(new String[] {contact.getGivenName(), contact.getFamilyName(), contact.getEmail()});
      }

      void modify(final int rowNumber, final Contact contact) {
        contacts.set(rowNumber, contact);

        setValueAt(contact.getGivenName(), rowNumber, 0);
        setValueAt(contact.getFamilyName(), rowNumber, 1);
        setValueAt(contact.getEmail(), rowNumber, 2);
      }

      void remove(final int rowNumber) {
        contacts.remove(rowNumber);
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
            // Import VCards from selected file
            List<VCard> vcards = Ezvcard.parse(fileChooser.getSelectedFile()).all();
            // Convert to Contact
            List<Contact> contacts =
                vcards.stream().map(RakipUtil::convert).collect(Collectors.toList());
            // Add converted contacts
            contacts.forEach(tableModel::add);
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
          final Contact newContact = editPanel.get();
          tableModel.add(newContact);
        }
      });

      final JButton editButton = UIUtils.createEditButton();
      editButton.addActionListener(event -> {

        final int rowToEdit = myTable.getSelectedRow();
        if (rowToEdit != -1) {

          final Contact creator = tableModel.contacts.get(rowToEdit);

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

    void init(final List<Contact> contacts) {
      tableModel.contacts.clear();
      tableModel.setRowCount(0);
      contacts.forEach(tableModel::add);
    }
  }

  private class EditCreatorPanel extends EditPanel<Contact> {

    private static final long serialVersionUID = 3472281253338213542L;

    private final FTextField givenNameField; // optional
    private final FTextField familyNameField; // optional
    private final FTextField contactField; // mandatory

    public EditCreatorPanel() {
      super(new BorderLayout());
      givenNameField = new FTextField(false);
      familyNameField = new FTextField(false);
      contactField = new FTextField(true);

      createUI();
    }

    private void createUI() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditCreatorPanel_";

      // Create labels
      FLabel givenNameLabel = new FLabel(bundle.getString(prefix + "givenNameLabel"));
      FLabel familyNameLabel = new FLabel(bundle.getString(prefix + "familyNameLabel"));
      FLabel contactLabel = new FLabel(bundle.getString(prefix + "contactLabel"));

      final FPanel formPanel =
          UIUtils.createFormPanel(Arrays.asList(givenNameLabel, familyNameLabel, contactLabel),
              Arrays.asList(givenNameField, familyNameField, contactField));
      final FPanel northPanel = UIUtils.createNorthPanel(formPanel);
      add(northPanel);
    }

    void init(final Contact contact) {

      if (contact != null) {
        givenNameField.setText(contact.getGivenName());
        familyNameField.setText(contact.getFamilyName());
        contactField.setText(contact.getEmail());
      }
    }

    @Override
    Contact get() {

      final Contact contact = MetadataFactory.eINSTANCE.createContact();
      contact.setGivenName(givenNameField.getText());
      contact.setFamilyName(familyNameField.getText());
      contact.setEmail(contactField.getText());

      return contact;
    }

    @Override
    List<String> validatePanel() {

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      String prefix = "EditCreatorPanel_";

      final List<String> errors = new ArrayList<>(1);
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
    final AutoSuggestField regionField;
    final AutoSuggestField countryField;

    private Scope scope = null;

    ScopePanel() {

      super(new BorderLayout());

      TreeMap<String, String> regionVocabulary = loadVocabulary("Region.xlsx");
      TreeMap<String, String> countryVocabulary = loadVocabulary("Country.xlsx");

      List<String> regionValues = new ArrayList<>(regionVocabulary.keySet());
      List<String> regionComments = new ArrayList<>(regionVocabulary.values());
      regionField = new AutoSuggestField(10, regionValues, regionComments, false);

      List<String> countryValues = new ArrayList<>(countryVocabulary.keySet());
      List<String> countryComments = new ArrayList<>(countryVocabulary.values());
      countryField = new AutoSuggestField(10, countryValues, countryComments, false);

      final JCheckBox advancedCheckBox = new JCheckBox("Advanced");

      String prefix = "ScopePanel_";

      // Build UI
      productButton.setToolTipText("Click me to add a product");
      productButton.addActionListener(event -> {
        EditProductPanel editProductPanel = new EditProductPanel(advancedCheckBox.isSelected());
        if (scope != null) {
          editProductPanel.init(scope.getProduct().get(0));
        }
        final ValidatableDialog dlg = new ValidatableDialog(editProductPanel, "Create a product");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Product product = editProductPanel.get();
          productButton
              .setText(String.format("%s_%s", product.getProductName(), product.getProductUnit()));
          scope.getProduct().clear();
          scope.getProduct().add(product);
        }
      });

      hazardButton.setToolTipText("Click me to add a hazard");
      hazardButton.addActionListener(event -> {
        EditHazardPanel editHazardPanel = new EditHazardPanel(advancedCheckBox.isSelected());
        if (scope != null) {
          editHazardPanel.init(scope.getHazard().get(0));
        }
        final ValidatableDialog dlg = new ValidatableDialog(editHazardPanel, "Create a hazard");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Hazard hazard = editHazardPanel.get();
          hazardButton
              .setText(String.format("%s_%s", hazard.getHazardName(), hazard.getHazardUnit()));
          scope.getHazard().clear();
          scope.getHazard().add(hazard);
        }
      });
      
      MetadataPackage pkg = MetadataPackage.eINSTANCE;

      populationButton.setToolTipText("Click me to add a Population group");
      populationButton.addActionListener(event -> {
        EditPopulationGroupPanel editPopulationGroupPanel =
            new EditPopulationGroupPanel(advancedCheckBox.isSelected());
        if (scope != null && scope.eIsSet(pkg.getScope_PopulationGroup())) {
          PopulationGroup pg0 = scope.getPopulationGroup().get(0);
          editPopulationGroupPanel.init(pg0);
        }
        final ValidatableDialog dlg =
            new ValidatableDialog(editPopulationGroupPanel, "Create a Population Group");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final PopulationGroup populationGroup = editPopulationGroupPanel.get();
          populationButton.setText(populationGroup.getPopulationName());
          scope.getPopulationGroup().add(populationGroup);
        }
      });

      // Create labels
      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
      final FLabel productLabel = new FLabel(bundle.getString(prefix + "productLabel"));
      final FLabel hazardLabel = new FLabel(bundle.getString(prefix + "hazardLabel"));
      final FLabel populationLabel = new FLabel(bundle.getString(prefix + "populationGroupLabel"));
      final FLabel temporalInformationLabel =
          GUIFactory.createLabelWithToolTip(bundle, prefix + "temporalInformation");
      final FLabel regionLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "region");
      final FLabel countryLabel = GUIFactory.createLabelWithToolTip(bundle, prefix + "country");

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
        FLabel label = GUIFactory.createLabelWithToolTip(bundle, prefix + "comment");
        JScrollPane commentPane = GUIFactory.createScrollPane(commentTextArea);
        northPanel.add(UIUtils.createFormPanel(Arrays.asList(label), Arrays.asList(commentPane)));
      }
      add(northPanel, BorderLayout.NORTH);
    }

    void init(final Scope scope) {
      if (scope != null) {

        this.scope = scope;

        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (scope.eIsSet(pkg.getScope_Product())) {
          Product product = scope.getProduct().get(0);
          if (product.eIsSet(pkg.getProduct_ProductName())
              && product.eIsSet(pkg.getProduct_ProductUnit())) {
            productButton.setText(product.getProductName() + "_" + product.getProductUnit());
          }
        }

        if (scope.eIsSet(pkg.getScope_Hazard())) {
          Hazard hazard = scope.getHazard().get(0);
          if (hazard.eIsSet(pkg.getHazard_HazardName())
              && hazard.eIsSet(pkg.getHazard_HazardUnit())) {
            hazardButton.setText(hazard.getHazardName() + "_" + hazard.getHazardUnit());
          }
        }

        if (scope.eIsSet(pkg.getScope_PopulationGroup())) {
          PopulationGroup pg0 = scope.getPopulationGroup().get(0);
          populationButton.setText(pg0.getPopulationName());
        }

        if (scope.eIsSet(pkg.getScope_TemporalInformation())) {
          try {
            dateChooser
                .setDate(new SimpleDateFormat("yyyy-MM-dd").parse(scope.getTemporalInformation()));
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }

        // TODO: SpatialInformation
        // if (!scope.region.isEmpty()) {
        // regionField.setSelectedItem(scope.region.get(0));
        // }
        //
        // if (!scope.country.isEmpty()) {
        // countryField.setSelectedItem(scope.country.get(0));
        // }
      }
    }

    Scope get() {

      final Scope scope = MetadataFactory.eINSTANCE.createScope();
      scope.getProduct().addAll(this.scope.getProduct());
      scope.getHazard().addAll(this.scope.getHazard());
      scope.getPopulationGroup().addAll(this.scope.getPopulationGroup());

      final Date date = dateChooser.getDate();
      if (date != null) {
        scope.setTemporalInformation(
            new SimpleDateFormat(dateChooser.getDateFormatString()).format(date));
      }

      // TODO: SpatialInformation
      // final Object region = regionField.getSelectedItem();
      // if (region != null) {
      // scope.region.add((String) region);
      // }
      //
      // final Object country = countryField.getSelectedItem();
      // if (country != null) {
      // scope.country.add((String) country);
      // }

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

    private DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();

    DataBackgroundPanel() {

      super(new BorderLayout());

      studyButton = new JButton();
      studyButton.setToolTipText("Click me to add Study");
      studyButton.addActionListener(event -> {
        EditStudyPanel editPanel = new EditStudyPanel(advancedCheckBox.isSelected());
        editPanel.init(dataBackground.getStudy());

        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create Study");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          Study study = editPanel.get();
          // Update button's text
          studyButton.setText(study.getStudyTitle());
          dataBackground.setStudy(study);
        }
      });

      studySampleButton = new JButton();
      studySampleButton.setToolTipText("Click me to add Study Sample");
      studySampleButton.addActionListener(event -> {

        EditStudySamplePanel editStudySamplePanel =
            new EditStudySamplePanel(advancedCheckBox.isSelected());
        editStudySamplePanel.init(dataBackground.getStudysample().get(0));

        final ValidatableDialog dlg =
            new ValidatableDialog(editStudySamplePanel, "Create Study sample");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final StudySample studySample = editStudySamplePanel.get();
          // Update button's text
          studySampleButton.setText(studySample.getSampleName());
          dataBackground.getStudysample().add(studySample);
        }
      });

      dietaryAssessmentMethodButton = new JButton();
      dietaryAssessmentMethodButton.setToolTipText("Click me to add Dietary assessment method");
      dietaryAssessmentMethodButton.addActionListener(event -> {
        EditDietaryAssessmentMethodPanel editPanel =
            new EditDietaryAssessmentMethodPanel(advancedCheckBox.isSelected());
        editPanel.init(dataBackground.getDietaryassessmentmethod().get(0));
        final ValidatableDialog dlg =
            new ValidatableDialog(editPanel, "Create dietary assessment method");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final DietaryAssessmentMethod method = editPanel.get();
          // Update button's text
          dietaryAssessmentMethodButton.setText(method.getCollectionTool());
          dataBackground.getDietaryassessmentmethod().add(method);
        }
      });

      laboratoryButton = new JButton();
      laboratoryButton.setToolTipText("Click me to add Laboratory");
      laboratoryButton.addActionListener(event -> {
        EditLaboratoryPanel editPanel = new EditLaboratoryPanel(advancedCheckBox.isSelected());
        editPanel.init(dataBackground.getLaboratory().get(0));
        ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create laboratory");

        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          Laboratory lab = editPanel.get();
          // Update button's text
          laboratoryButton.setText(lab.getLaboratoryAccreditation().get(0).getValue());
          dataBackground.getLaboratory().add(lab);
        }
      });

      assayButton = new JButton();
      assayButton.setToolTipText("Click me to add Assay");
      assayButton.addActionListener(event -> {
        EditAssayPanel editPanel = new EditAssayPanel(advancedCheckBox.isSelected());
        editPanel.init(dataBackground.getAssay().get(0));
        final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create assay");
        if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
          final Assay assay = editPanel.get();
          // Update button's text
          assayButton.setText(assay.getAssayName());
          dataBackground.getAssay().add(assay);
        }
      });

      ResourceBundle bundle = ResourceBundle.getBundle("EditorNodeBundle", new UTF8Control());
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

      if (dataBackground != null) {
        this.dataBackground = dataBackground;

        MetadataPackage pkg = MetadataPackage.eINSTANCE;

        if (dataBackground.eIsSet(pkg.getDataBackground_Study())
            && dataBackground.getStudy().eIsSet(pkg.getStudy_StudyTitle())) {
          String studyTitle = dataBackground.getStudy().getStudyTitle();
          studyButton.setText(studyTitle);
        }

        if (dataBackground.eIsSet(pkg.getDataBackground_Studysample())
            && dataBackground.getStudysample().get(0).eIsSet(pkg.getStudySample_SampleName())) {
          String sampleName = dataBackground.getStudysample().get(0).getSampleName();
          studySampleButton.setText(sampleName);
        }

        // TODO: laboratory

        if (dataBackground.eIsSet(pkg.getDataBackground_Assay())
            && dataBackground.getAssay().get(0).eIsSet(pkg.getAssay_AssayName())) {
          String assayName = dataBackground.getAssay().get(0).getAssayName();
          assayButton.setText(assayName);
        }
      }
    }

    DataBackground get() {

      final DataBackground dataBackground = MetadataFactory.eINSTANCE.createDataBackground();
      dataBackground.setStudy(this.dataBackground.getStudy());
      dataBackground.getStudysample().addAll(this.dataBackground.getStudysample());
      // TODO: laboratory ...
      dataBackground.getAssay().addAll(this.dataBackground.getAssay());

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

        parametersPanel.init(modelMath.getParameter());

        MetadataPackage pkg = MetadataPackage.eINSTANCE;
        if (modelMath.eIsSet(pkg.getModelMath_QualityMeasures())) {
          String qualityMeasuresString = modelMath.getQualityMeasures().get(0).getValue();

          JsonObject qualityMeasures =
              Json.createReader(new StringReader(qualityMeasuresString)).readObject();

          // Initialize SSE, MSE, R2, RMSE, AIC and BIC
          qualityMeasuresPanel.sseSpinnerModel.setValue(getUncertainty(qualityMeasures, "SSE"));
          qualityMeasuresPanel.mseSpinnerModel.setValue(getUncertainty(qualityMeasures, "MSE"));
          qualityMeasuresPanel.rmseSpinnerModel.setValue(getUncertainty(qualityMeasures, "RMSE"));
          qualityMeasuresPanel.r2SpinnerModel.setValue(getUncertainty(qualityMeasures, "Rsquared"));
          qualityMeasuresPanel.aicSpinnerModel.setValue(getUncertainty(qualityMeasures, "AIC"));
          qualityMeasuresPanel.bicSpinnerModel.setValue(getUncertainty(qualityMeasures, "BIC"));
        }

        // Initialize model equations
        modelEquationsPanel.init(modelMath.getModelEquation());

        // TODO: init fitting procedure
        // TODO: init exposure
        // TODO: init events
      }
    }

    @Override
    ModelMath get() {

      final ModelMath modelMath = MetadataFactory.eINSTANCE.createModelMath();

      // Save parameters
      modelMath.getParameter().addAll(parametersPanel.tableModel.parameters);

      // Save SSE, MSE, R2, RMSE, AIC and BIC
      JsonObject qualityMeasures = Json.createObjectBuilder()
          .add("SSE", qualityMeasuresPanel.sseSpinnerModel.getNumber().doubleValue())
          .add("MSE", qualityMeasuresPanel.mseSpinnerModel.getNumber().doubleValue())
          .add("RMSE", qualityMeasuresPanel.rmseSpinnerModel.getNumber().doubleValue())
          .add("Rsquared", qualityMeasuresPanel.r2SpinnerModel.getNumber().doubleValue())
          .add("AIC", qualityMeasuresPanel.aicSpinnerModel.getNumber().doubleValue())
          .add("BIC", qualityMeasuresPanel.bicSpinnerModel.getNumber().doubleValue()).build();
      modelMath.getQualityMeasures().add(createStringObject(qualityMeasures.toString()));

      // Save model equations
      modelMath.getModelEquation().addAll(modelEquationsPanel.tableModel.equations);

      // TODO: Save fitting procedure
      // TODO: Save exposure
      // TODO: Save events

      return modelMath;
    }

    private double getUncertainty(JsonObject object, String key) {
      try {
        return object.getJsonNumber(key).doubleValue();
      } catch (ClassCastException exception) {
        return 0.0;
      }
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
        EqualityHelper equalHelper = new EqualityHelper();
        for (final Parameter currentParam : parameters) {
          if (equalHelper.equals(currentParam, param)) {
            return;
          }
        }
        parameters.add(param);

        addRow(new String[] {param.getParameterID(), param.getParameterName(),
            param.getParameterUnit(), param.getParameterUnitCategory(),
            param.getParameterDataType().name()});
      }

      void modify(final int rowNumber, final Parameter param) {
        parameters.set(rowNumber, param);

        setValueAt(param.getParameterID(), rowNumber, 0);
        setValueAt(param.getParameterName(), rowNumber, 1);
        setValueAt(param.getParameterUnit(), rowNumber, 2);
        setValueAt(param.getParameterUnitCategory(), rowNumber, 3);
        setValueAt(param.getParameterDataType(), rowNumber, 4);
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

        addRow(new String[] {equation.getModelEquationName(), equation.getModelEquation()});
      }

      void modify(final int rowNumber, final ModelEquation equation) {
        equations.set(rowNumber, equation);

        setValueAt(equation.getModelEquationName(), rowNumber, 0);
        setValueAt(equation.getModelEquation(), rowNumber, 1);
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

  private static StringObject createStringObject(String value) {
    StringObject stringObject = MetadataFactory.eINSTANCE.createStringObject();
    stringObject.setValue(value);

    return stringObject;
  }
}
