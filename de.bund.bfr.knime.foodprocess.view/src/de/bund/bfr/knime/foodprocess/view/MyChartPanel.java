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
package de.bund.bfr.knime.foodprocess.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.AxisEntity;

public class MyChartPanel extends ChartPanel implements MouseListener, MouseMotionListener  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3272643950775446573L;
    private int lastPanY = -1;
    private ValueAxis rangeAxis = null;

	public MyChartPanel() {
		this(null);
	}
	public MyChartPanel(JFreeChart jfc) {
		super(jfc);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
     * Handles a 'mouse pressed' event.
     * <P>
     * This event is the popup trigger on Unix/Linux. For Windows, the popup trigger is the 'mouse
     * released' event.
     * 
     * @param e The mouse event.
     */
	@Override
    public void mousePressed(MouseEvent e) {
        rangeAxis = null;
        if (e.isControlDown() && SwingUtilities.isLeftMouseButton(e)) {
        	Rectangle2D screenDataArea = getScreenDataArea(e.getX(), e.getY());
        	if (e.getX() < screenDataArea.getMinX() || e.getX() > screenDataArea.getMaxX()) {
                lastPanY = e.getY();
                Object eo = this.getEntityForPoint(e.getX(), e.getY());
                if (eo != null && eo instanceof AxisEntity) {
					AxisEntity ae = (AxisEntity) eo;
					rangeAxis = (NumberAxis) ae.getAxis();
                }
                return;
        	}
        }
        super.mousePressed(e);
    }

    /**
     * Handles a 'mouse dragged' event.
     * 
     * @param e the mouse event.
     */
	@Override
    public void mouseDragged(MouseEvent e) {
		if (rangeAxis != null) {
	    	moveYAxis(lastPanY, e.getY());
	        lastPanY = e.getY();			
	        return;
		}
        super.mouseDragged(e);
    }

    private void moveYAxis(int oldY, int newY) {
        double diffY = newY - oldY;

        // check for change
        if (diffY == 0) return;

        try {
        	getChart().setNotify(false);

        	Rectangle2D chartArea = this.getChartRenderingInfo().getChartArea();
        	diffY = diffY * (rangeAxis.getUpperBound() - rangeAxis.getLowerBound()) / (chartArea.getMaxY() - chartArea.getMinY());
            rangeAxis.setRange(rangeAxis.getLowerBound() + diffY, rangeAxis.getUpperBound() + diffY);
        }
        finally {
        	getChart().setNotify(true);
        }
    }
}
