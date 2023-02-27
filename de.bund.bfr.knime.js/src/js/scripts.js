var _endpoint = window._endpoint || 'https://knime.bfr.berlin/landingpage/';//http://localhost:8080/' //'https://knime.bfr.berlin/backend/';
window._endpoints 	= {
	metadata		: _endpoint + 'metadata/',
	image			: _endpoint + 'image/',
	download		: _endpoint + 'download/',
	uploadDate		: _endpoint + 'uploadDate/',
	executionTime	: _endpoint + 'executionTime/',
	simulations		: _endpoint + 'simulations/',
	execution 		: _endpoint + 'execute/',
	search 			: _endpoint + 'search/',
	filter 			: _endpoint + 'filter',
	modelScript     : _endpoint + "modelscript/",
    visScript       : _endpoint + "visualizationscript/",
	controlledVocabularyEndpoint:'https://knime.bfr.berlin/'
};
window._debug = false;
window.noExecution = true;
window.editEventBus = new EventObserver();