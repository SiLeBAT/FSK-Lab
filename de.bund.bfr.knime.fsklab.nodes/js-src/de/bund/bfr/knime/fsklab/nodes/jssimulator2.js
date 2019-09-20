simulator = function() {
	
	var view = { version: "0.0.1" };
	view.name = "JavaScript Simulation Configurator"

	var _rep;
	var _val;

	// Regexp to match SId from SBML
	var _idRegexp = /^[A-Za-z_^s]\w*$/;

	// Index of selected simulation. Initially 0 (default simulation).
	var _currentSimulation = 0;

	view.init = function(representation, value) {
		_rep = representation;
    _val = value;

		create_body2();
    initUI();
    updateSimulationName(0);  // Show initially defaultSimulation (0)
    updateParameterValues(0); // Show initially the values for simulation 0.
	};

	view.getComponentValue = function() {
		return _val;
  };
  
	return view;

  /**
   * Create UI.
   * 
   * - Create top panel with simulation select (#simulationSelect) and simulation buttons (remove, add and save).
   * - Create parameter panels with parameter names and values.
   */
	function create_body2() {
		let body = `
    <div class="container">
      
      <form class="form-horizontal">
        <div class="form-group form-group-lg">
          <label class="col-md-3 control-label" for="simulationSelect">Simulation</label>
          <div class="col-md-5">
            <select class="form-control" id ="simulationSelect"></select>
          </div>
          <button id="removeButton" type="button" class="btn btn-lg btn-danger col-md-offset-1 col-xs-offset-1" title="Remove current simulation">
            <i class="glyphicon glyphicon-trash"></i>
          </button>
          <button id="addButton" type="button" class="btn btn-lg btn-primary" title="Add new simulation">
            <i class="glyphicon glyphicon-plus"></i>
          </button>
          <button id="saveButton" type="button" class="btn btn-lg btn-success" title="Save changes">
            <i class="glyphicon glyphicon-floppy-disk"></i>
          </button>
        </div>
      </form>

      <form class="form-horizontal">
        <div class="form-group form-group-lg">
          <label class="col-md-3 control-label" for="simulationName">Simulation name</label>
          <div class="col-md-5">
            <input id="simulationName" type="text" class="form-control">
          </div>
        </div>
      </form>

      <form class="form-horizontal" id="parameter-form"></form>
		</div> <!-- container -->
		`;

		document.createElement('body');
		$('body').html(body);

		$('#removeButton').click(function() {

      let simulationSelect = $('#simulationSelect');
      let selectedSimulationIndex = simulationSelect.prop('selectedIndex');

      if (selectedSimulationIndex != 0) {
        // Remove simulation in select
        $(`#simulationSelect option:eq(${selectedSimulationIndex})`).remove();

        // Remove simulation data
        _val.simulations.splice(selectedSimulationIndex, 1);

        // Select the default simulation (0)
        simulationSelect.val('defaultSimulation');

        // Update UI
        updateSimulationName(0);
        updateParameterValues(0);
      }
		});

		$('#addButton').click(function() {
      addSimulation();
		});

		$('#saveButton').click(function() { submitSimulation(); });
    
    $('#simulationSelect').change(function() {
      let selectedIndex = document.getElementById("simulationSelect").selectedIndex;
      updateSimulationName(selectedIndex);
      updateParameterValues(selectedIndex);
    });
	}

  /**
   * Initialize UI.
   * 
   * - Enter simulation names into the simulation select (#simulationSelect).
   * - Create parameters and add them (only names). Parameter values are added later for the current simulation.
   */
  function initUI() {
    
    // Include simulations
    let simulationSelect = $('#simulationSelect');
    _val.simulations.forEach(simulation => {
      simulationSelect.append(`<option>${simulation.name}</option>`);                  
    });

    // Add parameters
    let parameterForm = $('#parameter-form');
    _rep.parameters.forEach(parameter => {

      let inputType;
      if (parameter.dataType === "Integer" || parameter.dataType === "Double" || parameter.dataType === "Number") {
        inputType = "number";
      } else {
        inputType = "text";
      }

      let metadataId = parameter.id + "-metadata";

      let parameterHtml = `<div class="form-group form-group-sm">
        <label class="col-sm-3 control-label">${parameter.id}</label>
        <div class="col-sm-8">
          <input type="${inputType}" class="form-control parameter-input" value=""></input>
          <div class="collapse" id="${metadataId}" >
            <div class="alert alert-info card card-body">
              <ul>
                ${parameter.id ? `<li><b>ID</b>: ${parameter.id}</li>` : ''}
                ${parameter.name ? `<li><b>Name</b>: ${parameter.name}</li>` : ''}
                ${parameter.description ? `<li><b>Description</b>: ${parameter.description.substring(0,50)}</li>` : ''}
                ${parameter.unit ? `<li><b>Unit</b>: ${parameter.unit}</li>` : ''}
                ${parameter.unitCategory ? `<li><b>Unit category</b>: ${parameter.unitCategory}</li>` : ''}
                ${parameter.dataType ? `<li><b>Data type</b>: ${parameter.dataType}</li>` : ''}
                ${parameter.source ? `<li><b>Source</b>: ${parameter.source}</li>` : ''}
                ${parameter.subject ? `<li><b>Subject</b>: ${parameter.subject}</li>` : ''}
                ${parameter.distribution ? `<li><b>Distribution</b>: ${parameter.distribution}</li>` : ''}
                ${parameter.reference ? `<li><b>Reference</b>: ${parameter.reference}</li>` : ''}
                ${parameter.variabilitySubject ? `<li><b>Variability subject</b>: ${parameter.variabilitySubject}</li>` : ''}
                ${parameter.minValue ? `<li><b>Min value</b>: ${parameter.minValue}</li>` : ''}
                ${parameter.maxValue ? `<li><b>Max value</b>: ${parameter.maxValue}</li>` : ''}
                ${parameter.error ? `<li><b>Error</b>: ${parameter.error}</li>` : ''}
              </ul>
            </div>
          </div>
        </div>       
        <button class="btn" type="button" data-toggle="collapse" data-target="#${metadataId}" aria-expanded="false" aria-controls="${metadataId}" title="Show information">
          <i class="glyphicon glyphicon-info-sign"></i>
        </button>
      </div>`;
      parameterForm.append(parameterHtml);

      // Hide other parameter metadata panel when one is clicked.
      $('#' + metadataId).on('show.bs.collapse', function() {
        $('.collapse.in').collapse('hide');
      });
    });
  }

  /**
   * Update the simulation name input (#simulationName) when another simulation is selected.
   * 
   * - Disable the input for the default simulation.
   */
  function updateSimulationName(simulationIndex) {
    let simulationName = $('#simulationName');
    simulationName.val(_val.simulations[simulationIndex].name);
    simulationName.prop('disabled', simulationIndex == 0);    
  }

  /**
   * Update parameter values for the given simulation.
   * 
   * - All the parameter values are retrieved and displayed in the UI.
   * - Parameter inputs are disabled for the default simulation that is unmodifiable.
   */
  function updateParameterValues(simulationIndex) {

    // Disable parameter inputs for the default simulation.
    let isDisabled = simulationIndex == 0;

    $('.parameter-input').each(function(parameterIndex) {
      let parameterValue = _val.simulations[simulationIndex].values[parameterIndex];
      $(this).val(parameterValue);
      $(this).prop('disabled', isDisabled);
    });
  }

  /**
   * Prepare the UI to enter a new simulation.
   * 
   * - Clear simulation select (#simulationSelect).
   * - Clear and enable the simulation name input (#simulationName).
   * - Clear every parameter input (.parameter-input) and assign the default value.
   */
  function addSimulation() {
    $('#simulationSelect').val('');

    let simulationName = $('#simulationName');
    simulationName.val('');
    simulationName.prop('disabled', false);

    // Take the values from the default simulation
    let defaultSimulationValues = _val.simulations[0].values;
    $('.parameter-input').each(function(parameterIndex) {
      $(this).val(defaultSimulationValues[parameterIndex]);
      $(this).prop('disabled', false);
    });
  }

  /**
   * Validate simulation and submit if it is error free.
   */
  function submitSimulation() {

    /**
     * Validate the entered name for a new simulation.
     * - Check that the name is not used already
     * - Check that the name is not empty.
     * - Check that the name is a valid SId.
     */
    function validateSimulationName(name) {

      // Check that it is not duplicated
      let isDuplicated = false;
      for (let i = 0; i < _val.simulations.length && !isDuplicated; ++i) {
        if (name === _val.simulations[i].name) {
          isDuplicated = true;
        }
      }

      return !isDuplicated && name && _idRegexp.test(name);
    }

    /**
     * Validate parameter value.
     * 
     * So far it only checks that value is not empty.
     * TODO: Check data type and range (min and max value). 
     */
    function validateParameter(value) {
      return value ? true: false;
    }

    let errorCount = 0;
    
    // Validate simulation name
    let simulationNameInput = $('#simulationName');
    let newSimulationName = simulationNameInput.val();
    
    let simulationNameValidation;
    if (validateSimulationName(newSimulationName)) {
      simulationNameValidation = "has-success";
    } else {
      simulationNameValidation = "has-error";
      errorCount++;
    }
    let formDiv = simulationNameInput.parent().parent(); // Get the form-group div
    formDiv.removeClass('has-success has-error'); // error previous validation classes
    formDiv.addClass(simulationNameValidation);

    // Validate parameter inputs
    let parameterValues = [];
    $('.parameter-input').each(function(parameterIndex) {
      let value = $(this).val();
      parameterValues.push(value);  // cache value

      let parameterValidation;
      if (validateParameter(value)) {
        parameterValidation = "has-success";
      } else {
        parameterValidation = "has-error";
        errorCount++;
      }

      let formDiv = $(this).parent().parent(); // Get the form-group div
      formDiv.removeClass('has-success has-error'); // error previous validation classes
      formDiv.addClass(parameterValidation);
    });

    if (errorCount == 0) {
      // Save simulation in _val
      let newSimulation = {'name': newSimulationName, 'values': parameterValues};
      _val.simulations.push(newSimulation);

      // Add simulation name in simulationSelect
      $('#simulationSelect').append(`<option>${newSimulationName}</option>`);
      // and select it
      $('#simulationSelect option').last().prop('selected', true);
    }
  }
}();
