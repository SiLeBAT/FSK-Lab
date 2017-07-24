package de.bund.bfr.knime.fsklab.nodes

import org.knime.core.node.NodeSettingsRO
import org.knime.core.node.NodeSettingsWO
import org.knime.core.node.defaultnodesettings.SettingsModelColor
import org.knime.core.node.defaultnodesettings.SettingsModelInteger
import org.knime.core.node.defaultnodesettings.SettingsModelString
import java.awt.Color
import org.knime.core.node.InvalidSettingsException

class RunnerNodeSettings {

	companion object {
		val WIDTH = 640
		val HEIGHT = 640
		val RES = "NA"
		val COLOUR = Color.white
		val TEXT_POINT_SIZE = 12
	}

	// Setting models, with keys and default values
	val widthModel = SettingsModelInteger("width", WIDTH)
	val heightModel = SettingsModelInteger("height", HEIGHT)
	val resolutionModel = SettingsModelString("resolution", RES)
	val colourModel = SettingsModelColor("colour", COLOUR)
	val textPointSizeModel = SettingsModelInteger("textPointSize", TEXT_POINT_SIZE)

	fun saveSettingsTo(settings: NodeSettingsWO) {
		widthModel.saveSettingsTo(settings)
		heightModel.saveSettingsTo(settings)
		resolutionModel.saveSettingsTo(settings)
		colourModel.saveSettingsTo(settings)
		textPointSizeModel.saveSettingsTo(settings)
	}

	fun validateSettings(settings: NodeSettingsRO) {
		widthModel.validateSettings(settings = settings, defaultValue = WIDTH)
		heightModel.validateSettings(settings = settings, defaultValue = HEIGHT)
		resolutionModel.validateSettings(settings = settings, defaultValue = RES)
		colourModel.validateSettings(settings = settings, defaultValue = COLOUR)
		textPointSizeModel.validateSettings(settings = settings, defaultValue = TEXT_POINT_SIZE)
	}

	fun loadValidatedSettingsFrom(settings: NodeSettingsRO) {
		widthModel.loadSettingsFrom(settings = settings, defaultValue = WIDTH)
		heightModel.loadSettingsFrom(settings = settings, defaultValue = HEIGHT)
		resolutionModel.loadSettingsFrom(settings = settings, defaultValue = RES)
		colourModel.loadSettingsFrom(settings = settings, defaultValue = COLOUR)
		textPointSizeModel.loadSettingsFrom(settings = settings, defaultValue = TEXT_POINT_SIZE)
	}

	/**
	 * Reads the expected values from the settings object, without assigning them to the internal variable.
	 *
	 * @param settings the object to read the value from
	 * @param defaultValue value to be assigned if the value in the settings object is invalid
	 */
	private fun SettingsModelInteger.validateSettings(settings: NodeSettingsRO, defaultValue: Int) {
		try {
			validateSettings(settings)
		} catch (_: InvalidSettingsException) {
			setIntValue(defaultValue)
		}
	}

	/**
	 * Reads the expected values from the settings object, without assigning them to the internal variable.
	 *
	 * @param settings the object to read the value from
	 * @param defaultValue value to be assigned if the value in the settings object is invalid
	 */
	private fun SettingsModelString.validateSettings(settings: NodeSettingsRO, defaultValue: String) {
		try {
			validateSettings(settings)
		} catch (_: InvalidSettingsException) {
			setStringValue(defaultValue)
		}
	}

	/**
	 * Reads the expected values from the settings object, without assigning them to the internal variable.
	 *
	 * @param settings the object to read the value from
	 * @param defaultValue value to be assigned if the value in the settings object is invalid
	 */
	private fun SettingsModelColor.validateSettings(settings: NodeSettingsRO, defaultValue: Color) {
		try {
			validateSettings(settings)
		} catch (_: InvalidSettingsException) {
			setColorValue(defaultValue)
		}
	}

	/**
	 * Read value of this component model from configuration object.
	 *
	 * @param settings the {@link org.knime.core.node.NodeSettings} to read from
	 * @param defaultValue value to be assigned if the value is missing in the settings object
	 */
	private fun SettingsModelInteger.loadSettingsFrom(settings: NodeSettingsRO, defaultValue: Int) {
		try {
			loadSettingsFrom(settings)
		} catch (_: InvalidSettingsException) {
			setIntValue(defaultValue)
		}
	}

	/**
	 * Read value of this component model from configuration object.
	 *
	 * @param settings the {@link org.knime.core.node.NodeSettings} to read from
	 * @param defaultValue value to be assigned if the value is missing in the settings object
	 */
	private fun SettingsModelString.loadSettingsFrom(settings: NodeSettingsRO, defaultValue: String) {
		try {
			loadSettingsFrom(settings)
		} catch (_: InvalidSettingsException) {
			setStringValue(defaultValue)
		}
	}

	/**
	 * Read value of this component model from configuration object.
	 *
	 * @param settings the {@link org.knime.core.node.NodeSettings} to read from
	 * @param defaultValue value to be assigned if the value is missing in the settings object
	 */
	private fun SettingsModelColor.loadSettingsFrom(settings: NodeSettingsRO, defaultValue: Color) {
		try {
			loadSettingsFrom(settings)
		} catch (_: InvalidSettingsException) {
			setColorValue(defaultValue)
		}
	}
}