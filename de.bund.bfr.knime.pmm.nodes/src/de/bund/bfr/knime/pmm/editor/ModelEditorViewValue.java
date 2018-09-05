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
				matrix.setId(resultSet.getInt("ID"));
				matrix.setName(resultSet.getString(Bfrdb.ATT_MATRIXNAME));
				matrix.setDetail(resultSet.getString("Kommentar"));
				matrix.setDbuuid(DBKernel.getLocalDBUUID());

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
				agent.setId(resultSet.getInt("ID"));
				agent.setName(resultSet.getString("Agensname"));
				agent.setDetail(resultSet.getString("Kommentar"));
				agent.setDbuuid(DBKernel.getLocalDBUUID());

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
				literature.setId(resultSet.getInt("ID"));
				literature.setAuthor(resultSet.getString("Erstautor"));
				literature.setTitle(resultSet.getString("Titel"));
				literature.setAbstractText(resultSet.getString("Abstract"));
				literature.setJournal(resultSet.getString("Journal"));
				literature.setVolume(resultSet.getString("Volume"));
				literature.setIssue(resultSet.getString("Issue"));
				literature.setWebsite(resultSet.getString("Webseite"));
				literature.setComment(resultSet.getString("Kommentar"));
				literature.setYear(resultSet.getInt("Jahr"));
				literature.setPage(resultSet.getInt("Seite"));
				literature.setApprovalMode(resultSet.getInt("FreigabeModus"));
				literature.setType(resultSet.getInt("Literaturtyp"));
				literature.setDbuuid(DBKernel.getLocalDBUUID());

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
			u.setId(dbUnit.getId());
			u.setUnit(dbUnit.getUnit());
			u.setName(dbUnit.getKind_of_property_quantity());
			u.setKindOfPropertyQuantity(dbUnit.getKind_of_property_quantity());
			u.setNotationCaseSensitive(dbUnit.getNotation_case_sensitive());
			u.setConvertTo(dbUnit.getConvert_to());
			u.setConversionFunctionFactor(dbUnit.getConversion_function_factor());
			u.setInverseConversionFunctionFactor(dbUnit.getInverse_conversion_function_factor());
			u.setObjectType(dbUnit.getObject_type());
			u.setDisplayInGuiAs(dbUnit.getDisplay_in_GUI_as());
			u.setMathMLString(dbUnit.getMathML_string());
			u.setPriorityForDisplayInGui(dbUnit.getPriority_for_display_in_GUI());
			units[i] = u;
			i++;
		}

		m_db_units = new UnitList();
		m_db_units.setUnits(units);
	}
}
