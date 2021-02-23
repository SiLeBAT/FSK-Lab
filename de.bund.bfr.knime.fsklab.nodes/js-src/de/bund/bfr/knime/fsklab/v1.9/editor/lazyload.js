window.fetchVocabulary = function(vocabularyName) {
	console.log("Fetching vocabulary " + vocabularyName);
	
	const request = { "vocabularyName": vocabularyName};
	let promise = knimeService.requestViewUpdate(request);
	promise.progress(monitor => { /* No update */ })
	.then(response => {
		console.log(response);
		console.log(response.vocabularyItems);
	}).catch(error => {
		knimeService.logError(error);
	});
}
