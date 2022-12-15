/*

version: 1.0.0
author: sascha obermüller
date: 06.12.2020

*/

class APPModal {
	constructor( settings, $container ) {
		let O = this;
		O._$container = $container;
		O._debug = true;
		O._initiated = false;
		// defaults
		O._opts = $.extend( true, {}, {
			classes : '',
			data 	: null,
			on 		: {
				afterInit 	: null, // function
				show 		: ( O, event ) => {
					O._updateModal( event );
				}, // function
				hide 		: null // function
			}
		}, settings );
		// basic init actions
		O._create();
		O._initiated = true;
		// callback
		if ( $.isFunction( O.opts.on.afterInit ) ) {
			O.opts.on.afterInit.call( O );
		}
	}
	get opts () {
		return this._opts;
	}
	set opts ( settings ) {
		this._opts = $.extend( true, {}, this.opts, settings );
	}


	/**
	 * CREATE
	 * build basic model parts: modal header, body
	 */
	 
	_create () {
		let O = this;
		_log( 'MODAL / _create' );

		O._id = O.opts.id || 'modal'+ $.now();
		O._type = O.opts.type || 'default';


		O._$modal = $( '<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true"></div>' )
			.attr( 'id', O._id )
			.addClass( O.opts.classes )
			.appendTo( O._$container );

		O._$modalContent = $( '<div class="modal-content"></div>' )
			.appendTo( O._$modal )
			.wrap( '<div class="modal-dialog modal-xl" role="document"></div>' );

		// loader
		O._loader = _appUI._createLoader( { classes : 'loader-modal' }, O._$modalContent );

		// create modal
		O._createModal();

		// create bs modal
		O._$modal.modal( {
			show 	: false // initially hidden
		} );

		// bind show event
		O._$modal.on( 'show.bs.modal', ( event ) => {
			// callback
			if ( $.isFunction( O.opts.on.show ) ) {
				O.opts.on.show.call( O, O, event );
			}
			
			if(window.knimeService.isSingleView() == undefined && !(window.multipleview)) {
				window.parent.scrollTo(0,0);
			}
		} );

		// bind hide event
		O._$modal.on( 'hide.bs.modal', ( event ) => {
			// callback
			if ( $.isFunction( O.opts.on.hide ) ) {
				O.opts.on.hide.call( O, O, event );
			}
		} );
	}


	/**
	 * CREATE MODAL
	 * creates basic modal components: header and blank body
	 */
	 
	_createModal () {
		let O = this;
		_log( 'MODAL / _createModal' );

		// modal head default
		O._createModalHead()

		// modal body default
		O._createModalBody()
			.appendTo( O._$modalContent );
	}


	/**
	 * CREATE MODAL HEAD
	 * creates basic modal header with title and close
	 */
	 
	_createModalHead () {
		let O = this;
		_log( 'MODAL / _createModalHead' );

		// modal head
		O._$modalHead = $( '<div class="modal-header"></div>' )
			.appendTo( O._$modalContent );
		// modal head title
		O._$modalTitle = $( '<h1 class="modal-title"></h1>' )
			.appendTo( O._$modalHead );
		// modal close
		$( '<button type="button" class="modal-close action action-pure action-lg ml-2" data-dismiss="modal" aria-label="Close"><i class="feather icon-x"></i></button>' )
			.appendTo( O._$modalHead );
	}


	/**
	 * CREATE MODAL
	 * creates basic modal components: header and blank body
	 */
	 
	_createModalBody () {
		let O = this;
		_log( 'MODAL / _createBody' );

		O._$modalBody = $( '<div class="modal-body"></div>' )
			.appendTo( O._$modalContent );
	}

	/**
	 * BUILD MODAL
	 * build modal blank function
	 */
	 
	async _updateModal ( event ) {
		let O = this;
		_log( 'MODAL / _updateModal' );

		// let $trigger = $( event.relatedTarget );
		// let modalID = $trigger.data( 'modal-id' );
	}


	/**
	 * SET TITLE
	 * set modal title
	 */
	
	_setTitle ( text ) {
		let O = this;
		_log( 'MODAL / _setTitle: '+ text );

		if ( O._$modalTitle ) {
			O._$modalTitle.text( text );
		}
	}


	/**
	 * SHOW
	 * show modal
	 */

	_show () {
		let O = this;
		O._$modal.modal( 'show' );
	}


	/**
	 * HIDE
	 * hide modal
	 */
	
	_hide () {
		let O = this;
		O._$modal.modal( 'hide' );
	}


	/**
	 * CLEAR MODAL
	 * removes table body rows
	 */

	_clear () {
		let O = this;
		_log( 'MODAL / _clear' );

		if ( O._$modalContent ) {
			O._$modalContent.empty();
		}
	}
}