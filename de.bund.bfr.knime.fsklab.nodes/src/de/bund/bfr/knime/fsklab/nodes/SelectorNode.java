package de.bund.bfr.knime.fsklab.nodes;

import java.util.List;
import java.util.stream.Collectors;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.MathContainer;

/**
 * Create the {@link ASTNode} corresponding to a selector node with all the
 * values of an array.
 * 
 * Structure: {@code
 * <apply>
 *   <selector>
 *   <vector>
 *     <cn>0</cn>
 *     <cn>1</cn>
 *     ...
 *     <cn>n</cn>
 *   </vector>
 * </apply>
 * }
 */
public class SelectorNode {

	public final ASTNode node;

	public SelectorNode(final List<Double> numbers, MathContainer mc) {
		node = new ASTNode(ASTNode.Type.FUNCTION_SELECTOR, mc);
		ASTNode vectorNode = new ASTNode(ASTNode.Type.VECTOR, mc);
		numbers.forEach(d -> vectorNode.addChild(new ASTNode(d)));
		node.addChild(vectorNode);
	}
	
	public SelectorNode(final ASTNode node) {
		this.node = node;
	}

	public List<Double> getArray() {
		return node.getChild(0).getChildren().stream().map(ASTNode::getReal).collect(Collectors.toList());
	}
}
