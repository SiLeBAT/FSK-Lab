/*
 ***************************************************************************************************
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;

import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry;
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel;
import de.bund.bfr.rakip.generic.Assay;
import de.bund.bfr.rakip.generic.DataBackground;
import de.bund.bfr.rakip.generic.DietaryAssessmentMethod;
import de.bund.bfr.rakip.generic.GeneralInformation;
import de.bund.bfr.rakip.generic.GenericModel;
import de.bund.bfr.rakip.generic.Hazard;
import de.bund.bfr.rakip.generic.ModelEquation;
import de.bund.bfr.rakip.generic.ModelMath;
import de.bund.bfr.rakip.generic.Parameter;
import de.bund.bfr.rakip.generic.ParameterClassification;
import de.bund.bfr.rakip.generic.PopulationGroup;
import de.bund.bfr.rakip.generic.Product;
import de.bund.bfr.rakip.generic.RakipModule;
import de.bund.bfr.rakip.generic.Scope;
import de.bund.bfr.rakip.generic.Study;
import de.bund.bfr.rakip.generic.StudySample;
import ezvcard.VCard;

/**
 * A port object for an FSK model port providing R scripts and model meta data.
 * 
 * @author Miguel Alba, BfR, Berlin.
 */
public class FskPortObject implements PortObject {

	/**
	 * Convenience access member for
	 * <code>new PortType(FSKPortObject.class)</code>
	 */
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(FskPortObject.class);
	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(FskPortObject.class, true);

	private static final ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle");

	/** Model script. */
	public String model;

	/** Parameters script. */
	public String param;

	/** Visualization script. */
	public String viz;

	/** Model meta data. */
	public GenericModel genericModel;

	/** R workspace file. */
	public File workspace;

	/** R library files. */
	public final Set<File> libs;

	private static int numOfInstances = 0;

	public int objectNum;

	public FskPortObject(final String model, final String param, final String viz, final GenericModel genericModel,
			final File workspace, final Set<File> libs) {
		this.model = model;
		this.param = param;
		this.viz = viz;
		this.genericModel = genericModel;
		this.workspace = workspace;
		this.libs = libs;

		objectNum = numOfInstances;
		numOfInstances += 1;
	}

	@Override
	public FskPortObjectSpec getSpec() {
		return FskPortObjectSpec.INSTANCE;
	}

	@Override
	public String getSummary() {
		return "FSK Object";
	}

	/**
	 * Serializer used to save this port object.
	 * 
	 * @return a {@link FskPortObject}.
	 */
	public static final class Serializer extends PortObjectSerializer<FskPortObject> {

		private static final String MODEL = "model.R";
		private static final String PARAM = "param.R";
		private static final String VIZ = "viz.R";
		private static final String META_DATA = "metaData";
		private static final String WORKSPACE = "workspace";

		private static final ObjectMapper objectMapper = ExtensionsKt.jacksonObjectMapper()
				.registerModule(new RakipModule());

		/** {@inheritDoc} */
		@Override
		public void savePortObject(final FskPortObject portObject, final PortObjectZipOutputStream out,
				final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
			// model entry (file with model script)
			out.putNextEntry(new ZipEntry(MODEL));
			IOUtils.write(portObject.model, out);
			out.closeEntry();

			// param entry (file with param script)
			out.putNextEntry(new ZipEntry(PARAM));
			IOUtils.write(portObject.param, out);
			out.closeEntry();

			// viz entry (file with visualization script)
			out.putNextEntry(new ZipEntry(VIZ));
			IOUtils.write(portObject.viz, out);
			out.closeEntry();

			// template entry (file with model meta data)
			if (portObject.genericModel != null) {
				out.putNextEntry(new ZipEntry(META_DATA));
				final String stringVal = objectMapper.writeValueAsString(portObject.genericModel);
				IOUtils.write(stringVal, out);
				out.closeEntry();
			}

			// workspace entry
			if (portObject.workspace != null) {
				out.putNextEntry(new ZipEntry(WORKSPACE));
				try (FileInputStream fis = new FileInputStream(portObject.workspace)) {
					FileUtil.copy(fis, out);
				}
				out.closeEntry();
			}

			if (!portObject.libs.isEmpty()) {
				out.putNextEntry(new ZipEntry("library.list"));
				List<String> libNames = portObject.libs.stream().map(f -> f.getName().split("\\_")[0])
						.collect(Collectors.toList());
				IOUtils.writeLines(libNames, "\n", out, "UTF-8");
				out.closeEntry();
			}

			out.close();
		}

		@Override
		public FskPortObject loadPortObject(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec)
				throws IOException, CanceledExecutionException {

			// FskPortObject portObj = new FskPortObject();

			String modelScript = "";
			String parametersScript = "";
			String visualizationScript = "";
			GenericModel genericModel = null;
			File workspaceFile = null;
			Set<File> libs = Collections.emptySet();

			ZipEntry entry;
			while ((entry = in.getNextEntry()) != null) {
				String entryName = entry.getName();

				if (entryName.equals(MODEL)) {
					modelScript = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(PARAM)) {
					parametersScript = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(VIZ)) {
					visualizationScript = IOUtils.toString(in, "UTF-8");
				} else if (entryName.equals(META_DATA)) {
					final String metaDataAsString = IOUtils.toString(in, "UTF-8");
					genericModel = objectMapper.readValue(metaDataAsString, GenericModel.class);
				} else if (entryName.equals(WORKSPACE)) {
					workspaceFile = FileUtil.createTempFile("workspace", ".r");
					try (FileOutputStream fos = new FileOutputStream(workspaceFile)) {
						FileUtil.copy(in, fos);
					}
				} else if (entryName.equals("library.list")) {
					List<String> libNames = IOUtils.readLines(in, "UTF-8");

					try {
						LibRegistry libRegistry = LibRegistry.instance();
						// Install missing libraries
						List<String> missingLibs = new LinkedList<>();
						for (String lib : libNames) {
							if (!libRegistry.isInstalled(lib)) {
								missingLibs.add(lib);
							}
						}
						if (!missingLibs.isEmpty()) {
							libRegistry.installLibs(missingLibs);
						}
						// Adds to libs the Paths of the libraries converted to
						// Files
						libRegistry.getPaths(libNames).forEach(p -> libs.add(p.toFile()));
					} catch (RException | REXPMismatchException error) {
						throw new IOException(error.getMessage());
					}
				}
			}

			in.close();

			final FskPortObject portObj = new FskPortObject(modelScript, parametersScript, visualizationScript,
					genericModel, workspaceFile, libs);
			return portObj;
		}
	}

	/** {Override} */
	@Override
	public JComponent[] getViews() {
		JPanel modelScriptPanel = new ScriptPanel("Model script", model, false);
		JPanel paramScriptPanel = new ScriptPanel("Param script", param, false);
		JPanel vizScriptPanel = new ScriptPanel("Visualization script", viz, false);

		final JScrollPane metaDataPane = new JScrollPane(createTree(genericModel));
		metaDataPane.setName("Meta data");

		return new JComponent[] { modelScriptPanel, paramScriptPanel, vizScriptPanel, metaDataPane,
				new LibrariesPanel() };
	}

	/** JPanel with list of R libraries. */
	private class LibrariesPanel extends JPanel {

		private static final long serialVersionUID = -5084804515050256443L;

		LibrariesPanel() {
			super(new BorderLayout());
			setName("Libraries list");

			String[] libNames = new String[libs.size()];
			int i = 0;
			for (File lib : libs) {
				libNames[i] = lib.getName();
				i++;
			}

			JList<String> list = new JList<>(libNames);
			list.setLayoutOrientation(JList.VERTICAL);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			add(new JScrollPane(list));
		}
	}

	// Metadata pane stuff

	private static void add(final DefaultMutableTreeNode node, final String label, final String value) {
		if (StringUtils.isNotBlank(value)) {
			node.add(new DefaultMutableTreeNode(label + ": " + value));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final String label, final Date date) {
		node.add(new DefaultMutableTreeNode(label + ": " + new SimpleDateFormat("yyyy-MM-dd").format(date)));
	}

	private static void add(final DefaultMutableTreeNode node, final Record record) {

		final String prefix = "GM.EditReferencePanel";

		// isReferenceDescription is not supported

		Type recordType = record.getType();
		if (recordType != null) {
			final String key = prefix + "typeLabel";
			final String label = bundle.getString(key);
			final String value = recordType.toString();
			add(node, label, value);
		}

		final String date = record.getDate();
		if (StringUtils.isNotBlank(date)) {
			final String key = prefix + "dateLabel";
			final String label = bundle.getString(key);
			add(node, label, date);
		}

		// PubMedId is not supported

		List<String> authors = record.getAuthors();
		if (authors != null && !authors.isEmpty()) {
			final String key = prefix + "authorListLabel";
			final String label = bundle.getString(key);

			DefaultMutableTreeNode listNode = new DefaultMutableTreeNode(label);
			authors.forEach(it -> add(listNode, "Author", it));
		}

		final String title = record.getTitle();
		if (StringUtils.isNotBlank(title)) {
			final String key = prefix + "titleLabel";
			final String label = bundle.getString(key);
			add(node, label, title);
		}

		final String abstr = record.getAbstr();
		if (StringUtils.isNotBlank(abstr)) {
			final String key = prefix + "abstractLabel";
			final String label = bundle.getString(key);
			add(node, label, abstr);
		}

		final String secondaryTitle = record.getSecondaryTitle();
		if (StringUtils.isNotBlank(secondaryTitle)) {
			final String key = prefix + "journalLabel";
			final String label = bundle.getString(key);
			add(node, label, secondaryTitle);
		}

		final String volumeNumber = record.getVolumeNumber();
		if (StringUtils.isNotBlank(volumeNumber)) {
			final String key = prefix + "volumeLabel";
			final String label = bundle.getString(key);
			add(node, label, volumeNumber);
		}

		final Integer issueNumber = record.getIssueNumber();
		{
			final String key = prefix + "issueLabel";
			final String label = bundle.getString(key);
			final String value = issueNumber.toString();
			add(node, label, value);
		}

		// page not supported

		// status not supported

		final String websiteLink = record.getWebsiteLink();
		if (StringUtils.isNotBlank(websiteLink)) {
			final String key = prefix + "websiteLabel";
			final String label = bundle.getString(key);
			add(node, label, websiteLink);
		}

		// comment not supported
	}

	private static void add(DefaultMutableTreeNode node, VCard vcard) {

		final String prefix = "GM.EditCreatorPanel.";

		final ezvcard.property.Nickname nickname = vcard.getNickname();
		if (nickname != null) {
			final String key = prefix + "givenNameLabel";
			final String label = bundle.getString(key);
			final String value = nickname.toString();
			add(node, label, value);
		}

		final ezvcard.property.FormattedName formattedName = vcard.getFormattedName();
		if (formattedName != null) {
			final String key = prefix + "familyNameLabel";
			final String label = bundle.getString(key);
			final String value = formattedName.toString();
			add(node, label, value);
		}

		if (!vcard.getEmails().isEmpty()) {
			final String key = prefix + "contactLabel";
			final String label = bundle.getString(key);
			final String value = vcard.getEmails().get(0).toString();
			add(node, label, value);
		}
	}

	private static void add(DefaultMutableTreeNode node, Product product) {

		final String prefix = "GM.EditProductPanel.";

		final String envName = product.getEnvironmentName();
		if (StringUtils.isNotBlank(envName)) {
			final String key = prefix + "envNameLabel";
			final String label = bundle.getString(key);
			add(node, label, envName);
		}

		final String envDesc = product.getEnvironmentDescription();
		if (StringUtils.isNotBlank(envDesc)) {
			final String key = prefix + "envDescriptionLabel";
			final String label = bundle.getString(key);
			add(node, label, envDesc);
		}

		final String envUnit = product.getEnvironmentUnit();
		if (StringUtils.isNotBlank(envUnit)) {
			final String key = prefix + "envUnitLabel";
			final String label = bundle.getString(key);
			add(node, label, envUnit);
		}

		final List<String> productionMethods = product.getProductionMethod();
		if (!productionMethods.isEmpty()) {
			// Parent node that holds all the creators
			final String key = prefix + "productionMethodLabel";
			final String label = bundle.getString(key);
			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);

			for (String method : productionMethods) {
				parentNode.add(new DefaultMutableTreeNode(method));
			}
			node.add(parentNode);
		}

		final String originCountry = product.getOriginCountry();
		if (StringUtils.isNotBlank(originCountry)) {
			final String key = prefix + "originCountryLabel";
			final String label = bundle.getString(key);
			add(node, label, originCountry);
		}

		final String areaOfOrigin = product.getAreaOfOrigin();
		if (StringUtils.isNotBlank(areaOfOrigin)) {
			final String label = bundle.getString("GM.EditProductPanel.originAreaLabel");
			add(node, label, areaOfOrigin);
		}

		final String fisheriesArea = product.getFisheriesArea();
		if (StringUtils.isNotBlank(fisheriesArea)) {
			final String key = prefix + "fisheriesAreaLabel";
			final String label = bundle.getString(key);
			add(node, label, fisheriesArea);
		}

		final Date productionDate = product.getProductionDate();
		if (productionDate != null) {
			final String key = prefix + "productionDateLabel";
			final String label = bundle.getString(key);
			add(node, label, productionDate);
		}

		final Date expirationDate = product.getExpirationDate();
		if (expirationDate != null) {
			final String key = prefix + "expirationDateLabel";
			final String label = bundle.getString(key);
			add(node, label, expirationDate);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Hazard hazard) {

		final String prefix = "GM.EditHazardPanel.";

		final String hazardType = hazard.getHazardType();
		if (StringUtils.isNotBlank(hazardType)) {
			final String key = prefix + "hazardTypeLabel";
			final String label = bundle.getString(key);
			add(node, label, hazardType);
		}

		final String hazardName = hazard.getHazardName();
		if (StringUtils.isNotBlank(hazardName)) {
			final String key = prefix + "hazardNameLabel";
			final String label = bundle.getString(key);
			add(node, label, hazardName);
		}

		final String hazardDescription = hazard.getHazardDescription();
		if (StringUtils.isNotBlank(hazardDescription)) {
			final String key = prefix + "hazardDescriptionLabel";
			final String label = bundle.getString(key);
			add(node, label, hazardDescription);
		}

		final String hazardUnit = hazard.getHazardUnit();
		if (StringUtils.isNotBlank(hazardUnit)) {
			final String key = prefix + "hazardUnitLabel";
			final String label = bundle.getString(key);
			add(node, label, hazardUnit);
		}

		final String adverseEffect = hazard.getAdverseEffect();
		if (StringUtils.isNotBlank(adverseEffect)) {
			final String key = prefix + "adverseEffectLabel";
			final String label = bundle.getString(key);
			add(node, label, adverseEffect);
		}

		final String origin = hazard.getOrigin();
		if (StringUtils.isNotBlank(origin)) {
			final String key = prefix + "originLabel";
			final String label = bundle.getString(key);
			add(node, label, origin);
		}

		final String bmd = hazard.getBenchmarkDose();
		if (StringUtils.isNotBlank(bmd)) {
			final String key = prefix + "bmdLabel";
			final String label = bundle.getString(key);
			add(node, label, bmd);
		}

		final String residue = hazard.getMaximumResidueLimit();
		if (StringUtils.isNotBlank(residue)) {
			final String key = prefix + "maxResidueLimitLabel";
			final String label = bundle.getString(key);
			add(node, label, residue);
		}

		final String noAdverse = hazard.getNoObservedAdverse();
		if (StringUtils.isNotBlank(noAdverse)) {
			final String key = prefix + "noObservedAdverseLabel";
			final String label = bundle.getString(key);
			add(node, label, noAdverse);
		}

		final String lowestAdverse = hazard.getLowestObservedAdverse();
		if (StringUtils.isNotBlank(lowestAdverse)) {
			final String key = prefix + "lowestObserveLabel";
			final String label = bundle.getString(key);
			add(node, label, lowestAdverse);
		}

		final String acceptableOperator = hazard.getAcceptableOperator();
		if (StringUtils.isNotBlank(acceptableOperator)) {
			final String key = prefix + "acceptableOperatorLabel";
			final String label = bundle.getString(key);
			add(node, label, acceptableOperator);
		}

		final String acuteReferenceDose = hazard.getAcuteReferenceDose();
		if (StringUtils.isNotBlank(acuteReferenceDose)) {
			final String key = prefix + "acuteReferenceDoseLabel";
			final String label = bundle.getString(key);
			add(node, label, acuteReferenceDose);
		}

		final String acceptableDailyIntake = hazard.getAcceptableDailyIntake();
		if (StringUtils.isNotBlank(acceptableDailyIntake)) {
			final String key = prefix + "acceptableDailyIntake";
			final String label = bundle.getString(key);
			add(node, label, acceptableDailyIntake);
		}

		final String indSum = hazard.getHazardIndSum();
		if (StringUtils.isNotBlank(indSum)) {
			final String key = prefix + "indSumLabel";
			final String label = bundle.getString(key);
			add(node, label, indSum);
		}

		final String labName = hazard.getLaboratoryName();
		if (StringUtils.isNotBlank(labName)) {
			final String key = prefix + "labNameLabel";
			final String label = bundle.getString(key);
			add(node, label, labName);
		}

		final String labCountry = hazard.getLaboratoryCountry();
		if (StringUtils.isNotBlank(labCountry)) {
			final String key = prefix + "labCountryLabel";
			final String label = bundle.getString(key);
			add(node, label, labCountry);
		}

		final String detectionLimit = hazard.getDetectionLimit();
		if (StringUtils.isNotBlank(detectionLimit)) {
			final String key = prefix + "detectionLimitLabel";
			final String label = bundle.getString(key);
			add(node, label, detectionLimit);
		}

		final String quantificationLimit = hazard.getQuantificationLimit();
		if (StringUtils.isNotBlank(quantificationLimit)) {
			final String key = prefix + "quantificationLimitLabel";
			final String label = bundle.getString(key);
			add(node, label, quantificationLimit);
		}

		final String leftCensoredData = hazard.getLeftCensoredData();
		if (StringUtils.isNotBlank(leftCensoredData)) {
			final String key = prefix + "leftCensoredDataLabel";
			final String label = bundle.getString(key);
			add(node, label, leftCensoredData);
		}

		final String contaminationRange = hazard.getRangeOfContamination();
		if (StringUtils.isNotBlank(contaminationRange)) {
			final String key = prefix + "contaminationRangeLabel";
			final String label = bundle.getString(key);
			add(node, label, contaminationRange);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final PopulationGroup populationGroup) {

		final String prefix = "GM.EditPopulationGroupPanel.";

		final String populationName = populationGroup.getPopulationName();
		if (StringUtils.isNotBlank(populationName)) {
			final String key = prefix + "populationNameLabel";
			final String label = bundle.getString(key);
			add(node, label, populationName);
		}

		final List<String> populationSpan = populationGroup.getPopulationSpan();
		if (!populationSpan.isEmpty()) {
			final String key = prefix + "populationSpanLabel";
			final String label = bundle.getString(key);
			final DefaultMutableTreeNode spanNode = new DefaultMutableTreeNode(label);
			populationSpan.forEach(it -> spanNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> populationDesc = populationGroup.getPopulationDescription();
		if (!populationDesc.isEmpty()) {
			final String key = prefix + "populationDescriptionLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode descNode = new DefaultMutableTreeNode(label);
			populationDesc.forEach(it -> descNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> populationAge = populationGroup.getPopulationAge();
		if (!populationAge.isEmpty()) {
			final String key = prefix + "populationAgeLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode ageNode = new DefaultMutableTreeNode(label);
			populationAge.forEach(it -> ageNode.add(new DefaultMutableTreeNode(it)));
		}

		final String gender = populationGroup.getPopulationGender();
		if (StringUtils.isNotBlank(gender)) {
			final String key = prefix + "populationGenderLabel";
			final String label = bundle.getString(key);
			add(node, label, gender);
		}

		final List<String> bmi = populationGroup.getBmi();
		if (!bmi.isEmpty()) {
			final String key = prefix + "bmiLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode bmiNode = new DefaultMutableTreeNode(label);
			bmi.forEach(it -> bmiNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> groups = populationGroup.getSpecialDietGroups();
		if (!groups.isEmpty()) {
			final String key = prefix + "specialDietGroupsLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(label);
			groups.forEach(it -> groupNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> patterns = populationGroup.getPatternConsumption();
		if (!patterns.isEmpty()) {
			final String key = prefix + "patternConsumptionLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode patternNode = new DefaultMutableTreeNode(label);
			patterns.forEach(it -> patternNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> regions = populationGroup.getRegion();
		if (!regions.isEmpty()) {
			final String key = prefix + "regionLabel";
			final String label = bundle.getString(key);
			final DefaultMutableTreeNode regionNode = new DefaultMutableTreeNode(label);
			regions.forEach(it -> regionNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> countries = populationGroup.getCountry();
		if (!countries.isEmpty()) {
			final String key = prefix + "countryLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode countryNode = new DefaultMutableTreeNode(label);
			countries.forEach(it -> countryNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> factors = populationGroup.getPopulationRiskFactor();
		if (!factors.isEmpty()) {
			final String key = prefix + "riskAndPopulationLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode factorNode = new DefaultMutableTreeNode(label);
			factors.forEach(it -> factorNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> seasons = populationGroup.getSeason();
		if (!seasons.isEmpty()) {
			final String key = prefix + "seasonLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode seasonNode = new DefaultMutableTreeNode(label);
			seasons.forEach(it -> seasonNode.add(new DefaultMutableTreeNode(it)));
		}
	}

	private static void add(final DefaultMutableTreeNode node, final GeneralInformation generalInformation) {

		final String prefix = "GM.GeneralInformationPanel.";

		final String name = generalInformation.getName();
		if (StringUtils.isNotBlank(name)) {
			final String key = prefix + "studyNameLabel";
			final String label = bundle.getString(key);
			add(node, label, name);
		}

		final String identifier = generalInformation.getIdentifier();
		if (StringUtils.isNotBlank(identifier)) {
			final String key = prefix + "identifierLabel";
			final String label = bundle.getString(key);
			add(node, label, identifier);
		}

		final List<VCard> creators = generalInformation.getCreators();
		if (!creators.isEmpty()) {
			// Parent node that holds all the creators
			final DefaultMutableTreeNode creatorsNode = new DefaultMutableTreeNode("Creators");

			for (VCard creator : creators) {
				final DefaultMutableTreeNode creatorNode = new DefaultMutableTreeNode("Creator");
				add(creatorNode, creator);
				creatorsNode.add(creatorNode);
			}

			node.add(creatorsNode);
		}

		final Date creationDate = generalInformation.getCreationDate();
		if (creationDate != null) {
			final String key = prefix + "creationDateLabel";
			final String label = bundle.getString(key);
			add(node, label, creationDate);
		}

		final List<Date> modificationDates = generalInformation.getModificationDate();
		if (!modificationDates.isEmpty()) {
			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Modification dates");
			modificationDates.forEach(it -> add(parentNode, "Modification date", it));
		}

		final String rights = generalInformation.getRights();
		if (StringUtils.isNotBlank(rights)) {
			final String key = prefix + "rightsLabel";
			final String label = bundle.getString(key);
			add(node, label, rights);
		}

		// TODO: isAvailable

		final URL url = generalInformation.getUrl();
		if (url != null) {
			final String key = prefix + "urlLabel";
			final String label = bundle.getString(key);
			final String value = url.toString();
			add(node, label, value);
		}

		final List<Record> references = generalInformation.getReference();
		if (!references.isEmpty()) {
			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("References");
			for (Record record : references) {
				final DefaultMutableTreeNode refNode = new DefaultMutableTreeNode("Reference");
				add(refNode, record);
				parentNode.add(refNode);
			}
			node.add(parentNode);
		}

		final String language = generalInformation.getLanguage();
		if (!StringUtils.isNotBlank(language)) {
			final String key = prefix + "languageLabel";
			final String label = bundle.getString(key);
			add(node, label, language);
		}

		final String software = generalInformation.getSoftware();
		if (!StringUtils.isNotBlank(software)) {
			final String key = prefix + "softwareLabel";
			final String label = bundle.getString(key);
			add(node, label, software);
		}

		final String languageWrittenIn = generalInformation.getLanguageWrittenIn();
		if (!StringUtils.isNotBlank(languageWrittenIn)) {
			final String key = prefix + "languageWrittenInLabel";
			final String label = bundle.getString(key);
			add(node, label, languageWrittenIn);
		}

		final String status = generalInformation.getStatus();
		if (!StringUtils.isNotBlank(status)) {
			final String key = prefix + "statusLabel";
			final String label = bundle.getString(key);
			add(node, label, status);
		}

		final String objective = generalInformation.getObjective();
		if (!StringUtils.isNotBlank(objective)) {
			final String key = prefix + "objectiveLabel";
			final String label = bundle.getString(key);
			add(node, label, objective);
		}

		final String desc = generalInformation.getDescription();
		if (!StringUtils.isNotBlank(desc)) {
			final String key = prefix + "descriptionLabel";
			final String label = bundle.getString(key);
			add(node, label, desc);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Scope scope) {

		final String prefix = "GM.ScopePanel.";

		{
			final String key = prefix + "productLabel";
			final String label = bundle.getString(key);

			// Create productNode
			final DefaultMutableTreeNode productNode = new DefaultMutableTreeNode(label);
			final Product product = scope.getProduct();
			if (product != null) {
				add(productNode, product);
			}

			node.add(productNode);
		}

		{
			final String key = prefix + "hazardLabel";
			final String label = bundle.getString(key);

			// Create hazardNode
			final DefaultMutableTreeNode hazardNode = new DefaultMutableTreeNode(label);
			final Hazard hazard = scope.getHazard();
			if (hazard != null) {
				add(hazardNode, hazard);
			}

			node.add(hazardNode);
		}

		{
			final DefaultMutableTreeNode pgNode = new DefaultMutableTreeNode("Population group");
			final PopulationGroup populationGroup = scope.getPopulationGroup();
			if (populationGroup != null) {
				add(pgNode, populationGroup);
			}
			node.add(pgNode);
		}

		final String comment = scope.getGeneralComment();
		if (StringUtils.isNotBlank(comment)) {
			final String key = prefix + "commentLabel";
			final String label = bundle.getString(key);
			add(node, label, comment);
		}

		final String temporalInfo = scope.getTemporalInformation();
		if (StringUtils.isNotBlank(temporalInfo)) {
			final String key = prefix + "temporalInformationLabel";
			final String label = bundle.getString(key);
			add(node, label, temporalInfo);
		}

		final List<String> regions = scope.getRegion();
		if (!regions.isEmpty()) {
			final String key = prefix + "regionLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			regions.forEach(it -> parentNode.add(new DefaultMutableTreeNode(it)));
			node.add(parentNode);
		}

		final List<String> countries = scope.getCountry();
		if (!countries.isEmpty()) {
			final String key = prefix + "countryLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			countries.forEach(it -> parentNode.add(new DefaultMutableTreeNode(it)));
			node.add(parentNode);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final DataBackground dataBackground) {

		final String prefix = "GM.DataBackgroundPanel.";

		final Study study = dataBackground.getStudy();
		if (study != null) {
			final DefaultMutableTreeNode studyNode = new DefaultMutableTreeNode("Study");
			add(studyNode, study);
			node.add(studyNode);
		}

		final StudySample studySample = dataBackground.getStudySample();
		if (studySample != null) {
			final String key = prefix + "studySamplePanel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode sampleNode = new DefaultMutableTreeNode(label);
			add(sampleNode, studySample);
			node.add(sampleNode);
		}

		final DietaryAssessmentMethod dam = dataBackground.getDietaryAssessmentMethod();
		if (dam != null) {
			final String key = prefix + "dietaryAssessmentMethodLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode damNode = new DefaultMutableTreeNode(label);
			add(damNode, dam);
			node.add(damNode);
		}

		final List<String> accreditations = dataBackground.getLaboratoryAccreditation();
		if (!accreditations.isEmpty()) {
			final String key = prefix + "laboratoryAccreditationLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode accNode = new DefaultMutableTreeNode(label);
			accreditations.forEach(it -> accNode.add(new DefaultMutableTreeNode(it)));
			node.add(accNode);
		}

		final Assay assay = dataBackground.getAssay();
		if (assay != null) {
			final String key = prefix + "assayLabel";
			final String label = bundle.getString(key);
			final DefaultMutableTreeNode assayNode = new DefaultMutableTreeNode(label);
			add(assayNode, assay);
			node.add(assayNode);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final Study study) {

		final String prefix = "GM.StudyPanel.";

		final String title = study.getTitle();
		if (StringUtils.isNotBlank(title)) {
			final String key = prefix + "studyTitleLabel";
			final String label = bundle.getString(key);
			add(node, label, title);
		}

		final String desc = study.getDescription();
		if (StringUtils.isNotBlank(desc)) {
			final String key = prefix + "studyDescriptionLabel";
			final String label = bundle.getString(key);
			add(node, label, desc);
		}

		final String designType = study.getDesignType();
		if (StringUtils.isNotBlank(designType)) {
			final String key = prefix + "studyDesignTypeLabel";
			final String label = bundle.getString(key);
			add(node, label, designType);
		}

		final String measurementType = study.getMeasurementType();
		if (StringUtils.isNotBlank(measurementType)) {
			final String key = prefix + "studyAssayMeasurementsTypeLabel";
			final String label = bundle.getString(key);
			add(node, label, measurementType);
		}

		final String techType = study.getTechnologyType();
		if (StringUtils.isNotBlank(techType)) {
			final String key = prefix + "studyAssayTechnologyTypeLabel";
			final String label = bundle.getString(key);
			add(node, label, techType);
		}

		final String techPlat = study.getTechnologyPlatform();
		if (StringUtils.isNotBlank(techPlat)) {
			final String key = prefix + "studyAssayTechnologyPlatformLabel";
			final String label = bundle.getString(key);
			add(node, label, techPlat);
		}

		final String accreditation = study.getAccreditationProcedure();
		if (StringUtils.isNotBlank(accreditation)) {
			final String key = prefix + "accreditationProcedureLabel";
			final String label = bundle.getString(key);
			add(node, label, accreditation);
		}

		final String protocolName = study.getProtocolName();
		if (StringUtils.isNotBlank(protocolName)) {
			final String key = prefix + "protocolNameLabel";
			final String label = bundle.getString(key);
			add(node, label, protocolName);
		}

		final String protocolType = study.getProtocolType();
		if (StringUtils.isNotBlank(protocolType)) {
			final String key = prefix + "protocolTypeLabel";
			final String label = bundle.getString(key);
			add(node, label, protocolType);
		}

		final String protocolDesc = study.getProtocolDescription();
		if (StringUtils.isNotBlank(protocolDesc)) {
			final String key = prefix + "protocolDescriptionLabel";
			final String label = bundle.getString(key);
			add(node, label, protocolDesc);
		}

		final URI protocolUri = study.getProtocolURI();
		if (protocolUri != null) {
			final String key = prefix + "protocolURILabel";
			final String label = bundle.getString(key);
			final String value = protocolUri.toString();
			add(node, label, value);
		}

		final String protocolVersion = study.getProtocolVersion();
		if (StringUtils.isNotEmpty(protocolVersion)) {
			final String key = prefix + "protocolVersionLabel";
			final String label = bundle.getString(key);
			add(node, label, protocolVersion);
		}

		final String params = study.getParametersName();
		if (StringUtils.isNotBlank(params)) {
			final String key = prefix + "parametersLabel";
			final String label = bundle.getString(key);
			add(node, label, params);
		}

		final String componentsType = study.getComponentsType();
		if (StringUtils.isNotBlank(componentsType)) {
			final String key = prefix + "componentsTypeLabel";
			final String label = bundle.getString(key);
			add(node, label, componentsType);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final StudySample studySample) {

		final String prefix = "GM.EditStudySamplePanel.";

		final String sample = studySample.getSample();
		if (StringUtils.isNotBlank(sample)) {
			final String key = prefix + "sampleNameLabel";
			final String label = bundle.getString(key);
			add(node, label, sample);
		}

		final Double moistPerc = studySample.getMoisturePercentage();
		if (moistPerc != null) {
			final String key = prefix + "moisturePercentageLabel";
			final String label = bundle.getString(key);
			final String value = moistPerc.toString();
			add(node, label, value);
		}

		final Double fatPerc = studySample.getFatPercentage();
		if (fatPerc != null) {
			final String key = prefix + "fatPercentageLabel";
			final String label = bundle.getString(key);
			final String value = fatPerc.toString();
			add(node, label, value);
		}

		final String collectionProtocol = studySample.getCollectionProtocol();
		if (StringUtils.isNotBlank(collectionProtocol)) {
			final String key = prefix + "sampleProtocolLabel";
			final String label = bundle.getString(key);
			add(node, label, collectionProtocol);
		}

		final String samplingStrategy = studySample.getSamplingStrategy();
		if (StringUtils.isNotBlank(samplingStrategy)) {
			final String key = prefix + "samplingStrategyLabel";
			final String label = bundle.getString(key);
			add(node, label, samplingStrategy);
		}

		final String samplingMethod = studySample.getSamplingMethod();
		if (StringUtils.isNotBlank(samplingMethod)) {
			final String key = prefix + "samplingMethodLabel";
			final String label = bundle.getString(key);
			add(node, label, samplingMethod);
		}

		final String samplingPlan = studySample.getSamplingPlan();
		if (StringUtils.isNotBlank(samplingPlan)) {
			final String key = prefix + "samplingPlanLabel";
			final String label = bundle.getString(key);
			add(node, label, samplingPlan);
		}

		final String samplingWeight = studySample.getSamplingWeight();
		if (StringUtils.isNotBlank(samplingWeight)) {
			final String key = prefix + "samplingWeightLabel";
			final String label = bundle.getString(key);
			add(node, label, samplingWeight);
		}

		final String samplingSize = studySample.getSamplingWeight();
		if (StringUtils.isNotBlank(samplingSize)) {
			final String key = prefix + "samplingSizeLabel";
			final String label = bundle.getString(key);
			add(node, label, samplingSize);
		}

		final String lotUnit = studySample.getLotSizeUnit();
		if (StringUtils.isNotBlank(lotUnit)) {
			final String key = prefix + "lotSizeUnitLabel";
			final String label = bundle.getString(key);
			add(node, label, lotUnit);
		}

		final String samplingPoint = studySample.getSamplingPoint();
		if (StringUtils.isNotBlank(samplingPoint)) {
			final String key = prefix + "samplingPointLabel";
			final String label = bundle.getString(key);
			add(node, label, samplingPoint);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final DietaryAssessmentMethod method) {

		// Prefix in resource bundle
		final String prefix = "GM.EditDietaryAssessmentMethodPanel.";

		final String collectionTool = method.getCollectionTool();
		if (StringUtils.isNotBlank(collectionTool)) {
			final String key = prefix + "dataCollectionToolLabel";
			final String label = bundle.getString(key);
			add(node, label, collectionTool);
		}

		final int oneDays = method.getNumberOfNonConsecutiveOneDay();
		{
			final String key = prefix + "nonConsecutiveOneDaysLabel";
			final String label = bundle.getString(key);
			final String value = Integer.toString(oneDays);
			add(node, label, value);
		}

		final String softwareTool = method.getSoftwareTool();
		if (StringUtils.isNotBlank(softwareTool)) {
			final String key = prefix + "dietarySoftwareToolLabel";
			final String label = bundle.getString(key);
			add(node, label, softwareTool);
		}

		final List<String> foodItemNumbers = method.getNumberOfFoodItems();
		if (!foodItemNumbers.isEmpty()) {

			final String key = prefix + "foodItemNumberLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			foodItemNumbers.forEach(it -> parentNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> recordTypes = method.getRecordTypes();
		if (!recordTypes.isEmpty()) {
			final String key = prefix + "recordTypeLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			recordTypes.forEach(it -> parentNode.add(new DefaultMutableTreeNode(it)));
		}

		final List<String> foodDescriptors = method.getFoodDescriptors();
		if (!foodDescriptors.isEmpty()) {
			final String key = prefix + "foodDescriptorsLabel";
			final String label = bundle.getString(key);

			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			foodDescriptors.forEach(it -> parentNode.add(new DefaultMutableTreeNode(it)));
		}
	}

	private static void add(final DefaultMutableTreeNode node, Assay assay) {

		final String name = assay.getName();
		if (StringUtils.isNotBlank(name)) {
			final String key = "GM.EditAssayPanel.nameLabel";
			final String label = bundle.getString(key);
			add(node, label, name);
		}

		final String desc = assay.getDescription();
		if (StringUtils.isNotBlank(desc)) {
			final String key = "GM.EditAssayPanel.descriptionLabel";
			final String label = bundle.getString(key);
			add(node, label, desc);
		}
	}

	private static void add(final DefaultMutableTreeNode node, Parameter parameter) {

		final String prefix = "GM.EditParameterPanel.";

		final String id = parameter.getId();
		if (StringUtils.isNotBlank(id)) {
			final String key = prefix + "idLabel";
			final String label = bundle.getString(key);
			add(node, label, id);
		}

		final ParameterClassification clasif = parameter.getClassification();
		if (clasif != null) {
			final String key = prefix + "classificationLabel";
			final String label = bundle.getString(key);
			final String value = clasif.toString();
			add(node, label, value);
		}

		final String name = parameter.getName();
		if (StringUtils.isNotBlank(name)) {
			final String key = prefix + "parameterNameLabel";
			final String label = bundle.getString(key);
			add(node, label, name);
		}

		final String desc = parameter.getDescription();
		if (StringUtils.isNotBlank(desc)) {
			final String key = prefix + "descriptionLabel";
			final String label = bundle.getString(key);
			add(node, label, desc);
		}

		final String unit = parameter.getUnit();
		if (StringUtils.isNotBlank(unit)) {
			final String key = prefix + "unitLabel";
			final String label = bundle.getString(key);
			add(node, label, unit);
		}

		final String unitCategory = parameter.getUnitCategory();
		if (StringUtils.isNotBlank(unitCategory)) {
			final String key = prefix + "unitCategoryLabel";
			final String label = bundle.getString(key);
			add(node, label, unitCategory);
		}

		final String dataType = parameter.getDataType();
		if (StringUtils.isNotBlank(dataType)) {
			final String key = prefix + "dataTypeLabel";
			final String label = bundle.getString(key);
			add(node, label, dataType);
		}

		final String source = parameter.getSource();
		if (StringUtils.isNotBlank(source)) {
			final String key = prefix + "sourceLabel";
			final String label = bundle.getString(key);
			add(node, label, source);
		}

		final String subject = parameter.getSubject();
		if (StringUtils.isNotBlank(subject)) {
			final String key = prefix + "subjectLabel";
			final String label = bundle.getString(key);
			add(node, label, subject);
		}

		final String dist = parameter.getDistribution();
		if (StringUtils.isNotBlank(dist)) {
			final String key = prefix + "distributionLabel";
			final String label = bundle.getString(key);
			add(node, label, dist);
		}

		final String value = parameter.getValue();
		if (StringUtils.isNotBlank(value)) {
			final String key = prefix + "valueLabel";
			final String label = bundle.getString(key);
			add(node, label, value);
		}

		final String reference = parameter.getReference();
		if (StringUtils.isNotBlank(value)) {
			final String key = prefix + "referenceLabel";
			final String label = bundle.getString(key);
			add(node, label, reference);
		}

		final String variabilitySubject = parameter.getVariabilitySubject();
		if (StringUtils.isNotBlank(variabilitySubject)) {
			final String key = prefix + "variabilitySubjectLabel";
			final String label = bundle.getString(key);
			add(node, label, variabilitySubject);
		}

		final List<String> applicabilities = parameter.getModelApplicability();
		if (!applicabilities.isEmpty()) {
			final String key = prefix + "applicabilityLabel";
			final String label = bundle.getString(key);
			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(label);
			applicabilities.forEach(it -> parentNode.add(new DefaultMutableTreeNode(it)));
			node.add(parentNode);
		}

		final Double error = parameter.getError();
		if (error != null) {
			final String key = prefix + "errorLabel";
			final String label = bundle.getString(key);
			add(node, label, error.toString());
		}
	}

	private static void add(final DefaultMutableTreeNode node, final ModelEquation modelEquation) {

		final String prefix = "GM.EditModelEquationPanel.";

		final String name = modelEquation.getEquationName();
		if (StringUtils.isNotBlank(name)) {
			final String key = prefix + "nameLabel";
			final String label = bundle.getString(key);
			add(node, label, name);
		}

		final String equationClass = modelEquation.getEquationClass();
		if (StringUtils.isNotBlank(equationClass)) {
			final String key = prefix + "classLabel";
			final String label = bundle.getString(key);
			add(node, label, equationClass);
		}

		final List<Record> references = modelEquation.getEquationReference();
		if (!references.isEmpty()) {
			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("References");
			for (Record ref : references) {
				final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("Reference");
				add(childNode, ref);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		final String script = modelEquation.getEquation();
		if (StringUtils.isNotBlank(script)) {
			final String key = prefix + "scriptLabel";
			final String label = bundle.getString(key);
			add(node, label, script);
		}
	}

	private static void add(final DefaultMutableTreeNode node, final ModelMath modelMath) {

		List<Parameter> params = modelMath.getParameter();
		if (!params.isEmpty()) {
			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Parameters");
			for (Parameter param : params) {
				final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("Parameter");
				add(childNode, param);
				parentNode.add(childNode);
			}
			node.add(parentNode);
		}

		final Double sse = modelMath.getSse();
		if (sse != null) {
			final String value = sse.toString();
			add(node, "SSE", value);
		}

		final Double mse = modelMath.getMse();
		if (mse != null) {
			final String value = mse.toString();
			add(node, "MSE", value);
		}

		final Double rmse = modelMath.getRmse();
		if (rmse != null) {
			final String value = rmse.toString();
			add(node, "RMSE", value);
		}

		final Double r2 = modelMath.getRSquared();
		if (r2 != null) {
			final String value = r2.toString();
			add(node, "r-Squared", value);
		}

		final Double aic = modelMath.getAic();
		if (aic != null) {
			final String value = aic.toString();
			add(node, "aic", value);
		}

		final Double bic = modelMath.getBic();
		if (bic != null) {
			final String value = bic.toString();
			add(node, "bic", value);
		}

		final ModelEquation modelEquation = modelMath.getModelEquation();
		if (modelEquation != null) {
			final DefaultMutableTreeNode equationNode = new DefaultMutableTreeNode("Model equation");
			add(equationNode, modelEquation);
			node.add(equationNode);
		}

		final String procedure = modelMath.getFittingProcedure();
		if (StringUtils.isNotBlank(procedure)) {
			add(node, "Fitting procedure", procedure);
		}

		// TODO: exposure

		final List<String> events = modelMath.getEvent();
		if (!events.isEmpty()) {
			final DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Events");
			events.forEach(it -> parentNode.add(new DefaultMutableTreeNode(it)));
			node.add(parentNode);
		}
	}

	private static JTree createTree(GenericModel genericModel) {

		final DefaultMutableTreeNode generalInformationNode = new DefaultMutableTreeNode("General information");
		add(generalInformationNode, genericModel.getGeneralInformation());

		final DefaultMutableTreeNode scopeNode = new DefaultMutableTreeNode("Scope");
		add(scopeNode, genericModel.getScope());

		final DefaultMutableTreeNode dataBackgroundNode = new DefaultMutableTreeNode("Data background");
		final DataBackground dataBackground = genericModel.getDataBackground();
		if (dataBackground != null) {
			add(dataBackgroundNode, dataBackground);
		}

		final DefaultMutableTreeNode modelMathNode = new DefaultMutableTreeNode("Model math");
		final ModelMath modelMath = genericModel.getModelMath();
		if (modelMath != null) {
			add(modelMathNode, modelMath);
		}

		final DefaultMutableTreeNode simulationNode = new DefaultMutableTreeNode("Simulation");
		// TODO: simulation

		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		rootNode.add(generalInformationNode);
		rootNode.add(scopeNode);
		rootNode.add(dataBackgroundNode);
		rootNode.add(modelMathNode);
		rootNode.add(simulationNode);

		final JTree tree = new JTree(rootNode);
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		return tree;
	}
}
