// TODO: add copyright notice
package de.bund.bfr.knime.fsklab.nodes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;
import de.bund.bfr.knime.fsklab.FSKJoinRelation;
import de.bund.bfr.knime.fsklab.FskPortObject;
import de.bund.bfr.knime.fsklab.nodes.ui.UTF8Control;
import de.bund.bfr.knime.fsklab.rakip.GenericModel;
import de.bund.bfr.knime.fsklab.rakip.ModelMath;
import de.bund.bfr.knime.fsklab.rakip.Parameter;
import de.bund.bfr.swing.UI;


public class JoinerNodeDialog extends DataAwareNodeDialogPane {

  private JComboBox<String> baseModels;
  private Map<String, FskPortObject> listOfModels = new HashMap<String, FskPortObject>();

  BaseModelsListener bml = new BaseModelsListener();
  private final ParametersPanel parametersPanel = new ParametersPanel(false);
  Map<String, TableCellEditor> editors = new HashMap<String, TableCellEditor>();
  Map<String, List<Parameter>> parameterMap = new HashMap<String, List<Parameter>>();
  private JoinerNodeSettings settings;
  FskPortObject selectedPortObject;
  List<FskPortObject> otherPortObjects = new ArrayList<FskPortObject>();
  JTable myTable;

  public JoinerNodeDialog() {

    baseModels = new JComboBox<String>();
    settings = new JoinerNodeSettings();


    JPanel centralPanel = new JPanel(new BorderLayout());
    JPanel northPanel = new JPanel();
    northPanel.add(createLabel("Joiner.BaseModel", true));
    northPanel.add(baseModels);

    centralPanel.add(northPanel, BorderLayout.NORTH);
    centralPanel.add(parametersPanel, BorderLayout.CENTER);
    addTab("Options", centralPanel);


  }

  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {
    baseModels.removeActionListener(bml);
    final JoinerNodeSettings joinerSettings = new JoinerNodeSettings();
    try {
      joinerSettings.loadSettings(settings);
    } catch (InvalidSettingsException exception) {
      throw new NotConfigurableException("InvalidSettingsException", exception);
    }

    final FskPortObject inObj = (FskPortObject) input[0];

    /*
     * If input model has not changed (the original scripts stored in settings match the input
     * model).
     */
    boolean found = false;
    for (PortObject current : input) {
      if (current != null
          && ((FskPortObject) current).genericModel.equals(joinerSettings.genericModel)) {
        found = true;
        break;
      }
    }
    if (found) {
      // Updates settings

      this.settings = joinerSettings;
    }

    prepearePanels(input);

  }

  private void prepearePanels(PortObject[] input) {
    baseModels.removeAllItems();
    baseModels.setSelectedIndex(-1);
    for (PortObject current : input) {
      if (current != null) {
        String modelName = ((FskPortObject) current).genericModel.generalInformation.name;

        baseModels.addItem(modelName);

        if (this.settings.genericModel != null
            && modelName.equals(this.settings.genericModel.generalInformation.name)) {
          selectedPortObject = (FskPortObject) current;
        }
        otherPortObjects.add((FskPortObject) current);
        listOfModels.put(modelName, (FskPortObject) current);
        List<Parameter> parameterForCombo =
            ((ModelMath) ((FskPortObject) current).genericModel.modelMath).parameter;
        Object[] items1 = parameterForCombo.stream()
            .filter(o -> o.classification.equals("Input-environmental factor")
                || o.classification.equals("Input-hazard"))
            .map(sc -> sc.name).collect(Collectors.toList()).toArray();
        JComboBox tempParameterComboBox = new JComboBox(items1);
        DefaultCellEditor dce1 = new DefaultCellEditor(tempParameterComboBox);
        editors.put(modelName, dce1);
        parameterMap.put(modelName, parameterForCombo);
      }
    }
    // set selected Base Model
    GenericModel selectedGenericModel = this.settings.genericModel;
    if (selectedGenericModel != null) {
      baseModels.setSelectedItem(selectedGenericModel.generalInformation.name);
    } else {
      baseModels.setSelectedItem(null);
    }
    // load the relation between the parameters
    List<FSKJoinRelation> listOfFSKJoinRelation = this.settings.listOfFSKJoinRelation;
    if (listOfFSKJoinRelation != null) {

      for (FSKJoinRelation relation : listOfFSKJoinRelation) {
        parametersPanel.tableModel.add(relation);
      }
    }
    baseModels.addActionListener(bml);
  }

  public static boolean listEqualsIgnoreOrder(List list1, List list2) {
    return new HashSet<>(list1).equals(new HashSet<>(list2));
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    if (selectedPortObject != null) {
      this.settings.genericModel = selectedPortObject.genericModel;
      if (otherPortObjects != null) {
        this.settings.joinedGenericModels =
            otherPortObjects.stream().map(sc -> sc.genericModel).collect(Collectors.toList());
      }
      List<FSKJoinRelation> listOfFSKJoinRelation = new ArrayList<FSKJoinRelation>();
      for (int row = 0; row < myTable.getRowCount(); row++) {
        String parameter = (String) myTable.getValueAt(row, 0);
        String modeltoJoin = (String) myTable.getValueAt(row, 1);
        String parameterToJoin = (String) myTable.getValueAt(row, 2);
        if (modeltoJoin != null && parameterToJoin != null) {
          FSKJoinRelation fskJoinRalation = new FSKJoinRelation();
          System.out.println(selectedPortObject.genericModel.generalInformation.name);

          fskJoinRalation.setValueReciever(selectedPortObject.genericModel.modelMath.parameter
              .stream().filter(o -> o.name.equals(parameter)).collect(Collectors.toList()).get(0));
          fskJoinRalation.setValueSource(parameterMap.get(modeltoJoin).stream()
              .filter(o -> o.name.equals(parameterToJoin)).collect(Collectors.toList()).get(0));
          fskJoinRalation.setSourceGenericModel(listOfModels.get(modeltoJoin).genericModel);
          listOfFSKJoinRelation.add(fskJoinRalation);
        } else {
          FSKJoinRelation fskJoinRalation = new FSKJoinRelation();
          System.out.println(selectedPortObject.genericModel.generalInformation.name);

          fskJoinRalation.setValueReciever(selectedPortObject.genericModel.modelMath.parameter
              .stream().filter(o -> o.name.equals(parameter)).collect(Collectors.toList()).get(0));
          fskJoinRalation.setValueSource(null);
          fskJoinRalation.setSourceGenericModel(null);
          listOfFSKJoinRelation.add(fskJoinRalation);
        }

      }
      this.settings.listOfFSKJoinRelation = listOfFSKJoinRelation;
      this.settings.saveSettings(settings);
    }

  }

  private class BaseModelsListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent event) {

      Object item = event.getSource();
      selectedPortObject = listOfModels.get(((JComboBox) item).getSelectedItem());
      if (selectedPortObject != null) {
        otherPortObjects.remove(selectedPortObject);
        parametersPanel.init(((ModelMath) selectedPortObject.genericModel.modelMath).parameter);

      }
    }
  }

  private static JLabel createLabel(final String textKey, final boolean isMandatory) {
    ResourceBundle bundle = ResourceBundle.getBundle("JoinerNodeBundle", new UTF8Control());
    return new JLabel(bundle.getString(textKey) + (isMandatory ? "*" : ""));
  }

  private class ParametersPanel extends JPanel {

    private static final long serialVersionUID = -5986975090954482038L;

    final TableModel tableModel = new TableModel();


    // Non modifiable table model with headers
    class TableModel extends DefaultTableModel {

      private static final long serialVersionUID = 1677268552154885327L;

      ArrayList<Parameter> parameters = new ArrayList<>();

      TableModel() {
        super(new Object[0][0], new String[] {"Parameter Name", "Model", "Parameters To Join"});
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        return true;
      }

      void add(final Parameter param) {

        // Skip if param is already in table
        for (final Parameter currentParam : parameters) {
          if (currentParam.equals(param)) {
            return;
          }
        }
        parameters.add(param);

        addRow(new String[] {param.name});
      }

      void add(final FSKJoinRelation fSKJoinRelation) {


        // Skip if param is already in table
        for (final Parameter currentParam : parameters) {
          if (currentParam.equals(fSKJoinRelation.getValueReciever())) {
            return;
          }
        }
        parameters.add(fSKJoinRelation.getValueReciever());
        String sourceModelName = fSKJoinRelation.getSourceGenericModel() != null
            ? fSKJoinRelation.getSourceGenericModel().generalInformation.name
            : "";
        String parametertoJoin =
            fSKJoinRelation.getValueSource() != null ? fSKJoinRelation.getValueSource().name : "";
        addRow(new String[] {fSKJoinRelation.getValueReciever().name, sourceModelName,
            parametertoJoin});
      }

      void modify(final int rowNumber, final Parameter param) {
        parameters.set(rowNumber, param);

        setValueAt(param.name, rowNumber, 0);

      }

      void remove(final int rowNumber) {
        parameters.remove(rowNumber);
        removeRow(rowNumber);
      }

      void removeAllParamenters() {
        if (getRowCount() > 0) {
          for (int i = getRowCount() - 1; i > -1; i--) {
            removeRow(i);
          }
        }
      }
    }


    ParametersPanel(final boolean isAdvanced) {

      super(new BorderLayout());



      setBorder(BorderFactory.createTitledBorder("Parameters"));

      myTable = new JTable(tableModel) {
        // Determine editor to be used by row
        @Override
        public TableCellEditor getCellEditor(int row, int column) {
          if (column == 2) {
            Object currentModel = this.getValueAt(row, column - 1);
            if (currentModel != null && !currentModel.equals("")) {
              return editors.get(currentModel);
            } else {
              return super.getCellEditor(row, column);
            }
          } else {
            return super.getCellEditor(row, column);
          }
        }
      };



      final JPanel panel = UI.createTablePanel(myTable);



      add(panel);
    }


    void init(final List<Parameter> parameters) {

      int rowCount = tableModel.getRowCount();
      TableColumn col = myTable.getColumnModel().getColumn(1);

      JComboBox<String> modelsComboBox = new JComboBox<String>(new DefaultComboBoxModel(
          otherPortObjects.stream().map(sc -> sc.genericModel.generalInformation.name)
              .collect(Collectors.toList()).toArray()));
      TableColumn parameterToJoinCol = myTable.getColumnModel().getColumn(2);
      col.setCellEditor(new DefaultCellEditor(modelsComboBox));

      // Remove rows one by one from the end of the table
      for (int i = rowCount - 1; i >= 0; i--) {
        tableModel.remove(i);
      }
      tableModel.parameters = new ArrayList<>();
      parameters.stream().filter(o -> o.classification.equals("Input-environmental factor")
          || o.classification.equals("Input-hazard")).forEach(tableModel::add);
    }
  }
}


