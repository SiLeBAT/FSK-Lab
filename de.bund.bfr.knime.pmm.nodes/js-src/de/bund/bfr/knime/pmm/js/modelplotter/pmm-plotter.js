pmm_plotter = function() {
	
	/**
	 * @author Markus Freitag, EITCO GmbH, MFreitag@eitco.de, 2015
	 * 
	 * CODE CONVENTIONS
	 * Please try to avoid native JavaScript for the creation of DOM elements. 
	 * Use jQuery for the sake of clarity whenever possible. Improvements of 
	 * code readability are welcome.
	 * 
	 * - Global variables are marked with an underscore prefix ("_") or as messages ("msg")
	 * - Functions that are only used once are nested in the closest scope.
	 * - Functions are roughly ordered in the order of first usage.
	 * 
	 * GENERAL INFO
	 * The function Math.log10 as well as log10 in general is not supported by IE browsers 
	 * (same applies to the KNIME built-in browser engine). For some models, this may result in
	 * strange behavior like a missing tip on mouseover. However, all graphs could be drawn 
	 * correctly so far.
	 */
	String.prototype.replaceAll = function(search, replacement) {
	    var target = this;
	    return target.replace(new RegExp(search, 'g'), replacement);
	};
	/*if (!Math.sign) {
		  Math.sign = function(x) {
		    // If x is NaN, the result is NaN.
		    // If x is -0, the result is -0.
		    // If x is +0, the result is +0.
		    // If x is negative and not -0, the result is -1.
		    // If x is positive and not +0, the result is +1.
		    return ((x > 0) - (x < 0)) || +x;
		    // A more aesthetical persuado-representation is shown below
		    //
		    // ( (x > 0) ? 0 : 1 )  // if x is negative then negative one
		    //          +           // else (because you cant be both - and +)
		    // ( (x < 0) ? 0 : -1 ) // if x is positive then positive one
		    //         ||           // if x is 0, -0, or NaN, or not a number,
		    //         +x           // Then the result will be x, (or) if x is
		    //                      // not a number, then x converts to number
		  };
		}*/
	var modelPlotter = {
			version: "2.0.0"
	};
	modelPlotter.name = "Model Plotter";
	
	var _plotterValue;
	//	var plotterRep; // not used
	var _modelObjects = [];
	var _modelObjectsTemp = []; // for temporarily stored models like data points
	var _colorsArray = [];
	var _rawModels = [];
	var _dbUnits = [];
	var _mType;
	var xlable = "";
	var _parameterMap = [];
	var _recentPlot;
	var _COLORS = [			// copied frum function plot
	     'steelblue',
	     'red',
	     '#05b378',      // green
	     'orange',
	     '#4040e8',      // purple
	     'brown',
	     'magenta',
	     'cyan'
	];
	var msgAdd = "Add Model";
	var msgChoose = "Select Model";
	var msgTime = "Time";
	var msgNoMatrix = "no matrix data provided";
	var msgNoParameter = "no parameter provided";
	var msgNoName = "no name provided";
	var msgNoFunction = "no function provided";
	var msgNoScore = "no quality score provided";
	var msgNoLiterature = "no literature provided";
	var msgNext = "Next";
	var msg_To_ = " to ";
	var msgNoType = "No Type";
	var msgNoName = "No Name";
	var msgDone = "Done";
	var msgReportName = "Report Name";
	var msgAuthorsNames = "Authors";
	var msgComment = "Comment";
	var msgUnknown = "unknown";
	var msgIn = " in ";
	var msgName = "Name";
	var msgCatalogModel = "Catalog Model";
	var msgLiterature = "Literature";
	var msgScore = "Quality Score";
	var msgFunction = "Function";
	var msgParameter = "Initial Parameters";
	var msgMatrix = "Matrix";
	var msgExamples = "Examples";
	var msgShowData = "show given data points";
	var msg_error_noFormulaSec = "ERROR: Formula in secondary model is not a valid formula.";
	var	msg_error_unknownUnit = "unknown unit: ";
	var	msg_error_xUnitUnknown = "the x unit of one function is unknown to the database: transformation impossible";
	var	msg_error_yUnitUnknown = "the y unit of one function is unknown or has no conversion factor in the database: transformation impossible";
	var msg_error_noConversionFunctionFactor = "The PMM-Lab database does not know the unit provided by the model - model cannot be added. Unit: ";
	var msg_error_notSupported_booleanOnX = "The selected model contains boolean statements (a>b) on the x-Axis. This feature is not yet supported.";
	var msg_error_notSupported_secondaryModel = "The selected model is a secondary model. Secondary models are not yet supported. The result can be unexpected.";
	var msg_error_null = "A secondary formula contains a 'null' parameter. This cannot be parsed correctly."
	var _internalId = 0;
	var _defaultRangeValue = 13.37;
	var _xUnit = msgUnknown;
	var _yUnit = msgUnknown;
	var _ModelXName = {};
	
	/* the following values are subject to change */
	var _buttonWidth = "width: 250px;"; // not only used for buttons
	var _sliderWidth = "width: 190px;";
	var _sliderInputWidth = "width: 40px;";
	var _sliderBoxHeight = "height: 33px;";
	var _sliderStepSize = 0.0001; // aligns perfectly with the input field size
	var _totalHeight = "height: 800px;";
	var _plotWidth = 600;
	var _plotHeight = 300;
	var _defaultFadeTime = 500; // ms
	var _defaultTimeout = 200; // ms // responsiveness (lower) vs. performance/fluence (higher)
	var length;
	
	//Adding headless option so the Plotter can generate the plot without opening the view window
 	modelPlotter.init = function(representation, value,headless) {
		
		// view-bug - require js prevents global variable behavior 		
		if (!window.functionPlot) {
			if(!headless){
				window.functionPlot = parent.KnimePageLoader.getLibrary("de/bund/bfr/knime/pmm/js/modelplotter/function-plot");
			}
		}
		
		
		
		
		_plotterValue = value;
		_recentPlot = _plotterValue.svgPlot;
		
		
		if(value.models){
			_rawModels = value.models.schemas;
		}
		if(_rawModels){
			for(i = 0; i < _rawModels.length ; i++ ){
				currentOriginalModel = _rawModels[i];
				var dbuuid = currentOriginalModel.dbuuid;
				if(!isRealValue(dbuuid)){
					currentOriginalModel.dbuuid = generateGuid();
				}
			}
		}
		_dbUnits = value.units.units;
		_mType = value.modelType;
		_allModelsSelected = value.allModelsSelected;
		_onlySecondaryModel = value.onlySecondaryModel;
		//No need to generate layout if we run in headless mode
		if(!headless){
			initLayout();
		}
		initData(headless);
		//Nashorn doesn't support JQuery
		if(!headless){
			initJQuery();
		}
		
		
		/* 
		 * if all Models are Selected in the configuration of this
		 * node then all of the models will be shown without 
		 * the need to select it from the data models menu
		 */
		if(_onlySecondaryModel){
			_allModelsSelected = true;
			
		}
		if(_allModelsSelected){

			for(x = 0; x < idList.length ; x++ ){
				addFunctionFromSelection(idList[x],_onlySecondaryModel,"automatik",headless);
			}
			//No need to generate any view if we run in headless mode
			if(!headless){	
				$("#nextButton").button( "option", "disabled", false );
				$("#dataChoiceDiv").show();
				$("#graphTypeDiv").show();
			}
		}
	};
	
	/**
	 * initializes data that is necessary from the very beginning (models to select)
	 */
	function initData(headless) 
	{
		// parse models and create selection menu
		addSelectOptions(headless);
	}
	
	/**
	 * initializes all layout elements, e.g. calls jQuery methods to create jQuery 
	 * objects from the DOM elements
	 */
	function initJQuery() 
	{
		$("#dataChoiceDiv").hide();
		$("#graphTypeDiv").hide();
		
		// make buttons jquery buttons
		$("#nextButton").button({
			icons: {
				primary: "ui-icon-arrow-1-e"
			},
			disabled: true
		});
		
		$("#addModelButton").button({
			icons: {
				primary: "ui-icon-plus"
			},
			disabled: true
		}).click( function () 
			{
				// once a model is added, we can activate the "next" button
				$("#nextButton").button( "option", "disabled", false );
				$("#dataChoiceDiv").show();
				$("#graphTypeDiv").show();
			}
		);
		
		$("#dataChoiceCheckbox").click( function() {
			if(isDataPointsCheckboxChecked())
				plotDataPoints()
			else
				unplotDataPoints()
		});
		
		// make the selection a jquery select menu
		$("#modelSelectionMenu").selectmenu({
			change: function () {
				$("#addModelButton").button( "option", "disabled", false );
			}
		});
		
		// setup the div for the meta section as jQuery accordion
		$("#metaDataWrapper").accordion({
			content: "height-style",
			collapsible: true
		});
		
		$("#dataChoiceSelect").selectmenu();
	}
	
	/**
	 * initalizes and style all DOM elements, divs and placeholders
	 */
	function initLayout()
	{
		/*
		 * body
		 */
		var body = document.getElementsByTagName("body")[0];
		$('body').css({
			"width": "100%",
			"height": "100%",
			"background": "#fdfdfd",
			"font-family": "Verdana,Helvetica,sans-serif",
			"font-size": "12px",
			"overflow": "hidden"
		});
		
		/*
		 * layout
		 */
		var layoutWrapper = document.createElement("div");
		layoutWrapper.setAttribute("id", "layoutWrapper");
		layoutWrapper.setAttribute("style", "width: 1000px;overflow-y: scroll");
		body.appendChild(layoutWrapper);
		
		// left Pane
		var leftWrapper = document.createElement("div");
		leftWrapper.setAttribute("id", "leftWrapper");
		leftWrapper.setAttribute("style", "width: 300px; display: block; float: left;" + _totalHeight);
		layoutWrapper.appendChild(leftWrapper);		

		// selection
		var modelSelectionMenu = document.createElement("select");
		modelSelectionMenu.innerHTML = msgChoose;
		modelSelectionMenu.setAttribute("id", "modelSelectionMenu");
		modelSelectionMenu.setAttribute("style" , _buttonWidth);
		leftWrapper.appendChild(modelSelectionMenu);
		
		// inactive selection option serves both as a text hint for the user and a as placeholder
		var selectionPlaceholder = document.createElement("option");
		selectionPlaceholder.setAttribute("hidden", true);
		selectionPlaceholder.setAttribute("disabled", true);
		selectionPlaceholder.setAttribute("selected", true);
		selectionPlaceholder.setAttribute("value", "");
		selectionPlaceholder.innerHTML = msgChoose;
		modelSelectionMenu.appendChild(selectionPlaceholder);
		
		// add button
		var addModelButton = document.createElement("button");
		addModelButton.innerHTML = msgAdd;
		addModelButton.setAttribute("id", "addModelButton");
		addModelButton.setAttribute("style", _buttonWidth + "margin-bottom: 3px;");
		addModelButton.addEventListener("click", function() { 
				addFunctionFromSelection(); 
		});
		leftWrapper.appendChild(addModelButton);
		
		// slider wrapper
		var sliderWrapper = document.createElement("div");
		sliderWrapper.setAttribute("id", "sliderWrapper");
		sliderWrapper.setAttribute("style" , _buttonWidth);
		leftWrapper.appendChild(sliderWrapper);
		
		var nextButton = $("<button>", {
			id: "nextButton", 
			style: _buttonWidth, 
			text: msgNext 
		});
		$("#leftWrapper").append(nextButton);
		nextButton.on("click", function() 
			{ 
				$("#layoutWrapper").fadeOut(_defaultFadeTime, function() {
					showInputForm();
				});
			}
		);
		
		// right pane
		var rightWrapper = document.createElement("div");
		rightWrapper.setAttribute("id", "rightWrapper");
		rightWrapper.setAttribute("style", "width: 600px; display: block; float: left;" + _totalHeight);
		layoutWrapper.appendChild(rightWrapper);
		
		// div that includes the plotted functions
		var plotterWrapper = document.createElement("div");
		plotterWrapper.setAttribute("id", "plotterWrapper");
		rightWrapper.appendChild(plotterWrapper);
		
		// div for data choice buttons
		var dataChoiceDiv = document.createElement("div");
		dataChoiceDiv.setAttribute("style", "width: 200px; height: 40px;");
		dataChoiceDiv.setAttribute("id", "dataChoiceDiv");
		rightWrapper.appendChild(dataChoiceDiv);
		
		
		$("#dataChoiceDiv").append(
			$('<form><input type="checkbox" id="dataChoiceCheckbox" value="showTestData" checked />' + msgShowData + '</form>')
		);
	
		// meta data
		var metaDataWrapper = document.createElement("div");
		metaDataWrapper.setAttribute("id", "metaDataWrapper");
		rightWrapper.appendChild(metaDataWrapper);
	}
	
	/**
	 * chooses models to add to the selection menu and triggers adding
	 * 
	 * @param modelsArray list of models decoded by the node
	 */
	function addSelectOptions(headless)
	{	
		idList = []; // used to make sure there is only one option per global model id
		if(_rawModels)
		{
			for(i = 0; i < _rawModels.length ; i++ ){
				var dbuuid = _rawModels[i].dbuuid;

				// only add if not added before
				if(idList.indexOf(dbuuid) == -1)
				{
					var type = msgNoType;
					if(_rawModels[i].matrix && _rawModels[i].matrix.name && _rawModels[i].matrix.name != "")
						type = _rawModels[i].matrix.name;

					var modelName = msgNoName;
					if(_rawModels[i].estModel.name)
						modelName = _rawModels[i].estModel.name;
					else if (_rawModels[i].catalogModel.name)
						modelName = _rawModels[i].catalogModel.name;
						
					// pass data
					if(!headless){
						addSelectOption(dbuuid, modelName, type);
					}
					// remember id
					idList.push(dbuuid);
				}
			}
			
		
		}
	}
	
	/**
	 * adds a new option to the selection menu
	 * options of the same type are grouped (group name is type)
	 * options with no type have the "no type" group
	 * 
	 * @param dbuuid id of the model
	 * @param modelName name of the model
	 * @param type type of the model
	 */
	function addSelectOption(dbuuid, modelName, type)
	{

		
		// html <option>
		var option = document.createElement("option");
		option.setAttribute("value", dbuuid);
		option.innerHTML = "[" + dbuuid + "] " + modelName;
		
		// find or create html <optgroup>
		var groupId = "optGroup_" + type;
		var group = document.getElementById(groupId);
		if(!group) 
		{
			var group = document.createElement("optgroup");
			group.setAttribute("id", groupId);
			group.setAttribute("label", type);
			document.getElementById("modelSelectionMenu").appendChild(group);
		}
		group.appendChild(option);
	}

	/**
	 * Add-Button event in three steps:
	 * 1. determines the selected model from the selection menu
	 * 2. gets the model data
	 * 3. calls addFunctionObject() with the model data
	 */
	function addFunctionFromSelection(selectedModel,onlySecondaryModel,mode,headless)
	{	
		var manaual = !isRealValue(selectedModel)&&!isRealValue(onlySecondaryModel)&&!isRealValue(mode)
		if(manaual){
			onlySecondaryModel = _onlySecondaryModel;
			mode = "notAutomatik"
		}
		// get the selection
		if(mode != "automatik"){
			var selectMenu = document.getElementById("modelSelectionMenu");
			if(selectedModel != undefined){
				var selection = selectedModel;
			}else{
				var selection = selectMenu.options[selectMenu.selectedIndex].value;
			}
		
		}
		// get the model data
		var model;
		var modelList = [];
		
		// we do a primitive clone for the iteration over the original data 
		// (it helps to start fresh for each model)
		if(!headless){
			var rawDataClone = JSON.parse(JSON.stringify(_rawModels));
		}else{
			_rawModelsToJSONString = [];
			for(index in _rawModels){
				_rawModelsToJSONString.push(JSON.parse(_rawModels[0].toString()));
			}
			var rawDataClone =_rawModelsToJSONString;
		}
		if(mode == "automatik"){
			for(i = 0; i < rawDataClone.length ; i++ ){
				modelList.push(rawDataClone[i]);
			}
			
		}else{
			for(i = 0; i < rawDataClone.length ; i++ ){
				if(rawDataClone[i].dbuuid == selection)
				{
					modelList.push(rawDataClone[i]);
				}
			}
		}
		
		if(modelList.length >= 1)
		{
			
			if(onlySecondaryModel == false && !isRealValue(modelList[0].m2List)){
				onlySecondaryModel = true;
			}
			if(onlySecondaryModel){
				secModels  = parseModel(modelList,onlySecondaryModel)
				for(modelIndex in secModels){
					tritary = secModels[modelIndex];
					model = tritary[0]; // this has to be done first
					if(!isRealValue(model.paramList.params)){
						model.paramList.params = []
					}
					model.paramList.params.Y0 = _plotterValue.y0; // set the value from the settings here
					var dbuuid = tritary.dbuuid;
					var modelName = model.estModel.name;
					var functionAsString = prepareFunction(model.indeps, model.formula, model.xUnit, model.yUnit,onlySecondaryModel);
					var functionConstants = prepareParameters(model.indeps, dbuuid);		
					// call subsequent method
					addFunctionObject(dbuuid, functionAsString, functionConstants, model,headless);
					
				}
			}else{
				tritary  = parseModel(modelList,onlySecondaryModel)
				model = tritary[1]; // this has to be done first
				model.paramList.params.Y0 = _plotterValue.y0; // set the value from the settings here
				var dbuuid = tritary[1].dbuuid;
				var modelName = model.estModel.name;
				var functionAsString = prepareFunction(model.indeps, model.formula, model.xUnit, model.yUnit,onlySecondaryModel);
				var functionConstants = prepareParameters(model.indeps, dbuuid);		
				// call subsequent method
				addFunctionObject(dbuuid, functionAsString, functionConstants, model,headless);
			}
		}
		
		/**
		 * [nested function]
		 * parse function formula from model and modify it according to framework needs
		 * 
		 * @param functionString formula as delivered by the java class
		 * @return parsed function 
		 */
		function prepareFunction(parameterArray, functionString, xUnit, yUnit,onlySecondaryModel) {
			
			var newString = functionString;
		
			// cut the left part of the formula
			if(newString.indexOf("=") != -1)
			{
				var leftSide = newString.split("=")[0];
				newString = newString.replace(leftSide + "=", "");
			}
			
			// replace "T" and "Time" with "x" using regex
			// gi: global, case-insensitive
			//newString = newString.replace(/PH/gi, "x");

			
			if(onlySecondaryModel){
				xlable = _ModelXName[leftSide];
				newString = newString.replaceAll(_ModelXName[leftSide], "x");
				//newString = newString.replace("/\b\""+_ModelXName+"\"\b/gi", "x");
			}else{
				newString = newString.replace(/Time/gi, "x");
				newString = newString.replace(/\bT\b/gi, "x");
			}
			

			if((_xUnit != msgUnknown||!_xUnit) && xUnit != _xUnit)
			{
				newString = unifyX(newString, xUnit);
			}
			else
			{
				_xUnit = xUnit;
			}
			
			if(_yUnit != msgUnknown && _yUnit != yUnit)
			{
				newString = unifyY(newString, yUnit);
			}
			else
			{
				_yUnit = yUnit;
			}
			// math.js does not know "ln", ln equals log
			newString = newString.replace(/\bln\b/gi, "log");
			newString = newString.replace(/\blog10\b/gi, "log");
			
			/*
			 * replaces "expression^(0.5)" with "sqrt(expression)"
			 */
			newString = newString.replace(/\(([^)^()]+)\)\^\(0\.5\)/g, function(part) {
				part = part.replace("^(0.5)", "");
				part = "sqrt(" + part + ")";
				return part;
			});
			/*var Fraction = algebra.Fraction;
			var Expression = algebra.Expression;
			var Equation = algebra.Equation;
			var rightSide = newString.replaceAll("\\+-","-").replaceAll("Temperature","x").replaceAll("Time","x");
			var x1 = algebra.parse("10^x");
			var x2 = algebra.parse(_plotterValue.maxYAxis+"(x-x)");
			var eq = new Equation(x1, x2);
			var answer = eq.solveFor("x");*/
			return newString ;
		}
		
		/**
		 * [nested function]
		 * extract parameter names and values
		 * 
		 * @param parameterArray relevant parameters from the model
		 * @param modelId the id of the current model; it is used for the ranges
		 * @return reduced parameter array
		 */
		function prepareParameters(parameterArray, modelId) 
		{
			// this will be returned containing the preprocessed parameters
			var newParameterArray = {};
			for(index in parameterArray){
				param = parameterArray[index];
				var paramName = param.name;
				var paramValue;
				if (param.value != undefined)
					paramValue = param.value;
				else if(param.min != undefined)
					paramValue = param.min;
				else paramValue = 0;
				
				newParameterArray[paramName] = paramValue; 
				
				// save ranges for each parameter
				var parameterData = {
					name: paramName,
					value: paramValue,
					model: modelId,
					min: param.min,
					max: param.max
				};
				
				var existent = false;
				for(i in _parameterMap){
					existingEntry = _parameterMap[i];

					if(existingEntry.name == parameterData.name)
					{
						// set value to existing value - new model will get current values
						// override initial value
						newParameterArray[paramName] = existingEntry.value
						// override global map
						parameterData.value = existingEntry.value;
						
						
						// extend existing ranges
						if(parameterData.min < existingEntry.min)
							existingEntry.min = parameterData.min;
						if(parameterData.max > existingEntry.max)
							existingEntry.max = parameterData.max;
						existent = true;
						
						return true;
					}
				}
				if(!existent){
					_parameterMap.push(parameterData);
				}
			}
			return newParameterArray;
		}
		
		/**
		 * [nested function]
		 * use the primary and secondary models to create the tertiary model
		 * parses all nested formula and secondary parameters and injects them
		 * into the primary model (tertiary model)
		 * 
		 * @param modelList all models (data rows) that belong to the same model id
		 * @return tertiary model (if applicable)
		 */
		var formulasx = [] ;
		// get the global xUnit from the model
		function getGlobalxUnit(formulaPrim,paramsPrim,indepsPrim,depPrim,secondaryIndeps,secondaryIndepNames,tertiaryModel){
		
			if(!tertiaryModel.m2List){
				for(i in indepsPrim){
				
					var currentIndep = indepsPrim[i];	
					if(i == 0)
					{
						var xName = currentIndep["unit"];
						tertiaryModel.xUnit = xName;
						_ModelXName[formulaPrim.split("=")[0]] = currentIndep["name"]
						
					}else{
						var regex = new RegExp("\\b" + currentIndep["min"] + "\\b", "gi");
						formulaPrim = formulaPrim.replace(regex, currentIndep["value"]);
						secondaryIndeps.push(currentIndep);
					}
				}
			}
			else{
				for(i in indepsPrim){
					
					var currentIndep = indepsPrim[i];
					
					if(currentIndep["name"] == "Time" || currentIndep["name"] == "T")
					{
						var xName = currentIndep["unit"];
						tertiaryModel.xUnit = xName;
						return true;
					}
				};
				
			}
		}
		function parseSecondaryModels(currentModel,tertiaryModel,headless){
			var formulaPrim = currentModel.catalogModel.formula; // main formula is shared
			var paramsPrim = currentModel.paramList.params; // so are the primary parameters
			var indepsPrim = currentModel.indepList.indeps; // so are the indepdent parameters
			var depPrim = currentModel.dep; // so are the primary parameters
			var secondaryIndeps = []; // gather the variable independents for the sliders
			var secondaryIndepNames = []; // keep name list to distinguish them from the indeps
			getGlobalxUnit(formulaPrim,paramsPrim,indepsPrim,depPrim,secondaryIndeps,secondaryIndepNames,currentModel);
			
			
			if(depPrim)
			{
				currentModel.yUnit = depPrim.unit;
			}
			// add primary independents (which are in the parameters here)
			if(paramsPrim != null){
				for(index in paramsPrim){
					indep = paramsPrim[index];
					// convention #1: do not exchange a secondary formula dependent
					// convention #2: if a parameter has no mininmum or maximum but a value, it shall not be dynamic
					// convention #3: a parameter with "isStart"-Flag shall have a fixed value
					if( ( 
							(indep.min == undefined || indep.min == "" || indep.min == null) && 
							(indep.max == undefined || indep.max == "" || indep.max == null) &&
							(indep.value != undefined && indep.value !== "")
						) 
						||  
						(
							(indep.start == false) &&  // "isStart"-Flag means this should not be variable
							(secondaryIndepNames.indexOf(indep.name) == -1)
						) // unless its a secondaryIndep
					)
					{
						var regex = new RegExp("\\b" + indep["name"] + "\\b", "gi");
						formulaPrim = formulaPrim.replace(regex, indep["value"]);
					}
					else
					{
						// default behavior
						secondaryIndeps.push(indep);
					}
				};
			}
			
			var points = [];
			
			var timeSeries;
	
			if(currentModel.timeSeriesList)
				timeSeries = currentModel.timeSeriesList.timeSeries;
			
			if(timeSeries)
			{
				for(i2 in timeSeries){
					dataPointItem = timeSeries[i2];
					var point = [ 
						dataPointItem.time, 
						dataPointItem.concentration 
					];
					points.push(point);
				};
			}
			
			currentModel.dataPoints = points;	
			/*
			* post check
			* if you want to rename parameters consistently for all upcoming actions,
			* do it here
			*/
			for(index in secondaryIndeps){
				indep = secondaryIndeps[index];
			
				var oldName = indep.name;
				var newName = oldName;
				var category = indep.category
				
				if(category == "Temperature")
					newName = "Temperature";
				else if(category == "Time")
					newName = "Time";
				/* 
				 * special handling for indeps that are called "T" (sometimes used 
				 * for temperature), because "T" also sometimes refers to time and 
				 * will otherwise be exchanged with "x" in the formula
				 */
				else if(oldName == "T")
					newName = "T1";
				if(headless){
					newName = indep["min"];
				}
				//replace independent parameters with its value which is not done automatically if we run in headless mode
				var oldNamePattern = new RegExp("\\b" + oldName + "\\b", "gi");
				
				formulaPrim = formulaPrim.replace(oldNamePattern, newName);
				// renaming at last
				if(!headless)
					indep.name = newName;
			};
			
			currentModel.formula = formulaPrim;
			currentModel.indeps = secondaryIndeps;
			return [currentModel,tertiaryModel];
		}
		function parseTretiaryModels(modelList){
			/*
			 * we use the primary model data as a foundation for the tertiary model
			 * this applies to the attributes that are equal in all secondary models/data rows
			 */
				
			var tertiaryModel = modelList[0]; // primary model data is shared
			var formulaPrim = tertiaryModel.catalogModel.formula; // main formula is shared
			var paramsPrim = tertiaryModel.paramList.params; // so are the primary parameters
			var indepsPrim = tertiaryModel.indepList.indeps; // so are the indepdent parameters
			var depPrim = tertiaryModel.dep; // so are the primary parameters
			var secondaryIndeps = []; // gather the variable independents for the sliders
			var secondaryIndepNames = []; // keep name list to distinguish them from the indeps
			
			// get the global xUnit from the model
			getGlobalxUnit(formulaPrim,paramsPrim,indepsPrim,depPrim,secondaryIndeps,secondaryIndepNames,tertiaryModel);
			if(depPrim)
			{
				tertiaryModel.yUnit = depPrim.unit;
			}
			
			// inject nested formula in primary formula
			if(tertiaryModel.m2List){
				for(index in tertiaryModel.m2List.schemas){
					modelSec = tertiaryModel.m2List.schemas[index];
				
					var parameterPrim;
					var formulaSecRaw = modelSec.catalogModel.formula;
					paramsSec = modelSec.paramList.params;
					if(paramsSec)
						for(indexx in paramsSec){
							param = paramsSec[indexx];
							
							var regex = new RegExp("\\b" + param["name"] + "\\b", "gi");
							
						};
					if(formulaSecRaw.indexOf("=") != -1)
					{
						if(formulaSecRaw.indexOf("null") != -1) {
							show(msg_error_null);
							return false;
						}
						parameterPrim = formulaSecRaw.split("=")[0];
						secondaryIndepNames.push(parameterPrim);
					}
				};
			}
			// add primary independents (which are in the parameters here)
			for(index in paramsPrim){
				indep = paramsPrim[index];
			
				// convention #1: do not exchange a secondary formula dependent
				// convention #2: if a parameter has no mininmum or maximum but a value, it shall not be dynamic
				// convention #3: a parameter with "isStart"-Flag shall have a fixed value
				
				if( ( 
						(indep.min == undefined || indep.min == "" || indep.min == null) && 
						(indep.max == undefined || indep.max == "" || indep.max == null) &&
						(indep.value != undefined && indep.value !== "")
					) 
					||  
					(
						(indep.start == false) &&  // "isStart"-Flag means this should not be variable
						(secondaryIndepNames.indexOf(indep.name) == -1)
					) // unless its a secondaryIndep
				)
				{
					var regex = new RegExp("\\b" + indep["name"] + "\\b", "gi");
					formulaPrim = formulaPrim.replace(regex, indep["value"]);
				}
				else
				{
					// default behavior
					secondaryIndeps.push(indep);
				}
			};
			
			if(tertiaryModel.m2List)
			{
				// extract secondary independents
				for(i1 in tertiaryModel.m2List.schemas){
					modelSec = tertiaryModel.m2List.schemas[i1];
				
					var indepsSec = modelSec.indepList.indeps;
					for(i2 in indepsSec){
						indep = indepsSec[i2];
						secondaryIndeps.push(indep);
					};
				};
				
			    var paramsSec;
				// extract and replace secondary parameters (constants)
			    for(index in tertiaryModel.m2List.schemas){
					modelSec = tertiaryModel.m2List.schemas[index];
				
					// in catModelSec, a formula is expected
					var formulaSec = modelSec.catalogModel.formula;
					// in paramsSec, the values for that formula are expected
					//if(!paramsSec){
						paramsSec = modelSec.paramList.params;
					//}
					
					// we now simply replace the parameters from catModelSec with
					// the values from paramsSec
					
					if(paramsSec)
						for(indexx in paramsSec){
							param = paramsSec[indexx];
							
							var regex = new RegExp("\\b" + param["name"] + "\\b", "gi");

							formulaSec = formulaSec.replace(regex, param["value"]);
						};
					modelSec.formula = formulaSec; // new field "formula" holds the flat formula
				};
				
				// inject nested formula in primary formula
				for(index in tertiaryModel.m2List.schemas){
					modelSec = tertiaryModel.m2List.schemas[index];
				
					var formulaSecRaw = modelSec.formula; // this field is newly created before
					var formulaSec;
					var parameterPrim;  
					
					if(formulaSecRaw.indexOf("=") != -1)
					{
						if(formulaSecRaw.indexOf("null") != -1) {
							show(msg_error_null);
							return false;
						}
						// parameter name
						parameterPrim = formulaSecRaw.split("=")[0];
						// its formula from the secondary model
						formulaSec = formulaSecRaw.split("=")[1];
	
						/* 
						* we exchange the primary parameter with its formula from the secondary model
						* the parameter itself is computed depending on independents and cannot be 
						* changed directly therefore we remove it from the independents list of the 
						* tertiary model
						*/
						var indexToDelete;
						for(index in secondaryIndeps){
							indep = secondaryIndeps[index];
							 if(indep["name"] == parameterPrim)
							 {
								 indexToDelete = index;
								 break;
							 }
						};
						if(indexToDelete != undefined)
							secondaryIndeps.splice(indexToDelete, 1);
					}
					else
					{
						show(msg_error_noFormulaSec);
					}
					var regex = new RegExp("\\b" + parameterPrim + "\\b", "gi");
					formulaPrim = formulaPrim.replace(regex, "(" + formulaSec + ")");
				};
			}
			var points = [];
			
			var timeSeries;
			if(tertiaryModel.timeSeriesList)
				timeSeries = tertiaryModel.timeSeriesList.timeSeries;
			
			if(timeSeries)
			{
				for(i2 in timeSeries){
					dataPointItem = timeSeries[i2];
				
					var point = [ 
						dataPointItem.time, 
						dataPointItem.concentration 
					];
					points.push(point);
				};
			}
			
			tertiaryModel.dataPoints = points;	
			/*
			* post check
			* if you want to rename parameters consistently for all upcoming actions,
			* do it here
			*/
			for(index in secondaryIndeps){
				indep = secondaryIndeps[index];
				var oldName = indep.name;
				var newName = oldName;
				var category = indep.category
				
				if(category == "Temperature")
					newName = "Temperature";
				else if(category == "Time")
					newName = "Time";
				/* 
				 * special handling for indeps that are called "T" (sometimes used 
				 * for temperature), because "T" also sometimes refers to time and 
				 * will otherwise be exchanged with "x" in the formula
				 */
				else if(oldName == "T")
					newName = "T1";
				if(headless){
					newName = indep["min"];
				}
				
				//replace independent parameters with its value which is not done automatically if we run in headless mode
				var oldNamePattern = new RegExp("\\b" + oldName + "\\b", "gi");
				
				formulaPrim = formulaPrim.replace(oldNamePattern, newName);
				// renaming at last
				if(!headless)
					indep.name = newName;
			};
			
			tertiaryModel.formula = formulaPrim;
			tertiaryModel.indeps = secondaryIndeps;
		
			return [tertiaryModel,tertiaryModel];
		}
		function parseModel(modelList,onlySecondaryModel)
		{
			
			if(onlySecondaryModel){
				/*
				 * we use the primary model data as a foundation for the tertiary model
				 * this applies to the attributes that are equal in all secondary models/data rows
				 */
				currentModel = modelList[0];
				modelsMap = {};
				ModelsToBePlotted = [];
				if(!currentModel.m2List){	
					
					for(currentModelindex in modelList){
						currentModel = modelList[currentModelindex];
						var currentId = ""+currentModel.estModel.id;
						if(!(modelsMap.hasOwnProperty(currentId))){
							modelsMap[currentId] = currentModel;
						}
						
						for(modelIndex in modelsMap){
							ModelsToBePlotted[modelIndex] = parseSecondaryModels(modelsMap[modelIndex],currentModel,headless)
						}
					}
				}
				// Parsing secondary model 
				else{
					for(currentModelindex in currentModel.m2List.schemas){
						var currentId = ""+currentModel.m2List.schemas[currentModelindex].estModel.id;
						if(!(modelsMap.hasOwnProperty(currentId))){
							modelsMap[currentId] = currentModel.m2List.schemas[currentModelindex];
						}
					}
					for(modelIndex in modelsMap){
						ModelsToBePlotted[modelIndex] = parseSecondaryModels(modelsMap[modelIndex],currentModel,headless)
						
					}	
				}
				
				return ModelsToBePlotted;
			}
			else{		
				return parseTretiaryModels(modelList);
			}
		}
	}
	
	/**
	 * @return whether the data point checkbox is checked
	 */
	function isDataPointsCheckboxChecked(headless) {
		if(!headless){
			return $("#dataChoiceCheckbox").is(":checked");
		}
	}
	function isRealValue(obj)
	{
	 return obj && obj !== 'null' && obj !== 'undefined';
	}
	function generateGuid() {
		  function s4() {
		    return Math.floor((1 + Math.random()) * 0x10000)
		      .toString(16)
		      .substring(1);
		  }
		  return s4() + s4() ;
	}
	/**
	 * adds a function to the functions array and redraws the plot
	 * 
	 * @param dbuuid
	 * @param functionAsString the function string as returend by prepareFunction()
	 * @param the function constants as an array 
	 */
	
	function addFunctionObject(dbuuid, functionAsString, functionConstants, model,headless)
	{
	
		var color = getNextColor(); // functionPlot provides 9 colors
		var maxRange = _plotterValue.maxXAxis * 1000; // obligatoric for the range feature 
		var range = [-1000, maxRange];
		var id = ++_internalId; // no other id is unique
		if(!isRealValue(dbuuid)){
			dbuuid = generateGuid();
		}
	
		var modelObj = { 
			 id: id,
             dbuuid: dbuuid, // global model id
			 graphType: 'polyline',
			 name: model.estModel.name,
			 fn: functionAsString,	
			 rawFormula: functionAsString,	// this never changes
			 scope: functionConstants,
			 color: color,
			 range: range,
			 skipTip: false,
			 modelData: model
		};
		

		// resolve "(a<b)" parts of the formula
		resetBinaryFormulaBindings(modelObj);

		// resolve "(x<b)" parts of the formula
		resetBinaryFormulaBindingsOnX(modelObj);

		
		// for given data, we add an additional graph that only includes the data points
		if(model.dataPoints.length > 0)
		{
			var modelPointObj = {
				id: id,
				dbuuid: dbuuid,
				points: model.dataPoints,
			    color: color,
			    skipTip: false,
			    fnType: 'points',
			    graphType: 'scatter'
			};
			if(isDataPointsCheckboxChecked(headless))
				_modelObjects.push(modelPointObj);
			else
				_modelObjectsTemp.push(modelPointObj);
		}
		
		// add model to the list of used models
		_modelObjects.push(modelObj);
		
		
		if(!headless){
			// create dom elements in the meta accordion
			addMetaData(modelObj);
			// update plot and sliders after adding new function
			updateParameterSliders();
		}
		
	
		
		// redraw with all models
		drawD3Plot(headless);
	}
	
	/**
	 * deletes a model for good - including graph and meta data
	 * 
	 * @param internalId dbuuid of the model
	 */
	function deleteFunctionObject(internalId,dbuuid)
	{
		deleteMetaDataSection(internalId,dbuuid);
		removeModel(internalId,dbuuid);
		updateParameterSliders();
		
		/* 
		 * if there are no models to show left, the user cannot continue to the next 
		 * page anymore
		 */
		if(_modelObjects.length == 0)
		{
			// disable button
			$("#nextButton").button( "option", "disabled", true);
			$("#dataChoiceDiv").hide();
			$("#graphTypeDiv").hide();
			
			// reset variables
			_parameterMap = [];
			_xUnit = msgUnknown;
			_yUnit = msgUnknown;
		}
		
		drawD3Plot();
		
		/*
		 * [nested function]
		 * removes the model from the used model array
		 * 
		 * @param id dbuuid of the model
		 */
		function removeModel(id,dbuuid)
		{
			var reducedArray = [];
			$.each(_modelObjects, function (index, model) 
				{
					// if id and color equal, it is the model that is meant to be deleted
					if(model && model.id != id)
					{
						// only non-deleted models remain
						reducedArray.push(model)
					}
				}
			);
			_modelObjects = reducedArray;
		}
		
		/*
		 * [nested function]
		 * deletes the dom elements that belong to the meta data in the accordion
		 * 
		 * @param id dbuuid of the model
		 */
		function deleteMetaDataSection(id)
		{
			// remove meta data header
			var header = document.getElementById("h" + id);
			header.parentElement.removeChild(header);
			
			// remove meta data
			var data = document.getElementById(id+"functionAccordion");
			data.parentElement.removeChild(data);
			
			$("#metaDataWrapper").accordion("refresh");
			
		}
	}
	
	/**
	 * adds a new entry for a new model object and shows it in the accordion below the plot
	 * 
	 * @param modelObject the recently added modelObject
	 */
	function addMetaData(modelObject) 
	{
		
		/*
		 * Accordion needs a header followed by a div. We add a paragraph per parameter.
		 * The individual paragraph includes two divs, containing the parameter name and 
		 * value respectively.
		 * 
		 * Structure for each meta entry:
		 * > h3 (header)
		 * >> div (button>
		 * > div
		 * >> p
		 * >>> div (bold)
		 * >>> div
		 * >> p 
		 * >>> div /bold)
		 * >>> div
		 * ...
		 */
		var header = document.createElement("h3");
		header.setAttribute("id", "h" + modelObject.id);
		header.innerHTML = modelObject.dbuuid;
		
		// accordion-specific jQuery semantic for append()
		$("#metaDataWrapper").append(header);
		
		// delete button area (cross)
		var deleteDiv = document.createElement("span");
		deleteDiv.setAttribute("style", "float: right; color: transparent; background: transparent; border: transparent;")
		header.appendChild(deleteDiv);
		
		var deleteButton = document.createElement("button");
	    $(deleteButton).button({
	        icons: {
	          primary: "ui-icon-closethick"
	        },
	        text: false
	    }).click(function(event) {
	    	deleteFunctionObject(modelObject.id);
	    });
	    deleteButton.setAttribute("style", 	"color: transparent; background: transparent; border: transparent;");
		deleteDiv.appendChild(deleteButton);
		
		// color field
		var colorDiv = document.createElement("span");
		colorDiv.setAttribute("style", 
			"float: left; color: " + modelObject.color 
			+ "; background:  " + modelObject.color 
			+ "; border: 1px solid #cac3c3; margin-right: 5px; height: 10px; width: 10px; margin-top: 3px;")
		header.appendChild(colorDiv);

		// the field has to include a button instance to show a rectangle
		var colorDivSub = document.createElement("button");
	    $(colorDivSub).button({
	        icons: {
	          primary: "ui-icon-blank"
	        },
	        text: false
	    });
		colorDivSub.setAttribute("style", 
				"float: left; color: " 
				+ modelObject.color + "; background: " 
				+ modelObject.color + "; border: 0px; height: 10px; width: 10px;")
		colorDiv.appendChild(colorDivSub);
		
		// meta content divs
		var metaDiv = document.createElement("div");
		//metaDiv.setAttribute("id", modelObject.dbuuid);
		
		$("#metaDataWrapper").append(metaDiv);

		// name of the model
		addMetaParagraph(msgName, modelObject.name, msgNoName, false);
		
		// name of the underlying catalog model
		addMetaParagraph(msgCatalogModel, modelObject.modelData.catalogModel.name, msgNoName, false);
		
		// matrix data
		if(modelObject.modelData.matrix)
		{
			var matrix = modelObject.modelData.matrix;
			addMetaParagraph(msgMatrix, (matrix.name || "") + "; " + (matrix.detail || ""), msgNoMatrix, false);
		}

		// quality score
		addMetaParagraph(msgScore, modelObject.modelData.estModel.qualityScore, msgNoScore, false);
		
		var colorDivSub = document.createElement("div");
		metaDiv.setAttribute("id", modelObject.id + "functionAccordion");
	
		// model formula (function)
		
		addMetaParagraph(msgFunction,modelObject.modelData.catalogModel.formula, msgNoFunction, true);
		
		// function parameter
		addMetaParagraph(msgParameter, unfoldScope(modelObject.scope), msgNoParameter, true);

		// literature
		addMetaParagraph(msgLiterature, unfoldLiterature(modelObject.modelData.mLit.literature), msgNoLiterature, true);
		
		// ... add more paragraphs/attributes here ...
		
		// use jquery to refresh the accordion values
		$("#metaDataWrapper").accordion("refresh");
		
		var numSections = document.getElementById("metaDataWrapper").childNodes.length / 2;
		// open last index
		$("#metaDataWrapper").accordion({ active: (numSections - 1) });
		
		/**
		 * [nested function]
		 * adds a paragraph in the section for passed parameter data
		 * 
		 * @param title bold header title of the parameter (its name)
		 * @param content the value of the parameter
		 * @param alt alternative msg if parameter is null or empty
		 */
		function addMetaParagraph(title, content, alt, asAccordion) 
		{
			var header = "<div style='font-weight: bold; font-size:10px;'>" + title + "</div>";
			if(!content || content == "; ")
				var content = alt;
			var inner = "<div>" + content + "</div>";
			
			var paragraph = $("<p></p>").append(header, inner);
			$(metaDiv).append(paragraph);
			
			if(asAccordion)
				$(paragraph).accordion({
					content: "height-style",
					collapsible: true
				});
		}
		
		/**
		 * [nested function]
		 * adapt formula for readability
		 * 
		 * @param formula function formula
		 */
		function unparseFunction(formula)
		{
			newFormula = formula.replace(/\bx\b/gi, msgTime);
			newFormula = newFormula.replace(/\blog\b/gi, "ln");
			return newFormula;
		}
		
		/**
		 * [nested function]
		 * parses the parameter array and creates a DOM list from its items
		 * 
		 * @param paramArray an array of key value pairs that contains the parameters and 
		 * their respective values
		 */
		function unfoldScope(paramArray)
		{
			if(!paramArray)
				return null;
			var list = "";
			
			$.each(paramArray, function(elem) 
				{
					list += ("<li>" + elem + ": " + paramArray[elem] + "</li>");
				}
			);
			var domElement = "<ul type='square'>" + list + "</ul>";
			return domElement;
		}
		
		/**
		 * [nested function]
		 * parses the parameter array and creates a DOM list from its items
		 * 
		 * @param paramArray an array of key value pairs that contains the 
		 * 		  literature or references for the model
		 */
		function unfoldLiterature(literatureArray)
		{
			if(!literatureArray)
				return null;
			var list = "";
			
			$.each(literatureArray, function(i, lit) 
				{
					list += ("<li><b>(" + lit.year + ") " + lit.author + "</b><br />" 
							+ lit.title
							+ "</li>");
				}
			);
			
			var domElement = "<ul type='square'>" + list + "</ul>";
			return domElement;
		}
	}

    /**
     * adds, updates and removes sliders for all dynamic constants
     */
	function updateParameterSliders()
	{
	    var sliderWrapper = document.getElementById("sliderWrapper");
	    var sliderIds = []; // ids of all sliders that correspond to a constant from the used models
	    
	    // iterate over all models
	    for (var modelIndex in _modelObjects)
	    {
	    	var scopeParameters = _modelObjects[modelIndex].scope;
	    	if(scopeParameters)
	    	{
	    		// iterate over scope parameters
		    	$.each(scopeParameters, function(parameter, value)
		    	{
					var sliderId = "slider_" + parameter.toUpperCase();
					sliderIds.push(sliderId); // remember active sliders
					
					// do not recreate if already in the DOM
					if(document.getElementById(sliderId))
					{
						// do not add known parameters twice
						return true;
					}
					
					// determine slider range
					// standard values if no range given
					var sliderMin = value - _defaultRangeValue;
					var sliderMax = value + _defaultRangeValue;
					
					$.each(_parameterMap, function (index, range) {
						if(range.name == parameter)
						{
							if(range.min != undefined)
								sliderMin = range.min;
							if(range.max != undefined)
								sliderMax = range.max;
						}
					});
					
					sliderMin = roundValue(sliderMin);
					sliderMax = roundValue(sliderMax);
					
					
					/*
					 * the layout structure is as follows:
					 * > sliderBox
					 * >> sliderLabel
					 * >> slider | >> sliderValueDiv
					 * 			   >>> sliderValueInput
					 */
				    var sliderBox = document.createElement("p");
				    sliderBox.setAttribute("id", sliderId);
				    sliderBox.setAttribute("style" , _buttonWidth + _sliderBoxHeight);
				    sliderWrapper.appendChild(sliderBox);
				    
					var sliderLabel = document.createElement("div");
					var labelText = "<b>" + parameter + "</b>" + " (" + sliderMin + msg_To_ + sliderMax + ")";
					sliderLabel.innerHTML = labelText;
					sliderLabel.setAttribute("style" , "font-size: 10px;");
					sliderBox.appendChild(sliderLabel);
					
					var slider = document.createElement("div");
					slider.setAttribute("style" , _sliderWidth + "display: block; float: left; margin: 3px");
					sliderBox.appendChild(slider);
									    
					var sliderValueDiv = document.createElement("div");
					sliderValueDiv.setAttribute("style" , _sliderInputWidth + "display: block; float: left;");
					sliderBox.appendChild(sliderValueDiv);
					
					var sliderValueInput = document.createElement("input");
					sliderValueInput.setAttribute("type", "number");
					sliderValueInput.setAttribute("style" , _sliderInputWidth + "font-weight: bold;");
					sliderValueDiv.appendChild(sliderValueInput);
					
					sliderValueInput.setAttribute("min", sliderMin);
					sliderValueInput.setAttribute("max", sliderMax);
					
					// set input field to initial value
					$(sliderValueInput).val(value);
					
					// configure slider, its range and init value
				    $(slider).slider({
				    	value: value,
				    	min: sliderMin,
				    	max: sliderMax,
				    	step: _sliderStepSize,
				    	// changing the slider changes the input field
				        slide: function( event, ui ) {
				            $(sliderValueInput).val( ui.value );
				            // delay prevents excessive redrawing
				            window.setTimeout(updateFunctionParameter(parameter, ui.value), _defaultTimeout);
				        }
				    });
					$(sliderValueInput).change(function() {
						// changing the input field changes the slider
						$(slider).slider("value", this.value);
							if(this.value == undefined || this.value == "")
								return;
							// delay prevents excessive redrawing
							window.setTimeout(updateFunctionParameter(parameter, this.value), _defaultTimeout);
					});
					// react immediately on key input
					$(sliderValueInput).keyup(function() {
						$(this).change();
					});
				});
	    	}
	    }
	    
	    // at last, we delete unused sliders
	    var allIds = []; // ids of all shown sliders (may include obsolete sliders)
	    var sliderWrapperChildren = sliderWrapper.children;

	    for(var i = 0; i < sliderWrapperChildren.length; i++) 
	    {
	    	allIds.push(sliderWrapperChildren[i].id);
	    };

	    // delete obsolete sliders
	    $.each(allIds, function(i) {
	    	// check if slider is still used
	    	var found = sliderIds.indexOf(allIds[i]);
	    	// if not used, remove from DOM
	    	if(found == -1)
	    		sliderWrapper.removeChild(document.getElementById(allIds[i]));
	    });
	}

	/** 
	 * update a constant value in all functions
	 * 
	 * @param parameter parameter name
	 * @param value (new) parameter value
	 */
	function updateFunctionParameter(parameter, value)
	{
		var newValue = parseFloat(value);
		// update formula for all existing models
		for(var modelIndex in _modelObjects)
		{
			var constants = _modelObjects[modelIndex].scope;
			if(constants && constants[parameter] != undefined)
				constants[parameter] = newValue;
			resetBinaryFormulaBindings(_modelObjects[modelIndex]);
		}
		// update global map for future models
		$.each(_parameterMap, function(index, parameterEntry) {
			if(parameterEntry.name == parameter)
			{	
				parameterEntry.value = newValue;
			}
		});
		
		drawD3Plot();
	}
	
	/**
	 * This function is a workaround and parser to replace the non-existing recursion of 
	 * JS regexes. We parse the boolean operators and recreate their according terms.
	 * 
	 * As a RegEx in standard JS does neither support recursion nor references, we can only 
	 * recevie the full boolean statements in three steps:
	 * 
	 * 1. 	get all operators and their directly attached brackets.
	 * 2. 	convert results from 1. into regexes that include all 
	 * 		brackets that belong to the full statements
	 * 3. 	research the formula by using all regexes from 2.
	 */
	function resetBinaryFormulaBindings(model) {
		
		// not necessary for data points
		if(model.fnType == 'points')
			return;
		
		var scope = model.scope;
		var formula = model.rawFormula;
		// search these: \)*[<>!|&=]\(* (the operator within the statement plus the attached brackets)
		// results -> '))<(' or '))=((' etc.
		
		var booleanStatements = formula.match(/\)*[<>!|&=]+\(*/g);
		if(!booleanStatements || booleanStatements.length <= 0)
			return;
		
		// we rebuild new regexes from the statements by adding the missing brackets as regex parts
		var refilledStatements = []	
		for(index in booleanStatements){
			statement = booleanStatements[index];
			refilledStatements.push(refillStatement(statement));
		};
		var fullStatements =  [];

		// we now search again with are rebuild regexes
		for(index in refilledStatements){
			statement = refilledStatements[index];
			var statementRegEx = new RegExp(statement);
			fullStatements.push(formula.match(statementRegEx)[0]);
		};
		// fill still missing brackets
		for(index in fullStatements){
			statement = fullStatements[index];
			opening = statement.split("(").length
			closing = statement.split(")").length
			
			if(opening == closing)
				return;
			
			if(opening > closing)
			{
				for(i=0; i < (opening - closing); i++)
				{
					statement = statement + ")";
				}
				fullStatements[index] = statement;
			}
			else // (closing > opening)
			{
				for(i=0; i < (closing - opening); i++)
				{
					statement = "(" + statement;
				}
				fullStatements[index] = statement;
			}
		};
		
		
		
		formula = formula.replaceAll('&&','and')
		
		
		
		
		
		// replace old formula with parsed formula (no boolean statements)
		// the actual formula is still saved in the "rawFormula" property of the model
		model.fn = formula;
		
		/**
		 * [nested function]
		 * replace brackets from both sides with bracket regex
		 * 
		 * @param statement a boolean statement that has brackets
		 * 
		 * @return a boolean statement that serves as a regex
		 */
		function refillStatement(statement) {
			operator = statement.match(/[<>!|&=]+/g)[0];
			
			left = statement.split(operator)[0]
			right = statement.split(operator)[1]
					
			leftClosingNum = left.split(")").length - 1;
			rightOpeningNum = right.split("(").length - 1;
			
			escapedOperator = "";
			escapedStatement = "";

			for(i = 0; i < statement.length; i++)
			{
				escapedStatement = escapedStatement + "\\" + statement.charAt(i);
			}
			
			for(i = 0; i < leftClosingNum; i++)
			{
				escapedStatement = "\\([^(]*" + escapedStatement;
			}
			
			for(i = 0; i < rightOpeningNum; i++)
			{
				escapedStatement = escapedStatement + "[^)]*\\)";
			}
			
			escapedStatement = "\\([^(]*" + escapedStatement + "[^)]*\\)";
			
			return escapedStatement;
		}
	}
	
	/**
	 * [Implement the function according to the enhancement guide.]
	 * Converts a function into n+1 functions where n is the number of boolean statements.
	 * 
	 * @param model a model object
	 * 
	 */
	function resetBinaryFormulaBindingsOnX(model) {
		// var oldFormula = model.fn;
		// var formulas = []
				
		// 1. extract boolean statements
		// 2. use known parameters to replace all but x
		// 3. find thresholds for given values
		// 4. determine formula ranges by looking at the threshold values
		// 5. make n+1 formulas (n=amount of thresholds)
		// 6. limit formula ranges according to determined thresholds
		
		/* 7. add all new formulas as the formula of a model copy: 
			The first formula will be set as the formula (fn-field) of the current 
			modelObject (the "model" parameter) at the end of this method. 
			More model objects with the same ids (id, dbuuid) have to be created for 
			the other formulas. All models have to be pushed to the "_modelArray" 
			eventually.
		
			$.each(formulas, function(index, newFormula) {
				if(index!=0) { // do not push the first, it will be set in the end.
					cloneModel
					replace formula
					push to _modelArray
				}
			});
		*/		
		
		// model.fn = formulas[0]
	}

	/**
	 * redraws the plot and all graphs based on the modelObjects array and its data
	 */
	var instance;
	var xScale;
	var yScale;
	function drawD3Plot(headless) 
	{
		
		// the plot element has to be reset because otherwise functionPlot may draw artifacts
		if(!headless){
			var d3Plot = document.getElementById("d3plotter");
			if(d3Plot)
			{
				d3Plot.parentElement.removeChild(d3Plot);
			}
			d3Plot = document.createElement("div");
			d3Plot.setAttribute("id", "d3plotter");
			
			var wrapper = document.getElementById("plotterWrapper");
			wrapper.appendChild(d3Plot);
		}
		//headless mode doesn't have any DOM Object so we create basic DOM structure to be used by Plotting steps(d3,functionPlot)
		else{
			wrapper = document.getElementsByTagName("BODY")[0];
			d3Plot = document.createElement("div");
			d3Plot.setAttribute("id", "d3plotter");
			wrapper.appendChild(d3Plot);
		}
		if(!isRealValue(instance)){
			xScale = {label: (xlable!= ""?xlable:"Time") +(_xUnit != null?msgIn + _xUnit:""),domain: [_plotterValue.minXAxis, _plotterValue.maxXAxis]};
			yScale = {label: (!isRealValue(_yUnit)?_yUnit:""),domain: [_plotterValue.minYAxis, _plotterValue.maxYAxis]};
		}
		// actual plot
		try {
			instance= functionPlot({
			    target: '#d3plotter',
			    xAxis: xScale,
			    yAxis: yScale,
			    height: _plotHeight,
			    width: _plotWidth,
			    tip: 
			    {
			    	xLine: true,    // dashed line parallel to y = 0
				    yLine: true,    // dashed line parallel to x = 0
				    renderer: function (x, y, index) {
				    	return roundValue(y);
					}
				},
			    data: _modelObjects
			  
			});
			
			instance.on('zoom', function(e1,e2){
				
				window.setTimeout(serializePlot(), 0);
			})
			instance.on('mouseout', function(e1,e2){
				
				window.setTimeout(serializePlot(), 0);
			})
			if(!headless){
				window.setTimeout(serializePlot(), 0);
			}else{
				serializePlot();
				
			}
		} 
		catch(e)
		{
			show(e);
		}
	
		
		/**
		 * [nested function]
		 * Converts the svg image into a String instance. This can be used to 
		 * provide the image on a KNIME image port.
		 */
		function serializePlot() {
			var svgElement = document.getElementById("d3plotter").firstChild;
			var serializer = new XMLSerializer();
			if(svgElement != undefined && serializer != undefined)
			{
				_recentPlot = serializer.serializeToString(svgElement);
				_plotterValue.svgPlot = _recentPlot;
			}
		}
	}
	
	/**
	 * redraws the plot with data point functions
	 */
	function plotDataPoints()
	{
		$.merge(_modelObjects, _modelObjectsTemp)
		drawD3Plot();
	}
	
	/**
	 * redraws the plot without data point functions
	 */
	function unplotDataPoints()
	{
	
		var newArrayToPlot = [];
		$.each(_modelObjects, function(index, model) {
			if(model.fnType == 'points')
				_modelObjectsTemp.push(model);
			else
				newArrayToPlot.push(model);
		});
		_modelObjects = newArrayToPlot;
		drawD3Plot();
	}
	
	/**
	 * Deletes the view and opens a "second page" for the input of the user data.
	 * This function is meant to be called when the user has finished the plot.
	 */
	function showInputForm()
	{
		var svgElement = document.getElementById("d3plotter").firstChild;
		var serializer = new XMLSerializer();
		
		if(svgElement != undefined && serializer != undefined)
		{
			_recentPlot = serializer.serializeToString(svgElement);
			//_plotterValue.svgPlot = _recentPlot;
		}
		$("#layoutWrapper").empty();
		inputMember = [msgReportName, msgAuthorsNames, msgComment] // order matters
		
		var form = $("<form>", { 
			style: _buttonWidth + "; display: none;"
			
			});
		$.each(inputMember, function(i) {
			var paragraph = $("<p>", { style: _buttonWidth });
			var label = $("<div>", { 
				text: inputMember[i], 
				style: "font-weight: bold;  font-size: 10px;" + _buttonWidth 
			});
			var input = $('<input>', { 
				id: "input_" + inputMember[i].replace(/\s/g,""), 
				style: "width: 224px;" 
			})
			.button()
			.css({
			    'font' : 'inherit',
			    'background': '#eeeeee',
			    'color' : 'inherit',
			    'text-align' : 'left',
			    'outline' : 'thick',
			    'cursor' : 'text'
			});
			form.append(paragraph);
			paragraph.append(label);
			paragraph.append(input);
		})
		$(document.body).append(form);
		
		var finishButton = $("<button>", {
			id: "finishButton", 
			style: _buttonWidth + "; display: none;", 
			text: msgDone 
		}).button();
		
		$("#input_" + inputMember[0].replace(/\s/g,"")).val(_plotterValue.reportName);
		$("#input_" + inputMember[1].replace(/\s/g,"")).val(_plotterValue.authors);
		$("#input_" + inputMember[2].replace(/\s/g,"")).val(_plotterValue.comments);
		
		finishButton.on("click", function() 
			{ 
				_plotterValue.reportName = $("#input_" + inputMember[0].replace(/\s/g,"")).val();
				_plotterValue.authors = $("#input_" + inputMember[1].replace(/\s/g,"")).val();
				_plotterValue.comments = $("#input_" + inputMember[2].replace(/\s/g,"")).val();
				
				$(document.body).fadeOut(_defaultFadeTime);
			}
		);
		$(document.body).append(finishButton);
		$(form).fadeIn(_defaultFadeTime);
		$(finishButton).fadeIn(_defaultFadeTime);
		
	}
	
	/**
	 * Searches the list of DB-units for a conversion factor of a specific unit.
	 * This factor is used to normalize a function value (undo scale).
	 * 
	 * @param unit the unit from which the factor has to be determined
	 * @return the conversion factor fo the given unit
	 */
	function getUnitConversionFactor(unit)
	{
		var factor;
		$.each(_dbUnits, function (i, dbUnit) {
			if(unit == dbUnit.displayInGuiAs)
			{
				factor = dbUnit.conversionFunctionFactor;
				return true;
			}
		});
		
		if(factor == undefined)
			show(msg_error_noConversionFunctionFactor + unit);
		
		return factor;
	}
	
	/**
	 * Searches the list of DB-units for an inverse conversion factor of a specific unit.
	 * This factor is used to adapt a normalized function to a unit.
	 * 
	 * @param unit the unit from which the factor has to be determined
	 * 
	 * @return the conversion factor fo the given unit
	 */
	function getUnitInverseConversionFactor(unit)
	{
		var factor;
		$.each(_dbUnits, function (i, dbUnit) {
			if(unit == dbUnit.displayInGuiAs)
			{
				factor = dbUnit.inverseConversionFunctionFactor;
				return true;
			}
		});
		return factor;
	}
	
	/**
	 * Rearranges formula to fit to a common xAxis. We assume here, 
	 * that time is either counted in minutes ("min"), days ("d") or 
	 * hours ("h").
	 * 
	 * @param oldFunction non-unified function
	 * @param xUnit unit of the model to the oldFunction
	 * 
	 * @return unified function (String)
	 */
	function unifyX(oldFunction, xUnit)
	{
		var newFunction;
		var modifier = "*1";
		var defaultUnit = _xUnit;
		var secondUnit = xUnit;
		var defaultFactor = getUnitConversionFactor(defaultUnit).split("*")[1];
		var secondFactor = getUnitConversionFactor(secondUnit).split("*")[1];
		
		if(defaultFactor == undefined || secondFactor == undefined)
		{
			show(msg_error_xUnitUnknown);
			return oldFunction;
		}
		
		if(defaultFactor > secondFactor)
			modifier = "*" + defaultFactor / secondFactor;
		else
			modifier = "/" + secondFactor / defaultFactor;
			
		newFunction = modifyX(oldFunction, modifier);
		return newFunction;
	}
	
	/**
	 * convert x of a formula according to a common scale unit
	 * 
	 * @param unmodified function
	 * @param modifier includes operator + number that modify x
	 * 
	 * @return converted function for x in hours
	 */
	function modifyX(oldFunction, modifier)
	{
		newFunction = oldFunction.replace(/\bx\b/gi, "(x" + modifier + ")");
		return newFunction;
	}
	
	/**
	 * Rearranges formula to fit to a common yAxis. We assume here, 
	 * that the unit is either given in ln or log10.
	 * 
	 * @param oldFunction non-unified function
	 * @param yUnit unit of the model to the oldFunction
	 * 
	 * @return unified function (String)
	 */
	function unifyY(oldFunction, yUnit)
	{
		var newFunction;
		var defaultUnit = _yUnit;
		var secondUnit = yUnit;
		
		var normalizationFactor = getUnitConversionFactor(secondUnit);
		var adaptionFactor = getUnitInverseConversionFactor(defaultUnit);
		
		if(normalizationFactor == undefined || adaptionFactor == undefined)
		{
			show(msg_error_yUnitUnknown);
			return oldFunction;
		}
		
		// normalize function
		newFunction = modifyY(oldFunction, normalizationFactor);
		// apply default scale
		newFunction = modifyY(newFunction, adaptionFactor);

		return newFunction;
	}
	
	/**
	 * Modifies a function to match the current value unit
	 * 
	 * @param oldFunction non-unified function
	 * @param modifier operation to perform
	 * 
	 * @return unified function (String)
	 */
	function modifyY(oldFunction, modifier)
	{
		var newFunction = modifier.replace(/\bx\b/gi, "(" + oldFunction + ")");
		return newFunction;
	}
	
	/**
	 * color iterator based on the colors delivered by functionPlot (10 colors)
	 * 
	 * @return a color value
	 */
	function getNextColor()
	{
		if(_colorsArray.length <= 0)
			_colorsArray = _COLORS.slice(0); // clone function plot colors array
		return _colorsArray.shift();
	}
	
	/**
	 *	maintenance function: prints the content of any object
	 */
	function show(obj)
	{
		/*if(alert){
			alert(JSON.stringify(obj, null, 4));
		}*/
	}
	
	/**
	 * rounds a decimal value to at most 2 places
	 * 
	 * @param value a decimal value
	 * 
	 * @return rounded value
	 */
	function roundValue(value)
	{
		var roundedValue = Math.round((value + 0.00001) * 100) /  100;
		return roundedValue;
	}

	/*
	 * mandatory for JS
	 */
	modelPlotter.validate = function() 
	{
		return true;
	}
	
	modelPlotter.setValidationError = function () 
	{ 
		//console.log("validation error");
	}
	
	modelPlotter.getComponentValue = function() 
	{
	    return _plotterValue;
	}
	
	modelPlotter.getSVG = function()
	{
		return _recentPlot;
	}
	
	/*
	 * KNIME
	 */
	if (parent !== undefined && parent.KnimePageLoader !== undefined)
	{
		parent.KnimePageLoader.autoResize(window, frameElement.id)
	}
	
	return modelPlotter;	
}();

