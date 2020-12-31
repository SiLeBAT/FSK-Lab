fskdbview = function () {
	
    var view = {
        version: "0.0.1"
    };
    view.name = "FSK DB View"
    //transform JQuery :contains selector to case insensitive
    jQuery.expr[':'].contains = function (a, i, m) {
        return jQuery(a).text().toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
    };
    window.selectedModels = [];
    window.downloadURs = [];
    let _endpoint;
    let _globalVars = {};

    // These sets are used with the th-filters
    let _lazySriptsfetching = false;
    
    let _representation;
    let _value;
    var selectionEdited = function (modelMetaDatax) {
        console.log(modelMetaDatax);
        let edittedModelId = modelMetaDatax.changeSet.added[0].generalInformation.name;
        
        let modelIndex = -1;

        $.each(window.selectedModels,function(index,model){
                if(model.generalInformation.name === edittedModelId){
                    modelIndex = index;
                }
        });
        window.selectedModels[modelIndex]=modelMetaDatax.changeSet.added[0];
        knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' , [window.selectedModels, window.downloadURs],{elements:[]});
    }
                               
    view.init = function (representation, value) { 
        _representation = representation;
        knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21GlobalInit' , [{"tableID":_representation.tableID}],{elements:[]})
        _value = value;
        parent.tableID = _representation.tableID;
        //subscribe to events emitted by editor
        knimeService.subscribeToSelection('b800db46-4e25-4f77-bcc6-db0c21EditorSaved', selectionEdited);

        _endpoint = _representation.remoteRepositoryURL ? _representation.remoteRepositoryURL : "https://knime.bfr.berlin/backend/";
        _globalVars = {
            metadataEndpoint: _endpoint + "metadata",
            imageEndpoint: _endpoint + "image/",
            downloadEndpoint: _endpoint + "download/",
            modelscriptEndpoint: _endpoint + "modelscript/",
            visualizationscriptEndpoint: _endpoint + "visualizationscript/",
            simulationsEndpoint: _endpoint + "simulations/"
        }
        createDBViewUI();
    };
    
    view.getComponentValue = function () {
        _value.table = _representation.table;
        return _value;
    };

    view.validate = function () {
        return true;
    }

    async function createDBViewUI(){
        const metadata = await getMetadata();
        _representation.metadata = metadata;
        $(document).ready(() => {
            let rootDiv = $('<div />').appendTo('body');
            _app = new APPLandingpage(_appVars, rootDiv, metadata);
        });
    }
   
    /**
     * Get a metadata property or return empty string if missing.
     * @param {object} modelMetadata Whole metadata of a model
     * @param {string} toplevel Name of the metadata component. It can be
     *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
     * @param {string} name Name of the metadata property 
     */
    function getData(modelMetadata, toplevel, name) {
        try {
            return modelMetadata[toplevel][name];
        } catch (err) {
            return "no information for " + name;
        }
    }
   
    async function create_UUID(){
        var dt = new Date().getTime();
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = (dt + Math.random()*16)%16 | 0;
            dt = Math.floor(dt/16);
            return (c=='x' ? r :(r&0x3|0x8)).toString(16);
        });
        return uuid;
    }
   
    async function getMetadata() {
        if (_representation.table && _representation.table.rows && _representation.table.rows.length > 0) {
            const j = [];

            $.each(_representation.table.rows, function (index, rawdata) {
                var json = JSON.parse(rawdata.data[0]);
                var metaData = json[0];
                metaData['modelType'] = "genericModel";
                j.push(metaData);
            });
            return j;
        } else {
            parent.tableID = await create_UUID();
            _representation.tableID = parent.tableID;
            const rep = await fetch(_globalVars.metadataEndpoint);
            const j = await rep.json();
            
            for (index = 0; index < j.length; index++) {
                let row = j[index];
                try {
                    _lazySriptsfetching = true;
                    const simulations = await fetch(_globalVars.simulationsEndpoint + index);
                    simulations.json().then(function(data) {
                        row['simulation'] = data; // this will be a JSON
                    });
                    
                } catch (err) {
                    console.log(err);
                }

                if (_representation.table && _representation.table.rows) {
                    _representation.table.rows.push({
                        data: [
                            JSON.stringify(row)
                        ],
                        rowKey: 'Row'+index+'#'
                    });
                }
            }
            //postTableBuilt(_representation.table.rows);
            return j;
        }
    }
    var _appVars = {
			header 				: false,
			mainTable 			: {
				rowSelectable 	: 'multiple',
				rowActions 		: [
					
					{
						type 			: 'link',
						idPrefix 		: 'mtActionEdit_',
						icon			: 'icon-edit-2',
						title 			: 'Edit',
						on 				: {
							click 			: ( O, $action, modelIndex, rowData ) => {
								_log( 'on > clickEdit', 'hook' ); // example hook output
								_log( O );
								_log( $action );
								_log( modelIndex );
                                _log( rowData );
                                // emit selection event
                                let selectedModel = rowData.modelMetadata;
                                //fetch scripts
                                if(_lazySriptsfetching){
                                    const modelscript =  fetch(_globalVars.modelscriptEndpoint + modelIndex);
                                    modelscript.then(function(response) {
                                        return response.text();
                                    }).then(function(data) {
                                        selectedModel['modelscript'] = data; // this will be a string
                                        const visualizationscript =  fetch(_globalVars.visualizationscriptEndpoint + modelIndex);
                                        visualizationscript.then(function(responsevis) {
                                            return responsevis.text();
                                        }).then(function(datavis) {
                                            selectedModel['visualization'] = datavis; // this will be a string
                                            window.downloadURs.push(_globalVars.downloadEndpoint+modelIndex);
                                            knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c215846e1' , [selectedModel],{elements:[]}) 
                                        });
                                    });
                                    
                                }else{
                                    knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c215846e1' , [selectedModel],{elements:[]})
                                }
							}
						}
					}
				],
				cols 			: [
					{
						id 			: 'colModel',
						label		: 'Model',
						field 		: 'modelName',
						classes 	: {
							th 			: 'td-label min-200',
							td 			: 'td-label min-200 td-model'
						},
						sortable 	: true, // sortable
					},
					{
						id 			: 'colSoftware',
						label		: 'Software',
						field 		: 'software',
						classes 	: {
							th 			: null,
							td 			: 'td-soft'
						},
						sortable 	: true, // sortable
						facet 		: {
							tooltip 			: true,
							select2 			: true,
							select2SingleRow 	: true,
							placeholder 		: 'Software',
							maxSelectable 		: 1
						}
					},
					{
						id 			: 'colEnvironment',
						label		: 'Environment',
						field 		: 'environment',
						classes 	: {
							th 			: 'min-300',
							td 			: 'td-env min-300'
						},
						sortable 	: true, // sortable
						facet 		: {
							tooltip 			: true,
							select2 			: true,
							select2SingleRow 	: true,
							placeholder 		: 'Environment',
							maxSelectable 		: 1
						},
						collapsable	: true, // data-toggle-td
						formatter 	: '_list' // _formatter subroutine
					},
					{
						id 			: 'colHazard',
						label		: 'Hazard',
						field 		: 'hazard',
						classes 	: {
							th 			: null,
							td 			: 'td-haz'
						},
						sortable 	: true, // sortable
						facet 		: {
							tooltip 			: true,
							select2 			: true,
							select2SingleRow 	: true,
							placeholder 		: 'Hazard',
							maxSelectable 		: 1
						}
					},
					{
						id 			: 'colType',
						label		: 'Type',
						field 		: 'modelType',
						classes 	: {
							th 			: null,
							td 			: 'td-type'
						},
						sortable 	: true, // sortable
						facet 		: {
							tooltip 			: true,
							select2 			: true,
							select2SingleRow 	: true,
							placeholder 		: 'Type',
							maxSelectable 		: 1
						}
					},
				],
				on 				: {			
					afterInit 		: ( O ) => {
						_log( 'on > afterInit', 'hook' ); // example hook output
						_log( O );
					},
					afterPopulate 	: ( O, tableData ) => {
						_log( 'on > afterPopulate', 'hook' ); // example hook output
						_log( O );
						_log( tableData );
					},
					selectRow 		: ( O, rowIndex, rowData ) => {
                            
                            if (window.selectedModels.length >= _representation.maxSelectionNumber) {
                                $(this).prop("checked", false);
                                return;
                            }
                            this.checked = true;
                            $(this).closest("tr").css("background-color", "#e1e3e8");
                            //fetch scripts
                            if(_lazySriptsfetching){
                                const modelscript =  fetch(_globalVars.modelscriptEndpoint + rowIndex);
                                modelscript.then(function(response) {
                                    return response.text();
                                }).then(function(data) {
                                    _representation.metadata[rowIndex]['modelscript'] = data; // this will be a string
                                    const visualizationscript =  fetch(_globalVars.visualizationscriptEndpoint + rowIndex);
                                    visualizationscript.then(function(responsevis) {
                                        return responsevis.text();
                                    }).then(function(datavis) {
                                        _representation.metadata[rowIndex]['visualization'] = datavis; // this will be a string
                                        // save selected model
                                        window.selectedModels.push(_representation.metadata[rowIndex]);
                                        _value.selection.push(_representation.table.rows[rowIndex].rowKey);
                                        // emit selection event
                                        window.downloadURs.push(_globalVars.downloadEndpoint+rowIndex);
                                        knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' , [window.selectedModels,window.downloadURs],{elements:[]})  
                                    });
                                });
                                
                            }else{
                                console.log(rowData);
                                // save selected model
                                window.selectedModels.push(rowData.modelMetadata);
                                _value.selection.push(rowIndex);
                                // emit selection event
                                knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' , [window.selectedModels],{elements:[]})  
                            }
                        

                        
					},
					deselectRow 	: ( O, rowIndex, rowData ) => {
						_log( 'on > deselectRow', 'hook' ); // example hook output
						_log( O );
						_log( rowIndex );
                        _log( rowData );
                        this.checked = false;
                        $(this).closest("tr").css("background-color", "transparent");
                        //filter out the model if the Checkbox is unchecked
                        window.selectedModels = window.selectedModels.filter(function (value, index, arr) {
                            let selectedModelID = getData(_representation.metadata[rowIndex], "generalInformation", "identifier")
                            let currentModelId = getData(value, "generalInformation", "identifier");
                            return selectedModelID != currentModelId;
                        });
                        
                        _value.selection = _value.selection.filter(function (value, index, arr) {
                            return value != rowIndex;
                        });
                        knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' , [window.selectedModels,window.downloadURs],{elements:[]});
                          
					},
					updateFilter 	: ( O, filtered ) => {
						_log( 'on > updateFilter', 'hook' ); // example hook output
						_log( O );
						_log( filtered );
					}
				}
			}
		};
    return view;
}();