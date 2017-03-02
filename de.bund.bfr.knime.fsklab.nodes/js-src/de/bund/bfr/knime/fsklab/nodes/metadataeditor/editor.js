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
            var form = '' +
            '<div class="form-group">' +
            '  <label for="' + this.id + '" class="col-sm-3 control-label">' + this.label + '</label>' +
            '  <div class="col-sm-9">';
            if (this.type === 'text') {
                form += '<input type="text" class="form-control no-border" id="' + this.id + '" value="">';
            } else if (this.type === 'url') {
                form += '<input type="url" class="form-control no-border" id="' + this.id + '" value="">';
            } else if (this.type == 'checkbox') {
                form += '<input id="' + id + '" type="checkbox">';
            }
            form += '</div></div>';

            return form;
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


    function VariableRowBase() {
        this.variable = null;
        
        // HTML elements
        this.row = null;
        
        this.nameInput = null;
        this.unitInput = null;
        this.typeSelect = null;
        this.valueInput = null;
        this.minInput = null;
        this.maxInput = null;
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
     * The value property is undefined or null for dependent variable.
     */
    function VariableRow(variable, is_dependent) {
        VariableRowBase.call(this);
        this.variable = variable;
        this.is_dependent = is_dependent;
        return this;
    }
    VariableRow.prototype = Object.create(VariableRowBase.prototype);
    VariableRow.prototype.constructor = VariableRowBase;
    
    VariableRow.prototype.createHtml = function() {
        var name = this.variable.name === null ? "" : this.variable.name;
        var unit = this.variable.unit === null ? "" : this.variable.unit;
        var value = this.variable.value === null ? "" : this.variable.value;
        var min = this.variable.min === null ? "" : this.variable.min;
        var max = this.variable.max === null ? "" : this.variable.max;

        var row = '<tr>' +
            // Name column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="' + name + '">' +
            '</td>' +
            // Unit column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="' + unit + '">' +
            '</td>' +
            // Data type column
            '<td class="has-success">' +
            '  <select class="form-control no-border">' +
            '    <option value="character">character</option>' +
            '    <option value="integer">integer</option>' +
            '    <option value="numeric">numeric</option>' +
            '    <option value="array">array</option>' +
            '  </select>' +
            '</td>' +
            // Value column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="' + value + '">' +
            '</td>' +
            // Min column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="' + min + '">' +
            '</td>' +
            // Max column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="' + max + '">' +
            '</td>' +
            // Dependent column: whether the parameter is dependent
            '<td><input type="checkbox" class="form-control input-sm"' +
                (this.is_dependent ? " checked" : "") + '></td>' +
            // Remove parameter button
            '<td>' +
            '  <button type="button" class="btn btn-default btn-danger">' +
            '    <span class="glyphicon glyphicon-minus"></span>' +
            '  </button>' +
            '</td>' +
            '</tr>'; 

        return row;
    };
    
    VariableRow.prototype.loadData = function() {
        var outer = this;
        this.row = $('tbody tr td:first-child input').filter(function() {
            return $(this).val() == outer.variable.name;
        }).parent().parent();

        // Caches HTML elements
        this.nameInput = $('td:eq(0) input', this.row);
        this.unitInput = $('td:eq(1) input', this.row);
        this.typeSelect = $('td:eq(2) select', this.row);
        this.valueInput = $('td:eq(3) input', this.row);
        this.minInput = $('td:eq(4) input', this.row);
        this.maxInput = $('td:eq(5) input', this.row);
        this.depInput = $('td:eq(6) input', this.row);
        this.removeButton = $('td:eq(7) button', this.row);
        
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

                outer.variable.value = "";
                outer.variable.min = "";
                outer.variable.max = "";

                outer.valueInput.val("");
                outer.minInput.val("");
                outer.maxInput.val("");
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
            
            // outer.variable.value = outer.valueInput.val();
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
        this.depInput.change(function() {
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

        this.removeButton.click(function() {
            _variableRows.splice(outer.row.index(), 1);  // Remove this row data
            outer.row.remove();  // Remove row from table
        });
    }; 
    
    function NewVariableRow() {
        VariableRowBase.call(this);
        this.variable = new Variable();
        
        return this;
    } 
    NewVariableRow.prototype = Object.create(VariableRowBase.prototype);
    NewVariableRow.prototype.constructor = VariableRowBase;
    
    NewVariableRow.prototype.createHtml = function() {
        var row = '<tr>' +
            // Name column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="">' +
            '</td>' +
            // Unit column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="">' +
            '</td>' +
            // Data type column
            '<td class="has-success">' +
            '  <select class="form-control no-border">' +
            '    <option value="character">character</option>' +
            '    <option value="integer">integer</option>' +
            '    <option value="numeric">numeric</option>' +
            '    <option value="array">array</option>' +
            '  </select>' +
            '</td>' +
            // Value column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="">' +
            '</td>' +
            // Min column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="">' +
            '</td>' +
            // Max column
            '<td class="has-success">' +
            '  <input type="text" class="form-control input-sm" value="">' +
            '</td>' +
            // Dependent column (disabled)
            '<td>' +
            '  <input type="checkbox" class="form-control input-sm" disabled>' +
            '</td>' +
            // Add parameter button
            '<td>' +
            '  <button type="button" class="btn btn-default btn-success">' +
            '    <span class="glyphicon glyphicon-plus"></span>' +
            '  </button>' +
            '</td>' +
            '</tr>';
    
        return row;
    };
    
    NewVariableRow.prototype.loadData = function() {
        this.row = $('tfoot tr');
        
        this.nameInput = $('td:eq(0) input', this.row);
        this.unitInput = $('td:eq(1) input', this.row);
        this.typeSelect = $('td:eq(2) select', this.row);
        this.valueInput = $('td:eq(3) input', this.row);
        this.minInput = $('td:eq(4) input', this.row);
        this.maxInput = $('td:eq(5) input', this.row);
    };
    
    NewVariableRow.prototype.saveData = function() {
        var outer = this;

        this.nameInput.on('input', function() {
            var nameTd = outer.nameInput.parent();  // Parent td of nameInput 
            
            // If name is repeated mark cell as invalid
            if (outer.nameInput.val()) {
                // Gets td > tr > tr number
                var numRow = outer.row.index();
                var names = [];
                $('tbody tr').each(function(index, element) {
                    if (index !== 0  && index !== numRow) {
                        names.push($('td:first-child input', element).val());
                    }
                });

                if (names.indexOf(outer.nameInput.val()) > -1) {
                    _markInvalidTd(nameTd);
                } else {
                    _markValidTd(nameTd);
                    outer.variable.name = outer.nameInput.val();
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

        $('td:eq(7) button', this.row).click(function() {
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
                $('tbody').append(variableRow.createHtml());
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
            $('#' + this.id).val(this.value ? "" : this.value);
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
            var form = '' +
            '<div class="form-group">' +
            '  <label for="' + this.id + '" class="col-sm-3 control-label">' + this.label + '</label>' +
            '  <div class="col-sm-9">' +
            '    <select class="form-control no-border" id="' + this.id + '">';
            for (var key in this.entries) {
                form += '<option value="' + key + '">' + entries[key] + '</option>';
            }
            form += '</select></div></div>';

            return form;
        };

        this.loadData = function() {
            if (this.value) {
                var option = $('#' + this.id + ' option[value="' + this.value + '"]');
                option.prop('selected', true);
            }
        };

        this.saveData = function() {
            var outer = this;
            $('#' + this.id).change(function() { outer.value = $(this).val(); });
        };

        return this;
    }

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

    var _variableRows;
    var _newVariableRow;

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
        
        // FIXME dummy
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
            '  <tfoot>' + _newVariableRow.createHtml() + '</tfoot>' +
            '  <tbody>';
        for (var i = 0; i < _variableRows.length; i++) {
            varTable += _variableRows[i].createHtml();
        }
        varTable += '</tbody></table>';

        var form = '<form class="form-horizontal">' +
            _modelNameInput.createHtml() +
            _modelIdInput.createHtml() +
            _modelLinkInput.createHtml() +
            _organismInput.createHtml() +
            _organismDetailsInput.createHtml() +
            _matrixInput.createHtml() +
            _matrixDetailsInput.createHtml() +
            _creatorInput.createHtml() +
            _familyNameInput.createHtml() +
            _contactInput.createHtml() +
            _softwareInput.createHtml() +
            _referenceDescriptionInput.createHtml() +
            _referenceDescriptionLinkInput.createHtml() +
            _createdDateInput.createHtml() +
            _modifiedDateInput.createHtml() +
            _rightsInput.createHtml() +
            _notesInput.createHtml() +
            _curatedInput.createHtml() +
            _typeInput.createHtml() +
            _subjectInput.createHtml() +
            _foodProcessInput.createHtml() +
            _hasDataInput.createHtml() +
            '</form>';

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
