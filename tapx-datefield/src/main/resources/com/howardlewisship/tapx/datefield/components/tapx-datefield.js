Tapestry.Initializer.tapxDateField = function(spec)
{
    $T(spec.clientId).calendar = Calendar.setup({
        inputField: spec.clientId,
        button: spec.clientId + "-trigger",
        weekNumbers : false,
        showsTime : spec.time,
        ifFormat : spec.clientDateFormat,
        cache : true
    });
}