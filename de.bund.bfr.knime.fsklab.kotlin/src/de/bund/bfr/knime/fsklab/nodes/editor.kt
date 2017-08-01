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

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.bund.bfr.knime.fsklab.FskPortObject
import de.bund.bfr.knime.fsklab.FskPortObjectSpec
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel
import de.bund.bfr.rakip.editor.DataBackgroundPanel
import de.bund.bfr.rakip.editor.GeneralInformationPanel
import de.bund.bfr.rakip.editor.ModelMathPanel
import de.bund.bfr.rakip.editor.ScopePanel
import de.bund.bfr.rakip.generic.GenericModel
import de.bund.bfr.rakip.generic.RakipModule
import org.knime.core.node.DataAwareNodeDialogPane
import org.knime.core.node.ExecutionContext
import org.knime.core.node.NoInternalsModel
import org.knime.core.node.NodeFactory
import org.knime.core.node.NodeModel
import org.knime.core.node.NodeSettingsRO
import org.knime.core.node.NodeSettingsWO
import org.knime.core.node.NodeView
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObjectSpec
import java.net.URL

// JSON mapper to process metadata
val objectMapper = jacksonObjectMapper().registerModule(RakipModule())

class EditorNodeSettings {

	var originalModelScript = ""
	var originalParametersScript = ""
	var originalVisualizationScript = ""

	var modifiedModelScript = ""
	var modifiedParametersScript = ""
	var modifiedVisualizationScript = ""

	lateinit var genericModel: GenericModel

	/**
	 * Saves the settings into the given node settings object.
	 * @param settings a node settings object
	 */
	fun saveSettings(settings: NodeSettingsWO) {

		settings.addString("originalModelScript", originalModelScript)
		settings.addString("originalParametersScript", originalParametersScript)
		settings.addString("originalVisualizationScript", originalVisualizationScript)

		settings.addString("modifiedModelScript", modifiedModelScript)
		settings.addString("modifiedParametersScript", modifiedParametersScript)
		settings.addString("modifiedVisualizationScript", modifiedVisualizationScript)

		// Save meta data
		settings.addString("genericModel", objectMapper.writeValueAsString(genericModel))
	}

	/**
	 * Loads the settings from the given node settings object.
	 * @param settings a node settings object
	 */
	fun loadSettings(settings: NodeSettingsRO) {

		originalModelScript = settings.getString("originalModelScript", "")
		originalParametersScript = settings.getString("originalParametersScript", "")
		originalVisualizationScript = settings.getString("originalVisualizationScript", "")

		modifiedModelScript = settings.getString("modifiedModelScript", "")
		modifiedParametersScript = settings.getString("modifiedParametersScript")
		modifiedVisualizationScript = settings.getString("modifiedVisualizationScript", "")

		// Load meta data
		objectMapper.readValue(settings.getString("genericModel"), GenericModel::class.java)
	}
}


class NodeDialog : DataAwareNodeDialogPane() {

	// panels
	private val modelScriptPanel = ScriptPanel("Model script", "", true)
	private val paramScriptPanel = ScriptPanel("Parameters script", "", true)
	private val vizScriptPanel = ScriptPanel("Visualization script", "", true)

	private val generalInformationPanel = GeneralInformationPanel()
	private val scopePanel = ScopePanel()
	private val dataBackgroundPanel = DataBackgroundPanel()
	private val modelMathPanel = ModelMathPanel()

	// Initialize settings (values are garbage, need to be loaded from settings/input port)
	private var settings = EditorNodeSettings()

	init {
		// Add script panels
		addTab("Model script", modelScriptPanel)
		addTab("Parameters script", paramScriptPanel)
		addTab("Visualization script", vizScriptPanel)

		addTab("General information", generalInformationPanel)
		addTab("Scope", scopePanel)
		addTab("Data background", dataBackgroundPanel)
		addTab("Model math", modelMathPanel)

		updatePanels()
	}

	// Update the scripts in the ScriptPanels
	private fun updatePanels() {

		modelScriptPanel.textArea.text = settings.modifiedModelScript
		paramScriptPanel.textArea.text = settings.modifiedParametersScript
		vizScriptPanel.textArea.text = settings.modifiedVisualizationScript

		// generalInformationPanel
		settings.genericModel.generalInformation.let { gi ->

			val creatorPanelModel = generalInformationPanel.creatorPanel.dtm
			val referencePanelModel = generalInformationPanel.referencePanel.dtm

			generalInformationPanel.studyNameTextField.text = gi.name
			generalInformationPanel.identifierTextField.text = gi.identifier
			gi.creators.forEach { creatorPanelModel.addRow(arrayOf(it)) }
			generalInformationPanel.rightsField.selectedItem = gi.rights
			generalInformationPanel.availabilityCheckBox.isSelected = gi.isAvailable
			generalInformationPanel.urlTextField.text = gi.url.toString()
			generalInformationPanel.formatField.selectedItem = gi.format
			gi.reference.forEach { referencePanelModel.addRow(arrayOf(it)) }
			generalInformationPanel.languageField.selectedItem = gi.language
			generalInformationPanel.softwareField.selectedItem = gi.software
			generalInformationPanel.languageWrittenInField.selectedItem = gi.languageWrittenIn
			generalInformationPanel.statusField.selectedItem = gi.status
			generalInformationPanel.objectiveTextField.text = gi.objective
			generalInformationPanel.descriptionTextField.text = gi.description
		}

		// TODO: SP
		settings.genericModel.scope.let { scope ->

			/*
			 * TODO: scope should be made a variable in ScopePanel so that it can updated here
			 */
			scopePanel.productButton.text = "${scope.product?.environmentName} [${scope.product?.environmentUnit}]"
			scopePanel.hazardButton.text = "${scope.hazard?.hazardName} [${scope.hazard?.hazardUnit}]"

			scopePanel.populationButton.text = scope.populationGroup?.populationName
			scopePanel.commentField.text = scope.generalComment

			// TODO: temporal information should be a date
			// scopePanel.dateChooser.date = scope.temporalInformation

			// TODO: regionField
			// TODO: countryField
		}

		// TODO: DBP
		// TODO: MMP
	}

	// --- settings methods ---
	override fun loadSettingsFrom(settings: NodeSettingsRO, input: Array<PortObject>) {

		val editorSettings = EditorNodeSettings().apply { loadSettings(settings) }
		val inObj = input[0] as FskPortObject

		// if input model has not changed (the original scripts stored in settings match the input model)
		if (editorSettings.originalModelScript == inObj.model &&
				editorSettings.originalParametersScript == inObj.param &&
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

	/** Loads settings from saved settings. */
	override fun loadSettingsFrom(settings: NodeSettingsRO, specs: Array<PortObjectSpec>) {
		this.settings.loadSettings(settings)
		updatePanels()
	}

	override fun saveSettingsTo(settings: NodeSettingsWO) {

		// Save modified scripts to settings (and trim)
		this.settings.modifiedModelScript = modelScriptPanel.textArea.text.trim()
		this.settings.modifiedParametersScript = paramScriptPanel.textArea.text.trim()
		this.settings.modifiedVisualizationScript = vizScriptPanel.textArea.text.trim()

		// General information
		this.settings.genericModel.generalInformation.let { gi ->

			gi.name = generalInformationPanel.studyNameTextField.text
			// TODO: need to include JTextField for source in GUI
			gi.source = null
			gi.identifier = generalInformationPanel.identifierTextField.text
			gi.creationDate = generalInformationPanel.creationDateChooser.date
			gi.rights = generalInformationPanel.rightsField.selectedItem as String
			gi.isAvailable = generalInformationPanel.availabilityCheckBox.isSelected
			gi.url = URL(generalInformationPanel.urlTextField.text)
			gi.format = generalInformationPanel.formatField.selectedItem as? String

			gi.reference.clear()
			generalInformationPanel.referencePanel.refs?.forEach { gi.reference.add(it) }

			gi.language = generalInformationPanel.languageField.selectedItem as? String
			gi.software = generalInformationPanel.softwareField.selectedItem as? String
			gi.languageWrittenIn = generalInformationPanel.languageWrittenInField.selectedItem as? String

			// TODO: model category?

			gi.status = generalInformationPanel.statusField.selectedItem as? String
			gi.objective = generalInformationPanel.objectiveTextField.text
			gi.description = generalInformationPanel.descriptionTextField.text
		}

		// TODO: SP
		// TODO: DBP
		// TODO: MMP

		this.settings.saveSettings(settings)
	}
}


private val IN_TYPES = arrayOf(FskPortObject.TYPE_OPTIONAL)
private val OUT_TYPES = arrayOf(FskPortObject.TYPE)

class EditorNodeModel : NoInternalsModel(inPortTypes = IN_TYPES, outPortTypes = OUT_TYPES) {

	private val settings = EditorNodeSettings()

	// node settings methods
	override fun loadValidatedSettingsFrom(settings: NodeSettingsRO) = this.settings.loadSettings(settings)

	override fun validateSettings(settings: NodeSettingsRO) = EditorNodeSettings().loadSettings(settings)
	override fun saveSettingsTo(settings: NodeSettingsWO) = this.settings.saveSettings(settings)

	override fun configure(inSpecs: Array<PortObjectSpec>) = arrayOf(FskPortObjectSpec.INSTANCE)

	override fun execute(inObjects: Array<PortObject>, exec: ExecutionContext): Array<PortObject> {

		// If there is an input model
		if (inObjects.size > 0) {

			val inObj = inObjects[0] as FskPortObject

			// If input model has not changed (the original script stored in settings match the input model)
			if (settings.originalModelScript == inObj.model && settings.originalParametersScript == inObj.param &&
					settings.originalVisualizationScript == inObj.viz) {
				inObj.model = settings.modifiedModelScript
				inObj.param = settings.modifiedParametersScript
				inObj.viz = settings.modifiedVisualizationScript
				inObj.genericModel = settings.genericModel

			} else {

				settings.originalModelScript = inObj.model
				settings.originalParametersScript = inObj.param
				settings.originalVisualizationScript = inObj.viz

				settings.modifiedModelScript = inObj.model
				settings.modifiedParametersScript = inObj.param
				settings.modifiedVisualizationScript = inObj.viz
			}

			return arrayOf(inObj)
		}

		// If there is not input model then it will return the model created in the UI
		val outObj = FskPortObject(model = settings.modifiedModelScript, param = settings.modifiedParametersScript,
				viz = settings.modifiedVisualizationScript, genericModel = settings.genericModel)
		return arrayOf(outObj)
	}

	override fun reset() = Unit
}

class EditorNodeFactory : NodeFactory<EditorNodeModel>() {

	override fun createNodeModel() = EditorNodeModel()
	override fun getNrNodeViews() = 0
	override fun createNodeView(viewIndex: Int, nodeModel: EditorNodeModel): NodeView<EditorNodeModel>? = null
	override fun hasDialog() = true
	override fun createNodeDialogPane() = NodeDialog()
}
