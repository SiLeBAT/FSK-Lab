package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
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
		final MiscList list = new MiscList();
		assertNull(list.getMiscs());

		list.setMiscs(new Misc[] { misc });

		final Misc expected = misc;  // expected Misc
		final Misc obtained = list.getMiscs()[0];  // obtained Misc

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
		final MiscList list = new MiscList();
		list.setMiscs(new Misc[] { misc });

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numMiscs"));

		final Misc expected = misc;  // expected Misc
		final Misc obtained = new Misc();  // obtained Misc
		obtained.loadFromNodeSettings(settings.getNodeSettings("miscs0"));

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
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numMiscs", 1);
		misc.saveToNodeSettings(settings.addNodeSettings("miscs0"));

		final MiscList list = new MiscList();
		list.loadFromNodeSettings(settings);

		final Misc expected = misc;  // expected Misc
		final Misc obtained = list.getMiscs()[0];  // obtained Misc

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
