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
package de.bund.bfr.knime.fsklab.nodes

import com.gmail.gcolaianni5.jris.bean.Record
import com.gmail.gcolaianni5.jris.bean.Type
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.nodes.ui.FixedDateChooser
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel
import de.bund.bfr.knime.fsklab.rakip.*
import de.bund.bfr.knime.ui.AutoSuggestField
import ezvcard.VCard
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.knime.core.node.*
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import java.awt.*
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel

private val LOGGER = NodeLogger.getLogger("EditorNodeDialog")

class EditorNodeDialog : DataAwareNodeDialogPane() {

    private val modelScriptPanel = ScriptPanel("Model script", "", true)
    private val paramScriptPanel = ScriptPanel("Parameters script", "", true)
    private val vizScriptPanel = ScriptPanel("Visualization script", "", true)
    private val generalInformationPanel = GeneralInformationPanel()
    private val scopePanel = ScopePanel()
    private val dataBackgroundPanel = DataBackgroundPanel()
    private val modelMathPanel = ModelMathPanel()

    private var settings: EditorNodeSettings

    init {

        // Initialize settings (current values are garbage, need to be loaded from settings/input port)
        settings = EditorNodeSettings()
        settings.genericModel = GenericModel()

        // Add ScriptPanels
        addTab(modelScriptPanel.name, modelScriptPanel)
        addTab(paramScriptPanel.name, paramScriptPanel)
        addTab(vizScriptPanel.name, vizScriptPanel)
        addTab("General information", generalInformationPanel, true)
        addTab("Scope", scopePanel, true)
        addTab("Data background", dataBackgroundPanel, true)
        addTab("Model math", modelMathPanel, true)

        updatePanels()
    }

    // Update the scripts in the ScriptPanels
    private fun updatePanels() {
        modelScriptPanel.textArea.text = settings.modifiedModelScript
        paramScriptPanel.textArea.text = settings.modifiedParametersScript
        vizScriptPanel.textArea.text = settings.modifiedVisualizationScript

        generalInformationPanel.init(settings.genericModel.generalInformation)
        scopePanel.init(settings.genericModel.scope)
        dataBackgroundPanel.init(settings.genericModel.dataBackground)
        // TODO: init modelMathPanel
    }

    override fun loadSettingsFrom(settings: NodeSettingsRO, input: Array<PortObject>) {

        val editorSettings = EditorNodeSettings()
        try {
            editorSettings.loadSettings(settings)
        } catch (exception: InvalidSettingsException) {
            throw NotConfigurableException("InvalidSettingsException", exception)
        }

        val inObj = input[0] as FskPortObject

        // If input model has not changed (the original scripts stored iins ettings match the input model).
        if (editorSettings.originalModelScript == inObj.model && editorSettings.originalParametersScript == inObj.param &&
                editorSettings.originalVisualizationScript == inObj.viz) {
            // Updates settings
            this.settings = editorSettings
        } else {
            // Discard settings and replace them with input model
            this.settings.originalModelScript = inObj.model
            this.settings.originalParametersScript = inObj.param
            this.settings.originalVisualizationScript = inObj.viz

            this.settings.modifiedModelScript = inObj.model
            this.settings.modifiedParametersScript = inObj.param
            this.settings.modifiedVisualizationScript = inObj.viz

            this.settings.genericModel = inObj.genericModel
        }

        updatePanels()
    }

    override fun loadSettingsFrom(settings: NodeSettingsRO, input: Array<PortObjectSpec>) {
        try {
            this.settings.loadSettings(settings)
        } catch (exception: InvalidSettingsException) {
            throw NotConfigurableException("InvalidSettingsException", exception)
        }

        updatePanels()
    }

    override fun saveSettingsTo(settings: NodeSettingsWO) {

        // Save modified scripts to settings
        this.settings.modifiedModelScript = modelScriptPanel.textArea.text.trim()
        this.settings.modifiedParametersScript = paramScriptPanel.textArea.text.trim()
        this.settings.modifiedVisualizationScript = vizScriptPanel.textArea.text.trim()

        this.settings.genericModel.generalInformation = generalInformationPanel.get()
        this.settings.genericModel.scope = scopePanel.get()
        this.settings.genericModel.dataBackground = dataBackgroundPanel.get()
        // TODO: get model math

        this.settings.saveSettings(settings)
    }
}

val vocabs: Map<String, Set<String>> = readVocabularies()
private fun readVocabularies(): Map<String, Set<String>> {
    val vocabs = mutableMapOf<String, Set<String>>()

    val sheets = listOf(
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
            "Parameter data type", "Parameter source", "Parameter subject", "Parameter distribution"
    )

    EditorNodeDialog::class.java.getResourceAsStream("/FSKLab_Config_Controlled_Vocabularies.xlsx").use { stream ->

        val workbook = XSSFWorkbook(stream)
        sheets.forEach { vocabs.put(it, readVocabFromSheet(workbook = workbook, sheetName = it)) }
        workbook.close()
    }

    return vocabs
}

/**
 * Read controlled vocabullary from spreadsheet.
 * @return Set with controlled vocabulary. If the sheet is not found returns empty set.
 */
private fun readVocabFromSheet(workbook: Workbook, sheetName: String): Set<String> {

    val sheet = workbook.getSheet(sheetName)
    if (sheet == null) {
        LOGGER.warn("Spreadsheet not found: $sheetName")
        return emptySet()
    }

    return sheet
            .filter { it.rowNum != 0 }  // Skip header
            .mapNotNull { it.getCell(0) }
            // Replace faulty cells with "" that are later discarded
            .map {
                try {
                    it.stringCellValue
                } catch (exception: Exception) {
                    LOGGER.warn("Controlled vocabulary ${sheet.sheetName}: wrong value $it")
                    ""
                }
            }
            .filter { it.isNotBlank() }  // Skip empty cells
            .toSet()
}

// TODO: this should be shared through the plugin (all the FSK nodes)
private val bundle: ResourceBundle = ResourceBundle.getBundle("MessagesBundle")

/**
 * Create a JLabel with no tooltip, retrieving its text from resource bundle.
 * @param textKey Key of the JLabel text in the resource bundle
 * @param toolTipKey Key of the tool tip text in the resource bundle. [""] if not provided
 * @param isMandatory Whether the property is mandatory. false if not provided.
 */
private fun createLabel(textKey: String, toolTipKey: String = "",
                        isMandatory: Boolean = false): JLabel {

    var labelText = bundle.getString(textKey)
    if (isMandatory) labelText += " *"

    val label = JLabel(labelText)

    if (toolTipKey.isNotEmpty()) {
        label.toolTipText = bundle.getString(toolTipKey)
    }

    return JLabel(labelText)
}

private fun createAdvancedPanel(checkBox: JCheckBox): JPanel {

    val panel = JPanel()
    panel.background = Color.lightGray
    panel.add(checkBox)

    return panel
}

private fun createTextField() = JTextField(30)

private fun createTextArea(): JTextArea {

    val textArea = JTextArea(5, 30)
    textArea.text = ""
    textArea.lineWrap = true  // Wrap long lines
    textArea.wrapStyleWord = true // Wrap only at white space

    return textArea
}

/**
 * @param possibleValues Set
 * @return a JComboBox with the passed possible values
 */
private fun createComboBox(possibleValues: Collection<String>): JComboBox<String> {
    val comboBox = JComboBox(possibleValues.toTypedArray())
    comboBox.selectedIndex = -1

    return comboBox
}

/**
 * @param possibleValues Set
 * @return an AutoSuggestField with the passed possible values. The field has 10 columns.
 */
private fun createAutoSuggestField(possibleValues: Set<String>): AutoSuggestField {
    val field = AutoSuggestField(10)
    field.setPossibleValues(possibleValues)
    return field
}

/**
 * Adds a component to the end of a container. Also notifies the layout manager to add the
 * component to this container's layout using the specified constraints.
 * @param comp the component to be added
 * @param gridx the initial grid value
 * @param gridy the initial grid value
 * @param gridwidth the initial grid width
 * @param gridheight the initial grid height
 */
private fun JPanel.add(comp: JComponent, gridx: Int, gridy: Int, gridwidth: Int = 1, gridheight: Int = 1) {

    val constraints = GridBagConstraints()
    constraints.gridx = gridx
    constraints.gridy = gridy
    constraints.gridwidth = gridwidth
    constraints.gridheight = gridheight
    constraints.ipadx = 10
    constraints.ipady = 10
    constraints.anchor = GridBagConstraints.LINE_START

    add(comp, constraints)
}

/** Creates a JSpinner with 5 columns. */
private fun createSpinner(model: AbstractSpinnerModel): JSpinner {

    val spinner = JSpinner(model)
    (spinner.editor as JSpinner.DefaultEditor).textField.columns = 5

    return spinner
}

/** Creates a [SpinnerNumberModel] for integers with no limits and initial value 0. */
private fun createSpinnerIntegerModel() = SpinnerNumberModel(0, null, null, 1)

/** Creates a [SpinnerNumberModel] for real numbers with no limits and initial value 0.0. */
private fun createSpinnerDoubleModel() = SpinnerNumberModel(0.0, null, null, .01)

/**
 * Creates a [SpinnerNumberModel] for percentages (doubles) and initial value 0.0.
 *
 * Has limits 0.0 and 1.0.
 */
private fun createSpinnerPercentageModel() = SpinnerNumberModel(0.0, 0.0, 1.0, 0.1)

class NonEditableTableModel : DefaultTableModel(arrayOf(), arrayOf("header")) {
    override fun isCellEditable(row: Int, column: Int) = false
}

class HeadlessTable(model: NonEditableTableModel,
                    private val renderer: DefaultTableCellRenderer) : JTable(model) {

    init {
        tableHeader = null  // Hide header
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    }


    override fun getCellRenderer(row: Int, column: Int) = renderer
}

private class ButtonsPanel : JPanel() {

    val addButton = JButton("Add")
    val modifyButton = JButton("Modify")
    val removeButton = JButton("Remove")

    init {
        add(addButton)
        add(modifyButton)
        add(removeButton)
    }
}

private class CreatorPanel : JPanel(BorderLayout()) {

    private val tableModel = NonEditableTableModel()
    val creators = mutableListOf<VCard>()

    init {
        border = BorderFactory.createTitledBorder("Creators")

        val renderer = object : DefaultTableCellRenderer() {
            override fun setValue(value: Any?) {
                text = if (value == null) {
                    ""
                } else {
                    val creator = value as VCard

                    val givenName = creator.nickname.values[0]
                    val familyName = creator.formattedName.value
                    val contact = creator.emails[0].value

                    "${givenName}_${familyName}_$contact"
                }
            }
        }

        val myTable = HeadlessTable(tableModel, renderer)

        // buttons
        val buttonsPanel = ButtonsPanel()
        buttonsPanel.addButton.addActionListener {

            val editPanel = EditCreatorPanel()

            val result = JOptionPane.showConfirmDialog(null, editPanel, "Create creator",
                    JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE)
            if (result == JOptionPane.OK_OPTION) {
                tableModel.addRow(arrayOf(editPanel.toVCard()))
            }
        }

        buttonsPanel.modifyButton.addActionListener {
            val rowToEdit = myTable.selectedRow
            if (rowToEdit != -1) {

                val creator = tableModel.getValueAt(rowToEdit, 0) as VCard

                val editPanel = EditCreatorPanel()
                editPanel.init(creator)

                val result = JOptionPane.showConfirmDialog(null, editPanel, "Modify creator",
                        JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE)
                if (result == JOptionPane.OK_CANCEL_OPTION) {
                    tableModel.setValueAt(editPanel.toVCard(), rowToEdit, 0)
                }
            }
        }

        buttonsPanel.removeButton.addActionListener {
            val rowToDelete = myTable.selectedRow
            if (rowToDelete != -1) {
                tableModel.removeRow(rowToDelete)
            }
        }

        add(myTable, BorderLayout.NORTH)
        add(buttonsPanel, BorderLayout.SOUTH)
    }

    fun init(vcards: List<VCard>) {
        vcards.forEach { tableModel.addRow(arrayOf(it)) }
        creators.addAll(vcards)
    }
}

fun JPanel.addGridComponents(pairs: List<Pair<JLabel, JComponent>>) {

    val labelConstraints = GridBagConstraints()
    labelConstraints.gridx = 0
    labelConstraints.ipadx = 10
    labelConstraints.ipady = 10
    labelConstraints.anchor = GridBagConstraints.LINE_START

    val fieldConstraints = GridBagConstraints()
    fieldConstraints.gridx = 1
    fieldConstraints.ipadx = 10
    fieldConstraints.ipady = 10
    fieldConstraints.anchor = GridBagConstraints.LINE_START

    for ((index, entry) in pairs.withIndex()) {
        val label = entry.first
        val field = entry.second
        label.labelFor = field

        labelConstraints.gridy = index
        add(label, labelConstraints)

        fieldConstraints.gridy = index
        add(field, fieldConstraints)
    }
}

// Validation methods
internal fun JTextField.hasValidValue() = text.isNotBlank()

internal fun JTextArea.hasValidValue() = text.isNotBlank()

internal fun AutoSuggestField.hasValidValue(): Boolean {
    val field = editor.editorComponent as JTextField
    return field.text.isNotBlank()
}


private abstract class ValidatablePanel : JPanel(GridBagLayout()) {
    abstract fun validatePanel(): List<String>
}

private class DataBackgroundPanel : Box(BoxLayout.PAGE_AXIS) {

    private val advancedCheckBox = JCheckBox("Advanced")

    private val laboratoryAccreditationField = createAutoSuggestField(
            vocabs["Laboratory accreditation"] ?: emptySet())

    private val editStudySamplePanel = EditStudySamplePanel(false)
    private val editDietaryAssessmentMethodPanel = EditDietaryAssessmentMethodPanel(false)
    private val editAssayPanel = EditAssayPanel(false)

    init {

        val studyPanel = StudyPanel()
        studyPanel.border = BorderFactory.createTitledBorder("Study")

        val studySampleButton = JButton()
        studySampleButton.toolTipText = "Click me to add Study Sample"
        studySampleButton.addActionListener {
            val dlg = ValidatableDialog(editStudySamplePanel, "Create Study sample")
            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                val studySample = editStudySamplePanel.get()
                // Update button's text
                studySampleButton.text = studySample.sample
            }
        }

        val dietaryAssessmentMethodButton = JButton()
        dietaryAssessmentMethodButton.toolTipText = "Click me to add Dietary assessment method"
        dietaryAssessmentMethodButton.addActionListener {
            val dlg = ValidatableDialog(editDietaryAssessmentMethodPanel,
                    "Create dietary assessment method")
            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                val method = editDietaryAssessmentMethodPanel.get()
                // Update button's text
                dietaryAssessmentMethodButton.text = method.collectionTool
            }
        }

        val assayButton = JButton()
        assayButton.toolTipText = "Click me to add Assay"
        assayButton.addActionListener {
            val dlg = ValidatableDialog(editAssayPanel, "Create assay")
            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                val (name) = editAssayPanel.get()
                // Update button's text
                assayButton.text = name
            }
        }

        // Create labels
        val studySampleLabel = createLabel("GM.DataBackgroundPanel.studySampleLabel")
        val dietaryAssessmentMethodLabel = createLabel("GM.DataBackgroundPanel.dietaryAssessmentMethodLabel")
        val laboratoryAccreditationLabel = createLabel("GM.DataBackgroundPanel.laboratoryAccreditationLabel")
        val assayLabel = createLabel("GM.DataBackgroundPanel.assayLabel")

        val propertiesPanel = JPanel(GridBagLayout())
        propertiesPanel.add(studyPanel, 0, 0, 3)

        propertiesPanel.add(studySampleLabel, 0, 1)
        propertiesPanel.add(studySampleButton, 1, 1)

        propertiesPanel.add(dietaryAssessmentMethodLabel, 0, 2)
        propertiesPanel.add(dietaryAssessmentMethodButton, 1, 2)

        propertiesPanel.add(laboratoryAccreditationLabel, 0, 3)
        propertiesPanel.add(laboratoryAccreditationField, 1, 3)

        propertiesPanel.add(assayLabel, 0, 4)
        propertiesPanel.add(assayButton, 1, 4)

        // Advanced `checkbox`
        advancedCheckBox.addItemListener {
            studyPanel.advancedComponents.forEach { it.isVisible = advancedCheckBox.isSelected }
            editStudySamplePanel.toggleMode()
            editDietaryAssessmentMethodPanel.toggleMode()
            editAssayPanel.toggleMode()
        }

        add(createAdvancedPanel(advancedCheckBox))
        add(createGlue())
        add(propertiesPanel)
    }

    fun init(dataBackground: DataBackground) {
        editStudySamplePanel.init(dataBackground.studySample)
        editDietaryAssessmentMethodPanel.init(dataBackground.dietaryAssessmentMethod)
        editAssayPanel.init(dataBackground.assay)
    }

    fun get(): DataBackground {
        val dataBackground = DataBackground()
        dataBackground.studySample = editStudySamplePanel.get()
        dataBackground.dietaryAssessmentMethod = editDietaryAssessmentMethodPanel.get()
        dataBackground.assay = editAssayPanel.get()

        return dataBackground
    }
}

/** Validatable dialogs and panels. */
private class ValidatableDialog(panel: ValidatablePanel, dialogTitle: String) : JDialog(null as Frame?, true) {

    private val optionPane = JOptionPane(JScrollPane(panel), JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_OPTION)

    init {

        title = dialogTitle

        // Handle window closing correctly
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        optionPane.addPropertyChangeListener { e ->

            if (isVisible && e.source == optionPane &&
                    e.propertyName == JOptionPane.VALUE_PROPERTY &&
                    optionPane.value != JOptionPane.UNINITIALIZED_VALUE) {

                val value = optionPane.value as? Int

                if (value == JOptionPane.YES_OPTION) {
                    val errors = panel.validatePanel()
                    if (errors.isEmpty()) {
                        dispose()
                    } else {
                        val msg = errors.joinToString(separator = "\n")
                        JOptionPane.showMessageDialog(this, msg, "Missing fields",
                                JOptionPane.ERROR_MESSAGE)

                        // Reset the JOptionPane's value. If you don't this, the if the user presses
                        // the same button next time, no property change will be fired.
                        optionPane.value = JOptionPane.UNINITIALIZED_VALUE  // Reset value
                    }
                } else if (value == JOptionPane.NO_OPTION) {
                    dispose()
                }
            }
        }

        contentPane = optionPane
        pack()
        setLocationRelativeTo(null)  // center dialog
        isVisible = true
    }

    fun getValue(): Any = optionPane.value
}

// EditPanels
private abstract class EditPanel<T> : ValidatablePanel() {

    abstract fun init(t: T)
    abstract fun get(): T

    /** @return list of JComponents related to optional properties. */
    abstract fun getAdvancedComponents(): List<JComponent>

    /** Hide or show the JComponents related to optional properties. */
    fun toggleMode() = getAdvancedComponents().forEach { it.isVisible = !it.isVisible }
}

private class EditAssayPanel(isAdvanced: Boolean) : EditPanel<Assay>() {

    private val nameLabel: JLabel = createLabel("GM.EditAssayPanel.nameLabel", "GM.EditAssayPanel.nameTooltip", true)
    private val nameTextField: JTextField = createTextField()

    private val descriptionLabel: JLabel = createLabel("GM.EditAssayPanel.descriptionLabel", "GM.EditAssayPanel.descriptionTooltip", true)
    private val descriptionTextArea: JTextArea = createTextArea()
    private val descriptionPane = JScrollPane(descriptionTextArea)

    init {
        val pairs = listOf(Pair(nameLabel, nameTextField), Pair(descriptionLabel, descriptionTextArea))
        addGridComponents(pairs)

        // If simple mode hide advanced components
        if (!isAdvanced) {
            descriptionLabel.isVisible = false
            descriptionTextArea.isVisible = false
        }
    }

    override fun init(t: Assay) {
        nameTextField.text = t.name
        descriptionTextArea.text = t.description
    }

    override fun get() = Assay(name = nameTextField.text, description = descriptionTextArea.text)

    override fun validatePanel(): List<String> {

        val errors = mutableListOf<String>()
        if (nameTextField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditAssayPanel.nameLabel")}")
        }

        return errors
    }

    override fun getAdvancedComponents() = listOf<JComponent>(descriptionLabel, descriptionPane)
}

private class EditCreatorPanel : JPanel(GridBagLayout()) {

    private val givenNameTextField = createTextField()
    private val familyNameTextField = createTextField()
    private val contactTextField = createTextField()

    init {

        // Create labels
        val givenNameLabel = createLabel("GM.EditCreatorPanel.givenNameLabel")
        val familyNameLabel = createLabel("GM.EditCreatorPanel.familyNameLabel")
        val contactLabel = createLabel("GM.EditCreatorPanel.contactLabel")

        // Build UI
        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(givenNameLabel, givenNameTextField),
                Pair(familyNameLabel, familyNameTextField),
                Pair(contactLabel, contactTextField))
        addGridComponents(pairs)
    }

    fun init(creator: VCard) {
        givenNameTextField.text = creator.nickname.values[0]
        familyNameTextField.text = creator.formattedName.value
        contactTextField.text = creator.emails[0].value
    }

    fun toVCard(): VCard {

        val vCard = VCard()
        vCard.setNickname(givenNameTextField.text)
        vCard.setFormattedName(familyNameTextField.text)
        contactTextField.text.let {
            if (it.isNotEmpty()) {
                vCard.addEmail(it)
            }
        }

        return vCard
    }
}

private class EditDietaryAssessmentMethodPanel(isAdvanced: Boolean) : EditPanel<DietaryAssessmentMethod>() {

    private val dataCollectionToolLabel = createLabel(
            "GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel",
            "GM.EditDietaryAssessmentMethodPanel.dataCollectionToolTooltip", true)
    private val dataCollectionToolField = createAutoSuggestField(
            vocabs["Method. tool to collect data"] ?: emptySet())

    private val nonConsecutiveOneDayLabel = createLabel(
            "GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDaysLabel",
            "GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDaysTooltip", true)
    private val nonConsecutiveOneDayTextField = createTextField()

    private val dietarySoftwareToolLabel = createLabel(
            "GM.EditDietaryAssessmentMethodPanel.dietarySoftwareToolLabel",
            "GM.EditDietaryAssessmentMethodPanel.dietarySoftwareToolTooltip")
    private val dietarySoftwareToolTextField = createTextField()

    private val foodItemNumberLabel = createLabel(
            "GM.EditDietaryAssessmentMethodPanel.foodItemNumberLabel",
            "GM.EditDietaryAssessmentMethodPanel.foodItemNumberTooltip")
    private val foodItemNumberTextField = createTextField()

    private val recordTypeLabel = createLabel(
            "GM.EditDietaryAssessmentMethodPanel.recordTypeLabel",
            "GM.EditDietaryAssessmentMethodPanel.recordTypeTooltip")
    private val recordTypeTextField = createTextField()

    private val foodDescriptionLabel = createLabel(
            "GM.EditDietaryAssessmentMethodPanel.foodDescriptionLabel",
            "GM.EditDietaryAssessmentMethodPanel.foodDescriptionTooltip")
    private val foodDescriptionComboBox = createComboBox(vocabs["Food descriptors"] ?: emptySet())

    init {

        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(dataCollectionToolLabel, dataCollectionToolField), // data collection tool
                Pair(nonConsecutiveOneDayLabel, nonConsecutiveOneDayTextField), // non consecutive one day
                Pair(dietarySoftwareToolLabel, dietarySoftwareToolTextField), // dietary software tool
                Pair(foodItemNumberLabel, foodItemNumberTextField), // food item number
                Pair(recordTypeLabel, recordTypeTextField), // record type
                Pair(foodDescriptionLabel, foodDescriptionComboBox))  // food description

        addGridComponents(pairs)

        // If simple mode, hide advanced components
        if (!isAdvanced) {
            getAdvancedComponents().forEach { it.isVisible = false }
        }
    }

    override fun init(t: DietaryAssessmentMethod) {

        dataCollectionToolField.selectedItem = t.collectionTool
        nonConsecutiveOneDayTextField.text = t.numberOfNonConsecutiveOneDay.toString()
        dietarySoftwareToolTextField.text = t.softwareTool
        foodItemNumberTextField.text = t.numberOfFoodItems[0]
        recordTypeTextField.text = t.recordTypes[0]
        foodDescriptionComboBox.selectedItem = t.foodDescriptors[0]
    }

    override fun get(): DietaryAssessmentMethod {
        val method = DietaryAssessmentMethod()
        method.collectionTool = dataCollectionToolField.selectedItem as String
        method.numberOfNonConsecutiveOneDay = try {
            nonConsecutiveOneDayTextField.text.toInt()
        } catch (exception: NumberFormatException) {
            LOGGER.warn("numberOfNonConsecutiveOneDay", exception)
            0
        }
        method.softwareTool = dietarySoftwareToolTextField.text
        method.numberOfFoodItems.add(foodItemNumberTextField.text)
        method.recordTypes.add(recordTypeTextField.text)
        method.foodDescriptors.addAll(foodDescriptionComboBox.selectedObjects.filterNotNull().map { it as String })

        return method
    }

    override fun validatePanel(): List<String> {
        val errors = mutableListOf<String>()

        if (!dataCollectionToolField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditDietaryAssessmentMethodPanel.dataCollectionToolLabel")}")
        }

        if (!nonConsecutiveOneDayTextField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditDietaryAssessmentMethodPanel.nonConsecutiveOneDaysLabel")}")
        }

        return errors
    }

    override fun getAdvancedComponents(): List<JComponent> {
        return listOf(dietarySoftwareToolLabel, dietarySoftwareToolTextField,
                foodItemNumberLabel, foodItemNumberTextField, recordTypeLabel, recordTypeTextField,
                foodDescriptionLabel, foodDescriptionComboBox)
    }
}

private class EditHazardPanel(isAdvanced: Boolean) : EditPanel<Hazard>() {

    private val hazardTypeLabel = createLabel("GM.EditHazardPanel.hazardTypeLabel",
            "GM.EditHazardPanel.hazardTypeTooltip", true)
    private val hazardTypeField = createAutoSuggestField(vocabs["Hazard type"] ?: emptySet())

    private val hazardNameLabel = createLabel("GM.EditHazardPanel.hazardNameLabel",
            "GM.EditHazardPanel.hazardNameTooltip", true)
    private val hazardNameField = createAutoSuggestField(vocabs["Hazard name"] ?: emptySet())

    private val hazardDescriptionLabel = createLabel("GM.EditHazardPanel.hazardDescriptionLabel",
            "GM.EditHazardPanel.hazardDescriptionTooltip")
    private val hazardDescriptionTextArea = createTextArea()
    private val hazardDescriptionPanel = JScrollPane(hazardDescriptionTextArea)

    private val hazardUnitLabel = createLabel("GM.EditHazardPanel.hazardUnitLabel",
            "GM.EditHazardPanel.hazardUnitTooltip", true)
    private val hazardUnitField = createAutoSuggestField(vocabs["Hazard unit"] ?: emptySet())

    private val adverseEffectLabel = createLabel("GM.EditHazardPanel.adverseEffectLabel",
            "GM.EditHazardPanel.adverseEffectTooltip")
    private val adverseEffectTextField = createTextField()

    private val originLabel = createLabel("GM.EditHazardPanel.originLabel", "GM.EditHazardPanel.originTooltip")
    private val originTextField = createTextField()

    private val bmdLabel = createLabel("GM.EditHazardPanel.bmdLabel", "GM.EditHazardPanel.bmdTooltip")
    private val bmdTextField = createTextField()

    private val maxResidueLimitLabel = createLabel("GM.EditHazardPanel.maxResidueLimitLabel",
            "GM.EditHazardPanel.maxResidueLimitTooltip")
    private val maxResidueLimitTextField = createTextField()

    private val noObservedAdverseLabel = createLabel("GM.EditHazardPanel.noObservedAdverseLabel",
            "GM.EditHazardPanel.noObservedAdverseTooltip")
    private val noObservedAdverseTextField = createTextField()

    private val acceptableOperatorLabel = createLabel("GM.EditHazardPanel.acceptableOperatorLabel",
            "GM.EditHazardPanel.acceptableOperatorTooltip")
    private val acceptableOperatorTextField = createTextField()

    private val acuteReferenceDoseLabel = createLabel("GM.EditHazardPanel.acuteReferenceDoseLabel",
            "GM.EditHazardPanel.acuteReferenceDoseTooltip")
    private val acuteReferenceDoseTextField = createTextField()

    private val acceptableDailyIntakeLabel = createLabel("GM.EditHazardPanel.acceptableDailyIntakeLabel",
            "GM.EditHazardPanel.acceptableDailyIntakeTooltip")
    private val acceptableDailyIntakeTextField = createTextField()

    private val indSumLabel = createLabel("GM.EditHazardPanel.indSumLabel", "GM.EditHazardPanel.indSumTooltip")
    private val indSumField = createAutoSuggestField(vocabs["Hazard ind sum"] ?: emptySet())

    private val labNameLabel = createLabel("GM.EditHazardPanel.labNameLabel", "GM.EditHazardPanel.labNameTooltip")
    private val labNameTextField = createTextField()

    private val labCountryLabel = createLabel("GM.EditHazardPanel.labCountryLabel", "GM.EditHazardPanel.labCountryTooltip")
    private val labCountryField = createAutoSuggestField(vocabs["Laboratory country"] ?: emptySet())

    private val detectionLimitLabel = createLabel("GM.EditHazardPanel.detectionLimitLabel",
            "GM.EditHazardPanel.detectionLimitTooltip")
    private val detectionLimitTextField = createTextField()

    private val quantificationLimitLabel = createLabel("GM.EditHazardPanel.quantificationLimitLabel",
            "GM.EditHazardPanel.quantificationLimitTooltip")
    private val quantificationLimitTextField = createTextField()

    private val leftCensoredDataLabel = createLabel("GM.EditHazardPanel.leftCensoredDataLabel",
            "GM.EditHazardPanel.leftCensoredDataTooltip")
    private val leftCensoredDataTextField = createTextField()

    private val contaminationRangeLabel = createLabel("GM.EditHazardPanel.contaminationRangeLabel",
            "GM.EditHazardPanel.contaminationRangeTooltip")
    private val contaminationRangeTextField = createTextField()

    init {
        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(hazardTypeLabel, hazardTypeField), // hazard type
                Pair(hazardNameLabel, hazardNameField), // hazard name
                Pair(hazardDescriptionLabel, hazardDescriptionPanel), // hazard description
                Pair(hazardUnitLabel, hazardUnitField), // hazard unit
                Pair(adverseEffectLabel, adverseEffectTextField), // adverse effect
                Pair(originLabel, originTextField), Pair(bmdLabel, bmdTextField), // BMI
                Pair(maxResidueLimitLabel, maxResidueLimitTextField), // maximum residue limit
                Pair(noObservedAdverseLabel, noObservedAdverseTextField), // no. observed adverse
                Pair(acceptableOperatorLabel, acceptableOperatorTextField), // acceptable operator
                Pair(acuteReferenceDoseLabel, acuteReferenceDoseTextField), // acute reference dose
                Pair(indSumLabel, indSumField), // ind sum
                Pair(acceptableDailyIntakeLabel, acceptableDailyIntakeTextField), // acceptable daily intake
                Pair(labNameLabel, labNameTextField), // laboratory name
                Pair(labCountryLabel, labCountryField), // laboratory country
                Pair(detectionLimitLabel, detectionLimitTextField), // detection limit
                Pair(quantificationLimitLabel, quantificationLimitTextField), // quantification limit
                Pair(leftCensoredDataLabel, leftCensoredDataTextField), // left censored data
                Pair(contaminationRangeLabel, contaminationRangeTextField))  // contamination range

        addGridComponents(pairs)

        // If simple mode hide advanced components
        if (!isAdvanced) {
            getAdvancedComponents().forEach { it.isVisible = false }
        }
    }

    override fun init(t: Hazard) {
        hazardTypeField.selectedItem = t.hazardType
        hazardNameField.selectedItem = t.hazardName
        hazardDescriptionTextArea.text = t.hazardDescription
        hazardUnitField.selectedItem = t.hazardUnit
        adverseEffectTextField.text = t.adverseEffect
        originTextField.text = t.origin
        bmdTextField.text = t.benchmarkDose
        maxResidueLimitTextField.text = t.maximumResidueLimit
        noObservedAdverseTextField.text = t.noObservedAdverse
        acceptableOperatorTextField.text = t.acceptableOperator
        acuteReferenceDoseTextField.text = t.acuteReferenceDose
        indSumField.selectedItem = t.hazardIndSum
        acceptableDailyIntakeTextField.text = t.acceptableDailyIntake
        labNameTextField.text = t.laboratoryName
        labCountryField.selectedItem = t.laboratoryCountry
        detectionLimitTextField.text = t.detectionLimit
        quantificationLimitTextField.text = t.quantificationLimit
        leftCensoredDataTextField.text = t.leftCensoredData
        contaminationRangeTextField.text = t.contaminationRange
    }

    override fun get(): Hazard {
        val hazard = Hazard()
        hazard.hazardType = hazardTypeField.selectedItem as? String ?: ""
        hazard.hazardName = hazardNameField.selectedItem as? String ?: ""
        hazard.hazardUnit = hazardUnitField.selectedItem as? String ?: ""
        hazard.hazardDescription = hazardDescriptionTextArea.text
        hazard.adverseEffect = adverseEffectTextField.text
        hazard.origin = originTextField.text
        hazard.benchmarkDose = bmdTextField.text
        hazard.maximumResidueLimit = maxResidueLimitTextField.text
        hazard.noObservedAdverse = noObservedAdverseTextField.text
        hazard.acceptableOperator = acceptableOperatorTextField.text
        hazard.acuteReferenceDose = acuteReferenceDoseTextField.text
        hazard.acceptableDailyIntake = acceptableDailyIntakeTextField.text
        hazard.hazardIndSum = indSumField.selectedItem as String
        hazard.laboratoryName = labNameTextField.text
        hazard.laboratoryCountry = labCountryField.selectedItem as String
        hazard.detectionLimit = detectionLimitTextField.text
        hazard.leftCensoredData = leftCensoredDataTextField.text
        hazard.contaminationRange = contaminationRangeTextField.text

        return hazard
    }

    override fun validatePanel(): List<String> {
        val errors = mutableListOf<String>()
        if (!hazardTypeField.hasValidValue()) errors.add("Missing ${bundle.getString("GM.EditHazardPanel.hazardTypeLabel")}")
        if (!hazardNameField.hasValidValue()) errors.add("Missing ${bundle.getString("GM.EditHazardPanel.hazardNameLabel")}")
        if (!hazardUnitField.hasValidValue()) errors.add("Missing ${bundle.getString("GM.EditHazardPanel.hazardUnitLabel")}")
        return errors
    }

    override fun getAdvancedComponents(): List<JComponent> {
        return listOf(hazardDescriptionLabel, hazardDescriptionPanel, // hazard description
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
                contaminationRangeLabel, contaminationRangeTextField) // contamination range)
    }
}

private class EditModelEquationPanel(isAdvanced: Boolean) : EditPanel<ModelEquation>() {

    private val equationNameLabel = createLabel("GM.EditModelEquationPanel.nameLabel",
            "GM.EditModelEquationPanel.nameTooltip", true)
    private val equationNameTextField = createTextField()

    private val equationClassLabel = createLabel("GM.EditModelEquationPanel.classLabel",
            "GM.EditModelEquationPanel.classTooltip")
    private val equationClassTextField = createTextField()

    private val referencePanel = ReferencePanel(isAdvanced)

    private val scriptLabel = createLabel("GM.EditModelEquationPanel.scriptLabel", "GM.EditModelEquationPanel.scriptTooltip", true)
    private val scriptTextArea = createTextArea()
    private val scriptPane = JScrollPane(scriptTextArea)

    init {
        add(equationNameLabel, 0, 0)
        add(equationNameTextField, 0, 1)
        add(equationClassLabel, 1, 0)
        add(equationClassTextField, 1, 1)
        add(referencePanel, 2)
        add(scriptLabel, 3, 0)
        add(scriptPane, 3, 1)

        // If simple mode hide advanced components
        if (!isAdvanced) {
            getAdvancedComponents().forEach { it.isVisible = false }
        }
    }

    override fun init(t: ModelEquation) {
        equationNameTextField.text = t.equationName
        equationClassTextField.text = t.equationClass
        referencePanel.init(t.equationReference)
        scriptTextArea.text = t.equation
    }

    override fun validatePanel(): List<String> {
        val errors = mutableListOf<String>()
        if (!equationNameTextField.hasValidValue()) errors.add("Missing ${bundle.getString("GM.EditModelEquationPanel.nameLabel")}")
        if (!scriptTextArea.hasValidValue()) errors.add("Missing ${bundle.getString("GM.EditModelEquationPanel.scriptLabel")}")

        return errors
    }

    override fun get(): ModelEquation {
        val modelEquation = ModelEquation()
        modelEquation.equationName = equationNameTextField.text
        modelEquation.equation = scriptTextArea.text
        modelEquation.equationClass = equationClassTextField.text
        modelEquation.equationReference.addAll(referencePanel.refs)

        return modelEquation
    }

    override fun getAdvancedComponents(): List<JComponent> {
        return listOf(equationClassLabel, equationClassTextField,
                referencePanel)
    }
}

private class EditParameterPanel(isAdvanced: Boolean) : EditPanel<Parameter>() {

    private val idLabel = createLabel(
            "GM.EditParameterPanel.idLabel",
            "GM.EditParameterPanel.idTooltip", true)
    private val idTextField = createTextField()

    private val classificationLabel = createLabel(
            "GM.EditParameterPanel.classificationLabel",
            "GM.EditParameterPanel.classificationTooltip", true)
    private val classificationComboBox = JComboBox<Parameter.Classification>(Parameter.Classification.values())

    private val nameLabel = createLabel(
            "GM.EditParameterPanel.parameterNameLabel",
            "GM.EditParameterPanel.parameterNameTooltip", true)
    private val nameTextField = createTextField()

    private val descriptionLabel = createLabel(
            "GM.EditParameterPanel.descriptionLabel",
            "GM.EditParameterPanel.descriptionTooltip")
    private val descriptionTextArea = createTextArea()
    private val descriptionPane = JScrollPane(descriptionTextArea)

    private val typeLabel = createLabel(
            "GM.EditParameterPanel.typeLabel",
            "GM.EditParameterPanel.typeTooltip")
    private val typeField = createAutoSuggestField(
            vocabs["Parameter type"] ?: emptySet())

    private val unitLabel = createLabel(
            "GM.EditParameterPanel.unitLabel",
            "GM.EditParameterPanel.unitTooltip", true)
    private val unitField = createAutoSuggestField(
            vocabs["Parameter unit"] ?: emptySet())

    private val unitCategoryLabel = createLabel(
            "GM.EditParameterPanel.unitCategoryLabel",
            "GM.EditParameterPanel.unitCategoryTooltip", true)
    private val unitCategoryField = createAutoSuggestField(
            vocabs["Parameter unit category"] ?: emptySet())

    private val dataTypeLabel = createLabel(
            "GM.EditParameterPanel.dataTypeLabel",
            "GM.EditParameterPanel.dataTypeTooltip", true)
    private val dataTypeField = createAutoSuggestField(
            vocabs["Parameter data type"] ?: emptySet())

    private val sourceLabel = createLabel(
            "GM.EditParameterPanel.subjectLabel",
            "GM.EditParameterPanel.subjectTooltip")
    private val sourceField = createAutoSuggestField(
            vocabs["Parameter source"] ?: emptySet())

    private val subjectLabel = createLabel(
            "GM.EditParameterPanel.distributionLabel",
            "GM.EditParameterPanel.distributionTooltip")
    private val subjectField = createAutoSuggestField(
            vocabs["Parameter subject"] ?: emptySet())

    private val distributionLabel = createLabel(
            "GM.EditParameterPanel.valueLabel",
            "GM.EditParameterPanel.valueTooltip")
    private val distributionField = createAutoSuggestField(
            vocabs["Parameter distribution"] ?: emptySet())

    private val valueLabel = createLabel(
            "GM.EditParameterPanel.valueLabel",
            "GM.EditParameterPanel.valueTooltip")
    private val valueTextField = createTextField()

    private val referenceLabel = createLabel(
            "GM.EditParameterPanel.referenceLabel",
            "GM.EditParameterPanel.referenceTooltip")
    private val referenceTextField = createTextField()

    private val variabilitySubjectLabel = createLabel(
            "GM.EditParameterPanel.variabilitySubjectLabel",
            "GM.EditParameterPanel.variabilitySubjectTooltip")
    private val variabilitySubjectTextArea = createTextArea()
    private val variabilitySubjectPane = JScrollPane(variabilitySubjectTextArea)

    private val applicabilityLabel = createLabel(
            "GM.EditParameterPanel.applicabilityLabel",
            "GM.EditParameterPanel.applicabilityTooltip")
    private val applicabilityTextArea = createTextArea()
    private val applicabilityPane = JScrollPane(applicabilityTextArea)

    private val errorLabel = createLabel(
            "GM.EditParameterPanel.errorLabel",
            "GM.EditParameterPanel.errorTooltip")
    private val errorSpinnerModel = createSpinnerDoubleModel()
    private val errorSpinner = createSpinner(errorSpinnerModel)

    init {
        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(idLabel, idTextField), // id
                Pair(classificationLabel, classificationComboBox), // classification
                Pair(nameLabel, nameTextField), // name
                Pair(descriptionLabel, descriptionPane), // description
                Pair(typeLabel, typeField), // type
                Pair(unitCategoryLabel, unitCategoryField), // unit category
                Pair(unitLabel, unitField), // unit
                Pair(dataTypeLabel, dataTypeField), // data type
                Pair(sourceLabel, sourceField), // source
                Pair(subjectLabel, subjectField), // subject
                Pair(distributionLabel, distributionField), // distribution
                Pair(valueLabel, valueTextField), // value
                Pair(referenceLabel, referenceLabel), // reference
                Pair(variabilitySubjectLabel, variabilitySubjectPane), // variability subject
                Pair(applicabilityLabel, applicabilityPane), // applicability
                Pair(errorLabel, errorSpinner)) // error
        addGridComponents(pairs)

        // If simple mode hide advanced components
        if (!isAdvanced) {
            getAdvancedComponents().forEach { it.isVisible = false }
        }
    }

    override fun init(t: Parameter) {
        idTextField.text = t.id
        classificationComboBox.selectedItem = t.classification
        nameTextField.text = t.parameterName
        descriptionTextArea.text = t.description
        // TODO: typeField
        unitField.selectedItem = t.unit
        unitCategoryField.selectedItem = t.unitCategory
        dataTypeField.selectedItem = t.dataType
        sourceField.selectedItem = t.dataType
        subjectField.selectedItem = t.subject
        distributionField.selectedItem = t.distribution
        valueTextField.text = t.value
        referenceTextField.text = t.reference
        variabilitySubjectTextArea.text = t.variabilitySubject
        // TODO: fix model applicability
        errorSpinnerModel.value = t.error
    }

    override fun get(): Parameter {
        val param = Parameter(classification = classificationComboBox.selectedItem as Parameter.Classification)
        param.id = idTextField.text
        param.parameterName = nameTextField.text
        param.unit = unitField.selectedItem as String
        param.unitCategory = unitCategoryField.selectedItem as String
        param.dataType = dataTypeField.selectedItem as String
        // TODO: model applicability

        param.description = descriptionTextArea.text
        param.source = sourceField.selectedItem as String
        param.subject = subjectField.selectedItem as String
        param.distribution = distributionField.selectedItem as String
        param.value = valueTextField.text
        param.reference = referenceTextField.text
        param.variabilitySubject = variabilitySubjectTextArea.text
        param.error = errorSpinnerModel.number.toDouble()

        return param
    }

    override fun validatePanel(): List<String> {

        val errors = mutableListOf<String>()
        if (!idTextField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditParameterPanel.idLabel")}")
        }
        if (classificationComboBox.selectedIndex == -1) {
            errors.add("Missing ${bundle.getString("GM.EditParameterPanel.classificationLabel")}")
        }
        if (!nameTextField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditParameterPanel.parameterNameLabel")}")
        }
        if (!unitField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditParameterPanel.unitLabel")}")
        }
        if (!unitCategoryField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditParameterPanel.unitCategoryLabel")}")
        }
        if (!dataTypeField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditParameterPanel.dataTypeLabel")}")
        }

        return errors
    }

    override fun getAdvancedComponents(): List<JComponent> {
        return Arrays.asList(descriptionLabel, descriptionTextArea, // description
                typeLabel, typeField, // type
                sourceLabel, sourceField, // source
                subjectLabel, subjectField, // subject
                distributionLabel, distributionField, // distribution
                valueLabel, valueTextField, // value
                referenceLabel, referenceTextField, // reference
                variabilitySubjectLabel, variabilitySubjectTextArea, // variability subject
                applicabilityLabel, applicabilityTextArea, // applicability
                errorLabel, errorSpinner) // error
    }
}

private class EditProductPanel(isAdvanced: Boolean) : EditPanel<Product>() {

    private val envNameLabel: JLabel = createLabel(
            "GM.EditProductPanel.envNameLabel",
            "GM.EditProductPanel.envNameTooltip", true)
    private val envNameField: AutoSuggestField = createAutoSuggestField(
            vocabs["Product-matrix name"] ?: emptySet())

    private val envDescriptionLabel: JLabel = createLabel(
            "GM.EditProductPanel.envDescriptionLabel",
            "GM.EditProductPanel.envDescriptionTooltip")
    private val envDescriptionTextArea: JTextArea = createTextArea()
    private val envDescriptionPane = JScrollPane(envDescriptionTextArea)

    private val envUnitLabel = createLabel(
            "GM.EditProductPanel.envUnitLabel",
            "GM.EditProductPanel.envUnitTooltip", true)
    private val envUnitField: AutoSuggestField = createAutoSuggestField(
            vocabs["Product-matrix unit"] ?: emptySet())

    private val productionMethodLabel = createLabel(
            "GM.EditProductPanel.productionMethodLabel",
            "GM.EditProductPanel.productionMethodTooltip")
    private val productionMethodComboBox = createComboBox(
            vocabs["Method of production"] ?: emptySet())

    private val packagingLabel: JLabel = createLabel(
            "GM.EditProductPanel.packagingLabel",
            "GM.EditProductPanel.packagingTooltip")
    private val packagingComboBox = createComboBox(
            vocabs["Packaging"] ?: emptySet())

    private val productTreatmentLabel = createLabel(
            "GM.EditProductPanel.productTreatmentLabel",
            "GM.EditProductPanel.productTreatmentTooltip")
    private val productTreatmentComboBox = createComboBox(
            vocabs["Product treatment"] ?: emptySet())

    private val originCountryLabel = createLabel(
            "GM.EditProductPanel.originCountryLabel",
            "GM.EditProductPanel.originCountryTooltip")
    private val originCountryField = createAutoSuggestField(
            vocabs["Country of origin"] ?: emptySet())

    private val originAreaLabel = createLabel(
            "GM.EditProductPanel.originAreaLabel",
            "GM.EditProductPanel.originAreaTooltip")
    private val originAreaField = createAutoSuggestField(
            vocabs["Area of origin"] ?: emptySet())

    private val fisheriesAreaLabel: JLabel = createLabel(
            "GM.EditProductPanel.fisheriesAreaLabel",
            "GM.EditProductPanel.fisheriesAreaTooltip")
    private val fisheriesAreaField = createAutoSuggestField(
            vocabs["Fisheries area"] ?: emptySet())

    private val productionDateLabel = createLabel(
            "GM.EditProductPanel.productionDateLabel",
            "GM.EditProductPanel.productionDateTooltip")
    private val productionDateChooser = FixedDateChooser()

    private val expirationDateLabel = createLabel(
            "GM.EditProductPanel.expirationDateLabel",
            "GM.EditProductPanel.expirationDateTooltip")
    private val expirationDateChooser = FixedDateChooser()

    init {

        // Build UI
        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(envNameLabel, envNameField), // environment name
                Pair(envDescriptionLabel, envDescriptionPane), // environment description
                Pair(envUnitLabel, envUnitField), // environment unit
                Pair(productionMethodLabel, productionMethodComboBox), // production method
                Pair(packagingLabel, packagingComboBox), // packaging
                Pair(productTreatmentLabel, productTreatmentComboBox), // product treatment
                Pair(originCountryLabel, originCountryField), // origin country
                Pair(originAreaLabel, originAreaField), // origin area
                Pair(fisheriesAreaLabel, fisheriesAreaField), // fisheries area
                Pair(productionDateLabel, productionDateChooser), // production date
                Pair(expirationDateLabel, expirationDateChooser)) // expiration date
        addGridComponents(pairs)

        // If simple mode hides the advanced components
        if (!isAdvanced) {
            getAdvancedComponents().forEach { it -> it.isVisible = false }
        }
    }

    override fun init(t: Product) {

        envNameField.selectedItem = t.environmentName
        envDescriptionTextArea.text = t.environmentDescription
        envUnitField.selectedItem = t.environmentUnit
        // TODO: productonMethodComboBox
        // TODO: packagingComboBox
        // TODO: productTreatmentComboBox
        originCountryField.selectedItem = t.originCountry
        originAreaField.selectedItem = t.originArea
        fisheriesAreaField.selectedItem = t.fisheriesArea
        productionDateChooser.date = t.productionDate
        expirationDateChooser.date = t.expirationDate
    }

    override fun get(): Product {

        val product = Product()
        product.environmentName = envNameField.selectedItem as String
        product.environmentDescription = envDescriptionTextArea.text
        product.environmentUnit = envUnitField.selectedItem as String
        product.productionMethod.addAll(
                productionMethodComboBox.selectedObjects.map { it as String })
        product.packaging.addAll(
                packagingComboBox.selectedObjects.map { it as String })
        product.productTreatment.addAll(
                productTreatmentComboBox.selectedObjects.map { it as String })
        product.originCountry = originCountryField.selectedItem as? String ?: ""
        product.originArea = originAreaField.selectedItem as? String ?: ""
        product.fisheriesArea = fisheriesAreaField.selectedItem as? String ?: ""
        product.productionDate = productionDateChooser.date
        product.expirationDate = expirationDateChooser.date

        return product
    }

    override fun validatePanel(): List<String> {

        val errors = mutableListOf<String>()
        if (!envNameField.hasValidValue())
            errors.add("Missing ${bundle.getString("GM.EditProductPanel.envNameLabel")}")
        if (!envUnitField.hasValidValue())
            errors.add("Missing ${bundle.getString("GM.EditProductPanel.envUnitLabel")}")

        return errors
    }

    override fun getAdvancedComponents(): List<JComponent> {
        return listOf(
                envDescriptionLabel, envDescriptionPane, // environment description
                productionMethodLabel, productionMethodComboBox, // production method
                packagingLabel, packagingComboBox, // packaing
                productTreatmentLabel, productTreatmentComboBox, // product treatment
                originCountryLabel, originCountryField, // origin country
                originAreaLabel, originAreaField, // origin area
                fisheriesAreaLabel, fisheriesAreaField, // fisheries area
                productionDateLabel, productionDateChooser, // production date
                expirationDateLabel, expirationDateChooser)  // expiration date
    }
}

// TODO: EditPopulationGroupPanel
private class EditPopulationGroupPanel(isAdvanced: Boolean) : EditPanel<PopulationGroup>() {

    private val populationNameLabel = createLabel(
            "GM.EditPopulationGroupPanel.populationNameLabel",
            "GM.EditPopulationGroupPanel.populationNameTooltip", true)
    private val populationNameTextField = createTextField()

    private val targetPopulationLabel = createLabel(
            "GM.EditPopulationGroupPanel.targetPopulationLabel",
            "GM.EditPopulationGroupPanel.targetPopulationTooltip")
    private val targetPopulationTextField = createTextField()

    private val populationSpanLabel = createLabel(
            "GM.EditPopulationGroupPanel.populationSpanLabel",
            "GM.EditPopulationGroupPanel.populationSpanTooltip")
    private val populationSpanTextField = createTextField()

    private val populationDescriptionLabel = createLabel(
            "GM.EditPopulationGroupPanel.populationDescriptionLabel",
            "GM.EditPopulationGroupPanel.populationDescriptionTooltip")
    private val populationDescriptionTextArea = createTextArea()
    private val populationDescriptionPane = JScrollPane(populationDescriptionTextArea)

    private val populationAgeLabel = createLabel(
            "GM.EditPopulationGroupPanel.populationAgeLabel",
            "GM.EditPopulationGroupPanel.populationAgeTooltip")
    private val populationAgeTextField = createTextField()

    private val populationGenderLabel = createLabel(
            "GM.EditPopulationGroupPanel.populationGenderLabel",
            "GM.EditPopulationGroupPanel.populationGenderTooltip")
    private val populationGenderTextField = createTextField()

    private val bmiLabel = createLabel(
            "GM.EditPopulationGroupPanel.bmiLabel",
            "GM.EditPopulationGroupPanel.bmiTooltip")
    private val bmiTextField = createTextField()

    private val specialDietGroupLabel = createLabel(
            "GM.EditPopulationGroupPanel.specialDietGroupsLabel",
            "GM.EditPopulationGroupPanel.specialDietGroupsTooltip")
    private val specialDietGroupTextField = createTextField()

    private val patternConsumptionLabel = createLabel(
            "GM.EditPopulationGroupPanel.patternConsumptionLabel",
            "GM.EditPopulationGroupPanel.patternConsumptionTooltip")
    private val patternConsumptionTextField = createTextField()

    private val regionLabel = createLabel(
            "GM.EditPopulationGroupPanel.regionLabel",
            "GM.EditPopulationGroupPanel.regionTooltip")
    private val regionComboBox = createComboBox(vocabs["Country"] ?: emptySet())

    private val countryLabel = createLabel(
            "GM.EditPopulationGroupPanel.countryLabel",
            "GM.EditPopulationGroupPanel.countryTooltip")
    private val countryComboBox = createComboBox(vocabs["Country"] ?: emptySet())

    private val riskLabel = createLabel(
            "GM.EditPopulationGroupPanel.riskAndPopulationLabel",
            "GM.EditPopulationGroupPanel.riskAndPopulationTooltip")
    private val riskTextField = createTextField()

    private val seasonLabel = createLabel(
            "GM.EditPopulationGroupPanel.seasonLabel",
            "GM.EditPopulationGroupPanel.seasonTooltip")
    private val seasonTextField = createTextField()

    init {

        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(populationNameLabel, populationNameTextField), // population name
                Pair(targetPopulationLabel, targetPopulationTextField), // target population
                Pair(populationSpanLabel, populationSpanTextField), // population span
                Pair(populationDescriptionLabel, populationDescriptionPane), // population description
                Pair(populationAgeLabel, populationAgeTextField), // population age
                Pair(populationGenderLabel, populationGenderTextField), // population gender
                Pair(bmiLabel, bmiTextField), // BMI
                Pair(specialDietGroupLabel, specialDietGroupTextField), // special diet group
                Pair(patternConsumptionLabel, patternConsumptionTextField), // pattern consumption
                Pair(regionLabel, regionComboBox), // region
                Pair(countryLabel, countryComboBox), // country
                Pair(riskLabel, riskTextField), // risk
                Pair(seasonLabel, seasonTextField) // season
        )
        addGridComponents(pairs)

        // If simple mode hide advanced components
        if (!isAdvanced) {
            getAdvancedComponents().forEach { it.isVisible = false }
        }
    }

    override fun init(t: PopulationGroup) {
        populationNameTextField.text = t.populationName
        targetPopulationTextField.text = t.targetPopulation
        populationSpanTextField.text = t.populationSpan[0]
        populationDescriptionTextArea.text = t.populationDescription[0]
        populationAgeTextField.text = t.populationAge[0]
        populationGenderTextField.text = t.populationGender
        bmiTextField.text = t.bmi[0]
        specialDietGroupTextField.text = t.specialDietGroups[0]
        patternConsumptionTextField.text = t.patternConsumption[0]
        regionComboBox.selectedItem = t.region
        countryComboBox.selectedItem = t.country
        riskTextField.text = t.populationRiskFactor[0]
        seasonTextField.text = t.season[0]
    }

    override fun get(): PopulationGroup {
        val populationGroup = PopulationGroup()
        populationGroup.populationName = populationNameTextField.text
        populationGroup.targetPopulation = targetPopulationTextField.text
        populationGroup.populationSpan.add(populationSpanTextField.text)
        populationGroup.populationDescription.add(populationDescriptionTextArea.text)
        populationGroup.populationAge.add(populationAgeTextField.text)
        populationGroup.populationGender = populationGenderTextField.text
        populationGroup.bmi.add(bmiTextField.text)
        populationGroup.specialDietGroups.add(specialDietGroupTextField.text)
        populationGroup.patternConsumption.add(patternConsumptionTextField.text)
        populationGroup.region.add(regionComboBox.selectedItem as String)
        populationGroup.country.add(countryComboBox.selectedItem as String)
        populationGroup.populationRiskFactor.add(riskTextField.text)
        populationGroup.season.add(seasonTextField.text)

        return populationGroup
    }

    override fun validatePanel(): List<String> {
        val errors = mutableListOf<String>()
        if (!populationNameTextField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditPopulationGroupPanel.populationNameLabel")}")
        }

        return errors
    }

    override fun getAdvancedComponents(): List<JComponent> {
        return listOf(
                targetPopulationLabel, targetPopulationTextField, // Target population
                populationSpanLabel, populationSpanTextField, // Population span
                // Population description
                populationDescriptionLabel, populationDescriptionPane,
                populationAgeLabel, populationAgeTextField, // Population age
                populationGenderLabel, populationGenderTextField, // Population gender
                bmiLabel, bmiTextField, // BMI
                specialDietGroupLabel, specialDietGroupTextField, // Special diet group
                patternConsumptionLabel, patternConsumptionTextField, // Pattern consumption
                regionLabel, regionComboBox, // Region
                countryLabel, countryComboBox, // Country
                riskLabel, riskTextField, // Risk
                seasonLabel, seasonTextField) // Season
    }
}

private class EditReferencePanel(isAdvanced: Boolean) : EditPanel<Record>() {

    private val dateFormatStr = "yyyy-MM-dd"

    private val isReferenceDescriptionCheckBox = JCheckBox("Is reference description *")

    private val typeLabel = createLabel("GM.EditReferencePanel.typeLabel")
    private val typeComboBox = createComboBox(referenceTypeLabels.values)

    private val dateLabel = createLabel("GM.EditReferencePanel.dateLabel")
    private val dateChooser = FixedDateChooser()

    private val pmidLabel = createLabel("GM.EditReferencePanel.doiLabel",
            isMandatory = true)
    private val pmidTextField = createTextField()

    private val doiLabel = createLabel("GM.EditReferencePanel.authorListLabel")
    private val doiTextField = createTextField()

    private val authorListLabel = createLabel("GM.EditReferencePanel.authorListLabel")
    private val authorListTextField = createTextField()

    private val titleLabel = createLabel("GM.EditReferencePanel.titleLabel",
            isMandatory = true)
    private val titleTextField = createTextField()

    private val abstractLabel = createLabel("GM.EditReferencePanel.abstractLabel")
    private val abstractTextArea = createTextArea()
    private val abstractPane = JScrollPane(abstractTextArea)

    private val journalLabel = createLabel("GM.EditReferencePanel.journalLabel")
    private val journalTextField = createTextField()

    private val volumeLabel = createLabel("GM.EditReferencePanel.volumeLabel")
    // Create integer spinner model starting with 0 and taking only positive ints
    private val volumeSpinnerModel = SpinnerNumberModel(0, 0, null, 1)
    private val volumeSpinner = createSpinner(volumeSpinnerModel)

    private val issueLabel = createLabel("GM.EditReferencePanel.issueLabel")
    // Create integer spinner model starting with 0 and taking only positive ints
    private val issueSpinnerModel = SpinnerNumberModel(0, 0, null, 1)
    private val issueSpinner = createSpinner(issueSpinnerModel)

    private val pageLabel = createLabel("GM.EditReferencePanel.pageLabel")
    private val pageTextField = createTextField()

    private val statusLabel = createLabel("GM.EditReferencePanel.statusLabel")
    private val statusTextField = createTextField()

    private val websiteLabel = createLabel("GM.EditReferencePanel.websiteLabel")
    private val websiteTextField = createTextField()

    private val commentLabel = createLabel("GM.EditReferencePanel.commentLabel")
    private val commentTextArea = createTextArea()
    private val commentPane = JScrollPane(commentTextArea)

    init {
        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(typeLabel, typeComboBox), // Type
                Pair(dateLabel, dateChooser), // date
                Pair(pmidLabel, pmidTextField), // PMID
                Pair(doiLabel, doiTextField), // DOI
                Pair(authorListLabel, authorListTextField), // author list
                Pair(titleLabel, titleTextField), // title
                Pair(abstractLabel, abstractPane), // abstract
                Pair(journalLabel, journalTextField), // journal
                Pair(volumeLabel, volumeSpinner), // volume
                Pair(issueLabel, issueSpinner), // issue
                Pair(pageLabel, pageTextField), // page
                Pair(statusLabel, statusTextField), // status
                Pair(websiteLabel, websiteTextField), // website
                Pair(commentLabel, commentPane) // comment
        )

        var rowNumber = 0
        add(isReferenceDescriptionCheckBox, rowNumber, 0)
        for ((label, field) in pairs) {
            rowNumber++

            label.labelFor = field

            add(label, rowNumber, 0)
            add(field, rowNumber, 1)
        }

        // If simple mode hide advanced components
        if (!isAdvanced) {
            getAdvancedComponents().forEach { it.isVisible = false }
        }
    }

    override fun init(t: Record) {
        typeComboBox.selectedItem = referenceTypeLabels[t.type]

        try {
            val dateFormat = SimpleDateFormat(dateFormatStr)
            dateChooser.date = dateFormat.parse(t.date)
        } catch (exception: ParseException) {
            LOGGER.warn("Invalid date", exception)
        }

        doiTextField.text = t.doi
        authorListTextField.text = t.authors.joinToString(separator = ";")
        titleTextField.text = t.title
        abstractTextArea.text = t.abstr
        journalTextField.text = t.secondaryTitle

        volumeSpinnerModel.value = try {
            Integer.parseInt(t.volumeNumber)
        } catch (exception: NumberFormatException) {
            LOGGER.warn("Invalid volume number $exception")
            0
        }

        issueSpinnerModel.value = t.issueNumber
        websiteTextField.text = t.websiteLink
    }

    override fun get(): Record {

        val record = Record()

        // TODO: isReferenceDescriptionCheckBox

        if (typeComboBox.selectedIndex != -1) {
            record.type = referenceTypeLabels.keys.toTypedArray()[typeComboBox.selectedIndex]
        }

        dateChooser.date?.let { record.date = SimpleDateFormat(dateFormatStr).format(it) }

        record.doi = doiTextField.text

        record.authors.addAll(authorListTextField.text.split(";"))

        record.title = titleTextField.text
        record.abstr = abstractTextArea.text
        record.secondaryTitle = journalTextField.text
        record.volumeNumber = volumeSpinnerModel.number?.toString()
        record.issueNumber = issueSpinnerModel.number.toInt()
        // TODO: status
        record.websiteLink = websiteTextField.text
        // TODO: comment

        return record
    }

    override fun validatePanel(): List<String> {
        val errors = mutableListOf<String>()
        if (!doiTextField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditReferencePanel.doiLabel")}")
        }
        if (!titleTextField.hasValidValue()) {
            errors.add("Missing ${bundle.getString("GM.EditReferencePanel.titleLabel")}")
        }

        return errors
    }

    override fun getAdvancedComponents(): List<JComponent> {
        return listOf(typeLabel, typeComboBox, // type
                dateLabel, dateChooser, // date
                pmidLabel, pmidTextField, // pmid
                authorListLabel, authorListTextField, // author list
                abstractLabel, abstractPane, // abstract
                journalLabel, journalTextField, // journal
                volumeLabel, volumeSpinner, // volume
                issueLabel, issueSpinner, // issue
                pageLabel, pageTextField, // page
                statusLabel, statusTextField, // status
                websiteLabel, websiteTextField, // website
                commentLabel, commentPane)  // comment
    }
}

private val referenceTypeLabels = linkedMapOf(
        Type.ABST to "Abstract",
        Type.ADVS to "Audiovisual material",
        Type.AGGR to "Aggregated Database",
        Type.ANCIENT to "Ancient Text",
        Type.ART to "Art Work",
        Type.BILL to "Bill",
        Type.BLOG to "Blog",
        Type.BOOK to "Whole book",
        Type.CASE to "Case",
        Type.CHAP to "Book chapter",
        Type.CHART to "Chart",
        Type.CLSWK to "Classical Work",
        Type.COMP to "Computer program",
        Type.CONF to "Conference proceeding",
        Type.CPAPER to "Conference paper",
        Type.CTLG to "Catalog",
        Type.DATA to "Data file",
        Type.DBASE to "Online Database",
        Type.DICT to "Dictionary",
        Type.EBOOK to "Electronic Book",
        Type.ECHAP to "Electronic Book Section",
        Type.EDBOOK to "Edited Book",
        Type.EJOUR to "Electronic Article",
        Type.ELEC to "Web Page",
        Type.ENCYC to "Encyclopedia",
        Type.EQUA to "Equation",
        Type.FIGURE to "Figure",
        Type.GEN to "Generic",
        Type.GOVDOC to "Government Document",
        Type.GRANT to "Grant",
        Type.HEAR to "Hearing",
        Type.ICOMM to "Internet Communication",
        Type.INPR to "In Press",
        Type.JFULL to "Journal (full)",
        Type.JOUR to "Journal",
        Type.LEGAL to "Legal Rule or Regulation",
        Type.MANSCPT to "Manuscript",
        Type.MAP to "Map",
        Type.MGZN to "Magazine article",
        Type.MPCT to "Motion picture",
        Type.MULTI to "Online Multimedia",
        Type.MUSIC to "Music score",
        Type.NEWS to "Newspaper",
        Type.PAMP to "Pamphlet",
        Type.PAT to "Patent",
        Type.PCOMM to "Personal communication",
        Type.RPRT to "Report",
        Type.SER to "Serial publication",
        Type.SLIDE to "Slide",
        Type.SOUND to "Sound recording",
        Type.STAND to "Standard",
        Type.STAT to "Statute",
        Type.THES to "Thesis/Dissertation",
        Type.UNPB to "Unpublished work",
        Type.VIDEO to "Video recording"
)

private class EditStudySamplePanel(isAdvanced: Boolean) : EditPanel<StudySample>() {


    private val sampleNameLabel = createLabel(
            "GM.EditStudySamplePanel.sampleNameLabel",
            "GM.EditStudySamplePanel.sampleNameTooltip", true
    )
    private val sampleNameTextField = createTextField()

    private val moisturePercentageLabel = createLabel(
            "GM.EditStudySamplePanel.moisturePercentageLabel",
            "GM.EditStudySamplePanel.moisturePercentageTooltip")
    private val moisturePercentageSpinnerModel = createSpinnerPercentageModel()
    private val moisturePercentageSpinner = createSpinner(moisturePercentageSpinnerModel)

    private val fatPercentageLabel = createLabel(
            "GM.EditStudySamplePanel.fatPercentageLabel",
            "GM.EditStudySamplePanel.fatPercentageTooltip")
    private val fatPercentageSpinnerModel = createSpinnerPercentageModel()
    private val fatPercentageSpinner = createSpinner(fatPercentageSpinnerModel)

    private val sampleProtocolLabel = createLabel(
            "GM.EditStudySamplePanel.sampleProtocolLabel",
            "GM.EditStudySamplePanel.sampleProtocolTooltip", true)
    private val sampleProtocolTextField = createTextField()

    private val samplingStrategyLabel = createLabel(
            "GM.EditStudySamplePanel.samplingStrategyLabel",
            "GM.EditStudySamplePanel.samplingStrategyTooltip")
    private val samplingStrategyField = createAutoSuggestField(
            vocabs["Sampling strategy"] ?: emptySet())

    private val samplingTypeLabel = createLabel(
            "GM.EditStudySamplePanel.samplingTypeLabel",
            "GM.EditStudySamplePanel.samplingTypeTooltip")
    private val samplingTypeField = createAutoSuggestField(
            vocabs["Type of sampling program"] ?: emptySet())

    private val samplingMethodLabel = createLabel(
            "GM.EditStudySamplePanel.samplingMethodLabel",
            "GM.EditStudySamplePanel.samplingMethodTooltip")
    private val samplingMethodField = createAutoSuggestField(
            vocabs["Sampling method"] ?: emptySet())

    private val samplingPlanLabel = createLabel(
            "GM.EditStudySamplePanel.samplingPlanLabel",
            "GM.EditStudySamplePanel.samplingPlanTooltip", true)
    private val samplingPlanTextField = createTextField()

    private val samplingWeightLabel = createLabel(
            "GM.EditStudySamplePanel.samplingWeightLabel",
            "GM.EditStudySamplePanel.samplingWeightTooltip", true)
    private val samplingWeightTextField = createTextField()

    private val samplingSizeLabel = createLabel(
            "GM.EditStudySamplePanel.samplingSizeLabel",
            "GM.EditStudySamplePanel.samplingSizeTooltip", true)
    private val samplingSizeTextField = createTextField()

    private val lotSizeUnitLabel = createLabel(
            "GM.EditStudySamplePanel.lotSizeUnitLabel",
            "GM.EditStudySamplePanel.lotSizeUnitTooltip")
    private val lotSizeUnitField = createAutoSuggestField(
            vocabs["Lot size unit"] ?: emptySet())

    private val samplingPointLabel = createLabel(
            "GM.EditStudySamplePanel.samplingPointLabel",
            "GM.EditStudySamplePanel.samplingPointTooltip")
    private val samplingPointField = createAutoSuggestField(
            vocabs["Sampling point"] ?: emptySet())

    init {

        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(sampleNameLabel, sampleNameTextField),
                Pair(moisturePercentageLabel, moisturePercentageSpinner),
                Pair(fatPercentageLabel, fatPercentageSpinner),
                Pair(sampleProtocolLabel, sampleProtocolTextField),
                Pair(samplingStrategyLabel, samplingStrategyField),
                Pair(samplingTypeLabel, samplingTypeField),
                Pair(samplingMethodLabel, samplingMethodField),
                Pair(samplingPlanLabel, samplingPlanTextField),
                Pair(samplingWeightLabel, samplingWeightTextField),
                Pair(samplingSizeLabel, samplingSizeTextField),
                Pair(lotSizeUnitLabel, lotSizeUnitField),
                Pair(samplingPointLabel, samplingPointField)
        )
        addGridComponents(pairs)

        // If simple mode, hide advanced components
        if (!isAdvanced) {
            getAdvancedComponents().forEach { it.isVisible = false }
        }
    }

    override fun init(t: StudySample) {
        sampleNameTextField.text = t.sample
        moisturePercentageSpinnerModel.value = t.moisturePercentage
        fatPercentageSpinnerModel.value = t.fatPercentage
        sampleProtocolTextField.text = t.collectionProtocol
        samplingStrategyField.selectedItem = t.samplingStrategy
        samplingTypeField.selectedItem = t.samplingProgramType
        samplingMethodField.selectedItem = t.samplingMethod
        samplingPlanTextField.text = t.samplingPlan
        samplingWeightTextField.text = t.samplingWeight
        samplingSizeTextField.text = t.samplingSize
        lotSizeUnitField.selectedItem = t.lotSizeUnit
        samplingPointField.selectedItem = t.samplingPoint
    }

    override fun get(): StudySample {

        val studySample = StudySample()
        studySample.sample = sampleNameTextField.text
        studySample.collectionProtocol = sampleProtocolTextField.text
        studySample.samplingPlan = samplingPlanTextField.text
        studySample.samplingWeight = samplingWeightTextField.text
        studySample.samplingSize = samplingSizeTextField.text
        studySample.moisturePercentage = moisturePercentageSpinnerModel.number.toDouble()
        studySample.fatPercentage = fatPercentageSpinnerModel.number.toDouble()
        studySample.samplingStrategy = samplingStrategyField.selectedItem as? String ?: ""
        studySample.samplingProgramType = samplingTypeField.selectedItem as? String ?: ""
        studySample.samplingMethod = samplingMethodField.selectedItem as? String ?: ""
        studySample.lotSizeUnit = lotSizeUnitField.selectedItem as? String ?: ""
        studySample.samplingPoint = samplingPointField.selectedItem as? String ?: ""

        return studySample
    }

    override fun validatePanel(): List<String> {
        val errors = mutableListOf<String>()
        if (!sampleNameTextField.hasValidValue()) {
            val prop = bundle.getString("GM.EditStudySamplePanel.sampleNameLabel")
            errors.add("Missing $prop")
        }
        if (!sampleProtocolTextField.hasValidValue()) {
            val prop = bundle.getString("GM.EditStudySamplePanel.sampleProtocolLabel")
            errors.add("Missing $prop")
        }
        if (!samplingPlanTextField.hasValidValue()) {
            val prop = bundle.getString("GM.EditStudySamplePanel.samplingPlanTextField")
            errors.add("Missing $prop")
        }
        if (!samplingWeightTextField.hasValidValue()) {
            val prop = bundle.getString("GM.EditStudySamplePanel.samplingWeightLabel")
            errors.add("Missing $prop")
        }
        if (!samplingSizeTextField.hasValidValue()) {
            val prop = bundle.getString("GM.EditStudySamplePanel.samplingSizeLabel")
            errors.add("Missing $prop")
        }

        return errors
    }

    override fun getAdvancedComponents(): List<JComponent> {
        return listOf(
                moisturePercentageLabel, moisturePercentageSpinner, // moisture percentage
                fatPercentageLabel, fatPercentageSpinner, // fat percentag
                samplingStrategyLabel, samplingStrategyField, // sampling strategy
                samplingTypeLabel, samplingTypeField, // sampling program type
                samplingMethodLabel, samplingMethodField, // sampling method
                samplingSizeLabel, samplingSizeTextField, // sampling size
                lotSizeUnitLabel, lotSizeUnitField, // lot size unit
                samplingPointLabel, samplingPointField) // sampling point
    }
}

private class GeneralInformationPanel : Box(BoxLayout.PAGE_AXIS) {

    private val advancedCheckBox = JCheckBox("Advanced")

    private val studyNameTextField = createTextField()
    private val sourceTextField = createTextField()
    private val identifierTextField = createTextField()
    private val creatorPanel = CreatorPanel()
    private val creationDateChooser = FixedDateChooser()
    private val rightsField = createAutoSuggestField(
            vocabs["Rights"] ?: emptySet())
    private val availabilityCheckBox = JCheckBox()
    private val urlTextField = createTextField()
    private val formatField = createAutoSuggestField(
            vocabs["Format"] ?: emptySet())
    private val referencePanel = ReferencePanel(advancedCheckBox.isSelected)
    private val languageField = createAutoSuggestField(
            vocabs["Language"] ?: emptySet())
    private val softwareField = createAutoSuggestField(
            vocabs["Software"] ?: emptySet())
    private val languageWrittenInField = createAutoSuggestField(
            vocabs["Language written in"] ?: emptySet())
    private val statusField = createAutoSuggestField(
            vocabs["Status"] ?: emptySet())
    private val objectiveTextField = createTextField()
    private val descriptionTextField = createTextField()

    init {

        // Create labels
        val studyNameLabel = createLabel(
                "GM.GeneralInformationPanel.studyNameLabel",
                "GM.GeneralInformationPanel.studyNameTooltip")
        val sourceLabel = createLabel(
                "GM.GeneralInformationPanel.sourceLabel",
                "GM.GeneralInformationPanel.sourceTooltip")
        val identifierLabel = createLabel(
                "GM.GeneralInformationPanel.identifierLabel",
                "GM.GeneralInformationPanel.identifierTooltip")
        val creationDateLabel = createLabel(
                "GM.GeneralInformationPanel.creationDateLabel",
                "GM.GeneralInformationPanel.creationDateTooltip")
        val rightsLabel = createLabel(
                "GM.GeneralInformationPanel.rightsLabel",
                "GM.GeneralInformationPanel.rightsTooltip")
        val urlLabel = createLabel(
                "GM.GeneralInformationPanel.urlLabel",
                "GM.GeneralInformationPanel.urlTooltip")
        val formatLabel = createLabel(
                "GM.GeneralInformationPanel.formatLabel",
                "GM.GeneralInformationPanel.formatTooltip")
        val languageLabel = createLabel(
                "GM.GeneralInformationPanel.languageLabel",
                "GM.GeneralInformationPanel.languageTooltip")
        val softwareLabel = createLabel(
                "GM.GeneralInformationPanel.softwareLabel",
                "GM.GeneralInformationPanel.softwareTooltip")
        val languageWrittenInLabel = createLabel(
                "GM.GeneralInformationPanel.languageWrittenInLabel",
                "GM.GeneralInformationPanel.languageWrittenInTooltip")
        val statusLabel = createLabel(
                "GM.GeneralInformationPanel.statusLabel",
                "GM.GeneralInformationPanel.statusTooltip")
        val objectiveLabel = createLabel(
                "GM.GeneralInformationPanel.objectiveLabel",
                "GM.GeneralInformationPanel.objectiveTooltip")
        val descriptionLabel = createLabel(
                "GM.GeneralInformationPanel.descriptionLabel",
                "GM.GeneralInformationPanel.descriptionTooltip")

        availabilityCheckBox.text = bundle.getString(
                "GM.GeneralInformationPanel.availabilityLabel")
        availabilityCheckBox.toolTipText = bundle.getString(
                "GM.GeneralInformationPanel.availabilityTooltip")

        // Hide initially advanced components
        val advancedComponents = listOf<JComponent>(
                sourceLabel, sourceTextField, // source
                formatLabel, formatField, // format
                languageLabel, languageField, // language
                softwareLabel, softwareField, // software
                languageWrittenInLabel, languageWrittenInField, // language written in
                statusLabel, statusField, // status
                objectiveLabel, objectiveTextField, // objective
                descriptionLabel, descriptionTextField) // description
        advancedComponents.forEach { it.isVisible = false }

        // Build UI
        val propertiesPanel = JPanel(GridBagLayout())

        // add study name
        propertiesPanel.add(studyNameLabel, 0, 0)
        propertiesPanel.add(studyNameTextField, 0, 1, 2)

        // add source
        propertiesPanel.add(sourceLabel, 1, 0)
        propertiesPanel.add(sourceTextField, 1, 1, 2)

        // add identifier
        propertiesPanel.add(identifierLabel, 2, 0)
        propertiesPanel.add(identifierTextField, 2, 1, 2)

        // add creatorPanel
        propertiesPanel.add(creatorPanel, 3, 0)

        // add creationDate
        propertiesPanel.add(creationDateLabel, 4, 0)
        propertiesPanel.add(creationDateChooser, 4, 1)

        // add rights
        propertiesPanel.add(rightsLabel, 5, 0)
        propertiesPanel.add(rightsField, 5, 1, 2)

        // add availabilityCheckBox
        propertiesPanel.add(availabilityCheckBox, 6, 0)

        // add url
        propertiesPanel.add(urlLabel, 7, 0)
        propertiesPanel.add(urlTextField, 7, 1, 2)

        // add format
        propertiesPanel.add(formatLabel, 8, 0)
        propertiesPanel.add(formatField, 8, 1, 2)

        // add referencePanel
        propertiesPanel.add(referencePanel, 9, 0)

        // add language
        propertiesPanel.add(languageLabel, 10, 0)
        propertiesPanel.add(languageField, 10, 1, 2)

        // add software
        propertiesPanel.add(softwareLabel, 11, 0)
        propertiesPanel.add(softwareField, 11, 1)

        // add languageWrittenIn
        propertiesPanel.add(languageWrittenInLabel, 12, 0)
        propertiesPanel.add(languageWrittenInField, 12, 1)

        // add status
        propertiesPanel.add(statusLabel, 13, 0)
        propertiesPanel.add(statusField, 13, 1, 2)

        // add objective
        propertiesPanel.add(objectiveLabel, 14, 0)
        propertiesPanel.add(objectiveTextField, 14, 1, 2)

        // add description
        propertiesPanel.add(descriptionLabel, 15, 0)
        propertiesPanel.add(descriptionTextField, 15, 1, 2)

        advancedCheckBox.addItemListener {
            val showAdvanced = advancedCheckBox.isSelected
            advancedComponents.forEach { it.isVisible = showAdvanced }
            referencePanel.isAdvanced = showAdvanced
        }

        add(createAdvancedPanel(advancedCheckBox))
        add(createGlue())
        add(propertiesPanel)
    }

    fun init(generalInformation: GeneralInformation) {

        studyNameTextField.text = generalInformation.name
        sourceTextField.text = generalInformation.source
        identifierTextField.text = generalInformation.identifier
        creatorPanel.init(generalInformation.creators)
        creationDateChooser.date = generalInformation.creationDate
        rightsField.selectedItem = generalInformation.rights
        availabilityCheckBox.isSelected = generalInformation.isAvailable
        urlTextField.text = generalInformation.url.toString()
        formatField.selectedItem = generalInformation.format
        referencePanel.init(generalInformation.reference)
        languageField.selectedItem = generalInformation.language
        softwareField.selectedItem = generalInformation.software
        languageWrittenInField.selectedItem = generalInformation.languageWrittenIn
        statusField.selectedItem = generalInformation.status
        objectiveTextField.text = generalInformation.objective
        descriptionTextField.text = generalInformation.description
    }

    fun get(): GeneralInformation {

        val generalInformation = GeneralInformation()
        generalInformation.name = studyNameTextField.text
        generalInformation.source = sourceTextField.text
        generalInformation.identifier = identifierTextField.text
        generalInformation.creationDate = creationDateChooser.date
        generalInformation.rights = rightsField.selectedItem as String
        generalInformation.isAvailable = availabilityCheckBox.isSelected
        try {
            generalInformation.url = URL(urlTextField.text)
        } catch (e: MalformedURLException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        generalInformation.creators.addAll(creatorPanel.creators)
        generalInformation.format = formatField.selectedItem as String
        generalInformation.reference.addAll(referencePanel.refs)
        generalInformation.language = languageField.selectedItem as String
        generalInformation.software = softwareField.selectedItem as String
        generalInformation.languageWrittenIn = languageWrittenInField.selectedItem as String
        generalInformation.status = statusField.selectedItem as String
        generalInformation.objective = objectiveTextField.text
        generalInformation.description = descriptionTextField.text

        return generalInformation
    }
}

private class ModelEquationsPanel : JPanel(BorderLayout()) {

    val tableModel = NonEditableTableModel()
    val equations = mutableListOf<ModelEquation>()

    private val editPanel = EditModelEquationPanel(false)

    init {
        border = BorderFactory.createTitledBorder("Model equation")

        val renderer = object : DefaultTableCellRenderer() {
            override fun setValue(value: Any?) {
                text = (value as? ModelEquation)?.equation ?: ""
            }
        }

        val myTable = HeadlessTable(tableModel, renderer)

        val buttonsPanel = ButtonsPanel()

        buttonsPanel.addButton.addActionListener {
            val dlg = ValidatableDialog(editPanel, "Create equation")
            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                tableModel.addRow(arrayOf(editPanel.get()))
            }
        }

        buttonsPanel.modifyButton.addActionListener {
            val rowToEdit = myTable.selectedRow
            if (rowToEdit != -1) {
                val equation = tableModel.getValueAt(rowToEdit, 0) as ModelEquation
                editPanel.init(equation)

                val dlg = ValidatableDialog(editPanel, "Modify equation")
                if (dlg.getValue() == JOptionPane.OK_OPTION) {
                    tableModel.setValueAt(editPanel.get(), rowToEdit, 0)
                }
            }
        }

        buttonsPanel.removeButton.addActionListener {
            val rowToDelete = myTable.selectedRow
            if (rowToDelete != -1) {
                tableModel.removeRow(rowToDelete)
            }
        }

        add(myTable, BorderLayout.NORTH)
        add(buttonsPanel, BorderLayout.SOUTH)
    }

    fun toggleMode() = editPanel.toggleMode()
}

private class ModelMathPanel : Box(BoxLayout.PAGE_AXIS) {

    private val advancedCheckBox = JCheckBox("Advanced")

    init {

        val isAdvanced = advancedCheckBox.isSelected

        val parametersPanel = ParametersPanel(isAdvanced)
        val qualityMeasuresPanel = QualityMeasuresPanel()
        val modelEquationPanel = ModelEquationsPanel()

        val propertiesPanel = JPanel(GridBagLayout())
        propertiesPanel.add(parametersPanel, 0, 0)
        propertiesPanel.add(qualityMeasuresPanel, 0, 1)
        propertiesPanel.add(modelEquationPanel, 0, 2)

        add(createAdvancedPanel(advancedCheckBox))
        add(createGlue())
        add(propertiesPanel)

        advancedCheckBox.addItemListener {
            parametersPanel.isAdvanced = advancedCheckBox.isSelected
            modelEquationPanel.toggleMode()
        }
    }
}

private class ParametersPanel(var isAdvanced: Boolean) : JPanel(BorderLayout()) {

    init {

        border = BorderFactory.createTitledBorder("Parameters")

        val tableModel = NonEditableTableModel()
        // TODO: Parameters

        val renderer = object : DefaultTableCellRenderer() {
            override fun setValue(value: Any?) {
                text = (value as? Parameter)?.id ?: ""
            }
        }

        val myTable = HeadlessTable(tableModel, renderer)

        // buttons
        val buttonsPanel = ButtonsPanel()
        buttonsPanel.addButton.addActionListener {
            val editPanel = EditParameterPanel(isAdvanced)
            val dlg = ValidatableDialog(editPanel, "Create parameter")
            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                tableModel.addRow(arrayOf(editPanel.get()))
            }
        }

        buttonsPanel.modifyButton.addActionListener {
            val rowToEdit = myTable.selectedRow
            if (rowToEdit != -1) {

                val param = tableModel.getValueAt(rowToEdit, 0) as Parameter
                val editPanel = EditParameterPanel(isAdvanced)
                editPanel.init(param)

                val dlg = ValidatableDialog(editPanel, "Modify parameter")
                if (dlg.getValue() == JOptionPane.OK_OPTION) {
                    tableModel.setValueAt(editPanel.get(), rowToEdit, 0)
                }
            }
        }

        buttonsPanel.removeButton.addActionListener {
            val rowToDelete = myTable.selectedRow
            if (rowToDelete != -1) {
                tableModel.removeRow(rowToDelete)
            }
        }

        add(myTable, BorderLayout.NORTH)
        add(buttonsPanel, BorderLayout.SOUTH)
    }
}

private class QualityMeasuresPanel(sse: Double = 0.0, mse: Double = 0.0, rmse: Double = 0.0, r2: Double = 0.0,
                                   aic: Double = 0.0, bic: Double = 0.0) : JPanel(GridBagLayout()) {

    val sseSpinnerModel = createSpinnerDoubleModel()
    val mseSpinnerModel = createSpinnerDoubleModel()
    val rmseSpinnerModel = createSpinnerDoubleModel()
    val r2SpinnerModel = createSpinnerDoubleModel()
    val aicSpinnerModel = createSpinnerDoubleModel()
    val bicSpinnerModel = createSpinnerDoubleModel()

    init {

        sseSpinnerModel.value = sse
        mseSpinnerModel.value = mse
        rmseSpinnerModel.value = rmse
        r2SpinnerModel.value = r2
        aicSpinnerModel.value = aic
        bicSpinnerModel.value = bic

        val sseLabel = JLabel("SSE")
        val sseSpinner = createSpinner(sseSpinnerModel)

        val mseLabel = JLabel("MSE")
        val mseSpinner = createSpinner(mseSpinnerModel)

        val rmseLabel = JLabel("RMSE")
        val rmseSpinner = createSpinner(rmseSpinnerModel)

        val r2Label = JLabel("r-Squared")
        val r2Spinner = createSpinner(r2SpinnerModel)

        val aicLabel = JLabel("AIC")
        val aicSpinner = createSpinner(aicSpinnerModel)

        val bicLabel = JLabel("BIC")
        val bicSpinner = createSpinner(bicSpinnerModel)

        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(sseLabel, sseSpinner), // SSE
                Pair(mseLabel, mseSpinner), // MSE
                Pair(rmseLabel, rmseSpinner), // RMSE
                Pair(r2Label, r2Spinner), // R2
                Pair(aicLabel, aicSpinner), // AIC
                Pair(bicLabel, bicSpinner)) // Spinner
        addGridComponents(pairs)

        border = BorderFactory.createTitledBorder("Quality measures")
    }
}

private class ReferencePanel(var isAdvanced: Boolean) : JPanel(BorderLayout()) {

    private val tableModel = NonEditableTableModel()
    val refs = mutableListOf<Record>()

    init {
        border = BorderFactory.createTitledBorder("References")

        val renderer = object : DefaultTableCellRenderer() {

            override fun setValue(value: Any?) {
                text = if (value == null) {
                    ""
                } else {
                    val record = value as Record

                    val firstAuthor = record.authors[0]
                    val publicationYear = record.pubblicationYear
                    val title = record.title
                    "${firstAuthor}_${publicationYear}_$title"
                }
            }
        }

        val myTable = HeadlessTable(tableModel, renderer)

        // buttons
        val buttonsPanel = ButtonsPanel()
        buttonsPanel.addButton.addActionListener {
            val editPanel = EditReferencePanel(isAdvanced)
            val dlg = ValidatableDialog(editPanel, "Create reference")

            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                tableModel.addRow(arrayOf(editPanel.get()))
            }
        }

        buttonsPanel.modifyButton.addActionListener {
            val rowToEdit = myTable.selectedRow
            if (rowToEdit != -1) {
                val ref = tableModel.getValueAt(rowToEdit, 0) as Record
                val editPanel = EditReferencePanel(isAdvanced)
                editPanel.init(ref)

                val dlg = ValidatableDialog(editPanel, "Modify reference")
                if (dlg.getValue() == JOptionPane.OK_OPTION) {
                    tableModel.setValueAt(editPanel.get(), rowToEdit, 0)
                }
            }
        }

        buttonsPanel.removeButton.addActionListener {
            val rowToDelete = myTable.selectedRow
            if (rowToDelete != -1) {
                tableModel.removeRow(rowToDelete)
            }
        }

        add(myTable, BorderLayout.NORTH)
        add(buttonsPanel, BorderLayout.SOUTH)
    }

    fun init(references: List<Record>) {
        references.forEach { tableModel.addRow(arrayOf(it)) }
        refs.addAll(references.toSet())
    }
}

private class ScopePanel : Box(BoxLayout.PAGE_AXIS) {

    private val productButton = JButton()
    private val hazardButton = JButton()
    private val populationButton = JButton()

    private val commentTextArea = createTextArea()
    private val commentPane = JScrollPane(commentTextArea)

    private val dateChooser = FixedDateChooser()
    private val regionField = createAutoSuggestField(vocabs["Region"] ?: emptySet())
    private val countryField = createAutoSuggestField(vocabs["Country"] ?: emptySet())

    private val advancedCheckBox = JCheckBox("Advanced")

    private val editProductPanel = EditProductPanel(false)
    private val editHazardPanel = EditHazardPanel(false)
    private val editPopulationGroupPanel = EditPopulationGroupPanel(false)

    init {

        productButton.toolTipText = "Click me to add a product"
        productButton.addActionListener {
            val dlg = ValidatableDialog(editProductPanel, "Create a product")
            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                val (environmentName, _, environmentUnit) = editProductPanel.get()
                productButton.text = "${environmentName}_$environmentUnit"
            }
        }

        hazardButton.toolTipText = "Click me to add a hazard"
        hazardButton.addActionListener {
            val dlg = ValidatableDialog(editHazardPanel, "Create a hazard")

            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                val (_, hazardName, _, hazardUnit) = editHazardPanel.get()
                hazardButton.text = String.format("%s_%s", hazardName, hazardUnit)
            }
        }

        populationButton.toolTipText = "Click me to add a Population group"
        populationButton.addActionListener {
            val dlg = ValidatableDialog(editPopulationGroupPanel, "Create a Population Group")

            if (dlg.getValue() == JOptionPane.OK_OPTION) {
                val (populationName) = editPopulationGroupPanel.get()
                populationButton.text = populationName
            }
        }

        // Create labels
        val productLabel = createLabel("GM.ScopePanel.productLabel")
        val hazardLabel = createLabel("GM.ScopePanel.hazardLabel")
        val populationLabel = createLabel("GM.ScopePanel.populationGroupLabel")
        val commentLabel = createLabel("GM.ScopePanel.commentLabel",
                "GM.ScopePanel.commentTooltip")
        val temporalInformationLabel = createLabel("GM.ScopePanel.temporalInformationLabel",
                "GM.ScopePanel.temporalInformationTooltip")
        val regionLabel = createLabel("GM.ScopePanel.regionLabel",
                "GM.ScopePanel.regionTooltip")
        val countryLabel = createLabel("GM.ScopePanel.countryLabel",
                "GM.ScopePanel.countryTooltip")

        // Build UI
        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(productLabel, productButton), // product
                Pair(hazardLabel, hazardButton), // hazard
                Pair(populationLabel, populationButton), // population group
                Pair(commentLabel, commentPane), // comment
                Pair(temporalInformationLabel, dateChooser), // temporal information
                Pair(regionLabel, regionField), // region
                Pair(countryLabel, countryField))  // country

        val propertiesPanel = JPanel(GridBagLayout())
        propertiesPanel.addGridComponents(pairs)

        // Advanced checkbox
        advancedCheckBox.addItemListener {
            editProductPanel.toggleMode()
            editHazardPanel.toggleMode()
            editPopulationGroupPanel.toggleMode()
        }

        add(createAdvancedPanel(advancedCheckBox))
        add(createGlue())
        add(propertiesPanel)
    }

    fun init(scope: Scope) {
        editProductPanel.init(scope.product)
        editHazardPanel.init(scope.hazard)
        editPopulationGroupPanel.init(scope.populationGroup)

        if (scope.temporalInformation.isNotEmpty()) {
            try {
                dateChooser.date = SimpleDateFormat("yyyy-MM-dd").parse(scope.temporalInformation)
            } catch (exception: ParseException) {
                exception.printStackTrace()
            }
        }

        if (scope.region.isNotEmpty()) {
            regionField.selectedItem = scope.region[0]
        }
        if (scope.country.isNotEmpty()) {
            countryField.selectedItem = scope.country[0]
        }
    }

    fun get(): Scope {

        val scope = Scope()
        scope.product = editProductPanel.get()
        scope.hazard = editHazardPanel.get()
        scope.populationGroup = editPopulationGroupPanel.get()

        dateChooser.date?.let {
            scope.temporalInformation = SimpleDateFormat(dateChooser.dateFormatString).format(it)
        }
        scope.region.add(regionField.selectedItem as String)
        scope.country.add(countryField.selectedItem as String)

        return scope
    }
}

private class StudyPanel : JPanel(GridBagLayout()) {

    private val studyIdentifierTextField = createTextField()
    private val studyTitleTextField = createTextField()

    private val studyDescriptionTextArea = createTextArea()
    private val studyDescriptionPane = JScrollPane(studyDescriptionTextArea)

    private val studyDesignTypeField = createAutoSuggestField(
            vocabs["Study Design Type"] ?: emptySet())
    private val studyAssayMeasurementsTypeField = createAutoSuggestField(
            vocabs["Study Assay Measurement Type"] ?: emptySet())
    private val studyAssayTechnologyTypeField = createAutoSuggestField(
            vocabs["Study Assay Technology Type"] ?: emptySet())
    private val studyAssayTechnologyPlatformTextField = createTextField()
    private val accreditationProcedureField = createAutoSuggestField(
            vocabs["Accreditation procedur Ass.Tec"] ?: emptySet())
    private val studyProtocolNameTextField = createTextField()
    private val studyProtocolTypeField = createAutoSuggestField(
            vocabs["Study Protocol Type"] ?: emptySet())
    private val studyProtocolDescriptionTextField = createTextField()
    private val studyProtocolURITextField = createTextField()
    private val studyProtocolVersionTextField = createTextField()
    private val studyProtocolParametersField = createAutoSuggestField(
            vocabs["Study Protocol Parameters Name"] ?: emptySet())
    private val studyProtocolComponentsTypeField = createAutoSuggestField(
            vocabs["Study Protocol Components Type"] ?: emptySet())

    val advancedComponents: List<JComponent>

    init {

        // Create labels
        val studyIdentifierLabel = createLabel("GM.StudyPanel.studyIdentifierLabel",
                "GM.StudyPanel.studyIdentifierTooltip", true)
        val studyTitleLabel = createLabel("GM.StudyPanel.studyTitleLabel",
                "GM.StudyPanel.studyTitleTooltip", true)
        val studyDescriptionLabel = createLabel("GM.StudyPanel.studyDescriptionLabel",
                "GM.StudyPanel.studyDescriptionTooltip")
        val studyDesignTypeLabel = createLabel("GM.StudyPanel.studyDesignTypeLabel",
                "GM.StudyPanel.studyDesignTypeTooltip")
        val studyAssayMeasurementsTypeLabel = createLabel(
                "GM.StudyPanel.studyAssayMeasurementsTypeLabel",
                "GM.StudyPanel.studyAssayMeasurementsTypeTooltip")
        val studyAssayTechnologyTypeLabel = createLabel(
                "GM.StudyPanel.studyAssayTechnologyTypeLabel",
                "GM.StudyPanel.studyAssayTechnologyTypeTooltip")
        val studyAssayTechnologyPlatformLabel = createLabel(
                "GM.StudyPanel.studyAssayTechnologyPlatformLabel",
                "GM.StudyPanel.studyAssayTechnologyPlatformTooltip")
        val accreditationProcedureLabel = createLabel(
                "GM.StudyPanel.accreditationProcedureLabel",
                "GM.StudyPanel.accreditationProcedureTooltip")
        val studyProtocolNameLabel = createLabel(
                "GM.StudyPanel.protocolNameLabel",
                "GM.StudyPanel.protocolNameTooltip")
        val studyProtocolTypeLabel = createLabel(
                "GM.StudyPanel.protocolTypeLabel",
                "GM.StudyPanel.protocolTypeTooltip")
        val studyProtocolDescriptionLabel = createLabel(
                "GM.StudyPanel.protocolDescriptionLabel",
                "GM.StudyPanel.protocolDescriptionTooltip")
        val studyProtocolURILabel = createLabel(
                "GM.StudyPanel.protocolURILabel",
                "GM.StudyPanel.protocolURITooltip")
        val studyProtocolVersionLabel = createLabel(
                "GM.StudyPanel.protocolDescriptionLabel",
                "GM.StudyPanel.protocolDescriptionTooltip")
        val studyProtocolParametersLabel = createLabel(
                "GM.StudyPanel.parametersLabel",
                "GM.StudyPanel.parametersTooltip")
        val studyProtocolComponentsTypeLabel = createLabel(
                "GM.StudyPanel.componentsTypeLabel",
                "GM.StudyPanel.componentsTypeTooltip")

        // Build UI
        val pairs = listOf<Pair<JLabel, JComponent>>(
                Pair(studyIdentifierLabel, studyIdentifierTextField),
                Pair(studyTitleLabel, studyTitleTextField),
                Pair(studyDescriptionLabel, studyDescriptionPane),
                Pair(studyDesignTypeLabel, studyDesignTypeField),
                Pair(studyAssayMeasurementsTypeLabel, studyAssayMeasurementsTypeField),
                Pair(studyAssayTechnologyTypeLabel, studyAssayTechnologyTypeField),
                Pair(studyAssayTechnologyPlatformLabel, studyAssayTechnologyPlatformTextField),
                Pair(accreditationProcedureLabel, accreditationProcedureField),
                Pair(studyProtocolNameLabel, studyProtocolNameTextField),
                Pair(studyProtocolTypeLabel, studyProtocolTypeField),
                Pair(studyProtocolDescriptionLabel, studyProtocolDescriptionTextField),
                Pair(studyProtocolURILabel, studyProtocolURITextField),
                Pair(studyProtocolParametersLabel, studyProtocolParametersField),
                Pair(studyProtocolComponentsTypeLabel, studyProtocolComponentsTypeField))
        addGridComponents(pairs)

        advancedComponents = listOf(
                studyDescriptionLabel, studyDescriptionPane,
                studyDesignTypeLabel, studyDesignTypeField, studyAssayMeasurementsTypeLabel,
                studyAssayMeasurementsTypeField, studyAssayTechnologyTypeLabel,
                studyAssayTechnologyTypeField, studyAssayTechnologyPlatformLabel,
                studyAssayTechnologyPlatformTextField, accreditationProcedureLabel,
                accreditationProcedureField, studyProtocolNameLabel, studyProtocolNameTextField,
                studyProtocolTypeLabel, studyProtocolTypeField, studyProtocolDescriptionLabel,
                studyProtocolDescriptionTextField, studyProtocolURILabel, studyProtocolURITextField,
                studyProtocolVersionLabel, studyProtocolVersionTextField, studyProtocolParametersLabel,
                studyProtocolParametersField, studyProtocolComponentsTypeLabel,
                studyProtocolComponentsTypeField)

        advancedComponents.forEach { it.isVisible = false }
    }
}