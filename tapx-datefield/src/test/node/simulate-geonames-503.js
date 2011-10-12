/* Used to simulate what happens when http://ws.geonames.org/ returns a 503. */

var http = require("http");

http.createServer(function(request, response) {

  console.log("Sending 503 response for request " + request.url)

  response.statusCode = 503;
  response.end();

}).listen(8888);

console.log("Listening on port 8888")