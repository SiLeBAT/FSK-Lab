joiner = function () {

  // Dimension utility functions.
  // Returns the windows width minus the left and right padding.
  const getChartWidth = () => window.innerWidth - 32;

  // Returns the window height minus the navbar and modal footer
  const getChartHeight = () => window.innerHeight - $(".navbar-collapse").height() - $(".modal-footer").height() - 300;

  const view = { version: "1.0.0", name: "FSK Joiner" };

  let _representation;
  let _value;

  let _modelScriptTree;

  /** JointJS graph. */
  let _graph;

  /** JointJS graph view. */
  let _paper;

  let _firstModel;

  let _firstModelMath;
  let _secondModelMath;

  let _firstModelParameterMap = {};
  let _secondModelParameterMap = {};

  let _firstModelName;
  let _secondModelName;

  view.init = function (representation, value) {
    _representation = representation;
    _value = value;

    // TODO: before creating body:
    // TODO: process metadata

    if (value.modelMetaData) {
      _firstModel = JSON.parse(value.modelMetaData);
    } else {
      _firstModel = {
        generalInformation: {},
        scope: {},
        dataBackground: {},
        modelMath: {}
      };
    }

    _firstModelName = value.firstModelName;
    _secondModelName = value.secondModelName;

    _firstModelMath = JSON.parse(value.modelMath1);
    _secondModelMath = JSON.parse(value.modelMath2);

    _modelScriptTree = JSON.parse(value.modelScriptTree);

    window.joinRelationsMap = {};

    createBody();
  }

  view.getComponentValue = function () {
    // TODO: getComponentValue
    return _value;
  }

  return view;

  function createBody() {

    $('body').html(`<div class="container-fluid">
<nav class="navbar navbar-default">
 <div class="navbar-collapse collapse">
   <ul class="nav navbar-nav" id="viewTab">
     <li role="presentation">
       <a id="join-tab" href="#joinPanel" aria-controls="joinPanel" role="tab" data-toggle="tab">Join</a>
     </li>
     <li role="presentation">
       <a href="#generalInformationPanel" aria-controls="generalInformationPanel" role="tab" data-toggle="tab">General information</a>
     </li>
     <li role="presentation">
       <a href="#scopePanel" aria-controls="scopePanel" role="tab" data-toggle="tab">Scope</a>
     </li>
     <li role="presentation">
       <a href="#dataBackgroundPanel" aria-controls="dataBackgroundPanel" role="tab" data-toggle="tab">Data background</a>
     </li>
     <li role="presentation">
       <a href="#modelMathPanel" aria-controls="modelMathPanel" role="tab" data-toggle="tab">Model math</a>
     </li>
   </ul>
 </div>

  <div class="tab-content" id="viewContent">
    <div role="tabpanel" class="tab-pane active" id="joinPanel">
      <div id="paper"></div>
      <form id="detailsForm">
        <div class="form-group row">
          <label class="col-sm-2 col-form-label" for="source">Source Port:</label>
          <div class="col-sm-10"><input type="text" class="form-control" id="source"></div>
        </div>
        <div class="form-group row">
          <label class="col-sm-2 col-form-label" for="target">Target Port:</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="target"">
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
    </div>

   </div> <!-- tabpanel -->
   <div role="tabpanel" class="tab-pane" id="generalInformationPanel">
   </div>
   <div role="tabpanel" class="tab-pane" id="scopePanel">
   </div>
   <div role="tabpanel" class="tab-pane" id="dataBackgroundPanel">
   </div>
   <div role="tabpanel" class="tab-pane" id="modelMathPanel">
   </div>
 </div>
</nav>
</div>`);

    drawWorkflow();

    // Resize event. Resize the paper with the window.
    window.onresize = () => {
      _paper.setDimensions(getChartWidth(), getChartHeight());
      _paper.scaleContentToFit({ padding: 20 });
    };
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
      width: getChartWidth(),
      height: getChartHeight(),

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
        if (linkView.sourceView.id != linkView.paper.viewport.children[0].id)
          return false;

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

        return true;
      },
      // Enable marking available cells and magnets
      markAvailable: true
    });

    let previousOne;

    // Pointer is clicked on a cell
    _paper.on('cell:pointerclick', (cellView) => {
      if (previousOne) previousOne.unhighlight();
      previousOne = cellView;
      cellView.highlight();
    });

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

    // Update form when a link is double clicked
    _paper.on("cell:pointerdblclick", updateForm);

    let firstModelInputParameters = [];
    let firstModelOutputParameters = [];
    window.firstPortMap = {};
    window.secondPortMap = {};

    for (param of _firstModelMath.parameter) {
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

    for (param of _secondModelMath.parameter) {
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
    };

    let canvas = $("#paper");
    let paperWidth = canvas.width();
    let firstModelHeight = Math.max(firstModelInputParameters.length, firstModelOutputParameters.length) * 25;

    let firstModelNameWrap = joint.util.breakText(_firstModelName, {
      width: 200,
      height: firstModelHeight
    });

    let firstModelToJoin = createAtomic(paperWidth - 670, 60, 200,
      firstModelHeight, firstModelNameWrap, firstModelInputParameters,
      firstModelOutputParameters);

    let secondModelHeight = Math.max(secondModelInputParameters.length, secondModelOutputParameters.length) * 25;
    let secondModelNameWrap = joint.util.breakText(_secondModelName, {
      width: 200,
      height: secondModelHeight
    });

    let secondModelToJoin = createAtomic(paperWidth - 330, 180, 200,
      secondModelHeight, secondModelNameWrap, secondModelInputParameters,
      secondModelOutputParameters);

    // Update form when a link is selected (clicked)
    _paper.on("link:pointerclick", updateForm);

    _paper.on('link:connect', function (evt, cellView, magnet, arrowhead) {
      sourcePort = evt.model.attributes.source.port;
      targetPort = evt.model.attributes.target.port;
      if (!targetPort) {
        return;
      }

      // Update form
      document.getElementById("source").value = sourcePort;
      document.getElementById("target").value = targetPort;

      let command = document.getElementById("Command");
      command.value = sourcePort;
      command.onkeyup = () => window.sJoinRealtion.command = command.value;

      command.onblur = () => {
        joinModelScript = "";

        $.each(_viewValue.joinRelations, function (index, value) {
          joinModelScript += `${value.targetParam.parameterID} <- ${value.command} \n`;
          _modelScriptTree[1].script = joinModelScript;
        });
      }

      let sourceParameter = _firstModelParameterMap[sourcePort] ?
        _firstModelParameterMap[sourcePort] : _secondModelParameterMap[sourcePort];

      let targetParameter = _secondModelParameterMap[sourcePort] ?
        _secondModelParameterMap[sourcePort] : _firstModelParameterMap[sourcePort];

      if (targetParameter != undefined) {
        window.sJoinRealtion = {
          sourceParam: sourceParameter,
          targetParam: targetParameter,
          command: sourcePort
        };

        if (_firstModel.generalInformation.languageWrittenIn) {
          window.sJoinRealtion.language_written_in = _firstModel.generalInformation.languageWrittenIn;
          $('#commandLanguage').val(_firstModel.generalInformation.languageWrittenIn);
        }

        if (!_value.joinRelations) {
          _value.joinRelations = []
        }

        _value.joinRelations.push(sJoinRealtion);
        window.joinRelationsMap[sourcePort + "," + targetPort] = sJoinRealtion
        joinModelScript = "";

        $.each(_value.joinRelations, function (index, value) {
          joinModelScript += `${value.targetParam.parameterID} <- ${value.command}\n`;
          _modelScriptTree[1].script = joinModelScript
        });

        _value.jsonRepresentation = JSON.stringify(_graph.toJSON());
      }
    }); // paper.on('link:connect')

    _graph.on('remove', (link) => {
      sourcePort = link.attributes.source.port;
      targetPort = link.attributes.target.port;

      let sourceParameter = _firstModelParameterMap[sourcePort] ?
        _firstModelParameterMap[sourcePort] : _secondModelParameterMap[sourcePort];

      let targetParameter = _secondModelParameterMap[sourcePort] ?
        _secondModelParameterMap[sourcePort] : _firstModelParameterMap[sourcePort];

      if (targetParameter != undefined) {
        $.each(_viewValue.joinRelations, function (index, value) {

          if (value != undefined && value.sourceParam.parameterID == sourceParameter.parameterID
            && value.targetParam.parameterID == targetParameter.parameterID) {
            _viewValue.joinRelations.splice(index, 1);
          }
        });
        _viewValue.jsonRepresentation = JSON.stringify(graph.toJSON());
      }
    });

    if (_value.jsonRepresentation != undefined) {
      if (_value && _value.jsonRepresentation) {
        try {
          graphObject = JSON.parse(_value.jsonRepresentation)
          graph.fromJSON(JSON.parse(_value.jsonRepresentation));
          $.each(graphObject.cells, function (cellIndex, cell) {
            $.each(cell.ports.items, function (index, item) {
              graph.getCell(cell.id).addPort(item);
            })
          })

        } catch (err) {
        }
      }
    } else {
      _graph.addCells([firstModelToJoin, secondModelToJoin]);
      graphJSON = _graph.toJSON();
      firstNodeId = graphJSON['cells'][0].id;
      secondNodeId = graphJSON['cells'][1].id;
      var links = [];
      $.each(window.joinRelationsMap, function (key, value) {
        var portIds = key.split(",");

        firstPort = window.firstPortMap[portIds[0]]
        secondPort = window.secondPortMap[portIds[1]]

        let link = new joint.shapes.devs.Link({
          source: {
            id: firstNodeId,
            port: portIds[0]
          },
          target: {
            id: secondNodeId,
            port: portIds[1]
          }
        });

        links.push(link);
      });
      _graph.addCells(links);
    }
  }

  /**
   * Create a JointJS port for a model parameter.
   * @param {string} id Parameter id 
   * @param {string} dataType Parameter data type 
   */
  function createPort(id, dataType) {
    return {
      'id': id,
      'label': {
        'markup': `<text class="label-text" fill="black"><title>${dataType}</title>${id}</text>`
      },
      'attrs': { 'font-size': 10 }
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
    commandTextArea.onkeyup = () => window.sJoinRealtion.command = commandTextArea.val()
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
  function createAtomic(x, y, width, height, modelName, inputs, outputs) {
    let atomic = new joint.shapes.devs.Atomic({
      position: {
        x: x, y: y
      },
      size: {
        width: width, height: height
      },
      ports: {
        groups: {
          'in': {
            attrs: {
              '.port-body': {
                fill: '#16A085'
              }
            }
          },
          'out': {
            attrs: {
              '.port-body': {
                fill: '#E74C3C'
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
}();