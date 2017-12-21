/**
 * Created by Marcin on 2017-12-21.
 */
public class Solution {

    protected final GraphDatabase graphDatabase = GraphDatabase.createDatabase();

    public void runAllTests() {
        System.out.println(graphDatabase.runCypher("MATCH (n) RETURN n")); // show all
        System.out.println(findByUserName("3Buberk3"));
        System.out.println(getAllRelationships("Konkolido123"));
        System.out.println(findShortestPath("3Buberk3", "Konkolido123"));
    }

    public void dropDatabase() {
        graphDatabase.runCypher("MATCH (n) DETACH DELETE n");
    }

    private String findByUserName(final String userName) {
        String query = String.format("MATCH (u:USER) WHERE u.UserName = \"%s\" RETURN u", userName);
        return graphDatabase.runCypher(query);
    }

    private String getAllRelationships(String userName){
        String query = String.format("MATCH (p:USER {UserName: \"%s\"}) -[]-> (m) RETURN m", userName);
        return graphDatabase.runCypher(query);
    }

    private String findShortestPath(String userName1, String userName2){
        String query = String.format("MATCH (x:USER { UserName: \"%s\" }),(m:USER { UserName: \"%s\" })," +
                " p = shortestPath((x)-[*..15]-(m)) RETURN p", userName1, userName2);
        return graphDatabase.runCypher(query);
    }
}