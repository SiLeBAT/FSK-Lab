/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

class APPModalMTSimulations extends APPModal {
	constructor ( settings, $container ) {
		// defaults maintable simulations modal
		let modalSettings = $.extend( true, {}, {
			on : {
				simRunModelView : null // function
			}
		}, settings );
        
		super( modalSettings, $container );
	}

	/**
	 * CREATE
	 * calls super class and sets _metadata
	 */
	 
	_create () {
		let O = this;
		_log( 'MODAL SIM / _create', 'primary' );
		_log( O.opts );

		// global
		O._metadata = O.opts.data;
		O._state = 'params'; // default state: params form

		O._$simInputs = []; // inputs from params and customs
		O._simFields = {};
		O._simSelectedIndex = 0; // initial simulation

		super._create();

		// bind hide event
		O._$modal.on( 'hide.bs.modal', ( event ) => {
			// callback
			if ( $.isFunction( O.opts.on.hide ) ) {
				O.opts.on.hide.call( O, O, event );
			}
			// abort running fetch
			if( O._fetchController ) {
				_log( 'MODAL SIM / abort fetch' );
				O._fetchController.abort();
			}
		} );

	}


	/**
	 * CREATE MODAL
	 * creates basic modal components: header and blank body
	 */

	_createModal () {
		let O = this;
		_log( 'MODAL SIM / _createModal' );
		// modal head default
		O._createModalHead();
		O.opts._loader = O._loader;
		O._simulationPanel = new APPSimulation( O.opts, O._$modalContent );
		O._simulationPanel._createSimulationContent();
	}

	/**
	 * BUILD MODAL
	 * build modal content
	 * @param {event} event 
	 */
	 
	async _updateModal( event ) {
		let O = this;
		_log( 'MODAL SIM / _updateModal' );
		_log( event );
		O._loader._setState( true ); // set loader
		
		// get trigger
		let $trigger = $( event.relatedTarget );
		// get model id & data
		O._modelId = $trigger.data( 'modal-id' );
		O._modelMetadata = O._metadata[O._modelId];
		
		// modal title 
		if ( O._modelMetadata.generalInformation && O._modelMetadata.generalInformation.name ) {
			_log(O._modelMetadata.generalInformation.name);
			O._setTitle( O._modelMetadata.generalInformation.name );
		}
		// get simulations
		let modelIdentifier = O._modelMetadata.generalInformation.identifier 
		_simulations = await  _fetchData._json( window._endpoints.simulations, modelIdentifier );//O._app._getSimulations( O._modelId );
		await O._simulationPanel._updateContent(O._modelMetadata, O._modelId, _simulations);
		O._loader._setState( false ); // set loader
	}
}