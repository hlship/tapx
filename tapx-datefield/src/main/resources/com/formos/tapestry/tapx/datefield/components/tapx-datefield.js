Tapestry.Initializer.tapxDateField = function(clientId)
{
    // TODO: Add support for date format
    Calendar.setup({
        inputField: clientId,
        button: clientId + ":trigger",
        weekNumbers : false,
        cache : true
    });
}