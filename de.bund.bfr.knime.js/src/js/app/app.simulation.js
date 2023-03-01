/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

class APPSimulation {
	constructor ( settings, $container ) {
        let O = this;
		
        // defaults maintable simulations modal
        O._$container = $container;
		O._opts = $.extend( true, {}, {
			classes : '',
			data 	: null,
			on 		: {
				afterInit 	: null, // function
				show 		: ( O, event ) => {
					O._updateModal( event );
				}, // function
				hide 		: null // function
			}
		}, settings );
        O._create();
    }
    get opts () {
		return this._opts;
	}
	set opts ( settings ) {
		this._opts = $.extend( true, {}, this.opts, settings );
	}
    /**
	 * CREATE
	 * calls super class and sets _metadata
	 */
	 
	_create () {
		let O = this;
		_log( 'SIM / _create', 'primary' );
		_log( O.opts );

		// global
		O._metadata = O.opts.data;
		O._state = 'params'; // default state: params form
		O._savedState = 'clean'

		O._$simInputs = []; // inputs from params and customs
		O._simFields = {};
		O._simSelectedIndex = 0; // initial simulation
    }
    _createSimulationContent () {
		let O = this;
		_log( 'panel SIM / _createSimulationContent' );

		// nav
		O._$modalNav = $( '<div class="modal-body sim-select"></div>' )
			.appendTo( O._$container );
        O._$globalValidation = $( '<ul class="col-12 errorMessages"></ul>' )
            .appendTo( O._$container );

		// navbar
		O._$navBar = $( '<nav class="navbar">' )
			.appendTo( O._$modalNav )
			.wrap( '<form></form>' ); 

		// sim select label
		O._$simSelectLabel = $( '<label class="col-12 col-md-3 sim-select-label" for="simulationSelect">Simulations</label>' )
			.appendTo( O._$navBar );

		// sim select counter
		O._$simSelectCounter = $( '<span class="badge badge-primary ml-1">x1</span>' )
			.appendTo( O._$simSelectLabel );

		// sim select
		O._$simSelect = $( '<select id="simulationSelect" class="custom-control custom-select"></select>' )
			.attr( 'id', 'simulationSelect' )
			.appendTo( O._$navBar )
			.wrap( '<div class="col-12 col-xs-auto col-md-4 sim-select-field"></div>' )
			.on( 'change', ( event ) => {
				let selectedIndex = O._$simSelect[0].selectedIndex;
				if ( selectedIndex >= 0 ) {
					O._updateSimIndex( selectedIndex );
				}
			} ); 

		// create actions
		O._createSimActions();

		// panel bodys

		// panel params
		O._$modalParams = $( '<div class="modal-body p-0 sim-params"></div>' )
			.appendTo( O._$container );
		// param content container
		O._$modalParams._$content = $( '<div class="tab-content h-100"></div>' )
			.appendTo( O._$modalParams );

		// panel execution
		O._$modalExecution = $( '<div class="modal-body p-0 sim-execution"></div>' )
			.appendTo( O._$container );
		// execution content container
		O._$modalExecution._$content = $( '<div class="tab-content h-100"></div>' )
			.appendTo( O._$modalExecution );
    }
    
	/**
	 * CREATE SIM ACTIONS
	 * creates actionss
	 */
	 
	_createSimActions() {
		let O = this;
		_log( 'panel SIM / _createSimActions' );

		// sim select actions
		O._$simSelectActions = $( '<div class="col-12 col-xs-auto col-md-5 mt-2 mt-xs-0 sim-select-actions"></div>' )
			.appendTo( O._$navBar );

		// create actions
		// action group 1
		let $actionGroup1 = $( '<div class="col-auto sim-select-actions-group"></div>' )
			.appendTo( O._$simSelectActions );

		// remove
		O._$simActionRemove = $( '<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-trash-2"></i></button>' )
			.attr( 'id', 'simActionRemove' )
			.attr( 'data-tooltip', '' )
			.attr( 'title', 'Remove simulation' )
			.appendTo( $actionGroup1 )
			.on( 'click', ( event ) => {
				O._removeSimulation();
			} );		
		
		// add
		O._$simActionAdd = $( '<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-plus"></i></button>' )
			.attr( 'id', 'simActionAdd' )
			.attr( 'data-tooltip', '' )
			.attr( 'title', 'Add simulation' )
			.appendTo( $actionGroup1 )
			.on( 'click', ( event ) => {
				O._addSimulation();
			} );	

		// save
		O._$simActionSave = $( '<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-save"></i></button>' )
			.attr( 'id', 'simActionSave' )
			.attr( 'data-tooltip', '' )
			.attr( 'title', 'Save changes' )
			.appendTo( $actionGroup1 )
			.on( 'click', ( event ) => {
				O._saveSimulation();
			} );	

		// col divider
		
        if(!window.noExecution){
            $( '<div class="col-divider ml-auto ml-xs-0"></div>' )
            .appendTo( O._$simSelectActions );

            // action group 2
            let $actionGroup2 = $( '<div class="col-auto sim-select-actions-group"></div>' )
            .appendTo( O._$simSelectActions );
            
    		O._$simActionRun = $( '<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-play"></i></button>' )
    			.attr( 'id', 'simActionRun' )
    			.attr( 'data-tooltip', '' )
    			.attr( 'title', 'Run simulation' )
    			.appendTo( $actionGroup2 )
    			.on( 'click', ( event ) => {
    				O._runModelView();
    			} );
        }

		O._$simSelectActions
			.wrapInner( '<div class="row justify-content-end align-items-center"></div>' );
	}


	/**
	 * CREATE PARAM METADATA LIST
	 * create table for metadata collapse container
	 * @param {array} param 
	 */
	 
	_createParamMetadataList( param ) {
		let O = this;
		_log( 'PANEL SIM / _createParamMetadataList' );

		let listData = {
			'ID'					: param.id,
			'Name'					: param.name,
			'Description' 			: param.description,
			'Unit'					: param.unit,
			'Unit category'			: param.unitCategory,
			'Data type'				: param.dataType,
			'Source'				: param.source,
			'Subject'				: param.subject,
			'Distribution'			: param.distribution,
			'Reference'				: param.reference,
			'Variability subject' 	: param.variabilitySubject,
			'Min value'				: param.minValue,
			'Max value'				: param.maxValue,
			'Error'					: param.error,
		};
		_log( listData );

		// create table
		let $table = $( '<table class="table table-sm table-hover table-params-metadata"></table>' );

		// create rows
		$.each( listData, ( name, value ) => {

			if ( value ) {
				let $row = $( '<tr></tr>')
					.appendTo( $table );
				$row.append( '<td class="td-label">'+ name +'</td>' ); // label/name
				$row.append( '<td>'+ value +'</td>' ); // value
			}
		} );

		return $table;
	}


	/**
	 * CREATE FORM FIELD
	 * create field as form group
	 * @param {array} param
	 */
	 
	_createFormField ( param ) {
		let O = this;
		_log( 'PANEL SIM / _createFormField' );
		_log( param );

		if ( param ) {

			// formgroup
			let $formGroup = $( '<div class="form-group row"></div>' );
				// .appendTo( O._$simForm );

			// label
			let $label = $( '<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>' )
				.attr( 'for', 'paramInput_'+ param.id )
				.appendTo( $formGroup );
			// set custom label or id
			if(O.paramsColorMap[param.id]){
				param._label ? $label.text( param._label ) : $label.text(O.paramsColorMap[param.id][1] );
				$label.css("background-color",O.paramsColorMap[param.id][0]);
			}
			else
				param._label ? $label.text( param._label ) : $label.text( param.id );
			// field
			let $field = $( '<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>' )
				.appendTo( $formGroup );

			// actions
			let $actions = $( '<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>' )
				.appendTo( $formGroup );

			// input item
			let $input = null;
			// set input type
			let inputType = null;
			if ( param.dataType.toLowerCase() === 'integer' 
				|| param.dataType.toLowerCase() === 'double' 
				|| param.dataType.toLowerCase() === 'number' ) {
				inputType = 'number';
			} 
			else if( param.dataType.toLowerCase() === 'simname' ) {
				inputType = 'simName'; // custom type for name and description
			}
			else if( param.dataType.toLowerCase() === 'simdescription' ) {
				inputType = 'simDescription'; // custom type for name and description
			}
			else if( param.dataType.toLowerCase() === 'vectorofnumbers' ) {
				inputType = 'vectorofnumbers';
			}
			else if( param.dataType.toLowerCase() === 'matrixofnumbers' ) {
				inputType = 'matrixofnumbers';
			}
			else if( param.dataType.toLowerCase() === 'file' ) {
				inputType = 'file';
			}
			else {
				inputType = 'text';
			}

			// create param metadata action
			if ( _isNull( param._showMetadata )
				|| param._showMetadata === true ) {
				// action metadata list
				let $actionMetadata = $( '<button class="action action-pure" type="button"><i class="feather icon-info"></i></button>' )
					.attr( 'data-toggle', 'collapse' )
					.attr( 'data-target', '#paramMetadata_'+ param.id )
					.attr( 'aria-expanded', false )
					.attr( 'aria-controls', 'paramMetadata_'+ param.id )
					.attr( 'title', 'Show Metadata' )
					.appendTo( $actions );
			}

			// add special action for vector or matrix of numbers
			if( inputType == 'vectorofnumbers' ) {

				let $actionVectoEditor = $( '<button class="action action-pure" type="button"><i class="feather icon-edit"></i></button>' )
					.attr( 'title', 'Edit Vector' )
					.appendTo( $actions );

				// TO DO
				// action what to do on click
			}
			else if( inputType == 'matrixofnumbers' ) {

				let $actionVectoEditor = $( '<button class="action action-pure" type="button"><i class="feather icon-edit"></i></button>' )
					.attr( 'title', 'Edit Matrix' )
					.appendTo( $actions );
					
				// TO DO
				// action what to do on click
			}



			if ( inputType ) {
			
				// numeric
				if ( inputType == 'number' && param.classification != "CONSTANT") {

					let $inputGroup = $( '<div class="input-group input-group-sm"></div>' )
						.appendTo( $field );
                    
					$input = $( '<input type="text" />' )
						.attr( 'id', 'paramInput_'+ param.id )
						.data( 'param-input', param ) 
						.attr( 'aria-invalid', false )
						.appendTo( $inputGroup );
					// rangeslider single, if min/max
					if ( param.minValue && param.maxValue  && param.minValue != "-" && param.maxValue != "-") {

						let step = 1;
						// calc decimals for slider steps depending on min-max values
						if ( param.dataType.toLowerCase() === 'double' ) {
							let decimals = param.value.substring(param.value.indexOf('.') + 1).length;
							for ( let j = 0; j < decimals; j++ ) {
								step = step / 10;
							}
						}

						// add rangeslider attributes
						$input
							.addClass( 'custom-range' )
							.attr( 'data-rangeslider', '' )
							.attr( 'data-step', step )
							.attr( 'data-min', parseFloat(param.minValue) ) // min value
							.attr( 'data-max', parseFloat(param.maxValue) ) // max value
							.attr( 'data-control-single', '#paramControlSingle_'+ param.id );

						// control input field for range value
						let $inputControl = $( '<input type="text" class="form-control" />' )
							.attr( 'id', 'paramControlSingle_'+ param.id )
							.data( 'param-input', param )  
							.appendTo( $field )
							.wrap( '<div class="input-range-controls"></div>' )
							.wrap( '<div class="input-group input-group-sm input-range-control-single"></div>' );

						// O._$simInputs.push( $inputControl );

						// add unit postfix to touchspin
						if ( param.unit && param.unit != '[]' && param.unit != 'Others' ) {
							let $append = $( '<div class="input-group-text"></div>' )
								.text( param.unit )
								.insertAfter( $inputControl )
								.wrap( '<div class="input-group-append"></div>' )
						}
					}
					// touchspin
					else {
						$input
							.addClass( 'form-control form-control-sm' )
							.attr( 'data-touchspin', '' );
						// add unit postfix to touchspin
						if ( param.unit && param.unit != '[]' && param.unit != 'Others' ) {
							$input.attr( 'data-touchspin-postfix', param.unit );
						}
						let step = 1;

						// calc decimals for slider steps depending on min-max values
						if ( param.dataType.toLowerCase() === 'double' ) {
							let decimals = param.value.substring( param.value.indexOf( '.' ) + 1 ).length;
							for ( let j = 0; j < decimals; j++ ) {
								step = step / 10;
							}
							// add decimals support
							if ( decimals > 0 ) {
								$input.attr( 'data-touchspin-decimals', decimals );
							}
						}
						// add step range
						$input.attr( 'data-touchspin-step', step );
					}
				}
				// sim name
				else if ( inputType == 'simName' ) {
					$input = $( '<input type="text" class="form-control form-control-sm" />' )
						.attr( 'id', 'customInput_'+ param.id )
						.data( 'custom-input', param )
						.appendTo( $field );

					O._$simNameInput = $input;
					
				}
				// sim description
				else if ( inputType == 'simDescription' ) {
					$input = $( '<textarea type="text" class="form-control form-control-sm" rows="6" /></textarea>' )
						.attr( 'id', 'customInput_'+ param.id )
						.data( 'custom-input', param ) 
						.appendTo( $field );

					O._$simDescInput = $input;
				}
				else if ( inputType == 'file' ) {
					if (window.location.protocol != '' && window.location.host != '') {
					    var timeStampInMs = window.performance && window.performance.now
								            && window.performance.timing
								            && window.performance.timing.navigationStart ? window.performance
								            .now()
								            + window.performance.timing.navigationStart : Date.now();
				        // send AJAX request to acquire the JWT for the currently logged in
				        // user. Subsequent requests need to carry the token in the
				        // “Authorization” header
				        
				        server = window.location.protocol + "//" + window.location.host
				        var xhttp = new XMLHttpRequest();
				
				        xhttp.onreadystatechange = function() {
				            if (this.readyState == 4 && this.status == 200) {
				                JWT = this.responseText;
				            }
				            // create temp folder for the current running instance of the
					        // worklflow on the server
					        // this folder will be removed after coping all the content inside
					        // to the fsk object working directory.
					        var anotherxhttp = new XMLHttpRequest();
					        window.parentResourcesFolder = "knime://knime.mountpoint/tempResources/jsSimulatorTempFolder"
					                + timeStampInMs;
					        anotherxhttp.open("put", server
					                + "/knime/rest/v4/repository/tempResources/jsSimulatorTempFolder"
					                + timeStampInMs, true);
					        anotherxhttp.setRequestHeader("Authorization", "Bearer" + JWT);
					        anotherxhttp.send();
				        };
				        xhttp.open("GET", server + "/knime/rest/session", true);
				        xhttp.send();
				        
			
			            // title
			            let $panel = $('<div class="tab-pane" role="tabpanel"></div>')
            						.appendTo( $field );
			            $input = $("<input id='filesInput' type='file' style='display:none' />");
			            
			            $input.attr( 'id', 'paramInput_'+ param.id )
							.data( 'param-input', param )  
							.appendTo($panel);
			            let button = $("<button id='filesButton' type='button' style='border-radius: 5px; background-color: #fff; color: green;'>Add File</button>")
			                .appendTo($panel);
			              
			            let filesContainer = $("<div id='filesArea-"+ param.id+"'></div>")
			                .appendTo($panel);
						if(param.value){
							let fileElement = $("<div ><hr/><p>"+  param.value +"</div>");
							filesContainer.html(fileElement);	
						}
				        
			           
				        let files = [];
				        let fileIDMap = {}
				        let fileUploadAJAXMap = {}
				    
				        $input
				            .change(function () {
				                let newFiles = [];
				                for (let index = 0; index < $input[0].files.length; index++) {
				                    let file = $input[0].files[index];
				                    newFiles.push(file);
				                    files.push(file);
				                }
				                for (let index = 0; index < newFiles.length; index++) {
				                    let file = newFiles[index];
				                    var ID = function () {
				                        return '_'
				                            + Math.random().toString(36).substr(2,
				                                9);
				                    }();
				                    let fileElement = $("<div ><hr/><p>"
				                        + file.name
				                        + "</p><progress id='"
				                        + ID
				                        + "' value='0' max='100' style='width:300px;'/><button idFile='"+ file.name+"' type='button' >delete</button></div>");
				    
				                    fileElement.data('fileData', file);
				                    filesContainer.html(fileElement);
				                    fileIDMap[file.name] = ID
				                    $("[idFile='"+file.name+"']").click(function (event) {
				                        let fileElement = $(event.target);
				                        let indexToRemove = files.indexOf(fileElement
				                            .data('fileData'));
				                        $.ajax({
				                            type: "DELETE",
				                            url: server
				                                + "/knime/rest/v4/repository/tempResources/jsSimulatorTempFolder"
				                                + timeStampInMs + "/"
				                                + $( this ).attr('idFile') + "?deletePermanently",
				                            
				                            success: function(msg){
				                            
				                            }
				                        });
				                        $( this ).parent().remove();
				                        //$("#" + fileIDMap[fileElement.html()]).remove()
				                        files.splice(indexToRemove, 1);
				                        fileUploadAJAXMap[$( this ).attr('idFile') ].abort();
				                        
				                    });
				                    fileUploadAJAXMap[file.name] = $
				                        .ajax({
				                            url: server
				                                + "/knime/rest/v4/repository/tempResources/jsSimulatorTempFolder"
				                                + timeStampInMs + "/"
				                                + file.name + ":data",
				                            xhr: function () {
				                                var myXhr = $.ajaxSettings.xhr();
				                                if (myXhr.upload) {
				                                    myXhr.upload
				                                        .addEventListener(
				                                            'progress',
				                                            function (e) {
				    
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
				                            headers: {
				                                Authorization: "Bearer" + JWT
				                            },
				                            data: file,
				                            type: 'put',
				                            success: function (data) {
				                                window.resourcesFiles[param.id] = ["knime://knime.mountpoint"
				                                        + data.path, ""];
				                            },
				                            error: function (data) {
				                                console.log('ERROR !!!', data);
				                            },
				                            cache: false,
				                            processData: false,
				                            contentType: false
				                        });
				                }
				    
				            });
				    
				        button.click(function() {
				            $input.trigger( "click" );
				        });
				    
    
						
					}else{
						$input = $( '<input type="text" class="form-control form-control-sm" />' )
						.attr( 'id', 'paramInput_'+ param.id )
						.data( 'param-input', param )  
						.appendTo( $field );
					}
				}
				// string or others
				else {
					$input = $( '<input type="text" class="form-control form-control-sm" />' )
						.attr( 'id', 'paramInput_'+ param.id )
						.data( 'param-input', param )  
						.appendTo( $field );
				}

				// readonly attribute
				param.classification === "CONSTANT" ? $input.attr( 'readonly', '' ) : null;

				// add $input to global variable
				O._$simInputs.push( $input );
				O._simFields[param.id] = {
					input 	: $input,
					param 	: param
				}
			}

			// create validation container
			$input.$validationContainer = $( '<div class="validation-message mt-1"></div>' )
				.appendTo( $field );

			// create param metadata list
			if ( _isNull( param._showMetadata ) 
				|| param._showMetadata === true ) {
				// metadata table
				let $metadataContainer = $( '<div class="collapse param-metadata"></div>' )
					.attr( 'id', 'paramMetadata_'+ param.id )
					.attr( 'aria-expanded', false )
					.appendTo( $field );

				$metadataContainer.append( O._createParamMetadataList( param ) );
			}
		
			return $formGroup;
		}

		return null;
	}



	/**
	 * POPULATE SIM SELECT
	 * create select options 
	 */
	 
	_populateSimSelect() {
		let O = this;
		_log( 'PANEL SIM / _populateSimSelect' );

		if ( O._simulations 
			&& O._simulations.length > 0 
			&& O._$simSelect ) {
			// clear sim select
			O._$simSelect.empty();
			// options
			$.each( O._simulations, ( i, sim ) => {
				if ( sim.name ) {
					let $option = $( '<option>'+ sim.name +'</option>' )
						.appendTo( O._$simSelect );
				}
			} );
			// update badge counter
			O._$simSelectCounter.text( 'x'+ O._simulations.length );
		} 
	}


	/**
	 * POPULATE SIMULATION FORM
	 * creates all input fields
	 */
	 
	_populateSimForm () {
		let O = this;
		_log( 'PANEL SIM / _populateSimForm' );

		// clear
		O._state == 'form'
		O._$simInputs = []; // stores all inputs in global var to provide access on params an custom fields like name and description
		O._simFields = {};
		O._selectedSimIndex = 0;
		O._$simForm ? O._clear( O._$simForm ) : null; // clear form


		// create form
		O._$simForm = $( '<form class="form-striped"></form>' )
			.attr( 'id', 'simParamsForm' )
			.appendTo( O._$modalParams._$content );

		// create mandatory custom form group sim name 
		let simNameParam = {
			id 				: 'simName',
			dataType 		: 'SIMNAME',
			_showMetadata 	: false,
			_label 			: 'Simulation Name',
			_isCustom		: true,
			_on 			: {
				update 			: ( O, $input ) => {
					O._updateSimName();
				}
			}
		};
		let $simNameFormGroup = O._createFormField( simNameParam );
		$simNameFormGroup ? $simNameFormGroup.appendTo( O._$simForm ) : null;

		// create optional custom form group sim description 
		let simDescParam = {
			id 				: 'simDescription',
			dataType 		: 'SIMDESCRIPTION',
			_showMetadata 	: false,
			_label 			: 'Description (optional)',
			_isCustom		: true,
			_on 			: {
				update 			: ( O, $input ) => {
					O._updateSimDescription();
				}
			}
		};
		let $simDescFormGroup = O._createFormField( simDescParam );
		$simDescFormGroup ? $simDescFormGroup.appendTo( O._$simForm ) : null;

		// model metadata params
		let params = O._modelMetadata['modelMath']['parameter']
		if ( params.length > 0 ) {
			// create form group for each param
			$.each( params, ( i, param ) => {
				if(O.paramsColorMap[param.id][2] ){
					// create model name title 
							// formgroup
					let $formGroup = $( '<div class="form-group row"></div>' );
		
					// label containing the model name
					let $label = $( '<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>' )
						.text(O.paramsColorMap[param.id][2] )
						.css("background-color",O.paramsColorMap[param.id][0])
						.appendTo( $formGroup );
						
					let $simDescFormGroup = O._createFormField( simDescParam );
					$formGroup ? $formGroup.appendTo( O._$simForm ) : null;
				}
				if ( param.classification != 'OUTPUT' ) {
					
					let $formGroup = O._createFormField( param );
					$formGroup ? $formGroup.appendTo( O._$simForm ) : null;
				}
				
			} );

			// init form items' functions: touchspin, range, select2 ...
			_appUI._initFormItems( O._$simForm );
		}
		_log( O._simFields );
	}


	/**
	 * UPDATE SIMULATION INDEX
	 * set selected simulation index and trigger update
	 */
	 
	_updateSimIndex ( simIndex ) {
		let O = this;
		_log( 'PANEL SIM / _updateSimIndex: '+ simIndex );


		simIndex = ! _isNull( simIndex ) ? simIndex : O._simSelectedIndex;

		if ( simIndex >= -1 
			&& simIndex != O._simSelectedIndex
			&& simIndex <= O._simulations.length - 1 ) {
			// update sim index
			O._simSelectedIndex = simIndex;

			O._setState( 'params' );

			// update inputs
			O._updateSimForm( simIndex );
			O._updateSimActions( simIndex );
		}
		
		// update badge counter
		O._$simSelectCounter.text( 'x'+ O._simulations.length );
	}


	/**
	 * UPDATE SIMULATION INPUTS
	 * changes the selected simulation's inputs
	 * @param {integer} simIndex of selected simulation: -1 ich add action, 0 = default
	 */
	 
	_updateSimForm ( simIndex ) {
		let O = this;
		_log( 'PANEL SIM / _updateSimForm: '+ simIndex );

		simIndex = ! _isNull( simIndex ) ? simIndex : O._simSelectedIndex;

		// set value if param or custom
		let valIndex = simIndex;

		if( simIndex < 0 ) {
			valIndex = 0;
		}	

		let simulation = O._simulations[valIndex];

		// disable parameter inputs for the default simulation.
		O._simDisabled = simIndex == 0;

		// remove error classes
		$( '.has-error' ).removeClass( 'has-error' );
		$( '.is-invalid' ).removeClass( 'is-invalid' );

		// update param values
		// $.each( O._$simInputs, ( i, $input ) => {

		$.each( O._simFields, ( id, field ) => {
			_log( field );
			if( field.input && field.param ) {

				// disable or enable 
				field.input.prop( 'disabled', O._simDisabled );

				// disable rangeslider 
				if ( field.input.data( 'rangeslider' ) ) {
					field.input.data( 'rangeslider' )
						.update( {
							disable : O._simDisabled
						} );
				}
				// disable touchspin
				else if ( ! _isUndefined( field.input.attr( 'data-touchspin' ) ) ) {
					// disable/enable buttons oof touchspin group
					field.input.parent()
						.find( 'button' )
						.prop( 'disabled', O._simDisabled );
				}
				// param fields
				if( ! field.param._isCustom ) {	
					// set value of current selected sim index
					_log( field.param.id +' : '+ simulation.parameters[field.param.id], 'level1' );
					let paramValue = simulation.parameters[field.param.id];

					// change rangeslider value
					if ( ! _isUndefined( $( field.input ).data( 'rangeslider' ) ) ) {
						let $inputControl = $( $( field.input ).data( 'control-single' ) );
						if ( $inputControl.length > 0 ) {
							$inputControl.val( paramValue );
							// trigger change
							$inputControl.trigger( 'change' );
						}
					}
					// change other inputs
					else {
					    if(field.param.dataType == 'FILE'){
							if (window.location.protocol != '' && window.location.host != '') {
								
								let paramId  = field.input.attr('id').split('_').pop();
								let filesContainer = $('#filesArea-Input_'+paramId);
								let fileElement = $("<div ><hr/><p>"+ paramValue +"</div>");
								
								
								filesContainer.html(fileElement);
							}else{
								! _isNull( paramValue ) ? field.input.val( paramValue ) : null;
							}
						}
						else{
							! _isNull( paramValue ) ? field.input.val( paramValue ) : null;
						}
					}
				}
				// custom fields like name and desc
				else if( field.param._isCustom && field.param._isCustom == true ) {

					_log( field.param.id +' : ', 'level1' );

					// check opt for custom update function 
					if( field.param._on 
						&& field.param._on.update 
						&& $.isFunction( field.param._on.update ) ) {
						field.param._on.update.call( this, O );
					}
				}

				// trigger change
				field.input.trigger( 'change' );
			}
		});
	}


	/**
	 * UPDATE SIMULATION ACTIONS
	 * changes the selected simulation's inputs
	 * @param {integer} simIndex of selected simulation
	 */

	 _updateSimActions( simIndex ) {
		let O = this;
		_log( 'PANEL SIM / _updateSimActions' );

		simIndex = ! _isNull( simIndex ) ? simIndex : O._simSelectedIndex;

		if ( simIndex == -1 ) { // add simulation
			O._$simActionRemove.prop( 'disabled', false );
			O._$simActionAdd.prop( 'disabled', true );
			O._$simActionSave.prop( 'disabled', false );
		}
		else if ( simIndex == 0 ) { // default sim
			O._$simActionRemove.prop( 'disabled', true );
			O._$simActionAdd.prop( 'disabled', false );
			O._$simActionSave.prop( 'disabled', true );
		}
		else { // selected sim > 0
			O._$simActionRemove.prop( 'disabled', false );
			O._$simActionAdd.prop( 'disabled', false );
			O._$simActionSave.prop( 'disabled', false );
		}
	}


	/**
	 * UPDATE SIMULATION CUTOM NAME
	 * changes the custom input name to selected simulation's name
	 */
	 
	_updateSimName() {
		let O = this;
		_log( 'PANEL SIM / _updateSimName' );

		if( O._$simNameInput ) {
			let simName = '';
			if( ! _isNull( O._simulations )
				&& ! _isNull( O._simSelectedIndex )
				&& O._simulations[O._simSelectedIndex] ) {
				// get simulation name
				simName = O._simulations[O._simSelectedIndex].name;
			}
			// set name
			O._$simNameInput.val( simName );
		}
	}


	/**
	 * UPDATE SIMULATION CUSTOM DESCRIPTUION
	 * changes the custom input description to selected simulation's name
	 * @param {jquery elem} $input
	 */
	 
	_updateSimDescription( $input ) {
		let O = this;
		_log( 'PANEL SIM / _updateSimDescription' );

		if( O._$simDescInput ) {
			let simDesc = '';
			if( ! _isNull( O._simulations )
				&& ! _isNull( O._simSelectedIndex )
				&& O._simulations[O._simSelectedIndex] ) {
				// get simulation name
				simDesc = O._simulations[O._simSelectedIndex].desc;
			}
			// set name
			O._$simDescInput.val( simDesc );
		}
	}


	/**
	 * ADD SIMULATION
	 * add the selected simulation
	 */
	 
	_addSimulation () {
		let O = this;
		_log( 'PANEL SIM / _addSimulation', 'secondary' );

		// clear sim select
		O._$simSelect.val( '' );
		
		// update sim select to index for add-action : -1
		O._updateSimIndex( -1 );
		O._setSavedState( 'dirty' );

	}


	/**
	 * REMOVE SIMULATION
	 * remove the selected simulation
	 */
	 
	_removeSimulation () {
		let O = this;
		_log( 'PANEL SIM / _removeSimulation', 'secondary' );

		if ( O._simSelectedIndex != 0 ) {
			// remove from sim select
			O._$simSelect.find( 'option' ).eq( O._simSelectedIndex ).remove();

			// remove from simulations var
			O._simulations.splice( O._simSelectedIndex, 1 );

			// reset to default
			O._$simSelect.find( 'option' ).eq( 0 ).prop( 'selected', true );
			O._updateSimIndex( 0 );
		}
	}


	/**
	 * SAVE SIMULATION
	 * save the selected simulation
	 */
	 
	async _saveSimulation () {
		let O = this;
		_log( 'PANEL SIM / _saveSimulation', 'secondary' );

		if(O._$simNameInput.val() == ""){
			O._$simNameInput.val("simulationname" + O._getRandomInt(100));
		}
		// run validation
		let validation = await O._validateSimForm();

		// no errors ?
		if( validation.length == 0 ) {

			let newSimulation = JSON.parse( JSON.stringify( O._simulations[0] ) );
			// set new simulation's name
			newSimulation.name = O._$simNameInput.val();
			// set sim desc
			newSimulation.desc = O._$simDescInput ? O._$simDescInput.val() : '';

			// set new simulation's parameters
			if( O._simFields ) {
				// for each input get value and et param value
				$.each( O._simFields, ( id, field ) => {

					if( field.input && ! _isNull( field.param ) ) {
						// create simulation param
						if(field.param.dataType.toLowerCase() != 'file')
							newSimulation.parameters[field.param.id] = field.input.val();

					}
				} );

				O._simulations.push( newSimulation );
				// re-populate sim select
				O._populateSimSelect();
				// update sim index
				O._$simSelect.find( 'option' ).last().prop( 'selected', true );
				O._updateSimIndex( O._simulations.length - 1 );
			}
		}
		
		for (const property in window.resourcesFiles) {
			
			window.resourcesFile[property] = $('#customInput_simName').val();
		}
		O._setSavedState( 'clean' );
		// TO DO 
		// save to endpoint ?
	}


	/**
	 * VALIDATE SIMULATION FORM
	 * run validation on sim form inputs
	 */
	 
	async _validateSimForm () {
		let O = this;
		_log( 'PANEL SIM / _validateSimForm' );
		O._$globalValidation.empty(); 

		let validationErrors = [];
		// remove error classes
		$( '.has-error' ).removeClass( 'has-error' );
		$( '.is-invalid' ).removeClass( 'is-invalid' );
		$( '.validation-message' ).empty();

		// validate sim inputs values
		if( O._simFields ) {

			$.each( O._simFields, ( id, field ) => {
				// let param = $input.data( 'param-input' );

				if( ! _isNull( field.param ) ) {
					let validationParam = O._validateSimField( field ) ;
					validationParam ? validationErrors.push( validationParam ) : null; 
				} 
			} );
		}

		// proceed error visualization
		$.each( validationErrors, ( i, error ) => {

			error.input.parents( '.form-group' ).addClass( 'has-error' );
			error.input.addClass( 'is-invalid' );

			error.input.$validationContainer.text( error.msg );
            
            $( '<li>'+error.msg+'</li>' )
                .appendTo( O._$globalValidation );
			// let $errorMsg = $( '<div class="alert alert-danger alert-xs"></div>' )
			// 	.appendTo( error.input.$validationContainer )
			// 	.text( error.msg );

		} );
        O._$globalValidation.show();

		_log( validationErrors );

		return validationErrors;
	}


	/**
	 * VALIDATE SIMULATION PARAM
	 * run validation on param field
	 */
	 
	_validateSimField ( field ) {
		let O = this;
		_log( 'PANEL SIM / _validateSimField' );
		_log( field );

		if( field && field.input ) {

			let fieldValue = field.input.val();

			// check simulation name
			if( field.param.dataType.toLowerCase() === 'simname' ) {

				let idRegexp = /^[A-Za-z_^s]\w*$/;

				// name length
				if( fieldValue.length == 0 ) {
					return {
						input 	: field.input,
						msg 	: 'Simulation name is required'
					};
				}
				// name already exists?
				for ( let i = 0; i < O._simulations.length; ++i ) {
					if ( fieldValue === O._simulations[i].name) {
						return {
							input 	: field.input,
							msg 	: 'Simulation name already exists'
						};
					}
				}
				// name fits regexp
				if ( ! idRegexp.test( fieldValue ) ) {
					return {
						input 	: field.input,
						msg 	: 'Not a valid simulation name (SId)'
					};
				}
			}
			else if( field.param.dataType.toLowerCase() === 'simdescription' ) {


			}
			else {

				// no value
				if( ! fieldValue ) {
					if ( field.param.dataType.toLowerCase() != 'file' ) {
						return {
							input  	: field.input,
							msg 	: 'Please provide a value for '+ field.param.id
						};
					}
				}

				// check range for integers and doubles
				if ( field.param.dataType.toLowerCase() === 'integer' 
					|| field.param.dataType.toLowerCase() === 'double' ) {
                    
                    if ( field.param.dataType.toLowerCase === 'integer' && fieldValue.indexOf('.') > 0 ){
                        return {
                                input   : field.input,
                                msg     : 'Invalid integer value'
                            };
                    }
					if ( field.param.minValue && parseFloat( field.param.minValue ) > fieldValue ) {
						return {
							input  	: field.input,
							msg 	: 'Invalid value. Value is lower than minimal value: '+ field.param.minValue
						};
					}
					if ( field.param.maxValue && parseFloat( field.param.maxValue ) < fieldValue ) {
						return {
							input  	: field.input,
							msg 	: 'Invalid value. Value is higher than maximum value: '+ field.param.maxValue
						};
					}
				}	
				else if ( field.param.dataType.toLowerCase() === 'file' ) {

					// TBD

				}
				else if ( field.param.dataType.toLowerCase() === 'vectorofnumbers' ) {

					// TBD

				}
				else if ( field.param.dataType.toLowerCase() === 'matrixofnumbers' ) {

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
	 
	async _updateContent(_modelMetadata, _modelId, _simulations, paramsColorMap) {
		let O = this;
		_log( 'PANEL SIM / _updateContent' );
		O._setState( 'params' ); // reset state to form params when opening PANEL

		O._modelMetadata = _modelMetadata;
		O._modelId = _modelId;
		// clear tab-panes
		O._clear( O._$modalParams._$content );
		
		O._simSelectedIndex = 0; // reset simulation
		// get simulations
		O._simulations = _simulations;
		O.paramsColorMap = paramsColorMap;

		// create params		
		if ( O._simulations && O._simulations.length > 0 ) {

			// populate sim select
			O._populateSimSelect();
			// populate sim form
			O._populateSimForm();
			// update form groups' value
			O._updateSimForm( 0 );
			O._updateSimActions( 0 );
		}
	}

	/**
	 * RUN MODEL VIEW
	 * run modelview
	 */
	 
	async _runModelView ( modelId ) {
		let O = this;
		_log( 'PANEL SIM / _runModelView : '+ O._modelId, 'secondary' );

		modelId = modelId || O._modelId;

		if ( modelId >= 0 ) {	

			O._fetchController = new AbortController();
			O._signal = O._fetchController.signal;

			// clear tab content
			O._clear( O._$modalExecution._$content );
			O._clear( O._$modalExecution._$title );
			O._setState( 'execution' );

			// activate loader
			O._$container.addClass( 'loading' );
			O._opts._loader._setState( true );
			// create loading alert
			let $alert = _appUI._createAlert( 
				'executing model... please wait',
				{
					type  	: 'primary',
					state 	: 'show',
					classes : 'm-1'
				},
				O._$modalExecution._$content
			);

			// TO DO 
			// run simulation by selected index

			// execute result
			let result = await _fetchData._content( _endpoints.execution, modelId, O._signal ); //O._app._getExecutionResult( modelId ) ;
			
			// clear tab content
			O._clear( O._$modalExecution._$content );
			O._clear( O._$modalExecution._$title );
			
			$alert.remove();

			// add executet simulation name as panel-title
			let $title = $( '<div class="panel-heading"></div>' )
				.text( O._simulations[O._selectedSimIndex].name )
				.appendTo( O._$modalExecution._$content );

			// add result as plot
			let $plot = $( result )
				.appendTo( O._$modalExecution._$content )
				.wrapAll( '<div class="panel-plot"></div>' );

			// deactivate loader
			O._$container.removeClass( 'loading' );
			O._opts._loader._setState( false );

			// callback
			if ( $.isFunction( O.opts.on.simRunModelView ) ) {
				O.opts.on.simRunModelView.call( O, O, modelId, O._simulations[O._selectedSimIndex] );
			}
		}
	}


	/**
	 * CLEAR
	 * removes content from element
	 * @param {jquery selector} $elem
	 */

	_clear ( $elem ) {
		let O = this;
		_log( 'PANEL SIM / _clear' );
		_log( $elem );

		if ( $elem ) {
			$elem.empty();
		}
    }
    
    /**
	 * CLEAR
	 * removes content from element
	 * @param {string} state: execute or params
	 */

	_setState ( state ) {
		let O = this;
		_log( 'PANEL SIM / _setState: '+ state );
		if ( state && O._state != state ) {

			if( state == 'execution' ) {
				O._$modalExecution.show();
				O._$modalParams.hide();
				O._$modalNav.hide();
			}
			else {
				O._$modalExecution.hide();
				O._$modalParams.show();
				O._$modalNav.show();
			}
			// set sim modal state
			O._state = state;
		}
	}
	_setSavedState ( state ) {
		// set sim modal state
		this._savedState = state;
	}
	
	_getRandomInt(max) {
	  return Math.floor(Math.random() * max);
	}
}