'use strict';

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

var _get = function get(object, property, receiver) { if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { return get(parent, property, receiver); } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * The observer class maintains a list of its observers and notifies them automatically of any state changes,
 * by calling one of their methods.
 * version: 1.0.0
 * author: Ahmad Swaid 
 * date: 07.12.2020 
 */
var EventObserver = function () {
	function EventObserver() {
		_classCallCheck(this, EventObserver);

		var O = this;
		O.observers = []; //list of observed events (callback functions)
	}

	_createClass(EventObserver, [{
		key: 'subscribe',
		value: function subscribe(id, observer) {
			var O = this;
			//add new events
			O.observers.push({
				id: id,
				callback: observer
			}); //get list of observed events and push new item to array
		}
	}, {
		key: 'unsubscribe',
		value: function unsubscribe(observer) {
			var O = this;
			O.observers = O.observers.filter(function (subscriber) {
				return subscriber !== observer;
			}); //returns a new list with filtered entries
		}
	}, {
		key: 'broadcast',
		value: function broadcast(event) {
			var O = this;
			_log("Sending event ", event);
			O.observers.forEach(function (subscriber) {
				subscriber.callback(event);
			});
		}
	}]);

	return EventObserver;
}();

var _endpoint = window._endpoint || 'https://knime.bfr.berlin/backend/'; //http://localhost:8080/' //'https://knime.bfr.berlin/landingpage/';
window._endpoints = {
	metadata: _endpoint + 'metadata/',
	image: _endpoint + 'image/',
	download: _endpoint + 'download/',
	uploadDate: _endpoint + 'uploadDate/',
	executionTime: _endpoint + 'executionTime/',
	simulations: _endpoint + 'simulations/',
	execution: _endpoint + 'execute/',
	search: _endpoint + 'search/',
	filter: _endpoint + 'filter',
	modelScript: _endpoint + "modelscript/",
	visScript: _endpoint + "visualizationscript/",
	controlledVocabularyEndpoint: 'https://knime.bfr.berlin/'
};
window._debug = true;
window.editEventBus = new EventObserver();
/*

version: 1.0.0
author: sascha obermüller
date: 08.12.2020

*/

var ModelHandler = function () {
	function ModelHandler(metadata, img, modelScript, visScript) {
		_classCallCheck(this, ModelHandler);

		this._metadata = metadata;
		this._schema = {};
		this._menu = [];
		this._img = img;
		this._modelScript = modelScript;
		this._visScript = visScript;
		this.panels = {};
	}

	_createClass(ModelHandler, [{
		key: '_create',
		value: function _create() {
			this._panels = {
				generalInformation: {
					type: 'simple',
					schema: this._schema.generalInformation,
					metadata: this._metadata.generalInformation
				},
				modelCategory: {
					type: 'simple',
					schema: this._schema.modelCategory,
					metadata: this._metadata.generalInformation.modelCategory
				},
				author: {
					type: 'complex',
					schema: this._schema.contact,
					metadata: this._metadata.generalInformation.author
				},
				creator: {
					type: 'complex',
					schema: this._schema.contact,
					metadata: this._metadata.generalInformation.creator
				},
				reference: {
					type: 'complex',
					schema: this._schema.reference,
					metadata: this._metadata.generalInformation.reference
				},
				scopeGeneral: {
					type: 'simple',
					schema: this._schema.scope,
					metadata: this._metadata.scope
				},
				product: {
					type: 'complex',
					schema: this._schema.product,
					metadata: this._metadata.scope.product
				},
				hazard: {
					type: 'complex',
					schema: this._schema.hazard,
					metadata: this._metadata.scope.hazard
				},
				population: {
					type: 'complex',
					schema: this._schema.populationGroup,
					metadata: this._metadata.scope.populationGroup
				},
				study: {
					type: 'simple',
					schema: this._schema.study,
					metadata: this._metadata.dataBackground.study
				},
				studySample: {
					type: 'complex',
					schema: this._schema.studySample,
					metadata: this._metadata.dataBackground.studySample
				},
				dietaryAssessmentMethod: {
					type: 'complex',
					schema: this._schema.dietaryAssessmentMethod,
					metadata: this._metadata.dataBackground.dietaryAssessmentMethod
				},
				laboratory: {
					type: 'complex',
					schema: this._schema.laboratory,
					metadata: this._metadata.dataBackground.laboratory
				},
				assay: {
					type: 'complex',
					schema: this._schema.assay,
					metadata: this._metadata.dataBackground.assay
				},
				modelMath: {
					type: 'simple',
					schema: this._schema.modelMath,
					metadata: this._metadata.modelMath
				},
				parameter: {
					type: 'complex',
					schema: this._schema.parameter,
					metadata: this._metadata.modelMath.parameter
				},
				qualityMeasures: {
					type: 'complex',
					schema: this._schema.qualityMeasures,
					metadata: this._metadata.modelMath.qualityMeasures
				},
				modelEquation: {
					type: 'complex',
					schema: this._schema.modelEquation,
					metadata: this._metadata.modelMath.modelEquation
				},
				exposure: {
					type: 'complex',
					schema: this._schema.exposure,
					metadata: this._metadata.modelMath.exposure
				},
				plot: {
					type: 'plot'
				},
				modelScript: {
					type: 'modelScript'
				},
				visualizationScript: {
					type: 'visualizationScript'
				},
				readme: {
					type: 'readme'
				}
			};
		}
	}]);

	return ModelHandler;
}();

var GenericModel = function (_ModelHandler) {
	_inherits(GenericModel, _ModelHandler);

	function GenericModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, GenericModel);

		var _this = _possibleConstructorReturn(this, (GenericModel.__proto__ || Object.getPrototypeOf(GenericModel)).call(this, metadata, img, modelScript, visScript));

		_this._schema = schemas.genericModel;
		if (state) {
			_this.panels = _this._createPanels();
		}
		_this._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(GenericModel.prototype.__proto__ || Object.getPrototypeOf(GenericModel.prototype), '_create', _this).call(_this);
		return _this;
	}

	_createClass(GenericModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.genericModel;
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
				dietaryAssessmentMethod: new TablePanel("Dietary assessment method", schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
				laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
				assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
				modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
				parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
				exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {
			try {
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
				this._metadata.modelMath.parameter.forEach(function (param) {
					return delete param.reference;
				});

				this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
				this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
				this._metadata.modelMath.exposure = this.panels.exposure.data;

				this._metadata.modelType = "genericModel";

				this._metadata = metadataFix(this._metadata);
			} catch (error) {
				console.log(error);
			}
			return this._metadata;
		}
	}]);

	return GenericModel;
}(ModelHandler);

var DataModel = function (_ModelHandler2) {
	_inherits(DataModel, _ModelHandler2);

	function DataModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, DataModel);

		var _this2 = _possibleConstructorReturn(this, (DataModel.__proto__ || Object.getPrototypeOf(DataModel)).call(this, metadata, img, modelScript, visScript));

		_this2._schema = schemas.dataModel;
		if (state) {
			_this2.panels = _this2._createPanels();
		}
		_this2._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "parameter",
				label: "Parameter"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(DataModel.prototype.__proto__ || Object.getPrototypeOf(DataModel.prototype), '_create', _this2).call(_this2);
		return _this2;
	}
	// Validate this.panels and return boolean


	_createClass(DataModel, [{
		key: 'validate',
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.dataModel;

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
				dietaryAssessmentMethod: new TablePanel("Dietary assessment method", schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
				laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
				assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
				parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return DataModel;
}(ModelHandler);

var PredictiveModel = function (_ModelHandler3) {
	_inherits(PredictiveModel, _ModelHandler3);

	function PredictiveModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, PredictiveModel);

		var _this3 = _possibleConstructorReturn(this, (PredictiveModel.__proto__ || Object.getPrototypeOf(PredictiveModel)).call(this, metadata, img, modelScript, visScript));

		_this3._schema = schemas.predictiveModel;
		if (state) {
			_this3.panels = _this3._createPanels();
		}
		_this3._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "parameter",
				label: "Parameter"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(PredictiveModel.prototype.__proto__ || Object.getPrototypeOf(PredictiveModel.prototype), '_create', _this3).call(_this3);
		return _this3;
	}

	_createClass(PredictiveModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.predictiveModel;

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
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return PredictiveModel;
}(ModelHandler);

var OtherModel = function (_ModelHandler4) {
	_inherits(OtherModel, _ModelHandler4);

	function OtherModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, OtherModel);

		var _this4 = _possibleConstructorReturn(this, (OtherModel.__proto__ || Object.getPrototypeOf(OtherModel)).call(this, metadata, img, modelScript, visScript));

		_this4._schema = schemas.otherModel;
		if (state) {
			_this4.panels = _this4._createPanels();
		}
		_this4._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(OtherModel.prototype.__proto__ || Object.getPrototypeOf(OtherModel.prototype), '_create', _this4).call(_this4);
		return _this4;
	}

	_createClass(OtherModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.otherModel;

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
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return OtherModel;
}(ModelHandler);

var DoseResponseModel = function (_ModelHandler5) {
	_inherits(DoseResponseModel, _ModelHandler5);

	function DoseResponseModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, DoseResponseModel);

		var _this5 = _possibleConstructorReturn(this, (DoseResponseModel.__proto__ || Object.getPrototypeOf(DoseResponseModel)).call(this, metadata, img, modelScript, visScript));

		_this5._schema = schemas.doseResponseModel;
		if (state) {
			_this5.panels = _this5._createPanels();
		}
		_this5._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(DoseResponseModel.prototype.__proto__ || Object.getPrototypeOf(DoseResponseModel.prototype), '_create', _this5).call(_this5);
		return _this5;
	}

	_createClass(DoseResponseModel, [{
		key: 'validate',
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.doseResponseModel;

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
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
				exposure: new FormPanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return DoseResponseModel;
}(ModelHandler);

var ToxicologicalModel = function (_ModelHandler6) {
	_inherits(ToxicologicalModel, _ModelHandler6);

	function ToxicologicalModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, ToxicologicalModel);

		var _this6 = _possibleConstructorReturn(this, (ToxicologicalModel.__proto__ || Object.getPrototypeOf(ToxicologicalModel)).call(this, metadata, img, modelScript, visScript));

		_this6._schema = schemas.toxicologicalModel;
		if (state) {
			_this6.panels = _this6._createPanels();
		}
		_this6._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(ToxicologicalModel.prototype.__proto__ || Object.getPrototypeOf(ToxicologicalModel.prototype), '_create', _this6).call(_this6);
		return _this6;
	}

	_createClass(ToxicologicalModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.toxicologicalModel;

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
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
				exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return ToxicologicalModel;
}(ModelHandler);

var ExposureModel = function (_ModelHandler7) {
	_inherits(ExposureModel, _ModelHandler7);

	function ExposureModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, ExposureModel);

		var _this7 = _possibleConstructorReturn(this, (ExposureModel.__proto__ || Object.getPrototypeOf(ExposureModel)).call(this, metadata, img, modelScript, visScript));

		_this7._schema = schemas.exposureModel;
		if (state) {
			_this7.panels = _this7._createPanels();
		}
		_this7._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(ExposureModel.prototype.__proto__ || Object.getPrototypeOf(ExposureModel.prototype), '_create', _this7).call(_this7);
		return _this7;
	}

	_createClass(ExposureModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.exposureModel;

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
				dietaryAssessmentMethod: new TablePanel("Dietary assessment method", schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
				laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
				assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
				modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
				parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
				exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return ExposureModel;
}(ModelHandler);

var ProcessModel = function (_ModelHandler8) {
	_inherits(ProcessModel, _ModelHandler8);

	function ProcessModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, ProcessModel);

		var _this8 = _possibleConstructorReturn(this, (ProcessModel.__proto__ || Object.getPrototypeOf(ProcessModel)).call(this, metadata, img, modelScript, visScript));

		_this8._schema = schemas.processModel;
		if (state) {
			_this8.panels = _this8._createPanels();
		}
		_this8._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(ProcessModel.prototype.__proto__ || Object.getPrototypeOf(ProcessModel.prototype), '_create', _this8).call(_this8);
		return _this8;
	}

	_createClass(ProcessModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.processModel;

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
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return ProcessModel;
}(ModelHandler);

var ConsumptionModel = function (_ModelHandler9) {
	_inherits(ConsumptionModel, _ModelHandler9);

	function ConsumptionModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, ConsumptionModel);

		var _this9 = _possibleConstructorReturn(this, (ConsumptionModel.__proto__ || Object.getPrototypeOf(ConsumptionModel)).call(this, metadata, img, modelScript, visScript));

		_this9._schema = schemas.consumptionModel;
		if (state) {
			_this9.panels = _this9._createPanels();
		}
		_this9._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(ConsumptionModel.prototype.__proto__ || Object.getPrototypeOf(ConsumptionModel.prototype), '_create', _this9).call(_this9);
		return _this9;
	}

	_createClass(ConsumptionModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.consumptionModel;

			return {
				generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
				modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
				author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
				creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
				reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
				scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
				product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
				population: new TablePanel("Population group", schema.populationGroup, this._metadata.scope.populationGroup, port),
				study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
				studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
				dietaryAssessmentMethod: new TablePanel("Dietary assessment method", schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
				laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
				assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
				modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
				parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return ConsumptionModel;
}(ModelHandler);

var HealthModel = function (_ModelHandler10) {
	_inherits(HealthModel, _ModelHandler10);

	function HealthModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, HealthModel);

		var _this10 = _possibleConstructorReturn(this, (HealthModel.__proto__ || Object.getPrototypeOf(HealthModel)).call(this, metadata, img, modelScript, visScript));

		_this10._schema = schemas.healthModel;
		if (state) {
			_this10.panels = _this10._createPanels();
		}
		_this10._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(HealthModel.prototype.__proto__ || Object.getPrototypeOf(HealthModel.prototype), '_create', _this10).call(_this10);
		return _this10;
	}

	_createClass(HealthModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.healthModel;

			return {
				generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
				modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
				author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
				creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
				reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
				scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
				hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
				populationGroup: new TablePanel("Population group", schema.populationGroup, this._metadata.scope.populationGroup, port),
				study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
				studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
				laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
				assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
				modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
				parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
				exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return HealthModel;
}(ModelHandler);

var RiskModel = function (_ModelHandler11) {
	_inherits(RiskModel, _ModelHandler11);

	function RiskModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, RiskModel);

		var _this11 = _possibleConstructorReturn(this, (RiskModel.__proto__ || Object.getPrototypeOf(RiskModel)).call(this, metadata, img, modelScript, visScript));

		_this11._schema = schemas.riskModel;
		if (state) {
			_this11.panels = _this11._createPanels();
		}
		_this11._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(RiskModel.prototype.__proto__ || Object.getPrototypeOf(RiskModel.prototype), '_create', _this11).call(_this11);
		return _this11;
	}

	_createClass(RiskModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.riskModel;

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
				dietaryAssessmentMethod: new TablePanel("Dietary assessment method", schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
				laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
				assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
				modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
				parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
				exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return RiskModel;
}(ModelHandler);

var QraModel = function (_ModelHandler12) {
	_inherits(QraModel, _ModelHandler12);

	function QraModel(metadata, img, state, modelScript, visScript) {
		_classCallCheck(this, QraModel);

		var _this12 = _possibleConstructorReturn(this, (QraModel.__proto__ || Object.getPrototypeOf(QraModel)).call(this, metadata, img, modelScript, visScript));

		_this12._schema = schemas.qraModel;
		if (state) {
			_this12.panels = _this12._createPanels();
		}
		_this12._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}, {
			label: "Model",
			id: 'modelScript',
			submenus: []
		}, {
			label: "Visualization",
			id: 'visualizationScript',
			submenus: []
		}, {
			label: "Readme",
			id: 'readme',
			submenus: []
		}];
		_get(QraModel.prototype.__proto__ || Object.getPrototypeOf(QraModel.prototype), '_create', _this12).call(_this12);
		return _this12;
	}

	_createClass(QraModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.qraModel;

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
				dietaryAssessmentMethod: new TablePanel("Dietary assessment method", schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
				laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
				assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
				modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
				parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
				exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {

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
	}]);

	return QraModel;
}(ModelHandler);
/** Temporary workaround for some metadata glitches. */


var metadataFix = function metadataFix(metadata) {
	// Ignore temporarily publication type
	// TODO: publicationType takes the abbreviation instead of the full string
	// used in the Reference dialog. Since KNIME runs getComponentValue twice,
	// the value cannot be converted here. The 1st call to getComponentValue
	// would get the abbreviation but the 2nd call would corrupt it. The HTML
	// select should instead use the full string as label and the abreviation
	// as value.
	metadata.generalInformation.reference.forEach(function (ref) {
		return delete ref.publicationType;
	});

	/* TODO: Ignore temporarily reference.
 The reference property is of type Reference in the schema. Unfortunately,
 nested dialogs are not supported in Bootstrap, so the type is changed
 in the UI schema to text. Since the text type cannot be deserialized to
 Reference, the values are discarded temporarily here.*/
	metadata.modelMath.parameter.forEach(function (param) {
		return delete param.reference;
	});

	return metadata;
};
/**
 * 
 * @param {*} name 
 * @param {*} isMandatory 
 * @param {*} description 
 */
var createLabel = function createLabel(name, isMandatory, description) {
	var label = document.createElement("label");
	label.classList.add("col-sm-2", "control-label");
	label.title = description;
	label.setAttribute("data-toggle", "tooltip");
	label.textContent = name + (isMandatory ? "*" : "");

	$(label).tooltip(); // Enable Bootstrap tooltip

	return label;
};

/**
 * Create a Bootstrap dropdown menu.
 * @param {string} name Menu name 
 * @param {array} submenus Array of hashes of id and name of the submenus.
 */
var createSubMenu = function createSubMenu(name, submenus) {
	return '<li class="dropdown">\n\t<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"\n\t\taria-haspopup="true" aria-expanded="false">' + name + '<span class="caret"></a>\n\t<ul class="dropdown-menu">\n\t' + submenus.map(function (entry) {
		return '<li><a href="#' + entry.id + '" aria-controls="#' + entry.id + '"\n\t\trole="button" data-toggle="tab">' + entry.label + '</a></li>';
	}).join("") + '\n\t</ul>\n\t</li>';
};

/**
 * Add controlled vocabulary to an input.
 * @param {Element} input Input element
 * @param {Array} vocabulary String array with vocabulary terms.
 */
var addControlledVocabulary = function addControlledVocabulary(input, vocabulary, port) {
	if (window._endpoints.controlledVocabularyEndpoint) {

		fetch(window._endpoints.controlledVocabularyEndpoint + ('' + vocabulary)).then(function (response) {
			return response.json();
		}).then(function (data) {
			$(input).typeahead({
				source: data,
				autoSelect: true,
				fitToElement: true,
				showHintOnFocus: true
			});
		}).catch(function (error) {
			if (port >= 0) {
				fetch('http://localhost:' + port + '/getAllNames/' + vocabulary).then(function (response) {
					return response.json();
				}).then(function (data) {
					$(input).typeahead({
						source: data,
						autoSelect: true,
						fitToElement: true,
						showHintOnFocus: true
					});
				});
			}
		});
	} else if (port >= 0) {
		fetch('http://localhost:' + port + '/getAllNames/' + vocabulary).then(function (response) {
			return response.json();
		}).then(function (data) {
			$(input).typeahead({
				source: data,
				autoSelect: true,
				fitToElement: true,
				showHintOnFocus: true
			});
		});
	}
};

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
var createForm = function createForm(prop, value, port) {
	var isMandatory = prop.required ? prop.required : false;

	if (prop.type === "text" || prop.type === "number" || prop.type === "url" || prop.type === "date" || prop.type === "email") return new InputForm(prop.label, isMandatory, prop.type, prop.description, value ? value : "", port, prop.vocabulary, prop.sid);else if (prop.type === "long-text") {
		return new TextareaForm(prop.label, isMandatory, prop.description, value ? value : "");
	} else if (prop.type === "enum") return new SelectForm(prop.label, isMandatory, prop.description, value, port, prop.vocabulary);else if (prop.type === "boolean") return new InputForm(prop.label, false, "checkbox", prop.description, value, port);else if (prop.type === "text-array") return new ArrayForm(prop.label, isMandatory, prop.type, value ? value : [], prop.description, port, prop.vocabulary);else if (prop.type === "date-array") return new ArrayForm(prop.label, isMandatory, prop.type, value ? value : [], prop.description, port, prop.vocabulary);
};

/**
 * LOG
 * logs message to console, if window._debug flag is set for this class
 * @param {string} log: log message
 */

var _log = function _log(log, style) {
	if (window._debug) {
		var styles = {
			primary: 'background: #000; color:#fff; padding: 1px 2px;',
			secondary: 'background: #aaa; color:#fff; padding: 1px 2px;',
			level1: 'padding-left: 10px',
			level2: 'padding-left: 20px',
			strong: 'font-weight: bold;',
			error: 'background: red; color: #ffffff; padding: 1px 2px;',
			warning: 'background: yellow; padding: 1px 2px;',
			hook: 'background: orange; padding: 1px 2px;'
		};
		if (style in styles) {
			console.log('%c' + log, styles[style]);
		} else {
			console.log(log);
		}
	}
};
/**
 * 
 */
var _createHelperMetadataText = function _createHelperMetadataText(helperText) {
	var O = undefined;

	// create table
	var $table = $('<table class="table table-sm table-hover table-params-metadata"></table>');
	// create rows
	if (helperText) {
		var $row = $('<tr></tr>').appendTo($table);
		$row.append('<td>' + helperText + '</td>'); // value
	}
	return $table;
};
/** 
  * is undefined
  */

var _isUndefined = function _isUndefined(val) {
	return (typeof val === 'undefined' ? 'undefined' : _typeof(val)) === (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined)) ? true : false;
};

/**
  * is null
  */

var _isNull = function _isNull(val) {
	return val == null ? true : false;
};

/**
  * sorter functions
  */

var _sorter = {
	_name: function _name(a, b) {
		// add a method called name
		return a.localeCompare(b);
		// aa = a.replace( /^the /i, '' ); // remove 'The' from start of parameter
		// bb = b.replace( /^the /i, '' ); // remove 'The' from start of parameter

		// if ( aa < bb ) { // if value a is less than value b
		// 	return -1;
		// }
		// else { // Otherwise
		// 	return aa > bb ? 1 : 0; // if a is greater than b return 1 OR
		// }
	},
	_execution: function _execution(a, b) {
		var aa = parseFloat(a); // remove s for seconds
		var bb = parseFloat(b); // remove s for seconds
		return aa - bb;
	},
	_date: function _date(a, b) {
		var aa = new Date(a);
		var bb = new Date(b);
		return aa - bb;
	}

	/**
   * formatter functions
   */

};var _formatter = {
	_list: function _list(val) {
		var $ul = $('<ul></ul>');
		if (val && (typeof val === 'undefined' ? 'undefined' : _typeof(val)) === 'object' && val !== null) {
			var _iteratorNormalCompletion = true;
			var _didIteratorError = false;
			var _iteratorError = undefined;

			try {
				for (var _iterator = val.values()[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
					var _item = _step.value;

					var $li = $('<li>' + _item + '</li>').appendTo($ul);
				}
			} catch (err) {
				_didIteratorError = true;
				_iteratorError = err;
			} finally {
				try {
					if (!_iteratorNormalCompletion && _iterator.return) {
						_iterator.return();
					}
				} finally {
					if (_didIteratorError) {
						throw _iteratorError;
					}
				}
			}

			;
			return $ul;
		} else if (val && $.isArray(val) && val.length > 0) {
			$.each(val, function (i, li) {
				var $li = $('<li>' + item + '</li>').appendTo($ul);
			});
			return $ul;
		}
		return val;
	},
	_uploadDate: function _uploadDate(val) {
		if (val && val.indexOf(' | ') >= 0) {
			var time = val.substring(val.indexOf(' | '));
			return val.replace(time, '<br><small>' + time + '</small>').replace('|', '');
		}
		return val;
	},
	_searchHighlight: function _searchHighlight(val, search) {
		// prevents highlighting of html parts that fit search 
		var rx = new RegExp('(?![^<]+>)' + search, 'gi');
		// return val.toString().replace( new RegExp('(<.*?>)(' + search + '?.)(</.*?>)', 'g'), '<mark>$2</mark>' );
		return val.replace(rx, '<mark>$&</mark>');
	}

	/**
   * fetch data from src by id and type
   */

};var _fetchData = {
	_json: async function _json(src, id, signal) {
		_log('UTILS / _fetchData._json: ' + src + ', ' + id);
		var data = null;
		// append id if not type "set"
		src = !_isNull(id) ? src + id : src;

		var fetchOpts = {};
		!_isNull(signal) ? fetchOpts.signal = signal : null;

		var response = await fetch(src, fetchOpts).catch(function (error) {
			if (error.name === 'AbortError') {
				_log('cancelled', 'level1');
			}
		});

		if (response) {
			data = await response.json();
		}

		return data;
	},
	_blob: async function _blob(src, id, signal) {
		_log('UTILS / _fetchData._blob: ' + src + ', ' + id);
		var data = null;
		// append id if not type "set"
		src = !_isNull(id) ? src + id : src;

		var fetchOpts = {};
		!_isNull(signal) ? fetchOpts.signal = signal : null;

		var response = await fetch(src, fetchOpts).catch(function (error) {
			if (error.name === 'AbortError') {
				_log('cancelled', 'level1');
			}
		});
		if (response) {
			var blob = await response.blob();

			var urlCreator = window.URL || window.webkitURL || window;
			data = urlCreator.createObjectURL(blob);
		}

		return data;
	},
	_content: async function _content(src, id, signal) {
		_log('UTILS / _fetchData._content: ' + src + ', ' + id);
		var data = null;
		// append id if not type "set"
		src = !_isNull(id) ? src + id : src;

		var fetchOpts = {};
		!_isNull(signal) ? fetchOpts.signal = signal : null;

		var response = await fetch(src, fetchOpts).catch(function (error) {
			if (error.name === 'AbortError') {
				_log('cancelled', 'level1');
			}
		});

		if (response) {
			data = await response.text();
		}

		return data;
	},
	_array: async function _array(src, arrayLength, signal) {
		_log('UTILS / _fetchData._array: ' + src + ', ' + arrayLength);
		var data = [];
		var fetchOpts = {};
		!_isNull(signal) ? fetchOpts.signal = signal : null;

		var _loop = async function _loop(i) {
			await fetch(src + i, fetchOpts).then(function (resp) {
				return resp.text().then(function (text) {
					data[i] = text;
				});
			}).catch(function (error) {
				if (error.name === 'AbortError') {
					_log('cancelled', 'level1');
				}
			});
		};

		for (var i = 0; i < arrayLength; i++) {
			await _loop(i);
		}

		return data;
	}

	/**
  * CHECK UNDEFINED CONTENT
  * checks if value is empty and replace the empty value with default or custom placeholder for table views 
  * @param {string/object} value: any value
  * @param {string} placeholder: any string type as placeholder
  */

};var _checkUndefinedContent = function _checkUndefinedContent(value, placeholder) {
	// placeholder custom || default
	placeholder = placeholder || '-';

	// check empty criterions
	if (_isUndefined(value) || _isNull(value) || (typeof value === 'undefined' ? 'undefined' : _typeof(value)) == 'object' && Object.keys(value).length === 0 && value.constructor === Object || (typeof value === 'undefined' ? 'undefined' : _typeof(value)) == 'object' && value.length == 1 && _isNull(value[0]) || value.length <= 0) {

		value = placeholder;
	}
	return value;
};

/**
  * strip html tags
  */

var _stripHtmlTags = function _stripHtmlTags(str) {
	if (str === null || str === '') {
		return false;
	} else {
		str = str.toString();
		return str.replace(/<[^>]*>/g, '');
	}
};

/**
 * GET DOM TEXT
 * get pure text of dom elements
 * @param {element} element: dom element
 */

var _getDOMText = function _getDOMText(node) {
	var O = undefined;

	var text = void 0;

	if (node.outerText) {
		text = node.outerText.trim();
	} else if (node.innerText) {
		text = node.innerText.trim();
	} else {
		text = '';
	}

	if (node.childNodes) {
		node.childNodes.forEach(function (child) {
			return text += _getDOMText(child);
		});
	}

	return text;
};

/**
  * has attribute
  */

$.fn._hasAttr = function (attrName) {
	if ($(undefined)) {
		var attr = $(undefined).attr(attrName);
		if ((typeof attr === 'undefined' ? 'undefined' : _typeof(attr)) !== (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined)) && attr !== false) {
			// element has this attribute
			return true;
		}
	}
	return false;
};
/*

version: 1.0.0
author: sascha obermüller
date: 06.12.2020

*/

var APPModal = function () {
	function APPModal(settings, $container) {
		_classCallCheck(this, APPModal);

		var O = this;
		O._$container = $container;
		O._debug = true;
		O._initiated = false;
		// defaults
		O._opts = $.extend(true, {}, {
			classes: '',
			data: null,
			on: {
				afterInit: null, // function
				show: function show(O, event) {
					O._updateModal(event);
				}, // function
				hide: null // function
			}
		}, settings);
		// basic init actions
		O._create();
		O._initiated = true;
		// callback
		if ($.isFunction(O.opts.on.afterInit)) {
			O.opts.on.afterInit.call(O);
		}
	}

	_createClass(APPModal, [{
		key: '_create',


		/**
   * CREATE
   * build basic model parts: modal header, body
   */

		value: function _create() {
			var O = this;
			_log('MODAL / _create');

			O._id = O.opts.id || 'modal' + $.now();
			O._type = O.opts.type || 'default';

			O._$modal = $('<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true"></div>').attr('id', O._id).addClass(O.opts.classes).appendTo(O._$container);

			O._$modalContent = $('<div class="modal-content"></div>').appendTo(O._$modal).wrap('<div class="modal-dialog modal-xl" role="document"></div>');

			// loader
			O._loader = _appUI._createLoader({ classes: 'loader-modal' }, O._$modalContent);

			// create modal
			O._createModal();

			// create bs modal
			O._$modal.modal({
				show: false // initially hidden
			});

			// bind show event
			O._$modal.on('show.bs.modal', function (event) {
				// callback
				if ($.isFunction(O.opts.on.show)) {
					O.opts.on.show.call(O, O, event);
				}
			});

			// bind hide event
			O._$modal.on('hide.bs.modal', function (event) {
				// callback
				if ($.isFunction(O.opts.on.hide)) {
					O.opts.on.hide.call(O, O, event);
				}
			});
		}

		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModal',
		value: function _createModal() {
			var O = this;
			_log('MODAL / _createModal');

			// modal head default
			O._createModalHead();

			// modal body default
			O._createModalBody().appendTo(O._$modalContent);
		}

		/**
   * CREATE MODAL HEAD
   * creates basic modal header with title and close
   */

	}, {
		key: '_createModalHead',
		value: function _createModalHead() {
			var O = this;
			_log('MODAL / _createModalHead');

			// modal head
			O._$modalHead = $('<div class="modal-header"></div>').appendTo(O._$modalContent);
			// modal head title
			O._$modalTitle = $('<h1 class="modal-title"></h1>').appendTo(O._$modalHead);
			// modal close
			$('<button type="button" class="modal-close action action-pure action-lg ml-2" data-dismiss="modal" aria-label="Close"><i class="feather icon-x"></i></button>').appendTo(O._$modalHead);
		}

		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModalBody',
		value: function _createModalBody() {
			var O = this;
			_log('MODAL / _createBody');

			O._$modalBody = $('<div class="modal-body"></div>').appendTo(O._$modalContent);
		}

		/**
   * BUILD MODAL
   * build modal blank function
   */

	}, {
		key: '_updateModal',
		value: async function _updateModal(event) {
			var O = this;
			_log('MODAL / _updateModal');

			// let $trigger = $( event.relatedTarget );
			// let modalID = $trigger.data( 'modal-id' );
		}

		/**
   * SET TITLE
   * set modal title
   */

	}, {
		key: '_setTitle',
		value: function _setTitle(text) {
			var O = this;
			_log('MODAL / _setTitle: ' + text);

			if (O._$modalTitle) {
				O._$modalTitle.text(text);
			}
		}

		/**
   * SHOW
   * show modal
   */

	}, {
		key: '_show',
		value: function _show() {
			var O = this;
			O._$modal.modal('show');
		}

		/**
   * HIDE
   * hide modal
   */

	}, {
		key: '_hide',
		value: function _hide() {
			var O = this;
			O._$modal.modal('hide');
		}

		/**
   * CLEAR MODAL
   * removes table body rows
   */

	}, {
		key: '_clear',
		value: function _clear() {
			var O = this;
			_log('MODAL / _clear');

			if (O._$modalContent) {
				O._$modalContent.empty();
			}
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPModal;
}();
/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

var APPModalMTDetails = function (_APPModal) {
	_inherits(APPModalMTDetails, _APPModal);

	function APPModalMTDetails(settings, $container) {
		_classCallCheck(this, APPModalMTDetails);

		return _possibleConstructorReturn(this, (APPModalMTDetails.__proto__ || Object.getPrototypeOf(APPModalMTDetails)).call(this, settings, $container));
	}

	/**
  * CREATE
  * calls super class and sets _metadata
  */

	_createClass(APPModalMTDetails, [{
		key: '_create',
		value: function _create() {
			var O = this;
			_log('MODAL DETAILS / _create', 'primary');

			O._metadata = O.opts.data;

			_get(APPModalMTDetails.prototype.__proto__ || Object.getPrototypeOf(APPModalMTDetails.prototype), '_create', this).call(this);
		}

		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModal',
		value: function _createModal() {
			var O = this;
			_log('MODAL DETAILS / _createModal');

			// modal head default
			O._createModalHead();
			O.ModelMTPanel = new APPMTDetails(O.opts, O._$modalContent);
			O.ModelMTPanel._createModelMetadataContent();
		}

		/**
   * BUILD MODAL
   * build modal content
   */

	}, {
		key: '_updateModal',
		value: async function _updateModal(event) {
			var O = this;
			_log('MODAL DETAILS / _updateModal');
			_log(event);

			O._loader._setState(true); // set loader

			// get trigger
			var $trigger = $(event.relatedTarget);
			// get model id & data
			O._modelId = $trigger.data('modal-id');
			O._modelMetadata = O._metadata[O._modelId];

			// modal title
			if (O._modelMetadata.generalInformation && O._modelMetadata.generalInformation.name) {
				O._setTitle(O._modelMetadata.generalInformation.name);
			}
			await O.ModelMTPanel._updateContent(O._modelMetadata, O._modelId);
			O._loader._setState(false); // set loader
		}
	}]);

	return APPModalMTDetails;
}(APPModal);
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


var ArrayForm = function () {
	function ArrayForm(name, mandatory, type, value, helperText, vocabulary, port) {
		_classCallCheck(this, ArrayForm);

		var O = this;
		O.group = document.createElement("div");
		O.mandatory = mandatory;
		O.simpleTable = new SimpleTable(type, value, vocabulary, port);
		O._create(name, mandatory, helperText);
	}

	_createClass(ArrayForm, [{
		key: '_create',
		value: function _create(name, mandatory, helperText) {
			var O = this;
			if (name) {

				// formgroup
				$formGroup = $('<div class="form-group row"></div>');

				// label
				var _$label = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'input_' + name).appendTo($formGroup);
				_$label.text(name + (mandatory ? "*" : ""));

				// field
				var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>').appendTo($formGroup);

				// actions
				var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo($formGroup);

				// create param metadata action
				if (helperText) {
					// action metadata list
					var $actionMetadata = $('<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#paramMetadata_' + name).attr('aria-expanded', false).attr('aria-controls', 'paramMetadata_' + name).attr('title', 'Show Metadata').appendTo($actions);
				}

				// create actions            
				var header = $('<div class="card-header"></div>');

				// Create card in group
				var panelDiv = document.createElement("div");
				panelDiv.classList.add("card");
				header.appendTo($(panelDiv));
				panelDiv.appendChild(O.simpleTable.table);

				_$actionTrash = $('<button type="button" class="action action-pure float-right"><i class="feather icon-trash-2"></i></button>').attr('id', 'simActionRemove').attr('data-tooltip', '').attr('title', 'Trash').appendTo(header).on('click', function (event) {
					O.simpleTable.trash();
				});

				// remove
				_$actionRemove = $('<button type="button" class="action action-pure float-right"><i class="feather icon-delete"></i></button>').attr('id', 'simActionRemove').attr('data-tooltip', '').attr('title', 'Remove').appendTo(header).on('click', function (event) {
					O.simpleTable.remove();
				});

				// add
				_$actionAdd = $('<button type="button" class="action action-pure float-right"><i class="feather icon-plus"></i></button>').attr('id', 'actionAdd').attr('data-tooltip', '').attr('title', 'Add').appendTo(header).on('click', function (event) {
					O.simpleTable.add();
				});

				$(panelDiv).appendTo($field);

				// create validation container
				O.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

				// create param metadata list
				if (helperText) {
					// metadata table
					var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'paramMetadata_' + name).attr('aria-expanded', false).appendTo($field);

					$metadataContainer.append(_createHelperMetadataText(helperText));
				}

				O.group = $formGroup;
			}
		}
	}, {
		key: 'onblurHandler',
		value: function onblurHandler() {
			var O = this;
			var closestForm = O.input.closest("form");
			var attr = closestForm.attr('no-immidiate-submit');
			var can_emit_Event = (typeof attr === 'undefined' ? 'undefined' : _typeof(attr)) === (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined)) || attr === false;
			_log(' onblurHandler' + can_emit_Event);
			if (can_emit_Event) {
				window.editEventBus.broadcast('MetadataChanged');
			}
		}
	}, {
		key: 'clear',
		value: function clear() {
			var O = this;
			O.simpleTable.trash();
		}

		/**
   * @return {boolean} If the textarea is valid.
   */

	}, {
		key: 'validate',
		value: function validate() {
			var O = this;
			var isValid = true;
			if (O.mandatory) {
				isValid = O.simpleTable.value.length > 0 ? true : false;
			}
			if (!isValid) {
				O.$validationContainer.text('At least one row is required');
				O.group.addClass('has-error');
				O.group.addClass('is-invalid');
				O.$validationContainer.css("display", "block");
			} else {
				O.onblurHandler();
			}
			return isValid;
		}
	}, {
		key: 'value',
		get: function get() {
			var O = this;
			return O.simpleTable.value;
		},
		set: function set(newValue) {
			var O = this;
			O.simpleTable.trash();
			newValue.forEach(function (item) {
				return O.simpleTable._createRow(item);
			});
		}
	}]);

	return ArrayForm;
}();
/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

var APPMTEditableDetails = function () {
	function APPMTEditableDetails(settings, $container) {
		_classCallCheck(this, APPMTEditableDetails);

		var O = this;
		// defaults maintable modal
		O._$modalContent = $container;
		O._opts = $.extend(true, {}, {
			classes: '',
			data: null,
			on: {
				afterInit: null, // function
				show: function show(O, event) {
					O._updateModal(event);
				}, // function
				hide: null // function
			}
		}, settings);
		O._create();
	}

	_createClass(APPMTEditableDetails, [{
		key: '_create',

		/**
   * CREATE
   * calls super class and sets _metadata
   */

		value: function _create() {
			var O = this;
			_log('MODAL DETAILS / _create', 'primary');

			O._metadata = O.opts.data;
		}
		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModelMetadataContent',
		value: function _createModelMetadataContent() {
			var O = this;
			_log('MODAL DETAILS / _createModelMetadataContent');
			// modal nav with tabs & search
			O._$modalNav = $('<div class="modal-body modal-nav card-header"></div>').appendTo(O._$modalContent);

			O._navId = O._id + 'Nav';
			if (!O._$navBar) {
				O._$navBar = $('<nav class="navbar navbar-expand-sm row justify-content-start justify-content-md-between"></nav>').appendTo(O._$modalNav);

				// nav toggle
				var $navToggle = $('<button class="action action-pure mt-1 mb-1" type="button" data-toggle="collapse" aria-expanded="false" aria-label="Toggle navigation"><i class="feather icon-list"></i></button>').appendTo(O._$navBar).attr('data-target', '#' + O._navId).attr('aria-controls', O._navId).wrap('<div class="col-auto navbar-toggler order-1 modal-nav-toggler"></div>');

				// divider
				// O._$navBar.append('<div class="col-divider order-2 d-block d-sm-none d-md-block"></div>');

				// nav search
				/*O._$navBar._$search = $('<input class="form-control form-control-plaintext search-input" type="search" placeholder="Search Details" aria-label="Search Details" />')
        .appendTo(O._$navBar)
        .attr('id', O._id + 'NavSearch')
        .wrap('<div class="col col-xxs-auto order-2 modal-nav-search"></div>')
        .wrap('<div class="search"></div>');
    */

				// TO DO
				// search functionality


				// nav tabs
				O._$navBar._$nav = $('<ul class="nav nav-pointer pt-1 pt-md-0"></ul>').appendTo(O._$navBar).wrap('<div class="col-12 col-md-auto order-3 order-md-1 modal-nav-menu order-4"></div>').wrap('<div class="collapse navbar-collapse" id="' + O._navId + '"></div>');
			}

			// modal body
			O._createModalBody();
			O._$modalBody.addClass('p-0 modal-table');

			// content container
			O._$modalTabContent = $('<div class="tab-content h-100"></div>').appendTo(O._$modalBody);
		}

		/**
  * CREATE MODAL
  * creates basic modal components: header and blank body
  */

	}, {
		key: '_createModalBody',
		value: function _createModalBody() {
			var O = this;
			_log('MODAL / _createBody');

			O._$modalBody = $('<div class="modal-body"></div>').appendTo(O._$modalContent);
		}

		/**
  * BUILD PANEL
  * build PANEL content
  * @param {*} _modelMetadata 
   * @param {*} _modelId 
  */

	}, {
		key: '_updateContent',
		value: async function _updateContent(_modelMetadata, _modelId) {
			var O = this;
			_log('PANEL MetaData / _updateContent');

			// clear tab-panes
			O._$modalTabContent.html('');

			// get appropiate modelMetadata modelHandler for the model type.
			O._modelHandler = await O._getModelHandler(_modelMetadata);
			// populate nav
			O._populateModalNav(O._modelHandler, O._$navBar._$nav);

			// populate panel
			O._populateModalPanel(O._modelHandler);

			// activate first pane
			O._$navBar._$nav.find('.nav-link').first().addClass('active');
			O._$modalTabContent.find('.tab-pane').first().addClass('active');
		}

		/**
   * POPULATE MODAL MENU
   * @param {*} modelHandler 
   */

	}, {
		key: '_populateModalNav',
		value: function _populateModalNav(modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _populateModalNav');
			_log(modelHandler);

			// clear nav
			O._$navBar._$nav.html('');

			// create nav items
			if (modelHandler && modelHandler._menu) {

				$.each(modelHandler._menu, function (i, menuMeta) {
					if (menuMeta.id == 'plot') return;
					var $navItem = null;

					if (menuMeta.submenus && menuMeta.submenus.length > 0) {
						$navItem = O._createNavItemDropdown(menuMeta).appendTo(O._$navBar._$nav);
					} else {
						var _$navItem = O._createNavItem(menuMeta).appendTo(O._$navBar._$nav);
					}
				});
			}
			//init collapsable td
			$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				var target = $(e.target).attr("href"); // activated tab
				var targetTable = $('div' + target + '.tab-pane.h-100.active').find('table');
				//if not initialized
				if (targetTable.find('.td-collapse-toggle').length == 0) {
					_appUI._initTdCollapse(targetTable);
				}
			});
		}

		/**
   * POPULATE MODAL PANEL
   * @param {object} modelHandler
   */

	}, {
		key: '_populateModalPanel',
		value: function _populateModalPanel(modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _populateModalPanel');
			_log(modelHandler);

			// create panels
			if (modelHandler && modelHandler._menu && modelHandler.panels) {
				// get each menus id
				$.each(modelHandler._menu, function (i, menuMeta) {
					// dropdown nav item 
					if (menuMeta.submenus && menuMeta.submenus.length > 0) {
						// iterate over submenus
						$.each(menuMeta.submenus, function (j, submenuMeta) {
							// panel meta data exists in handler
							if (submenuMeta.id in modelHandler.panels) {
								O._preparePanel(submenuMeta, modelHandler, $(modelHandler.panels[submenuMeta.id].panel)).appendTo(O._$modalTabContent);
							}
						});
					}
					// single nav item ? create panel
					else {
							if (menuMeta.id && menuMeta.id != 'plot') {
								if (menuMeta.id in modelHandler._panels) {
									O._preparePanel(menuMeta, modelHandler, $(modelHandler._panels[menuMeta.id].panel)).appendTo(O._$modalTabContent);
									//O._createPanel(menuMeta, modelHandler)
									//    .appendTo(O._$modalTabContent);
								}
							}
						}
				});
			}
		}

		/**
   * CREATE NAV ITEM DROPDOWN
   * @param {array} menuMeta: array of dropdown-items width 'id' and 'label'
   */

	}, {
		key: '_createNavItemDropdown',
		value: function _createNavItemDropdown(menuMeta) {
			var O = this;
			_log('MODAL DETAILS / _createTabNavItemDropdown: ' + menuMeta.label);

			var $navItem = $('<li class="nav-item dropdown"></li>');

			var $navLink = $('<a class="nav-link dropdown-toggle" role="button">' + menuMeta.label + '</a>').attr('href', '#').attr('aria-haspopup', true).attr('aria-expanded', false).attr('data-toggle', 'dropdown').appendTo($navItem);
			var $dropdown = $('<div class="dropdown-menu"></div>').appendTo($navItem);

			$.each(menuMeta.submenus, function (i, submenuMeta) {

				var $dropdownItem = $('<a class="dropdown-item" role="button">' + submenuMeta.label + '</a>').attr('href', '#' + submenuMeta.id).attr('aria-controls', '#' + submenuMeta.id).attr('data-toggle', 'tab').appendTo($dropdown);
			});

			return $navItem;
		}

		/**
   * CREATE NAV ITEM
   * @param {array} menuMeta
   */

	}, {
		key: '_createNavItem',
		value: function _createNavItem(menuMeta) {
			var O = this;
			_log('MODAL DETAILS / _createNavItem: ' + menuMeta.label);

			var $navItem = $('<li class="nav-item"></li>');
			var $navLink = $('<a class="nav-link" role="button">' + menuMeta.label + '</a>').attr('href', '#' + menuMeta.id).attr('aria-controls', '#' + menuMeta.id).attr('data-toggle', 'tab').appendTo($navItem);

			return $navItem;
		}

		/**
   * CREATE PANEL
   * create tab-pane for specific menu by selecting type and calling specific creation (simple, complex, plot)
   * @param {array} menu
   * @param {object} modelHandler: object of type Model
   * @param {object} handlerPanel: Panel to add elements to.
   */

	}, {
		key: '_preparePanel',
		value: function _preparePanel(menu, modelHandler, handlerPanel) {
			var O = this;
			_log('MODAL DETAILS / _createPanel: ' + menu.id);

			var $panel = null;
			if (modelHandler && menu.id) {
				var panelMeta = modelHandler._panels[menu.id];
				if (panelMeta && panelMeta.type) {
					// complex
					if (panelMeta.type == 'modelScript') {
						$panel = O._createScriptPanel(menu, modelHandler);
					}
					// simple
					else if (panelMeta.type == 'visualizationScript') {
							$panel = O._createScriptPanel(menu, modelHandler);
						}
						// plot
						else if (panelMeta.type == 'readme') {
								$panel = O._createScriptPanel(menu, modelHandler);
							} else {
								$panel = O._createPanelPan(menu, modelHandler, handlerPanel);
							}
				}
			}
			return $panel;
		}

		/**
   * CREATE PLOT PANEL
   * create plot tab-pane for specific menu
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   * @param {object} handlerPanel: Panel to add elements to.
   */

	}, {
		key: '_createPanelPan',
		value: function _createPanelPan(menu, modelHandler, handlerPanel) {
			var O = this;
			_log('MODAL DETAILS / _createPlotPanel');

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');
				handlerPanel.appendTo($panel);
			}

			return $panel;
		}

		/**
   * CREATE SCRIPT PANEL
   * create SCRIPT tab-pane for specific menu
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createScriptPanel',
		value: function _createScriptPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createScriptPanel');

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');

				var $plot = $('<textarea row="6" class="form-control form-control-sm" />').attr('id', menu.id + 'Area').appendTo($panel);
			}

			return $panel;
		}

		/**
   * GET MODEL HANDLER
   * returns model handler of class Model
   * @param {array} modelMetadata: metadata for specific id
   */

	}, {
		key: '_getModelHandler',
		value: async function _getModelHandler(modelMetadata) {
			var O = this;
			_log('MODAL DETAILS / _getModelHandler');

			var modelHandler = null;

			if (modelMetadata) {

				// get plot image
				var imgUrl = void 0;
				// get appropiate modelMetadata modelHandler for the model type.

				if (modelMetadata.modelType === 'genericModel') {
					modelHandler = new GenericModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'dataModel') {
					modelHandler = new DataModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'predictiveModel') {
					modelHandler = new PredictiveModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'otherModel') {
					modelHandler = new OtherModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'toxicologicalModel') {
					modelHandler = new ToxicologicalModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'doseResponseModel') {
					modelHandler = new DoseResponseModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'exposureModel') {
					modelHandler = new ExposureModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'processModel') {
					modelHandler = new ProcessModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'consumptionModel') {
					modelHandler = new ConsumptionModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'healthModel') {
					modelHandler = new HealthModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'riskModel') {
					modelHandler = new RiskModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'qraModel') {
					modelHandler = new QraModel(modelMetadata, imgUrl, true);
				} else {
					modelHandler = new GenericModel(modelMetadata, imgUrl, true);
				}
			}

			return modelHandler;
		}
	}, {
		key: 'opts',
		get: function get() {
			var O = this;
			return O._opts;
		},
		set: function set(settings) {
			var O = this;
			O._opts = $.extend(true, {}, O.opts, settings);
		}
	}]);

	return APPMTEditableDetails;
}();

/**
 * Create a Bootstrap 3 modal dialog.
 */


var Dialog = function () {

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
	function Dialog(id, title, formData, port) {
		_classCallCheck(this, Dialog);

		var O = this;
		O.inputs = {}; // Hash of inputs by id

		// Index of the row currently edited. It is -1 if no row is being edited.
		// This is the case of when a new row is added.
		O.editedRow = -1;

		O.modal = document.createElement("div");
		O.create(id, title, formData, port);
	}

	_createClass(Dialog, [{
		key: 'create',
		value: function create(id, title, formData, port) {
			var O = this;
			// modal body
			var form = $('<form class="form-striped" no-immidiate-submit></form>');
			formData.forEach(function (prop) {
				var inputForm = createForm(prop, null, port);
				if (inputForm) {
					$(inputForm.group).appendTo(form);
					O.inputs[prop.id] = inputForm;
				}
			});

			var modalBody = $('<div class="modal-body p-0 sim-params"></div>');
			var modalinnerBody = $('<div class="tab-content h-100"></div>');
			form.appendTo(modalinnerBody);
			modalinnerBody.appendTo(modalBody);

			// modal action
			// nav
			_$modalNav = $('<div class="modal-body sim-select"></div>');

			// navbar
			_$navBar = $('<nav class="navbar sim-select">').appendTo(_$modalNav).wrap('<form></form>');

			//  select label
			$('<label class="col-4 col-md-3 sim-select-label" >' + title.replace("Add", "") + '</label>').appendTo(_$navBar);

			//  select actions
			_$dialogActions = $('<div class="col-8"></div>').appendTo(_$navBar);

			var $actionGroup1 = $('<div class="col-12"></div>').appendTo(_$dialogActions);

			closeButton = $('<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-x"></i></button>').attr('id', 'simActionclose').attr('data-tooltip', '').attr('title', 'close').attr('data-dismiss', 'modal').appendTo($actionGroup1);
			// col divider
			$('<div class="col-divider ml-auto ml-xs-0"></div>').appendTo($actionGroup1);
			saveButton = $('<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-save"></i></button>').attr('id', 'save').attr('data-tooltip', '').attr('title', 'Save changes').appendTo($actionGroup1).on('click', function (event) {
				// Validate inputs and stop saving if errors are found.
				var hasError = false;
				Object.values(O.inputs).forEach(function (input) {
					if (!input.validate()) hasError = true;
				});
				if (hasError) return;

				$(O.modal).modal('hide');

				// Retrieve data and clear inputs
				var data = {};
				for (var inputId in O.inputs) {
					var currentInput = O.inputs[inputId];
					data[inputId] = currentInput.value; // Save input value
					currentInput.clear(); // Clear input
				}

				if (O.editedRow != -1) {
					O.panel.save(O.editedRow, data);
					O.editedRow = -1;
					Object.values(O.inputs).forEach(function (input) {
						return input.clear();
					}); // Clear inputs
				} else {
					O.panel.add(data);
				}
			});
			$actionGroup1.wrapInner('<div class="row justify-content-end align-items-center"></div>');

			var content = document.createElement("div");
			content.classList.add("modal-content");
			content.innerHTML = '<div class="modal-header">\n                                <h1 class="modal-title">' + title + '</h1>\n                                <button type="button" class="action action-pure action-lg ml-2" data-dismiss="modal" aria-label="Close"><i class="feather icon-x"></i></button>\n                                </div>';

			_$navBar.appendTo($(content));
			modalBody.appendTo($(content));

			var modalDialog = document.createElement("div");
			modalDialog.classList.add("modal-dialog", "modal-xl");
			modalDialog.setAttribute("role", "document");
			modalDialog.appendChild(content);

			O.modal.classList.add("modal", "fade", "modal-sim");
			O.modal.id = id;
			O.modal.tabIndex = -1;
			O.modal.setAttribute("role", "dialog");
			O.modal.appendChild(modalDialog);
			_appUI._initFormItems(form);
		}
	}]);

	return Dialog;
}();
/**
 * Simple panel for non nested data like General information, study, etc.
 */


var FormPanel = function () {
	function FormPanel(title, formData, data, port) {
		_classCallCheck(this, FormPanel);

		_log('FormPanel /' + title, 'primary');
		var O = this;
		O.panel = $('<div class="panel-body"></div>');
		O.inputs = {};

		O._create(title, formData, data, port);
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


	_createClass(FormPanel, [{
		key: '_create',
		value: function _create(title, formData, data, port) {
			var O = this;
			var form = $('<form class="form-striped"></form>');
			formData.forEach(function (prop) {
				var inputForm = createForm(prop, data ? data[prop.id] : null, port, title === "Parameter" ? true : false);
				if (inputForm) {
					$(inputForm.group).appendTo(form);
					O.inputs[prop.id] = inputForm;
				}
			});
			form.appendTo(O.panel);
			// init form items' functions: touchspin, range, select2 ...
			_appUI._initFormItems(form);
		}
	}, {
		key: 'validate',
		value: function validate() {
			var O = this;
			var isValid = true;
			Object.values(O.inputs).forEach(function (input) {
				if (!input.validate()) isValid = false;
			});
			return isValid;
		}
	}, {
		key: 'data',
		get: function get() {
			var O = this;
			var data = {};
			Object.entries(O.inputs).forEach(function (_ref) {
				var _ref2 = _slicedToArray(_ref, 2),
				    id = _ref2[0],
				    input = _ref2[1];

				return data[id] = input.value;
			});
			return data;
		}
	}]);

	return FormPanel;
}();
/**
 * Bootstrap 3 form-group for an input.
 */


var InputForm = function () {

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
  * @param {Array} vocabulary Vocabulary name.
  */
	function InputForm(name, mandatory, type, helperText, value, port) {
		var vocabulary = arguments.length > 6 && arguments[6] !== undefined ? arguments[6] : null;
		var sid = arguments[7];

		_classCallCheck(this, InputForm);

		var O = this;
		O.name = name;
		O.mandatory = mandatory;
		O.type = type;
		O.helperText = helperText;
		O.isSID = !_isNull(sid) && !_isUndefined(sid);
		O.group = null;
		O._create(name, mandatory, type, helperText, value, vocabulary, port);
	}

	/**
  * @param {string} name Property name
  * @param {boolean} mandatory `true` if mandatory, `false` if optional.
  * @param {string} type Property type: text, url, checkbox, etc.
  * @param {string} helperText Tooltip
  * @param {string} value Initial value of the property.
  * @param {Array} vocabulary Vocabulary name.
  */


	_createClass(InputForm, [{
		key: '_create',
		value: function _create(name, mandatory, type, helperText, value, vocabulary, port) {
			var O = this;
			O._createFormField(name, mandatory, type, helperText, value, vocabulary, port);
		}
		/**
   * CREATE FORM FIELD
   * create field as form group
   * @param {array} param
   */

	}, {
		key: '_createFormField',
		value: function _createFormField(name, mandatory, type, helperText, value, vocabulary, port) {
			var O = this;
			_log('PANEL SIM / _createFormField');
			_log(name);

			if (name) {

				// formgroup
				var _$formGroup = $('<div class="form-group row"></div>');

				// label
				$label = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'input_' + name).appendTo(_$formGroup);
				$label.text(name + (mandatory ? "*" : ""));

				// field
				var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>').appendTo(_$formGroup);

				// actions
				var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo(_$formGroup);

				// input item
				O.input = null;

				// create param metadata action
				if (helperText) {
					// action metadata list
					var $actionMetadata = $('<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#metadata_' + name).attr('aria-expanded', false).attr('aria-controls', 'metadata_' + name).attr('title', 'Show Metadata').appendTo($actions);
				}

				if (type) {

					// numeric
					if (type == 'number') {

						var $inputGroup = $('<div class="input-group input-group-sm"></div>').appendTo($field);

						O.input = $('<input type="text" />').attr('id', 'input_' + name).attr('aria-invalid', false).attr('data-min', parseFloat(Number.MIN_SAFE_INTEGER)) // min value
						.attr('data-max', parseFloat(Number.MAX_SAFE_INTEGER)) // max value
						.appendTo($inputGroup);

						// touchspin
						O.input.addClass('form-control form-control-sm').attr('data-touchspin', '');
						// add postfix to touchspin

						//O.input.attr( 'data-touchspin-postfix', type );
					}
					// string or others
					//<input class="custom-control-input" type="checkbox" id="switchExample1" name="switchExample1" checked />
					else if (type == 'boolean') {
							O.input = $('<input type="checkbox" class="form-control form-control-sm" />').attr('id', 'input_' + name).appendTo($field);
						}
						// string or others
						else {
								O.input = $('<input type="text" class="form-control form-control-sm" />').attr('id', 'input_' + name).appendTo($field);
							}
				}
				if (type === "date" && typeof value != "string") {
					var day = ("" + value[2]).length > 1 ? "" + value[2] : "0" + value[2];
					var month = ("" + value[1]).length > 1 ? "" + value[1] : "0" + value[1];
					O.input.val(value[0] + "-" + month + "-" + day);
				} else {
					O.input.val(value);
				}
				if (type === "date") {
					//O.input.attr('type','date');
					O.input.attr('data-datepicker', '');
				}
				// Add autocomplete to input with vocabulary
				if (vocabulary) {
					addControlledVocabulary(O.input, vocabulary, port);
				}
				O.input.on("blur", function () {
					O.validate(O.value);
				});
				// create validation container
				O.input.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

				// create  metadata list
				if (helperText) {
					// metadata table
					var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'metadata_' + name).attr('aria-expanded', false).appendTo($field);

					$metadataContainer.append(_createHelperMetadataText(helperText));
				}

				O.group = _$formGroup;
			}
		}
	}, {
		key: 'clear',
		value: function clear() {
			var O = this;
			O.input.val("");

			if (O.input.$validationContainer) {
				O.input.$validationContainer.css("display", "none");
			}

			// Remove validation classes
			O.group.removeClass("has-success has-error");
		}
	}, {
		key: 'onblurHandler',
		value: function onblurHandler() {
			var O = this;
			var closestForm = O.input.closest("form");
			var attr = closestForm.attr('no-immidiate-submit');
			var can_emit_Event = (typeof attr === 'undefined' ? 'undefined' : _typeof(attr)) === (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined)) || attr === false;
			_log(' onblurHandler' + can_emit_Event);
			if (can_emit_Event) {
				window.editEventBus.broadcast('MetadataChanged');
			}
		}
		/**
   * @returns {boolean} If the input is valid.
   */

	}, {
		key: 'validate',
		value: function validate() {
			var O = this;
			_log('PANEL  / _validateForm');

			var validationErrors = [];
			// remove error classes
			O.input.find('.has-error').removeClass('has-error');
			O.input.find('.is-invalid').removeClass('is-invalid');
			O.input.find('.validation-message').empty();
			O.input.$validationContainer.text('');

			var isValid = true;
			if (!O.mandatory) {
				isValid = true;
			} else if (O.isSID) {
				var fieldValue = O.input.val();
				var idRegexp = /^[A-Za-z_^s]\w*$/;
				// name fits regexp
				if (!idRegexp.test(fieldValue)) {
					O.input.$validationContainer.text('Parameter ID is not a valid (SId)');
					isValid = false;
				}
			} else {
				isValid = O.input.val() ? true : false;
				if (!isValid) O.input.$validationContainer.text("required");
				// check if mail has correct structure
				if (isValid && O.type === "email") {
					var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\ ".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
					isValid = re.test(O.input.value);
					O.input.$validationContainer.text("Not a valid email value");
				}
			}

			if (!isValid) {
				O.input.parents('.form-group').addClass('has-error');
				O.input.$validationContainer.addClass('is-invalid');
				O.input.$validationContainer.css("display", "block");
			} else {
				O.onblurHandler();
			}

			return isValid;
		}
	}, {
		key: 'value',
		get: function get() {
			var O = this;
			return O.type !== "checkbox" ? O.input.val() : O.input.checked;
		},
		set: function set(newValue) {
			var O = this;
			O.input.val(newValue);
		}
	}]);

	return InputForm;
}();
/**
 * Bootstrap 3 form with a select.
 */


var SelectForm = function () {

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
  * <select id="select2ExampleS2" class="form-control form-control-sm" style="width: 100%;" data-sel2 data-placeholder="Select…">
 *									<option value="1">Option 1</option>
 *									<option value="2">Option 2</option>
 *									<option value="3">Option 3 with very long title lorem ipsum dolor sit amet</option>
 *								</select>
  */
	function SelectForm(name, mandatory, helperText, value, port) {
		var vocabulary = arguments.length > 5 && arguments[5] !== undefined ? arguments[5] : null;

		_classCallCheck(this, SelectForm);

		var O = this;
		O.group = document.createElement("div");

		O._create(name, mandatory, helperText, value, port, vocabulary);
	}

	_createClass(SelectForm, [{
		key: '_create',
		value: function _create(name, mandatory, helperText, value, port, vocabulary) {
			var O = this;
			// formgroup
			var $formGroup = $('<div class="form-group row"></div>');

			// label
			var $label = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'selectInput_' + name).appendTo($formGroup);
			$label.text(name + (mandatory ? "*" : ""));

			// field
			var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 "></div>').appendTo($formGroup);

			// actions
			var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo($formGroup);

			// input item
			O.input = null;

			// create param metadata action
			if (helperText) {
				// action metadata list
				var $actionMetadata = $('<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#paramMetadata_' + name).attr('aria-expanded', false).attr('aria-controls', 'paramMetadata_' + name).attr('title', 'Show Metadata').appendTo($actions);
			}

			O.input = $('<select class="form-control form-control-sm" style="width: 100%;" data-sel2 data-placeholder="Select…"/>').attr('id', 'selectInput_' + name).appendTo($field);
			O.input.val(value);
			// Add options from vocabulary. The option matching value is selected.
			if (window._endpoints.controlledVocabularyEndpoint) {

				fetch(window._endpoints.controlledVocabularyEndpoint + ('' + vocabulary)).then(function (response) {
					return response.json();
				}).then(function (data) {
					O.input.append(data.map(function (item) {
						return '<option>' + item + '</option>';
					}).join(""));
				}).catch(function (error) {
					if (port >= 0) {
						fetch('http://localhost:' + port + '/getAllNames/' + vocabulary).then(function (response) {
							return response.json();
						}).then(function (data) {
							O.input.append(data.map(function (item) {
								return '<option>' + item + '</option>';
							}).join(""));
						});
					}
				});
			} else if (port >= 0) {
				fetch('http://localhost:' + port + '/getAllNames/' + vocabulary).then(function (response) {
					return response.json();
				}).then(function (data) {
					O.input.append(data.map(function (item) {
						return '<option>' + item + '</option>';
					}).join(""));
				});
			}

			// create validation container
			O.input.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

			// create param metadata list
			if (helperText) {
				// metadata table
				var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'paramMetadata_' + name).attr('aria-expanded', false).appendTo($field);

				$metadataContainer.append(_createHelperMetadataText(helperText));
			}
			O.group = $formGroup;
		}
	}, {
		key: 'onblurHandler',
		value: function onblurHandler() {
			var O = this;
			var closestForm = O.input.closest("form");
			var attr = closestForm.attr('no-immidiate-submit');
			var can_emit_Event = (typeof attr === 'undefined' ? 'undefined' : _typeof(attr)) === (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined)) || attr === false;
			_log(' onblurHandler' + can_emit_Event);
			if (can_emit_Event) {
				window.editEventBus.broadcast('MetadataChanged');
			}
		}
	}, {
		key: 'clear',
		value: function clear() {
			var O = this;
			O.input.val('');
		}

		/**
   * @returns {boolean} If the input is valid.
   */

	}, {
		key: 'validate',
		value: function validate() {
			var O = this;
			var isValid = void 0;
			O.input.find('.has-error').removeClass('has-error');
			O.input.find('.is-invalid').removeClass('is-invalid');
			O.input.find('.validation-message').empty();
			if (!O.mandatory) {
				isValid = true;
			} else {
				isValid = O.input.value ? true : false;
			}

			if (!isValid) {
				O.input.$validationContainer.text('required');
				O.input.parents('.form-group').addClass('has-error');
				O.input.addClass('is-invalid');
				O.input.$validationContainer.css("display", "block");
			} else {
				O.onblurHandler();
			}
			return isValid;
		}
	}, {
		key: 'value',
		get: function get() {
			var O = this;
			return O.input.val() || O.input.find("option[data-select2-id]").text();
		},
		set: function set(newValue) {
			var O = this;
			O.select.val(newValue);
		}
	}]);

	return SelectForm;
}();

var SimpleTable = function () {
	function SimpleTable(type, data, vocabulary, port) {
		_classCallCheck(this, SimpleTable);

		var O = this;
		O.type = type === "text-array" ? "text" : "date";
		O.vocabulary = vocabulary;
		O.port = port;

		O.table = document.createElement("table");
		O.table.className = "table";
		O.table.innerHTML = '<thead><thead>';

		O.body = document.createElement("tbody");
		O.table.appendChild(O.body);

		data.forEach(function (value) {
			return O._createRow(value);
		});
	}

	/**
  * Create new row to enter data if the last row value is not empty.
  */


	_createClass(SimpleTable, [{
		key: 'add',
		value: function add() {
			var O = this;
			// If it has no rows or the last row value is not empty
			if (!O.body.lastChild || O.body.lastChild.lastChild.firstChild.value) {
				O._createRow();
			}
		}
	}, {
		key: 'remove',
		value: function remove() {
			var O = this;
			// Find checked rows and delete them
			Array.from(O.body.children).forEach(function (row) {
				// Get checkbox (tr > td > input)
				var checkbox = row.firstChild.firstChild;
				if (checkbox.checked) {
					O.body.removeChild(row);
				}
			});
		}

		/**
   * Remove every row in the table
   */

	}, {
		key: 'trash',
		value: function trash() {
			var O = this;
			O.body.innerHTML = "";
		}
	}, {
		key: '_createRow',
		value: function _createRow() {
			var value = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : "";

			var O = this;
			var input = document.createElement("input");
			input.type = O.type;
			input.className = "form-control";
			input.value = value;

			// Add autocomplete to input with vocabulary
			if (O.vocabulary) {
				addControlledVocabulary(input, O.vocabulary, O.port);
			}

			// If enter is pressed when the input if focused, lose focus and add a
			// new row (like clicking the add button). The new input from calling add
			// is focused.
			input.addEventListener("keyup", function (event) {
				if (event.key === "Enter") {
					input.blur();
					O.add();
				}
			});

			// Create cell with input
			var inputCell = document.createElement("td");
			inputCell.appendChild(input);

			// Create row with checkbox and input
			var newRow = document.createElement("tr");
			newRow.innerHTML = '<td><input type="checkbox"></td>';
			newRow.appendChild(inputCell);

			// Add row
			O.body.appendChild(newRow);

			input.focus(); // Focus the new input      
		}
	}, {
		key: 'value',
		get: function get() {
			var O = this;
			var data = [];
			O.body.childNodes.forEach(function (tr) {
				var inputCell = tr.lastChild; // 2nd cell (with input)
				var input = inputCell.firstChild; // <input>
				data.push(input.value);
			});

			return data;
		}
	}]);

	return SimpleTable;
}();
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


var TablePanel = function () {

	/**
  * Create a TablePanel.
  * 
  * @param {string} title Panel title.
  * @param {object} formData Related data from the UI schema.
  * @param {object} data Initial data of the table.
  */
	function TablePanel(title, formData, data, port) {
		_classCallCheck(this, TablePanel);

		var O = this;
		O.panel = document.createElement("div");

		// Register this panel in dialog (TODO: this should be done in Dialog's constr)
		// O.dialog = dialog;
		O.dialog = new Dialog(title + "Dialog", "Add " + title, formData, port);
		O.dialog.panel = this;
		O.tablePanel = O._createComplexPanel(data, formData, title, O.dialog);
		O.table = O.tablePanel.find("table.table-striped");
		O.data = data ? data : []; // Initialize null or undefined data
		O._create(title, O.dialog, formData);
	}

	/**
  * Create UI of the TablePanel.
  * 
  * @param {string} title Panel title.  
  * @param {Dialog} dialog Reference to Dialog object. This Dialog is later
  *   used for adding new entries and editing existing ones. 
  * @param {object} formData Related data from the UI schema.
  */


	_createClass(TablePanel, [{
		key: '_create',
		value: function _create(title, dialog, formData) {
			var O = this;
			// panel
			O.panel.classList.add("panel", "panel-default");
			O.tablePanel.appendTo($(O.panel));
		}
		/**
   * CREATE COMPLEX PANEL
   * create complex tab-pane for specific menu
   * table has in metadata and schema defined cols
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createComplexPanel',
		value: function _createComplexPanel(data, formData, title, dialog) {
			var O = this;

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', 'table' + title);
			// Add button
			var addButton = $('<button class="btn btn-outline-secondary btn-sm btn-icon" type="button"><i class="feather icon-plus"></i></button>').attr('aria-label', "Add a " + title).attr('title', "Add a " + title);
			addButton.on('click', function (event) {
				Object.values(dialog.inputs).forEach(function (input) {
					return input.clear();
				});
				$(dialog.modal).modal('show');
			});

			var removeAllButton = $('<button class="btn btn-outline-secondary btn-sm btn-icon" type="button"><i class="feather icon-trash"></i></button>').attr('aria-label', 'Remove all ' + title + '(s)').attr('title', 'Remove all ' + title + '(s)');
			removeAllButton.on('click', function (event) {
				O.removeAll();
			});

			// table settings
			var tableSettings = {
				cols: [],
				tableData: [],
				responsive: true,
				showToggle: true,
				rowActions: [{
					type: 'link',
					idPrefix: 'mtActionMerge_',
					icon: 'icon-arrow-up',
					title: 'Move Up',
					on: {
						click: function click(o, $action, rowIndex, rowData) {
							_log('on > clicktrash', 'hook');
							_log(o);
							_log($action);
							_log(rowIndex);
							_log(rowData);
							O.moveTo(rowIndex, 'up');
						}
					}
				}, {
					type: 'link',
					idPrefix: 'mtActionMerge_',
					icon: 'icon-arrow-down',
					title: 'Move down',
					on: {
						click: function click(o, $action, rowIndex, rowData) {
							_log('on > clickMoveTO ', 'hook');
							_log(o);
							_log($action);
							_log(rowIndex);
							_log(rowData);
							O.moveTo(rowIndex, 'down');
						}
					}
				}, {
					type: 'link',
					idPrefix: 'mtActionMerge_',
					icon: 'icon-trash',
					title: 'Trash',
					on: {
						click: function click(o, $action, rowIndex, rowData) {
							_log('on > clicktrash', 'hook');
							_log(o);
							_log($action);
							_log(rowIndex);
							_log(rowData);
							O.remove(rowIndex);
						}
					}
				}, {
					type: 'link',
					idPrefix: 'mtActionEdit_',
					icon: 'icon-edit-2',
					title: 'Edit',
					on: {
						click: function click(o, $action, rowIndex, rowData) {
							_log('on > clickEdit', 'hook');
							_log(o);
							_log($action);
							_log(rowIndex);
							_log(rowData);
							O.edit(rowIndex, rowData, dialog);
						}
					}
				}],
				editableToolbarbuttons: [addButton, removeAllButton]
			};

			// set table cols
			$.each(formData, function (i, prop) {
				tableSettings.cols.push({
					label: prop.label,
					field: prop.id,
					sortable: true,
					switchable: true
				});
			});

			// set table row data
			$.each(data, function (i, item) {
				// row each item
				var rowData = {
					cells: []
				};
				// cells
				$.each(formData, function (j, prop) {
					var data = item[prop.id];
					data = _checkUndefinedContent(data);
					// cell each prop
					rowData.cells.push(data);
				});

				tableSettings.tableData.push(rowData);
			});
			// create table
			O.panelTable = new APPTable(tableSettings, $panel);
			$panel.data('table', O.panelTable);
			return $panel;
		}
	}, {
		key: 'add',
		value: function add(data) {
			var O = this;
			data.el ? delete data.el : null;
			O.panelTable._tableData.push(data); // add data
			O.data.push(data); // add data
			O.panelTable.addRow(O.panelTable._tableData.length - 1, data, false);
			window.editEventBus.broadcast('MetadataChanged');
		}
	}, {
		key: 'edit',
		value: function edit(index, originalData, dialog) {
			var O = this;
			var keys = [];
			$.each(O.panelTable.opts.cols, function (index, key) {
				keys.push(key.field);
			});
			for (indexx in keys) {
				dialog.inputs[keys[indexx]].input.val(originalData.cells[indexx]);
			}

			dialog.editedRow = index;
			$(dialog.modal).modal('show');
			window.editEventBus.broadcast('MetadataChanged');
		}
	}, {
		key: 'save',
		value: function save(index, originalData) {
			var O = this;
			originalData.el ? delete originalData.el : null;
			O.data.splice(index, 1);
			O.panelTable._tableData.splice(index, 1);
			var row = $(O.panelTable._$tbody).find('tr').eq(index);
			row.find('td').each(function () {
				$(this).html(originalData[$(this).attr('data-id')]);
			});
			O.data.push(originalData); // add data
			O.panelTable._tableData.push(originalData);
			window.editEventBus.broadcast('MetadataChanged');
		}
	}, {
		key: 'remove',
		value: function remove(index) {
			var O = this;
			$(O.panelTable._$tbody).find('tr').eq(index).remove();;

			O.data.splice(index, 1);
			O.panelTable._tableData.splice(index, 1);

			$.each($(O.panelTable._$tbody).find('tr'), function (rowindex, row) {
				$(row).attr('data-row-id', rowindex);
			});
			window.editEventBus.broadcast('MetadataChanged');
		}
	}, {
		key: 'moveTo',
		value: function moveTo(index, command) {
			var O = this;
			var row = $(O.panelTable._$tbody).find('tr').eq(index);
			if (command === 'up') {
				row.insertBefore(row.prev());
			} else if (command === 'down') {
				row.insertAfter(row.next());
			}
			$.each($(O.panelTable._$tbody).find('tr'), function (rowindex, row) {
				$(row).attr('data-row-id', rowindex);
			});
		}
	}, {
		key: 'removeAll',
		value: function removeAll() {
			var O = this;
			O.data = []; // Clear data
			O.panelTable._tableData = []; // Clear data
			O.panelTable._clear(); // Empty table
			window.editEventBus.broadcast('MetadataChanged');
		}
	}]);

	return TablePanel;
}();
/**
 * Create a Bootstrap 3 form-group for a textarea. 
 */


var TextareaForm = function () {

	/**
  * Create a Bootstrap 3 form-group.
  * 
  * ```
  * <div class="form-group row">
  *   <label>name</label>
  *   <textarea class="form-control" rows="3"></textarea>
  * </div>
  * ```
  */

	function TextareaForm(name, mandatory, helperText, value) {
		_classCallCheck(this, TextareaForm);

		var O = this;
		O.name = name;
		O.mandatory = mandatory;
		O.helperText = helperText;

		O.textarea = $('<textarea row="6" class="form-control form-control-sm" />').attr('id', 'area_' + name);
		O._create(name, mandatory, helperText, value);
	}

	/**
  * @param {string} name Property name
  * @param {boolean} mandatory `true` if mandatory, `false` if optional.
  * @param {string} helperText Tooltip
  * @param {string} value Initial value of the property.
  */


	_createClass(TextareaForm, [{
		key: '_create',
		value: function _create(name, mandatory, helperText, value) {
			var O = this;
			// formgroup
			var $formGroup = $('<div class="form-group row"></div>');

			// label
			var $label = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'areaInput_' + name).appendTo($formGroup);
			$label.text(name + (mandatory ? "*" : ""));

			// field
			var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 "></div>').appendTo($formGroup);

			// actions
			var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo($formGroup);

			// input item
			O.input = null;

			// create param metadata action
			if (helperText) {
				// action metadata list
				var $actionMetadata = $('<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#paramMetadata_' + name).attr('aria-expanded', false).attr('aria-controls', 'paramMetadata_' + name).attr('title', 'Show Metadata').appendTo($actions);
			}

			O.input = $('<textarea type="text" row="6" class="form-control" />').attr('id', 'areaInput_' + name).appendTo($field);
			O.input.val(value);

			// create validation container
			O.input.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

			// create param metadata list
			if (helperText) {
				// metadata table
				var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'paramMetadata_' + name).attr('aria-expanded', false).appendTo($field);

				$metadataContainer.append(_createHelperMetadataText(helperText));
			}
			O.group = $formGroup;
		}
	}, {
		key: 'onblurHandler',
		value: function onblurHandler() {
			var O = this;
			var closestForm = O.input.closest("form");
			var attr = closestForm.attr('no-immidiate-submit');
			var can_emit_Event = (typeof attr === 'undefined' ? 'undefined' : _typeof(attr)) === (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined)) || attr === false;
			_log(' onblurHandler' + can_emit_Event);
			if (can_emit_Event) {
				window.editEventBus.broadcast('MetadataChanged');
			}
		}
	}, {
		key: 'clear',
		value: function clear() {
			var O = this;
			O.input.val("");
		}

		/**
   * @return {boolean} If the textarea is valid.
   */

	}, {
		key: 'validate',
		value: function validate() {
			var O = this;
			var isValid = void 0;
			O.input.find('.has-error').removeClass('has-error');
			O.input.find('.is-invalid').removeClass('is-invalid');
			O.input.find('.validation-message').empty();
			if (!O.mandatory) {
				isValid = true;
			} else {
				isValid = O.input.val() ? true : false;
			}
			if (!isValid) {
				O.input.$validationContainer.text('required');
				O.input.parents('.form-group').addClass('has-error');
				O.input.addClass('is-invalid');
				O.input.$validationContainer.css("display", "block");
			} else {
				O.onblurHandler();
			}
			return isValid;
		}
	}, {
		key: 'value',
		get: function get() {
			var O = this;
			return O.input.val();
		},
		set: function set(newValue) {
			var O = this;
			O.input.val(newValue);
		}
	}]);

	return TextareaForm;
}();
/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

var APPModalMTSimulations = function (_APPModal2) {
	_inherits(APPModalMTSimulations, _APPModal2);

	function APPModalMTSimulations(settings, $container) {
		_classCallCheck(this, APPModalMTSimulations);

		// defaults maintable simulations modal
		var modalSettings = $.extend(true, {}, {
			on: {
				simRunModelView: null // function
			}
		}, settings);

		return _possibleConstructorReturn(this, (APPModalMTSimulations.__proto__ || Object.getPrototypeOf(APPModalMTSimulations)).call(this, modalSettings, $container));
	}

	/**
  * CREATE
  * calls super class and sets _metadata
  */

	_createClass(APPModalMTSimulations, [{
		key: '_create',
		value: function _create() {
			var O = this;
			_log('MODAL SIM / _create', 'primary');
			_log(O.opts);

			// global
			O._metadata = O.opts.data;
			O._state = 'params'; // default state: params form

			O._$simInputs = []; // inputs from params and customs
			O._simFields = {};
			O._simSelectedIndex = 0; // initial simulation

			_get(APPModalMTSimulations.prototype.__proto__ || Object.getPrototypeOf(APPModalMTSimulations.prototype), '_create', this).call(this);

			// bind hide event
			O._$modal.on('hide.bs.modal', function (event) {
				// callback
				if ($.isFunction(O.opts.on.hide)) {
					O.opts.on.hide.call(O, O, event);
				}
				// abort running fetch
				if (O._fetchController) {
					_log('MODAL SIM / abort fetch');
					O._fetchController.abort();
				}
			});
		}

		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModal',
		value: function _createModal() {
			var O = this;
			_log('MODAL SIM / _createModal');
			// modal head default
			O._createModalHead();
			O.opts._loader = O._loader;
			O._simulationPanel = new APPSimulation(O.opts, O._$modalContent);
			O._simulationPanel._createSimulationContent();
		}

		/**
   * BUILD MODAL
   * build modal content
   * @param {event} event 
   */

	}, {
		key: '_updateModal',
		value: async function _updateModal(event) {
			var O = this;
			_log('MODAL SIM / _updateModal');
			_log(event);
			O._loader._setState(true); // set loader

			// get trigger
			var $trigger = $(event.relatedTarget);
			// get model id & data
			O._modelId = $trigger.data('modal-id');
			O._modelMetadata = O._metadata[O._modelId];

			// modal title 
			if (O._modelMetadata.generalInformation && O._modelMetadata.generalInformation.name) {
				_log(O._modelMetadata.generalInformation.name);
				O._setTitle(O._modelMetadata.generalInformation.name);
			}
			// get simulations
			_simulations = await _fetchData._json(window._endpoints.simulations, O._modelId); //O._app._getSimulations( O._modelId );
			await O._simulationPanel._updateContent(O._modelMetadata, O._modelId, _simulations);
			O._loader._setState(false); // set loader
		}
	}]);

	return APPModalMTSimulations;
}(APPModal);
/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

var APPSimulation = function () {
	function APPSimulation(settings, $container) {
		_classCallCheck(this, APPSimulation);

		var O = this;
		// defaults maintable simulations modal
		O._$container = $container;
		O._opts = $.extend(true, {}, {
			classes: '',
			data: null,
			on: {
				afterInit: null, // function
				show: function show(O, event) {
					O._updateModal(event);
				}, // function
				hide: null // function
			}
		}, settings);
		O._create();
	}

	_createClass(APPSimulation, [{
		key: '_create',

		/**
  * CREATE
  * calls super class and sets _metadata
  */

		value: function _create() {
			var O = this;
			_log('SIM / _create', 'primary');
			_log(O.opts);

			// global
			O._metadata = O.opts.data;
			O._state = 'params'; // default state: params form

			O._$simInputs = []; // inputs from params and customs
			O._simFields = {};
			O._simSelectedIndex = 0; // initial simulation
		}
	}, {
		key: '_createSimulationContent',
		value: function _createSimulationContent() {
			var O = this;
			_log('panel SIM / _createSimulationContent');

			// nav
			O._$modalNav = $('<div class="modal-body sim-select"></div>').appendTo(O._$container);

			// navbar
			O._$navBar = $('<nav class="navbar">').appendTo(O._$modalNav).wrap('<form></form>');

			// sim select label
			O._$simSelectLabel = $('<label class="col-12 col-md-3 sim-select-label" for="simulationSelect">Simulations</label>').appendTo(O._$navBar);

			// sim select counter
			O._$simSelectCounter = $('<span class="badge badge-primary ml-1">x1</span>').appendTo(O._$simSelectLabel);

			// sim select
			O._$simSelect = $('<select id="simulationSelect" class="custom-control custom-select"></select>').attr('id', 'simulationSelect').appendTo(O._$navBar).wrap('<div class="col-12 col-xs-auto col-md-4 sim-select-field"></div>').on('change', function (event) {
				var selectedIndex = O._$simSelect[0].selectedIndex;
				if (selectedIndex >= 0) {
					O._updateSimIndex(selectedIndex);
				}
			});

			// create actions
			O._createSimActions();

			// panel bodys

			// panel params
			O._$modalParams = $('<div class="modal-body p-0 sim-params"></div>').appendTo(O._$container);
			// param content container
			O._$modalParams._$content = $('<div class="tab-content h-100"></div>').appendTo(O._$modalParams);

			// panel execution
			O._$modalExecution = $('<div class="modal-body p-0 sim-execution"></div>').appendTo(O._$container);
			// execution content container
			O._$modalExecution._$content = $('<div class="tab-content h-100"></div>').appendTo(O._$modalExecution);
		}

		/**
   * CREATE SIM ACTIONS
   * creates actionss
   */

	}, {
		key: '_createSimActions',
		value: function _createSimActions() {
			var O = this;
			_log('panel SIM / _createSimActions');

			// sim select actions
			O._$simSelectActions = $('<div class="col-12 col-xs-auto col-md-5 mt-2 mt-xs-0 sim-select-actions"></div>').appendTo(O._$navBar);

			// create actions
			// action group 1
			var $actionGroup1 = $('<div class="col-auto sim-select-actions-group"></div>').appendTo(O._$simSelectActions);

			// remove
			O._$simActionRemove = $('<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-trash-2"></i></button>').attr('id', 'simActionRemove').attr('data-tooltip', '').attr('title', 'Remove simulation').appendTo($actionGroup1).on('click', function (event) {
				O._removeSimulation();
			});

			// add
			O._$simActionAdd = $('<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-plus"></i></button>').attr('id', 'simActionAdd').attr('data-tooltip', '').attr('title', 'Add simulation').appendTo($actionGroup1).on('click', function (event) {
				O._addSimulation();
			});

			// save
			O._$simActionSave = $('<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-save"></i></button>').attr('id', 'simActionSave').attr('data-tooltip', '').attr('title', 'Save changes').appendTo($actionGroup1).on('click', function (event) {
				O._saveSimulation();
			});

			// col divider
			$('<div class="col-divider ml-auto ml-xs-0"></div>').appendTo(O._$simSelectActions);

			// action group 2
			var $actionGroup2 = $('<div class="col-auto sim-select-actions-group"></div>').appendTo(O._$simSelectActions);

			O._$simActionRun = $('<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-play"></i></button>').attr('id', 'simActionRun').attr('data-tooltip', '').attr('title', 'Run simulation').appendTo($actionGroup2).on('click', function (event) {
				O._runModelView();
			});

			O._$simSelectActions.wrapInner('<div class="row justify-content-end align-items-center"></div>');
		}

		/**
   * CREATE PARAM METADATA LIST
   * create table for metadata collapse container
   * @param {array} param 
   */

	}, {
		key: '_createParamMetadataList',
		value: function _createParamMetadataList(param) {
			var O = this;
			_log('PANEL SIM / _createParamMetadataList');

			var listData = {
				'ID': param.id,
				'Name': param.name,
				'Description': param.description,
				'Unit': param.unit,
				'Unit category': param.unitCategory,
				'Data type': param.dataType,
				'Source': param.source,
				'Subject': param.subject,
				'Distribution': param.distribution,
				'Reference': param.reference,
				'Variability subject': param.variabilitySubject,
				'Min value': param.minValue,
				'Max value': param.maxValue,
				'Error': param.error
			};
			_log(listData);

			// create table
			var $table = $('<table class="table table-sm table-hover table-params-metadata"></table>');

			// create rows
			$.each(listData, function (name, value) {

				if (value) {
					var $row = $('<tr></tr>').appendTo($table);
					$row.append('<td class="td-label">' + name + '</td>'); // label/name
					$row.append('<td>' + value + '</td>'); // value
				}
			});

			return $table;
		}

		/**
   * CREATE FORM FIELD
   * create field as form group
   * @param {array} param
   */

	}, {
		key: '_createFormField',
		value: function _createFormField(param) {
			var O = this;
			_log('PANEL SIM / _createFormField');
			_log(param);

			if (param) {

				// formgroup
				var _$formGroup2 = $('<div class="form-group row"></div>');
				// .appendTo( O._$simForm );

				// label
				var _$label2 = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'paramInput_' + param.id).appendTo(_$formGroup2);
				// set custom label or id
				param._label ? _$label2.text(param._label) : _$label2.text(param.id);

				// field
				var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>').appendTo(_$formGroup2);

				// actions
				var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo(_$formGroup2);

				// input item
				var $input = null;

				// set input type
				var inputType = null;
				if (param.dataType.toLowerCase() === 'integer' || param.dataType.toLowerCase() === 'double' || param.dataType.toLowerCase() === 'number') {
					inputType = 'number';
				} else if (param.dataType.toLowerCase() === 'simname') {
					inputType = 'simName'; // custom type for name and description
				} else if (param.dataType.toLowerCase() === 'simdescription') {
					inputType = 'simDescription'; // custom type for name and description
				} else if (param.dataType.toLowerCase() === 'vectorofnumbers') {
					inputType = 'vectorofnumbers';
				} else if (param.dataType.toLowerCase() === 'matrixofnumbers') {
					inputType = 'matrixofnumbers';
				} else {
					inputType = 'text';
				}

				// create param metadata action
				if (_isNull(param._showMetadata) || param._showMetadata === true) {
					// action metadata list
					var $actionMetadata = $('<button class="action action-pure" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#paramMetadata_' + param.id).attr('aria-expanded', false).attr('aria-controls', 'paramMetadata_' + param.id).attr('title', 'Show Metadata').appendTo($actions);
				}

				// add special action for vector or matrix of numbers
				if (inputType == 'vectorofnumbers') {

					var $actionVectoEditor = $('<button class="action action-pure" type="button"><i class="feather icon-edit"></i></button>').attr('title', 'Edit Vector').appendTo($actions);

					// TO DO
					// action what to do on click
				} else if (inputType == 'matrixofnumbers') {

					var _$actionVectoEditor = $('<button class="action action-pure" type="button"><i class="feather icon-edit"></i></button>').attr('title', 'Edit Matrix').appendTo($actions);

					// TO DO
					// action what to do on click
				}

				if (inputType) {

					// numeric
					if (inputType == 'number') {

						var $inputGroup = $('<div class="input-group input-group-sm"></div>').appendTo($field);

						$input = $('<input type="text" />').attr('id', 'paramInput_' + param.id).data('param-input', param).attr('aria-invalid', false).appendTo($inputGroup);

						// rangeslider single, if min/max
						if (param.minValue && param.maxValue) {

							var step = 1;
							// calc decimals for slider steps depending on min-max values
							if (param.dataType.toLowerCase() === 'double') {
								var decimals = Math.max(param.minValue.substring(param.minValue.indexOf('.') + 1).length, param.maxValue.substring(param.maxValue.indexOf('.') + 1).length);
								for (var j = 0; j < decimals; j++) {
									step = step / 10;
								}
							}

							// add rangeslider attributes
							$input.addClass('custom-range').attr('data-rangeslider', '').attr('data-step', step).attr('data-min', parseFloat(param.minValue)) // min value
							.attr('data-max', parseFloat(param.maxValue)) // max value
							.attr('data-control-single', '#paramControlSingle_' + param.id);

							// control input field for range value
							var $inputControl = $('<input type="text" class="form-control" />').attr('id', 'paramControlSingle_' + param.id).data('param-input', param).appendTo($field).wrap('<div class="input-range-controls"></div>').wrap('<div class="input-group input-group-sm input-range-control-single"></div>');

							// O._$simInputs.push( $inputControl );

							// add unit postfix to touchspin
							if (param.unit && param.unit != '[]' && param.unit != 'Others') {
								var $append = $('<div class="input-group-text"></div>').text(param.unit).insertAfter($inputControl).wrap('<div class="input-group-append"></div>');
							}
						}
						// touchspin
						else {
								$input.addClass('form-control form-control-sm').attr('data-touchspin', '');
								// add unit postfix to touchspin
								if (param.unit && param.unit != '[]' && param.unit != 'Others') {
									$input.attr('data-touchspin-postfix', param.unit);
								}
							}
					}
					// sim name
					else if (inputType == 'simName') {
							$input = $('<input type="text" class="form-control form-control-sm" />').attr('id', 'customInput_' + param.id).data('custom-input', param).appendTo($field);

							O._$simNameInput = $input;
						}
						// sim description
						else if (inputType == 'simDescription') {
								$input = $('<textarea type="text" class="form-control form-control-sm" rows="6" /></textarea>').attr('id', 'customInput_' + param.id).data('custom-input', param).appendTo($field);

								O._$simDescInput = $input;
							}
							// string or others
							else {
									$input = $('<input type="text" class="form-control form-control-sm" />').attr('id', 'paramInput_' + param.id).data('param-input', param).appendTo($field);
								}

					// readonly attribute
					param.classification === "CONSTANT" ? $input.attr('readonly', '') : null;

					// add $input to global variable
					O._$simInputs.push($input);
					O._simFields[param.id] = {
						input: $input,
						param: param
					};
				}

				// create validation container
				$input.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

				// create param metadata list
				if (_isNull(param._showMetadata) || param._showMetadata === true) {
					// metadata table
					var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'paramMetadata_' + param.id).attr('aria-expanded', false).appendTo($field);

					$metadataContainer.append(O._createParamMetadataList(param));
				}

				return _$formGroup2;
			}

			return null;
		}

		/**
   * POPULATE SIM SELECT
   * create select options 
   */

	}, {
		key: '_populateSimSelect',
		value: function _populateSimSelect() {
			var O = this;
			_log('PANEL SIM / _populateSimSelect');

			if (O._simulations && O._simulations.length > 0 && O._$simSelect) {
				// clear sim select
				O._$simSelect.empty();
				// options
				$.each(O._simulations, function (i, sim) {
					if (sim.name) {
						var $option = $('<option>' + sim.name + '</option>').appendTo(O._$simSelect);
					}
				});
				// update badge counter
				O._$simSelectCounter.text('x' + O._simulations.length);
			}
		}

		/**
   * POPULATE SIMULATION FORM
   * creates all input fields
   */

	}, {
		key: '_populateSimForm',
		value: function _populateSimForm() {
			var O = this;
			_log('PANEL SIM / _populateSimForm');

			// clear
			O._state == 'form';
			O._$simInputs = []; // stores all inputs in global var to provide access on params an custom fields like name and description
			O._simFields = {};
			O._selectedSimIndex = 0;
			O._$simForm ? O._clear(O._$simForm) : null; // clear form


			// create form
			O._$simForm = $('<form class="form-striped"></form>').attr('id', 'simParamsForm').appendTo(O._$modalParams._$content);

			// create mandatory custom form group sim name 
			var simNameParam = {
				id: 'simName',
				dataType: 'SIMNAME',
				_showMetadata: false,
				_label: 'Simulation Name',
				_isCustom: true,
				_on: {
					update: function update(O, $input) {
						O._updateSimName();
					}
				}
			};
			var $simNameFormGroup = O._createFormField(simNameParam);
			$simNameFormGroup ? $simNameFormGroup.appendTo(O._$simForm) : null;

			// create optional custom form group sim description 
			var simDescParam = {
				id: 'simDescription',
				dataType: 'SIMDESCRIPTION',
				_showMetadata: false,
				_label: 'Description (optional)',
				_isCustom: true,
				_on: {
					update: function update(O, $input) {
						O._updateSimDescription();
					}
				}
			};
			var $simDescFormGroup = O._createFormField(simDescParam);
			$simDescFormGroup ? $simDescFormGroup.appendTo(O._$simForm) : null;

			// model metadata params
			var params = O._modelMetadata['modelMath']['parameter'];
			if (params.length > 0) {
				// create form group for each param
				$.each(params, function (i, param) {

					if (param.classification != 'OUTPUT') {

						var _$formGroup3 = O._createFormField(param);
						_$formGroup3 ? _$formGroup3.appendTo(O._$simForm) : null;
					}
				});

				// init form items' functions: touchspin, range, select2 ...
				_appUI._initFormItems(O._$simForm);
			}
			_log(O._simFields);
		}

		/**
   * UPDATE SIMULATION INDEX
   * set selected simulation index and trigger update
   */

	}, {
		key: '_updateSimIndex',
		value: function _updateSimIndex(simIndex) {
			var O = this;
			_log('PANEL SIM / _updateSimIndex: ' + simIndex);

			simIndex = !_isNull(simIndex) ? simIndex : O._simSelectedIndex;

			if (simIndex >= -1 && simIndex != O._simSelectedIndex && simIndex <= O._simulations.length - 1) {
				// update sim index
				O._simSelectedIndex = simIndex;

				O._setState('params');

				// update inputs
				O._updateSimForm(simIndex);
				O._updateSimActions(simIndex);
			}

			// update badge counter
			O._$simSelectCounter.text('x' + O._simulations.length);
		}

		/**
   * UPDATE SIMULATION INPUTS
   * changes the selected simulation's inputs
   * @param {integer} simIndex of selected simulation: -1 ich add action, 0 = default
   */

	}, {
		key: '_updateSimForm',
		value: function _updateSimForm(simIndex) {
			var _this15 = this;

			var O = this;
			_log('PANEL SIM / _updateSimForm: ' + simIndex);

			simIndex = !_isNull(simIndex) ? simIndex : O._simSelectedIndex;

			// set value if param or custom
			var valIndex = simIndex;

			if (simIndex < 0) {
				valIndex = 0;
			}

			var simulation = O._simulations[valIndex];

			// disable parameter inputs for the default simulation.
			O._simDisabled = simIndex == 0;

			// remove error classes
			$('.has-error').removeClass('has-error');
			$('.is-invalid').removeClass('is-invalid');

			// update param values
			// $.each( O._$simInputs, ( i, $input ) => {

			$.each(O._simFields, function (id, field) {

				if (field.input && field.param) {

					// disable or enable 
					field.input.prop('disabled', O._simDisabled);

					// disable rangeslider 
					if (field.input.data('rangeslider')) {
						field.input.data('rangeslider').update({
							disable: O._simDisabled
						});
					}
					// disable touchspin
					else if (!_isUndefined(field.input.attr('data-touchspin'))) {
							// disable/enable buttons oof touchspin group
							field.input.parent().find('button').prop('disabled', O._simDisabled);
						}
					// param fields
					if (!field.param._isCustom) {
						// set value of current selected sim index
						_log(field.param.id + ' : ' + simulation.parameters[field.param.id], 'level1');
						var paramValue = simulation.parameters[field.param.id];

						!_isNull(paramValue) ? field.input.val(paramValue) : null;
					}
					// custom fields like name and desc
					else if (field.param._isCustom && field.param._isCustom == true) {

							_log(field.param.id + ' : ', 'level1');

							// check opt for custom update function 
							if (field.param._on && field.param._on.update && $.isFunction(field.param._on.update)) {
								field.param._on.update.call(_this15, O);
							}
						}

					// trigger change
					field.input.trigger('change');
				}
			});
		}

		/**
   * UPDATE SIMULATION ACTIONS
   * changes the selected simulation's inputs
   * @param {integer} simIndex of selected simulation
   */

	}, {
		key: '_updateSimActions',
		value: function _updateSimActions(simIndex) {
			var O = this;
			_log('PANEL SIM / _updateSimActions');

			simIndex = !_isNull(simIndex) ? simIndex : O._simSelectedIndex;

			if (simIndex == -1) {
				// add simulation
				O._$simActionRemove.prop('disabled', false);
				O._$simActionAdd.prop('disabled', true);
				O._$simActionSave.prop('disabled', false);
			} else if (simIndex == 0) {
				// default sim
				O._$simActionRemove.prop('disabled', true);
				O._$simActionAdd.prop('disabled', false);
				O._$simActionSave.prop('disabled', true);
			} else {
				// selected sim > 0
				O._$simActionRemove.prop('disabled', false);
				O._$simActionAdd.prop('disabled', false);
				O._$simActionSave.prop('disabled', false);
			}
		}

		/**
   * UPDATE SIMULATION CUTOM NAME
   * changes the custom input name to selected simulation's name
   */

	}, {
		key: '_updateSimName',
		value: function _updateSimName() {
			var O = this;
			_log('PANEL SIM / _updateSimName');

			if (O._$simNameInput) {
				var simName = '';
				if (!_isNull(O._simulations) && !_isNull(O._simSelectedIndex) && O._simulations[O._simSelectedIndex]) {
					// get simulation name
					simName = O._simulations[O._simSelectedIndex].name;
				}
				// set name
				O._$simNameInput.val(simName);
			}
		}

		/**
   * UPDATE SIMULATION CUSTOM DESCRIPTUION
   * changes the custom input description to selected simulation's name
   * @param {jquery elem} $input
   */

	}, {
		key: '_updateSimDescription',
		value: function _updateSimDescription($input) {
			var O = this;
			_log('PANEL SIM / _updateSimDescription');

			if (O._$simDescInput) {
				var simDesc = '';
				if (!_isNull(O._simulations) && !_isNull(O._simSelectedIndex) && O._simulations[O._simSelectedIndex]) {
					// get simulation name
					simDesc = O._simulations[O._simSelectedIndex].desc;
				}
				// set name
				O._$simDescInput.val(simDesc);
			}
		}

		/**
   * ADD SIMULATION
   * add the selected simulation
   */

	}, {
		key: '_addSimulation',
		value: function _addSimulation() {
			var O = this;
			_log('PANEL SIM / _addSimulation', 'secondary');

			// clear sim select
			O._$simSelect.val('');

			// update sim select to index for add-action : -1
			O._updateSimIndex(-1);
		}

		/**
   * REMOVE SIMULATION
   * remove the selected simulation
   */

	}, {
		key: '_removeSimulation',
		value: function _removeSimulation() {
			var O = this;
			_log('PANEL SIM / _removeSimulation', 'secondary');

			if (O._simSelectedIndex != 0) {
				// remove from sim select
				O._$simSelect.find('option').eq(O._simSelectedIndex).remove();

				// remove from simulations var
				O._simulations.splice(O._simSelectedIndex, 1);

				// reset to default
				O._$simSelect.find('option').eq(0).prop('selected', true);
				O._updateSimIndex(0);
			}
		}

		/**
   * SAVE SIMULATION
   * save the selected simulation
   */

	}, {
		key: '_saveSimulation',
		value: async function _saveSimulation() {
			var O = this;
			_log('PANEL SIM / _saveSimulation', 'secondary');
			// run validation
			var validation = await O._validateSimForm();

			// no errors ?
			if (validation.length == 0) {

				var newSimulation = JSON.parse(JSON.stringify(O._simulations[0]));
				// set new simulation's name
				newSimulation.name = O._$simNameInput.val();
				// set sim desc
				newSimulation.desc = O._$simDescInput ? O._$simDescInput.val() : '';

				// set new simulation's parameters
				if (O._simFields) {
					// for each input get value and et param value
					$.each(O._simFields, function (id, field) {

						if (field.input && !_isNull(field.param)) {
							// create simulation param
							newSimulation.parameters[field.param.id] = field.input.val();
						}
					});

					O._simulations.push(newSimulation);
					// re-populate sim select
					O._populateSimSelect();
					// update sim index
					O._$simSelect.find('option').last().prop('selected', true);
					O._updateSimIndex(O._simulations.length - 1);
				}
			}

			// TO DO 
			// save to endpoint ?
		}

		/**
   * VALIDATE SIMULATION FORM
   * run validation on sim form inputs
   */

	}, {
		key: '_validateSimForm',
		value: async function _validateSimForm() {
			var O = this;
			_log('PANEL SIM / _validateSimForm');

			var validationErrors = [];
			// remove error classes
			$('.has-error').removeClass('has-error');
			$('.is-invalid').removeClass('is-invalid');
			$('.validation-message').empty();

			// validate sim inputs values
			if (O._simFields) {

				$.each(O._simFields, function (id, field) {
					// let param = $input.data( 'param-input' );

					if (!_isNull(field.param)) {
						var validationParam = O._validateSimField(field);
						validationParam ? validationErrors.push(validationParam) : null;
					}
				});
			}

			// proceed error visualization
			$.each(validationErrors, function (i, error) {

				error.input.parents('.form-group').addClass('has-error');
				error.input.addClass('is-invalid');

				error.input.$validationContainer.text(error.msg);
				// let $errorMsg = $( '<div class="alert alert-danger alert-xs"></div>' )
				// 	.appendTo( error.input.$validationContainer )
				// 	.text( error.msg );
			});

			_log(validationErrors);

			return validationErrors;
		}

		/**
   * VALIDATE SIMULATION PARAM
   * run validation on param field
   */

	}, {
		key: '_validateSimField',
		value: function _validateSimField(field) {
			var O = this;
			_log('PANEL SIM / _validateSimField');
			_log(field);

			if (field && field.input) {

				var fieldValue = field.input.val();

				// check simulation name
				if (field.param.dataType.toLowerCase() === 'simname') {

					var idRegexp = /^[A-Za-z_^s]\w*$/;

					// name length
					if (fieldValue.length == 0) {
						return {
							input: field.input,
							msg: 'Simulation name is required'
						};
					}
					// name already exists?
					for (var i = 0; i < O._simulations.length; ++i) {
						if (fieldValue === O._simulations[i].name) {
							return {
								input: field.input,
								msg: 'Simulation name already exists'
							};
						}
					}
					// name fits regexp
					if (!idRegexp.test(fieldValue)) {
						return {
							input: field.input,
							msg: 'Not a valid simulation name (SId)'
						};
					}
				} else if (field.param.dataType.toLowerCase() === 'simdescription') {} else {

					// no value
					if (!fieldValue) {
						return {
							input: field.input,
							msg: 'Please provide a value for ' + field.param.id
						};
					}

					// check range for integers and doubles
					if (field.param.dataType.toLowerCase() === 'integer' || field.param.dataType.toLowerCase() === 'double') {

						if (field.param.minValue && parseFloat(field.param.minValue) > fieldValue) {
							return {
								input: field.input,
								msg: 'Invalid value. Value is lower than minimal value: ' + field.param.minValue
							};
						}
						if (field.param.maxValue && parseFloat(field.param.maxValue) < fieldValue) {
							return {
								input: field.input,
								msg: 'Invalid value. Value is higher than maximum value: ' + field.param.maxValue
							};
						}
					} else if (field.param.dataType.toLowerCase() === 'file') {

						// TBD

					} else if (field.param.dataType.toLowerCase() === 'vectorofnumbers') {

						// TBD

					} else if (field.param.dataType.toLowerCase() === 'matrixofnumbers') {

						// TBD

					}
				}
			}

			return false;
		}
		/**
  * BUILD PANEL
  * build PANEL content
  * @param {event} event 
  */

	}, {
		key: '_updateContent',
		value: async function _updateContent(_modelMetadata, _modelId, _simulations) {
			var O = this;
			_log('PANEL SIM / _updateContent');
			O._setState('params'); // reset state to form params when opening PANEL

			O._modelMetadata = _modelMetadata;
			O._modelId = _modelId;
			// clear tab-panes
			O._clear(O._$modalParams._$content);

			O._simSelectedIndex = 0; // reset simulation
			// get simulations
			O._simulations = _simulations;

			// create params		
			if (O._simulations && O._simulations.length > 0) {

				// populate sim select
				O._populateSimSelect();
				// populate sim form
				O._populateSimForm();
				// update form groups' value
				O._updateSimForm(0);
				O._updateSimActions(0);
			}
		}

		/**
   * RUN MODEL VIEW
   * run modelview
   */

	}, {
		key: '_runModelView',
		value: async function _runModelView(modelId) {
			var O = this;
			_log('PANEL SIM / _runModelView : ' + O._modelId, 'secondary');

			modelId = modelId || O._modelId;

			if (modelId >= 0) {

				O._fetchController = new AbortController();
				O._signal = O._fetchController.signal;

				// clear tab content
				O._clear(O._$modalExecution._$content);
				O._clear(O._$modalExecution._$title);
				O._setState('execution');

				// activate loader
				O._$container.addClass('loading');
				O._opts._loader._setState(true);
				// create loading alert
				var $alert = _appUI._createAlert('executing model... please wait', {
					type: 'primary',
					state: 'show',
					classes: 'm-1'
				}, O._$modalExecution._$content);

				// TO DO 
				// run simulation by selected index

				// execute result
				var result = await _fetchData._content(_endpoints.execution, modelId, O._signal); //O._app._getExecutionResult( modelId ) ;

				// clear tab content
				O._clear(O._$modalExecution._$content);
				O._clear(O._$modalExecution._$title);

				$alert.remove();

				// add executet simulation name as panel-title
				var $title = $('<div class="panel-heading"></div>').text(O._simulations[O._selectedSimIndex].name).appendTo(O._$modalExecution._$content);

				// add result as plot
				var $plot = $(result).appendTo(O._$modalExecution._$content).wrapAll('<div class="panel-plot"></div>');

				// deactivate loader
				O._$container.removeClass('loading');
				O._opts._loader._setState(false);

				// callback
				if ($.isFunction(O.opts.on.simRunModelView)) {
					O.opts.on.simRunModelView.call(O, O, modelId, O._simulations[O._selectedSimIndex]);
				}
			}
		}

		/**
   * CLEAR
   * removes content from element
   * @param {jquery selector} $elem
   */

	}, {
		key: '_clear',
		value: function _clear($elem) {
			var O = this;
			_log('PANEL SIM / _clear');
			_log($elem);

			if ($elem) {
				$elem.empty();
			}
		}

		/**
  * CLEAR
  * removes content from element
  * @param {string} state: execute or params
  */

	}, {
		key: '_setState',
		value: function _setState(state) {
			var O = this;
			_log('PANEL SIM / _setState: ' + state);
			if (state && O._state != state) {

				if (state == 'execution') {
					O._$modalExecution.show();
					O._$modalParams.hide();
					O._$modalNav.hide();
				} else {
					O._$modalExecution.hide();
					O._$modalParams.show();
					O._$modalNav.show();
				}
				// set sim modal state
				O._state = state;
			}
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPSimulation;
}();
/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

var APPMTDetails = function () {
	function APPMTDetails(settings, $container) {
		_classCallCheck(this, APPMTDetails);

		var O = this;
		// defaults maintable simulations modal
		O._$modalContent = $container;
		O._opts = $.extend(true, {}, {
			classes: '',
			data: null,
			on: {
				afterInit: null, // function
				show: function show(O, event) {
					O._updateModal(event);
				}, // function
				hide: null // function
			}
		}, settings);
		O._create();
	}

	_createClass(APPMTDetails, [{
		key: '_create',

		/**
   * CREATE
   * calls super class and sets _metadata
   */

		value: function _create() {
			var O = this;
			_log('MODAL DETAILS / _create', 'primary');

			O._metadata = O.opts.data;
		}
		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModelMetadataContent',
		value: function _createModelMetadataContent() {
			var O = this;
			_log('MODAL DETAILS / _createModelMetadataContent');
			// modal nav with tabs & search
			O._$modalNav = $('<div class="modal-body modal-nav"></div>').appendTo(O._$modalContent);

			O._navId = O._id + 'Nav';
			if (!O._$navBar) {
				O._$navBar = $('<nav class="navbar navbar-expand-sm row justify-content-start justify-content-md-between"></nav>').appendTo(O._$modalNav);

				// nav toggle
				var $navToggle = $('<button class="action action-pure mt-1 mb-1" type="button" data-toggle="collapse" aria-expanded="false" aria-label="Toggle navigation"><i class="feather icon-list"></i></button>').appendTo(O._$navBar).attr('data-target', '#' + O._navId).attr('aria-controls', O._navId).wrap('<div class="col-auto navbar-toggler order-1 modal-nav-toggler"></div>');

				// divider
				O._$navBar.append('<div class="col-divider order-2 d-block d-sm-none d-md-block"></div>');

				// nav search
				O._$navBar._$search = $('<input class="form-control form-control-plaintext search-input" type="search" placeholder="Search Details" aria-label="Search Details" />').appendTo(O._$navBar).attr('id', O._id + 'NavSearch').wrap('<div class="col col-xxs-auto order-2 modal-nav-search"></div>').wrap('<div class="search"></div>');

				// TO DO
				// search functionality


				// nav tabs
				O._$navBar._$nav = $('<ul class="nav nav-pointer pt-1 pt-md-0"></ul>').appendTo(O._$navBar).wrap('<div class="col-12 col-md-auto order-3 order-md-1 modal-nav-menu order-4"></div>').wrap('<div class="collapse navbar-collapse" id="' + O._navId + '"></div>');
			}

			// modal body
			O._createModalBody();
			O._$modalBody.addClass('p-0 modal-table');

			// content container
			O._$modalTabContent = $('<div class="tab-content h-100"></div>').appendTo(O._$modalBody);
		}

		/**
  * CREATE MODAL
  * creates basic modal components: header and blank body
  */

	}, {
		key: '_createModalBody',
		value: function _createModalBody() {
			var O = this;
			_log('MODAL / _createBody');

			O._$modalBody = $('<div class="modal-body"></div>').appendTo(O._$modalContent);
		}

		/**
  * BUILD PANEL
  * build PANEL content
  * @param {event} event 
  */

	}, {
		key: '_updateContent',
		value: async function _updateContent(_modelMetadata, _modelId) {
			var O = this;
			_log('PANEL MetaData / _updateContent');

			// clear tab-panes
			O._$modalTabContent.html('');

			// get appropiate modelMetadata modelHandler for the model type.
			O._modelHandler = await O._getModelHandler(_modelMetadata, _modelId);
			// populate nav
			O._populateModalNav(O._modelHandler, O._$navBar._$nav);

			// populate panel
			O._populateModalPanel(O._modelHandler);

			// activate first pane
			O._$navBar._$nav.find('.nav-link').first().addClass('active');
			O._$modalTabContent.find('.tab-pane').first().addClass('active');
		}

		/**
   * POPULATE MODAL MENU
   * @param {object} Model
   */

	}, {
		key: '_populateModalNav',
		value: function _populateModalNav(modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _populateModalNav');
			_log(modelHandler);

			// clear nav
			O._$navBar._$nav.html('');

			// create nav items
			if (modelHandler && modelHandler._menu) {

				$.each(modelHandler._menu, function (i, menuMeta) {
					if (menuMeta.id == 'readme') return;

					var $navItem = null;

					if (menuMeta.submenus && menuMeta.submenus.length > 0) {
						$navItem = O._createNavItemDropdown(menuMeta).appendTo(O._$navBar._$nav);
					} else {
						//if(menuMeta.id !== 'modelScript' && menuMeta.id !== 'visualizationScript' && menuMeta.id !== 'readme'){
						var _$navItem2 = O._createNavItem(menuMeta).appendTo(O._$navBar._$nav);
						//}
					}
				});
			}
		}

		/**
   * POPULATE MODAL PANEL
   * @param {object} Model
   */

	}, {
		key: '_populateModalPanel',
		value: function _populateModalPanel(modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _populateModalPanel');
			_log(modelHandler);

			// create panels
			if (modelHandler && modelHandler._menu && modelHandler._panels) {
				// get each menus id
				$.each(modelHandler._menu, function (i, menuMeta) {
					// dropdown nav item 

					if (menuMeta.id == 'readme') return;
					if (menuMeta.submenus && menuMeta.submenus.length > 0) {
						// iterate over submenus
						$.each(menuMeta.submenus, function (j, submenuMeta) {
							// panel meta data exists in handler
							if (submenuMeta.id in modelHandler._panels) {
								O._createPanel(submenuMeta, modelHandler).appendTo(O._$modalTabContent);
							}
						});
					}
					// single nav item ? create panel
					else {
							if (menuMeta.id) {
								if (menuMeta.id in modelHandler._panels) {
									//if(menuMeta.id !== 'modelScript' && menuMeta.id !== 'visualizationScript' && menuMeta.id !== 'readme'){
									O._createPanel(menuMeta, modelHandler).appendTo(O._$modalTabContent);
									//}
								}
							}
						}
				});
			}
		}

		/**
   * CREATE NAV ITEM DROPDOWN
   * @param {array} menuMeta: array of dropdown-items width 'id' and 'label'
   */

	}, {
		key: '_createNavItemDropdown',
		value: function _createNavItemDropdown(menuMeta) {
			var O = this;
			_log('MODAL DETAILS / _createTabNavItemDropdown: ' + menuMeta.label);

			var $navItem = $('<li class="nav-item dropdown"></li>');

			var $navLink = $('<a class="nav-link dropdown-toggle" role="button">' + menuMeta.label + '</a>').attr('href', '#').attr('aria-haspopup', true).attr('aria-expanded', false).attr('data-toggle', 'dropdown').appendTo($navItem);
			var $dropdown = $('<div class="dropdown-menu"></div>').appendTo($navItem);

			$.each(menuMeta.submenus, function (i, submenuMeta) {

				var $dropdownItem = $('<a class="dropdown-item" role="button">' + submenuMeta.label + '</a>').attr('href', '#' + submenuMeta.id).attr('aria-controls', '#' + submenuMeta.id).attr('data-toggle', 'tab').appendTo($dropdown);
			});

			return $navItem;
		}

		/**
   * CREATE NAV ITEM
   * @param {array} menuMeta
   */

	}, {
		key: '_createNavItem',
		value: function _createNavItem(menuMeta) {
			var O = this;
			_log('MODAL DETAILS / _createNavItem: ' + menuMeta.label);

			var $navItem = $('<li class="nav-item"></li>');
			var $navLink = $('<a class="nav-link" role="button">' + menuMeta.label + '</a>').attr('href', '#' + menuMeta.id).attr('aria-controls', '#' + menuMeta.id).attr('data-toggle', 'tab').appendTo($navItem);

			return $navItem;
		}

		/**
   * CREATE PANEL
   * create tab-pane for specific menu by selecting type and calling specific creation (simple, complex, plot)
   * @param {array} menu
   * @param {object} modelHandler: object of type Model
   */

	}, {
		key: '_createPanel',
		value: function _createPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createPanel: ' + menu.id);

			var $panel = null;
			if (modelHandler && menu.id) {

				var panelMeta = modelHandler._panels[menu.id];
				// panel type
				if (panelMeta.type) {

					// complex
					if (panelMeta.type == 'complex') {
						$panel = O._createComplexPanel(menu, modelHandler);
					}
					// simple
					else if (panelMeta.type == 'simple') {
							$panel = O._createSimplePanel(menu, modelHandler);
						}
						// plot
						else if (panelMeta.type == 'plot') {
								$panel = O._createPlotPanel(menu, modelHandler);
							}
							// Model Script
							else if (panelMeta.type == 'modelScript') {
									$panel = O._createModelScriptPanel(menu, modelHandler);
								}
								// Visualization Script
								else if (panelMeta.type == 'visualizationScript') {
										$panel = O._createVisualizationScriptPanel(menu, modelHandler);
									}
				}
			}

			return $panel;
		}

		/**
   * CREATE SIMPLE PANEL
   * create simple tab-pane for specific menu
   * table has property, value cols
   * @param {array} menu
   * @param {object} modelHandler: object of type Model
   */

	}, {
		key: '_createSimplePanel',
		value: function _createSimplePanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createSimplePanel: ' + menu.id);

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');

				// table settings
				var tableSettings = {
					cols: [{
						label: 'Property',
						field: 'property',
						classes: {
							th: null,
							td: 'td-label min-200'
						},
						sortable: true,
						switchable: false
					}, {
						label: 'Value',
						field: 'value',
						sortable: false,
						switchable: false
					}],
					tableData: [],
					responsive: true,
					showToggle: true
				};

				// set table row data
				if (panelMeta.metadata && panelMeta.schema) {
					$.each(panelMeta.schema, function (j, prop) {
						var rowData = {
							cells: []
						};
						// cell 1 label
						rowData.cells.push(prop.label);
						// cell 2 val
						var data = panelMeta.metadata[prop.id];
						data = _checkUndefinedContent(data);
						rowData.cells.push(data);

						tableSettings.tableData.push(rowData);
					});
				}

				// create table
				var panelTable = new APPTable(tableSettings, $panel);
				$panel.data('table', panelTable);
			};

			return $panel;
		}

		/**
   * CREATE COMPLEX PANEL
   * create complex tab-pane for specific menu
   * table has in metadata and schema defined cols
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createComplexPanel',
		value: function _createComplexPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createComplexPanel: ' + menu.id);

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');

				// table settings
				var tableSettings = {
					cols: [],
					tableData: [],
					responsive: true,
					showToggle: true
				};

				// set table cols
				$.each(panelMeta.schema, function (i, prop) {
					tableSettings.cols.push({
						label: prop.label,
						field: prop.id,
						sortable: true,
						switchable: true
					});
				});

				// set table row data
				if (panelMeta.metadata && panelMeta.schema) {
					$.each(panelMeta.metadata, function (i, item) {
						// row each item
						var rowData = {
							cells: []
						};
						// cells
						$.each(panelMeta.schema, function (j, prop) {
							var data = item[prop.id];
							data = _checkUndefinedContent(data);
							// cell each prop
							rowData.cells.push(data);
						});

						tableSettings.tableData.push(rowData);
					});
				}

				// create table
				var panelTable = new APPTable(tableSettings, $panel);
				$panel.data('table', panelTable);
			};
			return $panel;
		}

		/**
  	 * CREATE PLOT PANEL
  	 * create plot tab-pane for specific menu
  	 * @param {array} menu
  	 * @param {object} modelHandler: object of class Model
  	 */

	}, {
		key: '_createPlotPanel',
		value: function _createPlotPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createPlotPanel');

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id && modelHandler._img) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');

				var $plot = $('<figure class="figure"><img src="' + modelHandler._img + '" /></figure>').appendTo($panel).wrap('<div class="panel-plot"></div>');
			}

			return $panel;
		}
		/**
   * CREATE Model Script PANEL
   * create Model Script tab-pane for specific menu
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createModelScriptPanel',
		value: function _createModelScriptPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createPlotPanel');

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id && modelHandler._modelScript) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');
				var $script = $('<pre class="brush: js;"></pre>').appendTo($panel).wrap('<div class="panel-plot"></div>');

				var lines = modelHandler._modelScript.split("\n");
				for (var i = 0; i < lines.length; i++) {
					$('<span class="line">' + lines[i] + '</span>').appendTo($script);
				}
			}

			return $panel;
		}
		/**
   * CREATE VISUALIZATION SCRIPT
   * create plot tab-pane for specific menu
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createVisualizationScriptPanel',
		value: function _createVisualizationScriptPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createPlotPanel');

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id && modelHandler._visScript) {
				// get panel meta
				_log('ifffff visualizationScript: ' + modelHandler._visScript);
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');
				var $script = $('<pre class="brush: js;"></pre>').appendTo($panel).wrap('<div class="panel-plot"></div>');

				var lines = modelHandler._visScript.split("\n");
				for (var i = 0; i < lines.length; i++) {
					$('<span class="line">' + lines[i] + '</span>').appendTo($script);
				}
			}

			return $panel;
		}

		/**
   * GET MODEL HANDLER
   * returns model handler of class Model
   * @param {array} modelMetadata: metadata for specific id
   */

	}, {
		key: '_getModelHandler',
		value: async function _getModelHandler(modelMetadata, modelId) {
			var O = this;
			_log('MODAL DETAILS / _getModelHandler');

			var modelHandler = null;

			if (modelMetadata) {

				// get plot image
				var imgUrl = await _fetchData._blob(_endpoints.image, modelMetadata.generalInformation.identifier); // O._app._getImage( modelMetadata.generalInformation.identifier );
				var modelScript = await _fetchData._content(_endpoints.modelScript, modelId); // O._app._getImage( modelMetadata.generalInformation.identifier );
				var visScript = await _fetchData._content(_endpoints.visScript, modelId); // O._app._getImage( modelMetadata.generalInformation.identifier );
				// get appropiate modelMetadata modelHandler for the model type.
				if (modelMetadata.modelType === 'genericModel') {
					modelHandler = new GenericModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'dataModel') {
					modelHandler = new DataModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'predictiveModel') {
					modelHandler = new PredictiveModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'otherModel') {
					modelHandler = new OtherModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'toxicologicalModel') {
					modelHandler = new ToxicologicalModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'doseResponseModel') {
					modelHandler = new DoseResponseModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'exposureModel') {
					modelHandler = new ExposureModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'processModel') {
					modelHandler = new ProcessModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'consumptionModel') {
					modelHandler = new ConsumptionModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'healthModel') {
					modelHandler = new HealthModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'riskModel') {
					modelHandler = new RiskModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else if (modelMetadata.modelType === 'qraModel') {
					modelHandler = new QraModel(modelMetadata, imgUrl, false, modelScript, visScript);
				} else {
					modelHandler = new GenericModel(modelMetadata, imgUrl, false, modelScript, visScript);
				}
			}

			return modelHandler;
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPMTDetails;
}();
/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

var APPTable = function () {
	function APPTable(settings, $container, metadata) {
		_classCallCheck(this, APPTable);

		var O = this;
		O._$container = $container;
		O._$wrapper = null;
		O.totalRows = 0;
		// defaults
		O._opts = $.extend(true, {}, {
			attributes: {}, // attribute : value pairs for <table>
			classes: '', // string of classes for <table>
			cols: [], // cols definition as array
			tableData: null, // table data
			ids: { // ids for specific table elements
				table: 'tGrid',
				thead: 'tHead',
				tbody: 'tRows'
			},
			responsive: true, // wrap table with .table-responsive
			rowActions: [],
			rowSelectable: false, // 'single', // 'multiple', //
			showToggle: true, // show card view toggle
			wrapper: false, // 'card'
			on: { // hooks/callbacks on specific events
				afterInit: null,
				afterPopulate: null,
				selectRow: null,
				deselectRow: null
			}
		}, settings);
		O._metadata = metadata;
		// basic init actions
		O._create();

		// callback
		if ($.isFunction(O.opts.on.afterInit)) {
			O.opts.on.afterInit.call(O, O);
		}
	}

	_createClass(APPTable, [{
		key: 'addRow',
		value: function addRow(rowIndex, rowData, tableData, isMainTable) {

			var O = this;
			tableData = O._tableData;
			// row
			var $tr = $('<tr data-row-id="' + rowIndex + '"></tr>').appendTo(O._$tbody);

			// rows selectable
			if (O.opts.rowSelectable) {

				// add selectable attribrutes
				$tr.data('selectable', '');
				$tr.data('selected', false);

				// add row click actions
				$tr.on('click', function (event) {
					// activate row not on click on buttons, actions or links in the table
					if (!$(event.target).is('a, button, .action') && !$(event.target).parent().is('a, button, .action')) {
						// select row
						O._handleRowSelect($tr);
					}
				});
			}

			// complete table data by adding row element to certain row data
			if (isMainTable) tableData[rowIndex].el = $tr;

			// create cols
			$.each(O.opts.cols, function (j, col) {
				var data = void 0;
				if (rowData.cells) data = rowData.cells[j];else data = rowData[col.field];
				var $td = $('<td></td>').appendTo($tr);
				if (data && data.length > 60) {
					col.collapsable = "true";
				}
				col.classes && col.classes.td ? $td.addClass(col.classes.td) : null; // classes
				col.collapsable ? $td.attr('data-td-collapse', col.collapsable) : null; // data collapsable
				col.label ? $td.attr('data-label', col.label) : null; // add data-label for toggle view cards
				col.field ? $td.attr('data-id', col.field) : null;

				// td attributes
				if (col.attributes && col.attributes.td) {
					$.each(col.attributes.td, function (attr, val) {
						attr && val ? $td.attr(attr, val) : '';
					});
				}

				// check for function that format the data
				if (col.formatter) {
					if ($.isFunction(col.formatter)) {
						data = col.formatter.call(O, data);
					} else if (_formatter && _formatter.hasOwnProperty(col.formatter)) {
						data = _formatter[col.formatter].call(O, data);
					}
				}

				// fill td with data
				$td.html(data);
			});
			// create row actions 
			if (O.opts.rowActions && O.opts.rowActions.length > 0) {

				// create action col
				var $tdActions = $('<td class="td-actions"></td>').appendTo($tr);

				// create row actions 
				$.each(O.opts.rowActions, function (j, action) {

					// create action element
					var $action = $('<button class="action action-outline-secondary"></button>').attr('id', action.idPrefix + rowIndex);
					// .appendTo( $tdActions );

					// create action icon
					$action.$icon = $('<i class="feather"></i>').appendTo($action);
					// set icon by class
					action.icon ? $action.$icon.addClass(action.icon) : null;

					// set tooltip and title
					if (action.title) {
						$action.attr('data-tooltip', '').attr('aria-label', action.title).attr('title', action.title);
					}

					// action on click
					if (action.on) {
						if (action.on.click && $.isFunction(action.on.click)) {
							// bind click action on action
							$action.on('click', function (event) {
								actionIndex = $tr.attr('data-row-id');
								action.on.click.call(O, O, $action, actionIndex, rowData);
							});
						}
					}

					// add action type specific attributes
					if (action.type) {
						// create modal action
						if (action.type == 'modal') {
							$action.attr('data-toggle', 'modal').attr('data-target', action.target).attr('data-modal-id', rowIndex);
						}
					}
					// append to td
					$action.appendTo($tdActions);
				});

				// wrap actions with inner container of td
				$tdActions.wrapInner('<div class="td-actions-container"></div>');
			}
			O.totalRows = rowIndex;
		}
		/**
   * CREATE TABLE HEAD
   * @param
   */

	}, {
		key: '_create',
		value: async function _create() {
			var O = this;
			_log('TABLE / _create', 'primary');

			// current state of view
			O._view = 'default'; // card; //

			// table rows
			O._tableData = O.opts.tableData;
			_log(O._tableData);

			// callback on create
			if ($.isFunction(O.opts.on.create)) {
				O.opts.on.create.call(O, O);
			}

			if (O._tableData) {

				// create table element
				O._$table = $('<table class="table table-striped table-hover"></table>').attr('id', O.opts.ids.table).attr('data-table', '').addClass(O.opts.classes).appendTo(O._$container);

				// wrapper
				if (O.opts.wrapper) {
					// card
					if (O.opts.wrapper == 'card') {
						O._$wrapper = $('<div class="card card-table-main overflow-hidden"></div>');
						O._$table.wrap(O._$wrapper);
						O._$table.wrap('<div class="card-body p-0"></div>');
					}
				}

				// responsive table wrapper
				if (O.opts.responsive) {
					O._$table.wrap('<div class="table-responsive"></div>');
				}

				// custom classes
				O.opts.classes ? O._$table.addClass(O.opts.classes) : null;

				// card view toggle
				O.opts.showToggle ? O._$table.attr('data-show-toggle', O.opts.showToggle) : null; // bs table / data-show-toggle for view: table/list

				// custom table attributes
				$.each(O.opts.attributes, function (attr, val) {
					O._$table.attr(attr, val);
				});

				// toolbar
				O._createTableToolbar();

				// create table head
				O._$thead = O._createTableHead(O.opts.cols).appendTo(O._$table);

				// create table head
				O._$tbody = O._createTableBody().appendTo(O._$table);

				// populate table
				O._populateTable(O._tableData);
			}

			// tooltips
			_appUI._initTooltips(O._$container);
		}

		/**
   * CREATE TABLE HEAD
   * @param {array} cols
   */

	}, {
		key: '_createTableHead',
		value: function _createTableHead(cols) {
			var O = this;
			_log('TABLE / _createTableHead');

			// thead
			var $thead = $('<thead></thead>');

			// create th cols
			if (cols) {
				$.each(cols, function (i, col) {
					var $th = $('<th></th>').appendTo($thead);

					// th attributes
					col.label ? $th.html('<span>' + col.label + '</span>') : null;
					col.id ? $th.attr('id', col.id) : null; // id
					col.classes && col.classes.th ? $th.addClass(col.classes.th) : null; // classes
					col.field ? $th.attr('data-field', col.field) : null; // bs table / data-field identifier

					// add sort functionality
					if (col.sortable) {
						$th.attr('data-sortable', col.sortable);
						$th.on('click', function (event) {
							O._updateOrder($th);
						});
						col.sorter ? $th.attr('data-sorter', col.sorter) : null; // bs table / data-sorter function
					}

					// th custom attributes
					if (col.attribute && col.attributes.th) {
						$.each(col.attributes.th, function (attr, val) {
							$th.attr(attr, val);
						});
					}
				});

				// create row actions col
				if (O.opts.rowActions && O.opts.rowActions.length > 0) {
					var $th = $('<th></th>').attr('id', 'colActions').attr('data-field', 'actionsTable').appendTo($thead);
				}

				$thead.wrapInner('<tr></tr>');
			}

			return $thead;
		}

		/**
   * CREATE TABLE TOOLBAR
   * creates button bar above the table for responsive toggle
   */

	}, {
		key: '_createTableToolbar',
		value: function _createTableToolbar() {
			var O = this;
			_log('TABLE / _createTableToolbar');

			if (O.opts.showToggle || O.opts.showColumns) {
				O._$toolbar = $('<div class="table-toolbar"></div>');

				if (O.opts.responsive) {
					O._$toolbar.insertBefore(O._$table.parent());
				} else {
					O._$toolbar.insertBefore(O._$table);
				}
				// place button group
				O._$toolbar._$btnGroup = $('<div class="col-auto btn-group"></div>').appendTo(O._$toolbar).wrap('<div class="ml-auto row justify-content-end"></div>');

				// create toolbar buttons 
				// toggle view
				if (O.opts.showToggle) {
					O._$toolbar._$btnToggleView = $('<button class="btn btn-outline-secondary btn-sm btn-icon toggle-card" type="button"><i class="feather icon-pause"></i></button>').attr('aria-label', 'Toggle view').attr('title', 'Toggle view').attr('data-tooltip', '').attr('data-toggle-table-view', '').appendTo(O._$toolbar._$btnGroup);

					O._$toolbar._$btnToggleView.on('click', function (event) {
						O._toggleTableView();
					});
				}
				if (O._opts.editableToolbarbuttons) {
					$.each(O._opts.editableToolbarbuttons, function (index, element) {
						element.appendTo(O._$toolbar._$btnGroup);
					});
				}
			}
		}

		/**
   * CREATE TABLE BODY
   * @param {object} tableData: metadata object
   */

	}, {
		key: '_createTableBody',
		value: function _createTableBody() {
			var O = this;
			_log('TABLE / _createTableBody');

			// table body
			var $tbody = $('<tbody></tbody>');

			return $tbody;
		}

		/**
   * POPULATE TABLE
   * @param {object} tableData: data object
   */

	}, {
		key: '_populateTable',
		value: function _populateTable(tableData) {
			var _this16 = this;

			var O = this;
			_log('TABLE / _populateTable');
			O._clear();

			tableData = tableData || O._tableData;

			// create rows
			$.each(tableData, function (rowIndex, rowData) {
				_this16.addRow(rowIndex, rowData, tableData, 'true');
			});

			// callback
			if ($.isFunction(O.opts.on.afterPopulate)) {
				O.opts.on.afterPopulate.call(O, O, O._tableData);
			}
		}

		/**
   * UPDATE ORDER
   * returns col index by field identifier
   * @param {string} field name of the column
   */

	}, {
		key: '_updateOrder',
		value: function _updateOrder($th) {
			var O = this;
			_log('TABLE / _updateOrder');

			var field = $th.data('field');
			var $rows = O._$tbody.find('tr').toArray();
			var col = O._getColIndexByField(field);

			// if selected item has alreay asc or desc class, just reverse contents
			if ($th.is('.asc') || $th.is('.desc')) {

				// toggle to other class
				$th.toggleClass('asc desc');

				// reverse the array
				O._$tbody.append($rows.reverse());
			}
			// otherwise perform a sort 
			else {
					// add class to header                            
					$th.addClass('asc');

					// remove asc or desc from all other headers
					$th.siblings().removeClass('asc desc');

					var fieldSorter = $th.data('sorter') || '_name';
					_log('sort by : ' + field + ' with ' + fieldSorter, 'level1');

					if (fieldSorter) {
						// fieldsorter is a function
						if ($.isFunction(fieldSorter)) {
							$rows.sort(function (a, b) {

								aa = $(a).find('td').eq(col).text(); // get text of column in row a
								bb = $(b).find('td').eq(col).text(); // get text of column in row b
								log(aa + ' / ' + bb);

								fieldSorter.call(aa, bb);
							});
						}
						// or fieldsorter is a sub routine of _sorter
						else if (_sorter && _sorter.hasOwnProperty(fieldSorter)) {
								// call sort() on rows array
								$rows.sort(function (a, b) {

									aa = $(a).find('td').eq(col).text(); // get text of column in row a
									bb = $(b).find('td').eq(col).text(); // get text of column in row b

									// call compare method
									return _sorter[fieldSorter](aa, bb);
								});
							}

						O._$tbody.append($rows);
					}
				}
		}

		/**
   * GET COL INDEX BY FIELD
   * returns col index by field identifier
   * @param {string} field name of the column
   */

	}, {
		key: '_getColIndexByField',
		value: function _getColIndexByField(field) {
			var O = this;

			var colIndex = -1;
			$.each(O.opts.cols, function (i, col) {
				if (field == col.field) {
					colIndex = i;
				}
			});

			return colIndex;
		}

		/**
   * CLEAR TABLE
   * removes table body rows
   */

	}, {
		key: '_clear',
		value: function _clear() {
			var O = this;
			_log('TABLE / _clear');

			if (O._$tbody) {
				O._$tbody.empty();
			}
		}

		/**
   * TOGGLE VIEW
   * change view of table <> card
   */

	}, {
		key: '_toggleTableView',
		value: function _toggleTableView(event) {
			var O = this;
			_log('TABLE / _toggleTableView');

			_log(O._view);
			// current state = default => set card view class
			if (O._view == 'default') {
				O._view = 'card';
				O._$table.addClass('table-view-card');
				// update toggles class
				O._$toolbar._$btnToggleView.removeClass('toggle-card').addClass('toggle-default');
			}
			// current state != default => set remove view class
			else {
					O._view = 'default';
					O._$table.removeClass('table-view-card');
					// update toggles class
					O._$toolbar._$btnToggleView.removeClass('toggle-default').addClass('toggle-card');
				}
		}

		/**
   * SELECT ROW
   * select or deselect a certain row
   * @param {dom object} $tr: jquery element 
   */

	}, {
		key: '_handleRowSelect',
		value: function _handleRowSelect($tr) {
			var O = this;
			// rows selectable and tr exists
			if (O.opts.rowSelectable && $tr) {
				// get current state
				var isSelected = $tr.data('selected');

				// already selected
				if (isSelected) {
					O._deselectRow($tr);
				} else {
					// deselect all
					if (O.opts.rowSelectable == 'single') {
						O._deselectAllRows();
						O._selectRow($tr);
					} else {
						O._selectRow($tr);
					}
				}
			}
		}

		/**
   * SELECT ROW
   * select a certain row
   * @param {dom object} $tr: jquery element 
   */

	}, {
		key: '_selectRow',
		value: function _selectRow($tr) {
			var O = this;
			_log('TABLE / _selectRow : ' + $tr.data('row-id'));

			if ($tr) {

				var rowIndex = $tr.data('row-id');
				var rowData = O._tableData[rowIndex];
				// add class marker
				$tr.addClass('tr-selected');
				$tr.data('selected', true);

				// callback on select row
				if ($.isFunction(O.opts.on.selectRow)) {
					O.opts.on.selectRow.call(O, O, rowIndex, rowData);
				}
			}
		}

		/**
   * DESELECT ROW
   * deselect a certain row
   * @param {dom object} $tr: jquery element 
   */

	}, {
		key: '_deselectRow',
		value: function _deselectRow($tr) {
			var O = this;
			_log('TABLE / _deselectRow : ' + $tr.data('row-id'));

			if ($tr) {

				var rowIndex = $tr.data('row-id');
				var rowData = O._tableData[rowIndex];

				// remove class marker
				$tr.removeClass('tr-selected');
				$tr.data('selected', false);

				// callback on de-select row
				if ($.isFunction(O.opts.on.deselectRow)) {
					O.opts.on.deselectRow.call(O, O, rowIndex, rowData);
				}
			}
		}

		/**
   * DESELECT ALL ROWS
   * deselect all rows
   */

	}, {
		key: '_deselectAllRows',
		value: function _deselectAllRows() {
			var O = this;
			_log('TABLE / _deselectAllRows');

			O._$table.find('.tr-selected').each(function (i, tr) {
				var $tr = $(tr);
				// remove class marker
				$tr.removeClass('tr-selected');
				$tr.data('selected', false);
			});
		}

		/**
   * GET SELECTD ROWS
   * deselect all rows
   * @return {object} tabledData filtered by rows selected
   */

	}, {
		key: '_getSelectedRows',
		value: function _getSelectedRows() {
			var O = this;
			_log('TABLE / _getSelectedRows');

			var selectedRows = [];

			// check all table rows
			$.each(O._tableData, function (i, row) {
				// row is currently selected and not hidden
				if (row.el.is('.tr-selected') && !row.el.is('.tr-hidden')) {
					selectedRows.push(row);
				}
			});

			return selectedRows;
		}

		/**
   * UPDATE STRIPES
   * updates stripe classes for each row
   */

	}, {
		key: '_updateStripes',
		value: function _updateStripes() {
			var O = this;
			_log('TABLE / _updateStripes');

			if (O._$table.is('.table-striped')) {

				// update stripes
				O._$tbody.find('tr:not(.tr-hidden)').each(function (i, tr) {

					var $tr = $(tr);
					$tr.removeClass('tr-odd tr-even');

					if ((i + 1) % 2 == 0) {
						$tr.addClass('tr-even');
					} else {
						$tr.addClass('tr-odd');
					}
				});
			}
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPTable;
}();
/*

version: 1.0.0
author: sascha obermüller
date: 06.12.2020

*/

var APPTableMT = function (_APPTable) {
	_inherits(APPTableMT, _APPTable);

	function APPTableMT(settings, $container, metadata) {
		_classCallCheck(this, APPTableMT);

		// defaults maintable
		var tableSettings = $.extend(true, {}, {
			classes: 'table-main',
			ids: {
				table: 'mtGrid',
				thead: 'mtHead',
				tbody: 'mtRows',
				filter: 'mtFilter'
			},
			rowActions: [],
			rowSelectable: false, //'multiple', // 'single', // 
			wrapper: 'card',
			on: {
				afterInit: null,
				selectRow: null,
				deselectRow: null,
				updateFilter: null
			}
		}, settings);
		return _possibleConstructorReturn(this, (APPTableMT.__proto__ || Object.getPrototypeOf(APPTableMT)).call(this, tableSettings, $container, metadata));
	}

	_createClass(APPTableMT, [{
		key: '_create',
		value: async function _create() {
			var O = this;
			_log('TABLE MAIN / _create', 'primary');

			O._facets = {}; // column facets
			O._filtered = []; // all hidden/filtered rows

			// current state of view
			O._view = 'default'; // card; //

			// loader
			O._loader = _appUI._createLoader({ classes: 'loader-page' }, O._$container);
			O._loader._setState(true);

			if (O._metadata) {
				await O._prepareDataTable();
			} else {
				// get full metadata and create tabledata
				await O._createData();
				await O._prepareDataTable();
			}

			// set loader
			O._loader._setState(false);

			// callback on create
			if ($.isFunction(O.opts.on.create)) {
				O.opts.on.create.call(O, O);
			}

			if (O._tableData) {

				// create filter
				O._createFilter();

				// create table element
				O._$table = $('<table class="table table-striped table-hover table-main"></table>').appendTo(O._$container).attr('id', O.opts.ids.table);

				// wrapper
				if (O.opts.wrapper) {
					// card
					if (O.opts.wrapper == 'card') {
						O._$table.add(O._$filter).wrapAll($('<div class="card card-table-main overflow-hidden"></div>'));
						O._$table.wrap('<div class="card-body p-0"></div>');
						O._$filter.wrap('<div class="card-header p-0"></div>');
					}
				}

				// responsive table wrapper
				if (O.opts.responsive) {
					O._$table.wrap('<div class="table-responsive"></div>');
				}

				// card view toggle
				O.opts.showToggle ? O._$table.attr('data-show-toggle', O.opts.showToggle) : null; // bs table / data-show-toggle for view: table/list

				// custom table attributes
				$.each(O.opts.attributes, function (attr, val) {
					O._$table.attr(attr, val);
				});

				// toolbar
				O._createTableToolbar();

				// create table head
				O._$thead = O._createTableHead(O.opts.cols).appendTo(O._$table);

				// create table head
				O._$tbody = O._createTableBody(O._tableData).appendTo(O._$table);

				// populate table
				O._populateTable(O._tableData);

				_appUI._initTdCollapse(O._$table);

				// set counter
				O._updateFilter();
			}

			// tooltips
			_appUI._initTooltips(O._$container);
		}

		/**
   * _refresh
   * _refresh the table after editing.
   * @param {string} query
   */

	}, {
		key: '_refresh',
		value: async function _refresh(index, modelMetadata) {
			var O = this;
			var rowData = O._tableData[index];
			rowData.modelMetadata = modelMetadata;
			var modelName = O._getData(modelMetadata, 'generalInformation', 'name');
			rowData.el.find('td[data-label="Model"]').html(modelName);
			var software = O._getData(modelMetadata, 'generalInformation', 'software');
			rowData.el.find('td[data-label="Software"]').html(software);
			console.log('_refresh', rowData);
			var environment = O._getScopeData(modelMetadata, 'scope', 'product', 'productName');
			rowData.el.find('td[data-label="Environment"]').html(environment);
			var hazard = O._getScopeData(modelMetadata, 'scope', 'hazard', 'hazardName');
			rowData.el.find('td[data-label="Hazard"]').html(hazard);
			var modelType = modelMetadata['modelType'];
			rowData.el.find('td[data-label="Type"]').html(modelType);
			// update sets
			if (software) O._updateSet('software', software);
			if (environment) {
				environment.forEach(function (x) {
					O._updateSet('environment', x);
				});
			}
			if (hazard) {
				hazard.forEach(function (x) {
					O._updateSet('hazard', x);
				});
			}
			if (modelType) O._updateSet('modelType', modelType);
		}
		/**
   * CREATE DATA
   * create tabledata
   */

	}, {
		key: '_createData',
		value: async function _createData() {
			var O = this;
			_log('TABLE MAIN / _createData');

			O._metadata = await _fetchData._json(window._endpoints.metadata); //O._app._getMetadata();
			O._uploadDates = await _fetchData._array(window._endpoints.uploadDate, O._metadata.length); //O._app._getUploadDates( window._endpoints.uploadDate, O._metadata.length );
			O._executionTimes = await _fetchData._array(window._endpoints.executionTime, O._metadata.length); //O._app._getExecutionTimes( window._endpoints.executionTime, O._metadata.length );


			_log(O._tableData);
		}
	}, {
		key: '_prepareDataTable',
		value: async function _prepareDataTable() {
			_log('TABLE MAIN / _prepareDataTable', 'primary');
			var O = this;
			console.log(O._metadata);
			// prepare table data
			O._tableData = [];

			var _loop2 = function _loop2(i) {

				var modelMetadata = O._metadata[i]; // full metadata of model
				var rowData = {
					modelMetadata: modelMetadata, // storess full model metadata for callbacks/hooks
					cells: [], // will contain raw cell value
					el: null // will be added later in _populateTable
				}; // model data container for table output

				// create table data of models for output
				$.each(O.opts.cols, function (j, col) {

					var data = null;

					if (col.field == 'modelName') {
						data = O._getData(modelMetadata, 'generalInformation', 'name');
					} else if (col.field == 'software') {
						data = O._getData(modelMetadata, 'generalInformation', 'software');
					} else if (col.field == 'hazard') {
						data = O._getScopeData(modelMetadata, 'scope', 'hazard', 'hazardName');
						// if formatter is not list join array
						if (col.formatter != '_list') {
							data = Array.from(data).join(' ');
						}
						// let joiner = col.formatter == 'list' ? '||' ' ';
						// data = Array.from( data ).join( '' );
					} else if (col.field == 'environment') {
						data = O._getScopeData(modelMetadata, 'scope', 'product', 'productName');
						// if formatter is not list join array
						if (col.formatter != '_list') {
							data = Array.from(data).join(' ');
						}
					} else if (col.field == 'modelType') {
						data = modelMetadata['modelType'];
					} else if (col.field == 'executionTime') {
						data = O._executionTimes[i];
					} else if (col.field == 'uploadDate') {
						data = O._uploadDates[i];
					}

					rowData.cells.push(data);
				});

				O._tableData.push(rowData);
			};

			for (var i = 0; i < O._metadata.length; i++) {
				_loop2(i);
			}
			_log(O._tableData);
		}

		/**
   * CREATE FILTER
   * @param
   */

	}, {
		key: '_createFilter',
		value: function _createFilter() {
			var O = this;
			_log('TABLE MAIN / _createFilter');

			// prepare sets for filter
			O._sets = O._sets || {};
			O._sets.software = new Set();
			O._sets.environment = new Set();
			O._sets.hazard = new Set();
			O._sets.modelType = new Set();

			for (var i = 0; i < O._metadata.length; i++) {

				var _modelMetadata2 = O._metadata[i];
				var software = O._getData(_modelMetadata2, 'generalInformation', 'software');
				var environment = O._getScopeData(_modelMetadata2, 'scope', 'product', 'productName');
				var hazard = O._getScopeData(_modelMetadata2, 'scope', 'hazard', 'hazardName');
				var modelType = _modelMetadata2['modelType'];

				// update sets
				if (software) O._updateSet('software', software);
				if (environment) {
					environment.forEach(function (x) {
						O._updateSet('environment', x);
					});
				}
				if (hazard) {
					hazard.forEach(function (x) {
						O._updateSet('hazard', x);
					});
				}
				if (modelType) O._updateSet('modelType', modelType);
			}

			// create table element
			O._$filter = $('<div class="filter">').attr('id', O.opts.ids.filter).appendTo(O._$container);

			// navbar
			var $navbar = $('<nav class="navbar navbar-expand-lg row justify-content-between"></nav>').appendTo(O._$filter);

			// navbar toggle
			var $navbarToggle = $('<button class="action action-pure collapsed" type="button" data-tooltip data-toggle="collapse" data-target="#mainTableFilterFacets" aria-controls="mainTableFilter" aria-expanded="false" title="Toggle Filter" aria-label="Toggle Filter"><span class="feather icon-sliders"></span></button>');
			$navbarToggle.appendTo($navbar).wrap('<div class="navbar-toggler col-auto order-1 filter-toggler"></div>');

			// divider 1
			$navbar.append('<div class="col-divider order-2 d-block d-lg-none"></div>');

			// filter search
			O._$search = $('<input id="mainTableFilterSearch" class="form-control form-control-plaintext search-input" type="search" placeholder="Search Models" aria-label="Search Models" />').appendTo($navbar).wrap('<div class="col col-xxs-auto order-3 filter-search">').wrap('<div class="search w-100"></div>');

			// search clear button
			O._$searchClear = $('<button class="search-clear" data-clear="#mainTableFilterSearch"><i class="feather icon-x"></i></button>').insertAfter(O._$search);

			// custom search
			O._$search.on('keyup, change', function (event) {
				// get the query by updating filter
				O._updateFilter();
			});

			// divider 2
			$navbar.append('<div class="col-divider order-4 d-none d-xxs-block"></div>');

			// facets
			O._$facets = $('<div id="mainTableFilterFacets" class="collapse navbar-collapse row mt-1 mt-lg-0 facets"></div>');
			O._$facets.appendTo($navbar).wrap('<div class="col-12 col-lg-8 order-5 order-xss-6 order-lg-5 filter-facets">');
			// create all facets of cols
			$.each(O.opts.cols, function (i, col) {
				if (col.field && col.facet) {
					var $facet = $('<select class="form-control form-control-sm" style="" multiple="multiple"></select>');
					$facet.appendTo(O._$facets);

					// crate facet wrapper
					var $facetWrapper = $('<div class="col-12 col-xs-6 col-md-3 facet"></div>');
					col.facet.tooltip && col.facet.placeholder ? $facetWrapper.attr('data-tooltip', col.facet.tooltip) : null;
					col.facet.tooltip && col.facet.placeholder ? $facetWrapper.attr('title', 'Filter by ' + col.facet.placeholder) : null;

					$facet.wrap($facetWrapper);

					// set unique id
					if (col.id) {
						$facet.id = col.id + 'Facet';
					} else {
						$facet.id = 'facet-' + $.now();
					}

					// facet attributes
					$facet.attr('id', $facet.id);
					col.facet.select2 ? $facet.attr('data-sel2', col.facet.select2) : null;
					col.facet.select2SingleRow ? $facet.attr('data-sel2-choice-single-row', col.facet.select2SingleRow) : null;
					col.facet.placeholder ? $facet.attr('data-placeholder', col.facet.placeholder) : null;
					col.facet.maxSelectable ? $facet.attr('data-maximum-selection-length', col.facet.maxSelectable) : null;

					// create action on filter facet
					$facet.on('change', function (event) {
						O._updateFilter();
					});

					O._facets[col.field] = {
						el: $facet
					};

					// populate facet select
					if (col.id && O._sets[col.field]) {
						_appUI._populateSelect($facet, O._sets[col.field]);
					}
				}
			});

			// facets clear
			var $facetsClear = $('<button class="action action-pure" data-tooltip title="Clear Filter"><i class="feather icon-x"></i></button>').appendTo(O._$facets).wrap('<div class="facets-clear"></div>');

			// set clear options of facets clear
			var facetsIds = [];
			$.each(O._facets, function (i, item) {
				facetsIds.push('#' + item.el.id);
			});
			$facetsClear.attr('data-clear', facetsIds.join(','));

			// init facets functions
			_appUI._initClear(O._$navbar);
			_appUI._initSelect2(O._$facets);

			// result counter
			O._$counter = $('<small id="mainTableCounter"></small>').appendTo($navbar).wrap('<div class="col-auto align-items-center order-6 order-xxs-4 order-lg-6 mt-2 mt-xxs-0 filter-counter">');
		}

		/**
   * SET COUNTER
   * set main table result counter
   * @param {boolean} state: false=hide, true=show
   */

	}, {
		key: '_updateCounter',
		value: function _updateCounter() {
			var O = this;

			var counterText = '';

			// filtered items?
			if (O._tableData.length - O._filtered.length > 0) {
				counterText = O._filtered.length + '/' + O._tableData.length;
			} else {
				counterText = O._filtered.length;
			}
			// append default label text
			counterText += ' models';

			// set counter text to elem
			counterText && O._$counter ? O._$counter.html(counterText) : null;
		}

		/**
   * SEARCH
   * search on enpoint for query
   * @param {string} query
   */

	}, {
		key: '_search',
		value: async function _search(query) {
			var O = this;
			_log('TABLE MAIN / _search : ' + query, 'secondary');

			O._loader._setState(true);

			// fetch result from endpoint
			var result = await _fetchData._json(window._endpoints.search, query);

			O._highlight(query);

			O._loader._setState(false);

			return result;
		}

		/**
   * HIGHLIGHT
   * highlight text matching query
   * @param {string} query
   */

	}, {
		key: '_highlight',
		value: function _highlight(query) {
			var O = this;

			if (query && query.length > 0 && query != '%20') {

				$.each(O._tableData, function (rowIndex, rowData) {
					rowData.el.find('td').each(function (j, td) {
						var value = $(td).html();
						value = _formatter._searchHighlight(value, query);

						$(td).html(value);
					});
				});
			}
		}

		/**
   * UPDATE FILTER
   * updates all filter and counter
   */

	}, {
		key: '_updateFilter',
		value: async function _updateFilter() {
			var _this18 = this;

			var O = this;
			_log('TABLE MAIN / _updateFilter');

			O._filtered = []; // stores all hidden/filtered rows

			if (O._$filter) {
				// clear on update
				// remove all highlighting
				O._$table.find('mark').contents().unwrap();

				// search 
				var searchQuery = O._$search.val() == undefined || O._$search.val() == '' ? '%20' : O._$search.val().trim().toLowerCase();
				var searchResult = [];

				if (searchQuery) {
					searchResult = await O._search(searchQuery);
				}

				// check each row for matchings
				$.each(O._tableData, function (rowIndex, rowData) {

					// initale state for each row 
					// match = true : will be shown
					var rowMatchesFilter = true;

					// filter by search
					if (searchResult.length > 0 && !searchResult.includes(rowIndex)) {
						rowMatchesFilter = false;
					}

					// filter by cols
					if (rowMatchesFilter) {
						$.each(O._facets, function (field, facet) {

							var facetValue = facet.el.val();
							if ($.isArray(facetValue) && facetValue.length > 0) {

								// get according col index
								var colIndex = _get(APPTableMT.prototype.__proto__ || Object.getPrototypeOf(APPTableMT.prototype), '_getColIndexByField', _this18).call(_this18, field);
								var cellData = rowData.cells[colIndex];

								if (cellData instanceof Set) {

									var cellMatches = false;

									$.each(facetValue, function (i, val) {
										// check set for matching one of the facet values
										cellData.has(val) ? cellMatches = true : null;
									});

									if (!cellMatches) {
										rowMatchesFilter = false;
									}
								} else {
									if (!facetValue.includes(cellData)) {
										// row data does not match
										rowMatchesFilter = false;
									}
								}
							}
						});
					}

					// _log( 'row '+ rowIndex + ' > '+ rowMatchesFilter );
					if (rowMatchesFilter && !O._filtered.includes(rowIndex)) {
						O._filtered.push(rowIndex); // row matches all filter facets citeria
					}

					// apply filter class on each row
					if (rowMatchesFilter) {
						rowData.el.removeClass('tr-hidden');
					} else {
						rowData.el.addClass('tr-hidden');
					}
				});

				O._updateStripes();

				// TO DO 
				// re-populate facets with visible sets only ?

				_log(O._filtered);

				// callback
				if ($.isFunction(O.opts.on.updateFilter)) {
					O.opts.on.updateFilter.call(O, O, O._filtered);
				}

				O._updateCounter();
			}
		}

		/**
   * GET SET
   * @param {string} name: name of the set 
   */

	}, {
		key: '_getSet',
		value: function _getSet(name) {
			var O = this;
			// _log( 'TABLE MT / _getSet' );

			if (name in O._sets) {
				return O._sets.name;
			} else {
				return O._sets;
			}
		}

		/**
   * UPDATE SET
   * @param {string} name: name of the set 
   * @param {string} data: new data 
   */

	}, {
		key: '_updateSet',
		value: function _updateSet(name, data) {
			var O = this;
			_log('TABLE MT / _updateSet');
			console.log(O._sets, name, data);
			if (name) {
				O._sets[name].add(data);
			}
		}

		/**
   * GET DATA
   * get a metadata property or return empty string if missing.
   * @param {object} modelMetadata: whole metadata of a model
   * @param {string} toplevel: name of the metadata component. it can be
   *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
   * @param {string} name: name of the metadata property 
   */

	}, {
		key: '_getData',
		value: function _getData(modelMetadata, toplevel, name) {
			var O = this;
			// _log( 'TABLE MT / _getData' );

			try {
				return modelMetadata[toplevel][name];
			} catch (err) {
				return 'no information for ' + name;
			}
		}

		/**
   * GET SCOPEDATA
   * get metadata property or return empty string if missing.
   * @param {object} modelMetadata: whole metadata of a model
   * @param {string} toplevel: name of the metadata component. It can be
   *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
   * @param {string} sublevel: Name of metadata comonent like *product*, *hazard*
   * @param {string} name: name of the metadata property 
   */

	}, {
		key: '_getScopeData',
		value: function _getScopeData(modelMetadata, toplevel, sublevel, name) {
			var O = this;
			// _log( 'TABLE MT / _getScopeData' );

			try {
				var subs = modelMetadata[toplevel][sublevel];
				names = new Set();
				subs.forEach(function (it) {
					var element = it[name];
					if (!element) element = it['name'];
					names.add(element);
				});
				return names;
			} catch (err) {
				return new Set().add('no information');
			}
		}
	}]);

	return APPTableMT;
}(APPTable);
/*

version: 1.0.0
author: sascha obermüller
date: 04.12.2020

*/

var APPUI = function () {
	function APPUI(settings) {
		_classCallCheck(this, APPUI);

		var O = this;
		O._debug = true;
		// defaults
		O._opts = $.extend(true, {}, {
			on: {
				afterInit: null
			}
		}, settings);
	}

	_createClass(APPUI, [{
		key: '_createTableHead',


		/**
   * CREATE TABLE HEAD
   * @param {array} settings
   */

		value: function _createTableHead(settings) {

			// thead
			var $thead = $('<thead></thead>');

			if (settings) {
				// thead id
				settings.id ? $thead.attr('id', settings.id) : null;

				// create th cols
				if (settings.cols) {
					$.each(settings.cols, function (i, col) {
						var $th = $('<th></th>').appendTo($thead);
						// th attributes
						col.label ? $th.html('<span>' + col.label + '</span>') : null;
						col.id ? $th.attr('id', col.id) : null; // id
						col.classes && col.classes.th ? $th.addClass(col.classes.th) : null; // classes
						col.field ? $th.attr('data-field', col.field) : null; // bs table / data-field identifier
						col.sortable ? $th.attr('data-sortable', col.sortable) : null; // bs table / data-sortable
						col.switchable ? $th.attr('data-switchable', col.switchable) : null; // bs table / data-switchable
						col.sorter ? $th.attr('data-sorter', col.sorter) : null; // bs table / data-sorter function

						// th custom attributes
						if (col.attribute && col.attributes.th) {
							$.each(col.attributes.th, function (attr, val) {
								$th.attr(attr, val);
							});
						}
					});
					$thead.wrapInner('<tr></tr>');
				}
			}
			return $thead;
		}

		/**
   * CREATE MODAL
   * @param {array} settings
   * @param {jquery selector/object} $container: append to this
   */

	}, {
		key: '_createModal',
		value: function _createModal(settings, $container) {
			var O = this;
			_log('UI / _createModal');

			return new APPModal(settings, $container);
		}

		/**
   * CREATE LOADER
   * create page loader
   * @param {array} settings
   * @param {jquery selector/object} $container: append to this
   */

	}, {
		key: '_createLoader',
		value: function _createLoader(settings, $container) {
			var O = this;
			_log('UI / _createLoader');

			var loader = {};
			loader._$el = $('<div class="loader loading"></div>').appendTo($container);
			// optional classes
			settings.classes ? loader._$el.addClass(settings.classes) : null;

			loader._setState = function (state) {
				_log('UI / loader._setState : ' + state);
				state ? loader._$el.addClass('loading') : loader._$el.removeClass('loading');
			};

			return loader;
		}

		/**
   * CREATE ALERT
   * create alert and place it into $container
   * @param {array} settings
   * @param {jquery selector/object} $container: append to this
   */

	}, {
		key: '_createAlert',
		value: function _createAlert(msg, settings, $container) {
			var O = this;
			_log('UI / _createAlert');

			var $alert = $('<div class="alert fade">' + msg + '</div>').appendTo($container);

			// alert type
			settings.type ? $alert.addClass('alert-' + settings.type) : $alert.addClass('alert-info'); // bs type: primary, secondary, success, danger, warning, info
			settings.state ? $alert.addClass(settings.state) : $alert.addClass('show'); // hide or show
			settings.classes ? $alert.addClass(settings.classes) : null;

			// dismissable
			if (settings.dismissable) {
				$alert.addClass('alert-dismissable');
				// close button
				$alert.append('<button type="button" class="close close-sm" data-dismiss="alert" aria-label="Close"><i class="feather icon-x" aria-hidden="true"></i></button>');
			}

			return $alert;
		}

		/**
   * POPULATE SELECT
   *
   * @param {element} select: dom element
   * @param {array} options: array of possible values
   */

	}, {
		key: '_populateSelect',
		value: function _populateSelect($select, options) {
			var O = this;
			if ($select) {
				options.forEach(function (entry) {
					$select.append('<option value="' + entry + '">' + entry + '</option>');
				});
			}
		}

		/**
   * POPULATE SELECT BY ID
   *
   * @param {string} selectID: element id
   * @param {array} options: array of possible values
   */

	}, {
		key: '_populateSelectByID',
		value: function _populateSelectByID(selectID, options) {
			var O = this;
			var $select = $(selectID);
			O._populateSelect($select, options);
		}

		/**
   * CREATE TOOLTIPS
   * initialize tooltips on all elements with data-attribute [data-tooltip]
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initTooltips',
		value: function _initTooltips(container) {
			var O = this;
			_log('UI / _initTooltips');

			var $elems = $(container).length > 0 ? $(container).find('[data-tooltip]') : $('[data-tooltip]');
			$elems.each(function (i, el) {

				var $el = $(el);
				// create tooltips
				$el.tooltip({
					offset: 10
				});
			});
		}

		/**
   * INIT TOGGLE TD
   * adds a collapsable container on element <td data-td-collapse>, when td's content height is higher than defined min-height
   * must be initiated before bs table init
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initTdCollapse',
		value: function _initTdCollapse($table) {
			var O = this;
			_log('UI / _initTdCollapse');

			var minH = 100;

			var $tds = $table.find('td[data-td-collapse="true"]');

			$tds.each(function (i, td) {

				var $td = $(td); // td
				$td.wrapInner('<div></div>');

				// check for content higher than min height
				if ($td.children().outerHeight() > minH) {
					// create unique id
					var collapseId = 'tdCollapse' + jQuery.now();
					// wrap inner with collapse container
					$td.wrapInner('<div id="' + collapseId + '" class="collapse td-collapse"></div>');
					// create toggle
					var $collapseToggle = $('<a href="#" class="td-collapse-toggle collapsed" data-target="#' + collapseId + '" data-toggle="collapse" aria-expanded="false" aria-controls="' + collapseId + '"></a>').appendTo($td);
					// create collapse
					$('#' + collapseId).collapse({
						toggle: false
					});
				}
			});
		}

		/**
   * INIT SELECT2
   * initialize select2 lib on element <select data-sel2 …>
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initSelect2',
		value: function _initSelect2(container) {
			var O = this;
			_log('UI / _initSelect2');

			var $elems = $(container).length > 0 ? $(container).find('select[data-sel2]') : $('select[data-sel2]');
			$elems.each(function (i, el) {

				var $el = $(el); // select

				var select2Defaults = {
					dropdownParent: $el.parent(),
					dropdownAutoWidth: false,
					// minimumResultsForSearch	: Infinity,
					width: '100%'
				};
				// check for settings by attributes
				// select size
				if (el.hasAttribute('data-sel2-size') && $el.data('sel2-size') == 'sm' || $el.hasClass('form-control-sm') || $el.hasClass('custom-select-sm')) {
					select2Defaults.selectionCssClass = 'select2-selection--sm';
					select2Defaults.dropdownCssClass = 'select2-dropdown--sm';
				}
				// check allow clear attr
				if (el.hasAttribute('data-allow-clear') && $el.data('allow-clear') == true) {
					select2Defaults.selectionCssClass += ' select2-selection--clear';
				}
				// check custom max height attr
				if (el.hasAttribute('data-sel2-max-height')) {
					select2Defaults.selectionCssClass += ' select2-selection--max-height';
				}
				// check custom choice single row
				if (el.hasAttribute('data-sel2-choice-single-row')) {
					select2Defaults.selectionCssClass += ' select2-selection--choice-single-row';
				}
				// check max selection length attr
				if (el.hasAttribute('data-maximum-selection-length') && $el.data('maximum-selection-length') == '1') {
					select2Defaults.selectionCssClass += ' select2-selection--max-sel-1';
				}
				// create select2
				$el.select2(select2Defaults);

				// $( window ).on( 'resize', function() {
				// 	$el.select2( select2Defaults );
				// } );
			});
		}

		/**
   * INIT CLEAR
   * initialize clear function
   * data-clear attribute should contain targets for clear as jquery selector
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initClear',
		value: function _initClear(container) {
			var O = this;
			_log('UI / _initClear');

			var $elems = $(container).length > 0 ? $(container).find('[data-clear]') : $('[data-clear]');
			$elems.each(function (i, el) {

				var $clear = $(el); // button or a
				// set clear targets
				$clear.targets = $clear.data('clear');

				if ($($clear.targets)) {

					$clear.state = false;

					$($clear.targets).on('change keyup', function (event) {

						$clear.state = false;
						// check all target's state
						$.each($($clear.targets), function (j, target) {
							if ($(target).val().length > 0) {
								$clear.addClass('visible');
								$clear.state = true;
							}
						});

						if ($clear.state) {
							$clear.addClass('visible');
						} else {
							$clear.removeClass('visible');
						}
					});
					// add event to clear
					$clear.on('click', function (event) {
						// iterate all targets and reset
						$.each($($clear.targets), function (j, target) {
							var $target = $(target);
							if ($target.is('select')) {
								$target.val('').trigger('change');
							} else if ($target.is('input')) {
								$target.val('').trigger('change');
							}
						});
						// hide clear
						$clear.state = false;
						$clear.removeClass('visible');
					});
				} else {
					$clear.remove();
				}
			});
		}

		/**
   * INIT TOUCHSSPIN
   * initialize touchspin lib on element <input type="text" data-touchspin …>
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initTouchspin',
		value: function _initTouchspin(container) {
			var O = this;
			_log('UI / _initTouchspin');

			var $elems = $(container).length > 0 ? $(container).find('input[data-touchspin]') : $('input[data-touchspin]');
			$elems.each(function (i, el) {

				var $el = $(el); // input

				var touchspinDefaults = {
					buttondown_class: "btn btn-outline-secondary",
					buttonup_class: "btn btn-outline-secondary",
					decimals: 0,
					initval: 0,
					mousewheel: true,
					step: 1
				};

				// check for settings by attributes
				// min & max
				if (el.hasAttribute('data-touchspin-min')) {
					touchspinDefaults.min = $el.data('touchspin-min');
				}
				if (el.hasAttribute('data-touchspin-max')) {
					touchspinDefaults.max = $el.data('touchspin-max');
				}
				// step
				if (el.hasAttribute('data-touchspin-step')) {
					touchspinDefaults.step = $el.data('touchspin-step');
				}
				// decimals
				if (el.hasAttribute('data-touchspin-decimals')) {
					touchspinDefaults.decimals = $el.data('touchspin-decimals');
				}
				// initial value
				if (el.hasAttribute('data-touchspin-initval')) {
					touchspinDefaults.initval = $el.data('touchspin-initval');
				}
				// prefix & postfix
				if (el.hasAttribute('data-touchspin-prefix')) {
					touchspinDefaults.prefix = $el.data('touchspin-prefix');
				}
				if (el.hasAttribute('data-touchspin-postfix')) {
					touchspinDefaults.postfix = $el.data('touchspin-postfix');
				}
				// create touchspin
				$el.TouchSpin(touchspinDefaults);
			});
		}

		/**
   * INIT DATEPICKER
   * initialize jquery.datepicker lib on element <input type="text" data-datepicker …>
   * @param {array} settings: setting for datepicker
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initDatepicker',
		value: function _initDatepicker(container) {
			var O = this;
			_log('UI / _initDatepicker');

			var $elems = $(container).length > 0 ? $(container).find('[data-datepicker]') : $('[data-datepicker]');
			$elems.each(function (i, el) {

				var $el = $(el); // input or input group

				// create datepicker
				$el.datepicker({
					format: {
						toDisplay: function toDisplay(date, format, language) {
							var d = new Date(date);
							var day = d.getDate();
							var month = d.getMonth();
							var year = d.getFullYear();

							return year + '-' + month + '-' + day;
						},
						toValue: function toValue(date, format, language) {
							return date;
						}
					},
					todayHighlight: true
				});
			});
		}

		/**
   * INIT RANGESLIDER
   * initialize ion.rangeslider lib on element <input type="text" data-rangeslider …>
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initRangeslider',
		value: function _initRangeslider(container) {
			var O = this;
			_log('UI / _initRangeslider');

			var $elems = $(container).length > 0 ? $(container).find('[data-rangeslider]') : $('[data-rangeslider]');
			$elems.each(function (i, el) {

				var $el = $(el); // input

				var rangesliderDefaults = {
					drag_interval: true
				};

				// check if inputs for rangeslider are set and exist as el
				// for double slider
				if (el.hasAttribute('data-type') && $el.data('type') == 'double') {
					// from input
					if (el.hasAttribute('data-control-double-from') && $($el.data('control-double-from')).length > 0) {
						$el.$inputDoubleFrom = $($el.data('control-double-from'));
						$el.$inputDoubleFrom.on('change', function (event) {
							var min = $el.data('rangeslider').result.min;
							var to = $el.data('rangeslider').result.to;
							var val = $el.$inputDoubleFrom.prop('value');
							// validate
							if (val < min) {
								val = min;
							} else if (val > to) {
								val = to;
							}

							$el.data('ionRangeSlider').update({
								from: val
							});

							$el.$inputDoubleFrom.val(val);
						});
					}
					// to input
					if (el.hasAttribute('data-control-double-to') && $($el.data('control-double-to')).length > 0) {
						$el.$inputDoubleTo = $($el.data('control-double-to'));
						$el.$inputDoubleTo.on('change', function (event) {
							var max = $el.data('rangeslider').result.max;
							var from = $el.data('rangeslider').result.from;
							var val = $el.$inputDoubleTo.prop('value');
							// validate
							if (val < from) {
								val = from;
							} else if (val > max) {
								val = max;
							}

							$el.data('rangeslider').update({
								to: val
							});

							$el.$inputDoubleTo.val(val);
						});
					}
					// if inputs for from/to, ad update routines
					if ($el.$inputDoubleFrom || $el.$inputDoubleTo) {
						rangesliderDefaults = $.merge({
							drag_interval: false,
							onStart: function onStart(data) {
								$el.updateInputs(data);
							},
							onChange: function onChange(data) {
								$el.updateInputs(data);
							},
							onFinish: function onFinish(data) {
								$el.updateInputs(data);
							}
						}, rangesliderDefaults);

						$el.updateInputs = function (data) {
							from = data.from;
							to = data.to;

							$el.$inputDoubleFrom.prop('value', from);
							$el.$inputDoubleTo.prop('value', to);
						};
					}
				}
				// for single slider
				else {
						// to do
						// from input
						if (el.hasAttribute('data-control-single') && $($el.data('control-single')).length > 0) {
							$el.$inputSingle = $($el.data('control-single'));
							$el.$inputSingle.on('change', function (event) {
								var min = $el.data('rangeslider').result.min;
								var max = $el.data('rangeslider').result.max;
								var val = $el.$inputSingle.prop('value');
								// validate
								if (val < min) {
									val = min;
								} else if (val > max) {
									val = max;
								}

								$el.data('rangeslider').update({
									from: val
								});

								$el.$inputSingle.val(val);
							});
						}
						// if inputs for from/to, ad update routines
						if ($el.$inputSingle) {
							rangesliderDefaults = $.merge({
								drag_interval: false,
								onStart: function onStart(data) {
									$el.updateInputs(data);
								},
								onChange: function onChange(data) {
									$el.updateInputs(data);
								},
								onFinish: function onFinish(data) {
									$el.updateInputs(data);
								}
							}, rangesliderDefaults);

							$el.updateInputs = function (data) {
								from = data.from;

								$el.$inputSingle.prop('value', from);
							};
						}
					}

				// create datepicker
				$el.ionRangeSlider(rangesliderDefaults);
				$el.data('rangeslider', $el.data('ionRangeSlider'));
			});
		}

		/**
   * INIT FORM VALIDATION
   * initialize validation on forms with data-atribute [data-validate]
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initFormValidation',
		value: function _initFormValidation(container) {
			var O = this;
			_log('UI / _initFormValidation');

			var $elems = $(container).length > 0 ? $(container).find('[data-validate]') : $('[data-validate]');
			$elems.each(function (i, el) {
				var $el = $(el); // form
				var $validations = $el.find('.validate-me');
				var validation = Array.prototype.filter.call($elems, function (form) {
					form.addEventListener('submit', function (event) {
						if (form.checkValidity() === false) {
							e.preventDefault();
							e.stopPropagation();
						}
						// added validation class to all form-groups in need of validation
						$validations.each(function (j, val) {
							$(val).addClass('was-validated');
						});
					}, false);
				});
			});
		}

		/**
   * INIT FORMS
   * combined initialization external lib items 
   * - touchspin
   * - select2
   * - datepicker
   * - ion rangeslider
   */

	}, {
		key: '_initFormItems',
		value: function _initFormItems(container) {
			var O = this;
			_log('UI / _initFormItems');

			O._initClear(container);
			O._initTouchspin(container);
			O._initSelect2(container);
			O._initDatepicker(container);
			O._initRangeslider(container);
		}
	}, {
		key: '_initAll',


		/**
   * INIT All
   * combined initialization of all external lib items 
   * - touchspin
   * - select2
   * - datepicker
   * - ion rangeslider
   */

		value: function _initAll() {
			var O = this;
			_log('UI / _initAll');

			O._initClear();
			O._initTouchspin();
			O._initSelect2();
			O._initDatepicker();
			O._initRangeslider();
			O._initTable();
			O._initFormValidation();
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPUI;
}();

var _appUI = _appUI || new APPUI();
/*

version: 1.0.0
author: sascha obermüller
date: 04.12.2020

*/

var APPLandingpage = function () {
	function APPLandingpage(settings, $container, metadata) {
		_classCallCheck(this, APPLandingpage);

		var O = this;
		O._metadata = metadata;
		O._$container = $container;
		O._debug = true;
		// defaults
		O._opts = $.extend(true, {}, {
			header: {
				brand: {
					logo: 'assets/img/bfr_logo.gif', // false
					title: 'FSK-Web Landing Page Test' // false or ''
				},
				nav: [{
					title: 'MenuItem',
					href: '#'
				}]
			},
			mainTable: {},
			on: {
				afterInit: null
			}
		}, settings);

		// basic init actions
		O._create();

		// callback
		if ($.isFunction(O.opts.on.afterInit)) {
			O.opts.on.afterInit.call(O);
		}
	}

	_createClass(APPLandingpage, [{
		key: '_create',


		/**
   * CREATE
   * init main app
   */

		value: async function _create() {
			var O = this;
			_log('LANDINGPAGE / _create', 'primary');

			// header
			if (O.opts.header) {
				O._$header = O._createHeader(O.opts.header).appendTo(O._$container);
			}

			// endpoints defined?
			if (_typeof(window._endpoints) != (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined))) {

				// main table settings: merge with app opts
				var mtSettings = $.extend(true, {}, {
					endpoints: window._endpoints,
					data: {
						metadata: O._metadata,
						uploadDates: O._uploadDates,
						executionTimes: O._executionTimes
					}
				}, O.opts.mainTable);
				O._mainTable = new APPTableMT(mtSettings, O._$container, O._metadata);

				// tooltips
				_appUI._initTooltips();
			} else {
				// error no endpoints
				_appUI._createAlert('Cannot create main table, no endpoints defined', {}, O._$container);
			}
		}

		/**
   * CREATE HEADER
   * @param {array} settingss
   */

	}, {
		key: '_createHeader',
		value: function _createHeader(settings) {
			var O = this;
			_log('LANDINGPAGE / _create header');

			// header
			var $header = $('<header class="page-header"></header>');

			// navbar
			$header.navBar = $('<nav class="navbar navbar-expand">').appendTo($header);

			// header brand
			if (settings.brand) {
				$header.navBrand = $('<div class="navbar-brand"></div>').appendTo($header.navBar);

				// brand logo
				if (settings.brand.logo) {
					// logo
					$('<span class="brand-logo"><img src="' + settings.brand.logo + '" alt="" /></span>').appendTo($header.navBrand);
					// brand logo + title ? add divider 
					if (settings.brand.title) {
						$('<span class="brand-divider"></span>').appendTo($header.navBrand);
					}
				}
				// brand title
				if (settings.brand.title) {
					$('<span class="brand-typo">' + settings.brand.title + '</span>').appendTo($header.navBrand);
				}
			}

			// create header nav
			if (settings.nav && settings.nav.length > 0) {

				// nav
				$header.nav = $('<ul class="navbar-nav mt-2 mt-sm-0 ml-auto"></ul>').appendTo($header.navBar);

				// toggle
				$header.navToggle = $('<button id="menuToggle" class="action action-pure action-lg" data-toggle="dropdown" aria-expanded="false" role="button"><i class="feather icon-menu"></i></button>').appendTo($header.nav).wrap('<li class="nav-item"></li>').wrap('<div class="dropdown"></div>');

				// dropdown menu
				$header.navDropdownMenu = $('<div class="dropdown-menu dropdown-menu-right" aria-labeledby="menuToggle"></div>').insertAfter($header.navToggle);

				// dropdown items by opts
				$.each(settings.nav, function (i, link) {
					// set link target or default value _self?
					link.target = link.target ? link.target : '_self';
					// add links
					var $link = $('<a href="' + link.href + '" target="' + link.target + '" class="dropdown-item">' + link.title + '</a>').appendTo($header.navDropdownMenu);
				});
			}

			return $header;
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPLandingpage;
}();
