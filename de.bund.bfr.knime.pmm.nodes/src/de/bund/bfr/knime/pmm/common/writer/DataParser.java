package de.bund.bfr.knime.pmm.common.writer;

import javax.xml.stream.XMLStreamException;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.numl.AtomicDescription;
import de.bund.bfr.pmfml.numl.AtomicValue;
import de.bund.bfr.pmfml.numl.ConcentrationOntology;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.numl.ResultComponent;
import de.bund.bfr.pmfml.numl.TimeOntology;
import de.bund.bfr.pmfml.numl.Tuple;
import de.bund.bfr.pmfml.numl.TupleDescription;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.PMFUnitDefinition;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.ReferenceImpl;

public class DataParser {
	private NuMLDocument numlDocument;

	public DataParser(KnimeTuple tuple, Metadata metadata, String notes) {

		// Gets CondID and CombaseID
		int condId = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);

		// Creates dim
		PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		double concValues[] = new double[mdData.size()];
		double timeValues[] = new double[mdData.size()];
		for (int i = 0; i < mdData.size(); i++) {
			TimeSeriesXml timeSeriesXml = (TimeSeriesXml) mdData.get(i);
			concValues[i] = timeSeriesXml.concentration;
			timeValues[i] = timeSeriesXml.time;
		}

		// Gets first point
		TimeSeriesXml aPoint = (TimeSeriesXml) tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES).get(0);
		String concUnit = aPoint.concentrationUnit;
		PMFUnitDefinition concUnitDef = null;
		try {
			concUnitDef = WriterUtils.createUnitFromDB(concUnit);
		} catch (XMLStreamException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String timeUnit = aPoint.timeUnit;
		PMFUnitDefinition timeUnitDef = null;
		try {
			timeUnitDef = WriterUtils.createUnitFromDB(timeUnit);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0);

		PmmXmlDoc miscDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		PMFCompartment compartment = WriterUtils.matrixXml2Compartment(matrixXml, miscDoc);

		AgentXml agentXml = (AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0);
		PMFSpecies species = WriterUtils.createSpecies(agentXml, concUnit, compartment.getId());

		// Gets microbial data literature
		PmmXmlDoc mdLitDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		Reference[] refs = new ReferenceImpl[mdLitDoc.size()];
		for (int i = 0; i < mdLitDoc.size(); i++) {
			refs[i] = WriterUtils.literatureItem2Reference((LiteratureItem) mdLitDoc.get(i));
		}

		// Creates time ontology
		TimeOntology timeOntology = new TimeOntology(timeUnitDef);

		// Creates concentration ontology
		ConcentrationOntology concOntology = new ConcentrationOntology(concUnitDef, compartment, species);

		AtomicDescription concDesc = new AtomicDescription("concentration", ConcentrationOntology.TERM);
		AtomicDescription timeDesc = new AtomicDescription("time", TimeOntology.TERM);
		TupleDescription tupleDesc = new TupleDescription(concDesc, timeDesc);

		Tuple[] values = new Tuple[timeValues.length];
		for (int i = 0; i < timeValues.length; i++) {
			AtomicValue concValue = new AtomicValue(concValues[i]);
			AtomicValue timeValue = new AtomicValue(timeValues[i]);
			values[i] = new Tuple(concValue, timeValue);
		}

		String creatorGivenName = metadata.getGivenName();
		String creatorFamilyName = metadata.getFamilyName();
		String creatorContact = metadata.getContact();
		String createdDate = metadata.getCreatedDate();
		String modifiedDate = metadata.getModifiedDate();
		String rights = metadata.getRights();
		ModelType modelType = metadata.getType();

		ResultComponent resultComponent = new ResultComponent("exp1", condId, combaseId, creatorGivenName,
				creatorFamilyName, creatorContact, createdDate, modifiedDate, modelType, rights, notes, refs, tupleDesc,
				values);

		numlDocument = new NuMLDocument(concOntology, timeOntology, resultComponent);
	}

	public NuMLDocument getDocument() {
		return numlDocument;
	}
}