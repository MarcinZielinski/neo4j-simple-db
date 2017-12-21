import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.Random;
import java.util.function.Function;

/**
 * Created by Marcin on 2017-12-21.
 */
public class Generator {
    private static final String[] userNames = {"Joe55", "Elena_J", "CohemanC2", "jjKopeldjj", "Lony21", "Koczy21"};
    private static final String[] firstNames = {"Jacob", "Jason", "Michael", "John", "Marshall"};
    private static final String[] lastNames = {"Rakoczy", "Piech", "Zieliński", "Gerrard", "Smith", "Marshall", "Doe"};
    private static final String[] countries = {"Poland", "Germany", "Ukraine", "Czech Republic", "Slovakia", "USA", "Denmark", "Sweden",
            "Russia", "Sri Lanka", "São Tomé and Príncipe", "Federation of Saint Kitts and Nevis",
            "Democratic Republic of the Congo", "Central African Republic"};
    private static final String[] titles = {"Despacito", "Rock obraża youtuberów", "Man's not hot"};
    private static final String[] categories = {"Animation", "Movie", "Educational"};


    public static void generateData(GraphDatabase db, GraphDatabaseService graphDatabaseService) {
        try (Transaction transaction = graphDatabaseService.beginTx()) {
            transaction.success();

            Random rand = new Random();
            Function<String[], String> r = tab -> tab[Math.abs(rand.nextInt() % tab.length)];

            int m = titles.length;
            Node[] movies = new Node[m];
            for(int i = 0; i < m; ++i) {
                movies[i] = db.createMovie(titles[i], r.apply(categories));
            }

            int n = userNames.length;
            Node[] users = new Node[n];
            for (int i = 0; i < n; ++i)
                users[i] = db.createUser(userNames[i], r.apply(firstNames), r.apply(lastNames), r.apply(countries), Math.abs(rand.nextInt() % 80) + 15);

            for (int i = 0; i < n; ++i) {
                if(rand.nextInt()%2 == 1) {
                    users[i].createRelationshipTo(users[(i + 333) % n != i ? (i + 333) % n : (i + 1) % n], Relationships.FRIENDS);
                    users[i].createRelationshipTo(users[(i * i + 5) % n != i ? (i * i + 5) % n : (i + 1) % n], Relationships.FRIENDS);
                    users[i].createRelationshipTo(movies[(i*i*i+1) % m], Relationships.RATED).setProperty("Scale", Math.abs(rand.nextInt())%10);
                    users[i].createRelationshipTo(movies[(i*i*i+1) % m], Relationships.VIEWED);
                    users[i].createRelationshipTo(movies[(i*i+123) % m], Relationships.NOT_INTERESTED);
                }
            }
        }
    }

    public static void generateGraph(GraphDatabase db, GraphDatabaseService graphDatabaseService) {
        try (Transaction tx = graphDatabaseService.beginTx()) {
            tx.success();

            Node user1 = db.createUser( "Konkolido123", "James", "Bond", "New York", 23);
            Node user2 = db.createUser("3Buberk3", "John", "Wayne", "England", 66);
            Node user3 = db.createUser("_jkm_", "Bob", "Smith", "Poland", 21);

            Node movie1 = db.createMovie("ASDFMovie", "Animation");
            Node movie2 = db.createMovie("Me in the zoo", "Movie");

            db.createFriendsRelationship(user1, user2, 2012);
            db.createFriendsRelationship(user2, user1, 2012);
            db.createFriendsRelationship(user2, user1, 2013);
            db.createFriendsRelationship(user3, user1, 2013);

            db.createRatedRelationship(user1, movie1, 10);
            db.createRatedRelationship(user1, movie2, 8);
            db.createRatedRelationship(user1, movie1, 2);

            db.createRelationship(user1, movie1, Relationships.VIEWED);
            db.createRelationship(user1, movie2, Relationships.VIEWED);
            db.createRelationship(user1, movie1, Relationships.VIEWED);
            db.createRelationship(user1, movie2, Relationships.NOT_INTERESTED);
            db.createRelationship(user2, movie1, Relationships.WANT_TO_SEE);
        }
    }
}
