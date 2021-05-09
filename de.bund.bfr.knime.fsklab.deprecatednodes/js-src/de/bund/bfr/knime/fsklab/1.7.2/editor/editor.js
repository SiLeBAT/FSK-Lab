fskeditorjs = function () {

  const view = { version: "1.7.2" };
  view.name = "Javascript FSK Editor";

  /**
   * Add controlled vocabulary to an input.
   * @param {Element} input Input element
   * @param {Array} vocabulary String array with vocabulary terms.
   */
  function addControlledVocabulary(input, vocabulary) {
    $(input).typeahead({
      source: vocabulary,
      autoSelect: true,
      fitToElement: true,
      showHintOnFocus: true
    });
  }

  // Handler for generic model schema
  class GenericModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;
      _metadata.modelMath.exposure = this.panels.exposure.data;

      _metadata.modelType = "GenericModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.genericModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        methodDialog: new Dialog("methodDialog", "Add method", schema.dietaryAssessmentMethod),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation),
        exposureDialog: new Dialog("exposureDialog", "Add exposure", schema.exposure)
      };
    }

    _createPanels() {

      let schema = schemas.genericModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        population: new TablePanel("Population", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        dietaryAssessmentMethod: new TablePanel("Dietary assessment method", this.dialogs.methodDialog,
          schema.dietaryAssessmentMethod, _metadata.dataBackground.dietaryAssessmentMethod),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation),
        exposure: new TablePanel("Exposure", this.dialogs.exposureDialog, schema.exposure, _metadata.modelMath.exposure)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
        { "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" },
        { "id": "exposure", "label": "Exposure" }]);
    }
  }

  class DataModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath.parameter = this.panels.parameter.data;

      _metadata.modelType = "DataModel";

      return _metadata;
    }

    _createDialogs() {

      let schema = schemas.dataModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        methodDialog: new Dialog("methodDialog", "Add method", schema.dietaryAssessmentMethod),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
      };
    }

    _createPanels() {

      let schema = schemas.dataModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference,
          _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        population: new TablePanel("Population", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        dietaryAssessmentMethod: new TablePanel("Dietary assessment method", this.dialogs.methodDialog,
          schema.dietaryAssessmentMethod, _metadata.dataBackground.dietaryAssessmentMethod),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "parameter", "label": "Parameter" }]);
    }
  }

  class PredictiveModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    // TODO: update get metaData
    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;
      _metadata.modelMath.exposure = this.panels.exposure.data;

      _metadata.modelType = "PredictiveModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.predictiveModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
      };
    }

    _createPanels() {

      let schema = schemas.predictiveModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "parameter", "label": "Parameter" }]);
    }
  }

  class OtherModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    get metaData() {

      // general information
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;

      _metadata.modelType = "OtherModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.otherModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation),
      };
    }

    _createPanels() {

      let schema = schemas.otherModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        population: new TablePanel("Population", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
        { "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" }]);
    }
  }

  // Handler for dose response model schema
  class DoseResponseModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    get metaData() {

      // general information
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;
      _metadata.modelMath.exposure = this.panels.exposure.data;

      _metadata.modelType = "DoseResponseModel";

      return _metadata;
    }

    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {
      let schema = schemas.doseResponseModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation),
        exposureDialog: new Dialog("exposureDialog", "Add exposure", schema.exposure)
      };
    }

    _createPanels() {
      let schema = schemas.doseResponseModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        population: new TablePanel("Population", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation),
        exposure: new FormPanel("Exposure", schema.exposure, _metadata.modelMath.exposure)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
        { "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" },
        { "id": "exposure", "label": "Exposure" }]);
    }
  }

  class ToxicologicalModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;
      _metadata.modelMath.exposure = this.panels.exposure.data;

      _metadata.modelType = "ToxicologicalModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.toxicologicalModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        methodDialog: new Dialog("methodDialog", "Add method", schema.dietaryAssessmentMethod),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation),
        exposureDialog: new Dialog("exposureDialog", "Add exposure", schema.exposure)
      };
    }

    _createPanels() {

      let schema = schemas.toxicologicalModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        population: new TablePanel("Population", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation),
        exposure: new TablePanel("Exposure", this.dialogs.exposureDialog, schema.exposure, _metadata.modelMath.exposure)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
        { "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" },
        { "id": "exposure", "label": "Exposure" }]);
    }
  }

  class ExposureModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    get metaData() {

      // general information
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;
      _metadata.modelMath.exposure = this.panels.exposure.data;

      _metadata.modelType = "ExposureModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.exposureModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        methodDialog: new Dialog("methodDialog", "Add method", schema.dietaryAssessmentMethod),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation),
        exposureDialog: new Dialog("exposureDialog", "Add exposure", schema.exposure)
      };
    }

    _createPanels() {

      let schema = schemas.exposureModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        population: new TablePanel("Population", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        dietaryAssessmentMethod: new TablePanel("Dietary assessment method", this.dialogs.methodDialog,
          schema.dietaryAssessmentMethod, _metadata.dataBackground.dietaryAssessmentMethod),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation),
        exposure: new TablePanel("Exposure", this.dialogs.exposureDialog, schema.exposure, _metadata.modelMath.exposure)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
        { "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" },
        { "id": "exposure", "label": "Exposure" }]);
    }
  }

  class ProcessModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    // TODO: update get metaData
    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.hazard = this.panels.hazard.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;

      _metadata.modelType = "ProcessModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.processModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation)
      };
    }

    _createPanels() {

      let schema = schemas.processModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" }]);
    }
  }

  class ConsumptionModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    // TODO: update get metaData
    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.populationGroup = this.panels.populationGroup.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;

      _metadata.modelType = "ConsumptionModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.consumptionModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        populationDialog: new Dialog("populationDialog", "Add population group", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        methodDialog: new Dialog("methodDialog", "Add dietary assessment method", schema.dietaryAssessmentMethod),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation)
      };
    }

    _createPanels() {

      let schema = schemas.consumptionModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        populationGroup: new TablePanel("Population group", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        dietaryAssessmentMethod: new TablePanel("Dietary assessment method", this.dialogs.methodDialog,
          schema.dietaryAssessmentMethod, _metadata.dataBackground.dietaryAssessmentMethod),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "populationGroup", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" }]);
    }
  }

  class HealthModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    // TODO: update get metaData
    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.populationGroup.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;
      _metadata.modelMath.exposure = this.panels.exposure.data;

      _metadata.modelType = "HealthModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.healthModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population group", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation),
        exposureDialog: new Dialog("exposureDialog", "Add exposure", schema.exposure)
      };
    }

    _createPanels() {

      let schema = schemas.healthModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        populationGroup: new TablePanel("Population group", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation),
        exposure: new TablePanel("Exposure", this.dialogs.exposureDialog, schema.exposure, _metadata.modelMath.exposure)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "populationGroup", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
        { "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" },
        { "id": "exposure", "label": "Exposure" }]);
    }
  }

  // Handler for generic model schema
  class RiskModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;
      _metadata.modelMath.exposure = this.panels.exposure.data;

      _metadata.modelType = "RiskModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.genericModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        methodDialog: new Dialog("methodDialog", "Add method", schema.dietaryAssessmentMethod),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation),
        exposureDialog: new Dialog("exposureDialog", "Add exposure", schema.exposure)
      };
    }

    _createPanels() {

      let schema = schemas.genericModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        population: new TablePanel("Population", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        dietaryAssessmentMethod: new TablePanel("Dietary assessment method", this.dialogs.methodDialog,
          schema.dietaryAssessmentMethod, _metadata.dataBackground.dietaryAssessmentMethod),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation),
        exposure: new TablePanel("Exposure", this.dialogs.exposureDialog, schema.exposure, _metadata.modelMath.exposure)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
        { "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" },
        { "id": "exposure", "label": "Exposure" }]);
    }
  }

  class QraModel {

    constructor() {
      this.dialogs = this._createDialogs();
      this.panels = this._createPanels();
      this.menus = this._createMenus();
    }

    get metaData() {

      // generalInformation
      _metadata.generalInformation = this.panels.generalInformation.data;
      _metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
      _metadata.generalInformation.author = this.panels.author.data;
      _metadata.generalInformation.creator = this.panels.creator.data;
      _metadata.generalInformation.reference = this.panels.reference.data;

      // Ignore temporarily publication type
      // TODO: publicationType takes the abbreviation instead of the full string
      // used in the Reference dialog. Since KNIME runs getComponentValue twice,
      // the value cannot be converted here. The 1st call to getComponentValue
      // would get the abbreviation but the 2nd call would corrupt it. The HTML
      // select should instead use the full string as label and the abreviation
      // as value.
      _metadata.generalInformation.reference.forEach(ref => ref.publicationType = null);

      // Scope
      _metadata.scope = this.panels.scopeGeneral.data;
      _metadata.scope.product = this.panels.product.data;
      _metadata.scope.hazard = this.panels.hazard.data;
      _metadata.scope.populationGroup = this.panels.population.data;

      // Data background
      _metadata.dataBackground.study = this.panels.study.data;
      _metadata.dataBackground.studySample = this.panels.studySample.data;
      _metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
      _metadata.dataBackground.laboratory = this.panels.laboratory.data;
      _metadata.dataBackground.assay = this.panels.assay.data;

      // Model math
      _metadata.modelMath = this.panels.modelMath.data;
      _metadata.modelMath.parameter = this.panels.parameter.data;
      _metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
      _metadata.modelMath.modelEquation = this.panels.modelEquation.data;
      _metadata.modelMath.exposure = this.panels.exposure.data;

      _metadata.modelType = "QraModel";

      return _metadata;
    }

    // Validate this.panels and return boolean
    validate() {
      let isValid = true;
      if (!this.panels.generalInformation.validate()) isValid = false;
      if (!this.panels.modelCategory.validate()) isValid = false;
      if (!this.panels.scopeGeneral.validate()) isValid = false;
      if (!this.panels.study.validate()) isValid = false;
      return isValid;
    }

    _createDialogs() {

      let schema = schemas.qraModel;

      return {
        authorDialog: new Dialog("authorDialog", "Add dialog", schema.contact),
        creatorDialog: new Dialog("creatorDialog", "Add creator", schema.contact),
        referenceDialog: new Dialog("referenceDialog", "Add reference", schema.reference),
        productDialog: new Dialog("productDialog", "Add product", schema.product),
        hazardDialog: new Dialog("hazardDialog", "Add hazard", schema.hazard),
        populationDialog: new Dialog("populationDialog", "Add population", schema.populationGroup),
        studySampleDialog: new Dialog("studySampleDialog", "Add study sample", schema.studySample),
        methodDialog: new Dialog("methodDialog", "Add method", schema.dietaryAssessmentMethod),
        laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", schema.laboratory),
        assayDialog: new Dialog("assayDialog", "Add assay", schema.assay),
        parameterDialog: new Dialog("parameterDialog", "Add parameter", schema.parameter),
        measuresDialog: new Dialog("measuresDialog", "Add quality measures", schema.qualityMeasures),
        equationDialog: new Dialog("equationDialog", "Add model equation", schema.modelEquation),
        exposureDialog: new Dialog("exposureDialog", "Add exposure", schema.exposure)
      };
    }

    _createPanels() {

      let schema = schemas.qraModel;

      return {
        generalInformation: new FormPanel("General", schema.generalInformation, _metadata.generalInformation),
        modelCategory: new FormPanel("Model category", schema.modelCategory, _metadata.generalInformation.modelCategory),
        author: new TablePanel("Author", this.dialogs.authorDialog, schema.contact, _metadata.generalInformation.author),
        creator: new TablePanel("Creator", this.dialogs.creatorDialog, schema.contact, _metadata.generalInformation.creator),
        reference: new TablePanel("Reference", this.dialogs.referenceDialog, schema.reference, _metadata.generalInformation.reference),
        scopeGeneral: new FormPanel("General", schema.scope, _metadata.scope),
        product: new TablePanel("Product", this.dialogs.productDialog, schema.product, _metadata.scope.product),
        hazard: new TablePanel("Hazard", this.dialogs.hazardDialog, schema.hazard, _metadata.scope.hazard),
        population: new TablePanel("Population", this.dialogs.populationDialog, schema.populationGroup,
          _metadata.scope.populationGroup),
        study: new FormPanel("Study", schema.study, _metadata.dataBackground.study),
        studySample: new TablePanel("Study sample", this.dialogs.studySampleDialog, schema.studySample,
          _metadata.dataBackground.studySample),
        dietaryAssessmentMethod: new TablePanel("Dietary assessment method", this.dialogs.methodDialog,
          schema.dietaryAssessmentMethod, _metadata.dataBackground.dietaryAssessmentMethod),
        laboratory: new TablePanel("Laboratory", this.dialogs.laboratoryDialog, schema.laboratory,
          _metadata.dataBackground.laboratory),
        assay: new TablePanel("Assay", this.dialogs.assayDialog, schema.assay, _metadata.dataBackground.assay),
        modelMath: new FormPanel("Model math", schema.modelMath, _metadata.modelMath),
        parameter: new TablePanel("Parameter", this.dialogs.parameterDialog, schema.parameter, _metadata.modelMath.parameter),
        qualityMeasures: new TablePanel("Quality measures", this.dialogs.measuresDialog, schema.qualityMeasures,
          _metadata.modelMath.qualityMeasures),
        modelEquation: new TablePanel("Model equation", this.dialogs.equationDialog, schema.modelEquation,
          _metadata.modelMath.modelEquation),
        exposure: new TablePanel("Exposure", this.dialogs.exposureDialog, schema.exposure, _metadata.modelMath.exposure)
      };
    }

    _createMenus() {
      return createSubMenu("General information", [
        { "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }]) +
        createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }]) +
        createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
        { "id": "studySample", "label": "Study sample" },
        { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
        { "id": "laboratory", "label": "Laboratory" },
        { "id": "assay", "label": "Assay" }]) +
        createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
        { "id": "parameter", "label": "Parameter" },
        { "id": "qualityMeasures", "label": "Quality measures" },
        { "id": "modelEquation", "label": "Model equation" },
        { "id": "exposure", "label": "Exposure" }]);
    }
  }

  // Hash of publication types full names to keys.
  // See more at https://en.wikipedia.org/w/index.php?title=RIS_(file_format)
  const RIS_TYPES = {
    "Abstract": "ABST",
    "Audiovisual material": "ADVS",
    "Aggregated Database": "AGGR",
    "Ancient Text": "ANCIENT",
    "Art Work": "ART",
    "Bill": "BILL",
    "Blog": "BLOG",
    "Whole book": "BOOK",
    "Case": "CASE",
    "Book chapter": "CHAP",
    "Chart": "CHART",
    "Classical Work": "CLSWK",
    "Computer program": "COMP",
    "Conference proceeding": "CONF",
    "Conference paper": "CPAPER",
    "Catalog": "CTLG",
    "Data file": "DATA",
    "Online Database": "DBASE",
    "Dictionary": "DICT",
    "Electronic Book": "EBOOK",
    "Electronic Book Section": "ECHAP",
    "Edited Book": "EDBOOK",
    "Electronic Article": "EJOUR",
    "Web Page": "ELEC",
    "Encyclopedia": "ENCYC",
    "Equation": "EQUA",
    "Figure": "FIGURE",
    "Generic": "GEN",
    "Government Document": "GOVDOC",
    "Grant": "GRANT",
    "Hearing": "HEAR",
    "Internet Communication": "ICOMM",
    "In Press": "INPR",
    "Journal (full)": "JFULL",
    "Journal": "JOUR",
    "Legal Rule or Regulation": "LEGAL",
    "Manuscript": "MANSCPT",
    "Map": "MAP",
    "Magazine article": "MGZN",
    "Motion picture": "MPCT",
    "Online Multimedia": "MULTI",
    "Music score": "MUSIC",
    "Newspaper": "NEWS",
    "Pamphlet": "PAMP",
    "Patent": "PAT",
    "Personal communication": "PCOMM",
    "Report": "RPRT",
    "Serial publication": "SER",
    "Slide": "SLIDE",
    "Sound recording": "SOUND",
    "Standard": "STAND",
    "Statute": "STAT",
    "Thesis/Dissertation": "THES",
    "Video recording": "VIDEO"
  }

  /**
   * Create an horizontal form for a metadata property. Missing values with
   * *null* or *undefined* are replaced with an empty string.
   * 
   * @param {object} prop Metadata property. It can be of type: text, number,
   *  url, data, boolean, text-array and date-array.
   * @param {string} value Input value. It can be *null* or *undefined* for
   *  missing values.
   * 
   * @returns InputForm or ArrayForm for the supported type. If wrong type
   *  it returns undefined.
   */
  function createForm(prop, value) {
    let vocabulary = prop.vocabulary ? vocabularies[prop.vocabulary] : null;
    let isMandatory = prop.required ? prop.required : false;

    if (prop.type === "text" || prop.type === "number" || prop.type === "url" ||
      prop.type === "date")
      return new InputForm(prop.label, isMandatory, prop.type, prop.description,
        value ? value : "", vocabulary);

    if (prop.type === "enum")
      return new SelectForm(prop.label, isMandatory, prop.description, value,
        vocabulary);

    if (prop.type === "boolean")
      return new InputForm(prop.label, isMandatory, "checkbox",
        prop.description, value, prop.description);

    if (prop.type === "text-array")
      return new ArrayForm(prop.label, isMandatory, prop.type,
        value ? value : [], prop.description, vocabulary);

    if (prop.type === "date-array")
      return new ArrayForm(prop.label, isMandatory, prop.type,
        value ? value : [], prop.description, vocabulary);
  }

  /**
   * Create a div to edit string arrays.
   * 
   * ```
   * <div class="panel panel-default">
   *   <div class="panel-heading clearfix">
   *     <h4 class="panel-title pull-left" style="padding-top:7.5px;">Title</h4>
   *     <div class="input-group">
   *       <p class="pull-right" /> <!-- gutter -->
   *       <div class="input-group-btn">
   *         <button type="button" class="btn btn-default" data-toggle="modal" data-target="#">
   *           <i class="glyphicon glyphicon-plus"></i>
   *         </button>
   *         <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
   *         <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>
   *       </div>
   *      </div>
   *    </div>
   *   <table id="${table}" class="table"></table>
   * </div>
   * ```
   */
  class ArrayForm {

    constructor(name, mandatory, type, value, helperText, vocabulary) {
      this.group = document.createElement("div");
      this.simpleTable = new SimpleTable(type, value, vocabulary);
      this._create(name, mandatory, helperText);
    }

    _create(name, mandatory, helperText) {

      // Create buttons with icons
      let addButton = document.createElement("button");
      addButton.type = "button";
      addButton.classList.add("btn", "btn-default");
      addButton.innerHTML = '<i class="glyphicon glyphicon-plus"></i>';
      addButton.onclick = () => this.simpleTable.add();

      let removeButton = document.createElement("button");
      removeButton.type = "button";
      removeButton.classList.add("btn", "btn-default");
      removeButton.innerHTML = '<i class="glyphicon glyphicon-remove"></i>';
      removeButton.onclick = () => this.simpleTable.remove();

      let trashButton = document.createElement("button");
      trashButton.type = "button";
      trashButton.classList.add("btn", "btn-default");
      trashButton.innerHTML = '<i class="glyphicon glyphicon-trash"></i>';
      trashButton.onclick = () => this.simpleTable.trash();

      // Create buttonDiv with buttons
      let buttonDiv = document.createElement("div");
      buttonDiv.classList.add("input-group-btn");
      buttonDiv.appendChild(addButton);
      buttonDiv.appendChild(removeButton);
      buttonDiv.appendChild(trashButton);

      // Create input-group
      let inputGroup = document.createElement("div");
      inputGroup.classList.add("input-group");
      inputGroup.innerHTML = '<p class="pull-right" />'; // gutter
      inputGroup.appendChild(buttonDiv);

      // Create panel-heading
      let panelHeading = document.createElement("div");
      panelHeading.classList.add("panel-heading", "clearfix");
      panelHeading.appendChild(inputGroup);

      // Create panel in group
      let panelDiv = document.createElement("div");
      panelDiv.classList.add("panel", "panel-default");
      panelDiv.appendChild(panelHeading);
      panelDiv.appendChild(this.simpleTable.table);

      let formDiv = document.createElement("div");
      formDiv.className = "col-sm-10";
      formDiv.appendChild(panelDiv);
      formDiv.title = helperText;

      this.group.classList.add("form-group", "row");
      this.group.appendChild(createLabel(name, mandatory, helperText));
      this.group.appendChild(formDiv);
    }

    get value() {
      return this.simpleTable.value;
    }

    set value(newValue) {
      this.simpleTable.trash();
      newValue.forEach(item => this.simpleTable._createRow(item));
    }

    clear() {
      this.simpleTable.trash();
    }

    /**
     * @returns {boolean} If the input is valid.
     */
    validate() {
      // TODO: Implement validate in ArrayForm
      return true;
    }
  }

  class SimpleTable {

    constructor(type, data, vocabulary) {
      this.type = type === "text-array" ? "text" : "date";
      this.vocabulary = vocabulary;

      this.table = document.createElement("table");
      this.table.className = "table";
      this.table.innerHTML = `<thead><thead>`;

      this.body = document.createElement("tbody");
      this.table.appendChild(this.body);

      data.forEach(value => this._createRow(value));
    }

    /**
     * Create new row to enter data if the last row value is not empty.
     */
    add() {
      // If it has no rows or the last row value is not empty
      if (!this.body.lastChild || this.body.lastChild.lastChild.firstChild.value) {
        this._createRow();
      }
    }

    remove() {
      // Find checked rows and delete them
      Array.from(this.body.children).forEach(row => {
        // Get checkbox (tr > td > input)
        let checkbox = row.firstChild.firstChild;
        if (checkbox.checked) {
          this.body.removeChild(row);
        }
      });
    }

    /**
     * Remove every row in the table
     */
    trash() {
      this.body.innerHTML = "";
    }

    _createRow(value = "") {
      let input = document.createElement("input");
      input.type = this.type;
      input.className = "form-control";
      input.value = value;

      // Add autocomplete to input with vocabulary
      if (this.vocabulary) {
        addControlledVocabulary(input, this.vocabulary);
      }

      // If enter is pressed when the input if focused, lose focus and add a
      // new row (like clicking the add button). The new input from calling add
      // is focused.
      input.addEventListener("keyup", (event) => {
        if (event.key === "Enter") {
          input.blur();
          this.add();
        }
      });

      // Create cell with input
      let inputCell = document.createElement("td");
      inputCell.appendChild(input);

      // Create row with checkbox and input
      let newRow = document.createElement("tr");
      newRow.innerHTML = '<td><input type="checkbox"></td>'
      newRow.appendChild(inputCell);

      // Add row
      this.body.appendChild(newRow);

      input.focus(); // Focus the new input      
    }

    get value() {
      let data = [];
      this.body.childNodes.forEach(tr => {
        let inputCell = tr.lastChild; // 2nd cell (with input)
        let input = inputCell.firstChild; // <input>
        data.push(input.value);
      });

      return data;
    }
  }

  class AdvancedTable {

    constructor(data, formData, dialog, panel) {
      this.formData = formData;
      this.dialog = dialog;
      this.panel = panel;

      this.table = document.createElement("table");
      this.table.className = "table";

      // Apply striped rows if table has over 10 rows.
      if (data && data.length > 10) {
        this.table.classList.add("table-striped");
      }

      // Create headers (1 extra columns at the end for buttons)
      let head = document.createElement("thead");
      head.innerHTML = `<tr>
        ${this.formData.map(prop => `<th>${prop.label}</th>`).join("")}
        <th></th>
      </tr>`;

      this.body = document.createElement("tbody");
      this.table.appendChild(head);
      this.table.appendChild(this.body);

      if (data) {
        data.forEach(entry => this.add(entry));
      }
    }

    /**
     * Create a new row with new metadata from a dialog.
     * 
     * @param {Object} data JSON object with new metadata.
     */
    add(data) {

      // Add new row (Order is fixed by formData)
      let newRow = document.createElement("tr");

      this.formData.forEach(prop => {
        // Get value for the current property
        let value = data[prop.id] ? data[prop.id] : "";

        let cell = document.createElement("td");
        if (prop.type === "boolean" && value) {
          cell.innerHTML = '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>';
        } else {
          cell.title = value; // Set the whole value as tooltip
          cell.textContent = value.length > 20 ? value.substring(0, 24) + "..." : value;
        }
        newRow.appendChild(cell);
      });

      let editButton = document.createElement("button");
      editButton.classList.add("btn", "btn-primary", "btn-sm");
      editButton.innerHTML = '<i class="glyphicon glyphicon-edit"></i>';
      editButton.title = "Edit";
      editButton.onclick = (e) => {

        // Get current row (button > btn-group > td > tr). It starts at 1
        // (it counts the header)
        let rowIndex = e.currentTarget.parentNode.parentNode.parentNode.rowIndex - 1;

        // Update inputs in dialog
        let originalData = this.panel.data[rowIndex];
        for (let prop in originalData) {
          this.dialog.inputs[prop].value = originalData[prop];
        }

        this.dialog.editedRow = rowIndex;
        $(this.dialog.modal).modal('show');
      }

      // Remove button
      let removeButton = document.createElement("button");
      removeButton.classList.add("btn", "btn-warning", "btn-sm");
      removeButton.innerHTML = '<i class="glyphicon glyphicon-remove"></i>';
      removeButton.onclick = (e) => {
        // Get current row (button > btn-group > td > tr). It starts at 1
        // (it counts the header)
        let rowIndex = e.currentTarget.parentNode.parentNode.parentNode.rowIndex - 1;
        this.panel.remove(rowIndex);
      };

      removeButton.title = "Remove";

      let btnGroup = document.createElement("div");
      btnGroup.className = "btn-group";
      btnGroup.setAttribute("role", "group");
      btnGroup.appendChild(editButton);
      btnGroup.appendChild(removeButton);

      let buttonCell = document.createElement("td");
      buttonCell.appendChild(btnGroup);
      newRow.appendChild(buttonCell);

      this.body.appendChild(newRow);
    }

    edit(rowNumber, data) {
      let row = this.body.childNodes[rowNumber];

      for (let i = 0; i < this.formData.length; i++) {
        let prop = this.formData[i];
        let cell = row.childNodes[i];

        let value = data[prop.id];
        cell.title = value;
        cell.textContent = value.length > 25 ? value.substring(0, 24) : value;
      }
    }

    /**
     * Remove row at the given index.
     */
    remove(index) {
      this.body.removeChild(this.body.childNodes[index]);
    }

    /**
     * Remove every row in the table.
     */
    trash() {
      this.body.innerHTML = "";
    }
  }

  /**
   * Bootstrap 3 form-group for an input.
   */
  class InputForm {

    /**
     * Create a Bootstrap 3 form-group.
     * 
     * ```
     * <div class="form-group row">
     *   <label>name</label>
     *   <div class="col-sm-10">
     *     <input type="text">
     *   </div>
     * </div>`;
     * ```
     * 
     * If type === checkbox
     * ```
     * <div class="form-group row">
     *   <label >name</label>
     *   <div class="col-sm-10">
     *     <input class="form-check-input" type="checkbox" checked="">
   	 *	 </div>
	   * </div>
     * ```
     * 
     * @param {string} name Property name
     * @param {boolean} mandatory `true` if mandatory, `false` if optional.
     * @param {string} type Property type: text, url, checkbox, etc.
     * @param {string} helperText Tooltip
     * @param {string} value Initial value of the property.
     * @param {Array} vocabulary List of possible value for autocompletion.
     */
    constructor(name, mandatory, type, helperText, value, vocabulary = null) {

      this.name = name;
      this.mandatory = mandatory;
      this.type = type;
      this.helperText = helperText;

      this.input = document.createElement("input");
      this.group = document.createElement("div");
      this._create(name, mandatory, type, helperText, value, vocabulary);
    }

    /**
     * @param {string} name Property name
     * @param {boolean} mandatory `true` if mandatory, `false` if optional.
     * @param {string} type Property type: text, url, checkbox, etc.
     * @param {string} helperText Tooltip
     * @param {string} value Initial value of the property.
     * @param {Array} vocabulary List of possible value for autocompletion.
     */
    _create(name, mandatory, type, helperText, value, vocabulary) {

      // Create input
      this.input.className = type === "checkbox" ? "form-check-input" : "form-control";
      this.input.type = type;
      this.input.value = value;
      this.input.title = helperText;

      // Create div for input
      let inputDiv = document.createElement("div");
      inputDiv.classList.add("col-sm-10");
      inputDiv.appendChild(this.input);
      if (mandatory) {
        this.helpBlock = document.createElement("span");
        this.helpBlock.className = "help-block";
        this.helpBlock.style.display = "none";
        this.helpBlock.textContent = `${name} is a required property`;
        inputDiv.appendChild(this.helpBlock);
      }

      // Add autocomplete to input with vocabulary
      if (vocabulary) {
        addControlledVocabulary(this.input, vocabulary);
      }

      // Collect everything into group
      this.group.classList.add("form-group", "row");
      this.group.appendChild(createLabel(name, mandatory, helperText));
      this.group.appendChild(inputDiv);
    }

    get value() {
      return this.type !== "checkbox" ? this.input.value : this.input.checked;
    }

    set value(newValue) {
      this.input.value = newValue;
    }

    clear() {
      this.input.value = "";

      if (this.helpBlock) {
        this.helpBlock.style.display = "none";
      }

      // Remove validation classes
      this.group.classList.remove("has-success", "has-error");
    }

    /**
     * @returns {boolean} If the input is valid.
     */
    validate() {

      let isValid;
      if (!this.mandatory) {
        isValid = true;
      } else {
        isValid = this.input.value ? true : false;
      }

      if (!isValid) {
        this.helpBlock.style.display = "block";
      }

      // Remove validation classes
      this.group.classList.remove("has-success", "has-error");

      // Add new validation class
      this.group.classList.add(isValid ? "has-success" : "has-error");

      return isValid;
    }
  }

  /**
   * Bootstrap 3 form with a select.
   */
  class SelectForm {

    /**
     * Create a Bootstrap 3 form-group with a select.
     * 
     * ```
     * <div class="form-group row">
     *   <label>name</label>
     *   <select class="form-control">
     *     <option>1</option>
     *     <option>2</option>
     *   </select>
     * </div>```
     */
    constructor(name, mandatory, helperText, value, vocabulary = null) {

      this.select = document.createElement("select");
      this.group = document.createElement("div");

      this._create(name, mandatory, helperText, value, vocabulary);
    }

    _create(name, mandatory, helperText, value, vocabulary) {

      this.select.className = "form-control";
      // Add options from vocabulary. The option matching value is selected.
      this.select.innerHTML = vocabulary.map(item => `<option>${item}</option>`)
        .join("");
      this.select.value = value;
      this.select.title = helperText;

      // Create div for select
      let selectDiv = document.createElement("div");
      selectDiv.className = "col-sm-10";
      selectDiv.appendChild(this.select);

      // this.group
      this.group.classList.add("form-group", "row");
      this.group.appendChild(createLabel(name, mandatory, helperText));
      this.group.appendChild(selectDiv);
    }

    get value() {
      return this.select.value;
    }

    set value(newValue) {
      this.select.value = newValue;
    }

    clear() {
      this.select.value = "";
    }

    /**
     * @returns {boolean} If the input is valid.
     */
    validate() {

      let isValid;
      if (!this.mandatory) {
        isValid = true;
      } else {
        isValid = this.input.value ? true : false;
      }

      // Remove validation classes
      this.group.classList.remove("has-success", "has-error");

      // Add new validation class
      this.group.classList.add(isValid ? "has-success" : "has-error");

      return isValid;
    }
  }

  /**
   * Create a Bootstrap 3 modal dialog.
   */
  class Dialog {

    /**
     * Create a Bootstrap 3 modal dialog.
     * 
     * ```
     * <div class="modal-fade">
     *   <div class="modal-dialog" role="document">
     *     <div class="modal-content">
     *       <div class="modal-header">
     *         <button>
     *           <span>
     *         </button>
     *         <h4 class="modal-title">title</h4>
     *       </div>
     *       <div class="modal-body">
     *         <form>...</form>
     *       </div>
     *       <div class="modal-footer">
     *         <button type="button">Close</button>
     *         <button type="button">Save changes</button>
     *       </div>
     *     </div>
     *   </div>
     * </div>
     * ```
     * 
     * @param {id} id Dialog id
     * @param {title} title Dialog title
     * @param {formData} formData Object with form data
     */
    constructor(id, title, formData) {
      this.inputs = {};  // Hash of inputs by id

      // Index of the row currently edited. It is -1 if no row is being edited.
      // This is the case of when a new row is added.
      this.editedRow = -1;

      this.modal = document.createElement("div");
      this.create(id, title, formData);
    }

    create(id, title, formData) {

      // modal body
      let form = document.createElement("form");
      formData.forEach(prop => {
        let inputForm = createForm(prop);
        if (inputForm) {
          form.appendChild(inputForm.group);
          this.inputs[prop.id] = inputForm;
        }
      });

      let modalBody = document.createElement("div");
      modalBody.classList.add("modal-body");
      modalBody.appendChild(form);

      // modal footer
      let closeButton = document.createElement("button");
      closeButton.type = "button";
      closeButton.classList.add("btn", "btn-default");
      closeButton.setAttribute("data-dismiss", "modal");
      closeButton.textContent = "Close";

      let saveButton = document.createElement("button");
      saveButton.type = "button";
      saveButton.classList.add("btn", "btn-primary");
      saveButton.textContent = "Save changes";
      saveButton.onclick = () => {

        // Validate inputs and stop saving if errors are found.
        let hasError = false;
        Object.values(this.inputs).forEach(input => {
          if (!input.validate()) hasError = true;
        });
        if (hasError) return;

        $(this.modal).modal('hide');

        // Retrieve data and clear inputs
        let data = {};
        for (const inputId in this.inputs) {
          let currentInput = this.inputs[inputId];
          data[inputId] = currentInput.value; // Save input value
          currentInput.clear(); // Clear input
        }

        if (this.editedRow != -1) {
          this.panel.edit(this.editedRow, data);
          this.editedRow = -1;
          this.inputs.forEach(input => input.clear()); // Clear inputs
        } else {
          this.panel.add(data);
        }
      }

      let footer = document.createElement("div");
      footer.classList.add("modal-footer");
      footer.appendChild(closeButton);
      footer.appendChild(saveButton);

      let content = document.createElement("div");
      content.classList.add("modal-content");
      content.innerHTML = `<div class="modal-header">
      <button class="close" data-dismiss="modal" aria-label="Close">
        <span aria-hidden="true">&times;</span>
      </button>
      <h4 class="modal-title">${title}</h4>
      </div>`;
      content.appendChild(modalBody);
      content.appendChild(footer);

      let modalDialog = document.createElement("div");
      modalDialog.classList.add("modal-dialog", "modal-dialog-centered");
      modalDialog.setAttribute("role", "document");
      modalDialog.appendChild(content);

      this.modal.classList.add("modal", "fade");
      this.modal.id = id;
      this.modal.tabIndex = -1;
      this.modal.setAttribute("role", "dialog");
      this.modal.appendChild(modalDialog);
    }
  }

  /**
   * Create a Bootstrap 3 panel with controls in the heading and a table as body.
   * 
   * ```
   * <div class="panel panel-default">
   *   <div class="panel-heading clearfix">
   *     <h4 class="panel-title pull-left" style="padding-top:7.5px;">${title}</h4>
   *     <div class="input-group">
   *       <p class="pull-right" /> <!-- gutter -->
   *       <div class="input-group-btn">
   *         <button type="button" class="btn btn-default" data-toggle="modal" data-target="#${dialog}">
   *           <i class="glyphicon glyphicon-plus"></i>
   *         </button>
   *         <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
   *         <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>
   *       </div>
   *     </div>
   *   </div>
   *   <table class="table">
   *     <tr>
   *       <th><input type="checkbox"></th>
   *     </tr>
   *   </table>
   * </div>`
   * ```
   */
  class TablePanel {

    /**
     * Create a TablePanel.
     * 
     * @param {string} title Panel title.  
     * @param {Dialog} dialog Reference to Dialog object. This Dialog is later
     *   used for adding new entries and editing existing ones. 
     * @param {object} formData Related data from the UI schema.
     * @param {object} data Initial data of the table.
     */
    constructor(title, dialog, formData, data) {

      this.panel = document.createElement("div");

      this.table = new AdvancedTable(data, formData, dialog, this);

      this._create(title, dialog, formData);
      this.data = data ? data : []; // Initialize null or undefined data

      // Register this panel in dialog (TODO: this should be done in Dialog's constr)
      this.dialog = dialog;
      this.dialog.panel = this;
    }

    /**
     * Create UI of the TablePanel.
     * 
     * @param {string} title Panel title.  
     * @param {Dialog} dialog Reference to Dialog object. This Dialog is later
     *   used for adding new entries and editing existing ones. 
     * @param {object} formData Related data from the UI schema.
     */
    _create(title, dialog, formData) {

      // Add button
      let addButton = document.createElement("button");
      addButton.classList.add("btn", "btn-primary");
      addButton.innerHTML = '<i class="glyphicon glyphicon-plus"></i>';
      addButton.title = "Add a " + title;
      addButton.onclick = () => {
        Object.values(dialog.inputs).forEach(input => input.clear());
        $(dialog.modal).modal('show');
      };

      // Trash button
      let trashButton = document.createElement("button");
      trashButton.classList.add("btn", "btn-danger");
      trashButton.innerHTML = '<i class="glyphicon glyphicon-trash"></i>';
      trashButton.onclick = () => this.removeAll();
      trashButton.title = `Remove all ${title}(s)`;

      // input-group-btn
      let inputGroupBtn = document.createElement("div");
      inputGroupBtn.className = "input-group-btn";
      inputGroupBtn.appendChild(addButton);
      inputGroupBtn.appendChild(trashButton);

      // input-group
      let inputGroup = document.createElement("div");
      inputGroup.className = "input-group";
      inputGroup.innerHTML = '<p class="pull-right" />'; // gutter
      inputGroup.appendChild(inputGroupBtn);

      // panel heading
      let panelHeading = document.createElement("div");
      panelHeading.classList.add("panel-heading", "clearfix");
      panelHeading.innerHTML = `<h4 class="panel-title pull-left" style="padding-top:7.5px;">${title}</h4>`;
      panelHeading.appendChild(inputGroup);

      // content div: responsive div for the table (no overflows)
      let content = document.createElement("div");
      content.className = "table-responsive";
      content.appendChild(this.table.table);

      // panel
      this.panel.classList.add("panel", "panel-default");
      this.panel.appendChild(panelHeading);
      this.panel.appendChild(content);
    }

    add(data) {
      this.data.push(data); // add data
      this.table.add(data); // Update table
    }

    edit(index, newData) {
      this.data[index] = newData; // Update this.data
      this.table.edit(index, newData); // Update table
    }

    remove(index) {
      this.data.splice(index, 1); // Update this.data
      this.table.remove(index); // TODO: update table
    }

    removeAll() {
      this.data = []; // Clear data
      this.table.trash();  // Empty table
    }
  }

  /**
   * Simple panel for non nested data like General information, study, etc.
   */
  class FormPanel {

    constructor(title, formData, data) {
      this.panel = document.createElement("div");
      this.inputs = {};

      this._create(title, formData, data);
    }

    /**
     * ```
     * <div class="panel panel-default">
     *   <div class="panel-heading">
     *     <h3 class="panel-title">Some title</h3>
     *   </div>
     *   <div class="panel-body">
     *     <form></form>
     *   </div>
     * </div>
     * ```
     * @param {*} title 
     * @param {*} formData 
     */
    _create(title, formData, data) {

      this.panel.classList.add("panel", "panel-default");
      this.panel.innerHTML = `<div class="panel-heading">
      <h3 class="panel-title">${title}</h3>
      </div>`;

      let form = document.createElement("form");
      formData.forEach(prop => {
        let inputForm = createForm(prop, data ? data[prop.id] : null);
        if (inputForm) {
          form.appendChild(inputForm.group);
          this.inputs[prop.id] = inputForm;
        }
      });

      let body = document.createElement("div");
      body.className = "panel-body";
      body.appendChild(form);

      this.panel.appendChild(body);
    }

    validate() {
      let isValid = true;
      Object.values(this.inputs).forEach(input => {
        if (!input.validate()) isValid = false;
      });
      return isValid;
    }

    get data() {
      let data = {};
      Object.entries(this.inputs).forEach(([id, input]) => data[id] = input.value);
      return data;
    }
  }

  var _rep;
  var _val;

  var _metadata = {
    generalInformation: {},
    scope: {},
    dataBackground: {},
    modelMath: {}
  };

  var _modelCodeMirror;
  var _visualizationCodeMirror;
  var _readmeCodeMirror;

  let handler;

  view.init = function (representation, value) {

    _rep = representation;
    _val = value;

    if (!value.modelMetaData || value.modelMetaData == "null" || value.modelMetaData == "") {
      _metadata.generalInformation = {};
      _metadata.generalInformation.modelCategory = {};
      _metadata.scope = {};
      _metadata.modelMath = {};
      _metadata.dataBackground = {}
    } else {
      let metaData = JSON.parse(value.modelMetaData);

      if (!metaData.generalInformation) {
        _metadata.generalInformation = { modelCategory: {} };
      } else {
        _metadata.generalInformation = metaData.generalInformation;
      }

      _metadata.scope = metaData.scope ? metaData.scope : {};
      _metadata.dataBackground = metaData.dataBackground ? metaData.dataBackground : {};
      _metadata.modelMath = metaData.modelMath ? metaData.modelMath : {};
      _metadata.modelType = metaData.modelType;
    }

    if (value.modelType === "genericModel") {
      handler = new GenericModel();
    } else if (value.modelType === "dataModel") {
      handler = new DataModel();
    } else if (value.modelType === "predictiveModel") {
      handler = new PredictiveModel();
    } else if (value.modelType === "otherModel") {
      handler = new OtherModel();
    } else if (value.modelType === "toxicologicalModel") {
      handler = new ToxicologicalModel();
    } else if (value.modelType === "doseResponseModel") {
      handler = new DoseResponseModel();
    } else if (value.modelType === "exposureModel") {
      handler = new ExposureModel();
    } else if (value.modelType === "processModel") {
      handler = new ProcessModel();
    } else if (value.modelType === "consumptionModel") {
      handler = new ConsumptionModel();
    } else if (value.modelType === "healthModel") {
      handler = new HealthModel();
    } else if (value.modelType === "riskModel") {
      handler = new RiskModel();
    } else if (value.modelType === "qraModel") {
      handler = new QraModel();
    } else {
      handler = new GenericModel();
    }

    createUI();
  }

  view.getComponentValue = () => {

    _metadata = handler.metaData;
    let metaDataString = JSON.stringify(_metadata);

    let viewValue = {
      modelMetaData: metaDataString,
      firstModelScript: _modelCodeMirror.getValue(),
      firstModelViz: _visualizationCodeMirror.getValue(),
      readme: _readmeCodeMirror.getValue(),
      resourceFiles: _val.resourceFiles, // TODO: get actual resource files from editor
      serverName: _val.serverName // TODO: get actual serverName from editor?
    }

    return viewValue;
  };

  view.validate = () => {
    // return handler.validate();
    return true;
  }

  return view;

  /** UI code. */
  function createUI() {

    let panelsById = [
      { id: "modelScript", panel: `<textarea id="modelScriptArea">${_val.firstModelScript}</textarea>` },
      { id: "visualizationScript", panel: `<textarea id="visualizationScriptArea">${_val.firstModelViz}</textarea>` },
      { id: "readme", panel: `<textarea id="readmeArea" name="readmeArea">${_val.readme}</textarea>` }
    ];

    let bodyContent = `
<div class="container-fluid">
  <nav class="navbar navbar-default">
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav" id="viewTab">
        ${handler.menus}
        <li role="presentation">
          <a id="modelScript-tab" href="#modelScript"
            aria-controls="modelScript" role="tab" data-toggle="tab">Model script</a>
        </li>
        <li role="presentation">
          <a id="visualizationScript-tab" href="#visualizationScript"
            aria-controls="visualizationScript" role="tab" data-toggle="tab">Visualization script</a>
        </li>
        <li role="presentation">
          <a id="readme-tab" href="#readme" aria-controls="readme" role="tab" data-toggle="tab">README</a>
        </li>
      </ul>
    </div>
  </nav>
  <div class="tab-content" id="viewContent">
    ${panelsById.map(entry => `<div role="tabpanel" class="tab-pane"
    id="${entry.id}">${entry.panel}</div>`).join("")}
  </div>
</div>`;

    document.createElement('body');
    $('body').html(bodyContent);

    // Add dialogs
    const container = document.getElementsByClassName("container-fluid")[0];
    Object.values(handler.dialogs).forEach(dialog => container.appendChild(dialog.modal));

    const viewContent = document.getElementById("viewContent");

    Object.entries(handler.panels).forEach(([key, value]) => {
      let tabPanel = document.createElement("div");
      tabPanel.setAttribute("role", "tabpanel");
      tabPanel.className = "tab-pane";
      tabPanel.id = key;
      tabPanel.appendChild(value.panel);

      viewContent.appendChild(tabPanel);
    });

    // Set the first tab (general information) as active
    document.getElementById("generalInformation").classList.add("active");

    // Create code mirrors for text areas with scripts and readme
    let require_config = {
      packages: [{
        name: "codemirror",
        location: "codemirror/",
        main: "lib/codemirror"
      }]
    };

    knimeService.loadConditionally(
      ["codemirror", "codemirror/mode/r/r", "codemirror/mode/markdown/markdown"],
      (arg) => {
        window.CodeMirror = arg[0];
        _modelCodeMirror = createCodeMirror("modelScriptArea", "text/x-rsrc");
        _visualizationCodeMirror = createCodeMirror("visualizationScriptArea", "text/x-rsrc");
        _readmeCodeMirror = createCodeMirror("readmeArea", "text/x-markdown");

        _modelCodeMirror.on("blur", () => { _modelCodeMirror.focus(); });
        _visualizationCodeMirror.on("blur", () => { _visualizationCodeMirror.focus(); });
        _readmeCodeMirror.on("blur", () =>{ _readmeCodeMirror.focus(); });
      },
      (err) => console.log("knimeService failed to install " + err),
      require_config);

    $('#modelScript-tab').on('shown.bs.tab', () => {
      _modelCodeMirror.refresh();
      _modelCodeMirror.focus();
    });

    $('#visualizationScript-tab').on('shown.bs.tab', () => {
      _visualizationCodeMirror.refresh();
      _visualizationCodeMirror.focus();
    });

    $('#readme-tab').on('shown.bs.tab', () => {
      _readmeCodeMirror.refresh();
      _readmeCodeMirror.focus();
    });
  }

  /**
   * Create a Bootstrap dropdown menu.
   * @param {string} name Menu name 
   * @param {array} submenus Array of hashes of id and name of the submenus.
   */
  function createSubMenu(name, submenus) {

    return `<li class="dropdown">
      <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
        aria-haspopup="true" aria-expanded="false">${name}<span class="caret"></a>
      <ul class="dropdown-menu">
      ${submenus.map(entry => `<li><a href="#${entry.id}" aria-controls="#${entry.id}"
        role="button" data-toggle="tab">${entry.label}</a></li>`).join("")}
      </ul>
    </li>`;
  }

  // Create a CodeMirror for a given text area
  function createCodeMirror(textAreaId, language) {
    return window.CodeMirror.fromTextArea(document.getElementById(textAreaId),
      {
        lineNumbers: true,
        lineWrapping: true,
        extraKeys: { 'Ctrl-Space': 'autocomplete' },
        mode: { 'name': language }
      });
  }

  function createLabel(name, isMandatory, description) {
    let label = document.createElement("label");
    label.classList.add("col-sm-2", "control-label");
    label.title = description;
    label.setAttribute("data-toggle", "tooltip");
    label.textContent = name + (isMandatory ? "*" : "");

    $(label).tooltip();  // Enable Bootstrap tooltip

    return label;
  }
}();