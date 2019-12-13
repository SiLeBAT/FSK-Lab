joiner = function() {

    const view = { version: "1.0.0", name: "FSK Joiner"};

    let _representation;
    let _value;

    /** JointJS graph. */
    let _graph;

    /** JointJS graph view. */
    let _paper;

    let _firstModelMath;
    let _secondModelMath;

    let _firstModelParameterMap = {};
    let _secondModelParameterMap = {};

    let _firstModelName;
    let _secondModelName;

    view.init = function(representation, value) {
        _representation = representation;
        _value = value;

        // TODO: before creating body:
        // TODO: process metadata

        _firstModelName = value.firstModelName;
        _secondModelName = value.secondModelName;

        _firstModelMath = JSON.parse(value.modelMath1);
        _secondModelMath = JSON.parse(value.modelMath2);

        createBody();
    }

    view.getComponentValue = function() {
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
       <a href="#generalInformationPanel" aria-controls="generalInformationPanel">General information</a>
     </li>
     <li role="presentation">
       <a href="#scopePanel" aria-controls="scopePanel">Scope</a>
     </li>
     <li role="presentation">
       <a href="#dataBackgroundPanel" aria-controls="dataBackgroundPanel">Data background</a>
     </li>
     <li role="presentation">
       <a href="#modelMathPanel" aria-controls="modelMathPanel">Model math</a>
     </li>
   </ul>
 </div>
 <div class="tab-content" id="viewContent">
   <div role="tabpanel" class="tab-pane active" id="joinPanel">
     <div id="paper">
     </div>
     <div id="details" class="col-sm-12">
     </div>
   </div>
 </div>
</nav>
</div>`);

        drawWorkflow();

        // Initialize tab function
        $('.nav a').click((event) => {
            event.preventDefault();
            $(this).tab('show');

            let canvas = $('#paper');
            _paper.setDimensions(canvas.width(), canvas.height());
        });

        $('.nav a').on('shown.bs.tab', function(event) {
            if (event.target.id === "join-tab") {
              let canvas = $('#paper');
              _paper.setDimensions(canvas.width(), canvas.height());
            }
        });
    }

    function drawWorkflow() {

        _graph = new joint.dia.Graph();

        _paper = new joint.dia.Paper({
            el: document.getElementById('paper'),
            drawGrid: 'mesh',
            gridSize : 10,
            model : _graph,
            snapLinks : true,
            linkPinning : true,
            drawGrid : true,
            
            highlighting: {
              'default' : {
                name : 'stroke',
                options : {
                  padding : 6
                }
              }
            },
            
            interactive: (cellView) => {
                // Disable the default vertex and add functionality on pointerdown
                if (cellView.model instanceof joint.dia.Link) {
                    return { vertexAdd: false};
                }
                return true;
            },
            validateConnection: function(cellViewS, magnetS, cellViewT, magnetT, end, linkView) {
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
            let sourcePort = event.model.attributes.source.port;
            let targetPort = event.model.attributes.target.port;
            if (!targetPort) {
              event.remove();
            }
          }
        });

        // Pointer is double clicked on a target
        _paper.on('cell:pointerdblclick', (cellView, event, x, y) => {
          if (!(cellView.model instanceof joint.dia.Link)) return;

          let link = cellView.model;

          let sourcePort = link.get('source').port;
          let targetPort = link.get('target').port;

          window.sJoinRealtion = window.joinRelationsMap[sourcePort + "," + targetPort];

          if (!document.getElementById("commandLanguage")) {
            let detailsForm = createDetailsForm(sourcePort, targetPort);
            $("#details").html(detailsForm);
          }

          document.getElementById("source").value = sourcePort;
          document.getElementById("target").value = targetPort;
          
          let commandLanguage = $("#commandLanguage");
          commandLanguage.val(sJoinRealtion.language_written_in);
          commandLanguage.onchange(() => window.sJoinRealtion.language_written_in = commandLanguage.val());

          let commandTextArea = $("Command");
          commandTextArea.val(sJoinRealtion.command);
          commandTextArea.keyup(() => window.sJoinRealtion.command = commandTextArea.val());
        });

        let firstModelInputParameters = [];
        let firstModelOutputParameters = [];
        window.firstPortMap = {};
        window.secondPortMap = {};
        try {

          _.each(_firstModelMath.parameter, function(param) {
            if (!_firstModelParameterMap[param.id]) {
              let port = {
                id: param.id,
                label: { markup: createMarkup(param.id, param.dataType)}
              };
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
          });
        } catch (err) {
          console.log(err);
        }

        let secondModelInputParameters = [];
        let secondModelOutputParameters = [];
        _.each(_secondModelMath.parameter, function(param) {
          if (!_firstModelParameterMap[param.id]) {
            let port = {
              id: param.id,
              label: { markup: createMarkup(param.id, param.dataType) }
            };
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
        });

        let canvas = $("#paper");
        canvas.height(Math.max(firstModelInputParameters.length,
          secondModelInputParameters.length) * 25 + 300);

        _paper.setDimensions(canvas.width(), canvas.height());

        let paperWidth = canvas.width();
        let firstModelHeight = Math.max(firstModelInputParameters.length, firstModelOutputParameters.length) * 25;

        let firstModelNameWrap = joint.util.breakText(_firstModelName, {
          width: 200,
          height: firstModelHeight
        });

        let firstModelToJoin = new joint.shapes.devs.Atomic({
          position: { x: paperWidth - 670, y: 60 },
          size: { width: 200, height: firstModelHeight },
          ports: {
            groups: {
              'in': {
                attrs: { '.port-body': { fill: '#16A085' }}
              },
              'out': {
                attrs: { '.port-body': { fill: '#E74C3C' }}
              }
            }
          }
        });

        // Add input ports to firstModelToJoin
        $.each(firstModelInputParameters, function(index, value) {
          try {
            firstModelToJoin.addPort(value);
          } catch (err) {
            console.log(err, value);
          }
        });

        // Add output ports to firstModelToJoin
        $.each(firstModelOutputParameters, function(index, value) {
          try {
            firstModelToJoin.addPort(value);
          } catch (err) {
            console.log(err, value);
          }
        });

        firstModelTojoin.attr({
          rect : { rx : 5, ry : 5, 'stroke-width' : 2, stroke : 'black' },
          text : {
            text : firstModelNameWrap,
            'font-size' : 12,
            'font-weight' : 'bold',
            'font-variant' : 'small-caps',
            'text-transform' : 'capitalize',
            margin : '20px',
            padding : '40px'
          }
        });

        let secondModelHeight = Math.max(secondModelInputParameters.length, secondModelOutputParameters.length) * 25;
        let secondModelNameWrap = joint.util.breakText(secondModelName, {
          width: 200,
          height: secondModelHeight
        });

        // TODO: ...
        let secondModelToJoin = new joint.shapes.devs.Atomic({

        });
    }

    /**
     * Return HTML string for the details form.
     * @param {*} sourcePort 
     * @param {*} targetPort 
     */
    function createDetailsForm(sourcePort, targetPort) {
      return `<form action="">
<div class="form-group row">
  <label class="col-6 col-form-label" for="source">Source Port:</label>
  <div class="col-6">
    <input type="text" class="form-control" id="source" value="${sourcePort}">
  </div>
</div>
<div class="form-group row">
  <label class="col-6 col-form-label" for="target">Target Port:</label>
  <div class="col-6">
    <input type="text" class="form-control" id="target" value="${targetPort}">
  </div>
</div>
<div class="form-group row">
  <label class="col-6 col-form-label" for="commandLanguage">Command language:</label>
  <div class="col-6">
    <input type="text" class="form-control" id="commandLanguage">
  </div>
</div>
<div class="form-group row">
  <label class="col-6 col-form-label" for="Command">Conversion command:</label>
  <div class="col-6">
    <textarea class="form-control" rows="3" id="Command">${sourcePort}</textarea>
  </div>
</div>
</form>`;
    }

    /**
     * Create HTML markup for a parameter.
     * @param {string} id Parameter id
     * @param {string} type Parameter type
     */
    function createMarkup(id, type) {
      return `<text class="label-text" fill="black"><title>${type}</title>${id}</text>`;
    }
}();