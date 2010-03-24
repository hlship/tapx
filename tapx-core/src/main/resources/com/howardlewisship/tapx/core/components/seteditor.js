Tapx.SetEditor = {

	updateHiddenField : function(field, state) {
		field.value = state.values.toJSON();
	},

	addItemToList : function(value, state, listElement, hiddenFieldElement,
			deleteIcon) {
		var listItemElement = new Element("li").update(Object.toHTML(value));

		var deleteElement = new Element("img", {
			src : deleteIcon,
			title : "Delete"
		});

		deleteElement.observe("click", function(event) {
			event.stop();

			state.values = state.values.without(value);
			hiddenFieldElement.value = state.values.toJSON();

			listItemElement.remove();
		});

		listItemElement.insert(deleteElement);
		listElement.insert(listItemElement);
	}
};

Tapestry.Initializer.tapxSetEditor = function(spec) {

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
		value : state.values.toJSON()
	});

	textField.insert( {
		after : hiddenField
	});

	var valuesColumn = textField.up(".tx-seteditor").down(".tx-valuescolumn");

	var valueList = new Element("ul");

	valuesColumn.insert(valueList);

	state.values.each(function(value) {
		Tapx.SetEditor.addItemToList(value, state, valueList, hiddenField,
				spec.deleteIcon);
	});

	textField.observe("keypress", function(event) {
		if (event.keyCode != Event.KEY_RETURN)
			return;

		event.stop();

		var value = textField.value;

		if (value == null || value.empty())
			return;

		Tapx.SetEditor.addItemToList(value, state, valueList, hiddenField,
				spec.deleteIcon);

		state.values.push(value);

		Tapx.SetEditor.updateHiddenField(hiddenField, state);

		textField.value = "";
	});
};