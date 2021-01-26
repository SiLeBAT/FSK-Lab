/*

version: 1.0.0
author: sascha obermüller
date: 06.12.2020

*/

class Model {
	constructor( metadata, img ) {
		this._metadata = metadata;
		this._schema = {};
		this._menu = [];
		this._img = img;
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
			}
		}		
	}
} 


class GenericModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.genericModel;
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
			}	
		];
		super._create();
	}
}
class DataModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.dataModel;
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
			}	
		];
		super._create();
	}
}

class PredictiveModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.predictiveModel;
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
			}	
		];
		super._create();
	}
}

class OtherModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.otherModel;
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
			}	
		];
		super._create();
	}
}

class DoseResponseModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.doseResponseModel;
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
			}	
		];
		// extend panels with specific data and schemas
		this._panels = $.extend( true, {}, this._panels, {
			study 			:  {
				type 			: 'simple',
				schema 			: this._schema.study,
				metadata 	 	: this._metadata.scope.study
			},
			studySample 			:  {
				type 			: 'complex',
				schema 			: this._schema.study,
				metadata 	 	: this._metadata.scope.studySample
			},
			laboratory 			:  {
				type 			: 'complex',
				schema 			: this._schema.laboratory,
				metadata 	 	: this._metadata.scope.laboratory
			},
			assay 			:  {
				type 			: 'complex',
				schema 			: this._schema.assay,
				metadata 	 	: this._metadata.scope.assay
			},
			exposure 			:  {
				type 			: 'complex',
				schema 			: this._schema.exposure,
				metadata 	 	: this._metadata.modelMath.exposure
			}
		} );
	}
}

class ToxicologicalModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.toxicologicalModel;
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
			}	
		];

		// extend panels with specific data and schemas
		this._panels = $.extend( true, {}, this._panels, {
			study 			:  {
				type 			: 'simple',
				schema 			: this._schema.study,
				metadata 	 	: this._metadata.scope.study
			},
			studySample 			:  {
				type 			: 'complex',
				schema 			: this._schema.study,
				metadata 	 	: this._metadata.scope.studySample
			},
			laboratory 			:  {
				type 			: 'complex',
				schema 			: this._schema.laboratory,
				metadata 	 	: this._metadata.scope.laboratory
			},
			assay 			:  {
				type 			: 'complex',
				schema 			: this._schema.assay,
				metadata 	 	: this._metadata.scope.assay
			},
			exposure 			:  {
				type 			: 'complex',
				schema 			: this._schema.exposure,
				metadata 	 	: this._metadata.modelMath.exposure
			}
		} );
	}
}

class ExposureModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.exposureModel;
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
			}	
		];
		super._create();
	}
}

class ProcessModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.processModel;
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
			}	
		];
		super._create();
	}
}

class ConsumptionModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.consumptionModel;
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
						id		: "populationGroup",
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
			}	
		];
		super._create();
	}
}

class HealthModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.healthModel;
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
			}
		];
	}
}

class RiskModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.riskModel;
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
			}	
		];
		super._create();
	}
}

class QraModel extends Model {

	constructor( metadata, img ) {
		super( metadata, img );
		this._schema = schemas.qraModel;
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
			}
		];
		super._create();
	}
}