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
		BufferedDataTable table = (BufferedDataTable) inObjects[0];
		List<KnimeTuple> tuples = PmmUtilities.getTuples(table, SchemaFactory.createM1DataSchema());

		ModelEditorViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
			setViewValue(viewValue);
		}

		if (!m_executed) {
			// Config of JavaScript view
			// viewValue.setModels(m_config.getModels());

			// Convert KNIME tuples to Model1DataTuple
			Model1DataTuple[] m1DataTuples = new Model1DataTuple[tuples.size()];
			for (int i = 0; i < tuples.size(); i++) {
				m1DataTuples[i] = codeTuple(tuples.get(i));
			}
			ModelList modelList = new ModelList();
			modelList.setModels(m1DataTuples);
			viewValue.setModels(modelList);

			setViewValue(viewValue);
			m_executed = true;
		}

		exec.setProgress(1);

		// return edited table
		BufferedDataContainer container = exec.createDataContainer(SchemaFactory.createM1DataSchema().createSpec());
		ModelList outModelList = getViewValue().getModels();
		for (Model1DataTuple m1DataTuple : outModelList.getModels()) {
			KnimeTuple outTuple = decodeTuple(m1DataTuple);
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
		Model1DataTuple outTuple = new Model1DataTuple();
		// process
		outTuple.setCondId(inTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		outTuple.setCombaseId(inTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		outTuple.setGlobalModelId(inTuple.getInt(TimeSeriesSchema.ATT_GLOBALMODELID));

		PmmXmlDoc miscDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		if (miscDoc != null) {
			Misc[] miscs = new Misc[miscDoc.size()];
			for (int i = 0; i < miscDoc.size(); i++) {
				MiscXml miscXml = (MiscXml) miscDoc.get(i);
				Misc misc = new Misc();
				misc.setId(miscXml.id);
				misc.setName(miscXml.name);
				misc.setDescription(miscXml.description);
				misc.setValue(miscXml.value);
				misc.setCategories(miscXml.categories.toArray(new String[miscXml.categories.size()]));
				misc.setUnit(miscXml.unit);
				misc.setOrigUnit(miscXml.origUnit);
				misc.setDbuuid(miscXml.dbuuid);
				miscs[i] = misc;
			}
			MiscList miscList = new MiscList();
			miscList.setMiscs(miscs);
			outTuple.setMiscList(miscList);
		}

		AgentXml agentXml = (AgentXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0);
		if (agentXml != null) {
			Agent agent = new Agent();
			agent.id = agentXml.id;
			agent.name = agentXml.name;
			agent.detail = agentXml.detail;
			agent.dbuuid = agentXml.dbuuid;
			outTuple.setAgent(agent);
		}

		MatrixXml matrixXml = (MatrixXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0);
		if (matrixXml != null) {
			Matrix matrix = new Matrix();
			matrix.setId(matrixXml.id);
			matrix.setName(matrixXml.name);
			matrix.setDetail(matrixXml.detail);
			matrix.setDbuuid(matrixXml.dbuuid);
			outTuple.setMatrix(matrix);
		}

		PmmXmlDoc timeSeriesDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		if (timeSeriesDoc != null) {
			TimeSeries[] timeSeriesArray = new TimeSeries[timeSeriesDoc.size()];
			for (int i = 0; i < timeSeriesDoc.size(); i++) {
				TimeSeriesXml timeSeriesXml = (TimeSeriesXml) timeSeriesDoc.get(i);

				TimeSeries timeSeries = new TimeSeries();
				timeSeries.setName(timeSeriesXml.name);
				timeSeries.setTime(timeSeriesXml.time);
				timeSeries.setTimeUnit(timeSeriesXml.timeUnit);
				timeSeries.setOrigTimeUnit(timeSeriesXml.origTimeUnit);
				timeSeries.setConcentration(timeSeriesXml.concentration);
				timeSeries.setConcentrationUnit(timeSeriesXml.concentrationUnit);
				timeSeries.setConcentrationUnitObjectType(timeSeriesXml.concentrationUnitObjectType);
				timeSeries.setOrigConcentrationUnit(timeSeriesXml.origConcentrationUnit);
				timeSeries.setConcentrationStdDev(timeSeriesXml.concentrationStdDev);
				timeSeries.setNumberOfMeasurements(timeSeriesXml.numberOfMeasurements);
				timeSeriesArray[i] = timeSeries;
			}
			TimeSeriesList timeSeriesList = new TimeSeriesList();
			timeSeriesList.setTimeSeries(timeSeriesArray);
			outTuple.setTimeSeriesList(timeSeriesList);
		}

		PmmXmlDoc mdInfoAtt = inTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO);
		MdInfoXml mdInfoXml = null;
		if (mdInfoAtt != null)
			mdInfoXml = (MdInfoXml) mdInfoAtt.get(0);

		if (mdInfoXml != null) {
			MdInfo mdInfo = new MdInfo();
			mdInfo.setId(mdInfoXml.id);
			mdInfo.setName(mdInfoXml.name);
			mdInfo.setComment(mdInfoXml.comment);
			mdInfo.setQualityScore(mdInfoXml.qualityScore);
			mdInfo.setChecked(mdInfoXml.checked);
			outTuple.setMdInfo(mdInfo);
		}

		PmmXmlDoc mdLitDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		if (mdLitDoc != null) {
			Literature[] mdLiteratureArray = new Literature[mdLitDoc.size()];
			for (int i = 0; i < mdLitDoc.size(); i++) {
				LiteratureItem literatureItem = (LiteratureItem) mdLitDoc.get(i);

				Literature literature = new Literature();
				literature.setAuthor(literatureItem.author);
				literature.setYear(literatureItem.year);
				literature.setTitle(literatureItem.title);
				literature.setAbstractText(literatureItem.abstractText);
				literature.setJournal(literatureItem.journal);
				literature.setVolume(literatureItem.volume);
				literature.setIssue(literatureItem.issue);
				literature.setPage(literatureItem.page);
				literature.setApprovalMode(literatureItem.approvalMode);
				literature.setWebsite(literatureItem.website);
				literature.setType(literatureItem.type);
				literature.setComment(literatureItem.comment);
				literature.setId(literatureItem.id);
				literature.setDbuuid(literatureItem.dbuuid);

				mdLiteratureArray[i] = literature;
			}
			LiteratureList mdLiteratureList = new LiteratureList();
			mdLiteratureList.setLiterature(mdLiteratureArray);
			outTuple.setLitMd(mdLiteratureList);
		}

		outTuple.setDbuuid(inTuple.getString(TimeSeriesSchema.ATT_DBUUID)); // deprecated?

		CatalogModelXml catalogModelXml = (CatalogModelXml) inTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
		if (catalogModelXml != null) {
			CatalogModel catalogModel = new CatalogModel();
			catalogModel.id = catalogModelXml.id;
			catalogModel.name = catalogModelXml.name;
			catalogModel.formula = catalogModelXml.formula;
			catalogModel.modelClass = catalogModelXml.modelClass;
			catalogModel.comment = catalogModelXml.comment;
			catalogModel.dbuuid = catalogModelXml.dbuuid;
			outTuple.setCatModel(catalogModel);
		}

		PmmXmlDoc catalogModelSecAtt = inTuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
		CatalogModelXml catalogModelSecXml = null;
		if (catalogModelSecAtt != null)
			catalogModelSecXml = (CatalogModelXml) catalogModelSecAtt.get(0);

		if (catalogModelSecXml != null) {
			CatalogModel catalogModelSec = new CatalogModel();
			catalogModelSec.id = catalogModelSecXml.id;
			catalogModelSec.name = catalogModelSecXml.name;
			catalogModelSec.formula = catalogModelSecXml.formula;
			catalogModelSec.modelClass = catalogModelSecXml.modelClass;
			catalogModelSec.comment = catalogModelSecXml.comment;
			catalogModelSec.dbuuid = catalogModelSecXml.dbuuid;
			outTuple.setCatModelSec(catalogModelSec);
		}

		PmmXmlDoc estModelAtt = inTuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
		EstModelXml estModelXml = null;
		if (estModelAtt != null)
			estModelXml = (EstModelXml) estModelAtt.get(0);

		if (estModelXml != null) {
			EstModel estModel = new EstModel();
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
			outTuple.setEstModel(estModel);
		}

		PmmXmlDoc depAtt = inTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
		DepXml depXml = null;
		if (depAtt != null)
			depXml = (DepXml) depAtt.get(0);

		if (depXml != null) {
			Dep dep = new Dep();
			dep.name = depXml.name;
			dep.origname = depXml.origName;
			dep.min = depXml.min;
			dep.max = depXml.max;
			dep.category = depXml.category;
			dep.unit = depXml.unit;
			dep.description = depXml.description;
			outTuple.setDep(dep);
		}

		PmmXmlDoc paramDoc = inTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		Param[] paramArray = new Param[paramDoc.size()];
		for (int i = 0; i < paramDoc.size(); i++) {
			ParamXml paramXml = (ParamXml) paramDoc.get(i);

			HashMap<String, Double> obtainedCorrelations = paramXml.correlations;
			String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
			double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
			int j = 0;
			for (Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
				obtainedCorrelationNames[j] = entry.getKey();
				obtainedCorrelationValues[j] = entry.getValue();
				j++;
			}

			Param param = new Param();
			param.setName(paramXml.name);
			param.setOrigName(paramXml.origName);
			param.setIsStart(paramXml.isStartParam);
			param.setValue(paramXml.value);
			param.setError(paramXml.error);
			param.setMin(paramXml.min);
			param.setMax(paramXml.max);
			param.setP(paramXml.P);
			param.setT(paramXml.t);
			param.setMinGuess(paramXml.minGuess);
			param.setMaxGuess(paramXml.maxGuess);
			param.setCategory(paramXml.category);
			param.setUnit(paramXml.unit);
			param.setDescription(paramXml.description);
			param.setCorrelationNames(obtainedCorrelationNames);
			param.setCorrelationValues(obtainedCorrelationValues);

			paramArray[i] = param;
		}
		ParamList paramList = new ParamList();
		paramList.setParams(paramArray);
		outTuple.setParams(paramList);

		PmmXmlDoc paramSecDoc = inTuple.getPmmXml(Model2Schema.ATT_PARAMETER);
		if (paramSecDoc != null) {
			Param[] paramSecArray = new Param[paramSecDoc.size()];
			for (int i = 0; i < paramSecDoc.size(); i++) {
				ParamXml paramSecXml = (ParamXml) paramSecDoc.get(i);

				HashMap<String, Double> obtainedCorrelations = paramSecXml.correlations;
				String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
				double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
				int j = 0;
				for (Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
					obtainedCorrelationNames[j] = entry.getKey();
					obtainedCorrelationValues[j] = entry.getValue();
					j++;
				}

				Param paramSec = new Param();
				paramSec.setName(paramSecXml.name);
				paramSec.setOrigName(paramSecXml.origName);
				paramSec.setIsStart(paramSecXml.isStartParam);
				paramSec.setValue(paramSecXml.value);
				paramSec.setError(paramSecXml.error);
				paramSec.setMin(paramSecXml.min);
				paramSec.setMax(paramSecXml.max);
				paramSec.setP(paramSecXml.P);
				paramSec.setT(paramSecXml.t);
				paramSec.setMinGuess(paramSecXml.minGuess);
				paramSec.setMaxGuess(paramSecXml.maxGuess);
				paramSec.setCategory(paramSecXml.category);
				paramSec.setUnit(paramSecXml.unit);
				paramSec.setDescription(paramSecXml.description);
				paramSec.setCorrelationNames(obtainedCorrelationNames);
				paramSec.setCorrelationValues(obtainedCorrelationValues);

				paramSecArray[i] = paramSec;
			}
			ParamList paramSecList = new ParamList();
			paramSecList.setParams(paramSecArray);
			outTuple.setParamsSec(paramSecList);
		}

		PmmXmlDoc indepDoc = inTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		if (indepDoc != null) {
			Indep[] indepArray = new Indep[indepDoc.size()];
			for (int i = 0; i < indepDoc.size(); i++) {
				IndepXml indepXml = (IndepXml) indepDoc.get(i);
				Indep indep = new Indep();
				indep.name = indepXml.name;
				indep.origname = indepXml.origName;
				indep.min = indepXml.min;
				indep.max = indepXml.max;
				indep.category = indepXml.category;
				indep.unit = indepXml.unit;
				indep.description = indepXml.description;
				indepArray[i] = indep;
			}
			IndepList indepList = new IndepList();
			indepList.setIndeps(indepArray);
			outTuple.setIndeps(indepList);
		}

		PmmXmlDoc indepSecDoc = inTuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
		if (indepSecDoc != null) {
			Indep[] indepSecArray = new Indep[indepSecDoc.size()];
			for (int i = 0; i < indepSecDoc.size(); i++) {
				IndepXml indepSecXml = (IndepXml) indepSecDoc.get(i);
				Indep indepSec = new Indep();
				indepSec.name = indepSecXml.name;
				indepSec.origname = indepSecXml.origName;
				indepSec.min = indepSecXml.min;
				indepSec.max = indepSecXml.max;
				indepSec.category = indepSecXml.category;
				indepSec.unit = indepSecXml.unit;
				indepSec.description = indepSecXml.description;
				indepSecArray[i] = indepSec;
			}
			IndepList indepSecList = new IndepList();
			indepSecList.setIndeps(indepSecArray);
			outTuple.setIndepsSec(indepSecList);
		}

		PmmXmlDoc mLitDoc = (PmmXmlDoc) inTuple.getPmmXml(Model1Schema.ATT_MLIT);
		if (mLitDoc != null) {
			Literature[] mLiteratureArray = new Literature[mLitDoc.size()];
			for (int i = 0; i < mLitDoc.size(); i++) {
				LiteratureItem literatureItem = (LiteratureItem) mLitDoc.get(i);
				Literature literature = new Literature();
				literature.setAuthor(literatureItem.author);
				literature.setYear(literatureItem.year);
				literature.setTitle(literatureItem.title);
				literature.setAbstractText(literatureItem.abstractText);
				literature.setJournal(literatureItem.journal);
				literature.setVolume(literatureItem.volume);
				literature.setIssue(literatureItem.issue);
				literature.setPage(literatureItem.page);
				literature.setApprovalMode(literatureItem.approvalMode);
				literature.setWebsite(literatureItem.website);
				literature.setType(literatureItem.type);
				literature.setComment(literatureItem.comment);
				literature.setId(literatureItem.id);
				literature.setDbuuid(literatureItem.dbuuid);

				mLiteratureArray[i] = literature;
			}
			LiteratureList mLiteratureList = new LiteratureList();
			mLiteratureList.setLiterature(mLiteratureArray);
			outTuple.setmLit(mLiteratureList);
		}

		PmmXmlDoc emLitDoc = (PmmXmlDoc) inTuple.getPmmXml(Model1Schema.ATT_EMLIT);
		if (emLitDoc != null) {
			Literature[] emLiteratureArray = new Literature[emLitDoc.size()];
			for (int i = 0; i < emLitDoc.size(); i++) {
				LiteratureItem literatureItem = (LiteratureItem) emLitDoc.get(i);

				Literature literature = new Literature();
				literature.setAuthor(literatureItem.author);
				literature.setYear(literatureItem.year);
				literature.setTitle(literatureItem.title);
				literature.setAbstractText(literatureItem.abstractText);
				literature.setJournal(literatureItem.journal);
				literature.setVolume(literatureItem.volume);
				literature.setIssue(literatureItem.issue);
				literature.setPage(literatureItem.page);
				literature.setApprovalMode(literatureItem.approvalMode);
				literature.setWebsite(literatureItem.website);
				literature.setType(literatureItem.type);
				literature.setComment(literatureItem.comment);
				literature.setId(literatureItem.id);
				literature.setDbuuid(literatureItem.dbuuid);

				emLiteratureArray[i] = literature;
			}
			LiteratureList emLiteratureList = new LiteratureList();
			emLiteratureList.setLiterature(emLiteratureArray);
			outTuple.setEmLit(emLiteratureList);
		}

		if (inTuple.getInt(Model1Schema.ATT_DATABASEWRITABLE) != null)
			outTuple.setDatabaseWritable(Model1Schema.WRITABLE == inTuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		else
			outTuple.setDatabaseWritable(false); // default is false

		return outTuple;
	}

	// TODO: Move to another class: --> Model1DataTuple
	public static KnimeTuple decodeTuple(final Model1DataTuple m1DataTuple) {
		KnimeTuple outTuple = new KnimeTuple(SchemaFactory.createM1DataSchema());

		outTuple.setValue(TimeSeriesSchema.ATT_CONDID, m1DataTuple.getCondId());
		outTuple.setValue(TimeSeriesSchema.ATT_COMBASEID, m1DataTuple.getCombaseId());

		MiscList miscList = m1DataTuple.getMiscList();
		PmmXmlDoc miscDoc = new PmmXmlDoc();
		for (Misc misc : miscList.getMiscs()) {
			miscDoc.add(new MiscXml(misc.getId(), misc.getName(), misc.getDescription(), misc.getValue(),
					Arrays.asList(misc.getCategories()), misc.getUnit(), misc.getOrigUnit(), misc.getDbuuid()));
		}
		outTuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);

		Agent agent = m1DataTuple.getAgent();
		PmmXmlDoc agentDoc = new PmmXmlDoc(new AgentXml(agent.id, agent.name, agent.detail, agent.dbuuid));
		outTuple.setValue(TimeSeriesSchema.ATT_AGENT, agentDoc);

		Matrix matrix = m1DataTuple.getMatrix();
		PmmXmlDoc matrixDoc = new PmmXmlDoc(
				new MatrixXml(matrix.getId(), matrix.getName(), matrix.getDetail(), matrix.getDbuuid()));
		outTuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixDoc);

		PmmXmlDoc timeSeriesDoc = new PmmXmlDoc();
		TimeSeriesList timeSeriesList = m1DataTuple.getTimeSeriesList();
		for (TimeSeries timeSeries : timeSeriesList.getTimeSeries()) {
			timeSeriesDoc.add(new TimeSeriesXml(timeSeries.getName(), timeSeries.getTime(), timeSeries.getTimeUnit(),
					timeSeries.getOrigTimeUnit(), timeSeries.getConcentration(), timeSeries.getConcentrationUnit(),
					timeSeries.getConcentrationUnitObjectType(), timeSeries.getOrigConcentrationUnit(),
					timeSeries.getConcentrationStdDev(), timeSeries.getNumberOfMeasurements()));
		}
		outTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesDoc);

		MdInfo mdInfo = (MdInfo) m1DataTuple.getMdInfo();
		PmmXmlDoc mdInfoDoc = new PmmXmlDoc(new MdInfoXml(mdInfo.getId(), mdInfo.getName(), mdInfo.getComment(),
				mdInfo.getQualityScore(), mdInfo.getChecked()));
		outTuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);

		PmmXmlDoc mdLitDoc = new PmmXmlDoc();
		for (Literature literature : m1DataTuple.getLitMd().getLiterature()) {
			mdLitDoc.add(new LiteratureItem(literature.getAuthor(), literature.getYear(), literature.getTitle(),
					literature.getAbstractText(), literature.getJournal(), literature.getVolume(),
					literature.getIssue(), literature.getPage(), literature.getApprovalMode(), literature.getWebsite(),
					literature.getType(), literature.getComment(), literature.getId(), literature.getDbuuid()));
		}
		outTuple.setValue(TimeSeriesSchema.ATT_LITMD, mdLitDoc);

		outTuple.setValue(TimeSeriesSchema.ATT_DBUUID, m1DataTuple.getDbuuid());
		outTuple.setValue(Model1Schema.ATT_DBUUID, m1DataTuple.getDbuuid());

		CatalogModel catalogModel = m1DataTuple.getCatModel();
		CatalogModelXml catalogModelXml = new CatalogModelXml(catalogModel.id, catalogModel.name, catalogModel.formula,
				catalogModel.modelClass, catalogModel.dbuuid);
		catalogModelXml.comment = catalogModel.comment;
		PmmXmlDoc catalogModelDoc = new PmmXmlDoc(catalogModelXml);
		outTuple.setValue(Model1Schema.ATT_MODELCATALOG, catalogModelDoc);

		EstModel estModel = m1DataTuple.getEstModel();
		EstModelXml estModelXml = new EstModelXml(estModel.id, estModel.name, estModel.sse, estModel.rms, estModel.r2,
				estModel.aic, estModel.bic, estModel.dof, true, estModel.qualityScore, estModel.dbuuid);
		estModelXml.comment = estModel.comment;
		PmmXmlDoc estModelDoc = new PmmXmlDoc(estModelXml);
		outTuple.setValue(Model1Schema.ATT_ESTMODEL, estModelDoc);

		Dep dep = m1DataTuple.getDep();
		DepXml depXml = new DepXml(dep.name, dep.origname, dep.category, dep.unit, dep.description);
		depXml.min = dep.min;
		depXml.max = dep.max;
		outTuple.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));

		PmmXmlDoc paramDoc = new PmmXmlDoc();
		ParamList paramList = m1DataTuple.getParams();
		for (Param param : paramList.getParams()) {
			HashMap<String, Double> correlations = new HashMap<>();
			if (param.getCorrelationNames() != null && param.getCorrelationValues() != null) {
				for (int i = 0; i < param.getCorrelationNames().length; i++) {
					String correlationName = param.getCorrelationNames()[i];
					Double correlationValue = param.getCorrelationValues()[i];
					correlations.put(correlationName, correlationValue);
				}
			}
			ParamXml paramXml = new ParamXml(param.getName(), param.getOrigName(), param.isStart(), param.getValue(),
					param.getError(), param.getMin(), param.getMax(), param.getP(), param.getT(), param.getMinGuess(),
					param.getMaxGuess(), param.getCategory(), param.getUnit(), param.getDescription(), correlations);
			paramDoc.add(paramXml);
		}
		outTuple.setValue(Model1Schema.ATT_PARAMETER, paramDoc);

		PmmXmlDoc indepDoc = new PmmXmlDoc();
		IndepList indepList = m1DataTuple.getIndeps();
		for (Indep indep : indepList.getIndeps()) {
			IndepXml indepXml = new IndepXml(indep.name, indep.origname, indep.min, indep.max, indep.category,
					indep.unit, indep.description);
			indepDoc.add(indepXml);
		}
		outTuple.setValue(Model1Schema.ATT_INDEPENDENT, indepDoc);

		PmmXmlDoc mLitDoc = new PmmXmlDoc();
		for (Literature literature : m1DataTuple.getmLit().getLiterature()) {
			mLitDoc.add(new LiteratureItem(literature.getAuthor(), literature.getYear(), literature.getTitle(),
					literature.getAbstractText(), literature.getJournal(), literature.getVolume(),
					literature.getIssue(), literature.getPage(), literature.getApprovalMode(), literature.getWebsite(),
					literature.getType(), literature.getComment(), literature.getId(), literature.getDbuuid()));
		}
		outTuple.setValue(Model1Schema.ATT_MLIT, mLitDoc);

		PmmXmlDoc emLitDoc = new PmmXmlDoc();
		for (Literature literature : m1DataTuple.getEmLit().getLiterature()) {
			emLitDoc.add(new LiteratureItem(literature.getAuthor(), literature.getYear(), literature.getTitle(),
					literature.getAbstractText(), literature.getJournal(), literature.getVolume(),
					literature.getIssue(), literature.getPage(), literature.getApprovalMode(), literature.getWebsite(),
					literature.getType(), literature.getComment(), literature.getId(), literature.getDbuuid()));
		}
		outTuple.setValue(Model1Schema.ATT_EMLIT, emLitDoc);

		int dbWritableAsInt = m1DataTuple.isDatabaseWritable() ? 1 : 0;
		outTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, dbWritableAsInt);

		return outTuple;
	}

	@Override
	public void setHideInWizard(boolean hide) {
	}

}