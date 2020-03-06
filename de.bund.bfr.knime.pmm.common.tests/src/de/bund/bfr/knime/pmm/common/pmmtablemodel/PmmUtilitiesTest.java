package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;

@SuppressWarnings("static-method")
public class PmmUtilitiesTest {

	private static List<KnimeTuple> tuples;

	@BeforeClass
	public static void setUp() {

		PmmXmlDoc indeps = new PmmXmlDoc();
		indeps.add(new IndepXml("x", 0.0, 1.0));
		indeps.add(new IndepXml("y", 0.0, 1.0));

		PmmXmlDoc miscs = new PmmXmlDoc();
		miscs.add(new MiscXml(0, "pH", "description", 0.0, Arrays.asList("water"), "unit"));
		miscs.add(new MiscXml(1, "water activity", "description", 0.0, Arrays.asList("water"), "unit"));

		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM1DataSchema());
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, indeps);
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscs);

		tuples = Arrays.asList(tuple);
	}

	@Test
	public void testGetIndeps() throws Exception {
		List<String> returnedIndeps = PmmUtilities.getIndeps(tuples);
		assertEquals("x", returnedIndeps.get(0));
		assertEquals("y", returnedIndeps.get(1));
	}
	
	@Test
	public void testGetMiscParams() throws Exception {
		List<String> returnedMiscs = PmmUtilities.getMiscParams(tuples);
		assertEquals("pH", returnedMiscs.get(0));
		assertEquals("water activity", returnedMiscs.get(1));
	}
	
	@Test
	public void testGetMiscCategories() throws Exception {
		Map<String, List<String>> returnedMap = PmmUtilities.getMiscCategories(tuples);
		assertEquals("water", returnedMap.get("pH").get(0));
		assertEquals("water", returnedMap.get("water activity").get(0));
	}
}
