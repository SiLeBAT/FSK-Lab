package de.bund.bfr.knime.fsklab.nodes.editor;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

public class FskEditorNodeFactory extends NodeFactory<FskEditorNodeModel> {
  
  /** {@inheritDoc} */
  @Override
  public FskEditorNodeModel createNodeModel() {
    return new FskEditorNodeModel();
  }
  
  /** {@inheritDoc} */
  @Override
  protected int getNrNodeViews() {
    return 0;
  }

  /** {@inheritDoc} */
  @Override
  public NodeView<FskEditorNodeModel> createNodeView(int viewIndex,
      FskEditorNodeModel nodeModel) {
    return null;
  }
  
  /** {@inheritDoc} */
  @Override
  protected boolean hasDialog() {
    return true;
  }
  
  /** {@inheritDoc} */
  @Override
  protected NodeDialogPane createNodeDialogPane() {
    return new FskEditorNodeDialog();
  }
}
