package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.bund.bfr.knime.pmm.common.IndepXml;
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
		
		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM1Schema());
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, indeps);
		
		tuples = Arrays.asList(tuple);
	}
	
	@Test
	public void testGetIndeps() throws Exception {
		List<String> returnedIndeps = PmmUtilities.getIndeps(tuples);
		assertEquals("x", returnedIndeps.get(0));
		assertEquals("y", returnedIndeps.get(1));
	}
}
