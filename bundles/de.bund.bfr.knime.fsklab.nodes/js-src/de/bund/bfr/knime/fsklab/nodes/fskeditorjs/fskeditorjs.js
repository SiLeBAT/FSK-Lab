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
	function bin2String(array) {
	  var result = "";
	  for (var i = 0; i < array.length; i++) {
	    result += String.fromCharCode(parseInt(array[i], 2));
	  }
	  return result;
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
	function autocomplete(inp, arr, store, schema, uischema, fieldName) {
		/*
		 * the autocomplete function takes two arguments, the text field element
		 * and an array of possible autocompleted values:
		 */
		
		var currentFocus;
		$(inp).addClass('domdsdsd');
		/* execute a function when someone writes in the text field: */
		inp.addEventListener("input", function(e) {
			var a, b, i, val = this.value;
			/* close any already open lists of autocompleted values */
			closeAllLists();
			
			
			currentFocus = -1;
			/* create a DIV element that will contain the items (values): */
			a = document.createElement("DIV");
			a.setAttribute("id", this.id + "autocomplete-list");
			a.setAttribute("class", "autocomplete-items");
			/* append the DIV element as a child of the autocomplete container: */
			this.parentNode.appendChild(a);
			/* for each item in the array... */
			if (!val) {
				for (i = 0; i < arr.length; i++) {
					/* create a DIV element for each matching element: */
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
						/* insert the value for the autocomplete text field: */

						store.getState().jsonforms.core.data[fieldName] = this
								.getElementsByTagName("input")[0].value;

						store.dispatch(Actions.init(
								store.getState().jsonforms.core.data, schema,
								uischema));
						/*
						 * close the list of autocompleted values, (or any other
						 * open lists of autocompleted values:
						 */
						closeAllLists();
					});
					a.appendChild(b);
				}
			}else{
				for (i = 0; i < arr.length; i++) {
					/*
					 * check if the item starts with the same letters as the
					 * text field value:
					 */
					if (arr[i].substr(0, val.length).toUpperCase() == val
							.toUpperCase()) {
						/* create a DIV element for each matching element: */
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
						b.addEventListener("click", function(e) {
							/* insert the value for the autocomplete text field: */
	
							store.getState().jsonforms.core.data[fieldName] = this
									.getElementsByTagName("input")[0].value;
	
							store.dispatch(Actions.init(
									store.getState().jsonforms.core.data, schema,
									uischema));
							/*
							 * close the list of autocompleted values, (or any
							 * other open lists of autocompleted values:
							 */
							closeAllLists();
						});
						a.appendChild(b);
					}
				}
			}
		});
		/* execute a function presses a key on the keyboard: */
		inp.addEventListener("keydown", function(e) {
			var x = document.getElementById(this.id + "autocomplete-list");
			if (x)
				x = x.getElementsByTagName("div");
			if (e.keyCode == 40) {
				/*
				 * If the arrow DOWN key is pressed, increase the currentFocus
				 * variable:
				 */
				currentFocus++;
				/* and and make the current item more visible: */
				addActive(x);
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
	let files = [];
	let newFilesNames = [];
	
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
	joinerNode.init = function(representation, value) {
		// console.log('value ',value);
		_firstModel.generalInformation = JSON.parse(value.generalInformation);
		_firstModel.scope = JSON.parse(value.scope);

		_firstModel.modelMath = JSON.parse(value.modelMath);
		_firstModel.dataBackground = JSON.parse(value.dataBackground);
		_firstModelScript = value.firstModelScript;
		_README =  value.readme != undefined ? value.readme :"";
		_firstModelViz = value.firstModelViz;

		_viewValue = value;
		
		window.generalInformation = _firstModel.generalInformation;
		window.scope = _firstModel.scope;
		window.modelMath = _firstModel.modelMath;
		window.dataBackground = _firstModel.dataBackground;

		prepareData(_firstModel);
		create_body();
	};
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
			console.log(err);
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
		if (window.firstModelScript && window.firstModelScript.save) {
			window.firstModelScript.save();
		}
		if (window.firstModelViz && window.firstModelViz.save) {
			window.firstModelViz.save();
		}
	
		_viewValue.firstModelScript = $('#firstModelScript').val();
		_viewValue.firstModelViz = $('#firstModelViz').val();
		_viewValue.files = files;
		_viewValue.fileNames = newFilesNames;
		return _viewValue;
	};

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
				+

				"                        <li class='active'><a href='#sub21'>General Information</a>\n"
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
				
				+ "                        <div class='tab-pane fade' id='sub27'>\n"
				+ "                    		 <div  >"
				+ "								<h4>README</h4>"
				+ "								<textarea disabled id='READMEArea' name='READMEArea'>"
				+ 									_README
				+ "								</textarea>"
				+ "					 		 </div>"
				+ "                        </div>"
			
				+ "                        <div class='tab-pane fade' id='sub25'>\n"
				+ "                    		 <div  >"
				+ "								<h4>Model Script</h4>"
				+ "								<textarea id='firstModelScript' name='firstModelScript'>"
				+ 									_firstModelScript
				+ "								</textarea>"
				+ "					 		 </div>"
				+ "                        </div>\n"
				+ "                        <div class='tab-pane fade' id='sub26'>\n"
				+ "                    		 <div >"
				+ "								<h4>Model Visualization Script</h4>"
				+ "								<textarea id='firstModelViz' name='firstModelViz'>"
				+ 									_firstModelViz 
				+ "								</textarea>" 
				+ "					 		 </div>" + "                        </div>\n"
				+ "                    </div>\n" + "                </div>\n"

		$('body').append(bodyContent);
		 /*
			 * let inputFile = $('#filesInput'); let button = $('#filesButton');
			 * let buttonSubmit = $('#uploadButton'); let filesContainer =
			 * $('#filesArea');
			 * 
			 * function readURL(input) { var reader = new FileReader();
			 * reader.onload = function(e) { console.log(e.target); path =
			 * e.target.result; files.push(path); } reader.readAsDataURL(input);
			 *  } inputFile.change(function() { let newFiles = []; for(let index =
			 * 0; index < inputFile[0].files.length; index++) { let file =
			 * inputFile[0].files[index]; newFiles.push(file);
			 * newFilesNames.push(file.name);
			 * 
			 * readURL(file);
			 * 
			 *  }
			 * 
			 * newFiles.forEach(file => { let fileElement = $(`<p>${file.name}</p>`);
			 * fileElement.data('fileData', file);
			 * filesContainer.append(fileElement);
			 * 
			 * fileElement.click(function(event) { let fileElement =
			 * $(event.target); let indexToRemove =
			 * files.indexOf(fileElement.data('fileData'));
			 * fileElement.remove(); files.splice(indexToRemove, 1); }); }); });
			 * 
			 * button.click(function() { inputFile.click(); });
			 * 
			 * buttonSubmit.click(function() { let formData = new FormData();
			 * 
			 * files.forEach(file => { formData.append('file', file); });
			 * 
			 * 
			 * $.ajax({ url: 'https://this_is_the_url_to_upload_to', data:
			 * formData, type: 'POST', success: function(data) {
			 * console.log('SUCCESS !!!'); }, error: function(data) {
			 * console.log('ERROR !!!'); }, cache: false, processData: false,
			 * contentType: false }); });
			 * 
			 * 
			 */
		  
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
																		readOnly:true,
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
			// console.log(err);
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

		$.each($("input[type='text']"), function(key, value) {

			$(value).removeAttr('class');
			$(value).addClass('form-control');
			$(value).parent().parent().removeAttr('class');
			$(value).parent().parent().addClass('form-group');
			fixInputCSS($(value));
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
			console.log(currentArea);

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

			console.log($(event.target).parent());
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
	}
	
}();