package de.bund.bfr.knime.fsklab.nodes.editor.js;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;
import org.knime.core.node.NodeView;

public class FskEditorNodeFactory
	extends NodeFactory<FskEditorNodeModel>
	implements WizardNodeFactoryExtension<FskEditorNodeModel, FskEditorViewRepresentation, FskEditorViewValue> {

	@Override
	public FskEditorNodeModel createNodeModel() {
		return new FskEditorNodeModel();
	}
	
	@Override
	protected int getNrNodeViews() {
		return 0;
	}
	
	public NodeView<FskEditorNodeModel> createNodeView(int viewIndex, FskEditorNodeModel nodeModel) {
		return null;
	};
	
	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return null;
	}
	
	@Override
	protected boolean hasDialog() {
		return false;
	}
}
