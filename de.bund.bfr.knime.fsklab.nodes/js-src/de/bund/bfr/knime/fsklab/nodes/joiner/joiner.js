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
    	
    	var serializer = new XMLSerializer();
    	var str = serializer.serializeToString(paper.svg);
		_viewValue.svgRepresentation  = str
    	
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
    	        drawGrid: 'mesh',
    	        gridSize: 10,
    	        model: graph,
    	        snapLinks: true,
    	        linkPinning: true,
    	        drawGrid : true,
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
    	            'font-size': 12, 'font-weight': 'bold', 'font-variant': 'small-caps', 'text-transform': 'capitalize',margin:'20px',padding: '40px'
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
    	            'font-size': 12, 'font-weight': 'bold', 'font-variant': 'small-caps', 'text-transform': 'capitalize',margin:'20px',padding: '40px'
    	        }
    	    });
    	    paper.on('cell:pointerdown', function(cellView, evt) {
    	        if (cellView.model.isLink()) {
    	        	/*link = cellView
    	        	ink.labels = " [{ position: 0.5, attrs: { text: { text: 'fancy label', fill: '#f6f6f6', 'font-family': 'sans-serif' }, rect: { stroke: '#7c68fc', 'stroke-width': 20, rx: 5, ry: 5 } }}]"*/
    	        }
    	    })
    	    var link7 = new joint.dia.Link({
			    source: { x: 400, y: 200 },
			    target: { x: 740, y: 200 },
			    attrs: {
			        '.marker-source': { fill: '#4b4a67', stroke: '#4b4a67', d: 'M 10 0 L 0 5 L 10 10 z' },
			        '.marker-target': { fill: '#4b4a67', stroke: '#4b4a67', d: 'M 10 0 L 0 5 L 10 10 z' }
			    },
			    labels: [
			        { position: 0.5, attrs: { text: { text: 'fancy label', fill: '#f6f6f6', 'font-family': 'sans-serif' }, rect: { stroke: '#7c68fc', 'stroke-width': 20, rx: 5, ry: 5 } }}
			    ]
			});
    
    	    graph.on('change:source change:target', function(link) {
    	    	
    	    	link.label(0, { 
    	    		position: 0.5, 
    	    		attrs: 
    	    		{ 
	    	    		text: { text: 'fancy label', fill: '#f6f6f6', 'font-family': 'sans-serif' },
	    	    		rect: { stroke: '#7c68fc', 'stroke-width': 20, rx: 5, ry: 5 } 
    	    		}
    	    	}
    	    	);
    	    	//link.renderLabels();
	        	//link.attributes.labels.push();
    	    	console.log(link);
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
    	        		
    	        		
    	        		
    	        		
    	        	}
    	        	
    	        }
    	        console.log(link);
    	    
    	    });

    	    
    	    
    	    if(_viewValue.jsonRepresentation != undefined){
    	    	
    	    	graph.fromJSON(JSON.parse(_viewValue.jsonRepresentation));
    	    }else{
    	    	graph.addCells([firstModelTojoin, secondModelToJoin]);
    	    }
    	    console.log(link7);
    	   
    	    
 
    }
}();
