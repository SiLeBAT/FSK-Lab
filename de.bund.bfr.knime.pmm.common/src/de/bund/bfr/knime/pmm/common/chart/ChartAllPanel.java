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
package de.bund.bfr.knime.pmm.common.chart;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

public class ChartAllPanel extends JPanel implements ComponentListener {

	private static final long serialVersionUID = 1L;

	private JSplitPane verticalSplitPane;
	private JSplitPane horizontalSplitPane;

	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartSamplePanel samplePanel;

	private boolean verticalPaneAdjusted;
	private boolean horizontalPaneAdjusted;

	public ChartAllPanel(ChartCreator chartCreator,
			ChartSelectionPanel selectionPanel, ChartConfigPanel configPanel) {
		verticalPaneAdjusted = false;
		horizontalPaneAdjusted = false;

		this.selectionPanel = selectionPanel;
		this.configPanel = configPanel;
		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(chartCreator, BorderLayout.CENTER);
		upperPanel.add(new JScrollPane(configPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.EAST);

		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				upperPanel, selectionPanel);
		verticalSplitPane.addComponentListener(this);

		setLayout(new BorderLayout());
		add(verticalSplitPane, BorderLayout.CENTER);
	}

	public ChartAllPanel(ChartCreator chartCreator,
			ChartSelectionPanel selectionPanel, ChartConfigPanel configPanel,
			ChartSamplePanel samplePanel) {
		verticalPaneAdjusted = false;
		horizontalPaneAdjusted = false;

		this.selectionPanel = selectionPanel;
		this.configPanel = configPanel;
		this.samplePanel = samplePanel;
		JPanel upperPanel = new JPanel();

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(chartCreator, BorderLayout.CENTER);
		upperPanel.add(new JScrollPane(configPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.EAST);

		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				selectionPanel, samplePanel);
		horizontalSplitPane.addComponentListener(this);

		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				upperPanel, horizontalSplitPane);
		verticalSplitPane.addComponentListener(this);

		setLayout(new BorderLayout());
		add(verticalSplitPane, BorderLayout.CENTER);
	}

	public ChartSelectionPanel getSelectionPanel() {
		return selectionPanel;
	}

	public ChartConfigPanel getConfigPanel() {
		return configPanel;
	}

	public ChartSamplePanel getSamplePanel() {
		return samplePanel;
	}

	public int getVerticalDividerLocation() {
		if (verticalSplitPane == null) {
			return -1;
		}

		return verticalSplitPane.getDividerLocation();
	}

	public void setVerticalDividerLocation(int location) {
		if (verticalSplitPane != null) {
			verticalSplitPane.setDividerLocation(location);
		}
	}

	public int getHorizontalDividerLocation() {
		if (horizontalSplitPane == null) {
			return -1;
		}

		return horizontalSplitPane.getDividerLocation();
	}

	public void setHorizontalDividerLocation(int location) {
		if (horizontalSplitPane != null) {
			horizontalSplitPane.setDividerLocation(location);
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if (e.getComponent() == verticalSplitPane && !verticalPaneAdjusted
				&& verticalSplitPane.getWidth() > 0
				&& verticalSplitPane.getHeight() > 0) {
			verticalSplitPane.setDividerLocation(0.5);
			verticalPaneAdjusted = true;
		}

		if (e.getComponent() == horizontalSplitPane && !horizontalPaneAdjusted
				&& horizontalSplitPane.getWidth() > 0
				&& horizontalSplitPane.getHeight() > 0) {
			horizontalSplitPane.setDividerLocation(0.5);
			horizontalPaneAdjusted = true;
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
}
