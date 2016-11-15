package de.bund.bfr.knime.pmm.fskx.creator;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

import de.bund.bfr.knime.fsklab.nodes.creator.FskCreatorNodeModel;
import de.bund.bfr.knime.fsklab.nodes.creator.FskCreatorNodeDialog;

public class FskxCreatorNodeFactory extends NodeFactory<FskCreatorNodeModel> {
  
  /** {@inheritDoc} */
  @Override
  public FskCreatorNodeModel createNodeModel() {
    return new FskCreatorNodeModel();
  }
  
  /** {@inheritDoc} */
  @Override
  public int getNrNodeViews() {
    return 0;
  }
  
  /** {@inheritDoc} */
  @Override
  public NodeView<FskCreatorNodeModel> createNodeView(final int viewIndex, final FskCreatorNodeModel nodeModel) {
    return null;
  }
  
  /** {@inheritDoc} */
  @Override
  public boolean hasDialog() {
    return true;
  }
  
  /** {@inheritDoc} */
  @Override
  public NodeDialogPane createNodeDialogPane() {
    return new FskCreatorNodeDialog();
  }
}
