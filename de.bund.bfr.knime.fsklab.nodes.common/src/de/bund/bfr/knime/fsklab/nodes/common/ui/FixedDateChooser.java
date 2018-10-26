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
package de.bund.bfr.knime.fsklab.nodes.common.ui;

import com.toedter.calendar.JDateChooser;

/** Fixes JDateChooser and disables the text field. */
public class FixedDateChooser extends JDateChooser {

  private static final long serialVersionUID = 2475793638936369100L;

  public FixedDateChooser() {

    // Fixes bug AP-5865
    popup.setFocusable(false);

    /*
     * Text field is disabled so that the dates are only chooseable through the calendar widget.
     * Then there is no need to validate the dates.
     */
    dateEditor.setEnabled(true);
  }
}
