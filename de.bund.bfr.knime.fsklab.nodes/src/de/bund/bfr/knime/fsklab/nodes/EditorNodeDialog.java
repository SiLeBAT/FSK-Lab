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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
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
import org.knime.core.util.Pair;
import org.knime.core.util.SimpleFileFilter;

import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;
import com.gmail.gcolaianni5.jris.engine.JRis;
import com.gmail.gcolaianni5.jris.exception.JRisException;

import de.bund.bfr.knime.fsklab.FskPlugin;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.ui.FixedDateChooser;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
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
import de.bund.bfr.knime.fsklab.rakip.StudySample;
import de.bund.bfr.knime.ui.AutoSuggestField;
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

	private EditorNodeSettings settings;

	EditorNodeDialog() {

		/*
		 * Initialize settings (current values are garbage, need to be loaded from
		 * settings/input port).
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
	}

	// --- settings methods ---
	/** Loads settings from input port. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input) throws NotConfigurableException {

		final EditorNodeSettings editorSettings = new EditorNodeSettings();
		try {
			editorSettings.loadSettings(settings);
		} catch (InvalidSettingsException exception) {
			throw new NotConfigurableException("InvalidSettingsException", exception);
		}

		final FskPortObject inObj = (FskPortObject) input[0];

		/*
		 * If input model has not changed (the original scripts stored in settings match
		 * the input model).
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
		}

		updatePanels();
	}

	/** Loads settings from saved settings. */
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
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
		this.settings.modifiedParametersScript = StringUtils.trim(this.settings.modifiedParametersScript);
		this.settings.modifiedVisualizationScript = StringUtils.trim(this.settings.modifiedVisualizationScript);

		this.settings.genericModel.generalInformation = generalInformationPanel.get();
		this.settings.genericModel.scope = scopePanel.get();
		this.settings.genericModel.dataBackground = dataBackgroundPanel.get();
		this.settings.genericModel.modelMath = modelMathPanel.get();

		this.settings.saveSettings(settings);
	}

	private static final Map<String, Set<String>> vocabs = new HashMap<>();
	static {

		try (final InputStream stream = EditorNodeDialog.class
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
					"Accreditation procedure Ass.Tec", "Study Protocol Type", "Study Protocol Parameters Name",
					"Study Protocol Components Type",

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
	 * @return Set with controlled vocabulary. If the sheet is not found returns
	 *         empty set.
	 */
	private static Set<String> readVocabFromSheet(final XSSFWorkbook workbook, final String sheetname) {

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
		 * Create a JLabel with no tooltip, retrieving its text from resource bundle.
		 * This is a convenience method for {@link #createLabel(String, boolean)} where
		 * the property is not mandatory.
		 * 
		 * @param textKey
		 *            Key of the JLabel text in the resource bundle
		 * @return JLabel
		 */
		private static JLabel createLabel(final String textKey) {
			return createLabel(textKey, false);
		}

		/**
		 * Create a JLabel with no tooltip, retrieving its text from resource bundle.
		 * 
		 * @param textKey
		 *            Key of the JLabel text in the resource bundle
		 * @param isMandatory
		 *            Whether the property described by the JLabel is mandatory
		 * @return JLabel
		 */
		private static JLabel createLabel(final String textKey, final boolean isMandatory) {
			final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;
			return new JLabel(bundle.getString(textKey) + (isMandatory ? "*" : ""));
		}

		/**
		 * Create a JLabel retrieving text and tool tip text from resource bundle. This
		 * is a convenience method for {@link #createLabel(String, String, boolean)}
		 * where the property is not mandatory.
		 * 
		 * @param textKey
		 *            Key of the JLabel text in the resource bundle
		 * @param toolTipKey
		 *            Key of the tool tip text in the resource bundle
		 * @return JLabel describing an optional property.
		 */
		private static JLabel createLabel(final String textKey, final String toolTipKey) {
			return createLabel(textKey, toolTipKey, false);
		}

		/**
		 * Create a JLabel retrieving text and tool tip text from resource bundle.
		 * 
		 * @param textKey
		 *            Key of the JLabel text in the resource bundle
		 * @param toolTipKey
		 *            Key of the tool tip text in the resource bundle
		 * @param isMandatory
		 *            Whether the property described by the JLabel is mandatory
		 * @return JLabel
		 */
		private static JLabel createLabel(final String textKey, final String toolTipKey, final boolean isMandatory) {
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

		@Deprecated
		private static JTextField createTextField() {
			return new JTextField(30);
		}

		private static JTextField createTextField2() {
			final JTextField field = new JTextField(30);
			field.setPreferredSize(new Dimension(100, field.getPreferredSize().height));

			return field;
		}

		@Deprecated
		private static JTextArea createTextArea() {
			final JTextArea textArea = new JTextArea(5, 30);
			textArea.setText("");
			textArea.setLineWrap(true); // Wrap long lines
			textArea.setWrapStyleWord(true); // Wrap only at white space

			return textArea;
		}

		private static JTextArea createTextArea2() {
			final JTextArea textArea = new JTextArea(5, 30);
			textArea.setText("");
			textArea.setLineWrap(true); // Wrap long lines
			textArea.setWrapStyleWord(true); // Wrap only at white space
			textArea.setPreferredSize(new Dimension(100, textArea.getPreferredSize().height));

			return textArea;
		}

		/**
		 * @param possibleValues
		 *            Set
		 * @return a JComboBox with the passed possible values
		 */
		private static JComboBox<String> createComboBox(final Collection<String> possibleValues) {
			final String[] array = possibleValues.stream().toArray(String[]::new);
			final JComboBox<String> comboBox = new JComboBox<>(array);
			comboBox.setSelectedIndex(-1);

			return comboBox;
		}

		/**
		 * @param possibleValues
		 *            Set
		 * @return an AutoSuggestField with the passed possible values. The field has 10
		 *         columns.
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
		 * Creates a SpinnerNumberModel for real numbers with no limits and initial
		 * value.
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

	/**
	 * Adds a component to the end of a container. Also notifies the layout manager
	 * to add the component to this container's layout using the specified
	 * constraints. This is a convenience method for
	 * {@link EditorNodeDialog#add(JPanel, JComponent, int, int, int, int)} where
	 * the initial grid width and height are 1.
	 * 
	 * @param panel
	 *            the panel the component is added to
	 * @param comp
	 *            the component to be added
	 * @param gridx
	 *            the initial gridx value
	 * @param gridy
	 *            the initial gridy value
	 * 
	 * @see {@link Container#add(java.awt.Component, java.lang.Object)}
	 */
	private static void add(final JPanel panel, final JComponent comp, final int gridx, final int gridy) {
		add(panel, comp, gridx, gridy, 1, 1);
	}

	/**
	 * Adds a component to the end of a container. Also notifies the layout manager
	 * to add the component to this container's layout using the specified
	 * constraints. This is a convenience method for
	 * {@link EditorNodeDialog#add(JPanel, JComponent, int, int, int, int) where the
	 * initial grid height is 1.
	 * 
	 * @param panel
	 *            the panel the component is added to
	 * @param comp
	 *            the component to be added
	 * @param gridx
	 *            the initial gridx value
	 * @param gridy
	 *            the initial gridy value
	 * @param gridwidth
	 *            the initial grid width
	 * 
	 * @see {@link Container#add(java.awt.Component, java.lang.Object)}
	 */
	private static void add(final JPanel panel, final JComponent comp, final int gridx, final int gridy,
			final int gridwidth) {
		add(panel, comp, gridx, gridy, gridwidth, 1);
	}

	/**
	 * Adds a component to the end of a container. Also notifies the layout manager
	 * to add the component to this container's layout using the specified
	 * constraints.
	 * 
	 * @param panel
	 *            the panel the component is added to
	 * @param comp
	 *            the component to be added
	 * @param gridx
	 *            the initial gridx value
	 * @param gridy
	 *            the initial gridy value
	 * @param gridwidth
	 *            the initial grid width
	 * @param gridheight
	 *            the initial grid height
	 * 
	 * @see {@link Container#add(java.awt.Component, java.lang.Object)}
	 */
	private static void add(final JPanel panel, final JComponent comp, final int gridx, final int gridy,
			final int gridwidth, final int gridheight) {

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

	private class NonEditableTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 8760456472042745780L;

		NonEditableTableModel() {
			super(new Object[][] {}, new String[] { "header" });
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

	/**
	 * Shows Swing ok/cancel dialog.
	 * 
	 * @return the selected option. JOptionaPane.OK_OPTION or
	 *         JOptionaPane.CANCEL_OPTION.
	 */
	private static int showConfirmDialog(final JPanel panel, final String title) {
		return JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
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

				if (isVisible() && e.getSource() == optionPane && e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)
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

		ValidatablePanel() {
			super(new GridBagLayout());
		}

		abstract List<String> validatePanel();
	}

	private abstract class EditPanel<T> extends ValidatablePanel {

		private static final long serialVersionUID = 5109496284766147394L;

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

	private class EditAssayPanel extends EditPanel<Assay> {

		private static final long serialVersionUID = -1195181696127795655L;

		private final JTextField nameTextField = GUIFactory.createTextField();
		private final JTextArea descriptionTextArea = GUIFactory.createTextArea();

		private final List<JComponent> advancedComponents;

		EditAssayPanel(final boolean isAdvanced) {

			// Create labels
			final JLabel nameLabel = GUIFactory.createLabel("GM.EditAssayPanel.nameLabel",
					"GM.EditAssayPanel.nameTooltip", true);
			final JLabel descriptionLabel = GUIFactory.createLabel("GM.EditAssayPanel.descriptionLabel",
					"GM.EditAssayPanel.descriptionTooltip", true);

			// Wrap text area in JScrollPane
			final JScrollPane descriptionPane = new JScrollPane(descriptionTextArea);

			final List<Pair<JLabel, JComponent>> pairs = Arrays.asList(new Pair<>(nameLabel, nameTextField), // name
					new Pair<>(descriptionLabel, descriptionPane)); // description
			addGridComponents(this, pairs);

			advancedComponents = Arrays.asList(descriptionLabel, descriptionPane);

			// If simple mode hide advanced components
			if (!isAdvanced) {
				advancedComponents.forEach(it -> it.setVisible(false));
			}
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
			if (!hasValidValue(nameTextField)) {
				errors.add("Missing " + bundle.getString("GM.EditAssayPanel.nameLabel"));
			}

			return errors;
		}

		@Override
		List<JComponent> getAdvancedComponents() {
			return advancedComponents;
		}
	}

	private class EditDietaryAssessmentMethodPanel extends EditPanel<DietaryAssessmentMethod> {

		private static final long serialVersionUID = -931984426171199928L;

		private final AutoSuggestField dataCollectionToolField = GUIFactory
				.createAutoSuggestField(vocabs.get("Method. tool to collect data"));
		private final JTextField nonConsecutiveOneDayTextField = GUIFactory.createTextField();
		private final JTextField dietarySoftwareToolTextField = GUIFactory.createTextField();
		private final JTextField foodItemNumberTextField = GUIFactory.createTextField();
		private final JTextField recordTypeTextField = GUIFactory.createTextField();
		private final JComboBox<String> foodDescriptionComboBox = GUIFactory
				.createComboBox(vocabs.get("Food descriptors"));
		private final List<JComponent> advancedComponents;

		EditDietaryAssessmentMethodPanel(final boolean isAdvanced) {

			// Create labels
			final JLabel dataCollectionToolLabel = GUIFactory.createLabel(
					"GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel",
					"GM.EditDietaryAssessmentMethodPanel.dataCollectionToolTooltip", true);
			final JLabel nonConsecutiveOneDayLabel = GUIFactory.createLabel(
					"GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDaysLabel",
					"GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDaysTooltip", true);
			final JLabel dietarySoftwareToolLabel = GUIFactory.createLabel(
					"GM.EditDietaryAssessmentMethodPanel.dietarySoftwareToolLabel",
					"GM.EditDietaryAssessmentMethodPanel.dietarySoftwareToolTooltip");
			final JLabel foodItemNumberLabel = GUIFactory.createLabel(
					"GM.EditDietaryAssessmentMethodPanel.foodItemNumberLabel",
					"GM.EditDietaryAssessmentMethodPanel.foodItemNumberTooltip");
			final JLabel recordTypeLabel = GUIFactory.createLabel("GM.EditDietaryAssessmentMethodPanel.recordTypeLabel",
					"GM.EditDietaryAssessmentMethodPanel.recordTypeTooltip");
			final JLabel foodDescriptionLabel = GUIFactory.createLabel(
					"GM.EditDietaryAssessmentMethodPanel.foodDescriptionLabel",
					"GM.EditDietaryAssessmentMethodPanel.foodDescriptionTooltip");

			final List<Pair<JLabel, JComponent>> pairs = Arrays.asList(
					new Pair<>(dataCollectionToolLabel, dataCollectionToolField),
					new Pair<>(nonConsecutiveOneDayLabel, nonConsecutiveOneDayTextField),
					new Pair<>(dietarySoftwareToolLabel, dietarySoftwareToolTextField),
					new Pair<>(foodItemNumberLabel, foodItemNumberTextField),
					new Pair<>(recordTypeLabel, recordTypeTextField),
					new Pair<>(foodDescriptionLabel, foodDescriptionComboBox));
			addGridComponents(this, pairs);

			advancedComponents = Arrays.asList(dietarySoftwareToolLabel, dietarySoftwareToolTextField,
					foodItemNumberLabel, foodItemNumberTextField, recordTypeLabel, recordTypeTextField,
					foodDescriptionLabel, foodDescriptionComboBox);

			// If simple mode hides advanced components
			if (!isAdvanced) {
				advancedComponents.forEach(it -> it.setVisible(false));
			}
		}

		@Override
		void init(DietaryAssessmentMethod method) {
			if (method != null) {
				dataCollectionToolField.setSelectedItem(method.collectionTool);
				nonConsecutiveOneDayTextField.setText(Integer.toString(method.numberOfNonConsecutiveOneDay));
				dietarySoftwareToolTextField.setText(method.softwareTool);

				if (!method.numberOfFoodItems.isEmpty()) {
					foodItemNumberTextField.setText(method.numberOfFoodItems.get(0));
				}

				if (!method.recordTypes.isEmpty()) {
					recordTypeTextField.setText(method.recordTypes.get(0));
				}

				if (!method.foodDescriptors.isEmpty()) {
					foodDescriptionComboBox.setSelectedItem(method.foodDescriptors.get(0));
				}
			}
		}

		@Override
		DietaryAssessmentMethod get() {

			final DietaryAssessmentMethod method = new DietaryAssessmentMethod();
			method.collectionTool = (String) dataCollectionToolField.getSelectedItem();

			final String nonConsecutiveOneDayTextFieldText = nonConsecutiveOneDayTextField.getText();
			if (StringUtils.isNotBlank(nonConsecutiveOneDayTextFieldText)) {
				try {
					method.numberOfNonConsecutiveOneDay = Integer.parseInt(nonConsecutiveOneDayTextFieldText);
				} catch (final NumberFormatException exception) {
					LOGGER.warn("numberOfNonConsecutiveOneDay", exception);
				}
			}

			method.softwareTool = dietarySoftwareToolTextField.getText();

			final String foodItemNumber = foodItemNumberTextField.getText();
			if (!foodItemNumber.isEmpty()) {
				method.numberOfFoodItems.add(foodItemNumber);
			}

			final String recordType = recordTypeTextField.getText();
			if (!recordType.isEmpty()) {
				method.recordTypes.add(recordType);
			}

			for (final Object o : foodDescriptionComboBox.getSelectedObjects()) {
				method.foodDescriptors.add((String) o);
			}

			return method;
		}

		@Override
		List<String> validatePanel() {

			final ResourceBundle bundle = FskPlugin.getDefault().MESSAGES_BUNDLE;

			final List<String> errors = new ArrayList<>(2);
			if (!hasValidValue(dataCollectionToolField)) {
				errors.add(
						"Missing " + bundle.getString("GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel"));
			}
			if (!hasValidValue(nonConsecutiveOneDayTextField)) {
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

	private class EditHazardPanel extends EditPanel<Hazard> {

		private static final long serialVersionUID = -1981279747311233487L;

		private final AutoSuggestField hazardTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard type"));
		private final AutoSuggestField hazardNameField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard name"));
		private final JTextArea hazardDescriptionTextArea = GUIFactory.createTextArea();
		private final AutoSuggestField hazardUnitField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard unit"));
		private final JTextField adverseEffectTextField = GUIFactory.createTextField();
		private final JTextField originTextField = GUIFactory.createTextField();
		private final JTextField bmdTextField = GUIFactory.createTextField();
		private final JTextField maxResidueLimitTextField = GUIFactory.createTextField();
		private final JTextField noObservedAdverseTextField = GUIFactory.createTextField();
		private final JTextField acceptableOperatorTextField = GUIFactory.createTextField();
		private final JTextField acuteReferenceDoseTextField = GUIFactory.createTextField();
		private final JTextField acceptableDailyIntakeTextField = GUIFactory.createTextField();
		private final AutoSuggestField indSumField = GUIFactory.createAutoSuggestField(vocabs.get("Hazard ind sum"));
		private final JTextField labNameTextField = GUIFactory.createTextField();
		private final AutoSuggestField labCountryField = GUIFactory
				.createAutoSuggestField(vocabs.get("Laboratory country"));
		private final JTextField detectionLimitTextField = GUIFactory.createTextField();
		private final JTextField quantificationLimitTextField = GUIFactory.createTextField();
		private final JTextField leftCensoredDataTextField = GUIFactory.createTextField();
		private final JTextField contaminationRangeTextField = GUIFactory.createTextField();

		private final List<JComponent> advancedComponents;

		EditHazardPanel(final boolean isAdvanced) {

			final JLabel hazardTypeLabel = GUIFactory.createLabel("GM.EditHazardPanel.hazardTypeLabel",
					"GM.EditHazardPanel.hazardTypeTooltip", true);
			final JLabel hazardNameLabel = GUIFactory.createLabel("GM.EditHazardPanel.hazardNameLabel",
					"GM.EditHazardPanel.hazardNameTooltip", true);
			final JLabel hazardDescriptionLabel = GUIFactory.createLabel("GM.EditHazardPanel.hazardDescriptionLabel",
					"GM.EditHazardPanel.hazardDescriptionTooltip");
			final JLabel hazardUnitLabel = GUIFactory.createLabel("GM.EditHazardPanel.hazardUnitLabel",
					"GM.EditHazardPanel.hazardUnitTooltip", true);
			final JLabel adverseEffectLabel = GUIFactory.createLabel("GM.EditHazardPanel.adverseEffectLabel",
					"GM.EditHazardPanel.adverseEffectTooltip");
			final JLabel originLabel = GUIFactory.createLabel("GM.EditHazardPanel.originLabel",
					"GM.EditHazardPanel.originTooltip");
			final JLabel bmdLabel = GUIFactory.createLabel("GM.EditHazardPanel.bmdLabel",
					"GM.EditHazardPanel.bmdTooltip");
			final JLabel maxResidueLimitLabel = GUIFactory.createLabel("GM.EditHazardPanel.maxResidueLimitLabel",
					"GM.EditHazardPanel.maxResidueLimitTooltip");
			final JLabel noObservedAdverseLabel = GUIFactory.createLabel("GM.EditHazardPanel.noObservedAdverseLabel",
					"GM.EditHazardPanel.noObservedAdverseTooltip");
			final JLabel acceptableOperatorLabel = GUIFactory.createLabel("GM.EditHazardPanel.acceptableOperatorLabel",
					"GM.EditHazardPanel.acceptableOperatorTooltip");
			final JLabel acuteReferenceDoseLabel = GUIFactory.createLabel("GM.EditHazardPanel.acuteReferenceDoseLabel",
					"GM.EditHazardPanel.acuteReferenceDoseTooltip");
			final JLabel indSumLabel = GUIFactory.createLabel("GM.EditHazardPanel.indSumLabel",
					"GM.EditHazardPanel.indSumTooltip");
			final JLabel acceptableDailyIntakeLabel = GUIFactory.createLabel(
					"GM.EditHazardPanel.acceptableDailyIntakeLabel", "GM.EditHazardPanel.acceptableDailyIntakeTooltip");
			final JLabel labNameLabel = GUIFactory.createLabel("GM.EditHazardPanel.labNameLabel",
					"GM.EditHazardPanel.labNameTooltip");
			final JLabel labCountryLabel = GUIFactory.createLabel("GM.EditHazardPanel.labCountryLabel",
					"GM.EditHazardPanel.labCountryTooltip");
			final JLabel detectionLimitLabel = GUIFactory.createLabel("GM.EditHazardPanel.detectionLimitLabel",
					"GM.EditHazardPanel.detectionLimitTooltip");
			final JLabel quantificationLimitLabel = GUIFactory.createLabel(
					"GM.EditHazardPanel.quantificationLimitLabel", "GM.EditHazardPanel.quantificationLimitTooltip");
			final JLabel leftCensoredDataLabel = GUIFactory.createLabel("GM.EditHazardPanel.leftCensoredDataLabel",
					"GM.EditHazardPanel.leftCensoredDataTooltip");
			final JLabel contaminationRangeLabel = GUIFactory.createLabel("GM.EditHazardPanel.contaminationRangeLabel",
					"GM.EditHazardPanel.contaminationRangeTooltip");

			// Wraps hazardDescriptionTextArea in a JScrollPane
			final JScrollPane hazardDescriptionPanel = new JScrollPane(hazardDescriptionTextArea);
			hazardDescriptionPanel.setBorder(BorderFactory.createTitledBorder(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditHazardPanel.hazardDescriptionLabel")));
			hazardDescriptionPanel.setToolTipText(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditHazardPanel.hazardDescriptionTooltip"));

			// leftPanel
			final JPanel leftPanel = new JPanel(new GridLayout(18, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			leftPanel.add(hazardTypeLabel);
			leftPanel.add(hazardNameLabel);
			leftPanel.add(hazardUnitLabel);
			leftPanel.add(adverseEffectLabel);
			leftPanel.add(originLabel);
			leftPanel.add(bmdLabel);
			leftPanel.add(maxResidueLimitLabel);
			leftPanel.add(noObservedAdverseLabel);
			leftPanel.add(acceptableOperatorLabel);
			leftPanel.add(acuteReferenceDoseLabel);
			leftPanel.add(indSumLabel);
			leftPanel.add(acceptableDailyIntakeLabel);
			leftPanel.add(labNameLabel);
			leftPanel.add(labCountryLabel);
			leftPanel.add(detectionLimitLabel);
			leftPanel.add(quantificationLimitLabel);
			leftPanel.add(leftCensoredDataLabel);
			leftPanel.add(contaminationRangeLabel);

			// rightPanel
			final JPanel rightPanel = new JPanel(new GridLayout(18, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			rightPanel.add(hazardTypeField);
			rightPanel.add(hazardNameField);
			rightPanel.add(hazardUnitField);
			rightPanel.add(adverseEffectTextField);
			rightPanel.add(originTextField);
			rightPanel.add(bmdTextField);
			rightPanel.add(maxResidueLimitTextField);
			rightPanel.add(noObservedAdverseTextField);
			rightPanel.add(acceptableOperatorTextField);
			rightPanel.add(acuteReferenceDoseTextField);
			rightPanel.add(indSumField);
			rightPanel.add(acceptableDailyIntakeTextField);
			rightPanel.add(labNameTextField);
			rightPanel.add(labCountryField);
			rightPanel.add(detectionLimitTextField);
			rightPanel.add(quantificationLimitTextField);
			rightPanel.add(leftCensoredDataTextField);
			rightPanel.add(contaminationRangeTextField);

			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(formPanel);
			northPanel.add(hazardDescriptionPanel);

			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);

			advancedComponents = Arrays.asList(hazardDescriptionLabel, hazardDescriptionPanel, // hazard description
					adverseEffectLabel, adverseEffectTextField, // adverse effect
					originLabel, originTextField, // origin
					bmdLabel, bmdTextField, // benchmark dose
					maxResidueLimitLabel, maxResidueLimitTextField, // maximum residue limit
					acceptableOperatorLabel, acceptableOperatorTextField, // acceptable operator
					noObservedAdverseLabel, noObservedAdverseTextField, // no observed adverse
					acuteReferenceDoseLabel, acuteReferenceDoseTextField, // acute reference dose
					acceptableDailyIntakeLabel, acceptableDailyIntakeTextField, // aceptable daily intake
					indSumLabel, indSumField, // ind sum
					labNameLabel, labNameTextField, // laboratory name
					labCountryLabel, labCountryField, // laboratory country
					detectionLimitLabel, detectionLimitTextField, // detection limit
					quantificationLimitLabel, quantificationLimitTextField, // quantification limit
					leftCensoredDataLabel, leftCensoredDataTextField, // left censored data
					contaminationRangeLabel, contaminationRangeTextField); // contamination range

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

	private class EditModelEquationPanel extends EditPanel<ModelEquation> {

		private static final long serialVersionUID = 3586499490386620791L;

		private final JTextField equationNameTextField = GUIFactory.createTextField();
		private final JTextField equationClassTextField = GUIFactory.createTextField();
		private final ReferencePanel referencePanel;
		private final JTextArea scriptTextArea = GUIFactory.createTextArea();

		private final List<JComponent> advancedComponents;

		EditModelEquationPanel(final boolean isAdvanced) {

			// Create labels
			final JLabel equationNameLabel = GUIFactory.createLabel("GM.EditModelEquationPanel.nameLabel",
					"GM.EditModelEquationPanel.nameTooltip", true);
			final JLabel equationClassLabel = GUIFactory.createLabel("GM.EditModelEquationPanel.classLabel",
					"GM.EditModelEquationPanel.classTooltip");

			referencePanel = new ReferencePanel(isAdvanced);

			final JScrollPane scriptPane = new JScrollPane(scriptTextArea);
			scriptPane.setBorder(BorderFactory.createTitledBorder(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditModelEquationPanel.scriptLabel")));
			scriptPane.setToolTipText(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditModelEquationPanel.scriptTooltip"));

			// leftPanel (labels)
			final JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			leftPanel.add(equationNameLabel);
			leftPanel.add(equationClassLabel);

			// rightPanel (inputs)
			final JPanel rightPanel = new JPanel(new GridLayout(2, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			rightPanel.add(equationNameTextField);
			rightPanel.add(equationClassTextField);

			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(formPanel);
			northPanel.add(referencePanel);
			northPanel.add(scriptPane);

			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);

			advancedComponents = Arrays.asList(equationClassLabel, equationClassTextField, referencePanel);

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
			if (!hasValidValue(equationNameTextField)) {
				errors.add("Missing " + bundle.getString("GM.EditModelEquationPanel.nameLabel"));
			}
			if (!hasValidValue(scriptTextArea)) {
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
			for (int i = 0; i < referencePanel.tableModel.getRowCount(); i++) {
				Record record = (Record) referencePanel.tableModel.getValueAt(i, 0);
				modelEquation.equationReference.add(record);
			}

			return modelEquation;
		}

		@Override
		List<JComponent> getAdvancedComponents() {
			return advancedComponents;
		}
	}

	private class EditParameterPanel extends EditPanel<Parameter> {

		private static final long serialVersionUID = 1826555468897327895L;

		private final JTextField idTextField = GUIFactory.createTextField();
		private final JComboBox<Parameter.Classification> classificationComboBox = new JComboBox<>(
				Parameter.Classification.values());
		private final JTextField nameTextField = GUIFactory.createTextField();
		private final JTextArea descriptionTextArea = GUIFactory.createTextArea();
		private final AutoSuggestField typeField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter type"));
		private final AutoSuggestField unitField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter unit"));
		private final AutoSuggestField unitCategoryField = GUIFactory
				.createAutoSuggestField(vocabs.get("Parameter unit category"));
		private final AutoSuggestField dataTypeField = GUIFactory
				.createAutoSuggestField(vocabs.get("Parameter data type"));
		private final AutoSuggestField sourceField = GUIFactory.createAutoSuggestField(vocabs.get("Parameter source"));
		private final AutoSuggestField subjectField = GUIFactory
				.createAutoSuggestField(vocabs.get("Parameter subject"));
		private final AutoSuggestField distributionField = GUIFactory
				.createAutoSuggestField(vocabs.get("Parameter distribution"));
		private final JTextField valueTextField = GUIFactory.createTextField();
		private final JTextField referenceTextField = GUIFactory.createTextField();
		private final JTextArea variabilitySubjectTextArea = GUIFactory.createTextArea();
		private final JTextArea applicabilityTextArea = GUIFactory.createTextArea();
		private SpinnerNumberModel errorSpinnerModel = GUIFactory.createSpinnerDoubleModel();

		private final List<JComponent> advancedComponents;

		public EditParameterPanel(final boolean isAdvanced) {

			final JLabel idLabel = GUIFactory.createLabel("GM.EditParameterPanel.idLabel",
					"GM.EditParameterPanel.idTooltip", true);
			final JLabel classificationLabel = GUIFactory.createLabel("GM.EditParameterPanel.classificationLabel",
					"GM.EditParameterPanel.classificationTooltip", true);
			final JLabel nameLabel = GUIFactory.createLabel("GM.EditParameterPanel.parameterNameLabel",
					"GM.EditParameterPanel.parameterNameTooltip", true);
			final JLabel typeLabel = GUIFactory.createLabel("GM.EditParameterPanel.typeLabel",
					"GM.EditParameterPanel.typeTooltip");
			final JLabel unitLabel = GUIFactory.createLabel("GM.EditParameterPanel.unitLabel",
					"GM.EditParameterPanel.unitTooltip", true);
			final JLabel unitCategoryLabel = GUIFactory.createLabel("GM.EditParameterPanel.unitCategoryLabel",
					"GM.EditParameterPanel.unitCategoryTooltip", true);
			final JLabel dataTypeLabel = GUIFactory.createLabel("GM.EditParameterPanel.dataTypeLabel",
					"GM.EditParameterPanel.dataTypeTooltip", true);
			final JLabel sourceLabel = GUIFactory.createLabel("GM.EditParameterPanel.sourceLabel",
					"GM.EditParameterPanel.sourceTooltip");
			final JLabel subjectLabel = GUIFactory.createLabel("GM.EditParameterPanel.subjectLabel",
					"GM.EditParameterPanel.subjectTooltip");
			final JLabel distributionLabel = GUIFactory.createLabel("GM.EditParameterPanel.distributionLabel",
					"GM.EditParameterPanel.distributionTooltip");
			final JLabel valueLabel = GUIFactory.createLabel("GM.EditParameterPanel.valueLabel",
					"GM.EditParameterPanel.valueTooltip");
			final JLabel referenceLabel = GUIFactory.createLabel("GM.EditParameterPanel.referenceLabel",
					"GM.EditParameterPanel.referenceTooltip");
			final JLabel errorLabel = GUIFactory.createLabel("GM.EditParameterPanel.errorLabel",
					"GM.EditParameterPanel.errorTooltip");

			// Build UI
			final JScrollPane descriptionPane = new JScrollPane(descriptionTextArea);
			final JScrollPane variabilitySubjectPane = new JScrollPane(variabilitySubjectTextArea);
			final JScrollPane applicabilityPane = new JScrollPane(applicabilityTextArea);
			final JSpinner errorSpinner = GUIFactory.createSpinner(errorSpinnerModel);

			descriptionPane.setBorder(BorderFactory.createTitledBorder(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditParameterPanel.descriptionLabel")));
			descriptionPane.setToolTipText(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditParameterPanel.descriptionTooltip"));

			variabilitySubjectPane.setBorder(BorderFactory.createTitledBorder(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditParameterPanel.variabilitySubjectLabel")));
			variabilitySubjectPane.setToolTipText(FskPlugin.getDefault().MESSAGES_BUNDLE
					.getString("GM.EditParameterPanel.variabilitySubjectTooltip"));

			applicabilityPane.setBorder(BorderFactory.createTitledBorder(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditParameterPanel.applicabilityLabel")));
			applicabilityPane.setToolTipText(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.EditParameterPanel.applicabilityTooltip"));

			// leftPanel (labels)
			final JPanel leftPanel = new JPanel(new GridLayout(13, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			leftPanel.add(idLabel);
			leftPanel.add(classificationLabel);
			leftPanel.add(nameLabel);
			leftPanel.add(typeLabel);
			leftPanel.add(unitLabel);
			leftPanel.add(unitCategoryLabel);
			leftPanel.add(dataTypeLabel);
			leftPanel.add(sourceLabel);
			leftPanel.add(subjectLabel);
			leftPanel.add(distributionLabel);
			leftPanel.add(valueLabel);
			leftPanel.add(referenceLabel);
			leftPanel.add(errorLabel);

			// rightPanel (inputs)
			final JPanel rightPanel = new JPanel(new GridLayout(13, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			rightPanel.add(idTextField);
			rightPanel.add(classificationComboBox);
			rightPanel.add(nameTextField);
			rightPanel.add(typeField);
			rightPanel.add(unitField);
			rightPanel.add(unitCategoryField);
			rightPanel.add(dataTypeField);
			rightPanel.add(sourceField);
			rightPanel.add(subjectField);
			rightPanel.add(distributionField);
			rightPanel.add(valueTextField);
			rightPanel.add(referenceTextField);
			rightPanel.add(errorSpinner);

			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(formPanel);
			northPanel.add(descriptionPane);
			northPanel.add(variabilitySubjectPane);
			northPanel.add(applicabilityPane);

			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);

			advancedComponents = Arrays.asList(descriptionPane, typeLabel, typeField, sourceLabel, sourceField,
					subjectLabel, subjectField, distributionLabel, distributionField, valueLabel, valueTextField,
					referenceLabel, referenceTextField, variabilitySubjectPane, applicabilityPane, errorLabel,
					errorSpinner);

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
			if (!hasValidValue(idTextField)) {
				errors.add("Missing " + bundle.getString("GM.EditParameterPanel.idLabel"));
			}
			if (classificationComboBox.getSelectedIndex() == -1) {
				errors.add("Missing " + bundle.getString("GM.EditParameterPanel.classificationLabel"));
			}
			if (!hasValidValue(nameTextField)) {
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

	private class EditPopulationGroupPanel extends EditPanel<PopulationGroup> {

		private static final long serialVersionUID = -4520186348489618333L;

		private final JTextField populationNameTextField = GUIFactory.createTextField();
		private final JTextField targetPopulationTextField = GUIFactory.createTextField();
		private final JTextField populationSpanTextField = GUIFactory.createTextField();
		private final JTextArea populationDescriptionTextArea = GUIFactory.createTextArea();
		private final JTextField populationAgeTextField = GUIFactory.createTextField();
		private final JTextField populationGenderTextField = GUIFactory.createTextField();
		private final JTextField bmiTextField = GUIFactory.createTextField();
		private final JTextField specialDietGroupTextField = GUIFactory.createTextField();
		private final JTextField patternConsumptionTextField = GUIFactory.createTextField();
		private final JComboBox<String> regionComboBox = GUIFactory.createComboBox(vocabs.get("Region"));
		private final JComboBox<String> countryComboBox = GUIFactory.createComboBox(vocabs.get("Country"));
		private final JTextField riskTextField = GUIFactory.createTextField();
		private final JTextField seasonTextField = GUIFactory.createTextField();

		private final List<JComponent> advancedComponents;

		public EditPopulationGroupPanel(final boolean isAdvanced) {

			final JLabel populationNameLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.populationNameLabel",
					"GM.EditPopulationGroupPanel.populationNameTooltip", true);
			final JLabel targetPopulationLabel = GUIFactory.createLabel(
					"GM.EditPopulationGroupPanel.targetPopulationLabel",
					"GM.EditPopulationGroupPanel.targetPopulationTooltip");
			final JLabel populationSpanLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.populationSpanLabel",
					"GM.EditPopulationGroupPanel.populationSpanTooltip");
			final JLabel populationAgeLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.populationAgeLabel",
					"GM.EditPopulationGroupPanel.populationAgeTooltip");
			final JLabel populationGenderLabel = GUIFactory.createLabel(
					"GM.EditPopulationGroupPanel.populationGenderLabel",
					"GM.EditPopulationGroupPanel.populationGenderTooltip");
			final JLabel bmiLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.bmiLabel",
					"GM.EditPopulationGroupPanel.bmiTooltip");
			final JLabel specialDietGroupLabel = GUIFactory.createLabel(
					"GM.EditPopulationGroupPanel.specialDietGroupsLabel",
					"GM.EditPopulationGroupPanel.specialDietGroupsTooltip");
			final JLabel patternConsumptionLabel = GUIFactory.createLabel(
					"GM.EditPopulationGroupPanel.patternConsumptionLabel",
					"GM.EditPopulationGroupPanel.patternConsumptionTooltip");
			final JLabel regionLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.regionLabel",
					"GM.EditPopulationGroupPanel.regionTooltip");
			final JLabel countryLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.countryLabel",
					"GM.EditPopulationGroupPanel.countryTooltip");
			final JLabel riskLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.riskAndPopulationLabel",
					"GM.EditPopulationGroupPanel.riskAndPopulationTooltip");
			final JLabel seasonLabel = GUIFactory.createLabel("GM.EditPopulationGroupPanel.seasonLabel",
					"GM.EditPopulationGroupPanel.seasonTooltip");

			final JScrollPane populationDescriptionPane = new JScrollPane(populationDescriptionTextArea);
			populationDescriptionPane.setBorder(BorderFactory.createTitledBorder(FskPlugin.getDefault().MESSAGES_BUNDLE
					.getString("GM.EditPopulationGroupPanel.populationDescriptionLabel")));
			populationDescriptionPane.setToolTipText(FskPlugin.getDefault().MESSAGES_BUNDLE
					.getString("GM.EditPopulationGroupPanel.populationDescriptionTooltip"));

			// left panel
			final JPanel leftPanel = new JPanel(new GridLayout(11, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			leftPanel.add(populationNameLabel);
			leftPanel.add(targetPopulationLabel);
			leftPanel.add(populationSpanLabel);
			leftPanel.add(populationAgeLabel);
			leftPanel.add(populationGenderLabel);
			leftPanel.add(bmiLabel);
			leftPanel.add(specialDietGroupLabel);
			leftPanel.add(regionLabel);
			leftPanel.add(countryLabel);
			leftPanel.add(riskLabel);
			leftPanel.add(seasonLabel);

			// right panel
			final JPanel rightPanel = new JPanel(new GridLayout(11, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			rightPanel.add(populationNameTextField);
			rightPanel.add(targetPopulationTextField);
			rightPanel.add(populationSpanTextField);
			rightPanel.add(populationAgeTextField);
			rightPanel.add(populationGenderTextField);
			rightPanel.add(bmiTextField);
			rightPanel.add(specialDietGroupTextField);
			rightPanel.add(regionComboBox);
			rightPanel.add(countryComboBox);
			rightPanel.add(riskTextField);
			rightPanel.add(seasonTextField);

			// form panel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(formPanel);
			northPanel.add(populationDescriptionPane);

			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);

			advancedComponents = Arrays.asList(targetPopulationLabel, targetPopulationTextField, // Target population
					populationSpanLabel, populationSpanTextField, // Population span
					populationDescriptionPane, // Population description
					populationAgeLabel, populationAgeTextField, // Population age
					populationGenderLabel, populationGenderTextField, // Population gender
					bmiLabel, bmiTextField, // BMI
					specialDietGroupLabel, specialDietGroupTextField, // Special diet group
					patternConsumptionLabel, patternConsumptionTextField, // Pattern consumption
					regionLabel, regionComboBox, // Region
					countryLabel, countryComboBox, // Country
					riskLabel, riskTextField, // Risk
					seasonLabel, seasonTextField); // Season

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
			if (!hasValidValue(populationNameTextField)) {
				errors.add("Missing " + bundle.getString("GM.EditPopulationGroupPanel.populationNameLabel"));
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

		private final AutoSuggestField envNameField = GUIFactory
				.createAutoSuggestField(vocabs.get("Product-matrix name"));
		private final JTextArea envDescriptionTextArea = GUIFactory.createTextArea();
		private final AutoSuggestField envUnitField = GUIFactory
				.createAutoSuggestField(vocabs.get("Product-matrix unit"));
		private final JComboBox<String> productionMethodComboBox = GUIFactory
				.createComboBox(vocabs.get("Method of production"));
		private final JComboBox<String> packagingComboBox = GUIFactory.createComboBox(vocabs.get("Packaging"));
		private final JComboBox<String> productTreatmentComboBox = GUIFactory
				.createComboBox(vocabs.get("Product treatment"));
		private final AutoSuggestField originCountryField = GUIFactory
				.createAutoSuggestField(vocabs.get("Country of origin"));
		private final AutoSuggestField originAreaField = GUIFactory
				.createAutoSuggestField(vocabs.get("Area of origin"));
		private final AutoSuggestField fisheriesAreaField = GUIFactory
				.createAutoSuggestField(vocabs.get("Fisheries area"));
		private final FixedDateChooser productionDateChooser = new FixedDateChooser();
		private final FixedDateChooser expirationDateChooser = new FixedDateChooser();

		private final List<JComponent> advancedComponents;

		public EditProductPanel(boolean isAdvanced) {

			final JLabel envNameLabel = GUIFactory.createLabel("GM.EditProductPanel.envNameLabel",
					"GM.EditProductPanel.envNameTooltip", true);
			final JLabel envDescriptionLabel = GUIFactory.createLabel("GM.EditProductPanel.envDescriptionLabel",
					"GM.EditProductPanel.envDescriptionTooltip");
			final JLabel envUnitLabel = GUIFactory.createLabel("GM.EditProductPanel.envUnitLabel",
					"GM.EditProductPanel.envUnitTooltip", true);
			final JLabel productionMethodLabel = GUIFactory.createLabel("GM.EditProductPanel.productionMethodLabel",
					"GM.EditProductPanel.productionMethodTooltip");
			final JLabel packagingLabel = GUIFactory.createLabel("GM.EditProductPanel.packagingLabel",
					"GM.EditProductPanel.packagingTooltip");
			final JLabel productTreatmentLabel = GUIFactory.createLabel("GM.EditProductPanel.productTreatmentLabel",
					"GM.EditProductPanel.productTreatmentTooltip");
			final JLabel originCountryLabel = GUIFactory.createLabel("GM.EditProductPanel.originCountryLabel",
					"GM.EditProductPanel.originCountryTooltip");
			final JLabel originAreaLabel = GUIFactory.createLabel("GM.EditProductPanel.originAreaLabel",
					"GM.EditProductPanel.originAreaTooltip");
			final JLabel fisheriesAreaLabel = GUIFactory.createLabel("GM.EditProductPanel.fisheriesAreaLabel",
					"GM.EditProductPanel.fisheriesAreaTooltip");
			final JLabel productionDateLabel = GUIFactory.createLabel("GM.EditProductPanel.productionDateLabel",
					"GM.EditProductPanel.productionDateTooltip");
			final JLabel expirationDateLabel = GUIFactory.createLabel("GM.EditProductPanel.expirationDateLabel",
					"GM.EditProductPanel.expirationDateTooltip");

			// Build UI
			final JScrollPane envDescriptionPane = new JScrollPane(envDescriptionTextArea);
			envDescriptionPane.setBorder(BorderFactory.createTitledBorder(envDescriptionLabel.getText()));
			envDescriptionPane.setToolTipText(envDescriptionLabel.getToolTipText());

			// leftPanel
			final JPanel leftPanel = new JPanel(new GridLayout(10, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			leftPanel.add(envNameLabel);
			leftPanel.add(envUnitLabel);
			leftPanel.add(productionMethodLabel);
			leftPanel.add(packagingLabel);
			leftPanel.add(productTreatmentLabel);
			leftPanel.add(originCountryLabel);
			leftPanel.add(originAreaLabel);
			leftPanel.add(fisheriesAreaLabel);
			leftPanel.add(productionDateLabel);
			leftPanel.add(expirationDateLabel);

			// rightPanel
			final JPanel rightPanel = new JPanel(new GridLayout(10, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			rightPanel.add(envNameField);
			rightPanel.add(envUnitField);
			rightPanel.add(productionMethodComboBox);
			rightPanel.add(packagingComboBox);
			rightPanel.add(productTreatmentComboBox);
			rightPanel.add(originCountryField);
			rightPanel.add(originAreaField);
			rightPanel.add(fisheriesAreaField);
			rightPanel.add(productionDateChooser);
			rightPanel.add(expirationDateChooser);

			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(formPanel);
			northPanel.add(envDescriptionPane);

			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);

			advancedComponents = Arrays.asList(envDescriptionLabel, envDescriptionPane, productionMethodLabel,
					productionMethodComboBox, packagingLabel, packagingComboBox, productTreatmentLabel,
					productTreatmentComboBox, originCountryLabel, originCountryField, originAreaLabel, originAreaField,
					fisheriesAreaLabel, fisheriesAreaField, productionDateLabel, productionDateChooser,
					expirationDateLabel, expirationDateChooser);

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

	private class EditReferencePanel extends EditPanel<Record> {

		private static final long serialVersionUID = -6874752919377124455L;

		private static final String dateFormatStr = "yyyy-MM-dd";

		private final JCheckBox isReferenceDescriptionCheckBox;
		private final JComboBox<String> typeComboBox = GUIFactory.createComboBox(referenceTypeLabels.values());
		private final FixedDateChooser dateChooser = new FixedDateChooser();
		private final JTextField pmidTextField = GUIFactory.createTextField();
		private final JTextField doiTextField = GUIFactory.createTextField();
		private final JTextField authorListTextField = GUIFactory.createTextField();
		private final JTextField titleTextField = GUIFactory.createTextField();
		private final JTextArea abstractTextArea = GUIFactory.createTextArea();
		private final JTextField journalTextField = GUIFactory.createTextField();
		// Spinner models starting with 0 and taking positive ints only
		private final SpinnerNumberModel volumeSpinnerModel = new SpinnerNumberModel(0, 0, null, 1);
		private final SpinnerNumberModel issueSpinnerModel = new SpinnerNumberModel(0, 0, null, 1);
		private final JTextField pageTextField = GUIFactory.createTextField();
		private final JTextField statusTextField = GUIFactory.createTextField();
		private final JTextField websiteTextField = GUIFactory.createTextField();
		private final JTextArea commentTextArea = GUIFactory.createTextArea();

		private final List<JComponent> advancedComponents;

		EditReferencePanel(final boolean isAdvanced) {

			// Create fields
			isReferenceDescriptionCheckBox = new JCheckBox("Is reference description *");

			// Create labels
			final JLabel typeLabel = GUIFactory.createLabel("GM.EditReferencePanel.typeLabel");
			final JLabel dateLabel = GUIFactory.createLabel("GM.EditReferencePanel.dateLabel");
			final JLabel pmidLabel = GUIFactory.createLabel("GM.EditReferencePanel.pmidLabel");
			final JLabel doiLabel = GUIFactory.createLabel("GM.EditReferencePanel.doiLabel", true);
			final JLabel authorListLabel = GUIFactory.createLabel("GM.EditReferencePanel.authorListLabel");
			final JLabel titleLabel = GUIFactory.createLabel("GM.EditReferencePanel.titleLabel", true);
			final JLabel abstractLabel = GUIFactory.createLabel("GM.EditReferencePanel.abstractLabel");
			final JLabel journalLabel = GUIFactory.createLabel("GM.EditReferencePanel.journalLabel");
			final JLabel volumeLabel = GUIFactory.createLabel("GM.EditReferencePanel.volumeLabel");
			final JLabel issueLabel = GUIFactory.createLabel("GM.EditReferencePanel.issueLabel");
			final JLabel pageLabel = GUIFactory.createLabel("GM.EditReferencePanel.pageLabel");
			final JLabel statusLabel = GUIFactory.createLabel("GM.EditReferencePanel.statusLabel");
			final JLabel websiteLabel = GUIFactory.createLabel("GM.EditReferencePanel.websiteLabel");
			final JLabel commentLabel = GUIFactory.createLabel("GM.EditReferencePanel.commentLabel");

			// Build UI
			final JSpinner volumeSpinner = GUIFactory.createSpinner(volumeSpinnerModel);
			final JSpinner issueSpinner = GUIFactory.createSpinner(issueSpinnerModel);

			// isReferenceDescription panel
			final JPanel isReferenceDescriptionPanel = new JPanel(new BorderLayout());
			isReferenceDescriptionPanel.add(isReferenceDescriptionCheckBox, BorderLayout.WEST);

			// left panel for the form (labels)
			final JPanel leftPanel = new JPanel(new GridLayout(12, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			leftPanel.add(typeLabel);
			leftPanel.add(dateLabel);
			leftPanel.add(pmidLabel);
			leftPanel.add(doiLabel);
			leftPanel.add(authorListLabel);
			leftPanel.add(titleLabel);
			leftPanel.add(journalLabel);
			leftPanel.add(volumeLabel);
			leftPanel.add(issueLabel);
			leftPanel.add(pageLabel);
			leftPanel.add(statusLabel);
			leftPanel.add(websiteLabel);

			// right panel for the form (inputs)
			final JPanel rightPanel = new JPanel(new GridLayout(12, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			rightPanel.add(typeComboBox);
			rightPanel.add(dateChooser);
			rightPanel.add(pmidTextField);
			rightPanel.add(doiTextField);
			rightPanel.add(authorListTextField);
			rightPanel.add(titleTextField);
			rightPanel.add(journalTextField);
			rightPanel.add(volumeSpinner);
			rightPanel.add(issueSpinner);
			rightPanel.add(pageTextField);
			rightPanel.add(statusTextField);
			rightPanel.add(websiteTextField);

			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// left panel for text areas (labels)
			final JPanel taLeftPanel = new JPanel(new GridLayout(2, 1, 5, 5));
			taLeftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			taLeftPanel.add(abstractLabel);
			taLeftPanel.add(commentLabel);

			// right panel for text areas (inputs)
			final JPanel taRightPanel = new JPanel(new GridLayout(2, 1, 5, 5));
			taRightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			taRightPanel.add(abstractTextArea);
			taRightPanel.add(commentTextArea);

			// taPanel: form with text areas
			final JPanel taPanel = new JPanel(new BorderLayout());
			taPanel.add(taLeftPanel, BorderLayout.WEST);
			taPanel.add(taRightPanel, BorderLayout.CENTER);

			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(isReferenceDescriptionPanel);
			northPanel.add(formPanel);
			northPanel.add(taPanel);

			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);

			// If simple mode hide advanced components
			advancedComponents = Arrays.asList(typeLabel, typeComboBox, dateLabel, dateChooser, pmidLabel,
					pmidTextField, authorListLabel, authorListTextField, abstractLabel, abstractTextArea, journalLabel,
					journalTextField, volumeLabel, volumeSpinner, issueLabel, issueSpinner, pageLabel, pageTextField,
					statusLabel, statusTextField, websiteLabel, websiteTextField, commentLabel, commentTextArea);
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
				authorListTextField.setText(String.join(";", t.getAuthors()));
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
			if (!hasValidValue(doiTextField)) {
				errors.add("Missing " + bundle.getString("GM.EditReferencePanel.doiLabel"));
			}
			if (!hasValidValue(titleTextField)) {
				errors.add("Missing " + bundle.getString("GM.EditReferencePanel.titleLabel"));
			}

			return errors;
		}

		@Override
		List<JComponent> getAdvancedComponents() {
			return advancedComponents;
		}
	}

	private class EditStudySamplePanel extends EditPanel<StudySample> {

		private static final long serialVersionUID = -4740851101237646103L;

		private final JTextField sampleNameTextField = GUIFactory.createTextField();
		private final SpinnerNumberModel moisturePercentageSpinnerModel = GUIFactory.createSpinnerPercentageModel();
		private final SpinnerNumberModel fatPercentageSpinnerModel = GUIFactory.createSpinnerPercentageModel();
		private final JTextField sampleProtocolTextField = GUIFactory.createTextField();
		private final AutoSuggestField samplingStrategyField = GUIFactory
				.createAutoSuggestField(vocabs.get("Sampling strategy"));
		private final AutoSuggestField samplingTypeField = GUIFactory
				.createAutoSuggestField(vocabs.get("Type of sampling program"));
		private final AutoSuggestField samplingMethodField = GUIFactory
				.createAutoSuggestField(vocabs.get("Sampling method"));
		private final JTextField samplingPlanTextField = GUIFactory.createTextField();
		private final JTextField samplingWeightTextField = GUIFactory.createTextField();
		private final JTextField samplingSizeTextField = GUIFactory.createTextField();
		private final AutoSuggestField lotSizeUnitField = GUIFactory
				.createAutoSuggestField(vocabs.get("Lot size unit"));
		private final AutoSuggestField samplingPointField = GUIFactory
				.createAutoSuggestField(vocabs.get("Sampling point"));

		private final List<JComponent> advancedComponents;

		public EditStudySamplePanel(final boolean isAdvanced) {

			// Create labels and fields
			final JLabel sampleNameLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.sampleNameLabel",
					"GM.EditStudySamplePanel.sampleNameTooltip", true);
			final JLabel moisturePercentageLabel = GUIFactory.createLabel(
					"GM.EditStudySamplePanel.moisturePercentageLabel",
					"GM.EditStudySamplePanel.moisturePercentageTooltip");
			final JLabel fatPercentageLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.fatPercentageLabel",
					"GM.EditStudySamplePanel.fatPercentageTooltip");
			final JLabel sampleProtocolLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.sampleProtocolLabel",
					"GM.EditStudySamplePanel.sampleProtocolTooltip", true);
			final JLabel samplingStrategyLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.samplingStrategyLabel",
					"GM.EditStudySamplePanel.samplingStrategyTooltip");
			final JLabel samplingTypeLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.samplingTypeLabel",
					"GM.EditStudySamplePanel.samplingTypeTooltip");
			final JLabel samplingMethodLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.samplingMethodLabel",
					"GM.EditStudySamplePanel.samplingMethodTooltip");
			final JLabel samplingPlanLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.samplingPlanLabel",
					"GM.EditStudySamplePanel.samplingPlanTooltip", true);
			final JLabel samplingWeightLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.samplingWeightLabel",
					"GM.EditStudySamplePanel.samplingWeightTooltip", true);
			final JLabel samplingSizeLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.samplingSizeLabel",
					"GM.EditStudySamplePanel.samplingSizeTooltip", true);
			final JLabel lotSizeUnitLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.lotSizeUnitLabel",
					"GM.EditStudySamplePanel.lotSizeUnitTooltip");
			final JLabel samplingPointLabel = GUIFactory.createLabel("GM.EditStudySamplePanel.samplingPointLabel",
					"GM.EditStudySamplePanel.samplingPointTooltip");

			// Build UI
			final JSpinner moisturePercentageSpinner = GUIFactory.createSpinner(moisturePercentageSpinnerModel);
			final JSpinner fatPercentageSpinner = GUIFactory.createSpinner(fatPercentageSpinnerModel);
			
			// leftPanel (labels)
			final JPanel leftPanel = new JPanel(new GridLayout(12, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			leftPanel.add(sampleNameLabel);
			leftPanel.add(moisturePercentageLabel);
			leftPanel.add(fatPercentageLabel);
			leftPanel.add(sampleProtocolLabel);
			leftPanel.add(samplingStrategyLabel);
			leftPanel.add(samplingTypeLabel);
			leftPanel.add(samplingMethodLabel);
			leftPanel.add(samplingPlanLabel);
			leftPanel.add(samplingWeightLabel);
			leftPanel.add(samplingSizeLabel);
			leftPanel.add(lotSizeUnitLabel);
			leftPanel.add(samplingPointLabel);
			
			// rightPanel (inputs)
			final JPanel rightPanel = new JPanel(new GridLayout(12, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			rightPanel.add(sampleNameTextField);
			rightPanel.add(moisturePercentageSpinner);
			rightPanel.add(fatPercentageSpinner);
			rightPanel.add(sampleProtocolTextField);
			rightPanel.add(samplingStrategyField);
			rightPanel.add(samplingTypeField);
			rightPanel.add(samplingMethodField);
			rightPanel.add(samplingPlanTextField);
			rightPanel.add(samplingWeightTextField);
			rightPanel.add(samplingSizeTextField);
			rightPanel.add(lotSizeUnitField);
			rightPanel.add(samplingPointField);
			
			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);
			
			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(formPanel);
			
			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);

			// If simple mode hide advanced components
			advancedComponents = Arrays.asList(moisturePercentageLabel, moisturePercentageSpinner, // moisture
																									// percentage
					fatPercentageLabel, fatPercentageSpinner, // fat percentag
					samplingStrategyLabel, samplingStrategyField, // sampling strategy
					samplingTypeLabel, samplingTypeField, // sampling program type
					samplingMethodLabel, samplingMethodField, // sampling method
					samplingSizeLabel, samplingSizeTextField, // sampling size
					lotSizeUnitLabel, lotSizeUnitField, // lot size unit
					samplingPointLabel, samplingPointField); // sampling point
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
	private static void addGridComponents(final JPanel panel, final List<Pair<JLabel, JComponent>> pairs) {

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
			final JLabel label = entry.getFirst();
			final JComponent field = entry.getSecond();
			label.setLabelFor(field);

			labelConstraints.gridy = index;
			panel.add(label, labelConstraints);

			fieldConstraints.gridy = index;
			panel.add(field, fieldConstraints);
		}
	}

	private abstract class TopLevelPanel<T> extends JPanel {

		private static final long serialVersionUID = 6410915237186157478L;

		abstract void init(final T t);

		abstract T get();
	}

	private class GeneralInformationPanel extends TopLevelPanel<GeneralInformation> {

		private static final long serialVersionUID = 2705689661594031061L;

		private final JCheckBox advancedCheckBox;

		private final JTextField studyNameTextField = GUIFactory.createTextField2();
		private final JTextField sourceTextField = GUIFactory.createTextField2();
		private final JTextField identifierTextField = GUIFactory.createTextField2();
		private final CreatorPanel creatorPanel = new CreatorPanel();
		private final FixedDateChooser creationDateChooser = new FixedDateChooser();
		private final AutoSuggestField rightsField = GUIFactory.createAutoSuggestField(vocabs.get("Rights"));
		private final JCheckBox availabilityCheckBox = new JCheckBox();
		private final JTextField urlTextField = GUIFactory.createTextField2();
		private final AutoSuggestField formatField = GUIFactory.createAutoSuggestField(vocabs.get("Format"));
		private final ReferencePanel referencePanel;
		private final AutoSuggestField languageField = GUIFactory.createAutoSuggestField(vocabs.get("Language"));
		private final AutoSuggestField softwareField = GUIFactory.createAutoSuggestField(vocabs.get("Software"));
		private final AutoSuggestField languageWrittenInField = GUIFactory
				.createAutoSuggestField(vocabs.get("Language written in"));
		private final AutoSuggestField statusField = GUIFactory.createAutoSuggestField(vocabs.get("Status"));
		private final JTextArea objectiveTextArea = GUIFactory.createTextArea2();
		private final JTextArea descriptionTextArea = GUIFactory.createTextArea2();

		public GeneralInformationPanel() {

			// Create fields
			advancedCheckBox = new JCheckBox("Advanced");

			availabilityCheckBox.setText(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.GeneralInformationPanel.availabilityLabel"));
			availabilityCheckBox.setToolTipText(
					FskPlugin.getDefault().MESSAGES_BUNDLE.getString("GM.GeneralInformationPanel.availabilityTooltip"));

			referencePanel = new ReferencePanel(advancedCheckBox.isSelected());

			// Create labels
			final JLabel studyNameLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.studyNameLabel",
					"GM.GeneralInformationPanel.studyNameTooltip");
			final JLabel sourceLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.sourceLabel",
					"GM.GeneralInformationPanel.sourceTooltip");
			final JLabel identifierLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.identifierLabel",
					"GM.GeneralInformationPanel.identifierTooltip");
			final JLabel creationDateLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.creationDateLabel",
					"GM.GeneralInformationPanel.creationDateTooltip");
			final JLabel rightsLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.rightsLabel",
					"GM.GeneralInformationPanel.rightsTooltip");
			final JLabel urlLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.urlLabel",
					"GM.GeneralInformationPanel.urlTooltip");
			final JLabel formatLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.formatLabel",
					"GM.GeneralInformationPanel.formatTooltip");
			final JLabel languageLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.languageLabel",
					"GM.GeneralInformationPanel.languageTooltip");
			final JLabel softwareLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.softwareLabel",
					"GM.GeneralInformationPanel.softwareTooltip");
			final JLabel languageWrittenInLabel = GUIFactory.createLabel(
					"GM.GeneralInformationPanel.languageWrittenInLabel",
					"GM.GeneralInformationPanel.languageWrittenInTooltip");
			final JLabel statusLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.statusLabel",
					"GM.GeneralInformationPanel.statusTooltip");
			final JLabel objectiveLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.objectiveLabel",
					"GM.GeneralInformationPanel.objectiveTooltip");
			final JLabel descriptionLabel = GUIFactory.createLabel("GM.GeneralInformationPanel.descriptionLabel",
					"GM.GeneralInformationPanel.descriptionTooltip");

			// Hide initially advanced components
			final JScrollPane objectivePane = new JScrollPane(objectiveTextArea);
			final JScrollPane descriptionPane = new JScrollPane(descriptionTextArea);
			final List<JComponent> advancedComponents = Arrays.asList(sourceLabel, sourceTextField, // source
					formatLabel, formatField, // format
					languageLabel, languageField, // language
					softwareLabel, softwareField, // software
					languageWrittenInLabel, languageWrittenInField, // language written in
					statusLabel, statusField, // status
					objectiveLabel, objectivePane, // objective
					descriptionLabel, descriptionPane); // description
			advancedComponents.forEach(it -> it.setEnabled(false));

			// leftPanel
			final JPanel leftPanel = new JPanel(new GridLayout(12, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			leftPanel.add(studyNameLabel);
			leftPanel.add(identifierLabel);
			leftPanel.add(creationDateLabel);
			leftPanel.add(rightsLabel);
			leftPanel.add(availabilityCheckBox);
			leftPanel.add(urlLabel);
			leftPanel.add(sourceLabel);
			leftPanel.add(formatLabel);
			leftPanel.add(languageLabel);
			leftPanel.add(softwareLabel);
			leftPanel.add(languageWrittenInLabel);
			leftPanel.add(statusLabel);

			// rightPanel
			final JPanel rightPanel = new JPanel(new GridLayout(12, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			rightPanel.add(studyNameTextField);
			rightPanel.add(identifierTextField);
			rightPanel.add(creationDateChooser);
			rightPanel.add(rightsField);
			rightPanel.add(new JLabel()); // availabilityCheckBox in leftPanel
			rightPanel.add(urlTextField);
			rightPanel.add(sourceTextField);
			rightPanel.add(formatField);
			rightPanel.add(languageField);
			rightPanel.add(softwareField);
			rightPanel.add(languageWrittenInField);
			rightPanel.add(statusField);

			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// left panel for text areas
			final JPanel taLeftPanel = new JPanel(new GridLayout(2, 1, 5, 5));
			taLeftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			taLeftPanel.add(objectiveLabel);
			taLeftPanel.add(descriptionLabel);

			// right panel for text areas
			final JPanel taRightPanel = new JPanel(new GridLayout(2, 1, 5, 5));
			taRightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			taRightPanel.add(objectivePane);
			taRightPanel.add(descriptionPane);

			// taPanel: form with text areas
			final JPanel taPanel = new JPanel(new BorderLayout());
			taPanel.add(taLeftPanel, BorderLayout.WEST);
			taPanel.add(taRightPanel, BorderLayout.CENTER);

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
			northPanel.add(taPanel);
			northPanel.add(creatorPanel);
			northPanel.add(referencePanel);

			setLayout(new BorderLayout());
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

			for (int i = 0; i < creatorPanel.tableModel.getRowCount(); i++) {
				final VCard vcard = (VCard) creatorPanel.tableModel.getValueAt(i, 0);
				generalInformation.creators.add(vcard);
			}
			generalInformation.format = (String) formatField.getSelectedItem();
			for (int i = 0; i < referencePanel.tableModel.getRowCount(); i++) {
				generalInformation.reference.add((Record) referencePanel.tableModel.getValueAt(i, 0));
			}
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

		final NonEditableTableModel tableModel = new NonEditableTableModel();
		boolean isAdvanced;

		public ReferencePanel(final boolean isAdvanced) {

			super(new BorderLayout());
			setBorder(BorderFactory.createTitledBorder("References"));

			this.isAdvanced = isAdvanced;

			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

				private static final long serialVersionUID = 8702844072231755585L;

				protected void setValue(Object value) {
					if (value == null) {
						setText("");
					} else {
						final Record record = (Record) value;

						final String firstAuthor = record.getAuthors().get(0);
						final String publicationYear = record.getPubblicationYear();
						final String title = record.getTitle();
						setText(String.format("%s_%s_%s", firstAuthor, publicationYear, title));
					}
				};
			};

			final HeadlessTable myTable = new HeadlessTable(tableModel, renderer);

			// buttons
			final ButtonsPanel buttonsPanel = new ButtonsPanel();
			buttonsPanel.addButton.addActionListener(event -> {

				final EditReferencePanel editPanel = new EditReferencePanel(this.isAdvanced);
				final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create reference");

				if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
					tableModel.addRow(new Record[] { editPanel.get() });
				}
			});

			final JButton importButton = new JButton("Import from file");
			importButton.addActionListener(event -> {

				// Configure file chooser
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.addChoosableFileFilter(new SimpleFileFilter("ris", "RIS"));

				final int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						final List<Record> importedRecords = JRis.parse(fc.getSelectedFile());
						for (final Record importedRecord : importedRecords) {
							tableModel.addRow(new Record[] { importedRecord });
						}
					} catch (final IOException | JRisException exception) {
						LOGGER.warn("Error importing RIS references", exception);
					}

				}
			});
			buttonsPanel.add(importButton, 1);

			buttonsPanel.modifyButton.addActionListener(event -> {

				final int rowToEdit = myTable.getSelectedRow();
				if (rowToEdit != -1) {

					final Record ref = (Record) tableModel.getValueAt(rowToEdit, 0);

					final EditReferencePanel editPanel = new EditReferencePanel(this.isAdvanced);
					editPanel.init(ref);

					final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Modify reference");
					if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
						tableModel.setValueAt(editPanel.get(), rowToEdit, 0);
					}
				}
			});

			buttonsPanel.removeButton.addActionListener(event -> {
				final int rowToDelete = myTable.getSelectedRow();
				if (rowToDelete != -1) {
					tableModel.removeRow(rowToDelete);
				}
			});

			add(myTable, BorderLayout.NORTH);
			add(buttonsPanel, BorderLayout.SOUTH);
		}

		void init(final List<Record> references) {
			tableModel.setRowCount(0); // Delete all rows in table
			references.forEach(it -> tableModel.addRow(new Record[] { it }));
		}
	}

	private class CreatorPanel extends JPanel {

		private static final long serialVersionUID = 3543570665869685092L;
		final NonEditableTableModel tableModel = new NonEditableTableModel();

		public CreatorPanel() {

			super(new BorderLayout());

			setBorder(BorderFactory.createTitledBorder("Creators"));

			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

				private static final long serialVersionUID = 1L;

				protected void setValue(Object value) {
					if (value != null) {
						setText(((VCard) value).write());
					}
				};
			};

			final JButton importButton = new JButton("Import from file");
			importButton.addActionListener(event -> {

				// Configure file chooser
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.addChoosableFileFilter(new SimpleFileFilter(".vcf", ".VCF"));

				// Read file
				final int returnVal = fileChooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						final List<VCard> vcards = Ezvcard.parse(fileChooser.getSelectedFile()).all();
						vcards.forEach(it -> tableModel.addRow(new VCard[] { it }));
					} catch (final IOException exception) {
						LOGGER.warn("Error importing VCards", exception);
					}
				}
			});

			final JTable myTable = new HeadlessTable(tableModel, renderer);

			// buttons
			final ButtonsPanel buttonsPanel = new ButtonsPanel();
			buttonsPanel.add(importButton, 1);
			buttonsPanel.addButton.addActionListener(event -> {

				final EditCreatorPanel editPanel = new EditCreatorPanel();
				final int result = showConfirmDialog(editPanel, "Create creator");
				if (result == JOptionPane.OK_OPTION) {
					tableModel.addRow(new VCard[] { editPanel.toVCard() });
				}
			});

			buttonsPanel.modifyButton.addActionListener(event -> {

				final int rowToEdit = myTable.getSelectedRow();
				if (rowToEdit != -1) {

					final VCard creator = (VCard) tableModel.getValueAt(rowToEdit, 0);

					final EditCreatorPanel editPanel = new EditCreatorPanel();
					editPanel.init(creator);
					final int result = showConfirmDialog(editPanel, "Modify creator");
					if (result == JOptionPane.OK_OPTION) {
						tableModel.setValueAt(editPanel.toVCard(), rowToEdit, 0);
					}
				}
			});

			buttonsPanel.removeButton.addActionListener(event -> {
				final int rowToDelete = myTable.getSelectedRow();
				if (rowToDelete != -1) {
					tableModel.removeRow(rowToDelete);
				}
			});

			add(myTable, BorderLayout.NORTH);
			add(buttonsPanel, BorderLayout.SOUTH);
		}

		void init(final List<VCard> vcards) {
			vcards.forEach(it -> tableModel.addRow(new VCard[] { it }));
		}
	}

	private class EditCreatorPanel extends JPanel {

		private static final long serialVersionUID = 3472281253338213542L;

		private final JTextField givenNameTextField;
		private final JTextField familyNameTextField;
		private final JTextField contactTextField;

		public EditCreatorPanel() {

			super(new GridBagLayout());

			// Create fields
			givenNameTextField = GUIFactory.createTextField();
			familyNameTextField = GUIFactory.createTextField();
			contactTextField = GUIFactory.createTextField();

			// Create labels
			final JLabel givenNameLabel = GUIFactory.createLabel("GM.EditCreatorPanel.givenNameLabel");
			final JLabel familyNameLabel = GUIFactory.createLabel("GM.EditCreatorPanel.familyNameLabel");
			final JLabel contactLabel = GUIFactory.createLabel("GM.EditCreatorPanel.contactLabel");

			// Build UI
			final List<Pair<JLabel, JComponent>> pairs = Arrays.asList(new Pair<>(givenNameLabel, givenNameTextField),
					new Pair<>(familyNameLabel, familyNameTextField), new Pair<>(contactLabel, contactTextField));
			addGridComponents(this, pairs);
		}

		void init(final VCard creator) {

			if (creator != null) {
				givenNameTextField.setText(creator.getNickname().getValues().get(0));
				familyNameTextField.setText(creator.getFormattedName().getValue());
				contactTextField.setText(creator.getEmails().get(0).getValue());
			}
		}

		VCard toVCard() {

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
	}

	private class ScopePanel extends TopLevelPanel<Scope> {

		private static final long serialVersionUID = 8153319336584952056L;

		final JButton productButton = new JButton();
		final JButton hazardButton = new JButton();
		final JButton populationButton = new JButton();

		final JTextArea commentTextArea = GUIFactory.createTextArea();
		final JScrollPane commentPane = new JScrollPane(commentTextArea);

		final FixedDateChooser dateChooser = new FixedDateChooser();
		final AutoSuggestField regionField = GUIFactory.createAutoSuggestField(vocabs.get("Region"));
		final AutoSuggestField countryField = GUIFactory.createAutoSuggestField(vocabs.get("Country"));

		final JCheckBox advancedCheckBox = new JCheckBox("Advanced");

		private final EditProductPanel editProductPanel = new EditProductPanel(false);
		private final EditHazardPanel editHazardPanel = new EditHazardPanel(false);
		private final EditPopulationGroupPanel editPopulationGroupPanel = new EditPopulationGroupPanel(false);

		ScopePanel() {

			// Build UI
			productButton.setToolTipText("Click me to add a product");
			productButton.addActionListener(event -> {
				final ValidatableDialog dlg = new ValidatableDialog(editProductPanel, "Create a product");

				if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
					final Product product = editProductPanel.get();
					productButton.setText(String.format("%s_%s", product.environmentName, product.environmentUnit));
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
				final ValidatableDialog dlg = new ValidatableDialog(editPopulationGroupPanel,
						"Create a Population Group");

				if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
					final PopulationGroup populationGroup = editPopulationGroupPanel.get();
					populationButton.setText(populationGroup.populationName);
				}
			});

			// Create labels
			final JLabel productLabel = GUIFactory.createLabel("GM.ScopePanel.productLabel");
			final JLabel hazardLabel = GUIFactory.createLabel("GM.ScopePanel.hazardLabel");
			final JLabel populationLabel = GUIFactory.createLabel("GM.ScopePanel.populationGroupLabel");
			final JLabel commentLabel = GUIFactory.createLabel("GM.ScopePanel.commentLabel",
					"GM.ScopePanel.commentTooltip");
			final JLabel temporalInformationLabel = GUIFactory.createLabel("GM.ScopePanel.temporalInformationLabel",
					"GM.ScopePanel.temporalInformationTooltip");
			final JLabel regionLabel = GUIFactory.createLabel("GM.ScopePanel.regionLabel",
					"GM.ScopePanel.regionTooltip");
			final JLabel countryLabel = GUIFactory.createLabel("GM.ScopePanel.countryLabel",
					"GM.ScopePanel.countryTooltip");

			// leftPanel (labels)
			final JPanel leftPanel = new JPanel(new GridLayout(6, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			leftPanel.add(productLabel);
			leftPanel.add(hazardLabel);
			leftPanel.add(populationLabel);
			leftPanel.add(temporalInformationLabel);
			leftPanel.add(regionLabel);
			leftPanel.add(countryLabel);

			// rightPanel (inputs)
			final JPanel rightPanel = new JPanel(new GridLayout(6, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			rightPanel.add(productButton);
			rightPanel.add(hazardButton);
			rightPanel.add(populationButton);
			rightPanel.add(dateChooser);
			rightPanel.add(regionField);
			rightPanel.add(countryField);

			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// left panel for text areas
			final JPanel taLeftPanel = new JPanel(new GridLayout(1, 1, 5, 5));
			taLeftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			taLeftPanel.add(commentLabel);

			// right panel for text areas
			final JPanel taRightPanel = new JPanel(new GridLayout(1, 1, 5, 5));
			taRightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			taRightPanel.add(commentPane);

			// taPanel: form with text area (comment)
			final JPanel taPanel = new JPanel(new BorderLayout());
			taPanel.add(taLeftPanel, BorderLayout.WEST);
			taPanel.add(taRightPanel, BorderLayout.CENTER);

			// Advanced checkbox
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
			northPanel.add(taPanel);

			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);
		}

		void init(final Scope scope) {
			if (scope != null) {
				editProductPanel.init(scope.product);
				editHazardPanel.init(scope.hazard);
				editPopulationGroupPanel.init(scope.populationGroup);
				if (StringUtils.isNotBlank(scope.temporalInformation)) {
					try {
						dateChooser.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(scope.temporalInformation));
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
				scope.temporalInformation = new SimpleDateFormat(dateChooser.getDateFormatString()).format(date);
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

		final AutoSuggestField laboratoryAccreditationField = GUIFactory
				.createAutoSuggestField(vocabs.get("Laboratory accreditation"));

		private final EditStudySamplePanel editStudySamplePanel = new EditStudySamplePanel(false);
		private final EditDietaryAssessmentMethodPanel editDietaryAssessmentMethodPanel = new EditDietaryAssessmentMethodPanel(
				false);
		private final EditAssayPanel editAssayPanel = new EditAssayPanel(false);

		DataBackgroundPanel() {

			final StudyPanel studyPanel = new StudyPanel();
			studyPanel.setBorder(BorderFactory.createTitledBorder("Study"));

			final JButton studySampleButton = new JButton();
			studySampleButton.setToolTipText("Click me to add Study Sample");
			studySampleButton.addActionListener(event -> {

				final ValidatableDialog dlg = new ValidatableDialog(editStudySamplePanel, "Create Study sample");

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

			final JLabel studySampleLabel = GUIFactory.createLabel("GM.DataBackgroundPanel.studySampleLabel");
			final JLabel dietaryAssessmentMethodLabel = GUIFactory
					.createLabel("GM.DataBackgroundPanel.dietaryAssessmentMethodLabel");
			final JLabel laboratoryAccreditationLabel = GUIFactory
					.createLabel("GM.DataBackgroundPanel.laboratoryAccreditationLabel");
			final JLabel assayLabel = GUIFactory.createLabel("GM.DataBackgroundPanel.assayLabel");

			// Advanced `checkbox`
			advancedCheckBox.addItemListener(event -> {
				studyPanel.advancedComponents.forEach(it -> it.setEnabled(advancedCheckBox.isSelected()));
				editStudySamplePanel.toggleMode();
				editDietaryAssessmentMethodPanel.toggleMode();
				editAssayPanel.toggleMode();
			});

			// left panel (labels)
			final JPanel leftPanel = new JPanel(new GridLayout(4, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			leftPanel.add(studySampleLabel);
			leftPanel.add(dietaryAssessmentMethodLabel);
			leftPanel.add(laboratoryAccreditationLabel);
			leftPanel.add(assayLabel);

			// right panel (inputs)
			final JPanel rightPanel = new JPanel(new GridLayout(4, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			rightPanel.add(studySampleButton);
			rightPanel.add(dietaryAssessmentMethodButton);
			rightPanel.add(laboratoryAccreditationField);
			rightPanel.add(assayButton);

			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);

			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
			northPanel.add(studyPanel);
			northPanel.add(formPanel);

			setLayout(new BorderLayout());
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

		private final JTextField studyIdentifierTextField;
		private final JTextField studyTitleTextField;

		private final JTextArea studyDescriptionTextArea;
		private final JScrollPane studyDescriptionPane;

		private final AutoSuggestField studyDesignTypeField;
		private final AutoSuggestField studyAssayMeasurementsTypeField;
		private final AutoSuggestField studyAssayTechnologyTypeField;
		private final JTextField studyAssayTechnologyPlatformTextField;
		private final AutoSuggestField accreditationProcedureField;
		private final JTextField studyProtocolNameTextField;
		private final AutoSuggestField studyProtocolTypeField;
		private final JTextField studyProtocolDescriptionTextField;
		private final JTextField studyProtocolURITextField;
		private final JTextField studyProtocolVersionTextField;
		private final AutoSuggestField studyProtocolParametersField;
		private final AutoSuggestField studyProtocolComponentsTypeField;

		private final List<JComponent> advancedComponents;

		StudyPanel() {

			super(new GridBagLayout());

			// Create fields
			studyIdentifierTextField = GUIFactory.createTextField();
			studyTitleTextField = GUIFactory.createTextField();

			studyDescriptionTextArea = GUIFactory.createTextArea();
			studyDescriptionPane = new JScrollPane(studyDescriptionTextArea);

			studyDesignTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Study Design Type"));
			studyAssayMeasurementsTypeField = GUIFactory
					.createAutoSuggestField(vocabs.get("Study Assay Measurement Type"));
			studyAssayTechnologyTypeField = GUIFactory
					.createAutoSuggestField(vocabs.get("Study Assay Technology Type"));
			studyAssayTechnologyPlatformTextField = GUIFactory.createTextField();
			accreditationProcedureField = GUIFactory
					.createAutoSuggestField(vocabs.get("Accreditation procedure Ass.Tec"));
			studyProtocolNameTextField = GUIFactory.createTextField();
			studyProtocolTypeField = GUIFactory.createAutoSuggestField(vocabs.get("Study Protocol Type"));
			studyProtocolDescriptionTextField = GUIFactory.createTextField();
			studyProtocolURITextField = GUIFactory.createTextField();
			studyProtocolVersionTextField = GUIFactory.createTextField();
			studyProtocolParametersField = GUIFactory
					.createAutoSuggestField(vocabs.get("Study Protocol Parameters Name"));
			studyProtocolComponentsTypeField = GUIFactory
					.createAutoSuggestField(vocabs.get("Study Protocol Components Type"));

			// Create labels
			final JLabel studyIdentifierLabel = GUIFactory.createLabel("GM.StudyPanel.studyIdentifierLabel",
					"GM.StudyPanel.studyIdentifierTooltip", true);
			final JLabel studyTitleLabel = GUIFactory.createLabel("GM.StudyPanel.studyTitleLabel",
					"GM.StudyPanel.studyTitleTooltip", true);
			final JLabel studyDescriptionLabel = GUIFactory.createLabel("GM.StudyPanel.studyDescriptionLabel",
					"GM.StudyPanel.studyDescriptionTooltip");
			final JLabel studyDesignTypeLabel = GUIFactory.createLabel("GM.StudyPanel.studyDesignTypeLabel",
					"GM.StudyPanel.studyDesignTypeTooltip");
			final JLabel studyAssayMeasurementsTypeLabel = GUIFactory.createLabel(
					"GM.StudyPanel.studyAssayMeasurementsTypeLabel", "GM.StudyPanel.studyAssayMeasurementsTypeTooltip");
			final JLabel studyAssayTechnologyTypeLabel = GUIFactory.createLabel(
					"GM.StudyPanel.studyAssayTechnologyTypeLabel", "GM.StudyPanel.studyAssayTechnologyTypeTooltip");
			final JLabel studyAssayTechnologyPlatformLabel = GUIFactory.createLabel(
					"GM.StudyPanel.studyAssayTechnologyPlatformLabel",
					"GM.StudyPanel.studyAssayTechnologyPlatformTooltip");
			final JLabel accreditationProcedureLabel = GUIFactory.createLabel(
					"GM.StudyPanel.accreditationProcedureLabel", "GM.StudyPanel.accreditationProcedureTooltip");
			final JLabel studyProtocolNameLabel = GUIFactory.createLabel("GM.StudyPanel.protocolNameLabel",
					"GM.StudyPanel.protocolNameTooltip");
			final JLabel studyProtocolTypeLabel = GUIFactory.createLabel("GM.StudyPanel.protocolTypeLabel",
					"GM.StudyPanel.protocolTypeTooltip");
			final JLabel studyProtocolDescriptionLabel = GUIFactory
					.createLabel("GM.StudyPanel.protocolDescriptionLabel", "GM.StudyPanel.protocolDescriptionTooltip");
			final JLabel studyProtocolURILabel = GUIFactory.createLabel("GM.StudyPanel.protocolURILabel",
					"GM.StudyPanel.protocolURITooltip");
			final JLabel studyProtocolVersionLabel = GUIFactory.createLabel("GM.StudyPanel.protocolDescriptionLabel",
					"GM.StudyPanel.protocolDescriptionTooltip");
			final JLabel studyProtocolParametersLabel = GUIFactory.createLabel("GM.StudyPanel.parametersLabel",
					"GM.StudyPanel.parametersTooltip");
			final JLabel studyProtocolComponentsTypeLabel = GUIFactory.createLabel("GM.StudyPanel.componentsTypeLabel",
					"GM.StudyPanel.componentsTypeTooltip");

			// Build UI
//			final List<Pair<JLabel, JComponent>> pairs = Arrays.asList(
//					new Pair<>(studyIdentifierLabel, studyIdentifierTextField),
//					new Pair<>(studyTitleLabel, studyTitleTextField),
//					new Pair<>(studyDescriptionLabel, studyDescriptionPane),
//					new Pair<>(studyDesignTypeLabel, studyDesignTypeField),
//					new Pair<>(studyAssayMeasurementsTypeLabel, studyAssayMeasurementsTypeField),
//					new Pair<>(studyAssayTechnologyTypeLabel, studyAssayTechnologyTypeField),
//					new Pair<>(studyAssayTechnologyPlatformLabel, studyAssayTechnologyPlatformTextField),
//					new Pair<>(accreditationProcedureLabel, accreditationProcedureField),
//					new Pair<>(studyProtocolNameLabel, studyProtocolNameTextField),
//					new Pair<>(studyProtocolTypeLabel, studyProtocolTypeField),
//					new Pair<>(studyProtocolDescriptionLabel, studyProtocolDescriptionTextField),
//					new Pair<>(studyProtocolURILabel, studyProtocolURITextField),
//					new Pair<>(studyProtocolParametersLabel, studyProtocolParametersField),
//					new Pair<>(studyProtocolComponentsTypeLabel, studyProtocolComponentsTypeField));
//			EditorNodeDialog.addGridComponents(this, pairs);
			
			// leftPanel (labels)
			final JPanel leftPanel = new JPanel(new GridLayout(13, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			leftPanel.add(studyIdentifierLabel);
			leftPanel.add(studyTitleLabel);
			leftPanel.add(studyDesignTypeLabel);
			leftPanel.add(studyAssayMeasurementsTypeLabel);
			leftPanel.add(studyAssayTechnologyTypeLabel);
			leftPanel.add(studyAssayTechnologyPlatformLabel);
			leftPanel.add(accreditationProcedureLabel);
			leftPanel.add(studyProtocolNameLabel);
			leftPanel.add(studyProtocolTypeLabel);
			leftPanel.add(studyProtocolDescriptionLabel);
			leftPanel.add(studyProtocolURILabel);
			leftPanel.add(studyProtocolParametersLabel);
			leftPanel.add(studyProtocolComponentsTypeLabel);
			
			// rightPanel (inputs)
			final JPanel rightPanel = new JPanel(new GridLayout(13, 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			rightPanel.add(studyIdentifierTextField);
			rightPanel.add(studyTitleTextField);
			rightPanel.add(studyDesignTypeField);
			rightPanel.add(studyAssayMeasurementsTypeField);
			rightPanel.add(studyAssayTechnologyPlatformTextField);
			rightPanel.add(accreditationProcedureField);
			rightPanel.add(studyProtocolNameTextField);
			rightPanel.add(studyProtocolTypeField);
			rightPanel.add(studyProtocolDescriptionTextField);
			rightPanel.add(studyProtocolURITextField);
			rightPanel.add(studyProtocolParametersField);
			rightPanel.add(studyProtocolComponentsTypeField);
			
			// formPanel
			final JPanel formPanel = new JPanel(new BorderLayout());
			formPanel.add(leftPanel, BorderLayout.WEST);
			formPanel.add(rightPanel, BorderLayout.CENTER);
			
			// northPanel
			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(formPanel);
			
			setLayout(new BorderLayout());
			add(northPanel, BorderLayout.NORTH);

			advancedComponents = Arrays.asList(studyDescriptionLabel, studyDescriptionPane, studyDesignTypeLabel,
					studyDesignTypeField, studyAssayMeasurementsTypeLabel, studyAssayMeasurementsTypeField,
					studyAssayTechnologyTypeLabel, studyAssayTechnologyTypeField, studyAssayTechnologyPlatformLabel,
					studyAssayTechnologyPlatformTextField, accreditationProcedureLabel, accreditationProcedureField,
					studyProtocolNameLabel, studyProtocolNameTextField, studyProtocolTypeLabel, studyProtocolTypeField,
					studyProtocolDescriptionLabel, studyProtocolDescriptionTextField, studyProtocolURILabel,
					studyProtocolURITextField, studyProtocolVersionLabel, studyProtocolVersionTextField,
					studyProtocolParametersLabel, studyProtocolParametersField, studyProtocolComponentsTypeLabel,
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

			final JPanel northPanel = new JPanel();
			northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
			northPanel.add(GUIFactory.createAdvancedPanel(advancedCheckBox));
			northPanel.add(parametersPanel);
			northPanel.add(qualityMeasuresPanel);
			northPanel.add(modelEquationsPanel);

			setLayout(new BorderLayout());
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
			modelMath.parameter.clear();
			modelMath.parameter.addAll(parametersPanel.params);

			// Save SSE, MSE, R2, RMSE, AIC and BIC
			modelMath.sse = qualityMeasuresPanel.sseSpinnerModel.getNumber().doubleValue();
			modelMath.mse = qualityMeasuresPanel.mseSpinnerModel.getNumber().doubleValue();
			modelMath.rSquared = qualityMeasuresPanel.r2SpinnerModel.getNumber().doubleValue();
			modelMath.rmse = qualityMeasuresPanel.rmseSpinnerModel.getNumber().doubleValue();
			modelMath.aic = qualityMeasuresPanel.aicSpinnerModel.getNumber().doubleValue();
			modelMath.bic = qualityMeasuresPanel.bicSpinnerModel.getNumber().doubleValue();

			// Save model equations
			modelMath.modelEquation.addAll(modelEquationsPanel.equations);

			// TODO: Save fitting procedure
			// TODO: Save exposure
			// TODO: Save events

			return modelMath;
		}
	}

	private class ParametersPanel extends JPanel {

		private static final long serialVersionUID = -5986975090954482038L;

		final NonEditableTableModel tableModel = new NonEditableTableModel();
		final List<Parameter> params = new ArrayList<>(0);

		boolean isAdvanced;

		ParametersPanel(final boolean isAdvanced) {

			super(new BorderLayout());

			this.isAdvanced = isAdvanced;

			setBorder(BorderFactory.createTitledBorder("Parameters"));

			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
				private static final long serialVersionUID = -2930961770705064623L;

				protected void setValue(Object value) {
					final Parameter param = (Parameter) value;
					setText(param.name + "[" + param.unit + "]");
				};
			};
			final JTable myTable = new HeadlessTable(tableModel, renderer);

			// buttons
			final ButtonsPanel buttonsPanel = new ButtonsPanel();
			buttonsPanel.addButton.addActionListener(event -> {
				final EditParameterPanel editPanel = new EditParameterPanel(this.isAdvanced);
				final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create parameter");
				if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
					tableModel.addRow(new Parameter[] { editPanel.get() });
				}
			});

			buttonsPanel.modifyButton.addActionListener(event -> {
				final int rowToEdit = myTable.getSelectedRow();
				if (rowToEdit != -1) {

					final Parameter param = (Parameter) tableModel.getValueAt(rowToEdit, 0);
					final EditParameterPanel editPanel = new EditParameterPanel(this.isAdvanced);
					editPanel.init(param);

					final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Modify parameter");
					if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
						tableModel.setValueAt(editPanel.get(), rowToEdit, 0);
					}
				}
			});

			buttonsPanel.removeButton.addActionListener(event -> {
				final int rowToDelete = myTable.getSelectedRow();
				if (rowToDelete != -1) {
					tableModel.removeRow(rowToDelete);
				}
			});

			add(myTable, BorderLayout.NORTH);
			add(buttonsPanel, BorderLayout.SOUTH);
		}

		void init(final List<Parameter> parameters) {
			tableModel.setRowCount(0); // Remove all rows in table
			parameters.forEach(it -> tableModel.addRow(new Parameter[] { it }));
			params.addAll(parameters);
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

			super(new GridBagLayout());

			final JLabel sseLabel = new JLabel("SSE");
			final JSpinner sseSpinner = GUIFactory.createSpinner(sseSpinnerModel);

			final JLabel mseLabel = new JLabel("MSE");
			final JSpinner mseSpinner = GUIFactory.createSpinner(mseSpinnerModel);

			final JLabel rmseLabel = new JLabel("RMSE");
			final JSpinner rmseSpinner = GUIFactory.createSpinner(rmseSpinnerModel);

			final JLabel r2Label = new JLabel("r-Squared");
			final JSpinner r2Spinner = GUIFactory.createSpinner(r2SpinnerModel);

			final JLabel aicLabel = new JLabel("AIC");
			final JSpinner aicSpinner = GUIFactory.createSpinner(aicSpinnerModel);

			final JLabel bicLabel = new JLabel("BIC");
			final JSpinner bicSpinner = GUIFactory.createSpinner(bicSpinnerModel);

			final List<Pair<JLabel, JComponent>> pairs = Arrays.asList(new Pair<>(sseLabel, sseSpinner),
					new Pair<>(mseLabel, mseSpinner), new Pair<>(rmseLabel, rmseSpinner),
					new Pair<>(r2Label, r2Spinner), new Pair<>(aicLabel, aicSpinner), new Pair<>(bicLabel, bicSpinner));
			EditorNodeDialog.addGridComponents(this, pairs);

			setBorder(BorderFactory.createTitledBorder("Quality measures"));
		}
	}

	private class ModelEquationsPanel extends JPanel {

		private static final long serialVersionUID = 7194287921709100267L;

		final NonEditableTableModel tableModel = new NonEditableTableModel();
		final List<ModelEquation> equations = new ArrayList<>();

		private final EditModelEquationPanel editPanel = new EditModelEquationPanel(false);

		ModelEquationsPanel(final boolean isAdvanced) {

			super(new BorderLayout());

			setBorder(BorderFactory.createTitledBorder("Model equation"));
			equations.forEach(it -> tableModel.addRow(new ModelEquation[] { it }));

			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				protected void setValue(Object value) {
					setText(((ModelEquation) value).equationName);
				}
			};

			final HeadlessTable myTable = new HeadlessTable(tableModel, renderer);

			final ButtonsPanel buttonsPanel = new ButtonsPanel();

			buttonsPanel.addButton.addActionListener(event -> {
				final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Create equation");
				if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
					tableModel.addRow(new ModelEquation[] { editPanel.get() });
				}
			});

			buttonsPanel.modifyButton.addActionListener(event -> {
				final int rowToEdit = myTable.getSelectedRow();
				if (rowToEdit != -1) {
					final ModelEquation equation = (ModelEquation) tableModel.getValueAt(rowToEdit, 0);
					editPanel.init(equation);

					final ValidatableDialog dlg = new ValidatableDialog(editPanel, "Modify equation");
					if (dlg.getValue().equals(JOptionPane.OK_OPTION)) {
						tableModel.setValueAt(editPanel.get(), rowToEdit, 0);
					}
				}
			});

			buttonsPanel.removeButton.addActionListener(event -> {
				final int rowToDelete = myTable.getSelectedRow();
				if (rowToDelete != -1) {
					tableModel.removeRow(rowToDelete);
				}
			});

			add(myTable, BorderLayout.NORTH);
			add(buttonsPanel, BorderLayout.SOUTH);
		}

		void toggleMode() {
			editPanel.toggleMode();
		}

		void init(final List<ModelEquation> modelEquations) {
			modelEquations.forEach(it -> tableModel.addRow(new ModelEquation[] { it }));
			equations.addAll(modelEquations);
		}
	}
}
