package de.bund.bfr.knime.internal.rhome;

import java.nio.file.Path;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataTableSpecCreator;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeView;
import org.knime.core.node.StatelessModel;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortType;

import de.bund.bfr.knime.fsklab.preferences.RPreferenceInitializer;
import de.bund.bfr.knime.fsklab.preferences.RPreferenceProvider;


public class RHomeNodeFactory extends NodeFactory<NodeModel> {

	@Override
	public NodeModel createNodeModel() {

		PortType[] inputTypes = {};
		PortType[] outputTypes = { BufferedDataTable.TYPE };

		return new StatelessModel(inputTypes, outputTypes) {
			protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {

				DataColumnSpec colSpec = new DataColumnSpecCreator("RHome", StringCell.TYPE).createSpec();
				DataTableSpecCreator specCreator = new DataTableSpecCreator().addColumns(colSpec);
				DataTableSpec tableSpec = specCreator.createSpec();

				return new DataTableSpec[] { tableSpec };
			};

			protected BufferedDataTable[] execute(BufferedDataTable[] inData, org.knime.core.node.ExecutionContext exec)
					throws Exception {

				DataTableSpec tableSpec = configure(null)[0];
				BufferedDataContainer container = exec.createDataContainer(tableSpec);

				// Cells
				RPreferenceProvider provider = RPreferenceInitializer.getR3Provider();

				// R home, bin and Rserve paths
				String rHome = "";
				String rBin = "";
				String rServeBin = "";

				if (provider != null) {
					rHome = provider.getRHome();

					Path rBinPath = provider.getRBinPath("R");
					rBin = rBinPath != null ? rBinPath.toString() : "";

					Path rServePath = provider.getRServeBinPath();
					rServeBin = rServePath != null ? rServePath.toString() : "";
				}

				container.addRowToTable(new DefaultRow("R home", new StringCell(rHome)));
				container.addRowToTable(new DefaultRow("R bin", new StringCell(rBin)));
				container.addRowToTable(new DefaultRow("Rserve", new StringCell(rServeBin)));

				container.close();

				return new BufferedDataTable[] { container.getTable() };
			};
		};
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	protected boolean hasDialog() {
		return false;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new DefaultNodeSettingsPane();
	}

	@Override
	public NodeView<NodeModel> createNodeView(int viewIndex, NodeModel nodeModel) {
		return null;
	}
}
