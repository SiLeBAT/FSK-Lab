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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class JCheckBoxListPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6500091242327647124L;
	
	private JCheckBoxList cb;
	private JCheckBox allNoneBox;
	private DefaultListModel<JCheckboxWithObject> listModel;
	private ViewUi viewUi;

	public JCheckBoxListPanel() {
		this("title", null);
	}
	public JCheckBoxListPanel(String title, ViewUi viewUi) {
		this.viewUi = viewUi;
		
		cb = new JCheckBoxList(this);
		listModel = new DefaultListModel<JCheckboxWithObject>();
		cb.setModel(listModel);
		
		JPanel northPanel = new JPanel();

		northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		allNoneBox = new JCheckBox("All/None");
		allNoneBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (allNoneBox.isSelected()) cb.selectAll();
                else cb.deselectAll();
                checkBoxChecked(null);
            }
        });	
		northPanel.add(allNoneBox);		

		this.setBorder(BorderFactory.createTitledBorder(title));
		this.setLayout(new BorderLayout());
		this.add(northPanel, BorderLayout.NORTH);
		this.add(new JScrollPane(cb,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	}
	public void checkBoxChecked(JCheckBox cb) {
		viewUi.checkBoxChecked(cb);
	}
	
	public JCheckBoxList getJCheckBoxList() {
		return cb;
	}
	public DefaultListModel<JCheckboxWithObject> getDefaultListModel() {
		return listModel;
	}
}
