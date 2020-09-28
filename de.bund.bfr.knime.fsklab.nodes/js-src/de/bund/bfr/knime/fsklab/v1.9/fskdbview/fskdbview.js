fskdbview = function() {

	var view = {
		version : "0.0.1"
	};
	view.name = "FSK DB View"

	var _rep;
	var _val;

	view.init = function(representation, value) {
		_rep = representation;
		_val = value;
	};

	view.getComponentValue = function() {
		return _val;
	};
	view.validate = function() {
		return true;
	}

	return view;

}();
