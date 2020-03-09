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
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.jfree.chart.JFreeChart;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import de.bund.bfr.knime.foodprocess.lib.FoodProcessDef;
import de.bund.bfr.pcml10.PCMLDocument;

/**
 * @author Armin Weiser
 */
public class ViewUi extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2363842170227621517L;

	private PCMLDocument pcmlDoc = null;
	private MyChartCreator mcc;

	public ViewUi() {
		initComponents();
	}

	public void createChart() {
		if (pcmlDoc != null) {
			if (mcc == null)
				mcc = new MyChartCreator(this);
			JFreeChart jfc = mcc.createChart(pcmlDoc, checkBoxListPanel1.getJCheckBoxList().getSelections(),
					xunits.getSelectedItem().toString(), equidistCheck.isSelected(), plotLinesCheck.isSelected(),
					plotPointsCheck.isSelected());
			manageLegend(mcc.getProcessLegend());
			this.remove(myChartPanel1);
			myChartPanel1 = new MyChartPanel(jfc);
			add(myChartPanel1, CC.xywh(1, 1, 7, 1));
			this.revalidate();
		}
	}

	private void manageLegend(List<ProcessLegendElement> processLegend) {
		panel2.removeAll();
		panel2.add(equidistCheck, CC.xy(1, 1));
		int row = 3;
		for (ProcessLegendElement ple : processLegend) {
			if (row <= 33) {
				panel2.add(ple, CC.xy(1, row));
				row += 2;
			}
		}
	}

	public JCheckBoxListPanel getJCheckBoxListPanel() {
		return checkBoxListPanel1;
	}

	public JComboBox<String> getXUnits() {
		return xunits;
	}

	public boolean isEquidistantProcesses() {
		return equidistCheck.isSelected();
	}

	public void setEquidistantProcesses(boolean b) {
		equidistCheck.setSelected(b);
	}

	public boolean isPlotLines() {
		return plotLinesCheck.isSelected();
	}

	public void setPlotLines(boolean b) {
		plotLinesCheck.setSelected(b);
	}

	public boolean isPlotPoints() {
		return plotPointsCheck.isSelected();
	}

	public void setPlotPoints(boolean b) {
		plotPointsCheck.setSelected(b);
	}

	public void setDoc(PCMLDocument pcmlDoc) {
		this.pcmlDoc = pcmlDoc;
	}

	private void xunitsActionPerformed(ActionEvent e) {
		createChart();
	}

	public void checkBoxChecked(JCheckBox cb) {
		createChart();
	}

	private void equidistCheckActionPerformed(ActionEvent e) {
		createChart();
	}

	private void plotLinesCheckActionPerformed(ActionEvent e) {
		createChart();
	}

	private void plotPointsCheckActionPerformed(ActionEvent e) {
		createChart();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		myChartPanel1 = new MyChartPanel();
		scrollPane1 = new JScrollPane();
		panel1 = new JPanel();
		xunits = new JComboBox<String>();
		xunits.setModel(new DefaultComboBoxModel<String>(FoodProcessDef.COMBO_TIMEUNIT));
		checkBoxListPanel1 = new JCheckBoxListPanel("Parameters to use (for y-axis)", this);
		panel2 = new JPanel();
		equidistCheck = new JCheckBox();
		label1 = new JLabel();

		panel3 = new JPanel();

		plotLinesCheck = new JCheckBox();
		plotLinesCheck.setToolTipText("If enabled draw lines");

		plotPointsCheck = new JCheckBox();
		plotPointsCheck.setToolTipText("If enabled draw points");

		// ======== this ========
		setLayout(new FormLayout("[100dlu,default], 2*($lcgap, default), $lcgap, default:grow",
				"fill:default:grow, $lgap, fill:default"));
		add(myChartPanel1, CC.xywh(1, 1, 7, 1, CC.FILL, CC.FILL));

		// ======== scrollPane1 ========
		{

			// ======== panel1 ========
			{
				panel1.setBorder(new TitledBorder("Units to use (for x-axis)"));
				panel1.setLayout(new FormLayout("default:grow", "default"));

				// ---- xunits ----
				xunits.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						xunitsActionPerformed(e);
					}
				});
				panel1.add(xunits, CC.xy(1, 1));
			}
			scrollPane1.setViewportView(panel1);
		}
		add(scrollPane1, CC.xy(1, 3));
		add(checkBoxListPanel1, CC.xy(3, 3));

		// ======== panel2 ========
		{
			panel2.setBorder(new TitledBorder("Process Legend"));
			panel2.setLayout(new FormLayout("[75dlu,default]", "16*(default, $lgap), default"));

			// ---- equidistCheck ----
			equidistCheck.setText("processes equidistant");
			equidistCheck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					equidistCheckActionPerformed(e);
				}
			});
			panel2.add(equidistCheck, CC.xy(1, 1));

			// ---- label1 ----
			label1.setText("Mischen");
			label1.setBackground(new Color(51, 204, 255));
			label1.setOpaque(true);
			label1.setHorizontalAlignment(SwingConstants.CENTER);
			panel2.add(label1, CC.xy(1, 3));
		}
		add(panel2, CC.xy(5, 3));
		// JFormDesigner - End of component initialization
		// //GEN-END:initComponents

		// ======== panel3 ========
		{
			panel3.setBorder(new TitledBorder("Chart options"));
			panel3.setLayout(new FormLayout("[75dlu,default]", "16*(default, $lgap), default"));

			// ---- plotLinesCheck ----
			plotLinesCheck.setText("Plot lines?");
			plotLinesCheck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					plotLinesCheckActionPerformed(e);
				}
			});
			panel3.add(plotLinesCheck, CC.xy(1, 1));

			// ---- plotPointsCheck ----
			plotPointsCheck.setText("Plot points?");
			plotPointsCheck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					plotPointsCheckActionPerformed(e);
				}
			});
			panel3.add(plotPointsCheck, CC.xy(1, 3));
		}
		add(panel3, CC.xy(7, 3));
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private MyChartPanel myChartPanel1;
	private JScrollPane scrollPane1;
	private JPanel panel1;
	private JComboBox<String> xunits;
	private JCheckBoxListPanel checkBoxListPanel1;
	private JPanel panel2;
	private JCheckBox equidistCheck;
	private JLabel label1;
	// JFormDesigner - End of variables declaration //GEN-END:variables
	private JPanel panel3;
	private JCheckBox plotLinesCheck;
	private JCheckBox plotPointsCheck;
}
