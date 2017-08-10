fsk_editor = function () {

    var editor = { version: "0.0.1" };
    editor.name = "FSK Editor";

    var _val;
    var _rep;

    editor.init = function (representation, value)
    {
        _rep = representation;
        _val = value;

        create_body ();
    };

    editor.getComponentValue = function ()
    {
        return _val;
    };

    return editor;

    function create_body ()
    {
        document.createElement("body");
        $("body").html('<div class="container"></div>');
        $(".container").append(
            '<ul class="nav nav-pills">' +
            '  <li role="presentation" class="active"><a href="javascript:void(0)">Model</a></li>' +
            '  <li role="presentation"><a href="javascript:void(0)">Parameters</a></li>' +
            '  <li role="presentation"><a href="javascript:void(0)">Visualization</a></li>' +
            '</ul>');

        $(".container").append('<pre><code contenteditable></code></pre>');
        $(".container pre code").text(_val.modelScript);

        $(".container ul li:eq(0) a").click(function () { tab_event(0); });
        $(".container ul li:eq(1) a").click(function () { tab_event(1); });
        $(".container ul li:eq(2) a").click(function () { tab_event(2); });

        $(".container").append(
            '<div>' +
            '  <input class="btn btn-danger" type="submit" value="Save">' +
            '</div>');
        $(".btn").click(function () {
            var activeTabName = $(".active a").text();

            if (activeTabName === "Model") {
                _val.modelScript = $(".container pre code").text();
            } else if (activeTabName === "Parameters") {
                _val.paramScript = $(".container pre code").text();
            } else if (activeTabName === "Visualization") {
                _val.vizScript = $(".container pre code").text();
            }
        });
    }

    /**
     * Event of selecting a tab.
     * The code is updated to follow the selected tab and the selected tab is
     * marked as active.
     * 
     * scriptType may be 0 (model), 1 (param) or 2 (viz).
     */
    function tab_event(scriptType) {

        // Remove active class to the button that was previously actived
        $(".active").removeClass("active");
        $(".container ul li:eq(" + scriptType + ")").addClass("active");

        if (scriptType == 0) {
            $(".container pre code").text(_val.modelScript);
        } else if (scriptType == 1) {
            $(".container pre code").text(_val.paramScript);
        } else if (scriptType == 2) {
            $(".container pre code").text(_val.vizScript);
        }
    }
}();
