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
package de.bund.bfr.knime.pmm.bfrdbiface.lib;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DBUtilities;
import de.bund.bfr.knime.pmm.common.DbIo;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

public class Bfrdb {

	public static final String ATT_AGENTDETAIL = "AgensDetail";
	public static final String ATT_AGENTID = "Agens";
	public static final String ATT_AGENTNAME = "Agensname";
	public static final String ATT_MATRIXDETAIL = "MatrixDetail";
	public static final String ATT_MATRIXID = "Matrix";
	public static final String ATT_MATRIXNAME = "Matrixname";
	public static final String ATT_AW = "aw";
	public static final String ATT_COMBASEID = "ID_CB";
	public static final String ATT_CONDITIONID = "Versuchsbedingung";
	public static final String ATT_CONDITIONS = "b_f_details_CB";
	public static final String ATT_CONDITION_MISCPARAM = "Versuchsbedingungen_Sonstiges";
	public static final String ATT_DEP = "Dependent";
	public static final String ATT_DESCRIPTION = "Beschreibung";
	public static final String ATT_ESTMODELID = "GeschaetztesModell";
	public static final String ATT_FIRSTAUTHOR = "Erstautor";
	public static final String ATT_INDEP = "Independent";
	public static final String ATT_LEVEL = "Level";
	public static final String ATT_LITERATUREID = "Literatur";
	public static final String ATT_LOG10N = "Konzentration";
	public static final String ATT_MAX = "max";
	public static final String ATT_MAXINDEP = "maxIndep";
	public static final String ATT_MAXVALUE = "maxValue";
	public static final String ATT_MIN = "min";
	public static final String ATT_MININDEP = "minIndep";
	public static final String ATT_MINVALUE = "minValue";
	public static final String ATT_MISC = "Sonstiges";
	public static final String ATT_MISCID = "SonstigesID";
	public static final String ATT_MODELID = "Modell";
	public static final String ATT_NAME = "Name";
	public static final String ATT_NAMESHORT = "Kurzbezeichnung";
	public static final String ATT_PARAMID = "Parameter";
	public static final String ATT_PARAMNAME = "Parametername";
	public static final String ATT_PARAMTYPE = "Parametertyp";
	public static final String ATT_PH = "pH";
	public static final String ATT_PRESSURE = "Druck";
	public static final String ATT_RMS = "RMS";
	public static final String ATT_RSQUARED = "Rsquared";
	public static final String ATT_RSS = "RSS";
	public static final String ATT_STANDARDERROR = "StandardError";
	public static final String ATT_TEMPERATURE = "Temperatur";
	public static final String ATT_TIME = "Zeit";
	public static final String ATT_UNIT = "Einheit";
	public static final String ATT_VALUE = "Wert";
	public static final String ATT_VALUETYPE = "Wert_typ";
	public static final String ATT_VARMAPFROM = "VarPar";
	public static final String ATT_VARMAPTO = "VarParMap";
	public static final String ATT_YEAR = "Jahr";
	public static final String REL_AGENT = "Agenzien";
	public static final String REL_COMBASE = "ImportedCombaseData";
	public static final String REL_CONDITION = "Versuchsbedingungen";
	public static final String REL_DATA = "Messwerte";
	public static final String REL_DOUBLE = "DoubleKennzahlen";
	public static final String REL_ESTMODEL = "GeschaetzteModelle";
	public static final String REL_ESTPARAM = "GeschaetzteParameter";
	public static final String REL_MATRIX = "Matrices";
	public static final String REL_MISCPARAM = "SonstigeParameter";
	public static final String REL_MODEL = "Modellkatalog";
	public static final String REL_MODEL_LITERATURE = "Modell_Referenz";
	public static final String REL_UNIT = "Einheiten";
	public static final String REL_VARMAP = "VarParMaps";
	public static final String VIEW_MISCPARAM = "SonstigesEinfach";

	private static final String queryTimeSeries9SinDataView = "SELECT\n" + "\n" + "    \"VersuchsbedingungenEinfach\".\"ID\" AS \"" + ATT_CONDITIONID + "\",\n" + "    \""
			+ REL_COMBASE + "\".\"CombaseID\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_TEMPERATURE + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_PH
			+ "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_AW + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_PRESSURE + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_AGENTID + "\",\n" + "    \"" + REL_AGENT
			+ "\".\"" + ATT_AGENTNAME + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_AGENTDETAIL + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_MATRIXID
			+ "\",\n" + "    \"" + REL_MATRIX + "\".\"" + ATT_MATRIXNAME + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_MATRIXDETAIL + "\",\n"
			+ "    \"VersuchsbedingungenEinfach\".\"Kommentar\",\n" + "    \"VersuchsbedingungenEinfach\".\"Guetescore\",\n" + "    \"VersuchsbedingungenEinfach\".\"Geprueft\",\n"
			+ "    \"VersuchsbedingungenEinfach\".\"Referenz\" AS \"" + ATT_LITERATUREID + "\",\n" + "    \"SonstigesEinfach\".\"SonstigesID\",\n"
			+ "    \"SonstigesEinfach\".\"Parameter\",\n" + "    \"SonstigesEinfach\".\"Beschreibung\",\n" + "    \"SonstigesEinfach\".\"Einheit\",\n"
			+ "    \"SonstigesEinfach\".\"Wert\" AS \"SonstigesWert\"\n" + "\n" + "FROM \"VersuchsbedingungenEinfach\"\n" + "\n" + "LEFT JOIN \"" + REL_COMBASE + "\"\n"
			+ "ON \"VersuchsbedingungenEinfach\".\"ID\"=\"" + REL_COMBASE + "\".\"" + ATT_CONDITIONID + "\"\n" + "\n" + "LEFT JOIN \"" + REL_AGENT + "\"\n"
			+ "ON \"VersuchsbedingungenEinfach\".\"" + ATT_AGENTID + "\"=\"" + REL_AGENT + "\".\"ID\"\n" + "\n" + "LEFT JOIN \"" + REL_MATRIX + "\"\n"
			+ "ON \"VersuchsbedingungenEinfach\".\"" + ATT_MATRIXID + "\"=\"" + REL_MATRIX + "\".\"ID\"\n" + "\n" + "LEFT JOIN \"" + ATT_LITERATUREID + "\"\n"
			+ "ON \"VersuchsbedingungenEinfach\".\"Referenz\"=\"" + ATT_LITERATUREID + "\".\"ID\"\n" + "\n" + "LEFT JOIN \"" + VIEW_MISCPARAM + "\"\n"
			+ "ON \"VersuchsbedingungenEinfach\".\"ID\"=\"" + VIEW_MISCPARAM + "\".\"" + ATT_CONDITIONID + "\"\n";

	private static final String queryTimeSeries9 = "SELECT\n" + "\n" + "    \"VersuchsbedingungenEinfach\".\"ID\" AS \"" + ATT_CONDITIONID + "\",\n" + "    \"" + REL_COMBASE
			+ "\".\"CombaseID\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_TEMPERATURE + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_PH + "\",\n"
			+ "    \"VersuchsbedingungenEinfach\".\"" + ATT_AW + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_PRESSURE + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_AGENTID + "\",\n" + "    \"" + REL_AGENT + "\".\""
			+ ATT_AGENTNAME + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_AGENTDETAIL + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_MATRIXID + "\",\n"
			+ "    \"" + REL_MATRIX + "\".\"" + ATT_MATRIXNAME + "\",\n" + "    \"VersuchsbedingungenEinfach\".\"" + ATT_MATRIXDETAIL + "\",\n" + "    \"DataView\".\"Zeit\",\n"
			+ "    \"DataView\".\"ZeitEinheit\",\n" + "    \"DataView\".\"Konzentration\",\n" + "    \"DataView\".\"KonzentrationsEinheit\",\n"
			+ "    \"DataView\".\"KonzentrationsObjectType\",\n" + "    \"DataView\".\"Standardabweichung\",\n" + "    \"DataView\".\"Wiederholungen\",\n"
			+ "    \"VersuchsbedingungenEinfach\".\"Kommentar\",\n" + "    \"VersuchsbedingungenEinfach\".\"Guetescore\",\n" + "    \"VersuchsbedingungenEinfach\".\"Geprueft\",\n"
			+ "    \"VersuchsbedingungenEinfach\".\"Referenz\" AS \"" + ATT_LITERATUREID + "\",\n" + "    \"" + VIEW_MISCPARAM + "\".\"" + ATT_MISCID + "\",\n" + "    \""
			+ VIEW_MISCPARAM + "\".\"" + ATT_PARAMID + "\",\n" + "    \"" + VIEW_MISCPARAM + "\".\"" + ATT_DESCRIPTION + "\",\n" + "    \"" + VIEW_MISCPARAM + "\".\"" + ATT_UNIT
			+ "\",\n" + "    \"" + VIEW_MISCPARAM + "\".\"" + ATT_VALUE + "\" AS \"" + ATT_MISC + "" + ATT_VALUE + "\"\n" + "\n" + "FROM \"VersuchsbedingungenEinfach\"\n" + "\n"
			+ "LEFT JOIN \"" + REL_COMBASE + "\"\n" + "ON \"VersuchsbedingungenEinfach\".\"ID\"=\"" + REL_COMBASE + "\".\"" + ATT_CONDITIONID + "\"\n" + "\n" + "LEFT JOIN \""
			+ REL_AGENT + "\"\n" + "ON \"VersuchsbedingungenEinfach\".\"" + ATT_AGENTID + "\"=\"" + REL_AGENT + "\".\"ID\"\n" + "\n" + "LEFT JOIN \"" + REL_MATRIX + "\"\n"
			+ "ON \"VersuchsbedingungenEinfach\".\"" + ATT_MATRIXID + "\"=\"" + REL_MATRIX + "\".\"ID\"\n" + "\n" + "LEFT JOIN \"" + ATT_LITERATUREID + "\"\n"
			+ "ON \"VersuchsbedingungenEinfach\".\"Referenz\"=\"" + ATT_LITERATUREID + "\".\"ID\"\n" + "\n" + "LEFT JOIN(\n" + "\n" + "    SELECT\n" + "\n"
			+ "        \"Versuchsbedingung\",\n" + "        ARRAY_AGG( \"Zeit\" )AS \"Zeit\",\n" + "        ARRAY_AGG( \"ZeitEinheit\" )AS \"ZeitEinheit\",\n"
			+ "        ARRAY_AGG( \"Konzentration\" )AS \"Konzentration\",\n" + "        ARRAY_AGG( \"KonzentrationsEinheit\" )AS \"KonzentrationsEinheit\",\n"
			+ "        ARRAY_AGG( \"KonzentrationsObjectType\" )AS \"KonzentrationsObjectType\",\n" + "        ARRAY_AGG( \"Standardabweichung\" )AS \"Standardabweichung\",\n"
			+ "        ARRAY_AGG( \"Wiederholungen\" )AS \"Wiederholungen\"\n" + "\n" + "    FROM(\n" + "\n" + "        SELECT\n" + "\n" + "            \"Messung\".\"ID\",\n"
			+ "            \"Messung\".\"Versuchsbedingung\",\n" + "            \"Messung\".\"Zeit\",\n" + "            \"Messung\".\"ZeitEinheit\",\n"
			+ "            \"Referenz\".\"Konzentration\"+\"Messung\".\"Konzentration\" AS \"Konzentration\",\n" + "            \"Messung\".\"KonzentrationsEinheit\",\n"
			+ "            \"Messung\".\"KonzentrationsObjectType\",\n" + "            \"Messung\".\"Standardabweichung\",\n" + "            \"Messung\".\"Wiederholungen\"\n"
			+ "\n" + "        FROM(\n" + "\n" + "            SELECT *\n" + "            FROM \"MesswerteEinfach\"\n"
			+ "            WHERE( \"Delta\" IS NULL OR NOT \"Delta\" )AND \"Zeit\"=0 AND \"Konzentration\" IS NOT NULL\n" + "\n" + "        )\"Referenz\"\n" + "\n"
			+ "        JOIN(\n" + "\n" + "            SELECT *\n" + "            FROM \"MesswerteEinfach\"\n"
			+ "            WHERE \"Delta\" AND NOT( \"Zeit\" IS NULL OR \"Konzentration\" IS NULL )\n" + "\n" + "        )\"Messung\"\n"
			+ "        ON \"Referenz\".\"Versuchsbedingung\"=\"Messung\".\"Versuchsbedingung\"\n" + "\n" + "        UNION\n" + "\n" + "        SELECT \n" + "\n"
			+ "            \"ID\",\n" + "            \"Versuchsbedingung\",\n" + "            \"Zeit\",\n" + "            \"ZeitEinheit\",\n" + "            \"Konzentration\",\n"
			+ "            \"KonzentrationsEinheit\",\n" + "            \"KonzentrationsObjectType\",\n" + "            \"Standardabweichung\",\n"
			+ "            \"Wiederholungen\"\n" + "\n" + "        FROM \"MesswerteEinfach\"\n" + "\n" + "        WHERE \"Delta\" IS NULL OR NOT \"Delta\"\n" + "\n" + "    )\n"
			+ "\n" + "    GROUP BY \"Versuchsbedingung\"\n" + "\n" + ")\"DataView\"\n" + "ON \"VersuchsbedingungenEinfach\".\"ID\"=\"DataView\".\"Versuchsbedingung\"\n" + "\n"
			+ "LEFT JOIN \"" + VIEW_MISCPARAM + "\"\n" + "ON \"VersuchsbedingungenEinfach\".\"ID\"=\"" + VIEW_MISCPARAM + "\".\"Versuchsbedingung\"\n" + "\n"
			+ "WHERE \"Zeit\" IS NOT NULL\n" + "\n" + "ORDER BY \"" + ATT_CONDITIONID + "\"\n";

	private static final String queryXmlDoc = "SELECT\n" + "\n" + "    \"" + ATT_CONDITION_MISCPARAM + "\".\"" + REL_CONDITION + "\" AS \"" + ATT_CONDITIONID + "\",\n" + "    \""
			+ REL_MISCPARAM + "\".\"ID\" AS \"" + ATT_MISCID + "\",\n" + "    \"" + REL_MISCPARAM + "\".\"" + ATT_PARAMID + "\",\n" + "    \"" + REL_MISCPARAM + "\".\""
			+ ATT_DESCRIPTION + "\",\n" + "    \"" + REL_UNIT + "\".\"" + ATT_UNIT + "\",\n" + "    \"" + REL_DOUBLE + "Einfach\".\"" + ATT_VALUE + "\"\n" + "\n" + "FROM \""
			+ ATT_CONDITION_MISCPARAM + "\"\n" + "\n" + "LEFT JOIN \"" + REL_UNIT + "\"\n" + "ON \"" + ATT_CONDITION_MISCPARAM + "\".\"" + ATT_UNIT + "\"=\"" + REL_UNIT
			+ "\".\"ID\"\n" + "\n" + "JOIN \"" + REL_MISCPARAM + "\"\n" + "ON \"" + ATT_CONDITION_MISCPARAM + "\".\"" + REL_MISCPARAM + "\"=\"" + REL_MISCPARAM + "\".\"ID\"\n"
			+ "\n" + "LEFT JOIN \"" + REL_DOUBLE + "Einfach\"\n" + "ON \"" + ATT_CONDITION_MISCPARAM + "\".\"" + ATT_VALUE + "\"=\"" + REL_DOUBLE + "Einfach\".\"ID\"\n";
	
	private static final String queryModelView = "SELECT\n" + "\n" + "\"" + REL_MODEL + "\".\"Formel\",\n" + "\"" + REL_MODEL + "\".\"ID\" AS \"" + ATT_MODELID + "\",\n"
			+ "\"P\".\"" + ATT_PARAMNAME + "\",\n" + "\"D\".\"" + ATT_PARAMNAME + "\" AS \"" + ATT_DEP + "\",\n" + "\"I\".\"" + ATT_PARAMNAME + "\" AS \"" + ATT_INDEP + "\",\n"
			+ "\"" + REL_MODEL + "\".\"" + ATT_NAME + "\",\n" + "\"" + ATT_MINVALUE + "\",\n" + "\"" + ATT_MAXVALUE + "\",\n" + "\"ParamDescription\",\n" + "\"" + ATT_PARAMTYPE + "\",\n" + "\"ParCategory\",\n"
			+ "\"ParUnit\",\n" + "\"DepCategory\",\n" + "\"DepUnit\",\n" + "\"DepDescription\",\n" + "\"IndepCategory\",\n" + "\"IndepUnit\",\n" + "\"" + ATT_MININDEP + "\",\n"
			+ "\"" + ATT_MAXINDEP + "\",\n" + "\"IndepDescription\",\n" + "\"LitMID\",\n" + "\"LitM\",\n" + "\"Modellkatalog\".\"" + ATT_LEVEL + "\",\n"
			+ "\"Modellkatalog\".\"Klasse\"\n" + "\n" + "FROM \"Modellkatalog\"\n" + "\n" + "LEFT JOIN \"LitMView\"\n" + "ON \"Modellkatalog\".\"ID\"=\"LitMView\".\""
			+ ATT_MODELID + "\"\n" + "\n" + "LEFT JOIN(\n" + "    SELECT \"" + ATT_MODELID + "\", \"" + ATT_PARAMNAME + "\",\n"
			+ "        \"Beschreibung\" AS \"DepDescription\",\n" + "        \"kind of property / quantity\" AS \"DepCategory\",\n"
			+ "        \"display in GUI as\" AS \"DepUnit\"\n" + "    FROM \"ModellkatalogParameter\"\n"
			+ " LEFT JOIN \"Einheiten\" ON \"Einheiten\".\"ID\" = \"ModellkatalogParameter\".\"Einheit\"" + "    WHERE \"" + ATT_PARAMTYPE + "\"=3 )AS \"D\"\n" + "ON \""
			+ REL_MODEL + "\".\"ID\"=\"D\".\"" + ATT_MODELID + "\"\n" + "\n" + "LEFT JOIN(\n" + "    SELECT\n" + "        \"" + ATT_MODELID + "\",\n" + "        ARRAY_AGG( \""
			+ ATT_PARAMNAME + "\" )AS \"" + ATT_PARAMNAME + "\",\n" + "        ARRAY_AGG( \"" + ATT_MIN + "\" )AS \"" + ATT_MININDEP + "\",\n" + "        ARRAY_AGG( \"" + ATT_MAX
			+ "\" )AS \"" + ATT_MAXINDEP + "\",\n" + "        ARRAY_AGG( \"Beschreibung\" )AS \"IndepDescription\",\n"
			+ "        ARRAY_AGG( \"kind of property / quantity\" )AS \"IndepCategory\",\n" + "        ARRAY_AGG( \"display in GUI as\" )AS \"IndepUnit\"\n"
			+ "    FROM \"ModellkatalogParameter\"\n" + " LEFT JOIN \"Einheiten\" ON \"Einheiten\".\"ID\" = \"ModellkatalogParameter\".\"Einheit\"" + "    WHERE \""
			+ ATT_PARAMTYPE + "\"=1\n" + "    GROUP BY \"" + ATT_MODELID + "\" )AS \"I\"\n" + "ON \"Modellkatalog\".\"ID\"=\"I\".\"" + ATT_MODELID + "\"\n" + "\n" + "LEFT JOIN(\n"
			+ "    SELECT\n" + "        \"" + ATT_MODELID + "\",\n" + "        ARRAY_AGG( \"" + ATT_PARAMNAME + "\" )AS \"" + ATT_PARAMNAME + "\",\n" + "        ARRAY_AGG( \""
			+ ATT_MIN + "\" )AS \"" + ATT_MINVALUE + "\",\n" + "        ARRAY_AGG( \"" + ATT_MAX + "\" )AS \"" + ATT_MAXVALUE + "\",\n"
			+ "        ARRAY_AGG( \"Beschreibung\" )AS \"ParamDescription\",\n" + "        ARRAY_AGG( \"kind of property / quantity\" )AS \"ParCategory\",\n" + "        ARRAY_AGG( \"" + ATT_PARAMTYPE + "\" )AS \"" + ATT_PARAMTYPE + "\",\n"
			+ "        ARRAY_AGG( \"display in GUI as\" )AS \"ParUnit\"\n" + "    FROM \"ModellkatalogParameter\"\n"
			+ " LEFT JOIN \"Einheiten\" ON \"Einheiten\".\"ID\" = \"ModellkatalogParameter\".\"Einheit\"" + "    WHERE \"" + ATT_PARAMTYPE + "\"=2  OR \"" + ATT_PARAMTYPE + "\"=4\n" + "    GROUP BY \""
			+ ATT_MODELID + "\" )AS \"P\"\n" + "ON \"" + REL_MODEL + "\".\"ID\"=\"P\".\"" + ATT_MODELID + "\"\n";

	private static final String querySecOnly = "SELECT " + "*" + "FROM" + "\"EstModelSecView\"\n";
	
	private static final String queryPei2 = "SELECT\n" + "\n" + "    \"MicrobialDataView\".\"" + ATT_CONDITIONID + "\",\n" + "    \"MicrobialDataView\".\"CombaseID\",\n"
			+ "    \"MicrobialDataView\".\"" + ATT_TEMPERATURE + "\",\n" + "    \"MicrobialDataView\".\"" + ATT_PH + "\",\n" + "    \"MicrobialDataView\".\"" + ATT_AW + "\",\n" + "    \"MicrobialDataView\".\"" + ATT_PRESSURE + "\",\n"
			+ "    \"MicrobialDataView\".\"" + ATT_AGENTID + "\",\n" + "    \"MicrobialDataView\".\"" + ATT_AGENTNAME + "\",\n" + "    \"MicrobialDataView\".\"" + ATT_AGENTDETAIL
			+ "\",\n" + "    \"MicrobialDataView\".\"" + ATT_MATRIXID + "\",\n" + "    \"MicrobialDataView\".\"" + ATT_MATRIXNAME + "\",\n" + "    \"MicrobialDataView\".\""
			+ ATT_MATRIXDETAIL + "\",    \n" + "    \"DataView\".\"Zeit\",\n" + "    \"DataView\".\"ZeitEinheit\" AS \"ZeitEinheitDV\",\n"
			+ "    \"DataView\".\"Konzentration\",\n" + "    \"DataView\".\"KonzentrationsEinheit\",\n" + "    \"DataView\".\"KonzentrationsObjectType\",\n"
			+ "    \"DataView\".\"Standardabweichung\",\n" + "    \"DataView\".\"Wiederholungen\",\n" + "    \"MicrobialDataView\".\"Kommentar\" AS \"MDKommentar\",\n"
			+ "    \"MicrobialDataView\".\"Guetescore\" AS \"MDGuetescore\",\n" + "    \"MicrobialDataView\".\"Geprueft\" AS \"MDGeprueft\",\n" + "    \"MicrobialDataView\".\""
			+ ATT_LITERATUREID + "\",\n" + "    \"MicrobialDataView\".\"SonstigesID\",\n" + "    \"MicrobialDataView\".\"Parameter\",\n"
			+ "    \"MicrobialDataView\".\"Beschreibung\",\n" + "    \"MicrobialDataView\".\"Einheit\",\n" + "    \"MicrobialDataView\".\"SonstigesWert\",\n"

			+ "    \"EstModelPrimView\".\"FittedModelName\",\n" + "    \"EstModelPrimView\".\"Formel\",\n" + "    \"EstModelPrimView\".\"" + ATT_DEP + "\",\n"
			+ "    \"EstModelPrimView\".\"" + ATT_INDEP + "\",\n" + "    \"EstModelPrimView\".\"" + ATT_PARAMNAME + "\",\n" + "    \"EstModelPrimView\".\"" + ATT_VALUE + "\",\n"
			+ "    \"EstModelPrimView\".\"ZeitEinheit\",\n" + "    \"EstModelPrimView\".\"Einheiten\",\n" + "    \"EstModelPrimView\".\"DepCategory\",\n"
			+ "    \"EstModelPrimView\".\"DepUnit\",\n" + "    \"EstModelPrimView\".\"DepDescription\",\n" + "    \"EstModelPrimView\".\"IndepCategory\",\n"
			+ "    \"EstModelPrimView\".\"IndepUnit\",\n" + "    \"EstModelPrimView\".\"IndepDescription\",\n" + "    \"EstModelPrimView\".\"ParCategory\",\n"
			+ "    \"EstModelPrimView\".\"ParUnit\",\n" + "    \"EstModelPrimView\".\"ParamDescription\",\n" + "    \"EstModelPrimView\".\"" + ATT_PARAMTYPE + "\",\n" + "    \"EstModelPrimView\".\"ParamP\",\n"
			+ "    \"EstModelPrimView\".\"Paramt\",\n" + "    \"EstModelPrimView\".\"" + ATT_NAME + "\",\n" + "    \"EstModelPrimView\".\"" + ATT_MODELID + "\",\n"
			+ "    \"EstModelPrimView\".\"" + ATT_ESTMODELID + "\",\n" + "    \"EstModelPrimView\".\"" + ATT_RMS + "\",\n" + "    \"EstModelPrimView\".\"" + ATT_RSQUARED + "\",\n"
			+ "    \"EstModelPrimView\".\"AIC\",\n" + "    \"EstModelPrimView\".\"BIC\",\n" + "    \"EstModelPrimView\".\"Kommentar\",\n"
			+ "    \"EstModelPrimView\".\"Guetescore\",\n" + "    \"EstModelPrimView\".\"Geprueft\",\n" + "    \"EstModelPrimView\".\"" + ATT_MIN + "\",\n"
			+ "    \"EstModelPrimView\".\"" + ATT_MAX + "\",\n" + "    \"EstModelPrimView\".\"iEinheiten\",\n" + "    \"EstModelPrimView\".\"" + ATT_MININDEP + "\",\n"
			+ "    \"EstModelPrimView\".\"" + ATT_MAXINDEP + "\",\n" + "    \"EstModelPrimView\".\"LitMID\",\n" + "    \"EstModelPrimView\".\"LitM\",\n"
			+ "    \"EstModelPrimView\".\"LitEmID\",\n" + "    \"EstModelPrimView\".\"LitEm\",\n" + "    \"EstModelPrimView\".\"" + ATT_STANDARDERROR + "\",\n"
			+ "    \"EstModelPrimView\".\"" + ATT_VARMAPTO + "\"\n" + "\n" + "FROM(\n" + "\n" + queryTimeSeries9SinDataView + "\n" + ")\"MicrobialDataView\"\n" + "\n"
			+ "RIGHT JOIN \"EstModelPrimView\"\n" + "ON \"EstModelPrimView\".\"" + ATT_CONDITIONID + "\"=\"MicrobialDataView\".\"" + ATT_CONDITIONID + "\"\n"

			+ "LEFT JOIN(\n" + "\n" + "    SELECT\n" + "\n" + "        \"" + ATT_CONDITIONID + "\",\n" + "        ARRAY_AGG( \"Zeit\" )AS \"Zeit\",\n"
			+ "        ARRAY_AGG( \"ZeitEinheit\" )AS \"ZeitEinheit\",\n" + "        ARRAY_AGG( \"Konzentration\" )AS \"Konzentration\",\n"
			+ "        ARRAY_AGG( \"KonzentrationsEinheit\" )AS \"KonzentrationsEinheit\",\n"
			+ "        ARRAY_AGG( \"KonzentrationsObjectType\" )AS \"KonzentrationsObjectType\",\n" + "        ARRAY_AGG( \"Standardabweichung\" )AS \"Standardabweichung\",\n"
			+ "        ARRAY_AGG( \"Wiederholungen\" )AS \"Wiederholungen\"\n" + "\n" + "    FROM \"MesswerteEinfach\"\n" + "    WHERE NOT( \"" + ATT_TIME + "\" IS NULL OR \""
			+ ATT_LOG10N + "\" IS NULL )\n" + "    GROUP BY \"" + ATT_CONDITIONID + "\"\n" + "\n" + ")\"DataView\"\n" + "ON \"EstModelPrimView\".\"" + ATT_CONDITIONID
			+ "\"=\"DataView\".\"" + ATT_CONDITIONID + "\"\n";

	private static final String querySei2 = "SELECT *\n" + "\n" + "FROM(\n" + "\n" + queryPei2 + "\n" + ")\"PeiView\"\n" + "\n" + "    JOIN \"Sekundaermodelle_Primaermodelle\"\n"
			+ "    ON \"Sekundaermodelle_Primaermodelle\".\"GeschaetztesPrimaermodell\"=\"PeiView\".\"" + ATT_ESTMODELID + "\"\n" + "\n" + "    JOIN \"EstModelSecView\"\n"
			+ "    ON \"Sekundaermodelle_Primaermodelle\".\"GeschaetztesSekundaermodell\"=\"EstModelSecView\".\"" + ATT_ESTMODELID + "2\"\n" + "\n"
			+ "ORDER BY \"EstModelSecView\".\"" + ATT_ESTMODELID + "2\" ASC\n";

	public static final int PARAMTYPE_INDEP = 1;
	public static final int PARAMTYPE_PARAM = 2;
	public static final int PARAMTYPE_DEP = 3;

	private Connection conn;

	public Bfrdb(final Connection conn) {
		this.conn = conn;
	}

	public Connection getConnection() {
		return conn;
	}

	public String getDBUUID() throws SQLException {
		return DBKernel.getLocalDBUUID();
	}

	public void close() throws SQLException {
		//super.close();
	}

	public ResultSet selectModel(final int level) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(queryModelView + " WHERE \"" + ATT_LEVEL + "\"=?");
		ps.setInt(1, level);
		//System.err.println(queryModelView);
		return ps.executeQuery();
	}

	public KnimeTuple getPrimModelById(int id) throws SQLException {
		KnimeTuple tuple = null;
		PreparedStatement stat = conn.prepareStatement(queryModelView + " WHERE \"" + ATT_LEVEL + "\"=1 AND \"Modellkatalog\".\"ID\"=?");
		try {
			stat.setInt(1, id);
			ResultSet result = stat.executeQuery();
			try {
				String dbuuid = DBKernel.getLocalDBUUID();
				if (result.next()) {
					String formula = result.getString("Formel");
					if (formula != null) formula = formula.replaceAll("~", "=").replaceAll("\\s", "");

					tuple = new KnimeTuple(new Model1Schema());

					PmmXmlDoc doc = new PmmXmlDoc();
					CatalogModelXml cm = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), formula, null);
					cm.modelClass = result.getInt("Klasse");
					cm.dbuuid = dbuuid;
					doc.add(cm);
					tuple.setValue(Model1Schema.ATT_MODELCATALOG, doc);

					doc = new PmmXmlDoc();
					DepXml dx = new DepXml(result.getString(Bfrdb.ATT_DEP), result.getString("DepCategory"), result.getString("DepUnit"));
					dx.description = result.getString("DepDescription");
					doc.add(dx);
					tuple.setValue(Model1Schema.ATT_DEPENDENT, doc);

					tuple.setValue(Model1Schema.ATT_INDEPENDENT, DbIo.convertArrays2IndepXmlDoc(null, result.getArray(Bfrdb.ATT_INDEP), null, null,
							result.getArray("IndepCategory"), result.getArray("IndepUnit"), result.getArray("IndepDescription"), true));

					tuple.setValue(Model1Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(null, result.getArray(Bfrdb.ATT_PARAMNAME), null, null,
							result.getArray("ParCategory"), result.getArray("ParUnit"), null, result.getArray(Bfrdb.ATT_MINVALUE), result.getArray(Bfrdb.ATT_MAXVALUE),
							result.getArray("ParamDescription"), result.getArray(Bfrdb.ATT_PARAMTYPE), null, null, null, null));

					doc = new PmmXmlDoc();
					doc.add(new EstModelXml(null, null, null, null, null, null, null, null));
					tuple.setValue(Model1Schema.ATT_ESTMODEL, doc);

					String s = result.getString("LitMID");
					if (s != null) tuple.setValue(Model1Schema.ATT_MLIT, getLiterature(s, dbuuid));

					tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
					tuple.setValue(Model1Schema.ATT_DBUUID, dbuuid);
				}

			} finally {
				result.close();
			}
		} finally {
			stat.close();
		}

		return tuple;
	}

	public KnimeTuple getSecModelById(int id) throws SQLException {
		KnimeTuple tuple = null;
		PreparedStatement stat = conn.prepareStatement(queryModelView + " WHERE \"" + ATT_LEVEL + "\"=2 AND \"Modellkatalog\".\"ID\"=?");
		try {
			stat.setInt(1, id);
			ResultSet result = stat.executeQuery();
			try {
				String dbuuid = DBKernel.getLocalDBUUID();
				if (result.next()) {
					String formula = result.getString("Formel");
					if (formula != null) formula = formula.replaceAll("~", "=").replaceAll("\\s", "");

					tuple = new KnimeTuple(new Model2Schema());

					PmmXmlDoc doc = new PmmXmlDoc();
					CatalogModelXml cm = new CatalogModelXml(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), formula, null);
					cm.modelClass = result.getInt("Klasse");
					cm.dbuuid = dbuuid;
					doc.add(cm);
					tuple.setValue(Model2Schema.ATT_MODELCATALOG, doc);

					doc = new PmmXmlDoc();
					DepXml dx = new DepXml(result.getString(Bfrdb.ATT_DEP), result.getString("DepCategory"), result.getString("DepUnit"));
					dx.description = result.getString("DepDescription");
					doc.add(dx);
					tuple.setValue(Model2Schema.ATT_DEPENDENT, doc);

					tuple.setValue(Model2Schema.ATT_INDEPENDENT, DbIo.convertArrays2IndepXmlDoc(null, result.getArray(Bfrdb.ATT_INDEP), null, null,
							result.getArray("IndepCategory"), result.getArray("IndepUnit"), result.getArray("IndepDescription"), false));

					tuple.setValue(Model2Schema.ATT_PARAMETER, DbIo.convertArrays2ParamXmlDoc(null, result.getArray(Bfrdb.ATT_PARAMNAME), null, null,
							result.getArray("ParCategory"), result.getArray("ParUnit"), null, result.getArray(Bfrdb.ATT_MINVALUE), result.getArray(Bfrdb.ATT_MAXVALUE),
							result.getArray("ParamDescription"), result.getArray(Bfrdb.ATT_PARAMTYPE), null, null, null, null));

					doc = new PmmXmlDoc();
					doc.add(new EstModelXml(null, null, null, null, null, null, null, null));
					tuple.setValue(Model2Schema.ATT_ESTMODEL, doc);

					tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, null);

					String s = result.getString("LitMID");
					if (s != null) tuple.setValue(Model2Schema.ATT_MLIT, getLiterature(s, dbuuid));

					tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
					tuple.setValue(Model2Schema.ATT_DBUUID, dbuuid);
				}

			} finally {
				result.close();
			}
		} finally {
			stat.close();
		}

		return tuple;
	}

	public PmmXmlDoc getLiteratureXml(String s, String dbuuid) {
		PmmXmlDoc l = new PmmXmlDoc();
		String[] ids = s.split(",");
		for (String id : ids) {
			LiteratureItem li = DBUtilities.getLiteratureItem(conn, Integer.valueOf(id), dbuuid);
			l.add(li);
		}
		return l;
	}

	public PmmXmlDoc getMiscXmlDoc(Integer tsID) throws SQLException {
		PmmXmlDoc miscDoc = new PmmXmlDoc();

		String q = queryXmlDoc + " WHERE \"" + REL_CONDITION + "\"=" + tsID;
		// System.out.println( q );
		PreparedStatement ps = conn.prepareStatement(q);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			MiscXml mx = new MiscXml(rs.getInt("SonstigesID"), rs.getString("Parameter"), rs.getString("Beschreibung"), rs.getDouble("Wert"), null, rs.getString("Einheit"));
			miscDoc.add(mx);
		}
		return miscDoc;
	}

	/*
	 * public PmmXmlDoc getMiscXmlDoc(ResultSet rs) throws SQLException { int
	 * condID = rs.getInt(Bfrdb.ATT_CONDITIONID); PmmXmlDoc miscDoc = new
	 * PmmXmlDoc();
	 * 
	 * do { if (rs.getObject("SonstigesID") != null) { MiscXml mx = new
	 * MiscXml(rs
	 * .getInt("SonstigesID"),rs.getString("Parameter"),rs.getString("Beschreibung"
	 * ),rs.getDouble("SonstigesWert"),rs.getString("Einheit"));
	 * miscDoc.add(mx); } } while (rs.next() && condID ==
	 * rs.getInt(Bfrdb.ATT_CONDITIONID)); rs.previous(); return miscDoc; }
	 */
	public ResultSet selectEstModel(final int level) throws SQLException {
		return selectEstModel(level, -1);
	}

	public ResultSet selectEstModel(final int level, int estimatedModelID) throws SQLException {
		return selectEstModel(level, estimatedModelID, "");
	}

	public ResultSet selectEstModel(final int level, String where) throws SQLException {
		return selectEstModel(level, -1, where);
	}

	public ResultSet selectEstModel(final int level, int estimatedModelID, String where) throws SQLException {
		return selectEstModel(level, estimatedModelID, where, false);
	}

	public ResultSet selectEstModel(final int level, int estimatedModelID, String where, boolean forceUpdate) throws SQLException {
		String q;
		String myWhere = "";
		String myWhereCache = "";
		if (level == 1) {
			q = queryPei2;
			//System.err.println(q);
			if (estimatedModelID > 0) {
				myWhere = " WHERE \"EstModelPrimView\".\"" + ATT_ESTMODELID + "\" = " + estimatedModelID;
				myWhereCache = " WHERE \"" + ATT_ESTMODELID + "\" = " + estimatedModelID;
			}
		} else if (level == 2) {
			q = querySei2;
			if (estimatedModelID > 0) {
				myWhere = " WHERE \"EstModelSecView\".\"" + ATT_ESTMODELID + "2\" = " + estimatedModelID;
				myWhereCache = " WHERE \"" + ATT_ESTMODELID + "2\" = " + estimatedModelID;
			}
		}
		else { // level = 3
			q = querySecOnly;
			if (estimatedModelID > 0) {
				myWhere = " WHERE \"EstModelSOView\".\"" + ATT_ESTMODELID + "\" = " + estimatedModelID;
				myWhereCache = " WHERE \"" + ATT_ESTMODELID + "\" = " + estimatedModelID;
			}
		}
		if (where != null && !where.isEmpty()) {
			myWhere = " WHERE " + where;
			myWhereCache = " WHERE " + where;
		}
		//System.err.println(q + myWhere);
		return getCachedTable("CACHE_selectEstModel" + level, q, myWhere, myWhereCache, new String[] { "GeschaetzteModelle", "Modellkatalog", "ModellkatalogParameter",
				"GeschaetzteParameter", "GueltigkeitsBereiche", "Modell_Referenz", "Literatur", "GeschaetztesModell_Referenz", "Sekundaermodelle_Primaermodelle", "Einheiten",
				"Versuchsbedingungen_Sonstiges", "SonstigeParameter", "DoubleKennzahlen", "Messwerte", "Versuchsbedingungen" });
	}

	private String prepareCaching(ResultSet rs, String cacheTableneme) throws SQLException {
		String sql = "CREATE TABLE " + DBKernel.delimitL(cacheTableneme) + " (";
		ResultSetMetaData mtd = rs.getMetaData();
		for (int i = 1; i <= mtd.getColumnCount(); i++) {
			String cn = mtd.getColumnLabel(i);
			String ct = mtd.getColumnTypeName(i);
			int cs = mtd.getColumnDisplaySize(i);
			if (cs > 2000) cs = 16383; // 16383; // 2047
			String toAppend = DBKernel.delimitL(cn) + " ";
			if (ct.equals("VARCHAR")) toAppend += ct + "(" + cs + "),";
			else if (ct.equals("VARCHAR ARRAY")) toAppend += "VARCHAR(" + cs + ") ARRAY,";
			else toAppend += ct + ",";
			sql += toAppend;
		}
		sql = sql.substring(0, sql.length() - 1) + ");";
		return sql;
	}

	public ResultSet selectTs() throws SQLException {
		return selectTs("");
	}

	public ResultSet selectTs(String where) throws SQLException {
		return selectTs(where, false);
	}

	private ResultSet selectTs(String where, boolean forceUpdate) throws SQLException {
		//return pushQuery(queryTimeSeries9, true);
		//System.err.println(queryTimeSeries9);
		return getCachedTable("CACHE_TS", queryTimeSeries9, where, where, new String[] { "Einheiten", "Versuchsbedingungen_Sonstiges", "SonstigeParameter", "DoubleKennzahlen",
				"Messwerte", "Versuchsbedingungen" });
	}

	private ResultSet getCachedTable(String cacheTable, String selectSQL, String whereSQL, String cacheWhereSQL, String[] relevantTables) throws SQLException {
		boolean dropCacheFirst = false;
		long lastCaching = DBKernel.getLastCache(conn, cacheTable);
		//System.err.println(selectSQL + whereSQL);
		//long ttt = System.currentTimeMillis();
		long lastRelevantChange = DBKernel.getLastRelevantChange(conn, relevantTables);
		//System.err.println(System.currentTimeMillis() - ttt);
		if (lastRelevantChange > lastCaching) {
			dropCacheFirst = true;
			DBKernel.setLastCache(conn, cacheTable, System.currentTimeMillis());
		}

		if (!dropCacheFirst && !cacheTable.isEmpty() && DBKernel.getRowCount(conn, cacheTable, "") > 0) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + DBKernel.delimitL(cacheTable) + " " + cacheWhereSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			return ps.executeQuery();
		}
		String selectWhereSQL = selectSQL;
		if (whereSQL.indexOf("WHERE") >= 0) whereSQL = " AND " + whereSQL.substring(whereSQL.indexOf("WHERE") + 5);
		int orderIndex = selectSQL.lastIndexOf("ORDER BY ");
		if (orderIndex > 0) selectWhereSQL = selectSQL.substring(0, orderIndex) + " " + whereSQL + " " + selectSQL.substring(orderIndex);
		else selectWhereSQL = selectSQL + " " + whereSQL;
		//System.err.println(selectWhereSQL);
		PreparedStatement ps = conn.prepareStatement(selectWhereSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = ps.executeQuery();
		if (!cacheTable.isEmpty()) {
			String createSQL = prepareCaching(rs, cacheTable);
			//System.err.println(createSQL);
			DBKernel.sendRequest(conn, "DROP TABLE " + DBKernel.delimitL(cacheTable) + " IF EXISTS", false, true);
			DBKernel.sendRequest(conn, createSQL, false, true);
			DBKernel.sendRequest(conn, "GRANT SELECT ON TABLE \"" + cacheTable + "\" TO \"PUBLIC\";", false, true);
			//System.err.println(q);
			DBKernel.sendRequest(conn, "INSERT INTO " + DBKernel.delimitL(cacheTable) + " (" + selectSQL + ")", false, true);
			if (conn == null || conn.isClosed()) {
				try {
					conn = DBKernel.getDBConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ps = conn.prepareStatement("SELECT * FROM " + DBKernel.delimitL(cacheTable) + " " + cacheWhereSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			return ps.executeQuery();
		}
		return rs;
	}

	public ResultSet selectRelatedLiterature(final String modelName) throws SQLException {
		String q = "SELECT \"" + ATT_LITERATUREID + "\".\"" + ATT_FIRSTAUTHOR + "\", \"" + ATT_LITERATUREID + "\".\"" + ATT_YEAR + "\" FROM \"" + REL_MODEL + "\" JOIN \""
				+ REL_MODEL_LITERATURE + "\" ON \"" + REL_MODEL + "\".\"ID\"=\"" + REL_MODEL_LITERATURE + "\".\"" + ATT_MODELID + "\" JOIN \"" + ATT_LITERATUREID + "\" ON \""
				+ ATT_LITERATUREID + "\".\"ID\"=\"" + REL_MODEL_LITERATURE + "\".\"" + ATT_LITERATUREID + "\" WHERE \"" + ATT_NAME + "\"=?";

		PreparedStatement psSelectRelLit = conn.prepareStatement(q);

		psSelectRelLit.setString(1, modelName);

		return psSelectRelLit.executeQuery();
	}

	public String getRelatedLiterature(final String modelName) {

		ResultSet result;
		String ret;

		ret = "";

		try {

			result = selectRelatedLiterature(modelName);

			while (result.next()) {

				if (!ret.isEmpty()) {
					ret += ", ";
				}

				ret += result.getString(1) + " et al. " + result.getString(2);
			}

			result.getStatement().close();
			result.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return ret;
	}

	public HashMap<Integer, String> getPossibleLiterature() {

		HashMap<Integer, String> ret;
		ResultSet result;

		ret = new HashMap<>();

		try {

			PreparedStatement psSelectPossLit;

			psSelectPossLit = conn.prepareStatement(

			"SELECT \"ID\",\"" + ATT_FIRSTAUTHOR + "\", \"" + ATT_YEAR + "\" FROM \"" + ATT_LITERATUREID + "\"");

			result = psSelectPossLit.executeQuery();

			while (result.next()) {

				ret.put(result.getInt("ID"), result.getString(2) + " et al. " + result.getInt(3));
			}

			result.getStatement().close();
			result.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return ret;
	}

	public int getNumLitEntry() {

		int n;
		PreparedStatement psNumLitEntry;
		ResultSet result;

		n = -1;

		try {

			psNumLitEntry = conn.prepareStatement("SELECT COUNT( * )FROM \"" + ATT_LITERATUREID + "\"");

			result = psNumLitEntry.executeQuery();

			result.next();
			return result.getInt(1);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return n;
	}

	public int getNumModel() {

		int n;
		PreparedStatement psNumLitEntry;
		ResultSet result;

		n = -1;

		try {

			psNumLitEntry = conn.prepareStatement("SELECT COUNT( * )FROM \"" + REL_MODEL + "\"");

			result = psNumLitEntry.executeQuery();

			result.next();
			return result.getInt(1);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return n;
	}

	public LinkedList<String> getPossibleModelName() {

		LinkedList<String> ret;
		String q;
		PreparedStatement psPossibleModelName;
		ResultSet result;

		ret = new LinkedList<>();

		q = "SELECT \"" + ATT_NAME + "\" FROM \"" + REL_MODEL + "\"";

		try {

			psPossibleModelName = conn.prepareStatement(q);

			result = psPossibleModelName.executeQuery();

			while (result.next()) {
				ret.add(result.getString(1));
			}

			result.getStatement().close();
			result.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return ret;
	}

	public HashMap<Integer, List<Integer>> getModLitMatrix() {

		HashMap<Integer, List<Integer>> resultt = new HashMap<>();
		//boolean[][] mat;
		//int n, m, i, j;
		//HashMap<Integer, String> literature;
		//LinkedList<String> model;
		String q;
		PreparedStatement psLitMat;
		ResultSet result;

		//n = getNumLitEntry();
		//m = getNumModel();

		//mat = new boolean[ n ][ m ];

		//literature = getPossibleLiterature();
		//model = getPossibleModelName();

		q = "SELECT \"Modell\",\"Literatur\" FROM \"" + REL_MODEL_LITERATURE + "\"";

		try {

			psLitMat = conn.prepareStatement(q);

			result = psLitMat.executeQuery();

			while (result.next()) {

				//i = indexOf( literature, result.getString( 2 ) );
				//j = indexOf( model, result.getString( 3 ) );

				//mat[ i ][ j ] = true;
				List<Integer> li = resultt.get(result.getInt(1));
				if (li == null) {
					li = new ArrayList<>();
				}
				li.add(result.getInt(2));
				resultt.put(result.getInt(1), li);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return resultt;
	}

	public void insertEm2(final Integer secID, final List<Integer> primIDs, Integer gmId) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"Sekundaermodelle_Primaermodelle\" (\"GeschaetztesPrimaermodell\", \"GeschaetztesSekundaermodell\", \"GlobalModel\")VALUES(?,?,?)");
			for (Integer id : primIDs) {
				if (id != null && id >= 0) {
					ps.setInt(1, id);
					ps.setInt(2, secID);
					if (gmId == null) ps.setNull(3, java.sql.Types.INTEGER);
					else ps.setInt(3, gmId);
					ps.executeUpdate();
				}
			}
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public Integer insertEm(final ParametricModel pm, Integer workflowID) { // , HashMap<String, String> hm
		return insertEm(pm, workflowID, null);
	}

	public Integer insertGm(Integer gmID) {
		Integer resultID = null;
		try {
			if (isObjectPresent("GlobalModels", gmID)) {
				resultID = gmID;
				/*
				 * PreparedStatement ps = conn.prepareStatement(
				 * "UPDATE \"GlobalModels\" SET \"Modellname\" = ? WHERE \"ID\"="
				 * +gmID); ps.setString(1, "GM_" + gmID); ps.executeUpdate();
				 * ps.close();
				 */
			} else {
				PreparedStatement ps = conn.prepareStatement("INSERT INTO \"GlobalModels\" (\"Modellname\") VALUES(?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, "GM_" + gmID);
				if (ps.executeUpdate() > 0) {
					ResultSet result = ps.getGeneratedKeys();
					result.next();
					resultID = result.getInt(1);
					result.close();
				}
				ps.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return resultID;
	}

	public Integer insertEm(final ParametricModel pm, Integer workflowID, final ParametricModel ppm) { // , HashMap<String, String> hm
		Integer estModelId = null;

		Double rms = pm.getRms();
		Double r2 = pm.getRsquared();

		estModelId = pm.getEstModelId();
		int condId = pm.getCondId();
		int modelId = pm.modelId;
		String fittedModelName = pm.getFittedModelName();

		HashMap<String, Integer> hmi = new HashMap<>();
		int responseId = queryParamId(modelId, pm.getDepXml().origName, PARAMTYPE_DEP);
		if (!pm.getDepXml().origName.equals(pm.getDepXml().name)) hmi.put(pm.getDepXml().name, responseId);

		if (responseId < 0) {
			if (ppm != null) responseId = queryParamId(ppm.modelId, pm.getDepXml().origName, PARAMTYPE_PARAM);
			if (responseId < 0) System.err.println("responseId < 0..." + pm.getDepVar() + "\t" + pm.getDepXml().origName);
		}

		if (isObjectPresent(REL_ESTMODEL, estModelId)) {
			updateEstModel(estModelId, fittedModelName, condId, modelId, rms, r2, pm.getAic(), pm.getBic(), responseId, pm.qualityScore, pm.isChecked, workflowID,
					pm.comment);
		} else {
			estModelId = insertEstModel(fittedModelName, condId, modelId, rms, r2, pm.getAic(), pm.getBic(), responseId, pm.qualityScore, pm.isChecked, workflowID,
					pm.comment);
			pm.setEstModelId(estModelId);
		}

		deleteFrom("GeschaetzteParameterCovCor", "GeschaetztesModell", estModelId);
		deleteFrom("GeschaetzteParameter", "GeschaetztesModell", estModelId);
		for (PmmXmlElementConvertable el : pm.getParameter().getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				int paramId = queryParamId(modelId, px.origName, PARAMTYPE_PARAM);
				if (paramId < 0) {
					System.err.println("paramId < 0... " + px.origName);
				}
				if (!px.origName.equals(px.name)) hmi.put(px.name, paramId);
				insertEstParam(estModelId, paramId, px.value, px.error, px.unit, pm, false, px.name);
			}
		}
		for (PmmXmlElementConvertable el : pm.getParameter().getElementSet()) {
			if (el instanceof ParamXml) {
				ParamXml px = (ParamXml) el;
				int paramId = queryParamId(modelId, px.origName, PARAMTYPE_PARAM);
				if (paramId < 0) {
					System.err.println("paramId < 0... " + px.origName);
				}
				insertEstParamCorrs(modelId, estModelId, paramId, px.correlations);
			}
		}

		insertModLit(estModelId, pm.getEstModelLit(), true, pm);

		deleteFrom("GueltigkeitsBereiche", "GeschaetztesModell", estModelId);
		for (PmmXmlElementConvertable el : pm.getIndependent().getElementSet()) {
			if (el instanceof IndepXml) {
				IndepXml ix = (IndepXml) el;
				int indepId = queryParamId(modelId, ix.origName, PARAMTYPE_INDEP);
				if (indepId >= 0) {
					insertMinMaxIndep(estModelId, indepId, ix.min, ix.max);
					if (!ix.origName.equals(ix.name)) hmi.put(ix.name, indepId);
					insertEstParam(estModelId, indepId, null, null, ix.unit, pm, true, ix.name);
				} else {
					System.err.println("insertEm:\t" + ix.origName + "\t" + modelId);
				}
			}
		}

		// insert mapping of parameters and variables of this estimation
		deleteFrom("VarParMaps", "GeschaetztesModell", estModelId);
		for (String newName : hmi.keySet()) {
			insertVarParMaps(estModelId, hmi.get(newName), newName);
		}

		return estModelId;
	}

	private void insertVarParMaps(final int estModelId, final int paramId, final String newVarPar) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"VarParMaps\" (\"GeschaetztesModell\", \"VarPar\", \"VarParMap\") VALUES (?,?,?)");
			ps.setInt(1, estModelId);
			ps.setInt(2, paramId);
			if (newVarPar == null) {
				ps.setNull(3, Types.VARCHAR);
			} else {
				ps.setString(3, newVarPar);
			}
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void insertMinMaxIndep(final int estModelId, final int paramId, final Double min, final Double max) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"GueltigkeitsBereiche\" (\"GeschaetztesModell\", \"Parameter\", \"Gueltig_von\", \"Gueltig_bis\")VALUES(?,?,?,?)");
			ps.setInt(1, estModelId);
			ps.setInt(2, paramId);
			if (min == null) {
				ps.setNull(3, Types.DOUBLE);
			} else {
				ps.setDouble(3, min);
			}
			if (max == null) {
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(4, max);
			}
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private Integer insertCondition(Integer condId, final Integer tempId, final Integer phId, final Integer awId, final String agentName, final String matrixName,
			final String combaseId, Integer matrixId, Integer agentId, final String agentDetail, final String matrixDetail, PmmXmlDoc misc, final String comment, final Integer qs, final Boolean checked, PmmXmlDoc lit,
			PmmTimeSeries ts) {

		String warnings = "";

		boolean doUpdate = isObjectPresent("Versuchsbedingungen", condId);
		Integer cdai = null;
		/*
		 * cdai = combaseDataAlreadyIn(combaseId); if (!doUpdate && cdai !=
		 * null) { condId = cdai;//return null; doUpdate = true; }
		 */
		Integer resultID = null;
		PreparedStatement ps;

		try {
			//if (agentId == null || agentId < 0) {
			agentId = queryAgentId(agentName == null ? agentDetail : agentName);
			if (agentId == null && (agentName != null || agentDetail != null)) {
				String sql = "INSERT INTO \"Agenzien\" (\"Agensname\") VALUES (?)";
				PreparedStatement psmt = DBKernel.getDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				psmt.setString(1, agentName == null ? agentDetail : agentName);
				if (psmt.executeUpdate() > 0) {
					agentId = DBKernel.getLastInsertedID(psmt);
					if (agentId != null) {
						sql = "INSERT INTO \"Codes_Agenzien\" (\"CodeSystem\",\"Code\",\"Basis\") VALUES ('PMF',?,?)";
						psmt = DBKernel.getDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						psmt.setString(1, agentId + "");
						psmt.setInt(2, agentId);
						psmt.executeUpdate();
					}
				}
			}
			//}
			//if (matrixId == null || matrixId < 0) {
			matrixId = queryMatrixId(matrixName == null ? matrixDetail : matrixName);
			if (matrixId == null && (matrixName != null || matrixDetail != null)) {
				String sql = "INSERT INTO \"Matrices\" (\"Matrixname\") VALUES (?)";
				PreparedStatement psmt = DBKernel.getDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				psmt.setString(1, matrixName == null ? matrixDetail : matrixName);
				if (psmt.executeUpdate() > 0) {
					matrixId = DBKernel.getLastInsertedID(psmt);
					if (matrixId != null) {
						sql = "INSERT INTO \"Codes_Matrices\" (\"CodeSystem\",\"Code\",\"Basis\") VALUES ('PMF',?,?)";
						psmt = DBKernel.getDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						psmt.setString(1, matrixId + "");
						psmt.setInt(2, matrixId);
						psmt.executeUpdate();
					}
				}
			}
			//}

			if (doUpdate) {
				ps = conn.prepareStatement("UPDATE \"Versuchsbedingungen\" SET \"" + ATT_TEMPERATURE + "\"=?, \"" + ATT_PH + "\"=?, \"" + ATT_AW + "\"=?, \"" + ATT_AGENTID
						+ "\"=?, \"AgensDetail\"=?, \"" + ATT_MATRIXID + "\"=?, \"MatrixDetail\"=?, \"b_f_details_CB\"=?, \"Kommentar\"=?, \"Guetescore\"=?, \"Geprueft\"=?, \"Referenz\"=? WHERE \"ID\"=?");
			} else {
				ps = conn.prepareStatement("INSERT INTO \"Versuchsbedingungen\" (\"" + ATT_TEMPERATURE + "\", \"" + ATT_PH + "\", \"" + ATT_AW + "\", \"" + ATT_AGENTID
						+ "\", \"AgensDetail\", \"" + ATT_MATRIXID
						+ "\", \"MatrixDetail\", \"b_f_details_CB\", \"Kommentar\", \"Guetescore\", \"Geprueft\", \"Referenz\" ) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS);
			}

			if (tempId >= 0) {
				ps.setInt(1, tempId);
			} else {
				ps.setNull(1, Types.INTEGER);
			}

			if (phId >= 0) {
				ps.setInt(2, phId);
			} else {
				ps.setNull(2, Types.INTEGER);
			}

			if (awId >= 0) {
				ps.setInt(3, awId);
			} else {
				ps.setNull(3, Types.INTEGER);
			}

			if (agentId == null || agentId <= 0) {
				ps.setNull(4, Types.INTEGER);
				warnings += "Agent not defined (" + agentDetail + ") for condition ID " + condId + "\n";
			} else {
				ps.setInt(4, agentId);
				try {
					ts.setAgentId(agentId);
				} catch (PmmException e) {
					e.printStackTrace();
				}
			}
			if (agentDetail == null) {
				ps.setNull(5, Types.VARCHAR);
			} else {
				ps.setString(5, agentDetail);
			}
			if (matrixId == null || matrixId <= 0) {
				ps.setNull(6, Types.INTEGER);
				warnings += "Matrix not defined (" + matrixDetail + ") for condition ID " + condId + "\n";
			} else {
				ps.setInt(6, matrixId);
				try {
					ts.setMatrixId(matrixId);
				} catch (PmmException e) {
					e.printStackTrace();
				}
			}
			if (matrixDetail == null) {
				ps.setNull(7, Types.VARCHAR);
			} else {
				ps.setString(7, matrixDetail);
			}
			/*
			 * if( misc == null ) { ps.setNull( 8, Types.VARCHAR ); } else {
			 * ps.setString( 8, misc ); }
			 */
			ps.setNull(8, Types.VARCHAR);
			
			if (comment == null) {
				ps.setNull(9, Types.VARCHAR);
			} else {
				ps.setString(9, comment);
			}

			if (qs == null) {
				ps.setNull(10, Types.INTEGER);
			} else {
				ps.setInt(10, qs);
			}

			if (checked == null) {
				ps.setNull(11, Types.BOOLEAN);
			} else {
				ps.setBoolean(11, checked);
			}

			insertLiteratureInCase(lit);
			List<PmmXmlElementConvertable> l = lit.getElementSet();
			if (l.size() > 0) {
				LiteratureItem li = (LiteratureItem) l.get(0);
				ps.setInt(12, li.id);
			} else {
				ps.setNull(12, Types.INTEGER);
			}
			if (doUpdate) {
				ps.setInt(13, condId);

				ps.executeUpdate();
				resultID = condId;
			} else {
				if (ps.executeUpdate() > 0) {
					ResultSet result = ps.getGeneratedKeys();
					result.next();
					resultID = result.getInt(1);

					result.close();
				}

			}
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cdai == null && resultID != null && combaseId != null && !combaseId.isEmpty()) {
			insertCondComb(resultID, combaseId);
		}
		String hcWarnings = handleConditions(resultID, misc, ts);
		warnings += hcWarnings;
		ts.setWarning(warnings);

		return resultID;
	}

	private String handleConditions(final Integer condId, final PmmXmlDoc misc, PmmTimeSeries ts) {
		String result = "";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("DELETE FROM \"Versuchsbedingungen_Sonstiges\" WHERE \"Versuchsbedingungen\" = " + condId);
			ps.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (condId != null && condId >= 0 && misc != null) {
			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				if (el instanceof MiscXml) {
					MiscXml mx = (MiscXml) el;
					String n = mx.name;
					String d = mx.description;
					if (n == null || n.isEmpty()) n = d;
					if (d == null || d.isEmpty() || d.equals("null")) d = n;
					if (n != null && !n.equals(AttributeUtilities.ATT_TEMPERATURE) && !n.equals(AttributeUtilities.ATT_PH) && !n.equals(AttributeUtilities.ATT_AW)) {
						//Integer paramID = getID("SonstigeParameter", "Beschreibung", d.toLowerCase()); // Beschreibung
						Integer paramID = getID("SonstigeParameter", "Parameter", n); // Parameter
						if (paramID == null) {
							try {
								if (n != null && d != null && !n.isEmpty() && !d.isEmpty()) {
									ps = conn.prepareStatement("INSERT INTO \"SonstigeParameter\" (\"Parameter\", \"Beschreibung\") VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
									ps.setString(1, n);
									ps.setString(2, d.toLowerCase());
									if (ps.executeUpdate() > 0) {
										ResultSet rs = ps.getGeneratedKeys();
										rs.next();
										paramID = rs.getInt(1);
										rs.close();
									}
									ps.close();
								}
							} catch (SQLException ex) {
								ex.printStackTrace();
							}
						}
						if (paramID != null) {
							//System.err.println("handleConditions:\t" + after + "\t" + dbl + "\t" + unit + "\t" + paramID + "\t" + (condIDs == null ? condIDs : condIDs.get(i)));
							try {
								ps = conn.prepareStatement("INSERT INTO \"Versuchsbedingungen_Sonstiges\" (\"Versuchsbedingungen\", \"SonstigeParameter\", \"Wert\", \"Einheit\", \"Ja_Nein\") VALUES (?,?,?,?,?)");
								ps.setInt(1, condId);
								ps.setInt(2, paramID);
								if (mx.origUnit == null) mx.origUnit = mx.unit;
								Object eid = mx.origUnit == null || mx.origUnit.isEmpty() ? null : DBKernel.getID("Einheiten", new String[] { "display in GUI as" },
										new String[] { mx.origUnit });
								if (eid == null && (mx.value == null || Double.isNaN(mx.value))) {
									ps.setNull(3, Types.DOUBLE);
									ps.setNull(4, Types.INTEGER);
									ps.setBoolean(5, true);
									ps.executeUpdate();
								} else {
									try {
										ps.setBoolean(5, false);
										if (mx.value == null || Double.isNaN(mx.value)) {
											ps.setNull(3, Types.DOUBLE);
										} else {
											Double origVal = (mx.origUnit == null ? mx.value : Categories.getCategoryByUnit(mx.unit).convert(mx.value,
													mx.unit, mx.origUnit));
											int valId = insertDouble(origVal);
											ps.setDouble(3, valId);
										}
										if (eid == null) {
											ps.setNull(4, Types.INTEGER);
										} else {
											ps.setInt(4, (Integer) eid);
										}
										ps.executeUpdate();
									} catch (ConvertException e) {
										e.printStackTrace();
									}
								}
								//try {ts.addValue(TimeSeriesSchema.ATT_MISCID, paramID);} catch (PmmException e) {e.printStackTrace();}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							//System.err.println("handleConditions, paramID not known:\t" + val + "\t" + after);
							result += "Insert of Misc failed:\t" + mx.name + "\t" + mx.description + "\n";
						}
					}
				}
			}
		}
		return result;
	}

	private void insertLiteratureInCase(PmmXmlDoc lit) {
		try {
			int i = 0;
			for (PmmXmlElementConvertable el : lit.getElementSet()) {
				if (el instanceof LiteratureItem) {
					LiteratureItem li = (LiteratureItem) el;
					if (li.id <= 0 || DBKernel.getValue(conn, "Literatur", "ID", "" + li.id, "ID") == null) {
						PreparedStatement psm = conn.prepareStatement(
								"INSERT INTO \"Literatur\" (\"Erstautor\", \"Jahr\", \"Titel\", \"Abstract\", \"Journal\", \"Volume\", \"Issue\", \"Seite\", \"FreigabeModus\", \"Webseite\", \"Literaturtyp\", \"Kommentar\") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
						if (li.author == null) psm.setNull(1, Types.VARCHAR);
						else psm.setString(1, li.author);
						if (li.year == null) psm.setNull(2, Types.INTEGER);
						else psm.setInt(2, li.year);
						if (li.title == null) psm.setNull(3, Types.VARCHAR);
						else psm.setString(3, li.title);
						if (li.abstractText == null) psm.setNull(4, Types.VARCHAR);
						else psm.setString(4, li.abstractText);
						if (li.journal == null) psm.setNull(5, Types.VARCHAR);
						else psm.setString(5, li.journal);
						if (li.volume == null) psm.setNull(6, Types.VARCHAR);
						else psm.setString(6, li.volume);
						if (li.issue == null) psm.setNull(7, Types.VARCHAR);
						else psm.setString(7, li.issue);
						if (li.page == null) psm.setNull(8, Types.INTEGER);
						else psm.setInt(8, li.page);
						if (li.approvalMode == null) psm.setNull(9, Types.INTEGER);
						else psm.setInt(9, li.approvalMode);
						if (li.website == null) psm.setNull(10, Types.VARCHAR);
						else psm.setString(10, li.website);
						if (li.type == null) psm.setNull(11, Types.INTEGER);
						else psm.setInt(11, li.type);
						if (li.comment == null) psm.setNull(12, Types.VARCHAR);
						else psm.setString(12, li.comment);
						int newID = 0;
						try {
							if (psm.executeUpdate() > 0) {
								ResultSet result = psm.getGeneratedKeys();
								result.next();
								newID = result.getInt(1);
								result.close();
							}
						} catch (Exception e) {
							if (e.getMessage().startsWith("integrity constraint violation: unique")) {
								String sql = "";
								try {
									sql = "SELECT \"ID\" FROM \"Literatur\" WHERE \"Erstautor\" = '" + li.author.replace("'", "''") + "' AND \"Jahr\" = " + li.year
											+ " AND \"Titel\" = '" + li.title.replace("'", "''") + "'";
									ResultSet result = getResultSet(sql, false);
									if (result != null && result.first()) {
										newID = result.getInt(1);
										result.close();
									}
								} catch (SQLException ex) {
									ex.printStackTrace();
								}
							}
						}
						if (newID > 0) {
							li.id = newID;
							lit.set(i, li);
						} else {
							MyLogger.handleMessage("insertLiteratureInCase failed... " + psm);
						}
						psm.close();
					}
				}
				i++;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void deleteFrom(String tablename, String fieldname, int id) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM \"" + tablename + "\" WHERE \"" + fieldname + "\"=" + id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private Double convert(Category cat, String fromUnit, Double value, String toUnit) {
		Double newValue;
		try {
			newValue = cat.convert(value, fromUnit, toUnit);
		} catch (ConvertException e) {
			newValue = value;
		}
		return newValue;
	}

	public Integer insertTs(final PmmTimeSeries ts) throws PmmException {
		Integer condId = ts.getCondId();
		Double ph = ts.getPh();
		Double temp = ts.getTemperature();
		temp = convert(Categories.getTempCategory(), ts.getTemperatureUnit(), temp, "°C");
		Double aw = ts.getWaterActivity();
		String agentName = ts.getAgentName();
		String matrixName = ts.getMatrixName();
		String combaseId = ts.getCombaseId();
		Integer matrixId = ts.getMatrixId();
		Integer agentId = ts.getAgentId();
		String agentDetail = ts.getAgentDetail();
		String matrixDetail = ts.getMatrixDetail();
		String comment = ts.getComment();
		Integer qs = ts.getQualityScore();
		Boolean checked = ts.getChecked();
		PmmXmlDoc misc = ts.getMisc();
		PmmXmlDoc lit = ts.getLiterature();
		PmmXmlDoc mdData = ts.getTimeSeries();

		int tempId = insertDouble(temp);
		int phId = insertDouble(ph);
		int awId = insertDouble(aw);

		condId = insertCondition(condId, tempId, phId, awId, agentName, matrixName, combaseId, matrixId, agentId, agentDetail, matrixDetail, misc, comment, qs, checked, lit, ts);

		ts.setLiterature(lit);
		ts.setMisc(misc);
		ts.setCondId(condId);
		if (condId == null || condId < 0) {
			return null;
		}

		// delete old data
		deleteFrom("Messwerte", "Versuchsbedingungen", condId);
		for (PmmXmlElementConvertable el : mdData.getElementSet()) {
			if (el instanceof TimeSeriesXml) {
				TimeSeriesXml tsx = (TimeSeriesXml) el;
				try {
					Double origTime = null, origConc = null, origConcStdDev = null;
					if (tsx.origTimeUnit == null || tsx.origTimeUnit.trim().isEmpty()) {
						tsx.origTimeUnit = tsx.timeUnit;
						origTime = tsx.time;
					} else {
						origTime = Categories.getTimeCategory().convert(tsx.time, tsx.timeUnit, tsx.origTimeUnit);
					}
					if (tsx.origConcentrationUnit == null || tsx.origConcentrationUnit.trim().isEmpty()) {
						tsx.origConcentrationUnit = tsx.concentrationUnit;
						origConc = tsx.concentration;
						origConcStdDev = tsx.concentrationStdDev;
					} else {
						origConc = Categories.getCategoryByUnit(tsx.concentrationUnit).convert(tsx.concentration, tsx.concentrationUnit,
								tsx.origConcentrationUnit);
						origConcStdDev = Categories.getCategoryByUnit(tsx.concentrationUnit).convert(tsx.concentrationStdDev, tsx.concentrationUnit,
								tsx.origConcentrationUnit);
					}
					int timeId = insertDouble(origTime);
					int lognId = insertDouble(origConc, origConcStdDev, tsx.numberOfMeasurements);
					insertData(condId, timeId, lognId, tsx.origTimeUnit, tsx.origConcentrationUnit, tsx.concentrationUnitObjectType);
				} catch (ConvertException e) {
					//System.out.println(tsx.getTime() + "\t" + tsx.getTimeUnit() + "\t" + tsx.getOrigTimeUnit());
					e.printStackTrace();
				}
			}
		}
		return condId;
	}

	public Integer insertM(final ParametricModel m) {
		int modelId = m.modelId;
		Integer fID = getId4Formula(m.getFormula(), m.getLevel(), modelId);
		boolean iop = isObjectPresent("Modellkatalog", modelId);

		if (iop && fID != null && fID == modelId) {
			try {
				PreparedStatement ps = conn.prepareStatement("UPDATE \"Modellkatalog\" SET \"Name\"=?, \"Kommentar\"=? WHERE \"ID\"=?");
				ps.setString(1, m.modelName);
				if (m.comment == null) {
					ps.setNull(2, Types.VARCHAR);
				} else {
					ps.setString(2, m.comment);
				}
				ps.setInt(3, modelId);

				ps.executeUpdate();
				ps.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		else {
			if (iop) {
				modelId = MathUtilities.getRandomNegativeInt();
				m.modelId = modelId;
			}
			Date date = new Date(System.currentTimeMillis());

			try {
				PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO \"Modellkatalog\" (\"Name\", \"Level\", \"Eingabedatum\", \"Formel\", \"Notation\", \"Klasse\", \"Kommentar\") VALUES( ?, ?, ?, ?, ?, ?, ? )",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, m.modelName); //  + "_" + (-modelId)
				ps.setInt(2, m.getLevel());
				ps.setDate(3, date);
				ps.setString(4, m.getFormula());
				ps.setString(5, m.modelName.toLowerCase().replaceAll("\\s", "_"));
				if (m.getModelClass() == null) ps.setNull(6, java.sql.Types.INTEGER);
				else ps.setInt(6, m.getModelClass());
				if (m.comment == null) {
					ps.setNull(7, Types.VARCHAR);
				} else {
					ps.setString(7, m.comment);
				}

				modelId = -1;
				if (ps.executeUpdate() > 0) {
					ResultSet result = ps.getGeneratedKeys();
					result.next();
					modelId = result.getInt(1);
					m.modelId = modelId;
					result.close();
				}
				ps.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			if (modelId < 0) {
				return null;
			}
			
			// insert dependent variable
			DepXml depXml = m.getDepXml();
			insertParam(modelId, depXml.origName, PARAMTYPE_DEP, null, null, depXml.category, depXml.unit, depXml.description);
			if (depXml.unit == null || depXml.unit.isEmpty()) {
				m.warning = m.warning + "\nUnit not defined for dependant variable '" + depXml.name + "' in model with ID " + m.modelId + "!";
			}

			// insert independent variable set
			for (PmmXmlElementConvertable el : m.getIndependent().getElementSet()) {
				if (el instanceof IndepXml) {
					IndepXml ix = (IndepXml) el;
					insertParam(modelId, ix.origName, PARAMTYPE_INDEP, ix.min, ix.max, ix.category, ix.unit, ix.description);
					if (ix.unit == null || ix.unit.isEmpty()) {
						m.warning = m.warning + "\nUnit not defined for independant variable '" + ix.name + "' in model with ID " + m.modelId + "!";
					}
				}
			}

			// insert parameters
			for (PmmXmlElementConvertable el : m.getParameter().getElementSet()) {
				if (el instanceof ParamXml) {
					ParamXml px = (ParamXml) el;
					insertParam(modelId, px.origName, PARAMTYPE_PARAM, px.min, px.max, px.category, px.unit, px.description);
				}
			}
		}

		insertModLit(modelId, m.getModelLit(), false, m);

		return modelId;
	}

	private void insertModLit(final int modelId, PmmXmlDoc modelLit, final boolean estimatedModels, final ParametricModel m) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM "
					+ (estimatedModels ? "\"GeschaetztesModell_Referenz\" WHERE \"GeschaetztesModell\"" : "\"Modell_Referenz\"WHERE \"Modell\"") + " = " + modelId);
			ps.executeUpdate();
			ps.close();
			PreparedStatement psm = conn.prepareStatement("INSERT INTO \"Modell_Referenz\" (\"Modell\", \"Literatur\") VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			PreparedStatement psgm = conn.prepareStatement("INSERT INTO \"GeschaetztesModell_Referenz\" (\"GeschaetztesModell\", \"Literatur\") VALUES (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			insertLiteratureInCase(modelLit);
			if (estimatedModels) m.setEstLit(modelLit);
			else m.setMLit(modelLit);
			for (PmmXmlElementConvertable el : modelLit.getElementSet()) {
				if (el instanceof LiteratureItem) {
					LiteratureItem li = (LiteratureItem) el;
					if (li.id >= 0) {
						if (!estimatedModels) {
							psm.setInt(1, modelId);
							psm.setInt(2, li.id);
							psm.executeUpdate();
						} else {
							psgm.setInt(1, modelId);
							psgm.setInt(2, li.id);
							psgm.executeUpdate();
						}
					}
				}
			}
			psm.close();
			psgm.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void insertCondComb(final Integer resultID, final String combaseId) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"ImportedCombaseData\" (\"CombaseID\", \"Versuchsbedingung\")VALUES(?,?)");
			ps.setString(1, combaseId);
			ps.setInt(2, resultID);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
		} //  ex.printStackTrace(); 
	}

	@SuppressWarnings("unused")
	private Integer combaseDataAlreadyIn(final String combaseId) {
		Integer res = null;
		try {
			ResultSet result = getResultSet("SELECT \"Versuchsbedingung\" FROM \"ImportedCombaseData\" WHERE \"CombaseID\" LIKE '" + combaseId
					+ "' AND \"Versuchsbedingung\" IS NOT NULL", false);

			if (result != null && result.first()) {
				res = result.getInt(1);
				result.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return res;
	}

	private int insertDouble(final Double value) {
		return insertDouble(value, null, null);
	}

	private int insertDouble(final Double value, final Double stdDev, final Integer numValues) {
		if (value == null || Double.isNaN(value) || Double.isInfinite(value)) return -1;
		PreparedStatement psInsertDouble;
		ResultSet result;

		int doubleId = -1;
		try {
			if (stdDev != null) {
				psInsertDouble = conn.prepareStatement("INSERT INTO \"DoubleKennzahlen\" (\"" + ATT_VALUE + "\",\"Standardabweichung\",\"Wiederholungen\", \"" + ATT_VALUETYPE
						+ "\" )VALUES(?, ?, ?, 2)", Statement.RETURN_GENERATED_KEYS);
			} else {
				psInsertDouble = conn.prepareStatement("INSERT INTO \"DoubleKennzahlen\" (\"" + ATT_VALUE + "\", \"" + ATT_VALUETYPE + "\" )VALUES(?, 1)",
						Statement.RETURN_GENERATED_KEYS);
			}
			psInsertDouble.setDouble(1, value);
			if (stdDev != null) {
				psInsertDouble.setDouble(2, stdDev);
				if (numValues == null) psInsertDouble.setNull(3, java.sql.Types.INTEGER);
				else psInsertDouble.setInt(3, numValues);
			}
			if (psInsertDouble.executeUpdate() < 1) return -1;

			result = psInsertDouble.getGeneratedKeys();
			result.next();
			doubleId = result.getInt(1);

			result.close();
			psInsertDouble.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return doubleId;
	}

	private Integer queryAgentId(final String agentName) {

		Integer agentId = null;
		PreparedStatement psQueryAgentId;
		ResultSet result;

		try {
			psQueryAgentId = conn.prepareStatement("SELECT \"ID\" FROM \"" + REL_AGENT + "\" WHERE \"" + ATT_AGENTNAME + "\" LIKE ? OR \"" + ATT_NAMESHORT + "\" LIKE ?");
			psQueryAgentId.setString(1, agentName);
			psQueryAgentId.setString(2, agentName);

			result = psQueryAgentId.executeQuery();

			if (!result.next()) {
				return agentId;
			}

			agentId = result.getInt(1);

			result.close();
			psQueryAgentId.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return agentId;
	}

	private Integer queryMatrixId(final String matrixName) {

		Integer matrixId = null;

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT \"ID\" FROM \"" + REL_MATRIX + "\" WHERE \"" + ATT_MATRIXNAME + "\" LIKE ?");
			ps.setString(1, matrixName);

			ResultSet result = ps.executeQuery();

			if (!result.next()) {
				return matrixId;
			}

			matrixId = result.getInt(1);

			result.close();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return matrixId;
	}

	private ResultSet getResultSet(final String sql, final boolean suppressWarnings) {
		ResultSet ergebnis = null;
		try {
			Statement anfrage = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ergebnis = anfrage.executeQuery(sql);
			ergebnis.first();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ergebnis;
	}

	private Integer getID(final String tablename, final String feldname, final String feldVal) {
		Integer result = null;
		String sql = "SELECT \"ID\" FROM \"" + tablename + "\" WHERE \"" + feldname + "\"";
		if (feldVal == null) {
			sql += " IS NULL";
		} else {
			sql += " = '" + feldVal.replace("'", "''") + "'";
		}
		ResultSet rs = getResultSet(sql, true);
		try {
			if (rs != null && rs.last()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void insertData(final int condId, final int timeId, final int lognId, String timeUnit, String concUnit, String concUnitObjectType) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"Messwerte\" (\"" + REL_CONDITION + "\", \"" + ATT_TIME + "\", \"ZeitEinheit\", \"" + ATT_LOG10N
					+ "\", \"Konz_Einheit\" )VALUES(?, ?, ?, ?, ?)");
			ps.setInt(1, condId);
			if (timeId >= 0) {
				ps.setInt(2, timeId);
			} else {
				ps.setNull(2, Types.INTEGER);
			}

			if (timeUnit != null && !timeUnit.isEmpty()) {
				Integer tid = DBKernel.getID("Einheiten", new String[] { "display in GUI as" }, new String[] { timeUnit });
				if (tid != null) ps.setInt(3, tid);
				else ps.setNull(3, Types.INTEGER);
			} else {
				ps.setNull(3, Types.INTEGER);
			}

			if (lognId >= 0) {
				ps.setInt(4, lognId);
			} else {
				ps.setNull(4, Types.INTEGER);
			}

			if (concUnit != null && !concUnit.isEmpty()) {
				Integer cid = null;
				if (concUnitObjectType == null || concUnitObjectType.trim().isEmpty()) cid = DBKernel.getID("Einheiten", new String[] { "display in GUI as" },
						new String[] { concUnit });
				else {
					cid = DBKernel.getID("Einheiten", new String[] { "display in GUI as", "object type" }, new String[] { concUnit, concUnitObjectType });
					if (cid == null) {
						cid = DBKernel.getID("Einheiten", new String[] { "display in GUI as" }, new String[] { concUnit });
					}
				}
				if (cid != null) ps.setInt(5, cid);
				else ps.setNull(5, Types.INTEGER);
			} else {
				ps.setNull(5, Types.INTEGER);
			}

			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private int insertParam(final int modelId, final String paramName, final int paramType, final Double min, final Double max, final String category, final String unit,
			final String description) {
		PreparedStatement ps;

		int id = -1;
		try {
			int paramId = queryParamId(modelId, paramName, paramType);
			if (paramId <= 0) {
				ps = conn.prepareStatement(
						"INSERT INTO \"ModellkatalogParameter\" ( \"Modell\", \"Parametername\", \"Parametertyp\", \"min\",\"max\",\"Einheit\",\"Beschreibung\" ) VALUES( ?, ?, ?, ?, ?, ?, ? )",
						Statement.RETURN_GENERATED_KEYS);
			} else {
				ps = conn.prepareStatement(
						"UPDATE \"ModellkatalogParameter\" SET \"Modell\" = ?, \"Parametername\" = ?, \"Parametertyp\" = ?, \"min\"= ?, \"max\" = ?, \"Einheit\" = ?, \"Beschreibung\" = ? WHERE \"ID\"="
								+ paramId, Statement.RETURN_GENERATED_KEYS);
			}

			ps.setInt(1, modelId);
			ps.setString(2, paramName);
			ps.setInt(3, paramType);
			if (min == null || paramType != PARAMTYPE_PARAM) {
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(4, min);
			}
			if (max == null || paramType != PARAMTYPE_PARAM) {
				ps.setNull(5, Types.DOUBLE);
			} else {
				ps.setDouble(5, max);
			}
			/*
			 * if (category == null) { ps.setNull(6, Types.VARCHAR); } else {
			 * ps.setString(6, category); }
			 */
			Integer unitID = unit == null || unit.isEmpty() ? null : DBKernel.getID("Einheiten", "display in GUI as", unit);
			if (unit == null || unitID == null) {
				ps.setNull(6, Types.INTEGER);
			} else {
				ps.setInt(6, unitID);
			}
			if (description == null) {
				ps.setNull(7, Types.VARCHAR);
			} else {
				ps.setString(7, description);
			}

			if (ps.executeUpdate() < 1) {
				return id;
			}
			if (paramId > 0) {
				return paramId;
			}

			ResultSet result = ps.getGeneratedKeys();

			if (!result.next()) {
				return id;
			}

			id = result.getInt(1);

			result.close();
			ps.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return id;
	}

	private Integer getId4Formula(final String formula, int level, int modelId) {
		Integer o = (Integer) DBKernel.getValue(conn, "Modellkatalog", new String[]{"Formel","Level","ID"}, new String[]{formula,""+level,""+modelId}, "ID");
		if (o == null) o = (Integer) DBKernel.getValue(conn, "Modellkatalog", new String[]{"Formel","Level"}, new String[]{formula,""+level}, "ID");
		return o;
	}

	private boolean isObjectPresent(final String tablename, final int id) {
		if (id <= 0) return false;

		int cnt = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT COUNT( * )FROM \"" + tablename + "\" WHERE \"ID\"=?");
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			result.next();
			cnt = result.getInt(1);
			result.close();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		if (cnt > 0) return true;

		return false;
	}

	private void insertEstParam(final int estModelId, final int paramId, final Double value, final Double paramErr, String unit, ParametricModel pm, boolean isDepIndep,
			String paramName) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"GeschaetzteParameter\" (\"GeschaetztesModell\", \"Parameter\", \"Wert\", \"StandardError\", \"Einheit\", \"p\", \"t\") VALUES(?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, estModelId);
			ps.setInt(2, paramId);
			if (value == null || Double.isNaN(value)) {
				ps.setNull(3, Types.DOUBLE);
			} else {
				ps.setDouble(3, value);
			}
			if (paramErr == null || Double.isNaN(paramErr)) {
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(4, paramErr);
			}
			Object unitID = unit == null || unit.isEmpty() ? null : DBKernel.getID("Einheiten", new String[] { "display in GUI as" }, new String[] { unit });
			if (unitID == null) {
				if (isDepIndep) {
					pm.warning = pm.warning + "\nUnit not defined for variable '" + paramName + "' in fitted model with ID " + estModelId + "!";
				}
				ps.setNull(5, Types.INTEGER);
			} else ps.setInt(5, (Integer) unitID);
			Double P = pm.getParamP(paramName);
			if (P == null || Double.isNaN(P)) {
				ps.setNull(6, Types.DOUBLE);
			} else {
				ps.setDouble(6, P);
			}
			Double t = pm.getParamT(paramName);
			if (t == null || Double.isNaN(t)) {
				ps.setNull(7, Types.DOUBLE);
			} else {
				ps.setDouble(7, t);
			}
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void insertEstParamCorrs(int modelId, int estModelId, final int paramId, HashMap<String, Double> hm) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO \"GeschaetzteParameterCovCor\" (\"param1\", \"param2\", \"GeschaetztesModell\", \"cor\", \"Wert\") VALUES(?, ?, ?, ?, ?)");
			Integer estParamId = DBKernel.getID("GeschaetzteParameter", new String[] { "GeschaetztesModell", "Parameter" }, new String[] { estModelId + "", paramId + "" });
			ps.setInt(1, estParamId);
			ps.setInt(3, estModelId);
			ps.setBoolean(4, true);

			for (String param2 : hm.keySet()) {
				int param2Id = queryParamId(modelId, param2, PARAMTYPE_PARAM);
				if (param2Id < 0) {
					System.err.println("paramId < 0... " + param2);
				} else {
					Integer estParam2Id = DBKernel.getID("GeschaetzteParameter", new String[] { "GeschaetztesModell", "Parameter" },
							new String[] { estModelId + "", param2Id + "" });
					ps.setInt(2, estParam2Id);
					Double value = hm.get(param2);
					if (value == null || Double.isNaN(value)) {
						ps.setNull(5, Types.DOUBLE);
					} else {
						ps.setDouble(5, value);
					}
					ps.executeUpdate();
				}
			}
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private int queryParamId(final int modelId, final String paramName, final int paramType) {
		int ret = -1;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT \"ID\" FROM \"ModellkatalogParameter\"  WHERE \"" + ATT_MODELID + "\"=? AND \"" + ATT_PARAMNAME
					+ "\" LIKE ? AND \"" + ATT_PARAMTYPE + "\"=?");
			ps.setInt(1, modelId);
			ps.setString(2, paramName);
			ps.setInt(3, paramType);

			//System.out.println( ps );

			ResultSet result = ps.executeQuery();
			if (!result.next()) {
				return ret;
			}
			ret = result.getInt(1);

			result.close();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return ret;
	}

	private void updateEstModel(final int estModelId, String name, final int condId, final int modelId, final double rms, final double rsquared, final double aic,
			final double bic, final int responseId, Integer qualityScore, Boolean isChecked, Integer workflowID, String comment) {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE \"GeschaetzteModelle\" SET \"Name\"=?, \"Versuchsbedingung\"=?, \"Modell\"=?, \"RMS\"=?, \"Rsquared\"=?, \"AIC\"=?, \"BIC\"=?, \"Response\"=?, \"Guetescore\"=?, \"Geprueft\"=?, \"Kommentar\"=?, \"PMMLabWF\"=? WHERE \"ID\"=?");
			if (name == null) {
				ps.setNull(1, Types.VARCHAR);
			} else {
				ps.setString(1, name);
			}
			if (condId > 0) {
				ps.setInt(2, condId);
			} else {
				ps.setNull(2, Types.INTEGER);
			}
			ps.setInt(3, modelId);
			if (Double.isNaN(rms)) {
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(4, rms);
			}
			if (Double.isNaN(rsquared)) {
				ps.setNull(5, Types.DOUBLE);
			} else {
				ps.setDouble(5, rsquared);
			}
			if (Double.isNaN(aic)) {
				ps.setNull(6, Types.DOUBLE);
			} else {
				ps.setDouble(6, aic);
			}
			if (Double.isNaN(bic)) {
				ps.setNull(7, Types.DOUBLE);
			} else {
				ps.setDouble(7, bic);
			}
			if (responseId > 0) {
				ps.setInt(8, responseId);
			} else {
				ps.setNull(8, Types.INTEGER);
			}
			if (qualityScore == null) {
				ps.setNull(9, Types.INTEGER);
			} else {
				ps.setInt(9, qualityScore);
			}
			if (isChecked == null) {
				ps.setNull(10, Types.BOOLEAN);
			} else {
				ps.setBoolean(10, isChecked);
			}
			if (comment == null) {
				ps.setNull(11, Types.VARCHAR);
			} else {
				ps.setString(11, comment);
			}
			if (workflowID != null && workflowID > 0) {
				ps.setInt(12, workflowID);
			} else {
				ps.setNull(12, Types.INTEGER);
			}
			ps.setInt(13, estModelId);

			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private int insertEstModel(String name, final int condId, final int modelId, final Double rms, final Double rsquared, final Double aic, final Double bic, final int responseId,
			Integer qualityScore, Boolean isChecked, Integer workflowID, String comment) {
		int ret = -1;
		try {
			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO \"GeschaetzteModelle\" (\"Name\", \"Versuchsbedingung\", \"Modell\", \"RMS\", \"Rsquared\", \"AIC\", \"BIC\", \"Response\", \"Guetescore\", \"Geprueft\", \"Kommentar\", \"PMMLabWF\") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			if (name == null) {
				ps.setNull(1, Types.VARCHAR);
			} else {
				ps.setString(1, name);
			}
			if (condId > 0) {
				ps.setInt(2, condId);
			} else {
				ps.setNull(2, Types.INTEGER);
			}
			ps.setInt(3, modelId);
			if (rms == null || Double.isNaN(rms)) {
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(4, rms);
			}
			if (rsquared == null || Double.isNaN(rsquared)) {
				ps.setNull(5, Types.DOUBLE);
			} else {
				ps.setDouble(5, rsquared);
			}
			if (aic == null || Double.isNaN(aic)) {
				ps.setNull(6, Types.DOUBLE);
			} else {
				ps.setDouble(6, aic);
			}
			if (bic == null || Double.isNaN(bic)) {
				ps.setNull(7, Types.DOUBLE);
			} else {
				ps.setDouble(7, bic);
			}
			if (responseId > 0) {
				ps.setInt(8, responseId);
			} else {
				ps.setNull(8, Types.INTEGER);
			}
			if (qualityScore == null) {
				ps.setNull(9, Types.INTEGER);
			} else {
				ps.setInt(9, qualityScore);
			}
			if (isChecked == null) {
				ps.setNull(10, Types.BOOLEAN);
			} else {
				ps.setBoolean(10, isChecked);
			}
			if (comment == null) {
				ps.setNull(11, Types.VARCHAR);
			} else {
				ps.setString(11, comment);
			}
			if (workflowID != null && workflowID > 0) {
				ps.setInt(12, workflowID);
			} else {
				ps.setNull(12, Types.INTEGER);
			}

			ps.executeUpdate();
			ResultSet result = ps.getGeneratedKeys();
			result.next();
			ret = result.getInt(1);

			result.close();
			ps.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return ret;
	}

	private PmmXmlDoc getLiterature(String s, String dbuuid) {
		PmmXmlDoc l = new PmmXmlDoc();
		String[] ids = s.split(",");
		for (String id : ids) {
			LiteratureItem li = DBUtilities.getLiteratureItem(conn, Integer.valueOf(id), dbuuid);
			l.add(li);
		}
		return l;
	}

}
