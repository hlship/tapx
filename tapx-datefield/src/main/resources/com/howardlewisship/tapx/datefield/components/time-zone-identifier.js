Tapestry.Initializer.identifyClientTimeZone = function(url) {

	function sendData(extra) {
		var date = new Date();

		var params = Object.extend( {
			offsetMinutes : date.getTimezoneOffset(),
			dateString : date.toString(),
			epochMillis : date.getTime()
		}, extra || {});

		Tapestry.ajaxRequest(url, {
			parameters : params,
			onSuccess : function(reply) {
				var response = reply.responseJSON;

				document.fire("tapx:time-zone-identified", response);
			}
		});
	}

	if (!navigator.geolocation) {
		sendData();
		return;
	}

	navigator.geolocation.getCurrentPosition(function(position) {

		sendData( {
			latitude : position.coords.latitude,
			longitude : position.coords.longitude
		});

	}, function(positionError) {
		sendData();
	});
};