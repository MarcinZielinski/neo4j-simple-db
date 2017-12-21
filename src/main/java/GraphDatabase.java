import java.io.File;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

/**
 * Created by Marcin on 2017-12-21.
 */
public final class GraphDatabase {

    private static final String GRAPH_DIR_LOC = "db";

    private final GraphDatabaseService graphDatabaseService;

    public static GraphDatabase createDatabase() {
        return new GraphDatabase();
    }

    private GraphDatabase() {
        graphDatabaseService = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(new File(GRAPH_DIR_LOC))
                .setConfig(GraphDatabaseSettings.allow_upgrade, "true")
                .newGraphDatabase();
        registerShutdownHook();
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(graphDatabaseService::shutdown));
    }

    public String runCypher(final String cypher) {
        try (Transaction transaction = graphDatabaseService.beginTx()) {
            final Result result = graphDatabaseService.execute(cypher);
            transaction.success();
            return result.resultAsString();
        }
    }

    public Node createUser(String userName, String firstName, String lastName, String country, int age){
        if(firstName == null)
            return null;

        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();

            Node newNode = graphDatabaseService.createNode(Labels.USER);
            newNode.setProperty("UserName", userName);
            newNode.setProperty("FirstName", firstName);
            if(lastName != null) newNode.setProperty("LastName", lastName);
            if(country != null) newNode.setProperty("Country", country);
            if(age != 0) newNode.setProperty("Age", age);

            return newNode;
        }
    }

    public void createRelationShip(Relationships relationType, Node firstNode, Node secondNode){
        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();
            firstNode.createRelationshipTo(secondNode, relationType);
        }
    }

    public void generateSimpleGrapgh() {
        Generator.generateGraph(this, graphDatabaseService);
    }

    public void generateSampleData() {
        Generator.generateData(this, graphDatabaseService);
    }

    public Node createMovie(String title, String category) {
        if(title == null)
            return null;

        try(Transaction transaction = graphDatabaseService.beginTx()){
            transaction.success();

            Node node = graphDatabaseService.createNode(Labels.MOVIE);
            node.setProperty("Title", title);
            if(category != null) node.setProperty("Category", category);

            return node;
        }
    }
}
