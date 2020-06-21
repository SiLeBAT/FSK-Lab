fskutil = function () {

    var fskutil = { version: "1.0.0" };

    /** Temporary workaround for some metadata glitches. */
    function metadataFix(metadata) {
        // Ignore temporarily publication type
        // TODO: publicationType takes the abbreviation instead of the full string
        // used in the Reference dialog. Since KNIME runs getComponentValue twice,
        // the value cannot be converted here. The 1st call to getComponentValue
        // would get the abbreviation but the 2nd call would corrupt it. The HTML
        // select should instead use the full string as label and the abreviation
        // as value.
        metadata.generalInformation.reference.forEach(ref => delete ref.publicationType);

        /* TODO: Ignore temporarily reference.
        The reference property is of type Reference in the schema. Unfortunately,
        nested dialogs are not supported in Bootstrap, so the type is changed
        in the UI schema to text. Since the text type cannot be deserialized to
        Reference, the values are discarded temporarily here.*/
        metadata.modelMath.parameter.forEach(param => delete param.reference);

        return metadata;
    }

    fskutil.createLabel = function (name, isMandatory, description) {
        let label = document.createElement("label");
        label.classList.add("col-sm-2", "control-label");
        label.title = description;
        label.setAttribute("data-toggle", "tooltip");
        label.textContent = name + (isMandatory ? "*" : "");

        $(label).tooltip();  // Enable Bootstrap tooltip

        return label;
    }

    /**
     * Create a Bootstrap dropdown menu.
     * @param {string} name Menu name 
     * @param {array} submenus Array of hashes of id and name of the submenus.
     */
    fskutil.createSubMenu = function (name, submenus) {
        return `<li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
            aria-haspopup="true" aria-expanded="false">${name}<span class="caret"></a>
        <ul class="dropdown-menu">
        ${submenus.map(entry => `<li><a href="#${entry.id}" aria-controls="#${entry.id}"
            role="button" data-toggle="tab">${entry.label}</a></li>`).join("")}
        </ul>
        </li>`;
    }

    /**
     * Add controlled vocabulary to an input.
     * @param {Element} input Input element
     * @param {Array} vocabulary String array with vocabulary terms.
     */
    fskutil.addControlledVocabulary = function (input, vocabulary) {
        $(input).typeahead({
            source: vocabulary,
            autoSelect: true,
            fitToElement: true,
            showHintOnFocus: true
        });
    }

    fskutil.SimpleTable = class {

        constructor(type, data, vocabulary) {
            this.type = type === "text-array" ? "text" : "date";
            this.vocabulary = vocabulary;

            this.table = document.createElement("table");
            this.table.className = "table";
            this.table.innerHTML = `<thead><thead>`;

            this.body = document.createElement("tbody");
            this.table.appendChild(this.body);

            data.forEach(value => this._createRow(value));
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

        _createRow(value = "") {
            let input = document.createElement("input");
            input.type = this.type;
            input.className = "form-control";
            input.value = value;

            // Add autocomplete to input with vocabulary
            if (this.vocabulary) {
                fskutil.addControlledVocabulary(input, this.vocabulary);
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

        get value() {
            let data = [];
            this.body.childNodes.forEach(tr => {
                let inputCell = tr.lastChild; // 2nd cell (with input)
                let input = inputCell.firstChild; // <input>
                data.push(input.value);
            });

            return data;
        }
    }

    fskutil.AdvancedTable = class {

        constructor(data, formData, dialog, panel) {
            this.formData = formData;
            this.dialog = dialog;
            this.panel = panel;

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
                if (prop.type === "boolean" && value) {
                    cell.innerHTML = '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>';
                } else {
                    cell.title = value; // Set the whole value as tooltip
                    cell.textContent = value.length > 20 ? value.substring(0, 24) + "..." : value;
                }
                newRow.appendChild(cell);
            });

            let editButton = document.createElement("button");
            editButton.classList.add("btn", "btn-primary", "btn-sm");
            editButton.innerHTML = '<i class="glyphicon glyphicon-edit"></i>';
            editButton.title = "Edit";
            editButton.onclick = (e) => {

                // Get current row (button > btn-group > td > tr). It starts at 1
                // (it counts the header)
                let rowIndex = e.currentTarget.parentNode.parentNode.parentNode.rowIndex - 1;

                // Update inputs in dialog
                let originalData = this.panel.data[rowIndex];
                for (let prop in originalData) {
                    this.dialog.inputs[prop].value = originalData[prop];
                }

                this.dialog.editedRow = rowIndex;
                $(this.dialog.modal).modal('show');
            }

            // Remove button
            let removeButton = document.createElement("button");
            removeButton.classList.add("btn", "btn-warning", "btn-sm");
            removeButton.innerHTML = '<i class="glyphicon glyphicon-remove"></i>';
            removeButton.onclick = (e) => {
                // Get current row (button > btn-group > td > tr). It starts at 1
                // (it counts the header)
                let rowIndex = e.currentTarget.parentNode.parentNode.parentNode.rowIndex - 1;
                this.panel.remove(rowIndex);
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
         * Remove row at the given index.
         */
        remove(index) {
            this.body.removeChild(this.body.childNodes[index]);
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
    fskutil.InputForm = class {

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
        constructor(name, mandatory, type, helperText, value, vocabulary = null) {

            this.name = name;
            this.mandatory = mandatory;
            this.type = type;
            this.helperText = helperText;

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
            if(type ==="date"){
                let day = (""+value[2]).length > 1 ? (""+value[2]) : ("0"+value[2]);
                let month = (""+value[1]).length > 1 ? (""+value[1]) : ("0"+value[1]);
                this.input.value = value[0]+"-"+month+"-"+day;
            }else{
                this.input.value = value;
            }
                       
            this.input.title = helperText;

            // Create div for input
            let inputDiv = document.createElement("div");
            inputDiv.classList.add("col-sm-10");
            inputDiv.appendChild(this.input);
            if (mandatory) {
                this.helpBlock = document.createElement("span");
                this.helpBlock.className = "help-block";
                this.helpBlock.style.display = "none";
                this.helpBlock.textContent = `${name} is a required property`;
                inputDiv.appendChild(this.helpBlock);
            }

            // Add autocomplete to input with vocabulary
            if (vocabulary) {
                fskutil.addControlledVocabulary(this.input, vocabulary);
            }

            // Collect everything into group
            this.group.classList.add("form-group", "row");
            this.group.appendChild(fskutil.createLabel(name, mandatory, helperText));
            this.group.appendChild(inputDiv);
        }

        get value() {
            return this.type !== "checkbox" ? this.input.value : this.input.checked;
        }

        set value(newValue) {
            this.input.value = newValue;
        }

        clear() {
            this.input.value = "";

            if (this.helpBlock) {
                this.helpBlock.style.display = "none";
            }

            // Remove validation classes
            this.group.classList.remove("has-success", "has-error");
        }

        /**
         * @returns {boolean} If the input is valid.
         */
        validate() {

            let isValid;
            if (!this.mandatory) {
                isValid = true;
            } else {
                isValid = this.input.value ? true : false;
            }

            if (!isValid) {
                this.helpBlock.style.display = "block";
            }

            // Remove validation classes
            this.group.classList.remove("has-success", "has-error");

            // Add new validation class
            this.group.classList.add(isValid ? "has-success" : "has-error");

            return isValid;
        }
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
    fskutil.ArrayForm = class {

        constructor(name, mandatory, type, value, helperText, vocabulary) {
            this.group = document.createElement("div");
            this.simpleTable = new fskutil.SimpleTable(type, value, vocabulary);
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
            formDiv.title = helperText;

            this.group.classList.add("form-group", "row");
            this.group.appendChild(fskutil.createLabel(name, mandatory, helperText));
            this.group.appendChild(formDiv);
        }

        get value() {
            return this.simpleTable.value;
        }

        set value(newValue) {
            this.simpleTable.trash();
            newValue.forEach(item => this.simpleTable._createRow(item));
        }

        clear() {
            this.simpleTable.trash();
        }

        /**
         * @returns {boolean} If the input is valid.
         */
        validate() {
            // TODO: Implement validate in ArrayForm
            return true;
        }
    }

    /**
     * Bootstrap 3 form with a select.
     */
    fskutil.SelectForm = class {

        /**
         * Create a Bootstrap 3 form-group with a select.
         * 
         * ```
         * <div class="form-group row">
         *   <label>name</label>
         *   <select class="form-control">
         *     <option>1</option>
         *     <option>2</option>
         *   </select>
         * </div>```
         */
        constructor(name, mandatory, helperText, value, vocabulary = null) {

            this.select = document.createElement("select");
            this.group = document.createElement("div");

            this._create(name, mandatory, helperText, value, vocabulary);
        }

        _create(name, mandatory, helperText, value, vocabulary) {

            this.select.className = "form-control";
            // Add options from vocabulary. The option matching value is selected.
            this.select.innerHTML = vocabulary.map(item => `<option>${item}</option>`)
                .join("");
            this.select.value = value;
            this.select.title = helperText;

            // Create div for select
            let selectDiv = document.createElement("div");
            selectDiv.className = "col-sm-10";
            selectDiv.appendChild(this.select);

            // this.group
            this.group.classList.add("form-group", "row");
            this.group.appendChild(fskutil.createLabel(name, mandatory, helperText));
            this.group.appendChild(selectDiv);
        }

        get value() {
            return this.select.value;
        }

        set value(newValue) {
            this.select.value = newValue;
        }

        clear() {
            this.select.value = "";
        }

        /**
         * @returns {boolean} If the input is valid.
         */
        validate() {

            let isValid;
            if (!this.mandatory) {
                isValid = true;
            } else {
                isValid = this.input.value ? true : false;
            }

            // Remove validation classes
            this.group.classList.remove("has-success", "has-error");

            // Add new validation class
            this.group.classList.add(isValid ? "has-success" : "has-error");

            return isValid;
        }
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
     * @returns InputForm or ArrayForm for the supported type. If wrong type
     *  it returns undefined.
     */
    fskutil.createForm = function (prop, value) {
        let vocabulary = prop.vocabulary ? cv[prop.vocabulary] : null;
        let isMandatory = prop.required ? prop.required : false;

        if (prop.type === "text" || prop.type === "number" || prop.type === "url" ||
            prop.type === "date")
            return new fskutil.InputForm(prop.label, isMandatory, prop.type, prop.description,
                value ? value : "", vocabulary);

        if (prop.type === "enum")
            return new fskutil.SelectForm(prop.label, isMandatory, prop.description, value,
                vocabulary);

        if (prop.type === "boolean")
            return new fskutil.InputForm(prop.label, false, "checkbox",
                prop.description, value);

        if (prop.type === "text-array")
            return new fskutil.ArrayForm(prop.label, isMandatory, prop.type,
                value ? value : [], prop.description, vocabulary);

        if (prop.type === "date-array")
            return new fskutil.ArrayForm(prop.label, isMandatory, prop.type,
                value ? value : [], prop.description, vocabulary);
    }

    /**
     * Create a Bootstrap 3 modal dialog.
     */
    fskutil.Dialog = class {

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
        constructor(id, title, formData) {
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
                let inputForm = fskutil.createForm(prop);
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

                // Validate inputs and stop saving if errors are found.
                let hasError = false;
                Object.values(this.inputs).forEach(input => {
                    if (!input.validate()) hasError = true;
                });
                if (hasError) return;

                $(this.modal).modal('hide');

                // Retrieve data and clear inputs
                let data = {};
                for (const inputId in this.inputs) {
                    let currentInput = this.inputs[inputId];
                    data[inputId] = currentInput.value; // Save input value
                    currentInput.clear(); // Clear input
                }

                if (this.editedRow != -1) {
                    this.panel.edit(this.editedRow, data);
                    this.editedRow = -1;
                    Object.values(this.inputs).forEach(input => input.clear()); // Clear inputs
                } else {
                    this.panel.add(data);
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
     * Simple panel for non nested data like General information, study, etc.
     */
    fskutil.FormPanel = class {

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
                let inputForm = fskutil.createForm(prop, data ? data[prop.id] : null);
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

        validate() {
            let isValid = true;
            Object.values(this.inputs).forEach(input => {
                if (!input.validate()) isValid = false;
            });
            return isValid;
        }

        get data() {
            let data = {};
            Object.entries(this.inputs).forEach(([id, input]) => data[id] = input.value);
            return data;
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
    fskutil.TablePanel = class {

        /**
         * Create a TablePanel.
         * 
         * @param {string} title Panel title.
         * @param {object} formData Related data from the UI schema.
         * @param {object} data Initial data of the table.
         */
        constructor(title, formData, data) {

            this.panel = document.createElement("div");

            // Register this panel in dialog (TODO: this should be done in Dialog's constr)
            // this.dialog = dialog;
            this.dialog = new fskutil.Dialog(title + "Dialog", "Add " + title, formData);
            this.dialog.panel = this;

            this.table = new fskutil.AdvancedTable(data, formData, this.dialog, this);

            this.data = data ? data : []; // Initialize null or undefined data

            this._create(title, this.dialog, formData);
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
            addButton.innerHTML = '<i class="glyphicon glyphicon-plus"></i>';
            addButton.title = "Add a " + title;
            addButton.onclick = () => {
                Object.values(dialog.inputs).forEach(input => input.clear());
                $(dialog.modal).modal('show');
            };

            // Trash button
            let trashButton = document.createElement("button");
            trashButton.classList.add("btn", "btn-danger");
            trashButton.innerHTML = '<i class="glyphicon glyphicon-trash"></i>';
            trashButton.onclick = () => this.removeAll();
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

            // content div: responsive div for the table (no overflows)
            let content = document.createElement("div");
            content.className = "table-responsive";
            content.appendChild(this.table.table);

            // panel
            this.panel.classList.add("panel", "panel-default");
            this.panel.appendChild(panelHeading);
            this.panel.appendChild(content);
        }

        add(data) {
            this.data.push(data); // add data
            this.table.add(data); // Update table
        }

        edit(index, newData) {
            this.data[index] = newData; // Update this.data
            this.table.edit(index, newData); // Update table
        }

        remove(index) {
            this.data.splice(index, 1); // Update this.data
            this.table.remove(index); // TODO: update table
        }

        removeAll() {
            this.data = []; // Clear data
            this.table.trash();  // Empty table
        }
    }

    // Handler for generic model schema
    fskutil.GenericModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.parameter.forEach(param => delete param.reference);

            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;
            this.metadata.modelMath.exposure = this.panels.exposure.data;

            this.metadata.modelType = "GenericModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.genericModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                population: new fskutil.TablePanel("Population", schema.populationGroup,
                    this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample,
                    this.metadata.dataBackground.studySample),
                dietaryAssessmentMethod: new fskutil.TablePanel("Dietary assessment method",
                    schema.dietaryAssessmentMethod, this.metadata.dataBackground.dietaryAssessmentMethod),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures,
                    this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation, this.metadata.modelMath.modelEquation),
                exposure: new fskutil.TablePanel("Exposure", schema.exposure, this.metadata.modelMath.exposure)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "population", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
                { "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" },
                { "id": "exposure", "label": "Exposure" }]);
        }
    }

    fskutil.DataModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath.parameter = this.panels.parameter.data;

            this.metadata.modelType = "DataModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        _createPanels() {

            let schema = fskui.dataModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                population: new fskutil.TablePanel("Population", schema.populationGroup, this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample, this.metadata.dataBackground.studySample),
                dietaryAssessmentMethod: new fskutil.TablePanel("Dietary assessment method",
                    schema.dietaryAssessmentMethod, this.metadata.dataBackground.dietaryAssessmentMethod),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "population", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "parameter", "label": "Parameter" }]);
        }
    }

    fskutil.PredictiveModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;
            this.metadata.modelMath.exposure = this.panels.exposure.data;

            this.metadata.modelType = "PredictiveModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.predictiveModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample, this.metadata.dataBackground.studySample),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "hazard", "label": "Hazard" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "parameter", "label": "Parameter" }]);
        }
    }

    fskutil.OtherModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // general information
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;

            this.metadata.modelType = "OtherModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.otherModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                population: new fskutil.TablePanel("Population", schema.populationGroup, this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample, this.metadata.dataBackground.studySample),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures, this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation, this.metadata.modelMath.modelEquation)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "population", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
                { "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" }]);
        }
    }

    // Handler for dose response model schema
    fskutil.DoseResponseModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // general information
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;
            this.metadata.modelMath.exposure = this.panels.exposure.data;

            this.metadata.modelType = "DoseResponseModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {
            let schema = fskui.doseResponseModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                population: new fskutil.TablePanel("Population", schema.populationGroup, this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample, this.metadata.dataBackground.studySample),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures,
                    this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation, this.metadata.modelMath.modelEquation),
                exposure: new fskutil.FormPanel("Exposure", schema.exposure, this.metadata.modelMath.exposure)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "population", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
                { "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" },
                { "id": "exposure", "label": "Exposure" }]);
        }
    }

    fskutil.ToxicologicalModel = class {

        constructor() {
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;
            this.metadata.modelMath.exposure = this.panels.exposure.data;

            this.metadata.modelType = "ToxicologicalModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.toxicologicalModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                population: new fskutil.TablePanel("Population", schema.populationGroup, this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample, this.metadata.dataBackground.studySample),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures,
                    this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation, this.metadata.modelMath.modelEquation),
                exposure: new fskutil.TablePanel("Exposure", schema.exposure, this.metadata.modelMath.exposure)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "population", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
                { "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" },
                { "id": "exposure", "label": "Exposure" }]);
        }
    }

    fskutil.ExposureModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // general information
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;
            this.metadata.modelMath.exposure = this.panels.exposure.data;

            this.metadata.modelType = "ExposureModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.exposureModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                population: new fskutil.TablePanel("Population", schema.populationGroup, this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample, this.metadata.dataBackground.studySample),
                dietaryAssessmentMethod: new fskutil.TablePanel("Dietary assessment method",
                    schema.dietaryAssessmentMethod, this.metadata.dataBackground.dietaryAssessmentMethod),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures, this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation, this.metadata.modelMath.modelEquation),
                exposure: new fskutil.TablePanel("Exposure", schema.exposure, this.metadata.modelMath.exposure)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "population", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
                { "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" },
                { "id": "exposure", "label": "Exposure" }]);
        }
    }

    fskutil.ProcessModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.hazard = this.panels.hazard.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;

            this.metadata.modelType = "ProcessModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.processModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample, this.metadata.dataBackground.studySample),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures, this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation, this.metadata.modelMath.modelEquation)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "hazard", "label": "Hazard" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" }]);
        }
    }

    fskutil.ConsumptionModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        // TODO: update get metaData
        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.populationGroup = this.panels.populationGroup.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;

            this.metadata.modelType = "ConsumptionModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.consumptionModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                populationGroup: new fskutil.TablePanel("Population group", schema.populationGroup,
                    this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample,
                    this.metadata.dataBackground.studySample),
                dietaryAssessmentMethod: new fskutil.TablePanel("Dietary assessment method",
                    schema.dietaryAssessmentMethod, this.metadata.dataBackground.dietaryAssessmentMethod),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory, this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures,
                    this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation, this.metadata.modelMath.modelEquation)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "populationGroup", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" }]);
        }
    }

    fskutil.HealthModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.populationGroup.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;
            this.metadata.modelMath.exposure = this.panels.exposure.data;

            this.metadata.modelType = "HealthModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.healthModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                populationGroup: new fskutil.TablePanel("Population group", schema.populationGroup,
                    this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample,
                    this.metadata.dataBackground.studySample),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory,
                    this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures,
                    this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation,
                    this.metadata.modelMath.modelEquation),
                exposure: new fskutil.TablePanel("Exposure", schema.exposure, this.metadata.modelMath.exposure)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "populationGroup", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
                { "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" },
                { "id": "exposure", "label": "Exposure" }]);
        }
    }

    fskutil.RiskModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;
            this.metadata.modelMath.exposure = this.panels.exposure.data;

            this.metadata.modelType = "RiskModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.genericModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                population: new fskutil.TablePanel("Population", schema.populationGroup,
                    this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample,
                    this.metadata.dataBackground.studySample),
                dietaryAssessmentMethod: new fskutil.TablePanel("Dietary assessment method", this.dialogs.methodDialog,
                    schema.dietaryAssessmentMethod, this.metadata.dataBackground.dietaryAssessmentMethod),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory,
                    this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures,
                    this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation,
                    this.metadata.modelMath.modelEquation),
                exposure: new fskutil.TablePanel("Exposure", schema.exposure, this.metadata.modelMath.exposure)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "population", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
                { "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" },
                { "id": "exposure", "label": "Exposure" }]);
        }
    }

    fskutil.QraModel = class {

        constructor(metadata) {
            this.metadata = metadata;
            this.panels = this._createPanels();
            this.menus = this._createMenus();
        }

        get metaData() {

            // generalInformation
            this.metadata.generalInformation = this.panels.generalInformation.data;
            this.metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
            this.metadata.generalInformation.author = this.panels.author.data;
            this.metadata.generalInformation.creator = this.panels.creator.data;
            this.metadata.generalInformation.reference = this.panels.reference.data;

            // Scope
            this.metadata.scope = this.panels.scopeGeneral.data;
            this.metadata.scope.product = this.panels.product.data;
            this.metadata.scope.hazard = this.panels.hazard.data;
            this.metadata.scope.populationGroup = this.panels.population.data;

            // Data background
            this.metadata.dataBackground.study = this.panels.study.data;
            this.metadata.dataBackground.studySample = this.panels.studySample.data;
            this.metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
            this.metadata.dataBackground.laboratory = this.panels.laboratory.data;
            this.metadata.dataBackground.assay = this.panels.assay.data;

            // Model math
            this.metadata.modelMath = this.panels.modelMath.data;
            this.metadata.modelMath.parameter = this.panels.parameter.data;
            this.metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
            this.metadata.modelMath.modelEquation = this.panels.modelEquation.data;
            this.metadata.modelMath.exposure = this.panels.exposure.data;

            this.metadata.modelType = "QraModel";

            this.metadata = metadataFix(this.metadata);

            return this.metadata;
        }

        // Validate this.panels and return boolean
        validate() {
            let isValid = true;
            if (!this.panels.generalInformation.validate()) isValid = false;
            if (!this.panels.modelCategory.validate()) isValid = false;
            if (!this.panels.scopeGeneral.validate()) isValid = false;
            if (!this.panels.study.validate()) isValid = false;
            return isValid;
        }

        _createPanels() {

            let schema = fskui.qraModel;

            return {
                generalInformation: new fskutil.FormPanel("General", schema.generalInformation, this.metadata.generalInformation),
                modelCategory: new fskutil.FormPanel("Model category", schema.modelCategory, this.metadata.generalInformation.modelCategory),
                author: new fskutil.TablePanel("Author", schema.contact, this.metadata.generalInformation.author),
                creator: new fskutil.TablePanel("Creator", schema.contact, this.metadata.generalInformation.creator),
                reference: new fskutil.TablePanel("Reference", schema.reference, this.metadata.generalInformation.reference),
                scopeGeneral: new fskutil.FormPanel("General", schema.scope, this.metadata.scope),
                product: new fskutil.TablePanel("Product", schema.product, this.metadata.scope.product),
                hazard: new fskutil.TablePanel("Hazard", schema.hazard, this.metadata.scope.hazard),
                population: new fskutil.TablePanel("Population", schema.populationGroup,
                    this.metadata.scope.populationGroup),
                study: new fskutil.FormPanel("Study", schema.study, this.metadata.dataBackground.study),
                studySample: new fskutil.TablePanel("Study sample", schema.studySample,
                    this.metadata.dataBackground.studySample),
                dietaryAssessmentMethod: new fskutil.TablePanel("Dietary assessment method",
                    schema.dietaryAssessmentMethod, this.metadata.dataBackground.dietaryAssessmentMethod),
                laboratory: new fskutil.TablePanel("Laboratory", schema.laboratory,
                    this.metadata.dataBackground.laboratory),
                assay: new fskutil.TablePanel("Assay", schema.assay, this.metadata.dataBackground.assay),
                modelMath: new fskutil.FormPanel("Model math", schema.modelMath, this.metadata.modelMath),
                parameter: new fskutil.TablePanel("Parameter", schema.parameter, this.metadata.modelMath.parameter),
                qualityMeasures: new fskutil.TablePanel("Quality measures", schema.qualityMeasures,
                    this.metadata.modelMath.qualityMeasures),
                modelEquation: new fskutil.TablePanel("Model equation", schema.modelEquation,
                    this.metadata.modelMath.modelEquation),
                exposure: new fskutil.TablePanel("Exposure", schema.exposure, this.metadata.modelMath.exposure)
            };
        }

        _createMenus() {
            return fskutil.createSubMenu("General information", [
                { "id": "generalInformation", "label": "General" },
                { "id": "modelCategory", "label": "Model category" },
                { "id": "author", "label": "Author" },
                { "id": "creator", "label": "Creator" },
                { "id": "reference", "label": "Reference" }]) +
                fskutil.createSubMenu("Scope", [{ "id": "scopeGeneral", "label": "General" },
                { "id": "product", "label": "Product" },
                { "id": "hazard", "label": "Hazard" },
                { "id": "population", "label": "Population group" }]) +
                fskutil.createSubMenu("Data Background", [{ "id": "study", "label": "Study" },
                { "id": "studySample", "label": "Study sample" },
                { "id": "dietaryAssessmentMethod", "label": "Dietary assessment method" },
                { "id": "laboratory", "label": "Laboratory" },
                { "id": "assay", "label": "Assay" }]) +
                fskutil.createSubMenu("Model math", [{ "id": "modelMath", "label": "General" },
                { "id": "parameter", "label": "Parameter" },
                { "id": "qualityMeasures", "label": "Quality measures" },
                { "id": "modelEquation", "label": "Model equation" },
                { "id": "exposure", "label": "Exposure" }]);
        }
    }

    return fskutil;
}