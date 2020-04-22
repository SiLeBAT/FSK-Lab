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
package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.common.ParametricModel;

/**
 * @author Armin Weiser
 */
public class SecDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, ParametricModel> m_secondaryModels;
	private String m_depVar;
	private MMC_M m_m2;
	
	public SecDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public SecDialog(Dialog owner) {
		super(owner);
		initComponents();
	}
	
	public void setPanel(final MMC_M m2, final String depVar, final HashMap<String, ParametricModel> secondaryModels) {
		m_m2 = m2;
		m_secondaryModels = secondaryModels;
		m_depVar = depVar;
		contentPanel.add(m2, CC.xy(1, 1));
	}

	private void okButtonActionPerformed(ActionEvent e) {
		ParametricModel pm = m_m2.getPM();
		if (!pm.getFormula().isEmpty()) {
			pm.setDepVar(m_depVar, false); // true; Hier: false wichtig, sonst geht was beim Speichern schief, siehe Ticket #329
			m_secondaryModels.put(m_depVar, pm);
		}
		this.dispose();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void deleteButtonActionPerformed(ActionEvent e) {
		m_secondaryModels.remove(m_depVar);
		this.dispose();
	}

	@SuppressWarnings("deprecation")
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		buttonBar = new JPanel();
		okButton = new JButton();
		deleteButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					"default:grow",
					"fill:default:grow"));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
				buttonBar.setLayout(new FormLayout(
					"$lcgap, default, $glue, $button, $rgap, default, $lcgap, $button",
					"pref"));
				((FormLayout)buttonBar.getLayout()).setColumnGroups(new int[][] {{4, 6, 8}});

				//---- okButton ----
				okButton.setText("OK");
				okButton.setPreferredSize(new Dimension(90, 25));
				okButton.setMaximumSize(new Dimension(90, 25));
				okButton.setMinimumSize(new Dimension(90, 25));
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, CC.xy(4, 1));

				//---- deleteButton ----
				deleteButton.setText("Delete");
				deleteButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						deleteButtonActionPerformed(e);
					}
				});
				buttonBar.add(deleteButton, CC.xy(6, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.setPreferredSize(new Dimension(90, 25));
				cancelButton.setMinimumSize(new Dimension(90, 25));
				cancelButton.setMaximumSize(new Dimension(90, 25));
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, CC.xy(8, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton deleteButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
