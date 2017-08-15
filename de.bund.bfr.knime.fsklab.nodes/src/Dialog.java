import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;
import com.toedter.calendar.JDateChooser;

import de.bund.bfr.knime.fsklab.rakip.Assay;
import de.bund.bfr.knime.fsklab.rakip.DietaryAssessmentMethod;
import de.bund.bfr.knime.fsklab.rakip.Hazard;
import de.bund.bfr.knime.fsklab.rakip.ModelEquation;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.knime.fsklab.rakip.PopulationGroup;
import de.bund.bfr.knime.fsklab.rakip.Product;
import de.bund.bfr.knime.fsklab.rakip.StudySample;
import de.bund.bfr.knime.ui.AutoSuggestField;


class Dialog {

  private static Logger LOGGER = Logger.getAnonymousLogger();

  private static final Map<String, Set<String>> vocabs = new HashMap<>();
  static {

    try (
        final InputStream stream =
            Dialog.class.getResourceAsStream("/FSKLab_Config_Controlled Vocabularies.xlsx");
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

  public Dialog() {
    // TODO Auto-generated constructor stub
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
      LOGGER.warning("Spreadsheet not found: " + sheetname);
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
        LOGGER.warning("Controlled vocabulary " + sheetname + ": wrong value " + cell);
      }
    }

    return vocab;
  }

  // TODO: this should be shared through the plugin (all the FSK nodes)
  private static final ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle");

  private static JLabel createLabel(final String text, final String tooltip) {

    final JLabel label = new JLabel(text);
    label.setToolTipText(tooltip);

    return label;
  }

  private static JLabel createLabel(final String textKey, final String toolTipKey,
      final boolean isMandatory) {

    final String text = bundle.getString(textKey) + (isMandatory ? "*" : "");
    final String toolTip = bundle.getString(toolTipKey);

    return createLabel(text, toolTip);
  }

  private static JPanel createAdvancedPanel(final JCheckBox checkbox) {

    final JPanel panel = new JPanel();
    panel.setBackground(Color.lightGray);
    panel.add(checkbox);

    return panel;
  }

  private static JTextField createTextField() {
    return new JTextField(30);
  }

  private static JTextArea createTextArea() {
    return new JTextArea(5, 30);
  }

  private static void add(final JPanel panel, final JComponent comp, final int gridx,
      final int gridy) {
    add(panel, comp, gridx, gridy, 1, 1);
  }

  private static void add(final JPanel panel, final JComponent comp, final int gridx,
      final int gridy, final int gridwidth) {
    add(panel, comp, gridx, gridy, gridwidth, 1);
  }

  private static void add(final JPanel panel, final JComponent comp, final int gridx,
      final int gridy, final int gridwidth, final int gridheight) {

    final GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = gridx;
    constraints.gridy = gridy;
    constraints.gridwidth = gridwidth;
    constraints.gridheight = gridheight;
    constraints.ipadx = 10;
    constraints.ipady = 10;
    constraints.anchor = GridBagConstraints.LINE_START;

    panel.add(comp, constraints);
  }

  /** Creates a JSpinner with 5 columns. */
  private static JSpinner createSpinner(final AbstractSpinnerModel model) {

    final JSpinner spinner = new JSpinner(model);
    ((DefaultEditor) spinner.getEditor()).getTextField().setColumns(5);

    return spinner;
  }

  /** Creates a SpinnerNumberModel for integers with no limits and initial value 0. */
  private static SpinnerNumberModel createSpinnerIntegerModel() {
    return new SpinnerNumberModel(0, null, null, 1);
  }

  /** Creates a SpinnerNumberModel for real numbers with no limits and initial value. */
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

  private class NonEditableTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 8760456472042745780L;

    NonEditableTableModel() {
      super(new Object[][] {}, new String[] {"header"});
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
      return false;
    }
  }

  private class HeadlessTable extends JTable {

    private static final long serialVersionUID = -8980920067513143776L;
    private final DefaultTableCellRenderer renderer;

    HeadlessTable(final NonEditableTableModel model, final DefaultTableCellRenderer renderer) {

      super(model);
      setTableHeader(null);
      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      this.renderer = renderer;
    }

    @Override
    public DefaultTableCellRenderer getCellRenderer(final int row, final int column) {
      return renderer;
    }

    private class ButtonsPanel extends JPanel {

      private static final long serialVersionUID = 6605670621595008750L;
      final JButton addButton = new JButton("Add");
      final JButton modifyButton = new JButton("Modify");
      final JButton removeButton = new JButton("Remove");

      ButtonsPanel() {
        add(addButton);
        add(modifyButton);
        add(removeButton);
      }
    }
  }

  /**
   * Shows Swing ok/cancel dialog.
   * 
   * @return the selected option. JOptionaPane.OK_OPTION or JOptionaPane.CANCEL_OPTION.
   */
  private static int showConfirmDialog(final JPanel panel, final String title) {
    return JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_OPTION,
        JOptionPane.PLAIN_MESSAGE);
  }

  /** Validatable dialogs and panels. */
  private class ValidatableDialog extends JDialog {

    private static final long serialVersionUID = -6572257674130882251L;
    private final JOptionPane optionPane;

    ValidatableDialog(final ValidatablePanel panel, final String dialogTitle) {
      super((Frame) null, true);

      optionPane =
          new JOptionPane(new JScrollPane(panel), JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_OPTION);
      setTitle(dialogTitle);

      // Handle window closing properly
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

      // TODO: listener
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
      pack();
      setLocationRelativeTo(null); // center dialog
      setVisible(true);
    }
  }

  abstract class ValidatablePanel extends JPanel {

    private static final long serialVersionUID = -314660860010487287L;

    ValidatablePanel() {
      super(new GridBagLayout());
    }

    abstract List<String> validatePanel();
  }

  private abstract class EditPanel<T> extends ValidatablePanel {

    private static final long serialVersionUID = 5109496284766147394L;

    abstract void init(final T t);

    abstract T get();
  }

  /** Panel to edit an assay with only mandatory properties. */
  private class EditAssayPanelSimple extends EditPanel<Assay> {

    private static final long serialVersionUID = 1751937902044407791L;

    private final JTextField nameTextField = createTextField();

    EditAssayPanelSimple() {

      final JLabel nameLabel =
          createLabel("GM.EditAssayPanel.nameLabel", "GM.EditAssayPanel.nameTooltip", true);
      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(nameLabel, nameTextField));

      addGridComponents(this, pairs);
    }

    @Override
    void init(Assay assay) {
      if (assay != null) {
        nameTextField.setText(assay.name);
      }
    }

    @Override
    Assay get() {

      final Assay assay = new Assay();
      assay.name = nameTextField.getText();

      return assay;
    }

    @Override
    List<String> validatePanel() {
      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(nameTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditAssayPanel.nameLabel"));
      }

      return errors;
    }
  }

  /** Panel to edit an assay with all its properties. */
  private class EditAssayPanelAdvanced extends EditPanel<Assay> {

    private static final long serialVersionUID = -1195181696127795655L;
    private final JTextField nameTextField = createTextField();
    private final JTextArea descriptionTextArea = createTextArea();

    EditAssayPanelAdvanced() {

      final JLabel nameLabel =
          createLabel("GM.EditAssayPanel.nameLabel", "GM.EditAssayPanel.nameToolTip", true);
      final JLabel descriptionLabel = createLabel("GM.EditAssayPanel.descriptionLabel",
          "GM.EditAssayPanel.descriptionTooltip", true);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(nameLabel, nameTextField),
              new ImmutablePair<>(descriptionLabel, descriptionTextArea));

      addGridComponents(this, pairs);
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
      if (!hasValidValue(nameTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditAssayPanel.nameLabel"));
      }

      return errors;
    }
  }

  private class EditDietaryAssessmentMethodPanelSimple extends EditPanel<DietaryAssessmentMethod> {

    private static final long serialVersionUID = 4504171711228150568L;
    private final AutoSuggestField dataCollectionToolField = new AutoSuggestField(10);
    private final JTextField nonConsecutiveOneDayTextField = createTextField();

    EditDietaryAssessmentMethodPanelSimple() {

      // Init combo boxes
      dataCollectionToolField.setPossibleValues(vocabs.get("Method. tool to collect data"));

      // Create labels
      final JLabel dataCollectionToolLabel =
          createLabel("dataCollectionToolLabel", "dataCollectionToolTooltip", true);
      final JLabel nonConsecutiveOneDayLabel =
          createLabel("nonConsecutiveOneDayLabel", "nonConsecutiveOneDayTooltip", true);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(dataCollectionToolLabel, dataCollectionToolField),
              new ImmutablePair<>(nonConsecutiveOneDayLabel, nonConsecutiveOneDayTextField));

      addGridComponents(this, pairs);
    }

    @Override
    void init(final DietaryAssessmentMethod method) {
      if (method != null) {
        dataCollectionToolField.setSelectedItem(method.collectionTool);
        nonConsecutiveOneDayTextField
            .setText(Integer.toString(method.numberOfNonConsecutiveOneDay));
      }
    }

    @Override
    DietaryAssessmentMethod get() {

      // TODO: cast temporarily null values to empty string and 0 (SHOULD be validated)
      final DietaryAssessmentMethod method = new DietaryAssessmentMethod();
      method.collectionTool = (String) dataCollectionToolField.getSelectedItem();
      method.numberOfNonConsecutiveOneDay =
          Integer.parseInt(nonConsecutiveOneDayTextField.getText());

      return method;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(dataCollectionToolField)) {
        errors.add("Missing "
            + bundle.getString("GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel"));
      }
      if (!hasValidValue(nonConsecutiveOneDayTextField)) {
        errors.add("Missing "
            + bundle.getString("GM.EditDietaryAssessmetnMethodPanel.nonConsecutiveOneDayLabel"));
      }

      return errors;
    }
  }

  private class EditDietaryAssessmentMethodPanelAdvanced
      extends EditPanel<DietaryAssessmentMethod> {

    private static final long serialVersionUID = -931984426171199928L;
    final AutoSuggestField dataCollectionToolField = new AutoSuggestField(10);
    final JTextField nonConsecutiveOneDayTextField = createTextField();
    final JTextField dietarySoftwareToolTextField = createTextField();
    final JTextField foodItemNumberTextField = createTextField();
    final JTextField recordTypeTextField = createTextField();
    final JComboBox<String> foodDescriptorComboBox = new JComboBox<>();

    EditDietaryAssessmentMethodPanelAdvanced() {

      // init combo boxes
      dataCollectionToolField.setPossibleValues(vocabs.get("Method. tool to collect data"));
      vocabs.get("Food descriptors").forEach(it -> foodDescriptorComboBox.addItem(it));

      final JLabel dataCollectionToolLabel =
          createLabel("GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel",
              "GM.EditDietaryAssessmentMethodPanel.dataCollectionToolTooltip", true);
      final JLabel nonConsecutiveOneDayLabel =
          createLabel("GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDayLabel",
              "GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDayToolTip", true);
      final JLabel dietarySoftwareToolLabel =
          createLabel("GM.EditDietaryAssessmentMethodPanel.dietarySoftwareToolLabel",
              "GM.EditDietaryAssessmentMethodPanel.dietarySoftwareToolTooltip", false);
      final JLabel foodItemNumberLabel =
          createLabel("GM.EditDietaryAssessmentMethodPanel.foodItemNumberLabel",
              "GM.EditDietaryAssessmentMethodPanel.foodItemNumberTooltip", false);
      final JLabel recordTypeLabel =
          createLabel("GM.EditDietaryAssessmentMethodPanel.recordTypeLabel",
              "GM.EditDietaryAssessmentMethodPanel.recordTypeTooltip", false);
      final JLabel foodDescriptionLabel =
          createLabel("GM.EditDietaryAssessmentMethodPanel.foodDescriptionLabel",
              "GM.EditDietaryAssessmentMethodPanel.foodDescriptionTooltip", false);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(dataCollectionToolLabel, dataCollectionToolField),
              new ImmutablePair<>(nonConsecutiveOneDayLabel, nonConsecutiveOneDayTextField),
              new ImmutablePair<>(dietarySoftwareToolLabel, dietarySoftwareToolTextField),
              new ImmutablePair<>(foodItemNumberLabel, foodItemNumberTextField),
              new ImmutablePair<>(recordTypeLabel, recordTypeTextField),
              new ImmutablePair<>(foodDescriptionLabel, foodDescriptorComboBox));

      addGridComponents(this, pairs);
    }

    @Override
    void init(DietaryAssessmentMethod method) {
      if (method != null) {
        dataCollectionToolField.setSelectedItem(method.collectionTool);
        nonConsecutiveOneDayTextField
            .setText(Integer.toString(method.numberOfNonConsecutiveOneDay));
        dietarySoftwareToolTextField.setText(method.softwareTool);
        foodItemNumberTextField.setText(method.numberOfFoodItems.get(0));
        recordTypeTextField.setText(method.recordTypes.get(0));
        foodDescriptorComboBox.setSelectedItem(method.foodDescriptors.get(0));
      }
    }

    @Override
    DietaryAssessmentMethod get() {

      final DietaryAssessmentMethod method = new DietaryAssessmentMethod();
      method.collectionTool = (String) dataCollectionToolField.getSelectedItem();
      method.numberOfNonConsecutiveOneDay =
          Integer.parseInt(nonConsecutiveOneDayTextField.getText());
      method.softwareTool = dietarySoftwareToolTextField.getText();
      if (foodItemNumberTextField.getText() != null) {
        method.numberOfFoodItems.add(foodItemNumberTextField.getText());
      }
      if (recordTypeTextField.getText() != null) {
        method.recordTypes.add(recordTypeTextField.getText());
      }
      for (final Object o : foodDescriptorComboBox.getSelectedObjects()) {
        method.foodDescriptors.add((String) o);
      }

      return method;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(dataCollectionToolField)) {
        errors.add("Missing "
            + bundle.getString("GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel"));
      }
      if (!hasValidValue(nonConsecutiveOneDayTextField)) {
        errors.add("Missing "
            + bundle.getString("GM.EditDietaryAssessmetnMethodPanel.nonConsecutiveOneDayLabel"));
      }

      return errors;
    }
  }

  private class EditHazardPanelSimple extends EditPanel<Hazard> {

    private static final long serialVersionUID = -2648017032463694751L;
    private final AutoSuggestField hazardTypeField = new AutoSuggestField(10);
    private final AutoSuggestField hazardNameField = new AutoSuggestField(10);
    private final AutoSuggestField hazardUnitField = new AutoSuggestField(10);

    EditHazardPanelSimple() {

      // Init combo boxes
      hazardTypeField.setPossibleValues(vocabs.get("Hazard type"));
      hazardNameField.setPossibleValues(vocabs.get("Hazard name"));
      hazardUnitField.setPossibleValues(vocabs.get("Hazard unit"));

      // Create labels
      final JLabel hazardTypeLabel = createLabel("GM.EditHazardPanel.hazardTypeLabel",
          "GM.EditHazardPanel.hazardTypeTooltip", true);
      final JLabel hazardNameLabel = createLabel("GM.EditHazardPanel.hazardNameLabel",
          "GM.EditHazardPanel.hazardNameTooltip", true);
      final JLabel hazardUnitLabel = createLabel("GM.EditHazardPanel.hazardUnitLabel",
          "GM.EditHazardPanel.hazardUnitTooltip", true);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<JLabel, JComponent>(hazardTypeLabel, hazardTypeField),
              new ImmutablePair<JLabel, JComponent>(hazardNameLabel, hazardNameField),
              new ImmutablePair<JLabel, JComponent>(hazardUnitLabel, hazardUnitField));
      addGridComponents(this, pairs);
    }

    @Override
    void init(final Hazard hazard) {
      if (hazard != null) {
        hazardTypeField.setSelectedItem(hazard.hazardType);
        hazardNameField.setSelectedItem(hazard.hazardName);
        hazardUnitField.setSelectedItem(hazard.hazardUnit);
      }
    }

    @Override
    Hazard get() {

      final Hazard hazard = new Hazard();
      hazard.hazardType = (String) hazardTypeField.getSelectedItem();
      hazard.hazardName = (String) hazardNameField.getSelectedItem();
      hazard.hazardUnit = (String) hazardUnitField.getSelectedItem();

      return hazard;
    }

    @Override
    List<String> validatePanel() {
      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(hazardNameField)) {
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
  }

  private class EditHazardPanelAdvanced extends EditPanel<Hazard> {

    private static final long serialVersionUID = -1981279747311233487L;

    private final AutoSuggestField hazardTypeField = new AutoSuggestField(10);
    private final AutoSuggestField hazardNameField = new AutoSuggestField(10);
    private final JTextArea hazardDescriptionTextArea = createTextArea();
    private final AutoSuggestField hazardUnitField = new AutoSuggestField(10);
    private final JTextField adverseEffectTextField = createTextField();
    private final JTextField originTextField = createTextField();
    private final JTextField bmdTextField = createTextField();
    private final JTextField maxResidueLimitTextField = createTextField();
    private final JTextField noObservedAdverseTextField = createTextField();
    private final JTextField acceptableOperatorTextField = createTextField();
    private final JTextField acuteReferenceDoseTextField = createTextField();
    private final JTextField acceptableDailyIntakeTextField = createTextField();
    private final AutoSuggestField indSumField = new AutoSuggestField(10);
    private final JTextField labNameTextField = createTextField();
    private final AutoSuggestField labCountryField = new AutoSuggestField(10);
    private final JTextField detectionLimitTextField = createTextField();
    private final JTextField quantificationLimitTextField = createTextField();
    private final JTextField leftCensoredDataTextField = createTextField();
    private final JTextField contaminationRangeTextField = createTextField();

    EditHazardPanelAdvanced() {

      // Init combo boxes
      hazardTypeField.setPossibleValues(vocabs.get("Hazard type"));
      hazardNameField.setPossibleValues(vocabs.get("Hazard name"));
      hazardUnitField.setPossibleValues(vocabs.get("Hazard unit"));
      indSumField.setPossibleValues(vocabs.get("Hazard ind sum"));
      labCountryField.setPossibleValues(vocabs.get("Laboratory country"));

      // Create labels
      final JLabel hazardTypeLabel = createLabel("GM.EditHazardPanel.hazardTypeLabel",
          "GM.EditHazardPanel.hazardTypeTooltip", true);
      final JLabel hazardNameLabel = createLabel("GM.EditHazardPanel.hazardNameLabel",
          "GM.EditHazardPanel.hazardNameTooltip", true);
      final JLabel hazardDescriptionLabel = createLabel("GM.EditHazardPanel.hazardDescriptionLabel",
          "GM.EditHazardPanel.hazardDescriptionTooltip", false);
      final JLabel hazardUnitLabel = createLabel("GM.EditHazardPanel.hazardUnitLabel",
          "GM.EditHazardPanel.hazardUnitTooltip", true);
      final JLabel adverseEffectLabel = createLabel("GM.EditHazardPanel.adverseEffectLabel",
          "GM.EditHazardPanel.adverseEffectTooltip", false);
      final JLabel originLabel =
          createLabel("GM.EditHazardPanel.originLabel", "GM.EditHazardPanel.originTooltip", false);
      final JLabel bmdLabel =
          createLabel("GM.EditHazardPanel.bmdLabel", "GM.EditHazardPanel.bmdTooltip", false);
      final JLabel maxResidueLimitLabel = createLabel("GM.EditHazardPanel.maxResidueLimitLabel",
          "GM.EditHazardPanel.maxResidueLimitTooltip", false);
      final JLabel noObserveAdverseLabel = createLabel("GM.EditHazardPanel.noObservedAdverseLabel",
          "GM.EditHazardPanel.noObservedAdverseTooltip", false);
      final JLabel acceptableOperatorLabel =
          createLabel("GM.EditHazardPanel.acceptableOperatorLabel",
              "GM.EditHazardPanel.acceptableOperatorTooltip", false);
      final JLabel acuteReferenceDoseLabel =
          createLabel("GM.EditHazardPanel.acuteReferenceDoseLabel",
              "GM.EditHazardPanel.acuteReferenceDoseTooltip", false);
      final JLabel indSumLabel =
          createLabel("GM.EditHazardPanel.indSumLabel", "GM.EditHazardPanel.indSumTooltip", false);
      final JLabel acceptableDailyIntakeLabel =
          createLabel("GM.EditHazardPanel.acceptableDailyIntakeLabel",
              "GM.EditHazardPanel.acceptableDailyIntakeTooltip", false);
      final JLabel labNameLabel = createLabel("GM.EditHazardPanel.labNameLabel",
          "GM.EditHazardPanel.labNameTooltip", false);
      final JLabel labCountryLabel = createLabel("GM.EditHazardPanel.labCountryLabel",
          "GM.EditHazardPanel.labCountryTooltip", false);
      final JLabel detectionLimitLabel = createLabel("GM.EditHazardPanel.detectionLimitLabel",
          "GM.EditHazardPanel.detectionLimitTooltip", false);
      final JLabel quantificationLimitLabel =
          createLabel("GM.EditHazardPanel.quantificationLimitLabel",
              "GM.EditHazardPanel.quantificationLimitTooltip", false);
      final JLabel leftCensoredDataLabel = createLabel("GM.EditHazardPanel.leftCensoredDataLabel",
          "GM.EditHazardPanel.leftCensoredDataTooltip", false);
      final JLabel contaminationRangeLabel =
          createLabel("GM.EditHazardPanel.contaminationRangeLabel",
              "GM.EditHazardPanel.contaminationRangeTooltip", false);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(hazardTypeLabel, hazardTypeField),
              new ImmutablePair<>(hazardNameLabel, hazardNameField),
              new ImmutablePair<>(hazardDescriptionLabel, hazardDescriptionTextArea),
              new ImmutablePair<>(hazardUnitLabel, hazardUnitField),
              new ImmutablePair<>(adverseEffectLabel, adverseEffectTextField),
              new ImmutablePair<>(originLabel, originTextField),
              new ImmutablePair<>(bmdLabel, bmdTextField),
              new ImmutablePair<>(maxResidueLimitLabel, maxResidueLimitTextField),
              new ImmutablePair<>(noObserveAdverseLabel, noObservedAdverseTextField),
              new ImmutablePair<>(acceptableOperatorLabel, acceptableOperatorTextField),
              new ImmutablePair<>(acuteReferenceDoseLabel, acuteReferenceDoseTextField),
              new ImmutablePair<>(indSumLabel, indSumField),
              new ImmutablePair<>(acceptableDailyIntakeLabel, acceptableDailyIntakeTextField),
              new ImmutablePair<>(labNameLabel, labNameTextField),
              new ImmutablePair<>(labCountryLabel, labCountryField),
              new ImmutablePair<>(detectionLimitLabel, detectionLimitTextField),
              new ImmutablePair<>(quantificationLimitLabel, quantificationLimitTextField),
              new ImmutablePair<>(leftCensoredDataLabel, leftCensoredDataTextField),
              new ImmutablePair<>(contaminationRangeLabel, contaminationRangeTextField));
      addGridComponents(this, pairs);
    }

    @Override
    void init(de.bund.bfr.knime.fsklab.rakip.Hazard hazard) {
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
      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(hazardNameField)) {
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
  }

  private class EditModelEquationPanelSimple extends EditPanel<ModelEquation> {

    private static final long serialVersionUID = 142679506574036762L;
    private final JTextField equationNameTextField;
    private final JTextArea scriptTextArea;

    EditModelEquationPanelSimple() {

      equationNameTextField = createTextField();
      scriptTextArea = createTextArea();

      final JLabel equationNameLabel = createLabel("GM.EditModelEquationPanel.nameLabel",
          "GM.EditModelEquationPanel.nameTooltip", true);
      final JLabel scriptLabel = createLabel("GM.EditModelEquationPanel.scriptLabel",
          "GM.EditModelEquationPanel.scriptTooltip", true);

      Dialog.add(this, equationNameLabel, 0, 0);
      Dialog.add(this, equationNameTextField, 0, 1);
      Dialog.add(this, scriptLabel, 1, 0);
      Dialog.add(this, scriptTextArea, 1, 1, 2);
    }

    @Override
    void init(final ModelEquation modelEquation) {
      if (modelEquation != null) {
        equationNameTextField.setText(modelEquation.equationName);
        scriptTextArea.setText(modelEquation.equation);
      }
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(equationNameTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditHazardPanel.nameLabel"));
      }
      if (!hasValidValue(scriptTextArea)) {
        errors.add("Missing " + bundle.getString("GM.EditHazardPanel.scriptLabel"));
      }

      return errors;
    }

    @Override
    ModelEquation get() {

      final ModelEquation modelEquation = new ModelEquation();
      modelEquation.equationName = equationNameTextField.getName();
      modelEquation.equation = scriptTextArea.getText();

      return modelEquation;
    }
  }

  private class EditModelEquationAdvanced extends EditPanel<ModelEquation> {

    private static final long serialVersionUID = 3586499490386620791L;

    private final JTextField equationNameTextField;
    private final JTextField equationClassTextField;
    private final ReferencePanel referencePanel;
    private final JTextArea scriptTextArea;

    EditModelEquationAdvanced(final ModelEquation modelEquation) {

      equationNameTextField = createTextField();
      scriptTextArea = createTextArea();
      referencePanel = new ReferencePanel(
          modelEquation != null ? modelEquation.equationReference : Collections.emptyList());
      scriptTextArea = createTextArea();

      final JLabel equationNameLabel = createLabel("GM.EditModelEquationPanel.nameLabel",
          "GM.EditModelEquationPanel.nameTooltip", true);
      final JLabel equationClassLabel = createLabel("GM.EditModelEquationPanel.classLabel",
          "GM.EditModelEquationPanel.classTooltip", false);
      final JLabel scriptLabel = createLabel("GM.EditModelEquationPanel.scriptLabel",
          "GM.EditModelEquationPanel.scriptTooltip", true);

      Dialog.add(this, equationNameLabel, 0, 0);
      Dialog.add(this, equationNameTextField, 0, 1);
      Dialog.add(this, equationClassLabel, 1, 0);
      Dialog.add(this, equationClassTextField, 1, 1);
      Dialog.add(this, referencePanel, 3, 0);
      Dialog.add(this, scriptLabel, 4, 0);
      Dialog.add(this, scriptTextArea, 4, 1);
    }

    @Override
    void init(final ModelEquation modelEquation) {

      if (modelEquation != null) {
        equationNameTextField.setText(modelEquation.equationName);
        equationClassTextField.setText(modelEquation.equationClass);
        // referencePanel is already initialized on declaration
        scriptTextArea.setText(modelEquation.equation);
      }
    }

    @Override
    List<String> validatePanel() {
      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(equationNameTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditHazardPanel.nameLabel"));
      }
      if (!hasValidValue(scriptTextArea)) {
        errors.add("Missing " + bundle.getString("GM.EditHazardPanel.scriptLabel"));
      }

      return errors;
    }

    @Override
    ModelEquation get() {
      final ModelEquation modelEquation = new ModelEquation();
      modelEquation.equationName = equationNameTextField.getText();
      modelEquation.equation = scriptTextArea.getText();
      modelEquation.equationClass = equationClassTextField.getText();
      referencePanel.refs.forEach(it -> modelEquation.equationReference.add(it));

      return modelEquation;
    }
  }

  private class EditParameterPanelSimple extends EditPanel<Parameter> {

    private final JTextField idTextField = createTextField();
    private final JComboBox<Parameter.Classification> classificationComboBox =
        new JComboBox<>(Parameter.Classification.values());
    private final JTextField nameTextField = createTextField();
    private final AutoSuggestField unitField = new AutoSuggestField(10);
    private final AutoSuggestField unitCategoryField = new AutoSuggestField(10);
    private final AutoSuggestField dataTypeField = new AutoSuggestField(10);

    EditParameterPanelSimple() {

      // init combo boxes
      unitField.setPossibleValues(vocabs.get("Parameter unit"));
      unitCategoryField.setPossibleValues(vocabs.get("Parameter unit category"));
      dataTypeField.setPossibleValues(vocabs.get("Parameter data type"));

      // Build UI
      final JLabel idLabel =
          createLabel("GM.EditParameterPanel.idLabel", "GM.EditParameterPanel.idTooltip", true);
      final JLabel classificationLabel = createLabel("GM.EditParameterPanel.classificationLabel",
          "GM.EditParameterPanel.classificationTooltip", true);
      final JLabel nameLabel =
          createLabel("GM.EditParameterPanel.nameLabel", "GM.EditParameterPanel.nameTooltip", true);
      final JLabel unitLabel =
          createLabel("GM.EditParameterPanel.unitLabel", "GM.EditParameterPanel.unitTooltip", true);
      final JLabel unitCategoryLabel = createLabel("GM.EditParameterPanel.unitCategoryLabel",
          "GM.EditParameterPanel.unitCategoryTooltip", true);
      final JLabel dataTypeLabel = createLabel("GM.EditParameterPanel.dataTypeLabel",
          "GM.EditParameterPanel.dataTypeTooltip", true);

      final List<Pair<JLabel, JComponent>> pairs = Arrays.asList(
          new ImmutablePair<>(idLabel, idTextField),
          new ImmutablePair<>(classificationLabel, classificationComboBox),
          new ImmutablePair<>(nameLabel, nameTextField), new ImmutablePair<>(unitLabel, unitField),
          new ImmutablePair<>(unitCategoryLabel, unitCategoryField),
          new ImmutablePair<>(dataTypeLabel, dataTypeField));
      addGridComponents(this, pairs);
    }

    @Override
    void init(final Parameter t) {

      if (t != null) {
        idTextField.setText(t.id);
        classificationComboBox.setSelectedItem(t.classification);
        nameTextField.setText(t.name);
        unitField.setSelectedItem(t.unit);
        unitCategoryField.setSelectedItem(t.unitCategory);
        dataTypeField.setSelectedItem(t.dataType);
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

      return param;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(idTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.idLabel"));
      }
      if (classificationComboBox.getSelectedIndex() == -1) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.classificationLabel"));
      }
      if (!hasValidValue(nameTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.nameLabel"));
      }
      if (!hasValidValue(unitField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.unitLabel"));
      }
      if (!hasValidValue(unitCategoryField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.unitCategoryLabel"));
      }
      if (!hasValidValue(dataTypeField)) {
        errors.add("Missing " + bundle.getString("GM.EditParametersPanel.dataTypeLabel"));
      }

      return errors;
    }
  }

  private class EditParameterPanelAdvanced extends EditPanel<Parameter> {

    private JTextField idTextField;
    private JComboBox<Parameter.Classification> classificationComboBox;
    private JTextField nameTextField;
    private JTextArea descriptionTextArea;
    private AutoSuggestField typeField;
    private AutoSuggestField unitField;
    private AutoSuggestField unitCategoryField;
    private AutoSuggestField dataTypeField;
    private AutoSuggestField sourceField;
    private AutoSuggestField subjectField;
    private AutoSuggestField distributionField;
    private JTextField valueTextField;
    private JTextField referenceTextField;
    private JTextArea variabilitySubjectTextArea;
    private JTextArea applicabilityTextArea;
    private SpinnerNumberModel errorSpinnerModel;

    public EditParameterPanelAdvanced() {

      idTextField = createTextField();
      classificationComboBox = new JComboBox<>(Parameter.Classification.values());
      nameTextField = createTextField();
      descriptionTextArea = createTextArea();
      typeField = new AutoSuggestField(10);
      unitField = new AutoSuggestField(10);
      dataTypeField = new AutoSuggestField(10);
      sourceField = new AutoSuggestField(10);
      subjectField = new AutoSuggestField(10);
      distributionField = new AutoSuggestField(10);
      valueTextField = createTextField();
      referenceTextField = createTextField();
      variabilitySubjectTextArea = createTextArea();
      applicabilityTextArea = createTextArea();
      errorSpinnerModel = createSpinnerDoubleModel();

      // init combo boxes
      typeField.setPossibleValues(vocabs.get("Parameter type"));
      unitField.setPossibleValues(vocabs.get("Parameter unit"));
      unitCategoryField.setPossibleValues(vocabs.get("Parameter unit category"));
      dataTypeField.setPossibleValues(vocabs.get("Parameter data type"));
      sourceField.setPossibleValues(vocabs.get("Parameter source"));
      subjectField.setPossibleValues(vocabs.get("Parameter subject"));
      distributionField.setPossibleValues(vocabs.get("Parameter distribution"));

      // Build UI
      final JLabel idLabel =
          createLabel("GM.EditParameterPanel.idLabel", "GM.EditParameterPanel.idTooltip", true);
      final JLabel classificationLabel = createLabel("GM.EditParameterPanel.classificationLabel",
          "GM.EditParameterPanel.classificationTooltip", true);
      final JLabel nameLabel = createLabel("GM.EditParameterPanel.parameterNameLabel",
          "GM.EditParameterPanel.parameterNameTooltip", true);
      final JLabel descriptionLabel = createLabel("GM.EditParameterPanel.descriptionLabel",
          "GM.EditParameterPanel.descriptionTooltip", false);
      final JLabel typeLabel = createLabel("GM.EditParameterPanel.typeLabel",
          "GM.EditParameterPanel.typeTooltip", false);
      final JLabel unitLabel =
          createLabel("GM.EditParameterPanel.unitLabel", "GM.EditParameterPanel.unitTooltip", true);
      final JLabel unitCategoryLabel = createLabel("GM.EditParameterPanel.unitCategoryLabel",
          "GM.EditParameterPanel.unitCategoryTooltip", true);
      final JLabel dataTypeLabel = createLabel("GM.EditParameterPanel.dataTypeLabel",
          "GM.EditParameterPanel.dataTypeTooltip", true);
      final JLabel sourceLabel = createLabel("GM.EditParameterPanel.sourceLabel",
          "GM.EditParameterPanel.sourceTooltip", false);
      final JLabel subjectLabel = createLabel("GM.EditParameterPanel.subjectLabel",
          "GM.EditParameterPanel.subjectTooltip", false);
      final JLabel distributionLabel = createLabel("GM.EditParameterPanel.distributionLabel",
          "GM.EditParameterPanel.distributionTooltip", false);
      final JLabel valueLabel = createLabel("GM.EditParameterPanel.valueLabel",
          "GM.EditParameterPanel.valueTooltip", false);
      final JLabel referenceLabel = createLabel("GM.EditParameterPanel.referenceLabel",
          "GM.EditParameterPanel.referenceTooltip", false);
      final JLabel variabilitySubjectLabel =
          createLabel("GM.EditParameterPanel.variabilitySubjectLabel",
              "GM.EditParameterPanel.variabilitySubjectTooltip", false);
      final JLabel applicabilityLabel = createLabel("GM.EditParameterPanel.applicabilityLabel",
          "GM.EditParameterPanel.applicabilityTooltip", false);
      final JLabel errorLabel = createLabel("GM.EditParameterPanel.errorLabel",
          "GM.EditParameterPanel.errorTooltip", false);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(idLabel, idTextField),
              new ImmutablePair<>(classificationLabel, classificationComboBox),
              new ImmutablePair<>(nameLabel, nameTextField),
              new ImmutablePair<>(descriptionLabel, descriptionTextArea),
              new ImmutablePair<>(typeLabel, typeField), new ImmutablePair<>(unitLabel, unitField),
              new ImmutablePair<>(unitCategoryLabel, unitCategoryField),
              new ImmutablePair<>(dataTypeLabel, dataTypeField),
              new ImmutablePair<>(sourceLabel, sourceField),
              new ImmutablePair<>(subjectLabel, subjectField),
              new ImmutablePair<>(distributionLabel, distributionField),
              new ImmutablePair<>(valueLabel, valueTextField),
              new ImmutablePair<>(referenceLabel, referenceTextField),
              new ImmutablePair<>(variabilitySubjectLabel, variabilitySubjectTextArea),
              new ImmutablePair<>(applicabilityLabel, applicabilityTextArea),
              new ImmutablePair<>(errorLabel, createSpinner(errorSpinnerModel)));
      addGridComponents(this, pairs);
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

      final List<String> errors = new ArrayList<>();
      if (!hasValidValue(idTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.idLabel"));
      }
      if (classificationComboBox.getSelectedIndex() == -1) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.classificationLabel"));
      }
      if (!hasValidValue(nameTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.nameLabel"));
      }
      if (!hasValidValue(unitField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.unitLabel"));
      }
      if (!hasValidValue(unitCategoryField)) {
        errors.add("Missing " + bundle.getString("GM.EditParameterPanel.unitCategoryLabel"));
      }
      if (!hasValidValue(dataTypeField)) {
        errors.add("Missing " + bundle.getString("GM.EditParametersPanel.dataTypeLabel"));
      }

      return errors;
    }
  }

  class EditPopulationGroupPanelSimple extends EditPanel<PopulationGroup> {

    private static final long serialVersionUID = -5742413648031612767L;

    private final JTextField populationNameTextField = new JTextField(30);

    EditPopulationGroupPanelSimple() {
      final JLabel populationNameLabel =
          createLabel("GM.EditPopulationGroupPanel.populationNameLabel",
              "GM.EditPopulationGroupPanel.populationNameTooltip", true);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(populationNameLabel, populationNameTextField));
      addGridComponents(this, pairs);
    }

    @Override
    void init(final PopulationGroup t) {
      if (t != null) {
        populationNameTextField.setText(t.populationName);
      }
    }

    @Override
    PopulationGroup get() {
      final PopulationGroup populationGroup = new PopulationGroup();
      populationGroup.populationName = populationNameTextField.getText();

      return populationGroup;
    }

    @Override
    List<String> validatePanel() {
      final List<String> errors = new ArrayList<>(1);
      if (!hasValidValue(populationNameTextField)) {
        errors
            .add("Missing " + bundle.getString("GM.EditPopulationGroupPanel.populationNameLabel"));
      }
      return errors;
    }
  }

  class EditPopulationGroupPanelAdvanced extends EditPanel<PopulationGroup> {

    private static final long serialVersionUID = -4520186348489618333L;

    private final JTextField populationNameTextField;
    private final JTextField targetPopulationTextField;
    private final JTextField populationSpanTextField;
    private final JTextArea populationDescriptionTextArea;
    private final JTextField populationAgeTextField;
    private final JTextField populationGenderTextField;
    private final JTextField bmiTextField;
    private final JTextField specialDietGroupTextField;
    private final JTextField patternConsumptionTextField;
    private final JComboBox<String> regionComboBox;
    private final JComboBox<String> countryComboBox;
    private final JTextField riskTextField;
    private final JTextField seasonTextField;

    public EditPopulationGroupPanelAdvanced() {

      populationNameTextField = createTextField();
      targetPopulationTextField = createTextField();
      populationSpanTextField = createTextField();
      populationDescriptionTextArea = createTextArea();
      populationAgeTextField = createTextField();
      populationGenderTextField = createTextField();
      bmiTextField = createTextField();
      specialDietGroupTextField = createTextField();
      patternConsumptionTextField = createTextField();
      regionComboBox = new JComboBox<>();
      countryComboBox = new JComboBox<>();
      riskTextField = createTextField();
      seasonTextField = createTextField();

      // init combo boxes
      vocabs.get("Region").forEach(it -> regionComboBox.addItem(it));
      vocabs.get("Country").forEach(it -> countryComboBox.addItem(it));

      final JLabel populationNameLabel =
          createLabel("GM.EditPopulationGroupPanel.populationNameLabel",
              "GM.EditPopulationGroupPanel.populationNameTooltip", true);
      final JLabel targetPopulationLabel =
          createLabel("GM.EditPopulationGroupPanel.targetPopulationLabel",
              "GM.EditPopulationGroupPanel.targetPopulationTooltip", false);
      final JLabel populationSpanLabel =
          createLabel("GM.EditPopulationGroupPanel.populationSpanLabel",
              "GM.EditPopulationGroupPanel.populationSpanTooltip", false);
      final JLabel populationDescriptionLabel =
          createLabel("GM.EditPopulationGroupPanel.populationDescriptionLabel",
              "GM.EditPopulationGroupPanel.populationDescriptionTooltip", false);
      final JLabel populationAgeLabel =
          createLabel("GM.EditPopulationGroupPanel.populationAgeLabel",
              "GM.EditPopulationGroupPanel.populationAgeTooltip");
      final JLabel populationGenderLabel =
          createLabel("GM.EditPopulationGroupPanel.populationGenderLabel",
              "GM.EditPopulationGroupPanel.populationGenderTooltip");
      final JLabel bmiLabel = createLabel("GM.EditPopulationGroupPanel.bmiLabel",
          "GM.EditPopulationGroupPanel.bmiTooltip", false);
      final JLabel specialDietGroupLabel =
          createLabel("GM.EditPopulationGroupPanel.specialDietGroupsLabel",
              "GM.EditPopulationGroupPanel.specialDietGroupsTooltip", false);
      final JLabel patternConsumptionLabel =
          createLabel("GM.EditPopulationGroupPanel.patternConsumptionLabel",
              "GM.EditPopulationGroupPanel.patternConsumptionTooltip", false);
      final JLabel regionLabel = createLabel("GM.EditPopulationGroupPanel.regionLabel",
          "GM.EditPopulationGroupPanel.regionTooltip", false);
      final JLabel countryLabel = createLabel("GM.EditPopulationGroupPanel.countryLabel",
          "GM.EditPopulationGroupPanel.countryTooltip", false);
      final JLabel riskLabel = createLabel("GM.EditPopulationGroupPanel.riskLabel",
          "GM.EditPopulationGroupPanel.riskTooltip", false);
      final JLabel seasonLabel = createLabel("GM.EditPopulationGroupPanel.seasonLabel",
          "GM.EditPopulationGroupPanel.seasonTooltip", false);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(populationNameLabel, populationNameTextField),
              new ImmutablePair<>(targetPopulationLabel, targetPopulationTextField),
              new ImmutablePair<>(populationSpanLabel, populationSpanTextField),
              new ImmutablePair<>(populationDescriptionLabel, populationDescriptionTextArea),
              new ImmutablePair<>(populationAgeLabel, populationAgeTextField),
              new ImmutablePair<>(populationGenderLabel, populationGenderTextField),
              new ImmutablePair<>(bmiLabel, bmiTextField),
              new ImmutablePair<>(specialDietGroupLabel, specialDietGroupTextField),
              new ImmutablePair<>(patternConsumptionLabel, patternConsumptionTextField),
              new ImmutablePair<>(regionLabel, regionComboBox),
              new ImmutablePair<>(countryLabel, countryComboBox),
              new ImmutablePair<>(riskLabel, riskTextField),
              new ImmutablePair<>(seasonLabel, seasonTextField));
      addGridComponents(this, pairs);
    }

    @Override
    void init(final PopulationGroup t) {
      if (t != null) {
        populationNameTextField.setText(t.populationName);
        targetPopulationTextField.setText(t.targetPopulation);
        populationSpanTextField.setText(t.populationSpan.get(0));
        populationDescriptionTextArea.setText(t.populationDescription.get(0));
        populationAgeTextField.setText(t.populationAge.get(0));
        populationGenderTextField.setText(t.populationGender);
        bmiTextField.setText(t.bmi.get(0));
        specialDietGroupTextField.setText(t.specialDietGroups.get(0));
        patternConsumptionTextField.setText(t.patternConsumption.get(0));
        regionComboBox.setSelectedItem(t.region);
        countryComboBox.setSelectedItem(t.country);
        riskTextField.setText(t.populationRiskFactor.get(0));
        seasonTextField.setText(t.season.get(0));
      }
    }

    @Override
    PopulationGroup get() {
      final PopulationGroup populationGroup = new PopulationGroup();
      populationGroup.populationName = populationNameTextField.getText();
      populationGroup.targetPopulation = targetPopulationTextField.getText();
      populationGroup.populationSpan.add(populationSpanTextField.getText());
      populationGroup.populationDescription.add(populationDescriptionTextArea.getText());
      populationGroup.populationAge.add(populationAgeTextField.getText());
      populationGroup.populationGender = populationGenderTextField.getText();
      populationGroup.bmi.add(bmiTextField.getText());
      populationGroup.specialDietGroups.add(specialDietGroupTextField.getText());
      populationGroup.patternConsumption.add(patternConsumptionTextField.getText());
      populationGroup.region.add((String) regionComboBox.getSelectedItem());
      populationGroup.country.add((String) countryComboBox.getSelectedItem());
      populationGroup.populationRiskFactor.add(riskTextField.getText());
      populationGroup.season.add(seasonTextField.getText());

      return populationGroup;
    }

    @Override
    List<String> validatePanel() {
      final List<String> errors = new ArrayList<>(1);
      if (!hasValidValue(populationNameTextField)) {
        errors
            .add("Missing " + bundle.getString("GM.EditPopulationGroupPanel.populationNameLabel"));
      }
      return errors;
    }
  }

  private class EditProductPanelSimple extends EditPanel<Product> {

    private final AutoSuggestField envNameField = new AutoSuggestField(10);
    private final AutoSuggestField envUnitField = new AutoSuggestField(10);

    EditProductPanelSimple() {

      // init combo boxes
      envNameField.setPossibleValues(vocabs.get("Product-matrix name"));
      envUnitField.setPossibleValues(vocabs.get("Product-matrix unit"));

      final JLabel envNameLabel =
          createLabel("GM.EditProductPanel.envName", "GM.EditProductPanel.envNameTooltip", true);
      final JLabel envUnitLabel =
          createLabel("GM.EditProductPanel.envUnit", "GM.EditProductPanel.envUnitTooltip", true);

      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(envNameLabel, envNameField),
              new ImmutablePair<>(envUnitLabel, envUnitField));
      addGridComponents(this, pairs);
    }

    @Override
    void init(final Product t) {
      if (t != null) {
        envNameField.setSelectedItem(t.environmentName);
        envUnitField.setSelectedItem(t.environmentUnit);
      }
    }

    @Override
    Product get() {

      final Product product = new Product();
      product.environmentName = (String) envNameField.getSelectedItem();
      product.environmentUnit = (String) envUnitField.getSelectedItem();

      return product;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(envNameField)) {
        errors.add("Missing " + bundle.getString("GM.EditProductPanel.envName"));
      }
      if (!hasValidValue(envUnitField)) {
        errors.add("Missing " + bundle.getString("GM.EditProductPanel.envUnit"));
      }

      return errors;
    }
  }

  private class EditProductPanelAdvanced extends EditPanel<Product> {

    private final AutoSuggestField envNameField;
    private final JTextArea envDescriptionTextArea;
    private final AutoSuggestField envUnitField;
    private final JComboBox<String> productionMethodComboBox;
    private final JComboBox<String> packagingComboBox;
    private final JComboBox<String> productTreatmentComboBox;
    private final AutoSuggestField originCountryField;
    private final AutoSuggestField originAreaField;
    private final AutoSuggestField fisheriesAreaField;
    private final FixedDateChooser productionDateChooser;
    private final FixedDateChooser expirationDateChooser;

    public EditProductPanelAdvanced() {

      // Create fields
      envNameField = new AutoSuggestField(10);
      envDescriptionTextArea = createTextArea();
      envUnitField = new AutoSuggestField(10);
      productionMethodComboBox = new JComboBox<>();
      packagingComboBox = new JComboBox<>();
      productTreatmentComboBox = new JComboBox<>();
      originCountryField = new AutoSuggestField(10);
      originAreaField = new AutoSuggestField(10);
      fisheriesAreaField = new AutoSuggestField(10);
      productionDateChooser = new FixedDateChooser();
      expirationDateChooser = new FixedDateChooser();

      // Init combo boxes
      envNameField.setPossibleValues(vocabs.get("Product-matrix name"));
      envUnitField.setPossibleValues(vocabs.get("Product-matrix unit"));
      vocabs.get("Method of production").forEach(it -> productionMethodComboBox.addItem(it));
      originCountryField.setPossibleValues(vocabs.get("Country of origin"));
      originAreaField.setPossibleValues(vocabs.get("Area of origin"));
      fisheriesAreaField.setPossibleValues(vocabs.get("Fisheries area"));

      // Create labels
      final JLabel envNameLabel = createLabel("GM.EditProductPanel.envNameLabel",
          "GM.EditProductPanel.envNameTooltip", true);
      final JLabel envDescriptionLabel = createLabel("GM.EditProductPanel.envDescriptionLabel",
          "GM.EditProductPanel.envDescriptionTooltip", false);;
      final JLabel envUnitLabel = createLabel("GM.EditProductPanel.envUnitLabel",
          "GM.EditProductPanel.envUnitTooltip", true);
      final JLabel productionMethodLabel = createLabel("GM.EditProductPanel.productionMethodLabel",
          "GM.EditProductPanel.productionMethodTooltip", false);
      final JLabel packagingLabel =
          createLabel("GM.EditProductPanel.packagingLabel", "GM.EditProductPanel.packagingTooltip");
      final JLabel productTreatmentLabel = createLabel("GM.EditProductPanel.productTreatmentLabel",
          "GM.EditProductPanel.productTreatmentTooltip", false);
      final JLabel originCountryLabel = createLabel("GM.EditProductPanel.originCountryLabel",
          "GM.EditProductPanel.originCountryTooltip", false);
      final JLabel originAreaLabel = createLabel("GM.EditProductPanel.originAreaLabel",
          "GM.EditProductPanel.originAreaTooltip", false);
      final JLabel fisheriesAreaLabel = createLabel("GM.EditProductPanel.fisheriesAreaLabel",
          "GM.EditProductPanel.fisheriesAreaTooltip", false);
      final JLabel productionDateLabel = createLabel("GM.EditProductPanel.productionDateLabel",
          "GM.EditProductPanel.productionDateTooltip", false);
      final JLabel expirationDateLabel = createLabel("GM.EditProductPanel.expirationDateLabel",
          "GM.EditProductPanel.expirationDateTooltip", false);

      // Build UI
      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(envNameLabel, envNameField),
              new ImmutablePair<>(envDescriptionLabel, envDescriptionTextArea),
              new ImmutablePair<>(envUnitLabel, envUnitField),
              new ImmutablePair<>(productionMethodLabel, productionMethodComboBox),
              new ImmutablePair<>(packagingLabel, packagingComboBox),
              new ImmutablePair<>(productTreatmentLabel, productTreatmentComboBox),
              new ImmutablePair<>(originCountryLabel, originCountryField),
              new ImmutablePair<>(originAreaLabel, originAreaField),
              new ImmutablePair<>(fisheriesAreaLabel, fisheriesAreaField),
              new ImmutablePair<>(productionDateLabel, productionDateChooser),
              new ImmutablePair<>(expirationDateLabel, expirationDateChooser));
      addGridComponents(this, pairs);
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

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(envNameField)) {
        errors.add("Missing " + bundle.getString("GM.EditProductPanel.envName"));
      }
      if (!hasValidValue(envUnitField)) {
        errors.add("Missing " + bundle.getString("GM.EditProductPanel.envUnit"));
      }

      return errors;
    }
  }

  private class EditReferencePanelSimple extends EditPanel<Record> {

    private final JCheckBox isReferenceDescriptionCheckBox;
    private final JTextField doiTextField;
    private final JTextField titleTextField;

    EditReferencePanelSimple() {

      // Create fields
      isReferenceDescriptionCheckBox = new JCheckBox("Is reference description *");
      doiTextField = createTextField();
      titleTextField = createTextField();

      // Create labels
      final JLabel doiLabel = new JLabel(bundle.getString("GM.EditReferencePanel.doiLabel") + " *");
      final JLabel titleLabel =
          new JLabel(bundle.getString("GM.EditReferencePanel.titleLabel") + " *");

      // Build UI
      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(doiLabel, doiTextField),
              new ImmutablePair<>(titleLabel, titleTextField));

      Dialog.add(this, isReferenceDescriptionCheckBox, 0, 0);
      for (int index = 0; index < pairs.size(); index++) {
        final Pair<JLabel, JComponent> pair = pairs.get(index);
        final JLabel label = pair.getLeft();
        final JComponent field = pair.getRight();
        label.setLabelFor(field);

        Dialog.add(this, label, index + 1, 0);
        Dialog.add(this, field, index + 1, 1);
      }
    }

    @Override
    void init(Record t) {
      if (t != null) {
        doiTextField.setText(t.getDoi());
        titleTextField.setText(t.getTitle());
      }
    }

    @Override
    Record get() {
      final Record record = new Record();
      record.setDoi(doiTextField.getText());
      record.setTitle(titleTextField.getText());

      return record;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(doiTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditReferencePanel.doiLabel"));
      }
      if (!hasValidValue(titleTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditReferencePanel.titleLabel"));
      }

      return errors;
    }
  }

  private static class EditReferencePanelAdvanced extends EditPanel<Record> {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final JCheckBox isReferenceDescriptionCheckBox;
    private final JComboBox<Type> typeComboBox;
    private final FixedDateChooser dateChooser;
    private final JTextField pmidTextField;
    private final JTextField doiTextField;
    private final JTextField authorListTextField;
    private final JTextField titleTextField;
    private final JTextArea abstractTextArea;
    private final JTextField journalTextField;
    private final SpinnerNumberModel volumeSpinnerModel;
    private final SpinnerNumberModel issueSpinnerModel;
    private final JTextField pageTextField;
    private final JTextField statusTextField;
    private final JTextField websiteTextField;
    private final JTextArea commentTextArea;

    public EditReferencePanelAdvanced() {

      // Create fields
      isReferenceDescriptionCheckBox = new JCheckBox("Is reference description *");
      typeComboBox = new JComboBox<>();
      dateChooser = new FixedDateChooser();
      pmidTextField = createTextField();
      doiTextField = createTextField();
      authorListTextField = createTextField();
      titleTextField = createTextField();
      abstractTextArea = createTextArea();
      journalTextField = createTextField();
      volumeSpinnerModel = createSpinnerIntegerModel();
      issueSpinnerModel = createSpinnerIntegerModel();
      pageTextField = createTextField();
      statusTextField = createTextField();
      websiteTextField = createTextField();
      commentTextArea = createTextArea();

      // Init combo boxes
      Arrays.stream(Type.values()).forEach(typeComboBox::addItem);

      // Create labels
      final JLabel typeLabel = new JLabel(bundle.getString("GM.EditReferencePanel.typeLabel"));
      final JLabel dateLabel = new JLabel(bundle.getString("GM.EditReferencePanel.dateLabel"));
      final JLabel pmidLabel = new JLabel(bundle.getString("GM.EditReferencePanel.pmidLabel"));
      final JLabel doiLabel = new JLabel(bundle.getString("GM.EditReferencePanel.doiLabel"));
      final JLabel authorLabel =
          new JLabel(bundle.getString("GM.EditReferencePanel.authorListLabel"));
      final JLabel titleLabel = new JLabel(bundle.getString("GM.EditReferencePanel.titleLabel"));
      final JLabel abstractLabel =
          new JLabel(bundle.getString("GM.EditReferencePanel.abstractLabel"));
      final JLabel journalLabel =
          new JLabel(bundle.getString("GM.EditReferencePanel.journalLabel"));
      final JLabel volumeLabel = new JLabel(bundle.getString("GM.EditReferencePanel.volumeLabel"));
      final JLabel issueLabel = new JLabel(bundle.getString("GM.EditReferencePanel.issueLabel"));
      final JLabel pageLabel = new JLabel(bundle.getString("GM.EditReferencePanel.pageLabel"));
      final JLabel statusLabel = new JLabel(bundle.getString("GM.EditReferencePanel.statusLabel"));
      final JLabel websiteLabel =
          new JLabel(bundle.getString("GM.EditReferencePanel.websiteLabel"));
      final JLabel commentLabel =
          new JLabel(bundle.getString("GM.EditReferencePanel.commentLabel"));

      // Build UI
      final List<Pair<JLabel, JComponent>> pairs = Arrays.asList(
          new ImmutablePair<>(typeLabel, typeComboBox), new ImmutablePair<>(dateLabel, dateChooser),
          new ImmutablePair<>(pmidLabel, pmidTextField),
          new ImmutablePair<>(doiLabel, doiTextField),
          new ImmutablePair<>(authorLabel, authorListTextField),
          new ImmutablePair<>(titleLabel, titleTextField),
          new ImmutablePair<>(abstractLabel, abstractTextArea),
          new ImmutablePair<>(journalLabel, journalTextField),
          new ImmutablePair<>(volumeLabel, createSpinner(volumeSpinnerModel)),
          new ImmutablePair<>(issueLabel, createSpinner(issueSpinnerModel)),
          new ImmutablePair<>(pageLabel, pageTextField),
          new ImmutablePair<>(statusLabel, statusTextField),
          new ImmutablePair<>(websiteLabel, websiteTextField),
          new ImmutablePair<>(commentLabel, commentTextArea));

      Dialog.add(this, isReferenceDescriptionCheckBox, 0, 0);
      for (int index = 0; index < pairs.size(); index++) {
        final Pair<JLabel, JComponent> pair = pairs.get(index);
        final JLabel label = pair.getLeft();
        final JComponent field = pair.getRight();
        label.setLabelFor(field);

        Dialog.add(this, label, index + 1, 0);
        Dialog.add(this, field, index + 1, 1);
      }
    }

    @Override
    void init(final Record t) {
      if (t != null) {
        typeComboBox.setSelectedItem(t.getType());
        try {
          dateChooser.setDate(dateFormat.parse(t.getDate()));
        } catch (ParseException e) {
          e.printStackTrace();
        }
        doiTextField.setText(t.getDoi());
        authorListTextField.setText(String.join(";", t.getAuthors()));
        titleTextField.setText(t.getTitle());
        abstractTextArea.setText(t.getAbstr());
        journalTextField.setText(t.getSecondaryTitle());

        final String volumeNumber = t.getVolumeNumber();
        if (volumeNumber != null) {
          volumeSpinnerModel.setValue(volumeNumber);
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
      record.setType((Type) typeComboBox.getSelectedItem());
      record.setDate(dateFormat.format(dateChooser.getDate()));
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

      final List<String> errors = new ArrayList<>(2);
      if (!hasValidValue(doiTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditReferencePanel.doiLabel"));
      }
      if (!hasValidValue(titleTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditReferencePanel.titleLabel"));
      }

      return errors;
    }
  }

  // TODO: EditStudySamplePanel
  private class EditStudySamplePanelSimple extends EditPanel<StudySample> {

    private final JTextField sampleNameTextField;
    private final JTextField sampleProtocolTextField;
    private final JTextField samplingPlanTextField;
    private final JTextField samplingWeightTextField;
    private final JTextField samplingSizeTextField;

    public EditStudySamplePanelSimple() {

      // Create fields
      sampleNameTextField = createTextField();
      sampleProtocolTextField = createTextField();
      samplingPlanTextField = createTextField();
      samplingWeightTextField = createTextField();
      samplingSizeTextField = createTextField();

      // Create labels
      final JLabel sampleNameLabel = createLabel("GM.EditStudySamplePanel.sampleNameLabel",
          "GM.EditStudySamplePanel.sampleNameTooltip", true);
      final JLabel sampleProtocolLabel = createLabel("GM.EditStudySamplePanel.sampleProtocolLabel",
          "GM.EditStudySamplePanel.sampleProtocolTooltip", true);
      final JLabel samplingPlanLabel = createLabel("GM.EditStudySamplePanel.samplingPlanLabel",
          "GM.EditStudySamplePanel.samplingPlanTooltip", true);
      final JLabel samplingWeightLabel = createLabel("GM.EditStudySamplePanel.samplingWeightLabel",
          "GM.EditStudySamplePanel.samplingWeightTooltip", true);;
      final JLabel samplingSizeLabel = createLabel("GM.EditStudySamplePanel.samplingSizeLabel",
          "GM.EditStudySamplePanel.samplingSizeTooltip", true);

      // Build UI
      final List<Pair<JLabel, JComponent>> pairs =
          Arrays.asList(new ImmutablePair<>(sampleNameLabel, sampleNameTextField),
              new ImmutablePair<>(sampleProtocolLabel, sampleProtocolTextField),
              new ImmutablePair<>(samplingPlanLabel, samplingPlanTextField),
              new ImmutablePair<>(samplingWeightLabel, samplingWeightTextField),
              new ImmutablePair<>(samplingSizeLabel, samplingSizeTextField));
      addGridComponents(this, pairs);
    }

    @Override
    void init(final StudySample t) {

      if (t != null) {
        sampleNameTextField.setText(t.sample);
        sampleProtocolTextField.setText(t.collectionProtocol);
        samplingPlanTextField.setText(t.samplingPlan);
        samplingWeightTextField.setText(t.samplingWeight);
        samplingSizeTextField.setText(t.samplingSize);
      }
    }

    @Override
    StudySample get() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    List<String> validatePanel() {

      final List<String> errors = new ArrayList<>(5);
      if (!hasValidValue(sampleNameTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.sampleNameLabel"));
      }
      if (!hasValidValue(sampleProtocolTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.sampleProtocolLabel"));
      }
      if (!hasValidValue(samplingPlanTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.samplingPlanLabel"));
      }
      if (!hasValidValue(samplingWeightTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.samplingWeightLabel"));
      }
      if (!hasValidValue(samplingSizeTextField)) {
        errors.add("Missing " + bundle.getString("GM.EditStudySamplePanel.samplingSizeTextField"));
      }

      return errors;
    }
  }

  // Validation methods
  private static boolean hasValidValue(final JTextField textField) {
    return StringUtils.isNotBlank(textField.getText());
  }

  private static boolean hasValidValue(final JTextArea textArea) {
    return StringUtils.isNotBlank(textArea.getText());
  }

  private static boolean hasValidValue(final AutoSuggestField field) {
    final JTextField textField = (JTextField) field.getEditor().getEditorComponent();
    return StringUtils.isNotBlank(textField.getText());
  }

  // other
  private static void addGridComponents(final JPanel panel,
      final List<Pair<JLabel, JComponent>> pairs) {

    final GridBagConstraints labelConstraints = new GridBagConstraints();
    labelConstraints.gridx = 0;
    labelConstraints.ipadx = 10;
    labelConstraints.ipady = 10;
    labelConstraints.anchor = GridBagConstraints.LINE_START;

    final GridBagConstraints fieldConstraints = new GridBagConstraints();
    fieldConstraints.gridx = 1;
    fieldConstraints.ipadx = 10;
    fieldConstraints.ipady = 10;
    fieldConstraints.anchor = GridBagConstraints.LINE_START;

    for (int index = 0; index < pairs.size(); index++) {

      final Pair<JLabel, JComponent> entry = pairs.get(index);
      final JLabel label = entry.getLeft();
      final JComponent field = entry.getRight();
      label.setLabelFor(field);

      labelConstraints.gridy = index;
      panel.add(label, labelConstraints);

      fieldConstraints.gridy = index;
      panel.add(field, fieldConstraints);
    }
  }

  /** Fixes JDateChooser and disables the text field. */
  private class FixedDateChooser extends JDateChooser {

    private static final long serialVersionUID = 2475793638936369100L;

    FixedDateChooser() {

      // Fixes bug AP-5865
      popup.setFocusable(false);

      /*
       * Text field is disabled so that the dates are only chooseable through the calendar widget.
       * Then there is no need to validate the dates.
       */
      dateEditor.setEnabled(true);
    }
  }
}
