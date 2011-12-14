import org.apache.tapestry5.test.Jetty7Runner

new Jetty7Runner("src/test/webapp", "/", 8080, 9999).start()
