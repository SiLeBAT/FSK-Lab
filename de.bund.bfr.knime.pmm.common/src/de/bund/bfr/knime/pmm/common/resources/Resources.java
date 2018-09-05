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
package de.bund.bfr.knime.pmm.common.resources;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Resources {

	private static Resources instance = null;

	private Resources() {
	}

	public static Resources getInstance() {
		if (instance == null) {
			instance = new Resources();
		}

		return instance;
	}

	public Image getDefaultIcon() {
		URL imgURL = getClass().getResource("knime_default.png");		
		
		try {
			return ImageIO.read(imgURL);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
