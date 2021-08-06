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
    let selectionMap = {};
    let selectedModelIndex = -1;
    window.downloadURs = [];
    window._endpoint;
    var _WebRepositoryVars = {};
    var _appVars = {};
    let uploadDates =[];
    let executionTimes = [];
    let _app;
    // These sets are used with the th-filters
    let _lazySriptsfetching = false;
    let editorAvailable = false;
    let _representation;
    let _value;
    let initiated = function(){
        editorAvailable =  true;
    }
     var selectionEdited = function (modelMetaDatax) {
        if(!modelMetaDatax.changeSet.added)
            return;
        let selectedEditedModel = JSON.parse(JSON.stringify(modelMetaDatax.changeSet.added[0]));
        if(window.selectedModels && selectionMap[selectedModelIndex] != undefined && window.selectedModels[selectionMap[selectedModelIndex]]){        
            window.selectedModels[selectionMap[selectedModelIndex]] = selectedEditedModel;
            knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' , [{"selecteModels":window.selectedModels, "downloadURs":window.downloadURs}],{elements:[]});
        }
        _app._mainTable._metadata[selectedModelIndex] = selectedEditedModel;
        _app._mainTable._refresh(selectedModelIndex, selectedEditedModel);
    }
                               
    view.init = function (representation, value) { 
        
        knimeService.subscribeToSelection('b800db46-4e25-4f77-bcc6-db0c21GlobalEditor', initiated);
        _representation = representation;
        knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21GlobalInit' , [{"tableID":_representation.tableID}],{elements:[]})
        _value = value;
        parent.tableID = _representation.tableID;
        //subscribe to events emitted by editor
        knimeService.subscribeToSelection('b800db46-4e25-4f77-bcc6-db0c21EditorSaved', selectionEdited);

        window._endpoint = _representation.remoteRepositoryURL ? _representation.remoteRepositoryURL : "https://knime.bfr.berlin/backend/";
        window._endpoints 	= {
			metadata		: window._endpoint + 'metadata/',
			image			: window._endpoint + 'image/',
			download		: window._endpoint + 'download/',
			uploadDate		: window._endpoint + 'uploadDate/',
			executionTime	: window._endpoint + 'executionTime/',
			simulations		: window._endpoint + 'simulations/',
			execution 		: window._endpoint + 'execute/',
			search 			: window._endpoint + 'search/',
			filter 			: window._endpoint + 'filter',
			modelscriptEndpoint: window._endpoint + "modelscript/",
            visualizationscriptEndpoint: window._endpoint + "visualizationscript/",
            simulationsEndpoint: window._endpoint + "simulations/"
		};
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
		// add Page Loader animation
		let loader = {};
		loader._$el = $( '<div class="loader loading"></div>' ).appendTo('body');
		loader._$el.addClass( 'loading' );
        const metadata = await getMetadata();
        _representation.metadata = metadata;
        
        $(document).ready(() => {
            let rootDiv = $('<div />').appendTo('body');
            createVars();
            let webRepositoryFlag = false;
            if( _representation.showHeaderButtonChecked == "true" ||
                _representation.showDetailsButtonChecked == "true" || 
                _representation.showExecuteButtonChecked == "true" || 
                _representation.showDownloadButtonChecked == "true" ){
                    
                webRepositoryFlag = true;
                
            }
            if(webRepositoryFlag && _representation.table && _representation.table.rows && _representation.table.rows.length > 0){
                _app = new APPLandingpage(_WebRepositoryVars, rootDiv, metadata, uploadDates, executionTimes);
            }else{
                _app = new APPLandingpage(_appVars, rootDiv, metadata);
            }
			// remove page loader animation 
			loader._$el.removeClass( 'loading' );
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
                uploadDates.push(metaData['uploadDate']?metaData['uploadDate']:" | ")
                executionTimes.push(metaData['executionTime']?metaData['executionTime']:"")
                metaData['modelType'] = "genericModel";
                j.push(metaData);
            });
            return j;
        } else {
            parent.tableID = await create_UUID();
            _representation.tableID = parent.tableID;
            const rep = await fetch(window._endpoints.metadata);
            const j = await rep.json();
            _lazySriptsfetching = true;
            for (index = 0; index < j.length; index++) {
                let row = j[index];
                

                if (_representation.table && _representation.table.rows) {
                    _representation.table.rows.push({
                        data: [
                            JSON.stringify(row)
                        ],
                        rowKey: 'Row'+index+'#'
                    });
                }
            }
			
            return j;
        }
    }
    function createVars(){
       _WebRepositoryVars = {
        header          : {
            brand           : {
                title           : _representation.title // false or ''
            },
            nav             : $.parseJSON(_representation.sandwichList)
        },
        mainTable           : {
            rowSelectable   : 'multiple',
            cols            : [
                {
                    id          : 'colModel',
                    label       : 'Model',
                    field       : 'modelName',
                    classes     : {
                        th          : 'td-label min-200',
                        td          : 'td-label min-200 td-model'
                    },
                    sortable    : true, // sortable
                },
                {
                    id          : 'colSoftware',
                    label       : 'Software',
                    field       : 'software',
                    classes     : {
                        th          : null,
                        td          : 'td-soft'
                    },
                    sortable    : true, // sortable
                    facet       : {
                        tooltip             : true,
                        select2             : true,
                        select2SingleRow    : true,
                        placeholder         : 'Software',
                        maxSelectable       : 1
                    }
                },
                {
                    id          : 'colEnvironment',
                    label       : 'Environment',
                    field       : 'environment',
                    classes     : {
                        th          : 'min-300',
                        td          : 'td-env min-300'
                    },
                    sortable    : true, // sortable
                    facet       : {
                        tooltip             : true,
                        select2             : true,
                        select2SingleRow    : true,
                        placeholder         : 'Environment',
                        maxSelectable       : 1
                    },
                    collapsable : true, // data-toggle-td
                    formatter   : '_list' // _formatter subroutine
                },
                {
                    id          : 'colHazard',
                    label       : 'Hazard',
                    field       : 'hazard',
                    classes     : {
                        th          : null,
                        td          : 'td-haz'
                    },
                    sortable    : true, // sortable
                    facet       : {
                        tooltip             : true,
                        select2             : true,
                        select2SingleRow    : true,
                        placeholder         : 'Hazard',
                        maxSelectable       : 1
                    }
                },
                {
                    id          : 'colType',
                    label       : 'Type',
                    field       : 'modelType',
                    classes     : {
                        th          : null,
                        td          : 'td-type'
                    },
                    sortable    : true, // sortable
                    facet       : {
                        tooltip             : true,
                        select2             : true,
                        select2SingleRow    : true,
                        placeholder         : 'Type',
                        maxSelectable       : 1
                    }
                },
                {
                    id          : 'colExecTime',
                    label       : 'Execution Time',
                    field       : 'executionTime',
                    classes     : {
                        th          : null,
                        td          : null
                    },
                    sortable    : true, // sortable
                    sorter      : '_execution', // _sorter subroutine
                    formatter   : '_execution' // _formatter subroutine
                },
                {
                    id          : 'colUploadDate',
                    label       : 'Upload Date',
                    field       : 'uploadDate',
                    classes     : {
                        th          : null,
                        td          : null
                    },
                    sortable    : true, // sortable
                    formatter   : '_uploadDate' // _formatter subroutine
                }
            ],
            rowActions      : [],
            on              : {         
                afterInit       : ( O ) => {
                    _log( 'on > afterInit', 'hook' ); // example hook output
                    _log( O );
                },
                create          : ( O ) => {
                    // create details modal
                    O._modalDetails = new APPModalMTDetails( {
                        data        : O._metadata,
                        id          : 'mtModalDetails',
                        classes     : 'modal-details',
                        type        : 'mtDetails'
                    }, O._$container );
                    _log( O._modalDetails );
    
                    // create simulations modal
                    O._modalSim = new APPModalMTSimulations( {
                        data        : O._metadata,
                        id          : 'mtModalSim',
                        classes     : 'modal-sim',
                        type        : 'mtSim',
                        on          : {
                            simRunModelView : ( O, modelId, simulation )=> {
                                _log( 'on > simRunModelView', 'hook' ); // example hook output
                                _log( O );
                                _log( modelId );
                                _log( simulation );
                            }
                        }
                    }, O._$container );
                    _log( O._modalSim );
                },
                afterPopulate   : ( O, tableData ) => {
                    _log( 'on > afterPopulate', 'hook' ); // example hook output
                    _log( O );
                    _log( tableData );
                    if(editorAvailable){
                        $('button[role="mtActionEdit"]').each(function() {
                           $(this).attr('hidden',false );
                        });
                        $('button[role="mtActionDetails"]').each(function() {
                           $(this).attr('hidden',true );
                        });
                    }
                },
                selectAllRow       : ( O, rowIndex, rowData ) => {
                    this.checked = true;
                    $(this).closest("tr").css("background-color", "#e1e3e8");
                    window.selectedModels.push(_representation.metadata[rowIndex]);
                    _value.selection.push(_representation.table.rows[rowIndex].rowKey);
                    return;
           
                },
                selectRow       : ( O, rowIndex, rowData ) => {
                            if(O.selectAllClicked){
                                    this.checked = true;
                                    $(this).closest("tr").css("background-color", "#e1e3e8");
                                    window.selectedModels.push(_representation.metadata[rowIndex]);
                                    _value.selection.push(_representation.table.rows[rowIndex].rowKey);
                                    return;
                            }
                            if (window.selectedModels.length >= _representation.maxSelectionNumber) {
                                    $(this).prop("checked", false);
                                    return;
                            }
                            
                            this.checked = true;
                            $(this).closest("tr").css("background-color", "#e1e3e8");
                            //fetch scripts
                            if(!_representation.metadata[rowIndex]['modelscript']){
                                const simulations = fetch(window._endpoints.simulationsEndpoint + rowIndex);
                                simulations.then(function(response) {
                                    return response.json();
                                }).then(function(data) {
                                    _representation.metadata[rowIndex]['simulation'] = data; // this will be a json
                                    const modelscript =  fetch(window._endpoints.modelscriptEndpoint + rowIndex);
                                    modelscript.then(function(response) {
                                        return response.text();
                                    }).then(function(data) {
                                        _representation.metadata[rowIndex]['modelscript'] = data; // this will be a string
                                        const visualizationscript =  fetch(window._endpoints.visualizationscriptEndpoint + rowIndex);
                                        visualizationscript.then(function(responsevis) {
                                            return responsevis.text();
                                        }).then(function(datavis) {
                                            _representation.metadata[rowIndex]['visualization'] = datavis; // this will be a string
                                            // save selected model
                                            selectionMap[rowIndex] = window.selectedModels.length;
                                            window.selectedModels.push(_representation.metadata[rowIndex]);
                                            _value.selection.push(_representation.table.rows[rowIndex].rowKey);
                                            // emit selection event
                                            window.downloadURs.push(window._endpoints.download+rowIndex);
                                            knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' ,
                                                    [{"selecteModels":window.selectedModels, "downloadURs":window.downloadURs}]
                                            /* [window.selectedModels,window.downloadURs]*/,{elements:[]}) 
                                        });
                                    });
                                });
                            }else{
                                // save selected model
                                selectionMap[rowIndex] = window.selectedModels.length;
                                window.selectedModels.push(_representation.metadata[rowIndex]);
                                _value.selection.push(_representation.table.rows[rowIndex].rowKey);
                                // emit selection event
                                window.downloadURs.push(window._endpoints.download+rowIndex);
                                knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' ,
                                        [{"selecteModels":window.selectedModels, "downloadURs":window.downloadURs}]
                                /* [window.selectedModels,window.downloadURs]*/,{elements:[]}) 
                            }
                        

                        
                    },
                    deselectRow     : ( O, rowIndex, rowData ) => {
                        _log( 'on > deselectRow', 'hook' ); 
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
                            let indexToBeSelected = value.replace("Row","").replace("#","");
                            return indexToBeSelected != rowIndex;
                        }); 
                        knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' ,
                                [{"selecteModels":window.selectedModels, "downloadURs":window.downloadURs}],{elements:[]});
                        delete selectionMap[rowIndex];
                          
                    },
                updateFilter    : ( O, filtered ) => {
                    _log( 'on > updateFilter', 'hook' ); // example hook output
                    _log( O );
                    _log( filtered );
                }
            }
        }
    };
    if(_representation.showHeaderButtonChecked == "true"){
        _WebRepositoryVars.header.brand.logo = 'js-lib/bfr/fskapp/img/RAKIP_logo.jpg' ; // or  'js-lib/bfr/fskapp/img/bfr_logo.gif'
    }
    
    if(_representation.showDetailsButtonChecked == "true"){
        _WebRepositoryVars.mainTable.rowActions.push({
                    type            : 'modal',
                    idPrefix        : 'mtActionDetails_',
                    role            : "mtActionDetails",
                    icon            : 'icon-eye',
                    title           : 'Details',
                    target          : '#mtModalDetails'
                });
    }
    if(_representation.showExecuteButtonChecked == "true"){
        _WebRepositoryVars.mainTable.rowActions.push({
                    type            : 'modal',
                    idPrefix        : 'mtActionSim_',
                    role            : "mtActionSim",
                    icon            : 'icon-play',
                    title           : 'Simulation',
                    target          : '#mtModalSim'
                });
    }
    if (_representation.showDownloadButtonChecked == "true") {
        _WebRepositoryVars.mainTable.rowActions.push({
            type: 'link',
            idPrefix: 'mtActionDownload_',
            icon: 'icon-download',
            title: 'Download',
            on: {
                click: (O, $action, rowIndex, rowData) => {
                    window.open(_endpoints.download + rowIndex, '_blank');
                }
            }
        });
    }


    _WebRepositoryVars.mainTable.rowActions.push({
        type: 'link',
        idPrefix: 'mtActionEdit_',
        role: "mtActionEdit",
        icon: 'icon-edit-2',
        hidden: true,
        title: 'Edit',
        on: {
            click: (O, $action, modelIndex, rowData) => {
                _log('on > clickEdit', 'hook');
                _log(O);
                _log($action);
                _log(modelIndex);
                _log(rowData);
                selectedModelIndex = modelIndex;
                // emit selection event
                let selectedModel = rowData.modelMetadata;
                //fetch scripts
                if (_lazySriptsfetching) {
                    const modelscript = fetch(window._endpoints.modelscriptEndpoint + modelIndex);
                    modelscript.then(function(response) {
                        return response.text();
                    }).then(function(data) {
                        selectedModel['modelscript'] = data; // this will be a string
                        const visualizationscript = fetch(window._endpoints.visualizationscriptEndpoint + modelIndex);
                        visualizationscript.then(function(responsevis) {
                            return responsevis.text();
                        }).then(function(datavis) {
                            selectedModel['visualization'] = datavis; // this will be a string
                            knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c215846e1', [selectedModel], { elements: [] })
                        });
                    });

                } else {
                    knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c215846e1', [selectedModel], { elements: [] })
                }
            }
        }
    });
    
    
    _appVars = {
            header              : false,
            mainTable           : {
                rowSelectable   : 'multiple',
                rowActions      : [
                    
                    {
                        type            : 'link',
                        idPrefix        : 'mtActionEdit_',
                        role: "mtActionEdit",
                        icon: 'icon-edit-2',
                        hidden: true,
                        title           : 'Edit',
                        on              : {
                            click           : ( O, $action, modelIndex, rowData ) => {
                                _log( 'on > clickEdit', 'hook' ); 
                                _log( O );
                                _log( $action );
                                _log( modelIndex );
                                _log( rowData );
                                selectedModelIndex = modelIndex;
                                // emit selection event
                                let selectedModel = rowData.modelMetadata;
                                //fetch scripts
                                if(_lazySriptsfetching){
                                    const modelscript =  fetch(window._endpoints.modelscriptEndpoint + modelIndex);
                                    modelscript.then(function(response) {
                                        return response.text();
                                    }).then(function(data) {
                                        selectedModel['modelscript'] = data; // this will be a string
                                        const visualizationscript =  fetch(window._endpoints.visualizationscriptEndpoint + modelIndex);
                                        visualizationscript.then(function(responsevis) {
                                            return responsevis.text();
                                        }).then(function(datavis) {
                                            selectedModel['visualization'] = datavis; // this will be a string
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
                cols            : [
                    {
                        id          : 'colModel',
                        label       : 'Model',
                        field       : 'modelName',
                        classes     : {
                            th          : 'td-label min-200',
                            td          : 'td-label min-200 td-model'
                        },
                        sortable    : true, // sortable
                    },
                    {
                        id          : 'colSoftware',
                        label       : 'Software',
                        field       : 'software',
                        classes     : {
                            th          : null,
                            td          : 'td-soft'
                        },
                        sortable    : true, // sortable
                        facet       : {
                            tooltip             : true,
                            select2             : true,
                            select2SingleRow    : true,
                            placeholder         : 'Software',
                            maxSelectable       : 1
                        }
                    },
                    {
                        id          : 'colEnvironment',
                        label       : 'Environment',
                        field       : 'environment',
                        classes     : {
                            th          : 'min-300',
                            td          : 'td-env min-300'
                        },
                        sortable    : true, // sortable
                        facet       : {
                            tooltip             : true,
                            select2             : true,
                            select2SingleRow    : true,
                            placeholder         : 'Environment',
                            maxSelectable       : 1
                        },
                        collapsable : true, // data-toggle-td
                        formatter   : '_list' // _formatter subroutine
                    },
                    {
                        id          : 'colHazard',
                        label       : 'Hazard',
                        field       : 'hazard',
                        classes     : {
                            th          : null,
                            td          : 'td-haz'
                        },
                        sortable    : true, // sortable
                        facet       : {
                            tooltip             : true,
                            select2             : true,
                            select2SingleRow    : true,
                            placeholder         : 'Hazard',
                            maxSelectable       : 1
                        }
                    },
                    {
                        id          : 'colType',
                        label       : 'Type',
                        field       : 'modelType',
                        classes     : {
                            th          : null,
                            td          : 'td-type'
                        },
                        sortable    : true, // sortable
                        facet       : {
                            tooltip             : true,
                            select2             : true,
                            select2SingleRow    : true,
                            placeholder         : 'Type',
                            maxSelectable       : 1
                        }
                    },
                ],
                on              : {         
                    afterInit       : ( O ) => {
                        _log( 'on > afterInit', 'hook' ); 
                        _log( O._tableData );
                    },
                    afterPopulate   : ( O, tableData ) => {
                        _log( 'on > afterPopulate', 'hook' ); 
                        _log( O );
                        _log( tableData );
                        if(editorAvailable){
                            $('button[role="mtActionEdit"]').each(function() {
                               $(this).attr('hidden',false );
                            });
                            $('button[role="mtActionDetails"]').each(function() {
                               $(this).attr('hidden',true );
                            });
                        }
                        let tbl = O._tableData;
                        $.each(_representation.selection,function(index,value){
                            let indexToBeSelected = value.replace("Row","").replace("#","");
                            let $tr = $(tbl[indexToBeSelected].el[0]);     
                            $tr.addClass('tr-selected');
                            $tr.data('selected', true);
                            $tr.find( "input.checkbox" ).prop('checked', true);
                            window.selectedModels.push(_representation.metadata[indexToBeSelected]);
                        });
                        
                    },
                    selectAllRow       : ( O, rowIndex, rowData ) => {
                        console.log('slect All', rowIndex);
                        this.checked = true;
                        $(this).closest("tr").css("background-color", "#e1e3e8");
                        window.selectedModels.push(_representation.metadata[rowIndex]);
                        _value.selection.push(_representation.table.rows[rowIndex].rowKey);
                        return;
               
                    },
                    selectRow       : ( O, rowIndex, rowData ) => {
                            if (window.selectedModels.length >= _representation.maxSelectionNumber) {
                                $(this).prop("checked", false);
                                return;
                            }
                            this.checked = true;
                            $(this).closest("tr").css("background-color", "#e1e3e8");
                            //fetch scripts
                            if(_lazySriptsfetching){
                                
                                const simulations = fetch(window._endpoints.simulationsEndpoint + rowIndex);
                                simulations.then(function(response) {
                                    return response.json();
                                }).then(function(data) {
                                    _representation.metadata[rowIndex]['simulation'] = data; // this will be a json
                                    const modelscript =  fetch(window._endpoints.modelscriptEndpoint + rowIndex);
                                    modelscript.then(function(response) {
                                        return response.text();
                                    }).then(function(data) {
                                        _representation.metadata[rowIndex]['modelscript'] = data; // this will be a string
                                        const visualizationscript =  fetch(window._endpoints.visualizationscriptEndpoint + rowIndex);
                                        visualizationscript.then(function(responsevis) {
                                            return responsevis.text();
                                        }).then(function(datavis) {
                                            _representation.metadata[rowIndex]['visualization'] = datavis; // this will be a string
                                            // save selected model
                                            selectionMap[rowIndex] = window.selectedModels.length;
                                            window.selectedModels.push(_representation.metadata[rowIndex]);
                                            _value.selection.push(_representation.table.rows[rowIndex].rowKey);
                                            // emit selection event
                                            window.downloadURs.push(window._endpoints.download+rowIndex);
                                            knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' ,
                                                    [{"selecteModels":window.selectedModels, "downloadURs":window.downloadURs}]
                                            /* [window.selectedModels,window.downloadURs]*/,{elements:[]})  
                                        });
                                    });
                                });
                                
                            }else{
                                // save selected model
                                window.selectedModels.push(rowData.modelMetadata);
                                _value.selection.push(_representation.table.rows[rowIndex].rowKey);
                                // emit selection event
                                knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' ,
                                        [{"selecteModels":window.selectedModels, "downloadURs":[]}] /*[window.selectedModels]*/,{elements:[]})  
                            }
                        

                        
                    },
                    deselectRow     : ( O, rowIndex, rowData ) => {
                        _log( 'on > deselectRow', 'hook' ); 
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
                            let indexToBeSelected = value.replace("Row","").replace("#","");
                            return indexToBeSelected != rowIndex;
                        }); 
                        knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21joiner' ,
                                [{"selecteModels":window.selectedModels, "downloadURs":window.downloadURs}],{elements:[]});
                        delete selectionMap[rowIndex];
                          
                    },
                    updateFilter    : ( O, filtered ) => {
                        _log( 'on > updateFilter', 'hook' ); 
                        _log( O );
                        _log( filtered );
                    }
                }
            }
        }; 
    }
    
    return view;
}();