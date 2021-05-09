joiner = function () {

  // Dimension utility functions.
  // Returns the windows width minus the left and right padding.
  //const getChartWidth = () => window.innerWidth - 32;

  // Returns the window height minus the navbar and modal footer
  //const getChartHeight = () => window.innerHeight - $(".navbar-collapse").height() - $(".modal-footer").height() - 300;

  const view = { version: "1.0.0", name: "FSK Joiner" };
  let _value;
  let _joinerModelsData;
  let _modelScriptTree;
  let _modelsParamsOriginalNames = {};
  let _simulationMap={};
  let _finalsimulationList = [];
  let _ignoreSelectSimulation;
  let _genericModelReference = {};
  let _vocabularies = {};
  let _UIInformation = {};
  
  /** JointJS graph. */
  let _graph;

  /** JointJS graph view. */
  let _paper;

  let _metadata;
  let _graphObject;
  let _firstModelParameterMap = {};
  let _secondModelParameterMap = {};
  let _modelColectionWithoutSuffixedmap = {};

  let modelsPool = {
    firstModel: {},
    secondModel: {},
    thirdModel: {},
    fourthModel: {}
  }
  let paperWidth;
  window.joinRelationsMap = {};
  let poolSize = 0;
  let addSuffixToParameters = function (_modelcolection) {
    switch (_modelcolection.length) {
      case 1:
        $.each(_modelcolection[0].modelMath.parameter, function (_index, param) {
          param.id = param.id + "1";
        });
        break;
      case 2:
        $.each(_modelcolection[0].modelMath.parameter, function (_index, param) {
          param.id = param.id + "1";
        });
        $.each(_modelcolection[1].modelMath.parameter, function (_index, param) {
          param.id = param.id + "2"
        });
        break;

      case 3:
        $.each(_modelcolection[0].modelMath.parameter, function (_index, param) {
          param.id = param.id + "11"
        });
        $.each(_modelcolection[1].modelMath.parameter, function (_index, param) {
          param.id = param.id + "12"
        });
        $.each(_modelcolection[2].modelMath.parameter, function (_index, param) {
          param.id = param.id + "2"
        });
        break;

      case 4:
        $.each(_modelcolection[0].modelMath.parameter, function (_index, param) {
          param.id = param.id + "111"
        });
        $.each(_modelcolection[1].modelMath.parameter, function (_index, param) {
          param.id = param.id + "112"
        });
        $.each(_modelcolection[2].modelMath.parameter, function (_index, param) {
          param.id = param.id + "12"
        });
        $.each(_modelcolection[3].modelMath.parameter, function (_index, param) {
          param.id = param.id + "2"
        });
        break;
    }

    return _modelcolection;
  }
  let buildOldNewParamName = function (newParamsx, oldParamsx) {
    let oldNewParamNames = {};
    $.each(oldParamsx, function (_ind1, oldParam) {
      $.each(newParamsx, function (_ind1, newParam) {
        if (newParam.id.startsWith(oldParam.id)) {
          oldNewParamNames[newParam.id] = oldParam.id
        }

      });
    });

    return oldNewParamNames;
  }
  function cleanUp(){
    modelsPool = {
      firstModel: {},
      secondModel: {},
      thirdModel: {},
      fourthModel: {}
    }
  }
  function makeVocabullaryMap() {
    for (key in ui) {
        if (ui.hasOwnProperty(key)) {
            ui[key].forEach(value => {
                if (value.hasOwnProperty('vocabulary')) {
                    delete value['description']
                    delete value['label']
                    delete value['required']
                    _vocabularies[key+value['id']] = value['vocabulary'];
                }
                _UIInformation[value['id']] = value['type'];
            });
        }
    }
  }
  
  let selectionChanged = function (modelMetaData) {
    cleanUp();
    let _modelColectionSuffixed =  addSuffixToParameters(JSON.parse(JSON.stringify(modelMetaData.changeSet.added[0]["selecteModels"])));
    poolSize =_modelColectionSuffixed.length;
    let keys = ['firstModel','secondModel','thirdModel','fourthModel']; 
    _graph.clear();
    $.each(_modelColectionSuffixed,function(index,selectedModel){
      _modelsParamsOriginalNames[escapeHtmlChars(selectedModel.generalInformation.name.trim())] = buildOldNewParamName(_modelColectionSuffixed[index].modelMath.parameter, modelMetaData.changeSet.added[0]["selecteModels"][index].modelMath.parameter);
      _modelColectionWithoutSuffixedmap[escapeHtmlChars(selectedModel.generalInformation.name.trim())] = modelMetaData.changeSet.added[0]["selecteModels"][index].modelMath.parameter;
      delete _modelColectionSuffixed[index]['modelscript'];
      delete _modelColectionSuffixed[index]['visualization'];
      delete _modelColectionSuffixed[index]['Location'];
      delete _modelColectionSuffixed[index]['simulation'];
      editModelsPool(keys[index], _modelColectionSuffixed[index].modelMath.parameter, escapeHtmlChars(selectedModel.generalInformation.name.trim()), _modelColectionSuffixed[index], selectedModel.modelType 
                    ,JSON.stringify(modelMetaData.changeSet.added[0]["selecteModels"][index]['modelscript'])
                    ,JSON.stringify(modelMetaData.changeSet.added[0]["selecteModels"][index]['visualization'])
                    ,JSON.stringify(modelMetaData.changeSet.added[0]["selecteModels"][index]['Location'])
                    ,modelMetaData.changeSet.added[0]["selecteModels"][index]['simulation']
                    ,modelMetaData.changeSet.added[0]['downloadURs']?modelMetaData.changeSet.added[0]['downloadURs'][index]:"");

    });
    
    
  }
  
  var _isUndefined = ( val ) => typeof val === typeof undefined ? true : false;

  var _isNull = ( val ) => val == null ? true : false;
  /**
   * A function join the metadata of all availible joined models
   */
  function iterateAndExtend(reference, model1, model2, model3, model4,vocabularyReferencekey) {
    for (var property in reference) {
        let holderModel = model1[property]?model1:(model2[property]?model2:(model3[property]?model3:model4[property]));
        
        if (holderModel && holderModel[property]) {
            console.log(vocabularyReferencekey+property, _vocabularies.hasOwnProperty(vocabularyReferencekey+property));
            if($.isArray(holderModel[property])){
                if(property == 'creationDate'){
                    reference[property] = model1[property];
                }else{
                    reference[property] = [].concat(model1[property] ? model1[property] : []
                        , model2[property] ? model2[property] : []
                        , model3[property] ? model3[property] : []
                        , model4[property] ? model4[property] : []);
                }
            }else if (typeof  holderModel[property] === 'object' &&  holderModel[property] !== null ) {
                reference[property] = iterateAndExtend(reference[property] ? reference[property] : {}
                    , model1 && model1[property] ? model1[property] : {}
                    , model2 && model2[property] ? model2[property] : {}
                    , model3 && model3[property] ? model3[property] : {}
                    , model4 && model4[property] ? model4[property] : {},property);
            }
             else if (property == 'modelType') {
                //do nothing 
            } else if (_vocabularies.hasOwnProperty(vocabularyReferencekey+property) ) {
                reference[property] = model1[property];
            } else if (_UIInformation[property] === 'date' || _UIInformation[property] === 'email') {
                reference[property] = model1[property];
            } else if (_UIInformation[property] === 'url') {
                reference[property] = model1[property] + ' ' + model2[property] ? model2[property] : '' + ' ' + model3[property] ? model3[property] : '' + ' ' + model4[property] ? model4[property] : '';
            } else if (_UIInformation[property] === 'date-array' || _UIInformation[property] === 'text-array') {
                reference[property] = [].concat(model1[property] ? model1[property] : []
                    , model2[property] ? model2[property] : []
                    , model3[property] ? model3[property] : []
                    , model4[property] ? model4[property] : []);
            } else if (_UIInformation[property] === 'boolean') {
                reference[property] = model1[property] && model2[property] ? model2[property] : true &&  model3[property] ? model3[property] : true &&  model4[property] ? model4[property] : true;
            } else if (_UIInformation[property] === 'text' || _UIInformation[property] === 'long-text') {
                reference[property] = (model1[property] ? model1[property] : '') +
                    (model2[property] ? model2[property] : '') +
                    (model3[property] ? model3[property] : '') +
                    (model4[property] ? model4[property] : '');
            } else if (_UIInformation[property] === 'number') {
                reference[property] = model1[property];
            }
            
            if(_isUndefined(reference[property]) || _isNull(reference[property]) ||
                    !isValidModel(reference[property])){
                 delete reference[property]
            }
             
        }  else if (!reference[property]){
            delete reference[property]
        }
    }
    return reference;
  }
  
  view.init = function (representation, value) {
    _genericModelReference = JSON.parse(representation.genericModelReference);
    _value = value;
    _graphObject = JSON.parse(_value.jsonRepresentation)
    _joinerModelsData = representation.joinerModelsData;
    
    _representation = representation;
    trimModelName(_joinerModelsData);
    $.each(_joinerModelsData.modelsParamsOriginalNames, (key, value) => {
        _modelsParamsOriginalNames[escapeHtmlChars(key.trim())] = value;
    })
    //subscribe to events emitted by FSK DB View
    knimeService.subscribeToSelection('b800db46-4e25-4f77-bcc6-db0c21joiner', selectionChanged);
    makeVocabullaryMap();
    if (value.modelMetaData) {
      _metadata = JSON.parse(value.modelMetaData);
    } else {
      _metadata = {
        generalInformation: {},
        scope: {},
        dataBackground: {},
        modelMath: {}
      };
    }


    if (_value.joinRelations && _value.joinRelations.length > 0) {

      $.each(_value.joinRelations, function (index, value) {
        if (value) {
          window.joinRelationsMap[value.sourceParam + "," + value.targetParam] = value;
        }
      });
    } else {
      _value.joinRelations = [];
    }



    createBody();
    //_paper.scaleContentToFit();
    window.toogle = true;
  }
  
  function isValidModel(model) {
    return Object.keys(model).length !== 0;
  }
  
  function escapeHtmlChars(text) {
     return text
          .replace(/&/g, "")
          .replace(/</g, "")
          .replace(/>/g, "")
          .replace(/"/g, "")
          .replace(/'/g, "");
  }
  
  trimModelName = (joinerModelsData) => {
    try{
        if(joinerModelsData.firstModel && joinerModelsData.firstModel[0]){
            joinerModelsData.firstModel[0] = fixModelMetadata(joinerModelsData.firstModel[0]);
            joinerModelsData.firstModelName = joinerModelsData.firstModelName?joinerModelsData.firstModelName.trim():joinerModelsData.firstModelName;
            joinerModelsData.firstModelName = escapeHtmlChars(joinerModelsData.firstModelName);
            
        }
        if(joinerModelsData.secondModel && joinerModelsData.secondModel[0]){
            joinerModelsData.secondModel[0] = fixModelMetadata(joinerModelsData.secondModel[0]);
            joinerModelsData.secondModelName = joinerModelsData.secondModelName?joinerModelsData.secondModelName.trim():joinerModelsData.secondModelName;
            joinerModelsData.secondModelName = escapeHtmlChars(joinerModelsData.secondModelName);
            
        }
        if(joinerModelsData.thirdModel && joinerModelsData.thirdModel[0]){
            joinerModelsData.thirdModel[0] = fixModelMetadata(joinerModelsData.thirdModel[0]);
            joinerModelsData.thirdModelName = joinerModelsData.thirdModelName?joinerModelsData.thirdModelName.trim():joinerModelsData.thirdModelName;
            joinerModelsData.thirdModelName = escapeHtmlChars(joinerModelsData.thirdModelName);
        }
        if(joinerModelsData.fourthModel && joinerModelsData.fourthModel[0]){
            joinerModelsData.fourthModel[0] = fixModelMetadata(joinerModelsData.fourthModel[0]);
            joinerModelsData.fourthModelName = joinerModelsData.fourthModelName?joinerModelsData.fourthModelName.trim():joinerModelsData.fourthModelName;
            joinerModelsData.fourthModelName = escapeHtmlChars(joinerModelsData.fourthModelName);
        }
    }catch(err){
        console.log(err);
    }
    
  }
  fixModelMetadata = (metadata) => {
    let jsonMetadata = JSON.parse(metadata);
    try{
        if(jsonMetadata.generalInformation.name)
            jsonMetadata.generalInformation.name = escapeHtmlChars(jsonMetadata.generalInformation.name.trim());
    }catch(err){
        console.log(err);
    }
    return  JSON.stringify(jsonMetadata);
   }
  view.getComponentValue = function () {
    if (!isValidModel(_value.joinerModelsData)) {
      _value.joinerModelsData = {
        firstModel: [],
        secondModel: [],
        thirdModel: [],
        fourthModel: [],
        firstModelType: "",
        secondModelType: "",
        thirdModelType: "",
        fourthModelType: ""
      }
    }

    if (parent.tableID) {
      _value.joinerModelsData.numberOfModels = 0;
      try {
        if (isValidModel(modelsPool.firstModel)) {
          modelsPool.firstModel['metadata']['modelMath']['parameter'] = _modelColectionWithoutSuffixedmap[modelsPool.firstModel['modelName']];
          _value.joinerModelsData.firstModel = [JSON.stringify(modelsPool.firstModel['metadata']), modelsPool.firstModel['modelScript'], modelsPool.firstModel['vis'], JSON.stringify(modelsPool.firstModel['simulation']), "[]", modelsPool.firstModel['Location'],modelsPool.firstModel['downloadURL'],modelsPool.firstModel['modelName']];
          _value.joinerModelsData.firstModelType = modelsPool.firstModel['modelType'];
          _value.joinerModelsData.firstModelName = modelsPool.firstModel['modelName'];
          _value.joinerModelsData.numberOfModels++;
          _value.joinerModelsData.firstModelParameters = modelsPool.firstModel['modelParameters'];

        }else if (_value.joinerModelsData.firstModel){
          _value.joinerModelsData.numberOfModels++;
        }

        if (isValidModel(modelsPool.secondModel)) {
          modelsPool.secondModel['metadata']['modelMath']['parameter'] = _modelColectionWithoutSuffixedmap[modelsPool.secondModel['modelName']];
          _value.joinerModelsData.secondModel = [JSON.stringify(modelsPool.secondModel['metadata']), modelsPool.secondModel['modelScript'], modelsPool.secondModel['vis'], JSON.stringify(modelsPool.secondModel['simulation']), "[]", modelsPool.secondModel['Location'],modelsPool.secondModel['downloadURL'],modelsPool.secondModel['modelName']];
          _value.joinerModelsData.secondModelType = modelsPool.secondModel['modelType'];
          _value.joinerModelsData.secondModelName = modelsPool.secondModel['modelName'];
          _value.joinerModelsData.numberOfModels++;
          _value.joinerModelsData.secondModelParameters = modelsPool.secondModel['modelParameters'];
        }else if (_value.joinerModelsData.secondModel){
          _value.joinerModelsData.numberOfModels++;
        }

        if (isValidModel(modelsPool.thirdModel)) {
          modelsPool.thirdModel['metadata']['modelMath']['parameter'] = _modelColectionWithoutSuffixedmap[modelsPool.thirdModel['modelName']];
          _value.joinerModelsData.thirdModel = [JSON.stringify(modelsPool.thirdModel['metadata']), modelsPool.thirdModel['modelScript'], modelsPool.thirdModel['vis'], JSON.stringify(modelsPool.thirdModel['simulation']), "[]", modelsPool.thirdModel['Location'],modelsPool.thirdModel['downloadURL'],modelsPool.thirdModel['modelName']];
          _value.joinerModelsData.thirdModelType = modelsPool.thirdModel['modelType'];
          _value.joinerModelsData.thirdModelName = modelsPool.thirdModel['modelName'];
          _value.joinerModelsData.numberOfModels++;
          _value.joinerModelsData.thirdModelParameters =  modelsPool.thirdModel['modelParameters'];
        }else if (_value.joinerModelsData.thirdModel){
          _value.joinerModelsData.numberOfModels++;
        }

        if (isValidModel(modelsPool.fourthModel)) {
          modelsPool.fourthModel['metadata']['modelMath']['parameter'] = _modelColectionWithoutSuffixedmap[modelsPool.fourthModel['modelName']];
          _value.joinerModelsData.fourthModel = [JSON.stringify(modelsPool.fourthModel['metadata']), modelsPool.fourthModel['modelScript'], modelsPool.fourthModel['vis'], JSON.stringify(modelsPool.fourthModel['simulation']), "[]", modelsPool.fourthModel['Location'],modelsPool.fourthModel['downloadURL'],modelsPool.fourthModel['modelName']];
          _value.joinerModelsData.fourthModelType = modelsPool.fourthModel['modelType'];
          _value.joinerModelsData.fourthModelName = modelsPool.fourthModel['modelName'];
          _value.joinerModelsData.numberOfModels++;
          _value.joinerModelsData.thirdModelParameters = modelsPool.fourthModel['modelParameters'];
        }else if (_value.joinerModelsData.fourthModel){
          _value.joinerModelsData.numberOfModels++;
        }
        
        _value.joinerModelsData.interactiveMode = true;
        $.each(_finalsimulationList,function(index, simulation){
            $.each(_value.joinRelations, function(indexz, connection){
              if(connection.targetParam != undefined && simulation.params[connection.targetParam] != undefined){
                delete simulation.params[connection.targetParam]
              }
            });
        });     
        _value.joinerModelsData.joinedSimulation = _finalsimulationList;
        if(isValidModel( modelsPool.secondModel)){
            console.log(_vocabularies);
            _genericModelReference = iterateAndExtend(_genericModelReference ,modelsPool.firstModel['metadata'], modelsPool.secondModel['metadata'],
                         modelsPool.thirdModel['metadata'] ,modelsPool.fourthModel['metadata'],'');
            console.log(_genericModelReference);
         }else if(isValidModel( modelsPool.firstModel)){
            _genericModelReference = modelsPool.firstModel['metadata']
         }    
        _value.modelMetaData = JSON.stringify(_genericModelReference);
         
        _value.joinRelations.forEach((relation) => {
          delete relation.sourceModel;
          delete relation.targetModel;
        });


        }catch (err){console.log(err);}
      } else {
       if(isValidModel( modelsPool.secondModel)){
          _genericModelReference = iterateAndExtend(_genericModelReference ,JSON.parse(modelsPool.firstModel['metadata'][0]), JSON.parse(modelsPool.secondModel['metadata'][0]),
                                    modelsPool.thirdModel['metadata']?JSON.parse(modelsPool.thirdModel['metadata'][0] ):undefined,
                                    modelsPool.fourthModel['metadata']?JSON.parse(modelsPool.fourthModel['metadata'][0],''):undefined);
       }else if(isValidModel( modelsPool.firstModel)){
          _genericModelReference = modelsPool.firstModel['metadata']
       }  
      _value.modelMetaData = JSON.stringify(_genericModelReference);
      _value.joinerModelsData.firstModel = modelsPool.firstModel['metadata'];
      _value.joinerModelsData.secondModel = modelsPool.secondModel['metadata'];
      _value.joinerModelsData.thirdModel = modelsPool.thirdModel['metadata'];
      _value.joinerModelsData.fourthModel = modelsPool.fourthModel['metadata'];
      _value.joinerModelsData.firstModelType = modelsPool.firstModel['modelType'];
      _value.joinerModelsData.secondModelType = modelsPool.secondModel['modelType'];
      _value.joinerModelsData.thirdModelType = modelsPool.thirdModel['modelType'];
      _value.joinerModelsData.fourthModelType = modelsPool.fourthModel['modelType'];
      $.each(_finalsimulationList,function(index, simulation){
            $.each(_value.joinRelations, function(indexz, connection){
              if(connection.targetParam != undefined && simulation.params[connection.targetParam] != undefined){
                delete simulation.params[connection.targetParam]
              }
            });
        });  
      _value.joinerModelsData.joinedSimulation = _finalsimulationList;
    }
    d3.select("svg").selectAll(".link-tools").remove();
    d3.select("svg").selectAll(".marker-arrowhead-group-source").remove();
    d3.select("svg").selectAll(".marker-arrowheads").attr("style", "fill-opacity: .5;");
    d3.select("svg").selectAll(".connection").attr("stroke-opacity", "0.5");
    d3.select("svg").selectAll("title").remove();
    d3.select("svg").selectAll("foreignObject").remove();
    _value.svgRepresentation = this.getSVG();
    if(_graph)
     _value.jsonRepresentation = JSON.stringify(_graph.toJSON());
    return _value;
  };

  view.getSVG = function () {
    if (!_paper) return null;
    _paper.svg.setAttribute("width", $('#viewContent').width());
    _paper.svg.setAttribute("height", 500);
    return (new XMLSerializer()).serializeToString(_paper.svg);
  };

  function editModelsPool(key, modelParameters, modelName, individualMetadata, modelType, modelScript, vis, Location, simulation, downloadURL) {
    _simulationMap[escapeHtmlChars(modelName.trim())] = { "selectedSimulation": "defaultSimulation", "simulationList":simulation };
    updatesFinalSimulation();
    modelsPool[key]['modelScript'] = modelScript;
    modelsPool[key]['simulation'] = simulation;
    modelsPool[key]['vis'] = vis;
    modelsPool[key]['downloadURL'] = downloadURL
    modelsPool[key]['Location'] = Location;
    modelsPool[key]['modelName'] = modelName;
    modelsPool[key]['modelType'] = modelType;
    modelsPool[key]['metadata'] = individualMetadata
    modelsPool[key]['inputParameters'] = [];
    modelsPool[key]['outputParameters'] = [];
    modelsPool[key]['portMap'] = {};
    modelsPool[key]['modelParameterMap'] = {};
    let orginalParamsWithoutSuffix = _modelsParamsOriginalNames[modelName];
    $.each(modelParameters, function (index, param) {
      param['idmask'] = orginalParamsWithoutSuffix[param.id];
    })
    modelsPool[key]['modelParameters'] = modelParameters;
    if (modelParameters) {
      for (param of modelParameters) {
        if (!modelsPool[key]['modelParameterMap'][param.id]) {
          let port = createPort(param);
          if (param.classification === "INPUT" || param.classification === "CONSTANT") {
            port.group = "in";
            modelsPool[key]['inputParameters'].push(port);
          } else {
            port.group = "out";
            modelsPool[key]['outputParameters'].push(port);
          }

          modelsPool[key]['portMap'][param.id] = port;
          modelsPool[key]['modelParameterMap'][param.id] = param;
        }
      }
    }

    let modelHeight = Math.max(modelsPool[key]['inputParameters'].length, modelsPool[key]['outputParameters'].length) * 25;

    let modelNameWrap = joint.util.breakText(modelName, {
      width: 150,
      height: modelHeight
    });
    let order = Object.keys(modelsPool).indexOf(key);
    modelsPool[key]['modelToJoin'] = createAtomic(order == 0 ? 80 : ( 250 * order) + ((4-order)*30), 55, 160,
      modelHeight, modelNameWrap, modelsPool[key]['inputParameters'],
      modelsPool[key]['outputParameters'], key, modelsPool[key]['simulation']);

    _graph.clear();
    $.each(Object.keys(modelsPool), function (index, value) {
      if (Object.keys(modelsPool[value]).length > 0)
        _graph.addCell(modelsPool[value]['modelToJoin']);
    })
    graphJSON = _graph.toJSON();
    _value.jsonRepresentation = JSON.stringify(graphJSON);
    reoderTool();
  }
  /**
   * take a model's orginalParamsWithoutSuffix and replace keys and values in that map
   * @param {*} modelName 
   */
  function reverseParameterMap(modelName){
    let orginalParamsWithoutSuffix = _modelsParamsOriginalNames[modelName];
    let reversedModelsParamsOriginalNames = {};
    $.each(Object.keys(orginalParamsWithoutSuffix), function (index, key) {
      reversedModelsParamsOriginalNames[orginalParamsWithoutSuffix[key]] = key;
    })
    return reversedModelsParamsOriginalNames;
  }
  /**
   * Afunction to generate the list of simulation for the joined Object
   * @param {*} selectedSimulationName which could be an 'All' which means cross-merge all simulation from all models.
   */
  function updatesFinalSimulation(selectedSimulationName) {
    if(_ignoreSelectSimulation)
      return;
    _finalsimulationList=[];
    let tespSimulationArray = [];
    if (selectedSimulationName == 'All') {
      _ignoreSelectSimulation = true;
      let tempSimulationInfo1 = {};
      let firstModelName = modelsPool.firstModel['modelName'];
      let reversedModelsParamsOriginalNames1 =  reverseParameterMap(firstModelName);
      $.each(_simulationMap[modelsPool.firstModel['modelName']]['simulationList'], function (ind2, firtModelSimulationInfo) {
          if(isValidModel(modelsPool.secondModel)){
            $.each(Object.keys(firtModelSimulationInfo["parameters"]), function (ind3, key3) {
              tempSimulationInfo1[reversedModelsParamsOriginalNames1[key3]] = firtModelSimulationInfo["parameters"][key3];
            })
            let tempSimulationInfo2 = {};
            let secondModelName = modelsPool.secondModel['modelName'];
            let reversedModelsParamsOriginalNames2 =  reverseParameterMap(secondModelName);
            $.each(_simulationMap[modelsPool.secondModel['modelName']]['simulationList'], function (ind2,secondModelSimulationInfo) {
              if(isValidModel(modelsPool.thirdModel)){
                $.each(Object.keys(secondModelSimulationInfo["parameters"]), function (ind3, key3) {
                  tempSimulationInfo2[reversedModelsParamsOriginalNames2[key3]] = secondModelSimulationInfo["parameters"][key3];
                })
                let tempSimulationInfo3 = {};
                let thirdModelName = modelsPool.thirdModel['modelName'];
                let reversedModelsParamsOriginalNames3 =  reverseParameterMap(thirdModelName);
                $.each(_simulationMap[modelsPool.thirdModel['modelName']]['simulationList'], function (ind2, thirdModelSimulationInfo) {
                  if (isValidModel(modelsPool.fourthModel)) {
                    $.each(Object.keys(thirdModelSimulationInfo["parameters"]), function (ind3, key3) {
                      tempSimulationInfo3[reversedModelsParamsOriginalNames3[key3]] =thirdModelSimulationInfo["parameters"][key3];
                    })
                    let tempSimulationInfo4 = {};
                    let fourthModelName = modelsPool.fourthModel['modelName'];
                    let reversedModelsParamsOriginalNames4 = reverseParameterMap(fourthModelName);
                    $.each(_simulationMap[modelsPool.fourthModel['modelName']]['simulationList'], function (ind2,fourthModelSimulationInfo) {
                        $.each(Object.keys(fourthModelSimulationInfo["parameters"]), function (ind3, key3) {
                          tempSimulationInfo4[reversedModelsParamsOriginalNames4[key3]] = fourthModelSimulationInfo["parameters"][key3];
                        })
                        Object.assign(tespSimulationArray, tempSimulationInfo1, tempSimulationInfo2, tempSimulationInfo3, tempSimulationInfo4)
                        let simulationSenarioName ='defaultSimulation';
                        if(firtModelSimulationInfo['name'] !='defaultSimulation' || secondModelSimulationInfo['name'] !='defaultSimulation' ||  thirdModelSimulationInfo['name'] !='defaultSimulation' || fourthModelSimulationInfo['name'] !='defaultSimulation'){
                            simulationSenarioName = firtModelSimulationInfo['name']+'_'+ secondModelSimulationInfo['name']+'_'+ thirdModelSimulationInfo['name']+'_'+ fourthModelSimulationInfo['name']
                        } 
                        _finalsimulationList.push({ 'name': simulationSenarioName, 'params': Object.assign({}, tespSimulationArray) });
                    });
                    

                  }else{
                    $.each(Object.keys(thirdModelSimulationInfo["parameters"]), function (ind3, key3) {
                      tempSimulationInfo3[reversedModelsParamsOriginalNames3[key3]] =thirdModelSimulationInfo["parameters"][key3];
                    })
                    Object.assign(tespSimulationArray, tempSimulationInfo1, tempSimulationInfo2, tempSimulationInfo3);
                    let simulationSenarioName ='defaultSimulation';
                    if(firtModelSimulationInfo['name'] !='defaultSimulation' || secondModelSimulationInfo['name'] !='defaultSimulation' ||  thirdModelSimulationInfo['name'] !='defaultSimulation' ){
                        simulationSenarioName = firtModelSimulationInfo['name']+'_'+ secondModelSimulationInfo['name']+'_'+ thirdModelSimulationInfo['name']
                    } 
                    _finalsimulationList.push({ 'name': simulationSenarioName, 'params': Object.assign({}, tespSimulationArray) });
                  }
                   

                });
              } else {
                $.each(Object.keys(secondModelSimulationInfo["parameters"]), function (ind3, key3) {
                    tempSimulationInfo2[reversedModelsParamsOriginalNames2[key3]] = secondModelSimulationInfo["parameters"][key3];
                })
                Object.assign(tespSimulationArray, tempSimulationInfo1, tempSimulationInfo2);
                let simulationSenarioName ='defaultSimulation';
                if(firtModelSimulationInfo['name'] !='defaultSimulation' || secondModelSimulationInfo['name'] !='defaultSimulation' ){
                    simulationSenarioName = firtModelSimulationInfo['name']+'_'+ secondModelSimulationInfo['name']
                } 
                _finalsimulationList.push({ 'name': simulationSenarioName, 'params': Object.assign({}, tespSimulationArray) });
              }
              
            });
          }else{
            $.each(Object.keys(firtModelSimulationInfo["parameters"]), function (ind3, key3) {
              tempSimulationInfo1[reversedModelsParamsOriginalNames1[key3]] = firtModelSimulationInfo["parameters"][key3];
            })
            Object.assign(tespSimulationArray, tempSimulationInfo1);
            _finalsimulationList.push({ 'name': firtModelSimulationInfo['name'], 'params': Object.assign({}, tespSimulationArray) });
          }
          
      });
     
      
    }
    else {
         _ignoreSelectSimulation = false;
         $.each(Object.keys(_simulationMap), function(ind1, modelName) {
            let selectedSimName = _simulationMap[modelName]['selectedSimulation'];
            let orginalParamsWithoutSuffix = _modelsParamsOriginalNames[modelName];
            let reversedModelsParamsOriginalNames = {};
            $.each(Object.keys(orginalParamsWithoutSuffix), function(index, key) {
                reversedModelsParamsOriginalNames[orginalParamsWithoutSuffix[key]] = key;
            })
            let tempSimulationInfo = {};
            $.each(_simulationMap[modelName]['simulationList'], function(ind2, simulationInfo) {
                if (simulationInfo['name'] == selectedSimName) {
                     $.each(Object.keys(simulationInfo["parameters"]), function(ind3, key3) {
                        tempSimulationInfo[reversedModelsParamsOriginalNames[key3]] = simulationInfo["parameters"][key3];
                     })
                     Object.assign(tespSimulationArray, tempSimulationInfo);
            	}
            });
            _finalsimulationList = [{ 'name': 'defaultSimulation', 'params': Object.assign({}, tespSimulationArray) }];
         });
    }


  }

  function createBody() {

    $('body').html(`<div class="container-fluid">
    <nav class="navbar navbar-default">
     <div class="navbar-collapse collapse">
       
     </div>
    
      <div class="tab-content" id="viewContent">
        <div role="tabpanel" class="tab-pane active" id="joinPanel">
          <div id="paper"></div>
          <div class="toolbar">
            <span id="zoom-out" class="minus bg-dark toolbar-button">-</span>
            <span id="scaletofit" class="scale bg-dark toolbar-button">Scale</span>
            <span id="zoom-in" class="plus bg-dark toolbar-button">+</span>
          </div>
          <form id="detailsForm">
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="source">Source Port:</label>
              <div class="col-sm-10"><input type="text" class="form-control" id="source" readonly></div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="target">Target Port:</label>
              <div class="col-sm-10">
                <input type="text" class="form-control" id="target" readonly>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="commandLanguage">Command language:</label>
              <div class="col-sm-10">
                <input type="text" class="form-control" id="commandLanguage">
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="Command">Conversion command:</label>
              <div class="col-sm-10">
                <textarea class="form-control" rows="3" id="Command"></textarea>
              </div>
            </div>
          </form>
        </div> <!-- tabpanel -->
    
        <div role="tabpanel" class="tab-pane" id="generalInformationPanel" />
        <div role="tabpanel" class="tab-pane" id="scopePanel" />
        <div role="tabpanel" class="tab-pane" id="dataBackgroundPanel" />
        <div role="tabpanel" class="tab-pane" id="modelMathPanel" />
       </div> <!-- viewContent -->
    
     </div>
    </nav>
    </div>`);



    drawWorkflow();

    // Resize event. Resize the paper with the window.
    window.onresize = () => {
      //  _paper.setDimensions(getChartWidth(), getChartHeight());

      //_paper.setDimensions($('#viewContent').width(), 500);
      _paper.svg.setAttribute("width", $('#viewContent').width());
      _paper.svg.setAttribute("height",  600);
      $("#paper").width( $('#viewContent').width());
      //_paper.scaleContentToFit();
      //_paper.scaleContentToFit({ padding: 20 });
    };
    reoderTool();


  }
  function updateSimulationMap(modelName,selectedSimulationName){
     _simulationMap[modelName]["selectedSimulation"] = selectedSimulationName ;
  }
  function reoderTool() {
    $('.selectSimulation').on('mousedown click', function (evt) {
      evt.stopPropagation();
    });
    var previous;
    $('.selectSimulation').on('change', function () {
      
      let testArray = this.value.split('---');
      let modelName = testArray[0];
      let selectedSimulationName = testArray[1];
      updateSimulationMap(modelName,selectedSimulationName);
      updatesFinalSimulation(selectedSimulationName);
      if (selectedSimulationName == 'All') {
        $.each(Object.keys(_simulationMap), function (ind1, modelNamex) {
          valueToBeSelected = modelNamex + '---All';
          $(`option[value='${valueToBeSelected}']`).attr('selected', 'selected');
        });
      }else{
        $.each(Object.keys(_simulationMap), function (ind1, modelNamex) {
          valueToBeSelected = modelNamex + '---defaultSimulation';
          valueToBeDeSelected = modelNamex + '---All';
          
          if($(`option[value='${valueToBeDeSelected}']`).attr('selected') == 'selected' && modelName != modelNamex ){
            $('#'+modelNamex+'_select').val(valueToBeSelected);
            //$(`option[value='${valueToBeSelected}']`).attr('selected', 'selected');
          }
        });
      }
    });
    $('.selectForOrder').on('mousedown click', function (evt) {
      evt.stopPropagation();
    });
    var previous;
    $('.selectForOrder').on('focus', function () {
      // Store the current value on focus and on change
      previous = this.value;
      window.toogle = true;
    }).on('change', function () {
      if (window.toogle) {

        window.toogle = false;
        let newValueText = this.value;
        let oldValue = JSON.parse(JSON.stringify(modelsPool[previous]));
        let newValue = JSON.parse(JSON.stringify(modelsPool[newValueText]));

        let newParams;
        let oldParams;
        let oldSuffix;
        let newSuffix;
        switch (previous) {
          case "firstModel":
            if (poolSize == 2) {
              oldSuffix = 1;
            } else if (poolSize == 3) {
              oldSuffix = 2;
            } else {
              oldSuffix = 3;
            }

            break;
          case "secondModel":
            if (poolSize == 2) {
              oldSuffix = 1;
            } else if (poolSize == 3) {
              oldSuffix = 2;
            } else {
              oldSuffix = 3;
            }
            break;
          case "thirdModel":
            if (poolSize == 3) {
              oldSuffix = 1;
            } else {
              oldSuffix = 2;
            }
            break;
          case "fourthModel":
            oldSuffix = 1;
            break;

        }
        switch (newValueText) {
          case "firstModel":
            if (poolSize == 2) {
              newSuffix = 1;
            } else if (poolSize == 3) {
              newSuffix = 2;
            } else {
              newSuffix = 3;
            }
            break;
          case "secondModel":
            if (poolSize == 2) {
              newSuffix = 1;
            } else if (poolSize == 3) {
              newSuffix = 2;
            } else {
              newSuffix = 3;
            }
            break;
          case "thirdModel":
            if (poolSize == 3) {
              newSuffix = 1;
            } else {
              newSuffix = 2;
            }
            break;
          case "fourthModel":
            newSuffix = 1;
            break;

        }
        oldParams = modelsPool[previous]['modelParameters'];
        newParams = modelsPool[newValueText]['modelParameters'];
        let tempModelName = modelsPool[previous]['modelName'];
        preapareParametersForReorder(modelsPool[previous]['modelName'], modelsPool[newValueText]['modelName'], newParams, newSuffix, oldParams, oldSuffix);

        modelsPool[previous] = newValue;
        modelsPool[previous]['modelName'] = modelsPool[newValueText]['modelName'];
        modelsPool[newValueText]['modelName'] = tempModelName;
        modelsPool[newValueText] = oldValue;

        editModelsPool(previous, newParams, modelsPool[previous]['modelName'], modelsPool[previous]['metadata'], modelsPool[previous]['modelType'],
                      modelsPool[previous]['modelScript']
                      ,modelsPool[previous]['vis']
                      ,modelsPool[previous]['Location']
                      ,modelsPool[previous]['simulation']
                      ,modelsPool[previous]['downloadURL']);

        editModelsPool(newValueText, oldParams, modelsPool[newValueText]['modelName'], modelsPool[newValueText]['metadata'], modelsPool[newValueText]['modelType'],
                      modelsPool[newValueText]['modelScript']
                      ,modelsPool[newValueText]['vis']
                      ,modelsPool[newValueText]['Location']
                      ,modelsPool[newValueText]['simulation']
                      ,modelsPool[newValueText]['downloadURL']);
        
      }
      try{
        $("#" + newValueText + ' option').filter(function () {
          return $(this).text() == previous;
        }).prop('selected', 'selected');
        $("#" + newValueText).prop('id', 'med');
        $("#" + previous).prop('id', newValueText);
        $("#med").prop('id', previous);
      }catch(err){}

    });
  }
  function preapareParametersForReorder(oldModelName, newMmodelName, newParams, newSuffix, oldParams, oldSuffix) {

    let newSuffixx = newParams[0].id.substring(newParams[0].id.length - newSuffix, newParams[0].id.length);
    let oldSuffixx = oldParams[0].id.substring(oldParams[0].id.length - oldSuffix, oldParams[0].id.length);
    for (param of oldParams) {
      let originalName = _modelsParamsOriginalNames[oldModelName][param.id];
      delete _modelsParamsOriginalNames[oldModelName][param.id];
      param.id = param.id.replace(oldSuffixx, newSuffixx);
      _modelsParamsOriginalNames[oldModelName][param.id] = originalName

    }
    for (param of newParams) {
      let originalName = _modelsParamsOriginalNames[newMmodelName][param.id];
      delete _modelsParamsOriginalNames[newMmodelName][param.id];
      param.id = param.id.replace(newSuffixx, oldSuffixx);
      _modelsParamsOriginalNames[newMmodelName][param.id] = originalName
    }
  }
  function drawWorkflow() {

    _graph = new joint.dia.Graph();

    _paper = new joint.dia.Paper({
      el: document.getElementById('paper'),
      drawGrid: 'mesh',
      gridSize: 10,
      model: _graph,
      snapLinks: true,
      linkPinning: true,
      drawGrid: true,
      width: 1200,
      height: 600,
      highlighting: {
        'default': {
          name: 'stroke',
          options: {
            padding: 6
          }
        }
      },

      interactive: (cellView) => {
        // Disable the default vertex and add functionality on pointerdown
        if (cellView.model instanceof joint.dia.Link) {
          return { vertexAdd: false };
        }
        return true;
      },
      validateConnection: function (cellViewS, magnetS, cellViewT, magnetT, end, linkView) {
        //It's not allowed to override parameters of type CONSTANT
        if($(magnetT).text().indexOf('Classification: CONSTANT') != -1){
            $(magnetT).attr( "fill","yellow" );
            return false;
        }
        var links = _graph.getLinks();
        for (var i = 0; i < links.length; i++) {
          if (linkView == links[i].findView(_paper)) //Skip the wire the user is drawing
            continue;

          if (((cellViewT.model.id == links[i].get('source').id) && (magnetT.getAttribute('port') == links[i].get('source').port)) ||
            ((cellViewT.model.id == links[i].get('target').id) && (magnetT.getAttribute('port') == links[i].get('target').port))) {
            return false;
          }
        }
        //if (linkView.sourceView.id != linkView.paper.viewport.children[0].id)
        //return false;

        if (cellViewT.id == linkView.paper.viewport.children[0].id &&
          magnetT && magnetT.getAttribute('port-group') === 'in')
          return false;

        if (magnetT && magnetT.getAttribute('port-group') === 'out' &&
          magnetS && magnetS.getAttribute('port-group') === 'out' &&
          cellViewS.id === cellViewT.id)
          return false;

        if (magnetT && magnetT.getAttribute('port-group') === 'out' &&
          magnetS && magnetS.getAttribute('port-group') === 'in' &&
          cellViewS.id === cellViewT.id)
          return false;

        //output to output does not make sense
        if (magnetT && magnetT.getAttribute('port-group') === 'out' &&
          magnetS && magnetS.getAttribute('port-group') === 'out')
          return false;

        //input to input not supported right now
        // if (magnetT && magnetT.getAttribute('port-group') === 'in' &&
        //   magnetS && magnetS.getAttribute('port-group') === 'in' )
        //   return false;

        //inputS to outpuT_Target not sensible
        if (magnetT && magnetT.getAttribute('port-group') === 'out' &&
          magnetS && magnetS.getAttribute('port-group') === 'in')
          return false;

        return true;
      },

      // Enable marking available cells and magnets
      markAvailable: true
    });
    //_paper.setDimensions($('#viewContent').width(), 500);
    _paper.svg.setAttribute("width", $('#viewContent').width());
    _paper.svg.setAttribute("height",  600);
    $("#paper").width( $('#viewContent').width());
    var MIN_SCALE = 0.1
    var MAX_SCALE = 1000
    var handleCellMouseWheel = ( e, x, y, delta) =>
      handleCanvasMouseWheel(e, x, y, delta);

    var handleCanvasMouseWheel = (e, x, y, delta) => {
      e.preventDefault();

      const oldScale = _paper.scale().sx;
      const newScale = oldScale + delta * .01;
      scaleToPoint(newScale, x, y);
    };

    var scaleToPoint = (nextScale, x, y) => {
      
      if (nextScale >= MIN_SCALE && nextScale <= MAX_SCALE) {
        const ctm = _paper.matrix();

        ctm.a = nextScale;
        ctm.d = nextScale;

        _paper.matrix(ctm);
      }
    };
    _paper.on('blank:mousewheel', function(evt, x, y, delta) {
      handleCellMouseWheel(evt, x, y,  delta );
    });
    // Toolbar
    var zoomLevel = 1;

    document.getElementById('zoom-in').addEventListener('click', function() {
        zoomLevel = Math.min(3, zoomLevel + 0.2);
        var size = _paper.getComputedSize();
        _paper.translate(0,0);
        _paper.scale(zoomLevel, zoomLevel, size.width / 2, size.height / 2);
    });

    document.getElementById('scaletofit').addEventListener('click', function() {
        _paper.scaleContentToFit();
    });

    document.getElementById('zoom-out').addEventListener('click', function() {
        zoomLevel = Math.max(0.2, zoomLevel - 0.2);
        var size = _paper.getComputedSize();
        _paper.translate(0,0);
        _paper.scale(zoomLevel, zoomLevel, size.width / 2, size.height / 2);
    });

    let previousOne;
    function highlight(cellView) {
      if (previousOne) previousOne.unhighlight();
      previousOne = cellView;
      cellView.highlight();
    }


    // Highlight a cell when clicked
    _paper.on('cell:pointerclick', highlight);
    // Pointer is released after pressing down a link
    _paper.on('link:pointerup', (event) => {
      if (event.model instanceof joint.dia.Link) {
        let targetPort = event.model.attributes.target.port;
        if (!targetPort) {
          event.remove();
        }
      }
    });

    // Update form when a link is clicked
    _paper.on("link:pointerclick", updateForm);
    let canvas = $("#paper");
    paperWidth = canvas.width();
                    
      _joinerModelsData.firstModelName ? editModelsPool('firstModel', _joinerModelsData.firstModelParameters, _joinerModelsData.firstModelName, _joinerModelsData.firstModel, _joinerModelsData.firstModelType,
      _joinerModelsData.firstModel[1]
      ,_joinerModelsData.firstModel[2]
      ,''
      ,JSON.parse(_joinerModelsData.firstModel[3])
      ,"") : '';
    _joinerModelsData.secondModelName ? editModelsPool('secondModel', _joinerModelsData.secondModelParameters, _joinerModelsData.secondModelName, _joinerModelsData.secondModel, _joinerModelsData.secondModelType,
    _joinerModelsData.secondModel[1]
      ,_joinerModelsData.secondModel[2]
      ,''
      ,JSON.parse(_joinerModelsData.secondModel[3])
      ,"") : '';
    _joinerModelsData.thirdModelName ? editModelsPool('thirdModel', _joinerModelsData.thirdModelParameters, _joinerModelsData.thirdModelName, _joinerModelsData.thirdModel, _joinerModelsData.thirdModelType,
    _joinerModelsData.thirdModel[1]
      ,_joinerModelsData.thirdModel[2]
      ,''
      ,JSON.parse(_joinerModelsData.thirdModel[3])
      ,"") : '';
    _joinerModelsData.fourthModelName ? editModelsPool('fourthModel', _joinerModelsData.fourthModelParameters, _joinerModelsData.fourthModelName, _joinerModelsData.fourthModel, _joinerModelsData.fourthModelType,
    _joinerModelsData.fourthModel[1]
      ,_joinerModelsData.fourthModel[2]
      ,''
      ,JSON.parse(_joinerModelsData.fourthModel[3])
      ,"") : '';
    /*let firstModelInputParameters = [];
    let firstModelOutputParameters = [];
    window.firstPortMap = {};
    
 
    for (param of _representation.firstModelParameters) {
      if (!_firstModelParameterMap[param.id]) {
        let port = createPort(param.id, param.dataType);
        if (param.classification === "INPUT" || param.classification === "CONSTANT") {
          port.group = "in";
          firstModelInputParameters.push(port);
        } else {
          port.group = "out";
          firstModelOutputParameters.push(port);
        }
 
        window.firstPortMap[param.id] = port;
        _firstModelParameterMap[param.id] = param;
      }
    }
 
    let secondModelInputParameters = [];
    let secondModelOutputParameters = [];
    window.secondPortMap = {};
    for (param of _representation.secondModelParameters) {
      if (!_firstModelParameterMap[param.id]) {
        let port = createPort(param.id, param.dataType);
        if (param.classification === "INPUT" || param.classification === "CONSTANT") {
          port.group = "in";
          secondModelInputParameters.push(port);
        } else {
          port.group = "out";
          secondModelOutputParameters.push(port);
        }
 
        window.secondPortMap[param.id] = port;
        _secondModelParameterMap[param.id] = param;
      }
    }
      */


    /*if(modelsPool['secondModel']['inputParameters']){
      let secondModelHeight = Math.max(modelsPool['secondModel']['inputParameters'].length, modelsPool['secondModel']['outputParameters'].length) * 25;
      let secondModelNameWrap = joint.util.breakText(modelsPool['secondModel']['modelName'], {
        width: 200,
        height: secondModelHeight
      });
  
      let secondModelToJoin = createAtomic(paperWidth - 330, 180, 200,
        secondModelHeight, secondModelNameWrap, modelsPool['secondModel']['inputParameters'],
        modelsPool['secondModel']['outputParameters']);
      secondModelToJoin.on('change:position', (element) => {
        let cellView = _paper.findViewByModel(element);
        highlight(cellView);
      });
    }*/
    // Update form when a link is selected (clicked)
    //_paper.on("link:pointerclick", updateForm);

    _paper.on('link:connect', function (linkView, evt, elementViewDisconnected, magnet, arrowhead) {
      sModel = linkView.model.attributes.source;
      tModel = linkView.model.attributes.target;
      sourcePort = linkView.model.attributes.source.port;
      targetPort = linkView.model.attributes.target.port;
      let sourceMask;
      let targetMask;
      let sourceMaskSuffix;
      let targetMaskSuffix;
      let commandMask;
      //let keys = Object.keys(_modelsParamsOriginalNames);
      for (key in _modelsParamsOriginalNames) {
        if (_modelsParamsOriginalNames[key][sourcePort])
          sourceMask = _modelsParamsOriginalNames[key][sourcePort];
        sourceMaskSuffix = sourceMask;
        commandMask = "[" + sourceMask + "]";

        if (_modelsParamsOriginalNames[key][targetPort]) {
          targetMask = _modelsParamsOriginalNames[key][targetPort];
          targetMaskSuffix = targetMask;
        }
      }
      if (!targetPort) {
        return;
      }

      //highlight(linkView);

      // Update form
      document.getElementById("source").value = sourceMaskSuffix;
      document.getElementById("target").value = targetMaskSuffix;

      let command = document.getElementById("Command");
      command.value = commandMask;

      command.onkeyup = () => window.sJoinRealtion.command = command.value.replace(sourceMask, sourcePort);

      command.onblur = () => {
        if (_modelScriptTree) {
          _modelScriptTree[1].script = _value.joinRelations
            .map(relation => `${relation.targetParam} <- ${relation.command}`)
            .join("\n");
        }
      };

      if (targetPort != undefined) {

        window.sJoinRealtion = {
          sourceParam: sourcePort,
          targetParam: targetPort,
          command: "[" + sourcePort + "]",//sourcePort
          sourceModel: sModel,
          targetModel: tModel
        };

        if (_metadata.generalInformation.languageWrittenIn && _metadata.generalInformation.languageWrittenIn.length > 0) {
          window.sJoinRealtion.language_written_in = _metadata.generalInformation.languageWrittenIn;
          $('#commandLanguage').val(_metadata.generalInformation.languageWrittenIn);
        }
        if (!_value.joinRelations) {
          _value.joinRelations = []
        }

        _value.joinRelations.push(sJoinRealtion);
        window.joinRelationsMap[sourcePort + "," + targetPort] = sJoinRealtion

        if (_modelScriptTree) {
          _modelScriptTree[1].script = _value.joinRelations
            .map(relation => `${relation.targetParam} <- ${relation.command}`)
            .join("\n");
        }

        _value.jsonRepresentation = JSON.stringify(_graph.toJSON());
      }
    }); // paper.on('link:connect')

    _graph.on('remove', (link) => {
      if (!(link.attributes.source))
        return;
      sourcePort = link.attributes.source.port;
      targetPort = link.attributes.target.port;

      let sourceParameter = _firstModelParameterMap[sourcePort] ?
        _firstModelParameterMap[sourcePort] : _secondModelParameterMap[sourcePort];

      let targetParameter = _secondModelParameterMap[sourcePort] ?
        _secondModelParameterMap[sourcePort] : _firstModelParameterMap[sourcePort];

      if (targetParameter != undefined) {
        $.each(_value.joinRelations, function (index, value) {

          if (value != undefined && value.sourceParam.parameterID == sourceParameter.parameterID
            && value.targetParam.parameterID == targetParameter.parameterID) {
            _value.joinRelations.splice(index, 1);
          }
        });
        _value.jsonRepresentation = JSON.stringify(_graph.toJSON());
      }
    });

    if (_graphObject) { 
      _graph.fromJSON(_graphObject);

      if (_graphObject.cells) {
        for (const cell of _graphObject.cells) {
          if (cell.ports && cell.ports.items) {
            let currentCell = _graph.getCell(cell.id);
            cell.ports.items.forEach(item => currentCell.addPort(item));
          }
        }
      }
      
    } else {
      $.each(Object.keys(modelsPool), function (index, value) {
        if (Object.keys(modelsPool[value]).length > 0)
          _graph.addCell(modelsPool[value]['modelToJoin']);
      })
      graphJSON = _graph.toJSON();
      var links = [];
      $.each(window.joinRelationsMap, function (key, value) {
        var portIds = key.split(",");
        let link = new joint.shapes.devs.Link({
          source: { id: value.sourceModel, port: portIds[0] },
          target: { id: value.targetModel, port: portIds[1] }
        });

        links.push(link);
      });
      _graph.addCells(links);
    }
    $('.joint-port-body').each(function( index ) {
        if($( this ).text().indexOf('Classification: CONSTANT') != -1){
            $( this ).attr( "fill","yellow" );
        }
    });
  }

  /**
   * Create a JointJS port for a model parameter.
   * @param {Parameter} param 
   */
  function createPort(param) {
    return {
      // id: 'abc', // generated if `id` value is not present
      'id': param.id,
      'label': {
        'markup': `<text class="label-text" fill="black"><title>${param.dataType}</title>${param.idmask}</text>`
      },
      'markup': `<circle fill="#FF7979"  r="92.5" class="port-body"><title>Parameter ID: ` + param.idmask + `\nParameter Name: ` + param.name  + `\nClassification: ` + param.classification +  `\nDescription: ` + param.description + `\nUnit: ` + param.unit + `\nDataType: ` + param.dataType + `\nSource: ` + param.source + `\nSubject: ` + param.subject + `\nDistribution: ` + param.distribution + `\nReference: ` + param.reference + `\nVariabilitySubject: ` + param.variabilitySubject + `\nMinValue: ` + param.minValue + `\nMaxValue: ` + param.maxValue + `\nError: ` + param.error + `\n</title></circle>`,
    };
  }

  /**
   * Call-back function for updating the form when a link between two ports is clicked.
   */
  function updateForm(evt, cellView, magnet, arrowhead) {
    let sourcePort = evt.model.get("source").port;
    let targetPort = evt.model.get("target").port;

    let sourceMask;
    let targetMask;
    let sourceMaskSuffix;
    let targetMaskSuffix;
    //let keys = Object.keys(_modelsParamsOriginalNames);
    for (key in _modelsParamsOriginalNames) {
      if (_modelsParamsOriginalNames[key][sourcePort])
        sourceMask = _modelsParamsOriginalNames[key][sourcePort];
      sourceMaskSuffix = sourceMask;

      if (_modelsParamsOriginalNames[key][targetPort]) {
        targetMask = _modelsParamsOriginalNames[key][targetPort];
        targetMaskSuffix = targetMask;
      }
    }

    window.sJoinRealtion = window.joinRelationsMap[sourcePort + "," + targetPort];

    document.getElementById("source").value = sourceMaskSuffix;
    document.getElementById("target").value = targetMaskSuffix;

    let commandLanguage = document.getElementById("commandLanguage");
    commandLanguage.value = window.sJoinRealtion.language_written_in;
    commandLanguage.onchange = () => window.sJoinRealtion.language_written_in = commandLanguage.value;

    let commandTextArea = document.getElementById("Command");
    commandTextArea.value = window.sJoinRealtion.command.replace(sourcePort, sourceMask);
    commandTextArea.onkeyup = () => window.sJoinRealtion.command = commandTextArea.value.replace(sourceMask, sourcePort);;
  }

  /** Create model to join.
   * 
   * @param {int} x X position
   * @param {int} y Y position
   * @param {int} width Width
   * @param {int} height Height
   * @param {string} modelName Model name
   * @param {array} inputs Array of input parameter ports
   * @param {array} inputs Array of output parameter ports
  */
  function createAtomic(x, y, width, height, modelName, inputs, outputs, modelIndex,simulations) {
    let simulationoption = ``;
    let disabled = 'disabled="disabled"';
    let nothing = '';
    simulations.forEach(sim => {
      simulationoption += `<option value ="${modelName}---${sim.name}">${sim.name}</option>`;
    })
    simulationoption += `<option value ="${modelName}---All">All Simulation</option>`;
    let atomic = new joint.shapes.devs.Atomic({
      markup: `<g>
                      <rect class="body" stroke-width="0" rx="5px" ry="5px"></rect>
                      <text class="label" y="0.8em" xml:space="preserve" font-size="14" text-anchor="middle" font-family="Arial, helvetica, sans-serif">
                        <tspan id="v-18" dy="0em" x="0" class="v-line"></tspan>
                      </text>
                      

                      <foreignObject  x="20" y="20" width="${width}" height="${height + 60}" >
                      <!--
                        In the context of SVG embedded in an HTML document, the XHTML 
                        namespace could be omitted, but it is mandatory in the 
                        context of an SVG document
                      -->
                      <div class="html-element" style="position: absolute;
                      right: 25px;
                      bottom: 25px;" xmlns="http://www.w3.org/1999/xhtml">
                        <select id = '${modelName}_select' class = 'selectSimulation'>
                        ${simulationoption}
                        </select>
                        <select id = '${modelIndex}' class = 'selectForOrder'>
                          <option ${poolSize<=0?disabled : nothing}  value ="firstModel" `+ (modelIndex == "firstModel" ? `selected="selected"` : ``) + `>firstModel</option>
                          <option ${poolSize<=1?disabled : nothing} value ="secondModel" `+ (modelIndex == "secondModel" ? `selected="selected"` : ``) + `>secondModel</option>
                          <option ${poolSize<=2?disabled : nothing} value ="thirdModel" `+ (modelIndex == "thirdModel" ? `selected="selected"` : ``) + `>thirdModel</option>
                          <option ${poolSize<=3?disabled : nothing} value ="fourthModel" `+ (modelIndex == "fourthModel" ? `selected="selected"` : ``) + `>fourthModel</option>
                        </select>
                      </div>
                    </foreignObject>
                    <g class="inPorts"/>
                    <g class="outPorts"/>
                  </g>`,

      position: {
        x: x, y: y
      },
      size: {
        width: width, height: height + 60
      },
      ports: {
        groups: {
          'in': {
            attrs: {
              '.port-body': {
                fill: '#0000FF'
              }
            },
            label: {
              position: {
                name: 'left',
                args: { y: -0.3 }
              }
            }
          },
          'out': {
            attrs: {
              '.port-body': {
                fill: '#FF0000'
              }
            },
            label: {
              position: {
                name: 'right',
                args: { y: -0.3 }
              }
            }
          }
        }
      },
      attrs: {
        rect: {
          rx: 5,
          ry: 5,
          'stroke-width': 2,
          stroke: 'black'
        },
        text: {
          text: modelName,
          'font-size': 12,
          'font-weight': 'bold',
          'font-variant': 'small-caps',
          'text-transform': 'capitalize',
          margin: '20px',
          padding: '40px'
        }
      }
    });

    // Add input parameter ports
    inputs.forEach(input => atomic.addPort(input));


    // Add output parameter ports
    outputs.forEach(output => atomic.addPort(output));

    return atomic;
  }

  return view;
}();