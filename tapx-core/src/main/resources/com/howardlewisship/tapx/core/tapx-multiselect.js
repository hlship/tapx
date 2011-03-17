Tapx.extendInitializer(function() {

	function setupButton(select, button, callback) {
		var enabled = false;

		function updateButton() {
			var newEnabled = select.selectedIndex >= 0;

			if (enabled != newEnabled) {
				enabled = newEnabled;

				if (enabled)
					button.removeClassName("tx-disabled");
				else
					button.addClassName("tx-disabled");
			}
		}

		select.observe("change", updateButton);
		select.observe("tapx:refreshbuttonstate", updateButton);

		function callbackIfEnabled() {
			if (enabled)
				callback();
		}

		button.observe("click", callbackIfEnabled);

		select.observe("dblclick", callbackIfEnabled);
	}

	function moveOption(option, to) {

		var before = $A(to.options).detect(function(targetOption) {
			return targetOption.innerHTML > option.innerHTML;
		});

		if (Prototype.IE) {
			if (before == null) {
				to.add(option);
			} else {
				to.add(option, before.index);
			}

			return;
		}

		to.add(option, before);
	}

	function moveOptions(movers, to) {
		movers.each(function(option) {
			moveOption(option, to);
		});
	}

	function deselectAllOptions(select) {
		$A(select.options).each(function(option) {
			option.selected = false;
		});
	}

	function removeSelectedOptions(select) {
		var movers = [];
		var options = select.options;

		for ( var i = select.selectedIndex; i < select.length; i++) {
			var option = options[i];
			if (option.selected) {
				select.remove(i--);
				movers.push(option);
			}
		}

		return movers;
	}

	function transferOptions(from, to) {
		deselectAllOptions(to);
		moveOptions(removeSelectedOptions(from), to);
		from.fire("tapx:refreshbuttonstate");
		to.fire("tapx:refreshbuttonstate");
	}

	function initializer(spec) {
		var availableSelect = $(spec.clientId);
		var outerDiv = availableSelect.up(".tx-multiselect");

		var hidden = outerDiv.down("input[type='hidden']");

		var selectedSelect = outerDiv.down(".tx-selected > select");

		(spec.model || []).each(function(row) {

			var valueId = row[0];
			var selected = (spec.values || []).include(valueId);
			var selectElement = selected ? selectedSelect : availableSelect;

			var option = new Element("option").update(row[1]);

			option.txValue = {
				clientValue : valueId
			};

			selectElement.insert(option);
		});

		function rebuildHiddenFieldValue() {
			// First array is the list of selected values (for values
			// defined by the model at initial render). Second array is the list
			// of selected labels (for values added on the client)

			var hiddenFieldValue = [ [], [] ];

			$A(selectedSelect.options).each(function(option) {
				var value = option.txValue;

				if (value.clientValue) {
					hiddenFieldValue[0].push(value.clientValue);
				} else {
					hiddenFieldValue[1].push(value.label);
				}
			});

			hidden.value = hiddenFieldValue.toJSON();
		}

		rebuildHiddenFieldValue();

		setupButton(availableSelect, outerDiv.down(".tx-select"), function() {
			transferOptions(availableSelect, selectedSelect);
			rebuildHiddenFieldValue();

		});
		setupButton(selectedSelect, outerDiv.down(".tx-deselect"), function() {
			transferOptions(selectedSelect, availableSelect);
			rebuildHiddenFieldValue();
		});

		var errorDiv = outerDiv.down('.tx-error');

		errorDiv.hide();

		var inputField = outerDiv.down('.tx-input input');

		function error(message) {
			inputField.addClassName("t-error").select();
			errorDiv.update(message).show();
		}

		function addNewOption() {
			var newLabel = inputField.value;

			inputField.removeClassName("t-error");
			errorDiv.hide().update();

			if (newLabel === "")
				return;

			var allOptions = $A(availableSelect.options).concat(
					$A(selectedSelect.options));

			if (allOptions.any(function(opt) {
				return opt.innerHTML === newLabel
			})) {
				error("Value already exists.");
				return;
			}

			deselectAllOptions(selectedSelect);

			var option = new Element("option", {
				selected : true
			}).update(newLabel);

			option.txValue = {
				label : newLabel
			};

			moveOption(option, selectedSelect);

			rebuildHiddenFieldValue();

			inputField.value = '';
			inputField.focus();
		}

		inputField.observe("change", addNewOption);

		inputField.observe("keypress", function(event) {
			if (event.keyCode != Event.KEY_RETURN)
				return;

			event.stop();

			addNewOption();
		});
	}

	return {
		tapxMultipleSelect : initializer
	};
});
