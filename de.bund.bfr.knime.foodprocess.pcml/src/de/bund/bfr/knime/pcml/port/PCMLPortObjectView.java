/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pcml.port;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeView;
import org.knime.core.node.port.pmml.XMLTreeCreator;
import org.xml.sax.InputSource;

/**
 * The view for the PCML Port Object.
 * 
 * @author Heiko Hofer
 */
@SuppressWarnings("serial")
public class PCMLPortObjectView extends JComponent {
	private static final NodeLogger LOGGER = NodeLogger.getLogger(
            PCMLPortObjectView.class);
	
	public PCMLPortObjectView(final PCMLPortObject portObject) {
		this.setLayout(new BorderLayout());
		this.setBackground(NodeView.COLOR_BACKGROUND);
	    
		this.setName("PCML model");
	    	    
	    PCMLPortObjectSpec spec = (PCMLPortObjectSpec) portObject.getSpec();
	    JScrollPane htmlScrollPane = new JScrollPane(PCMLPortObjectSpecView.getEditor(spec));
        
	    JTree tree = new JTree();
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PCMLFormatter.save(portObject.getPcmlDoc(), out);
	        out.close();
			SAXParserFactory saxFac = SAXParserFactory.newInstance();
			SAXParser parser = saxFac.newSAXParser();
			XMLTreeCreator treeCreator = new XMLTreeCreator();
			parser.parse(
					new InputSource(new ByteArrayInputStream(out
							.toByteArray())), treeCreator);
			tree.setModel(new DefaultTreeModel(treeCreator.getTreeNode()));
			JScrollPane xmlScrollPane = new JScrollPane(tree);
			
		    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, htmlScrollPane, xmlScrollPane);
		    splitPane.setOneTouchExpandable(true);
		    this.add(splitPane);
		    this.setPreferredSize(htmlScrollPane.getPreferredSize());
			this.revalidate();
		    splitPane.setDividerLocation(htmlScrollPane.getPreferredSize().height + 100);
		}
		catch (Exception e) {
			// log and return a "error during saving" component
			LOGGER.error("PMML contains errors", e);
			PCMLPortObjectView.this.add(new JLabel("PCML contains errors: "
					+ e.getMessage()));
		}
	}
}
