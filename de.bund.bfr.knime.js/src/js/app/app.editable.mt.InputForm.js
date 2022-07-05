    /**
     * Bootstrap 3 form-group for an input.
     */
    class InputForm {

        /**
         * Create a Bootstrap 3 form-group.
         * 
         * ```
         * <div class="form-group row">
         *   <label>name</label>
         *   <div class="col-sm-10">
         *     <input type="text">
         *   </div>
         * </div>`;
         * ```
         * 
         * If type === checkbox
         * ```
         * <div class="form-group row">
         *   <label >name</label>
         *   <div class="col-sm-10">
         *     <input class="form-check-input" type="checkbox" checked="">
               *	 </div>
           * </div>
         * ```
         * 
         * @param {string} name Property name
         * @param {boolean} mandatory `true` if mandatory, `false` if optional.
         * @param {string} type Property type: text, url, checkbox, etc.
         * @param {string} helperText Tooltip
         * @param {string} value Initial value of the property.
         * @param {Array} vocabulary Vocabulary name.
         */
        constructor(name, mandatory, type, helperText, value, port, vocabulary = null, sid) {
            let O = this;
            O.name = name;
            O.mandatory = mandatory;
            O.type = type;
            O.helperText = helperText;
            O.isSID = !_isNull(sid) && !_isUndefined(sid);
            O.group = null ;
            O._create(name, mandatory, type, helperText, value, vocabulary, port);
        }

        /**
         * @param {string} name Property name
         * @param {boolean} mandatory `true` if mandatory, `false` if optional.
         * @param {string} type Property type: text, url, checkbox, etc.
         * @param {string} helperText Tooltip
         * @param {string} value Initial value of the property.
         * @param {Array} vocabulary Vocabulary name.
         */
        _create(name, mandatory, type, helperText, value, vocabulary, port) {
            let O = this;
            O._createFormField ( name, mandatory, type, helperText, value, vocabulary, port );
        }
        /**
         * CREATE FORM FIELD
         * create field as form group
         * @param {array} param
         */
        
        _createFormField ( name, mandatory, type, helperText, value, vocabulary, port ) {
            let O = this;
            _log( 'PANEL SIM / _createFormField' );
            _log( name );

            if ( name ) {

                // formgroup
                let $formGroup = $( '<div class="form-group row"></div>' );

                // label
                $label = $( '<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>' )
                    .attr( 'for', 'input_'+ name.replace(/[\W_]+/g,"_") )
                    .appendTo( $formGroup );
                $label.text(name+(mandatory?"*":""));
                
                // field
                let $field = $( '<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>' )
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
                        .attr( 'data-target', '#metadata_'+ name.replace(/[\W_]+/g,"_") )
                        .attr( 'aria-expanded', false )
                        .attr( 'aria-controls', 'metadata_'+ name.replace(/[\W_]+/g,"_") )
                        .attr( 'title', 'Show Metadata' )
                        .appendTo( $actions );
                }

                



                if ( type ) {

                    // numeric
                    if ( type == 'number' ) {

                        let $inputGroup = $( '<div class="input-group input-group-sm"></div>' )
                            .appendTo( $field );

                        O.input = $( '<input type="text" />' )
                            .attr( 'id', 'input_'+ name.replace(/[\W_]+/g,"_") )
                            .attr( 'aria-invalid', false )
                            .attr( 'data-min', parseFloat(Number.MIN_SAFE_INTEGER) ) // min value
                            .attr( 'data-max', parseFloat(Number.MAX_SAFE_INTEGER) ) // max value
                            .appendTo( $inputGroup );

                        
                        // touchspin
                        O.input
                            .addClass( 'form-control form-control-sm' )
                            .attr( 'data-touchspin', '' );
                        // add postfix to touchspin
                        
                        //O.input.attr( 'data-touchspin-postfix', type );
                        
                    }
                    // string or others
                    //<input class="custom-control-input" type="checkbox" id="switchExample1" name="switchExample1" checked />
                    else if ( type == 'checkbox' ) {
                        O.input = $( '<input type="checkbox" class="form-check-input" style="width: auto;" />' )
                            .attr( 'id', 'input_'+ name.replace(/[\W_]+/g,"_") )
                            .appendTo( $field );

                    }
                    // string or others
                    else {
                        O.input = $( '<input type="text" class="form-control form-control-sm" />' )
                            .attr( 'id', 'input_'+ name.replace(/[\W_]+/g,"_") )
                            .appendTo( $field );
                    }
                }
                if (type === "date"){
                    O.input.attr('type','date');
                    //O.input.attr('data-datepicker','');
                }
                
                if (type === "year_date"){
                    O.input.datepicker( {
                        format: " yyyy", // Notice the Extra space at the beginning
                        viewMode: "years", 
                        minViewMode: "years"
                    });
                }
                
                if (type === "date" && typeof (value) != "string") {
                    let day = ("" + value[2]).length > 1 ? ("" + value[2]) : ("0" + value[2]);
                    let month = ("" + value[1]).length > 1 ? ("" + value[1]) : ("0" + value[1]);
                    O.input.val(value[0] + "-" + month + "-" + day);
                    
                }else if(type === "checkbox" ){
                    O.input.checked = value 
                }else{
                    O.input.val(value);
                }
                
                // Add autocomplete to input with vocabulary
                if (vocabulary) {
                    // addControlledVocabulary(O.input, vocabulary, port);
                    addControlledVocabulary(O.input, vocabulary);
                }
                O.input.on( "blur", () => {O.validate(O.value)} );
                // create validation container
                O.input.$validationContainer = $( '<div class="validation-message mt-1"></div>' )
                    .appendTo( $field );

                // create  metadata list
                if (helperText) {
                    // metadata table
                    let $metadataContainer = $( '<div class="collapse param-metadata"></div>' )
                        .attr( 'id', 'metadata_'+ name.replace(/[\W_]+/g,"_") )
                        .attr( 'aria-expanded', false )
                        .appendTo( $field );

                    $metadataContainer.append( _createHelperMetadataText( helperText ) );
                }
            
                O.group =  $formGroup;
            }
        }

        get value() {
            let O = this;
            if (O.type === "date") {
                if(O.input.val()){
                    value = O.input.val().split("-");
                    let day = ("" + value[2]).length > 1 ? ("" + value[2]) : ("0" + value[2]);
                    let month = ("" + value[1]).length > 1 ? ("" + value[1]) : ("0" + value[1]);
                    return value[0] + "-" + month + "-" + day;
                }
            }else if(O.type === "checkbox" ){
                return O.input.is(":checked")?"true":"false"; 
            }else if (O.type === "year_date"){
                return O.input.val().trim();
            }else{
                return O.input.val();
            }
        }

        set value(newValue) {
            let O = this;
             if(O.type === "checkbox" ){
                console.log(newValue);
                O.input.selected(newValue=='true'?true:false); 
            }
            else{
                O.val( newValue );
            }
        }

        clear() {
            let O = this;
            if(O.type !== "checkbox" )
                O.input.val( "" );
            else
                O.input.checked = false;

            if (O.input.$validationContainer) {
                O.input.$validationContainer.css("display", "none") ;
            }

            // Remove validation classes
            O.group.removeClass("has-success has-error");
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
        /**
         * @returns {boolean} If the input is valid.
         */
        validate() {
            let O = this;
            _log( 'PANEL  / _validateForm' );
           
            let validationErrors = [];
            // remove error classes
            O.input.find( '.has-error' ).removeClass( 'has-error' );
            O.input.find( '.is-invalid' ).removeClass( 'is-invalid' );
            O.input.find( '.validation-message' ).empty();
            O.input.$validationContainer.text('');
            
            let isValid = true;
            if (!O.mandatory) {
                isValid = true;
            }else if(O.isSID){
                let fieldValue = O.input.val();
				let idRegexp = /^[A-Za-z_^s]\w*$/;
				// name fits regexp
				if ( ! idRegexp.test( fieldValue ) ) {
                    O.input.$validationContainer.text('Parameter ID is not a valid (SId)');
                    isValid =  false;
				}
			
            }else {
                isValid = O.input.val() ? true : false;
                if(!isValid){
	//remove "required" message
                    //O.input.$validationContainer.text("required");
					isValid = true;
                }
                

            }
			// check if mail has correct structure, provided there is a value
            if(O.input.val() && O.type === "email"){
				if(O.input.val().trim()){
					isValid= /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(O.input.val());
                	O.input.$validationContainer.text("Not a valid email value");	
				}
            }
            if (!isValid) {
                O.input.parents( '.form-group' ).addClass( 'has-error' );
			    O.input.$validationContainer.addClass( 'is-invalid' );
                O.input.$validationContainer.css("display", "block") ;
            }else{
                O.onblurHandler();
            }

            return isValid;
        }

        
    }