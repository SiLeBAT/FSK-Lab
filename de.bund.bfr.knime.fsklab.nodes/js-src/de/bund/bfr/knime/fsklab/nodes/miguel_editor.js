fskeditorjs = function() {
    
    const view = { version: "1.0.0" };
    view.name = "Javascript FSK Editor";

    var _rep;
    var _val;

    var _metadata = {
      generalInformation : {},
      scope : {},
      dataBackground : {},
      modelMath : {}
    };

    var _modelCodeMirror;
    var _visualizationCodeMirror;
    var _readmeCodeMirror;

    view.init = function(representation, value) {
      
      _rep = representation;
      _val = value;

      if (!value.modelMetaData || value.modelMetaData == "null" || value.modelMetaData == "") {
        _metadata.generalInformation = {};
        _metadata.scope = {};
        _metadata.modelMath = {};
        _metadata.dataBackground = {}
      } else {
        let metaData = JSON.parse(value.modelMetaData);
        if (metaData) {
          metaData = traverse(metaData);
        }
  
        _metadata.generalInformation = metaData.generalInformation;
        _metadata.scope = metaData.scope;
        _metadata.modelMath = metaData.modelMath;
        _metadata.dataBackground = metaData.dataBackground;
      }      

      createUI();
    }

    view.getComponentValue = () => _val;
    view.validate = () => true;

    return view;

    /** Functions taken from the old editor to parse metadata. */
    function isObj(obj) {
      return obj ? obj.constructor.name === "Object" : false;
    }

    function traverse(obj) {
      let keys = Object.keys(obj)
      for (let i = 0; i < keys.length; ++i) {
        let val = obj[keys[i]]
        if (isObj(val)) {
          traverse(val)
        } else if (Array.isArray(val)) {
          for (let j = 0; j < val.length; ++j) {
            if (isObj(val[j]) && val[j] != null) {
              traverse(val[j])
            }else{
              delete obj[keys[i]];
            }
          }
        } else {
          if(val == null){
            delete obj[keys[i]]
          }
        }
      }
      return  obj;
    }

    /** UI code. */
    function createUI() {

        let bodyContent = `
<div class="container-fluid">
  <ul class="nav nav-tabs" id="viewTab" role="tablist">
    <li role="presentation" class="active dropdown">
      <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
        aria-haspopup="true" aria-expanded="false">
        General information <span class="caret">
      </a>
      <ul class="dropdown-menu">
        <li><a href="#generalInformation" aria-controls="generalInformation" role="button" data-toggle="tab">General</a></li>
        <li><a href="#modelCategory" aria-controls="modelCategory" role="button" data-toggle="tab">Model category</a></li>
        <li><a href="#modificationDate" aria-controls="modificationDate" role="button" data-toggle="tab">Modification date</a></li>
        <li><a href="#author" aria-controls="author" role="button" data-toggle="tab">Author</a></li>
        <li><a href="#creator" aria-controls="creator" role="button" data-toggle="tab">Creator</a></li>
        <li><a href="#reference" aria-controls="reference" role="button" data-toggle="tab">Reference</a></li>
      </ul>
    </li>
    <li role="presentation" class="dropdown">
      <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
        aria-haspopup="true" aria-expanded="false">
        Scope <span class="caret">
      </a>
      <ul class="dropdown-menu">
        <li><a href="#scopeGeneral" aria-controls="#scopeGeneral" role="button" data-toggle="tab">General</a></li>
      </ul>
    <li role="presentation">
      <a href="#modelScript" aria-controls="modelScript" role="tab"
        data-toggle="tab">Model script</a>
    </li>
    <li role="presentation">
      <a href="#visualizationScript" aria-controls="visualizationScript"
        role="tab" data-toggle="tab">Visualization script</a>
    </li>
    <li role="presentation">
      <a href="#readme" aria-controls="readme" role="tab" data-toggle="tab">README</a>
    </li>
  </ul>

  <div class="tab-content" id="viewContent">
    <div role="tabpanel" class="tab-pane active" id="generalInformation">
      ${createGeneralInformationForm()}
    </div>

    <div role="tabpanel" class="tab-pane" id="modelCategory">
      ${createModelCategory()}
    </div>

    <div role="tabpanel" class="tab-pane" id="modificationDate">
      ${createModificationDate()}
    </div>

    <div role="tabpanel" class="tab-pane" id="author">
      ${createAuthor()}
    </div>

    <div role="tabpanel" class="tab-pane" id="creator">
      ${createCreator()}
    </div>

    <div role="tabpanel" class="tab-pane" id="reference">
      Reference
    </div>

    <div role="tabpanel" class="tab-pane" id="scopeGeneral">Scope general</div>

    <div role="tabpanel" class="tab-pane" id="modelScript">
      <textarea id="modelScriptArea" name="modelScriptArea">${_val.firstModelScript}</textarea>    
    </div>

    <div role="tabpanel" class="tab-pane" id="visualizationScript">
      <textarea id="visualizationScriptArea" name="visualizationScriptArea">${_val.firstModelViz}</textarea>    
    </div>

    <div role="tabpanel" class="tab-pane" id="readme">
      <textarea id="readmeArea" name="readmeArea">${_val.readme}</textarea>
    </div>
  </div>
</div>`;

        document.createElement('body');
        $('body').html(bodyContent);

        // Create code mirrors for text areas with scripts and readme
        _modelCodeMirror = createCodeMirror("modelScriptArea", "text/x-rsrc");
        _visualizationCodeMirror = createCodeMirror("visualizationScriptArea", "text/x-rsrc");
        _readmeCodeMirror = createCodeMirror("readmeArea", "htmlmixed");

        // Every time a tab is shown
        $('.nav-tabs a').on('shown.bs.tab', () => {
            // Refresh code mirrors
            _modelCodeMirror.refresh();
            _visualizationCodeMirror.refresh();
            _readmeCodeMirror.refresh();
        });
    }

  // Create a CodeMirror for a given text area
	function createCodeMirror(textAreaId, language) {
		return window.CodeMirror.fromTextArea(document.getElementById(textAreaId),
				{
					lineNumbers: true,
					lineWrapping: true,
					extraKeys: {'Ctrl-Space': 'autocomplete'},
					mode: {'name': language}
				});
  }

  function createStringGroup(label, id, value, helperText) {
    return `<div class="form-group row">
      <label for="${id}" class="col-sm-2 col-form-label">${label}</label>
      <div class="col-sm-10">
        <input type="text" class="form-control" id="${id}" value="${value}" placeholder="${helperText}">
      </div>
    </div>`;
  }

  function createCheckboxGroup(label, id, value, helperText) {
    return `<div class="form-check">
      <input class="form-check-input" type="checkbox" value="" id="${id}" ${value ? "checked" : ""}>
      <label class="form-check-label" for="${id}">${label}</label>
    </div>`;
  }

  function createUrlGroup(label, id, value, helperText) {
    return `<div class="form-group row">
      <label for="${id}" class="col-sm-2 col-form-label">${label}</label>        
      <div class="col-sm-10">
        <input type="url" class="form-control" id="${id}" value="${value}" placeholder="${helperText}">
      </div>
    </div>`
  }

  function createDateGroup(label, id, value, helperText) {
    return `<div class="form-group row">
      <label for="${id}" class="col-sm-2 col-form-label">${label}</label>        
      <div class="col-sm-10">
        <input type="date" class="form-control" value="${value}" id="${id}">
      </div>
    </div>`
  }

  function createForm(formData) {
    let value = _metadata.generalInformation[formData.id];
    if (formData.type === 'text')        
      return createStringGroup(formData.label, formData.id, value, formData.description);
    if (formData.type === 'boolean')
      return createCheckboxGroup(formData.label, formData.id, value, formData.description);
    if (formData.type === 'url')
      return createUrlGroup(formData.label, formData.id, value, formData.description);
    if (formData.type === 'date')
      return createDateGroup(formData.label, formData.id, value, formData.description)
  }

  /** Create a table with a single (data) column to edit a string array. */
  function createStringTable(label) {
    return `<div>
    <span class="pull-right">
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
      </button>
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
      </button>
    </span>
    <table class="table">
      <tr>
        <th><input type="checkbox"></th>
        <th>${label}</th>
      </tr>
      <tr></tr>
      </tr>
    </table>
   </div>`;
  }

  function createDateTable(label) {
    return `<div>
    <span class="pull-right">
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
      </button>
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
      </button>
    </span>
    <table class="table">
      <tr>
        <th><input type="checkbox"></th>
        <th>${label}</th>
      </tr>
      <tr></tr>
    </table>
   </div>`;
  }

  /**
   * Create a Bootstrap 3 panel.
   * - title: string
   * - body: HTML string
   */
  function createPanel(title, body) {
    return `<div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">${title}</h3>
      </div>
      <div class="panel-body">${body}</div>
    </div>`;
  }
  
  function createGeneralInformationForm() {

    let form = "<form>"
    for (prop of ui['generalInformation']) {
      form += createForm(prop);
    }
    form += "</form>";

    return createPanel("General", form);
  }

  function createModelCategory() {

    let body = `<form>
    ${createForm(ui.modelCategory[0])}
    ${createForm(ui.modelCategory[1])}
    </form>
    ${createStringTable("Model sub class")}
    ${createStringTable("Basic process")}`;

    return createPanel("Model category", body);
  }

  function createModificationDate() {
    return createPanel("Modification date", createDateTable("Modification date"));
  }

  function createContactTable() {
    return `<div>
    <span class="pull-right">
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
      </button>
      <button type="button" class="btn btn-default">
        <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
      </button>
    </span>
    <table class="table">
      <tr>
        <th><input type="checkbox"></th>
        <th>Title</th>
        <th>Family name</th>
        <th>Given name</th>
        <th>Email</th>
        <th>Telephone</th>
        <th>Street address</th>
        <th>Country</th>
        <th>Zip code<th>
        <th>Region</th>
        <th>Time zone</th>
        <th>Gender</th>
        <th>Note</th>
        <th>Organization</th>
      </tr>
      <tr></tr>
    </table>
   </div>`;
  }

  function createAuthor() {
    return createPanel("Author", createContactTable());
  }

  function createCreator() {
    return createPanel("Creator", createContactTable());
  }
}();