package de.bund.bfr.knime.pmm.fskx.runner;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

public class FskxRunnerNodeFactory extends NodeFactory<FsxkRunnerNodeModel> {

	@Override
	public FsxkRunnerNodeModel createNodeModel() {
		return new FsxkRunnerNodeModel();
	}

	@Override
	protected int getNrNodeViews() {
		return 1;
	}

	@Override
	public NodeView<FsxkRunnerNodeModel> createNodeView(int viewIndex, FsxkRunnerNodeModel nodeModel) {
		return new FskxRunnerNodeView(nodeModel);
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new DefaultNodeSettingsPane();
	}
}
