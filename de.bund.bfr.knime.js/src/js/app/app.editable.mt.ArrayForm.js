    /**
     * Create a div to edit string arrays.
     * 
     * ```
     * <div class="panel panel-default">
     *   <div class="panel-heading clearfix">
     *     <h4 class="panel-title pull-left" style="padding-top:7.5px;">Title</h4>
     *     <div class="input-group">
     *       <p class="pull-right" /> <!-- gutter -->
     *       <div class="input-group-btn">
     *         <button type="button" class="btn btn-default" data-toggle="modal" data-target="#">
     *           <i class="glyphicon glyphicon-plus"></i>
     *         </button>
     *         <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
     *         <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>
     *       </div>
     *      </div>
     *    </div>
     *   <table id="${table}" class="table"></table>
     * </div>
     * ```
     */
    class ArrayForm{

        constructor(name, mandatory, type, value, helperText, vocabulary, port) {
            let O = this;
            O.group = document.createElement("div");
            O.mandatory = mandatory;
            O.simpleTable = new SimpleTable(type, value, vocabulary, port);
            O._create(name, mandatory, helperText);
        }

        _create(name, mandatory, helperText) {
            let O = this;
            if ( name ) {

                // formgroup
                $formGroup = $( '<div class="form-group row"></div>' );

                // label
                let $label = $( '<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>' )
                    .attr( 'for', 'input_'+ name.replace(/[\W_]+/g,"_") )
                    .appendTo( $formGroup );
                $label.text(name+(mandatory?"*":""));
                
                // field
                let $field = $( '<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>' )
                    .appendTo( $formGroup );

                // actions
                let $actions = $( '<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>' )
                    .appendTo( $formGroup );
                
                

                // create param metadata action
                if (helperText) {
                    // action metadata list
                    let $actionMetadata = $( '<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>' )
                        .attr( 'data-toggle', 'collapse' )
                        .attr( 'data-target', '#paramMetadata_'+ name.replace(/[\W_]+/g,"_") )
                        .attr( 'aria-expanded', false )
                        .attr( 'aria-controls', 'paramMetadata_'+ name.replace(/[\W_]+/g,"_") )
                        .attr( 'title', 'Show Metadata' )
                        .appendTo( $actions );
                }
                
                // create actions            
                let header = $( '<div class="card-header"></div>' )
                                

                // Create card in group
                let panelDiv = document.createElement("div");
                panelDiv.classList.add("card");
                header.appendTo($(panelDiv));
                panelDiv.appendChild(O.simpleTable.table);
                
                _$actionTrash = $( '<button type="button" class="action action-pure float-right"><i class="feather icon-trash-2"></i></button>' )
                .attr( 'id', 'simActionRemove' )
                .attr( 'data-tooltip', '' )
                .attr( 'title', 'Trash' )
                .appendTo( header )
                .on( 'click', ( event ) => {
                    O.simpleTable.trash();
                } );
                
                
                // remove
                _$actionRemove = $( '<button type="button" class="action action-pure float-right"><i class="feather icon-delete"></i></button>' )
                .attr( 'id', 'simActionRemove' )
                .attr( 'data-tooltip', '' )
                .attr( 'title', 'Remove' )
                .appendTo( header )
                .on( 'click', ( event ) => {
                    O.simpleTable.remove();
                } );
                
    
                
                
                // add
                _$actionAdd = $( '<button type="button" class="action action-pure float-right"><i class="feather icon-plus"></i></button>' )
                .attr( 'id', 'actionAdd' )
                .attr( 'data-tooltip', '' )
                .attr( 'title', 'Add' )
                .appendTo( header )
                .on( 'click', ( event ) => {
                    O.simpleTable.add();
                } );
                
                $(panelDiv).appendTo($field);

                // create validation container
                O.$validationContainer = $( '<div class="validation-message mt-1"></div>' )
                .appendTo( $field );

                // create param metadata list
                if (helperText) {
                    // metadata table
                    let $metadataContainer = $( '<div class="collapse param-metadata"></div>' )
                        .attr( 'id', 'paramMetadata_'+ name.replace(/[\W_]+/g,"_") )
                        .attr( 'aria-expanded', false )
                        .appendTo( $field );

                    $metadataContainer.append( _createHelperMetadataText( helperText ) );
                }
            
                O.group =  $formGroup;
            }
            
        }

        get value() {
            let O = this;
            return O.simpleTable.value;
        }

        set value(newValue) {
            let O = this;
            O.simpleTable.trash();
            newValue.forEach(item => O.simpleTable._createRow(item));
        }
        onblurHandler(){
            let O = this;
            let can_emit_Event = typeof attr === typeof undefined || attr === false;
            _log( ' onblurHandler' + can_emit_Event );
            if ( can_emit_Event ) { 
                window.editEventBus.broadcast('MetadataChanged');
            }
        }
        clear() {
            let O = this;
            O.simpleTable.trash();
        }

        /**
         * @return {boolean} If the textarea is valid.
         */
        validate() {
            let O = this;
            let isValid = true;
            if(O.mandatory){
                isValid = O.simpleTable.value.length > 0 ? true : false;
            }
            if (!isValid) {
                O.$validationContainer.text(`At least one row is required`);
                O.group.addClass( 'has-error' );
                O.group.addClass( 'is-invalid' );
                O.$validationContainer.css("display", "block") ;
            }
            else{
                O.onblurHandler();
            }
            return isValid;
        }
    }