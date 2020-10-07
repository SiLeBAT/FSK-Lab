joiner = function () {

  // Dimension utility functions.
  // Returns the windows width minus the left and right padding.
  //const getChartWidth = () => window.innerWidth - 32;

  // Returns the window height minus the navbar and modal footer
  //const getChartHeight = () => window.innerHeight - $(".navbar-collapse").height() - $(".modal-footer").height() - 300;

  const view = { version: "1.0.0", name: "FSK Joiner" };
  let _representation;
  let _value;

  let _modelScriptTree;

  /** JointJS graph. */
  let _graph;

  /** JointJS graph view. */
  let _paper;

  let _metadata;

  let _firstModelParameterMap = {};
  let _secondModelParameterMap = {};


  window.joinRelationsMap = {};

  view.init = function (representation, value) {

    _representation = representation;
    _value = value;

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
      
			$.each(_value.joinRelations, function(index, value) {
				if (value) {
					window.joinRelationsMap[value.sourceParam + "," + value.targetParam] = value ;	
				}
			});
		} else {
      _value.joinRelations = [];
    }


    
    createBody();
    _paper.scaleContentToFit();
  }

  view.getComponentValue = function() {  
    return _value;
  };

  view.getSVG = function() {
    if (!_paper) return null;
    _paper.svg.setAttribute("width", $('#viewContent').width());
    _paper.svg.setAttribute("height", 500);
    return (new XMLSerializer()).serializeToString(_paper.svg);
  };

  return view;

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

    <!-- Add code panels -->
    <div role="tabpanel" class="tab-pane" id="modelScriptA">
      <textarea id="modelScriptAArea">${_representation.firstModelScript}</textarea>
    </div>
    
    <div role="tabpanel" class="tab-pane" id="modelScriptB">
     <textarea id="modelScriptBArea">${_representation.secondModelScript}</textarea>
    </div>

    <div role="tabpanel" class="tab-pane" id="visualizationScriptA">
      <textarea id="visualizationScriptAArea">${_representation.firstModelViz}</textarea>
    </div>

    <div role="tabpanel" class="tab-pane" id="visualizationScriptB">
      <textarea id="visualizationScriptBArea">${_representation.secondModelViz}</textarea>
    </div>

   </div> <!-- viewContent -->

 </div>
</nav>
</div>`);

  

    drawWorkflow();

    // Resize event. Resize the paper with the window.
    window.onresize = () => {
    //  _paper.setDimensions(getChartWidth(), getChartHeight());
    
      _paper.setDimensions($('#viewContent').width(),500);
      _paper.scaleContentToFit();
      //_paper.scaleContentToFit({ padding: 20 });
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
        for (var i = 0; i < links.length; i++)
        {  
            if(linkView == links[i].findView(_paper)) //Skip the wire the user is drawing
            continue;
    
            if ( (( cellViewT.model.id  == links[i].get('source').id ) && ( magnetT.getAttribute('port') == links[i].get('source').port)) ||
            (( cellViewT.model.id  == links[i].get('target').id ) && ( magnetT.getAttribute('port') == links[i].get('target').port)) ){
                return false;
            }
        } 
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

          //output to output does not make sense
        if (magnetT && magnetT.getAttribute('port-group') === 'out' &&
            magnetS && magnetS.getAttribute('port-group') === 'out' )
          return false;

            //input to input not supported right now
        // if (magnetT && magnetT.getAttribute('port-group') === 'in' &&
        //   magnetS && magnetS.getAttribute('port-group') === 'in' )
        //   return false;

            //inputS to outpuT_Target not sensible
         if (magnetT && magnetT.getAttribute('port-group') === 'out' &&
            magnetS && magnetS.getAttribute('port-group') === 'in' )
            return false;
               
        return true;
      },

      // Enable marking available cells and magnets
      markAvailable: true
    });
    _paper.setDimensions($('#viewContent').width(),500);
    

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

    let firstModelInputParameters = [];
    let firstModelOutputParameters = [];
    window.firstPortMap = {};
    window.secondPortMap = {};

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

    let canvas = $("#paper");
    let paperWidth = canvas.width();
    let firstModelHeight = Math.max(firstModelInputParameters.length, firstModelOutputParameters.length) * 25;

    let firstModelNameWrap = joint.util.breakText(_representation.firstModelName, {
      width: 200,
      height: firstModelHeight
    });

    let firstModelToJoin = createAtomic(paperWidth - 670, 60, 200,
      firstModelHeight, firstModelNameWrap, firstModelInputParameters,
      firstModelOutputParameters);
    firstModelToJoin.on('change:position', (element) => {
      let cellView = _paper.findViewByModel(element);
      highlight(cellView);
    });

    let secondModelHeight = Math.max(secondModelInputParameters.length, secondModelOutputParameters.length) * 25;
    let secondModelNameWrap = joint.util.breakText(_representation.secondModelName, {
      width: 200,
      height: secondModelHeight
    });

    let secondModelToJoin = createAtomic(paperWidth - 330, 180, 200,
      secondModelHeight, secondModelNameWrap, secondModelInputParameters,
      secondModelOutputParameters);
    secondModelToJoin.on('change:position', (element) => {
      let cellView = _paper.findViewByModel(element);
      highlight(cellView);
    });

    // Update form when a link is selected (clicked)
    //_paper.on("link:pointerclick", updateForm);

    _paper.on('link:connect', function(linkView, evt, elementViewDisconnected, magnet, arrowhead) {
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
      command.value = "[" + sourcePort +"]";
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
          command: command.value//sourcePort
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
          source: { id: firstNodeId, port: portIds[0] },
          target: { id: secondNodeId, port: portIds[1] }
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
                fill: '#0000FF'
              }
            },
            label: {
              position: {
                name : 'left',
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
                name : 'right',
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

  
}();