Tapestry.Initializer.identifyClientTimeZone = function(url) {

	Tapestry.debug("identifyClientTimeZone()")

	function sendData(extra) {
		Tapestry.debug("sendData()");

		var date = new Date();

		var params = Object.extend( {
			offsetMinutes : date.getTimezoneOffset(),
			dateString : date.toString(),
			epochMillis : date.getTime()
		}, extra || {});

		Tapestry.ajaxRequest(url, {
			parameters : params
		});
	}

	if (!navigator.geolocation) {
		sendData();
		return;
	}

	navigator.geolocation.getCurrentPosition(function(position) {

		Tapestry.debug("got GEOLocation " + Object.toJSON(position));

		sendData( {
			latitude : position.coords.latitude,
			longitude : position.coords.longitude
		});

	}, function(positionError) {
		Tapestry.debug("GEO error " + positionError.message);

		sendData();
	});
};