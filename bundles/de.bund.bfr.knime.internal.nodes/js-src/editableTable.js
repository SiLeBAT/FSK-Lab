editable_table = function() {

	var _representation;
	var _value;
    var _knimeTable;

	// Object with GUI functions
	var table = {};

	table.init = function(representation, value) {

		if (!value.table) {
			$('body').append("Error: No data available");
			return;
		}
		_representation = representation;
		_value = value;

        _knimeTable = new kt();
        _knimeTable.setDataTable(_value.table);

		drawTable();
	};

	table.getComponentValue = function() {
		return _value;
	};

	drawTable = function() {

		var body = $('body');
		$('body').html('<div class="container"></div>');
		$('.container').append('<table class="table table-condensed table-bordered">' +
			'<thead><tr class="bg-primary"></tr></thead>' +
			'<tbody></tbody></table>');

		var thead = $('.table thead');
		for (var i = 0; i < _knimeTable.getColumnNames().length; i++) {
			$('tr', thead).append('<th>' + _knimeTable.getColumnNames()[i] + '</th>');
		}

		var tbody = $(".table tbody");
        var rows = _knimeTable.getRows();

        // Apply banded rows styling if table has more than 10 rows
        var bandedRows = (rows.length > 10) ? 'class="bg-info"' : '';

        for (var i = 0; i < rows.length; i++) {
        	// Only apply banded rows styling to odd rows
			tbody.append('<tr ' + (i % 2 === 0 ? bandedRows: '') + '></tr>');
            var tr = $('tbody tr:eq(' + i + ')');

            var dataFields = rows[i].data;

            for (var j = 0; j < dataFields.length; j++) {
                if (_knimeTable.getColumnTypes()[j] == 'NUMBER') {
                    tr.append('<td><input type="number"' + (i % 2 === 0 ? bandedRows: '') + 'value="' + dataFields[j] + '"></input></td>');
                    $('input', tr).spinner({
                    	change: function (event, ui) {
                    		var td = $(this).parent().parent();
  							var col = td.index();
							var row = td.parent().index();
                    		_value.table.rows[row].data[col] = $(this).spinner('value');
                    	}
                	});
                } else {
                    tr.append('<td>' + dataFields[j] + '</td>');
                }
            }
		}
	}

	return table;
}();
