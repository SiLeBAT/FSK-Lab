/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

class APPMTEditableDetails {
    constructor(settings, $container) {
        let O = this;
        // defaults maintable modal
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
        let O = this;
        return O._opts;
    }
    set opts(settings) {
        let O = this;
        O._opts = $.extend(true, {}, O.opts, settings);
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
        O._$modalNav = $('<div class="modal-body modal-nav card-header"></div>')
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
           // O._$navBar.append('<div class="col-divider order-2 d-block d-sm-none d-md-block"></div>');

            // nav search
            /*O._$navBar._$search = $('<input class="form-control form-control-plaintext search-input" type="search" placeholder="Search Details" aria-label="Search Details" />')
                .appendTo(O._$navBar)
                .attr('id', O._id + 'NavSearch')
                .wrap('<div class="col col-xxs-auto order-2 modal-nav-search"></div>')
                .wrap('<div class="search"></div>');
            */

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
	 * @param {*} _modelMetadata 
     * @param {*} _modelId 
	 */
	async _updateContent(_modelMetadata, _modelId) {
		let O = this;
        _log( 'PANEL MetaData / _updateContent' );
        
		// clear tab-panes
		O._$modalTabContent.html( '' );

		// get appropiate modelMetadata modelHandler for the model type.
		O._modelHandler = await O._getModelHandler( _modelMetadata );
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
     * @param {*} modelHandler 
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
                if(menuMeta.id == 'plot') return ;
                let $navItem = null;

                if (menuMeta.submenus && menuMeta.submenus.length > 0) {
                    $navItem = O._createNavItemDropdown(menuMeta)
                        .appendTo(O._$navBar._$nav);
                }
                else {
                    let $navItem = O._createNavItem(menuMeta)
                        .appendTo(O._$navBar._$nav);
                }
            })
        }
        //init collapsable td
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            var target = $(e.target).attr("href") // activated tab
            let targetTable = $('div'+target+'.tab-pane.h-100.active').find('table') ;
            //if not initialized
            if(targetTable.find('.td-collapse-toggle').length==0){
                _appUI._initTdCollapse( targetTable);
            }
        });
        
            
    }


    /**
     * POPULATE MODAL PANEL
     * @param {object} modelHandler
     */

    _populateModalPanel(modelHandler) {
        let O = this;
        _log('MODAL DETAILS / _populateModalPanel');
        _log(modelHandler);

        // create panels
        if (modelHandler && modelHandler._menu && modelHandler.panels) {
            // get each menus id
            $.each(modelHandler._menu, (i, menuMeta) => {
                // dropdown nav item 
                if (menuMeta.submenus && menuMeta.submenus.length > 0) {
                    // iterate over submenus
                    $.each(menuMeta.submenus, (j, submenuMeta) => {
                        // panel meta data exists in handler
                        if (submenuMeta.id in modelHandler.panels) {
                            O._preparePanel(submenuMeta, modelHandler, $(modelHandler.panels[submenuMeta.id].panel))
                                .appendTo(O._$modalTabContent);
                        }
                    });
                }
                // single nav item ? create panel
                else {
                    if (menuMeta.id && menuMeta.id != 'plot') {
                        if (menuMeta.id in modelHandler._panels) {
                            O._preparePanel(menuMeta, modelHandler, $(modelHandler._panels[menuMeta.id].panel))
                                .appendTo(O._$modalTabContent);
                            //O._createPanel(menuMeta, modelHandler)
                            //    .appendTo(O._$modalTabContent);
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
     * @param {object} handlerPanel: Panel to add elements to.
     */

    _preparePanel(menu, modelHandler, handlerPanel) {
        let O = this;
        _log('MODAL DETAILS / _createPanel: ' + menu.id);
        
        let $panel = null;
        if (modelHandler && menu.id) {
            let panelMeta = modelHandler._panels[menu.id];
            if (panelMeta.type) {
                // complex
                if (panelMeta.type == 'modelScript') {
                    $panel = O._createScriptPanel(menu, modelHandler);
                }
                // simple
                else if (panelMeta.type == 'visualizationScript') {
                    $panel = O._createScriptPanel(menu, modelHandler);
                }
                // plot
                else if (panelMeta.type == 'readme') {
                    $panel = O._createScriptPanel(menu, modelHandler);
                }else{
                    $panel = O._createPanelPan(menu, modelHandler, handlerPanel);
                }
            }
        }
        return $panel;
    }

    /**
     * CREATE PLOT PANEL
     * create plot tab-pane for specific menu
     * @param {array} menu
     * @param {object} modelHandler: object of class Model
     * @param {object} handlerPanel: Panel to add elements to.
     */

    _createPanelPan(menu, modelHandler, handlerPanel) {
        let O = this;
        _log('MODAL DETAILS / _createPlotPanel');

        // tab-pane
        let $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>')
            .attr('id', menu.id);

        if (modelHandler && menu.id ) {
            // get panel meta
            let panelMeta = modelHandler._panels[menu.id];

            // title
            $panel.append('<div class="panel-heading">' + menu.label + '</div>');
            handlerPanel.appendTo($panel);
        }

        return $panel;
    }

    /**
     * CREATE SCRIPT PANEL
     * create SCRIPT tab-pane for specific menu
     * @param {array} menu
     * @param {object} modelHandler: object of class Model
     */

    _createScriptPanel(menu, modelHandler) {
        let O = this;
        _log('MODAL DETAILS / _createScriptPanel');

        // tab-pane
        let $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>')
            .attr('id', menu.id);

        if (modelHandler && menu.id ) {
            // get panel meta
            let panelMeta = modelHandler._panels[menu.id];

            // title
            $panel.append('<div class="panel-heading">' + menu.label + '</div>');

            let $plot = $('<textarea row="6" class="form-control form-control-sm" />')
                .attr('id',menu.id+'Area')
                .appendTo($panel);
        }

        return $panel;
    }

    /**
     * GET MODEL HANDLER
     * returns model handler of class Model
     * @param {array} modelMetadata: metadata for specific id
     */

    async _getModelHandler(modelMetadata) {
        let O = this;
        _log('MODAL DETAILS / _getModelHandler');

        let modelHandler = null;

        if (modelMetadata) {

            // get plot image
            let imgUrl ;
            // get appropiate modelMetadata modelHandler for the model type.
            
            if (modelMetadata.modelType === 'genericModel') {
                modelHandler = new GenericModel(modelMetadata, imgUrl, true);
            }
            else if (modelMetadata.modelType === 'dataModel') {
                modelHandler = new DataModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'predictiveModel') {
                modelHandler = new PredictiveModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'otherModel') {
                modelHandler = new OtherModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'toxicologicalModel') {
                modelHandler = new ToxicologicalModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'doseResponseModel') {
                modelHandler = new DoseResponseModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'exposureModel') {
                modelHandler = new ExposureModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'processModel') {
                modelHandler = new ProcessModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'consumptionModel') {
                modelHandler = new ConsumptionModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'healthModel') {
                modelHandler = new HealthModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'riskModel') {
                modelHandler = new RiskModel(modelMetadata, imgUrl);
            }
            else if (modelMetadata.modelType === 'qraModel') {
                modelHandler = new QraModel(modelMetadata, imgUrl);
            }
            else {
                modelHandler = new GenericModel(modelMetadata, imgUrl, true);
            }
        }

        return modelHandler;
    }
}