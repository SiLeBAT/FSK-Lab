package de.bund.bfr.knime.fsklab.nodes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import org.sbml.jsbml.validator.SyntaxChecker;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.FskSimulation;
import de.bund.bfr.knime.fsklab.SimulationEntity;
import de.bund.bfr.knime.fsklab.nodes.ui.FLabel;
import de.bund.bfr.knime.fsklab.nodes.ui.FPanel;
import de.bund.bfr.knime.fsklab.nodes.ui.FSpinner;
import de.bund.bfr.knime.fsklab.nodes.ui.UIUtils;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.knime.fsklab.rakip.Parameter.DataTypes;

public class SimulatorNodeDialog extends DataAwareNodeDialogPane {
  public static String INTEGER_DATA_TYPES = "Integer";
  public static String DOUBLE_DATA_TYPES = "Double";
  private JList<SimulationEntity> list;
  private DefaultListModel<SimulationEntity> simulation_listModel;
  private JButton addButton;
  private JButton removeButton;
  private JTextField simulationName;
  private FPanel simulationSettingPanel;
  private SimulationEntity defaultSimulation;
  private static final String addString = "Add";
  private static final String removeString = "Remove";

  private SimulatorNodeSettings settings;
  Map<String, Parameter> parameterMap = new TreeMap<String, Parameter>();
  private static NodeLogger LOGGER = NodeLogger.getLogger("SimulatorNodeDialog");
  SimulationEntity currentSimulation;
  JPanel settingPanel;

  GenericModel currentGenericModel;

  public SimulatorNodeDialog() {
    simulation_listModel = new DefaultListModel<SimulationEntity>();

    list = new JList<>(simulation_listModel);

    addButton = UIUtils.createAddButton();
    removeButton = UIUtils.createRemoveButton();

    simulationName = new JTextField(10);

    simulationSettingPanel = new FPanel();
    simulationSettingPanel.setLayout(new BorderLayout());

    settings = new SimulatorNodeSettings();

    settingPanel = new JPanel(new BorderLayout());

    currentGenericModel = new GenericModel();

    createUI();

    addTab("Options", settingPanel);
  }

  private void createUI() {

    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setSelectedIndex(0);
    list.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting() == false) {

          @SuppressWarnings("unchecked")
          JList<SimulationEntity> list = (JList<SimulationEntity>) e.getSource();

          int selections[] = list.getSelectedIndices();
          Object selectionValues[] = list.getSelectedValuesList().toArray();

          for (int i = 0, n = selections.length; i < n; i++) {
            currentSimulation = (SimulationEntity) selectionValues[i];
          }

          // If selection enable the fire button

          removeButton.setEnabled((list.getSelectedIndex() != -1
              && !currentSimulation.getSimulationName().equals(NodeUtils.DEFAULT_SIMULATION)));

          updatePanel();
        }
      }
    });

    AddSimulationListener addSimulationListener = new AddSimulationListener(addButton);

    addButton.setActionCommand(addString);
    addButton.addActionListener(addSimulationListener);
    addButton.setEnabled(false);

    removeButton.setActionCommand(removeString);
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // This method can be called only if there's a valid selection so go ahead and remove
        // whatever's selected.
        int index = list.getSelectedIndex();

        // Default simulation (at position 0) should be fixed (cannot be removed)
        if (!list.getSelectedValue().getSimulationName().equals(NodeUtils.DEFAULT_SIMULATION)) {
          simulation_listModel.remove(index);

          int size = simulation_listModel.getSize();

          if (size == 0) { // Nobody's left, disable firing.
            removeButton.setEnabled(false);
          } else { // Select an index.
            if (index == simulation_listModel.getSize()) {
              // removed item in last position
              index--;
            }

            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
          }
        }
      }
    });

    simulationName.addActionListener(addSimulationListener);
    simulationName.getDocument().addDocumentListener(addSimulationListener);

    GridBagConstraints contraints = new GridBagConstraints();
    contraints.weightx = 1.0;
    contraints.weighty = 1.0;
    contraints.fill = GridBagConstraints.BOTH;
    contraints.anchor = GridBagConstraints.NORTHWEST;
    JPanel centerPanel = new JPanel(new GridBagLayout());

    centerPanel.add(simulationSettingPanel, contraints);
    JScrollPane scroll = new JScrollPane();
    scroll.setViewportView(simulationSettingPanel);

    JScrollPane listScrollPane = new JScrollPane(list);
    FPanel buttonPane = UIUtils.createHorizontalPanel(removeButton, simulationName, addButton);

    // Create a panel that uses BoxLayout.
    JPanel leftPane = new JPanel(new BorderLayout());
    leftPane.add(listScrollPane, BorderLayout.CENTER);
    leftPane.add(buttonPane, BorderLayout.SOUTH);
    settingPanel.setPreferredSize(new Dimension(600, 400));
    settingPanel.setBackground(UIUtils.WHITE);
    settingPanel.add(scroll, BorderLayout.CENTER);
    settingPanel.add(leftPane, BorderLayout.WEST);
  }

  public void updatePanel() {
    if (parameterMap.isEmpty()) {
      for (Parameter param : currentGenericModel.modelMath.parameter) {
        parameterMap.put(param.name, param);
      }
    }
    simulationSettingPanel.removeAll();
    simulationSettingPanel.revalidate();
    simulationSettingPanel.repaint();

    // Remove titled border if no simulation is selected
    if (simulation_listModel.size() == 0) {
      simulationSettingPanel.setBorder(null);
      return;
    }

    Border titledBorder = BorderFactory.createTitledBorder(currentSimulation.getSimulationName());
    Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, emptyBorder);
    simulationSettingPanel.setBorder(compoundBorder);

    List<Parameter> parameters = currentSimulation.getSimulationParameters();

    List<FLabel> labels = new ArrayList<>(parameters.size());
    List<JComponent> fields = new ArrayList<>(parameters.size());

    // Listener for text fields holding parameter values
    FocusListener focusListener = new FocusListener() {

      @Override
      public void focusLost(FocusEvent e) {
        JTextField field = (JTextField) e.getComponent();
        try {
          Double.parseDouble(field.getText());
        } catch (NumberFormatException exception) {
          field.setText("0.0");
          JOptionPane.showMessageDialog(simulationSettingPanel,
              "Please provide numeric value only!", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }

      @Override
      public void focusGained(FocusEvent e) {
        // does nothing
      }
    };

    for (Parameter param : parameters) {

      FLabel paramLabel = new FLabel(param.name);
      Parameter fullParam = parameterMap.get(param.name);
      SpinnerNumberModel spinnerModel = null;

      final DataTypes dataType = fullParam.dataType;

      if (dataType != null && (dataType == DataTypes.Integer || dataType == DataTypes.Double
          || dataType == DataTypes.Number)) {

        if (fullParam.dataType == DataTypes.Integer) {

          int min = fullParam.minValue.isEmpty() ? Integer.MIN_VALUE
              : Integer.parseInt(fullParam.minValue);
          int max = fullParam.maxValue.isEmpty() ? Integer.MAX_VALUE
              : Integer.parseInt(fullParam.maxValue);
          spinnerModel = new SpinnerNumberModel(Integer.parseInt(param.value), min, max, 1);
        }

        else if (fullParam.dataType == DataTypes.Double) {
          double min = fullParam.minValue.isEmpty() ? Integer.MIN_VALUE
              : Double.parseDouble(fullParam.minValue);
          double max = fullParam.maxValue.isEmpty() ? Integer.MAX_VALUE
              : Double.parseDouble(fullParam.maxValue);
          spinnerModel = new SpinnerNumberModel(Double.parseDouble(param.value), min, max, 0.01);
        }

        FSpinner paramField = new FSpinner(spinnerModel, false);
        paramField.putClientProperty("id", param.name);
        paramField.setEnabled(
            !currentSimulation.getSimulationName().equals(NodeUtils.DEFAULT_SIMULATION));
        paramField.addFocusListener(focusListener);
        paramField.addKeyListener(new SimulationParameterValueListener());

        paramLabel.setLabelFor(paramField);
        paramLabel.setToolTipText(fullParam.getDescription());
        labels.add(paramLabel);

        FPanel parameterFieldAndUnitArea = new FPanel();
        parameterFieldAndUnitArea.setLayout(new FlowLayout(FlowLayout.LEFT));
        parameterFieldAndUnitArea.add(paramField);
        parameterFieldAndUnitArea.add(new JLabel(fullParam.unit));
        fields.add(parameterFieldAndUnitArea);

      } else {
        JTextField paramField = new JTextField(10);
        paramField.setText(param.value);
        paramField.putClientProperty("id", param.name);
        paramField.setEditable(
            !currentSimulation.getSimulationName().equals(NodeUtils.DEFAULT_SIMULATION));
        paramField.addKeyListener(new SimulationParameterValueListener());

        paramLabel.setLabelFor(paramField);
        labels.add(paramLabel);

        FPanel parameterFieldAndUnitArea = new FPanel();
        parameterFieldAndUnitArea.setLayout(new FlowLayout(FlowLayout.LEFT));
        parameterFieldAndUnitArea.add(paramField);
        parameterFieldAndUnitArea.add(new JLabel(fullParam.unit));
        fields.add(UIUtils.createHorizontalPanel(paramField));
      }
    }

    FPanel formPanel = UIUtils.createFormPanel(labels, fields);
    FPanel northPanel = UIUtils.createNorthPanel(formPanel);

    simulationSettingPanel.add(northPanel, BorderLayout.CENTER);
  }

  public boolean isNumeric(String dataType) {

    return dataType.equals(INTEGER_DATA_TYPES) || dataType.equals(DOUBLE_DATA_TYPES);
  }

  class SimulationParameterValueListener implements KeyListener {

    @Override
    public void keyPressed(KeyEvent arg0) {}

    @Override
    public void keyReleased(KeyEvent arg0) {

      JTextField source = ((JTextField) arg0.getSource());

      for (Parameter param : currentSimulation.getSimulationParameters()) {
        if (param.name.equalsIgnoreCase((String) source.getClientProperty("id"))) {

          if (param.dataType == DataTypes.Integer) {

            try {
              Integer.parseInt(source.getText());
              source.setBackground(Color.WHITE);
            } catch (Exception e) {
              param.value = source.getText();
              source.setBackground(Color.RED);
            }
          }

          else if (param.dataType == DataTypes.Double) {

            try {
              Double.parseDouble(source.getText());
              source.setBackground(Color.WHITE);
            } catch (Exception e) {
              param.value = source.getText(); // Set text if not double
              source.setBackground(Color.RED);
            }
          }

          break;
        }
      }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {}
  }

  // This listener is shared by the text field and the hire button.
  class AddSimulationListener implements ActionListener, DocumentListener {

    private boolean alreadyEnabled = false;
    private JButton button;

    public AddSimulationListener(JButton button) {
      this.button = button;
    }

    // Required by ActionListener.
    public void actionPerformed(ActionEvent e) {

      String name = simulationName.getText();

      // User didn't type in a unique name...
      if (StringUtils.isBlank(name) || alreadyInList(name)
          || !SyntaxChecker.isValidId(name, 3, 1)) {
        Toolkit.getDefaultToolkit().beep();
        simulationName.requestFocusInWindow();
        simulationName.selectAll();
        return;
      }

      int index = list.getSelectedIndex(); // get selected index
      // If no selection (-1) insert at the beginning (0). Otherwise add after the selected item.
      index = index == -1 ? 0 : index + 1;

      SimulationEntity sE = new SimulationEntity();
      sE.setSimulationName(simulationName.getText());
      if (defaultSimulation == null) {
        defaultSimulation = simulation_listModel.get(0);
      }

      List<Parameter> simulationParameters = defaultSimulation.getSimulationParameters().stream()
          .map(it -> parameterMap.get(it.name)).collect(Collectors.toList());
      sE.setSimulationParameters(simulationParameters);

      simulation_listModel.addElement(sE);
      // If we just wanted to add to the end, we'd do this:
      // listModel.addElement(simulationName.getText());

      // Reset the text field.
      simulationName.requestFocusInWindow();
      simulationName.setText("");

      // Select the new item and make it visible.
      list.setSelectedIndex(index);
      list.ensureIndexIsVisible(index);
      updatePanel();
    }

    // This method tests for string equality. You could certainly
    // get more sophisticated about the algorithm. For example,
    // you might want to ignore white space and capitalization.
    protected boolean alreadyInList(String name) {
      for (int i = 0; i < simulation_listModel.size(); i++) {
        if (name.equals(simulation_listModel.getElementAt(i).getSimulationName()))
          return true;
      }
      return false;
    }

    // Required by DocumentListener.
    public void insertUpdate(DocumentEvent e) {
      enableButton();
    }

    // Required by DocumentListener.
    public void removeUpdate(DocumentEvent e) {
      handleEmptyTextField(e);
    }

    // Required by DocumentListener.
    public void changedUpdate(DocumentEvent e) {
      if (!handleEmptyTextField(e)) {
        enableButton();
      }
    }

    private void enableButton() {
      if (!alreadyEnabled) {
        button.setEnabled(true);
      }
    }

    private boolean handleEmptyTextField(DocumentEvent e) {

      if (e.getDocument().getLength() <= 0) {
        button.setEnabled(false);
        alreadyEnabled = false;
        return true;
      }

      return false;
    }
  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {

    final SimulatorNodeSettings simulationSettings = new SimulatorNodeSettings();
    try {
      simulationSettings.loadSettings(settings);
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException("InvalidSettingsException", exception);
    }
    final FskPortObject inObj = (FskPortObject) input[0];

    // If connected to same FSK model (input) then load simulations from settings
    if (Objects.equals(simulationSettings.genericModel, inObj.genericModel)) {
      // Updates settings
      this.settings = simulationSettings;
      this.settings.genericModel = simulationSettings.genericModel;
      currentGenericModel = simulationSettings.genericModel;
      simulation_listModel = new DefaultListModel<SimulationEntity>();
      list.removeAll();
      list.revalidate();
      list.repaint();
      list.setModel(simulation_listModel);

      List<SimulationEntity> savedList = simulationSettings.getListOfSimulation();
      if (savedList != null && savedList.size() > 0) {
        savedList.forEach(simulation_listModel::addElement);
      }

      list.setSelectedIndex(0);
      currentSimulation = savedList.get(0);
      updatePanel();
    }

    // If different (or new) connected FSK model then load simulations from input model
    else {

      this.settings.genericModel = inObj.genericModel;
      currentGenericModel = inObj.genericModel;

      simulation_listModel.clear();

      // Add simulations from input model
      for (FskSimulation fskSimulation : inObj.simulations) {

        List<Parameter> params = new ArrayList<>(fskSimulation.getParameters().size());
        for (Map.Entry<String, String> entry : fskSimulation.getParameters().entrySet()) {

          Parameter p = new Parameter();
          p.name = entry.getKey();
          p.value = entry.getValue();

          params.add(p);
        }

        SimulationEntity se = new SimulationEntity();
        se.setSimulationName(fskSimulation.getName());
        se.setSimulationParameters(params);

        simulation_listModel.addElement(se);
      }

      currentSimulation = simulation_listModel.firstElement();
      list.setSelectedIndex(0);
      list.ensureIndexIsVisible(0);
      list.revalidate();
      list.repaint();
      updatePanel();
    }
  }

  public static List<SimulationEntity> asList(final DefaultListModel<SimulationEntity> model) {
    return new AbstractList<SimulationEntity>() {

      @Override
      public SimulationEntity get(int index) {
        return (SimulationEntity) model.getElementAt(index);
      }

      @Override
      public int size() {
        return model.size();
      }
    };
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    // nodeSettings.filePath = field.getText();
    List<SimulationEntity> simulationList = asList(simulation_listModel);
    this.settings.setListOfSimulation(simulationList);
    this.settings.saveSettings(settings);
  }
}
