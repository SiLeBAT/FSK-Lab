/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab.nodes;

import java.util.List;
import java.util.stream.Collectors;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.MathContainer;

/**
 * Create the {@link ASTNode} corresponding to a selector node with all the values of an array.
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
    return node.getChild(0).getChildren().stream().map(ASTNode::getReal)
        .collect(Collectors.toList());
  }
}
