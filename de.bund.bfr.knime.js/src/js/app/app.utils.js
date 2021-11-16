 /** Temporary workaround for some metadata glitches. */
 var metadataFix = (metadata) =>   {
	// Ignore temporarily publication type
	// TODO: publicationType takes the abbreviation instead of the full string
	// used in the Reference dialog. Since KNIME runs getComponentValue twice,
	// the value cannot be converted here. The 1st call to getComponentValue
	// would get the abbreviation but the 2nd call would corrupt it. The HTML
	// select should instead use the full string as label and the abreviation
	// as value.
	metadata.generalInformation.reference.forEach(ref => delete ref.publicationType);

	/* TODO: Ignore temporarily reference.
	The reference property is of type Reference in the schema. Unfortunately,
	nested dialogs are not supported in Bootstrap, so the type is changed
	in the UI schema to text. Since the text type cannot be deserialized to
	Reference, the values are discarded temporarily here.*/
	metadata.modelMath.parameter.forEach(param => delete param.reference);

	return metadata;
}
/**
 * 
 * @param {*} name 
 * @param {*} isMandatory 
 * @param {*} description 
 */
var createLabel = (name, isMandatory, description)  =>   {
	let label = document.createElement("label");
	label.classList.add("col-sm-2", "control-label");
	label.title = description;
	label.setAttribute("data-toggle", "tooltip");
	label.textContent = name + (isMandatory ? "*" : "");

	$(label).tooltip();  // Enable Bootstrap tooltip

	return label;
}

/**
 * Create a Bootstrap dropdown menu.
 * @param {string} name Menu name 
 * @param {array} submenus Array of hashes of id and name of the submenus.
 */
var createSubMenu = (name, submenus)  =>   {
	return `<li class="dropdown">
	<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
		aria-haspopup="true" aria-expanded="false">${name}<span class="caret"></a>
	<ul class="dropdown-menu">
	${submenus.map(entry => `<li><a href="#${entry.id}" aria-controls="#${entry.id}"
		role="button" data-toggle="tab">${entry.label}</a></li>`).join("")}
	</ul>
	</li>`;
}



/**
 * Add controlled vocabulary to an input.
 * @param {Element} input Input element
 * @param {string} vocabularyName Vocabulary name.
 */
var addControlledVocabulary = (input, vocabularyName) => {
	if (window.vocabularies[vocabularyName]) {
		$(input).typeahead({
			source: window.vocabularies[vocabularyName],
			autoSelect: true,
            items: 9999,
			fitToElement: false,
			showHintOnFocus: true
		});
	}
}

/**
 * Create an horizontal form for a metadata property. Missing values with
 * *null* or *undefined* are replaced with an empty string.
 * 
 * @param {object} prop Metadata property. It can be of type: text, number,
 *  url, data, boolean, text-array and date-array.
 * @param {string} value Input value. It can be *null* or *undefined* for
 *  missing values.
 * 
 * @returns InputForm or ArrayForm for the supported type. If wrong type
 *  it returns undefined.
 */
var createForm = (prop, value, port) =>   {
	let isMandatory = prop.required ? prop.required : false;

	if (prop.type === "text" || prop.type === "number" || prop.type === "url" ||
		prop.type === "date" || prop.type === "email"  || prop.type === "year_date")
		return new InputForm(prop.label, isMandatory, prop.type, prop.description,
			value ? value : "", port, prop.vocabulary, prop.sid);
	
	else if (prop.type === "long-text"){
		return new TextareaForm(prop.label, isMandatory, prop.description,
			value ? value : "");
	}
	else if (prop.type === "enum")
		return new SelectForm(prop.label, isMandatory, prop.description, value,
			port, prop.vocabulary);

	else if (prop.type === "boolean")
		return new InputForm(prop.label, false, "checkbox",
			prop.description, value, port);

	else if (prop.type === "text-array")
		return new ArrayForm(prop.label, isMandatory, prop.type,
			value ? value : [], prop.description, port, prop.vocabulary);

	else if (prop.type === "date-array")
		return new ArrayForm(prop.label, isMandatory, prop.type,
			value ? value : [], prop.description, port, prop.vocabulary);
}

/**
 * LOG
 * logs message to console, if window._debug flag is set for this class
 * @param {string} log: log message
 */

var _log = ( log, style ) => {
	if( window._debug ) {
		let styles = {
			primary 	: 'background: #000; color:#fff; padding: 1px 2px;',
			secondary 	: 'background: #aaa; color:#fff; padding: 1px 2px;',
			level1		: 'padding-left: 10px',
			level2		: 'padding-left: 20px',
			strong 		: 'font-weight: bold;',
			error 		: 'background: red; color: #ffffff; padding: 1px 2px;',
			warning 	: 'background: yellow; padding: 1px 2px;',
			hook 		: 'background: orange; padding: 1px 2px;'
		};
		if( style in styles ) {
			console.log( '%c'+ log, styles[style] );
		}
		else {
			console.log( log );
		}
	}
}
/**
 * 
 */
var _createHelperMetadataText = ( helperText ) => {
	let O = this;

	// create table
	let $table = $( '<table class="table table-sm table-hover table-params-metadata"></table>' );
	// create rows
	if ( helperText ) {
		let $row = $( '<tr></tr>')
			.appendTo( $table );
		$row.append( '<td>'+ helperText +'</td>' ); // value
	}
	return $table;
}
/** 
  * is undefined
  */

var _isUndefined = ( val ) => typeof val === typeof undefined ? true : false;

/**
  * is null
  */

var _isNull = ( val ) => val == null ? true : false;

/**
  * sorter functions
  */

var _sorter = {
	_name 			: function( a, b ) { // add a method called name
		return a.localeCompare( b );
		// aa = a.replace( /^the /i, '' ); // remove 'The' from start of parameter
		// bb = b.replace( /^the /i, '' ); // remove 'The' from start of parameter

		// if ( aa < bb ) { // if value a is less than value b
		// 	return -1;
		// }
		// else { // Otherwise
		// 	return aa > bb ? 1 : 0; // if a is greater than b return 1 OR
		// }
	},
	_execution: function _execution(a, b) {
		var array_a = a.split(" ");
		var array_b = b.split(" ");
		if(a == "") {
		    return -1;
		}
		if(b == "") {
		    return 1;
		}
		if( array_a.length < array_b.length){
            return -1;
        }
        if( array_a.length > array_b.length){
            return 1;
        }
		return _sorter._executionArray(array_a, array_b);
	},
	_executionArray: function _executionArray(a, b) {
    	var aa = parseFloat(a[0]); // get first numbers (h,min, seconds)
        var bb = parseFloat(b[0]);

		if( (aa - bb) == 0 && a.length > 1) {

		    a.shift();
		    b.shift();// remove first element
            return _sorter._executionArray(a,b);
        }


		return aa - bb;

	},
	_date: function _date(a, b) {
	    if(a == "") {
    	    return -1;
    	}
    	if(b == "") {
    	    return 1;
    	}

		var aa = new Date(a);
		var bb = new Date(b);
		return aa - bb;
	}
}

/**
  * formatter functions
  */

var _formatter = {
	_list 			: ( val ) => {
		let $ul = $( '<ul></ul>' );
		if ( val && typeof val === 'object' && val !== null ) {
			for( let item of val.values() ) {
				let $li = $( '<li>'+ item +'</li>' )
					.appendTo( $ul );
			};
			return $ul;
		}
		else if ( val && $.isArray( val ) && val.length > 0 ) {
			$.each( val, ( i, li ) => {
				let $li = $( '<li>'+ item +'</li>' )
					.appendTo( $ul );

			} );
			return $ul;
		}
		return val;
	},
	_uploadDate 	: ( val ) => {
		if ( val && val.indexOf( ' | ' ) >= 0 ) {
			let time = val.substring( val.indexOf( ' | ' ) );
			return val.replace( time, '<br><small>'+ time +'</small>' ).replace( '|', '' );
		}
		return val;
	},
	_searchHighlight : ( val, search ) => { // prevents highlighting of html parts that fit search 
		var rx = new RegExp('(?![^<]+>)' + search, 'gi');
		// return val.toString().replace( new RegExp('(<.*?>)(' + search + '?.)(</.*?>)', 'g'), '<mark>$2</mark>' );
		return val.replace( rx, '<mark>$&</mark>' );
	},
	_metadataDate: function _metadataDate(data) {
        if(data && data.constructor == Array) {
            var dTemp = new Date(data);
            if (Object.prototype.toString.call(dTemp) === "[object Date]") {
            // it is a date
                if (!isNaN(dTemp.getTime())) {  // d.valueOf() could also work
                    // date is valid
                    data = dTemp.toLocaleDateString();
                }
            }

            }
    	return data;
    },
	_metadataDateArray: function _metadataDateArray(data) {
		if(data){
	        return data.map(_formatter._metadataDate)
	    }
	    return data;
    }
}


/**
  * fetch data from src by id and type and token (URL parameters)
  */

var _fetchData = {
	_json 		: async ( src, id, signal ) => {
		_log( 'UTILS / _fetchData._json: '+ src + ', '+ id + ', ' + window._token);
		let data = null;
		// append id if not type "set"
		src = ! _isNull( id ) ? src + id + window._token: src + window._token;

		let fetchOpts = {};
		! _isNull( signal ) ? fetchOpts.signal = signal : null;

		const response = await fetch( src, fetchOpts )
			.catch( error => {
				if( error.name === 'AbortError' ) {
					_log( 'cancelled', 'level1' );
				}
			} );

		if( response )  {
			data = await response.json();
		}


		return data;
	},
	_blob 		: async ( src, id, signal ) => {
		_log( 'UTILS / _fetchData._blob: '+ src + ', '+ id + ', ' + window._token);
		let data = null;
		// append id if not type "set"
		src = ! _isNull( id ) ? src + id + window._token: src  + window._token;

		let fetchOpts = {};
		! _isNull( signal ) ? fetchOpts.signal = signal : null;

		const response = await fetch( src, fetchOpts )
			.catch( error => {
				if( error.name === 'AbortError' ) {
					_log( 'cancelled', 'level1' );
				}
			} );
		if( response )  {
			const blob = await response.blob();

			let urlCreator = window.URL || window.webkitURL || window;
			data = urlCreator.createObjectURL( blob );
		}
		
		return data;
	},
	_content 	: async ( src, id, signal ) => {
		_log( 'UTILS / _fetchData._content: '+ src + ', '+ id + ', ' + window._token);
		let data = null;
		// append id if not type "set"
		src = ! _isNull( id ) ? src + id + window._token : src + window._token;

		let fetchOpts = {};
		! _isNull( signal ) ? fetchOpts.signal = signal : null;

		const response = await fetch( src, fetchOpts )
			.catch( error => {
				if( error.name === 'AbortError' ) {
					_log( 'cancelled', 'level1' );
				}
			} );

		if( response )  {
			data = await response.text();
		}


		return data;
	},
	_array 		: async ( src, arrayLength, signal ) => {
		_log( 'UTILS / _fetchData._array: '+ src + ', '+ arrayLength + ', ' + window._token);
		let data = [];
		let fetchOpts = {};
		! _isNull( signal ) ? fetchOpts.signal = signal : null;

		for ( let i = 0; i < arrayLength; i++ ) {
			await fetch( src + i, fetchOpts )
			.then( ( resp ) => {
				return resp.text().then( ( text ) => {
					data[i] = text;
				} );
			} )
			.catch( error => {
				if( error.name === 'AbortError' ) {
					_log( 'cancelled', 'level1' );
				}
			} );
		}

		return data;
	}
}


/**
 * CHECK UNDEFINED CONTENT
 * checks if value is empty and replace the empty value with default or custom placeholder for table views 
 * @param {string/object} value: any value
 * @param {string} placeholder: any string type as placeholder
 */

var _checkUndefinedContent = ( value, placeholder ) => {
	// placeholder custom || default
	placeholder = placeholder || '-';

	// check empty criterions
	if ( _isUndefined( value ) 
		|| _isNull( value ) 
		|| typeof value == 'object' && Object.keys( value ).length === 0 && value.constructor === Object
		|| typeof value == 'object' && value.length == 1 && _isNull( value[0] )
		|| value.length <= 0 ) {

		value = placeholder;
	}
	return value;
}

/**
  * strip html tags
  */

var _stripHtmlTags = ( str ) => {
	if ( ( str === null ) || ( str === '' ) ) {
		return false;
	}
	else {
		str = str.toString();
		return str.replace( /<[^>]*>/g, '' );
	}
};


/**
 * GET DOM TEXT
 * get pure text of dom elements
 * @param {element} element: dom element
 */

var _getDOMText = ( node ) => {
	let O = this;

	let text;

	if( node.outerText ) {
		text = node.outerText.trim();
	}
	else if( node.innerText ) {
		text = node.innerText.trim();
	}
	else {
		text = '';
	}

	if( node.childNodes ) {
		node.childNodes.forEach( child => text += _getDOMText( child ) );
	}

	return text;
};


/**
  * has attribute
  */

$.fn._hasAttr = ( attrName ) => {
	if( $( this ) ) {
		let attr = $( this ).attr( attrName );
		if ( typeof attr !== typeof undefined && attr !== false ) { // element has this attribute
			return true;
		}
	}
	return false;
};