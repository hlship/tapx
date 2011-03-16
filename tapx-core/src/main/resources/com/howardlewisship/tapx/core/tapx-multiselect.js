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

		button.observe("click", function(event) {
			if (enabled) {
				callback();
			}
		});
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
		var outerDiv = $(spec.clientId);
		var hidden = new Element("input", {
			type : "hidden",
			name : spec.name
		});
		outerDiv.insert(hidden);

		var mainDiv = new Element("div", {
			"class" : "tx-multiselect-columns"
		});

		outerDiv.insert(mainDiv);

		mainDiv.update("<div class='tx-available'>"
				+ "<div class='tx-title'>Available:</div>"
				+ "<select multiple='multiple'></select></div>"
				+ "<div class='tx-controls'>"
				+ "<span class='tx-select tx-disabled' title='Select'></span>"
				+ "<span class='tx-deselect tx-disabled' title='Deselect'>"
				+ "</span></div><div class='tx-selected'>"
				+ "<div class='tx-title'>Selected:</div>"
				+ "<select multiple='multiple'></select></div>");

		var availableSelect = mainDiv.down(".tx-available > select");
		var selectedSelect = mainDiv.down(".tx-selected > select");

		(spec.model || []).each(function(row) {

			var valueId = row[0];
			var selected = (spec.values || []).include(valueId);
			var divToUpdate = selected ? selectedSelect : availableSelect;

			var option = new Element("option").update(row[1]);

			option.txValue = {
				clientValue : valueId
			};

			divToUpdate.insert(option);
		});

		function rebuildHiddenFieldValue() {
			// First array is the list of selected values (for values defined by
			// the model) Second array is the list of selected labels (for
			// values added on the client)

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

		setupButton(availableSelect, mainDiv.down(".tx-select"), function() {
			transferOptions(availableSelect, selectedSelect);
			rebuildHiddenFieldValue();

		});
		setupButton(selectedSelect, mainDiv.down(".tx-deselect"), function() {
			transferOptions(selectedSelect, availableSelect);
			rebuildHiddenFieldValue();
		});

	}

	return {
		tapxMultipleSelect : initializer
	};
});
