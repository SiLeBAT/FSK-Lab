/*jshint esversion: 6 */
/* global $ */
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
    var dataTypeDic = {
        'character': 'character',
        'integer': 'integer',
        'numeric': 'numeric',
        'array': 'array'
    };

    function Variable() {
        this.name = '';
        this.unit = '';
        this.value = '';
        this.min = '';
        this.max = '';
        this.type = '';

        this.clone = function() {
            return {
                'name': this.name,
                'unit': this.unit,
                'value': this.value,
                'min': this.min,
                'max': this.max,
                'type': this.type
            };
        };

        return this;
    }

    /**
     * Create a form-group envolving an input.
     * - type: Input type {text, url, checkbox}
     * - label: Text label
     * 
     * The input is accesible through the property `input`.
     */
    function InputForm(type, label) {
        var form = $('<div class="form-group">' +
            '  <label class="col-sm-3 control-label">' + label + '</label>' +
            '  <div class="col-sm-9"></div>' +
            '</div>');
        form.input = new Input(type);
        $('div', form).append(form.input);
        
        return form;
    }
    
    function Input(type) {
        var input;
        
        if (type === 'text') {
            input = $('<input type="text" class="form-control no-border" value="">');
        } else if (type === 'url') {
            input = $('<input type="url" class="form-control no-border" value="">');
        } else if (type === 'checkbox') {
            input = $('<input type="checkbox" class="form-control no-border">');
        } else if (type === 'date') {
            input = $('<input type="text" class="form-control no-border">');
            input.datepicker({ dateFormat: 'mm.dd.yy' });
            input.click(function() { $(this).datepicker('show'); });
        }
        
        return input;
    }
    
    /**
     * Create a form-group envolving a textarea.
     * - label: Text label
     */
    function TextAreaForm(label) {
        var form = $('<div class="form-group">' +
            '  <label class="col-sm-3 control-label">' + label + '</label>' +
            '  <div class="col-sm-9"></div>' +
            '</div>');
        form.textarea = $('<textarea class="form-control no-border" rows="3"></textarea>');
        $('div', form).append(form.textarea);
        
        return form;
    }

    function Select(entries) {
        var select = $('<select class="form-control no-border"></select>');
        for (var key in entries) {
            select.append($('<option value="' + key + '">' + entries[key] + '</option>'));
        }
        return select;
    }
    
    /**
     * Create a form-group envolving a select.
     * - label: Text label
     * - entries: Dictionary where the keys are the labels and the values are
     *   the option values.
     */
    function SelectForm(label, entries) {
        var form = $('<div class="form-group">' +
            '  <label class="col-sm-3 control-label">' + label + '</label>' +
            '  <div class="col-sm-9"></div>' +
            '</div>');
        form.input = new Select(entries);
        $('div', form).append(form.input);
        
        return form;
    } 
    
    /**
     * TableInput.
     * 
     * Text input with border. Extends Input.
     */
    function TableInput() {
        var input = new Input('text');
        input.removeClass('no-border');
        
        return input;
    }
    
    /**
     * Table select.
     * 
     * Table select with border. Extends Select.
     */
     function TableSelect(entries) {
         var input = new Select(entries);
         input.removeClass('no-border');
         
         return input;
     }

    function VariableRowBase() {
        this.variable = null;
        
        // HTML elements
        this.nameInput = new TableInput();
        this.unitInput = new TableInput();
        this.typeSelect = new TableSelect(dataTypeDic);
        this.valueInput = new TableInput();
        this.minInput = new TableInput();
        this.maxInput = new TableInput();
        this.isDependentInput = null;
        this.removeAddButton = null;
        
        this.row = $('<tr>' +
            '<td class="has-success">' +  // name column
            '<td class="has-success">' +  // unit column
            '<td class="has-success">' +  // data type column
            '<td class="has-success">' +  // value column
            '<td class="has-success">' +  // min column
            '<td class="has-success">' +  // max column
            '<td></td>' +  // dependent column
            '<td></td>' +  // remove parameter button
            '</tr>'
        );
            
        // Add HTML elements to this.row
        $('td:eq(0)', this.row).append(this.nameInput);
        $('td:eq(1)', this.row).append(this.unitInput);
        $('td:eq(2)', this.row).append(this.typeSelect);
        $('td:eq(3)', this.row).append(this.valueInput);
        $('td:eq(4)', this.row).append(this.minInput);
        $('td:eq(5)', this.row).append(this.maxInput);
    }
    
    /**
     * Returns whether the name is valid.
     * name is not null or undefined.
     */
    VariableRowBase.prototype.validateName = function() {
        var outer = this; 
        $('tbody tr').filter(function() {$(this) !== outer.row}).each(function() {
            var currInput = $('td:first-child input', this);
            if (outer.nameInput.val() === currInput.val()) {
                return false;
            }
        });
        return true;
    };
        
    /**
     * Returns whether value is valid.
     * value is not null or undefined.
     */
    VariableRowBase.prototype.validateValue = function() {
        var value = Number(this.valueInput.val());
        var min = Number(this.minInput.val());
        var max = Number(this.maxInput.val());
        
        return min <= value && value <= max;
    };
    
    /**
     * Returns whether min is valid.
     * min and max are not null or undefined.
     */
    VariableRowBase.prototype.validateMin = function() {
        var min = Number(this.minInput.val());
        var max = Number(this.maxInput.val());
        
        return min <= max;
    };
    
    /**
     * Returns whether max is valid.
     * min and max are not null or undefined.
     */
    VariableRowBase.prototype.validateMax = function() {
        var min = Number(this.minInput.val());
        var max = Number(this.maxInput.val());

        return max >= min;
    };
    
    /**
     * Create a table row with variable data.
     * 
     * Changes with VariableRowBase:
     * - isDependentInput is enabled
     * - removeAddButton is a removeButton
     */
    function VariableRow(variable, is_dependent) {
        VariableRowBase.call(this);
        this.variable = variable;
        this.is_dependent = is_dependent;
       
        // HTML elements
        this.isDependentInput = $('<input type="checkbox" class="form-control input-sm">');
        this.removeAddButton = $('<button type="button" class="btn btn-default btn-danger">' +
            '  <span class="glyphicon glyphicon-minus"></span>' +
            '</button>'); 
            
        $('td:eq(6)', this.row).append(this.isDependentInput);
        $('td:eq(7)', this.row).append(this.removeAddButton);
        
        return this;
    }
    VariableRow.prototype = Object.create(VariableRowBase.prototype);
    VariableRow.prototype.constructor = VariableRowBase;
    
    VariableRow.prototype.loadData = function() {
        this.nameInput.val(this.variable.name ? this.variable.name : "");
        this.unitInput.val(this.variable.unit ? this.variable.unit : "");
        
        // Mark variable type as selected in typeSelect
        $('option[value=' + this.variable.type + ']', this.typeSelect).prop('selected', true);
        
        this.valueInput.val(this.variable.value);
        this.minInput.val(this.variable.min);
        this.maxInput.val(this.variable.max);

        // Disable value, min and max inputs for array variables
        if (this.variable.type === 'array') {
            this.valueInput.prop('disabled', true);
            this.minInput.prop('disabled', true);
            this.maxInput.prop('disabled', true);
        }
        
        if (this.is_dependent) {
            this.isDependentInput.prop(':checked', true);
        }
    };
    
    VariableRow.prototype.saveData = function() {
        var outer = this;

        this.nameInput.on('input', function() {
            
            var nameTd = outer.nameInput.parent();
            if (outer.nameInput.val()) {
                if (outer.validateName()) {
                    _markValidTd(nameTd);
                } else {
                    _markInvalidTd(nameTd);
                }
            }
            // If name is empty mark cell as invalid
            else {
                _markInvalidTd(nameTd);
            }
        });

        this.unitInput.on('input', function() { outer.variable.unit = $(this).val(); });

        // When the type changed discards the former value, min and max
        this.typeSelect.change(function() {
            if (outer.variable.type !== $(this).val()) {
                outer.variable.type = $(this).val();

                outer.variable.value = '';
                outer.variable.min = '';
                outer.variable.max = '';

                outer.valueInput.val('');
                outer.minInput.val('');
                outer.maxInput.val('');
            }
        });
        
        // Validates value
        this.valueInput.on('input', function() {
            var newVal = Number(outer.valueInput.val());
            var valTd = outer.valueInput.parent();
            
            if (outer.variable.type === 'integer' && newVal % 2 !== 0) {
                _markInvalidTd(valTd);
            } else {
                if (outer.validateValue()) {
                    _markValidTd(valTd);
                    outer.variable.value = outer.valueInput.val();
                } else {
                    _markInvalidTd(valTd);
                }
            }
        });
            
        // Validate min
        this.minInput.on('input', function() {
            var minTd = outer.maxInput.parent();  // Parent td of minInput
            
            if (outer.validateMin()) {
                _markValidTd(minTd);
            } else {
                _markInvalidTd(minTd);
            }
        });
        
        // Validate max
        this.maxInput.on('input', function() {
            var maxTd = outer.maxInput.parent();  // Parent td of maxInput
            
            if (outer.validateMax()) {
                _markValidTd(maxTd);
            } else {
                _markInvalidTd(maxTd);
            }
        });


        // When a dependent checkbox is checked, then uncheck the
        // previously checked checkbox
        this.isDependentInput.change(function() {
            // Style row with dependent variable
            outer.row.addClass('danger');

            if ($(this).is(':checked')) {
                var checkboxes = $('td input[type=checkbox]:checked');
                checkboxes.each(function() {
                    // Get number of current row: input > td
                    var currRow = $(this).parent().parent();
                    if (currRow.index() != outer.row.index()) {
                        $(this).prop('checked', false);
                        currRow.removeClass('danger');
                    }
                });
            }
        });

        this.removeAddButton.click(function() {
            _variableRows.splice(outer.row.index(), 1);  // Remove this row data
            outer.row.remove();  // Remove row from table
        });
    }; 
    
    /**
     * Create a table row to introduce a new variable.
     * 
     * Changes with VariableRowBase:
     * - isDependentInput is disabled
     * - removeAddButton is an add button
     */
    function NewVariableRow() {
        VariableRowBase.call(this);
        this.variable = new Variable();
        
        // HTML elements
        this.isDependentInput = $('<input type="checkbox" class="form-control input-sm" disabled>');
        this.removeAddButton = $('<button type="button" class="btn btn-default btn-success">' +
            '  <span class="glyphicon glyphicon-plus"></span>' +
            '</button>');
        
        $('td:eq(6)', this.row).append(this.isDependentInput);
        $('td:eq(7)', this.row).append(this.removeAddButton);
        
        return this;
    } 
    NewVariableRow.prototype = Object.create(VariableRowBase.prototype);
    NewVariableRow.prototype.constructor = VariableRowBase;
    
    NewVariableRow.prototype.loadData = function() {
    };
    
    NewVariableRow.prototype.saveData = function() {
        var outer = this;

        this.nameInput.on('input', function() {
            outer.variable.name = outer.nameInput.val();
        });

        this.unitInput.on('input', function() { outer.variable.unit = $(this).val(); });

        // When the type changed discards the former value, min and max
        this.typeSelect.change(function() {
            if (outer.variable.type !== $(this).val()) {
                outer.variable.type = $(this).val();

                outer.variable.value = '';
                outer.variable.min = '';
                outer.variable.max = '';

                outer.valueInput.val('');
                outer.minInput.val('');
                outer.maxInput.val('');
            }
        });
    
        // Saves value from valueInput. The value is not validated here but
        // in the `Add parameter` event
        this.valueInput.on('input', function() {
            outer.variable.value = outer.valueInput.val();
        });
        
        // Saves min from minInput. The min is not validated here but in the
        // `Add parameter` event
        this.minInput.on('input', function() {
            outer.variable.min = outer.minInput.val();
        });
    
        // Saves max from maxInput. The max is not validated here but in the
        // `Add parameter` event
        this.maxInput.on('input', function() {
            outer.variable.max = outer.maxInput.val();
        });

        this.removeAddButton.click(function() {
            if (!outer.nameInput.val()) {
                outer.createWarning('Missing name');
            } else if (!outer.unitInput.val()) {
                outer.createWarning('Missing unit');
            } else if (!outer.typeSelect.val()) {
                outer.createWarning('Missing type');
            } else if (!outer.valueInput.val()) {
                outer.createWarning('Missing value');
            } else if (!outer.minInput.val()) {
                outer.createWarning('Missing min');
            } else if (!outer.maxInput.val()) {
                outer.createWarning('Missing max');
            }
            else if (!outer.validateName()) {
                outer.createWarning('Invalid name: ' + outer.nameInput.val());
            }
            
            else if (!outer.validateValue()) {
                outer.createWarning('Invalid value: ' + outer.valueInput.val());
            }
            
            else if (!outer.validateMin()) {
                outer.createWarning('Invalid min: ' + outer.minInput.val());
            }
            
            else if (!outer.validateMax()) {
                outer.createWarning('Invalid max: ' + outer.maxInput.val());
            }
            
            else {
                // Create new row with new variable data
                var variableRow = new VariableRow(outer.variable.clone());

                // Insert new row just on top of the new variable row
                $('tbody').append(variableRow.row);
                variableRow.loadData();
                variableRow.saveData();

                // Include the new variable data at the end of _variableRows
                _variableRows.push(variableRow);

                // Wipe out contents of the new variable data row
                outer.nameInput.val('');
                outer.unitInput.val('');
                outer.valueInput.val('');
                outer.minInput.val('');
                outer.maxInput.val('');
                outer.typeSelect.val('');
            }
        });
    };
    
    NewVariableRow.prototype.createWarning = function(reason) {
        var warning = $('<div class="alert alert-danger" role="alert">' + reason + '</div>');
        $('table').before(warning);
        window.setTimeout(function() {warning.remove();}, 5000);
    };



    // Util
    /** Mark a table cell as valid. */
    function _markValidTd(td) {
        td.removeClass('has-error');
        td.addClass('has-success');
    }

    /** Mark a table cell as invalid. */
    function _markInvalidTd(td) {
        td.removeClass('has-success');
        td.addClass('has-error');
    }


    var editor = {
	   version: '0.0.1'
    };
    editor.name = 'FSK Metadata Editor';

    var _value;  // Raw FskMetadataEditorViewValue

    var _modelNameInput = new InputForm('text', 'Model name');
    var _modelIdInput = new InputForm('text', 'Model id');
    var _modelLinkInput = new InputForm('url', 'Model link');
    var _organismInput = new InputForm('text', 'Organism');
    var _organismDetailsInput = new InputForm('text', 'Organism details');
    var _matrixInput = new InputForm('text', 'Matrix');
    var _matrixDetailsInput = new InputForm('text', 'Matrix details');
    var _creatorInput = new InputForm('text', 'Creator');
    var _familyNameInput = new InputForm('text', 'Family name');
    var _contactInput = new InputForm('text', 'Contact');
    var _softwareInput = new SelectForm('Software', softwareDic);
    var _referenceDescriptionInput = new InputForm('text', 'Reference description');
    var _referenceDescriptionLinkInput = new InputForm('url', 'Reference description link');
    var _createdDateInput = new InputForm('date', 'Created date');
    var _modifiedDateInput = new InputForm('date', 'Modified date');
    var _rightsInput = new InputForm('text', 'Rights');
    var _notesInput = new TextAreaForm('Notes');
    var _curatedInput = new InputForm('checkbox', 'Curated');
    var _typeInput = new SelectForm('Model type', modelTypeDic);
    var _subjectInput = new SelectForm('Model subject', modelClassDic);
    var _foodProcessInput = new InputForm('text', 'Food process');
    var _hasDataInput = new InputForm('checkbox', 'Has data?');

    var _variableRows;
    var _newVariableRow;

    editor.init = function (representation, value)
    {
        _value = value;

        _modelNameInput.input.val(_value.metadata.modelName);
        _modelIdInput.input.val(_value.metadata.modelId);
        _modelLinkInput.input.val(_value.metadata.modelLink);
        _organismInput.input.val(_value.metadata.organism);
        _organismDetailsInput.input.val(_value.metadata.organismDetails);
        _matrixInput.input.val(_value.metadata.matrix);
        _matrixDetailsInput.input.val(_value.metadata.matrixDetails);
        _creatorInput.input.val(_value.metadata.creator);
        _familyNameInput.input.val(_value.metadata.familyName);
        _contactInput.input.val(_value.metadata.contact);
        $('option[value=' + _value.metadata.software + ']', _softwareInput.input).prop('selected', true);
        _referenceDescriptionInput.input.val(_value.metadata.referenceDescription);
        _referenceDescriptionLinkInput.input.val(_value.metadata.referenceDescriptionLink);
        _createdDateInput.input.datepicker('setDate', _value.metadata.createdDate);
        _modifiedDateInput.input.datepicker('setDate', _value.metadata.modifiedDate);
        _rightsInput.input.val(_value.metadata.rights);
        _notesInput.textarea.val(_value.metadata.notes);
        _curatedInput.input.val(_value.metadata.curated);
        $('option[value=' + _value.metadata.type + ']', _typeInput.input).prop('selected', true);
        $('option[value=' + _value.metadata.subject + ']', _subjectInput.input).prop('selected', true);
        _foodProcessInput.input.val(_value.metadata.foodProcess);
        _hasDataInput.input.val(_value.metadata.hasData);

        _variableRows = [];
        _variableRows.push(new VariableRow(_value.metadata.dependentVariable, true));
        for (var i = 0; i < _value.metadata.independentVariables.length; i++) {
            _variableRows.push(new VariableRow(_value.metadata.independentVariables[i], false));
        }
        _newVariableRow = new NewVariableRow();

        checkVariables();
        create_body ();
    };


    editor.getComponentValue = function ()
    {
        _value.metadata.modelName = _modelNameInput.input.val();
        _value.metadata.modelId = _modelIdInput.input.val();
        _value.metadata.modelLink = _modelLinkInput.input.val();
        _value.metadata.organism = _organismInput.input.val();
        _value.metadata.organismDetails = _organismDetailsInput.input.val();
        _value.metadata.matrix = _matrixInput.input.val();
        _value.metadata.matrixDetails = _matrixDetailsInput.input.val();
        _value.metadata.creator = _creatorInput.input.val();
        _value.metadata.familyName = _familyNameInput.input.val();
        _value.metadata.contact = _contactInput.input.val();
        _value.metadata.software = _softwareInput.input.val();
        _value.metadata.referenceDescription = _referenceDescriptionInput.input.val();
        _value.metadata.referenceDescriptionLink = _referenceDescriptionLinkInput.input.val();
        _value.metadata.createdDate = _createdDateInput.input.datepicker('getDate');
        _value.metadata.modifiedDate = _modifiedDateInput.input.datepicker('getDate');
        _value.metadata.rights = _rightsInput.input.val();
        _value.metadata.notes = _notesInput.textarea.val();
        _value.metadata.curated = _curatedInput.input.val();
        _value.metadata.type = _typeInput.input.val();
        _value.metadata.subject = _subjectInput.input.val();
        _value.metadata.foodProcess = _foodProcessInput.input.val();
        _value.metadata.hasData = _hasDataInput.input.val();
        
        _value.metadata.independentVariables = [];
        for (var i = 0; i < _variableRows.length; i++) {
            var varRow = _variableRows[i];
            if ($('td input[type=checkbox]', varRow.row).is(':checked')) {
                _value.metadata.dependentVariable = varRow.variable;
            } else {
                _value.metadata.independentVariables.push(varRow.variable);
            }
        }

        return _value;
    };

    return editor;

    // --- utility functions ---
    function create_body ()
    {
        var varTable =
            '<table class="table table-condensed">' +
            '  <thead><tr>' +
            '    <th>Name</th>' +
            '    <th>Unit</th>' +
            '    <th>Type</th>' +
            '    <th>Value</th>' +
            '    <th>Min</th>' +
            '    <th>Max</th>' +
            '    <th>Dependent</th>' +
            '    <th>Remove</th>' +
            '  </tr></thead>' +
            '  <tfoot></tfoot>' +
            '  <tbody></tbody>' +
            '</table>';

        var form = '<form class="form-horizontal"></form>';

        document.createElement("body");
        $("body").html('<div class="container">' + form + varTable + '</div');
        
        // Add inputs for form
        $('.form-horizontal').append(_modelNameInput);
        $('.form-horizontal').append(_modelIdInput);
        $('.form-horizontal').append(_modelLinkInput);
        $('.form-horizontal').append(_organismInput);
        $('.form-horizontal').append(_organismDetailsInput);
        $('.form-horizontal').append(_matrixInput);
        $('.form-horizontal').append(_matrixDetailsInput);
        $('.form-horizontal').append(_creatorInput);
        $('.form-horizontal').append(_familyNameInput);
        $('.form-horizontal').append(_contactInput);
        $('.form-horizontal').append(_softwareInput);
        $('.form-horizontal').append(_referenceDescriptionInput);
        $('.form-horizontal').append(_referenceDescriptionLinkInput);
        $('.form-horizontal').append(_createdDateInput);
        $('.form-horizontal').append(_modifiedDateInput);
        $('.form-horizontal').append(_rightsInput);
        $('.form-horizontal').append(_notesInput);
        $('.form-horizontal').append(_curatedInput);
        $('.form-horizontal').append(_typeInput);
        $('.form-horizontal').append(_subjectInput);
        $('.form-horizontal').append(_foodProcessInput);
        $('.form-horizontal').append(_hasDataInput);
        
        // Add rows to table
        for (var i = 0; i < _variableRows.length; i++) {
            $('tbody').append(_variableRows[i].row);
        }
        $('tfoot').append(_newVariableRow.row);

        loadData();
        saveData();
    }

    function loadData () {
        for (var i = 0; i < _variableRows.length; i++) {
            _variableRows[i].loadData();
        }
        _newVariableRow.loadData();
    }

    /**
     * Saves data on modification.
     * 
     * - Text inputs use the oninput event.
     * - Selects and checkboxes use the onchange event.
     * - jQuery datepickers are already saving their data. No need to save it here.
     */
    function saveData () {
        for (var i = 0; i < _variableRows.length; i++) {
            _variableRows[i].saveData();
        }
        _newVariableRow.saveData();
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
