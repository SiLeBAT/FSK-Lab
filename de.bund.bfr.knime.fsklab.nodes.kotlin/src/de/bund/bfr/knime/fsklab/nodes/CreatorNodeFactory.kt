package de.bund.bfr.knime.fsklab.nodes

import org.knime.core.node.NodeDialogPane
import org.knime.core.node.NodeFactory
import org.knime.core.node.NodeView
import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;

class CreatorNodeFactory : NodeFactory<CreatorNodeModel>() {
	
	override fun createNodeModel() = CreatorNodeModel()
	
	override fun getNrNodeViews() = 0
	
	override fun createNodeView(viewIndex: Int, nodeModel: CreatorNodeModel) : NodeView<CreatorNodeModel>? = null
	
	override fun hasDialog() = true
	
	override fun createNodeDialogPane() : NodeDialogPane {
		
		val settings = CreatorNodeSettings()
		
		// Create components
		val dlgType = JFileChooser.OPEN_DIALOG
		val rFilters = ".r|.R"  // Extension filters for the R script
		
		val modelScriptChooser = DialogComponentFileChooser(settings.modelScript, "modelScript-history", dlgType, rFilters)
		modelScriptChooser.setBorderTitle("Model script (*)")
		modelScriptChooser.setToolTipText("Script that calculates the values of the model (mandatory)")
		
		val paramScriptChooser = DialogComponentFileChooser(settings.paramScript, "paramScript-history", dlgType, rFilters)
		paramScriptChooser.setBorderTitle("Parameters script")
		paramScriptChooser.setToolTipText("Script with the parameter values of the model (Optional)")
		
		val vizScriptChooser = DialogComponentFileChooser(settings.vizScript, "vizScript-history", dlgType, rFilters)
		vizScriptChooser.setBorderTitle("Visualization script")
		vizScriptChooser.setToolTipText(
				"Script with a number of commands to create plots or charts using the simulation results (Optional)")
		
		val metaDataChooser = DialogComponentFileChooser(settings.metaDataDoc, "metaData-history", dlgType)
		metaDataChooser.setBorderTitle("XLSX spreadsheet")
		metaDataChooser.setToolTipText("XLSX file with model metadata (Optional)")
		
		// Create pane and add components
		val pane = DefaultNodeSettingsPane()
		pane.addDialogComponent(modelScriptChooser)
		pane.addDialogComponent(paramScriptChooser)
		pane.addDialogComponent(vizScriptChooser)
		pane.addDialogComponent(metaDataChooser)
		
		return pane
	}
}