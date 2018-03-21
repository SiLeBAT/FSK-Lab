/*jshint esversion: 6 */
/* global $ */
joiner = function() {

    var joinerNode = {
        version: '1.0.0'
    };
    joinerNode.name = 'FSK Joiner';
    var paper;
    var _firstModel;
    var _secondModel;
    var _viewValue;
    var firstModelParameterMap = new Object();
    var secomndModelParameterMap = new Object();
    joinerNode.init = function(representation, value) {
     
    	_firstModel = value.firstModel;
    	_secondModel= value.secondModel;
    	_viewValue = value;
        create_body();
    };

    joinerNode.getComponentValue = function() {
    	console.log(paper.svg);
    	
        return _viewValue;
    };
   

    return joinerNode;

    // --- utility functions ---
    
    function create_body() {
        document.createElement("body");
        $('body').append("<div id='paper'></div>");
        drawWorkflow();
      
    }
    
    
    function drawWorkflow() {
    	 	var graph = new joint.dia.Graph;
    	 	

    	 	 paper = new joint.dia.Paper({

    	        el: document.getElementById('paper'),
    	        width: 1000,
    	        height: 1000,
    	        gridSize: 1,
    	        model: graph,
    	        snapLinks: true,
    	        linkPinning: false,
    	        embeddingMode: true,
    	        highlighting: {
    	            'default': {
    	                name: 'stroke',
    	                options: {
    	                    padding: 6
    	                }
    	            },
    	            'embedding': {
    	                name: 'addClass',
    	                options: {
    	                    className: 'highlighted-parent'
    	                }
    	            }
    	        },

    	        validateEmbedding: function(childView, parentView) {

    	            return parentView.model instanceof joint.shapes.devs.Coupled;
    	        },

    	        validateConnection: function(sourceView, sourceMagnet, targetView, targetMagnet) {
    	        	
    	            return sourceMagnet != targetMagnet;
    	        }
    	    });

    	    var connect = function(source, sourcePort, target, targetPort) {
    	    		
    	        var link = new joint.shapes.devs.Link({
    	            source: {
    	                id: source.id,
    	                port: sourcePort
    	            },
    	            target: {
    	                id: target.id,
    	                port: targetPort
    	            }
    	        });

    	        link.addTo(graph).reparent();
    	    };

    	    var firstModelInputParameters = [];
    	    var firstModelOutputParameters= [];

    	    _.each(_firstModel.listOfParameter, function(param) {
    	    	firstModelParameterMap[param.name] = param
    	    	if(param.classification == 'input'){
    	    		firstModelInputParameters.push(param.name);
    	    	}else{
    	    		firstModelOutputParameters.push(param.name);
    	    	}
    	        
    	    });
    	    var secondModelInputParameters = [];
    	    var secondModelOutputParameters= [];
    	    _.each(_secondModel.listOfParameter, function(param) {
    	    	secomndModelParameterMap[param.name] = param
    	    	if(param.classification == 'input'){
    	    		secondModelInputParameters.push(param.name);
    	    	}else{
    	    		secondModelOutputParameters.push(param.name);
    	    	}
    	    });
    	    var firstModelTojoin = new joint.shapes.devs.Atomic({
    	    	
    	        position: {
    	            x: 200,
    	            y: 160
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
    	            text: _firstModel.modelID, 
    	            'font-size': 18, 'font-weight': 'bold', 'font-variant': 'small-caps', 'text-transform': 'capitalize',margin:'20px',padding: '40px'
    	        }
    	    });

    	    var secondModelToJoin = new joint.shapes.devs.Atomic({
    	    	
    	        position: {
    	            x: 600,
    	            y: 160
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
    	            text: _secondModel.modelID, 
    	            'font-size': 18, 'font-weight': 'bold', 'font-variant': 'small-caps', 'text-transform': 'capitalize',margin:'20px',padding: '40px'
    	        }
    	    });
    	    
    	    graph.on('change:source change:target', function(link) {
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
    	        		
    	        		var serializer = new XMLSerializer();
    	        		var str = serializer.serializeToString(paper.svg);
    	        		
    	        	}
    	        	
    	        }
    	    
    	    });

    	    
    	    
    	    if(_viewValue.jsonRepresentation != undefined){
    	    	
    	    	graph.fromJSON(JSON.parse(_viewValue.jsonRepresentation));
    	    }else{
    	    	graph.addCells([firstModelTojoin, secondModelToJoin]);
    	    }
    	    
    	   
    	    
 
    }
}();
