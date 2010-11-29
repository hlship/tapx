Tapx.runModalDialog = function(title, message, proceed) {
	var div = new Element('div', {
		className : 'mb-confirm'
	}).update(new Element('p').update(message));

	/*
	 * Have to assign ids and reconnect at after load, because ModalBox copies
	 * DOM elements, rather than move them.
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
		title : title,
		afterLoad : function() {
			$(noId).observe("click", function(event) {
				Modalbox.hide();
			});
			$(yesId).observe("click", function(event) {
				event.stop();
				Modalbox.hide();
				proceed.defer();
			});
		}
	});
};

Tapestry.Initializer.tapxConfirm = function(spec) {

	var element = $(spec.clientId);
	var type = element.type;

	var interceptClickEvent = true;

	/*
	 * Replace the normal click event, knowing that in most cases, the original
	 * link or button has an Tapestry.ACTION_EVENT event handler to do its real
	 * work.
	 */
	element.stopObserving("click");

	function doAction() {
		if ($T(element).hasAction) {
			element.fire(Tapestry.ACTION_EVENT, event);
			return;
		}

		/*
		 * Is it a submit element (i.e., it has a click() method)? Try that
		 * next.
		 */

		if (element.click) {
			interceptClickEvent = false;

			element.click();
			return;
		}

		/*
		 * Not a zone update, so just do a full page refresh to the indicated
		 * URL.
		 */
		window.location = element.href;
	}

	element.observe("click", function(event) {

		if (interceptClickEvent) {

			event.stop();

			if ($(element).hasClassName('tx-disable-confirm')) {
				doAction();
				return;
			}

			Tapx.runModalDialog(spec.title, spec.message, doAction);
		} else {
			interceptClickEvent = true;
		}
	});

};