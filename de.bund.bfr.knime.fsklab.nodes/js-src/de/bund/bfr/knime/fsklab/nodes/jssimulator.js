simulator = function() {

	var view = { version: "0.0.1" };
	view.name = "JavaScript Simulation Configurator"

	var _rep;
	var _val;

	// Regexp to match SId from SBML
	var _idRegexp = /^[A-Za-z_][A-Za-z_0-9]*/;

	// Index of selected simulation. Initially 0 (default simulation).
	var _currentSimulation = 0;

	view.init = function(representation, value) {
		_rep = representation;
		_val = value;

		create_body();
	};

	view.getComponentValue = function() {
		return _val;
	};

	return view;

	function create_body() {

		function create_form(parameterIndex) {

			var parameter = _rep.parameters[parameterIndex];
			var value = _val.simulations[_currentSimulation].values[parameterIndex];

			var inputType;
			if (parameter.dataType === "Integer" || parameter.dataType === "Double" ||
				parameter.dataType === "Number") {
				inputType = "number";
			} else {
				inputType = "text";
			}

			var input = $('<input type="' + inputType + '" class="form-control" value="">');
			input.val(value);

			
			input.focusout(function() {
				_val.simulations[_currentSimulation].values[parameterIndex] = $(this).val();
			  })
			var form = $('<div class="form-group">' +
				'  <label class="col-sm-3 control-label">' + parameter.id + '</label>' +
				'  <div class="col-sm-6 xxx"></div>' +
				'  <div class="col-sm-3"><label>' + parameter.unit + '</label></div>' +
				'</div>');
			$('.xxx', form).append(input);
			form.input = input;

			return form;
		}

		document.createElement('body');

		// Create simulations and parameters divs
		$('body').html(
			'<div class="row">' +
			'  <div class="col-md-4 simulationsDiv">'+
			'    <h2>Simulations</h2>' +
			'    <div id="simulationNamesDiv"></div>' +
			'    <form class="form-horizontal">' +
			'      <div id="buttonsDiv" class="form-group">' +
			// The remove button is disabled initially for the default simulation
			'        <button type="button" class="btn btn-default btn-warning" disabled>Remove</button>' +
			'        <input id="nameInput" type="text" class="btn btn-default" placeholder="Enter new simulation">' +
			// The add button is disabled initially (#nameInput is empty)
			'        <button type="button" class="btn btn-default btn-success" disabled>Add</button>' +
			'      </div>' +
			'    </form>' +
			'  </div>' +
			'  <div class="col-md-8 parametersDiv">' +
			'    <h2>defaultSimulation</h2>' +
			'    <form class="form-horizontal"></form>' +
			'  </div>' +
			'</div>');

		// Add simulation names to simulationsDiv
		for (var i = 0; i < _val.simulations.length; i++) {
			var simulationName = _val.simulations[i].name;
			var button = createSimulationButton(simulationName);
			$('#simulationNamesDiv').append(button);
		}

		// Remove simulation event
		$('.btn-warning').click(function() {
			var index = $('#simulationNamesDiv button.btn-primary').index();
			_val.simulations.splice(index, 1);  // Remove selected simulation
			$('.btn-primary').remove();
		})

		// Enables the add button for valid simulation names
		$('#nameInput').on('input', function() {

			var name = $(this).val();

			if (!name) {
				$('.btn-success').prop('disabled', true);
			} else if (isSimulationContained(name)) {
				$('.btn-success').prop('disabled', true);
			}
			// If simulation name does not match the SId format then disable the add button
			else if (!_idRegexp.test(name)) {
				$('.btn-success').prop('disabled', true);
			} else {
				$('.btn-success').prop('disabled', false);
			}
		});

		// Add button event
		$('.btn-success').click(function() {
			var simulationName = $('#nameInput').val();
			var button = createSimulationButton(simulationName);
			$('#simulationNamesDiv').append(button);

			// Wipe out the name entry
			$('#nameInput').val('');

			// Create and save new simulation (the values are taken from the default simulation)
			valuesArray = [];
			$.each(_val.simulations[0].values,function(index,value){
				valuesArray.push(value);
			})
			var newSimulation = {'name': simulationName, 'values': valuesArray }
			_val.simulations.push(newSimulation);
		});


		// Marks first simulation simulation as selected
		var firstSimulation = $('#simulationNamesDiv button:first');
		firstSimulation.removeClass('btn-default');
		firstSimulation.addClass('btn-primary');

		var parameterForm = $('.parametersDiv form');
		for (var i = 0; i < _rep.parameters.length; i++) {
			var form = create_form(i);

			// disables the input for the default simulation
			form.input.prop('disabled', true);
			parameterForm.append(form);
		}
	}

	function createSimulationButton(simulationName) {

		var button = $('<button type="button" class="btn btn-default btn-block">'
			+ simulationName + '</button>');

		button.click(function() {
			var button = $(this);

			var simulationName = button.text();

			// Find index of selected simulation
			for (var i = 0; i < _val.simulations.length; i++) {
				if (simulationName === _val.simulations[i].name) {
					_currentSimulation = i;
					break;
				}
			}
			_val.selectedSimulationIndex = _currentSimulation;
			var newDisabledValue = simulationName === 'defaultSimulation';

			// Disables the remove button for the default simulation which cannot be removed
			$('.btn-warning').prop('disabled', newDisabledValue);

			// Mark selected simulation as selected and unselect previously selected
			$('.btn-primary').removeClass('btn-primary');

			button.removeClass('btn-default');
			button.addClass('btn-primary');

			// Update title
			$('.parametersDiv h2').text(simulationName);
			
			// Update values for the selected simulation
			$('.parametersDiv input').each(function(index) {
				$(this).prop('disabled', newDisabledValue);
				$(this).val(_val.simulations[_currentSimulation].values[index]);
				
			});
		});

		return button;
	}

	function isSimulationContained(simulationName) {
		for (var i = 0; i < _val.simulations.length; i++) {
			if (simulationName === _val.simulations[i].name) {
				return true;
			}
		}
		return false;
	}
}();
