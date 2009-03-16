Tapestry.Initializer.tapxDateField = function(clientId, clientDateFormat)
{
    $T(clientId).calendar = Calendar.setup({
        inputField: clientId,
        button: clientId + ":trigger",
        weekNumbers : false,
        ifFormat : clientDateFormat,
        cache : true
    });
}