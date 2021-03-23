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
  let initiated = function(event){
    //if(parent.tableID){
    //  $('#saveButton').show();
    //}
  }
  let doSave = function(_metadatax){
	 _metadatax.modelMath.parameter.forEach(param => {
		param.classification = param.classification.toUpperCase()
		});
    _metadatax.modelMath.parameter.forEach(param => {
      if(param.classification != "OUTPUT"){ 
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
    //subscribe to events emitted by FSK DB View
    knimeService.subscribeToSelection('b800db46-4e25-4f77-bcc6-db0c215846e1', selectionChanged);
    knimeService.subscribeToSelection('b800db46-4e25-4f77-bcc6-db0c21GlobalInit', initiated);
    
    _rep = representation;
    _val = value;
    window._endpoints.controlledVocabularyEndpoint = _rep.controlledVocabularyURL;
    window.vocabularies = representation.vocabularies;
    //fskutil = new fskutil();
    extractAndCreateUI(value.modelMetaData);
    
    // TODO: remove this test for the vocabularies
    // makeRequest("source");
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
                        data 		  : {},
                        id 			  : 'mtModalDetails',
                        classes 	: 'modal-details',
                        type 		  : 'mtDetails'
                      }, mainContainer );
    _modalDetails._createModelMetadataContent();
    await _modalDetails._updateContent(_metadata, 0);
    createUI(modelscript, visualization);
    window.editEventBus.subscribe('EditorJS',(event) =>{
      _metadata = _modalDetails._modelHandler.metaData;
      doSave(_metadata)
    });
  }


  view.getComponentValue = () => {
    _metadata = _modalDetails._modelHandler.metaData;
    delete _metadata['simulation'];
    delete _metadata['modelscript'];
    delete _metadata['visualization'];
    delete _metadata['Location'];
    let metaDataString = JSON.stringify(_metadata);

    // If the code mirrors are not created yet, use the original scripts.
    let viewValue = {
      modelMetaData: metaDataString,
      modelScript: _modelCodeMirror ? _modelCodeMirror.getValue() : _val.modelScript,
      visualizationScript: _visualizationCodeMirror ? _visualizationCodeMirror.getValue() : _val.visualizationScript,
      readme: _readmeCodeMirror ? _readmeCodeMirror.getValue() : _metadata.readme,
      resourceFiles: _val.resourceFiles, // TODO: get actual resource files from editor
      serverName: _val.serverName, // TODO: get actual serverName from editor?
      isCompleted: true,
      validationErrors: [],
      modelType: _metadata.modelType
    };
    
    return viewValue;
  };

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