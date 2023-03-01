    /**
     * Create a Bootstrap 3 panel with controls in the heading and a table as body.
     * 
     * ```
     * <div class="panel panel-default">
     *   <div class="panel-heading clearfix">
     *     <h4 class="panel-title pull-left" style="padding-top:7.5px;">${title}</h4>
     *     <div class="input-group">
     *       <p class="pull-right" /> <!-- gutter -->
     *       <div class="input-group-btn">
     *         <button type="button" class="btn btn-default" data-toggle="modal" data-target="#${dialog}">
     *           <i class="glyphicon glyphicon-plus"></i>
     *         </button>
     *         <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
     *         <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>
     *       </div>
     *     </div>
     *   </div>
     *   <table class="table">
     *     <tr>
     *       <th><input type="checkbox"></th>
     *     </tr>
     *   </table>
     * </div>`
     * ```
     */
    class TablePanel {

        /**
         * Create a TablePanel.
         * 
         * @param {string} title Panel title.
         * @param {object} formData Related data from the UI schema.
         * @param {object} data Initial data of the table.
         */
        constructor(title, formData, data, port) {
            let O = this;
            O.panel = document.createElement("div");

            // Register this panel in dialog (TODO: this should be done in Dialog's constr)
            // O.dialog = dialog;
            O.dialog = new Dialog(title + "Dialog", "Add " + title, formData, port);
            O.dialog.panel = this;
            O.tablePanel = O._createComplexPanel(data, formData, title, O.dialog);
            O.table = O.tablePanel.find( "table.table-striped");
            O.data = data ? data : []; // Initialize null or undefined data
            O._create(title, O.dialog, formData);
        }

        /**
         * Create UI of the TablePanel.
         * 
         * @param {string} title Panel title.  
         * @param {Dialog} dialog Reference to Dialog object. This Dialog is later
         *   used for adding new entries and editing existing ones. 
         * @param {object} formData Related data from the UI schema.
         */
        _create(title, dialog, formData) {
            let O = this;
            // panel
            O.panel.classList.add("panel", "panel-default");
            O.tablePanel.appendTo( $(O.panel));
        }
        /**
         * CREATE COMPLEX PANEL
         * create complex tab-pane for specific menu
         * table has in metadata and schema defined cols
         * @param {array} menu
         * @param {object} modelHandler: object of class Model
         */

        _createComplexPanel(data, formData, title, dialog) {
            let O = this;

            // tab-pane
            let $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>')
                .attr('id', 'table'+title);
                // Add button
                let addButton = $( '<button class="btn btn-outline-secondary btn-sm btn-icon" type="button"><i class="feather icon-plus"></i></button>' )
                    .attr( 'aria-label', "Add a " + title )
                    .attr( 'title', "Add a " + title );
                addButton.on('click', (event) => {
                    Object.values(dialog.inputs).forEach(input => input.clear());
                    $(dialog.modal).modal('show');
                });
               

                let removeAllButton = $( '<button class="btn btn-outline-secondary btn-sm btn-icon" type="button"><i class="feather icon-trash"></i></button>' )
                    .attr( 'aria-label', `Remove all ${title}(s)`)
                    .attr( 'title', `Remove all ${title}(s)` );
                removeAllButton.on('click', (event) => {
                    O.removeAll();
                });

                // table settings
                let tableSettings = {
                    cols: [],
                    tableData: [],
                    responsive: true,
                    showToggle: true,
                    rowActions 		: [
                        {
							type 			: 'link',
							idPrefix 		: 'mtActionMerge_',
							icon			: 'icon-arrow-up',
							title 			: 'Move Up',
							on 				: {
								click 			: ( o, $action, rowIndex, rowData ) => {
									_log( 'on > clicktrash', 'hook' ); 
									_log( o );
									_log( $action );
									_log( rowIndex );
                                    _log( rowData );
                                    O.moveTo(rowIndex,'up');
								}
							}
                        },
                        {
							type 			: 'link',
							idPrefix 		: 'mtActionMerge_',
							icon			: 'icon-arrow-down',
							title 			: 'Move down',
							on 				: {
								click 			: ( o, $action, rowIndex, rowData ) => {
									_log( 'on > clickMoveTO ', 'hook' ); 
									_log( o );
									_log( $action );
									_log( rowIndex );
                                    _log( rowData );
                                    O.moveTo(rowIndex,'down');
								}
							}
						},
						{
							type 			: 'link',
							idPrefix 		: 'mtActionMerge_',
							icon			: 'icon-trash',
							title 			: 'Trash',
							on 				: {
								click 			: ( o, $action, rowIndex, rowData ) => {
									_log( 'on > clicktrash', 'hook' ); 
									_log( o );
									_log( $action );
									_log( rowIndex );
                                    _log( rowData );
                                    O.remove(rowIndex);
								}
							}
						},
						{
							type 			: 'link',
							idPrefix 		: 'mtActionEdit_',
							icon			: 'icon-edit-2',
							title 			: 'Edit',
							on 				: {
								click 			: ( o, $action, rowIndex, rowData ) => {
									_log( 'on > clickEdit', 'hook' ); 
									_log( o );
									_log( $action );
									_log( rowIndex );
                                    _log( rowData );
                                    O.edit(rowIndex, rowData, dialog)
								}
							}
						}
					],
                    editableToolbarbuttons:[addButton, removeAllButton]
                };

                // set table cols
                $.each(formData, (i, prop) => {
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
                $.each(data, (i, item) => {
                    // row each item
                    let rowData = {
                        cells: []
                    };
                    // cells
                    $.each(formData, (j, prop) => {
                        let data = item[prop.id];
                        data = _checkUndefinedContent(data);
                        // cell each prop
                        rowData.cells.push(data);
                    });

                    tableSettings.tableData.push(rowData);
                });
                // create table
                O.panelTable = new APPTable(tableSettings, $panel);
                $panel.data('table', O.panelTable);
            return $panel;
        }
        add(data,index,isEdit) {
            let O = this;
            data.el ?delete data.el:null;
            
            let keys = [];
            $.each(O.panelTable.opts.cols,function(index,key){
                keys.push(key.field);
            })
            // set table row data
            let rowData = {
                cells: []
            };
            // cells
            $.each(data, (j, value) => {
                // cell each prop
                rowData.cells.push(value);
            });
            if(isEdit){
                O.panelTable._tableData.splice(index, 1, rowData);
                O.data.splice(index, 1, data); // add data
                O.panelTable.addRow(index, rowData, O.panelTable._tableData,'true',isEdit);
            }else{
                O.panelTable._tableData.push(rowData); 
                O.data.push(data);// add data
                O.panelTable.addRow(O.panelTable._tableData.length-1, rowData, O.panelTable._tableData,'true',false);
            }
            window.editEventBus.broadcast('MetadataChanged');
        }

        edit(index, originalData, dialog) {
            let O = this;
            let keys = [];
            $.each(O.panelTable.opts.cols,function(index,key){
                keys.push(key.field);
            })
            for(indexx in keys){
                let input = dialog.inputs[keys[indexx]].input
                if(input){
                    if ( input.attr('type') === "date") {
                        let value = originalData.cells[indexx]
                        if(value){
                            let day = ("" + value[2]).length > 1 ? ("" + value[2]) : ("0" + value[2]);
                            let month = ("" + value[1]).length > 1 ? ("" + value[1]) : ("0" + value[1]);
                            dialog.inputs[keys[indexx]].input.val(value[0] + "-" + month + "-" + day);
                        }
                    }else if(input.attr('type') === "checkbox"){
                        dialog.inputs[keys[indexx]].input.prop('checked', originalData.cells[indexx] );  
                    }else{
                        dialog.inputs[keys[indexx]].input.val(originalData.cells[indexx]);
                        if(dialog.inputs[keys[indexx]].input.trigger){
                            dialog.inputs[keys[indexx]].input.trigger('change');
                        }
                    }
               }else if(Array.isArray(originalData.cells[indexx]) && dialog.inputs[keys[indexx]].simpleTable){
                   $.each(originalData.cells[indexx],function(j,val){
                        dialog.inputs[keys[indexx]].simpleTable._createRow(val);
                   })
               }
            }
           
            dialog.editedRow = index;
            $(dialog.modal).modal('show');
            //window.editEventBus.broadcast('MetadataChanged');
        }
       
        save(index, originalData) {
            let O = this;
            console.log(originalData);
            O.add(originalData, index, true);
            _appUI._initTdCollapse(O.table);
            
        }
        remove(index) {
            let O = this;
            $(O.panelTable._$tbody).find('tr').eq(index).remove();;
            
            O.data.splice(index, 1);
            O.panelTable._tableData.splice(index, 1);
            
            $.each($(O.panelTable._$tbody).find('tr'),function(rowindex, row){
                $(row).attr('data-row-id',rowindex);
            });
            window.editEventBus.broadcast('MetadataChanged');
        }
        moveTo(index, command){
            let O = this;
            var row = $(O.panelTable._$tbody).find('tr').eq(index);
            if (command === 'up') {
                row.insertBefore(row.prev());
                O.moveElement(O.data,index,--index);
            } else if (command === 'down'){
                row.insertAfter(row.next());
                O.moveElement(O.data,index,++index);
            }
            $.each($(O.panelTable._$tbody).find('tr'),function(rowindex, row){
                $(row).attr('data-row-id',rowindex);
            });
            window.editEventBus.broadcast('MetadataChanged');
        }
        moveElement(arr, old_index, new_index) {
            if (new_index >= arr.length) {
                var k = new_index - arr.length + 1;
                while (k--) {
                    arr.push(undefined);
                }
            }
            arr.splice(new_index, 0, arr.splice(old_index, 1)[0]);
        };
        removeAll() {
            let O = this;
            O.data = []; // Clear data
            O.panelTable._tableData = []; // Clear data
            O.panelTable._clear();  // Empty table
            window.editEventBus.broadcast('MetadataChanged');
        }
        
    }