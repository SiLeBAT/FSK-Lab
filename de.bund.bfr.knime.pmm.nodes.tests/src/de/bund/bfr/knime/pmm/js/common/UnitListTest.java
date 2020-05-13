package de.bund.bfr.knime.pmm.js.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;

@SuppressWarnings("static-method")
public class UnitListTest {

	static Unit unit = UnitTest.unit;

	@Test
	public void testUnits() {
		final UnitList list = new UnitList();
		assertNull(list.getUnits());

		list.setUnits(new Unit[] { unit });

		final Unit expected = unit; // expected Unit
		final Unit obtained = list.getUnits()[0]; // obtained Unit

		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testSaveToNodeSettings() throws InvalidSettingsException {
		final UnitList list = new UnitList();
		list.setUnits(new Unit[] { unit });

		final NodeSettings settings = new NodeSettings("irrelevantKey");
		list.saveToNodeSettings(settings);

		assertEquals(1, settings.getInt("numUnits"));

		final Unit expected = unit; // expected unit
		final Unit obtained = new Unit(); // obtained unit
		obtained.loadFromNodeSettings(settings.getNodeSettings("units0"));

		TestUtils.compare(obtained, expected);
	}

	@Test
	public void testLoadFromNodeSettings() {
		final NodeSettings settings = new NodeSettings("irrelevantKey");
		settings.addInt("numUnits", 1);
		unit.saveToNodeSettings(settings.addNodeSettings("units0"));

		final UnitList list = new UnitList();
		list.loadFromNodeSettings(settings);

		final Unit expected = unit; // expected Unit
		final Unit obtained = list.getUnits()[0]; // obtained Unit
		TestUtils.compare(obtained, expected);
	}
}
