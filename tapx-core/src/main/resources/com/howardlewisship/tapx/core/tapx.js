// Copyright 2011 Howard M. Lewis Ship
// Copyright 2011 Howard M. Lewis Ship
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

T5.extendInitializers(function() {

	function updateHiddenField(field, state) {
		field.value = Object.toJSON(state.values);
	}

	function addItemToList(value, state, listElement, hiddenFieldElement) {
		var listItemElement = new Element("li").update(Object.toHTML(value));

		var deleteElement = new Element("span", {
			"class" : "tx-seteditor-delete",
			title : "Delete"
		});

		deleteElement.observe("click", function(event) {
			event.stop();

			state.values = state.values.without(value);
			hiddenFieldElement.value = Object.toJSON(state.values);

			listItemElement.remove();
		});

		listItemElement.insert(deleteElement);
		listElement.insert(listItemElement);
	}

	function initializer(spec) {

		/**
		 * A shared object that is mutable; adding or removing a value updates
		 * state.values
		 */
		var state = {
			values : $A(spec.values)
		};

		var textField = $(spec.id);

		var hiddenField = new Element("input", {
			type : "hidden",
			name : spec.name,
			value : Object.toJSON(state.values)
		});

		var addButton = new Element("span", {
			"class" : "tx-seteditor-add"
		});

		textField.insert({
			after : hiddenField
		}).insert({
			after : addButton
		});

		var valuesColumn = textField.up(".tx-seteditor").down(
				".tx-valuescolumn");

		var valueList = new Element("ul");

		valuesColumn.insert(valueList);

		state.values.each(function(value) {
			addItemToList(value, state, valueList, hiddenField);
		});

		function addNewItem() {
			var value = textField.value;

			if (value == null || value.empty())
				return;

			addItemToList(value, state, valueList, hiddenField);

			state.values.push(value);

			updateHiddenField(hiddenField, state);

			textField.value = "";
		}

		textField.observe("keypress", function(event) {
			if (event.keyCode != Event.KEY_RETURN)
				return;

			event.stop();

			addNewItem();
		});

		addButton.observe("click", addNewItem);
	}

	return {
		tapxSetEditor : initializer
	};
});

Tapestry.Initializer.tapxExpando = function(spec) {

	var loaded = false;

	function zone() {
		return Tapestry.findZoneManagerForZone(spec.zoneId);
	}

	function expand(event) {
		$(spec.clientId).removeClassName("tx-collapsed").addClassName(
				"tx-expanded");
		$(spec.clientId).down("div.tx-content").show();

		if (!loaded) {
			loaded = true;
			zone().updateFromURL(spec.contentURL);
		}
	}

	function collapse(event) {
		$(spec.clientId).removeClassName("tx-expanded").addClassName(
				"tx-collapsed");
		$(spec.clientId).down("div.tx-content").hide();
	}

	$(spec.clientId).down(".tx-expand").observe("click", expand);
	$(spec.clientId).down(".tx-collapse").observe("click", collapse);
};

T5.extendInitializers(function() {

	function runModalDialog(title, message, proceed) {

		var div = new Element('div', {
			className : 'mb-confirm'
		}).update(new Element('p').update(message));

		/*
		 * Have to assign ids and reconnect at after load, because ModalBox
		 * copies DOM elements, rather than move them.
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
	}

	function initializer(spec) {

		var element = $(spec.clientId);
		var type = element.type;

		var interceptClickEvent = true;
		var capturedClickEvent;

		/*
		 * Replace the normal click event, knowing that in most cases, the
		 * original link or button has an Tapestry.ACTION_EVENT event handler to
		 * do its real work.
		 */
		element.stopObserving("click");

		function doAction() {
			if ($T(element).hasAction) {
				element.fire(Tapestry.ACTION_EVENT, capturedClickEvent);
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
			 * Not a zone update, so just do a full page refresh to the
			 * indicated URL.
			 */
			window.location = element.href;
		}

		element.observe("click", function(event) {

			if (interceptClickEvent) {

				event.stop();
				
				capturedClickEvent = event;

				if ($(element).hasClassName('tx-disable-confirm')) {
					doAction();
					return;
				}

				runModalDialog(spec.title, spec.message, doAction);
			} else {
				interceptClickEvent = true;
			}
		});
	}

	return {
		tapxConfirm : initializer
	};
});