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
package de.bund.bfr.knime.pmm.common.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.UIManager;

public class QualityComboBox extends JComboBox<Color> {

	private static final long serialVersionUID = 1L;

	public QualityComboBox() {
		super(new Color[] { Color.WHITE, Color.GREEN, Color.YELLOW, Color.RED });
		setRenderer(new ColorListRenderer());
	}

	public Integer getQuality() {
		Color color = (Color) getSelectedItem();

		if (color.equals(Color.GREEN)) {
			return 1;
		} else if (color.equals(Color.YELLOW)) {
			return 2;
		} else if (color.equals(Color.RED)) {
			return 3;
		}
		
		return null;
	}

	public void setQuality(Integer quality) {
		Color color = null;
		
		if (quality == null) {
			color = Color.WHITE;
		} else if (quality == 1) {
			color = Color.GREEN;
		} else if (quality == 2) {
			color = Color.YELLOW;
		} else if (quality == 3) {
			color = Color.RED;
		}
		
		setSelectedItem(color);
	}

	private class ColorListRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 1L;

		private Color color;
		private boolean isSelected;

		public ColorListRenderer() {
			color = Color.WHITE;
			isSelected = false;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			color = (Color) value;
			this.isSelected = isSelected;

			return super.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Rectangle rect = g.getClipBounds();

			if (rect != null) {
				g.setColor(color);
				g.fillRect(rect.x, rect.y, rect.width, rect.height);

				if (isSelected) {
					g.setColor(UIManager.getDefaults().getColor(
							"List.selectionBackground"));
				} else {
					g.setColor(UIManager.getDefaults().getColor(
							"List.background"));
				}

				((Graphics2D) g).setStroke(new BasicStroke(5));
				g.drawRect(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}
}
