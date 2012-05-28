Tapestry.Initializer.tapxDateField = function (spec)
{
    /**
     * Checks if the date is outside the range
     * @param date a date to display
     * @return {Boolean} true to disable the date (if outside the range), false if date is valid
     */
    function dateStatusHandler(date)
    {
        if (spec.min && date.getTime() < spec.min) {
            return true;
        }

        if (spec.max && date.getTime() > spec.max) {
            return true;
        }

        return false;
    }

    $T(spec.clientId).calendar = Calendar.setup({
        inputField:spec.clientId,
        button:spec.clientId + "-trigger",
        weekNumbers:false,
        showsTime:spec.time,
        ifFormat:spec.clientDateFormat,
        timeFormat:spec.time && spec.clientDateFormat.match("%p") != null ? "12" : "24",
        cache:true,
        singleClick:spec.singleClick,
        dateStatusFunc:dateStatusHandler
    });
}