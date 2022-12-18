/*

version: 1.0.0
author: sascha obermüller
date: 04.12.2020

*/

class APPUI {
	constructor( settings ) {
		let O = this;
		O._debug = true;
		// defaults
		O._opts = $.extend( true, {}, {
			on 				: {
				afterInit		: null
			}
		}, settings );
	}
	get opts() {
		return this._opts;
	}
	set opts( settings ) {
		this._opts = $.extend( true, {}, this.opts, settings );
	}


	/**
	 * CREATE TABLE HEAD
	 * @param {array} settings
	 */

	_createTableHead ( settings ) {

		// thead
		let $thead = $( '<thead></thead>' );

		if( settings ) {
			// thead id
			settings.id ? $thead.attr( 'id', settings.id ) : null;

			// create th cols
			if( settings.cols ) {
				$.each( settings.cols, ( i, col ) => {
					let $th = $( '<th></th>' )
						.appendTo( $thead );
						// th attributes
						col.label ? $th.html( '<span>'+ col.label +'</span>' ) : null;
						col.id ? $th.attr( 'id', col.id ) : null; // id
						col.classes && col.classes.th ? $th.addClass( col.classes.th ) : null; // classes
						col.field ? $th.attr( 'data-field', col.field ) : null; // bs table / data-field identifier
						col.sortable ? $th.attr( 'data-sortable', col.sortable ) : null; // bs table / data-sortable
						col.switchable ? $th.attr( 'data-switchable', col.switchable ) : null; // bs table / data-switchable
						col.sorter ? $th.attr( 'data-sorter', col.sorter ) : null; // bs table / data-sorter function

						// th custom attributes
						if( col.attribute && col.attributes.th ) {
							$.each( col.attributes.th, ( attr, val ) => {
								$th.attr( attr, val );
							} );
						}
				} );
				$thead.wrapInner( '<tr></tr>' );
			}	
		}
		return $thead;
	}


	/**
	 * CREATE MODAL
	 * @param {array} settings
	 * @param {jquery selector/object} $container: append to this
	 */

	_createModal ( settings, $container ) {
		let O = this;
		_log( 'UI / _createModal' );

		return new APPModal( settings, $container );
	}


	/**
	 * CREATE LOADER
	 * create page loader
	 * @param {array} settings
	 * @param {jquery selector/object} $container: append to this
	 */

	_createLoader ( settings, $container ) {
		let O = this;
		_log( 'UI / _createLoader' );

		let loader = {};
		loader._$el = $( '<div class="loader loading"></div>' )
			.appendTo( $container );
		// optional classes
		settings.classes ? loader._$el.addClass( settings.classes ) : null;

		loader._setState = ( state ) => {
			_log( 'UI / loader._setState : '+ state );
			state ? loader._$el.addClass( 'loading' ) : loader._$el.removeClass( 'loading' );
		};

		return loader;
	}



	/**
	 * CREATE ALERT
	 * create alert and place it into $container
	 * @param {array} settings
	 * @param {jquery selector/object} $container: append to this
	 */

	_createAlert ( msg, settings, $container ) {
		let O = this;
		_log( 'UI / _createAlert' );

		let $alert = $( '<div class="alert fade">'+ msg +'</div>' )
			.appendTo( $container );

		// alert type
		settings.type ? $alert.addClass( 'alert-'+ settings.type ) : $alert.addClass( 'alert-info' ); // bs type: primary, secondary, success, danger, warning, info
		settings.state ? $alert.addClass( settings.state ) : $alert.addClass( 'show' ); // hide or show
		settings.classes ? $alert.addClass( settings.classes ) : null;

		// dismissable
		if ( settings.dismissable ) {
			$alert.addClass( 'alert-dismissable' );
			// close button
			$alert.append( '<button type="button" class="close close-sm" data-dismiss="alert" aria-label="Close"><i class="feather icon-x" aria-hidden="true"></i></button>' );
		}

		return $alert;
	}

	_precision(param) {
		let step = 1;
		// add case if parameter is integer (no decimal point)
	  	let decimals = param.includes(".") ? param.substring(param.indexOf('.') + 1).length : 0;
		for ( let j = 0; j < decimals; j++ ) {
			step = step / 10;
		}
	  	return step;
	}
	/**
	 * POPULATE SELECT
	 *
	 * @param {element} select: dom element
	 * @param {array} options: array of possible values
	 */
	_populateSelect ( $select, options ) {
		let O = this;
		if( $select ) {
			options.forEach( entry => {
				$select.append( '<option value="'+ entry +'">'+ entry +'</option>' );
			} );
		}
	}


	/**
	 * POPULATE SELECT BY ID
	 *
	 * @param {string} selectID: element id
	 * @param {array} options: array of possible values
	 */
	_populateSelectByID ( selectID, options ) {
		let O = this;
		let $select = $( selectID );
		O._populateSelect( $select, options );
	}


	/**
	 * CREATE TOOLTIPS
	 * initialize tooltips on all elements with data-attribute [data-tooltip]
	 * @param {string/jquery selector} container: dom-element that contains the elements to init
	 */

	_initTooltips ( container ) {
		let O = this;
		_log( 'UI / _initTooltips' );

		let $elems = $( container ).length > 0 ? $( container ).find( '[data-tooltip]' ) : $( '[data-tooltip]' );
		$elems.each( ( i, el ) => {

			let $el = $( el );
			// create tooltips
			$el.tooltip( {
				offset : 10
			} );
		} );
	}

	/**
	 * INIT TOGGLE TD
	 * adds a collapsable container on element <td data-td-collapse>, when td's content height is higher than defined min-height
	 * must be initiated before bs table init
	 * @param {string/jquery selector} container: dom-element that contains the elements to init
	 */
		 
	_initTdCollapse ( $table ) {
		let O = this;
		_log( 'UI / _initTdCollapse' );

		let minH = 100;

		let $tds = $table.find( 'td[data-td-collapse="true"]' );
		
		$tds.each( ( i, td ) => {
			
			let $td = $( td ); // td
			$td.wrapInner( '<div></div>' );

			// check for content higher than min height
			if( $td.children().outerHeight() > minH ) {
				// create unique id
				let collapseId = 'tdCollapse'+ jQuery.now();
				// wrap inner with collapse container
				$td.wrapInner( '<div id="'+ collapseId +'" class="collapse td-collapse"></div>' );
				// create toggle
				let $collapseToggle = $( '<a href="#" class="td-collapse-toggle collapsed" data-target="#'+ collapseId +'" data-toggle="collapse" aria-expanded="false" aria-controls="'+ collapseId +'"></a>' ).appendTo( $td );
				// create collapse
				$( '#'+ collapseId ).collapse( {
					toggle: false
				} )
			}
		} );
	}


	/**
	 * INIT SELECT2
	 * initialize select2 lib on element <select data-sel2 …>
	 * @param {string/jquery selector} container: dom-element that contains the elements to init
	 */

	_initSelect2 ( container ) {
		let O = this;
		_log( 'UI / _initSelect2' );

		let $elems = $( container ).length > 0 ? $( container ).find( 'select[data-sel2]' ) : $( 'select[data-sel2]' );
		$elems.each( ( i, el ) => {

			let $el = $( el ); // select

			let select2Defaults = {
				dropdownParent			: $el.parent(),
				dropdownAutoWidth 		: false,
				// minimumResultsForSearch	: Infinity,
				width 					: '100%'
			};
			// check for settings by attributes
			// select size
			if( el.hasAttribute( 'data-sel2-size' ) && $el.data( 'sel2-size' ) == 'sm' ||
				$el.hasClass( 'form-control-sm' ) ||
				$el.hasClass( 'custom-select-sm' ) ) {
				select2Defaults.selectionCssClass = 'select2-selection--sm';
				select2Defaults.dropdownCssClass = 'select2-dropdown--sm';
			}
			// check allow clear attr
			if( el.hasAttribute( 'data-allow-clear' ) && $el.data( 'allow-clear' ) == true ) {
				select2Defaults.selectionCssClass += ' select2-selection--clear';
			}
			// check custom max height attr
			if( el.hasAttribute( 'data-sel2-max-height' ) ) {
				select2Defaults.selectionCssClass += ' select2-selection--max-height';
			}
			// check custom choice single row
			if( el.hasAttribute( 'data-sel2-choice-single-row' ) ) {
				select2Defaults.selectionCssClass += ' select2-selection--choice-single-row';
			}
			// check max selection length attr
			if( el.hasAttribute( 'data-maximum-selection-length' ) && $el.data( 'maximum-selection-length' ) == '1' ) {
				select2Defaults.selectionCssClass += ' select2-selection--max-sel-1';
			}
			// create select2
			
			window[$($el).attr('id')] = $el.select2( select2Defaults );

			// $( window ).on( 'resize', function() {
			// 	$el.select2( select2Defaults );
			// } );
		} );
	}


	/**
	 * INIT CLEAR
	 * initialize clear function
	 * data-clear attribute should contain targets for clear as jquery selector
	 * @param {string/jquery selector} container: dom-element that contains the elements to init
	 */

	_initClear ( container ) {
		let O = this;
		_log( 'UI / _initClear' );

		let $elems = $( container ).length > 0 ? $( container ).find( '[data-clear]' ) : $( '[data-clear]' );
		$elems.each( ( i, el ) => {

			let $clear = $( el ); // button or a
			// set clear targets
			$clear.targets = $clear.data( 'clear' );

			if( $( $clear.targets ) ) {

				$clear.state = false;

				$( $clear.targets ).on( 'change keyup', ( event ) => {
					
					$clear.state = false;
					// check all target's state
					$.each( $( $clear.targets ), ( j, target ) => {
						if( $( target ).val().length > 0 ) {
							$clear.addClass( 'visible' );
							$clear.state = true;
						}
					} );

					if( $clear.state ) {
						$clear.addClass( 'visible' );
					}
					else {
						$clear.removeClass( 'visible' );
					}
				} );
				// add event to clear
				$clear.on( 'click', ( event ) => {
					// iterate all targets and reset
					$.each( $( $clear.targets ), ( j, target ) => {
						let $target = $( target );
						if( $target.is( 'select') ) {
							$target.val( '' ).trigger('change');
						}
						else if( $target.is( 'input') ) {
							$target.val( '' ).trigger('change');
						}
					} );
					// hide clear
					$clear.state = false;	
					$clear.removeClass( 'visible' );
				} );
			}
			else {
				$clear.remove();
			}
		} );
	}


	/**
	 * INIT TOUCHSSPIN
	 * initialize touchspin lib on element <input type="text" data-touchspin …>
	 * @param {string/jquery selector} container: dom-element that contains the elements to init
	 */

	_initTouchspin ( container ) {
		let O = this;
		_log( 'UI / _initTouchspin' );

		let $elems = $( container ).length > 0 ? $( container ).find( 'input[data-touchspin]' ) : $( 'input[data-touchspin]' );
		$elems.each( ( i, el ) => {

			let $el = $( el ); // input

			let touchspinDefaults = {
				buttondown_class 	: "btn btn-outline-secondary",
				buttonup_class 		: "btn btn-outline-secondary",
				decimals			: 0,
				initval 			: 0,
				mousewheel			: true,
				min					: null,
				forcestepdivisibility : 'none',
				step 				: 1
			};

			// check for settings by attributes
			// min & max
			if( el.hasAttribute( 'data-touchspin-min' ) ) {
				touchspinDefaults.min = $el.data( 'touchspin-min' );
			}
			if( el.hasAttribute( 'data-touchspin-max' ) ) {
				touchspinDefaults.max = $el.data( 'touchspin-max' );
			}
			// step
			if( el.hasAttribute( 'data-touchspin-step' ) ) {
				touchspinDefaults.step = $el.data( 'touchspin-step' );
			}
			// decimals
			if( el.hasAttribute( 'data-touchspin-decimals' ) ) {
				touchspinDefaults.decimals = $el.data( 'touchspin-decimals' );
			}
			// initial value
			if( el.hasAttribute( 'data-touchspin-initval' ) ) {
				touchspinDefaults.initval = $el.data( 'touchspin-initval' );
			}
			// prefix & postfix
			if( el.hasAttribute( 'data-touchspin-prefix' ) ) {
				touchspinDefaults.prefix = $el.data( 'touchspin-prefix' );
			}
			if( el.hasAttribute( 'data-touchspin-postfix' ) ) {
				touchspinDefaults.postfix = $el.data( 'touchspin-postfix' );
			}
			_log( touchspinDefaults );
			// create touchspin
			$el.TouchSpin( touchspinDefaults );
            $el.on('change', function (event) {                    
                if(isNaN(event.currentTarget.value)){
                    $el.trigger('touchspin.destroy');  
                }else{
                    $el.TouchSpin( touchspinDefaults );
                }
            });
		} );
	}


	/**
	 * INIT DATEPICKER
	 * initialize jquery.datepicker lib on element <input type="text" data-datepicker …>
	 * @param {array} settings: setting for datepicker
	 * @param {string/jquery selector} container: dom-element that contains the elements to init
	 */

	_initDatepicker ( container ) {
		let O = this;
		_log( 'UI / _initDatepicker' );

		let $elems = $( container ).length > 0 ? $( container ).find( '[data-datepicker]' ) : $( '[data-datepicker]' );
		$elems.each( ( i, el ) => {

			let $el = $( el ); // input or input group

			// create datepicker
			$el.datepicker( {
				format			: {
					toDisplay: ( date, format, language ) => {
						let d = new Date( date );
						let day = d.getDate();
						let month = d.getMonth();
						let year = d.getFullYear();

						return year +'-'+ month +'-'+ day;
					},
					toValue: ( date, format, language ) => {
						return date;
					}
				},
				todayHighlight	: true,
			} );
		} );
	}


	/**
	 * INIT RANGESLIDER
	 * initialize ion.rangeslider lib on element <input type="text" data-rangeslider …>
	 * @param {string/jquery selector} container: dom-element that contains the elements to init
	 */

	_initRangeslider ( container ) {
		let O = this;
		_log( 'UI / _initRangeslider' );

		let $elems = $( container ).length > 0 ? $( container ).find( '[data-rangeslider]' ) : $( '[data-rangeslider]' );
		$elems.each( ( i, el ) => {

			let $el = $( el ); // input

			let rangesliderDefaults = {
				drag_interval	: true,
			};

			// check if inputs for rangeslider are set and exist as el
			// for double slider
			if( el.hasAttribute( 'data-type' ) && $el.data( 'type' ) == 'double' ) {
				// from input
				if( el.hasAttribute( 'data-control-double-from' ) && $( $el.data( 'control-double-from' ) ).length > 0 ) {
					$el.$inputDoubleFrom = $( $el.data( 'control-double-from' ) );
					$el.$inputDoubleFrom.on( 'change', ( event ) => {
						let min = $el.data( 'rangeslider' ).result.min;
						let to = $el.data( 'rangeslider' ).result.to;
						let val = $el.$inputDoubleFrom.prop( 'value' );
						// validate
						if ( val < min ) {
							val = min;
						}
						else if ( val > to ) {
							val = to;
						}

						$el.data( 'ionRangeSlider' ).update( {
							from : val
						} );

						$el.$inputDoubleFrom.val( val );
					} );
				}
				// to input
				if( el.hasAttribute( 'data-control-double-to' ) && $( $el.data( 'control-double-to' ) ).length > 0 ) {
					$el.$inputDoubleTo = $( $el.data( 'control-double-to' ) );
					$el.$inputDoubleTo.on( 'change', ( event ) => {
						let max = $el.data( 'rangeslider' ).result.max;
						let from = $el.data( 'rangeslider' ).result.from;
						let val = $el.$inputDoubleTo.prop( 'value' );
						// validate
						if ( val < from ) {
							val = from;
						}
						else if ( val > max ) {
							val = max;
						}

						$el.data( 'rangeslider' ).update( {
							to : val
						} );

						$el.$inputDoubleTo.val( val );
					} );
				}
				// if inputs for from/to, ad update routines
				if( $el.$inputDoubleFrom || $el.$inputDoubleTo ) {
					rangesliderDefaults = $.merge( {
						drag_interval	: false,
						onStart			: ( data ) => { $el.updateInputs( data ); },
						onChange		: ( data ) => { $el.updateInputs( data ); },
						onFinish		: ( data ) => { $el.updateInputs( data ); }
					}, rangesliderDefaults );

					$el.updateInputs = ( data ) => {
						from = data.from;
						to = data.to;

						$el.$inputDoubleFrom.prop( 'value', from );
						$el.$inputDoubleTo.prop( 'value', to );
					};
				}
			}
			// for single slider
			else {
				// to do
				// from input
				if( el.hasAttribute( 'data-control-single' ) && $( $el.data( 'control-single' ) ).length > 0 ) {
					$el.$inputSingle = $( $el.data( 'control-single' ) );
					$el.$inputSingle.on( 'change', ( event ) => {
						let min = $el.data( 'rangeslider' ).result.min;
						let max = $el.data( 'rangeslider' ).result.max;
						let val = $el.$inputSingle.prop( 'value' );
						// validate
						if ( val < min ) {
							val = min;
						}
						else if ( val > max ) {
							val = max;
						}

						$el.data( 'rangeslider' ).update( {
							from : val,
							step: O._precision(val)
						} );

						$el.$inputSingle.val( val );
					} );
				}
				// if inputs for from/to, ad update routines
				if( $el.$inputSingle ) {
					rangesliderDefaults = $.merge( {
						drag_interval	: false,
						onStart			: ( data ) => { $el.updateInputs( data ); },
						onChange		: ( data ) => { $el.updateInputs( data ); },
						onFinish		: ( data ) => { $el.updateInputs( data ); }
					}, rangesliderDefaults );

					$el.updateInputs = ( data ) => {
						from = data.from;

						$el.$inputSingle.prop( 'value', from );
					};
				}
			}

			// create datepicker
			$el.ionRangeSlider( rangesliderDefaults );
			$el.data( 'rangeslider', $el.data( 'ionRangeSlider' ) );

		} );
	}


	/**
	 * INIT FORM VALIDATION
	 * initialize validation on forms with data-atribute [data-validate]
	 * @param {string/jquery selector} container: dom-element that contains the elements to init
	 */

	_initFormValidation ( container ) {
		let O = this;
		_log( 'UI / _initFormValidation' );

		let $elems = $( container ).length > 0 ? $( container ).find( '[data-validate]' ) : $( '[data-validate]' );
		$elems.each( ( i, el ) => {
			let $el = $( el ); // form
			let $validations = $el.find( '.validate-me' );
			let validation = Array.prototype.filter.call( $elems, ( form ) => {
				form.addEventListener( 'submit', ( event ) => {
					if ( form.checkValidity() === false ) {
						e.preventDefault();
						e.stopPropagation();
					}
					// added validation class to all form-groups in need of validation
					$validations.each( ( j, val ) => {
						$( val ).addClass( 'was-validated' );
					} );
				}, false );
			} );
		} );
	}


	/**
	 * INIT FORMS
	 * combined initialization external lib items 
	 * - touchspin
	 * - select2
	 * - datepicker
	 * - ion rangeslider
	 */

	_initFormItems ( container ) {
		let O = this;
		_log( 'UI / _initFormItems' );

		O._initClear( container );
		O._initTouchspin( container );
		O._initSelect2( container );
		O._initDatepicker( container );
		O._initRangeslider( container );
	};


	/**
	 * INIT All
	 * combined initialization of all external lib items 
	 * - touchspin
	 * - select2
	 * - datepicker
	 * - ion rangeslider
	 */

	_initAll () {
		let O = this;
		_log( 'UI / _initAll' );

		O._initClear();
		O._initTouchspin();
		O._initSelect2();
		O._initDatepicker();
		O._initRangeslider();
		O._initFormValidation();
	};

}

var _appUI = _appUI || new APPUI();