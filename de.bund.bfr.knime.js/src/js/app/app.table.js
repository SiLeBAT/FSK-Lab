/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

class APPTable {
	constructor(settings, $container, metadata, uploadDates, executionTimes) {
		let O = this;
		O._$container = $container;
		O._$wrapper = null;
		O.totalRows = 0;
		// defaults
		O._opts = $.extend(true, {}, {
			attributes: {}, // attribute : value pairs for <table>
			classes: '', // string of classes for <table>
			cols: [], // cols definition as array
			tableData: null, // table data
			ids: { // ids for specific table elements
				table: 'tGrid',
				thead: 'tHead',
				tbody: 'tRows'
			},
			responsive: true, // wrap table with .table-responsive
			rowActions: [],
			rowSelectable: false, // 'single', // 'multiple', //
			showToggle: true, // show card view toggle
			wrapper: false, // 'card'
			on: { // hooks/callbacks on specific events
				afterInit: null,
				afterPopulate: null,
				selectRow: null,
                selectAllRow: null,
				deselectRow: null
			}
		}, settings);
		O._metadata = metadata;
        O._uploadDates = uploadDates;
        O._executionTimes = executionTimes;
		// basic init actions
		O._create();

		// callback
		if ($.isFunction(O.opts.on.afterInit)) {
			O.opts.on.afterInit.call(O, O);
		}
	}
	get opts() {
		return this._opts;
	}
	set opts(settings) {
		this._opts = $.extend(true, {}, this.opts, settings);
	}

	addRow( rowIndex, rowData, tableData, isMainTable, isEdit) {
		
		let O = this;
		tableData = O._tableData
		// row
		let $tr = $('<tr data-row-id="' + rowIndex + '"></tr>');			
        
        if(isEdit)
            O._$tbody.find( 'tr:nth('+(rowIndex)+')').replaceWith($tr);
        else
            $tr.appendTo(O._$tbody);
		// rows selectable
		if (O.opts.rowSelectable) {

			// add selectable attribrutes
			$tr.data('selectable', '');
			$tr.data('selected', false);

			// add row click actions
			$tr.on('click', (event) => {
				// activate row not on click on buttons, actions or links in the table
				if (!$(event.target).is('a, button, .action')
					&& !$(event.target).parent().is('a, button, .action')) {
					// select row
					O._handleRowSelect($tr);
				}
			});
		}
      
		// complete table data by adding row element to certain row data
		if(isMainTable){
			tableData[rowIndex].el = $tr;
            
        }
        if(O._opts.data){
            $('<td class="active"><input type="checkbox" class="select-item checkbox" name="select-item" /></td>')
                                    .appendTo($tr);
        }
        
		// create cols
		$.each(O.opts.cols, (j, col) => {
			let data ;
			if(rowData.cells)
				data = rowData.cells[j];
			else
				data =  rowData[col.field];
			let $td = $('<td></td>')
				.appendTo($tr);
			if(data && data.length > 60){
				col.collapsable = "true";
			}
			col.classes && col.classes.td ? $td.addClass(col.classes.td) : null; // classes
			col.collapsable ? $td.attr('data-td-collapse', col.collapsable) : null; // data collapsable
			col.label ? $td.attr('data-label', col.label) : null; // add data-label for toggle view cards
			col.field ? $td.attr('data-id', col.field) : null; 

			// td attributes
			if (col.attributes && col.attributes.td) {
				$.each(col.attributes.td, (attr, val) => {
					attr && val ? $td.attr(attr, val) : '';
				});
			}


			// check for function that format the data
			if (col.formatter) {
				if ($.isFunction(col.formatter)) {
					data = col.formatter.call(O, data);
				}
				else if (_formatter && _formatter.hasOwnProperty(col.formatter)) {
					data = _formatter[col.formatter].call(O, data);
				}
			}
			
			// fill td with data
			$td.html(data);

		});
		// create row actions 
		if (O.opts.rowActions && O.opts.rowActions.length > 0) {

			// create action col
			let $tdActions = $('<td class="td-actions"></td>')
				.appendTo($tr);

			// create row actions 
			$.each(O.opts.rowActions, (j, action) => {

				// create action element
				let $action = $('<button class="action action-outline-secondary"></button>')
					.attr('id', action.idPrefix + rowIndex)
                    .attr('hidden', action.hidden )
                    .attr('role', action.role );
				// .appendTo( $tdActions );

				// create action icon
				$action.$icon = $('<i class="feather"></i>')
					.appendTo($action);
				// set icon by class
				action.icon ? $action.$icon.addClass(action.icon) : null;

				// set tooltip and title
				if (action.title) {
					$action
						.attr('data-tooltip', '')
						.attr('aria-label', action.title)
						.attr('title', action.title);
				}

				// action on click
				if (action.on) {
					if (action.on.click && $.isFunction(action.on.click)) {
						// bind click action on action
						$action.on('click', (event) => {
							actionIndex = $tr.attr('data-row-id');
							action.on.click.call(O, O, $action, actionIndex, rowData);
						})
					}
				}

				// add action type specific attributes
				if (action.type) {
					// create modal action
					if (action.type == 'modal') {
						$action.attr('data-toggle', 'modal')
							.attr('data-target', action.target)
							.attr('data-modal-id', rowIndex);
					}
				}
				// append to td
				$action.appendTo($tdActions);

			});

			// wrap actions with inner container of td
			$tdActions.wrapInner('<div class="td-actions-container"></div>');
		}
		O.totalRows = O._$tbody.find( 'tr').length;
	}
	/**
	 * CREATE TABLE HEAD
	 * @param
	 */

	async _create() {
		let O = this;
		_log('TABLE / _create', 'primary');

		// current state of view
		O._view = 'default'; // card; //

		// table rows
		O._tableData = O.opts.tableData;
		_log(O._tableData);

		// callback on create
		if ($.isFunction(O.opts.on.create)) {
			O.opts.on.create.call(O, O);
		}

		if (O._tableData) {

			// create table element
			O._$table = $('<table class="table table-striped table-hover"></table>')
				.attr('id', O.opts.ids.table)
				.attr('data-table', '')
				.addClass(O.opts.classes)
				.appendTo(O._$container);

			// wrapper
			if (O.opts.wrapper) {
				// card
				if (O.opts.wrapper == 'card') {
					O._$wrapper = $('<div class="card card-table-main overflow-hidden"></div>');
					O._$table.wrap(O._$wrapper);
					O._$table.wrap('<div class="card-body p-0"></div>');
				}
			}

			// responsive table wrapper
			if (O.opts.responsive) {
				O._$table.wrap('<div class="table-responsive"></div>');
			}

			// custom classes
			O.opts.classes ? O._$table.addClass(O.opts.classes) : null;

			// card view toggle
			O.opts.showToggle ? O._$table.attr('data-show-toggle', O.opts.showToggle) : null; // bs table / data-show-toggle for view: table/list

			// custom table attributes
			$.each(O.opts.attributes, (attr, val) => {
				O._$table.attr(attr, val);
			});

			// toolbar
			O._createTableToolbar();

			// create table head
			O._$thead = O._createTableHead(O.opts.cols)
				.appendTo(O._$table);

			// create table head
			O._$tbody = O._createTableBody()
				.appendTo(O._$table);

			// populate table
			O._populateTable(O._tableData);
		}

		// tooltips
		_appUI._initTooltips(O._$container);
	}


	/**
	 * CREATE TABLE HEAD
	 * @param {array} cols
	 */

	_createTableHead(cols) {
		let O = this;
		_log('TABLE / _createTableHead');
		// thead
		let $thead = $('<thead></thead>');
        if(O._opts.data){
            let $checkAll =  $('<th class="active"><input type="checkbox" class="select-all checkbox" name="select-all" /></th>')
                                        .appendTo($thead);
            $checkAll.click(function () {
                                this.checked = !this.checked
                                var checked = this.checked;
                                $("input.select-item").each(function (index,item) {
						            $tr = $(item.closest("tr"));
                                    if(!$tr.is('.tr-hidden')){
                                        $tr.addClass('tr-selected');
                                        $tr.data('selected', true);
                                        item.checked = checked;
                                        if ($.isFunction(O.opts.on.selectAllRow) ) {
                                            let rowData = O._tableData[index]
                                            O.opts.on.selectAllRow.call(O, O, index, rowData);
                                        }
                                    }
                                });
                            });
                        
         }
		// create th cols
		if (cols) {
			$.each(cols, (i, col) => {
				let $th = $('<th></th>')
					.appendTo($thead);

				// th attributes
                
				col.label ? $th.html($th.html()+'<span>' + col.label + '</span>') : null;
				col.id ? $th.attr('id', col.id) : null; // id
				col.classes && col.classes.th ? $th.addClass(col.classes.th) : null; // classes
				col.field ? $th.attr('data-field', col.field) : null; // bs table / data-field identifier

				// add sort functionality
				if (col.sortable) {
					$th.attr('data-sortable', col.sortable);
					$th.on('click', (event) => {
						O._updateOrder($th);
					});
					col.sorter ? $th.attr('data-sorter', col.sorter) : null; // bs table / data-sorter function
				}

				// th custom attributes
				if (col.attribute && col.attributes.th) {
					$.each(col.attributes.th, (attr, val) => {
						$th.attr(attr, val);
					});
				}
			});

			// create row actions col
			if (O.opts.rowActions && O.opts.rowActions.length > 0) {
				let $th = $('<th></th>')
					.attr('id', 'colActions')
					.attr('data-field', 'actionsTable')
					.appendTo($thead);
			}

			$thead.wrapInner('<tr></tr>');
		}

		return $thead;
	}


	/**
	 * CREATE TABLE TOOLBAR
	 * creates button bar above the table for responsive toggle
	 */

	_createTableToolbar() {
		let O = this;
		_log('TABLE / _createTableToolbar');

		if (O.opts.showToggle || O.opts.showColumns) {
			O._$toolbar = $('<div class="table-toolbar"></div>');

			if (O.opts.responsive) {
				O._$toolbar.insertBefore(O._$table.parent());
			}
			else {
				O._$toolbar.insertBefore(O._$table);
			}
			// place button group
			O._$toolbar._$btnGroup = $('<div class="col-auto btn-group"></div>')
				.appendTo(O._$toolbar)
				.wrap('<div class="ml-auto row justify-content-end"></div>');

			// create toolbar buttons 
			// toggle view
			if (O.opts.showToggle) {
				O._$toolbar._$btnToggleView = $('<button class="btn btn-outline-secondary btn-sm btn-icon toggle-card" type="button"><i class="feather icon-pause"></i></button>')
					.attr('aria-label', 'Toggle view')
					.attr('title', 'Toggle view')
					.attr('data-tooltip', '')
					.attr('data-toggle-table-view', '')
					.appendTo(O._$toolbar._$btnGroup);

				O._$toolbar._$btnToggleView.on('click', (event) => {
					O._toggleTableView();
				});
			}
			if (O._opts.editableToolbarbuttons) {
				$.each(O._opts.editableToolbarbuttons, function (index, element) {
					element.appendTo(O._$toolbar._$btnGroup);
				});
			}
		}
	}


	/**
	 * CREATE TABLE BODY
	 * @param {object} tableData: metadata object
	 */

	_createTableBody() {
		let O = this;
		_log('TABLE / _createTableBody');

		// table body
		let $tbody = $('<tbody></tbody>');

		return $tbody;
	}


	/**
	 * POPULATE TABLE
	 * @param {object} tableData: data object
	 */

	_populateTable(tableData) {
		let O = this;
		_log('TABLE / _populateTable');
		O._clear();

		tableData = tableData || O._tableData

		// create rows
		$.each(tableData, (rowIndex, rowData) => {
			this.addRow(rowIndex, rowData, tableData,'true');
		});

		// callback
		if ($.isFunction(O.opts.on.afterPopulate)) {
			O.opts.on.afterPopulate.call(O, O, O._tableData);
		}
	}


	/**
	 * UPDATE ORDER
	 * returns col index by field identifier
	 * @param {string} field name of the column
	 */

	_updateOrder($th) {
		let O = this;
		_log('TABLE / _updateOrder');

		let field = $th.data('field');
		let $rows = O._$tbody.find('tr').toArray();
		let col = O._getColIndexByField(field);

		// if selected item has alreay asc or desc class, just reverse contents
		if ($th.is('.asc') || $th.is('.desc')) {

			// toggle to other class
			$th.toggleClass('asc desc');

			// reverse the array
			O._$tbody.append($rows.reverse());
		}
		// otherwise perform a sort 
		else {
			// add class to header                            
			$th.addClass('asc');

			// remove asc or desc from all other headers
			$th.siblings().removeClass('asc desc');

			let fieldSorter = $th.data('sorter') || '_name';
			_log('sort by : ' + field + ' with ' + fieldSorter, 'level1');

			if (fieldSorter) {
				// fieldsorter is a function
				if ($.isFunction(fieldSorter)) {
					$rows.sort((a, b) => {

						aa = $(a).find('td').eq(col).text() // get text of column in row a
						bb = $(b).find('td').eq(col).text() // get text of column in row b
						log(aa + ' / ' + bb);

						fieldSorter.call(aa, bb);
					});
				}
				// or fieldsorter is a sub routine of _sorter
				else if (_sorter && _sorter.hasOwnProperty(fieldSorter)) {
					// call sort() on rows array
					$rows.sort((a, b) => {

						aa = $(a).find('td').eq(col).text() // get text of column in row a
						bb = $(b).find('td').eq(col).text() // get text of column in row b

						// call compare method
						return _sorter[fieldSorter](aa, bb);
					});
				}

				O._$tbody.append($rows);
			}
		}
	}


	/**
	 * GET COL INDEX BY FIELD
	 * returns col index by field identifier
	 * @param {string} field name of the column
	 */

	_getColIndexByField(field) {
		let O = this;

		let colIndex = -1;
		$.each(O.opts.cols, (i, col) => {
			if (field == col.field) {
				colIndex = i;
			}
		});

		return colIndex;
	}


	/**
	 * CLEAR TABLE
	 * removes table body rows
	 */

	_clear() {
		let O = this;
		_log('TABLE / _clear');

		if (O._$tbody) {
			O._$tbody.empty();
		}
	}
	

	/**
	 * TOGGLE VIEW
	 * change view of table <> card
	 */

	_toggleTableView(event) {
		let O = this;
		_log('TABLE / _toggleTableView');

		_log(O._view);
		// current state = default => set card view class
		if (O._view == 'default') {
			O._view = 'card';
			O._$table.addClass('table-view-card');
			// update toggles class
			O._$toolbar._$btnToggleView
				.removeClass('toggle-card')
				.addClass('toggle-default');
		}
		// current state != default => set remove view class
		else {
			O._view = 'default';
			O._$table.removeClass('table-view-card');
			// update toggles class
			O._$toolbar._$btnToggleView
				.removeClass('toggle-default')
				.addClass('toggle-card');
		}
	}


	/**
	 * SELECT ROW
	 * select or deselect a certain row
	 * @param {dom object} $tr: jquery element 
	 */

	_handleRowSelect($tr) {
		let O = this;
		// rows selectable and tr exists
		if (O.opts.rowSelectable && $tr) {
			// get current state
			let isSelected = $tr.data('selected');
			// already selected
			if (isSelected) {
				O._deselectRow($tr);
                $tr.find( "input.checkbox" ).prop('checked', false);
			}
			else {
				// deselect all
				if (O.opts.rowSelectable == 'single') {
					O._deselectAllRows();
					O._selectRow($tr)
				}
				else {
					O._selectRow($tr)
				}
                $tr.find( "input.checkbox" ).prop('checked', true);
			}
		}
	}


	/**
	 * SELECT ROW
	 * select a certain row
	 * @param {dom object} $tr: jquery element 
	 */

	_selectRow($tr) {
		let O = this;
		_log('TABLE / _selectRow : ' + $tr.data('row-id'));

		if ($tr) {

			let rowIndex = $tr.data('row-id');
			let rowData = O._tableData[rowIndex]
			// add class marker
			$tr.addClass('tr-selected');
			$tr.data('selected', true);

			// callback on select row
			if ($.isFunction(O.opts.on.selectRow) ) {
				O.opts.on.selectRow.call(O, O, rowIndex, rowData);
			}
		}
	}


	/**
	 * DESELECT ROW
	 * deselect a certain row
	 * @param {dom object} $tr: jquery element 
	 */

	_deselectRow($tr) {
		let O = this;
		_log('TABLE / _deselectRow : ' + $tr.data('row-id'));

		if ($tr) {

			let rowIndex = $tr.data('row-id');
			let rowData = O._tableData[rowIndex]

			// remove class marker
			$tr.removeClass('tr-selected');
			$tr.data('selected', false);

			// callback on de-select row
			if ($.isFunction(O.opts.on.deselectRow)) {
				O.opts.on.deselectRow.call(O, O, rowIndex, rowData);
			}
		}
	}


	/**
	 * DESELECT ALL ROWS
	 * deselect all rows
	 */

	_deselectAllRows() {
		let O = this;
		_log('TABLE / _deselectAllRows');

		O._$table.find('.tr-selected').each((i, tr) => {
			let $tr = $(tr);
			// remove class marker
			$tr.removeClass('tr-selected');
			$tr.data('selected', false)
		});
	}


	/**
	 * GET SELECTD ROWS
	 * deselect all rows
	 * @return {object} tabledData filtered by rows selected
	 */

	_getSelectedRows() {
		let O = this;
		_log('TABLE / _getSelectedRows');

		let selectedRows = [];

		// check all table rows
		$.each(O._tableData, (i, row) => {
			// row is currently selected and not hidden
			if (row.el.is('.tr-selected') && !row.el.is('.tr-hidden')) {
				selectedRows.push(row);
			}
		});

		return selectedRows;
	}


	/**
	 * UPDATE STRIPES
	 * updates stripe classes for each row
	 */

	_updateStripes() {
		let O = this;
		_log('TABLE / _updateStripes');

		if (O._$table.is('.table-striped')) {

			// update stripes
			O._$tbody.find('tr:not(.tr-hidden)').each((i, tr) => {

				let $tr = $(tr);
				$tr.removeClass('tr-odd tr-even');

				if ((i + 1) % 2 == 0) {
					$tr.addClass('tr-even');
				}
				else {
					$tr.addClass('tr-odd');
				}
			});
		}
	}
}