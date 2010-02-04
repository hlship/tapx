Tapestry.Initializer.tapxConfirm = function(spec) {

	var element = $(spec.clientId);
	var type = element.type;

	// Problem is, we don't know the order in which Initializer functions get
	// executed; it very
	// arbitrary. So what we do is defer the hookup of the "click" listener so
	// that wer
	// can undo the normal "click" event handler for.

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

	(function() {

		if (element.type == "submit") {

			var interceptClickEvent = true;

			element.observe("click", function(event) {

				if (interceptClickEvent) {
					event.stop();

					runModalDialog(function() {
						interceptClickEvent = false;

						element.click();
					});
				}
			});

			return;
		}

		// Assume it is a link component of some kind.

		// Delete any prior click listener.

		element.stopObserving("click");

		element.observe("click", function(event) {
			event.stop();

			runModalDialog(function() {

				// If the zoneId property exists, then we just fire an event to
				// take
				// it from here.`
				if ($T(element).zoneId) {
					element.fire(Tapestry.TRIGGER_ZONE_UPDATE_EVENT);
					return;
				}

				window.location = element.href;
			});
		});

	}).defer();
};