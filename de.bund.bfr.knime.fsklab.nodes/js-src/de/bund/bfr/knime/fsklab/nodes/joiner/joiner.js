/*jshint esversion: 6 */
/* global $ */
joiner = function() {
	$('head').append(
			'<meta http-equiv="X-UA-Compatible" content="IE=8,IE=edge" />');
	if (!String.prototype.startsWith) {
		String.prototype.startsWith = function(searchString, position) {
			position = position || 0;
			return this.indexOf(searchString, position) === position;
		};
	}
	var nodeSearch = function nodeSearch(treeNodes, searchID){
	    for (var nodeIdx = 0; nodeIdx <= treeNodes.length-1; nodeIdx++) {
	        var currentNode = treeNodes[nodeIdx],
	            currentId = currentNode.id,
	            currentChildren = currentNode.nodes;
	        if(currentChildren) {
	            var foundDescendant = nodeSearch(currentChildren, searchID); 
	            if (foundDescendant) {
	            	return foundDescendant;
	            }
	        }
	        if (currentId == searchID) {    
	            return currentNode;
	        }
	        
	    }
	    return false;
	};
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
	function autocomplete(inp, arr, store, schema, uischema, fieldName) {
		/*
		 * the autocomplete function takes two arguments, the text field element
		 * and an array of possible autocompleted values:
		 */
		var currentFocus;
		function inputHandler(e,value) {
			var a, b, i, val = this.value;
			/*
			 * close any already open lists of autocompleted
			 * values
			 */
			closeAllLists();

			currentFocus = -1;
			/*
			 * create a DIV element that will contain the items
			 * (values):
			 */
			a = document.createElement("DIV");
			a.setAttribute("id", e.target.id + "autocomplete-list");
			a.setAttribute("class", "autocomplete-items");
			/*
			 * append the DIV element as a child of the
			 * autocomplete container:
			 */

			e.target.parentNode.appendChild(a);
			/* for each item in the array... */
			if (!val) {
				for (i = 0; i < arr.length; i++) {
					/*
					 * create a DIV element for each matching
					 * element:
					 */
					b = document.createElement("DIV");
					/* make the matching letters bold: */
					b.innerHTML = "<strong>"
							+ arr[i].substr(0, 0) + "</strong>";
					b.innerHTML += arr[i].substr(0);
					/*
					 * insert a input field that will hold the
					 * current array item's value:
					 */
					b.innerHTML += "<input type='hidden' value='"
							+ arr[i] + "'>";
					/*
					 * execute a function when someone clicks on
					 * the item value (DIV element):
					 */
					b
							.addEventListener(
									"click",
									function(e) {
										/*
										 * insert the value for
										 * the autocomplete text
										 * field:
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
										/*
										 * close the list of
										 * autocompleted values,
										 * (or any other open
										 * lists of
										 * autocompleted values:
										 */
										closeAllLists();
									});
					a.appendChild(b);
				}
			} else {
				for (i = 0; i < arr.length; i++) {
					/*
					 * check if the item starts with the same
					 * letters as the text field value:
					 */
					if (arr[i].substr(0, val.length)
							.toUpperCase() == val.toUpperCase()) {
						/*
						 * create a DIV element for each
						 * matching element:
						 */
						b = document.createElement("DIV");
						/* make the matching letters bold: */
						b.innerHTML = "<strong>"
								+ arr[i].substr(0, val.length)
								+ "</strong>";
						b.innerHTML += arr[i]
								.substr(val.length);
						/*
						 * insert a input field that will hold
						 * the current array item's value:
						 */
						b.innerHTML += "<input type='hidden' value='"
								+ arr[i] + "'>";
						/*
						 * execute a function when someone
						 * clicks on the item value (DIV
						 * element):
						 */
						b
								.addEventListener(
										"click",
										function(e) {
											/*
											 * insert the value
											 * for the
											 * autocomplete text
											 * field:
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
											/*
											 * close the list of
											 * autocompleted
											 * values, (or any
											 * other open lists
											 * of autocompleted
											 * values:
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
		inp
				.addEventListener(
						"input",inputHandler
						);
		
		/* execute a function presses a key on the keyboard: */
		inp.addEventListener("focus", function(e) {
			console.log("e");
			var x = document.getElementById(this.id + "autocomplete-list");
			if (x)
				x = x.getElementsByTagName("div");
			
			if(x == null){
				inputHandler(e);
			}
		});
		inp.addEventListener("click", function(e) {
			event.preventDefault(); // Let's stop this event.
			event.stopPropagation(); // Really this time.
			var x = document.getElementById(this.id + "autocomplete-list");
			if (x)
				x = x.getElementsByTagName("div");
			
			if(x == null){
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
				console.log(x);
				if(x == null){
					inputHandler(e);
				}else{
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
	joinerNode.name = 'FSK Joiner';
	var navigationMap = {};
	var firstModelName  = '';
	var secondmodelName = '';
	var paper;
	var graph;
	var _firstModel = {
		generalInformation : {},
		scope : {},
		dataBackground : {},
		modelMath : {}
	};
	var _secondModel = {
		generalInformation : {},
		scope : {},
		dataBackground : {},
		modelMath : {}
	};
	var _firstModelMath;
	var _secondModelMath;
	var _secondModelScript;

	var _secondModelViz;
	var _viewValue;
	var scriptBeingEdited;
	var _modelScriptTree;  
	window.joinRelationsMap = {};
	var firstModelParameterMap = new Object();
	var secomndModelParameterMap = new Object();
	joinerNode.init = function(representation, value) {
		_firstModel.generalInformation = JSON.parse(value.generalInformation);
		_firstModel.scope = JSON.parse(value.scope);
		firstModelName = value.firstModelName;
		secondModelName = value.secondModelName;
		
		_firstModel.modelMath = JSON.parse(value.modelMath);
		_firstModel.dataBackground = JSON.parse(value.dataBackground);

		_firstModelMath = JSON.parse(value.modelMath1);
		_secondModelMath = JSON.parse(value.modelMath2);
		/*
		 * _secondModel.generalInformation = value.secondGeneralInformation;
		 * _secondModel.scope = value.secondScope; _secondModel.modelMath =
		 * value.secondModelMath; _secondModel.dataBackground =
		 * value.secondDataBackground;
		 */
		_secondModelScript = value.secondModelScript;
		_secondModelViz = value.secondModelViz;
		_modelScriptTree = JSON.parse(value.modelScriptTree);
		_viewValue = value;

		if (_viewValue.joinRelations && _viewValue.joinRelations != ""){
			_viewValue.joinRelations = JSON.parse(_viewValue.joinRelations);
			$.each(_viewValue.joinRelations,function(index,value){
				if(value ){
					window.joinRelationsMap[value.sourceParam.parameterID+value.targetParam.parameterID] = value ;	
				}
				 
			})
			
		}
		else
			_viewValue.joinRelations = [];

		window.generalInformation = _firstModel.generalInformation;
		window.scope = _firstModel.scope;
		window.modelMath = _firstModel.modelMath;
		window.dataBackground = _firstModel.dataBackground;
		prepareData(_firstModel);
		create_body();
		fixTableHeaders();
	};
	function fixTableHeaders(){
		var tablePopups ={};
		tablePopups["modelCategory"] = window.uischema13
		tablePopups["modificationdate"] = window.uischema14
		tablePopups["creators"] = window.uischema23
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
		console.log(tablePopups)
		$(".table-responsive").each(function (index, val){
			var x = $(val).parent().find("[aria-describedby*='tooltip-add']");
			currentArea = window.makeId($(x).attr('aria-label'));
			if(tablePopups[currentArea]){
				$.each($(val).find("th"),function(indexss, th){
					thText = $(th).text()
					$.each(tablePopups[currentArea]["elements"],function(indexss, UIElement){
						textInUIModel = UIElement.label.toLowerCase().replace(new RegExp(" ", 'g'), "");
						if(textInUIModel == thText.toLowerCase()){
							$(th).text(UIElement.label)
						}
						
					});
					
				});
			}
		})
		
		$("[role='tooltip']").find("div:contains('should be equal to one of the allowed values')" ).css('visibility', 'hidden');
	}
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
			//console.log(err);
		}
		_firstModel.generalInformation.description = _firstModel.generalInformation.description != null ? _firstModel.generalInformation.description
				: "";
		_firstModel.generalInformation.author = _firstModel.generalInformation.author != null ? _firstModel.generalInformation.author
				: {};
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
	
	joinerNode.getComponentValue = function() {
		
		window.store1.getState().jsonforms.core.data.author = window.store23
				.getState().jsonforms.core.data;
		window.store6.getState().jsonforms.core.data.study = window.store7
				.getState().jsonforms.core.data;
		_viewValue.generalInformation = JSON
				.stringify(window.store1.getState().jsonforms.core.data);
		_viewValue.scope = JSON
				.stringify(window.store2.getState().jsonforms.core.data);
		_viewValue.modelMath = JSON
				.stringify(window.store17.getState().jsonforms.core.data);
		_viewValue.dataBackground = JSON
				.stringify(window.store6.getState().jsonforms.core.data);
		if(_viewValue.joinRelations.push){
			_viewValue.joinRelations = JSON.stringify(_viewValue.joinRelations);
		}
		
		var ajv = new Ajv({allErrors: true, format:'fast'});		
		// Add convert keyword for date-time schema
		ajv = ajv.removeKeyword("format")
		try{
			ajv.addKeyword('format',{
				  type: 'string',
				  compile: function(sch,parentSchema) {
				    return parentSchema.format === 'date-time' && sch ? function(value,objectKey,object,key) {
				      // Update date-time string to Date object
				      object[key] = new Date(value);
				      return true;
				    } : function() {
				      return true;
				    }
				  }
				});
			ajv.validate(window.schema,window.store1.getState().jsonforms.core.data); 		
			ajv.validate(window.schema2, window.store2.getState().jsonforms.core.data); 
			ajv.validate(window.schema17,window.store17.getState().jsonforms.core.data); 
			ajv.validate(window.schema6,window.store6.getState().jsonforms.core.data ); 
		}catch(err){
			console.log(err)
		}
		var serializer = new XMLSerializer();
		var str = serializer.serializeToString(paper.svg);
		_viewValue.svgRepresentation = str

		if(window.viscodeMirrorContainerx && window.viscodeMirrorContainerx.CodeMirror){
			 visScript = window.viscodeMirrorContainerx.CodeMirror.getValue();
			_viewValue.secondModelViz = visScript;
		}
		if(_modelScriptTree)
			_viewValue.modelScriptTree = JSON.stringify(_modelScriptTree);
		var generalError = "";
		generalError += validateAgainstSchema(window.schema, window.store1.getState().jsonforms.core.data,"- General Information:");
		generalError += validateAgainstSchema(window.schema2, window.store2.getState().jsonforms.core.data,"- Scope:");
		generalError += validateAgainstSchema(window.schema17, window.store17.getState().jsonforms.core.data,"- Model Math:");
		generalError += validateAgainstSchema(window.schema6, window.store6.getState().jsonforms.core.data,"- Data Background:");
		_viewValue.validationErrors = generalError.trim();
		return _viewValue;
	};
	function validateAgainstSchema(schema, data, schemaName){
		var ajv = new Ajv({allErrors: true});
		ajv.validate(schema, data); 
		var requiredErrorText = "   Field(s): ";
		var formatErrorText = "   Field(s): ";
		for(i = 0 ; i < ajv.errors.length;i++){
			theError =  ajv.errors[i]
			console.log(theError)
			if(theError.keyword == "required" ){
				requiredErrorText += (requiredErrorText.length > 13 ? " , " : " ") + theError.params.missingProperty
			}else if(theError.keyword == "format"){
				formatErrorText += (formatErrorText.length > 13?" , " : " " )+ theError.dataPath.substring(1)
			}
		}
		requiredErrorText = (requiredErrorText.length > 13 ? (schemaName +",,,"+ requiredErrorText + " are(is) required,,,") : " ") ;
		if(requiredErrorText.length == 1 && formatErrorText.length > 13){
			formatErrorText =  schemaName +",,,"+ formatErrorText + " have(has) wrong format,,,";
		}else if(formatErrorText.length > 13){
			formatErrorText = formatErrorText + " have(has) wrong format,,,"
		}else{
			formatErrorText = " "
		}
		return requiredErrorText+formatErrorText;
		
	}
	return joinerNode;

	// --- utility functions ---

	function create_body() {
		$.ajaxSetup({
			cache : true
		});
		document.createElement("body");
		bodyContent = "<meta http-equiv='X-UA-Compatible' content='IE=edge'>"
				+ "<h3>Combined FSK Object </h3>"
				+ "        <div class='tabbable boxed parentTabs'>\n"
				+ "        <ul class='nav nav-tabs'>\n"
				+ "            <li class='active'><a href='#set1'>Join Panel</a>\n"
				+ "            </li>\n"
				+ "            <li><a href='#set2'>Combined Model</a>\n"
				+ "            </li>\n"
				+ "        </ul>\n"
				+ "        <div class='tab-content'>\n"
				+ "            <div class='tab-pane fade active in' id='set1'>\n"
				+ "					<div class='container-fluid'><div class='row content'><div id='paper' class='width: 500px; height: 500px; overflow: scroll;'></div></div> <div id='details' class='col-sm-12'></div></div>"
				+ "            </div>\n"
				+ "            <div class='tab-pane fade' id='set2'>\n"
				+ "                <div class='tabbable'>\n"
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
				+ "                    </ul>\n"
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
				+ "								<div class=\"demoform\">"
				+ "								</div>"
				+ "					 		 </div>"
				+ "                        </div>\n"
				+ "                        <div class='tab-pane fade' id='sub25'>\n"
				+ "							<div class='row'>"
				+ " 					         <div class='col-xs-6 col-sm-6 col-lg-4' id='tree'>" 
				+ "                        		 </div>"
				+ "                    			 <div class='col-xs-6 col-sm-6 col-lg-8'> "
				+ "									<h4>Model Script</h4>"
				+ "									<textarea id='firstModelScript' name='firstModelScript'>"
				+ "									</textarea>"
				+ "					 			 </div>"
				+ "                        	</div>"
				+ "                        </div>\n"
				+ "                        <div class='tab-pane fade' id='sub26'>\n"
				+ "                		        <div >"
				+ "								     <h4>Second Model Visualization Script</h4>"
				+ "								     <textarea id='secondModelViz' name='secondModelViz'>"
				+   									 _secondModelViz
				+ "								     </textarea>"
				+ "					 		 </div>"
				+ "                        </div>\n"
				+ "                    </div>\n"
				+ "                </div>\n"
				+ "            </div>\n" + "        </div>\n" + "    </div>"

		$('body').append(bodyContent);

		$('body').append(' <div id="root"></div>');
		$('#Metadata a').on('click', function(e) {
			e.preventDefault()
			$(this).tab('show')
		})

		$('#tree').treeview({data:_modelScriptTree});
		$('#tree').on('nodeSelected', function(event, data) {
			
			scriptBeingEdited = data;
			window.codeMirrorContainer.CodeMirror.setValue(data.script);
			window.codeMirrorContainer.CodeMirror.refresh();
			
		});
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
							$('.nav-tabs a').click(
									function(e) {
										e.preventDefault();
										$(this).tab('show');
										var canvas = $('#paper');

										paper.setDimensions(canvas.width(),
												canvas.height());

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
												if(e.currentTarget.innerHTML == "Join Panel"){
													
													var canvas = $('#paper');
													paper.setDimensions(canvas
															.width(), canvas
															.height());
												}
												

												window.codeMirrorContainer = $(
														'#sub25').find(
														".CodeMirror")[0];
												if (window.codeMirrorContainer
														&& window.codeMirrorContainer.CodeMirror) {
													window.codeMirrorContainer.CodeMirror
															.refresh();

												} else {
													window.codeMirrorContainer = window.CodeMirror
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
													window.codeMirrorContainer.on("blur", function(){
													    nodeSearch(_modelScriptTree,scriptBeingEdited.id).script = window.codeMirrorContainer.CodeMirror.getValue();
													    $('#tree').treeview({data:_modelScriptTree});
														$('#tree').on('nodeSelected', function(event, data) {
															scriptBeingEdited = data;
															window.codeMirrorContainer.CodeMirror.setValue(data.script);
															window.codeMirrorContainer.CodeMirror.refresh();
															
														});
													    
													});

												}

												window.viscodeMirrorContainerx  = $(
														'#sub26').find(
														".CodeMirror")[0];
												if (window.viscodeMirrorContainerx
														&& window.viscodeMirrorContainerx.CodeMirror) {
													window.viscodeMirrorContainerx.CodeMirror
															.refresh();

												} else {
													window.viscodeMirrorContainerx = window.CodeMirror
															.fromTextArea(
																	document
																			.getElementById("secondModelViz"),
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
		drawWorkflow();
		try {
			createEMFForm();
		} catch (err) {

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
			}

		});
		$(window).resize(function() {
			var canvas = $('#paper');
			paper.setDimensions(canvas.width(), canvas.height());
			console.log(graph.toJSON());
			_viewValue.jsonRepresentation = JSON.stringify(graph.toJSON());
			console.log("-------------------------------------------------------------------------------------");
		});

		$.each($("input[type='text']"), function(key, value) {

			$(value).removeAttr('class');
			$(value).addClass('form-control');
			$(value).parent().parent().removeAttr('class');
			$(value).parent().parent().addClass('form-group');
			fixInputCSS($(value));
			$(value).focus(function(){
			    if(this.value.length > 0){
			    	if($(value).width() <  ((this.value.length + 1) * 12)){
			    		this.style.width = ((this.value.length + 1) * 12) + 'px';
			    		
			    	}
			    }

			});
			$(value).blur(function(){
			    if(this.value.length > 0){
			        this.style.width = "";
			    }

			});
		});

		$('.MuiFormLabel-root-100').css('font-size', '1.5rem');
		$('.MuiDialog-paper-128').css('display', 'inline');
		$('.MuiDialog-paper-128').css('max-height', '');
		$('.MuiDialog-paper-128').css('overflow-y', 'visible');

		$(".MuiTable-root-222 thead").removeAttr('class');
		$(".MuiTable-root-222 thead tr").removeAttr('class');
		$(".MuiTable-root-222 thead tr th").removeAttr('class');
		$(".MuiTable-root-222 thead tr th th").removeAttr('class');
		$(".MuiTable-root-222 tbody").removeAttr('class');
		$(".MuiTable-root-222 tbody tr").removeAttr('class');
		$(".MuiTable-root-222 tbody tr td").removeAttr('class');
		$(".MuiTable-root-222 tbody tr td div").removeAttr('class');
		$(".MuiTable-root-222 tbody tr td div div").removeAttr('class');
		$(".MuiTable-root-222 tbody tr td div div div").removeAttr('class');

		$(".MuiTable-root-222 tbody tr td div div div input").removeAttr(
				'class');
		window
				.tableInputBootstraping($(".MuiTable-root-222 tbody tr td div div div input"));

		$('.MuiTable-root-222').addClass('table');
		$('.MuiTable-root-222').parent().addClass('table-responsive');
		$('.MuiTable-root-222').parent().removeClass('MuiGrid-typeItem-2');
		$('.MuiTable-root-222').removeClass('MuiTable-root-222');

		$('.MuiFormControl-root-90').addClass('form-group');
		$.each($('.MuiToolbar-root-196').find('h2'), function(index, value) {
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

		var StringObjectPopupsName = [ 'qualityMeasures', 'event',
				'laboratoryAccreditation', 'populationSpan',
				'populationDescription', 'bmi', 'specialDietGroups', 'region',
				'country', 'populationRiskFactor', 'season',
				'patternConsumption', 'populationAge' ];
		$("[aria-describedby*='tooltip-add']").click(function(event) {
			currentArea = window.makeId($(this).attr('aria-label'));
			window.generalInformation = window.store1.getState().jsonforms.core.data;
			window.scope = window.store2.getState().jsonforms.core.data;
			window.modelMath =  window.store17.getState().jsonforms.core.data;
			window.dataBackground =  window.store6.getState().jsonforms.core.data;
			if ($.inArray(currentArea, StringObjectPopupsName) < 0) {
				event.preventDefault(); // Let's stop this event.
				event.stopPropagation(); // Really this time.
				$('#title' + currentArea).text(currentArea);

				$('#' + currentArea).modal('show');
				$('.modal-content').resizable({
				// alsoResize: ".modal-dialog",
				// minHeight: 150
				});
				$('.modal-dialog').draggable();
				$('#' + currentArea).on('show.bs.modal', function() {
					$(this).find('.modal-body').css({
						'max-height' : '100%'
					});
				});
				window.scrollTo(0, 0);
			}
		});
		autoCompleteCB = [ 'country', 'language', 'source', 'rights', 'format',
				'software', 'languageWrittenIn', 'modelClass', 'basicProcess',
				'status', 'productName', 'productUnit', 'productionMethod',
				'packaging', 'productTreatment', 'originArea', 'originCountry',
				'fisheriesArea', 'hazardType', 'hazardName', 'hazardUnit',
				'hazardIndSum', 'populationName', 'studyAssayTechnologyType',
				'accreditationProcedureForTheAssayTechnology',
				'samplingStrategy', 'typeOfSamplingProgram', 'samplingMethod',
				'lotSizeUnit', 'samplingPoint', 'collectionTool',
				'recordTypes', 'foodDescriptors', 'laboratoryCountry',
				'parameterType', 'parameterUnit', 'parameterUnitCategory',
				'parameterSource', 'parameterSubject', 'parameterDistribution',
				'modelEquationClass', 'typeOfExposure' ];
		autoCompleteArray = [ window.Country, window.Language, window.Source,
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
		autoCompleteStores = [ window.store23, window.store1, window.store1,
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
		autoCompleteSchemas = [ window.schema23, window.schema, window.schema,
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
		autoCompleteUischema = [ window.uischema23, window.uischema,
				window.uischema, window.uischema, window.uischema,
				window.uischema, window.uischema, window.uischema13,
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
		window.autocomplete = autocomplete;
		$.each(autoCompleteCB, function(index, value) {
			var ID = '#/properties/' + value;
			autocomplete(document.getElementById(ID), autoCompleteArray[index],
					autoCompleteStores[index], autoCompleteSchemas[index],
					autoCompleteUischema[index], autoCompleteCB[index]);

		})
		$("input[type='text']").focus(function(event) {
			event.preventDefault(); // Let's stop this event.
			event.stopPropagation(); // Really this time.

			fixInputCSS($(event.target));

		});
		$("input[type='text']").click(function(event) {

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
		/*
		 * $.each( $(".demoform"), function( key, value ) {
		 * 
		 * 
		 * $(value).addClass('card');
		 * 
		 * });
		 */
		$(".notReplace button[aria-describedby*='tooltip-add']").off("click");
		$(".notReplace button[aria-describedby*='tooltip-add']").off("click");
		$("div[role*='tooltip']:contains('should match format')").parent()
				.parent().remove();
		$("div[role*='tooltip']:contains('should be')").parent().parent()
				.remove();

		$("div[role*='tooltip']").click(function(event) {

			currentArea = window.makeId($(this).attr('aria-label'));

		});
		reDesign("generalinformation");
		reDesign("scope");
		reDesign("databackground");
		reDesign("modelMath");
	}
	function reDesign(ID){
		var row = "					<div class='row'>"
			+ " 					         <div class='col-xs-3 col-sm-3 col-lg-2 "+ID+"SideBar'><div class='list-group' id ='"+ID+"gisidenav'><button type='button' data='"+ID+"General' class='list-group-item list-group-item-action active sidenavibutton'>General</button></div></div>"
			+ "                    			 <div class='col-xs-9 col-sm-9 col-lg-10 "+ID+"Content'><div data='General'></div> </div>"
			+ "                        	</div>";
			
		$("#"+ID).append( row);
		$("#"+ID+" div.MuiGrid-typeItem-2 div.table-responsive , #"+ID+" div.MuiGrid-typeItem-2 div.demoform").filter(function(index ,element){
			//filter out all emfforms of modals
			return $(element).parents('.modal-dialog').length <= 0;
			  
        }).each(function(index, element) {
		  
			//console.log($(element));
		    var parent = $(element).parent().parent();
		    
		    var text;
		    if($(this).attr('class').indexOf('demoform') >= 0 )
		    	 text = parent.find('.MuiFormLabel-root-100').html();
		    else
		    	 text = parent.find('.control-labelal').html();
		    
		    
		    var sideNavigationButton = "<button data='"+text+"' type='button' class='list-group-item list-group-item-action sidenavibutton'>"+text+"</button>\n" ;
		    $("#"+ID+"gisidenav").append(sideNavigationButton);
		    
		    parent.addClass('detailedSide');
		   
		    navigationMap[text] = parent;
			$("#"+ID+" ."+ID+"Content").append( parent);
			parent.hide();
			
		});
		
		
		$("#"+ID+" > div.demoform").each(function(index, element) {
			//console.log(element);
			$(element).addClass('detailedSide');
			navigationMap[ID+"General"] = $(element);
			$("#"+ID+" div[data='General']").append( element);
			$("div[data='General']").show();
		});
		
		if($("#"+ID+" div[data='General'] .demoform .MuiGrid-typeItem-2").children().length  < 1 ){
			
			$("#"+ID+" button[data='"+ID+"General']").remove();
			$("#"+ID+" div[data='General']").remove();
			
			$("#"+ID).find("div[class$='Content']").children().first().show();
			
			$("#"+ID).find("div[class$='list-group']").children().first().addClass('active');
			//
			
			
			
		}
		$(".sidenavibutton").on("click",function(event){
			//console.log("click ",$(this),event);
			$(this).parent().parent().parent().find(".detailedSide").hide();
			$(this).parent().find(".active").removeClass('active');
			
			navigationMap[$(this).attr('data')].show(); 
			$(this).addClass('active');
			
		})
		//console.log(navigationMap);
		$(".table-responsive").parent().css("flex-direction","unset");
		$(".MuiGrid-typeContainer-1").css("display","inherit");
		$(".MuiGrid-spacing-xs-16-22").css("display","flex");
	}
	function drawWorkflow() {
		graph = new joint.dia.Graph;

		paper = new joint.dia.Paper({

			el : document.getElementById('paper'),
			drawGrid : 'mesh',
			gridSize : 10,
			model : graph,
			snapLinks : true,
			linkPinning : true,
			drawGrid : true,

			highlighting : {
				'default' : {
					name : 'stroke',
					options : {
						padding : 6
					}
				}
			},
			interactive : function(cellView) {
				if (cellView.model instanceof joint.dia.Link) {
					// Disable the default vertex add functionality on
					// pointerdown.
					return {
						vertexAdd : false
					};
					return false;
				}
				return true;
			},
			
			validateEmbedding : function(childView, parentView) {

				return parentView.model instanceof joint.shapes.devs.Coupled;
			}

			
		});
		var previousOne;
		
		
		paper.on('cell:pointerclick', function(cellView) {
			
			if(previousOne){
				previousOne.unhighlight();
			}
			previousOne = cellView;
		    cellView.highlight();
		});
		paper.on('link:pointerup', function(linkx) {
			//console.log(linkx);
			if (linkx.model instanceof joint.dia.Link) {
				console.log(linkx) 
				sourcePort = linkx.model.attributes.source.port;
				targetPort = linkx.model.attributes.target.port;
			    console.log('New cell with id ' + linkx.id + ' added to the graph.',sourcePort,targetPort) 
			    if(targetPort == undefined){
			    	linkx.remove();
			    }
			    
		
			}
		});
		
		paper
				.on(
						'cell:pointerdblclick',
						function(cellView, evt, x, y) {
							//console.log(cellView.model?cellView.model.getPorts()[0]:"");
							//console.log(cellView.model);
							if (cellView.model instanceof joint.dia.Link) {
								
								link = cellView.model

								var sourcePort = link.get('source').port;
								var sourceId = link.get('source').id;
								var targetPort = link.get('target').port;
								var targetId = link.get('target').id;
								window.sJoinRealtion = window.joinRelationsMap[sourcePort+targetPort]; 
								if(!(document.getElementById("commandLanguage"))){
									$('#details')
									.html(
											'<form action="">'
													+ '<div class="form-group row">'
													+ '<label class="col-6 col-form-label" for="source">Source Port:</label>'
													+ '<div class="col-6"><input type="text" class="form-control" id="source" value="'
													+ sourcePort
													+ '"  ></div>'
													+ '</div>'
													+ '<div class="form-group row">'
													+ ' <label class="col-6 col-form-label" for="target">Target Port:</label>'
													+ '<div class="col-6"><input  type="text" class="form-control" id="target" value = "'
													+ targetPort
													+ '" ></div>'
													+ '</div>'
													+ '<div class="form-group row">'
													+ ' <label class="col-6 col-form-label" for="target">Command Language:</label>'
													+ '<div class="col-6"><input  type="text" class="form-control" id="commandLanguage" ></div>'
													+ '</div>'
													+ '<div class="form-group row">'
													+ ' <label class="col-6 col-form-label" for="Command">Conversion Command:</label>'
													+ '<div class="col-6"><textarea class="form-control" rows="3" id="Command" >'
													+ sourcePort
													+ '</textarea> </div>'
													+ '</div>'
													+ '<div class="form-group">'
													+ '</div>' + '</form>');
								}
								
								$('#source').val(sourcePort);
								$('#target').val(targetPort);
								$('#commandLanguage').val(sJoinRealtion.language_written_in);
								$('#Command').val(sJoinRealtion.command);
								$('#Command').keyup(function() {
									window.sJoinRealtion.command = $('#Command').val();									
								});
								$('#commandLanguage').change(function() {
									window.sJoinRealtion.language_written_in = $('#commandLanguage').val();
								});
								
							}
						});
		var firstModelInputParameters = [];
		var firstModelOutputParameters = [];
		_.each(_firstModelMath.parameter, function(param) {
			firstModelParameterMap[param.parameterID] = param
			if (param.parameterClassification == 'Input') {
				var port = {
						id:param.parameterID,
					    group: 'in',
					    label: {
					        markup: "<text class='label-text' fill='black'><title>"+param.parameterDataType+"</title>"+param.parameterID+"</text>"
					    }
					   
					};
				firstModelInputParameters.push(port);
			} else {
				var port = {
						id:param.parameterID,
					    group: 'out',
					    label: {
					        markup: "<text class='label-text' fill='black'><title>"+param.parameterDataType+"</title>"+param.parameterID+"</text>"
					    }
					   
					};
				firstModelOutputParameters.push(port);
			}

		});
		var secondModelInputParameters = [];
		var secondModelOutputParameters = [];
		_.each(_secondModelMath.parameter, function(param) {
			secomndModelParameterMap[param.parameterID] = param
			if (param.parameterClassification == 'Input') {
				var port = {
						id:param.parameterID,
					    group: 'in',
					    label: {
					        markup: "<text class='label-text' fill='black'><title>"+param.parameterDataType+"</title>"+param.parameterID+"</text>"
					    }
					   
					};
				secondModelInputParameters.push(port);
			} else {
				var port = {
						id:param.parameterID,
					    group: 'out',
					    label: {
					        markup: "<text class='label-text' fill='black'><title>"+param.parameterDataType+"</title>"+param.parameterID+"</text>"
					    }
					   
					};
				secondModelOutputParameters.push(port);
			}
		});
		
		var canvasheight = 500;
		if (firstModelInputParameters.length > secondModelInputParameters.length) {
			canvasheight = firstModelInputParameters.length
		} else {
			canvasheight = secondModelInputParameters.length
		}
		var canvas = $('#paper');

		$('#paper').height((canvasheight * 25) + 300);
		paper.setDimensions(canvas.width(), canvas.height());
		var paperWidth = $('#paper').width();
		firstModelHeight = firstModelInputParameters.length >firstModelOutputParameters.length? firstModelInputParameters.length* 25:firstModelOutputParameters.length* 25

		var firstModelNameWrap = joint.util.breakText(firstModelName, {
		    width: 200,
		    height: firstModelHeight
		});
		var firstModelTojoin = new joint.shapes.devs.Atomic({

			position : {
				x : paperWidth - 670,
				y : 60
			},
			size : {
				width : 200,
				height : firstModelHeight
			},
			
			ports : {
				groups : {
					'in' : {
						attrs : {
							'.port-body' : {
								fill : '#16A085'
							}
						}
					},
					'out' : {
						attrs : {
							'.port-body' : {
								fill : '#E74C3C'
							}
						}
					}
				}
			}
		});
		
		$.each(firstModelInputParameters, function(index, value){
			
			firstModelTojoin.addPort(value);
			
		})
		$.each(firstModelOutputParameters, function(index, value){
			
			firstModelTojoin.addPort(value);
			
		})
		firstModelTojoin.attr({
			rect : {
				rx : 5,
				ry : 5,
				'stroke-width' : 2,
				stroke : 'black'
			},
			text : {
				text : firstModelNameWrap,
				'font-size' : 12,
				'font-weight' : 'bold',
				'font-variant' : 'small-caps',
				'text-transform' : 'capitalize',
				margin : '20px',
				padding : '40px'
			}
		});
		secondModelHeight = secondModelInputParameters.length >secondModelOutputParameters.length? secondModelInputParameters.length* 25:secondModelOutputParameters.length* 25
		var secondModelNameWrap = joint.util.breakText(secondModelName, {
		    width: 200,
		    height: secondModelHeight
		});
		var secondModelToJoin = new joint.shapes.devs.Atomic({

			position : {
				x : paperWidth - 330,
				y : 180
			},
			size : {
				width : 200,
				height :secondModelHeight 
			},
			ports : {
				groups : {
					'in' : {
						attrs : {
							'.port-body' : {
								fill : '#16A085'
							}
						}
					},
					'out' : {
						attrs : {
							'.port-body' : {
								fill : '#E74C3C'
							}
						}
					}
				}
			}
		});
		$.each(secondModelInputParameters, function(index, value){
			
			secondModelToJoin.addPort(value);
			
		})
		
		$.each(secondModelOutputParameters, function(index, value){
			
			secondModelToJoin.addPort(value);
			
		})
		secondModelToJoin.attr({
			rect : {
				rx : 5,
				ry : 5,
				'stroke-width' : 2,
				stroke : 'black'
			},
			text : {
				text : secondModelNameWrap,
				'font-size' : 12,
				'font-weight' : 'bold',
				'font-variant' : 'small-caps',
				'text-transform' : 'capitalize',
				margin : '20px',
				padding : '40px'
			}
		});
		
		paper.on('link:connect', function(evt, cellView, magnet, arrowhead) {
			console.log(evt.model);
			sourcePort = evt.model.attributes.source.port;
			targetPort = evt.model.attributes.target.port;
			if(!targetPort){
				return;
			}
			
				$('#details')
						.html(
								'<form action="">'
										+ '<div class="form-group row">'
										+ '<label class="col-6 col-form-label" for="source">Source Port:</label>'
										+ '<div class="col-6"><input type="text" class="form-control" id="source" value="'
										+ sourcePort
										+ '"  ></div>'
										+ '</div>'
										+ '<div class="form-group row">'
										+ ' <label class="col-6 col-form-label" for="target">Target Port:</label>'
										+ '<div class="col-6"><input  type="text" class="form-control" id="target" value = "'
										+ targetPort
										+ '" ></div>'
										+ '</div>'
										+ '<div class="form-group row">'
										+ ' <label class="col-6 col-form-label" for="target">Command Language:</label>'
										+ '<div class="col-6"><input  type="text" class="form-control" id="commandLanguage" ></div>'
										+ '</div>'
										+ '<div class="form-group row">'
										+ ' <label class="col-6 col-form-label" for="Command">Conversion Command:</label>'
										+ '<div class="col-6"><textarea class="form-control" rows="3" id="Command" >'
										+ sourcePort
										+ '</textarea> </div>'
										+ '</div>'
										+ '<div class="form-group">'
										+ '</div>' + '</form>');
			autocomplete(document.getElementById('commandLanguage'), window.Language_written_in,undefined,undefined,undefined);
			$('#Command').keyup(function() {
				window.sJoinRealtion.command = $('#Command').val();
				
			});
			$('#commandLanguage').change(function() {
				window.sJoinRealtion.language_written_in = $('#commandLanguage').val();
			});
			
			sourceParameter = firstModelParameterMap[sourcePort];
			if (sourceParameter == undefined) {
				sourceParameter = secomndModelParameterMap[sourcePort];
			}
			targetParameter = secomndModelParameterMap[targetPort];
			if (targetParameter == undefined) {
				targetParameter = firstModelParameterMap[targetPort];
			}
			if (targetParameter != undefined) {
				window.sJoinRealtion = {
						sourceParam : sourceParameter,
						targetParam : targetParameter,
						command: sourcePort
					};
				
				if(!(_viewValue.joinRelations.push)){
					_viewValue.joinRelations = []
				}
				_viewValue.joinRelations.push(sJoinRealtion);
				window.joinRelationsMap[sourcePort+targetPort] = sJoinRealtion 
				_viewValue.jsonRepresentation = JSON.stringify(graph.toJSON());
			}
			
		});

		graph.on('remove', function(link) {
			sourcePort = link.attributes.source.port;
			targetPort = link.attributes.target.port;

			sourceParameter = firstModelParameterMap[sourcePort];
			if (sourceParameter == undefined) {
				sourceParameter = secomndModelParameterMap[sourcePort];
			}
			targetParameter = secomndModelParameterMap[targetPort];
			if (targetParameter == undefined) {
				targetParameter = firstModelParameterMap[targetPort];
			}
			if (targetParameter != undefined) {
				$.each(_viewValue.joinRelations, function(index, value) {
					if (value!= undefined && value.sourceParam === sourceParameter
							&& value.targetParam === targetParameter) {
						_viewValue.joinRelations.splice(index, 1);
					}
				})
				_viewValue.jsonRepresentation = JSON.stringify(graph.toJSON());
			}
		})
		

		/*
		 * graph.on('change:source change:target', function(link) { var
		 * sourcePort = undefined; var sourceId = undefined; var targetPort =
		 * undefined; var targetId = undefined; var sourcePort =
		 * link.get('source').port; var sourceId = link.get('source').id; var
		 * targetPort = link.get('target').port; var targetId =
		 * link.get('target').id; var sourceParameter; var targetParameter;
		 * if(targetPort != undefined && targetId != undefined ){
		 * //console.log(link); sourceParameter =
		 * firstModelParameterMap[sourcePort]; if(sourceParameter == undefined){
		 * sourceParameter = secomndModelParameterMap[sourcePort]; }
		 * targetParameter = secomndModelParameterMap[targetPort];
		 * if(targetParameter == undefined){ targetParameter =
		 * firstModelParameterMap[targetPort]; } if(targetParameter !=
		 * undefined){
		 * 
		 * _viewValue.joinRelations.push({sourceParam:sourceParameter,targetParam:targetParameter});
		 * //console.log(_viewValue.joinRelations);
		 * _viewValue.jsonRepresentation =JSON.stringify(graph.toJSON());
		 * //_viewValue.svgRepresentation = paper.svg; //link.label(0, {
		 * position: 0.5, attrs: { text: { text: sourcePort+" = "+targetPort } }
		 * }); } }
		 * 
		 * });
		 */

		if (_viewValue.jsonRepresentation != undefined) {
			if (_viewValue && _viewValue.jsonRepresentation
					&& _viewValue.jsonRepresentation != "") {
				try {
					graphObject = JSON.parse(_viewValue.jsonRepresentation)
					
					graph.fromJSON(JSON.parse(_viewValue.jsonRepresentation));
					$.each(graphObject.cells, function(cellIndex, cell){
						//console.log(cell)
						$.each(cell.ports.items, function(index, item){
							console.log(item)
							graph.getCell(cell.id).addPort(item);
						})
					})
					
				} catch (err) {
					console.log(err)
					console.log('here err');
				}
			}
		} else {
			graph.addCells([ firstModelTojoin, secondModelToJoin ]);
			console.log('here 3 ');
		}

	}
}();
