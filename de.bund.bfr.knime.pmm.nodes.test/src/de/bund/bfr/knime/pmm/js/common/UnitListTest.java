package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class UnitListTest {
	
	static Unit unit;
	
	static {
		unit = new Unit();
		unit.setId(UnitTest.id);
		unit.setUnit(UnitTest.unitString);
		unit.setDescription(UnitTest.description);
		unit.setName(UnitTest.name);
		unit.setKindOfPropertyQuantity(UnitTest.kindOfPropertyQuantity);
		unit.setNotationCaseSensitive(UnitTest.notationCaseSensitive);
		unit.setConvertTo(UnitTest.convertTo);
		unit.setConversionFunctionFactor(UnitTest.conversionFunctionFactor);
		unit.setInverseConversionFunctionFactor(UnitTest.inversionConversionFunctionFactor);
		unit.setObjectType(UnitTest.objectType);
		unit.setDisplayInGuiAs(UnitTest.displayInGuiAs);
		unit.setMathMLString(UnitTest.mathMlString);
		unit.setPriorityForDisplayInGui(UnitTest.priorityForDisplayInGui);
	}

	@Test
	public void testUnits() {
		UnitList list = new UnitList();
		assertNull(list.getUnits());
		
		list.setUnits(new Unit[] { unit });
		
		Unit expected = unit;  // expected Unit
		Unit obtained = list.getUnits()[0];  // obtained Unit
		
		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getKindOfPropertyQuantity(), obtained.getKindOfPropertyQuantity());
		assertEquals(expected.getNotationCaseSensitive(), obtained.getNotationCaseSensitive());
		assertEquals(expected.getConvertTo(), obtained.getConvertTo());
		assertEquals(expected.getConversionFunctionFactor(), obtained.getConversionFunctionFactor());
		assertEquals(expected.getInverseConversionFunctionFactor(), obtained.getInverseConversionFunctionFactor());
		assertEquals(expected.getObjectType(), obtained.getObjectType());
		assertEquals(expected.getDisplayInGuiAs(), obtained.getDisplayInGuiAs());
		assertEquals(expected.getMathMLString(), obtained.getMathMLString());
		assertEquals(expected.getPriorityForDisplayInGui(), obtained.getPriorityForDisplayInGui());
	}
	
	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		UnitList list = new UnitList();
		list.setUnits(new Unit[] { unit });
		
		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);
		
		assertEquals(1, settings.getInt(UnitList.NUM_UNITS));
		
		Unit expected = unit;  // expected unit
		Unit obtained = new Unit();  // obtained unit
		obtained.loadFromNodeSettings(settings.getNodeSettings(UnitList.UNITS + 0));
		
		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getKindOfPropertyQuantity(), obtained.getKindOfPropertyQuantity());
		assertEquals(expected.getNotationCaseSensitive(), obtained.getNotationCaseSensitive());
		assertEquals(expected.getConvertTo(), obtained.getConvertTo());
		assertEquals(expected.getConversionFunctionFactor(), obtained.getConversionFunctionFactor());
		assertEquals(expected.getInverseConversionFunctionFactor(), obtained.getInverseConversionFunctionFactor());
		assertEquals(expected.getObjectType(), obtained.getObjectType());
		assertEquals(expected.getDisplayInGuiAs(), obtained.getDisplayInGuiAs());
		assertEquals(expected.getMathMLString(), obtained.getMathMLString());
		assertEquals(expected.getPriorityForDisplayInGui(), obtained.getPriorityForDisplayInGui());
	}
	
	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(UnitList.NUM_UNITS, 1);
		unit.saveToNodeSettings(settings.addNodeSettings(UnitList.UNITS + 0));
		
		UnitList list = new UnitList();
		list.loadFromNodeSettings(settings);
		
		Unit expected = unit;  // expected Unit
		Unit obtained = list.getUnits()[0];  // obtained Unit
		
		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getKindOfPropertyQuantity(), obtained.getKindOfPropertyQuantity());
		assertEquals(expected.getNotationCaseSensitive(), obtained.getNotationCaseSensitive());
		assertEquals(expected.getConvertTo(), obtained.getConvertTo());
		assertEquals(expected.getConversionFunctionFactor(), obtained.getConversionFunctionFactor());
		assertEquals(expected.getInverseConversionFunctionFactor(), obtained.getInverseConversionFunctionFactor());
		assertEquals(expected.getObjectType(), obtained.getObjectType());
		assertEquals(expected.getDisplayInGuiAs(), obtained.getDisplayInGuiAs());
		assertEquals(expected.getMathMLString(), obtained.getMathMLString());
		assertEquals(expected.getPriorityForDisplayInGui(), obtained.getPriorityForDisplayInGui());
	}
}
