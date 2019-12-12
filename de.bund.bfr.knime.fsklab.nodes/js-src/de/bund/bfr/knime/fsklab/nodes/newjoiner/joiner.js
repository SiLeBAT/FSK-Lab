joiner = function() {

    const view = { version: "1.0.0", name: "FSK Joiner"};

    let _representation;
    let _value;

    view.init = function(representation, value) {
        _representation = representation;
        _value = value;

        console.log("Hola mundo");
        createBody();
    }

    view.getComponentValue = function() {
        // TODO: getComponentValue
        return _value;
    }

    return view;

    function createBody() {

        $('body').html(`<div class="container-fluid">
<nav class="navbar navbar-default">
 <div class="navbar-collapse collapse">
   <ul class="nav navbar-nav" id="viewTab">
     <li role="presentation">
       <a id="join-tab" href="#joinPanel" aria-controls="joinPanel">Join</a>
     </li>
     <li role="presentation">
       <a id="join-tab" href="#generalInformationPanel" aria-controls="generalInformationPanel">General information</a>
     </li>
     <li role="presentation">
       <a id="join-tab" href="#scopePanel" aria-controls="scopePanel">Scope</a>
     </li>
     <li role="presentation">
       <a id="join-tab" href="#dataBackgroundPanel" aria-controls="dataBackgroundPanel">Data background</a>
     </li>
     <li role="presentation">
       <a id="join-tab" href="#modelMathPanel" aria-controls="modelMathPanel">Model math</a>
     </li>
   </ul>
 </div>
 <div class="tab-content" id="viewContent">
   <div role="tabpanel" class="tab-pane" id="joinPanel"></div>
 </div>
</nav>
</div>`);
    }
}();