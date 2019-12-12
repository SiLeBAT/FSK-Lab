joiner = function() {

    const view = { version: "1.0.0", name: "FSK Joiner"};

    let _representation;
    let _value;

    view.init = function(representation, value) {
        _representation = representation;
        _value = value;

        console.log("Hola mundo");
    }

    view.getComponentValue = function() {
        // TODO: getComponentValue
        return _value;
    }

    return view;
}();