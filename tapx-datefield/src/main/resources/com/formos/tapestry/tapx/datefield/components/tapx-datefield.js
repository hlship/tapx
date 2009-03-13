Tapestry.Initializer.tapxDateField = function(clientId)
{
    // TODO: Add support for date format
    Calendar.setup({ element: clientId,
        button: clientId + ":trigger"});
}