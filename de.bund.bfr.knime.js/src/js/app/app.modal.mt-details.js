/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

class APPModalMTDetails extends APPModal {
	constructor ( settings, $container ) {
		super( settings, $container );
	}


	/**
	 * CREATE
	 * calls super class and sets _metadata
	 */
	 
	_create () {
		let O = this;
		_log( 'MODAL DETAILS / _create', 'primary' );

		O._metadata = O.opts.data;

		super._create();
	}


	/**
	 * CREATE MODAL
	 * creates basic modal components: header and blank body
	 */
	 
	_createModal () {
		let O = this;
		_log( 'MODAL DETAILS / _createModal' );

		// modal head default
		O._createModalHead();
		O.ModelMTPanel = new APPMTDetails( O.opts, O._$modalContent );
		O.ModelMTPanel._createModelMetadataContent();
	}


	/**
	 * BUILD MODAL
	 * build modal content
	 */
	 
	async _updateModal ( event ) {
		let O = this;
		_log( 'MODAL DETAILS / _updateModal' );
		_log( event );
		
		O._loader._setState( true ); // set loader
		
		// get trigger
		let $trigger = $( event.relatedTarget );
		// get model id & data
		O._modelId = $trigger.data( 'modal-id' );
		O._modelMetadata = O._metadata[O._modelId];

		// modal title
		if ( O._modelMetadata.generalInformation && O._modelMetadata.generalInformation.name ) {
			O._setTitle( O._modelMetadata.generalInformation.name );
		}
		await O.ModelMTPanel._updateContent(O._modelMetadata, O._modelId);
		O._loader._setState( false ); // set loader
	}
}