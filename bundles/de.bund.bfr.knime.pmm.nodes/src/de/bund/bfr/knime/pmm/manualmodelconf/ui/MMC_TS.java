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

import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;
import org.jdom2.JDOMException;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.PmmConstants;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.*;
import de.bund.bfr.knime.pmm.common.units.Categories;

/**
 * @author Armin Weiser
 */
public class MMC_TS extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PmmTimeSeries theTS;
		
	public MMC_TS() {
		initComponents();
		theTS = null;
	}

	public Integer getCondId() {
		return theTS == null ? null : theTS.getCondId();
	}
	public PmmTimeSeries getTS() throws PmmException {
		fillTS();
		return theTS;
	}
	/*
	private PmmTimeSeries getNewTS() {
		if (!temperatureField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		if (!phField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		if (!waterActivityField.isValueValid()) {
			throw new PmmException("Invalid Value");
		}

		PmmTimeSeries tuple = new PmmTimeSeries();

		int agentID = MathUtilities.getRandomNegativeInt();
		int matrixID = MathUtilities.getRandomNegativeInt();
		try {agentID = Integer.parseInt(agensIDField.getText());}
		catch (Exception e) {}
		try {matrixID = Integer.parseInt(matrixIDField.getText());}
		catch (Exception e) {}
		
		PmmXmlDoc matDoc = new PmmXmlDoc(); 
		MatrixXml mx = new MatrixXml(matrixID, matrixField.getText(), matrixDetailField.getText());
		matDoc.add(mx);
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matDoc);
		PmmXmlDoc agtDoc = new PmmXmlDoc(); 
		AgentXml ax = new AgentXml(agentID, agentField.getText(), agensDetailField.getText());
		agtDoc.add(ax);
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, agtDoc);
		PmmXmlDoc mdInfoDoc = new PmmXmlDoc();
		int ri = MathUtilities.getRandomNegativeInt();
		if (theTS != null) ri = theTS.getCondId();
		MdInfoXml mdix = new MdInfoXml(ri, "i"+ri, commentField.getText(), null, null);
		mdInfoDoc.add(mdix);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		tuple.setCondId(ri);
		tuple.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID,AttributeUtilities.ATT_TEMPERATURE,AttributeUtilities.ATT_TEMPERATURE,temperatureField.getValue(),"°C");
		tuple.addMisc(AttributeUtilities.ATT_PH_ID,AttributeUtilities.ATT_PH,AttributeUtilities.ATT_PH,phField.getValue(),null);
		tuple.addMisc(AttributeUtilities.ATT_AW_ID,AttributeUtilities.ATT_WATERACTIVITY,AttributeUtilities.ATT_WATERACTIVITY,waterActivityField.getValue(),null);

		return tuple;		
	}
	*/
	private void fillTS() {
		// @Todo: dbuuid muss evtl. noch angepasst werden!!!! in MatrixXml, AgentXml
		if (theTS == null) theTS = new PmmTimeSeries();
		
			int agentID = MathUtilities.getRandomNegativeInt();
			int matrixID = MathUtilities.getRandomNegativeInt();
			try {agentID = Integer.parseInt(agensIDField.getText());}
			catch (Exception e) {}
			try {matrixID = Integer.parseInt(matrixIDField.getText());}
			catch (Exception e) {}
			
			
			PmmXmlDoc matDoc = new PmmXmlDoc(); 
			MatrixXml mx = new MatrixXml(matrixID, matrixField.getText(), matrixDetailField.getText(), null);
			matDoc.add(mx);
			theTS.setValue(TimeSeriesSchema.ATT_MATRIX, matDoc);
			
			PmmXmlDoc agtDoc = new PmmXmlDoc(); //theTS.getAgent();
			AgentXml ax = new AgentXml(agentID, agentField.getText(), agensDetailField.getText(), null);
			agtDoc.add(ax);
			theTS.setValue(TimeSeriesSchema.ATT_AGENT, agtDoc);
			
			PmmXmlDoc mdInfoDoc = theTS.getMdInfo();
			MdInfoXml mdix = (MdInfoXml) mdInfoDoc.get(0);
			mdix.setComment(commentField.getText());
			mdInfoDoc = new PmmXmlDoc();
			mdInfoDoc.add(mdix);
			theTS.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);
			
			List<String> cl = new ArrayList<>();
			cl.add("Temperature");
			theTS.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID,AttributeUtilities.ATT_TEMPERATURE,AttributeUtilities.ATT_TEMPERATURE,temperatureField.getValue(),cl,tempUnit.getSelectedIndex() >= 0 ? tempUnit.getSelectedItem().toString() : "°C");
			cl = new ArrayList<>();cl.add(Categories.getPhCategory().getName());
			theTS.addMisc(AttributeUtilities.ATT_PH_ID,AttributeUtilities.ATT_PH,AttributeUtilities.ATT_PH,phField.getValue(),cl,Categories.getPhCategory().getAllUnits().toArray(new String[0])[0]);
			cl = new ArrayList<>();cl.add(Categories.getAwCategory().getName());
			theTS.addMisc(AttributeUtilities.ATT_AW_ID,AttributeUtilities.ATT_AW,AttributeUtilities.ATT_AW,waterActivityField.getValue(),cl,Categories.getAwCategory().getAllUnits().toArray(new String[0])[1]);
	}
	private void fillFields() {
		agentField.setText(theTS.getAgentName() == null ? "" : theTS.getAgentName());
		matrixField.setText(theTS.getMatrixName() == null ? "" : theTS.getMatrixName());
		agensDetailField.setText(theTS.getAgentDetail() == null ? "" : theTS.getAgentDetail());
		matrixDetailField.setText(theTS.getMatrixDetail() == null ? "" : theTS.getMatrixDetail());
		agensIDField.setText(theTS.getAgentId() == null ? "" : theTS.getAgentId()+"");
		matrixIDField.setText(theTS.getMatrixId() == null ? "" : theTS.getMatrixId()+"");
		commentField.setText(theTS.getComment());
		if (theTS.getTemperature() != null && !Double.isNaN(theTS.getTemperature())) temperatureField.setValue(theTS.getTemperature()); else temperatureField.setText("");
		if (theTS.getTemperatureUnit() != null) tempUnit.setSelectedItem(theTS.getTemperatureUnit()); else tempUnit.setSelectedIndex(-1);
		if (theTS.getPh() != null && !Double.isNaN(theTS.getPh())) phField.setValue(theTS.getPh()); else phField.setText("");
		if (theTS.getWaterActivity() != null && !Double.isNaN(theTS.getWaterActivity())) waterActivityField.setValue(theTS.getWaterActivity()); else waterActivityField.setText("");		
	}
	public void setTS(PmmTimeSeries ts) throws PmmException {
		theTS = ts;
		fillFields();
	}
	public void setTS(String agent, String agentDetail, Integer agentID, String matrix, String matrixDetail, Integer matrixID,
			String comment, double temperature, String tempUnit, double ph, double aw) throws PmmException {
		
		theTS = new PmmTimeSeries();
		
		PmmXmlDoc matDoc = new PmmXmlDoc(); 
		MatrixXml mx = new MatrixXml(matrixID, matrix, matrixDetail);
		matDoc.add(mx);
		theTS.setValue(TimeSeriesSchema.ATT_MATRIX, matDoc);
		PmmXmlDoc agtDoc = new PmmXmlDoc(); 
		AgentXml ax = new AgentXml(agentID, agent, agentDetail);
		agtDoc.add(ax);
		theTS.setValue(TimeSeriesSchema.ATT_AGENT, agtDoc);
		PmmXmlDoc mdInfoDoc = new PmmXmlDoc();
		int ri = MathUtilities.getRandomNegativeInt();
		if (theTS != null) ri = theTS.getCondId();
		MdInfoXml mdix = new MdInfoXml(ri, "i"+ri, comment, null, null);
		mdInfoDoc.add(mdix);
		theTS.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);
		theTS.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		theTS.setCondId(ri);
		List<String> cl = new ArrayList<>();
		cl.add("Temperature");
		theTS.addMisc(AttributeUtilities.ATT_TEMPERATURE_ID,AttributeUtilities.ATT_TEMPERATURE,AttributeUtilities.ATT_TEMPERATURE,temperature,cl,tempUnit);
		theTS.addMisc(AttributeUtilities.ATT_PH_ID,AttributeUtilities.ATT_PH,AttributeUtilities.ATT_PH,ph,null,null);
		theTS.addMisc(AttributeUtilities.ATT_AW_ID,AttributeUtilities.ATT_AW,AttributeUtilities.ATT_AW,aw,null,null);
		
		fillFields();
		/*
		agentField.setText(agent);
		agensDetailField.setText(agentDetail);
		agensIDField.setText(agentID+"");
		matrixField.setText(matrix);
		matrixDetailField.setText(matrixDetail);
		matrixIDField.setText(matrixID+"");
		commentField.setText(comment);
		if (!Double.isNaN(temp)) temperatureField.setValue(temp); else temperatureField.setValue(null);
		if (!Double.isNaN(ph)) phField.setValue(ph); else phField.setValue(null);
		if (!Double.isNaN(aw)) waterActivityField.setValue(aw); else waterActivityField.setValue(null);
		*/
	}
	public void setTS(String xmlString) {
		try {
			PmmXmlDoc doc = new PmmXmlDoc(xmlString);
			for (int i = 0; i < doc.size(); i++) {
				PmmXmlElementConvertable el = doc.get(i);
				if (el instanceof PmmTimeSeries) {
					PmmTimeSeries ts = (PmmTimeSeries) el;
					setTS(ts);
					break;
				}
			}
		}
		catch (IOException  e) {
			e.printStackTrace();
		} 
		catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	private void button1ActionPerformed(ActionEvent e) {
		MyTable age = DBKernel.myDBi.getTable("Agenzien");
		Integer agensID = null;
		try {agensID = Integer.parseInt(agensIDField.getText());}
		catch (Exception e1) {}
		Object newVal = DBKernel.mainFrame.openNewWindow(
				age,
				agensID,
				(Object) "Agenzien",
				null,
				1,
				1,
				null,
				true, null, this);
		if (newVal != null && newVal instanceof Integer) {
			Object agensname = DBKernel.getValue("Agenzien", "ID", newVal.toString(), "Agensname");
			agentField.setText(agensname+"");
			agensIDField.setText(""+newVal);
			agensDetailField.setText("");
		}		
		else {
			agentField.setText("");
			agensIDField.setText("");
			agensDetailField.setText("");
		}
	}

	private void button2ActionPerformed(ActionEvent e) {
		MyTable mat = DBKernel.myDBi.getTable("Matrices");
		Integer matrixID = null;
		try {matrixID = Integer.parseInt(matrixIDField.getText());}
		catch (Exception e1) {}
		Object newVal = DBKernel.mainFrame.openNewWindow(
				mat,
				matrixID,
				(Object) "Matrices",
				null,
				1,
				1,
				null,
				true, null, this);
		if (newVal != null && newVal instanceof Integer) {
			Object matrixname = DBKernel.getValue("Matrices", "ID", newVal.toString(), "Matrixname");
			matrixField.setText(matrixname+"");
			matrixIDField.setText(""+newVal);
			matrixDetailField.setText("");
		}	
		else {
			matrixField.setText("");
			matrixIDField.setText("");
			matrixDetailField.setText("");
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		agentLabel = new JLabel();
		agentField = new StringTextField(true);
		button1 = new JButton();
		agensDetailField = new StringTextField(true);
		agensIDField = new JTextField();
		matrixLabel = new JLabel();
		matrixField = new StringTextField(true);
		button2 = new JButton();
		matrixDetailField = new StringTextField(true);
		matrixIDField = new JTextField();
		commentLabel = new JLabel();
		commentField = new StringTextField(true);
		tempLabel = new JLabel();
		temperatureField = new DoubleTextField(true);
		tempUnit = new JComboBox<>();
		phLabel = new JLabel();
		phField = new DoubleTextField(PmmConstants.MIN_PH, PmmConstants.MAX_PH, true);
		awLabel = new JLabel();
		waterActivityField = new DoubleTextField(PmmConstants.MIN_WATERACTIVITY, PmmConstants.MAX_WATERACTIVITY, true);

		//======== this ========
		setBorder(new CompoundBorder(
			new TitledBorder("Microbial Data Properties"),
			Borders.DLU2));
		setLayout(new FormLayout(
			"default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default",
			"5*(default, $lgap), default"));

		//---- agentLabel ----
		agentLabel.setText("Agent:");
		agentLabel.setText(AttributeUtilities.getName(TimeSeriesSchema.ATT_AGENT) + ":");
		add(agentLabel, CC.xy(1, 1));

		//---- agentField ----
		agentField.setEditable(false);
		add(agentField, CC.xy(3, 1));

		//---- button1 ----
		button1.setText("...");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});
		add(button1, CC.xy(5, 1));

		//---- agensDetailField ----
		agensDetailField.setColumns(4);
		add(agensDetailField, CC.xy(7, 1));

		//---- agensIDField ----
		agensIDField.setColumns(5);
		agensIDField.setVisible(false);
		add(agensIDField, CC.xy(9, 1));

		//---- matrixLabel ----
		matrixLabel.setText("Matrix:");
		matrixLabel.setText(AttributeUtilities.getName(TimeSeriesSchema.ATT_MATRIX) + ":");
		add(matrixLabel, CC.xy(1, 3));

		//---- matrixField ----
		matrixField.setEditable(false);
		add(matrixField, CC.xy(3, 3));

		//---- button2 ----
		button2.setText("...");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button2ActionPerformed(e);
			}
		});
		add(button2, CC.xy(5, 3));

		//---- matrixDetailField ----
		matrixDetailField.setColumns(5);
		add(matrixDetailField, CC.xy(7, 3));

		//---- matrixIDField ----
		matrixIDField.setColumns(5);
		matrixIDField.setVisible(false);
		add(matrixIDField, CC.xy(9, 3));

		//---- commentLabel ----
		commentLabel.setText(":");
		commentLabel.setText(AttributeUtilities.getName(MdInfoXml.ATT_COMMENT) + ":");
		add(commentLabel, CC.xy(1, 5));
		add(commentField, CC.xywh(3, 5, 5, 1));

		//---- tempLabel ----
		tempLabel.setText("Temperature:");
		tempLabel.setText(AttributeUtilities.getName(AttributeUtilities.ATT_TEMPERATURE) + ":");
		add(tempLabel, CC.xy(1, 7));
		add(temperatureField, CC.xywh(3, 7, 3, 1));

		//---- tempUnit ----
		tempUnit.setModel(new DefaultComboBoxModel<>(new String[] {
			"\u00b0C",
			"\u00b0F",
			"K"
		}));
		add(tempUnit, CC.xy(7, 7));

		//---- phLabel ----
		phLabel.setText("pH:");
		phLabel.setText(AttributeUtilities.getName(AttributeUtilities.ATT_PH) + ":");
		add(phLabel, CC.xy(1, 9));
		add(phField, CC.xywh(3, 9, 5, 1));

		//---- awLabel ----
		awLabel.setText("aw:");
		awLabel.setText(AttributeUtilities.getName(AttributeUtilities.ATT_AW) + ":");
		add(awLabel, CC.xy(1, 11));
		add(waterActivityField, CC.xywh(3, 11, 5, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel agentLabel;
	private StringTextField agentField;
	private JButton button1;
	private StringTextField agensDetailField;
	private JTextField agensIDField;
	private JLabel matrixLabel;
	private StringTextField matrixField;
	private JButton button2;
	private StringTextField matrixDetailField;
	private JTextField matrixIDField;
	private JLabel commentLabel;
	private StringTextField commentField;
	private JLabel tempLabel;
	private DoubleTextField temperatureField;
	private JComboBox<String> tempUnit;
	private JLabel phLabel;
	private DoubleTextField phField;
	private JLabel awLabel;
	private DoubleTextField waterActivityField;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
