/*jshint esversion: 6 */
/* global $ */
fskeditorjs = function() {
	$('head').append('<meta http-equiv="X-UA-Compatible" content="IE=8,IE=edge" />');
	if (!String.prototype.startsWith) {
		  String.prototype.startsWith = function(searchString, position) {
		    position = position || 0;
		    return this.indexOf(searchString, position) === position;
		  };
	}
	
	if (!Array.from) {
		  Array.from = (function () {
		    var toStr = Object.prototype.toString;
		    var isCallable = function (fn) {
		      return typeof fn === 'function' || toStr.call(fn) === '[object Function]';
		    };
		    var toInteger = function (value) {
		      var number = Number(value);
		      if (isNaN(number)) { return 0; }
		      if (number === 0 || !isFinite(number)) { return number; }
		      return (number > 0 ? 1 : -1) * Math.floor(Math.abs(number));
		    };
		    var maxSafeInteger = Math.pow(2, 53) - 1;
		    var toLength = function (value) {
		      var len = toInteger(value);
		      return Math.min(Math.max(len, 0), maxSafeInteger);
		    };

		    // The length property of the from method is 1.
		    return function from(arrayLike/*, mapFn, thisArg */) {
		      // 1. Let C be the this value.
		      var C = this;

		      // 2. Let items be ToObject(arrayLike).
		      var items = Object(arrayLike);

		      // 3. ReturnIfAbrupt(items).
		      if (arrayLike == null) {
		        throw new TypeError('Array.from requires an array-like object - not null or undefined');
		      }

		      // 4. If mapfn is undefined, then let mapping be false.
		      var mapFn = arguments.length > 1 ? arguments[1] : void undefined;
		      var T;
		      if (typeof mapFn !== 'undefined') {
		        // 5. else
		        // 5. a If IsCallable(mapfn) is false, throw a TypeError exception.
		        if (!isCallable(mapFn)) {
		          throw new TypeError('Array.from: when provided, the second argument must be a function');
		        }

		        // 5. b. If thisArg was supplied, let T be thisArg; else let T be undefined.
		        if (arguments.length > 2) {
		          T = arguments[2];
		        }
		      }

		      // 10. Let lenValue be Get(items, "length").
		      // 11. Let len be ToLength(lenValue).
		      var len = toLength(items.length);

		      // 13. If IsConstructor(C) is true, then
		      // 13. a. Let A be the result of calling the [[Construct]] internal method 
		      // of C with an argument list containing the single item len.
		      // 14. a. Else, Let A be ArrayCreate(len).
		      var A = isCallable(C) ? Object(new C(len)) : new Array(len);

		      // 16. Let k be 0.
		      var k = 0;
		      // 17. Repeat, while k < len… (also steps a - h)
		      var kValue;
		      while (k < len) {
		        kValue = items[k];
		        if (mapFn) {
		          A[k] = typeof T === 'undefined' ? mapFn(kValue, k) : mapFn.call(T, kValue, k);
		        } else {
		          A[k] = kValue;
		        }
		        k += 1;
		      }
		      // 18. Let putStatus be Put(A, "length", len, true).
		      A.length = len;
		      // 20. Return A
		      ;
		      return A;
		    };
		  }());
		}
    var joinerNode = {
        version: '1.0.0'
    };
    joinerNode.name = 'FSK Editor JS';
    var paper;
    var _firstModel = {generalInformation:{},scope:{},dataBackground:{},modelMath:{}};
    
    
    var _firstModelScript;
   
    
    var _firstModelViz;
   
    var _viewValue;
    //new ParameterizedModel(inObj2.genericModel.generalInformation.name, inObj2.genericModel.modelMath.parameter)
    var firstModelParameterMap = new Object();
    var secomndModelParameterMap = new Object();
    joinerNode.init = function(representation, value) {
    	_firstModel.generalInformation = JSON.parse(value.generalInformation);
    	_firstModel.scope =  JSON.parse(value.scope);
    	
    	_firstModel.modelMath =  JSON.parse(value.modelMath);
    	_firstModel.dataBackground =  JSON.parse(value.dataBackground);
    	_firstModelScript = value.firstModelScript;
    	_firstModelViz = value.firstModelViz;
    	
    	_viewValue = value;
    	window.generalInformation = _firstModel.generalInformation;
    	window.scope =  _firstModel.scope;
    	window.modelMath =  _firstModel.modelMath;
    	window.dataBackground =  _firstModel.dataBackground;
    	
    	prepareData(_firstModel);
    	create_body();
    };
	function prepareData(_firstModel){
		//prepare generalInformation
		if(_firstModel.generalInformation.creationDate  === undefined){
			_firstModel.generalInformation.creationDate = '';
		}else{
			_firstModel.generalInformation.creationDate = new Date(_firstModel.generalInformation.creationDate).toISOString();
		}
		_firstModel.generalInformation.description = _firstModel.generalInformation.description != null ?_firstModel.generalInformation.description:"";
		_firstModel.generalInformation.author = _firstModel.generalInformation.author != null ?_firstModel.generalInformation.author:{};
		_firstModel.generalInformation.format = _firstModel.generalInformation.format != null ?_firstModel.generalInformation.format:"";
		_firstModel.generalInformation.language = _firstModel.generalInformation.language != null ?_firstModel.generalInformation.language:"";
		_firstModel.generalInformation.languageWrittenIn = _firstModel.generalInformation.languageWrittenIn != null ?_firstModel.generalInformation.languageWrittenIn:"";
		_firstModel.generalInformation.software = _firstModel.generalInformation.software != null ?_firstModel.generalInformation.software:"";
		_firstModel.generalInformation.source = _firstModel.generalInformation.source != null ?_firstModel.generalInformation.source:"";
		_firstModel.generalInformation.status = _firstModel.generalInformation.status != null ?_firstModel.generalInformation.status:"";
		_firstModel.generalInformation.objective = _firstModel.generalInformation.objective != null ?_firstModel.generalInformation.objective:"";

		_firstModel.scope.generalComment = _firstModel.scope.generalComment != null ?_firstModel.scope.generalComment:"";
		_firstModel.scope.temporalInformation = _firstModel.scope.temporalInformation != null ?_firstModel.scope.temporalInformation:"";
		window.scope.populationGroup = window.scope.populationGroup != null ? window.scope.populationGroup:{};
		window.scope.populationGroup.populationName = window.scope.populationGroup.populationName != null ?window.scope.populationGroup.populationName:"";
		window.scope.populationGroup.targetPopulation = window.scope.populationGroup.targetPopulation != null ?window.scope.populationGroup.targetPopulation:"";
		var StringObjectPopupsNamex = ['populationSpan','populationDescription','bmi','specialDietGroups','region','country','populationRiskFactor','season','patternConsumption','populationAge'];
		$.each(StringObjectPopupsNamex, function( index, value ) {
			window.scope.populationGroup[value] = window.scope.populationGroup[value] != null ?window.scope.populationGroup[value]:[];
		});
		
		_firstModel.dataBackground.study = _firstModel.dataBackground.study!=null?_firstModel.dataBackground.study:{};
		_firstModel.dataBackground.dietaryassessmentmethod = _firstModel.dataBackground.dietaryassessmentmethod!=null?_firstModel.dataBackground.dietaryassessmentmethod:{};
		_firstModel.dataBackground.laboratory = _firstModel.dataBackground.laboratory!=null?_firstModel.dataBackground.laboratory:{};

	}
    joinerNode.getComponentValue = function() {
    	
    	window.store2.getState().jsonforms.core.data.populationGroup = window.toBeReplacedMap["Population Group"].getState().jsonforms.core.data;
    	window.store1.getState().jsonforms.core.data.author = window.store23.getState().jsonforms.core.data;
    	window.store6.getState().jsonforms.core.data.study = window.store7.getState().jsonforms.core.data;
    	_viewValue.generalInformation = JSON.stringify(window.store1.getState().jsonforms.core.data);
    	_viewValue.scope = JSON.stringify(window.store2.getState().jsonforms.core.data);
    	_viewValue.modelMath = JSON.stringify(window.store17.getState().jsonforms.core.data);
    	_viewValue.dataBackground = JSON.stringify(window.store6.getState().jsonforms.core.data);
        
        return _viewValue;
    };
   
    
    return joinerNode;

    // --- utility functions ---
    
    function create_body() {
    	$.ajaxSetup({
    		  cache: true
    		});
    	document.createElement("body");
    	bodyContent = "<meta http-equiv='X-UA-Compatible' content='IE=edge'>" +
    			"<h3>FSK Object</h3>" +
    	
        
        
        "                <div class='tabbable'>\n" + 
        "                    <ul class='nav nav-tabs'>\n" + 
        
        "                        <li class='active'><a href='#sub21'>General Information</a>\n" + 
        "                        </li>\n" + 
        "                        <li><a href='#sub22'>Scope</a>\n" + 
        "                        </li>\n" + 
        "                        <li><a href='#sub23'>Data Background</a>\n" + 
        "                        </li>\n" +
        "                        <li><a href='#sub24'>Model Math</a>\n" + 
        "                        </li>\n" +
        "                        <li ><a href='#sub25'>Model Script</a>\n" + 
        "                        </li>\n" + 
        "                        <li ><a href='#sub26'>Visualization Script</a>\n" + 
        "                        </li>\n" + 
        "                    </ul>\n" + 
        "                    <div class='tab-content'>\n" + 
        "                        <div class='tab-pane fade active in' id='sub21'>\n" + 
        "                     		<div id=\"generalinformation\" class=\"App\">"+
		"								<div class=\"demoform\">"+
		"								</div>"+
		"					  		</div>" +
        "                        </div>\n" + 
        "                        <div class='tab-pane fade' id='sub22'>\n" + 
        "                    		 <div id=\"scope\" class=\"App\">"+
		"								<div class=\"demoform\">"+
		"								</div>"+
		"					 		 </div>" +
		"                        </div>\n" + 
		"                        <div class='tab-pane fade' id='sub23'>\n" + 
        "                    		 <div id=\"databackground\" class=\"App\">"+
		"								<div class=\"demoform\">"+
		"								</div>"+
		"					 		 </div>" +
		"                        </div>\n" + 
		"                        <div class='tab-pane fade' id='sub24'>\n" + 
        "                    		 <div id=\"modelMath\" class=\"App\">"+
		"								<div class=\"demoform\">"+
		"								</div>"+
		"					 		 </div>" +
		"                        </div>\n" + 
		"                        <div class='tab-pane fade' id='sub25'>\n" + 
        "                    		 <div  >" +
        "								<h4>Model Script</h4>"+
        "								<textarea id='firstModelScript' name='firstModelScript'>" +
        									_firstModelScript+
		"								</textarea>"+
	
		"					 		 </div>" +
		"                        </div>\n" + 
		"                        <div class='tab-pane fade' id='sub26'>\n" + 
        "                    		 <div >"+
        "								<h4>Model Visualization Script</h4>"+
        "								<textarea id='firstModelViz' name='firstModelViz'>" +
        									_firstModelViz+
		"								</textarea>"+
		
		"					 		 </div>" +
		"                        </div>\n" + 
        "                    </div>\n" + 
        "                </div>\n" 
        
        $('body').append(bodyContent);
       
        $('body').append(' <div id="root"></div>');
        $('#Metadata a').on('click', function (e) {
        	  e.preventDefault()
        	  $(this).tab('show')
        	})
       
        $(document).ready(function() {

            // DEPENDENCY: https://github.com/flatlogic/bootstrap-tabcollapse


            // if the tabs are in a narrow column in a larger viewport
            $('.sidebar-tabs').tabCollapse({
                tabsClass: 'visible-tabs',
                accordionClass: 'hidden-tabs'
            });

            // if the tabs are in wide columns on larger viewports
            $('.content-tabs').tabCollapse();

            // initialize tab function
            $('.nav-tabs a').click(function(e) {
                e.preventDefault();
                $(this).tab('show');
                
                
            });
            

            // slide to top of panel-group accordion
            $('.panel-group').on('shown.bs.collapse', function() {
                var panel = $(this).find('.in');
                $('html, body').animate({
                    scrollTop: panel.offset().top + (-60)
                }, 500);
            });
            $('.nav-tabs a').on('shown.bs.tab', function(e){
            	var codeMirrorContainer = $('#sub25').find(".CodeMirror")[0];
                if (codeMirrorContainer && codeMirrorContainer.CodeMirror) {
    				codeMirrorContainer.CodeMirror.refresh();
    				
		        } else {
		        	 window.CodeMirror.fromTextArea(document.getElementById("firstModelScript"), {
		          	  lineNumbers: true,
		          	  extraKeys: {"Ctrl-Space": "autocomplete"},
		          	  mode: {name: "R"}
		          	});
		        	
		            
		        }
            	
               
                
                var codeMirrorContainer = $('#sub26').find(".CodeMirror")[0];
                if (codeMirrorContainer && codeMirrorContainer.CodeMirror) {
    				codeMirrorContainer.CodeMirror.refresh();
    				
		        } else {
		        	 window.CodeMirror.fromTextArea(document.getElementById("firstModelViz"), {
		          	  lineNumbers: true,
		          	  extraKeys: {"Ctrl-Space": "autocomplete"},
		          	  mode: {name: "R"}
		          	});
		        	
		            
		        }
            	
           
                
              });
            
            
           
        });
        try{
        		createEMFForm();
        }catch(err) {
        		console.log(err);
        }
        
          
        //$('html').find('style').remove();
        //data-meta MuiInputLabel
       $.each($('html').find('style'), function( key, value ) {
        	console.log(key);
        	if($(value).attr('data-meta') == 'MuiInput'){
        		console.log(' MuiInput');
        		$(value).remove();
        	}else if($(value).attr('data-meta') == 'MuiInputLabel'){
        		$(value).remove();
        	}else if($(value).attr('data-meta') == 'MuiFormLabel'){
        		$(value).remove();
        	}
        	
    	});
        
        /*$.each(  $('html').find('input'), function( key, value ) {
        	
        	$(value).removeAttr('class');
        	$(value).addClass('form-control');
        	$(value).parent().removeAttr('class');
        	$(value).parent().addClass('col-sm-10');
        	$(value).parent().parent().removeAttr('class');
        	$(value).parent().parent().addClass('form-group');
        	$(value).parent().parent().find('label').removeAttr('class');
        	$(value).parent().parent().find('label').addClass('control-label col-sm-2');
        	
        	$(value).focusin(function(event) {
        		event.preventDefault(); // Let's stop this event.
                event.stopPropagation(); // Really this time.
                $(value).parent().removeAttr('class');
            	$(value).parent().addClass('col-sm-10');
            	$(value).parent().parent().removeAttr('class');
            	$(value).parent().parent().addClass('form-group');
            	$(value).parent().parent().find('label').removeAttr('class');
            	$(value).parent().parent().find('label').addClass('control-label col-sm-2');
                
             });
        	$(value).blur(function(event) {
        		event.preventDefault(); // Let's stop this event.
                event.stopPropagation(); // Really this time.
                $(value).parent().removeAttr('class');
            	$(value).parent().addClass('col-sm-10');
            	$(value).parent().parent().removeAttr('class');
            	$(value).parent().parent().addClass('form-group');
            	$(value).parent().parent().find('label').removeAttr('class');
            	$(value).parent().parent().find('label').addClass('control-label col-sm-2');
                
             });
    	});*/
        
        
        
        $('.MuiFormLabel-root-100').css('font-size','1.5rem');
        $('.MuiDialog-paper-128').css('display','inline');
        $('.MuiDialog-paper-128').css('max-height','');
        $('.MuiDialog-paper-128').css('overflow-y','visible');

        $(".MuiTable-root-222 thead").removeAttr('class');
        $(".MuiTable-root-222 thead tr").removeAttr('class');
        $(".MuiTable-root-222 thead tr th").removeAttr('class');
        $(".MuiTable-root-222 thead tr th th").removeAttr('class');
        $(".MuiTable-root-222 tbody").removeAttr('class');
        $(".MuiTable-root-222 tbody tr").removeAttr('class');
        $(".MuiTable-root-222 tbody tr td").removeAttr('class');
        $(".MuiTable-root-222 tbody tr td div").removeAttr('class');
        $(".MuiTable-root-222 tbody tr td div div").removeAttr('class');
        $(".MuiTable-root-222 tbody tr td div div div").removeAttr('class');
      
        $(".MuiTable-root-222 tbody tr td div div div input").removeAttr('class');
        

        
	   
	   $('.MuiTable-root-222').addClass('table'); 
	   $('.MuiTable-root-222').parent().addClass('table-responsive');
	   $('.MuiTable-root-222').parent().removeClass('MuiGrid-typeItem-2'); 
       $('.MuiTable-root-222').removeClass('MuiTable-root-222'); 
       
       $('.MuiFormControl-root-90').addClass('form-group');
       
     /*   $(document.body).delegate('input:text', 'focusin', function(e) {
        	
            
        });*/
        $('.replaced').parent().addClass('panel'); 
        $('.replaced').parent().addClass('panel-default'); 
        $('.replaced').addClass('panel-body'); 
        //$($("[aria-describedby*='tooltip-add']")[0]).off();
        function makeId(words) {
            var n = words.split("Add to ");
            m = n[n.length - 1].replace(/\s/g, '');
            return m[0].toLowerCase()+""+m.substring(1);

        }
        /*$($("[aria-describedby*='tooltip-add']")).attr('data-toggle','modal');
        $($("[aria-describedby*='tooltip-add']")).attr('data-target','#myModal');*/
        $($("[aria-describedby*='tooltip-add']")).click(function(event) {
        	
        	currentArea = makeId($(this).attr('aria-label'));
        	console.log(currentArea);
        	event.preventDefault(); // Let's stop this event.
            event.stopPropagation(); // Really this time.
            $('#title'+currentArea).text(currentArea);
            $('#'+currentArea).modal('show');
        });
    }
    
   
}();
