Tapx = {

	/**
	 * Extends an object using a source. In the simple case, the source object's
	 * properties are overlayed on top of the destination object. In the typical
	 * case, the source parameter is a function that returns the source object
	 * ... this is to faciliate modularity and encapsulation.
	 * 
	 * @param destination
	 *            object to receive new or updated properties
	 * @param source
	 *            source object for properties, or function returning source
	 *            object
	 */
	extend : function(destination, source) {
		if (Object.isFunction(source))
			source = source();

		Object.extend(destination, source);
	},

	extendInitializer : function(source) {
		this.extend(Tapestry.Initializer, source);
	},

	/**
	 * Tapx.Tree contains configurable constants for controlling how animations
	 * of the tapx/Tree component operate.
	 */
	Tree : {

		/**
		 * Approximate time per pixel for the hide and reveal animations. The
		 * idea is to have small (few children) and large (many childen)
		 * animations operate at the same visible rate, even though they will
		 * take different amounts of time.
		 */
		ANIMATION_RATE : .005,

		/**
		 * Maximum animation time, in seconds. This is necessary for very large
		 * animations, otherwise its looks visually odd to see the child tree
		 * nodes whip down the screen.
		 */
		MAX_ANIMATION_DURATION : .5,

		/** Type of Scriptaculous effect to hide/show child nodes. */
		TOGGLE_TYPE : 'blind',

		/**
		 * Name of Scriptaculous effects queue to ensure that animations do not
		 * overwrite each other.
		 */
		QUEUE_NAME : 'tx-tree-updates'
	}
};

Tapx.extendInitializer(function() {

	function updateHiddenField(field, state) {
		field.value = state.values.toJSON();
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
			hiddenFieldElement.value = state.values.toJSON();

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
			value : state.values.toJSON()
		});

		textField.insert({
			after : hiddenField
		});

		var valuesColumn = textField.up(".tx-seteditor").down(
				".tx-valuescolumn");

		var valueList = new Element("ul");

		valuesColumn.insert(valueList);

		state.values.each(function(value) {
			addItemToList(value, state, valueList, hiddenField);
		});

		textField.observe("keypress", function(event) {
			if (event.keyCode != Event.KEY_RETURN)
				return;

			event.stop();

			var value = textField.value;

			if (value == null || value.empty())
				return;

			addItemToList(value, state, valueList, hiddenField);

			state.values.push(value);

			updateHiddenField(hiddenField, state);

			textField.value = "";
		});
	}

	return {
		tapxSetEditor : initializer
	};
});

Tapx.extendInitializer(function() {

	var cfg = Tapx.Tree;

	function doAnimate(element) {
		var sublist = $(element).up('li').down("ul");

		var dim = sublist.getDimensions();

		var duration = Math.min(dim.height * cfg.ANIMATION_RATE,
				cfg.MAX_ANIMATION_DURATION)

		new Effect.toggle(sublist, cfg.TOGGLE_TYPE, {
			duration : duration,
			queue : {
				position : 'end',
				scope : cfg.QUEUE_NAME
			}
		});
	}

	function animateRevealChildren(element) {
		$(element).addClassName("tx-tree-expanded");

		doAnimate(element);
	}

	function animateHideChildren(element) {
		$(element).removeClassName("tx-tree-expanded");

		doAnimate(element);
	}

	function initializer(spec) {
		var loaded = spec.expanded;
		var expanded = spec.expanded;
		var loading = false;

		function successHandler(reply) {
			// Remove the Ajax load indicator
			$(spec.clientId).update("");
			$(spec.clientId).removeClassName("tx-empty-node");

			var response = reply.responseJSON;

			Tapestry.loadScriptsInReply(response, function() {
				var element = $(spec.clientId).up("li");
				var label = element.down("span.tx-tree-label");

				label.insert({
					after : response.content
				});

				// Hide the new sublist so that we can animate revealing it.
				element.down("ul").hide();

				animateRevealChildren(spec.clientId);

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

			Tapestry.ajaxRequest(spec.expandChildrenURL, successHandler);
		}

		$(spec.clientId).observe("click", function(event) {
			event.stop();

			if (!loaded) {

				doLoad();

				return;
			}

			// Children have been loaded, just a matter of toggling
			// between
			// showing or hiding the children.

			var f = expanded ? animateHideChildren : animateRevealChildren;

			f.call(null, spec.clientId);

			var url = expanded ? spec.markCollapsedURL : spec.markExpandedURL;

			Tapestry.ajaxRequest(url, {});

			expanded = !expanded;
		});
	}

	return {
		tapxTreeNode : initializer
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