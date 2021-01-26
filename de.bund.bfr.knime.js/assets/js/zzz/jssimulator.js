// // get simulations of a model
// async function getSimulations(identifier) {
//     const rep = await fetch(_globalVars.simulationsEndpoint + identifier);
//     return await rep.json();

// }

// async function buildSimulatorWindow(event) {

    // button id has format "executor{i}" where i is the model number (6th char)
    // let buttonId = event.target.id;
    // let modelNumber = buttonId.substr(8);

    // const metaPromise = _representation.metadata; //await getMetadata();
    // let metadata = metaPromise[modelNumber];

    // // get simulations of currently selected model
    // const simulations = await getSimulations(modelNumber);
    // _simulations = simulations;


    // // Update .modal-title
    // if (metadata.generalInformation && metadata.generalInformation.name) {
    //     $(".modal-title").text(metadata.generalInformation.name);
    // }

    // let body = `


    //           <form class="form-horizontal">
    //             <div class="form-group form-group-lg">
    //               <label class="col-md-3 control-label" for="simulationSelect">Simulation</label>
    //               <div class="col-md-5">
    //                 <select class="form-control" id ="simulationSelect">
    //                   <!-- Add simulations to select -->
    //                  ${simulations.map(simulation => `<option>${simulation.name}</option>`)}
    //                 </select>
    //               </div>
    //               <button id="removeButton" type="button" class="btn btn-lg btn-danger col-md-offset-1 col-xs-offset-1"
    //                title="Remove current simulation">
    //                 <i class="glyphicon glyphicon-trash"></i>
    //               </button>
    //               <button id="addButton" type="button" class="btn btn-lg btn-primary" title="Add new simulation">
    //                 <i class="glyphicon glyphicon-plus"></i>
    //               </button>
    //               <button id="saveButton" type="button" class="btn btn-lg btn-success" title="Save changes">
    //                 <i class="glyphicon glyphicon-floppy-disk"></i>
    //               </button>
    //             </div>
    //           </form>

    //           <form class="form-horizontal">
    //             <div class="form-group form-group-lg">
    //               <label class="col-md-3 control-label" for="simulationName">Simulation name</label>
    //               <div class="col-md-5">
    //                 <input id="simulationName" type="text" class="form-control">
    //                 <span id="helpBlock-simulationName" class="help-block"></span>
    //               </div>
    //             </div>
    //           </form>

    //           <form class="form-horizontal" id="parameter-form"></form>

    //     		`;



    // // remove tab panels from view:
    // let viewTab = document.getElementById("viewTab"); // "<div class='loader'></div>";
    // let runButton =  `<button type="button" class="btn btn-primary runButton" id="run${modelNumber}"
    //                 data-toggle="modal">RUN</button>`
    // viewTab.innerHTML = runButton;
    // $("#run" + modelNumber).click((event) => runModelView(event));
    // // Add viewContent
    // let viewContent = document.getElementById("viewContent");
    // viewContent.innerHTML = body; // First remove old tabs

    // $('#removeButton').click(function() {

    //     let simulationSelect = $('#simulationSelect');
    //     let selectedSimulationIndex = simulationSelect.prop('selectedIndex');

    //     if (selectedSimulationIndex != 0) {
    //         // Remove simulation in select
    //         $(`#simulationSelect option:eq(${selectedSimulationIndex})`).remove();

    //         // Remove simulation data
    //         _simulations.splice(selectedSimulationIndex, 1);

    //         // Select the default simulation (0)
    //         simulationSelect.val('defaultSimulation');
    //         _selectedSimulationIndex = 0;
    //         // Update UI
    //         updateSimulationName(0);
    //         updateParameterValues(0);
    //     }
    // });

    // document.getElementById('addButton').onclick = addSimulation;
    // document.getElementById('saveButton').onclick = submitSimulation;

    // $('#simulationSelect').change(() => {
    //     let selectedIndex = document.getElementById("simulationSelect").selectedIndex;
    //     updateSimulationName(selectedIndex);
    //     updateParameterValues(selectedIndex);

    //     // Store the index of the selected simulation as selected in the view value
    //     _selectedSimulationIndex = selectedIndex;
    // });



    // initSimulatorUI(metadata["modelMath"]["parameter"])
    // updateParameterValues(0);
    // updateSimulationName(0);
    // $(".modal").modal("show");
// }



// async function getExecutionResult(modelNumber){
//     const rep = await fetch(_globalVars.executionEndpoint + modelNumber);
//     return await rep.text();
// }



// async function runModelView(event){
//   // button id has format "run{i}" where i is the model number (6th char)
//     let buttonId = event.target.id;
//     let modelNumber = buttonId.substr(3);

//    // remove tab panels from view:
//     let viewTab = document.getElementById("viewTab"); // "<div class='loader'></div>";

//     viewTab.innerHTML = "<div>executing model... please wait</div>";

//     // Add viewContent
//     let viewContent = document.getElementById("viewContent");
//     viewContent.innerHTML = "<div class='loader'></div>";


//     viewContent.innerHTML = await getExecutionResult(modelNumber) ;
//     viewTab.innerHTML = "<div>model executed: </div>";
// }


/**
 * Initialize UI.
 *
 * - Create parameters and add them (only names). Parameter values are added later for the current simulation.
 */
// function initSimulatorUI(parameterRep) {

//     function createMetadataList(parameter) {

//         const formatProperty = (name, value) => value ? `<li><b>${name}</b>: ${value}</li>` : '';

//         return `<ul>
//               ${formatProperty("ID", parameter.id)}
//               ${formatProperty("Name", parameter.name)}
//               ${parameter.description ? `<li><b>Description</b>: ${parameter.description.substring(0,50)}</li>` : ''}
//               ${formatProperty("Unit", parameter.unit)}
//               ${formatProperty("Unit category", parameter.unitCategory)}
//               ${formatProperty("Data type", parameter.dataType)}
//               ${formatProperty("Source", parameter.source)}
//               ${formatProperty("Subject", parameter.subject)}
//               ${formatProperty("Distribution", parameter.distribution)}
//               ${formatProperty("Reference", parameter.reference)}
//               ${formatProperty("Variability subject", parameter.variabilitySubject)}
//               ${formatProperty("Min value", parameter.minValue)}
//               ${formatProperty("Max value", parameter.maxValue)}
//               ${formatProperty("Error", parameter.error)}
//             </ul>`;
//     }

//     // remove parameter IDs from global variable
//     _parameters = [];
//     _selectedSimulationIndex = 0;
//     // Add parameters
//     let parameterForm = $('#parameter-form');
//     parameterRep.forEach(parameter => {
//         if (parameter.classification != "OUTPUT") {

//             // add parameterIDs to global variable
//             _parameters.push(parameter);



//             let inputType;
//             if (parameter.dataType === "Integer" || parameter.dataType === "Double" || parameter.dataType === "Number") {
//                 inputType = "number";
//             } else {
//                 inputType = "text";
//             }

//             let metadataId = parameter.id + "-metadata";
//             let readonly = parameter.classification === "CONSTANT" ? "readonly" : "";
//             let parameterHtml = `<div class="form-group form-group-sm">
//               <label class="col-sm-3 control-label">${parameter.id}</label>
//               <div class="col-sm-8">
//                 <input type="${inputType}" class="form-control parameter-input" value="" ${readonly}></input>
//                 <span class="help-block"></span>
//                 <div class="collapse" id="${metadataId}" >
//                   <div class="alert alert-info card card-body">
//                     ${createMetadataList(parameter)}
//                   </div>
//                 </div>
//               </div>
//               <button class="btn" type="button" data-toggle="collapse" data-target="#${metadataId}" aria-expanded="false" aria-controls="${metadataId}" title="Show information">
//                 <i class="glyphicon glyphicon-info-sign"></i>
//               </button>
//             </div>`;
//             parameterForm.append(parameterHtml);

//             // Hide other parameter metadata panel when one is clicked.
//             $('#' + metadataId).on('show.bs.collapse', function() {
//                 $('.collapse.in').collapse('hide');
//             });
//         }
//     });
// }

/**
 * Update the simulation name input (#simulationName) when another simulation is selected.
 *
 * - Disable the input for the default simulation.
 */
// function updateSimulationName(simulationIndex) {
//     const simulationName = document.getElementById('simulationName');
//     simulationName.value = _simulations[simulationIndex].name;
//     simulationName.disabled = simulationIndex == 0;
// }

/**
 * Update parameter values for the given simulation.
 *
 * - All the parameter values are retrieved and displayed in the UI.
 * - Parameter inputs are disabled for the default simulation that is unmodifiable.
 */
// function updateParameterValues(simulationIndex) {

//     // Disable parameter inputs for the default simulation.
//     const isDisabled = simulationIndex == 0;

//     const inputs = document.getElementsByClassName('parameter-input');
//     //let vals = _parameterIds.
//     Array.from(inputs).forEach((input, index) => {
//         input.value = _simulations[simulationIndex].parameters[_parameters[index].id];
//         input.disabled = isDisabled;
//     });
// }

/**
 * Prepare the UI to enter a new simulation.
 *
 * - Clear simulation select (#simulationSelect).
 * - Clear and enable the simulation name input (#simulationName).
 * - Clear every parameter input (.parameter-input) and assign the default value.
 */
// function addSimulation() {

//     document.getElementById('simulationSelect').value = '';

//     const simulationName = document.getElementById('simulationName');
//     simulationName.value = '';
//     simulationName.disabled = false;

//     // Take the values from the default simulation

//     const inputs = document.getElementsByClassName('parameter-input');
//     Array.from(inputs).forEach((input, index) => {
//         input.value = _simulations[_selectedSimulationIndex].parameters[_parameters[index].id];
//         input.disabled = false;
//     });
// }

/**
 * Validate simulation and submit if it is error free.
 */
// function submitSimulation() {

    /**
     * Validate the entered name for a new simulation.
     * - Check that the name is not used already
     * - Check that the name is not empty.
     * - Check that the name is a valid SId.
     *
     * @returns An error if found. If not null.
     */
    // function validateSimulationName(name) {

    //     // Check that it is not duplicated
    //     for (let i = 0; i < _simulations.length; ++i) {
    //         if (name === _simulations[i].name) {
    //             return "Simulation name already exists"
    //         }
    //     }

    //     if (!_idRegexp.test(name)) return "Not a valid simulation name (SId)";

    //     return null;
    // }

    /**
     * Validate parameter value.
     *
     * Checks missing values and range (min and max values).
     */
    // function validateParameter(value, metadata) {

    //     // Check missing value
    //     if (!value) return `Please provide a value for ${metadata.id}`;

    //     // Check range for integers and doubles
    //     if (metadata.dataType === "INTEGER" || metadata.dataType === "DOUBLE") {
    //         if (metadata.minValue && parseFloat(metadata.minValue) > value) {
    //             return `Invalid value. Value is lower than minimal value (${metadata.minValue})`;
    //         }
    //         if (metadata.maxValue && parseFloat(metadata.maxValue) < value) {
    //             return `Invalid value. Value is higher than maximal value (${metadata.maxValue})`;
    //         }
    //     }

    //     return null;
    // }

//     let errorCount = 0;

//     // Validate simulation name
//     const simulationNameInput = document.getElementById("simulationName");
//     const nameValidationResult = validateSimulationName(simulationNameInput.value);
//     const helpBlock = document.getElementById("helpBlock-simulationName");

//     if (!nameValidationResult) {
//         // Clear and hide the help block
//         helpBlock.textContent = "";
//         helpBlock.style.visibility = "hidden";
//     } else {
//         // Add error and show the help block
//         helpBlock.textContent = nameValidationResult;
//         helpBlock.style.visibility = "visible";

//         errorCount++;
//     }

//     // Get the form-group div and set the validation status
//     const formDiv = simulationNameInput.parentNode.parentNode;
//     formDiv.classList.remove('has-success', 'has-error');
//     formDiv.classList.add(!nameValidationResult ? 'has-success' : 'has-error');

//     // create new simulation object with default values
//     // deep clone necessary
//     let newSimulation = JSON.parse(JSON.stringify(_simulations[0]));

//     const parameterInputs = document.getElementsByClassName('parameter-input');
//     Array.from(parameterInputs).forEach(function(parameter, index) {

//         // savae new value
//         newSimulation.parameters[_parameters[index].id] = parameter.value;

//         const parameterHelp = parameter.parentNode.querySelector('.help-block');

//         const validationResult = validateParameter(parameter.value, _parameters[index]);
//         if (!validationResult) {
//             // Clear and hide the help block
//             parameterHelp.textContent = "";
//             parameterHelp.style.visibility = "hidden";
//         } else {
//             // Add error and show the help block
//             parameterHelp.textContent = validationResult;
//             parameterHelp.style.visibility = "visible";

//             errorCount++;
//         }

//         // Get the form-group div and set the validation status
//         const formDiv = parameter.parentNode.parentNode;
//         formDiv.classList.remove('has-success', 'has-error');
//         formDiv.classList.add(!validationResult ? 'has-success' : 'has-error');
//     });

//     if (errorCount == 0) {
//         // Save simulation
//         newSimulation.name = simulationNameInput.value;
//         _simulations.push(newSimulation);

//         // Mark newSimulation as selected
//         _selectedSimulationIndex = _simulations.length - 1;

//         // Add simulation name in simulationSelect
//         $('#simulationSelect').append(`<option>${simulationNameInput.value}</option>`);
//         // and select it
//         $('#simulationSelect option').last().prop('selected', true);
//     }
// }