    /**
     * Bootstrap 3 form with a select.
     */
    class SelectForm {

        /**
         * Create a Bootstrap 3 form-group with a select.
         * 
         * ```
         * <div class="form-group row">
         *   <label>name</label>
         *   <select class="form-control">
         *     <option>1</option>
         *     <option>2</option>
         *   </select>
         * </div>```
         * <select id="select2ExampleS2" class="form-control form-control-sm" style="width: 100%;" data-sel2 data-placeholder="Select…">
		 *									<option value="1">Option 1</option>
		 *									<option value="2">Option 2</option>
		 *									<option value="3">Option 3 with very long title lorem ipsum dolor sit amet</option>
		 *								</select>
         */
        constructor(name, mandatory, helperText, value,port , vocabulary = null) {
            let O = this;
            O.group = document.createElement("div");

            O._create(name, mandatory, helperText, value,port, vocabulary);
        }

        _create(name, mandatory, helperText, value,port , vocabulary) {
            let O = this;
            // formgroup
            let $formGroup = $( '<div class="form-group row"></div>' );

            // label
            let $label = $( '<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>' )
                .attr( 'for', 'selectInput_'+ name )
                .appendTo( $formGroup );
            $label.text(name+(mandatory?"*":""));
            
            // field
            let $field = $( '<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 "></div>' )
                .appendTo( $formGroup );

            // actions
            let $actions = $( '<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>' )
                .appendTo( $formGroup );

            // input item
            O.input = null;

            // create param metadata action
            if (helperText) {
                // action metadata list
                let $actionMetadata = $( '<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>' )
                    .attr( 'data-toggle', 'collapse' )
                    .attr( 'data-target', '#paramMetadata_'+ name )
                    .attr( 'aria-expanded', false )
                    .attr( 'aria-controls', 'paramMetadata_'+ name )
                    .attr( 'title', 'Show Metadata' )
                    .appendTo( $actions );
            }

            O.input  = $( '<select class="form-control form-control-sm" style="width: 100%;" data-sel2 data-placeholder="Select…"/>' )
                      .attr( 'id', 'selectInput_'+ name )
                            .appendTo( $field );
            O.input.val(value);
            // Add options from vocabulary. The option matching value is selected.
            if(window._endpoints.controlledVocabularyEndpoint){
                
                fetch(window._endpoints.controlledVocabularyEndpoint+`${vocabulary}`)
                    .then(response => response.json())
                    .then(data => {
                            O.input.append(data.map(item => `<option>${item}</option>`).join(""));
                        
                }).catch(error => {
                    if(port >= 0){
                        fetch(`http://localhost:${port}/getAllNames/${vocabulary}`)
                            .then(response => response.json())
                            .then(data => {
                                    O.input.append(data.map(item => `<option>${item}</option>`).join(""));
                                
                        });
                    }
                });
                
            }
            else if(port >= 0){
                fetch(`http://localhost:${port}/getAllNames/${vocabulary}`)
                    .then(response => response.json())
                    .then(data => {
                            O.input.append(data.map(item => `<option>${item}</option>`).join(""));
                        
                    });
            }

            // create validation container
            O.input.$validationContainer = $( '<div class="validation-message mt-1"></div>' )
            .appendTo( $field );

            // create param metadata list
            if (helperText) {
                // metadata table
                let $metadataContainer = $( '<div class="collapse param-metadata"></div>' )
                    .attr( 'id', 'paramMetadata_'+ name )
                    .attr( 'aria-expanded', false )
                    .appendTo( $field );

                $metadataContainer.append( _createHelperMetadataText( helperText ) );
            }
            O.group =  $formGroup;


        }
        
        get value() {
            let O = this;
            return O.input.val() || O.input.find("option[data-select2-id]").text();
        }

        set value(newValue) {
            let O = this;
            O.select.val(newValue);
        }

        onblurHandler(){
            let O = this;
            let closestForm = O.input.closest( "form" );
            let attr = closestForm.attr('no-immidiate-submit' );
            let can_emit_Event = typeof attr === typeof undefined || attr === false;
            _log( ' onblurHandler' + can_emit_Event );
            if ( can_emit_Event ) { 
                window.editEventBus.broadcast('MetadataChanged');
            }
        }
        clear() {
            let O = this;
            O.input.val('');
        }

        /**
         * @returns {boolean} If the input is valid.
         */
        validate() {
            let O = this;
            let isValid;
            O.input.find( '.has-error' ).removeClass( 'has-error' );
            O.input.find( '.is-invalid' ).removeClass( 'is-invalid' );
            O.input.find( '.validation-message' ).empty();
            if (!O.mandatory) {
                isValid = true;
            } else {
                isValid = O.input.value ? true : false;
            }

            if (!isValid) {
                O.input.$validationContainer.text(`required`);
                O.input.parents( '.form-group' ).addClass( 'has-error' );
			    O.input.addClass( 'is-invalid' );
                O.input.$validationContainer.css("display", "block") ;
            }
            else{
                O.onblurHandler();
            }
            return isValid;
        }
    }