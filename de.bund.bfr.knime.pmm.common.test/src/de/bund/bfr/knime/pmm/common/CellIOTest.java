package de.bund.bfr.knime.pmm.common;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.data.xml.XMLCellFactory;

import de.bund.bfr.knime.pmm.extendedtable.Model1Metadata;
import de.bund.bfr.knime.pmm.extendedtable.Model2Metadata;
import de.bund.bfr.knime.pmm.extendedtable.TimeSeriesMetadata;

public class CellIOTest {

	@Test
	public void testGetString() {
		assertNull(CellIO.getString(DataType.getMissingCell()));
		assertNull(CellIO.getString(new StringCell("")));
		assertEquals("Hello world", CellIO.getString(new StringCell("Hello world")));
	}

	@Test
	public void testGetInt() {
		assertNull(CellIO.getInt(DataType.getMissingCell()));
		assertEquals(7, CellIO.getInt(new IntCell(7)).intValue());
	}

	@Test
	public void testGetPmmXml() throws Exception {
		PmmXmlDoc doc0 = CellIO.getPmmXml(DataType.getMissingCell());
		assertTrue(doc0 instanceof PmmXmlDoc && doc0.size() == 0);

		DataCell cell = XMLCellFactory.create("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<note>\r\n"
				+ "  <to>Tove</to>\r\n" + "  <from>Jani</from>\r\n" + "  <heading>Reminder</heading>\r\n"
				+ "  <body>Don't forget me this weekend!</body>\r\n" + "</note>");
		PmmXmlDoc doc1 = CellIO.getPmmXml(cell);
		assertTrue(doc1 instanceof PmmXmlDoc);
	}

	@Test
	public void testGetDouble() {
		assertNull(CellIO.getDouble(DataType.getMissingCell()));
		assertEquals(5.0, CellIO.getDouble(new DoubleCell(5.0)).doubleValue(), .0);
	}

	@Test
	public void testGetStringList() {
		assertTrue(CellIO.getStringList(DataType.getMissingCell()).isEmpty());
		assertEquals(Arrays.asList("A", "B", "C"), CellIO.getStringList(new StringCell("A,B,C")));
	}

	@Test
	public void testGetDoubleList() {
		assertTrue(CellIO.getDoubleList(DataType.getMissingCell()).isEmpty());
		assertEquals(Arrays.asList(1.0, 2.0, 3.0), CellIO.getDoubleList(new StringCell("1.0,2.0,3.0")));
	}

	@Test
	public void testGetIntList() {
		assertTrue(CellIO.getIntList(DataType.getMissingCell()).isEmpty());
		assertEquals(Arrays.asList(1, 2, 3), CellIO.getIntList(new StringCell("1,2,3")));
	}

	@Test
	public void testCreateIntCell() {
		assertTrue(CellIO.createIntCell(null).isMissing());
		assertEquals(1, ((IntCell) CellIO.createIntCell("1")).getIntValue());
		assertTrue(CellIO.createIntCell("NAN").isMissing());
	}

	@Test
	public void testCreateDoubleCell() {
		assertTrue(CellIO.createDoubleCell(null).isMissing());
		assertEquals(2.0, ((DoubleCell) CellIO.createDoubleCell("2.0")).getDoubleValue(), .0);
		assertTrue(CellIO.createDoubleCell("NAN").isMissing());
	}

	@Test
	public void testCreateXmlCell() {

		// #createXMLCell(PmmXmlDoc)
		assertNull(CellIO.createXmlCell((PmmXmlDoc) null));

		XMLCell cell0 = (XMLCell) CellIO.createXmlCell(new PmmXmlDoc(new MatrixXml()));
		assertEquals("matrix", cell0.getDocument().getFirstChild().getFirstChild().getNodeName());

		// #createXMLCell(TimeSeriesMetadata)
		assertNull(CellIO.createXmlCell((TimeSeriesMetadata) null));

		XMLCell cell1 = (XMLCell) CellIO.createXmlCell(new TimeSeriesMetadata());
		assertEquals("PmmDoc", cell1.getDocument().getFirstChild().getNodeName());

		// #createXMLCell(Model1Metadata)
		assertNull(CellIO.createXmlCell((Model1Metadata) null));

		XMLCell cell2 = (XMLCell) CellIO.createXmlCell(new Model1Metadata());
		assertEquals("PmmDoc", cell2.getDocument().getFirstChild().getNodeName());

		// #createXMLCell(Model2Metadata)
		assertNull(CellIO.createXmlCell((Model2Metadata) null));

		XMLCell cell3 = (XMLCell) CellIO.createXmlCell(new Model2Metadata());
		assertEquals("PmmDoc", cell3.getDocument().getFirstChild().getNodeName());
	}
}
