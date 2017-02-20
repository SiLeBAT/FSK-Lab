metadata_editor = function () {

    var softwareDic = {'R': 'R', 'Matlab': 'Matlab'};
    var modelTypeDic = {
        'EXPERIMENTAL_DATA': 'Experimental data',
        'PRIMARY_MODEL_WDATA': 'Primary model with data',
        'PRIMARY_MODEL_WODATA': 'Primary model without data',
        'TWO_STEP_SECONDARY_MODEL': 'Two step secondary model',
        'ONE_STEP_SECONDARY_MODEL': 'One step secondary model',
        'MANUAL_SECONDARY_MODEL': 'Manual secondary model',
        'TWO_STEP_TERTIARY_MODEL': 'Two step tertiary model',
        'ONE_STEP_TERTIARY_MODEL': 'One step tertiary model',
        'MANUAL_TERTIARY_MODEL': 'Manual tertiary model'
    };
    var modelClassDic = {
        'UNKNOWN': 'unknown',
        'GROWTH': 'growth',
        'INACTIVATION': 'inactivation',
        'SURVIVAL': 'survival',
        'GROWTH_INACTIVATION': 'growth/inactivation',
        'INACTIVATION_SURVIVAL': 'inactivation/survival',
        'GROWTH_SURVIVAL': 'growth/survival',
        'GROWTH_INACTIVATION_SURVIVAL': 'growth/inactivation/survival',
        'T': 'T',
        'PH': 'pH',
        'AW': 'aw',
        'T_PH': 'T/pH',
        'T_AW': 'T/aw',
        'PH_AW': 'pH/aw',
        'T_PH_AW': 'T/pH/aw'
    };

    /**
     * Create a form-group envolving an input.
     * - id: Input id
     * - type: Input type {text, url, checkbox}
     * - label: Text label
     * - value: String value
     */
    function InputForm(id, type, label, value) {
        this.id = id;
        this.type = type;
        this.label = label;
        this.value = value;

        this.createHtml = function() {
            var formStr = '<div class="form-group">';
            formStr += '<label for="' + this.id + '" class="col-sm-3 control-label">' + this.label + '</label>';
            formStr += '<div class="col-sm-9">';
            if (this.type === 'text') {
                formStr += '<input type="text" class="form-control no-border" id="' + this.id + '" value="">';
            } else if (this.type === 'url') {
                formStr += '<input type="url" class="form-control no-border" id="' + this.id + '" value="">';
            } else if (this.type === 'checkbox') {
                formStr += '<input id="' + id + '" type="checkbox">';
            }
            formStr += '</div>';
            formStr += '</div>';

            return formStr;
        };

        this.loadData = function() {
            if (this.type === 'text' || this.type === 'url') {
                $('#' + id).val(this.value === null ? "" : this.value);
            } else if (this.type === 'checkbox') {
                $('#' + id).prop('checked', _value.metadata.hasData);
            }
        };

        this.saveData = function() {
            var outer = this;
            if (this.type === 'text' || this.type === 'url') {
                $('#' + this.id).on('input', function() { outer.value = $(this).val(); });
            } else if (this.type === 'checkbox') {
                $('#' + this.id).change(function() { outer.value = $(this).is(':checked'); });
            }
        };

        return this;
    }

    /**
     * Create a form-group envolving a textarea.
     * - id: Textarea id
     * - label: Text label
     * - value: String value
     */
    function TextAreaForm(id, label, value) {
        this.id = id;
        this.label = label;
        this.value = value;

        this.createHtml = function() {
            return '<div class="form-group">' +
                '  <label for="' + this.id + '" class="col-sm-3 control-label">' + this.label + '</label>' +
                '  <div class="col-sm-9">' +
                '    <textarea id="' + this.id + '" class="form-control no-border" rows="3"></textArea>' +
                '  </div>' +
                '</div>';
        };

        this.loadData = function() {
            $('#' + this.id).val(this.value === null ? "" : this.value);
        };

        this.saveData = function() {
            var outer = this;
            $('#' + this.id).on('input', function() { outer.value = $(this).val(); });
        };

        return this;
    }

    /**
     * Create a form-group envolving a select.
     * - id: Select id
     * - label: Text label
     * - entries: Dictionary where the keys are the labels and the values are the option values
     * - value: String value with the selected option
     */
    function SelectForm(id, label, entries, value) {
        this.id = id;
        this.label = label;
        this.entries = entries;
        this.value = value;

        this.createHtml = function() {
            var formStr = '<div class="form-group">';
            formStr += '<label for="' + this.id + '" class="col-sm-3 control-label">' + this.label + '</label>';
            formStr += '<div class="col-sm-9">';
            formStr += '<select class="form-control no-border" id="' + this.id + '">';
            for (var key in this.entries) {
                formStr += '<option value="' + key + '">' + entries[key] + '</option>';
            }
            formStr += '</select></div></div>';

            return formStr;
        }

        this.loadData = function() {
            if (this.value) {
                var option = $('#' + this.id + ' option[value="' + this.value + '"]');
                option.prop('selected', true);
            }
        }

        this.saveData = function() {
            var outer = this;
            $('#' + this.id).change(function() { outer.value = $(this).val(); });
        }

        return this;
    }

    var editor = {
	   version: "0.0.1"
    };
    editor.name = "FSK Metadata Editor";

    var _value;  // Raw FskMetadataEditorViewValue

    var _modelNameInput = new InputForm("modelNameInput", "text", "Model name", "");
    var _modelIdInput = new InputForm("modelIdInput", "text", "Model id", "");
    var _modelLinkInput = new InputForm("modelLinkInput", "url", "Model link", "");
    var _organismInput = new InputForm("organismInput", "text", "Organism", "");
    var _organismDetailsInput = new InputForm("organismDetailsInput", "text", "Organism details", "");
    var _matrixInput = new InputForm("matrixInput", "text", "Matrix", "");
    var _matrixDetailsInput = new InputForm("matrixDetailsInput", "text", "Matrix details", "");
    var _creatorInput = new InputForm("creatorInput", "text", "Creator", "");
    var _familyNameInput = new InputForm("familyNameInput", "text", "Family name", "");
    var _contactInput = new InputForm("contactInput", "text", "Contact", "");
    var _softwareInput = new SelectForm("softwareInput", "Software", softwareDic, "");
    var _referenceDescriptionInput = new InputForm("referenceDescriptionInput", "text", "Reference description", "");
    var _referenceDescriptionLinkInput = new InputForm("referenceDescriptionLinkInput", "url", "Reference description link", "");
    var _createdDateInput = new InputForm("createdDateInput", "text", "Created date", "");
    var _modifiedDateInput = new InputForm("modifiedDateInput", "text", "Modified date", "");
    var _rightsInput = new InputForm("rightsInput", "text", "Rights", "");
    var _notesInput = new TextAreaForm("notesInput", "Notes:", "");
    var _curatedInput = new InputForm("curatedInput", "checkbox", "Curated", "");
    var _typeInput = new SelectForm("typeInput", "Model type", modelTypeDic, "");
    var _subjectInput = new SelectForm("subjectInput", "Model subject", modelClassDic, "");
    var _foodProcessInput = new InputForm("foodProcessInput", "text", "Food process", "");
    var _hasDataInput = new InputForm("hasDataInput", "checkbox", "Has data?:", "");

    editor.init = function (representation, value)
    {
        _value = value;

        // Initialize input with input metadata
        _modelNameInput.value = _value.metadata.modelName;
        _modelIdInput.value = _value.metadata.modelId;
        _modelLinkInput.value = _value.metadata.modelLink;
        _organismInput.value = _value.metadata.organism;
        _organismDetailsInput.value = _value.metadata.organismDetails;
        _matrixInput.value = _value.metadata.matrix;
        _matrixDetailsInput.value = _value.metadata.matrixDetails;
        _creatorInput.value = _value.metadata.creator;
        _familyNameInput.value = _value.metadata.familyName;
        _contactInput.value = _value.metadata.contact;
        _softwareInput.value = _value.metadata.software;
        _referenceDescriptionInput.value = _value.metadata.referenceDescription;
        _referenceDescriptionLinkInput.value = _value.metadata.referenceDescriptionLink;
        _createdDateInput.value = _value.metadata.createdDate;
        _modifiedDateInput.value = _value.metadata.modifiedDate;
        _rightsInput.value = _value.metadata.rights;
        _notesInput.value = _value.metadata.notes;
        _curatedInput.value = _value.metadata.curated;
        _typeInput.value = _value.metadata.type;
        _subjectInput.value = _value.metadata.subject;
        _foodProcessInput.value = _value.metadata.foodProcess;
        _hasDataInput.value = _value.metadata.hasData;

        checkVariables();
        create_body ();
    };


    editor.getComponentValue = function ()
    {
        // assign input values to _value
        _value.metadata.modelName = _modelNameInput.value;
        _value.metadata.modelId = _modelIdInput.value;
        _value.metadata.modelLink = _modelLinkInput.value;
        _value.metadata.organism = _organismInput.value;
        _value.metadata.organismDetails = _organismDetailsInput.value;
        _value.metadata.matrix = _matrixInput.value;
        _value.metadata.matrixDetails = _matrixDetailsInput.value;
        _value.metadata.creator = _creatorInput.value;
        _value.metadata.familyName = _familyNameInput.value;
        _value.metadata.contact = _contactInput.value;
        _value.metadata.software = _softwareInput.value;
        _value.metadata.referenceDescription = _referenceDescriptionInput.value;
        _value.metadata.referenceDescriptionLink = _referenceDescriptionLinkInput.value;
        _value.metadata.createdDate = _createdDateInput.value;
        _value.metadata.modifiedDate = _modifiedDateInput.value;
        _value.metadata.rights = _rightsInput.value;
        _value.metadata.notes = _notesInput.value;
        _value.metadata.curated = _curatedInput.value;
        _value.metadata.type = _typeInput.value;
        _value.metadata.subject = _subjectInput.value;
        _value.metadata.foodProcess = _foodProcessInput.value;
        _value.metadata.hasData = _hasDataInput.value;

        return _value;
    };

    return editor;

    // --- utility functions ---
    function create_body ()
    {
        var varTable =
            '<table class="table table-condensed">' +
            '  <tr>' +
            '    <th>Name</th>' +
            '    <th>Unit</th>' +
            '    <th>Type</th>' +
            '    <th>Value</th>' +
            '    <th>Min</th>' +
            '    <th>Max</th>' +
            '    <th>Dependent</th>'
            '  </tr>';
        // Row with dependent variable
        varTable +=
            '<tr>' +
            '  <td></td>' + // name
            '  <td></td>' + // unit
            '  <td></td>' + // type
            '  <td class="has-success"><input type="text" class="form-control input-sm" value"" disabled></td>' + // value
            '  <td class="has-success"><input type="text" class="form-control input-sm" value""></td>' + // min
            '  <td class="has-success"><input type="text" class="form-control input-sm" value""></td>' + // max
            '  <td class="has-success"><input type="checkbox" class="form-control" checked disabled></td>' +
            '</tr>';
        // Row with independent variables
        for (var i = 0; i < _value.metadata.independentVariables.length; i++) {
            var variable = _value.metadata.independentVariables[i];
            varTable +=
                '<tr>' +
                '  <td></td>' + // name
                '  <td></td>' + // unit
                '  <td></td>' + // type
                '  <td class="has-success"><input type="text" class="form-control input-sm" value""></td>' + // value
                '  <td class="has-success"><input type="text" class="form-control input-sm" value""></td>' + // min
                '  <td class="has-success"><input type="text" class="form-control input-sm" value""></td>' + // max
                '  <td class="has-success"><input type="checkbox" class="form-control" disabled></td>' +
                '</tr>';
        }
        varTable += '</table>';

        var form = '<form class="form-horizontal">';
        form += _modelNameInput.createHtml();
        form += _modelIdInput.createHtml();
        form += _modelLinkInput.createHtml();
        form += _organismInput.createHtml();
        form += _organismDetailsInput.createHtml();
        form += _matrixInput.createHtml();
        form += _matrixDetailsInput.createHtml();
        form += _creatorInput.createHtml();
        form += _familyNameInput.createHtml();
        form += _contactInput.createHtml();
        form += _softwareInput.createHtml();
        form += _referenceDescriptionInput.createHtml();
        form += _referenceDescriptionLinkInput.createHtml();
        form += _createdDateInput.createHtml();
        form += _modifiedDateInput.createHtml();
        form += _rightsInput.createHtml();
        form += _notesInput.createHtml();
        form += _curatedInput.createHtml();
        form += _typeInput.createHtml();
        form += _subjectInput.createHtml();
        form += _foodProcessInput.createHtml();
        form += _hasDataInput.createHtml();
        form += '</form>';

        document.createElement("body");
        $("body").html('<div class="container">' + form + varTable + '</div');

        // Create date pickers. Set date formats and save when dates change.
        $("#createdDateInput").datepicker({
            dateFormat: "mm.dd.yy",
            onSelect: function(dateText) { _createdDateInput.value = dateText; }
        });
        $("#modifiedDateInput").datepicker({
            dateFormat: "mm.dd.yy",
            onSelect: function(dateText) { _modifiedDateInput.value = dateText; }
        });

        loadData();
        saveData();
    }

    function loadData () {
        _modelNameInput.loadData();
        _modelIdInput.loadData();
        _modelLinkInput.loadData();
        _organismInput.loadData();
        _organismDetailsInput.loadData();
        _matrixInput.loadData();
        _matrixDetailsInput.loadData();
        _creatorInput.loadData();
        _familyNameInput.loadData();
        _contactInput.loadData();
        _softwareInput.loadData();
        _referenceDescriptionInput.loadData();
        _referenceDescriptionLinkInput.loadData();
        _createdDateInput.loadData();
        _modifiedDateInput.loadData();
        _rightsInput.loadData();
        _notesInput.loadData();
        _curatedInput.loadData();
        _typeInput.loadData();
        _subjectInput.loadData();
        _foodProcessInput.loadData();
        _hasDataInput.loadData();

        var depRow = $('table tr:eq(1)');
        $('td:eq(0)', depRow).text(_value.metadata.dependentVariable.name);
        $('td:eq(1)', depRow).text(_value.metadata.dependentVariable.unit);
        $('td:eq(2)', depRow).text(_value.metadata.dependentVariable.type);
        $('td:eq(3) input', depRow).val(_value.metadata.dependentVariable.value);
        $('td:eq(4) input', depRow).val(_value.metadata.dependentVariable.min);
        $('td:eq(5) input', depRow).val(_value.metadata.dependentVariable.max);

        for (var i = 0; i < _value.metadata.independentVariables.length; i++) {
            var variable = _value.metadata.independentVariables[i];
            var tableRow = $('table tr:eq(' + (i + 2) + ')');

            $('td:eq(0)', tableRow).text(variable.name);
            $('td:eq(1)', tableRow).text(variable.unit);
            $('td:eq(2)', tableRow).text(variable.type);
            $('td:eq(3) input', tableRow).val(variable.value);
            $('td:eq(4) input', tableRow).val(variable.min);
            $('td:eq(5) input', tableRow).val(variable.max);

            // Disable arrays
            if (variable.type === 'array') {
                $('td:eq(3) input', tableRow).prop('disabled', true);
                $('td:eq(4) input', tableRow).prop('disabled', true);
                $('td:eq(5) input', tableRow).prop('disabled', true);
            }
        }
    }

    /**
     * Saves data on modification.
     * 
     * - Text inputs use the oninput event.
     * - Selects and checkboxes use the onchange event.
     * - jQuery datepickers are already saving their data. No need to save it here.
     */
    function saveData () {
        _modelNameInput.saveData();
        _modelIdInput.saveData();
        _modelLinkInput.saveData();
        _organismInput.saveData();
        _organismDetailsInput.saveData();
        _matrixInput.saveData();
        _matrixDetailsInput.saveData();
        _creatorInput.saveData();
        _familyNameInput.saveData();
        _contactInput.saveData();
        _softwareInput.saveData();
        _referenceDescriptionInput.saveData();
        _referenceDescriptionLinkInput.saveData();
        _createdDateInput.saveData();
        _modifiedDateInput.saveData();
        _rightsInput.saveData();
        _notesInput.saveData();
        _curatedInput.saveData();
        _typeInput.saveData();
        _subjectInput.saveData();
        _foodProcessInput.saveData();
        _hasDataInput.saveData();

        // Saves and validates changes in variables table (dependent variable)
        var depRow = $('table tr:eq(1)');
        $('td:eq(4) input', depRow).on('input', function() {
            var newVal = Number($(this).val());
            var max = Number(_value.metadata.dependentVariable.max);

            if (newVal < max) {
                _value.metadata.dependentVariable.min = newVal;
                markValidTd($(this).parent());
            } else {
                markInvalidTd($(this).parent());
            }
        });
        $('td:eq(5) input', depRow).on('input', function() {
            var newVal = Number($(this).val());
            var min = Number(_value.metadata.dependentVariable.min);
            if (newVal > min) {
                _value.metadata.dependentVariable.max = newVal;
                markValidTd($(this).parent());
            } else {
                markInvalidTd($(this).parent());
            }
        });

        // Save changes in variables table (independent variables)
        $("body div table tr:gt(1)").each(function(i, row) {
            // Value change
            $("td:eq(3) input", this).on('input', function() {
                var newVal = Number($(this).val());
                if (_value.metadata.independentVariables[i].type === 'integer'
                    && newVal % 2 != 0) {
                    markInvalidTd($(this).parent());
                } else {
                    var min = Number(_value.metadata.independentVariables[i].min);
                    var max = Number(_value.metadata.independentVariables[i].max);
                    if (min <= newVal && newVal <= max) {
                        _value.metadata.independentVariables[i].value = newVal;
                        markValidTd($(this).parent());
                    } else {
                        markInvalidTd($(this).parent());
                    }
                }
            });
            // Minimum value change
            $("td:eq(4) input", this).on('input', function() {
                var newVal = Number($(this).val());
                var max = Number(_value.metadata.independentVariables[i].max);
                if (newVal < max) {
                    _value.metadata.independentVariables[i].min = newVal;
                    markValidTd($(this).parent());
                } else {
                    markInvalidTd($(this).parent());
                }
            });
            // Maximum value change
            $("td:eq(5) input", this).on('input', function() {
                var newVal = Number($(this).val());
                var min = Number(_value.metadata.independentVariables[i].min);
                if (newVal > min) {
                    _value.metadata.independentVariables[i].max = newVal;
                    markValidTd($(this).parent());
                } else {
                    markInvalidTd($(this).parent());
                }
            });
        });

        /** Mark a table cell as valid. */
        function markValidTd(td) {
            td.removeClass('has-error');
            td.addClass('has-success');
        }

        /** Mark a table cell as invalid. */
        function markInvalidTd(td) {
            td.removeClass('has-success');
            td.addClass('has-error');
        }
    }

    /** Check variables. Integer variables will truncate decimals. */
    function checkVariables() {
        for (var i = 0; i < _value.metadata.independentVariables.length; i++) {
            var variable = _value.metadata.independentVariables[i];
            if (variable.type === 'integer') {
                variable.value = Math.floor(variable.value);
            }
        }
    }
}();
