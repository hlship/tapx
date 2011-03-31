Tapestry.ZoneManager.addMethods({
  processReply : function(reply) {
    Tapestry.loadScriptsInReply(reply, function() {
      /*
       * In a multi-zone update, the reply.content may be missing, in which
       * case, leave the curent content in place. TAP5-1177
       */
      reply.content != undefined && this.show(reply.content);

      /*
       * zones is an object of zone ids and zone content that will be present in
       * a multi-zone update response.
       */
      reply.zones && Object.keys(reply.zones).each(function(zoneId) {
        var manager = Tapestry.findZoneManagerForZone(zoneId);

        if (manager) {
          var zoneContent = reply.zones[zoneId];
          manager.show(zoneContent);
        }
      });
    }.bind(this));
  }
});
