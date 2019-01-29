package de.bund.bfr.knime.pmm.editor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.js.common.Agent;
import de.bund.bfr.knime.pmm.js.common.AgentList;
import de.bund.bfr.knime.pmm.js.common.Literature;
import de.bund.bfr.knime.pmm.js.common.LiteratureList;
import de.bund.bfr.knime.pmm.js.common.Matrix;
import de.bund.bfr.knime.pmm.js.common.MatrixList;
import de.bund.bfr.knime.pmm.js.common.ModelList;
import de.bund.bfr.knime.pmm.js.common.Unit;
import de.bund.bfr.knime.pmm.js.common.UnitList;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ModelEditorViewValue extends JSONViewContent {

	// Configuration keys
	private static final String DB_MATRICES = "dbMatrices";
	private static final String DB_AGENTS = "dbAgents";
	private static final String DB_LITERATURE = "dbLiterature";
	private static final String DB_UNITS = "dbUnits";
	private static final String MODELS = "models";

	private MatrixList m_db_matrices;
	private AgentList m_db_agents;
	private LiteratureList m_db_literatureItems;
	private UnitList m_db_units;
	private ModelList m_models;

	public ModelEditorViewValue() {
		// Query database
		loadMatricesFromDB();
		loadAgentsFromDB();
		loadLiteratureItemsFromDB();
		loadUnitsFromDB();
		m_models = new ModelList();
	}

	public MatrixList getDbMatrices() {
		return m_db_matrices;
	}

	public AgentList getDbAgents() {
		return m_db_agents;
	}

	public LiteratureList getDbLiteratureItems() {
		return m_db_literatureItems;
	}

	public UnitList getDbUnits() {
		return m_db_units;
	}

	public ModelList getModels() {
		return m_models;
	}

	public void setDbMatrices(MatrixList dbMatrices) {
		m_db_matrices = dbMatrices;
	}

	public void setDbAgents(AgentList dbAgents) {
		m_db_agents = dbAgents;
	}

	public void setDbLiteratureItems(LiteratureList dbLiteratureItems) {
		m_db_literatureItems = dbLiteratureItems;
	}

	public void setDbUnits(UnitList dbUnits) {
		m_db_units = dbUnits;
	}

	public void setModels(ModelList models) {
		m_models = models;
	}

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		m_db_matrices.saveToNodeSettings(settings.addNodeSettings(DB_MATRICES));
		m_db_agents.saveToNodeSettings(settings.addNodeSettings(DB_AGENTS));
		m_db_literatureItems.saveToNodeSettings(settings.addNodeSettings(DB_LITERATURE));
		m_db_units.saveToNodeSettings(settings.addNodeSettings(DB_UNITS));
		m_models.saveToNodeSettings(settings.addNodeSettings(MODELS));
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		m_db_matrices.loadFromNodeSettings(settings.getNodeSettings(DB_MATRICES));
		m_db_agents.loadFromNodeSettings(settings.getNodeSettings(DB_AGENTS));
		m_db_literatureItems.loadFromNodeSettings(settings.getNodeSettings(DB_LITERATURE));
		m_db_units.loadFromNodeSettings(settings.getNodeSettings(DB_UNITS));
		m_models.loadFromNodeSettings(settings.getNodeSettings(MODELS));
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	private void loadMatricesFromDB() {
		List<Matrix> matrixList = new LinkedList<>();
		String query = "SELECT * FROM \"" + Bfrdb.REL_MATRIX + "\"";
		ResultSet resultSet = DBKernel.getResultSet(query, false);

		try {
			while (resultSet.next()) {
				Matrix matrix = new Matrix();
				matrix.id = resultSet.getInt("ID");
				matrix.name = resultSet.getString(Bfrdb.ATT_MATRIXNAME);
				matrix.detail = resultSet.getString("Kommentar");
				matrix.dbuuid = DBKernel.getLocalDBUUID();

				matrixList.add(matrix);
			}
		} catch (SQLException e) {
		}

		Matrix[] matrixArray = matrixList.toArray(new Matrix[matrixList.size()]);
		m_db_matrices = new MatrixList();
		m_db_matrices.setMatrices(matrixArray);
	}

	private void loadAgentsFromDB() {
		List<Agent> agentList = new LinkedList<>();
		String query = "SELECT * FROM \"" + Bfrdb.REL_AGENT + "\"";
		ResultSet resultSet = DBKernel.getResultSet(query, false);

		try {
			while (resultSet.next()) {
				Agent agent = new Agent();
				agent.id = resultSet.getInt("ID");
				agent.name = resultSet.getString("Agensname");
				agent.detail = resultSet.getString("Kommentar");
				agent.dbuuid = DBKernel.getLocalDBUUID();

				agentList.add(agent);
			}
		} catch (SQLException e) {
		}

		Agent[] agentArray = agentList.toArray(new Agent[agentList.size()]);
		m_db_agents = new AgentList();
		m_db_agents.setAgents(agentArray);
	}

	private void loadLiteratureItemsFromDB() {
		List<Literature> literatureList = new LinkedList<>();
		String query = "SELECT * FROM \"Literatur\"";
		ResultSet resultSet = DBKernel.getResultSet(query, true);

		try {
			while (resultSet.next()) {
				Literature literature = new Literature();
				literature.id = resultSet.getInt("ID");
				literature.author = resultSet.getString("Erstautor");
				literature.title = resultSet.getString("Titel");
				literature.abstractText = resultSet.getString("Abstract");
				literature.journal = resultSet.getString("Journal");
				literature.volume = resultSet.getString("Volume");
				literature.issue = resultSet.getString("Issue");
				literature.website = resultSet.getString("Webseite");
				literature.comment = resultSet.getString("Kommentar");
				literature.year = resultSet.getInt("Jahr");
				literature.page = resultSet.getInt("Seite");
				literature.approvalMode = resultSet.getInt("FreigabeModus");
				literature.type = resultSet.getInt("Literaturtyp");
				literature.dbuuid = DBKernel.getLocalDBUUID();

				literatureList.add(literature);
			}
		} catch (SQLException e) {
		}

		Literature[] literatureArray = literatureList.toArray(new Literature[literatureList.size()]);
		m_db_literatureItems = new LiteratureList();
		m_db_literatureItems.setLiterature(literatureArray);
	}

	private void loadUnitsFromDB() {
		Collection<UnitsFromDB> ufdbs = DBUnits.getDBUnits().values();
		int numUnits = ufdbs.size();

		Unit[] units = new Unit[numUnits];

		int i = 0;
		for (UnitsFromDB dbUnit : ufdbs) {
			Unit u = new Unit();
			u.id = dbUnit.id;
			u.unit = dbUnit.unit;
			u.name = dbUnit.kindOfPropertyQuantity;
			u.kind_of_property_quantity = dbUnit.kindOfPropertyQuantity;
			u.notation_case_sensitive = dbUnit.notationCaseSensitive;
			u.convert_to = dbUnit.convertTo;
			u.conversion_function_factor = dbUnit.conversionFunctionFactor;
			u.inverse_conversion_function_factor = dbUnit.inverseConversionFunctionFactor;
			u.object_type = dbUnit.objectType;
			u.display_in_GUI_as = dbUnit.displayInGuiAs;
			u.mathML_string = dbUnit.mathMlString;
			u.priority_for_display_in_GUI = dbUnit.priorityForDisplayInGui;
			units[i] = u;
			i++;
		}

		m_db_units = new UnitList();
		m_db_units.setUnits(units);
	}
}
