/*

version: 1.0.0
author: sascha obermüller
date: 06.12.2020

*/

class APPTableMT extends APPTable {
	constructor ( settings, $container, metadata, uploadDates, executionTimes ) {
		// defaults maintable
		let tableSettings = $.extend( true, {}, {
			classes 		: 'table-main',
			ids 			: {
				table 			: 'mtGrid',
				thead 			: 'mtHead',
				tbody 			: 'mtRows',
				filter 			: 'mtFilter'
			},
			rowActions 		: [],
			rowSelectable 	: false, //'multiple', // 'single', // 
			wrapper			: 'card',
			on 				: {
				afterInit		: null,
				selectRow 		: null,
                selectAllRow    : null,
				deselectRow 	: null,
				updateFilter	: null
			}
		}, settings );
		super( tableSettings, $container, metadata, uploadDates, executionTimes  );
		
	}

	async _create () {
		let O = this;
		_log( 'TABLE MAIN / _create', 'primary' );
		
		O._facets = {}; // column facets
		O._filtered = []; // all hidden/filtered rows

		// current state of view
		O._view = 'default'; // card; //
		
		// loader
		O._loader = _appUI._createLoader( { classes : 'loader-page' }, O._$container );
		O._loader._setState( true );

		if(!O._metadata){
			await O._createData();
		}
        
        if(!O._uploadDates || O._uploadDates.length == 0){
            await O._createDataMetadata();
        }
        
        await O._prepareDataTable();
		// set loader
		O._loader._setState( false );


		// callback on create
		if ( $.isFunction( O.opts.on.create ) ) {
			O.opts.on.create.call( O, O );
		}

		if ( O._tableData ) {

			// create filter
			O._createFilter();

			// create table element
			O._$table = $( '<table class="table table-striped table-hover table-main"></table>' )
				.appendTo( O._$container )
				.attr( 'id', O.opts.ids.table );

			// wrapper
			if ( O.opts.wrapper ) {
				// card
				if ( O.opts.wrapper == 'card' ) {
					O._$table.add( O._$filter ).wrapAll( $( '<div class="card card-table-main overflow-hidden"></div>' ) );
					O._$table.wrap( '<div class="card-body p-0"></div>' );
					O._$filter.wrap( '<div class="card-header p-0"></div>' );
				}
			}

			// responsive table wrapper
			if ( O.opts.responsive ) {
				O._$table.wrap( '<div class="table-responsive"></div>' );
			}

			// card view toggle
			O.opts.showToggle ? O._$table.attr( 'data-show-toggle', O.opts.showToggle ) : null; // bs table / data-show-toggle for view: table/list

			// custom table attributes
			$.each( O.opts.attributes, ( attr, val ) => {
				O._$table.attr( attr, val );
			} );

			// toolbar
			O._createTableToolbar();

			// create table head
			O._$thead = O._createTableHead( O.opts.cols )
				.appendTo( O._$table );

			// create table head
			O._$tbody = O._createTableBody( O._tableData )
				.appendTo( O._$table );

			// populate table
			O._populateTable( O._tableData );

			_appUI._initTdCollapse( O._$table );

			// set counter
			O._updateFilter();
		}
		
		// tooltips
		_appUI._initTooltips( O._$container );
	}

	
	/**
	 * _refresh
	 * _refresh the table after editing.
	 * @param {string} query
	 */

	async _refresh(index, modelMetadata ) {
		let O = this;
		let rowData = O._tableData[index];
		rowData.modelMetadata = modelMetadata;
		let modelName = O._getData( modelMetadata, 'generalInformation', 'name' );
		rowData.el.find('td[data-label="Model"]' ).html( modelName );
		let software = O._getData( modelMetadata, 'generalInformation', 'software' );
		rowData.el.find('td[data-label="Software"]' ).html( software );
		let environment = O._getScopeData( modelMetadata, 'scope', 'product', 'productName' );
		rowData.el.find('td[data-label="Environment"]' ).html( environment );
		let hazard = O._getScopeData( modelMetadata, 'scope', 'hazard', 'hazardName' );
		rowData.el.find('td[data-label="Hazard"]' ).html( hazard );
		//let modelType = modelMetadata['modelType'];
		let modelType = modelMetadata['generalInformation']['modelCategory']['modelClass'] ?
			modelMetadata['generalInformation']['modelCategory']['modelClass'] : "Generic model";
		// special case: (Data) -> Data model
		if(modelType === "(Data)"){modelType = "Data model";}
		rowData.el.find('td[data-label="Type"]' ).html( modelType );
		// update sets
		if ( software ) O._updateSet( 'software', software );
		if ( environment ) {
			environment.forEach( x => { 
				O._updateSet( 'environment', x );
			} );
		}
		if ( hazard ) {
			hazard.forEach( x => { 
				O._updateSet( 'hazard', x );
			} );
		}
		if ( modelType ) O._updateSet( 'modelType', modelType );
	}
	/**
	 * CREATE DATA
	 * create tabledata
	 */
	 
	async _createData () {
		let O = this;
		_log( 'TABLE MAIN / _createData' );

		O._metadata = await _fetchData._json( window._endpoints.metadata ); //O._app._getMetadata();
		
		_log( O._tableData );
	}
    
    async _createDataMetadata () {
        let O = this;
        _log( 'TABLE MAIN / _createDataMetadata' );

        //O._uploadDates = await _fetchData._array( window._endpoints.uploadDate, O._metadata.length ); //O._app._getUploadDates( window._endpoints.uploadDate, O._metadata.length );
        //O._executionTimes = await _fetchData._array( window._endpoints.executionTime, O._metadata.length ); //O._app._getExecutionTimes( window._endpoints.executionTime, O._metadata.length );
        O._uploadDates = await _fetchData._json( window._endpoints.uploadDate); //O._app._getUploadDates( window._endpoints.uploadDate, O._metadata.length );
        O._executionTimes = await _fetchData._json( window._endpoints.executionTime); //O._app._getExecutionTimes( window._endpoints.executionTime, O._metadata.length );

        _log( O._tableData );
    }
	async _prepareDataTable(){
		_log( 'TABLE MAIN / _prepareDataTable', 'primary' );
		let O = this;
		// prepare table data
		O._tableData = [];

		for ( let i = 0; i < O._metadata.length; i++ ) {

			let modelMetadata = O._metadata[i]; // full metadata of model
			var identifier = modelMetadata['generalInformation']['identifier']
			let rowData = {
				modelMetadata 	: modelMetadata, // storess full model metadata for callbacks/hooks
				cells  			: [], // will contain raw cell value
				el 				: null  // will be added later in _populateTable
			}; // model data container for table output

			// create table data of models for output
			$.each( O.opts.cols, ( j, col ) => {

				let data = null;

				if ( col.field == 'modelName' ) {
					data = O._getData( modelMetadata, 'generalInformation', 'name' );
				}
				else if ( col.field == 'software' ) {
					data = O._getData( modelMetadata, 'generalInformation', 'software' );
				}
				else if ( col.field == 'hazard' ) {
					data = O._getScopeData( modelMetadata, 'scope', 'hazard', 'hazardName' );
					// if formatter is not list join array
					if ( col.formatter != '_list' ) {
						data = Array.from( data ).join( ' ' );
					}
					// let joiner = col.formatter == 'list' ? '||' ' ';
					// data = Array.from( data ).join( '' );
				}
				else if ( col.field == 'environment' ) {
					data = O._getScopeData( modelMetadata, 'scope', 'product', 'productName' );
					// if formatter is not list join array
					if ( col.formatter != '_list' ) {
						data = Array.from( data ).join( ' ' );
					}
				}
				else if ( col.field == 'modelType' ) {
					data = modelMetadata['generalInformation']['modelCategory']['modelClass'] ?
					modelMetadata['generalInformation']['modelCategory']['modelClass'] : "Generic model";
					// special case: (Data) -> Data model
					if(data === "(Data)"){data = "Data model";}
				}
				else if ( col.field == 'executionTime' ) {
					data = O._executionTimes[identifier]? O._executionTimes[identifier] :"";
				}
				else if ( col.field == 'uploadDate' ) {
					data = O._uploadDates[identifier]? O._uploadDates[identifier] :"";
				}

				rowData.cells.push( data );
			} );

			O._tableData.push( rowData );
		}
		_log( O._tableData);
	}

	/**
	 * CREATE FILTER
	 * @param
	 */

	_createFilter () {
		let O = this;
		_log( 'TABLE MAIN / _createFilter' );

		// prepare sets for filter
		O._sets = O._sets || {};
		O._sets.software = new Set();
		O._sets.environment = new Set();
		O._sets.hazard = new Set();
		O._sets.modelType = new Set();

		for ( let i = 0; i < O._metadata.length; i++ ) {
			
			let modelMetadata = O._metadata[i];
			let software = O._getData( modelMetadata, 'generalInformation', 'software' );
			let environment = O._getScopeData( modelMetadata, 'scope', 'product', 'productName' );
			let hazard = O._getScopeData( modelMetadata, 'scope', 'hazard', 'hazardName' );
			//let modelType = modelMetadata['modelType'];
			let modelType = _modelMetadata['generalInformation']['modelCategory']['modelClass'] ? 
				_modelMetadata['generalInformation']['modelCategory']['modelClass'] : "Generic model";//_modelMetadata2['modelType'];
			// special case: (Data) -> Data model
			if(modelType === "(Data)"){modelType = "Data model";}

			// update sets
			if ( software ) O._updateSet( 'software', software );
			if ( environment ) {
				environment.forEach( x => { 
					O._updateSet( 'environment', x );
				} );
			}
			if ( hazard ) {
				hazard.forEach( x => { 
					O._updateSet( 'hazard', x );
				} );
			}
			if ( modelType ) O._updateSet( 'modelType', modelType );
		}

		// create table element
		O._$filter = $( '<div class="filter">' )
			.attr( 'id', O.opts.ids.filter )
			.appendTo( O._$container );

		// navbar
		let $navbar = $( '<nav class="navbar navbar-expand-lg row justify-content-between"></nav>' )
			.appendTo( O._$filter );

		// navbar toggle
		let $navbarToggle = $( '<button class="action action-pure collapsed" type="button" data-tooltip data-toggle="collapse" data-target="#mainTableFilterFacets" aria-controls="mainTableFilter" aria-expanded="false" title="Toggle Filter" aria-label="Toggle Filter"><span class="feather icon-sliders"></span></button>' );
		$navbarToggle
			.appendTo( $navbar )
			.wrap( '<div class="navbar-toggler col-auto order-1 filter-toggler"></div>' );

		// divider 1
		$navbar.append( '<div class="col-divider order-2 d-block d-lg-none"></div>' );

		// filter search
		O._$search = $( '<input id="mainTableFilterSearch" class="form-control form-control-plaintext search-input" type="search" placeholder="Search Models" aria-label="Search Models" />' )
			.appendTo( $navbar )
			.wrap( '<div class="col col-xxs-auto order-3 filter-search">' )
			.wrap( '<div class="search w-100"></div>' );

		// search clear button
		O._$searchClear = $( '<button class="search-clear" data-clear="#mainTableFilterSearch"><i class="feather icon-x"></i></button>' )
			.insertAfter( O._$search ); 
		
		// custom search
		O._$search.on( 'keyup, change', ( event ) => {
			// get the query by updating filter
			O._updateFilter();
		} );

		// divider 2
		$navbar.append( '<div class="col-divider order-4 d-none d-xxs-block"></div>' );

		// facets
		O._$facets = $( '<div id="mainTableFilterFacets" class="collapse navbar-collapse row mt-1 mt-lg-0 facets"></div>' );
		O._$facets
			.appendTo( $navbar )
			.wrap( '<div class="col-12 col-lg-8 order-5 order-xss-6 order-lg-5 filter-facets">' );
		// create all facets of cols
		$.each( O.opts.cols, ( i, col ) => {
			if ( col.field && col.facet ) {
				let $facet = $( '<select class="form-control form-control-sm" style="" multiple="multiple"></select>' ); 
				$facet
					.appendTo( O._$facets ); 

				// crate facet wrapper
				let $facetWrapper = $( '<div class="col-12 col-xs-6 col-md-3 facet"></div>' );
				col.facet.tooltip && col.facet.placeholder ? $facetWrapper.attr( 'data-tooltip', col.facet.tooltip ) : null;
				col.facet.tooltip && col.facet.placeholder ? $facetWrapper.attr( 'title', 'Filter by '+ col.facet.placeholder ) : null;

				$facet.wrap( $facetWrapper );

				// set unique id
				if ( col.id ) {
					$facet.id = col.id +'Facet';
				}
				else {
					$facet.id = 'facet-'+ $.now();
				}
				
				// facet attributes
				$facet.attr( 'id', $facet.id );
				col.facet.select2 ? $facet.attr( 'data-sel2', col.facet.select2 ) : null;
				col.facet.select2SingleRow ? $facet.attr( 'data-sel2-choice-single-row', col.facet.select2SingleRow ) : null;
				col.facet.placeholder ? $facet.attr( 'data-placeholder', col.facet.placeholder ) : null;
				col.facet.maxSelectable ? $facet.attr( 'data-maximum-selection-length', col.facet.maxSelectable ) : null;
				
				// create action on filter facet
				$facet.on( 'change', ( event ) => {
					O._updateFilter();
				} );

				O._facets[col.field] = {
					el 		: $facet 
				};

				// populate facet select
				if ( col.id && O._sets[col.field] ) {
					_appUI._populateSelect( $facet, O._sets[col.field] );
				}

			}
		} ); 

		// facets clear
		let $facetsClear = $( '<button class="action action-pure" data-tooltip title="Clear Filter"><i class="feather icon-x"></i></button>' )
			.appendTo( O._$facets )
			.wrap( '<div class="facets-clear"></div>' );

		// set clear options of facets clear
		let facetsIds = [];
		$.each( O._facets, ( i, item ) => {
			facetsIds.push( '#'+ item.el.id );
		} );
		$facetsClear.attr( 'data-clear', facetsIds.join( ',' ) );

		// init facets functions
		_appUI._initClear( O._$navbar );
		_appUI._initSelect2( O._$facets );

		// result counter
		O._$counter = $( '<small id="mainTableCounter"></small>' )
			.appendTo( $navbar )
			.wrap( '<div class="col-auto align-items-center order-6 order-xxs-4 order-lg-6 mt-2 mt-xxs-0 filter-counter">' );
	}

	/**
	 * SET COUNTER
	 * set main table result counter
	 * @param {boolean} state: false=hide, true=show
	 */

	_updateCounter () {
		let O = this;

		let counterText = '';

		// filtered items?
		if( O._tableData.length - O._filtered.length > 0 ) {
			counterText = O._filtered.length +'/'+ O._tableData.length
		}
		else {
			counterText = O._filtered.length;
		}
		// append default label text
		counterText += ' models';

		// set counter text to elem
		counterText && O._$counter ? O._$counter.html( counterText ) : null;
	}


	/**
	 * SEARCH
	 * search on enpoint for query
	 * @param {string} query
	 */

	async _search( query ) {
		let O = this;
		_log( 'TABLE MAIN / _search : '+ query, 'secondary' );

		O._loader._setState( true );

		// fetch result from endpoint
		let result = await _fetchData._json( window._endpoints.search, query);

		O._highlight( query );

		O._loader._setState( false );

		return result;
	}


	/**
	 * HIGHLIGHT
	 * highlight text matching query
	 * @param {string} query
	 */

	_highlight( query ) {
		let O = this;

		if( query && query.length > 0 && query != '%20' ) {

			$.each( O._tableData, ( rowIndex, rowData ) => {
				rowData.el.find( 'td' ).each( ( j, td ) => {
					let $td = $( td );
					if( ! $td.is( '.td-actions' ) ) {
						let value = $td.html();
						value = _formatter._searchHighlight( value, query );

						$td.html( value );
					}
				} );
			} );
		}
	}


	/**
	 * UPDATE FILTER
	 * updates all filter and counter
	 */

	async _updateFilter() {
		let O = this;
		_log( 'TABLE MAIN / _updateFilter' );

		O._filtered = []; // stores all hidden/filtered rows

		if( O._$filter ) {
			// clear on update
			// remove all highlighting
			O._$table.find( 'mark' ).contents().unwrap();

			// search 
			let searchQuery = ( O._$search.val() == undefined || O._$search.val() == '' ) ? '%20' : O._$search.val().trim().toLowerCase();
			let searchResult = [];
			
			if( searchQuery ) {
				searchResult = await O._search( searchQuery );
			}

			// check each row for matchings
			$.each( O._tableData, ( rowIndex, rowData ) => {

				// initale state for each row 
				// match = true : will be shown
				let rowMatchesFilter = true;

				// if search result is empty, no match was found
				if( searchResult.length == 0) {
					rowMatchesFilter = false;
				} 
				// filter by search
				if( searchResult.length > 0 
					&& ! searchResult.includes( rowIndex ) ) {
					rowMatchesFilter = false;
				}

				// filter by cols
				if( rowMatchesFilter ) { 
					$.each( O._facets, ( field, facet ) => {

						let facetValue = facet.el.val();
						if ( $.isArray( facetValue ) && facetValue.length > 0 ) {

							// get according col index
							let colIndex = super._getColIndexByField( field );
							let cellData = rowData.cells[colIndex];

							if( cellData instanceof Set ) {

								let cellMatches = false;

								$.each( facetValue, ( i, val ) => {
									// check set for matching one of the facet values
									//cellData.has( val ) ? cellMatches = true : null;
									for (const element of cellData) {
										element.includes(val.trim()) ? cellMatches = true : null;
									}
								} );

								if( ! cellMatches ) {
									rowMatchesFilter = false;
								}
							}
							else {
								if ( ! cellData.includes( facetValue ) ) {
									// row data does not match
									rowMatchesFilter = false;
								}							
							}
						}
					} );
				}

				// _log( 'row '+ rowIndex + ' > '+ rowMatchesFilter );
				if( rowMatchesFilter && ! O._filtered.includes( rowIndex ) ){
					O._filtered.push( rowIndex ); // row matches all filter facets citeria
				}

				// apply filter class on each row
				if ( rowMatchesFilter ) {
					rowData.el.removeClass( 'tr-hidden' );
				}
				else {
					rowData.el.addClass( 'tr-hidden' );
				}

			} );

			O._updateStripes();

			// TO DO 
			// re-populate facets with visible sets only ?

			_log( O._filtered );

			// callback
			if ( $.isFunction( O.opts.on.updateFilter ) ) {
				O.opts.on.updateFilter.call( O, O, O._filtered );
			}

			O._updateCounter();
		}
	}


	/**
	 * GET SET
	 * @param {string} name: name of the set 
	 */
	 
	_getSet( name ) {
		let O = this;
		// _log( 'TABLE MT / _getSet' );

		if ( name in O._sets ) {
			return O._sets.name
		}
		else {
			return O._sets;
		}
	}


	/**
	 * UPDATE SET
	 * @param {string} name: name of the set 
	 * @param {string} data: new data 
	 */

	_updateSet( name, data ) {
		let O = this;
		 _log( 'TABLE MT / _updateSet' );
		if ( name ) {
			O._sets[name].add( data ) ;
		}
	}


	/**
	 * GET DATA
	 * get a metadata property or return empty string if missing.
	 * @param {object} modelMetadata: whole metadata of a model
	 * @param {string} toplevel: name of the metadata component. it can be
	 *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
	 * @param {string} name: name of the metadata property 
	 */

	_getData( modelMetadata, toplevel, name ) {
		let O = this;
		// _log( 'TABLE MT / _getData' );

		try {
			return modelMetadata[toplevel][name];
		}
		catch (err) {
			return 'no information for ' + name;
		}
	}


	/**
	 * GET SCOPEDATA
	 * get metadata property or return empty string if missing.
	 * @param {object} modelMetadata: whole metadata of a model
	 * @param {string} toplevel: name of the metadata component. It can be
	 *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
	 * @param {string} sublevel: Name of metadata comonent like *product*, *hazard*
	 * @param {string} name: name of the metadata property 
	 */

	_getScopeData( modelMetadata, toplevel, sublevel, name ) {
		let O = this;
		// _log( 'TABLE MT / _getScopeData' );

		try {
			let subs = modelMetadata[toplevel][sublevel];
			names = new Set();
			subs.forEach( ( it ) => {
				let element = it[name];
				if ( !element )
					element = it['name'];
				names.add(element);
			} );
			return names;
		}
		catch (err) {
			return new Set().add( 'no information' );
		}
	}
	
	/**
     * GET COL INDEX BY FIELD 
	 * (adjusted for checkbox column which add +1 column)
     * returns col index by field identifier
     * @param {string} field name of the column
     */

    _getColIndexByField(field) {
        let O = this;
        console.log("_getColIndexByField child");
        let colIndex = -1;
        $.each(O.opts.cols, (i, col) => {
            if (field == col.field) {
                colIndex = i+1;
            }
        });

        return colIndex;
    } 
}