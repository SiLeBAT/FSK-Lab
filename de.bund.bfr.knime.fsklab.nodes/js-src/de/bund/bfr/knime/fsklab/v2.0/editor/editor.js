fskeditorjs = function () {

  const view = { version: "1.0.0" };
  view.name = "Javascript FSK Editor";

  var _rep;
  var _val;

  var _metadata = {
    generalInformation: {},
    scope: {},
    dataBackground: {},
    modelMath: {}
  };
  let _modalDetails;
  var _modelCodeMirror;
  var _visualizationCodeMirror;
  var _readmeCodeMirror;
  var _location;
  var _simulation;
  var resourcesFiles = [];
  var parentResourcesFolder;
  var JWT;
  var server;
  var timeStampInMs = window.performance && window.performance.now
            && window.performance.timing
            && window.performance.timing.navigationStart ? window.performance
            .now()
            + window.performance.timing.navigationStart : Date.now();
  let handler;
  let selectionChanged = function (modelMetaData) { 
    window._debug = false;
    let selectedModel = modelMetaData.changeSet.added[0];
    _location = selectedModel.Location;
    _simulation = selectedModel.simulation;
    extractAndCreateUI(JSON.stringify(selectedModel), selectedModel.modelscript, selectedModel.visualization);
    _modelCodeMirror.setValue(selectedModel.modelscript);
    _visualizationCodeMirror.setValue(selectedModel.visualization);

  }
  let camelize = function (str) {
      return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function(word, index) {
            return index === 0 ? word.toUpperCase() : word.toLowerCase();
          }).replace(/\s+/g, '');
        }
  let doSave = function(_metadatax){
    _metadatax = JSON.parse(JSON.stringify(_metadatax));
    
    _metadatax.modelMath.parameter.forEach(param => {
      param.classification = param.classification.toUpperCase();
      if(param.classification != "OUTPUT" && _simulation){ 
        _simulation.forEach(simulation => {
            if(Object.keys(simulation.parameters).indexOf(param.id) < 0){
              simulation.parameters[param.id] = param.value;
            }
        });
      }
    });
    _metadatax.Location =_location;
    _metadatax.simulation =_simulation;
    _metadatax.modelscript= _modelCodeMirror ? _modelCodeMirror.getValue() : _val.modelScript;
    _metadatax.visualization = _visualizationCodeMirror ? _visualizationCodeMirror.getValue() : _val.visualizationScript;
    knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21EditorSaved' , [_metadatax],{elements:[]}) 
    // fix related to a bug in knime 4.0 interactivity service where it's empty the sevice load'
    knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21EditorSaved' , [],{elements:[]}) 
  }
  view.init = function (representation, value) {
    knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c21GlobalEditor' , [{"editorAvailable":true}],{elements:[]})
    
    //subscribe to events emitted by FSK DB View
    knimeService.subscribeToSelection('b800db46-4e25-4f77-bcc6-db0c215846e1', selectionChanged);
    
    _rep = representation;
    _val = value;
    window._endpoints.controlledVocabularyEndpoint = _rep.controlledVocabularyURL;
    window.vocabularies = representation.vocabularies;
    //fskutil = new fskutil();
    extractAndCreateUI(value.modelMetaData);
    
    // TODO: remove this test for the vocabularies
    // makeRequest("source");
  }

  function convert(jsosMetadata, schema) {
		
	    const result = {};
	    Object.keys(jsosMetadata || {}).forEach(key => {
	        if(schema.properties && schema.properties.hasOwnProperty(key) ){
		        if(schema.properties[key].type !== 'object') {
		            result[key] = jsosMetadata[key];
		        }
		        else if(schema.properties[key].type === 'array') {
		            result[key] = jsosMetadata[key];
		        }
		        else if(schema.properties[key].type === 'object') {
		            const value = convert(jsosMetadata[key], schema.properties[key]);
		            if (value !== undefined) {
		                result[key] = value;
		            }
		        }
		    }
	    });
	    return result;
  }
  
  function initResourcesTab(){
    if (window.location.protocol != '' && window.location.host != '') {
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
        parentResourcesFolder = "knime://knime.mountpoint/tempResources/jsEditorTempFolder"
                + timeStampInMs;
        anotherxhttp.open("put", server
                + "/knime/rest/v4/repository/tempResources/jsEditorTempFolder"
                + timeStampInMs, true);
        anotherxhttp.setRequestHeader("Authorization", "Bearer" + JWT);
        anotherxhttp.send();
        let inputFile = $('#filesInput');
        let button = $('#filesButton');
        let buttonSubmit = $('#uploadButton');
        let filesContainer = $('#filesArea');
        let files = [];
        let fileIDMap = {}
        let fileUploadAJAXMap = {}
    
        inputFile
            .change(function () {
                let newFiles = [];
                for (let index = 0; index < inputFile[0].files.length; index++) {
                    let file = inputFile[0].files[index];
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
                    filesContainer.append(fileElement);
                    fileIDMap[file.name] = ID
                    $("[idFile='"+file.name+"']").click(function (event) {
                        let fileElement = $(event.target);
                        let indexToRemove = files.indexOf(fileElement
                            .data('fileData'));
                        $.ajax({
                            type: "DELETE",
                            url: server
                                + "/knime/rest/v4/repository/tempResources/jsEditorTempFolder"
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
                                + "/knime/rest/v4/repository/tempResources/jsEditorTempFolder"
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
                                resourcesFiles
                                    .push("knime://knime.mountpoint"
                                        + data.path);
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
            inputFile.click();
        });
    
        buttonSubmit.click(function () {
            let formData = new FormData();
            for (let index = 0; index < files.length; index++) {
                let file = files[index];
                formData.append('file', file);
            }
    
        });
        $('#Resources').show();
    }
    
  }
  async function  extractAndCreateUI(modelMetaData, modelscript, visualization){
    if (!modelMetaData || modelMetaData == "null" || modelMetaData == "") {
      _metadata.generalInformation = {};
      _metadata.generalInformation.modelCategory = {};
      _metadata.scope = {};
      _metadata.modelMath = {};
      _metadata.dataBackground = {}
    } else {
      let receivedObject = modelMetaData instanceof Object? modelMetaData: JSON.parse(modelMetaData);
      let metaData = Array.isArray(receivedObject) ? receivedObject[0] : receivedObject;

      if (!metaData.generalInformation) {
        _metadata.generalInformation = { modelCategory: {} };
      } else {
        _metadata.generalInformation = metaData.generalInformation;
      }

      _metadata.scope = metaData.scope ? metaData.scope : {};
      _metadata.dataBackground = metaData.dataBackground ? metaData.dataBackground : {};
      _metadata.modelMath = metaData.modelMath ? metaData.modelMath : {};
      _metadata.modelMath.parameter.forEach(param => {
        param.classification = camelize(param.classification)
    });
      _metadata.modelType = metaData.modelType;
    }
    /*
    switch (_metadata.modelType) {
      case "genericModel": handler = new fskutil.GenericModel(_metadata, _rep.servicePort); break;
      case "dataModel": handler = new fskutil.DataModel(_metadata, _rep.servicePort); break;
      case "predictiveModel": handler = new fskutil.PredictiveModel(_metadata, _rep.servicePort); break;
      case "otherModel": handler = new fskutil.OtherModel(_metadata, _rep.servicePort); break;
      case "toxicologicalModel": handler = new fskutil.ToxicologicalModel(_metadata, _rep.servicePort); break;
      case "doseResponseModel": handler = new fskutil.DoseResponseModel(_metadata, _rep.servicePort); break;
      case "exposureModel": handler = new fskutil.ExposureModel(_metadata, _rep.servicePort); break;
      case "processModel": handler = new fskutil.ProcessModel(_metadata, _rep.servicePort); break;
      case "consumptionModel": handler = new fskutil.ConsumptionModel(_metadata, _rep.servicePort); break;
      case "healthModel": handler = new fskutil.HealthModel(_metadata, _rep.servicePort); break;
      case "riskModel": handler = new fskutil.RiskModel(_metadata, _rep.servicePort); break;
      case "qraModel": handler = new fskutil.QraModel(_metadata, _rep.servicePort); break;
      default: handler = new fskutil.GenericModel(_metadata, _rep.servicePort); break;
    }

    createUI();
    */
    window.port = _rep.servicePort;
    let mainContainer = $(`<div class="card"></div>`);
    $('body').html(mainContainer);
    _modalDetails = new APPMTEditableDetails( {
                        data          : {},
                        id            : 'mtModalDetails',
                        classes     : 'modal-details',
                        type          : 'mtDetails'
                      }, mainContainer );
    _modalDetails._createModelMetadataContent();
    await _modalDetails._updateContent(_metadata, 0);
    createUI(modelscript, visualization);
    initResourcesTab();
    window.editEventBus.subscribe('EditorJS',(event) =>{
      _metadata = _modalDetails._modelHandler.metaData;
      doSave(_metadata)
    });
    Object.keys(_val.modelsTypeslabel).forEach(function(key) {
	  if(_val.modelType == _val.modelsTypeslabel[key])
	  	window['selectInput_Model_class'].val(key);
	});
    
    window['selectInput_Model_class'].trigger('change');
    
    setTimeout(function() {
	    window['selectInput_Model_class'].on('change', function(e){
			
		    var modelType = $('#selectInput_Model_class').val();
		    modelType = modelType == '(Data)' ? 'Data model' : modelType;
		    
			let receivedObject = _val.modelMetaData instanceof Object? _val.modelMetaData: JSON.parse(_val.modelMetaData);
      		let metaData = Array.isArray(receivedObject) ? receivedObject[0] : receivedObject;
		    
		    var convertedModel = convert(metaData, _val.modelsMap[modelType]);
		    
		    convertedModel.modelType = _val.modelsTypeslabel[modelType];
		    convertedModel.generalInformation.modelCategory.modelClass = modelType;
		    
		    _val.modelMetaData = convertedModel;
		    _val.modeTypeChanged = "true";
		    extractAndCreateUI(convertedModel);
		    setTimeout(function() {
				    $('#select2-selectInput_Model_class-container').text(modelType);
				    
			}, 500);
		    
			});
	}, 500);
	
	

    
  }


  view.getComponentValue = () => {
    _metadata = _modalDetails._modelHandler.metaData;
    delete _metadata['simulation'];
    delete _metadata['modelscript'];
    delete _metadata['visualization'];
    delete _metadata['Location'];
    _metadata.modelMath.parameter.forEach(param => {
        param.classification = param.classification.toUpperCase()
    });
    let metaDataString = JSON.stringify(_metadata);

    // If the code mirrors are not created yet, use the original scripts.
    let viewValue = {
      modelMetaData: metaDataString,
      modelScript: _modelCodeMirror ? _modelCodeMirror.getValue() : _val.modelScript,
      visualizationScript: _visualizationCodeMirror ? _visualizationCodeMirror.getValue() : _val.visualizationScript,
      readme: _readmeCodeMirror ? _readmeCodeMirror.getValue() : _metadata.readme,
      resourceFiles: _val.resourceFiles, // TODO: get actual resource files from editor
      serverName: _val.serverName, // TODO: get actual serverName from editor?
      environment: _val.environment,
      completed: true,
      validationErrors: [],
      modeTypeChanged:_val.modeTypeChanged,
      modelType: _metadata.modelType
    };
    const ajv = new Ajv({allErrors:true});
    let referenceSchema = prepareSchema(window.modelSchema, _metadata.modelType);
    const validate = ajv.compile(referenceSchema)
    validate(deleteEmptyValues(JSON.parse(JSON.stringify(_metadata))))
    if(validate.errors){
        viewValue.validationErrors = validate.errors.map(errorItem => {
            return JSON.stringify(errorItem)
        });
    }
    viewValue.resourcesFiles = resourcesFiles;
    viewValue.parentResourcesFolder = parentResourcesFolder;
    return viewValue;
    
  };
  function deleteEmptyValues(modeData) {
    for (property in modeData) {
            if($.isArray(modeData[property]) && modeData[property].length == 0){
                delete modeData[property];
            }else if (typeof  modeData[property] === "string" && modeData[property]== '' ) {
                delete modeData[property];
            }
            else if (typeof  modeData[property] === 'object' &&  modeData[property] !== null ) {
                deleteEmptyValues(modeData[property])
            }
    }
    return modeData;
  }
  function prepareSchema(mainSchema, modelType){
     let resolvedValue = {properties:{}};
     let modeTypeKey = modelType.charAt(0).toUpperCase() + modelType.slice(1);
     let preSchema = mainSchema.definitions[modeTypeKey];
     resolvedValue.type = preSchema.allOf[1].type
     $.each(preSchema.allOf[1].properties, function (key, value) {
        resolve(mainSchema,key ,modeTypeKey ,value, resolvedValue);
     });
     return resolvedValue;
  }
  function resolve(mainSchema, key,modeTypeKey, currentSchemaElement, resolvedValue){
    
    if(currentSchemaElement.hasOwnProperty('$ref') ){
        let schemaKey = currentSchemaElement['$ref'].replace('#/definitions/','');
        resolvedKey = schemaKey.charAt(0).toUpperCase() + schemaKey.slice(1);
        currentSchemaElement = mainSchema.definitions[resolvedKey];
        $.each(currentSchemaElement.properties, function (childKey, childValue) {
            resolve(mainSchema,childKey, modeTypeKey , childValue, currentSchemaElement);
        });
        let elementKey = schemaKey.replace(modeTypeKey+'_','').replace(modeTypeKey,'');
        elementKey = elementKey.charAt(0).toLowerCase() + elementKey.slice(1)
        resolvedValue.properties[elementKey] = currentSchemaElement
    }else if(currentSchemaElement.type == 'array' && currentSchemaElement.items && currentSchemaElement.items.hasOwnProperty('$ref') ){
        let schemaKey = currentSchemaElement.items['$ref'].replace('#/definitions/','');
        resolvedKey = schemaKey.charAt(0).toUpperCase() + schemaKey.slice(1);
        let elementKey = schemaKey.replace(modeTypeKey,'');
        elementKey = elementKey.charAt(0).toLowerCase() + elementKey.slice(1)
        resolvedValue.properties[key]={};
        resolvedValue.properties[key].items = mainSchema.definitions[resolvedKey]
        resolvedValue.properties[key].type = 'array'
        $.each(resolvedValue.properties[key].items.properties, function (childKey, childValue) {
            resolve(mainSchema,childKey, modeTypeKey , childValue, resolvedValue.properties[key].items);
        });
    }else{
        $.each(currentSchemaElement.properties, function (childKey, childValue) {
            resolve(mainSchema, childKey, modeTypeKey , childValue, currentSchemaElement);
        });
        resolvedValue.properties[key] = currentSchemaElement
        
    }
  }
  view.validate = () => {
    // return handler.validate();
    return true;
  }

  return view;

  /** UI code. */
  async function createUI(modelscript, visualization) {
    $('#modelScriptArea').val(modelscript || _val.modelScript);
    $('#visualizationScriptArea').val(visualization || _val.visualizationScript);
    $('#readmeArea').val(_val.readme);
    
     _modelCodeMirror = createCodeMirror("modelScriptArea", "text/x-rsrc");
     _visualizationCodeMirror = createCodeMirror("visualizationScriptArea", "text/x-rsrc");
     _readmeCodeMirror = createCodeMirror("readmeArea", "text/x-markdown");
     let doScriptSave = () => { 
        _metadata = _modalDetails._modelHandler.metaData;
        doSave(_metadata)
      };
      
     _modelCodeMirror.on("blur", () => { 
        _modelCodeMirror.focus();
        doScriptSave();
      });
     _visualizationCodeMirror.on("blur", () => { 
        _visualizationCodeMirror.focus();
        doScriptSave();
      });
     _readmeCodeMirror.on("blur", () => { 
        _readmeCodeMirror.focus();
        doScriptSave();
      });
    
     $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
         if (e.currentTarget.text == 'Model') {
             _modelCodeMirror.refresh();
             _modelCodeMirror.focus();
         } else if (e.currentTarget.text == 'Visualization') {
             _visualizationCodeMirror.refresh();
             _visualizationCodeMirror.focus();
         } else if (e.currentTarget.text == 'Readme') {
             _readmeCodeMirror.refresh();
             _readmeCodeMirror.focus();
         }
     });

    if(_rep.combinedObject){
        $('[aria-controls="#modelScript"]').hide();
        $('[aria-controls="#visualizationScript"]').hide();
        $('[aria-controls="#readme"]').hide();
    }
    

  }

  // Create a CodeMirror for a given text area
  function createCodeMirror(textAreaId, language) {

    return CodeMirror.fromTextArea(document.getElementById(textAreaId),
      {
        lineNumbers: true,
        lineWrapping: true,
        extraKeys: { 'Ctrl-Space': 'autocomplete' },
        mode: { 'name': language }
      });
  }

  function makeRequest(vocabularyName) {
    knimeService.loadConditionally(["js-src/de/bund/bfr/knime/fsklab/v1.9/editor/lazyload.js"],
            () => window.fetchVocabulary(vocabularyName));
  }
}();