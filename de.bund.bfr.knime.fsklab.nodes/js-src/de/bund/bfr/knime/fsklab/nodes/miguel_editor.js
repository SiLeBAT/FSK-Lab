fskeditorjs = function () {

  const view = { version: "1.0.0" };
  view.name = "Javascript FSK Editor";

  /**
   * Create a div to edit string arrays.
   * 
   * ```
   * <div>
   *   <span class="pull-right">
   *     <button type="button"></button>
   *     <button type="button"></button>
   *   </span>
   *   <table class="table">
   *     <thead>
   *       <tr>
   *         <th><input type="checkbox"></th>
   *         <th>Label</th>
   *       </tr>
   *     </thead>
   *     <tbody></tbody>
   *   </table>
   * </div>
   * ```
   */
  class StringArrayForm {

    constructor (name, value, helperText) {
      this.group = document.createElement("div");      
      this._create(name, value, helperText);
    }

    _create(name, value, helperText) {
      
      let addSpan = document.createElement("span");
      addSpan.classList.add("glyphicon", "glyphicon-plus");
      addSpan.setAttribute("aria-hidden", "true");

      let addButton = document.createElement("button");
      addButton.type = "button";
      addButton.classList.add("btn", "btn-default");
      addButton.appendChild(addSpan);

      let removeSpan = document.createElement("span");
      removeSpan.classList.add("glyphicon", "glyphicon-remove");
      removeSpan.setAttribute("aria-hidden", "true");

      let removeButton = document.createElement("button");
      removeButton.type = "button";
      removeButton.classList.add("btn", "btn-default");
      removeButton.appendChild(removeSpan);

      let controlSpan = document.createElement("span");
      controlSpan.classList.add("pull-right");
      controlSpan.appendChild(addButton);
      controlSpan.appendChild(removeButton);

      let row = document.createElement("tr");

      let checkbox = document.createElement("input");
      checkbox.type = "checkbox";

      let checkboxCell = document.createElement("th");
      checkboxCell.appendChild(checkbox);

      let header = document.createElement("thead");
      header.appendChild(row);

      let body = document.createElement("tbody");

      let table = document.createElement("table");
      table.appendChild(header);
      table.appendChild(body);

      this.group.appendChild(controlSpan);
      this.group.appendChild(table);
    }
  }

  /**
   * Bootstrap 3 form-group for an input.
   */
  class InputForm {
    
    /**
     * Create a Bootstrap 3 form-group.
     * 
     * ```
     * <div class="form-group row">
     *   <label>name</label>
     *   <div class="col-sm-10">
     *     <input type="text">
     *   </div>
     * </div>`;
     * ```
     * 
     * @param {string} name Property name
     * @param {string} type Property type: text, url, checkbox, etc.
     * @param {string} helperText Tooltip
     */
    constructor (name, type, helperText) {
      
      this.input = document.createElement("input");
      this.group = document.createElement("div");

      this._create(name, type, helperText);
    }

    _create(name, type, helperText) {
      // Create label
      let label = document.createElement("label");
      label.classList.add("col-sm-2", "col-form-label");
      label.innerText = name;

      // Create div for input
      let inputDiv = document.createElement("div");
      inputDiv.classList.add("col-sm-10");
      inputDiv.appendChild(this.input);

      // Create input
      this.input.classList.add("form-control");
      this.input.type = type;
      this.input.placeholder = helperText;

      // Collect everything into group
      this.group.classList.add("form-group", "row");
      this.group.appendChild(label);
      this.group.appendChild(inputDiv);
    }

    get value ()  {
      return this.input.value;
    }
  }

  /**
   * Create a Bootstrap 3 modal dialog.
   */
  class Dialog {

    /**
     * Create a Bootstrap 3 modal dialog.
     * 
     * ```
     * <div class="modal-fade">
     *   <div class="modal-content">
     *     <div class="modal-header">
     *       <button>
     *         <span>
     *       </button>
     *       <h4 class="modal-title">title</h4>
     *     </div>
     *     <div class="modal-body">
     *       <form>...</form>
     *     </div>
     *     <div class="modal-footer">
     *       <button type="button">Close</button>
     *       <button type="button">Save changes</button>
     *     </div>
     *   </div>
     * </div>
     * ```
     * 
     * @param {id} id Dialog id
     * @param {title} title Dialog title
     * @param {formData} formData Object with form data
     */
    constructor (id, title, formData) {
      this.inputs = {};  // Hash of inputs by id
      this.create(id, title, formData);
    }

    create(id, title, formData) {
      // modal header
      let closeControl = document.createElement("button");
      closeControl.type = "button";
      closeControl.classList.add("close");
      closeControl.setAttribute("data-dismiss", "modal");
      closeControl.setAttribute("aria-label", "Close");

      let span = document.createElement("span");
      span.setAttribute("aria-hidden", "true");
      span.innerHTML = "&times;";
      closeControl.appendChild(span);

      let modalTitle = document.createElement("h4");
      modalTitle.classList.add("modal-title");
      modalTitle.textContent = title;

      let modalHeader = document.createElement("div");
      modalHeader.classList.add("modal-header");
      modalHeader.appendChild(closeControl);
      modalHeader.appendChild(modalTitle);

      // modal body
      let form = document.createElement("form");
      formData.forEach(prop => {
        let inputForm;
        if (prop.type === "text" || prop.type === "number" ||
          prop.type === "url" || prop.type === "date") {
          inputForm = new InputForm(prop.label, prop.type, prop.description);            
        } else if (prop.type === "boolean") {
          inputForm = new InputForm(prop.label, "checkbox", prop.description);
        } else if (prop.type === "text-array") {
          // TODO: Fix StringArrayForm
          // inputForm = new StringArrayForm(prop.label, "", prop.description);
          return;
        } else {
          return;
        }

        form.appendChild(inputForm.group);
        this.inputs[prop.id] = inputForm;
      });

      let modalBody = document.createElement("div");
      modalBody.classList.add("modal-body");
      modalBody.appendChild(form);

      // modal footer
      let closeButton = document.createElement("button");
      closeButton.type = "button";
      closeButton.classList.add("btn", "btn-default");
      closeButton.setAttribute("data-dismiss", "modal");
      closeButton.textContent = "Close";

      let saveButton = document.createElement("button");
      saveButton.type = "button";
      saveButton.classList.add("btn", "btn-primary");
      saveButton.textContent = "Save changes";
      saveButton.onclick = () => {
        console.log("saveButton says hi");
        $("#" + id).modal('hide');
        // Create new row
        let newRow = document.createElement("tr");

        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        let checkboxCell = document.createElement("th");
        checkboxCell.appendChild(checkbox);
        newRow.appendChild(checkboxCell);

        this.formData.forEach(prop => {
          let cell = document.createElement("td");
          cell.textContent = prop.label;
          newRow.appendChild(cell);
        })

        // Save data to panel (add new row)
        document.querySelector("#author tbody").appendChild(newRow);
      }

      let footer = document.createElement("div");
      footer.classList.add("modal-footer");
      footer.appendChild(closeButton);
      footer.appendChild(saveButton);

      let content = document.createElement("div");
      content.classList.add("modal-content");
      content.appendChild(modalHeader);
      content.appendChild(modalBody);
      content.appendChild(footer);

      this.modal = document.createElement("div");
      this.modal.classList.add("modal", "fade");
      this.modal.id = id;
      this.modal.tabIndex = -1;
      this.modal.setAttribute("role", "dialog");
      this.modal.appendChild(content);
    }
    
  }

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

    let dialogs = {
      contactDialog: new Dialog("contactDialog", "Add contact", ui['contact']),
      referenceDialog: new Dialog("referenceDialog", "Add reference", ui['reference']),
      productDialog: new Dialog("productDialog", "Add product", ui['product']),
      hazardDialog: new Dialog("hazardDialog", "Add hazard", ui['hazard']),
      populationDialog: new Dialog("populationDialog", "Add population", ui['populationGroup']),
      studySampleDialog: new Dialog("studySampleDialog", "Add study sample", ui['studySample']),
      methodDialog: new Dialog("methodDialog", "Add method", ui['dietaryAssessmentMethod']),
      laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", ui['laboratory']),
      assayDialog: new Dialog("assayDialog", "Add assay", ui['assay']),
      parameterDialog: new Dialog("parameterDialog", "Add parameter", ui['parameter']),
      measuresDialog: new Dialog("measuresDialog", "Add quality measures", ui['qualityMeasures'])
    }

    let panelsById = [
      {id: "generalInformation", panel: createGeneralInformationForm(), "active": true},
      {id: "modelCategory", panel: createModelCategory()},
      {id: "modificationDate", panel: createPanel("Modification date", createDateTable("Modification date"))},
      {id: "author", panel: createTablePanel("Author", "contactDialog", ui.contact)},
      {id: "creator", panel: createTablePanel("Creator", "contactDialog", ui.contact)},
      {id: "reference", panel: createTablePanel("Reference", "referenceDialog", ui.reference)},
      {id: "scopeGeneral", panel: "Scope general"},
      {id: "product", panel: createTablePanel("Product", "productDialog", ui.product)},
      {id: "hazard", panel: createTablePanel("Hazard", "hazardDialog", ui.hazard)},
      {id: "population", panel: createTablePanel("Population group", "populationDialog", ui.populationGroup)},
      {id: "spatialInformation", panel: createStringTable("Spatial information", "spatialInformation")},
      {id: "study", panel: createStudy()},
      {id: "studySample", panel: createTablePanel("Study sample", "studySampleDialog", ui.studySample)},
      {id: "dietaryAssessmentMethod", panel: createTablePanel("Dietary assessment method", "methodDialog", ui.dietaryAssessmentMethod)},
      {id: "laboratory", panel: createTablePanel("Laboratory", "laboratoryDialog", ui.laboratory)},
      {id: "assay", panel: createTablePanel("Assay", "assayDialog", ui.assay)},
      {id: "parameter", panel: createTablePanel("Parameter", "parameterDialog", ui.parameter)},
      {id: "qualityMeasures", panel: createTablePanel("Quality measures", "measuresDialog", ui.qualityMeasures)},
      {id: "modelScript", panel: `<textarea id="modelScriptArea">${_val.firstModelScript}</textarea>`},
      {id: "visualizationScript", panel: `<textarea id="visualizationScriptArea">${_val.firstModelViz}</textarea>`},
      {id: "readme", panel: `<textarea id="readmeArea" name="readmeArea">${_val.readme}</textarea>`}      
    ];

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
    ${createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
      { "id": "studySample", "label": "Study sample" },
      { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
      { "id": "laboratory", "label": "Laboratory" },
      { "id": "assay", "label": "Assay" }])}
    ${createSubMenu("Model math", [{ "id": "mathGeneral", "label": "General" },
      { "id": "parameter", "label": "Parameter" },
      { "id": "qualityMeasures", "label": "Quality measures" },
      { "id": "modelEquation", "label": "Model equation" },
      { "id": "exposure", "label": "Exposure" }])}
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
    ${panelsById.map(entry =>
      `<div role="tabpanel" class="tab-pane ${"active" in entry ? 'active' : ''}"
        id="${entry.id}">${entry.panel}</div>`).join("")}
  </div>
</div>`;

    document.createElement('body');
    $('body').html(bodyContent);

    // Add dialogs
    const container = document.getElementsByClassName("container-fluid")[0];
    Object.values(dialogs).forEach(dialog => container.appendChild(dialog.modal));

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

  function createNumberGroup(label, id, value, helperText) {
    return `<div class="form-group row">
      <label for="${id}" class="col-sm-2 col-form-label">${label}</label>
      <div class="col-sm-10">
        <input type="number" class="form-control" id="${id}" value="${value}" placeholder="${helperText}">
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
    if (formData.type === 'number')
      return createNumberGroup(formData.label, formData.id, value, formData.description);
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

  function createStudy() {
    let body = `<form>${ui['study'].map(prop => createForm(prop, "")).join("")}</form>`;
    return createPanel("Study", body);
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
   * Creates a Bootstrap 3 modal dialog.
   * @param {string} id ID of the modal dialog
   * @param {string} title Dialog title
   * @param {object} formData Object holding the metadata properties
   */
  function createDialog(id, title, formData) {
    return `<div class="modal fade" id="${id}" taxindex="-1" role="dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span area-hidden="true">&times;</span>
          </button>
          <h4 class="modal-title">${title}</h4>
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