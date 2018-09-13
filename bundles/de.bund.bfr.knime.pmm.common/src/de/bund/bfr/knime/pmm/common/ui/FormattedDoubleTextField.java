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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FormattedDoubleTextField extends DoubleTextField {

	private static final long serialVersionUID = 1L;

	public FormattedDoubleTextField() {
		super();
	}

	public FormattedDoubleTextField(boolean optional) {
		super(optional);
	}

	public FormattedDoubleTextField(double minValue, double maxValue) {
		super(minValue, maxValue);
	}

	public FormattedDoubleTextField(double minValue, double maxValue,
			boolean optional) {
		super(minValue, maxValue, optional);
	}

	@Override
	protected void formatText() {
		if (getValue() != null) {
			NumberFormat format = new DecimalFormat("0.####",
					new DecimalFormatSymbols(Locale.US));

			setText(format.format(getValue()));
		}
	}

}
