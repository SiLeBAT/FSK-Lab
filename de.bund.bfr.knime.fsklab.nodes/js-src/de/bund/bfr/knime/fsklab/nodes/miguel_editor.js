fskeditorjs = function() {
    
    const view = { version: "1.0.0" };
    view.name = "Javascript FSK Editor";

    var _rep;
    var _val;

    var _modelCodeMirror;
    var _visualizationCodeMirror;
    var _readmeCodeMirror;

    view.init = function(representation, value) {
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
  <div class="row">
    <div class="col">
      <ul class="nav nav-tabs" id="viewTab" role="tablist">
        <li class="nav-item">
          <a class="nav-link active" id="generalInformation-tab"
             data-toggle="tab" href="#generalInformation" role="tab"
             aria-controls="generalInformation" aria-selected="true">
             <h3>General information</h3></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="modelScript-tab" data-toggle="tab"
            href="#modelScript" role="tab" aria-controls="modelScript"
            aria-selected="false"><h3>Model script</h3></a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="visualizationScript-tab" data-toggle="tab"
            href="#visualizationScript" role="tab"
            aria-controls="visualizationScript" aria-selected="false">
            <h3>Visualization script</h3>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="readme-tab" data-toggle="tab"
            href="#readme" role="tab" aria-controls="readme"
            aria-selected="false"><h3>README</h3></a>
        </li>
      </ul>

      <div class="tab-content" id="viewContent">
        <div class="tab-pane fade show active" id="generalInformation"
          role="tabpanel" aria-labelledby="generalInformation-tab">
          ${createGeneralInformationForm()}
          <!-- ${createGeneralInformationPanel()} -->
        </div>
        <div class="tab-pane fade show" id="modelScript" role="tabpanel"
          aria-labelledby="modelScript-tab">
          <textarea id="modelScriptArea" name="modelScriptArea">${_val.firstModelScript}</textarea>
        </div>
        <div class="tab-pane fade show" id="visualizationScript" role="tabpanel"
          aria-labelledby="visualizationScript-tab">
          <textarea id="visualizationScriptArea" name="visualizationScriptArea">${_val.firstModelViz}</textarea>
        </div>
        <div class="tab-pane fade show" id="readme" role="tabpanel"
          aria-labelledby="readme-tab">
          <textarea id="readmeArea" name="readmeArea">${_val.readme}</textarea>
        </div>
      </div>

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

  function createGeneralInformationPanel() {

    return `<ul class="nav nav-tabs flex-column">
      <li class="nav-item">
        <a class="nav-link active" id="generalInformationGeneral-tab"
          data-toggle="tab" href="#generalInformation" role="tab"
          aria-controls="generalInformation" aria-selected="true">General</a>
      </li>
      <li class="nav-item"><a class="nav-link" href="#">Model category</a></li>
      <li class="nav-item"><a class="nav-link" href="#">Modification date</a></li>
      <li class="nav-item"><a class="nav-link" href="#">Author</a></li>
      <li class="nav-item"><a class="nav-link" href="#">Creators</a></li>
      <li class="nav-item"><a class="nav-link" href="#">Reference</a></li>
    </ul>
    <div class="tab-content" id="generalInformationContent">
    </div>`;
  }
}();