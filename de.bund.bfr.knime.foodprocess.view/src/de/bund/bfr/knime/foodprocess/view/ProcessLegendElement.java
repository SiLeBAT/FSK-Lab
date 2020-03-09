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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ProcessLegendElement extends JLabel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -160476636400024276L;
	private ViewUi vui;
	
	public ProcessLegendElement(String processName, Color color, ViewUi vui) {
		this.vui = vui;
		this.setOpaque(true);
		//this.setSelected(true);
		this.setBackground(color);
		this.setForeground(getForeGroundColorBasedOnBGBrightness(color));
		this.setText(processName);
		this.setHorizontalAlignment(SwingConstants.LEFT);
		//this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
        if (vui != null) {
        	vui.checkBoxChecked(null);
        }
	}
	
	private Color getForeGroundColorBasedOnBGBrightness(Color color) {
		if (color == null) return Color.BLACK;
	    if (getBrightness(color) < 130) return Color.WHITE;
	    else return Color.BLACK;
	}	
	private int getBrightness(Color c) {
	    return (int) Math.sqrt(
	      c.getRed() * c.getRed() * .241 +
	      c.getGreen() * c.getGreen() * .691 +
	      c.getBlue() * c.getBlue() * .068);
	}
}
