/*jshint esversion: 6 */
/* global $ */
fskeditorjs = function() {
	$('head').append(
			'<meta http-equiv="X-UA-Compatible" content="IE=8,IE=edge" />');
	if (!String.prototype.startsWith) {
		String.prototype.startsWith = function(searchString, position) {
			position = position || 0;
			return this.indexOf(searchString, position) === position;
		};
	}
	window.idIndex = {}
	function bin2String(array) {
		var result = "";
		for (var i = 0; i < array.length; i++) {
			result += String.fromCharCode(parseInt(array[i], 2));
		}
		return result;
	}
	function getLastDigits(s) {
	    return s.match(/\d+$/)[0];
	}
	function getCurrentStoreInfo(){
		var currenStoreInfo;
		
			$.each($('li.active'),function(index, value){
				currentSection = $(value).find('a')[0].innerText.toLowerCase().replace(
						new RegExp(" ", 'g'),
				"");
				currenStoreInfo = window.parentStores[currentSection];
				
			})
			return currenStoreInfo;
		
	}
	if (!Array.from) {
		Array.from = (function() {
			var toStr = Object.prototype.toString;
			var isCallable = function(fn) {
				return typeof fn === 'function'
						|| toStr.call(fn) === '[object Function]';
			};
			var toInteger = function(value) {
				var number = Number(value);
				if (isNaN(number)) {
					return 0;
				}
				if (number === 0 || !isFinite(number)) {
					return number;
				}
				return (number > 0 ? 1 : -1) * Math.floor(Math.abs(number));
			};
			var maxSafeInteger = Math.pow(2, 53) - 1;
			var toLength = function(value) {
				var len = toInteger(value);
				return Math.min(Math.max(len, 0), maxSafeInteger);
			};

			// The length property of the from method is 1.
			return function from(arrayLike/* , mapFn, thisArg */) {
				// 1. Let C be the this value.
				var C = this;

				// 2. Let items be ToObject(arrayLike).
				var items = Object(arrayLike);

				// 3. ReturnIfAbrupt(items).
				if (arrayLike == null) {
					throw new TypeError(
							'Array.from requires an array-like object - not null or undefined');
				}

				// 4. If mapfn is undefined, then let mapping be false.
				var mapFn = arguments.length > 1 ? arguments[1]
						: void undefined;
				var T;
				if (typeof mapFn !== 'undefined') {
					// 5. else
					// 5. a If IsCallable(mapfn) is false, throw a TypeError
					// exception.
					if (!isCallable(mapFn)) {
						throw new TypeError(
								'Array.from: when provided, the second argument must be a function');
					}

					// 5. b. If thisArg was supplied, let T be thisArg; else let
					// T be undefined.
					if (arguments.length > 2) {
						T = arguments[2];
					}
				}

				// 10. Let lenValue be Get(items, "length").
				// 11. Let len be ToLength(lenValue).
				var len = toLength(items.length);

				// 13. If IsConstructor(C) is true, then
				// 13. a. Let A be the result of calling the [[Construct]]
				// internal method
				// of C with an argument list containing the single item len.
				// 14. a. Else, Let A be ArrayCreate(len).
				var A = isCallable(C) ? Object(new C(len)) : new Array(len);

				// 16. Let k be 0.
				var k = 0;
				// 17. Repeat, while k < len… (also steps a - h)
				var kValue;
				while (k < len) {
					kValue = items[k];
					if (mapFn) {
						A[k] = typeof T === 'undefined' ? mapFn(kValue, k)
								: mapFn.call(T, kValue, k);
					} else {
						A[k] = kValue;
					}
					k += 1;
				}
				// 18. Let putStatus be Put(A, "length", len, true).
				A.length = len;
				// 20. Return A
				;
				return A;
			};
		}());
	}
	function autocomplete(originalname, inp, arr, store, schema, uischema, fieldName,currentArea) {
		/*
		 * the autocomplete function takes two arguments, the text field element
		 * and an array of possible autocompleted values:
		 */
		var currentFocus;
		function inputHandler(e, value) {
			var a, b, i, val = this.value;
			/*
			 * close any already open lists of autocompleted values
			 */
			closeAllLists();

			currentFocus = -1;
			/*
			 * create a DIV element that will contain the items (values):
			 */
			a = document.createElement("DIV");
			a.setAttribute("id", e.target.id + "autocomplete-list");
			a.setAttribute("class", "autocomplete-items");
			/*
			 * append the DIV element as a child of the autocomplete container:
			 */

			e.target.parentNode.appendChild(a);
			/* for each item in the array... */
			if (!val) {
				for (i = 0; i < arr.length; i++) {
					/*
					 * create a DIV element for each matching element:
					 */
					b = document.createElement("DIV");
					/* make the matching letters bold: */
					b.innerHTML = "<strong>" + arr[i].substr(0, 0)
							+ "</strong>";
					b.innerHTML += arr[i].substr(0);
					/*
					 * insert a input field that will hold the current array
					 * item's value:
					 */
					b.innerHTML += "<input type='hidden' value='" + arr[i]
							+ "'>";
					/*
					 * execute a function when someone clicks on the item value
					 * (DIV element):
					 */
					b.addEventListener("click", function(e) {
						/*
						 * insert the value for the autocomplete text field:
						 */
						
						if(!fieldName.indexOf('#/properties/') == 0){
							
							store.getState().jsonforms.core.data[originalname] = e.srcElement.innerText;
							
							dispatched = store.dispatch(Actions.init(
									store.getState().jsonforms.core.data, schema,
									uischema));
							
							document.getElementById('#/properties/' + fieldName)
								.focus();
	
						}else{
							currentSection = getCurrentStoreInfo();
							currentFormId = $(e.srcElement.closest( ".demoform" )).parent().attr('id')
							// rejoinStores();
							document.getElementById( fieldName).value = e.srcElement.innerText;
							store.getState().jsonforms.core.data[currentArea][getLastDigits(fieldName)] = {'value':document.getElementById( fieldName).value};
							store.dispatch(Actions.init(
									store.getState().jsonforms.core.data, schema,
									uischema));
							
							currentSection[0].getState().jsonforms.core.data[currentFormId] = store.getState().jsonforms.core.data;
							currentSection[0].dispatch(Actions.init(
									currentSection[0].getState().jsonforms.core.data, currentSection[1],
									currentSection[2]));
							
							document.getElementById( fieldName)
								.focus();

						}

						
						/*
						 * close the list of autocompleted values, (or any other
						 * open lists of autocompleted values:
						 */
						closeAllLists();
					});
					a.appendChild(b);
				}
			} else {
				for (i = 0; i < arr.length; i++) {
					/*
					 * check if the item starts with the same letters as the
					 * text field value:
					 */
					if (arr[i].substr(0, val.length).toUpperCase() == val
							.toUpperCase()) {
						/*
						 * create a DIV element for each matching element:
						 */
						b = document.createElement("DIV");
						/* make the matching letters bold: */
						b.innerHTML = "<strong>" + arr[i].substr(0, val.length)
								+ "</strong>";
						b.innerHTML += arr[i].substr(val.length);
						/*
						 * insert a input field that will hold the current array
						 * item's value:
						 */
						b.innerHTML += "<input type='hidden' value='" + arr[i]
								+ "'>";
						/*
						 * execute a function when someone clicks on the item
						 * value (DIV element):
						 */
						b
								.addEventListener(
										"click",
										function(e) {
											/*
											 * insert the value for the
											 * autocomplete text field:
											 */

											store.getState().jsonforms.core.data[fieldName] = this
													.getElementsByTagName("input")[0].value;

											store
													.dispatch(Actions
															.init(
																	store
																			.getState().jsonforms.core.data,
																	schema,
																	uischema));
											if(document.getElementById('#/properties/' + fieldName)){
												document.getElementById('#/properties/' + fieldName)
												.focus();
											}
											/*
											 * close the list of autocompleted
											 * values, (or any other open lists
											 * of autocompleted values:
											 */
											closeAllLists();
										});
						a.appendChild(b);
					}
				}
			}
		}
		$(inp).addClass('domdsdsd');
		/* execute a function when someone writes in the text field: */
		inp.addEventListener("input", inputHandler);
		/* execute a function presses a key on the keyboard: */
		inp.addEventListener("focus", function(e) {
			var x = document.getElementById(this.id + "autocomplete-list");
			if (x)
				x = x.getElementsByTagName("div");

			if (x == null) {
				inputHandler(e);
			}
		});
		inp.addEventListener("click", function(e) {
			e.preventDefault(); // Let's stop this event.
			e.stopPropagation(); // Really this time.
			var x = document.getElementById(this.id + "autocomplete-list");
			if (x)
				x = x.getElementsByTagName("div");

			if (x == null) {
				inputHandler(e);
			}

		});
		inp.addEventListener("keydown", function(e) {

			var x = document.getElementById(this.id + "autocomplete-list");

			if (x)
				x = x.getElementsByTagName("div");
			if (e.keyCode == 40) {
				/*
				 * If the arrow DOWN key is pressed, increase the currentFocus
				 * variable:
				 */

				if (x == null) {
					inputHandler(e);
				} else {
					currentFocus++;
					addActive(x);
				}
				/* and and make the current item more visible: */

			} else if (e.keyCode == 38) { // up
				/*
				 * If the arrow UP key is pressed, decrease the currentFocus
				 * variable:
				 */
				currentFocus--;
				/* and and make the current item more visible: */
				addActive(x);
			} else if (e.keyCode == 13) {
				/*
				 * If the ENTER key is pressed, prevent the form from being
				 * submitted,
				 */
				e.preventDefault();
				if (currentFocus > -1) {
					/* and simulate a click on the "active" item: */
					if (x)
						x[currentFocus].click();
				}
			}
		});
		function addActive(x) {
			/* a function to classify an item as "active": */
			if (!x)
				return false;
			/* start by removing the "active" class on all items: */
			removeActive(x);
			if (currentFocus >= x.length)
				currentFocus = 0;
			if (currentFocus < 0)
				currentFocus = (x.length - 1);
			/* add class "autocomplete-active": */
			x[currentFocus].classList.add("autocomplete-active");
		}
		function removeActive(x) {
			/*
			 * a function to remove the "active" class from all autocomplete
			 * items:
			 */
			for (var i = 0; i < x.length; i++) {
				x[i].classList.remove("autocomplete-active");
			}
		}
		function closeAllLists(elmnt) {
			/*
			 * close all autocomplete lists in the document, except the one
			 * passed as an argument:
			 */
			var x = document.getElementsByClassName("autocomplete-items");
			for (var i = 0; i < x.length; i++) {
				if (elmnt != x[i] && elmnt != inp) {
					x[i].parentNode.removeChild(x[i]);
				}
			}
		}
		/* execute a function when someone clicks in the document: */
		document.addEventListener("click", function(e) {
			closeAllLists(e.target);
		});
	}
	var joinerNode = {
		version : '1.0.0'
	};
	joinerNode.name = 'FSK Editor JS';
	var navigationMap = {};
	var resourcesFiles = [];

	var paper;
	var _firstModel = {
		generalInformation : {},
		scope : {},
		dataBackground : {},
		modelMath : {}
	};

	var _firstModelScript;
	var _README;
	var _firstModelViz;

	var _viewValue;
	// new ParameterizedModel(inObj2.genericModel.generalInformation.name,
	// inObj2.genericModel.modelMath.parameter)
	var firstModelParameterMap = new Object();
	var secomndModelParameterMap = new Object();

	// only value is available when we run this code in knime server
	var JWT;
	var server;

	var timeStampInMs = window.performance && window.performance.now
			&& window.performance.timing
			&& window.performance.timing.navigationStart ? window.performance
			.now()
			+ window.performance.timing.navigationStart : Date.now();
	var hash = window.parent.location.hash;
	var tempSpace = ""
	if (hash) {
		sub = hash.substring(2, hash.indexOf("?"))
		tempSpace = sub.substring(0, sub.indexOf("/")) + "/temp/tempspace/";
		if (tempSpace.indexOf("-Web") < 0) {
			tempSpace = "";
		}
	}
	function traverse(obj) {
	  let keys = Object.keys(obj)
	  for (let i = 0; i < keys.length; ++i) {
	    let val = obj[keys[i]]
	    if (isObj(val)) {
	      traverse(val)
	    } else if (Array.isArray(val)) {
	      for (let j = 0; j < val.length; ++j) {
	    	  if (isObj(val[j]) && val[j] != null) {
			      traverse(val[j])
			    }else{
			    	delete obj[keys[i]];
			    }
	      }
	    } else {
	      if(val == null){
	    	  delete obj[keys[i]]
	      }
	    }
	  }
	  return  obj;
	}
	 
	function isObj(obj) {
	  if (obj == null) { return false }
	  return obj.constructor.name === "Object"
	}
	joinerNode.init = function(representation, value) {
		window.modelPrefix = (value.modelType ? value.modelType
				: "GenericModel");
		
	  
		if (parent !== undefined && parent.KnimePageLoader !== undefined) {
			// send AJAX request to acquire the JWT for the currently logged in
			// user. Subsequent requests need to carry the token in the
			// “Authorization” header
			server = window.location.protocol + "//" + window.location.host
			var xhttp = new XMLHttpRequest();

			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {

					JWT = this.responseText;

				}
			};
			xhttp.open("GET", server + "/knime/rest/session", true);
			xhttp.send();
			// create temp folder for the current running instance of the
			// worklflow on the server
			// this folder will be removed after coping all the content inside
			// to the fsk object working directory.
			var anotherxhttp = new XMLHttpRequest();

			anotherxhttp.open("put", server + "/knime/rest/v4/repository/"
					+ tempSpace + "jsEditorTempFolder" + timeStampInMs, true);
			anotherxhttp.setRequestHeader("Authorization", "Bearer" + JWT);
			anotherxhttp.send();
		}
		//console.log(value);
		if(!value.modelMetaData || value.modelMetaData == "null" ){
			
			_firstModel.generalInformation = JSON.parse("{}");
			_firstModel.generalInformation = JSON.parse("{}");
			_firstModel.scope = JSON.parse("{}");

			_firstModel.modelMath = JSON.parse("{}");
			_firstModel.dataBackground = JSON.parse("{}");
		}else{
			metaData =  JSON.parse(value.modelMetaData);
			if(metaData){
				metaData = 	traverse(metaData);
			}
			_firstModel.generalInformation = metaData.generalInformation;
			console.log(_firstModel.generalInformation);
			_firstModel.scope =metaData.scope;
	
			_firstModel.modelMath = metaData.modelMath;
			_firstModel.dataBackground = metaData.dataBackground;
			
		}
		
		_firstModelScript = value.firstModelScript;
		_README = value.readme != undefined ? value.readme : "";
		_firstModelViz = value.firstModelViz;

		_viewValue = value;

		window.generalInformation = _firstModel.generalInformation;
		window.scope = _firstModel.scope;
		window.modelMath = _firstModel.modelMath;
		window.dataBackground = _firstModel.dataBackground;

		window.objectsToFix = [window.generalInformation,
			    window.scope,
			    window.databackground,
			    window.modelmath
			  ]
		
		//prepareData(_firstModel);
		$.each(window.objectsToFix,function(storeindex,currentObjectToFix){
			if(!currentObjectToFix)
				return true ;
			$.each(Object.keys(currentObjectToFix),function (index, theValue){
				if(currentObjectToFix[theValue] == null){
				}else if(typeof currentObjectToFix[theValue] === "object"  ){
					console.log(currentObjectToFix[theValue]);
					if(Array.isArray(currentObjectToFix[theValue]) && currentObjectToFix[theValue].length  > 0  ){
						console.log(currentObjectToFix[theValue]);
						arrayOfObjects = [];
				 		$.each(currentObjectToFix[theValue],function(index,val){
				 			
				 			if(typeof val === "string"){
				 					arrayOfObjects.push({'value': val});
				 			}
				 			else{
				 				console.log("val",val);
				 				if(!val)
				 					return true ;
				 				$.each(Object.keys(val),function (inex, oneKey){
				 					console.log("inex, oneKey",inex, oneKey);
				 					if(Array.isArray(val[oneKey]) && val[oneKey].length  > 0 && (typeof val[oneKey][0] === "string") ){
				 						anotherArrayOfObjects = [];
				 						$.each(val[oneKey],function(index,stringValue){
				 							anotherArrayOfObjects.push({'value': stringValue});
				 						});
				 						val[oneKey] = anotherArrayOfObjects;
				 					}
				 				});
				 			}
				 		})
				 		if(arrayOfObjects.length > 0 ){
				 			currentObjectToFix[theValue]= arrayOfObjects
				 		}
					}
					else{
						if(!currentObjectToFix[theValue]){
							return true ;
						}
						 $.each(Object.keys(currentObjectToFix[theValue]),function(ind,key){
							 console.log(key);
						 	if(Array.isArray(currentObjectToFix[theValue][key])){
						 		arrayOfObjects = [];
						 		$.each(currentObjectToFix[theValue][key],function(index,val){
						 			console.log(val);
						 			if(typeof val === "string"){
						 				arrayOfObjects.push({'value':val});
						 			}
						 		})
						 		if(arrayOfObjects.length > 0 ){
							 		currentObjectToFix[theValue][key] = arrayOfObjects
						 		}
						 	}
						 });
					}
					
				}
					
			});
			console.log('currentObjectToFix',currentObjectToFix);
		});
		
		console.log('view Value',traverse(JSON.parse(_viewValue.modelMetaData)));
		create_body();
		fixTableHeaders();
		
		
	};
	function fixTableHeaders() {
		var tablePopups = {};
		tablePopups["modelCategory"] = window.uischema13
		tablePopups["modificationDate"] = window.uischema14
		tablePopups["creator"] = window.uischema23
		tablePopups["reference"] = window.uischema22
		tablePopups["product"] = window.uischema3
		tablePopups["hazard"] = window.uischema4
		tablePopups["studySample"] = window.uischema8
		tablePopups["populationGroup"] = window.uischema5
		tablePopups["dietaryAssessmentMethod"] = window.uischema9
		tablePopups["laboratory"] = window.uischema10
		tablePopups["parameter"] = window.uischema18
		tablePopups["assay"] = window.uischema11
		tablePopups["modelEquation"] = window.uischema19
		$(".table-responsive").each(
				function(index, val) {
					var x = $(val).parent().find(
							"[aria-describedby*='tooltip-add']");
					currentArea = window.makeId($(x).attr('aria-label'));
					if (tablePopups[currentArea]) {
						$.each($(val).find("th"), function(indexss, th) {
							thText = $(th).text()
							$.each(tablePopups[currentArea]["elements"],
									function(indexss, UIElement) {
										textInUIModel = UIElement.label
												.toLowerCase().replace(
														new RegExp(" ", 'g'),
														"");
										if (textInUIModel == thText
												.toLowerCase()) {
											$(th).text(UIElement.label)
										}

									});

						});
					}
				})

		$("[role='tooltip']").find(
				"div:contains('should be equal to one of the allowed values')")
				.css('visibility', 'hidden');

		/*
		 * $("[data='Modificationdate']") .html( 'Modification Date' );
		 */}
	function prepareData(_firstModel) {
		// prepare generalInformation
		try {
			if (_firstModel.generalInformation.creationDate === undefined) {
				_firstModel.generalInformation.creationDate = '';
			} else {
				_firstModel.generalInformation.creationDate = new Date(
						_firstModel.generalInformation.creationDate)
						.toISOString();
			}
		} catch (err) {
			// console.log(err);
		}

		_firstModel.generalInformation.description = _firstModel.generalInformation.description != null ? _firstModel.generalInformation.description
				: "";
		_firstModel.generalInformation.format = _firstModel.generalInformation.format != null ? _firstModel.generalInformation.format
				: "";
		_firstModel.generalInformation.language = _firstModel.generalInformation.language != null ? _firstModel.generalInformation.language
				: "";
		_firstModel.generalInformation.languageWrittenIn = _firstModel.generalInformation.languageWrittenIn != null ? _firstModel.generalInformation.languageWrittenIn
				: "";
		_firstModel.generalInformation.software = _firstModel.generalInformation.software != null ? _firstModel.generalInformation.software
				: "";
		_firstModel.generalInformation.source = _firstModel.generalInformation.source != null ? _firstModel.generalInformation.source
				: "";
		_firstModel.generalInformation.status = _firstModel.generalInformation.status != null ? _firstModel.generalInformation.status
				: "";
		_firstModel.generalInformation.objective = _firstModel.generalInformation.objective != null ? _firstModel.generalInformation.objective
				: "";

		_firstModel.scope.generalComment = _firstModel.scope.generalComment != null ? _firstModel.scope.generalComment
				: "";
		_firstModel.scope.temporalInformation = _firstModel.scope.temporalInformation != null ? _firstModel.scope.temporalInformation
				: "";

		_firstModel.dataBackground.study = _firstModel.dataBackground.study != null ? _firstModel.dataBackground.study
				: {};
		_firstModel.dataBackground.dietaryassessmentmethod = _firstModel.dataBackground.dietaryassessmentmethod != null ? _firstModel.dataBackground.dietaryassessmentmethod
				: [];
		_firstModel.dataBackground.laboratory = _firstModel.dataBackground.laboratory != null ? _firstModel.dataBackground.laboratory
				: [];
	}
	window.outputParameterNotDefined = false;
	window.parameterValidationError = ""
	mapOfSingleValueTable = {};
	joinerNode.validate = function() {
		/*if (noValidation == true) {
			return true;
		} else {
			if (window.firstModelScript && window.firstModelScript.save) {
				window.firstModelScript.save();
			}
			if (window.firstModelViz && window.firstModelViz.save) {
				window.firstModelViz.save();
			}
			if (window.readme && window.readme.save) {
				window.readme.save();
			}

			$
					.each(
							window.modelMath.parameter,
							function(index, param) {
								if (param.parameterClassification == "Output") {
									parameterID = param.parameterID;
									const re1 = new RegExp(parameterID
											+ "\\s*<-")
									const re2 = new RegExp(parameterID
											+ "\\s*=")

									if (_viewValue.firstModelScript.search(re1) == -1
											&& _viewValue.firstModelScript
													.search(re2) == -1) {
										window.outputParameterNotDefined = true;
									}
								} else {
									if (param.parameterDataType == "Integer"
											|| param.parameterDataType == "Double"
											|| param.parameterDataType == "Number") {
										if (isNaN(parseFloat(param.parameterValue))) {
											window.parameterValidationError += ",,,Parameter: "
													+ param.parameterID
													+ " should have a value"
										} else if (param.parameterValueMin != ""
												&& param.parameterValueMax != "") {

											if (parseFloat(param.parameterValue) < parseFloat(param.parameterValueMin)
													|| parseFloat(param.parameterValue) > parseFloat(param.parameterValueMax)) {
												window.parameterValidationError += (",,,Parameter: "
														+ param.parameterID
														+ " should be between "
														+ param.parameterValueMin
														+ " and " + param.parameterValueMax)
											}

										}
									}
								}
							})
			if (window.outputParameterNotDefined) {
				if (parent !== undefined
						&& parent.KnimePageLoader !== undefined) {
					swal(
							{
								title : "Are you sure?",
								text : "One of the output parameters or more is not declared in your Model script! Do you want to save anyway?!",
								icon : "warning",
								buttons : true,
								dangerMode : true,
							}).then(function(saveAnyway) {
						if (saveAnyway) {
							noValidation = true;
						}
					});
					return false;
				}
			}
		}*/
		console.log("validate ",_viewValue);
		
		return true;
	}
	noValidation = false

	joinerNode.displayErrors = function(error) {

	}
	function rejoinStores(){
		$.each($('.demoform'),function(index, value){
			id = $(value).parent().parent().parent().parent().attr('id')
			parentId = $(value).parent().parent().parent().parent().attr('id')
			
			if(window.parentStores[parentId] && id){
				console.log(window.parentStores[parentId]);
			}
				
			
		})
	}
	checked = false;
	joinerNode.getComponentValue = function() {
		//rejoinStores();
		if(window.store7.getState().jsonforms.core.data){
			window.store6.getState().jsonforms.core.data.study = window.store7.getState().jsonforms.core.data
		}
		if(window.store13.getState().jsonforms.core.data){
			window.store1.getState().jsonforms.core.data.modelCategory = window.store13.getState().jsonforms.core.data
		}
		if(!checked){
			checked = true;
			console.log(window.generalInformation);
			
			$.each(window.parentStores,function(storeindex,container){
				currentStore = container[0]
				$.each(Object.keys(currentStore.getState().jsonforms.core.data),function (index, theValue){
					if(typeof currentStore.getState().jsonforms.core.data[theValue] === "object"){
						console.log(currentStore.getState().jsonforms.core.data[theValue]);
						if(Array.isArray(currentStore.getState().jsonforms.core.data[theValue]) && currentStore.getState().jsonforms.core.data[theValue].length  > 0){
							console.log(currentStore.getState().jsonforms.core.data[theValue]);
							arrayOfObjects = [];
					 		$.each(currentStore.getState().jsonforms.core.data[theValue],function(index,val){
					 			console.log(index,val,typeof val);
					 			if(typeof val === "object" && Object.keys(val).length == 1 && val.value){
					 				arrayOfObjects.push(val.value);
					 			}else if(typeof val === "object") {
					 				console.log(index,val);
					 				try{
						 				$.each(Object.keys(val),function(indexx,valx){
						 					if(Array.isArray(val[valx]) && val[valx].length  > 0 && (typeof val[valx][0] === "object") ){
												console.log('val[valx]',val[valx]);
												console.log('val',val);
												anotherArrayOfObjects = [];
										 		$.each(val[valx],function(index,valxx){
										 			
										 			if(typeof valxx === "object" && valxx.value){
										 				anotherArrayOfObjects.push(valxx.value);
										 			}
										 		})
										 		if(anotherArrayOfObjects.length > 0 ){
										 			val[valx]= anotherArrayOfObjects
										 		}
											}
						 				});
					 				}catch(erro){
					 					console.log(erro);
					 				}
					 			}
					 		})
					 		if(arrayOfObjects.length > 0 ){
					 			currentStore.getState().jsonforms.core.data[theValue]= arrayOfObjects
					 		}
						}
						else{
							 $.each(Object.keys(currentStore.getState().jsonforms.core.data[theValue]),function(ind,key){
								 console.log(key);
							 	if(Array.isArray(currentStore.getState().jsonforms.core.data[theValue][key])){
							 		arrayOfObjects = [];
							 		$.each(currentStore.getState().jsonforms.core.data[theValue][key],function(index,val){
							 			console.log(val);
							 			if(typeof val === "object" && val.value){
							 				arrayOfObjects.push(val.value);
							 			}
							 		})
							 		currentStore.getState().jsonforms.core.data[theValue][key] = arrayOfObjects
							 	}
							 });
						}
						
					}	
				});
			});
			console.log(window.store1.getState().jsonforms.core.data);
			
			
		}
		function removeAdditionalProperties(schema,data){
			var ajv = new window.Ajv({
				allErrors : true,
				format : 'fast'
			});
			ajv.validate(schema,data);
			console.log(ajv.errors);
			$.each(ajv.errors,function(index,value){
				if(value.keyword == 'additionalProperties'){
					additionalProperty = value.params.additionalProperty;
					console.log(additionalProperty);
					if(value.dataPath == ""){
						console.log('delete',value);
						delete data[additionalProperty];
						
					}else{
						matches = value.dataPath.match(/\[(.*?)\]/);
						var index = matches[1];
						var holder = value.dataPath.substr(1,matches.index-1);
						console.log('delete',value);
						delete data[holder][index][additionalProperty];
					}
				}
			});
		}
		
		removeAdditionalProperties(window.schema, window.store1.getState().jsonforms.core.data);
		removeAdditionalProperties(window.schema2, window.store2.getState().jsonforms.core.data);
		removeAdditionalProperties(window.schema6, window.store6.getState().jsonforms.core.data);
		removeAdditionalProperties(window.schema17, window.store17.getState().jsonforms.core.data);
		
		modelMetaData = {			modelType: window.modelPrefix,
									generalInformation : window.store1.getState().jsonforms.core.data ,
									scope : window.store2.getState().jsonforms.core.data,
									modelMath : window.store17.getState().jsonforms.core.data,
									dataBackground : window.store6.getState().jsonforms.core.data,
									
									}; 
		_viewValue.modelMetaData = JSON
				.stringify(modelMetaData);
		console.log(_viewValue);
		if(window.firstModelScript && window.firstModelScript.save) {
		  window.firstModelScript.save(); 
		} 
		if (window.firstModelViz && window.firstModelViz.save) {
			window.firstModelViz.save();
		}
		if(window.readme && window.readme.save) {
			window.readme.save(); 
		}
		_viewValue.firstModelScript = $('#firstModelScript').val();
		 _viewValue.firstModelViz = $('#firstModelViz').val();
		 _viewValue.readme = $('#READMEArea').val();
		 
		 
		 _viewValue.resourcesFiles = resourcesFiles;
		 _viewValue.serverName = server;
		 /*_viewValue.notCompleted = "" + window.outputParameterNotDefined;
		 
		 var generalError = ""; generalError +=
		 validateAgainstSchema(window.schema, window.store1
		 .getState().jsonforms.core.data, "- General Information:");
		 generalError += validateAgainstSchema(window.schema2, window.store2
		 .getState().jsonforms.core.data, "- Scope:");
		 
		 generalError += validateAgainstSchema(window.schema17, window.store17
		 .getState().jsonforms.core.data, "- Model Math:"); generalError +=
		 validateAgainstSchema(window.schema6, window.store6
		 .getState().jsonforms.core.data, "- Data Background:"); generalError +=
		 window.parameterValidationError _viewValue.validationErrors =
		 generalError.trim(); console.log(_viewValue);*/
		 
		/*
		 * window.store1.getState().jsonforms.core.data.author = window.store23
		 * .getState().jsonforms.core.data;
		 * 
		 * 
		 * window.store6.getState().jsonforms.core.data.study = window.store7
		 * .getState().jsonforms.core.data; _viewValue.generalInformation = JSON
		 * .stringify(window.store1.getState().jsonforms.core.data);
		 * window.store2.getState().jsonforms.core.data.spatialInformation =
		 * window.toBeReplacedMap["Spatial Information"]
		 * .getState().jsonforms.core.data; _viewValue.scope = JSON
		 * .stringify(window.store2.getState().jsonforms.core.data);
		 * 
		 * window.store17.getState().jsonforms.core.data.exposure =
		 * window.toBeReplacedMap["Exposure"] .getState().jsonforms.core.data;
		 * _viewValue.modelMath = JSON
		 * .stringify(window.store17.getState().jsonforms.core.data);
		 * 
		 * _viewValue.dataBackground = JSON
		 * .stringify(window.store6.getState().jsonforms.core.data); if
		 * (window.firstModelScript && window.firstModelScript.save) {
		 * window.firstModelScript.save(); } if (window.firstModelViz &&
		 * window.firstModelViz.save) { window.firstModelViz.save(); } if
		 * (window.readme && window.readme.save) { window.readme.save(); } var
		 * ajv = new window.Ajv({ allErrors : true, format : 'fast' }); // Add
		 * convert keyword for date-time schema ajv =
		 * ajv.removeKeyword("format") try { ajv .addKeyword('format', { type :
		 * 'string', compile : function(sch, parentSchema) { return
		 * parentSchema.format === 'date-time' && sch ? function(value,
		 * objectKey, object, key) { // Update date-time string to Date //
		 * object object[key] = new Date(value); return true; } : function() {
		 * return true; } } }); ajv.validate(window.schema,
		 * window.store1.getState().jsonforms.core.data);
		 * ajv.validate(window.schema2,
		 * window.store2.getState().jsonforms.core.data);
		 * ajv.validate(window.schema17,
		 * window.store17.getState().jsonforms.core.data);
		 * ajv.validate(window.schema6,
		 * window.store6.getState().jsonforms.core.data); } catch (err) { //
		 * console.log(err) }
		 * 
		 * _viewValue.firstModelScript = $('#firstModelScript').val();
		 * _viewValue.firstModelViz = $('#firstModelViz').val();
		 * _viewValue.readme = $('#READMEArea').val();
		 * 
		 * 
		 * _viewValue.resourcesFiles = resourcesFiles; _viewValue.serverName =
		 * server; _viewValue.notCompleted = "" +
		 * window.outputParameterNotDefined;
		 * 
		 * var generalError = ""; generalError +=
		 * validateAgainstSchema(window.schema, window.store1
		 * .getState().jsonforms.core.data, "- General Information:");
		 * generalError += validateAgainstSchema(window.schema2, window.store2
		 * .getState().jsonforms.core.data, "- Scope:");
		 * 
		 * generalError += validateAgainstSchema(window.schema17, window.store17
		 * .getState().jsonforms.core.data, "- Model Math:"); generalError +=
		 * validateAgainstSchema(window.schema6, window.store6
		 * .getState().jsonforms.core.data, "- Data Background:"); generalError +=
		 * window.parameterValidationError _viewValue.validationErrors =
		 * generalError.trim(); console.log(_viewValue);
		 */
		 console.log(_viewValue);
		return _viewValue;
	};
	function validateAgainstSchema(schema, data, schemaName) {

		var ajv = new window.Ajv({
			allErrors : true,
			format : 'fast'
		});
		ajv.validate(schema, data);
		var requiredErrorText = "   Field(s): ";
		var formatErrorText = "   Field(s): ";
		for (i = 0; i < ajv.errors.length; i++) {
			theError = ajv.errors[i]
			// console.log(theError);
			if (theError.keyword == "required") {
				requiredErrorText += (requiredErrorText.length > 13 ? ", "
						: " ")
						+ theError.params.missingProperty
			} else if (theError.keyword == "format") {
				formatErrorText += (formatErrorText.length > 13 ? ", " : " ")
						+ theError.dataPath.substring(1)
			}
		}
		requiredErrorText = (requiredErrorText.length > 13 ? (schemaName
				+ ",,," + requiredErrorText + " are(is) required,,,") : " ");
		if (requiredErrorText.length == 1 && formatErrorText.length > 13) {
			formatErrorText = schemaName + ",,," + formatErrorText
					+ " have(has) wrong format,,,";
		} else if (formatErrorText.length > 13) {
			formatErrorText = formatErrorText + " have(has) wrong format,,,"
		} else {
			formatErrorText = " "
		}
		return requiredErrorText + formatErrorText;

	}
	return joinerNode;

	// --- utility functions ---

	function create_body() {
		$.ajaxSetup({
			cache : true
		});
		document.createElement("body");
		bodyContent = "<meta http-equiv='X-UA-Compatible' content='IE=edge'>"
				+ "<h3>FSK Object</h3>"
				+

				"                <div class='tabbable'>\n"
				+ "                    <ul class='nav nav-tabs'>\n"
				+ "                        <li class='active'><a href='#sub21'>General Information</a>\n"
				+ "                        </li>\n"
				+ "                        <li><a href='#sub22'>Scope</a>\n"
				+ "                        </li>\n"
				+ "                        <li><a href='#sub23'>Data Background</a>\n"
				+ "                        </li>\n"
				+ "                        <li><a href='#sub24'>Model Math</a>\n"
				+ "                        </li>\n"
				+ "                        <li ><a href='#sub25'>Model Script</a>\n"
				+ "                        </li>\n"
				+ "                        <li ><a href='#sub26'>Visualization Script</a>\n"
				+ "                        </li>\n"
				+ "                        <li ><a href='#sub27'>README</a>\n"
				+ "                        </li>\n"
		if (parent !== undefined && parent.KnimePageLoader !== undefined) {
			bodyContent += "                        <li ><a href='#sub28'>Resources</a>\n"
					+ "                        </li>\n";
		}
		bodyContent += "                    </ul>\n"
				+ "                    <div class='tab-content'>\n"
				+ "                        <div class='tab-pane fade active in' id='sub21'>\n"
				+ "                     		<div id=\"generalinformation\" class=\"App\">"
				+ "								<div class=\"demoform\">"
				+ "								</div>"
				+ "					  		</div>"
				+ "                        </div>\n"
				+ "                        <div class='tab-pane fade' id='sub22'>\n"
				+ "                    		 <div id=\"scope\" class=\"App\">"
				+ "								<div class=\"demoform\">"
				+ "								</div>"
				+ "					 		 </div>"
				+ "                        </div>\n"
				+ "                        <div class='tab-pane fade' id='sub23'>\n"
				+ "                    		 <div id=\"databackground\" class=\"App\">"
				+ "								<div class=\"demoform\">"
				+ "								</div>"
				+ "					 		 </div>"
				+ "                        </div>\n"
				+ "                        <div class='tab-pane fade' id='sub24'>\n"
				+ "                    		 <div id=\"modelMath\" class=\"App\">"
				+ "								<div class=\"demoform\">" + "								</div>"
				+ "					 		 </div>" + "                        </div>\n"
		if (parent !== undefined && parent.KnimePageLoader !== undefined) {
			bodyContent += "                        <div class='tab-pane fade' id='sub28'>\n"
					+ "                    		 <div>"
					+ "								<h4>resources</h4>"
					+ "						   		<input id='filesInput' type='file' multiple style='display:none' />"
					+ "				   				<button id='filesButton' type='button' style='border-radius: 5px; background-color: #fff; color: green;'>+ Add Files</button>"
					+ "				  				<div id='filesArea'></div>"
					+ "					 		 </div>" + "                        </div>"

		}
		bodyContent += "                        <div class='tab-pane fade' id='sub27'>\n"
				+ "                    		 <div  >"
				+ "								<h4>README</h4>"
				+ "								<textarea  id='READMEArea' name='READMEArea'>"
				+ _README
				+ "								</textarea>"
				+ "					 		 </div>"
				+ "                        </div>"

				+ "                        <div class='tab-pane fade' id='sub25'>\n"
				+ "                    		 <div  >"
				+ "								<h4>Model Script</h4>"
				+ "								<textarea id='firstModelScript' name='firstModelScript'>"
				+ _firstModelScript
				+ "								</textarea>"
				+ "					 		 </div>"
				+ "                        </div>\n"
				+ "                        <div class='tab-pane fade' id='sub26'>\n"
				+ "                    		 <div >"
				+ "								<h4>Model Visualization Script</h4>"
				+ "								<textarea id='firstModelViz' name='firstModelViz'>"
				+ _firstModelViz
				+ "								</textarea>"
				+ "					 		 </div>"
				+ "                        </div>\n"
				+ "                    </div>\n" + "                </div>\n"

		$('body').append(bodyContent);

		$(function() {
			let inputFile = $('#filesInput');
			let button = $('#filesButton');
			let buttonSubmit = $('#uploadButton');
			let filesContainer = $('#filesArea');
			let files = [];
			let fileIDMap = {}
			let fileUploadAJAXMap = {}

			inputFile
					.change(function() {
						let newFiles = [];
						for (let index = 0; index < inputFile[0].files.length; index++) {
							let file = inputFile[0].files[index];
							newFiles.push(file);
							files.push(file);
						}
						for (let index = 0; index < newFiles.length; index++) {
							let file = newFiles[index];
							var ID = function() {
								return '_'
										+ Math.random().toString(36).substr(2,
												9);
							}();
							let fileElement = $("<p>"
									+ file.name
									+ "</p><progress id='"
									+ ID
									+ "' value='0' max='100' style='width:300px;'>");

							fileElement.data('fileData', file);
							filesContainer.append(fileElement);
							fileIDMap[file.name] = ID
							fileElement.click(function(event) {
								let fileElement = $(event.target);
								let indexToRemove = files.indexOf(fileElement
										.data('fileData'));
								fileElement.remove();
								$("#" + fileIDMap[fileElement.html()]).remove()
								files.splice(indexToRemove, 1);
								fileUploadAJAXMap[fileElement.html()].abort();
							});
							fileUploadAJAXMap[file.name] = $
									.ajax({
										url : server
												+ "/knime/rest/v4/repository/"
												+ tempSpace
												+ "/jsEditorTempFolder"
												+ timeStampInMs + "/"
												+ file.name + ":data",
										xhr : function() {
											var myXhr = $.ajaxSettings.xhr();
											if (myXhr.upload) {
												myXhr.upload
														.addEventListener(
																'progress',
																function(e) {

																	if (e.lengthComputable) {
																		var max = e.total;
																		var current = e.loaded;

																		var Percentage = (current * 100)
																				/ max;

																		$(
																				"#"
																						+ fileIDMap[file.name])
																				.attr(
																						'value',
																						Percentage);

																		if (Percentage >= 100) {
																			// process
																			// completed
																		}
																	}
																}, false);
											}
											return myXhr;
										},
										headers : {
											Authorization : "Bearer" + JWT
										},
										data : file,
										type : 'put',
										success : function(data) {
											// console.log('SUCCESS !!!', data);
											resourcesFiles
													.push("knime://knime.mountpoint"
															+ data.path);
										},
										error : function(data) {
											// console.log('ERROR !!!', data);
										},
										cache : false,
										processData : false,
										contentType : false
									});
						}

					});

			button.click(function() {
				inputFile.click();
			});

			buttonSubmit.click(function() {
				let formData = new FormData();
				for (let index = 0; index < files.length; index++) {
					let file = files[index];
					formData.append('file', file);
				}

			});
		});

		$('body').append(' <div id="root"></div>');
		$('#Metadata a').on('click', function(e) {
			e.preventDefault()
			$(this).tab('show')
		})
		function fixInputCSS(source) {
			source.parent().removeAttr('class');
			source.parent().parent().find('label').removeAttr('class');
			source.parent().parent().find('label').addClass('control-labelal');
		}
		window.tableInputBootstraping = function(elements) {
			$.each(elements, function(index, value) {
				id = $(value).attr("id");
				$(value).attr("id", id + 'table');
				$(value).addClass("form-control");
			})
		}
		$(document)
				.ready(
						function() {

							// DEPENDENCY:
							// https://github.com/flatlogic/bootstrap-tabcollapse

							// if the tabs are in a narrow column in a larger
							// viewport
							$('.sidebar-tabs').tabCollapse({
								tabsClass : 'visible-tabs',
								accordionClass : 'hidden-tabs'
							});

							// if the tabs are in wide columns on larger
							// viewports
							$('.content-tabs').tabCollapse();

							// initialize tab function
							$('.nav-tabs a').click(function(e) {
								e.preventDefault();
								$(this).tab('show');

							});

							// slide to top of panel-group accordion
							$('.panel-group')
									.on(
											'shown.bs.collapse',
											function() {
												var panel = $(this).find('.in');
												$('html, body')
														.animate(
																{
																	scrollTop : panel
																			.offset().top
																			+ (-60)
																}, 500);
											});
							$('.nav-tabs a')
									.on(
											'shown.bs.tab',
											function(e) {
												// console.log($(e.currentTarget.hash).find(".active"))

												if (!$(e.currentTarget.hash)
														.find(".active")) {

													$(e.currentTarget.hash)
															.find(
																	"button[data$='General']")
															.addClass("active");
													$(e.currentTarget.hash)
															.find(
																	"div[data$='General']")
															.show();
													// console.log(e.currentTarget.hash,$(e.currentTarget.hash).find("div[data$='General']"),$(e.currentTarget.hash).find("button[data$='General']"))
												}

												var codeMirrorContainer = $(
														'#sub25').find(
														".CodeMirror")[0];
												if (codeMirrorContainer
														&& codeMirrorContainer.CodeMirror) {
													codeMirrorContainer.CodeMirror
															.refresh();

												} else {
													window.firstModelScript = window.CodeMirror
															.fromTextArea(
																	document
																			.getElementById("firstModelScript"),
																	{
																		lineNumbers : true,
																		extraKeys : {
																			"Ctrl-Space" : "autocomplete"
																		},
																		mode : {
																			name : "R"
																		}
																	});

												}
												var codeMirrorContainer = $(
														'#sub27').find(
														".CodeMirror")[0];
												if (codeMirrorContainer
														&& codeMirrorContainer.CodeMirror) {
													codeMirrorContainer.CodeMirror
															.refresh();

												} else {
													window.readme = window.CodeMirror
															.fromTextArea(
																	document
																			.getElementById("READMEArea"),
																	{

																		lineNumbers : true,
																		extraKeys : {
																			"Ctrl-Space" : "autocomplete"
																		},
																		mode : {
																			name : "htmlmixed"
																		}
																	});

												}
												var codeMirrorContainer = $(
														'#sub26').find(
														".CodeMirror")[0];
												if (codeMirrorContainer
														&& codeMirrorContainer.CodeMirror) {
													codeMirrorContainer.CodeMirror
															.refresh();

												} else {
													window.firstModelViz = window.CodeMirror
															.fromTextArea(
																	document
																			.getElementById("firstModelViz"),
																	{
																		lineNumbers : true,
																		extraKeys : {
																			"Ctrl-Space" : "autocomplete"
																		},
																		mode : {
																			name : "R"
																		}
																	});

												}

											});

						});
		try {
			createEMFForm();
		} catch (err) {
			console.log(err);
		}

		// $('html').find('style').remove();
		// data-meta MuiInputLabel
		$.each($('html').find('style'), function(key, value) {
			if ($(value).attr('data-meta') == 'MuiInput') {
				$(value).remove();
			} else if ($(value).attr('data-meta') == 'MuiInputLabel') {
				$(value).remove();
			} else if ($(value).attr('data-meta') == 'MuiFormLabel') {
				$(value).remove();
			} else if ($(value).attr('data-meta') == 'MuiFormHelperText') {
				$(value).remove();
			}

		});

		$
				.each(
						$("input[type='text']"),
						function(key, value) {

							$(value).removeAttr('class');
							$(value).addClass('form-control');
							$(value).parent().parent().removeAttr('class');
							$(value).parent().parent().addClass('form-group');
							fixInputCSS($(value));
							$(value)
									.focus(
											function() {
												if (this.value.length > 0) {
													if ($(value).width() < ((this.value.length + 1) * 12)) {
														this.style.width = ((this.value.length + 1) * 12)
																+ 'px';

													}
												}

											});
							$(value).blur(function() {
								if (this.value.length > 0) {
									this.style.width = "";
								}

							});
						});

		$('.MuiFormLabel-root-100').css('font-size', '1.5rem');
		$('.MuiDialog-paper-128').css('display', 'inline');
		$('.MuiDialog-paper-128').css('max-height', '');
		$('.MuiDialog-paper-128').css('overflow-y', 'visible');

		$(".MuiTable-root-201 thead").removeAttr('class');
		$(".MuiTable-root-201 thead tr").removeAttr('class');
		$(".MuiTable-root-201 thead tr th").removeAttr('class');
		$(".MuiTable-root-201 thead tr th th").removeAttr('class');
		$(".MuiTable-root-201 tbody").removeAttr('class');
		$(".MuiTable-root-201 tbody tr").removeAttr('class');
		$(".MuiTable-root-201 tbody tr td").removeAttr('class');
		$(".MuiTable-root-201 tbody tr td div").removeAttr('class');
		$(".MuiTable-root-201 tbody tr td div div").removeAttr('class');
		$(".MuiTable-root-201 tbody tr td div div div").removeAttr('class');

		$(".MuiTable-root-201 tbody tr td div div div input").removeAttr(
				'class');
		window
				.tableInputBootstraping($(".MuiTable-root-201 tbody tr td div div div input"));

		$('.MuiTable-root-201').addClass('table');
		$('.MuiTable-root-201').parent().addClass('table-responsive');
		$('.MuiTable-root-201').parent().removeClass('MuiGrid-typeItem-2');
		$('.MuiTable-root-201').removeClass('MuiTable-root-201');

		$('.MuiFormControl-root-90').addClass('form-group');
		$.each($('.MuiToolbar-root-135').find('h2'), function(index, value) {
			text = $(value).text();

			$(value).replaceWith(
					$('<label class="control-labelal">' + text + '</label>'));
		});

		$(
				".MuiTooltip-tooltip-201:contains('should NOT have additional properties')")
				.parent().parent().parent().remove();
		$('.replaced').parent().addClass('panel');
		$('.replaced').parent().addClass('panel-default');
		$('.replaced').addClass('panel-body');
		window.makeId = function(words) {
			var n = words.split("Add to ");
			m = n[n.length - 1].replace(/\s/g, '');
			return m[0].toLowerCase() + "" + m.substring(1);

		}
		var keepLast = "";
		
		$("[aria-describedby*='tooltip-add']")
				.click(
						function(event) {
							currentArea = window.makeId($(this).attr(
									'aria-label'));
							parentID = $($(this).closest( ".demoform" )).parent().attr('id');
							window.generalInformation = window.store1
									.getState().jsonforms.core.data;
							window.scope = window.store2.getState().jsonforms.core.data;
							window.modelMath = window.store17.getState().jsonforms.core.data;
							window.dataBackground = window.store6.getState().jsonforms.core.data;
							console
									.log("length "
											+ $('#' + currentArea).length);
							if ($('#' + currentArea).length > 0
									|| (currentArea == "reference" && keepLast != "modelEquation")) {
								event.preventDefault(); // Let's stop this
								// event.
								event.stopPropagation(); // Really this time.
								$('#title' + currentArea).text(currentArea);
								keepLast = currentArea == "modelEquation" ? "modelEquation"
										: "";
								$('#' + currentArea).modal('show');
								$('.modal-content').resizable({
								// alsoResize: ".modal-dialog",
								// minHeight: 150
								});
								$('.modal-dialog').draggable();
								$('#' + currentArea).on('show.bs.modal',
										function() {
											$(this).find('.modal-body').css({
												'max-height' : '100%'
											});
										});
								fixNotAvailible();
								window.scrollTo(0, 0);
							} else {

								var description = currentArea.replace(
										/([a-z])([A-Z])/g, '$1 $2');
								
								var element = $(
										"button[aria-label*='Add to "
												+ description.charAt(0)
														.toUpperCase()+description.substring(1,description.length) + "']")
										.parent().parent().parent().parent()
										.parent().parent().parent().parent();
								
								
								setTimeout(function(){
									$.each(element.find(".table-responsive .MuiInput-input-113"), function(index,
											value) {
										
										theID = $(value).attr('id');
										
										splited = theID.split('/')
										modifiedID = '';
										$.each(splited,function(index, value){
											if(index != splited.length -1 ){
												modifiedID += value+'/';
											}else{
												modifiedID += currentArea;
											}
										});
										$.each($('li.active'),function(index, value){
											currentSection = $(value).find('a')[0].innerText.toLowerCase().replace(
													new RegExp(" ", 'g'),
											"");
											mapOfSingleValueTable[currentArea] = [parentID,currentSection,getCurrentStoreInfo()]
										})
										if(autoCompleteCB.indexOf(currentArea) < 0){
											$(value).attr('id',"");
											//console.log('not found ', $(value));
											
											return
										}
										if(!window.idIndex[modifiedID]){
											window.idIndex[modifiedID] = 0
										}
										modifiedID = modifiedID + (window.idIndex[modifiedID]++);
										$(value).attr('id',modifiedID);
										;
										try {
											indexx = autoCompleteCB.indexOf(currentArea);
											document.getElementById(modifiedID).className = 'form-control';
											autocomplete(modelAutoCompleteCB[indexx],document.getElementById(modifiedID),
													autoCompleteArray[indexx],

													autoCompleteStores[indexx], autoCompleteSchemas[indexx],
													autoCompleteUischema[indexx], modifiedID,currentArea);

										} catch (error) {
											console.log(error);
										}
										
										
									});
								}, 1);							
							}});
		
		autoCompletInitialIDS = [ 'country', 'language', 'source', 'rights', 'format',
				'software', 'languageWrittenIn', 'modelClass', 'basicProcess',
				'status', 'name', 'unit', 'method', 'packaging', 'treatment',
				'originArea', 'originCountry', 'fisheriesArea', 'type', 'name',
				'unit', 'indSum', 'name', 'studyAssayTechnologyType',
				'accreditationProcedureForTheAssayTechnology',
				'samplingStrategy', 'typeOfSamplingProgram', 'samplingMethod',
				'lotSizeUnit', 'samplingPoint', 'collectionTool',
				'recordTypes', 'foodDescriptors', 'laboratoryCountry',
				'parameterType', 'parameterUnit', 'parameterUnitCategory',
				'parameterSource', 'parameterSubject', 'parameterDistribution',
				'modelEquationClass', 'typeOfExposure' ];				
		$.each(autoCompletInitialIDS,function(index,value){
			var ID = '#/properties/' + value;
			currentField = $("[id='"+ID+"']")
			if(currentField.length > 1){
			$.each(currentField,function(ind, val){
				parentID = $($(val).closest( ".demoform" )).parent().attr('id');
				
				splited = ID.split('/')
				modifiedID = '';
				$.each(splited,function(indexx, valuex){
					if(indexx != splited.length -1 ){
						modifiedID += valuex+'/';
					}else{
						modifiedID += parentID+valuex;
					}
				});
				$(val).attr('id',modifiedID);
				//console.log(modifiedID);
			})}
			
		});
		autoCompleteCB = [ 'creatorModelContentcountry', 'authorModelContentcountry', 'laboratoryModelContentcountry', 'language',
			'generalinformationsource','parameterModelContentsource', 'rights', 'format',
			'software', 'languageWrittenIn', 'modelClass', 'basicProcess',
			'generalinformationstatus', 'productModelContentname', 'unit', 'method', 'packaging', 'treatment',
			'originArea', 'originCountry', 'fisheriesArea', 'type', 'hazardModelContentname',
			'unit', 'indSum', 'populationGroupModelContentname', 'studyAssayTechnologyType',
			'accreditationProcedureForTheAssayTechnology',
			'samplingStrategy', 'typeOfSamplingProgram', 'samplingMethod',
			'lotSizeUnit', 'samplingPoint', 'collectionTool',
			'recordTypes', 'foodDescriptors', 'laboratoryCountry',
			'parameterType', 'parameterModelContentunit', 'unitCategory',
			'source', 'subject', 'distribution',
			'modelEquationClass', 'typeOfExposure' ];
		modelAutoCompleteCB = [ 'country','country','country', 'language', 'source','source', 'rights', 'format',
			'software', 'languageWrittenIn', 'modelClass', 'basicProcess',
			'status', 'name', 'unit', 'method', 'packaging', 'treatment',
			'originArea', 'originCountry', 'fisheriesArea', 'type', 'name',
			'unit', 'indSum', 'name', 'studyAssayTechnologyType',
			'accreditationProcedureForTheAssayTechnology',
			'samplingStrategy', 'typeOfSamplingProgram', 'samplingMethod',
			'lotSizeUnit', 'samplingPoint', 'collectionTool',
			'recordTypes', 'foodDescriptors', 'laboratoryCountry',
			'type', 'unit', 'unitCategory',
			'source', 'subject', 'distribution',
			'modelEquationClass', 'typeOfExposure' ];
	
	autoCompleteArray = [ window.Country,window.Country,window.Country, window.Language, window.Source , window.Source,
			window.Rights, window.Format, window.Software,
			window.Language_written_in, window.Model_Class,
			window.Basic_process, window.Status,
			window.Product_matrix_name, window.Parameter_unit,
			window.Method_of_production, window.Packaging,
			window.Product_treatment, window.Area_of_origin,
			window.Country, window.Fisheries_area, window.Hazard_type,
			window.Hazard_name, window.Parameter_unit,
			window.Hazard_ind_sum, window.Population_name,
			window.Study_Assay_Technology_Type,
			window.Accreditation_procedure_Ass_Tec,
			window.Sampling_strategy, window.Type_of_sampling_program,
			window.Sampling_method, window.Parameter_unit,
			window.Sampling_point, window.Method_tool_to_collect_data,
			window.Type_of_records, window.Food_descriptors,
			window.Country, window.Parameter_type, window.Parameter_unit,
			window.Parameter_unit_category, window.Parameter_source,
			window.Parameter_subject, window.Parameter_distribution,
			window.Model_equation_class_distr, window.Type_of_exposure ];
	autoCompleteStores = [ window.store24 ,window.store23,window.store10, window.store1, window.store1 , window.store18,
			window.store1, window.store1, window.store1, window.store1,
			window.store13, window.store13, window.store1, window.store3,
			window.store3, window.store3, window.store3, window.store3,
			window.store3, window.store3, window.store3, window.store4,
			window.store4, window.store4, window.store4, window.store5,
			window.store7, window.store7, window.store29, window.store29,
			window.store29, window.store29, window.store29, window.store9,
			window.store9, window.store9, window.store10, window.store18,
			window.store18, window.store18, window.store18, window.store18,
			window.store18, window.store19, window.store21 ];
	autoCompleteSchemas = [ window.schema23,window.schema23,window.schema10, window.schema, window.schema , window.schema18,
			window.schema, window.schema, window.schema, window.schema,
			window.schema13, window.schema13, window.schema,
			window.schema3, window.schema3, window.schema3, window.schema3,
			window.schema3, window.schema3, window.schema3, window.schema3,
			window.schema4, window.schema4, window.schema4, window.schema4,
			window.schema5, window.schema7, window.schema7,
			window.schema29, window.schema29, window.schema29,
			window.schema29, window.schema29, window.schema9,
			window.schema9, window.schema9, window.schema10,
			window.schema18, window.schema18, window.schema18,
			window.schema18, window.schema18, window.schema18,
			window.schema19, window.schema21 ];
	autoCompleteUischema = [ window.uischema23,window.uischema23,window.uischema10, window.uischema, window.uischema, window.uischema18,
			window.uischema, window.uischema, window.uischema,
			window.uischema, window.uischema13,
			window.uischema13, window.uischema, window.uischema3,
			window.uischema3, window.uischema3, window.uischema3,
			window.uischema3, window.uischema3, window.uischema3,
			window.uischema3, window.uischema4, window.uischema4,
			window.uischema4, window.uischema4, window.uischema5,
			window.uischema7, window.uischema7, window.uischema29,
			window.uischema29, window.uischema29, window.uischema29,
			window.uischema29, window.uischema9, window.uischema9,
			window.uischema9, window.uischema10, window.uischema18,
			window.uischema18, window.uischema18, window.uischema18,
			window.uischema18, window.uischema18, window.uischema19,
			window.uischema21 ];
		window.parentStores = {
								"generalinformation":[window.store1,window.schema,window.uischema],
							    "scope":[window.store2,window.schema2,window.uischema2],
							    "databackground":[window.store6,window.schema6,window.uischema6],
							    "modelmath":[window.store17,window.schema17,window.uischema17]
							  }
		window.autocomplete = autocomplete;
		$.each(autoCompleteCB, function(index, value) {
			var ID = '#/properties/' + value;
			try {
				autocomplete(modelAutoCompleteCB[index],document.getElementById(ID),
						autoCompleteArray[index],

						autoCompleteStores[index], autoCompleteSchemas[index],
						autoCompleteUischema[index], autoCompleteCB[index]);

			} catch (error) {
			}

		})

		$("input[type='text']").focus(function(event) {

			event.preventDefault(); // Let's stop this event.
			event.stopPropagation(); // Really this time.

			fixInputCSS($(event.target));

		});
		$("input[type='text']").click(function(event) {

			// console.log($(event.target).parent());
			var source = $(event.target);

			setTimeout(function() {
				fixInputCSS(source);
				$(".MuiButtonBase-root-156").click(function(event) {
					$.each($("input[readonly]"), function(index, dim) {

						setTimeout(function() {
							fixInputCSS($(dim));
						}, 50);
					});

				});
			}, 10);

		});

		$("input[type='text']").blur(function(event) {
			event.preventDefault(); // Let's stop this event.
			event.stopPropagation(); // Really this time.
			fixInputCSS($(event.target));

		});
		
		$(".notReplace button[aria-describedby*='tooltip-add']").off("click");
		$(".notReplace button[aria-describedby*='tooltip-add']").off("click");
		$("div[role*='tooltip']:contains('should match format')").parent()
				.parent().remove();

		$("div[role*='tooltip']").click(function(event) {

			currentArea = window.makeId($(this).attr('aria-label'));

		});
		reDesign("generalinformation");
		reDesign("scope");
		reDesign("databackground");
		reDesign("modelMath");
		$(document)
				.ready(
						function() {
							// Helper function to keep table row from collapsing
							// when being sorted
							table = $($(
									$(".control-labelal:contains('Parameter')")[15])
									.parent().parent().parent().parent()
									.parent().find('.table-responsive')[0])
							table.attr("id", "Parametertable")
							var fixHelperModified = function(e, tr) {
								var $originals = tr.children();
								var $helper = tr.clone();
								$helper.children().each(function(index) {
									$(this).width($originals.eq(index).width())
								});
								return $helper;
							}, updateIndex = function(e, ui) {
								$('td.index', ui.item.parent()).each(
										function(i) {
											$(this).html(i + 1);
										});
							}

							// Make Parameter table sortable
							$(table.find('tbody')[0])
									.sortable(
											{
												helper : fixHelperModified,
												stop : function(event, ui) {
													updateIndex(event, ui);
													newParameterList = [];
													table
															.find('tr')
															.each(
																	function(
																			index,
																			value) {
																		if (index > 0) {
																			$(
																					window.store17
																							.getState().jsonforms.core.data.parameter)
																					.each(
																							function(
																									index,
																									valuex) {
																								console
																										.log(
																												"valuex",
																												valuex.parameterID,
																												$(
																														value)
																														.find(
																																"input[id='#/properties/parameterIDtable']")
																														.first()
																														.val())
																								if (valuex.parameterID == $(
																										value)
																										.find(
																												"input[id='#/properties/parameterIDtable']")
																										.first()
																										.val()) {
																									newParameterList
																											.push(valuex);
																								}
																							});
																			console
																					.log($(
																							value)
																							.find(
																									"input[id='#/properties/parameterIDtable']")
																							.first()
																							.val());
																		}
																	});
													window.store17.getState().jsonforms.core.data.parameter = newParameterList;
												}
											}).disableSelection();

						});

	}
	function fixNotAvailible(){

		notAProperDiv = $("div:contains('No applicable'):not(:has(div))");

		$.each(notAProperDiv, function( index, value ) {
			
			var parentxc ;
			var areaName;
			try{
					
					parentxc = value.parentNode;
					areaName =  parentxc.firstChild.textContent;
					console.log("No applicable",value);
					if(parentxc.firstChild.textContent.indexOf('*') >= 0){
						areaName = areaName.slice(0,-1);
					}
					if(areaName == 'Model Category'){
						areaName = "modelCategory";
					}else if(areaName == 'Modification Date'){
						areaName = "modificationDate";
					}
					if(areaName.indexOf('No applicable field found') < 0){
						$(value).remove();
						if(areaName !='Exposure' ){
							$(parentxc).append( "<div id ='"+areaName+"' class='replaced' ></div>" );
						}else{
							$(parentxc).append( "<div id ='"+areaName+"' class='notReplace' ></div>" );
						}
						ReactDOM.render(React.createFactory(Provider)({store: window.toBeReplacedMap[areaName]},
								App()
						), document.getElementById(areaName));
					}
			}catch(err){
				//console.log("loop ",parentxc, err);
			}
			
		});
	}
	function reDesign(ID) {
		var row = "					<div class='row'>"
				+ " 					         <div class='col-xs-3 col-sm-3 col-lg-2 "
				+ ID
				+ "SideBar'><div class='list-group' id ='"
				+ ID
				+ "gisidenav'><button type='button' data='"
				+ ID
				+ "General' class='list-group-item list-group-item-action active sidenavibutton'>General</button></div></div>"
				+ "                    			 <div class='col-xs-9 col-sm-9 col-lg-10 "
				+ ID + "Content'><div data='General'></div> </div>"
				+ "                        	</div>";

		$("#" + ID).append(row);
		$(
				"#" + ID + " div.MuiGrid-typeItem-2 div.table-responsive , #"
						+ ID + " div.MuiGrid-typeItem-2 div.demoform")
				.filter(function(index, element) {
					// filter out all emfforms of modals
					return $(element).parents('.modal-dialog').length <= 0;

				})
				.each(function(index, element) {
					$(element).addClass('selectedParent');
				})
				.each(
						function(index, element) {
							if ($(element).parents().hasClass('selectedParent')) {
								return;
							}
							// console.log($(element));
							var parent = $(element).parent().parent();

							var text;
							if ($(this).attr('class').indexOf('demoform') >= 0)
								text = parent.find('.MuiFormLabel-root-100')
										.html();
							else
								text = parent.find('.control-labelal').html();

							var sideNavigationButton = "<button data='"
									+ text
									+ "' type='button' class='list-group-item list-group-item-action sidenavibutton'>"
									+ text + "</button>\n";
							$("#" + ID + "gisidenav").append(
									sideNavigationButton);

							parent.addClass('detailedSide');

							navigationMap[text] = parent;
							$("#" + ID + " ." + ID + "Content").append(parent);
							parent.hide();

						});

		$("#" + ID + " > div.demoform").each(function(index, element) {
			$(element).addClass('detailedSide');
			navigationMap[ID + "General"] = $(element);
			$("#" + ID + " div[data='General']").append(element);
			$("div[data='General']").show();
		});

		if ($("#" + ID + " div[data='General'] .demoform .MuiGrid-typeItem-2")
				.children().length < 1) {

			$("#" + ID + " button[data='" + ID + "General']").remove();
			$("#" + ID + " div[data='General']").remove();

			$("#" + ID).find("div[class$='Content']").children().first().show();

			$("#" + ID).find("div[class$='list-group']").children().first()
					.addClass('active');
			//

		}
		$(".sidenavibutton").on("click", function(event) {
			// console.log("click ",$(this),event);
			$(this).parent().parent().parent().find(".detailedSide").hide();
			$(this).parent().find(".active").removeClass('active');

			navigationMap[$(this).attr('data')].show();
			$(this).addClass('active');

		})
		// console.log(navigationMap);
		$(".table-responsive").parent().css("flex-direction", "unset");
		$(".MuiGrid-typeContainer-1").css("display", "inherit");
		$(".MuiGrid-spacing-xs-16-22").css("display", "flex");

	}

	if (parent !== undefined && parent.KnimePageLoader !== undefined) {
		parent.KnimePageLoader.autoResize(window, frameElement.id)
	}
}();