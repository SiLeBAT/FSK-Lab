/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

class APPMTDetails {
    constructor(settings, $container) {
        let O = this;
        // defaults maintable simulations modal
        O._$modalContent = $container;
        O._opts = $.extend(true, {}, {
            classes: '',
            data: null,
            on: {
                afterInit: null, // function
                show: (O, event) => {
                    O._updateModal(event);
                }, // function
                hide: null // function
            }
        }, settings);
        O._create();
    }
    get opts() {
        return this._opts;
    }
    set opts(settings) {
        this._opts = $.extend(true, {}, this.opts, settings);
    }
    /**
     * CREATE
     * calls super class and sets _metadata
     */

    _create() {
        let O = this;
        _log('MODAL DETAILS / _create', 'primary');

        O._metadata = O.opts.data;    
    }
    /**
     * CREATE MODAL
     * creates basic modal components: header and blank body
     */

    _createModelMetadataContent() {
        let O = this;
		_log( 'MODAL DETAILS / _createModelMetadataContent' );
        // modal nav with tabs & search
        O._$modalNav = $('<div class="modal-body modal-nav"></div>')
            .appendTo(O._$modalContent);

        O._navId = O._id + 'Nav';
        if (!O._$navBar) {
            O._$navBar = $('<nav class="navbar navbar-expand-sm row justify-content-start justify-content-md-between"></nav>')
                .appendTo(O._$modalNav);

            // nav toggle
            let $navToggle = $('<button class="action action-pure mt-1 mb-1" type="button" data-toggle="collapse" aria-expanded="false" aria-label="Toggle navigation"><i class="feather icon-list"></i></button>')
                .appendTo(O._$navBar)
                .attr('data-target', '#' + O._navId)
                .attr('aria-controls', O._navId)
                .wrap('<div class="col-auto navbar-toggler order-1 modal-nav-toggler"></div>');

            // divider
            O._$navBar.append('<div class="col-divider order-2 d-block d-sm-none d-md-block"></div>');

            // nav search
            O._$navBar._$search = $('<input class="form-control form-control-plaintext search-input" type="search" placeholder="Search Details" aria-label="Search Details" />')
                .appendTo(O._$navBar)
                .attr('id', O._id + 'NavSearch')
                .wrap('<div class="col col-xxs-auto order-2 modal-nav-search"></div>')
                .wrap('<div class="search"></div>');


            // TO DO
            // search functionality


            // nav tabs
            O._$navBar._$nav = $('<ul class="nav nav-pointer pt-1 pt-md-0"></ul>')
                .appendTo(O._$navBar)
                .wrap('<div class="col-12 col-md-auto order-3 order-md-1 modal-nav-menu order-4"></div>')
                .wrap('<div class="collapse navbar-collapse" id="' + O._navId + '"></div>');
        }

        // modal body
        O._createModalBody();
        O._$modalBody.addClass('p-0 modal-table');

        // content container
        O._$modalTabContent = $('<div class="tab-content h-100"></div>')
            .appendTo(O._$modalBody);
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
	 * BUILD PANEL
	 * build PANEL content
	 * @param {event} event 
	 */
	 
	async _updateContent(_modelMetadata, _modelId) {
		let O = this;
        _log( 'PANEL MetaData / _updateContent' );
        
		// clear tab-panes
		O._$modalTabContent.html( '' );

		// get appropiate modelMetadata modelHandler for the model type.
		O._modelHandler = await O._getModelHandler( _modelMetadata , _modelId);
		// populate nav
		O._populateModalNav( O._modelHandler, O._$navBar._$nav );

		// populate panel
		O._populateModalPanel( O._modelHandler );

		// activate first pane
		O._$navBar._$nav.find( '.nav-link' ).first().addClass( 'active' );
		O._$modalTabContent.find( '.tab-pane' ).first().addClass( 'active' );

    }
    

    /**
     * POPULATE MODAL MENU
     * @param {object} Model
     */

    _populateModalNav(modelHandler) {
        let O = this;
        _log('MODAL DETAILS / _populateModalNav');
        _log(modelHandler);

        // clear nav
        O._$navBar._$nav.html('');

        // create nav items
        if (modelHandler && modelHandler._menu) {

            $.each(modelHandler._menu, (i, menuMeta) => {
				if ( menuMeta.id == 'resources')
					return; 

                let $navItem = null;

                if (menuMeta.submenus && menuMeta.submenus.length > 0) {
                    $navItem = O._createNavItemDropdown(menuMeta)
                        .appendTo(O._$navBar._$nav);
                }
                else {
                    //if(menuMeta.id !== 'modelScript' && menuMeta.id !== 'visualizationScript' && menuMeta.id !== 'readme'){
                        let $navItem = O._createNavItem(menuMeta)
                            .appendTo(O._$navBar._$nav);
                    //}
                }
            })
        }
    }


    /**
     * POPULATE MODAL PANEL
     * @param {object} Model
     */

    _populateModalPanel(modelHandler) {
        let O = this;
        _log('MODAL DETAILS / _populateModalPanel');
        _log(modelHandler);

        // create panels
        if (modelHandler && modelHandler._menu && modelHandler._panels) {
            // get each menus id
            $.each(modelHandler._menu, (i, menuMeta) => {
                // dropdown nav item 
                if ( menuMeta.id == 'resources')
					return;
                if (menuMeta.submenus && menuMeta.submenus.length > 0) {
                    // iterate over submenus
                    $.each(menuMeta.submenus, (j, submenuMeta) => {
                        // panel meta data exists in handler
                        if (submenuMeta.id in modelHandler._panels) {
                            O._createPanel(submenuMeta, modelHandler)
                                .appendTo(O._$modalTabContent);
                        }
                    });
                }
                // single nav item ? create panel
                else {
                    if (menuMeta.id) {
                        if (menuMeta.id in modelHandler._panels) {
                            //if(menuMeta.id !== 'modelScript' && menuMeta.id !== 'visualizationScript' && menuMeta.id !== 'readme'){
                                O._createPanel(menuMeta, modelHandler)
                                    .appendTo(O._$modalTabContent);
                            //}
                        }
                    }
                }
            });
        }
    }


    /**
     * CREATE NAV ITEM DROPDOWN
     * @param {array} menuMeta: array of dropdown-items width 'id' and 'label'
     */

    _createNavItemDropdown(menuMeta) {
        let O = this;
        _log('MODAL DETAILS / _createTabNavItemDropdown: ' + menuMeta.label);

        let $navItem = $('<li class="nav-item dropdown"></li>');

        let $navLink = $('<a class="nav-link dropdown-toggle" role="button">' + menuMeta.label + '</a>')
            .attr('href', '#')
            .attr('aria-haspopup', true)
            .attr('aria-expanded', false)
            .attr('data-toggle', 'dropdown')
            .appendTo($navItem);
        let $dropdown = $('<div class="dropdown-menu"></div>')
            .appendTo($navItem);

        $.each(menuMeta.submenus, (i, submenuMeta) => {

            let $dropdownItem = $('<a class="dropdown-item" role="button">' + submenuMeta.label + '</a>')
                .attr('href', '#' + submenuMeta.id)
                .attr('aria-controls', '#' + submenuMeta.id)
                .attr('data-toggle', 'tab')
                .appendTo($dropdown);
        });

        return $navItem;
    }


    /**
     * CREATE NAV ITEM
     * @param {array} menuMeta
     */

    _createNavItem(menuMeta) {
        let O = this;
        _log('MODAL DETAILS / _createNavItem: ' + menuMeta.label);

        let $navItem = $('<li class="nav-item"></li>');
        let $navLink = $('<a class="nav-link" role="button">' + menuMeta.label + '</a>')
            .attr('href', '#' + menuMeta.id)
            .attr('aria-controls', '#' + menuMeta.id)
            .attr('data-toggle', 'tab')
            .appendTo($navItem);

        return $navItem;
    }


    /**
     * CREATE PANEL
     * create tab-pane for specific menu by selecting type and calling specific creation (simple, complex, plot)
     * @param {array} menu
     * @param {object} modelHandler: object of type Model
     */

    _createPanel(menu, modelHandler) {
        let O = this;
        _log('MODAL DETAILS / _createPanel: ' + menu.id);

        let $panel = null;
        if (modelHandler && menu.id) {

            let panelMeta = modelHandler._panels[menu.id];
            // panel type
            if (panelMeta.type) {

                // complex
                if (panelMeta.type == 'complex') {
                    $panel = O._createComplexPanel(menu, modelHandler);
                }
                // simple
                else if (panelMeta.type == 'simple') {
                    $panel = O._createSimplePanel(menu, modelHandler);
                }
                // plot
                else if (panelMeta.type == 'plot') {
                    $panel = O._createPlotPanel(menu, modelHandler);
                }
                // Model Script
				else if( panelMeta.type == 'modelScript' ) {
					$panel = O._createModelScriptPanel( menu, modelHandler );
				}
				// Visualization Script
				else if( panelMeta.type == 'visualizationScript' ) {
					$panel = O._createVisualizationScriptPanel( menu, modelHandler );
				}
                // readme
                else if( panelMeta.type == 'readme' ) {
                    $panel = O._createReadmetPanel( menu, modelHandler );
                }

                
            }
        }
        
        return $panel;
    }

    /**
     * CREATE SIMPLE PANEL
     * create simple tab-pane for specific menu
     * table has property, value cols
     * @param {array} menu
     * @param {object} modelHandler: object of type Model
     */

    _createSimplePanel(menu, modelHandler) {
        let O = this;
        _log('MODAL DETAILS / _createSimplePanel: ' + menu.id);

        // tab-pane
        let $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>')
            .attr('id', menu.id);

        if (modelHandler && menu.id) {
            // get panel meta
            let panelMeta = modelHandler._panels[menu.id];

            // title
            $panel.append('<div class="panel-heading">' + menu.label + '</div>');

            // table settings
            let tableSettings = {
                cols: [
                    {
                        label: 'Property',
                        field: 'property',
                        classes: {
                            th: null,
                            td: 'td-label min-200'
                        },
                        sortable: true,
                        switchable: false
                    },
                    {
                        label: 'Value',
                        field: 'value',
                        sortable: false,
                        switchable: false
                    }
                ],
                tableData: [],
                responsive: true,
                showToggle: true
            };

            // set table row data
            if (panelMeta.metadata && panelMeta.schema) {
                $.each(panelMeta.schema, (j, prop) => {
                    let rowData = {
                        cells: []
                    };
                    // cell 1 label
                    rowData.cells.push(prop.label);
                    // cell 2 val
                    let data = panelMeta.metadata[prop.id];
                    data = _checkUndefinedContent(data);
                    rowData.cells.push(data);

                    tableSettings.tableData.push(rowData);
                });
            }

            // create table
            let panelTable = new APPTable(tableSettings, $panel);
            $panel.data('table', panelTable);
        };

        return $panel;
    }


    /**
     * CREATE COMPLEX PANEL
     * create complex tab-pane for specific menu
     * table has in metadata and schema defined cols
     * @param {array} menu
     * @param {object} modelHandler: object of class Model
     */

    _createComplexPanel(menu, modelHandler) {
        let O = this;
        _log('MODAL DETAILS / _createComplexPanel: ' + menu.id);

        // tab-pane
        let $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>')
            .attr('id', menu.id);

        if (modelHandler && menu.id) {
            // get panel meta
            let panelMeta = modelHandler._panels[menu.id];

            // title
            $panel.append('<div class="panel-heading">' + menu.label + '</div>');

            // table settings
            let tableSettings = {
                cols: [],
                tableData: [],
                responsive: true,
                showToggle: true
            };

            // set table cols
            $.each(panelMeta.schema, (i, prop) => {
                tableSettings.cols.push(
                    {
                        label: prop.label,
                        field: prop.id,
                        sortable: true,
                        switchable: true
                    }
                )
            });

            // set table row data
            if (panelMeta.metadata && panelMeta.schema) {
                $.each(panelMeta.metadata, (i, item) => {
                    // row each item
                    let rowData = {
                        cells: []
                    };
                    // cells
                    $.each(panelMeta.schema, (j, prop) => {
                        let data = item[prop.id];
                        data = _checkUndefinedContent(data);
                        // cell each prop
                        rowData.cells.push(data);
                    });

                    tableSettings.tableData.push(rowData);
                });
            }

            // create table
            let panelTable = new APPTable(tableSettings, $panel);
            $panel.data('table', panelTable);
        };
        return $panel;
    }

/**
	 * CREATE PLOT PANEL
	 * create plot tab-pane for specific menu
	 * @param {array} menu
	 * @param {object} modelHandler: object of class Model
	 */

	_createPlotPanel ( menu, modelHandler ) {
		let O = this;
		_log( 'MODAL DETAILS / _createPlotPanel' );

		// tab-pane
		let $panel = $( '<div class="tab-pane h-100" role="tabpanel"></div>' )
			.attr( 'id', menu.id );

		if( modelHandler && menu.id && modelHandler._img ) {
			// get panel meta
			let panelMeta = modelHandler._panels[menu.id];

			// title
			$panel.append( '<div class="panel-heading">'+ menu.label +'</div>' );

			let $plot = $( '<figure class="figure"><svg width = "100%" height = "1500" >"'+ modelHandler._img +'" </svg></figure>' )
				.appendTo( $panel )
				.wrap( '<div class="panel-plot"></div>' );

		}

		return $panel;
	}
	/**
	 * CREATE Model Script PANEL
	 * create Model Script tab-pane for specific menu
	 * @param {array} menu
	 * @param {object} modelHandler: object of class Model
	 */

	_createModelScriptPanel ( menu, modelHandler ) {
		let O = this;
		_log( 'MODAL DETAILS / _createPlotPanel' );

		// tab-pane
		let $panel = $( '<div class="tab-pane h-100" role="tabpanel"></div>' )
			.attr( 'id', menu.id );

		if( modelHandler && menu.id && modelHandler._modelScript ) {
			// get panel meta
			let panelMeta = modelHandler._panels[menu.id];


			// title
			$panel.append( '<div class="panel-heading">'+ menu.label +'</div>' );
			let $script = $( '<pre class="precss"></pre>' )
				.appendTo( $panel )
				.wrap( '<div class="panel-plot"></div>' );

			var lines = modelHandler._modelScript.split("\n");
			for(var i = 0; i < lines.length; i++) {
				$( '<span class="line">'+lines[i]+'</span>' )
				.appendTo( $script )
			}

		}

		return $panel;
	}
	/**
	 * CREATE VISUALIZATION SCRIPT
	 * create plot tab-pane for specific menu
	 * @param {array} menu
	 * @param {object} modelHandler: object of class Model
	 */


	_createVisualizationScriptPanel ( menu, modelHandler ) {
		let O = this;
		_log( 'MODAL DETAILS / _createPlotPanel' );

		// tab-pane
		let $panel = $( '<div class="tab-pane h-100" role="tabpanel"></div>' )
			.attr( 'id', menu.id );

		if( modelHandler && menu.id && modelHandler._visScript ) {
            // get panel meta
            _log('ifffff visualizationScript: ' + modelHandler._visScript );
			let panelMeta = modelHandler._panels[menu.id];

			// title
			$panel.append( '<div class="panel-heading">'+ menu.label +'</div>' );
			let $script = $( '<pre class="precss"></pre>' )
				.appendTo( $panel )
				.wrap( '<div class="panel-plot"></div>' );

			var lines = modelHandler._visScript.split("\n");
			for(var i = 0; i < lines.length; i++) {
				$( '<span class="line">'+lines[i]+'</span>' )
				.appendTo( $script )
			}

		}

		return $panel;
	}
    
    _createReadmetPanel ( menu, modelHandler ) {
        let O = this;
        _log( 'MODAL DETAILS / _createPlotPanel' );

        // tab-pane
        let $panel = $( '<div class="tab-pane h-100" role="tabpanel"></div>' )
            .attr( 'id', menu.id );

        if( modelHandler && menu.id && modelHandler.readme ) {
            // get panel meta
            _log('ifffff visualizationScript: ' + modelHandler.readme );
            let panelMeta = modelHandler._panels[menu.id];

            // title
            $panel.append( '<div class="panel-heading">'+ menu.label +'</div>' );
            let $script = $( '<pre class="precss"></pre>' )
                .appendTo( $panel )
                .wrap( '<div class="panel-plot"></div>' );

            var lines = modelHandler.readme.split("\n");
            for(var i = 0; i < lines.length; i++) {
                $( '<span class="line">'+lines[i]+'</span>' )
                .appendTo( $script )
            }

        }

        return $panel;
    }
	/**
	 * GET MODEL HANDLER
	 * returns model handler of class Model
	 * @param {array} modelMetadata: metadata for specific id
	 */

	async _getModelHandler ( modelMetadata, modelId ) {
		let O = this;
		_log( 'MODAL DETAILS / _getModelHandler' );

		let modelHandler = null;

		if( modelMetadata ) {
			// get plot image
            //never create a local file via _blob as this will not work for Knime server if used in node
            let imgUrl = await _fetchData._content( window._endpoints.image , modelMetadata.generalInformation.identifier );// O._app._getImage( modelMetadata.generalInformation.identifier );
            let modelScript;
            let visScript;
			if(!modelMetadata.modelscript && modelMetadata.modelscript!=""){
               modelScript = await _fetchData._content( window._endpoints.modelscriptEndpoint, modelId );// O._app._getImage( modelMetadata.generalInformation.identifier );
            }else{
               modelScript = modelMetadata.modelscript;
            }
            if(!modelMetadata.visualization && modelMetadata.visualization!=""){
               visScript = await _fetchData._content( window._endpoints.visualizationscriptEndpoint, modelId );// O._app._getImage( modelMetadata.generalInformation.identifier );
            }else{
               visScript = modelMetadata.visualization;
            }
            if(!modelMetadata.readme){
               readme = await _fetchData._content( window._endpoints.readmeEndpoint, modelId );// O._app._getImage( modelMetadata.generalInformation.identifier );
            }else{
               readme = modelMetadata.readme;
            }
            // get appropiate modelMetadata modelHandler for the model type.
			if ( modelMetadata.modelType === 'genericModel' ) {
				modelHandler = new GenericModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'dataModel' ) {
				modelHandler = new DataModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'predictiveModel' ) {
				modelHandler = new PredictiveModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'otherModel' ) {
				modelHandler = new OtherModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'toxicologicalModel' ) {
				modelHandler = new ToxicologicalModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'doseResponseModel' ) {
				modelHandler = new DoseResponseModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'exposureModel' ) {
				modelHandler = new ExposureModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'processModel' ) {
				modelHandler = new ProcessModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'consumptionModel' ) {
				modelHandler = new ConsumptionModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'healthModel' ) {
				modelHandler = new HealthModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'riskModel' ) {
				modelHandler = new RiskModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else if ( modelMetadata.modelType === 'qraModel' ) {
				modelHandler = new QraModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
			else {
				modelHandler = new GenericModel( modelMetadata, imgUrl, false,  modelScript, visScript , readme ) ;
			}
		}

		return modelHandler;
	}
}