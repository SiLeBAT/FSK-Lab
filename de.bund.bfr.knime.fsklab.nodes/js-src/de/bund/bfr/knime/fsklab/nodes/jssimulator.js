simulator = function() {

	var view = { version: "0.0.1" };
	view.name = "JavaScript Simulation Configurator"

	var _rep;
	var _val;

	view.init = function(representation, value) {
		_rep = representation;
		_val = value;

		alert("Yahaha! You found me!");

		alert(JSON.stringify(_rep));
		alert(JSON.stringify(_val));
	};

	view.getComponentValue = function() {
		return _val;
	};

	return view;
}();