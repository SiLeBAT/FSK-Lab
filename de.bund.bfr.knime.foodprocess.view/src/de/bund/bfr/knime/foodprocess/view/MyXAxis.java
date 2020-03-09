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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.ui.RectangleEdge;

import de.bund.bfr.knime.pmm.common.chart.ColorAndShapeCreator;

public class MyXAxis extends NumberAxis {

	private static final long serialVersionUID = 1L;

	//private LinkedHashMap<Point2D.Double, String> ranges;
	private LinkedList<Point2D.Double> ranges = new LinkedList<Point2D.Double>();
	private boolean equidistantProcesses;
	//private LinkedList<String> processNames = new LinkedList<String>();

	public MyXAxis(String label, LinkedList<Point2D.Double> ranges, LinkedList<String> processNames, boolean equidistantProcesses) {
		super(equidistantProcesses ? "Process" : label);
		this.ranges = ranges;
		this.equidistantProcesses = equidistantProcesses;
		//this.processNames = processNames;
	}

	@Override
	protected void drawAxisLine(Graphics2D g2, double cursor,
			Rectangle2D dataArea, RectangleEdge edge) {
		Line2D axisLine = null;
		if (edge == RectangleEdge.TOP) {
			axisLine = new Line2D.Double(dataArea.getX(), cursor,
					dataArea.getMaxX(), cursor);
		} else if (edge == RectangleEdge.BOTTOM) {
			axisLine = new Line2D.Double(dataArea.getX(), cursor,
					dataArea.getMaxX(), cursor);
		} else if (edge == RectangleEdge.LEFT) {
			axisLine = new Line2D.Double(cursor, dataArea.getY(), cursor,
					dataArea.getMaxY());
		} else if (edge == RectangleEdge.RIGHT) {
			axisLine = new Line2D.Double(cursor, dataArea.getY(), cursor,
					dataArea.getMaxY());
		}
		g2.setPaint(getAxisLinePaint());
		g2.setStroke(getAxisLineStroke());
		g2.draw(axisLine);

		/* ------------------------------------------------------------------ */

		List<Color> colorList = new ColorAndShapeCreator(ranges.size()).getColorList();

	    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 8));
	    //FontMetrics fm = g2.getFontMetrics();	    
	    int i = 0;
		for (Point2D.Double range : ranges) {
			Line2D rangeLine = null;
			float xx1, xx2;
			if (equidistantProcesses) {
				xx1 = (float) valueToJava2D(i, dataArea, edge);
				xx2 = (float) valueToJava2D(i+1, dataArea, edge);
			}
			else {
				xx1 = (float) valueToJava2D(range.x, dataArea, edge);
				xx2 = (float) valueToJava2D(range.y, dataArea, edge);
			}

			if (edge == RectangleEdge.TOP) {
				rangeLine = new Line2D.Double(xx1, cursor, xx2, cursor);
			} else if (edge == RectangleEdge.BOTTOM) {
				rangeLine = new Line2D.Double(xx1, cursor - (10*(i%4)), xx2, cursor - (10*(i%4)));
			} else if (edge == RectangleEdge.LEFT) {
				rangeLine = new Line2D.Double(cursor, xx1, cursor, xx2);
			} else if (edge == RectangleEdge.RIGHT) {
				rangeLine = new Line2D.Double(cursor, xx1, cursor, xx2);
			}

			Color c = colorList.get(i);
			g2.setPaint(c);
			g2.setStroke(new BasicStroke(8.0f));
			g2.draw(rangeLine);
			/*
		    Rectangle2D r = fm.getStringBounds(processNames.get(i), g2);
			g2.drawString(processNames.get(i), (float) ((xx2+xx1)/2 - r.getWidth() / 2), (float)cursor - (5 + (10*(i%4))));
			*/
			i++;
		}

		/* ------------------------------------------------------------------ */

		boolean drawUpOrRight = false;
		boolean drawDownOrLeft = false;
		if (this.isPositiveArrowVisible()) {
			if (this.isInverted()) {
				drawDownOrLeft = true;
			} else {
				drawUpOrRight = true;
			}
		}
		if (this.isNegativeArrowVisible()) {
			if (this.isInverted()) {
				drawUpOrRight = true;
			} else {
				drawDownOrLeft = true;
			}
		}
		if (drawUpOrRight) {
			double x = 0.0;
			double y = 0.0;
			Shape arrow = null;
			if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
				x = dataArea.getMaxX();
				y = cursor;
				arrow = this.getRightArrow();
			} else if (edge == RectangleEdge.LEFT
					|| edge == RectangleEdge.RIGHT) {
				x = cursor;
				y = dataArea.getMinY();
				arrow = this.getUpArrow();
			}

			// draw the arrow...
			AffineTransform transformer = new AffineTransform();
			transformer.setToTranslation(x, y);
			Shape shape = transformer.createTransformedShape(arrow);
			g2.fill(shape);
			g2.draw(shape);
		}

		if (drawDownOrLeft) {
			double x = 0.0;
			double y = 0.0;
			Shape arrow = null;
			if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
				x = dataArea.getMinX();
				y = cursor;
				arrow = this.getLeftArrow();
			} else if (edge == RectangleEdge.LEFT
					|| edge == RectangleEdge.RIGHT) {
				x = cursor;
				y = dataArea.getMaxY();
				arrow = this.getDownArrow();
			}

			// draw the arrow...
			AffineTransform transformer = new AffineTransform();
			transformer.setToTranslation(x, y);
			Shape shape = transformer.createTransformedShape(arrow);
			g2.fill(shape);
			g2.draw(shape);
		}
	}

}
