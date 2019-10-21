package de.bund.bfr.knime.pmm.editor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.js.common.Agent;
import de.bund.bfr.knime.pmm.js.common.CatalogModel;
import de.bund.bfr.knime.pmm.js.common.Dep;
import de.bund.bfr.knime.pmm.js.common.EstModel;
import de.bund.bfr.knime.pmm.js.common.Indep;
import de.bund.bfr.knime.pmm.js.common.IndepList;
import de.bund.bfr.knime.pmm.js.common.Literature;
import de.bund.bfr.knime.pmm.js.common.LiteratureList;
import de.bund.bfr.knime.pmm.js.common.Matrix;
import de.bund.bfr.knime.pmm.js.common.MdInfo;
import de.bund.bfr.knime.pmm.js.common.Misc;
import de.bund.bfr.knime.pmm.js.common.MiscList;
import de.bund.bfr.knime.pmm.js.common.Model1DataTuple;
import de.bund.bfr.knime.pmm.js.common.ModelList;
import de.bund.bfr.knime.pmm.js.common.Param;
import de.bund.bfr.knime.pmm.js.common.ParamList;
import de.bund.bfr.knime.pmm.js.common.TimeSeries;
import de.bund.bfr.knime.pmm.js.common.TimeSeriesList;

/**
 * Model Editor node model.
 *
 * @author Miguel Alba
 */
public final class ModelEditorNodeModel
		extends AbstractWizardNodeModel<ModelEditorViewRepresentation, ModelEditorViewValue> {

	private boolean m_executed = false;
	private final ModelEditorViewConfig m_config;

	/** Constructor of (@code ModelEditorNodeModel). */
	protected ModelEditorNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE }, "Model editor");
		m_config = new ModelEditorViewConfig();
	}

	/** {@inheritDoc} */
	@Override
	public ModelEditorViewRepresentation createEmptyViewRepresentation() {
		return new ModelEditorViewRepresentation();
	}

	/** {@inheritDoc} */
	@Override
	public ModelEditorViewValue createEmptyViewValue() {
		return new ModelEditorViewValue();
	}

	@Override
	public String getJavascriptObjectID() {
		return "de.bund.bfr.knime.pmm.editor";
	}

	@Override
	public boolean isHideInWizard() {
		return false;
	}

	@Override
	public ValidationError validateViewValue(ModelEditorViewValue viewContent) {
		synchronized (getLock()) {
			// Nothing to do.
		}
		return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO settings) {
		getViewValue().getModels().saveToNodeSettings(settings);
	}

	@Override
	public DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		if (!SchemaFactory.createM1DataSchema().conforms(inSpecs[0])) {
			throw new InvalidSettingsException("Wrong input. Only works so far with M1DataSchema");
		}
		return new DataTableSpec[] { null };
	}

	@Override
	protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		final BufferedDataTable table = (BufferedDataTable) inObjects[0];
		final List<KnimeTuple> tuples = PmmUtilities.getTuples(table, SchemaFactory.createM1DataSchema());

		ModelEditorViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
			setViewValue(viewValue);
		}

		if (!m_executed) {
			// Config of JavaScript view
			// viewValue.setModels(m_config.getModels());

			// Convert KNIME tuples to Model1DataTuple
			final Model1DataTuple[] m1DataTuples = new Model1DataTuple[tuples.size()];
			for (int i = 0; i < tuples.size(); i++) {
				m1DataTuples[i] = codeTuple(tuples.get(i));
			}
			final ModelList modelList = new ModelList();
			modelList.setModels(m1DataTuples);
			viewValue.setModels(modelList);

			setViewValue(viewValue);
			m_executed = true;
		}

		exec.setProgress(1);

		// return edited table
		final BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createM1DataSchema().createSpec());
		final ModelList outModelList = getViewValue().getModels();
		for (final Model1DataTuple m1DataTuple : outModelList.getModels()) {
			final KnimeTuple outTuple = decodeTuple(m1DataTuple);
			container.addRowToTable(outTuple);
		}
		container.close();
		return new BufferedDataTable[] { container.getTable() };
	}

	@Override
	protected void performReset() {
		m_executed = false;
	}

	@Override
	protected void useCurrentValueAsDefault() {
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_config.saveSettings(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		new ModelEditorViewConfig().loadSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_config.loadSettings(settings);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////

	// TODO: Move to another class: --> Model1DataTuple
	public static Model1DataTuple codeTuple(KnimeTuple inTuple) {
		final Model1DataTuple outTuple = new Model1DataTuple();
		// process
		outTuple.condId = inTuple.getInt(TimeSeriesSchema.ATT_CONDID);
		outTuple.combaseId = inTuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		outTuple.globalModelId = inTuple.getInt(TimeSeriesSchema.ATT_GLOBALMODELID);

		final PmmXmlDoc miscDoc = inTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		if (miscDoc != null) {
			final Misc[] miscs = new Misc[miscDoc.size()];
			for (int i = 0; i < miscDoc.size(); i++) {
				final MiscXml miscXml = (MiscXml) miscDoc.get(i);
				final Misc misc = new Misc();
				misc.id = miscXml.id;
				misc.name = miscXml.name;
				misc.description = miscXml.description;
				misc.value = miscXml.value;
				misc.categories = miscXml.categories.toArray(new String[miscXml.categories.size()]);
				misc.unit = miscXml.unit;
				misc.origUnit = miscXml.origUnit;
				misc.dbuuid = miscXml.dbuuid;
				miscs[i] = misc;
			}
			outTuple.miscList = new MiscList();
			outTuple.miscList.setMiscs(miscs);
		}

		final AgentXml agentXml = (AgentXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0);
		if (agentXml != null) {
			final Agent agent = new Agent();
			agent.id = agentXml.id;
			agent.name = agentXml.name;
			agent.detail = agentXml.detail;
			agent.dbuuid = agentXml.dbuuid;
			outTuple.agent = agent;
		}

		final MatrixXml matrixXml = (MatrixXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0);
		if (matrixXml != null) {
			final Matrix matrix = new Matrix();
			matrix.id = matrixXml.id;
			matrix.name = matrixXml.name;
			matrix.detail = matrixXml.detail;
			matrix.dbuuid = matrixXml.dbuuid;
			outTuple.matrix = matrix;
		}

		final PmmXmlDoc timeSeriesDoc = inTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		if (timeSeriesDoc != null) {
			final TimeSeries[] timeSeriesArray = new TimeSeries[timeSeriesDoc.size()];
			for (int i = 0; i < timeSeriesDoc.size(); i++) {
				final TimeSeriesXml timeSeriesXml = (TimeSeriesXml) timeSeriesDoc.get(i);

				final TimeSeries timeSeries = new TimeSeries();
				timeSeries.name = timeSeriesXml.name;
				timeSeries.time = timeSeriesXml.time;
				timeSeries.timeUnit = timeSeriesXml.timeUnit;
				timeSeries.origTimeUnit = timeSeriesXml.origTimeUnit;
				timeSeries.concentration = timeSeriesXml.concentration;
				timeSeries.concentrationUnit = timeSeriesXml.concentrationUnit;
				timeSeries.concentrationUnitObjectType = timeSeriesXml.concentrationUnitObjectType;
				timeSeries.origConcentrationUnit = timeSeriesXml.origConcentrationUnit;
				timeSeries.concentrationStdDev = timeSeriesXml.concentrationStdDev;
				timeSeries.numberOfMeasurements = timeSeriesXml.numberOfMeasurements;
				timeSeriesArray[i] = timeSeries;
			}

			outTuple.timeSeriesList = new TimeSeriesList();
			outTuple.timeSeriesList.setTimeSeries(timeSeriesArray);
		}

		final PmmXmlDoc mdInfoAtt = inTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO);
		MdInfoXml mdInfoXml = null;
		if (mdInfoAtt != null) {
			mdInfoXml = (MdInfoXml) mdInfoAtt.get(0);
		}

		if (mdInfoXml != null) {
			final MdInfo mdInfo = new MdInfo();
			mdInfo.id = mdInfoXml.id;
			mdInfo.name = mdInfoXml.name;
			mdInfo.comment = mdInfoXml.comment;
			mdInfo.qualityScore = mdInfoXml.qualityScore;
			mdInfo.checked = mdInfoXml.checked;
			outTuple.mdInfo = mdInfo;
		}

		final PmmXmlDoc mdLitDoc = inTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		if (mdLitDoc != null) {
			final Literature[] mdLiteratureArray = new Literature[mdLitDoc.size()];
			for (int i = 0; i < mdLitDoc.size(); i++) {
				final LiteratureItem literatureItem = (LiteratureItem) mdLitDoc.get(i);

				final Literature literature = new Literature();
				literature.author = literatureItem.author;
				literature.year = literatureItem.year;
				literature.title = literatureItem.title;
				literature.abstractText = literatureItem.abstractText;
				literature.journal = literatureItem.journal;
				literature.volume = literatureItem.volume;
				literature.issue = literatureItem.issue;
				literature.page = literatureItem.page;
				literature.approvalMode = literatureItem.approvalMode;
				literature.website = literatureItem.website;
				literature.type = literatureItem.type;
				literature.comment = literatureItem.comment;
				literature.id = literature.id;
				literature.dbuuid = literature.dbuuid;

				mdLiteratureArray[i] = literature;
			}
			outTuple.litMd = new LiteratureList();
			outTuple.litMd.setLiterature(mdLiteratureArray);
		}

		outTuple.dbuuid = inTuple.getString(TimeSeriesSchema.ATT_DBUUID); // deprecated?

		final CatalogModelXml catalogModelXml = (CatalogModelXml) inTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG)
				.get(0);
		if (catalogModelXml != null) {
			final CatalogModel catalogModel = new CatalogModel();
			catalogModel.id = catalogModelXml.id;
			catalogModel.name = catalogModelXml.name;
			catalogModel.formula = catalogModelXml.formula;
			catalogModel.modelClass = catalogModelXml.modelClass;
			catalogModel.comment = catalogModelXml.comment;
			catalogModel.dbuuid = catalogModelXml.dbuuid;
			outTuple.catModel = catalogModel;
		}

		final PmmXmlDoc catalogModelSecAtt = inTuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
		CatalogModelXml catalogModelSecXml = null;
		if (catalogModelSecAtt != null) {
			catalogModelSecXml = (CatalogModelXml) catalogModelSecAtt.get(0);
		}

		if (catalogModelSecXml != null) {
			final CatalogModel catalogModelSec = new CatalogModel();
			catalogModelSec.id = catalogModelSecXml.id;
			catalogModelSec.name = catalogModelSecXml.name;
			catalogModelSec.formula = catalogModelSecXml.formula;
			catalogModelSec.modelClass = catalogModelSecXml.modelClass;
			catalogModelSec.comment = catalogModelSecXml.comment;
			catalogModelSec.dbuuid = catalogModelSecXml.dbuuid;
			outTuple.catModelSec = catalogModelSec;
		}

		final PmmXmlDoc estModelAtt = inTuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
		EstModelXml estModelXml = null;
		if (estModelAtt != null) {
			estModelXml = (EstModelXml) estModelAtt.get(0);
		}

		if (estModelXml != null) {
			final EstModel estModel = new EstModel();
			estModel.id = estModelXml.id;
			estModel.name = estModelXml.name;
			estModel.sse = estModelXml.sse;
			estModel.rms = estModelXml.rms;
			estModel.r2 = estModelXml.r2;
			estModel.aic = estModelXml.aic;
			estModel.bic = estModelXml.bic;
			estModel.dof = estModelXml.dof;
			estModel.qualityScore = estModelXml.qualityScore;
			estModel.checked = estModelXml.checked;
			estModel.comment = estModelXml.comment;
			estModel.dbuuid = estModelXml.dbuuid;
			outTuple.estModel = estModel;
		}

		final PmmXmlDoc depAtt = inTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
		DepXml depXml = null;
		if (depAtt != null) {
			depXml = (DepXml) depAtt.get(0);
		}

		if (depXml != null) {
			final Dep dep = new Dep();
			dep.name = depXml.name;
			dep.origname = depXml.origName;
			dep.min = depXml.min;
			dep.max = depXml.max;
			dep.category = depXml.category;
			dep.unit = depXml.unit;
			dep.description = depXml.description;
			outTuple.dep = dep;
		}

		final PmmXmlDoc paramDoc = inTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		final Param[] paramArray = new Param[paramDoc.size()];
		for (int i = 0; i < paramDoc.size(); i++) {
			final ParamXml paramXml = (ParamXml) paramDoc.get(i);

			final HashMap<String, Double> obtainedCorrelations = paramXml.correlations;
			final String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
			final double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
			int j = 0;
			for (final Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
				obtainedCorrelationNames[j] = entry.getKey();
				obtainedCorrelationValues[j] = entry.getValue();
				j++;
			}

			final Param param = new Param();
			param.name = paramXml.name;
			param.origName = paramXml.origName;
			param.isStart = paramXml.isStartParam;
			param.value = paramXml.value;
			param.error = paramXml.error;
			param.min = paramXml.min;
			param.max = paramXml.max;
			param.p = paramXml.P;
			param.t = paramXml.t;
			param.minGuess = paramXml.minGuess;
			param.maxGuess = paramXml.maxGuess;
			param.category = paramXml.category;
			param.unit = paramXml.unit;
			param.description = paramXml.description;
			param.correlationNames = obtainedCorrelationNames;
			param.correlationValues = obtainedCorrelationValues;

			paramArray[i] = param;
		}
		outTuple.params = new ParamList();
		outTuple.params.setParams(paramArray);

		final PmmXmlDoc paramSecDoc = inTuple.getPmmXml(Model2Schema.ATT_PARAMETER);
		if (paramSecDoc != null) {
			final Param[] paramSecArray = new Param[paramSecDoc.size()];
			for (int i = 0; i < paramSecDoc.size(); i++) {
				final ParamXml paramSecXml = (ParamXml) paramSecDoc.get(i);

				final HashMap<String, Double> obtainedCorrelations = paramSecXml.correlations;
				final String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
				final double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
				int j = 0;
				for (final Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
					obtainedCorrelationNames[j] = entry.getKey();
					obtainedCorrelationValues[j] = entry.getValue();
					j++;
				}

				final Param paramSec = new Param();
				paramSec.name = paramSecXml.name;
				paramSec.origName = paramSecXml.origName;
				paramSec.isStart = paramSecXml.isStartParam;
				paramSec.value = paramSecXml.value;
				paramSec.error = paramSecXml.error;
				paramSec.min = paramSecXml.min;
				paramSec.max = paramSecXml.max;
				paramSec.p = paramSecXml.P;
				paramSec.t = paramSecXml.t;
				paramSec.minGuess = paramSecXml.minGuess;
				paramSec.maxGuess = paramSecXml.maxGuess;
				paramSec.category = paramSecXml.category;
				paramSec.unit = paramSecXml.unit;
				paramSec.description = paramSecXml.description;
				paramSec.correlationNames = obtainedCorrelationNames;
				paramSec.correlationValues = obtainedCorrelationValues;

				paramSecArray[i] = paramSec;
			}
			outTuple.paramsSec = new ParamList();
			outTuple.paramsSec.setParams(paramSecArray);
		}

		final PmmXmlDoc indepDoc = inTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		if (indepDoc != null) {
			final Indep[] indepArray = new Indep[indepDoc.size()];
			for (int i = 0; i < indepDoc.size(); i++) {
				final IndepXml indepXml = (IndepXml) indepDoc.get(i);
				final Indep indep = new Indep();
				indep.name = indepXml.name;
				indep.origname = indepXml.origName;
				indep.min = indepXml.min;
				indep.max = indepXml.max;
				indep.category = indepXml.category;
				indep.unit = indepXml.unit;
				indep.description = indepXml.description;
				indepArray[i] = indep;
			}
			outTuple.indeps = new IndepList();
			outTuple.indeps.setIndeps(indepArray);
		}

		final PmmXmlDoc indepSecDoc = inTuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
		if (indepSecDoc != null) {
			final Indep[] indepSecArray = new Indep[indepSecDoc.size()];
			for (int i = 0; i < indepSecDoc.size(); i++) {
				final IndepXml indepSecXml = (IndepXml) indepSecDoc.get(i);
				final Indep indepSec = new Indep();
				indepSec.name = indepSecXml.name;
				indepSec.origname = indepSecXml.origName;
				indepSec.min = indepSecXml.min;
				indepSec.max = indepSecXml.max;
				indepSec.category = indepSecXml.category;
				indepSec.unit = indepSecXml.unit;
				indepSec.description = indepSecXml.description;
				indepSecArray[i] = indepSec;
			}

			outTuple.indepsSec = new IndepList();
			outTuple.indepsSec.setIndeps(indepSecArray);
		}

		final PmmXmlDoc mLitDoc = inTuple.getPmmXml(Model1Schema.ATT_MLIT);
		if (mLitDoc != null) {
			final Literature[] mLiteratureArray = new Literature[mLitDoc.size()];
			for (int i = 0; i < mLitDoc.size(); i++) {
				final LiteratureItem literatureItem = (LiteratureItem) mLitDoc.get(i);
				final Literature literature = new Literature();
				literature.author = literatureItem.author;
				literature.year = literatureItem.year;
				literature.title = literatureItem.title;
				literature.abstractText = literatureItem.abstractText;
				literature.journal = literatureItem.journal;
				literature.volume = literatureItem.volume;
				literature.issue = literatureItem.issue;
				literature.page = literatureItem.page;
				literature.approvalMode = literatureItem.approvalMode;
				literature.website = literatureItem.website;
				literature.type = literatureItem.type;
				literature.comment = literatureItem.comment;
				literature.id = literatureItem.id;
				literature.dbuuid = literatureItem.dbuuid;

				mLiteratureArray[i] = literature;
			}
			outTuple.mLit = new LiteratureList();
			outTuple.mLit.setLiterature(mLiteratureArray);
		}

		final PmmXmlDoc emLitDoc = inTuple.getPmmXml(Model1Schema.ATT_EMLIT);
		if (emLitDoc != null) {
			final Literature[] emLiteratureArray = new Literature[emLitDoc.size()];
			for (int i = 0; i < emLitDoc.size(); i++) {
				final LiteratureItem literatureItem = (LiteratureItem) emLitDoc.get(i);

				final Literature literature = new Literature();
				literature.author = literatureItem.author;
				literature.year = literatureItem.year;
				literature.title = literatureItem.title;
				literature.abstractText = literatureItem.abstractText;
				literature.journal = literatureItem.journal;
				literature.volume = literatureItem.volume;
				literature.issue = literatureItem.issue;
				literature.page = literatureItem.page;
				literature.approvalMode = literatureItem.approvalMode;
				literature.website = literatureItem.website;
				literature.type = literatureItem.type;
				literature.comment = literatureItem.comment;
				literature.id = literatureItem.id;
				literature.dbuuid = literatureItem.dbuuid;

				emLiteratureArray[i] = literature;
			}
			outTuple.emLit = new LiteratureList();
			outTuple.emLit.setLiterature(emLiteratureArray);
		}

		if (inTuple.getInt(Model1Schema.ATT_DATABASEWRITABLE) != null) {
			outTuple.databaseWritable = Model1Schema.WRITABLE == inTuple.getInt(Model1Schema.ATT_DATABASEWRITABLE);
		} else {
			outTuple.databaseWritable = false; // default is false
		}

		return outTuple;
	}

	// TODO: Move to another class: --> Model1DataTuple
	public static KnimeTuple decodeTuple(final Model1DataTuple m1DataTuple) {
		final KnimeTuple outTuple = new KnimeTuple(SchemaFactory.createM1DataSchema());

		outTuple.setValue(TimeSeriesSchema.ATT_CONDID, m1DataTuple.condId);
		outTuple.setValue(TimeSeriesSchema.ATT_COMBASEID, m1DataTuple.combaseId);

		final PmmXmlDoc miscDoc = new PmmXmlDoc();
		for (final Misc misc : m1DataTuple.miscList.getMiscs()) {
			miscDoc.add(new MiscXml(misc.id, misc.name, misc.description, misc.value, Arrays.asList(misc.categories),
					misc.unit, misc.origUnit, misc.dbuuid));
		}
		outTuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);

		final Agent agent = m1DataTuple.agent;
		final PmmXmlDoc agentDoc = new PmmXmlDoc(new AgentXml(agent.id, agent.name, agent.detail, agent.dbuuid));
		outTuple.setValue(TimeSeriesSchema.ATT_AGENT, agentDoc);

		final Matrix matrix = m1DataTuple.matrix;
		final PmmXmlDoc matrixDoc = new PmmXmlDoc(new MatrixXml(matrix.id, matrix.name, matrix.detail, matrix.dbuuid));
		outTuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixDoc);

		final PmmXmlDoc timeSeriesDoc = new PmmXmlDoc();
		for (final TimeSeries timeSeries : m1DataTuple.timeSeriesList.getTimeSeries()) {
			timeSeriesDoc.add(new TimeSeriesXml(timeSeries.name, timeSeries.time, timeSeries.timeUnit,
					timeSeries.origTimeUnit, timeSeries.concentration, timeSeries.concentrationUnit,
					timeSeries.concentrationUnitObjectType, timeSeries.origConcentrationUnit,
					timeSeries.concentrationStdDev, timeSeries.numberOfMeasurements));
		}
		outTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesDoc);

		final MdInfo mdInfo = m1DataTuple.mdInfo;
		final PmmXmlDoc mdInfoDoc = new PmmXmlDoc(
				new MdInfoXml(mdInfo.id, mdInfo.name, mdInfo.comment, mdInfo.qualityScore, mdInfo.checked));
		outTuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);

		final PmmXmlDoc mdLitDoc = new PmmXmlDoc();
		for (final Literature literature : m1DataTuple.litMd.getLiterature()) {
			mdLitDoc.add(new LiteratureItem(literature.author, literature.year, literature.title,
					literature.abstractText, literature.journal, literature.volume, literature.issue, literature.page,
					literature.approvalMode, literature.website, literature.type, literature.comment, literature.id,
					literature.dbuuid));
		}
		outTuple.setValue(TimeSeriesSchema.ATT_LITMD, mdLitDoc);

		outTuple.setValue(TimeSeriesSchema.ATT_DBUUID, m1DataTuple.dbuuid);
		outTuple.setValue(Model1Schema.ATT_DBUUID, m1DataTuple.dbuuid);

		final CatalogModel catalogModel = m1DataTuple.catModel;
		final CatalogModelXml catalogModelXml = new CatalogModelXml(catalogModel.id, catalogModel.name,
				catalogModel.formula, catalogModel.modelClass, catalogModel.dbuuid);
		catalogModelXml.comment = catalogModel.comment;
		final PmmXmlDoc catalogModelDoc = new PmmXmlDoc(catalogModelXml);
		outTuple.setValue(Model1Schema.ATT_MODELCATALOG, catalogModelDoc);

		final EstModel estModel = m1DataTuple.estModel;
		final EstModelXml estModelXml = new EstModelXml(estModel.id, estModel.name, estModel.sse, estModel.rms,
				estModel.r2, estModel.aic, estModel.bic, estModel.dof, true, estModel.qualityScore, estModel.dbuuid);
		estModelXml.comment = estModel.comment;
		final PmmXmlDoc estModelDoc = new PmmXmlDoc(estModelXml);
		outTuple.setValue(Model1Schema.ATT_ESTMODEL, estModelDoc);

		final Dep dep = m1DataTuple.dep;
		final DepXml depXml = new DepXml(dep.name, dep.origname, dep.category, dep.unit, dep.description);
		depXml.min = dep.min;
		depXml.max = dep.max;
		outTuple.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));

		final PmmXmlDoc paramDoc = new PmmXmlDoc();
		final ParamList paramList = m1DataTuple.params;
		for (final Param param : paramList.getParams()) {
			final HashMap<String, Double> correlations = new HashMap<>();
			if (param.correlationNames != null && param.correlationValues != null) {
				for (int i = 0; i < param.correlationNames.length; i++) {
					final String correlationName = param.correlationNames[i];
					final Double correlationValue = param.correlationValues[i];
					correlations.put(correlationName, correlationValue);
				}
			}
			final ParamXml paramXml = new ParamXml(param.name, param.origName, param.isStart, param.value, param.error,
					param.min, param.max, param.p, param.t, param.minGuess, param.maxGuess, param.category, param.unit,
					param.description, correlations);
			paramDoc.add(paramXml);
		}
		outTuple.setValue(Model1Schema.ATT_PARAMETER, paramDoc);

		final PmmXmlDoc indepDoc = new PmmXmlDoc();
		final IndepList indepList = m1DataTuple.indeps;
		for (final Indep indep : indepList.getIndeps()) {
			final IndepXml indepXml = new IndepXml(indep.name, indep.origname, indep.min, indep.max, indep.category,
					indep.unit, indep.description);
			indepDoc.add(indepXml);
		}
		outTuple.setValue(Model1Schema.ATT_INDEPENDENT, indepDoc);

		final PmmXmlDoc mLitDoc = new PmmXmlDoc();
		for (final Literature literature : m1DataTuple.mLit.getLiterature()) {
			mLitDoc.add(new LiteratureItem(literature.author, literature.year, literature.title,
					literature.abstractText, literature.journal, literature.volume, literature.issue, literature.page,
					literature.approvalMode, literature.website, literature.type, literature.comment, literature.id,
					literature.dbuuid));
		}
		outTuple.setValue(Model1Schema.ATT_MLIT, mLitDoc);

		final PmmXmlDoc emLitDoc = new PmmXmlDoc();
		for (final Literature literature : m1DataTuple.emLit.getLiterature()) {
			emLitDoc.add(new LiteratureItem(literature.author, literature.year, literature.title,
					literature.abstractText, literature.journal, literature.volume, literature.issue, literature.page,
					literature.approvalMode, literature.website, literature.type, literature.comment, literature.id,
					literature.dbuuid));
		}
		outTuple.setValue(Model1Schema.ATT_EMLIT, emLitDoc);

		outTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1DataTuple.databaseWritable ? 1 : 0);

		return outTuple;
	}

	@Override
	public void setHideInWizard(boolean hide) {
	}

}