package com.howardlewiship.tapx.heroku;

import com.mongodb.DB;
import com.mongodb.MongoURI;
import org.apache.commons.cli.*;
import org.eclipse.jetty.nosql.mongodb.MongoSessionIdManager;
import org.eclipse.jetty.nosql.mongodb.MongoSessionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Launches Jetty to service the web application at {@code src/main/webapp}.
 */
public class JettyMain {

    private final String webappFolder;

    private final String mongoURLEnvVar;

    private final String sessionCollectionName;

    public JettyMain(String webappFolder, String mongoURLEnvVar, String sessionCollectionName) {
        this.webappFolder = webappFolder;
        this.mongoURLEnvVar = mongoURLEnvVar;
        this.sessionCollectionName = sessionCollectionName;
    }

    public void startServer() throws Exception {

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        Server server = new Server(Integer.valueOf(webPort));
        WebAppContext root = new WebAppContext();

        MongoURI mongoURI = new MongoURI(System.getenv(mongoURLEnvVar));
        DB connectedDB = mongoURI.connectDB();

        if (mongoURI.getUsername() != null) {
            connectedDB.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
        }

        MongoSessionIdManager idMgr = new MongoSessionIdManager(server, connectedDB.getCollection(sessionCollectionName));

        server.setSessionIdManager(idMgr);

        SessionHandler sessionHandler = new SessionHandler();
        MongoSessionManager mongoMgr = new MongoSessionManager();
        mongoMgr.setSessionIdManager(server.getSessionIdManager());
        sessionHandler.setSessionManager(mongoMgr);

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
        options.addOption("m", "mongo-env-var", true, "Name of environment variable storing the MongoDB URL");
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
                    commandLine.getOptionValue("mongo-env-var", "MONGOHQ_URL"),
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
