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

  var _modelCodeMirror;
  var _visualizationCodeMirror;
  var _readmeCodeMirror;

  let handler;
  var selectionChanged = function (modelMetaData) {	
    console.log('in editor',modelMetaData);
    extractAndCreateUI(JSON.stringify(modelMetaData.changeSet.added[0]));
  }
  view.init = function (representation, value) {
    //subscribe to events emitted by FSK DB View
    knimeService.subscribeToSelection('b800db46-4e25-4f77-bcc6-db0c215846e1', selectionChanged);
    fskutil = new fskutil();
    _rep = representation;
    _val = value;
    extractAndCreateUI(value.modelMetaData);
    
  }
  function extractAndCreateUI(modelMetaData){
    if (!modelMetaData || modelMetaData == "null" || modelMetaData == "") {
      _metadata.generalInformation = {};
      _metadata.generalInformation.modelCategory = {};
      _metadata.scope = {};
      _metadata.modelMath = {};
      _metadata.dataBackground = {}
    } else {
      let recievedObject = modelMetaData instanceof Object? modelMetaData: JSON.parse(modelMetaData);
      let metaData = Array.isArray(recievedObject)?recievedObject[0]:recievedObject;

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
  }
  view.getComponentValue = () => {

    _metadata = handler.metaData;
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
  function createUI() {

    let panelsById = [
      { id: "modelScript", panel: `<textarea id="modelScriptArea">${_val.modelScript}</textarea>` },
      { id: "visualizationScript", panel: `<textarea id="visualizationScriptArea">${_val.visualizationScript}</textarea>` },
      { id: "readme", panel: `<textarea id="readmeArea" name="readmeArea">${_val.readme}</textarea>` }
    ];

    let bodyContent = `
<div class="container-fluid">
  <nav class="navbar navbar-default">
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav" id="viewTab">
        ${handler.menus}
        <li role="presentation">
          <a id="modelScript-tab" href="#modelScript"
            aria-controls="modelScript" role="tab" data-toggle="tab">Model script</a>
        </li>
        <li role="presentation">
          <a id="visualizationScript-tab" href="#visualizationScript"
            aria-controls="visualizationScript" role="tab" data-toggle="tab">Visualization script</a>
        </li>
        <li role="presentation">
          <a id="readme-tab" href="#readme" aria-controls="readme" role="tab" data-toggle="tab">README</a>
        </li>
        <li role="presentation">
          <a id="meee-tab" href="#meeee"  onClick="knimeService.log('hereis editor');knimeService.setSelectedRows('b800db46-4e25-4f77-bcc6-db0c215846d9' , [{data:'meta',seta:'geta'}],{elements:[]})" role="tab" >Meeee</a>
        </li>
      </ul>
    </div>
  </nav>
  <div class="tab-content" id="viewContent">
    ${panelsById.map(entry => `<div role="tabpanel" class="tab-pane"
    id="${entry.id}">${entry.panel}</div>`).join("")}
  </div>
</div>`;

    document.createElement('body');
    $('body').html(bodyContent);

    // Add dialogs
    const container = document.getElementsByClassName("container-fluid")[0];
    // Object.values(handler.dialogs).forEach(dialog => container.appendChild(dialog.modal));

    const viewContent = document.getElementById("viewContent");

    Object.entries(handler.panels).forEach(([key, value]) => {
      let tabPanel = document.createElement("div");
      tabPanel.setAttribute("role", "tabpanel");
      tabPanel.className = "tab-pane";
      tabPanel.id = key;
      tabPanel.appendChild(value.panel);

      // Add dialog if fskutil.TablePanel
      if (value.dialog) {
        container.appendChild(value.dialog.modal);
      }

      viewContent.appendChild(tabPanel);
    });

    // Set the first tab (general information) as active
    document.getElementById("generalInformation").classList.add("active");

    // Create code mirrors for text areas with scripts and readme
    let require_config = {
      packages: [{
        name: "codemirror",
        location: "codemirror/",
        main: "lib/codemirror"
      }]
    };

    knimeService.loadConditionally(
      ["codemirror", "codemirror/mode/r/r", "codemirror/mode/markdown/markdown"],
      (arg) => {
        window.CodeMirror = arg[0];
        _modelCodeMirror = createCodeMirror("modelScriptArea", "text/x-rsrc");
        _visualizationCodeMirror = createCodeMirror("visualizationScriptArea", "text/x-rsrc");
        _readmeCodeMirror = createCodeMirror("readmeArea", "text/x-markdown");

        _modelCodeMirror.on("blur", () => { _modelCodeMirror.focus(); });
        _visualizationCodeMirror.on("blur", () => { _visualizationCodeMirror.focus(); });
        _readmeCodeMirror.on("blur", () =>{ _readmeCodeMirror.focus(); });
      },
      (err) => console.log("knimeService failed to install " + err),
      require_config);

    $('#modelScript-tab').on('shown.bs.tab', () => {
      _modelCodeMirror.refresh(); 
      _modelCodeMirror.focus();
    });

    $('#visualizationScript-tab').on('shown.bs.tab', () => {
      _visualizationCodeMirror.refresh();
      _visualizationCodeMirror.focus();
    });
    
    $('#readme-tab').on('shown.bs.tab', () => {
      _readmeCodeMirror.refresh();
      _readmeCodeMirror.focus();
    });
  }

  // Create a CodeMirror for a given text area
  function createCodeMirror(textAreaId, language) {

    return window.CodeMirror.fromTextArea(document.getElementById(textAreaId),
      {
        lineNumbers: true,
        lineWrapping: true,
        extraKeys: { 'Ctrl-Space': 'autocomplete' },
        mode: { 'name': language }
      });
  }
}();