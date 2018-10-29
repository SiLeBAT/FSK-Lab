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
package de.bund.bfr.knime.pmm.common;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

public class DbIo {

	public static String stripNonValidXMLCharacters(String in) {
	      StringBuffer out = new StringBuffer(); // Used to hold the output.
	      char current; // Used to reference the current character.

	      if (in == null || ("".equals(in))) return ""; // vacancy test.
	      for (int i = 0; i < in.length(); i++) {
	          current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
	          if ((current == 0x9) ||
	              (current == 0xA) ||
	              (current == 0xD) ||
	              ((current >= 0x20) && (current <= 0xD7FF)) ||
	              ((current >= 0xE000) && (current <= 0xFFFD)) ||
	              ((current >= 0x10000) && (current <= 0x10FFFF)))
	              out.append(current);
	      }
	      return out.toString();
	} 
	public static String convertArray2String(Array array) {
    	String result = null;
	    if (array != null) {
		    try {
				Object[] o = (Object[])array.getArray();
				if (o != null && o.length > 0) {
					result = convertO(o[0]);
					for (int i=1;i<o.length;i++) {
						result += "," + convertO(o[i]);
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
    	return result;
    }
    public static PmmXmlDoc convertStringLists2TSXmlDoc(Array t, Array tu, Array l, Array lu, Array lot, Array stddevs, Array wdhs, String modelUnit, String timeUnit) {
		PmmXmlDoc tsDoc = new PmmXmlDoc();
		if (t != null) {
			try {
				Object[] toksT = (Object[])t.getArray();
				Object[] toksTu = (tu == null) ? null : (Object[])tu.getArray();
				Object[] toksL = (l == null) ? null : (Object[])l.getArray();
				Object[] toksLu = (lu == null) ? null : (Object[])lu.getArray();
				Object[] toksLot = (lot == null) ? null : (Object[])lot.getArray();
				Object[] toksSd = (stddevs == null) ? null : (Object[])stddevs.getArray();
				Object[] toksWdh = (wdhs == null) ? null : (Object[])wdhs.getArray();
				checkUnits(toksT, toksLu);
				if (toksT.length > 0) {
					Category modelUnitCat = modelUnit == null ? null : Categories.getCategoryByUnit(modelUnit);
					Category timeUnitCat = timeUnit == null ? null : Categories.getCategoryByUnit(timeUnit);
					int i=0;
					for (Object time : toksT) {
						try {
							String toksLui = toksLu == null || toksLu[i] == null ? null : toksLu[i].toString();
							String toksTui = toksTu == null || toksTu[i] == null ? null : toksTu[i].toString();
							TimeSeriesXml tsx = new TimeSeriesXml("t"+i,
									time == null ? null : convert(Double.parseDouble(time.toString()), toksTui, timeUnit, timeUnitCat),
											timeUnit != null && toksTui != null ? timeUnit : toksTui,
											toksTui,
											toksL == null || toksL[i] == null ? null : convert(Double.parseDouble(toksL[i].toString()), toksLui, modelUnit, modelUnitCat),
											modelUnit != null && toksLui != null ? modelUnit : toksLui,
											null,
											toksLui,
											toksSd == null || toksSd[i] == null ? null : convert(Double.parseDouble(toksSd[i].toString()), toksLui, modelUnit, modelUnitCat),
											toksWdh == null || toksWdh[i] == null ? null : (int) Double.parseDouble(toksWdh[i].toString()));
							if (toksLot != null && toksLot[i] != null) tsx.concentrationUnitObjectType = toksLot[i].toString();
							tsDoc.add(tsx);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						i++;
					}
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tsDoc;    	
    }
    private static void checkUnits(Object[] toksT, Object[] toksLu) {
    	try {
        	if (toksLu != null && toksLu.length > 0) {
        		String unit = toksLu[0].toString();
        		int i=1;
        		for (;i<toksLu.length;i++) {
        			if (!toksLu[i].toString().equals(unit)) break;
        		}
        		if (i < toksLu.length) {
        			String theUnit = "";
        			for (i=0;i<toksLu.length;i++) {
        				if (Double.parseDouble(toksT[i].toString()) == 0.0) {
        					theUnit = toksLu[i].toString();
        					break;
        				}
        			}
        			if (i < toksLu.length) {
        				for (int j=0;j<toksLu.length;j++) {
        					toksLu[j] = theUnit;
            			}
        			}
        		}
        	}
    	}
    	catch (Exception e) {e.printStackTrace();}
    }
    private static double convert(double value, String unit, String newUnit, Category newUnitCat) {
		if (newUnit != null && unit != null && !newUnit.equals(unit)) {
			
			try {
				return newUnitCat.convert(value, unit, newUnit);
			}
			catch (ConvertException e) {
				e.printStackTrace();
			}    					
		}
    	return value;
    }
    public static PmmXmlDoc convertArrays2ParamXmlDoc(LinkedHashMap<String, String> varMap, Array name,
    		Array value, Array timeUnit, Array categories, Array units, Array error, Array min, Array max, Array desc, Array paramType, Array P, Array t, Integer modelId, Integer emid) {
		PmmXmlDoc paramDoc = new PmmXmlDoc();
	    if (name != null) {
		    try {
				Object[] na = (Object[])name.getArray();
				Object[] va = (value == null) ? null : (Object[])value.getArray();
				//Object[] tu = (timeUnit == null) ? null : (Object[])timeUnit.getArray();
				Object[] cu = (units == null) ? null : (Object[])units.getArray();
				Object[] cc = (categories == null) ? (cu == null ? null : new Object[cu.length]) : (Object[])categories.getArray();
				Object[] er = (error == null) ? null : (Object[])error.getArray();
				Object[] mi = (Object[])min.getArray();
				Object[] ma = (Object[])max.getArray();
				Object[] cd = (Object[])desc.getArray();
				Object[] cp = (P == null) ? null : (Object[])P.getArray();
				Object[] ct = (t == null) ? null : (Object[])t.getArray();
				Object[] pta = (paramType == null) ? null : (Object[])paramType.getArray();
				if (na != null && na.length > 0) {
					for (int i=0;i<na.length;i++) {
						String nas = na[i].toString();
						Double vad = (va == null || va[i] == null) ? Double.NaN : Double.parseDouble(va[i].toString());
						/*
						if (!Double.isNaN(vad)) {
							if (tu != null && tu[i] != null) {
								if (tu[i].toString().equalsIgnoreCase("Sekunde")) vad = vad / 3600;
								else if (tu[i].toString().equalsIgnoreCase("Minute")) vad = vad / 60;
								else if (tu[i].toString().equalsIgnoreCase("Stunde")) ;
								else if (tu[i].toString().equalsIgnoreCase("Tag")) vad = vad * 24;
								else if (tu[i].toString().equalsIgnoreCase("Woche")) vad = vad * 24 * 7;
								else if (tu[i].toString().equalsIgnoreCase("Monat")) vad = vad * 24 * 30;
								else if (tu[i].toString().equalsIgnoreCase("Jahr")) vad = vad * 24 * 365;
								else System.err.println("convertArrays2ParamXmlDoc - Unconsidered Time Unit used... Please Check!!!! ->" + tu[i]);
							}
							if (cu != null && cu[i] != null) {
								System.err.println("convertArrays2ParamXmlDoc - Unconsidered concentration Unit used... Please Check!!!! ->" + cu[i]);
							}
						}
						*/
						Double erd = (er == null || er[i] == null) ? Double.NaN : Double.parseDouble(er[i].toString());
						Double mid = (mi[i] == null) ? Double.NaN : Double.parseDouble(mi[i].toString());
						Double mad = (ma[i] == null) ? Double.NaN : Double.parseDouble(ma[i].toString());
						String onas = nas;
			    		if (varMap != null && varMap.containsKey(nas)) onas = varMap.get(nas);
			    		if (cc != null && cc[i] == null && cu[i] != null) {
			    			cc[i] = DBKernel.getValue("Einheiten", "display in GUI as", (String) cu[i], "kind of property / quantity");
			    		}
			    		Boolean ptab = pta == null || pta[i] == null ? null : ((Integer) pta[i]).intValue() == 4;
						ParamXml px = new ParamXml(onas,ptab,vad,erd,mid,mad,null,null,cc==null?null:(String) cc[i],cu==null?null:(String) cu[i]);
						px.name = nas;
						if (cd != null && cd[i] != null) px.description = stripNonValidXMLCharacters(cd[i].toString());
						if (cp != null && cp[i] != null) px.P = Double.parseDouble(cp[i].toString());
						if (ct != null && ct[i] != null) px.t = Double.parseDouble(ct[i].toString());
						if (emid != null) px = addCorrs(px, modelId, emid);
						paramDoc.add(px);
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return paramDoc;
    }
    private static ParamXml addCorrs(ParamXml px, int modelId, int emid) throws SQLException {
		Integer paramId = DBKernel.getID("ModellkatalogParameter", new String[]{"Modell", "Parametername", "Parametertyp"}, new String[]{modelId+"", px.origName, "2"}); // Bfrdb.PARAMTYPE_PARAM
		if (paramId == null) System.err.println("paramId = null... " + px.origName);
		Integer estParamId = DBKernel.getID("GeschaetzteParameter", new String[]{"GeschaetztesModell", "Parameter"}, new String[]{emid+"", paramId+""});
    	ResultSet rs = DBKernel.getResultSet("SELECT \"param2\",\"Wert\" FROM \"GeschaetzteParameterCovCor\" WHERE \"param1\" = " + estParamId + " AND \"GeschaetztesModell\" = " + emid, false);
    	if (rs != null && rs.first()) {
    		do {
    			int estParam2Id = rs.getInt("param2");
    			Object param2 = DBKernel.getValue(null,"GeschaetzteParameter", new String[]{"GeschaetztesModell", "ID"}, new String[]{emid+"", estParam2Id+""}, "Parameter");
    			Object o = DBKernel.getValue(null,"ModellkatalogParameter", new String[]{"Modell", "ID", "Parametertyp"}, new String[]{modelId+"", param2+"", "2"}, "Parametername");
    	    	if (o != null && rs.getObject("Wert") != null) px.correlations.put(o.toString(), rs.getDouble("Wert"));
    		} while(rs.next());
    	}
    	return px;
    }
    public static PmmXmlDoc convertArrays2IndepXmlDoc(LinkedHashMap<String, String> varMap, Array name, Array min, Array max, Array categories, Array units, Array desc, boolean isPrimary) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();
	    if (name != null) {
		    try {
				Object[] na = (Object[])name.getArray();
				Object[] mi = (min == null) ? null : (Object[])min.getArray();
				Object[] ma = (max == null) ? null : (Object[])max.getArray();
				Object[] cc = (categories == null) ? null : (Object[])categories.getArray();
				Object[] cu = (units == null) ? null : (Object[])units.getArray();
				Object[] cd = (desc == null) ? null : (Object[])desc.getArray();
				if (na != null && na.length > 0) {
					for (int i=0;i<na.length;i++) {
						String nas = na[i].toString();
						Double mid = (mi == null || mi[i] == null) ? Double.NaN : Double.parseDouble(mi[i].toString());
						Double mad = (ma == null || ma[i] == null) ? Double.NaN : Double.parseDouble(ma[i].toString());
						if (isPrimary && varMap != null && !varMap.containsKey(AttributeUtilities.TIME) && nas.equals("t")) nas = AttributeUtilities.TIME;
						String onas = nas;
			    		if (varMap != null && varMap.containsKey(nas)) onas = varMap.get(nas);
						IndepXml ix = new IndepXml(onas,mid,mad,cc==null?null:(String) cc[i],cu==null?null:(String) cu[i]);
						ix.name = nas;
						if (cd != null && cd[i] != null) ix.description = stripNonValidXMLCharacters(cd[i].toString());
						indepDoc.add(ix);
						if (ix.unit == null || ix.unit.isEmpty()) {
							indepDoc.addWarning("\nUnit not defined for independant variable '" + ix.name + "'\n");
						}
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return indepDoc;
    }
    public static PmmXmlDoc convertArrays2MiscXmlDoc(Array id, Array param, Array desc, Array value, Array unit, String dbuuid) {
		PmmXmlDoc miscDoc = new PmmXmlDoc();
	    if (id != null) {
		    try {
				Object[] sid = (Object[])id.getArray();
				Object[] spa = (param == null) ? null : (Object[])param.getArray();
				Object[] sde = (desc == null) ? null : (Object[])desc.getArray();
				Object[] sv = (value == null) ? null : (Object[])value.getArray();
				Object[] su = (unit == null) ? null : (Object[])unit.getArray();
				if (sid != null && sid.length > 0) {
					for (int i=0;i<sid.length;i++) {
						Integer sidi = (sid[i] == null ? null : Integer.parseInt(sid[i].toString()));
						String spas = (spa == null || spa[i] == null ? null : spa[i].toString());
						String sdes = (sde == null || sde[i] == null ? null : sde[i].toString());
						Double svd = (sv == null || sv[i] == null) ? Double.NaN : Double.parseDouble(sv[i].toString());
						String sus = (su == null || su[i] == null ? null : su[i].toString());
			    		MiscXml mx = new MiscXml(sidi,spas,sdes,svd,null,sus,dbuuid);
						miscDoc.add(mx);
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
		return miscDoc;
    }
    
    private static String convertO(Object o) {
		if (o == null) {
			return "?";
		} else {
			return o.toString();
		}
    }

    public static LinkedHashMap<String, String> getVarParMap(String varparStr) {
    	LinkedHashMap<String, String> ret = new LinkedHashMap<>();
    	if (varparStr != null) {
    		String[] t1 = varparStr.split(",");

    		for (String map : t1) {
    			String[] t2 = map.split("=");
    			if (t2.length == 2) ret.put(t2[0], t2[1]);
    		}
    	}
		return ret;    	
    }
}
