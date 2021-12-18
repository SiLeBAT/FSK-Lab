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
package de.bund.bfr.knime.pmm.fskxexporter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.IntTextField;
import de.bund.bfr.knime.pmm.common.ui.SpacePanel;

public class PMMToFSKExporterNodeDialog extends DataAwareNodeDialogPane implements ActionListener {

  private BufferedDataTable[] input;
  private SettingsHelper set;

  private JComboBox<String> fittingBox;
  private JCheckBox limitsBox;
  private JCheckBox expertBox;



  private JPanel panel;
  private JPanel expertSettingsPanel;
  private JPanel fittingPanel;

  private IntTextField nParamSpaceField;
  private IntTextField nLevenbergField;
  private JCheckBox stopWhenSuccessBox;
  private JButton modelRangeButton;
  private JButton rangeButton;
  private JButton clearButton;
  private Map<String, Map<String, DoubleTextField>> minimumFields;
  private Map<String, Map<String, DoubleTextField>> maximumFields;
  
  PMModelReadder pmmodelReader = new PMModelReadder();

  /**
   * New pane for configuring the ModelEstimation node.
   */
  protected PMMToFSKExporterNodeDialog() {
    fittingBox = new JComboBox<>(new String[] {SettingsHelper.PRIMARY_FITTING,
        SettingsHelper.SECONDARY_FITTING, SettingsHelper.ONESTEP_FITTING});
    limitsBox = new JCheckBox("Enforce limits of Formula Definition");
    expertBox = new JCheckBox("Expert Settings");
    expertBox.addActionListener(this);
    nParamSpaceField = new IntTextField(0, 1000000);
    nParamSpaceField
        .setPreferredSize(new Dimension(100, nParamSpaceField.getPreferredSize().height));
    nLevenbergField = new IntTextField(1, 100);
    nLevenbergField.setPreferredSize(new Dimension(100, nLevenbergField.getPreferredSize().height));
    stopWhenSuccessBox = new JCheckBox("Stop When Regression Successful");

    JPanel fittingTypePanel = new JPanel();

    fittingTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    fittingTypePanel.add(fittingBox);

    JPanel limitsPanel = new JPanel();

    limitsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    limitsPanel.add(limitsBox);

    JPanel expertPanel = new JPanel();

    expertPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    expertPanel.add(expertBox);

    JPanel leftRegressionPanel = new JPanel();

    leftRegressionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    leftRegressionPanel.setLayout(new GridLayout(3, 1, 5, 5));
    leftRegressionPanel.add(new JLabel("Maximal Evaluations to Find Start Values"));
    leftRegressionPanel.add(new JLabel("Maximal Executions of the Levenberg Algorithm"));
    leftRegressionPanel.add(stopWhenSuccessBox);

    JPanel rightRegressionPanel = new JPanel();

    rightRegressionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    rightRegressionPanel.setLayout(new GridLayout(3, 1, 5, 5));
    rightRegressionPanel.add(nParamSpaceField);
    rightRegressionPanel.add(nLevenbergField);
    rightRegressionPanel.add(new JLabel());

    JPanel regressionPanel = new JPanel();

    regressionPanel.setBorder(new TitledBorder("Nonlinear Regression Parameters"));
    regressionPanel.setLayout(new BorderLayout());
    regressionPanel.add(leftRegressionPanel, BorderLayout.WEST);
    regressionPanel.add(rightRegressionPanel, BorderLayout.EAST);

    JPanel upperPanel = new JPanel();

    upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
    upperPanel.add(fittingTypePanel);
    upperPanel.add(limitsPanel);
    upperPanel.add(expertPanel);

    fittingPanel = new JPanel();
    fittingPanel.setBorder(
        BorderFactory.createTitledBorder("Specific Start Values for Fitting Procedure - Optional"));
    fittingPanel.setLayout(new BorderLayout());
    expertSettingsPanel = new JPanel();
    expertSettingsPanel.setLayout(new BorderLayout());
    expertSettingsPanel.add(regressionPanel, BorderLayout.NORTH);
    expertSettingsPanel.add(fittingPanel, BorderLayout.CENTER);
    panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(upperPanel, BorderLayout.NORTH);
    panel.add(expertSettingsPanel, BorderLayout.CENTER);

    addTab("Options", panel);
  }

  protected void loadSettingsFrom(NodeSettingsRO settings, BufferedDataTable[] input)
      throws NotConfigurableException {
    this.input = input;
    if(SchemaFactory.conformsM1Schema(input[0].getSpec())){
      pmmodelReader.readPrimaryTable(input[0]);
    }else if(SchemaFactory.conformsM2Schema(input[0].getSpec())){
      pmmodelReader.readSecondaryTable(input[0]);
    }else {
      System.out.println("was");
    }
    set = new SettingsHelper();
    set.loadSettings(settings);

    if (set.getFittingType().equals(SettingsHelper.NO_FITTING)) {
      if (SchemaFactory.createM12DataSchema().conforms(input[0])) {
        set.setFittingType(SettingsHelper.SECONDARY_FITTING);
      } else {
        set.setFittingType(SettingsHelper.PRIMARY_FITTING);
      }
    }

    fittingBox.setSelectedItem(set.getFittingType());
    fittingBox.addActionListener(this);
    limitsBox.setSelected(set.isEnforceLimits());
    expertBox.setSelected(set.isExpertSettings());
    nParamSpaceField.setValue(set.getnParameterSpace());
    nLevenbergField.setValue(set.getnLevenberg());
    stopWhenSuccessBox.setSelected(set.isStopWhenSuccessful());
    initGUI();
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    if (!nParamSpaceField.isValueValid() || !nLevenbergField.isValueValid() || minimumFields == null
        || maximumFields == null) {
      throw new InvalidSettingsException("");
    }

    Map<String, Map<String, Point2D.Double>> guessMap = new LinkedHashMap<>();

    for (String modelName : pmmodelReader.parameters.keySet()) {
      Map<String, Point2D.Double> guesses = new LinkedHashMap<>();

      for (String param : pmmodelReader.parameters.get(modelName)) {
        Double min = minimumFields.get(modelName).get(param).getValue();
        Double max = maximumFields.get(modelName).get(param).getValue();

        if (min == null) {
          min = Double.NaN;
        }

        if (max == null) {
          max = Double.NaN;
        }

        guesses.put(param, new Point2D.Double(min, max));
      }

      guessMap.put(modelName, guesses);
    }

    set.setFittingType((String) fittingBox.getSelectedItem());
    set.setnParameterSpace(nParamSpaceField.getValue());
    set.setnLevenberg(nLevenbergField.getValue());
    set.setEnforceLimits(limitsBox.isSelected());
    set.setExpertSettings(expertBox.isSelected());
    set.setStopWhenSuccessful(stopWhenSuccessBox.isSelected());
    set.setParameterGuesses(guessMap);
    set.saveSettings(settings);
  }



  private JComponent createPanel() {
    JPanel panel = new JPanel();
    JPanel rangePanel = new JPanel();

    minimumFields = new LinkedHashMap<>();
    maximumFields = new LinkedHashMap<>();

    rangePanel.setLayout(new BoxLayout(rangePanel, BoxLayout.Y_AXIS));

    for (String id : pmmodelReader.modelNames.keySet()) {
      JPanel modelPanel = new JPanel();
      JPanel leftPanel = new JPanel();
      JPanel rightPanel = new JPanel();
      List<String> params = pmmodelReader.parameters.get(id);
      Map<String, DoubleTextField> minFields = new LinkedHashMap<>();
      Map<String, DoubleTextField> maxFields = new LinkedHashMap<>();
      Map<String, Point2D.Double> guesses = set.getParameterGuesses().get(id);

      leftPanel.setLayout(new GridLayout(params.size(), 1));
      rightPanel.setLayout(new GridLayout(params.size(), 1));

      if (guesses == null) {
        guesses = new LinkedHashMap<>();
      }

      for (String param : params) {
        JPanel labelPanel = new JPanel();
        JPanel minMaxPanel = new JPanel();
        DoubleTextField minField = new DoubleTextField(true);
        DoubleTextField maxField = new DoubleTextField(true);
        Double min = pmmodelReader.minValues.get(id).get(param);
        Double max = pmmodelReader.maxValues.get(id).get(param);

        minField.setPreferredSize(new Dimension(100, minField.getPreferredSize().height));
        maxField.setPreferredSize(new Dimension(100, maxField.getPreferredSize().height));

        if (guesses.containsKey(param)) {
          Point2D.Double range = guesses.get(param);

          if (!Double.isNaN(range.x)) {
            minField.setValue(range.x);
          }

          if (!Double.isNaN(range.y)) {
            maxField.setValue(range.y);
          }
        } else {
          minField.setValue(min);
          maxField.setValue(max);
        }

        String rangeString;

        if (min != null && max != null) {
          rangeString = " (" + min + " to " + max + "):";
        } else if (min != null) {
          rangeString = " (" + min + " to ):";
        } else if (max != null) {
          rangeString = " ( to " + max + "):";
        } else {
          rangeString = ":";
        }

        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(new JLabel(param + rangeString));

        minMaxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        minMaxPanel.add(minField);
        minMaxPanel.add(new JLabel("to"));
        minMaxPanel.add(maxField);

        minFields.put(param, minField);
        maxFields.put(param, maxField);
        leftPanel.add(labelPanel);
        rightPanel.add(minMaxPanel);
      }

      minimumFields.put(id, minFields);
      maximumFields.put(id, maxFields);

      modelPanel.setBorder(BorderFactory.createTitledBorder(pmmodelReader.modelNames.get(id)));
      modelPanel.setLayout(new BorderLayout());
      modelPanel.add(leftPanel, BorderLayout.WEST);
      modelPanel.add(rightPanel, BorderLayout.EAST);

      rangePanel.add(modelPanel);
    }

    panel.setLayout(new BorderLayout());
    panel.add(rangePanel, BorderLayout.NORTH);

    return new JScrollPane(panel);
  }

  private void initGUI() {
    pmmodelReader.modelNames = null;
    pmmodelReader.parameters = null;
    pmmodelReader.minValues = null;
    pmmodelReader.maxValues = null;
    minimumFields = null;
    maximumFields = null;
    fittingPanel.removeAll();

    JComponent fittingValuesPanel = null;

    if (fittingBox.getSelectedItem().equals(SettingsHelper.PRIMARY_FITTING)
        && SchemaFactory.createM1DataSchema().conforms(input[0])) {
      pmmodelReader.readPrimaryTable(input[0]);
      fittingValuesPanel = createPanel();
    } else if (fittingBox.getSelectedItem().equals(SettingsHelper.SECONDARY_FITTING)
        && SchemaFactory.createM12DataSchema().conforms(input[0])) {
      pmmodelReader.readSecondaryTable(input[0]);
      fittingValuesPanel = createPanel();
    } else if (fittingBox.getSelectedItem().equals(SettingsHelper.ONESTEP_FITTING)
        && SchemaFactory.createM12DataSchema().conforms(input[0])) {
      pmmodelReader.readSecondaryTable(input[0]);
      fittingValuesPanel = createPanel();
    }

    if (fittingValuesPanel != null) {
      modelRangeButton = new JButton("Use Range from Formula Definition");
      modelRangeButton.addActionListener(this);
      rangeButton = new JButton("Fill Empty Fields");
      rangeButton.addActionListener(this);
      clearButton = new JButton("Clear");
      clearButton.addActionListener(this);

      JPanel buttonPanel = new JPanel();

      buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      buttonPanel.add(modelRangeButton);
      buttonPanel.add(rangeButton);
      buttonPanel.add(clearButton);

      fittingPanel.add(new SpacePanel(buttonPanel), BorderLayout.NORTH);
      fittingPanel.add(fittingValuesPanel, BorderLayout.CENTER);
      fittingPanel.revalidate();
    } else {
      if (fittingBox.isValid()) {
        JOptionPane.showMessageDialog(fittingBox,
            "Data is not valid for " + fittingBox.getSelectedItem());
      }

      fittingPanel.add(new JLabel(), BorderLayout.CENTER);
    }

    Dimension preferredSize = panel.getPreferredSize();

    if (expertBox.isSelected()) {
      expertSettingsPanel.setVisible(true);
    } else {
      expertSettingsPanel.setVisible(false);
    }

    panel.setPreferredSize(preferredSize);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == fittingBox) {
      initGUI();
    } else if (e.getSource() == expertBox) {
      if (expertBox.isSelected()) {
        expertSettingsPanel.setVisible(true);
      } else {
        expertSettingsPanel.setVisible(false);
      }
    } else if (e.getSource() == modelRangeButton) {
      for (String id : pmmodelReader.parameters.keySet()) {
        for (String param : pmmodelReader.parameters.get(id)) {
          minimumFields.get(id).get(param).setValue(pmmodelReader.minValues.get(id).get(param));
          maximumFields.get(id).get(param).setValue(pmmodelReader.maxValues.get(id).get(param));
        }
      }
    } else if (e.getSource() == rangeButton) {
      for (String id : pmmodelReader.parameters.keySet()) {
        for (String param : pmmodelReader.parameters.get(id)) {
          Double min = minimumFields.get(id).get(param).getValue();
          Double max = maximumFields.get(id).get(param).getValue();

          if (min == null && max == null) {
            minimumFields.get(id).get(param).setValue(-1000000.0);
            maximumFields.get(id).get(param).setValue(1000000.0);
          }
        }
      }
    } else if (e.getSource() == clearButton) {
      for (String id : pmmodelReader.parameters.keySet()) {
        for (String param : pmmodelReader.parameters.get(id)) {
          minimumFields.get(id).get(param).setValue(null);
          maximumFields.get(id).get(param).setValue(null);
        }
      }
    }
  }
  
}
