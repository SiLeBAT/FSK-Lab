/*

version: 1.0.0
author: sascha obermüller
date: 04.12.2020

*/

class APPLandingpage {
	constructor ( settings, $container, metadata, uploadDates, executionTimes  ) {
		let O = this;
		O._metadata = metadata;
        O._uploadDates = uploadDates;
        O._executionTimes = executionTimes;
		O._$container = $container;
		O._debug = true;
		// defaults
		O._opts = $.extend( true, {}, {
			header			: {
				brand			: {
					logo			: 'assets/img/bfr_logo.gif', // false
					title			: 'FSK-Web Landing Page Test' // false or ''
				},
				nav				: [
					{
						title		: 'MenuItem',
						href		: '#'
					}
				]
			},
			mainTable 		: {},
			on 				: {
				afterInit		: null
			}
		}, settings );

		// basic init actions
		O._create();
		
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
	 * init main app
	 */

	async _create () {
		let O = this;
		_log( 'LANDINGPAGE / _create', 'primary' );

		// header
		if( O.opts.header ) {
			O._$header = O._createHeader( O.opts.header )
				.appendTo( O._$container );
		}

		// endpoints defined?
		if ( typeof window._endpoints != typeof undefined ) {

			// main table settings: merge with app opts
			let mtSettings = $.extend( true, {}, {
				endpoints		: window._endpoints,
				data 		: {
					metadata 		: O._metadata,
					uploadDates 	: O._uploadDates,
					executionTimes 	: O._executionTimes
				},
			}, O.opts.mainTable );
			O._mainTable = new APPTableMT( mtSettings, O._$container, O._metadata,O._uploadDates, O._executionTimes  );

			// tooltips
			_appUI._initTooltips();
		}
		else {
			// error no endpoints
			_appUI._createAlert( 'Cannot create main table, no endpoints defined', {}, O._$container );
		}
	}


	/**
	 * CREATE HEADER
	 * @param {array} settingss
	 */

	_createHeader ( settings ) {
		let O = this;
		_log( 'LANDINGPAGE / _create header' );

		// header
		let $header = $( '<header class="page-header"></header>' );

		// navbar
		$header.navBar = $( '<nav class="navbar navbar-expand">' )
			.appendTo( $header );

		// header brand
		if ( settings.brand ) {
			$header.navBrand = $( '<div class="navbar-brand"></div>' )
				.appendTo( $header.navBar );
			
			// brand logo
			if ( settings.brand.logo ) {
				// logo
				$( '<span class="brand-logo"><img src="'+ settings.brand.logo +'" alt="" /></span>' )
					.appendTo( $header.navBrand );
				// brand logo + title ? add divider 
				if ( settings.brand.title ) {
					$( '<span class="brand-divider"></span>' )
						.appendTo( $header.navBrand );
				}
			}
			// brand title
			if ( settings.brand.title ) {
				$( '<span class="brand-typo">'+ settings.brand.title +'</span>' )
					.appendTo( $header.navBrand );
			}
		}

		// create header nav
		if ( settings.nav && settings.nav.length > 0 ) {
			
			// nav
			$header.nav = $( '<ul class="navbar-nav mt-2 mt-sm-0 ml-auto"></ul>' )
				.appendTo( $header.navBar );

			// toggle
			$header.navToggle = $( '<button id="menuToggle" class="action action-pure action-lg" data-toggle="dropdown" aria-expanded="false" role="button"><i class="feather icon-menu"></i></button>' )
				.appendTo( $header.nav )
				.wrap( '<li class="nav-item"></li>' )
				.wrap( '<div class="dropdown"></div>' );

			// dropdown menu
			$header.navDropdownMenu = $( '<div class="dropdown-menu dropdown-menu-right" aria-labeledby="menuToggle"></div>' )
				.insertAfter( $header.navToggle );

			// dropdown items by opts
			$.each( settings.nav, ( i, link ) => {
				// set link target or default value _self?
				link.target = link.target ? link.target : '_self';
				// add links
				let $link = $( '<a href="'+ link.href  +'" target="'+ link.target +'" class="dropdown-item">'+ link.title +'</a>' )
					.appendTo( $header.navDropdownMenu) ;
			} );
		}

		return $header;
	}
}