// For debug purposes
function printKeys(object) {
	for (var key in object) { alert(key); }
}

js_editor = function() {
	var editor = {
		version: "1.0.0"
	};

	editor.name = "JavaScript editor";

	var editorValue;
	var editorRep;

	var models;
	var dbAgents;
	var dbMatrices;
	var dbUnits;
	var dbLiterature;

	editor.init = function(representation, value) {
		editorValue = value;
		editorRep = representation;		

		models = value.models.models;

		dbAgents = value.dbAgents.agents;  // Keeps agents from DB
		dbMatrices = value.dbMatrices.matrices;  // Keeps matrices from DB
		dbLiterature = value.dbLiteratureItems.literature;  // Keeps literature from DB

		// Populates dbUnits with unit strings from DB
		dbUnits = new Array();
		for (var i = 0; i < value.dbUnits.units.length; i++) {
			var currentUnit = value.dbUnits.units[i];
			if (currentUnit.displayInGuiAs) {
				dbUnits.push(currentUnit.displayInGuiAs);
			}
		}

		create_body();
		create_accordion();
		create_block();
	};

	editor.value = function() {
		return editorValue;
	};

	/****************HTML BODY****************/
	function create_body() {
		var header =  	'<div class="bs-docs-header" id="content" tabindex="-1" onclick="create_block()" >' + 
						'	<div class="container">' + 
						'		<h1>SBML Editor</h1>' + 
						'		<p>SBML Editor for Knime</p>' + 
						'		<div id="carbonads-container">' + 
						'			<div class="carbonad"><div id="azcarbon"></div></div>' + 
						'		</div>' + 
						'	</div>' +
						'</div>';

		var container = '<div class="container">' +
						'	<div class="row">' +
						'		<div class="col-md-3" id="leftCol">' +
						'			<div class="panel-group" ></div>' +
						'			<div class="panel-group" id="sidebar" role="tablist" aria-multiselectable="true"></div>' +
						'		</div>' +
						'		<div class="col-md-9" id="rightCol"></div>' +
						'	</div>' +
						'</div>';

		document.createElement('body');
		$("body").html(header + container);
	}

	/****************SIDEBAR ACCORDION****************/
	function create_accordion() {
		var accordion = '';

		for (var numModel = 0; numModel < models.length; numModel++) {

			var estModel = models[numModel].estModel;
			var name1 = estModel.name ? estModel.name.substring(0, 17) : "Model-" + numModel;

			var category = 'P';  // only dealing with primary models so far

			var primaryModel = '<li class="list-group-item clickable pencil lvl-1" data-id="' + numModel + '" data-value="-1" data-type="organism"> Organism </li>' +
							   '<li class="list-group-item clickable pencil lvl-1" data-id="' + numModel + '" data-value="-1" data-type="matrix"> Matrix </li>';

			accordion += '<div class="panel panel-default">' +
						 '  <div class="panel-heading collapsed clickable" data-id="' + numModel + '" data-value="-1" data-category="' + category + '" ' +
						 '    data-type="model" role="tab" data-toggle="collapse" data-parent="#sidebar" href="#collapse' + numModel + '" ' +
						 '    id="link-window-' + numModel + '"aria-expanded="false" aria-controls="collapse0">' +
						 '    <h4 class="panel-title">' +
						 '      <div class="type-model">' + category + '</div>' +
						 '      <a class="link-model">' + name1 + '</a>' +
						 '    </h4>' +
						 '  </div>' +
						 '  <div id="collapse' + numModel + '" class="panel-collapse collapse" role="tabpanel">' +
						 '    <ul class="list-group">' +
						        primaryModel +
						 '      <li class="list-group-item clickable pencil lvl-1" data-id="' + numModel + '" data-type="references"  data-category="' + category + '" data-value="-1"> References </li>' +
				         '	    <li class="list-group-item clickable pencil lvl-1" data-id="' + numModel + '" data-type="formula"  data-category="' + category + '"  data-value="-1"> Formula</li>' +
				         '    </ul>' +
				         '  </div>' +
				         '</div>';
		}

		$("#sidebar").append(accordion);
		$(".second").hide();
	}

	/****************BLOCK RIGHT****************/
	function create_block() {

		var container = '<div class="bs-callout bs-callout-info" id="callout-popover-data">' +
						'	<h4 id="titleModel"> Data attributes for individual popovers </h4>' +
						'	<p>Options for individual popovers can alternatively be specified through the use of data attributes, as explained above.</p>' +
						'	<div id="insertModule"></div>' +
						'	<div class="clearfix"></div></br>' +
						'	<div class="form-group"><input class="btn btn-default pull-right btn-submit" style="display:none" type="submit" onclick="save_block()" value="Submit"></div>' +
						'	<div class="clearfix"></div>' +
						'</div>';
		$("#rightCol").html(container);
		$(".btn-submit").click(save_block);
		load_block();
	}

	/****************LOAD AND SAVE BLOCK: MODEL, MATRIX, ORGANISM, REFERENCES, FORMULAS, SECONDARY MODEL****************/
	function load_block() {

		$(".clickable").click(function(){ 

			$(".pencil").removeClass('pencil-active');
			$(this).addClass('pencil-active');
			$(this).find("div.pencil").addClass('pencil-active');

			var container = '<input type="hidden" class="form-control" id="primary" value="'+$(this).data('id')+'">' +
							'<input type="hidden" class="form-control" id="secondary" value="'+$(this).data('value')+'">' +
							'<input type="hidden" class="form-control" id="type" value="'+$(this).data('type')+'">' +
							'<input type="hidden" class="form-control" id="category" value="'+$(this).data('category')+'">';
							
			$("#insertModule").html(container);

			var modelNumber = $(this).data('id');
			var moduleType = $(this).data('type');

			switch (moduleType) {
				case "model":
					insert_model(modelNumber);
					break;
				case "organism":
					insert_organism(modelNumber);
					break;
				case "matrix":
					insert_matrix(modelNumber);
					break;
				case "references":
					insert_references(modelNumber);
					break;
				case "formula":
					insert_formula(modelNumber);
					break;
				default:
					break;
			}
		
			$(".btn-submit").show();
		});
	}

	function save_block() {
		index = $("#primary").val();
		value = $("#secondary").val();
		type = $("#type").val();
		category = $("#category").val();

		var modelNumber = $("#primary").val();
		var moduleType = $("#type").val();

		switch (moduleType) {
			case "model":
				save_model(modelNumber);
				break;
			case "organism":
				save_organism(modelNumber);
				break;
			case "matrix":
				save_matrix(modelNumber);
				break;
			case "references":
				save_references(modelNumber);
				break;
			case "formula":
				save_formula(modelNumber);
				break;
			default:
				break;
		}
	}

	/****************FUNCTIONS LOAD AND SAVE MODELS****************/
	function insert_model(modelNumber) {
		$("#titleModel").html("Primary model info");

		var estModel = models[modelNumber].estModel;

		var name = estModel.name ? estModel.name : "";
		var r2 = estModel.r2 ? estModel.r2 : "";
		var aic = estModel.aic ? estModel.aic : "";
		var bic = estModel.bic ? estModel.bic : "";
		var rms = estModel.rms ? estModel.rms : "";
		var comment = estModel.comment ? estModel.comment : "";
		var qualityScore = estModel.qualityScore ? estModel.qualityScore : 0;
		var checked = estModel.checked ? "checked" : "";

		function create_uncertainty_div(measure_name, measure_value) {
			var label = '<label for="' + measure_name + '">' + measure_name.toUpperCase() + "</label>";
			var input = '<input type="text" class="form-control" id="' + measure_name + '" value="' + measure_value + '">';
			var div = '<div class="col-xs-3">' +
						label +
						input +
					  '</div>';
			return div;
		}

		var container = '<form class="form-horizontal">' +
						'  <div class="form-group">' +
						'    <label for="title" class="col-sm-3 control-label">Title</label>' +
						'    <div class="col-sm-5">' +
						'      <input type="text" class="form-control" id="title" value="'+ name +'">' +
						'    </div>' +
						'  </div>' +

						'  <h4>Goodness of fit</h4>' +

						'  <div class="row col-md-offset-3">' +
  						     create_uncertainty_div("r2", r2) +
						     create_uncertainty_div("rms", rms) +
						'  </div>' +

						'  <div class="row col-md-offset-3">' +
	  					     create_uncertainty_div("aic", aic) +
  						     create_uncertainty_div("bic", bic) +
						'  </div>' +

						'  <h4>Subjective quality</h4>' +
						'  <div class="form-group">' +
						'    <label for="title" class="col-sm-3 control-label">Quality Score</label>' +
						'    <div class="col-sm-5">' +
						'      <select class="form-control" id="ddlQualityScore">' +
						'        <option value="0" style="color: black">White</option>' +
						'        <option value="1" style="color: green">Green</option>' +
						'        <option value="2" style="color: yellow">Yellow</option>' +
						'        <option value="3" style="color: red">Red</option>' +
						'      </select>' +
						'    </div>' +
						'  </div>'  +

						'  <div class="form-group">' +
						'    <label for="title" class="col-sm-3 control-label">Validated</label>' +
						'    <div class="col-sm-5">' +
						'		<input type="checkbox" id="validated" '+ checked +'>Checked</label>' +
						'    </div>' +
						'  </div>'  +

						'  <div class="form-group">' +
						'    <label for="title" class="col-sm-3 control-label">Description</label>' +
						'    <div class="col-sm-5">' +
						'	<div class="form-group"><textarea id="desc" class="form-control" rows="3">'+ comment +'</textarea></div>'+
						'    </div>' +
						'  </div>'  +
						'</form>';
		$("#insertModule").append(container);
		$("#ddlQualityScore").val(qualityScore);
	}

	function save_model(modelNumber) {
		function create_measure_error_msg(measure_name) {
			return "Goodness of Fitness (" + measure_name + "): Not a number";
		}

		if (isNaN($("#r2").val())) {
			print_error("error", create_measure_error_msg("R2"), "Error!");
		} else if (isNaN($("#aic").val())) {
			print_error("error", create_measure_error_msg("AIC"), "Error!");
		} else if (isNaN($("#bic").val())) {
			print_error("error", create_measure_error_msg("BIC"), "Error!");
		} else if (isNaN($("#rms").val())) {
			print_error("error", create_measure_error_msg("RMS"), "Error!");
		} else {
			var estModel = models[modelNumber].estModel;

			estModel.name = ($("#title").val()) ? $("#title").val(): null;
			estModel.r2 = ($("#r2").val()) ? $("#r2").val() : null ;
			estModel.aic = ($("#aic").val()) ? $("#aic").val() : null;
			estModel.bic = ($("#bic").val()) ? $("#bic").val() : null;	
			estModel.rms = ($("#rms").val()) ? $("#rms").val() : null;
			estModel.comment = ($("#desc").val()) ? $("#desc").val() : null;
			estModel.checked = $("#validated").prop("checked");
			estModel.qualityScore = $("#ddlQualityScore").val();

			if (estModel.name) {
				$("#link-window-"+ modelNumber +" h4 a.link-model").text(estModel.name.substring(0, 17));	
			} else {
				$("#link-window-"+ modelNumber +" h4 a.link-model").text("Model-"+id);
			}
			print_error("success", "Data Model Save", "Save!");
		}
	}

	/****************FUNCTIONS LOAD AND SAVE ORGANISM****************/
	function insert_organism(modelNumber) {

		function load_organism_combo(selected_organism_id) {
			var list = '';
			for (var i = 0; i < dbAgents.length; i++) {
				var organism = dbAgents[i];
				var selected = (organism.id === selected_organism_id) ? "selected" : "";
				list += '<option value="' + organism.id + '" "' + selected + ">" + organism.name + "</option>";
			}

			$("#ddlOrganism").html(list);
		}

		$("#titleModel").html("Organism info");

		var container = '<form class="form-horizontal">' +
						'  </br>' +
						'  <div class="form-horizontal">' +
						'    <label for="ddlOrganism" class="col-sm-3 control-label">Organism</label>' +
						'    <div class="col-sm-5">' +
						'      <select type="text" class="form-control" id="ddlOrganism"></select>' +
						'    </div>' +
						'  </div>' +
						'</form>';

		$("#insertModule").append(container);
		var selected_organism_id = models[modelNumber].id;
		load_organism_combo(selected_organism_id);
		$("#ddlOrganism").select2();
	}

	function save_organism(modelNumber) {
		var organism_id = parseInt($('#ddlOrganism').val());
		var organism_name = '';
		var organism_detail = '';
		var organism_dbuuid = '';

		// Search agent
		for (var i = 0; i < dbAgents.length; i++) {
			var organism = dbAgents[i];
			if (organism_id === organism.id) {
				organism_name = organism.name;
				organism_detail = organism.detail;
				organism_dbuuid = organism.dbuuid;
				break;
			}
		}

		models[modelNumber].agent.id = organism_id;
		models[modelNumber].agent.name = organism_name;
		models[modelNumber].agent.detail = organism_detail;
		models[modelNumber].agent.dbuuid = organism_dbuuid;

		print_error("success", "Save Organism", "Save!");
	}

	/****************FUNCTIONS LOAD AND SAVE MATRIX****************/
	function insert_matrix(modelNumber){

		function load_matrix_combo(selected_matrix_id) {
			var list = '';
			for (var i = 0; i < dbMatrices.length; i++) {
				var matrix = dbMatrices[i];
				var selected = (matrix.id === selected_matrix_id) ? "selected" : "";
				list += '<option value="' + matrix.id + '" "' + selected + ">" + matrix.name + "</option>";
			}

			$("#ddlMatrix").html(list);
		}

		$("#titleModel").html("Matrix info");
		
		var container = '<form class="form-horizontal">' +
						'  </br>' +
						'  <div class="form-group">' +
						'    <label for="identifier" class="col-sm-3 control-label">Matrix</label>' +
						'    <div class="col-sm-5">' +
						'      <select type="text" class="form-control" id="ddlMatrix"></select>' +
						'    </div>' +
						'  </div>' +
						'</form>';
		
		
		$("#insertModule").append(container);
		var selected_matrix_id = models[modelNumber].matrix.id;
		load_matrix_combo(selected_matrix_id);
		$("#ddlMatrix").select2();
	}

	function save_matrix(modelNumber) {

		var matrix_id = parseInt($('#ddlMatrix').val());
		var matrix_name = '';
		var matrix_detail = '';
		var matrix_dbuuid = '';

		// Search matrix
		for (var i = 0; i < dbMatrices.length; i++) {
			var matrix = dbMatrices[i];
			if (matrix_id === matrix.id) {
				matrix_name = matrix.name;
				matrix_detail = matrix.detail;
				matrix_dbuuid = matrix.dbuuid;
				break;
			}	
		}

		models[modelNumber].matrix.id = matrix_id;
		models[modelNumber].matrix.name = matrix_name;
		models[modelNumber].matrix.detail = matrix_detail;
		models[modelNumber].matrix.dbuuid = matrix_dbuuid;

		print_error("success", "Save Matrix", "Save!");
	}

	/****************FUNCTIONS LOAD, SAVE REFERENCES****************/
	function insert_references(modelNumber) {

		function add_reference(reference_id, reference_title) {
			var row = '<tr data-id="'+ reference_id + '">' +
					  '  <td><input type="checkbox"></td>' +
					  '  <td>' + reference_id + '</td>' +
					  '  <td>' + reference_title + '</td>' +
					  '</tr>';
			$("#ref_table_body").append(row);
		}

		function load_references_combo() {
			var list = '';
			for (var j = 0; j < dbLiterature.length; j++) {
				var id = dbLiterature[j].id;
				var title = dbLiterature[j].title;
				list += '<option value="' + id + '">"' + title + "</option>";
			}

			$("#ddlReference").html(list);

			// Remove literature items in the table from the combo list
			$('#ref_table_body tr').each(function(){
				$('#ddlReference option[value="' + $(this).data('id') + '"').remove();
			});
		}

		$("#titleModel").html("References info");

		var container = '<form class="form-horizontal">' +
						'  	<div class="form-group" id="listReference">' +
						'    	<table class="table">' +
						'      		<thead>' +
						'        		<tr><th>#</th><th>ID</th><th>Title</th></tr>' +
						'      		</thead>' +
						'      		<tbody id="ref_table_body"></tbody>' +
						'    	</table></br>' +
						'       <label for="identifier" class="col-sm-3 control-label">New Reference: </label>' +
						'    	<div class="col-sm-5">' +
						'      		<select type="text" class="form-control" id="ddlReference"></select>' +
						'    		</div>' +
						'    	<input id="clear_button" class="btn btn-default pull-right" type="button" value="Clear">' +					
						'  	</div>' +
						'</form>';
		$("#insertModule").append(container);

		// Adds literature items to the table
		var modelLiteratureItems = models[modelNumber].mLit.literature;
		for (var i = 0; i < modelLiteratureItems.length; i++) {
			var modelLiteratureItem = modelLiteratureItems[i];
			add_reference(modelLiteratureItem.id, modelLiteratureItem.title);
		}

		load_references_combo();

		$("#ddlReference").select2().on("change", function() {
			var selectedOption = $("select option:selected");	
			var id = selectedOption.val();
			var text = selectedOption.text();
			// add literature item to table
			add_reference(id, text);
			// update combo list
			$('#ddlReference option[value="' + id + '"').remove();
		});

		$("#clear_button").click(function () {
			$('#ref_table_body tr input:checked').parent().parent().remove();
			load_references_combo();
		});
	}

	function save_references(modelNumber) {
		var literatureItems = Array();
		$('#ref_table_body tr').each(function() {
			var literatureId = parseInt($(this).data('id'));
			for (var i = 0; i < dbLiterature.length; i++) {
				var dbLit = dbLiterature[i];
				if (literatureId === dbLit.id) {
					literatureItems.push(dbLit);
				}
			}
		});
		models[modelNumber].mLit.literature = literatureItems;

		print_error("success", "Save References", "Save!");
	}

	/****************FUNCTIONS LOAD AND SAVE FORMULA & PARAMETERS****************/
	function add_dep_parameter(dep) {
		var class_cell = '<td></td>';
		var name_cell = '<td>' + dep.name + '</td>';

		var dep_unit = dep.unit ? dep.unit : '';
		var unit_cell = '<td><a class="selectUnit editable editable-click">' + dep_unit + '</td>';

		var check_cell = '<td></td>';

		var dep_value = (dep.value == undefined) ? '-' : dep.value;
		var value_cell = '<td><a>' + dep_value + '</a></td>';

		var error_cell = '<td><a>-</a></td>';

		var dep_min = (dep.min == undefined) ? '-' : dep.min;
		var min_cell = '<td><a class="textEdit editable editable-click">' + dep_min + '</a></td>';

		var dep_max = (dep.max == undefined) ? '-' : dep.max;
		var max_cell = '<td><a class="textEdit editable editable-click">' + dep_max + '</a></td>';

		var descr = (dep.description == undefined) ? '-' : dep.description;
		var descr_cell = '<td><a class="description editable editable-click>"' + descr + "</a></td>";

		var row = '<tr id="' + dep.name + '" data-type="D" data-name="' + dep.name + '" >' +
				    class_cell +
				    name_cell +
				    unit_cell +
				    check_cell +
				    value_cell +
				    error_cell +
				    min_cell +
				    max_cell +
				    descr_cell +
				  '</tr>';
		$("#par_table_body").append(row);
	}

	function add_independent_parameter(indep) {
		var class_cell = '<td></td>';
		var name_cell = '<td>' + indep.name + '</td>';

		var indep_unit = indep.unit ? indep.unit : '';
		var unit_cell = '<td><a class="selectUnit editable editable-click">' + indep_unit + '</td>';

		var check_cell = '<td><input type="checkbox" checked class="checkeable"></td>';

		var indep_value = (indep.value == undefined) ? '-' : indep.value;
		var value_cell = '<td><a>' + indep_value + '</a></td>';
		var error_cell = '<td><a>-</a></td>';

		var indep_min = (indep.min == undefined) ? '-' : indep.min;
		var min_cell = '<td><a class="textEdit editable editable-click">' + indep_min + '</a></td>';

		var indep_max = (indep.max == undefined) ? '-' : indep.max;
		var max_cell = '<td><a class="textEdit editable editable-click">' + indep_max + '</a></td>';

		var descr = (indep.description == undefined) ? '-' : indep.description;
		var descr_cell = '<td><a class="description editable editable-click>"' + descr + "</a></td>";

		var row = '<tr id="' + indep.name + '" data-type="I" data-name="' + indep.name + '" >' +
				    class_cell +
				    name_cell +
				    unit_cell +
				    check_cell +
				    value_cell +
				    error_cell +
				    min_cell +
				    max_cell +
				    descr_cell +
				  '</tr>';
		$("#par_table_body").append(row);
	}

	function add_constant_parameter(param) {
		var condition_attribute = ' data-status="1" ';
		var class_attribute = ' class="col-sm-1 item-first some-remove" ';

		var editable = ' class="textEdit editable editable-click" '; 

		var class_cell = '<td ' + class_attribute + '></td>';
		var name_cell = '<td>' + param.name + '</td>';

		var param_unit = (param.unit === null) ? '' : param.unit;
		var unit_cell = '<td><a class="selectUnit editable editable-click">' + param_unit + '</a></td>';

		var check_cell = '<td><input type="checkbox" class="checkeable"></td>';

		var value = (param.value == undefined) ? '-' : param.value;
		var value_cell = '<td><a ' + editable + '>' + value + '</a></td>';

		var error = (param.error == undefined) ? '-' : param.error;
		var error_cell = '<td><a ' + editable + '>' + error + '</a></td>';

		var param_min = (param.min == undefined) ? '-' : param.min;
		var min_cell = '<td><a class="textEdit editable editable-click">' + param_min + '</a></td>';

		var param_max = (param.max == undefined) ? '-' : param.max;
		var max_cell = '<td><a class="textEdit editable editable-click">' + param_max + '</a></td>';

		var descr = (param.description == undefined) ? '-' : param.description;
		var descr_cell = '<td><a class="description editable editable-click>"' + descr + "</a></td>";

		var row = '<tr id="' + param.name + '" data-type="P" data-name="' + param.name + '" ' + condition_attribute + '>' +
				    class_cell +
				    name_cell +
				    unit_cell +
				    check_cell +
				    value_cell +
				    error_cell +
				    min_cell +
				    max_cell +
				    descr_cell +
				  '</tr>';
		$("#par_table_body").append(row);
	}

	function insert_formula(modelNumber) {

		function load_category_combo(selected_category) {
			var MODEL_CLASSES = ["unknown", "growth", "inactivation", "survival",
				"growth/inactivation", "inactivation/survival", "growth/survival",
				"growth/inactivation/survival"];

			var list = '';
			for (var i = 0; i < MODEL_CLASSES.length; i++) {
				var modelClass = MODEL_CLASSES[i];
				var selected = (i === selected_category) ? "selected" : "";
				list += '<option value="' + modelClass + '" "' + selected + ">" + modelClass + "</option>";
			}
			$("#ddlCategory").html(list);
		}

		// Gets current model and its catalog model
		var model = models[modelNumber];
		var catalogModel = model.catModel;

		// Updates header
		$("#titleModel").html("Primary formula");

		var originalFormula = catalogModel.formula;
		var name = catalogModel.name;

		var boundaryConditionStartPos = originalFormula.lastIndexOf("*((((");

		var equalsSignPos = originalFormula.indexOf("=");
		var formula;
		var boundary;

		// Formula has no boundary conditions
		if (boundaryConditionStartPos == -1){
			formula = originalFormula.substring(equalsSignPos + 1);
			boundary = '';
		}

		// Formula has boundary conditions
		else {
			var boundaryConditionEndPos = originalFormula.lastIndexOf(")))))");
			formula = originalFormula.substring(equalsSignPos + 1, boundaryConditionStartPos);
			boundary = originalFormula.substring(boundaryConditionStartPos + 6, boundaryConditionEndPos);
		}

 		var container = '<form class="form form-horizontal1">' +
				'  <div class="form-group">' +
				'    <label for="identifier" class="col-sm-3 control-label">Formula Name</label>' +
				'    <div class="col-sm-9">' +
				'      <input type="text" class="form-control" id="name" value="'+ name+ '">' +
				'    </div>' +
				'  </div>' + 
				'  <div class="form-group">' +
				'    <label for="identifier" class="col-sm-3 control-label">Formula</label>' +
				'    <div class="col-sm-9">' +
				'      <input type="text" class="form-control" id="formula" value="'+ formula + '" data-value="' + formula + '">' +
				'    </div>' +
				'  </div>' +

				'  <div class="form-group">' +
				'    <label for="title" class="col-sm-3 control-label">Boundary Conditions</label>' +
				'    <div class="col-sm-5">' +
				'      <input type="text" class="form-control" id="boundary_conditions" value="'+ boundary + '" data-value="' + boundary + '">' +
				'    </div>' +
				'  </div>' +
				'	<div class="form-group">' +
				'    <label for ="identifier" class="col-sm-3 control-label">Category</label>' +
				'    <div class="col-sm-5">' +
				'      <select type="text" class="form-control" id="ddlCategory"></select>' +
				'    </div>' +
				'  </div>' +
				' <div  id="divParameter">' +
				'  <table  id="tableList"> ' +
				'    <thead>' +
				'        <tr>' +
				'			 <th dta-field="Select" data-sortable="false">#</th>' +
				'            <th data-field="Parameter" data-sortable="true">Parameter</th>' +
				'            <th data-field="Unit" data-sortable="false">Unit</th>' +
				'            <th data-field="Indep" data-sortable="false">Ind</th>' +
				'            <th data-field="Value" data-sortable="false">Value</th>' +
				'            <th data-field="Error" data-sortable="false" >S.E.</th>' +
				'            <th data-field="Min" data-sortable="false">Min</th>' +
				'            <th data-field="Max" data-sortable="false" >Max</th>' +
				'            <th data-field="Description" data-sortable="false">Description</th>' +
				'        </tr>' +
				'    </thead>' +
				'    <tbody id="par_table_body"></tbody>' +
				'  </table>' +
				' </div>' +
				'</form>';
		$("#insertModule").append(container);

		// Loads category combo
		load_category_combo(catalogModel.modelClass);
		$("#ddlCategory").select2();

		// Adds dependent parameter
		add_dep_parameter(model.dep);

		// Adds independent parameters
		for (var indepNum = 0; indepNum < model.indeps.indeps.length; indepNum++) {
			add_independent_parameter(model.indeps.indeps[indepNum]);
		}

		// Adds constant parameters
		for (var paramNum = 0; paramNum < model.params.params.length; paramNum++) {
			add_constant_parameter(model.params.params[paramNum]);
		}

		$("#tableList").bootstrapTable();

		check_dependent();
		editable_text();
		editable_description();
		editable_unit();

		// TODO: 
		change_parameters(model.dep.name);
//		load_formula();
	}

	function check_dependent() {
		$(".checkeable").click(function() {
			var el = $(this).parent().parent();
			var class_cell = el.find("td:eq(0)");
			var value_cell = el.find("td:eq(4)");
			var status_cell = el.find("td:eq(5)");

			if ($(this).prop("checked")) {
		 		class_cell.removeClass('col-sm-1 item-first some-success');
		 		class_cell.removeClass('col-sm-1 item-first some-remove');
		 		value_cell.html("-");
		 		status_cell.html("-");
		 		el.data('type', 'I');
		 		class_cell.off("click");
			} else {
				if(el.data('status') == 2) {
		 			class_cell.addClass('col-sm-1 item-first some-success');
				} else {
		 			class_cell.addClass('col-sm-1 item-first some-remove');
		 		}
		 		value_cell.html('<a class="textEdit editable editable-click"></a>');
		 		status_cell.html('<a class="textEdit editable editable-click"></a>');
	 			el.data('type', 'P');

	 			editable_text();
			}
		});
	}

	function editable_text() {
		$('#tableList td a.textEdit').editable({
		    type: 'text',
		    name: 'description',
		    url: '/post',
		    title: 'Enter value',
		    validate: function(value) {
	    		if (isNaN(value)) return 'Not a number';
			}
		});
	}

	function editable_description() {
		$('#tableList td a.description').editable({
		    type: 'textarea',
		    name: 'description',
		    url: '/post',
		    title: 'Enter description'
		});
	}

	function editable_unit() {
		$('#tableList td a.selectUnit').editable({
		    type: 'select2',
		    name: 'description',
		    title: 'Enter unit',
	       	source: dbUnits,
		    select2: { width: '230px', allowClear: true }
		});
	}

	function change_parameters(name) {
		$("#formula, #boundary_conditions").change(function() {
			if($("#formula").val() === ''){
				$("#formula").val($("#formula").data('value'));
				$("#boundary_conditions").val($("#boundary_conditions").data('value'));
				print_error("error", "Incorrect Formula 1", "Error!");
				return;
			}

			try{
				var cond;
				var boundaryConditionsVal = $("#boundary_conditions").val();
				if (boundaryConditionsVal === "") {
					cond = "";
				} else {
					cond = "*" + boundaryConditionsVal;
				}
				var tree = jsep($("#formula").val() + cond);
			} catch(err) {
				$("#formula").val($("#formula").data('value'));
				$("#boundary_conditions").val($("#boundary_conditions").data('value'));
				print_error("error", "Incorrect Formula 2", "Error!");
				return;
			}	 

			var ids = [name];
			function parseIds(node) {
				if (!node) {
					return;
				}
				parseIds(node.left);
				parseIds(node.right);
				// Parse arguments
				if (node.arguments) {
					for (var narg = 0; narg < node.arguments.length; narg++) {
						parseIds(node.arguments[narg]);
					}
				}
				if (node.type == "Identifier") {
					ids.push(node.name);
				}
			}
			parseIds(tree);
			ids = ids.filter(function(item, pos, self) {
				return self.indexOf(item) == pos;
			});

			cad = "";
			$.each(ids, function(index, value){
				cad += "#" + value + ","; 

				if ($("#" + value).length == 0) {
					par = {name : value, min: '-', max: '-', description: '-', unit: '-'}; 
					add_constant_parameter(par);
					editable_text();
					editable_description();
					editable_unit();
					check_dependent();
				}
			});

			cad = cad.substring(0, cad.length - 1);  // Trims last comma
			$("#par_table_body tr:not('" + cad + "')").remove();
		
			$("#formula").data('value', $("#formula").val());
			$("#boundary_conditions").data('value', $("#boundary_conditions").val());
		});
	}

	function load_formula() {
		$("#ddlFormula").on("change", function() {
			if ($(this).val() === "") return;
			var num = $(this).val();

			$("#par_table_body").html("");

			check_dependent();
			editable_text();
			editable_description();
			editable_unit();
		});
	}

	function save_formula(modelNumber) {
		var formula = $("#formula").val();
		var boundaryConditions = $("#boundary_conditions").val();

		var cond;
		if (boundaryConditions == "") {
			cond = "";
		} else {
			cond = "*(((((" + boundaryConditions + ")))))";
		}

		var catModel = models[modelNumber].catModel;

		catModel.name = $("#name").val();
		catModel.modelClass = $("#ddlCategory").val() * 1;

		// Only for primary models
		var rule = "Value=" + formula + cond;
		if (catModel.formula !== rule) {
			catModel.id = get_random();
			catModel.formula = rule;
			catModel.dbuuid = "";
		}

		save_parameters(modelNumber);
		print_error("success", "Save Formula & Parameters", "Save!");
	}

	function save_parameters(model_number) {
		var model = models[model_number];
		model.indeps.indeps = Array();  // independent parameters
		model.params.params = Array();  // constant parameters

		// Takes parameters from the table, one parameter a time
		$("#par_table_body tr").each(function() {

			function row2Dep(row) {

				var unit_cell = row.find("td:eq(2) a");
				var min_cell = row.find("td:eq(6) a");
				var max_cell = row.find("td:eq(7) a");
				var desc_cell = row.find("td:eq(8) a");	

				var dep = {};
				dep["@class"] = "de.bund.bfr.knime.pmm.js.common.Dep";
				dep.name = row.data('name');
				dep.origname = row.data('name');
				dep.min = min_cell.text() === "-" ? null : parseFloat(min_cell.text());
				dep.max = max_cell.text() === "-" ? null : parseFloat(max_cell.text());
				dep.category = null;  // TODO: need to fill category according to `unit`
				dep.unit = unit_cell.text() === "-" ? null : unit_cell.text();
				dep.description = desc_cell.text() === "Empty" ? null : desc_cell.text();

				return dep;
			}

			function row2Indep(row) {
				var unit_cell = row.find("td:eq(2) a");
				var min_cell = row.find("td:eq(6) a");
				var max_cell = row.find("td:eq(7) a");
				var desc_cell = row.find("td:eq(8) a");	

				var indep = {};
				indep["@class"] = "de.bund.bfr.knime.pmm.js.common.Indep";
				indep.name = row.data('name');
				indep.origname = row.data('name');
				indep.min = min_cell.text() === "-" ? null : parseFloat(min_cell.text());
				indep.max = max_cell.text() === "-" ? null : parseFloat(max_cell.text());
				indep.category = null;  // TODO: need to fill category according to `unit`
				indep.unit = unit_cell.text() === "Empty" ? null : unit_cell.text();
				indep.description = desc_cell.text() === "Empty" ? null : desc_cell.text();

				return indep;
			}

			function row2Param(row) {
				var unit_cell = row.find("td:eq(2) a");
				var value_cell = row.find("td:eq(4) a");
				var error_cell = row.find("td:eq(5) a");
				var min_cell = row.find("td:eq(6) a");
				var max_cell = row.find("td:eq(7) a");
				var desc_cell = row.find("td:eq(8) a");	

				var param = {};
				param["@class"] = "de.bund.bfr.knime.pmm.js.common.Param";
				param.name = row.data('name');
				param.origname = row.data('name');
				param.isStart = null;
				param.value = value_cell.text() === "-" ? null : parseFloat(value_cell.text());
				param.error = value_cell.text() === "-" ? null : parseFloat(error_cell.text());
				param.min = min_cell.text() === "-" ? null : parseFloat(min_cell.text());
				param.max = max_cell.text() === "-" ? null : parseFloat(max_cell.text());
				param.p = null;
				param.t = null;
				param.minGuess = null;
				param.maxGuess = null;
				param.category = null;  // TODO
				param.unit = unit_cell.text() === "Empty" ? null : unit_cell.text();
				param.description = desc_cell.text() === "Empty" ? null : desc_cell.text();
				param.correlationNames = null;
				param.correlationValues = null;

				return param;

			}

			// Saves dependent, independent and constant parameters
			var param_type = $(this).data('type');
			if (param_type === 'D') {
				model.dep = row2Dep($(this));
			} else if (param_type === 'I') {
				model.indeps.indeps.push(row2Indep($(this)));
			} else if (param_type === 'P') {
				model.params.params.push(row2Param($(this)));
			}
		});
	}	

	/****************GLOBAL FUNCTIONS****************/
	/** Gets a random negative integer in the interval [-999999, -100000]. */
	function get_random(){
		var randNumMin = -999999;
		var randNumMax = -100000;
		return (Math.floor(Math.random() * (randNumMax - randNumMin + 1)) + randNumMin);
	}

	function print_error(type, msg, title){
		toastr.options = {
			"closeButton": true,
			"debug": true,
			"newestOnTop": false,
			"progressBar": true,
			"positionClass": "toast-top-right",
			"preventDuplicates": false,
			"showDuration": "300",
			"hideDuration": "100",
			"timeOut": "5000",
			"extendedTimeOut": "1000",
			"showEasing": "swing",
			"hideEasing": "linear",
			"showMethod": "fadeIn",
			"hideMethod": "fadeOut"
		};
		
		return toastr[type](msg, title);
	}

	return editor;	
}();