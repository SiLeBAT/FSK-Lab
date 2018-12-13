package de.bund.bfr.knime.pmm.common.reader;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.TimeSeriesMetadata;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.items.MDAgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MDLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MDMatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmfml.numl.NuMLDocument;
import de.bund.bfr.pmfml.numl.Tuple;
import de.bund.bfr.pmfml.sbml.Model1Annotation;
import de.bund.bfr.pmfml.sbml.ModelVariable;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.SBMLFactory;

public class DataTuple {

	public KnimeTuple knimeTuple;

	// time series schema
	private static KnimeSchema schema = SchemaFactory.createDataSchema();

	public DataTuple(NuMLDocument doc) {

		int condID = doc.getResultComponent().getCondID();
		String combaseID = (doc.getResultComponent().isSetCombaseID()) ? doc.getResultComponent().getCombaseID() : "?";

		String timeUnit = doc.getTimeOntologyTerm().getUnitDefinition().getName();
		String concUnit = doc.getConcentrationOntologyTerm().getUnitDefinition().getName();

		// Gets concentration unit object type from DB
		String concUnitObjectType;
		if (DBUnits.getDBUnits().containsKey(concUnit)) {
			UnitsFromDB ufdb = DBUnits.getDBUnits().get(concUnit);
			concUnitObjectType = ufdb.getObject_type();
		} else {
			concUnitObjectType = "";
		}

		PMFSpecies species = doc.getConcentrationOntologyTerm().getSpecies();
		AgentXml originalAgentXml = new AgentXml();
		originalAgentXml.name = species.getName();
		MDAgentXml agentXml = new MDAgentXml();
		agentXml.name = species.getName();
		if (species.isSetDetail()) {
			originalAgentXml.detail = species.getDetail();
			agentXml.detail = species.getDetail();
		}

		PMFCompartment compartment = doc.getConcentrationOntologyTerm().getCompartment();
		MatrixXml originalMatrixXml = new MatrixXml();
		MDMatrixXml matrixXml = new MDMatrixXml();
		originalMatrixXml.name = compartment.getName();
		matrixXml.setName(compartment.getName());
		if (compartment.isSetDetail()) {
			originalMatrixXml.detail = compartment.getDetail();
			matrixXml.setDetail(compartment.getDetail());
		}

		// Gets time series
		Tuple[] dimensions = doc.getResultComponent().getDimensions();
		double[][] data = new double[dimensions.length][2];
		for (int i = 0; i < dimensions.length; i++) {
			Tuple tuple = dimensions[i];
			data[i] = new double[] { tuple.getConcValue().getValue(), tuple.getTimeValue().getValue() };
		}
		PmmXmlDoc mdData = ReaderUtils.createTimeSeries(timeUnit, concUnit, concUnitObjectType, data);

		// Gets model variables
		ModelVariable[] modelVariables = compartment.getModelVariables();
		PmmXmlDoc miscDoc;
		if (modelVariables == null) {
			miscDoc = new PmmXmlDoc();
		} else {
			Map<String, Double> miscs = Arrays.stream(modelVariables)
					.collect(Collectors.toMap(ModelVariable::getName, ModelVariable::getValue));
			miscDoc = ReaderUtils.parseMiscs(miscs);
		}

		// Creates empty model info
		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		TimeSeriesMetadata metadata = new TimeSeriesMetadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		// Gets literature items
		PmmXmlDoc litDoc = new PmmXmlDoc();
		for (Reference reference : doc.getResultComponent().getReferences()) {
			String author = reference.isSetAuthor() ? reference.getAuthor() : null;
			Integer year = reference.isSetYear() ? reference.getYear() : null;
			String title = reference.isSetTitle() ? reference.getTitle() : null;
			String abstractText = reference.isSetAbstractText() ? reference.getAbstractText() : null;
			String journal = reference.isSetJournal() ? reference.getJournal() : null;
			String volume = reference.isSetVolume() ? reference.getVolume() : null;
			String issue = reference.isSetIssue() ? reference.getIssue() : null;
			Integer page = reference.isSetPage() ? reference.getPage() : null;
			Integer approvalMode = reference.isSetApprovalMode() ? reference.getApprovalMode() : null;
			String website = reference.isSetWebsite() ? reference.getWebsite() : null;
			int typeValue = reference.isSetType() ? reference.getType().value() : 0;
			String comment = reference.isSetComment() ? reference.getComment() : null;

			LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
					approvalMode, website, typeValue, comment);
			litDoc.add(lit);

			MDLiteratureItem mdLit = new MDLiteratureItem(author, year, title, abstractText, journal, volume, issue,
					page, approvalMode, website, typeValue, comment);
			metadata.addLiteratureItem(mdLit);
		}

		// Creates and fills tuple
		knimeTuple = new KnimeTuple(schema);
		knimeTuple.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		knimeTuple.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
		knimeTuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(originalAgentXml));
		knimeTuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(originalMatrixXml));
		knimeTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
		knimeTuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);
		knimeTuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		knimeTuple.setValue(TimeSeriesSchema.ATT_LITMD, litDoc);
		knimeTuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
		knimeTuple.setValue(TimeSeriesSchema.ATT_METADATA, metadata);
	}

	public DataTuple(SBMLDocument sbmlDoc) {

		Model model = sbmlDoc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
		AgentXml originalAgentXml = new AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
				species.getDetail(), null);
		MDAgentXml agentXml = new MDAgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
				species.getDetail(), null);

		PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
		MatrixXml originalMatrixXml = new MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
				compartment.getDetail(), null);
		MDMatrixXml matrixXml = new MDMatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
				compartment.getDetail(), null);

		TimeSeriesMetadata metadata = new TimeSeriesMetadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		PmmXmlDoc miscCell = new PmmXmlDoc();
		if (compartment.isSetModelVariables()) {
			Map<String, Double> miscs = Arrays.stream(compartment.getModelVariables())
					.collect(Collectors.toMap(ModelVariable::getName, ModelVariable::getValue));
			miscCell = ReaderUtils.parseMiscs(miscs);
		}

		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		knimeTuple = new KnimeTuple(schema);
		knimeTuple.setValue(TimeSeriesSchema.ATT_CONDID, m1Annot.getCondID());
		knimeTuple.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		knimeTuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(originalAgentXml));
		knimeTuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(originalMatrixXml));
		knimeTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		knimeTuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		knimeTuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		knimeTuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		knimeTuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
		knimeTuple.setValue(TimeSeriesSchema.ATT_METADATA, metadata);
	}
}