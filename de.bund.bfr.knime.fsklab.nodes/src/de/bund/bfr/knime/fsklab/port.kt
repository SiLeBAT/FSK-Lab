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
import de.bund.bfr.knime.fsklab.nodes.ui.createLibrariesPanel
import de.bund.bfr.knime.fsklab.rakip.*
import ezvcard.VCard
import org.apache.commons.io.IOUtils
import org.knime.core.node.ExecutionMonitor
import org.knime.core.node.port.*
import org.knime.core.node.port.PortObject.PortObjectSerializer
import org.knime.core.util.FileUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeSelectionModel
import de.bund.bfr.knime.fsklab.FskPlugin.Companion.MESSAGES_BUNDLE

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

    class Serializer : PortObjectSpec.PortObjectSpecSerializer<FskPortObjectSpec>() {
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
 * @property workspace R workspace file
 * @property libs R library files
 *
 * @author Miguel de Alba, BfR, Berlin.
 */
class FskPortObject(var model: String = "", var param: String = "", var viz: String = "",
                    var genericModel: GenericModel = GenericModel(), var workspace: File? = null,
                    val libs: MutableSet<File> = mutableSetOf()) : PortObject {

    companion object {
        val TYPE: PortType = PortTypeRegistry.getInstance().getPortType(FskPortObject::class.java)
        val TYPE_OPTIONAL: PortType = PortTypeRegistry.getInstance().getPortType(FskPortObject::class.java, true)

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

        val metaDataPane = JScrollPane(createTree(genericModel))
        metaDataPane.name = "Meta data"

        return arrayOf(modelScriptPanel, paramScriptPanel, vizScriptPanel, metaDataPane,
                createLibrariesPanel(libs))
    }

//	/**
//	 * Serializer used to save this port object.
//	 * @return a [FskPortObject]
//	 */
//	class Serializer: PortObjectSerializer<FskPortObject>() {
//
//		override fun savePortObject(portObject: FskPortObject, out: PortObjectZipOutputStream, exec: ExecutionMonitor) {
//
//			// model entry (file with model script)
//			out.putNextEntry(ZipEntry(MODEL))
//			IOUtils.write(portObject.model, out)
//			out.closeEntry()
//
//			// param entry (file with param script)
//			out.putNextEntry(ZipEntry(PARAM))
//			IOUtils.write(portObject.param, out)
//			out.closeEntry()
//
//			// viz entry (file with visualization script)
//			out.putNextEntry(ZipEntry(VIZ))
//			IOUtils.write(portObject.viz, out)
//			out.closeEntry()
//
//			// template entry (file with model meta data)
//			out.putNextEntry(ZipEntry(META_DATA))
//			IOUtils.write(objectMapper.writeValueAsString(portObject.genericModel), out)
//			out.closeEntry()
//
//			// workspace entry
//			portObject.workspace?.let {
//				out.putNextEntry(ZipEntry(WORKSPACE))
//				it.inputStream().use { inputStream ->
//					FileUtil.copy(inputStream, out)
//				}
//				out.closeEntry()
//			}
//
//			// Libraries
//			if (portObject.libs.isNotEmpty()) {
//				out.putNextEntry(ZipEntry("library.list"))
//				val libNames = portObject.libs.map { it.name.split("\\_")[0] }
//				IOUtils.writeLines(libNames, "\n", out, "UTF-8")
//				out.closeEntry()
//			}
//
//			out.close()
//		}
//
//		override fun loadPortObject(inputStream: PortObjectZipInputStream, spec: PortObjectSpec, exec: ExecutionMonitor):
//				FskPortObject {
//
//			val portObj = FskPortObject()
//
//			while (true) {
//
//				// Get entry name or exits the loop if there are no more entries
//				val entryName: String = inputStream.nextEntry?.name ?: break
//
//				when (entryName) {
//					MODEL -> portObj.model = IOUtils.toString(inputStream, "UTF-8")
//					PARAM -> portObj.param = IOUtils.toString(inputStream, "UTF-8")
//					VIZ -> portObj.viz = IOUtils.toString(inputStream, "UTF-8")
//                    META_DATA -> {
//                        val metaDataAsString = IOUtils.toString(inputStream, "UTF-8")
//                        portObj.genericModel = objectMapper.readValue(metaDataAsString, GenericModel::class.java)
//                    }
//                    WORKSPACE -> {
//                        val newFile = FileUtil.createTempFile("workspace", ".r")
//                        newFile.outputStream().use { outputStream ->
//                            FileUtil.copy(inputStream, outputStream)
//                        }
//                        portObj.workspace = newFile
//                    }
//                    "library.list" -> {
//                        val libNames = IOUtils.readLines(inputStream, "UTF-8")
//                        val libRegistry = LibRegistry.instance
//
//                        // Install mmissing libraries
//                        val missingLibs = libNames.filter { !libRegistry.isInstalled(it) }
//                        if (missingLibs.isNotEmpty()) { libRegistry.installLibs(missingLibs) }
//
//                        // Adds to #libs the Paths of the libraries converted to Files
//                        libRegistry.getPaths(libNames).forEach { portObj.libs.add(it.toFile()) }
//                    }
//				}
//			}
//
//            inputStream.close()
//
//            return portObj
//		}
//	}
}

/**
 * Serializer used to save this port object.
 * @return a {@link FskPortObject}.
 */
class FskPortObjectSerializer : PortObjectSerializer<FskPortObject>() {

    private val MODEL = "model.R"
    private val PARAM = "param.R"
    private val VIZ = "viz.R"
    private val META_DATA = "metaData"
    private val WORKSPACE = "workspace"

    private val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(RakipModule())

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
        stream.nextEntry
        val modelScript = IOUtils.toString(stream, "UTF-8")

        // parameters script entry
        stream.nextEntry
        val parameterScript = IOUtils.toString(stream, "UTF-8")

        // visualization script entry
        stream.nextEntry
        val visualizationScript = IOUtils.toString(stream, "UTF-8")

        // metadata entry
        stream.nextEntry
        val metadataAsString = IOUtils.toString(stream, "UTF-8")
        val genericModel = objectMapper.readValue(metadataAsString, GenericModel::class.java)

        // workspace
        stream.nextEntry
        val workspaceFile = FileUtil.createTempFile("workspace", ".r")
        FileOutputStream(workspaceFile).use { FileUtil.copy(stream, it) }

        val portObj = FskPortObject(model = modelScript, param = parameterScript, viz = visualizationScript,
                genericModel = genericModel, workspace = workspaceFile)

        // libraries
        stream.nextEntry
        val libNames = IOUtils.readLines(stream, "UTF-8")

        val libRegistry = LibRegistry.instance

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

/**
 * Create and add a tree node for a string property. If the value is blank then no new node is added.
 *
 * @param key Key in resource bundle for the string label of the property. Cannot be empty.
 * @param value Can be empty.
 */
private fun DefaultMutableTreeNode.add(key: String, value: String) {
    if (value.isNotBlank()) {
        val label = MESSAGES_BUNDLE.getString(key)
        add(DefaultMutableTreeNode("$label: $value"))
    }
}

/**
 * Create and add tree node for a date property. The date is formatted with the 'yyyy-MM-dd' format.
 *
 * @param key Key in resource bundle for the string label of the property. Cannot be empty.
 * @param value date
 */
private fun DefaultMutableTreeNode.add(key: String, value: Date) {
    val label = MESSAGES_BUNDLE.getString(key)
    val dateStr = SimpleDateFormat("yyyy-MM-dd").format(value)
    add(DefaultMutableTreeNode("$label: $dateStr"))
}

/**
 * Create and add tree node for a Double property.
 *
 * @param key Key in resource bundle for the string label of the property. Cannot be empty.
 * @param value double
 */
private fun DefaultMutableTreeNode.add(key: String, value: Double) {
    val label = MESSAGES_BUNDLE.getString(key)
    add(DefaultMutableTreeNode("$label: $value"))
}

/**
 * Create a tree node for {@code List<String>} and add it to a passed node. If the value is empty then no new node is added.
 *
 * @param key Key in resource bundle for the string label of the property. Cannot be empty.
 * @param value Can be empty.
 */
private fun DefaultMutableTreeNode.add(key: String, value: List<String>) {

    if (value.isNotEmpty()) {
        val label = MESSAGES_BUNDLE.getString(key)
        val listNode = DefaultMutableTreeNode(label)
        value.map { DefaultMutableTreeNode() }.forEach(listNode::add)
        add(listNode)
    }
}

/**
 * Create and add a number of tree nodes with several properties of a passed Record object.
 *
 * The Record properties to be added are: type, date, authors, title, abstract, volume, issue and website.
 *
 * @param record RIS record
 */
private fun DefaultMutableTreeNode.add(record: Record) {

    val prefix = "GM.EditReferencePanel"

    // isReferenceDescription is not supported

    record.type?.let { add(prefix + "typeLabel", it.toString()) }
    record.date?.let { add(prefix + "dateLabel", it) }

    // PubMedId is not supported

    record.authors?.let { add(prefix + "authorListLabel", it) }
    record.title?.let { add(prefix + "titleLabel", it) }
    record.abstr?.let { add(prefix + "abstractLabel", it) }
    record.secondaryTitle?.let { add(prefix + "journalLabel", it) }
    record.volumeNumber?.let { add(prefix + "volumeLabel", it) }
    record.issueNumber?.let { add(prefix + "issueLabel", it.toString()) }

    // page not supported
    // status not supported

    record.websiteLink?.let { add(prefix + "websiteLink", it) }

    // comment not supported
}

/**
 * Create and add a number of tree nodes with several properties of a passed VCard object.
 *
 * The VCard properties to be added are:
 * - Nickname as given name
 * - Formatted name as family name
 * - First e-mail as contact information
 *
 * @param vcard VCard
 */
private fun DefaultMutableTreeNode.add(vcard: VCard) {

    val prefix = "GM.EditCreatorPanel"

    vcard.nickname?.let { add(prefix + "givenNameLabel", it.toString()) }
    vcard.formattedName?.let { add(prefix + "familyNameLabel", it.toString()) }

    if (vcard.emails.isNotEmpty()) {
        val value = vcard.emails[0].toString()
        add(prefix + "contactLabel", value)
    }
}

/**
 * Create and add a number of tree nodes with several properties of a passed Product.
 * @param product
 */
private fun DefaultMutableTreeNode.add(product: Product) {

    val prefix = "GM.EditProductPanel."

    add(prefix + "envNameLabel", product.environmentName)
    add(prefix + "envDescriptionLabel", product.environmentDescription)
    add(prefix + "envUnitLabel", product.environmentUnit)
    add(prefix + "productionMethodLabel", product.productionMethod)
    add(prefix + "originCountryLabel", product.originCountry)
    add(prefix + "originAreaLabel", product.originArea)
    add(prefix + "fisheriesAreaLabel", product.fisheriesArea)
    add(prefix + "productionDateLabel", product.productionDate)
    add(prefix + "expirationDateLabel", product.expirationDate)
}

/**
 * Create and add a number of tree nodes for the properties of a passed Hazard.
 * @param hazard
 */
private fun DefaultMutableTreeNode.add(hazard: Hazard) {

    val prefix = "GM.EditHazardPanel."

    add(prefix + "hazardTypeLabel", hazard.hazardType)
    add(prefix + "hazardNameLabel", hazard.hazardName)
    add(prefix + "hazardDescriptionLabel", hazard.hazardDescription)
    add(prefix + "hazardUnitLabel", hazard.hazardUnit)
    add(prefix + "adverseEffectLabel", hazard.adverseEffect)
    add(prefix + "originLabel", hazard.origin)
    add(prefix + "bmdLabel", hazard.benchmarkDose)
    add(prefix + "maxResidueLimitLabel", hazard.maximumResidueLimit)
    add(prefix + "noObservedAdverseLabel", hazard.noObservedAdverse)
    add(prefix + "lowestObserveLabel", hazard.lowestObservedAdverse)
    add(prefix + "acceptableOperatorLabel", hazard.acceptableOperator)
    add(prefix + "acuteReferenceDoseLabel", hazard.acuteReferenceDose)
    add(prefix + "acceptableDailyIntake", hazard.acceptableDailyIntake)
    add(prefix + "indSumLabel", hazard.hazardIndSum)
    add(prefix + "labNameLabel", hazard.laboratoryName)
    add(prefix + "labCountryLabel", hazard.laboratoryCountry)
    add(prefix + "detectionLimitLabel", hazard.detectionLimit)
    add(prefix + "quantificationLimitLabel", hazard.quantificationLimit)
    add(prefix + "leftCensoredDataLabel", hazard.leftCensoredData)
    add(prefix + "contaminationRangeLabel", hazard.contaminationRange)
}

/**
 * Create and add a number of tree nodes for the properties of a passed PopulationGroup.
 * @param populationGroup
 */
private fun DefaultMutableTreeNode.add(populationGroup: PopulationGroup) {

    val prefix = "GM.EditPopoulationGroupPanel."

    add(prefix + "populationNameLabel", populationGroup.populationName)
    add(prefix + "populationSpanLabel", populationGroup.populationSpan)
    add(prefix + "populationDescriptionLabel", populationGroup.populationDescription)
    add(prefix + "populationAgeLabel", populationGroup.populationAge)
    add(prefix + "populationGenderLabel", populationGroup.populationGender)
    add(prefix + "bmiLabel", populationGroup.bmi)
    add(prefix + "specialDietGroupsLabel", populationGroup.specialDietGroups)
    add(prefix + "regionLabel", populationGroup.region)
    add(prefix + "countryLabel", populationGroup.country)
    add(prefix + "riskAndPopulationLabel", populationGroup.populationRiskFactor)
    add(prefix + "seasonLabel", populationGroup.season)
}

/**
 * Create and add a number of tree nodes for the properties of a passed GeneralInformation.
 * @param generalInformation
 */
private fun DefaultMutableTreeNode.add(generalInformation: GeneralInformation) {

    val prefix = "GM.GeneralInformationPanel."

    add(prefix + "studyNameLabel", generalInformation.name)
    add(prefix + "sourceLabel", generalInformation.source)
    add(prefix + "identifierLabel", generalInformation.identifier)

    if (generalInformation.creators.isNotEmpty()) {
        // Parent node that holds all the creators
        val creatorsNode = DefaultMutableTreeNode("Creators")

        generalInformation.creators.forEach {
            val creatorNode = DefaultMutableTreeNode("Creator")
            creatorNode.add(it)
            creatorsNode.add(creatorNode)
        }
        add(creatorsNode)
    }

    add(prefix + "creationDateLabel", generalInformation.creationDate)

    if (generalInformation.modificationDate.isNotEmpty()) {
        // Parent node that holds all the modification dates
        val modificationDateNode = DefaultMutableTreeNode("Modification dates")
        generalInformation.modificationDate.forEach {
            modificationDateNode.add(prefix + "modificationDateLabel", it)
        }
        add(modificationDateNode)
    }

    add(prefix + "rightsLabel", generalInformation.rights)

    // TODO: isAvailable

    add(prefix + "urlLabel", generalInformation.url.toString())

    if (generalInformation.reference.isNotEmpty()) {
        val parentNode = DefaultMutableTreeNode("References")
        generalInformation.reference.forEach {
            val refNode = DefaultMutableTreeNode("Reference")
            refNode.add(it)
            parentNode.add(refNode)
        }
    }

    add(prefix + "languageLabel", generalInformation.language)
    add(prefix + "softwareLabel", generalInformation.software)
    add(prefix + "languageWrittenInLabel", generalInformation.languageWrittenIn)
    add(prefix + "statusLabel", generalInformation.status)
    add(prefix + "objectiveLabel", generalInformation.objective)
    add(prefix + "descriptionLabel", generalInformation.description)
}

/**
 * Creates tree nodes for the properties of a passed Scope.
 * @param scope
 */
private fun DefaultMutableTreeNode.add(scope: Scope) {

    val prefix = "GM.ScopePanel."

    scope.product.let {

        val label = MESSAGES_BUNDLE.getString(prefix + "productLabel")
        val productNode = DefaultMutableTreeNode(label)
        productNode.add(it)

        add(productNode)
    }

    scope.hazard.let {

        val label = MESSAGES_BUNDLE.getString(prefix + "hazardLabel")
        val hazardNode = DefaultMutableTreeNode(label)
        hazardNode.add(it)

        add(hazardNode)
    }

    scope.populationGroup.let {

        val pgNode = DefaultMutableTreeNode("Population group")
        pgNode.add(it)
        add(pgNode)
    }

    add(prefix + "commentLabel", scope.generalComment)
    add(prefix + "temporalInformationLabel", scope.temporalInformation)
    add(prefix + "regionLabel", scope.region)
    add(prefix + "countryLabel", scope.country)
}

/**
 * Creates tree nodes for the properties of the passed DataBackground and adds them.
 * @param dataBackground
 */
private fun DefaultMutableTreeNode.add(dataBackground: DataBackground) {

    val prefix = "GM.DataBackgroundPanel."

    dataBackground.study.let {
        val studyNode = DefaultMutableTreeNode("Study")
        studyNode.add(it)

        add(studyNode)
    }

    dataBackground.studySample.let {
        val key = prefix + "studySampleLabel"
        val label = MESSAGES_BUNDLE.getString(key)
        val studySampleNode = DefaultMutableTreeNode(label)
        studySampleNode.add(it)

        add(studySampleNode)
    }

    dataBackground.dietaryAssessmentMethod.let {
        val key = prefix + "assayLabel"
        val label = MESSAGES_BUNDLE.getString(key)
        val damNode = DefaultMutableTreeNode(label)
        damNode.add(it)

        add(damNode)
    }

    add(prefix + "laboratoryAccreditationLabel", dataBackground.laboratoryAccreditation)

    dataBackground.assay.let {
        val key = prefix + "assayLabel"
        val label = MESSAGES_BUNDLE.getString(key)
        val assayNode = DefaultMutableTreeNode(label)
        assayNode.add(dataBackground.assay)

        add(assayNode)
    }
}

/**
 * Creates tree nodes for the properties of a passed Study and adds them.
 * @param study
 */
private fun DefaultMutableTreeNode.add(study: Study) {

    val prefix = "GM.StudyPanel."

    add(prefix + "studyTitleLabel", study.title)
    add(prefix + "studyDescriptionLabel", study.description)
    add(prefix + "studyDesignTypeLabel", study.designType)
    add(prefix + "studyAssayMeasurementsTypeLabel", study.measurementType)
    add(prefix + "studyAssayTechnologyTypeLabel", study.technologyType)
    add(prefix + "studyAssayTechnologyPlatformLabel", study.technologyPlatform)
    add(prefix + "accreditationProcedureLabel", study.accreditationProcedure)
    add(prefix + "protocolNameLabel", study.protocolName)
    add(prefix + "protocolTypeLabel", study.protocolType)
    add(prefix + "protocolDescriptionLabel", study.protocolDescription)
    add(prefix + "protocolURILabel", study.protocolURI.toString())
    add(prefix + "protocolVersionLabel", study.protocolVersion)
    add(prefix + "parametersLabel", study.parametersName)
    add(prefix + "componentsTypeLabel", study.componentsType)
}

private fun DefaultMutableTreeNode.add(studySample: StudySample) {

    val prefix = "GM.EditStudySamplePanel."

    add(prefix + "sampleNameLabel", studySample.sample)
    add(prefix + "moisturePercentageLabel", studySample.moisturePercentage)
    add(prefix + "fatPercentageLabel", studySample.fatPercentage)
    add(prefix + "sampleProtocolLabel", studySample.collectionProtocol)
    add(prefix + "samplingStrategyLabel", studySample.samplingStrategy)
    add(prefix + "samplingMethodLabel", studySample.samplingMethod)
    add(prefix + "samplingPlanLabel", studySample.samplingPlan)
    add(prefix + "samplingWeightLabel", studySample.samplingWeight)
    add(prefix + "samplingSizeLabel", studySample.samplingSize)
    add(prefix + "lotSizeUnitLabel", studySample.lotSizeUnit)
    add(prefix + "samplingPointLabel", studySample.lotSizeUnit)
}

private fun DefaultMutableTreeNode.add(method: DietaryAssessmentMethod) {

    val prefix = "GM.EditDietaryAssessmentMethodPanel."

    add(prefix + "dataCollectionToolLabel", method.collectionTool)
    add(prefix + "nonConsecutiveOneDaysLabel", method.numberOfNonConsecutiveOneDay.toString())
    add(prefix + "dietarySoftwareToolLabel", method.softwareTool)
    add(prefix + "foodItemNumberLabel", method.numberOfFoodItems)
    add(prefix + "recordTypeLabel", method.recordTypes)
    add(prefix + "foodDescriptionLabel", method.foodDescriptors)
}

private fun DefaultMutableTreeNode.add(assay: Assay) {
    add("GM.EditAssayPanel.nameLabel", assay.name)
    add("GM.EditAssayPanel.descriptionLabel", assay.description)
}

private fun DefaultMutableTreeNode.add(parameter: Parameter) {

    val prefix = "GM.EditParameterPanel."

    add(prefix + "idLabel", parameter.id)
    add(prefix + "classificationLabel", parameter.classification.toString())
    add(prefix + "parameterNameLabel", parameter.parameterName)
    add(prefix + "descriptionLabel", parameter.description)
    add(prefix + "unitLabel", parameter.unit)
    add(prefix + "unitCategoryLabel", parameter.unitCategory)
    add(prefix + "dataTypeLabel", parameter.dataType)
    add(prefix + "sourceLabel", parameter.source)
    add(prefix + "subjectLabel", parameter.subject)
    add(prefix + "distributionLabel", parameter.distribution)
    add(prefix + "valueLabel", parameter.value)
    add(prefix + "referenceLabel", parameter.reference)
    add(prefix + "variabilitySubjectLabel", parameter.variabilitySubject)
    add(prefix + "applicabilityLabel", parameter.modelApplicability)
    add(prefix + "errorLabel", parameter.error)
}

private fun DefaultMutableTreeNode.add(modelEquation: ModelEquation) {

    val prefix = "GM.EditModelEquationPanel."

    add(prefix + "nameLabel", modelEquation.equationName)
    add(prefix + "classLabel", modelEquation.equationClass)

    if (modelEquation.equationReference.isNotEmpty()) {
        val parentNode = DefaultMutableTreeNode("References")
        modelEquation.equationReference.forEach {
            val refNode = DefaultMutableTreeNode("Reference")
            refNode.add(it)
            parentNode.add(refNode)
        }
        add(parentNode)
    }

    add(prefix + "scriptLabel", modelEquation.equation)
}

private fun DefaultMutableTreeNode.add(modelMath: ModelMath) {

    if (modelMath.parameter.isNotEmpty()) {
        val parentNode = DefaultMutableTreeNode("Parameters")
        modelMath.parameter.forEach {
            val childNode = DefaultMutableTreeNode("Parameter")
            childNode.add(it)
            parentNode.add(childNode)
        }
        add(parentNode)
    }

    add("ModelMath.SSE", modelMath.sse)
    add("ModelMath.MSE", modelMath.mse)
    add("ModelMath.RMSE", modelMath.rmse)
    add("ModelMath.R2", modelMath.rSquared)
    add("ModelMath.AIC", modelMath.aic)
    add("ModelMath.BIC", modelMath.bic)

    val equationNode = DefaultMutableTreeNode("Model equation")
    equationNode.add(modelMath.modelEquation)
    add(equationNode)

    add("Fitting procedure", modelMath.fittingProcedure)

    // TODO: Exposure

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
    dataBackgroundNode.add(genericModel.dataBackground)

    val modelMathNode = DefaultMutableTreeNode("Model math")
    modelMathNode.add(genericModel.modelMath)

    val simulationNode = DefaultMutableTreeNode("Simulation")
    // TODO: Simulation

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
