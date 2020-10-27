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

  /** JointJS graph. */
  let _graph;

  /** JointJS graph view. */
  let _paper;

  let _metadata;

  let _firstModelParameterMap = {};
  let _secondModelParameterMap = {};

  let modelsPool = {
    firstModel: {},
    secondModel: {},
    thirdModel: {},
    fourthModel: {}
  }
  let paperWidth;
  window.joinRelationsMap = {};

  view.init = function (representation, value) {
    _value = value;
    _joinerModelsData = representation.joinerModelsData;

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
    _paper.scaleContentToFit();
    window.toogle = true;
  }

  view.getComponentValue = function () {

    _value.joinerModelsData.firstModel = modelsPool.firstModel['metadata'];
    _value.joinerModelsData.secondModel = modelsPool.secondModel['metadata'];
    _value.joinerModelsData.thirdModel = modelsPool.thirdModel['metadata'];
    _value.joinerModelsData.fourthModel = modelsPool.fourthModel['metadata'];
    _value.joinerModelsData.firstModelType = modelsPool.firstModel['modelType'];
    _value.joinerModelsData.secondModelType = modelsPool.secondModel['modelType'];
    _value.joinerModelsData.thirdModelType = modelsPool.thirdModel['modelType'];
    _value.joinerModelsData.fourthModelType = modelsPool.fourthModel['modelType'];
    return _value;
  };

  view.getSVG = function () {
    let emptySVG = `<?xml version="1.0" encoding="UTF-8" standalone="no"?>
                        <svg xmlns="http://www.w3.org/2000/svg" version="1.1"
                            viewBox="0 0 500 500" width="500" height="500" id="starter_svg">
  
                            <!-- Place your SVG elements here... -->
  
                        </svg>`;
    if (!_paper) return (new XMLSerializer()).serializeToString(emptySVG);
    _paper.svg.setAttribute("width", $('#viewContent').width());
    _paper.svg.setAttribute("height", 500);
    return (new XMLSerializer()).serializeToString(_paper.svg);
  };

  function editModelsPool(key, modelParameters, modelName, individualMetadata, modelType) {
    modelsPool[key]['modelName'] = modelName;
    modelsPool[key]['modelType'] = modelType;
    modelsPool[key]['metadata'] = individualMetadata
    modelsPool[key]['inputParameters'] = [];
    modelsPool[key]['outputParameters'] = [];
    modelsPool[key]['portMap'] = {};
    modelsPool[key]['modelParameterMap'] = {};
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

    modelsPool[key]['modelToJoin'] = createAtomic(paperWidth - (250 * (4 - (Object.keys(modelsPool).indexOf(key)))), 60, 150,
      modelHeight, modelNameWrap, modelsPool[key]['inputParameters'],
      modelsPool[key]['outputParameters'], key);

    _graph.clear();
    $.each(Object.keys(modelsPool), function (index, value) {
      if (Object.keys(modelsPool[value]).length > 0)
        _graph.addCell(modelsPool[value]['modelToJoin']);
    })
    graphJSON = _graph.toJSON();
    reoderTool();
  }

  function createBody() {

    $('body').html(`<div class="container-fluid">
    <nav class="navbar navbar-default">
     <div class="navbar-collapse collapse">
       
     </div>
    
      <div class="tab-content" id="viewContent">
        <div role="tabpanel" class="tab-pane active" id="joinPanel">
          <div id="paper"></div>
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

      _paper.setDimensions($('#viewContent').width(), 500);
      _paper.scaleContentToFit();
      //_paper.scaleContentToFit({ padding: 20 });
    };
    reoderTool();


  }

  function reoderTool() {

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
            oldSuffix = 3;
            break;
          case "secondModel":
            oldSuffix = 3;
            break;
          case "thirdModel":
            oldSuffix = 2;
            break;
          case "fourthModel":
            oldSuffix = 1;
            break;

        }
        switch (newValueText) {
          case "firstModel":
            newSuffix = 3;
            break;
          case "secondModel":
            newSuffix = 3;
            break;
          case "thirdModel":
            newSuffix = 2;
            break;
          case "firstModel":
            newSuffix = 1;
            break;

        }
        oldParams = modelsPool[previous]['modelParameters'];
        newParams = modelsPool[newValueText]['modelParameters'];
        preapareParametersForReorder(newParams, newSuffix, oldParams, oldSuffix);
        let tempModelName = modelsPool[previous]['modelName'];
        modelsPool[previous] = newValue;
        modelsPool[previous]['modelName'] = modelsPool[newValueText]['modelName'];
        modelsPool[newValueText]['modelName'] = tempModelName;
        modelsPool[newValueText] = oldValue;

        editModelsPool(previous, newParams, modelsPool[previous]['modelName'], modelsPool[previous]['metadata'], modelsPool[previous]['modelType'])
        editModelsPool(newValueText, oldParams, modelsPool[newValueText]['modelName'], modelsPool[newValueText]['metadata'], modelsPool[newValueText]['modelType']);
      }
      $("#" + newValueText + ' option').filter(function () {
        return $(this).text() == previous;
      }).prop('selected', 'selected');
      $("#" + newValueText).prop('id', 'med');
      $("#" + previous).prop('id', newValueText);
      $("#med").prop('id', previous);
    });
  }
  function preapareParametersForReorder(newParams, newSuffix, oldParams, oldSuffix) {
    newSuffix = newParams[0].id.substring(newParams[0].id.length - newSuffix, newParams[0].id.length);
    oldSuffix = oldParams[0].id.substring(oldParams[0].id.length - oldSuffix, oldParams[0].id.length);
    for (param of oldParams) {
      param.id = param.id.replace(oldSuffix, newSuffix);
    }
    for (param of newParams) {
      param.id = param.id.replace(newSuffix, oldSuffix);
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
    _paper.setDimensions($('#viewContent').width(), 500);


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

    _joinerModelsData.firstModelName ? editModelsPool('firstModel', _joinerModelsData.firstModelParameters, _joinerModelsData.firstModelName, _joinerModelsData.firstModel, _joinerModelsData.firstModelType) : '';
    _joinerModelsData.secondModelName ? editModelsPool('secondModel', _joinerModelsData.secondModelParameters, _joinerModelsData.secondModelName, _joinerModelsData.secondModel, _joinerModelsData.secondModelType) : '';
    _joinerModelsData.thirdModelName ? editModelsPool('thirdModel', _joinerModelsData.thirdModelParameters, _joinerModelsData.thirdModelName, _joinerModelsData.thirdModel, _joinerModelsData.thirdModelType) : '';
    _joinerModelsData.fourthModelName ? editModelsPool('fourthModel', _joinerModelsData.fourthModelParameters, _joinerModelsData.fourthModelName, _joinerModelsData.fourthModel, _joinerModelsData.fourthModelType) : '';
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
      if (!targetPort) {
        return;
      }

      highlight(linkView);

      // Update form
      document.getElementById("source").value = sourcePort;
      document.getElementById("target").value = targetPort;

      let command = document.getElementById("Command");
      command.value = "[" + sourcePort + "]";
      command.onkeyup = () => window.sJoinRealtion.command = command.value;

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
          command: command.value,//sourcePort
          sourceModel: sModel,
          targetModel: tModel
        };

        if (_metadata.generalInformation.languageWrittenIn.length > 0) {
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

    if (_value && _value.jsonRepresentation) {
      let graphObject = JSON.parse(_value.jsonRepresentation)
      _graph.fromJSON(graphObject);

      if (graphObject.cells) {
        for (const cell of graphObject.cells) {
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
        'markup': `<text class="label-text" fill="black"><title>${param.dataType}</title>${param.id}</text>`
      },
      'markup': `<circle fill="#FF7979"  r="92.5" class="port-body"><title>Parameter ID: ` + param.id + `\nParameter Name: ` + param.name + `\nDescription: ` + param.description + `\nUnit: ` + param.unit + `\nDataType: ` + param.dataType + `\nSource: ` + param.source + `\nSubject: ` + param.subject + `\nDistribution: ` + param.distribution + `\nReference: ` + param.reference + `\nVariabilitySubject: ` + param.variabilitySubject + `\nMinValue: ` + param.minValue + `\nMaxValue: ` + param.maxValue + `\nError: ` + param.error + `\n</title></circle>`,
    };
  }

  /**
   * Call-back function for updating the form when a link between two ports is clicked.
   */
  function updateForm(evt, cellView, magnet, arrowhead) {
    let sourcePort = evt.model.get("source").port;
    let targetPort = evt.model.get("target").port;

    window.sJoinRealtion = window.joinRelationsMap[sourcePort + "," + targetPort];

    document.getElementById("source").value = sourcePort;
    document.getElementById("target").value = targetPort;

    let commandLanguage = document.getElementById("commandLanguage");
    commandLanguage.value = window.sJoinRealtion.language_written_in;
    commandLanguage.onchange = () => window.sJoinRealtion.language_written_in = commandLanguage.value;

    let commandTextArea = document.getElementById("Command");
    commandTextArea.value = window.sJoinRealtion.command;
    commandTextArea.onkeyup = () => window.sJoinRealtion.command = commandTextArea.value;
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
  function createAtomic(x, y, width, height, modelName, inputs, outputs, modelIndex) {
    let atomic = new joint.shapes.devs.Atomic({
      markup: `<g>
                      <rect class="body" stroke-width="0" rx="5px" ry="5px"></rect>
                      <text class="label" y="0.8em" xml:space="preserve" font-size="14" text-anchor="middle" font-family="Arial, helvetica, sans-serif">
                        <tspan id="v-18" dy="0em" x="0" class="v-line"></tspan>
                      </text>
                      <foreignObject  x="20" y="20" width="${width}" height="${height + 30}" >
                      <!--
                        In the context of SVG embedded in an HTML document, the XHTML 
                        namespace could be omitted, but it is mandatory in the 
                        context of an SVG document
                      -->
                      <div class="html-element" style="position: absolute;
                      right: 35px;
                      bottom: 25px;" xmlns="http://www.w3.org/1999/xhtml">
                        <select id = '${modelIndex}' class = 'selectForOrder'>
                          <option value ="firstModel" `+ (modelIndex == "firstModel" ? `selected="selected"` : ``) + `>firstModel</option>
                          <option value ="secondModel" `+ (modelIndex == "secondModel" ? `selected="selected"` : ``) + `>secondModel</option>
                          <option value ="thirdModel" `+ (modelIndex == "thirdModel" ? `selected="selected"` : ``) + `>thirdModel</option>
                          <option value ="fourthModel" `+ (modelIndex == "fourthModel" ? `selected="selected"` : ``) + `>fourthModel</option>
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
        width: width, height: height + 30
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