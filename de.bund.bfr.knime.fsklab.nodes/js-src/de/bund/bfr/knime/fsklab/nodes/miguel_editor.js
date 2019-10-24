fskeditorjs = function() {
    
    const view = { version: "1.0.0" };
    view.name = "Javascript FSK Editor";

    var _rep;
    var _val;

    var _modelCodeMirror;
    var _visualizationCodeMirror;
    var _readmeCodeMirror;

    view.init = function(representation, value) {
      console.log(value.ModelMetaData);
      console.log(JSON.parse(value.ModelMetaData));


        _rep = representation;
        _val = value;

        createUI();
    }

    view.getComponentValue = () => _val;
    view.validate = () => true;

    return view;

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
      Modification date
    </div>

    <div role="tabpanel" class="tab-pane" id="author">
      Author
    </div>

    <div role="tabpanel" class="tab-pane" id="creator">
      Creator
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
        _visualizationCodeMirror = createCodeMirror("visualizationScriptArea", "text/x-rsrc")
        _readmeCodeMirror = createCodeMirror("readmeArea", "htmlmixed")

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
  
  function createGeneralInformationForm() {

    function createStringGroup(label, id, helperText) {
      return `<div class="form-group row">
        <label for="${id}" class="col-sm-2 col-form-label">${label}</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="${id}" placeholder="${helperText}">
        </div>
      </div>`;
    }

    function createCheckboxGroup(label, id, helperText) {
      return `<div class="form-check">
        <input class="form-check-input" type="checkbox" value="" id="${id}">
        <label class="form-check-label" for="${id}">${label}</label>
      </div>`;
    }

    function createUrlGroup(label, id, helperText) {
      return `<div class="form-group row">
        <label for="${id}" class="col-sm-2 col-form-label">${label}</label>        
        <div class="col-sm-10">
          <input type="url" class="form-control" id="${id}" placeholder="${helperText}">
        </div>
      </div>`
    }

    function createDateGroup(label, id, helperText) {
      return `<div class="form-group row">
        <label for="${id}" class="col-sm-2 col-form-label">${label}</label>        
        <div class="col-sm-10">
          <input type="date" class="form-control" value="" id="${id}">
        </div>
      </div>`
    }

    const generalInformationDescriptions = descriptions['generalInformation'];
    
    return `<form>
    ${createStringGroup("Name", "generalInformationName", generalInformationDescriptions['name'])}
    ${createStringGroup("Source", "generalInformationSource", generalInformationDescriptions['source'])}
    ${createStringGroup("Identifier", "generalInformationIdentifier", generalInformationDescriptions['identifier'])}
    ${createDateGroup("Creation date", "generalInformationCreationDate", generalInformationDescriptions['creationDate'])}
    ${createStringGroup("Rights", "generalInformationRights", generalInformationDescriptions['rights'])}
    ${createCheckboxGroup("Availability", "generalInformationAvailability", generalInformationDescriptions['availability'])}
    ${createStringGroup("URL", "generalInformationUrl", generalInformationDescriptions['url'])}
    ${createStringGroup("Format", "generalInformationFormat", generalInformationDescriptions['format'])}
    ${createStringGroup("Language", "generalInformationLanguage", generalInformationDescriptions['language'])}
    ${createStringGroup("Software", "generalInformationSoftware", generalInformationDescriptions['software'])}
    ${createStringGroup("Language written in", "generalInformationLanguageWrittenIn", generalInformationDescriptions['languageWrittenIn'])}
    ${createStringGroup("Status", "generalInformationStatus", generalInformationDescriptions['status'])}
    ${createStringGroup("Objective", "generalInformationObjective", generalInformationDescriptions['objective'])}
    ${createStringGroup("Description", "generalInformationDescription", generalInformationDescriptions['description'])}
    </form>`;
  }

  function createModelCategory() {

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
        <th>Model class</th>
        <th>Model sub class</th>
        <th>Model class comment</th>
        <th>Basic process</th>
      </tr>

      </table>
    </div>`;
  }


}();