/*jshint esversion: 6 */
/* global $ */
joiner = function() {
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
		    	console.log(arrayLike);
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
    joinerNode.name = 'FSK Joiner';
    var paper;
    var _firstModel;
    var _secondModel;
    
    var _firstModelScript;
    var _secondModelScript;
    
    var _firstModelViz;
    var _secondModelViz;
    var _viewValue;
    //new ParameterizedModel(inObj2.genericModel.generalInformation.name, inObj2.genericModel.modelMath.parameter)
    var firstModelParameterMap = new Object();
    var secomndModelParameterMap = new Object();
    joinerNode.init = function(representation, value) {
     
    	_firstModel = value.firstModel;
    	_firstModelScript = value.firstModelScript;
    	_firstModelViz = value.firstModelViz;
    	_secondModel= value.secondModel;
    	_secondModelScript = value.secondModelScript;
    	_secondModelViz = value.secondModelViz;
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
		//prepare scope
		if(_firstModel.scope.product.productionDate  === undefined){
			_firstModel.scope.product.productionDate = '';
		}else{
			_firstModel.scope.product.productionDate = new Date(_firstModel.scope.product.productionDate).toISOString()
		}
		
		if(_firstModel.scope.product.expirationDate  === undefined){
			_firstModel.scope.product.expirationDate = '';
		}else{
			_firstModel.scope.product.expirationDate = new Date(_firstModel.scope.product.expirationDate).toISOString();
		}
		if(_firstModel.scope.product.productionMethod.length == 0){
			_firstModel.scope.product.productionMethod = '';
		}else{
			var methods = '';
			$.each(_firstModel.scope.product.productionMethod, function( index, value ) {
				  methods = methods + value +', ';
			});
			_firstModel.scope.product.productionMethod = methods;
		}
		
		if(_firstModel.scope.product.packaging.length == 0){
			_firstModel.scope.product.packaging = '';
		}else{
			var packaging = '';
			$.each(_firstModel.scope.product.packaging, function( index, value ) {
				packaging = packaging + value +', ';
			});
			_firstModel.scope.product.packaging = packaging;
		}
		
		if(_firstModel.scope.product.productTreatment.length == 0){
			_firstModel.scope.product.productTreatment = '';
		}else{
			var productTreatment = '';
			$.each(_firstModel.scope.product.productTreatment, function( index, value ) {
				productTreatment = productTreatment + value +', ';
			});
			_firstModel.scope.product.productTreatment = productTreatment;
		}
		console.log(_firstModel.dataBackground);
		//prepare databackground
		if(_firstModel.dataBackground.study.protocolUri == null){
			_firstModel.dataBackground.study.protocolUri = '';
		}
		
		if(_firstModel.dataBackground.dietaryAssessmentMethod.numberOfFoodItems.length == 0){
			_firstModel.dataBackground.dietaryAssessmentMethod.numberOfFoodItems = '';
		}else{
			var numberOfFoodItems = '';
			$.each(_firstModel.dataBackground.dietaryAssessmentMethod.numberOfFoodItems, function( index, value ) {
				numberOfFoodItems = numberOfFoodItems + value +', ';
			});
			_firstModel.dataBackground.dietaryAssessmentMethod.numberOfFoodItems = numberOfFoodItems;
		}
		
		if(_firstModel.dataBackground.dietaryAssessmentMethod.foodDescriptors.length == 0){
			_firstModel.dataBackground.dietaryAssessmentMethod.foodDescriptors = '';
		}else{
			var foodDescriptors = '';
			$.each(_firstModel.dataBackground.dietaryAssessmentMethod.foodDescriptors, function( index, value ) {
				foodDescriptors = foodDescriptors + value +', ';
			});
			_firstModel.dataBackground.dietaryAssessmentMethod.foodDescriptors = foodDescriptors;
		}
		
		if(_firstModel.dataBackground.dietaryAssessmentMethod.recordTypes.length == 0){
			_firstModel.dataBackground.dietaryAssessmentMethod.recordTypes = '';
		}else{
			var recordTypes = '';
			$.each(_firstModel.dataBackground.dietaryAssessmentMethod.recordTypes, function( index, value ) {
				recordTypes = recordTypes + value +', ';
			});
			_firstModel.dataBackground.dietaryAssessmentMethod.recordTypes = recordTypes;
		}
	}
    joinerNode.getComponentValue = function() {
    	
    	var serializer = new XMLSerializer();
    	var str = serializer.serializeToString(paper.svg);
		_viewValue.svgRepresentation  = str
    	
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
    			"<h3>Combined FSK Object </h3>" +
    	"        <div class='tabbable boxed parentTabs'>\n" + 
        "        <ul class='nav nav-tabs'>\n" + 
        "            <li class='active'><a href='#set1'>Join Panel</a>\n" + 
        "            </li>\n" + 
        "            <li><a href='#set2'>Combined Model</a>\n" + 
        "            </li>\n" + 
        "        </ul>\n" + 
        "        <div class='tab-content'>\n" + 
        "            <div class='tab-pane fade active in' id='set1'>\n" + 
        "					<div class='container-fluid'><div class='row content'><div id='paper' class='col-sm-9 sidenav'></div><div id='details' class='col-sm-3'></div></div> </div>"+
        "            </div>\n" + 
        "            <div class='tab-pane fade' id='set2'>\n" + 
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
        "								<h4>First Model Script</h4>"+
        "								<textarea id='firstModelScript' name='firstModelScript'>" +
        									_firstModelScript+
		"								</textarea>"+
		"								<h4>Second Model Script</h4>"+
		"								<textarea id='secondModelScript' name='secondModelScript'>" +
											_secondModelScript+
		"								</textarea>"+
		"					 		 </div>" +
		"                        </div>\n" + 
		"                        <div class='tab-pane fade' id='sub26'>\n" + 
        "                    		 <div >"+
        "								<h4>First Model Visualization Script</h4>"+
        "								<textarea id='firstModelViz' name='firstModelViz'>" +
        									_firstModelViz+
		"								</textarea>"+
		"								<h4>Second Model Visualization Script</h4>"+
		"								<textarea id='secondModelViz' name='secondModelViz'>" +
											_secondModelViz+
		"								</textarea>"+
		"					 		 </div>" +
		"                        </div>\n" + 
        "                    </div>\n" + 
        "                </div>\n" + 
        "            </div>\n" + 
        "        </div>\n" + 
        "    </div>" 
        
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
		        	 window.CodeMirror.fromTextArea(document.getElementById("secondModelScript"), {
		          	  lineNumbers: true,
		          	  extraKeys: {"Ctrl-Space": "autocomplete"},
		          	  mode: {name: "R"}
		          	});
		        	
		            
		        }
            	
                var codeMirrorContainerx = $('#sub25').find(".CodeMirror")[1];
                if (codeMirrorContainerx && codeMirrorContainerx.CodeMirror) {
    				codeMirrorContainerx.CodeMirror.refresh();
    				
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
		        	 window.CodeMirror.fromTextArea(document.getElementById("secondModelViz"), {
		          	  lineNumbers: true,
		          	  extraKeys: {"Ctrl-Space": "autocomplete"},
		          	  mode: {name: "R"}
		          	});
		        	
		            
		        }
            	
                var codeMirrorContainerx = $('#sub26').find(".CodeMirror")[1];
                if (codeMirrorContainerx && codeMirrorContainerx.CodeMirror) {
    				codeMirrorContainerx.CodeMirror.refresh();
    				
		        } else {
		        	 window.CodeMirror.fromTextArea(document.getElementById("firstModelViz"), {
		          	  lineNumbers: true,
		          	  extraKeys: {"Ctrl-Space": "autocomplete"},
		          	  mode: {name: "R"}
		          	});
		        	
		            
		        }
                
              });
            
            
           
        });
        drawWorkflow();
        createEMFForm();
        $(window).resize(function() {
            var canvas = $('#paper');
            paper.setDimensions(canvas.width(), canvas.height());
        });
          
          
        
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
        //$(".MuiInput-input-113").prop("readonly", true);
         
        $('.MuiTable-root-222').addClass('table table-dark'); 
        $('.MuiFormControl-root-90').addClass('form-group');
       
        
        $('.replaced').parent().addClass('panel'); 
        $('.replaced').parent().addClass('panel-default'); 
        $('.replaced').addClass('panel-body'); 
        function getLastWord(words) {
            var n = words.split(" ");
            return n[n.length - 1];

        }
        /*$($("[aria-describedby*='tooltip-add']")).attr('data-toggle','modal');
        $($("[aria-describedby*='tooltip-add']")).attr('data-target','#myModal');*/
        $($("[aria-describedby*='tooltip-add']")).click(function(event) {
        	currentArea = getLastWord($(this).attr('aria-label'));
        	event.preventDefault(); // Let's stop this event.
            event.stopPropagation(); // Really this time.
            $('#title'+currentArea).text(currentArea);
            $('#'+currentArea).modal('show');
        });
    }
    
    
    function drawWorkflow() {
    	 	var graph = new joint.dia.Graph;
    	 	

    	 	 paper = new joint.dia.Paper({

    	        el: document.getElementById('paper'),
    	        drawGrid: 'mesh',
    	        gridSize: 10,
    	        model: graph,
    	        snapLinks: true,
    	        linkPinning: true,
    	        drawGrid : true,
    	        
    	        highlighting: {
    	            'default': {
    	                name: 'stroke',
    	                options: {
    	                    padding: 6
    	                }
    	            }
    	        },
    	        interactive: function(cellView) {
    	            if (cellView.model instanceof joint.dia.Link) {
    	                // Disable the default vertex add functionality on pointerdown.
    	                return { vertexAdd: false };
    	            }
    	            return true;
    	        },
    	        validateEmbedding: function(childView, parentView) {

    	            return parentView.model instanceof joint.shapes.devs.Coupled;
    	        },

    	        validateConnection: function(sourceView, sourceMagnet, targetView, targetMagnet) {
    	        	
    	            return sourceMagnet != targetMagnet;
    	        }
    	    });
    	 	paper.on('cell:pointerdblclick', 
    	 		    function(cellView, evt, x, y) { 
    	 				if (cellView.model instanceof joint.dia.Link) {  
    	 					link = cellView.model
    	 					
    	 					var sourcePort = link.get('source').port;
    	 	    	        var sourceId = link.get('source').id;
    	 	    	        var targetPort = link.get('target').port;
    	 	    	        var targetId = link.get('target').id;
    	 	    	      
    	 	    	      $('#details').html(
    	 	    	    		  '<form action="">'+
    	 	    	    		  	  '<div class="form-group row">'+
			    	 	    	        '<label class="col-6 col-form-label" for="source">Source Port:</label>'+
			    	 	    	        '<div class="col-6"><input type="text" class="form-control" id="source" value="'+sourcePort+'"  ></div>'+
			    	 	    	      '</div>'+
			    	 	    	      '<div class="form-group row">'+
			    	 	    	       ' <label class="col-6 col-form-label" for="target">Target Port:</label>'+
			    	 	    	        '<div class="col-6"><input  type="text" class="form-control" id="target" value = "'+targetPort+'" ></div>'+
			    	 	    	      '</div>'+
			    	 	    	     '<div class="form-group row">'+
			    	 	    	       ' <label class="col-6 col-form-label" for="Command">Conversion Command:</label>'+
			    	 	    	        '<div class="col-6"><textarea class="form-control" rows="3" id="Command" > '+targetPort+' = '+sourcePort +'</textarea> </div>'+
			    	 	    	      '</div>'+
			    	 	    	      '<div class="form-group">'+
	    	 	    	    		    '<div class="col-sm-offset-2 col-sm-10">'+
	    	 	    	    		      '<button type="button" id="save" class="btn btn-default">Save</button>'+
	    	 	    	    		    '</div>'+
	    	 	    	    		  '</div>'+
			    	 	    	   '</form>');
	    	 	    	      $('#save').click(function() {
	    	 	    	    	  
	    	 	    	    	  $.each( _viewValue.joinRelations, function( i, relation ) {
	    	 	    	    		 fParam = firstModelParameterMap[sourcePort];
	    	 	    	    		 sParam = secomndModelParameterMap[targetPort];
	    	 	    	    		  if(relation.sourceParam.name == fParam.name && relation.targetParam.name == sParam.name){
	    	 	    	    			 relation.command = $("textarea#Command").val();
	    	 	    	    			 
	    	 	    	    		  }
	    	 	    	    	  });
	    	 	    	     });
    	 					
    	 					
    	 				}
    	 		    }
    	 	);
    	 	var firstModelInputParameters = [];
    	    var firstModelOutputParameters= [];

    	    _.each(_firstModel.modelMath.parameter, function(param) {
    	    	firstModelParameterMap[param.name] = param
    	    	if(param.classification == 'input'){
    	    		firstModelInputParameters.push(param.name);
    	    	}else{
    	    		firstModelOutputParameters.push(param.name);
    	    	}
    	        
    	    });
    	    var secondModelInputParameters = [];
    	    var secondModelOutputParameters= [];
    	    _.each(_secondModel.modelMath.parameter, function(param) {
    	    	secomndModelParameterMap[param.name] = param
    	    	if(param.classification == 'input'){
    	    		secondModelInputParameters.push(param.name);
    	    	}else{
    	    		secondModelOutputParameters.push(param.name);
    	    	}
    	    });
    	    var paperWidth = $('#paper').width();
    	    var firstModelTojoin = new joint.shapes.devs.Atomic({
    	    	
    	        position: {
    	            x: paperWidth-520,
    	            y: 60
    	        },
    	        size: { width: 200, height: firstModelInputParameters.length*25 },
    	        inPorts: firstModelInputParameters,
    	        outPorts: firstModelOutputParameters,ports: {
    	            groups: {
    	                'in': {
    	                    attrs: {
    	                        '.port-body': {
    	                            fill: '#16A085'
    	                        }
    	                    }
    	                },
    	                'out': {
    	                    attrs: {
    	                        '.port-body': {
    	                            fill: '#E74C3C'
    	                        }
    	                    }
    	                }
    	            }
    	        }
    	    });
    	    firstModelTojoin.attr({
    	        rect: {  rx: 5, ry: 5, 'stroke-width': 2, stroke: 'black' },
    	        text: {
    	            text: _firstModel.generalInformation.name, 
    	            'font-size': 12, 'font-weight': 'bold', 'font-variant': 'small-caps', 'text-transform': 'capitalize',margin:'20px',padding: '40px'
    	        }
    	    });

    	    var secondModelToJoin = new joint.shapes.devs.Atomic({
    	    	
    	        position: {
    	            x: paperWidth-270,
    	            y: 280
    	        },
    	        size: { width: 200, height: secondModelInputParameters.length*25 },
    	        inPorts: secondModelInputParameters,
    	        outPorts: secondModelOutputParameters,
    	        ports: {
    	            groups: {
    	                'in': {
    	                    attrs: {
    	                        '.port-body': {
    	                            fill: '#16A085'
    	                        }
    	                    }
    	                },
    	                'out': {
    	                    attrs: {
    	                        '.port-body': {
    	                            fill: '#E74C3C'
    	                        }
    	                    }
    	                }
    	            }
    	        }
    	    });
    	    secondModelToJoin.attr({
    	        rect: {  rx: 5, ry: 5, 'stroke-width': 2, stroke: 'black' },
    	        text: {
    	            text: _secondModel.generalInformation.name, 
    	            'font-size': 12, 'font-weight': 'bold', 'font-variant': 'small-caps', 'text-transform': 'capitalize',margin:'20px',padding: '40px'
    	        }
    	    });
    	  
    	   
    
    	    graph.on('change:source change:target', function(link) {
    	    	var sourcePort = undefined;
     	        var sourceId =  undefined;
     	        var targetPort = undefined;
     	        var targetId = undefined;
    	        var sourcePort = link.get('source').port;
    	        var sourceId = link.get('source').id;
    	        var targetPort = link.get('target').port;
    	        var targetId = link.get('target').id;
    	        var sourceParameter;
    	        var targetParameter;
    	        if(targetPort != undefined && targetId != undefined ){
    	        	sourceParameter = firstModelParameterMap[sourcePort];
    	        	if(sourceParameter == undefined){
    	        		sourceParameter = secomndModelParameterMap[sourcePort];
    	        	}
    	        	targetParameter = secomndModelParameterMap[targetPort];
    	        	if(targetParameter == undefined){
    	        		targetParameter = firstModelParameterMap[targetPort];
    	        	}
    	        	if(targetParameter != undefined){
    	        		
    	        		_viewValue.joinRelations.push({sourceParam:sourceParameter,targetParam:targetParameter});
    	        		_viewValue.jsonRepresentation =JSON.stringify(graph.toJSON());
    	        		//_viewValue.svgRepresentation = paper.svg;
    	        		//link.label(0, { position: 0.5, attrs: { text: { text: sourcePort+" = "+targetPort } } });
    	        	    
    	        		
    	        		
    	        	}
    	        	
    	        	
    	        }
    	    
    	    });
    	    
    	    
    	    
    	    if(_viewValue.jsonRepresentation != undefined){
    	    	if(_viewValue && _viewValue.jsonRepresentation){
    	    		graph.fromJSON(JSON.parse(_viewValue.jsonRepresentation));
    	    	}
    	    }else{
    	    	graph.addCells([firstModelTojoin, secondModelToJoin]);
    	    }
    	  
    	   
    	    
 
    }
}();
