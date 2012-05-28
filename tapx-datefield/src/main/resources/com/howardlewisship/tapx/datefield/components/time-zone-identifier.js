Tapestry.Initializer.identifyClientTimeZone = function (url) {

    var date = new Date();

    var params = {
        offsetMinutes: date.getTimezoneOffset(),
        dateString: date.toString(),
        epochMillis: date.getTime()
    };

    Tapestry.ajaxRequest(url, {
        parameters: params,
        onSuccess: function (reply) {
            var response = reply.responseJSON;

            document.fire("tapx:time-zone-identified", response);
        }
    });
};