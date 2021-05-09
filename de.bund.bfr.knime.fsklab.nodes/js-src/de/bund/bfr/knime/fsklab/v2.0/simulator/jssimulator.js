simulator = function() {
	
	var view = { version: "0.0.1" };
	view.name = "JavaScript Simulation Configurator"

	var _rep;
	var _val;
	
	let _simulations = [];
	let _modelSim;
	view.init = function(representation, value) {
		_rep = representation;
	    _val = value;
	    let _container = $(`<div class="simulationsDiv modal-sim"></div>`);
	    document.createElement('body');
	    $('body').html(_container);
		
	   _modelSim = new APPSimulation( {
			data 		: {},
			id 			: 'mtModelSim',
			classes 	: 'model-sim',
			type 		: 'mtSim',
			on 			: {
				simRunModelView : ( O, modelId, simulation )=> {
					_log( 'on > simRunModelView', 'hook' ); // example hook
															// output
					_log( O );
					_log( modelId );
					_log( simulation );
				}
			}
		}, _container );
	   _modelSim._createSimulationContent();
	   let _parameters = JSON.parse(value.modelMath)['parameter'];
	   $.each(value.simulations, function(index, sim){
		   let params = {};
		   $.each(representation.parameters, function(index, param){
			   params[param['id']] = sim['values'][index];
		   });
		   _simulations.push({'name':sim['name'],'parameters':params});
	   });
	   _modelSim._updateContent({'modelMath':JSON.parse(value.modelMath)}, 0, _simulations);
	};

	view.getComponentValue = function() {
		let sims = [];
		$.each(_simulations, function(index, sim){
		   let params = [];
		   $.each(sim.parameters, function(key, value){
			   params.push(value);
		   });
		   sims.push({'name':sim['name'],'values':params});
	   });
		_val.simulations = sims;
		_val.selectedSimulationIndex = _modelSim._simSelectedIndex;
		return _val;
  };
  
	return view;
}();
