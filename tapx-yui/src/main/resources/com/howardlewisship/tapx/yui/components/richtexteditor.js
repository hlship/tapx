Tapestry.Initializer.tapxRichTextEditor = function(spec) {

	$(document.body).addClassName("yui-skin-sam");

	var editor = new YAHOO.widget.SimpleEditor(spec.clientId, {
		dompath : false,
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
	
	$(spec.clientId).up("form").observe(Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT, function() { editor.saveHTML(); });
}