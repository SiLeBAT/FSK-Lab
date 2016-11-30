package de.bund.bfr.knime.fsklab.nodes.runner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColorChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

public class FskRunnerNodeFactory extends NodeFactory<FskRunnerNodeModel> {

	@Override
	public FskRunnerNodeModel createNodeModel() {
		return new FskRunnerNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 1;
	}

	@Override
	public NodeView<FskRunnerNodeModel> createNodeView(int viewIndex, FskRunnerNodeModel nodeModel) {
		return new FskRunnerNodeView(nodeModel);
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		// return new DefaultNodeSettingsPane();
		DefaultNodeSettingsPane pane = new DefaultNodeSettingsPane();
		pane.createNewGroup("Options");

		FskRunnerNodeSettings settings = new FskRunnerNodeSettings();

		// Width component
		DialogComponentNumberEdit widthComp = new DialogComponentNumberEdit(settings.widthModel, "Width");
		widthComp.setToolTipText("Width of the plot");
		pane.addDialogComponent(widthComp);

		// Height component
		DialogComponentNumberEdit heightComp = new DialogComponentNumberEdit(settings.heightModel, "Height");
		heightComp.setToolTipText("Height of the plot");
		pane.addDialogComponent(heightComp);

		// Resolution component
		DialogComponentString resolutionComp = new DialogComponentString(settings.resolutionModel, "Resolution");
		resolutionComp.setToolTipText(
				"Nominal resolution in ppi which will be recorded in the bitmap file, if a positive integer.");
		pane.addDialogComponent(resolutionComp);

		// Background colour component
		DialogComponentColorChooser colorComp = new DialogComponentColorChooser(settings.colourModel,
				"Background colour", true);
		colorComp.setToolTipText("Background colour");
		pane.addDialogComponent(colorComp);

		// Text point size component
		DialogComponentNumberEdit textPointSizeComp = new DialogComponentNumberEdit(settings.textPointSizeModel,
				"Text point size");
		textPointSizeComp.setToolTipText("Point size of plotted text, interpreted as big points (1/72 inch) at res ppi.");

		pane.addDialogComponent(textPointSizeComp);

		return pane;
	}
}
