Tapestry.Initializer.tapxConfirm = function(spec) {

	var element = $(spec.clientId);
	var type = element.type;

	/*
	 * This function is executed with "early" priority, so it gets to add its
	 * own "click" handlers before others. Function that runs the dialog, and
	 * invokes the proceed button if the user clicks "Yes" Otherwise, the (if
	 * "No" or the modal dialog is closed), no further processing occurs.
	 */
	var runModalDialog = function(proceed) {
		var div = new Element('div', {
			className : 'mb-confirm'
		}).update(new Element('p').update(spec.message));

		/*
		 * Have to assign ids and reconnect at after load, because ModalBox
		 * copies DOM elements, rather than moves them.
		 */
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

	/*
	 * Replace the normal click event, knowing that in most cases, the original
	 * link or button has an Tapestry.ACTION_EVENT event handler to do its real
	 * work.
	 */
	element.stopObserving("click");

	element.observe("click", function(event) {

		if (interceptClickEvent) {

			event.stop();

			runModalDialog(function() {

				if ($T(element).hasAction) {
					element.fire(Tapestry.ACTION_EVENT, event);
					return;
				}

				/*
				 * A submit element (i.e., it has a click() method)? Try that
				 * next.
				 */

				if (element.click) {
					interceptClickEvent = false;

					element.click();
				}

				/*
				 * Not a zone updater, so just do a full page refresh to the
				 * indicated URL.
				 */
				window.location = element.href;
			});
		}
	});

};