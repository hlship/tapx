package com.howardlewisship.tapx.heroku;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoURI;
import org.apache.commons.cli.*;
import org.eclipse.jetty.nosql.mongodb.MongoSessionIdManager;
import org.eclipse.jetty.nosql.mongodb.MongoSessionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Launches Jetty to service the web application at {@code src/main/webapp}.
 */
public class JettyMain {

    private final String webappFolder;

    private final String mongoURL;

    private final String sessionCollectionName;

    public JettyMain(String webappFolder, String mongoURL, String sessionCollectionName) {
        this.webappFolder = webappFolder;
        this.mongoURL = mongoURL;
        this.sessionCollectionName = sessionCollectionName;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    private static String firstEnvValue(String... envNames) {
        for (String name : envNames) {
            String value = System.getenv(name);

            if (!isBlank(name)) {
                return value;
            }
        }

        return null;
    }

    public void startServer() throws Exception {

        String webPort = System.getenv("PORT");

        int port = isBlank(webPort) ? 8080 : Integer.parseInt(webPort);


        Server server = new Server(port);
        WebAppContext root = new WebAppContext();


        SessionHandler sessionHandler = new SessionHandler();

        if (mongoURL != null) {

            System.out.printf("Starting Jetty server, port %d, for '%s', using MongoDB URL '%s' (session collection '%s').\n", port, webappFolder, mongoURL, sessionCollectionName);

            MongoURI mongoURI = new MongoURI(mongoURL);
            DB connectedDB = mongoURI.connectDB();

            if (mongoURI.getUsername() != null) {
                connectedDB.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
            }

            DBCollection sessions = connectedDB.getCollection(sessionCollectionName);

            SessionIdManager sessionIdManager = new MongoSessionIdManager(server, sessions);

            MongoSessionManager manager = new MongoSessionManager();

            manager.setSessionIdManager(sessionIdManager);

            sessionHandler.setSessionManager(manager);

        } else {
            System.out.printf("Starting Jetty server, port %d, for '%s', using in-memory sessions only.\n", port, webappFolder);

            sessionHandler.setSessionManager(new HashSessionManager());
        }

        root.setSessionHandler(sessionHandler);

        root.setContextPath("/");
        root.setDescriptor(webappFolder + "/WEB-INF/web.xml");
        root.setResourceBase(webappFolder);
        root.setParentLoaderPriority(true);

        server.setHandler(root);

        server.start();
        server.join();
    }


    public static void main(String[] args) throws Exception {

        CommandLineParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("w", "web-app-folder", true, "Root folder of web application");
        options.addOption("m", "mongo-url", true, "MongoDB URL");
        options.addOption("c", "session-collection", true, "Name of MongoDB collection storing session data");
        options.addOption("h", "help", false, "Show command usage");

        try {
            CommandLine commandLine = parser.parse(options, args, true);

            if (!commandLine.getArgList().isEmpty()) {
                throw new ParseException("Unexpected command line arguments.");
            }

            if (commandLine.hasOption("help")) {
                showHelp(options);
                System.exit(0);
            }

            JettyMain main = new JettyMain(
                    commandLine.getOptionValue("web-app-folder", "src/main/webapp"),
                    commandLine.getOptionValue("mongo-url", firstEnvValue("MONGOHQ_URL", "MONGOLAB_URL")),
                    commandLine.getOptionValue("session-collection", "sessions"));
            main.startServer();

        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
            showHelp(options);
            System.exit(-1);
        }

    }

    private static void showHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(JettyMain.class.getName(), options);
    }

}
