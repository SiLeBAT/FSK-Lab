package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

public class MiscListTest {
	
	static Misc misc;
	
	static {
		misc = new Misc();
		misc.setId(MiscTest.id);
		misc.setName(MiscTest.name);
		misc.setDescription(MiscTest.description);
		misc.setValue(MiscTest.value);
		misc.setCategories(MiscTest.categories);
		misc.setUnit(MiscTest.unit);
		misc.setOrigUnit(MiscTest.origUnit);
		misc.setDbuuid(MiscTest.dbuuid);
	}

	@Test
	public void testMiscs() {
		MiscList list = new MiscList();
		assertNull(list.getMiscs());

		list.setMiscs(new Misc[] { misc });

		Misc expected = misc;  // expected Misc
		Misc obtained = list.getMiscs()[0];  // obtained Misc

		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertEquals(expected.getValue(), obtained.getValue());
		assertArrayEquals(expected.getCategories(), obtained.getCategories());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getOrigUnit(), obtained.getOrigUnit());
		assertEquals(expected.getDbuuid(), obtained.getDbuuid());
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		MiscList list = new MiscList();
		list.setMiscs(new Misc[] { misc });

		NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt(MiscList.NUM_MISCS));

		Misc expected = misc;  // expected Misc
		Misc obtained = new Misc();  // obtained Misc
		obtained.loadFromNodeSettings(settings.getNodeSettings(MiscList.MISCS + 0));

		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertEquals(expected.getValue(), obtained.getValue());
		assertArrayEquals(expected.getCategories(), obtained.getCategories());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getOrigUnit(), obtained.getOrigUnit());
		assertEquals(expected.getDbuuid(), obtained.getDbuuid());
	}

	@Test
	public void testLoadFromNodeSettings() {
		NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt(MiscList.NUM_MISCS, 1);
		misc.saveToNodeSettings(settings.addNodeSettings(MiscList.MISCS + 0));

		MiscList list = new MiscList();
		list.loadFromNodeSettings(settings);

		Misc expected = misc;  // expected Misc
		Misc obtained = list.getMiscs()[0];  // obtained Misc

		assertEquals(expected.getId(), obtained.getId());
		assertEquals(expected.getName(), obtained.getName());
		assertEquals(expected.getDescription(), obtained.getDescription());
		assertEquals(expected.getValue(), obtained.getValue());
		assertArrayEquals(expected.getCategories(), obtained.getCategories());
		assertEquals(expected.getUnit(), obtained.getUnit());
		assertEquals(expected.getOrigUnit(), obtained.getOrigUnit());
		assertEquals(expected.getDbuuid(), obtained.getDbuuid());
	}
}
