/*

version: 1.0.0
author: sascha obermüller
date: 08.12.2020

*/

class ModelHandler {
	constructor( metadata, img , modelScript, visScript ) {
		this._metadata = metadata;
		this._schema = {};
		this._menu = [];
		this._img = img;
		this._modelScript = modelScript;
		this._visScript = visScript;
		this.panels = {};
	}
	_create() {
		this._panels = {
			generalInformation:  {
				type 			: 'simple',
				schema 			: this._schema.generalInformation,
				metadata 	 	: this._metadata.generalInformation
			},
			modelCategory	:  {
				type 			: 'simple',
				schema 			: this._schema.modelCategory,
				metadata 	 	: this._metadata.generalInformation.modelCategory
			},
			author 			:  {
				type 			: 'complex',
				schema 			: this._schema.contact,
				metadata 	 	: this._metadata.generalInformation.author
			},
			creator 		:  {
				type 			: 'complex',
				schema 			: this._schema.contact,
				metadata 	 	: this._metadata.generalInformation.creator
			},
			reference 		:  {
				type 			: 'complex',
				schema 			: this._schema.reference,
				metadata 	 	: this._metadata.generalInformation.reference
			},
			scopeGeneral 	:  {
				type 			: 'simple',
				schema 			: this._schema.scope,
				metadata 	 	: this._metadata.scope
			},
			product 		:  {
				type 			: 'complex',
				schema 			: this._schema.product,
				metadata 	 	: this._metadata.scope.product
			},
			hazard 			:  {
				type 			: 'complex',
				schema 			: this._schema.hazard,
				metadata 	 	: this._metadata.scope.hazard
			},
			population 		:  {
				type 			: 'complex',
				schema 			: this._schema.populationGroup,
				metadata 	 	: this._metadata.scope.populationGroup
			},
			study 			:  {
				type 			: 'simple',
				schema 			: this._schema.study,
				metadata 	 	: this._metadata.dataBackground.study
			},
			studySample 	:  {
				type 			: 'complex',
				schema 			: this._schema.studySample,
				metadata 	 	: this._metadata.dataBackground.studySample
			},
			dietaryAssessmentMethod: {
				type 			: 'complex',
				schema 			: this._schema.dietaryAssessmentMethod,
				metadata 	 	: this._metadata.dataBackground.dietaryAssessmentMethod
			},
			laboratory 		:  {
				type 			: 'complex',
				schema 			: this._schema.laboratory,
				metadata 	 	: this._metadata.dataBackground.laboratory
			},
			assay 			:  {
				type 			: 'complex',
				schema 			: this._schema.assay,
				metadata 	 	: this._metadata.dataBackground.assay
			},
			modelMath 		:  {
				type 			: 'simple',
				schema 			: this._schema.modelMath,
				metadata 	 	: this._metadata.modelMath
			},
			parameter 		:  {
				type 			: 'complex',
				schema 			: this._schema.parameter,
				metadata 	 	: this._metadata.modelMath.parameter
			},
			qualityMeasures :  {
				type 			: 'complex',
				schema 			: this._schema.qualityMeasures,
				metadata 	 	: this._metadata.modelMath.qualityMeasures
			},
			modelEquation 	:  {
				type 			: 'complex',
				schema 			: this._schema.modelEquation,
				metadata 	 	: this._metadata.modelMath.modelEquation
			},
			exposure 	:  {
				type 			: 'complex',
				schema 			: this._schema.exposure,
				metadata 	 	: this._metadata.modelMath.exposure
			},
			plot 			:  {
				type 			: 'plot'
			},
			modelScript 	:  {
				type 			: 'modelScript'
			},
			visualizationScript :  {
				type 			: 'visualizationScript'
			},
			readme 			:  {
				type 			: 'readme'
			}
		}		
	}
} 


class GenericModel extends ModelHandler {

	constructor( metadata, img , state , modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.genericModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "dietaryAssessmentMethod",
						label 	: "Dietary assessment method"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "modelMath",
						label 	: "General"
					},
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					},
					{
						id		: "exposure",
						label 	: "Exposure"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}
		];
		super._create();
	}

	get metaData() {
		try{
		// generalInformation
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.population.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.parameter.forEach(param => delete param.reference);

		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
		this._metadata.modelMath.exposure = this.panels.exposure.data;

		this._metadata.modelType = "genericModel";

		this._metadata = metadataFix(this._metadata);
		}catch(error){
			console.log(error);
		}
		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.genericModel;
		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port ),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			population: new TablePanel("Population", schema.populationGroup,
				this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample,
				this._metadata.dataBackground.studySample, port),
			dietaryAssessmentMethod: new TablePanel("Dietary assessment method",
				schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures,
				this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
			exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
		};
	}
	
}
class DataModel extends ModelHandler {

	constructor( metadata, img , state, modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.dataModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "dietaryAssessmentMethod",
						label 	: "Dietary assessment method"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "parameter",
						label 	: "Parameter"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
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
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.population.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath.parameter = this.panels.parameter.data;

		this._metadata.modelType = "dataModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
	}
	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.dataModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			population: new TablePanel("Population", schema.populationGroup, this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
			dietaryAssessmentMethod: new TablePanel("Dietary assessment method",
				schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port)
		};
	}
}

class PredictiveModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.predictiveModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "parameter",
						label 	: "Parameter"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
	}
	get metaData() {

		// generalInformation
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.hazard = this.panels.hazard.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;

		this._metadata.modelType = "predictiveModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.predictiveModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures,
				this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port)
		};
	}
}

class OtherModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.otherModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "modelMath",
						label 	: "General"
					},
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
	}
	get metaData() {

		// general information
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.population.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;

		this._metadata.modelType = "otherModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.otherModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			population: new TablePanel("Population", schema.populationGroup, this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port)
		};
	}
}

class DoseResponseModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.doseResponseModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "modelMath",
						label 	: "General"
					},
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					},
					{
						id		: "exposure",
						label 	: "Exposure"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
	}
	get metaData() {

		// general information
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.population.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
		this._metadata.modelMath.exposure = this.panels.exposure.data;

		this._metadata.modelType = "doseResponseModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
	}

	validate() {
		let isValid = true;
		if (!this.panels.generalInformation.validate()) isValid = false;
		if (!this.panels.modelCategory.validate()) isValid = false;
		if (!this.panels.scopeGeneral.validate()) isValid = false;
		if (!this.panels.study.validate()) isValid = false;
		return isValid;
	}

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.doseResponseModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			population: new TablePanel("Population", schema.populationGroup, this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures,
				this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
			exposure: new FormPanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
		};
	}
}

class ToxicologicalModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.toxicologicalModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "modelMath",
						label 	: "General"
					},
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					},
					{
						id		: "exposure",
						label 	: "Exposure"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
	}
	get metaData() {

		// generalInformation
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.population.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
		this._metadata.modelMath.exposure = this.panels.exposure.data;

		this._metadata.modelType = "toxicologicalModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.toxicologicalModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			population: new TablePanel("Population", schema.populationGroup, this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures,
				this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
			exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
		};
	}

}

class ExposureModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.exposureModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "dietaryAssessmentMethod",
						label 	: "Dietary assessment method"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "modelMath",
						label 	: "General"
					},
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					},
					{
						id		: "exposure",
						label 	: "Exposure"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
	}
	get metaData() {

		// general information
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.population.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
		this._metadata.modelMath.exposure = this.panels.exposure.data;

		this._metadata.modelType = "exposureModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.exposureModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			population: new TablePanel("Population", schema.populationGroup, this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
			dietaryAssessmentMethod: new TablePanel("Dietary assessment method",
				schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
			exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
		};
	}
}

class ProcessModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.processModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
	}
	get metaData() {

		// generalInformation
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.hazard = this.panels.hazard.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;

		this._metadata.modelType = "processModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.processModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port)
		};
	}
}

class ConsumptionModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.consumptionModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
	}
	get metaData() {

		// generalInformation
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.populationGroup = this.panels.populationGroup.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;

		this._metadata.modelType = "consumptionModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.consumptionModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			population: new TablePanel("Population group", schema.populationGroup,
				this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample,
				this._metadata.dataBackground.studySample, port),
			dietaryAssessmentMethod: new TablePanel("Dietary assessment method",
				schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures,
				this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port)
		};
	}
}

class HealthModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.healthModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "modelMath",
						label 	: "General"
					},
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					},
					{
						id		: "exposure",
						label 	: "Exposure"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}
		];
		super._create();
	}
	get metaData() {

		// generalInformation
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.populationGroup.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
		this._metadata.modelMath.exposure = this.panels.exposure.data;

		this._metadata.modelType = "healthModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.healthModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			populationGroup: new TablePanel("Population group", schema.populationGroup,
				this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample,
				this._metadata.dataBackground.studySample, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory,
				this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures,
				this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation,
				this._metadata.modelMath.modelEquation, port),
			exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
		};
	}
}

class RiskModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.riskModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "dietaryAssessmentMethod",
						label 	: "Dietary assessment method"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "modelMath",
						label 	: "General"
					},
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					},
					{
						id		: "exposure",
						label 	: "Exposure"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}	
		];
		super._create();
	}
	get metaData() {

		// generalInformation
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.population.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
		this._metadata.modelMath.exposure = this.panels.exposure.data;

		this._metadata.modelType = "riskModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.riskModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			population: new TablePanel("Population", schema.populationGroup,
				this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample,
				this._metadata.dataBackground.studySample, port),
			dietaryAssessmentMethod: new TablePanel("Dietary assessment method",
				schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory,
				this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures,
				this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation,
				this._metadata.modelMath.modelEquation, port),
			exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
		};
	}
}

class QraModel extends ModelHandler {

	constructor( metadata, img , state ,  modelScript, visScript ) {
		super( metadata, img , modelScript, visScript);
		this._schema = schemas.qraModel;
		if(state){
			this.panels = this._createPanels();
		}
		this._menu = [
			{ 
				label	 : "General information",
				submenus : [
					{
						id		: "generalInformation",
						label 	: "General"
					},
					{
						id		: "modelCategory",
						label 	: "Model category"
					},
					{
						id		: "author",
						label 	: "Author"
					},
					{
						id		: "creator",
						label 	: "Creator"
					},
					{
						id		: "reference",
						label 	: "Reference"
					}
				]
			}, 
			{
				label	 : "Scope",
				submenus : [
					{
						id		: "scopeGeneral",
						label 	: "General"
					},
					{
						id		: "product",
						label 	: "Product"
					},
					{
						id		: "hazard",
						label 	: "Hazard"
					},
					{
						id		: "population",
						label 	: "Population group"
					}
				]
			}, 
			{
				label	 : "Data Background",
				submenus : [
					{
						id		: "study",
						label 	: "Study"
					},
					{
						id		: "studySample",
						label 	: "Study sample"
					},
					{
						id		: "dietaryAssessmentMethod",
						label 	: "Dietary assessment method"
					},
					{
						id		: "laboratory",
						label 	: "Laboratory"
					},
					{
						id		: "assay",
						label 	: "Assay"
					}
				]
			},
			{
				label	 : "Model math",
				submenus : [
					{
						id		: "modelMath",
						label 	: "General"
					},
					{
						id		: "parameter",
						label 	: "Parameter"
					},
					{
						id		: "qualityMeasures",
						label 	: "Quality measures"
					},
					{
						id		: "modelEquation",
						label 	: "Model equation"
					},
					{
						id		: "exposure",
						label 	: "Exposure"
					}
				]
			},
			{
				label	: "Plot",
				id		: 'plot',
				submenus : []
			},
			{
				label	: "Model",
				id		: 'modelScript',
				submenus : []
			},
			{
				label	: "Visualization",
				id		: 'visualizationScript',
				submenus : []
			},
			{
				label	: "Readme",
				id		: 'readme',
				submenus : []
			}
		];
		super._create();
	}
	get metaData() {

		// generalInformation
		this._metadata.generalInformation = this.panels.generalInformation.data;
		this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
		this._metadata.generalInformation.author = this.panels.author.data;
		this._metadata.generalInformation.creator = this.panels.creator.data;
		this._metadata.generalInformation.reference = this.panels.reference.data;

		// Scope
		this._metadata.scope = this.panels.scopeGeneral.data;
		this._metadata.scope.product = this.panels.product.data;
		this._metadata.scope.hazard = this.panels.hazard.data;
		this._metadata.scope.populationGroup = this.panels.population.data;

		// Data background
		this._metadata.dataBackground.study = this.panels.study.data;
		this._metadata.dataBackground.studySample = this.panels.studySample.data;
		this._metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
		this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
		this._metadata.dataBackground.assay = this.panels.assay.data;

		// Model math
		this._metadata.modelMath = this.panels.modelMath.data;
		this._metadata.modelMath.parameter = this.panels.parameter.data;
		this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
		this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
		this._metadata.modelMath.exposure = this.panels.exposure.data;

		this._metadata.modelType = "qraModel";

		this._metadata = metadataFix(this._metadata);

		return this._metadata;
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

	_createPanels() {
		let port = window.port || -1;
		let schema = schemas.qraModel;

		return {
			generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
			modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
			author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
			creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
			reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
			scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
			product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
			hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
			population: new TablePanel("Population", schema.populationGroup,
				this._metadata.scope.populationGroup, port),
			study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
			studySample: new TablePanel("Study sample", schema.studySample,
				this._metadata.dataBackground.studySample, port),
			dietaryAssessmentMethod: new TablePanel("Dietary assessment method",
				schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
			laboratory: new TablePanel("Laboratory", schema.laboratory,
				this._metadata.dataBackground.laboratory, port),
			assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
			modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
			parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
			qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures,
				this._metadata.modelMath.qualityMeasures, port),
			modelEquation: new TablePanel("Model equation", schema.modelEquation,
				this._metadata.modelMath.modelEquation, port),
			exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
		};
	}
}