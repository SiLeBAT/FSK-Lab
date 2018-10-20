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
		if(miscDoc != null)
		{
			Misc[] miscs = new Misc[miscDoc.size()];
			for (int i = 0; i < miscDoc.size(); i++) {
				MiscXml miscXml = (MiscXml) miscDoc.get(i);
				Misc misc = new Misc();
				misc.setId(miscXml.getId());
				misc.setName(miscXml.getName());
				misc.setDescription(miscXml.getDescription());
				misc.setValue(miscXml.getValue());
				misc.setCategories(miscXml.getCategories().toArray(new String[miscXml.getCategories().size()]));
				misc.setUnit(miscXml.getUnit());
				misc.setOrigUnit(miscXml.getOrigUnit());
				misc.setDbuuid(miscXml.getDbuuid());
				miscs[i] = misc;
			}
			MiscList miscList = new MiscList();
			miscList.setMiscs(miscs);
			outTuple.setMiscList(miscList);
		}

		AgentXml agentXml = (AgentXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0);
		if(agentXml != null)
		{
			Agent agent = new Agent();
			agent.setId(agentXml.id);
			agent.setName(agentXml.name);
			agent.setDetail(agentXml.detail);
			agent.setDbuuid(agentXml.dbuuid);
			outTuple.setAgent(agent);
		}

		MatrixXml matrixXml = (MatrixXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0);
		if(matrixXml != null)
		{
			Matrix matrix = new Matrix();
			matrix.setId(matrixXml.getId());
			matrix.setName(matrixXml.getName());
			matrix.setDetail(matrixXml.getDetail());
			matrix.setDbuuid(matrixXml.getDbuuid());
			outTuple.setMatrix(matrix);
		}

		PmmXmlDoc timeSeriesDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		if(timeSeriesDoc != null)
		{
			TimeSeries[] timeSeriesArray = new TimeSeries[timeSeriesDoc.size()];
			for (int i = 0; i < timeSeriesDoc.size(); i++) {
				TimeSeriesXml timeSeriesXml = (TimeSeriesXml) timeSeriesDoc.get(i);
	
				TimeSeries timeSeries = new TimeSeries();
				timeSeries.setName(timeSeriesXml.getName());
				timeSeries.setTime(timeSeriesXml.getTime());
				timeSeries.setTimeUnit(timeSeriesXml.getTimeUnit());
				timeSeries.setOrigTimeUnit(timeSeriesXml.getOrigTimeUnit());
				timeSeries.setConcentration(timeSeriesXml.getConcentration());
				timeSeries.setConcentrationUnit(timeSeriesXml.getConcentrationUnit());
				timeSeries.setConcentrationUnitObjectType(timeSeriesXml.getConcentrationUnitObjectType());
				timeSeries.setOrigConcentrationUnit(timeSeriesXml.getOrigConcentrationUnit());
				timeSeries.setConcentrationStdDev(timeSeriesXml.getConcentrationStdDev());
				timeSeries.setNumberOfMeasurements(timeSeriesXml.getNumberOfMeasurements());
				timeSeriesArray[i] = timeSeries;
			}
			TimeSeriesList timeSeriesList = new TimeSeriesList();
			timeSeriesList.setTimeSeries(timeSeriesArray);
			outTuple.setTimeSeriesList(timeSeriesList);
		}

		PmmXmlDoc mdInfoAtt = inTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO);
		MdInfoXml mdInfoXml = null;
		if(mdInfoAtt != null)
			mdInfoXml = (MdInfoXml) mdInfoAtt.get(0);
		
		if(mdInfoXml != null)
		{
			MdInfo mdInfo = new MdInfo();
			mdInfo.setId(mdInfoXml.getId());
			mdInfo.setName(mdInfoXml.getName());
			mdInfo.setComment(mdInfoXml.getComment());
			mdInfo.setQualityScore(mdInfoXml.getQualityScore());
			mdInfo.setChecked(mdInfoXml.getChecked());
			outTuple.setMdInfo(mdInfo);
		}

		PmmXmlDoc mdLitDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		if(mdLitDoc != null)
		{
			Literature[] mdLiteratureArray = new Literature[mdLitDoc.size()];
			for (int i = 0; i < mdLitDoc.size(); i++) {
				LiteratureItem literatureItem = (LiteratureItem) mdLitDoc.get(i);
	
				Literature literature = new Literature();
				literature.setAuthor(literatureItem.getAuthor());
				literature.setYear(literatureItem.getYear());
				literature.setTitle(literatureItem.getTitle());
				literature.setAbstractText(literatureItem.getAbstractText());
				literature.setJournal(literatureItem.getJournal());
				literature.setVolume(literatureItem.getVolume());
				literature.setIssue(literatureItem.getIssue());
				literature.setPage(literatureItem.getPage());
				literature.setApprovalMode(literatureItem.getApprovalMode());
				literature.setWebsite(literatureItem.getWebsite());
				literature.setType(literatureItem.getType());
				literature.setComment(literatureItem.getComment());
				literature.setId(literatureItem.getId());
				literature.setDbuuid(literatureItem.getDbuuid());
	
				mdLiteratureArray[i] = literature;
			}
			LiteratureList mdLiteratureList = new LiteratureList();
			mdLiteratureList.setLiterature(mdLiteratureArray);
			outTuple.setLitMd(mdLiteratureList);
		}

		outTuple.setDbuuid(inTuple.getString(TimeSeriesSchema.ATT_DBUUID)); // deprecated?

		CatalogModelXml catalogModelXml = (CatalogModelXml) inTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
		if(catalogModelXml != null)
		{
			CatalogModel catalogModel = new CatalogModel();
			catalogModel.setId(catalogModelXml.id);
			catalogModel.setName(catalogModelXml.name);
			catalogModel.setFormula(catalogModelXml.formula);
			catalogModel.setModelClass(catalogModelXml.modelClass);
			catalogModel.setComment(catalogModelXml.comment);
			catalogModel.setDbuuid(catalogModelXml.dbuuid);
			outTuple.setCatModel(catalogModel);
		}
		
		PmmXmlDoc catalogModelSecAtt = inTuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
		CatalogModelXml catalogModelSecXml = null;
		if(catalogModelSecAtt != null)
			catalogModelSecXml = (CatalogModelXml) catalogModelSecAtt.get(0);
		
		if(catalogModelSecXml != null)
		{
			CatalogModel catalogModelSec = new CatalogModel();
			catalogModelSec.setId(catalogModelSecXml.id);
			catalogModelSec.setName(catalogModelSecXml.name);
			catalogModelSec.setFormula(catalogModelSecXml.formula);
			catalogModelSec.setModelClass(catalogModelSecXml.modelClass);
			catalogModelSec.setComment(catalogModelSecXml.comment);
			catalogModelSec.setDbuuid(catalogModelSecXml.dbuuid);
			outTuple.setCatModelSec(catalogModelSec);
		}

		PmmXmlDoc estModelAtt = inTuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
		EstModelXml estModelXml = null;
		if(estModelAtt != null)
			estModelXml = (EstModelXml) estModelAtt.get(0);
		
		if(estModelXml != null)
		{
			EstModel estModel = new EstModel();
			estModel.setId(estModelXml.getId());
			estModel.setName(estModelXml.getName());
			estModel.setSse(estModelXml.getSse());
			estModel.setRms(estModelXml.getRms());
			estModel.setR2(estModelXml.getR2());
			estModel.setAIC(estModelXml.getAic());
			estModel.setBIC(estModelXml.getBic());
			estModel.setDof(estModelXml.getDof());
			estModel.setQualityScore(estModelXml.getQualityScore());
			estModel.setChecked(estModelXml.getChecked());
			estModel.setComment(estModelXml.getComment());
			estModel.setDbuuid(estModelXml.getDbuuid());
			outTuple.setEstModel(estModel);
		}

		PmmXmlDoc depAtt = inTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
		DepXml depXml = null;
		if(depAtt != null)
			depXml = (DepXml) depAtt.get(0);

		if(depXml != null)
		{
			Dep dep = new Dep();
			dep.setName(depXml.getName());
			dep.setOrigname(depXml.getOrigName());
			dep.setMin(depXml.getMin());
			dep.setMax(depXml.getMax());
			dep.setCategory(depXml.getCategory());
			dep.setUnit(depXml.getUnit());
			dep.setDescription(depXml.getDescription());
			outTuple.setDep(dep);
		}

		PmmXmlDoc paramDoc = inTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		Param[] paramArray = new Param[paramDoc.size()];
		for (int i = 0; i < paramDoc.size(); i++) {
			ParamXml paramXml = (ParamXml) paramDoc.get(i);

			HashMap<String, Double> obtainedCorrelations = paramXml.getAllCorrelations();
			String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
			double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
			int j = 0;
			for (Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
				obtainedCorrelationNames[j] = entry.getKey();
				obtainedCorrelationValues[j] = entry.getValue();
				j++;
			}

			Param param = new Param();
			param.setName(paramXml.getName());
			param.setOrigName(paramXml.getOrigName());
			param.setIsStart(paramXml.isStartParam());
			param.setValue(paramXml.getValue());
			param.setError(paramXml.getError());
			param.setMin(paramXml.getMin());
			param.setMax(paramXml.getMax());
			param.setP(paramXml.getP());
			param.setT(paramXml.getT());
			param.setMinGuess(paramXml.getMinGuess());
			param.setMaxGuess(paramXml.getMaxGuess());
			param.setCategory(paramXml.getCategory());
			param.setUnit(paramXml.getUnit());
			param.setDescription(paramXml.getDescription());
			param.setCorrelationNames(obtainedCorrelationNames);
			param.setCorrelationValues(obtainedCorrelationValues);

			paramArray[i] = param;
		}
		ParamList paramList = new ParamList();
		paramList.setParams(paramArray);
		outTuple.setParams(paramList);
		
		PmmXmlDoc paramSecDoc = inTuple.getPmmXml(Model2Schema.ATT_PARAMETER);
		if(paramSecDoc != null)
		{
			Param[] paramSecArray = new Param[paramSecDoc.size()];
			for (int i = 0; i < paramSecDoc.size(); i++) {
				ParamXml paramSecXml = (ParamXml) paramSecDoc.get(i);
				
				HashMap<String, Double> obtainedCorrelations = paramSecXml.getAllCorrelations();
				String[] obtainedCorrelationNames = new String[obtainedCorrelations.size()];
				double[] obtainedCorrelationValues = new double[obtainedCorrelations.size()];
				int j = 0;
				for (Map.Entry<String, Double> entry : obtainedCorrelations.entrySet()) {
					obtainedCorrelationNames[j] = entry.getKey();
					obtainedCorrelationValues[j] = entry.getValue();
					j++;
				}
				
				Param paramSec = new Param();
				paramSec.setName(paramSecXml.getName());
				paramSec.setOrigName(paramSecXml.getOrigName());
				paramSec.setIsStart(paramSecXml.isStartParam());
				paramSec.setValue(paramSecXml.getValue());
				paramSec.setError(paramSecXml.getError());
				paramSec.setMin(paramSecXml.getMin());
				paramSec.setMax(paramSecXml.getMax());
				paramSec.setP(paramSecXml.getP());
				paramSec.setT(paramSecXml.getT());
				paramSec.setMinGuess(paramSecXml.getMinGuess());
				paramSec.setMaxGuess(paramSecXml.getMaxGuess());
				paramSec.setCategory(paramSecXml.getCategory());
				paramSec.setUnit(paramSecXml.getUnit());
				paramSec.setDescription(paramSecXml.getDescription());
				paramSec.setCorrelationNames(obtainedCorrelationNames);
				paramSec.setCorrelationValues(obtainedCorrelationValues);
				
				paramSecArray[i] = paramSec;
			}
			ParamList paramSecList = new ParamList();
			paramSecList.setParams(paramSecArray);
			outTuple.setParamsSec(paramSecList);
		}

		PmmXmlDoc indepDoc = inTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		if(indepDoc != null)
		{
			Indep[] indepArray = new Indep[indepDoc.size()];
			for (int i = 0; i < indepDoc.size(); i++) {
				IndepXml indepXml = (IndepXml) indepDoc.get(i);
				Indep indep = new Indep();
				indep.setName(indepXml.getName());
				indep.setOrigname(indepXml.getOrigName());
				indep.setMin(indepXml.getMin());
				indep.setMax(indepXml.getMax());
				indep.setCategory(indepXml.getCategory());
				indep.setUnit(indepXml.getUnit());
				indep.setDescription(indepXml.getDescription());
				indepArray[i] = indep;
			}
			IndepList indepList = new IndepList();
			indepList.setIndeps(indepArray);
			outTuple.setIndeps(indepList);
		}
		
		PmmXmlDoc indepSecDoc = inTuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
		if(indepSecDoc != null)
		{
			Indep[] indepSecArray = new Indep[indepSecDoc.size()];
			for (int i = 0; i < indepSecDoc.size(); i++) {
				IndepXml indepSecXml = (IndepXml) indepSecDoc.get(i);
				Indep indepSec = new Indep();
				indepSec.setName(indepSecXml.getName());
				indepSec.setOrigname(indepSecXml.getOrigName());
				indepSec.setMin(indepSecXml.getMin());
				indepSec.setMax(indepSecXml.getMax());
				indepSec.setCategory(indepSecXml.getCategory());
				indepSec.setUnit(indepSecXml.getUnit());
				indepSec.setDescription(indepSecXml.getDescription());
				indepSecArray[i] = indepSec;
			}
			IndepList indepSecList = new IndepList();
			indepSecList.setIndeps(indepSecArray);
			outTuple.setIndepsSec(indepSecList);
		}

		PmmXmlDoc mLitDoc = (PmmXmlDoc) inTuple.getPmmXml(Model1Schema.ATT_MLIT);
		if(mLitDoc != null)
		{
			Literature[] mLiteratureArray = new Literature[mLitDoc.size()];
			for (int i = 0; i < mLitDoc.size(); i++) {
				LiteratureItem literatureItem = (LiteratureItem) mLitDoc.get(i);
				Literature literature = new Literature();
				literature.setAuthor(literatureItem.getAuthor());
				literature.setYear(literatureItem.getYear());
				literature.setTitle(literatureItem.getTitle());
				literature.setAbstractText(literatureItem.getAbstractText());
				literature.setJournal(literatureItem.getJournal());
				literature.setVolume(literatureItem.getVolume());
				literature.setIssue(literatureItem.getIssue());
				literature.setPage(literatureItem.getPage());
				literature.setApprovalMode(literatureItem.getApprovalMode());
				literature.setWebsite(literatureItem.getWebsite());
				literature.setType(literatureItem.getType());
				literature.setComment(literatureItem.getComment());
				literature.setId(literatureItem.getId());
				literature.setDbuuid(literatureItem.getDbuuid());
	
				mLiteratureArray[i] = literature;
			}
			LiteratureList mLiteratureList = new LiteratureList();
			mLiteratureList.setLiterature(mLiteratureArray);
			outTuple.setmLit(mLiteratureList);
		}

		PmmXmlDoc emLitDoc = (PmmXmlDoc) inTuple.getPmmXml(Model1Schema.ATT_EMLIT);
		if(emLitDoc != null)
		{
			Literature[] emLiteratureArray = new Literature[emLitDoc.size()];
			for (int i = 0; i < emLitDoc.size(); i++) {
				LiteratureItem literatureItem = (LiteratureItem) emLitDoc.get(i);
	
				Literature literature = new Literature();
				literature.setAuthor(literatureItem.getAuthor());
				literature.setYear(literatureItem.getYear());
				literature.setTitle(literatureItem.getTitle());
				literature.setAbstractText(literatureItem.getAbstractText());
				literature.setJournal(literatureItem.getJournal());
				literature.setVolume(literatureItem.getVolume());
				literature.setIssue(literatureItem.getIssue());
				literature.setPage(literatureItem.getPage());
				literature.setApprovalMode(literatureItem.getApprovalMode());
				literature.setWebsite(literatureItem.getWebsite());
				literature.setType(literatureItem.getType());
				literature.setComment(literatureItem.getComment());
				literature.setId(literatureItem.getId());
				literature.setDbuuid(literatureItem.getDbuuid());
	
				emLiteratureArray[i] = literature;
			}
			LiteratureList emLiteratureList = new LiteratureList();
			emLiteratureList.setLiterature(emLiteratureArray);
			outTuple.setEmLit(emLiteratureList);
		}

		if(inTuple.getInt(Model1Schema.ATT_DATABASEWRITABLE) != null)
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
		PmmXmlDoc agentDoc = new PmmXmlDoc(
				new AgentXml(agent.getId(), agent.getName(), agent.getDetail(), agent.getDbuuid()));
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
		CatalogModelXml catalogModelXml = new CatalogModelXml(catalogModel.getId(), catalogModel.getName(),
				catalogModel.getFormula(), catalogModel.getModelClass(), catalogModel.getDbuuid());
		catalogModelXml.comment = catalogModel.getComment();
		PmmXmlDoc catalogModelDoc = new PmmXmlDoc(catalogModelXml);
		outTuple.setValue(Model1Schema.ATT_MODELCATALOG, catalogModelDoc);

		EstModel estModel = m1DataTuple.getEstModel();
		EstModelXml estModelXml = new EstModelXml(estModel.getId(), estModel.getName(), estModel.getSse(),
				estModel.getRms(), estModel.getR2(), estModel.getAIC(), estModel.getBIC(), estModel.getDof(), true,
				estModel.getQualityScore(), estModel.getDbuuid());
		estModelXml.setComment(estModel.getComment());
		PmmXmlDoc estModelDoc = new PmmXmlDoc(estModelXml);
		outTuple.setValue(Model1Schema.ATT_ESTMODEL, estModelDoc);

		Dep dep = m1DataTuple.getDep();
		DepXml depXml = new DepXml(dep.getName(), dep.getOrigname(), dep.getCategory(), dep.getUnit(),
				dep.getDescription());
		depXml.setMin(dep.getMin());
		depXml.setMax(dep.getMax());
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
			ParamXml paramXml = new ParamXml(param.getName(), param.getOrigName(), param.isStart(), param.getValue(), param.getError(),
					param.getMin(), param.getMax(), param.getP(), param.getT(), param.getMinGuess(),
					param.getMaxGuess(), param.getCategory(), param.getUnit(), param.getDescription(), correlations);
			paramDoc.add(paramXml);
		}
		outTuple.setValue(Model1Schema.ATT_PARAMETER, paramDoc);

		PmmXmlDoc indepDoc = new PmmXmlDoc();
		IndepList indepList = m1DataTuple.getIndeps();
		for (Indep indep : indepList.getIndeps()) {
			IndepXml indepXml = new IndepXml(indep.getName(), indep.getOrigname(), indep.getMin(), indep.getMax(),
					indep.getCategory(), indep.getUnit(), indep.getDescription());
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