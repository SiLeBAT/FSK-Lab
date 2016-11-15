package de.bund.bfr.knime.fsklab.nodes.metadataeditor;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;

public class FskMetadataEditorNodeFactory extends NodeFactory<FskMetadataEditorNodeModel> implements
		WizardNodeFactoryExtension<FskMetadataEditorNodeModel, FskMetadataEditorViewRepresentation, FskMetadataEditorViewValue> {

	@Override
	public FskMetadataEditorNodeModel createNodeModel() {
		return new FskMetadataEditorNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<FskMetadataEditorNodeModel> createNodeView(int viewIndex, FskMetadataEditorNodeModel nodeModel) {
		return null;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return false;
	}
}
