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

  view.init = function (representation, value) {

    _rep = representation;
    _val = value;

    if (!value.modelMetaData || value.modelMetaData == "null" || value.modelMetaData == "") {
      _metadata.generalInformation = {};
      _metadata.scope = {};
      _metadata.modelMath = {};
      _metadata.dataBackground = {}
    } else {
      let metaData = JSON.parse(value.modelMetaData);
      if (metaData) {
        metaData = traverse(metaData);
      }

      _metadata.generalInformation = metaData.generalInformation;
      _metadata.scope = metaData.scope;
      _metadata.modelMath = metaData.modelMath;
      _metadata.dataBackground = metaData.dataBackground;
    }

    createUI();
  }

  view.getComponentValue = () => _val;
  view.validate = () => true;

  return view;

  /** Functions taken from the old editor to parse metadata. */
  function isObj(obj) {
    return obj ? obj.constructor.name === "Object" : false;
  }

  function traverse(obj) {
    let keys = Object.keys(obj)
    for (let i = 0; i < keys.length; ++i) {
      let val = obj[keys[i]]
      if (isObj(val)) {
        traverse(val)
      } else if (Array.isArray(val)) {
        for (let j = 0; j < val.length; ++j) {
          if (isObj(val[j]) && val[j] != null) {
            traverse(val[j])
          } else {
            delete obj[keys[i]];
          }
        }
      } else {
        if (val == null) {
          delete obj[keys[i]]
        }
      }
    }
    return obj;
  }

  /** UI code. */
  function createUI() {

    let bodyContent = `
<div class="container-fluid">
  <ul class="nav nav-tabs" id="viewTab" role="tablist">
    ${createSubMenu("General information", [{ "id": "generalInformation", "label": "General" },
      { "id": "modelCategory", "label": "Model category" },
      { "id": "modificationDate", "label": "Modification date" },
      { "id": "author", "label": "Author" },
      { "id": "creator", "label": "Creator" },
      { "id": "reference", "label": "Reference" }], true)}
    ${createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
      { "id": "product", "label": "Product" },
      { "id": "hazard", "label": "Hazard" },
      { "id": "population", "label": "Population group" },
      { "id": "spatialInformation", "label": "Spatial information" }])}
    <li role="presentation">
      <a href="#modelScript" aria-controls="modelScript" role="tab"
        data-toggle="tab">Model script</a>
    </li>
    <li role="presentation">
      <a href="#visualizationScript" aria-controls="visualizationScript"
        role="tab" data-toggle="tab">Visualization script</a>
    </li>
    <li role="presentation">
      <a href="#readme" aria-controls="readme" role="tab" data-toggle="tab">README</a>
    </li>
  </ul>

  <div class="tab-content" id="viewContent">
    <div role="tabpanel" class="tab-pane active" id="generalInformation">
      ${createGeneralInformationForm()}
    </div>

    <div role="tabpanel" class="tab-pane" id="modelCategory">
      ${createModelCategory()}
    </div>

    <div role="tabpanel" class="tab-pane" id="modificationDate">
      ${createPanel("Modification date", createDateTable("Modification date"))}
    </div>

    <div role="tabpanel" class="tab-pane" id="author">${createTablePanel("Author", "contactDialog", ui['contact'])}</div>
    <div role="tabpanel" class="tab-pane" id="creator">${createTablePanel("Creator", "contactDialog", ui['contact'])}</div>
    <div role="tabpanel" class="tab-pane" id="reference">${createTablePanel("Reference", "referenceDialog", ui['reference'])}</div>

    <div role="tabpanel" class="tab-pane" id="scopeGeneral">Scope general</div>
    <div role="tabpanel" class="tab-pane" id="product">${createTablePanel("Product", "productDialog", ui['product'])}</div>
    <div role="tabpanel" class="tab-pane" id="hazard">${createTablePanel("Hazard", "hazardDialog", ui['hazard'])}</div>
    <div role="tabpanel" class="tab-pane" id="population">${createTablePanel("Population group", "populationDialog", ui['populationGroup'])}</div>
    <div role="tabpanel" class="tab-pane" id="spatialInformation">${createStringTable("Spatial information", "spatialInformation")}</div>

    <div role="tabpanel" class="tab-pane" id="modelScript">
      <textarea id="modelScriptArea" name="modelScriptArea">${_val.firstModelScript}</textarea>    
    </div>

    <div role="tabpanel" class="tab-pane" id="visualizationScript">
      <textarea id="visualizationScriptArea" name="visualizationScriptArea">${_val.firstModelViz}</textarea>    
    </div>

    <div role="tabpanel" class="tab-pane" id="readme">
      <textarea id="readmeArea" name="readmeArea">${_val.readme}</textarea>
    </div>
  </div>
  <!-- Modal dialogs -->
  ${createDialog("contactDialog", ui['contact'])}
  ${createDialog("referenceDialog", ui['reference'])}
  ${createDialog("productDialog", ui['product'])}
  ${createDialog("hazardDialog", ui['hazard'])}
  ${createDialog("populationDialog", ui['populationGroup'])}
</div>`;

    document.createElement('body');
    $('body').html(bodyContent);

    // Create code mirrors for text areas with scripts and readme
    _modelCodeMirror = createCodeMirror("modelScriptArea", "text/x-rsrc");
    _visualizationCodeMirror = createCodeMirror("visualizationScriptArea", "text/x-rsrc");
    _readmeCodeMirror = createCodeMirror("readmeArea", "htmlmixed");

    // Every time a tab is shown
    $('.nav-tabs a').on('shown.bs.tab', () => {
      // Refresh code mirrors
      _modelCodeMirror.refresh();
      _visualizationCodeMirror.refresh();
      _readmeCodeMirror.refresh();
    });
  }

  /**
   * Create a Bootstrap dropdown menu.
   * @param {string} name Menu name 
   * @param {array} submenus Array of hashes of id and name of the submenus. 
   * @param {boolean} isActive If the menu is active initially.
   */
  function createSubMenu(name, submenus, isActive = false) {

    return `<li role="presentation" class="${isActive ? "active" : ""} dropdown">
      <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
        aria-haspopup="true" aria-expanded="false">${name}<span class="caret"></a>
      <ul class="dropdown-menu">
      ${submenus.map(entry => `<li><a href="#${entry.id}" aria-controls="#${entry.id}"
        role="button" data-toggle="tab">${entry.label}</a></li>`).join("")}
      </ul>
    </li>`;
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

  function createStringGroup(label, id, value, helperText) {
    return `<div class="form-group row">
      <label for="${id}" class="col-sm-2 col-form-label">${label}</label>
      <div class="col-sm-10">
        <input type="text" class="form-control" id="${id}" value="${value}" placeholder="${helperText}">
      </div>
    </div>`;
  }

  function createCheckboxGroup(label, id, value, helperText) {
    return `<div class="form-check">
      <input class="form-check-input" type="checkbox" value="" id="${id}" ${value ? "checked" : ""}>
      <label class="form-check-label" for="${id}">${label}</label>
    </div>`;
  }

  function createUrlGroup(label, id, value, helperText) {
    return `<div class="form-group row">
      <label for="${id}" class="col-sm-2 col-form-label">${label}</label>        
      <div class="col-sm-10">
        <input type="url" class="form-control" id="${id}" value="${value}" placeholder="${helperText}">
      </div>
    </div>`
  }

  function createDateGroup(label, id, value, helperText) {
    return `<div class="form-group row">
      <label for="${id}" class="col-sm-2 col-form-label">${label}</label>        
      <div class="col-sm-10">
        <input type="date" class="form-control" value="${value}" id="${id}">
      </div>
    </div>`
  }

  function createForm(formData, value = "") {
    if (formData.type === 'text')
      return createStringGroup(formData.label, formData.id, value, formData.description);
    if (formData.type === 'text-array')
      return createStringTable(formData.label, formData.id);
    if (formData.type === 'boolean')
      return createCheckboxGroup(formData.label, formData.id, value, formData.description);
    if (formData.type === 'url')
      return createUrlGroup(formData.label, formData.id, value, formData.description);
    if (formData.type === 'date')
      return createDateGroup(formData.label, formData.id, value, formData.description)
  }

  /** Create a table with a single (data) column to edit a string array. */
  function createStringTable(label) {
    return `<div>
    <span class="pull-right">
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
      </button>
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
      </button>
    </span>
    <table class="table">
      <tr>
        <th><input type="checkbox"></th>
        <th>${label}</th>
      </tr>
      <tr></tr>
      </tr>
    </table>
   </div>`;
  }

  function createDateTable(label) {
    return `<div>
    <span class="pull-right">
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
      </button>
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
      </button>
    </span>
    <table class="table">
      <tr>
        <th><input type="checkbox"></th>
        <th>${label}</th>
      </tr>
      <tr></tr>
    </table>
   </div>`;
  }

  /**
   * Create a Bootstrap 3 panel.
   * - title: string
   * - body: HTML string
   */
  function createPanel(title, body) {
    return `<div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">${title}</h3>
      </div>
      <div class="panel-body">${body}</div>
    </div>`;
  }

  function createGeneralInformationForm() {

    let form = `<form>${ui['generalInformation'].map(prop => createForm(prop, _metadata.generalInformation[prop.id])).join("")}</form>`;
    return createPanel("General", form);
  }

  function createModelCategory() {
    let body = `<form>${ui['modelCategory'].map(prop => createForm(prop, "")).join("")}</form>`;
    return createPanel("Model category", body);
  }

  /**
   * Create a Bootstrap 3 panel with controls in the heading and a table as body.
   * 
   * @param {string} title Panel title
   * @param {string} dialog ID of the dialog to add more items.
   * @param {object} formData UI form data holding the metadata properties.
   */
  function createTablePanel(title, dialog, formData) {
    return `<div class="panel panel-default">
      <div class="panel-heading clearfix">
        <h4 class="panel-title pull-left" style="padding-top:7.5px;">${title}</h4>
        <div class="input-group">
          <p class="pull-right" /> <!-- gutter -->
          <div class="input-group-btn">
            <button type="button" class="btn btn-default" data-toggle="modal" data-target="#${dialog}">
              <i class="glyphicon glyphicon-plus"></i>
            </button>
            <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
            <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>    
          </div>
        </div>
      </div>
      <table class="table">
        <tr>
          <th><input type="checkbox"></th>
          ${formData.map(prop => `<th>${prop.label}</th>`).join("")}
        </tr>
      </table>
    </div>`;
  }

  /**
   * Create a Bootstrap 3 panel for control a list of strings.
   * 
   * @param {string} title Panel title
   * @param {string} table ID to the table. This ID will be used later to update
   * the table on an event.
   */
  function createStringTable(title, table) {
    return `<div class="panel panel-default">
      <div class="panel-heading clearfix">
        <h4 class="panel-title pull-left" style="padding-top:7.5px;">${title}</h4>
        <div class="input-group">
          <p class="pull-right" /> <!-- gutter -->
          <div class="input-group-btn">
            <button type="button" class="btn btn-default" data-toggle="modal" data-target="#">
              <i class="glyphicon glyphicon-plus"></i>
            </button>
            <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
            <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>    
          </div>
        </div>
      </div>
      <table id="${table}" class="table"></table>
    </div>`;
  }

  /**
   * Create an inline table that adds rows without a dialog by pressing an add
   * button.
   */
  function createInlineTable() {
    return `<div class="panel panel-default">
      <div class="panel-heading clearfix">
          <h4 class="panel-title pull-left" style="padding-top: 7.5px;">Panel header</h4>
          <div class="input-group">
            <p style="pull-right" />
            <div class="input-group-btn">
              <button class="btn btn-default"><i class="glyphicon glyphicon-plus"></i></button>
              <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
            </div>
          </div>
      </div>
      <div class="panel-body">
          Panel content
      </div>
    </div>`;
  }

  /**
   * Creates a Bootstrap 3 modal dialog.
   * @param {string} id ID of the modal dialog. 
   * @param {object} formData Object holding the metadata properties.
   */
  function createDialog(id, formData) {
    return `<div class="modal fade" id="${id}" taxindex="-1" role="dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span area-hidden="true">&times;</span>
          </button>
          <h4 class="modal-title">Add reference</h4>
        </div>
        <div class="modal-body">
          <form>${formData.map(prop => createForm(prop)).join("")}</form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary">Save changes</button>
        </div>
      </div>
    </div>`;
  }
}();