package de.bund.bfr.knime.pmm.common.reader;

import java.util.Map;

import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Unit;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.Model1Metadata;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.items.EMLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.Model1AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.Model1MatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.pmfml.sbml.Correlation;
import de.bund.bfr.pmfml.sbml.Limits;
import de.bund.bfr.pmfml.sbml.Model1Annotation;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.PMFCoefficient;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.SBMLFactory;

public class Model1Tuple {

  public KnimeTuple knimeTuple;
  private static KnimeSchema schema = SchemaFactory.createM1Schema(); // model1
                                                                      // schema

  public Model1Tuple(SBMLDocument doc) {

    Model model = doc.getModel();

    // Parses annotation
    Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

    ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    CatalogModelXml catModel = ReaderUtils.model1Rule2CatModel(rule);

    // Parse constraints
    Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());

    PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));

    DepXml depXml = new DepXml("Value");
    String depUnitID = species.getUnits();
    if (depUnitID != null) {
      if (depUnitID.equals("dimensionless")) {
        depXml.unit = "dimensionless";
        depXml.category = "Dimensionless quantity";
      } else {
        String depUnitName = model.getUnitDefinition(depUnitID).getName();
        depXml.unit = depUnitName;
        if (DBUnits.getDBUnits().containsKey(depUnitName)) {
          depXml.category = DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity();
        }
      }
    }
    if (species.isSetDescription()) {
      depXml.description = species.getDescription();
    }

    // Gets limits
    if (limits.containsKey(species.getId())) {
      Limits depLimits = limits.get(species.getId());
      depXml.max = depLimits.getMax();
      depXml.min = depLimits.getMin();
    }

    // Parses indeps
    Parameter indepParam = model.getParameter(Categories.getTime());
    IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
    String indepUnitID = indepParam.getUnits();
    if (!indepUnitID.isEmpty()
        && !indepUnitID.equalsIgnoreCase(Unit.Kind.DIMENSIONLESS.getName())) {
      String unitName = model.getUnitDefinition(indepUnitID).getName();
      indepXml.unit = unitName;
      indepXml.category = Categories.getTimeCategory().getName();
      indepXml.description = Categories.getTime();
    }

    // Get limits
    if (limits.containsKey(indepParam.getId())) {
      Limits indepLimits = limits.get(indepParam.getId());
      indepXml.max = indepLimits.getMax();
      indepXml.min = indepLimits.getMin();
    }

    // Parse Consts
    PmmXmlDoc paramCell = new PmmXmlDoc();
    for (Parameter param : model.getListOfParameters()) {
      if (!param.isConstant())
        continue;

      ParamXml paramXml = new ParamXml(param.getId(), null, param.getValue());

      String unitID = param.getUnits();
      if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
        String unitName = model.getUnitDefinition(unitID).getName();
        paramXml.unit = unitName;
        if (DBUnits.getDBUnits().containsKey(unitName)) {
          paramXml.category = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
        }
      }

      PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
      if (coefficient.isSetP()) {
        paramXml.P = coefficient.getP();
      }
      if (coefficient.isSetError()) {
        paramXml.error = coefficient.getError();
      }
      if (coefficient.isSetT()) {
        paramXml.t = coefficient.getT();
      }
      if (coefficient.isSetDescription()) {
        paramXml.description = coefficient.getDescription();
      }
      if (coefficient.isSetCorrelations()) {
        for (Correlation correlation : coefficient.getCorrelations()) {
          paramXml.correlations.put(correlation.getName(), correlation.getValue());
        }
      }
      // Adds limits
      if (limits.containsKey(param.getId())) {
        Limits constLimits = limits.get(param.getId());
        paramXml.max = constLimits.getMax();
        paramXml.min = constLimits.getMin();
      }
      if (coefficient.isSetIsStart()) {
        paramXml.isStartParam = coefficient.getIsStart();
      }
      paramCell.add(paramXml);
    }

    EstModelXml estModel = ReaderUtils.uncertainties2EstModel(m1Annot.getUncertainties());
    if (model.isSetName()) {
      estModel.name = model.getName();
    }

    Model1AgentXml agentXml = new Model1AgentXml(MathUtilities.getRandomNegativeInt(),
        species.getName(), species.getDetail(), null);

    PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
    Model1MatrixXml matrixXml = new Model1MatrixXml(MathUtilities.getRandomNegativeInt(),
        compartment.getName(), compartment.getDetail(), null);

    Model1Metadata metadata = new Model1Metadata();
    metadata.setAgentXml(agentXml);
    metadata.setMatrixXml(matrixXml);

    // Reads model literature
    PmmXmlDoc mLit = new PmmXmlDoc();
    for (Reference ref : rule.getReferences()) {
      String author = ref.getAuthor();
      Integer year = ref.getYear();
      String title = ref.getTitle();
      String abstractText = ref.getAbstractText();
      String journal = ref.getJournal();
      String volume = ref.getVolume();
      String issue = ref.getIssue();
      Integer page = ref.getPage();
      Integer approvalMode = ref.getApprovalMode();
      String website = ref.getWebsite();
      Integer type = ref.isSetType() ? ref.getType().value() : null;
      String comment = ref.getComment();

      LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal,
          volume, issue, page, approvalMode, website, type, comment);
      mLit.add(literatureItem);

      MLiteratureItem mLiteratureItem = new MLiteratureItem(author, year, title, abstractText,
          journal, volume, issue, page, approvalMode, website, type, comment);
      metadata.addLiteratureItem(mLiteratureItem);
    }

    // Reads estimated model literature
    PmmXmlDoc emLit = new PmmXmlDoc();
    for (Reference ref : m1Annot.getReferences()) {
      String author = ref.getAuthor();
      Integer year = ref.getYear();
      String title = ref.getTitle();
      String abstractText = ref.getAbstractText();
      String journal = ref.getJournal();
      String volume = ref.getVolume();
      String issue = ref.getIssue();
      Integer page = ref.getPage();
      Integer approvalMode = ref.getApprovalMode();
      String website = ref.getWebsite();
      Integer type = ref.isSetType() ? ref.getType().value() : null;
      String comment = ref.getComment();

      EMLiteratureItem emLiteratureItem = new EMLiteratureItem(author, year, title, abstractText,
          journal, volume, issue, page, approvalMode, website, type, comment);
      metadata.addLiteratureItem(emLiteratureItem);

      LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume,
          issue, page, approvalMode, website, type, comment);
      emLit.add(lit);
    }

    knimeTuple = new KnimeTuple(schema);
    knimeTuple.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
    knimeTuple.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
    knimeTuple.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc(indepXml));
    knimeTuple.setValue(Model1Schema.ATT_PARAMETER, paramCell);
    knimeTuple.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
    knimeTuple.setValue(Model1Schema.ATT_MLIT, mLit);
    knimeTuple.setValue(Model1Schema.ATT_EMLIT, emLit);
    knimeTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
    knimeTuple.setValue(Model1Schema.ATT_DBUUID, "?");
    knimeTuple.setValue(Model1Schema.ATT_METADATA, metadata);
  }
}
