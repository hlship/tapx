Tapestry.Initializer.tapxConfirm = function(spec) {

	var element = $(spec.clientId);
	var type = element.type;

	// This function is executed with "early" priority, so it gets
	// to add its own "click" handlers before others.

	// Function that runs the dialog, and invokes the proceed button if the user
	// clicks "Yes"
	// Otherwise, the (if "No" or the modal dialog is closed), no further
	// processing occurs.

	var runModalDialog = function(proceed) {
		var div = new Element('div', {
			className : 'mb-confirm'
		}).update(new Element('p').update(spec.message));

		// Have to assign ids and reconnect at after load, because ModalBox
		// copies DOM elements, rather than moves them.

		var baseId = "mb-" + new Date().getTime();
		var yesId = baseId + "-yes";
		var noId = baseId + "-no";

		var yesButton = new Element('button', {
			className : 'mb-yes-button',
			id : yesId
		}).update('Yes');

		var noButton = new Element('button', {
			className : 'mb-no-button',
			id : noId
		}).update('No');

		div.insert(yesButton).insert(noButton);

		Modalbox.show(div, {
			title : spec.title,
			afterLoad : function() {
				$(noId).observe("click",
						Modalbox.hide.bindAsEventListener(Modalbox));
				$(yesId).observe("click", function() {
					Modalbox.hide();
					proceed.defer();
				});
			}
		});
	};

	var interceptClickEvent = true;

	element.observe("click", function(event) {

		if (interceptClickEvent) {
			event.stop();

			runModalDialog(function() {

			if (element.click) {
				interceptClickEvent = false;

				element.click();
				return;
			}

			// It's a link, does it have an event handler?

			if (element.onclick) {
				// This is a necessary assumption, alas. There is no good way to
				// get an Element to fire a built-in event such as "click".
				// fire()
				// is really for user-defined events only. fire("click") here
				// does
				// not work. Anyway, this assumes the link is a zone trigger and
				// fires
				// the event that initiates the zone update.
				element.fire(Tapestry.TRIGGER_ZONE_UPDATE_EVENT);
				return;
			}

			// Otherwise, no event handler, just change the window location as
			// if the user
			// clicked the link.
			window.location = element.href;
		});
	}
}	);

};