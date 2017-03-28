/*jshint esversion: 6 */
/* global $ */
metadata_editor = function() {

    var softwareDic = {
        'R': 'R',
        'Matlab': 'Matlab'
    };
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
        }
        else if (type === 'url') {
            input = $('<input type="url" class="form-control no-border" value="">');
        }
        else if (type === 'checkbox') {
            input = $('<input type="checkbox" class="form-control no-border">');
        }
        else if (type === 'date') {
            input = $('<input type="text" class="form-control no-border">');
            input.datepicker({
                dateFormat: 'mm.dd.yy'
            });
            input.click(function() {
                $(this).datepicker('show');
            });
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

    function ParametersTable(depvars, indepvars) {
        /**
         * Table select with border. Extends Select.
         */
        function TableSelect(entries) {
            var input = new Select(entries);
            input.removeClass('no-border');

            return input;
        }

        /**
         * Text input with border. Extends input.
         */
        function TableInput() {
            var input = new Input('text');
            input.removeClass('no-border');

            return input;
        }

        function VariableRowBase() {
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
                '<td class="has-success">' + // name column
                '<td class="has-success">' + // unit column
                '<td class="has-success">' + // data type column
                '<td class="has-success">' + // value column
                '<td class="has-success">' + // min column
                '<td class="has-success">' + // max column
                '<td></td>' + // dependent column
                '<td></td>' + // remove parameter button
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
            $('tbody tr').filter(function() {return $(this) !== outer.row;}).each(function() {
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

            // HTML elements
            this.isDependentInput = $('td:eq(6)', this.row).append(
                '<input type="checkbox" class="form-control input-sm">');
            this.removeAddButton = $('td:eq(7)', this.row).append(
                '<button type="button" class="btn btn-default btn-danger">' +
                '  <span class="glyphicon glyphicon-minus"></span>' +
                '</button>');

            // Load data
            if (variable.name) {
                this.nameInput.val(variable.name);
            }
            if (variable.unit) {
                this.unitInput.val(variable.unit);
            }
            // Mark variable type as selected in typeSelect
            $('option[value=' + variable.type + ']', this.typeSelect).prop('selected', true);
            this.valueInput.val(variable.value);
            this.minInput.val(variable.min);
            this.maxInput.val(variable.max);

            // Disable value, min and max inputs for array variables
            if (variable.type === 'array') {
                this.valueInput.prop('disabled', true);
                this.minInput.prop('disabled', true);
                this.maxInput.prop('disabled', true);
            }

            if (is_dependent) {
                this.isDependentInput.prop(':checked', true);
            }

            // Save data
            var outer = this;

            this.nameInput.on('input', function() {
                var nameTd = outer.nameInput.parent();
                if (outer.nameInput.val()) {
                    if (outer.validateName()) {
                        outer.markValidTd(nameTd);
                    }
                    else {
                        outer.markInvalidTd(nameTd);
                    }
                }
                // If name is empty, mark cell as invalid
                else {
                    outer.markInvalidTd(nameTd);
                }
            });

            // When the type changes, discard the former value, min and max
            this.typeSelect.change(function() {
                outer.valueInput.val('');
                outer.minInput.val('');
                outer.maxInput.val('');
            });

            // Validates value
            this.valueInput.on('input', function() {
                var newVal = Number(outer.valueInput.val());
                var valTd = outer.valueInput.parent();

                if (outer.variable.type === 'integer' && newVal % 2 !== 0) {
                    outer.markInvalidTd(valTd);
                }
                else {
                    if (outer.validateValue()) {
                        outer.markValidTd(valTd);
                    }
                    else {
                        outer.markInvalidTd(valTd);
                    }
                }
            });

            // Validate min
            this.minInput.on('input', function() {
                var minTd = outer.maxInput.parent(); // Parent td of minInput

                if (outer.validateMin()) {
                    outer.markValidTd(minTd);
                }
                else {
                    outer.markInvalidTd(minTd);
                }
            });

            // Validate max
            this.maxInput.on('input', function() {
                var maxTd = outer.maxInput.parent(); // Parent td of maxInput

                if (outer.validateMax()) {
                    outer.markValidTd(maxTd);
                }
                else {
                    outer.markInvalidTd(maxTd);
                }
            });

            this.isDependentInput.change(function() {
                // Style row
                if ($(this).is(':checked')) {
                    outer.row.addClass('danger');
                }
                else {
                    outer.row.removeClass('danger');
                }
            });

            this.removeAddButton.click(function() {
                _table.variableRows.splice(outer.row.index(), 1); // Remove this row data
                outer.row.remove(); // Remove row from table
            });

            return this;
        }

        VariableRow.prototype = Object.create(VariableRowBase.prototype);
        VariableRow.prototype.constructor = VariableRowBase;

        /** Mark a table cell as valid. */
        VariableRow.prototype.markValidTd = function(td) {
            td.removeClass('has-error');
            td.addClass('has-success');
        };

        /** Mark a table cell as invalid. */
        VariableRow.prototype.markInvalidTd = function(td) {
            td.removeClass('has-success');
            td.addClass('has-error');
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

            // HTML elements
            this.isDependentInput = $('td:eq(6)', this.row).append(
                '<input type="checkbox" class="form-control input-sm" disabled>');
            this.removeAddButton = $('td:eq(7)', this.row).append(
                '<button type="button" class="btn btn-default btn-success">' +
                '  <span class="glyphicon glyphicon-plus"></span>' +
                '</button>');
            var outer = this;

            // When the type changes, discards the former value, min and max
            this.typeSelect.change(function() {
                outer.valueInput.val('');
                outer.minInput.val('');
                outer.maxInput.val('');
            });

            this.removeAddButton.click(function() {
                if (!outer.nameInput.val()) {
                    outer.createWarning('Missing name');
                }
                else if (!outer.unitInput.val()) {
                    outer.createWarning('Missing unit');
                }
                else if (!outer.typeSelect.val()) {
                    outer.createWarning('Missing type');
                }
                else if (!outer.valueInput.val()) {
                    outer.createWarning('Missing value');
                }
                else if (!outer.minInput.val()) {
                    outer.createWarning('Missing min');
                }
                else if (!outer.maxInput.val()) {
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
                    var variable = {
                        'name': outer.nameInput.val(),
                        'unit': outer.unitInput.val(),
                        'type': outer.typeSelect.val(),
                        'value': outer.valueInput.val(),
                        'min': outer.minInput.val(),
                        'max': outer.maxInput.val()
                    };
                    var variableRow = new VariableRow(variable);

                    // Insert new row just on top of the new variable row
                    $('tbody').append(variableRow.row);

                    // Include the new variable data at the end of _variableRows
                    _table.variableRows.push(variableRow);

                    // Wipe out contents of the new variable data row
                    outer.nameInput.val('');
                    outer.unitInput.val('');
                    outer.valueInput.val('');
                    outer.minInput.val('');
                    outer.maxInput.val('');
                    outer.typeSelect.val('');
                }
            });

            return this;
        }

        NewVariableRow.prototype = Object.create(VariableRowBase.prototype);
        NewVariableRow.prototype.constructor = VariableRowBase;

        NewVariableRow.prototype.createWarning = function(reason) {
            var warning = $('<div class="alert alert-danger" role="alert">' + reason + '</div>');
            $('table').before(warning);
            window.setTimeout(function() {
                warning.remove();
            }, 5000);
        };

        var table = $('<table class="table table-condensed">' +
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
            '</table>');

        table.variableRows = [];
        for (var i = 0; i < depvars.length; i++) {
            var vr = new VariableRow(depvars[i], true);
            $('tbody', table).append(vr.row);
            table.variableRows.push(vr);
        }
        for (var i = 0; i < indepvars.length; i++) {
            var vr = new VariableRow(indepvars[i], false);
            $('tbody', table).append(vr.row);
            table.variableRows.push(vr);
        }
        $('tfoot', table).append(new NewVariableRow().row);

        return table;
    }

    function Form(metadata) {
        var form = $('<form class="form-horizontal"></form>');
        form.metadata = metadata;

        // modelName input
        var modelNameInput = new InputForm('text', 'Model name');
        modelNameInput.input.val(form.metadata.modelName);
        modelNameInput.input.on('input', function() {
            form.metadata.modelName = modelNameInput.input.val();
        });
        form.append(modelNameInput);

        // model id input
        var modelIdInput = new InputForm('text', 'Model id');
        modelIdInput.input.val(form.metadata.modelId);
        modelIdInput.input.on('input', function() {
            form.metadata.modelid = modelIdInput.input.val();
        });
        form.append(modelIdInput);

        // model link input
        var modelLinkInput = new InputForm('url', 'Model link');
        modelLinkInput.input.val(form.metadata.modelLink);
        modelLinkInput.input.on('input', function() {
            form.metadata.modelLink = modelLinkInput.input.val();
        });
        form.append(modelLinkInput);

        // organism input
        var organismInput = new InputForm('text', 'Organism');
        organismInput.input.val(form.metadata.organism);
        organismInput.input.on('input', function() {
            form.metadata.organism = organismInput.input.val();
        });
        form.append(organismInput);

        // organism details input
        var organismDetailsInput = new InputForm('text', 'Organism details');
        organismDetailsInput.input.val(form.metadata.organismDetailsInput);
        organismDetailsInput.input.on('input', function() {
            form.metadata.organismDetails = organismDetailsInput.input.val();
        });
        form.append(organismDetailsInput);

        // matrix input
        var matrixInput = new InputForm('text', 'Matrix');
        matrixInput.input.val(form.metadata.matrix);
        matrixInput.input.on('input', function() {
            form.metadata.matrix = matrixInput.input.val();
        });
        form.append(matrixInput);

        // matrix details input
        var matrixDetailsInput = new InputForm('text', 'Matrix details');
        matrixDetailsInput.input.val(form.metadata.matrixDetails);
        matrixDetailsInput.input.on('input', function() {
            form.metadata.matrixDetails = matrixDetailsInput.input.val();
        });
        form.append(matrixDetailsInput);

        // creator input
        var creatorInput = new InputForm('text', 'Creator');
        creatorInput.input.val(form.metadata.creator);
        creatorInput.input.on('input', function() {
            form.metadata.creator = creatorInput.input.val();
        });
        form.append(creatorInput);

        // family name input
        var familyNameInput = new InputForm('text', 'Family name');
        familyNameInput.input.val(form.metadata.familyName);
        familyNameInput.input.on('input', function() {
            form.metadata.familyName = familyNameInput.input.val();
        });
        form.append(familyNameInput);

        // contact input
        var contactInput = new InputForm('text', 'Contact');
        contactInput.input.val(form.metadata.contact);
        contactInput.input.on('input', function() {
            form.metadata.contact = contactInput.input.val();
        });
        form.append(contactInput);

        // software input
        var softwareInput = new SelectForm('Software', softwareDic);
        softwareInput.input.val(form.metadata.software);
        softwareInput.input.change(function() {
            form.metadata.softwareInput = softwareInput.input.val();
        });
        form.append(softwareInput);

        // reference description input
        var referenceDescriptionInput = new InputForm('text', 'Reference description');
        referenceDescriptionInput.input.val(form.metadata.referenceDescriptionInput);
        referenceDescriptionInput.input.on('input', function() {
            form.metadata.referenceDescription = referenceDescriptionInput.input.val();
        });
        form.append(referenceDescriptionInput);

        // reference description link input
        var referenceDescriptionLinkInput = new InputForm('text', 'Reference link description');
        referenceDescriptionLinkInput.input.val(form.metadata.referenceDescriptionLinkInput);
        referenceDescriptionLinkInput.input.on('input', function() {
            form.metadata.referenceDescriptionLink = referenceDescriptionLinkInput.input.val();
        });
        form.append(referenceDescriptionLinkInput);

        // created date input
        var createdDateInput = new InputForm('date', 'Created date');
        createdDateInput.input.datepicker('setDate', form.metadata.createdDate);
        createdDateInput.input.datepicker({
            onSelect: function() {
                form.metadata.createdDate = this.value;
            }
        });
        form.append(createdDateInput);

        // modified date input
        var modifiedDateInput = new InputForm('date', 'Modified date');
        modifiedDateInput.input.datepicker('setDate', form.metadata.modifiedDate);
        modifiedDateInput.input.datepicker({
            onSelect: function() {
                form.metadata.modifiedDate = this.value;
            }
        });
        form.append(modifiedDateInput);

        // rights input
        var rightsInput = new InputForm('text', 'Rights');
        rightsInput.input.val(form.metadata.rights);
        rightsInput.input.on('input', function() {
            form.metadata.rights = rightsInput.input.val();
        });
        form.append(rightsInput);

        // notes input
        var notesInput = new TextAreaForm('Notes');
        notesInput.textarea.val(form.metadata.notes);
        notesInput.textarea.on('input', function() {
            form.metadata.notes = notesInput.textarea.val();
        });
        form.append(notesInput);

        // curated input
        var curatedInput = new InputForm('checkbox', 'Curated');
        curatedInput.input.val(form.metadata.curated);
        curatedInput.input.on('input', function() {
            form.metadata.curated = curatedInput.input.is(':checked');
        });
        form.append(curatedInput);

        // type input
        var typeInput = new SelectForm('Model type', modelTypeDic);
        typeInput.input.val(form.metadata.type);
        typeInput.input.on('input', function() {
            form.metadata.type = typeInput.input.val();
        });
        form.append(typeInput);

        // subject input
        var subjectInput = new SelectForm('Model subject', modelClassDic);
        subjectInput.input.val(form.metadata.subject);
        subjectInput.input.on('input', function() {
            form.metadata.subject = subjectInput.input.val();
        });
        form.append(subjectInput);

        // food process input
        var foodProcessInput = new InputForm('text', 'Food process');
        foodProcessInput.input.val(form.metadata.foodProcess);
        foodProcessInput.input.on('input', function() {
            form.metadata.foodProcess = foodProcessInput.input.val();
        });
        form.append(foodProcessInput);

        // has data input
        var hasDataInput = new InputForm('checkbox', 'Has data?');
        hasDataInput.input.val(form.metadata.hasData);
        hasDataInput.input.change(function() {
            form.metadata.hasData = hasDataInput.input.is(':checked');
        });
        form.append(hasDataInput);

        return form;
    }


    var editor = {
        version: '0.0.1'
    };
    editor.name = 'FSK Metadata Editor';

    var _value; // Raw FskMetadataEditorViewValue


    // UI elements
    var _form;
    var _table;

    editor.init = function(representation, value) {
        _value = value;
        _form = new Form(_value.metadata);
        _table = new ParametersTable(_value.metadata.dependentVariables, _value.metadata.independentVariables);

        checkVariables();
        create_body();
    };


    editor.getComponentValue = function() {
        alert(JSON.stringify(_form.metadata));
        _value.metadata = _form.metadata;

        _value.metadata.dependentVariables = [];
        _value.metadata.independentVariables = [];
        for (var i = 0; i < _table.variableRows.length; i++) {
            var row = _table.variableRows[i];
            var variable = {
                'name': row.nameInput.val(),
                'unit': row.unitInput.val(),
                'type': row.typeSelect.val(),
                'value': row.valueInput.val(),
                'min': row.minInput.val(),
                'max': row.maxInput.val(),
            };
            if (row.isDependentInput.is(':checked')) {
                _value.metadata.dependentVariables.push(variable);
            }
            else {
                _value.metadata.independentVariables.push(variable);
            }
        }

        return _value;
    };

    return editor;

    // --- utility functions ---
    function create_body() {
        document.createElement("body");
        $('body').html('<div class="container"></div>');
        $('.container').append(_form); // Add form
        $('.container').append(_table); // Add parameters table
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
