Tapx = {
	SetEditor : {

		updateHiddenField : function(field, state) {
			field.value = state.values.toJSON();
		},

		addItemToList : function(value, state, listElement, hiddenFieldElement) {
			var listItemElement = new Element("li")
					.update(Object.toHTML(value));

			var deleteElement = new Element("span", {
				"class" : "tx-seteditor-delete",
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
	},

	Tree : {
		animateRevealChildren : function(element) {
			$(element).addClassName("tx-tree-expanded");
			var div = $(element).up('li').down("div.tx-children");

			new Effect.SlideDown(div);
		},

		animateHideChildren : function(element) {
			$(element).removeClassName("tx-tree-expanded");
			var div = $(element).up('li').down("div.tx-children");

			new Effect.SlideUp(div);
		}
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

	textField.insert({
		after : hiddenField
	});

	var valuesColumn = textField.up(".tx-seteditor").down(".tx-valuescolumn");

	var valueList = new Element("ul");

	valuesColumn.insert(valueList);

	state.values.each(function(value) {
		Tapx.SetEditor.addItemToList(value, state, valueList, hiddenField);
	});

	textField.observe("keypress", function(event) {
		if (event.keyCode != Event.KEY_RETURN)
			return;

		event.stop();

		var value = textField.value;

		if (value == null || value.empty())
			return;

		Tapx.SetEditor.addItemToList(value, state, valueList, hiddenField);

		state.values.push(value);

		Tapx.SetEditor.updateHiddenField(hiddenField, state);

		textField.value = "";
	});
};

Tapestry.Initializer.tapxTreeNode = function(spec) {

	var loaded = false;
	var expanded = false;
	var loading = false;

	function successHandler(reply) {
		// Remove the Ajax load indicator
		$(spec.clientId).update("");
		$(spec.clientId).removeClassName("tx-empty-node");
		
		var response = reply.responseJSON;

		Tapestry.loadScriptsInReply(response, function() {
			var outerDiv = $(spec.clientId).up('li').down("div.tx-children");

			// Wrap the content inside a <div> to ensure
			// animations work correctly.

			outerDiv.update("<div>" + response.content + "</div>");

			Tapx.Tree.animateRevealChildren(spec.clientId);

			loading = false;
			loaded = true;
			expanded = true;
		});

	}

	function doLoad() {
		if (loading)
			return;

		loading = true;

		$(spec.clientId).addClassName("tx-empty-node");
		$(spec.clientId).update("<span class='tx-ajax-wait'/>");

		Tapestry.ajaxRequest(spec.expandURL, successHandler);
	}

	$(spec.clientId).observe(
			"click",
			function(event) {
				event.stop();

				if (!loaded) {

					doLoad();

					return;
				}

				// Children have been loaded, just a matter of toggling between
				// showing or hiding the children.

				var f = expanded ? Tapx.Tree.animateHideChildren
						: Tapx.Tree.animateRevealChildren;

				f.call(null, spec.clientId);

				expanded = !expanded;
			});
}