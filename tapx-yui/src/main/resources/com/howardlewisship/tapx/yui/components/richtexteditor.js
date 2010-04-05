Tapestry.Initializer.tapxRichTextEditor = function(spec) {

	$(document.body).addClassName("yui-skin-sam");

	var editor = new YAHOO.widget.SimpleEditor(spec.clientId, {
		dompath : false,
		handleSubmit : true,
		width : spec.width + "px",
		height : spec.height + "px",
		toolbar : {
			buttons : [ {
				group : 'textstyle',
				label : 'Font Style',
				buttons : [ {
					type : 'push',
					label : 'Bold',
					value : 'bold'
				}, {
					type : 'push',
					label : 'Italic',
					value : 'italic'
				}, {
					type : 'push',
					label : 'Underline',
					value : 'underline'
				} ]
			} ]
		}
	});

	editor.render();
}