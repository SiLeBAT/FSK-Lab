package de.bund.bfr.knime.pmm.editor;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;

public class ModelEditorNodeFactory extends NodeFactory<ModelEditorNodeModel> implements
		WizardNodeFactoryExtension<ModelEditorNodeModel, ModelEditorViewRepresentation, ModelEditorViewValue> {

	@Override
	public ModelEditorNodeModel createNodeModel() {
		return new ModelEditorNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<ModelEditorNodeModel> createNodeView(int viewIndex, ModelEditorNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return false;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return null;
	}

}
