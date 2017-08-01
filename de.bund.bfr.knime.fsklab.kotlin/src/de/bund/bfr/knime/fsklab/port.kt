/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.gmail.gcolaianni5.jris.bean.Record
import de.bund.bfr.knime.fsklab.nodes.controller.LibRegistry
import de.bund.bfr.knime.fsklab.nodes.ui.ScriptPanel
import de.bund.bfr.rakip.ASSAY_DESC
import de.bund.bfr.rakip.ASSAY_NAME
import de.bund.bfr.rakip.CREATOR_CONTACT
import de.bund.bfr.rakip.CREATOR_FAMILYNAME
import de.bund.bfr.rakip.CREATOR_GIVENNAME
import de.bund.bfr.rakip.DAM_FOOD_ITEM
import de.bund.bfr.rakip.DAM_NUMBER_NON_CONSECUTIVE
import de.bund.bfr.rakip.DAM_RECORD_TYPE
import de.bund.bfr.rakip.DAM_SOFTWARE
import de.bund.bfr.rakip.DAM_TOOL
import de.bund.bfr.rakip.DB_ACCREDITATION
import de.bund.bfr.rakip.DB_ASSAY
import de.bund.bfr.rakip.DB_DIETARYASSESSMENTMETHOD
import de.bund.bfr.rakip.DB_STUDYSAMPLE
import de.bund.bfr.rakip.GI_CREATION_DATE
import de.bund.bfr.rakip.GI_DESC
import de.bund.bfr.rakip.GI_FORMAT
import de.bund.bfr.rakip.GI_ID
import de.bund.bfr.rakip.GI_LANGUAGE
import de.bund.bfr.rakip.GI_LANGUAGE_WRITTEN_IN
import de.bund.bfr.rakip.GI_OBJECTIVE
import de.bund.bfr.rakip.GI_RIGHTS
import de.bund.bfr.rakip.GI_SOFTWARE
import de.bund.bfr.rakip.GI_STATUS
import de.bund.bfr.rakip.GI_STUDY_NAME
import de.bund.bfr.rakip.GI_URL
import de.bund.bfr.rakip.HAZARD_ACCEPTABLEOPERATOR
import de.bund.bfr.rakip.HAZARD_ACUTEDOSE
import de.bund.bfr.rakip.HAZARD_ADVERSE
import de.bund.bfr.rakip.HAZARD_BMD
import de.bund.bfr.rakip.HAZARD_CENSOREDDATA
import de.bund.bfr.rakip.HAZARD_CONTAMINATION
import de.bund.bfr.rakip.HAZARD_DAILYINTAKE
import de.bund.bfr.rakip.HAZARD_DESCRIPTION
import de.bund.bfr.rakip.HAZARD_DETECTIONLIM
import de.bund.bfr.rakip.HAZARD_INDSUM
import de.bund.bfr.rakip.HAZARD_LABCOUNTRY
import de.bund.bfr.rakip.HAZARD_LABNAME
import de.bund.bfr.rakip.HAZARD_LOWESTADVERSE
import de.bund.bfr.rakip.HAZARD_NAME
import de.bund.bfr.rakip.HAZARD_NOADVERSE
import de.bund.bfr.rakip.HAZARD_ORIGIN
import de.bund.bfr.rakip.HAZARD_QUANTIFICATIONLIM
import de.bund.bfr.rakip.HAZARD_RESIDUE
import de.bund.bfr.rakip.HAZARD_TYPE
import de.bund.bfr.rakip.HAZARD_UNIT
import de.bund.bfr.rakip.MODEL_EQUATION_CLASS
import de.bund.bfr.rakip.MODEL_EQUATION_NAME
import de.bund.bfr.rakip.MODEL_EQUATION_SCRIPT
import de.bund.bfr.rakip.PARAMETER_APPLICABILITY
import de.bund.bfr.rakip.PARAMETER_CLASIF
import de.bund.bfr.rakip.PARAMETER_DATA_TYPE
import de.bund.bfr.rakip.PARAMETER_DESC
import de.bund.bfr.rakip.PARAMETER_DIST
import de.bund.bfr.rakip.PARAMETER_ERROR
import de.bund.bfr.rakip.PARAMETER_ID
import de.bund.bfr.rakip.PARAMETER_NAME
import de.bund.bfr.rakip.PARAMETER_REFERENCE
import de.bund.bfr.rakip.PARAMETER_SOURCE
import de.bund.bfr.rakip.PARAMETER_SUBJECT
import de.bund.bfr.rakip.PARAMETER_UNIT
import de.bund.bfr.rakip.PARAMETER_UNIT_CATEGORY
import de.bund.bfr.rakip.PARAMETER_VALUE
import de.bund.bfr.rakip.PARAMETER_VARIABILITY
import de.bund.bfr.rakip.PG_AGE
import de.bund.bfr.rakip.PG_BMI
import de.bund.bfr.rakip.PG_COUNTRY
import de.bund.bfr.rakip.PG_DESC
import de.bund.bfr.rakip.PG_DIETGROUPS
import de.bund.bfr.rakip.PG_GENDER
import de.bund.bfr.rakip.PG_NAME
import de.bund.bfr.rakip.PG_PATTERNCONSUMPTION
import de.bund.bfr.rakip.PG_REGION
import de.bund.bfr.rakip.PG_RISK
import de.bund.bfr.rakip.PG_SEASON
import de.bund.bfr.rakip.PG_SPAN
import de.bund.bfr.rakip.PG_TARGET
import de.bund.bfr.rakip.PRODUCT_DESC
import de.bund.bfr.rakip.PRODUCT_EXPIRATIONDATE
import de.bund.bfr.rakip.PRODUCT_FISHERIES
import de.bund.bfr.rakip.PRODUCT_NAME
import de.bund.bfr.rakip.PRODUCT_ORIGINAREA
import de.bund.bfr.rakip.PRODUCT_ORIGINCOUNTRY
import de.bund.bfr.rakip.PRODUCT_PACKAGING
import de.bund.bfr.rakip.PRODUCT_PRODUCTIONDATE
import de.bund.bfr.rakip.PRODUCT_PRODUCTIONMETHOD
import de.bund.bfr.rakip.PRODUCT_TREATMENT
import de.bund.bfr.rakip.PRODUCT_UNIT
import de.bund.bfr.rakip.REFERENCE_ABSTRACT
import de.bund.bfr.rakip.REFERENCE_AUTHORLIST
import de.bund.bfr.rakip.REFERENCE_DATE
import de.bund.bfr.rakip.REFERENCE_DOI
import de.bund.bfr.rakip.REFERENCE_ISSUE
import de.bund.bfr.rakip.REFERENCE_JOURNAL
import de.bund.bfr.rakip.REFERENCE_TITLE
import de.bund.bfr.rakip.REFERENCE_TYPE
import de.bund.bfr.rakip.REFERENCE_VOLUME
import de.bund.bfr.rakip.REFERENCE_WEBSITE
import de.bund.bfr.rakip.SCOPE_COMMENT
import de.bund.bfr.rakip.SCOPE_COUNTRY
import de.bund.bfr.rakip.SCOPE_HAZARD
import de.bund.bfr.rakip.SCOPE_PRODUCT
import de.bund.bfr.rakip.SCOPE_REGION
import de.bund.bfr.rakip.SCOPE_TEMPORAL
import de.bund.bfr.rakip.SS_FAT_PERC
import de.bund.bfr.rakip.SS_LOT_UNIT
import de.bund.bfr.rakip.SS_MOISTURE_PERC
import de.bund.bfr.rakip.SS_SAMPLE
import de.bund.bfr.rakip.SS_SAMPLE_PROTOCOL
import de.bund.bfr.rakip.SS_SAMPLING_METHOD
import de.bund.bfr.rakip.SS_SAMPLING_PLAN
import de.bund.bfr.rakip.SS_SAMPLING_POINT
import de.bund.bfr.rakip.SS_SAMPLING_SIZE
import de.bund.bfr.rakip.SS_SAMPLING_STRATEGY
import de.bund.bfr.rakip.SS_SAMPLING_TYPE
import de.bund.bfr.rakip.SS_SAMPLING_WEIGHT
import de.bund.bfr.rakip.STUDY_ACCREDITATION
import de.bund.bfr.rakip.STUDY_COMPONENTS_TYPE
import de.bund.bfr.rakip.STUDY_DESC
import de.bund.bfr.rakip.STUDY_DESIGN
import de.bund.bfr.rakip.STUDY_MEASUREMENT
import de.bund.bfr.rakip.STUDY_PARAMETERS
import de.bund.bfr.rakip.STUDY_PROTOCOL_DESC
import de.bund.bfr.rakip.STUDY_PROTOCOL_NAME
import de.bund.bfr.rakip.STUDY_PROTOCOL_TYPE
import de.bund.bfr.rakip.STUDY_PROTOCOL_URI
import de.bund.bfr.rakip.STUDY_PROTOCOL_VERSION
import de.bund.bfr.rakip.STUDY_TECH_PLAT
import de.bund.bfr.rakip.STUDY_TECH_TYPE
import de.bund.bfr.rakip.STUDY_TITLE
import de.bund.bfr.rakip.editor.DataBackgroundPanel
import de.bund.bfr.rakip.editor.GeneralInformationPanel
import de.bund.bfr.rakip.editor.ModelMathPanel
import de.bund.bfr.rakip.editor.ScopePanel
import de.bund.bfr.rakip.generic.Assay
import de.bund.bfr.rakip.generic.DataBackground
import de.bund.bfr.rakip.generic.DietaryAssessmentMethod
import de.bund.bfr.rakip.generic.GeneralInformation
import de.bund.bfr.rakip.generic.GenericModel
import de.bund.bfr.rakip.generic.Hazard
import de.bund.bfr.rakip.generic.ModelEquation
import de.bund.bfr.rakip.generic.ModelMath
import de.bund.bfr.rakip.generic.Parameter
import de.bund.bfr.rakip.generic.PopulationGroup
import de.bund.bfr.rakip.generic.Product
import de.bund.bfr.rakip.generic.RakipModule
import de.bund.bfr.rakip.generic.Scope
import de.bund.bfr.rakip.generic.Study
import de.bund.bfr.rakip.generic.StudySample
import ezvcard.VCard
import org.apache.commons.io.IOUtils
import org.knime.core.node.ExecutionMonitor
import org.knime.core.node.port.PortObject
import org.knime.core.node.port.PortObject.PortObjectSerializer
import org.knime.core.node.port.PortObjectSpec
import org.knime.core.node.port.PortObjectSpec.PortObjectSpecSerializer
import org.knime.core.node.port.PortObjectSpecZipInputStream
import org.knime.core.node.port.PortObjectSpecZipOutputStream
import org.knime.core.node.port.PortObjectZipInputStream
import org.knime.core.node.port.PortObjectZipOutputStream
import org.knime.core.node.port.PortTypeRegistry
import org.knime.core.util.FileUtil
import java.awt.BorderLayout
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.zip.ZipEntry
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.ListSelectionModel
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeSelectionModel

/**
 * A port object spec for R model port.
 * @author Miguel de Alba, BfR, Berlin.
 */
class FskPortObjectSpec private constructor() : PortObjectSpec {

	companion object {
		val INSTANCE = FskPortObjectSpec()
	}

	override fun getViews() = emptyArray<JComponent>()

	fun getPortObjectSpecSerializer() = Serializer()

	class Serializer : PortObjectSpecSerializer<FskPortObjectSpec>() {
		override fun loadPortObjectSpec(stream: PortObjectSpecZipInputStream) = INSTANCE
		override fun savePortObjectSpec(spec: FskPortObjectSpec, stream: PortObjectSpecZipOutputStream) = Unit
	}
}


/**
 * A port object for an FSK model port providing R scripts and model meta data.
 * @property model Model script
 * @property param Parameters script
 * @property viz Visualization script
 * @property genericModel Model meta data
 * @property workspace  R workspace file
 * @property libs R library files

 * @author Miguel de Alba, BfR, Berlin.
 */
class FskPortObject(var model: String = "", var param: String = "", var viz: String = "",
					var genericModel: GenericModel, var workspace: File? = null,
					val libs: MutableSet<File> = mutableSetOf()) : PortObject {

	companion object {
		val TYPE = PortTypeRegistry.getInstance().getPortType(FskPortObject::class.java)
		val TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(FskPortObject::class.java, true)

		private var numOfInstances = 0
	}

	val objectNum = numOfInstances

	init {
		numOfInstances++
	}

	override fun getSpec() = FskPortObjectSpec.INSTANCE

	override fun getSummary() = "FSK Object"

	override fun getViews(): Array<JComponent> {

		val modelScriptPanel = ScriptPanel("Model script", model, false)
		val paramScriptPanel = ScriptPanel("Param script", param, false)
		val vizScriptPanel = ScriptPanel("Visualization script", viz, false)

		val generalInformationPanel = GeneralInformationPanel(genericModel.generalInformation)
		generalInformationPanel.name = "General information"

		val scopePanel = ScopePanel(genericModel.scope)
		scopePanel.name = "Scope"

		val dataBackgroundPanel = DataBackgroundPanel(genericModel.dataBackground)
		dataBackgroundPanel.name = "Data background"

		val modelMathPanel = ModelMathPanel(genericModel.modelMath)
		modelMathPanel.name = "Model math"

		val metaDataPane = JScrollPane(createTree(genericModel = genericModel))
		metaDataPane.name = "Meta data"

		return arrayOf(modelScriptPanel, paramScriptPanel, vizScriptPanel, metaDataPane, LibrariesPanel())
	}

	/** JPanel with list of R libraries. */
	private inner class LibrariesPanel : JPanel(BorderLayout()) {

		init {
			name = "Libraries list"

			val libNames = libs.map { it.name }.toTypedArray()

			val list = JList<String>(libNames)
			list.layoutOrientation = JList.VERTICAL
			list.selectionMode = ListSelectionModel.SINGLE_SELECTION

			add(JScrollPane(list))
		}
	}

	// Metadata pane stuff
	private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

	private fun DefaultMutableTreeNode.add(label: String, value: String) {
		if (value.isNotBlank()) add(DefaultMutableTreeNode("$label: $value"))
	}

	private fun DefaultMutableTreeNode.add(label: String, date: Date) {
		add(DefaultMutableTreeNode("$label: ${dateFormat.format(date)}"))
	}

	private fun DefaultMutableTreeNode.add(record: Record) {

		// isReferenceDescription is not supported

		record.type?.let { add(label = REFERENCE_TYPE, value = it.toString()) }
		record.date?.let { add(label = REFERENCE_DATE, value = it) }

		// PubMedId is not supported

		record.doi?.let { add(label = REFERENCE_DOI, value = it) }

		if (record.authors != null && record.authors.isNotEmpty()) {
			val listNode = DefaultMutableTreeNode(REFERENCE_AUTHORLIST)
			record.authors.forEach { listNode.add(label = "Author", value = it) }
		}

		record.title?.let { add(label = REFERENCE_TITLE, value = it) }
		record.abstr?.let { add(label = REFERENCE_ABSTRACT, value = it) }
		record.secondaryTitle?.let { add(label = REFERENCE_JOURNAL, value = it) }
		record.volumeNumber?.let { add(label = REFERENCE_VOLUME, value = it) }
		record.issueNumber?.let { add(label = REFERENCE_ISSUE, value = it.toString()) }

		// page not supported

		// status not supported

		record.websiteLink?.let { add(label = REFERENCE_WEBSITE, value = it) }

		// comment not supported
	}

	private fun DefaultMutableTreeNode.add(vCard: VCard) {

		vCard.nickname?.values?.firstOrNull()?.let { add(label = CREATOR_GIVENNAME, value = it) }
		vCard.formattedName?.value?.let { add(label = CREATOR_FAMILYNAME, value = it) }
		vCard.emails?.firstOrNull()?.let { add(label = CREATOR_CONTACT, value = it.value) }
	}

	private fun DefaultMutableTreeNode.add(product: Product) {

		add(label = PRODUCT_NAME, value = product.environmentName)
		product.environmentDescription?.let { add(label = PRODUCT_DESC, value = it) }
		add(label = PRODUCT_UNIT, value = product.environmentUnit)

		if (product.productionMethod.isNotEmpty()) {
			// Parent node that holds all the creators
			val parentNode = DefaultMutableTreeNode(PRODUCT_PRODUCTIONMETHOD)
			product.productionMethod.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
		}

		if (product.packaging.isNotEmpty()) {
			// Parent node that holds all the packagings
			val parentNode = DefaultMutableTreeNode(PRODUCT_PACKAGING)
			product.packaging.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
		}

		if (product.productTreatment.isNotEmpty()) {
			// Parent node that holds all the product treatments
			val parentNode = DefaultMutableTreeNode(PRODUCT_TREATMENT)
			product.productTreatment.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
		}

		product.originCountry?.let { add(label = PRODUCT_ORIGINCOUNTRY, value = it) }
		product.areaOfOrigin?.let { add(label = PRODUCT_ORIGINAREA, value = it) }
		product.fisheriesArea?.let { add(label = PRODUCT_FISHERIES, value = it) }
		product.productionDate?.let { add(label = PRODUCT_PRODUCTIONDATE, date = it) }
		product.expirationDate?.let { add(label = PRODUCT_EXPIRATIONDATE, date = it) }
	}

	private fun DefaultMutableTreeNode.add(hazard: Hazard) {

		add(label = HAZARD_TYPE, value = hazard.hazardType)
		add(label = HAZARD_NAME, value = hazard.hazardName)
		hazard.hazardDescription?.let { add(label = HAZARD_DESCRIPTION, value = it) }
		add(label = HAZARD_UNIT, value = hazard.hazardUnit)
		hazard.adverseEffect?.let { add(label = HAZARD_ADVERSE, value = it) }
		hazard.origin?.let { add(label = HAZARD_ORIGIN, value = it) }
		hazard.benchmarkDose?.let { add(label = HAZARD_BMD, value = it) }
		hazard.maximumResidueLimit?.let { add(label = HAZARD_RESIDUE, value = it) }
		hazard.noObservedAdverse?.let { add(label = HAZARD_NOADVERSE, value = it) }
		hazard.lowestObservedAdverse?.let { add(label = HAZARD_LOWESTADVERSE, value = it) }
		hazard.acceptableOperator?.let { add(label = HAZARD_ACCEPTABLEOPERATOR, value = it) }
		hazard.acuteReferenceDose?.let { add(label = HAZARD_ACUTEDOSE, value = it) }
		hazard.acceptableDailyIntake?.let { add(label = HAZARD_DAILYINTAKE, value = it) }
		hazard.hazardIndSum?.let { add(label = HAZARD_INDSUM, value = it) }
		hazard.laboratoryName?.let { add(label = HAZARD_LABNAME, value = it) }
		hazard.laboratoryCountry?.let { add(label = HAZARD_LABCOUNTRY, value = it) }
		hazard.detectionLimit?.let { add(label = HAZARD_DETECTIONLIM, value = it) }
		hazard.quantificationLimit?.let { add(label = HAZARD_QUANTIFICATIONLIM, value = it) }
		hazard.leftCensoredData?.let { add(label = HAZARD_CENSOREDDATA, value = it) }
		hazard.rangeOfContamination?.let { add(label = HAZARD_CONTAMINATION, value = it) }
	}

	private fun DefaultMutableTreeNode.add(populationGroup: PopulationGroup) {

		add(label = PG_NAME, value = populationGroup.populationName)
		populationGroup.targetPopulation?.let { add(label = PG_TARGET, value = it) }

		if (populationGroup.populationSpan.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_SPAN)
			populationGroup.populationSpan.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (populationGroup.populationDescription.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_DESC)
			populationGroup.populationDescription.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (populationGroup.populationAge.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_AGE)
			populationGroup.populationAge.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		populationGroup.populationGender?.let { add(label = PG_GENDER, value = it) }

		if (populationGroup.bmi.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_BMI)
			populationGroup.bmi.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (populationGroup.specialDietGroups.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_DIETGROUPS)
			populationGroup.specialDietGroups.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (populationGroup.patternConsumption.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_PATTERNCONSUMPTION)
			populationGroup.patternConsumption.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (populationGroup.region.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_REGION)
			populationGroup.region.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (populationGroup.country.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_COUNTRY)
			populationGroup.country.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (populationGroup.populationRiskFactor.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_RISK)
			populationGroup.populationRiskFactor.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (populationGroup.season.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PG_SEASON)
			populationGroup.season.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
		}
	}

	private fun DefaultMutableTreeNode.add(generalInformation: GeneralInformation) {

		add(label = GI_STUDY_NAME, value = generalInformation.name)
		add(label = GI_ID, value = generalInformation.identifier)

		if (generalInformation.creators.isNotEmpty()) {
			// Parent node that holds all the creators
			val parentNode = DefaultMutableTreeNode("Creators")
			generalInformation.creators.forEach {
				val creatorNode = DefaultMutableTreeNode("Creator")
				creatorNode.add(it)
				parentNode.add(creatorNode)
			}
			add(parentNode)
		}

		add(label = GI_CREATION_DATE, date = generalInformation.creationDate)

		generalInformation.modificationDate.let {
			// Parent node that holds all the modification dates
			val modificationDateNode = DefaultMutableTreeNode("Modification dates")
			it.forEach { modificationDateNode.add(label = "Modification date", date = it) }
			add(modificationDateNode)
		}

		add(label = GI_RIGHTS, value = generalInformation.rights)

		// TODO: isAvailable

		add(label = GI_URL, value = generalInformation.url.toString())
		generalInformation.format?.let { add(label = GI_FORMAT, value = it) }

		if (generalInformation.reference.isNotEmpty()) {
			// Parent node that holds all the reference nodes
			val parentNode = DefaultMutableTreeNode("References")
			generalInformation.reference.forEach {
				val referenceNode = DefaultMutableTreeNode("Reference")
				referenceNode.add(it)
				parentNode.add(referenceNode)
			}
			add(parentNode)
		}

		generalInformation.language?.let { add(label = GI_LANGUAGE, value = it) }
		generalInformation.software?.let { add(label = GI_SOFTWARE, value = it) }
		generalInformation.languageWrittenIn?.let { add(label = GI_LANGUAGE_WRITTEN_IN, value = it) }
		generalInformation.status?.let { add(label = GI_STATUS, value = it) }
		generalInformation.objective?.let { add(label = GI_OBJECTIVE, value = it) }
		generalInformation.description?.let { add(label = GI_DESC, value = it) }
	}

	private fun DefaultMutableTreeNode.add(scope: Scope) {

		val productNode = DefaultMutableTreeNode(SCOPE_PRODUCT)
		scope.product?.let { productNode.add(product = it) }
		add(productNode)

		val hazardNode = DefaultMutableTreeNode(SCOPE_HAZARD)
		scope.hazard?.let { hazardNode.add(hazard = it) }
		add(hazardNode)

		val populationGroupNode = DefaultMutableTreeNode("Population group")
		scope.populationGroup?.let { populationGroupNode.add(it) }
		add(populationGroupNode)

		scope.generalComment?.let { add(label = SCOPE_COMMENT, value = it) }
		scope.temporalInformation?.let { add(label = SCOPE_TEMPORAL, value = it) }

		if (scope.region.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(SCOPE_REGION)
			scope.region.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		if (scope.country.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(SCOPE_COUNTRY)
			scope.country.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}
	}

	private fun DefaultMutableTreeNode.add(dataBackground: DataBackground) {

		dataBackground.study?.let {
			val node = DefaultMutableTreeNode("Study")
			node.add(it)
			add(node)
		}

		dataBackground.studySample?.let {
			val node = DefaultMutableTreeNode(DB_STUDYSAMPLE)
			node.add(it)
			add(node)
		}

		dataBackground.dietaryAssessmentMethod?.let {
			val node = DefaultMutableTreeNode(DB_DIETARYASSESSMENTMETHOD)
			node.add(it)
			add(node)
		}

		if (dataBackground.laboratoryAccreditation.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(DB_ACCREDITATION)
			dataBackground.laboratoryAccreditation.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		dataBackground.assay?.let {
			val node = DefaultMutableTreeNode(DB_ASSAY)
			node.add(it)
			add(node)
		}
	}

	private fun DefaultMutableTreeNode.add(study: Study) {

		add(label = STUDY_TITLE, value = study.title)
		study.description?.let { add(label = STUDY_DESC, value = it) }
		study.designType?.let { add(label = STUDY_DESIGN, value = it) }
		study.measurementType?.let { add(label = STUDY_MEASUREMENT, value = it) }
		study.technologyType?.let { add(label = STUDY_TECH_TYPE, value = it) }
		study.technologyPlatform?.let { add(label = STUDY_TECH_PLAT, value = it) }
		study.accreditationProcedure?.let { add(label = STUDY_ACCREDITATION, value = it) }
		study.protocolName?.let { add(label = STUDY_PROTOCOL_NAME, value = it) }
		study.protocolType?.let { add(label = STUDY_PROTOCOL_TYPE, value = it) }
		study.protocolDescription?.let { add(label = STUDY_PROTOCOL_DESC, value = it) }
		study.protocolURI?.let { add(label = STUDY_PROTOCOL_URI, value = it.toString()) }
		study.protocolVersion?.let { add(label = STUDY_PROTOCOL_VERSION, value = it) }
		study.parametersName?.let { add(label = STUDY_PARAMETERS, value = it) }

		// TODO: componentsName

		study.componentsType?.let { add(label = STUDY_COMPONENTS_TYPE, value = it) }
	}

	private fun DefaultMutableTreeNode.add(studySample: StudySample) {

		add(label = SS_SAMPLE, value = studySample.sample)
		studySample.moisturePercentage?.let { add(label = SS_MOISTURE_PERC, value = it.toString()) }
		studySample.fatPercentage?.let { add(label = SS_FAT_PERC, value = it.toString()) }
		add(label = SS_SAMPLE_PROTOCOL, value = studySample.collectionProtocol)
		studySample.samplingStrategy?.let { add(label = SS_SAMPLING_STRATEGY, value = it) }
		studySample.samplingProgramType?.let { add(label = SS_SAMPLING_TYPE, value = it) }
		studySample.samplingMethod?.let { add(label = SS_SAMPLING_METHOD, value = it) }
		add(label = SS_SAMPLING_PLAN, value = studySample.samplingWeight)
		add(label = SS_SAMPLING_WEIGHT, value = studySample.samplingWeight)
		add(label = SS_SAMPLING_SIZE, value = studySample.samplingSize)
		studySample.lotSizeUnit?.let { add(label = SS_LOT_UNIT, value = it) }
		studySample.samplingPoint?.let { add(label = SS_SAMPLING_POINT, value = it) }
	}

	private fun DefaultMutableTreeNode.add(dietaryAssessmentMethod: DietaryAssessmentMethod) {

		add(label = DAM_TOOL, value = dietaryAssessmentMethod.collectionTool)
		add(label = DAM_NUMBER_NON_CONSECUTIVE, value = dietaryAssessmentMethod.numberOfNonConsecutiveOneDay.toString())
		dietaryAssessmentMethod.softwareTool?.let { add(label = DAM_SOFTWARE, value = it) }

		if (dietaryAssessmentMethod.numberOfFoodItems.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(DAM_FOOD_ITEM)
			dietaryAssessmentMethod.numberOfFoodItems.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
		}

		add(label = DAM_NUMBER_NON_CONSECUTIVE, value = dietaryAssessmentMethod.numberOfNonConsecutiveOneDay.toString())

		if (dietaryAssessmentMethod.recordTypes.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(DAM_RECORD_TYPE)
			dietaryAssessmentMethod.recordTypes.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
		}

		if (dietaryAssessmentMethod.foodDescriptors.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(DAM_FOOD_ITEM)
			dietaryAssessmentMethod.foodDescriptors.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
		}
	}

	private fun DefaultMutableTreeNode.add(assay: Assay) {
		add(label = ASSAY_NAME, value = assay.name)
		assay.description?.let { add(label = ASSAY_DESC, value = it) }
	}

	private fun DefaultMutableTreeNode.add(parameter: Parameter) {

		add(label = PARAMETER_ID, value = parameter.id)
		add(label = PARAMETER_CLASIF, value = parameter.classification.toString())
		add(label = PARAMETER_NAME, value = parameter.name)
		parameter.description?.let { add(label = PARAMETER_DESC, value = it) }
		add(label = PARAMETER_UNIT, value = parameter.unit)
		add(label = PARAMETER_UNIT_CATEGORY, value = parameter.unitCategory)
		add(label = PARAMETER_DATA_TYPE, value = parameter.dataType)
		parameter.source?.let { add(label = PARAMETER_SOURCE, value = it) }
		parameter.subject?.let { add(label = PARAMETER_SUBJECT, value = it) }
		parameter.distribution?.let { add(label = PARAMETER_DIST, value = it) }
		parameter.value?.let { add(label = PARAMETER_VALUE, value = it) }
		parameter.reference?.let { add(label = PARAMETER_REFERENCE, value = it) }
		parameter.variabilitySubject?.let { add(label = PARAMETER_VARIABILITY, value = it) }

		if (parameter.modelApplicability.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode(PARAMETER_APPLICABILITY)
			parameter.modelApplicability.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}

		parameter.error?.let { add(label = PARAMETER_ERROR, value = it.toString()) }
	}

	private fun DefaultMutableTreeNode.add(modelEquation: ModelEquation) {

		add(label = MODEL_EQUATION_NAME, value = modelEquation.equationName)
		modelEquation.equationClass?.let { add(label = MODEL_EQUATION_CLASS, value = it) }

		if (modelEquation.equationReference.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode("References")
			modelEquation.equationReference.forEach {
				val childNode = DefaultMutableTreeNode("Reference")
				childNode.add(it)
				parentNode.add(childNode)
			}
			add(parentNode)
		}

		add(label = MODEL_EQUATION_SCRIPT, value = modelEquation.equation)
	}

	private fun DefaultMutableTreeNode.add(modelMath: ModelMath) {

		if (modelMath.parameter.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode("Parameters")
			modelMath.parameter.forEach {
				val childNode = DefaultMutableTreeNode("Parameter")
				childNode.add(parameter = it)
				parentNode.add(childNode)
			}
			add(parentNode)
		}

		modelMath.sse?.let { add(label = "SSE", value = it.toString()) }
		modelMath.mse?.let { add(label = "MSE", value = it.toString()) }
		modelMath.rmse?.let { add(label = "RMSE", value = it.toString()) }
		modelMath.rSquared?.let { add(label = "r-Squared", value = it.toString()) }
		modelMath.aic?.let { add(label = "AIC", value = it.toString()) }
		modelMath.bic?.let { add(label = "BIC", value = it.toString()) }

		modelMath.modelEquation?.let {
			val node = DefaultMutableTreeNode("Model equation")
			node.add(modelEquation = it)
			add(node)
		}

		modelMath.fittingProcedure?.let { add(label = "Fitting procedure", value = it) }

		// TODO: exposure

		if (modelMath.event.isNotEmpty()) {
			val parentNode = DefaultMutableTreeNode("Events")
			modelMath.event.forEach { parentNode.add(DefaultMutableTreeNode(it)) }
			add(parentNode)
		}
	}

	private fun createTree(genericModel: GenericModel): JTree {

		val generalInformationNode = DefaultMutableTreeNode("General information")
		generalInformationNode.add(genericModel.generalInformation)

		val scopeNode = DefaultMutableTreeNode("Scope")
		scopeNode.add(genericModel.scope)

		val dataBackgroundNode = DefaultMutableTreeNode("Data background")
		genericModel.dataBackground?.let { dataBackgroundNode.add(it) }

		val modelMathNode = DefaultMutableTreeNode("Model math")
		genericModel.modelMath?.let { modelMathNode.add(it) }

		val simulationNode = DefaultMutableTreeNode("Simulation")
		// TODO: simulation

		val rootNode = DefaultMutableTreeNode()

		rootNode.add(generalInformationNode)
		rootNode.add(scopeNode)
		rootNode.add(dataBackgroundNode)
		rootNode.add(modelMathNode)
		rootNode.add(simulationNode)

		val tree = JTree(rootNode)
		tree.isRootVisible = false
		tree.selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION

		return tree
	}
}

/**
 * Serializer used to save this port object.
 * @return a {@link FskPortObject}.
 */
class FskPortObjectSerializer : PortObjectSerializer<FskPortObject>() {

	val MODEL = "model.R"
	val PARAM = "param.R"
	val VIZ = "viz.R"
	val META_DATA = "metaData"
	val WORKSPACE = "workspace"

	val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(RakipModule())

	override fun savePortObject(portObject: FskPortObject, stream: PortObjectZipOutputStream, exec: ExecutionMonitor) {

		// model entry (file with model script)
		stream.putNextEntry(ZipEntry(MODEL))
		IOUtils.write(portObject.model, stream)
		stream.closeEntry()

		// param entry (file with param script)
		stream.putNextEntry(ZipEntry(PARAM))
		IOUtils.write(portObject.param, stream)
		stream.closeEntry()

		// viz entry (file with visualization script)
		stream.putNextEntry(ZipEntry(VIZ))
		IOUtils.write(portObject.viz, stream)
		stream.closeEntry()

		// template entry (file with model meta data)
		stream.putNextEntry(ZipEntry(META_DATA))
		val stringVal = objectMapper.writeValueAsString(portObject.genericModel)
		IOUtils.write(stringVal, stream)
		stream.closeEntry()

		// workspace entry
		stream.putNextEntry(ZipEntry(WORKSPACE))
		portObject.workspace?.let { workspace ->
			FileInputStream(workspace).use { FileUtil.copy(it, stream) }
		}
		stream.closeEntry()
	}

	override fun loadPortObject(stream: PortObjectZipInputStream, spec: PortObjectSpec, exec: ExecutionMonitor): FskPortObject {

		// model script entry
		stream.getNextEntry()
		val modelScript = IOUtils.toString(stream, "UTF-8")

		// parameters script entry
		stream.getNextEntry()
		val parameterScript = IOUtils.toString(stream, "UTF-8")

		// visualization script entry
		stream.getNextEntry()
		val visualizationScript = IOUtils.toString(stream, "UTF-8")

		// metadata entry
		stream.getNextEntry()
		val metadataAsString = IOUtils.toString(stream, "UTF-8")
		val genericModel = objectMapper.readValue(metadataAsString, GenericModel::class.java)

		// workspace
		stream.getNextEntry()
		val workspaceFile = FileUtil.createTempFile("workspace", ".r")
		FileOutputStream(workspaceFile).use { FileUtil.copy(stream, it) }

		val portObj = FskPortObject(model = modelScript, param = parameterScript, viz = visualizationScript, genericModel = genericModel, workspace = workspaceFile)

		// libraries
		stream.getNextEntry()
		val libNames = IOUtils.readLines(stream, "UTF-8")

		val libRegistry = LibRegistry.instance()

		// Install missing libraries
		val missingLibs = libNames.filter { !libRegistry.isInstalled(it) }.toList()
		if (missingLibs.isNotEmpty()) libRegistry.installLibs(missingLibs)

		// Adds libs to the paths of the libraries converted to Files
		if (libNames.isNotEmpty()) {
			libRegistry.getPaths(libNames).map { it.toFile() }.forEach { portObj.libs.add(it) }
		}

		stream.close()

		return portObj
	}
}