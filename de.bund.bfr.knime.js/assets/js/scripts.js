var _appVars = {
	header			: {
		brand			: {
			logo			: 'assets/img/bfr_logo.gif', // false
			title			: 'FSK-Web Landing Page Test' // false or ''
		},
		nav				: [
			{
				title		: 'Infos',
				href		: '#'
			},
			{
				title		: 'Imprint',
				href		: '#'
			},
			{
				title		: 'Privacy Policy',
				href		: '#'
			}
		]
	},
	mainTable 			: {
		cols 			: [
			{
				id 			: 'colModel',
				label		: 'Model',
				field 		: 'modelName',
				classes 	: {
					th 			: 'td-label min-200',
					td 			: 'td-label min-200 td-model'
				},
				sortable 	: true, // sortable
			},
			{
				id 			: 'colSoftware',
				label		: 'Software',
				field 		: 'software',
				classes 	: {
					th 			: null,
					td 			: 'td-soft'
				},
				sortable 	: true, // sortable
				facet 		: {
					tooltip 			: true,
					select2 			: true,
					select2SingleRow 	: true,
					placeholder 		: 'Software',
					maxSelectable 		: 1
				}
			},
			{
				id 			: 'colEnvironment',
				label		: 'Environment',
				field 		: 'environment',
				classes 	: {
					th 			: 'min-300',
					td 			: 'td-env min-300'
				},
				sortable 	: true, // sortable
				facet 		: {
					tooltip 			: true,
					select2 			: true,
					select2SingleRow 	: true,
					placeholder 		: 'Environment',
					maxSelectable 		: 1
				},
				collapsable	: true, // data-toggle-td
				formatter 	: '_list' // _formatter subroutine
			},
			{
				id 			: 'colHazard',
				label		: 'Hazard',
				field 		: 'hazard',
				classes 	: {
					th 			: null,
					td 			: 'td-haz'
				},
				sortable 	: true, // sortable
				facet 		: {
					tooltip 			: true,
					select2 			: true,
					select2SingleRow 	: true,
					placeholder 		: 'Hazard',
					maxSelectable 		: 1
				}
			},
			{
				id 			: 'colType',
				label		: 'Type',
				field 		: 'modelType',
				classes 	: {
					th 			: null,
					td 			: 'td-type'
				},
				sortable 	: true, // sortable
				facet 		: {
					tooltip 			: true,
					select2 			: true,
					select2SingleRow 	: true,
					placeholder 		: 'Type',
					maxSelectable 		: 1
				}
			},
			{
				id 			: 'colExecTime',
				label		: 'Execution Time',
				field 		: 'executionTime',
				classes 	: {
					th 			: null,
					td 			: null
				},
				sortable 	: true, // sortable
				sorter 		: '_execution', // _sorter subroutine
				formatter 	: '_execution' // _formatter subroutine
			},
			{
				id 			: 'colUploadDate',
				label		: 'Upload Date',
				field 		: 'uploadDate',
				classes 	: {
					th 			: null,
					td 			: null
				},
				sortable 	: true, // sortable
				formatter 	: '_uploadDate' // _formatter subroutine
			}
		],
		rowActions 		: [
			{
				type 			: 'modal',
				idPrefix 		: 'mtActionDetails_',
				icon			: 'icon-eye',
				title 			: 'Details',
				target			: '#mtModalDetails'
			},
			{
				type 			: 'link',
				idPrefix 		: 'mtActionDownload_',
				icon			: 'icon-download',
				title 			: 'Download',
				on 				: {
					click 			: ( O, $action, rowIndex, rowData ) => {
						window.open( _endpoints.download + rowIndex, '_blank' );
					}
				}
			},
			{
				type 			: 'modal',
				idPrefix 		: 'mtActionSim_',
				icon			: 'icon-play',
				title 			: 'Simulation',
				target			: '#mtModalSim'
			}
		],
		on 				: {			
			afterInit 		: ( O ) => {
				_log( 'on > afterInit', 'hook' ); // example hook output
				_log( O );
			},
			create			: ( O ) => {
				// create details modal
				O._modalDetails = new APPModalMTDetails( {
					data 		: O._metadata,
					id 			: 'mtModalDetails',
					classes 	: 'modal-details',
					type 		: 'mtDetails'
				}, O._$container );
				_log( O._modalDetails );

				// create simulations modal
				O._modalSim = new APPModalMTSimulations( {
					data 		: O._metadata,
					id 			: 'mtModalSim',
					classes 	: 'modal-sim',
					type 		: 'mtSim',
					on 			: {
						simRunModelView : ( O, modelId, simulation )=> {
							_log( 'on > simRunModelView', 'hook' ); // example hook output
							_log( O );
							_log( modelId );
							_log( simulation );
						}
					}
				}, O._$container );
				_log( O._modalSim );
			},
			afterPopulate 	: ( O, tableData ) => {
				_log( 'on > afterPopulate', 'hook' ); // example hook output
				_log( O );
				_log( tableData );
			},
			selectRow 		: ( O, rowIndex, rowData ) => {
				_log( 'on > selectRow', 'hook' ); // example hook output
				_log( O );
				_log( rowIndex );
				_log( rowData );
			},
			deselectRow 	: ( O, rowIndex, rowData ) => {
				_log( 'on > deselectRow', 'hook' ); // example hook output
				_log( O );
				_log( rowIndex );
				_log( rowData );
			},
			updateFilter 	: ( O, filtered ) => {
				_log( 'on > updateFilter', 'hook' ); // example hook output
				_log( O );
				_log( filtered );
			}
		}
	}
};


/**
 * app
 * initialization of main app
 */

$( document ).ready( () => { 
	_debug = true;
	_app = new APPLandingpage( _appVars, $( '.landingpage' ) );
} );