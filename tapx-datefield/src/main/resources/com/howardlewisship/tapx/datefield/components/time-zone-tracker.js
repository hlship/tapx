Tapestry.Initializer.identifyClientTimeZone = function(url) {

	Tapestry.debug("identifyClientTimeZone()")

	var date = new Date();

	Tapestry.ajaxRequest(url, {
		parameters : {
			offsetMinutes : date.getTimezoneOffset(),
			dateString : date.toString(),
			epochMillis : date.getTime()
		}
	});
};