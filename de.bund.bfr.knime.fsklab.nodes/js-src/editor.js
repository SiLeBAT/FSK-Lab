metadata_editor = function () {

    var editor = {
	   version: "0.0.1"
    };
    editor.name = "FSK Metadata Editor";

    var _value;  // Raw FskMetadataEditorViewValue

    editor.init = function (representation, value)
    {
        _value = value;
        _data = value.metadata;
        checkVariables();
        create_body ();
    };

    editor.getComponentValue = function ()
    {
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

        var form = 
            '<form class="form-horizontal">' +
            createInputForm("Model name", "modelNameInput", "text") +  // Model name form
            createInputForm("Model id", "modelIdInput", "text") + // Model id form
            createInputForm("Model link", "modelLinkInput", "text") +  // Model link form
            createInputForm("Organism", "organismInput", "text") +  // Organism form
            createInputForm("Organism details", "organismDetailsInput", "text") +  // Organism details form
            createInputForm("Matrix", "matrixInput", "text") +  // Matrix form
            createInputForm("Matrix details", "matrixDetailsInput", "text") +  // Matrix details form
            createInputForm("Creator", "creatorInput", "text") +  // Creator form
            createInputForm("Family name", "familyNameInput", "text") +  // Family name form
            createInputForm("Contact", "contactInput", "text") +  // Contact form
            createSelectForm("Software", "softwareInput", softwareDic) +  // Software form
            createInputForm("Reference description", "referenceDescriptionInput", "text") +  // Reference description
            createInputForm("Reference description link", "referenceDescriptionLinkInput", "url") +  // Reference description link form
            createInputForm("Created date", "createdDateInput", "text") +  // Created date form
            createInputForm("Modified date", "modifiedDateInput", "text") +  // Modified date form
            createInputForm("Rights", "rightsInput", "text") +  // Rights form
            createTextArea("Notes:", "notesInput") +  // Notes form 
            createInputForm("Curated", "curatedInput", "checkbox") +  // Curated form
            createSelectForm("Model type", "typeInput", modelTypeDic) + // Model type form
            createSelectForm("Model subject", "subjectInput", modelClassDic) + // Model subject form
            createInputForm("Food process", "foodProcessInput", "text") +  // Food process form
            createInputForm("Has data?:", "hasDataInput", "checkbox") +  // Has data form
            '</form>';

        /**
         * Create a form-group.
         * - label: Text label
         * - id: Input id
         * - type: Input type {text, checkbox}
         */
        function createInputForm(label, id, type) {
            var formStr = '<div class="form-group">';
            formStr += '<label for="' + id + '" class="col-sm-3 control-label">' + label + '</label>';
            formStr += '<div class="col-sm-9">';
            if (type === 'text') {
                formStr += '<input type="text" class="form-control no-border" id="' + id + '" value="">';
            } else if (type === 'url') {
                formStr += '<input type="url" class="form-control no-border" id="' + id + '" value="">';
            } else if (type === 'checkbox') {
                formStr += '<input id="' + id + '" type="checkbox">';
            }
            formStr += '</div>';
            formStr += '</div>';

            return formStr;
        }

        function createTextArea(label, id) {
            return '<div class="form-group">' +
                '  <label for="' + id + '" class="col-sm-3 control-label">' + label + '</label>' +
                '  <div class="col-sm-9">' +
                '    <textarea id="' + id + '" class="form-control no-border" rows="3"></textArea>' +
                '  </div>' +
                '</div>';
        }

        /**
         * Creates a form-group with a select input. Entries is a dictionary
         * where the keys are the labels and the values are the option values. 
         */
        function createSelectForm(label, id, entries) {
            var formStr = '<div class="form-group">';
            formStr += '<label for="' + id + '" class="col-sm-3 control-label">' + label + '</label>';
            formStr += '<div class="col-sm-9">';
            formStr += '<select class="form-control no-border" id="' + id + '">';
            for (var key in entries) {
                formStr += '<option value="' + key + '">' + entries[key] + '</option>';
            }
            formStr += '</select></div></div>';

            return formStr;
        }

        document.createElement("body");
        $("body").html('<div class="container">' + form + varTable + '</div');

        // Create date pickers. Set date formats and save when dates change.
        $("#createdDateInput").datepicker({
            dateFormat: "mm.dd.yy",
            onSelect: function(dateText) { _value.metadata.createdDate = dateText; }
        });
        $("#modifiedDateInput").datepicker({
            dateFormat: "mm.dd.yy",
            onSelect: function(dateText) { _value.metadata.modifiedDate = dateText; }
        });

        loadData();
        saveData();
    }

    function loadData () {
        $('#modelNameInput').val(nullToEmpty(_value.metadata.modelName));
        $('#modelIdInput').val(nullToEmpty(_value.metadata.modelId));
        $('#modelLinkInput').val(nullToEmpty(_value.metadata.modelLink));

        $('#organismInput').val(nullToEmpty(_value.metadata.organism));
        $('#organismDetailsInput').val(nullToEmpty(_value.metadata.organismDetails));

        $('#matrixInput').val(nullToEmpty(_value.metadata.matrix));
        $('#matrixDetailsInput').val(nullToEmpty(_value.metadata.matrixDetails));

        $('#creatorInput').val(nullToEmpty(_value.metadata.creator));
        $('#familyNameInput').val(nullToEmpty(_value.metadata.familyName));
        $('#contactInput').val(nullToEmpty(_value.metadata.contact));
        if (_value.metadata.software) {
            $('#softwareInput option[value="' + _value.metadata.software + '"]').prop('selected', true);
        }

        $('#referenceDescriptionInput').val(nullToEmpty(_value.metadata.referenceDescription));
        $('#referenceDescriptionLinkInput').val(nullToEmpty(_value.metadata.referenceDescriptionLink));

        $('#createdDateInput').val(nullToEmpty(_value.metadata.createdDate));
        $('#modifiedDateInput').val(nullToEmpty(_value.metadata.modifiedDate));

        $('#rightsInput').val(nullToEmpty(_value.metadata.rights));
        $('#notesInput').val(nullToEmpty(_value.metadata.notes));
        $('#curatedInput').prop('checked', _value.metadata.curated);

        if (_value.metadata.type) {
            $('#typeInput option[value="' + _value.metadata.type + '"]').prop('selected', true);
        }
        if (_value.metadata.subject) {
            $('#subjectInput option[value="' + _value.metadata.subject + '"]').prop('selected', true);
        }
        $('#foodProcessInput').val(nullToEmpty(_value.metadata.foodProcess));
        $('#hasDataInput').prop('checked', _value.metadata.hasData);

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
        $("#modelNameInput").on('input', function() { _value.metadata.modelName = $(this).val(); });
        $("#modelIdInput").on('input', function() { _value.metadata.modelId = $(this).val(); });
        $("#modelLinkInput").on('input', function() { _value.metadata.modelLink = $(this).val(); });

        $("#organismInput").on('input', function() { _value.metadata.organism = $(this).val(); });
        $("#organismDetailsInput").change(function() { _value.metadata.organismDetails = $(this).val(); });

        $("#matrixInput").on('input', function() { _value.metadata.matrix = $(this).val(); });
        $("#matrixDetailsInput").on('input', function() { _value.metadata.matrixDetails = $(this).val(); });

        $("#creatorInput").on('input', function() { _value.metadata.creator = $(this).val(); });
        $("#familyNameInput").on('input', function() { _value.metadata.familyName = $(this).val(); });
        $("#contactInput").on('input', function() { _value.metadata.contact = $(this).val(); });
        $("#softwareInput").change(function() { _value.metadata.software = $(this).val(); });

        $("#referenceDescriptionInput").on('input', function() { _value.metadata.referenceDescription = $(this).val(); });
        $("#referenceDescriptionLinkInput").on('input', function() { _value.metadata.referenceDescriptionLink = $(this).val(); });

        $("#createdDateInput").on('input', function() { _value.metadata.createdDate = $(this).val(); });
        $("#modifiedDateInput").on('input', function() { _value.metadata.modifiedDate = $(this).val(); });

        $("#rightsInput").on('input', function() { _value.metadata.rights = $(this).val(); });
        $("#notesInput").on('input', function() { _value.metadata.notes = $(this).val(); });
        $("#curatedInput").change(function() { _value.metadata.curated  = $(this).is(':checked'); });

        $("#typeInput").change(function() { _value.metadata.type = $(this).val(); });
        $('#subjectInput').change(function() { _value.metadata.subject = $(this).val(); });
        $("#foodProcessInput").on('input', function() { _value.metadata.foodProcess = $(this).val(); });
        $("#hasDataInput").change(function() { _value.metadata.hasData = $(this).is(':checked'); });

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
                alert(newVal);
                if (_data.independentVariables[i].type === 'integer'
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

    function nullToEmpty(stringVar) {
        return stringVar === null ? "" : stringVar;
    }

    /** Check variables. Integer variables will truncate decimals. */
    function checkVariables() {
        for (var i = 0; i < _data.independentVariables.length; i++) {
            var variable = _data.independentVariables[i];
            if (variable.type === 'integer') {
                variable.value = Math.floor(variable.value);
            }
        }
    }
}();
