/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.js.modelplotter.modern;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.knime.base.data.xml.SvgCell;
import org.knime.base.data.xml.SvgImageContent;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractSVGWizardNodeModel;
import org.osgi.framework.Bundle;
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
import de.bund.bfr.knime.pmm.common.chart.ChartUtilities;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
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
import de.bund.bfr.knime.pmm.js.common.Param;
import de.bund.bfr.knime.pmm.js.common.ParamList;
import de.bund.bfr.knime.pmm.js.common.TimeSeries;
import de.bund.bfr.knime.pmm.js.common.TimeSeriesList;
import de.bund.bfr.knime.pmm.js.common.Unit;
import de.bund.bfr.knime.pmm.js.common.UnitList;
import de.bund.bfr.knime.pmm.js.common.schema.JsM12DataSchema;
import de.bund.bfr.knime.pmm.js.common.schema.JsM12DataSchemaList;
import de.bund.bfr.knime.pmm.js.common.schema.JsM1DataSchema;
import de.bund.bfr.knime.pmm.js.common.schema.JsM1DataSchemaList;
import de.bund.bfr.knime.pmm.js.common.schema.JsM2Schema;
import de.bund.bfr.knime.pmm.js.common.schema.JsM2SchemaList;
import jdk.nashorn.api.scripting.JSObject;

/**
 * Model Plotter node model. Reading all plotables functions of input table and
 * preparing the first plotable for JavaScript view.
 * 
 * @author Kilian Thiel, KNIME.com AG, Berlin, Germany
 * @author Markus Freitag, EITCO GmbH, Berlin, Germany
 *
 */
public final class ModelPlotterNodeModel
		extends AbstractSVGWizardNodeModel<ModelPlotterViewRepresentation, ModelPlotterViewValue> {

	enum MODEL_TYPE {
		M12, M1, M2
	};

	private static final NodeLogger LOGGER = NodeLogger.getLogger(ModelPlotterNodeModel.class);

	static final String FLOWVAR_FUNCTION_ORIG = "Original Function";
	static final String FLOWVAR_FUNCTION_FULL = "Full Function";
	static final String FLOWVAR_FUNCTION_APPLIED = "Applied Function";
	static final String AUTHORS = "authors";
	static final String REPORT_NAME = "reportName";
	static final String COMMENT = "comments";
	static final String SVG_PLOT = "svgPlot";

	private final ModelPlotterViewConfig m_config;
	private boolean m_executed = false;
	private MODEL_TYPE mType = null;

	/**
	 * Constructor of {@code ModelPlotterNodeModel}.
	 */
	protected ModelPlotterNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE },
				new PortType[] { BufferedDataTable.TYPE, BufferedDataTable.TYPE, ImagePortObject.TYPE },
				(new ModelPlotterNodeFactory()).getInteractiveViewName());
		m_config = new ModelPlotterViewConfig();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModelPlotterViewRepresentation createEmptyViewRepresentation() {
		return new ModelPlotterViewRepresentation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModelPlotterViewValue createEmptyViewValue() {
		return new ModelPlotterViewValue();
	}

	@Override
	public String getJavascriptObjectID() {
		return "de.bund.bfr.knime.pmm.js.modelplotter.modern";
	}

	@Override
	public boolean isHideInWizard() {
		return m_config.getHideInwizard();
	}

	@Override
	public ValidationError validateViewValue(ModelPlotterViewValue viewContent) {
		synchronized (getLock()) {
			// Nothing to do.
		}
		return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO content) {
		// Nothing to do.
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		// check input schema
		if (SchemaFactory.conformsM12DataSchema((DataTableSpec) inSpecs[0]))
			mType = MODEL_TYPE.M12;
		else if (SchemaFactory.conformsM1DataSchema((DataTableSpec) inSpecs[0]))
			mType = MODEL_TYPE.M1;
		else if (SchemaFactory.conformsM2Schema((DataTableSpec) inSpecs[0]))
			mType = MODEL_TYPE.M2;
		else {
			LOGGER.error("model schema not supported / unknown data table spec");
			throw new InvalidSettingsException("Wrong input!");
		}

		return createOutputDataTableSpecs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.knime.js.core.node.AbstractWizardNodeModel#performExecute(org.knime.
	 * core.node.port.PortObject[], org.knime.core.node.ExecutionContext)
	 */
	@Override
	protected PortObject[] performExecuteCreatePortObjects(PortObject svgImageFromView, PortObject[] inObjects,
			ExecutionContext exec) throws Exception {

		BufferedDataTable table = (BufferedDataTable) inObjects[0];
		List<KnimeTuple> tuples = getTuples(table);

		ModelPlotterViewValue viewValue = getViewValue();

		if (viewValue == null) {
			viewValue = createEmptyViewValue();
			setViewValue(viewValue);
		}
		viewValue.setModelType(mType);

		if (!m_executed) {
			// Config of JavaScript view
			viewValue.setY0(m_config.getY0());
			viewValue.setAllModelsSelected(m_config.isAllModelAreSelected());
			viewValue.setOnlySecondaryModel(m_config.isSecondaryModel());
			viewValue.setMinXAxis(m_config.getMinXAxis());
			viewValue.setMinYAxis(m_config.getMinYAxis());
			viewValue.setMaxXAxis(m_config.getMaxXAxis());
			viewValue.setMaxYAxis(m_config.getMaxYAxis());

			// create UnitList from DBUnits
			// (this way we can use the units known to the DB and do not have to
			// implement extra JSONType declarations)
			ArrayList<Unit> tempUnitList = new ArrayList<Unit>();
			for (UnitsFromDB dbUnit : DBUnits.getDBUnits().values()) {
				Unit newUnit = new Unit();
				// only copy attributes that are used
				newUnit.display_in_GUI_as = dbUnit.displayInGuiAs;
				newUnit.conversion_function_factor = dbUnit.conversionFunctionFactor;
				newUnit.inverse_conversion_function_factor = dbUnit.inverseConversionFunctionFactor;
				newUnit.name = dbUnit.name;
				newUnit.unit =dbUnit.unit;

				tempUnitList.add(newUnit);
			}
			UnitList unitList = new UnitList();
			unitList.setUnits((Unit[]) tempUnitList.toArray(new Unit[0]));
			// make list available to view
			viewValue.setUnits(unitList);

			setViewValue(viewValue);

			m_executed = true;
		}

		List<?> modelList = null;

		if (mType == MODEL_TYPE.M12) {
			modelList = codeM12DataSchema(tuples);

			// convert list to necessary view list
			JsM12DataSchema[] modelArray = new JsM12DataSchema[modelList.size()];
			modelArray = modelList.toArray(modelArray);

			JsM12DataSchemaList list = new JsM12DataSchemaList();
			list.setModels(modelArray);

			// set new list to view
			viewValue.setM2DataModels(list);
		} else if (mType == MODEL_TYPE.M1) {
			modelList = codeM1DataSchema(tuples);

			// convert list to necessary view list
			JsM1DataSchema[] modelArray = new JsM1DataSchema[modelList.size()];
			modelArray = modelList.toArray(modelArray);

			JsM1DataSchemaList list = new JsM1DataSchemaList();
			list.setModels(modelArray);

			// set new list to view
			viewValue.setM1DataModels(list);
		} else if (mType == MODEL_TYPE.M2) {
			modelList = codeM2Schema(tuples);

			// convert list to necessary view list
			JsM2Schema[] modelArray = new JsM2Schema[modelList.size()];
			modelArray = modelList.toArray(modelArray);

			JsM2SchemaList list = new JsM2SchemaList();
			list.setModels(modelArray);

			// set new list to view
			viewValue.setM2Models(list);
		}

		exec.setProgress(1);

		// return edited table
		BufferedDataContainer container = null;
		List<KnimeTuple> outTuple = null;

		if (mType == MODEL_TYPE.M12) {
			container = exec.createDataContainer(SchemaFactory.createM12DataSchema().createSpec());
			JsM12DataSchemaList outModelList = getViewValue().getM12Models();
			JsM12DataSchema[] resultSchemaArray = outModelList.getSchemas();
			List<JsM12DataSchema> resultList = new ArrayList<JsM12DataSchema>();
			for (JsM12DataSchema jsM12DataSchema : resultSchemaArray) {
				resultList.add(jsM12DataSchema);
			}
			outTuple = decodeM12DataSchemas(resultList);
		} else if (mType == MODEL_TYPE.M1) {
			container = exec.createDataContainer(SchemaFactory.createM1DataSchema().createSpec());
			JsM1DataSchemaList outModelList = getViewValue().getM1Models();
			JsM1DataSchema[] resultDataSchemaArray = outModelList.getSchemas();
			List<JsM1DataSchema> resultList = new ArrayList<JsM1DataSchema>();
			for (JsM1DataSchema jsM1DataDataSchema : resultDataSchemaArray) {
				resultList.add(jsM1DataDataSchema);
			}
			outTuple = decodeM1DataSchemas(resultList);
		} else if (mType == MODEL_TYPE.M2) {
			container = exec.createDataContainer(SchemaFactory.createM2Schema().createSpec());
			JsM2SchemaList outModelList = getViewValue().getM2Models();
			JsM2Schema[] resultSchemaArray = outModelList.getSchemas();
			List<JsM2Schema> resultList = new ArrayList<JsM2Schema>();
			for (JsM2Schema jsM2Schema : resultSchemaArray) {
				resultList.add(jsM2Schema);
			}
			outTuple = decodeM2Schemas(resultList);
		}
		// loading the basic libraries for invoking JavaScript functions from Java
		if (m_config.isHeadLess()) {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			// required to create the basic JavaScript environment which is not available to
			// Nashorn.
			/*
			 * some change are applied to the D3 and Function-plot library to be able to run
			 * in headless mode(Nashorn) updating these files will break the headless
			 * generation of the Plot.
			 */
			engine.eval("load('https://bugs.openjdk.java.net/secure/attachment/11893/env.nashorn.1.2.js')");

			Bundle bundle = Platform.getBundle("de.bund.bfr.knime.pmm.nodes");
			URL pmmPlotter = FileLocator.find(bundle,
					new Path("js-src/de/bund/bfr/knime/pmm/js/modelplotter/pmm-plotter.js"), null);

			pmmPlotter = FileLocator.toFileURL(pmmPlotter);

			ScriptContext context = new SimpleScriptContext();
			context.setBindings(engine.getBindings(ScriptContext.ENGINE_SCOPE), ScriptContext.ENGINE_SCOPE);

			engine.eval(Files.newBufferedReader(Paths.get(URIUtil.toURI(pmmPlotter)), StandardCharsets.UTF_8), context);

			URL d3 = FileLocator.find(bundle, new Path("js-src/de/bund/bfr/knime/pmm/js/modelplotter/d3.js"), null);
			d3 = FileLocator.toFileURL(d3);
			engine.eval(Files.newBufferedReader(Paths.get(URIUtil.toURI(d3)), StandardCharsets.UTF_8), context);

			URL functionPlot = FileLocator.find(bundle,
					new Path("js-src/de/bund/bfr/knime/pmm/js/modelplotter/function-plot.js"), null);
			functionPlot = FileLocator.toFileURL(functionPlot);
			engine.eval(Files.newBufferedReader(Paths.get(URIUtil.toURI(functionPlot)), StandardCharsets.UTF_8),
					context);

			JSObject jSObject = (JSObject) context.getAttribute("pmm_plotter", ScriptContext.ENGINE_SCOPE);
			JSObject init = (JSObject) jSObject.getMember("init");
			init.call(jSObject, null, viewValue, true);
		}

		for (KnimeTuple knimeTuple : outTuple) {
			container.addRowToTable(knimeTuple);
		}
		container.close();

		BufferedDataContainer userContainer = exec.createDataContainer(getUserSpec());
		String reportName = getViewValue().getReportName();
		String authors = getViewValue().getAuthors();
		String comment = getViewValue().getComments();
		String svgPlot = getViewValue().getSvgPlot();
		// adding xmlPrimer and svgPrimer to the generated SVG Text and output the
		// imagePort
		if (svgPlot != null && m_config.isHeadLess()) {
			String xmlPrimer = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
			String svgPrimer = xmlPrimer + "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" "
					+ "\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">";
			String svg = null;
			svg = svgPrimer + svgPlot
					.replaceAll("<rect class=\"clip static-clip\"",
							"<rect  margin=\"10\" width = \"100%\" height = \"100%\" class=\"clip static-clip\"")
					.replaceAll("<rect class=\"zoom-and-drag\"", "");
			InputStream is = new ByteArrayInputStream(svg.getBytes("UTF-8"));
			ImagePortObjectSpec imageSpec = new ImagePortObjectSpec(SvgCell.TYPE);
			ImagePortObject imagePort = null;
			try {
				imagePort = new ImagePortObject(new SvgImageContent(is), imageSpec);
			} catch (IOException e) {
				LOGGER.error("Creating SVG port object failed: " + e.getMessage(), e);
			}
			svgImageFromView = imagePort;
		}
		KnimeSchema userSchema = new KnimeSchema();
		userSchema.addStringAttribute(REPORT_NAME);
		userSchema.addStringAttribute(AUTHORS);
		userSchema.addStringAttribute(COMMENT);
		userSchema.addStringAttribute(SVG_PLOT);

		KnimeTuple userTuple = new KnimeTuple(userSchema);
		userTuple.setValue(REPORT_NAME, reportName);
		userTuple.setValue(AUTHORS, authors);
		userTuple.setValue(COMMENT, comment);
		userTuple.setValue(SVG_PLOT, svgPlot);

		userContainer.addRowToTable(userTuple);
		userContainer.close();

		// TODO: finish output
		return new PortObject[] { container.getTable(), userContainer.getTable(), svgImageFromView };
	}

	private PortObjectSpec[] createOutputDataTableSpecs() {
		if (mType == MODEL_TYPE.M12)
			return new PortObjectSpec[] { SchemaFactory.createM12DataSchema().createSpec(), getUserSpec(),
					ChartUtilities.getImageSpec(true) };
		else if (mType == MODEL_TYPE.M1)
			return new PortObjectSpec[] { SchemaFactory.createM1DataSchema().createSpec(), getUserSpec(),
					ChartUtilities.getImageSpec(true) };
		else // secondary model
			return new PortObjectSpec[] { SchemaFactory.createM2Schema().createSpec(), getUserSpec(),
					ChartUtilities.getImageSpec(true) };
	}

	private DataTableSpec getUserSpec() {
		String[] fields = new String[4];
		fields[0] = REPORT_NAME;
		fields[1] = AUTHORS;
		fields[2] = COMMENT;
		fields[3] = SVG_PLOT;

		DataType[] types = { StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE };
		DataTableSpec userDataSpec = new DataTableSpec(fields, types);
		return userDataSpec;
	}

	@Override
	protected void performReset() {
		m_executed = false;
	}

	@Override
	protected void useCurrentValueAsDefault() {
		// save value #getViewValue() as config for default values after restart
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_config.saveSettings(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		new ModelPlotterViewConfig().loadSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_config.loadSettings(settings);
	}

	private static List<KnimeTuple> getTuples(DataTable table) {
		boolean isTertiaryModel = SchemaFactory.createM12Schema().conforms(table);
		boolean containsData = SchemaFactory.createDataSchema().conforms(table);

		if (isTertiaryModel) {
			if (containsData) {
				return PmmUtilities.getTuples(table, SchemaFactory.createM12DataSchema());
			} else {
				return PmmUtilities.getTuples(table, SchemaFactory.createM12Schema());
			}
		} else {
			boolean isSecondaryModel = SchemaFactory.createM2Schema().conforms(table);
			if (isSecondaryModel) {
				if (containsData) {
					return PmmUtilities.getTuples(table, SchemaFactory.createM12DataSchema());
				} else {
					return PmmUtilities.getTuples(table, SchemaFactory.createM2Schema());
				}
			} else {
				if (containsData) {
					return PmmUtilities.getTuples(table, SchemaFactory.createM1DataSchema());
				} else {
					return PmmUtilities.getTuples(table, SchemaFactory.createM1Schema());
				}
			}

		}
	}

	@Override
	protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("notify of working");

	}

	@Override
	protected boolean generateImage() {
		// always generate image
		return true;
	}

	private String getDbuuid(KnimeTuple tuple) {
		String gid = "";
		if (mType == MODEL_TYPE.M1) {
			gid = tuple.getString(Model1Schema.ATT_DBUUID);

		} else {
			gid = tuple.getString(Model2Schema.ATT_GLOBAL_MODEL_ID);
		}
		if (gid == null || gid.equals("?") || gid.isEmpty()) {
			LOGGER.warn("DATA PROBLEM: No dbuuid given. Random ID will be generated.");
			int seed;
			if (tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG) != null)
				seed = ((CatalogModelXml) tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0)).formula
						.hashCode();
			else
				seed = tuple.hashCode();
			/* "g" for "generated"; max 6 digits */
			gid = "g" + String.valueOf((new Random(seed)).nextInt(999999));
		}

		return gid;
	}

	private List<JsM1DataSchema> codeM1DataSchema(List<KnimeTuple> tuples) {

		List<JsM1DataSchema> schemas = new ArrayList<>(tuples.size());

		for (KnimeTuple tuple : tuples) {

			JsM1DataSchema schema = new JsM1DataSchema();

			// Model1Schema fields
			PmmXmlDoc catalogModelDoc = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			if (catalogModelDoc.size() > 0) {
				CatalogModelXml catalogModelXml = (CatalogModelXml) catalogModelDoc.get(0);
				schema.setCatalogModel(CatalogModel.toCatalogModel(catalogModelXml));
			}

			PmmXmlDoc depDoc = tuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
			if (depDoc.size() > 0) {
				DepXml depXml = (DepXml) depDoc.get(0);
				schema.setDep(Dep.toDep(depXml));
			}

			PmmXmlDoc paramDoc = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			if (paramDoc.size() > 0) {
				Param[] paramArray = new Param[paramDoc.size()];
				for (int i = 0; i < paramDoc.size(); i++) {
					ParamXml paramXml = (ParamXml) paramDoc.get(i);
					paramArray[i] = Param.toParam(paramXml);
				}

				ParamList paramList = new ParamList();
				paramList.setParams(paramArray);
				schema.setParamList(paramList);
			}

			PmmXmlDoc indepDoc = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			if (indepDoc.size() > 0) {
				Indep[] indepArray = new Indep[indepDoc.size()];
				for (int i = 0; i < indepDoc.size(); i++) {
					IndepXml indepXml = (IndepXml) indepDoc.get(i);
					indepArray[i] = Indep.toIndep(indepXml);
				}

				IndepList indepList = new IndepList();
				indepList.setIndeps(indepArray);
				schema.setIndepList(indepList);
			}

			PmmXmlDoc estModelDoc = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
			if (estModelDoc.size() == 1) {
				EstModelXml estModelXml = (EstModelXml) estModelDoc.get(0);
				schema.setEstModel(EstModel.toEstModel(estModelXml));
			}

			PmmXmlDoc mLitDoc = tuple.getPmmXml(Model1Schema.ATT_MLIT);
			if (mLitDoc.size() > 0) {
				Literature[] litArray = new Literature[mLitDoc.size()];
				for (int i = 0; i < mLitDoc.size(); i++) {
					LiteratureItem literatureItem = (LiteratureItem) mLitDoc.get(i);
					litArray[i] = Literature.toLiterature(literatureItem);
				}

				LiteratureList litList = new LiteratureList();
				litList.setLiterature(litArray);
				schema.setmLit(litList);
			}

			PmmXmlDoc emLitDoc = tuple.getPmmXml(Model1Schema.ATT_EMLIT);
			if (emLitDoc.size() > 0) {
				Literature[] litArray = new Literature[emLitDoc.size()];
				for (int i = 0; i < emLitDoc.size(); i++) {
					LiteratureItem literatureItem = (LiteratureItem) emLitDoc.get(i);
					litArray[i] = Literature.toLiterature(literatureItem);
				}

				LiteratureList litList = new LiteratureList();
				litList.setLiterature(litArray);
				schema.setEmLit(litList);
			}

			if (tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE) == null) {
				schema.setDatabaseWritable(false); // default is false
			} else {
				schema.setDatabaseWritable(Model1Schema.WRITABLE == tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
			}
			String BDuuid = getDbuuid(tuple);
			schema.setDbuuid(BDuuid);

			// TimeSeriesSchema fields
			schema.setCondId(tuple.getInt(TimeSeriesSchema.ATT_CONDID));
			schema.setCombaseId(tuple.getString(TimeSeriesSchema.ATT_COMBASEID));

			PmmXmlDoc miscDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
			if (miscDoc.size() > 0) {
				Misc[] miscArray = new Misc[miscDoc.size()];
				for (int i = 0; i < miscDoc.size(); i++) {
					miscArray[i] = Misc.toMisc((MiscXml) miscDoc.get(i));
				}
				MiscList miscList = new MiscList();
				miscList.setMiscs(miscArray);
				schema.setMiscList(miscList);
			}

			PmmXmlDoc agentDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT);
			if (agentDoc.size() == 1) {
				AgentXml agentXml = (AgentXml) agentDoc.get(0);
				schema.setAgent(Agent.toAgent(agentXml));
			}

			PmmXmlDoc matrixDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX);
			if (matrixDoc.size() == 1) {
				MatrixXml matrixXml = (MatrixXml) matrixDoc.get(0);
				schema.setMatrix(Matrix.toMatrix(matrixXml));
			}

			PmmXmlDoc timeSeriesDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			if (timeSeriesDoc.size() > 0) {
				TimeSeries[] timeSeriesArray = new TimeSeries[timeSeriesDoc.size()];
				for (int i = 0; i < timeSeriesDoc.size(); i++) {
					TimeSeriesXml timeSeriesXml = (TimeSeriesXml) timeSeriesDoc.get(i);
					timeSeriesArray[i] = TimeSeries.toTimeSeries(timeSeriesXml);
				}

				TimeSeriesList timeSeriesList = new TimeSeriesList();
				timeSeriesList.setTimeSeries(timeSeriesArray);
				schema.setTimeSeriesList(timeSeriesList);
			}

			PmmXmlDoc mdInfoDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO);
			if (mdInfoDoc.size() == 1) {
				MdInfoXml mdInfoXml = (MdInfoXml) mdInfoDoc.get(0);
				schema.setMdInfo(MdInfo.toMdInfo(mdInfoXml));
			}

			PmmXmlDoc mdLitDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
			if (mdLitDoc.size() > 0) {
				Literature[] litArray = new Literature[mdLitDoc.size()];
				for (int i = 0; i < mdLitDoc.size(); i++) {
					LiteratureItem litItem = (LiteratureItem) mdLitDoc.get(i);
					litArray[i] = Literature.toLiterature(litItem);
				}

				LiteratureList litList = new LiteratureList();
				litList.setLiterature(litArray);
				schema.setLiteratureList(litList);
			}
			schemas.add(schema);
		}

		return schemas; // return lovely schema
	}

	private List<JsM12DataSchema> codeM12DataSchema(List<KnimeTuple> tuples) {

		// sort tuples according to primary models
		// combined model = (Model1Schema + TimeSeriesSchema) + n x Model2Schema
		Map<Integer, List<KnimeTuple>> tertiaryModelMap = new HashMap<>();
		for (KnimeTuple tuple : tuples) {
			int gmId = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

			if (tertiaryModelMap.containsKey(gmId)) {
				// Add tuple to existing tertiary model
				tertiaryModelMap.get(gmId).add(tuple);
			} else {
				// Create tertiary model and add it
				LinkedList<KnimeTuple> tupleList = new LinkedList<>();
				tupleList.add(tuple);
				tertiaryModelMap.put(gmId, tupleList);
			}
		}

		List<JsM12DataSchema> schemas = new ArrayList<>(tertiaryModelMap.size());
		for (List<KnimeTuple> combinedTuple : tertiaryModelMap.values()) {

			JsM12DataSchema schema = new JsM12DataSchema();
			KnimeTuple firstTuple = combinedTuple.get(0);

			// process Model1Schema fields from first tuple
			PmmXmlDoc catalogModelDoc = firstTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			if (catalogModelDoc.size() > 0) {
				CatalogModelXml catalogModelXml = (CatalogModelXml) catalogModelDoc.get(0);
				schema.setCatalogModel(CatalogModel.toCatalogModel(catalogModelXml));
			}

			PmmXmlDoc depDoc = firstTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
			if (depDoc.size() > 0) {
				DepXml depXml = (DepXml) depDoc.get(0);
				schema.setDep(Dep.toDep(depXml));
			}

			PmmXmlDoc paramDoc = firstTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			if (paramDoc.size() > 0) {
				Param[] paramArray = new Param[paramDoc.size()];
				for (int i = 0; i < paramDoc.size(); i++) {
					ParamXml paramXml = (ParamXml) paramDoc.get(i);
					paramArray[i] = Param.toParam(paramXml);
				}

				ParamList paramList = new ParamList();
				paramList.setParams(paramArray);
				schema.setParamList(paramList);
			}

			PmmXmlDoc indepDoc = firstTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			if (indepDoc.size() > 0) {
				Indep[] indepArray = new Indep[indepDoc.size()];
				for (int i = 0; i < indepDoc.size(); i++) {
					IndepXml indepXml = (IndepXml) indepDoc.get(i);
					indepArray[i] = Indep.toIndep(indepXml);
				}

				IndepList indepList = new IndepList();
				indepList.setIndeps(indepArray);
				schema.setIndepList(indepList);
			}

			PmmXmlDoc estModelDoc = firstTuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
			if (estModelDoc.size() == 1) {
				EstModelXml estModelXml = (EstModelXml) estModelDoc.get(0);
				schema.setEstModel(EstModel.toEstModel(estModelXml));
			}

			PmmXmlDoc mLitDoc = firstTuple.getPmmXml(Model1Schema.ATT_MLIT);
			if (mLitDoc.size() > 0) {
				Literature[] litArray = new Literature[mLitDoc.size()];
				for (int i = 0; i < mLitDoc.size(); i++) {
					LiteratureItem literatureItem = (LiteratureItem) mLitDoc.get(i);
					litArray[i] = Literature.toLiterature(literatureItem);
				}

				LiteratureList litList = new LiteratureList();
				litList.setLiterature(litArray);
				schema.setmLit(litList);
			}

			PmmXmlDoc emLitDoc = firstTuple.getPmmXml(Model1Schema.ATT_EMLIT);
			if (emLitDoc.size() > 0) {
				Literature[] litArray = new Literature[emLitDoc.size()];
				for (int i = 0; i < emLitDoc.size(); i++) {
					LiteratureItem literatureItem = (LiteratureItem) emLitDoc.get(i);
					litArray[i] = Literature.toLiterature(literatureItem);
				}

				LiteratureList litList = new LiteratureList();
				litList.setLiterature(litArray);
				schema.setEmLit(litList);
			}

			if (firstTuple.getInt(Model1Schema.ATT_DATABASEWRITABLE) == null) {
				schema.setDatabaseWritable(false); // default is false
			} else {
				boolean dbWritable = Model1Schema.WRITABLE == firstTuple.getInt(Model1Schema.ATT_DATABASEWRITABLE);
				schema.setDatabaseWritable(dbWritable);
			}

			schema.setDbuuid(getDbuuid(firstTuple));

			// process TimeSeriesSchema fields from first tuple
			schema.setCondId(firstTuple.getInt(TimeSeriesSchema.ATT_CONDID));
			schema.setCombaseId(firstTuple.getString(TimeSeriesSchema.ATT_COMBASEID));

			PmmXmlDoc miscDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
			if (miscDoc.size() > 0) {
				Misc[] miscArray = new Misc[miscDoc.size()];
				for (int i = 0; i < miscDoc.size(); i++) {
					miscArray[i] = Misc.toMisc((MiscXml) miscDoc.get(i));
				}
				MiscList miscList = new MiscList();
				miscList.setMiscs(miscArray);
				schema.setMiscList(miscList);
			}

			PmmXmlDoc agentDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT);
			if (agentDoc.size() == 1) {
				AgentXml agentXml = (AgentXml) agentDoc.get(0);
				schema.setAgent(Agent.toAgent(agentXml));
			}

			PmmXmlDoc matrixDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX);
			if (matrixDoc.size() == 1) {
				MatrixXml matrixXml = (MatrixXml) matrixDoc.get(0);
				schema.setMatrix(Matrix.toMatrix(matrixXml));
			}

			PmmXmlDoc timeSeriesDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			if (timeSeriesDoc.size() > 0) {
				TimeSeries[] timeSeriesArray = new TimeSeries[timeSeriesDoc.size()];
				for (int i = 0; i < timeSeriesDoc.size(); i++) {
					TimeSeriesXml timeSeriesXml = (TimeSeriesXml) timeSeriesDoc.get(i);
					timeSeriesArray[i] = TimeSeries.toTimeSeries(timeSeriesXml);
				}

				TimeSeriesList timeSeriesList = new TimeSeriesList();
				timeSeriesList.setTimeSeries(timeSeriesArray);
				schema.setTimeSeriesList(timeSeriesList);
			}

			PmmXmlDoc mdInfoDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO);
			if (mdInfoDoc.size() == 1) {
				MdInfoXml mdInfoXml = (MdInfoXml) mdInfoDoc.get(0);
				schema.setMdInfo(MdInfo.toMdInfo(mdInfoXml));
			}

			PmmXmlDoc mdLitDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
			if (mdLitDoc.size() > 0) {
				Literature[] litArray = new Literature[mdLitDoc.size()];
				for (int i = 0; i < mdLitDoc.size(); i++) {
					LiteratureItem litItem = (LiteratureItem) mdLitDoc.get(i);
					litArray[i] = Literature.toLiterature(litItem);
				}

				LiteratureList litList = new LiteratureList();
				litList.setLiterature(litArray);
				schema.setLiteratureList(litList);
			}

			// process Model2Schema fields from all the tuples
			JsM2Schema[] m2Schemas = new JsM2Schema[combinedTuple.size()];
			for (int j = 0; j < combinedTuple.size(); j++) {
				KnimeTuple atuple = combinedTuple.get(j);
				m2Schemas[j] = new JsM2Schema();

				PmmXmlDoc secCatalogModelDoc = atuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
				if (secCatalogModelDoc.size() > 0) {
					CatalogModelXml catalogModelXml = (CatalogModelXml) secCatalogModelDoc.get(0);
					m2Schemas[j].setCatalogModel(CatalogModel.toCatalogModel(catalogModelXml));
				}

				PmmXmlDoc secDepDoc = atuple.getPmmXml(Model2Schema.ATT_DEPENDENT);
				if (secDepDoc.size() > 0) {
					DepXml depXml = (DepXml) secDepDoc.get(0);
					m2Schemas[j].setDep(Dep.toDep(depXml));
				}

				PmmXmlDoc secParamDoc = atuple.getPmmXml(Model2Schema.ATT_PARAMETER);
				if (secParamDoc.size() > 0) {
					Param[] paramArray = new Param[secParamDoc.size()];
					for (int z = 0; z < secParamDoc.size(); z++) {
						ParamXml paramXml = (ParamXml) secParamDoc.get(z);
						paramArray[z] = Param.toParam(paramXml);
					}

					ParamList paramList = new ParamList();
					paramList.setParams(paramArray);
					m2Schemas[j].setParamList(paramList);
				}

				PmmXmlDoc secIndepDoc = atuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
				if (secIndepDoc.size() > 0) {
					Indep[] indepArray = new Indep[secIndepDoc.size()];
					for (int z = 0; z < secIndepDoc.size(); z++) {
						IndepXml indepXml = (IndepXml) secIndepDoc.get(z);
						indepArray[z] = Indep.toIndep(indepXml);
					}

					IndepList indepList = new IndepList();
					indepList.setIndeps(indepArray);
					m2Schemas[j].setIndepList(indepList);
				}

				PmmXmlDoc secEstModelDoc = atuple.getPmmXml(Model2Schema.ATT_ESTMODEL);
				if (secEstModelDoc.size() == 1) {
					EstModelXml estModelXml = (EstModelXml) secEstModelDoc.get(0);
					m2Schemas[j].setEstModel(EstModel.toEstModel(estModelXml));
				}

				PmmXmlDoc secModelLitDoc = atuple.getPmmXml(Model2Schema.ATT_MLIT);
				if (secModelLitDoc.size() > 0) {
					Literature[] litArray = new Literature[secModelLitDoc.size()];
					for (int i = 0; i < secModelLitDoc.size(); i++) {
						LiteratureItem litItem = (LiteratureItem) secModelLitDoc.get(i);
						litArray[i] = Literature.toLiterature(litItem);
					}

					LiteratureList litList = new LiteratureList();
					litList.setLiterature(litArray);
					m2Schemas[j].setmLit(litList);
				}

				PmmXmlDoc secEstModelLitDoc = atuple.getPmmXml(Model2Schema.ATT_EMLIT);
				if (secEstModelLitDoc.size() > 0) {
					Literature[] litArray = new Literature[secEstModelLitDoc.size()];
					for (int i = 0; i < secEstModelLitDoc.size(); i++) {
						LiteratureItem litItem = (LiteratureItem) secEstModelLitDoc.get(i);
						litArray[i] = Literature.toLiterature(litItem);
					}

					LiteratureList litList = new LiteratureList();
					litList.setLiterature(litArray);
					m2Schemas[j].setEmLit(litList);
				}

				if (atuple.getInt(Model2Schema.ATT_DATABASEWRITABLE) == null) {
					m2Schemas[j].setDatabaseWritable(false); // default is false
				} else {
					boolean dbWritable = Model2Schema.WRITABLE == atuple.getInt(Model2Schema.ATT_DATABASEWRITABLE);
					m2Schemas[j].setDatabaseWritable(dbWritable);
				}

			}

			JsM2SchemaList m2List = new JsM2SchemaList();
			m2List.setModels(m2Schemas);
			schema.setM2List(m2List);
			schemas.add(schema);
		}

		return schemas;
	}

	private List<JsM2Schema> codeM2Schema(List<KnimeTuple> tuples) {
		List<JsM2Schema> schemas = new ArrayList<>(tuples.size());

		for (KnimeTuple tuple : tuples) {

			JsM2Schema schema = new JsM2Schema();

			// Catalog model
			PmmXmlDoc catalogModelDoc = tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG);
			if (catalogModelDoc.size() > 0) {
				CatalogModelXml catalogModelXml = (CatalogModelXml) catalogModelDoc.get(0);
				schema.setCatalogModel(CatalogModel.toCatalogModel(catalogModelXml));
			}

			// Estimated model
			PmmXmlDoc estModelDoc = tuple.getPmmXml(Model2Schema.ATT_ESTMODEL);
			if (estModelDoc.size() > 0) {
				EstModelXml estModelXml = (EstModelXml) estModelDoc.get(0);
				schema.setEstModel(EstModel.toEstModel(estModelXml));
			}

			// Dependent variable
			PmmXmlDoc depDoc = tuple.getPmmXml(Model2Schema.ATT_DEPENDENT);
			if (depDoc.size() > 0) {
				DepXml depXml = (DepXml) depDoc.get(0);
				schema.setDep(Dep.toDep(depXml));
			}

			// Parameters
			PmmXmlDoc paramDoc = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);
			if (paramDoc.size() > 0) {
				Param[] paramArray = new Param[paramDoc.size()];
				for (int i = 0; i < paramDoc.size(); i++) {
					ParamXml paramXml = (ParamXml) paramDoc.get(i);
					paramArray[i] = Param.toParam(paramXml);
				}

				ParamList paramList = new ParamList();
				paramList.setParams(paramArray);
				schema.setParamList(paramList);
			}

			// Independent variables
			PmmXmlDoc indepDoc = tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
			if (indepDoc.size() > 0) {
				Indep[] indepArray = new Indep[indepDoc.size()];
				for (int i = 0; i < indepDoc.size(); i++) {
					IndepXml indepXml = (IndepXml) indepDoc.get(i);
					indepArray[i] = Indep.toIndep(indepXml);
				}

				IndepList indepList = new IndepList();
				indepList.setIndeps(indepArray);
				schema.setIndepList(indepList);
			}

			// Model literature
			PmmXmlDoc mLitDoc = tuple.getPmmXml(Model2Schema.ATT_MLIT);
			if (mLitDoc.size() > 0) {
				Literature[] litArray = new Literature[mLitDoc.size()];
				for (int i = 0; i < mLitDoc.size(); i++) {
					LiteratureItem literatureItem = (LiteratureItem) mLitDoc.get(i);
					litArray[i] = Literature.toLiterature(literatureItem);
				}

				LiteratureList litList = new LiteratureList();
				litList.setLiterature(litArray);
				schema.setmLit(litList);
			}

			// Estimated model literature
			PmmXmlDoc emLitDoc = tuple.getPmmXml(Model2Schema.ATT_EMLIT);
			if (emLitDoc.size() > 0) {
				Literature[] litArray = new Literature[emLitDoc.size()];
				for (int i = 0; i < emLitDoc.size(); i++) {
					LiteratureItem literatureItem = (LiteratureItem) emLitDoc.get(i);
					litArray[i] = Literature.toLiterature(literatureItem);
				}

				LiteratureList litList = new LiteratureList();
				litList.setLiterature(litArray);
				schema.setEmLit(litList);
			}

			// db writable
			Integer dbWritableFromTuple = tuple.getInt(Model2Schema.ATT_DATABASEWRITABLE);
			if (dbWritableFromTuple == null) {
				schema.setDatabaseWritable(false);
			} else {
				schema.setDatabaseWritable(Model2Schema.WRITABLE == dbWritableFromTuple);
			}

			schema.setDbuuid(getDbuuid(tuple));

			// global model id
			Integer gidFromTuple = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			if (gidFromTuple != null) {
				schema.setGlobalModelId(gidFromTuple);
			}

			schemas.add(schema);
		}

		return schemas;
	}

	private List<KnimeTuple> decodeM2Schemas(List<JsM2Schema> schemas) {

		List<KnimeTuple> tuples = new ArrayList<>(schemas.size());

		KnimeSchema knimeSchema = SchemaFactory.createM2Schema();

		for (JsM2Schema schema : schemas) {

			KnimeTuple tuple = new KnimeTuple(knimeSchema);

			// Model catalog
			PmmXmlDoc catalogModelDoc = new PmmXmlDoc();
			if (schema.getCatalogModel() != null) {
				catalogModelDoc.add(schema.getCatalogModel().toCatalogModelXml());
			}
			tuple.setValue(Model2Schema.ATT_MODELCATALOG, catalogModelDoc);

			// Estimated model
			PmmXmlDoc estModelDoc = new PmmXmlDoc();
			if (schema.getEstModel() != null) {
				estModelDoc.add(schema.getEstModel().toEstModelXml());
			}
			tuple.setValue(Model2Schema.ATT_ESTMODEL, estModelDoc);

			// Dependent variable
			PmmXmlDoc depDoc = new PmmXmlDoc();
			if (schema.getDep() != null) {
				depDoc.add(schema.getDep().toDepXml());
			}
			tuple.setValue(Model2Schema.ATT_DEPENDENT, depDoc);

			// Independent variable
			PmmXmlDoc indepDoc = new PmmXmlDoc();
			Arrays.stream(schema.getIndepList().getIndeps()).map(Indep::toIndepXml).forEach(indepDoc::add);
			tuple.setValue(Model2Schema.ATT_INDEPENDENT, indepDoc);

			// Model literature
			PmmXmlDoc mLitDoc = new PmmXmlDoc();
			if (schema.getmLit().getLiterature() != null) {
				Arrays.stream(schema.getmLit().getLiterature()).map(Literature::toLiteratureItem).forEach(mLitDoc::add);
			}
			tuple.setValue(Model2Schema.ATT_MLIT, mLitDoc);

			// Estimated model literature
			PmmXmlDoc emLitDoc = new PmmXmlDoc();
			if (schema.getEmLit().getLiterature() != null) {
				Arrays.stream(schema.getEmLit().getLiterature()).map(Literature::toLiteratureItem)
						.forEach(emLitDoc::add);
			}
			tuple.setValue(Model2Schema.ATT_EMLIT, mLitDoc);

			// Database writable
			Boolean dbWritableFromSchema = schema.getDatabaseWritable();
			if (dbWritableFromSchema == null) {
				tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, 0);
			} else {
				int dbWritable = dbWritableFromSchema ? 1 : 0;
				tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, dbWritable);
			}

			// dbuuid
			tuple.setValue(Model2Schema.ATT_DBUUID, schema.getDbuuid());

			// global model id
			if (schema.getGlobalModelId() != null) {
				tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, schema.getGlobalModelId());
			}

			tuples.add(tuple);
		}

		return tuples;
	}

	private List<KnimeTuple> decodeM1DataSchemas(List<JsM1DataSchema> schemas) {

		List<KnimeTuple> tuples = new ArrayList<>(schemas.size());

		KnimeSchema knimeSchema = SchemaFactory.createM1DataSchema();

		for (JsM1DataSchema schema : schemas) {

			KnimeTuple tuple = new KnimeTuple(knimeSchema);

			// Model1Schema fields
			PmmXmlDoc catalogModelDoc = new PmmXmlDoc();
			if (schema.getCatalogModel() != null) {
				catalogModelDoc.add(schema.getCatalogModel().toCatalogModelXml());
			}
			tuple.setValue(Model1Schema.ATT_MODELCATALOG, catalogModelDoc);

			PmmXmlDoc depDoc = new PmmXmlDoc();
			if (schema.getDep() != null) {
				depDoc.add(schema.getDep().toDepXml());
			}
			tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);

			PmmXmlDoc paramDoc = new PmmXmlDoc();
			for (Param param : schema.getParamList().getParams()) {
				paramDoc.add(param.toParamXml());
			}
			tuple.setValue(Model1Schema.ATT_PARAMETER, paramDoc);

			PmmXmlDoc indepDoc = new PmmXmlDoc();
			for (Indep indep : schema.getIndepList().getIndeps()) {
				indepDoc.add(indep.toIndepXml());
			}

			PmmXmlDoc estModelDoc = new PmmXmlDoc();
			if (schema.getEstModel() != null) {
				estModelDoc.add(schema.getEstModel().toEstModelXml());
			}
			tuple.setValue(Model1Schema.ATT_ESTMODEL, estModelDoc);

			PmmXmlDoc mLitDoc = new PmmXmlDoc();
			if (schema.getmLit().getLiterature() != null)
				for (Literature literature : schema.getmLit().getLiterature()) {
					mLitDoc.add(literature.toLiteratureItem());
				}
			tuple.setValue(Model1Schema.ATT_MLIT, mLitDoc);

			PmmXmlDoc emLitDoc = new PmmXmlDoc();
			if (schema.getEmLit() != null && schema.getEmLit().getLiterature() != null)
				for (Literature literature : schema.getEmLit().getLiterature()) {
					emLitDoc.add(literature.toLiteratureItem());
				}
			tuple.setValue(Model1Schema.ATT_EMLIT, emLitDoc);

			if (schema.getDatabaseWritable() == null) {
				tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, 0);
			} else {
				int dbWritable = schema.getDatabaseWritable() ? 1 : 0;
				tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, dbWritable);
			}

			tuple.setValue(Model1Schema.ATT_DBUUID, schema.getDbuuid());

			// TimeSeriesSchema fields
			tuple.setValue(TimeSeriesSchema.ATT_CONDID, schema.getCondId());
			tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, schema.getCombaseId());

			PmmXmlDoc miscDoc = new PmmXmlDoc();
			if (schema.getMiscList().getMiscs() != null)
				for (Misc misc : schema.getMiscList().getMiscs()) {
					miscDoc.add(misc.toMiscXml());
				}
			tuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);

			PmmXmlDoc agentDoc = new PmmXmlDoc();
			if (schema.getAgent() != null) {
				agentDoc.add(schema.getAgent().toAgentXml());
			}
			tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentDoc);

			PmmXmlDoc matrixDoc = new PmmXmlDoc();
			if (schema.getMatrix() != null) {
				matrixDoc.add(schema.getMatrix().toMatrixXml());
			}
			tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixDoc);

			PmmXmlDoc timeSeriesDoc = new PmmXmlDoc();
			if (schema.getTimeSeriesList() != null && schema.getTimeSeriesList().getTimeSeries() != null)
				for (TimeSeries timeSeries : schema.getTimeSeriesList().getTimeSeries()) {
					timeSeriesDoc.add(timeSeries.toTimeSeriesXml());
				}
			tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesDoc);

			PmmXmlDoc mdInfoDoc = new PmmXmlDoc();
			if (schema.getMdInfo() != null) {
				mdInfoDoc.add(schema.getMdInfo().toMdInfoXml());
			}
			tuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);

			PmmXmlDoc mdLitDoc = new PmmXmlDoc();
			if (schema.getLiteratureList().getLiterature() != null)
				for (Literature literature : schema.getLiteratureList().getLiterature()) {
					mdLitDoc.add(literature.toLiteratureItem());
				}
			tuple.setValue(TimeSeriesSchema.ATT_LITMD, mdLitDoc);

			tuples.add(tuple);
		}

		return tuples;
	}

	private List<KnimeTuple> decodeM12DataSchemas(List<JsM12DataSchema> schemas) {

		List<KnimeTuple> tuples = new LinkedList<>();
		KnimeSchema knimeSchema = SchemaFactory.createM12DataSchema();

		for (JsM12DataSchema schema : schemas) {

			// Take Model1Schema fields

			PmmXmlDoc catalogModelDoc = new PmmXmlDoc();
			if (schema.getCatalogModel() != null) {
				catalogModelDoc.add(schema.getCatalogModel().toCatalogModelXml());
			}

			PmmXmlDoc depDoc = new PmmXmlDoc();
			if (schema.getDep() != null) {
				depDoc.add(schema.getDep().toDepXml());
			}

			PmmXmlDoc paramDoc = new PmmXmlDoc();
			for (Param param : schema.getParamList().getParams()) {
				paramDoc.add(param.toParamXml());
			}

			PmmXmlDoc indepDoc = new PmmXmlDoc();
			for (Indep indep : schema.getIndepList().getIndeps()) {
				indepDoc.add(indep.toIndepXml());
			}

			PmmXmlDoc estModelDoc = new PmmXmlDoc();
			if (schema.getEstModel() != null) {
				estModelDoc.add(schema.getEstModel().toEstModelXml());
			}

			PmmXmlDoc mLitDoc = new PmmXmlDoc();
			if (schema.getmLit().getLiterature() != null)
				for (Literature literature : schema.getmLit().getLiterature()) {
					mLitDoc.add(literature.toLiteratureItem());
				}

			PmmXmlDoc emLitDoc = new PmmXmlDoc();
			if (schema.getEmLit().getLiterature() != null)
				for (Literature literature : schema.getEmLit().getLiterature()) {
					emLitDoc.add(literature.toLiteratureItem());
				}

			int dbWritable;
			if (schema.getDatabaseWritable() == null) {
				dbWritable = 0;
			} else {
				dbWritable = schema.getDatabaseWritable() ? 1 : 0;
			}

			String dbuuid = schema.getDbuuid();

			// Take TimeSeriesSchema fields
			Integer condId = schema.getCondId();
			String combaseId = schema.getCombaseId();

			PmmXmlDoc miscDoc = new PmmXmlDoc();
			if (schema.getMiscList().getMiscs() != null)
				for (Misc misc : schema.getMiscList().getMiscs()) {
					miscDoc.add(misc.toMiscXml());
				}

			PmmXmlDoc agentDoc = new PmmXmlDoc();
			if (schema.getAgent() != null) {
				agentDoc.add(schema.getAgent().toAgentXml());
			}

			PmmXmlDoc matrixDoc = new PmmXmlDoc();
			if (schema.getMatrix() != null) {
				matrixDoc.add(schema.getMatrix().toMatrixXml());
			}

			PmmXmlDoc timeSeriesDoc = new PmmXmlDoc();
			if (schema.getTimeSeriesList().getTimeSeries() != null)
				for (TimeSeries timeSeries : schema.getTimeSeriesList().getTimeSeries()) {
					timeSeriesDoc.add(timeSeries.toTimeSeriesXml());
				}

			PmmXmlDoc mdInfoDoc = new PmmXmlDoc();
			if (schema.getMdInfo() != null) {
				mdInfoDoc.add(schema.getMdInfo().toMdInfoXml());
			}

			PmmXmlDoc mdLitDoc = new PmmXmlDoc();
			if (schema.getLiteratureList().getLiterature() != null)
				for (Literature literature : schema.getLiteratureList().getLiterature()) {
					mdLitDoc.add(literature.toLiteratureItem());
				}

			for (JsM2Schema m2Schema : schema.getM2List().getSchemas()) {
				// Build actual KnimeTuple
				KnimeTuple tuple = new KnimeTuple(knimeSchema);

				// copy Model1Schema fields
				tuple.setValue(Model1Schema.ATT_MODELCATALOG, catalogModelDoc);
				tuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);
				tuple.setValue(Model1Schema.ATT_PARAMETER, paramDoc);
				tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepDoc);
				tuple.setValue(Model1Schema.ATT_ESTMODEL, estModelDoc);
				tuple.setValue(Model1Schema.ATT_MLIT, mLitDoc);
				tuple.setValue(Model1Schema.ATT_EMLIT, emLitDoc);
				tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, dbWritable);
				tuple.setValue(Model1Schema.ATT_DBUUID, dbuuid);

				// copy TimeSeriesSchema fields
				tuple.setValue(TimeSeriesSchema.ATT_CONDID, condId);
				tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseId);
				tuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);
				tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentDoc);
				tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixDoc);
				tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesDoc);
				tuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);
				tuple.setValue(TimeSeriesSchema.ATT_LITMD, mdLitDoc);

				// take and copy Model2Schema fields
				PmmXmlDoc secCatModelDoc = new PmmXmlDoc();
				if (m2Schema.getCatalogModel() != null) {
					secCatModelDoc.add(m2Schema.getCatalogModel().toCatalogModelXml());
				}
				tuple.setValue(Model2Schema.ATT_MODELCATALOG, secCatModelDoc);

				PmmXmlDoc secDepDoc = new PmmXmlDoc();
				if (m2Schema.getDep() != null) {
					secDepDoc.add(m2Schema.getDep().toDepXml());
				}
				tuple.setValue(Model2Schema.ATT_DEPENDENT, secDepDoc);

				PmmXmlDoc secParamDoc = new PmmXmlDoc();
				if (m2Schema.getParamList().getParams() != null)
					for (Param param : m2Schema.getParamList().getParams()) {
						secParamDoc.add(param.toParamXml());
					}
				tuple.setValue(Model2Schema.ATT_PARAMETER, secParamDoc);

				PmmXmlDoc secIndepDoc = new PmmXmlDoc();
				if (m2Schema.getIndepList().getIndeps() != null)
					for (Indep indep : m2Schema.getIndepList().getIndeps()) {
						secIndepDoc.add(indep.toIndepXml());
					}
				tuple.setValue(Model2Schema.ATT_INDEPENDENT, secIndepDoc);

				PmmXmlDoc secEstModelDoc = new PmmXmlDoc();
				if (m2Schema.getEstModel() != null) {
					secEstModelDoc.add(m2Schema.getEstModel().toEstModelXml());
				}
				tuple.setValue(Model2Schema.ATT_ESTMODEL, secEstModelDoc);

				PmmXmlDoc secMLitDoc = new PmmXmlDoc();
				if (m2Schema.getmLit().getLiterature() != null)
					for (Literature literature : m2Schema.getmLit().getLiterature()) {
						secMLitDoc.add(literature.toLiteratureItem());
					}
				tuple.setValue(Model2Schema.ATT_MLIT, secMLitDoc);

				PmmXmlDoc secEmLitDoc = new PmmXmlDoc();
				if (m2Schema.getEmLit().getLiterature() != null)
					for (Literature literature : m2Schema.getEmLit().getLiterature()) {
						secEmLitDoc.add(literature.toLiteratureItem());
					}
				tuple.setValue(Model2Schema.ATT_EMLIT, secEmLitDoc);

				tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, dbWritable);
				tuple.setValue(Model2Schema.ATT_DBUUID, dbuuid);

				// save KnimeTuple into tuples
				tuples.add(tuple);
			}
		}

		return tuples;
	}

	@Override
	public void setHideInWizard(boolean hide) {
	}
}
