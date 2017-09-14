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
package de.bund.bfr.knime.fsklab.nodes

import org.knime.core.node.NodeFactory
import org.knime.core.node.NodeView

class EditorNodeFactory : NodeFactory<EditorNodeModel>() {

    override fun createNodeModel() = EditorNodeModel()
    override fun getNrNodeViews() = 0
    override fun createNodeView(viewIndex: Int, nodeModel: EditorNodeModel): NodeView<EditorNodeModel> {
        throw UnsupportedOperationException("No views! $viewIndex")
    }

    override fun hasDialog() = true
    override fun createNodeDialogPane() = EditorNodeDialog()
}