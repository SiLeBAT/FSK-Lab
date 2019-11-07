fskeditorjs = function () {

  const view = { version: "1.0.0" };
  view.name = "Javascript FSK Editor";

  /**
   * Add controlled vocabulary to an input.
   * @param {Element} input Input element
   * @param {Array} vocabulary String array with vocabulary terms.
   */
  function addControlledVocabulary(input, vocabulary) {
    $(input).typeahead({
      source: vocabulary,
      autoSelect: true,
      fitToElement: true
    });
  }

  /**
   * Create an horizontal form for a metadata property. Missing values with
   * *null* or *undefined* are replaced with an empty string.
   * 
   * @param {object} prop Metadata property. It can be of type: text, number,
   *  url, data, boolean, text-array and date-array.
   * @param {string} value Input value. It can be *null* or *undefined* for
   *  missing values.
   * 
   * @returns InputForm or StringArrayForm for the supported type. If wrong type
   *  it returns undefined.
   */
  function createForm(prop, value) {
    value = value ? value : "";
    let vocabulary = prop.vocabulary ? vocabularies[prop.vocabulary] : null;
    let isMandatory = prop.required ? prop.required : false;

    if (prop.type === "text" || prop.type === "number" || prop.type === "url" ||
      prop.type === "date")
      return new InputForm(prop.label, isMandatory, prop.type, prop.description, value, vocabulary);
    
    if (prop.type === "boolean")
      return new InputForm(prop.label, isMandatory, "checkbox", value, prop.description);
    
    if (prop.type === "text-array")
      return new ArrayForm(prop.label, isMandatory, prop.type, prop.description, value, vocabulary);

    if (prop.type === "date-array")
      return new ArrayForm(prop.label, isMandatory, prop.type, prop.description, value, vocabulary);
  }

  /**
   * Create a div to edit string arrays.
   * 
   * ```
   * <div class="panel panel-default">
   *   <div class="panel-heading clearfix">
   *     <h4 class="panel-title pull-left" style="padding-top:7.5px;">Title</h4>
   *     <div class="input-group">
   *       <p class="pull-right" /> <!-- gutter -->
   *       <div class="input-group-btn">
   *         <button type="button" class="btn btn-default" data-toggle="modal" data-target="#">
   *           <i class="glyphicon glyphicon-plus"></i>
   *         </button>
   *         <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
   *         <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>
   *       </div>
   *      </div>
   *    </div>
   *   <table id="${table}" class="table"></table>
   * </div>
   * ```
   */
  class ArrayForm {

    constructor (name, mandatory, type, value, helperText, vocabulary) {
      this.group = document.createElement("div");      
      this.simpleTable = new SimpleTable(type, ["a", "b", "c"], vocabulary);
      this._create(name, mandatory, helperText);
    }

    _create(name, mandatory, helperText) {

      // Create buttons with icons
      let addButton = document.createElement("button");
      addButton.type = "button";
      addButton.classList.add("btn", "btn-default");
      addButton.innerHTML = '<i class="glyphicon glyphicon-plus"></i>';
      addButton.onclick = () => this.simpleTable.add();

      let removeButton = document.createElement("button");
      removeButton.type = "button";
      removeButton.classList.add("btn", "btn-default");
      removeButton.innerHTML = '<i class="glyphicon glyphicon-remove"></i>';
      removeButton.onclick = () => this.simpleTable.remove();

      let trashButton = document.createElement("button");
      trashButton.type = "button";
      trashButton.classList.add("btn", "btn-default");
      trashButton.innerHTML = '<i class="glyphicon glyphicon-trash"></i>';
      trashButton.onclick = () => this.simpleTable.trash();      

      // Create buttonDiv with buttons
      let buttonDiv = document.createElement("div");
      buttonDiv.classList.add("input-group-btn");
      buttonDiv.appendChild(addButton);
      buttonDiv.appendChild(removeButton);
      buttonDiv.appendChild(trashButton);

      // Create input-group
      let inputGroup = document.createElement("div");
      inputGroup.classList.add("input-group");
      inputGroup.innerHTML = '<p class="pull-right" />'; // gutter
      inputGroup.appendChild(buttonDiv);

      // Create panel-heading
      let panelHeading = document.createElement("div");
      panelHeading.classList.add("panel-heading", "clearfix");
      panelHeading.appendChild(inputGroup);

      // Create panel in group
      let panelDiv = document.createElement("div");
      panelDiv.classList.add("panel", "panel-default");
      panelDiv.appendChild(panelHeading);
      panelDiv.appendChild(this.simpleTable.table);

      let formDiv = document.createElement("div");
      formDiv.className = "col-sm-10";
      formDiv.appendChild(panelDiv);

      this.group.classList.add("form-group", "row");
      this.group.innerHTML = `<label class="col-sm-2 col-form-label">
        ${name + (mandatory ? " *" : "")}</label>`;
      this.group.appendChild(formDiv);
    }
  }

  class SimpleTable {

    constructor (type, data, vocabulary) {
      this.type = type === "text-array" ? "text" : "date";
      this.vocabulary = vocabulary;

      this.table = document.createElement("table");
      this.table.className = "table";
      this.table.innerHTML = `<thead><thead>`;

      this.body = document.createElement("tbody");
      this.table.appendChild(this.body);
    }

    /**
     * Create new row to enter data if the last row value is not empty.
     */
    add() {
      // If it has no rows or the last row value is not empty
      if (!this.body.lastChild || this.body.lastChild.lastChild.firstChild.value) {
        this._createRow();
      }
    }

    remove() {
      // Find checked rows and delete them
      Array.from(this.body.children).forEach(row => {
        // Get checkbox (tr > td > input)
        let checkbox = row.firstChild.firstChild;
        if (checkbox.checked) {
          this.body.removeChild(row);
        }
      });
    }

    /**
     * Remove every row in the table
     */
    trash() {
      this.body.innerHTML = "";      
    }

    _createRow() {
      let input = document.createElement("input");
      input.type = this.type;
      input.className = "form-control";

      // Add autocomplete to input with vocabulary
      if (this.vocabulary) {
        addControlledVocabulary(input, this.vocabulary);
      }

      // If enter is pressed when the input if focused, lose focus and add a
      // new row (like clicking the add button). The new input from calling add
      // is focused.
      input.addEventListener("keyup", (event) => {
        if (event.key === "Enter") {
          input.blur();
          this.add();
        }
      });
      
      // Create cell with input
      let inputCell = document.createElement("td");
      inputCell.appendChild(input);

      // Create row with checkbox and input
      let newRow = document.createElement("tr");
      newRow.innerHTML = '<td><input type="checkbox"></td>'
      newRow.appendChild(inputCell);

      // Add row
      this.body.appendChild(newRow);

      input.focus(); // Focus the new input      
    }
  }

  class AdvancedTable {

    constructor (data, formData, dialog) {
      this.data = data;
      this.formData = formData;
      this.dialog = dialog;

      this.table = document.createElement("table");
      this.table.className = "table";

      // Apply striped rows if table has over 10 rows.
      if (data && data.length > 10) {
        this.table.classList.add("table-striped");
      }

      // Create headers (1 extra columns at the end for buttons)
      let head = document.createElement("thead");
      head.innerHTML = `<tr>
        ${this.formData.map(prop => `<th>${prop.label}</th>`).join("")}
        <th></th>
      </tr>`;

      this.body = document.createElement("tbody");
      this.table.appendChild(head);
      this.table.appendChild(this.body);

      if (data) {
        data.forEach(entry => this.add(entry));
      }
    }

    /**
     * Create a new row with new metadata from a dialog.
     * 
     * @param {Object} data JSON object with new metadata.
     */
    add(data) {

      // Add new row (Order is fixed by formData)
      let newRow = document.createElement("tr");

      this.formData.forEach(prop => {
        // Get value for the current property
        let value = data[prop.id] ? data[prop.id] : "";

        let cell = document.createElement("td");
        cell.title = value; // Set the whole value as tooltip
        cell.textContent = value.length > 20 ? value.substring(0, 24) + "..." : value;
        newRow.appendChild(cell);
      });

      let editButton = document.createElement("button");
      editButton.classList.add("btn", "btn-primary", "btn-sm");
      editButton.innerHTML = '<i class="glyphicon glyphicon-edit"></i>';
      editButton.title = "Edit";
      editButton.onclick = (e) => {
        // Get current row (button > btn-group > td > tr)
        let currentRow = e.currentTarget.parentNode.parentNode.parentNode;

        // Iterate every cell but the last one (with buttons)
        for (let i = 0; i < this.formData.length; i++) {
          let prop = this.formData[i];
          let currentCell = currentRow.childNodes[i];
          // Update dialog with the cell value (from title). The tooltip title
          // has the full value. The textContent may be truncated.
          this.dialog.inputs[prop.id].value = currentCell.title;
        }

        this.dialog.editedRow = currentRow.rowIndex - 1;
        $(this.dialog.modal).modal('show');
      }

      // Remove button
      let removeButton = document.createElement("button");
      removeButton.classList.add("btn", "btn-warning", "btn-sm");
      removeButton.innerHTML = '<i class="glyphicon glyphicon-remove"></i>';
      removeButton.onclick = (e) => {
        // Get current row (button > btn-group > td > tr)
        let currentRow = e.currentTarget.parentNode.parentNode.parentNode;
        this.body.removeChild(currentRow);
      };

      removeButton.title = "Remove";

      let btnGroup = document.createElement("div");
      btnGroup.className = "btn-group";
      btnGroup.setAttribute("role", "group");
      btnGroup.appendChild(editButton);
      btnGroup.appendChild(removeButton);

      let buttonCell = document.createElement("td");
      buttonCell.appendChild(btnGroup);
      newRow.appendChild(buttonCell);

      this.body.appendChild(newRow);
    }

    edit(rowNumber, data) {
      let row = this.body.childNodes[rowNumber];
      
      for (let i = 0; i < this.formData.length; i++) {
        let prop = this.formData[i];
        let cell = row.childNodes[i];

        let value = data[prop.id];
        cell.title = value;
        cell.textContent = value.length > 25 ? value.substring(0, 24) : value;
      }
    }

    /**
     * Remove every row in the table.
     */
    trash() {
      this.body.innerHTML = "";
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
     * If type === checkbox
     * ```
     * <div class="form-group row">
     *   <label >name</label>
     *   <div class="col-sm-10">
     *     <input class="form-check-input" type="checkbox" checked="">
   	 *	 </div>
	   * </div>
     * ```
     * 
     * @param {string} name Property name
     * @param {boolean} mandatory `true` if mandatory, `false` if optional.
     * @param {string} type Property type: text, url, checkbox, etc.
     * @param {string} helperText Tooltip
     * @param {string} value Initial value of the property.
     * @param {Array} vocabulary List of possible value for autocompletion.
     */
    constructor (name, mandatory, type, helperText, value, vocabulary=null) {
      
      this.input = document.createElement("input");
      this.group = document.createElement("div");

      this._create(name, mandatory, type, helperText, value, vocabulary);
    }

    /**
     * @param {string} name Property name
     * @param {boolean} mandatory `true` if mandatory, `false` if optional.
     * @param {string} type Property type: text, url, checkbox, etc.
     * @param {string} helperText Tooltip
     * @param {string} value Initial value of the property.
     * @param {Array} vocabulary List of possible value for autocompletion.
     */
    _create(name, mandatory, type, helperText, value, vocabulary) {

      // Create input
      this.input.className = type === "checkbox" ? "form-check-input" : "form-control";
      this.input.type = type;
      this.input.placeholder = helperText;
      this.input.value = value;

      // Create div for input
      let inputDiv = document.createElement("div");
      inputDiv.classList.add("col-sm-10");
      inputDiv.appendChild(this.input);

      // Add autocomplete to input with vocabulary
      if (vocabulary) {
        addControlledVocabulary(this.input, vocabulary);
      }

      // Collect everything into group
      this.group.classList.add("form-group", "row");
      this.group.innerHTML = `<label class="col-sm-2 col-form-label">
        ${name + (mandatory ? " *" : "")}</label>`;
      this.group.appendChild(inputDiv);
    }

    get value ()  {
      return this.input.value;
    }

    set value(newValue) {
       this.input.value = newValue;
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
     *   <div class="modal-dialog" role="document">
     *     <div class="modal-content">
     *       <div class="modal-header">
     *         <button>
     *           <span>
     *         </button>
     *         <h4 class="modal-title">title</h4>
     *       </div>
     *       <div class="modal-body">
     *         <form>...</form>
     *       </div>
     *       <div class="modal-footer">
     *         <button type="button">Close</button>
     *         <button type="button">Save changes</button>
     *       </div>
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

      // Index of the row currently edited. It is -1 if no row is being edited.
      // This is the case of when a new row is added.
      this.editedRow = -1;

      this.modal = document.createElement("div");      
      this.create(id, title, formData);
    }

    create(id, title, formData) {

      // modal body
      let form = document.createElement("form");
      formData.forEach(prop => {
        let inputForm = createForm(prop);
        if (inputForm) {
          form.appendChild(inputForm.group);
          this.inputs[prop.id] = inputForm;
        }
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
        $(this.modal).modal('hide');

        // Retrieve data and clear inputs
        let data = {};
        for (const inputId in this.inputs) {
          let currentInput = this.inputs[inputId];
          data[inputId] = currentInput.value; // Save input value
          currentInput.value = ""; // Clear input
        }

        if (this.editedRow != -1) {
          this.table.edit(this.editedRow, data);
          this.editedRow = -1;
        } else {
          this.table.add(data);
        }
      }

      let footer = document.createElement("div");
      footer.classList.add("modal-footer");
      footer.appendChild(closeButton);
      footer.appendChild(saveButton);

      let content = document.createElement("div");
      content.classList.add("modal-content");
      content.innerHTML = `<div class="modal-header">
      <button class="close" data-dismiss="modal" aria-label="Close">
        <span aria-hidden="true">&times;</span>
      </button>
      <h4 class="modal-title">${title}</h4>
      </div>`;
      content.appendChild(modalBody);
      content.appendChild(footer);

      let modalDialog = document.createElement("div");
      modalDialog.classList.add("modal-dialog", "modal-dialog-centered");
      modalDialog.setAttribute("role", "document");
      modalDialog.appendChild(content);

      this.modal.classList.add("modal", "fade");
      this.modal.id = id;
      this.modal.tabIndex = -1;
      this.modal.setAttribute("role", "dialog");
      this.modal.appendChild(modalDialog);
    }
  }

  /**
   * Create a Bootstrap 3 panel with controls in the heading and a table as body.
   * 
   * ```
   * <div class="panel panel-default">
   *   <div class="panel-heading clearfix">
   *     <h4 class="panel-title pull-left" style="padding-top:7.5px;">${title}</h4>
   *     <div class="input-group">
   *       <p class="pull-right" /> <!-- gutter -->
   *       <div class="input-group-btn">
   *         <button type="button" class="btn btn-default" data-toggle="modal" data-target="#${dialog}">
   *           <i class="glyphicon glyphicon-plus"></i>
   *         </button>
   *         <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
   *         <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>
   *       </div>
   *     </div>
   *   </div>
   *   <table class="table">
   *     <tr>
   *       <th><input type="checkbox"></th>
   *     </tr>
   *   </table>
   * </div>`
   * ```
   */
  class TablePanel {

    /**
     * Create a TablePanel.
     * 
     * @param {string} title Panel title.  
     * @param {Dialog} dialog Reference to Dialog object. This Dialog is later
     *   used for adding new entries and editing existing ones. 
     * @param {object} formData Related data from the UI schema.
     * @param {object} data Initial data of the table.
     */
    constructor (title, dialog, formData, data) {

      this.panel = document.createElement("div");
      this.table = new AdvancedTable(data, formData, dialog);
      this._create(title, dialog, formData);
    }

    /**
     * Create UI of the TablePanel.
     * 
     * @param {string} title Panel title.  
     * @param {Dialog} dialog Reference to Dialog object. This Dialog is later
     *   used for adding new entries and editing existing ones. 
     * @param {object} formData Related data from the UI schema.
     */
    _create(title, dialog, formData) {

      // Add button
      let addButton = document.createElement("button");
      addButton.classList.add("btn", "btn-primary");
      addButton.setAttribute("data-toggle", "modal");
      addButton.setAttribute("data-target", "#" + dialog.modal.id);
      addButton.innerHTML = '<i class="glyphicon glyphicon-plus"></i>';
      addButton.title = "Add a " + title;
      // Set dialog to add to this panel's table
      dialog.table = this.table;
      
      // Trash button
      let trashButton = document.createElement("button");
      trashButton.classList.add("btn", "btn-danger");
      trashButton.innerHTML = '<i class="glyphicon glyphicon-trash"></i>';
      trashButton.onclick = () => this.table.trash();
      trashButton.title = `Remove all ${title}(s)`;

      // input-group-btn
      let inputGroupBtn = document.createElement("div");
      inputGroupBtn.className = "input-group-btn";
      inputGroupBtn.appendChild(addButton);
      inputGroupBtn.appendChild(trashButton);

      // input-group
      let inputGroup = document.createElement("div");
      inputGroup.className = "input-group";
      inputGroup.innerHTML = '<p class="pull-right" />'; // gutter
      inputGroup.appendChild(inputGroupBtn);

      // panel heading
      let panelHeading = document.createElement("div");
      panelHeading.classList.add("panel-heading", "clearfix");
      panelHeading.innerHTML = `<h4 class="panel-title pull-left" style="padding-top:7.5px;">${title}</h4>`;
      panelHeading.appendChild(inputGroup);

      // panel
      this.panel.classList.add("panel", "panel-default");
      this.panel.appendChild(panelHeading);
      this.panel.appendChild(this.table.table);
    }
  }

  /**
   * Simple panel for non nested data like General information, study, etc.
   */
  class FormPanel {

    constructor(title, formData, data) {
      this.panel = document.createElement("div");
      this.inputs = {};

      this._create(title, formData, data);
    }

    /**
     * ```
     * <div class="panel panel-default">
     *   <div class="panel-heading">
     *     <h3 class="panel-title">Some title</h3>
     *   </div>
     *   <div class="panel-body">
     *     <form></form>
     *   </div>
     * </div>
     * ```
     * @param {*} title 
     * @param {*} formData 
     */
    _create(title, formData, data) {
      
      this.panel.classList.add("panel", "panel-default");
      this.panel.innerHTML = `<div class="panel-heading">
      <h3 class="panel-title">${title}</h3>
      </div>`;

      let form = document.createElement("form");
      formData.forEach(prop => {
        let inputForm = createForm(prop, data[prop.id]);
        if (inputForm) {
          form.appendChild(inputForm.group);
          this.inputs[prop.id] = inputForm;
        }
      });

      let body = document.createElement("div");
      body.className = "panel-body";
      body.appendChild(form);

      this.panel.appendChild(body);
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

  var _panels = {}; // Metadata panels created in createUI.

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

  view.getComponentValue = () => {

    Object.entries(tablePanels.generalInformation.inputs).forEach(([id, input]) => {
      _metadata.generalInformation[id] = input.value;
    });

    _metadata.modelType = "GenericModel";
    let metaDataString = JSON.stringify(_metadata);

    let viewValue = {
      modelMetaData: metaDataString,
      firstModelScript: _modelCodeMirror.getValue(),
      firstModelViz: _visualizationCodeMirror.getValue(),
      readme: _readmeCodeMirror.getValue(),
      resourceFiles: _val.resourceFiles, // TODO: get actual resource files from editor
      serverName: _val.serverName // TODO: get actual serverName from editor?
    }

    return viewValue;
  }

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
      contactDialog: new Dialog("contactDialog", "Add contact", ui.contact),
      referenceDialog: new Dialog("referenceDialog", "Add reference", ui.reference),
      productDialog: new Dialog("productDialog", "Add product", ui.product),
      hazardDialog: new Dialog("hazardDialog", "Add hazard", ui.hazard),
      populationDialog: new Dialog("populationDialog", "Add population", ui.populationGroup),
      studySampleDialog: new Dialog("studySampleDialog", "Add study sample", ui.studySample),
      methodDialog: new Dialog("methodDialog", "Add method", ui.dietaryAssessmentMethod),
      laboratoryDialog: new Dialog("laboratoryDialog", "Add laboratory", ui.laboratory),
      assayDialog: new Dialog("assayDialog", "Add assay", ui.assay),
      parameterDialog: new Dialog("parameterDialog", "Add parameter", ui.parameter),
      measuresDialog: new Dialog("measuresDialog", "Add quality measures", ui.qualityMeasures),
      equationDialog: new Dialog("equationDialog", "Add model equation", ui.modelEquation),
      exposureDialog: new Dialog("exposureDialog", "Add exposure", ui.exposure)
    }

    let panelsById = [
      {id: "modelScript", panel: `<textarea id="modelScriptArea">${_val.firstModelScript}</textarea>`},
      {id: "visualizationScript", panel: `<textarea id="visualizationScriptArea">${_val.firstModelViz}</textarea>`},
      {id: "readme", panel: `<textarea id="readmeArea" name="readmeArea">${_val.readme}</textarea>`}      
    ];

    let bodyContent = `
<div class="container-fluid">
  <nav class="navbar navbar-default">
    <div class="navbar-collapse collapse">
      <ul class="nav navbar-nav" id="viewTab">
      ${createSubMenu("General information", [{ "id": "generalInformation", "label": "General" },
        { "id": "modelCategory", "label": "Model category" },
        { "id": "author", "label": "Author" },
        { "id": "creator", "label": "Creator" },
        { "id": "reference", "label": "Reference" }])}
      ${createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
        { "id": "product", "label": "Product" },
        { "id": "hazard", "label": "Hazard" },
        { "id": "population", "label": "Population group" }])}
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
          <a id="modelScript-tab" href="#modelScript"
            aria-controls="modelScript" role="tab" data-toggle="tab">Model script</a>
        </li>
        <li role="presentation">
          <a id="visualizationScript-tab" href="#visualizationScript"
            aria-controls="visualizationScript" role="tab" data-toggle="tab">Visualization script</a>
        </li>
        <li role="presentation">
          <a href="#readme" aria-controls="readme" role="tab" data-toggle="tab">README</a>
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
    Object.values(dialogs).forEach(dialog => container.appendChild(dialog.modal));

    // Add (table) panels
    tablePanels = {
      generalInformation: new FormPanel("General", ui.generalInformation, _metadata.generalInformation),
      modelCategory: new FormPanel("Model category", ui.modelCategory, _metadata.generalInformation.modelCategory),
      author: new TablePanel("Author", dialogs.contactDialog, ui.contact, _metadata.generalInformation.author),
      creator: new TablePanel("Creator", dialogs.contactDialog, ui.contact, _metadata.generalInformation.creator),
      reference: new TablePanel("Reference", dialogs.referenceDialog, ui.reference, _metadata.generalInformation.reference),
      scopeGeneral: new FormPanel("General", ui.scope, _metadata.scope),
      product: new TablePanel("Product", dialogs.productDialog, ui.product, _metadata.scope.product),
      hazard: new TablePanel("Hazard", dialogs.hazardDialog, ui.hazard, _metadata.scope.hazard),
      population: new TablePanel("Population", dialogs.populationDialog, ui.populationGroup, _metadata.scope.populationGroup),
      study: new FormPanel("Study", ui.study, _metadata.dataBackground.study),
      studySample: new TablePanel("Study sample", dialogs.studySampleDialog, ui.studySample, _metadata.dataBackground.studySample),
      dietaryAssessmentMethod: new TablePanel("Dietary assessment method", dialogs.methodDialog, ui.dietaryAssessmentMethod, _metadata.dataBackground.dietaryAssessmentMethod),
      laboratory: new TablePanel("Laboratory", dialogs.laboratoryDialog, ui.laboratory, _metadata.dataBackground.laboratory),
      assay: new TablePanel("Assay", dialogs.assayDialog, ui.assay, _metadata.dataBackground.assay),
      parameter: new TablePanel("Parameter", dialogs.parameterDialog, ui.parameter, _metadata.modelMath.parameter),
      qualityMeasures: new TablePanel("Quality measures", dialogs.measuresDialog, ui.qualityMeasures, _metadata.modelMath.qualityMeasures),
      modelEquation: new TablePanel("Model equation", dialogs.equationDialog, ui.modelEquation, _metadata.modelMath.modelEquation),
      exposure: new TablePanel("Exposure", dialogs.exposureDialog, ui.exposure, _metadata.modelMath.exposure)
    };

    const viewContent = document.getElementById("viewContent");    

    Object.entries(tablePanels).forEach(([key, value]) => {
      let tabPanel = document.createElement("div");
      tabPanel.setAttribute("role", "tabpanel");
      tabPanel.className = "tab-pane";
      tabPanel.id = key;
      tabPanel.appendChild(value.panel);

      viewContent.appendChild(tabPanel);
    });

    // Set the first tab (general information) as active
    document.getElementById("generalInformation").classList.add("active");

    // Create code mirrors for text areas with scripts and readme
    _modelCodeMirror = createCodeMirror("modelScriptArea", "text/x-rsrc");
    _visualizationCodeMirror = createCodeMirror("visualizationScriptArea", "text/x-rsrc");
    _readmeCodeMirror = createCodeMirror("readmeArea", "htmlmixed");

    $('#modelScript-tab').on('shown.bs.tab', () => {
      _modelCodeMirror.refresh();
    });

    $('#visualizationScript-tab').on('shown.bs.tab', () => {
      _visualizationCodeMirror.refresh();
    });

    $('#readme-tab').on('shown.bs.tab', () => {
      _readmeCodeMirror.refresh();
    });
  }

  /**
   * Create a Bootstrap dropdown menu.
   * @param {string} name Menu name 
   * @param {array} submenus Array of hashes of id and name of the submenus.
   */
  function createSubMenu(name, submenus) {

    return `<li class="dropdown">
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
}();