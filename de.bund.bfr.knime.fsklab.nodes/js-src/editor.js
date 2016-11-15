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

        var form = 
            '<form class="form-horizontal">' +

            // Model name form
            '  <div class="form-group">' +
            '    <label for="modelName" class="col-sm-3 control-label">Model name:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" id="modelNameInput" class="form-control no-border" value="">' + 
            '    </div>' +
            '  </div>' +

            // Model id form
            '  <div class="form-group">' +
            '    <label for="modelId" class="col-sm-3 control-label">Model id:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" id="modelIdInput" class="form-control no-border" value="">' +
            '    </div>' +
            '  </div>' +

            // Model link form
            '  <div class="form-group">' +
            '    <label for="modelLinkInput" class="col-sm-3 control-label">Model link:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="url" class="form-control no-border" id="modelLinkInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Organism form
            '  <div class="form-group">' +
            '    <label for="organism" class="col-sm-3 control-label">Organism:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="organismInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Organism details form
            '  <div class="form-group">' +
            '    <label for="organismDetails" class="col-sm-3 control-label">Organism details:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="organismDetailsInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Matrix form
            '  <div class="form-group">' +
            '    <label for="matrix" class="col-sm-3 control-label">Matrix:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="matrixInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Matrix details form
            '  <div class="form-group">' +
            '    <label for="matrixDetails" class="col-sm-3 control-label">Matrix details:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="matrixDetailsInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Creator form
            '  <div class="form-group">' +
            '    <label for="creator" class="col-sm-3 control-label">Creator:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="creatorInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Family name form
            '  <div class="form-group">' +
            '    <label for="familyName" class="col-sm-3 control-label">Family name:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="familyNameInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Contact form
            '  <div class="form-group">' +
            '    <label for="contact" class="col-sm-3 control-label">Contact:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="contactInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Software form
            '  <div class="form-group">' +
            '    <label for="software" class="col-sm-3 control-label">Software:</label>' +
            '    <div class="col-sm-9">' +
            '      <select class="form-control no-border" id="softwareInput" >' +
            '        <option value="R">R</option>' +
            '        <option value="Matlab">Matlab</option>' +
            '      </select>' +
            '    </div>' +
            '  </div>' +

            // Reference description form
            '  <div class="form-group">' +
            '    <label for="referenceDescription" class="col-sm-3 control-label">Reference description:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="referenceDescriptionInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Reference description link form
            '  <div class="form-group">' +
            '    <label for="referenceDescriptionLink" class="col-sm-3 control-label">Reference description link:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type"url" class="form-control no-border" id="referenceDescriptionLinkInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Created date form
            '  <div class="form-group">' +
            '    <label for="createdDate" class="col-sm-3 control-label">Created date:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="createdDateInput" value="">' + 
            '    </div>' +
            '  </div>' +

            // Modified date form
            '  <div class="form-group">' +
            '    <label for="modifiedDate" class="col-sm-3 control-label">Modified date:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="modifiedDateInput" value="">' + 
            '    </div>' +
            '  </div>' +

            // Rights form
            '  <div class="form-group">' +
            '    <label for="rights" class="col-sm-3 control-label">Rights:</label>' +
            '      <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="rightsInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Notes form
            '  <div class="form-group">' +
            '    <label for="notes" class="col-sm-3 control-label">Notes:</label>' +
            '    <div class="col-sm-9">' +
            '      <textarea id="notesInput" class="form-control no-border" rows="3"></textArea>' +
            '    </div>' +
            '  </div>' +

            // Curated form
            '  <div class="form-group">' +
            '    <label for="curated" class="col-sm-3 control-label">Curated:</label>' +
            '    <div class="col-sm-9">' +
            '      <input id="curatedInput" type="checkbox">' +
            '    </div>' +
            '  </div>' +

            // Model type form
            '  <div class="form-group">' +
            '    <label for="modelType" class="col-sm-3 control-label">Model type:</label>' +
            '    <div class="col-sm-9">' +
            '      <select class="form-control no-border" id="typeInput">' +
            '        <option value="EXPERIMENTAL_DATA">Experimental data</option>' +
            '        <option value="PRIMARY_MODEL_WDATA">Primary model with data</option>' +
            '        <option value="PRIMARY_MODEL_WODATA">Primary model without data</option>' +
            '        <option value="TWO_STEP_SECONDARY_MODEL">Two step secondary model</option>' +
            '        <option value="ONE_STEP_SECONDARY_MODEL">One step secondary model</option>' +
            '        <option value="MANUAL_SECONDARY_MODEL">Manual secondary model</option>' +
            '        <option value="TWO_STEP_TERTIARY_MODEL">Two step tertiary model</option>' +
            '        <option value="ONE_STEP_TERTIARY_MODEL">One step tertiary model</option>' +
            '        <option value="MANUAL_TERTIARY_MODEL">Manual tertiary model</option>' +
            '      </select>' +
            '    </div>' +
            '  </div>' +

            // Model subject form
            '  <div class="form-group">' +
            '    <label for="modelSubject" class="col-sm-3 control-label">Model subject:</label>' +
            '    <div class="col-sm-9">' +
            '      <select class="form-control no-border" id="subjectInput">' +
            '        <option value="UNKNOWN">unknown</option>' +
            '        <option value="GROWTH">growth</option>' +
            '        <option value="INACTIVATION">inactivation</option>' +
            '        <option value="SURVIVAL">survival</option>' +
            '        <option value="GROWTH_INACTIVATION">growth/inactivation</option>' +
            '        <option value="INACTIVATION_SURVIVAL">inactivation/survival</option>' +
            '        <option value="GROWTH_SURVIVAL">growth/survival</option>' +
            '        <option value="GROWTH_INACTIVATION_SURVIVAL">growth/inactivation/survival</option>' +
            '        <option value="T">T</option>' +
            '        <option value="PH">pH</option>' +
            '        <option value="AW">aw</option>' +
            '        <option value="T_PH">T/pH</option>' +
            '        <option value="T_AW">T/aw</option>' +
            '        <option value="PH_AW">pH/aw</option>' +
            '        <option value="T_PH_AW">T/pH/aw</option>' +
            '      </select>' +
            '    </div>' +
            '  </div>' +

            // Food process form
            '  <div class="form-group">' +
            '    <label for="foodProcess" class="col-sm-3 control-label">Food process:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="foodProcessInput" value="">' +
            '    </div>' +
            '  </div>' +

            // Has data form
            '  <div class="form-group">' +
            '    <label for="hasData" class="col-sm-3 control-label">Has data?:</label>' +
            '    <div class="col-sm-9">' +
            '      <input id="hasDataInput" type="checkbox">' +
            '    </div>' +
            '  </div>' +

            '</form>';

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

        $('table tr:eq(1) td:eq(0)').text(_value.metadata.dependentVariable.name);
        $('table tr:eq(1) td:eq(1)').text(_value.metadata.dependentVariable.unit);
        $('table tr:eq(1) td:eq(2)').text(_value.metadata.dependentVariable.type);
        $('table tr:eq(1) td:eq(3) input').val(_value.metadata.dependentVariable.value);
        $('table tr:eq(1) td:eq(4) input').val(_value.metadata.dependentVariable.min);
        $('table tr:eq(1) td:eq(5) input').val(_value.metadata.dependentVariable.max);

        for (var i = 0; i < _value.metadata.independentVariables.length; i++) {
            var variable = _value.metadata.independentVariables[i];
            // Table row. Stars from 2 where 1 is the dependent variable row.
            var tableRow = i + 2;
            $('table tr:eq(' + tableRow + ') td:eq(0)').text(variable.name);
            $('table tr:eq(' + tableRow + ') td:eq(1)').text(variable.unit);
            $('table tr:eq(' + tableRow + ') td:eq(2)').text(variable.type);
            $('table tr:eq(' + tableRow + ') td:eq(3) input').val(variable.value);
            $('table tr:eq(' + tableRow + ') td:eq(4) input').val(variable.min);
            $('table tr:eq(' + tableRow + ') td:eq(5) input').val(variable.max);
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
        $('table tr:eq(1) td:eq(3) input').on('input', function() {
            _value.metadata.dependentVariable.value = $(this).val();
        });
        $('table tr:eq(1) td:eq(4) input').on('input', function() {
            var newVal = Number($(this).val());
            var max = Number(_value.metadata.dependentVariable.max);

            if (newVal < max) {
                _value.metadata.dependentVariable.min = newVal;
                markValidTd($(this).parent());
            } else {
                markInvalidTd($(this).parent());
            }
        });
        $('table tr:eq(1) td:eq(5) input').on('input', function() {
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
                var min = Number(_value.metadata.independentVariables[i].min);
                var max = Number(_value.metadata.independentVariables[i].max);
                if (min <= newVal && newVal <= max) {
                    _value.metadata.independentVariables[i].value = newVal;
                    markValidTd($(this).parent());
                } else {
                    markInvalidTd($(this).parent());
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
                    markInvalidId($(this).parent());
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
}();
