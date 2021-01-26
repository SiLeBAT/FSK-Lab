
    /**
     * Create a Bootstrap 3 modal dialog.
     */
    class Dialog{

        /**
         * Create a Bootstrap 3 modal dialog.
         * 
         * ```
         * <div class="modal-fade">
         *   <div class="modal-dialog" role="document">
         *     <div class="modal-content">
         *       <div class="modal-header">
         *         <button>
         *           <span>
         *         </button>
         *         <h4 class="modal-title">title</h4>
         *       </div>
         *       <div class="modal-body">
         *         <form>...</form>
         *       </div>
         *       <div class="modal-footer">
         *         <button type="button">Close</button>
         *         <button type="button">Save changes</button>
         *       </div>
         *     </div>
         *   </div>
         * </div>
         * ```
         * 
         * @param {id} id Dialog id
         * @param {title} title Dialog title
         * @param {formData} formData Object with form data
         */
        constructor(id, title, formData, port) {
            let O = this;
            O.inputs = {};  // Hash of inputs by id

            // Index of the row currently edited. It is -1 if no row is being edited.
            // This is the case of when a new row is added.
            O.editedRow = -1;

            O.modal = document.createElement("div");
            O.create(id, title, formData, port);
        }

        create(id, title, formData, port) {
            let O = this;
            // modal body
            let form = $( '<form class="form-striped" no-immidiate-submit></form>' )
            formData.forEach(prop => {
                let inputForm = createForm(prop, null, port);
                if (inputForm) {
                    $(inputForm.group).appendTo( form )
                    O.inputs[prop.id] = inputForm;
                }
            });

            let modalBody = $( '<div class="modal-body p-0 sim-params"></div>' ) 
            let modalinnerBody = $( '<div class="tab-content h-100"></div>' ) 
            form.appendTo(modalinnerBody);
            modalinnerBody.appendTo( modalBody)
            

            // modal action
            // nav
            _$modalNav = $( '<div class="modal-body sim-select"></div>' )

            // navbar
            _$navBar = $( '<nav class="navbar sim-select">' )
            .appendTo( _$modalNav )
            .wrap( '<form></form>' ); 

            //  select label
            $( '<label class="col-4 col-md-3 sim-select-label" >'+title.replace("Add", "")+'</label>' )
            .appendTo( _$navBar );


            //  select actions
            _$dialogActions = $( '<div class="col-8"></div>' )
            .appendTo( _$navBar );

            let $actionGroup1 = $( '<div class="col-12"></div>' )
            .appendTo( _$dialogActions);

            closeButton = $( '<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-x"></i></button>' )
			.attr( 'id', 'simActionclose' )
			.attr( 'data-tooltip', '' )
            .attr( 'title', 'close' )
            .attr( 'data-dismiss', 'modal' )
            .appendTo( $actionGroup1 );
			// col divider
            $( '<div class="col-divider ml-auto ml-xs-0"></div>' )
            .appendTo( $actionGroup1 );
            saveButton = $( '<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-save"></i></button>' )
			.attr( 'id', 'save' )
			.attr( 'data-tooltip', '' )
			.attr( 'title', 'Save changes' )
			.appendTo( $actionGroup1 )
			.on( 'click', ( event ) => {
				// Validate inputs and stop saving if errors are found.
                let hasError = false;
                Object.values(O.inputs).forEach(input => {
                    if (!input.validate()) hasError = true;
                });
                if (hasError) return;
                
                $(O.modal).modal('hide');

                // Retrieve data and clear inputs
                let data = {};
                for (const inputId in O.inputs) {
                    let currentInput = O.inputs[inputId];
                    data[inputId] = currentInput.value; // Save input value
                    currentInput.clear(); // Clear input
                }

                if (O.editedRow != -1) {
                    O.panel.save(O.editedRow, data);
                    O.editedRow = -1;
                    Object.values(O.inputs).forEach(input => input.clear()); // Clear inputs
                } else {
                    O.panel.add(data);
                }
                
            } );
            $actionGroup1
			    .wrapInner( '<div class="row justify-content-end align-items-center"></div>' );
            

            let content = document.createElement("div");
            content.classList.add("modal-content");
            content.innerHTML = `<div class="modal-header">
                                <h1 class="modal-title">${title}</h1>
                                <button type="button" class="action action-pure action-lg ml-2" data-dismiss="modal" aria-label="Close"><i class="feather icon-x"></i></button>
                                </div>`;
            
            _$navBar.appendTo($(content));
            modalBody.appendTo($(content));
            
            let modalDialog = document.createElement("div");
            modalDialog.classList.add("modal-dialog", "modal-xl");
            modalDialog.setAttribute("role", "document");
            modalDialog.appendChild(content);

            O.modal.classList.add("modal", "fade", "modal-sim");
            O.modal.id = id;
            O.modal.tabIndex = -1;
            O.modal.setAttribute("role", "dialog");
            O.modal.appendChild(modalDialog);
            _appUI._initFormItems( form);
        }
    }