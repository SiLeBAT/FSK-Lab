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
          <a class="nav-link active" id="modelScript-tab" data-toggle="tab"
            href="#modelScript" role="tab" aria-controls="modelScript"
            aria-selected="true">Model script</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="visualizationScript-tab" data-toggle="tab"
            href="#visualizationScript" role="tab"
            aria-controls="visualizationScript" aria-selected="true">
            Visualization script
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" id="readme-tab" data-toggle="tab"
            href="#readme" role="tab" aria-controls="readme"
            aria-selected="true">README</a>
        </li>
      </ul>

      <div class="tab-content id="viewContent">
        <div class="tab-pane fade show active" id="modelScript" role="tabpanel"
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
}();