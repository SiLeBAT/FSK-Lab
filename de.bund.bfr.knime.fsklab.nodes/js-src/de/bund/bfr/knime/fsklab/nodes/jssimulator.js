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
            <select class="form-control" id ="simulationSelect">
              <!-- Add simulations to select -->
              ${_val.simulations.map(simulation => `<option>${simulation.name}</option>`)}
            </select>
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
            <span id="helpBlock-simulationName" class="help-block"></span>
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

    document.getElementById('addButton').onclick = addSimulation;
    document.getElementById('saveButton').onclick = submitSimulation;
    
    $('#simulationSelect').change(() => {
      let selectedIndex = document.getElementById("simulationSelect").selectedIndex;
      updateSimulationName(selectedIndex);
      updateParameterValues(selectedIndex);

      // Store the index of the selected simulation as selected in the view value
      _val.selectedSimulationIndex = selectedIndex;
    });
	}

  /**
   * Initialize UI.
   * 
   * - Create parameters and add them (only names). Parameter values are added later for the current simulation.
   */
  function initUI() {

    function createMetadataList(parameter) {

      const formatProperty = (name, value) => value ? `<li><b>${name}</b>: ${value}</li>` : '';

      return `<ul>
        ${formatProperty("ID", parameter.id)}
        ${formatProperty("Name", parameter.name)}
        ${parameter.description ? `<li><b>Description</b>: ${parameter.description.substring(0,50)}</li>` : ''}
        ${formatProperty("Unit", parameter.unit)}
        ${formatProperty("Unit category", parameter.unitCategory)}
        ${formatProperty("Data type", parameter.dataType)}
        ${formatProperty("Source", parameter.source)}
        ${formatProperty("Subject", parameter.subject)}
        ${formatProperty("Distribution", parameter.distribution)}
        ${formatProperty("Reference", parameter.reference)}
        ${formatProperty("Variability subject", parameter.variabilitySubject)}
        ${formatProperty("Min value", parameter.minValue)}
        ${formatProperty("Max value", parameter.maxValue)}
        ${formatProperty("Error", parameter.error)}
      </ul>`;
    }

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
      let readonly = (parameter.classification == "CONSTANT")? "readonly" : "";
      let parameterHtml = `<div class="form-group form-group-sm">
        <label class="col-sm-3 control-label">${parameter.id}</label>
        <div class="col-sm-8">
          <input type="${inputType}" class="form-control parameter-input" value="" ${readonly}></input>
          <span class="help-block"></span>
          <div class="collapse" id="${metadataId}" >
            <div class="alert alert-info card card-body">
              ${createMetadataList(parameter)}
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
    const simulationName = document.getElementById('simulationName');
    simulationName.value = _val.simulations[simulationIndex].name;
    simulationName.disabled = simulationIndex == 0;
  }

  /**
   * Update parameter values for the given simulation.
   * 
   * - All the parameter values are retrieved and displayed in the UI.
   * - Parameter inputs are disabled for the default simulation that is unmodifiable.
   */
  function updateParameterValues(simulationIndex) {

    // Disable parameter inputs for the default simulation.
    const isDisabled = simulationIndex == 0;

    const inputs = document.getElementsByClassName('parameter-input');
    Array.from(inputs).forEach((input, parameterIndex) => {
      input.value = _val.simulations[simulationIndex].values[parameterIndex];
      input.disabled = isDisabled;
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

    document.getElementById('simulationSelect').value = '';

    const simulationName = document.getElementById('simulationName');
    simulationName.value = '';
    simulationName.disabled = false;

    // Take the values from the default simulation
    const defaultSimulationValues = _val.simulations[0].values;
    const inputs = document.getElementsByClassName('parameter-input');
    Array.from(inputs).forEach((input, index) => {
      input.value = defaultSimulationValues[index];
      input.disabled = false;
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
     * 
     * @returns An error if found. If not null.
     */
    function validateSimulationName(name) {

      // Check that it is not duplicated
      for (let i = 0; i < _val.simulations.length; ++i) {
        if (name === _val.simulations[i].name) {
          return "Simulation name already exists"
        }
      }

      if (!_idRegexp.test(name)) return "Not a valid simulation name (SId)";

      return null;
    }

    /**
     * Validate parameter value.
     * 
     * Checks missing values and range (min and max values).
     */
    function validateParameter(value, metadata) {

      // Check missing value
      if (!value) return `Please provide a value for ${metadata.id}`;
      
      // Check range for integers and doubles
      if (metadata.dataType === "INTEGER" || metadata.dataType === "DOUBLE") {
        if (metadata.minValue && parseFloat(metadata.minValue) > value) {
          return `Invalid value. Value is lower than minimal value (${metadata.minValue})`;
        }
        if (metadata.maxValue && parseFloat(metadata.maxValue) < value) {
          return `Invalid value. Value is higher than maximal value (${metadata.maxValue})`;
        }
      }

      return null;
    }

    let errorCount = 0;
    
    // Validate simulation name
    const simulationNameInput = document.getElementById("simulationName");
    const nameValidationResult = validateSimulationName(simulationNameInput.value);
    const helpBlock = document.getElementById("helpBlock-simulationName");

    if (!nameValidationResult) {
      // Clear and hide the help block
      helpBlock.textContent = "";
      helpBlock.style.visibility = "hidden";
    } else {
      // Add error and show the help block
      helpBlock.textContent = nameValidationResult;
      helpBlock.style.visibility = "visible";

      errorCount++;
    }

    // Get the form-group div and set the validation status
    const formDiv = simulationNameInput.parentNode.parentNode;
    formDiv.classList.remove('has-success', 'has-error');
    formDiv.classList.add(!nameValidationResult ? 'has-success' : 'has-error');

    // Validate parameter inputs
    let parameterValues = [];

    const parameterInputs = document.getElementsByClassName('parameter-input');
    Array.from(parameterInputs).forEach(function(parameter, index) {

      parameterValues.push(parameter.value); // cache value

      const parameterHelp = parameter.parentNode.querySelector('.help-block');
      
      const validationResult = validateParameter(parameter.value, _rep.parameters[index]);
      if (!validationResult) {
        // Clear and hide the help block
        parameterHelp.textContent = "";
        parameterHelp.style.visibility = "hidden";
      } else {
        // Add error and show the help block
        parameterHelp.textContent = validationResult;
        parameterHelp.style.visibility = "visible";

        errorCount++;
      }

      // Get the form-group div and set the validation status
      const formDiv = parameter.parentNode.parentNode;
      formDiv.classList.remove('has-success', 'has-error');
      formDiv.classList.add(!validationResult ? 'has-success' : 'has-error');
    });

    if (errorCount == 0) {
      // Save simulation in _val
      let newSimulation = {'name': simulationNameInput.value, 'values': parameterValues};
      _val.simulations.push(newSimulation);

      // Mark newSimulation as selected
      _val.selectedSimulationIndex = _val.simulations.length - 1;

      // Add simulation name in simulationSelect
      $('#simulationSelect').append(`<option>${simulationNameInput.value}</option>`);
      // and select it
      $('#simulationSelect option').last().prop('selected', true);
    }
  }
}();
